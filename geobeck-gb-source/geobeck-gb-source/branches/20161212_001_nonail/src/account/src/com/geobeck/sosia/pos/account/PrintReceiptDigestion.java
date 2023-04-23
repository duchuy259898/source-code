/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.master.account.MstReceiptSetting;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.*;
import java.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.text.SimpleDateFormat;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

/**
 *
 * @author Tran Thi Mai Loan
 */
public class PrintReceiptDigestion extends ArrayList<ReceiptDigestionData> {
    
    private MstReceiptSetting receiptSetting = null;
    private PrintService printer = null;
    private MstCustomer customer = null;
    private MstStaff staff = null;
    private Date nextVisit1 = null;
    private Date nextVisit2 = null;
    private String message = null;
    
    public enum ReceiptType {
        
        NORMAL, LARGE, SPECIAL
    };
    private ReceiptType receiptType = ReceiptType.NORMAL;
    //IVS_TMTrong start add 2015/10/13 Bug #43438
    private static final String REPORT_PATH_DIGESTION = "/report/EpsonReceiptDigestion1.jasper";
    //IVS_TMTrong end add 2015/10/13 Bug #43438
    private static final String REPORT_PATH_DIGESTION_LARGE = "/report/EpsonLargeReceiptDigestion.jasper";
    
    private static final String REPORT_PATH_EPSON_RIZAP = "/report/EpsonReceiptDigestionRizap.jasper";
    private static final String REPORT_PATH_STAR_RIZAP = "/report/StarReceiptDigestionRizap.jasper";
    private static final String REPORT_PATH_EPSON_RIZAP_LARGE = "/report/EpsonLargeReceiptDigestionRizap.jasper";
    private static final String REPORT_PATH_STAR_RIZAP_LARGE = "/report/StarLargeReceiptDigestionRizap.jasper";
    
    public PrintReceiptDigestion() {
        this.init();
    }
    
    public void init() {
        setReceiptSetting(new MstReceiptSetting());
        getReceiptSetting().setShop(SystemInfo.getCurrentShop());
        
        try {
            this.getReceiptSetting().load(SystemInfo.getConnection());
        } catch (SQLException e) {
        }
        
        this.setPrinter();
    }
    
    public void setReceiptSetting(MstReceiptSetting receiptSetting) {
        this.receiptSetting = receiptSetting;
    }
    
    public MstReceiptSetting getReceiptSetting() {
        return receiptSetting;
    }
    
    public PrintService getPrinter() {
        return printer;
    }
    
    public MstCustomer getCustomer() {
        return customer;
    }
    
    public void setCustomer(MstCustomer customer) {
        this.customer = customer;
    }
    
    public void setReceiptType(ReceiptType rtype) {
        receiptType = rtype;
    }
    
    public MstStaff getStaff() {
        return staff;
    }
    
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    
    public Date getNextVisit1() {
        return nextVisit1;
    }
    
    public void setNextVisit1(Date nextVisit1) {
        this.nextVisit1 = nextVisit1;
    }
    
    public Date getNextVisit2() {
        return nextVisit2;
    }
    
    public void setNextVisit2(Date nextVisit2) {
        this.nextVisit2 = nextVisit2;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    private void setPrinter() {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, aset);
        for (PrintService ps : printServices) {
            if (ps.getName().equals(this.getReceiptSetting().getPrinterName())) {
                this.printer = ps;
                System.out.println("使用レシートプリンタセット :" + this.getPrinter().toString());
            }
        }
    }
    
    public boolean canPrint() {
        return this.getPrinter() != null;
    }
    
    public boolean print() {
        return this.print(null, null);
    }
    
    public boolean print(MediaSizeName size, OrientationRequested orientation) {
        MstShop shop = this.getReceiptSetting().getShop();
        
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("group_name", SystemInfo.getGroup().getGroupName());
        param.put("shopName", shop.getShopName());
//        MstGroup g = new MstGroup();
//        g.setGroupID(shop.getGroupID());
//        try {
//            g.load(SystemInfo.getConnection());
//            g.loadData(SystemInfo.getConnection());
//        } catch (Exception e) {
//            
//        }
       // param.put("group_name", g.getGroupName());
        //IVS_LVTu start edit 2015/10/15 Bug #43436
        // 顧客名が15桁過ぎの場合は15桁まで切って表示します。
        String fullName = "";
        if (this.getCustomer() != null) {
            if ( this.getCustomer().getFullCustomerName().length() > 15 ){
                fullName = this.getCustomer().getFullCustomerName().substring(0, 14);
            }else {
                fullName = this.getCustomer().getFullCustomerName();
            }
        }
        param.put("phoneNumber", shop.getPhoneNumber().length() > 0 ? shop.getPhoneNumber() : null);
        //param.put("customerName", (this.getCustomer() == null ? "" : this.getCustomer().getFullCustomerName()));
        param.put("customerName", fullName);
        //IVS_LVTu end edit 2015/10/15 Bug #43436
        param.put("memberNo", (this.getCustomer() == null ? "" : this.getCustomer().getCustomerNo()));
        // vtbphuong start change 20150518 
        //String staffname = this.getStaff().getStaffName()[0] + this.getStaff().getStaffName()[1];
        String staffname = (  this.getStaff().getDisplayName()==null || this.getStaff().getDisplayName().equals("") ?  this.getStaff().getStaffName()[0] + this.getStaff().getStaffName()[1] :this.getStaff().getDisplayName() );
         // vtbphuong end change 20150518 
        param.put("confirmStaff", this.getStaff() == null ? "" : staffname);
        param.put("logoImage", this.getReceiptSetting().getLogoImage());
        String layout = null;
        
        if (SystemInfo.getDatabase().equals("pos_hair_rizap_bak") || SystemInfo.getDatabase().equals("pos_hair_rizap")) {
            param.put("message", this.getMessage() == null ? "" : this.getMessage());
            String sql = "";
            sql = " SELECT DISTINCT reservation_datetime :: DATE FROM data_reservation dr \n"
                    + "inner join data_reservation_detail drd on dr.shop_id = drd.shop_id \n "
                    + "and dr.reservation_no = drd.reservation_no \n "
                    + " WHERE customer_id =" + SQLUtil.convertForSQL(this.getCustomer().getCustomerID())
                    + " and dr.delete_date is null and drd.delete_date is null \n "
                    + " and drd.reservation_datetime > current_date  + interval '1 days' \n "
                    + "group by reservation_datetime \n "
                    + " order by reservation_datetime limit 2 \n ";
            try {
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql);
                if (rs.next()) {
                    this.setNextVisit1(rs.getDate("reservation_datetime"));
                    if (rs.next()) {
                        
                        this.setNextVisit2(rs.getDate("reservation_datetime"));
                    }
                }
                rs.close();
            } catch (Exception ex) {
            }
            
            if (this.getNextVisit1() != null) {
                SimpleDateFormat sdate = new SimpleDateFormat("yyyy年MM月dd日（E）");
                param.put("nextVisit1", sdate.format(this.getNextVisit1()));
                
            }
            if (this.getNextVisit2() != null) {
                SimpleDateFormat sdate = new SimpleDateFormat("yyyy年MM月dd日（E）");
                param.put("nextVisit2", sdate.format(this.getNextVisit2()));
                
            }
            switch (receiptType) {
                case NORMAL:
                    if (SystemInfo.getReceiptPrinterType().equals(1)) {
                        layout = REPORT_PATH_EPSON_RIZAP;
                    } else {
                        layout = REPORT_PATH_STAR_RIZAP;
                    }
                    break;
                case LARGE:
                    if (SystemInfo.getReceiptPrinterType().equals(1)) {
                        layout = REPORT_PATH_EPSON_RIZAP_LARGE;
                    } else {
                        layout = REPORT_PATH_STAR_RIZAP_LARGE;
                    }
                    break;
                default:
                    throw new Error("Panic!!");
            }
            
        } else {
        //IVS_TMTrong start add 2015/10/13 Bug #43438
             switch (receiptType) {
                case NORMAL:
                     layout = REPORT_PATH_DIGESTION;
                        break;
                case LARGE:
                     layout = REPORT_PATH_DIGESTION_LARGE;
                    break;
                default:
                    throw new Error("Panic!!");
             }     
        //IVS_TMTrong end add 2015/10/13 Bug #43438
        }
        InputStream report = PrintReceiptDigestion.class.getResourceAsStream(layout);
        
        ReportManager.exportReport(report, this.getPrinter(), 3, param, this, size, orientation);
        
        return true;
    }
    
    public String getReceiptDigestionSQL(Integer contractID, Integer slipNo, Integer contractDetailNo, Integer contractShopId, Integer shopId ) {
        StringBuilder sql = new StringBuilder(1000);
        
        if (contractID != null) {
            sql.append("select distinct  dc.shop_id, \n");
            sql.append("vmp.product_name, \n ");
            sql.append(" dc.product_num ::INT as co_product_num , \n ");
            sql.append(" dcd.product_num ::INT as cd_product_num , \n ");
            // vtbphuong start change 20150518 
           // sql.append(" ms.staff_name1, \n ");
            //sql.append("ms.staff_name2, \n");
            sql.append(" case when ms.display_name is  null  or ms.display_name =''  then ms.staff_name1 || ms.staff_name2    else ms.display_name  end as staff_name  , \n ");
             // vtbphuong end change 20150518 
            sql.append("dcd2.product_sum ::INT ,\n  ");
            sql.append("dc.valid_date \n  ");
            sql.append("from data_contract_digestion dcd \n ");
            sql.append("inner join ( \n");
            sql.append(" SELECT SUM(cd1.product_num) AS product_sum, cd1.contract_no, cd1.contract_shop_id FROM data_contract_digestion AS cd1 \n ");
            sql.append(" WHERE cd1.contract_no= " + SQLUtil.convertForSQL(contractID)
                    // vtbphuong start change 20140627 Bug #26172
                  //  + " and shop_id = " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID())
                      + " and contract_shop_id = " + SQLUtil.convertForSQL(contractShopId)
                      // vtbphuong end change 20140627 Bug #26172
                    + " and contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n");
            sql.append("AND cd1.delete_date IS  NULL GROUP BY  cd1.contract_no,cd1.contract_shop_id, cd1.contract_detail_no  \n ");
            sql.append(") dcd2 on dcd.contract_shop_id = dcd2.contract_shop_id and dcd.contract_no = dcd2.contract_no \n ");
            sql.append("inner join data_contract dc on dc.shop_id = dcd.contract_shop_id and dc.contract_no = dcd.contract_no  and dc.contract_detail_no = dcd.contract_detail_no \n ");
            sql.append("inner join data_sales_detail dsd on dsd.shop_id = dcd.shop_id and dsd.slip_no = dcd.slip_no and dsd.product_id = dc.product_id  and dsd.product_division=6   \n ");
            sql.append("inner join view_mst_product vmp on   vmp.product_division = dsd.product_division AND vmp.product_id = dsd.product_id \n ");
            sql.append("left join mst_staff ms on ms.staff_id = dsd.staff_id \n ");
            sql.append(" where dcd.slip_no =" + SQLUtil.convertForSQL(slipNo)
                    + " and dcd.shop_id = " + SQLUtil.convertForSQL(shopId)
                    + " AND dcd.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n");
        } else {
            sql.append("select distinct  ms.staff_name1, ms.staff_name2 , dc.product_num ::INT  as co_product_num  \n ");
            sql.append(", dsd.product_num ::INT  as cd_product_num, \n  ");
            sql.append("dcd.product_num ::INT  as product_sum \n  ");
            sql.append(", dc.valid_date , \n ");
            sql.append("vmp.product_name \n ");
            sql.append("from  \n  ");
            sql.append("data_sales ds inner join  \n  ");
            sql.append("data_sales_detail dsd on ds.slip_no = dsd.slip_no  \n  ");
            sql.append("inner join data_contract dc on ds.slip_no = dc.slip_no  and dc.product_id  = dsd.product_id  \n ");
            sql.append("inner join data_contract_digestion   dcd on dcd.slip_no = ds.slip_no   and dc.contract_no = dcd.contract_no  \n ");
            sql.append("inner join view_mst_product vmp on   vmp.product_division = dsd.product_division AND vmp.product_id = dsd.product_id  \n ");
            sql.append("left join mst_staff ms on ms.staff_id = ds.staff_id   \n ");
            sql.append("where ds.slip_no = " + SQLUtil.convertForSQL(slipNo) + "  \n ");
            sql.append("and dsd.product_division = 6 \n ");
            sql.append(" and ds.shop_id = " + SQLUtil.convertForSQL(shopId));
            sql.append("and ds.delete_date is null \n ");
        }
        return sql.toString();
    }
}

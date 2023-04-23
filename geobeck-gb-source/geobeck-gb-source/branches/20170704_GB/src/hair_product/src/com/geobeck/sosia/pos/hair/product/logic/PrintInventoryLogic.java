/*
 * PrintInventoryLogic.java
 *
 * Created on 2008/09/25, 10:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.util.SQLUtil;
import java.util.*;
import java.text.*;
import java.sql.SQLException;
import net.sf.jasperreports.engine.data.*;
import com.geobeck.sosia.pos.hair.product.beans.PrintInventoryCategoryBean;
import com.geobeck.sosia.pos.hair.product.beans.PrintInventorySubReportBean;
import com.geobeck.sosia.pos.hair.product.beans.PrintInventoryProductBean;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.util.TaxUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;

/**
 *
 * @author gloridel
 */
public class PrintInventoryLogic extends ReportGeneratorLogic {
    
    public static final int INVENTORY_FOR_SELL   = 1 ;
    public static final int INVENTORY_FOR_OPERATION  = 2;
    public static final int INVENTORY_WITH_TAX   = 1 ;
    public static final int INVENTORY_WITHOU_TAX  = 2;
    
    private MstShop     shop = null;
    private MstSupplier supplier = null;
    private Date  toDate = null;
    private Date  fromDate = null;
    private int taxtype   = 0;
    private int inventory = 0;
    //IVS_LVTu start add 2015/07/20 New request #40693
    private MstItemClass itemClass = new MstItemClass();
    // </editor-fold>

    public MstItemClass getItemClass() {
        return itemClass;
    }

    public void setItemClass(MstItemClass itemClass) {
        this.itemClass = itemClass;
    }
    ArrayList<PrintInventoryProductBean>  inventoryProductBean = new ArrayList<PrintInventoryProductBean>();
    public ArrayList<PrintInventoryProductBean> getInventoryProductBean() {
        return inventoryProductBean;
    }

    public void setInventoryProductBean(ArrayList<PrintInventoryProductBean> inventoryProductBean) {
        this.inventoryProductBean = inventoryProductBean;
    }
    //IVS_LVTu end add 2015/07/20 New request #40693

    /** Creates a new instance of PrintInventoryLogic */
    public PrintInventoryLogic(MstShop mstshop, MstSupplier mstsupplier) {
        this.shop = mstshop;
        this.supplier = mstsupplier;
    }
    //IVS_LVTu start add 2015/07/22 New request #40693
    public PrintInventoryLogic() {

    }
    //IVS_LVTu end add 2015/07/22 New request #40693
    
    public void setDateRange(Date to, Date from) {
        this.toDate   = to;
        this.fromDate = from;
    }
    
    public void setInventory(int id) {
        this.inventory = id;
    }
    
    public void setTaxCondition(int cond) {
        this.taxtype = cond;
    }
    
    private ArrayList getSubProductInfo() throws Exception {

        ArrayList<PrintInventoryProductBean>  prodlist = new ArrayList<PrintInventoryProductBean>();
        PrintInventoryProductBean prodbean = null;
        int account = 0;
        int excess = 0;
        try {
            ConnectionWrapper dbConnection = SystemInfo.getConnection();
            ResultSetWrapper rs = dbConnection.executeQuery(this.productInfoSQL());
            
            
            Double taxRate = SystemInfo.getTaxRate(new Date());            
        
            while( rs.next() ) {
                prodbean = new PrintInventoryProductBean();
                
                prodbean.setClassId(rs.getInt("item_class_id"));
                prodbean.setId(rs.getInt("item_id"));
                
                prodbean.setCategory(rs.getString("item_class_name"));
                prodbean.setStockName(rs.getString("item_name"));

                Long costPrice = rs.getLong("cost_price");
                if( this.taxtype == INVENTORY_WITHOU_TAX ){
                    Long tax = TaxUtil.getTax(costPrice, taxRate, 1);
                    costPrice = costPrice - tax;
                }
                
                prodbean.setRawProductEst(costPrice);

                if( this.taxtype == INVENTORY_WITH_TAX ){
                    prodbean.setTotalAmnt( rs.getLong("real_cost_in_tax") );
                    prodbean.setStoreInAmnt(rs.getLong("in_cost_in_tax"));
                    prodbean.setStoreOutAmnt(rs.getLong("out_cost_in_tax"));
                    prodbean.setExcessAmnt(rs.getLong("excess_cost_in_tax"));
                }else{
                    prodbean.setTotalAmnt( rs.getLong("real_cost_no_tax") );
                    prodbean.setStoreInAmnt(rs.getLong("in_cost_no_tax"));
                    prodbean.setStoreOutAmnt(rs.getLong("out_cost_no_tax"));
                    prodbean.setExcessAmnt(rs.getLong("excess_cost_no_tax"));
                }

                prodbean.setTermProd(rs.getInt("initial_stock"));
                prodbean.setActualCnt(rs.getInt("real_stock"));
                
                prodbean.setStoreInCnt(rs.getInt("in_num"));
                prodbean.setStoreAttachCnt(rs.getInt("attach_num"));
                prodbean.setStoreOutCnt(rs.getInt("out_num"));
                
                prodbean.setAccount(rs.getInt("total_cnt"));
                prodbean.setExcessCnt(rs.getInt("excess_cnt"));

                prodlist.add(prodbean);
            }  
        } catch(SQLException e) {
            e.printStackTrace();
            throw new Exception("Error report data.");
        }
        
        return prodlist;
    }
    
    public int viewInventoryReport() {
        
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        ArrayList<PrintInventoryCategoryBean> beanlist = new ArrayList<PrintInventoryCategoryBean>();
        ArrayList<PrintInventoryProductBean>  prodlist = new ArrayList<PrintInventoryProductBean>();
        ArrayList<PrintInventorySubReportBean> sublist = new ArrayList<PrintInventorySubReportBean>();
        
        PrintInventorySubReportBean subbean = new PrintInventorySubReportBean();
        SimpleDateFormat genformat  = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            prodlist = this.getSubProductInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        int classId = 0;
        
        if( prodlist.size() == 0 ){
            return RESULT_DATA_NOTHNIG;
        }
        
        PrintInventoryCategoryBean beanCat = null;
        for( int i=0; i < prodlist.size(); i++ ) {
            PrintInventoryProductBean bean  = prodlist.get(i);
            if( i == 0 || classId != bean.getClassId() ){
                if( i != 0 ){
                    beanlist.add(beanCat);
                }
                classId = bean.getClassId();
                beanCat = new PrintInventoryCategoryBean();
                
                beanCat.setCategory(bean.getCategory());
                beanCat.setTerm(bean.getTermProd());
                beanCat.setStoreInCnt(bean.getStoreInCnt());
                beanCat.setStoreAttachCnt(bean.getStoreAttachCnt());
                beanCat.setStoreInAmnt(bean.getStoreInAmnt());
                beanCat.setStoreOutCnt(bean.getStoreOutCnt());
                beanCat.setStoreOutAmnt(bean.getStoreOutAmnt());
                beanCat.setAccount(bean.getAccount());
                beanCat.setActualCnt(bean.getActualCnt());
                beanCat.setExcessCnt(bean.getExcessCnt());
                beanCat.setExcessAmnt(bean.getExcessAmnt());
                beanCat.setTotalAmnt(bean.getTotalAmnt());
            }else{
                //beanCat.setCategory(beanCat.getCategory() + bean.getCategory());
                beanCat.setTerm(beanCat.getTerm() + bean.getTermProd());
                beanCat.setStoreInCnt(beanCat.getStoreInCnt() + bean.getStoreInCnt());
                beanCat.setStoreAttachCnt(beanCat.getStoreAttachCnt() + bean.getStoreAttachCnt());
                beanCat.setStoreInAmnt(beanCat.getStoreInAmnt() + bean.getStoreInAmnt());
                beanCat.setStoreOutCnt(beanCat.getStoreOutCnt() + bean.getStoreOutCnt());
                beanCat.setStoreOutAmnt(beanCat.getStoreOutAmnt() + bean.getStoreOutAmnt());
                beanCat.setAccount(beanCat.getAccount() + bean.getAccount());
                beanCat.setActualCnt(beanCat.getActualCnt() + bean.getActualCnt());
                beanCat.setExcessCnt(beanCat.getExcessCnt() + bean.getExcessCnt());
                beanCat.setExcessAmnt(beanCat.getExcessAmnt() + bean.getExcessAmnt());
                beanCat.setTotalAmnt(beanCat.getTotalAmnt() + bean.getTotalAmnt());
            }
        }
        if( beanCat != null ){
            beanlist.add(beanCat);
        }
        
        subbean.setSubCategory(new JRBeanCollectionDataSource(beanlist));
        subbean.setSubProduct(new JRBeanCollectionDataSource(prodlist));
        sublist.add(subbean);
        
        paramMap.put("StoreName", this.shop.getShopName());
        paramMap.put("GenerateDay", genformat.format(new Date()));
        paramMap.put("Materials", this.supplier.getSupplierName());
        
        String reqDateStart = this.fromDate != null ? dateformat.format(this.fromDate) : "          ";
        String reqDateEnd = this.toDate != null ? dateformat.format(this.toDate) : "          ";
        
        switch(this.taxtype) {
            case INVENTORY_WITH_TAX:
                paramMap.put("TaxCond", "税込");
                break;
            case INVENTORY_WITHOU_TAX:
                paramMap.put("TaxCond", "税抜");
                break;
        }
        
        switch(this.inventory) {
            case INVENTORY_FOR_SELL:
                paramMap.put("Division", "店販用");
                break;
            case INVENTORY_FOR_OPERATION:
                paramMap.put("Division", "業務用");
                break;
        }
        
        JExcelApi jx = new JExcelApi("棚卸表");
        jx.setTemplateFile("/reports/棚卸表.xls");

        // ヘッダ
        jx.setValue(2, 3, paramMap.get("StoreName"));
        jx.setValue(2, 4, paramMap.get("Materials"));
        jx.setValue(2, 5, paramMap.get("Division"));
        jx.setValue(2, 6, reqDateStart + " ～ " + reqDateEnd);
        jx.setValue(2, 7, paramMap.get("TaxCond"));

        int row = 11;

        /************************************/
        // 分類集計データセット
        /************************************/
        jx.insertRow(row, beanlist.size() - 1);
        
        for (PrintInventoryCategoryBean d : beanlist) {
            jx.setValue(1, row, d.getCategory());
            jx.setValue(2, row, d.getTerm());
            jx.setValue(3, row, d.getStoreInCnt());
            jx.setValue(4, row, d.getStoreAttachCnt());
            jx.setValue(5, row, d.getStoreInAmnt());
            jx.setValue(6, row, d.getStoreOutCnt());
            jx.setValue(7, row, d.getStoreOutAmnt());
            jx.setValue(8, row, d.getAccount());
            jx.setValue(9, row, d.getActualCnt());
            jx.setValue(11, row, d.getTotalAmnt());
            jx.setValue(12, row, d.getExcessAmnt());
            
            row++;
        }

        jx.removeRow(row);

        row += 4;
        
        jx.setPrintTitlesRow(row - 2, row - 1);
        
        /************************************/
        // 商品別一覧データセット
        /************************************/
        jx.insertRow(row, prodlist.size() - 1);
        for (int i = 1; i < prodlist.size(); i++) {
            jx.mergeCells(2, row + i, 4, row + i);
        }
        
        for (PrintInventoryProductBean d : prodlist) {
            jx.setValue(1, row, d.getCategory());
            jx.setValue(2, row, d.getStockName());
            jx.setValue(5, row, d.getRawProductEst());
            jx.setValue(6, row, d.getTermProd());
            jx.setValue(7, row, d.getStoreInCnt());
            jx.setValue(8, row, d.getStoreAttachCnt());
            jx.setValue(9, row, d.getStoreInAmnt());
            jx.setValue(10, row, d.getStoreOutCnt());
            jx.setValue(11, row, d.getStoreOutAmnt());
            jx.setValue(13, row, d.getActualCnt());
            
            row++;
        }
        
        jx.openWorkbook();
        
        return RESULT_SUCCESS;
    }
    
    private String productInfoSQL() {

        //期首在庫のキーとなる日付を求める
        Calendar prevCal = Calendar.getInstance();
        if (fromDate == null) {
            prevCal.setTime(toDate);
            prevCal.add(Calendar.DATE, -1); //Toの前日が前回の棚卸終了日
        } else {
            prevCal.setTime(fromDate);
            prevCal.add(Calendar.DATE, -1); //Fromの前日が前回の棚卸終了日
        }
        
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String OutNum;
        if(inventory == 1){
            OutNum = "coalesce(out_num, 0) + coalesce(out_num2, 0) + coalesce(out_num3, 0)";
        }else{
            OutNum = "coalesce(out_num, 0) + coalesce(out_num3, 0)";
        }
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      base.item_class_id");
        sql.append("     ,base.class_seq");
        sql.append("     ,base.item_class_name");
        sql.append("     ,base.item_seq");
        sql.append("     ,base.item_id");
        sql.append("     ,base.item_name");
        sql.append("     ,coalesce(base.cost_price, 0) as cost_price");
        sql.append("     ,coalesce(initial_stock, 0) as initial_stock");
        sql.append("     ,coalesce(init_cost_in_tax, 0) as init_cost_in_tax");
        sql.append("     ,coalesce(init_cost_no_tax, 0) as init_cost_no_tax");
        sql.append("     ,coalesce(real_stock, 0)    as real_stock");
        sql.append("     ,coalesce(real_cost_in_tax, 0) as real_cost_in_tax");
        sql.append("     ,coalesce(real_cost_no_tax, 0) as real_cost_no_tax");
        sql.append("     ,coalesce(in_num, 0) as in_num");
        sql.append("     ,coalesce(attach_num, 0) as attach_num");
        sql.append("     ,coalesce(in_cost_in_tax, 0) as in_cost_in_tax");
        sql.append("     ,coalesce(in_cost_no_tax, 0) as in_cost_no_tax");
        sql.append("     ," + OutNum + " as out_num");
        
        if(inventory == 1){
            sql.append(" ,coalesce(out_cost_in_tax, 0) + coalesce(out_cost_in_tax2, 0) + coalesce(out_cost_in_tax3, 0) as out_cost_in_tax");
            sql.append(" ,coalesce(out_cost_no_tax, 0) + coalesce(out_cost_no_tax2, 0) + coalesce(out_cost_no_tax3, 0) as out_cost_no_tax");
        }else{
            sql.append(" ,coalesce(out_cost_in_tax, 0) + coalesce(out_cost_in_tax3, 0) as out_cost_in_tax");
            sql.append(" ,coalesce(out_cost_no_tax, 0) + coalesce(out_cost_no_tax3, 0) as out_cost_no_tax");
        }

        sql.append("     ,coalesce(initial_stock, 0) + coalesce(in_num, 0) + coalesce(attach_num, 0) - (" + OutNum + ") as total_cnt");
        sql.append("     ,coalesce(real_stock, 0) - (coalesce(initial_stock, 0) + coalesce(in_num, 0) + coalesce(attach_num, 0) - (" + OutNum + ")) as excess_cnt");
	
        sql.append("     ,(coalesce(real_stock, 0) - (coalesce(initial_stock, 0) + coalesce(in_num, 0) + coalesce(attach_num, 0) - (" + OutNum + "))) * coalesce(base.cost_price, 0)  as excess_cost_in_tax");
        sql.append("     ,sign((coalesce(real_stock, 0) - (coalesce(initial_stock, 0) + coalesce(in_num, 0) + coalesce(attach_num, 0) - (" + OutNum + "))) * coalesce(base.cost_price, 0)");
        sql.append("         / (1.0 + get_tax_rate(" + SQLUtil.convertForSQL(toDate) + ")))");
        sql.append("         * ceil(abs(coalesce(real_stock, 0) - (coalesce(initial_stock, 0) + coalesce(in_num, 0) + coalesce(attach_num, 0) - (" + OutNum + "))) * coalesce(base.cost_price, 0)");
        sql.append("         / (1.0 + get_tax_rate(" + SQLUtil.convertForSQL(toDate) + "))) as excess_cost_no_tax");
        
        sql.append(" from");
        
        sql.append("     (");
        sql.append("         select distinct");
        sql.append("              m_itc.item_class_id");
        sql.append("             ,m_itc.display_seq as class_seq");
        sql.append("             ,m_itc.item_class_name ");
        sql.append("             ,m_it.item_id");
        sql.append("             ,m_it.display_seq as item_seq");
        sql.append("             ,m_it.item_name");
        sql.append("             ,m_si.cost_price");
        sql.append("         from");
        sql.append("             mst_supplier_item m_si");
        
        sql.append("                 left join mst_item m_it");
        sql.append("                        on m_it.item_id = m_si.item_id");
        sql.append("                       and m_it.delete_date is null");
        sql.append("                 left join mst_item_class m_itc");
        sql.append("                        on m_itc.item_class_id = m_it.item_class_id");
        sql.append("                       and m_itc.delete_date is null");       
        sql.append("             ,mst_use_product m_up");
        sql.append("         where");
        sql.append("                 m_up.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("             and m_up.product_division = 2");
        sql.append("             and m_up.product_id = m_si.item_id");
        if (this.getItemClass().getItemClassID() != null ) {
            sql.append("        AND m_itc.item_class_id = " + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()));
        }
        if (supplier.getSupplierID() != null) {
            sql.append("         and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
        }
        sql.append("             and m_si.delete_date is null");
        
        // 未登録の場合は使用区分の条件で抽出する
        // 20130807 start change phuong
//        sql.append("                and");
//        sql.append("                 (");
//        sql.append("                     case when");
//        sql.append("                         not exists");
//        sql.append("                         (");
//        sql.append("                             select");
//        sql.append("                                 1");
//        sql.append("                             from");
//        sql.append("                                 data_inventory");
//        sql.append("                             where");
//        sql.append("                                     delete_date is null");
//        sql.append("                                 and shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
//        sql.append("                                 and inventory_division = " + SQLUtil.convertForSQL(inventory));
//        sql.append("                                 and inventory_date = " + SQLUtil.convertForSQLDateOnly(toDate));
//        sql.append("                         )");
//        sql.append("                         then m_it.item_use_division in (" + SQLUtil.convertForSQL(inventory) + ", 3)");
//        sql.append("                         else true");
//        sql.append("                     end");
//        sql.append("                 )");
//
        sql.append("                and m_it.item_use_division in (" + SQLUtil.convertForSQL(inventory) + ", 3)");
        
        sql.append("     ) base");

        //sql.append("         inner join");
        sql.append("         left join");
        // 20130807 end change phuong
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("                     ,sum(d_ivd.real_stock)    as real_stock");
        sql.append("                     ,sum(d_ivd.initial_stock * d_ivd.cost_price)  as init_cost_in_tax");
        sql.append("                     ,sum(sign(d_ivd.initial_stock * d_ivd.cost_price");
        sql.append("                         / (1.0 + get_tax_rate(d_iv.inventory_date)))");
        sql.append("                         * ceil(abs(d_ivd.initial_stock * d_ivd.cost_price");
        sql.append("                         / (1.0 + get_tax_rate(d_iv.inventory_date))))) as init_cost_no_tax");
        sql.append("                     ,sum(d_ivd.real_stock * m_si.cost_price) as real_cost_in_tax");
        sql.append("                     ,sum(sign(d_ivd.real_stock * m_si.cost_price");
        sql.append("                         / (1.0 + get_tax_rate(d_iv.inventory_date)))");
        sql.append("                         * ceil(abs(d_ivd.real_stock * m_si.cost_price");
        sql.append("                         / (1.0 + get_tax_rate(d_iv.inventory_date))))) as real_cost_no_tax");
        sql.append("                 from");
        sql.append("                      data_inventory d_iv");
        sql.append("                     ,data_inventory_detail d_ivd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ivd.item_id");
        sql.append("                                and m_si.delete_date is null");
        if (supplier.getSupplierID() != null) {
            sql.append("                            and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
        }
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_iv.inventory_id = d_ivd.inventory_id");
        sql.append("                     and d_iv.inventory_division = d_ivd.inventory_division");
        sql.append("                     and d_iv.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(toDate));
        sql.append("                     and d_ivd.delete_date is null");
        sql.append("                     and d_iv.delete_date is null");
        sql.append("                     and d_ivd.inventory_division = " + SQLUtil.convertForSQL(inventory));
        sql.append("                 group by");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("             ) s_iv");
        sql.append("             on s_iv.item_class_id = base.item_class_id");
        sql.append("            and s_iv.item_id = base.item_id");

        // 期首在庫
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      mi.item_class_id");
        sql.append("                     ,did.item_id");
        sql.append("                     ,did.real_stock as initial_stock");
        sql.append("                 from");
        sql.append("                     data_inventory di");
        sql.append("                         join data_inventory_detail did");
        sql.append("                             using (inventory_id, inventory_division)");
        sql.append("                         left join mst_item mi");
        sql.append("                                on mi.item_id = did.item_id");
        sql.append("                               and mi.delete_date is null");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = did.item_id");
        sql.append("                                and m_si.delete_date is null");
        if (supplier.getSupplierID() != null) {
            sql.append("                            and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
        }
        sql.append("                 where");
        sql.append("                         di.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("                     and did.inventory_division = " + SQLUtil.convertForSQL(inventory));
        sql.append("                     and di.inventory_date = " + SQLUtil.convertForSQLDateOnly(prevCal));
        sql.append("                     and di.delete_date IS NULL");
        sql.append("                     and did.delete_date IS NULL");
        sql.append("             ) initial");
        sql.append("             on initial.item_class_id = base.item_class_id");
        sql.append("            and initial.item_id = base.item_id");
        
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("                     ,sum(d_ssd.in_num) as in_num");
        sql.append("                     ,sum(coalesce(d_ssd.attach_num, 0)) as attach_num");
        sql.append("                     ,sum(d_ssd.in_num * d_ssd.cost_price) as in_cost_in_tax");
        sql.append("                     ,sum(sign(d_ssd.in_num * d_ssd.cost_price ");
        sql.append("                         / (1.0 + get_tax_rate(d_ss.store_date))) ");
        sql.append("                         * ceil(abs(d_ssd.in_num * d_ssd.cost_price ");
        sql.append("                         / (1.0 + get_tax_rate(d_ss.store_date))))) as in_cost_no_tax");
        sql.append("                 from");
        sql.append("                      data_slip_store d_ss");
        sql.append("                     ,data_slip_store_detail d_ssd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ssd.item_id");
        sql.append("                                and m_si.delete_date is null");
        if (supplier.getSupplierID() != null) {
            sql.append("                            and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
        }
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_ss.shop_id = d_ssd.shop_id");
        sql.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sql.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        if( fromDate != null ){
            sql.append("                 and d_ss.store_date >= " + SQLUtil.convertForSQL(fromDate));
        }
        sql.append("                     and d_ss.store_date <= " + SQLUtil.convertForSQL(toDate));
        sql.append("                     and d_ss.delete_date is null");
        sql.append("                     and d_ssd.delete_date is null");
        sql.append("                     and d_ssd.item_use_division = " + SQLUtil.convertForSQL(inventory));
        sql.append("                 group by");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("             ) s_store");
        sql.append("             on s_store.item_class_id = base.item_class_id");
        sql.append("            and s_store.item_id = base.item_id ");

        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("                     ,sum(d_ssd.out_num) as out_num");
        sql.append("                     ,sum(d_ssd.out_num * d_ssd.cost_price) as out_cost_in_tax");
        sql.append("                     ,sum(sign(d_ssd.out_num * d_ssd.cost_price ");
        sql.append("                         / (1.0 + get_tax_rate(d_ss.ship_date))) ");
        sql.append("                         * ceil(abs(d_ssd.out_num * d_ssd.cost_price ");
        sql.append("                         / (1.0 + get_tax_rate(d_ss.ship_date))))) as out_cost_no_tax");
        sql.append("                 from");
        sql.append("                      data_slip_ship d_ss");
        sql.append("                     ,data_slip_ship_detail d_ssd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ssd.item_id");
        sql.append("                                and m_si.delete_date is null");
        if (supplier.getSupplierID() != null) {
            sql.append("                            and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
        }
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_ss.shop_id = d_ssd.shop_id");
        sql.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sql.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        if( fromDate != null ){
            sql.append("                 and d_ss.ship_date >= " + SQLUtil.convertForSQL(fromDate));
        }
        sql.append("                     and d_ssd.item_use_division = " + SQLUtil.convertForSQL(inventory));
        sql.append("                     and d_ss.ship_date <= " + SQLUtil.convertForSQL(toDate));
        sql.append("                     and d_ss.delete_date is null");
        sql.append("                     and d_ssd.delete_date is null");
        sql.append("                 group by");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("                 order by");
        sql.append("                      m_it.item_class_id");
        sql.append("                     ,m_it.item_id");
        sql.append("             ) s_slip");
        sql.append("             on s_slip.item_class_id = base.item_class_id");
        sql.append("            and s_slip.item_id = base.item_id ");
        
        if(inventory == 1){
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      m_it.item_id");
            sql.append("                     ,sum(d_sd.product_num) as out_num2");
            sql.append("                     ,sum(d_sd.product_num * m_si.cost_price) as out_cost_in_tax2");
            sql.append("                     ,sum(sign(d_sd.product_num * m_si.cost_price");
            sql.append("                         / (1.0 + get_tax_rate(d_s.sales_date))) ");
            sql.append("                         * ceil(abs(d_sd.product_num * m_si.cost_price");
            sql.append("                         / (1.0 + get_tax_rate(d_s.sales_date))))) as out_cost_no_tax2");
            sql.append("                 from");
            sql.append("                      data_sales d_s");
            sql.append("                     ,data_sales_detail d_sd");
            sql.append("                         inner join mst_supplier_item m_si");
            sql.append("                                 on m_si.item_id = d_sd.product_id");
            sql.append("                                and m_si.delete_date is null");
            if (supplier.getSupplierID() != null) {
                sql.append("                                and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
            }
            sql.append("                         left join mst_item m_it");
            sql.append("                                on m_it.item_id = m_si.item_id");
            sql.append("                               and m_it.delete_date is null");
            sql.append("                 where");
            sql.append("                         d_s.shop_id = d_sd.shop_id");
            sql.append("                     and d_s.slip_no = d_sd.slip_no");
            sql.append("                     and d_s.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
            if( fromDate != null ){
                sql.append("                 and d_s.sales_date >= " + SQLUtil.convertForSQL(fromDate));
            }
            sql.append("                     and 1 = 1");
            sql.append("                     and product_division = 2");
            sql.append("                     and d_s.sales_date <= " + SQLUtil.convertForSQL(toDate));
            sql.append("                     and d_s.delete_date is null");
            sql.append("                     and d_sd.delete_date is null");
            sql.append("                 group by");
            sql.append("                     m_it.item_id");
            sql.append("             ) sales");
            sql.append("             on sales.item_id = base.item_id");
        }
        
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_it.item_id");
        sql.append("                     ,sum(d_ssd.item_num) as out_num3");
        sql.append("                     ,sum(d_ssd.item_num * m_si.cost_price) as out_cost_in_tax3");
        sql.append("                     ,sum(sign(d_ssd.item_num * m_si.cost_price");
        sql.append("                         / (1.0 + get_tax_rate(d_ss.sales_date)))");
        sql.append("                         * ceil(abs(d_ssd.item_num * m_si.cost_price");
        sql.append("                         / (1.0 + get_tax_rate(d_ss.sales_date))))) as out_cost_no_tax3");
        sql.append("                 from");
        sql.append("                      data_staff_sales d_ss");
        sql.append("                     ,data_staff_sales_detail d_ssd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ssd.item_id");
        sql.append("                                and m_si.delete_date is null");
        if (supplier.getSupplierID() != null) {
            sql.append("                            and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()));
        }
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_ss.shop_id = d_ssd.shop_id");
        sql.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sql.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        if( fromDate != null ){
            sql.append("                 and d_ss.sales_date >= " + SQLUtil.convertForSQL(fromDate));
        }
        sql.append("                     and d_ssd.item_use_division = " + SQLUtil.convertForSQL(inventory));
        sql.append("                     and d_ss.sales_date <= " + SQLUtil.convertForSQL(toDate) + "");
        sql.append("                     and d_ss.delete_date is null");
        sql.append("                     and d_ssd.delete_date is null");
        sql.append("                 group by");
        sql.append("                     m_it.item_id");
        sql.append("             ) staff");
        sql.append("             on staff.item_id = base.item_id");

        sql.append(" order by");
        sql.append("      base.class_seq");
        sql.append("     ,base.item_class_id");
        sql.append("     ,base.item_seq");
        sql.append("     ,base.item_id");
        return sql.toString();
        
    }
    //IVS_LVTu start add 2015/07/22 New request #40693
    public int viewInventoryReport2() {
        
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        ArrayList<PrintInventoryCategoryBean> beanlist = new ArrayList<PrintInventoryCategoryBean>();
        ArrayList<PrintInventoryProductBean>  prodlist = new ArrayList<PrintInventoryProductBean>();
        ArrayList<PrintInventorySubReportBean> sublist = new ArrayList<PrintInventorySubReportBean>();
        
        PrintInventorySubReportBean subbean = new PrintInventorySubReportBean();
        SimpleDateFormat genformat  = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            prodlist = this.getInventoryProductBean();
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        int classId = 0;
        
        if( prodlist.size() == 0 ){
            return RESULT_DATA_NOTHNIG;
        }
        
        PrintInventoryCategoryBean beanCat = null;
        for( int i=0; i < prodlist.size(); i++ ) {
            PrintInventoryProductBean bean  = prodlist.get(i);
            if( i == 0 || classId != bean.getClassId() ){
                if( i != 0 ){
                    beanlist.add(beanCat);
                }
                classId = bean.getClassId();
                beanCat = new PrintInventoryCategoryBean();
                
                beanCat.setCategory(bean.getCategory());
                beanCat.setTerm(bean.getTermProd());
                beanCat.setStoreInCnt(bean.getStoreInCnt());
                beanCat.setStoreAttachCnt(bean.getStoreAttachCnt());
                beanCat.setStoreInAmnt(bean.getStoreInAmnt());
                beanCat.setStoreOutCnt(bean.getStoreOutCnt());
                beanCat.setStoreOutAmnt(bean.getStoreOutAmnt());
                beanCat.setAccount(bean.getAccount());
                beanCat.setActualCnt(bean.getActualCnt());
                beanCat.setExcessCnt(bean.getExcessCnt());
                beanCat.setExcessAmnt(bean.getExcessAmnt());
                beanCat.setTotalAmnt(bean.getTotalAmnt());
            }else{
                //beanCat.setCategory(beanCat.getCategory() + bean.getCategory());
                beanCat.setTerm(beanCat.getTerm() + bean.getTermProd());
                beanCat.setStoreInCnt(beanCat.getStoreInCnt() + bean.getStoreInCnt());
                beanCat.setStoreAttachCnt(beanCat.getStoreAttachCnt() + bean.getStoreAttachCnt());
                beanCat.setStoreInAmnt(beanCat.getStoreInAmnt() + bean.getStoreInAmnt());
                beanCat.setStoreOutCnt(beanCat.getStoreOutCnt() + bean.getStoreOutCnt());
                beanCat.setStoreOutAmnt(beanCat.getStoreOutAmnt() + bean.getStoreOutAmnt());
                beanCat.setAccount(beanCat.getAccount() + bean.getAccount());
                beanCat.setActualCnt(beanCat.getActualCnt() + bean.getActualCnt());
                beanCat.setExcessCnt(beanCat.getExcessCnt() + bean.getExcessCnt());
                beanCat.setExcessAmnt(beanCat.getExcessAmnt() + bean.getExcessAmnt());
                beanCat.setTotalAmnt(beanCat.getTotalAmnt() + bean.getTotalAmnt());
            }
        }
        if( beanCat != null ){
            beanlist.add(beanCat);
        }
        
        subbean.setSubCategory(new JRBeanCollectionDataSource(beanlist));
        subbean.setSubProduct(new JRBeanCollectionDataSource(prodlist));
        sublist.add(subbean);
        
        paramMap.put("StoreName", this.shop.getShopName());
        paramMap.put("GenerateDay", genformat.format(new Date()));
        paramMap.put("Materials", this.supplier.getSupplierName());
        
        String reqDateStart = this.fromDate != null ? dateformat.format(this.fromDate) : "          ";
        String reqDateEnd = this.toDate != null ? dateformat.format(this.toDate) : "          ";
        
        switch(this.taxtype) {
            case INVENTORY_WITH_TAX:
                paramMap.put("TaxCond", "税込");
                break;
            case INVENTORY_WITHOU_TAX:
                paramMap.put("TaxCond", "税抜");
                break;
        }
        
        switch(this.inventory) {
            case INVENTORY_FOR_SELL:
                paramMap.put("Division", "店販用");
                break;
            case INVENTORY_FOR_OPERATION:
                paramMap.put("Division", "業務用");
                break;
        }
        
        JExcelApi jx = new JExcelApi("棚卸表");
        jx.setTemplateFile("/reports/棚卸表.xls");

        // ヘッダ
        jx.setValue(2, 3, paramMap.get("StoreName"));
        jx.setValue(2, 4, paramMap.get("Materials"));
        jx.setValue(2, 5, paramMap.get("Division"));
        jx.setValue(2, 6, reqDateStart + " ～ " + reqDateEnd);
        jx.setValue(2, 7, paramMap.get("TaxCond"));

        int row = 11;

        /************************************/
        // 分類集計データセット
        /************************************/
        jx.insertRow(row, beanlist.size() - 1);
        
        for (PrintInventoryCategoryBean d : beanlist) {
            jx.setValue(1, row, d.getCategory());
            jx.setValue(2, row, d.getTerm());
            jx.setValue(3, row, d.getStoreInCnt());
            jx.setValue(4, row, d.getStoreAttachCnt());
            jx.setValue(5, row, d.getStoreInAmnt());
            jx.setValue(6, row, d.getStoreOutCnt());
            jx.setValue(7, row, d.getStoreOutAmnt());
            jx.setValue(8, row, d.getAccount());
            jx.setValue(9, row, d.getActualCnt());
            jx.setValue(11, row, d.getTotalAmnt());
            jx.setValue(12, row, d.getExcessAmnt());
            
            row++;
        }

        jx.removeRow(row);

        row += 4;
        
        jx.setPrintTitlesRow(row - 2, row - 1);
        
        /************************************/
        // 商品別一覧データセット
        /************************************/
        jx.insertRow(row, prodlist.size() - 1);
        for (int i = 1; i < prodlist.size(); i++) {
            jx.mergeCells(2, row + i, 4, row + i);
        }
        
        for (PrintInventoryProductBean d : prodlist) {
            jx.setValue(1, row, d.getCategory());
            jx.setValue(2, row, d.getStockName());
            jx.setValue(5, row, d.getRawProductEst());
            jx.setValue(6, row, d.getTermProd());
            jx.setValue(7, row, d.getStoreInCnt());
            jx.setValue(8, row, d.getStoreAttachCnt());
            jx.setValue(9, row, d.getStoreInAmnt());
            jx.setValue(10, row, d.getStoreOutCnt());
            jx.setValue(11, row, d.getStoreOutAmnt());
            jx.setValue(13, row, d.getActualCnt());
            
            row++;
        }
        
        jx.openWorkbook();
        
        return RESULT_SUCCESS;
    }
    //IVS_LVTu end add 2015/07/22 New request #40693
}

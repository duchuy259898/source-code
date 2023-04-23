/*
 * StaffSalesListLogic.java
 *
 * Created on 2008/09/25, 20:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.util.SQLUtil;
import java.util.*;
import java.io.*;
import java.text.*;
// use for JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.product.beans.StaffSalesListBean;
import java.util.logging.Level;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
/**
 *
 * @author trino
 */
public class StaffSalesListLogic extends ReportGeneratorLogic
{
    // <editor-fold defaultstate="collapsed" desc="Definitions and Variables">
    private static String REPORT_STAFFSALESLIST           = "StaffSalesList";
    private static String REPORT_STAFFSALESLIST_JASPER    = "StaffSalesList.jasper";
    
    private final String TAX_INCLUDED_STR = "税込";
    private final String TAX_EXCLUDED_STR = "税抜";
    private MstShop shop= null;
    private Date toDate = null;
    private Date fromDate = null;
    private int  filetype = 0;
    private int  taxtype = 0;
    private MstStaff staff  = null;
     // </editor-fold>
    
    /** Creates a new instance of StaffSalesListLogic */
    public StaffSalesListLogic(MstShop shopValue,         // Shop 
             MstStaff staffValue,                             // スタッフ
             Date todate,                                     // 期間
             Date fromdate,                                   //期間
             int tax,                                        // 税
             int file)                                       //出力ファイル
    {
        this.shop = shopValue;
        this.staff = staffValue;
        this.toDate = todate;
        this.fromDate = fromdate;
        this.filetype = file;
        this.taxtype = tax;
    }
    
    
    private ArrayList getStaffInfoList() throws Exception
    {
        ArrayList<StaffSalesListBean> list = new ArrayList<StaffSalesListBean>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        try
        {
            ConnectionWrapper dbConnection = SystemInfo.getConnection();
            ResultSetWrapper rs = dbConnection.executeQuery(this.staffsalesSQL());
            while(rs.next())
            {
                StaffSalesListBean bean = new StaffSalesListBean();
                bean.setDateReq(dateFormat.format(rs.getDate("date")));
                bean.setStaffNo(rs.getInt("sid"));
                bean.setStaffName(rs.getString("sname1") + " " + rs.getString("sname2"));
                bean.setCategory(rs.getString("cname"));
                bean.setProductName(rs.getString("iname"));
                bean.setDiscount(rs.getLong("dvalue"));
                bean.setVolume(rs.getInt("count"));
                switch(this.taxtype)
                {
                    case BackProductLogic.TAX_INCLUDED:
                        bean.setEstimate(rs.getLong("taxinEst"));
                        bean.setAmount(rs.getLong("totalTaxIn"));
                        bean.setTotal(rs.getLong("itemTax"));
                        break;
                    case BackProductLogic.TAX_EXCLUDED:
                        bean.setEstimate(rs.getLong("taxnoEst"));
                        bean.setAmount(rs.getLong("totalNoTax"));
                        bean.setTotal(rs.getLong("itemNoTax"));
                        break;
                }
                
                list.add(bean);
            }
            
        }
        catch( Exception e)
        {
             SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
             throw new Exception("Data Error.");
        }
        
        return list;
    }
    
    
    public int viewStaffSalesReport()
    {
        
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat genFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm");
   
        paramMap.put("StoreName", this.shop.getShopName());
        paramMap.put("ReqDateStart", dateFormat.format(this.fromDate));
        paramMap.put("ReqDateEnd", dateFormat.format(this.toDate));
        paramMap.put("GeneratedDate", genFormat.format(new Date()));
        if( this.staff != null )
        {
            paramMap.put("StaffName", this.staff.getStaffName(0) + " " + this.staff.getStaffName(1));
        }
        else
        {
            paramMap.put("StaffName", "指定なし");
        }
        
        switch(this.taxtype)
        {
            case BackProductLogic.TAX_INCLUDED:
                paramMap.put("TaxCond", TAX_INCLUDED_STR);
                break;
            case BackProductLogic.TAX_EXCLUDED:
                paramMap.put("TaxCond", TAX_EXCLUDED_STR);
                break;
        }
        try {
            ArrayList<StaffSalesListBean> list = this.getStaffInfoList();
            if( list.size() == 0 ) return RESULT_DATA_NOTHNIG;
            this.generateFile(list, paramMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        return RESULT_SUCCESS;
    }
    
    private void generateFile(Collection collection, HashMap<String,Object> paramMap)
    {
        try
        {       
            JasperReport jasperReport = this.loadReport(REPORT_STAFFSALESLIST_JASPER, REPORT_FILE_TYPE_JASPER);
            JasperPrint  jasperPrint  = JasperFillManager.fillReport(jasperReport, paramMap, new JRBeanCollectionDataSource(collection));
            switch(this.filetype)
            {
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile(REPORT_STAFFSALESLIST_JASPER, jasperPrint);
                    break;
                case EXPORT_FILE_XLS:      
                    this.generateAndPreviewXLSFile(REPORT_STAFFSALESLIST_JASPER, jasperPrint);
                    break;
            }
          
        }
        catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    
    private String staffsalesSQL()
    {
        StringBuffer buffer = new StringBuffer();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        
        buffer.append("select \n");
        buffer.append("ss.sales_date as date, \n");
        buffer.append("ss.staff_id as sid, \n");
        buffer.append("ms.staff_name1 as sname1, ms.staff_name2 as sname2,\n");
        buffer.append("mic.item_class_name as cname,\n");
        buffer.append("mi.item_name as iname,\n"); 
        buffer.append("mi.price as taxinEst, \n");
        buffer.append("sign(mi.price/(1.0 + get_tax_rate(ss.sales_date))) * \n");
        buffer.append("ceil(abs(mi.price/(1.0 + get_tax_rate(ss.sales_date)))) as taxnoEst,\n");
        buffer.append("sd.item_num as count, \n" );
        buffer.append("sd.item_num*mi.price as totalTaxIn,");
        buffer.append("sign(sd.item_num*mi.price/(1.0 + get_tax_rate(ss.sales_date))) * \n"); 
        buffer.append("ceil(abs(sd.item_num*mi.price/(1.0 + get_tax_rate(ss.sales_date)))) as totalNoTax,\n");
        buffer.append("sd.discount_value as dvalue,\n");
        buffer.append("sd.item_value as itemTax,\n");
        buffer.append("sign(sd.item_value/(1.0 + get_tax_rate(ss.sales_date))) * \n");
        buffer.append("ceil(abs(sd.item_value/(1.0 + get_tax_rate(ss.sales_date)))) as itemNoTax \n");
        buffer.append("from \n"); 
        buffer.append("data_staff_sales ss inner join data_staff_sales_detail sd on \n");
        buffer.append("ss.slip_no = sd.slip_no,\n");
        buffer.append("mst_staff ms, mst_item mi , mst_item_class mic \n");
        buffer.append("where \n");
        buffer.append(" ss.shop_id  = " + SQLUtil.convertForSQL(shop.getShopID()) + " and ss.delete_date is null and ss.shop_id = sd.shop_id and sd.delete_date is null and \n");
        if( this.staff != null )
        {
            buffer.append("ss.staff_id = " + this.staff.getStaffID() + " and \n"); 
        }
        buffer.append("ss.sales_date between '"+ dateformat.format(this.fromDate)+ "' and '" +  dateformat.format(this.toDate)+"' and \n");
        buffer.append("ss.staff_id = ms.staff_id and ms.delete_date is null and \n");
        buffer.append("sd.item_id = mi.item_id and mi.delete_date is null and   \n");
        buffer.append("mi.item_class_id = mic.item_class_id and mic.delete_date is null \n");
        
        return buffer.toString();
    }
}

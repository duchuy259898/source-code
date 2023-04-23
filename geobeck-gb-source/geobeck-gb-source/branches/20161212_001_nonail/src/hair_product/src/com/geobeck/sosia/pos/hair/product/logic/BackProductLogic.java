/*
 * BackProductLogic.java
 *
 * Created on 2008/09/25, 16:27
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
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.hair.product.beans.BackProductBean;
import java.util.logging.Level;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;

/**
 *
 * @author trino
 */
public class BackProductLogic extends ReportGeneratorLogic {
    // <editor-fold defaultstate="collapsed" desc="Definitions and Variables">
    public static String REPORT_BACKPRODUCT_JASPER                  = "BackList.jasper";
    public static String REPORT_BACKPRODUCT                         = "BackList";
    public static final int  TAX_INCLUDED                          =  1;
    public static final int  TAX_EXCLUDED                          =  2;
    
    private final String TAX_INCLUDED_STR                            = "税込";
    private final String TAX_EXCLUDED_STR                           = "税抜";
    
    private final String newline = "\n";
    private final int BACK_DIVISION = 4;
    private int shopId;
    private String shopName;
    private Date toDate = null;
    private Date fromDate = null;
    private int reportType = 0;
    private int taxtype  = 0;
    
    // </editor-fold>
    
    /** Creates a new instance of BackProductLogic */
    public BackProductLogic( int id,              // Shop ID
            Date todate,         //集計期間
            Date fromdate,       //集計期間
            int filetype)       // 出力ファイル　PDF/XLS
    {
        this.shopId = id;
        this.toDate = todate;
        this.fromDate = fromdate;
        this.reportType = filetype;
    }
    
    public void setTaxType(int type) {
        this.taxtype = type;
    }
    
    public void setShopName(String name) {
        this.shopName = name;
    }
    
    private ArrayList<BackProductBean> getBackProductList() throws Exception {
        ArrayList<BackProductBean> beanlist = new ArrayList<BackProductBean>();
        BackProductBean bean = null;
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy/MM/dd");
        
        try {
            ConnectionWrapper  dbConnection = SystemInfo.getConnection();
            ResultSetWrapper rs = dbConnection.executeQuery(this.backProductSQL());
            
            while(rs.next()) {
                bean = new BackProductBean();
                bean.setDateReq(dateFormat.format(rs.getDate("date")));       //日付
                bean.setCustomerNo(rs.getInt("id"));                           //顧客NO
                bean.setCustomerName(rs.getString("cname1") +  "　" + rs.getString("cname2"));  //顧客名
                bean.setCategory(rs.getString("cname"));          //分類
                bean.setProductName(rs.getString("pname"));       //商品名
                bean.setEstimate(Math.abs(rs.getLong("value")));             //単価
                bean.setVolume(rs.getInt("vol"));                  //数量
                switch(this.taxtype) {
                    case TAX_INCLUDED:
                        bean.setAmount(Math.abs(rs.getLong("taxin")));
                        break;
                    case TAX_EXCLUDED:
                        bean.setAmount(Math.abs(rs.getLong("taxno")));
                        break;
                }
                beanlist.add(bean);
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            throw new Exception("Data Error.");
        }
        
        return beanlist;
    }
    
    public int viewBackProductReport() {
        
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        
        
        Date date = new Date();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat genDate  = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        
        
        
        paramMap.put("StoreName", this.shopName);
        paramMap.put("GeneratedDate", genDate.format(date));
        paramMap.put("ReqDateStart", dateFormat.format(this.fromDate));
        paramMap.put("ReqDateEnd", dateFormat.format(this.toDate));
        switch(this.taxtype) {
            case TAX_INCLUDED:
                paramMap.put("TaxCond", TAX_INCLUDED_STR);
                break;
            case TAX_EXCLUDED:
                paramMap.put("TaxCond", TAX_EXCLUDED_STR);
                break;
        }
        ArrayList<BackProductBean> beanlist;
        try {
            beanlist = this.getBackProductList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        if( beanlist.size() == 0 ){
            return RESULT_DATA_NOTHNIG;
        }
            
        this.generateFile(beanlist, paramMap, this.reportType);
        return RESULT_SUCCESS;
    }
    
    
    //PDF/Excel ファイル作成
    private void generateFile(Collection collection, HashMap<String, Object> paramMap, int fileType) {
        try {
            JasperReport jasperReport = this.loadReport(REPORT_BACKPRODUCT_JASPER, this.REPORT_FILE_TYPE_JASPER);
            JasperPrint  jasperPrint  = JasperFillManager.fillReport(jasperReport, paramMap, new JRBeanCollectionDataSource(collection));
            switch(fileType) {
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile(REPORT_BACKPRODUCT, jasperPrint);
                    break;
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile(REPORT_BACKPRODUCT, jasperPrint);
                    break;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private String backProductSQL() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        
        String sql =  " SELECT sv.sales_date as date ,sv.customer_id as id, sd.product_value as value, SUM(sd.product_num) as vol,"  + newline +
                " SUM(sd.detail_value_in_tax) as taxin, SUM(sd.detail_value_no_tax) as taxno, " + newline +
                " mc.customer_name1 as cname1, mc.customer_name2 as cname2," + newline +
                " mic.item_class_name as cname, mi.item_name as pname " + newline +
                " FROM view_data_sales_valid sv inner join view_data_sales_detail_valid sd on" + newline +
                " sv.slip_no = sd.slip_no," + newline +
                " mst_customer mc, mst_item mi, mst_item_class mic " + newline +
                " WHERE sv.shop_id = " + SQLUtil.convertForSQL(this.shopId) + " and sv.shop_id = sd.shop_id and " + newline +
                " sv.sales_date between '" + dateformat.format(this.fromDate)+"' and '" + dateformat.format(this.toDate)+ "' and " + newline +
                " sd.product_division = " + BACK_DIVISION + " and sv.customer_id = mc.customer_id and " + newline +
                " mc.delete_date is null and sd.product_id = mi.item_id and mi.delete_date is null and " + newline +
                " mi.item_class_id = mic.item_class_id and mic.delete_date is null " + newline +
                " group by sv.sales_date,  sv.customer_id,  sd.product_value, mc.customer_name1,mc.customer_name2," + newline +
                "mic.item_class_name ,  mi.item_name  " ;
        return sql;
    }
}

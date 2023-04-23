/*
 * MedicalRecordAnalysisLogic.java
 *
 * Created on 2006/05/18, 21:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.BusinessConsumptionReportBean;
import com.geobeck.sosia.pos.hair.report.beans.BusinessConsumptionReportCourseBean;
import com.geobeck.sosia.pos.hair.report.beans.BusinessCourseReportBean;
import com.geobeck.sosia.pos.hair.report.beans.BusinessCourseReportCourseBean;
import com.geobeck.sosia.pos.hair.report.beans.StaffCourseConsumptionBean;
import com.geobeck.sosia.pos.hair.report.beans.StaffCourseConsumptionResultBean;
import com.geobeck.sosia.pos.hair.report.beans.StaffCourseContractBean;
import com.geobeck.sosia.pos.hair.report.beans.StaffCourseContractResultBean;
import com.geobeck.sosia.pos.hair.report.util.*;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstShopSetting;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.report.bean.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.*;
import com.lowagie.tools.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import jxl.CellView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * レポートロジック
 *
 * @author k.anayama
 */
public class MedicalRecordAnalysisLogic {

    public static int REPORT_TYPE_PDF = 0;
    public static int REPORT_TYPE_EXCEL = 1;
    public static int STAFF_REPORT_COLUMN_COUNT = 6;
    public static String REPORT_RESOURCE_PATH = "/reports/";
    public static String REPORT_EXPORT_PATH = "./";
    public static String REPORT_XML_FILE_EXT = ".jasper";
    public static String REPORT_FILE_STAFF_TECHNICAL = "StaffReportTechnic";
    public static String REPORT_FILE_STAFF_TECHNICAL_CONTINUE = "StaffReportTechnicContinue";
    public static String REPORT_FILE_STAFF_GOODS = "StaffReportItem";
    public static String REPORT_FILE_STAFF_GOODS_CONTINUE = "StaffReportItemContinue";
    public static String REPORT_FILE_STAFF_CUSTOMER_SALES = "StaffReportCustomerSales";
    public static String REPORT_FILE_OCCUPATION_RATIO = "StaffReportOccupationRatio";
    public static String REPORT_FILE_PROPORTIONALLY_DISTRIBUTION = "StaffReportPropDistribution";
    public static int ORDER_DISPLAY_KINGAKU = 0;
    public static int ORDER_DISPLAY_POINT = 1;
    //Excel１シート当りの按分数
    public static int PROPORTIONALLY_COUNT_OF_ONE_EXCEL_SHEET = 15;
    //按分成績のソート用
    private int proportionallyOrderDisplay;
    public static String year ="";
    

    /**
     * コンストラクタ
     */
    public MedicalRecordAnalysisLogic() {
    }

    //IVS end add 来店サイクル分析処理する。20121127
    /*　
     * @param paramBean
     * @return アクティブ顧客分析処理する。
     * @throws Exception 
     */
    //IVS start add アクティブ顧客分析処理する。20121127
    public boolean MedicalRecordAnalysis(ReportParameterBean paramBean) throws Exception {
        //パーラーメーター処理
        String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());
        String endDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetEndDateObj());
        String startDate1 = String.format("%1$tY年%1$tm月", paramBean.getTargetStartDateObj());
        String endDate1 = String.format("%1$tY年%1$tm月", paramBean.getTargetEndDateObj());
        String yearStart = String.format("%1$tY", paramBean.getTargetStartDateObj());
        String yearEnd = String.format("%1$tY", paramBean.getTargetEndDateObj());
        int monthStart = Integer.parseInt(String.format("%1$tm", paramBean.getTargetStartDateObj()));
        int monthEnd = Integer.parseInt(String.format("%1$tm", paramBean.getTargetEndDateObj()));
        //DBから設定期間取得
        ConnectionWrapper cw = SystemInfo.getConnection();
        
        //帳票のExcelを開く、データ挿入する
        JExcelApi jx = new JExcelApi("来店カルテ分析");
        jx.setTemplateFile("/reports/来店カルテ分析.xls");
        jx.setValue(3, 2,yearStart);
        jx.setValue(2, 3,paramBean.getTargetName());
        if(paramBean.getStaffId() != null ){
             jx.setValue(10, 3,paramBean.getStaffName());
        }
        String dateTime = ""; 
        int Column = 3;
        ResultSetWrapper rs = new ResultSetWrapper();
        for (int i = 9; i <= 12; i++) {
            dateTime = (Integer.parseInt(year) - 1) + "" + i; 
            if (i == 9) {
                dateTime = (Integer.parseInt(year) - 1) + "09";  
            }  
            dateTime += "01";
            rs = cw.executeQuery(GetSQLNewCustomer(paramBean, dateTime));
            if (rs.next()) {
                 jx.setValue(Column, 6, rs.getInt("Total_Cus"));
                 jx.setValue(Column, 7, rs.getInt("new_count"));
            }
            // rs = cw.executeQuery(GetSQLReturnNewCustomer(paramBean, dateTime,true,false,true));
            rs = cw.executeQuery(GetSQLNewCustomer(paramBean, dateTime,true,false,true));
            if (rs.next()) {
                 jx.setValue(Column + 2, 9, rs.getInt("tota_ReturnNewCutomer"));                
            }
            // rs = cw.executeQuery(GetSQLReturnNewCustomer(paramBean, dateTime,true,false,true));
            rs = cw.executeQuery(GetSQLNewCustomer(paramBean, dateTime,false,false,false));
            if (rs.next()) {
                 jx.setValue(Column + 2, 10, rs.getInt("tota_ReturnNewCutomer"));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime));
            if (rs.next()) {
                 jx.setValue(Column, 11, rs.getInt("Total_loseCustomer"));                
            }
           // rs = cw.executeQuery(GetSQLReturnNewCustomer(paramBean, dateTime,false,true,true));
            rs = cw.executeQuery(GetSQLAllCustomer(paramBean, dateTime));
            if (rs.next()) {
                 jx.setValue(Column, 12, rs.getInt("tota_ReturnNewCutomer"));                
            }
            rs = cw.executeQuery(GetSQLReturnCustomerByMonth(paramBean, dateTime,3));
            if (rs.next()) {
                 jx.setValue(Column, 14, rs.getInt("Total_ReturnCustomerByMonth"));                
            }
            rs = cw.executeQuery(GetSQLReturnCustomerByMonth(paramBean, dateTime,2));
            if (rs.next()) {
                 jx.setValue(Column, 15, rs.getInt("Total_ReturnCustomerByMonth"));                
            }
            rs = cw.executeQuery(GetSQLReturnCustomerBy1Month(paramBean, dateTime,1));
            if (rs.next()) {
                 jx.setValue(Column, 16, rs.getInt("Total_ReturnCustomerByMonth"));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,1));
            if (rs.next()) {
                 jx.setValue(Column, 21, rs.getInt("total_LoseCutomer"));                
            }
            
          
           
            // vtbphuong start comment 20140609 Bug #24766
//            Integer[] result = this.CountReturnLostCustomer(paramBean, dateTime);
//            jx.setValue(Column, 21, result[0]); 
//            jx.setValue(Column, 22, result[1]); 
//            jx.setValue(Column, 23, result[2]); 
//            jx.setValue(Column, 24, result[3]); 
//            jx.setValue(Column, 25, result[4]); 
//            
//            Integer[] result2 = this.CountLostReturnLostCustomer(paramBean, dateTime);
//            jx.setValue(Column +2, 21, result2[0]); 
//            jx.setValue(Column +2 , 22, result2[1]); 
//            jx.setValue(Column +2, 23, result2[2]); 
//            jx.setValue(Column +2, 24, result2[3]); 
//            jx.setValue(Column +2, 25, result2[4]); 
            
              // vtbphuong end  comment 20140609 Bug #24766
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,2));
            if (rs.next()) {
                 jx.setValue(Column, 22, rs.getInt("total_LoseCutomer"));                
            }
            rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,2));
            if (rs.next()) {
                 jx.setValue(Column + 2, 22, rs.getInt(1));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,3));
            if (rs.next()) {
                 jx.setValue(Column, 23, rs.getInt("total_LoseCutomer"));                
            }
             rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,3));
            if (rs.next()) {
                 jx.setValue(Column + 2, 23, rs.getInt(1));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,4));
            if (rs.next()) {
                 jx.setValue(Column, 24, rs.getInt("total_LoseCutomer"));                
            }
              rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,4));
            if (rs.next()) {
                 jx.setValue(Column + 2, 24, rs.getInt(1));                
            }
             rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,5));
            if (rs.next()) {
                 jx.setValue(Column, 25, rs.getInt("total_LoseCutomer"));                
            }
              rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,5));
            if (rs.next()) {
                 jx.setValue(Column + 2, 25, rs.getInt(1));                
            }
          
            // vtbphuong start coment 20140609 
//            rs = cw.executeQuery(GetSQLIntegrationLoseCustomer(paramBean, dateTime,2));
//            if (rs.next()) {
//                 jx.setValue(Column, 26, rs.getInt(1));                
//            }
//              rs = cw.executeQuery(GetSQLIntegrationLoseCustomer(paramBean, dateTime,3));
//            if (rs.next()) {
//                 jx.setValue(Column, 27, rs.getInt(1));                
//            }
            rs = cw.executeQuery(GetSQLIntegrationLoseCustomer(paramBean, dateTime));
            while (rs.next()) {
                 if(rs.getInt("technic_integration_id") ==2)
                 {
                     jx.setValue(Column, 26, rs.getInt("num"));    
                 }else{
                     jx.setValue(Column, 27, rs.getInt("num"));
                 }
                             
            }
            // vtbphuong start commnet 20140609 
            if (i == 11) {
                 Column +=8;
            }
            else
            {
                Column +=4;
            }
        }
        for (int i = 1; i <= 8; i++) {
            dateTime = year + "0" + i;           
            dateTime += "01";
            rs = cw.executeQuery(GetSQLNewCustomer(paramBean, dateTime));
            if (rs.next()) {
                 jx.setValue(Column, 6, rs.getInt("Total_Cus"));
                 jx.setValue(Column, 7, rs.getInt("new_count"));
            }
           // rs = cw.executeQuery(GetSQLReturnNewCustomer(paramBean, dateTime,true,false,true));
            rs = cw.executeQuery(GetSQLNewCustomer(paramBean, dateTime,true,false,true));
            if (rs.next()) {
                 jx.setValue(Column + 2, 9, rs.getInt("tota_ReturnNewCutomer"));                
            }
            //rs = cw.executeQuery(GetSQLReturnNewCustomer(paramBean, dateTime,false,false,false));
            rs = cw.executeQuery(GetSQLNewCustomer(paramBean, dateTime,false,false,false));
            if (rs.next()) {
                 jx.setValue(Column + 2, 10, rs.getInt("tota_ReturnNewCutomer"));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime));
            if (rs.next()) {
                 jx.setValue(Column, 11, rs.getInt("Total_loseCustomer"));                
            }
            //rs = cw.executeQuery(GetSQLReturnNewCustomer(paramBean, dateTime,false,true,true));
            rs = cw.executeQuery(GetSQLAllCustomer(paramBean, dateTime));
            if (rs.next()) {
                 jx.setValue(Column, 12, rs.getInt("tota_ReturnNewCutomer"));                
            }
             rs = cw.executeQuery(GetSQLReturnCustomerByMonth(paramBean, dateTime,3));
            if (rs.next()) {
                 jx.setValue(Column, 14, rs.getInt("Total_ReturnCustomerByMonth"));                
            }
            rs = cw.executeQuery(GetSQLReturnCustomerByMonth(paramBean, dateTime,2));
            if (rs.next()) {
                 jx.setValue(Column, 15, rs.getInt("Total_ReturnCustomerByMonth"));                
            }
            rs = cw.executeQuery(GetSQLReturnCustomerBy1Month(paramBean, dateTime,1));
            if (rs.next()) {
                 jx.setValue(Column, 16, rs.getInt("Total_ReturnCustomerByMonth"));                
            }
            
             rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,1));
            if (rs.next()) {
                 jx.setValue(Column, 21, rs.getInt("total_LoseCutomer"));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,2));
            if (rs.next()) {
                 jx.setValue(Column, 22, rs.getInt("total_LoseCutomer"));                
            }
            rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,2));
            if (rs.next()) {
                 jx.setValue(Column + 2, 22, rs.getInt(1));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,3));
            if (rs.next()) {
                 jx.setValue(Column, 23, rs.getInt("total_LoseCutomer"));                
            }
             rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,3));
            if (rs.next()) {
                 jx.setValue(Column + 2, 23, rs.getInt(1));                
            }
            rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,4));
            if (rs.next()) {
                 jx.setValue(Column, 24, rs.getInt("total_LoseCutomer"));                
            }
              rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,4));
            if (rs.next()) {
                 jx.setValue(Column + 2, 24, rs.getInt(1));                
            }
             rs = cw.executeQuery(GetSQLReturnLoseCustomer(paramBean, dateTime,5));
            if (rs.next()) {
                 jx.setValue(Column, 25, rs.getInt("total_LoseCutomer"));                
            }
              rs = cw.executeQuery(GetSQLLoseCustomer(paramBean, dateTime,5));
            if (rs.next()) {
                 jx.setValue(Column + 2, 25,rs.getInt(1));                
            }
            // vtbphuong start commnet 20140609 
//            Integer[] result = this.CountReturnLostCustomer(paramBean, dateTime);
//            jx.setValue(Column, 21, result[0]); 
//            jx.setValue(Column, 22, result[1]); 
//            jx.setValue(Column, 23, result[2]); 
//            jx.setValue(Column, 24, result[3]); 
//            jx.setValue(Column, 25, result[4]); 
//            
//            Integer[] result2 = this.CountLostReturnLostCustomer(paramBean, dateTime);
//            jx.setValue(Column +2, 21, result2[0]); 
//            jx.setValue(Column +2 , 22, result2[1]); 
//            jx.setValue(Column +2, 23, result2[2]); 
//            jx.setValue(Column +2, 24, result2[3]); 
//            jx.setValue(Column +2, 25, result2[4]); 
            
            // vtbphuong end commnet 20140609 
            
            // vtbphuong start comment 20140609 Bug #24766
//            rs = cw.executeQuery(GetSQLIntegrationLoseCustomer(paramBean, dateTime,2));
//            if (rs.next()) {
//                 jx.setValue(Column, 26, rs.getInt(1));                
//            }
//              rs = cw.executeQuery(GetSQLIntegrationLoseCustomer(paramBean, dateTime,3));
//            if (rs.next()) {
//                 jx.setValue(Column, 27, rs.getInt(1));                
//            }
            
            rs = cw.executeQuery(GetSQLIntegrationLoseCustomer(paramBean, dateTime));
             while (rs.next()) {
                 if(rs.getInt("technic_integration_id") ==2)
                 {
                     jx.setValue(Column, 26, rs.getInt("num"));    
                 }else{
                     jx.setValue(Column, 27, rs.getInt("num"));
                 }
                             
            }
             // vtbphuong start comment 20140609 Bug #24766
            if (i == 2 || i == 5) {
                 Column +=8;
            }
            else
            {
                Column +=4;
            }
        }

        //jx.setFormularActive();
        
            jx.openWorkbook();
            return true;
      
    }

    private String GetSQLNewCustomer(ReportParameterBean paramBean, String startDate) {
        String CountCus = "";
        CountCus += " select count(distinct case when visit_num = 1 then customer_id end) as new_count \n";
        //CountCus += " ,count(distinct customer_id) as Total_Cus \n";
        CountCus += " ,count(customer_id) as Total_Cus \n";
        CountCus += " from data_sales ds \n";
        CountCus += " inner join mst_customer using(customer_id) \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
       // CountCus += " and sales_date between date_trunc('month', " + SQLUtil.convertForSQL(startDate) + "::date) \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char("+ SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        // vtbphuong start change 20140404
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        // vtbphuong end change 20140404
        return CountCus;
    }
    
    private String GetSQLReturnNewCustomer(ReportParameterBean paramBean, String startDate, boolean flagNew, boolean flagRegular, boolean distinct ) {
        String CountCus = "";
        if(distinct)
        {
            CountCus += " select count(distinct customer_id) as tota_ReturnNewCutomer from data_sales ds \n";
        }
        else{
            CountCus += " select count(customer_id) as tota_ReturnNewCutomer from data_sales ds \n";
        }
        
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " and customer_id in ( \n";
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " inner join mst_customer using(customer_id) \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        if (!flagRegular) {
            if (flagNew) {
                CountCus += " and visit_num = 1 \n";
            } else {
                CountCus += " and visit_num > 1 \n";
            }
        }
        // vtbphuong start change 20140416 Request #22392
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        // vtbphuong end change 20140416 Request #22392
        CountCus += " ) \n";
        return CountCus;
    }
    
    private String GetSQLNewCustomer(ReportParameterBean paramBean, String startDate, boolean flagNew, boolean flagRegular, boolean distinct ) {
        String CountCus = "";
        CountCus += " select  \n";
        CountCus += " sum(CASE WHEN  \n";
        if (!flagRegular) {
            if (flagNew) {
                CountCus += " visit_num = 1 \n";
            } else {
                CountCus += " visit_num > 1 \n";
            }
        }
        CountCus += " AND EXISTS  \n";
        CountCus += " (SELECT 1  \n";
        CountCus += " FROM  \n";
        CountCus += " (SELECT ds.sales_date ,ds.customer_id ,ds.insert_date  \n";
        CountCus += " FROM data_sales ds  \n";
        CountCus += " JOIN mst_customer mc using(customer_id)  \n";
        CountCus += " INNER  JOIN data_sales_detail dsd on ds.shop_id = dsd.shop_id and  ds.slip_no = dsd.slip_no   \n";
        CountCus += " WHERE  \n";
        CountCus += " ds.delete_date IS NULL  \n";
        CountCus += " AND mc.customer_no <> '0'  \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and dsd.product_division in(1)  \n"; 
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " ) w  \n";
        CountCus += " WHERE w.customer_id = ds.customer_id  \n";
        CountCus += " AND (w.sales_date > ds.sales_date  \n";
        CountCus += " OR (w.sales_date = ds.sales_date  \n";
        CountCus += " AND w.insert_date > ds.insert_date)  \n";
        CountCus += "  ) \n";
        CountCus += " ) THEN 1 ELSE 0 END) as tota_ReturnNewCutomer   \n";
        CountCus += " from (  \n";
        CountCus += " select distinct ds.customer_id, min(sales_date) as sales_date, min( ds.insert_date) as insert_date,visit_num  \n";
        CountCus += " from data_sales ds   \n";
        CountCus += " INNER  JOIN data_sales_detail dsd on ds.shop_id = dsd.shop_id and  ds.slip_no = dsd.slip_no   \n";
        CountCus += " where ds.delete_date is null   \n";
        CountCus += " and dsd.product_division in(1)  \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        if (!flagRegular) {
            if (flagNew) {
                CountCus += " and visit_num = 1 \n";
            } else {
                CountCus += " and visit_num > 1 \n";
            }
        }
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " group by ds.customer_id, ds.insert_date,visit_num) ds";
        return CountCus;
    }
    
    private String GetSQLAllCustomer(ReportParameterBean paramBean, String startDate ) {
        String CountCus = "";
        CountCus += " select  \n";
        CountCus += " sum(CASE WHEN  \n";
        CountCus += " EXISTS  \n";
        CountCus += " (SELECT 1  \n";
        CountCus += " FROM  \n";
        CountCus += " (SELECT ds.sales_date ,ds.customer_id ,ds.insert_date  \n";
        CountCus += " FROM data_sales ds  \n";
        CountCus += " JOIN mst_customer mc using(customer_id)  \n";
        CountCus += " INNER  JOIN data_sales_detail dsd on ds.shop_id = dsd.shop_id and  ds.slip_no = dsd.slip_no   \n";
        CountCus += " WHERE  \n";
        CountCus += " ds.delete_date IS NULL  \n";
        CountCus += " AND mc.customer_no <> '0'  \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and dsd.product_division in(1)  \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " ) w  \n";
        CountCus += " WHERE w.customer_id = ds.customer_id  \n";
        CountCus += " AND (w.sales_date > ds.sales_date  \n";
        CountCus += " OR (w.sales_date = ds.sales_date  \n";
        CountCus += " AND w.insert_date > ds.insert_date)  \n";
        CountCus += "  ) \n";
        CountCus += " ) THEN 1 ELSE 0 END) as tota_ReturnNewCutomer   \n";
        CountCus += " from (  \n";
        CountCus += " select distinct ds.customer_id, max(ds.sales_date) as sales_date, max(ds.insert_date) as insert_date  \n";
        CountCus += " from data_sales ds   \n";
        CountCus += " inner join data_sales_detail dsd using(shop_id, slip_no )    \n";
        CountCus += " where ds.delete_date is null   \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
         CountCus += " and dsd.product_division in (1)  \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " group by ds.customer_id) ds";
        return CountCus;
    }
    
    
    
     private String GetSQLReturnLoseCustomer(ReportParameterBean paramBean, String startDate) {
        String CountCus = "";
        
         CountCus += " select  \n";
         CountCus += "sum(CASE WHEN  \n";
         CountCus += "EXISTS   \n";
         CountCus += "(SELECT 1   \n";
         CountCus += "FROM   \n";
         CountCus += "(SELECT ds.sales_date ,ds.customer_id ,ds.insert_date   \n";
         CountCus += " FROM data_sales ds   \n";
         CountCus += " JOIN mst_customer mc using(customer_id)   \n";
         CountCus += " INNER  JOIN data_sales_detail dsd on ds.shop_id = dsd.shop_id and  ds.slip_no = dsd.slip_no   \n";
         CountCus += " WHERE   \n";
         CountCus += " ds.delete_date IS NULL   \n";
         CountCus += "AND mc.customer_no <> '0'   \n";
         CountCus += "and ds.shop_id  in ( "  + paramBean.getShopIDList() + " )   \n";
         CountCus += "and dsd.product_division in(1)   \n";
          if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        } 
         CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')"; 
         CountCus += " )w WHERE w.customer_id =  ds.customer_id    \n";
         CountCus += "and customer_id not in (   \n";
         CountCus += "select distinct customer_id from data_sales ds   \n";
         CountCus += "INNER JOIN data_sales_detail dsd using( shop_id, slip_no )   \n";
         CountCus += "where ds.delete_date is null  \n";
         CountCus += "and ds.shop_id  in ( "  + paramBean.getShopIDList() + " )  \n";
         CountCus += "and dsd.product_division in (1)   \n";
         CountCus += "and to_char(sales_date,'YYYYMM') between to_char("+ SQLUtil.convertForSQL(startDate) +"::date - INTERVAL '3month','YYYYMM')  \n";
         CountCus += "and to_char("+ SQLUtil.convertForSQL(startDate) +" ::date - INTERVAL '1month','YYYYMM')    \n";
         CountCus += " )) THEN 1 ELSE 0 END) as Total_loseCustomer     \n";
         CountCus += "from (    \n";
         CountCus += "select distinct ds.customer_id, max(ds.sales_date) as sales_date, max(ds.insert_date) as insert_date  \n";
         CountCus += "from data_sales ds     \n";
         CountCus += "inner join data_sales_detail dsd using(shop_id, slip_no )    \n";
         CountCus += "where ds.delete_date is null     \n";
         CountCus += "and ds.shop_id  in ( "  + paramBean.getShopIDList() + " )   \n";
         CountCus += "and dsd.product_division in (1)    \n";
         CountCus += "and to_char(sales_date,'YYYYMM') < to_char("+ SQLUtil.convertForSQL(startDate) +" ::date,'YYYYMM') group by ds.customer_id) ds  \n";
        
        
//        CountCus += " select count(distinct mc.customer_id) as Total_loseCustomer from data_sales ds \n";
//        CountCus += " inner join mst_customer mc  using(customer_id) \n";
//        CountCus += " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no  \n";
//        CountCus += " where ds.delete_date is null \n";
//        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
//        CountCus += " and dsd.product_division  in (1) \n";
//         CountCus += " and  mc.customer_no <> '0' \n";
//        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
//        CountCus += " and visit_num > 1 \n"; 
//         // vtbphuong start change 20140404
//        if(paramBean.getStaffId()!=null){
//            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
//        }
//        // vtbphuong end change 20140404
//        CountCus += " and customer_id not in ( \n"; 
//        CountCus += " select distinct customer_id from data_sales ds \n";
//         CountCus += " INNER JOIN data_sales_detail dsd using( shop_id, slip_no )  \n";
//        CountCus += " where ds.delete_date is null \n";
//        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
//        CountCus += " and dsd.product_division in (1)  \n";
//        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date - INTERVAL '3month','YYYYMM')";
//        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date - INTERVAL '1month','YYYYMM')";
//       // CountCus += " and to_char(sales_date,'YYYYMM') < to_char(" + SQLUtil.convertForSQL(startDate) + "::date - INTERVAL '3month','YYYYMM')";
//        CountCus += " ) \n";
        return CountCus;
    }
     
    private String GetSQLReturnCustomerByMonth(ReportParameterBean paramBean, String startDate, int Month) {
        String CountCus = "";
        
        CountCus += " SELECT sum(CASE WHEN EXISTS  \n";
        CountCus += "    (SELECT 1  \n";
        CountCus += "    FROM  \n";
        CountCus += "      (SELECT ds.sales_date ,ds.customer_id ,ds.insert_date  \n";
        CountCus += "       FROM data_sales ds  \n";
        CountCus += "        JOIN mst_customer mc using(customer_id)  \n";
        CountCus += "        INNER  JOIN data_sales_detail dsd ON ds.shop_id = dsd.shop_id  \n";
        CountCus += "       AND ds.slip_no = dsd.slip_no  \n";
        CountCus += "        WHERE ds.delete_date IS NULL  \n";
        CountCus += "          AND mc.customer_no <> '0'  \n";
        CountCus += "         AND ds.shop_id IN (" + paramBean.getShopIDList() + ")  \n";
        CountCus += "         AND dsd.product_division IN(1)  \n";

        if (paramBean.getStaffId() != null) {
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId());
        }
       // CountCus += " and customer_id in ( \n";
       // CountCus += " select distinct customer_id from data_sales ds \n";
       // CountCus += " where ds.delete_date is null \n";
       // CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        if (Month == 1) {
          //  CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
              CountCus += " and to_char(sales_date,'YYYYMM') BETWEEN   to_char(" + SQLUtil.convertForSQL(startDate) + "::date ,'YYYYMM') AND  to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        } else if (Month == 2) {
            CountCus += " and customer_id in ( \n";
            CountCus += " select distinct customer_id from data_sales ds \n";
            
            //nhanvt start add 20141016 Bug #31520
            CountCus += " JOIN mst_customer mc using(customer_id) ";
            CountCus += " INNER  JOIN data_sales_detail dsd ON ds.shop_id = dsd.shop_id ";
            CountCus += "  AND ds.slip_no = dsd.slip_no ";
            CountCus += " AND ds.shop_id = dsd.shop_id ";
            CountCus += " AND dsd.product_division IN(1) ";
            CountCus += " AND mc.customer_no <> '0' ";
            //nhanvt end add 20141016 Bug #31520
            
            CountCus += " where ds.delete_date is null \n";
            CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
            CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '2month','YYYYMM')";
            CountCus += " and customer_id not in ( \n ";
            CountCus += " select distinct customer_id from data_sales ds \n";
            //nhanvt start add 20141016 Bug #31520
            CountCus += " JOIN mst_customer mc using(customer_id) ";
            CountCus += " INNER  JOIN data_sales_detail dsd ON ds.shop_id = dsd.shop_id ";
            CountCus += "  AND ds.slip_no = dsd.slip_no ";
            CountCus += " AND ds.shop_id = dsd.shop_id ";
            CountCus += " AND dsd.product_division IN(1) ";
            CountCus += " AND mc.customer_no <> '0' ";
            //nhanvt end add 20141016 Bug #31520
            CountCus += " where ds.delete_date is null \n";
            CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM') )";
             CountCus += ")  \n ";
        } else if (Month == 3) {
            CountCus += " and customer_id in ( \n";
            CountCus += " select distinct customer_id from data_sales ds \n";
             //nhanvt start add 20141016 Bug #31520
            CountCus += " JOIN mst_customer mc using(customer_id) ";
            CountCus += " INNER  JOIN data_sales_detail dsd ON ds.shop_id = dsd.shop_id ";
            CountCus += "  AND ds.slip_no = dsd.slip_no ";
            CountCus += " AND ds.shop_id = dsd.shop_id ";
            CountCus += " AND dsd.product_division IN(1) ";
            CountCus += " AND mc.customer_no <> '0' ";
            //nhanvt end add 20141016 Bug #31520
            CountCus += " where ds.delete_date is null \n";
            CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '2month','YYYYMM')";
            CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
            CountCus += " and customer_id not in ( \n ";
            CountCus += " select distinct customer_id from data_sales ds \n";
             //nhanvt start add 20141016 Bug #31520
            CountCus += " JOIN mst_customer mc using(customer_id) ";
            CountCus += " INNER  JOIN data_sales_detail dsd ON ds.shop_id = dsd.shop_id ";
            CountCus += "  AND ds.slip_no = dsd.slip_no ";
            CountCus += " AND ds.shop_id = dsd.shop_id ";
            CountCus += " AND dsd.product_division IN(1) ";
            CountCus += " AND mc.customer_no <> '0' ";
            //nhanvt end add 20141016 Bug #31520
            CountCus += " where ds.delete_date is null \n";
            CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
            CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '2month','YYYYMM') )";
             CountCus += ")  \n ";
             
        }
         CountCus += " and to_char(sales_date,'YYYYMM') =   to_char(" + SQLUtil.convertForSQL(startDate) + "::date ,'YYYYMM')";
       // CountCus += ")  \n ";
        CountCus += " ) w  \n";
        CountCus += "  WHERE w.customer_id = ds.customer_id \n";
        CountCus += ") THEN 1 ELSE 0 END) AS Total_ReturnCustomerByMonth  \n";
        CountCus += " from (  \n";
        CountCus += " select distinct ds.customer_id, max(ds.sales_date) as sales_date, max(ds.insert_date) as insert_date  \n";
        CountCus += " from data_sales ds   \n";
        CountCus += " inner join data_sales_detail dsd using(shop_id, slip_no )    \n";
        CountCus += " where ds.delete_date is null   \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
         CountCus += " and dsd.product_division in (1)  \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " group by ds.customer_id) ds";
      
//        
//        CountCus += " select count(mc.customer_id) as Total_ReturnCustomerByMonth from data_sales ds \n";
//        CountCus += " inner join mst_customer mc  using(customer_id) \n";
//        CountCus += " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no  = dsd.slip_no \n";
//        CountCus += " where ds.delete_date is null \n";
//        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n"; 
//        CountCus += " and dsd.product_division in (1) \n";
//         CountCus += " and mc.customer_no <>'0' \n";
//        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";      
//        if(paramBean.getStaffId()!=null){
//            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
//        }
//        CountCus += " and customer_id in ( \n"; 
//        CountCus += " select distinct customer_id from data_sales ds \n";
//        CountCus += " where ds.delete_date is null \n";
//        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
//        if (Month == 1) {
//            CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";              
//        }
//        else if (Month == 2)
//        {
//            CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
//            CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '2month','YYYYMM')";
//           // vtbphuong start add 20140321 Bug #21311[gb]比較集計表　集計の不具合
//            CountCus +=" and customer_id not in ( \n ";
//            CountCus += " select distinct customer_id from data_sales ds \n";
//            CountCus += " where ds.delete_date is null \n";
//            CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
//            CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM') )";
//           // vtbphuong end add 20140321 Bug #21311[gb]比較集計表　集計の不具合
//        }
//        else if (Month == 3)
//        {
//           CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '2month','YYYYMM')";
//           CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
//           // vtbphuong start add 20140321 Bug #21311[gb]比較集計表　集計の不具合
//            CountCus +=" and customer_id not in ( \n ";
//            CountCus += " select distinct customer_id from data_sales ds \n";
//            CountCus += " where ds.delete_date is null \n";
//            CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
//            CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
//            CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '2month','YYYYMM') )";
//           // vtbphuong end add 20140321 Bug #21311[gb]比較集計表　集計の不具合
//            
//        }
//        
//        CountCus += " ) \n";
        return CountCus;
    } 
    
    
        private String GetSQLReturnCustomerBy1Month(ReportParameterBean paramBean, String startDate, int Month) {
        String CountCus = "";
        
        CountCus += " SELECT sum(CASE WHEN EXISTS  \n";
        CountCus += "    (SELECT 1  \n";
        CountCus += "    FROM  \n";
        CountCus += "      (SELECT ds.sales_date ,ds.customer_id ,ds.insert_date  \n";
        CountCus += "       FROM data_sales ds  \n";
        CountCus += "        JOIN mst_customer mc using(customer_id)  \n";
        CountCus += "        INNER  JOIN data_sales_detail dsd ON ds.shop_id = dsd.shop_id  \n";
        CountCus += "       AND ds.slip_no = dsd.slip_no  \n";
        CountCus += "        WHERE ds.delete_date IS NULL  \n";
        CountCus += "          AND mc.customer_no <> '0'  \n";
        CountCus += "         AND ds.shop_id IN (" + paramBean.getShopIDList() + ")  \n";
        CountCus += "         AND dsd.product_division IN(1)  \n";
        CountCus += " and to_char(sales_date,'YYYYMM') BETWEEN   to_char(" + SQLUtil.convertForSQL(startDate) + "::date ,'YYYYMM') AND  to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " ) w  \n";
        CountCus += "  WHERE w.customer_id = ds.customer_id \n";
        CountCus += " AND (w.sales_date > ds.sales_date  \n";
        CountCus += "                OR (w.sales_date = ds.sales_date  \n";
        CountCus += "                    AND w.insert_date > ds.insert_date))  \n";
        CountCus += ") THEN 1 ELSE 0 END) AS Total_ReturnCustomerByMonth  \n";
        CountCus += "FROM  \n";
        CountCus += "  (  \n";
        CountCus += " SELECT ds.sales_date ,  \n";
        CountCus += "   ds.customer_id ,  \n";
        CountCus += "     ds.insert_date  \n";
        CountCus += " FROM data_sales ds  \n";
        CountCus += " JOIN mst_customer mc using(customer_id)  \n";
        CountCus += " WHERE EXISTS  \n";
        CountCus += "    (SELECT 1  \n";
        CountCus += "     FROM data_sales_detail  \n";
        CountCus += "    WHERE shop_id = ds.shop_id  \n";
        CountCus += "      AND slip_no = ds.slip_no  \n";
        CountCus += "      AND delete_date IS NULL  \n";
        CountCus += "     AND product_division IN (1))  \n";
        CountCus += "  AND ds.delete_date IS NULL  \n";
        CountCus += "  AND mc.customer_no <> '0'  \n";
        CountCus += "  AND ds.shop_id IN (" + paramBean.getShopIDList() + ")  \n";
        CountCus += "  AND to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + " ::date,'YYYYMM')  \n";
        if (paramBean.getStaffId() != null) {
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId());
        }
        CountCus += ") ds  \n";
     
        return CountCus;
    } 
    private String GetSQLReturnLoseCustomer(ReportParameterBean paramBean, String startDate,int vistNum) {
        String CountCus = "";
        CountCus += " select count(distinct customer_id) as total_LoseCutomer from data_sales ds \n";
        CountCus += " where ds.delete_date is null \n";
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        if (vistNum >= 5) {
            CountCus += " and visit_num >= " + vistNum + " \n";
            CountCus += "and customer_id not in ( ";
            CountCus += "select distinct customer_id from data_sales ds \n";
            CountCus += "inner join mst_customer using(customer_id) \n";
            CountCus += "where ds.delete_date is null 	\n";
            CountCus += "and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += "and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
            CountCus += " and visit_num <  " + vistNum + " ) \n";
        }else
        {
            CountCus += " and visit_num = " + vistNum + " \n";
            CountCus += "and customer_id not in ( ";
            CountCus += "select distinct customer_id from data_sales ds \n";
            CountCus += "inner join mst_customer using(customer_id) \n";
            CountCus += "where ds.delete_date is null 	\n";
            CountCus += "and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += "and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
            CountCus += " and visit_num >  " + vistNum + "  )\n";								

            
        }
        CountCus += " and customer_id not in ( \n";
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " inner join mst_customer using(customer_id) \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";        
        CountCus += " ) \n";
        return CountCus;
    }
    
     private String GetSQLLoseCustomer(ReportParameterBean paramBean, String startDate,int vistNum) {
        String CountCus = "";
//        CountCus += " select Case when \n";
//        CountCus += " (select count(distinct customer_id) from data_sales ds \n";
//        CountCus += " where ds.delete_date is null \n";
//        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";        
//        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')\n"; 
//        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')) = 0 \n"; 
//        CountCus += " then 0 else \n"; 
//        CountCus += " count(distinct customer_id) end as Total_loseCustomer from data_sales ds \n";
        CountCus += " select count(distinct customer_id) as Total_loseCustomer from data_sales ds \n";
        CountCus += " inner join mst_customer using(customer_id) \n";
        CountCus += " where ds.delete_date is null \n";
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";       
         if (vistNum >= 5) {
            CountCus += " and visit_num >= " + vistNum + " \n";
            CountCus += "and customer_id not in ( ";
            CountCus += "select distinct customer_id from data_sales ds \n";
            CountCus += "inner join mst_customer using(customer_id) \n";
            CountCus += "where ds.delete_date is null 	\n";
            CountCus += "and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += "and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
            CountCus += " and visit_num <  " + vistNum + " ) \n";
        }else
        {
            CountCus += " and visit_num = " + vistNum + " \n";
            CountCus += "and customer_id not in ( ";
            CountCus += "select distinct customer_id from data_sales ds \n";
            CountCus += "inner join mst_customer using(customer_id) \n";
            CountCus += "where ds.delete_date is null 	\n";
            CountCus += "and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
            CountCus += "and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
            CountCus += " and visit_num >  " + vistNum + "  )\n";
        }
        CountCus += " and customer_id not in ( \n"; 
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date - INTERVAL '4month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date - INTERVAL '1month','YYYYMM')";
        CountCus += " ) \n";
        CountCus += " and customer_id not in ( \n"; 
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";        
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " ) \n";
        return CountCus;
    }
     
     
      private String GetSQLIntegrationLoseCustomer(ReportParameterBean paramBean, String startDate, int Integration) {
        String CountCus = "";
        CountCus += " select count(distinct customer_id) from data_sales ds \n";       
        CountCus += " inner join data_sales_detail dsd using(shop_id,slip_no) \n";
        CountCus += " inner join mst_technic mt on mt.technic_id = dsd.product_id \n";
        CountCus += " inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        CountCus += " and dsd.product_division = 1 \n"; 
        CountCus += " and mtc.technic_integration_id = " + Integration + " \n"; 
         // vtbphuong start change 20140404
        if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        // vtbphuong end change 20140404
        CountCus += " and customer_id not in ( \n"; 
        CountCus += " select distinct customer_id from data_sales \n";
        CountCus += " where delete_date is null \n";
        CountCus += " and shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " ) \n";
        return CountCus;
    }
      
      
    private String GetSQLIntegrationLoseCustomer(ReportParameterBean paramBean, String startDate) {
        String CountCus = "";
        CountCus += "SELECT technic_integration_id , ( total- reappearance ) as num FROM  \n ";
        CountCus += " ( \n ";
        CountCus += "SELECT\n ";
        CountCus += "t.technic_integration_id, \n ";
        CountCus += "count(*) AS total , \n ";
        CountCus += "coalesce(sum(CASE WHEN EXISTS \n ";
        CountCus += "  (SELECT 1 \n ";
        CountCus += "   FROM view_data_sales_detail_valid a \n ";
        CountCus += "   JOIN \n ";
        CountCus += "     ( SELECT dsd.shop_id ,dsd.slip_no \n ";
        CountCus += "      FROM view_data_sales_detail_valid dsd \n ";
        CountCus += "      INNER JOIN data_sales ds ON dsd.shop_id = ds.shop_id \n ";
        CountCus += "      AND dsd.slip_no = ds.slip_no \n ";
        CountCus += "      INNER JOIN mst_customer mc ON dsd.customer_id = mc.customer_id \n ";
        CountCus += "      WHERE dsd.customer_id = t.customer_id \n ";
        CountCus += "        AND mc.customer_no <> '0' \n ";
        CountCus += "        AND dsd.product_division IN (1) \n ";
         if(paramBean.getStaffId()!=null){
            CountCus += " and dsd.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += "        AND dsd.shop_id in (" + paramBean.getShopIDList() + ") \n";
        CountCus += "        AND to_char(dsd.sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date ,'YYYYMM')";
        CountCus += "        AND to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += "        AND (dsd.sales_date > t.sales_date \n ";
        CountCus += "             OR (dsd.sales_date = t.sales_date \n ";
        CountCus += "                 AND ds.insert_date > t.insert_date)) \n ";
        CountCus += "      ORDER BY dsd.sales_date ,ds.insert_date LIMIT 1) b USING (shop_id, slip_no) \n ";
        CountCus += "   INNER JOIN mst_technic mt ON a.product_id = mt.technic_id) THEN 1 ELSE 0 END), 0) AS reappearance \n ";
        CountCus += "   FROM \n ";
        CountCus += "   (SELECT DISTINCT dsd.shop_id , \n ";
        CountCus += "    dsd.slip_no , \n ";
        CountCus += "    dsd.sales_date , \n ";
        CountCus += "    dsd.customer_id , \n ";
        CountCus += "    mt.technic_class_id , \n ";
        CountCus += "    mtc.technic_class_name , \n ";
        CountCus += "    mtc.display_seq , mtc.technic_integration_id , \n ";
        CountCus += "    ds.insert_date \n ";
        CountCus += "   FROM view_data_sales_detail_valid dsd \n ";
        CountCus += "   INNER JOIN \n ";
        CountCus += "   (SELECT ds.shop_id , \n ";
        CountCus += "   ds.slip_no , \n ";
        CountCus += "   ds.insert_date \n ";
        CountCus += "   FROM data_sales ds \n ";
        CountCus += "   JOIN  ";
        CountCus += "   (SELECT a.shop_id ,  ";
        CountCus += "     a.sales_date ,  ";
        CountCus += "   a.customer_id ,  ";
        CountCus += "   max(a.slip_no) AS slip_no  ";
        CountCus += "   FROM data_sales a  ";
        CountCus += "   JOIN  ";
        CountCus += "  (SELECT ds.customer_id ,  ";
        CountCus += "        max(ds.sales_date) AS sales_date  ";
        CountCus += " FROM data_sales ds  ";
        CountCus += " JOIN mst_customer mc USING(customer_id)  ";
        CountCus += " WHERE EXISTS  ";
        CountCus += "  (SELECT 1  ";
        CountCus += "  FROM data_sales_detail  ";
        CountCus += "  WHERE shop_id = ds.shop_id  ";
        CountCus += "  AND slip_no = ds.slip_no  ";
        CountCus += "  AND delete_date IS NULL  ";
        CountCus += "  AND product_division IN (1))  ";
        CountCus += "  AND ds.delete_date IS NULL  ";
        CountCus += "  AND mc.customer_no <> '0'  ";
        CountCus += "  AND ds.shop_id  in (" + paramBean.getShopIDList() + ") \n"; 
         CountCus += "        AND to_char(ds.sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date ,'YYYYMM')";
          if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " GROUP BY ds.customer_id) b USING(customer_id,  ";
        CountCus += "                                 sales_date)  ";
        CountCus += " WHERE a.delete_date IS NULL  ";
        CountCus += " GROUP BY shop_id ,  ";
        CountCus += "        sales_date ,  ";
        CountCus += "        customer_id) b USING(shop_id,  ";
        CountCus += "                           customer_id,  ";
        CountCus += "                           sales_date,  ";
        CountCus += "                           slip_no)) ds ON dsd.shop_id = ds.shop_id  ";
        CountCus += " AND dsd.slip_no = ds.slip_no  ";
        CountCus += " INNER JOIN mst_technic mt ON dsd.product_id = mt.technic_id  ";
        CountCus += "AND dsd.product_division IN (1)  ";
        CountCus += "INNER JOIN mst_technic_class mtc ON mt.technic_class_id = mtc.technic_class_id ) t  ";
        CountCus += "WHERE  t.technic_integration_id in (2,3)  ";
        CountCus += "GROUP BY t.technic_integration_id   ";
        CountCus += ") ds  ";
        return CountCus;
    }
      
    private Integer[] CountReturnLostCustomer( ReportParameterBean paramBean, String startDate)
    {
         Integer[] result = new Integer[5];
         Integer result1 = 0;
         Integer result2 = 0;
         Integer result3 = 0;
         Integer result4 = 0;
         Integer result5 = 0;
         Integer num = 0;
         String CountCus = "";
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
        CountCus += " and visit_num > 1 \n ";
         if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " and customer_id not in ( \n";
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " inner join mst_customer using(customer_id) \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " ) \n";
        CountCus += " order by customer_id asc ";
        
     
         ConnectionWrapper cw = SystemInfo.getConnection();
         ResultSetWrapper rs = new ResultSetWrapper();
         try
         {
             rs = cw.executeQuery(CountCus);
             while(rs.next())
             {
                 String sqlCount = "";
                 sqlCount +=" Select sales_date   from data_sales ds  \n ";
                 sqlCount +=" where ds.delete_date is null   \n ";
                 sqlCount +=" and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
                 sqlCount += " and to_char(sales_date,'YYYYMM') <=  to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
                 sqlCount += " and ds.customer_id = " + SQLUtil.convertForSQL(rs.getInt("customer_id")) ;
                 sqlCount += " order by sales_date desc ";
                 ResultSetWrapper rsCount = new ResultSetWrapper();
                 rsCount = cw.executeQuery(sqlCount);
                 java.util.Date maxDate =null ;
                 if(rsCount.next())
                 {
                     maxDate = rsCount.getDate("sales_date");
                     num++;
                     rsCount.first();
                 }
                 while (rsCount.next()){
                     if(((maxDate.getTime()  - rsCount.getDate("sales_date").getTime()) / (24 * 60 * 60 * 1000) ) <= 365){
                         num ++;
                     }
                     else{
                         rsCount.last();
                     }
                     maxDate = rsCount.getDate("sales_date");
                 }
                  if (num == 1) {
                     result1 ++;
                  }else if (num == 2) {
                     result2 ++;
                 } else if( num == 3) {
                     result3 ++;
                 } else if(num == 4) {
                     result4 ++;
                 }else if(num >= 5){   
                     result5 ++;
                 }
                 num = 0;
             }
             
             
              CountCus = "";
             CountCus += " select count (distinct customer_id) as num   from data_sales ds \n";
             CountCus += " where ds.delete_date is null \n";
             CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
             CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
             CountCus += " and visit_num = 1 \n ";
             if (paramBean.getStaffId() != null) {
                 CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId());
             }
             CountCus += " and customer_id not in ( \n";
             CountCus += " select distinct customer_id from data_sales ds \n";
             CountCus += " inner join mst_customer using(customer_id) \n";
             CountCus += " where ds.delete_date is null \n";
             CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
             CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
             CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
             CountCus += " ) \n";
             rs = cw.executeQuery(CountCus);
             if (rs.next()) {
                 result1 += rs.getInt("num");
             }
              
         }
         
         
         catch( SQLException e){
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
         }
         result[0] = result1 ;
         result[1] = result2 ;
         result[2] = result3 ;
         result[3] = result4 ;
         result[4] = result5 ;
         return result;
        
        
    }
    
     private Integer[] CountLostReturnLostCustomer( ReportParameterBean paramBean, String startDate)
    {
         Integer[] result = new Integer[5];
         Integer result1 = 0;
         Integer result2 = 0;
         Integer result3 = 0;
         Integer result4 = 0;
         Integer result5 = 0;
         Integer num = 0;
         String CountCus = "";
        CountCus += " select distinct customer_id  from data_sales ds \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') = to_char(" + SQLUtil.convertForSQL(startDate) + "::date,'YYYYMM')";
         if(paramBean.getStaffId()!=null){
            CountCus += " and ds.staff_id = " + SQLUtil.convertForSQL(paramBean.getStaffId()) ;
        }
        CountCus += " and customer_id not in ( \n";
        CountCus += " select distinct customer_id from data_sales ds \n";
        CountCus += " inner join mst_customer using(customer_id) \n";
        CountCus += " where ds.delete_date is null \n";
        CountCus += " and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
        CountCus += " and to_char(sales_date,'YYYYMM') between to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '1month','YYYYMM')";
        CountCus += " and to_char(" + SQLUtil.convertForSQL(startDate) + "::date + INTERVAL '3month','YYYYMM')";
        CountCus += " ) \n";
          
        CountCus += " order by customer_id asc ";
        
         ConnectionWrapper cw = SystemInfo.getConnection();
         ResultSetWrapper rs = new ResultSetWrapper();
         try
         {
             rs = cw.executeQuery(CountCus);
             while(rs.next())
             {
                 String sqlCount = "";
                 sqlCount +=" Select count(slip_no ) as num from data_sales ds  \n ";
                 sqlCount +=" where ds.delete_date is null   \n ";
                 sqlCount +=" and ds.shop_id  in (" + paramBean.getShopIDList() + ") \n";
                 sqlCount += " and to_char(sales_date,'YYYYMMDD') <  to_char(" + SQLUtil.convertForSQL(startDate) + "::date - INTERVAL '11month' ,'YYYYMMDD')";
                 sqlCount += " and ds.customer_id = " + SQLUtil.convertForSQL(rs.getInt("customer_id")) ;
                 ResultSetWrapper rsCount = new ResultSetWrapper();
                 rsCount = cw.executeQuery(sqlCount);
                 if (rsCount.next()){
                         num = rsCount.getInt("num");
                 }
                 if (num == 1) {
                     result1 ++;
                }else if (num == 2) {
                     result2 ++;
                 } else if( num == 3) {
                     result3 ++;
                 } else if(num == 4) {
                     result4 ++;
                 }else if(num >= 5){   
                     result5 ++;
                 }
                 num = 0;
             }
         }
         catch( SQLException e){
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
         }
         result[0] = result1 ;
         result[1] = result2 ;
         result[2] = result3 ;
         result[3] = result4 ;
         result[4] = result5 ;
         return result;
        
        
    }
    
}

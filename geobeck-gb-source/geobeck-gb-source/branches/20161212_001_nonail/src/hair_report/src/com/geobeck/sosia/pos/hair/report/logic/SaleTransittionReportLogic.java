/*
 * ReportLogic.java
 *
 * Created on 2013/01/28, 09:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;


import com.geobeck.sosia.pos.system.SystemInfo;
import java.util.*;
import java.text.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.report.bean.*;
import com.geobeck.sosia.pos.report.util.*;
import java.sql.*;
import com.geobeck.sosia.pos.hair.report.util.*;

class HashMapArrayListSaleTransittion extends ArrayList<HashMap> {
};

/**
 * ���|�[�g���W�b�N
 *
 * @author Duong Hoang Thanh
 */
public class SaleTransittionReportLogic {

    public static int REPORT_TYPE_PDF = 0;
    public static int REPORT_TYPE_EXCEL = 1;
    public static int STAFF_REPORT_COLUMN_COUNT = 6;
    public static String REPORT_RESOURCE_PATH = "/reports/";
    public static String REPORT_EXPORT_PATH = "./";
    public static String REPORT_XML_FILE_EXT = ".jasper";
    public static int ORDER_DISPLAY_KINGAKU = 0;
    public static int ORDER_DISPLAY_POINT = 1;
    //Excel�P�V�[�g����̈���
    public static int PROPORTIONALLY_COUNT_OF_ONE_EXCEL_SHEET = 15;
    
    public static boolean bCurrentYear = false;
    public static boolean bLastYear = false;
    public static boolean bBeforeLastYear = false;
    /**
     * �R���X�g���N�^
     */
    public SaleTransittionReportLogic() {
    }

    private StringBuilder GetSQLSaleTrend(ReportParameterBean paramBean, Integer Year, boolean isTechnic) {
        // �c�Ɠ����i����Ȃ��̃��W���ߓ������܂߂�j
        StringBuilder sql = new StringBuilder();
        sql.append(" select year_label as ����, month_label as ��, sum(sales_value) as ��������, technic_name as ��");
        sql.append(" from ( ");
        sql.append(" select  '���N' :: text as year_label");
        sql.append(" , extract(month from ds.sales_date) as month_label");
        sql.append(" , sum(dsd.sale_value) as sales_value");
        if (isTechnic) {
              sql.append(" , mt.technic_name as technic_name");
        }
        else
        {
             sql.append(" , mt.item_name as technic_name");
        }      
        sql.append(" from     view_data_sales_valid ds");
        sql.append(" inner join (");
        sql.append(" select shop_id");
        sql.append(" ,slip_no");
        sql.append(" ,product_id");
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
            sql.append(" ,sum(discount_detail_value_no_tax) as sale_value");
        }
        else
        {
            sql.append(" ,sum(discount_detail_value_in_tax) as sale_value");
        }
        sql.append(" from view_data_sales_detail_valid");
        sql.append(" where  shop_id in (" + paramBean.getShopIDList() + ")");
        if (isTechnic) {
            sql.append(" and date_part('year', sales_date) = " + Year + " and product_division = 1");
        }
        else
        {
            sql.append(" and date_part('year', sales_date) = " + Year + " and product_division = 2");
        }        
        sql.append(" group by shop_id,slip_no,product_id) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no");
        if (isTechnic) {
            sql.append(" left join mst_technic mt");
            sql.append(" on dsd.product_id = mt.technic_id");
        }
        else
        {
            sql.append(" left join mst_item mt");
            sql.append(" on dsd.product_id = mt.item_id");
        }        
        sql.append(" left join mst_customer mc");
        sql.append(" using(customer_id)");
        sql.append(" group by ds.sales_date,technic_name");
        /*
        sql.append(" union all");
        sql.append(" select  	'�O�N' as year_label");
        sql.append(" , extract(month from ds.sales_date) as month_label");
        sql.append(" , sum(dsd.sale_value) as sales_value");
        sql.append(" , mt.technic_name as technic_name");
        sql.append(" from     view_data_sales_valid ds");
        sql.append(" inner join (");
        sql.append(" select shop_id");
        sql.append(" ,slip_no");
        sql.append(" ,product_id");
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
        sql.append(" ,sum(discount_detail_value_no_tax) as sale_value");
        }
        else
        {
        sql.append(" ,sum(discount_detail_value_in_tax) as sale_value");
        }
        sql.append(" from view_data_sales_detail_valid");
        sql.append(" where  shop_id in (" + paramBean.getShopIDList() + ")");
        sql.append(" and date_part('year', sales_date) = 2012 and product_division = 1");
        sql.append(" group by shop_id,slip_no,product_id) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no");
        sql.append(" left join mst_technic mt");
        sql.append(" on dsd.product_id = mt.technic_id");
        sql.append(" left join mst_customer mc");
        sql.append(" using(customer_id)");
        sql.append(" group by ds.sales_date,mt.technic_name");
        sql.append(" union all");
        sql.append(" select  	'�O�X�N' as year_label");
        sql.append(" , extract(month from ds.sales_date) as month_label");
        sql.append(" , sum(dsd.sale_value) as sales_value");
        sql.append(" , mt.technic_name as technic_name");
        sql.append(" from     view_data_sales_valid ds");
        sql.append(" inner join (");
        sql.append(" select shop_id");
        sql.append(" ,slip_no");
        sql.append(" ,product_id");
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
        sql.append(" ,sum(discount_detail_value_no_tax) as sale_value");
        }
        else
        {
        sql.append(" ,sum(discount_detail_value_in_tax) as sale_value");
        }
        sql.append(" from view_data_sales_detail_valid");
        sql.append(" where  shop_id in (" + paramBean.getShopIDList() + ")");
        sql.append(" and date_part('year', sales_date) = 2011 and product_division = 1");
        sql.append(" group by shop_id,slip_no,product_id) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no");
        sql.append(" left  join mst_technic mt");
        sql.append(" on dsd.product_id = mt.technic_id");
        sql.append(" left join mst_customer mc");
        sql.append(" using(customer_id)");
        sql.append(" group by ds.sales_date,mt.technic_name");
         */
        sql.append(" ) t ");
        sql.append(" group by year_label, month_label,technic_name");
        sql.append(" order by technic_name,month_label");
         /*
        sql.append(" order by (case when year_label = '���N' then 0 ");
        sql.append(" when year_label = '�O�N' then 1");
        sql.append(" else 2 end ), month_label");
        */ 
        return sql;
    }

    private void SetValueByMonth(Integer month, JPOIApiSaleTransittion jx, int row, ResultSetWrapper rs) throws SQLException {
        switch (month) {
            case 1:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(3, row, rs.getInt("��������"));    
                break;
            case 2:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(5, row, rs.getInt("��������"));    
                break;
            case 3:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(7, row, rs.getInt("��������"));    
                break;
            case 4:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(9, row, rs.getInt("��������"));    
                break;
            case 5:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(11, row, rs.getInt("��������"));    
                break;
            case 6:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(13, row, rs.getInt("��������"));    
                break;  
            case 7:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(15, row, rs.getInt("��������"));    
                break; 
            case 8:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(17, row, rs.getInt("��������"));    
                break;  
            case 9:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(19, row, rs.getInt("��������"));    
                break;
            case 10:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(21, row, rs.getInt("��������"));    
                break;
            case 11:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(23, row, rs.getInt("��������"));    
                break; 
            case 12:
                jx.setCellValue(2, row, rs.getString("��"));
                jx.setCellValue(25, row, rs.getInt("��������"));    
                break;     
        }
    }

    private int SetValueForExcel(ResultSetWrapper rs, int row, int startRow, String OldTechnich, JPOIApiSaleTransittion jx, int indexReport) throws SQLException {
        while (rs.next()) {
            if (indexReport == 1) {
                bCurrentYear = true;
            }
            else if (indexReport == 2) {
                bLastYear = true;
            }
             else if (indexReport == 3) {
                bBeforeLastYear = true;
            }
             Integer month = rs.getInt("��");
            //dayCount = rs.getInt("date_count");   
            if (row == startRow && OldTechnich == "") {               
                SetValueByMonth(month, jx, row, rs);
                
                OldTechnich = rs.getString("��");
            }
            else
            {
                try {
                    if (OldTechnich.equals(rs.getString("��"))) {                    
                        SetValueByMonth(month, jx, row, rs);
                        OldTechnich = rs.getString("��");
                    }
                    else
                    {       
                            jx.copyRow(startRow - 1,row);
                             row = row + 1;
                            SetValueByMonth(month, jx, row, rs);
                            OldTechnich = rs.getString("��");
                    }
                } catch (Exception e) {
                    SetValueByMonth(month, jx, row, rs);
                     OldTechnich = rs.getString("��");
                }
   
                    
               
            }
           
        }
        jx.setFormular(row, startRow, row);
        return row;
    }

    /**
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return true:���� false:�o�͑ΏۂȂ�
     * @exception Exception
     */
    public boolean viewSaleTrendReport(ReportParameterBean paramBean, boolean isTechnic, Integer year) throws Exception {
        return viewSaleTrendForTechnicalAndItem(paramBean, isTechnic, year);
    }

    
    private boolean viewSaleTrendForTechnicalAndItem(ReportParameterBean paramBean, boolean isTechnical, Integer year) throws Exception {
        bBeforeLastYear = false;
        bCurrentYear = false;
        bLastYear = false;
        
        ConnectionWrapper cw = SystemInfo.getConnection();

        // �X�܏��擾
        HashMap<String, String> shopInfo = this.getShopInfo(paramBean.getShopIDList());

        HashMap<String, Object> paramMap = new HashMap<String, Object>();  

        // �ō��ݥ�ŕʂ̋敪
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
            paramMap.put("TaxType", "�Ŕ�");
        } else if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_UNIT) {
            paramMap.put("TaxType", "�ō�");
        }
    
        StringBuilder sql = GetSQLSaleTrend(paramBean, year,isTechnical);
        String fileName = "���㐄�ڕ\_Non";
           
        //JExcelApi jx = new JExcelApi(fileName);
        JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion(fileName);
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        jx.setCellValue(3, 2,year + "�N�x");
        jx.setCellValue(3, 3,shopInfo.get("shop_name"));
        jx.setCellValue(6, 3, paramMap.get("TaxType").toString()); 
        if (isTechnical) {
             jx.setCellValue(6, 2, "�Z�p"); 
        }
        else
        {
             jx.setCellValue(6, 2, "���i"); 
        }
        
        jx.setCellValue(2, 5, "��" + year + "�N�x"); 
        
        int startRow = 8;
        int row = 8;
        ResultSetWrapper rs = cw.executeQuery(sql.toString());
        String OldTechnich = "";
        row = SetValueForExcel(rs, row, startRow, OldTechnich, jx,1); 
        
        StringBuilder sqlLastYear = GetSQLSaleTrend(paramBean, year - 1,isTechnical);
        ResultSetWrapper rsLastYear = cw.executeQuery(sqlLastYear.toString());
        OldTechnich = "";
        jx.setCellValue(2, row + 3, "��" + (year - 1) + "�N�x"); 
        row = row + 6;
        int lastYearStartRow = row;
        
        row = SetValueForExcel(rsLastYear, row, lastYearStartRow, OldTechnich, jx,2); 
        
        StringBuilder sqlBeforeLastYear = GetSQLSaleTrend(paramBean, year - 2,isTechnical);
        ResultSetWrapper rsBeforeLastYear = cw.executeQuery(sqlBeforeLastYear.toString());
        OldTechnich = "";
        jx.setCellValue(2, row + 3, "��" + (year - 2) + "�N�x"); 
        row = row + 6;
        int lastBeforeLastYear = row;
        
        row = SetValueForExcel(rsBeforeLastYear, row, lastBeforeLastYear, OldTechnich, jx,3); 
       
        if (bCurrentYear == false && bLastYear == false && bBeforeLastYear == false) {
            return false;
        }
        
        jx.setFormularActive();     
        
        jx.openWorkbook();
        
        return true;
    }

    /**
     * �X�܏��擾
     *
     * @return �X�܏��(HashMap)
     */
    public HashMap<String, String> getShopInfo(String shopIDList) throws Exception {
        HashMap<String, String> shopInfo = new HashMap<String, String>();

        ConnectionWrapper cw = SystemInfo.getConnection();

        String query = "SELECT shop_name, cutoff_day FROM mst_shop\n"
                + "where shop_id in (" + shopIDList + ")";
        ResultSetWrapper rs = cw.executeQuery(query);
        if (!rs.next()) {
            // ���R�[�h�����݂��Ȃ����߃G���[
            return null;
        }
        shopInfo.put("shop_name", rs.getString("shop_name"));
        if (0 == rs.getInt("cutoff_day")) {
            shopInfo.put("cutoff_day", null);
        } else {
            shopInfo.put("cutoff_day", String.valueOf(rs.getInt("cutoff_day")));
        }

        return shopInfo;
    }
    
    /**
     * �Ŕ����A�ō��ݑΉ������񐶐�
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return �Ŕ����A�ō��ݑΉ�������
     * @exception Exception
     */
    public String makeValueString(ReportParameterBean paramBean) throws Exception {
        // �Ŕ���
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {

            return "discount_detail_value_no_tax";
        } else {
            // �ō���
            return "discount_detail_value_in_tax";
        }
    }

    /**
     * �Ŕ����A�ō��ݑΉ������񐶐�
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return �Ŕ����A�ō��ݑΉ�������
     * @exception Exception
     */
    public String makeValueString(ReportParameterBean paramBean, String dsName, String dsdName, String taxDateName) throws Exception {
        return makeValueString(paramBean);
    }

    /**
     * �Ŕ����A�ō��ݑΉ������񐶐�
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return �Ŕ����A�ō��ݑΉ�������
     * @exception Exception
     */
    public String makeDiscountString(ReportParameterBean paramBean) throws Exception {
        // �Ŕ���
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {

            return "discount_value_no_tax";
        } else {
            // �ō���
            return "discount_value";
        }

    }


    /**
     * �w��~���b���s���~�߂�
     *
     * @param msec ���s���~�߂鎞��(�~���b)
     */
    public synchronized void sleep(long msec) {
        try {
            wait(msec);
        } catch (InterruptedException e) {
        }
    }
}
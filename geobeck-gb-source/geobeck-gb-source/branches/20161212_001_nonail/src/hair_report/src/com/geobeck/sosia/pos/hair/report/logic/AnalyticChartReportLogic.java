/*
 * AnalyticChartReportLogic.java
 * Created on 2012/10/30, 13:00
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.report.bean.*;
import java.sql.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.text.SimpleDateFormat;
import com.geobeck.sosia.pos.hair.report.util.*;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.SQLUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFChart;
import org.apache.poi.ss.util.CellRangeAddressBase;
import org.apache.poi.hssf.usermodel.HSSFChart.HSSFSeries;

/**
 *
 * @author ivs
 */
public class AnalyticChartReportLogic {

    /**
     * ����\������
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return true:���� false:�o�͑ΏۂȂ�
     * @exception Exception
     */
    public boolean viewSalesReportAll(ReportParameterBean paramBean) throws IOException, FileNotFoundException, SQLException {

        try {
            //�R�l�N�V�����쐬
            ConnectionWrapper cw = SystemInfo.getConnection();

            //�p�����[�^�[����
            String mainQuery = "";
            String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());
            String endDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetEndDateObj());
            String startDate1 = String.format("%1$tY�N%1$tm��", paramBean.getTargetStartDateObj());
            String endDate1 = String.format("%1$tY�N%1$tm��", paramBean.getTargetEndDateObj());
            int startDate2 = Integer.parseInt(String.format("%1$tm", paramBean.getTargetStartDateObj()));
            int endDate2 = Integer.parseInt(String.format("%1$tm", paramBean.getTargetEndDateObj()));
            String yearStart = String.format("%1$tY", paramBean.getTargetStartDateObj());
            String yearEnd = String.format("%1$tY", paramBean.getTargetEndDateObj());

            //SQL����

            mainQuery += " select year_label as �N, month_label as ��\n";
            mainQuery += " ,sum(tech_only_num+tech_and_item_num) as �Z�p�q��\n";
            mainQuery += " ,sum(new_commer)   as �V�K�q��\n";
            mainQuery += " ,sum(item_only_num+tech_and_item_num) as ���i�q�� ,sum(tech_value) as �Z�p����\n";
            mainQuery += " ,sum(item_value) as ���i����\n";
            mainQuery += " ,sum(fixed2+fixed3+fixed4+fixed5) as �Œ�q��\n";
            mainQuery += " ,sum(tech_only_num+tech_and_item_num)+sum(item_only_num) as ���q��--�Z�p�q��+���i�̂݋q\n";
            mainQuery += " from (     select         extract(month from ds.sales_date) as month_label ,\n";
            mainQuery += "     extract(year from ds.sales_date) as year_label	   ,\n";
            mainQuery += "     count(case when dsd.tech_flg > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id ,ds.shop_id ,ds.sales_date ) = 1\n";
            mainQuery += "     then dsd.slip_no else null end) as new_num	   ,sum(dsd.tech_value) as tech_value	   ,sum(dsd.item_value) as item_value\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and dsd.item_num + dsd.item_crame_num = 0) then 1 else 0 end)  as tech_only_num\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and dsd.item_num + dsd.item_crame_num > 0) then 1 else 0 end)  as tech_and_item_num\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and dsd.item_num + dsd.item_crame_num > 0)	then 1 else 0 end) as tech_item_num\n";
            mainQuery += "     ,sum(case when (dsd.item_num + dsd.item_crame_num > 0 and dsd.tech_num + dsd.tech_crame_num = 0)	then 1 else 0 end) as item_only_num\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1) then 1 else 0 end)           as new_commer\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 2) then 1 else 0 end)        as fixed2\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 3) then 1 else 0 end)        as fixed3\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 4) then 1 else 0 end)        as fixed4\n";
            mainQuery += "     ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) >= 5) then 1 else 0 end)        as fixed5\n";
            mainQuery += "     from     view_data_sales_valid ds\n";
            mainQuery += "     inner join         (\n";
            mainQuery += "         select shop_id	       ,slip_no,\n";
            mainQuery += "         sum(case when product_division = 4 then discount_detail_value_no_tax else 0 end) as item_crame_value\n";
            if (paramBean.getTaxType() == 2) {
                mainQuery += "        ,sum(case when product_division IN(1) then discount_detail_value_in_tax else 0 end) as tech_value\n";
                mainQuery += "        ,sum(case when product_division IN(2) then discount_detail_value_in_tax else 0 end) as item_value\n";
            } else if (paramBean.getTaxType() == 1) {
                mainQuery += "        ,sum(case when product_division IN(1) then discount_detail_value_no_tax else 0 end) as tech_value\n";
                mainQuery += "        ,sum(case when product_division IN(2) then discount_detail_value_no_tax else 0 end) as item_value\n";
            }
            mainQuery += "         ,sum(case when product_division IN(1,3) then 1 else 0 end) as tech_flg\n";
            mainQuery += "         ,sum(case when product_division = 1 then 1 else 0 end) as tech_num\n";
            mainQuery += "         ,sum(case when product_division = 3 then 1 else 0 end) as tech_crame_num\n";
            mainQuery += "         ,sum(case when product_division = 2 then 1 else 0 end) as item_num\n";
            mainQuery += "         ,sum(case when product_division = 4 then 1 else 0 end) as item_crame_num\n";
            mainQuery += "         from view_data_sales_detail_valid\n";
            mainQuery += "         where  shop_id in (" + paramBean.getShopIDList() + ")\n";
            mainQuery += "         and sales_date between '" + startDate + "' and '" + endDate + "'\n";
            mainQuery += "         group by shop_id,slip_no) as dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no\n";
            mainQuery += "     left join mst_customer mc	   using(customer_id) group by ds.sales_date ) t\n";
            mainQuery += " group by �N,month_label order by �N,��\n";

            //SQL���s
            ResultSetWrapper rs = cw.executeQuery(mainQuery.toString());


            //���[�J���A�f�[�^�}������
            JExcelApi jx = new JExcelApi("����\������");
            jx.setTemplateFile("/reports/����\������.xls");

            //���[�Ƀp�����[�^�[�ݒ�
            jx.setValue(2, 3, startDate1 + " �` " + endDate1);
            jx.setValue(2, 4, paramBean.getTargetName());
            jx.setValue(2, 5, paramBean.getTypeCondi());
            if (paramBean.getTaxType() == 2) {
                jx.setValue(2, 6, "�ō�");
            } else if (paramBean.getTaxType() == 1) {
                jx.setValue(2, 6, "�Ŕ�");
            }

            int colNum = 0;

            //���[�̌��̃^�C�g���ݒ�
            if (startDate2 != 1 && endDate2 != 12) {
                for (int i = startDate2; i <= 12; i++) {
                    jx.setValue(2 + colNum, 27, String.valueOf(i) + "��");
                    colNum++;
                }

                for (int i = 1; i <= endDate2; i++) {
                    jx.setValue(2 + colNum, 27, String.valueOf(i) + "��");

                    colNum++;
                }
            } else {
                for (int i = 1; i <= 12; i++) {
                    jx.setValue(2 + colNum, 27, String.valueOf(i) + "��");
                    colNum++;
                }
            }

            int i = 0;
            boolean flagdata = false;
            // ���[�Ƀf�[�^�}������
            while (rs.next()) {
                flagdata = true;
                String month = String.valueOf(rs.getInt("��")) + "��";
                if (month.equals(jx.getValue(2 + i, 27))) {
                    jx.setValue(2 + i, 28, rs.getDouble("�Z�p����"));
                    jx.setValue(2 + i, 29, rs.getDouble("���i����"));
                    jx.setValue(2 + i, 34, rs.getDouble("�Z�p�q��"));
                    jx.setValue(2 + i, 35, rs.getDouble("�V�K�q��"));
                    jx.setValue(2 + i, 36, rs.getDouble("�Œ�q��"));
                    jx.setValue(2 + i, 37, rs.getDouble("���i�q��"));
                    jx.setValue(2 + i, 38, rs.getDouble("���q��"));
                    i++;
                }
            }
            colNum = 0;

            //���[�̌��̃^�C�g���ݒ�
            if (startDate2 != 1 && endDate2 != 12) {
                for (i = startDate2; i <= 12; i++) {
                    jx.setValue(2 + colNum, 27, yearStart + "�N" + String.valueOf(i) + "��");
                    colNum++;
                }

                for (i = 1; i <= endDate2; i++) {
                    jx.setValue(2 + colNum, 27, yearEnd + "�N" + String.valueOf(i) + "��");

                    colNum++;
                }
            } else {
                for (i = 1; i <= 12; i++) {
                    jx.setValue(2 + colNum, 27, yearStart + "�N" + String.valueOf(i) + "��");
                    colNum++;
                }
            }

            if (flagdata) {
                //���[���J��
                jx.openWorkbook();
                return true;
            } else {

                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return true;


    }
    //An start add 20130320

    public boolean viewSalesReportAllRaPa(ReportParameterBean paramBean) throws IOException, FileNotFoundException, SQLException {

        try {
            //�R�l�N�V�����쐬
            ConnectionWrapper cw = SystemInfo.getConnection();

            //�p�����[�^�[����
            String mainQuery = "";
            String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());

            //SQL����
            mainQuery += " select ";
            mainQuery += " sum(tech_value) as �Z�p";
            mainQuery += " ,sum(item_value) as ���i";

            mainQuery += " ,sum(course_value) as �_��";
            mainQuery += " ,sum(digestion_value) as ����";

            mainQuery += " from (";
            mainQuery += "     select";
            mainQuery += "         extract(month from ds.sales_date) as month_label ,extract(year from ds.sales_date) as year_label";
            mainQuery += "	   ,count(case when dsd.tech_flg > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id ,ds.shop_id ,ds.sales_date ) = 1";
            mainQuery += "	   then dsd.slip_no else null end) as new_num";
            mainQuery += "	   ,sum(dsd.tech_value) as tech_value";
            mainQuery += "	   ,sum(dsd.item_value) as item_value";

            mainQuery += "	   ,sum(dsd.course_value) as course_value";
            mainQuery += "	   ,sum(dsd.digestion_value) as digestion_value";

            mainQuery += "	   ,sum(case when (dsd.tech_num + dsd.tech_crame_num > 0 and dsd.item_num + dsd.item_crame_num > 0)";
            mainQuery += "	   then 1 else 0 end) as tech_item_num";
            mainQuery += "	   ,sum(case when (dsd.item_num + dsd.item_crame_num > 0 and dsd.tech_num + dsd.tech_crame_num = 0)";
            mainQuery += "	   then 1 else 0 end) as item_only_num";
            mainQuery += "     from     view_data_sales_valid ds";
            mainQuery += "     inner join";
            mainQuery += "         (";
            mainQuery += "         select shop_id";
            mainQuery += "	       ,slip_no";

            mainQuery += ",sum(case when product_division IN(1) then discount_detail_value_in_tax else 0 end) as tech_value";
            mainQuery += ",sum(case when product_division IN(2) then discount_detail_value_in_tax else 0 end) as item_value";
            mainQuery += ",sum(case when product_division IN(5) then discount_detail_value_in_tax else 0 end) as course_value";
            mainQuery += ",sum(case when product_division IN(6) then discount_detail_value_in_tax else 0 end) as digestion_value";



            mainQuery += "		,sum(case when product_division IN(1,3) then 1 else 0 end) as tech_flg";
            mainQuery += "		,sum(case when product_division = 1 then 1 else 0 end) as tech_num";
            mainQuery += "		,sum(case when product_division = 3 then 1 else 0 end) as tech_crame_num";
            mainQuery += "		,sum(case when product_division = 2 then 1 else 0 end) as item_num";
            mainQuery += "		,sum(case when product_division = 4 then 1 else 0 end) as item_crame_num";
            mainQuery += "         from view_data_sales_detail_valid";
            mainQuery += "	   where  shop_id in (" + paramBean.getShopId() + ")";
            mainQuery += "	      and EXTRACT(MONTH from sales_date) = (select EXTRACT(MONTH  from (date" + SQLUtil.convertForSQL(startDate) + ")))";
            mainQuery += "	      and EXTRACT(YEAR from sales_date) = (select EXTRACT(YEAR  from (date" + SQLUtil.convertForSQL(startDate) + ")))";
            mainQuery += "         group by shop_id,slip_no) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no";
            mainQuery += "	   left join mst_customer mc";
            mainQuery += "	   using(customer_id) group by ds.sales_date";
            mainQuery += " ) t";


            //SQL���s
            ResultSetWrapper rs = cw.executeQuery(mainQuery.toString());
            System.out.println(mainQuery.toString());
            if (rs.first()) {


                paramBean.setTargetTechnic(rs.getInt("�Z�p"));
                paramBean.setTargetItem(rs.getInt("���i"));
                paramBean.setTargetCourse(rs.getInt("�_��"));
                paramBean.setTargetDigestion(rs.getInt("����"));

            }



        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return true;


    }
    //An end add 20130320

    public enum TechnicClass {

        �J�b�g,
        �J���[,
        �p�[�},
        �X�g���[�g,
        �g���[�g�����g,
        ���̑�,
        // ThuanNK start add 20140321
        �l�C��,
        // ThuanNK end add 20140321
        ���v
    }

    /**
     * ���ޕʔ���\�� Luc Add 20121011
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return true:���� false:�o�͑ΏۂȂ�
     * @exception Exception
     */
    public boolean viewSalesReportByCategory(ReportParameterBean paramBean) throws IOException, FileNotFoundException, SQLException {

        try {
            //�R�l�N�V�����쐬
            ConnectionWrapper cw = SystemInfo.getConnection();

            //�p�����[�^�[����
            String mainQuery = "";
            String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());
            String endDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetEndDateObj());
            String startDate1 = String.format("%1$tY�N%1$tm��", paramBean.getTargetStartDateObj());
            String endDate1 = String.format("%1$tY�N%1$tm��", paramBean.getTargetEndDateObj());
            int startDate2 = Integer.parseInt(String.format("%1$tm", paramBean.getTargetStartDateObj()));
            int endDate2 = Integer.parseInt(String.format("%1$tm", paramBean.getTargetEndDateObj()));
            String yearStart = String.format("%1$tY", paramBean.getTargetStartDateObj());
            String yearEnd = String.format("%1$tY", paramBean.getTargetEndDateObj());

            String techicClass = "";
            //SQL����
            mainQuery += " select year_label as �N,";
            mainQuery += "     month_label as ��";
            mainQuery += "     ,tch.technic_class_name as ����";
            mainQuery += "     ,sum(sales_value) as ����";
            mainQuery += " from (";
            mainQuery += "     select";
            mainQuery += "	       extract(month from ds.sales_date) as month_label,extract(year from ds.sales_date) as year_label";
            mainQuery += "         ,dsd.technic_class_id";
            mainQuery += "         , sum(dsd.sale_value) as sales_value";
            mainQuery += "	   from     view_data_sales_valid ds";
            mainQuery += "     inner join";
            mainQuery += "	       (";
            mainQuery += "	       select shop_id";
            mainQuery += "	           ,slip_no";
            mainQuery += "		   ,technic_class_id";
            if (paramBean.getTaxType() == 2) {
                mainQuery += "		   ,sum(discount_detail_value_in_tax) as sale_value";
            } else if (paramBean.getTaxType() == 1) {
                mainQuery += "		   ,sum(discount_detail_value_no_tax) as sale_value";
            }

            mainQuery += "         from view_data_sales_detail_valid";
            mainQuery += "	       left join mst_technic on product_id = technic_id";
            //VTAn Start add
            mainQuery += "	       where  shop_id in (" + paramBean.getShopIDList() + ")  and product_division in (1,3)";
            mainQuery += "	           and sales_date between '" + startDate + "'and '" + endDate + "'";
            //VTAN end add
            //Luc start delete 20130613
            //mainQuery += "	           and technic_class_id in (31,32,33,34,35,40)";
            //Luc end delete 20130613
            mainQuery += "         group by shop_id,slip_no, technic_class_id) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no";
            mainQuery += "	       left join mst_customer mc";
            mainQuery += "	       using(customer_id)";
            mainQuery += "	       group by ds.sales_date, dsd.technic_class_id";
            mainQuery += " ) t";
            mainQuery += " inner join mst_technic_class tch on t.technic_class_id = tch.technic_class_id";
            mainQuery += " group by �N,month_label,technic_class_name,tch.display_seq";
            mainQuery += " order by tch.display_seq";

            //SQL���s
            ResultSetWrapper rs = cw.executeQuery(mainQuery.toString());

            HashMap<String, Integer> paramMap = new HashMap<String, Integer>();
            int row = 38;
            int rowIndex = 0;
            rs.beforeFirst();
            while (rs.next()) {
                if(!paramMap.containsKey(rs.getString("����"))) {
                    paramMap.put(rs.getString("����"), row);
                    row ++;
                    rowIndex ++;
                }
            }
            //���[���J���A�f�[�^�}������
            JExcelApi jx = new JExcelApi("���ޕʔ��㐄��");
            if (rowIndex < 7) {
                jx.setTemplateFile("/reports/���ޕʔ��㐄��.xls");
            }else if(rowIndex > 6 && rowIndex < 26) {
                jx.setTemplateFile("/reports/���ޕʔ��㐄��" +rowIndex+ ".xls");
            }else if ( rowIndex > 25) {
                jx.setTemplateFile("/reports/���ޕʔ��㐄��_about25.xls");
            }else if (rowIndex > 30) {
                MessageDialog.showMessageDialog(null,
                        "���[�̐��`�����Ă��܂��B\n"
                         + "�V�X�e���Ǘ��҂ɘA�����Ă��������B",
                        "���ޕʔ��㐄��", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            jx.setValue(2, 3, startDate1 + " �` " + endDate1);
            jx.setValue(2, 4, paramBean.getTargetName());
            jx.setValue(2, 5, paramBean.getTypeCondi());
            if (paramBean.getTaxType() == 2) {
                jx.setValue(2, 6, "�ō�");
            } else if (paramBean.getTaxType() == 1) {
                jx.setValue(2, 6, "�Ŕ�");
            }

            int colNum = 0;

            //���[�̌��̃^�C�g����ݒ�
            if (startDate2 != 1 && endDate2 != 12) {
                for (int i = startDate2; i <= 12; i++) {
                    jx.setValue(2 + colNum, 36, String.valueOf(i) + "��");
                    colNum += 2;
                }

                for (int i = 1; i <= endDate2; i++) {
                    jx.setValue(2 + colNum, 36, String.valueOf(i) + "��");

                    colNum += 2;
                }

            } else {
                for (int i = 1; i <= 12; i++) {
                    jx.setValue(2 + colNum, 36, String.valueOf(i) + "��");
                    colNum += 2;
                }
            }
            
            
            int i = 0;
            boolean flagdata = false;
            rs.beforeFirst();
            while (rs.next()) {
                flagdata = true;
                //���[�Ƀf�[�^�}������
                techicClass = rs.getString("����");
                String month = String.valueOf(rs.getInt("��")) + "��";

                if(paramMap.containsKey(rs.getString("����"))) {
                    jx.setValue(1, paramMap.get(rs.getString("����")), techicClass);
                    for (int j = 2; j <= 24; j++) {
                        if (month.equals(jx.getValue(j, 36))) {
                            jx.setValue(j, paramMap.get(rs.getString("����")), rs.getInt("����"));
                        }
                    }
                }
                i++;
            }

            colNum = 0;

            //���[�̌��̃^�C�g����ݒ�
            if (startDate2 != 1 && endDate2 != 12) {
                for (i = startDate2; i <= 12; i++) {
                    jx.setValue(2 + colNum, 36, yearStart + "�N" + String.valueOf(i) + "��");
                    colNum += 2;
                }

                for (i = 1; i <= endDate2; i++) {
                    jx.setValue(2 + colNum, 36, yearEnd + "�N" + String.valueOf(i) + "��");

                    colNum += 2;
                }

            } else {
                for (i = 1; i <= 12; i++) {
                    jx.setValue(2 + colNum, 36, yearStart + "�N" + String.valueOf(i) + "��");
                    colNum += 2;
                }
            }

            if (flagdata) {
                //�f�[�^������ꍇ�͒��[���J��
                jx.openWorkbook();
                return true;
            } else {
                //�f�[�^���Ȃ��ꍇ�̓��b�Z�[�W�\��
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public enum YearLable {

        ���N,
        �O�N,
        �O�X�N
    }

    /**
     * Z�`���[�g���� Luc Add 20121011
     *
     * @param paramBean ���|�[�g�p�����[�^Bean
     * @return true:���� false:�o�͑ΏۂȂ�
     * @exception Exception
     */
    public boolean viewSalesReportByZchart(ReportParameterBean paramBean) throws IOException, FileNotFoundException, SQLException {

        try {
            //�R�l�N�V�����쐬
            ConnectionWrapper cw = SystemInfo.getConnection();

            //�p�����[�^�[����
            String mainQuery = "";
            String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());
            String endDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetEndDateObj());
            String startDate1 = String.format("%1$tY�N%1$tm��", paramBean.getTargetStartDateObj());
            String endDate1 = String.format("%1$tY�N%1$tm��", paramBean.getTargetEndDateObj());
            String subStartDate1 = paramBean.getTargetStartDate();
            String subEndDate1 = paramBean.getTargetEndDate();
            String subStartDate2 = paramBean.getTargetStartMonth();
            String subEndDate2 = paramBean.getTargetEndMonth();
            int startDate2 = Integer.parseInt(String.format("%1$tm", paramBean.getTargetStartDateObj()));
            int endDate2 = Integer.parseInt(String.format("%1$tm", paramBean.getTargetEndDateObj()));
            String yearStart = String.format("%1$tY", paramBean.getTargetStartDateObj());
            String yearEnd = String.format("%1$tY", paramBean.getTargetEndDateObj());
            // ThuanNK start add 20140307
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                cal.setTime(sdf.parse(startDate));
            } catch (ParseException ex) {
                Logger.getLogger(AnalyticChartReportLogic.class.getName()).log(Level.SEVERE, null, ex);
            }
            // ThuanNK end add 20140307
            String subQuery = "";
            String techicClass = "";

            // �i���N��������A�O�N��������A�O�X�N��������j�`���[�g�f�[�^�擾SQL
            mainQuery += " select ";
            mainQuery += " year_label as ���� ";
            mainQuery += " , month_label as �� ";
            mainQuery += " , sum(sales_value) as �������� ";
            mainQuery += " from (  ";
            mainQuery += " select  '���N' as year_label ";
            mainQuery += " , extract(month from ds.sales_date) as month_label ";
            mainQuery += " , sum(dsd.sale_value) as sales_value ";
            mainQuery += " from     view_data_sales_valid ds ";
            mainQuery += " inner join ";
            mainQuery += " ( ";
            mainQuery += " select shop_id ";
            mainQuery += " ,slip_no ";

            if (paramBean.getTaxType() == 2) {
                // ThuanNK start edit 20140307
                mainQuery += " ,sum(case product_division when 1 then\n";
                mainQuery += "discount_detail_value_in_tax else 0 end) ";
                mainQuery += " + sum(case product_division when 2 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 3 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 4 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += "as sale_value ";
                // ThuanNK end edit 20140307
            } else if (paramBean.getTaxType() == 1) {
                mainQuery += " ,sum(case product_division when 1 then\n";
                mainQuery += "discount_detail_value_no_tax else 0 end) ";
                mainQuery += " + sum(case product_division when 2 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 3 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 4 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += "as sale_value ";
            }

            mainQuery += " from view_data_sales_detail_valid ";
            mainQuery += " where  shop_id in (" + paramBean.getShopIDList() + ") ";
            mainQuery += " and sales_date between '" + startDate + "' and '" + endDate + "' ";
            mainQuery += " group by shop_id,slip_no) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no ";
            mainQuery += " left join mst_customer mc ";
            mainQuery += " using(customer_id) ";
            mainQuery += " group by ds.sales_date ";
            mainQuery += " union all ";
            mainQuery += " select  	'�O�N' as year_label ";
            mainQuery += " , extract(month from ds.sales_date) as month_label ";
            mainQuery += " , sum(dsd.sale_value) as sales_value ";
            mainQuery += " from     view_data_sales_valid ds ";
            mainQuery += " inner join ";
            mainQuery += " ( ";
            mainQuery += " select shop_id ";
            mainQuery += " ,slip_no ";

            if (paramBean.getTaxType() == 2) {
                // ThuanNK start edit 20140307
                mainQuery += " ,sum(case product_division when 1 then\n";
                mainQuery += "discount_detail_value_in_tax else 0 end) ";
                mainQuery += " + sum(case product_division when 2 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 3 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 4 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += "as sale_value ";
                // ThuanNK end edit 20140307
            } else if (paramBean.getTaxType() == 1) {
                mainQuery += " ,sum(case product_division when 1 then\n";
                mainQuery += "discount_detail_value_no_tax else 0 end) ";
                mainQuery += " + sum(case product_division when 2 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 3 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 4 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += "as sale_value ";
            }

            mainQuery += " from view_data_sales_detail_valid ";
            mainQuery += " where  shop_id in (" + paramBean.getShopIDList() + ") ";
            mainQuery += " and sales_date between '" + subStartDate1 + "' and '" + subEndDate1 + "' ";
            mainQuery += " group by shop_id,slip_no) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no ";
            mainQuery += " left join mst_customer mc ";
            mainQuery += " using(customer_id) ";
            mainQuery += " group by ds.sales_date ";
            mainQuery += " union all ";
            mainQuery += " select  	'�O�X�N' as year_label ";
            mainQuery += " , extract(month from ds.sales_date) as month_label ";
            mainQuery += " , sum(dsd.sale_value) as sales_value ";
            mainQuery += " from     view_data_sales_valid ds ";
            mainQuery += " inner join ";
            mainQuery += " ( ";
            mainQuery += " select shop_id ";
            mainQuery += " ,slip_no ";

            if (paramBean.getTaxType() == 2) {
                // ThuanNK start edit 20140307
                mainQuery += " ,sum(case product_division when 1 then\n";
                mainQuery += "discount_detail_value_in_tax else 0 end) ";
                mainQuery += " + sum(case product_division when 2 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 3 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 4 then\n";
                mainQuery += "discount_detail_value_in_tax  else 0 end) ";
                mainQuery += "as sale_value ";
                // ThuanNK end edit 20140307
            } else if (paramBean.getTaxType() == 1) {
                mainQuery += " ,sum(case product_division when 1 then\n";
                mainQuery += "discount_detail_value_no_tax else 0 end) ";
                mainQuery += " + sum(case product_division when 2 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 3 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += " + sum(case product_division when 4 then\n";
                mainQuery += "discount_detail_value_no_tax  else 0 end) ";
                mainQuery += "as sale_value ";
            }

            mainQuery += " from view_data_sales_detail_valid ";
            mainQuery += " where  shop_id in (" + paramBean.getShopIDList() + ") ";
            mainQuery += " and sales_date between '" + subStartDate2 + "' and '" + subEndDate2 + "' ";
            mainQuery += " group by shop_id,slip_no) dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no ";
            mainQuery += " left join mst_customer mc ";
            mainQuery += " using(customer_id) ";
            mainQuery += " group by ds.sales_date ";
            mainQuery += " ) t ";
            mainQuery += " group by year_label, month_label ";
            mainQuery += " order by (case when year_label = '���N' then 0 ";
            mainQuery += " when year_label = '�O�N' then 1 ";
            mainQuery += " else 2 end ), month_label ";

            // �����ڕW����̃f�[�^SQL
            subQuery += " select a.month as ��, sum(coalesce(target_item,0)) as �����ڕW���� ";
            subQuery += " from ( ";
            subQuery += " select 1 as month ";
            subQuery += " union ";
            subQuery += " select 2 as month ";
            subQuery += " union ";
            subQuery += " select 3 as month ";
            subQuery += " union ";
            subQuery += " select 4 as month ";
            subQuery += " union ";
            subQuery += " select 5 as month ";
            subQuery += " union ";
            subQuery += " select 6 as month ";
            subQuery += " union ";
            subQuery += " select 7 as month ";
            subQuery += " union ";
            subQuery += " select 8 as month ";
            subQuery += " union ";
            subQuery += " select 9 as month ";
            subQuery += " union ";
            subQuery += " select 10 as month ";
            subQuery += " union ";
            subQuery += " select 11 as month ";
            subQuery += " union ";
            subQuery += " select 12 as month ";
            subQuery += " ) a ";
            subQuery += " left join ";
            subQuery += " ( ";
            subQuery += " select ";
            subQuery += " target_item,month, cast(year as varchar(4)) as year ";
            subQuery += " from data_target_result ";
            subQuery += " where ";
            subQuery += " shop_id in (" + paramBean.getShopIDList() + ") ";
            subQuery += " and (cast(year as varchar(4))||trim(to_char(month ,'00'))) between '" + startDate + "' and '" + endDate + "' ";
            subQuery += " ) b on a.month = b.month ";
            subQuery += " group by b.year,a.month ";
            subQuery += " order by a.month ";

            //SQL���s
            ResultSetWrapper rs = cw.executeQuery(mainQuery.toString());
            ResultSetWrapper rs1 = cw.executeQuery(subQuery.toString());

            //Excel���J���A�f�[�^�}������
            JPOIApi jx = new JPOIApi("Z�`���[�g");
            jx.setTemplateFile("/reports/Z�`���[�g.xls");

            //Excel�Ƀp�����[�^�[��}������
            jx.setCellValue(2, 3, startDate1 + " �` " + endDate1);
            jx.setCellValue(2, 4, paramBean.getTargetName());
            if (paramBean.getTaxType() == 2) {
                jx.setCellValue(2, 5, "�ō�");
            } else if (paramBean.getTaxType() == 1) {
                jx.setCellValue(2, 5, "�Ŕ�");
            }

            int colNum = 0;
            //���[�̌��̃^�C�g���ݒ�
            if (startDate2 != 1 && endDate2 != 12) {
                for (int i = startDate2; i <= 12; i++) {
                    jx.setCellValue(2 + colNum, 72, String.valueOf(i) + "��");
                    colNum++;
                }

                for (int i = 1; i <= endDate2; i++) {
                    jx.setCellValue(2 + colNum, 72, String.valueOf(i) + "��");

                    colNum++;
                }
            } else {
                for (int i = 1; i <= 12; i++) {
                    jx.setCellValue(2 + colNum, 72, String.valueOf(i) + "��");
                    colNum++;
                }
            }

            int month;
            String nameColFormat = "";
            int value;
            int count = 0;

            //���[�ɍ��N��������A�O�N��������A�O�X�N��������̃f�[�^�}������
            while (rs.next()) {
                count = 1;
                techicClass = rs.getString("����");
                switch (YearLable.valueOf(techicClass)) {
                    case ���N:
                        month = rs.getInt("��");
                        nameColFormat = String.valueOf(month) + "��";
                        value = rs.getInt("��������");
                        for (int i = 2; i <= 13; i++) {
                            String nameColExcel = jx.getCellValue(i, 72);
                            if (nameColFormat.equals(nameColExcel)) {
                                jx.setCellValue(i, 73, value);
                            }
                        }
                        break;
                    case �O�N:
                        month = rs.getInt("��");
                        nameColFormat = String.valueOf(month) + "��";
                        value = rs.getInt("��������");
                        for (int i = 2; i <= 13; i++) {
                            String nameColExcel = jx.getCellValue(i, 72);
                            if (nameColFormat.equals(nameColExcel)) {
                                jx.setCellValue(i, 76, value);
                            }
                        }
                        break;
                    case �O�X�N:
                        month = rs.getInt("��");
                        nameColFormat = String.valueOf(month) + "��";
                        value = rs.getInt("��������");
                        for (int i = 2; i <= 13; i++) {
                            String nameColExcel = jx.getCellValue(i, 72);
                            if (nameColFormat.equals(nameColExcel)) {
                                jx.setCellValue(i, 79, value);
                            }
                        }
                        break;
                }
            }

            //���[l�Ɍ����ڕW����̃f�[�^�}������
            while (rs1.next()) {
                count = 2;
                month = rs1.getInt("��");
                nameColFormat = String.valueOf(month) + "��";
                value = rs1.getInt("�����ڕW����");
                for (int i = 2; i <= 13; i++) {
                    String nameColExcel = jx.getCellValue(i, 72);
                    if (nameColFormat.equals(nameColExcel)) {
                        jx.setCellValue(i, 80, value);
                    }
                }
            }

            colNum = 0;
            //���[�̌��̃^�C�g���ݒ�
            if (startDate2 != 1 && endDate2 != 12) {
                for (int i = startDate2; i <= 12; i++) {
                    jx.setCellValue(2 + colNum, 72, yearStart + "�N" + String.valueOf(i) + "��");
                    colNum++;
                }

                for (int i = 1; i <= endDate2; i++) {
                    jx.setCellValue(2 + colNum, 72, yearEnd + "�N" + String.valueOf(i) + "��");

                    colNum++;
                }
            } else {
                for (int i = 1; i <= 12; i++) {
                    jx.setCellValue(2 + colNum, 72, yearStart + "�N" + String.valueOf(i) + "��");
                    colNum++;
                }
            }

            //Workbook�̊֐���Active
            jx.setFormularActive();

            if (count > 0) {
                //���[���J��
                jx.openWorkbook();
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private String getSQLAllDiscount(ReportParameterBean paramBean, String startDate, String endDate){
         // ThuanNK
            String sqlGetDiscount = "select\n";
            
            if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
                sqlGetDiscount += // �S�̊���
                        "sum(ds.discount_value_no_tax) as all_discount,\n";
            } //�ō��ݕ\��
            else {
                sqlGetDiscount += "sum(ds.discount_value) as all_discount\n";
            }

            sqlGetDiscount += "from view_data_sales_valid ds\n"
                    + "where ds.shop_id in (" + paramBean.getShopIDList() + ")\n"
                    + "and ds.sales_date between '" + startDate + "' and '" + endDate + "'\n";

            sqlGetDiscount += "     and exists";
            sqlGetDiscount += "         (";
            sqlGetDiscount += "             select 1";
            sqlGetDiscount += "             from";
            sqlGetDiscount += "                 view_data_sales_detail_valid";
            sqlGetDiscount += "             where";
            sqlGetDiscount += "                     shop_id = ds.shop_id";
            sqlGetDiscount += "                 and slip_no = ds.slip_no";
            sqlGetDiscount += "                 and product_division in(1,2)";
            sqlGetDiscount += "         )";
            // ThuanNK
            return sqlGetDiscount;
    }
    /*
     * �G���A���͂ł��q�l�̏�����m�낤
     */
    public boolean viewSalesReportByAreaAnalyticChart(ReportParameterBean paramBean, String straddress) throws IOException, FileNotFoundException, SQLException {
        try {
            //IVS_vtnhan start add 20140723 MASHU_�S���ė�����
            //CATEGORY
            String category = paramBean.getListCategoryId();
            //useCategory
            int useCategory = paramBean.getUseShopCategory();
            // �ė��擾�p�T�u�N�G��(���ׂ�) 
             //IVS_vtnhan end add 20140723 MASHU_�S���ė�����
            //�p�����[�^�[����
            String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());
            String endDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetEndDateObj());
            ConnectionWrapper cw = SystemInfo.getConnection();
            
            StringBuilder sql = new StringBuilder();
            sql.append(" select ");
            sql.append("         b.address2 as address2, count(distinct a.customer_id) as count ");
            sql.append("         From ");
            sql.append("                data_sales a ");
            sql.append("                inner join  mst_customer b ");
            sql.append("                            on a.customer_id = b.customer_id ");
            sql.append("                               and b.address1 ='" + straddress + "'");
            sql.append("                               and b.address2 is not null and b.address2 <>''");
            sql.append("                               and b.delete_date is null ");
            
            sql.append("         where ");
            sql.append("                 a.shop_id in (" + paramBean.getShopIDList() + ")");
            sql.append("                 and a.sales_date BETWEEN '" + startDate + "' AND '" + endDate + "'");
            sql.append("                 and a.delete_date is null");
            
            //nhanvt start add 20141212 New request #32798
            if(useCategory == 1 && category != null && category != ""){
                sql.append("      and   exists");
                sql.append("             (");

                //�Ƒԗ��p���邩���Ȃ����`�F�b�N
           
                sql.append("                 SELECT");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                     data_sales_detail dsd");
                sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                sql.append("                 WHERE");
                sql.append("                         dsd.shop_id = a.shop_id");
                sql.append("                     AND dsd.slip_no = a.slip_no");
                sql.append("                     AND dsd.delete_date is null");
                sql.append("                     AND dsd.product_division in (1,3)");
                sql.append("                     AND mstc.shop_category_id in (" + category + ") ");
                
                sql.append("                  UNION ALL  ");
                sql.append("                  SELECT ");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                      data_sales_detail dsd");
                sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id  ");
                sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id  ");
                sql.append("                  WHERE");
                sql.append("                          dsd.shop_id = a.shop_id");
                sql.append("                      AND dsd.slip_no = a.slip_no");
                sql.append("                      AND dsd.delete_date is null");
                sql.append("                      AND dsd.product_division in (2,4)");
                sql.append("                      AND mic.shop_category_id in (" + category + ") ");
                
                sql.append("                 UNION ALL  ");
                sql.append("                 SELECT ");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                     data_sales_detail dsd");
                sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                sql.append("                 WHERE");
                sql.append("                         dsd.shop_id = a.shop_id");
                sql.append("                     AND dsd.slip_no = a.slip_no");
                sql.append("                     AND dsd.delete_date is null");
                sql.append("                     AND dsd.product_division in (5,6)");
                sql.append("                     AND mscc.shop_category_id in (" + category + ") ");
                sql.append(" ) ");
            }
            //nhanvt end add 20141212 New request #32798
            
            sql.append("                 Group by b.address2");
           
            //SQL���s
            ResultSetWrapper rs = cw.executeQuery(sql.toString());

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Vector<String> data1 = new Vector<String>();
            Vector<String> data2 = new Vector<String>();
            while (rs.next()) {
                data1.addElement(rs.getString("count"));
                data2.addElement(rs.getString("address2"));
            }

            //Excel���J���A�f�[�^�}������
            JExcelApi jx = new JExcelApi("�G���A����");
            jx.setTemplateFile("/reports/�G���A����.xls");

            //���[�Ƀp�����[�^�[�ݒ�
            jx.setValue(1, 3, "�Ώۊ��ԁF" + format.format(paramBean.getTargetStartDateObj()).toString() + " �` " + format.format(paramBean.getTargetEndDateObj()).toString());
            jx.setValue(1, 4, "�ΏۓX��:" + paramBean.getTargetName());
            jx.setValue(1, 5, "�s���{��:" + straddress);

            //vtnhan start
            if(paramBean.isIsHideCategory()){
                jx.setValue(2, 3, "�Ƒԕ��� �F " + paramBean.getListCategoryName() );
            }
            //vtnhan end
            
            int n = data1.size();
            if (n == 0) {
                return false;
            }
            //���[�Ƀf�[�^�}������
            if (n == 1) {
                jx.setValue(2, 9, Integer.parseInt(data1.elementAt(0)));
                jx.setValue(1, 9, data2.elementAt(0));
                jx.removeRow(10);
                jx.openWorkbook();
            } else if (n == 2) {
                jx.setValue(2, 9, Integer.parseInt(data1.elementAt(0)));
                jx.setValue(1, 9, data2.elementAt(0));
                jx.setValue(2, 10, Integer.parseInt(data1.elementAt(1)));
                jx.setValue(1, 10, data2.elementAt(1));
                jx.openWorkbook();
            } else if (n > 2) {

                jx.insertRow(9, n - 2);
                for (int i = 0; i < n; i++) {
                    jx.setValue(2, 9 + i, Integer.parseInt(data1.elementAt(i)));

                }
                for (int i = 0; i < n; i++) {
                    jx.setValue(1, 9 + i, data2.elementAt(i));
                }

                String path = jx.getFilePath();
                jx.openWorkBookHidden();
                InputStream inp = new FileInputStream(jx.getFilePath());
                HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(inp));
                HSSFSheet sheet = workbook.getSheetAt(0);

                //Chart�̃f�[�^Range�ݒ�
                HSSFChart[] charts = HSSFChart.getSheetCharts(sheet);
                for (HSSFChart chart : charts) {
                    chart.setChartHeight(1000);
                    HSSFSeries[] seriesArray = chart.getSeries();
                    for (HSSFSeries series : seriesArray) {
                        CellRangeAddressBase range;
                        range = series.getValuesCellRange();
                        range.setLastRow(range.getLastRow() + n - 2);
                        series.setValuesCellRange(range);
                        range = series.getCategoryLabelsCellRange();
                        range.setLastRow(range.getLastRow() + n - 2);
                        series.setCategoryLabelsCellRange(range);
                    }
                }

                //���[���J��
                FileOutputStream fileOut = new FileOutputStream(path);
                workbook.write(fileOut);
                fileOut.close();
                jx.openWorkbook(path);
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * �ڋq�������͂Ōڋq�w��m�낤
     */
    public boolean viewSalesReportByCustomerAttributeAnalyticChart(ReportParameterBean paramBean, String a) throws IOException, FileNotFoundException, SQLException {
        //IVS_vtnhan start add 20140723 MASHU_�S���ė�����
        //CATEGORY
        String category = paramBean.getListCategoryId();
        //useCategory
        int useCategory = paramBean.getUseShopCategory();
        // �ė��擾�p�T�u�N�G��(���ׂ�) 
         //IVS_vtnhan end add 20140723 MASHU_�S���ė�����
        //�p�����[�^�[����
        String startDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetStartDateObj());
        String endDate = String.format("%1$tY/%1$tm/%1$td", paramBean.getTargetEndDateObj());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        //���[���J���A�f�[�^�}������
        JExcelApi jx = new JExcelApi("�ڋq��������");
        jx.setTemplateFile("/reports/�ڋq��������.xls");

        //���[�Ƀp�����[�^�[�ݒ�
        jx.setValue(2, 3, format.format(paramBean.getTargetStartDateObj()).toString() + " �` " + format.format(paramBean.getTargetEndDateObj()).toString());
        jx.setValue(2, 4, paramBean.getTargetName());

        //vtnhan start
        if(paramBean.isIsHideCategory()){
            jx.setValue(1, 5, " �Ƒԕ��� �F " );
            jx.setValue(2, 5, paramBean.getListCategoryName() );
        }
        //vtnhan end
        //���ʔN��̏����f�[�^�擾SQL

        StringBuilder sql1 = new StringBuilder();
        
        sql1.append( " select");
        sql1.append( " oldgroup, sum(num) as �l�� ");
        sql1.append( " from ");
        sql1.append( " (select extract(year from age(current_date, a.birthday))  as year, ");
        sql1.append( " case when extract(year from age(current_date, a.birthday)) <=14 then 1 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=19 then 2  ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=24 then 3 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=29 then 4 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=34 then 5 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=39 then 6 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=44 then 7 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=49 then 8 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=54 then 9 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) <=59 then 10 ");
        sql1.append( " when extract(year from age(current_date, a.birthday)) >=60 then 11 ");
        sql1.append( " else 12 ");
        sql1.append( " end ");
        sql1.append( " as oldgroup ");
        sql1.append( " ,count(distinct b.customer_id) as num ");
        sql1.append( " from ");
        sql1.append("       mst_customer a ");
        sql1.append("       inner join data_sales b ");
        sql1.append("                             on a.customer_id = b.customer_id ");
        sql1.append("                                and b.shop_id in (" + paramBean.getShopIDList() + " )");
        sql1.append("                                and b.sales_date BETWEEN'" + startDate + "' AND '" + endDate + "'");
        sql1.append("                                and b.delete_date is null ");
        //nhanvt start add 20141212 New request #32798
        if (useCategory == 1 && category != null && category != "") {
            sql1.append("      and   exists");
            sql1.append("             (");
            //�Ƒԗ��p���邩���Ȃ����`�F�b�N
            sql1.append("                 SELECT");
            sql1.append("                     1");
            sql1.append("                 FROM");
            sql1.append("                     data_sales_detail dsd");
            sql1.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id  ");
            sql1.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql1.append("                 WHERE");
            sql1.append("                         dsd.shop_id = b.shop_id");
            sql1.append("                     AND dsd.slip_no = b.slip_no");
            sql1.append("                     AND dsd.delete_date is null");
            sql1.append("                     AND dsd.product_division in (1,3)");
            sql1.append("                     AND mstc.shop_category_id in (" + category + ") ");
           
            sql1.append("                  UNION ALL  ");
            sql1.append("                  SELECT ");
            sql1.append("                     1");
            sql1.append("                 FROM");
            sql1.append("                      data_sales_detail dsd");
            sql1.append("                     inner join mst_item mi on dsd.product_id = mi.item_id ");
            sql1.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
            sql1.append("                  WHERE");
            sql1.append("                          dsd.shop_id = b.shop_id");
            sql1.append("                      AND dsd.slip_no = b.slip_no");
            sql1.append("                      AND dsd.delete_date is null");
            sql1.append("                      AND dsd.product_division in (2,4)");
            sql1.append("                      AND mic.shop_category_id in (" + category + ") ");
            
            sql1.append("                 UNION ALL  ");
            sql1.append("                 SELECT ");
            sql1.append("                     1");
            sql1.append("                 FROM");
            sql1.append("                     data_sales_detail dsd");
            sql1.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
            sql1.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
            sql1.append("                 WHERE");
            sql1.append("                         dsd.shop_id = b.shop_id");
            sql1.append("                     AND dsd.slip_no = b.slip_no");
            sql1.append("                     AND dsd.delete_date is null");
            sql1.append("                     AND dsd.product_division in (5,6)");
            sql1.append("                     AND mscc.shop_category_id in (" + category + ") ");
            sql1.append(" ) ");
        }
        //nhanvt end add 20141212 New request #32798
        sql1.append("        where ");
        sql1.append("             a.sex= 2    ");
        sql1.append("             and a.delete_date is null    ");
        
        sql1.append(" Group by year) as t1 ");
        sql1.append(" Group by oldgroup ");
        sql1.append(" Order by oldgroup ");
        

        //SQL���s
        ConnectionWrapper cw1 = SystemInfo.getConnection();
        ResultSetWrapper rs1 = cw1.executeQuery(sql1.toString());

        //�f�[�^����
        //ThuanNK start edit 2014/02/10
        boolean flg = false;
        while (rs1.next()) {
            flg = true;
            jx.setValue(2, 8 + rs1.getInt("oldgroup"), rs1.getInt("�l��"));
        }
        //ThuanNK end edit 2014/02/10

        //���ʔN��̒j���f�[�^�擾SQL
        StringBuilder sql2 = new StringBuilder();
        
        sql2.append(" select");
        sql2.append(" oldgroup, sum(num) as �l�� ");
        sql2.append(" from ");
        sql2.append(" (select extract(year from age(current_date, a.birthday))  as year, ");
        sql2.append(" case when extract(year from age(current_date, a.birthday)) <=14 then 1 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=19 then 2  ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=24 then 3 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=29 then 4 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=34 then 5 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=39 then 6 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=44 then 7 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=49 then 8 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=54 then 9 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) <=59 then 10 ");
        sql2.append(" when extract(year from age(current_date, a.birthday)) >=60 then 11 ");
        sql2.append(" else 12 ");
        sql2.append(" end ");
        sql2.append(" as oldgroup ");
        sql2.append(" ,count(distinct b.customer_id) as num ");
        sql2.append(" from ");
        sql2.append("      mst_customer a ");
        sql2.append("                   inner join data_sales b ");
        sql2.append("                   on  a.customer_id = b.customer_id ");
        sql2.append("                   and b.shop_id in (" + paramBean.getShopIDList() + " )");
        sql2.append("                   and b.sales_date BETWEEN'" + startDate + "' AND '" + endDate + "'");
        sql2.append("                   and b.delete_date is null ");
        //nhanvt start add 20141212 New request #32798
        //�Ƒԗ��p���邩���Ȃ����`�F�b�N
        if (useCategory == 1 && category != null && category != "") {
            sql2.append("      and   exists");
            sql2.append("             (");
            sql2.append("                 SELECT");
            sql2.append("                     1");
            sql2.append("                 FROM");
            sql2.append("                     data_sales_detail dsd");
            sql2.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id  ");
            sql2.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            
            sql2.append("                 WHERE");
            sql2.append("                         dsd.shop_id = b.shop_id");
            sql2.append("                     AND dsd.slip_no = b.slip_no");
            sql2.append("                     AND dsd.delete_date is null");
            sql2.append("                     AND dsd.product_division in (1,3)");
            sql2.append("                     AND mstc.shop_category_id in (" + category + ") ");
            
            sql2.append("                  UNION ALL  ");
            sql2.append("                  SELECT ");
            sql2.append("                     1");
            sql2.append("                 FROM");
            sql2.append("                      data_sales_detail dsd");
            sql2.append("                     inner join mst_item mi on dsd.product_id = mi.item_id ");
            sql2.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id ");
            sql2.append("                  WHERE");
            sql2.append("                          dsd.shop_id = b.shop_id");
            sql2.append("                      AND dsd.slip_no = b.slip_no");
            sql2.append("                      AND dsd.delete_date is null");
            sql2.append("                      AND dsd.product_division in (2,4)");
            sql2.append("                      AND mic.shop_category_id in (" + category + ") ");
           
            sql2.append("                 UNION ALL  ");
            sql2.append("                 SELECT ");
            sql2.append("                     1");
            sql2.append("                 FROM");
            sql2.append("                     data_sales_detail dsd");
            sql2.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
            sql2.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
            sql2.append("                 WHERE");
            sql2.append("                         dsd.shop_id = b.shop_id");
            sql2.append("                     AND dsd.slip_no = b.slip_no");
            sql2.append("                     AND dsd.delete_date is null");
            sql2.append("                     AND dsd.product_division in (5,6)");
            sql2.append("                     AND mscc.shop_category_id in (" + category + ") ");
            sql2.append(" ) ");
        }
        //nhanvt end add 20141212 New request #32798
        sql2.append("      where a.delete_date is null ");
        sql2.append("           and a.sex= 1 ");
        
        sql2.append(" Group by year) as t1 ");
        sql2.append(" Group by oldgroup ");
        sql2.append(" Order by oldgroup; ");
        
        
        
        //SQL���s
        ConnectionWrapper cw2 = SystemInfo.getConnection();
        ResultSetWrapper rs2 = cw2.executeQuery(sql2.toString());

        //�f�[�^����
        while (rs2.next()) {
            // ThuanNK start edit 2014/02/10
            flg = true;
            jx.setValue(4, 8 + rs2.getInt("oldgroup"), rs2.getInt("�l��"));
            // ThuanNK end edit 2014/02/10
        }

//        //�q�P���f�[�^�擾SQL
        StringBuilder sql3 = new StringBuilder();
        sql3.append(" select valuegroup, count(valuegroup) as �l��  \n");
        sql3.append(" from ( \n");
        sql3.append(" select sum(value ) / count(slip_no) as avgValue, \n");
        sql3.append(" case when sum(value ) / count(slip_no) <=4000 then 1 \n");
        sql3.append("      when sum(value ) / count(slip_no) <=5000 then 2 \n");
        sql3.append("      when sum(value ) / count(slip_no) <=6000 then 3  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=7000 then 4  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=8000 then 5  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=9000 then 6  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=10000 then 7  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=12000 then 8  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=14000 then 9  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=16000 then 10  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=18000 then 11  \n");
        sql3.append("      when sum(value ) / count(slip_no) <=20000 then 12  \n");
        sql3.append("      else 13  end as valuegroup   \n");
        sql3.append("      from (  \n");
        sql3.append("             select ds.slip_no,ds.customer_id,sum((product_value * product_num) - discount_value) as value");
        sql3.append("             from data_sales ds  \n");
        sql3.append("             inner join data_sales_detail dsd using (shop_id,slip_no) \n");
        sql3.append("             where ds.delete_date is null \n");
        sql3.append("             and ds.shop_id in (" + paramBean.getShopIDList() + ") \n");
        sql3.append("             and ds.sales_date BETWEEN'" + startDate + "' AND '" + endDate + "' \n");
        
        //nhanvt start add 20141212 New request #32798
        if (useCategory == 1 && category != null && category != "") {
            sql3.append("      and   exists");
            sql3.append("             (");
            sql3.append("                 SELECT");
            sql3.append("                     1");
            sql3.append("                 FROM");
            sql3.append("                     data_sales_detail dsd1");
            sql3.append("                     inner join mst_technic mst on mst.technic_id = dsd1.product_id  ");
            sql3.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql3.append("                 WHERE");
            sql3.append("                         dsd1.shop_id = dsd.shop_id");
            sql3.append("                     AND dsd1.slip_no = dsd.slip_no");
            sql3.append("                     and dsd1.slip_detail_no = dsd.slip_detail_no   ");
            sql3.append("                     AND dsd1.delete_date is null");
            sql3.append("                     AND dsd1.product_division in (1,3)");
            sql3.append("                           AND mstc.shop_category_id in (" + category + ") ");
           
            
            sql3.append("                  UNION ALL  ");
            sql3.append("                  SELECT ");
            sql3.append("                     1");
            sql3.append("                 FROM");
            sql3.append("                      data_sales_detail dsd1");
            sql3.append("                     inner join mst_item mi on dsd1.product_id = mi.item_id ");
            sql3.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id ");
            sql3.append("                  WHERE");
            sql3.append("                          dsd1.shop_id = dsd.shop_id");
            sql3.append("                      AND dsd1.slip_no = dsd.slip_no");
            sql3.append("                     and dsd1.slip_detail_no = dsd.slip_detail_no   ");
            sql3.append("                      AND dsd1.delete_date is null");
            sql3.append("                      AND dsd1.product_division in (2,4)");
            sql3.append("                      AND mic.shop_category_id in (" + category + ") ");
            
            sql3.append("                 UNION ALL  ");
            sql3.append("                 SELECT ");
            sql3.append("                     1");
            sql3.append("                 FROM");
            sql3.append("                     data_sales_detail dsd1");
            sql3.append("                     inner join mst_course msc on msc.course_id = dsd1.product_id  ");
            sql3.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
            sql3.append("                 WHERE");
            sql3.append("                         dsd1.shop_id = dsd.shop_id");
            sql3.append("                     AND dsd1.slip_no = dsd.slip_no");
            sql3.append("                     and dsd1.slip_detail_no = dsd.slip_detail_no   ");
            sql3.append("                     AND dsd1.delete_date is null");
            sql3.append("                     AND dsd1.product_division in (5,6)");
            sql3.append("                     AND mscc.shop_category_id in (" + category + ") ");
           
            sql3.append(" ) ");
        }
        //nhanvt end add 20141212 New request #32798
        
        sql3.append("             group by ds.slip_no,ds.customer_id ) a \n");
        sql3.append("             group by customer_id ) b \n");
        sql3.append("             group by valuegroup  order by valuegroup \n");
        // ThuanNK end edit 2014/02/10
        

        //SQL���s
        ConnectionWrapper cw3 = SystemInfo.getConnection();
        ResultSetWrapper rs3 = cw3.executeQuery(sql3.toString());

        //�f�[�^����
        // ThuanNK start add 2013/02/10
        while (rs3.next()) {
            flg = true;
            jx.setValue(2, 38 + rs3.getInt("valuegroup"), rs3.getInt("�l��"));
        }
        if (!flg) {
            return false;
        }
        // ThuanNK end add 2013/02/10

        //���[���J��
        jx.openWorkbook();

        return true;
    }
}

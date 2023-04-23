/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.report.custom;

import com.geobeck.sosia.pos.hair.report.util.JPOIApi;
import com.geobeck.sosia.pos.master.account.MstTax;
import com.geobeck.sosia.pos.master.account.MstTaxs;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.apache.poi.hssf.util.HSSFColor;

import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.*;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author yasumoto
 */
public class ModestyReportLogic {

    private ModestyReportPanel parentPanel;

    /**
     * �R�~�b�V�������׏�
     *
     * @param shop
     * @param staff
     * @param targetDate
     * @return
     */
    protected boolean printMonthlyCommission(MstShop shop, MstStaff staff, Date targetDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar td = Calendar.getInstance();
        td.setTime(targetDate);

        SimpleDateFormat sdft = new SimpleDateFormat("yyyy�NMM");
        String month = sdft.format(td.getTime());

        // ����
        td.set(Calendar.DATE, 1);
        Date bef = td.getTime();
        // ����
        td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
        Date aft = td.getTime();
        // ���O�o��
        SystemInfo.getLogger().info(sdf.format(bef) + " to " + sdf.format(aft) + " - "
                + shop.getShopName() + " - " + staff.getStaffNo() + " : " + staff.getStaffName(0));

        //------------------------------
        // �f�[�^�擾
        //------------------------------
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
        try {
            StringBuilder sql = new StringBuilder(4000);
            sql.append(" select  ");
            sql.append(" detail.shop_id, mshop.shop_name ,sales_date");
            sql.append(",customer_no ");
            sql.append(",customer_name1 || customer_name2 as customer_name ");
            sql.append(",slip_no ");
            sql.append(",SUM(detail_value_no_tax) as price ");
            sql.append(",rtype ");
            sql.append(",introducer_no ");
            sql.append(",introducer_name1 || introducer_name2 as introducer_name ");
            sql.append(",(select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = (CASE WHEN staff='" + staff.getStaffID() + "' THEN stf ELSE staff END)) as staffb ");
            sql.append(",sum(cnt) as cnts");
            sql.append(" from ");
            sql.append("( "); // ���ז��̑Ώۃf�[�^���擾
            sql.append(" select ");
            sql.append(" dsd.shop_id, dsd.slip_no, dsd.slip_detail_no, ds.sales_date, dsd.staff_id as stf ");
            sql.append(" , ( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + COALESCE(dsd.tax_rate, get_tax_rate(ds.sales_date)) )))   ");
            sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + COALESCE(dsd.tax_rate, get_tax_rate(ds.sales_date)) ))))) as detail_value_no_tax   ");
            sql.append(",mc.customer_no, mc.customer_name1, mc.customer_name2 ");
            sql.append(",mc2.customer_no as introducer_no, mc2.customer_name1 as introducer_name1, mc2.customer_name2 as introducer_name2 ");
            sql.append(",COALESCE(dsdp.staff_id, dcss.staff_id) as staff, free_heading_name as rtype ,ds.customer_id ");
            sql.append(" from ");// �̔��ڍ�
            sql.append(" data_sales_detail dsd  ");
            sql.append(" inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no ");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id ");
            sql.append(" left outer join mst_customer mc2 on mc.introducer_id = mc2.customer_id ");
            sql.append(" left outer join data_sales_detail_proportionally dsdp on dsd.product_division in (2,4) and dsd.shop_id = dsdp.shop_id and  dsd.slip_no = dsdp.slip_no and  dsd.slip_detail_no = dsdp.slip_detail_no and dsd.staff_id <> dsdp.staff_id ");
            sql.append(" left outer join data_contract dc on dc.shop_id = dsd.shop_id and dc.slip_no = dsd.slip_no and dsd.product_division = 5 and dsd.product_id = dc.product_id ");
            sql.append(" left outer join data_contract_staff_share dcss on dsd.shop_id = dcss.shop_id  and dc.slip_no = dcss.slip_no and dc.contract_no = dcss.contract_no and dc.contract_detail_no = dcss.contract_detail_no and dsd.staff_id <> dcss.staff_id ");
            sql.append(" left outer join mst_customer_free_heading mcfh on ds.customer_id = mcfh.customer_id and mcfh.free_heading_class_id = '4' ");
            sql.append(" left outer join mst_free_heading mfh on mcfh.free_heading_class_id = mfh.free_heading_class_id and mcfh.free_heading_id = mfh.free_heading_id ");
            sql.append(" where ");
            sql.append(" ( ");// �Z�por�Z�p�N���[�� and �A�C���b�V�����or�t���[���
            sql.append(" ( dsd.product_division in (1,2) and exists(select technic_id from mst_technic mt where mt.technic_class_id in (2,4) and dsd.product_id = mt.technic_id ) ) ");
            sql.append(" or ");// ���ior���i�ԕi���E�L�ȊO�̏��i�i�K�E���{�Ɩ��p�{���̑��{�C���I�[���{�Ј����������W�v���Ȃ��j
            // 20170602 GB START EDIT #4331 ���f�X�e�B���J�X�^�}�C�Y���[�@�R�~�b�V�������׏��d�l�ύX��]
            //sql.append(" ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (10,12,14,15,16,39,46,55,57,60,64,65,66,67,68,69,70,71,72,73,74,75) and dsd.product_id = mt.item_id ) ) ");
            sql.append(" ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (1,2,3,10,12,13,14,15,16,18,22,39,40,42,43,45,54,55,56,57,60,64,65,66,67,68,69,70,71,72,73,74,75,78) and dsd.product_id = mt.item_id ) ) ");
            // 20170602 GB END EDIT #4331 ���f�X�e�B���J�X�^�}�C�Y���[�@�R�~�b�V�������׏��d�l�ύX��]
            sql.append(" or ");// �_�񁄂��ׂĂ̕���
            sql.append(" dsd.product_division = 5) ");
            sql.append(" and (dsd.staff_id ='" + staff.getStaffID() + "' or dsdp.staff_id ='" + staff.getStaffID() + "' or dcss.staff_id ='" + staff.getStaffID() + "' ) ");
            sql.append(" and ds.sales_date >= '" + sdf.format(bef) + "' and ds.sales_date <= '" + sdf.format(aft) + "' ");
            sql.append("  ) detail ");
            sql.append(" inner join mst_shop mshop on mshop.shop_id = detail.shop_id ");
            // �ߋ��Ɍ_�񓙂�����ڋq���擾
            sql.append(" left outer join");
            sql.append(" (select ds.customer_id, count(ds.customer_id) as cnt");
            sql.append(" from ");// �̔��ڍ�
            sql.append(" data_sales_detail dsd  ");
            sql.append(" inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no ");
            //�S���[�U�[���Əd���̂ō����A�N�e�B�u�Ȑl�ōi�荞��
            sql.append(" inner join data_sales ds2 on ds2.customer_id = ds.customer_id and ds2.sales_date >= '" + sdf.format(bef) + "' and ds2.sales_date <= '" + sdf.format(aft) + "'");
            sql.append(" where ");
            sql.append(" (( dsd.product_division in (1,2) and exists(select technic_id from mst_technic mt where mt.technic_class_id in (2,4) and dsd.product_id = mt.technic_id ) ) ");
            // 20170602 GB START EDIT #4331 ���f�X�e�B���J�X�^�}�C�Y���[�@�R�~�b�V�������׏��d�l�ύX��]
            //sql.append(" or ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (10,12,14,15,16,39,46,55,57,60,64,65,66,67,68,69,70,71,72,73,74,75) and dsd.product_id = mt.item_id ) ) ");
            sql.append(" or ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (1,2,3,10,12,13,14,15,16,18,22,39,40,42,43,45,54,55,56,57,60,64,65,66,67,68,69,70,71,72,73,74,75,78) and dsd.product_id = mt.item_id ) ) ");
            // 20170602 GB END EDIT #4331 ���f�X�e�B���J�X�^�}�C�Y���[�@�R�~�b�V�������׏��d�l�ύX��]
            sql.append(" or dsd.product_division = 5)");
            sql.append(" and ds.sales_date < '" + sdf.format(bef) + "'");
            sql.append(" group by ds.customer_id) cont on cont.customer_id = detail.customer_id");
            sql.append(" group by detail.shop_id, mshop.shop_name, sales_date, slip_no, stf, customer_no, staff, customer_name1, customer_name2, rtype, introducer_no, introducer_name1, introducer_name2 ");
            sql.append(" order by sales_date, slip_no");

            SystemInfo.getLogger().info(sql.toString());
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                // �����̊���
                int n = (rs.getString("staffb") != null && !rs.getString("staffb").equals("")) ? 2 : 1;

                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("salesDate", rs.getDate("sales_date"));
                resMap.put("customerNo", rs.getString("shop_name"));
                resMap.put("customerName", rs.getString("customer_name"));
                resMap.put("price", rs.getInt("price") / n);
                resMap.put("rType", rs.getString("rtype"));
                resMap.put("introducerNo", rs.getString("introducer_no"));
                resMap.put("introducerName", rs.getString("introducer_name"));

                // �ߋ��̗��X
                if (rs.getInt("cnts") > 0) {
                    resMap.put("newP", "-");
                    resMap.put("repP", rs.getInt("price") / (2 * n));
                } else {
                    resMap.put("newP", rs.getInt("price") / (2 * n));
                    resMap.put("repP", "-");
                }
                resMap.put("staffB", rs.getString("staffb"));

                SystemInfo.getLogger().info(resMap.toString());
                resultList.add(resMap);
            }
            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        //�Ώۃf�[�^��0���̏ꍇfalse��Ԃ�
        if (resultList.size() < 1) {
            return false;
        }

        JPOIApi jx = new JPOIApi("�R�~�b�V�������׏�");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_monthly_commission.xls");
        int maxPage = (int) Math.ceil(resultList.size() / 19d);
        int p = 0;
        SimpleDateFormat dd = new SimpleDateFormat("dd");

        for (int x = maxPage; x > 1; x--) {
            jx.copySheet(0);
        }

        int a_sum_price = 0;
        int a_sum_new = 0;
        int a_sum_rep = 0;
        //�Ώۃf�[�^��20���ȏ゠��ꍇ�i�����y�[�W�o�͂���K�v������ꍇ�j
        while (p < maxPage) {
            p++;
            //�Ώۃf�[�^��S�ďo�͂���܂Ń��[�v
            jx.setTargetSheet(p - 1);
            //------------------------------
            // �w�b�_
            //------------------------------
            jx.setCellValue(1, 1, month);
            jx.setCellValue(3, 2, shop.getShopName());
            jx.setCellValue(3, 3, staff.getStaffName(0) + " " + staff.getStaffName(1));
            jx.setCellValue(14, 1, p);
            //------------------------------
            // �{�f�B�[
            //------------------------------
            int ll = 19 * (p - 1);
            int row = 8;
            int sum_price = 0;
            int sum_new = 0;
            int sum_rep = 0;
            for (int i = 0; i < 19; i++) {
                if (i + ll < resultList.size()) {
                    Map<String, Object> nowRow = resultList.get(i + ll);
                    jx.setCellValue(1, row + i, dd.format(nowRow.get("salesDate")));
                    jx.setCellValue(2, row + i, (String) nowRow.get("customerNo"));
                    jx.setCellValue(3, row + i, (String) nowRow.get("customerName"));
                    jx.setCellValue(4, row + i, (int) nowRow.get("price"));
                    jx.setCellValue(10, row + i, (String) nowRow.get("staffB"));

                    if (nowRow.get("price") != null) {
                        sum_price += (int) nowRow.get("price");
                    }
                    if (!nowRow.get("newP").equals("-")) {
                        //�V�K�̂ݕ\��
                        jx.setCellValue(8, row + i, (int) nowRow.get("newP"));
                        jx.setCellValue(9, row + i, (String) nowRow.get("repP"));
                        jx.setCellValue(5, row + i, (String) nowRow.get("rType"));
                        jx.setCellValue(6, row + i, (String) nowRow.get("introducerNo"));
                        jx.setCellValue(7, row + i, (String) nowRow.get("introducerName"));
                        sum_new += (int) nowRow.get("newP");
                    }
                    if (!nowRow.get("repP").equals("-")) {
                        jx.setCellValue(8, row + i, (String) nowRow.get("newP"));
                        jx.setCellValue(9, row + i, (int) nowRow.get("repP"));
                        sum_rep += (int) nowRow.get("repP");
                    }
                }
            }
            //------------------------------
            // ���v��
            //------------------------------
            jx.setCellValue(4, row + 19, sum_price);
            jx.setCellValue(8, row + 19, sum_new);
            jx.setCellValue(9, row + 19, sum_rep);

            a_sum_price += sum_price;
            a_sum_new += sum_new;
            a_sum_rep += sum_rep;

            if (p < maxPage) {
                jx.setCellValue(4, row + 20, "");
                jx.setCellValue(8, row + 20, "");
                jx.setCellValue(9, row + 20, "");
            } else {
                jx.setCellValue(4, row + 20, a_sum_price);
                jx.setCellValue(8, row + 20, a_sum_new);
                jx.setCellValue(9, row + 20, a_sum_rep);
            }
        }

        jx.openWorkbook();
        return true;
    }

    /**
     * �ӔC�ҕ��͕\
     *
     * @param shop
     * @param targetDate
     * @return
     */
    protected boolean printModestyAnalysisResponsibility(MstShop shop, Date targetDate, ModestyReportPanel panel) {

        //�o�͑Ώۃf�[�^������܂���B
        this.parentPanel = panel;

        String cust1 = "";
        String cust2 = "";
        String JCarendar = "����";
        int toDayAmortization = 0;
        String responsiblePerson = "";
        int tomorrowReservationNum = 0;

        Locale locale = new Locale("ja", "JP", "JP");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        //�a��
         //SimpleDateFormat sdft = new SimpleDateFormat(JCarendar+"�@y�N�@MM���@dd��", locale);

         //����ɂ���Ȃ�B����1 �@���ƂP�ӏ��A311�s�ڂ�����
         SimpleDateFormat sdftSei = new SimpleDateFormat("yyyy�N�@MM���@dd��");


        String shopList = shop.getShopID() + "";
        String shopName = shop.getShopName();

        //���P�ʗp
        Calendar tm = Calendar.getInstance();

        //�{���p
        Calendar td = Calendar.getInstance(locale);

        //�ǉ��d�l_�w�b�_���� �j���܂ŏo�͗p
        Calendar jtd = Calendar.getInstance();

        //���ݓ��t�ł͂Ȃ��A�v���_�E���őI���������t�ŏ������{
        tm.setTime(targetDate);
        td.setTime(targetDate);

        jtd.setTime(targetDate);

        //String nowDay = sdft.format(jtd.getTime());
        //����ɂ���Ȃ�B����2
        String nowDay = sdftSei.format(jtd.getTime());


        String[] week = new String[7];
        week[0] = "��";
        week[1] = "��";
        week[2] = "��";
        week[3] = "��";
        week[4] = "��";
        week[5] = "��";
        week[6] = "�y";
        int week_now = jtd.get(jtd.DAY_OF_WEEK);
        nowDay = nowDay.concat("(�@" + week[week_now - 1] + "�@)");

        // ����
        tm.set(Calendar.DATE, 1);
        Date bef = tm.getTime();

        // ����
        tm.set(Calendar.DATE, tm.getActualMaximum(Calendar.DATE));
        Date aft = tm.getTime();

        //����
        td.add(Calendar.DATE, 1);
        Date tr = td.getTime();

        //�����
        td.add(Calendar.DATE, 1);
        Date ntr = td.getTime();

        // ���O�o��
        SystemInfo.getLogger().info(nowDay);
SystemInfo.getLogger().info(sdftSei.format(jtd.getTime()));


        //------------------------------
        // �f�[�^�擾
        //------------------------------
        ResultSetWrapper rsSold;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>(0);
        //�{�����сQ�󒍐V�K�A�󒍓���
        List<Map<String, Object>> toDayNewOrdersList = new ArrayList<Map<String, Object>>(0);
        //�{�����сQ���㕨�́A����T����
        List<Map<String, Object>> toDayPerformanceItemSaronList = new ArrayList<Map<String, Object>>(0);
        //���㕨��
        List<Map<String, Object>> itemSoldList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> itemSoldListM = new ArrayList<Map<String, Object>>(0);
        //����T����
        List<Map<String, Object>> saronSoldList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> saronSoldListM = new ArrayList<Map<String, Object>>(0);
        //����T����_���i
        List<Map<String, Object>> saronItemSoldList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> saronItemSoldListM = new ArrayList<Map<String, Object>>(0);
        //����T����_���i�i�T�������ϕi�j
        List<Map<String, Object>> saronItemDiscountSoldList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> saronItemDiscountSoldListM = new ArrayList<Map<String, Object>>(0);
        //�𖱔���
        List<Map<String, Object>> corseSoldList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> corseSoldListM = new ArrayList<Map<String, Object>>(0);
        //�������сQ�󒍐V�K�A�󒍓����i�S���ҕʁj
        List<Map<String, Object>> newOrdersListM = new ArrayList<Map<String, Object>>(0);

        //�������сQ�󒍐V�K�A�󒍓����i�S���ҕ�_���̂݁j
        List<Map<String, Object>> newOrdersListMonthProporsal = new ArrayList<Map<String, Object>>(0);


        //�������сQ���㕨�́A����T�����i�S���ҕʁj
        List<Map<String, Object>> performanceItemSaronListM = new ArrayList<Map<String, Object>>(0);

        //�������сQ���㕨�́A����T�����i�S���ҕʁQ���̂݁j
        List<Map<String, Object>> performanceItemSaronListMonthProporsal = new ArrayList<Map<String, Object>>(0);


        //�V�K�A��������f�[�^�̎擾
        Map<String, Map<String, Object>> checkMap = new HashMap<String, Map<String, Object>>(0);
        //��������ꗗ�i���X�܂̐l�̔���j
        Map<String, Map<String, Object>> monthSalesInternal = new HashMap<String, Map<String, Object>>(0);
        //��������ꗗ�i���X�܂̐l�̔���j
        Map<String, Map<String, Object>> monthSalesOutside = new HashMap<String, Map<String, Object>>(0);

        //�X�܃X�^�b�t�̃��X�g
        List<Map<String, Object>> staffSortedList = new ArrayList<Map<String, Object>>();

        //�t���[�i�Œ�j����
        List<Map<String, Object>> saronSoldFreeList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> saronSoldFreeListM = new ArrayList<Map<String, Object>>(0);





        try {
            StringBuilder sql = new StringBuilder(4000);
            ResultSetWrapper rs;

            //---------------------------------------------------------------------------------
            // �V�K�A��������f�[�^�̎擾
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            //sql.append(" select distinct ds.customer_id , first_date , DATE_TRUNC('month', first_date + '1 months') + '-1 days' as end_of_month ");
            sql.append(" select distinct ds.customer_id ");

            ////2018/11/27 �V�K���̔���ύX (first_date �ƁAend_of_month)
            sql.append(" ,case 1 =1   when first_date is NOT NULL THEN first_date  when first_date is NULL THEN current_date  end as first_date ,case 1 =1 when first_date is NOT NULL THEN    DATE_TRUNC('month', first_date + '1 months') + '-1 days'   when first_date is NULL THEN current_date  end as end_of_month");

            sql.append("     , ddd.product_division , ddd.product_id , ddd.slip_cnt from data_sales ds ");
            sql.append(" left outer join ( select ds.customer_id , ds.shop_id , dsd.product_division , dsd.product_id , count(ds.slip_no) as slip_cnt ");
            sql.append(" from data_sales ds inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no ");
            sql.append("  where ds.delete_date is null and dsd.product_division in (1, 2, 3, 4, 5, 7, 8)  ");
            sql.append("  and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            sql.append(" group by ds.customer_id , ds.shop_id , dsd.product_division , dsd.product_id ) ddd  on ds.customer_id = ddd.customer_id  ");
           sql.append(" left outer join ( select ( select min(sales_date) from data_sales where customer_id = ds.customer_id  ");

            //2018/11/27 �V�K���̔���ύX
            //�Z�p�́A1:�̌��ƁA19:���̑� �́A�V�K�����肩��O��(�@first_date�̒�`�� �ύX )
             //sql.append(" left outer join ( select ( select min(sales_date) from data_sales where customer_id = ds.customer_id ) as first_date , ds.customer_id ");
             sql.append(" and  slip_no NOT IN (     select      slip_no     from      data_sales_detail     where	  product_division = 1 and       product_id  IN (         select          technic_id         from          mst_technic         where          technic_class_id  IN (1, 19)	)  )) ");

             sql.append(" as first_date , ds.customer_id ");


            sql.append(" from data_sales ds where ds.delete_date is null ) dayt on ds.customer_id = dayt.customer_id where ");
            sql.append(" ds.shop_id =").append(shopList);
            sql.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            sql.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            sql.append(" order by ds.customer_id , ddd.product_division , ddd.product_id , first_date ");
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                String customerId = rs.getString("customer_id");
                Map<String, Object> customerMap = null;
                if (checkMap.containsKey(customerId)) {
                    customerMap = checkMap.get(customerId);
                } else {
                    customerMap = new HashMap<String, Object>(0);
                    customerMap.put("first_date", rs.getDate("first_date"));
                    customerMap.put("end_of_month", rs.getDate("end_of_month")); //���񗈓X���̌���
                }
                String key = rs.getString("product_division");
                customerMap.put(key, rs.getInt("slip_cnt"));
                customerMap.put("product_division", rs.getInt("product_division"));
             //   SystemInfo.getLogger().info(customerMap.toString());
                checkMap.put(customerId, customerMap);
            }

            //---------------------------------------------------------------------------------
            // �\�������x��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            //�𖱃��x���ꗗ
            sql.append("select                                                              ");
            //�\����
            sql.append(" display_seq                                                        ");
            //����
            sql.append(" , course_class_name                                                  ");
            sql.append("from mst_course_class                                                 ");
            sql.append("where                                                               ");
            //�K�v�ȏ��i����ID
            sql.append(" display_seq in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20) ");
            sql.append(" and delete_date IS NULL                                                 ");
            sql.append(" order by display_seq                                            ");
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();;
                resultMap.put("display_seq", rs.getInt("display_seq"));
                resultMap.put("class_name", rs.getString("course_class_name"));
            //    SystemInfo.getLogger().info(resultMap.toString());
                labelList.add(resultMap);
            }

            sql = new StringBuilder(4000);
            //���̃��x���ꗗ
            sql.append("select                                                              ");
            //�\����
            sql.append(" display_seq                                                        ");
            //����
            sql.append(" , item_class_name                                                  ");
            sql.append("from mst_item_class                                                 ");
            sql.append("where                                                               ");
            //�K�v�ȏ��i����ID

            //2018/11/27 1�A�C�e���폜 (�\����44�͕s�v)
            //sql.append(" display_seq in (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44) ");
            sql.append(" display_seq in (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43) ");

            sql.append(" and delete_date IS NULL                                                 ");
            sql.append(" order by display_seq                                            ");
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();;
                resultMap.put("display_seq", rs.getInt("display_seq"));
                resultMap.put("class_name", rs.getString("item_class_name"));
//                SystemInfo.getLogger().info(resultMap.toString());
                labelList.add(resultMap);
            }

            //---------------------------------------------------------------------------------
            // �ӔC�� �ƁA�����X�^�b�t
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select");
            sql.append("  ms.staff_no");
            sql.append(" ,ms.staff_name1  ");
            sql.append(" ,ms.staff_name2  ");
            sql.append(" ,msc.staff_class_name  ");
            sql.append(" from   ");
            sql.append(" mst_staff ms ");
            sql.append(" left join mst_staff_class msc                              ");
            sql.append("   on ms.staff_class_id = msc.staff_class_id                ");

            sql.append(" where  ");
            sql.append("  ms.shop_id =").append(shopList);

            //2018/11/1 �폜�X�^�b�t�͑ΏۊO
            sql.append(" and ms.delete_date is null ");

            //2018/11/1 �\�����̏���
            sql.append(" order by ms.display_seq ASC ;  ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());

            int staffMaxCounter = 0;
            while (rs.next()) {
                //�w�b�_ �ӔC�җ� �����X�^�b�t�A�\�����F�P�̐l
                if (staffMaxCounter == 0) {
                    responsiblePerson = rs.getString("staff_name1") + " " + rs.getString("staff_name2");
                }

                //2018/11/1 FIX �l���ʂɔ��オ�Ȃ��X�^�b�t���o�́i���A�X�^�b�t���ɁA/������ꍇ�͏��O�j
                if (rs.getString("staff_name1") != null && rs.getString("staff_name1").contains("/") == false && rs.getString("staff_name1").equals("�T����") == false) {

                    Map<String, Object> staffMap = new HashMap<String, Object>();
                    staffMap.put("staff_no", rs.getString("staff_no"));
                    staffMap.put("staff_name1", rs.getString("staff_name1"));
                    staffMap.put("staff_name2", rs.getString("staff_name2"));
                    staffMap.put("staff_class_name", rs.getString("staff_class_name"));

                    //���X�g�ɕt�^
                    staffSortedList.add(staffMap);

                    staffMaxCounter = staffMaxCounter + 1;
                }
            }
            rs.close();

            //8���ȏ�̏ꍇ�A�G���[�Ƃ��A�㑱���������{���Ȃ��B�i���[���o���Ȃ��B�j
            if (staffMaxCounter > 8) {
                MessageDialog.showMessageDialog(parentPanel,
                        "���[�o�͉\�ȃX�^�b�t���𒴉߂��Ă��܂��B",
                        parentPanel.getTitle(),
                        JOptionPane.ERROR_MESSAGE);

                //������return ���A�R�����g�A�E�g���邾���ŁA(���b�Z�[�W�͏o�����A���[���o��)�ɁB
                return true;

            }

            //---------------------------------------------------------------------------------
            // ����Q�{������
            //---------------------------------------------------------------------------------
            //�{�����сQ���㕨��
            StringBuilder toDayNewOrders = new StringBuilder(4000);
            //�{�����сQ���㕨��
            StringBuilder toDayPerformanceItem = new StringBuilder(4000);
            //�{�����сQ����T����
            StringBuilder toDayPerformanceSaron = new StringBuilder(4000);

            //�����󒍃f�[�^�擾
            toDayNewOrders.append("select                                                       ");
            //���i����ID
            toDayNewOrders.append(" mc.course_class_id ");
            //������z

            //toDayNewOrders.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08) AS price ");
            //������z�i����őΉ��j
            toDayNewOrders.append(" , case dsd.product_division when 8 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 5 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            toDayNewOrders.append(" , mcc.course_class_name ");
            //�����z
            toDayNewOrders.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            toDayNewOrders.append(" , dsd.product_division ");
            //�X�^�b�t��
            toDayNewOrders.append(" , ms.staff_name1 ");
            //�ڋq�h�c
            toDayNewOrders.append(" , ds.customer_id ");
            //�̔���
            toDayNewOrders.append(" , ds.sales_date ");
            toDayNewOrders.append("from                                                         ");
            toDayNewOrders.append(" data_sales_detail as dsd                                    ");
            toDayNewOrders.append(" inner join data_sales as ds                                 ");
            toDayNewOrders.append("   on dsd.shop_id = ds.shop_id                               ");
            toDayNewOrders.append("   and dsd.slip_no = ds.slip_no                              ");
            toDayNewOrders.append(" inner join mst_course as mc                                 ");
            toDayNewOrders.append("   on dsd.product_id = mc.course_id                          ");
            toDayNewOrders.append(" inner join mst_course_class mcc                             ");
            toDayNewOrders.append("   on mc.course_class_id = mcc.course_class_id               ");
            toDayNewOrders.append(" left join mst_staff ms                                     ");
            toDayNewOrders.append("   on dsd.staff_id = ms.staff_id                             ");
            toDayNewOrders.append("where                                                        ");
            toDayNewOrders.append(" dsd.product_division in (5,8)                                  ");
            toDayNewOrders.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            toDayNewOrders.append(" and ds.shop_id = ").append(shopList);
            toDayNewOrders.append(" and dsd.delete_date IS NULL                                 ");
            toDayNewOrders.append(" and ds.delete_date IS NULL                                  ");
            toDayNewOrders.append(" and mc.delete_date IS NULL                                  ");
            toDayNewOrders.append(" and mcc.delete_date IS NULL                                 ");

            //�폜�X�^�b�t�ł��A���׏�͌v��
            //toDayNewOrders.append(" and ms.delete_date IS NULL                                 ");
            toDayNewOrders.append("group by ");
            toDayNewOrders.append("  mc.course_class_id ");
            toDayNewOrders.append("  , mcc.course_class_name ");
            toDayNewOrders.append("  , dsd.product_division ");
            toDayNewOrders.append("  , dsd.discount_value ");
            toDayNewOrders.append("  , ms.staff_name1 ");
            toDayNewOrders.append("  , ds.customer_id ");
            toDayNewOrders.append("  , ds.sales_date , dsd.tax_rate ");
            toDayNewOrders.append("order by                                                     ");
            toDayNewOrders.append(" mc.course_class_id                                           ");
            toDayNewOrders.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(toDayNewOrders.toString());
            rsSold = SystemInfo.getConnection().executeQuery(toDayNewOrders.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("course_class_id"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
              //  SystemInfo.getLogger().info(resMap.toString());
                toDayNewOrdersList.add(resMap);
            }
            rsSold.close();

            //�������㕨�̃f�[�^�擾
            toDayPerformanceItem.append("select                                                       ");
            //���i����ID
            toDayPerformanceItem.append(" mi.item_class_id ");
            //������z
            //toDayPerformanceItem.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");

            //������z(����őΉ�)
            toDayPerformanceItem.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price ");

            //���i���ޖ�
            toDayPerformanceItem.append(" , mic.item_class_name ");
            //�����z
            toDayPerformanceItem.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            toDayPerformanceItem.append(" , dsd.product_division ");
            //�X�^�b�t��
            toDayPerformanceItem.append(" , ms.staff_name1 ");
            //�ڋq�h�c
            toDayPerformanceItem.append(" , ds.customer_id ");
            toDayPerformanceItem.append("from                                                         ");
            toDayPerformanceItem.append(" data_sales_detail as dsd                                    ");
            toDayPerformanceItem.append(" inner join data_sales as ds                                 ");
            toDayPerformanceItem.append("   on dsd.shop_id = ds.shop_id                               ");
            toDayPerformanceItem.append("   and dsd.slip_no = ds.slip_no                              ");
            toDayPerformanceItem.append(" inner join mst_item as mi                                   ");
            toDayPerformanceItem.append("   on dsd.product_id = mi.item_id                            ");
            toDayPerformanceItem.append(" inner join mst_item_class mic                               ");
            toDayPerformanceItem.append("   on mi.item_class_id = mic.item_class_id                   ");
            toDayPerformanceItem.append(" left join mst_staff ms                                     ");
            toDayPerformanceItem.append("   on ds.staff_id = ms.staff_id                             ");
            toDayPerformanceItem.append("where                                                        ");
            toDayPerformanceItem.append(" dsd.product_division in (2,4)                                  ");
            toDayPerformanceItem.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            toDayPerformanceItem.append(" and ds.shop_id = ").append(shopList);
            toDayPerformanceItem.append(" and dsd.delete_date IS NULL                                 ");
            toDayPerformanceItem.append(" and ds.delete_date IS NULL                                  ");
            toDayPerformanceItem.append(" and mi.delete_date IS NULL                                  ");
            toDayPerformanceItem.append(" and mic.delete_date IS NULL                                 ");
            //�l�����Ȃ����i�̂�
            toDayPerformanceItem.append(" and dsd.discount_value = 0 ");
          // toDayPerformanceItem.append(" and ms.staff_name1<>'�T����' ");

            //�폜�X�^�b�t�ł��A���׏�͌v��
            //toDayPerformanceItem.append(" and ms.delete_date IS NULL                                 ");
            toDayPerformanceItem.append("group by");
            toDayPerformanceItem.append("  mi.item_class_id ");
            toDayPerformanceItem.append("  , mic.item_class_name ");
            toDayPerformanceItem.append("  , dsd.product_division ");
            toDayPerformanceItem.append("  , dsd.discount_value ");
            toDayPerformanceItem.append("  , ms.staff_name1 ");
            toDayPerformanceItem.append("  , ds.customer_id , ds.sales_date, dsd.tax_rate ");
            toDayPerformanceItem.append("order by                                                     ");
            toDayPerformanceItem.append(" mi.item_class_id                                           ");
            toDayPerformanceItem.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(toDayPerformanceItem.toString());
            rsSold = SystemInfo.getConnection().executeQuery(toDayPerformanceItem.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("item_class_id"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
            //    SystemInfo.getLogger().info(resMap.toString());
                toDayPerformanceItemSaronList.add(resMap);
            }
            rsSold.close();

            //��������T�����f�[�^�擾
            toDayPerformanceSaron.append("select                                                       ");
            //���i����ID
            toDayPerformanceSaron.append(" mt.technic_class_id ");
            //������z
            //toDayPerformanceSaron.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price  ");

            //������z(�����)
            toDayPerformanceSaron.append(" , case dsd.product_division when 3 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");
            //���i���ޖ�
            toDayPerformanceSaron.append(" , mtc.technic_class_name ");
            //�����z
            toDayPerformanceSaron.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            toDayPerformanceSaron.append(" , dsd.product_division ");
            //�X�^�b�t��
            toDayPerformanceSaron.append(" , ms.staff_name1 ");
            //�ڋq�h�c
            toDayPerformanceSaron.append(" , ds.customer_id ");
            toDayPerformanceSaron.append("from                                                         ");
            toDayPerformanceSaron.append(" data_sales_detail as dsd                                    ");
            toDayPerformanceSaron.append(" inner join data_sales as ds                                 ");
            toDayPerformanceSaron.append("   on dsd.shop_id = ds.shop_id                               ");
            toDayPerformanceSaron.append("   and dsd.slip_no = ds.slip_no                              ");
            toDayPerformanceSaron.append(" inner join mst_technic as mt                                   ");
            toDayPerformanceSaron.append("   on dsd.product_id = mt.technic_id                            ");
            toDayPerformanceSaron.append(" inner join mst_technic_class mtc                               ");
            toDayPerformanceSaron.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            toDayPerformanceSaron.append(" left join mst_staff ms                                     ");
            toDayPerformanceSaron.append("   on ds.staff_id = ms.staff_id                             ");
            toDayPerformanceSaron.append("where                                                        ");
            toDayPerformanceSaron.append(" dsd.product_division in (1,3)                                   ");
            toDayPerformanceSaron.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            toDayPerformanceSaron.append(" and ds.shop_id = ").append(shopList);
            toDayPerformanceSaron.append(" and dsd.delete_date IS NULL                                 ");
            toDayPerformanceSaron.append(" and ds.delete_date IS NULL                                  ");
            toDayPerformanceSaron.append(" and mt.delete_date IS NULL                                  ");
            toDayPerformanceSaron.append(" and mtc.delete_date IS NULL                                 ");


            //�폜�X�^�b�t�ł��A���׏�͌v��
            //toDayPerformanceSaron.append(" and ms.delete_date IS NULL                                 ");
            toDayPerformanceSaron.append("group by                                                     ");
            toDayPerformanceSaron.append("  mt.technic_class_id ");
            toDayPerformanceSaron.append("  , mtc.technic_class_name ");
            toDayPerformanceSaron.append("  , dsd.product_division ");
            toDayPerformanceSaron.append("  , dsd.discount_value ");
            toDayPerformanceSaron.append("  , ms.staff_name1 ");
            toDayPerformanceSaron.append("  , ds.customer_id , ds.sales_date, dsd.tax_rate ");
            toDayPerformanceSaron.append("order by                                                     ");
            toDayPerformanceSaron.append("  mt.technic_class_id                                        ");
            toDayPerformanceSaron.append(" , product_division;                                         ");

            SystemInfo.getLogger().info(toDayPerformanceSaron.toString());
            rsSold = SystemInfo.getConnection().executeQuery(toDayPerformanceSaron.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("technic_class_id"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
            //    SystemInfo.getLogger().info(resMap.toString());
                toDayPerformanceItemSaronList.add(resMap);
            }
            rsSold.close();

            //---------------------------------------------------------------------------------
            // �����ғ����p�l���i�{�����p�l���j
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select  ");
            sql.append(" count(sum.customer_id) as cnt  ");
            sql.append(" from (select  ");
            sql.append(" ds.customer_id, count(dcd.slip_no) as dcnt  ");
            sql.append(" from data_contract_digestion dcd  ");
            sql.append(" inner join data_sales_detail dsd  ");
            sql.append("  on  dcd.shop_id = dsd.shop_id  ");
            sql.append("  and dcd.slip_no = dsd.slip_no  ");
            sql.append("  and dcd.contract_no = dsd.contract_no  ");
            sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("  and dsd.product_division = 6 and dsd.delete_date is null ");
            sql.append(" inner join data_sales ds  ");
            sql.append("  on ds.shop_id = dsd.shop_id  ");
            sql.append("  and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
            sql.append(" inner join mst_course mc ");
            sql.append("  on dsd.product_id = mc.course_id  ");
            //sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
            sql.append(" where  ");
            sql.append("  dcd.product_num > 0  ");
            sql.append(" and ds.shop_id =").append(shopList);
            sql.append(" and sales_date = '").append(sdf.format(targetDate)).append("'");
            sql.append(" group by ds.customer_id) sum  ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                toDayAmortization = rs.getInt("cnt");
            }
            rs.close();

            //---------------------------------------------------------------------------------
            // �����\��l��
            //---------------------------------------------------------------------------------
            StringBuilder tomorrowReservation = new StringBuilder(4000);
            tomorrowReservation.append("select ");
            tomorrowReservation.append(" count(reservation_no) as trnum");
            tomorrowReservation.append(" from ");
            tomorrowReservation.append(" ( select ");
            tomorrowReservation.append("     reservation_no");
            tomorrowReservation.append("   from ");
            tomorrowReservation.append("     data_reservation_detail ");
            tomorrowReservation.append("   where ");
            tomorrowReservation.append("     reservation_datetime >= '").append(sdf.format(tr)).append("'");
            tomorrowReservation.append("     and reservation_datetime < '").append(sdf.format(ntr)).append("'");
            tomorrowReservation.append("     and shop_id = ").append(shopList);
            tomorrowReservation.append("     and delete_date is NULL ");
            tomorrowReservation.append("     group by reservation_no ");
            tomorrowReservation.append(" ) AS trt ");

            SystemInfo.getLogger().info(tomorrowReservation.toString());
            rs = SystemInfo.getConnection().executeQuery(tomorrowReservation.toString());
            while (rs.next()) {
                // �����̗\��l��
                tomorrowReservationNum = rs.getInt("trnum");
            }
            rs.close();

            //---------------------------------------------------------------------------------
            // ����T����
            //---------------------------------------------------------------------------------
            //�{������_�Z�p�}�X�^SQL
            StringBuilder saronDateSold = new StringBuilder(4000);
            //�����݌v_�Z�p�}�X�^SQL
            StringBuilder saronDateMonthSold = new StringBuilder(4000);

            //�{������_�Z�p�}�X�^SQL�t���[�p
            StringBuilder saronDateSoldFree = new StringBuilder(4000);
            //�����݌v_�Z�p�}�X�^SQL�@�t���[�p
            StringBuilder saronDateMonthSoldFree = new StringBuilder(4000);

            //�{������_���i�}�X�^SQL
            StringBuilder saronItemDateSold = new StringBuilder(4000);
            //�����݌v_���i�}�X�^SQL
            StringBuilder saronItemDateMonthSold = new StringBuilder(4000);
            //�{������_���i�}�X�^SQL�i�T�������ϕi�j
            StringBuilder saronItemDiscountDateSold = new StringBuilder(4000);
            //�����݌v_���i�}�X�^SQL�i�T�������ϕi�j
            StringBuilder saronItemDiscountDateMonthSold = new StringBuilder(4000);

            //�@����T�����f�[�^�擾
            saronDateSold.append("select                                                       ");
            saronDateSold.append("selectResult.technic_class_id ");
            saronDateSold.append(",SUM(selectResult.product_num) AS product_num");
            saronDateSold.append(",SUM(selectResult.price) as price");
            saronDateSold.append("  , selectResult.technic_class_name");
            saronDateSold.append("  , selectResult.product_division ");
            saronDateSold.append("from (select ");


            //���i����ID
            saronDateSold.append(" mt.technic_class_id ");
            //����
            saronDateSold.append(" , dsd.product_num AS product_num ");
            //������z
            //saronDateSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price  ");

             //������z(�����)
            saronDateSold.append(" , case dsd.product_division when 3 then  ceil( abs(    (   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     (  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            saronDateSold.append(" , mtc.technic_class_name ");
            //�Z�p���i�敪�h�c
            saronDateSold.append(" , dsd.product_division ");
            saronDateSold.append("from                                                         ");
            saronDateSold.append(" data_sales_detail as dsd                                    ");
            saronDateSold.append(" inner join data_sales as ds                                 ");
            saronDateSold.append("   on dsd.shop_id = ds.shop_id                               ");
            saronDateSold.append("   and dsd.slip_no = ds.slip_no                              ");
            saronDateSold.append(" inner join mst_technic as mt                                   ");
            saronDateSold.append("   on dsd.product_id = mt.technic_id                            ");
            saronDateSold.append(" inner join mst_technic_class mtc                               ");
            saronDateSold.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateSold.append(" left join mst_staff msf                                ");
            saronDateSold.append("   on msf.staff_id = ds.staff_id                   ");



            saronDateSold.append("where                                                        ");
            saronDateSold.append(" dsd.product_division in (1,3)                                   ");
            saronDateSold.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");

            //�S���҂��T�����̏ꍇ�̂� �ǉ�

            //�o�͂��鏤�i����ID
            //�̌��@ID=1�A�C���I�[���@ID=15�A������@ID=3�A�t���[����@ID=2
            //�t���[��ʂŎ擾

            saronDateSold.append(" and mt.technic_class_id in (1,15,3) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronDateSold.append(" and ds.shop_id = ").append(shopList);
            saronDateSold.append(" and dsd.delete_date IS NULL                                 ");
            saronDateSold.append(" and ds.delete_date IS NULL                                  ");
            saronDateSold.append(" and mt.delete_date IS NULL                                  ");
            saronDateSold.append(" and mtc.delete_date IS NULL                                 ");

            saronDateSold.append(" )as selectResult ");


            saronDateSold.append("group by                                                     ");
            saronDateSold.append("  selectResult.technic_class_id ");
            saronDateSold.append("  , selectResult.technic_class_name ");
            saronDateSold.append("  , selectResult.product_division ");

            //�t���[�����A�ʂŎ擾����union
         saronDateSold.append("union                                                       ");
            saronDateSold.append("select                                                       ");
            saronDateSold.append("selectResult2.technic_class_id ");
            saronDateSold.append(",SUM(selectResult2.product_num) AS product_num");
            saronDateSold.append(",SUM(selectResult2.price) as price");
            saronDateSold.append("  , selectResult2.technic_class_name");
            saronDateSold.append("  , selectResult2.product_division ");
            saronDateSold.append("from (select ");


            //���i����ID
            saronDateSold.append(" mt.technic_class_id ");
            //����
            saronDateSold.append(" , dsd.product_num AS product_num ");
            //������z
            //saronDateSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price  ");

             //������z(�����)
            saronDateSold.append(" , case dsd.product_division when 3 then  ceil( abs(    (   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     (  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            saronDateSold.append(" , mtc.technic_class_name ");
            //�Z�p���i�敪�h�c
            saronDateSold.append(" , dsd.product_division ");
            saronDateSold.append("from                                                         ");
            saronDateSold.append(" data_sales_detail as dsd                                    ");
            saronDateSold.append(" inner join data_sales as ds                                 ");
            saronDateSold.append("   on dsd.shop_id = ds.shop_id                               ");
            saronDateSold.append("   and dsd.slip_no = ds.slip_no                              ");
            saronDateSold.append(" inner join mst_technic as mt                                   ");
            saronDateSold.append("   on dsd.product_id = mt.technic_id                            ");
            saronDateSold.append(" inner join mst_technic_class mtc                               ");
            saronDateSold.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateSold.append(" left join mst_staff msf                                ");
            saronDateSold.append("   on msf.staff_id = ds.staff_id                   ");



            saronDateSold.append("where                                                        ");
            saronDateSold.append(" dsd.product_division in (1,3)                                   ");
            saronDateSold.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");

            //�S���҂��T�����̏ꍇ�̂� �ǉ�

            //�o�͂��鏤�i����ID
            //�̌��@ID=1�A�C���I�[���@ID=15�A������@ID=3�A�t���[����@ID=2
            //�t���[��ʂŎ擾

            saronDateSold.append(" and mt.technic_class_id in (2) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronDateSold.append(" and ds.shop_id = ").append(shopList);
            saronDateSold.append(" and dsd.delete_date IS NULL                                 ");
            saronDateSold.append(" and ds.delete_date IS NULL                                  ");
            saronDateSold.append(" and mt.delete_date IS NULL                                  ");
            saronDateSold.append(" and mtc.delete_date IS NULL                                 ");

            //�t���[�́A�T�����̂�
            saronDateSold.append(" and msf.staff_name1='�T����'                                 ");

            saronDateSold.append(" )as selectResult2 ");


            saronDateSold.append("group by                                                     ");
            saronDateSold.append("  selectResult2.technic_class_id ");
            saronDateSold.append("  , selectResult2.technic_class_name ");
            saronDateSold.append("  , selectResult2.product_division ");


            saronDateSold.append("order by                                                     ");
            saronDateSold.append("  technic_class_id                                        ");
            saronDateSold.append(" , product_division;                                         ");

            SystemInfo.getLogger().info(saronDateSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronDateSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("technic_class_id", rsSold.getInt("technic_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
            //    SystemInfo.getLogger().info(resMap.toString());
                saronSoldList.add(resMap);
            }
            rsSold.close();

            //�A��������T�����f�[�^�擾
            saronDateMonthSold.append("select                                                       ");


            saronDateMonthSold.append("selectResult.technic_class_id ");
            saronDateMonthSold.append(",SUM(selectResult.product_num) AS product_num");
            saronDateMonthSold.append(",SUM(selectResult.price) as price");
            saronDateMonthSold.append("  , selectResult.technic_class_name");
            saronDateMonthSold.append("  , selectResult.product_division ");
            saronDateMonthSold.append("from (select ");

            //���i����ID
            saronDateMonthSold.append(" mt.technic_class_id ");
            //����
            saronDateMonthSold.append(" , dsd.product_num AS product_num ");
            //������z
            //saronDateMonthSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08) AS price  ");

            //������z(�����)
            saronDateMonthSold.append(" , case dsd.product_division when 3 then  ceil( abs(    (   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     (  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");


            //���i���ޖ�
            saronDateMonthSold.append(" , mtc.technic_class_name ");
            //�Z�p���i�敪�h�c
            saronDateMonthSold.append(" , dsd.product_division ");
            saronDateMonthSold.append("from                                                         ");
            saronDateMonthSold.append(" data_sales_detail as dsd                                    ");
            saronDateMonthSold.append(" inner join data_sales as ds                                 ");
            saronDateMonthSold.append("   on dsd.shop_id = ds.shop_id                               ");
            saronDateMonthSold.append("   and dsd.slip_no = ds.slip_no                              ");
            saronDateMonthSold.append(" inner join mst_technic as mt                                   ");
            saronDateMonthSold.append("   on dsd.product_id = mt.technic_id                            ");
            saronDateMonthSold.append(" inner join mst_technic_class mtc                               ");
            saronDateMonthSold.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateMonthSold.append(" left join mst_staff msf                                ");
            saronDateMonthSold.append("   on msf.staff_id = ds.staff_id                   ");

            saronDateMonthSold.append("where                                                        ");
            saronDateMonthSold.append(" dsd.product_division in (1,3)                                   ");
            //��������
            saronDateMonthSold.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            saronDateMonthSold.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            //�̌��@ID=1�A�C���I�[���@ID=15�A������@ID=3�A�t���[����@ID=2
            //�t���[�͕ʂŎ擾
            saronDateMonthSold.append(" and mt.technic_class_id in (1,15,3) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronDateMonthSold.append(" and ds.shop_id = ").append(shopList);
            saronDateMonthSold.append(" and dsd.delete_date IS NULL                                 ");
            saronDateMonthSold.append(" and ds.delete_date IS NULL                                  ");
            saronDateMonthSold.append(" and mt.delete_date IS NULL                                  ");
            saronDateMonthSold.append(" and mtc.delete_date IS NULL                                 ");

            saronDateMonthSold.append(" )as selectResult ");
            saronDateMonthSold.append("group by");
            saronDateMonthSold.append("  selectResult.technic_class_id ");
            saronDateMonthSold.append("  , selectResult.technic_class_name ");
            saronDateMonthSold.append("  , selectResult.product_division ");


            //�t���[�����ʂ�UNION
             saronDateMonthSold.append("union                                                       ");
            saronDateMonthSold.append("select                                                       ");


            saronDateMonthSold.append("selectResult2.technic_class_id ");
            saronDateMonthSold.append(",SUM(selectResult2.product_num) AS product_num");
            saronDateMonthSold.append(",SUM(selectResult2.price) as price");
            saronDateMonthSold.append("  , selectResult2.technic_class_name");
            saronDateMonthSold.append("  , selectResult2.product_division ");
            saronDateMonthSold.append("from (select ");

            //���i����ID
            saronDateMonthSold.append(" mt.technic_class_id ");
            //����
            saronDateMonthSold.append(" , dsd.product_num AS product_num ");
            //������z
            //saronDateMonthSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08) AS price  ");

            //������z(�����)
            saronDateMonthSold.append(" , case dsd.product_division when 3 then  ceil( abs(    (   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date)))  ))*-1   when 1 then   ceil(     (  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");


            //���i���ޖ�
            saronDateMonthSold.append(" , mtc.technic_class_name ");
            //�Z�p���i�敪�h�c
            saronDateMonthSold.append(" , dsd.product_division ");
            saronDateMonthSold.append("from                                                         ");
            saronDateMonthSold.append(" data_sales_detail as dsd                                    ");
            saronDateMonthSold.append(" inner join data_sales as ds                                 ");
            saronDateMonthSold.append("   on dsd.shop_id = ds.shop_id                               ");
            saronDateMonthSold.append("   and dsd.slip_no = ds.slip_no                              ");
            saronDateMonthSold.append(" inner join mst_technic as mt                                   ");
            saronDateMonthSold.append("   on dsd.product_id = mt.technic_id                            ");
            saronDateMonthSold.append(" inner join mst_technic_class mtc                               ");
            saronDateMonthSold.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateMonthSold.append(" left join mst_staff msf                                ");
            saronDateMonthSold.append("   on msf.staff_id = ds.staff_id                   ");

            saronDateMonthSold.append("where                                                        ");
            saronDateMonthSold.append(" dsd.product_division in (1,3)                                   ");
            //��������
            saronDateMonthSold.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            saronDateMonthSold.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            //�̌��@ID=1�A�C���I�[���@ID=15�A������@ID=3�A�t���[����@ID=2
            //�t���[�͕ʂŎ擾
            saronDateMonthSold.append(" and mt.technic_class_id in (2) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronDateMonthSold.append(" and ds.shop_id = ").append(shopList);
            saronDateMonthSold.append(" and dsd.delete_date IS NULL                                 ");
            saronDateMonthSold.append(" and ds.delete_date IS NULL                                  ");
            saronDateMonthSold.append(" and mt.delete_date IS NULL                                  ");
            saronDateMonthSold.append(" and mtc.delete_date IS NULL                                 ");
            saronDateMonthSold.append(" and msf.staff_name1 = '�T����'  ");

            saronDateMonthSold.append(" )as selectResult2 ");
            saronDateMonthSold.append("group by");
            saronDateMonthSold.append("  selectResult2.technic_class_id ");
            saronDateMonthSold.append("  , selectResult2.technic_class_name ");
            saronDateMonthSold.append("  , selectResult2.product_division ");
            saronDateMonthSold.append("order by                                                     ");
            saronDateMonthSold.append(" technic_class_id                                           ");
            saronDateMonthSold.append(" , product_division;                                        ");

            SystemInfo.getLogger().info(saronDateMonthSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronDateMonthSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("technic_class_id", rsSold.getInt("technic_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
             //   SystemInfo.getLogger().info(resMap.toString());
                saronSoldListM.add(resMap);
            }
            rsSold.close();

            //�t���[�p(����)
            saronDateSoldFree.append("select                                                       ");
            saronDateSoldFree.append("selectResult2.technic_class_id ");
            saronDateSoldFree.append(",SUM(selectResult2.product_num) AS product_num");
            saronDateSoldFree.append(",SUM(selectResult2.price) as price");
            saronDateSoldFree.append("  , selectResult2.technic_class_name");
            saronDateSoldFree.append("  , selectResult2.product_division ");
            saronDateSoldFree.append("from (select ");


            //���i����ID
            saronDateSoldFree.append(" mt.technic_class_id ");
            //����
            saronDateSoldFree.append(" , dsd.product_num AS product_num ");
            //������z
            //saronDateSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price  ");

             //������z(�����)
            saronDateSoldFree.append(" , case dsd.product_division when 3 then  ceil( abs(    (   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     (  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            saronDateSoldFree.append(" , mtc.technic_class_name ");
            //�Z�p���i�敪�h�c
            saronDateSoldFree.append(" , dsd.product_division ");
            saronDateSoldFree.append("from                                                         ");
            saronDateSoldFree.append(" data_sales_detail as dsd                                    ");
            saronDateSoldFree.append(" inner join data_sales as ds                                 ");
            saronDateSoldFree.append("   on dsd.shop_id = ds.shop_id                               ");
            saronDateSoldFree.append("   and dsd.slip_no = ds.slip_no                              ");
            saronDateSoldFree.append(" inner join mst_technic as mt                                   ");
            saronDateSoldFree.append("   on dsd.product_id = mt.technic_id                            ");
            saronDateSoldFree.append(" inner join mst_technic_class mtc                               ");
            saronDateSoldFree.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateSoldFree.append(" left join mst_staff msf                                ");
            saronDateSoldFree.append("   on msf.staff_id = ds.staff_id                   ");
            saronDateSoldFree.append("where                                                        ");
            saronDateSoldFree.append(" dsd.product_division in (1,3)                                   ");
            saronDateSoldFree.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");

            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateSoldFree.append(" and mt.technic_class_id in (2) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronDateSoldFree.append(" and ds.shop_id = ").append(shopList);
            saronDateSoldFree.append(" and dsd.delete_date IS NULL                                 ");
            saronDateSoldFree.append(" and ds.delete_date IS NULL                                  ");
            saronDateSoldFree.append(" and mt.delete_date IS NULL                                  ");
            saronDateSoldFree.append(" and mtc.delete_date IS NULL                                 ");

            //�t���[���̂̓T�����ȊO
            saronDateSoldFree.append(" and msf.staff_name1<>'�T����'                                 ");

            saronDateSoldFree.append(" )as selectResult2 ");


            saronDateSoldFree.append("group by                                                     ");
            saronDateSoldFree.append("  selectResult2.technic_class_id ");
            saronDateSoldFree.append("  , selectResult2.technic_class_name ");
            saronDateSoldFree.append("  , selectResult2.product_division ");


            saronDateSoldFree.append("order by                                                     ");
            saronDateSoldFree.append("  selectResult2.technic_class_id                                        ");
            saronDateSoldFree.append(" , selectResult2.product_division;                                         ");

            SystemInfo.getLogger().info(saronDateSoldFree.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronDateSoldFree.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("technic_class_id", rsSold.getInt("technic_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
             //   SystemInfo.getLogger().info(resMap.toString());
                saronSoldFreeList.add(resMap);
            }
            rsSold.close();


            //�t���[����
            saronDateMonthSoldFree.append("select                                                       ");


            saronDateMonthSoldFree.append("selectResult2.technic_class_id ");
            saronDateMonthSoldFree.append(",SUM(selectResult2.product_num) AS product_num");
            saronDateMonthSoldFree.append(",SUM(selectResult2.price) as price");
            saronDateMonthSoldFree.append("  , selectResult2.technic_class_name");
            saronDateMonthSoldFree.append("  , selectResult2.product_division ");
            saronDateMonthSoldFree.append("from (select ");

            //���i����ID
            saronDateMonthSoldFree.append(" mt.technic_class_id ");
            //����
            saronDateMonthSoldFree.append(" , dsd.product_num AS product_num ");
            //������z
            //saronDateMonthSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08) AS price  ");

            //������z(�����)
            saronDateMonthSoldFree.append(" , case dsd.product_division when 3 then  ceil( abs(    (   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     (  dsd.product_num * dsd.product_value - dsd.discount_value ) /(1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");


            //���i���ޖ�
            saronDateMonthSoldFree.append(" , mtc.technic_class_name ");
            //�Z�p���i�敪�h�c
            saronDateMonthSoldFree.append(" , dsd.product_division ");
            saronDateMonthSoldFree.append("from                                                         ");
            saronDateMonthSoldFree.append(" data_sales_detail as dsd                                    ");
            saronDateMonthSoldFree.append(" inner join data_sales as ds                                 ");
            saronDateMonthSoldFree.append("   on dsd.shop_id = ds.shop_id                               ");
            saronDateMonthSoldFree.append("   and dsd.slip_no = ds.slip_no                              ");
            saronDateMonthSoldFree.append(" inner join mst_technic as mt                                   ");
            saronDateMonthSoldFree.append("   on dsd.product_id = mt.technic_id                            ");
            saronDateMonthSoldFree.append(" inner join mst_technic_class mtc                               ");
            saronDateMonthSoldFree.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            //�S���҂��T�����̏ꍇ�̂� �ǉ�
            saronDateMonthSoldFree.append(" left join mst_staff msf                                ");
            saronDateMonthSoldFree.append("   on msf.staff_id = ds.staff_id                   ");

            saronDateMonthSoldFree.append("where                                                        ");
            saronDateMonthSoldFree.append(" dsd.product_division in (1,3)                                   ");
            //��������
            saronDateMonthSoldFree.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            saronDateMonthSoldFree.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            //�̌��@ID=1�A�C���I�[���@ID=15�A������@ID=3�A�t���[����@ID=2
            //�t���[�͕ʂŎ擾
            saronDateMonthSoldFree.append(" and mt.technic_class_id in (2) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronDateMonthSoldFree.append(" and ds.shop_id = ").append(shopList);
            saronDateMonthSoldFree.append(" and dsd.delete_date IS NULL                                 ");
            saronDateMonthSoldFree.append(" and ds.delete_date IS NULL                                  ");
            saronDateMonthSoldFree.append(" and mt.delete_date IS NULL                                  ");
            saronDateMonthSoldFree.append(" and mtc.delete_date IS NULL                                 ");
            //�t���[���̂̓T�����ȊO

            saronDateMonthSoldFree.append(" and msf.staff_name1 <> '�T����'  ");

            saronDateMonthSoldFree.append(" )as selectResult2 ");
            saronDateMonthSoldFree.append("group by");
            saronDateMonthSoldFree.append("  selectResult2.technic_class_id ");
            saronDateMonthSoldFree.append("  , selectResult2.technic_class_name ");
            saronDateMonthSoldFree.append("  , selectResult2.product_division ");
            saronDateMonthSoldFree.append("order by                                                     ");
            saronDateMonthSoldFree.append(" technic_class_id                                           ");
            saronDateMonthSoldFree.append(" , product_division;                                        ");

            SystemInfo.getLogger().info(saronDateMonthSoldFree.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronDateMonthSoldFree.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("technic_class_id", rsSold.getInt("technic_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
            //    SystemInfo.getLogger().info(resMap.toString());
                saronSoldFreeListM.add(resMap);
            }
            rsSold.close();





            //�B�������㕨�̃f�[�^�擾
            saronItemDateSold.append("select                                                       ");
            //���i����ID
            saronItemDateSold.append(" mi.item_class_id ");
            //����
            saronItemDateSold.append(" , SUM(dsd.product_num) AS product_num ");
            //������z
            //saronItemDateSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");

            //������z�i����Łj
            saronItemDateSold.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");


            //���i���ޖ�
            saronItemDateSold.append(" , mic.item_class_name ");
            //�Z�p���i�敪�h�c
            saronItemDateSold.append(" , dsd.product_division ");
            saronItemDateSold.append("from                                                         ");
            saronItemDateSold.append(" data_sales_detail as dsd                                    ");
            saronItemDateSold.append(" inner join data_sales as ds                                 ");
            saronItemDateSold.append("   on dsd.shop_id = ds.shop_id                               ");
            saronItemDateSold.append("   and dsd.slip_no = ds.slip_no                              ");
            saronItemDateSold.append(" inner join mst_item as mi                                   ");
            saronItemDateSold.append("   on dsd.product_id = mi.item_id                            ");
            saronItemDateSold.append(" inner join mst_item_class mic                               ");
            saronItemDateSold.append("   on mi.item_class_id = mic.item_class_id                   ");


            saronItemDateSold.append("where                                                        ");
            saronItemDateSold.append(" dsd.product_division in (2,4)                                  ");
            saronItemDateSold.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            saronItemDateSold.append(" and mi.item_class_id in (14,22,65,66,67,68,69,70,71,72,73,74,75,78) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronItemDateSold.append(" and ds.shop_id = ").append(shopList);
            saronItemDateSold.append(" and dsd.delete_date IS NULL                                 ");
            saronItemDateSold.append(" and ds.delete_date IS NULL                                  ");
            saronItemDateSold.append(" and mi.delete_date IS NULL                                  ");
            saronItemDateSold.append(" and mic.delete_date IS NULL                                 ");
            saronItemDateSold.append("group by");
            saronItemDateSold.append("  mi.item_class_id ");
            saronItemDateSold.append("  , mic.item_class_name ");
            saronItemDateSold.append("  , dsd.product_division ");
            saronItemDateSold.append("  , ds.sales_date, dsd.tax_rate ");
            saronItemDateSold.append("order by                                                     ");
            saronItemDateSold.append(" mi.item_class_id                                           ");
            saronItemDateSold.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(saronItemDateSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronItemDateSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("item_class_id", rsSold.getInt("item_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
           //     SystemInfo.getLogger().info(resMap.toString());
                saronItemSoldList.add(resMap);
            }
            rsSold.close();

            //�C�������㕨�̃f�[�^�擾
            saronItemDateMonthSold.append("select                                                       ");
            //���i����ID
            saronItemDateMonthSold.append(" mi.item_class_id ");
            //����
            saronItemDateMonthSold.append(" , SUM(dsd.product_num) AS product_num ");
            //������z
            //saronItemDateMonthSold.append(" , ceil(SUM(dsd.product_num * dsd.product_value - dsd.discount_value) / 1.08) AS price ");

            //������z(�����)
            saronItemDateMonthSold.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            saronItemDateMonthSold.append(" , mic.item_class_name ");
            //�Z�p���i�敪�h�c
            saronItemDateMonthSold.append(" , dsd.product_division ");
            saronItemDateMonthSold.append("from                                                         ");
            saronItemDateMonthSold.append(" data_sales_detail as dsd                                    ");
            saronItemDateMonthSold.append(" inner join data_sales as ds                                 ");
            saronItemDateMonthSold.append("   on dsd.shop_id = ds.shop_id                               ");
            saronItemDateMonthSold.append("   and dsd.slip_no = ds.slip_no                              ");
            saronItemDateMonthSold.append(" inner join mst_item as mi                                   ");
            saronItemDateMonthSold.append("   on dsd.product_id = mi.item_id                            ");
            saronItemDateMonthSold.append(" inner join mst_item_class mic                               ");
            saronItemDateMonthSold.append("   on mi.item_class_id = mic.item_class_id                   ");
            saronItemDateMonthSold.append("where                                                        ");
            saronItemDateMonthSold.append(" dsd.product_division in (2,4)                                  ");
            //��������
            saronItemDateMonthSold.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            saronItemDateMonthSold.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            saronItemDateMonthSold.append(" and mi.item_class_id in (14,22,65,66,67,68,69,70,71,72,73,74,75,78) ");
            //���O�C�����[�U�[�̃V���b�vID
            saronItemDateMonthSold.append(" and ds.shop_id = ").append(shopList);
            saronItemDateMonthSold.append(" and dsd.delete_date IS NULL                                 ");
            saronItemDateMonthSold.append(" and ds.delete_date IS NULL                                  ");
            saronItemDateMonthSold.append(" and mi.delete_date IS NULL                                  ");
            saronItemDateMonthSold.append(" and mic.delete_date IS NULL                                 ");
            saronItemDateMonthSold.append("group by");
            saronItemDateMonthSold.append("  mi.item_class_id ");
            saronItemDateMonthSold.append("  , mic.item_class_name ");
            saronItemDateMonthSold.append("  , dsd.product_division ,ds.sales_date, dsd.tax_rate ");
            saronItemDateMonthSold.append("order by                                                     ");
            saronItemDateMonthSold.append(" mi.item_class_id                                           ");
            saronItemDateMonthSold.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(saronItemDateMonthSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronItemDateMonthSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("item_class_id", rsSold.getInt("item_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
           //     SystemInfo.getLogger().info(resMap.toString());
                saronItemSoldListM.add(resMap);
            }
            rsSold.close();

            //�D�������㕨�̃f�[�^�擾�i�T�������ϕi�j
            saronItemDiscountDateSold.append("select                                                                                                              ");

            //����őΉ�
            saronItemDiscountDateSold.append("sum(resultCalc.product_num)  AS product_num,sum(resultCalc.price) as price from ( select");

            saronItemDiscountDateSold.append("  SUM(zyoudaiSron.product_numFugo) AS product_num                                                                   ");

            //saronItemDiscountDateSold.append("  , ceil( SUM( zyoudaiSron.product_num * zyoudaiSron.product_value - zyoudaiSron.discount_value ) / 1.08 ) as price ");
            // ����őΉ�
            saronItemDiscountDateSold.append("  , case 1=1 when  (zyoudaiSron.product_numFugo <0) then  ceil( abs( SUM( zyoudaiSron.product_num * zyoudaiSron.product_value - zyoudaiSron.discount_value  ) / (1+COALESCE(zyoudaiSron.tax_rate,  get_tax_rate(zyoudaiSron.sales_date))))  )*-1   when (zyoudaiSron.product_numFugo >=0) then ceil(  SUM( zyoudaiSron.product_num * zyoudaiSron.product_value - zyoudaiSron.discount_value  ) / (1+COALESCE(zyoudaiSron.tax_rate,  get_tax_rate(zyoudaiSron.sales_date))))  end as price ");
            saronItemDiscountDateSold.append("from                                                                                                                ");
            saronItemDiscountDateSold.append("  (                                                                                                                 ");
            saronItemDiscountDateSold.append("    select                                                                                                          ");
            saronItemDiscountDateSold.append("      case dsd.product_division                                                                                     ");
            saronItemDiscountDateSold.append("        when 2 then dsd.product_num * 1                                                                             ");
            saronItemDiscountDateSold.append("        when 4 then dsd.product_num * - 1                                                                           ");
            saronItemDiscountDateSold.append("        end as product_numFugo                                                                                      ");
            saronItemDiscountDateSold.append("      , dsd.product_num                                                                                             ");
            saronItemDiscountDateSold.append("      , dsd.product_value                                                                                           ");
            saronItemDiscountDateSold.append("      , dsd.discount_value                                                                                          ");
            saronItemDiscountDateSold.append("      , dsd.tax_rate                                                                                          ");
            saronItemDiscountDateSold.append("      , ds.sales_date                                                                                          ");
            saronItemDiscountDateSold.append("    from                                                                                                            ");
            saronItemDiscountDateSold.append("      data_sales_detail as dsd                                                                                      ");
            saronItemDiscountDateSold.append("      inner join data_sales as ds                                                                                   ");
            saronItemDiscountDateSold.append("        on dsd.shop_id = ds.shop_id                                                                                 ");
            saronItemDiscountDateSold.append("        and dsd.slip_no = ds.slip_no                                                                                ");
            saronItemDiscountDateSold.append("      inner join mst_item as mi                                                                                     ");
            saronItemDiscountDateSold.append("        on dsd.product_id = mi.item_id                                                                              ");
            saronItemDiscountDateSold.append("      inner join mst_item_class mic                                                                                 ");
            saronItemDiscountDateSold.append("        on mi.item_class_id = mic.item_class_id                                                                     ");

            //�l�����Ȃ��{�S���X�^�b�t���T����
            saronItemDiscountDateSold.append("      left join mst_staff msf                                                                                 ");
            saronItemDiscountDateSold.append("        on msf.staff_id = ds.staff_id                                                                     ");

            saronItemDiscountDateSold.append("    where                                                                                                           ");
            saronItemDiscountDateSold.append("      dsd.product_division in (2, 4)                                                                                ");
            saronItemDiscountDateSold.append("      and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");

            //�l�����Ȃ��{�S���X�^�b�t���T����
            saronItemDiscountDateSold.append("      and (dsd.discount_value != 0 or msf.staff_name1 ='�T����' )                                                                                ");
            saronItemDiscountDateSold.append("      and ds.shop_id = ").append(shopList);
            saronItemDiscountDateSold.append("      and dsd.delete_date IS NULL                                                                                   ");
            saronItemDiscountDateSold.append("      and ds.delete_date IS NULL                                                                                    ");
            saronItemDiscountDateSold.append("      and mi.delete_date IS NULL                                                                                    ");
            saronItemDiscountDateSold.append("      and mic.delete_date IS NULL                                                                                   ");

            //�K�E����EMS�ƎЈ�����
            saronItemDiscountDateSold.append("      and mi.item_class_id not in (14,22,65,66,67,68,69,70,71,72,73,74,75,78)                                                                                   ");



            saronItemDiscountDateSold.append("  ) as zyoudaiSron   group by zyoudaiSron.product_numFugo, zyoudaiSron.sales_date, zyoudaiSron.tax_rate  ) as resultCalc");

            SystemInfo.getLogger().info(saronItemDiscountDateSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronItemDiscountDateSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                if (null != rsSold.getString("product_num")) {
                    resMap.put("product_num", rsSold.getInt("product_num"));
                    resMap.put("price", rsSold.getInt("price"));
                //    SystemInfo.getLogger().info(resMap.toString());
                    saronItemDiscountSoldList.add(resMap);
                }
            }
            rsSold.close();

            //�E�������㕨�̃f�[�^�擾�i�T�������ϕi�j
            saronItemDiscountDateMonthSold.append("select                                                                                                              ");

            //����őΉ�
            saronItemDiscountDateMonthSold.append("sum(resultCalc.product_num)  AS product_num,sum(resultCalc.price) as price from (select ");

            saronItemDiscountDateMonthSold.append("  SUM(zyoudaiSron.product_numFugo) AS product_num                                                                   ");

            //saronItemDiscountDateMonthSold.append("  , ceil( SUM( zyoudaiSron.product_num * zyoudaiSron.product_value - zyoudaiSron.discount_value ) / 1.08 ) as price ");

            //����őΉ�
            saronItemDiscountDateMonthSold.append("  , case 1=1  when (zyoudaiSron.product_numFugo <0) then  ceil( abs( SUM( zyoudaiSron.product_num * zyoudaiSron.product_value - zyoudaiSron.discount_value  ) / (1+COALESCE(zyoudaiSron.tax_rate,  get_tax_rate(zyoudaiSron.sales_date))))  )*-1   when (zyoudaiSron.product_numFugo >=0) then ceil(  SUM( zyoudaiSron.product_num * zyoudaiSron.product_value - zyoudaiSron.discount_value  ) / (1+COALESCE(zyoudaiSron.tax_rate,  get_tax_rate(zyoudaiSron.sales_date))))  end as price ");
            saronItemDiscountDateMonthSold.append("from                                                                                                                ");
            saronItemDiscountDateMonthSold.append("  (                                                                                                                 ");
            saronItemDiscountDateMonthSold.append("    select                                                                                                          ");
            saronItemDiscountDateMonthSold.append("      case dsd.product_division                                                                                     ");
            saronItemDiscountDateMonthSold.append("        when 2 then dsd.product_num * 1                                                                             ");
            saronItemDiscountDateMonthSold.append("        when 4 then dsd.product_num * - 1                                                                           ");
            saronItemDiscountDateMonthSold.append("        end as product_numFugo                                                                                      ");
            saronItemDiscountDateMonthSold.append("      , dsd.product_num                                                                                             ");
            saronItemDiscountDateMonthSold.append("      , dsd.product_value                                                                                           ");
            saronItemDiscountDateMonthSold.append("      , dsd.discount_value                                                                                          ");
            saronItemDiscountDateMonthSold.append("      , dsd.tax_rate                                                                                          ");
            saronItemDiscountDateMonthSold.append("      , ds.sales_date                                                                                          ");
            saronItemDiscountDateMonthSold.append("    from                                                                                                            ");
            saronItemDiscountDateMonthSold.append("      data_sales_detail as dsd                                                                                      ");
            saronItemDiscountDateMonthSold.append("      inner join data_sales as ds                                                                                   ");
            saronItemDiscountDateMonthSold.append("        on dsd.shop_id = ds.shop_id                                                                                 ");
            saronItemDiscountDateMonthSold.append("        and dsd.slip_no = ds.slip_no                                                                                ");
            saronItemDiscountDateMonthSold.append("      inner join mst_item as mi                                                                                     ");
            saronItemDiscountDateMonthSold.append("        on dsd.product_id = mi.item_id                                                                              ");
            saronItemDiscountDateMonthSold.append("      inner join mst_item_class mic                                                                                 ");
            saronItemDiscountDateMonthSold.append("        on mi.item_class_id = mic.item_class_id                                                                     ");
            //�l�����Ȃ��{�S���X�^�b�t���T����
            saronItemDiscountDateMonthSold.append("      left join mst_staff msf                                                                                 ");
            saronItemDiscountDateMonthSold.append("        on msf.staff_id = ds.staff_id                                                                     ");


            saronItemDiscountDateMonthSold.append("    where                                                                                                           ");
            saronItemDiscountDateMonthSold.append("      dsd.product_division in (2, 4)                                                                                ");
            saronItemDiscountDateMonthSold.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            saronItemDiscountDateMonthSold.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");

            //�l�����Ȃ��{�S���X�^�b�t���T����
            saronItemDiscountDateMonthSold.append("      and (dsd.discount_value != 0  or msf.staff_name1='�T����')                                                                                 ");
            saronItemDiscountDateMonthSold.append("      and ds.shop_id = ").append(shopList);
            saronItemDiscountDateMonthSold.append("      and dsd.delete_date IS NULL                                                                                   ");
            saronItemDiscountDateMonthSold.append("      and ds.delete_date IS NULL                                                                                    ");
            saronItemDiscountDateMonthSold.append("      and mi.delete_date IS NULL                                                                                    ");
            saronItemDiscountDateMonthSold.append("      and mic.delete_date IS NULL                                                                                   ");
            //�K�E����EMS�ƎЈ�����
            saronItemDiscountDateMonthSold.append("      and mi.item_class_id not in (14,22,65,66,67,68,69,70,71,72,73,74,75,78)                                                                                   ");


            //����őΉ�
            saronItemDiscountDateMonthSold.append("  ) as zyoudaiSron  group by zyoudaiSron.product_numFugo, zyoudaiSron.sales_date, zyoudaiSron.tax_rate ) as resultCalc");

            SystemInfo.getLogger().info(saronItemDiscountDateMonthSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(saronItemDiscountDateMonthSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                if (null != rsSold.getString("product_num")) {
                    resMap.put("product_num", rsSold.getInt("product_num"));
                    resMap.put("price", rsSold.getInt("price"));
              //     SystemInfo.getLogger().info(resMap.toString());
                    saronItemDiscountSoldListM.add(resMap);
                }
            }
            rsSold.close();

            //---------------------------------------------------------------------------------
            // �𖱔���
            //---------------------------------------------------------------------------------
            //�{�������SQL
            StringBuilder contractDateSold = new StringBuilder(4000);
            //���������SQL
            StringBuilder contractDateMonthSold = new StringBuilder(4000);

            //�@�𖱔���f�[�^�擾
            contractDateSold.append("select                                               ");
            //�_�񕪗�ID
            contractDateSold.append(" mc.course_class_id                                ");
            //����
            contractDateSold.append(" , SUM(dsd.product_num) AS product_num ");
            //������z
            //contractDateSold.append(" , ceil(SUM( dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");

            //������z�i����Łj
            contractDateSold.append(" , case dsd.product_division when 8 then  ceil( abs(    SUM(   dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 5 then   ceil(     SUM(   dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //�R�[�X���ޖ�
            contractDateSold.append(" , mcc.course_class_name                             ");
            //�Z�p���i�敪�h�c
            contractDateSold.append(" , dsd.product_division ");
            //�\����
            contractDateSold.append(" , mcc.display_seq                                           ");
            contractDateSold.append("from                                                 ");
            contractDateSold.append(" data_sales_detail as dsd                            ");
            contractDateSold.append(" inner join data_sales as ds                         ");
            contractDateSold.append("   on dsd.shop_id = ds.shop_id                       ");
            contractDateSold.append("   and dsd.slip_no = ds.slip_no                      ");
            contractDateSold.append(" inner join mst_course as mc                         ");
            contractDateSold.append("   on dsd.product_id = mc.course_id                  ");
            contractDateSold.append(" inner join mst_course_class mcc                     ");
            contractDateSold.append("   on mc.course_class_id = mcc.course_class_id       ");
            contractDateSold.append("where                                                ");
            contractDateSold.append(" dsd.product_division in(5,8)                        ");
            contractDateSold.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            contractDateSold.append(" and mcc.display_seq in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)         ");
            //���O�C�����[�U�[�̃V���b�vID
            contractDateSold.append(" and ds.shop_id = ").append(shopList);
            contractDateSold.append(" and dsd.delete_date IS NULL                         ");
            contractDateSold.append(" and ds.delete_date IS NULL                          ");
            contractDateSold.append(" and mc.delete_date IS NULL                          ");
            contractDateSold.append(" and mcc.delete_date IS NULL                         ");
            contractDateSold.append("group by ");
            contractDateSold.append("  mc.course_class_id ");
            contractDateSold.append("  , mcc.course_class_name ");
            contractDateSold.append("  , dsd.product_division ");
            contractDateSold.append("  , mcc.display_seq , ds.sales_date, dsd.tax_rate ");
            contractDateSold.append("order by                                             ");
            contractDateSold.append(" mcc.display_seq                                 ");
            contractDateSold.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(contractDateSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(contractDateSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("course_class_id", rsSold.getInt("course_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("display_seq", rsSold.getInt("display_seq"));
              //  SystemInfo.getLogger().info(resMap.toString());
                corseSoldList.add(resMap);
            }
            rsSold.close();

            //�A�݌v�𖱔���f�[�^�擾
            contractDateMonthSold.append("select                                               ");
            //�_�񕪗�ID
            contractDateMonthSold.append(" mc.course_class_id                                ");
            //����
            contractDateMonthSold.append(" , SUM(dsd.product_num) AS product_num ");
            //������z
            //contractDateMonthSold.append(" , ceil(SUM( dsd.product_value - dsd.discount_value ) / 1.08) AS price ");

            //������z(�����)
            contractDateMonthSold.append(" , case dsd.product_division when 8 then  ceil( abs(    SUM(   dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 5 then   ceil(     SUM(   dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price ");

            //�R�[�X���ޖ�
            contractDateMonthSold.append(" , mcc.course_class_name                             ");
            //�Z�p���i�敪�h�c
            contractDateMonthSold.append(" , dsd.product_division ");
            //�\����
            contractDateMonthSold.append(" , mcc.display_seq                                           ");
            contractDateMonthSold.append("from                                                 ");
            contractDateMonthSold.append(" data_sales_detail as dsd                            ");
            contractDateMonthSold.append(" inner join data_sales as ds                         ");
            contractDateMonthSold.append("   on dsd.shop_id = ds.shop_id                       ");
            contractDateMonthSold.append("   and dsd.slip_no = ds.slip_no                      ");
            contractDateMonthSold.append(" inner join mst_course as mc                         ");
            contractDateMonthSold.append("   on dsd.product_id = mc.course_id                  ");
            contractDateMonthSold.append(" inner join mst_course_class mcc                     ");
            contractDateMonthSold.append("   on mc.course_class_id = mcc.course_class_id       ");
            contractDateMonthSold.append("where                                                ");
            contractDateMonthSold.append(" dsd.product_division in(5,8)                        ");
            //��������
            contractDateMonthSold.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            contractDateMonthSold.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
            contractDateMonthSold.append(" and mcc.display_seq in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)         ");
            //���O�C�����[�U�[�̃V���b�vID
            contractDateMonthSold.append(" and ds.shop_id = ").append(shopList);
            contractDateMonthSold.append(" and dsd.delete_date IS NULL                         ");
            contractDateMonthSold.append(" and ds.delete_date IS NULL                          ");
            contractDateMonthSold.append(" and mc.delete_date IS NULL                          ");
            contractDateMonthSold.append(" and mcc.delete_date IS NULL                         ");
            contractDateMonthSold.append("group by");
            contractDateMonthSold.append("  mc.course_class_id");
            contractDateMonthSold.append("  , mcc.course_class_name ");
            contractDateMonthSold.append("  , dsd.product_division ");
            contractDateMonthSold.append("  , mcc.display_seq , ds.sales_date, dsd.tax_rate ");
            contractDateMonthSold.append("order by                                             ");
            contractDateMonthSold.append(" mcc.display_seq                                 ");
            contractDateMonthSold.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(contractDateMonthSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(contractDateMonthSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("course_class_id", rsSold.getInt("course_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("display_seq", rsSold.getInt("display_seq"));
             //   SystemInfo.getLogger().info(resMap.toString());
                corseSoldListM.add(resMap);
            }
            rsSold.close();

            //---------------------------------------------------------------------------------
            // ���㕨��
            //---------------------------------------------------------------------------------
            //�{�����㕨�iSQL
            StringBuilder itemDateSold = new StringBuilder(4000);
            //�����݌v���㕨�iSQL
            StringBuilder itemDateMonthSold = new StringBuilder(4000);

            //�@�������㕨�̃f�[�^�擾
            itemDateSold.append("select                                                       ");
            //���i����ID
            itemDateSold.append(" mi.item_class_id ");
            //����
            itemDateSold.append(" , SUM(dsd.product_num) AS product_num ");
            //������z
            //itemDateSold.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");

            //������z(�����
            itemDateSold.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");


            //���i���ޖ�
            itemDateSold.append(" , mic.item_class_name ");
            //�Z�p���i�敪�h�c
            itemDateSold.append(" , dsd.product_division ");
            //�\����
            itemDateSold.append(" , mic.display_seq                                           ");
            itemDateSold.append("from                                                         ");
            itemDateSold.append(" data_sales_detail as dsd                                    ");
            itemDateSold.append(" inner join data_sales as ds                                 ");
            itemDateSold.append("   on dsd.shop_id = ds.shop_id                               ");
            itemDateSold.append("   and dsd.slip_no = ds.slip_no                              ");
            itemDateSold.append(" inner join mst_item as mi                                   ");
            itemDateSold.append("   on dsd.product_id = mi.item_id                            ");
            itemDateSold.append(" inner join mst_item_class mic                               ");
            itemDateSold.append("   on mi.item_class_id = mic.item_class_id                   ");
            itemDateSold.append(" left join mst_staff msf                               ");
            itemDateSold.append("   on msf.staff_id = ds.staff_id                   ");

            //�X�^�b�t���T�����͏��O

            itemDateSold.append("where                                                        ");
            itemDateSold.append(" dsd.product_division in (2,4)                                  ");
            itemDateSold.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID
           // 2018/11/127 �\����44 �͕s�v
            //itemDateSold.append(" and mic.display_seq in (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44) ");
            itemDateSold.append(" and mic.display_seq in (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43) ");

            //���O�C�����[�U�[�̃V���b�vID
            itemDateSold.append(" and ds.shop_id = ").append(shopList);
			//20190310 �l�����ȊO
            itemDateSold.append(" and dsd.discount_value = 0                                 ");
            itemDateSold.append(" and dsd.delete_date IS NULL                                 ");
            itemDateSold.append(" and ds.delete_date IS NULL                                  ");
            itemDateSold.append(" and mi.delete_date IS NULL                                  ");
            itemDateSold.append(" and mic.delete_date IS NULL                                 ");
            itemDateSold.append(" and msf.staff_name1 <>'�T����'                                 ");

            itemDateSold.append("group by");
            itemDateSold.append("  mi.item_class_id");
            itemDateSold.append("  , mic.item_class_name ");
            itemDateSold.append("  , dsd.product_division ");
            itemDateSold.append("  , mic.display_seq        , ds.sales_date, dsd.tax_rate                                   ");
            itemDateSold.append("order by                                                     ");
            itemDateSold.append(" mic.display_seq                                           ");
            itemDateSold.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(itemDateSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(itemDateSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("item_class_id", rsSold.getInt("item_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("display_seq", rsSold.getInt("display_seq"));
            //    SystemInfo.getLogger().info(resMap.toString());
                itemSoldList.add(resMap);
            }
            rsSold.close();

            //�A�������㕨�̃f�[�^�擾
            itemDateMonthSold.append("select                                                       ");
            //���i����ID
            itemDateMonthSold.append(" mi.item_class_id ");
            //����
            itemDateMonthSold.append(" , SUM(dsd.product_num) AS product_num ");
            //������z
            //itemDateMonthSold.append(" , ceil(SUM(dsd.product_num * dsd.product_value - dsd.discount_value) / 1.08 ) AS price ");

            //������z(�����)
            itemDateMonthSold.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            itemDateMonthSold.append(" , mic.item_class_name ");
            //�Z�p���i�敪�h�c
            itemDateMonthSold.append(" , dsd.product_division ");
            //�\����
            itemDateMonthSold.append(" , mic.display_seq                                           ");
            itemDateMonthSold.append("from                                                         ");
            itemDateMonthSold.append(" data_sales_detail as dsd                                    ");
            itemDateMonthSold.append(" inner join data_sales as ds                                 ");
            itemDateMonthSold.append("   on dsd.shop_id = ds.shop_id                               ");
            itemDateMonthSold.append("   and dsd.slip_no = ds.slip_no                              ");
            itemDateMonthSold.append(" inner join mst_item as mi                                   ");
            itemDateMonthSold.append("   on dsd.product_id = mi.item_id                            ");
            itemDateMonthSold.append(" inner join mst_item_class mic                               ");
            itemDateMonthSold.append("   on mi.item_class_id = mic.item_class_id                   ");

            //�T�������O
            itemDateMonthSold.append(" left join mst_staff msf                               ");
            itemDateMonthSold.append("   on msf.staff_id = ds.staff_id                   ");


            itemDateMonthSold.append("where                                                        ");
            itemDateMonthSold.append(" dsd.product_division in (2,4)                                  ");
            //��������
            itemDateMonthSold.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            itemDateMonthSold.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //�o�͂��鏤�i����ID 44�͕s�v 2018/11/27
            //itemDateMonthSold.append(" and mic.display_seq in (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44) ");
            itemDateMonthSold.append(" and mic.display_seq in (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43) ");

            //���O�C�����[�U�[�̃V���b�vID
            itemDateMonthSold.append(" and ds.shop_id = ").append(shopList);
			//20190315 �l�����ȊO
            itemDateMonthSold.append(" and dsd.discount_value = 0 ");
            itemDateMonthSold.append(" and dsd.delete_date IS NULL                                 ");
            itemDateMonthSold.append(" and ds.delete_date IS NULL                                  ");
            itemDateMonthSold.append(" and mi.delete_date IS NULL                                  ");
            itemDateMonthSold.append(" and mic.delete_date IS NULL                                 ");
            itemDateMonthSold.append(" and msf.staff_name1 <>'�T����'                                 ");

            itemDateMonthSold.append("group by");
            itemDateMonthSold.append("  mi.item_class_id");
            itemDateMonthSold.append("  , mic.item_class_name ");
            itemDateMonthSold.append("  , dsd.product_division ");
            itemDateMonthSold.append("  , mic.display_seq      , ds.sales_date, dsd.tax_rate                                     ");
            itemDateMonthSold.append("order by                                                     ");
            itemDateMonthSold.append(" mic.display_seq                                           ");
            itemDateMonthSold.append(" , product_division;                                 ");

            SystemInfo.getLogger().info(itemDateMonthSold.toString());
            rsSold = SystemInfo.getConnection().executeQuery(itemDateMonthSold.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("item_class_id", rsSold.getInt("item_class_id"));
                resMap.put("product_num", rsSold.getInt("product_num"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("display_seq", rsSold.getInt("display_seq"));
           //     SystemInfo.getLogger().info(resMap.toString());
                itemSoldListM.add(resMap);
            }
            rsSold.close();

            //---------------------------------------------------------------------------------
            // ����Q��������
            //---------------------------------------------------------------------------------
            //�������сQ�𖱁i(�����O)�j
            StringBuilder newOrdersM = new StringBuilder(4000);
            //�������сQ���㕨��(�����O)
            StringBuilder performanceItemM = new StringBuilder(4000);
            //�������сQ�Z�p����(�����O)
            StringBuilder performanceSaronM = new StringBuilder(4000);


            //�������сQ�𖱁i(���̂�)�j
            StringBuilder newOrdersMonthProporsal = new StringBuilder(4000);
            //�������сQ���㕨��_���̂�
            StringBuilder performanceItemMonthProporsal = new StringBuilder(4000);
            //�������сQ�Z�p����_���̂�
            StringBuilder performanceSaronMonthProporsal = new StringBuilder(4000);


            //�����󒍃f�[�^�擾
            newOrdersM.append("select                                                       ");
            //���i����ID
            newOrdersM.append(" mc.course_class_id ");
            //������z
            //newOrdersM.append(" , ceil( SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");

            //������z(�����)
            newOrdersM.append(" , case dsd.product_division when 8 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 5 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            newOrdersM.append(" , mcc.course_class_name ");
            //�����z
            newOrdersM.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            newOrdersM.append(" , dsd.product_division ");
            //�X�^�b�t��_�c��
            newOrdersM.append(" , ms.staff_name1 ");
            //�X�^�b�t��_���O
            newOrdersM.append(" , ms.staff_name2 ");
            //�X�܂h�c
            newOrdersM.append(" , ms.shop_id ");
            //�X�ܖ�
            newOrdersM.append(" , msh.shop_name ");
            //�X�^�b�t�敪��
            newOrdersM.append(" , msc.staff_class_name ");
            //�ڋq�h�c
            newOrdersM.append(" , ds.customer_id ");
            //�X�^�b�t�m�n
            newOrdersM.append(" , ms.staff_no ");
            //�̔���
            newOrdersM.append(" , ds.sales_date ");
            newOrdersM.append("from                                                         ");
            newOrdersM.append(" data_sales_detail as dsd                                    ");
            newOrdersM.append(" inner join data_sales as ds                                 ");
            newOrdersM.append("   on dsd.shop_id = ds.shop_id                               ");
            newOrdersM.append("   and dsd.slip_no = ds.slip_no                              ");
            newOrdersM.append(" inner join mst_course as mc                                 ");
            newOrdersM.append("   on dsd.product_id = mc.course_id                          ");
            newOrdersM.append(" inner join mst_course_class mcc                             ");
            newOrdersM.append("   on mc.course_class_id = mcc.course_class_id               ");
            newOrdersM.append(" left join mst_staff ms                                     ");
            newOrdersM.append("   on dsd.staff_id = ms.staff_id                             ");
            newOrdersM.append(" left join mst_staff_class msc                              ");
            newOrdersM.append("   on ms.staff_class_id = msc.staff_class_id                ");
            newOrdersM.append(" left join mst_shop msh                                     ");
            newOrdersM.append("   on ms.shop_id = msh.shop_id                               ");

            //�����O�̂��߁A���e�[�u�����O������
            newOrdersM.append("   left join data_contract_staff_share as dsp ");
            newOrdersM.append("   on dsd.shop_id = dsp.shop_id ");
            newOrdersM.append("   and dsd.slip_no = dsp.slip_no ");
            newOrdersM.append("   and dsd.slip_detail_no = dsp.contract_detail_no ");


            newOrdersM.append("where                                                        ");
            newOrdersM.append(" dsd.product_division in (5,8)                                  ");
            //��������
            newOrdersM.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            newOrdersM.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            newOrdersM.append(" and ds.shop_id = ").append(shopList);
            newOrdersM.append(" and dsd.delete_date IS NULL                                 ");
            newOrdersM.append(" and ds.delete_date IS NULL                                  ");
            newOrdersM.append(" and mc.delete_date IS NULL                                  ");
            newOrdersM.append(" and mcc.delete_date IS NULL                                 ");

            //�폜�X�^�b�t�ł��A���׏�͌v��
            //newOrdersM.append(" and ms.delete_date IS NULL                                 ");
            newOrdersM.append(" and msc.delete_date IS NULL                                 ");
            newOrdersM.append(" and msh.delete_date IS NULL                                 ");

            //���ȊO (data_sales_detail_proportionally�e�[�u���Ƀ��R�[�h�Ȃ��A�����Ă��A���[�g100)
            newOrdersM.append(" and (     (       dsp.slip_no is null       and dsp.contract_detail_no is null    )     or (       dsp.slip_no is not null       and dsp.contract_detail_no is not null       and dsp.rate = 100    )  ) ");

            newOrdersM.append("group by ");
            newOrdersM.append("  mc.course_class_id ");
            newOrdersM.append("  , mcc.course_class_name ");
            newOrdersM.append("  , dsd.product_division ");
            newOrdersM.append("  , dsd.discount_value ");
            newOrdersM.append("  , ms.staff_name1 ");
            newOrdersM.append("  , ms.shop_id ");
            newOrdersM.append("  , msc.staff_class_name ");
            newOrdersM.append("  , ds.customer_id ");
            newOrdersM.append("  , ms.staff_no ");
            newOrdersM.append("  , msh.shop_name ");
            newOrdersM.append("  , ds.sales_date , dsd.tax_rate");
            newOrdersM.append("  , ms.staff_name2 ");
            newOrdersM.append("order by                                                     ");
            newOrdersM.append(" ms.staff_no;                                 ");

            SystemInfo.getLogger().info(newOrdersM.toString());
            rsSold = SystemInfo.getConnection().executeQuery(newOrdersM.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("course_class_id"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("staff_name2", rsSold.getString("staff_name2"));
                resMap.put("staff_class_name", rsSold.getString("staff_class_name"));
                resMap.put("shop_id", rsSold.getString("shop_id"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("staff_no", rsSold.getString("staff_no"));
                resMap.put("shop_name", rsSold.getString("shop_name"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
            //    SystemInfo.getLogger().info(resMap.toString());
                newOrdersListM.add(resMap);
            }
            rsSold.close();

            //�������㕨�̃f�[�^�擾
            performanceItemM.append("select                                                       ");
            //���i����ID
            performanceItemM.append(" mi.item_class_id ");
            //������z
            //performanceItemM.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");
            //������z�i����Łj
            performanceItemM.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            performanceItemM.append(" , mic.item_class_name ");
            //�����z
            performanceItemM.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            performanceItemM.append(" , dsd.product_division ");
            //�X�^�b�t��_�c��
            performanceItemM.append(" , ms.staff_name1 ");
            //�X�^�b�t��_���O
            performanceItemM.append(" , ms.staff_name2 ");
            //�X�܂h�c
            performanceItemM.append(" , ms.shop_id ");
            //�X�ܖ�
            performanceItemM.append(" , msh.shop_name ");
            //�X�^�b�t�敪��
            performanceItemM.append(" , msc.staff_class_name ");
            //�ڋq�h�c
            performanceItemM.append(" , ds.customer_id ");
            //�X�^�b�t�m�n
            performanceItemM.append(" , ms.staff_no ");
            //�̔���
            performanceItemM.append(" , ds.sales_date ");
            performanceItemM.append("from                                                         ");
            performanceItemM.append(" data_sales_detail as dsd                                    ");
            performanceItemM.append(" inner join data_sales as ds                                 ");
            performanceItemM.append("   on dsd.shop_id = ds.shop_id                               ");
            performanceItemM.append("   and dsd.slip_no = ds.slip_no                              ");
            performanceItemM.append(" inner join mst_item as mi                                   ");
            performanceItemM.append("   on dsd.product_id = mi.item_id                            ");
            performanceItemM.append(" inner join mst_item_class mic                               ");
            performanceItemM.append("   on mi.item_class_id = mic.item_class_id                   ");
            performanceItemM.append(" left join mst_staff ms                                     ");
            performanceItemM.append("   on dsd.staff_id = ms.staff_id                             ");
            performanceItemM.append(" left join mst_staff_class msc                              ");
            performanceItemM.append("   on ms.staff_class_id = msc.staff_class_id                ");
            performanceItemM.append(" left join mst_shop msh                                     ");
            performanceItemM.append("   on ms.shop_id = msh.shop_id                               ");

            //���e�[�u���O�������i�����O�̂��߁j
            performanceItemM.append("   left join data_sales_detail_proportionally as dsp ");
            performanceItemM.append("on dsd.shop_id = dsp.shop_id ");
            performanceItemM.append("and dsd.slip_no = dsp.slip_no ");
            performanceItemM.append("and dsd.slip_detail_no = dsp.slip_detail_no ");


            performanceItemM.append("where                                                        ");
            performanceItemM.append(" dsd.product_division in (2,4)                                  ");
            //��������
            performanceItemM.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            performanceItemM.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            performanceItemM.append(" and ds.shop_id = ").append(shopList);
            performanceItemM.append(" and dsd.delete_date IS NULL                                 ");
            performanceItemM.append(" and ds.delete_date IS NULL                                  ");
            performanceItemM.append(" and mi.delete_date IS NULL                                  ");
            performanceItemM.append(" and mic.delete_date IS NULL                                 ");
           //�폜�X�^�b�t�ł��A���׏�͌v��
            //performanceItemM.append(" and ms.delete_date IS NULL                                 ");
            performanceItemM.append(" and msc.delete_date IS NULL                                 ");
            performanceItemM.append(" and msh.delete_date IS NULL                                 ");

            //�l�����ȊO
            performanceItemM.append(" and dsd.discount_value = 0                                 ");

            //���ȊO (data_sales_detail_proportionally�e�[�u���Ƀ��R�[�h�Ȃ��A�����Ă��A���[�g100)
           performanceItemM.append(" and ((dsp.slip_no is null and  dsp.slip_detail_no is null) or (dsp.slip_no is not null and  dsp.slip_detail_no is not null and dsp.ratio = 100))");

           //�T�����ȊO(Java���Ő��䂷��K�v����)
            //performanceItemM.append(" and ms.staff_name1<>'�T����' ");

            performanceItemM.append("group by");
            performanceItemM.append("  mi.item_class_id ");
            performanceItemM.append("  , mic.item_class_name ");
            performanceItemM.append("  , dsd.product_division ");
            performanceItemM.append("  , dsd.discount_value ");
            performanceItemM.append("  , ms.staff_name1 ");
            performanceItemM.append("  , ms.shop_id ");
            performanceItemM.append("  , msc.staff_class_name ");
            performanceItemM.append("  , ds.customer_id ");
            performanceItemM.append("  , ms.staff_no ");
            performanceItemM.append("  , msh.shop_name ");
            performanceItemM.append("  , ds.sales_date ,dsd.tax_rate ");
            performanceItemM.append("  , ms.staff_name2 ");
            performanceItemM.append("order by                                                     ");
            performanceItemM.append(" ms.staff_no;                                 ");

            SystemInfo.getLogger().info(performanceItemM.toString());
            rsSold = SystemInfo.getConnection().executeQuery(performanceItemM.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("item_class_id"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("staff_name2", rsSold.getString("staff_name2"));
                resMap.put("staff_class_name", rsSold.getString("staff_class_name"));
                resMap.put("shop_id", rsSold.getString("shop_id"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("staff_no", rsSold.getString("staff_no"));
                resMap.put("shop_name", rsSold.getString("shop_name"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
               // SystemInfo.getLogger().info(resMap.toString());
                performanceItemSaronListM.add(resMap);
            }
            rsSold.close();


            //��������T�����f�[�^�擾
            performanceSaronM.append("select                                                       ");
            //���i����ID
            performanceSaronM.append(" mt.technic_class_id ");
            //������z
            //performanceSaronM.append(" , ceil(SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08) AS price  ");

            //������z(�����)
            performanceSaronM.append(" , case dsd.product_division when 3 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))))  )*-1   when 1 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) )  end as price  ");

            //���i���ޖ�
            performanceSaronM.append(" , mtc.technic_class_name ");
            //�����z
            performanceSaronM.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            performanceSaronM.append(" , dsd.product_division ");
            //�X�^�b�t��_�c��
            performanceSaronM.append(" , ms.staff_name1 ");
            //�X�^�b�t��_���O
            performanceSaronM.append(" , ms.staff_name2 ");
            //�X�܂h�c
            performanceSaronM.append(" , ms.shop_id ");
            //�X�ܖ�
            performanceSaronM.append(" , msh.shop_name ");
            //�X�^�b�t�敪��
            performanceSaronM.append(" , msc.staff_class_name ");
            //�ڋq�h�c
            performanceSaronM.append(" , ds.customer_id ");
            //�X�^�b�t�m�n
            performanceSaronM.append(" , ms.staff_no ");
            //�̔���
            performanceSaronM.append(" , ds.sales_date ");
            performanceSaronM.append("from                                                         ");
            performanceSaronM.append(" data_sales_detail as dsd                                    ");
            performanceSaronM.append(" inner join data_sales as ds                                 ");
            performanceSaronM.append("   on dsd.shop_id = ds.shop_id                               ");
            performanceSaronM.append("   and dsd.slip_no = ds.slip_no                              ");
            performanceSaronM.append(" inner join mst_technic as mt                                   ");
            performanceSaronM.append("   on dsd.product_id = mt.technic_id                            ");
            performanceSaronM.append(" inner join mst_technic_class mtc                               ");
            performanceSaronM.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            performanceSaronM.append(" left join mst_staff ms                                     ");
            performanceSaronM.append("   on dsd.staff_id = ms.staff_id                             ");
            performanceSaronM.append(" left join mst_staff_class msc                              ");
            performanceSaronM.append("   on ms.staff_class_id = msc.staff_class_id                ");
            performanceSaronM.append(" left join mst_shop msh                                     ");
            performanceSaronM.append("   on ms.shop_id = msh.shop_id                               ");

            //���e�[�u���O�������i�����O�̂��߁j
            performanceItemM.append("   left join data_sales_detail_proportionally as dsp ");
            performanceItemM.append("on dsd.shop_id = dsp.shop_id ");
            performanceItemM.append("and dsd.slip_no = dsp.slip_no ");
            performanceItemM.append("and dsd.slip_detail_no = dsp.slip_detail_no ");


            performanceSaronM.append("where                                                        ");
            performanceSaronM.append(" dsd.product_division in (1,3)                                   ");
            //��������
            performanceSaronM.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            performanceSaronM.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            performanceSaronM.append(" and ds.shop_id = ").append(shopList);
            performanceSaronM.append(" and dsd.delete_date IS NULL                                 ");
            performanceSaronM.append(" and ds.delete_date IS NULL                                  ");
            performanceSaronM.append(" and mt.delete_date IS NULL                                  ");
            performanceSaronM.append(" and mtc.delete_date IS NULL                                 ");

            //�폜�X�^�b�t�ł��A���׏�͌v��
            // performanceSaronM.append(" and ms.delete_date IS NULL                                  ");
            performanceSaronM.append(" and msc.delete_date IS NULL                                 ");
            performanceSaronM.append(" and msh.delete_date IS NULL                                 ");

            //���ȊO (data_sales_detail_proportionally�e�[�u���Ƀ��R�[�h�Ȃ��A�����Ă��A���[�g100)
           performanceItemM.append(" and ((dsp.slip_no is null and  dsp.slip_detail_no is null) or (dsp.slip_no is not null and  dsp.slip_detail_no is not null and dsp.ratio = 100))");


            performanceSaronM.append("group by                                                     ");
            performanceSaronM.append("  mt.technic_class_id ");
            performanceSaronM.append("  , mtc.technic_class_name ");
            performanceSaronM.append("  , dsd.product_division ");
            performanceSaronM.append("  , dsd.discount_value ");
            performanceSaronM.append("  , ms.staff_name1 ");
            performanceSaronM.append("  , ms.shop_id ");
            performanceSaronM.append("  , msc.staff_class_name ");
            performanceSaronM.append("  , ds.customer_id ");
            performanceSaronM.append("  , ms.staff_no ");
            performanceSaronM.append("  , msh.shop_name ");
            performanceSaronM.append("  , ds.sales_date ,dsd.tax_rate ");
            performanceSaronM.append("  , ms.staff_name2 ");
            performanceSaronM.append("order by                                                     ");
            performanceSaronM.append("  ms.staff_no;                                         ");

            SystemInfo.getLogger().info(performanceSaronM.toString());
            rsSold = SystemInfo.getConnection().executeQuery(performanceSaronM.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("technic_class_id"));
                resMap.put("price", rsSold.getInt("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("staff_name2", rsSold.getString("staff_name2"));
                resMap.put("staff_class_name", rsSold.getString("staff_class_name"));
                resMap.put("shop_id", rsSold.getString("shop_id"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("staff_no", rsSold.getString("staff_no"));
                resMap.put("shop_name", rsSold.getString("shop_name"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
               // SystemInfo.getLogger().info(resMap.toString());
                performanceItemSaronListM.add(resMap);
            }
            rsSold.close();


            SystemInfo.getLogger().info("���N�G���J�n");
            //�ǉ��P �i�R�[�X�̈��j


            //�����󒍃f�[�^�擾
            newOrdersMonthProporsal.append("select                                                       ");
            //���i����ID
            newOrdersMonthProporsal.append(" mc.course_class_id ");
            //������z
            //newOrdersM.append(" , ceil( SUM( dsd.product_num * dsd.product_value - dsd.discount_value ) / 1.08 ) AS price ");

            //������z(�����) ���A���[�g����
            newOrdersMonthProporsal.append(" , case dsd.product_division when 8 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date)))) *(dsp.rate * 0.01) )*-1   when 5 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) ) *(dsp.rate * 0.01) end as price  ");

            //���i���ޖ�
            newOrdersMonthProporsal.append(" , mcc.course_class_name ");
            //�����z
            newOrdersMonthProporsal.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            newOrdersMonthProporsal.append(" , dsd.product_division ");
            //�X�^�b�t��_�c��
            newOrdersMonthProporsal.append(" , ms.staff_name1 ");
            //�X�^�b�t��_���O
            newOrdersMonthProporsal.append(" , ms.staff_name2 ");
            //�X�܂h�c
            newOrdersMonthProporsal.append(" , ms.shop_id ");
            //�X�ܖ�
            newOrdersMonthProporsal.append(" , msh.shop_name ");
            //�X�^�b�t�敪��
            newOrdersMonthProporsal.append(" , msc.staff_class_name ");
            //�ڋq�h�c
            newOrdersMonthProporsal.append(" , ds.customer_id ");
            //�X�^�b�t�m�n
            newOrdersMonthProporsal.append(" , ms.staff_no ");
            //�̔���
            newOrdersMonthProporsal.append(" , ds.sales_date ");
            newOrdersMonthProporsal.append("from                                                         ");
            newOrdersMonthProporsal.append(" data_sales_detail as dsd                                    ");
            newOrdersMonthProporsal.append(" inner join data_sales as ds                                 ");
            newOrdersMonthProporsal.append("   on dsd.shop_id = ds.shop_id                               ");
            newOrdersMonthProporsal.append("   and dsd.slip_no = ds.slip_no                              ");

            //���e�[�u����������
            newOrdersMonthProporsal.append(" inner join data_contract_staff_share as dsp                                 ");
            newOrdersMonthProporsal.append("   on dsd.shop_id = dsp.shop_id                               ");
            newOrdersMonthProporsal.append("   and dsd.slip_no = dsp.slip_no                              ");
            newOrdersMonthProporsal.append("   and dsd.slip_detail_no = dsp.contract_detail_no                              ");

            newOrdersMonthProporsal.append(" inner join mst_course as mc                                 ");
            newOrdersMonthProporsal.append("   on dsd.product_id = mc.course_id                          ");
            newOrdersMonthProporsal.append(" inner join mst_course_class mcc                             ");
            newOrdersMonthProporsal.append("   on mc.course_class_id = mcc.course_class_id               ");
            newOrdersMonthProporsal.append(" left join mst_staff ms                                     ");

            //�X�^�b�t�}�X�^�Ƃ̌����́A���e�[�u��
            newOrdersMonthProporsal.append("   on dsp.staff_id = ms.staff_id                             ");
            newOrdersMonthProporsal.append(" left join mst_staff_class msc                              ");
            newOrdersMonthProporsal.append("   on ms.staff_class_id = msc.staff_class_id                ");
            newOrdersMonthProporsal.append(" left join mst_shop msh                                     ");
            newOrdersMonthProporsal.append("   on ms.shop_id = msh.shop_id                               ");
            newOrdersMonthProporsal.append("where                                                        ");
            newOrdersMonthProporsal.append(" dsd.product_division in (5,8)                                  ");
            //��������
            newOrdersMonthProporsal.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            newOrdersMonthProporsal.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            newOrdersMonthProporsal.append(" and ds.shop_id = ").append(shopList);
            newOrdersMonthProporsal.append(" and dsd.delete_date IS NULL                                 ");
            newOrdersMonthProporsal.append(" and ds.delete_date IS NULL                                  ");
            newOrdersMonthProporsal.append(" and mc.delete_date IS NULL                                  ");
            newOrdersMonthProporsal.append(" and mcc.delete_date IS NULL                                 ");

            //�폜�X�^�b�t�ł��A���׏�͌v��
            //newOrdersM.append(" and ms.delete_date IS NULL                                 ");
            newOrdersMonthProporsal.append(" and msc.delete_date IS NULL                                 ");
            newOrdersMonthProporsal.append(" and msh.delete_date IS NULL                                 ");

            //���̂݁i���[�g100�͑ΏۊO�j
            newOrdersMonthProporsal.append(" and dsp.rate <>100                                 ");

            newOrdersMonthProporsal.append("group by ");
            newOrdersMonthProporsal.append("  mc.course_class_id ");
            newOrdersMonthProporsal.append("  , mcc.course_class_name ");
            newOrdersMonthProporsal.append("  , dsd.product_division ");
            newOrdersMonthProporsal.append("  , dsd.discount_value ");
            newOrdersMonthProporsal.append("  , ms.staff_name1 ");
            newOrdersMonthProporsal.append("  , ms.shop_id ");
            newOrdersMonthProporsal.append("  , msc.staff_class_name ");
            newOrdersMonthProporsal.append("  , ds.customer_id ");
            newOrdersMonthProporsal.append("  , ms.staff_no ");
            newOrdersMonthProporsal.append("  , msh.shop_name ");
            newOrdersMonthProporsal.append("  , ds.sales_date , dsd.tax_rate");
            newOrdersMonthProporsal.append("  , ms.staff_name2 ");

            //���[�g�ŃO���[�s���O
            newOrdersMonthProporsal.append("  , dsp.rate ");

            newOrdersMonthProporsal.append("order by                                                     ");
            newOrdersMonthProporsal.append(" ms.staff_no;                                 ");

            SystemInfo.getLogger().info(newOrdersMonthProporsal.toString());
            rsSold = SystemInfo.getConnection().executeQuery(newOrdersMonthProporsal.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("course_class_id"));
                resMap.put("price", rsSold.getBigDecimal("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("staff_name2", rsSold.getString("staff_name2"));
                resMap.put("staff_class_name", rsSold.getString("staff_class_name"));
                resMap.put("shop_id", rsSold.getString("shop_id"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("staff_no", rsSold.getString("staff_no"));
                resMap.put("shop_name", rsSold.getString("shop_name"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
            //    SystemInfo.getLogger().info(resMap.toString());
                newOrdersListMonthProporsal.add(resMap);
            }

            rsSold.close();



          //�ǉ��Q �i���̂̈��j
            //�������㕨�̃f�[�^�擾
            performanceItemMonthProporsal.append("select                                                       ");
            //���i����ID
            performanceItemMonthProporsal.append(" mi.item_class_id ");
            //������z�i����Łj�ƁA�����[�g����
			// product_division in (1,3) then discount_value * (1 + get_tax_rate(tax_rate, sales_date))
            performanceItemMonthProporsal.append(" , case dsd.product_division when 4 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date)))) *(dsp.ratio * 0.01) )*-1   when 2 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) ) *(dsp.ratio * 0.01) end as price  ");

            //���i���ޖ�
            performanceItemMonthProporsal.append(" , mic.item_class_name ");
            //�����z
            performanceItemMonthProporsal.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            performanceItemMonthProporsal.append(" , dsd.product_division ");
            //�X�^�b�t��_�c��
            performanceItemMonthProporsal.append(" , ms.staff_name1 ");
            //�X�^�b�t��_���O
            performanceItemMonthProporsal.append(" , ms.staff_name2 ");
            //�X�܂h�c
            performanceItemMonthProporsal.append(" , ms.shop_id ");
            //�X�ܖ�
            performanceItemMonthProporsal.append(" , msh.shop_name ");
            //�X�^�b�t�敪��
            performanceItemMonthProporsal.append(" , msc.staff_class_name ");
            //�ڋq�h�c
            performanceItemMonthProporsal.append(" , ds.customer_id ");
            //�X�^�b�t�m�n
            performanceItemMonthProporsal.append(" , ms.staff_no ");
            //�̔���
            performanceItemMonthProporsal.append(" , ds.sales_date ");
            performanceItemMonthProporsal.append("from                                                         ");
            performanceItemMonthProporsal.append(" data_sales_detail as dsd                                    ");
            performanceItemMonthProporsal.append(" inner join data_sales as ds                                 ");
            performanceItemMonthProporsal.append("   on dsd.shop_id = ds.shop_id                               ");
            performanceItemMonthProporsal.append("   and dsd.slip_no = ds.slip_no                              ");

            //���e�[�u���Ɠ�������
            performanceItemMonthProporsal.append(" inner join data_sales_detail_proportionally as dsp                                 ");
            performanceItemMonthProporsal.append("   on dsd.shop_id = dsp.shop_id                               ");
            performanceItemMonthProporsal.append("   and dsd.slip_no = dsp.slip_no                              ");
            performanceItemMonthProporsal.append("   and dsd.slip_detail_no = dsp.slip_detail_no                              ");


            performanceItemMonthProporsal.append(" inner join mst_item as mi                                   ");
            performanceItemMonthProporsal.append("   on dsd.product_id = mi.item_id                            ");
            performanceItemMonthProporsal.append(" inner join mst_item_class mic                               ");
            performanceItemMonthProporsal.append("   on mi.item_class_id = mic.item_class_id                   ");
            performanceItemMonthProporsal.append(" left join mst_staff ms                                     ");

            //���e�[�u���̃X�^�b�tID
            performanceItemMonthProporsal.append("   on dsp.staff_id = ms.staff_id                             ");
            performanceItemMonthProporsal.append(" left join mst_staff_class msc                              ");
            performanceItemMonthProporsal.append("   on ms.staff_class_id = msc.staff_class_id                ");
            performanceItemMonthProporsal.append(" left join mst_shop msh                                     ");
            performanceItemMonthProporsal.append("   on ms.shop_id = msh.shop_id                               ");
            performanceItemMonthProporsal.append("where                                                        ");
            performanceItemMonthProporsal.append(" dsd.product_division in (2,4)                                  ");
            //��������
            performanceItemMonthProporsal.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            performanceItemMonthProporsal.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            performanceItemMonthProporsal.append(" and ds.shop_id = ").append(shopList);
            performanceItemMonthProporsal.append(" and dsd.delete_date IS NULL                                 ");
            performanceItemMonthProporsal.append(" and ds.delete_date IS NULL                                  ");
            performanceItemMonthProporsal.append(" and mi.delete_date IS NULL                                  ");
            performanceItemMonthProporsal.append(" and mic.delete_date IS NULL                                 ");
           //�폜�X�^�b�t�ł��A���׏�͌v��
            //performanceItemM.append(" and ms.delete_date IS NULL                                 ");
            performanceItemMonthProporsal.append(" and msc.delete_date IS NULL                                 ");
            performanceItemMonthProporsal.append(" and msh.delete_date IS NULL                                 ");

            //�l�����ȊO
            performanceItemMonthProporsal.append(" and dsd.discount_value = 0                                 ");

            //���̂�
            performanceItemMonthProporsal.append(" and dsp.ratio <> 100 ");

            performanceItemMonthProporsal.append("group by");
            performanceItemMonthProporsal.append("  mi.item_class_id ");
            performanceItemMonthProporsal.append("  , mic.item_class_name ");
            performanceItemMonthProporsal.append("  , dsd.product_division ");
            performanceItemMonthProporsal.append("  , dsd.discount_value ");
            performanceItemMonthProporsal.append("  , ms.staff_name1 ");
            performanceItemMonthProporsal.append("  , ms.shop_id ");
            performanceItemMonthProporsal.append("  , msc.staff_class_name ");
            performanceItemMonthProporsal.append("  , ds.customer_id ");
            performanceItemMonthProporsal.append("  , ms.staff_no ");
            performanceItemMonthProporsal.append("  , msh.shop_name ");
            performanceItemMonthProporsal.append("  , ds.sales_date , dsd.tax_rate ");
            performanceItemMonthProporsal.append("  , ms.staff_name2 ");
            performanceItemMonthProporsal.append("  , dsp.ratio ");


            performanceItemMonthProporsal.append("order by                                                     ");
            performanceItemMonthProporsal.append(" ms.staff_no;                                 ");

            SystemInfo.getLogger().info(performanceItemMonthProporsal.toString());
            rsSold = SystemInfo.getConnection().executeQuery(performanceItemMonthProporsal.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("item_class_id"));
                resMap.put("price", rsSold.getBigDecimal("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("staff_name2", rsSold.getString("staff_name2"));
                resMap.put("staff_class_name", rsSold.getString("staff_class_name"));
                resMap.put("shop_id", rsSold.getString("shop_id"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("staff_no", rsSold.getString("staff_no"));
                resMap.put("shop_name", rsSold.getString("shop_name"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
               // SystemInfo.getLogger().info(resMap.toString());
                performanceItemSaronListMonthProporsal.add(resMap);
            }
            rsSold.close();


            //�ǉ��R�F�Z�p��

             performanceSaronMonthProporsal.append("select                                                       ");
            //���i����ID
            performanceSaronMonthProporsal.append(" mt.technic_class_id ");

            //������z(�����) ���[�g����
            performanceSaronMonthProporsal.append(" , case dsd.product_division when 3 then  ceil( abs(    SUM(   dsd.product_num * dsd.product_value - dsd.discount_value    ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date)))) *(dsp.ratio * 0.01) )*-1   when 1 then   ceil(     SUM(  dsd.product_num * dsd.product_value - dsd.discount_value ) / (1+COALESCE(dsd.tax_rate,  get_tax_rate(ds.sales_date))) ) *(dsp.ratio * 0.01) end as price  ");

            //���i���ޖ�
            performanceSaronMonthProporsal.append(" , mtc.technic_class_name ");
            //�����z
            performanceSaronMonthProporsal.append(" , dsd.discount_value ");
            //�Z�p���i�敪�h�c
            performanceSaronMonthProporsal.append(" , dsd.product_division ");
            //�X�^�b�t��_�c��
            performanceSaronMonthProporsal.append(" , ms.staff_name1 ");
            //�X�^�b�t��_���O
            performanceSaronMonthProporsal.append(" , ms.staff_name2 ");
            //�X�܂h�c
            performanceSaronMonthProporsal.append(" , ms.shop_id ");
            //�X�ܖ�
            performanceSaronMonthProporsal.append(" , msh.shop_name ");
            //�X�^�b�t�敪��
            performanceSaronMonthProporsal.append(" , msc.staff_class_name ");
            //�ڋq�h�c
            performanceSaronMonthProporsal.append(" , ds.customer_id ");
            //�X�^�b�t�m�n
            performanceSaronMonthProporsal.append(" , ms.staff_no ");
            //�̔���
            performanceSaronMonthProporsal.append(" , ds.sales_date ");
            performanceSaronMonthProporsal.append("from                                                         ");
            performanceSaronMonthProporsal.append(" data_sales_detail as dsd                                    ");
            performanceSaronMonthProporsal.append(" inner join data_sales as ds                                 ");
            performanceSaronMonthProporsal.append("   on dsd.shop_id = ds.shop_id                               ");
            performanceSaronMonthProporsal.append("   and dsd.slip_no = ds.slip_no                              ");

            //����������
            performanceSaronMonthProporsal.append(" inner join data_sales_detail_proportionally as dsp                                 ");
            performanceSaronMonthProporsal.append("   on dsd.shop_id = dsp.shop_id                               ");
            performanceSaronMonthProporsal.append("   and dsd.slip_no = dsp.slip_no                              ");
            performanceSaronMonthProporsal.append("   and dsd.slip_detail_no = dsp.slip_detail_no                              ");

            performanceSaronMonthProporsal.append(" inner join mst_technic as mt                                   ");
            performanceSaronMonthProporsal.append("   on dsd.product_id = mt.technic_id                            ");
            performanceSaronMonthProporsal.append(" inner join mst_technic_class mtc                               ");
            performanceSaronMonthProporsal.append("   on mt.technic_class_id = mtc.technic_class_id                   ");
            performanceSaronMonthProporsal.append(" left join mst_staff ms                                     ");
            performanceSaronMonthProporsal.append("   on dsd.staff_id = ms.staff_id                             ");
            performanceSaronMonthProporsal.append(" left join mst_staff_class msc                              ");
            performanceSaronMonthProporsal.append("   on ms.staff_class_id = msc.staff_class_id                ");
            performanceSaronMonthProporsal.append(" left join mst_shop msh                                     ");
            performanceSaronMonthProporsal.append("   on ms.shop_id = msh.shop_id                               ");
            performanceSaronMonthProporsal.append("where                                                        ");
            performanceSaronMonthProporsal.append(" dsd.product_division in (1,3)                                   ");
            //��������
            performanceSaronMonthProporsal.append(" and ds.sales_date >= '").append(sdf.format(bef)).append("' ");
            //�{���܂�
            performanceSaronMonthProporsal.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            //���O�C�����[�U�[�̃V���b�vID
            performanceSaronMonthProporsal.append(" and ds.shop_id = ").append(shopList);
            performanceSaronMonthProporsal.append(" and dsd.delete_date IS NULL                                 ");
            performanceSaronMonthProporsal.append(" and ds.delete_date IS NULL                                  ");
            performanceSaronMonthProporsal.append(" and mt.delete_date IS NULL                                  ");
            performanceSaronMonthProporsal.append(" and mtc.delete_date IS NULL                                 ");

            //�폜�X�^�b�t�ł��A���׏�͌v��
            // performanceSaronM.append(" and ms.delete_date IS NULL                                  ");
            performanceSaronMonthProporsal.append(" and msc.delete_date IS NULL                                 ");
            performanceSaronMonthProporsal.append(" and msh.delete_date IS NULL                                 ");

            //���̂�
            performanceItemMonthProporsal.append(" and dsp.ratio <> 100 ");


            performanceSaronMonthProporsal.append("group by                                                     ");
            performanceSaronMonthProporsal.append("  mt.technic_class_id ");
            performanceSaronMonthProporsal.append("  , mtc.technic_class_name ");
            performanceSaronMonthProporsal.append("  , dsd.product_division ");
            performanceSaronMonthProporsal.append("  , dsd.discount_value ");
            performanceSaronMonthProporsal.append("  , ms.staff_name1 ");
            performanceSaronMonthProporsal.append("  , ms.shop_id ");
            performanceSaronMonthProporsal.append("  , msc.staff_class_name ");
            performanceSaronMonthProporsal.append("  , ds.customer_id ");
            performanceSaronMonthProporsal.append("  , ms.staff_no ");
            performanceSaronMonthProporsal.append("  , msh.shop_name ");
            performanceSaronMonthProporsal.append("  , ds.sales_date , dsd.tax_rate");
            performanceSaronMonthProporsal.append("  , ms.staff_name2 ");

            performanceSaronMonthProporsal.append("  , dsp.ratio ");

            performanceSaronMonthProporsal.append("order by                                                     ");
            performanceSaronMonthProporsal.append("  ms.staff_no;                                         ");

            SystemInfo.getLogger().info(performanceSaronMonthProporsal.toString());
            rsSold = SystemInfo.getConnection().executeQuery(performanceSaronMonthProporsal.toString());
            while (rsSold.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("class_id", rsSold.getInt("technic_class_id"));
                resMap.put("price", rsSold.getBigDecimal("price"));
                resMap.put("product_division", rsSold.getInt("product_division"));
                resMap.put("discount_value", rsSold.getInt("discount_value"));
                resMap.put("staff_name1", rsSold.getString("staff_name1"));
                resMap.put("staff_name2", rsSold.getString("staff_name2"));
                resMap.put("staff_class_name", rsSold.getString("staff_class_name"));
                resMap.put("shop_id", rsSold.getString("shop_id"));
                resMap.put("customer_id", rsSold.getInt("customer_id"));
                resMap.put("staff_no", rsSold.getString("staff_no"));
                resMap.put("shop_name", rsSold.getString("shop_name"));
                resMap.put("sales_date", rsSold.getDate("sales_date"));
               // SystemInfo.getLogger().info(resMap.toString());
                performanceItemSaronListMonthProporsal.add(resMap);
            }
            rsSold.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        JPOIApi jx = new JPOIApi("�ӔC�ҕ��͕\");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_analysis_responsibility.xls");
        int maxPage = (int) Math.ceil(resultList.size() / 19d);

        for (int x = maxPage; x > 1; x--) {
            jx.copySheet(0);
        }

        //------------------------------
        // ���x��
        //------------------------------
        int labelPointY = 13;//�����c��
        int labelPointX = 38;//��������
        int nextValueLine = 7;//���s�J�n�\�����ԍ�
        for (int i = 0; i < labelList.size(); i++) {
            Map<String, Object> nowRow = labelList.get(i);
            int display_seq = (Integer) nowRow.get("display_seq");
            if (display_seq == nextValueLine) {
                //�s���ǉ�
                labelPointY += 7;
                switch (nextValueLine) {
                    case 7://��s�ڏI��
                        nextValueLine = 3;
                        break;
                    case 3://��s�ڏI��
                        nextValueLine = 17;
                        break;
                    case 17://�O�s�ڏI��
                        nextValueLine = 31;
                        break;
                    default:
                        break;
                }
                //�o�̓Z�������ɖ߂�̂ň����Z
                labelPointX -= 56;
            }
            //���x���o��
            jx.setCellValue(labelPointX, labelPointY, (String) nowRow.get("class_name"));
            //�o�̓Z�����E�ɂ��炷
            labelPointX += 4;
        }

        //------------------------------
        // �w�b�_
        //------------------------------
        //���t
        jx.setCellValue(1, 2, nowDay);
        //�X�ܖ�
        jx.setCellValue(29, 2, shopName);
        //�ӔC��
        jx.setCellValue(48, 2, responsiblePerson);
        //�{�����p�l��
        jx.setCellValue(54, 5, toDayAmortization);
        //�����\��l��
        jx.setCellValue(54, 9, tomorrowReservationNum);



        //------------------------------
        // �{�f�B�[
        //------------------------------
        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>(0);

        //------------------------------------
        // ����Q�{�����сA�{���ԓ`�A�{�����ʁi�󒍐V�K�A�󒍓����j
        //------------------------------------
        double gedai = 0.5;
        int newOrders = 0;
        int manyOrders = 0;
        int newRedOrders = 0;
        int manyRedOrders = 0;
        int toMonthPerformanceSaron = 0;

        //�󒍐V�K�A�󒍓���
        for (int i = 0; i < toDayNewOrdersList.size(); i++) {
            Map<String, Object> nowRow = toDayNewOrdersList.get(i);
            int cId = (Integer) nowRow.get("customer_id");
            int key = (Integer) nowRow.get("product_division");
            String mapKey = String.valueOf(key);
            String mapCId = String.valueOf(cId);
            Map<String, Object> customerMap = checkMap.get(mapCId);
            Date oneMonth = (Date) customerMap.get("end_of_month");
            Date sales_date = (Date) nowRow.get("sales_date");
            Boolean flag = customerMap.containsKey(mapKey);


            if (sales_date.compareTo(oneMonth) < 1) {
                if (flag) {
                    if (key == 5) {
                        //�@���񗈓X�̌��ɍw��
                        newOrders += Math.ceil((Integer) nowRow.get("price") * gedai);
                    } else if (key == 8) {
                        //�@���񗈓X�̌��ɐԓ`
                        newRedOrders += Math.ceil((Integer) nowRow.get("price") * gedai);
                    }
                }
            } else if (key == 5) {
                //�@�����w���i�����ȍ~�̍w���j
                manyOrders += Math.ceil((Integer) nowRow.get("price") * gedai);
            } else if (key == 8) {
                //�@�����w���i�����ȍ~�̐ԓ`�j
                manyRedOrders += Math.ceil((Integer) nowRow.get("price") * gedai);
            }
        }
        //���㌋��EXCEL�o��
        //�{�����сQ�󒍐V�K
        jx.setCellValue(6, 7, newOrders);
        //�{���ԓ`�Q�󒍐V�K
        jx.setCellValue(20, 7, newRedOrders);
        //�{�����ʁQ�󒍐V�K
        jx.setCellValue(30, 7, newOrders + newRedOrders);
        //�{�����сQ�󒍓���
        jx.setCellValue(6, 8, manyOrders);
        //�{���ԓ`�Q�󒍓���
        jx.setCellValue(20, 8, manyRedOrders);
        //�{�����ʁQ�󒍓���
        jx.setCellValue(30, 8, manyOrders + manyRedOrders);

        //------------------------------------
        // ����Q�{�����сA�{���ԓ`�A�{������(���㕨�́A����T����)
        //------------------------------------
        int toDayPerformanceItem = 0;
        int toDayPerformanceSaron = 0;
        int toDayCreditItem = 0;
        int toDayCreditSaron = 0;
        int saronItemFlag = 0; //0�F���㕨�́@1�F����T�����@2�F�t���[����i�v�Z����Ȃ����߁j 3�F������i�����̌v�Z�ɂ���邽�߁j
        int nowProductDivision = 0;

        for (int i = 0; i < toDayPerformanceItemSaronList.size(); i++) {
            Map<String, Object> nowRow = toDayPerformanceItemSaronList.get(i);
            if (null != (Integer) nowRow.get("class_id")) {
                nowProductDivision = (Integer) nowRow.get("product_division");
                saronItemFlag = 0;

                if (nowProductDivision == 1 || nowProductDivision == 3) {
                        switch ((Integer) nowRow.get("class_id")) {
                            case 2://2          �t���[���
                                saronItemFlag = 2;
                                break;
                            case 3://3          �����
                                saronItemFlag = 3;
                                break;

                            case 1://�̌�
                               // saronItemFlag = 1;
                               continue;
                               //�̌��ƃC���I�[���́A���Z�ΏۊO

                            case 15://�C���I�[��
                                //�̌��ƃC���I�[���́A���Z�ΏۊO
                               // saronItemFlag = 1;
                               continue;

                            default:
                                break;
                        }
                    //����
                    } else if (nowProductDivision == 2 || nowProductDivision == 4) {


                        switch ((Integer) nowRow.get("class_id")) {
                            case 14://14	EMS�p�b�h
                            case 22://22	�K�E��
                            case 65://65	�Ј��@�X�L���P�A
                            case 66://66	�Ј��@�����p���ϕi
                            case 67://67	�Ј��@���C�N
                            case 68://68	�Ј��@�w�A�E�{�f�B�E�t�b�g�P�A
                            case 69://69	�Ј��@�T�v�������g
                            case 70://70	�Ј��@���e�퓙
                            case 71://71	�Ј��@����
                            case 72://72	�Ј��@�C���I�[��
                            case 73://73	�Ј��@�A�C���b�V���֘A
                            case 74://74	�Ј��@���̑�
                            case 75://75	�Ј��@�T�|�[�^�[
                            case 78://78	�Ј��@���s�^
                                saronItemFlag = 1;
                                break;
                            default:
                                break;
                        }
                    }

                //�{�����т��A�{���ԓ`���𔻒f����
                switch (saronItemFlag) {
                    case 0:

                        //�T�����A�C�e������Ȃ��ꍇ�A�S�����T��������Ȃ��ꍇ�A���Z�Ώ�
                        if (null != (String) nowRow.get("staff_name1") && !"�T����".equals((String) nowRow.get("staff_name1"))){

                        if (nowProductDivision == 1) {
                            //20190129 �C��
							if ((Integer) nowRow.get("class_id") == 2) {
								toDayPerformanceItem += Math.ceil((Integer) nowRow.get("price") * gedai);
							}
                        } else if ( nowProductDivision == 2) {
								toDayPerformanceItem += Math.ceil((Integer) nowRow.get("price") * gedai);
						} else {
                            toDayCreditItem += Math.ceil((Integer) nowRow.get("price") * gedai);
                        }
                        break;
                        }
                        break;
                    //�Ј�����,�K�E���AEMS�p�b�h
                    case 1:
                        if (nowProductDivision == 1 || nowProductDivision == 2) {

                            toDayPerformanceSaron += Integer.parseInt(String.valueOf((int)Math.ceil((Integer) nowRow.get("price") * gedai)));
                        //    SystemInfo.getLogger().info(String.valueOf(toDayPerformanceSaron));
                        } else {
                            toDayCreditSaron += Math.ceil((Integer) nowRow.get("price") * gedai);
                        }
                        break;


                    //�t���[�̏ꍇ
                    case 2:
                        if (nowProductDivision == 1 || nowProductDivision == 2) {

                            //���גS�����T�����̏ꍇ�́A�T��������
                            if (null != (String) nowRow.get("staff_name1") && "�T����".equals((String) nowRow.get("staff_name1"))){
                             toDayPerformanceSaron += Integer.parseInt(String.valueOf((int)Math.ceil((Integer) nowRow.get("price") * gedai)));
                            }else{
                            //���גS�����T��������Ȃ��ꍇ�A���̔���
                                toDayPerformanceItem += Math.ceil((Integer) nowRow.get("price") * gedai);

                            }
                        }
                        break;

                    case 3:
                        if (nowProductDivision == 1 || nowProductDivision == 2) {
                            // 2018/11/1 FIX ������́A����A���̔���Ɋ܂܂Ȃ�
                            //toDayPerformanceItem += Math.ceil((Integer)nowRow.get("price") * gedai);

                            toDayPerformanceSaron += (int)Math.ceil((Integer) nowRow.get("price") * gedai);

                        } else {
                            // 2018/11/1 FIX ������́A����A���̔���Ɋ܂܂Ȃ�
                            //toDayCreditItem += Math.ceil((Integer)nowRow.get("price") * gedai);
                            toDayCreditSaron += Math.ceil((Integer) nowRow.get("price") * gedai);
                        }
                        break;
                    default:
                        break;
                }
            }
        }


        //�T�������ϕi�Q�{�������50%�����Z
        if (0 < saronItemDiscountSoldList.size()) {
            Map<String, Object> saronCosmetics = saronItemDiscountSoldList.get(0);
            if (null != saronCosmetics.get("product_num")) {
                 toDayPerformanceSaron += (int)Math.ceil((Integer) saronCosmetics.get("price") * gedai);
            }
        }


        //���㌋��EXCEL�o��
        //�{�����сQ���v
        jx.setCellValue(6, 6, toDayPerformanceItem + toDayPerformanceSaron + newOrders + manyOrders);
        //�{���ԓ`�Q���v
        jx.setCellValue(20, 6, toDayCreditItem + toDayCreditSaron + newRedOrders + manyRedOrders);
        //�{�����ʁQ���v
        jx.setCellValue(30, 6, toDayPerformanceItem + toDayCreditItem + toDayPerformanceSaron + toDayCreditSaron + newOrders + manyOrders + newRedOrders + manyRedOrders);
        //�{�����сQ���㕨��
        jx.setCellValue(6, 9, toDayPerformanceItem);
        //�{���ԓ`�Q���㕨��
        jx.setCellValue(20, 9, toDayCreditItem);
        //�{�����ʁQ���㕨��
        jx.setCellValue(30, 9, toDayPerformanceItem + toDayCreditItem);
        //�{�����сQ����T����
        jx.setCellValue(6, 10, toDayPerformanceSaron);

        //�{���ԓ`�Q����T����
        jx.setCellValue(20, 10, toDayCreditSaron);
        //�{�����ʁQ����T����
        jx.setCellValue(30, 10, toDayPerformanceSaron + toDayCreditSaron);

        //------------------------------
        // ����T����
        //------------------------------
        int staffPurchasePrice;
        int staffPurchaseNum;
        boolean staffPurchaseFlag = false;

        //����T����_�Z�p_�{������
        //�擾�������ナ�X�g�̋��z�Ɛ��ʂ̌v�Z
        //�������F���㌋�ʂ̃��X�g
        //�������F�Ή����锄��̋Z�p���i�敪�h�c
        resultMap = setZyodaiExcelSheetSaron(saronSoldList, 1, "technic_class_id");
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("newItemClassId")) {
                switch ((Integer) nowRow.get("newItemClassId")) {
                    case 1://�̌�
                        jx.setCellValue(6, 14, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(6, 15, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    case 15://�C���I�[��
                        jx.setCellValue(10, 14, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(10, 15, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    case 3://�����
                        jx.setCellValue(14, 14, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(14, 15, (Integer) nowRow.get("couresDatePrice"));

                        break;
                    case 2://�t���[���
                        jx.setCellValue(18, 14, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(18, 15, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    default:
                        break;
                }
            }
        }

        //�t���[����
        resultMap = setZyodaiExcelSheetSaron(saronSoldFreeList, 1, "technic_class_id");
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("newItemClassId")) {
                switch ((Integer) nowRow.get("newItemClassId")) {

                    case 2://�t���[���
                        jx.setCellValue(58, 42, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(58, 43, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    default:
                        break;
                }
            }
        }

        //����T����_�Z�p_��������
        resultMap = setZyodaiExcelSheetSaron(saronSoldListM, 1, "technic_class_id");
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("newItemClassId")) {
                switch ((Integer) nowRow.get("newItemClassId")) {
                    case 1://�̌�
                        jx.setCellValue(6, 16, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(6, 17, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    case 15://�C���I�[��
                        jx.setCellValue(10, 16, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(10, 17, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    case 3://�����
                        jx.setCellValue(14, 16, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(14, 17, (Integer) nowRow.get("couresDatePrice"));
                        //���ԗ݌v���Z
                        toMonthPerformanceSaron += (Integer) nowRow.get("couresDatePrice");

                        break;
                    case 2://�t���[���
                        jx.setCellValue(18, 16, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(18, 17, (Integer) nowRow.get("couresDatePrice"));
                        //���ԗ݌v���Z
                        toMonthPerformanceSaron += (Integer) nowRow.get("couresDatePrice");


                        break;
                    default:
                        break;
                }
            }
        }


        //�t���[����
        resultMap = setZyodaiExcelSheetSaron(saronSoldFreeListM, 1, "technic_class_id");
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("newItemClassId")) {
                switch ((Integer) nowRow.get("newItemClassId")) {

                    case 2://�t���[���
                        jx.setCellValue(58, 44, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(58, 45, (Integer) nowRow.get("couresDatePrice"));

                        break;
                    default:
                        break;
                }
            }
        }

        //����T����_���i_�{������
        staffPurchasePrice = 0;
        staffPurchaseNum = 0;

        resultMap = setZyodaiExcelSheetSaron(saronItemSoldList, 2, "item_class_id");
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("newItemClassId")) {
                switch ((Integer) nowRow.get("newItemClassId")) {
                    case 14:
                        jx.setCellValue(22, 14, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(22, 15, (Integer) nowRow.get("couresDatePrice"));

                        break;
                    case 22:
                        jx.setCellValue(26, 14, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(26, 15, (Integer) nowRow.get("couresDatePrice"));
                        break;
                    case 65://65	�Ј��@�X�L���P�A
                    case 66://66	�Ј��@�����p���ϕi
                    case 67://67	�Ј��@���C�N
                    case 68://68	�Ј��@�w�A�E�{�f�B�E�t�b�g�P�A
                    case 69://69	�Ј��@�T�v�������g
                    case 70://70	�Ј��@���e�퓙
                    case 71://71	�Ј��@����
                    case 72://72	�Ј��@�C���I�[��
                    case 73://73	�Ј��@�A�C���b�V���֘A
                    case 74://74	�Ј��@���̑�
                    case 75://75	�Ј��@�T�|�[�^�[
                    case 78://78	�Ј��@���s�^
                        staffPurchasePrice += (Integer) nowRow.get("couresDatePrice");
                        staffPurchaseNum += (Integer) nowRow.get("couresDateNum");
                        staffPurchaseFlag = true;
                        break;
                    default:
                        break;
                }
            }
        }
        if (staffPurchaseFlag) {
            jx.setCellValue(34, 14, staffPurchaseNum);
            jx.setCellValue(34, 15, staffPurchasePrice);
            staffPurchaseFlag = false;
        }

        //����T����_���i_��������
        staffPurchasePrice = 0;
        staffPurchaseNum = 0;

        resultMap = setZyodaiExcelSheetSaron(saronItemSoldListM, 2, "item_class_id");
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("newItemClassId")) {
                switch ((Integer) nowRow.get("newItemClassId")) {
                    case 14:
                        jx.setCellValue(22, 16, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(22, 17, (Integer) nowRow.get("couresDatePrice"));
                        //���ԗ݌v���Z�i�K�E���j
                        toMonthPerformanceSaron += (Integer) nowRow.get("couresDatePrice");

                        break;
                    case 22:
                        jx.setCellValue(26, 16, (Integer) nowRow.get("couresDateNum"));
                        jx.setCellValue(26, 17, (Integer) nowRow.get("couresDatePrice"));
                        //���ԗ݌v���Z�iEMS�j
                        toMonthPerformanceSaron += (Integer) nowRow.get("couresDatePrice");

                        break;
                    case 65://65	�Ј��@�X�L���P�A
                    case 66://66	�Ј��@�����p���ϕi
                    case 67://67	�Ј��@���C�N
                    case 68://68	�Ј��@�w�A�E�{�f�B�E�t�b�g�P�A
                    case 69://69	�Ј��@�T�v�������g
                    case 70://70	�Ј��@���e�퓙
                    case 71://71	�Ј��@����
                    case 72://72	�Ј��@�C���I�[��
                    case 73://73	�Ј��@�A�C���b�V���֘A
                    case 74://74	�Ј��@���̑�
                    case 75://75	�Ј��@�T�|�[�^�[
                    case 78://78	�Ј��@���s�^
                        staffPurchasePrice += (Integer) nowRow.get("couresDatePrice");
                        staffPurchaseNum += (Integer) nowRow.get("couresDateNum");
                        staffPurchaseFlag = true;
                        break;
                    default:
                        break;
                }
            }
        }
        if (staffPurchaseFlag) {
            jx.setCellValue(34, 16, staffPurchaseNum);
            jx.setCellValue(34, 17, staffPurchasePrice);
             //���ԗ݌v���Z�i�Ј��j
             toMonthPerformanceSaron += staffPurchasePrice;

            staffPurchaseFlag = false;
        }

        //�T�������ϕi�Q�{������
        if (0 < saronItemDiscountSoldList.size()) {
            Map<String, Object> saronCosmetics = saronItemDiscountSoldList.get(0);
            if (null != saronCosmetics.get("product_num")) {
                jx.setCellValue(30, 14, (Integer) saronCosmetics.get("product_num"));
                jx.setCellValue(30, 15, (Integer) saronCosmetics.get("price"));
            }
        }
        //�T�������ϕi�Q��������
        if (0 < saronItemDiscountSoldListM.size()) {
            Map<String, Object> saronCosmeticsM = saronItemDiscountSoldListM.get(0);
            if (null != saronCosmeticsM.get("product_num")) {
                jx.setCellValue(30, 16, (Integer) saronCosmeticsM.get("product_num"));
                jx.setCellValue(30, 17, (Integer) saronCosmeticsM.get("price"));
             //���ԗ݌v���Z�i�T�������ϕi�j
             toMonthPerformanceSaron += (Integer) saronCosmeticsM.get("price");
            }
        }

        //------------------------------
        // �󒍖𖱔���
        //------------------------------
        //�{������
        resultMap = new ArrayList<Map<String, Object>>(0);
        //�擾�������ナ�X�g�̋��z�Ɛ��ʂ̌v�Z
        //�������F���㌋�ʂ̃��X�g
        //�������F�Ή����锄��̃R�[�X�敪�h�c
        //��O�����FEXCEL�̊J�n�c��
        //��l�����FEXCEL�̊J�n����
        //��܈����F���i�̕\����
        resultMap = setZyodaiExcelSheet(corseSoldList, 5, 14, 38, 1);
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("display_seq")) {
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis"), (Integer) nowRow.get("couresDateNum"));
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis") + 1, (Integer) nowRow.get("couresDatePrice"));
            }
        }

        //�����݌v
        resultMap = new ArrayList<Map<String, Object>>(0);
        //�擾�������ナ�X�g�̋��z�Ɛ��ʂ̌v�Z
        resultMap = setZyodaiExcelSheet(corseSoldListM, 5, 16, 38, 1);

        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("display_seq")) {
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis"), (Integer) nowRow.get("couresDateNum"));
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis") + 1, (Integer) nowRow.get("couresDatePrice"));
            }
        }

        //------------------------------
        // ���㕨��
        //------------------------------
        //�{������
        resultMap = new ArrayList<Map<String, Object>>(0);
        //�擾�������ナ�X�g�̋��z�Ɛ��ʂ̌v�Z
        resultMap = setZyodaiExcelSheet(itemSoldList, 2, 28, 6, 3);


        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("display_seq")) {
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis"), (Integer) nowRow.get("couresDateNum"));
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis") + 1, (Integer) nowRow.get("couresDatePrice"));
            }
        }



        //�����݌v
        resultMap = new ArrayList<Map<String, Object>>(0);
        //�擾�������ナ�X�g�̋��z�Ɛ��ʂ̌v�Z
        resultMap = setZyodaiExcelSheet(itemSoldListM, 2, 30, 6, 3);

        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> nowRow = resultMap.get(i);
            if (null != (Integer) nowRow.get("display_seq")) {
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis"), (Integer) nowRow.get("couresDateNum"));
                jx.setCellValue((Integer) nowRow.get("xAxis"), (Integer) nowRow.get("yAxis") + 1, (Integer) nowRow.get("couresDatePrice"));
            }
        }

        //------------------------------------------------------
        // ����Q�󒍐V�K�A�󒍓����A�V�K���́A��������(�������Q������)
        //------------------------------------------------------
        saronItemFlag = 0; //0�F���㕨�́@1�F����T�����@2�F�t���[����i�v�Z����Ȃ����߁j 3�F������i�����̌v�Z�ɂ���邽�߁j
        nowProductDivision = 0;
        int MonthRedOrders = 0;
        int toMonthPerformanceItem = 0;
        int toMonthPerformanceItemNaibu = 0;

        //���́A�T�����S���ҕʐU�蕪��
        for (int i = 0; i < performanceItemSaronListM.size(); i++) {
            Map<String, Object> nowRow = performanceItemSaronListM.get(i);
            Map<String,
                    Object> monthSalesItemSaron = new HashMap<String, Object>();
            int cId = (Integer) nowRow.get("customer_id");
            int key = (Integer) nowRow.get("product_division");
            String mapKey = String.valueOf(key);
            String mapCId = String.valueOf(cId);
            Map<String, Object> customerMap = checkMap.get(mapCId);
            Date oneMonth = (Date) customerMap.get("end_of_month");
            Date sales_date = (Date) nowRow.get("sales_date");
            Boolean flag = customerMap.containsKey(mapKey);

            if (null != (Integer) nowRow.get("class_id")) {
                nowProductDivision = (Integer) nowRow.get("product_division");
                saronItemFlag = 0;
                toMonthPerformanceItem = 0;
                toMonthPerformanceItemNaibu = 0;
                MonthRedOrders = 0;

                //���㕨�̂�����T�������̔��f���s��
                //�����Ŕ��肷��ƁA0�ɂȂ�ꍇ����i"�T����"�j�ƁA�l�����Ȃ�
              //  if (null != (String) nowRow.get("staff_name1") && !"�T����".equals((String) nowRow.get("staff_name1")) && (Integer) nowRow.get("discount_value") == 0) {
                    if (nowProductDivision == 1 || nowProductDivision == 3) {
                        switch ((Integer) nowRow.get("class_id")) {
                            case 2://2          �t���[���
                                saronItemFlag = 2;
                                break;
                            case 3://3          �����
                                saronItemFlag = 3;
                                break;
                          case 1://�̌�
                               // saronItemFlag = 1;
                               continue;
                               //�̌��ƃC���I�[���́A���Z�ΏۊO

                            case 15://�C���I�[��
                                //�̌��ƃC���I�[���́A���Z�ΏۊO
                               // saronItemFlag = 1;
                               continue;

                            default:
                                break;
                        }
                    } else if (nowProductDivision == 2 || nowProductDivision == 4) {
                        switch ((Integer) nowRow.get("class_id")) {
                            case 14://14	�K�E��
                            case 22://22	EMS
                            case 65://65	�Ј��@�X�L���P�A
                            case 66://66	�Ј��@�����p���ϕi
                            case 67://67	�Ј��@���C�N
                            case 68://68	�Ј��@�w�A�E�{�f�B�E�t�b�g�P�A
                            case 69://69	�Ј��@�T�v�������g
                            case 70://70	�Ј��@���e�퓙
                            case 71://71	�Ј��@����
                            case 72://72	�Ј��@�C���I�[��
                            case 73://73	�Ј��@�A�C���b�V���֘A
                            case 74://74	�Ј��@���̑�
                            case 75://75	�Ј��@�T�|�[�^�[
                            case 78://78	�Ј��@���s�^
                                saronItemFlag = 1;
                                break;
                            default:
                                break;
                        }
                    }
             //   } else {
             //       saronItemFlag = 1;
             //   }

                //�������т��A�����ԓ`������T�����Ȃ̂��𔻒f����
                switch (saronItemFlag) {
                    case 0:

                        if (null != (String) nowRow.get("staff_name1") && !"�T����".equals((String) nowRow.get("staff_name1"))){


                        if (nowProductDivision == 1 || nowProductDivision == 2) {

                            if (sales_date.compareTo(oneMonth) < 1 && flag) {
                                toMonthPerformanceItem = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                            } else {
                                toMonthPerformanceItemNaibu = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                            }
                        } else {
                            MonthRedOrders = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                        }
                        }

                        break;
                    case 1:
                        //�T��������i�݌v�j�̎Z�o�s�� �e
                        //toMonthPerformanceSaron += Math.ceil((Integer) nowRow.get("price") * gedai);
                        break;


                    //�l���т́A�t���[������A�i�V�K���́E�������́j�ɔ��f������B
                    case 2:
                        if (nowProductDivision == 1 || nowProductDivision == 2) {
                            if (sales_date.compareTo(oneMonth) < 1 && flag) {

                                toMonthPerformanceItem = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                            } else {


                                toMonthPerformanceItemNaibu = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                            }
                        } else {
                            MonthRedOrders = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                        }
                        break;

                    case 3:

                        //�T�������㗓�̉��Z�s���i�eEXCEL�o�͒l�̍��Z�ŎZ�o�j
                        //  toMonthPerformanceSaron += Math.ceil((Integer) nowRow.get("price") * gedai);
                        if (nowProductDivision == 1 || nowProductDivision == 2) {

                            if (sales_date.compareTo(oneMonth) < 1 && flag) {
                                //�V�K����
                                toMonthPerformanceItem = (int) Math.ceil((Integer) nowRow.get("price") * gedai);

                            } else {
                                //��������
                                toMonthPerformanceItemNaibu = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                            }
                        } else {
                            MonthRedOrders = (int) Math.ceil((Integer) nowRow.get("price") * gedai);
                        }
                        break;
                    default:
                        break;
                }

                //���ʂ̃Z�b�g
                monthSalesItemSaron.put("staff_name1", (String) nowRow.get("staff_name1"));
                monthSalesItemSaron.put("staff_name2", (String) nowRow.get("staff_name2"));
                monthSalesItemSaron.put("staff_class_name", (String) nowRow.get("staff_class_name"));
                monthSalesItemSaron.put("shop_name", (String) nowRow.get("shop_name"));

                String staff_no = String.valueOf( nowRow.get("staff_no"));

                if (nowRow.get("shop_id") != null) {
                    //���X�܁A���X�܃X�^�b�t�ɕ�����
                    if (nowRow.get("shop_id").equals(shopList)) {
                        if (monthSalesInternal.containsKey(staff_no)) {
                            monthSalesItemSaron.put("newMonthOrders", (int) monthSalesInternal.get(staff_no).get("newMonthOrders"));
                            monthSalesItemSaron.put("manyMonthOrders", (int) monthSalesInternal.get(staff_no).get("manyMonthOrders"));
                            monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders + (int) monthSalesInternal.get(staff_no).get("MonthRedOrders"));
                            monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem + (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItem"));
                            monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu + (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItemNaibu"));
                            monthSalesInternal.put( String.valueOf(nowRow.get("staff_no")), monthSalesItemSaron);
                        } else {
                            monthSalesItemSaron.put("newMonthOrders", 0);
                            monthSalesItemSaron.put("manyMonthOrders", 0);
                            monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders);
                            monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem);
                            monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu);
                            monthSalesInternal.put(String.valueOf(nowRow.get("staff_no")), monthSalesItemSaron);
                        }
                    } else if (monthSalesOutside.containsKey(staff_no)) {
                        monthSalesItemSaron.put("newMonthOrders", (int) monthSalesOutside.get(staff_no).get("newMonthOrders"));
                        monthSalesItemSaron.put("manyMonthOrders", (int) monthSalesOutside.get(staff_no).get("manyMonthOrders"));
                        monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders + (int) monthSalesOutside.get(staff_no).get("MonthRedOrders"));
                        monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem + (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItem"));
                        monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu + (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItemNaibu"));
                        monthSalesOutside.put(String.valueOf( nowRow.get("staff_no")), monthSalesItemSaron);
                    } else {
                        monthSalesItemSaron.put("newMonthOrders", 0);
                        monthSalesItemSaron.put("manyMonthOrders", 0);
                        monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders);
                        monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem);
                        monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu);
                        monthSalesOutside.put(String.valueOf( nowRow.get("staff_no")), monthSalesItemSaron);
                    }
                }
            }
        }

        //�𖱍�������Q�S���ҕʐU�蕪��
        for (int i = 0; i < newOrdersListM.size(); i++) {
            Map<String, Object> nowRow = newOrdersListM.get(i);
            Map<String, Object> monthSales = new HashMap<String, Object>();
            int cId = (Integer) nowRow.get("customer_id");
            int key = (Integer) nowRow.get("product_division");
            String mapKey = String.valueOf(key);
            String mapcId = String.valueOf(cId);
            Map<String, Object> customerMap = checkMap.get(mapcId);
            Date oneMonth = (Date) customerMap.get("end_of_month");
            Date sales_date = (Date) nowRow.get("sales_date");
            Boolean flag = customerMap.containsKey(mapKey);
            int newMonthOrders = 0;
            int manyMonthOrders = 0;
            MonthRedOrders = 0;

            if (sales_date.compareTo(oneMonth) < 1) {
                if (flag) {
                    if (key == 5) {
                        //�@���񗈓X�̌��ɍw��
                        newMonthOrders += (Integer) nowRow.get("price") * gedai;
                    } else if (key == 8) {
                        //�@�ԓ`
                        MonthRedOrders += (Integer) nowRow.get("price") * gedai;
                    }
                }
            } else if (key == 5) {
                //�@�����w���i�����ȍ~�̍w���j
                manyMonthOrders += (Integer) nowRow.get("price") * gedai;
            } else if (key == 8) {
                //�@�ԓ`
                MonthRedOrders += (Integer) nowRow.get("price") * gedai;
            }

            monthSales.put("staff_name1", (String) nowRow.get("staff_name1"));
            monthSales.put("staff_name2", (String) nowRow.get("staff_name2"));
            monthSales.put("staff_class_name", (String) nowRow.get("staff_class_name"));
            monthSales.put("shop_name", (String) nowRow.get("shop_name"));

            String staff_no = String.valueOf(nowRow.get("staff_no"));

            if (nowRow.get("shop_id") != null) {
                //���X�܁A���X�܃X�^�b�t�ɕ�����
                if (nowRow.get("shop_id").equals(shopList)) {
                    if (monthSalesInternal.containsKey(staff_no)) {
                        monthSales.put("newMonthOrders", newMonthOrders + (int) monthSalesInternal.get(staff_no).get("newMonthOrders"));
                        monthSales.put("manyMonthOrders", manyMonthOrders + (int) monthSalesInternal.get(staff_no).get("manyMonthOrders"));
                        monthSales.put("MonthRedOrders", MonthRedOrders + (int) monthSalesInternal.get(staff_no).get("MonthRedOrders"));
                        monthSales.put("toMonthPerformanceItem", (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItem"));
                        monthSales.put("toMonthPerformanceItemNaibu", (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItemNaibu"));
                        monthSalesInternal.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                    } else {
                        monthSales.put("newMonthOrders", newMonthOrders);
                        monthSales.put("manyMonthOrders", manyMonthOrders);
                        monthSales.put("MonthRedOrders", MonthRedOrders);
                        monthSales.put("toMonthPerformanceItem", 0);
                        monthSales.put("toMonthPerformanceItemNaibu", 0);
                        monthSalesInternal.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                    }
                } else if (monthSalesOutside.containsKey(nowRow.get("staff_no"))) {
                    monthSales.put("newMonthOrders", newMonthOrders + (int) monthSalesOutside.get(staff_no).get("newMonthOrders"));
                    monthSales.put("manyMonthOrders", manyMonthOrders + (int) monthSalesOutside.get(staff_no).get("manyMonthOrders"));
                    monthSales.put("MonthRedOrders", MonthRedOrders + (int) monthSalesOutside.get(staff_no).get("MonthRedOrders"));
                    monthSales.put("toMonthPerformanceItem", (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItem"));
                    monthSales.put("toMonthPerformanceItemNaibu", (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItemNaibu"));
                    monthSalesOutside.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                } else {
                    monthSales.put("newMonthOrders", newMonthOrders);
                    monthSales.put("manyMonthOrders", manyMonthOrders);
                    monthSales.put("MonthRedOrders", MonthRedOrders);
                    monthSales.put("toMonthPerformanceItem", 0);
                    monthSales.put("toMonthPerformanceItemNaibu", 0);
                    monthSalesOutside.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                }
            }
        }


       //�ǉ� �S���킯 ���̂ƃe�N�j�b�N�@��
        for (int i = 0; i < performanceItemSaronListMonthProporsal.size(); i++) {
            Map<String, Object> nowRow = performanceItemSaronListMonthProporsal.get(i);

            Map<String,
                    Object> monthSalesItemSaron = new HashMap<String, Object>();
            int cId = (Integer) nowRow.get("customer_id");
            int key = (Integer) nowRow.get("product_division");
            String mapKey = String.valueOf(key);
            String mapCId = String.valueOf(cId);
            Map<String, Object> customerMap = checkMap.get(mapCId);
            Date oneMonth = (Date) customerMap.get("end_of_month");
            Date sales_date = (Date) nowRow.get("sales_date");
            Boolean flag = customerMap.containsKey(mapKey);

            if (null != (Integer) nowRow.get("class_id")) {
                nowProductDivision = (Integer) nowRow.get("product_division");
                saronItemFlag = 0;
                toMonthPerformanceItem = 0;
                toMonthPerformanceItemNaibu = 0;
                MonthRedOrders = 0;

                //���㕨�̂�����T�������̔��f���s��
                //�����Ŕ��肷��ƁA0�ɂȂ�ꍇ����i"�T����"�j�ƁA�l�����Ȃ�
              //  if (null != (String) nowRow.get("staff_name1") && !"�T����".equals((String) nowRow.get("staff_name1")) && (Integer) nowRow.get("discount_value") == 0) {
                    if (nowProductDivision == 1 || nowProductDivision == 3) {
                        switch ((Integer) nowRow.get("class_id")) {
                            case 2://2          �t���[���
                                saronItemFlag = 2;
                                break;
                            case 3://3          �����
                                saronItemFlag = 3;
                                break;
                          case 1://�̌�
                               // saronItemFlag = 1;
                               continue;
                               //�̌��ƃC���I�[���́A���Z�ΏۊO

                            case 15://�C���I�[��
                                //�̌��ƃC���I�[���́A���Z�ΏۊO
                               // saronItemFlag = 1;
                               continue;

                            default:
                                break;
                        }
                    } else if (nowProductDivision == 2 || nowProductDivision == 4) {
                        switch ((Integer) nowRow.get("class_id")) {
                            case 14://14	�K�E��
                            case 22://22	EMS
                            case 65://65	�Ј��@�X�L���P�A
                            case 66://66	�Ј��@�����p���ϕi
                            case 67://67	�Ј��@���C�N
                            case 68://68	�Ј��@�w�A�E�{�f�B�E�t�b�g�P�A
                            case 69://69	�Ј��@�T�v�������g
                            case 70://70	�Ј��@���e�퓙
                            case 71://71	�Ј��@����
                            case 72://72	�Ј��@�C���I�[��
                            case 73://73	�Ј��@�A�C���b�V���֘A
                            case 74://74	�Ј��@���̑�
                            case 75://75	�Ј��@�T�|�[�^�[
                            case 78://78	�Ј��@���s�^
                                saronItemFlag = 1;
                                break;
                            default:
                                break;
                        }
                    }
             //   } else {
             //       saronItemFlag = 1;
             //   }

                //�������т��A�����ԓ`������T�����Ȃ̂��𔻒f����
                switch (saronItemFlag) {
                    case 0:

                        if (null != (String) nowRow.get("staff_name1") && !"�T����".equals((String) nowRow.get("staff_name1"))){


                        if (nowProductDivision == 1 || nowProductDivision == 2) {

                            if (sales_date.compareTo(oneMonth) < 1 && flag) {


                                toMonthPerformanceItem = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                            } else {
                                toMonthPerformanceItemNaibu = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                            }
                        } else {
                            MonthRedOrders = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                        }
                        }

                        break;
                    case 1:
                        //�T��������i�݌v�j�̎Z�o�s�� �e
                        //toMonthPerformanceSaron += Math.ceil((Integer) nowRow.get("price") * gedai);
                        break;


                    //�l���т́A�t���[������A�i�V�K���́E�������́j�ɔ��f������B
                    case 2:
                        if (nowProductDivision == 1 || nowProductDivision == 2) {
                            if (sales_date.compareTo(oneMonth) < 1 && flag) {

                                toMonthPerformanceItem = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                            } else {


                                toMonthPerformanceItemNaibu = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                            }
                        } else {
                            MonthRedOrders = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                        }
                        break;

                    case 3:

                        //�T�������㗓�̉��Z�s���i�eEXCEL�o�͒l�̍��Z�ŎZ�o�j
                        //  toMonthPerformanceSaron += Math.ceil((Integer) nowRow.get("price") * gedai);
                        if (nowProductDivision == 1 || nowProductDivision == 2) {

                            if (sales_date.compareTo(oneMonth) < 1 && flag) {
                                //�V�K����
                                toMonthPerformanceItem = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);

                            } else {
                                //��������
                                toMonthPerformanceItemNaibu = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                            }
                        } else {
                            MonthRedOrders = (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                        }
                        break;
                    default:
                        break;
                }

                //���ʂ̃Z�b�g
                monthSalesItemSaron.put("staff_name1", (String) nowRow.get("staff_name1"));
                monthSalesItemSaron.put("staff_name2", (String) nowRow.get("staff_name2"));
                monthSalesItemSaron.put("staff_class_name", (String) nowRow.get("staff_class_name"));
                monthSalesItemSaron.put("shop_name", (String) nowRow.get("shop_name"));

                String staff_no = String.valueOf( nowRow.get("staff_no"));

                if (nowRow.get("shop_id") != null) {
                    //���X�܁A���X�܃X�^�b�t�ɕ�����
                    if (nowRow.get("shop_id").equals(shopList)) {
                        if (monthSalesInternal.containsKey(staff_no)) {
                            monthSalesItemSaron.put("newMonthOrders", (int) monthSalesInternal.get(staff_no).get("newMonthOrders"));
                            monthSalesItemSaron.put("manyMonthOrders", (int) monthSalesInternal.get(staff_no).get("manyMonthOrders"));
                            monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders + (int) monthSalesInternal.get(staff_no).get("MonthRedOrders"));
                            monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem + (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItem"));
                            monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu + (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItemNaibu"));
                            monthSalesInternal.put( String.valueOf(nowRow.get("staff_no")), monthSalesItemSaron);
                        } else {
                            monthSalesItemSaron.put("newMonthOrders", 0);
                            monthSalesItemSaron.put("manyMonthOrders", 0);
                            monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders);
                            monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem);
                            monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu);
                            monthSalesInternal.put(String.valueOf(nowRow.get("staff_no")), monthSalesItemSaron);
                        }
                    } else if (monthSalesOutside.containsKey(staff_no)) {
                        monthSalesItemSaron.put("newMonthOrders", (int) monthSalesOutside.get(staff_no).get("newMonthOrders"));
                        monthSalesItemSaron.put("manyMonthOrders", (int) monthSalesOutside.get(staff_no).get("manyMonthOrders"));
                        monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders + (int) monthSalesOutside.get(staff_no).get("MonthRedOrders"));
                        monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem + (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItem"));
                        monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu + (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItemNaibu"));
                        monthSalesOutside.put(String.valueOf( nowRow.get("staff_no")), monthSalesItemSaron);
                    } else {
                        monthSalesItemSaron.put("newMonthOrders", 0);
                        monthSalesItemSaron.put("manyMonthOrders", 0);
                        monthSalesItemSaron.put("MonthRedOrders", MonthRedOrders);
                        monthSalesItemSaron.put("toMonthPerformanceItem", toMonthPerformanceItem);
                        monthSalesItemSaron.put("toMonthPerformanceItemNaibu", toMonthPerformanceItemNaibu);
                        monthSalesOutside.put(String.valueOf( nowRow.get("staff_no")), monthSalesItemSaron);
                    }
                }
            }
        }


       //�ǉ� �S���킯 �𖱁i���j

       for (int i = 0; i < newOrdersListMonthProporsal.size(); i++) {
            Map<String, Object> nowRow = newOrdersListMonthProporsal.get(i);
            Map<String, Object> monthSales = new HashMap<String, Object>();
            int cId = (Integer) nowRow.get("customer_id");
            int key = (Integer) nowRow.get("product_division");
            String mapKey = String.valueOf(key);
            String mapcId = String.valueOf(cId);
            Map<String, Object> customerMap = checkMap.get(mapcId);
            Date oneMonth = (Date) customerMap.get("end_of_month");
            Date sales_date = (Date) nowRow.get("sales_date");
            Boolean flag = customerMap.containsKey(mapKey);
            int newMonthOrders = 0;
            int manyMonthOrders = 0;
            MonthRedOrders = 0;

            if (sales_date.compareTo(oneMonth) < 1) {
                if (flag) {
                    if (key == 5) {
                        //�@���񗈓X�̌��ɍw��
                        newMonthOrders += (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                    } else if (key == 8) {
                        //�@�ԓ`
                        MonthRedOrders +=(int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
                    }
                }
            } else if (key == 5) {
                //�@�����w���i�����ȍ~�̍w���j
                manyMonthOrders += (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
            } else if (key == 8) {
                //�@�ԓ`
                MonthRedOrders += (int) Math.ceil(((BigDecimal) nowRow.get("price")).intValue() * gedai);
            }

            monthSales.put("staff_name1", (String) nowRow.get("staff_name1"));
            monthSales.put("staff_name2", (String) nowRow.get("staff_name2"));
            monthSales.put("staff_class_name", (String) nowRow.get("staff_class_name"));
            monthSales.put("shop_name", (String) nowRow.get("shop_name"));

            String staff_no = String.valueOf(nowRow.get("staff_no"));

            if (nowRow.get("shop_id") != null) {
                //���X�܁A���X�܃X�^�b�t�ɕ�����
                if (nowRow.get("shop_id").equals(shopList)) {
                    if (monthSalesInternal.containsKey(staff_no)) {
                        monthSales.put("newMonthOrders", newMonthOrders + (int) monthSalesInternal.get(staff_no).get("newMonthOrders"));
                        monthSales.put("manyMonthOrders", manyMonthOrders + (int) monthSalesInternal.get(staff_no).get("manyMonthOrders"));
                        monthSales.put("MonthRedOrders", MonthRedOrders + (int) monthSalesInternal.get(staff_no).get("MonthRedOrders"));
                        monthSales.put("toMonthPerformanceItem", (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItem"));
                        monthSales.put("toMonthPerformanceItemNaibu", (int) monthSalesInternal.get(staff_no).get("toMonthPerformanceItemNaibu"));
                        monthSalesInternal.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                    } else {
                        monthSales.put("newMonthOrders", newMonthOrders);
                        monthSales.put("manyMonthOrders", manyMonthOrders);
                        monthSales.put("MonthRedOrders", MonthRedOrders);
                        monthSales.put("toMonthPerformanceItem", 0);
                        monthSales.put("toMonthPerformanceItemNaibu", 0);
                        monthSalesInternal.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                    }
                } else if (monthSalesOutside.containsKey(nowRow.get("staff_no"))) {
                    monthSales.put("newMonthOrders", newMonthOrders + (int) monthSalesOutside.get(staff_no).get("newMonthOrders"));
                    monthSales.put("manyMonthOrders", manyMonthOrders + (int) monthSalesOutside.get(staff_no).get("manyMonthOrders"));
                    monthSales.put("MonthRedOrders", MonthRedOrders + (int) monthSalesOutside.get(staff_no).get("MonthRedOrders"));
                    monthSales.put("toMonthPerformanceItem", (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItem"));
                    monthSales.put("toMonthPerformanceItemNaibu", (int) monthSalesOutside.get(staff_no).get("toMonthPerformanceItemNaibu"));
                    monthSalesOutside.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                } else {
                    monthSales.put("newMonthOrders", newMonthOrders);
                    monthSales.put("manyMonthOrders", manyMonthOrders);
                    monthSales.put("MonthRedOrders", MonthRedOrders);
                    monthSales.put("toMonthPerformanceItem", 0);
                    monthSales.put("toMonthPerformanceItemNaibu", 0);
                    monthSalesOutside.put(String.valueOf(nowRow.get("staff_no")), monthSales);
                }
            }
        }


        int countStaff = 0;

        //�}�b�v�̃L�[�Z�b�g�ł͂Ȃ��A�\�[�g�����S���ҁB
        for (int sid = 0; staffSortedList.size() > sid; sid++) {
        // for (Integer key : monthSalesInternal.keySet()) {

        //�}�b�v����̎擾�́A��������Ȃ�
        // Map<String, Object> staffDifferentSales = monthSalesInternal.get(key);

        //���̂̎擾�s�v�i/���܂ޖ��̂����O�����A���ɏ��O�ρj
        //String removal = (String) staffDifferentSales.get("staff_name1");
            Map<String, Object> staffDetail = staffSortedList.get(sid);

            //8�ȏ�Ȃ�A�����܂ł��Ȃ����炳��Ȃ�
            //  if (countStaff < 8) {
            //�@������A�\�[�g�����S���҂��擾�����ۂɍ���Ă�B����Aif���s�v
            //   if (!removal.contains("/")) {
            //��E
            jx.setCellValue(3, 49 + countStaff, (String) staffDetail.get("staff_class_name"));
            //����
            jx.setCellValue(7, 49 + countStaff, (String) staffDetail.get("staff_name1") + " " + (String) staffDetail.get("staff_name2"));

            //�����Ŕ���i����̋l�܂���Map�ɁA�ΏۃX�^�b�t������Ƃ��i�}�b�v�ɃL�[������Ƃ��j�L�[�̓X�^�b�tNo�j
            if (monthSalesInternal.containsKey(staffDetail.get("staff_no"))) {

                //����̋l�܂����}�b�v������z���擾
                Map<String, Object> staffDifferentSales = monthSalesInternal.get(staffDetail.get("staff_no"));

                //�󒍐V�K
                jx.setCellValue(20, 49 + countStaff, (int) staffDifferentSales.get("newMonthOrders"));
                //�V�K����
                jx.setCellValue(27, 49 + countStaff, (int) staffDifferentSales.get("toMonthPerformanceItem"));
                //�󒍓���
                jx.setCellValue(34, 49 + countStaff, (int) staffDifferentSales.get("manyMonthOrders"));
                //��������
                jx.setCellValue(41, 49 + countStaff, (int) staffDifferentSales.get("toMonthPerformanceItemNaibu"));
                //�ԓ`
                jx.setCellValue(50, 49 + countStaff, (int) staffDifferentSales.get("MonthRedOrders"));
            }else{
                //�󔒂��ƁA���ꊴ�����̂ŁA0�~����
                //�󒍐V�K
                jx.setCellValue(20, 49 + countStaff, 0);
                //�V�K����
                jx.setCellValue(27, 49 + countStaff, 0);
                //�󒍓���
                jx.setCellValue(34, 49 + countStaff, 0);
                //��������
                jx.setCellValue(41, 49 + countStaff, 0);
                //�ԓ`
                jx.setCellValue(50, 49 + countStaff, 0);

            }
            //�J�E���g�A�b�v�i���̍s�Ɉړ�����j
            countStaff++;
        }

        //�`�\�̕\���C���f�b�N�X��0�ɏ�����
        countStaff = 0;
        for (String key : monthSalesOutside.keySet()) {









            Map<String, Object> staffDifferentSales = monthSalesOutside.get(key);
             //���オ0�Ȃ�o�͑ΏۊO
            if( 0 == (int) staffDifferentSales.get("newMonthOrders") &&
                0 == (int) staffDifferentSales.get("toMonthPerformanceItem") &&
                0 == (int) staffDifferentSales.get("manyMonthOrders") &&
                0 == (int) staffDifferentSales.get("toMonthPerformanceItemNaibu") &&
                0 == (int) staffDifferentSales.get("MonthRedOrders")
                    ){
                continue;
            }

			//�����X�ܖ�����̏ꍇ�X�L�b�v
			if( staffDifferentSales.get("shop_name") ==  null
					|| ((String) staffDifferentSales.get("shop_name")).equals("")){
				continue;
			}

            String removal = (String) staffDifferentSales.get("staff_name1");
            if (!removal.contains("/")) {
                if (countStaff < 4) {
                    //��E
                    jx.setCellValue(3, 60 + countStaff, (String) staffDifferentSales.get("staff_class_name"));
                    //����
                    jx.setCellValue(7, 60 + countStaff, (String) staffDifferentSales.get("staff_name1") + " " + (String) staffDifferentSales.get("staff_name2"));
                    //�����X�ܖ�
                    jx.setCellValue(14, 60 + countStaff, (String) staffDifferentSales.get("shop_name"));
                    //�󒍐V�K
                    jx.setCellValue(20, 60 + countStaff, (int) staffDifferentSales.get("newMonthOrders"));
                    //�V�K����
                    jx.setCellValue(27, 60 + countStaff, (int) staffDifferentSales.get("toMonthPerformanceItem"));
                    //�󒍓���
                    jx.setCellValue(34, 60 + countStaff, (int) staffDifferentSales.get("manyMonthOrders"));
                    //��������
                    jx.setCellValue(41, 60 + countStaff, (int) staffDifferentSales.get("toMonthPerformanceItemNaibu"));
                    //�ԓ`
                    jx.setCellValue(50, 60 + countStaff, (int) staffDifferentSales.get("MonthRedOrders"));
                    //�J�E���g�A�b�v�i���̍s�Ɉړ�����j
                    countStaff++;
                }
            }
        }



        //50%
        toMonthPerformanceSaron =(int)Math.ceil(toMonthPerformanceSaron*gedai);

        //�X�^�b�t�ʈꗗ�Q����T����
        jx.setCellValue(14, 57, toMonthPerformanceSaron);

        //------------------------------
        // ���v��
        //------------------------------
        jx.openWorkbook();
        return true;
    }

    /**
     * �G�N�Z���V�[�g�̏��Ƀf�[�^���Z�b�g����(����T����)
     *
     * @param soldList ����̎擾���ʃ��X�g
     * @param product_division �Z�p���i�敪�h�c�̂�����L���X�g�ɔ������������
     * @param classId ���̃��X�g���l�����Ă���N���XID
     * @return resultMap �󂯎�������X�g�̕\�����ɕ��ёւ��āA���X�g����������
     */
    private List<Map<String, Object>> setZyodaiExcelSheetSaron(List<Map<String, Object>> soldList, int product_division, String classId) {

        int product_division_sold = product_division; //����̋Z�p���i�敪�h�c
        int couresDatePrice = 0; //����
        int couresDateNum = 0; //����
        int retention_display_seq = 0;//�t���O�Ǘ�
        int newItemClassId = 0; //�擾�\����
        int nowLine = 1;//���ݍs��

        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>(0);

        for (int i = 0; i < soldList.size(); i++) {
            Map<String, Object> nowRow = soldList.get(i);
            Map<String, Object> resMap = new HashMap<String, Object>();

            newItemClassId = (Integer) nowRow.get(classId);

            if (null != (Integer) nowRow.get(classId)) {

                if (retention_display_seq == 0) {
                    //���ڕK���ʂ鏈��
                    couresDatePrice = (Integer) nowRow.get("price");
                    if ((Integer) nowRow.get("product_division") == product_division_sold) {
                        couresDateNum = (Integer) nowRow.get("product_num");
                        if (soldList.size() == 1 || nowLine == soldList.size()) {
                            retention_display_seq = -1;
                        } else {
                            retention_display_seq = newItemClassId;
                        }
                    } else {
                        retention_display_seq = -1;
                        couresDateNum -= (Integer) nowRow.get("product_num");
                    }
                } else if (retention_display_seq == newItemClassId) {
                    //���ڂŕԕi���񂪂������ꍇ�̏���
                    retention_display_seq = -1;
                    couresDatePrice += (Integer) nowRow.get("price");
                    couresDateNum -= (Integer) nowRow.get("product_num");
                    //���̍s�ɂȂ�̂�nowLine�̒l���{�P����B
                    nowLine++;
                } else {
                    i--;
                    newItemClassId = (Integer) soldList.get(i).get(classId);
                    retention_display_seq = -1;
                }
                //���ʂ����肵����A���ʂ�Ԃ����߂�Map�ɒǉ��B���̌�A�ϐ��̏�����
                if (retention_display_seq == -1) {

                    resMap.put("couresDatePrice", couresDatePrice);
                    resMap.put("couresDateNum", couresDateNum);
                    resMap.put("newItemClassId", newItemClassId);
                    resultMap.add(resMap);
                    retention_display_seq = 0;
                    couresDatePrice = 0;
                    couresDateNum = 0;
                    //���̍s�ɂȂ�̂�nowLine�̒l���{�P����B
                    nowLine++;
                }
            }
        }
        return resultMap;
    }

    /**
     * �G�N�Z���V�[�g�̏��Ƀf�[�^���Z�b�g����(�󒍖𖱔���Ɣ��㕨��)
     *
     * @param soldList ����̎擾���ʃ��X�g
     * @param product_division �Z�p���i�敪�h�c�̂�����L���X�g�ɔ������������
     * @param yAxis EXCEL�̊J�n�c��
     * @param xAxis EXCEL�̊J�n����
     * @param cursor ���i�̕\����
     * @return resultMap �󂯎�������X�g�̕\�����ɕ��ёւ��āA���X�g����������
     */
    private List<Map<String, Object>> setZyodaiExcelSheet(List<Map<String, Object>> soldList, int product_division, int yAxis, int xAxis, int cursor) {

        int product_division_sold = product_division; //����̋Z�p���i�敪�h�c
        int couresDatePrice = 0; //����
        int couresDateNum = 0; //����
        int retention_display_seq = 0;//�t���O�Ǘ�
        int display_seq = 0; //�擾�\����
        int flap = 62; //�E�[�̐ܕԂ��n�_
        int newyAxis = yAxis; //���݂̏c��
        int newxAxis = xAxis; //���݂̉���
        int newxCursor = cursor; //���݂̏��i�\����
        int nowLine = 1;//���ݍs��

        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>(0);

        for (int i = 0; i < soldList.size(); i++) {
            Map<String, Object> nowRow = soldList.get(i);
            Map<String, Object> resMap = new HashMap<String, Object>();

            display_seq = (Integer) nowRow.get("display_seq");

            if (null != (Integer) nowRow.get("display_seq")) {

                if (retention_display_seq == 0) {
                    //���ڕK���ʂ鏈��
                    couresDatePrice = (Integer) nowRow.get("price");
                    if ((Integer) nowRow.get("product_division") == product_division_sold) {
                        couresDateNum = (Integer) nowRow.get("product_num");
                        if (soldList.size() == 1 || nowLine == soldList.size()) {
                            retention_display_seq = -1;
                        } else {
                            retention_display_seq = display_seq;
                        }
                    } else {
                        retention_display_seq = -1;
                        couresDateNum -= (Integer) nowRow.get("product_num");
                    }
                } else if (retention_display_seq == display_seq) {
                    //���ڂŕԕi���񂪂������ꍇ�̏���
                    retention_display_seq = -1;
                    couresDatePrice += (Integer) nowRow.get("price");
                    couresDateNum -= (Integer) nowRow.get("product_num");
                    //���̍s�ɂȂ�̂�nowLine�̒l���{�P����B
                    nowLine++;
                } else {
                    i--;
                    display_seq = (Integer) soldList.get(i).get("display_seq");
                    retention_display_seq = -1;
                }
                //���ʂ����肵����A���ʂ�Ԃ����߂�Map�ɒǉ��B���̌�A�ϐ��̏�����
                if (retention_display_seq == -1) {

                    //����Ă��Ȃ����i�͔�΂��̂�
                    if (display_seq > newxCursor) {
                        newxAxis += 4 * (display_seq - newxCursor);
                    } else if ((product_division_sold != 2 && display_seq != 3) && (product_division_sold != 5 && display_seq != 1)) {
                        newxAxis += 4;
                    }

                    //���s�n�_�̉E�[�܂œ��ꂫ������AEXCEL�ɋL�����邽�߂ɃJ�[�\������i���̍��[�܂ŉ�����B
                    if (newxAxis >= flap) {
                        newyAxis += 7;
                        newxAxis = 6 + (newxAxis - flap);
                        if (newxAxis >= flap) {
                            newyAxis += 7;
                            newxAxis = 6 + (newxAxis - flap);
                        }
                    }

                    resMap.put("couresDatePrice", couresDatePrice);
                    resMap.put("couresDateNum", couresDateNum);
                    resMap.put("display_seq", display_seq);
                    resMap.put("yAxis", newyAxis);
                    resMap.put("xAxis", newxAxis);
                    resultMap.add(resMap);
                    retention_display_seq = 0;
                    couresDatePrice = 0;
                    couresDateNum = 0;
                    newxCursor = display_seq;
                    //���̍s�ɂȂ�̂�nowLine�̒l���{�P����B
                    nowLine++;
                }
            }
        }
        return resultMap;
    }

    /**
     * �𖱏��p����o��
     *
     * @param shop
     * @param targetDate
     * @return
     */
    protected boolean printDailyReport(MstShop shop, Date targetDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String shopList = shop.getShopID() + "";
        String shopName = shop.getShopName();

        // ���t�֘A
        Calendar td = Calendar.getInstance();
        td.setTime(targetDate);
        //������
        td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
        Date aftDate = td.getTime();
        // ����
        td.set(Calendar.DATE, 1);
        Date baseDate = td.getTime();
        // 1�J���O
        td.add(Calendar.MONTH, -1);
        Date bef1m = td.getTime();
        // 2�J���O
        td.add(Calendar.MONTH, -1);
        Date bef2m = td.getTime();
        // 3�J���O
        td.add(Calendar.MONTH, -1);
        Date bef3m = td.getTime();

        // ���O�o��
        SystemInfo.getLogger().info(sdf.format(targetDate) + " - " + shop.getClass() + "_" + shopList);

        //------------------------------
        // �f�[�^�擾
        //------------------------------
        int money = 0;
        int money_tax = 0;
        int invalid_money = 0;
        Long[] mdata = new Long[4];
        Set<Integer> cIds = new HashSet<Integer>(0);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
        List<Map<Integer, Integer>> summaryList = new ArrayList<Map<Integer, Integer>>(0);
        Set<String> staffNames = new HashSet<String>(0);
        try {
            StringBuilder sql = new StringBuilder(4000);
            sql.append(" select");
            sql.append("   mc.customer_id ");
            sql.append("   , mc.customer_name1 ");
            sql.append("   , mc.customer_name2 ");
            sql.append("   , mcr.course_name ");
            // 20170705 GB Start Add #17419
            //sql.append("   , mcr.num , (dcc.product_value / mcr.num) as product_value");
            sql.append("   , dcc.product_num as num , (sign(dc.product_num * (dcc.product_value / dcc.product_num)) * ceil(abs(dc.product_num * (dcc.product_value / dcc.product_num)))) as product_value");
            //sql.append("   , ( sign( ( (dc.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(dsx.sales_date))))  ");
            sql.append("   , ( sign( ( (dc.product_num * (dcc.product_value/dcc.product_num)) / (1.0 +  COALESCE(dcc.tax_rate, get_tax_rate(dsx.sales_date)) )))  ");
            //sql.append("     * ceil( abs( ( (dc.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(dsx.sales_date))))) ");
            sql.append("     * ceil( abs( ( (dc.product_num * (dcc.product_value/dcc.product_num)) / (1.0 + COALESCE(dcc.tax_rate, get_tax_rate(dsx.sales_date)) )))) ");
            // 20170705 GB End Add #17419
            sql.append("     ) AS detail_value_no_tax ");
            sql.append("   , dc.staff_id ");
            sql.append("   , ms.staff_name1 || ms.staff_name2 AS staff_name ");
            sql.append("   , (select sum(n.product_num) from data_contract_digestion n  ");
            // 20170705 GB Start Add #17419
            sql.append("      inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null ");
            // 20170705 GB End Add #17419
            sql.append("      where n.contract_no = dc.contract_no  ");
            // 20170705 GB Start Edit #17419
            //sql.append("      and n.slip_no <= dc.slip_no and n.contract_detail_no = dc.contract_detail_no ");
            sql.append("      and s.sales_date <= ds.sales_date and n.contract_detail_no = dc.contract_detail_no ");
            //sql.append("      and n.contract_shop_id = dc.contract_shop_id and n.delete_date is null) as used ");
            sql.append("      and n.contract_shop_id = dc.contract_shop_id) as used ");
            // 20170705 GB End Edit #17419
            sql.append("   ,dcc.valid_date ");
            sql.append("   ,(select sales_date from data_sales sdt where sdt.shop_id = dcc.shop_id and sdt.slip_no = dcc.slip_no ) as insert_date ");
            sql.append("   ,(CASE WHEN mcr.is_praise_time THEN  mcr.praise_time_limit ");
            sql.append("    ELSE 0 END) AS limit_flg ");
            sql.append(",(select count(cds.slip_no) as can2 from data_sales_detail cds where cds.shop_id = ds.shop_id and cds.slip_no = ds.slip_no and cds.product_id=24 and cds.product_division=1) as can2 ");
            sql.append(" from ");
            sql.append("   data_sales ds  ");
            sql.append("   inner join mst_customer mc on ds.customer_id = mc.customer_id  ");
            sql.append("   left outer join data_sales_detail dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no  ");
            sql.append("   left outer join data_contract_digestion dc on ds.shop_id = dc.shop_id  and ds.slip_no = dc.slip_no and dsd.contract_no = dc.contract_no and dsd.contract_detail_no = dc.contract_detail_no  ");
            sql.append("   left outer join data_contract dcc on  dc.contract_shop_id = dcc.shop_id and dc.contract_no = dcc.contract_no and dc.contract_detail_no = dcc.contract_detail_no ");
            sql.append("   left outer join data_sales dsx on  dsx.shop_id = dcc.shop_id and dsx.slip_no = dcc.slip_no ");
            sql.append("   left outer join mst_course mcr on dsd.product_id = mcr.course_id ");
            sql.append("   left outer join mst_staff ms on dc.staff_id = ms.staff_id ");
            sql.append(" where ");
            sql.append("   dsd.product_division = 6  ");
            sql.append("   and ds.shop_id =").append(shopList);
            sql.append("   and ds.sales_date = '" + sdf.format(targetDate) + "' ");
            sql.append("   and ds.delete_date is null ");
            sql.append("   and dsd.delete_date is null ");
            sql.append(" order by ds.slip_no, dsd.slip_detail_no ");
            SystemInfo.getLogger().info(sql.toString());
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("customer_id", rs.getInt("customer_id"));
                resMap.put("customer_name1", rs.getString("customer_name1"));
                resMap.put("customer_name2", rs.getString("customer_name2"));
                resMap.put("course_name", rs.getString("course_name"));
                resMap.put("num", rs.getBigDecimal("num"));
                resMap.put("detail_value_no_tax", rs.getInt("detail_value_no_tax"));
                resMap.put("staff_name", rs.getString("staff_name"));
                resMap.put("used", rs.getBigDecimal("used"));
                resMap.put("limit_flg", "");
                resMap.put("can2", rs.getInt("can2"));

                if (rs.getInt("limit_flg") > 0
                        && rs.getDate("valid_date") != null
                        && rs.getDate("insert_date") != null) {
                    // �J�����_�[�N���X�̃C���X�^���X���擾
                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(rs.getDate("valid_date"));
                    cal2.setTime(rs.getDate("insert_date"));
                    cal2.add(Calendar.MONTH, rs.getInt("limit_flg"));

                    if (cal1.compareTo(cal2) > 0) {
                        SystemInfo.getLogger().info(
                                sdf.format(cal1.getTime()) + ","
                                + sdf.format(cal2.getTime()));

                        resMap.put("limit_flg", "�~");
                        invalid_money += rs.getBigDecimal("product_value").intValue();
                        // 20170705 GB Start Add #17419
                        money_tax += rs.getBigDecimal("product_value").intValue();
                        // 20170705 GB End Add #17419
                    } else {
                        cIds.add(rs.getInt("customer_id"));
                        money += rs.getInt("detail_value_no_tax");
                        money_tax += rs.getBigDecimal("product_value").intValue();
                    }
//					if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
//						&&cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)
//						&&cal1.get(Calendar.DATE)==cal2.get(Calendar.DATE)) {
//						resMap.put("limit_flg", "�~");
//						//invalid_money+=rs.getInt("detail_value_no_tax");
//						invalid_money+=rs.getInt("product_value");
//					}
                }

                SystemInfo.getLogger().info(resMap.toString());
                resultList.add(resMap);

            }
            rs.close();

            //---------------------------------------------------------------------------------
            // �@�O���ғ��l��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select ");
            sql.append(" course_class_id ");
            sql.append(" ,count(customer_id) as cnt ");
            sql.append(" from (select distinct ");
            sql.append(" mc.course_class_id, ds.customer_id");
            sql.append(" from data_contract_digestion dcd ");
            sql.append(" inner join data_sales_detail dsd ");
            sql.append("  on  dcd.shop_id = dsd.shop_id and dcd.slip_no = dsd.slip_no ");
            sql.append("  and dcd.contract_no = dsd.contract_no and dcd.contract_detail_no = dsd.contract_detail_no ");
            sql.append("  and dsd.product_division = 6 ");
            sql.append(" inner join data_sales ds ");
            sql.append("  on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
            sql.append(" inner join mst_course mc ");
            sql.append("  on dsd.product_id = mc.course_id ");
            //sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23) ");
            sql.append(" where dcd.product_num > 0 ");
            sql.append("  and sales_date < '").append(sdf.format(baseDate)).append("' ");
            sql.append("  and sales_date >= '").append(sdf.format(bef3m)).append("' ");
            sql.append("  and ds.shop_id =").append(shopList);
            sql.append(" group by course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) sum ");
            sql.append(" group by course_class_id ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            Map<Integer, Integer> summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
                summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
            }
            summaryList.add(summaryMap);
            rs.close();
            //---------------------------------------------------------------------------------
            // �A"��"���X���グ�l��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append("	select");
            sql.append("	course_class_id");
            sql.append("	,count(customer_id) as cnt");
            sql.append("	from");
            sql.append("	(select distinct  a.course_class_id, a.customer_id from");
            sql.append("	(select distinct");
            sql.append("	mc.course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no");
            sql.append("	from data_contract_digestion dcd");
            sql.append("	inner join data_sales_detail dsd");
            sql.append("	 on  dcd.shop_id = dsd.shop_id");
            sql.append("	 and dcd.slip_no = dsd.slip_no");
            sql.append("	 and dcd.contract_no = dsd.contract_no");
            sql.append("	 and dcd.contract_detail_no = dsd.contract_detail_no");
            sql.append("	 and dsd.product_division = 6");
            sql.append("	inner join data_sales ds");
            sql.append("	 on ds.shop_id = dsd.shop_id");
            sql.append("	 and ds.slip_no = dsd.slip_no ");
            sql.append("     and ds.shop_id =").append(shopList);
            sql.append("	inner join mst_course mc ");
            sql.append("	 on dsd.product_id = mc.course_id");
            //sql.append("	and mc.course_class_id in (1,2,7,8,9,22,23)");
            sql.append("	where dcd.product_num > 0");
            sql.append("	 and sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append("	 and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append("	group by course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) a");
            sql.append("	left outer join");
            sql.append("	(select distinct");
            sql.append("	mc.course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no");
            sql.append("	from data_contract_digestion dcd");
            sql.append("	inner join data_sales_detail dsd");
            sql.append("	 on  dcd.shop_id = dsd.shop_id");
            sql.append("	 and dcd.slip_no = dsd.slip_no");
            sql.append("	 and dcd.contract_no = dsd.contract_no");
            sql.append("	 and dcd.contract_detail_no = dsd.contract_detail_no");
            sql.append("	 and dsd.product_division = 6");
            sql.append("	inner join data_sales ds");
            sql.append("	 on ds.shop_id = dsd.shop_id");
            sql.append("	 and ds.slip_no = dsd.slip_no");
            sql.append("     and ds.shop_id =").append(shopList);
            sql.append("	inner join mst_course mc ");
            sql.append("	 on dsd.product_id = mc.course_id");
            //sql.append("	and mc.course_class_id in (1,2,7,8,9,22,23)");
            sql.append("	where dcd.product_num > 0");
            sql.append("	 and sales_date < '").append(sdf.format(baseDate)).append("'");
            sql.append("	 and sales_date >= '").append(sdf.format(bef3m)).append("'");
            sql.append("	group by course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) b");
            sql.append("	on a.course_class_id = b.course_class_id");
            sql.append("	and a.customer_id = b.customer_id");
            sql.append("	and a.contract_no = b.contract_no");
            sql.append("	and a.contract_detail_no = b.contract_detail_no");
            sql.append("	where b.customer_id is null) aa");
            sql.append("	group by course_class_id");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
                summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
            }
            summaryList.add(summaryMap);
            rs.close();

            //---------------------------------------------------------------------------------
            // �B�V�K�E�p���l��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select ");
            sql.append("   course_class_id ");
            sql.append("   , count(customer_id) as cnt  ");
            sql.append(" from ");
            sql.append("  (select ");
            sql.append("   ds.sales_date ");
            sql.append(" , ds.customer_id ");
            sql.append(" , course_class_id ");
            sql.append(" , ds.shop_id ");
            sql.append(" , dcd.contract_shop_id ");
            sql.append(" , dcd.contract_no ");
            sql.append(" , dcd.contract_detail_no ");
            sql.append(" , counter.cnt ");
            sql.append(" from ");
            sql.append(" data_contract_digestion dcd  ");
            sql.append(" inner join data_sales_detail dsd  ");
            sql.append("   on  dcd.shop_id = dsd.shop_id  ");
            sql.append("   and dcd.slip_no = dsd.slip_no  ");
            sql.append("   and dcd.contract_no = dsd.contract_no  ");
            sql.append("   and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("   and dsd.product_division = 6  ");
            sql.append("   and dsd.delete_date is null ");
            sql.append(" inner join data_sales ds  ");
            sql.append("   on  ds.shop_id = dsd.shop_id  ");
            sql.append("   and ds.slip_no = dsd.slip_no  ");
            sql.append("   and ds.delete_date is null ");
            sql.append("    inner join mst_course mc  ");
            sql.append("   on dsd.product_id = mc.course_id  ");
            //sql.append("   and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
            sql.append("    left outer join (select ");
            sql.append("  dcd.contract_shop_id ");
            sql.append("   , dcd.contract_no ");
            sql.append("   , dcd.contract_detail_no ");
            sql.append("   , count(dcd.slip_no) as cnt  ");
            sql.append("    from ");
            sql.append("   data_contract_digestion dcd  ");
            sql.append("   inner join data_sales_detail dsd  ");
            sql.append("  on dcd.shop_id = dsd.shop_id  ");
            sql.append("  and dcd.slip_no = dsd.slip_no  ");
            sql.append("  and dcd.contract_no = dsd.contract_no  ");
            sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("  and dsd.product_division = 6  ");
            sql.append("  and dsd.delete_date is null ");
            sql.append("   inner join data_sales ds  ");
            sql.append("  on ds.shop_id = dsd.shop_id  ");
            sql.append("  and ds.slip_no = dsd.slip_no  ");
            sql.append("  and ds.delete_date is null ");
            sql.append("   inner join mst_course mc  ");
            sql.append("  on dsd.product_id = mc.course_id  ");
            //sql.append("  and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
            sql.append("    where ");
            sql.append("   dcd.product_num > 0  ");
            sql.append("    group by ");
            sql.append("  dcd.contract_shop_id ");
            sql.append("   , dcd.contract_no ");
            sql.append("   , dcd.contract_detail_no ");
            sql.append("  ) counter on counter.contract_shop_id = dcd.contract_shop_id ");
            sql.append("  and counter.contract_no = dcd.contract_no ");
            sql.append("  and counter.contract_detail_no = dcd.contract_detail_no ");
            sql.append("   where ");
            sql.append("    ds.shop_id =").append(shopList);
            sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append(" ) base ");
            sql.append(" where ");
            sql.append("  base.cnt = 1  ");
            sql.append(" group by course_class_id ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
                summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
            }
            summaryList.add(summaryMap);
            rs.close();

            //---------------------------------------------------------------------------------
            // �C�I���l��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            // 20170707 GB Start Add #17419
            sql.append(" select course_class_id ");
            sql.append("        , count(customer_id) as cnt from ( ");
            // 20170707 GB End Add #17419
            sql.append(" select ");
            sql.append("   course_class_id ");
            // 20170707 GB Start Edit #17419
            //sql.append("   , count(customer_id) as cnt  " );
            sql.append("   , customer_id ");
            // 20170707 GB End Edit #17419
            sql.append(" from ");
            sql.append("  (select ");
            sql.append("   ds.sales_date ");
            sql.append(" , ds.customer_id ");
            sql.append(" , course_class_id ");
            sql.append(" , ds.shop_id ");
            sql.append(" , dcd.contract_shop_id ");
            sql.append(" , dcd.contract_no ");
            sql.append(" , dcd.contract_detail_no ");
            sql.append(" , counter.cnt ");
            sql.append(" from ");
            sql.append(" data_contract_digestion dcd  ");
            sql.append(" inner join data_sales_detail dsd  ");
            sql.append("   on  dcd.shop_id = dsd.shop_id  ");
            sql.append("   and dcd.slip_no = dsd.slip_no  ");
            sql.append("   and dcd.contract_no = dsd.contract_no  ");
            sql.append("   and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("   and dsd.product_division = 6  ");
            sql.append("   and dsd.delete_date is null ");
            sql.append(" inner join data_sales ds  ");
            sql.append("   on  ds.shop_id = dsd.shop_id  ");
            sql.append("   and ds.slip_no = dsd.slip_no  ");
            sql.append("   and ds.delete_date is null ");
            sql.append("    inner join mst_course mc  ");
            sql.append("   on dsd.product_id = mc.course_id  ");
            // 20170707 GB Start Add #17419
            sql.append(" inner join data_contract dc ");
            sql.append("	 on dsd.contract_no = dc.contract_no ");
            sql.append("	 and dsd.contract_shop_id = dc.shop_id ");
            sql.append("	 and dsd.contract_detail_no = dc.contract_detail_no ");
            // 20170707 GB End Add #17419
            //sql.append("   and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
            sql.append("    left outer join (select ");
            sql.append("  dcd.contract_shop_id ");
            sql.append("   , dcd.contract_no ");
            sql.append("   , dcd.contract_detail_no ");
            sql.append("   , count(dcd.product_num) as cnt  ");
            sql.append("    from ");
            sql.append("   data_contract_digestion dcd  ");
            sql.append("   inner join data_sales_detail dsd  ");
            sql.append("  on dcd.shop_id = dsd.shop_id  ");
            sql.append("  and dcd.slip_no = dsd.slip_no  ");
            sql.append("  and dcd.contract_no = dsd.contract_no  ");
            sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("  and dsd.product_division = 6  ");
            sql.append("  and dsd.delete_date is null ");
            sql.append("   inner join data_sales ds  ");
            sql.append("  on ds.shop_id = dsd.shop_id  ");
            sql.append("  and ds.slip_no = dsd.slip_no  ");
            sql.append("  and ds.delete_date is null ");
            sql.append("   inner join mst_course mc  ");
            sql.append("  on dsd.product_id = mc.course_id  ");
            //sql.append("  and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
            sql.append("    where ");
            sql.append("   dcd.product_num > 0  ");
            sql.append("    group by ");
            sql.append("  dcd.contract_shop_id ");
            sql.append("   , dcd.contract_no ");
            sql.append("   , dcd.contract_detail_no ");
            sql.append("  ) counter on counter.contract_shop_id = dcd.contract_shop_id ");
            sql.append("  and counter.contract_no = dcd.contract_no ");
            sql.append("  and counter.contract_detail_no = dcd.contract_detail_no ");
            sql.append("   where ");
            sql.append("     ds.shop_id =").append(shopList);
            // 20170707 GB Start Edit #17419
            //sql.append(" and mc.num = counter.cnt and sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append(" and dc.product_num = counter.cnt and sales_date >= '").append(sdf.format(baseDate)).append("'");
            // 20170707 GB End Edit #17419
            sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append(" ) base ");
            // 20170707 GB Start Add #17419
            sql.append(" group by course_class_id, customer_id) a ");
            // 20170707 GB End Add #17419
            sql.append(" group by course_class_id ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
                summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
            }
            summaryList.add(summaryMap);
            rs.close();

            // �_�~�[�s
            summaryList.add(new HashMap<Integer, Integer>());

            //---------------------------------------------------------------------------------
            // �����ғ����p�l��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select  ");
            sql.append(" course_class_id  ");
            sql.append(" ,count(customer_id) as cnt  ");
            sql.append(" from (select  ");
            sql.append(" mc.course_class_id, ds.customer_id, count(dcd.slip_no) as dcnt  ");
            sql.append(" from data_contract_digestion dcd  ");
            sql.append(" inner join data_sales_detail dsd  ");
            sql.append("  on  dcd.shop_id = dsd.shop_id  ");
            sql.append("  and dcd.slip_no = dsd.slip_no  ");
            sql.append("  and dcd.contract_no = dsd.contract_no  ");
            sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("  and dsd.product_division = 6 and dsd.delete_date is null ");
            sql.append(" inner join data_sales ds  ");
            sql.append("  on ds.shop_id = dsd.shop_id  ");
            sql.append("  and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
            sql.append(" inner join mst_course mc ");
            sql.append("  on dsd.product_id = mc.course_id  ");
            //sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
            sql.append(" where  ");
            sql.append("  dcd.product_num > 0  ");
            sql.append(" and ds.shop_id =").append(shopList);
            sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append(" group by course_class_id, ds.customer_id) sum  ");
            sql.append(" group by course_class_id  ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
                summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
            }
            summaryList.add(summaryMap);
            rs.close();

            //---------------------------------------------------------------------------------
            // �Q��ȏ㏞�p�����l��
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select  ");
            sql.append(" course_class_id  ");
            sql.append(" ,count(customer_id) as cnt  ");
            sql.append(" from (select  ");
            sql.append(" mc.course_class_id, ds.customer_id, count(dcd.slip_no) as dcnt  ");
            sql.append(" from data_contract_digestion dcd  ");
            sql.append(" inner join data_sales_detail dsd  ");
            sql.append("  on  dcd.shop_id = dsd.shop_id  ");
            sql.append("  and dcd.slip_no = dsd.slip_no  ");
            sql.append("  and dcd.contract_no = dsd.contract_no  ");
            sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append("  and dsd.product_division = 6  and dsd.delete_date is null ");
            sql.append(" inner join data_sales ds  ");
            sql.append("  on ds.shop_id = dsd.shop_id  ");
            sql.append("  and ds.slip_no = dsd.slip_no  and ds.delete_date is null ");
            sql.append(" inner join mst_course mc   ");
            sql.append("  on dsd.product_id = mc.course_id  ");
            //sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
            sql.append(" where  ");
            sql.append("  dcd.product_num > 0  ");
            sql.append(" and ds.shop_id =").append(shopList);
            sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append(" group by course_class_id, ds.customer_id) sum  ");
            sql.append(" where dcnt >1 ");
            sql.append(" group by course_class_id  ");

            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
                summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
            }
            summaryList.add(summaryMap);
            rs.close();

            //---------------------------------------------------------------------------------
            // �X�^�b�t
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select ");
            sql.append(" ms.staff_id, ms.staff_name1, ms.staff_name2");
            sql.append(" from ");
            sql.append(" data_contract_digestion dcd  ");
            sql.append(" inner join data_sales_detail dsd  ");
            sql.append(" on dcd.shop_id = dsd.shop_id and dcd.slip_no = dsd.slip_no  ");
            sql.append(" and dcd.contract_no = dsd.contract_no and dcd.contract_detail_no = dsd.contract_detail_no  ");
            sql.append(" and dsd.product_division = 6 and dsd.delete_date is null ");
            sql.append(" inner join data_sales ds  ");
            sql.append(" on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
            sql.append(" inner join mst_staff ms ");
            sql.append(" on dcd.staff_id = ms.staff_id  ");
            sql.append(" where ");
            sql.append(" dcd.product_num > 0  ");
            sql.append(" and ds.shop_id =").append(shopList);
            sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append(" group by ");
            sql.append(" ms.display_seq, ms.shop_id, ms.staff_id, ms.staff_name1, ms.staff_name2 ");
            sql.append("  order by ms.display_seq, ms.shop_id ");
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                if (1 > rs.getString("staff_name1").indexOf('/')
                        && 1 > rs.getString("staff_name2").indexOf('/')
                        && 1 > rs.getString("staff_name1").indexOf('�^')
                        && 1 > rs.getString("staff_name2").indexOf('�^')) {
                    staffNames.add(rs.getString("staff_name1"));
                }
            }
            rs.close();

            //---------------------------------------------------------------------------------
            // ���̏W�v
            //---------------------------------------------------------------------------------
            sql = new StringBuilder(4000);
            sql.append(" select ");
            // 20170705 GB Start Edit #4456
            //sql.append("     count(distinct customer_id) as a ");
            sql.append("     count(distinct ds.customer_id) as a ");
            // 20170705 GB End Edit #4456
            sql.append("   , count(distinct dsd.slip_no || '' ||  dsd.slip_detail_no) as b ");
            // 20170705 GB Start Edit #4456
            //sql.append("   , sum( sign( ( (dcd.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(ds.sales_date))))  ");
            //sql.append("     * ceil( abs( ( (dcd.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(ds.sales_date))))) ");
            sql.append("   , sum( sign( ( (dcd.product_num * (dcc.product_value/dcc.product_num)) / (1.0 + COALESCE(dcc.tax_rate, get_tax_rate(dsx.sales_date)) )))  ");
            sql.append("     * ceil( abs( ( (dcd.product_num * (dcc.product_value/dcc.product_num)) / (1.0 + COALESCE(dcc.tax_rate, get_tax_rate(dsx.sales_date)) )))) ");
            // 20170705 GB End Edit #4456
            sql.append("     ) AS val ");
            sql.append(" , sum(sign(dcd.product_num * (dcc.product_value / dcc.product_num)) * ceil(abs(dcd.product_num * (dcc.product_value / dcc.product_num)))) AS tax_val ");
            sql.append(" from ");
            sql.append("   data_contract_digestion dcd ");
            sql.append("   inner join data_sales_detail dsd ");
            sql.append("     on dcd.shop_id = dsd.shop_id and dcd.slip_no = dsd.slip_no ");
            sql.append("     and dcd.contract_no = dsd.contract_no and dcd.contract_detail_no = dsd.contract_detail_no ");
            sql.append("     and dsd.product_division = 6  and dsd.delete_date is null ");
            sql.append("   inner join data_sales ds ");
            sql.append("     on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
            sql.append("   inner join mst_staff ms on dcd.staff_id = ms.staff_id ");
            sql.append("   inner join data_contract dcc on  dcd.contract_shop_id = dcc.shop_id and dcd.contract_no = dcc.contract_no and dcd.contract_detail_no = dcc.contract_detail_no ");
            sql.append("   inner join mst_course mcr on dsd.product_id = mcr.course_id ");
            // 20170705 GB Start Add #4456
            sql.append("   inner join data_sales dsx ");
            sql.append("     on dcc.shop_id = dsx.shop_id and dcc.slip_no = dsx.slip_no and dsx.delete_date is null ");
            // 20170705 GB End Add #4456
            sql.append(" where ");
            sql.append(" ds.shop_id =").append(shopList);
            // 20170705 GB Start Edit #4456
            //sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
            //sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
            sql.append(" and ds.sales_date >= '").append(sdf.format(baseDate)).append("'");
            sql.append(" and ds.sales_date <= '").append(sdf.format(targetDate)).append("'");
            // 20170705 GB End Edit #4456
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                mdata[0] = rs.getLong("a");
                mdata[1] = rs.getLong("b");
                mdata[2] = rs.getLong("val");
                mdata[3] = rs.getLong("tax_val");
            }
            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        JPOIApi jx = new JPOIApi("�𖱏��p����");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_daily_report.xls");
        // 20170707 GB Start Add #17419
        // �V�[�g�̍Čv�Z������
        jx.getSheet().setForceFormulaRecalculation(true);
        // 20170707 GB Start Add #17419

        SimpleDateFormat printSdf = new SimpleDateFormat("yyyy�NMM��dd���iE�j");
        jx.setCellValue(1, 1, printSdf.format(targetDate));
        jx.setCellValue(18, 1, shopName);
        jx.setCellValue(8, 3, cIds.size());
        //jx.setCellValue(11, 3, resultList.size());
        jx.setCellValue(3, 4, money);
        jx.setCellValue(9, 4, money_tax);
        //���v
        jx.setCellValue(7, 27, cIds.size());
        jx.setCellValue(15, 27, money);
        jx.setCellValue(22, 27, money_tax - invalid_money);

        jx.setCellValue(20, 3, mdata[0]);
        //jx.setCellValue( 23, 3, mdata[1]);
        jx.setCellValue(15, 4, mdata[2]);
        jx.setCellValue(21, 4, mdata[3]);

        int row = 7;
        int addCnt = 0;
        for (int i = 20; i <= resultList.size(); i++) {
            jx.copyRow(9, 10);
            addCnt++;
        }
        //�@�ꗗ
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> nowRow = resultList.get(i);
            jx.setCellValue(1, row + i, i + 1);
            jx.setCellValue(2, row + i, (String) nowRow.get("customer_name1"));
            jx.setCellValue(4, row + i, (String) nowRow.get("customer_name2"));
            jx.setCellValue(6, row + i, (String) nowRow.get("course_name"));
            jx.setCellValue(10, row + i, ((BigDecimal) nowRow.get("num")).doubleValue());
            jx.setCellValue(12, row + i, (Integer) nowRow.get("detail_value_no_tax"));
            jx.setCellValue(14, row + i, (String) nowRow.get("staff_name"));
            jx.setCellValue(18, row + i, ((BigDecimal) nowRow.get("used")).doubleValue());
            jx.setCellValue(20, row + i, ((BigDecimal) nowRow.get("num")).subtract((BigDecimal) nowRow.get("used")).doubleValue());
            jx.setCellValue(22, row + i, (0 < (int) nowRow.get("can2")) ? "���L�����A" : "");
            jx.setCellValue(25, row + i, (String) nowRow.get("limit_flg"));
        }

        //�v�����̏ꍇ�@�t�F�C�V�����@�X�L�����v�@�������A���@�v���X�e�B���@�R�_�}�@�X�^���_�[�h
        if (shop.getGroupID() == 2) {
            jx.setCellValue(6, 29 + addCnt, "�t�F�C�V����");
            jx.setCellValue(8, 29 + addCnt, "�X�L�����v");
            jx.setCellValue(10, 29 + addCnt, "�������A��");
            jx.setCellValue(12, 29 + addCnt, "�v���X�e�B��");
            jx.setCellValue(14, 29 + addCnt, "�R�_�}");
            jx.setCellValue(16, 29 + addCnt, "�X�^���_�[�h");
            // ���ޕʏW�v�@30,6�`
            for (int i = 0; i < summaryList.size(); i++) {
                if (i == 4) {
                    continue;
                }
                Map<Integer, Integer> nowRow = summaryList.get(i);
                // �X�^���_�[�h
                if (nowRow.containsKey(1)) {
                    jx.setCellValue(16, 30 + addCnt + i, nowRow.get(1));
                } else {
                    jx.setCellValue(16, 30 + addCnt + i, 0);
                }
                //�t�F�C�V����
                if (nowRow.containsKey(2)) {
                    jx.setCellValue(6, 30 + addCnt + i, nowRow.get(2));
                } else {
                    jx.setCellValue(6, 30 + addCnt + i, 0);
                }
                //�X�L�����v
                if (nowRow.containsKey(7)) {
                    jx.setCellValue(8, 30 + addCnt + i, nowRow.get(7));
                } else {
                    jx.setCellValue(8, 30 + addCnt + i, 0);
                }
                //�R�_�}
                if (nowRow.containsKey(8)) {
                    jx.setCellValue(14, 30 + addCnt + i, nowRow.get(8));
                } else {
                    jx.setCellValue(14, 30 + addCnt + i, 0);
                }
                //�������A��
                if (nowRow.containsKey(9)) {
                    jx.setCellValue(10, 30 + addCnt + i, nowRow.get(9));
                } else {
                    jx.setCellValue(10, 30 + addCnt + i, 0);
                }
                //�v���X�e�B��
                if (nowRow.containsKey(22) || nowRow.containsKey(23)) {
                    int nm = ((nowRow.containsKey(22) ? nowRow.get(22) : 0)
                            + (nowRow.containsKey(23) ? nowRow.get(23) : 0));
                    jx.setCellValue(12, 30 + addCnt + i, nm);
                } else {
                    jx.setCellValue(12, 30 + addCnt + i, 0);
                }
            }
            //2018/0821 �𖱏��p����@�R�[�X���ޏW�v�N���[�k�ǉ��@start
        } else if (shop.getGroupID() == 4) {
            //�N���[�k�̏ꍇ�@ZB�@SK�@ES�@RB�@�������A��
            jx.setCellValue(6, 29 + addCnt, "ZB");
            jx.setCellValue(8, 29 + addCnt, "SK");
            jx.setCellValue(10, 29 + addCnt, "ES");
            jx.setCellValue(12, 29 + addCnt, "RB");
            jx.setCellValue(14, 29 + addCnt, "�������A��");

            // ���ޕʏW�v�@30,6�`
            for (int i = 0; i < summaryList.size(); i++) {
                if (i == 4) {
                    continue;
                }
                Map<Integer, Integer> nowRow = summaryList.get(i);
                //ZB
                if (nowRow.containsKey(26)) {
                    jx.setCellValue(6, 30 + addCnt + i, nowRow.get(26));
                } else {
                    jx.setCellValue(6, 30 + addCnt + i, 0);
                }
                //SK
                if (nowRow.containsKey(27)) {
                    jx.setCellValue(8, 30 + addCnt + i, nowRow.get(27));
                } else {
                    jx.setCellValue(8, 30 + addCnt + i, 0);
                }
                //ES
                if (nowRow.containsKey(28)) {
                    jx.setCellValue(10, 30 + addCnt + i, nowRow.get(28));
                } else {
                    jx.setCellValue(10, 30 + addCnt + i, 0);
                }
                //RB
                if (nowRow.containsKey(29)) {
                    jx.setCellValue(12, 30 + addCnt + i, nowRow.get(29));
                } else {
                    jx.setCellValue(12, 30 + addCnt + i, 0);
                }
                //�������A��
                if (nowRow.containsKey(9)) {
                    jx.setCellValue(14, 30 + addCnt + i, nowRow.get(9));
                } else {
                    jx.setCellValue(14, 30 + addCnt + i, 0);
                }
            }
            //2018/0821 �𖱏��p����@�R�[�X���ޏW�v�N���[�k�ǉ��@end
        } else {
            // �X�^�C�� �X�g�����O �t�F�U�[ �~�b�N�X �A���} �s�[�����O �L�[�v �X�^���_�[�h �������A�� ���R�[�X
            jx.setCellValue(6, 29 + addCnt, "�X�^�C��");
            jx.setCellValue(8, 29 + addCnt, "�X�g�����O");
            jx.setCellValue(10, 29 + addCnt, "�t�F�U�[");
            jx.setCellValue(12, 29 + addCnt, "�~�b�N�X");
            jx.setCellValue(14, 29 + addCnt, "�A���}");
            jx.setCellValue(16, 29 + addCnt, "�s�[�����O");
            jx.setCellValue(18, 29 + addCnt, "�L�[�v");
            jx.setCellValue(20, 29 + addCnt, "�X�^���_�[�h");
            jx.setCellValue(22, 29 + addCnt, "�������A��");
            jx.setCellValue(24, 29 + addCnt, "���R�[�X");
            // ���ޕʏW�v�@30,6�`
            for (int i = 0; i < summaryList.size(); i++) {
                if (i == 4) {
                    continue;
                }
                Map<Integer, Integer> nowRow = summaryList.get(i);
                // �X�^�C��
                if (nowRow.containsKey(18)) {
                    jx.setCellValue(6, 30 + addCnt + i, nowRow.get(18));
                } else {
                    jx.setCellValue(6, 30 + addCnt + i, 0);
                }
                //�X�g�����O
                if (nowRow.containsKey(16)) {
                    jx.setCellValue(8, 30 + addCnt + i, nowRow.get(16));
                } else {
                    jx.setCellValue(8, 30 + addCnt + i, 0);
                }
                //�t�F�U�[
                if (nowRow.containsKey(17)) {
                    jx.setCellValue(10, 30 + addCnt + i, nowRow.get(17));
                } else {
                    jx.setCellValue(10, 30 + addCnt + i, 0);
                }
                //�~�b�N�X
                if (nowRow.containsKey(19)) {
                    jx.setCellValue(12, 30 + addCnt + i, nowRow.get(19));
                } else {
                    jx.setCellValue(12, 30 + addCnt + i, 0);
                }
                //�A���}
                if (nowRow.containsKey(24) || nowRow.containsKey(25)) {
                    int nm = ((nowRow.containsKey(24) ? nowRow.get(24) : 0)
                            + (nowRow.containsKey(25) ? nowRow.get(25) : 0));
                    jx.setCellValue(14, 30 + addCnt + i, nm);
                } else {
                    jx.setCellValue(14, 30 + addCnt + i, 0);
                }
                //�s�[�����O
                if (nowRow.containsKey(15)) {
                    jx.setCellValue(16, 30 + addCnt + i, nowRow.get(15));
                } else {
                    jx.setCellValue(16, 30 + addCnt + i, 0);
                }
                //�L�[�v
                if (nowRow.containsKey(14)) {
                    jx.setCellValue(18, 30 + addCnt + i, nowRow.get(14));
                } else {
                    jx.setCellValue(18, 30 + addCnt + i, 0);
                }
                //�X�^���_�[�h
                if (nowRow.containsKey(1)) {
                    jx.setCellValue(20, 30 + addCnt + i, nowRow.get(1));
                } else {
                    jx.setCellValue(20, 30 + addCnt + i, 0);
                }
                //�������A��
                if (nowRow.containsKey(9)) {
                    jx.setCellValue(22, 30 + addCnt + i, nowRow.get(9));
                } else {
                    jx.setCellValue(22, 30 + addCnt + i, 0);
                }
                //���R�[�X
                if (nowRow.containsKey(11) || nowRow.containsKey(12) || nowRow.containsKey(13)) {
                    int nm = ((nowRow.containsKey(11) ? nowRow.get(11) : 0)
                            + (nowRow.containsKey(12) ? nowRow.get(12) : 0)
                            + (nowRow.containsKey(13) ? nowRow.get(13) : 0));
                    jx.setCellValue(24, 30 + addCnt + i, nm);
                } else {
                    jx.setCellValue(24, 30 + addCnt + i, 0);
                }
            }
        }

        int i = 0;
        for (String stff : staffNames) {
            jx.setCellValue(8 + (i * 2), 38 + addCnt, stff);
            i++;
        }

        jx.openWorkbook();
        return true;
    }

    /**
     * �̔�����
     *
     * @param shop
     * @param targetDate
     * @return
     */
    protected boolean printPaymentReport(MstShop shop, Date targetDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String shopList = shop.getShopID() + "";
        String shopName = shop.getShopName();

        Map<String, Map<String, Object>> checkMap = new HashMap<String, Map<String, Object>>(0);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>(0);
        List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>(0);

        Map<Integer, Map<String, String>> payMap = new HashMap<Integer, Map<String, String>>(0);

        Integer regCash = 0;
        String regStaff = "";
        try {
            //�o���ꗗ
            StringBuilder sql = new StringBuilder(4000);
            // 20170720 Start edit #19991
            //sql.append("select use_for ,ms.staff_name1 || ms.staff_name2 as staff ,io_value");
            sql.append("select use_for , case when cash.staff_id is null then '�S���҂Ȃ�' ");
            sql.append("  else ms.staff_name1 || ms.staff_name2 end as staff , io_value ");
            // 20170720 End edit #19991
            sql.append(" from data_cash_io cash");
            sql.append(" left outer join mst_staff ms on cash.staff_id = ms.staff_id");
            sql.append(" where in_out = false");
            sql.append(" and cash.shop_id =").append(shopList);
            sql.append(" and io_date = '" + sdf.format(targetDate) + "' and cash.delete_date is null ");
            sql.append(" order by io_no");
            SystemInfo.getLogger().info(sql.toString());
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("use_for", rs.getString("use_for"));
                resMap.put("staff", rs.getString("staff"));
                resMap.put("io_value", rs.getInt("io_value"));
                outList.add(resMap);
            }
            // �O���̃��W�f�[�^&�Ώۓ��̃��W���߃X�^�b�t
            Calendar td = Calendar.getInstance();
            td.setTime(targetDate);
            // 20170904 #22950 edit start �Ώۓ��ȑO�Œ��߂̃��W���߃f�[�^���擾����
            //td.add(Calendar.DATE, -1);
            sql = new StringBuilder(4000);
            sql.append("select ( money_1 + money_5*5 + money_10*10 + money_50*50 ");
            sql.append("+ money_100*100 + money_500*500 + money_1000*1000 ");
            sql.append("+ money_2000*2000 + money_5000*5000 + money_10000*10000) as regi_money");
            //sql.append(", (select staff_name1 || staff_name2 from mst_staff ms where ms.staff_id = dr.staff_id) as staff_name");
            sql.append(", coalesce (( select ms.staff_name1 || ms.staff_name2  ");
            sql.append("     from data_register dr left join mst_staff ms on dr.staff_id = ms.staff_id  where ");
            sql.append("         dr.manage_date =  '").append(sdf.format(td.getTime())).append("' ");
            sql.append("         and dr.shop_id = ").append(shopList);
            sql.append("   ), '' ) as staff_name  ");
            sql.append(" from data_register dr ");
            sql.append(" where");
            sql.append(" shop_id =").append(shopList);
            //sql.append(" and manage_date = '"+sdf.format(td.getTime())+"' ");
            sql.append(" and manage_date < '").append(sdf.format(td.getTime())).append("' ");
            sql.append("  order by manage_date desc limit 1 ");
            // 20170904 #22950 edit end
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                regCash = rs.getInt("regi_money");
                regStaff = rs.getString("staff_name");
            }

            //�x�����}�X�^
            sql = new StringBuilder(4000);
            sql.append("select mpm.payment_method_id, mpm.payment_method_name, mpm.payment_class_id, mpc.payment_class_name");
            sql.append(" from mst_payment_method mpm inner join mst_payment_class mpc on mpm.payment_class_id = mpc.payment_class_id");
            sql.append(" order by mpm.payment_class_id, mpm.payment_method_id");
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, String> tmpMap = new HashMap<String, String>(0);
                tmpMap.put("payment_method_name", rs.getString("payment_method_name"));
                tmpMap.put("payment_class_id", rs.getString("payment_class_id"));
                tmpMap.put("payment_class_name", rs.getString("payment_class_name"));
                payMap.put(rs.getInt("payment_method_id"), tmpMap);
            }

            //---------------------
            // ����f�[�^�̎擾
            //---------------------
            // 20170907 #22950 edit start ���񗈓X���̒�`�ύX
            sql = new StringBuilder(4000);
            //            sql.append(" select   ");
            //            sql.append(" ds.customer_id, first_date, first_date + '1 month' as one_month, ddd.shop_id, ddd.product_division, ddd.product_id, ddd.slip_cnt  ");
            //            sql.append(" from data_sales ds  ");
            //            sql.append(" left outer join   ");
            //            sql.append(" (select min(ds.sales_date) as first_date, ds.customer_id, ds.shop_id, dsd.product_division, dsd.product_id, count(ds.slip_no) as slip_cnt  ");
            //            sql.append("  from data_sales_detail dsd  ");
            //            sql.append("  inner join data_sales ds on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null  ");
            //            sql.append("  where dsd.delete_date is null  ");
            //            // 2017725 add #19991 (start)
            //            sql.append("  and ds.sales_date is not null  ");
            //            // 20170725 add #19991 (end)
            //            sql.append("  group by ds.customer_id, ds.shop_id, dsd.product_division, dsd.product_id  ");
            //            sql.append("  order by ds.customer_id, ds.shop_id, dsd.product_division, dsd.product_id) ddd  ");
            //            sql.append("  on ds.customer_id = ddd.customer_id  ");
            //            sql.append(" left outer join mst_technic mt on ddd.product_id = mt.technic_id and ddd.product_division in (1,3)   ");
            //            sql.append(" where  ");
            //            sql.append(" ds.shop_id =").append(shopList);
            //            sql.append(" and ds.sales_date = '"+sdf.format(targetDate)+"' ");
            //             // �S�̊�������E�����ȊO
            //            sql.append(" and ddd.product_division not in (0, 6, 9)  ");
            //             // 1�F�Z�p 3�F�Z�p�N���[���ňꕔ�Z�p���ނ�����
            //            sql.append(" and ( ddd.product_division in (1,3) and mt.technic_class_id not in (5,6,7,8,9,10,11,12,13,16,17,18))  ");
            //            sql.append(" order by ds.customer_id, ddd.product_division, ddd.product_id, first_date  ");
            sql.append(" select distinct ds.customer_id , first_date , DATE_TRUNC('month', first_date + '1 months') + '-1 days' as end_of_month ");
            sql.append("     , ddd.product_division , ddd.product_id , ddd.slip_cnt from data_sales ds ");
            sql.append(" left outer join ( select ds.customer_id , ds.shop_id , dsd.product_division , dsd.product_id , count(ds.slip_no) as slip_cnt ");
            sql.append(" from data_sales ds inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no ");
            sql.append("  where ds.delete_date is null and dsd.product_division in (1, 2, 3, 4, 5, 7, 8)  ");
            sql.append("  and ds.sales_date <= '").append(sdf.format(targetDate)).append("' ");
            sql.append(" group by ds.customer_id , ds.shop_id , dsd.product_division , dsd.product_id ) ddd  on ds.customer_id = ddd.customer_id  ");
            sql.append(" left outer join ( select ( select min(sales_date) from data_sales where customer_id = ds.customer_id ) as first_date , ds.customer_id ");
            sql.append(" from data_sales ds where ds.delete_date is null ) dayt on ds.customer_id = dayt.customer_id where ");
            sql.append(" ds.shop_id =").append(shopList);
            sql.append(" and ds.sales_date = '").append(sdf.format(targetDate)).append("' ");
            sql.append(" order by ds.customer_id , ddd.product_division , ddd.product_id , first_date ");
            // 20170907 #22950 edit end ���񗈓X���̒�`�ύX
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                String customerId = rs.getString("customer_id");
                Map<String, Object> customerMap = null;
                if (checkMap.containsKey(customerId)) {
                    customerMap = checkMap.get(customerId);
                    // ���񗈓X�����V������΍X�V
                    //            if (rs.getDate("first_date").compareTo((Date)customerMap.get("first_date")) < 0) {
                    //                    customerMap.put("first_date", rs.getDate("first_date"));
                    //                    customerMap.put("one_month", rs.getDate("one_month"));
                    //            }
                } else {
                    customerMap = new HashMap<String, Object>(0);
                    customerMap.put("first_date", rs.getDate("first_date"));
                    //customerMap.put("one_month", rs.getDate("one_month"));
                    customerMap.put("end_of_month", rs.getDate("end_of_month")); //���񗈓X���̌���
                }
                String key = rs.getString("product_division") + "-" + rs.getString("product_id");
                customerMap.put(key, rs.getInt("slip_cnt"));
                SystemInfo.getLogger().info(customerMap.toString());
                checkMap.put(customerId, customerMap);
            }

            //---------------------
            // �ꗗ�f�[�^�̎擾
            //---------------------
            sql = new StringBuilder(4000);
            // 20170904 #22950 edit start �̔��z�͖��׊���̋��z��\���E���͌_�񎞂̐ŗ��ŐŔ��z���v�Z
            sql.append(" select   ");
            sql.append(" dsd.sales_date, dsd.product_division, dsd.product_id, dsd.slip_no, dsd.slip_detail_no  ");
            sql.append(" , mc.customer_id, mc.customer_name1, mc.customer_name2  ");
            sql.append(" ,  case when dsd.product_division = 8 then ");
            sql.append("      ( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + COALESCE(contract.tax_rate, get_tax_rate(contract.sales_date)) )))   ");
            sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + COALESCE(contract.tax_rate, get_tax_rate(contract.sales_date)) ))))) ");
            sql.append("    else  ( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + COALESCE(dsd.tax_rate, get_tax_rate(dsd.sales_date)) )))  ");
            sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + COALESCE(dsd.tax_rate, get_tax_rate(dsd.sales_date)) ))))) end as detail_value_no_tax ");
            sql.append(" , (dsd.product_num * dsd.product_value) as product_value  ");
            sql.append(" , dsd.discount_detail_value_in_tax,  dsd.discount_detail_value_no_tax ");
            sql.append(" , dp.bill_value  ");
            sql.append(" , payment.pm1, payment.pv1, payment.pm2, payment.pv2, payment.pm3, payment.pv3, payment.pm4, payment.pv4 ");
            sql.append(" , dsd.detail_staff_id as staff_id ");
            sql.append(" , mt.technic_class_id, mt.technic_name ");
            sql.append(" , mi.item_class_id, mi.item_name ");
            sql.append(" , mcc.course_class_id, mcc.course_name ");
            sql.append(" ,(select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = dsd.detail_staff_id) as staff ");
            sql.append(" , total ,dp.change_value ,mt.technic_class_id ");
            // 20170906 #22950 add start ���l���Ƀ|�C���g���o��
            sql.append("  , point.use_point as use_point ");
            sql.append("  , (select sum(discount_value)  from view_data_sales_detail_valid vdsd left join mst_technic submt ");
            sql.append("         on vdsd.product_id = submt.technic_id and vdsd.product_division in (1,3) ");
            sql.append("       where dsd.shop_id = vdsd.shop_id and dsd.slip_no = vdsd.slip_no  and (  vdsd.product_division in (1, 2, 3, 4, 5, 7, 8) ");
            sql.append("              and ( submt.technic_class_id is null  or submt.technic_class_id not in (5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 21, 22  ) ");
            sql.append("   ) )  ) as total_detail_discount_value");
            // 20170906 #22950 add end ���l���Ƀ|�C���g���o��
            sql.append(" from ");
            //sql.append(" data_sales_detail dsd  ");
            //sql.append(" inner join data_sales ds on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null  ");
            sql.append(" view_data_sales_detail_valid dsd ");
            sql.append(" 	left join (select dsx.sales_date, ds.shop_id, ds.slip_no, dc.tax_rate from data_sales_detail ds ");
            sql.append("    inner join data_contract dc on ds.contract_no = dc.contract_no and ds.contract_shop_id = dc.shop_id ");
            sql.append("    inner join data_sales dsx on dc.shop_id = dsx.shop_id and dc.slip_no = dsx.slip_no where ds.product_division = 8 ");
            sql.append("            group by dsx.sales_date, ds.shop_id, ds.slip_no, dc.tax_rate) contract on dsd.shop_id = contract.shop_id and dsd.slip_no = contract.slip_no ");
            sql.append(" inner join data_payment dp on dp.shop_id = dsd.shop_id and dp.slip_no = dsd.slip_no and dp.payment_no = 0 and dp.delete_date is null  ");
            sql.append(" inner join mst_customer mc on dsd.customer_id = mc.customer_id  ");
            sql.append(" inner join ( select ds.shop_id, ds.sales_date, ds.slip_no, dp.payment_no, ds.customer_id");
            sql.append(" , dp.staff_id, dp.bill_value, dp.change_value");
            sql.append(" , p1.payment_method_id as pm1, p1.payment_value as pv1, p2.payment_method_id as pm2, p2.payment_value as pv2");
            sql.append(" , p3.payment_method_id as pm3, p3.payment_value as pv3, p4.payment_method_id as pm4, p4.payment_value as pv4");
            sql.append(" from data_sales ds inner join data_payment dp on ds.shop_id = dp.shop_id and  ds.slip_no = dp.slip_no and dp.payment_no = 0");
            sql.append(" left outer join data_payment_detail p1 on p1.payment_detail_no = 1 and ds.shop_id = p1.shop_id and  ds.slip_no = p1.slip_no and dp.payment_no = p1.payment_no");
            sql.append(" left outer join data_payment_detail p2 on p2.payment_detail_no = 2 and ds.shop_id = p2.shop_id and  ds.slip_no = p2.slip_no and dp.payment_no = p2.payment_no");
            sql.append(" left outer join data_payment_detail p3 on p3.payment_detail_no = 3 and ds.shop_id = p3.shop_id and  ds.slip_no = p3.slip_no and dp.payment_no = p3.payment_no");
            sql.append(" left outer join data_payment_detail p4 on p4.payment_detail_no = 4 and ds.shop_id = p4.shop_id and  ds.slip_no = p4.slip_no and dp.payment_no = p4.payment_no");
            sql.append(" where ds.sales_date = '" + sdf.format(targetDate) + "' ) payment on dp.shop_id = payment.shop_id and dp.slip_no = payment.slip_no and dp.payment_no = payment.payment_no ");
            sql.append(" left outer join mst_technic mt on dsd.product_id = mt.technic_id and dsd.product_division in (1,3)   ");
            sql.append(" left outer join mst_item mi on dsd.product_id = mi.item_id and dsd.product_division in (2,4)   ");
            sql.append(" left outer join mst_course mcc on dsd.product_id = mcc.course_id and dsd.product_division in (5,6,7,8) ");
            sql.append(" left outer join (select dx.shop_id, dx.slip_no, SUM(dx.product_value) as total from data_sales_detail dx group by dx.shop_id, dx.slip_no) dsdd on dsdd.shop_id = dsd.shop_id and dsdd.slip_no = dsd.slip_no   ");
            // 20170906 #22950 add start ���l���Ƀ|�C���g���o��
            sql.append("	left join data_point point  on dsd.shop_id = point.shop_id and dsd.slip_no = point.slip_no ");
            // 20170906 #22950 add end ���l���Ƀ|�C���g���o��
            sql.append(" where ");
            sql.append(" dsd.shop_id =").append(shopList);
            sql.append(" and dsd.sales_date = '" + sdf.format(targetDate) + "' ");
            // --�S�̊�������E�����ȊO  1�F�Z�p 3�F�Z�p�N���[���ňꕔ�Z�p���ނ�����
            // 20170830 #22950 ������ꏞ�p�E���i�w����ǉ�
            // �����L�����Z�����E �A�t�^�[ �E���ށE ���b�X�� �E���K�E ���V�E �J�E���Z�����O�E ���f�B�֌W �E�P���܂��E �T�C�Y�E �ʐ^�E ���i�[�i �E ������ꏞ�p�E���i�w��
            //sql.append(" and ( dsd.product_division in (1, 2, 3, 4, 5, 7, 8) and (mt.technic_class_id is null or mt.technic_class_id not in (5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18)))  ");
            sql.append(" and ( dsd.product_division in (1, 2, 3, 4, 5, 7, 8) and (mt.technic_class_id is null or mt.technic_class_id not in (5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 21, 22)))  ");
            sql.append(" order by dsd.slip_no, dsd.detail_staff_id, dsd.slip_detail_no");
            // 20170904 #22950 edit end
            SystemInfo.getLogger().info(sql.toString());
            rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                int cash = 0;
                int voucher = 0;
                int div = 0;
                Map<String, Object> resMap = new HashMap<String, Object>();

                String productName = (rs.getString("technic_name") != null) ? rs.getString("technic_name")
                        : (rs.getString("item_name") != null) ? rs.getString("item_name")
                        : rs.getString("course_name");
                resMap.put("slip_no", rs.getInt("slip_no"));
                resMap.put("productName", productName);
                resMap.put("customerName", rs.getString("customer_name1") + rs.getString("customer_name2"));
                resMap.put("detailValueNoTax", rs.getInt("detail_value_no_tax")); //���׊��O���z�i�Ŕ��j
                // 20170905 #22950 edit start
                //resMap.put("tax", rs.getInt("product_value") - rs.getInt("detail_value_no_tax"));
                resMap.put("tax", rs.getInt("discount_detail_value_in_tax") - rs.getInt("discount_detail_value_no_tax"));
                resMap.put("discountDetailValueNoTax", rs.getInt("discount_detail_value_no_tax")); //���׊�����z�i�Ŕ��j
                div = rs.getInt("product_division");
                resMap.put("productDivision", div);
                resMap.put("usePoint", rs.getInt("use_point"));
                resMap.put("totalDetailDiscountValue", rs.getInt("total_detail_discount_value"));
                // 20170905 #22950 edit end

                //�x������
                int count = 1;
                for (int i = 1; i <= 4; i++) {
                    if (rs.getString("pm" + i) != null && !rs.getString("pm" + i).equals("")) {
                        // 20170904 #22950 edit start �����͎x�����@���Ɋ܂߂Ȃ�
                        Map<String, String> tmpMap = payMap.get(rs.getInt("pm" + i));
                        if (tmpMap.get("payment_class_id").equals("1")) {
                            cash += rs.getInt("pv" + i);
                            continue;
                        } else if (tmpMap.get("payment_class_id").equals("4")) {
                            voucher += rs.getInt("pv" + i);
                        }

                        //    resMap.put("pm"+i,tmpMap.get("payment_method_name"));
                        //    resMap.put("pc"+i,tmpMap.get("payment_class_id"));
                        //    resMap.put("pcn"+i,tmpMap.get("payment_class_name"));
                        //    resMap.put("pv"+i,rs.getInt("pv"+i));
                        if (count > 3) {
                            break;
                        }

                        resMap.put("pm" + count, tmpMap.get("payment_method_name"));
                        resMap.put("pc" + count, tmpMap.get("payment_class_id"));
                        resMap.put("pcn" + count, tmpMap.get("payment_class_name"));
                        resMap.put("pv" + count, rs.getInt("pv" + i));

                        count++;

                        //    if(tmpMap.get("payment_class_id").equals("1")) {
                        //            cash += rs.getInt("pv"+i);
                        //    } else if(tmpMap.get("payment_class_id").equals("4")) {
                        //            voucher += rs.getInt("pv"+i);
                        //    }
                        // 20170904 #22950 edit end
                    }
                }
                resMap.put("cash", cash - rs.getInt("change_value"));
                resMap.put("staff", rs.getString("staff"));
                resMap.put("billValue", rs.getInt("bill_value"));

                String cId = rs.getString("customer_id");
                // �V�K���s�[�g
                resMap.put("nr", "");
                int tc = rs.getInt("technic_class_id");
                int it = rs.getInt("item_class_id");
                // 20170830 #22950
                //if (tc == 14 || tc == 20 || it == 15 || it ==16 || ( 65<= it && it<= 76)) {
                if (tc == 14 || tc == 20 || it == 15 || it == 16 || (65 <= it && it <= 75) || it == 78) {
                    // �Z�p�F14 �A�C���b�V���Ј�, 20 �Ј��������T�[�r�X
                    // ���i�F15 �Ј��w��, 16 �A�C���b�V���Ј��w��, 65~75, 78 �Ј��@�ق��ق�
                    resMap.put("nr", "��");
                } else {
                    // 20170915 #22950 edit start �u�V�E���E���E�Ёv�̎d�l�ύX
//					// �ߋ��f�[�^���Ȃ��ꍇ
//					if(!checkMap.containsKey(cId)){
//						resMap.put("nr","�V");
//					} else {
//						Map<String, Object> customerMap = checkMap.get(cId);
//						String key = rs.getString("product_division") +"-"+ rs.getString("product_id");
//
//						if (customerMap.containsKey(key)
//								&& (int)customerMap.get(key) == 2) {
//							// 2��ڂ̏��i�w���̏ꍇ
//							resMap.put("nr","��");
//						} else {
//							// ���񗈓X����1������̓��t���擾
//							Date oneMonth = (Date)customerMap.get("one_month");
//							if( rs.getDate("sales_date").compareTo(oneMonth) < 1) {
//								resMap.put("nr","�V");
//							} else if (!customerMap.containsKey(key)){
//								resMap.put("nr","��");
//							}
//						}
//					}
                    Map<String, Object> customerMap = checkMap.get(cId);
                    String key = rs.getString("product_division") + "-" + rs.getString("product_id");
                    Date oneMonth = (Date) customerMap.get("end_of_month");
                    Boolean flag = customerMap.containsKey(key);
                    int keyCount = (int) customerMap.get(key);

                    if (rs.getDate("sales_date").compareTo(oneMonth) < 1) {
                        // ���񗈓X���ȓ�
                        if (flag && keyCount == 1) {
                            //�@�V�K�i����w���j
                            resMap.put("nr", "�V");
                        } else if ((div == 1 || div == 2) && flag && keyCount >= 2) {
                            //�@���s�[�g�i�Q��ڈȏ�̍w���B�Z�p�Ə��i�̂݁j
                            resMap.put("nr", "��");
                        }

                    } else // ���񗈓X���ȍ~
                    {
                        if (div == 5 && flag && keyCount == 1) {
                            // �_��
                            resMap.put("nr", "��");
                        } else if (div == 1 || div == 2) {
                            if (it == 6 || it == 7 || it == 9 || it == 17 || it == 19 || it == 24 || it == 25 || it == 27 || it == 28 || it == 77) {
                                // �����Ώۏ��i
                                if (flag && keyCount == 1) {
                                    resMap.put("nr", "��");
                                } else if (flag && keyCount >= 2) {
                                    resMap.put("nr", "��");
                                }
                            } else // �����ȊO�̏��i�ƋZ�p
                            {
                                if (flag && keyCount >= 1) {
                                    resMap.put("nr", "��");
                                }
                            }
                        }
                    }

                }
                // ��E���E�T����
                resMap.put("kbn", "");
                if (resMap.get("nr").equals("��")
                        || rs.getInt("staff_id") == 17 || rs.getInt("staff_id") == 19 || rs.getInt("staff_id") == 20
                        || voucher > 0) {
                    resMap.put("kbn", "�T");
                } else if (rs.getInt("product_division") >= 5 && rs.getInt("product_division") <= 8) {
                    resMap.put("kbn", "��");
                    // 20170830 #22950
                    // } else if(rs.getInt("product_division") >= 2 && rs.getInt("product_division") <= 4 ) {
                } else if (rs.getInt("product_division") >= 1 && rs.getInt("product_division") <= 4) {
                    resMap.put("kbn", "��");
                }

                // 3�F�Z�p�N���[���@4�F���i�ԕi�͕�����
//				if (rs.getInt("product_division") == 3 || rs.getInt("product_division") == 4
//						|| (rs.getInt("product_division") ==1 && rs.getInt("technic_class_id") ==1)	) {
                // �̌��E�C���I�[���E���̑��E�Ј��������T�[�r�X
                if (rs.getInt("technic_class_id") == 1
                        || rs.getInt("technic_class_id") == 15
                        || rs.getInt("technic_class_id") == 19
                        || rs.getInt("technic_class_id") == 20) {
                    resultList2.add(resMap);
                } else {
                    resultList.add(resMap);
                }
            }
            // ���̑�����
            sql = new StringBuilder(4000);
            sql.append(" select ds.slip_no, TO_CHAR(ds.sales_date, 'MM��DD��') || ' ���|��' as  title");
            sql.append(" , mc.customer_name1 || mc.customer_name2 as cusname");
            sql.append(" , ( select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = dp.staff_id ) as staff");
            sql.append(" , p1.payment_method_id as pm1, p1.payment_value - dp.change_value as pv1, p2.payment_method_id as pm2, p2.payment_value - dp.change_value as pv2");
            sql.append(" , p3.payment_method_id as pm3, p3.payment_value - dp.change_value as pv3, p4.payment_method_id as pm4, p4.payment_value - dp.change_value as pv4");
            sql.append(" , ( select sum(dpdx.payment_value - dp.change_value ) from data_payment_detail dpdx");
            sql.append("  where dpdx.shop_id = dp.shop_id and dpdx.slip_no = dp.slip_no ");
            sql.append("  and dpdx.payment_no = dp.payment_no and dpdx.payment_method_id = 1) as sum");
            sql.append(" , dp.bill_value as bill_value ");
            sql.append(" from data_sales ds ");
            sql.append(" inner join data_payment dp on ds.shop_id = dp.shop_id and ds.slip_no = dp.slip_no");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id ");
//            sql.append(" left outer join data_payment_detail p1 on p1.payment_detail_no = 1 and ds.shop_id = p1.shop_id and ds.slip_no = p1.slip_no and dp.payment_no = p1.payment_no ");
			sql.append(" join data_payment_detail p1 on p1.payment_detail_no = 1 and p1.delete_date is null and ds.shop_id = p1.shop_id and ds.slip_no = p1.slip_no and dp.payment_no = p1.payment_no ");
            sql.append(" left outer join data_payment_detail p2 on p2.payment_detail_no = 2 and ds.shop_id = p2.shop_id and ds.slip_no = p2.slip_no and dp.payment_no = p2.payment_no ");
            sql.append(" left outer join data_payment_detail p3 on p3.payment_detail_no = 3 and ds.shop_id = p3.shop_id and ds.slip_no = p3.slip_no and dp.payment_no = p3.payment_no ");
            sql.append(" left outer join data_payment_detail p4 on p4.payment_detail_no = 4 and ds.shop_id = p4.shop_id and ds.slip_no = p4.slip_no and dp.payment_no = p4.payment_no ");
            sql.append(" where dp.shop_id = ").append(shopList).append(" and dp.payment_date = '" + sdf.format(targetDate) + "' and dp.payment_no > 0");
            sql.append(" union all");
            sql.append(" select ( -99 - io_no)as slip_no, use_for as  title, null as cusname");
            // 20170720 Start edit #19991
            //sql.append(" , ( select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = dci.staff_id ) as staff");
            sql.append("  , case  when dci.staff_id is null  then '�S���҂Ȃ�'  else ");
            sql.append("    ( select ms.staff_name1 || ms.staff_name2 from  mst_staff ms ");
            sql.append("      where  ms.staff_id = dci.staff_id )  end as staff ");
            // 20170720 End edit #19991
            sql.append(" , null as pm1, null as pv1, null as pm2, null as pv2, null as pm3, null as pv3, null as pm4, null as pv4");
            // 20170911 #22950 edit start
            //sql.append(" , io_value as sum from data_cash_io dci");
            sql.append(" , io_value as sum, null as bill_value from data_cash_io dci ");
            // 20170911 #22950 edit end
            sql.append(" where dci.in_out = True and dci.shop_id = ").append(shopList).append(" and dci.io_date = '" + sdf.format(targetDate) + "'");
            // 20170720 Start add #19991
            sql.append(" and dci.delete_date is null ");
            // 20170720 End add #19991

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

            SystemInfo.getLogger().info(sql.toString());

            while (rs.next()) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("slip_no", rs.getInt("slip_no"));
                resMap.put("productName", rs.getString("title"));
                resMap.put("customerName", rs.getString("cusname"));
                //�x������
                // 20170904 #22950 edit start �����͎x�����@���ɕ\�����Ȃ�
                int count = 1;
                for (int i = 1; i <= 4; i++) {
                    if (rs.getString("pm" + i) != null && !rs.getString("pm" + i).equals("")) {
                        Map<String, String> tmpMap = payMap.get(rs.getInt("pm" + i));
                        if (tmpMap.get("payment_class_id").equals("1")) {
                            continue;
                        }

                        if (count > 3) {
                            break;
                        }

                        //    resMap.put("pm"+i,tmpMap.get("payment_method_name"));
                        //    resMap.put("pc"+i,tmpMap.get("payment_class_id"));
                        //    resMap.put("pcn"+i,tmpMap.get("payment_class_name"));
                        //    resMap.put("pv"+i,rs.getInt("pv"+i));
                        resMap.put("pm" + count, tmpMap.get("payment_method_name"));
                        resMap.put("pc" + count, tmpMap.get("payment_class_id"));
                        resMap.put("pcn" + count, tmpMap.get("payment_class_name"));
                        resMap.put("pv" + count, rs.getInt("pv" + i));

                        count++;
                    }
                }
                // 20170904 #22950 edit end
                resMap.put("cash", rs.getInt("sum"));
                resMap.put("staff", rs.getString("staff"));
                // 20170911 #22950 add start
                resMap.put("billValue", rs.getInt("bill_value"));
                // 20170911 #22950 add end
                resultList2.add(resMap);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        JPOIApi jx = new JPOIApi("�̔����ѕ\");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_payment_report.xls");
        // �V�[�g�̍Čv�Z������ 20170905 #22950 add
        jx.getSheet().setForceFormulaRecalculation(true);
        // �w�b�_�[
        SimpleDateFormat printSdf = new SimpleDateFormat("yyyy�NMM��dd���iE�j");
        //jx.setCellValue(20, 1, printSdf.format(targetDate));
        jx.setCellValue(18, 1, printSdf.format(targetDate));
        //jx.setCellValue(20, 2, shopName);
        jx.setCellValue(18, 2, shopName);

        //�@�ꗗ
        int row = 5;
        int addCnt = 0;
        int salesSum = 0;
        int cashSum = 0;
        for (int i = 18; i < resultList.size(); i++) {
            jx.copyRow(5, 6);
            addCnt++;
        }

        int tmpSlip = 0;
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> nowRow = resultList.get(i);
            jx.setCellValue(2, row + i, nowRow.get("nr").toString());
            jx.setCellValue(3, row + i, nowRow.get("kbn").toString());
            jx.setCellValue(4, row + i, nowRow.get("productName").toString());
            jx.setCellValue(6, row + i, nowRow.get("customerName").toString());
            // 20170905 #22950 edit start
            //jx.setCellValue(8, row+i, (int)nowRow.get("detailValueNoTax"));
            jx.setCellValue(8, row + i, (int) nowRow.get("discountDetailValueNoTax"));
            // 20170905 #22950 edit end
            jx.setCellValue(10, row + i, (int) nowRow.get("tax"));

            if (tmpSlip != (int) nowRow.get("slip_no")) {
                // 20170904 #22950 �x�����@4��3�ɕύX&�����͕\�����Ȃ�
                //for(int j=1; j<=4; j++) {
                for (int j = 1; j <= 3; j++) {
                    if (nowRow.containsKey("pm" + j)) {
                        jx.setCellValue(11 + (j * 2 - 2), row + i, nowRow.get("pm" + j).toString());
                        jx.setCellValue(12 + (j * 2 - 2), row + i, (int) nowRow.get("pv" + j));
                    }
                }
                //jx.setCellValue(20, row+i,(int) nowRow.get("cash"));
                jx.setCellValue(18, row + i, (int) nowRow.get("cash"));
                //jx.setCellValue(23, row+i, (int)nowRow.get("billValue"));
                jx.setCellValue(21, row + i, (int) nowRow.get("billValue"));
                cashSum += (int) nowRow.get("cash");

                // 20170906 #22950 add start
                // ** ���l���Ƀ|�C���g�o�� **
                String strPoint = "";
                int shopPoint = (int) nowRow.get("usePoint");
                int totalDiscountVal = (int) nowRow.get("totalDetailDiscountValue");
                int webPoint = totalDiscountVal - shopPoint;
                if (shopPoint > 0 && webPoint > 0) {
                    strPoint = shopPoint + "P/\nw" + webPoint + "P";
                } else if (shopPoint > 0) {
                    strPoint = shopPoint + "P";
                } else if (webPoint > 0) {
                    strPoint = "w" + webPoint + "P";
                }
                jx.setCellValue(17, row + i, strPoint);
                // 20170906 #22950 add end
            }
            //jx.setCellValue(22, row+i, (String)nowRow.get("staff"));
            jx.setCellValue(20, row + i, (String) nowRow.get("staff"));
            salesSum += (int) nowRow.get("detailValueNoTax");
            tmpSlip = (int) nowRow.get("slip_no");

            // 20170906 #22950 add start �Z�p�N���[���E���i�ԕi�E���͔̔��z�Ə���ł�Ԏ��ɂ���
            int div = (int) nowRow.get("productDivision");
            if (div == 3 || div == 4 || div == 8) {
                jx.setCellColor(8, row + i, HSSFColor.RED.index);
                jx.setCellColor(10, row + i, HSSFColor.RED.index);
            }
            // 20170906 #22950 add end �Z�p�N���[���E���i�ԕi�E���͔̔��z�Ə���ł�Ԏ��ɂ���
        }
        jx.setCellValue(8, 25 + addCnt, salesSum);

        //�@�N���[���E�ԕi�ꗗ
        tmpSlip = 0;
        int row2 = 30 + addCnt;
        int addCnt2 = 0;
        int cashSum2 = 0;

        for (int i = 3; i < resultList2.size(); i++) {
            // 2017725 edit #19991 (start)
            //jx.copyRow(30, 31);
            jx.copyRow(row2, row2 + 1);
            // 2017725 edit #19991 (end)
            addCnt2++;
        }
        for (int i = 0; i < resultList2.size(); i++) {
            Map<String, Object> nowRow = resultList2.get(i);
            jx.setCellValue(2, row2 + i, (String) nowRow.get("nr"));
            jx.setCellValue(3, row2 + i, (String) nowRow.get("kbn"));
            jx.setCellValue(4, row2 + i, (String) nowRow.get("productName"));
            jx.setCellValue(6, row2 + i, (String) nowRow.get("customerName"));
            // 20170905 #22950 edit start
            //    if (nowRow.containsKey("detailValueNoTax")) {
            //            jx.setCellValue(8, row2+i, (Integer)nowRow.get("detailValueNoTax"));
            //    }
            if (nowRow.containsKey("discountDetailValueNoTax")) {
                jx.setCellValue(8, row2 + i, (Integer) nowRow.get("discountDetailValueNoTax"));
            }
            // 20170905 #22950 edit end
            if (nowRow.containsKey("tax")) {
                jx.setCellValue(10, row2 + i, (Integer) nowRow.get("tax"));
            }

            if (tmpSlip == -99) {
                //jx.setCellValue(20, row+i,(int) nowRow.get("cash"));
                jx.setCellValue(18, row + i, (int) nowRow.get("cash"));
                cashSum2 += (int) nowRow.get("cash");
            } else if (tmpSlip != (Integer) nowRow.get("slip_no")) {
                // 20170904 #22950 �x�����@4��3�ɕύX&�����͕\�����Ȃ�
                //for(int j=1; j<=4; j++) {
                for (int j = 1; j <= 3; j++) {
                    if (nowRow.containsKey("pm" + j)) {
                        jx.setCellValue(11 + (j * 2 - 2), row2 + i, nowRow.get("pm" + j).toString());
                        jx.setCellValue(12 + (j * 2 - 2), row2 + i, (int) nowRow.get("pv" + j));
                    }
                }
                //jx.setCellValue(20, row2+i, (Integer) nowRow.get("cash"));
                jx.setCellValue(18, row2 + i, (Integer) nowRow.get("cash"));
                //jx.setCellValue(22, row2+i, (String) nowRow.get("staff"));
                jx.setCellValue(20, row2 + i, (String) nowRow.get("staff"));
                if (nowRow.containsKey("billValue")) {
                    //jx.setCellValue(23, row2+i, (Integer)nowRow.get("billValue"));
                    jx.setCellValue(21, row2 + i, (Integer) nowRow.get("billValue"));
                }
                cashSum2 += (Integer) nowRow.get("cash");

                // 20170906 #22950 add start
                // ** ���l���Ƀ|�C���g�o�� **
                String strPoint = "";
                int shopPoint = 0;
                int totalDiscountVal = 0;
                int webPoint = 0;
                if (nowRow.containsKey("usePoint")) {
                    shopPoint = (int) nowRow.get("usePoint");
                }
                if (nowRow.containsKey("totalDetailDiscountValue")) {
                    totalDiscountVal = (int) nowRow.get("totalDetailDiscountValue");
                }
                webPoint = totalDiscountVal - shopPoint;
                if (shopPoint > 0 && webPoint > 0) {
                    strPoint = shopPoint + "P/\nw" + webPoint + "P";
                } else if (shopPoint > 0) {
                    strPoint = shopPoint + "P";
                } else if (webPoint > 0) {
                    strPoint = "w" + webPoint + "P";
                }
                jx.setCellValue(17, row2 + i, strPoint);
                // 20170906 #22950 add end
            }
            //jx.setCellValue(22, row2+i, nowRow.get("staff").toString());
            jx.setCellValue(20, row2 + i, nowRow.get("staff").toString());
            tmpSlip = (Integer) nowRow.get("slip_no");

            // 20170906 #22950 add start �Z�p�N���[����Ԏ��ɂ���
            int div = 0;
            if (nowRow.containsKey("totalDetailDiscountValue")) {
                div = (int) nowRow.get("productDivision");
            }
            if (div == 3) {
                jx.setCellColor(8, row2 + i, HSSFColor.RED.index);
                jx.setCellColor(10, row2 + i, HSSFColor.RED.index);
            }
            // 20170906 #22950 add end �Z�p�N���[����Ԏ��ɂ���
        }
        //jx.setCellValue(20, 35+addCnt+addCnt2, cashSum + cashSum2);
        jx.setCellValue(18, 35 + addCnt + addCnt2, cashSum + cashSum2);
        //�o�����e
        int row3 = 38 + addCnt + addCnt2;
        int r3Sum = 0;
        for (int i = 0; i < outList.size(); i++) {
            Map<String, Object> nowRow = outList.get(i);
            jx.setCellValue(11, row3 + i, (String) nowRow.get("use_for"));
            //jx.setCellValue(21, row3+i, (nowRow.get("staff")!= null)?(String)nowRow.get("staff"): "");
            jx.setCellValue(19, row3 + i, (nowRow.get("staff") != null) ? (String) nowRow.get("staff") : "");
            //jx.setCellValue(23, row3+i, (Integer)nowRow.get("io_value"));
            jx.setCellValue(21, row3 + i, (Integer) nowRow.get("io_value"));
            r3Sum += (Integer) nowRow.get("io_value");
        }
        //jx.setCellValue(21, 44 + addCnt + addCnt2, r3Sum);
        jx.setCellValue(19, 44 + addCnt + addCnt2, r3Sum);
        //�O�����W
        jx.setCellValue(6, 45 + addCnt + addCnt2, regStaff);
        jx.setCellValue(6, 37 + addCnt + addCnt2, regCash);
        jx.setCellValue(6, 39 + addCnt + addCnt2, cashSum + cashSum2);
        jx.setCellValue(6, 41 + addCnt + addCnt2, cashSum + cashSum2 + regCash);

        jx.setCellValue(8, 44 + addCnt + addCnt2, cashSum + cashSum2 + regCash - r3Sum);

        jx.openWorkbook();
        return true;
    }

	    /**
     * �y���ŗ���ǂݍ���
     * @return
     */
    public MstTaxs getTaxs(java.util.Date date) {
        MstTaxs taxs = new MstTaxs();

        try {
            taxs.loadTaxs(SystemInfo.getConnection(), date);
            MstTax tax = new MstTax();
            tax.setTaxRate(SystemInfo.getTaxRate(date));
            java.util.Date applyDate = (taxs.size() > 0 && taxs.get(0).getApplyDate() != null) ? taxs.get(0).getApplyDate() : date;
            tax.setApplyDate(applyDate);
            taxs.add(0, tax);
        } catch (SQLException ex) {
            Logger.getLogger(ModestyReportLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return taxs;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.EstheticInWeekItemBean;
import com.geobeck.sosia.pos.hair.report.beans.WeeklyEstheticBean;
import com.geobeck.sosia.pos.hair.report.beans.WeeklyEstheticGroupBean;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.DateUtil;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 週で見るエステ推移
 *
 * @author IVS
 */
public class EstheticTransitionInWeekLogic extends ReportCustomize {

    //店舗
    protected String shopIDList;
    //出力対象
    protected OutputType outputType;

    public enum OutputType {
        SHOP, STAFF
    }

    public EstheticTransitionInWeekLogic(String shopIDList, Integer year, Integer month, OutputType outputType) {
        this.shopIDList = shopIDList;
        this.year = year;
        this.month = month;
        this.outputType = outputType;
    }

    @Override
    public boolean report() {
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getReportSQL());

            LinkedHashMap<Integer, WeeklyEstheticGroupBean> customizeMapItems = new LinkedHashMap<>();
            while (rs.next()) {

                WeeklyEstheticGroupBean customizeItem = new WeeklyEstheticGroupBean();
                int customizeId;
                if (this.outputType == OutputType.SHOP) {
                    customizeId = rs.getInt("shop_id");
                    if (customizeMapItems.containsKey(customizeId)) {
                        customizeItem = customizeMapItems.get(customizeId);
                    } else {
                        customizeItem.setId(customizeId);
                        customizeItem.setName(rs.getString("shop_name"));
                    }
                } else {
                    customizeId = rs.getInt("staff_id");
                    if (customizeMapItems.containsKey(customizeId)) {
                        customizeItem = customizeMapItems.get(customizeId);
                    } else {
                        customizeItem.setId(customizeId);
                        customizeItem.setName(rs.getString("staffname"));
                    }
                }

                WeeklyEstheticBean weekItem = new WeeklyEstheticBean();
                weekItem.setFirstDayOfWeek(rs.getString("lable_weekly"));

                EstheticInWeekItemBean bean = new EstheticInWeekItemBean();
                bean.setBeautyTreatmentNum(rs.getInt("beauty_treatment_num"));
                bean.setVisitNum(rs.getInt("visit_num"));

                weekItem.setItem(bean);

                customizeItem.getWeekItems().add(weekItem);
                customizeMapItems.put(customizeId, customizeItem);
            }
            setLastWeekOfMonth(customizeMapItems);
            fillData(customizeMapItems);
        } catch (SQLException ex) {
            Logger.getLogger(EstheticTransitionInWeekLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    /**
     * そのため第５週目だけ 営業日日数が７日以上になる可能性がある。
     *
     * @param customizeMapItems
     */
    private void setLastWeekOfMonth(LinkedHashMap<Integer, WeeklyEstheticGroupBean> customizeMapItems) {

        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            WeeklyEstheticGroupBean group = customizeMapItems.get(key);
            if (group != null && group.getWeekItems().size() > 5) {
                EstheticInWeekItemBean fiveWeekOfMonth = group.getWeekItems().get(5).getItem();
                EstheticInWeekItemBean lastWeekOfMonth = group.getWeekItems().get(6).getItem();

                fiveWeekOfMonth.setVisitNum(fiveWeekOfMonth.getVisitNum() + lastWeekOfMonth.getVisitNum());
                fiveWeekOfMonth.setBeautyTreatmentNum(fiveWeekOfMonth.getBeautyTreatmentNum() + lastWeekOfMonth.getBeautyTreatmentNum());

                group.getWeekItems().remove(lastWeekOfMonth);
            }
        }
    }

    /**
     * fill data
     *
     * @param customizeMapItems
     */
    private void fillData(LinkedHashMap<Integer, WeeklyEstheticGroupBean> customizeMapItems) {

        String fileName = "週で見るエステ推移";
        JExcelApi jx = new JExcelApi(fileName);
        //テンプレートとなるファイルをセット
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        
        jx.getTargetSheet().setName(this.outputType == OutputType.SHOP ? "週間エステ推移_店舗" : "週間エステ推移_スタッフ");

        //出力対象月
        jx.setValue(2, 1, this.year);
        jx.setValue(3, 1, this.month);

        int rowFill = 6;
        // 追加行数セット
        jx.insertRow(rowFill,  customizeMapItems.size() - 1);
        
        ArrayList<String> mondays = getMondayOfMonth(this.calendarFor());

        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            WeeklyEstheticGroupBean customizeItem = customizeMapItems.get(key);

            jx.setValue(1, rowFill, customizeItem.getName());
            int colVisitNum  = 2;
            int colBeautyNum = 3;
            int visitNum = 0;
            int beautyNum = 0;
            int weekCnt = 0;
            for(String week : mondays) {
                for(WeeklyEstheticBean bean : customizeItem.getWeekItems()){
                    if(bean.getFirstDayOfWeek() != null && bean.getFirstDayOfWeek().contains(week)) {
                        visitNum = visitNum + bean.getItem().getVisitNum();
                        beautyNum = beautyNum + bean.getItem().getBeautyTreatmentNum();
                        jx.setValue(colVisitNum, rowFill, visitNum);
                        jx.setValue(colBeautyNum, rowFill, beautyNum);
                        break;
                    } else {
                        jx.setValue(colVisitNum, rowFill, visitNum);
                        jx.setValue(colBeautyNum, rowFill, beautyNum);
                    }
                }
                weekCnt++;
                if (weekCnt < 5) {
                colVisitNum = colVisitNum  + 3;
                colBeautyNum= colBeautyNum + 3;
                }
            }
            rowFill ++;
        }
        jx.openWorkbook();
    }

    @Override
    public String getReportSQL() {

        StringBuilder sql = new StringBuilder(1000);

        sql.append("SELECT                                                                                      ");
        if (OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_name1 || ' ' || mstaff.staff_name2 AS staffname,                          ");
        }else {
            sql.append(" ms.shop_name,                                                                          ");
        }
        sql.append(" ds.*                                                                                       ");
        
        sql.append(" FROM                                                                                       ");
        if (OutputType.STAFF == outputType) {
            sql.append(" mst_staff mstaff                                                                       ");
        } else {
            sql.append(" mst_shop ms                                                                            ");
        }
        sql.append(" INNER JOIN (                                                                               ");
        sql.append(" SELECT                                                                                     ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ds.staff_id,                                                                           ");
        } else {
            sql.append(" ds.shop_id,                                                                            ");
        }
        sql.append("       date_part('year', ds.sales_date) AS lable_year,                                      ");
        sql.append("       date_part('month', ds.sales_date) AS lable_month,                                    ");
        sql.append("       TO_CHAR(CASE            ");
        sql.append("       WHEN date_trunc('week', ds.sales_date) < date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime())).append(" THEN date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime()));
        sql.append("       ELSE date_trunc('week', ds.sales_date)                                               ");
        sql.append("       END , 'YYYY/MM/DD') AS lable_weekly,                                                 ");
        sql.append("count(DISTINCT ds.slip_no) AS visit_num,                                                    ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN EXISTS                                                               ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND dsd.product_division in (1, 3)                              ");
        sql.append("                            AND mst.technic_class_id = 5 ) THEN ds.slip_no                  ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS beauty_treatment_num                                                  ");
        sql.append(" FROM                                                                                       ");
        sql.append(" view_data_sales_detail_valid ds                                                            ");
        sql.append("  WHERE ds.sales_date BETWEEN date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime())).append(" AND date").append(SQLUtil.convertForSQLDateOnly(DateUtil.getLastDate(this.calendarFor())));
        sql.append("  AND ds.product_division in (1, 3)                                                         ");
        sql.append(" GROUP BY                                                                                   ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ds.staff_id,                                                                           ");
        } else {
            sql.append(" ds.shop_id,                                                                            ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month,                                                                               ");
        sql.append(" lable_weekly                                                                               ");
        sql.append(" ORDER BY                                                                                   ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ds.staff_id,                                                                           ");
        }else {
            sql.append(" ds.shop_id,                                                                            ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month,                                                                               ");
        sql.append(" lable_weekly                                                                               ");
        sql.append(" ) ds ON                                                                                    ");
        if (OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_id = ds.staff_id                                                          ");
        }else {
            sql.append(" ms.shop_id = ds.shop_id                                                                ");
        }
        if (OutputType.SHOP == outputType) {
            sql.append(" WHERE                                                                                  ");
            sql.append(" ms.delete_date IS NULL                                                                 ");
        }
        sql.append(" ORDER BY                                                                                   ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ds.staff_id,                                                                           ");
        }else {
            sql.append(" ds.shop_id,                                                                            ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month,                                                                               ");
        sql.append(" lable_weekly                                                                               ");


        return sql.toString();
    }

    /**
     * Set Date
     *
     * @return
     */
    private Calendar calendarFor() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.year);
        cal.set(Calendar.MONTH, (this.month - 1));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        return cal;
    }
    
    private ArrayList<String> getMondayOfMonth(java.util.Calendar cal) {
        if (cal == null) {
            return null;
        }
        String pattern = "yyyy/MM/dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Calendar scal = cal;
        Calendar ecal = DateUtil.getLastDate(cal);

        ArrayList<String> mondaysDates = new ArrayList<>();

        while (scal.before(ecal) || scal.equals(ecal)) {
            if (scal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                //get date is monday
                mondaysDates.add(df.format(scal.getTime()));
            }else if(scal.get(Calendar.DAY_OF_MONTH) == 1){
                //get date is first day of the month
                mondaysDates.add(df.format(scal.getTime()));
            }
            scal.add(Calendar.DATE, 1);
        }

        return mondaysDates;
    }
}

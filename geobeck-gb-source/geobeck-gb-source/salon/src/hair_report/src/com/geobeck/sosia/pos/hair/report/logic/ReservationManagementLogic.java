/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.CustomizeReservationBean;
import com.geobeck.sosia.pos.hair.report.beans.MonthReservationBean;
import com.geobeck.sosia.pos.hair.report.beans.ReservationManagementBean;
import com.geobeck.sosia.pos.hair.report.util.JPOIApiSaleTransittion;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.util.LinkedHashMap;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 予約管理
 * @author IVS
 */
public class ReservationManagementLogic extends ReportCustomize {
    
    //店舗
    protected String shopIDList;
    protected String targetStores;
    protected String targetName;
    //出力対象
    protected OutputType outputType;
    public enum OutputType {
        SHOP, STAFF
    }

    public ReservationManagementLogic(String shopIDList, String targetStores, Integer year, Integer month, OutputType outputType) {
        this.shopIDList     = shopIDList;
        this.targetStores   = targetStores;
        this.year           = year;
        this.month          = month;
        this.outputType     = outputType;
    }

    @Override
    public boolean report() {
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getReportSQL());
            
            LinkedHashMap<Integer, CustomizeReservationBean> customizeMapItems = new LinkedHashMap<>();
             while (rs.next()) {
                 
                 CustomizeReservationBean customizeItem = new CustomizeReservationBean();
                 int customizeId;

                customizeId = rs.getInt("ms_id");
                 if(customizeMapItems.containsKey(customizeId)) {
                    customizeItem = customizeMapItems.get(customizeId);
                } else {
                    customizeItem.setId(customizeId);
                    customizeItem.setName(rs.getString("ms_name"));
                }
                 
                MonthReservationBean monthItem = new MonthReservationBean();
                monthItem.setYearLabel(rs.getInt("lable_year"));
                monthItem.setMonthLabel(rs.getInt("lable_month"));

                ReservationManagementBean item = new ReservationManagementBean();
                //予約数
                item.setReservationNum(rs.getInt("reservation_num"));
                //総客数
                item.setCustomerTotalNum(rs.getInt("customer_total_num"));
                //新予約
                item.setNewReservation(rs.getInt("new_reservation"));
                //離脱
                item.setWithdrawal(rs.getInt("withdrawal"));

                monthItem.setItem(item);
                customizeItem.getMonthItems().add(monthItem);
                if ((OutputType.SHOP == outputType && customizeMapItems.size() >= LIMIT_SHOP)
                         || (OutputType.STAFF == outputType && customizeMapItems.size() >= LIMIT_STAFF)) {
                    if (!customizeMapItems.containsKey(customizeId)) {
                        continue;
                    }
                }
                customizeMapItems.put(customizeId, customizeItem);
            }
            fillData(customizeMapItems);
        } catch (SQLException ex) {
            Logger.getLogger(ReservationManagementLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * fill data
     * @param customizeMapItems 
     */
    private void fillData(LinkedHashMap<Integer, CustomizeReservationBean> customizeMapItems) {
  
        String fileName = this.outputType == OutputType.SHOP ? "予約管理（店舗）" : "予約管理（スタッフ）";
        JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion(fileName);
        //テンプレートとなるファイルをセット
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        
        //出力対象月
        jx.setCellValue(3, 3, this.year);
        jx.setCellValue(6, 3, this.month);
        //対象店舗
        if (this.outputType == OutputType.STAFF) {
            jx.setCellValue(3, 4, this.targetStores);
            jx.setCellValue(2, 8, this.targetStores);
        }

        int row = 9;
        
        //list month
        List<Integer> months    = getMonths(this.month);
        
        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {
            CustomizeReservationBean customizeItem = customizeMapItems.get(key);

            int colReservationNum = 3;
            int colCustomerTotalNum = 4;
            int colNewReservation = 6;
            int colWithdrawal = 9;

            jx.setCellValue(2, row, customizeItem.getName());
            int stepMonth  = 7;
            for(Integer itemMonth : months) {
                MonthReservationBean item = null;
                for(MonthReservationBean itemTemp : customizeItem.getMonthItems()) {
                    if(itemMonth.equals(itemTemp.getMonthLabel())) {
                        item = itemTemp;
                        break;
                    }
                }

                if(item != null) {
                    jx.setCellValue(colReservationNum, row, item.getItem().getReservationNum());
                    jx.setCellValue(colCustomerTotalNum, row, item.getItem().getCustomerTotalNum());
                    jx.setCellValue(colNewReservation, row, item.getItem().getNewReservation());
                    jx.setCellValue(colWithdrawal, row, item.getItem().getWithdrawal());
                }

                colReservationNum = colReservationNum + stepMonth;
                colCustomerTotalNum = colCustomerTotalNum + stepMonth;
                colNewReservation = colNewReservation + stepMonth;
                colWithdrawal = colWithdrawal + stepMonth;
            }

            row++;
        }

        jx.setFormularActive();
        jx.openWorkbook();
    }

    @Override
    public String getReportSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        if (OutputType.STAFF == outputType) {
            sql.append(" SELECT ms.staff_id AS ms_id, ");
            sql.append(" ms.staff_name1 || ' ' || ms.staff_name2 AS ms_name, ");
        } else {
            sql.append("SELECT ms.shop_id AS ms_id, ");
            sql.append("       ms.shop_name AS ms_name, ");
        }
        sql.append("       date_part('year', ds.sales_date) AS lable_year,  ");
        sql.append("       date_part('month', ds.sales_date) AS lable_month, ");        
        sql.append("       count(DISTINCT CASE   ");
        sql.append("               WHEN (ds.product_division in (1) ");
        sql.append("                     AND mst.technic_id in (121, 122)) THEN  ds.slip_no ");
        sql.append("               ELSE NULL ");
        sql.append("           END) AS reservation_num, ");
        sql.append("       count(DISTINCT CASE   ");
        sql.append("               WHEN ds.product_division in (1, 3) ");
        sql.append("               THEN  ds.slip_no ");
        sql.append("               ELSE NULL ");
        sql.append("               END) AS customer_total_num, ");
        sql.append("       count(DISTINCT CASE   ");
        sql.append("               WHEN (ds.product_division in (1) ");
        sql.append("                     AND mst.technic_id in (122)) THEN  ds.slip_no ");
        sql.append("               ELSE NULL ");
        sql.append("           END) AS new_reservation, ");
        sql.append("       count(DISTINCT CASE   ");
        sql.append("               WHEN (ds.product_division in (1) ");
        sql.append("                     AND mst.technic_id in (131)) THEN  ds.slip_no ");
        sql.append("               ELSE NULL ");
        sql.append("           END) AS withdrawal  ");
        sql.append(" FROM   ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mst_staff ms ");
        } else {
            sql.append(" mst_shop ms ");
        }
        sql.append(" INNER JOIN view_data_sales_detail_valid ds ON ds.shop_id in (").append(shopIDList).append(")  ");
        sql.append("  AND ds.sales_date BETWEEN date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime())).append(" AND date").append(SQLUtil.convertForSQLDateOnly(this.getLastYear(this.calendarFor()).getTime())).append(" + interval '1 year' ");
        if (OutputType.STAFF == outputType) {
            sql.append("  AND ms.staff_id = ds.staff_id ");
        } else {
            sql.append("  AND ms.shop_id = ds.shop_id ");
            sql.append("  AND ms.delete_date IS NULL ");
        }
        sql.append(" LEFT JOIN mst_technic mst ON ds.product_id = mst.technic_id  AND mst.delete_date IS NULL  ");
        if (OutputType.SHOP == outputType) {
            sql.append(" WHERE ms.shop_id in (").append(shopIDList).append(") ");
        }
        sql.append(" GROUP BY ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ms.staff_id, ");
            sql.append(" ms.staff_name1, ");
            sql.append(" ms.staff_name2, ");
        } else {
            sql.append(" ms.shop_id, ");
            sql.append(" ms.shop_name, ");
        }
        sql.append(" lable_year,   ");
        sql.append(" lable_month   ");
        sql.append(" ORDER BY ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ms.staff_id, ");
        } else {
            sql.append("  ms.shop_id, ");
        }
        sql.append(" lable_year,   ");
        sql.append(" lable_month   ");
        
        return sql.toString();
    }
    
    /**
     * get months
     * @param startMonth
     * @return 
     */
    private List<Integer> getMonths(int startMonth) {
        
        List<Integer> months = new ArrayList<>();
        int indxMonth = startMonth;
        months.add(startMonth);
        
        if (indxMonth == 12) {
            indxMonth = 0;
        }
        
        for (int i= 1; i < 12; i ++) {
            months.add(indxMonth + 1);
            indxMonth ++;
            
            if (indxMonth == 12) {
                indxMonth = 0;
            }
        }
        
        return months;
    }
    
    /**
     * Set Date
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
    
    private Calendar getLastYear(java.util.Calendar cal)
    {
        if (cal == null) {
            return null;
        }

        java.util.Calendar temp = java.util.Calendar.getInstance();
        temp.setTime(cal.getTime());

        temp.set(java.util.Calendar.DAY_OF_MONTH, 1);
        temp.add(java.util.Calendar.DAY_OF_MONTH, -1);

        return temp;
    }
}

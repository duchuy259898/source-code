/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IVS
 */
public class WeeklyPurchasingLogic extends ReportCustomize {

    protected List<Map<String, String>> weeklyList;
    protected String dataYM;

    //èoóÕëŒè€
    protected OutputType outputType;
    public enum OutputType {
        SHOP, STAFF
    }
    
    public WeeklyPurchasingLogic(Integer year, Integer month, OutputType outputType, List<Map<String, String>> weeklyList) {
        this.year = year;
        this.month = month;
        this.outputType = outputType;
        this.weeklyList = weeklyList;
        this.dataYM = weeklyList.get(0).get("stOfWeek").substring(0, 6);
    }
    
    @Override
    public boolean report() {
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getReportSQL());
            String ms_id = "";
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            
            while (rs.next()) {
                if (!ms_id.equals(String.valueOf(rs.getInt("ms_id")))) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("msname", rs.getString("ms_name"));
                    
                    for (int i = 0; i < weeklyList.size(); i++) {
                        if (rs.getString("sales_date").compareTo(weeklyList.get(i).get("stOfWeek")) >= 0
                            && weeklyList.get(i).get("edOfWeek").compareTo(rs.getString("sales_date")) >= 0) {
                            item.put("visitornum" + String.valueOf(i) , rs.getInt("visitor_num"));
                            item.put("purchasernum" + String.valueOf(i) , rs.getInt("purchaser_num"));
                        } else {
                            item.put("visitornum" + String.valueOf(i) , 0);
                            item.put("purchasernum" + String.valueOf(i) , 0);
                        }
                    }

                    dataList.add(item);
                } else {
                    for (int i = 0; i < weeklyList.size(); i++) {
                        if (rs.getString("sales_date").compareTo(weeklyList.get(i).get("stOfWeek")) >= 0
                            && weeklyList.get(i).get("edOfWeek").compareTo(rs.getString("sales_date")) >= 0) {
                            int dataIndex = dataList.size() - 1;
                            int visitorNum = (int)dataList.get(dataIndex).get("visitornum" + String.valueOf(i));
                            int purchaserNum = (int)dataList.get(dataIndex).get("purchasernum" + String.valueOf(i));

                            dataList.get(dataIndex).put("visitornum" + String.valueOf(i), visitorNum + rs.getInt("visitor_num"));
                            dataList.get(dataIndex).put("purchasernum" + String.valueOf(i), purchaserNum + rs.getInt("purchaser_num"));
                            
                            break;
                        }
                    }
                }
                
                ms_id = String.valueOf(rs.getInt("ms_id"));
            }
           
            fillData(dataList);
        } catch (SQLException ex) {
            Logger.getLogger(WeeklyPurchasingLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * fill data
     * @param customizeMapItems 
     */
    private void fillData(List<Map<String, Object>> dataList) {
        String sheetName = this.outputType == OutputType.STAFF ? "èTä‘çwîÉêÑà⁄_ÉXÉ^ÉbÉt" : "èTä‘çwîÉêÑà⁄_ìXï‹";
        
        JExcelApi jx = new JExcelApi("èTÇ≈å©ÇÈçwîÉêÑà⁄");
        jx.setTemplateFile("/reports/èTÇ≈å©ÇÈçwîÉêÑà⁄.xls");
        jx.getTargetSheet().setName(sheetName);
        
        jx.setValue(2, 1, year);
        jx.setValue(3, 1, month + 1);

        int row = 6;
        
        if (dataList.size() > 0) {
            jx.insertRow(row, dataList.size() - 1);
        }
        
        for (Map<String, Object> data : dataList) {
            int totalVisitorNum = 0;
            int totalPurchasernum = 0;
            jx.setValue(1, row, data.get("msname"));
            for (int i = 0; i < weeklyList.size(); i++) {
                int visitorNum = (int)data.get("visitornum" + String.valueOf(i));
                int purchaserNum = (int)data.get("purchasernum" + String.valueOf(i));
                totalVisitorNum = visitorNum + totalVisitorNum;
                totalPurchasernum = purchaserNum + totalPurchasernum;
                jx.setValue(2+(i*3), row, totalVisitorNum);
                jx.setValue(3+(i*3), row, totalPurchasernum);
            }
            
            row++;
        }

        jx.openWorkbook();
    }

    @Override
    public String getReportSQL() {
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" SELECT ");
        if (OutputType.STAFF == outputType) {
            sql.append("  ms.staff_id AS ms_id ");
            sql.append("  , ms.staff_name1 || ' ' || ms.staff_name2 AS ms_name ");
        } else {
            sql.append("  ms.shop_id as ms_id ");
            sql.append("  , ms.shop_name as ms_name ");
        }        
        sql.append("      , ds.sales_date ");
        sql.append("      , ds.visitor_num ");
        sql.append("      , ds.purchaser_num");
        sql.append(" FROM ");
        if (OutputType.STAFF == outputType) {
            sql.append(" mst_staff ms ");
        } else {
            sql.append(" mst_shop ms ");
        }
        sql.append(" INNER JOIN ");
        sql.append(" ( ");
        sql.append("      SELECT ");
        if (OutputType.STAFF == outputType) {
            sql.append("           ds.staff_id ");
        } else {
            sql.append("           ds.shop_id ");
        }
        sql.append("           , to_char(ds.sales_date, 'yyyymmdd') AS sales_date ");
        sql.append("           , count(DISTINCT ds.slip_no) AS visitor_num ");
        sql.append("           , count(DISTINCT CASE ");
        sql.append("                   WHEN ds.product_division in (2) THEN ds.slip_no ");
        sql.append("                   ELSE NULL ");
        sql.append("                   END) AS purchaser_num ");
        sql.append("      FROM view_data_sales_detail_valid ds ");
        sql.append("      WHERE to_char(ds.sales_date, 'yyyymm') like '").append(dataYM).append("' ");
        sql.append("      GROUP BY ds.shop_id, sales_date ");
        if (OutputType.STAFF == outputType) {
            sql.append("        , ds.staff_id ");
        }
        sql.append(" ) ds ");
        if (OutputType.STAFF == outputType) {
            sql.append(" ON ms.staff_id = ds.staff_id ");
            sql.append(" ORDER BY ms.staff_id ");
        } else {
            sql.append(" ON ms.shop_id = ds.shop_id ");
            sql.append(" AND ms.delete_date IS NULL ");
            sql.append(" ORDER BY ms.shop_id ");
        }
        sql.append(" , ds.sales_date");

        return sql.toString();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.MonthProductPurchaseBean;
import com.geobeck.sosia.pos.hair.report.beans.ProductPurchaseBean;
import com.geobeck.sosia.pos.hair.report.beans.StaffProductPurchaseBean;
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
 * スタッフ別商品購買内訳
 * @author IVS
 */
public class ProductPurchaseByStaffLogic extends ReportCustomize {
    
    //店舗
    protected String shopIDList;
    protected String targetStores;
    protected String targetName;


    public ProductPurchaseByStaffLogic(String shopIDList, String targetStores, Integer year, Integer month) {
        this.shopIDList     = shopIDList;
        this.targetStores   = targetStores;
        this.year           = year;
        this.month          = month;
    }

    @Override
    public boolean report() {
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getReportSQL());

            LinkedHashMap<Integer, StaffProductPurchaseBean> customizeMapItems = new LinkedHashMap<>();
             while (rs.next()) {
                 
                 StaffProductPurchaseBean customizeItem = new StaffProductPurchaseBean();
                 int customizeId;

                customizeId = rs.getInt("staff_id");
                 if(customizeMapItems.containsKey(customizeId)) {
                    customizeItem = customizeMapItems.get(customizeId);
                } else {
                    customizeItem.setId(customizeId);
                    customizeItem.setName(rs.getString("staffname"));
                }
                 
                 MonthProductPurchaseBean monthItem = new MonthProductPurchaseBean();
                 monthItem.setYearLabel(rs.getInt("lable_year"));
                 monthItem.setMonthLabel(rs.getInt("lable_month"));
                 
                 ProductPurchaseBean item = new ProductPurchaseBean();
                 //毛髪ケア-金額
                 item.setHairCareValue(rs.getInt("hair_care_value"));
                 //毛髪ケア-人数
                 item.setHairCareNum(rs.getInt("hair_care_num"));
                 //頭皮ケア-金額
                 item.setScalpCareValue(rs.getInt("scalp_care_value"));
                 //頭皮ケア-人数
                 item.setScalpCareNum(rs.getInt("scalp_care_num"));
                 //美容機器-金額
                 item.setBeautyValue(rs.getInt("beauty_value"));
                 //美容機器-人数
                 item.setBeautyNum(rs.getInt("beauty_num"));
                 //スタイ-金額
                 item.setBibValue(rs.getInt("bib_value"));
                 //スタイ-人数
                 item.setBibNum(rs.getInt("bib_num"));
                 
                 monthItem.setItem(item);
                 customizeItem.getMonthItems().add(monthItem);
                 if(customizeMapItems.size() >= LIMIT_STAFF){
                     if(!customizeMapItems.containsKey(customizeId)){
                         continue;
                     }
                 }
                 customizeMapItems.put(customizeId, customizeItem);
             }
             fillData(customizeMapItems);
        } catch (SQLException ex) {
            Logger.getLogger(ProductPurchaseByStaffLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * fill data
     * @param customizeMapItems 
     */
    private void fillData(LinkedHashMap<Integer, StaffProductPurchaseBean> customizeMapItems) {
        
        String fileName = "スタッフ別商品購買内訳";
        JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion(fileName);
        //テンプレートとなるファイルをセット
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        
        //出力対象月
        jx.setCellValue(4, 3, this.year);
        jx.setCellValue(5, 3, this.month);
        //対象店舗
        jx.setCellValue(4, 4, this.targetStores);
        int stepRowCustomize    = 4;
        for ( int i = (customizeMapItems.size() *stepRowCustomize) + 8; i <=jx.getSheet().getLastRowNum(); i ++) {
            jx.getSheet().getRow(i).setZeroHeight(true);
        }
        int rowName             = 9;
        //毛髪ケア
        int rowHairCare         = 9;
        //頭皮ケア
        int rowScalpCareIdx     = 10;
        //美容機器
        int rowBeautyIdx        = 11;
        //スタイ
        int rowBibIdx           = 12;
        
        //list month
        List<Integer> months    = getMonths(this.month);
        
        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            StaffProductPurchaseBean customizeItem = customizeMapItems.get(key);
            
            int colValue   = 4;
            int colNum     = 5;
            
            jx.setCellValue(2, rowName, customizeItem.getName());
            int stepMonth  = 2;
            for(Integer itemMonth : months) {
                MonthProductPurchaseBean item = null;
                for(MonthProductPurchaseBean itemTemp : customizeItem.getMonthItems()) {
                    if(itemMonth.equals(itemTemp.getMonthLabel())) {
                        item = itemTemp;
                        break;
                    }
                }
                
                if(item != null) {
                    jx.setCellValue(colValue, rowHairCare, item.getItem().getHairCareValue());
                    jx.setCellValue(colNum, rowHairCare, item.getItem().getHairCareNum());
                    jx.setCellValue(colValue, rowScalpCareIdx, item.getItem().getScalpCareValue());
                    jx.setCellValue(colNum, rowScalpCareIdx, item.getItem().getScalpCareNum());
                    jx.setCellValue(colValue, rowBeautyIdx, item.getItem().getBeautyValue());
                    jx.setCellValue(colNum, rowBeautyIdx, item.getItem().getBeautyNum());
                    jx.setCellValue(colValue, rowBibIdx, item.getItem().getBibValue());
                    jx.setCellValue(colNum, rowBibIdx, item.getItem().getBibNum());
                }else {
                    jx.setCellValue(colValue, rowHairCare, 0);
                    jx.setCellValue(colNum, rowHairCare, 0);
                    jx.setCellValue(colValue, rowScalpCareIdx, 0);
                    jx.setCellValue(colNum, rowScalpCareIdx, 0);
                    jx.setCellValue(colValue, rowBeautyIdx, 0);
                    jx.setCellValue(colNum, rowBeautyIdx, 0);
                    jx.setCellValue(colValue, rowBibIdx, 0);
                    jx.setCellValue(colNum, rowBibIdx, 0);
                }
                colValue   = colValue + stepMonth;
                colNum     = colNum   + stepMonth;
            }
            
            rowName             = rowName           + stepRowCustomize;
            rowHairCare         = rowHairCare       + stepRowCustomize;
            rowScalpCareIdx     = rowScalpCareIdx   + stepRowCustomize;
            rowBeautyIdx        = rowBeautyIdx      + stepRowCustomize;
            rowBibIdx           = rowBibIdx         + stepRowCustomize;
        }
        jx.setFormularActive();
        jx.openWorkbook();
    }

    @Override
    public String getReportSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append("SELECT ms.shop_id,                                                                          ");
        sql.append("       ms.shop_name,                                                                        ");
        sql.append(" mstaff.staff_id,                                                                           ");
        sql.append(" mstaff.staff_name1 || ' ' || mstaff.staff_name2 AS staffname,                              ");
        sql.append(" date_part('year', ds.sales_date) AS lable_year,                                            ");
        sql.append(" date_part('month', ds.sales_date) AS lable_month,                                          ");
        sql.append(" coalesce(sum(CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                            ");
        sql.append(" 					 (SELECT 1                                              ");
        sql.append(" 					  FROM data_sales_detail dsd                            ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                ");
        sql.append(" 						AND msi.item_class_id in(1, 2, 18)) THEN ds.discount_detail_value_no_tax ");
        sql.append(" 			ELSE 0                                                                    ");
        sql.append(" 		END), 0) AS hair_care_value,                                                      ");
        sql.append(" count(DISTINCT CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                              ");
        sql.append(" 					 (SELECT 1                                                ");
        sql.append(" 					  FROM data_sales_detail dsd                              ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                 ");
        sql.append(" 						AND msi.item_class_id in(1, 2, 18)) THEN ds.slip_no ");
        sql.append(" 			  ELSE NULL                                                                 ");
        sql.append(" 		END) AS hair_care_num,                                                              ");
        sql.append(" coalesce(sum(CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                            ");
        sql.append(" 					 (SELECT 1                                              ");
        sql.append(" 					  FROM data_sales_detail dsd                            ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                ");
        sql.append(" 						AND msi.item_class_id in(5)) THEN ds.discount_detail_value_no_tax ");
        sql.append(" 			ELSE 0                                                                    ");
        sql.append(" 		END), 0) AS scalp_care_value,                                                      ");
        sql.append(" count(DISTINCT CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                              ");
        sql.append(" 					 (SELECT 1                                                ");
        sql.append(" 					  FROM data_sales_detail dsd                              ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                 ");
        sql.append(" 						AND msi.item_class_id in(5)) THEN ds.slip_no ");
        sql.append(" 			  ELSE NULL                                                                 ");
        sql.append(" 		END) AS scalp_care_num,                                                             ");
        sql.append(" coalesce(sum(CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                            ");
        sql.append(" 					 (SELECT 1                                              ");
        sql.append(" 					  FROM data_sales_detail dsd                            ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                ");
        sql.append(" 						AND msi.item_class_id in(9)) THEN ds.discount_detail_value_no_tax ");
        sql.append(" 			ELSE 0                                                                    ");
        sql.append(" 		END), 0) AS beauty_value,                                                      ");
        sql.append(" count(DISTINCT CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                              ");
        sql.append(" 					 (SELECT 1                                                ");
        sql.append(" 					  FROM data_sales_detail dsd                              ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                 ");
        sql.append(" 						AND msi.item_class_id in(9)) THEN ds.slip_no ");
        sql.append(" 			  ELSE NULL                                                                ");
        sql.append(" 		END) AS beauty_num,                                                                ");
         sql.append(" coalesce(sum(CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                            ");
        sql.append(" 					 (SELECT 1                                              ");
        sql.append(" 					  FROM data_sales_detail dsd                            ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                ");
        sql.append(" 						AND msi.item_class_id not in(1, 2, 5, 9, 18)) THEN ds.discount_detail_value_no_tax ");
        sql.append(" 			ELSE 0                                                                    ");
        sql.append(" 		END), 0) AS bib_value,                                                      ");
        sql.append(" count(DISTINCT CASE                                                                          ");
        sql.append(" 			WHEN ds.product_division in(2, 4) AND EXISTS                              ");
        sql.append(" 					 (SELECT 1                                                ");
        sql.append(" 					  FROM data_sales_detail dsd                              ");
        sql.append(" 					  INNER JOIN mst_item msi ON msi.item_id = dsd.product_id ");
        sql.append(" 					  WHERE dsd.shop_id = ds.shop_id                          ");
        sql.append(" 						AND dsd.slip_no = ds.slip_no                      ");
        sql.append(" 						AND dsd.slip_detail_no = ds.slip_detail_no        ");
        sql.append(" 						AND dsd.delete_date IS NULL                       ");
        sql.append(" 						AND dsd.product_division in (2, 4)                 ");
        sql.append(" 						AND msi.item_class_id not in(1, 2, 5, 9, 18)) THEN ds.slip_no       ");
        sql.append(" 			  ELSE NULL                                                                ");
        sql.append(" 		END) AS bib_num                                                                    ");

        sql.append(" FROM                                                                                       ");
        sql.append(" mst_staff mstaff                                                                           ");
        sql.append(" INNER JOIN view_data_sales_detail_valid ds ON ds.staff_id = mstaff.staff_id                ");
        sql.append("  AND ds.sales_date BETWEEN date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime())).append(" AND date").append(SQLUtil.convertForSQLDateOnly(this.getLastYear(this.calendarFor()).getTime())).append(" + interval '1 year'");
        sql.append("  AND mstaff.staff_id = ds.staff_id                                                         ");
        sql.append("  AND ds.product_division in (2, 4)                                                         ");
        sql.append(" INNER JOIN mst_shop ms ON ms.shop_id = ds.shop_id                                          ");
        sql.append(" WHERE ms.shop_id in (").append(shopIDList).append(")                                       ");
        sql.append(" GROUP BY ms.shop_id,                                                                       ");
        sql.append("         ms.shop_name,                                                                      ");
        sql.append(" mstaff.staff_id,                                                                           ");
        sql.append(" mstaff.staff_name1,                                                                        ");
        sql.append(" mstaff.staff_name2,                                                                        ");
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month                                                                                ");
        sql.append(" ORDER BY ms.shop_id,                                                                       ");
        sql.append("         ms.shop_name,                                                                      ");
        sql.append(" mstaff.staff_id,                                                                           ");
        sql.append(" mstaff.staff_name1,                                                                        ");
        sql.append(" mstaff.staff_name2,                                                                        ");
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month                                                                                ");
        
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
        
        for(int i= 1; i < 12; i ++) {
            months.add(indxMonth + 1);
            indxMonth ++;
            
            if(indxMonth == 12) {
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
            if(cal == null)
            {
                    return	null;
            }

            java.util.Calendar	temp	=	java.util.Calendar.getInstance();
            temp.setTime(cal.getTime());

            temp.set(java.util.Calendar.DAY_OF_MONTH, 1);
            temp.add(java.util.Calendar.DAY_OF_MONTH, -1);

            return	temp;
    }
}

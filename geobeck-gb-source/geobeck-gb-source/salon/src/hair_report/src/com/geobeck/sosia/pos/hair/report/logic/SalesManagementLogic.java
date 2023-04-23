/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.CustomizeSalesBean;
import com.geobeck.sosia.pos.hair.report.beans.MonthSalesBean;
import com.geobeck.sosia.pos.hair.report.beans.SaleManagementBean;
import com.geobeck.sosia.pos.hair.report.util.JPOIApiSaleTransittion;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * îÑè„ä«óù
 * @author IVS
 */
public class SalesManagementLogic extends ReportCustomize {

    DecimalFormat df = new DecimalFormat("#.#");  
        //ìXï‹
    protected String shopIDList;
    protected String targetStores;
    protected String targetName;
       //èoóÕëŒè€
    protected OutputType outputType;
    public enum OutputType {
        SHOP, STAFF
    }

    public SalesManagementLogic(String shopIDList, String targetStores, Integer year, Integer month, OutputType outputType) {
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
            
            LinkedHashMap<Integer, CustomizeSalesBean> customizeMapItems = new LinkedHashMap<>();
             while (rs.next()) {
                 
                 CustomizeSalesBean customizeItem = new CustomizeSalesBean();
                 int customizeId;
                 if(this.outputType == OutputType.SHOP){
                     customizeId = rs.getInt("shop_id");
                     if(customizeMapItems.containsKey(customizeId)) {
                        customizeItem = customizeMapItems.get(customizeId);
                    } else {
                        customizeItem.setId(customizeId);
                        customizeItem.setName(rs.getString("shop_name"));
                    }
                 } else {
                    customizeId = rs.getInt("staff_id");
                     if(customizeMapItems.containsKey(customizeId)) {
                        customizeItem = customizeMapItems.get(customizeId);
                    } else {
                        customizeItem.setId(customizeId);
                        customizeItem.setName(rs.getString("staffname"));
                    }
                 }
                 
                 
                 MonthSalesBean monthItem = new MonthSalesBean();
                 monthItem.setYearLabel(rs.getInt("lable_year"));
                 monthItem.setMonthLabel(rs.getInt("lable_month"));
                 
                SaleManagementBean salesBean = new SaleManagementBean();
                salesBean.setTotalTechSales(rs.getInt("total_tech_sales"));
                salesBean.setTechNomination(rs.getInt("tech_nomination"));
                salesBean.setNewTech(rs.getInt("new_tech"));
                salesBean.setItemSales(rs.getInt("item_sales"));
                salesBean.setTotalNumCustomers(rs.getInt("total_num_customers"));
                salesBean.setTotalNumTechOfCustomers(rs.getInt("total_num_tech_of_customers"));
                salesBean.setDesignatedMainCharge(rs.getInt("designated_main_charge"));
                salesBean.setNewCustomer(rs.getInt("new_customer"));
                salesBean.setMale(rs.getInt("male"));
                salesBean.setMaleCount(rs.getInt("male_count"));
                salesBean.setFemale(rs.getInt("female"));
                salesBean.setFemaleCount(rs.getInt("female_count"));
                int totalPerson = salesBean.getMaleCount() + salesBean.getFemaleCount();
                if(totalPerson != 0) {
                    salesBean.setFemaleRate((salesBean.getFemaleCount()*1.0)/totalPerson);
                } else {
                    salesBean.setFemaleRate(0);
                }
                
                monthItem.setItem(salesBean);
                 
                 customizeItem.getMonthItems().add(monthItem);
                if((OutputType.SHOP == outputType && customizeMapItems.size() >= LIMIT_SHOP)
                         || (OutputType.STAFF == outputType && customizeMapItems.size() >= LIMIT_STAFF)){
                     if(!customizeMapItems.containsKey(customizeId)){
                         continue;
                     }
                 }
                 customizeMapItems.put(customizeId, customizeItem);
             }
             setTotalOfAllStores(customizeMapItems);
             fillData(customizeMapItems);
        } catch (SQLException ex) {
            Logger.getLogger(SalesManagementLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * ëSìXï‹çáåv
     * @param customizeMapItems 
     */
    private void setTotalOfAllStores(LinkedHashMap<Integer, CustomizeSalesBean> customizeMapItems) {
        
        CustomizeSalesBean shopTotal = new CustomizeSalesBean();
        shopTotal.setId(0);
        shopTotal.setName(this.outputType == OutputType.STAFF ? this.targetStores : "ëSìXï‹çáåv");
        
        //list month
        List<Integer> months    = getMonths(this.month);
        initShopTotal(shopTotal, months);
        
        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            CustomizeSalesBean shopItem = customizeMapItems.get(key);
            
            for(MonthSalesBean monthItem : shopTotal.getMonthItems()) {
                MonthSalesBean item = null;
                
                for(MonthSalesBean itemTemp : shopItem.getMonthItems()) {
                    if(itemTemp.getMonthLabel() == monthItem.getMonthLabel()) {
                        item = itemTemp;
                        break;
                    }
                }
                
                if(item != null) {

                    if (monthItem.getMonthLabel() == item.getMonthLabel()) {
                        monthItem.getItem().setTotalTechSales(monthItem.getItem().getTotalTechSales() + item.getItem().getTotalTechSales());
                        monthItem.getItem().setTechNomination(monthItem.getItem().getTechNomination() + item.getItem().getTechNomination());
                        monthItem.getItem().setNewTech(monthItem.getItem().getNewTech() + item.getItem().getNewTech());
                        monthItem.getItem().setItemSales(monthItem.getItem().getItemSales() + item.getItem().getItemSales());
                        monthItem.getItem().setTotalNumCustomers(monthItem.getItem().getTotalNumCustomers() + item.getItem().getTotalNumCustomers());
                        monthItem.getItem().setTotalNumTechOfCustomers(monthItem.getItem().getTotalNumTechOfCustomers() + item.getItem().getTotalNumTechOfCustomers());
                        monthItem.getItem().setDesignatedMainCharge(monthItem.getItem().getDesignatedMainCharge() + item.getItem().getDesignatedMainCharge());
                        monthItem.getItem().setNewCustomer(monthItem.getItem().getNewCustomer() + item.getItem().getNewCustomer());
                        monthItem.getItem().setMale(monthItem.getItem().getMale() + item.getItem().getMale());
                        monthItem.getItem().setMaleCount(monthItem.getItem().getMaleCount()+ item.getItem().getMaleCount());
                        monthItem.getItem().setFemale(monthItem.getItem().getFemale() + item.getItem().getFemale());
                        monthItem.getItem().setFemaleCount(monthItem.getItem().getFemaleCount()+ item.getItem().getFemaleCount());
                        int totalPerson = monthItem.getItem().getMaleCount() + monthItem.getItem().getFemaleCount();
                        if(totalPerson != 0) {
                            monthItem.getItem().setFemaleRate((monthItem.getItem().getFemaleCount()*1.0)/totalPerson);
                        } else {
                            monthItem.getItem().setFemaleRate(0);
                        }
                    }
                }
            }
        }
        LinkedHashMap<Integer, CustomizeSalesBean> newMap =(LinkedHashMap<Integer, CustomizeSalesBean>) customizeMapItems.clone();
        customizeMapItems.clear();
        customizeMapItems.put(shopTotal.getId(), shopTotal);
        customizeMapItems.putAll(newMap);
    }
    
    /**
     * fill data
     * @param customizeMapItems 
     */
    private void fillData(LinkedHashMap<Integer, CustomizeSalesBean> customizeMapItems) {
        
        String fileName = this.outputType == OutputType.SHOP ? "îÑè„ä«óùÅiìXï‹Åj":"îÑè„ä«óùÅiÉXÉ^ÉbÉtÅj";
        JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion(fileName);
        //ÉeÉìÉvÉåÅ[ÉgÇ∆Ç»ÇÈÉtÉ@ÉCÉãÇÉZÉbÉg
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        
        //èoóÕëŒè€åé
        jx.setCellValue(3, 3, this.year);
        jx.setCellValue(4, 3, this.month);
        //ëŒè€ìXï‹
        jx.setCellValue(3, 4, this.targetStores);
        //ê≈ãÊï™
        jx.setCellValue(3, 5, "ê≈î≤");
        int stepRowCustomize    = 15;

        int rowName             = 8;
        //ãZèpëçîÑè„
        int rowTechSalesIdx     = 10;
        //ãZèpéwñº
        int rowTechNominationIdx= 11;
        //ãZèpêVãK
        int rowNewTechIdx       = 12;
        //è§ïiîÑè„
        int rowItemSalesIdx     = 13;
        //ëçãqêî
        int rowNumTechOfCustomersIdx= 14;
        //éwñº
        int rowDesignatedChargeIdx= 15;
        //êVãK
        int rowNewCustomerIdx     = 16;
        //ëçíPâø
        int rowTotalUnitPrice     = 17;
        //èóê´
        int rowFemale             = 20;
        //íjê´
        int rowMaleIdx            = 21;
        //èóê´î‰ó¶
        int rowFemaleRateIdx      = 22;
        
        //list month
        List<Integer> months    = getMonths(this.month);
        
        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            CustomizeSalesBean customizeItem = customizeMapItems.get(key);
            
            jx.setCellValue(2, rowName, customizeItem.getName());
            
            int col         = 4;
            int stepMonth   = 1;
            for(Integer itemMonth : months) {
                MonthSalesBean item = null;
                for(MonthSalesBean itemTemp : customizeItem.getMonthItems()) {
                    if(itemMonth.equals(itemTemp.getMonthLabel())) {
                        item = itemTemp;
                        break;
                    }
                }
                
                if(item != null) {
                
                    jx.setCellValue(col, rowTechSalesIdx, item.getItem().getTotalTechSales());
                    jx.setCellValue(col, rowTechNominationIdx, item.getItem().getTechNomination());

                    jx.setCellValue(col, rowNewTechIdx, item.getItem().getNewTech());
                    jx.setCellValue(col, rowItemSalesIdx, item.getItem().getItemSales());

                    jx.setCellValue(col, rowNumTechOfCustomersIdx, item.getItem().getTotalNumTechOfCustomers());
                    jx.setCellValue(col, rowDesignatedChargeIdx, item.getItem().getDesignatedMainCharge());

                    jx.setCellValue(col, rowNewCustomerIdx, item.getItem().getNewCustomer());
                    if(item.getItem().getTotalNumCustomers() != 0) {
                        long totalSale      = item.getItem().getTotalTechSales() + item.getItem().getItemSales();
                        int totalUnitPrice  = (int) (totalSale/item.getItem().getTotalNumCustomers());
                        jx.setCellValue(col, rowTotalUnitPrice, totalUnitPrice );
                    } else {
                        jx.setCellValue(col, rowTotalUnitPrice, "-" );
                    }
                    jx.setCellValue(col, rowFemale, getUnitPrice(item.getItem().getFemale(), item.getItem().getFemaleCount()));

                    jx.setCellValue(col, rowMaleIdx, getUnitPrice(item.getItem().getMale(), item.getItem().getMaleCount()));
                    jx.setCellValue(col, rowFemaleRateIdx, item.getItem().getFemaleRate());
                }
                
                col   = col + stepMonth;
            }
            
            rowName             = rowName           + stepRowCustomize;
            //ãZèpëçîÑè„
            rowTechSalesIdx     = rowTechSalesIdx   + stepRowCustomize;
            //ãZèpéwñº
            rowTechNominationIdx= rowTechNominationIdx  + stepRowCustomize;
            //ãZèpêVãK
            rowNewTechIdx       = rowNewTechIdx     + stepRowCustomize;
            //è§ïiîÑè„
            rowItemSalesIdx     = rowItemSalesIdx   + stepRowCustomize;
            //ëçãqêî
            rowNumTechOfCustomersIdx= rowNumTechOfCustomersIdx  + stepRowCustomize;
            //éwñº
            rowDesignatedChargeIdx= rowDesignatedChargeIdx  + stepRowCustomize;
            //êVãK
            rowNewCustomerIdx     = rowNewCustomerIdx   + stepRowCustomize;
            //ëçíPâø
            rowTotalUnitPrice     = rowTotalUnitPrice + stepRowCustomize;
            //èóê´
            rowFemale             = rowFemale       + stepRowCustomize;
            //íjê´
            rowMaleIdx            = rowMaleIdx      + stepRowCustomize;
            //èóê´î‰ó¶
            rowFemaleRateIdx      = rowFemaleRateIdx+ stepRowCustomize;
        }
        jx.setFormularActive();
        jx.openWorkbook();
    }

    @Override
    public String getReportSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append("SELECT                                                                                      ");
        sql.append("       ms.shop_name,                                                                        ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_name1 || ' ' || mstaff.staff_name2 AS staffname,                          ");
        }
        sql.append(" ds.*                                                                                       ");
        sql.append(" FROM                                                                                       ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mst_staff  mstaff                                                                      ");
        }else {
            sql.append(" mst_shop ms                                                                            ");
        }
        sql.append(" INNER JOIN (                                                                               ");
        sql.append(" SELECT                                                                                     ");
        if(OutputType.STAFF == outputType) {
            sql.append(" ds.staff_id,                                                                           ");
        }
        sql.append("       ds.shop_id,                                                                          ");
        sql.append("       date_part('year', ds.sales_date) AS lable_year,                                                                         ");
        sql.append("       date_part('month', ds.sales_date) AS lable_month,                                                                       ");
        sql.append("       coalesce(sum(CASE                                                                                                       ");
        sql.append("                        WHEN ds.product_division in(1, 3) THEN ds.discount_detail_value_no_tax                                 ");
        sql.append("                        ELSE 0                                                                                                 ");
        sql.append("                    END), 0) AS total_tech_sales,                                                                              ");
        sql.append("       coalesce(sum(CASE                                                                                                       ");
        sql.append("                        WHEN ds.product_division in(1, 3)                                                                      ");
        sql.append("                             AND ds.designated_flag THEN ds.discount_detail_value_no_tax                                       ");
        sql.append("                        ELSE 0                                                                                                 ");
        sql.append("                    END), 0) AS tech_nomination,                                                                               ");
        sql.append("       sum(CASE                                                                                                                ");
        sql.append("               WHEN ds.product_division = 1                                                                                    ");
        sql.append("                    AND get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1 THEN ds.discount_detail_value_no_tax    ");
        sql.append("               ELSE 0                                                                                                          ");
        sql.append("           END) AS new_tech,                                                                                                   ");
        sql.append("       coalesce(sum(CASE                                                                                                       ");
        sql.append("                        WHEN ds.product_division in(2, 4) THEN ds.discount_detail_value_no_tax                                 ");
        sql.append("                        ELSE 0                                                                                                 ");
        sql.append("                    END), 0) AS item_sales,                                                                                    ");
        sql.append("       count(DISTINCT ds.slip_no) AS total_num_customers,                                                              ");
        sql.append("       count(DISTINCT CASE                                                                                                     ");
        sql.append("                          WHEN ds.product_division in(1, 3) THEN ds.slip_no                                                    ");
        sql.append("                          ELSE NULL                                                                                            ");
        sql.append("                      END) AS total_num_tech_of_customers,                                                                     ");
        sql.append("       count(DISTINCT CASE                                                                                                     ");
        sql.append("                          WHEN ds.product_division in(1, 3) AND ds.designated_flag THEN ds.slip_no                                                              ");
        sql.append("                          ELSE NULL                                                                                            ");
        sql.append("                      END) AS designated_main_charge,                                                                          ");
        sql.append("       count(DISTINCT CASE                                                                                                     ");
        sql.append("                          WHEN ds.product_division in (1, 3) and mc.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1 THEN ds.slip_no                  ");
        sql.append("                          ELSE NULL                                                                                            ");
        sql.append("                      END) AS new_customer,                                                                                    ");
        sql.append("       sum(CASE                                                                                                                ");
        sql.append("               WHEN (ds.product_division in (1, 3)                                                                             ");
        sql.append("                     AND mc.sex = 1) THEN ds.discount_detail_value_no_tax                                                      ");
        sql.append("               ELSE 0                                                                                                          ");
        sql.append("           END) AS male,                                                                                                       ");
        sql.append("       count(DISTINCT CASE                                                                                                     ");
        sql.append("               WHEN (ds.product_division in (1, 3)                                                                             ");
        sql.append("                     AND mc.sex = 1) THEN  ds.slip_no                                                                          ");
        sql.append("               ELSE NULL                                                                                                          ");
        sql.append("           END) AS male_count,                                                                                                 ");
        sql.append("       sum(CASE                                                                                                                ");
        sql.append("               WHEN (ds.product_division in (1, 3)                                                                             ");
        sql.append("                     AND mc.sex = 2) THEN ds.discount_detail_value_no_tax                                                      ");
        sql.append("               ELSE 0                                                                                                          ");
        sql.append("           END) AS female,                                                                                                      ");
        sql.append("       count(DISTINCT CASE                                                                                                      ");
        sql.append("               WHEN (ds.product_division in (1, 3)                                                                              ");
        sql.append("                     AND mc.sex = 2) THEN ds.slip_no                                                                            ");
        sql.append("               ELSE NULL                                                                                                           ");
        sql.append("           END) AS female_count                                                                                                  ");
        sql.append(" FROM                                                                                                                            ");
        sql.append(" view_data_sales_detail_valid ds                                                                                                 ");
        sql.append(" LEFT JOIN mst_customer mc ON ds.customer_id = mc.customer_id                               ");
        sql.append("  WHERE ds.sales_date BETWEEN date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime())).append(" AND date").append(SQLUtil.convertForSQLDateOnly(this.getLastYear(this.calendarFor()).getTime())).append(" + interval '1 year'");
        sql.append(" GROUP BY ds.shop_id,                                                                       ");
        if(OutputType.STAFF == outputType) {
            sql.append(" ds.staff_id,                                                                           ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month                                                                                ");
        sql.append("  ) ds ON  ds.shop_id in (").append(shopIDList).append(")                                   ");
        if(OutputType.STAFF == outputType) {
            sql.append(" AND mstaff.staff_id = ds.staff_id                                                       ");
            sql.append(" INNER JOIN mst_shop ms ON ms.shop_id = ds.shop_id                                       ");
        }else if(OutputType.SHOP == outputType) {
             sql.append("  AND ds.shop_id = ms.shop_id                                                           ");
        }
        sql.append(" WHERE ms.shop_id in (").append(shopIDList).append(")                                       ");
        if(OutputType.SHOP == outputType) {
             sql.append("  AND ms.delete_date IS NULL                                                           ");
        }
        sql.append(" ORDER BY ms.shop_id,                                                                       ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_id,                                                                       ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month                                                                                ");
        
        return sql.toString();
    }
    
    /**
     * init shop total
     * @param shopTotal 
     */
    private void initShopTotal(CustomizeSalesBean shopTotal, List<Integer> months) {
        
        for (Integer item : months) {
            MonthSalesBean monthItem = new MonthSalesBean();
            SaleManagementBean itemSales = new SaleManagementBean();

            monthItem.setMonthLabel(item);
            monthItem.setItem(itemSales);
            shopTotal.getMonthItems().add(monthItem);
        }
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
    
    /**
     * get unit value
     * @param totalValue
     * @param num
     * @return 
     */
    private double getUnitPrice(long totalValue, int num) {
        if(num != 0) {
            return Double.valueOf(df.format(totalValue*1.0/num));
        }else {
            return 0.0;
        }
    }
    
}

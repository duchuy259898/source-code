/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.CustomerItemBean;
import com.geobeck.sosia.pos.hair.report.beans.GenderItemCountBean;
import com.geobeck.sosia.pos.hair.report.beans.MonthItemBean;
import com.geobeck.sosia.pos.hair.report.beans.ServiceItemBean;
import com.geobeck.sosia.pos.hair.report.beans.CustomizeItemBean;
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
 * カスタム帳票
 * エステ管理
 * @author IVS
 */
public class EstheticManagementLogic extends ReportCustomize {
    
    //店舗
    protected String shopIDList;
    protected String targetStores;
    protected String targetName;
    //出力対象
    protected OutputType outputType;
    public enum OutputType {
        SHOP, STAFF
    }


    public EstheticManagementLogic(String shopIDList, String targetStores, Integer year, Integer month, OutputType outputType) {
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
            
            LinkedHashMap<Integer, CustomizeItemBean> customizeMapItems = new LinkedHashMap<>();
             while (rs.next()) {
                 
                 CustomizeItemBean customizeItem = new CustomizeItemBean();
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
                 
                 
                 MonthItemBean monthItem = new MonthItemBean();
                 monthItem.setYearLabel(rs.getInt("lable_year"));
                 monthItem.setMonthLabel(rs.getInt("lable_month"));
                 
                 GenderItemCountBean genderItem = new GenderItemCountBean();
                 CustomerItemBean customerMale = new CustomerItemBean();
                 customerMale.setNumberOfCustomer(rs.getInt("male_count_all"));
                 customerMale.setDesignated(rs.getInt("male_count_designated"));
                 customerMale.setCusNew(rs.getInt("new_cus_male_count"));
                 
                 ServiceItemBean customerOld = new ServiceItemBean();
                 //エステ
                 customerOld.setEsthetic(rs.getInt("male_count_esthe"));
                 //クレ・潤い
                 customerOld.setKure(rs.getInt("male_count_kure"));
                 //プチ・改善
                 customerOld.setPetit(rs.getInt("male_count_petit"));
                 //健康・美髪
                 customerOld.setHealthAndBeauty(rs.getInt("male_count_health"));
                 //プラチナ
                 customerOld.setPlatinum(rs.getInt("male_count_platinum"));
                 //育毛
                 customerOld.setHairGrowth(rs.getInt("male_count_hair_growth"));
                 customerMale.setCustomerOld(customerOld);
                 
                 ServiceItemBean customerNew = new ServiceItemBean();
                 //クレ・潤い
                 customerNew.setKure(rs.getInt("new_cus_male_count_kure"));
                 //プチ・改善
                 customerNew.setPetit(rs.getInt("new_cus_male_count_petit"));
                 //健康・美髪
                 customerNew.setHealthAndBeauty(rs.getInt("new_cus_male_count_health"));
                 //プラチナ
                 customerNew.setPlatinum(rs.getInt("new_cus_male_count_platinum"));
                 //育毛
                 customerNew.setHairGrowth(rs.getInt("new_cus_male_count_hair_growth"));
                 customerMale.setCustomerNew(customerNew);
                 genderItem.setMale(customerMale);
                 
                 CustomerItemBean customerFemale = new CustomerItemBean();
                 customerOld = new ServiceItemBean();
                 customerFemale.setNumberOfCustomer(rs.getInt("female_count_all"));
                 customerFemale.setDesignated(rs.getInt("female_count_designated"));
                 customerFemale.setCusNew(rs.getInt("new_cus_female_count"));
                 //エステ
                 customerOld.setEsthetic(rs.getInt("female_count_esthe"));
                 //クレ・潤い
                 customerOld.setKure(rs.getInt("female_count_kure"));
                 //プチ・改善
                 customerOld.setPetit(rs.getInt("female_count_petit"));
                 //健康・美髪
                 customerOld.setHealthAndBeauty(rs.getInt("female_count_health"));
                 //プラチナ
                 customerOld.setPlatinum(rs.getInt("female_count_platinum"));
                 //育毛
                 customerOld.setHairGrowth(rs.getInt("female_count_hair_growth"));
                 customerFemale.setCustomerOld(customerOld);
                 
                 customerNew = new ServiceItemBean();
                 //クレ・潤い
                 customerNew.setKure(rs.getInt("new_cus_female_count_kure"));
                 //プチ・改善
                 customerNew.setPetit(rs.getInt("new_cus_female_count_petit"));
                 //健康・美髪
                 customerNew.setHealthAndBeauty(rs.getInt("new_cus_female_count_health"));
                 //プラチナ
                 customerNew.setPlatinum(rs.getInt("new_cus_female_count_platinum"));
                 //育毛
                 customerNew.setHairGrowth(rs.getInt("new_cus_female_count_hair_growth"));
                 customerFemale.setCustomerNew(customerNew);
                 genderItem.setMale(customerFemale);
                 
                 monthItem.setItemMale(customerMale);
                 monthItem.setItemFemale(customerFemale);
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
            Logger.getLogger(EstheticManagementLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * 全店舗合計
     * @param customizeMapItems 
     */
    private void setTotalOfAllStores(LinkedHashMap<Integer, CustomizeItemBean> customizeMapItems) {
        
        CustomizeItemBean shopTotal = new CustomizeItemBean();
        shopTotal.setId(0);
        shopTotal.setName(this.outputType == OutputType.STAFF ? this.targetStores : "全店舗合計");
        
        //list month
        List<Integer> months    = getMonths(this.month);
        initShopTotal(shopTotal, months);
        
        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            CustomizeItemBean shopItem = customizeMapItems.get(key);
            
            for(MonthItemBean monthItem : shopTotal.getMonthItems()) {
                MonthItemBean item = null;
                
                for(MonthItemBean itemTemp : shopItem.getMonthItems()) {
                    if(itemTemp.getMonthLabel() == monthItem.getMonthLabel()) {
                        item = itemTemp;
                        break;
                    }
                }
                
                if(item != null) {

                    if (monthItem.getMonthLabel() == item.getMonthLabel()) {
                        monthItem.getItemFemale().setNumberOfCustomer(monthItem.getItemFemale().getNumberOfCustomer() + item.getItemFemale().getNumberOfCustomer());
                        monthItem.getItemMale().setNumberOfCustomer(monthItem.getItemMale().getNumberOfCustomer() + item.getItemMale().getNumberOfCustomer());

                        monthItem.getItemFemale().getCustomerOld().setEsthetic(monthItem.getItemFemale().getCustomerOld().getEsthetic() + item.getItemFemale().getCustomerOld().getEsthetic());
                        monthItem.getItemMale().getCustomerOld().setEsthetic(monthItem.getItemMale().getCustomerOld().getEsthetic() + item.getItemMale().getCustomerOld().getEsthetic());

                        monthItem.getItemFemale().setDesignated(monthItem.getItemFemale().getDesignated() + item.getItemFemale().getDesignated());
                        monthItem.getItemMale().setDesignated(monthItem.getItemMale().getDesignated() + item.getItemMale().getDesignated());

                        monthItem.getItemFemale().getCustomerOld().setKure(monthItem.getItemFemale().getCustomerOld().getKure() + item.getItemFemale().getCustomerOld().getKure());
                        monthItem.getItemMale().getCustomerOld().setKure(monthItem.getItemMale().getCustomerOld().getKure() + item.getItemMale().getCustomerOld().getKure());

                        monthItem.getItemFemale().getCustomerOld().setPetit(monthItem.getItemFemale().getCustomerOld().getPetit() + item.getItemFemale().getCustomerOld().getPetit());
                        monthItem.getItemMale().getCustomerOld().setPetit(monthItem.getItemMale().getCustomerOld().getPetit() + item.getItemMale().getCustomerOld().getPetit());

                        monthItem.getItemFemale().getCustomerOld().setHealthAndBeauty(monthItem.getItemFemale().getCustomerOld().getHealthAndBeauty() + item.getItemFemale().getCustomerOld().getHealthAndBeauty());
                        monthItem.getItemMale().getCustomerOld().setHealthAndBeauty(monthItem.getItemMale().getCustomerOld().getHealthAndBeauty() + item.getItemMale().getCustomerOld().getHealthAndBeauty());

                        monthItem.getItemFemale().getCustomerOld().setPlatinum( monthItem.getItemFemale().getCustomerOld().getPlatinum() + item.getItemFemale().getCustomerOld().getPlatinum());
                        monthItem.getItemMale().getCustomerOld().setPlatinum( monthItem.getItemMale().getCustomerOld().getPlatinum() + item.getItemMale().getCustomerOld().getPlatinum());

                        monthItem.getItemFemale().getCustomerOld().setHairGrowth(monthItem.getItemFemale().getCustomerOld().getHairGrowth() + item.getItemFemale().getCustomerOld().getHairGrowth());
                        monthItem.getItemMale().getCustomerOld().setHairGrowth(monthItem.getItemMale().getCustomerOld().getHairGrowth() + item.getItemMale().getCustomerOld().getHairGrowth());

                        monthItem.getItemFemale().setCusNew(monthItem.getItemFemale().getCusNew() + item.getItemFemale().getCusNew());
                        monthItem.getItemMale().setCusNew(monthItem.getItemMale().getCusNew() + item.getItemMale().getCusNew());

                        monthItem.getItemFemale().getCustomerNew().setKure(monthItem.getItemFemale().getCustomerNew().getKure() + item.getItemFemale().getCustomerNew().getKure());
                        monthItem.getItemMale().getCustomerNew().setKure(monthItem.getItemMale().getCustomerNew().getKure() + item.getItemMale().getCustomerNew().getKure());

                        monthItem.getItemFemale().getCustomerNew().setPetit(monthItem.getItemFemale().getCustomerNew().getPetit() + item.getItemFemale().getCustomerNew().getPetit());
                        monthItem.getItemMale().getCustomerNew().setPetit(monthItem.getItemMale().getCustomerNew().getPetit() + item.getItemMale().getCustomerNew().getPetit());

                        monthItem.getItemFemale().getCustomerNew().setHealthAndBeauty(monthItem.getItemFemale().getCustomerNew().getHealthAndBeauty() + item.getItemFemale().getCustomerNew().getHealthAndBeauty());
                        monthItem.getItemMale().getCustomerNew().setHealthAndBeauty(monthItem.getItemMale().getCustomerNew().getHealthAndBeauty() + item.getItemMale().getCustomerNew().getHealthAndBeauty());

                        monthItem.getItemFemale().getCustomerNew().setPlatinum(monthItem.getItemFemale().getCustomerNew().getPlatinum() + item.getItemFemale().getCustomerNew().getPlatinum());
                        monthItem.getItemMale().getCustomerNew().setPlatinum(monthItem.getItemMale().getCustomerNew().getPlatinum() + item.getItemMale().getCustomerNew().getPlatinum());

                        monthItem.getItemFemale().getCustomerNew().setHairGrowth(monthItem.getItemFemale().getCustomerNew().getHairGrowth() + item.getItemFemale().getCustomerNew().getHairGrowth());
                        monthItem.getItemMale().getCustomerNew().setHairGrowth(monthItem.getItemMale().getCustomerNew().getHairGrowth() + item.getItemMale().getCustomerNew().getHairGrowth());
                    }
                }
            }
        }
        LinkedHashMap<Integer, CustomizeItemBean> newMap =(LinkedHashMap<Integer, CustomizeItemBean>) customizeMapItems.clone();
        customizeMapItems.clear();
        customizeMapItems.put(shopTotal.getId(), shopTotal);
        customizeMapItems.putAll(newMap);
    }
    
    /**
     * fill data
     * @param customizeMapItems 
     */
    private void fillData(LinkedHashMap<Integer, CustomizeItemBean> customizeMapItems) {
        
        String fileName = this.outputType == OutputType.SHOP ? "エステ管理（店舗）":"エステ管理（スタッフ）";
        JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion(fileName);
        //テンプレートとなるファイルをセット
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        
        //出力対象月
        jx.setCellValue(3, 3, this.year);
        jx.setCellValue(4, 3, this.month);
        //対象店舗
        jx.setCellValue(3, 4, this.targetStores);
        int stepRowCustomize    = 17;

        //店名-D
        int rowShopName         = 9;
        //客数-E
        int rowNumOfCusIdx      = 10;
        //エステ-F
        int rowEstheticIdx      = 11;
        //指名-G
        int rowDesignateIdx     = 12;
        //クレ・潤い-H
        int rowKureIdx          = 14;
        //プチ・改善-I
        int rowPetitIdx         = 15;
        //健康・美髪-J
        int rowHealthIdx        = 16;
        //プラチナ-K
        int rowPlatinumIdx      = 17;
        //育毛-L
        int rowHairGrowth       = 18;
        //新規-M
        int rowNewCusIdx        = 19;
        //クレ・潤い-N
        int rowNewKureIdx       = 21;
        //プチ・改善-O
        int rowNewPetitIdx      = 22;
        //健康・美髪-P
        int rowNewHealthIdx     = 23;
        //プラチナ-Q
        int rowNewPlatinumIdx   = 24;
        //育毛-R
        int rowNewHairGrowth    = 25;
        
        //list month
        List<Integer> months    = getMonths(this.month);
        
        Set<Integer> keySet = customizeMapItems.keySet();
        for (Integer key : keySet) {

            CustomizeItemBean customizeItem = customizeMapItems.get(key);
            
            jx.setCellValue(2, rowShopName, customizeItem.getName());
            
            int colFemale   = 4;
            int colMale     = 6;
            int stepMonth   = 6;
            for(Integer itemMonth : months) {
                MonthItemBean item = null;
                for(MonthItemBean itemTemp : customizeItem.getMonthItems()) {
                    if(itemMonth.equals(itemTemp.getMonthLabel())) {
                        item = itemTemp;
                        break;
                    }
                }
                
                if(item != null) {
                
                    jx.setCellValue(colFemale, rowNumOfCusIdx, item.getItemFemale().getNumberOfCustomer());
                    jx.setCellValue(colMale, rowNumOfCusIdx, item.getItemMale().getNumberOfCustomer());

                    jx.setCellValue(colFemale, rowEstheticIdx, item.getItemFemale().getCustomerOld().getEsthetic());
                    jx.setCellValue(colMale, rowEstheticIdx, item.getItemMale().getCustomerOld().getEsthetic());

                    jx.setCellValue(colFemale, rowDesignateIdx, item.getItemFemale().getDesignated());
                    jx.setCellValue(colMale, rowDesignateIdx, item.getItemMale().getDesignated());

                    jx.setCellValue(colFemale, rowKureIdx, item.getItemFemale().getCustomerOld().getKure());
                    jx.setCellValue(colMale, rowKureIdx, item.getItemMale().getCustomerOld().getKure());

                    jx.setCellValue(colFemale, rowPetitIdx, item.getItemFemale().getCustomerOld().getPetit());
                    jx.setCellValue(colMale, rowPetitIdx, item.getItemMale().getCustomerOld().getPetit());

                    jx.setCellValue(colFemale, rowHealthIdx, item.getItemFemale().getCustomerOld().getHealthAndBeauty());
                    jx.setCellValue(colMale, rowHealthIdx, item.getItemMale().getCustomerOld().getHealthAndBeauty());

                    jx.setCellValue(colFemale, rowPlatinumIdx, item.getItemFemale().getCustomerOld().getPlatinum());
                    jx.setCellValue(colMale, rowPlatinumIdx, item.getItemMale().getCustomerOld().getPlatinum());

                    jx.setCellValue(colFemale, rowHairGrowth, item.getItemFemale().getCustomerOld().getHairGrowth());
                    jx.setCellValue(colMale, rowHairGrowth, item.getItemMale().getCustomerOld().getHairGrowth());

                    jx.setCellValue(colFemale, rowNewCusIdx, item.getItemFemale().getCusNew());
                    jx.setCellValue(colMale, rowNewCusIdx, item.getItemMale().getCusNew());

                    jx.setCellValue(colFemale, rowNewKureIdx, item.getItemFemale().getCustomerNew().getKure());
                    jx.setCellValue(colMale, rowNewKureIdx, item.getItemMale().getCustomerNew().getKure());

                    jx.setCellValue(colFemale, rowNewPetitIdx, item.getItemFemale().getCustomerNew().getPetit());
                    jx.setCellValue(colMale, rowNewPetitIdx, item.getItemMale().getCustomerNew().getPetit());

                    jx.setCellValue(colFemale, rowNewHealthIdx, item.getItemFemale().getCustomerNew().getHealthAndBeauty());
                    jx.setCellValue(colMale, rowNewHealthIdx, item.getItemMale().getCustomerNew().getHealthAndBeauty());

                    jx.setCellValue(colFemale, rowNewPlatinumIdx, item.getItemFemale().getCustomerNew().getPlatinum());
                    jx.setCellValue(colMale, rowNewPlatinumIdx, item.getItemMale().getCustomerNew().getPlatinum());

                    jx.setCellValue(colFemale, rowNewHairGrowth, item.getItemFemale().getCustomerNew().getHairGrowth());
                    jx.setCellValue(colMale, rowNewHairGrowth, item.getItemMale().getCustomerNew().getHairGrowth());
                }
                
                colFemale   = colFemale + stepMonth;
                colMale     = colMale   + stepMonth;
            }
            
            rowShopName         = rowShopName       + stepRowCustomize;
            rowNumOfCusIdx      = rowNumOfCusIdx    + stepRowCustomize;
            rowEstheticIdx      = rowEstheticIdx    + stepRowCustomize;
            rowDesignateIdx     = rowDesignateIdx   + stepRowCustomize;
            rowKureIdx          = rowKureIdx        + stepRowCustomize;
            rowPetitIdx         = rowPetitIdx       + stepRowCustomize;
            rowHealthIdx        = rowHealthIdx      + stepRowCustomize;
            rowPlatinumIdx      = rowPlatinumIdx    + stepRowCustomize;
            rowHairGrowth       = rowHairGrowth     + stepRowCustomize;
            rowNewCusIdx        = rowNewCusIdx      + stepRowCustomize;
            rowNewKureIdx       = rowNewKureIdx     + stepRowCustomize;
            rowNewPetitIdx      = rowNewPetitIdx    + stepRowCustomize;
            rowNewHealthIdx     = rowNewHealthIdx   + stepRowCustomize;
            rowNewPlatinumIdx   = rowNewPlatinumIdx + stepRowCustomize;
            rowNewHairGrowth    = rowNewHairGrowth  + stepRowCustomize;
        }
        jx.setFormularActive();
        jx.openWorkbook();
    }

    @Override
    public String getReportSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append("SELECT ms.shop_id,                                                                          ");
        sql.append("       ms.shop_name,                                                                        ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_id,                                                                       ");
            sql.append(" mstaff.staff_name1 || ' ' || mstaff.staff_name2 AS staffname,                          ");
        }
        sql.append(" date_part('year', ds.sales_date) AS lable_year,                                            ");
        sql.append(" date_part('month', ds.sales_date) AS lable_month,                                          ");
        sql.append(" count(DISTINCT CASE                                                                        ");
        sql.append("                  WHEN mc.sex = 1 THEN ds.slip_no                                           ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_all,                                                       ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 2 THEN ds.slip_no                                           ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_all,                                                     ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_class_id = 5 ) THEN ds.slip_no                  ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_esthe,                                                     ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_class_id = 5 ) THEN ds.slip_no                  ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_esthe,                                                   ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_designated,                                                ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_designated,                                              ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (81, 137) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_kure,                                                      ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (81, 137) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_kure,                                                    ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (84, 87) ) THEN ds.slip_no                ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_petit,                                                     ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (84, 87) ) THEN ds.slip_no                ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_petit,                                                   ");
        
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (97, 120) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_health,                                                    ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (97, 120) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_health,                                                  ");
        
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (86) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_platinum,                                                  ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (86) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_platinum,                                                ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (93) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS male_count_hair_growth,                                               ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE sub_ds.designated_flag = TRUE                               ");
        sql.append("                            AND dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (93) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS female_count_hair_growth,                                             ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 1 AND mc.customer_no <> '0'                                 ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1   ");
        sql.append("                        THEN ds.slip_no                                                     ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_male_count,                                                   ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2 AND mc.customer_no <> '0'                                 ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1   ");
        sql.append("                        THEN ds.slip_no                                                     ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_female_count,                                                 ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (81, 137) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_male_count_kure,                                              ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (81, 137) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_female_count_kure,                                            ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (84, 87) ) THEN ds.slip_no                ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_male_count_petit,                                             ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (84, 87) ) THEN ds.slip_no                ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_female_count_petit,                                           ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (97, 120) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_male_count_health,                                            ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales sub_ds                                            ");
        sql.append("                          INNER JOIN data_sales_detail dsd USING(shop_id, slip_no)          ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (97, 120) ) THEN ds.slip_no               ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_female_count_health,                                          ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (86) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_male_count_platinum,                                          ");
        sql.append("count(DISTINCT CASE                                                                         ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (86) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_female_count_platinum,                                        ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 1                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (93) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_male_count_hair_growth,                                       ");
        sql.append("count(DISTINCT CASE                                                                          ");
        sql.append("                  WHEN mc.sex = 2                                                           ");
        sql.append("                       AND get_visit_count(ds.customer_id, ds.sales_date) = 1               ");
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id     ");
        sql.append("                          AND dsd.product_division in (1, 3)                                      ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND mst.technic_id in (93) ) THEN ds.slip_no                    ");
        sql.append("                  ELSE NULL                                                                 ");
        sql.append("              END) AS new_cus_female_count_hair_growth                                      ");
        sql.append(" FROM                                                                                       ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mst_staff mstaff                                                                       ");
            sql.append(" INNER JOIN data_sales ds ON ds.staff_id = mstaff.staff_id                              ");
        }else {
            sql.append(" mst_shop ms        ");
            sql.append(" INNER JOIN data_sales ds ON ds.shop_id = ms.shop_id                                    ");
        }
        sql.append("                       AND EXISTS                                                           ");
        sql.append("                         (SELECT 1                                                          ");
        sql.append("                          FROM data_sales_detail dsd                                        ");
        sql.append("                          WHERE dsd.shop_id = ds.shop_id                                    ");
        sql.append("                            AND dsd.slip_no = ds.slip_no                                    ");
        sql.append("                            AND dsd.delete_date IS NULL                                     ");
        sql.append("                            AND dsd.product_division in (1, 3))                                ");
        sql.append("  AND ds.sales_date BETWEEN date").append(SQLUtil.convertForSQLDateOnly(this.calendarFor().getTime())).append(" AND date").append(SQLUtil.convertForSQLDateOnly(this.getLastYear(this.calendarFor()).getTime())).append(" + interval '1 year'");
        if(OutputType.STAFF == outputType) {
            sql.append("  AND mstaff.staff_id = ds.staff_id                                                      ");
            sql.append(" INNER JOIN mst_shop ms ON ms.shop_id = ds.shop_id                                      ");
        }
        sql.append(" LEFT JOIN mst_customer mc ON ds.customer_id = mc.customer_id                              ");
        sql.append(" WHERE ds.delete_date IS NULL                                                               ");
        sql.append("  AND ms.shop_id in (").append(shopIDList).append(")                                        ");
        if(OutputType.SHOP == outputType) {
             sql.append("  AND ms.delete_date IS NULL                                                           ");
        }
        sql.append(" GROUP BY ms.shop_id,                                                                       ");
        sql.append("         ms.shop_name,                                                                      ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_id,                                                                       ");
            sql.append(" mstaff.staff_name1,                                                                    ");
            sql.append(" mstaff.staff_name2,                                                                    ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month                                                                                ");
        sql.append(" ORDER BY ms.shop_id,                                                                       ");
        sql.append("         ms.shop_name,                                                                      ");
        if(OutputType.STAFF == outputType) {
            sql.append(" mstaff.staff_id,                                                                       ");
            sql.append(" mstaff.staff_name1,                                                                    ");
            sql.append(" mstaff.staff_name2,                                                                    ");
        }
        sql.append(" lable_year,                                                                                ");
        sql.append(" lable_month                                                                                ");
        
        return sql.toString();
    }
    
    /**
     * init shop total
     * @param shopTotal 
     */
    private void initShopTotal(CustomizeItemBean shopTotal, List<Integer> months) {
        
        for (Integer item : months) {
            MonthItemBean monthItem = new MonthItemBean();

            GenderItemCountBean genderItem = new GenderItemCountBean();
            CustomerItemBean customerMale = new CustomerItemBean();

            ServiceItemBean customerOld = new ServiceItemBean();
            customerMale.setCustomerOld(customerOld);

            ServiceItemBean customerNew = new ServiceItemBean();
            customerMale.setCustomerNew(customerNew);
            genderItem.setMale(customerMale);

            CustomerItemBean customerFemale = new CustomerItemBean();
            customerOld = new ServiceItemBean();
            customerFemale.setCustomerOld(customerOld);

            customerNew = new ServiceItemBean();
            customerFemale.setCustomerNew(customerNew);
            genderItem.setMale(customerFemale);

            monthItem.setMonthLabel(item);
            monthItem.setItemMale(customerMale);
            monthItem.setItemFemale(customerFemale);
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author ttmloan
 */
public class MstRankAdvancedSetting {

    //‹Æ‘ÔID
    private Integer shopCategoryId;
    //—ˆ“X’P‰¿5
    private Integer unitPrice5;
    //—ˆ“X’P‰¿4
    private Integer unitPrice4;
    //—ˆ“X’P‰¿3
    private Integer unitPrice3;
    //—ˆ“X’P‰¿2
    private Integer unitPrice2;
    //—ˆ“X’P‰¿1
    private Integer unitPrice1;
    //‹Zp”„ã5
    private Integer technicValue5;
    //‹Zp”„ã4
    private Integer technicValue4;
    //‹Zp”„ã3
    private Integer technicValue3;
    //‹Zp”„ã2
    private Integer technicValue2;
    //‹Zp”„ã1
    private Integer technicValue1;
    //“X”Ì”„ã5
    private Integer itemValue5;
    //“X”Ì”„ã4
    private Integer itemValue4;
    //“X”Ì”„ã3
    private Integer itemValue3;
    //“X”Ì”„ã2
    private Integer itemValue2;
    //“X”Ì”„ã1
    private Integer itemValue1;
    //—ˆ“XüŠú5
    private Integer visitCycle5;
    //—ˆ“XüŠú4
    private Integer visitCycle4;
    //—ˆ“XüŠú3
    private Integer visitCycle3;
    //—ˆ“XüŠú2
    private Integer visitCycle2;
    //	—ˆ“XüŠú1
    private Integer visitCycle1;
    //¦“X”Ìw“ü‘ÎÛƒOƒ‹[ƒv
    private String itemGroupId;
    //¦“X”Ì”„ã‘ÎÛƒOƒ‹[ƒv
    private String itemSalesGroup;

    //IVS_LVTu start edit 2015/04/17 New request #36344
    //0F‹Zp”„ã‚Ì‚İ@1F“X”Ì”„ãŠÜ‚Ş
    private Integer sales_flag;

    public Integer getSales_flag() {
        return sales_flag;
    }

    public void setSales_flag(Integer sales_flag) {
        this.sales_flag = sales_flag;
    }
    //IVS_LVTu start edit 2015/04/17 New request #36344
    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public Integer getUnitPrice5() {
        return unitPrice5;
    }

    public void setUnitPrice5(Integer unitPrice5) {
        this.unitPrice5 = unitPrice5;
    }

    public Integer getUnitPrice4() {
        return unitPrice4;
    }

    public void setUnitPrice4(Integer unitPrice4) {
        this.unitPrice4 = unitPrice4;
    }

    public Integer getUnitPrice3() {
        return unitPrice3;
    }

    public void setUnitPrice3(Integer unitPrice3) {
        this.unitPrice3 = unitPrice3;
    }

    public Integer getUnitPrice2() {
        return unitPrice2;
    }

    public void setUnitPrice2(Integer unitPrice2) {
        this.unitPrice2 = unitPrice2;
    }

    public Integer getUnitPrice1() {
        return unitPrice1;
    }

    public void setUnitPrice1(Integer unitPrice1) {
        this.unitPrice1 = unitPrice1;
    }

    public Integer getTechnicValue5() {
        return technicValue5;
    }

    public void setTechnicValue5(Integer technicValue5) {
        this.technicValue5 = technicValue5;
    }

    public Integer getTechnicValue4() {
        return technicValue4;
    }

    public void setTechnicValue4(Integer technicValue4) {
        this.technicValue4 = technicValue4;
    }

    public Integer getTechnicValue3() {
        return technicValue3;
    }

    public void setTechnicValue3(Integer technicValue3) {
        this.technicValue3 = technicValue3;
    }

    public Integer getTechnicValue2() {
        return technicValue2;
    }

    public void setTechnicValue2(Integer technicValue2) {
        this.technicValue2 = technicValue2;
    }

    public Integer getTechnicValue1() {
        return technicValue1;
    }

    public void setTechnicValue1(Integer technicValue1) {
        this.technicValue1 = technicValue1;
    }

    public Integer getItemValue5() {
        return itemValue5;
    }

    public void setItemValue5(Integer itemValue5) {
        this.itemValue5 = itemValue5;
    }

    public Integer getItemValue4() {
        return itemValue4;
    }

    public void setItemValue4(Integer itemValue4) {
        this.itemValue4 = itemValue4;
    }

    public Integer getItemValue3() {
        return itemValue3;
    }

    public void setItemValue3(Integer itemValue3) {
        this.itemValue3 = itemValue3;
    }

    public Integer getItemValue2() {
        return itemValue2;
    }

    public void setItemValue2(Integer itemValue2) {
        this.itemValue2 = itemValue2;
    }

    public Integer getItemValue1() {
        return itemValue1;
    }

    public void setItemValue1(Integer itemValue1) {
        this.itemValue1 = itemValue1;
    }

    public Integer getVisitCycle5() {
        return visitCycle5;
    }

    public void setVisitCycle5(Integer visitCycle5) {
        this.visitCycle5 = visitCycle5;
    }

    public Integer getVisitCycle4() {
        return visitCycle4;
    }

    public void setVisitCycle4(Integer visitCycle4) {
        this.visitCycle4 = visitCycle4;
    }

    public Integer getVisitCycle3() {
        return visitCycle3;
    }

    public void setVisitCycle3(Integer visitCycle3) {
        this.visitCycle3 = visitCycle3;
    }

    public Integer getVisitCycle2() {
        return visitCycle2;
    }

    public void setVisitCycle2(Integer visitCycle2) {
        this.visitCycle2 = visitCycle2;
    }

    public Integer getVisitCycle1() {
        return visitCycle1;
    }

    public void setVisitCycle1(Integer visitCycle1) {
        this.visitCycle1 = visitCycle1;
    }

    public String getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public String getItemSalesGroup() {
        return itemSalesGroup;
    }

    public void setItemSalesGroup(String itemSalesGroup) {
        this.itemSalesGroup = itemSalesGroup;
    }

    /**
     * ResultSetWrapper‚Ìƒf[ƒ^‚ğ“Ç‚İ‚ŞB
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setShopCategoryId(rs.getInt("shop_category_id"));
        this.setUnitPrice5(rs.getInt("unit_price5"));
        this.setUnitPrice4(rs.getInt("unit_price4"));
        this.setUnitPrice3(rs.getInt("unit_price3"));
        this.setUnitPrice2(rs.getInt("unit_price2"));
        this.setUnitPrice1(rs.getInt("unit_price1"));
        this.setTechnicValue5(rs.getInt("technic_value5"));
        this.setTechnicValue4(rs.getInt("technic_value4"));
        this.setTechnicValue3(rs.getInt("technic_value3"));
        this.setTechnicValue2(rs.getInt("technic_value2"));
        this.setTechnicValue1(rs.getInt("technic_value1"));
        this.setItemValue5(rs.getInt("item_value5"));
        this.setItemValue4(rs.getInt("item_value4"));
        this.setItemValue3(rs.getInt("item_value3"));
        this.setItemValue2(rs.getInt("item_value2"));
        this.setItemValue1(rs.getInt("item_value1"));
        this.setVisitCycle5(rs.getInt("visit_cycle5"));
        this.setVisitCycle4(rs.getInt("visit_cycle4"));
        this.setVisitCycle3(rs.getInt("visit_cycle3"));
        this.setVisitCycle2(rs.getInt("visit_cycle2"));
        this.setVisitCycle1(rs.getInt("visit_cycle1"));
        this.setItemGroupId(rs.getString("item_group_id"));
        this.setItemSalesGroup(rs.getString("item_sales_group"));
        //IVS_LVTu start edit 2015/04/17 New request #36344
        this.setSales_flag(rs.getInt("sales_flag"));
        //IVS_LVTu end edit 2015/04/17 New request #36344
    }

    //IVS_LVTu start add 2015/04/20 New request #36344
    /**
     * Load data from mst_rank_advanced_setting
     */
    public void loadRankAdvancedSettingData() {
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getRankAdvancedSettingSQL());

            if (rs.next()) {
                this.setData(rs);
            }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    /**
     * Select•¶‚ğæ“¾‚·‚éB
     *
     * @return Select•¶
     */
    private String getRankAdvancedSettingSQL() {
        return "select * from mst_rank_advanced_setting"
                + " where shop_category_id = " + this.getShopCategoryId()
                + " and delete_date is null";

    }
    //IVS_LVTu end add 2015/04/20 New request #36344
}

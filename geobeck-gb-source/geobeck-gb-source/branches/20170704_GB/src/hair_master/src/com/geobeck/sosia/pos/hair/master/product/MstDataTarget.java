/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nahoang GB_mashu 2014/10/06_ñ⁄ïWê›íË
 */
public class MstDataTarget {

    private Integer shopId = null;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    private Integer year = null;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getTechnic() {
        return technic;
    }

    public void setTechnic(Integer technic) {
        this.technic = technic;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Double getFemaleRate() {
        return femaleRate;
    }

    public void setFemaleRate(Double femaleRate) {
        this.femaleRate = femaleRate;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getMaleUnitPrice() {
        return maleUnitPrice;
    }

    public void setMaleUnitPrice(Integer maleUnitPrice) {
        this.maleUnitPrice = maleUnitPrice;
    }

    public Integer getFemaleUnitPirce() {
        return femaleUnitPirce;
    }

    public void setFemaleUnitPirce(Integer femaleUnitPirce) {
        this.femaleUnitPirce = femaleUnitPirce;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public Double getRepert90New() {
        return repeat90New;
    }

    public void setRepert90New(Double repeat90New) {
        this.repeat90New = repeat90New;
    }

    public Double getRepert90Semifix() {
        return repeat90Semifix;
    }

    public void setRepert90Semifix(Double repeat90Semifix) {
        this.repeat90Semifix = repeat90Semifix;
    }

    public Double getRepert90Fix() {
        return repeat90Fix;
    }

    public void setRepert90Fix(Double repeat90Fix) {
        this.repeat90Fix = repeat90Fix;
    }

    public Double getRepert120New() {
        return repeat120New;
    }

    public void setRepert120New(Double repeat120New) {
        this.repeat120New = repeat120New;
    }

    public Double getRepert120Semifix() {
        return repeat120Semifix;
    }

    public void setRepert120Semifix(Double repeat120Semifix) {
        this.repeat120Semifix = repeat120Semifix;
    }

    public Double getRepert120Fix() {
        return repeat120Fix;
    }

    public void setRepert120Fix(Double repeat120Fix) {
        this.repeat120Fix = repeat120Fix;
    }

    public Double getRepert180New() {
        return repeat180New;
    }

    public void setRepert180New(Double repeat180New) {
        this.repeat180New = repeat180New;
    }

    public Double getRepert180Semifix() {
        return repeat180Semifix;
    }

    public void setRepert180Semifix(Double repeat180Semifix) {
        this.repeat180Semifix = repeat180Semifix;
    }

    public Double getRepert180Fix() {
        return repeat180Fix;
    }

    public void setRepert180Fix(Double repeat180Fix) {
        this.repeat180Fix = repeat180Fix;
    }

    public Double getBeforeReserve() {
        return beforeReserve;
    }

    public void setBeforeReserve(Double beforeReserve) {
        this.beforeReserve = beforeReserve;
    }

    public Double getNextReserve() {
        return nextReserve;
    }

    public void setNextReserve(Double nextReserve) {
        this.nextReserve = nextReserve;
    }

    public Double getNewRate() {
        return newRate;
    }

    public void setNewRate(Double newRate) {
        this.newRate = newRate;
    }

    public Integer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Integer newCustomer) {
        this.newCustomer = newCustomer;
    }

    public Integer getDecyl1Rate() {
        return decyl1Rate;
    }

    public void setDecyl1Rate(Integer decyl1Rate) {
        this.decyl1Rate = decyl1Rate;
    }

    public Integer getDecyl1Num() {
        return decyl1Num;
    }

    public void setDecyl1Num(Integer decyl1Num) {
        this.decyl1Num = decyl1Num;
    }

    public Integer getDecyl2Rate() {
        return decyl2Rate;
    }

    public void setDecyl2Rate(Integer decyl2Rate) {
        this.decyl2Rate = decyl2Rate;
    }

    public Integer getDecyl2Num() {
        return decyl2Num;
    }

    public void setDecyl2Num(Integer decyl2Num) {
        this.decyl2Num = decyl2Num;
    }

    public Integer getDecyl3Rate() {
        return decyl3Rate;
    }

    public void setDecyl3Rate(Integer decyl3Rate) {
        this.decyl3Rate = decyl3Rate;
    }

    public Integer getDecyl3Num() {
        return decyl3Num;
    }

    public void setDecyl3Num(Integer decyl3Num) {
        this.decyl3Num = decyl3Num;
    }

    public Integer getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(Integer shopCategory) {
        this.shopCategory = shopCategory;
    }

    private Integer month = null;
    private Integer technic = null;
    private Integer item = null;
    private Double femaleRate = null;
    private Integer unitPrice = null;
    private Integer maleUnitPrice = null;
    private Integer femaleUnitPirce = null;
    private Integer customer = null;
    private Double repeat90New = null;
    private Double repeat90Semifix = null;
    private Double repeat90Fix = null;
    private Double repeat120New = null;
    private Double repeat120Semifix = null;
    private Double repeat120Fix = null;
    private Double repeat180New = null;
    private Double repeat180Semifix = null;
    private Double repeat180Fix = null;
    private Double beforeReserve = null;
    private Double nextReserve = null;
    private Double newRate = null;
    private Integer newCustomer = null;
    private Integer decyl1Rate = null;
    private Integer decyl1Num = null;
    private Integer decyl2Rate = null;
    private Integer decyl2Num = null;
    private Integer decyl3Rate = null;
    private Integer decyl3Num = null;
    private Integer shopCategory = null;

    private Integer technicItem = null;

    public Integer getTechnicItem() {
        return technicItem;
    }

    public void setTechnicItem(Integer technicItem) {
        this.technicItem = technicItem;
    }

    public ArrayList<MstDataTarget> getSQLDataTarget(Integer shopId,
            Integer shopCategory, Integer targetYear, Integer targetMonth) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT b.year, b.month,");
        builder.append(" (SELECT technic");
        builder.append("  FROM data_target tg WHERE tg.year = b.year");
        builder.append("    AND tg.month = b.month");
        builder.append("    AND tg.shop_id = " + shopId + "");
        builder.append("    AND tg.shop_category_id=" + shopCategory + ") AS technic_amount,");
        builder.append("  (SELECT item");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS item_amount,");
        builder.append("  (SELECT technic+ item");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS amount,");
        builder.append("  (SELECT customer");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS customer_count,");
        builder.append("  (SELECT new_rate");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS new_customer_rate,");
        builder.append("  (SELECT new_customer");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS new_customer,");
        builder.append("  (SELECT repeat_90_new");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append(" AND tg.shop_category_id=" + shopCategory + ") AS repeat_90_new,");
        builder.append("  (SELECT repeat_90_semifix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_90_semifix,");
        builder.append("  (SELECT repeat_90_fix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_90_fix,");
        builder.append("  (SELECT repeat_120_new");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_120_new,");
        builder.append("  (SELECT repeat_120_semifix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_120_semifix,");
        builder.append("  (SELECT repeat_120_fix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_120_fix,");
        builder.append("  (SELECT repeat_180_new");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_180_new,");
        builder.append("  (SELECT repeat_180_semifix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_180_semifix,");
        builder.append("  (SELECT repeat_180_fix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_180_fix,");
        builder.append("  (SELECT before_reserve");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS before_reserve,");
        builder.append("  (SELECT next_reserve");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS next_reserve");
        builder.append(" FROM");
        builder.append("   (SELECT date_part('year',a.date) AS YEAR,");
        builder.append("           date_part('month',a.date) AS MONTH");
        builder.append("   FROM");
        builder.append("     (SELECT date '" + targetYear + "-" + targetMonth + "-01' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'1 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'2 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'3 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'4 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'5 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'6 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'7 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'8 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'9 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'10 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'11 month' AS date)a");
        builder.append(" )b");

        ConnectionWrapper con = SystemInfo.getConnection();
        ArrayList<MstDataTarget> listItem = new ArrayList<MstDataTarget>();

        try {
            ResultSetWrapper rs = con.executeQuery(builder.toString());
            while (rs.next()) {
                MstDataTarget targetItem = new MstDataTarget();
                  
                targetItem.setMonth(rs.getInt("month"));
                targetItem.setYear(rs.getInt("year"));
                targetItem.setBeforeReserve(rs.getDouble("before_reserve"));
                targetItem.setCustomer(rs.getInt("customer_count"));
                targetItem.setItem(rs.getInt("item_amount"));
                targetItem.setNewCustomer(rs.getInt("new_customer"));
                targetItem.setNewRate(rs.getDouble("new_customer_rate"));
                targetItem.setNextReserve(rs.getDouble("next_reserve"));
                targetItem.setRepert120Fix(rs.getDouble("repeat_120_fix"));
                targetItem.setRepert120New(rs.getDouble("repeat_120_new"));
                targetItem.setRepert120Semifix(rs.getDouble("repeat_120_semifix"));
                targetItem.setRepert180Fix(rs.getDouble("repeat_180_fix"));
                targetItem.setRepert180New(rs.getDouble("repeat_180_new"));
                targetItem.setRepert180Semifix(rs.getDouble("repeat_180_semifix"));
                targetItem.setRepert90Fix(rs.getDouble("repeat_90_fix"));
                targetItem.setRepert90New(rs.getDouble("repeat_90_new"));
                targetItem.setRepert90Semifix(rs.getDouble("repeat_90_semifix"));
                targetItem.setTechnic(rs.getInt("technic_amount"));
                targetItem.setTechnicItem(rs.getInt("amount"));

                listItem.add(targetItem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MstDataTarget.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listItem;
    }

}

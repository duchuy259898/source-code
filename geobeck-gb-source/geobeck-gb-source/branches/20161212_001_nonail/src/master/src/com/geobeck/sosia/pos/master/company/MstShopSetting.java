/*
 * MstShopSetting.java
 *
 * Created on 2010/04/02, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.logging.Level;

/**
 * ログイン店舗設定情報
 * @author geobeck
 */
public final class MstShopSetting
{
    /**
     * 店舗ID
     */
    private Integer shopID = null;
    /**
     * 有効顧客の期間設定１
     */
    private Integer validCustomerPeriod1 = null;
    /**
     * 有効顧客の期間設定２
     */
    private Integer validCustomerPeriod2 = null;
     /**
     * 有効顧客の期間設定3
     */
    private Integer validCustomerPeriod3 = null;
    /**
     * いとーさん分析　客数問題発見シートのスタッフ数（今年のスタイリスト数）
     */
    private Integer itoStaffCount1 = null;
    /**
     * いとーさん分析　客数問題発見シートのスタッフ数（昨年のスタイリスト数）
     */
    private Integer itoPrevStaffCount1 = null;
    /**
     * いとーさん分析　客数問題発見シートのスタッフ数（今年のアシスタント数）
     */
    private Integer itoStaffCount2 = null;
    /**
     * いとーさん分析　客数問題発見シートのスタッフ数（昨年のアシスタント数）
     */
    private Integer itoPrevStaffCount2 = null;

    /**
     * 時間帯分析　集計方法
     */
    private Integer timeAnalysisCondition1 = null;
    /**
     * 時間帯分析　時間条件
     */
    private Integer timeAnalysisCondition2 = null;
    private static boolean flg=true;
    private static final MstShopSetting instance = new MstShopSetting();
   // private static final MstShopSetting instance1 = new MstShopSetting(flg);
    private MstShopSetting()
    {
         this.setShopID(SystemInfo.isGroup() ? 0 : SystemInfo.getCurrentShop().getShopID());
        this.load();
    }
    
    public MstShopSetting(boolean flg) {
        
        this.setShopID(SystemInfo.isGroup() ? 0 : SystemInfo.getCurrentShop().getShopID());
        this.load1();
    }
    
    public static MstShopSetting getInstance() {
        return MstShopSetting.instance;
    }
    /*public static MstShopSetting getInstance1(boolean falg) {
        return MstShopSetting.instance1;
    }*/
    public Integer getShopID() {
        return shopID;
    }
    
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
        
    public Integer getValidCustomerPeriod1() {
        return validCustomerPeriod1;
    }

    public void setValidCustomerPeriod1(Integer validCustomerPeriod1) {
        this.validCustomerPeriod1 = validCustomerPeriod1;
    }

    public Integer getValidCustomerPeriod2() {
        return validCustomerPeriod2;
    }

    public void setValidCustomerPeriod2(Integer validCustomerPeriod2) {
        this.validCustomerPeriod2 = validCustomerPeriod2;
    }
    public void setValidCustomerPeriod3(Integer validCustomerPeriod3) {
        this.validCustomerPeriod3 = validCustomerPeriod3;
    }
     public Integer getValidCustomerPeriod3() {
        return validCustomerPeriod3;
    }

    public Integer getItoStaffCount1() {
        return itoStaffCount1;
    }

    public void setItoStaffCount1(Integer itoStaffCount1) {
        this.itoStaffCount1 = itoStaffCount1;
    }

    public Integer getItoPrevStaffCount1() {
        return itoPrevStaffCount1;
    }

    public void setItoPrevStaffCount1(Integer itoPrevStaffCount1) {
        this.itoPrevStaffCount1 = itoPrevStaffCount1;
    }

    public Integer getItoStaffCount2() {
        return itoStaffCount2;
    }

    public void setItoStaffCount2(Integer itoStaffCount2) {
        this.itoStaffCount2 = itoStaffCount2;
    }

    public Integer getItoPrevStaffCount2() {
        return itoPrevStaffCount2;
    }

    public void setItoPrevStaffCount2(Integer itoPrevStaffCount2) {
        this.itoPrevStaffCount2 = itoPrevStaffCount2;
    }

    public Integer getTimeAnalysisCondition1() {
        return timeAnalysisCondition1;
    }

    public void setTimeAnalysisCondition1(Integer timeAnalysisCondition1) {
        this.timeAnalysisCondition1 = timeAnalysisCondition1;
    }

    public Integer getTimeAnalysisCondition2() {
        return timeAnalysisCondition2;
    }

    public void setTimeAnalysisCondition2(Integer timeAnalysisCondition2) {
        this.timeAnalysisCondition2 = timeAnalysisCondition2;
    }
    
    private String getSelectSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_shop_setting");
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShopID()));

        return sql.toString();
    }
    
    private boolean load() {

        ConnectionWrapper con = SystemInfo.getConnection();
        ResultSetWrapper rs = null;
        
        try {

            rs = con.executeQuery(this.getSelectSQL());
            if (!rs.next()) {
                String sql = "insert into mst_shop_setting (shop_id) values (" + SQLUtil.convertForSQL(this.getShopID()) + ")";
                con.executeUpdate(sql.toString());
                rs = con.executeQuery(this.getSelectSQL());
                if (!rs.next()) return false;
            }
            
            this.setValidCustomerPeriod1(rs.getInt("valid_customer_period_1"));
            this.setValidCustomerPeriod2(rs.getInt("valid_customer_period_2"));
            this.setItoStaffCount1(rs.getInt("ito_staff_count1"));
            this.setItoStaffCount2(rs.getInt("ito_staff_count2"));
            this.setItoPrevStaffCount1(rs.getInt("ito_prev_staff_count1"));
            this.setItoPrevStaffCount2(rs.getInt("ito_prev_staff_count2"));
            this.setTimeAnalysisCondition1(rs.getInt("time_analysis_condition1"));
            this.setTimeAnalysisCondition2(rs.getInt("time_analysis_condition2"));
            
            rs.close();
            
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        return true;
    }
    
    public void regist() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_shop_setting");
        sql.append(" set");
        sql.append("      valid_customer_period_1 = " + SQLUtil.convertForSQL(this.getValidCustomerPeriod1()));
        sql.append("     ,valid_customer_period_2 = " + SQLUtil.convertForSQL(this.getValidCustomerPeriod2()));
        sql.append("     ,ito_staff_count1 = " + SQLUtil.convertForSQL(this.getItoStaffCount1()));
        sql.append("     ,ito_staff_count2 = " + SQLUtil.convertForSQL(this.getItoStaffCount2()));
        sql.append("     ,ito_prev_staff_count1 = " + SQLUtil.convertForSQL(this.getItoPrevStaffCount1()));
        sql.append("     ,ito_prev_staff_count2 = " + SQLUtil.convertForSQL(this.getItoPrevStaffCount2()));
        sql.append("     ,time_analysis_condition1 = " + SQLUtil.convertForSQL(this.getTimeAnalysisCondition1()));
        sql.append("     ,time_analysis_condition2 = " + SQLUtil.convertForSQL(this.getTimeAnalysisCondition2()));
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShopID()));

        try {

            SystemInfo.getConnection().executeUpdate(sql.toString());

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
     public void regist1() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_shop_setting");
        sql.append(" set");
        sql.append("      valid_customer_period_1 = " + SQLUtil.convertForSQL(this.getValidCustomerPeriod1()));
        sql.append("     ,valid_customer_period_2 = " + SQLUtil.convertForSQL(this.getValidCustomerPeriod2()));
        sql.append("     ,valid_customer_period_3 = " + SQLUtil.convertForSQL(this.getValidCustomerPeriod3()));
        sql.append("     ,ito_staff_count1 = " + SQLUtil.convertForSQL(this.getItoStaffCount1()));
        sql.append("     ,ito_staff_count2 = " + SQLUtil.convertForSQL(this.getItoStaffCount2()));
        sql.append("     ,ito_prev_staff_count1 = " + SQLUtil.convertForSQL(this.getItoPrevStaffCount1()));
        sql.append("     ,ito_prev_staff_count2 = " + SQLUtil.convertForSQL(this.getItoPrevStaffCount2()));
        sql.append("     ,time_analysis_condition1 = " + SQLUtil.convertForSQL(this.getTimeAnalysisCondition1()));
        sql.append("     ,time_analysis_condition2 = " + SQLUtil.convertForSQL(this.getTimeAnalysisCondition2()));
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShopID()));

        try {

            SystemInfo.getConnection().executeUpdate(sql.toString());

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
     
      private boolean load1() {

        ConnectionWrapper con = SystemInfo.getConnection();
        ResultSetWrapper rs = null;
        
        try {

            rs = con.executeQuery(this.getSelectSQL());
            if (!rs.next()) {
                String sql = "insert into mst_shop_setting (shop_id) values (" + SQLUtil.convertForSQL(this.getShopID()) + ")";
                con.executeUpdate(sql.toString());
                rs = con.executeQuery(this.getSelectSQL());
                if (!rs.next()) return false;
            }
            
            this.setValidCustomerPeriod1(rs.getInt("valid_customer_period_1"));
            this.setValidCustomerPeriod2(rs.getInt("valid_customer_period_2"));
            this.setValidCustomerPeriod3(rs.getInt("valid_customer_period_3"));
            this.setItoStaffCount1(rs.getInt("ito_staff_count1"));
            this.setItoStaffCount2(rs.getInt("ito_staff_count2"));
            this.setItoPrevStaffCount1(rs.getInt("ito_prev_staff_count1"));
            this.setItoPrevStaffCount2(rs.getInt("ito_prev_staff_count2"));
            this.setTimeAnalysisCondition1(rs.getInt("time_analysis_condition1"));
            this.setTimeAnalysisCondition2(rs.getInt("time_analysis_condition2"));
            
            rs.close();
            
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        return true;
    }
    
}

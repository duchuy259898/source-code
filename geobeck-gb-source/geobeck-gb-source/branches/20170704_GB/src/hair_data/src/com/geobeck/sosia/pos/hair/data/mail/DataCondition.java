/*
 * DataCondition.java
 *
 * Created on 2012/03/17, 12:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.mail;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.util.*;
import java.util.logging.Level;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.util.SQLUtil;
import com.geobeck.util.DateUtil;
import java.sql.Timestamp;

/**
 * 条件検索テンプレート
 * @author geobeck
 */
public class DataCondition {

    private Integer shopID = null;
    private Integer condID = null;
    private String condName = null;
    private Timestamp insDate = null;

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getCondID() {
        return condID;
    }

    public void setCondID(Integer condID) {
        this.condID = condID;
    }

    public String getCondName() {
        return condName;
    }

    public void setCondName(String condName) {
        this.condName = condName;
    }

    public String toString() {
        return condName;
    }

    public Timestamp getInsDate() {
        return insDate;
    }

    public void setInsDate(Timestamp insDate) {
        this.insDate = insDate;
    }

    public String getInsDateStrDateOnly() {
        return DateUtil.format(getInsDate(), "yyyy/MM/dd");
    }

    public static ArrayList<DataCondition> getList(MstShop shop) {

        ArrayList<DataCondition> result = new ArrayList<DataCondition>();

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_condition");
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append(" order by");
        sql.append("     insert_date desc");

        try {

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                DataCondition dc = new DataCondition();
                dc.setShopID(rs.getInt("shop_id"));
                dc.setCondID(rs.getInt("cond_id"));
                dc.setCondName(rs.getString("cond_name"));
                dc.setInsDate(rs.getTimestamp("insert_date"));
                result.add(dc);
            }

            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    public boolean delete() {

        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {

            con.begin();

            try {
                StringBuilder sql = null;

                sql = new StringBuilder(1000);
                sql.append(" delete from data_condition");
                sql.append(" where");
                sql.append("         shop_id = " + SQLUtil.convertForSQL(getShopID()));
                sql.append("     and cond_id = " + SQLUtil.convertForSQL(getCondID()));
                con.executeUpdate(sql.toString());

                sql = new StringBuilder(1000);
                sql.append(" delete from data_condition_detail");
                sql.append(" where");
                sql.append("         shop_id = " + SQLUtil.convertForSQL(getShopID()));
                sql.append("     and cond_id = " + SQLUtil.convertForSQL(getCondID()));
                con.executeUpdate(sql.toString());

                con.commit();
                result = true;

            } catch (Exception e) {
                con.rollback();
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

}

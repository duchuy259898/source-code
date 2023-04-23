/*
 * DataConditionDetail.java
 *
 * Created on 2012/03/17, 12:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.mail;

import java.util.*;
import java.util.logging.Level;

import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.util.SQLUtil;

/**
 * 条件検索テンプレート
 * @author geobeck
 */
public class DataConditionDetail {

    private Integer shopID = null;
    private Integer condID = null;
    private Integer itemNo = null;
    private String itemValue = null;

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

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public static ArrayList<DataConditionDetail> getList(DataCondition dc) {

        ArrayList<DataConditionDetail> result = new ArrayList<DataConditionDetail>();

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_condition_detail");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(dc.getShopID()));
        sql.append("     and cond_id = " + SQLUtil.convertForSQL(dc.getCondID()));
        sql.append(" order by");
        sql.append("     item_no");
        try {

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                DataConditionDetail dd = new DataConditionDetail();
                dd.setShopID(rs.getInt("shop_id"));
                dd.setCondID(rs.getInt("cond_id"));
                dd.setItemNo(rs.getInt("item_no"));
                dd.setItemValue(rs.getString("item_value"));
                result.add(dd);
            }

            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

}

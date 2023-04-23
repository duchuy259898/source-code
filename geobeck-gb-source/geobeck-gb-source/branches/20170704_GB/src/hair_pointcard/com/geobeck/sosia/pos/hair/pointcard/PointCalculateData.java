/*
 * PointCalculateData.java
 *
 * Created on 2008/09/08, 17:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;
import java.util.logging.Level;

/**
 *
 * @author shiera.delusa
 */
public class PointCalculateData {

    public static final int NULL_VALUE = -1;
    private ConnectionWrapper dbConnection;

    /**
     * Creates a new instance of PointCalculateData
     */
    public PointCalculateData() {
        this.dbConnection = SystemInfo.getConnection();
    }

    /**
     * ポイント計算レートを作成する
     *
     * @param bean
     * @return
     */
    public boolean insertPointCalculation(PointCalculateBean bean) {
        try {
            this.dbConnection.execute(getInsertSQLStatement(bean));
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * ポイント計算レートを更新する
     *
     * @param bean
     * @return
     */
    public boolean updatePointCalculation(PointCalculateBean bean) {
        try {
            this.dbConnection.execute(getUpdateSQLStatement(bean));
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * ポイント計算データを取得する
     *
     * @param shop_id
     * @return ポイント計算データ 取得できなかった場合は、null
     */
    public PointCalculateBean getPointCalculateData(Integer shop_id) {

        PointCalculateBean bean = null;
        String queryString = getSelectSQLStatement(shop_id);
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery(queryString);
            if (result.next()) {
                bean = new PointCalculateBean();
                bean.setShopid(result.getInt("shop_id"));
                bean.setBasicRate(result.getInt("basic_rate"));
                bean.setBasicPoint(result.getInt("basic_point"));
                bean.setBasicRate2(result.getInt("basic_rate2"));
                bean.setBasicPoint2(result.getInt("basic_point2"));
                bean.setBasicRate3(result.getInt("basic_rate3"));
                bean.setBasicPoint3(result.getInt("basic_point3"));
                bean.setBasicRate4(result.getInt("basic_rate4"));
                bean.setBasicPoint4(result.getInt("basic_point4"));
                bean.setBasicTaxType(result.getInt("basic_tax_type"));
                bean.setBasicTarget(result.getInt("basic_target"));
                bean.setBasicTarget2(result.getInt("basic_target2"));
                bean.setVisitPoint(result.getInt("visit_point"));
                bean.setFirstTimeEnabled(result.getInt("first_time_enabled"));
                bean.setFirstTimePoint(result.getInt("first_time_point"));
                bean.setFirstTimeRate(result.getDouble("first_time_rate"));
                bean.setBirthdayEnabled(result.getInt("birthday_enabled"));
                bean.setBirthdayCond(result.getInt("birthday_cond"));
                bean.setBirthdayRange(result.getInt("birthday_range"));
                bean.setBirthdayPoint(result.getInt("birthday_point"));
                bean.setBirthdayRate(result.getDouble("birthday_rate"));
                bean.setCashbackPoint(result.getInt("cashback_point"));
                bean.setCashbackRate(result.getInt("cashback_rate"));
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            System.out.println(e.getLocalizedMessage());
        }
        return bean;
    }

    private String getInsertSQLStatement(PointCalculateBean bean) {
        String COMMA = ",";
        String sqlStatement = "INSERT INTO mst_point_calculate("
                + "shop_id,"
                + "basic_rate,"
                + "basic_point,"
                + "basic_rate2,"
                + "basic_point2,"
                + "basic_rate3,"
                + "basic_point3,"
                + "basic_rate4,"
                + "basic_point4,"
                + "basic_tax_type,"
                + "basic_target,"
                + "basic_target2,"
                + "visit_point,"
                + "first_time_enabled,"
                + "first_time_point,"
                + "first_time_rate,"
                + "birthday_enabled,"
                + "birthday_cond,"
                + "birthday_range,"
                + "birthday_point,"
                + "birthday_rate,"
                + "cashback_point,"
                + "cashback_rate,"
                + "insert_date,"
                + "update_date )"
                + "VALUES("
                + SQLUtil.convertForSQL(bean.getShopid()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicRate()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicPoint()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicRate2()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicPoint2()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicRate3()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicPoint3()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicRate4()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicPoint4()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicTaxType()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicTarget()) + COMMA
                + SQLUtil.convertForSQL(bean.getBasicTarget2()) + COMMA
                + SQLUtil.convertForSQL(bean.getVisitPoint()) + COMMA
                + SQLUtil.convertForSQL(bean.getFirstTimeEnabled()) + COMMA
                + SQLUtil.convertForSQL(bean.getFirstTimePoint()) + COMMA
                + SQLUtil.convertForSQL(bean.getFirstTimeRate()) + COMMA
                + SQLUtil.convertForSQL(bean.getBirthdayEnabled()) + COMMA
                + SQLUtil.convertForSQL(bean.getBirthdayCond()) + COMMA
                + SQLUtil.convertForSQL(bean.getBirthdayRange()) + COMMA
                + SQLUtil.convertForSQL(bean.getBirthdayPoint()) + COMMA
                + SQLUtil.convertForSQL(bean.getBirthdayRate()) + COMMA
                + SQLUtil.convertForSQL(bean.getCashbackPoint() == null ? 0 : bean.getCashbackPoint()) + COMMA
                + SQLUtil.convertForSQL(bean.getCashbackRate() == null ? 0 : bean.getCashbackRate()) + COMMA
                + "current_timestamp, current_timestamp)";

        return sqlStatement;
    }

    private String getUpdateSQLStatement(PointCalculateBean bean) {
        String COMMA = ",";
        String sqlStatement = "update mst_point_calculate set"
                + " basic_rate = " + SQLUtil.convertForSQL(bean.getBasicRate()) + COMMA
                + " basic_point = " + SQLUtil.convertForSQL(bean.getBasicPoint()) + COMMA
                + " basic_rate2 = " + SQLUtil.convertForSQL(bean.getBasicRate2()) + COMMA
                + " basic_point2 = " + SQLUtil.convertForSQL(bean.getBasicPoint2()) + COMMA
                + " basic_rate3 = " + SQLUtil.convertForSQL(bean.getBasicRate3()) + COMMA
                + " basic_point3 = " + SQLUtil.convertForSQL(bean.getBasicPoint3()) + COMMA
                + " basic_rate4 = " + SQLUtil.convertForSQL(bean.getBasicRate4()) + COMMA
                + " basic_point4 = " + SQLUtil.convertForSQL(bean.getBasicPoint4()) + COMMA
                + " basic_tax_type = " + SQLUtil.convertForSQL(bean.getBasicTaxType()) + COMMA
                + " basic_target = " + SQLUtil.convertForSQL(bean.getBasicTarget()) + COMMA
                + " basic_target2 = " + SQLUtil.convertForSQL(bean.getBasicTarget2()) + COMMA
                + " visit_point = " + SQLUtil.convertForSQL(bean.getVisitPoint()) + COMMA
                + " first_time_enabled = " + SQLUtil.convertForSQL(bean.getFirstTimeEnabled()) + COMMA
                + " first_time_point = " + SQLUtil.convertForSQL(bean.getFirstTimePoint()) + COMMA
                + " first_time_rate = " + SQLUtil.convertForSQL(bean.getFirstTimeRate()) + COMMA
                + " birthday_enabled = " + SQLUtil.convertForSQL(bean.getBirthdayEnabled()) + COMMA
                + " birthday_cond = " + SQLUtil.convertForSQL(bean.getBirthdayCond()) + COMMA
                + " birthday_range = " + SQLUtil.convertForSQL(bean.getBirthdayRange()) + COMMA
                + " birthday_point = " + SQLUtil.convertForSQL(bean.getBirthdayPoint()) + COMMA
                + " birthday_rate = " + SQLUtil.convertForSQL(bean.getBirthdayRate()) + COMMA
                + " cashback_point = " + SQLUtil.convertForSQL(bean.getCashbackPoint() == null ? 0 : bean.getCashbackPoint()) + COMMA
                + " cashback_rate = " + SQLUtil.convertForSQL(bean.getCashbackRate()==null ? 0 : bean.getCashbackRate()) + COMMA
                + " update_date  = current_timestamp"
                + " WHERE shop_id = " + SQLUtil.convertForSQL(bean.getShopid());
        return sqlStatement;
    }

    private String getSelectSQLStatement(Integer shop_id) {
        String sqlStatement = "SELECT "
                + "shop_id,"
                + "basic_rate,"
                + "basic_point,"
                + "basic_rate2,"
                + "basic_point2,"
                + "basic_rate3,"
                + "basic_point3,"
                + "basic_rate4,"
                + "basic_point4,"
                + "basic_tax_type,"
                + "basic_target,"
                + "basic_target2,"
                + "visit_point,"
                + "first_time_enabled,"
                + "first_time_point,"
                + "first_time_rate,"
                + "birthday_enabled,"
                + "birthday_cond,"
                + "birthday_range,"
                + "birthday_point,"
                + "birthday_rate,"
                + "cashback_point,"
                + "cashback_rate,"
                + "insert_date,"
                + "update_date,"
                + "delete_date"
                + " FROM mst_point_calculate"
                + " WHERE shop_id = " + SQLUtil.convertForSQL(shop_id)
                + " AND delete_date is  null";

        return sqlStatement;
    }
}

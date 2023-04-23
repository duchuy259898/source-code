/*
 * DataResponseEffect.java
 *
 * Created on 2007/08/29, 13:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.company;

import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.master.company.*;

/**
 *
 * @author kanemoto
 */
public class DataResponseEffect {

    protected Integer slipNo = null;	// 伝票No
    protected MstResponse response = null;	// レスポンスマスタ
    protected Integer shopID = null;	// 店舗ID
    protected DataResponseIssue responseIssue = null;	// レスポンス発行データ
    protected GregorianCalendar dataResponseDate = null;	// レスポンス回収日

    /** Creates a new instance of DataResponseEffect */
    public DataResponseEffect() {
    }

    /**
     * 伝票Noを取得する
     * @return 伝票No
     */
    public Integer getSlipNo() {
        return this.slipNo;
    }

    /**
     * 伝票Noを登録する
     * @param slipNo 伝票No
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * レスポンスを取得する
     * @return レスポンス
     */
    public MstResponse getResponse() {
        return this.response;
    }

    /**
     * レスポンスを登録する
     * @param response レスポンス
     */
    public void setResponse(MstResponse response) {
        this.response = response;
    }

    /**
     * 店舗IDを取得する
     * @return レスポンス
     */
    public Integer getShopID() {
        return this.shopID;
    }

    /**
     * 店舗IDを登録する
     * @param shopID レスポンス
     */
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    /**
     * レスポンス発行データを取得する
     * @return レスポンス
     */
    public DataResponseIssue getResponseIssue() {
        return this.responseIssue;
    }

    /**
     * レスポンス発行データを登録する
     * @param responseIssue レスポンス
     */
    public void setResponseIssue(DataResponseIssue responseIssue) {
        this.responseIssue = responseIssue;
    }

    /**
     * レスポンス回収日を取得する
     * @return レスポンス回収日
     */
    public GregorianCalendar getDataResponseDate() {
        return this.dataResponseDate;
    }

    /**
     * レスポンス回収日を登録する
     * @param dataResponseDate レスポンス回収日
     */
    public void setDataResponseDate(GregorianCalendar dataResponseDate) {
        this.dataResponseDate = dataResponseDate;
    }

    /**
     * DataResponseEffectからデータをセットする。
     * @param dre DataResponseEffect
     */
    public void setData(DataResponseEffect dre) {
        this.setSlipNo(dre.getSlipNo());
        this.setResponse(dre.getResponse());
        this.setShopID(dre.getShopID());
        this.setResponseIssue(dre.getResponseIssue());
        this.setDataResponseDate(dre.getDataResponseDate());
    }

    /**
     * ResultSetWrapperからデータをセットする。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setSlipNo(rs.getInt("slip_no"));
        MstResponse mr = new MstResponse();
        mr.setData(rs);
        this.setResponse(mr);
        this.setShopID(rs.getInt("shop_id"));
        DataResponseIssue mri = new DataResponseIssue();
        mri.setData(rs);
        this.setResponseIssue(mri);
        this.setDataResponseDate(rs.getGregorianCalendar("data_response_date"));
    }

    /**
     * レスポンスデータを登録する。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";
        
        // レスポンスが存在しない場合には処理を行わなわず、成功として終了。
        if (this.getResponse() == null || this.getResponse().getResponseID() <= 0) {
            return true;
        }

        // レコードの有無を確認し、状態に沿ったSQLを取得する
        if (isExists(con)) {
            // レスポンスが存在しない場合には削除SQLを実行
            if (this.getResponse().getResponseID() <= 0) {
                sql = this.getDeleteSQL();
            } else {
                sql = this.getUpdateSQL();
            }
        } else {
            sql = this.getInsertSQL();
        }
        // SQLの実行を行う
        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteAll(ConnectionWrapper con, Integer shopID, Integer slipNo) {
        boolean result = false;

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update data_response_effect");
        sql.append(" set");
        sql.append("      update_date = current_timestamp");
        sql.append("     ,delete_date = current_timestamp");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(shopID));
        sql.append("     and slip_no = " + SQLUtil.convertForSQL(slipNo));

        try {

            con.executeUpdate(sql.toString());
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * レスポンスデータを削除する。（論理削除）
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        // レコードが存在しない場合には処理を行わない
        if (!isExists(con)) {
            return false;
        }

        // 削除用SQLを取得する
        sql = this.getDeleteSQL();

        if (con.executeUpdate(sql) == 1) {
            return true;
        }
        return false;
    }

    /**
     * レスポンスデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        //Edit start 2103-11-01 Hoa
        if (con == null || this.getResponse() == null) {
            return false;
        }
        //Edit start 2103-11-01 Hoa
        ResultSetWrapper rs = con.executeQuery(this.getExistsSelectSQL());
        return rs.next();
    }

    /**
     * レスポンスデータ存在取得SQL
     */
    public String getExistsSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_response_effect as dre");
        sql.append(" where");
        sql.append("         dre.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     AND dre.slip_no = coalesce( " + SQLUtil.convertForSQL(this.getSlipNo()) + ", 0 )");
        sql.append("     AND dre.response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()));

        return sql.toString();
    }

    public String getInsertSQL() {
        return "insert into data_response_effect\n"
                + "(slip_no, response_id, response_issue_id, data_response_date, shop_id, insert_date, \n"
                + "update_date, delete_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + ",\n"
                + (this.getResponseIssue() == null ? "-1" : SQLUtil.convertForSQL(this.getResponseIssue().getResponseIssueId())) + ",\n"
                + SQLUtil.convertForSQL(this.getDataResponseDate()) + ",\n"
                + SQLUtil.convertForSQL(this.getShopID()) + ",\n"
                + "current_timestamp, current_timestamp, null );\n";
    }

    public String getUpdateSQL() {
        return "update\n"
                + "data_response_effect\n"
                + "set\n"
                + "response_issue_id = " + (this.getResponseIssue() == null ? "-1" : SQLUtil.convertForSQL(this.getResponseIssue().getResponseIssueId())) + ", \n"
                + "data_response_date = " + SQLUtil.convertForSQL(this.getDataResponseDate()) + ", \n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ", \n"
                + "update_date = current_timestamp,\n"
                + "delete_date = null\n"
                + "where\n"
                + "     shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + " and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + " and response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + "\n"
                + ";\n";
    }

    public String getDeleteSQL() {
        return "update data_response_effect\n"
                + "set\n"
                + "update_date = current_timestamp, \n"
                + "delete_date = current_timestamp\n"
                + "where\n"
                + "     shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + " and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + " and response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + "\n"
                + ";\n";
    }
}

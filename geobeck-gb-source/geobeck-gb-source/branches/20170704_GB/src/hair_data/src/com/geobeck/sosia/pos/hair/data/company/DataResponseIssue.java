/*
 * DataResponseIssue.java
 *
 * Created on 2007/08/31, 10:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.company;

import java.util.*;
import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.master.company.*;

/**
 * レスポンス発行データ
 * @author kanemoto
 */
public class DataResponseIssue {

    protected Integer responseIssueId;
    /** レスポンス発行ID */
    protected MstResponse response;
    /** レスポンス */
    protected Integer shop_id;
    /** 店舗ID */
    protected MstStaff staff;
    /** 発行スタッフ */
    protected GregorianCalendar circulationMonthlyDate;
    /** 発行年月 */
    protected Integer circulationNumber;
    /** 発行部数 */
    protected GregorianCalendar registDate;

    /** 登録日 */
    /** Creates a new instance of DataResponseIssue */
    public DataResponseIssue() {
    }

    /**
     * レスポンス発行IDを取得する
     * @return レスポンス発行ID
     */
    public Integer getResponseIssueId() {
        return this.responseIssueId;
    }

    /**
     * レスポンス発行IDを登録する
     * @param responseIssueId レスポンス発行ID
     */
    public void setResponseIssueId(Integer responseIssueId) {
        this.responseIssueId = responseIssueId;
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
     * レスポンスを取得する
     * @return レスポンス
     */
    public Integer getShopID() {
        return this.shop_id;
    }

    /**
     * 店舗IDを登録する
     * @param shop_id 店舗ID
     */
    public void setShopID(Integer shop_id) {
        this.shop_id = shop_id;
    }

    /**
     * 発行スタッフを取得する
     * @return 発行スタッフ
     */
    public MstStaff getStaff() {
        return this.staff;
    }

    /**
     * 発行スタッフを登録する
     * @param response 発行スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * 発行年月を取得する
     * @return 発行年月
     */
    public GregorianCalendar getCirculationMonthlyDate() {
        return this.circulationMonthlyDate;
    }

    /**
     * 発行年月を登録する
     * @param circulationMonthlyDate 発行年月
     */
    public void setCirculationMonthlyDate(GregorianCalendar circulationMonthlyDate) {
        this.circulationMonthlyDate = circulationMonthlyDate;
    }

    /**
     * 発行部数を取得する
     * @return 発行部数
     */
    public Integer getCirculationNumber() {
        return this.circulationNumber;
    }

    /**
     * 発行部数を登録する
     * @param circulationNumber 発行部数
     */
    public void setCirculationNumber(Integer circulationNumber) {
        this.circulationNumber = circulationNumber;
    }

    /**
     * 登録日を取得する
     * @return 登録日
     */
    public GregorianCalendar getRegistDate() {
        return this.registDate;
    }

    /**
     * 登録日を登録する
     * @param registDate 登録日
     */
    public void setRegistDate(GregorianCalendar registDate) {
        this.registDate = registDate;
    }

    /**
     * DataResponseIssue。
     * @param dri DataResponseIssue
     */
    public void setData(DataResponseIssue dri) {
        this.setResponseIssueId(dri.getResponseIssueId());
        this.setResponse(dri.getResponse());
        this.setShopID(dri.getShopID());
        this.setStaff(dri.getStaff());
        this.setCirculationMonthlyDate(dri.getCirculationMonthlyDate());
        this.setCirculationNumber(dri.getCirculationNumber());
        this.setRegistDate(dri.getRegistDate());
    }

    /**
     * ResultSetWrapperからデータをセットする。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setResponseIssueId(rs.getInt("response_issue_id"));
        MstResponse mr = new MstResponse();
        mr.setData(rs);
        this.setResponse(mr);
        this.setShopID(rs.getInt("shop_id"));
        MstStaff ms = new MstStaff();
        ms.setStaffNo(rs.getString("staff_no"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
        this.setStaff(ms);
        this.setCirculationMonthlyDate(rs.getGregorianCalendar("circulation_monthly_date"));
        this.setCirculationNumber(rs.getInt("circulation_number"));
        this.setRegistDate(rs.getGregorianCalendar("regist_date"));
    }

    /**
     * レスポンスデータを登録する。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";

        // レコードが存在しない場合には新規登録を行う
        if (!isExists(con)) {
            sql = this.getInsertSQL();
        } else {
            sql = this.getUpdateSQL();
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        }
        return false;
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
        if (this.getResponseIssueId() == null || this.getResponseIssueId() == 0) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getExistsSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * レスポンスデータ存在取得SQL
     */
    public String getExistsSelectSQL() {
        return "select\n"
                + "*\n"
                + "from\n"
                + "data_response_issue\n"
                + "where\n"
                + "response_issue_id = " + SQLUtil.convertForSQL(this.getResponseIssueId()) + "\n"
                + ";\n";
    }

    /**
     * 出退勤データ挿入SQLを取得する
     */
    public String getInsertSQL() {
        return "insert into data_response_issue\n"
                + "(response_issue_id, response_id, shop_id, staff_id, \n"
                + "circulation_monthly_date, circulation_number, regist_date, insert_date, \n"
                + "update_date, delete_date)\n"
                + "values(\n"
                + "( select max( response_issue_id ) from data_response_issue ) + 1,\n"
                + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + ",\n"
                + SQLUtil.convertForSQL(this.getShopID()) + ",\n"
                + ((this.getStaff() == null) ? "null" : SQLUtil.convertForSQL(this.getStaff().getStaffID())) + ",\n"
                + ((this.getCirculationMonthlyDate() == null) ? "null" : SQLUtil.convertForSQL(this.getCirculationMonthlyDate())) + ",\n"
                + SQLUtil.convertForSQL(this.getCirculationNumber()) + ",\n"
                + SQLUtil.convertForSQL(this.getRegistDate()) + ",\n"
                + "current_timestamp, current_timestamp, null );\n";
    }

    /**
     * 出退勤データ更新SQLを取得する
     */
    public String getUpdateSQL() {
        return "update data_response_issue\n"
                + "set\n"
                + "response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + ", \n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ", \n"
                + ((this.getStaff() == null) ? ("staff_id = null,\n") : ("staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ", \n"))
                + "circulation_monthly_date = " + SQLUtil.convertForSQL(this.getCirculationMonthlyDate()) + ", \n"
                + "circulation_number = " + SQLUtil.convertForSQL(this.getCirculationNumber()) + ", \n"
                + "regist_date = " + SQLUtil.convertForSQL(this.getRegistDate()) + ", \n"
                + "update_date = current_timestamp\n"
                + "where\n"
                + "response_issue_id = " + SQLUtil.convertForSQL(this.getResponseIssueId()) + "\n"
                + ";\n";
    }

    /**
     * 出退勤データ削除SQLを取得する
     */
    public String getDeleteSQL() {
        return "update data_response_issue\n"
                + "set update_date = current_timestamp, \n"
                + "delete_date = current_timestamp\n"
                + "where\n"
                + "response_issue_id = " + SQLUtil.convertForSQL(this.getResponseIssueId()) + "\n"
                + ";\n";
    }
}

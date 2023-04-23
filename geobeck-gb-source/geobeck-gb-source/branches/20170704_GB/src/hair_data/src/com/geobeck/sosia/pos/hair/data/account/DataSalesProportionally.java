/*
 * DataSalesProportionally.java
 *
 * Created on 2007/10/16, 9:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.account;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;

/**
 *
 * @author kanemoto
 */
public class DataSalesProportionally {

    protected DataSalesDetail dataSalesDetail = null;
    /** 精算詳細 */
    protected DataProportionally proportionally = new DataProportionally();
    /** 按分 */
    protected MstStaff staff = new MstStaff();
    /** スタッフ */
    protected boolean designated = false;
    /** 指名 */
    protected Integer point = null;
    /** ポイント */
    protected Integer rate = null;

    /** 一意連番（主キーの一部） */
    private Integer seqNum = null;

    /** 割合 */
    /** Creates a new instance of DataSalesProportionally */
    public DataSalesProportionally() {
    }

    /**
     * 店舗を取得する。
     * @return 店舗
     */
    public MstShop getShop() {
        if (dataSalesDetail != null) {
            return dataSalesDetail.getShop();
        } else {
            return null;
        }
    }

    /**
     * 伝票Noを取得する
     */
    public Integer getSlipNo() {
        if (dataSalesDetail != null) {
            return dataSalesDetail.getSlipNo();
        } else {
            return null;
        }
    }

    /**
     * 伝票詳細No.を取得する。
     * @return 伝票詳細No.
     */
    public Integer getSlipDetailNo() {
        if (dataSalesDetail != null) {
            return dataSalesDetail.getSlipDetailNo();
        } else {
            return null;
        }
    }

    /**
     * 伝票詳細を取得する。
     * @return 伝票詳細
     */
    public DataSalesDetail getSalesDetail() {
        return dataSalesDetail;
    }

    /**
     * 伝票詳細をセットする。
     * @param dataSalesDetail 伝票詳細
     */
    public void setDataSalesDetail(DataSalesDetail dataSalesDetail) {
        this.dataSalesDetail = dataSalesDetail;
    }

    /**
     * 按分を取得する。
     * @return 按分
     */
    public DataProportionally getProportionally() {
        return proportionally;
    }

    /**
     * 按分IDを取得する。
     * @return 按分ID
     */
    public Integer getDataProportionallyID() {
        if (proportionally != null) {
            return proportionally.getDataProportionallyID();
        }
        return null;
    }

    /**
     * 按分をセットする。
     * @param technic 按分
     */
    public void setProportionally(DataProportionally proportionally) {
        this.proportionally = proportionally;
    }

    /**
     * スタッフを取得する。
     * @return スタッフ
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * スタッフをセットする。
     * @param staff スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * 指名フラグを取得する
     * @return 指名 true:指名 false:フリー
     */
    public boolean getDesignated() {
        return designated;
    }

    /**
     * 指名フラグをセットする
     * @param designated 指名フラグ
     */
    public void setDesignated(boolean designated) {
        this.designated = designated;
    }

    /**
     * ポイントを取得する
     * return 按分ポイント
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * ポイントをセットする
     * @param point 按分ポイント
     */
    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * 割合を取得する
     * return 割合
     */
    public Integer getRate() {
        return rate;
    }

    /**
     * 割合をセットする
     * @param rate 割合
     */
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    /**
     * @return the seqNum
     */
    public Integer getSeqNum() {
        return seqNum;
    }

    /**
     * @param seqNum the seqNum to set
     */
    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    /**
     * 予約按分データをセットする。
     * @param dr 予約按分データ
     */
    public void set(DataSalesProportionally dsp) {
        this.setDataSalesDetail(dsp.getSalesDetail());
        this.setProportionally(dsp.getProportionally());
        this.setDesignated(dsp.getDesignated());
        this.setStaff(dsp.getStaff());
        this.setPoint(dsp.getPoint());
        this.setRate(dsp.getRate());
        this.setSeqNum(dsp.getSeqNum());
    }

    /**
     * データを登録する。
     * @param con
     * @throws java.sql.SQLException
     * @return
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getUpdateSQL();
        } else {
            sql = this.getInsertSQL();
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * データを削除する。（論理削除）
     * @param con
     * @throws java.sql.SQLException
     * @return
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getDeleteSQL();
        } else {
            return false;
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * データが存在するかチェックする。
     * @param con
     * @throws java.sql.SQLException
     * @return
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getSlipNo() == null
                || this.getSlipDetailNo() == null
                || this.getDataProportionallyID() == null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Select文を取得する。
     * @return
     */
    private String getSelectSQL() {
        return "select\n"
                + "*\n"
                + "from\n"
                + "data_sales_proportionally\n"
                + "where\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "and seq_num = " + SQLUtil.convertForSQL(this.getSeqNum()) + "\n"
                + ";\n";
    }

    /**
     * Insert文を取得する。
     * @return
     */
    private String getInsertSQL() {
        return "insert into data_sales_proportionally\n"
                + "(shop_id, slip_no, slip_detail_no, data_proportionally_id, seq_num,\n"
                + "designated_flag, staff_id, point, ratio,\n"
                + "insert_date, update_date, delete_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipDetailNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getDataProportionallyID()) + ",\n"
                + "(\n"
                + "select coalesce(max(seq_num), 0) + 1 from data_sales_proportionally\n"
                + "where\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "),\n"
                + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + (staff != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID()) : null) + ",\n"
                + SQLUtil.convertForSQL(this.getPoint()) + ",\n"
                + SQLUtil.convertForSQL(this.getRate()) + ",\n"
                + "current_timestamp, current_timestamp, null );";
    }

    /**
     * Update文を取得する。
     * @return
     */
    private String getUpdateSQL() {
        return "update\n"
                + "data_sales_proportionally\n"
                + "set\n"
                + "designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + "staff_id = " + (staff != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID()) : null) + ", \n"
                + "point = " + SQLUtil.convertForSQL(this.getPoint()) + ", \n"
                + "ratio = " + SQLUtil.convertForSQL(this.getRate()) + ", \n"
                + "update_date = current_timestamp, \n"
                + "delete_date = null\n"
                + "where\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "and seq_num = " + SQLUtil.convertForSQL(this.getSeqNum()) + "\n"
                + ";\n";
    }

    /**
     * 削除用Update文を取得する。
     * @return
     */
    private String getDeleteSQL() {
        return "update data_sales_proportionally\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where \n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "and seq_num = " + SQLUtil.convertForSQL(this.getSeqNum()) + "\n"
                + ";\n";
    }

}

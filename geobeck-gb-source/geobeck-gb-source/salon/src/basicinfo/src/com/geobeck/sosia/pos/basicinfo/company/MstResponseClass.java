/*
 * MstStaffClass.java
 *
 * Created on 2006/04/25, 17:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * スタッフ区分データ
 *
 * @author katagiri
 */
public class MstResponseClass {

    /**
     * レスポンス分類ID
     */
    private Integer responseClassId = null;
    /**
     * レスポンス分類名
     */
    private String responseClassName = "";
    /**
     * 表示順
     */
    private Integer displaySeq = null;
    /**
     * 登録日
     */
    private Timestamp insertDate = null;
    /**
     *
     */
    private Timestamp updateDate = null;
    /**
     *
     */
    private Timestamp deleteDate = null;

    public MstResponseClass() {
    }

    /**
     * 文字列に変換する。（スタッフ区分名）
     *
     * @return スタッフ区分名
     */
    public String toString() {
        return this.getResponseClassName();
    }

    /**
     * スタッフ区分ＩＤを取得する。
     *
     * @return スタッフ区分ＩＤ
     */
    public Integer getResponseClassId() {
        return responseClassId;
    }

    /**
     * スタッフ区分ＩＤをセットする。
     *
     * @param responseClassId スタッフ区分ＩＤ
     */
    public void setResponseClassId(Integer responseClassId) {
        this.responseClassId = responseClassId;
    }

    /**
     * スタッフ区分名を取得する。
     *
     * @return スタッフ区分名
     */
    public String getResponseClassName() {
        return responseClassName;
    }

    /**
     * スタッフ区分名をセットする。
     *
     * @param responseClassName スタッフ区分名
     */
    public void setetResponseClassName(String responseClassName) {
        this.responseClassName = responseClassName;
    }

    /**
     * 表示順を取得する。
     *
     * @return 表示順
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * 表示順をセットする。
     *
     * @param displaySeq 表示順
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Timestamp getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Timestamp deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * データをクリアする。
     */
    public void clear() {
        this.setResponseClassId(null);
        this.setetResponseClassName("");
        this.setDisplaySeq(0);
        this.setInsertDate(null);
        this.setUpdateDate(null);
        this.setDeleteDate(null);
    }

    /**
     * スタッフ区分マスタデータからデータをセットする。
     *
     * @param msc スタッフ区分マスタデータ
     */
    public void setData(MstResponseClass msc) {
        this.setResponseClassId(msc.getResponseClassId());
        this.setetResponseClassName(msc.getResponseClassName());
        this.setDisplaySeq(msc.getDisplaySeq());
        this.setInsertDate(msc.getInsertDate());
        this.setUpdateDate(msc.getUpdateDate());
        this.setDeleteDate(msc.getDeleteDate());
    }

    /**
     * ResultSetWrapperのデータを読み込む。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
       
        this.setResponseClassId(rs.getInt("response_class_id"));
        this.setetResponseClassName(rs.getString("response_class_name"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        this.setInsertDate(rs.getTimestamp("insert_date"));
        this.setUpdateDate(rs.getTimestamp("update_date"));
        this.setDeleteDate(rs.getTimestamp("delete_date"));
    }

    /**
     * スタッフ区分マスタデータをArrayListに読み込む。
     */
    public ArrayList<MstResponseClass> load(ConnectionWrapper con) throws SQLException {
        ArrayList<MstResponseClass> list = new ArrayList<MstResponseClass>();

        ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());

        while (rs.next()) {
            MstResponseClass msc = new MstResponseClass();
            msc.setData(rs);
            list.add(msc);
        }

        rs.close();

        return list;
    }
    
     /**
     * スタッフ区分マスタデータをArrayListに読み込む。
     */
    public ArrayList<MstResponseClass> loadAllResponseClass(ConnectionWrapper con) throws SQLException {
        ArrayList<MstResponseClass> list = new ArrayList<MstResponseClass>();

        ResultSetWrapper rs = con.executeQuery(getAllResponseSQL());

        while (rs.next()) {
            MstResponseClass msc = new MstResponseClass();
            msc.setData(rs);
            list.add(msc);
        }

        rs.close();

        return list;
    }
    
    /**
     * スタッフ区分マスタデータをArrayListに読み込む。
     */
    public ArrayList<MstResponseClass> loadByClassID(ConnectionWrapper con) throws SQLException {
        ArrayList<MstResponseClass> list = new ArrayList<MstResponseClass>();

        ResultSetWrapper rs = con.executeQuery(getSelectSQL());

        while (rs.next()) {
            MstResponseClass msc = new MstResponseClass();
            msc.setData(rs);
            list.add(msc);
        }

        rs.close();

        return list;
    }

    /**
     * スタッフ区分マスタにデータを登録する。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean regist(ConnectionWrapper con,
            Integer lastSeq) throws SQLException {
        if (isExists(con)) {
            if (lastSeq != this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0) {
                    return false;
                }

                if (0 <= this.getDisplaySeq()) {
                    if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }

            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }
        } else {
            if (0 <= this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }

            if (con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * スタッフ区分マスタからデータを削除する。（論理削除）
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getDeleteSQL();
        } else {
            return false;
        }

        if (con.executeUpdate(sql) != 1) {
            return false;
        }

        if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0) {
            return false;
        }

        return true;
    }

    /**
     * スタッフ区分マスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getResponseClassId() == null || this.getResponseClassId() < 1) {
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
     *
     * @return Select文
     */
    private String getSelectAllSQL() {
        String sqlWhere = "";
        sqlWhere += "SELECT r.response_class_id \n ";
        sqlWhere += "FROM  mst_response r  \n ";
        sqlWhere += "INNER JOIN mst_use_response ur\n ";
        sqlWhere += "ON r.response_id = ur.response_id\n ";
        sqlWhere += "AND ur.shop_id =  " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()) + "\n";
        return "select *\n"
                + "from mst_response_class\n"
                + "where response_class_id IN ( "
                + sqlWhere + ")\n"
                + "AND delete_date is NULL\n"
                + "order by display_seq\n";
    }
    
     /**
     * Select文を取得する。
     *
     * @return Select文
     */
        private String getAllResponseSQL() {
               return "select *\n"
                       + "from mst_response_class\n"
                       + "where delete_date is null\n"
                       + "order by display_seq\n";
           }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        return "select *\n"
                + "from mst_response_class\n"
                + "where delete_date is null\n"
                + "and	response_class_id = " + SQLUtil.convertForSQL(this.getResponseClassId()) + "\n";
    }

    /**
     * 表示順をずらすＳＱＬ文を取得する
     *
     * @param seq 元の表示順
     * @param isIncrement true - 加算、false - 減算
     * @return 表示順をずらすＳＱＬ文
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        return "update mst_response_class\n"
                + "set display_seq = display_seq "
                + (isIncrement ? "+" : "-") + " 1\n"
                + "where delete_date is null\n"
                + "and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        return "";
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        return "";
    }

    /**
     * 削除用Update文を取得する。
     *
     * @return 削除用Update文
     */
    private String getDeleteSQL() {
        return "update mst_response_class\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	staff_class_id = " + SQLUtil.convertForSQL(this.getResponseClassId()) + "\n";
    }
}

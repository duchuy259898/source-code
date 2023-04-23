/*
 * SimpleMaster.java
 *
 * Created on 2006/06/05, 13:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo;

import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * シンプルマスタ登録処理 （ＩＤ、名称、表示順のみ）
 *
 * @author katagiri
 */
public class SimpleMaster extends ArrayList<MstData> {

    /**
     * マスタの名称
     */
    protected String masterName = "";
    /**
     * テーブル名
     */
    protected String tableName = "";
    /**
     * ＩＤの列名
     */
    protected String idColName = "";
    /**
     * 名称の列名
     */
    protected String nameColName = "";
    /**
     * 名称の最大文字数
     */
    protected int nameLength = 0;

    /**
     * コンストラクタ
     *
     * @param masterName マスタの名称
     * @param tableName テーブル名
     * @param idColName ＩＤの列名
     * @param nameColName 名称の列名
     */
    public SimpleMaster(String masterName,
            String tableName,
            String idColName,
            String nameColName,
            int nameLength) {
        this.setMasterName(masterName);
        this.setTableName(tableName);
        this.setIDColName(idColName);
        this.setNameColName(nameColName);
        this.setNameLength(nameLength);
    }

    /**
     * マスタの名称
     *
     * @return マスタの名称
     */
    public String getMasterName() {
        return masterName;
    }

    /**
     * マスタの名称
     *
     * @param masterName マスタの名称
     */
    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    /**
     * テーブル名
     *
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * テーブル名
     *
     * @param tableName テーブル名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * ＩＤの列名
     *
     * @return ＩＤの列名
     */
    public String getIDColName() {
        return idColName;
    }

    /**
     * ＩＤの列名
     *
     * @param idColName ＩＤの列名
     */
    public void setIDColName(String idColName) {
        this.idColName = idColName;
    }

    /**
     * 名称の列名
     *
     * @return 名称の列名
     */
    public String getNameColName() {
        return nameColName;
    }

    /**
     * 名称の列名
     *
     * @param nameColName 名称の列名
     */
    public void setNameColName(String nameColName) {
        this.nameColName = nameColName;
    }

    /**
     * 名称の最大文字数
     *
     * @return 名称の最大文字数
     */
    public int getNameLength() {
        return nameLength;
    }

    /**
     * 名称の最大文字数
     *
     * @param nameLength 名称の最大文字数
     */
    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }

    /**
     * データをデータベースから読み込む。
     */
    public void loadData() {
        this.clear();

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getSelectAllSQL());

            while (rs.next()) {
                MstData temp = new MstData(
                        rs.getInt("data_id"),
                        rs.getString("data_name"),
                        rs.getInt("display_seq"));
                this.add(temp);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    public void loadDataForResponse() {
        this.clear();

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getSelectAllResponseSQL());

            while (rs.next()) {
                MstData temp = new MstData(
                        rs.getInt("data_id"),
                        rs.getString("data_name"),
                        rs.getInt("display_seq"),
                        rs.getString("response_no"),
                        rs.getInt("response_class_id"),
                        rs.getString("response_class_name"));
                this.add(temp);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private String getSelectAllResponseSQL() {
        String sql = "select response_id as data_id,\n"
                + "response_name  as data_name,\n"
                + "a.response_no,\n"
                + "(case when b.delete_date is null\n"
                + " then a.response_class_id\n"
                + " else 0 end) as response_class_id,\n"
                + " (case when b.delete_date is null\n"
                + " then b.response_class_name\n"
                + " else null end) as response_class_name,\n"
                + "a.display_seq\n"
                + "from mst_response as a\n"
                + "left join mst_response_class as b\n"
                + "on a.response_class_id = b.response_class_id\n"
                + "where a.delete_date is null\n"
                + "order by a.display_seq\n";
        return sql;
    }

    public boolean registForResponse(MstData data, Integer dataIndex) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (this.registDataForResponse(con, data, dataIndex)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    private boolean registDataForResponse(ConnectionWrapper con,
            MstData data, Integer dataIndex) throws SQLException {
        //存在しないデータの場合
        if (dataIndex < 0) {
            //表示順が入力されている場合
            if (0 <= data.getDisplaySeq()) {
                //入力された表示順以降のデータの表示順を１加算する
                if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }
            //データをInsertする
            if (con.executeUpdate(this.getInsertDataSQLForResponse(data)) != 1) {
                return false;
            }
        } //存在するデータの場合
        else {
            //表示順が変更されている場合
            if (this.get(dataIndex).getDisplaySeq() != data.getDisplaySeq()) {
                //元の表示順以降のデータの表示順を１減算する
                if (con.executeUpdate(this.getSlideSQL(this.get(dataIndex).getDisplaySeq(), false)) < 0) {
                    return false;
                }
                //表示順が入力されている場合
                if (0 <= data.getDisplaySeq()) {
                    //入力された表示順以降のデータの表示順を１加算する
                    if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }
            //データをUpdateする
            if (con.executeUpdate(this.getUpdateDataSQLForResponse(data)) != 1) {
                return false;
            }
        }

        return true;
    }

    private String getInsertDataSQLForResponse(MstData data) {
        return "insert into " + this.getTableName() + "\n"
                + "(" + this.idColName + ", " + this.nameColName + ", \n" + "response_no, response_class_id, display_seq, "
                + "insert_date, update_date, delete_date)"
                + "select\n"
                + "coalesce(max(" + this.getIDColName() + "), 0) + 1,\n"
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + SQLUtil.convertForSQL(data.getResponse_no()) + ",\n"
                + SQLUtil.convertForSQL(data.getResponse_class_id()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) + 1 end,\n"
                + "current_timestamp, current_timestamp, null\n"
                + "from " + this.getTableName() + "\n";
    }

    private String getUpdateDataSQLForResponse(MstData data) {
        String sql = "update " + this.getTableName() + "\n"
                + "set\n"
                + this.getNameColName() + " = "
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + "response_no = " + SQLUtil.convertForSQL(data.getResponse_no()) + ",\n"
                + "response_class_id = " + SQLUtil.convertForSQL(data.getResponse_class_id()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where " + this.getIDColName() + " = "
                + SQLUtil.convertForSQL(data.getID()) + "\n";

        return sql;
    }

    /**
     * 全てのデータを取得するＳＱＬ文を取得する。
     *
     * @return 全てのデータを取得するＳＱＬ文
     */
    private String getSelectAllSQL() {
        return "select " + this.getIDColName() + " as data_id,\n"
                + this.getNameColName() + " as data_name,\n"
                + "display_seq\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "order by display_seq\n";
    }

    /**
     * データを登録する。
     *
     * @param data 対象となるデータ
     * @param dataIndex データのインデックス
     * @return true - 成功
     */
    public boolean regist(MstData data, Integer dataIndex) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (this.registData(con, data, dataIndex)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * データを登録する。
     *
     * @param con ConnectionWrapper
     * @param data 対象となるデータ
     * @param dataIndex データのインデックス
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    private boolean registData(ConnectionWrapper con,
            MstData data, Integer dataIndex) throws SQLException {
        //存在しないデータの場合
        if (dataIndex < 0) {
            //表示順が入力されている場合
            if (0 <= data.getDisplaySeq()) {
                //入力された表示順以降のデータの表示順を１加算する
                if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }
            //データをInsertする
           
            
            if (con.executeUpdate(this.getInsertDataSQL(data)) != 1) {
                return false;
            }
        } //存在するデータの場合
        else {
            //表示順が変更されている場合
            if (this.get(dataIndex).getDisplaySeq() != data.getDisplaySeq()) {
                //元の表示順以降のデータの表示順を１減算する
                if (con.executeUpdate(this.getSlideSQL(this.get(dataIndex).getDisplaySeq(), false)) < 0) {
                    return false;
                }
                //表示順が入力されている場合
                if (0 <= data.getDisplaySeq()) {
                    //入力された表示順以降のデータの表示順を１加算する
                    if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }
            //データをUpdateする
            if (con.executeUpdate(this.getUpdateDataSQL(data)) != 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * データを削除する。
     *
     * @param data 対象となるデータ
     * @return true - 成功
     */
    public boolean delete(MstData data) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (this.deleteData(con, data)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * データを削除する。
     *
     * @param con ConnectionWrapper
     * @param data 対象となるデータ
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    private boolean deleteData(ConnectionWrapper con,
            MstData data) throws SQLException {
        if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), false)) < 0) {
            return false;
        }

        if (con.executeUpdate(this.getDeleteDataSQL(data)) != 1) {
            return false;
        }

        return true;
    }

    /**
     * 表示順をずらすＳＱＬ文を取得する。
     *
     * @param seq 表示順
     * @param isIncrement true - 増やす
     * @return 表示順をずらすＳＱＬ文
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        return "update " + this.getTableName() + "\n"
                + "set display_seq = display_seq "
                + (isIncrement ? "+" : "-") + " 1\n"
                + "where delete_date is null\n"
                + "and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
    }

    /**
     * データをInsertするＳＱＬ文を取得する。
     *
     * @param data 対象となるデータ
     * @return データをInsertするＳＱＬ文
     */
    private String getInsertDataSQL(MstData data) {
        return "insert into " + this.getTableName() + "\n"
                + "(" + this.idColName + ", " + this.nameColName + ", \n" + "display_seq, "
                + "insert_date, update_date, delete_date)"
                + "select\n"
                + "coalesce(max(" + this.getIDColName() + "), 0) + 1,\n"
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) + 1 end,\n"
                + "current_timestamp, current_timestamp, null\n"
                + "from " + this.getTableName() + "\n";
    }

    /**
     * データをUpdateするＳＱＬ文を取得する。
     *
     * @param data 対象となるデータ
     * @return データをUpdateするＳＱＬ文
     */
    private String getUpdateDataSQL(MstData data) {
        return "update " + this.getTableName() + "\n"
                + "set\n"
                + this.getNameColName() + " = "
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where " + this.getIDColName() + " = "
                + SQLUtil.convertForSQL(data.getID()) + "\n";
    }

    /**
     * データを削除（論理削除）するＳＱＬ文を取得する。
     *
     * @param data 対象となるデータ
     * @return データを削除（論理削除）するＳＱＬ文
     */
    //IVS_LTThuc start add 20140805 MASHU_業態登録
    private String getDeleteDataSQL(MstData data) {
        return "update " + this.getTableName() + "\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where " + this.getIDColName() + " = " + SQLUtil.convertForSQL(data.getID()) + "\n";
    }
    public int getDeleteDataSQL2(String table,MstData data){
        String sql = "select count(shop_category_id) as number from "+table 
                +" where shop_category_id = " +SQLUtil.convertForSQL(data.getID())
                +" and delete_date is  null";
         ConnectionWrapper con = SystemInfo.getConnection();
        int count=0;
        try {
            ResultSetWrapper rs = con.executeQuery(sql);
           if(rs.next()){
           count = rs.getInt("number");
           }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    return count;
        
    }
 //IVS_LTThuc end add 20140805 MASHU_業態登録
}

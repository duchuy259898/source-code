/*
 * DataInventory.java
 *
 * Created on 2008/09/19, 19:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.commodity;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mizukawa
 */
public class DataInventory {

    /** 棚卸ID */
    private Integer inventoryId;
    /** 棚卸区分 */
    private Integer inventory_division;
    /** ショップID */
    private Integer shopId;
    /** 棚卸終了日 */
    private Date inventoryDate;
    /** 確定フラグ */
    private Integer fixed;
    /** 最終更新日 */
    private Date updateDate;
    /** 棚卸詳細 */
    private List<DataInventoryDetail> detail = new ArrayList<DataInventoryDetail>();

    /** Creates a new instance of DataInventory */
    public DataInventory() {
    }

    /**
     * 棚卸IDを返す。
     * @return 棚卸ID
     */
    public Integer getInventoryId() {
        return this.inventoryId;
    }

    /**
     * 棚卸IDをセットする。
     * @param v 棚卸ID
     */
    public void setInventoryId(Integer v) {
        this.inventoryId = v;
    }

    /**
     * 棚卸IDを返す。
     * @return 棚卸ID
     */
    public Integer getInventoryDivision() {
        return this.inventory_division;
    }

    /**
     * 棚卸IDをセットする。
     * @param v 棚卸ID
     */
    public void setInventoryDivision(Integer v) {
        this.inventory_division = v;
    }

    /**
     * ショップIDを返す。
     * @return ショップID
     */
    public Integer getShopId() {
        return this.shopId;
    }

    /**
     * ショップIDをセットする。
     * @param v ショップID
     */
    public void setShopId(Integer v) {
        this.shopId = v;
    }

    /**
     * 棚卸終了日を返す。
     * @return 棚卸終了日
     */
    public Date getInventoryDate() {
        return this.inventoryDate;
    }

    /**
     * 棚卸終了日をセットする。
     * @param v 棚卸終了日
     */
    public void setInventoryDate(Date v) {
        this.inventoryDate = v;
    }

    /**
     * 確定フラグを返す。
     * @return 確定フラグ
     */
    public Integer getFixed() {
        return this.fixed;
    }

    /**
     * 確定フラグをセットする。
     * @param v 確定フラグ
     */
    public void setFixed(Integer v) {
        this.fixed = v;
    }

    /**
     * 最終更新日
     * @param updateDate 最終更新日
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 最終更新日
     * @return 最終更新日
     */
    public Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * 存在確認用SQL文を取得する。
     * @return SQL文
     */
    private String getExistSQL(boolean lock) {
        StringBuilder buf = new StringBuilder();
        if (this.getInventoryId() == null) {
            buf.append("select\n").append("  inventory_id\n").append("from\n").append("  data_inventory\n").append("where\n").append("  shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append("\n").append("and\n").append("  inventory_date = ").append(SQLUtil.convertForSQLDateOnly(this.getInventoryDate())).append("\n");
        } else {
            buf.append("SELECT\n").append("  inventory_id\n").append("FROM\n").append("  data_inventory\n").append("WHERE\n").append("  inventory_id = ").append(SQLUtil.convertForSQL(this.getInventoryId())).append("\n").append("AND\n").append("  inventory_division = ").append(SQLUtil.convertForSQL(this.getInventoryDivision())).append("\n");
        }

        if (lock) {
            buf.append("for update\n");
        }

        return new String(buf);
    }

    /**
     * 新規棚卸ID採番用SQLを取得する。
     * @return 検索SQL文
     */
    private String getNewInventryIDSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT ");
        sql.append("     COALESCE(MAX(inventory_id),0) + 1 AS new_inventory_id ");
        sql.append(" FROM  data_inventory ");

        return sql.toString();
    }

    /**
     * Insert文を取得する。
     * @return Insert文
     */
    private String getInsertSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" INSERT INTO data_inventory ( ");
        sql.append("     inventory_id ");
        sql.append("    ,inventory_division ");
        sql.append("    ,shop_id ");
        sql.append("    ,inventory_date ");
        sql.append("    ,fixed ");
        sql.append("    ,insert_date ");
        sql.append("    ,update_date ");
        sql.append("    ,delete_date ");
        sql.append(" ) VALUES ( ");
        sql.append("  " + SQLUtil.convertForSQL(this.getInventoryId()));
        sql.append(" ," + SQLUtil.convertForSQL(this.getInventoryDivision()));
        sql.append(" ," + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" ," + SQLUtil.convertForSQLDateOnly(this.getInventoryDate()));
        sql.append(" ," + SQLUtil.convertForSQL(this.getFixed()));
        sql.append(" ,current_timestamp ");
        sql.append(" ,current_timestamp ");
        sql.append(" ,NULL ");
        sql.append(" ) ");

        return sql.toString();
    }

    /**
     * Update文を取得する。
     * @return Update文
     */
    private String getUpdateSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" UPDATE data_inventory ");
        sql.append(" SET ");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("    ,inventory_date = " + SQLUtil.convertForSQLDateOnly(this.getInventoryDate()));
        sql.append("    ,fixed = " + SQLUtil.convertForSQL(this.getFixed()));
        sql.append("    ,update_date = current_timestamp ");
        sql.append(" WHERE ");
        sql.append("     inventory_id = " + SQLUtil.convertForSQL(this.getInventoryId()));
        sql.append(" AND ");
        sql.append("     inventory_division = " + SQLUtil.convertForSQL(this.getInventoryDivision()));

        return sql.toString();
    }

    /**
     * このオブジェクトにデータベースから取得した値をセットする。
     * @param rs 問い合わせ結果
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        setInventoryId(rs.getInt("inventory_id"));
        setInventoryDivision(rs.getInt("inventory_division"));
        setShopId(rs.getInt("shop_id"));
        setInventoryDate(rs.getTimestamp("inventory_date"));
        setFixed(rs.getInt("fixed"));
        setUpdateDate(rs.getTimestamp("update_date"));
    }

    /**
     * 新しい棚卸IDをセットする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setNewInventoryID(ConnectionWrapper con) throws SQLException {
        Integer invId = matchInventory(con);
        if (invId != null) {
            this.setInventoryId(invId);
        } else {
            ResultSetWrapper rs = con.executeQuery(this.getNewInventryIDSQL());

            if (rs.next()) {
                this.setInventoryId(rs.getInt("new_inventory_id"));
            }

            rs.close();
        }
    }

    /**
     * 棚卸にデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getExistSQL(true));

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 棚卸に店舗、棚卸終了日にマッチするデータが存在するかチェックし、存在したらその棚卸IDを返す
     * @param con データベースコネクション
     * @return 棚卸ID/null
     */
    public Integer matchInventory(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getExistSQL(true));

        if (rs.next()) {
            return rs.getInt("inventory_id");
        } else {
            return null;
        }
    }

    /**
     * 追加
     */
    public boolean insert(ConnectionWrapper con) throws SQLException {
        if (con.executeUpdate(this.getInsertSQL()) < 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 更新
     */
    public boolean update(ConnectionWrapper con) throws SQLException {
        if (con.executeUpdate(this.getUpdateSQL()) < 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * このレコードが誰かに更新されたかどうかを返す
     */
    public boolean isUpdated(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getCheckUpdateSQL());
        try {
            if (rs.next()) {
                Date dt = rs.getTimestamp("update_date");
                return !dt.equals(getUpdateDate());
            } else {
                if (getUpdateDate() == null) {
                    return false;
                }

                return true;
            }
        } finally {
            rs.close();
        }
    }

    private String getCheckUpdateSQL() {
        StringBuilder buf = new StringBuilder();
        if (getInventoryId() != null) {
            buf.append("select\n").append("  update_date\n").append("from\n").append("  data_inventory\n").append("where\n").append("  inventory_id = ").append(SQLUtil.convertForSQL(getInventoryId())).append("\n").append("and\n").append("  inventory_division = ").append(SQLUtil.convertForSQL(getInventoryDivision())).append("\n");
        } else {
            buf.append("select\n").append("  update_date\n").append("from\n").append("  data_inventory\n").append("where\n").append("  shop_id = ").append(SQLUtil.convertForSQL(getShopId())).append("\n").append("and\n").append("  inventory_date = ").append(SQLUtil.convertForSQLDateOnly(getInventoryDate())).append("\n").append("and\n").append("  delete_date is null\n");
        }

        return new String(buf);
    }

    /**
     * 最新の棚卸データをデータベースから取得しこのオブジェクトに値をセットする。
     * @param con データベースコネクション
     * @param shopId ショップID
     */
    public void loadLatest(ConnectionWrapper con, Integer shopId, Date basedate) throws SQLException {
        // 最新の棚卸データを取得する
        ResultSetWrapper rs = con.executeQuery(getLoadLatestSQL(shopId, basedate));
        try {
            if (rs.next()) {
                this.setData(rs);
            }
        } finally {
            rs.close();
        }

        // 最新の棚卸データに紐付く詳細データを取得する
        rs = con.executeQuery(getDetailSQL(this.getInventoryId(), this.getInventoryDivision()));
        try {
            detail.clear();
            while (rs.next()) {
                DataInventoryDetail d = new DataInventoryDetail();
                d.setData(rs);
                detail.add(d);
            }
        } finally {
            rs.close();
        }
    }

    /**
     * 最新の棚卸データをデータベースから取得するためのSQLを返す。
     * @param shopId ショップID
     * @return 最新の棚卸データをデータベースから取得するためのSQL
     */
    public String getLoadLatestSQL(Integer shopId, Date basedate) {
        StringBuilder buf = new StringBuilder();
        buf.append("select d.* from data_inventory d\n").append("where\n").append("shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n").append("and d.inventory_date = (\n").append("select max(inventory_date) from (\n").append("select max(inventory_date) as inventory_date from data_inventory where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append(" and inventory_date <= ").append(SQLUtil.convertForSQLDateOnly(basedate)).append(" and delete_date is null\n").append("union\n").append("select min(inventory_date) as inventory_date from data_inventory where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append(" and inventory_date >= ").append(SQLUtil.convertForSQLDateOnly(basedate)).append(" and delete_date is null\n").append(") a\n").append(")");
        return new String(buf);
    }

    /**
     * 棚卸情報の詳細を取得するためのSQLを返す。
     * @param inventoryId 棚卸ID
     * @return 棚卸情報の詳細を取得するためのSQL
     */
    public String getDetailSQL(Integer inventoryId, Integer inventory_division) {
        StringBuilder buf = new StringBuilder();
        buf.append("select d.*, mi.* from data_inventory_detail d, mst_item mi where d.item_id = mi.item_id and d.delete_date is null and inventory_id = ").append(SQLUtil.convertForSQL(inventoryId));
        buf.append(" and inventory_division = ").append(SQLUtil.convertForSQL(inventory_division));
        return new String(buf);
    }

    /**
     *
     */
    public DataInventoryDetail getDetail(Integer itemId, Integer inventoryDivision) {
        for (DataInventoryDetail d : detail) {
            if (itemId.equals(d.getItemID())) {
                if (inventoryDivision.equals(d.getInventoryDivision())) {
                    return d;
                }
            }
        }

        return null;
    }

    /*
     * 棚卸明細データの追加
     */
    public void addDetail(DataInventoryDetail dd) {
        detail.add(dd);
    }
}

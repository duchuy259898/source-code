/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 * 店舗業態関連マスタ
 *
 * @author IVS_TTMLoan
 * @since 2014/07/07
 */
public class MstShopRelation {

    /**
     * 店舗ID
     */
    private Integer shopId;
    /**
     * 業態ID
     */
    private Integer shopCategoryId;
    /**
     * 業態名
     */
    private String shopClassName;

    /**
     * 表示順
     */
    private Integer displaySeq;
    
    /**
     * 業態IDを取得する
     *
     * @return 業態ID
     */
    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    /**
     * 業態IDを設定する
     *
     * @param shopCategoryId 業態ID
     */
    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    /**
     * 店舗IDを取得する
     *
     * @return 店舗ID
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * 店舗IDを設定する
     *
     * @param shopId 店舗ID
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 業態名を取得する
     *
     * @return 業態名
     */
    public String getShopClassName() {
        return shopClassName;
    }

    /**
     * 業態名を設定する
     *
     * @param shopClassName 業態名
     */
    public void setShopClassName(String shopClassName) {
        this.shopClassName = shopClassName;
    }

    /**
     * 表示順を取得する
     * @return 表示順
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * 業態名を設定する
     * @param displaySeq 表示順
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    
    
    /**
     * ResultSetWrapperのデータを読み込む。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setShopCategoryId(rs.getInt("shop_category_id"));
        this.setShopClassName(rs.getString("shop_class_name"));
        this.setShopId(rs.getInt("shop_id"));
    }
    
    /**
     * ResultSetWrapperのデータを読み込む。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/25
     */
    public void setDataWithSeq(ResultSetWrapper rs) throws SQLException {
        this.setShopCategoryId(rs.getInt("shop_category_id"));
        this.setShopClassName(rs.getString("shop_class_name"));
        this.setShopId(rs.getInt("shop_id"));
        this.setDisplaySeq(rs.getInt("display_seq"));
    }
    
    /**
     * 店舗業態関連マスタにデータを登録する。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true 成功
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (con != null) {
            return (con.executeUpdate(this.getInsertSQL()) == 1);
        }
        return false;
    }

    /**
     * 店舗業態関連マスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (con != null) {

            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

            if (rs.next()) {
                return true;
            }
        }
        return false;

    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getSelectSQL() {
        return "SELECT * "
                + " FROM mst_shop_relation"
                + " WHERE shop_id = " + SQLUtil.convertForSQL(this.shopId)
                + " AND shop_category_id = " + SQLUtil.convertForSQL(this.shopCategoryId)
                + " AND delete_date IS NULL";
    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getInsertSQL() {
        return "insert into mst_shop_relation"
                + " (shop_id, shop_category_id, insert_date, update_date) values("
                + SQLUtil.convertForSQL(this.getShopId()) + ","
                + SQLUtil.convertForSQL(this.getShopCategoryId()) + ","
                + "current_timestamp, current_timestamp)";
    }
	
	//GB_Mashu nahoang Start add 2014/10/06_目標設定
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("");

        return sb.toString() + this.getShopClassName();
    }
    // GB_Mashu nahoang end add 2014/10/06_目標設定
}

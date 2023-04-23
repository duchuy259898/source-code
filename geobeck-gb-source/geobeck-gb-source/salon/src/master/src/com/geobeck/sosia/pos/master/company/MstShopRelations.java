/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 店舗業態関連マスタ
 *
 * @author IVS_TTMLoan
 * @since 2014/07/07
 */
public class MstShopRelations extends ArrayList<MstShopRelation> {

    /**
     * 店舗業態関連マスタから、設定されている店舗IDのデータを読み込む。
     *
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public void loadByShop(ConnectionWrapper con, Integer shopId) throws SQLException {
        if (con != null) {
            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(shopId));
            while (rs.next()) {
                MstShopRelation element = new MstShopRelation();
                element.setData(rs);
                this.add(element);
            }
        }
    }
    
    /**
     * 店舗業態関連マスタから、設定されている店舗IDのデータを読み込む。
     *
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public void loadAllByShop(ConnectionWrapper con, Integer shopId) throws SQLException {
        if (con != null) {
            ResultSetWrapper rs = con.executeQuery(this.getSelectAllSQL(shopId));
            while (rs.next()) {
                MstShopRelation element = new MstShopRelation();
                element.setDataWithSeq(rs);
                this.add(element);
            }
        }
    }

    /**
     * 店舗マスタにデータを登録する。
     *
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public void deleteByShop(ConnectionWrapper con, Integer shopId) throws SQLException {
        if (con != null) {
            con.executeUpdate(this.getDeleteByShopSQL(shopId));
        }

    }

    /**
     * Select文を取得する。
     *
     * @param shopId Integer
     * @return Select文
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getSelectSQL(Integer shopId) {
        return " SELECT mc.shop_category_id, mc.shop_class_name, mr.shop_id"
                + " FROM mst_shop_category mc"
                + " inner JOIN mst_shop_relation mr"
                + " ON mr.shop_category_id = mc.shop_category_id"
                + " AND mr.shop_id = " + shopId
                + " AND mr.delete_date IS NULL"
                + " WHERE mc.delete_date IS NULL"
                + " ORDER BY mc.display_seq";
    }
    
    /**
     * SelectAll文を取得する。
     *
     * @param shopId Integer
     * @return Select文
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getSelectAllSQL(Integer shopId) {
        return " SELECT mc.shop_category_id, mc.shop_class_name, mr.shop_id, mc.display_seq"
                + " FROM mst_shop_category mc"
                + " LEFT JOIN mst_shop_relation mr"
                + " ON mr.shop_category_id = mc.shop_category_id"
                + " AND mr.shop_id = " + shopId
                + " AND mr.delete_date IS NULL"
                + " WHERE mc.delete_date IS NULL"
                + " ORDER BY mc.display_seq";
    }

    /**
     * Delete文を取得する。
     *
     * @param shopId Integer
     * @return Delete文
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getDeleteByShopSQL(Integer shopId) {
        return " DELETE FROM mst_shop_relation"
                + " WHERE shop_id = " + shopId;
    }
    
    /**
     * Sort list category
     * @author IVS_TTMLoan
     * @since 2014/07/25
     */
    public void sort(){
         Collections.sort(this, new CategoryComparator());
    }

    /**
     * Compare category
     * @author IVS_TTMLoan
     * @since 2014/07/25
     */
    private class CategoryComparator implements Comparator<MstShopRelation> {
            public int compare(MstShopRelation p0, MstShopRelation p1)
            {
                    return	p0.getDisplaySeq().compareTo(p1.getDisplaySeq());
            }
    }
    
    /**
     * Check category exist in staff and bed table
     *
     * @param con ConnectionWrapper
     * @param category String
     * @param shopId Integer
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/08/05
     */
    public boolean checkExistInStaffOrBed(ConnectionWrapper con, String category,Integer shopId) throws SQLException {
        if (con != null) {
            ResultSetWrapper rs = con.executeQuery(this.getSelectStaffAndBedSQL(category,shopId));
            while (rs.next()) {
                if(rs.getInt("total") != 0){
                 return true;   
                }
            }
        }
        return false;
    }
    
    /**
     * Select SQL to get category in staff and bed table
     *
     * @param category 業態
     * @return Select文
     * @author IVS_TTMLoan
     * @since 2014/08/05
     */
    private String getSelectStaffAndBedSQL(String category, Integer shopId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(num)  as total");
        sql.append(" FROM");
        sql.append("   (SELECT COUNT(*) AS num");
        sql.append("    FROM mst_staff_relation msr");
        sql.append("    INNER JOIN mst_staff ms");
        sql.append("    ON ms.staff_id = msr.staff_id");
        sql.append("    WHERE msr.shop_category_id IN ("+ category +")");
        sql.append("      AND msr.delete_date IS NULL");
        sql.append("      AND ms.shop_id = " + shopId);
        sql.append("      AND ms.delete_date IS NULL");
        sql.append("   UNION ALL ");
        sql.append("   SELECT COUNT(*) AS num");
        sql.append("    FROM mst_bed_relation mbr");
        sql.append("    INNER JOIN mst_bed mb");
        sql.append("       ON mb.bed_id = mbr.bed_id");
        sql.append("    WHERE mbr.shop_category_id IN ("+ category +")");
        sql.append("      AND mbr.delete_date IS NULL");
        sql.append("      AND mb.shop_id = " + shopId);
        sql.append("      AND mb.delete_date IS NULL");
        sql.append("      ) AS T");
        
        return sql.toString();
    }
    
}

/*
 * MstTechnicClasses.java
 *
 * Created on 2006/05/24, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.master.MstData;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * 技術分類マスタデータのArrayList
 *
 * @author katagiri
 */
public class MstTechnicClasses extends ArrayList<MstTechnicClass> {
    
    /**
     * コンストラクタ
     */
    public MstTechnicClasses() {
    }

    /**
     * 技術分類マスタデータをArrayListに読み込む。
     */
    public void getAll(ConnectionWrapper con) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(
                MstTechnicClasses.getSelectSQL());

        while (rs.next()) {
            MstTechnicClass mtc = new MstTechnicClass();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
     /**
     * LUC start add メニュー選択 LOAD TECHNIC_CLASS SQL 20121030
     */
    public void load(ConnectionWrapper con) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(
                MstTechnicClasses.getSelectSQL());

        while (rs.next()) {
            MstTechnicClass mtc = new MstTechnicClass();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
    /**
     * LUC start add メニュー選択 LOAD TECHNIC_CLASS SQL 20121030
     */
    public void loadAll(ConnectionWrapper con, Integer productDivision) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(
                MstTechnicClasses.getAllSelectSQL(productDivision));

        while (rs.next()) {
            MstTechnicClass mtc = new MstTechnicClass();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
    //Thuyet End add メニュー選択 LOAD SQL 20130313
    
    /**
     * 技術分類マスタデータを全て取得するＳＱＬ文を取得する
     *
     * @return 技術分類マスタデータを全て取得するＳＱＬ文
     */
    public static String getAllSelectSQL(Integer productDivision) {
        StringBuilder sql = new StringBuilder(1000);
        switch(productDivision)
        {
            case 1:
                sql.append(" select");
                sql.append("      a.*");
                sql.append("     ,b.technic_integration_name");
                sql.append("     ,b.display_seq");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                sql.append("      ,c.shop_class_name");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                sql.append(" from");
                sql.append("     mst_technic_class a");
                sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
                sql.append("         using (technic_integration_id)");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                sql.append(" left join mst_shop_category c ");
                sql.append(" on a.shop_category_id =  c.shop_category_id ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                sql.append(" where");
                sql.append("     a.delete_date is null");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                sql.append(" and c.delete_date is null ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                sql.append(" order by");
                sql.append("      a.display_seq");
                // IVS_LeTheHieu Start delete 20140703 GB_MASHU_技術分類登録
                //sql.append("     ,a.technic_class_name");
                // IVS_LeTheHieu End delete 20140703 GB_MASHU_技術分類登録

                break;
            //商品
            case 2:
                sql.append(" select");
                sql.append( "     a.item_class_id as technic_class_id,");
                sql.append("      a.item_class_name as technic_class_name,");
                sql.append("      a.item_class_contracted_name as technic_class_contracted_name,");
                sql.append("      a.item_integration_id as technic_integration_id,");
                sql.append("      a.display_seq,");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                sql.append("      a.shop_category_id,");
                sql.append("      c.shop_class_name,");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                sql.append("      0 as prepaid,");
                sql.append("      b.item_integration_name as technic_integration_name,");
                sql.append("     b.display_seq");
                sql.append(" from");
                sql.append("     mst_item_class a");
                sql.append("         left join (select * from mst_item_integration where delete_date is null) b");
                sql.append("         using (item_integration_id)");
                 // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                 sql.append(" left join mst_shop_category c ");
                sql.append(" on a.shop_category_id =  c.shop_category_id ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                sql.append(" where");
                sql.append("     a.delete_date is null");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                sql.append(" and c.delete_date is null ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                sql.append(" order by");
                sql.append("      a.display_seq");
                // IVS_LeTheHieu Start delete 20140703 GB_MASHU_技術分類登録
                //sql.append("     ,a.technic_class_name");
                // IVS_LeTheHieu End delete 20140703 GB_MASHU_技術分類登録
                break;           

        }
        return sql.toString();
    }
    /**
     * 技術分類マスタデータを全て取得するＳＱＬ文を取得する
     *
     * @return 技術分類マスタデータを全て取得するＳＱＬ文
     */
    public static String getSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      a.*");
        sql.append("     ,b.technic_integration_name");
        sql.append("     ,b.display_seq");
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
        sql.append("     ,c.shop_class_name ");
        sql.append("     ,c.shop_category_id ");
        // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
        sql.append(" from");
        sql.append("     mst_technic_class a");
        sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
        sql.append("         using (technic_integration_id)");
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
        sql.append(" left join mst_shop_category c ");
        sql.append(" on a.shop_category_id =  c.shop_category_id ");
        // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
        sql.append(" where");
        sql.append("     a.delete_date is null");
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
        sql.append(" and c.delete_date is null ");
        // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
        sql.append(" order by");
        sql.append("      a.display_seq");
        // IVS_LeTheHieu Start delete 20140703 GB_MASHU_技術分類登録
        //sql.append("     ,a.technic_class_name");
        // IVS_LeTheHieu End delete 20140703 GB_MASHU_技術分類登録

       return sql.toString();
    }
}

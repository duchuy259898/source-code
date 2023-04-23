/*
 * MstBeds.java
 *
 * Created on 2006/05/24, 21:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * ベッドマスタデータのArrayList
 * @author katagiri
 */
public class MstBeds extends ArrayList<MstBed>
{
    private MstShop shop = new MstShop();

    /**
     * コンストラクタ
     */
    public MstBeds()
    {
    }

    public MstShop getShop()
    {
        return shop;
    }

    public void setShop(MstShop shop)
    {
        this.shop = shop;
    }

    /**
     * ベッドマスタデータをArrayListに読み込む。
     */
    public void load(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        while (rs.next()) {
            MstBed mtc = new MstBed();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
    
    //VUINV start add 20140715 MASHU_予約登録画面
    /**
     * load beds by 
     * @param con
     * @param shopId
     * @param courseId
     * @param technicId
     * @throws java.sql.SQLException
     */
    public void loadBeds(ConnectionWrapper con, Integer shopId, Integer shopCategoryID) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQLLoadBedBy(shopId, shopCategoryID));

        while (rs.next()) {
            MstBed mtc = new MstBed();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
    //VUINV end add 20140715 MASHU_予約登録画面

    //VUINV start add 20140715 MASHU_予約登録画面
    /**
     * sql load bed by
     * @param shopId
     * @param shopCategoryID
     * @return
     */
    public String getSelectSQLLoadBedBy(Integer shopId, Integer shopCategoryID) {
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" SELECT msb.* ");
        sql.append(" FROM mst_bed msb ");
        sql.append(" INNER JOIN mst_bed_relation msbr ON msbr.bed_id = msb.bed_id AND msbr.shop_id = msb.shop_id ");
        sql.append(" INNER JOIN mst_shop_relation msr ON msbr.shop_category_id = msr.shop_category_id AND msbr.shop_id = msr.shop_id ");
        sql.append(" WHERE msbr.shop_id = ").append(SQLUtil.convertForSQL(shopId));
        sql.append(" AND msbr.shop_category_id = ").append(SQLUtil.convertForSQL(shopCategoryID));
        sql.append(" AND msb.delete_date IS NULL ");
        sql.append(" ORDER BY msb.display_seq ");

        return sql.toString();
    }
    //VUINV end add 20140715 MASHU_予約登録画面
    
    /**
     * ベッドマスタデータを全て取得するＳＱＬ文を取得する
     * @return ベッドマスタデータを全て取得するＳＱＬ文
     */
    public String getSelectSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_bed");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and shop_id = ").append(SQLUtil.convertForSQL(shop.getShopID()));
        sql.append(" order by");
        sql.append("      display_seq");
        sql.append("     ,bed_name");

        return sql.toString();
    }
    
    //IVS_LVTu start add 2015/06/16 #37295
    public void loadBedInputData(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        while (rs.next()) {
            MstBed mtc = new MstBed();
            mtc.setDataBedInput(rs);
            this.add(mtc);
        }

        rs.close();
    }
    //IVS_LVTu end add 2015/06/16 #37295
}

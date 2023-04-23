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
        sql.append("     and shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append(" order by");
        sql.append("      display_seq");
        sql.append("     ,bed_name");

        return sql.toString();
    }
}

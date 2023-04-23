/*
 * MstDiscounts.java
 *
 * Created on 2006/06/06, 15:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;

/**
 * 割引マスタデータのArrayList
 * @author katagiri
 */
public class MstDiscounts extends ArrayList<MstDiscount> {

    private Integer shopId = SystemInfo.getCurrentShop().getShopID();

    public MstDiscounts() {
    }

    public MstDiscounts(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 割引マスタを全て読み込む。
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException SQLException
     */
    public boolean load(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(getSelectSQL(null));

        while (rs.next()) {
            MstDiscount temp = new MstDiscount();
            temp.setData(rs);
            this.add(temp);
        }

        rs.close();

        return true;
    }

    public boolean load_Use(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(getSelectSQL(false));

        while (rs.next()) {
            MstDiscount temp = new MstDiscount();
            temp.setData(rs);
            this.add(temp);
        }

        rs.close();

        return true;
    }

    public boolean load_NotUse(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(getSelectSQL(true));

        while (rs.next()) {
            MstDiscount temp = new MstDiscount();
            temp.setData(rs);
            this.add(temp);
        }

        rs.close();

        return true;
    }

    /**
     * 割引マスタを取得するＳＱＬ文を取得する。
     * @return 割引マスタを取得するＳＱＬ文
     */
    public String getSelectSQL(Boolean isNotUse)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        //IVS_LVTu start edit 2015/06/16 #37295
        sql.append("     discount_id, discount_name, display_seq, discount_division, ");
        sql.append("     discount_method, discount_rate, discount_value ");
        //IVS_LVTu end edit 2015/06/16 #37295
        sql.append(" from");
        sql.append("     mst_discount");
        sql.append(" where");
        sql.append("         delete_date is null");
        if (isNotUse != null) {
            sql.append(" and " + (isNotUse ? "not exists" : "exists"));
            sql.append("     (");
            sql.append("         select 1 from mst_use_discount");
            sql.append("         where discount_id = mst_discount.discount_id");
            sql.append("           and shop_id = " + SQLUtil.convertForSQL(this.shopId));
            sql.append("     )");
        }
        sql.append(" order by");
        sql.append("     display_seq");

        return sql.toString();
    }

    public void regist(ConnectionWrapper con) throws SQLException {
        con.executeUpdate("delete from mst_use_discount where shop_id = " + SQLUtil.convertForSQL(this.shopId));
        for (MstDiscount mr : this) {
            con.executeUpdate("insert into mst_use_discount(shop_id, discount_id) values (" + SQLUtil.convertForSQL(this.shopId) + "," + SQLUtil.convertForSQL(mr.getDiscountID()) + ")") ;
        }
    }

}

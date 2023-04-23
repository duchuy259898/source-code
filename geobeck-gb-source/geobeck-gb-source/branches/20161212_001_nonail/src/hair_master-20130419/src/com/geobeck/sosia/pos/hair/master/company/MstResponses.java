/*
 * MstResponses.java
 *
 * Created on 2007/08/29, 11:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;

/**
 *
 * @author kanemoto
 */
public class MstResponses extends ArrayList<MstResponse> {

    private Integer shopId = SystemInfo.getCurrentShop().getShopID();
    private Integer slipNo = null;
    
    public MstResponses() {
    }

    public MstResponses(Integer shopId) {
        this.shopId = shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * レスポンスマスタデータをArrayListに読み込む。
     */
    public void load( ConnectionWrapper con ) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(null));

        while (rs.next()) {
            MstResponse mr = new MstResponse();
            mr.setData(rs);
            this.add(mr);
        }

        rs.close();
    }

    public void load_Use( ConnectionWrapper con ) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(false));

        while (rs.next()) {
            MstResponse mr = new MstResponse();
            mr.setData(rs);
            this.add(mr);
        }

        rs.close();
    }

    public void load_NotUse( ConnectionWrapper con ) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(true));

        while (rs.next()) {
            MstResponse mr = new MstResponse();
            mr.setData(rs);
            this.add(mr);
        }

        rs.close();
    }

    /**
     * レスポンスマスタデータを全て取得するＳＱＬ文を取得する
     * @return レスポンスマスタデータを全て取得するＳＱＬ文
     */
    public String getSelectSQL(Boolean isNotUse)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_response");
        sql.append(" where");
        sql.append("         delete_date is null");
        if (isNotUse != null) {
            sql.append(" and " + (isNotUse ? "not exists" : "exists"));
            sql.append("     (");
            sql.append("         select 1 from mst_use_response");
            sql.append("         where response_id = mst_response.response_id ");
            sql.append("           and shop_id = " + SQLUtil.convertForSQL(this.shopId));
            sql.append("     )");
        }

        if (this.slipNo != null) {
            sql.append(" union");
            sql.append(" select * from mst_response");
            sql.append(" where");
            sql.append("     response_id in");
            sql.append("     (");
            sql.append("         select");
            sql.append("             response_id");
            sql.append("         from");
            sql.append("             data_response_effect");
            sql.append("         where");
            sql.append("                 shop_id = " + SQLUtil.convertForSQL(this.shopId));
            sql.append("             and slip_no = " + SQLUtil.convertForSQL(this.slipNo));
            sql.append("     )");
        }

        sql.append(" order by");
        sql.append("      display_seq");
        sql.append("     ,response_name");

        return sql.toString();
    }

    public void regist(ConnectionWrapper con) throws SQLException {
        con.executeUpdate("delete from mst_use_response where shop_id = " + SQLUtil.convertForSQL(this.shopId));
        for (MstResponse mr : this) {
            con.executeUpdate("insert into mst_use_response(shop_id, response_id) values (" + SQLUtil.convertForSQL(this.shopId) + "," + SQLUtil.convertForSQL(mr.getResponseID()) + ")") ;
        }
    }

}

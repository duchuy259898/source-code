/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *口座情報
 * @author lvtu
 */
public class DataAccountInfoList extends ArrayList<DataAccountInfo>
{

    /**
     * 口座情報
     */
    public DataAccountInfoList()
    {
    }

    /**
     * 口座情報をArrayListに読み込む。
     * @param con
     * @throws java.sql.SQLException
     */
    public void load(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        while (rs.next()) {
            DataAccountInfo accountInfo = new DataAccountInfo();
            accountInfo.setData(rs);
            this.add(accountInfo);
        }

        rs.close();
    }
    
    /**
     * 口座情報て取得するＳＱＬ文を取得する
     * @return 口座情報を全て取得するＳＱＬ文
     */
    public String getSelectSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_account_info");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append(" order by");
        sql.append("      customer_id");

        return sql.toString();
    }
}

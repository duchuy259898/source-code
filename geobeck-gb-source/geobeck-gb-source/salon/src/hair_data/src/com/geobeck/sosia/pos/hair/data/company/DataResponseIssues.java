/*
 * DataResponseIssues.java
 *
 * Created on 2007/08/31, 11:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.company;

import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * レスポンス発行データ
 * @author kanemoto
 */
public class DataResponseIssues extends ArrayList<DataResponseIssue> {

    protected Integer shopID = null;

    /** Creates a new instance of DataResponseIssues */
    public DataResponseIssues() {
    }

    public DataResponseIssues(Integer shopID) {
        this.shopID = shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    /**
     * 発行データを取得する
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void loadData(ConnectionWrapper con) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        while (rs.next()) {
            DataResponseIssue dri = new DataResponseIssue();
            dri.setData(rs);
            this.add(dri);
        }

        rs.close();
    }

    /**
     * データ取得用SQLを取得する
     */
    public String getSelectSQL() {
        return "select\n"
                + "*\n"
                + "from\n"
                + "data_response_issue as dri\n"
                + "left join mst_response as mr on\n"
                + "dri.response_id = mr.response_id\n"
                + "left join mst_staff as ms on\n"
                + "dri.staff_id = ms.staff_id\n"
                + "where\n"
                + "dri.delete_date is null\n"
                + "and dri.shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "order by\n"
                + "dri.response_issue_id\n"
                + ";\n";
    }
}

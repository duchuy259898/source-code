/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *一括処理ログ
 * @author lvtu
 */
public class DataMonthlyBatchLogs extends ArrayList<DataMonthlyBatchLog> {
    
    /**
     * 対象月
     */
    protected Date targetMonth = null;
    
    /**
     * 一括処理ログ
     */
    public DataMonthlyBatchLogs()
    {
    }
    
    /**
     * 対象月を取得する。
     *
     * @return 対象月
     */
    public Date getTargetMonth() {
        return targetMonth;
    }

    /**
     * 対象月をセットする。
     *
     * @param targetMonth 対象月
     */
    public void setTargetMonth(Date targetMonth) {
        this.targetMonth = targetMonth;
    }

    /**
     * 一括処理ログをArrayListに読み込む。
     * @param con
     * @throws java.sql.SQLException
     */
    public void load(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        while (rs.next()) {
            DataMonthlyBatchLog batch = new DataMonthlyBatchLog();
            batch.setTargetMonth(rs.getDate("target_month"));
            this.add(batch);
        }

        rs.close();
    }
    
    /**
     * 一括処理ログて取得するＳＱＬ文を取得する
     * @return 口座情報を全て取得するＳＱＬ文
     */
    public String getSelectSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append("select mbl.target_month\n");
        sql.append("from data_monthly_batch_log mbl\n");
        sql.append("where mbl.delete_date is null\n");
        if ( this.getTargetMonth() != null) {
            sql.append("and mbl.target_month >= ").append(SQLUtil.convertForSQL(this.getTargetMonth())).append("::date\n");
        }
        sql.append("group by mbl.target_month\n");
        sql.append("order by mbl.target_month\n");

        return sql.toString();
    }
}

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
 *�ꊇ�������O
 * @author lvtu
 */
public class DataMonthlyBatchLogs extends ArrayList<DataMonthlyBatchLog> {
    
    /**
     * �Ώی�
     */
    protected Date targetMonth = null;
    
    /**
     * �ꊇ�������O
     */
    public DataMonthlyBatchLogs()
    {
    }
    
    /**
     * �Ώی����擾����B
     *
     * @return �Ώی�
     */
    public Date getTargetMonth() {
        return targetMonth;
    }

    /**
     * �Ώی����Z�b�g����B
     *
     * @param targetMonth �Ώی�
     */
    public void setTargetMonth(Date targetMonth) {
        this.targetMonth = targetMonth;
    }

    /**
     * �ꊇ�������O��ArrayList�ɓǂݍ��ށB
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
     * �ꊇ�������O�Ď擾����r�p�k�����擾����
     * @return ��������S�Ď擾����r�p�k��
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

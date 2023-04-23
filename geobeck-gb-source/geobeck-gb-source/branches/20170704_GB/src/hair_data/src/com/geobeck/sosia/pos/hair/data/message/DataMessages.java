/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.message;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author ivs
 */
public class DataMessages extends ArrayList<DataMessage> {
    
    public String dateLog = null;

    public String getDateLog() {
        return dateLog;
    }

    public void setDateLog(String dateLog) {
        this.dateLog = dateLog;
    }
    
    public boolean loadAll(ConnectionWrapper con, boolean main) {
        try{
             ResultSetWrapper rs = new ResultSetWrapper();
            if(main) {
                 rs = con.executeQuery(this.getSelectFromMainSQL());
            }
            else
            {
                 rs = con.executeQuery(this.getSelectAllSQL());
            }
        
            while (rs.next()) {
                DataMessage dm = new DataMessage();
                dm.setData(rs);
                this.add(dm);
        }
        rs.close();
        return true;
        }
        catch(Exception ex)
        {}
        return false;
    }
    
    public String getSelectAllSQL()
    {
        String sql="";
        sql="SELECT  * FROM data_message ";
        sql+= "        WHERE viewable_date between " + SQLUtil.convertForSQL(this.getDateLog().trim()) + " AND ";
        sql+= "    current_timestamp AND " ;
        sql+= "    view_flg = 1 ";
        sql+= "   AND delete_date is null ";
        sql+= " order by viewable_date DESC ";
       
        return sql;
    }
    public String getSelectFromMainSQL()
    {
        String sql = "";
        sql = " select * from data_message \n";
        sql += "where view_flg = 1 \n ";
        sql += "and viewable_date <= current_timestamp \n ";
        sql += "and delete_date is null \n ";
        sql += "order by viewable_date DESC \n ";
        sql += "offset 0 limit 10 \n";
        return sql;
    }
    
}

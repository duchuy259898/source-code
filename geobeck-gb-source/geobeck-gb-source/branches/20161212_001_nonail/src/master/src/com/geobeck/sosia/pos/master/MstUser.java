/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author lvtu
 */
public class MstUser {
    String  loginID         = null;
    Integer shopID          = null;
    Integer databaseID      = null;
    Integer useApi          = null;
    Integer apiSalonID      = null;

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(Integer databaseID) {
        this.databaseID = databaseID;
    }

    public Integer getUseApi() {
        return useApi;
    }

    public void setUseApi(Integer useApi) {
        this.useApi = useApi;
    }

    public Integer getApiSalonID() {
        return apiSalonID;
    }

    public void setApiSalonID(Integer apiSalonID) {
        this.apiSalonID = apiSalonID;
    }
    
    /**
     * set data.
     * @param rs
     * @throws SQLException 
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
        this.setUseApi(rs.getInt("use_api"));
    }
    
    /**
    * get database table mst_user.
    * @param con ConnectionWrapper
    * @throws java.sql.SQLException SQLException
    * @return Integer
    */
    public Integer load(ConnectionWrapper con) throws SQLException
    {
        if(this.getShopID() == null || this.getShopID() < 0)	return	null;

        if(con == null)	return	null;

        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

        if(rs.next())
        {
                this.setData(rs);
        }
        else
        {
                return null;
        }

        return	this.useApi;
    }
    
    /**
    * Select data.
    * @return Select String.
    */
    private String getSelectSQL()
    {
        return	"SELECT MAX (use_api) as use_api\n" +
                "FROM\n" +
                "mst_user\n" +
                "WHERE database_id = " + SQLUtil.convertForSQL(this.getDatabaseID()) + "\n" +
                "AND shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
    }
    
}

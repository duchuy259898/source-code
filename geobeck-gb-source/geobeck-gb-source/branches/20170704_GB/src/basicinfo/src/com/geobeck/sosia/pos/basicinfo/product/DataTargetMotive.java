/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author lvtu
 */
public class DataTargetMotive {
    
    private Integer shopId = 0;
    private Integer motiveId = 0;
    private Integer year = 0;
    private Integer month = 0;
    private Integer num = 0;
    private Integer shopCategoryId = 0;
    //IVS_LVTu start add 2015/01/22 Task #35026
    private Integer own_flg = 0;
    

    public Integer getOwn_flg() {
        return own_flg;
    }

    public void setOwn_flg(Integer own_flg) {
        this.own_flg = own_flg;
    }
    //IVS_LVTu end add 2015/01/22 Task #35026
    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getMotiveId() {
        return motiveId;
    }

    public void setMotiveId(Integer motiveId) {
        this.motiveId = motiveId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    
    /**
	 * ベッドマスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";

            if(isExists(con))
            {
                sql	=	this.getUpdateSQL();
            }
            else
            {
                sql	=	this.getInsertSQL();
            }

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
	}
        
        /**
	 * ベッドマスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopId() == null || this.getYear() == null
                        || this.getMonth()== null|| this.getMotiveId() == null|| this.getShopCategoryId() == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        
        /**
	 * Selectdata_target
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_target_motive\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
				"and	year = " + SQLUtil.convertForSQL(this.getYear()) + "\n"+
                                "and	month = " + SQLUtil.convertForSQL(this.getMonth()) + "\n"+
                                "and	shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategoryId()) + "\n"+
                                "and	motive_id = " + SQLUtil.convertForSQL(this.getMotiveId()) + "\n";
	}
    
        private String	getInsertSQL()
	{
		return	"insert into data_target_motive \n" +
                        "(shop_id, year, month, motive_id, num, insert_date, update_date, delete_date, own_flg, shop_category_id)  \n" +
                        "	values( \n" +
                        
                        "	" + SQLUtil.convertForSQL(this.getShopId()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getYear()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getMonth()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getMotiveId()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getNum()) + ", \n" +
                        "	current_timestamp, \n" +
                        "	current_timestamp, \n" +
                        "	null, \n" +
                        //IVS_LVTu start add 2015/01/22 Task #35026
                        "	" + SQLUtil.convertForSQL(this.getOwn_flg()) + ", \n" +
                        //IVS_LVTu end add 2015/01/22 Task #35026
                        "	" + SQLUtil.convertForSQL(this.getShopCategoryId()) + ")\n" ;
	}
	
	/**
	 * Update
	 * @return Update
	 */
	private String	getUpdateSQL()
	{
		return	"update data_target_motive \n" +
                "set \n" +
                "	num	      = "+SQLUtil.convertForSQL(this.getNum())+",\n" +
                //IVS_LVTu start add 2015/01/22 Task #35026        
                "	own_flg	      = "+SQLUtil.convertForSQL(this.getOwn_flg())+",\n" +
                //IVS_LVTu end add 2015/01/22 Task #35026        
                "	update_date	          = current_timestamp \n" +
                "where \n" +
                "	shop_id                   = "+SQLUtil.convertForSQL(this.getShopId())+"\n" +
                "	and shop_category_id      = "+SQLUtil.convertForSQL(this.getShopCategoryId())+"\n" +
                "	and motive_id             = "+SQLUtil.convertForSQL(this.getMotiveId())+"\n" +
                "	and year                  = "+SQLUtil.convertForSQL(this.getYear())+"\n" +
                "	and month                 = "+SQLUtil.convertForSQL(this.getMonth())+"";
	}
    
}

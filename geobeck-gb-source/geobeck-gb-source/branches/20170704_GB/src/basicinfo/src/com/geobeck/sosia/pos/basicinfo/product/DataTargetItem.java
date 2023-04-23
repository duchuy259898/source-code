package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lvtu
 */
public class DataTargetItem {
    
    private Integer shopId = 0;
    private Integer year = 0;
    private Integer month = 0;
    private Integer shopCategoryId = 0;
    private Integer itemcClassId = 0;
    private Double rate = 0d;
    private Integer value = 0;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public Integer getItemcClassId() {
        return itemcClassId;
    }

    public void setItemcClassId(Integer itemcClassId) {
        this.itemcClassId = itemcClassId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
                        || this.getMonth()== null|| this.getItemcClassId()== null|| this.getShopCategoryId() == null)	return	false;
		
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
				"from data_target_item\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
				"and	year = " + SQLUtil.convertForSQL(this.getYear()) + "\n"+
                                "and	month = " + SQLUtil.convertForSQL(this.getMonth()) + "\n"+
                                "and	shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategoryId()) + "\n"+
                                "and	item_class_id = " + SQLUtil.convertForSQL(this.getItemcClassId()) + "\n";
	}
    
        private String	getInsertSQL()
	{
		return	"insert into data_target_item \n" +
                        "(shop_id, year, month,item_class_id, rate, value, insert_date, update_date, delete_date ,shop_category_id)  \n" +
                        "	values( \n" +
                        
                        "	" + SQLUtil.convertForSQL(this.getShopId()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getYear()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getMonth()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getItemcClassId()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getValue()) + ", \n" +
                        "	current_timestamp, \n" +
                        "	current_timestamp, \n" +
                        "	null, \n" +
                        "	" + SQLUtil.convertForSQL(this.getShopCategoryId()) + ")\n" ;
	}
	
	/**
	 * Update
	 * @return Update
	 */
	private String	getUpdateSQL()
	{
		return	"update data_target_item \n" +
                "set \n" +
                "	rate	      = "+SQLUtil.convertForSQL(this.getRate())+",\n" +
                "	value	      = "+SQLUtil.convertForSQL(this.getValue())+",\n" +
                "	update_date	          = current_timestamp \n" +
                "where \n" +
                "	shop_id                   = "+SQLUtil.convertForSQL(this.getShopId())+"\n" +
                "	and shop_category_id      = "+SQLUtil.convertForSQL(this.getShopCategoryId())+"\n" +
                "	and item_class_id      = "+SQLUtil.convertForSQL(this.getItemcClassId())+"\n" +
                "	and year                  = "+SQLUtil.convertForSQL(this.getYear())+"\n" +
                "	and month                 = "+SQLUtil.convertForSQL(this.getMonth())+"";
	}
}

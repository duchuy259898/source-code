/*
 * DataReservationMainstaff.java
 *
 * Created on 2014/07/07, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.reservation;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.master.product.*;

/**
 *
 * @author DHThanh
 */
public class DataReservationMainstaff {
    
        protected	MstShop			shop				=	new MstShop();
	        
        private	Integer				shopCategoryId			= null;
        
        private	Integer				reservationNo			= null;
        
	/**
	 * スタッフ
	 */
	protected	MstStaff			staff				=	new MstStaff();
	
	/**
	 * 指名
	 */
	protected	boolean				designated			= false;
	
	/** Creates a new instance of DataReservationProportionally */
	public DataReservationMainstaff() {
	}
	
	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}
        
        public void setShop(MstShop shop) {
            this.shop = shop;
        }
	
	/**
	 * 予約Noを取得する
	 */
	public Integer getReservationNo()
	{
		return reservationNo;
	}        
       
        public void setReservationNo(Integer reservationNo) {
            this.reservationNo = reservationNo;
        }
        
        /**
        * @return the shopCategoryId
        */
       public Integer getShopCategoryId() {
           return shopCategoryId;
       }

       /**
        * @param shopCategoryId the shopCategoryId to set
        */
       public void setShopCategoryId(Integer shopCategoryId) {
           this.shopCategoryId = shopCategoryId;
       }

	/**
	 * スタッフを取得する。
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフをセットする。
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	/**
	 * 指名フラグを取得する
	 * @return designated 指名 true:指名 false:フリー
	 */
	public boolean getDesignated()
	{
		return designated;
	}
	
	/**
	 * 指名フラグをセットする
	 * @param designated 指名フラグ
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * データを登録する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
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
	 * データが存在するかチェックする。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(
			this.getReservationNo() == null ||
			this.getShopCategoryId() == null 
		) return false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        
        /**
	 * set Data for arraylist。
	 * @return 
	 */
        public void setData(ResultSetWrapper rs) throws SQLException {
            this.setShopCategoryId(rs.getInt("shop_category_id"));
            this.setDesignated(rs.getBoolean("designated_flag"));
            MstStaff ms = new MstStaff();
            ms.setStaffID(rs.getInt("staff_id"));
            ms.setStaffNo(rs.getString("staff_no"));
            this.setStaff(ms);
        }
	
	
	/**
	 * Select文を取得する。
	 * @return 
	 */
	private String getSelectSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"data_reservation_mainstaff\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			"and shop_category_id = " + SQLUtil.convertForSQL( this.getShopCategoryId() ) + "\n" +			
			";\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return 
	 */
	private String	getInsertSQL()
	{
		return
			"insert into data_reservation_mainstaff\n" +
			"( shop_id, reservation_no, shop_category_id, \n" +
			"designated_flag, staff_id,\n" +
			"insert_date, update_date, delete_date)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getShop().getShopID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getReservationNo() ) + ",\n" +
			SQLUtil.convertForSQL( this.getShopCategoryId() ) + ",\n" +			
			SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
			( this.getStaff() != null ? SQLUtil.convertForSQL( this.getStaff().getStaffID() ) : "null" ) + ",\n" +
			"current_timestamp, current_timestamp, null);\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return 
	 */
	private String	getUpdateSQL()
	{
		return
			"update data_reservation_mainstaff\n" +
			"set\n" + 
			"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ", \n" +
			"staff_id = " + ( this.getStaff() != null ? SQLUtil.convertForSQL( this.getStaff().getStaffID() ) : "null" ) + ", \n" +
			"update_date = current_timestamp, \n" +
			"delete_date = null\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			"and shop_category_id = " + SQLUtil.convertForSQL( this.getShopCategoryId() ) + "\n" +			
			";\n";
	}
    
}

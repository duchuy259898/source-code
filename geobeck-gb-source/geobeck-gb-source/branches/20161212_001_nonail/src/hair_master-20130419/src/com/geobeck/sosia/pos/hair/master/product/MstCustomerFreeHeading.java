/*
 * MstCustomerFreeHeading.java
 *
 * Created on 2007/08/20, 18:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;

/**
 *
 * @author kanemoto
 */
public class MstCustomerFreeHeading {
	protected	MstCustomer		mc	=	null;	/** 顧客情報 */
	protected	MstFreeHeading		mfh	=	null;	/** フリー項目 */ 

	/** Creates a new instance of MstCustomerFreeHeading */
	public MstCustomerFreeHeading() {
	}

	/**
	 * 顧客を取得する。
	 * @return 顧客
	 */
	public MstCustomer getMstCustomer()
	{
		return mc;
	}

	/**
	 * 顧客をセットする。
	 * @param mc 顧客
	 */
	public void setMstCustomer(MstCustomer mc)
	{
		this.mc = mc;
	}
    
	/**
	 * フリー項目区分を取得する。
	 * @return フリー項目区分
	 */
	public MstFreeHeading getMstFreeHeading()
	{
		return mfh;
	}

	/**
	 * フリー項目区分をセットする。
	 * @param mfh フリー項目区分
	 */
	public void setMstFreeHeading(MstFreeHeading mfh)
	{
		this.mfh = mfh;
	}
    
	/**
	 * データをセットする
	 */
	public void setData( MstCustomerFreeHeading mcfh )
	{
	    this.setMstCustomer( this.getMstCustomer() );
	    this.setMstFreeHeading( this.getMstFreeHeading() );
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstFreeHeadingClass mfhc = new MstFreeHeadingClass();
		
		mc	=	new	MstCustomer();
		mfh	=	new	MstFreeHeading();
		this.getMstCustomer().setData( rs );
		mfhc.setData( rs );
		this.getMstFreeHeading().setData( rs );
		this.getMstFreeHeading().setFreeHeadingClass( mfhc );
	}
	
	/**
	 * データを登録する。
	 * @param con ConnectionWrapper
	 * @param data 対象となるデータ
	 * @param dataIndex データのインデックス
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//存在しないデータの場合
		if(!this.isExist(con))
		{
			//データをInsertする
			if(con.executeUpdate(this.getInsertDataSQL()) != 1)
			{
				return	false;
			}
		}
		//存在するデータの場合
		else
		{
			//データをUpdateする
			if(con.executeUpdate(this.getUpdateDataSQL()) != 1)
			{
				return	false;
			}
		}
		return	true;
	}
	
	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		boolean		result		=	false;
		
		if(this.getMstCustomer() == null ||
				this.getMstFreeHeading() == null)
		{
			return	result;
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			result	=	true;
		}
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * データを削除する。
	 * @param con ConnectionWrapper
	 * @param data 対象となるデータ
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getDeleteDataSQL()) != 1)
		{
			return	false;
		}
		
		return	true;
	}
		
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_customer_free_heading\n" +
			"where\n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertDataSQL()
	{
		return
			"insert into mst_customer_free_heading\n" +
			"(customer_id, free_heading_class_id, free_heading_id, insert_date, \n" +
			"	update_date, delete_date)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingID() ) + ",\n" +
			"current_timestamp,current_timestamp, null);\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateDataSQL()
	{
		return
			"update mst_customer_free_heading\n" +
			"set free_heading_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingID() ) + ",\n" +
			"update_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteDataSQL()
	{
		return
			"update mst_customer_free_heading\n" +
			"set update_date = current_timestamp\n" +
			"delete_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
}

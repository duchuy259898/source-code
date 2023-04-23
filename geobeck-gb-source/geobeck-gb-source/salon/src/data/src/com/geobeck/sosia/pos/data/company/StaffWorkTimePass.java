/*
 * StaffWorkTimePass.java
 *
 * Created on 2007/08/10, 11:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.company;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author kanemoto
 */
public class StaffWorkTimePass {
	private		MstShop		shop		=	null;
	private		String		password	=	null;
	
	/** Creates a new instance of StaffWorkTimePass */
	public StaffWorkTimePass() {
	}
	
	/**
	 * 店舗を取得する
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return this.shop;
	}
	
	/**
	 * 店舗を登録する
	 * @param shop 店舗
	 */
	 public void setShop( MstShop shop )
	 {
		this.shop = shop;
	}
	
	/**
	 * パスワードを取得する
	 * @return パスワード
	 */
	public String getPassword()
	{
		return this.password;
	}
	
	/**
	 * パスワードを登録する
	 * @param shop パスワード
	 */
	 public void setPassword( String password )
	 {
		this.password = password;
	}
	
	/**
	 * データをセットする
	 */
	public void setData( ConnectionWrapper con, MstShop shop ) throws SQLException
	{
		this.setShop( shop );
		this.setPassword( this.getPassword( con, this.getShop().getShopID() ) );
	}
	
	/**
	 * レコードが存在するかを判定します
	* @param ConnectionWrapper con
	* @return レコードが存在する場合にTrueを返します
	 */
	public boolean isExists( ConnectionWrapper con ) throws SQLException
	{
		boolean		result	=	false;
		
		if(con == null)	return	false;
		ResultSetWrapper	rs	=	con.executeQuery( this.getSelectExistsPasswordSQL() );

		if(rs.next()) result	=	true;
		rs.close();
		return	result;
	}
	
	/**
	* パスワードが合っているかを判定する
	* @param ConnectionWrapper con
	* @return パスワードが正しい場合にTrueを返します
	*/
	public boolean isComparePassword( ConnectionWrapper con ) throws SQLException
	{
		boolean		result	=	false;
		
		if(con == null)	return	false;
		ResultSetWrapper	rs	=	con.executeQuery( this.getSelectComparePasswordSQL() );

		if(rs.next()) result	=	true;
		rs.close();
		return	result;
	}
	 
	/**
	 * パスワードを取得します
	 */
	public String getPassword( ConnectionWrapper con, Integer shopID ) throws SQLException
	{
		String result = null;
		
		if( (con != null)&&(shopID !=null) )
		{
                    ResultSetWrapper rs = con.executeQuery( this.getSelectPasswordByShopIDSQL( shopID ) );
                    
                    // データが存在すれば
                    if ( rs.next() )
                    {
                        result = rs.getString( "work_time_password" );
                    }
                    rs.close();
		}

		return	result;
	}
	
	/**
	 * パスワードを変更する
	 */
	public boolean updatePassword( ConnectionWrapper con, String oldPass, String newPass ) throws SQLException
	{
		String sql = "";

		// レコードが存在するならSQLを取得する
		if( isExists( con ) ) sql = this.getUpdatePasswordSQL( oldPass, newPass );
		else sql = this.getInsertPasswordSQL( newPass );
		
		if(con.executeUpdate(sql) == 1)
		{
			// 所持パスワードを変更する
			this.setPassword( newPass );
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * パスワードを削除する
	 */
	public boolean deletePassword( ConnectionWrapper con ) throws SQLException
	{
		String sql = "";

		// レコードが存在するならSQLを取得する
		if( !isExists( con ) ) sql = this.getDeletePasswordSQL();
		else return false;
		
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
	 * レコード存在チェック用SQLを取得する
	 * @return レコード存在チェック用SQL
	 */
	public String getSelectExistsPasswordSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_work_time_password\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and delete_date is  null\n" +
			";\n";
	}
	
	/**
	 * パスワードチェック用SQLを取得する
	 * @return パスワードチェック用SQL
	 */
	public String getSelectComparePasswordSQL()
	{
		String retStr = "";
		
		retStr = 
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_work_time_password\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n";
		
		if( this.getPassword() == null ) retStr += "and work_time_password is null\n";
		else                                retStr += "and work_time_password = " + SQLUtil.convertForSQL( this.getPassword() ) + "\n";
		
		retStr += 
			"and delete_date is  null\n" +
			";\n";
		return retStr;
	}
	
	/**
	 * 店舗IDからパスワード取得用SQLを取得する
	 * @return パスワード
	 */
	public String getSelectPasswordByShopIDSQL( int shopID )
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_work_time_password\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( shopID ) + "\n" +
			"and delete_date is  null\n" +
			";\n";
	}
	
	/**
	 * パスワード挿入用SQLを取得する
	 * @return 挿入用SQL
	 */
	public String getInsertPasswordSQL( String newPassword )
	{
	    return
		"insert into mst_work_time_password\n" +
		"(shop_id, work_time_password, insert_date, update_date, delete_date)\n" +
		"values(\n" +
		SQLUtil.convertForSQL( this.getShop().getShopID() ) + ",\n" +
		SQLUtil.convertForSQL( newPassword ) + ",\n" +
		"current_timestamp, current_timestamp, null );\n";
	}
	/**
	 * パスワード変更用SQLを取得する
	 * @param  oldPassword 旧パスワード
	 * @param  newPassword 新パスワード
	 * @return パスワード変雇用SQL
	 */
	public String getUpdatePasswordSQL( String oldPassword, String newPassword )
	{
		String retStr = "";
		
		retStr = 
			"update\n" +
			"mst_work_time_password\n" +
			"set work_time_password = " + SQLUtil.convertForSQL( newPassword ) + ", \n" +
			"update_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n";
		
		if( this.getPassword() == null ) retStr += "and work_time_password is null\n";
		else                                retStr += "and work_time_password = " + SQLUtil.convertForSQL( oldPassword ) + "\n";
		
		retStr +=
			"and delete_date is  null\n" +
			";\n";
		return retStr;
	}
	
	/**
	 * パスワード削除用SQLを取得する
	 * @return パスワード削除用SQL
	 */
	public String getDeletePasswordSQL()
	{
		return
			"update\n" +
			"mst_work_time_password\n" +
			"set update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and delete_date is  null\n" +
			";\n";
	}
}

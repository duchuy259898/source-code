/*
 * MstPlace.java
 *
 * Created on 2008/09/03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;


/**
 * 置き場マスタデータ
 * @author mizukawa
 */
public class MstPlace
{
	/**
	 * 置き場ＩＤ
	 */
	private	Integer		placeID		=	null;
	/**
	 * 置き場名
	 */
	private	String		placeName		=	"";
	
	/**
	 * コンストラクタ
	 */
	public MstPlace()
	{
	}
	
	/**
	 * 文字列に変換する。（スタッフ区分名）
	 * @return スタッフ区分名
	 */
	public String toString()
	{
		return	this.getPlaceName();
	}

	/**
	 * 置き場ＩＤを取得する。
	 * @return 置き場ＩＤ
	 */
	public Integer getPlaceID()
	{
		return placeID;
	}

	/**
	 * 置き場をセットする。
	 * @param placeID 置き場ＩＤ
	 */
	public void setPlaceID(Integer placeID)
	{
		this.placeID = placeID;
	}

	/**
	 * 置き場名を取得する。
	 * @return 置き場名
	 */
	public String getPlaceName()
	{
		return placeName;
	}

	/**
	 * 置き場名をセットする。
	 * @param placeName 置き場名
	 */
	public void setPlaceName(String placeName)
	{
		this.placeName = placeName;
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setPlaceID(null);
		this.setPlaceName("");
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper 
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setPlaceID(rs.getInt("place_id"));
		this.setPlaceName(rs.getString("place_name"));
	}
	
	
	/**
	 * 置き場マスタにデータを登録する。
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
	 * 置き場マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getDeleteSQL();
		}
		else
		{
			return	false;
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
	 * 置き場マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getPlaceID() == null || this.getPlaceID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_place\n" +
				"where	place_id = " + SQLUtil.convertForSQL(this.getPlaceID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_place\n" +
				"(place_id, place_name,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(place_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getPlaceName()) + ",\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_place\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_place\n" +
				"set\n" +
				"place_name = " + SQLUtil.convertForSQL(this.getPlaceName()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	place_id = " + SQLUtil.convertForSQL(this.getPlaceID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_place\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where	place_id = " + SQLUtil.convertForSQL(this.getPlaceID()) + "\n";
	}
}

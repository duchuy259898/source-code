/*
 * TableInfo.java
 *
 * Created on 2006/12/22, 11:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.csv;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * テーブル情報クラス
 * @author katagiri
 */
public class TableInfo extends ArrayList<ColumnInfo>
{
	/**
	 * テーブル名
	 */
	private	String		tableName		=	"";
	/**
	 * テーブルのタイトル
	 */
	private	String		tableTitle		=	"";
	
	/**
	 * コンストラクタ
	 */
	public TableInfo()
	{
	}
	
	/**
	 * コンストラクタ
	 * @param tableName テーブル名
	 * @param tableTitle テーブルのタイトル
	 */
	public TableInfo(String tableName, String tableTitle)
	{
		this.setTableName(tableName);
		this.setTableTitle(tableTitle);
	}

	/**
	 * 文字列（テーブルのタイトル）に変換する。
	 * @return 文字列（テーブルのタイトル）
	 */
	public String toString()
	{
		return tableTitle;
	}

	/**
	 * テーブル名を取得する。
	 * @return テーブル名
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * テーブル名を設定する。
	 * @param tableName テーブル名
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * テーブルのタイトルを取得する。
	 * @return テーブルのタイトル
	 */
	public String getTableTitle()
	{
		return tableTitle;
	}

	/**
	 * テーブルのタイトルを設定する。
	 * @param tableTitle テーブルのタイトル
	 */
	public void setTableTitle(String tableTitle)
	{
		this.tableTitle = tableTitle;
	}
	
	/**
	 * テーブル情報を読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void loadTableInfo(ConnectionWrapper con) throws SQLException
	{
		if(tableName.equals(""))	return;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getTableInfoSQL());
		
		while(rs.next())
		{
			ColumnInfo		ci	=	new ColumnInfo();
			ci.setColumnName(rs.getString("name"));
			ci.setColumnType(rs.getString("type"));
			
			this.add(ci);
		}
		
		rs.close();
	}
	
	/**
	 * テーブル情報を読み込むＳＱＬ文を取得する。
	 * @return テーブル情報を読み込むＳＱＬ文
	 */
	private String getTableInfoSQL()
	{
		return	"select\n" +
				"cls.oid as id,\n" +
				"att.attname as name,\n" +
				"typ.typname as type\n" +
				"from (\n" +
				"select oid, *\n" +
				"from pg_class\n" +
				"where relname = '" + tableName + "'\n" +
				") as cls\n" +
				"inner join pg_namespace as nsp\n" +
				"on nsp.oid = cls.relnamespace\n" +
				"left join pg_attribute as att\n" +
				"on cls.oid = att.attrelid\n" +
				"and not att.attisdropped\n" +
				"left join pg_type as typ\n" +
				"on typ.oid = att.atttypid\n" +
				"where att.attnum > 0\n" +
				"order by att.attnum\n";
	}
}

/*
 * ColumnInfo.java
 *
 * Created on 2006/12/22, 11:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.csv;

/**
 * テーブルの列情報クラス
 * @author katagiri
 */
public class ColumnInfo
{
	/**
	 * 列名
	 */
	private	String		columnName		=	"";
	/**
	 * 列の種類
	 */
	private	String		columnType		=	"";
	/**
	 * 列の情報を保持するためのクラス
	 */
	private	Class		columnClass		=	null;
	/**
	 * 文字列の列かどうか
	 */
	private Boolean		stringColumn	=	false;
	
	/**
	 * コンストラクタ
	 */
	public ColumnInfo()
	{
	}

	/**
	 * 文字列（列名）を返す。
	 * @return 文字列（列名）
	 */
	public String toString()
	{
		return columnName;
	}

	/**
	 * 列名を取得する。
	 * @return 列名
	 */
	public String getColumnName()
	{
		return columnName;
	}

	/**
	 * 列名を設定する。
	 * @param columnName 列名
	 */
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	/**
	 * 列の種類を取得する。
	 * @return 列の種類
	 */
	public String getColumnType()
	{
		return columnType;
	}

	/**
	 * 列の種類を設定する。
	 * @param columnType 列の種類
	 */
	public void setColumnType(String columnType)
	{
		this.columnType = columnType;
		this.setColumnClass();
	}

	/**
	 * 列の情報を保持するためのクラスを取得する。
	 * @return 列の情報を保持するためのクラス
	 */
	public Class getColumnClass()
	{
		return columnClass;
	}

	/**
	 * 列の情報を保持するためのクラスを設定する。
	 */
	private void setColumnClass()
	{
		this.setStringColumn(false);
		
		if(columnType.equals("char") ||
				columnType.equals("varchar") ||
				columnType.equals("text") ||
				columnType.equals("date") ||
				columnType.equals("time") ||
				columnType.equals("datetime"))
		{
			columnClass	=	String.class;
			this.setStringColumn(true);
		}
		else if(columnType.equals("int2") ||
				columnType.equals("int4"))
		{
			columnClass	=	Integer.class;
		}
		else if(columnType.equals("int8"))
		{
			columnClass	=	Long.class;
		}
		else if(columnType.equals("float4") ||
				columnType.equals("float8") ||
				columnType.equals("numeric"))
		{
			columnClass	=	Double.class;
		}
		else if(columnType.equals("bool"))
		{
			columnClass	=	Boolean.class;
		}
		else
		{
			columnClass	=	null;
		}
	}
	
	/**
	 * 文字列の列かどうかを取得する。
	 * @return 文字列の列かどうか
	 */
	public Boolean isStringColumn()
	{
		return	stringColumn;
	}
	
	/**
	 * 文字列の列かどうかを設定する。
	 * @param stringColumn 文字列の列かどうか
	 */
	private void setStringColumn(Boolean stringColumn)
	{
		this.stringColumn = stringColumn;
	}
}

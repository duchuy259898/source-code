/*
 * MstSheet.java
 *
 * Created on 2006/09/08, 17:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.label;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;

/**
 * 用紙マスタ用クラス
 * @author katagiri
 */
public class MstSheet
{
	/**
	 * 用紙ID
	 */
	private Integer		sheetID			=	null;
	/**
	 * 用紙名
	 */
	private	String		sheetName		=	"";
	/**
	 * 用紙の幅
	 */
	private	Double		sheetWidth		=	0d;
	/**
	 * 用紙の高さ
	 */
	private	Double		sheetHeight		=	0d;
	
	/** Creates a new instance of MstSheet */
	public MstSheet()
	{
	}

	/**
	 * 用紙IDを取得する。
	 * @return 用紙ID
	 */
	public Integer getSheetID()
	{
		return sheetID;
	}

	/**
	 * 用紙IDをセットする。
	 * @param sheetID 用紙ID
	 */
	public void setSheetID(Integer sheetID)
	{
		this.sheetID = sheetID;
	}

	/**
	 * 用紙名を取得する。
	 * @return 用紙名
	 */
	public String getSheetName()
	{
		return sheetName;
	}

	/**
	 * 用紙名セットする。
	 * @param sheetName 用紙名
	 */
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}

	/**
	 * 用紙の幅を取得する。
	 * @return 用紙の幅
	 */
	public Double getSheetWidth()
	{
		return sheetWidth;
	}

	/**
	 * 用紙の幅セットする。
	 * @param sheetWidth 用紙の幅
	 */
	public void setSheetWidth(Double sheetWidth)
	{
		this.sheetWidth = sheetWidth;
	}

	/**
	 * 用紙の高さを取得する。
	 * @return 用紙の高さ
	 */
	public Double getSheetHeigth()
	{
		return sheetHeight;
	}

	/**
	 * 用紙の高さセットする。
	 * @param sheetHeight 用紙の高さ
	 */
	public void setSheetHeight(Double sheetHeight)
	{
		this.sheetHeight = sheetHeight;
	}
	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setSheetID(rs.getInt("sheet_id"));
		this.setSheetName(rs.getString("sheet_name"));
		this.setSheetWidth(rs.getDouble("sheet_width"));
		this.setSheetHeight(rs.getDouble("sheet_height"));
	}
	
	/**
	 * 文字列に変換する。（用紙名）
	 * @return 用紙名
	 */
	public String toString()
	{
		return	this.getSheetName();
	}
}

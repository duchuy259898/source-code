/*
 * AddressUtil.java
 *
 * Created on 2006/04/21, 20:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;

/**
 * ZŠƒ†[ƒeƒBƒŠƒeƒB
 * @author katagiri
 */
public class AddressUtil
{
	/**
	 * ‘S‘’n•ûŒö‹¤’c‘ÌƒR[ƒh
	 */
	private	String		code			=	"";
	/**
	 * ‹Œ—X•Ö”Ô†
	 */
	private	String		postalCodeOld	=	"";
	/**
	 * —X•Ö”Ô†
	 */
	private	String		postalCode		=	"";
	/**
	 * “s“¹•{Œ§ƒtƒŠƒKƒi
	 */
	private	String		prefectureKana	=	"";
	/**
	 * s‹æ’¬‘ºƒtƒŠƒKƒi
	 */
	private	String		cityKana		=	"";
	/**
	 * ’¬ˆæƒtƒŠƒKƒi
	 */
	private	String		townKana		=	"";
	/**
	 * “s“¹•{Œ§–¼
	 */
	private	String		prefectureName	=	"";
	/**
	 * s‹æ’¬‘º–¼
	 */
	private	String		cityName		=	"";
	/**
	 * ’¬ˆæ–¼
	 */
	private	String		townName		=	"";
	
	/** Creates a new instance of AddressUtil */
	public AddressUtil()
	{
	}

	/**
	 * ‘S‘’n•ûŒö‹¤’c‘ÌƒR[ƒh
	 * @return ‘S‘’n•ûŒö‹¤’c‘ÌƒR[ƒh
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * ‘S‘’n•ûŒö‹¤’c‘ÌƒR[ƒh
	 * @param code ‘S‘’n•ûŒö‹¤’c‘ÌƒR[ƒh
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * ‹Œ—X•Ö”Ô†
	 * @return ‹Œ—X•Ö”Ô†
	 */
	public String getPostalCodeOld()
	{
		return postalCodeOld;
	}

	/**
	 * ‹Œ—X•Ö”Ô†
	 * @param postalCodeOld ‹Œ—X•Ö”Ô†
	 */
	public void setPostalCodeOld(String postalCodeOld)
	{
		this.postalCodeOld = postalCodeOld;
	}

	/**
	 * —X•Ö”Ô†
	 * @return —X•Ö”Ô†
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * —X•Ö”Ô†
	 * @param postalCode —X•Ö”Ô†
	 */
	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * “s“¹•{Œ§ƒtƒŠƒKƒi
	 * @return “s“¹•{Œ§ƒtƒŠƒKƒi
	 */
	public String getPrefectureKana()
	{
		return prefectureKana;
	}

	/**
	 * “s“¹•{Œ§ƒtƒŠƒKƒi
	 * @param prefectureKana “s“¹•{Œ§ƒtƒŠƒKƒi
	 */
	public void setPrefectureKana(String prefectureKana)
	{
		this.prefectureKana = prefectureKana;
	}

	/**
	 * s‹æ’¬‘ºƒtƒŠƒKƒi
	 * @return s‹æ’¬‘ºƒtƒŠƒKƒi
	 */
	public String getCityKana()
	{
		return cityKana;
	}

	/**
	 * s‹æ’¬‘ºƒtƒŠƒKƒi
	 * @param cityKana s‹æ’¬‘ºƒtƒŠƒKƒi
	 */
	public void setCityKana(String cityKana)
	{
		this.cityKana = cityKana;
	}

	/**
	 * ’¬ˆæ–¼
	 * @return ’¬ˆæ–¼
	 */
	public String getTownKana()
	{
		return townKana;
	}

	/**
	 * ’¬ˆæ–¼
	 * @param townKana ’¬ˆæ–¼
	 */
	public void setTownKana(String townKana)
	{
		this.townKana = townKana;
	}

	/**
	 * “s“¹•{Œ§–¼
	 * @return “s“¹•{Œ§–¼
	 */
	public String getPrefectureName()
	{
		return prefectureName;
	}

	/**
	 * “s“¹•{Œ§–¼
	 * @param prefectureName “s“¹•{Œ§–¼
	 */
	public void setPrefectureName(String prefectureName)
	{
		this.prefectureName = prefectureName;
	}

	/**
	 * s‹æ’¬‘º–¼
	 * @return s‹æ’¬‘º–¼
	 */
	public String getCityName()
	{
		return cityName;
	}

	/**
	 * s‹æ’¬‘º–¼
	 * @param cityName s‹æ’¬‘º–¼
	 */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * ’¬ˆæ–¼
	 * @return ’¬ˆæ–¼
	 */
	public String getTownName()
	{
		return townName;
	}

	/**
	 * ’¬ˆæ–¼
	 * @param townName ’¬ˆæ–¼
	 */
	public void setTownName(String townName)
	{
		this.townName = townName;
	}
	
	/**
	 * —X•Ö”Ô†‚©‚çZŠƒf[ƒ^‚ğæ“¾‚·‚éB
	 * @return true - ¬Œ÷
	 */
	public boolean getDataByPostalCode(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
		String		sql		=	"";
		
		sql	+=	"select	*\n";
		sql	+=	"from	mst_postal_code\n";
		sql	+=	"where	postal_code = '" + this.getPostalCode() + "'\n";
		
		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(sql);

		if(rs.next())
		{
			this.setData(rs);
			result	=	true;
		}

		rs.close();
		
		return	result;
	}
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚éB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException 
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCode(rs.getString("code"));
		this.setPostalCodeOld(rs.getString("postal_code_old"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setPrefectureKana(rs.getString("prefecture_kana"));
		this.setCityKana(rs.getString("city_kana"));
		this.setTownKana(rs.getString("town_kana"));
		this.setPrefectureName(rs.getString("prefecture_name"));
		this.setCityName(rs.getString("city_name"));
		this.setTownName(rs.getString("town_name"));
	}
}

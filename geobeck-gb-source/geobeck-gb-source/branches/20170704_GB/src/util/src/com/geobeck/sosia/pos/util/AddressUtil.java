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
 * Z[eBeB
 * @author katagiri
 */
public class AddressUtil
{
	/**
	 * Snϋφ€cΜR[h
	 */
	private	String		code			=	"";
	/**
	 * XΦΤ
	 */
	private	String		postalCodeOld	=	"";
	/**
	 * XΦΤ
	 */
	private	String		postalCode		=	"";
	/**
	 * sΉ{§tKi
	 */
	private	String		prefectureKana	=	"";
	/**
	 * sζ¬ΊtKi
	 */
	private	String		cityKana		=	"";
	/**
	 * ¬ζtKi
	 */
	private	String		townKana		=	"";
	/**
	 * sΉ{§Ό
	 */
	private	String		prefectureName	=	"";
	/**
	 * sζ¬ΊΌ
	 */
	private	String		cityName		=	"";
	/**
	 * ¬ζΌ
	 */
	private	String		townName		=	"";
	
	/** Creates a new instance of AddressUtil */
	public AddressUtil()
	{
	}

	/**
	 * Snϋφ€cΜR[h
	 * @return Snϋφ€cΜR[h
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Snϋφ€cΜR[h
	 * @param code Snϋφ€cΜR[h
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * XΦΤ
	 * @return XΦΤ
	 */
	public String getPostalCodeOld()
	{
		return postalCodeOld;
	}

	/**
	 * XΦΤ
	 * @param postalCodeOld XΦΤ
	 */
	public void setPostalCodeOld(String postalCodeOld)
	{
		this.postalCodeOld = postalCodeOld;
	}

	/**
	 * XΦΤ
	 * @return XΦΤ
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * XΦΤ
	 * @param postalCode XΦΤ
	 */
	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * sΉ{§tKi
	 * @return sΉ{§tKi
	 */
	public String getPrefectureKana()
	{
		return prefectureKana;
	}

	/**
	 * sΉ{§tKi
	 * @param prefectureKana sΉ{§tKi
	 */
	public void setPrefectureKana(String prefectureKana)
	{
		this.prefectureKana = prefectureKana;
	}

	/**
	 * sζ¬ΊtKi
	 * @return sζ¬ΊtKi
	 */
	public String getCityKana()
	{
		return cityKana;
	}

	/**
	 * sζ¬ΊtKi
	 * @param cityKana sζ¬ΊtKi
	 */
	public void setCityKana(String cityKana)
	{
		this.cityKana = cityKana;
	}

	/**
	 * ¬ζΌ
	 * @return ¬ζΌ
	 */
	public String getTownKana()
	{
		return townKana;
	}

	/**
	 * ¬ζΌ
	 * @param townKana ¬ζΌ
	 */
	public void setTownKana(String townKana)
	{
		this.townKana = townKana;
	}

	/**
	 * sΉ{§Ό
	 * @return sΉ{§Ό
	 */
	public String getPrefectureName()
	{
		return prefectureName;
	}

	/**
	 * sΉ{§Ό
	 * @param prefectureName sΉ{§Ό
	 */
	public void setPrefectureName(String prefectureName)
	{
		this.prefectureName = prefectureName;
	}

	/**
	 * sζ¬ΊΌ
	 * @return sζ¬ΊΌ
	 */
	public String getCityName()
	{
		return cityName;
	}

	/**
	 * sζ¬ΊΌ
	 * @param cityName sζ¬ΊΌ
	 */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * ¬ζΌ
	 * @return ¬ζΌ
	 */
	public String getTownName()
	{
		return townName;
	}

	/**
	 * ¬ζΌ
	 * @param townName ¬ζΌ
	 */
	public void setTownName(String townName)
	{
		this.townName = townName;
	}
	
	/**
	 * XΦΤ©ηZf[^πζΎ·ιB
	 * @return true - ¬χ
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
	 * ResultSetWrapper©ηf[^πZbg·ιB
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

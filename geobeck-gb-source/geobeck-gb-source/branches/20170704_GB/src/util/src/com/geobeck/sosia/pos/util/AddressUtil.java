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
 * �Z�����[�e�B���e�B
 * @author katagiri
 */
public class AddressUtil
{
	/**
	 * �S���n�������c�̃R�[�h
	 */
	private	String		code			=	"";
	/**
	 * ���X�֔ԍ�
	 */
	private	String		postalCodeOld	=	"";
	/**
	 * �X�֔ԍ�
	 */
	private	String		postalCode		=	"";
	/**
	 * �s���{���t���K�i
	 */
	private	String		prefectureKana	=	"";
	/**
	 * �s�撬���t���K�i
	 */
	private	String		cityKana		=	"";
	/**
	 * ����t���K�i
	 */
	private	String		townKana		=	"";
	/**
	 * �s���{����
	 */
	private	String		prefectureName	=	"";
	/**
	 * �s�撬����
	 */
	private	String		cityName		=	"";
	/**
	 * ���於
	 */
	private	String		townName		=	"";
	
	/** Creates a new instance of AddressUtil */
	public AddressUtil()
	{
	}

	/**
	 * �S���n�������c�̃R�[�h
	 * @return �S���n�������c�̃R�[�h
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * �S���n�������c�̃R�[�h
	 * @param code �S���n�������c�̃R�[�h
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * ���X�֔ԍ�
	 * @return ���X�֔ԍ�
	 */
	public String getPostalCodeOld()
	{
		return postalCodeOld;
	}

	/**
	 * ���X�֔ԍ�
	 * @param postalCodeOld ���X�֔ԍ�
	 */
	public void setPostalCodeOld(String postalCodeOld)
	{
		this.postalCodeOld = postalCodeOld;
	}

	/**
	 * �X�֔ԍ�
	 * @return �X�֔ԍ�
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * �X�֔ԍ�
	 * @param postalCode �X�֔ԍ�
	 */
	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * �s���{���t���K�i
	 * @return �s���{���t���K�i
	 */
	public String getPrefectureKana()
	{
		return prefectureKana;
	}

	/**
	 * �s���{���t���K�i
	 * @param prefectureKana �s���{���t���K�i
	 */
	public void setPrefectureKana(String prefectureKana)
	{
		this.prefectureKana = prefectureKana;
	}

	/**
	 * �s�撬���t���K�i
	 * @return �s�撬���t���K�i
	 */
	public String getCityKana()
	{
		return cityKana;
	}

	/**
	 * �s�撬���t���K�i
	 * @param cityKana �s�撬���t���K�i
	 */
	public void setCityKana(String cityKana)
	{
		this.cityKana = cityKana;
	}

	/**
	 * ���於
	 * @return ���於
	 */
	public String getTownKana()
	{
		return townKana;
	}

	/**
	 * ���於
	 * @param townKana ���於
	 */
	public void setTownKana(String townKana)
	{
		this.townKana = townKana;
	}

	/**
	 * �s���{����
	 * @return �s���{����
	 */
	public String getPrefectureName()
	{
		return prefectureName;
	}

	/**
	 * �s���{����
	 * @param prefectureName �s���{����
	 */
	public void setPrefectureName(String prefectureName)
	{
		this.prefectureName = prefectureName;
	}

	/**
	 * �s�撬����
	 * @return �s�撬����
	 */
	public String getCityName()
	{
		return cityName;
	}

	/**
	 * �s�撬����
	 * @param cityName �s�撬����
	 */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * ���於
	 * @return ���於
	 */
	public String getTownName()
	{
		return townName;
	}

	/**
	 * ���於
	 * @param townName ���於
	 */
	public void setTownName(String townName)
	{
		this.townName = townName;
	}
	
	/**
	 * �X�֔ԍ�����Z���f�[�^���擾����B
	 * @return true - ����
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
	 * ResultSetWrapper����f�[�^���Z�b�g����B
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

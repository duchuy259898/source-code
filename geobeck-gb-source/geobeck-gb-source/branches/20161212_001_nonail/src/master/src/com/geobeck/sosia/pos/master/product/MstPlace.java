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
 * �u����}�X�^�f�[�^
 * @author mizukawa
 */
public class MstPlace
{
	/**
	 * �u����h�c
	 */
	private	Integer		placeID		=	null;
	/**
	 * �u���ꖼ
	 */
	private	String		placeName		=	"";
	
	/**
	 * �R���X�g���N�^
	 */
	public MstPlace()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�X�^�b�t�敪���j
	 * @return �X�^�b�t�敪��
	 */
	public String toString()
	{
		return	this.getPlaceName();
	}

	/**
	 * �u����h�c���擾����B
	 * @return �u����h�c
	 */
	public Integer getPlaceID()
	{
		return placeID;
	}

	/**
	 * �u������Z�b�g����B
	 * @param placeID �u����h�c
	 */
	public void setPlaceID(Integer placeID)
	{
		this.placeID = placeID;
	}

	/**
	 * �u���ꖼ���擾����B
	 * @return �u���ꖼ
	 */
	public String getPlaceName()
	{
		return placeName;
	}

	/**
	 * �u���ꖼ���Z�b�g����B
	 * @param placeName �u���ꖼ
	 */
	public void setPlaceName(String placeName)
	{
		this.placeName = placeName;
	}
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setPlaceID(null);
		this.setPlaceName("");
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper 
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setPlaceID(rs.getInt("place_id"));
		this.setPlaceName(rs.getString("place_name"));
	}
	
	
	/**
	 * �u����}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �u����}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �u����}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
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
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_place\n" +
				"where	place_id = " + SQLUtil.convertForSQL(this.getPlaceID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
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
	 * Update�����擾����B
	 * @return Update��
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
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
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

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
 * �p���}�X�^�p�N���X
 * @author katagiri
 */
public class MstSheet
{
	/**
	 * �p��ID
	 */
	private Integer		sheetID			=	null;
	/**
	 * �p����
	 */
	private	String		sheetName		=	"";
	/**
	 * �p���̕�
	 */
	private	Double		sheetWidth		=	0d;
	/**
	 * �p���̍���
	 */
	private	Double		sheetHeight		=	0d;
	
	/** Creates a new instance of MstSheet */
	public MstSheet()
	{
	}

	/**
	 * �p��ID���擾����B
	 * @return �p��ID
	 */
	public Integer getSheetID()
	{
		return sheetID;
	}

	/**
	 * �p��ID���Z�b�g����B
	 * @param sheetID �p��ID
	 */
	public void setSheetID(Integer sheetID)
	{
		this.sheetID = sheetID;
	}

	/**
	 * �p�������擾����B
	 * @return �p����
	 */
	public String getSheetName()
	{
		return sheetName;
	}

	/**
	 * �p�����Z�b�g����B
	 * @param sheetName �p����
	 */
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}

	/**
	 * �p���̕����擾����B
	 * @return �p���̕�
	 */
	public Double getSheetWidth()
	{
		return sheetWidth;
	}

	/**
	 * �p���̕��Z�b�g����B
	 * @param sheetWidth �p���̕�
	 */
	public void setSheetWidth(Double sheetWidth)
	{
		this.sheetWidth = sheetWidth;
	}

	/**
	 * �p���̍������擾����B
	 * @return �p���̍���
	 */
	public Double getSheetHeigth()
	{
		return sheetHeight;
	}

	/**
	 * �p���̍����Z�b�g����B
	 * @param sheetHeight �p���̍���
	 */
	public void setSheetHeight(Double sheetHeight)
	{
		this.sheetHeight = sheetHeight;
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
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
	 * ������ɕϊ�����B�i�p�����j
	 * @return �p����
	 */
	public String toString()
	{
		return	this.getSheetName();
	}
}

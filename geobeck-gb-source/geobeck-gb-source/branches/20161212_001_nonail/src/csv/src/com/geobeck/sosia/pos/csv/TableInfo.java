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
 * �e�[�u�����N���X
 * @author katagiri
 */
public class TableInfo extends ArrayList<ColumnInfo>
{
	/**
	 * �e�[�u����
	 */
	private	String		tableName		=	"";
	/**
	 * �e�[�u���̃^�C�g��
	 */
	private	String		tableTitle		=	"";
	
	/**
	 * �R���X�g���N�^
	 */
	public TableInfo()
	{
	}
	
	/**
	 * �R���X�g���N�^
	 * @param tableName �e�[�u����
	 * @param tableTitle �e�[�u���̃^�C�g��
	 */
	public TableInfo(String tableName, String tableTitle)
	{
		this.setTableName(tableName);
		this.setTableTitle(tableTitle);
	}

	/**
	 * ������i�e�[�u���̃^�C�g���j�ɕϊ�����B
	 * @return ������i�e�[�u���̃^�C�g���j
	 */
	public String toString()
	{
		return tableTitle;
	}

	/**
	 * �e�[�u�������擾����B
	 * @return �e�[�u����
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * �e�[�u������ݒ肷��B
	 * @param tableName �e�[�u����
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * �e�[�u���̃^�C�g�����擾����B
	 * @return �e�[�u���̃^�C�g��
	 */
	public String getTableTitle()
	{
		return tableTitle;
	}

	/**
	 * �e�[�u���̃^�C�g����ݒ肷��B
	 * @param tableTitle �e�[�u���̃^�C�g��
	 */
	public void setTableTitle(String tableTitle)
	{
		this.tableTitle = tableTitle;
	}
	
	/**
	 * �e�[�u������ǂݍ��ށB
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
	 * �e�[�u������ǂݍ��ނr�p�k�����擾����B
	 * @return �e�[�u������ǂݍ��ނr�p�k��
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

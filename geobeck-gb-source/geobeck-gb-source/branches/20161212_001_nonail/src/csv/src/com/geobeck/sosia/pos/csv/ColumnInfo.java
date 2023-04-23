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
 * �e�[�u���̗���N���X
 * @author katagiri
 */
public class ColumnInfo
{
	/**
	 * ��
	 */
	private	String		columnName		=	"";
	/**
	 * ��̎��
	 */
	private	String		columnType		=	"";
	/**
	 * ��̏���ێ����邽�߂̃N���X
	 */
	private	Class		columnClass		=	null;
	/**
	 * ������̗񂩂ǂ���
	 */
	private Boolean		stringColumn	=	false;
	
	/**
	 * �R���X�g���N�^
	 */
	public ColumnInfo()
	{
	}

	/**
	 * ������i�񖼁j��Ԃ��B
	 * @return ������i�񖼁j
	 */
	public String toString()
	{
		return columnName;
	}

	/**
	 * �񖼂��擾����B
	 * @return ��
	 */
	public String getColumnName()
	{
		return columnName;
	}

	/**
	 * �񖼂�ݒ肷��B
	 * @param columnName ��
	 */
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	/**
	 * ��̎�ނ��擾����B
	 * @return ��̎��
	 */
	public String getColumnType()
	{
		return columnType;
	}

	/**
	 * ��̎�ނ�ݒ肷��B
	 * @param columnType ��̎��
	 */
	public void setColumnType(String columnType)
	{
		this.columnType = columnType;
		this.setColumnClass();
	}

	/**
	 * ��̏���ێ����邽�߂̃N���X���擾����B
	 * @return ��̏���ێ����邽�߂̃N���X
	 */
	public Class getColumnClass()
	{
		return columnClass;
	}

	/**
	 * ��̏���ێ����邽�߂̃N���X��ݒ肷��B
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
	 * ������̗񂩂ǂ������擾����B
	 * @return ������̗񂩂ǂ���
	 */
	public Boolean isStringColumn()
	{
		return	stringColumn;
	}
	
	/**
	 * ������̗񂩂ǂ�����ݒ肷��B
	 * @param stringColumn ������̗񂩂ǂ���
	 */
	private void setStringColumn(Boolean stringColumn)
	{
		this.stringColumn = stringColumn;
	}
}

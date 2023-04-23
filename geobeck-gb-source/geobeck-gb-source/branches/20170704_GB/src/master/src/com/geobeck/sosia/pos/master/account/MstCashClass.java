/*
 * MstCashClass.java
 *
 * Created on 2008/09/22, 20:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �������ڃf�[�^
 * @author saito
 */
public class MstCashClass extends ArrayList<MstCashMenu>
{
	/**
	 * �������ڂh�c
	 */
	protected Integer		cashClassId		=	null;
	/**
	 * �������ږ�
	 */
	protected String		cashClassName          =	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstCashClass()
	{
		super();
	}
        
	/**
	 * �R���X�g���N�^
	 * @param cashClassId �������ڂh�c
	 */
	public MstCashClass(Integer cashClassId)
	{
		this.setCashClassId(cashClassId);
	}
	
	/**
	 * ������ɕϊ�����B�i�������ږ��j
	 * @return �������ږ�
	 */
	public String toString()
	{
		return cashClassName;
	}

	/**
	 * �������ڂh�c���擾����B
	 * @return �������ڂh�c
	 */
	public Integer getCashClassId()
	{
		return cashClassId;
	}

	/**
	* �������ڂh�c���Z�b�g����B
	* @param cashClassId �������ڂh�c
	*/
	public void setCashClassId(Integer cashClassId)
	{
		this.cashClassId = cashClassId;
	}

	/**
	 * �������ږ����擾����B
	 * @return �������ږ�
	 */
	public String getCashClassName()
	{
		return cashClassName;
	}

	/**
	* �������ږ����Z�b�g����B
	* @param cashClassName �������ږ�
	*/
	public void setCashClassName(String cashClassName)
	{
                this.cashClassName	=	cashClassName;
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
                this.displaySeq	=	displaySeq;
	}
	
	/**
	 * �I�u�W�F�N�g���r����B
	 * @param o �I�u�W�F�N�g
	 */
	public boolean equals(Object o)
	{
		if(o instanceof MstCashClass)
		{
			MstCashClass	mkc	=	(MstCashClass)o;
			
			if(mkc.getCashClassId() == cashClassId &&
					mkc.getCashClassName().equals(cashClassName) &&
					mkc.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * �������ڃf�[�^����f�[�^���Z�b�g����B
	 * @param mkc �������ڃf�[�^
	 */
	public void setData(MstCashClass mkc)
	{
		this.setCashClassId(mkc.getCashClassId());
		this.setCashClassName(mkc.getCashClassName());
		this.setDisplaySeq(mkc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCashClassId(rs.getInt("cash_class_id"));
		this.setCashClassName(rs.getString("cash_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * �������ڂɃf�[�^��o�^����B
	 * @return true - ����
	 * @param lastSeq �\�����̍ő�l
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	
	/**
	 * �������ڂ���f�[�^���폜����B�i�_���폜�j
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * �������ڂɃf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCashClassId() == null || this.getCashClassId() < 1)	return	false;
		
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
		return	"select *\n"
			+	"from mst_cash_class\n"
                        +	"where delete_date is null\n"
			+	"and cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_cash_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_cash_class\n" +
				"(cash_class_id, cash_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(cash_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getCashClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_cash_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_cash_class\n" +
				"set\n" +
				"cash_class_name = " + SQLUtil.convertForSQL(this.getCashClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null\n" +
				"and cash_class_id != " +
				SQLUtil.convertForSQL(this.getCashClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null\n" +
				"and cash_class_id != " +
				SQLUtil.convertForSQL(this.getCashClassId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_cash_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n";
	}
	
	/**
	 * �����ڍ׃}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param con ConnectionWrapper
	 */
	public void loadCashMenu(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectCashMenuSQL());

		while(rs.next())
		{
			MstCashMenu	mkd	=	new	MstCashMenu();
			mkd.setData(rs);
			this.add(mkd);
		}

		rs.close();
	}
	
	/**
	 * �����ڍ׃}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �����ڍ׃}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectCashMenuSQL()
	{
		return	"select *\n" +
			"from mst_cash_menu\n" +
			"where delete_date is null\n" +
			"and cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n" +
			"order by display_seq, cash_menu_id\n";
	}
}

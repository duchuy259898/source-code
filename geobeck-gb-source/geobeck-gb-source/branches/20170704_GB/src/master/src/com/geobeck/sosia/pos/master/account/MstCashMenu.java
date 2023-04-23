/*
 * MstCashMenu.java
 *
 * Created on 2008/09/22, 20:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �����ڍ׃f�[�^
 * @author saito
 */
public class MstCashMenu
{
	/**
	 * ��������
	 */
	protected MstCashClass          cashClass		=	new MstCashClass();
	/**
	 * �����ڍׂh�c
	 */
	protected Integer		cashMenuId		=	null;
	/**
	 * �����ڍז�
	 */
	protected String		cashMenuName         =	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstCashMenu()
	{
	}
        
	/**
	 * �R���X�g���N�^
	 * @param cashMenuId �����ڍׂh�c
	 */
	public MstCashMenu(Integer cashMenuId)
	{
		this.setCashMenuId(cashMenuId);
	}

	/**
	 * �������ڂ��擾����B
	 * @return ��������
	 */
	public MstCashClass getCashClass()
	{
		return cashClass;
	}

	/**
	 * �������ڂ��Z�b�g����B
	 * @param cashClass ��������
	 */
	public void setCashClass(MstCashClass cashClass)
	{
		this.cashClass = cashClass;
	}
	
	/**
	 * ������ɕϊ�����B�i�����ڍז��j
	 * @return �����ڍז�
	 */
	public String toString()
	{
		return cashMenuName;
	}

	/**
	 * �����ڍׂh�c���擾����B
	 * @return �����ڍׂh�c
	 */
	public Integer getCashMenuId()
	{
		return cashMenuId;
	}

	/**
	* �����ڍׂh�c���Z�b�g����B
	* @param cashMenuId �����ڍׂh�c
	*/
	public void setCashMenuId(Integer cashMenuId)
	{
		this.cashMenuId = cashMenuId;
	}

	/**
	 * �����ڍז����擾����B
	 * @return �����ڍז�
	 */
	public String getCashMenuName()
	{
		return cashMenuName;
	}

	/**
	* �����ڍז����Z�b�g����B
	* @param cashMenuName �����ڍז�
	*/
	public void setCashMenuName(String cashMenuName)
	{
                this.cashMenuName = cashMenuName;
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
		if(o instanceof MstCashMenu)
		{
			MstCashMenu	mkd	=	(MstCashMenu)o;
			
			if(mkd.getCashClass().getCashClassId() == cashClass.getCashClassId() &&
					mkd.getCashMenuId() == cashMenuId &&
					mkd.getCashMenuName().equals(cashMenuName) &&
					mkd.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * �����ڍ׃f�[�^����f�[�^���Z�b�g����B
	 * @param mkd �����ڍ׃f�[�^
	 */
	public void setData(MstCashMenu mkd)
	{
                this.setCashClass(new MstCashClass(mkd.getCashClass().getCashClassId()));
		this.setCashMenuId(mkd.getCashMenuId());
		this.setCashMenuName(mkd.getCashMenuName());
		this.setDisplaySeq(mkd.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCashClass(new MstCashClass(rs.getInt("cash_class_id")));
		this.setCashMenuId(rs.getInt("cash_menu_id"));
		this.setCashMenuName(rs.getString("cash_menu_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * �����ڍׂɃf�[�^��o�^����B
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
			
			this.setMaxCashMenuID(con);
		}
		
		return	true;
	}
	
	/**
	 * �����ڍׂh�c�̍ő�l���擾����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	private void setMaxCashMenuID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxCashMenuIDSQL());
		
		if(rs.next())
		{
			this.setCashMenuId(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * �����ڍׂ���f�[�^���폜����B�i�_���폜�j
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
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
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
	 * �����ڍׂɃf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCashMenuId() == null)	return	false;
		
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
		return	"select mkc.cash_class_name, mkd.*\n" +
				"from mst_cash_menu mkd\n" +
				"left outer join mst_cash_class mkc\n" +
				"on mkc.cash_class_id = mkd.cash_class_id\n" +
				"and mkc.delete_date is null\n" +
                                "where mkd.delete_date is null\n" +
				"and mkd.cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_cash_menu\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_cash_menu\n" +
				"(cash_class_id, cash_menu_id, cash_menu_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + ",\n" +
				"(select coalesce(max(cash_menu_id), 0) + 1\n" +
				"from mst_cash_menu),\n" +
				SQLUtil.convertForSQL(this.getCashMenuName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_cash_menu\n" +
				"set\n" +
				"cash_class_id = " + SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + ",\n" +
				"cash_menu_name = " + SQLUtil.convertForSQL(this.getCashMenuName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "\n" +
				"and cash_menu_id != " +
				SQLUtil.convertForSQL(this.getCashMenuId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "\n" +
				"and cash_menu_id != " +
				SQLUtil.convertForSQL(this.getCashMenuId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String getDeleteSQL()
	{
		return	"update mst_cash_menu\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()) + "\n";
	}
	
	/**
	 * �����ڍׂh�c�̍ő�l���擾����SQL�����擾����B
	 * @return �����ڍׂh�c�̍ő�l���擾����SQL��
	 */
	private static String getMaxCashMenuIDSQL()
	{
		return	"select max(cash_menu_id) as max_id\n" +
				"from mst_cash_menu\n";
	}
	
}
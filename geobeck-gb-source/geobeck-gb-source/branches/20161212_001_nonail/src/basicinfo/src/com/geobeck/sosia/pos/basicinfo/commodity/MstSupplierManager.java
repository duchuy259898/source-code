/*
 * MstSupplierManager.java
 *
 * Created on 2007/04/02, 17:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.commodity;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.master.commodity.*;

/**
 * �d����o�^�����N���X
 * @author katagiri
 */
public class MstSupplierManager extends ArrayList<MstSupplier>
{
	/**
	 * �R���X�g���N�^
	 */
	public MstSupplierManager()
	{
		currentSupplier	=	new MstSupplier();
	}
	
	/**
	 * �������̎d����
	 */
	private	MstSupplier		currentSupplier		=	null;

	/**
	 * �������̎d������擾����B
	 * @return �������̎d����
	 */
	public MstSupplier getCurrentSupplier()
	{
		return currentSupplier;
	}

	/**
	 * �������̎d�����ݒ肷��B
	 * @param currentSupplier �������̎d����
	 */
	public void setCurrentSupplier(MstSupplier currentSupplier)
	{
		this.currentSupplier.setData(currentSupplier);
	}
	
	/**
	 * �d����f�[�^��ǂݍ��ށB
	 * @return true - �����Afalse - ���s
	 */
	public boolean load()
	{
		boolean		result	=	false;
		
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());
			
			while(rs.next())
			{
				MstSupplier		temp	=	new MstSupplier();
				
				temp.setData(rs);
				
				this.add(temp);
			}
			
			rs.close();
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * �d����f�[�^��ǂݍ��ނ��߂̂r�p�k�����擾����B
	 * @return �d����f�[�^��ǂݍ��ނ��߂̂r�p�k��
	 */
	private String getLoadSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" +
				"where delete_date is null\n" +
				"order by supplier_no\n";
	}
	
	/**
	 * �d����No.�̏d���`�F�b�N���s���B
	 * @return true - OK�Afalse - NG
	 */
	public boolean checkSupplierNo()
	{
		try
		{
			return	currentSupplier.checkSupplierNo(SystemInfo.getConnection());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
	}

	/**
	 * �������̎d����̃f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 */
	public boolean register(ConnectionWrapper con) throws SQLException
	{
		try
		{
			if(currentSupplier.regist(con))
			{
				return true;
			}
			
			return false;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		}
	}

	/**
	 * �������̎d����̃f�[�^��o�^����B
	 * @return true - �����Afalse - ���s
	 */
	public boolean regist()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
				if (register(con))
				{
					con.commit();
					result	=	true;
				}
				else
				{
					con.rollback();
				}
			}
			catch(SQLException e)
			{
				con.rollback();
				throw e;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * �������̎d����̃f�[�^���폜����B
	 * @return true - �����Afalse - ���s
	 */
	public boolean delete()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
				if(currentSupplier.delete(con))
				{
					con.commit();
					result	=	true;
				}
				else
				{
					con.rollback();
				}
			}
			catch(SQLException e)
			{
				con.rollback();
				throw e;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}

	/**
	 * �d����No�ɑΉ�����d�����Ԃ��B
	 * @param supplierNo �d����No
	 * @return �d����No�ɑΉ�����d����
	 */
	public MstSupplier lookupSupplier(Integer supplierNo)
	{
		for (MstSupplier s : this)
		{
			if (s.getSupplierNo().equals(supplierNo))
			{
				return s;
			}
		}
		
		return null;
	}
}

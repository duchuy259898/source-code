/*
 * MstPaymentClass.java
 *
 * Created on 2006/04/25, 17:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �x���敪�}�X�^�f�[�^
 * @author katagiri
 */
public class MstPaymentClass extends ArrayList<MstPaymentMethod>
{
	/**
	 * �x���敪�h�c
	 */
	protected	Integer		paymentClassID		=	null;
	/**
	 * �x���敪��
	 */
	protected	String		paymentClassName	=	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstPaymentClass()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�x���敪���j
	 * @return �x���敪��
	 */
	public String toString()
	{
		if(paymentClassName == null)	return	"";
		return	this.getPaymentClassName();
	}

	/**
	 * �x���敪�h�c���擾����B
	 * @return �x���敪�h�c
	 */
	public Integer getPaymentClassID()
	{
		return paymentClassID;
	}

	/**
	 * �x���敪�h�c���Z�b�g����B
	 * @param paymentClassID �x���敪�h�c
	 */
	public void setPaymentClassID(Integer paymentClassID)
	{
		this.paymentClassID = paymentClassID;
	}

	/**
	 * �x���敪�����擾����B
	 * @return �x���敪��
	 */
	public String getPaymentClassName()
	{
		return paymentClassName;
	}

	/**
	 * �x���敪�����Z�b�g����B
	 * @param paymentClassName �x���敪��
	 */
	public void setPaymentClassName(String paymentClassName)
	{
		this.paymentClassName = paymentClassName;
	}
	
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setPaymentClassID(null);
		this.setPaymentClassName("");
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setPaymentClassID(rs.getInt("payment_class_id"));
		this.setPaymentClassName(rs.getString("payment_class_name"));
	}
	
	
	/**
	 * �x�����敪�}�X�^�Ƀf�[�^��o�^����B
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
	 * �x�����敪�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �x�����敪�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getPaymentClassID() == null || this.getPaymentClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * ���i�敪�}�X�^�擾�p�̂r�p�k�����擾����B
	 * @return ���i�敪�}�X�^�擾�p�̂r�p�k��
	 * @param isIncludeCash true - �������܂�
	 */
	public static String getSelectAllSQL(boolean isIncludeCash)
	{
		return	"select		*\n"
			+	"from		mst_payment_class\n"
			+	"where		delete_date is null\n"
			+	(isIncludeCash ? "" : "and	payment_class_id != 1")
			+	"order by	payment_class_id\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_payment_class\n"
			+	"where	payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_payment_class\n"
			+	"(payment_class_id, payment_class_name,\n"
			+	"insert_date, update_date, delete_date)\n"
			+	"select\n"
			+	"coalesce(max(payment_class_id), 0) + 1,\n"
			+	SQLUtil.convertForSQL(this.getPaymentClassName()) + ",\n"
			+	"current_timestamp, current_timestamp, null\n"
			+	"from mst_payment_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_payment_class\n"
			+	"set\n"
			+	"payment_class_name = " + SQLUtil.convertForSQL(this.getPaymentClassName()) + ",\n"
			+	"update_date = current_timestamp\n"
			+	"where	payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_payment_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + "\n";
	}
}

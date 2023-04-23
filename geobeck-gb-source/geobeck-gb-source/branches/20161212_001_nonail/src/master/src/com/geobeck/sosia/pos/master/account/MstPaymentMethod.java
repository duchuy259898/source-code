/*
 * MstPaymentMethod.java
 *
 * Created on 2006/04/25, 17:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �x�����@�}�X�^�f�[�^
 * @author katagiri
 */
public class MstPaymentMethod
{
	/**
	 * �x���敪
	 */
	protected	MstPaymentClass		paymentClass		=	new MstPaymentClass();
	/**
	 * �x�����@�h�c
	 */
	protected	Integer				paymentMethodID		=	null;
	/**
	 * �x�����@��
	 */
	protected	String				paymentMethodName	=	"";
	/**
	 * ���ߓ�
	 */
	protected	Integer				cutoffDay			=	null;
	/**
	 * ������
	 */
	protected	Integer				receiptClass		=	null;
	/**
	 * ������
	 */
	protected	Integer				receiptDay			=	null;
	/**
	 * �v���y�C�h
	 */
	private	Integer				prepaid			=	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstPaymentMethod()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�x�����@���j
	 * @return �x�����@��
	 */
	public String toString()
	{
		if(paymentMethodName == null)	return	"";
		return	this.getPaymentMethodName();
	}

	/**
	 * �x���敪���擾����B
	 * @return �x���敪
	 */
	public MstPaymentClass getPaymentClass()
	{
		return paymentClass;
	}

	/**
	 * �x���敪���Z�b�g����B
	 * @param paymentClass �x���敪
	 */
	public void setPaymentClass(MstPaymentClass paymentClass)
	{
		this.paymentClass = paymentClass;
	}

	/**
	 * �x���敪�h�c���擾����B
	 * @return �x���敪�h�c
	 */
	public Integer getPaymentClassID()
	{
		if(paymentClass == null)	return	null;
		else	return paymentClass.getPaymentClassID();
	}

	/**
	 * �x���敪�h�c���Z�b�g����B
	 * @param paymentClassID �x���敪�h�c
	 */
	public void setPaymentClassID(Integer paymentClassID)
	{
		if(paymentClass == null)	paymentClass	=	new	MstPaymentClass();
		this.paymentClass.setPaymentClassID(paymentClassID);
	}

	/**
	 * �x�����@�h�c���擾����B
	 * @return �x�����@�h�c
	 */
	public Integer getPaymentMethodID()
	{
		return paymentMethodID;
	}

	/**
	 * �x�����@�h�c���Z�b�g����B
	 * @param paymentMethodID �x�����@�h�c
	 */
	public void setPaymentMethodID(Integer paymentMethodID)
	{
		this.paymentMethodID = paymentMethodID;
	}

	/**
	 * �x�����@�����擾����B
	 * @return �x�����@��
	 */
	public String getPaymentMethodName()
	{
		return paymentMethodName;
	}

	/**
	 * �x�����@�����Z�b�g����B
	 * @param paymentMethodName �x�����@��
	 */
	public void setPaymentMethodName(String paymentMethodName)
	{
		this.paymentMethodName = paymentMethodName;
	}

	/**
	 * ���ߓ����擾����B
	 * @return ���ߓ�
	 */
	public Integer getCutoffDay()
	{
		return cutoffDay;
	}

	/**
	 * ���ߓ����Z�b�g����B
	 * @param cutoffDay ���ߓ�
	 */
	public void setCutoffDay(Integer cutoffDay)
	{
		this.cutoffDay = cutoffDay;
	}

	/**
	 * ���������擾����B
	 * @return ������
	 */
	public Integer getReceiptClass()
	{
		return receiptClass;
	}

	/**
	 * ���������Z�b�g����B
	 * @param receiptClass ������
	 */
	public void setReceiptClass(Integer receiptClass)
	{
		this.receiptClass = receiptClass;
	}

	/**
	 * �����������擾����B
	 * @return ��������
	 */
	public String getReceiptClassName()
	{
		switch(receiptClass)
		{
			case 1:
				return	"����";
			case 2:
				return	"����";
			case 3:
				return	"���X��";
			default:
				return	"";
		}
	}

	/**
	 * ���������擾����B
	 * @return ������
	 */
	public Integer getReceiptDay()
	{
		return receiptDay;
	}

	/**
	 * ���������Z�b�g����B
	 * @param receiptDay ������
	 */
	public void setReceiptDay(Integer receiptDay)
	{
		this.receiptDay = receiptDay;
	}

        /**
         * @return the prepaid
         */
        public Integer getPrepaid() {
            return prepaid;
        }

        /**
         * @param prepaid the prepaid to set
         */
        public void setPrepaid(Integer prepaid) {
            this.prepaid = prepaid;
        }
        
        public Boolean isPrepaid() {
            return prepaid == 1;
        }
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setPaymentClass(null);
		this.setPaymentMethodID(null);
		this.setPaymentMethodName("");
		this.setCutoffDay(null);
		this.setReceiptClass(null);
		this.setReceiptDay(null);
		this.setPrepaid(null);
	}
	
	/**
	 * ���i�}�X�^����A�ݒ肳��Ă��鏤�iID�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			this.setData(rs);
		}
		
		return	true;
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setPaymentClassID(rs.getInt("payment_class_id"));
		this.setPaymentMethodID(rs.getInt("payment_method_id"));
		this.setPaymentMethodName(rs.getString("payment_method_name"));
		this.setCutoffDay(rs.getInt("cutoff_day"));
		this.setReceiptClass(rs.getInt("receipt_class"));
		this.setReceiptDay(rs.getInt("receipt_day"));
		this.setPrepaid(rs.getInt("prepaid"));
	}
	
	
	/**
	 * ���i�}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(this.getPaymentClassID() == null)	return	false;
		
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
	 * ���i�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * ���i�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getPaymentMethodID() == null || this.getPaymentMethodID().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * ���i�}�X�^�擾�p�̂r�p�k�����擾����B
	 * @return ���i�}�X�^�擾�p�̂r�p�k��
	 * @param productClassID ���i�敪�h�c
	 */
	public static String getSelectAllSQL(Integer productClassID)
	{
		return	"select		*\n" +
				"from		mst_payment_method\n" +
				"where		delete_date is null\n" +
				"		and	payment_class_id = " +
				SQLUtil.convertForSQL(productClassID) + "\n" +
				"order by	payment_method_id\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_payment_method\n" +
				"where payment_method_id = " + SQLUtil.convertForSQL(this.getPaymentMethodID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_payment_method\n" +
				"(payment_class_id, payment_method_id, payment_method_name,\n" +
				"cutoff_day, receipt_class, receipt_day,prepaid,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getPaymentClassID()) + ",\n" +
				"coalesce(max(payment_method_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getPaymentMethodName()) + ",\n" +
				SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
				SQLUtil.convertForSQL(this.getReceiptClass()) + ",\n" +
				SQLUtil.convertForSQL(this.getReceiptDay()) + ",\n" +
				SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_payment_method\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_payment_method\n" +
				"set\n" +
				"payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + ",\n" +
				"payment_method_name = " + SQLUtil.convertForSQL(this.getPaymentMethodName()) + ",\n" +
				"cutoff_day = " + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
				"receipt_class = " + SQLUtil.convertForSQL(this.getReceiptClass()) + ",\n" +
				"receipt_day = " + SQLUtil.convertForSQL(this.getReceiptDay()) + ",\n" +
				"prepaid = " + SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where payment_method_id = " + SQLUtil.convertForSQL(this.getPaymentMethodID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_payment_method\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where payment_method_id = " + SQLUtil.convertForSQL(this.getPaymentMethodID()) + "\n";
	}

}

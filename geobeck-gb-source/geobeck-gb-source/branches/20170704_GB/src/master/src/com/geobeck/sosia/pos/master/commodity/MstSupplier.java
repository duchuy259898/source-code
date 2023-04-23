/*
 * MstSupplier.java
 *
 * Created on 2007/04/02, 16:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;


import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import javax.swing.JComboBox;
import javax.swing.JComboBox;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;


/**
 *
 * @author katagiri
 */
public class MstSupplier
{
	/**
	 * �X�֔ԍ��̕������̍ő�l
	 */
	public static int		POSTAL_CODE_MAX			=	7;
	/**
	 * �d�b�ԍ��̕������̍ő�l
	 */
	public static int		PHONE_NUMBER_MAX		=	15;
	/**
	 * �e�`�w�ԍ��̕������̍ő�l
	 */
	public static int		FAX_NUMBER_MAX			=	15;
	/**
	 * ���[���A�h���X�̕������̍ő�l
	 */
	public static int		MAIL_ADDRESS_MAX		=	64;
	
	/**
	 * �d����ID
	 */
	protected	Integer		supplierID				=	null;
	/**
	 * �d����No.
	 */
	protected	Integer		supplierNo				=	null;
	/**
	 * �d���於
	 */
	protected	String		supplierName			=	"";
	/**
	 * �d���敪
	 */
	protected	Integer		purchaseDivision		=	0;
	/**
	 * �X�֔ԍ�
	 */
	private	String			postalCode			=	"";
	/**
	 * �Z��
	 */
	private	String[]		address				=	{"", "", "", ""};
	/**
	 * �d�b�ԍ�
	 */
	private	String			phoneNumber			=	"";
	/**
	 * �e�`�w�ԍ�
	 */
	private	String			faxNumber			=	"";
	/**
	 * ���[���A�h���X
	 */
	private	String			mailAddress			=	"";
	
	/** �x���T�C�g ���ߓ� */
	private Integer			cutoffDay			=	null;
	/** �x���T�C�g �x�����R�[�h */
	private Integer			paymentClass		=	null;
	/** �x���T�C�g�x���� */
	private Integer			paymentDay			=	null;
	/**
	 * �d����S����
	 */
	protected	String		supplierStaff			=	"";
	
	/** Creates a new instance of MstSupplier */
	public MstSupplier()
	{
	}

	/**
	 * �d����ID���擾����B
	 * @return �d����ID
	 */
	public Integer getSupplierID()
	{
		return supplierID;
	}

	/**
	 * �d����ID���Z�b�g����B
	 * @param supplierID �d����ID
	 */
	public void setSupplierID(Integer supplierID)
	{
		this.supplierID = supplierID;
	}

	/**
	 * �d����No.���擾����B
	 * @return �d����No.
	 */
	public Integer getSupplierNo()
	{
		return supplierNo;
	}

	/**
	 * �d����No.���Z�b�g����B
	 * @param supplierNo �d����No.
	 */
	public void setSupplierNo(Integer supplierNo)
	{
		this.supplierNo = supplierNo;
	}

	/**
	 * �d����No.���擾����B
	 * @return �d����No.
	 */
	public String getSupplierNoString()
	{
		if(supplierNo != null)
		{
			return	supplierNo.toString();
		}
		
		return "";
	}

	/**
	 * �d���於���擾����B
	 * @return �d���於
	 */
	public String getSupplierName()
	{
		return supplierName;
	}

	/**
	 * �d���於���Z�b�g����B
	 * @param supplierName �d���於
	 */
	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public Integer getPurchaseDivision()
	{
		return purchaseDivision;
	}

	public void setPurchaseDivision(Integer purchaseDivision)
	{
		this.purchaseDivision = purchaseDivision;
	}

	public String getPurchaseDivisionName()
	{
		return	MstSupplier.getPurchaseDivisionName(this.purchaseDivision);
	}

	public static String getPurchaseDivisionName(Integer purchaseDivision)
	{
		switch(purchaseDivision)
		{
			case 0:
				return	"�|�d��";
			case 1:
				return	"�����d��";
		}
		
		return	"";
	}

	/**
	 * �X�֔ԍ����擾����B
	 * @return �X�֔ԍ�
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * �X�֔ԍ����擾����B
	 * @return �X�֔ԍ�
	 */
	public String getPostalCodeWithHyphen()
	{
		if(postalCode.equals(""))
		{
			return	"";
		}
		else
		{
			return postalCode.substring(0,3) + "-" + postalCode.substring(3);
		}
	}

	/**
	 * �X�֔ԍ����Z�b�g����B
	 * @param postalCode �X�֔ԍ�
	 */
	public void setPostalCode(String postalCode)
	{
		if (postalCode == null) {
                    this.postalCode = "       ";
                } else if (postalCode.length() <= MstSupplier.POSTAL_CODE_MAX) {
                    this.postalCode = (postalCode + "       ").substring(0, 7);
		} else {
                    this.postalCode = postalCode.substring(0, MstSupplier.POSTAL_CODE_MAX);
		}
	}

	/**
	 * �Z�����擾����B
	 * @return �Z��
	 */
	public String[] getAddress()
	{
		return address;
	}

	/**
	 * �Z�����擾����B
	 * @param index �C���f�b�N�X
	 * @return �Z��
	 */
	public String getAddress(int index)
	{
		return address[index];
	}

	/**
	 * �Z��
	 * @param address �Z��
	 */
	public void setAddress(String[] address)
	{
		this.address = address;
	}

	/**
	 * �Z�����Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param address �Z��
	 */
	public void setAddress(int index, String address)
	{
		this.address[index] = address;
	}
	
	/**
	 * �Z�����擾����B
	 * @return �Z��
	 */
	public String getFullAddress()
	{
		return	(address[0] == null ? "" : address[0]) +
				(address[1] == null ? "" : address[1]) +
				(address[2] == null ? "" : address[2]) +
				(address[3] == null ? "" : address[3]);
	}

	/**
	 * �d�b�ԍ����擾����B
	 * @return �d�b�ԍ�
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * �d�b�ԍ����Z�b�g����B
	 * @param phoneNumber �d�b�ԍ�
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		if(phoneNumber == null || phoneNumber.length() <= MstSupplier.PHONE_NUMBER_MAX)
		{
			this.phoneNumber	=	phoneNumber;
		}
		else
		{
			this.phoneNumber	=	phoneNumber.substring(0, MstSupplier.PHONE_NUMBER_MAX);
		}
	}

	/**
	 * �e�`�w�ԍ����擾����B
	 * @return �e�`�w�ԍ�
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * �e�`�w�ԍ����Z�b�g����B
	 * @param faxNumber �e�`�w�ԍ�
	 */
	public void setFaxNumber(String faxNumber)
	{
		if(faxNumber == null || faxNumber.length() <= MstSupplier.FAX_NUMBER_MAX)
		{
			this.faxNumber	=	faxNumber;
		}
		else
		{
			this.faxNumber	=	faxNumber.substring(0, MstSupplier.FAX_NUMBER_MAX);
		}
	}

	/**
	 * ���[���A�h���X���擾����B
	 * @return ���[���A�h���X
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * ���[���A�h���X���Z�b�g����B
	 * @param mailAddress ���[���A�h���X
	 */
	public void setMailAddress(String mailAddress)
	{
		if(mailAddress == null || mailAddress.length() <= MstSupplier.MAIL_ADDRESS_MAX)
		{
			this.mailAddress	=	mailAddress;
		}
		else
		{
			this.mailAddress	=	mailAddress.substring(0, MstSupplier.MAIL_ADDRESS_MAX);
		}
	}
	
	/**
	 * MstSupplier����f�[�^���Z�b�g����B
	 * @param mg MstSupplier
	 */
	public void setData(MstSupplier mg)
	{
		this.setSupplierID(mg.getSupplierID());
		this.setSupplierNo(mg.getSupplierNo());
		this.setSupplierName(mg.getSupplierName());
		this.setPurchaseDivision(mg.getPurchaseDivision());
		this.setPostalCode(mg.getPostalCode());
		this.setAddress(mg.getAddress());
		this.setPhoneNumber(mg.getPhoneNumber());
		this.setFaxNumber(mg.getFaxNumber());
		this.setMailAddress(mg.getMailAddress());
		this.setCutoffDay(mg.getCutoffDay());
		this.setPaymentClass(mg.getPaymentClass());
		this.setPaymentDay(mg.getPaymentDay());
		this.setSupplierStaff(mg.getSupplierStaff());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException ��O
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setSupplierID(rs.getInt("supplier_id"));
		this.setSupplierNo(rs.getInt("supplier_no"));
		this.setSupplierName(rs.getString("supplier_name"));
		this.setPurchaseDivision(rs.getInt("purchase_division"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setFaxNumber(rs.getString("fax_number"));
		this.setMailAddress(rs.getString("mail_address"));
		this.setCutoffDay(rs.getInt("cutoff_day"));
		this.setPaymentClass(rs.getInt("payment_class"));
		this.setPaymentDay(rs.getInt("payment_day"));
		this.setSupplierStaff(rs.getString("supplier_staff"));
	}
	
	/**
	 * ������i�d���於�j���擾����B
	 * @return ������i�d���於�j
	 */
	public String toString()
	{
		return	this.getSupplierName();
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof MstSupplier)
		{
			if(((MstSupplier)obj).getSupplierID() == this.getSupplierID())
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * �d����}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSupplierID() == null || this.getSupplierID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * �d����}�X�^�ɓ���d����No�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws SQLException DB�A�N�Z�X���s��
	 * @return true - ���݂���
	 */
	public boolean existsSameSupplierNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByNoSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * �f�[�^�x�[�X����f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			this.setData(rs);
		}
		
		rs.close();
		
		return	true;
	}
	
	/**
	 * �f�[�^���擾����r�p�k�����擾����B
	 * @return �f�[�^���擾����r�p�k��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" +
				"where supplier_id = " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n" +
				"and delete_date is null\n";
	}
	
	/**
	 * �f�[�^���擾����r�p�k�����擾����B
	 * @return �f�[�^���擾����r�p�k��
         *200809
	 */
	private static String getSelectAllSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" ;
	}
	
	/**
	 * �f�[�^�x�[�X����f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	public boolean loadByNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByNoSQL());
		
		if(rs.next())
		{
			this.setData(rs);
		}
		
		rs.close();
		
		return	true;
	}
	
	/**
	 * �f�[�^���擾����r�p�k�����擾����B
	 * @return �f�[�^���擾����r�p�k��
	 */
	private String getSelectByNoSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" +
				"where supplier_no = " + SQLUtil.convertForSQL(this.getSupplierNo()) + "\n" +
				"and delete_date is null\n";
	}
	
	
	/**
	 * �d����}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			return	(con.executeUpdate(this.getUpdateSQL()) == 1);
		}
		else
		{
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
			
			this.setSupplierID(getMaxSupplierID(con));
			
			return	true;
		}
	}
	
	
	/**
	 * �d����}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			if(con.executeUpdate(this.getDeleteSQL()) == 1)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_supplier\n" +
				"select\n" +
				"coalesce(max(supplier_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getSupplierNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getSupplierName()) + ",\n" +
				SQLUtil.convertForSQL(this.getPurchaseDivision()) + ",\n" +
				SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"current_timestamp, current_timestamp, null,\n" +
				SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentClass()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentDay()) + ",\n" +
				SQLUtil.convertForSQL(this.getSupplierStaff()) + "\n" +
				"from mst_supplier\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_supplier\n" +
				"set\n" +
				"supplier_no = " + SQLUtil.convertForSQL(this.getSupplierNo()) + ",\n" +
				"supplier_name = " + SQLUtil.convertForSQL(this.getSupplierName()) + ",\n" +
				"purchase_division = " + SQLUtil.convertForSQL(this.getPurchaseDivision()) + ",\n" +
				"postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				"address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				"address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				"address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				"address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				"phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				"fax_number = " + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				"mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"update_date = current_timestamp,\n" +
				"cutoff_day = " + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
				"payment_class = " + SQLUtil.convertForSQL(this.getPaymentClass()) + ",\n" +
				"payment_day = " + SQLUtil.convertForSQL(this.getPaymentDay()) + ",\n" +
				"supplier_staff = " + SQLUtil.convertForSQL(this.getSupplierStaff()) + "\n" +
				"where	supplier_id = " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_supplier\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where	supplier_id = " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n";
	}
	
	/**
	 * �d����ID�̍ő�l���擾����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public static Integer getMaxSupplierID(ConnectionWrapper con) throws SQLException
	{
		Integer		result	=	null;
		
		if(con == null)	return	result;
		
		ResultSetWrapper	rs	=	con.executeQuery(getMaxSupplierIDSQL());

		if(rs.next())
		{
			result	=	rs.getInt("max_supplier_id");
		}
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * 
	 * @return 
	 */
	private static String getMaxSupplierIDSQL()
	{
		return	"select max(supplier_id) as max_supplier_id\n" +
				"from mst_supplier";
	}
	
	
	public boolean checkSupplierNo(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getCheckSupplierNoSQL());
		
		if(rs.next())
		{
			result	=	(rs.getInt("cnt") == 0);
		}
		else
		{
			result	=	true;
		}
		
		rs.close();
		
		return	result;
	}
	
	public String getCheckSupplierNoSQL()
	{
		return	"select count(*) as cnt\n" +
				"from mst_supplier\n" +
				"where delete_date is null\n" +
				"and supplier_no = " + SQLUtil.convertForSQL(this.getSupplierNo()) + "\n" +
				(this.getSupplierID() != null && 0 < this.getSupplierID() ?
					"and supplier_id != " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n" :
					"");
	}
	
	public Integer getCutoffDay()
	{
		return this.cutoffDay;
	}
	
	public void setCutoffDay(Integer cutoffDay)
	{
		this.cutoffDay = cutoffDay;
	}
	
	public Integer getPaymentClass()
	{
		return this.paymentClass;
	}
	
	public void setPaymentClass(Integer paymentClass)
	{
		this.paymentClass = paymentClass;
	}
	
	public Integer getPaymentDay()
	{
		return this.paymentDay;
	}
	
	public void setPaymentDay(Integer paymentDay)
	{
		this.paymentDay = paymentDay;
	}
        
        public String getSupplierStaff() {
                return supplierStaff;
        }

        public void setSupplierStaff(String supplierStaff) {
                this.supplierStaff = supplierStaff;
        }
        
        /**
	 * 200809
	 * @param cb 
	 */
	public static void addSupplierDataToJComboBox(ConnectionWrapper con, JComboBox cb) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(MstSupplier.getSelectAllSQL());
		
		while(rs.next())
		{
			MstSupplier	ms	=	new MstSupplier();
			ms.setData(rs);
			
			cb.addItem(ms);
		}
		
		rs.close();
	}

}

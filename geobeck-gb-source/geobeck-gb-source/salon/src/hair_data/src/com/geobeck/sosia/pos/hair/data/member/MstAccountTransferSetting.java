/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author lvtu
 */
public class MstAccountTransferSetting {

	/**
	 * �ϑ��҃R�[�h
	 */
	private	String		consignorCode		=	null;
	/**
	 * �敪
	 */
	private	String		division		=	null;
	/**
	 * �ϑ��Җ�
	 */
	private	String		consignorName		=	null;
	/**
	 * �U�֓�
	 */
	private	String		transferDate		=	null;
        /**
	 * �x�����@
	 */
	private	Integer		paymentMethod		=	null;

	/** Creates a new instance of MstAccountTransferSetting */
	public MstAccountTransferSetting()
	{
	}
        /**
         * �ϑ��҃R�[�h
         * @return consignorCode
         */
         public String getConsignorCode() {
            return consignorCode;
        }

         /**
          * �ϑ��҃R�[�h
          * @param consignorCode 
          */
        public void setConsignorCode(String consignorCode) {
            this.consignorCode = consignorCode;
        }

        /**
         * �敪
         * @return division
         */
        public String getDivision() {
            return division;
        }

        /**
         * �敪
         * @param division 
         */
        public void setDivision(String division) {
            this.division = division;
        }

        /**
         * �ϑ��Җ�
         * @return consignorName
         */
        public String getConsignorName() {
            return consignorName;
        }

        /**
         * �ϑ��Җ�
         * @param consignorName 
         */
        public void setConsignorName(String consignorName) {
            this.consignorName = consignorName;
        }

        /**
         * �U�֓�
         * @return transferDate
         */
        public String getTransferDate() {
            return transferDate;
        }

        /**
         * �U�֓�
         * @param transferDate 
         */
        public void setTransferDate(String transferDate) {
            this.transferDate = transferDate;
        }
        
        public Integer getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(Integer paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
	
	/**
	 * ������ɕϊ�����B�i�ϑ��Җ��j
	 * @return �x�b�h��
	 */
        @Override
	public String toString()
	{
		return	this.getConsignorName();
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
                this.setConsignorCode(rs.getString("consignor_code"));
                this.setConsignorName(rs.getString("consignor_name"));
                this.setDivision(rs.getString("division"));
                this.setTransferDate(rs.getString("transfer_date"));
                this.setPaymentMethod(rs.getInt("payment_method"));
	}

	/**
	 * �����U�֊�{���̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
        * @return 
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if (con == null) return false;
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                
		if (rs.next()) {
                    this.setData(rs);
                    return true;
                }
                
                return false;
	}
        
        /**
	 * ��{���Ƀf�[�^��o�^����B
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
	 * ��{���Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if (this.getConsignorCode()== null)	return	false;
		
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
				"from mst_account_transfer_setting\n" +
				"limit 1\n";
	}
        
        /**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_account_transfer_setting\n" +
				"(consignor_code, division, consignor_name, transfer_date, payment_method,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getConsignorCode()) + ",\n" +
				SQLUtil.convertForSQL(this.getDivision()) + ",\n" +
				SQLUtil.convertForSQL(this.getConsignorName()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTransferDate()) + ",\n" +
                                SQLUtil.convertForSQL(this.getPaymentMethod()) + ",\n" +
                                "current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_account_transfer_setting\n" +
				"set\n" +
				"division = " + SQLUtil.convertForSQL(this.getDivision()) + ",\n" +
				"consignor_name = " + SQLUtil.convertForSQL(this.getConsignorName()) + ",\n" +
                                "transfer_date = " + SQLUtil.convertForSQL(this.getTransferDate()) + ",\n" +
                                "payment_method = " + SQLUtil.convertForSQL(this.getPaymentMethod()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	consignor_code = " + SQLUtil.convertForSQL(this.getConsignorCode());
	}
}

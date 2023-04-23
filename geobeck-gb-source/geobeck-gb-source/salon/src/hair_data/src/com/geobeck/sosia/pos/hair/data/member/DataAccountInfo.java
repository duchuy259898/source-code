/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *�������
 * @author lvtu
 */
public class DataAccountInfo {

        
	/**
	 * �ڋq
	 */
	private	MstCustomer	customer		=	null;
	/**
	 * �����U�֗p�ڋqNO
	 */
	private	String		accountCustomerNo	=	null;
	/**
	 * ��s�R�[�h
	 */
	private	String		bankCode		=	null;
	/**
	 * ��s��
	 */
	private	String		bankName		=	null;
	/**
	 * �x�X�R�[�h
	 */
	private	String		branchCode		=	null;
	/**
	 * �x�X��
	 */
	private	String		branchName		=	null;
	/**
	 * �������
	 */
	private	String		accountType		=	null;
	/**
	 * �����ԍ�
	 */
	private	String		accountNumber		=	null;
	/**
	 * �������`
	 */
	private	String		accountName		=	null;
        /**
         * �a�����
         */
        public static enum AccountType {

            NORMAL(1, "����"),
            POPULAR(2, "����");

            private final Integer value;
            private final String text;

            /**
             * �R�[�h�ɂ�錟����e�Ղɂ��邽�߂̐����R�[�h�Ƃ���ɑΉ�����e�L�X�g�Ԃ̃}�b�s���O�B
             */
            private static Map<Integer, AccountType> valueToTextMapping;

            private AccountType(Integer value, String text){
                this.value = value;
                this.text = text;
            }

            public static AccountType getAccountType(Integer i){
                if(valueToTextMapping == null){
                    initMapping();
                }
                return valueToTextMapping.get(i);
            }

            private static void initMapping(){
                valueToTextMapping = new HashMap<Integer, AccountType>();
                for(AccountType s : values()){
                    valueToTextMapping.put(s.value, s);
                }
            }

            public Integer getValue(){
                return value;
            }

            public String getText(){
                return text;
            }
        }

	/** Creates a new instance of DataAccountInfo */
	public DataAccountInfo()
	{
	}
        
        public DataAccountInfo(MstCustomer customer)
	{
            this.setCustomer(customer);
	}
	
	/**
	 * ������ɕϊ�����B�i�x�b�h���j
	 * @return �x�b�h��
	 */
        @Override
	public String toString()
	{
		return	this.getCustomer().getCustomerNo();
	}

        /**
	 * �x�b�ڋq���Z�b�g����B
	 * @return �x�b�hcustomer
	 */
        public MstCustomer getCustomer() {
            return customer;
        }

        /**
	 * �x�b�ڋq���Z�b�g����B
	 * @param customer �x�b�ڋq
	 */
        public void setCustomer(MstCustomer customer) {
            this.customer = customer;
        }

        /**
	 * �x�b�����U�֗p�ڋqNO���Z�b�g����B
	 * @return accountCustomerNo �x�b�h�����U�֗p�ڋqNO
	 */
         public String getAccountCustomerNo() {
             return accountCustomerNo;
         }

        /**
	 * �x�b�����U�֗p�ڋqNO���Z�b�g����B
	 * @param accountCustomerNo �x�b�����U�֗p�ڋqNO
	 */
         public void setAccountCustomerNo(String accountCustomerNo) {
             this.accountCustomerNo = accountCustomerNo;
         }

        /**
	 * �x�b��s�R�[�h���Z�b�g����B
	 * @return bankCode �x�b�h��s�R�[�h
	 */
         public String getBankCode() {
             return bankCode;
         }

         /**
	 * �x�b��s�R�[�h���Z�b�g����B
	 * @param bankCode �x�b��s�R�[�h
	 */
         public void setBankCode(String bankCode) {
             this.bankCode = bankCode;
         }

        /**
	 * �x�b��s�����Z�b�g����B
	 * @return bankName �x�b�h��s��
	 */
         public String getBankName() {
             return bankName;
         }

         /**
	 * �x�b��s�����Z�b�g����B
	 * @param bankName �x�b��s��
	 */
         public void setBankName(String bankName) {
             this.bankName = bankName;
         }

         /**
	 * �x�b�x�X�R�[�h���Z�b�g����B
	 * @return branchCode �x�b�h�x�X�R�[�h
	 */
         public String getBranchCode() {
             return branchCode;
         }

         /**
	 * �x�b�x�X�R�[�h���Z�b�g����B
	 * @param branchCode �x�b�x�X�R�[�h
	 */
         public void setBranchCode(String branchCode) {
             this.branchCode = branchCode;
         }

         /**
	 * �x�b�x�X�����Z�b�g����B
	 * @return branchName �x�b�h�x�X��
	 */
         public String getBranchName() {
             return branchName;
         }

         /**
	 * �x�b�x�X�����Z�b�g����B
	 * @param branchName �x�b�x�X��
	 */
         public void setBranchName(String branchName) {
             this.branchName = branchName;
         }

         /**
	 * �x�b������ʂ��Z�b�g����B
	 * @return accountType �x�b�h�������
	 */
         public String getAccountType() {
             return accountType;
         }

         /**
	 * �x�b������ʂ��Z�b�g����B
	 * @param accountType �x�b�������
	 */
         public void setAccountType(String accountType) {
             this.accountType = accountType;
         }

         /**
	 * �x�b�����ԍ����Z�b�g����B
	 * @return �����ԍ� �x�b�h�����ԍ�
	 */
         public String getAccountNumber() {
             return accountNumber;
         }

         /**
	 * �x�b�����ԍ����Z�b�g����B
	 * @param accountNumber �x�b�����ԍ�
	 */
         public void setAccountNumber(String accountNumber) {
             this.accountNumber = accountNumber;
         }

         /**
	 * �x�b�������`���Z�b�g����B
	 * @return accountName �x�b�h�������`
	 */
         public String getAccountName() {
             return accountName;
         }

         /**
	 * �x�b�������`���Z�b�g����B
	 * @param accountName �x�b�������`
	 */
         public void setAccountName(String accountName) {
             this.accountName = accountName;
         }
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
                MstCustomer customer = new MstCustomer(rs.getInt("customer_id"));
                customer.load(SystemInfo.getConnection());
                this.setCustomer(customer);
		this.setAccountCustomerNo(rs.getString("account_customer_no"));
                this.setBankCode(rs.getString("bank_code"));
                this.setBankName(rs.getString("bank_name"));
                this.setBranchCode(rs.getString("branch_code"));
                this.setBranchName(rs.getString("branch_name"));
                this.setAccountType(rs.getString("account_type"));
                this.setAccountNumber(rs.getString("account_number"));
                this.setAccountName(rs.getString("account_name"));
	}
        
        /**
	 * �V���������U�֗p�ڋqNO.���Z�b�g����B
	 */
	public void setAccountCustomerNo()
	{
            String newAccountCustomerNo = padRightZeros(String.valueOf(this.getAccountCustomerNo()), 14);
            this.setAccountCustomerNo(newAccountCustomerNo);
	}
        
    /**
     * �E�[���𖄂߂�
     * @param inputString
     * @param length
     * @return 
     */
    public String padRightZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
	
	
	/**
	 * �������Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
                //0000000000001���玩���̔�
                setAccountCustomerNo();
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
	 * ������񂩂�f�[�^���폜����B�i�_���폜�j
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
	 * �������̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if (this.getCustomer()== null || this.getCustomer().getCustomerID() == null) return false;
		
		if (con == null) return false;
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                
		if (rs.next()) {
                    this.setData(rs);
                    return true;
                }
                
                return false;
	}
        
	/**
	 * �������Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if (this.getCustomer()== null || this.getCustomer().getCustomerID() == null)	return	false;
		
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
				"from data_account_info\n" +
				"where	customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + "\n";
	}
        
        /**
	 * �V���������U�֗p�ڋqNO.�𐿋�����r�p�k���𐿋�����B
	 * @return �V���������U�֗p�ڋqNO���擾����r�p�k��
	 */
	public String getNewAccountCustomerNoSQL()
	{
		return	"select coalesce(max(CAST(account_customer_no AS INTEGER)), 0) + 1 as new_account_customer_no\n" +
				"from data_account_info\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into data_account_info\n" +
				"(customer_id, account_customer_no, bank_code, bank_name,\n" +
                                "branch_code, branch_name, account_type, account_number, account_name,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
				SQLUtil.convertForSQL(this.getAccountCustomerNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getBankCode()) + ",\n" +
                                SQLUtil.convertForSQL(this.getBankName()) + ",\n" +
                                SQLUtil.convertForSQL(this.getBranchCode()) + ",\n" +
                                SQLUtil.convertForSQL(this.getBranchName()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountType()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountNumber()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountName()) + ",\n" +
                                "current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update data_account_info\n" +
				"set\n" +
                                "account_customer_no = " + SQLUtil.convertForSQL(this.getAccountCustomerNo()) + ",\n" +
				"bank_code = " + SQLUtil.convertForSQL(this.getBankCode()) + ",\n" +
                                "bank_name = " + SQLUtil.convertForSQL(this.getBankName()) + ",\n" +
                                "branch_code = " + SQLUtil.convertForSQL(this.getBranchCode()) + ",\n" +
                                "branch_name = " + SQLUtil.convertForSQL(this.getBranchName()) + ",\n" +
                                "account_type = " + SQLUtil.convertForSQL(this.getAccountType()) + ",\n" +
                                "account_number = " + SQLUtil.convertForSQL(this.getAccountNumber()) + ",\n" +
                                "account_name = " + SQLUtil.convertForSQL(this.getAccountName()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID());
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update data_account_info\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + "\n";
	}
}
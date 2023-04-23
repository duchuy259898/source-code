/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.data.account.DataPaymentDetail;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *�����E���ʏ�񖾍�
 * @author lvtu
 */
public class DataTransferResultDetail {
    
        /**�U�֍�**/
        public static final int                 TRANSFERRED            =        0;
        /**�����s��**/
        public static final int                 LACK_OF_FUNDS          =        1;
        /**�a������Ȃ�**/
        public static final int                 NO_DEPOSIT_TRANSACTIONS=        2;
        /**�a���ғs���ɂ��U�֒�~**/
        public static final int                 STOP_TRANSFER_REASON   =        3;
        /**�a�������U�ֈ˗����Ȃ�**/
        public static final int                 NO_TRANSFER_REQUEST    =        4;
        /**�ϑ��ғs���ɂ��U�֒�~**/
        public static final int                 TRANSFER_STOP          =        8;
        /**���̑�**/
        public static final int                 OTHER                  =        9;
    
    	/**
	 * �������
	 */
	protected         DataAccountInfo         dataAccountInfo       =	null;
	/**
	 * ����ID
	 */
	protected	Integer			transferID		=	null;
	/**
	 * ��������ID
	 */
	protected	Integer			transferDetailID	=	null;
	/**
	 * �ڋqID
	 */
	protected	Integer			customerID		=	null;
	/**
	 * �����U�֗p�ڋqNO
	 */
	protected	String			accountCustomerNo	=	null;
	/**
	 * ��s�R�[�h
	 */
	protected	String			bankCode        	=	null;
	/**
	 * �x�X�R�[�h
	 */
	protected	String			branchCode              =	null;
	/**
	 * �������
	 */
	protected	String			accountType             =	null;
	/**
	 * �����ԍ�
	 */
	protected	String			accountNumber           =	null;
	/**
	 * �������`
	 */
	protected	String			accountName             =	null;
	/**
	 * �������z
	 */
	protected	Integer			billingAmount		=	null;
	/**
	 * �U�֌��ʃR�[�h
	 */
	protected	Integer			resultCode              =	null;
	/**
	 * �X��ID
	 */
	protected	Integer			shopID                  =	null;
	/**
	 * �`�[No
	 */
	protected	Integer			slipNo                  =	null;
        /**
	 * �X��
	 */
	protected	MstShop			mstShop                 =	null;
        /**
         * �v������
         */
	protected	String			planName                 =	"";
        /**
         * is show collect bill
         */
        protected       boolean                showCollectBill          =       false;

        /**
         * �x���f�[�^
         */
        protected ArrayList<DataPayment> payments = new ArrayList<DataPayment>();

        
        /** Creates a new instance of DataTransferResultDetail */
	public DataTransferResultDetail()
	{
	}

        @Override
	public String toString()
	{
            if (this.getResultCode() == null) {
                return "";
            }
            
            switch(this.getResultCode()) {
                case TRANSFERRED:
                  return "����";
                case LACK_OF_FUNDS:
                  return "���s:�����s��";
                case NO_DEPOSIT_TRANSACTIONS:
                  return "���s:�a������Ȃ�";
                case STOP_TRANSFER_REASON:
                  return "���s:�a���ғs���ɂ��U�֒�~ ";
                case NO_TRANSFER_REQUEST:
                  return "���s:�a�������U�ֈ˗����Ȃ�";  
                case TRANSFER_STOP:
                  return "���s:�ϑ��ғs���ɂ��U�֒�~";
                case OTHER:
                  return "���s:���̑�";
                default:
		return	"";
            }
	}
        
        /**
	 * �x�b���������Z�b�g����B
	 * @return �x�b�hdataAccountInfo
	 */
        public DataAccountInfo getDataAccountInfo() {
            return dataAccountInfo;
        }

        /**
	 * �x�b���������Z�b�g����B
	 * @param dataAccountInfo �x�b�������
	 */
        public void setDataAccountInfo(DataAccountInfo dataAccountInfo) {
            this.dataAccountInfo = dataAccountInfo;
        }
        
        /**
         * ����ID
         * @return transferID
         */
        public Integer getTransferID() {
            return transferID;
        }

        /**
         * ����ID
         * @param transferID 
         */
        public void setTransferID(Integer transferID) {
            this.transferID = transferID;
        }

        /**
         * ��������ID
         * @return 
         */
        public Integer getTransferDetailID() {
            return transferDetailID;
        }

        /**
         * ��������ID
         * @param transferDetailID 
         */
        public void setTransferDetailID(Integer transferDetailID) {
            this.transferDetailID = transferDetailID;
        }

        /**
         * �ڋqID
         * @return 
         */
        public Integer getCustomerID() {
            return customerID;
        }

        /**
         * �ڋqID
         * @param customerID 
         */
        public void setCustomerID(Integer customerID) {
            this.customerID = customerID;
        }

        /**
         * �����U�֗p�ڋqNO
         * @return 
         */
        public String getAccountCustomerNo() {
            return accountCustomerNo;
        }

        /**
         * �����U�֗p�ڋqNO
         * @param accountCustomerNo 
         */
        public void setAccountCustomerNo(String accountCustomerNo) {
            this.accountCustomerNo = accountCustomerNo;
        }

        /**
         * ��s�R�[�h
         * @return 
         */
        public String getBankCode() {
            return bankCode;
        }

        /**
         * ��s�R�[�h
         * @param bankCode 
         */
        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        /**
         * �x�X�R�[�h
         * @return 
         */
        public String getBranchCode() {
            return branchCode;
        }

        /**
         * �x�X�R�[�h
         * @param branchCode 
         */
        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        /**
         * �������
         * @return 
         */
        public String getAccountType() {
            return accountType;
        }

        /**
         * �������
         * @param accountType 
         */
        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        /**
         * �����ԍ�
         * @return 
         */
        public String getAccountNumber() {
            return accountNumber;
        }

        /**
         * �����ԍ�
         * @param accountNumber 
         */
        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        /**
         * �������`
         * @return 
         */
        public String getAccountName() {
            return accountName;
        }

        /**
         * �������`
         * @param accountName 
         */
        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        /**
         * �������z
         * @return 
         */
        public Integer getBillingAmount() {
            return billingAmount;
        }

        /**
         * �������z
         * @param billingAmount 
         */
        public void setBillingAmount(Integer billingAmount) {
            this.billingAmount = billingAmount;
        }

        /**
         * �U�֌��ʃR�[�h
         * @return 
         */
        public Integer getResultCode() {
            return resultCode;
        }

        /**
         * �U�֌��ʃR�[�h
         * @param resultCode 
         */
        public void setResultCode(Integer resultCode) {
            this.resultCode = resultCode;
        }

        /**
         * �X��ID
         * @return 
         */
        public Integer getShopID() {
            return shopID;
        }

        /**
         * �X��ID
         * @param shopID 
         */
        public void setShopID(Integer shopID) {
            this.shopID = shopID;
        }

        /**
         * �`�[No
         * @return 
         */
        public Integer getSlipNo() {
            return slipNo;
        }

        /**
         * �`�[No
         * @param slipNo 
         */
        public void setSlipNo(Integer slipNo) {
            this.slipNo = slipNo;
        }
        
        /**
         * �X��
         * @return 
         */
        public MstShop getMstShop() {
            return mstShop;
        }

        /**
         * �X��
         * @param mstShop 
         */
        public void setMstShop(MstShop mstShop) {
            this.mstShop = mstShop;
        }
        /**
         * �v������
         * @return 
         */
        public String getPlanName() {
            return planName;
        }

        /**
         * �v������
         * @param planName 
         */
        public void setPlanName(String planName) {
            this.planName = planName;
        }
        /**
        * �x���f�[�^���擾����B
        *
        * @return �x���f�[�^
        */
       public ArrayList<DataPayment> getPayments() {
           return payments;
       }

       /**
        * �x���f�[�^���Z�b�g����B
        *
        * @param payments �x���f�[�^
        */
       public void setPayments(ArrayList<DataPayment> payments) {
           this.payments = payments;
       }
       
        public boolean isShowCollectBill() {
            if( this.getPayments().isEmpty() ) {
                this.showCollectBill = this.isExistsBill();
            }
            return showCollectBill;
        }
       
       /**
        * ���݂���r���ł�
        * @return 
        */
       private boolean isExistsBill() {
        if ( this == null) {
            return false;
        }
        
        DataPayment dp;
        DataPaymentDetail dpd;
        try {
        if( this.getPayments().isEmpty() ) {
            ConnectionWrapper	con	=	SystemInfo.getConnection();

            this.loadPayments(con);
            dp = this.getPayments().get(this.getPayments().size() -1);
            dpd = dp.get(0);
            if (dp.getBillValue() > 0 && !dpd.isExists(con)) {
                if( this.getResultCode() != null && this.getResultCode() > 0 ) {
                    return true;
                }
            }
        }
        } catch (SQLException ex) {
            Logger.getLogger(DataTransferResultDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

	/**
	 * �����E���ʏ�񖾍ׁ[�^����f�[�^���Z�b�g����B
	 * @param detail �����E���ʏ�񖾍�
	 */
	public void setData(DataTransferResultDetail detail)
	{   
                this.setTransferID(detail.getTransferID());
		this.setTransferDetailID(detail.getTransferDetailID());
		this.setCustomerID(detail.getCustomerID());
                this.setAccountCustomerNo(detail.getAccountCustomerNo());
                this.setBankCode(detail.getBankCode());
                this.setBranchCode(detail.getBranchCode());
		this.setAccountType(detail.getAccountType());
		this.setAccountNumber(detail.getAccountNumber());
		this.setAccountName(detail.getAccountName());
                this.setBillingAmount(detail.getBillingAmount());
                this.setResultCode(detail.getResultCode());
                this.setShopID(detail.getShopID());
                this.setSlipNo(detail.getSlipNo());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setTransferID(rs.getInt("transfer_id"));
		this.setTransferDetailID(rs.getInt("transfer_detail_id"));
		this.setCustomerID(rs.getInt("customer_id"));
                if (this.getCustomerID() != null ) {
                    MstCustomer cus = new MstCustomer(this.getCustomerID());
                    DataAccountInfo accountInfo = new DataAccountInfo(cus);
                    accountInfo.load(SystemInfo.getConnection());
                    this.setDataAccountInfo(accountInfo);
                }
                this.setAccountCustomerNo(rs.getString("account_customer_no"));
                this.setBankCode(rs.getString("bank_code"));
                this.setBranchCode(rs.getString("branch_code"));
		this.setAccountType(rs.getString("account_type"));
		this.setAccountNumber(rs.getString("account_number"));
		this.setAccountName(rs.getString("account_name"));
                this.setBillingAmount(rs.getInt("billing_amount"));
                Object obj = rs.getObject("result_code");
                if (obj != null) {
                    this.setResultCode(rs.getInt("result_code"));
                }
                this.setShopID(rs.getInt("shop_id"));
                if (this.getShopID() != null) {
                    MstShop shop = new MstShop();
                    shop.setShopID(this.getShopID());
                    shop.load(SystemInfo.getConnection());
                    this.setMstShop(shop);
                }
                this.setSlipNo(rs.getInt("slip_no"));
	}
	
	
	/**
	 * �����E���ʏ�񖾍ׂɃf�[�^��o�^����B
	 * @return true - ����
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
                        this.setNewtransferDetailID(con);
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
        
        /**
	 * �V�����x��No.���Z�b�g����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewtransferDetailID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewTransferDetailIdSQL());
		
		if(rs.next())
		{
			this.setTransferDetailID(rs.getInt("new_transfer_detail_id"));
		}
		
		rs.close();
	}

	/**
	 * �����E���ʏ�񖾍ׂ���f�[�^���폜����B�i�_���폜�j
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
	 * �����E���ʏ�񖾍ׂɃf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getTransferID()== null || this.getTransferDetailID() == null || this.getCustomerID() == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        /**
         * �x�����̓ǂݍ���
         * @param con
         * @return
         * @throws SQLException 
         */
        public boolean loadPayments(ConnectionWrapper con) throws SQLException {
            payments.clear();

            ResultSetWrapper rs = con.executeQuery(this.getSelectPaymentsSQL());

            while (rs.next()) {
                DataPayment dp = new DataPayment();
                dp.setData(rs);
                payments.add(dp);
            }

            rs.close();

            for (DataPayment dp : payments) {
                dp.loadAll(con);
            }

            return true;
        }
        /**
	 * �V�����x��No.���Z�b�g����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public int getNewPaymentNo(ConnectionWrapper con) throws SQLException
	{
            int newPaymentNo = 1;
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewPaymentNoSQL());
		
		if(rs.next())
		{
			newPaymentNo = rs.getInt("new_payment_no");
		}
		
		rs.close();
                
                return newPaymentNo;
	}
        /**
	 * Select�����擾����B
	 * @return Select��
	 */
        public String getSelectPaymentsSQL() {
            return "select dp.*, ms.staff_name1, ms.staff_name2\n"
                    + "from data_payment dp\n"
                    + "left outer join mst_staff ms\n"
                    + "on ms.staff_id = dp.staff_id\n"
                    + "where dp.delete_date is null\n"
                    + "and dp.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                    + "and dp.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
        }
        
        /**
	 * �V�����x��No.���擾����r�p�k�����擾����B
	 * @return �V�����x��No.���擾����r�p�k��
	 */
	public String getNewPaymentNoSQL()
	{
		return	"select coalesce(max(payment_no), 0) + 1 as new_payment_no\n" +
				"from data_payment\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}

	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_transfer_result_detail\n" +
				"where	transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n" +
                                "and transfer_detail_id = " + SQLUtil.convertForSQL(this.getTransferDetailID()) + "\n" +
                                "and customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
	}
        
        /**
	 * �V������������ID.�𐿋�����r�p�k���𐿋�����B
	 * @return �V������������ID���擾����r�p�k��
	 */
	public String getNewTransferDetailIdSQL()
	{
		return	"select coalesce(max(transfer_detail_id), 0) + 1 as new_transfer_detail_id\n" +
				"from data_transfer_result_detail\n" +
                                "where transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into data_transfer_result_detail\n" +
				"(transfer_id, transfer_detail_id, customer_id, \n" +
                                "account_customer_no, bank_code, branch_code, account_type, \n" +
                                "account_number, account_name, billing_amount,  \n" +
				"result_code, shop_id, slip_no, \n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getTransferID()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTransferDetailID()) + ",\n" +
                                SQLUtil.convertForSQL(this.getCustomerID()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountCustomerNo()) + ",\n" +
                                SQLUtil.convertForSQL(this.getBankCode()) + ",\n" +
                                SQLUtil.convertForSQL(this.getBranchCode()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountType()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountNumber()) + ",\n" +
                                SQLUtil.convertForSQL(this.getAccountName()) + ",\n" +
                                SQLUtil.convertForSQL(this.getBillingAmount()) + ",\n" +
                                SQLUtil.convertForSQL(this.getResultCode()) + ",\n" +
                                SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                                SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
            
            
		return	"update data_transfer_result_detail\n" +
				"set\n" +
                                "account_customer_no = " + SQLUtil.convertForSQL(this.getAccountCustomerNo()) + ",\n" +
                                "bank_code = " + SQLUtil.convertForSQL(this.getBankCode()) + ",\n" +
                                "branch_code = " + SQLUtil.convertForSQL(this.getBranchCode()) + ",\n" +
                                "account_type = " + SQLUtil.convertForSQL(this.getAccountType()) + ",\n" +
                                "account_number = " + SQLUtil.convertForSQL(this.getAccountNumber()) + ",\n" +
                                "account_name = " + SQLUtil.convertForSQL(this.getAccountName()) + ",\n" +
                                "billing_amount = " + SQLUtil.convertForSQL(this.getBillingAmount()) + ",\n" +
                                "result_code = " + SQLUtil.convertForSQL(this.getResultCode()) + ",\n" +
                                "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                                "slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n" +
                                "and transfer_detail_id = " + SQLUtil.convertForSQL(this.getTransferDetailID()) + "\n" +
                                "and customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update data_transfer_result_detail\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n" +
                                "and transfer_detail_id = " + SQLUtil.convertForSQL(this.getTransferDetailID()) + "\n" +
                                "and customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
	}
}

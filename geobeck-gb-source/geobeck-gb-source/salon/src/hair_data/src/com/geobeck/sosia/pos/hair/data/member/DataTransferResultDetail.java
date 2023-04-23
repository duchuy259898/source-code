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
 *請求・結果情報明細
 * @author lvtu
 */
public class DataTransferResultDetail {
    
        /**振替済**/
        public static final int                 TRANSFERRED            =        0;
        /**資金不足**/
        public static final int                 LACK_OF_FUNDS          =        1;
        /**預金取引なし**/
        public static final int                 NO_DEPOSIT_TRANSACTIONS=        2;
        /**預金者都合による振替停止**/
        public static final int                 STOP_TRANSFER_REASON   =        3;
        /**預金口座振替依頼書なし**/
        public static final int                 NO_TRANSFER_REQUEST    =        4;
        /**委託者都合による振替停止**/
        public static final int                 TRANSFER_STOP          =        8;
        /**その他**/
        public static final int                 OTHER                  =        9;
    
    	/**
	 * 口座情報
	 */
	protected         DataAccountInfo         dataAccountInfo       =	null;
	/**
	 * 請求ID
	 */
	protected	Integer			transferID		=	null;
	/**
	 * 請求明細ID
	 */
	protected	Integer			transferDetailID	=	null;
	/**
	 * 顧客ID
	 */
	protected	Integer			customerID		=	null;
	/**
	 * 口座振替用顧客NO
	 */
	protected	String			accountCustomerNo	=	null;
	/**
	 * 銀行コード
	 */
	protected	String			bankCode        	=	null;
	/**
	 * 支店コード
	 */
	protected	String			branchCode              =	null;
	/**
	 * 口座種別
	 */
	protected	String			accountType             =	null;
	/**
	 * 口座番号
	 */
	protected	String			accountNumber           =	null;
	/**
	 * 口座名義
	 */
	protected	String			accountName             =	null;
	/**
	 * 請求金額
	 */
	protected	Integer			billingAmount		=	null;
	/**
	 * 振替結果コード
	 */
	protected	Integer			resultCode              =	null;
	/**
	 * 店舗ID
	 */
	protected	Integer			shopID                  =	null;
	/**
	 * 伝票No
	 */
	protected	Integer			slipNo                  =	null;
        /**
	 * 店舗
	 */
	protected	MstShop			mstShop                 =	null;
        /**
         * プラン名
         */
	protected	String			planName                 =	"";
        /**
         * is show collect bill
         */
        protected       boolean                showCollectBill          =       false;

        /**
         * 支払データ
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
                  return "成功";
                case LACK_OF_FUNDS:
                  return "失敗:資金不足";
                case NO_DEPOSIT_TRANSACTIONS:
                  return "失敗:預金取引なし";
                case STOP_TRANSFER_REASON:
                  return "失敗:預金者都合による振替停止 ";
                case NO_TRANSFER_REQUEST:
                  return "失敗:預金口座振替依頼書なし";  
                case TRANSFER_STOP:
                  return "失敗:委託者都合による振替停止";
                case OTHER:
                  return "失敗:その他";
                default:
		return	"";
            }
	}
        
        /**
	 * ベッ口座情報をセットする。
	 * @return ベッドdataAccountInfo
	 */
        public DataAccountInfo getDataAccountInfo() {
            return dataAccountInfo;
        }

        /**
	 * ベッ口座情報をセットする。
	 * @param dataAccountInfo ベッ口座情報
	 */
        public void setDataAccountInfo(DataAccountInfo dataAccountInfo) {
            this.dataAccountInfo = dataAccountInfo;
        }
        
        /**
         * 請求ID
         * @return transferID
         */
        public Integer getTransferID() {
            return transferID;
        }

        /**
         * 請求ID
         * @param transferID 
         */
        public void setTransferID(Integer transferID) {
            this.transferID = transferID;
        }

        /**
         * 請求明細ID
         * @return 
         */
        public Integer getTransferDetailID() {
            return transferDetailID;
        }

        /**
         * 請求明細ID
         * @param transferDetailID 
         */
        public void setTransferDetailID(Integer transferDetailID) {
            this.transferDetailID = transferDetailID;
        }

        /**
         * 顧客ID
         * @return 
         */
        public Integer getCustomerID() {
            return customerID;
        }

        /**
         * 顧客ID
         * @param customerID 
         */
        public void setCustomerID(Integer customerID) {
            this.customerID = customerID;
        }

        /**
         * 口座振替用顧客NO
         * @return 
         */
        public String getAccountCustomerNo() {
            return accountCustomerNo;
        }

        /**
         * 口座振替用顧客NO
         * @param accountCustomerNo 
         */
        public void setAccountCustomerNo(String accountCustomerNo) {
            this.accountCustomerNo = accountCustomerNo;
        }

        /**
         * 銀行コード
         * @return 
         */
        public String getBankCode() {
            return bankCode;
        }

        /**
         * 銀行コード
         * @param bankCode 
         */
        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        /**
         * 支店コード
         * @return 
         */
        public String getBranchCode() {
            return branchCode;
        }

        /**
         * 支店コード
         * @param branchCode 
         */
        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        /**
         * 口座種別
         * @return 
         */
        public String getAccountType() {
            return accountType;
        }

        /**
         * 口座種別
         * @param accountType 
         */
        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        /**
         * 口座番号
         * @return 
         */
        public String getAccountNumber() {
            return accountNumber;
        }

        /**
         * 口座番号
         * @param accountNumber 
         */
        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        /**
         * 口座名義
         * @return 
         */
        public String getAccountName() {
            return accountName;
        }

        /**
         * 口座名義
         * @param accountName 
         */
        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        /**
         * 請求金額
         * @return 
         */
        public Integer getBillingAmount() {
            return billingAmount;
        }

        /**
         * 請求金額
         * @param billingAmount 
         */
        public void setBillingAmount(Integer billingAmount) {
            this.billingAmount = billingAmount;
        }

        /**
         * 振替結果コード
         * @return 
         */
        public Integer getResultCode() {
            return resultCode;
        }

        /**
         * 振替結果コード
         * @param resultCode 
         */
        public void setResultCode(Integer resultCode) {
            this.resultCode = resultCode;
        }

        /**
         * 店舗ID
         * @return 
         */
        public Integer getShopID() {
            return shopID;
        }

        /**
         * 店舗ID
         * @param shopID 
         */
        public void setShopID(Integer shopID) {
            this.shopID = shopID;
        }

        /**
         * 伝票No
         * @return 
         */
        public Integer getSlipNo() {
            return slipNo;
        }

        /**
         * 伝票No
         * @param slipNo 
         */
        public void setSlipNo(Integer slipNo) {
            this.slipNo = slipNo;
        }
        
        /**
         * 店舗
         * @return 
         */
        public MstShop getMstShop() {
            return mstShop;
        }

        /**
         * 店舗
         * @param mstShop 
         */
        public void setMstShop(MstShop mstShop) {
            this.mstShop = mstShop;
        }
        /**
         * プラン名
         * @return 
         */
        public String getPlanName() {
            return planName;
        }

        /**
         * プラン名
         * @param planName 
         */
        public void setPlanName(String planName) {
            this.planName = planName;
        }
        /**
        * 支払データを取得する。
        *
        * @return 支払データ
        */
       public ArrayList<DataPayment> getPayments() {
           return payments;
       }

       /**
        * 支払データをセットする。
        *
        * @param payments 支払データ
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
        * 存在するビルです
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
	 * 請求・結果情報明細ータからデータをセットする。
	 * @param detail 請求・結果情報明細
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
	 * ResultSetWrapperからデータをセットする
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
	 * 請求・結果情報明細にデータを登録する。
	 * @return true - 成功
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
	 * 新しい支払No.をセットする。
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
	 * 請求・結果情報明細からデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * 請求・結果情報明細にデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
         * 支払いの読み込み
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
	 * 新しい支払No.をセットする。
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
	 * Select文を取得する。
	 * @return Select文
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
	 * 新しい支払No.を取得するＳＱＬ文を取得する。
	 * @return 新しい支払No.を取得するＳＱＬ文
	 */
	public String getNewPaymentNoSQL()
	{
		return	"select coalesce(max(payment_no), 0) + 1 as new_payment_no\n" +
				"from data_payment\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}

	/**
	 * Select文を取得する。
	 * @return Select文
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
	 * 新しい請求明細ID.を請求するＳＱＬ文を請求する。
	 * @return 新しい請求明細IDを取得するＳＱＬ文
	 */
	public String getNewTransferDetailIdSQL()
	{
		return	"select coalesce(max(transfer_detail_id), 0) + 1 as new_transfer_detail_id\n" +
				"from data_transfer_result_detail\n" +
                                "where transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
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

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
 *口座情報
 * @author lvtu
 */
public class DataAccountInfo {

        
	/**
	 * 顧客
	 */
	private	MstCustomer	customer		=	null;
	/**
	 * 口座振替用顧客NO
	 */
	private	String		accountCustomerNo	=	null;
	/**
	 * 銀行コード
	 */
	private	String		bankCode		=	null;
	/**
	 * 銀行名
	 */
	private	String		bankName		=	null;
	/**
	 * 支店コード
	 */
	private	String		branchCode		=	null;
	/**
	 * 支店名
	 */
	private	String		branchName		=	null;
	/**
	 * 口座種別
	 */
	private	String		accountType		=	null;
	/**
	 * 口座番号
	 */
	private	String		accountNumber		=	null;
	/**
	 * 口座名義
	 */
	private	String		accountName		=	null;
        /**
         * 預金種別
         */
        public static enum AccountType {

            NORMAL(1, "普通"),
            POPULAR(2, "当座");

            private final Integer value;
            private final String text;

            /**
             * コードによる検索を容易にするための整数コードとそれに対応するテキスト間のマッピング。
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
	 * 文字列に変換する。（ベッド名）
	 * @return ベッド名
	 */
        @Override
	public String toString()
	{
		return	this.getCustomer().getCustomerNo();
	}

        /**
	 * ベッ顧客をセットする。
	 * @return ベッドcustomer
	 */
        public MstCustomer getCustomer() {
            return customer;
        }

        /**
	 * ベッ顧客をセットする。
	 * @param customer ベッ顧客
	 */
        public void setCustomer(MstCustomer customer) {
            this.customer = customer;
        }

        /**
	 * ベッ口座振替用顧客NOをセットする。
	 * @return accountCustomerNo ベッド口座振替用顧客NO
	 */
         public String getAccountCustomerNo() {
             return accountCustomerNo;
         }

        /**
	 * ベッ口座振替用顧客NOをセットする。
	 * @param accountCustomerNo ベッ口座振替用顧客NO
	 */
         public void setAccountCustomerNo(String accountCustomerNo) {
             this.accountCustomerNo = accountCustomerNo;
         }

        /**
	 * ベッ銀行コードをセットする。
	 * @return bankCode ベッド銀行コード
	 */
         public String getBankCode() {
             return bankCode;
         }

         /**
	 * ベッ銀行コードをセットする。
	 * @param bankCode ベッ銀行コード
	 */
         public void setBankCode(String bankCode) {
             this.bankCode = bankCode;
         }

        /**
	 * ベッ銀行名をセットする。
	 * @return bankName ベッド銀行名
	 */
         public String getBankName() {
             return bankName;
         }

         /**
	 * ベッ銀行名をセットする。
	 * @param bankName ベッ銀行名
	 */
         public void setBankName(String bankName) {
             this.bankName = bankName;
         }

         /**
	 * ベッ支店コードをセットする。
	 * @return branchCode ベッド支店コード
	 */
         public String getBranchCode() {
             return branchCode;
         }

         /**
	 * ベッ支店コードをセットする。
	 * @param branchCode ベッ支店コード
	 */
         public void setBranchCode(String branchCode) {
             this.branchCode = branchCode;
         }

         /**
	 * ベッ支店名をセットする。
	 * @return branchName ベッド支店名
	 */
         public String getBranchName() {
             return branchName;
         }

         /**
	 * ベッ支店名をセットする。
	 * @param branchName ベッ支店名
	 */
         public void setBranchName(String branchName) {
             this.branchName = branchName;
         }

         /**
	 * ベッ口座種別をセットする。
	 * @return accountType ベッド口座種別
	 */
         public String getAccountType() {
             return accountType;
         }

         /**
	 * ベッ口座種別をセットする。
	 * @param accountType ベッ口座種別
	 */
         public void setAccountType(String accountType) {
             this.accountType = accountType;
         }

         /**
	 * ベッ口座番号をセットする。
	 * @return 口座番号 ベッド口座番号
	 */
         public String getAccountNumber() {
             return accountNumber;
         }

         /**
	 * ベッ口座番号をセットする。
	 * @param accountNumber ベッ口座番号
	 */
         public void setAccountNumber(String accountNumber) {
             this.accountNumber = accountNumber;
         }

         /**
	 * ベッ口座名義をセットする。
	 * @return accountName ベッド口座名義
	 */
         public String getAccountName() {
             return accountName;
         }

         /**
	 * ベッ口座名義をセットする。
	 * @param accountName ベッ口座名義
	 */
         public void setAccountName(String accountName) {
             this.accountName = accountName;
         }
	
	/**
	 * ResultSetWrapperからデータをセットする。
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
	 * 新しい口座振替用顧客NO.をセットする。
	 */
	public void setAccountCustomerNo()
	{
            String newAccountCustomerNo = padRightZeros(String.valueOf(this.getAccountCustomerNo()), 14);
            this.setAccountCustomerNo(newAccountCustomerNo);
	}
        
    /**
     * 右ゼロを埋める
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
	 * 口座情報にデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
                //0000000000001から自動採番
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
	 * 口座情報からデータを削除する。（論理削除）
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
	 * 口座情報のデータを読み込む。
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
	 * 口座情報にデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_account_info\n" +
				"where	customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + "\n";
	}
        
        /**
	 * 新しい口座振替用顧客NO.を請求するＳＱＬ文を請求する。
	 * @return 新しい口座振替用顧客NOを取得するＳＱＬ文
	 */
	public String getNewAccountCustomerNoSQL()
	{
		return	"select coalesce(max(CAST(account_customer_no AS INTEGER)), 0) + 1 as new_account_customer_no\n" +
				"from data_account_info\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update data_account_info\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + "\n";
	}
}
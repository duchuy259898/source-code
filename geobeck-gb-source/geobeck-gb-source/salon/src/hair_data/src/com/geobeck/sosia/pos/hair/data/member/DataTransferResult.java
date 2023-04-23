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
import java.util.ArrayList;
import java.util.Date;

/**
 * 請求・結果情報ヘッダ
 * @author lvtu
 */
public final class DataTransferResult  extends ArrayList<DataTransferResultDetail>
{
        /**
         * 請求データ出力
         */
        public final static  int EXPORT_DATA_REQUEST                   =       1;
        /**
         * 振替結果取込
         */
        public final static  int IMPORT_DATA_RESULT                    =       2;
	/**
	 * 請求ID
	 */
	protected	Integer		transferID		=	null;
	/**
	 * 対象月
	 */
	protected	java.util.Date	targetMonth             =	null;
        /**
	 * 口座振替年
	 */
	protected	String		transferYear            =	null;
        /**
	 * 口座振替月
	 */
	protected	String		transferMonth           =	null;
        /**
	 * 口座振替日
	 */
	protected	String		transferDate            =	null;
	/**
	 * 請求件数合計
	 */
	protected	Integer		totalNum		=	0;
	/**
	 * 請求金額合計
	 */
	protected	Integer		totalAmount		=	0;
        /**
	 * 請求金額合計
         * 1:請求データ登録　2:結果データ取込済
	 */
	protected	Integer		status                  =	1;
	
	/**
	 * 請求・結果情報ヘッダ
	 */
	public DataTransferResult()
	{
	}
        
        /**
	 * 請求・結果情報ヘッダ
         * @param other
	 */
	public DataTransferResult( DataTransferResult other)
	{
            this.setTransferID(other.getTransferID());
            this.setTargetMonth(other.getTargetMonth());
            this.setTransferYear(other.getTransferYear());
            this.setTransferMonth(other.getTransferMonth());
            this.setTransferDate(other.getTransferDate());
            this.setTotalNum(other.getTotalNum());
            this.setTotalAmount(other.getTotalAmount());
	}

        /**
         * 請求ID
         * @return 
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
         * 対象月
         * @return 
         */
        public Date getTargetMonth() {
            return targetMonth;
        }

        /**
         * 対象月
         * @param targetMonth 
         */
        public void setTargetMonth(Date targetMonth) {
            this.targetMonth = targetMonth;
        }

        /**
         * 口座振替年
         * @return 
         */
        public String getTransferYear() {
            return transferYear;
        }

        /**
         * 口座振替年
         * @param transferYear 
         */
        public void setTransferYear(String transferYear) {
            this.transferYear = transferYear;
        }

        /**
         * 口座振替月
         * @return 
         */
        public String getTransferMonth() {
            return transferMonth;
        }

        /**
         * 口座振替月
         * @param transferMonth 
         */
        public void setTransferMonth(String transferMonth) {
            this.transferMonth = transferMonth;
        }

        /**
         * 口座振替日
         * @return 
         */
        public String getTransferDate() {
            return transferDate;
        }

        /**
         * 口座振替日
         * @param transferDate 
         */
        public void setTransferDate(String transferDate) {
            this.transferDate = transferDate;
        }

        /**
         * 請求件数合計
         * @return 
         */
        public Integer getTotalNum() {
            return totalNum;
        }

        /**
         * 請求件数合計
         * @param totalNum 
         */
        public void setTotalNum(Integer totalNum) {
            this.totalNum = totalNum;
        }

        /**
         * 請求金額合計
         * @return 
         */
        public Integer getTotalAmount() {
            return totalAmount;
        }

        /**
         * 請求金額合計
         * @param totalAmount 
         */
        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }
        /**
         * ステータス
         * @return 
         */
        public Integer getStatus() {
            return status;
        }

        /**
         * ステータス
         * @param status 
         */
        public void setStatus(Integer status) {
            this.status = status;
        }
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setTransferID(rs.getInt("transfer_id"));
		this.setTargetMonth(rs.getDate("target_month"));
		this.setTransferYear(rs.getString( "transfer_year" ) );
                this.setTransferMonth(rs.getString("transfer_month"));
		this.setTransferDate(rs.getString("transfer_date"));
                this.setTotalNum(rs.getInt("total_num"));
                this.setTotalAmount(rs.getInt("total_amount"));
                this.setStatus(rs.getInt("status"));
	}
	
        /**
	 * 新しい支払No.をセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewtransferID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewTransferIdSQL());
		
		if(rs.next())
		{
			this.setTransferID(rs.getInt("new_transfer_id"));
		}
		
		rs.close();
	}
	
	/**
	 * 支払データと、明細を全て登録する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con) throws SQLException
	{
		if(!this.regist(con))	return	false;

		for(DataTransferResultDetail detail : this)
		{
			detail.setTransferID(this.getTransferID());
			if(!detail.regist(con))	return	false;			

		}

		return	true;
	}
	
	/**
	 * コース分類マスタにデータを登録する。
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
			this.setNewtransferID(con);
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	
	/**
	 * コース分類マスタからデータを削除する。（論理削除）
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
	 * 請求・結果情報明細をArrayListに読み込む。
         * @param con
         * @param typeLoad
         * @throws java.sql.SQLException
	 */
	public void load(ConnectionWrapper con, int typeLoad) throws SQLException
	{
		this.clear();
                
                String sql = "";
                if (typeLoad == EXPORT_DATA_REQUEST) {
                    sql = this.getSelectDataRequestByTargetMonthSQL();
                } else {
                    sql = this.getSelectTransferDetailSQL();
                }
		
		ResultSetWrapper	rs	=	con.executeQuery(sql);
                
		while(rs.next())
		{
                        if (this.getTransferID() == null ) {
                            this.setData(rs);
                        }
			DataTransferResultDetail	detail	=	new	DataTransferResultDetail();
			detail.setData(rs);
			this.add(detail);
		}

		rs.close();
	}
        
        /**
	 * 請求・結果情報明細をArrayListに読み込む。
         * @param con
	 */
	public void loadByCustomer(ConnectionWrapper con, Integer customerID) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getTransferDetailByCustomerSQL(customerID));

		while(rs.next())
		{
                        if (this.getTransferID() == null ) {
                            this.setData(rs);
                        }
			DataTransferResultDetail	detail	=	new	DataTransferResultDetail();
			detail.setData(rs);
			this.add(detail);
		}

		rs.close();
	}
	
	/**
	 * コース分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getTransferID()== null)	return	false;
		
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
		return	"select *\n"
			+	"from data_transfer_result\n"
			+	"where	transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n";
	}
        
        /**
	 * 新しい請求ID.を請求するＳＱＬ文を請求する。
	 * @return 新しい請求IDを取得するＳＱＬ文
	 */
	public String getNewTransferIdSQL()
	{
		return	"select coalesce(max(transfer_id), 0) + 1 as new_transfer_id\n" +
				"from data_transfer_result\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into data_transfer_result\n" +
				"(transfer_id, target_month, transfer_year, " +
                                "transfer_month, transfer_date, total_num, total_amount, status,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getTransferID()) + ",\n" +
				SQLUtil.convertForSQL(this.getTargetMonth()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTransferYear()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTransferMonth()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTransferDate()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTotalNum()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTotalAmount()) + ",\n" +
                                "1,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
                return	"update data_transfer_result\n" +
                                    "set\n" +
                                    "target_month = " + SQLUtil.convertForSQL(this.getTargetMonth()) + ",\n" +
                                    "transfer_year = " + SQLUtil.convertForSQL(this.getTransferYear()) + ",\n" +
                                    "transfer_month = " + SQLUtil.convertForSQL(this.getTransferMonth()) + ",\n" +
                                    "transfer_date = " + SQLUtil.convertForSQL(this.getTransferDate()) + ",\n" +
                                    "total_num = " + SQLUtil.convertForSQL(this.getTotalNum()) + ",\n" +
                                    "total_amount = " + SQLUtil.convertForSQL(this.getTotalAmount()) + ",\n" +
                                    "status = " + SQLUtil.convertForSQL(this.getStatus()) + "\n" +
                                    "where transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update data_transfer_result\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	transfer_id = " + SQLUtil.convertForSQL(this.getTransferID()) + "\n";
	}
	
	/**
	 * 請求・結果情報明細
	 * @return 請求・結果情報明細タデータを全て取得するＳＱＬ文
	 */
	private String getSelectTransferDetailSQL()
	{
		return	"SELECT T1.transfer_id, \n" +
                        "	   T1.target_month, \n" +
                        "	   T1.transfer_year, \n" +
                        "	   T1.transfer_month, \n" +
                        "	   T1.transfer_date, \n" +
                        "	   T1.total_num, \n" +
                        "	   T1.total_amount, \n" +
                        "	   T1.status, \n" +
                        "	   T2.transfer_detail_id, \n" +
                        "	   T2.account_customer_no, \n" +
                        "	   T2.account_type, \n" +
                        "	   T2.account_number, \n" +
                        "	   T2.account_name, \n" +
                        "	   T2.billing_amount, \n" +
                        "	   T2.result_code, \n" +
                        "	   T2.shop_id, \n" +
                        "	   T2.slip_no, \n" +
                        "	   T3.bank_code, \n" +
                        "	   T3.bank_name, \n" +
                        "	   T3.branch_code, \n" +
                        "	   T3.branch_name, \n" +
                        "	   T4.customer_id \n" +
                        "FROM data_transfer_result T1 \n" +
                        "INNER JOIN data_transfer_result_detail T2 ON T1.transfer_id = T2.transfer_id \n" +
                        "INNER JOIN data_account_info T3 ON T2.customer_id = T3.customer_id \n" +
                        "INNER JOIN mst_customer T4 ON T3.customer_id = T4.customer_id \n" +
                        "WHERE T1.delete_date IS NULL \n" +
                        "  AND T1.target_month =  " +
                                            SQLUtil.convertForSQL(this.getTargetMonth()) + "\n" +
                        "ORDER BY T1.transfer_id, \n" +
                        "		 T2.transfer_detail_id \n";
	}
        
        private String getSelectDataRequestByTargetMonthSQL() {
        return	"SELECT    T6.transfer_id AS transfer_id,\n" +
                "	   T1.target_month,\n" +
                "	   T6.transfer_year AS transfer_year,\n" +
                "	   T6.transfer_month AS transfer_month,\n" +
                "	   T6.transfer_date AS transfer_date,\n" +
                "	   NULL AS total_num,\n" +
                "	   NULL AS total_amount,\n" +
                "	   1 AS status,\n" +
                "	   T6.transfer_detail_id AS transfer_detail_id,\n" +
                "	   T3.account_customer_no,\n" +
                "	   T3.account_type,\n" +
                "	   T3.account_number,\n" +
                "	   T3.account_name,\n" +
                "	   T6.result_code AS result_code,\n" +
                "	   T1.shop_id,\n" +
                "	   T2.slip_no,\n" +
                "	   T2.course_value AS billing_amount,\n" +
                "	   T3.*\n" +
                "FROM data_monthly_batch_log T1\n" +
                "INNER JOIN data_monthly_batch_detail_log T2 ON T1.batch_id = T2.batch_id\n" +
                "INNER JOIN data_account_info T3 ON T2.customer_id = T3.customer_id\n" +
                "LEFT JOIN (\n" +
                "        SELECT T4.transfer_id, T4.target_month, T4.transfer_year, \n" +
                "        T4.transfer_month, T4.transfer_date, T5.customer_id, \n" +
                "        T5.transfer_detail_id, T5.result_code\n" +
                "        FROM data_transfer_result T4\n" +
                "        INNER JOIN data_transfer_result_detail T5 ON T5.transfer_id = T4.transfer_id\n" +
                "        WHERE T4.target_month = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "\n" +
                " ) T6 ON T6.target_month = T1.target_month AND T6.customer_id = T2.customer_id\n" +
                "WHERE T1.delete_date IS NULL\n" +
                "   AND T1.target_month = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "\n" +
                "ORDER BY T1.target_month, T6.transfer_id, T6.transfer_detail_id";
        }
        
        
        /**
	 * 請求・結果情報明細
        * @param customerID
	 * @return 請求・結果情報明細タデータを全て取得するＳＱＬ文
	 */
	private String getTransferDetailByCustomerSQL(Integer customerID)
	{
		return	"select T1.bank_code, \n" +
                        "T1.bank_name, \n" +
                        "T1.branch_code, \n" +
                        "T1.branch_name, \n" +
                        "T1.account_type, \n" +
                        "T1.account_number, \n" +
                        "T1.account_name, \n" +
                        "T3.target_month, \n" +
                        "T3.transfer_year, \n" +
                        "T3.transfer_month, \n" +
                        "T3.transfer_date, \n" +
                        "T3.status, \n" +
                        "T2.result_code, \n" +
                        "T2.slip_no, \n" +
                        "T4.shop_id, \n" +
                        "T4.shop_name \n" +
                        "from data_account_info T1 \n" +
                        "inner join data_transfer_result_detail T2 on T2.customer_id = T1.customer_id \n" +
                        "inner join data_transfer_result T3 on T3.transfer_id = T2.transfer_id \n" +
                        "inner join mst_shop T4 on T4.shop_id = T2.shop_id \n" +
			"where T1.delete_date is null\n" +
				"and T1.customer_id = " + SQLUtil.convertForSQL(customerID) + "\n" +
			"order by T3.transfer_id, T2.transfer_detail_id\n";
	}
        
}

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
	 * 委託者コード
	 */
	private	String		consignorCode		=	null;
	/**
	 * 区分
	 */
	private	String		division		=	null;
	/**
	 * 委託者名
	 */
	private	String		consignorName		=	null;
	/**
	 * 振替日
	 */
	private	String		transferDate		=	null;
        /**
	 * 支払方法
	 */
	private	Integer		paymentMethod		=	null;

	/** Creates a new instance of MstAccountTransferSetting */
	public MstAccountTransferSetting()
	{
	}
        /**
         * 委託者コード
         * @return consignorCode
         */
         public String getConsignorCode() {
            return consignorCode;
        }

         /**
          * 委託者コード
          * @param consignorCode 
          */
        public void setConsignorCode(String consignorCode) {
            this.consignorCode = consignorCode;
        }

        /**
         * 区分
         * @return division
         */
        public String getDivision() {
            return division;
        }

        /**
         * 区分
         * @param division 
         */
        public void setDivision(String division) {
            this.division = division;
        }

        /**
         * 委託者名
         * @return consignorName
         */
        public String getConsignorName() {
            return consignorName;
        }

        /**
         * 委託者名
         * @param consignorName 
         */
        public void setConsignorName(String consignorName) {
            this.consignorName = consignorName;
        }

        /**
         * 振替日
         * @return transferDate
         */
        public String getTransferDate() {
            return transferDate;
        }

        /**
         * 振替日
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
	 * 文字列に変換する。（委託者名）
	 * @return ベッド名
	 */
        @Override
	public String toString()
	{
		return	this.getConsignorName();
	}
	
	/**
	 * ResultSetWrapperからデータをセットする。
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
	 * 口座振替基本情報のデータを読み込む。
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
	 * 基本情報にデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * 基本情報にデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_account_transfer_setting\n" +
				"limit 1\n";
	}
        
        /**
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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

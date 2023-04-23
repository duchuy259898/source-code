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
 * 支払方法マスタデータ
 * @author katagiri
 */
public class MstPaymentMethod
{
	/**
	 * 支払区分
	 */
	protected	MstPaymentClass		paymentClass		=	new MstPaymentClass();
	/**
	 * 支払方法ＩＤ
	 */
	protected	Integer				paymentMethodID		=	null;
	/**
	 * 支払方法名
	 */
	protected	String				paymentMethodName	=	"";
	/**
	 * 締め日
	 */
	protected	Integer				cutoffDay			=	null;
	/**
	 * 入金月
	 */
	protected	Integer				receiptClass		=	null;
	/**
	 * 入金日
	 */
	protected	Integer				receiptDay			=	null;
	/**
	 * プリペイド
	 */
	private	Integer				prepaid			=	null;
	
	/**
	 * コンストラクタ
	 */
	public MstPaymentMethod()
	{
	}
	
	/**
	 * 文字列に変換する。（支払方法名）
	 * @return 支払方法名
	 */
	public String toString()
	{
		if(paymentMethodName == null)	return	"";
		return	this.getPaymentMethodName();
	}

	/**
	 * 支払区分を取得する。
	 * @return 支払区分
	 */
	public MstPaymentClass getPaymentClass()
	{
		return paymentClass;
	}

	/**
	 * 支払区分をセットする。
	 * @param paymentClass 支払区分
	 */
	public void setPaymentClass(MstPaymentClass paymentClass)
	{
		this.paymentClass = paymentClass;
	}

	/**
	 * 支払区分ＩＤを取得する。
	 * @return 支払区分ＩＤ
	 */
	public Integer getPaymentClassID()
	{
		if(paymentClass == null)	return	null;
		else	return paymentClass.getPaymentClassID();
	}

	/**
	 * 支払区分ＩＤをセットする。
	 * @param paymentClassID 支払区分ＩＤ
	 */
	public void setPaymentClassID(Integer paymentClassID)
	{
		if(paymentClass == null)	paymentClass	=	new	MstPaymentClass();
		this.paymentClass.setPaymentClassID(paymentClassID);
	}

	/**
	 * 支払方法ＩＤを取得する。
	 * @return 支払方法ＩＤ
	 */
	public Integer getPaymentMethodID()
	{
		return paymentMethodID;
	}

	/**
	 * 支払方法ＩＤをセットする。
	 * @param paymentMethodID 支払方法ＩＤ
	 */
	public void setPaymentMethodID(Integer paymentMethodID)
	{
		this.paymentMethodID = paymentMethodID;
	}

	/**
	 * 支払方法名を取得する。
	 * @return 支払方法名
	 */
	public String getPaymentMethodName()
	{
		return paymentMethodName;
	}

	/**
	 * 支払方法名をセットする。
	 * @param paymentMethodName 支払方法名
	 */
	public void setPaymentMethodName(String paymentMethodName)
	{
		this.paymentMethodName = paymentMethodName;
	}

	/**
	 * 締め日を取得する。
	 * @return 締め日
	 */
	public Integer getCutoffDay()
	{
		return cutoffDay;
	}

	/**
	 * 締め日をセットする。
	 * @param cutoffDay 締め日
	 */
	public void setCutoffDay(Integer cutoffDay)
	{
		this.cutoffDay = cutoffDay;
	}

	/**
	 * 入金月を取得する。
	 * @return 入金月
	 */
	public Integer getReceiptClass()
	{
		return receiptClass;
	}

	/**
	 * 入金月をセットする。
	 * @param receiptClass 入金月
	 */
	public void setReceiptClass(Integer receiptClass)
	{
		this.receiptClass = receiptClass;
	}

	/**
	 * 入金月名を取得する。
	 * @return 入金月名
	 */
	public String getReceiptClassName()
	{
		switch(receiptClass)
		{
			case 1:
				return	"当月";
			case 2:
				return	"翌月";
			case 3:
				return	"翌々月";
			default:
				return	"";
		}
	}

	/**
	 * 入金日を取得する。
	 * @return 入金日
	 */
	public Integer getReceiptDay()
	{
		return receiptDay;
	}

	/**
	 * 入金日をセットする。
	 * @param receiptDay 入金日
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
	 * データをクリアする。
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
	 * 商品マスタから、設定されている商品IDのデータを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功
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
	 * ResultSetWrapperのデータを読み込む。
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
	 * 商品マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * 商品マスタからデータを削除する。（論理削除）
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
	 * 商品マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * 商品マスタ取得用のＳＱＬ文を取得する。
	 * @return 商品マスタ取得用のＳＱＬ文
	 * @param productClassID 商品区分ＩＤ
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
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_payment_method\n" +
				"where payment_method_id = " + SQLUtil.convertForSQL(this.getPaymentMethodID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_payment_method\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where payment_method_id = " + SQLUtil.convertForSQL(this.getPaymentMethodID()) + "\n";
	}

}

/*
 * MstPaymentClass.java
 *
 * Created on 2006/04/25, 17:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 支払区分マスタデータ
 * @author katagiri
 */
public class MstPaymentClass extends ArrayList<MstPaymentMethod>
{
	/**
	 * 支払区分ＩＤ
	 */
	protected	Integer		paymentClassID		=	null;
	/**
	 * 支払区分名
	 */
	protected	String		paymentClassName	=	null;
	
	/**
	 * コンストラクタ
	 */
	public MstPaymentClass()
	{
	}
	
	/**
	 * 文字列に変換する。（支払区分名）
	 * @return 支払区分名
	 */
	public String toString()
	{
		if(paymentClassName == null)	return	"";
		return	this.getPaymentClassName();
	}

	/**
	 * 支払区分ＩＤを取得する。
	 * @return 支払区分ＩＤ
	 */
	public Integer getPaymentClassID()
	{
		return paymentClassID;
	}

	/**
	 * 支払区分ＩＤをセットする。
	 * @param paymentClassID 支払区分ＩＤ
	 */
	public void setPaymentClassID(Integer paymentClassID)
	{
		this.paymentClassID = paymentClassID;
	}

	/**
	 * 支払区分名を取得する。
	 * @return 支払区分名
	 */
	public String getPaymentClassName()
	{
		return paymentClassName;
	}

	/**
	 * 支払区分名をセットする。
	 * @param paymentClassName 支払区分名
	 */
	public void setPaymentClassName(String paymentClassName)
	{
		this.paymentClassName = paymentClassName;
	}
	
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setPaymentClassID(null);
		this.setPaymentClassName("");
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setPaymentClassID(rs.getInt("payment_class_id"));
		this.setPaymentClassName(rs.getString("payment_class_name"));
	}
	
	
	/**
	 * 支払い区分マスタにデータを登録する。
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
	 * 支払い区分マスタからデータを削除する。（論理削除）
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
	 * 支払い区分マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getPaymentClassID() == null || this.getPaymentClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 商品区分マスタ取得用のＳＱＬ文を取得する。
	 * @return 商品区分マスタ取得用のＳＱＬ文
	 * @param isIncludeCash true - 現金を含む
	 */
	public static String getSelectAllSQL(boolean isIncludeCash)
	{
		return	"select		*\n"
			+	"from		mst_payment_class\n"
			+	"where		delete_date is null\n"
			+	(isIncludeCash ? "" : "and	payment_class_id != 1")
			+	"order by	payment_class_id\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_payment_class\n"
			+	"where	payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_payment_class\n"
			+	"(payment_class_id, payment_class_name,\n"
			+	"insert_date, update_date, delete_date)\n"
			+	"select\n"
			+	"coalesce(max(payment_class_id), 0) + 1,\n"
			+	SQLUtil.convertForSQL(this.getPaymentClassName()) + ",\n"
			+	"current_timestamp, current_timestamp, null\n"
			+	"from mst_payment_class\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_payment_class\n"
			+	"set\n"
			+	"payment_class_name = " + SQLUtil.convertForSQL(this.getPaymentClassName()) + ",\n"
			+	"update_date = current_timestamp\n"
			+	"where	payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_payment_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	payment_class_id = " + SQLUtil.convertForSQL(this.getPaymentClassID()) + "\n";
	}
}

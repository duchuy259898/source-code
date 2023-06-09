/*
 * DataPaymentDetail.java
 *
 * Created on 2006/05/09, 9:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

import com.geobeck.sosia.pos.master.account.*;

/**
 * 支払明細データ
 * @author katagiri
 */
public class DataPaymentDetail
{
	/**
	 * 店舗
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * 伝票No.
	 */
	protected	Integer				slipNo			=	null;
	/**
	 * 支払No.
	 */
	protected	Integer				paymentNo		=	null;
	/**
	 * 支払明細No.
	 */
	protected	Integer				paymentDetailNo	=	null;
	/**
	 * 支払方法
	 */
	protected	MstPaymentMethod	paymentMethod	=	null;
	/**
	 * 支払金額
	 */
	protected	Long				paymentValue	=	null;
	
	/**
	 * コンストラクタ
	 */
	public DataPaymentDetail()
	{
	}

	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * 店舗をセットする。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * 伝票No.を取得する。
	 * @return 伝票No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * 伝票No.をセットする。
	 * @param slipNo 伝票No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * 支払No.を取得する。
	 * @return 支払No.
	 */
	public Integer getPaymentNo()
	{
		return paymentNo;
	}

	/**
	 * 支払No.をセットする。
	 * @param paymentNo 支払No.
	 */
	public void setPaymentNo(Integer paymentNo)
	{
		this.paymentNo = paymentNo;
	}

	/**
	 * 支払明細No.を取得する。
	 * @return 支払明細No.
	 */
	public Integer getPaymentDetailNo()
	{
		return paymentDetailNo;
	}

	/**
	 * 支払明細No.をセットする。
	 * @param paymentDetailNo 支払明細No.
	 */
	public void setPaymentDetailNo(Integer paymentDetailNo)
	{
		this.paymentDetailNo = paymentDetailNo;
	}

	/**
	 * 支払方法を取得する。
	 * @return 支払方法
	 */
	public MstPaymentMethod getPaymentMethod()
	{
		return paymentMethod;
	}

	/**
	 * 支払方法をセットする。
	 * @param paymentMethod 支払方法
	 */
	public void setPaymentMethod(MstPaymentMethod paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 支払金額を取得する。
	 * @return 支払金額
	 */
	public Long getPaymentValue()
	{
		return paymentValue;
	}

	/**
	 * 支払金額をセットする。
	 * @param paymentValue 支払金額
	 */
	public void setPaymentValue(Long paymentValue)
	{
		this.paymentValue = paymentValue;
	}
	
	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setPaymentNo(rs.getInt("payment_no"));
		this.setPaymentDetailNo(rs.getInt("payment_detail_no"));
		MstPaymentClass		mpc	=	new MstPaymentClass();
		MstPaymentMethod	mpm	=	new MstPaymentMethod();
		mpc.setPaymentClassID(rs.getInt("payment_class_id"));
		mpc.setPaymentClassName(rs.getString("payment_class_name"));
		mpm.setPaymentClass(mpc);
		mpm.setPaymentMethodID(rs.getInt("payment_method_id"));
		mpm.setPaymentMethodName(rs.getString("payment_method_name"));
		mpm.setPrepaid(rs.getInt("prepaid"));
		this.setPaymentMethod(mpm);
		this.setPaymentValue(rs.getLong("payment_value"));
	}
	
	
	/**
	 * 新しい支払明細No.をセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewSlipDetailNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewPaymentDetailNoSQL());
		
		if(rs.next())
		{
			this.setPaymentDetailNo(rs.getInt("new_payment_detail_no"));
		}
		
		rs.close();
	}
	
	
	/**
	 * 登録処理を行う。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
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
	 * 支払明細データが存在するかを取得する。
	 * @param con ConnectionWrapper
	 * @return true - 存在する。
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	
	/**
	 * 新しい支払明細No.を取得するＳＱＬ文を取得する。
	 * @return 新しい支払明細No.を取得するＳＱＬ文
	 */
	public String getNewPaymentDetailNoSQL()
	{
		return	"select coalesce(max(payment_detail_no), 0) + 1 as new_payment_detail_no\n" +
				"from data_payment_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo()) + "\n";
	}
	
	
	/**
	 * 支払明細データを取得するＳＱＬ文を取得する。
	 * @return 支払明細データを取得するＳＱＬ文
	 */
	public String getSelectSQL()
	{
		return	"select *\n" +
				"from data_payment_detail\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo()) + "\n" +
				"and payment_detail_no = " + SQLUtil.convertForSQL(this.getPaymentDetailNo());
	}
	
	/**
	 * 支払明細データをInsertするＳＱＬ文を取得する。
	 * @return 支払明細データをInsertするＳＱＬ文
	 */
	public String getInsertSQL()
	{
		return	"insert into data_payment_detail\n" +
				"(shop_id, slip_no, payment_no, payment_detail_no,\n" +
				"payment_method_id, payment_value,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentDetailNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentMethod().getPaymentMethodID()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentValue(), "0") + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * 支払明細データをUpdateするＳＱＬ文を取得する。
	 * @return 支払明細データをUpdateするＳＱＬ文
	 */
	public String getUpdateSQL()
	{
		return	"update data_payment_detail\n" +
				"set\n" +
				"payment_date = " + SQLUtil.convertForSQL(this.getPaymentMethod().getPaymentMethodID()) + ",\n" +
				"payment_date = " + SQLUtil.convertForSQL(this.getPaymentValue(), "0") + ",\n" +
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo()) + "\n" +
				"and payment_detail_no = " + SQLUtil.convertForSQL(this.getPaymentDetailNo());
	}
}

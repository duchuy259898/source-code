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
 * x•¥–¾×ƒf[ƒ^
 * @author katagiri
 */
public class DataPaymentDetail
{
	/**
	 * “X•Ü
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * “`•[No.
	 */
	protected	Integer				slipNo			=	null;
	/**
	 * x•¥No.
	 */
	protected	Integer				paymentNo		=	null;
	/**
	 * x•¥–¾×No.
	 */
	protected	Integer				paymentDetailNo	=	null;
	/**
	 * x•¥•û–@
	 */
	protected	MstPaymentMethod	paymentMethod	=	null;
	/**
	 * x•¥‹àŠz
	 */
	protected	Long				paymentValue	=	null;
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public DataPaymentDetail()
	{
	}

	/**
	 * “X•Ü‚ğæ“¾‚·‚éB
	 * @return “X•Ü
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * “X•Ü‚ğƒZƒbƒg‚·‚éB
	 * @param shop “X•Ü
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * “`•[No.‚ğæ“¾‚·‚éB
	 * @return “`•[No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * “`•[No.‚ğƒZƒbƒg‚·‚éB
	 * @param slipNo “`•[No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * x•¥No.‚ğæ“¾‚·‚éB
	 * @return x•¥No.
	 */
	public Integer getPaymentNo()
	{
		return paymentNo;
	}

	/**
	 * x•¥No.‚ğƒZƒbƒg‚·‚éB
	 * @param paymentNo x•¥No.
	 */
	public void setPaymentNo(Integer paymentNo)
	{
		this.paymentNo = paymentNo;
	}

	/**
	 * x•¥–¾×No.‚ğæ“¾‚·‚éB
	 * @return x•¥–¾×No.
	 */
	public Integer getPaymentDetailNo()
	{
		return paymentDetailNo;
	}

	/**
	 * x•¥–¾×No.‚ğƒZƒbƒg‚·‚éB
	 * @param paymentDetailNo x•¥–¾×No.
	 */
	public void setPaymentDetailNo(Integer paymentDetailNo)
	{
		this.paymentDetailNo = paymentDetailNo;
	}

	/**
	 * x•¥•û–@‚ğæ“¾‚·‚éB
	 * @return x•¥•û–@
	 */
	public MstPaymentMethod getPaymentMethod()
	{
		return paymentMethod;
	}

	/**
	 * x•¥•û–@‚ğƒZƒbƒg‚·‚éB
	 * @param paymentMethod x•¥•û–@
	 */
	public void setPaymentMethod(MstPaymentMethod paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	/**
	 * x•¥‹àŠz‚ğæ“¾‚·‚éB
	 * @return x•¥‹àŠz
	 */
	public Long getPaymentValue()
	{
		return paymentValue;
	}

	/**
	 * x•¥‹àŠz‚ğƒZƒbƒg‚·‚éB
	 * @param paymentValue x•¥‹àŠz
	 */
	public void setPaymentValue(Long paymentValue)
	{
		this.paymentValue = paymentValue;
	}
	
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚éB
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
	 * V‚µ‚¢x•¥–¾×No.‚ğƒZƒbƒg‚·‚éB
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
	 * “o˜^ˆ—‚ğs‚¤B
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
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
	 * x•¥–¾×ƒf[ƒ^‚ª‘¶İ‚·‚é‚©‚ğæ“¾‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ‘¶İ‚·‚éB
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
	 * V‚µ‚¢x•¥–¾×No.‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return V‚µ‚¢x•¥–¾×No.‚ğæ“¾‚·‚é‚r‚p‚k•¶
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
	 * x•¥–¾×ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥–¾×ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶
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
	 * x•¥–¾×ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥–¾×ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶
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
	 * x•¥–¾×ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥–¾×ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶
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

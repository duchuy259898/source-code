/*
 * Bill.java
 *
 * Created on 2006/05/09, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.data.account.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.company.*;


/**
 * 売掛金データ
 * @author katagiri
 */
public class Bill
{
	/**
	 * 精算情報
	 */
	protected DataSales sales = new DataSales(SystemInfo.getTypeID());
	/**
	 * スタッフ
	 */
	protected MstStaff staff = new MstStaff();
	/**
	 * 売掛金
	 */
	protected Long bill = 0l;
	/**
	 * 売掛残
	 */
	protected Long billRest = 0l;
	
	
	/** Creates a new instance of Bill */
	public Bill()
	{
	}
	
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
	    getSales().setSalesDate(rs.getDate("sales_date"));
	    getSales().setSlipNo(rs.getInt("slip_no"));
	    getSales().getCustomer().setCustomerID(rs.getInt("customer_id"));
	    getSales().getCustomer().setCustomerNo(rs.getString("customer_no"));
	    getSales().getCustomer().setCustomerName(0, rs.getString("customer_name1"));
	    getSales().getCustomer().setCustomerName(1, rs.getString("customer_name2"));
	    getStaff().setStaffID(rs.getInt("staff_id"));
	    getStaff().setStaffName(0, rs.getString("staff_name1"));
	    getStaff().setStaffName(1, rs.getString("staff_name2"));
	    setBill(rs.getLong("bill_value"));
	    setBillRest(rs.getLong("bill_value_rest"));
	}

	/**
	 * 精算情報を取得する。
	 * @return 精算情報
	 */
	public DataSales getSales()
	{
	    return sales;
	}

	/**
	 * 精算情報をセットする。
	 * @param sales 精算情報
	 */
	public void setSales(DataSales sales)
	{
	    this.sales = sales;
	}
	
	public Integer getSlipNo()
	{
	    if(this.getSales() != null) {
		return this.getSales().getSlipNo();
	    }
		
	    return null;
	}
	
	public java.util.Date getSalesDate()
	{
	    if(this.getSales() != null) {
		return this.getSales().getSalesDate();
	    }
		
	    return null;
	}
	
	public String getCustomerNo()
	{
	    if(this.getSales() != null && this.getSales().getCustomer() != null) {
		return this.getSales().getCustomer().getCustomerNo();
	    }

	    return null;
	}
	
	public String getCustomerName()
	{
	    if(this.getSales() != null && this.getSales().getCustomer() != null) {
		return this.getSales().getCustomer().getFullCustomerName();
	    }

	    return null;
	}

	/**
	 * スタッフを取得する。
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
	    return staff;
	}

	/**
	 * スタッフをセットする。
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
	    this.staff = staff;
	}
	
	public String getStaffName()
	{
	    if(this.getStaff() != null) {
		return this.getStaff().getFullStaffName();
	    }

	    return null;
	}

	/**
	 * 売掛金を取得する。
	 * @return 売掛金
	 */
	public Long getBill()
	{
		return bill;
	}

	/**
	 * 売掛金をセットする。
	 * @param bill 売掛金
	 */
	public void setBill(Long bill)
	{
		this.bill = bill;
	}

	/**
	 * 売掛残を取得する。
	 * @return 売掛残
	 */
	public Long getBillRest()
	{
		return billRest;
	}

	/**
	 * 売掛残をセットする。
	 * @param billRest 売掛残
	 */
	public void setBillRest(Long billRest)
	{
		this.billRest = billRest;
	}
}

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
 * ���|���f�[�^
 * @author katagiri
 */
public class Bill
{
	/**
	 * ���Z���
	 */
	protected DataSales sales = new DataSales(SystemInfo.getTypeID());
	/**
	 * �X�^�b�t
	 */
	protected MstStaff staff = new MstStaff();
	/**
	 * ���|��
	 */
	protected Long bill = 0l;
	/**
	 * ���|�c
	 */
	protected Long billRest = 0l;
	
	
	/** Creates a new instance of Bill */
	public Bill()
	{
	}
	
	
	/**
	 * ResultSetWrapper����f�[�^���擾����B
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
	 * ���Z�����擾����B
	 * @return ���Z���
	 */
	public DataSales getSales()
	{
	    return sales;
	}

	/**
	 * ���Z�����Z�b�g����B
	 * @param sales ���Z���
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
	 * �X�^�b�t���擾����B
	 * @return �X�^�b�t
	 */
	public MstStaff getStaff()
	{
	    return staff;
	}

	/**
	 * �X�^�b�t���Z�b�g����B
	 * @param staff �X�^�b�t
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
	 * ���|�����擾����B
	 * @return ���|��
	 */
	public Long getBill()
	{
		return bill;
	}

	/**
	 * ���|�����Z�b�g����B
	 * @param bill ���|��
	 */
	public void setBill(Long bill)
	{
		this.bill = bill;
	}

	/**
	 * ���|�c���擾����B
	 * @return ���|�c
	 */
	public Long getBillRest()
	{
		return billRest;
	}

	/**
	 * ���|�c���Z�b�g����B
	 * @param billRest ���|�c
	 */
	public void setBillRest(Long billRest)
	{
		this.billRest = billRest;
	}
}

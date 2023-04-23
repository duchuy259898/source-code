/*
 * DataStockAccount.java
 *
 * Created on 2007/04/18, 15:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.commodity;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.commodity.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class DataStockAccount extends ArrayList<DataStockAccountDetail>
{
	protected	MstGroup			group				=	null;
	protected	MstShop				shop				=	null;
	protected	Integer				stockNo				=	null;
	protected	GregorianCalendar	stockDate			=	null;
	protected	GregorianCalendar	paymentDate			=	null;
	protected	MstSupplier			supplier			=	null;
	protected	Integer				purchaseDivision	=	0;
	protected	MstStaff			staff				=	null;
	protected	Double				discountRate		=	0d;
	protected	Integer				discountValue		=	0;
	
	
	/** Creates a new instance of DataStockAccount */
	public DataStockAccount()
	{
		super();
	}

	public MstGroup getGroup()
	{
		return group;
	}

	public void setGroup(MstGroup group)
	{
		this.group = group;
	}

	public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	public Integer getStockNo()
	{
		return stockNo;
	}

	public void setStockNo(Integer stockNo)
	{
		this.stockNo = stockNo;
	}

	public GregorianCalendar getStockDate()
	{
		return stockDate;
	}

	public void setStockDate(GregorianCalendar stockDate)
	{
		this.stockDate = stockDate;
	}

	public GregorianCalendar getPaymentDate()
	{
		return paymentDate;
	}

	public void setPaymentDate(GregorianCalendar paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	public MstSupplier getSupplier()
	{
		return supplier;
	}

	public void setSupplier(MstSupplier supplier)
	{
		this.supplier = supplier;
	}

	public Integer getPurchaseDivision()
	{
		return purchaseDivision;
	}

	public void setPurchaseDivision(Integer purchaseDivision)
	{
		this.purchaseDivision = purchaseDivision;
	}

	public MstStaff getStaff()
	{
		return staff;
	}

	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	public Double getDiscountRate()
	{
		return discountRate;
	}

	public void setDiscountRate(Double discountRate)
	{
		this.discountRate = discountRate;
	}

	public Integer getDiscountValue()
	{
		return discountValue;
	}

	public void setDiscountValue(Integer discountValue)
	{
		this.discountValue = discountValue;
	}
	
	public void add(MstItem item, Integer costPrice, Integer stockNum)
	{
		DataStockAccountDetail	temp	=	new DataStockAccountDetail();
		
		temp.setGroup(this.getGroup());
		temp.setShop(this.getShop());
		temp.setStockNo(this.getStockNo());
		temp.setItem(item);
		temp.setCostPrice(costPrice);
		temp.setStockNum(stockNum);
		
		this.add(temp);
	}
}

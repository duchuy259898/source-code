/*
 * DataStockAccountDetail.java
 *
 * Created on 2007/04/18, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.commodity;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class DataStockAccountDetail
{
	protected	MstGroup			group				=	null;
	protected	MstShop				shop				=	null;
	protected	Integer				stockNo				=	null;
	protected	Integer				stockDetailNo		=	null;
	protected	MstItem				item				=	null;
	protected	Integer				costPrice			=	null;
	protected	Integer				stockNum			=	null;
	
	/** Creates a new instance of DataStockAccountDetail */
	public DataStockAccountDetail()
	{
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

	public Integer getStockDetailNo()
	{
		return stockDetailNo;
	}

	public void setStockDetailNo(Integer stockDetailNo)
	{
		this.stockDetailNo = stockDetailNo;
	}

	public MstItem getItem()
	{
		return item;
	}

	public void setItem(MstItem item)
	{
		if(this.item == null)
		{
			this.item	=	new MstItem();
		}
		
		this.item.setData(item);
	}

	public Integer getCostPrice()
	{
		return costPrice;
	}

	public void setCostPrice(Integer costPrice)
	{
		this.costPrice = costPrice;
	}

	public Integer getStockNum()
	{
		return stockNum;
	}

	public void setStockNum(Integer stockNum)
	{
		this.stockNum = stockNum;
	}
	
}

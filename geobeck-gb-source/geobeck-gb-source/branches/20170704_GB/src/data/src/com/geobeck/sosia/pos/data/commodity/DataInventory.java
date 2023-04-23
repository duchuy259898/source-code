/*
 * DataInventory.java
 *
 * Created on 2007/04/25, 18:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.commodity;

import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.GregorianCalendar;

/**
 *
 * @author katagiri
 */
public class DataInventory
{
	protected MstGroup				group			=	null;
	protected MstShop				shop			=	null;
	protected GregorianCalendar		inventoryDate	=	null;
	protected MstItem				item			=	null;
	protected Integer				costPrice		=	0;
	protected Integer				initialStock	=	0;
	protected Integer				inNum			=	0;
	protected Integer				outNum			=	0;
	protected Integer				realNum			=	0;
	
	/** Creates a new instance of DataInventory */
	public DataInventory()
	{
	}

	public String toString()
	{
		if(getItem() != null && getItem().getItemClass() != null)
		{
			return getItem().getItemClass().getItemClassName();
		}
		else
		{
			return "";
		}
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

	public GregorianCalendar getInventoryDate()
	{
		return inventoryDate;
	}

	public void setInventoryDate(GregorianCalendar inventoryDate)
	{
		this.inventoryDate = inventoryDate;
	}

	public MstItem getItem()
	{
		return item;
	}

	public void setItem(MstItem item)
	{
		this.item = item;
	}

	public String getItemClassName()
	{
		if(item != null && item.getItemClass() != null)
		{
			return item.getItemClass().getItemClassName();
		}
		else
		{
			return "";
		}
	}

	public String getItemNo()
	{
		if(item != null)
		{
			return item.getItemNo();
		}
		else
		{
			return "";
		}
	}

	public String getItemName()
	{
		if(item != null)
		{
			return item.getItemName();
		}
		else
		{
			return "";
		}
	}

	public Integer getCostPrice()
	{
		return costPrice;
	}

	public void setCostPrice(Integer costPrice)
	{
		this.costPrice = costPrice;
	}

	public Integer getInitialStock()
	{
		return initialStock;
	}

	public void setInitialStock(Integer initialStock)
	{
		this.initialStock = initialStock;
	}

	public Integer getInNum()
	{
		return inNum;
	}

	public void setInNum(Integer inNum)
	{
		this.inNum = inNum;
	}

	public Integer getInValue()
	{
		return Integer.valueOf(getCostPrice().intValue() * getInNum().intValue());
	}

	public Integer getOutNum()
	{
		return outNum;
	}

	public void setOutNum(Integer outNum)
	{
		this.outNum = outNum;
	}

	public Integer getOutValue()
	{
		return Integer.valueOf(getCostPrice().intValue() * getOutNum().intValue());
	}

	public Integer getLogicalNum()
	{
		return Integer.valueOf((getInitialStock().intValue() + getInNum().intValue()) - getOutNum().intValue());
	}

	public Integer getRealNum()
	{
		return realNum;
	}

	public void setRealNum(Integer realNum)
	{
		this.realNum = realNum;
	}

	public Integer getDifferenceNum()
	{
		return Integer.valueOf(getRealNum().intValue() - getLogicalNum().intValue());
	}

	public Integer getRealValue()
	{
		return Integer.valueOf(getCostPrice().intValue() * getRealNum().intValue());
	}

	public void setData(ResultSetWrapper rs)
		throws SQLException
	{
		MstGroup mg = new MstGroup();
		mg.setGroupID(Integer.valueOf(rs.getInt("group_id")));
		setGroup(mg);
		MstShop ms = new MstShop();
		ms.setShopID(Integer.valueOf(rs.getInt("group_id")));
		setShop(ms);
		
		if(rs.getDate("inventory_date") == null)
		{
			setInventoryDate(null);
		} else
		{
			setInventoryDate(new GregorianCalendar());
			getInventoryDate().setTime(rs.getDate("inventory_date"));
		}
		
		MstItem mi = new MstItem();
		mi.setData(rs);
		mi.getItemClass().setItemClassName(rs.getString("item_class_name"));
		setItem(mi);
		setCostPrice(Integer.valueOf(rs.getInt("cost_price")));
		setInitialStock(Integer.valueOf(rs.getInt("initial_stock")));
		setInNum(Integer.valueOf(rs.getInt("in_num")));
		setOutNum(Integer.valueOf(rs.getInt("out_num")));
		setRealNum(Integer.valueOf(rs.getInt("real_num")));
	}

	public boolean regist(ConnectionWrapper con)
		throws SQLException
	{
		return con.executeUpdate(getInsertSQL()) == 1;
	}

	private String getInsertSQL()
	{
		return "insert into data_inventory(\n" +
				"group_id, shop_id,\n" +
				"inventory_date, item_id, cost_price,\n" +
				"initial_stock, in_num, out_num, real_num,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(getGroup().getGroupID()) + ",\n" +
				SQLUtil.convertForSQL(getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQLDateOnly(getInventoryDate()) + ",\n" +
				SQLUtil.convertForSQL(getItem().getItemID()) + ",\n" +
				SQLUtil.convertForSQL(getCostPrice()) + ",\n" +
				SQLUtil.convertForSQL(getInitialStock()) + ",\n" +
				SQLUtil.convertForSQL(getInNum()) + ",\n" +
				SQLUtil.convertForSQL(getOutNum()) + ",\n" +
				SQLUtil.convertForSQL(getRealNum()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null)\n";
	}
}

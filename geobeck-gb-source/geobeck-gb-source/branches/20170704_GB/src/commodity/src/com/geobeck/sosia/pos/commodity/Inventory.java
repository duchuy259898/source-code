/*
 * Inventory.java
 *
 * Created on 2007/04/20, 15:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.commodity;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.data.commodity.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author katagiri
 */
public class Inventory extends ArrayList<DataInventory>
{
	private static final String		REPORT_PATH	=	"/report/inventory.jasper";
	private static final String		REPORT_NAME	=	"Inventory";
	
	protected	MstGroup				group			=	null;
	protected	MstShop					shop			=	null;
	protected	GregorianCalendar		inventoryDate	=	null;
	
	/** Creates a new instance of Inventory */
	public Inventory()
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
	
	public String getTargetName()
	{
		if(shop != null && shop.getShopID() != null && 0 < shop.getShopID())
		{
			return	shop.getShopName();
		}
		else if(group != null && group.getGroupID() != null && 0 < group.getGroupID())
		{
			return	group.getGroupName();
		}
		
		return	"";
	}

	public GregorianCalendar getInventoryDate()
	{
		return inventoryDate;
	}

	public void setInventoryDate(GregorianCalendar inventoryDate)
	{
		this.inventoryDate = inventoryDate;
	}
	
	public void loadNewInventory()
	{
		this.clear();
		
		try
		{
			GregorianCalendar	lastInventoryDate	=	this.getLastInventoryDate();
			
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(
					this.getLoadNewInventorySQL(lastInventoryDate));
			
			while(rs.next())
			{
				DataInventory	di	=	new DataInventory();
				
				di.setData(rs);
				
				this.add(di);
			}
			
			rs.close();
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public GregorianCalendar getLastInventoryDate()
	{
		GregorianCalendar	lastInventoryDate	=	null;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLastInventoryDateSQL());
			
			if(rs.next())
			{
				if(rs.getDate("last_inventory_date") != null)
				{
					lastInventoryDate	=	new GregorianCalendar();
					lastInventoryDate.setTime(rs.getDate("last_inventory_date"));
				}
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	lastInventoryDate;
	}
	
	private String getLastInventoryDateSQL()
	{
		return	"select max(di.inventory_date) as last_inventory_date\n" +
				"from data_inventory di\n" +
				"where di.group_id = " + SQLUtil.convertForSQL(this.getGroup().getGroupID()) + "\n" +
				"and di.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
	
	private String getLoadNewInventorySQL(GregorianCalendar lastInventoryDate)
	{
		return	"select mic.item_class_name,\n" +
				SQLUtil.convertForSQL(this.getGroup().getGroupID()) + " as group_id,\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + " as shop_id,\n" +
				"null as inventory_date,\n" +
				"mi.*,\n" +
				"coalesce(mp.cost_price, mi.price, 0) as cost_price,\n" +
				"coalesce(li.real_num, 0) as initial_stock,\n" +
				"coalesce(din.in_num, 0) as in_num,\n" +
				(0 < this.getGroup().getGroupID() ?
				"coalesce(dout.out_num, 0)" : "0") + " as out_num,\n" +
				"0 as real_num\n" +
				"from mst_item mi\n" +
				"inner join mst_item_class mic\n" +
				"on mic.item_class_id = mi.item_class_id\n" +
				"left outer join (\n" +
				"select mp.item_id,\n" +
				"round(avg(mp.cost_price), 0) as cost_price\n" +
				"from mst_purchase mp\n" +
				"where mp.delete_date is null\n" +
				"group by mp.item_id\n" +
				") as mp\n" +
				"on mp.item_id = mi.item_id\n" +
				"left outer join (\n" +
				"select di.item_id,\n" +
				"di.real_num\n" +
				"from data_inventory di\n" +
				"where di.group_id = " + SQLUtil.convertForSQL(this.getGroup().getGroupID()) + "\n" +
				"and di.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and di.inventory_date = to_date(" + SQLUtil.convertForSQLDateOnly(lastInventoryDate) + ", 'YYYY/MM/DD')\n" +
				") li\n" +
				"on li.item_id = mi.item_id\n" +
				"left outer join (\n" +
				"select dsd.item_id,\n" +
				"sum(dsd.stock_num) as in_num\n" +
				"from data_stock ds\n" +
				"left outer join data_stock_detail dsd\n" +
				"on dsd.group_id = ds.group_id\n" +
				"and dsd.shop_id = ds.shop_id\n" +
				"and dsd.stock_no = ds.stock_no\n" +
				"and dsd.delete_date is null\n" +
				"where ds.group_id = " + SQLUtil.convertForSQL(this.getGroup().getGroupID()) + "\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				(lastInventoryDate == null ? "" : "and ds.stock_date >= to_date("
				+ SQLUtil.convertForSQLDateOnly(lastInventoryDate) + ", 'YYYY/MM/DD') + 1\n") +
				"group by dsd.item_id\n" +
				") din\n" +
				"on din.item_id = mi.item_id\n" +
				(0 < this.getGroup().getGroupID() ?
				"left outer join (\n" +
				"select dsd.product_id as item_id,\n" +
				"sum(dsd.product_num) as out_num\n" +
				"from data_sales ds\n" +
				"left outer join data_sales_detail dsd\n" +
				"on dsd.shop_id = ds.shop_id\n" +
				"and dsd.slip_no = ds.slip_no\n" +
				"and dsd.product_division = 2\n" +
				"and dsd.delete_date is null\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				(lastInventoryDate == null ? "" : "and ds.sales_date >= to_date("
				+ SQLUtil.convertForSQLDateOnly(lastInventoryDate) + ", 'YYYY/MM/DD') + 1\n") +
				"group by dsd.product_id\n" +
				") dout\n" +
				"on dout.item_id = mi.item_id\n" :
				"") +
				"order by mic.display_seq, mi.display_seq\n";
	}
	
	public boolean regist()
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
				for(DataInventory di : this)
				{
					di.setInventoryDate(this.getInventoryDate());
					
					if(!di.regist(con))
					{
						con.rollback();
						break;
					}
				}
				
				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	true;
	}
	
	public Integer getTotalRealValue()
	{
		Integer		total	=	0;
		
		for(DataInventory di : this)
		{
			total	+=	di.getRealValue();
		}
		
		return	total;
	}
	
	public void print()
	{
		HashMap<String, Object>		param	=	new HashMap<String, Object>();
		
		param.put("targetName", this.getTargetName());
		param.put("inventoryDate", String.format("%1$tY”N%1$tmŒŽ%1$td“ú", this.getInventoryDate()));
		
		InputStream		report		=	Inventory.class.getResourceAsStream(REPORT_PATH);
		String			fileName	=	REPORT_NAME + String.format("%1$tY%1$tm%1$td%2$ts",
				this.getInventoryDate(), new java.util.Date());
		
		ReportManager.exportReport(report, fileName, ReportManager.PDF_FILE, param, this);
	}
}

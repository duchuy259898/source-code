/*
 * DataAdjustment.java
 *
 * Created on 2007/07/03, 11:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class DataAdjustment
{
	private MstShop				shop				=	null;
	private	Integer				adjustmentNo		=	null;
	private	GregorianCalendar	adjustmentDate		=	null;
	private	MstStaff			staff				=	null;
	private	Long				adjustmentValue		=	0l;
	private	String				note				=	"";
	
	/** Creates a new instance of DataAdjustment */
	public DataAdjustment()
	{
	}

	public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	public Integer getShopID()
	{
		if(shop == null)
		{
			return	null;
		}
		else
		{
			return	shop.getShopID();
		}
	}

	public Integer getAdjustmentNo()
	{
		return adjustmentNo;
	}

	public void setAdjustmentNo(Integer adjustmentNo)
	{
		this.adjustmentNo = adjustmentNo;
	}

	public GregorianCalendar getAdjustmentDate()
	{
		return adjustmentDate;
	}

	public void setAdjustmentDate(GregorianCalendar adjustmentDate)
	{
		this.adjustmentDate = adjustmentDate;
	}

	public MstStaff getStaff()
	{
		return staff;
	}
	
	public Integer getStaffID()
	{
		if(staff == null)
		{
			return	null;
		}
		else
		{
			return	staff.getShopID();
		}
	}

	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	public Long getAdjustmentValue()
	{
		return adjustmentValue;
	}

	public void setAdjustmentValue(Long adjustmentValue)
	{
		this.adjustmentValue = adjustmentValue;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}
	
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() <= 0 || this.getAdjustmentDate() == null || this.getAdjustmentValue() == 0)
		{
			return	false;
		}
		
		if(this.isExist(con))
		{
			if(con.executeUpdate(this.getUpdateSQL()) == 1)
			{
				return	true;
			}
		}
		else
		{
			if(con.executeUpdate(this.getInsertSQL()) == 1)
			{
				this.setAdjustmentNo(this.getMaxIONo(con));
				
				if(0 < this.getAdjustmentNo())
				{
					return	true;
				}
			}
		}
		
		return	false;
	}
	
	
	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null || this.getAdjustmentNo() == null)
		{
			return	false;
		}
		
		boolean		result	=	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			result	=	true;
		}
		
		rs.close();
		
		return	result;
	}
	
	private Integer getMaxIONo(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null)
		{
			return	0;
		}
		
		Integer		result	=	0;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getMaxIONoSQL());
		
		if(rs.next())
		{
			result	=	rs.getInt("max_adjustment_no");
		}
		
		rs.close();
		
		return	result;
	}
	
	
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null || this.getAdjustmentNo() == null)
		{
			return	false;
		}
		
		return	(con.executeUpdate(this.getDeleteSQL()) == 1);
	}
	
	private String getSelectSQL()
	{
		return	"select da.*,\n" +
				"msp.group_id, msp.shop_name,\n" +
				"mst.staff_no, mst.staff_name1, mst.staff_name2\n" +
				"from data_adjustment da\n" +
				"left outer join mst_shop msp\n" +
				"on msp.shop_id = da.shop_id\n" +
				"left outer join mst_staff mst\n" +
				"on mst.staff_id = da.staff_id\n" +
				"where da.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and da.adjustment_no = " + SQLUtil.convertForSQL(this.getAdjustmentNo()) + "\n";
	}
	
	private String getInsertSQL()
	{
		return	"insert into data_adjustment\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				"coalesce(max(da.adjustment_no), 0) + 1,\n" +
				SQLUtil.convertForSQLDateOnly(this.getAdjustmentDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getAdjustmentValue()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffID()) + ",\n" +
				SQLUtil.convertForSQL(this.getNote()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null\n" +
				"from data_adjustment da\n" +
				"where da.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
	}
	
	private String getUpdateSQL()
	{
		return	"update data_adjustment\n" +
				"set\n" +
				"adjustment_date = " + SQLUtil.convertForSQLDateOnly(this.getAdjustmentDate()) + ",\n" +
				"adjustment_value = " + SQLUtil.convertForSQL(this.getAdjustmentValue()) + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + ",\n" +
				"note = " + SQLUtil.convertForSQL(this.getNote()) + ",\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = null\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and adjustment_no = " + SQLUtil.convertForSQL(this.getAdjustmentNo()) + "\n";
	}
	
	private String getDeleteSQL()
	{
		return	"update data_adjustment\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and adjustment_no = " + SQLUtil.convertForSQL(this.getAdjustmentNo()) + "\n";
	}
	
	private String getMaxIONoSQL()
	{
		return	"select coalesce(max(adjustment_no), 0) as max_adjustment_no\n" +
				"from data_adjustment\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
	}
}

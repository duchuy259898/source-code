/*
 * DataCashIO.java
 *
 * Created on 2007/04/11, 11:11
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
public class DataCashIO
{
	private	MstShop				shop		=	new MstShop();
	private	Integer				ioNo		=	null;
	private GregorianCalendar	ioDate		=	null;
	private	MstStaff			staff		=	new MstStaff();
	private	boolean				in			=	true;
	private	Integer				value		=	0;
	private	String				useFor		=	"";
	
	/** Creates a new instance of DataCashIO */
	public DataCashIO()
	{
	}
	
	public String toString()
	{
		return	this.getUseFor();
	}

	public MstShop getShop()
	{
		return shop;
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

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	public Integer getIoNo()
	{
		return ioNo;
	}

	public void setIoNo(Integer ioNo)
	{
		this.ioNo = ioNo;
	}

	public GregorianCalendar getIoDate()
	{
		return ioDate;
	}

	public java.util.Date getIoTime()
	{
		return ioDate.getTime();
	}
	
	public void setIoDate(GregorianCalendar ioDate)
	{
		this.ioDate = ioDate;
	}

	public void setIoDate(java.sql.Date ioDate)
	{
		this.ioDate	=	new GregorianCalendar();
		this.ioDate.setTime(ioDate);
	}

	public MstStaff getStaff()
	{
		return staff;
	}
	
	public String getStaffName()
	{
		return staff.toString();
	}
	
	public Integer getStaffID()
	{
		if(staff == null)
		{
			return	null;
		}
		else
		{
			return	staff.getStaffID();
		}
	}

	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	public boolean isIn()
	{
		return in;
	}

	public void setIn(boolean in)
	{
		this.in = in;
	}

	public Integer getValue()
	{
		return value;
	}

	public Integer getInValue()
	{
		return ( in ? value : 0 );
	}
	
	public Integer getOutValue()
	{
		return( !in ? value : 0 );
	}
	
	public void setValue(Integer value)
	{
		this.value = value;
	}

	public String getUseFor()
	{
		return useFor;
	}

	public void setUseFor(String useFor)
	{
		this.useFor = useFor;
	}
	
	public void setData(DataCashIO dcio)
	{
		MstShop		msp	=	new MstShop();
		msp.setShopID(dcio.getShopID());
		this.setShop(msp);
		this.setIoNo(dcio.getIoNo());
		this.setIoDate((GregorianCalendar)dcio.getIoDate().clone());
		this.setIn(dcio.isIn());
		this.setValue(dcio.getValue());
		MstStaff	mst	=	new MstStaff();
		mst.setStaffID(dcio.getStaffID());
		this.setStaff(mst);
		this.setUseFor(dcio.getUseFor());
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstShop		msp	=	new MstShop();
		msp.setShopID(rs.getInt("shop_id"));
		this.setShop(msp);
		this.setIoNo(rs.getInt("io_no"));
		this.setIoDate(rs.getDate("io_date"));
		this.setIn(rs.getBoolean("in_out"));
		this.setValue(rs.getInt("io_value"));
		MstStaff	mst	=	new MstStaff();
		mst.setStaffID(rs.getInt("staff_id"));
		this.setStaff(mst);
		this.setUseFor(rs.getString("use_for"));
	}
	
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() <= 0 || this.getIoDate() == null || this.getValue() == 0)
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
				this.setIoNo(this.getMaxIONo(con));
				
				if(0 < this.getIoNo())
				{
					return	true;
				}
			}
		}
		
		return	false;
	}
	
	
	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null || this.getIoNo() == null)
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
			result	=	rs.getInt("max_io_no");
		}
		
		rs.close();
		
		return	result;
	}
	
	
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null || this.getIoNo() == null)
		{
			return	false;
		}
		
		return	(con.executeUpdate(this.getDeleteSQL()) == 1);
	}
	
	private String getSelectSQL()
	{
		return	"select dcio.*,\n" +
				"msp.group_id, msp.shop_name,\n" +
				"mst.staff_no, mst.staff_name1, mst.staff_name2\n" +
				"from data_cash_io dcio\n" +
				"left outer join mst_shop msp\n" +
				"on msp.shop_id = dcio.shop_id\n" +
				"left outer join mst_staff mst\n" +
				"on mst.staff_id = dcio.staff_id\n" +
				"where dcio.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and dcio.io_no = " + SQLUtil.convertForSQL(this.getIoNo()) + "\n";
	}
	
	private String getInsertSQL()
	{
		return	"insert into data_cash_io\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				"coalesce(max(io_no), 0) + 1,\n" +
				SQLUtil.convertForSQLDateOnly(this.getIoDate()) + ",\n" +
				SQLUtil.convertForSQL(this.isIn()) + ",\n" +
				SQLUtil.convertForSQL(this.getValue()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffID()) + ",\n" +
				SQLUtil.convertForSQL(this.getUseFor()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null\n" +
				"from data_cash_io dcio\n" +
				"where dcio.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
	}
	
	private String getUpdateSQL()
	{
		return	"update data_cash_io\n" +
				"set\n" +
				"io_date = " + SQLUtil.convertForSQLDateOnly(this.getIoDate()) + ",\n" +
				"in_out = " + SQLUtil.convertForSQL(this.isIn()) + ",\n" +
				"io_value = " + SQLUtil.convertForSQL(this.getValue()) + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + ",\n" +
				"use_for = " + SQLUtil.convertForSQL(this.getUseFor()) + ",\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = null\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and io_no = " + SQLUtil.convertForSQL(this.getIoNo()) + "\n";
	}
	
	private String getDeleteSQL()
	{
		return	"update data_cash_io\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and io_no = " + SQLUtil.convertForSQL(this.getIoNo()) + "\n";
	}
	
	private String getMaxIONoSQL()
	{
		return	"select coalesce(max(io_no), 0) as max_io_no\n" +
				"from data_cash_io\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
	}
}

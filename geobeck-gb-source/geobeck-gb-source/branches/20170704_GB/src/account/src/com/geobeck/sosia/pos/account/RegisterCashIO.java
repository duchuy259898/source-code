/*
 * RegisterCashIO.java
 *
 * Created on 2007/04/11, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.data.account.*;

/**
 *
 * @author katagiri
 */
public class RegisterCashIO extends ArrayList<DataCashIO>
{
	private	MstShop				shop		=	new MstShop();
	private GregorianCalendar	date		=	null;
	private	Integer				inTotal		=	0;
	private	Integer				outTotal	=	0;
	
	/**
	 * Creates a new instance of RegisterCashIO
	 */
	public RegisterCashIO()
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

	public GregorianCalendar getDate()
	{
		return date;
	}

	public void setDate(GregorianCalendar date)
	{
		this.date = date;
	}

	public Integer getInTotal()
	{
		return inTotal;
	}

	public Integer getOutTotal()
	{
		return outTotal;
	}
	
	public void load()
	{
		this.clear();
		inTotal		=	0;
		outTotal	=	0;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());
			
			while(rs.next())
			{
				DataCashIO	dcio	=	new DataCashIO();
				dcio.setData(rs);
				MstStaff	ms	=	new MstStaff();
				ms.setStaffID(rs.getInt("staff_id"));
				ms.setStaffNo(rs.getString("staff_no"));
				ms.setStaffName(0, rs.getString("staff_name1"));
				ms.setStaffName(1, rs.getString("staff_name2"));
				dcio.setStaff(ms);
				
				if(dcio.isIn())
				{
					inTotal		+=	dcio.getValue();
				}
				else
				{
					outTotal	+=	dcio.getValue();
				}
				
				this.add(dcio);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private String getLoadSQL()
	{
		return	"select dcio.*,\n" +
				"ms.staff_no, ms.staff_name1, ms.staff_name2\n" +
				"from data_cash_io dcio\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dcio.staff_id\n" +
				"where dcio.delete_date is null\n" +
				"and dcio.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dcio.io_date = " + SQLUtil.convertForSQLDateOnly(this.getDate()) + "\n" +
				"order by dcio.io_no desc\n";
	}
}

/*
 * NotMemberList.java
 *
 * Created on 2007/03/12, 9:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class NotMemberList extends ArrayList<NotMemberData>
{
	private	MstShop			shop	=	new MstShop();
	private java.util.Date	date	=	null;
	
	
	/** Creates a new instance of NotMemberList */
	public NotMemberList()
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
	
	public java.util.Date getDate()
	{
		return date;
	}
	
	public void setDate( java.util.Date date )
	{
		this.date = date;
	}
	
	public boolean load()
	{
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getNotMemberListSQL());
			
			while(rs.next())
			{
				NotMemberData	nmd	=	new NotMemberData();
				nmd.setData(rs);
				this.add(nmd);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	true;
	}
	
	
	public String getNotMemberListSQL()
	{
		return	"select *\n" +
				"from mst_customer mc\n" +
				"where mc.delete_date is null\n" +
				(shop.getShopID() == 0 ? "" :
					"and mc.shop_id = " + SQLUtil.convertForSQL(shop.getShopID())+"\n") +
				"and mc.customer_no = '0'\n" +
				( date == null ? "" : 
				"and TO_CHAR( mc.insert_date, 'YYYY-MM-DD') = " + SQLUtil.convertForSQL( String.format( "%1$tY-%1$tm-%1$td", date ) ) + "\n" ) +
				"order by mc.insert_date, customer_name1, customer_name2\n";
	}
	
	
	public boolean delete(Integer index)
	{
		boolean	result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			con.begin();
			
			try
			{
				if(this.get(index).delete(con))
				{
					result	=	true;
				}
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			
			if(result)
			{
				con.commit();
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
}

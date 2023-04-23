/*
 * RepeaterManager.java
 *
 * Created on 2007/03/29, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.report.util.*;

/**
 *
 * @author katagiri
 */
public class RepeaterManager extends ArrayList<RepeaterData>
{
	private static final String		REPORT_PATH	=	"/report/RepeaterList.jasper";
	private static final String		REPORT_NAME	=	"RepeaterList";
	
	private String				targetName		=	"";
	private String				targetList		=	"";
	private	boolean				repeater		=	true;
	private	GregorianCalendar	termFrom		=	null;
	private	GregorianCalendar	termTo			=	null;
	
	/** Creates a new instance of RepeaterManager */
	public RepeaterManager()
	{
		super();
	}

	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

	public String getTargetList()
	{
		return targetList;
	}

	public void setTargetList(String targetList)
	{
		this.targetList = targetList;
	}

	public boolean isRepeater()
	{
		return repeater;
	}

	public void setRepeater(boolean repeater)
	{
		this.repeater = repeater;
	}
	
	public String getTargetTypeName()
	{
		if(repeater)
		{
			return	"Ä—ˆ“XŽÒ";
		}
		else
		{
			return	"Ž¸‹qŒó•âŽÒ";
		}
	}

	public GregorianCalendar getTermFrom()
	{
		return termFrom;
	}

	public void setTermFrom(GregorianCalendar termFrom)
	{
		this.termFrom = termFrom;
	}

	public GregorianCalendar getTermTo()
	{
		return termTo;
	}

	public void setTermTo(GregorianCalendar termTo)
	{
		this.termTo = termTo;
	}
	
	public String getTermString()
	{
		if(this.getTermFrom() == null || this.getTermTo() == null)
		{
			return	"";
		}
		
		if(this.getTermFrom() == this.getTermTo())
		{
			return	String.format("%1$tY”N%1$tmŒŽ%1$td“ú", this.getTermFrom());
		}
		else
		{
			return	String.format("%1$tY”N%1$tmŒŽ%1$td“ú@`@%2$tY”N%2$tmŒŽ%2$td“ú",
					this.getTermFrom(), this.getTermTo());
		}
	}
	
	public boolean load()
	{
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());
			
			while(rs.next())
			{
				RepeaterData	rd	=	new RepeaterData();
				rd.setData(rs);
				this.add(rd);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			
		}
		
		return	true;
	}
	
	private String getLoadSQL()
	{
		return	"select ds.sales_date, ds.repeat_date, ds.visit_num, dsd.sales_value, mc.*\n" +
				"from (\n" +
				"select ds.shop_id, ds.slip_no, ds.sales_date, ds.customer_id, ds.visit_num,\n" +
				"min(ds_repeat.sales_date) as repeat_date\n" +
				"from data_sales ds\n" +
				"left outer join data_sales ds_repeat\n" +
				"on ds_repeat.sales_date > ds.sales_date\n" +
				"and ds_repeat.delete_date is null\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id in (" + SQLUtil.convertForSQL(this.getTargetList()) + ")\n" +
				"and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(this.getTermFrom()) + "\n" +
				"and " + SQLUtil.convertForSQLDateOnly(this.getTermTo()) + "\n" +
				"group by ds.shop_id, ds.slip_no, ds.sales_date, ds.customer_id, ds.visit_num\n" +
				") ds\n" +
				"inner join mst_customer mc\n" +
				"on mc.customer_id = ds.customer_id\n" +
				"and mc.customer_no != '0'\n" +
				"left outer join (\n" +
				"select dsd.shop_id, dsd.slip_no,\n" +
				"sum(case dsd.product_division\n" +
				"when 0 then - dsd.discount_value\n" +
				"else dsd.product_value * dsd.product_num - dsd.discount_value\n" +
				"end) as sales_value\n" +
				"from data_sales_detail dsd\n" +
				"where dsd.delete_date is null\n" +
				"group by dsd.shop_id, dsd.slip_no\n" +
				") dsd\n" +
				"on dsd.shop_id = ds.shop_id\n" +
				"and dsd.slip_no = ds.slip_no\n" +
				"where ds.repeat_date is " + (this.isRepeater() ? "not " : "") + "null\n" +
				"order by ds.sales_date, ds.visit_num, ds.slip_no\n";
	}
	
	public void print()
	{
		HashMap<String, Object>		param	=	new HashMap<String, Object>();
		
		param.put("targetName", this.getTargetName());
		param.put("targetType", this.getTargetTypeName());
		param.put("term", this.getTermString());
		
		InputStream		report		=	RepeaterManager.class.getResourceAsStream(REPORT_PATH);
		String			fileName	=	REPORT_NAME + String.format("%1$tY%1$tm%1$td%2$ts",
				this.getTermFrom(), new java.util.Date());
		
		ReportManager.exportReport(report, fileName, ReportManager.PDF_FILE, param, this);
	}
	
	public ArrayList<MstCustomer> getSelectedCustomers(Integer optimizeType)
	{
		ArrayList<MstCustomer> customers	=	new ArrayList<MstCustomer>();
		
		for(RepeaterData rd : this)
		{
			if(!rd.isSelected())
			{
				continue;
			}
			
			boolean		isAdd	=	true;
			
			switch(optimizeType)
			{
				case 0:
				case 1:
					if(rd.getPCMailAddress().equals("") &&
							rd.getCellularMailAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
				case 2:
					if(rd.getPCMailAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
				case 3:
					if(rd.getCellularMailAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
				case 4:
					if(rd.getFullAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
			}
			
			if(isAdd)
			{
				MstCustomer	mc	=	new MstCustomer(rd);
				customers.add(mc);
			}
		}
		
		return	customers;
	}
}

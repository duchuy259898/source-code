/*
 * HairRepeaterData.java
 *
 * Created on 2007/03/29, 9:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.customer.*;

/**
 *
 * @author katagiri
 */
public class HairRepeaterData extends MstCustomer
{
	private GregorianCalendar	salesDate	=	null;
	private GregorianCalendar	repeatDate	=	null;
	private Integer				visitNum	=	0;
	private Integer				salesValue	=	0;
	private	boolean				selected	=	false;
	
	/** Creates a new instance of HairRepeaterData */
	public HairRepeaterData()
	{
	}

	public GregorianCalendar getSalesDate()
	{
		return salesDate;
	}

	public void setSalesDate(GregorianCalendar salesDate)
	{
		this.salesDate = salesDate;
	}
	
	public String getSalesDateString()
	{
		if(salesDate == null)
		{
			return	"";
		}
		else
		{
			return	String.format("%1$tY/%1$tm/%1$td", salesDate);
		}
	}

	public GregorianCalendar getRepeatDate()
	{
		return repeatDate;
	}

	public void setRepeatDate(GregorianCalendar repeatDate)
	{
		this.repeatDate = repeatDate;
	}
	
	public String getRepeatDateString()
	{
		if(repeatDate == null)
		{
			return	"";
		}
		else
		{
			return	String.format("%1$tY/%1$tm/%1$td", repeatDate);
		}
	}

	public Integer getVisitNum()
	{
		return visitNum;
	}

	public void setVisitNum(Integer visitNum)
	{
		this.visitNum = visitNum;
	}

	public Integer getSalesValue()
	{
		return salesValue;
	}

	public void setSalesValue(Integer salesValue)
	{
		this.salesValue = salesValue;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);
		
		if(rs.getDate("sales_date") == null)
		{
			this.setSalesDate(null);
		}
		else
		{
			GregorianCalendar	temp	=	new GregorianCalendar();
			temp.setTime(rs.getDate("sales_date"));
			this.setSalesDate(temp);
		}
		
		if(rs.getDate("repeat_date") == null)
		{
			this.setRepeatDate(null);
		}
		else
		{
			GregorianCalendar	temp	=	new GregorianCalendar();
			temp.setTime(rs.getDate("repeat_date"));
			this.setRepeatDate(temp);
		}
		
		this.setVisitNum(rs.getInt("visit_num"));
		this.setSalesValue(rs.getInt("sales_value"));
	}
}

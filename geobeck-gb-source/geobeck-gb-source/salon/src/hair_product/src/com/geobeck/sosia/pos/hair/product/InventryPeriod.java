/*
 * InventryPeriod.java
 *
 * Created on 2008/09/12, 10:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author s_matsumura
 */
public class InventryPeriod
{
	protected Integer	shop		=	null;
	protected Integer	cutoffday	=   null;
	protected Integer	inventoryDivision	=	null;
	
	/** Creates a new instance of InventryPeriod */
	public InventryPeriod()
	{
		super();
	}
	
	public Integer getShop()
	{
		return shop;
	}
	
	public void setShop(Integer shop)
	{
		this.shop = shop;
	}
	
	public Integer getcutoffday()
	{
		return cutoffday;
	}
	
	public void setcutoffday(Integer cutoff_day)
	{
		this.cutoffday = cutoff_day;
	}

	public Integer getInventoryDivision()
	{
		return this.inventoryDivision;
	}

	public void setInventoryDivision(Integer inventoryDivision)
	{
		this.inventoryDivision = inventoryDivision;
	}
	
	public DateRange[] getInventrydate(ConnectionWrapper con, boolean containNow) throws SQLException
	{
		List<DateRange> buf = new ArrayList<DateRange>();
		
		Date prevDate = null;
		Timestamp[] timestampArray = getInventoryDates(con);
		for (Timestamp ts : timestampArray)
		{
			if (prevDate == null)
			{
				DateRange dateRange = new DateRange();
				dateRange.setTo(ts);
				
				buf.add(dateRange);
			}
			else
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(prevDate);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				
				DateRange dateRange = new DateRange();
				dateRange.setFrom(cal.getTime());
				dateRange.setTo(ts);
				
				buf.add(dateRange);
			}
			
			prevDate = ts;
		}
		
		if (containNow)
		{
			if (prevDate != null)
			{
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				String strNow = fmt.format(new Date());
				String strPrev = fmt.format(prevDate);
				
				if (strNow.compareTo(strPrev) > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTime(prevDate);
					cal.add(Calendar.DAY_OF_MONTH, 1);
					
					DateRange dateRange = new DateRange();
					dateRange.setFrom(cal.getTime());
					
					buf.add(dateRange);
				}
			}
		}
		
		Collections.reverse(buf);
		return buf.toArray(new DateRange[buf.size()]);
	}
	
	public DateRange[] getStoreShipdate(ConnectionWrapper con, boolean containNow) throws SQLException
	{
		LinkedHashSet<DateRange> buf = new LinkedHashSet<DateRange>();
		
		// 現在日付の属する締め日の初めを取得(まだ締まっていない)
		Date uncutoffFrom = calcDateRange(new Date(), this.getcutoffday()).getFrom();
		
		// 「締め日+1～」を作成
		if (containNow)
		{
			DateRange dateRange = new DateRange();
			dateRange.setFrom(uncutoffFrom);
			buf.add(dateRange);
		}
		
		Timestamp[] timestampArray = getShipStoreDates(con);
		for (Timestamp ts : timestampArray)
		{
			DateRange dateRange = new DateRange();
			dateRange = calcDateRange(ts, this.getcutoffday());
			if (!uncutoffFrom.equals(dateRange.getFrom()))
			{
				buf.add(dateRange);
			}
		}
		
		return buf.toArray(new DateRange[buf.size()]);
	}
	
	//確認日の最大
	public Timestamp getMaxInventryPeriod(ConnectionWrapper con) throws SQLException
	{
		Timestamp		result	=	null;
		
		if(con == null)	return	result;
		
		ResultSetWrapper	rs	=	con.executeQuery(getMAXPeriodSelectSQL());
		
		if(rs.next())
		{
			result	=	rs.getTimestamp("max_inventory_date");
		}
		
		rs.close();
		
		return	result;
	}
	
	public String getMAXPeriodSelectSQL()
	{
		return	"select MAX(inventory_date) as max_inventory_date\n" +
			"from data_inventory\n" +
			"Where shop_id =" + SQLUtil.convertForSQL(this.getShop());
	}
	
	//確認日の最小
	public Timestamp getMINInventryPeriod(ConnectionWrapper con) throws SQLException
	{
		Timestamp		result	=	null;
		
		if(con == null)	return	result;
		//System.err.println(getMINPeriodSelectSQL());
		ResultSetWrapper	rs	=	con.executeQuery(getMINPeriodSelectSQL());
		
		if(rs.next())
		{
			result	=	rs.getTimestamp("min_inventory_date");
		}
		
		rs.close();
		
		return	result;
	}
	
	public String getMINPeriodSelectSQL()
	{
		return	"select MIN(inventory_date) as min_inventory_date\n" +
			"from data_inventory\n" +
			"Where shop_id =" + SQLUtil.convertForSQL(this.getShop());
	}
	
	public Timestamp[] getInventoryDates(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	null;
		//System.err.println(getMINPeriodSelectSQL());
		ResultSetWrapper	rs	=	con.executeQuery(getInventoryDatesSQL());
		
		List<Timestamp> buf = new ArrayList<Timestamp>();
		while(rs.next())
		{
			buf.add(rs.getTimestamp("inventory_date"));
		}
		
		rs.close();
		
		return	buf.toArray(new Timestamp[buf.size()]);
	}
	
	public String getInventoryDatesSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select distinct inventory_date from data_inventory\n");
		buf.append("where shop_id = ").append(SQLUtil.convertForSQL(this.getShop())).append("\n")
			.append("and delete_date is null\n");
		if (getInventoryDivision() != null)
		{
			/*
			buf.append("and inventory_id in (")
				.append("select distinct inventory_id from data_inventory_detail\n")
				.append("where inventory_division = ").append(this.getInventoryDivision()).append("\n")
				.append("and delete_date is null\n")
				.append(")\n");
			 */
			buf.append("and inventory_division = ").append(SQLUtil.convertForSQL(this.getInventoryDivision())).append("\n");
		}
		buf.append("order by inventory_date\n");
		return new String(buf);
	}
	
	public Timestamp[] getShipStoreDates(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	null;
		//System.err.println(getMINPeriodSelectSQL());
		ResultSetWrapper	rs	=	con.executeQuery(getShipStoreDatesSQL());
		
		List<Timestamp> buf = new ArrayList<Timestamp>();
		while(rs.next())
		{
			buf.add(rs.getTimestamp("storeship_date"));
		}
		
		rs.close();
		
		return	buf.toArray(new Timestamp[buf.size()]);
	}
	
	public String getShipStoreDatesSQL()
	{
		return
			"select store_date as storeship_date from data_slip_store\n" +
			"where shop_id =" + SQLUtil.convertForSQL(this.getShop()) + "\n" +
			"and delete_date is null\n" +
			"union\n" +
			"select ship_date as storeship_date from data_slip_ship\n" +
			"where shop_id =" + SQLUtil.convertForSQL(this.getShop()) + "\n" +
			"and delete_date is null\n" +
			"order by storeship_date desc";
	}
	
	private DateRange calcDateRange(Date date, int cutoffday)
	{
		DateRange dateRange = new DateRange();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day <= cutoffday)
		{
			Calendar fromcal = Calendar.getInstance();
			fromcal.setLenient(false);
			try
			{
				fromcal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, cutoffday);
				fromcal.add(Calendar.DAY_OF_MONTH, 1);
				dateRange.setFrom(fromcal.getTime());
			}
			catch (IllegalArgumentException e)
			{
				fromcal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
				dateRange.setFrom(fromcal.getTime());
			}
			
			Calendar tocal = Calendar.getInstance();
			tocal.setLenient(false);
			try
			{
				tocal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cutoffday);
				dateRange.setTo(tocal.getTime());
			}
			catch (IllegalArgumentException e)
			{
				tocal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				dateRange.setTo(tocal.getTime());
			}
		}
		else
		{
			Calendar fromcal = Calendar.getInstance();
			try
			{
				fromcal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cutoffday);
				fromcal.add(Calendar.DAY_OF_MONTH, 1);
				dateRange.setFrom(fromcal.getTime());
			}
			catch (IllegalArgumentException e)
			{
				fromcal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
				dateRange.setFrom(fromcal.getTime());
			}
			
			Calendar tocal = Calendar.getInstance();
			tocal.setLenient(false);
			try
			{
				tocal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cutoffday);
				dateRange.setTo(tocal.getTime());
			}
			catch (IllegalArgumentException e)
			{
				tocal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				dateRange.setTo(tocal.getTime());
			}
		}
		
		return dateRange;
	}
}

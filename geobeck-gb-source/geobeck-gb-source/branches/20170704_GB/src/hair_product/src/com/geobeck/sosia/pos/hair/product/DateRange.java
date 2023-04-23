/*
 * DateRange.java
 *
 * Created on 2008/09/13, 0:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日付範囲をあらわすクラス
 * @author mizukawa
 */
public class DateRange
{
	private static final DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	/** 日付FROM */
	private Date from;
	/** 日付TO */
	private Date to;
	
	public void setFrom(Date from)
	{
		this.from  = from;
	}
	
	public Date getFrom()
	{
		return this.from;
	}
	
	public void setTo(Date to)
	{
		this.to = to;
	}
	
	public Date getTo()
	{
		return this.to;
	}

	/**
	 * 指定された年月の初日から末日を日付範囲として設定する。
	 * @param year 年
	 * @param month Calendar.MONTHの値
	 */
	public void setMonth(int year, int month)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.set(year, month, 1, 0, 0, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		
		setFrom(cal1.getTime());
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(year, month, cal1.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		
		setTo(cal2.getTime());
	}

	public void setMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		setMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof DateRange))
		{
			return false;
		}
		
		DateRange dr = (DateRange) obj;
		
		// Fromのチェック
		if (this.from == null && dr.getFrom() != null)
		{
			return false;
		}
		
		if (this.from != null && !this.from.equals(dr.getFrom()))
		{
			return false;
		}
		
		// Toのチェック
		if (this.to == null && dr.getTo() != null)
		{
			return false;
		}
		
		if (this.to != null && !this.to.equals(dr.getTo()))
		{
			return false;
		}
		
		return true;
	}

	public int hashCode()
	{
		int hash = 1;
		hash = hash * 31 + (from == null ? 0 : from.hashCode());
		hash = hash * 31 + (to == null ? 0 : to.hashCode());
		return hash;
	}

	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		if (this.from != null)
		{
			buf.append(formatter.format(this.from));
		}
		else
		{
			buf.append("               ");
		}

		buf.append("～");

		if (this.to != null)
		{
			buf.append(formatter.format(this.to));
		}

		return new String(buf);
	}

	public int diffDays()
	{
		long dateFromValue = from.getTime();
		long dateToValue = to.getTime();
		long unit_day = 1000 * 60 * 60 * 24;
		long diffDays = (dateToValue - dateFromValue) / unit_day;

		return (int)diffDays;
	}

	public static int diffDays(Date dateFrom, Date dateTo)
	{
		if (dateFrom == null)
		{
			return 0;
		}

		long dateFromValue = dateFrom.getTime();
		long dateToValue = dateTo.getTime();
		long unit_day = 1000 * 60 * 60 * 24;
		long diffDays = (dateToValue - dateFromValue) / unit_day;
		
		return (int)diffDays;
	}

	public static int compareToDateOnly(Date dateBase, Date dateTo)
	{
		Calendar calBase = Calendar.getInstance();
		Calendar calTo = Calendar.getInstance();

		calBase.setTime(dateBase);
		calTo.setTime(dateTo);

		//時刻を除いて比較する
		calBase.set(Calendar.HOUR_OF_DAY, 0);
		calBase.set(Calendar.MINUTE, 0);
		calBase.set(Calendar.SECOND, 0);
		calBase.set(Calendar.MILLISECOND, 0);
		calTo.set(Calendar.HOUR_OF_DAY, 0);
		calTo.set(Calendar.MINUTE, 0);
		calTo.set(Calendar.SECOND, 0);
		calTo.set(Calendar.MILLISECOND, 0);

		return calBase.compareTo(calTo);
	}
}

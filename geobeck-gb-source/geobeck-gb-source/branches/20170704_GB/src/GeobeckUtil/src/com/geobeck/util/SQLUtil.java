/*
 * SQLUtil.java
 *
 * Created on 2006/04/24, 13:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.sql.*;
import java.util.*;

import com.ibm.icu.util.*;

/**
 * ＳＱＬユーティリティ
 * @author katagiri
 */
public class SQLUtil
{
	
	/**
	 * コンストラクタ
	 */
	public SQLUtil()
	{
	}
	
	
	/**
	 * Boolean型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Integer型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Boolean value)
	{
		if(value == null)	return	"null";
		else	return	(value ? "'t'" : "'f'");
	}
	
	/**
	 * Integer型の値を、SQL文で使用できる文字列に変換する。
	 * 
	 * @return 変換された文字列
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @param value Integer型の値
	 */
	public static String convertForSQL(Boolean value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	(value ? "'t'" : "'f'");
	}
	
	/**
	 * Integer型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Short型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Short value)
	{
		if(value == null)	return	"null";
		else	return	value.toString();
	}
	
	/**
	 * Short型の値を、SQL文で使用できる文字列に変換する。
	 * 
	 * @return 変換された文字列
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @param value Short型の値
	 */
	public static String convertForSQL(Short value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	value.toString();
	}
	
	/**
	 * Integer型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Integer型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Integer value)
	{
		if(value == null)	return	"null";
		else	return	value.toString();
	}
	
	/**
	 * Integer型の値を、SQL文で使用できる文字列に変換する。
	 * 
	 * @return 変換された文字列
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @param value Integer型の値
	 */
	public static String convertForSQL(Integer value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	value.toString();
	}
	
	
	/**
	 * Long型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Long型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Long value)
	{
		if(value == null)	return	"null";
		else	return	value.toString();
	}
	
	/**
	 * Long型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Long型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Long value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	value.toString();
	}
	
	
	/**
	 * Double型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Double型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Double value)
	{
		if(value == null)	return	"null";
		else	return	value.toString();
	}
	
	/**
	 * Double型の値を、SQL文で使用できる文字列に変換する。
	 * @param value Double型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(Double value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	value.toString();
	}
	
	
	/**
	 * String型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(String value)
	{
		if(value == null)	return	"null";
		else	return	"'" + value.toString().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\") + "'";
	}
	
	/**
	 * String型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(String value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	"'" + value.toString().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\") + "'";
	}
	
	
	/**
	 * java.util.Date型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(java.util.Date value)
	{
		if(value == null)	return	"null";
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd HH:mm") + "'";
	}
	
	/**
	 * java.util.Date型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(java.util.Date value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd HH:mm") + "'";
	}
	
	/**
	 * java.util.Date型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(java.util.Date value)
	{
		if(value == null)	return	"null";
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd") + "'";
	}
	
	/**
	 * java.util.Date型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(java.util.Date value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd") + "'";
	}
	
	
	/**
	 * java.sql.Date型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(java.sql.Date value)
	{
		if(value == null)	return	"null";
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd HH:mm") + "'";
	}
	
	/**
	 * java.sql.Date型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(java.sql.Date value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd HH:mm") + "'";
	}
	
	/**
	 * java.sql.Date型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(java.sql.Date value)
	{
		if(value == null)	return	"null";
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd") + "'";
	}
	
	/**
	 * java.sql.Date型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(java.sql.Date value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	"'" + DateUtil.format(value, "yyyy/MM/dd") + "'";
	}
	
	
	/**
	 * java.util.Calendar型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(java.util.Calendar value)
	{
		if(value == null)	return	"null";
		else	return	SQLUtil.convertForSQL(value.getTime());
	}
	
	/**
	 * java.util.Calendar型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(java.util.Calendar value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	SQLUtil.convertForSQL(value.getTime());
	}
	
	/**
	 * java.util.Calendar型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(java.util.Calendar value)
	{
		if(value == null)	return	"null";
		else	return	SQLUtil.convertForSQLDateOnly(value.getTime());
	}
	
	/**
	 * java.util.Calendar型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(java.util.Calendar value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	SQLUtil.convertForSQLDateOnly(value.getTime());
	}
	
	
	/**
	 * JapaneseCalendar型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQL(JapaneseCalendar value)
	{
		if(value == null)	return	"null";
		else	return	SQLUtil.convertForSQL(value.getTime());
	}
	
	/**
	 * JapaneseCalendar型の値を、SQL文で使用できる文字列に変換する。
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQL(JapaneseCalendar value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	SQLUtil.convertForSQL(value.getTime());
	}
	
	/**
	 * JapaneseCalendar型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(JapaneseCalendar value)
	{
		if(value == null)	return	"null";
		else	return	SQLUtil.convertForSQLDateOnly(value.getTime());
	}
	
	/**
	 * JapaneseCalendar型の値を、SQL文で使用できる文字列に変換する。（日付のみ）
	 * @param value String型の値
	 * @param nullValue valueがnullの場合置き換える文字列
	 * @return 変換された文字列
	 */
	public static String convertForSQLDateOnly(JapaneseCalendar value, String nullValue)
	{
		if(value == null)	return	nullValue;
		else	return	SQLUtil.convertForSQLDateOnly(value.getTime());
	}
	
	public static String encodeSpecialCharacters(String value)
	{
		value	=	value.replaceAll("\\\\", "\\\\\\\\");
		value	=	value.replaceAll("\u2014", "\u2015");
		value	=	value.replaceAll("\u301C", "\uFF5E");
		value	=	value.replaceAll("\u2016", "\u2225");
		value	=	value.replaceAll("\u2212", "\uFF0D");
		value	=	value.replaceAll("\u00A2", "\uFFE0");
		value	=	value.replaceAll("\u00A3", "\uFFE1");
		value	=	value.replaceAll("\u00AC", "\uFFE2");
		
		return	value;
	}
	
	public static String decodeSpecialCharacters(String value)
	{
		value	=	value.replaceAll("\u2015", "\u2014");
		value	=	value.replaceAll("\uFF5E", "\u301C");
		value	=	value.replaceAll("\u2225", "\u2016");
		value	=	value.replaceAll("\uFF0D", "\u2212");
		value	=	value.replaceAll("\uFFE0", "\u00A2");
		value	=	value.replaceAll("\uFFE1", "\u00A3");
		value	=	value.replaceAll("\uFFE2", "\u00AC");
		
		return	value;
	}
}

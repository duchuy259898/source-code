/*
 * CheckUtil.java
 *
 * Created on 2004/04/27, 15:00
 */
package com.geobeck.util;
import	java.util.*;
/**
 * データのチェックを行うクラス
 */
public class CheckUtil
{
	/**
	 * 引数なしのコンストラクタ
	 */	
	public CheckUtil()
	{
	}
	
	/**
	 * 文字列の長さをチェックする。
	 * @param value チェックする文字列
	 * @param length 最長の文字数
	 * @return valueの長さがlength以下ならtrue、lengthより長ければfalse
	 */	
	public static boolean checkStringLength(String value, int length)
	{
		if(length < value.length())
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * 文字列の長さをチェックする。
	 * @param value チェックする文字列
	 * @param length_min 最短の文字数
	 * @param length_max 最長の文字数
	 * @return valueの長さがlength_min以上length_max以下ならtrue、それ以外ならばfalse
	 */	
	public static boolean checkStringLength(String value, int length_min, int length_max)
	{
		if(value.length() < length_min || length_max < value.length())
		{
			if(length_min == length_max)
			{
				return	false;
			}
			else
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	/**
	 * 半角文字列かチェックする。
	 * @param value チェックする文字列
	 * @return valueの文字が全て半角ならばtrue、半角以外の文字が含まれていればfalse
	 */	
	public static boolean is1ByteChars(String value)
	{
		return	value.matches("[ -~｡-ﾟ]*");
	}
	
	/*	全角チェック
	 *	value			チェックする文字列
	 */
	/**
	 * 全角文字列かチェックする。
	 * @param value チェックする文字列
	 * @return valueの文字が全て全角ならばtrue、全角以外の文字が含まれていればfalse
	 */	
	public static boolean is2ByteChars(String value)
	{
		return	value.matches("[^ -~｡-ﾟ]*");
	}
	
	/**
	 * 数値かチェックする。
	 * @param value チェックする文字列
	 * @return valueが数値ならばtrue、数値意外ならfalseを返す。
	 */	
	public static boolean isNumeric(String value)
	{
		return	value.matches("-?[0-9]*[.]?[0-9]+");
	}
	
	/**
	 * ナンバーかチェックする。
	 * @param value チェックする文字列
	 * @return valueが0から9の文字列ならばtrue、0から9の文字列以外ならfalseを返す。
	 */	
	public static boolean isNumber(String value)
	{
		return	value.matches("[0-9]+");
	}
	
	/**
	 * 数値の範囲をチェックする。
	 * @param value チェックする数値。
	 * @param min_value 最小値。
	 * @param max_value 最大値。
	 * @return valueがmin_value以上max_value以下ならtrue、それ以外ならfalseを返す。
	 */	
	public static boolean checkRange(byte value, byte min_value, byte max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * 数値の範囲をチェックする。
	 * @param value チェックする数値。
	 * @param min_value 最小値。
	 * @param max_value 最大値。
	 * @return valueがmin_value以上max_value以下ならtrue、それ以外ならfalseを返す。
	 */	
	public static boolean checkRange(double value, double min_value, double max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * 数値の範囲をチェックする。
	 * @param value チェックする数値。
	 * @param min_value 最小値。
	 * @param max_value 最大値。
	 * @return valueがmin_value以上max_value以下ならtrue、それ以外ならfalseを返す。
	 */	
	public static boolean checkRange(float value, float min_value, float max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * 数値の範囲をチェックする。
	 * @param value チェックする数値。
	 * @param min_value 最小値。
	 * @param max_value 最大値。
	 * @return valueがmin_value以上max_value以下ならtrue、それ以外ならfalseを返す。
	 */	
	public static boolean checkRange(int value, int min_value, int max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * 数値の範囲をチェックする。
	 * @param value チェックする数値。
	 * @param min_value 最小値。
	 * @param max_value 最大値。
	 * @return valueがmin_value以上max_value以下ならtrue、それ以外ならfalseを返す。
	 */	
	public static boolean checkRange(long value, long min_value, long max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * 数値の範囲をチェックする。
	 * @param value チェックする数値。
	 * @param min_value 最小値。
	 * @param max_value 最大値。
	 * @return valueがmin_value以上max_value以下ならtrue、それ以外ならfalseを返す。
	 */	
	public static boolean checkRange(short value, short min_value, short max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * 日付かチェックする。
	 * 「YYYY/MM/DD」、「YYYYMMDD」両対応
	 * @param value チェックする日付の文字列
	 * @return valueが日付の文字列ならばtrue、日付以外の文字列ならばfalseを返す。
	 */	
	public static boolean isDate(String value)
	{
		//YYYY/MM/DD
		if(value.matches("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}"))
		{
			return	isDateSlash(value);
		}
		//YYYYMMDD
		else if(value.matches("[0-9]{8}"))
		{
			return	isDateNonSlash(value);
		}
		
		return	false;
	}
	
	/**
	 * 日付かチェックする。
	 * 「YYYY/MM/DD」
	 * @param value チェックする日付の文字列
	 * @return valueが日付の文字列ならばtrue、日付以外の文字列ならばfalseを返す。
	 */	
	public static boolean isDateSlash(String value)
	{
		//文字列の形式チェック
		if(value.matches("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}"))
		{
			try
			{
				int		year	=	Integer.parseInt(value.substring(0, 4));
				int		month	=	Integer.parseInt(value.substring(5, 7));
				int		date	=	Integer.parseInt(value.substring(8, 10));
				return	isDate(year, month , date);
			}
			catch(Exception e)
			{
			}
		}
		
		return	false;
	}
	
	/**
	 * 日付かチェックする。
	 * 「YYYYMMDD」
	 * @param value チェックする日付の文字列
	 * @return valueが日付の文字列ならばtrue、日付以外の文字列ならばfalseを返す。
	 */	
	public static boolean isDateNonSlash(String value)
	{
		//文字列の形式チェック
		if(value.matches("[0-9]{8}"))
		{
			try
			{
				int		year	=	Integer.parseInt(value.substring(0, 4));
				int		month	=	Integer.parseInt(value.substring(4, 6));
				int		date	=	Integer.parseInt(value.substring(6, 8));
				return	isDate(year, month , date);
			}
			catch(Exception e)
			{
			}
		}
		
		return	false;
	}
	
	/**
	 * 日付かチェックする。
	 * @param year チェックする年。
	 * @param month チェックする月。
	 * @param date チェックする日。
	 * @return 受け取った年月日が日付として妥当ならばtrue、妥当でなければfalseを返す。
	 */	
	public static boolean isDate(int year, int month, int date)
	{
		try
		{
			month	=	month - 1;
			
			GregorianCalendar	cal	=	new GregorianCalendar();
			
			cal.setLenient(false);
			cal.set(year, month - 1, date);
			
			//不正な日付がセットされた場合、例外が発生する
			cal.getTime();
			
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
}

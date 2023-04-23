/*
 * FormatterCreator.java
 *
 * Created on 2006/04/19, 9:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.text.*;
import java.text.*;

/**
 * Formatterを作成するクラス
 * @author katagiri
 */
public class FormatterCreator
{
	
	/**
	 * コンストラクタ
	 */
	public FormatterCreator()
	{
	}
	
	
	/**
	 * MaskFormatterを作成する。
	 * @param formatString 書式を設定するマスク
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			formatter	=	new MaskFormatter(formatString);
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	
	/**
	 * MaskFormatterを作成する。
	 * @param formatString 書式を設定するマスク
	 * @param validCharacters 使用可能な文字の正規表現
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString,
														String validCharacters)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			if(formatString != null)
			{
				formatter	=	new MaskFormatter(formatString);
			}
			else
			{
				formatter	=	new MaskFormatter();
			}
			
			if(validCharacters != null)
			{
				formatter.setValidCharacters(validCharacters);
			}
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	
	/**
	 * MaskFormatterを作成する。
	 * @param formatString 書式を設定するマスク
	 * @param validCharacters 使用可能な文字の正規表現
	 * @param placeholder 値がマスクを完全に埋めていない場合に使用する文字列
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString,
														String validCharacters,
														String placeholder)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			if(formatString != null)
			{
				formatter	=	new MaskFormatter(formatString);
			}
			else
			{
				formatter	=	new MaskFormatter();
			}
			
			if(validCharacters != null)
			{
				formatter.setValidCharacters(validCharacters);
			}
			
			if(placeholder != null)
			{
				formatter.setPlaceholder(placeholder);
			}
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	
	/**
	 * MaskFormatterを作成する。
	 * @param formatString 書式を設定するマスク
	 * @param validCharacters 使用可能な文字の正規表現
	 * @param placeholder 値がマスクを完全に埋めていない場合に使用する文字列
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString,
														String validCharacters,
														char placeholder)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			if(formatString != null)
			{
				formatter	=	new MaskFormatter(formatString);
			}
			else
			{
				formatter	=	new MaskFormatter();
			}
			
			if(validCharacters != null)
			{
				formatter.setValidCharacters(validCharacters);
			}
			
			formatter.setPlaceholderCharacter(placeholder);
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	/**
	 * 時刻用MaskFormatterを作成する。
	 * @return 時刻用MaskFormatter
	 */
	public static MaskFormatter createTimeFormatter()
	{
		return	FormatterCreator.createMaskFormatter("##:##",
												"0123456789",
												"0".charAt(0));
	}
	
	/**
	 * 郵便番号用MaskFormatterを作成する。
	 * @return 郵便番号用MaskFormatter
	 */
	public static MaskFormatter createPostalCodeFormatter()
	{
		return	FormatterCreator.createMaskFormatter("###-####",
												"0123456789",
												"_".charAt(0));
	}
	
	/**
	 * 電話番号用MaskFormatterを作成する。
	 * @return 電話番号用MaskFormatter
	 */
	public static MaskFormatter createPhoneNumberFormatter()
	{
		return	FormatterCreator.createMaskFormatter(null,
												"0123456789-#*");
	}
	
	
	/**
	 * NumberFormatterを作成する。
	 * @param maximumIntegerDigits 整数部の最大桁数
	 * @param maximumFractionDigits 小数部の最大桁数
	 * @param minimumValue 最小値
	 * @param maximumValue 最大値
	 * @return NumberFormatter
	 */
	public static NumberFormatter createNumberFormatter(
			int maximumIntegerDigits,
			int maximumFractionDigits,
			double minimumValue,
			double maximumValue)
	{
		DecimalFormat	df	=	new DecimalFormat();
		df.setMaximumIntegerDigits(maximumIntegerDigits);
		df.setMaximumFractionDigits(maximumFractionDigits);
		NumberFormatter	nf	=	new NumberFormatter(df);
		nf.setMinimum(minimumValue);
		nf.setMaximum(maximumValue);
		
		return	nf;
	}
	
	
	/**
	 * 整数値のみのNumberFormatterを作成する。
	 * @param maximumIntegerDigits 整数部の最大桁数
	 * @param minimumValue 最小値
	 * @param maximumValue 最大値
	 * @return 整数値のみのNumberFormatter
	 */
	public static NumberFormatter createNumberFormatter(
			int maximumIntegerDigits,
			int minimumValue,
			int maximumValue)
	{
		DecimalFormat	df	=	new DecimalFormat();
		df.setMinimumIntegerDigits(0);
		df.setMaximumIntegerDigits(maximumIntegerDigits);
		NumberFormatter	nf	=	new NumberFormatter(df);
		nf.setMinimum(minimumValue);
		nf.setMaximum(maximumValue);
		
		return	nf;
	}
	
	/**
	 * 整数値のみのNumberFormatterを作成する。
	 * @param maximumIntegerDigits 整数部の最大桁数
	 * @param minimumValue 最小値
	 * @param maximumValue 最大値
	 * @return 整数値のみのNumberFormatter
	 */
	public static NumberFormatter createNumberFormatter(
			int maximumIntegerDigits,
			long minimumValue,
			long maximumValue)
	{
		DecimalFormat	df	=	new DecimalFormat();
		df.setMinimumIntegerDigits(0);
		df.setMaximumIntegerDigits(maximumIntegerDigits);
		NumberFormatter	nf	=	new NumberFormatter(df);
		nf.setMinimum(minimumValue);
		nf.setMaximum(maximumValue);
		
		return	nf;
	}
	
	/**
	 * DateFormatterを作成する。
	 * @param pattern 日付の書式
	 * @return DateFormatter
	 */
	public static DateFormatter createDateFormatter(
			String pattern)
	{
		DateFormat	df	=	new SimpleDateFormat(pattern);

		return	new DateFormatter(df);
	}
	
}

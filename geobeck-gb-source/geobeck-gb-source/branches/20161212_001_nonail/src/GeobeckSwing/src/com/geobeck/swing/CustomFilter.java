/*
 * CustomFilter.java
 *
 * Created on 2006/07/05, 12:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author katagiri
 */
public class CustomFilter extends DocumentFilter
{
	/**
	 * 数値の正規表現
	 */
	public static final String	NUMBER			=	"0-9";
	
	/**
	 * 整数の正規表現
	 */
	public static final String	INTEGER	=	"0-9-";
	
	/**
	 * 数値の正規表現
	 */
	public static final String	NUMERIC			=	"0-9-\\.";
	
	/**
	 * 大文字アルファベットの正規表現
	 */
	public static final String	ALPHABET_BIG	=	"A-Z";
	/**
	 * 小文字アルファベットの正規表現
	 */
	public static final String	ALPHABET_SMALL	=	"a-z";
	/**
	 * アルファベットの正規表現
	 */
	public static final String	ALPHABET		=	"a-zA-Z";
	
	/**
	 * 英数字の正規表現（ハイフン含む）
	 */
	public static final String	ALPHAMERIC		=	"0-9a-zA-Z-";
	
	/**
	 * 記号の正規表現
	 */
	public static final String	SYMBOL			=	"\\p{Punct}";
	
	/**
	 * 半角文字の正規表現
	 */
	public static final String	HALF_CHAR		=	"\\p{Graph}";
	
	/**
	 * 電話番号で使える文字の正規表現
	 */
	public static final String	PHONE_NUMBER	=	"0-9#\\*";
	
	/**
	 * メールアドレスで使える文字の正規表現
	 */
//	public static final String	MAIL_ADDRESS	=	"\\p{Alnum}!\\$\\%\\&\\*\\+-\\./=\\?@\\^\\_\\~";
	public static final String	MAIL_ADDRESS	=	"\\p{Alnum}-\\._@";
	
	/**
	 * 最大文字数
	 */
	protected	int		limit;
	
	/**
	 * 使用可能な文字の正規表現
	 */
	protected	String		validValues		=	"";

	/**
	 * 最大文字数を取得する。
	 * @return 最大文字数
	 */
	public int getLimit()
	{
		return limit;
	}

	/**
	 * 最大文字数をセットする。
	 * @param limit 最大文字数
	 */
	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	/**
	 * 使用可能な文字の正規表現を取得する。
	 * @return 使用可能な文字の正規表現
	 */
	public String getValidValues()
	{
		return validValues;
	}

	/**
	 * 使用可能な文字の正規表現をセットする。
	 * @param validValues 使用可能な文字の正規表現
	 */
	public void setValidValues(String validValues)
	{
		this.validValues = validValues;
	}
	
	/**
	 * コンストラクタ
	 * @param limit 最大文字数
	 */
	public CustomFilter(int limit)
	{
		this.limit	=	limit;
	}
	
	/**
	 * コンストラクタ
	 * @param validValues 使用可能な文字の正規表現
	 */
	public CustomFilter(String validValues)
	{
		this.validValues	=	validValues;
	}
	
	/**
	 * コンストラクタ
	 * @param limit 最大文字数
	 * @param validValues 使用可能な文字の正規表現
	 */
	public CustomFilter(int limit, String validValues)
	{
		this.limit	=	limit;
		this.validValues	=	validValues;
	}
	
	/**
	 * offset から offset + length までのテキストの領域を削除し、string に置き換えます。
	 * @param fb Document の変更に使用する FilterBypass
	 * @param offset Document での位置
	 * @param length 削除するテキストの長さ
	 * @param string 挿入するテキスト。null の場合、テキストは挿入されない
	 * @param attr 挿入されたテキストの属性を示す AttributeSet。null も可
	 * @throws javax.swing.text.BadLocationException 
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offset,
			int length, String string, AttributeSet attr) throws BadLocationException
	{
		//挿入するテキストがnullの場合、処理を抜ける
		if(string == null)
		{
			super.replace(fb, offset, length, string, attr);
			return;
		}
		
		Document	doc			=	fb.getDocument();
		int			totalLen	=	doc.getLength();
		
		//入力可能な文字以外を削除する
		string	=	this.checkString(string);
		
		int			len			=	string.length();
		
		//最大文字数が0の場合
		if(limit == 0)
		{
			super.replace(fb, offset, length, string, attr);
		}
		//
		else if(limit <= totalLen - length)
		{
			if(0 <= limit - totalLen + length)
			{
				string	=	string.substring(0, limit - totalLen + length);
			}
			else
			{
				string	=	"";
			}
			super.replace(fb, offset, length, string, attr);
		}
		//
		else if(limit <= (offset + len))
		{
			string	=	string.substring(0, limit - totalLen + length);
			super.replace(fb, offset, length, string, attr);
		}
		//
		else if(limit <= (offset + len + totalLen - offset - length))
		{
			string	=	string.substring(0, limit - totalLen + length);
			super.replace(fb, offset, length, string, attr);
		}
		//
		else
		{
			super.replace(fb, offset, length, string, attr);
		}
	}
	
	/**
	 * 入力可能な文字かチェックする。
	 * @param string チェックする文字
	 * @return 入力可能な文字のみ
	 */
	private String checkString(String string)
	{
		if(validValues.equals(""))
				return	string;
		
		StringBuffer	sb	=	new StringBuffer();
		
		for(int i = 0; i < string.length(); i ++)
		{
			char	ch	=	string.charAt(i);
			
			if(Character.toString(ch).matches("[" + this.getValidValues() + "]*"))
					sb.append(ch);
		}
		
		return	sb.toString();
	}
}

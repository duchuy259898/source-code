/*
 * CustomPlainDocument.java
 *
 * Created on 2006/04/20, 9:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.text.*;

/**
 * 独自のPlainDocumentクラス
 * @author katagiri
 */
public class CustomPlainDocument extends PlainDocument
{
	/**
	 * 数値の正規表現
	 */
	public static final String	NUMERIC			=	"0-9";
	
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
	 * 英数字の正規表現
	 */
	public static final String	ALPHAMERIC		=	"\\p{Alnum}";
	
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
	public static final String	PHONE_NUMBER	=	"0-9#\\*-";
	
	/**
	 * メールアドレスで使える文字の正規表現
	 */
	public static final String	MAIL_ADDRESS	=	"\\p{Alnum}!\\$\\%\\&\\*\\+-\\./=\\?@\\^\\_\\~";
	
	/**
	 * 最大文字数
	 */
	protected	int			maxLength		=	0;
	
	/**
	 * 使用可能な文字の正規表現
	 */
	protected	String		validValues		=	"";
	
	/**
	 * コンストラクタ
	 */
	public CustomPlainDocument()
	{
		this(0, "");
	}
	
	/**
	 * コンストラクタ
	 * @param maxLength 最大文字数
	 */
	public CustomPlainDocument(int maxLength)
	{
		this(maxLength, "");
	}
	
	/**
	 * コンストラクタ
	 * @param validValues 使用可能な文字の正規表現
	 */
	public CustomPlainDocument(String validValues)
	{
		this(0, validValues);
	}
	
	public CustomPlainDocument(DocumentFilter filter)
	{
		this(0, "", filter);
	}
	
	/**
	 * コンストラクタ
	 * @param maxLength 最大文字数
	 * @param validValues 使用可能な文字の正規表現
	 */
	public CustomPlainDocument(int maxLength, String validValues)
	{
		this.setMaxLength(maxLength);
		this.setValidValues(validValues);
	}
	
	/**
	 * コンストラクタ
	 * @param maxLength 最大文字数
	 * @param filter フィルター
	 */
	public CustomPlainDocument(int maxLength, DocumentFilter filter)
	{
		this(maxLength, "", filter);
	}
	
	/**
	 * コンストラクタ
	 * @param validValues 使用可能な文字の正規表現
	 * @param filter フィルター
	 */
	public CustomPlainDocument(String validValues, DocumentFilter filter)
	{
		this(0, validValues, filter);
	}
	
	/**
	 * コンストラクタ
	 * @param maxLength 最大文字数
	 * @param validValues 使用可能な文字の正規表現
	 * @param filter フィルター
	 */
	public CustomPlainDocument(int maxLength, String validValues,
			DocumentFilter filter)
	{
		this.setMaxLength(maxLength);
		this.setValidValues(validValues);
		this.setDocumentFilter(filter);
	}

	/**
	 * 最大文字数を取得する。
	 * @return 最大文字数
	 */
	public int getMaxLength()
	{
		return maxLength;
	}

	/**
	 * 最大文字数をセットする。
	 * @param maxLength 最大文字数
	 */
	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
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
	 * 文字列を挿入する。
	 * @param offs オフセット
	 * @param str 挿入する文字列
	 * @param a 
	 */
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		if(this.isMatchValidValues(str) && this.checkLength())
		{
			super.insertString(offs, str, a);
		}
		else
		{
			super.insertString(offs, "", a);
		}
	}
	
	/**
	 * 使用可能な文字と一致するかチェックする。
	 * @param str チェックする文字列
	 * @return true - ＯＫ
	 */
	private boolean isMatchValidValues(String str)
	{
		if(this.getValidValues().equals(""))	return	true;
		
		return	str.matches("[" + this.getValidValues() + "]*");
	}
	
	/**
	 * 文字列の長さチェック
	 * @return true - ＯＫ
	 */
	private boolean checkLength()
	{
		if(this.getMaxLength() == 0)	return	true;
		
		return	(this.getLength() < this.getMaxLength());
	}
}

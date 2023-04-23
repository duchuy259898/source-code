/*
 * WildcardFileFilter.java
 *
 * Created on 2006/05/30, 11:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing.filechooser;

import java.io.*;

/**
 * JFileChooser用のFileFilter
 * @author katagiri
 */
public class WildcardFileFilter extends javax.swing.filechooser.FileFilter
{
	/**
	 * 拡張子のフィルタ文字列
	 */
	private	String	extension		=	".*\\.exe";
	/**
	 * 説明
	 */
	private String	description		=	"exe file";
	
	/**
	 * コンストラクタ
	 * @param wildcard 拡張子のフィルタ文字列
	 * @param description 説明
	 */
	public WildcardFileFilter(String wildcard, String description)
	{
		this.extension		=	this.convertWildCard(wildcard);
		this.description	=	description;
	}
	
	/**
	 * 拡張子のフィルタ文字列を変換する。（エスケープ文字等）
	 * @param wildcard 拡張子のフィルタ文字列
	 * @return 変換された拡張子のフィルタ文字列
	 */
	private String convertWildCard(String wildcard)
	{
		wildcard	=	wildcard.replace(';', '|');
		wildcard	=	wildcard.replace('*', '/');
		wildcard	=	wildcard.replaceAll("\\.", "\\\\.");
		wildcard	=	wildcard.replaceAll("/.", ".*");
		wildcard	=	wildcard.replace("?", ".");
		return	wildcard;
	}
	
	/**
	 * ファイルを表示する必要がある場合に true を返します。
	 * @param f ファイル
	 * @return true - ファイルを表示する必要がある場合
	 */
	public boolean accept(File f)
	{
		if(f.isDirectory())	return	true;
		
		String	name	=	f.getName().toLowerCase();
		return	name.matches(extension);
	}
	
	/**
	 * 説明を取得する。
	 * @return 説明
	 */
	public String getDescription()
	{
		return	description;
	}
}

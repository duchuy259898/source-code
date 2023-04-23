/*
 * DesktopUtil.java
 *
 * Created on 2007/06/01, 11:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.awt.*;
import java.io.*;
import java.net.*;

/**
 * Desktopクラス用ユーティリティ
 * @author katagiri
 */
public class DesktopUtil
{
	/**
	 * ブラウザを起動する
	 * @param url URL
	 * @throws java.net.MalformedURLException 
	 * @throws java.io.IOException 
	 * @throws java.net.URISyntaxException 
	 */
	public static void runWebBrowser(String url) throws MalformedURLException, IOException,
			URISyntaxException
	{
		Desktop.getDesktop().browse(
				new URL(url).toURI());
	}
	
	/**
	 * ブラウザを起動する
	 * @param uri 
	 * @throws java.net.MalformedURLException 
	 * @throws java.io.IOException 
	 * @throws java.net.URISyntaxException 
	 */
	public static void runWebBrowser(URI uri) throws MalformedURLException, IOException,
			URISyntaxException
	{
		Desktop.getDesktop().browse( uri );
	}
	
	/**
	 * ブラウザを起動する
	 * @param url 
	 * @throws java.net.MalformedURLException 
	 * @throws java.io.IOException 
	 * @throws java.net.URISyntaxException 
	 */
	public static void runWebBrowser(URL url) throws MalformedURLException, IOException,
			URISyntaxException
	{
		Desktop.getDesktop().browse(
				url.toURI());
	}

	/**
	 * メーラーを起動する
	 * @throws java.io.IOException 
	 */
	public static void runMailer() throws IOException
	{
		Desktop.getDesktop().mail();
	}

	/**
	 * 
	 * @param path 
	 * @throws java.io.IOException 
	 */
	public static void runExplorer(String path) throws IOException
	{
		Desktop.getDesktop().open(new File(path));
	}

	/**
	 * テキストエディタを起動する
	 * @param filePath ファイルのパス
	 * @throws java.io.IOException 
	 */
	public static void runTextEditor(String filePath) throws IOException
	{
		Desktop.getDesktop().open(new File(filePath));
	}

	/**
	 * テキストエディタを起動する
	 * @param file ファイル
	 * @throws java.io.IOException 
	 */
	public static void runTextEditor(File file) throws IOException
	{
		Desktop.getDesktop().open(file);
	}

}

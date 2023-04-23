/*
 * IOUtil.java
 *
 * Created on 2006/07/03, 20:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.io.*;

/**
 * 入出力関連ユーティリティ
 * @author katagiri
 */
public class IOUtil
{
	
	/**
	 * コンストラクタ
	 */
	public IOUtil()
	{
	}
	
	
	/**
	 * ファイルが存在するかをチェックする。
	 * @param filePath チェックするファイルのパス
	 * @return 存在する場合true
	 */
	public static boolean isExistFile(String filePath)
	{
		File	f	=	new File(filePath);
		
		if(f.exists())
		{
			return	true;
		}
		
		return	false;
	}
}

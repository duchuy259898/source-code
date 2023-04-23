/*
 * BarcodeLogic.java
 *
 * Created on 2007/10/16, 16:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.barcode;

import org.krysalis.barcode4j.*;

/**
 * バーコードロジッククラス
 * @author katagiri
 */
public class BarcodeLogic
{
	/**
	 * EAN13のコードの正当性をチェックする
	 * @param code コード
	 * @throws org.krysalis.barcode4j.BarcodeException 
	 * @return 
	 */
	public static boolean checkEAN13CheckDigit(String code) throws BarcodeException
	{
		//コードがnullの場合
		if(code == null)
		{
			throw	new BarcodeException("code is null");
		}
		//13桁の数値かチェックする
		if(!code.matches("[0-9]{13}"))
		{
			throw	new BarcodeException("code is not 13-digit numeric string");
		}
		//チェックディジットを取得する
		Integer	cd	=	BarcodeLogic.getEAN13CheckDigit(code.substring(0, 12));
		//チェックディジットのチェックをする
		return	cd.toString().equals(code.substring(12, 13));
	}
	
	/**
	 * チェックディジットを追加する
	 * @param code コード
	 * @throws org.krysalis.barcode4j.BarcodeException 
	 * @return 
	 */
	public static String addEAN13CheckDigit(String code) throws BarcodeException
	{
		return	code + BarcodeLogic.getEAN13CheckDigit(code).toString();
	}
	
	/**
	 * チェックディジットを取得する
	 * @param code コード
	 * @throws org.krysalis.barcode4j.BarcodeException 
	 * @return 
	 */
	public static Integer getEAN13CheckDigit(String code) throws BarcodeException
	{
		//コードがnullの場合
		if(code == null)
		{
			throw	new BarcodeException("code is null");
		}
		//12桁の数値かチェックする
		if(!code.matches("[0-9]{12}"))
		{
			throw	new BarcodeException("code is not 12-digit numeric string");
		}
		
		String[]	sTemp	=	code.split("");
		Integer[]	iTemp	=	new Integer[12];
		
		for(int i = 0; i < 12; i ++)
		{
			iTemp[i]	=	new Integer(sTemp[i + 1]);
		}
		
		int		temp0	=	0;
		
		for(int i = 1; i < 12; i += 2)
		{
			temp0	+=	iTemp[i];
		}
		
		temp0	*=	3;
		
		int		temp1	=	0;
		
		for(int i = 0; i < 12; i += 2)
		{
			temp1	+=	iTemp[i];
		}
		
		Integer		temp	=	(10 - ((temp0 + temp1) % 10)) % 10;
		
		return	temp;
	}
}

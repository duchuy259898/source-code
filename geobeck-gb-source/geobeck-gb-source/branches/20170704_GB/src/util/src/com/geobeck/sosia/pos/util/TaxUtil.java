/*
 * TaxUtil.java
 *
 * Created on 2006/05/15, 18:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;


/**
 * 税金ユーティリティ
 * @author katagiri
 */
public class TaxUtil
{
	
	/**
	 * コンストラクタ
	 */
	public TaxUtil()
	{
	}
	
	
	/**
	 * 税額を取得する。
	 * @param value 金額
	 * @return 税額
	 */
	public static Long getTax(Long value, Double taxRate, int rounding)
	{
		Double	tax	=	0d;
		Long rs = 0l;
		if(taxRate == null)	return tax.longValue();
		
		tax	=	(value.doubleValue() / (1d + taxRate) * taxRate);
		
		switch(rounding)
		{
			//切り捨て
			case 1:
				rs		=	(long)Math.floor(tax);
				break;
			//四捨五入
			case 2:
				rs		=	Math.round(tax);
				break;
			//切り上げ
			case 3:
				rs		=	(long)Math.ceil((double)tax);
				break;
			default:
				rs		=	0l;
		}
		
		return	rs;
	}
	
	
	/**
	 * 税額を取得する。
	 * @param value 金額
	 * @return 税額
	 */
	public static Integer getTax(Integer value, Double taxRate, int rounding)
	{
		Integer	tax	=	0;
		
		if(taxRate == null)	return	tax;
		
		tax	=	(int)(value.doubleValue() / (1d + taxRate) * taxRate);
		
		switch(rounding)
		{
			//切り捨て
			case 1:
				tax		=	(int)Math.floor((double)tax);
				break;
			//四捨五入
			case 2:
				tax		=	(int)Math.round((double)tax);
				break;
			//切り上げ
			case 3:
				tax		=	(int)Math.ceil((double)tax);
				break;
			default:
				tax		=	0;
		}
		
		return	tax;
	}
	
	
	/**
	 * 税額を取得する。
	 * @param value 金額
	 * @return 税額
	 */
	public static Double getTax(Double value, Double taxRate, int rounding)
	{
		Double	tax	=	0d;
		
		if(taxRate == null)	return	tax;
		
		tax	=	value / (1d + taxRate) * taxRate;
		
		switch(rounding)
		{
			//切り捨て
			case 1:
				tax		=	Math.floor(tax);
				break;
			//四捨五入
			case 2:
				tax		=	(double)Math.round(tax);
				break;
			//切り上げ
			case 3:
				tax		=	Math.ceil(tax);
				break;
			default:
				tax		=	0d;
		}
		
		return	tax;
	}
}

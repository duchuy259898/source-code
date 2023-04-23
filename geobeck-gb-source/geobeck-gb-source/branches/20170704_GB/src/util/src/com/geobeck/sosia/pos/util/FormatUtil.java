/*
 * FormatUtil.java
 *
 * Created on 2007/05/14, 16:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;

import java.text.*;

/**
 *
 * @author katagiri
 */
public class FormatUtil
{
	private static final String		DECIMAL_FORMAT	=	"#,##0";
	private static final String		FLORT_FORMAT1	=	"#,##0.0";
	private static final String		FLORT_FORMAT2	=	"#,##0.00";
	
	/** Creates a new instance of FormatUtil */
	public FormatUtil()
	{
	}
	
	private static DecimalFormat	dFormat		=	null;
	private static DecimalFormat	fFormat1	=	null;
	private static DecimalFormat	fFormat2	=	null;
	
	private static DecimalFormat getDecimalFormat()
	{
		if(dFormat == null)
		{
			dFormat	=	new DecimalFormat(DECIMAL_FORMAT);
		}
		
		return	dFormat;
	}
	
	private static DecimalFormat getFloatFormat1()
	{
		if(fFormat1 == null)
		{
			fFormat1	=	new DecimalFormat(FLORT_FORMAT1);
		}
		
		return	fFormat1;
	}
	
	private static DecimalFormat getFloatFormat2()
	{
		if(fFormat2 == null)
		{
			fFormat2	=	new DecimalFormat(FLORT_FORMAT2);
		}
		
		return	fFormat2;
	}
	
	public static String decimalFormat(Object obj)
	{
		return	getDecimalFormat().format(obj);
	}
	
	public static String floatFormat1(Object obj)
	{
		return	getFloatFormat1().format(obj);
	}
	
	public static String floatFormat2(Object obj)
	{
		return	getFloatFormat2().format(obj);
	}
}

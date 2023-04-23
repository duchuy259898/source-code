/*
 * JANCodeListData.java
 *
 * Created on 2007/04/30, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.commodity;

import java.awt.*;
import java.awt.image.*;

import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class JANCodeListData extends MstItemClass
{
	private static final int DATA_COUNT	=	4;
	
	private JANCodeData[]	janCodeData	=	null;
	
	/** Creates a new instance of JANCodeListData */
	public JANCodeListData(MstItemClass mic)
	{
		super();
		
		this.setItemClass(mic);
		
		janCodeData	=	new JANCodeData[DATA_COUNT];
		
		for(int i = 0; i < DATA_COUNT; i ++)
		{
			janCodeData[i]	=	new JANCodeData();
		}
	}
	
	public void setItemClass(MstItemClass mic)
	{
		this.setData(mic);
	}

	public void setJanCodeData(int index, JANCodeData janCodeData)
	{
		this.janCodeData[index] = janCodeData;
	}
	
	public String getItemNo(int index)
	{
		if(janCodeData[index] != null)
		{
			return	janCodeData[index].getItemNo();
		}
		
		return	"";
	}
	
	public String getItemNo0()
	{
		return	this.getItemNo(0);
	}
	
	public String getItemNo1()
	{
		return	this.getItemNo(1);
	}
	
	public String getItemNo2()
	{
		return	this.getItemNo(2);
	}
	
	public String getItemNo3()
	{
		return	this.getItemNo(3);
	}
	
	public String getItemName(int index)
	{
		if(janCodeData[index] != null)
		{
			return	janCodeData[index].getItemName();
		}
		
		return	"";
	}
	
	public String getItemName0()
	{
		return	this.getItemName(0);
	}
	
	public String getItemName1()
	{
		return	this.getItemName(1);
	}
	
	public String getItemName2()
	{
		return	this.getItemName(2);
	}
	
	public String getItemName3()
	{
		return	this.getItemName(3);
	}
	
	public BufferedImage getBarcode(int index)
	{
		if(janCodeData[index] != null)
		{
			return	janCodeData[index].getBarcode();
		}
		
		return	null;
	}
	
	public BufferedImage getBarcode0()
	{
		return	this.getBarcode(0);
	}
	
	public BufferedImage getBarcode1()
	{
		return	this.getBarcode(1);
	}
	
	public BufferedImage getBarcode2()
	{
		return	this.getBarcode(2);
	}
	
	public BufferedImage getBarcode3()
	{
		return	this.getBarcode(3);
	}
}

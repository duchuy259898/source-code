/*
 * PostcardSetting.java
 *
 * Created on 2006/09/07, 11:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.mail;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/**
 *
 * @author katagiri
 */
public class PostcardSetting
{
	private Vector<BaseFont>    fonts           =   new Vector<BaseFont>();
	private int                 selectedIndex   =   -1;
	private	FieldSetting        postal          =   new FieldSetting();
	private	FieldSetting        address         =   new FieldSetting();
	private	FieldSetting        name            =   new FieldSetting();
	
	/** Creates a new instance of PostcardSetting */
	public PostcardSetting()
	{
		this.initFonts();
	}

	public Vector<BaseFont> getFonts()
	{
		return fonts;
	}

	public void setFonts(Vector<BaseFont> fonts)
	{
		this.fonts = fonts;
	}

	public int getSelectedIndex()
	{
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex)
	{
		this.selectedIndex = selectedIndex;
	}
	
	public BaseFont getSelectedFont()
	{
		if(0 <= selectedIndex && selectedIndex < fonts.size())
			return	fonts.get(selectedIndex);
		else
			return	null;
	}

	public FieldSetting getPostal()
	{
		return postal;
	}

	public void setPostal(FieldSetting postal)
	{
		this.postal = postal;
	}

	public FieldSetting getAddress()
	{
		return address;
	}

	public void setAddress(FieldSetting address)
	{
		this.address = address;
	}

	public FieldSetting getName()
	{
		return name;
	}

	public void setName(FieldSetting name)
	{
		this.name = name;
	}
	
	private void initFonts()
	{
		try
		{
			BaseFont	bfMincho	=	BaseFont.createFont("HeiseiMin-W3", "UniJIS-UCS2-HW-H", false);
			fonts.add(bfMincho);
			BaseFont	bfGothic	=	BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", false);
			fonts.add(bfGothic);
		}
		catch(DocumentException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch(IOException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}

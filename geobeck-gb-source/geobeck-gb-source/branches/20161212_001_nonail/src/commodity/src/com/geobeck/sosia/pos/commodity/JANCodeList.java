/*
 * JANCodeList.java
 *
 * Created on 2007/04/23, 17:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.commodity;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.krysalis.barcode4j.*;
import org.krysalis.barcode4j.impl.*;
import org.krysalis.barcode4j.output.*;
import org.krysalis.barcode4j.output.bitmap.*;
import org.krysalis.barcode4j.tools.*;

import org.krysalis.barcode4j.*;
import org.krysalis.barcode4j.output.*;
import org.krysalis.barcode4j.output.bitmap.*;
import org.krysalis.barcode4j.tools.*;
import org.krysalis.barcode4j.impl.upcean.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class JANCodeList extends ArrayList<JANCodeListData>
{
	private static final String		REPORT_PATH	=	"/report/JANCodeList.jasper";
	private static final String		REPORT_NAME	=	"JANCodeList";
	private static final int		BARCODE_DPI	=	200;
	
	private MstItemClasses		itemClasses			=	null;
	private	BarcodeGenerator	barcodeGenerator	=	null;
	
	/** Creates a new instance of JANCodeList */
	public JANCodeList()
	{
		this.initItemClasses();
		this.initBarcodeGenerator();
	}

	public MstItemClasses getItemClasses()
	{
		return itemClasses;
	}

	public void setItemClasses(MstItemClasses itemClasses)
	{
		this.itemClasses = itemClasses;
	}
	
	private void initItemClasses()
	{
		itemClasses	=	new MstItemClasses();
		
		try
		{
			itemClasses.loadAll(SystemInfo.getConnection());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void initBarcodeGenerator()
	{
		barcodeGenerator = new EAN13Bean();
		
		EAN13Bean	bean	=	(EAN13Bean)barcodeGenerator;

		//Configure the barcode generator
		bean.setModuleWidth(0.2);
		bean.doQuietZone(true);
		bean.setQuietZone(2);
		bean.setHeight(12);
		bean.setFontName("Helvetica");
		bean.setFontSize(2);
	}
	
	public JANCodeData getJANCodeData(MstItem mi)
	{
		JANCodeData		jcd	=	new JANCodeData();
		jcd.setData(mi);
		
		if(!jcd.getJANCode().equals(""))
		{
			try
			{
				jcd.setBarcode(BitmapBuilder.getImage(
						barcodeGenerator,
						jcd.getJANCode(),
						BARCODE_DPI));
			}
			catch(Exception e)
			{
			}
		}
		
		return	jcd;
	}
	
	public boolean print()
	{
		HashMap<String, Object>		param	=	new HashMap<String, Object>();
		
		InputStream		report		=	JANCodeListPanel.class.getResourceAsStream(REPORT_PATH);
		String			fileName	=	REPORT_NAME + String.format("%1$tY%1$tm%1$td%1$ts", new java.util.Date());
		
		ReportManager.exportReport(report, fileName, ReportManager.PDF_FILE, param, this);
		
		return	true;
	}
}

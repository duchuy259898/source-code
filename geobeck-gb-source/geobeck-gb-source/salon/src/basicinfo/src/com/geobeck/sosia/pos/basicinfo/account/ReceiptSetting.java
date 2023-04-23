/*
 * ReceiptSetting.java
 *
 * Created on 2007/10/22, 17:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.account;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.imageio.stream.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.swing.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.company.*;


/**
 *
 * @author katagiri
 */
public class ReceiptSetting extends MstReceiptSetting
{
	private	PrintService[] printServices = null;
	
	/** Creates a new instance of ReceiptSetting */
	public ReceiptSetting()
	{
		this.loadPrinterList();
		this.setShop(SystemInfo.getCurrentShop());
		this.load();
	}
	
	public boolean load()
	{
		try
		{
			this.load(SystemInfo.getConnection());
			return	true;
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	false;
	}

	public PrintService[] getPrintServices()
	{
		return printServices;
	}

	private void setPrintServices(PrintService[] printServices)
	{
		this.printServices = printServices;
	}
	
	private void loadPrinterList()
	{
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		this.setPrintServices(PrintServiceLookup.lookupPrintServices(flavor, aset));
	}
	
	public boolean regist()
	{
		boolean result = false;
		
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			
			con.begin();
			
			if(super.regist(con))
			{
				con.commit();
				result = true;
			}
			else
			{
				con.rollback();
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
}

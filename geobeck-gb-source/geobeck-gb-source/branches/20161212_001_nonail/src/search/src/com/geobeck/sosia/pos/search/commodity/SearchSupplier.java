/*
 * SearchSupplier.java
 *
 * Created on 2007/04/17, 15:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.search.commodity;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.commodity.*;

/**
 *
 * @author katagiri
 */
public class SearchSupplier extends ArrayList<MstSupplier>
{
	private	Integer		supplierNoFrom			=	null;
	private	Integer		supplierNoTo			=	null;
	private String		supplierName			=	"";
	private Integer		purchaseDivision		=	0;
	private	String		prefecture				=	"";
	private String		phoneNumber				=	"";
	private	Integer		itemID					=	null;
	
	/** Creates a new instance of SearchSupplier */
	public SearchSupplier()
	{
	}

	public Integer getSupplierNoFrom()
	{
		return supplierNoFrom;
	}

	public void setSupplierNoFrom(Integer supplierNoFrom)
	{
		this.supplierNoFrom = supplierNoFrom;
	}

	public Integer getSupplierNoTo()
	{
		return supplierNoTo;
	}

	public void setSupplierNoTo(Integer supplierNoTo)
	{
		this.supplierNoTo = supplierNoTo;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public Integer getPurchaseDivision()
	{
		return purchaseDivision;
	}

	public void setPurchaseDivision(Integer purchaseDivision)
	{
		this.purchaseDivision = purchaseDivision;
	}

	public String getPrefecture()
	{
		return prefecture;
	}

	public void setPrefecture(String prefecture)
	{
		this.prefecture = prefecture;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public Integer getItemID()
	{
		return itemID;
	}

	public void setItemID(Integer itemID)
	{
		this.itemID = itemID;
	}
	
	public void load()
	{
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());
			
			while(rs.next())
			{
				MstSupplier		ms	=	new MstSupplier();
				
				ms.setData(rs);
				
				this.add(ms);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	
	private String getLoadSQL()
	{
		String	sql	=	"select distinct ms.*\n" +
			"from mst_supplier ms\n" +
			"left outer join mst_purchase mp\n" +
			"on mp.supplier_id = ms.supplier_id\n" +
			"and mp.delete_date is null\n" +
			"where ms.delete_date is null\n";
		
		if(supplierNoFrom != null)
		{
			sql	+=	"and ms.supplier_no >= " + SQLUtil.convertForSQL(supplierNoFrom) + "\n";
		}
		
		if(supplierNoTo != null)
		{
			sql	+=	"and ms.supplier_no <= " + SQLUtil.convertForSQL(supplierNoTo) + "\n";
		}
		
		if(!supplierName.equals(""))
		{
			sql	+=	"and ms.supplier_name like '" + supplierName + "%'\n";
		}
		
		if(0 <= purchaseDivision)
		{
			sql	+=	"and ms.purchase_division = " + SQLUtil.convertForSQL(purchaseDivision) + "\n";
		}
		
		if(!prefecture.equals(""))
		{
			sql	+=	"and ms.address1 = '" + prefecture + "'\n";
		}
		
		if(!phoneNumber.equals(""))
		{
			sql	+=	"and ms.phone_number like '" + phoneNumber + "%'\n";
		}
		
		if(itemID != null)
		{
			sql	+=	"and mp.item_id = " + SQLUtil.convertForSQL(itemID) + "\n";
		}
		
		return	sql;
	}
}

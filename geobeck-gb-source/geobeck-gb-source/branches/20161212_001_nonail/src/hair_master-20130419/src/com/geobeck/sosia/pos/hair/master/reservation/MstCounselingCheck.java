/*
 * MstCounselingCheck.java
 *
 * Created on 2007/11/13, 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.reservation;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class MstCounselingCheck
{
	private MstShop			shop			=	null;
	private	Integer			checkID			=	null;
	private	String			checkContent	=	"";
	
	/**
	 * Creates a new instance of MstCounselingCheck
	 */
	public MstCounselingCheck()
	{
		
	}

	public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	public Integer getShopID()
	{
		if(this.getShop() != null)
		{
			return	this.getShop().getShopID();
		}
		
		return	null;
	}

	public Integer getCheckID()
	{
		return checkID;
	}

	public void setCheckID(Integer checkID)
	{
		this.checkID = checkID;
	}

	public String getCheckContent()
	{
		return checkContent;
	}

	public void setCheckContent(String checkContent)
	{
		this.checkContent = checkContent;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof MstCounselingCheck)
		{
			MstCounselingCheck	temp	=	(MstCounselingCheck)o;
			
			if(temp != null)
			{
				return	(temp.getShopID() == this.getShopID() &&
						temp.getCheckID() == this.getCheckID());
			}
		}
		
		return	false;
	}
	
	public String toString()
	{
		return	this.getCheckContent();
	}
	
	public void setData(MstCounselingCheck mcc)
	{
		this.setShop(mcc.getShop());
		this.setCheckID(mcc.getCheckID());
		this.setCheckContent(mcc.getCheckContent());
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstShop	ms	=	new MstShop();
		ms.setShopID(rs.getInt("shop_id"));
		this.setShop(ms);
		this.setCheckID(rs.getInt("check_id"));
		this.setCheckContent(rs.getString("check_content"));
	}
}

/*
 * MstMailTemplateClasses.java
 *
 * Created on 2006/11/01, 10:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.mail;

import java.util.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class MstMailTemplateClasses extends ArrayList<MstMailTemplateClass>
{
	private	MstGroup	group					=	new MstGroup();
	private	MstShop		shop					=	new MstShop();
	
	/** Creates a new instance of MstMailTemplateClasses */
	public MstMailTemplateClasses()
	{
	}

	public MstGroup getGroup()
	{
		return group;
	}

	public void setGroup(MstGroup group)
	{
		this.group = group;
	}
	
	public Integer getGroupID()
	{
		if(group == null)	return	null;
		else	return	group.getGroupID();
	}
	
	public void setGroupID(Integer groupID)
	{
		if(group == null)
		{
			group	=	new MstGroup();
		}
		
		group.setGroupID(groupID);
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
		if(shop == null)	return	null;
		else	return	shop.getShopID();
	}
	
	public void setShopID(Integer shopID)
	{
		if(shop == null)
		{
			shop	=	new MstShop();
		}
		
		shop.setShopID(shopID);
	}
	
	/**
	 * テンプレート分類を読み込む
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
		
		if(isAddBlank)
		{
			this.add(new MstMailTemplateClass());
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		while(rs.next())
		{
			MstMailTemplateClass	mmtc	=	new MstMailTemplateClass();
			
			mmtc.setData(rs);
			
			this.add(mmtc);
		}
		
		return	true;
	}
	
	public String getSelectSQL()
	{
		return	"select mmtc.*\n" +
				"from mst_mail_template_class mmtc\n" +
				"where mmtc.delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"order by mmtc.display_seq\n";
	}
	
	
	public int getIndexByID(Integer mailTemplateClassID)
	{
		for(int i = 0; i < this.size(); i ++)
		{
			MstMailTemplateClass mmtc	=	this.get(i);
			
			if(mmtc.getMailTemplateClassID() == mailTemplateClassID)
			{
				return	i;
			}
		}
		
		return	-1;
	}
}

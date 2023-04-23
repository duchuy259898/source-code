/*
 * MstMailTemplates.java
 *
 * Created on 2006/11/01, 10:32
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
public class MstMailTemplates extends ArrayList<MstMailTemplate>
{
	private	MstGroup	group					=	new MstGroup();
	private	MstShop		shop					=	new MstShop();
	private MstMailTemplateClass	mailTemplateClass	=	new MstMailTemplateClass();
	
	/** Creates a new instance of MstMailTemplates */
	public MstMailTemplates()
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

	public MstMailTemplateClass getMailTemplateClass()
	{
		return mailTemplateClass;
	}

	public void setMailTemplateClass(MstMailTemplateClass mailTemplateClass)
	{
		this.mailTemplateClass = mailTemplateClass;
	}
	
	/**
	 * テンプレートを読み込む
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
		
		if(isAddBlank)
		{
			this.add(new MstMailTemplate());
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		while(rs.next())
		{
			MstMailTemplate		mmt	=	new MstMailTemplate();
			
			mmt.setData(rs);
			
			this.add(mmt);
		}
		
		return	true;
	}
	
	public String getSelectSQL()
	{
		return	"select mmt.*\n" +
				"from mst_mail_template mmt\n" +
				"inner join mst_mail_template_class mmtc\n" +
				"on mmtc.mail_template_class_id = mmt.mail_template_class_id\n" +
				"and mmtc.group_id = mmt.group_id\n" +
				"and mmtc.shop_id = mmt.shop_id\n" +
				"and mmtc.delete_date is null\n" +
				"where mmt.delete_date is null\n" +
				"and mmt.group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and mmt.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mmt.mail_template_class_id = " + SQLUtil.convertForSQL(mailTemplateClass.getMailTemplateClassID()) + "\n" +
				"order by mmtc.display_seq, mmt.display_seq\n";
	}
	
	
	public int getIndexByID(Integer mailTemplateClassID, Integer mailTemplateID)
	{
		for(int i = 0; i < this.size(); i ++)
		{
			MstMailTemplate	 mmt	=	this.get(i);
			
			if(mmt.getMailTemplateClassID() == mailTemplateClassID &&
					mmt.getMailTemplateID() == mailTemplateID)
			{
				return	i;
			}
		}
		
		return	-1;
	}
}

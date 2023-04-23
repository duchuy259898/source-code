/*
 * MstSkin.java
 *
 * Created on 2007/02/09, 17:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master;

import java.awt.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author katagiri
 */
public class MstSkin
{
	private	Integer		skinID			=	null;
	private	String		skinName		=	"";
	private	String		packageName		=	"";
	private	Integer		menuRed			=	0;
	private	Integer		menuGreen		=	0;
	private	Integer		menuBlue		=	0;
	private	Integer		tableRed		=	0;
	private	Integer		tableGreen		=	0;
	private	Integer		tableBlue		=	0;
	
	/** Creates a new instance of MstSkin */
	public MstSkin()
	{
	}
	
	public String toString()
	{
		return	this.getSkinName();
	}

	public Integer getSkinID()
	{
		return skinID;
	}

	public void setSkinID(Integer skinID)
	{
		this.skinID = skinID;
	}

	public String getSkinName()
	{
		return skinName;
	}

	public void setSkinName(String skinName)
	{
		this.skinName = skinName;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public Integer getMenuRed()
	{
		return menuRed;
	}

	public void setMenuRed(Integer menuRed)
	{
		this.menuRed = menuRed;
	}

	public Integer getMenuGreen()
	{
		return menuGreen;
	}

	public void setMenuGreen(Integer menuGreen)
	{
		this.menuGreen = menuGreen;
	}

	public Integer getMenuBlue()
	{
		return menuBlue;
	}

	public void setMenuBlue(Integer menuBlue)
	{
		this.menuBlue = menuBlue;
	}
	
	public Color getMenuColor()
	{
//新メニューにともないとりあえず固定値で
//		return	new Color(this.getMenuRed(),
//				this.getMenuGreen(),
//				this.getMenuBlue());
		return	new Color(255,
				153,
				0);
	}

	public Integer getTableRed()
	{
		return tableRed;
	}

	public void setTableRed(Integer tableRed)
	{
		this.tableRed = tableRed;
	}

	public Integer getTableGreen()
	{
		return tableGreen;
	}

	public void setTableGreen(Integer tableGreen)
	{
		this.tableGreen = tableGreen;
	}

	public Integer getTableBlue()
	{
		return tableBlue;
	}

	public void setTableBlue(Integer tableBlue)
	{
		this.tableBlue = tableBlue;
	}
	
	public Color getTableColor()
	{
		return	new Color(this.getTableRed(),
				this.getTableGreen(),
				this.getTableBlue());
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setSkinID(rs.getInt("skin_id"));
		this.setSkinName(rs.getString("skin_name"));
		this.setPackageName(rs.getString("package_name"));
		this.setMenuRed(rs.getInt("menu_red"));
		this.setMenuGreen(rs.getInt("menu_green"));
		this.setMenuBlue(rs.getInt("menu_blue"));
		this.setTableRed(rs.getInt("table_red"));
		this.setTableGreen(rs.getInt("table_green"));
		this.setTableBlue(rs.getInt("table_blue"));
	}
	
	
	public void load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			this.setData(rs);
		}
		
		rs.close();
	}
	
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_skin\n" +
				"where skin_id = " + SQLUtil.convertForSQL(this.getSkinID()) + "\n";
	}
}

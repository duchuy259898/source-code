/*
 * SortInfo.java
 *
 * Created on 2007/02/16, 9:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sql;

import java.util.*;

/**
 *
 * @author katagiri
 */
public class SortInfo
{
	private	static final	Integer		MAX_ORDER	=	10;
	
	protected	ArrayList<Integer>	colList		=	new ArrayList<Integer>();
	protected	ArrayList<Boolean>	ascList		=	new ArrayList<Boolean>();
	
	protected	Integer				maxOrder	=	MAX_ORDER;
	protected	String				preOrder	=	"";
	
	/** Creates a new instance of SortInfo */
	public SortInfo()
	{
	}
	
	public SortInfo(Integer maxOrder)
	{
		this.setMaxOrder(maxOrder);
	}
	
	public SortInfo(String preOrder)
	{
		this.setPreOrder(preOrder);
	}
	
	public SortInfo(Integer maxOrder, String preOrder)
	{
		this.setMaxOrder(maxOrder);
		this.setPreOrder(preOrder);
	}

	public Integer getMaxOrder()
	{
		return maxOrder;
	}

	public void setMaxOrder(Integer maxOrder)
	{
		this.maxOrder = maxOrder;
	}

	public String getPreOrder()
	{
		return preOrder;
	}

	public void setPreOrder(String preOrder)
	{
		this.preOrder = preOrder;
	}
	
	public void clear()
	{
		colList.clear();
		ascList.clear();
	}
	
	public void addOrder(Integer col)
	{
		if(colList.size() == 0)
		{
			this.addOrder(col, true);
		}
		else
		{
			if(col == colList.get(0))
			{
				this.addOrder(col, !ascList.get(0));
			}
			else
			{
				this.addOrder(col, true);
			}
		}
	}
	
	public void addOrder(Integer col, boolean idAsc)
	{
		colList.add(0, col);
		ascList.add(0, idAsc);
		
		if(maxOrder < colList.size())
		{
			colList.remove(maxOrder);
			ascList.remove(maxOrder);
		}
	}
	
	public String getOrderSQL()
	{
		String	sql	=	"";
		
		for(int i = 0; i < colList.size(); i ++)
		{
			if(0 < i)
			{
				sql	+=	", ";
			}
			
			sql	+=	colList.get(i).toString() + (ascList.get(i) ? " asc" : " desc");
		}
		
		if(0 < preOrder.length() && 0 < sql.length())
		{
			sql	=	preOrder + ", " + sql;
		}
		else
		{
			sql	=	preOrder;
		}
		
		if(0 < sql.length())
		{
			sql	=	"order by " + sql + "\n";
		}
		
		return	sql;
	}
}

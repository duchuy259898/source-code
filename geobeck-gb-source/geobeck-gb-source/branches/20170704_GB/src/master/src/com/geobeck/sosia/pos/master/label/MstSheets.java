/*
 * MstSheets.java
 *
 * Created on 2006/09/08, 17:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.label;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;

/**
 *
 * @author katagiri
 */
public class MstSheets extends ArrayList<MstSheet>
{
	
	/** Creates a new instance of MstSheets */
	public MstSheets()
	{
	}
	
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		while(rs.next())
		{
			MstSheet	ms	=	new MstSheet();
			ms.setData(rs);
			this.add(ms);
		}

		rs.close();
	}
	
	public String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_sheet\n" +
				"where delete_date is null\n" +
				"order by sheet_id\n";
	}
	
	public MstSheet getSheetByID(Integer sheetID)
	{
		for(MstSheet ms : this)
		{
			if(ms.getSheetID() == sheetID)
			{
				return	ms;
			}
		}
		
		return	new MstSheet();
	}
}

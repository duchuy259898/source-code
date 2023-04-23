/*
 * MstLabelMakers.java
 *
 * Created on 2006/09/08, 18:05
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
public class MstLabelMakers extends ArrayList<MstLabelMaker>
{
	
	/** Creates a new instance of MstLabelMakers */
	public MstLabelMakers()
	{
	}
	
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		while(rs.next())
		{
			MstLabelMaker	mlm	=	new MstLabelMaker();
			mlm.setData(rs);
			this.add(mlm);
		}

		rs.close();
	}
	
	public String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_label_maker\n" +
				"where delete_date is null\n" +
				"order by label_maker_id\n";
	}
	
}

/*
 * MstFreeHeadingUnits.java
 *
 * Created on 2007/08/17, 20:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author kanemoto
 */
public class MstFreeHeadingUnits extends ArrayList<MstFreeHeadingUnitPanel> {
    
    /** Creates a new instance of MstFreeHeadingUnits */
    public MstFreeHeadingUnits() {
		this.initArray();
    }
    
	private void initArray()
	{
		try
		{
			ResultSetWrapper	rs	=	SystemInfo.getConnection().executeQuery(
					this.getLoadSQL());
			
			while(rs.next())
			{
				MstFreeHeadingUnitPanel		mitdsp
						=	new MstFreeHeadingUnitPanel(
								rs.getInt("free_heading_class_id"),
								rs.getString("free_heading_class_name"));
				this.add(mitdsp);
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
		return	"select *\n" +
				"from mst_free_heading_class\n" +
				"where delete_date is null\n" +
				"and use_type = true\n" +
				"order by free_heading_class_id\n";
	}
}

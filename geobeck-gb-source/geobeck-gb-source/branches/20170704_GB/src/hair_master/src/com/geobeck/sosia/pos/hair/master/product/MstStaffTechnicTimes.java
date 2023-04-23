/*
 * MstStaffTechnicTimes.java
 *
 * Created on 2007/09/15, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author kanemoto
 */
public class MstStaffTechnicTimes extends ArrayList<MstStaffTechnicTime> {
	
	private		String		shopIDList		=	"";
	private		int			technicClassID	=	-1;
	private		int			technicID		=	-1;
	private		int			staffID			=	-1;
	private		int			limitNum		=	-1;
	
	/** Creates a new instance of MstStaffTechnicTimes */
	public MstStaffTechnicTimes() {
	}
	
	public String getShopIDList()
	{
		return this.shopIDList;
	}
	
	public void setShopIDList( String shopIDList )
	{
		this.shopIDList = shopIDList;
	}
	
	public Integer getTechnicClassID()
	{
		return this.technicClassID;
	}
	
	public void setTechnicClassID( Integer technicClassID )
	{
		this.technicClassID = technicClassID;
	}
	
	public void setTechnicID( Integer technicID )
	{
		this.technicID = technicID;
	}
	
	public Integer getTechnicID()
	{
		return this.technicID;
	}
	
	public void setStaffID( Integer staffID )
	{
		this.staffID = staffID;
	}
	
	public Integer getStaffID()
	{
		return this.staffID;
	}
	
	public void setLimitNum( Integer limitNum )
	{
		this.limitNum = limitNum;
	}
	
	public Integer getLimitNum()
	{
		return this.limitNum;
	}
	
    public boolean load()
    {
	    this.clear();

	    try
	    {
		    ResultSetWrapper	rs	=	SystemInfo.getConnection().executeQuery(
				    this.getLoadSQL());

		    while(rs.next())
		    {
			    MstStaffTechnicTime		mstt	=	new MstStaffTechnicTime();

			    mstt.setData(rs);

			    this.add( mstt );
		    }

		    rs.close();
	    }
	    catch(SQLException e)
	    {
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	    return	true;
    }

    private String getLoadSQL()
    {
		return
		"select\n" +
		"stl.*,\n" +
		"coalesce( mstt.operation_time, stl.operation_time ) as staff_operation_time\n" +
		"from\n" +
		"(\n" +
		"select\n" +
		"mt.*,\n" +
		"mtc.technic_class_name,\n" +
		"mtc.technic_class_contracted_name,\n" +
		"ms.*\n" +
		"from\n" +
		"mst_technic as mt\n" +
		"inner join mst_technic_class as mtc on\n" +
		"mtc.technic_class_id = mt.technic_class_id,\n" +
		"mst_staff as ms\n" +
		"where\n" +
		"mt.delete_date is null\n" +
		"and mtc.delete_date is null\n" +
		"and ms.delete_date is null\n" +
		( shopIDList.equals( "" ) ? "" : "and ms.shop_id IN ( " + shopIDList + " )\n" ) +
		( technicClassID == -1 ? "" : "and mt.technic_class_id = " + SQLUtil.convertForSQL( technicClassID ) +"\n" ) +
		( technicID == -1 ? "" : "and mt.technic_id = " + SQLUtil.convertForSQL( technicID ) +"\n" ) +
		( staffID == -1 ? "" : "and ms.staff_id = " + SQLUtil.convertForSQL( staffID ) +"\n" ) +
		") as stl\n" +
		"left outer join mst_staff_technic_time as mstt on\n" +
		"mstt.delete_date is null\n" +
		"and mstt.technic_id = stl.technic_id\n" +
		"and mstt.staff_id = stl.staff_id\n" +
		"order by\n" +
		"stl.technic_class_id, stl.technic_id, stl.staff_id\n" +
		( limitNum == -1 ? "" : "limit " + SQLUtil.convertForSQL( limitNum ) +"\n" ) +
		";\n";
    }
    
    public boolean regist()
    {
	    try
	    {
		    ConnectionWrapper con	=	SystemInfo.getConnection();

		    con.begin();

		    boolean		result	=	true;

		    try
		    {
			    for( MstStaffTechnicTime mstt : this )
			    {
				    if(!mstt.regist(con))
				    {
					    result	=	false;
					    break;
				    }
			    }
		    }
		    catch(SQLException e)
		    {
			    result	=	false;
		    }

		    if(result)
		    {
			    con.commit();
		    }
		    else
		    {
			    con.rollback();
		    }
	    }
	    catch(SQLException e)
	    {
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return	true;
    }
	
}

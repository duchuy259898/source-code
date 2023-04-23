/*
 * MstFreeHeadingUnit.java
 *
 * Created on 2007/08/17, 20:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author kanemoto
 */
public class MstFreeHeadingUnit extends MstFreeHeadingClass {
    
    /** Creates a new instance of MstFreeHeadingUnit */
    public MstFreeHeadingUnit( Integer freeHeadingClassID, String freeHeadingClassName ) {
	this.setFreeHeadingClassID( freeHeadingClassID );
	this.setFreeHeadingClassName( freeHeadingClassName );
    }
    
    /**
     * ÉçÅ[Éhèàóù
     */
    public boolean load()
    {
	    boolean		result	=	false;

	    this.clear();

	    try
	    {
		    ResultSetWrapper	rs	=	SystemInfo.getConnection().executeQuery(
				    this.getLoadSQL());

		    while(rs.next())
		    {
			    MstFreeHeading	mitd	=	new MstFreeHeading();
			    mitd.setData(rs);
			    mitd.setFreeHeadingClass( this );
			    this.add(mitd);
		    }

		    rs.close();

		    result	=	true;
	    }
	    catch(SQLException e)
	    {
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return	result;
    }

	
	private String getLoadSQL()
	{
		return	"select *\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClassID() ) + "\n" +
				"order by display_seq\n";
	}
	
	
	public boolean regist(MstFreeHeading mitd)
	{
		boolean		result	=	false;
		
		mitd.setFreeHeadingClass( this );
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			con.begin();
			
			if(mitd.regist(con) )
			{
				con.commit();
				result	=	true;
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
		
		return	result;
	}
	
	public boolean delete(MstFreeHeading mitd)
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			con.begin();
			
			if(mitd.delete(con))
			{
				con.commit();
				result	=	true;
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
		
		return	result;
	}
}

/*
 * MstFreeHeadingClasses.java
 *
 * Created on 2007/08/17, 17:17
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
public class MstFreeHeadingClasses extends ArrayList<MstFreeHeadingClass>
{
    
    /** Creates a new instance of MstFreeHeadingClasses */
    public MstFreeHeadingClasses() {
	    this.load();
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
			    MstFreeHeadingClass		mit	=	new MstFreeHeadingClass();

			    mit.setData(rs);

			    this.add(mit);
		    }

		    rs.close();
	    }
	    catch(SQLException e)
	    {
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    for(int i = this.size(); i < MstFreeHeadingClass.FREE_HEADING_TYPE_NUM; i ++)
	    {
		    MstFreeHeadingClass		mit	=	new MstFreeHeadingClass();

		    mit.setFreeHeadingClassID(i+1);

		    this.add(mit);
	    }

	    return	true;
    }

    private String getLoadSQL()
    {
	    return	"select *\n" +
			    "from mst_free_heading_class\n" +
			    "order by free_heading_class_id\n";
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
			    for(MstFreeHeadingClass mit : this)
			    {
				    if(!mit.regist(con))
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

/*
 * MstLabelMaker.java
 *
 * Created on 2006/09/08, 17:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.label;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author katagiri
 */
public class MstLabelMaker extends ArrayList<MstLabel>
{
	private	Integer	labelMakerID	=	null;
	private	String	labelMakerName	=	"";
	
	/** Creates a new instance of MstLabelMaker */
	public MstLabelMaker()
	{
	}

	public Integer getLabelMakerID()
	{
		return labelMakerID;
	}

	public void setLabelMakerID(Integer labelMakerID)
	{
		this.labelMakerID = labelMakerID;
	}

	public String getLabelMakerName()
	{
		return labelMakerName;
	}

	public void setLabelMakerName(String labelMakerName)
	{
		this.labelMakerName = labelMakerName;
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setLabelMakerID(rs.getInt("label_maker_id"));
		this.setLabelMakerName(rs.getString("label_maker_name"));
	}
	
	public String toString()
	{
		return	this.getLabelMakerName();
	}
	
	public void loadLabel(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectLabelSQL());

		while(rs.next())
		{
			MstLabel	ml	=	new MstLabel();
			ml.setData(rs);
			this.add(ml);
		}

		rs.close();
	}
	
	public String getSelectLabelSQL()
	{
		return	"select *\n" +
				"from mst_label\n" +
				"where delete_date is null\n" +
				"and label_maker_id = " + SQLUtil.convertForSQL(this.getLabelMakerID()) +
				"order by label_id\n";
	}
	
}

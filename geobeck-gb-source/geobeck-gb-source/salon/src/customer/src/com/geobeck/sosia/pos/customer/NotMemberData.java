/*
 * NotMemberData.java
 *
 * Created on 2007/03/12, 10:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.SQLException;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;

/**
 *
 * @author katagiri
 */
public class NotMemberData extends MstCustomer
{
	private	GregorianCalendar	insertDate		=	new GregorianCalendar();
	
	/** Creates a new instance of NotMemberData */
	public NotMemberData()
	{
		super();
	}

	public GregorianCalendar getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(GregorianCalendar insertDate)
	{
		this.insertDate = insertDate;
	}

	public void setInsertDate(Date insertDate)
	{
		this.insertDate.setTime(insertDate);
	}

	public String getInsertDateString()
	{
		return String.format("%1$tY/%1$tm/%1$td", insertDate);
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);
		this.setInsertDate(rs.getDate("insert_date"));
	}
}

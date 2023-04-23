/*
 * MstTax.java
 *
 * Created on 2006/12/20, 18:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author katagiri
 */
public class MstTax
{
	private	Integer				taxID		=	null;
	private	java.util.Date		applyDate	=	null;
	private	Double				taxRate		=	0d;
	
	/** Creates a new instance of MstTax */
	public MstTax()
	{
	}

	public Integer getTaxID()
	{
		return taxID;
	}

	public void setTaxID(Integer taxID)
	{
		this.taxID = taxID;
	}

	public java.util.Date getApplyDate()
	{
		return applyDate;
	}

	public void setApplyDate(java.util.Date applyDate)
	{
		this.applyDate = applyDate;
	}

	public Double getTaxRate()
	{
		return taxRate;
	}

	public void setTaxRate(Double taxRate)
	{
		this.taxRate = taxRate;
	}
	
	public Double getTaxRatePercentage()
	{
		if(this.getTaxRate() == null)
		{
			return	0d;
		}
		else
		{
			return	this.getTaxRate() * 10000.0/100.0;                    
		}
	}
	
	public void setDatas(ResultSetWrapper rs) throws SQLException
	{
		this.setTaxID(rs.getInt("tax_id"));
		this.setApplyDate(rs.getDate("apply_date"));
		this.setTaxRate(rs.getDouble("tax_rate"));
	}
	
	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		if(this.getTaxID() != null)
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
			
			if(rs.next())
			{
				result	=	true;
			}
			
			rs.close();
		}
		
		return	result;
	}
	
	public boolean isExistSameDateData(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		if(this.getApplyDate() != null)
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSameDateDataSQL());
			
			if(rs.next())
			{
				result	=	true;
			}
			
			rs.close();
		}
		
		return	result;
	}
	
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(this.isExist(con))
		{
			return	con.executeUpdate(this.getUpdateSQL()) == 1;
		}
		else
		{
			return	con.executeUpdate(this.getInsertSQL()) == 1;
		}
	}
	
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(!this.isExist(con))	return	false;
		
		return	con.executeUpdate(this.getDeleteSQL()) == 1;
	}
	
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_tax\n" +
				"where tax_id = " + SQLUtil.convertForSQL(taxID) + "\n";
	}
	
	private String getSelectSameDateDataSQL()
	{
		return	"select *\n" +
				"from mst_tax\n" +
				"where delete_date is null\n" +
				(this.getTaxID() == null ? "" : "and tax_id != " + SQLUtil.convertForSQL(this.getTaxID()) + "\n") +
				"and apply_date = " + SQLUtil.convertForSQLDateOnly(this.getApplyDate()) + "\n";
	}
	
	private String getInsertSQL()
	{
		return	"insert into mst_tax(tax_id, apply_date, tax_rate,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				"(select coalesce(max(tax_id), 0) + 1 from mst_tax),\n" +
				SQLUtil.convertForSQLDateOnly(this.getApplyDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getTaxRate()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null)\n";
	}
	
	private String getUpdateSQL()
	{
		return	"update mst_tax\n" +
				"set apply_date = " + SQLUtil.convertForSQLDateOnly(this.getApplyDate()) + ",\n" +
				"tax_rate = " + SQLUtil.convertForSQL(this.getTaxRate()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where tax_id = " + SQLUtil.convertForSQL(this.getTaxID()) + "\n";
	}
	
	private String getDeleteSQL()
	{
		return	"update mst_tax\n" +
				"set delete_date = current_timestamp,\n" +
				"update_date = current_timestamp\n" +
				"where tax_id = " + SQLUtil.convertForSQL(this.getTaxID()) + "\n";
	}
	
	public static Double getCurrentTaxRate(ConnectionWrapper con, java.util.Date date) throws SQLException
	{
		Double	result	=	0d;
		
		ResultSetWrapper	rs	=	con.executeQuery(MstTax.getCurrentTaxRateSQL(date));
		
		if(rs.next())
		{
			result	=	rs.getDouble("tax_rate");
		}
		
		rs.close();
		
		return	result;
	}
	
	private static String getCurrentTaxRateSQL(java.util.Date date)
	{
		return	"select coalesce(tax_rate, 0) as tax_rate\n" +
				"from mst_tax\n" +
				"where delete_date is null\n" +
				"and apply_date = (\n" +
				"select max(apply_date)\n" +
				"from mst_tax\n" +
				"where delete_date is null\n" +
				"and apply_date <= " + SQLUtil.convertForSQLDateOnly(date) + ")\n";
	}
	
	public static String getSelectAllSQL()
	{
		return	"select *\n" +
			"from mst_tax\n" +
			"where delete_date is null\n" +
			"order by apply_date\n";
	}
}

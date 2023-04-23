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
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author katagiri
 */
public class MstTax
{
	private	Integer				taxID		=	null;
	private	java.util.Date		applyDate	=	null;
	private	Double				taxRate		=	0d;
        // IVS_LVTU start add 2019/07/23 #97064 [gb]SPOS増税対応
        private	boolean				reducedTax;
        // IVS_LVTU end add 2019/07/23 #97064 [gb]SPOS増税対応
	
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
        // IVS_LVTU start add 2019/07/23 #97064 [gb]SPOS増税対応
        public boolean isReducedTax() {
            return reducedTax;
        }

        public void setReducedTax(boolean reducedTax) {
            this.reducedTax = reducedTax;
        }
        // IVS_LVTU end add 2019/07/23 #97064 [gb]SPOS増税対応
	
	public Double getTaxRatePercentage()
	{
		if(this.getTaxRate() == null)
		{
			return	0d;
		}
		else
		{
                    BigDecimal bg1, bg2, bg3, bg4, bg5;
                    bg1 = new BigDecimal(this.getTaxRate());
                    bg2 = new BigDecimal(10000.0);
                    bg3 = new BigDecimal(100.0);
                    
                    bg4 = bg1.multiply(bg2, new MathContext(4, RoundingMode.HALF_UP));
                    bg5 = bg4.divide(bg3 , RoundingMode.CEILING);
                    
			return	bg5.doubleValue();                    
		}
	}
        
        // IVS_LVTU start edit 2019/07/23 #97064 [gb]SPOS増税対応
        private int getTaxRatePercent()
	{
		if(this.getTaxRate() == null)
		{
			return	0;
		}
		else
		{
			return	(int) (this.getTaxRate() * 10000/100);                    
		}
	}
	
	public void setDatas(ResultSetWrapper rs) throws SQLException
	{
		this.setTaxID(rs.getInt("tax_id"));
		this.setApplyDate(rs.getDate("apply_date"));
		this.setTaxRate(rs.getDouble("tax_rate"));
                this.setReducedTax(rs.getBoolean("reduced_tax"));
	}
        // IVS_LVTU end edit 2019/07/23 #97064 [gb]SPOS増税対応
	
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
        
        public MstTax getTaxBySaleDate(ConnectionWrapper con, java.util.Date date) throws SQLException
	{
		

                ResultSetWrapper	rs	=	con.executeQuery(MstTax.getTaxBySaleDateSQL(date));

                if(rs.next())
                {
                        this.setDatas(rs);
                }

                rs.close();
		
		return	this;
	}
        
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_tax\n" +
				"where tax_id = " + SQLUtil.convertForSQL(taxID) + "\n";
	}
	
        // IVS_LVTU start edit 2019/07/23 #97064 [gb]SPOS増税対応
	private String getSelectSameDateDataSQL()
	{
		return	"select *\n" +
				"from mst_tax\n" +
				"where delete_date is null\n" +
                                "and reduced_tax = false\n" +
				(this.getTaxID() == null ? "" : "and tax_id != " + SQLUtil.convertForSQL(this.getTaxID()) + "\n") +
				"and apply_date = " + SQLUtil.convertForSQLDateOnly(this.getApplyDate()) + "\n";
	}
	
	private String getInsertSQL()
	{
		return	"insert into mst_tax(tax_id, apply_date, tax_rate, reduced_tax,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				"(select coalesce(max(tax_id), 0) + 1 from mst_tax),\n" +
				SQLUtil.convertForSQLDateOnly(this.getApplyDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getTaxRate()) + ",\n" +
                                SQLUtil.convertForSQL(this.isReducedTax()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null)\n";
	}
	
	private String getUpdateSQL()
	{
		return	"update mst_tax\n" +
				"set apply_date = " + SQLUtil.convertForSQLDateOnly(this.getApplyDate()) + ",\n" +
				"tax_rate = " + SQLUtil.convertForSQL(this.getTaxRate()) + ",\n" +
                                "reduced_tax = " + SQLUtil.convertForSQL(this.isReducedTax()) + ",\n" +
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
			"order by apply_date, reduced_tax, tax_rate \n";
	}
        
        private static String getTaxBySaleDateSQL(java.util.Date date)
	{
		return	"select tax_id, apply_date, coalesce(tax_rate, 0) as tax_rate, reduced_tax\n" +
				"from mst_tax\n" +
				"where delete_date is null\n" +
				"and apply_date = (\n" +
				"select max(apply_date)\n" +
				"from mst_tax\n" +
				"where delete_date is null\n" +
				"and apply_date <= " + SQLUtil.convertForSQLDateOnly(date) + ")\n";
	}
        
        /**
	 * 文字列に変換する。（スタッフ名）
	 * @return スタッフ名
	 */
	public String toString()
	{
		return	this.getTaxRatePercent() + "%";
	}
        // IVS_LVTU end edit 2019/07/23 #97064 [gb]SPOS増税対応
}

/*
 * DataSlipStore.java
 *
 * Created on 2008/09/22, 10:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ryu
 */
public class DataStaffSales
{
	private int  shopId;
	private int  slipNo;
	private int  staffId;
	private Date salesDate;
	private int  confirmStaffId;

	private DataStaffSalesDetails details = new DataStaffSalesDetails(this);
	
	/** Creates a new instance of DataSlipStore */
	public DataStaffSales()
	{
	}
	
	public int getShopId()
	{
		return shopId;
	}
	
	public void setShopId(int shopId)
	{
		this.shopId = shopId;
	}
	
	public int getSlipNo()
	{
		return slipNo;
	}
	
	public void setSlipNo(int slipNo)
	{
		this.slipNo = slipNo;
	}

	public int getStaffId()
	{
		return staffId;
	}
	
	public void setStaffId(int staffId)
	{
		this.staffId = staffId;
	}
	
	public Date getSalesDate()
	{
		return salesDate;
	}
	
	public void setSalesDate(Date salesDate)
	{
		this.salesDate = salesDate;
	}
	
	public int getConfirmStaffId()
	{
		return confirmStaffId;
	}
	
	public void setConfirmStaffId(int confirmStaffId)
	{
		this.confirmStaffId = confirmStaffId;
	}

	public DataStaffSalesDetails getDetails()
	{
		return this.details;
	}

	public void addDetail(DataStaffSalesDetail d)
	{
		details.add(d);
	}

	public void removeDetail(DataStaffSalesDetail d)
	{
		details.remove(d);
	}

	public void removeDetail(int idx)
	{
		details.remove(idx);
	}

	/**
	 * êVãKì`ï[NOÇéÊìæÇ∑ÇÈÅB
	 * @return åüçıSQLï∂
	 */
	private String getNewSlipNoSQL()
	{
		return "select COALESCE(max(slip_no),0) + 1 as slip_no from data_staff_sales " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId());
	}
	
	/**
	 * Insertï∂ÇéÊìæÇ∑ÇÈÅB
	 * @return Insertï∂
	 */
	private String getInsertSQL()
	{
		return "insert into data_staff_sales("+
			"shop_id,"+
			"slip_no," +
			"staff_id,"+
			"sales_date,"+
			"confirm_staff_id,"+
			"insert_date,"+
			"update_date,"+
			"delete_date" +
			") values ("+
			SQLUtil.convertForSQL(this.getShopId()) + "," +
			SQLUtil.convertForSQL(this.getSlipNo()) + "," +
			SQLUtil.convertForSQL(this.getStaffId()) + "," +
			SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + "," +
			SQLUtil.convertForSQL(this.getConfirmStaffId()) + "," +
			"current_timestamp," +
			"current_timestamp," +
			"null)";
	}
	
	/**
	 * Updateï∂ÇéÊìæÇ∑ÇÈÅB
	 * @return Updateï∂
	 */
	private String getUpdateSQL()
	{
		return "update data_staff_sales set\n" +
			"staff_id = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
			", sales_date = " + SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + "\n" +
			", confirm_staff_id = " + SQLUtil.convertForSQL(this.getConfirmStaffId()) + "\n" +
			", update_date = current_timestamp" +
			", delete_date = null where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * Deleteï∂ÇéÊìæÇ∑ÇÈÅB
	 * @return Deleteï∂
	 */
	private String getDeleteSQL()
	{
		return "update data_staff_sales set " +
			" delete_date = current_timestamp where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * Selectï∂ÇéÊìæÇ∑ÇÈÅB
	 * @return Selectï∂
	 */
	private String getSelectSQL()
	{
		return "select * from data_staff_sales " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/*
	 *êVãKì`ï[î‘çÜÇê›íuÇ∑ÇÈ
	 */
	public void setNewSlipNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getNewSlipNoSQL());
		
		if(rs.next())
		{
			this.setSlipNo(rs.getInt("slip_no"));
		}
		
		rs.close();
	}
	
	/*
	 *ç∑çûÇ∑ÇÈ
	 */
	public boolean insert(ConnectionWrapper con) throws SQLException
	{
		if (con.executeUpdate(this.getInsertSQL()) < 0)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	/*
	 *çXêVÇ∑ÇÈ
	 */
	public boolean update(ConnectionWrapper con) throws SQLException
	{
		if (con.executeUpdate(this.getUpdateSQL()) < 0)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	/*
	 *çÌèúÇ∑ÇÈ
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if (con.executeUpdate(this.getDeleteSQL()) < 0)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	/*
	 *ìXï‹NOÇ∆ì`ï[î‘çÜÇÇ‡Ç∆Ç…ÅAì`ï[èÓïÒÇéÊìæÇ∑ÇÈ
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getSelectSQL());

		try
		{
			if(rs.next())
			{
				this.setData(rs);
			}
		}
		finally
		{
			rs.close();
		}

		details.load(con);
	}

	/**
	 * ResultSetWrapperÇ©ÇÁÉfÅ[É^ÇÉZÉbÉgÇ∑ÇÈ
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setShopId(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setSalesDate(rs.getDate("sales_date"));
		this.setStaffId(rs.getInt("staff_id"));
		this.setConfirmStaffId(rs.getInt("confirm_staff_id"));
	}
}

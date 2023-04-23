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
public class DataSlipShip
{
	private int		shopId;
	private int		slipNo;
	private int		supplierId;
	private Date	shipDate;
	private int		staffId;
	
	private List<DataSlipShipDetail> detailList = new ArrayList<DataSlipShipDetail>();
	
	/** Creates a new instance of DataSlipStore */
	public DataSlipShip()
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
	
	public int getSupplierId()
	{
		return supplierId;
	}
	
	public void setSupplierId(int supplierId)
	{
		this.supplierId = supplierId;
	}
	
	public Date getShipDate()
	{
		return shipDate;
	}
	
	public void setShipDate(Date shipDate)
	{
		this.shipDate = shipDate;
	}
	
	public int getStaffId()
	{
		return staffId;
	}
	
	public void setStaffId(int staffId)
	{
		this.staffId = staffId;
	}
	
	public void addDetail(DataSlipShipDetail detail)
	{
		detailList.add(detail);
	}
	
	public DataSlipShipDetail[] getDetail()
	{
		return detailList.toArray(new DataSlipShipDetail[detailList.size()]);
	}
	
	/**
	 * V‹K“`•[NO‚ğæ“¾‚·‚éB
	 * @return ŒŸõSQL•¶
	 */
	private String getNewSlipNoSQL()
	{
		return "select COALESCE(max(slip_no),0) + 1 as slip_no from data_slip_ship " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId());
	}
	
	/**
	 * Insert•¶‚ğæ“¾‚·‚éB
	 * @return Insert•¶
	 */
	private String getInsertSQL()
	{
		return "insert into data_slip_ship(" +
			"shop_id," +
			"slip_no," +
			"supplier_id," +
			"ship_date," +
			"staff_id," +
			"insert_date," +
			"update_date," +
			"delete_date" +
			") values (" +
			SQLUtil.convertForSQL(this.getShopId()) + "," +
			SQLUtil.convertForSQL(this.getSlipNo()) + "," +
			SQLUtil.convertForSQL(this.getSupplierId()) +"," +
			SQLUtil.convertForSQLDateOnly(this.getShipDate()) + "," +
			SQLUtil.convertForSQL(this.getStaffId()) + "," +
			"current_timestamp," +
			"current_timestamp," +
			"null)" ;
	}
	
	/**
	 * Update•¶‚ğæ“¾‚·‚éB
	 * @return Update•¶
	 */
	private String getUpdateSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            
            sql.append(" update data_slip_ship");
            sql.append(" set");
            sql.append("      ship_date = " + SQLUtil.convertForSQLDateOnly(this.getShipDate()));
            sql.append("     ,staff_id = " + SQLUtil.convertForSQL(this.getStaffId()));
            sql.append("     ,update_date = current_timestamp");
            sql.append("     ,delete_date = null");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
            sql.append("     and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()));

            return sql.toString();
	}
	
	/**
	 * Delete•¶‚ğæ“¾‚·‚éB
	 * @return Delete•¶
	 */
	private String getDeleteSQL()
	{
		return "update data_slip_ship set " +
			" delete_date = current_timestamp where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * Select•¶‚ğæ“¾‚·‚éB
	 * @return Select•¶
	 */
	private String getSelectSQL()
	{
		return "select * from data_slip_ship " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/*
	 *V‹K“`•[”Ô†‚ğİ’u‚·‚é
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
	 *·‚·‚é
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
	 *XV‚·‚é
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
	 *íœ‚·‚é
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
	 *“X•ÜNO‚Æ“`•[”Ô†‚ğ‚à‚Æ‚ÉA“`•[î•ñ‚ğæ“¾‚·‚é
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getSelectSQL());
		
		if(rs.next())
		{
			this.setData(rs);
			rs.close();
			return	true;
		}
		
		rs.close();
		return	false;
	}
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚é
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setShopId(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setSupplierId(rs.getInt("supplier_id"));
		this.setShipDate(rs.getDate("ship_date"));
		this.setStaffId(rs.getInt("staff_id"));
	}
}

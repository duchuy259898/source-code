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
	 * 新規伝票NOを取得する。
	 * @return 検索SQL文
	 */
	private String getNewSlipNoSQL()
	{
		return "select COALESCE(max(slip_no),0) + 1 as slip_no from data_slip_ship " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId());
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * Delete文を取得する。
	 * @return Delete文
	 */
	private String getDeleteSQL()
	{
		return "update data_slip_ship set " +
			" delete_date = current_timestamp where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return "select * from data_slip_ship " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/*
	 *新規伝票番号を設置する
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
	 *差込する
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
	 *更新する
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
	 *削除する
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
	 *店舗NOと伝票番号をもとに、伝票情報を取得する
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
	 * ResultSetWrapperからデータをセットする
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

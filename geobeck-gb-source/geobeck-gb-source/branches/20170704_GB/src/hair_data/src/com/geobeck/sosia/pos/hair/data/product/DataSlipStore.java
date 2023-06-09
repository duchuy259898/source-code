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
import java.util.Date;

/**
 *
 * @author ryu
 */
public class DataSlipStore
{
	
	private int		shopId;
	private int		slipNo;
	private int		supplierId;
	private Date	storeDate;
	private int		staffId;
	private Integer	orderSlipNo;
	private Integer shipSlipNo;
	private int		discount;
	
	/** Creates a new instance of DataSlipStore */
	public DataSlipStore()
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
	
	public Date getStoreDate()
	{
		return storeDate;
	}
	
	public void setStoreDate(Date storeDate)
	{
		this.storeDate = storeDate;
	}
	
	public int getStaffId()
	{
		return staffId;
	}
	
	public void setStaffId(int staffId)
	{
		this.staffId = staffId;
	}
	
	public Integer getOrderSlipNo()
	{
		return orderSlipNo;
	}
	
	public void setOrderSlipNo(Integer orderSlipNo)
	{
		this.orderSlipNo = orderSlipNo;
	}
	
	public Integer getShipSlipNo()
	{
		return shipSlipNo;
	}
	
	public void setShipSlipNo(Integer shipSlipNo)
	{
		this.shipSlipNo = shipSlipNo;
	}

	/**
	 * 新規伝票NOを取得する。
	 * @return 検索SQL文
	 */
	private String getNewSlipNoSQL()
	{
		return "select COALESCE(max(slip_no),0) + 1 as slip_no from data_slip_store " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId());
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String getInsertSQL()
	{
		return "insert into data_slip_store("+
			"shop_id,"+
			"slip_no," +
			"supplier_id,"+
			"store_date,"+
			"staff_id,"+
			"order_slip_no,"+
			"ship_slip_no,"+
			"discount,"+
			"insert_date,"+
			"update_date,"+
			"delete_date"+
			") values (" +
			SQLUtil.convertForSQL(this.getShopId()) + "," +
			SQLUtil.convertForSQL(this.getSlipNo()) + "," +
			SQLUtil.convertForSQL(this.getSupplierId()) +"," +
			SQLUtil.convertForSQLDateOnly(this.getStoreDate()) + "," +
			SQLUtil.convertForSQL(this.getStaffId()) + "," +
			SQLUtil.convertForSQL(this.getOrderSlipNo()) + "," +
			SQLUtil.convertForSQL(this.getShipSlipNo()) + "," +
			SQLUtil.convertForSQL(this.getDiscount()) + "," +
			"current_timestamp," +
			"current_timestamp," +
			"null)";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String getUpdateSQL()
	{
		return "update data_slip_store set\n" +
			" store_date = " + SQLUtil.convertForSQLDateOnly(this.getStoreDate()) + "\n" +
			", staff_id = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
			", discount = " + SQLUtil.convertForSQL(this.getDiscount()) + "\n" +
			", update_date = current_timestamp\n" +
			", delete_date = null\n" +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * 削除するSQLを取得する。
	 * @return Delete文
	 */
	private static String getDeleteSQL(int shopId, int slipNo)
	{
		return "update data_slip_store set " +
			" delete_date = current_timestamp where shop_id = " + SQLUtil.convertForSQL(shopId) +
			" and slip_no = " + SQLUtil.convertForSQL(slipNo);
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return "select * from data_slip_store " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectByShipSlipNoSQL()
	{
		return "select * from data_slip_store " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and ship_slip_no = " + SQLUtil.convertForSQL(this.getShipSlipNo());
	}

	public int getDiscount()
	{
		return discount;
	}
	
	public void setDiscount(int discount)
	{
		this.discount = discount;
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
		if (con.executeUpdate(this.getInsertSQL()) <= 0)
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
		if (con.executeUpdate(getDeleteSQL(this.getShopId(), this.getSlipNo())) < 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
		
	/**
	 * 伝票と詳細を削除する
	 */
	public static void deleteAllByShipSlipNo(ConnectionWrapper con, int shopId, int shipSlipNo) throws SQLException
	{
		con.executeUpdate(DataSlipStoreDetails.getDeleteSQLByShipSlipNo(shopId, shipSlipNo));
		con.executeUpdate(DataSlipStore.getDeleteByShipSlipNoSQL(shopId, shipSlipNo));
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
	
	/*
	 *店舗NOと伝票番号をもとに、伝票情報を取得する
	 */
	public boolean loadByShipSlipNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getSelectByShipSlipNoSQL());
		
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
		this.setStoreDate(rs.getDate("store_date"));
		this.setStaffId(rs.getInt("staff_id"));
		this.setOrderSlipNo(rs.getInt("order_slip_no"));
		this.setShipSlipNo(rs.getInt("ship_slip_no"));
		this.setDiscount(rs.getInt("discount"));
	}

	/**
	 *
	 */
	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getSelectSQL());
		if(rs.next())
		{
			rs.close();
			return	true;
		}
		
		rs.close();
		return	false;
	}

	/**
	 *
	 */
	private static String getDeleteByShipSlipNoSQL(int shopId, int slipNo)
	{
		return "update data_slip_store set\n" +
			" delete_date = current_timestamp\n" +
			" where shop_id = " + SQLUtil.convertForSQL(shopId) +
			" and ship_slip_no = " + SQLUtil.convertForSQL(slipNo);
	}

	/**
	 *
	 */
	private static String getPhysicalDeleteByShipSlipNoSQL(int shopId, int slipNo)
	{
		return "delete from data_slip_store\n" +
			" where shop_id = " + SQLUtil.convertForSQL(shopId) +
			" and ship_slip_no = " + SQLUtil.convertForSQL(slipNo);
	}

	/**
	 *更新する
	 */
	public static void physicalDeleteByShipSlipNo(ConnectionWrapper con, int shopId, int shipSlipNo) throws SQLException
	{
		con.executeUpdate(getPhysicalDeleteByShipSlipNoSQL(shopId, shipSlipNo));
	}
}

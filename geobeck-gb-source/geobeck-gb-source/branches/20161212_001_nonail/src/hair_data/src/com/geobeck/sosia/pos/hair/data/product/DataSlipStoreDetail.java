/*
 * DataSlipStoreDetail.java
 *
 * Created on 2008/09/22, 10:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author ryu
 */
public class DataSlipStoreDetail
{
	private int   shopId;
	private int  slipNo;
	private int  slipDetailNo;
	private int  itemId;
	private int  itemUseDivision;
	private int  costPrice;
	private int  inNum;
	private int  attachNum;
	private int  inClass;
	
	/** Creates a new instance of DataSlipStoreDetail */
	public DataSlipStoreDetail()
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
	
	public int getSlipDetailNo()
	{
		return slipDetailNo;
	}
	
	public void setSlipDetailNo(int slipDetailNo)
	{
		this.slipDetailNo = slipDetailNo;
	}
	
	public int getItemId()
	{
		return itemId;
	}
	
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}
	
	public int getItemUseDivision()
	{
		return itemUseDivision;
	}
	
	public void setItemUseDivision(int itemUseDivision)
	{
		this.itemUseDivision = itemUseDivision;
	}
	
	public int getCostPrice()
	{
		return this.costPrice;
	}
	
	public void setCostPrice(int costPrice)
	{
		this.costPrice = costPrice;
	}
	
	public int getInNum()
	{
		return inNum;
	}
	
	public void setInNum(int inNum)
	{
		this.inNum = inNum;
	}
	
	public int getAttachNum()
	{
		return attachNum;
	}
	
	public void setAttachNum(int attachNum)
	{
		this.attachNum = attachNum;
	}
	
	public int getInClass()
	{
		return inClass;
	}
	
	public void setInClass(int inClass)
	{
		this.inClass = inClass;
	}
	
	/**
	 * 新規詳細伝票NOを取得する。
	 * @return 検索SQL文
	 */
	private String getNewSlipDetailNoSQL()
	{
		return "select COALESCE(max(slip_detail_no),0) + 1 as slip_detail_no from data_slip_store_detail " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return "select * from data_slip_store_detail " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" and item_id = " + SQLUtil.convertForSQL(this.getItemId()) +
			" and item_use_division = " + SQLUtil.convertForSQL(this.getItemUseDivision()) +
			" order by slip_detail_no";
	}
	
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getInsertSQL()
	{
		return "insert into data_slip_store_detail (" +
			"shop_id," +
			"slip_no,"+
			"slip_detail_no,"+
			"item_id,"+
			"item_use_division,"+
			"in_num,"+
			"attach_num,"+
			"cost_price," +
			"in_class,"+
			"insert_date,"+
			"update_date,"+
			"delete_date"+
			") values (" + SQLUtil.convertForSQL(this.getShopId()) +
			"," + SQLUtil.convertForSQL(this.getSlipNo()) +
			"," + SQLUtil.convertForSQL(this.getSlipDetailNo()) +
			"," + SQLUtil.convertForSQL(this.getItemId()) +
			"," + SQLUtil.convertForSQL(this.getItemUseDivision()) +
			"," + SQLUtil.convertForSQL(this.getInNum()) +
			"," + SQLUtil.convertForSQL(this.getAttachNum()) +
			"," + SQLUtil.convertForSQL(this.getCostPrice()) +
			"," + SQLUtil.convertForSQL(this.getInClass()) +
			",current_timestamp" +
			",current_timestamp" +
			",null)";
	}
	
	/**
	 * Delete文を取得する。
	 * @return Delete文
	 */
	private String getDeleteSQL()
	{
		return " update data_slip_store_detail set "+
			" delete_date = current_timestamp " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
		//+ " and item_id = " + SQLUtil.convertForSQL(this.getItem_id());
	}

	/**
	 *
	 */
	private static String getPhysicalDeleteSQL(int shopId, int slipNo)
	{
		return "delete from data_slip_store_detail\n" +
			" where shop_id = " + SQLUtil.convertForSQL(shopId) +
			" and slip_no = " + SQLUtil.convertForSQL(slipNo);
	}

	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String getUpdateSQL()
	{
		return " update data_slip_store_detail set "+
			" in_num = " + SQLUtil.convertForSQL(this.getInNum()) +
			", attach_num = " + SQLUtil.convertForSQL(this.getAttachNum()) +
			", cost_price = " + SQLUtil.convertForSQL(this.getCostPrice()) +
			", update_date = current_timestamp" +
			", delete_date = null " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" and item_id = " + SQLUtil.convertForSQL(this.getItemId()) +
			" and item_use_division = " + SQLUtil.convertForSQL(this.getItemUseDivision());
	}
	
	/**
	 * 伝票詳細にデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 *新規詳細伝票番号を設置する
	 */
	public void setNewSlipDetailNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getNewSlipDetailNoSQL());
		
		if(rs.next())
		{
			this.setSlipDetailNo(rs.getInt("slip_detail_no"));
		}
		
		rs.close();
	}
	
	/**
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
	/**
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
	
	/**
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
	
	private String toDateStr(String str)
	{
		if (str == null) return null;
		
		return "'" + str + "'";
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
		this.setSlipDetailNo(rs.getInt("slip_detail_no"));
		this.setItemId(rs.getInt("item_id"));
		this.setItemUseDivision(rs.getInt("item_use_division"));
		this.setCostPrice(rs.getInt("cost_price"));
		this.setInNum(rs.getInt("in_num"));
		this.setAttachNum(rs.getInt("attach_num"));
		this.setInClass(rs.getInt("in_class"));
	}

	/**
	 *更新する
	 */
	public static void physicalDelete(ConnectionWrapper con, int shopId, int slipNo) throws SQLException
	{
		con.executeUpdate(getPhysicalDeleteSQL(shopId, slipNo));
	}
}

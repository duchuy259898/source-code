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
public class DataSlipShipDetail
{
	private int	shopId;
	private int	slipNo;
	private int	slipDetailNo;
	private int	itemId;
	private int	itemUseDivision;
	private int	outNum;
	private int	costPrice;
	private int	outClass;
        //IVS_LVTu start add 2015/10/16 Bug #43499
        private int     itemDivision = 0;

        public int getItemDivision() {
            return itemDivision;
        }

        public void setItemDivision(int itemDivision) {
            this.itemDivision = itemDivision;
        }
	//IVS_LVTu end add 2015/10/16 Bug #43499
	/** Creates a new instance of DataSlipStoreDetail */
	public DataSlipShipDetail()
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
	
	public int getOutNum()
	{
		return this.outNum;
	}
	
	public void setOutNum(int outNum)
	{
		this.outNum = outNum;
	}
	
	public int getCostPrice()
	{
		return this.costPrice;
	}
	
	public void setCostPrice(int costPrice)
	{
		this.costPrice = costPrice;
	}
	
	public int getOutClass()
	{
		return this.outClass;
	}
	
	public void setOutClass(int outClass)
	{
		this.outClass = outClass;
	}
	
	/**
	 * 新規詳細伝票NOを取得する。
	 * @return 検索SQL文
	 */
	private String getNewSlipDetailNoSQL()
	{
		return "select COALESCE(max(slip_detail_no),0) + 1 as slip_detail_no from data_slip_ship_detail " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return "select * from data_slip_ship_detail " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) +
			" and item_use_division = " + SQLUtil.convertForSQL(this.getItemUseDivision()) +
			" order by slip_detail_no";
	}
	
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getInsertSQL()
	{
		return "insert into  data_slip_ship_detail(" +
			"shop_id," +
			"slip_no,"+
			"slip_detail_no,"+
			"item_id,"+
			"item_use_division,"+
			"out_num,"+
			"cost_price,"+
			"out_class,"+
			"insert_date,"+
			"update_date,"+
			"delete_date"+
			") values (" +
			SQLUtil.convertForSQL(this.getShopId()) +
			"," + SQLUtil.convertForSQL(this.getSlipNo()) +
			"," + SQLUtil.convertForSQL(this.getSlipDetailNo()) +
			"," + SQLUtil.convertForSQL(this.getItemId()) +
			"," + SQLUtil.convertForSQL(this.getItemUseDivision()) +
			"," + SQLUtil.convertForSQL(this.getOutNum()) +
			"," + SQLUtil.convertForSQL(this.getCostPrice()) +
			"," + SQLUtil.convertForSQL(this.getOutClass()) +
			", current_timestamp" +
			", current_timestamp" +
			", null)";
	}
	
	/**
	 * Delete文を取得する。
	 * @return Delete文
	 */
	private String getDeleteSQL()
	{
		return " update data_slip_ship_detail set "+
			" delete_date = now() " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}

	private static String getPhysicalDeleteSQL(int shopId, int slipNo)
	{
		return "delete\n" +
			"from data_slip_ship_detail\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL(shopId) + "\n" +
			"and slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String getUpdateSQL()
	{
		return " update data_slip_store_detail set "+
			" out_num = " + SQLUtil.convertForSQL(this.getOutNum()) +
			", out_class = " + SQLUtil.convertForSQL(this.getOutClass()) +
			", update_date = current_timestamp" +
			", delete_date = null " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" and item_id = " + SQLUtil.convertForSQL(this.getItemId());
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
		this.setOutNum(rs.getInt("out_num"));
		this.setCostPrice(rs.getInt("cost_price"));
		this.setOutClass(rs.getInt("out_class"));
                //IVS_LVTu start add 2015/10/16 Bug #43499
                try {
                    this.setItemDivision(rs.getInt("item_division"));
                }catch(Exception e){}
                //IVS_LVTu end add 2015/10/16 Bug #43499
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
	
	  /*
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

	/**
	 *削除する
	 */
	public static boolean physicalDelete(ConnectionWrapper con, int shopId, int slipNo) throws SQLException	
	{
		if (con.executeUpdate(getPhysicalDeleteSQL(shopId, slipNo)) < 0)
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
}

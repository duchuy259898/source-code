/*
 * DataSlipStoreDetail.java
 *
 * Created on 2008/09/22, 10:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.product;

import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 *
 * @author ryu
 */
public class DataStaffSalesDetail extends MstItem
{
	private int			shopId;
	private int			slipNo;
	private int			slipDetailNo;
	private int			itemNum;
	private BigDecimal	itemValue;
	private BigDecimal	discountRate;
	private BigDecimal	discountValue;
	private int			salesItemUseDivision;

	/** Creates a new instance of DataSlipStoreDetail */
	public DataStaffSalesDetail()
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

	public void setItemNum(int itemNum)
	{
		this.itemNum = itemNum;
	}

	public int getItemNum()
	{
		return itemNum;
	}

	public BigDecimal getItemValue()
	{
		return itemValue;
	}

	public void setItemValue(BigDecimal itemValue)
	{
		this.itemValue = itemValue;
	}

	public BigDecimal getDiscountRate()
	{
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate)
	{
		this.discountRate = discountRate;
	}

	public void setDiscountValue(BigDecimal discountValue)
	{
		this.discountValue = discountValue;
	}

	public BigDecimal getDiscountValue()
	{
		return discountValue;
	}

	public void setSalesItemUseDivision(int salesItemUseDivision)
	{
		this.salesItemUseDivision = salesItemUseDivision;
	}

	public int getSalesItemUseDivision()
	{
		return this.salesItemUseDivision;
	}

	/**
	 * 新規詳細伝票NOを取得する。
	 * @return 検索SQL文
	 */
	private String getNewDetailNoSQL()
	{
		return "select COALESCE(max(slip_detail_no),0) + 1 as slip_detail_no from data_staff_sales_detail " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}

	/**
	 *
	 */
	private String getCountSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select\n")
			.append("count(1) as cnt\n")
			.append("from\n")
			.append("data_staff_sales_detail\n")
			.append("where\n")
			.append("shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append("\n")
			.append("and slip_no = ").append(SQLUtil.convertForSQL(this.getSlipNo())).append("\n")
			.append("and slip_detail_no = ").append(SQLUtil.convertForSQL(this.getSlipDetailNo())).append("\n");
		return new String(buf);
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return "select mic.item_class_name, d.item_use_division as sales_item_use_division, mi.item_use_division, d.*, mi.*\n" +
			"from mst_item mi left outer join mst_item_class mic on mi.item_class_id = mic.item_class_id, data_staff_sales_detail d\n" +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
			" and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n";
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getInsertSQL()
	{
		return "insert into  data_staff_sales_detail(" +
			"shop_id," +
			"slip_no,"+
			"slip_detail_no,"+
			"item_class_id,"+
			"item_id,"+
			"item_num,"+
			"item_value,"+
			"discount_rate,"+
			"discount_value,"+
			"item_use_division,"+
			"insert_date,"+
			"update_date,"+
			"delete_date"+
			") values (" +
			SQLUtil.convertForSQL(this.getShopId()) +
			"," + SQLUtil.convertForSQL(this.getSlipNo()) +
			"," + SQLUtil.convertForSQL(this.getSlipDetailNo()) +
			"," + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) +
			"," + SQLUtil.convertForSQL(this.getItemID()) +
			"," + SQLUtil.convertForSQL(this.getItemNum()) +
			"," + SQLUtil.convertForSQL(this.getItemValue().longValue()) +
			"," + SQLUtil.convertForSQL(this.getDiscountRate().doubleValue()) +
			"," + SQLUtil.convertForSQL(this.getDiscountValue().longValue()) +
			"," + SQLUtil.convertForSQL(this.getSalesItemUseDivision()) +
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
		return " update data_staff_sales_detail set "+
			" delete_date = current_timestamp " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
	}

	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String getUpdateSQL()
	{
		return " update data_staff_sales_detail set "+
			" item_num = " + SQLUtil.convertForSQL(this.getItemNum()) +
			", item_value = " + SQLUtil.convertForSQL(this.getItemValue().longValue()) +
			", discount_rate = " + SQLUtil.convertForSQL(this.getDiscountRate().doubleValue()) +
			", discount_value = " + SQLUtil.convertForSQL(this.getDiscountValue().longValue()) +
			", item_use_division = " + SQLUtil.convertForSQL(this.getSalesItemUseDivision()) +
			", update_date = current_timestamp" +
			", delete_date = null " +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo());
	}

	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);

		this.setShopId(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setSlipDetailNo(rs.getInt("slip_detail_no"));
		this.setItemNum(rs.getInt("item_num"));
		this.setItemValue( rs.getBigDecimal("item_value"));
		this.setDiscountRate(rs.getBigDecimal("discount_rate"));
		this.setDiscountValue(rs.getBigDecimal("discount_value"));
		this.setSalesItemUseDivision(rs.getInt("sales_item_use_division"));
	}

	public void setData(DataStaffSalesDetail d)
	{
		setData((MstItem) d);
		this.shopId = d.getShopId();
		this.slipNo = d.getSlipNo();
		this.slipDetailNo = d.getSlipDetailNo();
		this.itemNum = d.getItemNum();
		this.itemValue = d.getItemValue();
		this.discountRate = d.getDiscountRate();
		this.discountValue = d.getDiscountValue();
		this.salesItemUseDivision = d.getSalesItemUseDivision();
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
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getCountSQL());
		
		if(rs.next())
		{
			return rs.getInt("cnt") > 0;
		}
		return	false;
	}

	/**
	 *新規詳細伝票番号を設置する
	 */
	public void setNewSlipDetailNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getNewDetailNoSQL());
		
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
}

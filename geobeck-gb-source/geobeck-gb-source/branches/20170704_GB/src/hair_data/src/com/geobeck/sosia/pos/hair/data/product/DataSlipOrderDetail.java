/*
 * DataSlipOrderDetail.java
 *
 * Created on 2008/09/18, 14:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.product;

import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author mizukawa
 */
public class DataSlipOrderDetail extends MstItem
{
	private DataSlipOrder parent;
	
	private int orderNum;
	private int costPrice;
	private int orderItemUseDivision;
	
	/** Creates a new instance of DataSlipOrderDetail */
	public DataSlipOrderDetail()
	{
	}
	
	public DataSlipOrderDetail(DataSlipOrder parent)
	{
		this.parent = parent;
	}
	
	public DataSlipOrder getParent()
	{
		return this.parent;
	}
	
	public Integer getOrderNum()
	{
		return this.orderNum;
	}
	
	public void setOrderNum(int orderNum)
	{
		this.orderNum = orderNum;
	}
	
	public Integer getOrderItemUseDivision()
	{
		return this.orderItemUseDivision;
	}
	
	public void setOrderItemUseDivision(int orderItemUseDivision)
	{
		this.orderItemUseDivision = orderItemUseDivision;
	}
	
	public int getCostPrice()
	{
		return costPrice;
	}
	
	public void setCostPrice(int costPrice)
	{
		this.costPrice = costPrice;
	}
	
	public void setData(DataSlipOrderDetail data)
	{
		this.parent  = data.getParent();
		super.setData(this);
		this.orderNum = data.getOrderNum();
	}
	
	public void setData(DataSlipOrder parent, ResultSetWrapper rs) throws SQLException
	{
		this.parent = parent;
		super.setData(rs);
		
		this.orderItemUseDivision = rs.getInt("order_item_use_division");
		this.costPrice = rs.getInt("cost_price");
		this.orderNum = rs.getInt("order_num");
	}
	
	public void setData(DataSlipOrder parent, MstSupplierItem msi)
	{
		this.parent = parent;
		super.setData(msi);
	}
	
	public boolean register(ConnectionWrapper con, int slipDetailNo) throws SQLException
	{
		return con.executeUpdate(getInsertSQL(slipDetailNo)) == 1;
	}
	
	public String getInsertSQL(int slipDetailNo)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("insert into data_slip_order_detail (")
		.append("shop_id,\n")
		.append("slip_no,\n")
		.append("slip_detail_no,\n")
		.append("item_id,\n")
		.append("item_use_division,\n")
		.append("order_num,\n")
		.append("cost_price,\n")
		.append("insert_date,\n")
		.append("update_date,\n")
		.append("delete_date\n")
		.append(") values (\n")
		.append(SQLUtil.convertForSQL(parent.getShopId())).append(",\n")
		.append(SQLUtil.convertForSQL(parent.getSlipNo())).append(",\n")
		.append(SQLUtil.convertForSQL(slipDetailNo)).append(",\n")
		.append(SQLUtil.convertForSQL(getItemID())).append(",\n")
		.append(SQLUtil.convertForSQL(getOrderItemUseDivision())).append(",\n")
		.append(SQLUtil.convertForSQL(getOrderNum())).append(",\n")
		.append(SQLUtil.convertForSQL(getCostPrice())).append(",\n")
		.append("current_timestamp,\n")
		.append("current_timestamp,\n")
		.append("null\n")
		.append(")");
		return new String(buf);
	}
	
	public static String getSelectSQL(int shopId, int slipNo)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select\n")
		.append("d.item_use_division as order_item_use_division,\n")
		.append("d.*,\n")
		.append("mi.*\n")
		.append("from mst_item mi,\n")
		.append("data_slip_order_detail d\n")
		.append("where mi.item_id = d.item_id\n")
		.append("and d.delete_date is null\n")
		.append("and d.shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
		.append("and d.slip_no = ").append(SQLUtil.convertForSQL(slipNo)).append("\n");
		return new String(buf);
	}
	
	public static String getPhysicalDeleteSQL(int shopId, int slipNo)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("delete from data_slip_order_detail\n")
		.append("where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
		.append("and slip_no = ").append(SQLUtil.convertForSQL(slipNo)).append("\n");
		return new String(buf);
	}
	
	public static String getLogicalDeleteSQL(int shopId, int slipNo)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("update data_slip_order_detail set\n")
		.append("delete_date = current_timestamp\n")
		.append("where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
		.append("and slip_no = ").append(SQLUtil.convertForSQL(slipNo)).append("\n");
		return new String(buf);
	}
}

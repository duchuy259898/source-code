/*
 * DataSlipOrder.java
 *
 * Created on 2008/09/18, 13:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.product;

import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.apache.commons.lang.ObjectUtils;

/**
 *
 * @author mizukawa
 */
public class DataSlipOrder
{
	private Integer shopId;
	private Integer slipNo;
	private Integer supplierId;
	private Date	orderDate;
	private Integer staffId;

	private Set<DataSlipOrderDetail>	detailList = new TreeSet<DataSlipOrderDetail>(new Comparator<DataSlipOrderDetail>()
	{
		private final Integer ZERO_VALUE = Integer.valueOf(0);

		public int compare(DataSlipOrderDetail d1, DataSlipOrderDetail d2)
		{
			int d1ItemUseDivision = d1.getOrderItemUseDivision();
			int d1ItemClassDispSeq = (Integer) ObjectUtils.defaultIfNull(d1.getItemClass().getDisplaySeq(), ZERO_VALUE);
			int d1ItemDispSeq = d1.getDisplaySeq();

			int d2ItemUseDivision = d2.getOrderItemUseDivision();
			if (d1ItemUseDivision < d2ItemUseDivision)
			{
				return -1;
			}
			else if (d1ItemUseDivision > d2ItemUseDivision)
			{
				return 1;
			}
			else
			{
				int d2ItemClassDispSeq = (Integer) ObjectUtils.defaultIfNull(d2.getItemClass().getDisplaySeq(), ZERO_VALUE);
				if (d1ItemClassDispSeq < d2ItemClassDispSeq)
				{
					return -1;
				}
				else if (d1ItemClassDispSeq > d2ItemClassDispSeq)
				{
					return 1;
				}

				int d2ItemDispSeq = d1.getDisplaySeq();
				if (d1ItemDispSeq < d2ItemDispSeq)
				{
					return -1;
				}
				else if (d1ItemDispSeq > d2ItemDispSeq)
				{
					return 1;
				}

				return d1.getItemID().compareTo(d2.getItemID());
			}
		}
	});
	
	/** Creates a new instance of DataSlipOrder */
	public DataSlipOrder()
	{
	}
	
	public Integer getShopId()
	{
		return this.shopId;
	}
	
	public void setShopId(Integer shopId)
	{
		this.shopId = shopId;
	}
	
	public Integer getSlipNo()
	{
		return this.slipNo;
	}
	
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}
	
	public Integer getSupplierId()
	{
		return this.supplierId;
	}
	
	public void setSupplierId(Integer supplierId)
	{
		this.supplierId = supplierId;
	}
	
	public Date getOrderDate()
	{
		return this.orderDate;
	}
	
	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}
	
	public Integer getStaffId()
	{
		return this.staffId;
	}
	
	public void setStaffId(Integer staffId)
	{
		this.staffId = staffId;
	}
	
	public DataSlipOrderDetail[] getDetail()
	{
		return (DataSlipOrderDetail[]) detailList.toArray(new DataSlipOrderDetail[detailList.size()]);
	}
	
	/**
	 *
	 */
	public boolean load(int shopId, int slipNo)
	{
		ConnectionWrapper con = SystemInfo.getConnection();
		try
		{
			try
			{
				con.begin();
				load(con, shopId, slipNo);
				con.commit();
				return true;
			}
			catch (SQLException e)
			{
				con.rollback();
				throw e;
			}
			finally
			{
				con.close();
			}
		}
		catch (SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}
	
	/**
	 *
	 */
	public boolean load(ConnectionWrapper con, int shopId, int slipNo) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(getSelectSql(shopId, slipNo));
		try
		{
			if (rs.next())
			{
				setData(rs);
			}
			else
			{
				return false;
			}
		}
		finally
		{
			rs.close();
		}
		
		rs = con.executeQuery(DataSlipOrderDetail.getSelectSQL(shopId, slipNo));
		try
		{
			while (rs.next())
			{
				DataSlipOrderDetail detail = new DataSlipOrderDetail();
				detail.setData(this, rs);
				detailList.add(detail);
			}
		}
		finally
		{
			rs.close();
		}
		
		return true;
	}
	
	public void addDetail(DataSlipOrderDetail detail)
	{
		detailList.add(detail);
	}
	
	public void addDetail(MstSupplierItem item)
	{
		DataSlipOrderDetail detail = new DataSlipOrderDetail(this);
		detail.setData(item);
		
		detailList.add(detail);
	}
	
	public String getInsertSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("insert into data_slip_order (")
		.append("shop_id,\n")
		.append("slip_no,\n")
		.append("supplier_id,\n")
		.append("order_date,\n")
		.append("staff_id,\n")
		.append("insert_date,\n")
		.append("update_date,\n")
		.append("delete_date\n")
		.append(") values (\n")
		.append(SQLUtil.convertForSQL(this.shopId)).append(",\n")
		.append(SQLUtil.convertForSQL(this.slipNo)).append(",\n")
		.append(SQLUtil.convertForSQL(this.supplierId)).append(",\n")
		.append(SQLUtil.convertForSQL(this.orderDate)).append(",\n")
		.append(SQLUtil.convertForSQL(this.staffId)).append(",\n")
		.append("current_timestamp,\n")
		.append("current_timestamp,\n")
		.append("null\n")
		.append(")");
		return new String(buf);
	}
	
	public String getUpdateSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("update data_slip_order set\n")
		.append("supplier_id = ").append(SQLUtil.convertForSQL(this.supplierId)).append(",\n")
		.append("order_date = ").append(SQLUtil.convertForSQL(this.orderDate)).append(",\n")
		.append("staff_id = ").append(SQLUtil.convertForSQL(this.staffId)).append("\n")
		.append("where\n")
		.append("shop_id = ").append(SQLUtil.convertForSQL(this.shopId)).append("\n")
		.append("and slip_no = ").append(SQLUtil.convertForSQL(this.slipNo)).append("\n");
		return new String(buf);
	}
	
	public String getDeleteSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("update data_slip_order set\n")
		.append("delete_date = current_timestamp\n")
		.append("where\n")
		.append("shop_id = ").append(SQLUtil.convertForSQL(this.shopId)).append("\n")
		.append("and slip_no = ").append(SQLUtil.convertForSQL(this.slipNo)).append("\n");
		return new String(buf);
	}
	
	/**
	 *
	 */
	public String getSelectSql(Integer shopId, Integer slipNo)
	{
		return "select * from data_slip_order\n" +
			"where shop_id = " + SQLUtil.convertForSQL(shopId) + "\n" +
			"and slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
	
	/**
	 *
	 */
	public boolean exists(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(getSlipOrderExistanceSQL());
		if (rs.next())
		{
			return rs.getInt("cnt") > 0;
		}
		
		return false;
	}
	
	/**
	 *
	 */
	public String getSlipOrderExistanceSQL()
	{
		return "select count(1) as cnt from data_slip_order\n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.shopId) + "\n" +
			"and slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n";
	}
	
	/**
	 * slip_noÇÃç≈ëÂílÇìæÇÈ
	 */
	public int getMaxSlipNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(getMaxSlipNoSQL());
		if (rs.next())
		{
			return rs.getInt("max_slip_no");
		}
		
		return 1;
	}
	
	/**
	 * slip_noÇÃç≈ëÂílÇìæÇÈSQL
	 */
	public String getMaxSlipNoSQL()
	{
		return "select COALESCE(max(slip_no), 0) as max_slip_no from data_slip_order\n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.shopId) + "\n";
	}
	
	/**
	 *
	 */
	public void setData(DataSlipOrder data)
	{
		this.shopId = data.getShopId();
		this.slipNo = data.getSlipNo();
		this.supplierId = data.getSupplierId();
		this.orderDate = data.getOrderDate();
		this.staffId = data.getStaffId();
	}
	
	/**
	 *
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.shopId = rs.getInt("shop_id");
		this.slipNo = rs.getInt("slip_no");
		this.supplierId = rs.getInt("supplier_id");
		this.orderDate = rs.getDate("order_date");
		this.staffId = rs.getInt("staff_id");
	}
	
	/**
	 *
	 */
	public boolean register()
	{
		ConnectionWrapper con = SystemInfo.getConnection();
		try
		{
			try
			{
				con.begin();
				if (this.getSlipNo() == null)
				{
					setSlipNo(getMaxSlipNo(con) + 1);
				}
				
				if (exists(con))
				{
					if (con.executeUpdate(getUpdateSQL()) != 1)
					{
						con.rollback();
						return false;
					}
				}
				else
				{
					if (con.executeUpdate(getInsertSQL()) != 1)
					{
						con.rollback();
						return false;
					}
				}
				
				con.executeUpdate(DataSlipOrderDetail.getPhysicalDeleteSQL(this.getShopId(), this.getSlipNo()));
				int detailNo = 1;
				for (DataSlipOrderDetail detail : detailList)
				{
					if (!detail.register(con, detailNo++))
					{
						con.rollback();
						return false;
					}
				}
				
				con.commit();
				return true;
			}
			catch (Exception e)
			{
				con.rollback();
				throw e;
			}
			finally
			{
				con.close();
			}
		}
		catch (Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}
	
	public boolean delete()
	{
		ConnectionWrapper con = SystemInfo.getConnection();
		try
		{
			try
			{
				con.begin();
				if (con.executeUpdate(getDeleteSQL()) != 1)
				{
					con.rollback();
					return false;
				}
				
				con.executeUpdate(DataSlipOrderDetail.getLogicalDeleteSQL(this.getShopId(), this.getSlipNo()));
				
				con.commit();
				return true;
			}
			catch (Exception e)
			{
				con.rollback();
				throw e;
			}
			finally
			{
				con.close();
			}
		}
		catch (Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}
}

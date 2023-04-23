/*
 * DataSlipStoreDetails.java
 *
 * Created on 2008/09/29, 14:27
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

/**
 *
 * @author ryu
 */
public class DataStaffSalesDetails extends ArrayList<DataStaffSalesDetail>
{
	private DataStaffSales	parent;

	/** Creates a new instance of DataSlipStoreDetails */
	public DataStaffSalesDetails(DataStaffSales parent)
	{
		this.parent = parent;
	}

	/**
	 * ì`ï[èÓïÒÇì«Ç›çûÇﬁ
	 * @param con ConnectionWrapper
	 * @return true - ê¨å˜
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();

		ResultSetWrapper	rs	=	con.executeQuery(this.getDetailSelectSQL());
		try
		{
			while(rs.next())
			{
				DataStaffSalesDetail		ms		=	new DataStaffSalesDetail();
				ms.setData(rs);

				this.add(ms);
			}
		}
		finally
		{
			rs.close();
		}
	}

	/**
	 *
	 */
	public void deleteAll(ConnectionWrapper con, boolean logicalDelete) throws SQLException
	{
		if (logicalDelete)
		{
			con.executeUpdate(this.getLogicalDeleteAllSQL());
		}
		else
		{
			con.executeUpdate(this.getPhysicalDeleteAllSQL());
		}
		this.clear();
	}

	/**
	 *
	 */
	public String getDetailSelectSQL()
	{
		return	"select mic.item_class_name, d.item_use_division as sales_item_use_division, mi.item_use_division, d.*, mi.*\n" +
			"from mst_item mi left outer join mst_item_class mic on mi.item_class_id = mic.item_class_id, data_staff_sales_detail d\n" +
			" where shop_id = " + SQLUtil.convertForSQL(getParent().getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(getParent().getSlipNo()) +
			" and mi.item_id = d.item_id" +
			" order by slip_detail_no"
			;
	}

	public String getPhysicalDeleteAllSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("delete from data_staff_sales_detail\n")
			.append("where shop_id = ").append(SQLUtil.convertForSQL(getParent().getShopId())).append("\n")
			.append("and slip_no = ").append(SQLUtil.convertForSQL(getParent().getSlipNo())).append("\n");
		return new String(buf);
	}

	public String getLogicalDeleteAllSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("update data_staff_sales_detail set delete_date = current_timestamp\n")
			.append("where shop_id = ").append(SQLUtil.convertForSQL(getParent().getShopId())).append("\n")
			.append("and slip_no = ").append(SQLUtil.convertForSQL(getParent().getSlipNo())).append("\n");
		return new String(buf);
	}

	public DataStaffSales getParent()
	{
		return this.parent;
	}
}

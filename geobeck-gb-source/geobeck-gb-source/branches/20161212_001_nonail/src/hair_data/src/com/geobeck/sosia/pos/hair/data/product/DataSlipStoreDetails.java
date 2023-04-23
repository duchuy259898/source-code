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
public class DataSlipStoreDetails extends ArrayList<DataSlipStoreDetail>
{
	
	private int   shopId;
	private int  slipNo;
	
	
	/** Creates a new instance of DataSlipStoreDetails */
	public DataSlipStoreDetails()
	{
	}
	
	/**
	 * ì`ï[èÓïÒÇì«Ç›çûÇﬁ
	 * @param con ConnectionWrapper
	 * @return true - ê¨å˜
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
		
		if(isAddBlank)
		{
			this.add(new DataSlipStoreDetail());
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getDetailSelectSQL());
		
		while(rs.next())
		{
			DataSlipStoreDetail		ms		=	new DataSlipStoreDetail();
			ms.setData(rs);
			
			this.add(ms);
		}
		
		return	true;
	}
	
	public String getDetailSelectSQL()
	{
		return	"select *\n" +
			"from data_slip_store_detail\n" +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" order by slip_detail_no"
			;
	}

	public static String getDeleteSQLByShipSlipNo(int shopId, int shipSlipNo)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("update data_slip_store_detail set\n")
			.append(" delete_date = current_timestamp\n")
			.append(" where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
			.append(" and slip_no in (\n")
				.append("select slip_no from data_slip_store where shop_id = ").append(shopId).append("\n")
				.append(" and ship_slip_no = ").append(shipSlipNo).append("\n")
				.append(" and delete_date is null\n")
			.append(")\n");
		return new String(buf);
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
}

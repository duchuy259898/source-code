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
public class DataSlipShipDetails extends ArrayList<DataSlipShipDetail>
{
	
	private int   shopId;
	private int  slipNo;
	
	
	/** Creates a new instance of DataSlipStoreDetails */
	public DataSlipShipDetails()
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
			this.add(new DataSlipShipDetail());
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getDetailSelectSQL());
		
		while(rs.next())
		{
			DataSlipShipDetail		ms		=	new DataSlipShipDetail();
			ms.setData(rs);
			
			this.add(ms);
		}
		
		return	true;
	}
	
        //IVS_LVTu start edit 2015/10/16 Bug #43499
	public String getDetailSelectSQL()
	{
		//return	"select *\n" +
		//	"from data_slip_ship_detail\n" +
                return	"select dssd.*, ms.item_use_division as item_division \n" +
			"from data_slip_ship_detail dssd \n" +
                        "inner join mst_item ms on ms.item_id = dssd.item_id \n" +
			" where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
			" and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
			" order by slip_detail_no"
			;
	}
        //IVS_LVTu end edit 2015/10/16 Bug #43499
	
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

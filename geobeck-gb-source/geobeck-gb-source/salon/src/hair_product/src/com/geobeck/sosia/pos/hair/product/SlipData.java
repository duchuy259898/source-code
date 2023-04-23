/*
 * SlipData.java
 *
 * Created on 2008/09/24, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author s_matsumura
 */
public class SlipData
{
	private	int		shopID;
	private	Date	slipDate;
	private	int		slipNo;
	private	String	supplierName;
	private	String	staffName;
	
        //IVS_LVTu start add 2015/10/07 New request #43148
        private	Integer 	itemNum1 = 0;
        private	Integer 	itemNum2 = 0;
        private	Long     	costPriceTotal = 0l;

    public Integer getItemNum1() {
        return itemNum1;
    }

    public void setItemNum1(Integer itemNum1) {
        this.itemNum1 = itemNum1;
    }

    public Integer getItemNum2() {
        return itemNum2;
    }

    public void setItemNum2(Integer itemNum2) {
        this.itemNum2 = itemNum2;
    }

    public Long getCostPriceTotal() {
        return costPriceTotal;
    }

    public void setCostPriceTotal(Long costPriceTotal) {
        this.costPriceTotal = costPriceTotal;
    }
    //IVS_LVTu end add 2015/10/07 New request #43148
	/**
	 * Creates a new instance of SlipData
	 */
	public SlipData()
	{
	}
	
	public int getShopID()
	{
		return this.shopID;
	}
	
	public Date getSlipDate()
	{
		return this.slipDate;
	}
	
	public int getSlipNo()
	{
		return this.slipNo;
	}
	
	public String getSupplierName()
	{
		return this.supplierName;
	}
	
	public String getStaffName()
	{
		return this.staffName;
	}
	
	public void setData(ResultSetWrapper rs, SearchSlip.SlipType slipType) throws SQLException
	{
		shopID = rs.getInt("shop_id");
		slipDate = rs.getDate("slip_date");
		slipNo = rs.getInt("slip_no");
		supplierName = rs.getString("supplier_name");
		staffName = rs.getString("staff_name1") + "  " + rs.getString("staff_name2");
                //IVS_LVTu start add 2015/10/07 New request #43148
                switch (slipType)
		{
                    case STORE:
                        this.itemNum1 = rs.getInt("item_num_division1");
                        this.itemNum2 = rs.getInt("item_num_division2");
                        this.costPriceTotal = rs.getLong("cost_price_total");
                        break;
                    case SHIP:
                        this.itemNum1 = rs.getInt("item_num_division1");
                        this.itemNum2 = rs.getInt("item_num_division2");
                        break;
                    default:
                        break;
		}
                //IVS_LVTu end add 2015/10/07 New request #43148
	}
}

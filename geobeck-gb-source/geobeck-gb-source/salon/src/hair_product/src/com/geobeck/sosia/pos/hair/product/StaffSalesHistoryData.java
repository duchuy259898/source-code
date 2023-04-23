/*
 * StaffSalesHistoryData.java
 *
 * Created on 2008/09/25, 16:43
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
public class StaffSalesHistoryData
{
	private	int		shopID;              //ìXï‹
	private	int		slipNo;             //ì`ï[Np
	private	String	staffName;          //ÉXÉ^ÉbÉtñº
	private	Date	salesDate;           //ì˙ït
	private String	itemName;           //è§ïiñº
	private long	itemvalue;          //ã‡äz
	private long	discountvalue;      //äÑà¯ã‡äz
	private long	amount;
	private long	taxValue;
	
	/** Creates a new instance of StaffSalesHistoryData */
	public StaffSalesHistoryData()
	{
	}
	
	//ìXï‹
	public int getShopID()
	{
		return this.shopID;
	}
	
	//ì˙ït
	public Date getSalesDate()
	{
		return this.salesDate;
	}
	
	//ì`ï[No
	public int getSlipNo()
	{
		return this.slipNo;
	}
	
	//è§ïiñº
	public String getItemName() throws SQLException
	{
		return this.itemName;
	}
	
	//ÉXÉ^ÉbÉtñº
	public String getStaffName()
	{
		return this.staffName;
	}
	
	//è¨åv
	public long getItemValue()
	{
		return this.itemvalue;
	}
	
	//äÑà¯
	public long getDiscountValue()
	{
		return this.discountvalue;
	}
	
	//è¡îÔê≈
	public long getTaxValue()
	{
		return this.taxValue;
	}

	//ã‡äz
	public long getAmount()
	{
		return this.amount;
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		shopID = rs.getInt("shop_id");
		slipNo = rs.getInt("slip_no");
		staffName = rs.getString("staff_name");
		salesDate = rs.getDate("sales_date");
		itemName = rs.getString("item_name");
		itemvalue = rs.getLong("item_value");
		discountvalue = rs.getLong("discount_value");
		amount = rs.getLong("amount");
		taxValue = rs.getLong("tax_value");
	}
}

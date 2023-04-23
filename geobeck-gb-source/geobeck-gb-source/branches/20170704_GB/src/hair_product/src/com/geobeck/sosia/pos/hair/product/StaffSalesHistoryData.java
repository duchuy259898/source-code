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
	private	int		shopID;              //�X��
	private	int		slipNo;             //�`�[Np
	private	String	staffName;          //�X�^�b�t��
	private	Date	salesDate;           //���t
	private String	itemName;           //���i��
	private long	itemvalue;          //���z
	private long	discountvalue;      //�������z
	private long	amount;
	private long	taxValue;
	
	/** Creates a new instance of StaffSalesHistoryData */
	public StaffSalesHistoryData()
	{
	}
	
	//�X��
	public int getShopID()
	{
		return this.shopID;
	}
	
	//���t
	public Date getSalesDate()
	{
		return this.salesDate;
	}
	
	//�`�[No
	public int getSlipNo()
	{
		return this.slipNo;
	}
	
	//���i��
	public String getItemName() throws SQLException
	{
		return this.itemName;
	}
	
	//�X�^�b�t��
	public String getStaffName()
	{
		return this.staffName;
	}
	
	//���v
	public long getItemValue()
	{
		return this.itemvalue;
	}
	
	//����
	public long getDiscountValue()
	{
		return this.discountvalue;
	}
	
	//�����
	public long getTaxValue()
	{
		return this.taxValue;
	}

	//���z
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

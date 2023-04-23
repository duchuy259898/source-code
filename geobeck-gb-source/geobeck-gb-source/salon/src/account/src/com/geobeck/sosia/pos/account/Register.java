/*
 * Register.java
 *
 * Created on 2007/03/26, 9:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author katagiri
 */
public class Register extends DataRegister
{
	private static final String		REPORT_PATH	=	"/report/Register.jasper";
	private static final String		REPORT_NAME	=	"Register";
	
	private Integer				itemValue		=	0;
	private Integer				itemDiscount	=	0;
	private Integer				allDiscount		=	0;
	private Integer				taxValue		=	0;
	
	private	Integer				cashSales		=	0;
	private	Integer				cardSales		=	0;
	private	Integer				creditSales		=	0;
	private	Integer				cashCollect		=	0;
	private	Integer				cardCollect		=	0;
	private	Integer				creditCollect	=	0;
	private	Integer				billValue		=	0;
	
	/** Creates a new instance of Register */
	public Register(MstShop shop, GregorianCalendar manageDate)
	{
		super();
		this.setShop(shop);
		this.setManageDate(manageDate);
		this.load();
	}

	public Integer getItemValue()
	{
		return itemValue;
	}

	public void setItemValue(Integer itemValue)
	{
		this.itemValue = itemValue;
	}

	public Integer getItemDiscount()
	{
		return itemDiscount;
	}

	public void setItemDiscount(Integer itemDiscount)
	{
		this.itemDiscount = itemDiscount;
	}
	
	public Integer getItemSales()
	{
		return	itemValue - itemDiscount;
	}

	public Integer getAllDiscount()
	{
		return allDiscount;
	}

	public void setAllDiscount(Integer allDiscount)
	{
		this.allDiscount = allDiscount;
	}

	public Integer getTaxValue()
	{
		return taxValue;
	}

	public void setTaxValue(Integer taxValue)
	{
		this.taxValue = taxValue;
	}
	
	public Integer getTotalSales()
	{
		return	this.getItemSales() - this.getAllDiscount();
	}

	public Integer getCashSales()
	{
		return cashSales;
	}

	public void setCashSales(Integer cashSales)
	{
		this.cashSales = cashSales;
	}

	public Integer getCardSales()
	{
		return cardSales;
	}

	public void setCardSales(Integer cardSales)
	{
		this.cardSales = cardSales;
	}

	public Integer getCreditSales()
	{
		return creditSales;
	}

	public void setCreditSales(Integer creditSales)
	{
		this.creditSales = creditSales;
	}

	public Integer getCashCollect()
	{
		return cashCollect;
	}

	public void setCashCollect(Integer cashCollect)
	{
		this.cashCollect = cashCollect;
	}

	public Integer getCardCollect()
	{
		return cardCollect;
	}

	public void setCardCollect(Integer cardCollect)
	{
		this.cardCollect = cardCollect;
	}

	public Integer getCreditCollect()
	{
		return creditCollect;
	}

	public void setCreditCollect(Integer creditCollect)
	{
		this.creditCollect = creditCollect;
	}
	
	public Integer getCollectTotal()
	{
		return	this.getCashCollect() + this.getCardCollect() + this.getCreditCollect();
	}

	public Integer getBillValue()
	{
		return billValue;
	}

	public void setBillValue(Integer billValue)
	{
		this.billValue = billValue;
	}
	
	public Integer getCashTotal()
	{
		return	this.getCashSales() + this.getCashCollect();
	}
	
	public Integer getLogicalValue()
	{
		return	this.getBaseValue() + this.getCashTotal();
	}
	
	public Integer getPhysicalValue()
	{
		return	this.getTotalMoney();
	}
	
	public Integer getDiscrepancies()
	{
		return	this.getPhysicalValue() - this.getLogicalValue();
	}
	
	public boolean load()
	{
		boolean		result	=	false;
		
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			this.loadSales(con);
			this.loadPayment(con);
			super.load(con);
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	private boolean loadSales(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSalesSQL());

		if(rs.next())
		{
			this.setItemValue(rs.getInt("item_product_value"));
			this.setItemDiscount(rs.getInt("item_discount_value"));
			this.setAllDiscount(rs.getInt("all_discount_value"));
			this.setTaxValue(rs.getInt("tax_value"));

			result	=	true;
		}

		rs.close();
		
		return	result;
	}
	
	private String getSalesSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append("      sum(discount_detail_value_in_tax) as total_value");
            sql.append("     ,sum(case when product_division = 2 then detail_value_in_tax else 0 end) as item_product_value");
            sql.append("     ,sum(case when product_division = 2 then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as item_discount_value");
            sql.append("     ,sum((select discount_value from view_data_sales_valid where shop_id = a.shop_id and slip_no = a.slip_no)) as all_discount_value");
            sql.append("     ,sum(discount_detail_value_in_tax - discount_detail_value_no_tax) as tax_value");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            sql.append("     and sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));

            return sql.toString();
	}
	
	private boolean loadPayment(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getPaymentSQL());

		if(rs.next())
		{
			this.setCashSales(rs.getInt("cash_sales"));
			this.setCardSales(rs.getInt("card_sales"));
			this.setCreditSales(rs.getInt("credit_sales"));
			this.setCashCollect(rs.getInt("cash_collect"));
			this.setCardCollect(rs.getInt("card_collect"));
			this.setCreditCollect(rs.getInt("credit_collect"));
			this.setBillValue(rs.getInt("bill_value"));

			result	=	true;
		}

		rs.close();
		
		return	result;
	}
	
	
	private String getPaymentSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      sum(case dp.payment_no when 0 then dpd.cash_value - (case when dp.change_value < 0 then 0 else dp.change_value end) else 0 end) as cash_sales");
            sql.append("     ,sum(case dp.payment_no when 0 then dpd.card_value else 0 end) as card_sales");
            sql.append("     ,sum(case dp.payment_no when 0 then dpd.ecash_value + dpd.gift_value else 0 end) as credit_sales");

            sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.cash_value - (case when dp.change_value < 0 then 0 else dp.change_value end) end) as cash_collect");
            sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.card_value end) as card_collect");
            sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.ecash_value + dpd.gift_value end) as credit_collect");
            sql.append("     ,sum(case dp.payment_no when 0 then dp.bill_value else 0 end) as bill_value");

            sql.append(" from");
            sql.append("     data_payment dp");
            sql.append("         left outer join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      dpd.shop_id");
            sql.append("                     ,dpd.slip_no");
            sql.append("                     ,dpd.payment_no");
            sql.append("                     ,sum(case mpm.payment_class_id when 1 then dpd.payment_value else 0 end) as cash_value");
            sql.append("                     ,sum(case mpm.payment_class_id when 2 then dpd.payment_value else 0 end) as card_value");
            sql.append("                     ,sum(case mpm.payment_class_id when 3 then dpd.payment_value else 0 end) as ecash_value");
            sql.append("                     ,sum(case mpm.payment_class_id when 4 then dpd.payment_value else 0 end) as gift_value");
            sql.append("                 from");
            sql.append("                     data_payment_detail dpd");
            sql.append(" 				        inner join mst_payment_method mpm");
            sql.append(" 				                on mpm.payment_method_id = dpd.payment_method_id");
            sql.append(" 				        inner join data_payment dp");
            sql.append(" 				                on dpd.shop_id = dp.shop_id");
            sql.append("                                and dpd.slip_no = dp.slip_no");
            sql.append("                                and dpd.payment_no = dp.payment_no");
            sql.append("                                and dp.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            sql.append("                                and dp.payment_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
            sql.append("                 where");
            sql.append("                     dpd.delete_date is null");
            sql.append("                 group by");
            sql.append("                      dpd.shop_id");
            sql.append("                     ,dpd.slip_no");
            sql.append("                     ,dpd.payment_no");
            sql.append("             ) dpd");
            sql.append("              on dpd.shop_id = dp.shop_id");
            sql.append("             and dpd.slip_no = dp.slip_no");
            sql.append("             and dpd.payment_no = dp.payment_no");
            sql.append(" where");
            sql.append("         dp.delete_date is null");
            sql.append("     and dp.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            sql.append("     and dp.payment_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
            sql.append("     and exists");
            sql.append("         (");
            sql.append("             select 1");
            sql.append("             from");
            sql.append("                 view_data_sales_valid");
            sql.append("             where");
            sql.append("                     shop_id = dp.shop_id");
            sql.append("                 and slip_no = dp.slip_no");
            sql.append("                 and sales_date is not null");
            sql.append("         )");

            return sql.toString();
	}
	
	public boolean regist()
	{
		boolean	result	=	false;
		
		try
		{
			result	=	super.regist(SystemInfo.getConnection());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	public void print()
	{
		HashMap<String, Object>		param	=	new HashMap<String, Object>();
		
		param.put("shopName", this.getShop().getShopName());
		param.put("manageDate", String.format("%1$tY”N%1$tmŒŽ%1$td“ú", this.getManageDate()));
		
		this.putSalesData(param);
		this.putPaymentData(param);
		this.putMoneyData(param);
		
		InputStream		report		=	Register.class.getResourceAsStream(REPORT_PATH);
		String			fileName	=	REPORT_NAME + String.format("%1$tY%1$tm%1$td%2$ts",
				this.getManageDate(), new java.util.Date());
		
		Vector<Integer>	temp	=	new Vector<Integer>();
		temp.add(0);
		ReportManager.exportReport(report, fileName, ReportManager.PDF_FILE, param, temp);
	}
	
	public void putSalesData(HashMap<String, Object> param)
	{
		param.put("itemValue", this.getItemValue());
		param.put("itemDiscount", this.getItemDiscount());
		param.put("allDiscount", this.getAllDiscount());
		param.put("taxValue", this.getTaxValue());
	}
	
	public void putPaymentData(HashMap<String, Object> param)
	{
		param.put("cashPayment", this.getCashSales());
		param.put("cardPayment", this.getCardSales());
		param.put("creditPayment", this.getCreditSales());
		param.put("cashCollect", this.getCashCollect());
		param.put("cardCollect", this.getCardCollect());
		param.put("creditCollect", this.getCreditCollect());
		param.put("billValue", this.getBillValue());
	}
	
	public void putMoneyData(HashMap<String, Object> param)
	{
		param.put("baseValue", this.getBaseValue());
		param.put("moneyNumber", this.getMoney());
	}
}

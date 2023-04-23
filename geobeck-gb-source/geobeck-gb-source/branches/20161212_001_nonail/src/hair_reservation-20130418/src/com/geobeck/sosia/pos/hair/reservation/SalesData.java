/*
 * SalesData.java
 *
 * Created on 2006/07/07, 16:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;

import java.sql.*;
import java.util.logging.*;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 *
 * @author katagiri
 */
public class SalesData
{
	/**
	 * 店舗
	 */
	protected	MstShop			shop			=	new MstShop();
	protected	Integer			slipNo			=	-1;
	protected	MstCustomer		customer		=	new MstCustomer();
	protected	Long			technic			=	0l;
	protected	Long			item			=	0l;
	protected	Long			discount		=	0l;
	protected	Long			overallDiscount	=	0l;	// 予約ステータス売上一覧表用
	protected	Long			tax				=	0l;
        protected       MstStaff                staff                   =       new MstStaff();
        protected       boolean                designated              =        false;                  
	protected	Long			totalValue		=	0l;

	/** Creates a new instance of SalesData */
	public SalesData()
	{
	}
	
	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * 店舗を設定する。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	public Integer getSlipNo()
	{
		return slipNo;
	}

	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	public MstCustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	public Long getTechnic()
	{
		return technic;
	}

	public void setTechnic(Long technic)
	{
		this.technic = technic;
	}

	public Long getItem()
	{
		return item;
	}

	public void setItem(Long item)
	{
		this.item = item;
	}

	public Long getDiscount()
	{
		return discount;
	}

	public void setDiscount(Long discount)
	{
		this.discount = discount;
	}

	public Long getOverallDiscount()
	{
		return overallDiscount;
	}

	public void setOverallDiscount(Long overallDiscount)
	{
		this.overallDiscount = overallDiscount;
	}
	
	public Long getTax()
	{
		return	tax;
	}

	public void setTax(Long tax)
	{
		this.tax = tax;
	}
	
	public Long getTotal()
	{
//割引にたいして、税込みで引くか、税抜きで引くか対応
//		return	technic + item - discount;
//技術、商品で明細割引しているので明細割引しないように変更
//		return	getClaimValue(technic + item,discount + overallDiscount ,SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
		return	getClaimValue(technic + item, overallDiscount ,SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
	}

	public Long getTotal(Double taxRate)
	{
//技術、商品で明細割引しているので明細割引しないように変更
//		return	getClaimValue(technic + item,discount + overallDiscount ,taxRate);
		return	getClaimValue(technic + item, overallDiscount ,taxRate);
	}	

	
	public Long getTotalValue()
	{
		return totalValue;
	}
	
	public void setTotalValue(Long totalValue)
	{
		this.totalValue = totalValue;
	}
	
        public void setDesignated(boolean designated)
        {
                this.designated = designated;
        }
        
        public boolean getDesignated()
        {
                return designated;
        }
	
        public void setStaff(MstStaff staff)
        {
                this.staff=staff;
        }
        
        public MstStaff getStaff()
        {
                return staff;
        }
        
	public void clearValue()
	{
		this.setTechnic(0l);
		this.setItem(0l);
		this.setDiscount(0l);
		this.setOverallDiscount( 0l );
		this.setTax(0l);
		this.setTotalValue(0l);
	}
	
	public void addValue(SalesData sd)
	{
		technic			+=	sd.getTechnic();
		item			+=	sd.getItem();
		discount		+=	sd.getDiscount();
		overallDiscount		+=	sd.getOverallDiscount();
		tax			+=	sd.getTax();
		totalValue		+=	sd.getTotalValue();
	}

	/*
	 *価格より税込み、税抜きを考慮して割引を引いた値を返す
	 *@param value 価格
	 *@param discount 割引額
	 *@param taxRate 税率
	 *@return 価格より割引を引いた値
	 */
	public Long getClaimValue(Long value, Long discount, Double taxRate)
	{
		try
		{
			MstAccountSetting ma = new MstAccountSetting();
			ma.load(SystemInfo.getConnection());
			
			//外税表示
			if(ma.getDisplayPriceType() == 1)
			{
				//税抜きから割引
				if(ma.getDiscountType() == 1)
				{
					return ma.round((value - discount) * (1d + taxRate));
				}
				//税込みから割引
				else
				{
					return	ma.round(value.doubleValue() * (1d + taxRate)) - discount;
				}
			}
			//内税表示
			else
			{
				//税抜きから割引
				if(ma.getDiscountType() == 1)
				{
					return ma.round((Math.floor(value.doubleValue() / (1d + taxRate)) - discount.doubleValue()) * (1d + taxRate));
				}
				//税込みから割引
				else
				{
					return	value - discount;
				}
			}
			
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return 0l;	
	}
}

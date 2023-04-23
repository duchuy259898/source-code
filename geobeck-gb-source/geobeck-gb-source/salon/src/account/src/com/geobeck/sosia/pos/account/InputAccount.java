/*
 * InputAccount.java
 *
 * Created on 2006/05/09, 12:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.MstProduct;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * 伝票入力画面処理
 * @author katagiri
 */
public class InputAccount
{
	/**
	 * 伝票データ
	 */
	protected	DataSales		sales			=	new	DataSales(
			SystemInfo.getTypeID());
	
	/**
	 * 割引マスタ
	 */
	protected	MstDiscounts	discounts		=	new MstDiscounts();
	
	/**
	 * 合計
	 */
	protected	ArrayList<NameValue>	total	=	new ArrayList<NameValue>();
	
	/**
	 * 売掛金データ
	 */
	protected	DataPayment		bill			=	new DataPayment();
	
	/**
	 * スタッフリスト
	 */
	protected	MstStaffs		staffs			=	null;

	public MstStaffs getStaffs()
	{
		if(staffs == null)
		{
			staffs	=	new MstStaffs();
			
			staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
			
			try
			{
				staffs.load(SystemInfo.getConnection(), true);
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return staffs;
	}

	public void setStaffs(MstStaffs staffs)
	{
		this.staffs = staffs;
	}
	
	
	/** Creates a new instance of InputAccount */
	public InputAccount()
	{
		for(int i = 0; i < 5; i ++)	total.add(new NameValue());
	}
	
	/**
	 * 初期化処理を行う。
	 */
	public void init()
	{
		sales.clear();
		sales.addPayment(0, new DataPayment());
		
		try
		{
			ConnectionWrapper con	=	SystemInfo.getConnection();
			
			discounts.load(con);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e.getCause());
		}
		
		this.initTotal();
	}
	
	/**
	 * 合計を初期化する。
	 */
	protected void initTotal()
	{
		total.clear();
		total.add(new NameValue("商品合計", 0l));
		total.add(new NameValue("割引合計", 0l));
		total.add(new NameValue("請求金額", 0l));
		total.add(new NameValue("（内税）", 0l));
		for(MstPaymentClass mpc : SystemInfo.getPaymentClasses())
		{
			total.add(new NameValue(mpc.getPaymentClassName() + "支払", 0l));
		}
		total.add(new NameValue("お釣り", 0l));
	}

	/**
	 * 割引マスタを取得する。
	 * @return 割引マスタ
	 */
	public MstDiscounts getDiscounts()
	{
		return discounts;
	}

	/**
	 * 割引マスタをセットする。
	 * @param discounts 割引マスタ
	 */
	public void setDiscounts(MstDiscounts discounts)
	{
		this.discounts = discounts;
	}

	/**
	 * 伝票データを取得する。
	 * @return 伝票データ
	 */
	public DataSales getSales()
	{
		return sales;
	}

	/**
	 * 伝票データをセットする。
	 * @param sales 伝票データ
	 */
	public void setSales(DataSales sales)
	{
		this.sales = sales;
	}
	
	/**
	 * 伝票詳細データを追加する。
	 * @param mp 伝票詳細データ
	 */
	public void addSalesDetail(MstProduct mp)
	{
		DataSalesDetail		dsd	=	new DataSalesDetail(mp);
		dsd.setProductDivision(1);
		dsd.setProductNum(1);
		sales.add(dsd);
	}
	
	/**
	 * 合計をセットする。
	 */
	public void setTotal()
	{
		this.getTotal(0).setValue(sales.getSalesTotal());
		this.getTotal(1).setValue(sales.getDiscountTotal());
		this.getTotal(2).setValue(sales.getDiscountTotal());
		this.getTotal(2).setValue(this.getTotal(0).getValue()
				- this.getTotal(1).getValue());
		long	temp	=	this.getTotal(2).getValue();
		
		this.getTotal(4).setValue(sales.getTaxTotal(
				SystemInfo.getTaxRate(this.getSales().getSalesDate()),
				SystemInfo.getCurrentShop().getRounding()));
		
		try
		{
			//売掛金をセット
			if(temp < 0)
			{
				this.getSales().getPayment(0).setChangeValue(0l);
				this.getSales().getPayment(0).setBillValue(-temp);
			}
			else
			{
				this.getSales().getPayment(0).setChangeValue(temp);
				this.getSales().getPayment(0).setBillValue(0l);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public long getPaymentTotal()
	{
		try
		{
			return	this.getSales().getPayment(0).getPaymentTotal();
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	0l;
	}
	
	/**
	 * 合計を取得する。
	 * @return 合計
	 */
	public ArrayList<NameValue> getTotal()
	{
		return	total;
	}
	
	/**
	 * 合計を取得する。
	 * @param index インデックス
	 * @return 合計
	 */
	public NameValue getTotal(int index)
	{
		if(0 <= index && index < total.size())	return	total.get(index);
		return	null;
	}
	
	/**
	 * 登録処理を行う。
	 * @return true - 成功
	 */
	public boolean regist()
	{
		boolean		result	=	false;
		
		try
		{
			sales.getPayment(0).setPaymentDate(sales.getSalesDate());
			total.get(4).setValue(total.get(4).getValue()
					- total.get(total.size() - 1).getValue());
			
			if(0 < total.get(total.size() - 1).getValue() &&
					0 < sales.getPayment(0).get(0).getPaymentValue())
			{
				sales.getPayment(0).get(0).setPaymentValue(total.get(4).getValue());
			}
			
			//コネクションを取得
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				//トランザクション開始
				con.begin();
				
				if(sales.getSlipNo() == null)
				{
					sales.setNewSlipNo(con);
				}
				
				//売上Noを取得
				if(sales.registAll(con, 0))
				{
					//トランザクションコミット
					con.commit();
					result	=	true;
				}
				else
				{
					//トランザクションロールバック
					con.rollback();
				}
			}
			catch(Exception e)
			{
				//トランザクションロールバック
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}

	/**
	 * 売掛金を取得する。
	 * @return 売掛金
	 */
	public DataPayment getBill()
	{
		return bill;
	}

	/**
	 * 売掛金をセットする。
	 * @param bill 売掛金
	 */
	public void setBill(DataPayment bill)
	{
		this.bill = bill;
	}
	
	/**
	 * 売掛金を読み込む。
	 * @param customerID 顧客ＩＤ
	 */
	public void loadBill(Integer customerID)
	{
		Long	billValue	=	0l;
		Integer	slipNo		=	null;
		int		count		=	0;
		
		if(!customerID.equals(""))
		{
			try
			{
				ConnectionWrapper con	=	SystemInfo.getConnection();

				ResultSetWrapper	rs	=	con.executeQuery(BillsList.getSelectSQL(customerID));

				while(rs.next())
				{
					slipNo		=	rs.getInt("slip_no");
					billValue	+=	rs.getLong("bill_value_rest");

					count	++;
				}
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		
		if(count != 1)	slipNo	=	null;
		this.getBill().setSlipNo(slipNo);
		this.getBill().setBillValue(billValue);
	}
	
	/**
	 * データを読み込む。
	 * @param slipNo 伝票No.
	 */
	public void load(MstShop shop, Integer slipNo)
	{
		sales.setShop(shop);
		sales.setSlipNo(slipNo);
		
		try
		{
			//コネクションを取得
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			sales.loadAll(con, 0);
			//sales.getShop().load(con);
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		this.optimizePayment(0);
	}
	
	/**
	 * 支払データを最適化する。
	 * @param index 
	 */
	protected void optimizePayment(int index)
	{
		ArrayList<DataPaymentDetail>	temp	=	new ArrayList<DataPaymentDetail>();
		
		try
		{
			for(MstPaymentClass mpc : SystemInfo.getPaymentClasses())
			{
				boolean		isFind	=	false;

				for(int i = 0; i < sales.getPayment(index).size(); i ++)
				{
					DataPaymentDetail	dpd	=	sales.getPayment(index).get(i);
					if(mpc.getPaymentClassID() == dpd.getPaymentMethod().getPaymentClassID())
					{
						temp.add(dpd);
						sales.getPayment(index).remove(i);
						isFind	= true;
						break;
					}
				}

				if(!isFind)
				{
					temp.add(new DataPaymentDetail());
				}
			}

			for(DataPaymentDetail dpd : sales.getPayment(index))
			{
				temp.add(dpd);
			}

			temp.add(new DataPaymentDetail());

			sales.getPayment(index).clear();

			for(DataPaymentDetail dpd : temp)
			{
				sales.getPayment(index).add(dpd);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}

/*
 * DataSales.java
 *
 * Created on 2006/05/09, 9:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;


import com.geobeck.sosia.pos.master.account.MstDiscount;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.master.product.MstProduct;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;

/**
 * 伝票ヘッダデータ
 * @author katagiri
 */
public class DataSales extends ArrayList<DataSalesDetail>
{
	private	Integer			type		=	0;
	/**
	 * 店舗
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * 伝票No.
	 */
	protected	Integer			slipNo		=	null;
	/**
	 * 売上日
	 */
	protected	java.util.Date	salesDate	=	null;
	/**
	 * 顧客
	 */
	protected	MstCustomer		customer	=	new MstCustomer();
	/**
	 * 来店回数
	 */
	protected	Integer			visitNum	=	null;
	
	/**
	 * 割引データ
	 */
	protected	ArrayList<DataSalesDetail>		discounts	=	new ArrayList<DataSalesDetail>();
	/**
	 * 支払データ
	 */
	protected	HashMap<Integer, DataPayment>	payments	=	new HashMap<Integer, DataPayment>();
	
	/** Creates a new instance of DataSales */
	public DataSales(Integer type)
	{
		this.setType(type);
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
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
	 * 店舗をセットする。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * 伝票No.を取得する。
	 * @return 伝票No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * 伝票No.をセットする。
	 * @param slipNo 伝票No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * 売上日を取得する。
	 * @return 売上日
	 */
	public java.util.Date getSalesDate()
	{
		return salesDate;
	}

	/**
	 * 売上日をセットする。
	 * @param salesDate 売上日
	 */
	public void setSalesDate(java.util.Date salesDate)
	{
		this.salesDate = salesDate;
	}

	/**
	 * 顧客を取得する。
	 * @return 顧客
	 */
	public MstCustomer getCustomer()
	{
		return customer;
	}

	/**
	 * 顧客をセットする。
	 * @param customer 顧客
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	/**
	 * 来店回数を取得する。
	 * @return 来店回数
	 */
	public Integer getVisitNum()
	{
		return visitNum;
	}

	/**
	 * 来店回数をセットする。
	 * @param visitNum 来店回数
	 */
	public void setVisitNum(Integer visitNum)
	{
		this.visitNum = visitNum;
	}

	/**
	 * 割引データを取得する。
	 * @return 割引データ
	 */
	public ArrayList<DataSalesDetail> getDiscounts()
	{
		return discounts;
	}

	/**
	 * 割引データをセットする。
	 * @param discounts 割引データ
	 */
	public void setDiscounts(ArrayList<DataSalesDetail> discounts)
	{
		this.discounts = discounts;
	}

	/**
	 * 支払データを取得する。
	 * @return 支払データ
	 */
	public HashMap<Integer, DataPayment> getPayments()
	{
		return payments;
	}

	/**
	 * 支払データをセットする。
	 * @param payments 支払データ
	 */
	public void setPayments(HashMap<Integer, DataPayment> payments)
	{
		this.payments = payments;
	}

	/**
	 * 支払データを取得する。
	 * @param paymentNo 支払No.
	 * @return 支払データ
	 */
	public DataPayment getPayment(int paymentNo) throws Exception
	{
		return payments.get(paymentNo);
	}
	
	
	/**
	 * 割引データを追加する。
	 * @param md 割引マスタデータ
	 * @param value 割引金額
	 */
	public void addDiscount(MstDiscount md, Long value)
	{
		DataSalesDetail	dsd	=	new DataSalesDetail();
		
		dsd.setShop(this.getShop());
		dsd.setSlipNo(this.getSlipNo());
		dsd.setProductDivision(0);
		MstProduct	mp	=	new MstProduct();
		mp.setProductID(md.getDiscountID());
		mp.setProductName(md.getDiscountName());
		dsd.setProduct(mp);
		dsd.setProductValue(0l);
		dsd.setDiscountValue(value);
		discounts.add(dsd);
	}
	
	
	/**
	 * 支払明細データを追加する。
	 * @param paymentNo 支払No.
	 * @param payment 支払明細データ
	 */
	public void addPayment(int paymentNo, DataPayment payment)
	{
		payment.setShop(this.getShop());
		payment.setSlipNo(this.slipNo);
		payment.setPaymentNo(paymentNo);
		payments.put(paymentNo, payment);
	}
	
	/**
	 * 支払データの数を取得する。
	 * @return 支払データの数
	 */
	public int paymentSize()
	{
		return	this.payments.size();
	}
	
	/**
	 * 金額の合計を取得する。
	 * @return 売上金額の合計
	 */
	public long getValueTotal()
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			total	+=	dsd.getProductValue() * dsd.getProductNum();
		}
		
		return	total;
	}
	
	/**
	 * 売上金額の合計を取得する。
	 * @return 売上金額の合計
	 */
	public long getSalesTotal()
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			if(dsd.getProductValue() != null &&
					dsd.getProductNum() != null)
			{
				total	+=	dsd.getProductValue() * dsd.getProductNum();
			}
		}
		
		return	total;
	}
	
	/**
	 * 割引金額の合計を取得する。
	 * @return 割引金額の合計
	 */
	public long getDiscountTotal()
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			if(dsd.getDiscountValue() != null)
			{
				total	+=	dsd.getDiscountValue();
			}
		}
		
		for(DataSalesDetail dsd : discounts)
		{
			if(dsd.getDiscountValue() != null)
			{
				total	+=	dsd.getDiscountValue();
			}
		}
		
		return	total;
	}
	
	/**
	 * 税金の合計を取得する。
	 * @return 税金の合計
	 */
	public long getTaxTotal(Double taxRate, Integer rounding)
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			total	+=	dsd.getTax(taxRate, rounding);
		}
		
		return	total;
	}
	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstShop	ms	=	new MstShop();
		ms.setShopID(rs.getInt("shop_id"));
		this.setShop(ms);
		this.setSalesDate(rs.getDate("sales_date"));
		MstCustomer	mc	=	new MstCustomer();
		mc.setCustomerID(rs.getInt("customer_id"));
		mc.setCustomerName(0, rs.getString("customer_name1"));
		mc.setCustomerName(1, rs.getString("customer_name2"));
		this.setCustomer(mc);
		if(type == 1)
				this.setVisitNum(rs.getInt("visit_num"));
	}
	
	
	/**
	 * 新しい伝票No.をセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewSlipNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewSlipNoSQL());
		
		if(rs.next())
		{
			this.setSlipNo(rs.getInt("new_slip_no"));
		}
		
		rs.close();
	}
	
	/**
	 * 伝票ヘッダデータ、伝票明細データ、支払データ、支払明細データを登録する。
	 * @param con ConnectionWrapper
	 * @param paymentNo 支払No.
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con, int paymentNo) throws SQLException
	{
		if(!this.deleteAllChildren(con))
				return	false;
		
		if(!this.regist(con))	return	false;
		
		//売上明細テーブル
		for(DataSalesDetail dsd : this)
		{
			dsd.setShop(this.getShop());
			dsd.setSlipNo(this.getSlipNo());
			dsd.setNewSlipDetailNo(con);
			if(!dsd.regist(con))	return	false;
		}
		
		for(DataSalesDetail dsd : discounts)
		{
			if(dsd.getProduct().getProductID().equals("") ||
					dsd.getDiscountValue() == 0)	continue;
			
			dsd.setShop(this.getShop());
			dsd.setSlipNo(this.getSlipNo());
			dsd.setNewSlipDetailNo(con);
			if(!dsd.regist(con))	return	false;
		}
		
		DataPayment dp	=	this.getPayments().get(paymentNo);
		
		dp.setShop(this.getShop());
		dp.setSlipNo(this.getSlipNo());
		dp.registAll(con);
		
		return	true;
	}
	
	
	/**
	 * 登録処理を行う。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getUpdateSQL();
		}
		else
		{
			sql	=	this.getInsertSQL();
		}
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	/**
	 * 伝票ヘッダデータが存在するかを取得する。
	 * @param con ConnectionWrapper
	 * @return true - 存在する
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 伝票ヘッダデータ、伝票明細データ、支払データ、支払明細データを読み込む。
	 * @param con ConnectionWrapper
	 * @param paymentNo 支払No.
	 * @return true - 成功
	 * @throws java.lang.Exception Exception
	 */
	public boolean loadAll(ConnectionWrapper con, Integer paymentNo) throws Exception
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllSQL());
		
		if(rs.next())
		{
			this.setData(rs);
			
			rs.beforeFirst();
			
			while(rs.next())
			{
				DataSalesDetail	dsd	=	new DataSalesDetail();
				dsd.setData(rs);
				this.add(dsd);
			}
		}
		
		rs.close();
		
		this.loadDiscount(con);
		
		this.addPayment(0, new DataPayment());
		
		this.getPayment(0).loadAll(con);
		
		return	true;
	}
	
	/**
	 * 割引データを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean loadDiscount(ConnectionWrapper con) throws SQLException
	{
		discounts.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectDiscountSQL());
		
		while(rs.next())
		{
			DataSalesDetail	dsd	=	new DataSalesDetail();
			dsd.setDiscountData(rs);
			discounts.add(dsd);
		}
		
		rs.close();
		
		return	true;
	}
	
	
	/**
	 * 未回収の売掛金が存在するかを取得する。
	 * @param con ConnectionWrapper
	 * @param slipNo 伝票No.
	 * @return true - 存在する
	 * @throws java.sql.SQLException SQLException
	 */
	public static boolean isExistCollectedBill(ConnectionWrapper con,
			Integer shopID, Integer slipNo) throws SQLException
	{
		boolean	result	=	false;
		
		if(slipNo == null || slipNo < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(DataSales.getExistCollectedBillSQL(shopID, slipNo));

		if(rs.next())	result	=	(0 < rs.getInt("cnt"));
		
		rs.close();
		
		return	result;
	}
	
	
	/**
	 * 未回収の売掛金が存在するかを取得する。
	 * @param con ConnectionWrapper
	 * @return true - 存在する
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExistCollectedBill(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
		
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	result	=	(0 < rs.getInt("cnt"));
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * 伝票ヘッダデータを論理削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
		
		result	=	(con.executeUpdate(this.getDeleteSQL()) == 1);
		
		return	result;
	}
	
	/**
	 * 伝票明細データ、支払データ、支払明細データを論理削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean deleteAllChildren(ConnectionWrapper con) throws SQLException
	{
		if(!this.deleteDetail(con))
				return	false;
		
		if(!this.deletePayment(con))
				return	false;
		
		if(!this.deletePaymentDetail(con))
				return	false;
		
		return	true;
	}
	
	/**
	 * 売上ヘッダデータ、伝票明細データ、支払データ、支払明細データを論理削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean deleteAll(ConnectionWrapper con) throws SQLException
	{
		//売上明細、支払、支払明細
		if(!this.deleteAllChildren(con))
				return	false;
		//売上ヘッダ
		if(!this.delete(con))
				return	false;
		
		return	true;
	}
	
	
	/**
	 * 伝票明細データを論理削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean deleteDetail(ConnectionWrapper con) throws SQLException
	{
		con.execute(this.getDeleteDetailSQL());
		
		return	true;
	}
	
	
	/**
	 * 支払データを論理削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean deletePayment(ConnectionWrapper con) throws SQLException
	{
		con.execute(this.getDeletePaymentSQL());
		
		return	true;
	}
	
	
	/**
	 * 支払明細データを論理削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean deletePaymentDetail(ConnectionWrapper con) throws SQLException
	{
		con.execute(this.getDeletePaymentDetailSQL());
		
		return	true;
	}
	
	
	/**
	 * 新しい伝票No.を取得するＳＱＬ文を取得する。
	 * @return 新しい伝票No.を取得するＳＱＬ文
	 */
	public String getNewSlipNoSQL()
	{
		return	"select coalesce(max(slip_no), 0) + 1 as new_slip_no\n" +
				"from data_sales\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
	
	
	/**
	 * 伝票ヘッダデータを取得するＳＱＬ文を取得する。
	 * @return 伝票ヘッダデータを取得するＳＱＬ文
	 */
	public String getSelectSQL()
	{
		return	"select ds.*,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2\n" +
				"from data_sales ds\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = ds.customer_id\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * 伝票ヘッダデータをInsertするＳＱＬ文を取得する。
	 * @return 伝票ヘッダデータをInsertするＳＱＬ文
	 */
	public String getInsertSQL()
	{
		return	"insert into data_sales\n" +
				"(shop_id, slip_no, sales_date, customer_id,\n" +
				(type == 1 ? "visit_num,\n" : "") +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
				(type == 1 ? "(select count(*) + 1\n" +
						"from data_sales\n" +
						"where delete_date is null\n" +
						"and sales_date < " + SQLUtil.convertForSQL(this.getSalesDate()) + "\n" +
						"and customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ")\n" : "") +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * 伝票ヘッダデータをUpdateするＳＱＬ文を取得する。
	 * @return 伝票ヘッダデータをUpdateするＳＱＬ文
	 */
	public String getUpdateSQL()
	{
		return	"update data_sales\n" +
				"set\n" +
				"sales_date = " + SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + ",\n" +
				"customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
				(type == 1 ? "visit_num = (select count(*) + 1" +
						"from data_sales" +
						"where delete_date is null\n" +
						"and sales_date < " + SQLUtil.convertForSQL(this.getSalesDate()) + "\n" +
						"and customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ")\n" : "") +
				"update_date = current_timestamp\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * 売上ヘッダデータ、伝票明細データを取得するＳＱＬ文を取得する。
	 * @return 売上ヘッダデータ、伝票明細データを取得するＳＱＬ文
	 */
	public String getSelectAllSQL()
	{
		return	"select ds.shop_id,\n" +
				"ds.slip_no,\n" +
				"ds.sales_date,\n" +
				"ds.customer_id,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				(type == 1 ? "visit_num,\n" : "") +
				"dsd.slip_detail_no,\n" +
				"mp.product_class_id,\n" +
				"mpc.product_class_name,\n" +
				"dsd.product_division,\n" +
				"dsd.product_id,\n" +
				"mp.product_name,\n" +
				"dsd.product_num,\n" +
				"dsd.product_value,\n" +
				"dsd.discount_value,\n" +
				"dsd.staff_id,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2\n" +
				"from data_sales ds\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = ds.customer_id\n" +
				"left outer join data_sales_detail dsd\n" +
				"on dsd.shop_id = ds.shop_id\n" +
				"and dsd.slip_no = ds.slip_no\n" +
				"and dsd.product_division != 0\n" +
				"and dsd.delete_date is null\n" +
				"left outer join mst_product mp\n" +
				"on mp.product_id = dsd.product_id\n" +
				"left outer join mst_product_class mpc\n" +
				"on mpc.product_class_id = mp.product_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dsd.staff_id\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * 割引データを取得するＳＱＬ文を取得する。
	 * @return 割引データを取得するＳＱＬ文
	 */
	public String getSelectDiscountSQL()
	{
		return	"select dsd.slip_no,\n" +
				"dsd.slip_detail_no,\n" +
				"dsd.product_division,\n" +
				"dsd.product_id,\n" +
				"md.discount_name,\n" +
				"dsd.product_value,\n" +
				"dsd.discount_value,\n" +
				"dsd.staff_id,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2\n" +
				"from data_sales_detail dsd\n" +
				"left outer join mst_discount md\n" +
				"on md.discount_id = dsd.product_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dsd.staff_id\n" +
				"where dsd.delete_date is null\n" +
				"and dsd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dsd.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and dsd.product_division = 0\n";
	}
	
	/**
	 * 未回収の売掛金が存在するかを取得するＳＱＬ文を取得する。
	 * @param slipNo 伝票No.
	 * @return 未回収の売掛金が存在するかを取得するＳＱＬ文
	 */
	public static String getExistCollectedBillSQL(Integer shopID, Integer slipNo)
	{
		return	"select count(*) as cnt\n" +
				"from data_sales ds\n" +
				"inner join data_payment dp\n" +
				"on dp.shop_id = ds.shop_id\n" +
				"and dp.slip_no = ds.slip_no\n" +
				"and dp.payment_no > 0\n" +
				"and dp.delete_date is null\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(shopID) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
	
	/**
	 * 売上ヘッダデータを削除するＳＱＬ文を取得する。
	 * @return 売上ヘッダデータを削除するＳＱＬ文
	 */
	public String getDeleteSQL()
	{
		return	"update data_sales\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * 伝票明細データを論理削除するＳＱＬ文を取得する。
	 * @return 伝票明細データを論理削除するＳＱＬ文
	 */
	public String getDeleteDetailSQL()
	{
		return	"update data_sales_detail\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * 支払データを論理削除するＳＱＬ文を取得する。
	 * @return 支払データを論理削除するＳＱＬ文
	 */
	public String getDeletePaymentSQL()
	{
		return	"update data_payment\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no >= 0\n";
	}
	
	/**
	 * 支払明細データを論理削除するＳＱＬ文を取得する。
	 * @return 支払明細データを論理削除するＳＱＬ文
	 */
	public String getDeletePaymentDetailSQL()
	{
		return	"update data_payment_detail\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
}

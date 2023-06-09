/*
 * DataSalesDetail.java
 *
 * Created on 2006/05/09, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.util.*;

/**
 * 伝票明細データ
 * @author katagiri
 */
public class DataSalesDetail
{
	/**
	 * 店舗
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * 伝票No.
	 */
	protected	Integer		slipNo			=	null;
	/**
	 * 伝票明細No.
	 */
	protected	Integer		slipDetailNo	=	null;
	/**
	 * 伝票明細区分
	 */
	protected	Integer		productDivision	=	null;
	/**
	 * 商品データ
	 */
	protected	MstProduct	product			=	new MstProduct();
	/**
	 * 数量
	 */
	protected	Integer		productNum		=	null;
	/**
	 * 単価
	 */
	protected	Long		productValue	=	null;
	/**
	 * スタッフ
	 */
	protected	MstStaff	staff			=	new MstStaff();
	/**
	 * 割引金額
	 */
	protected	Long		discountValue	=	null;
	
	/**
	 * コンストラクタ
	 */
	public DataSalesDetail()
	{
	}
	/**
	 * コンストラクタ
	 * @param mp 商品データ
	 */
	public DataSalesDetail(MstProduct mp)
	{
		this.setProduct(mp);
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
	 * 伝票No.
	 * @return 伝票No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * 伝票No.
	 * @param slipNo 伝票No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * 伝票明細No.
	 * @return 伝票明細No.
	 */
	public Integer getSlipDetailNo()
	{
		return slipDetailNo;
	}

	/**
	 * 伝票明細No.
	 * @param slipDetailNo 伝票明細No.
	 */
	public void setSlipDetailNo(Integer slipDetailNo)
	{
		this.slipDetailNo = slipDetailNo;
	}

	/**
	 * 伝票明細区分
	 * @return 伝票明細区分
	 */
	public Integer getProductDivision()
	{
		return productDivision;
	}

	/**
	 * 伝票明細区分
	 * @param productDivision 伝票明細区分
	 */
	public void setProductDivision(Integer productDivision)
	{
		this.productDivision = productDivision;
	}

	/**
	 * 商品データ
	 * @return 商品データ
	 */
	public MstProduct getProduct()
	{
		return product;
	}

	/**
	 * 商品データ
	 * @param product 商品データ
	 */
	public void setProduct(MstProduct product)
	{
		this.product = product;
		this.productValue	=	product.getPrice();
	}

	/**
	 * 数量
	 * @return 数量
	 */
	public Integer getProductNum()
	{
		return productNum;
	}

	/**
	 * 数量
	 * @param productNum 数量
	 */
	public void setProductNum(Integer productNum)
	{
		this.productNum = productNum;
	}

	/**
	 * 単価
	 * @return 単価
	 */
	public Long getProductValue()
	{
		return productValue;
	}

	/**
	 * 単価
	 * @param productValue 単価
	 */
	public void setProductValue(Long productValue)
	{
		this.productValue = productValue;
	}

	/**
	 * 割引金額
	 * @return 割引金額
	 */
	public Long getDiscountValue()
	{
		return discountValue;
	}

	/**
	 * 割引金額
	 * @param discountValue 割引金額
	 */
	public void setDiscountValue(Long discountValue)
	{
		this.discountValue = discountValue;
	}

	/**
	 * スタッフ
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフ
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	/**
	 * 合計金額
	 * @return 合計金額
	 */
	public Long getTotal()
	{
		long	total	=	0;
		
		if(productNum != null && productValue != null)
		{
			total	=	productValue * productNum;
		}
		
		if(discountValue != null)
		{
			total	-=	discountValue;
		}
		
		return	total;
	}
	
	/**
	 * 税金
	 * @return 税金
	 */
	public Long getTax(Double taxRate, Integer rounding)
	{
		return	TaxUtil.getTax(this.getTotal(), taxRate, rounding);
	}
	
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setSlipDetailNo(rs.getInt("slip_detail_no"));
		this.setProductDivision(rs.getInt("product_division"));
		
		MstProductClass	mpc	=	new MstProductClass();
		MstProduct	mp	=	new MstProduct();
		mpc.setProductClassID(rs.getInt("product_class_id"));
		mpc.setProductClassName(rs.getString("product_class_name"));
		mp.setProductClass(mpc);
		mp.setProductID(rs.getInt("product_id"));
		mp.setProductName(rs.getString("product_name"));
		this.setProduct(mp);
		this.setProductNum(rs.getInt("product_num"));
		this.setProductValue(rs.getLong("product_value"));
		this.setDiscountValue(rs.getLong("discount_value"));
		
		MstStaff	ms	=	new MstStaff();
		ms.setStaffID(rs.getInt("staff_id"));
	}
	
	
	/**
	 * ResultSetWrapperから割引データを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setDiscountData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setSlipDetailNo(rs.getInt("slip_detail_no"));
		this.setProductDivision(rs.getInt("product_division"));
		
		MstProduct	mp	=	new MstProduct();
		mp.setProductID(rs.getInt("product_id"));
		mp.setProductName(rs.getString("discount_name"));
		this.setProduct(mp);
		this.setProductValue(0l);
		this.setProductNum(1);
		this.setDiscountValue(rs.getLong("product_value"));
		
		MstStaff	ms	=	new MstStaff();
		ms.setStaffID(rs.getInt("staff_id"));
		ms.setStaffName(0, rs.getString("staff_name1"));
		ms.setStaffName(1, rs.getString("staff_name2"));
	}
	
	
	/**
	 * 新しい伝票明細No.をセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewSlipDetailNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewSlipDetailNoSQL());
		
		if(rs.next())
		{
			this.setSlipDetailNo(rs.getInt("new_slip_detail_no"));
		}
		
		rs.close();
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
	 * 伝票明細データが存在するかを取得する。
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
	 * 新しい伝票明細No.を取得するＳＱＬ文を取得する。
	 * @return 新しい伝票明細No.を取得するＳＱＬ文
	 */
	public String getNewSlipDetailNoSQL()
	{
		return	"select coalesce(max(slip_detail_no), 0) + 1 as new_slip_detail_no\n" +
				"from data_sales_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * 伝票明細データを取得するＳＱＬ文を取得する。
	 * @return 伝票明細データを取得するＳＱＬ文
	 */
	public String getSelectSQL()
	{
		return	"select dsd.*,\n" +
				"mp.product_class_id,\n" +
				"mpc.product_class_name,\n" +
				"mp.product_name,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2\n" +
				"from data_sales_detail dsd\n" +
				"left outer join mst_product mp\n" +
				"on mp.product_id = dsd.product_id\n" +
				"left outer join mst_product_class mpc\n" +
				"on mpc.product_class_id = mp.product_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dsd.staff_id\n" +
				"where dsd.delete_date is null\n" +
				"and dsd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dsd.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and dsd.slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo());
	}
	
	/**
	 * 伝票明細データをInsertするＳＱＬ文を取得する。
	 * @return 伝票明細データをInsertするＳＱＬ文
	 */
	public String getInsertSQL()
	{
		return	"insert into data_sales_detail\n" +
				"(shop_id, slip_no, slip_detail_no, product_division, product_id,\n" +
				"product_num, product_value, discount_value, staff_id,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipDetailNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductDivision()) + ",\n" +
				SQLUtil.convertForSQL(this.getProduct().getProductID()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductNum(), "0") + ",\n" +
				SQLUtil.convertForSQL(this.getProductValue(), "0") + ",\n" +
				SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n" +
				SQLUtil.convertForSQL((this.getStaff().getStaffID().equals("") ?
						null : this.getStaff().getStaffID())) + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * 伝票明細データをUpdateするＳＱＬ文を取得する。
	 * @return 伝票明細データをUpdateするＳＱＬ文
	 */
	public String getUpdateSQL()
	{
		return	"update data_sales_detail\n" +
				"set\n" +
				"product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + ",\n" +
				"product_id = " + SQLUtil.convertForSQL(this.getProduct().getProductID()) + ",\n" +
				"product_num = " + SQLUtil.convertForSQL(this.getProductNum(), "0") + ",\n" +
				"product_value = " + SQLUtil.convertForSQL(this.getProductValue(), "0") + ",\n" +
				"discount_value = " + SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo());
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataSalesDetail other = (DataSalesDetail) obj;
        if (this.slipNo != other.slipNo && (this.slipNo == null || !this.slipNo.equals(other.slipNo))) {
            return false;
        }
        if (this.slipDetailNo != other.slipDetailNo && (this.slipDetailNo == null || !this.slipDetailNo.equals(other.slipDetailNo))) {
            return false;
        }
        return true;
    }
        
        
}

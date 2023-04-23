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
 * “`•[–¾×ƒf[ƒ^
 * @author katagiri
 */
public class DataSalesDetail
{
	/**
	 * “X•Ü
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * “`•[No.
	 */
	protected	Integer		slipNo			=	null;
	/**
	 * “`•[–¾×No.
	 */
	protected	Integer		slipDetailNo	=	null;
	/**
	 * “`•[–¾×‹æ•ª
	 */
	protected	Integer		productDivision	=	null;
	/**
	 * ¤•iƒf[ƒ^
	 */
	protected	MstProduct	product			=	new MstProduct();
	/**
	 * ”—Ê
	 */
	protected	Integer		productNum		=	null;
	/**
	 * ’P‰¿
	 */
	protected	Long		productValue	=	null;
	/**
	 * ƒXƒ^ƒbƒt
	 */
	protected	MstStaff	staff			=	new MstStaff();
	/**
	 * Š„ˆø‹àŠz
	 */
	protected	Long		discountValue	=	null;
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public DataSalesDetail()
	{
	}
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 * @param mp ¤•iƒf[ƒ^
	 */
	public DataSalesDetail(MstProduct mp)
	{
		this.setProduct(mp);
	}

	/**
	 * “X•Ü‚ğæ“¾‚·‚éB
	 * @return “X•Ü
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * “X•Ü‚ğƒZƒbƒg‚·‚éB
	 * @param shop “X•Ü
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * “`•[No.
	 * @return “`•[No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * “`•[No.
	 * @param slipNo “`•[No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * “`•[–¾×No.
	 * @return “`•[–¾×No.
	 */
	public Integer getSlipDetailNo()
	{
		return slipDetailNo;
	}

	/**
	 * “`•[–¾×No.
	 * @param slipDetailNo “`•[–¾×No.
	 */
	public void setSlipDetailNo(Integer slipDetailNo)
	{
		this.slipDetailNo = slipDetailNo;
	}

	/**
	 * “`•[–¾×‹æ•ª
	 * @return “`•[–¾×‹æ•ª
	 */
	public Integer getProductDivision()
	{
		return productDivision;
	}

	/**
	 * “`•[–¾×‹æ•ª
	 * @param productDivision “`•[–¾×‹æ•ª
	 */
	public void setProductDivision(Integer productDivision)
	{
		this.productDivision = productDivision;
	}

	/**
	 * ¤•iƒf[ƒ^
	 * @return ¤•iƒf[ƒ^
	 */
	public MstProduct getProduct()
	{
		return product;
	}

	/**
	 * ¤•iƒf[ƒ^
	 * @param product ¤•iƒf[ƒ^
	 */
	public void setProduct(MstProduct product)
	{
		this.product = product;
		this.productValue	=	product.getPrice();
	}

	/**
	 * ”—Ê
	 * @return ”—Ê
	 */
	public Integer getProductNum()
	{
		return productNum;
	}

	/**
	 * ”—Ê
	 * @param productNum ”—Ê
	 */
	public void setProductNum(Integer productNum)
	{
		this.productNum = productNum;
	}

	/**
	 * ’P‰¿
	 * @return ’P‰¿
	 */
	public Long getProductValue()
	{
		return productValue;
	}

	/**
	 * ’P‰¿
	 * @param productValue ’P‰¿
	 */
	public void setProductValue(Long productValue)
	{
		this.productValue = productValue;
	}

	/**
	 * Š„ˆø‹àŠz
	 * @return Š„ˆø‹àŠz
	 */
	public Long getDiscountValue()
	{
		return discountValue;
	}

	/**
	 * Š„ˆø‹àŠz
	 * @param discountValue Š„ˆø‹àŠz
	 */
	public void setDiscountValue(Long discountValue)
	{
		this.discountValue = discountValue;
	}

	/**
	 * ƒXƒ^ƒbƒt
	 * @return ƒXƒ^ƒbƒt
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * ƒXƒ^ƒbƒt
	 * @param staff ƒXƒ^ƒbƒt
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	/**
	 * ‡Œv‹àŠz
	 * @return ‡Œv‹àŠz
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
	 * Å‹à
	 * @return Å‹à
	 */
	public Long getTax(Double taxRate, Integer rounding)
	{
		return	TaxUtil.getTax(this.getTotal(), taxRate, rounding);
	}
	
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğæ“¾‚·‚éB
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
	 * ResultSetWrapper‚©‚çŠ„ˆøƒf[ƒ^‚ğæ“¾‚·‚éB
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
	 * V‚µ‚¢“`•[–¾×No.‚ğƒZƒbƒg‚·‚éB
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
	 * “o˜^ˆ—‚ğs‚¤B
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
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
	 * “`•[–¾×ƒf[ƒ^‚ª‘¶İ‚·‚é‚©‚ğæ“¾‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ‘¶İ‚·‚é
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
	 * V‚µ‚¢“`•[–¾×No.‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return V‚µ‚¢“`•[–¾×No.‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	public String getNewSlipDetailNoSQL()
	{
		return	"select coalesce(max(slip_detail_no), 0) + 1 as new_slip_detail_no\n" +
				"from data_sales_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * “`•[–¾×ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return “`•[–¾×ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶
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
	 * “`•[–¾×ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return “`•[–¾×ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶
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
	 * “`•[–¾×ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return “`•[–¾×ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶
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

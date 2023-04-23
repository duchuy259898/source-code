/*
 * StaffSalesHistory.java
 *
 * Created on 2008/09/25, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author s_matsumura
 */
public class StaffSalesHistory extends ArrayList<StaffSalesHistoryData>
{
	
	protected MstShop				shop		=	new MstShop();
	
	/**
	 * ���t
	 */
	protected	java.util.Date[]	SearchDate	=	{	null, null	};
	
	/**
	 * �`�[No.
	 */
	protected	Integer[]			slipNO		=	{	null, null	};
	
	/**
	 * �X�^�b�t
	 */
	protected	MstStaff			staff		=	new MstStaff();
	
	/** Creates a new instance of StaffSalesHistory */
	public StaffSalesHistory()
	{
	}
	
	//�X��
	public MstShop getShop()
	{
		return shop;
	}
	
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	/**
	 * ���t
	 * @return ���t
	 */
	public java.util.Date[] getSearchDate()
	{
		return SearchDate;
	}
	
	/**
	 * ���t
	 * @param SearchDate ���t
	 */
	public void setSearchDate(java.util.Date[] SearchDate)
	{
		this.SearchDate = SearchDate;
	}
	
	/**
	 * ���t
	 * @param index �C���f�b�N�X
	 * @return ���t
	 */
	public java.util.Date getSearchDate(int index)
	{
		return SearchDate[index];
	}
	
	/**
	 * ���t
	 * @param index �C���f�b�N�X
	 * @param SearchDate ���t
	 */
	public void setSearchDate(int index, java.util.Date SearchDate)
	{
		this.SearchDate[index] = SearchDate;
	}
	
	/**
	 * �`�[No.
	 * @return �`�[No.
	 */
	public Integer[] getSlipNO()
	{
		return slipNO;
	}
	
	/**
	 * �`�[No.
	 * @param slipNO �`�[No.
	 */
	public void setSlipNO(Integer[] slipNO)
	{
		this.slipNO = slipNO;
	}
	
	/**
	 * �`�[No.
	 * @param index �C���f�b�N�X
	 * @return �`�[No.
	 */
	public Integer getSlipNO(int index)
	{
		return slipNO[index];
	}
	
	/**
	 * �`�[No.
	 * @param index �C���f�b�N�X
	 * @param slipNO �`�[No.
	 */
	public void setSlipNO(int index, Integer slipNO)
	{
		this.slipNO[index] = slipNO;
	}
	
	/**
	 * �X�^�b�t
	 * @return �X�^�b�t
	 */
	public MstStaff getStaff()
	{
		return staff;
	}
	
	/**
	 * �X�^�b�t
	 * @param supplier �X�^�b�t
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		String strSQL = "";
		
		strSQL = this.getSelectSQL();
		
		ResultSetWrapper rs = con.executeQuery(strSQL);
		
		while(rs.next())
		{
			StaffSalesHistoryData sshd = new StaffSalesHistoryData();
			sshd.setData(rs);
			this.add(sshd);
		}
		
		rs.close();
	}
	
	
	//SQL
	private String getSelectSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT\n")
			.append("dss.shop_id\n")
			.append(", dss.slip_no\n")
			.append(", ms.staff_name1 || ' ' || ms.staff_name2 as staff_name\n")
			.append(", dss.sales_date\n")
			.append(", dssd.item_value\n")
			.append(", dssd.discount_value\n")
			.append(", dssd.item_value - dssd.discount_value as amount\n")
			.append(", floor((dssd.item_value - dssd.discount_value) * get_tax_rate(dss.sales_date)) as tax_value\n")
			.append(", (\n")
			.append("SELECT\n")
			.append("item_name\n")
			.append("FROM\n")
			.append("mst_item\n")
			.append("WHERE\n")
			.append("mst_item.item_id = (\n")
			.append("SELECT\n")
			.append("item_id\n")
			.append("FROM\n")
			.append("data_staff_sales_detail\n")
			.append("WHERE\n")
			.append("data_staff_sales_detail.shop_id = dssd.shop_id\n")
			.append("AND data_staff_sales_detail.slip_no = dssd.slip_no\n")
			.append("AND data_staff_sales_detail.slip_detail_no = dssd.detail_minno\n")
			.append(")\n")
			.append(") || case when dssd.detail_count >= 2 then '�A��' else '' end as item_name\n")
			.append("FROM\n")
			.append("data_staff_sales dss\n")
			.append(", (\n")
			.append("SELECT\n")
			.append("a.shop_id\n")
			.append(", a.slip_no\n")
			.append(", COUNT(b.slip_detail_no) as detail_count\n")
			.append(", MIN(b.slip_detail_no) as detail_minno\n")
			.append(", SUM(b.item_num * b.item_value) as item_value\n")
			.append(", SUM(b.discount_value) as discount_value\n")
			.append("FROM\n")
			.append("data_staff_sales a\n")
			.append(", data_staff_sales_detail b\n")
			.append("WHERE\n")
			.append("a.shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID())).append("\n");
		if(SearchDate[0] != null)
		{
			buf.append("AND\n")
				.append(SQLUtil.convertForSQLDateOnly(SearchDate[0])).append(" <= a.sales_date\n");
		}
		if(SearchDate[1] != null)
		{
			buf.append("AND\n")
				.append("a.sales_date <= ").append(SQLUtil.convertForSQLDateOnly(SearchDate[1])).append("\n");
		}
		if(slipNO[0] != null)
		{
			buf.append("AND\n")
				.append(SQLUtil.convertForSQL(slipNO[0])).append(" <= a.slip_no \n");
		}
		if(slipNO[1] != null)
		{
			buf.append("AND\n")
				.append("a.slip_no <= ").append(SQLUtil.convertForSQL(slipNO[1])).append("\n");
		}
		if(staff.getStaffID() != null)
		{
			buf.append("AND\n")
				.append("a.staff_id = ").append(SQLUtil.convertForSQL(staff.getStaffID())).append("\n");
		}
		buf.append("AND a.shop_id = b.shop_id\n")
			.append("AND a.slip_no = b.slip_no\n")
			.append("AND a.delete_date is null\n")
			.append("AND b.delete_date is null\n")
			.append("GROUP BY\n")
			.append("a.shop_id\n")
			.append(", a.slip_no\n")
			.append(") dssd\n")
			.append(", mst_staff ms\n")
			.append("WHERE\n")
			.append("dss.shop_id = dssd.shop_id\n")
			.append("AND dss.slip_no = dssd.slip_no\n")
			.append("AND dss.staff_id = ms.staff_id\n")
			.append("AND dss.delete_date is null\n")
			.append("ORDER BY\n")
			.append("dss.shop_id\n")
			.append(", dss.slip_no\n");

		return	new String(buf);
	}
}

/*
 * MstSupplierItem.java
 *
 * Created on 2008/09/11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;

import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mizukawa
 */
public class MstSupplierItem extends MstItem
{
	/** Creates a new instance of MstSupplierItem */
	public MstSupplierItem()
	{
	}
	
	public MstSupplierItem(MstSupplier supplier, MstItem item)
	{
		this.setSupplier(supplier);
		this.setData(item);
	}
	
	protected	MstSupplier	supplier		=	null;
	protected	Integer		costPrice		=	0;
	
	public MstSupplier getSupplier()
	{
		return supplier;
	}
	
	public void setSupplier(MstSupplier supplier)
	{
		this.supplier = supplier;
	}
	
	public Integer getCostPrice()
	{
		return costPrice;
	}
	
	public void setCostPrice(Integer costPrice)
	{
		this.costPrice = costPrice;
	}
	
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(this.getExistsSQL());

		int cnt = 0;
		if (rs.next())
		{
			if (rs.getInt("cnt") > 0)
			{
				cnt = con.executeUpdate(this.getUpdateSQL());
			}
			else
			{
				cnt = con.executeUpdate(this.getInsertSQL());
			}
		}

		return	(cnt == 1);
	}
	
	public void setData(MstSupplierItem msi)
	{
		super.setData(msi);
		this.supplier = msi.getSupplier();
		this.costPrice = msi.getCostPrice();
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);
		
		MstSupplier s = new MstSupplier();
		s.setSupplierID(rs.getInt("supplier_id"));
		setSupplier(s);
		
		setCostPrice(rs.getInt("cost_price"));
	}
	
	public void setDataAll(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);
	}
	
	/*
	 *price dataを設置する
	 */
	
	public void setPriceData(ResultSetWrapper rs) throws SQLException
	{
		setCostPrice(rs.getInt("cost_price"));
	}
	
	/**
	 * 検索に使用するSQLの基盤部分を返す。
	 */
	private String getExistsSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select count(1) as cnt from mst_supplier_item").append("\n")
			.append(" where supplier_id = ").append(this.getSupplier().getSupplierID()).append("\n")
			.append(" and item_id = ").append(this.getItemID());
		return new String(buf);
	}

	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("insert into mst_supplier_item (\n")
		.append("supplier_id,\n")
		.append("item_id,\n")
		.append("cost_price,\n")
		.append("insert_date,\n")
		.append("update_date,\n")
		.append("delete_date\n")
		.append(") values(\n")
		.append(SQLUtil.convertForSQL(this.getSupplier().getSupplierID())).append(",\n")
		.append(SQLUtil.convertForSQL(this.getItemID())).append(",\n")
		.append(SQLUtil.convertForSQL(this.getCostPrice())).append(",\n")
		.append("current_timestamp, current_timestamp, null)\n");
		return new String(buf);
	}

	public String getUpdateSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("update mst_supplier_item set\n")
			.append("cost_price = ").append(SQLUtil.convertForSQL(this.getCostPrice())).append("\n")
			.append(", update_date = current_timestamp\n")
			.append(", delete_date = null\n")
			.append("where supplier_id = ").append(this.getSupplier().getSupplierID()).append("\n")
			.append("and item_id = ").append(this.getItemID()).append("\n");
		return new String(buf);
	}

	/**
	 * 店舗と仕入先で商品を検索するSQLを返す
	 */
	public static String getFindBySupplierIdSQL(int shopId, int supplierId)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select mic.item_class_name, msi.*, mi.* from mst_supplier_item msi, mst_item mi, mst_item_class mic, mst_use_product mup\n")
		.append("where msi.item_id = mi.item_id\n")
		.append("and mi.item_class_id = mic.item_class_id\n")
		.append("and mup.shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
		.append("and mup.product_division = 2\n")
		.append("and mi.item_id = mup.product_id\n")
		.append("and msi.delete_date is null\n")
		.append("and mi.delete_date is null\n")
		.append("and mic.delete_date is null\n")
		.append("and mup.delete_date is null\n")
		.append("and supplier_id = ").append(SQLUtil.convertForSQL(supplierId)).append("\n")
		.append("order by mic.display_seq, mi.display_seq, mi.item_id")
		;
		return new String(buf);
	}

	/**
	 * 店舗と商品IDで商品を検索するSQLを返す
	 */
	public static String getFindByItemIdSQL(int itemId)
	{
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      mic.item_class_name");
                sql.append("     ,msi.*");
                sql.append("     ,mi.*");
                sql.append(" from");
                sql.append("     mst_supplier_item msi");
                sql.append("         inner join mst_item mi");
                sql.append("                 on msi.item_id = mi.item_id");
                sql.append("         inner join mst_item_class mic");
                sql.append("                 on mic.item_class_id = mi.item_class_id");
                sql.append(" where");
                //IVS_LVTu start edit 2015/10/12 Bug #43439
                //sql.append("         msi.delete_date is null");
                sql.append("     msi.item_id = " + SQLUtil.convertForSQL(itemId));
                sql.append("     limit 1 ");
                //IVS_LVTu end edit 2015/10/12 Bug #43439
                
		return sql.toString();
	}

	/**
	 * 検索に使用するSQLの基盤部分を返す。
	 */
	private String getSelectSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select * from mst_supplier_item").append("\n")
			.append(" where supplier_id = ").append(this.getSupplier().getSupplierID()).append("\n")
			.append(" and item_id = ").append(this.getItemID());
		return new String(buf);
	}
	
	
	/**
	 * 値引きを取得する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 取得成功する
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	false;
		
		ResultSetWrapper 	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			this.setPriceData(rs);
			rs.close();
			return	true;
		}
		
		rs.close();
		return	false;
	}

	/**
	 * 店舗、仕入先に紐付く商品を検索する
	 */
	public static MstSupplierItem[] find(ConnectionWrapper con, int shopId, int supplierId) throws SQLException
	{
		List<MstSupplierItem> buf = new ArrayList<MstSupplierItem>();
		
		StringBuilder query = new StringBuilder(getFindBySupplierIdSQL(shopId, supplierId));

		ResultSetWrapper rs = con.executeQuery(new String(query));
		while (rs.next())
		{
			MstSupplierItem msi = new MstSupplierItem();
			msi.setData(rs);
			buf.add(msi);
		}

		return (MstSupplierItem[]) buf.toArray(new MstSupplierItem[buf.size()]);
	}

	/**
	 * 
	 */
	public static MstSupplierItem findByItemId(ConnectionWrapper con, int itemId) throws SQLException
	{
		StringBuilder query = new StringBuilder(getFindByItemIdSQL(itemId));

		ResultSetWrapper rs = con.executeQuery(new String(query));
		try
		{
			if (rs.next())
			{
				MstSupplierItem msi = new MstSupplierItem();
				msi.setData(rs);
				return msi;
			}

			return null;
		}
		finally
		{
			rs.close();
		}
	}
}

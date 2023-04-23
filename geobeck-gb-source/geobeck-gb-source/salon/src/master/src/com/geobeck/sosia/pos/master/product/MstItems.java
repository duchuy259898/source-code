/*
 * MstItemClasses.java
 *
 * Created on 2006/05/31, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 仕入価格つく商品データのArrayList
 * @author ryu
 */
public class MstItems extends ArrayList<MstItem>
{
	private int shopId;
	private int itemClassId;
	private int itemUseDivision;
	
	private int supplier_id;
	/**
	 * コンストラクタ
	 */
	public MstItems()
	{
	}
	
	public MstItems(int item_class_id)
	{
		setItem_class_id(item_class_id);
	}
	
	public MstItems(int item_class_id, int item_use_division, int supplier_id)
	{
		setItem_class_id(item_class_id);
		setItem_use_division(item_use_division);
		setSupplier_id(supplier_id);
	}
	
	/**
	 * 商品分類マスタデータをArrayListに読み込む。
	 */
	public void loadAll(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllSQL());
		
		while(rs.next())
		{
			MstItem	mi	=	new	MstItem();
			mi.setData(rs);
			
			this.add(mi);
		}
		
		rs.close();
	}

	/**
	 * 商品分類マスタデータをArrayListに読み込む。
	 */
	public void loadAllWithShopId(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllByShopIdSQL());
		
		while(rs.next())
		{
			MstItem	mi	=	new	MstItem();
			mi.setData(rs);
			
			this.add(mi);
		}
		
		rs.close();
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private  String getSelectByClassId()
	{
		return	"select mi.*,msi.cost_price " +
			"from mst_item mi left join mst_supplier_item msi " +
			" on mi.item_id = msi.item_id where mi.item_class_id  = " + getItem_class_id() +
			" and (mi.item_use_division = 3 or mi.item_use_division = " + itemUseDivision + ")" +
			" and supplier_id = " + this.getSupplier_id();
	}

	/**
	 * Select文を取得する。
	 * すべての商品情報
	 * @return Select文
	 */
	private String getSelectAllSQL()
	{
		return	"select mic.item_class_name, mi.*\n" +
			"from mst_item mi\n" +
			"left outer join mst_item_class mic\n" +
			"on mic.item_class_id = mi.item_class_id\n" +

			"where \n" +
			( (getItem_class_id() == 0) ? "" : " mi.item_class_id  = " + getItem_class_id() ) +
			" and mi.delete_date is null" +
			" order by display_seq, item_no, item_id";
	}

	/**
	 * Select文を取得する。
	 * すべての商品情報
	 * @return Select文
	 */
	private String getSelectAllByShopIdSQL()
	{
		return	"select mic.item_class_name, mi.*\n" +
				"from mst_item mi\n" +
				"inner join mst_use_product mup\n" +
				"on mi.item_id = mup.product_id\n" +
				"and mup.product_division = 2\n" +
				"and mup.shop_id = " + SQLUtil.convertForSQL(getShopId()) + "\n" +
				"and mup.delete_date is null\n" +
				"left outer join mst_item_class mic\n" +
				"on mic.item_class_id = mi.item_class_id\n" +

				"where \n" +
				( (getItem_class_id() == 0) ? "" : " mi.item_class_id  = " + SQLUtil.convertForSQL(getItem_class_id()) ) +
				" and mi.delete_date is null" +
				" order by display_seq, item_no, item_id";
	}

	public int getShopId()
	{
		return this.shopId;
	}
	
	public void setShopId(int shopId)
	{
		this.shopId = shopId;
	}
	
	public int getItem_class_id()
	{
		return itemClassId;
	}
	
	public void setItem_class_id(int item_class_id)
	{
		this.itemClassId = item_class_id;
	}
	
	public int getItem_use_division()
	{
		return itemUseDivision;
	}
	
	public void setItem_use_division(int item_use_division)
	{
		this.itemUseDivision = item_use_division;
	}
	
	public int getSupplier_id()
	{
		return supplier_id;
	}
	
	public void setSupplier_id(int supplier_id)
	{
		this.supplier_id = supplier_id;
	}
}
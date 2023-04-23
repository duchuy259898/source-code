/*
 * MstSupplierItems.java
 *
 * Created on 2008/09/25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 仕入価格つく商品データのArrayList
 * @author ryu
 */
public class MstSupplierItems extends ArrayList<MstSupplierItem>
{
	private int shopId;
	private int itemClassId;
	private int itemUseDivision;
	
	private int supplierId;
	/**
	 * コンストラクタ
	 */
	public MstSupplierItems()
	{
	}
	
	
	public MstSupplierItems(int item_class_id, int item_use_division, int supplier_id)
	{
		setItemClassId(item_class_id);
		setItemUseDivision(item_use_division);
		setSupplierId(supplier_id);
	}
	
	/**
	 * 商品分類マスタデータをArrayListに読み込む。
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByClassId());
		
		while(rs.next())
		{
			MstSupplierItem	msi	=	new	MstSupplierItem();
			msi.setData(rs);
			
			msi.setCostPrice(rs.getInt("cost_price"));
			
			this.add(msi);
		}
		
		rs.close();
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private  String getSelectByClassId()
	{
		return	"select msi.*,mi.*\n" +
			"from mst_item mi, mst_supplier_item msi, mst_use_product mup\n" +
			"where\n" +
			" mi.item_id = msi.item_id\n" +
			" and mup.product_division = 2\n" +
			" and mup.shop_id = " + SQLUtil.convertForSQL(getShopId()) + "\n" +
			" and mi.item_id = mup.product_id\n" +
			" and mi.item_class_id = " + SQLUtil.convertForSQL(getItemClassId()) + "\n" +
			" and mi.item_use_division in (" + SQLUtil.convertForSQL(getItemUseDivision()) + ", 3)\n" +
			" and msi.supplier_id = " + SQLUtil.convertForSQL(this.getSupplierId()) + "\n" +
			" and msi.delete_date is null\n" +
			" and mi.delete_date is null\n" +
			" and mup.delete_date is null\n" +
                        " order by mi.display_seq, mi.item_id";
	}

	public int getShopId()
	{
		return this.shopId;
	}

	public void setShopId(int shopId)
	{
		this.shopId = shopId;
	}

	public int getItemClassId()
	{
		return itemClassId;
	}
	
	public void setItemClassId(int item_class_id)
	{
		this.itemClassId = item_class_id;
	}
	
	public int getItemUseDivision()
	{
		return itemUseDivision;
	}
	
	public void setItemUseDivision(int item_use_division)
	{
		this.itemUseDivision = item_use_division;
	}
	
	public int getSupplierId()
	{
		return supplierId;
	}
	
	public void setSupplierId(int supplier_id)
	{
		this.supplierId = supplier_id;
	}
}
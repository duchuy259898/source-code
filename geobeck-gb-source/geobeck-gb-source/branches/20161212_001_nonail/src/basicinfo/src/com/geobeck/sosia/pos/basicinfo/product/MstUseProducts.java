/*
 * MstUseProducts.java
 *
 * Created on 2007/01/18, 15:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.products.*;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * 使用商品・技術選択クラス
 * @author katagiri
 */
public class MstUseProducts
{
	/**
	 * 店舗
	 */
	private	MstShop				shop				=	new MstShop();
	/**
	 * 処理区分（1：技術、2：商品）
	 */
	private Integer				productDivision		=	null;
	
	/**
	 * 参照商品・技術
	 */
	private	ProductClasses		referenceProducts	=	new ProductClasses();
	/**
	 * 選択商品・技術
	 */
	private	ProductClasses		selectProducts		=	new ProductClasses();
	
	/**
	 * コンストラクタ
	 */
	public MstUseProducts()
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

	/**
	 * 処理区分（1：技術、2：商品）を取得する。
	 * @return 処理区分（1：技術、2：商品）
	 */
	public Integer getProductDivision()
	{
		return productDivision;
	}

	/**
	 * 処理区分（1：技術、2：商品）を設定する。
	 * @param productDivision 処理区分（1：技術、2：商品）
	 */
	public void setProductDivision(Integer productDivision)
	{
		this.productDivision = productDivision;
		referenceProducts.setProductDivision(productDivision);
		selectProducts.setProductDivision(productDivision);
	}
	
	/**
	 * 処理区分名を取得する。
	 * @return 処理区分名
	 */
	public String getProductDivisionName()
	{
		switch(productDivision)
		{
			case 1:
				return	"店舗使用技術";
			case 2:
				return	"店舗使用商品";
			default:
				return	"";
		}
	}

	/**
	 * 参照商品・技術を取得する。
	 * @return 参照商品・技術
	 */
	public ProductClasses getReferenceProducts()
	{
		return referenceProducts;
	}

	/**
	 * 参照商品・技術を設定する。
	 * @param referenceProducts 参照商品・技術
	 */
	public void setReferenceProducts(ProductClasses referenceProducts)
	{
		this.referenceProducts = referenceProducts;
	}

	/**
	 * 選択商品・技術を取得する。
	 * @return 選択商品・技術
	 */
	public ProductClasses getSelectProducts()
	{
		return selectProducts;
	}

	/**
	 * 選択商品・技術を設定する。
	 * @param selectProducts 選択商品・技術
	 */
	public void setSelectProducts(ProductClasses selectProducts)
	{
		this.selectProducts = selectProducts;
	}
	
	
	/**
	 * データを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(shop.getShopID() == null || productDivision == null)
		{
			return	false;
		}
		
		if(!referenceProducts.load(con))
		{
			return	false;
		}
		
		if(!selectProducts.load(con))
		{
			return	false;
		}
		
		for(ProductClass pc : referenceProducts)
		{
			if(!pc.loadProducts(con, this.getProductDivision()))
			{
				return	false;
			}
		}
		
		if(!this.loadMstUseProduct(con))
		{
			return	false;
		}
		
		return	true;
	}
	
	
	/**
	 * 取扱商品・技術を読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean loadMstUseProduct(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(this.getLoadMstUseProductSQL());
		
		while(rs.next())
		{
			ProductClass pc = referenceProducts.getProductClass(rs.getInt("product_class_id"));
			if (pc == null) continue;
				
			Product p = pc.getProduct(rs.getInt("product_id"));
			if (p == null) continue;
                        
			long price = rs.getLong("price");
			if (!rs.wasNull()) {
                            p.setPrice(price);
			}
                        
			int useProperStock = rs.getInt("use_proper_stock");
			if (rs.wasNull()) {
                            p.setUseProperStock(null);
			} else {
                            p.setUseProperStock(useProperStock);
			}

			int sellProperStock = rs.getInt("sell_proper_stock");
			if (rs.wasNull()) {
                            p.setSellProperStock(null);
			} else {
                            p.setSellProperStock(sellProperStock);
			}

			int displaySeq = rs.getInt("display_seq");
			if (rs.wasNull()) {
                            p.setDisplaySeq(null);
			} else {
                            p.setDisplaySeq(displaySeq);
			}

			String color = rs.getString("color");
			if (rs.wasNull()) {
                            p.setColor("");
			} else {
                            p.setColor(color);
			}

			int reserveFlg = rs.getInt("reserve_flg");
			if (rs.wasNull()) {
                            p.setReserveFlg(false);
			} else {
                            p.setReserveFlg(reserveFlg == 1);
			}

			this.moveProduct(true,
					referenceProducts.getProductClassIndex(rs.getInt("product_class_id")),
					referenceProducts.getProductClass(rs.getInt("product_class_id")).getProductIndex(rs.getInt("product_id")));
		}
		
		rs.close();
		rs = null;
		
		return true;
	}
	
	/**
	 * 取扱商品・技術を読み込むＳＱＬ文を取得する。
	 * @return 取扱商品・技術を読み込むＳＱＬ文
	 */
	private String getLoadMstUseProductSQL()
	{
		StringBuilder sql = new StringBuilder(1000);
		
		switch(productDivision)
		{
                    //技術
                    case 1:
                        sql.append(" select");
                        sql.append("      mup.product_id");
                        sql.append("     ,mt.technic_class_id as product_class_id");
                        sql.append("     ,mup.price");
                        sql.append("     ,null as use_proper_stock");
                        sql.append("     ,null as sell_proper_stock");
                        sql.append("     ,mup.display_seq");
                        sql.append("     ,mup.color");
                        sql.append("     ,coalesce(mup.reserve_flg, 0) as reserve_flg");
                        sql.append(" from");
                        sql.append("     mst_use_product mup");
                        sql.append("         inner join mst_technic mt");
                        sql.append("                 on mt.technic_id = mup.product_id");
                        sql.append("                and mt.delete_date is null");
                        sql.append(" where");
                        sql.append("         mup.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                        sql.append("     and mup.product_division = 1");
                        sql.append("     and mup.delete_date is null ");
                        sql.append(" order by");
                        sql.append("     mup.display_seq");

                        break;

                    //商品
                    case 2:
                        sql.append(" select");
                        sql.append("      mup.product_id");
                        sql.append("     ,mi.item_class_id as product_class_id");
                        sql.append("     ,mup.price");
                        sql.append("     ,mup.use_proper_stock");
                        sql.append("     ,mup.sell_proper_stock");
                        sql.append("     ,mup.display_seq");
                        sql.append("     ,null as color");
                        sql.append("     ,null as reserve_flg");
                        sql.append(" from");
                        sql.append("     mst_use_product mup");
                        sql.append("         inner join mst_item mi");
                        sql.append("                 on mi.item_id = mup.product_id");
                        sql.append("                and mi.delete_date is null");
                        sql.append(" where");
                        sql.append("         mup.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                        sql.append("     and mup.product_division = 2");
                        sql.append("     and mup.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mup.display_seq");
                        //IVS_LVTu start add 2015/09/04 New request #42445
                        sql.append("     , mi.item_id");
                        //IVS_LVTu end add 2015/09/04 New request #42445

                        break;

                    //コース
                    case 3:
                        sql.append(" select");
                        sql.append("      mup.product_id");
                        sql.append("     ,mc.course_class_id as product_class_id");
                        sql.append("     ,mup.price");
                        sql.append("     ,null as use_proper_stock");
                        sql.append("     ,null as sell_proper_stock");
                        sql.append("     ,mup.display_seq");
                        sql.append("     ,mup.color");
                        sql.append("     ,coalesce(mup.reserve_flg, 0) as reserve_flg");
                        sql.append(" from");
                        sql.append("     mst_use_product mup");
                        sql.append("         inner join mst_course mc");
                        sql.append("         on mc.course_id = mup.product_id");
                        sql.append("         and mc.delete_date is null");
                        sql.append(" where");
                        sql.append("         mup.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                        sql.append("     and mup.product_division = 3");
                        sql.append("     and mup.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mup.display_seq");
                        
                        break;
		}
		
		return	sql.toString();
	}
	
	/**
	 * 商品・技術を選択（解除）する。
	 * @param isSelect true：選択、false：解除
	 * @param productClassIndex 分類のインデックス
	 * @param productIndex 商品・技術のインデックス
	 */
	public void moveProduct(boolean isSelect, Integer productClassIndex, Integer productIndex)
	{
		if(productClassIndex == null || productIndex == null)
		{
			return;
		}
		
		ProductClasses	from	=	(isSelect ? referenceProducts : selectProducts);
		ProductClasses	to		=	(isSelect ? selectProducts : referenceProducts);
		
		ProductClass	pcFrom	=	from.get(productClassIndex);
		ProductClass	pcTo	=	to.get(productClassIndex);
		
		pcTo.add(pcFrom.remove(productIndex.intValue()));
	}
	
	/**
	 * 商品・技術を全て選択（解除）する。
	 * @param isSelect true：選択、false：解除
	 * @param productClassIndex 分類のインデックス
	 */
	public void moveProductAll(boolean isSelect, Integer productClassIndex)
	{
		if(productClassIndex == null)
		{
			return;
		}
		
		ProductClasses	from	=	(isSelect ? referenceProducts : selectProducts);
		ProductClasses	to		=	(isSelect ? selectProducts : referenceProducts);
		
		ProductClass	pcFrom	=	from.get(productClassIndex);
		ProductClass	pcTo	=	to.get(productClassIndex);
		
		while(0 < pcFrom.size())
		{
			pcTo.add(pcFrom.remove(0));
		}
	}
	
	public void sort(boolean isSelect, Integer productClassIndex)
	{
		if(productClassIndex == null)
		{
			return;
		}
		
		ProductClasses	pcs		=	(isSelect ? selectProducts : referenceProducts);
		ProductClass	pc	=	pcs.get(productClassIndex);
		pc.sort();
	}
	
	/**
	 * 登録処理を行う。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//元データを削除
		if(!this.deleteData(con))
		{
			return	false;
		}
		//データを登録
		if(!this.registData(con))
		{
			return false;
		}
		
		return	true;
	}
	
	/**
	 * 元のデータを削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean deleteData(ConnectionWrapper con) throws SQLException
	{
		return (0 <= con.executeUpdate(this.getDeleteMstUseProductSQL()));
	}
	
	/**
	 * 元のデータを削除するＳＱＬ文を取得する。
	 * @return 元のデータを削除するＳＱＬ文
	 */
	private String getDeleteMstUseProductSQL()
	{
		return	"delete from mst_use_product\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and  product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + "\n";
	}
	
	/**
	 * データを登録する。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean registData(ConnectionWrapper con) throws SQLException
	{
		for(ProductClass pc : selectProducts)
		{
			for(Product p : pc)
			{
				if(con.executeUpdate(this.getInsertMstUseProductSQL(p)) != 1)
				{
					return	false;
				}
			}
		}
		
		return	true;
	}
	
	/**
	 * １件分のデータを登録するＳＱＬ文を取得する。
	 * @param p 登録する商品・技術
	 * @return １件分のデータを登録するＳＱＬ文
	 */
	private String getInsertMstUseProductSQL(Product p)
	{
		return	"insert into mst_use_product\n" +
				"(shop_id, product_division, product_id,\n" +
				"insert_date, update_date, delete_date,\n" +
				"use_proper_stock, sell_proper_stock, price, display_seq, color, reserve_flg)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductDivision()) + ",\n" +
				SQLUtil.convertForSQL(p.getProductID()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null,\n" +
				SQLUtil.convertForSQL(p.getUseProperStock()) + ",\n" +
				SQLUtil.convertForSQL(p.getSellProperStock()) + ",\n" +
				SQLUtil.convertForSQL(p.getPrice()) + ",\n" +
				SQLUtil.convertForSQL(p.getDisplaySeq()) + ",\n" +
				SQLUtil.convertForSQL(p.getColor()) + ",\n" +
				SQLUtil.convertForSQL(p.isReserveFlg() ? 1 : 0) + ")\n";
	}
	
	/**
	 * 削除処理を行う。
	 * @param con ConnectionWrapper
	 * @param id プロダクトID
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean deleteProductId(ConnectionWrapper con, Integer id) throws SQLException
	{
		return (0 <= con.executeUpdate(this.getProductIdSQL(id)));
	}
	
	/**
	 * プロダクトIDのデータを削除するＳＱＬ文を取得する。
	 * @param id 削除するプロダクトID
	 * @return プロダクトIDのデータを削除するＳＱＬ文
	 */
	private String getProductIdSQL(Integer id)
	{
		return	"delete from mst_use_product\n" +
				"where product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + "\n" +
				"and  product_id = " + SQLUtil.convertForSQL(id) + "\n";
	}
}

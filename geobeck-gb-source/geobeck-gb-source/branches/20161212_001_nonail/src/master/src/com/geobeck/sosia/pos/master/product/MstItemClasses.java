/*
 * MstItemClasses.java
 *
 * Created on 2006/05/31, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * 商品分類マスタデータのArrayList
 * @author katagiri
 */
public class MstItemClasses extends ArrayList<MstItemClass>
{
	
	/**
	 * コンストラクタ
	 */
	public MstItemClasses()
	{
	}
	
	
	/**
	 * 商品分類マスタデータをArrayListに読み込む。
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstItemClasses.getSelectSQL());

		while(rs.next())
		{
			MstItemClass	mic	=	new	MstItemClass();
			mic.setData(rs);
			this.add(mic);
		}

		rs.close();
	}
	
	/**
	 * MstItemClassを検索し返す。
	 * @param itemClassId itemClassId
	 * @return 
	 */
	public MstItemClass lookup(Integer itemClassId)
	{
		for (MstItemClass msc : this)
		{
			if (itemClassId.equals(msc.getItemClassID()))
			{
				return msc;
			}
		}

		return null;
	}

	/**
	 * 商品分類マスタデータを全て取得するＳＱＬ文を取得する
	 * @return 商品分類マスタデータを全て取得するＳＱＬ文
	 */
	public static String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.*");
            sql.append("     ,b.item_integration_name");
            sql.append("     ,b.display_seq");
            //IVS_vtnhan start add 20140708 MASHU_商品分類登録
            sql.append("     ,category.shop_class_name ");
            sql.append("     ,category.shop_category_id ");
            //IVS_vtnhan end add 20140708 MASHU_商品分類登録
            sql.append(" from");
            sql.append("     mst_item_class a");
            sql.append("         left join (select * from mst_item_integration where delete_date is null) b");
            sql.append("         using (item_integration_id)");
            //IVS_vtnhan start add 20140707 MASHU_商品分類登録
            sql.append(" left join mst_shop_category category ");
            sql.append(" on a.shop_category_id =  category.shop_category_id ");
            //IVS_vtnhan end add 20140707 MASHU_商品分類登録
            sql.append(" where");
            sql.append("     a.delete_date is null");
             //IVS_vtnhan start add 20140707 MASHU_商品分類登録
            sql.append(" and category.delete_date is null ");
             //IVS_vtnhan end add 20140707 MASHU_商品分類登録
            sql.append(" order by");
            sql.append("      a.display_seq");
            sql.append("     ,a.item_class_name");

            return sql.toString();
	}
	
	public void loadAll(ConnectionWrapper con) throws SQLException
	{
		this.load(con);
		
		for(MstItemClass mic : this)
		{
			mic.loadItem(con);
		}
	}
}
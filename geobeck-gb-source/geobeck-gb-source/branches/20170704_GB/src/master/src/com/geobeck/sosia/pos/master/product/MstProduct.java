/*
 * MstProduct.java
 *
 * Created on 2006/04/26, 20:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 商品マスタデータ
 * @author katagiri
 */
public class MstProduct
{
	/**
	 * 商品区分
	 */
	private	MstProductClass		productClass	=	null;
	/**
	 * 商品ＩＤ
	 */
	private	Integer				productID		=	null;
	/**
	 * 商品コード
	 */
	private	String				productNo		=	"";
	/**
	 * 商品名
	 */
	private	String				productName		=	"";
	/**
	 * 単価
	 */
	private	Long				price			=	null;
	/**
	 * 表示順
	 */
	private	Integer				displaySeq		=	null;
	
	
	/**
	 * コンストラクタ
	 */
	public MstProduct()
	{
	}

	/**
	 * 商品区分を取得する。
	 * @return 商品区分
	 */
	public MstProductClass getProductClass()
	{
		return productClass;
	}

	/**
	 * 商品区分を取得する。
	 * @param productClass 商品区分
	 */
	public void setProductClass(MstProductClass productClass)
	{
		this.productClass = productClass;
	}

	/**
	 * 商品区分ＩＤを取得する。
	 * @return 商品区分ＩＤ
	 */
	public Integer getProductClassID()
	{
		if(productClass == null)	return	null;
		else	return productClass.getProductClassID();
	}

	/**
	 * 商品区分ＩＤを取得する。
	 * @param productClassID 商品区分ＩＤ
	 */
	public void setProductClassID(Integer productClassID)
	{
		if(productClass == null)	productClass	=	new	MstProductClass();
		this.productClass.setProductClassID(productClassID);
	}

	/**
	 * 商品ＩＤを取得する。
	 * @return 商品ＩＤ
	 */
	public Integer getProductID()
	{
		return productID;
	}

	/**
	 * 商品ＩＤを取得する。
	 * @param productID 商品ＩＤ
	 */
	public void setProductID(Integer productID)
	{
		this.productID = productID;
	}

	/**
	 * 商品コードを取得する。
	 * @return 商品コード
	 */
	public String getProductNo()
	{
		return productNo;
	}

	/**
	 * 商品コードを取得する。
	 * @param productNo 商品コード
	 */
	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	/**
	 * 商品名を取得する。
	 * @return 商品名
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * 商品名を取得する。
	 * @param productName 商品名
	 */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	/**
	 * 単価を取得する。
	 * @return 単価
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * 単価を取得する。
	 * @param price 単価
	 */
	public void setPrice(Long price)
	{
		this.price = price;
	}

	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * 表示順を取得する。
	 * @param displaySeq 表示順
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setProductClass(null);
		this.setProductID(null);
		this.setProductNo("");
		this.setProductName("");
		this.setPrice(null);
		this.setDisplaySeq(null);
	}
	
	/**
	 * 商品マスタから、設定されている商品IDのデータを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			this.setData(rs);
		}
		
		return	true;
	}
	
	/**
	 * 商品マスタデータからデータをセットする。
	 * @param mp 商品マスタデータ
	 */
	public void setData(MstProduct mp)
	{
		this.setProductClass(mp.getProductClass());
		this.setProductID(mp.getProductID());
		this.setProductNo(mp.getProductNo());
		this.setProductName(mp.getProductName());
		this.setPrice(mp.getPrice());
		this.setDisplaySeq(mp.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProductClassID(rs.getInt("product_class_id"));
		this.setProductID(rs.getInt("product_id"));
		this.setProductNo(rs.getString("product_no"));
		this.setProductName(rs.getString("product_name"));
		this.setPrice(rs.getLong("price"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * 商品マスタにデータを登録する。
	 * @param lastSeq 元の表示順
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(this.getProductID() == null || this.getProductID().equals(""))	return	false;
		
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	
	/**
	 * 商品マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getDeleteSQL();
		}
		else
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
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
	 * 商品マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getProductID() == null || this.getProductID().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 商品マスタ取得用のＳＱＬ文を取得する。
	 * @param productClassID 商品区分ＩＤ
	 * @return 商品マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL(Integer productClassID)
	{
		return	"select		*\n" +
				"from		mst_product\n" +
				"where		delete_date is null\n" +
				"		and	product_class_id = " +
				SQLUtil.convertForSQL(productClassID) + "\n" +
				"order by	display_seq, product_id\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_product\n" +
				"where	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_product\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_product\n" +
				"(product_class_id, product_id, product_no, product_name,\n" +
				"price, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getProductClassID()) + ",\n" +
				"(select coalesce(max(product_id), 0) + 1\n" +
				"from mst_product),\n" +
				SQLUtil.convertForSQL(this.getProductNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductName()) + ",\n" +
				SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_product\n" +
				"set\n" +
				"product_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + ",\n" +
				"product_no = " + SQLUtil.convertForSQL(this.getProductNo()) + ",\n" +
				"product_name = " + SQLUtil.convertForSQL(this.getProductName()) + ",\n" +
				"price = " + SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
				"and product_id != " +
				SQLUtil.convertForSQL(this.getProductID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_product\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n";
	}
}

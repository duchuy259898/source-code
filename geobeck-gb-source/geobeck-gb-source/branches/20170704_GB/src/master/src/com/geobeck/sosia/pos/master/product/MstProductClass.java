/*
 * MstProductClass.java
 *
 * Created on 2006/04/26, 18:08
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
 * 商品区分マスタデータ
 * @author katagiri
 */
public class MstProductClass
{
	/**
	 * 商品区分ＩＤの最小値
	 */
	public static int		PRODUCT_CLASS_ID_MIN		=	1;
	/**
	 * 商品区分ＩＤの最大値
	 */
	public static int		PRODUCT_CLASS_ID_MAX		=	Integer.MAX_VALUE;
	/**
	 * 商品区分名の長さの最大値
	 */
	public static int		PRODUCT_CLASS_NAME_MAX		=	20;
	/**
	 * 表示順の最小値
	 */
	public static int		DISPLAY_SEQ_MIN				=	0;
	/**
	 * 表示順の最大値
	 */
	public static int		DISPLAY_SEQ_MAX				=	9999;
	
	/**
	 * 商品区分ＩＤ
	 */
	private Integer		productClassID		=	null;
	/**
	 * 商品区分名
	 */
	private	String		productClassName	=	null;
	/**
	 * 表示順
	 */
	private Integer		displaySeq			=	null;
	
	/**
	 * コンストラクタ
	 */
	public MstProductClass()
	{
	}

	/**
	 * 文字列に変換する。（商品区分名）
	 * @return 商品区分名
	 */
	public String toString()
	{
		return productClassName;
	}

	/**
	 * 商品区分ＩＤを取得する。
	 * @return 商品区分ＩＤ
	 */
	public Integer getProductClassID()
	{
		return productClassID;
	}

	/**
	 * 商品区分ＩＤをセットする。
	 * @param productClassID 商品区分ＩＤ
	 */
	public void setProductClassID(Integer productClassID)
	{
		this.productClassID = productClassID;
	}

	/**
	 * 商品区分名を取得する。
	 * @return 商品区分名
	 */
	public String getProductClassName()
	{
		return productClassName;
	}

	/**
	 * 商品区分名をセットする。
	 * @param productClassName 商品区分名
	 */
	public void setProductClassName(String productClassName)
	{
		if(productClassName == null || productClassName.length() <= MstProductClass.PRODUCT_CLASS_NAME_MAX)
		{
			this.productClassName	=	productClassName;
		}
		else
		{
			this.productClassName	=	productClassName.substring(0, MstProductClass.PRODUCT_CLASS_NAME_MAX);
		}
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
	 * 表示順をセットする。
	 * @param displaySeq 表示順
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		if(displaySeq == null	||
				displaySeq < MstProductClass.DISPLAY_SEQ_MIN ||
				MstProductClass.DISPLAY_SEQ_MAX < displaySeq)
		{
			this.displaySeq	=	null;
		}
		else
		{
			this.displaySeq	=	displaySeq;
		}
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setProductClassID(null);
		this.setProductClassName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * 商品区分マスタデータからデータをセットする。
	 * @param mpc 商品区分マスタデータ
	 */
	public void setData(MstProductClass mpc)
	{
		this.setProductClassID(mpc.getProductClassID());
		this.setProductClassName(mpc.getProductClassName());
		this.setDisplaySeq(mpc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProductClassID(rs.getInt("product_class_id"));
		this.setProductClassName(rs.getString("product_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * 商品区分マスタにデータを登録する。
	 * @param lastSeq 元の表示順
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
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
	 * 商品区分マスタからデータを削除する。（論理削除）
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
	 * 商品区分マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getProductClassID() == null || this.getProductClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 商品区分マスタ取得用のＳＱＬ文を取得する。
	 * @return 商品区分マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_product_class\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, product_class_name\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_product_class\n"
			+	"where	product_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_product_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_product_class\n" +
				"(product_class_id, product_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(product_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getProductClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_product_class\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_product_class\n" +
				"set\n" +
				"product_class_name = " +
				SQLUtil.convertForSQL(this.getProductClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null\n" +
				"and product_class_id != " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null\n" +
				"and product_class_id != " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_product_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	product_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
}

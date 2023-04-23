/*
 * MstBed.java
 *
 * Created on 2006/05/24, 20:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ベッドマスタデータ
 * @author katagiri
 */
public class MstBed
{
	/**
	 * 店舗
	 */
	protected	MstShop		shop		=	new MstShop();
	/**
	 * ベッドＩＤ
	 */
	protected	Integer		bedID		=	null;
	/**
	 * ベッド名
	 */
	protected	String		bedName		=	"";
	/**
	 * 床数
	 */
	protected	Integer		bedNum		=	null;
	/**
	 * 表示順
	 */
	private Integer		displaySeq	=	null;
	
	/** Creates a new instance of MstBed */
	public MstBed()
	{
	}
	/**
	 * コンストラクタ
	 * @param bedID ベッドＩＤ
	 */
	public MstBed(Integer bedID)
	{
		this.setBedID(bedID);
	}
	
	/**
	 * 文字列に変換する。（ベッド名）
	 * @return ベッド名
	 */
	public String toString()
	{
		return	this.getBedName();
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
	 * ベッドＩＤを取得する。
	 * @return ベッドＩＤ
	 */
	public Integer getBedID()
	{
		return bedID;
	}

	/**
	 * ベッドＩＤをセットする。
	 * @param bedID ベッドＩＤ
	 */
	public void setBedID(Integer bedID)
	{
		this.bedID = bedID;
	}

	/**
	 * ベッド名を取得する。
	 * @return ベッド名
	 */
	public String getBedName()
	{
		return bedName;
	}

	/**
	 * ベッド名をセットする。
	 * @param bedName ベッド名
	 */
	public void setBedName(String bedName)
	{
		this.bedName = bedName;
	}

	/**
	 * 床数を取得する。
	 * @return 床数
	 */
	public Integer getBedNum()
	{
		return bedNum;
	}

	/**
	 * 床数をセットする。
	 * @param bedNum 床数
	 */
	public void setBedNum(Integer bedNum)
	{
		this.bedNum = bedNum;
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
		this.displaySeq	=	displaySeq;
	}
	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setBedID(rs.getInt("bed_id"));
		this.setBedName(rs.getString("bed_name"));
		this.setBedNum(rs.getInt("bed_num"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * ベッドマスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				//表示順を前にずらす
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					//表示順を後にずらす
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			sql	=	this.getUpdateSQL();
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				//表示順を後にずらす
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
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
	 * ベッドマスタからデータを削除する。（論理削除）
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
		
		//表示順を前にずらす
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
	 * ベッドマスタのデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if (this.getBedID() == null || this.getBedID() < 1) return false;
		
		if (con == null) return false;
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                
		if (rs.next()) {
                    this.setData(rs);
                    return true;
                }
                
                return false;
	}
        
	/**
	 * ベッドマスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getBedID() == null || this.getBedID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_bed\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	bed_id = " + SQLUtil.convertForSQL(this.getBedID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_bed\n" +
				"(shop_id, bed_id, bed_name, bed_num, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				"coalesce(max(bed_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getBedName()) + ",\n" +
				SQLUtil.convertForSQL(this.getBedNum()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_bed\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_bed\n" +
				"set\n" +
				"bed_name = " + SQLUtil.convertForSQL(this.getBedName()) + ",\n" +
				"bed_num = " + SQLUtil.convertForSQL(this.getBedNum()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and bed_id != " +
				SQLUtil.convertForSQL(this.getBedID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and bed_id != " +
				SQLUtil.convertForSQL(this.getBedID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	bed_id = " + SQLUtil.convertForSQL(this.getBedID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_bed\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_bed\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	bed_id = " + SQLUtil.convertForSQL(this.getBedID()) + "\n";
	}
}

/*
 * MstDiscount.java
 *
 * Created on 2006/06/06, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 割引マスタデータ
 * @author katagiri
 */
public class MstDiscount
{
        //割引区分
        public	static	final	int	DISCOUNT_DIVISION_ALL           =	0;
	public	static	final	int	DISCOUNT_DIVISION_TECHNIC	=	1;
	public	static	final	int	DISCOUNT_DIVISION_PRODUCT	=	2;
        //nhanvt start 20141219 New request #34216
        public	static	final	int	DISCOUNT_DIVISION_COURSE	=	5;
        //nhanvt end 20141219 New request #34216
        //割引方法
	public	static	final	int	DISCOUNT_METHOD_VALUE	=	1;
	public	static	final	int	DISCOUNT_METHOD_RATE	=	2;
	/**
	 * 割引ＩＤの最小値
	 */
	public static int		DISCOUNT_ID_MIN			=	1;
	/**
	 * 割引ＩＤの最大値
	 */
	public static int		DISCOUNT_ID_MAX			=	Integer.MAX_VALUE;
	/**
	 * 割引名の長さの最大値
	 */
	public static int		DISCOUNT_NAME_MAX		=	10;
	/**
	 * 表示順の最小値
	 */
	public static int		DISPLAY_SEQ_MIN			=	0;
	/**
	 * 表示順の最大値
	 */
	public static int		DISPLAY_SEQ_MAX			=	9999;
	
	/**
	 * 割引ＩＤ
	 */
	private Integer		discountID		=	null;
	/**
	 * 割引名
	 */
	private	String		discountName		=	null;
	/**
	 * 表示順
	 */
	private Integer		displaySeq	=	null;
	/**
	 * 割引区分
	 */
	private	Integer	discountDivision			=	null;
	/**
	 * 割引方法
	 */
	private	Integer	discountMethod			=	null;
	/**
	 * 割引率
	 */
	private	Double				discountRate		=	0d;
	/**
	 * 割引金額
	 */
	private	Long		discountValue	=	null;
	
	/** Creates a new instance of MstDiscount */
	public MstDiscount()
	{
	}
	
	/**
	 * 文字列に変換する。（割引名）
	 * @return 割引名
	 */
	public String toString()
	{
		return discountName;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof MstDiscount)
		{
			if(obj != null)
			{
				return	((MstDiscount)obj).getDiscountID() == this.getDiscountID();
			}
		}
		
		return	false;
	}

	/**
	 * 割引ＩＤを取得する。
	 * @return 割引ＩＤ
	 */
	public Integer getDiscountID()
	{
		return discountID;
	}

	/**
	 * 割引ＩＤをセットする。
	 * @param discountID 割引ＩＤ
	 */
	public void setDiscountID(Integer discountID)
	{
		this.discountID = discountID;
	}

	/**
	 * 割引名を取得する。
	 * @return 割引名
	 */
	public String getDiscountName()
	{
		return discountName;
	}

	/**
	 * 割引名をセットする。
	 * @param discountName 割引名
	 */
	public void setDiscountName(String discountName)
	{
		if(discountName == null || discountName.length() <= MstDiscount.DISCOUNT_NAME_MAX)
		{
			this.discountName	=	discountName;
		}
		else
		{
			this.discountName	=	discountName.substring(0, MstDiscount.DISCOUNT_NAME_MAX);
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
                this.displaySeq	=	displaySeq;
	}

	/**
	 * 割引区分を取得する
	 * @return 割引区分
	 */
	public Integer getDiscountDivision()
	{
		return discountDivision;
	}

	/**
	 * 割引区分を設定する
	 * @param discountDivision 割引区分
	 */
	public void setDiscountDivision(Integer discountDivision)
	{
		this.discountDivision = discountDivision;
	}

	/**
	 * 割引区分名を取得する。
	 * @return 割引区分名
	 */
	public String getDiscountDivisionName()
	{
		switch(discountDivision)
		{
			case DISCOUNT_DIVISION_ALL:
				return	"全体";
			case DISCOUNT_DIVISION_TECHNIC:
				return	"技術";
			case DISCOUNT_DIVISION_PRODUCT:
				return	"商品";
                        //nhanvt start 20141219 New request #34216
                        case DISCOUNT_DIVISION_COURSE:
				return	"コース";
                        //nhanvt end 20141219 New request #34216
			default:
				return	"";
		}
	}

	/**
	 * 割引方法を取得する
	 * @return 割引方法
	 */
	public Integer getDiscountMethod()
	{
		return discountMethod;
	}

	/**
	 * 割引方法を設定する
	 * @param discountMethod 割引方法
	 */
	public void setDiscountMethod(Integer discountMethod)
	{
		this.discountMethod = discountMethod;
	}

	/**
	 * 割引方法名を取得する。
	 * @return 割引方法名
	 */
	public String getDiscountMethodName()
	{
		switch(discountMethod)
		{
			case DISCOUNT_METHOD_VALUE:
				return	"金額";
			case DISCOUNT_METHOD_RATE:
				return	"％";
			default:
				return	"";
		}
	}

	/**
	 * 割引率を取得する
	 * @return 割引率
	 */
	public Double getDiscountRate()
	{
		return discountRate;
	}

	/**
	 * 割引率を設定する
	 * @param discountRate 割引率
	 */
	public void setDiscountRate(Double discountRate)
	{
		this.discountRate = discountRate;
	}
	
	/**
	 * 割引率(%表示)を取得する
	 * @return 割引率(%表示)
	 */
	public Double getDiscountRatePercentage()
	{
		if(this.getDiscountRate() == null)
		{
			return	0d;
		}
		else
		{
			return	this.getDiscountRate() * 100d;
		}
	}

	/**
	 * 割引金額
	 * @return 割引金額
	 */
	public Long getDiscountValue()
	{
		return discountValue;
	}

	/**
	 * 割引金額
	 * @param discountValue 割引金額
	 */
	public void setDiscountValue(Long discountValue)
	{
		this.discountValue = discountValue;
	}

	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setDiscountID(null);
		this.setDiscountName("");
		this.setDisplaySeq(null);
		this.setDiscountDivision(null);
		this.setDiscountMethod(null);
		this.setDiscountRate(null);
		this.setDiscountValue(null);
	}
	
	/**
	 * ResultSetWrapperからデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setDiscountID(rs.getInt("discount_id"));
		this.setDiscountName(rs.getString("discount_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setDiscountDivision(rs.getInt("discount_division"));
		this.setDiscountMethod(rs.getInt("discount_method"));
		this.setDiscountRate(rs.getDouble("discount_rate"));
		this.setDiscountValue(rs.getLong("discount_value"));
	}
	
	/**
	 * 割引種別マスタにデータを登録する。
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
	 * 割引種別マスタからデータを削除する。（論理削除）
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
	 * 割引マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getDiscountID() == null || this.getDiscountID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 割引マスタ取得用のＳＱＬ文を取得する。
	 * @return 割引マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_discount\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, discount_name\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_discount\n"
			+	"where	discount_id = " + SQLUtil.convertForSQL(this.getDiscountID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_discount\n"
			+	"(discount_id, discount_name, display_seq,\n"
			+	"discount_division, discount_method, discount_rate, discount_value,\n"
			+	"insert_date, update_date, delete_date)\n"
			+	"select\n"
			+	"coalesce(max(discount_id), 0) + 1,\n"
			+	SQLUtil.convertForSQL(this.getDiscountName()) + ",\n"
			+	"case\n"
			+	"when " + SQLUtil.convertForSQL(this.getDisplaySeq())
			+	" between 0 and coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null), 0) then "
			+	SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
			+	"else coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null), 0) + 1 end,\n"
			+	SQLUtil.convertForSQL(this.getDiscountDivision()) + ",\n"
			+	SQLUtil.convertForSQL(this.getDiscountMethod()) + ",\n"
			+	SQLUtil.convertForSQL(this.getDiscountRate(), "0") + ",\n"
			+	SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n"
			+	"current_timestamp, current_timestamp, null\n"
			+	"from mst_discount\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_discount\n"
			+	"set\n"
			+	"discount_name = " + SQLUtil.convertForSQL(this.getDiscountName()) + ",\n"
			+	"display_seq = case\n"
			+	"when " + SQLUtil.convertForSQL(this.getDisplaySeq())
			+	" between 0 and coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null\n"
			+	"and discount_id != "
			+	SQLUtil.convertForSQL(this.getDiscountID()) + "), 0) then "
			+	SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
			+	"else coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null\n"
			+	"and discount_id != "
			+	SQLUtil.convertForSQL(this.getDiscountID()) + "), 0) + 1 end,\n"
			+	"discount_division = " + SQLUtil.convertForSQL(this.getDiscountDivision()) + ",\n"
			+	"discount_method = " + SQLUtil.convertForSQL(this.getDiscountMethod()) + ",\n"
			+	"discount_rate = " + SQLUtil.convertForSQL(this.getDiscountRate(), "0") + ",\n"
			+	"discount_value = " + SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n"
			+	"update_date = current_timestamp\n"
			+	"where	discount_id = " + SQLUtil.convertForSQL(this.getDiscountID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_discount\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_discount\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	discount_id = " + SQLUtil.convertForSQL(this.getDiscountID()) + "\n";
	}
}

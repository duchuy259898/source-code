/*
 * MstCashClass.java
 *
 * Created on 2008/09/22, 20:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 小口項目データ
 * @author saito
 */
public class MstCashClass extends ArrayList<MstCashMenu>
{
	/**
	 * 小口項目ＩＤ
	 */
	protected Integer		cashClassId		=	null;
	/**
	 * 小口項目名
	 */
	protected String		cashClassName          =	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * コンストラクタ
	 */
	public MstCashClass()
	{
		super();
	}
        
	/**
	 * コンストラクタ
	 * @param cashClassId 小口項目ＩＤ
	 */
	public MstCashClass(Integer cashClassId)
	{
		this.setCashClassId(cashClassId);
	}
	
	/**
	 * 文字列に変換する。（小口項目名）
	 * @return 小口項目名
	 */
	public String toString()
	{
		return cashClassName;
	}

	/**
	 * 小口項目ＩＤを取得する。
	 * @return 小口項目ＩＤ
	 */
	public Integer getCashClassId()
	{
		return cashClassId;
	}

	/**
	* 小口項目ＩＤをセットする。
	* @param cashClassId 小口項目ＩＤ
	*/
	public void setCashClassId(Integer cashClassId)
	{
		this.cashClassId = cashClassId;
	}

	/**
	 * 小口項目名を取得する。
	 * @return 小口項目名
	 */
	public String getCashClassName()
	{
		return cashClassName;
	}

	/**
	* 小口項目名をセットする。
	* @param cashClassName 小口項目名
	*/
	public void setCashClassName(String cashClassName)
	{
                this.cashClassName	=	cashClassName;
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
	 * オブジェクトを比較する。
	 * @param o オブジェクト
	 */
	public boolean equals(Object o)
	{
		if(o instanceof MstCashClass)
		{
			MstCashClass	mkc	=	(MstCashClass)o;
			
			if(mkc.getCashClassId() == cashClassId &&
					mkc.getCashClassName().equals(cashClassName) &&
					mkc.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * 小口項目データからデータをセットする。
	 * @param mkc 小口項目データ
	 */
	public void setData(MstCashClass mkc)
	{
		this.setCashClassId(mkc.getCashClassId());
		this.setCashClassName(mkc.getCashClassName());
		this.setDisplaySeq(mkc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCashClassId(rs.getInt("cash_class_id"));
		this.setCashClassName(rs.getString("cash_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * 小口項目にデータを登録する。
	 * @return true - 成功
	 * @param lastSeq 表示順の最大値
	 * @param con ConnectionWrapper
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
	 * 小口項目からデータを削除する。（論理削除）
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * 小口項目にデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCashClassId() == null || this.getCashClassId() < 1)	return	false;
		
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
		return	"select *\n"
			+	"from mst_cash_class\n"
                        +	"where delete_date is null\n"
			+	"and cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_cash_class\n" +
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
		return	"insert into mst_cash_class\n" +
				"(cash_class_id, cash_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(cash_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getCashClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_cash_class\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_cash_class\n" +
				"set\n" +
				"cash_class_name = " + SQLUtil.convertForSQL(this.getCashClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null\n" +
				"and cash_class_id != " +
				SQLUtil.convertForSQL(this.getCashClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_class\n" +
				"where delete_date is null\n" +
				"and cash_class_id != " +
				SQLUtil.convertForSQL(this.getCashClassId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_cash_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n";
	}
	
	/**
	 * 小口詳細マスタデータをArrayListに読み込む。
	 * @param con ConnectionWrapper
	 */
	public void loadCashMenu(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectCashMenuSQL());

		while(rs.next())
		{
			MstCashMenu	mkd	=	new	MstCashMenu();
			mkd.setData(rs);
			this.add(mkd);
		}

		rs.close();
	}
	
	/**
	 * 小口詳細マスタデータを全て取得するＳＱＬ文を取得する
	 * @return 小口詳細マスタデータを全て取得するＳＱＬ文
	 */
	public String getSelectCashMenuSQL()
	{
		return	"select *\n" +
			"from mst_cash_menu\n" +
			"where delete_date is null\n" +
			"and cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()) + "\n" +
			"order by display_seq, cash_menu_id\n";
	}
}

/*
 * MstCashMenu.java
 *
 * Created on 2008/09/22, 20:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 小口詳細データ
 * @author saito
 */
public class MstCashMenu
{
	/**
	 * 小口項目
	 */
	protected MstCashClass          cashClass		=	new MstCashClass();
	/**
	 * 小口詳細ＩＤ
	 */
	protected Integer		cashMenuId		=	null;
	/**
	 * 小口詳細名
	 */
	protected String		cashMenuName         =	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * コンストラクタ
	 */
	public MstCashMenu()
	{
	}
        
	/**
	 * コンストラクタ
	 * @param cashMenuId 小口詳細ＩＤ
	 */
	public MstCashMenu(Integer cashMenuId)
	{
		this.setCashMenuId(cashMenuId);
	}

	/**
	 * 小口項目を取得する。
	 * @return 小口項目
	 */
	public MstCashClass getCashClass()
	{
		return cashClass;
	}

	/**
	 * 小口項目をセットする。
	 * @param cashClass 小口項目
	 */
	public void setCashClass(MstCashClass cashClass)
	{
		this.cashClass = cashClass;
	}
	
	/**
	 * 文字列に変換する。（小口詳細名）
	 * @return 小口詳細名
	 */
	public String toString()
	{
		return cashMenuName;
	}

	/**
	 * 小口詳細ＩＤを取得する。
	 * @return 小口詳細ＩＤ
	 */
	public Integer getCashMenuId()
	{
		return cashMenuId;
	}

	/**
	* 小口詳細ＩＤをセットする。
	* @param cashMenuId 小口詳細ＩＤ
	*/
	public void setCashMenuId(Integer cashMenuId)
	{
		this.cashMenuId = cashMenuId;
	}

	/**
	 * 小口詳細名を取得する。
	 * @return 小口詳細名
	 */
	public String getCashMenuName()
	{
		return cashMenuName;
	}

	/**
	* 小口詳細名をセットする。
	* @param cashMenuName 小口詳細名
	*/
	public void setCashMenuName(String cashMenuName)
	{
                this.cashMenuName = cashMenuName;
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
		if(o instanceof MstCashMenu)
		{
			MstCashMenu	mkd	=	(MstCashMenu)o;
			
			if(mkd.getCashClass().getCashClassId() == cashClass.getCashClassId() &&
					mkd.getCashMenuId() == cashMenuId &&
					mkd.getCashMenuName().equals(cashMenuName) &&
					mkd.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * 小口詳細データからデータをセットする。
	 * @param mkd 小口詳細データ
	 */
	public void setData(MstCashMenu mkd)
	{
                this.setCashClass(new MstCashClass(mkd.getCashClass().getCashClassId()));
		this.setCashMenuId(mkd.getCashMenuId());
		this.setCashMenuName(mkd.getCashMenuName());
		this.setDisplaySeq(mkd.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCashClass(new MstCashClass(rs.getInt("cash_class_id")));
		this.setCashMenuId(rs.getInt("cash_menu_id"));
		this.setCashMenuName(rs.getString("cash_menu_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * 小口詳細にデータを登録する。
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
			
			this.setMaxCashMenuID(con);
		}
		
		return	true;
	}
	
	/**
	 * 小口詳細ＩＤの最大値を取得する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	private void setMaxCashMenuID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxCashMenuIDSQL());
		
		if(rs.next())
		{
			this.setCashMenuId(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * 小口詳細からデータを削除する。（論理削除）
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
	 * 小口詳細にデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCashMenuId() == null)	return	false;
		
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
		return	"select mkc.cash_class_name, mkd.*\n" +
				"from mst_cash_menu mkd\n" +
				"left outer join mst_cash_class mkc\n" +
				"on mkc.cash_class_id = mkd.cash_class_id\n" +
				"and mkc.delete_date is null\n" +
                                "where mkd.delete_date is null\n" +
				"and mkd.cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_cash_menu\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_cash_menu\n" +
				"(cash_class_id, cash_menu_id, cash_menu_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + ",\n" +
				"(select coalesce(max(cash_menu_id), 0) + 1\n" +
				"from mst_cash_menu),\n" +
				SQLUtil.convertForSQL(this.getCashMenuName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_cash_menu\n" +
				"set\n" +
				"cash_class_id = " + SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + ",\n" +
				"cash_menu_name = " + SQLUtil.convertForSQL(this.getCashMenuName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "\n" +
				"and cash_menu_id != " +
				SQLUtil.convertForSQL(this.getCashMenuId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_cash_menu\n" +
				"where delete_date is null\n" +
				"and cash_class_id = " +
				SQLUtil.convertForSQL(this.getCashClass().getCashClassId()) + "\n" +
				"and cash_menu_id != " +
				SQLUtil.convertForSQL(this.getCashMenuId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String getDeleteSQL()
	{
		return	"update mst_cash_menu\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()) + "\n";
	}
	
	/**
	 * 小口詳細ＩＤの最大値を取得するSQL文を取得する。
	 * @return 小口詳細ＩＤの最大値を取得するSQL文
	 */
	private static String getMaxCashMenuIDSQL()
	{
		return	"select max(cash_menu_id) as max_id\n" +
				"from mst_cash_menu\n";
	}
	
}
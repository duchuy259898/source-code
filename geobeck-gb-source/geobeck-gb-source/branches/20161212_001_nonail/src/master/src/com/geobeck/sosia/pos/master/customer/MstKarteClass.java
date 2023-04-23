/*
 * MstKarteClass.java
 *
 * Created on 2008/09/22, 20:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * カルテ分類マスタデータ
 * @author saito
 */
public class MstKarteClass extends ArrayList<MstKarteDetail>
{
	/**
	 * カルテ分類ＩＤ
	 */
	protected Integer		karteClassId		=	null;
	/**
	 * カルテ分類名
	 */
	protected String		karteClassName          =	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * コンストラクタ
	 */
	public MstKarteClass()
	{
		super();
	}
        
	/**
	 * コンストラクタ
	 * @param karteClassId カルテ分類ＩＤ
	 */
	public MstKarteClass(Integer karteClassId)
	{
		this.setKarteClassId(karteClassId);
	}
	
	/**
	 * 文字列に変換する。（カルテ分類名）
	 * @return カルテ分類名
	 */
	public String toString()
	{
		return karteClassName;
	}

	/**
	 * カルテ分類ＩＤを取得する。
	 * @return カルテ分類ＩＤ
	 */
	public Integer getKarteClassId()
	{
		return karteClassId;
	}

	/**
	* カルテ分類ＩＤをセットする。
	* @param karteClassId カルテ分類ＩＤ
	*/
	public void setKarteClassId(Integer karteClassId)
	{
		this.karteClassId = karteClassId;
	}

	/**
	 * カルテ分類名を取得する。
	 * @return カルテ分類名
	 */
	public String getKarteClassName()
	{
		return karteClassName;
	}

	/**
	* カルテ分類名をセットする。
	* @param karteClassName カルテ分類名
	*/
	public void setKarteClassName(String karteClassName)
	{
                this.karteClassName	=	karteClassName;
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
		if(o instanceof MstKarteClass)
		{
			MstKarteClass	mkc	=	(MstKarteClass)o;
			
			if(mkc.getKarteClassId() == karteClassId &&
					mkc.getKarteClassName().equals(karteClassName) &&
					mkc.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * カルテ分類マスタデータからデータをセットする。
	 * @param mkc カルテ分類マスタデータ
	 */
	public void setData(MstKarteClass mkc)
	{
		this.setKarteClassId(mkc.getKarteClassId());
		this.setKarteClassName(mkc.getKarteClassName());
		this.setDisplaySeq(mkc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteClassId(rs.getInt("karte_class_id"));
		this.setKarteClassName(rs.getString("karte_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * カルテ分類マスタにデータを登録する。
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
	 * カルテ分類マスタからデータを削除する。（論理削除）
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
	 * カルテ分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getKarteClassId() == null || this.getKarteClassId() < 1)	return	false;
		
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
			+	"from mst_karte_class\n"
                        +	"where delete_date is null\n"
			+	"and karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_karte_class\n" +
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
		return	"insert into mst_karte_class\n" +
				"(karte_class_id, karte_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(karte_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getKarteClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_karte_class\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_karte_class\n" +
				"set\n" +
				"karte_class_name = " + SQLUtil.convertForSQL(this.getKarteClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null\n" +
				"and karte_class_id != " +
				SQLUtil.convertForSQL(this.getKarteClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null\n" +
				"and karte_class_id != " +
				SQLUtil.convertForSQL(this.getKarteClassId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_karte_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n";
	}
	
	/**
	 * カルテ詳細マスタデータをArrayListに読み込む。
	 * @param con ConnectionWrapper
	 */
	public void loadKarteDetail(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectKarteDetailSQL());

		while(rs.next())
		{
			MstKarteDetail	mkd	=	new	MstKarteDetail();
			mkd.setData(rs);
			this.add(mkd);
		}

		rs.close();
	}
	
	/**
	 * カルテ詳細マスタデータを全て取得するＳＱＬ文を取得する
	 * @return カルテ詳細マスタデータを全て取得するＳＱＬ文
	 */
	public String getSelectKarteDetailSQL()
	{
		return	"select *\n" +
			"from mst_karte_detail\n" +
			"where delete_date is null\n" +
			"and karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n" +
			"order by display_seq, karte_detail_id\n";
	}
}

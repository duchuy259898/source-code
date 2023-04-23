/*
 * MstKarteDetail.java
 *
 * Created on 2008/09/22, 20:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.ArrayList;

/**
 * カルテ詳細マスタデータ
 * @author saito
 */
public class MstKarteDetail extends ArrayList<MstKarteReference>
{
	/**
	 * カルテ分類
	 */
	protected	MstKarteClass	karteClass		=	new MstKarteClass();
	/**
	 * カルテ詳細ＩＤ
	 */
	protected Integer		karteDetailId		=	null;
	/**
	 * カルテ詳細名
	 */
	protected String		karteDetailName         =	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * コンストラクタ
	 */
	public MstKarteDetail()
	{
	}
        
	/**
	 * コンストラクタ
	 * @param karteDetailId カルテ詳細ＩＤ
	 */
	public MstKarteDetail(Integer karteDetailId)
	{
		this.setKarteDetailId(karteDetailId);
	}

	/**
	 * カルテ分類を取得する。
	 * @return カルテ分類
	 */
	public MstKarteClass getKarteClass()
	{
		return karteClass;
	}

	/**
	 * カルテ分類をセットする。
	 * @param karteClass カルテ分類
	 */
	public void setKarteClass(MstKarteClass karteClass)
	{
		this.karteClass = karteClass;
	}
	
	/**
	 * 文字列に変換する。（カルテ詳細名）
	 * @return カルテ詳細名
	 */
	public String toString()
	{
		return karteDetailName;
	}

	/**
	 * カルテ詳細ＩＤを取得する。
	 * @return カルテ詳細ＩＤ
	 */
	public Integer getKarteDetailId()
	{
		return karteDetailId;
	}

	/**
	* カルテ詳細ＩＤをセットする。
	* @param karteDetailId カルテ詳細ＩＤ
	*/
	public void setKarteDetailId(Integer karteDetailId)
	{
		this.karteDetailId = karteDetailId;
	}

	/**
	 * カルテ詳細名を取得する。
	 * @return カルテ詳細名
	 */
	public String getKarteDetailName()
	{
		return karteDetailName;
	}

	/**
	* カルテ詳細名をセットする。
	* @param karteDetailName カルテ詳細名
	*/
	public void setKarteDetailName(String karteDetailName)
	{
                this.karteDetailName	=	karteDetailName;
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
		if(o instanceof MstKarteDetail)
		{
			MstKarteDetail	mkd	=	(MstKarteDetail)o;
			
			if(mkd.getKarteClass().getKarteClassId() == karteClass.getKarteClassId() &&
					mkd.getKarteDetailId() == karteDetailId &&
					mkd.getKarteDetailName().equals(karteDetailName) &&
					mkd.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * カルテ詳細マスタデータからデータをセットする。
	 * @param mkd カルテ詳細マスタデータ
	 */
	public void setData(MstKarteDetail mkd)
	{
                this.setKarteClass(new MstKarteClass(mkd.getKarteClass().getKarteClassId()));
		this.setKarteDetailId(mkd.getKarteDetailId());
		this.setKarteDetailName(mkd.getKarteDetailName());
		this.setDisplaySeq(mkd.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteClass(new MstKarteClass(rs.getInt("karte_class_id")));
		this.setKarteDetailId(rs.getInt("karte_detail_id"));
		this.setKarteDetailName(rs.getString("karte_detail_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * カルテ詳細マスタにデータを登録する。
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
			
			this.setMaxKarteDetailID(con);
		}
		
		return	true;
	}
	
	/**
	 * カルテ詳細ＩＤの最大値を取得する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	private void setMaxKarteDetailID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxKarteDetailIDSQL());
		
		if(rs.next())
		{
			this.setKarteDetailId(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * カルテ詳細マスタからデータを削除する。（論理削除）
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
	 * カルテ詳細マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getKarteDetailId() == null)	return	false;
		
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
		return	"select mkc.karte_class_name, mkd.*\n" +
				"from mst_karte_detail mkd\n" +
				"left outer join mst_karte_class mkc\n" +
				"on mkc.karte_class_id = mkd.karte_class_id\n" +
				"and mkc.delete_date is null\n" +
                                "where mkd.delete_date is null\n" +
				"and mkd.karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_karte_detail\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_karte_detail\n" +
				"(karte_class_id, karte_detail_id, karte_detail_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + ",\n" +
				"(select coalesce(max(karte_detail_id), 0) + 1\n" +
				"from mst_karte_detail),\n" +
				SQLUtil.convertForSQL(this.getKarteDetailName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_karte_detail\n" +
				"set\n" +
				"karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + ",\n" +
				"karte_detail_name = " + SQLUtil.convertForSQL(this.getKarteDetailName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "\n" +
				"and karte_detail_id != " +
				SQLUtil.convertForSQL(this.getKarteDetailId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "\n" +
				"and karte_detail_id != " +
				SQLUtil.convertForSQL(this.getKarteDetailId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String getDeleteSQL()
	{
		return	"update mst_karte_detail\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n";
	}
	
	/**
	 * カルテ詳細ＩＤの最大値を取得するSQL文を取得する。
	 * @return カルテ詳細ＩＤの最大値を取得するSQL文
	 */
	private static String getMaxKarteDetailIDSQL()
	{
		return	"select max(karte_detail_id) as max_id\n" +
				"from mst_karte_detail\n";
	}
	
	/**
	 * カルテ参照マスタデータをArrayListに読み込む。
	 * @param con ConnectionWrapper
	 */
	public void loadKarteReference(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectKarteReferenceSQL());

		while(rs.next())
		{
			MstKarteReference	mkr	=	new	MstKarteReference();
			mkr.setData(rs);
			this.add(mkr);
		}

		rs.close();
	}
	
	/**
	 * カルテ参照マスタデータを全て取得するＳＱＬ文を取得する
	 * @return カルテ参照マスタデータを全て取得するＳＱＬ文
	 */
	public String getSelectKarteReferenceSQL()
	{
		return	"select *\n" +
			"from mst_karte_reference\n" +
			"where delete_date is null\n" +
			"and karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n" +
			"order by display_seq, karte_reference_id\n";
	}
}
/*
 * MstKarteReference.java
 *
 * Created on 2008/09/23, 10:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * カルテ参照マスタデータ
 * @author saito
 */
public class MstKarteReference
{
	/**
	 * カルテ詳細
	 */
	protected	MstKarteDetail	karteDetail		=	new MstKarteDetail();
	/**
	 * カルテ参照ＩＤ
	 */
	protected Integer		karteReferenceId	=	null;
	/**
	 * カルテ参照名
	 */
	protected String		karteReferenceName      =	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * コンストラクタ
	 */
	public MstKarteReference()
	{
	}

	/**
	 * カルテ詳細を取得する。
	 * @return カルテ詳細
	 */
	public MstKarteDetail getKarteDetail()
	{
		return karteDetail;
	}

	/**
	 * カルテ詳細をセットする。
	 * @param karteDetail カルテ詳細
	 */
	public void setKarteDetail(MstKarteDetail karteDetail)
	{
		this.karteDetail = karteDetail;
	}
	
	/**
	 * 文字列に変換する。（カルテ参照名）
	 * @return カルテ参照名
	 */
	public String toString()
	{
		return karteReferenceName;
	}

	/**
	 * カルテ参照ＩＤを取得する。
	 * @return カルテ参照ＩＤ
	 */
	public Integer getKarteReferenceId()
	{
		return karteReferenceId;
	}

	/**
	* カルテ参照ＩＤをセットする。
	* @param karteReferenceId カルテ参照ＩＤ
	*/
	public void setKarteReferenceId(Integer karteReferenceId)
	{
		this.karteReferenceId = karteReferenceId;
	}

	/**
	 * カルテ参照名を取得する。
	 * @return カルテ参照名
	 */
	public String getKarteReferenceName()
	{
		return karteReferenceName;
	}

	/**
	* カルテ参照名をセットする。
	* @param karteReferenceName カルテ参照名
	*/
	public void setKarteReferenceName(String karteReferenceName)
	{
                this.karteReferenceName	=	karteReferenceName;
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
	 * カルテ参照マスタデータからデータをセットする。
	 * @param mkr カルテ参照マスタデータ
	 */
	public void setData(MstKarteReference mkr)
	{
                this.setKarteDetail(new MstKarteDetail(mkr.getKarteDetail().getKarteDetailId()));
		this.setKarteReferenceId(mkr.getKarteReferenceId());
		this.setKarteReferenceName(mkr.getKarteReferenceName());
		this.setDisplaySeq(mkr.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteDetail(new MstKarteDetail(rs.getInt("karte_detail_id")));
		this.setKarteReferenceId(rs.getInt("karte_reference_id"));
		this.setKarteReferenceName(rs.getString("karte_reference_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * カルテ参照マスタにデータを登録する。
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
			
			this.setMaxKarteReferenceID(con);
		}
		
		return	true;
	}
	
	/**
	 * カルテ参照ＩＤの最大値を取得する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	private void setMaxKarteReferenceID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxKarteReferenceIDSQL());
		
		if(rs.next())
		{
			this.setKarteReferenceId(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * カルテ参照マスタからデータを削除する。（論理削除）
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
	 * カルテ参照マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getKarteReferenceId() == null)	return	false;
		
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
		return	"select mkd.karte_detail_name, mkr.*\n" +
				"from mst_karte_reference mkr\n" +
				"left outer join mst_karte_detail mkd\n" +
				"on mkd.karte_detail_id = mkr.karte_detail_id\n" +
				"and mkd.delete_date is null\n" +
                                "where mkr.delete_date is null\n" +
				"and mkr.karte_reference_id = " + SQLUtil.convertForSQL(this.getKarteReferenceId()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_karte_reference\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_karte_reference\n" +
				"(karte_detail_id, karte_reference_id, karte_reference_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + ",\n" +
				"(select coalesce(max(karte_reference_id), 0) + 1\n" +
				"from mst_karte_reference),\n" +
				SQLUtil.convertForSQL(this.getKarteReferenceName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_karte_reference\n" +
				"set\n" +
				"karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + ",\n" +
				"karte_reference_name = " + SQLUtil.convertForSQL(this.getKarteReferenceName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "\n" +
				"and karte_reference_id != " +
				SQLUtil.convertForSQL(this.getKarteReferenceId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "\n" +
				"and karte_reference_id != " +
				SQLUtil.convertForSQL(this.getKarteReferenceId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where karte_reference_id = " + SQLUtil.convertForSQL(this.getKarteReferenceId()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String getDeleteSQL()
	{
		return	"update mst_karte_reference\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	karte_reference_id = " + SQLUtil.convertForSQL(this.getKarteReferenceId()) + "\n";
	}
	
	/**
	 * カルテ参照ＩＤの最大値を取得するSQL文を取得する。
	 * @return カルテ参照ＩＤの最大値を取得するSQL文
	 */
	private static String getMaxKarteReferenceIDSQL()
	{
		return	"select max(karte_reference_id) as max_id\n" +
				"from mst_karte_reference\n";
	}
}
/*
 * TechnicKarte.java
 *
 * Created on 2008/09/08, 21:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;

/**
 * 技術カルテデータ
 * @author saito
 */
public class TechnicKarte  extends ArrayList<TechnicKarteReference>
{
	/**
	 * 分類
	 */
	protected	TechnicKarteClass	technicKarteClass	=	new TechnicKarteClass();
	/**
	 * カルテ分類ＩＤ
	 */
	protected Integer		karteClassId		=	null;
	/**
	 * カルテ詳細ＩＤ
	 */
	protected Integer		karteDetailId		=	null;
	/**
	 * カルテ詳細名
	 */
	protected	String		karteDetailName         =	null;
	/**
	 * 内容
	 */
	private	String		contents                =	null;
	/**
	 * 表示順
	 */
	protected Integer		displaySeq              =	null;

        /**
	 * コンストラクタ
	 */
	public TechnicKarte()
	{
	}

	/**
	 * 分類を取得する。
	 * @return 分類
	 */
	public TechnicKarteClass getTechnicKarteClass()
	{
		return technicKarteClass;
	}

	/**
	 * 分類を設定する。
	 * @param technicKarteClass 分類
	 */
	public void setTechnicKarteClass(TechnicKarteClass technicKarteClass)
	{
		this.technicKarteClass = technicKarteClass;
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
	 * 内容を取得する。
	 * @return 内容
	 */
	public String getContents()
	{
		return contents;
	}

	/**
	* 内容をセットする。
	* @param contents 内容
	*/
	public void setContents(String contents)
	{
            this.contents	=	contents;
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
                this.displaySeq	=	null;
	}
	
	/**
	 * 技術カルテ参照を取得する。
	 * @param karteReferenceId カルテ参照ＩＤ
	 * @param karteDetailId カルテ詳細ＩＤ
	 * @return 技術カルテ参照
	 */
	public TechnicKarteReference getTechnicKarteReference(Integer karteReferenceId, Integer karteDetailId)
	{
		for(TechnicKarteReference tkr : this)
		{
			if(tkr.getKarteReferenceId() == karteReferenceId &&
                        tkr.getKarteDetailId() == karteDetailId)
			{
				return	tkr;
			}
		}
		
		return	null;
	}
	
	/**
	 * 技術カルテ参照のインデックスを取得する。
	 * @param karteReferenceId カルテ参照ＩＤ
	 * @param karteDetailId カルテ詳細ＩＤ
	 * @return 技術カルテ参照のインデックス
	 */
	public Integer getTechnicKarteReferenceIndex(Integer karteReferenceId, Integer karteDetailId)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getKarteReferenceId().intValue() == karteReferenceId.intValue() &&
                            this.get(i).getKarteDetailId().intValue() == karteDetailId.intValue())
			{       
				return	i;
			}
		}
		
		return	null;
	}
	
	/**
	 * データを設定する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteClassId(rs.getInt("karte_class_id"));
		this.setKarteDetailId(rs.getInt("karte_detail_id"));
		this.setKarteDetailName(rs.getString("karte_detail_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * 技術カルテ参照を読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadTechnicKarteReferences(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadTechnicKarteReferencesSQL());

		while(rs.next())
		{
			TechnicKarteReference	tkr	=	new TechnicKarteReference();
			tkr.setData(rs);
			tkr.setTechnicKarte(this);
			this.add(tkr);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * 技術カルテ参照を読み込むＳＱＬ文を取得する。
	 * @return 技術カルテ参照を読み込むＳＱＬ文
	 */
	public String getLoadTechnicKarteReferencesSQL()
	{
		return	"select		*\n"
			+	"from		mst_karte_reference\n"
			+	"where		delete_date is null\n"
        		+	"and karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n"
			+	"order by	display_seq, karte_reference_name\n";
	}
}

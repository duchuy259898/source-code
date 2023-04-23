/*
 * TechnicKarteClass.java
 *
 * Created on 2008/09/08, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;


import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 技術カルテ分類データ
 * @author saito
 */
public class TechnicKarteClass extends ArrayList<TechnicKarte>
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
	public TechnicKarteClass()
	{
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
	 * 技術カルテを取得する。
	 * @param karteClassId カルテ分類ＩＤ
	 * @param karteDetailId カルテ詳細ＩＤ
	 * @return 技術カルテ
	 */
	public TechnicKarte getTechnicKarte(Integer karteClassId, Integer karteDetailId)
	{
		for(TechnicKarte tk : this)
		{
			if(tk.getKarteClassId().intValue() == karteClassId.intValue() &&
                        tk.getKarteDetailId().intValue() == karteDetailId.intValue())
			{
				return	tk;
			}
		}
		
		return	null;
	}
	
	/**
	 * 技術カルテのインデックスを取得する。
	 * @param karteClassId カルテ分類ＩＤ
	 * @param karteDetailId カルテ詳細ＩＤ
	 * @return 技術カルテのインデックス
	 */
	public Integer getTechnicKarteIndex(Integer karteClassId, Integer karteDetailId)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getKarteClassId().intValue() == karteClassId.intValue() &&
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
		this.setKarteClassName(rs.getString("karte_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * 技術カルテを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadTechnicKartes(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadTechnicKartesSQL());

		while(rs.next())
		{
			TechnicKarte	tk	=	new TechnicKarte();
			tk.setData(rs);
			tk.setTechnicKarteClass(this);
			this.add(tk);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * 技術カルテを読み込むＳＱＬ文を取得する。
	 * @return 技術カルテを読み込むＳＱＬ文
	 */
	public String getLoadTechnicKartesSQL()
	{
		return	"select		*\n"
			+	"from		mst_karte_detail\n"
			+	"where		delete_date is null\n"
        		+	"and karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n"
			+	"order by	display_seq, karte_detail_name\n";
	}
}

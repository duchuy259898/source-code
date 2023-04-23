/*
 * TechnicKarteClasses.java
 *
 * Created on 2008/09/08, 22:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * 技術カルテ分類データのArrayList
 * @author saito
 */
public class TechnicKarteClasses extends Vector<TechnicKarteClass>
{
	/**
	 * コンストラクタ
	 */
	public TechnicKarteClasses()
	{
	}
	
	/**
	 * 技術カルテ分類を取得する。
	 * @param karteClassId カルテ分類ＩＤ
	 * @return 技術カルテ分類
	 */
	public TechnicKarteClass getTechnicKarteClass(Integer karteClassId)
	{
		for(TechnicKarteClass tkc : this)
		{
			if(tkc.getKarteClassId().intValue() == karteClassId.intValue())
			{
				return	tkc;
			}
		}
		
		return	null;
	}
	
	/**
	 * 技術カルテ分類のインデックスを取得する。
	 * @param karteClassId カルテ分類ＩＤ
	 * @return 技術カルテ分類のインデックス
	 */
	public Integer getTechnicKarteClassIndex(Integer karteClassId)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getKarteClassId().intValue() == karteClassId.intValue())
			{       
				return	i;
			}
		}
		
		return	null;
	}
	
	/**
	 * データを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());

		while(rs.next())
		{
			TechnicKarteClass	tkc	=	new TechnicKarteClass();
			tkc.setData(rs);
			this.add(tkc);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * データを読み込むＳＱＬ文を取得する。
	 * @return データを読み込むＳＱＬ文
	 */
	public String getLoadSQL()
	{
		return	"select		*\n"
			+	"from		mst_karte_class\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, karte_class_name\n";
	}
}

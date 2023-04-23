/*
 * MstKarteClasses.java
 *
 * Created on 2008/09/22, 21:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * カルテ分類マスタデータのArrayList
 * @author saito
 */
public class MstKarteClasses extends ArrayList<MstKarteClass>
{
	
	/**
	 * コンストラクタ
	 */
	public MstKarteClasses()
	{
	}
	
	/**
	 * カルテ分類マスタデータをArrayListに読み込む。
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstKarteClasses.getSelectSQL());

		while(rs.next())
		{
			MstKarteClass	mkc	=	new	MstKarteClass();
			mkc.setData(rs);
			this.add(mkc);
		}

		rs.close();
	}
	
	/**
	 * カルテ分類マスタデータを全て取得するＳＱＬ文を取得する
	 * @return カルテ分類マスタデータを全て取得するＳＱＬ文
	 */
	public static String getSelectSQL()
	{
		return	"select *\n" +
			"from mst_karte_class\n" +
			"where delete_date is null\n" +
			"order by display_seq, karte_class_name\n";
	}
	
	public void loadAll(ConnectionWrapper con) throws SQLException
	{
		this.load(con);
		
		for(MstKarteClass mkc : this)
		{
			mkc.loadKarteDetail(con);
		}
	}
}
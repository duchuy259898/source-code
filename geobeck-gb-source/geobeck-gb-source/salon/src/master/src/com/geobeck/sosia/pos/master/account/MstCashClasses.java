/*
 * MstCashClasses.java
 *
 * Created on 2008/09/22, 21:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * 小口項目データのArrayList
 * @author saito
 */
public class MstCashClasses extends ArrayList<MstCashClass>
{
	
	/**
	 * コンストラクタ
	 */
	public MstCashClasses()
	{
	}
	
	/**
	 * 小口分類データをArrayListに読み込む。
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstCashClasses.getSelectSQL());

		while(rs.next())
		{
			MstCashClass	mkc	=	new	MstCashClass();
			mkc.setData(rs);
			this.add(mkc);
		}

		rs.close();
	}
	
	/**
	 * 小口分類データを全て取得するＳＱＬ文を取得する
	 * @return 小口分類データを全て取得するＳＱＬ文
	 */
	public static String getSelectSQL()
	{
		return	"select *\n" +
			"from mst_cash_class\n" +
			"where delete_date is null\n" +
			"order by display_seq, cash_class_name\n";
	}
	
	public void loadAll(ConnectionWrapper con) throws SQLException
	{
		this.load(con);
		
		for(MstCashClass mkc : this)
		{
			mkc.loadCashMenu(con);
		}
	}
}
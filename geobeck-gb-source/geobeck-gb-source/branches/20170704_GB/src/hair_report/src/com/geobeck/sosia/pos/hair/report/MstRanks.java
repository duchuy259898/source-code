/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author lvtu
 */
public class MstRanks extends ArrayList<MstRank>
{
	
	/**
	 * コンストラクタ
	 */
	public MstRanks()
	{
	}
	
	/**
	 * 小口分類データをArrayListに読み込む。
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(getSelectSQL());

		while(rs.next())
		{
			MstRank	mkc	=	new	MstRank();
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
			"from mst_rank\n" +
			"where delete_date is null\n" +
			"order by rank_id\n";
	}
	
}
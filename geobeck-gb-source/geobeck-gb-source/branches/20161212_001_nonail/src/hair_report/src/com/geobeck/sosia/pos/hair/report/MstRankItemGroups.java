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
public class MstRankItemGroups extends ArrayList<MstRankItemGroup>
{
	
	/**
	 * init MstRankItemGroups.
	 */
	public MstRankItemGroups()
	{
	}
	
	/**
	 *  get data show table.
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(getSelectSQL());

		while(rs.next())
		{
			MstRankItemGroup	mrig	=	new	MstRankItemGroup();
			mrig.setData(rs);
                        this.add(mrig);
		}

		rs.close();
	}
	
	/**
	 * ñºèÃê›íË
	 * @return String.
	 */
	public static String getSelectSQL()
	{
		return	"select *\n" +
			"from mst_rank_item_group\n" +
			"where delete_date is null\n" +
			"order by item_group_id\n";
	}
	
}
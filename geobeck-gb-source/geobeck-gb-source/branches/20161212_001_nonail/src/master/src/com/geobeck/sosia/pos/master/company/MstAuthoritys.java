/*
 * MstAuthoritys.java
 *
 * Created on 2006/09/04, 11:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author katagiri
 */
public class MstAuthoritys extends ArrayList<MstAuthority>
{
	
	/**
	 * グループ
	 */
	private Integer		groupID		=	0;
	
	/**
	 * データタイプ
	 */
	private Integer		type		=	0;
	
	/**
	 * コンストラクタ
	 */
	public MstAuthoritys()
	{
	}

	/**
	 * グループを取得する。
	 * @return グループ
	 */
	public Integer getGroupID()
	{
		return groupID;
	}

	/**
	 * グループをセットする。
	 * 
	 * @param groupID 
	 */
	public void setGroup(Integer groupID)
	{
		this.groupID = groupID;
	}

	/**
	 * データタイプを取得する。
	 * @return データタイプ
	 */
	public Integer getType()
	{
		return type;
	}

	/**
	 * データタイプをセットする。
	 * @param type データタイプ
	 */
	public void setType(Integer type)
	{
		this.type = type;
	}
	
	
	/**
	 * データベースからデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		while(rs.next())
		{
			MstAuthority	ma	=	new MstAuthority();
			ma.setData(rs);
			this.add(ma);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getSelectSQL()
	{
		String	sql	=	"";
		sql	=	"select ma.*,\n" +
				"mg.group_name,\n" +
				"ms.shop_name\n" +
				"from mst_authority ma\n" +
				"left outer join mst_group mg\n" +
				"on mg.group_id = ma.group_id\n" +
				"left outer join mst_shop ms\n" +
				"on ms.shop_id = ma.shop_id\n" +
				"where ma.delete_date is null\n" +
				"and ma.group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n";
		
		switch(this.getType())
		{
			case 1:
				sql	+=	"and ma.shop_id = 0\n";
				break;
			case 2:
				sql	+=	"and ma.shop_id > 0\n";
				break;
		}
		sql	+=	"order by ma.shop_id\n";
		
		return	sql;
	}
}

/*
 * MstPlaces.java
 *
 * Created on 2008/09/03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 置き場マスタデータのArrayList
 * @author mizukawa
 */
public class MstPlaces extends ArrayList<MstPlace>
{
	private String		placeList		=	"";
	/**
	 * コンストラクタ
	 */
	public MstPlaces()
	{
	}
	
    public String getPlacesList()
	{
		return placeList;
	}
    
    public void setPlacesList(String placeList)
	{
		this.placeList = placeList;
	}
	
	/**
	 * 置き場マスタデータをArrayListに読み込む。
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstPlaces.getSelectSQL());

		while(rs.next())
		{
			MstPlace	mp	=	new	MstPlace();
			mp.setData(rs);
			this.add(mp);
		}

		rs.close();
	}
    /**
	 * 置き場マスタデータをArrayListに読み込む。
	 */
	public void Placeload(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
        
        if(isAddBlank)
		{
			this.add(new MstPlace());
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstPlaces.getSelectSQL());

		while(rs.next())
		{
			MstPlace	mp	=	new	MstPlace();
			mp.setData(rs);
			this.add(mp);
		}

		rs.close();
	}
	
	/**
	 * 置き場マスタデータを全て取得するＳＱＬ文を取得する
	 * @return 置き場マスタデータを全て取得するＳＱＬ文
	 */
	public static String getSelectSQL()
	{
		return	"select *\n" +
			"from mst_place\n" +
			"where delete_date is null\n" +
			"order by display_seq, place_name\n";
	}
}
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
	 * �R���X�g���N�^
	 */
	public MstRanks()
	{
	}
	
	/**
	 * �������ރf�[�^��ArrayList�ɓǂݍ��ށB
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
	 * �������ރf�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �������ރf�[�^��S�Ď擾����r�p�k��
	 */
	public static String getSelectSQL()
	{
		return	"select *\n" +
			"from mst_rank\n" +
			"where delete_date is null\n" +
			"order by rank_id\n";
	}
	
}
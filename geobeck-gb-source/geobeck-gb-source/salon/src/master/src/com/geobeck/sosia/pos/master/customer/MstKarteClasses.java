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
 * �J���e���ރ}�X�^�f�[�^��ArrayList
 * @author saito
 */
public class MstKarteClasses extends ArrayList<MstKarteClass>
{
	
	/**
	 * �R���X�g���N�^
	 */
	public MstKarteClasses()
	{
	}
	
	/**
	 * �J���e���ރ}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
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
	 * �J���e���ރ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �J���e���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
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
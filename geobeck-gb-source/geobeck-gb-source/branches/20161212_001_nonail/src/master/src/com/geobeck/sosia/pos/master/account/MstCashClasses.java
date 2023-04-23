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
 * �������ڃf�[�^��ArrayList
 * @author saito
 */
public class MstCashClasses extends ArrayList<MstCashClass>
{
	
	/**
	 * �R���X�g���N�^
	 */
	public MstCashClasses()
	{
	}
	
	/**
	 * �������ރf�[�^��ArrayList�ɓǂݍ��ށB
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
	 * �������ރf�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �������ރf�[�^��S�Ď擾����r�p�k��
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
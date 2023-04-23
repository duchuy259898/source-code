/*
 * MstTechnicClasses.java
 *
 * Created on 2006/05/24, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * �Z�p���ރ}�X�^�f�[�^��ArrayList
 * @author katagiri
 */
public class MstTechnicClasses extends ArrayList<MstTechnicClass>
{
	
	/**
	 * �R���X�g���N�^
	 */
	public MstTechnicClasses()
	{
	}
	
	
	/**
	 * �Z�p���ރ}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstTechnicClasses.getSelectSQL());

		while(rs.next())
		{
			MstTechnicClass	mtc	=	new	MstTechnicClass();
			mtc.setData(rs);
			this.add(mtc);
		}

		rs.close();
	}
	
	/**
	 * �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public static String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.*");
            sql.append("     ,b.technic_integration_name");
            sql.append("     ,b.display_seq");
            sql.append(" from");
            sql.append("     mst_technic_class a");
            sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
            sql.append("         using (technic_integration_id)");
            sql.append(" where");
            sql.append("     a.delete_date is null");
            sql.append(" order by");
            sql.append("      a.display_seq");
            sql.append("     ,a.technic_class_name");

            return sql.toString();
	}
}

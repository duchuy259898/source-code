/*
 * MstProportionallys.java
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
 * ���}�X�^�f�[�^��ArrayList
 * @author katagiri
 */
public class MstProportionallys extends ArrayList<MstProportionally>
{
	
	/**
	 * �R���X�g���N�^
	 */
	public MstProportionallys()
	{
	}
	
	
	/**
	 * ���}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				MstProportionallys.getSelectSQL());

		while(rs.next())
		{
			MstProportionally	mp	=	new	MstProportionally();
			mp.setData(rs);
			this.add(mp);
		}

		rs.close();
	}
	
	public int getIndexByID(Integer proportionallysID)
	{
		for(int i = 0; i < this.size(); i ++)
		{
			MstProportionally mp	=	this.get(i);
			
			if( mp.getProportionallyID() == proportionallysID )
			{
			    return i;
			}
		}
		
		return	-1;
	}
	
	/**
	 * ���}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public static String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.*");
            sql.append("     ,b.proportionally_integration_name");
            sql.append("     ,b.display_seq");
            sql.append(" from");
            sql.append("     mst_proportionally a");
            sql.append("         left join (select * from mst_proportionally_integration where delete_date is null) b");
            sql.append("         using (proportionally_integration_id)");
            sql.append(" where");
            sql.append("     a.delete_date is null");
            sql.append(" order by");
            sql.append("      a.display_seq");
            sql.append("     ,a.proportionally_name");

            return sql.toString();
	}
}

/*
 * TechnicKarteClasses.java
 *
 * Created on 2008/09/08, 22:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * �Z�p�J���e���ރf�[�^��ArrayList
 * @author saito
 */
public class TechnicKarteClasses extends Vector<TechnicKarteClass>
{
	/**
	 * �R���X�g���N�^
	 */
	public TechnicKarteClasses()
	{
	}
	
	/**
	 * �Z�p�J���e���ނ��擾����B
	 * @param karteClassId �J���e���ނh�c
	 * @return �Z�p�J���e����
	 */
	public TechnicKarteClass getTechnicKarteClass(Integer karteClassId)
	{
		for(TechnicKarteClass tkc : this)
		{
			if(tkc.getKarteClassId().intValue() == karteClassId.intValue())
			{
				return	tkc;
			}
		}
		
		return	null;
	}
	
	/**
	 * �Z�p�J���e���ނ̃C���f�b�N�X���擾����B
	 * @param karteClassId �J���e���ނh�c
	 * @return �Z�p�J���e���ނ̃C���f�b�N�X
	 */
	public Integer getTechnicKarteClassIndex(Integer karteClassId)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getKarteClassId().intValue() == karteClassId.intValue())
			{       
				return	i;
			}
		}
		
		return	null;
	}
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());

		while(rs.next())
		{
			TechnicKarteClass	tkc	=	new TechnicKarteClass();
			tkc.setData(rs);
			this.add(tkc);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * �f�[�^��ǂݍ��ނr�p�k�����擾����B
	 * @return �f�[�^��ǂݍ��ނr�p�k��
	 */
	public String getLoadSQL()
	{
		return	"select		*\n"
			+	"from		mst_karte_class\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, karte_class_name\n";
	}
}

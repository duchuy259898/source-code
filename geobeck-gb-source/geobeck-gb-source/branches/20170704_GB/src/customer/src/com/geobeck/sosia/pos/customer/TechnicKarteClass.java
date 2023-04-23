/*
 * TechnicKarteClass.java
 *
 * Created on 2008/09/08, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;


import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �Z�p�J���e���ރf�[�^
 * @author saito
 */
public class TechnicKarteClass extends ArrayList<TechnicKarte>
{
	/**
	 * �J���e���ނh�c
	 */
	protected Integer		karteClassId		=	null;
	/**
	 * �J���e���ޖ�
	 */
	protected String		karteClassName          =	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public TechnicKarteClass()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�J���e���ޖ��j
	 * @return �J���e���ޖ�
	 */
	public String toString()
	{
		return karteClassName;
	}

	/**
	 * �J���e���ނh�c���擾����B
	 * @return �J���e���ނh�c
	 */
	public Integer getKarteClassId()
	{
		return karteClassId;
	}

	/**
	* �J���e���ނh�c���Z�b�g����B
	* @param karteClassId �J���e���ނh�c
	*/
	public void setKarteClassId(Integer karteClassId)
	{
		this.karteClassId = karteClassId;
	}

	/**
	 * �J���e���ޖ����擾����B
	 * @return �J���e���ޖ�
	 */
	public String getKarteClassName()
	{
		return karteClassName;
	}

	/**
	* �J���e���ޖ����Z�b�g����B
	* @param karteClassName �J���e���ޖ�
	*/
	public void setKarteClassName(String karteClassName)
	{
                this.karteClassName	=	karteClassName;
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
                this.displaySeq	=	displaySeq;
	}
	
	/**
	 * �Z�p�J���e���擾����B
	 * @param karteClassId �J���e���ނh�c
	 * @param karteDetailId �J���e�ڍׂh�c
	 * @return �Z�p�J���e
	 */
	public TechnicKarte getTechnicKarte(Integer karteClassId, Integer karteDetailId)
	{
		for(TechnicKarte tk : this)
		{
			if(tk.getKarteClassId().intValue() == karteClassId.intValue() &&
                        tk.getKarteDetailId().intValue() == karteDetailId.intValue())
			{
				return	tk;
			}
		}
		
		return	null;
	}
	
	/**
	 * �Z�p�J���e�̃C���f�b�N�X���擾����B
	 * @param karteClassId �J���e���ނh�c
	 * @param karteDetailId �J���e�ڍׂh�c
	 * @return �Z�p�J���e�̃C���f�b�N�X
	 */
	public Integer getTechnicKarteIndex(Integer karteClassId, Integer karteDetailId)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getKarteClassId().intValue() == karteClassId.intValue() &&
                            this.get(i).getKarteDetailId().intValue() == karteDetailId.intValue())
			{       
				return	i;
			}
		}
		
		return	null;
	}
	
	/**
	 * �f�[�^��ݒ肷��B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteClassId(rs.getInt("karte_class_id"));
		this.setKarteClassName(rs.getString("karte_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * �Z�p�J���e��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadTechnicKartes(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadTechnicKartesSQL());

		while(rs.next())
		{
			TechnicKarte	tk	=	new TechnicKarte();
			tk.setData(rs);
			tk.setTechnicKarteClass(this);
			this.add(tk);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * �Z�p�J���e��ǂݍ��ނr�p�k�����擾����B
	 * @return �Z�p�J���e��ǂݍ��ނr�p�k��
	 */
	public String getLoadTechnicKartesSQL()
	{
		return	"select		*\n"
			+	"from		mst_karte_detail\n"
			+	"where		delete_date is null\n"
        		+	"and karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n"
			+	"order by	display_seq, karte_detail_name\n";
	}
}

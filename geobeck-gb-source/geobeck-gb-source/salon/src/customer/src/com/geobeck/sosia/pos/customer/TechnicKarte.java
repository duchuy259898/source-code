/*
 * TechnicKarte.java
 *
 * Created on 2008/09/08, 21:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;

/**
 * �Z�p�J���e�f�[�^
 * @author saito
 */
public class TechnicKarte  extends ArrayList<TechnicKarteReference>
{
	/**
	 * ����
	 */
	protected	TechnicKarteClass	technicKarteClass	=	new TechnicKarteClass();
	/**
	 * �J���e���ނh�c
	 */
	protected Integer		karteClassId		=	null;
	/**
	 * �J���e�ڍׂh�c
	 */
	protected Integer		karteDetailId		=	null;
	/**
	 * �J���e�ڍז�
	 */
	protected	String		karteDetailName         =	null;
	/**
	 * ���e
	 */
	private	String		contents                =	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;

        /**
	 * �R���X�g���N�^
	 */
	public TechnicKarte()
	{
	}

	/**
	 * ���ނ��擾����B
	 * @return ����
	 */
	public TechnicKarteClass getTechnicKarteClass()
	{
		return technicKarteClass;
	}

	/**
	 * ���ނ�ݒ肷��B
	 * @param technicKarteClass ����
	 */
	public void setTechnicKarteClass(TechnicKarteClass technicKarteClass)
	{
		this.technicKarteClass = technicKarteClass;
	}
	
	/**
	 * ������ɕϊ�����B�i�J���e�ڍז��j
	 * @return �J���e�ڍז�
	 */
	public String toString()
	{
		return karteDetailName;
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
	 * �J���e�ڍׂh�c���擾����B
	 * @return �J���e�ڍׂh�c
	 */
	public Integer getKarteDetailId()
	{
		return karteDetailId;
	}

	/**
	* �J���e�ڍׂh�c���Z�b�g����B
	* @param karteDetailId �J���e�ڍׂh�c
	*/
	public void setKarteDetailId(Integer karteDetailId)
	{
		this.karteDetailId = karteDetailId;
	}

	/**
	 * �J���e�ڍז����擾����B
	 * @return �J���e�ڍז�
	 */
	public String getKarteDetailName()
	{
		return karteDetailName;
	}

	/**
	* �J���e�ڍז����Z�b�g����B
	* @param karteDetailName �J���e�ڍז�
	*/
	public void setKarteDetailName(String karteDetailName)
	{
                this.karteDetailName	=	karteDetailName;
	}

	/**
	 * ���e���擾����B
	 * @return ���e
	 */
	public String getContents()
	{
		return contents;
	}

	/**
	* ���e���Z�b�g����B
	* @param contents ���e
	*/
	public void setContents(String contents)
	{
            this.contents	=	contents;
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
                this.displaySeq	=	null;
	}
	
	/**
	 * �Z�p�J���e�Q�Ƃ��擾����B
	 * @param karteReferenceId �J���e�Q�Ƃh�c
	 * @param karteDetailId �J���e�ڍׂh�c
	 * @return �Z�p�J���e�Q��
	 */
	public TechnicKarteReference getTechnicKarteReference(Integer karteReferenceId, Integer karteDetailId)
	{
		for(TechnicKarteReference tkr : this)
		{
			if(tkr.getKarteReferenceId() == karteReferenceId &&
                        tkr.getKarteDetailId() == karteDetailId)
			{
				return	tkr;
			}
		}
		
		return	null;
	}
	
	/**
	 * �Z�p�J���e�Q�Ƃ̃C���f�b�N�X���擾����B
	 * @param karteReferenceId �J���e�Q�Ƃh�c
	 * @param karteDetailId �J���e�ڍׂh�c
	 * @return �Z�p�J���e�Q�Ƃ̃C���f�b�N�X
	 */
	public Integer getTechnicKarteReferenceIndex(Integer karteReferenceId, Integer karteDetailId)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getKarteReferenceId().intValue() == karteReferenceId.intValue() &&
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
		this.setKarteDetailId(rs.getInt("karte_detail_id"));
		this.setKarteDetailName(rs.getString("karte_detail_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * �Z�p�J���e�Q�Ƃ�ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadTechnicKarteReferences(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadTechnicKarteReferencesSQL());

		while(rs.next())
		{
			TechnicKarteReference	tkr	=	new TechnicKarteReference();
			tkr.setData(rs);
			tkr.setTechnicKarte(this);
			this.add(tkr);
		}

		rs.close();
		
		return	true;
	}
	
	/**
	 * �Z�p�J���e�Q�Ƃ�ǂݍ��ނr�p�k�����擾����B
	 * @return �Z�p�J���e�Q�Ƃ�ǂݍ��ނr�p�k��
	 */
	public String getLoadTechnicKarteReferencesSQL()
	{
		return	"select		*\n"
			+	"from		mst_karte_reference\n"
			+	"where		delete_date is null\n"
        		+	"and karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n"
			+	"order by	display_seq, karte_reference_name\n";
	}
}

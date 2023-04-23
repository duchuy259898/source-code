/*
 * MstKarteDetail.java
 *
 * Created on 2008/09/22, 20:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.ArrayList;

/**
 * �J���e�ڍ׃}�X�^�f�[�^
 * @author saito
 */
public class MstKarteDetail extends ArrayList<MstKarteReference>
{
	/**
	 * �J���e����
	 */
	protected	MstKarteClass	karteClass		=	new MstKarteClass();
	/**
	 * �J���e�ڍׂh�c
	 */
	protected Integer		karteDetailId		=	null;
	/**
	 * �J���e�ڍז�
	 */
	protected String		karteDetailName         =	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstKarteDetail()
	{
	}
        
	/**
	 * �R���X�g���N�^
	 * @param karteDetailId �J���e�ڍׂh�c
	 */
	public MstKarteDetail(Integer karteDetailId)
	{
		this.setKarteDetailId(karteDetailId);
	}

	/**
	 * �J���e���ނ��擾����B
	 * @return �J���e����
	 */
	public MstKarteClass getKarteClass()
	{
		return karteClass;
	}

	/**
	 * �J���e���ނ��Z�b�g����B
	 * @param karteClass �J���e����
	 */
	public void setKarteClass(MstKarteClass karteClass)
	{
		this.karteClass = karteClass;
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
	 * �I�u�W�F�N�g���r����B
	 * @param o �I�u�W�F�N�g
	 */
	public boolean equals(Object o)
	{
		if(o instanceof MstKarteDetail)
		{
			MstKarteDetail	mkd	=	(MstKarteDetail)o;
			
			if(mkd.getKarteClass().getKarteClassId() == karteClass.getKarteClassId() &&
					mkd.getKarteDetailId() == karteDetailId &&
					mkd.getKarteDetailName().equals(karteDetailName) &&
					mkd.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * �J���e�ڍ׃}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mkd �J���e�ڍ׃}�X�^�f�[�^
	 */
	public void setData(MstKarteDetail mkd)
	{
                this.setKarteClass(new MstKarteClass(mkd.getKarteClass().getKarteClassId()));
		this.setKarteDetailId(mkd.getKarteDetailId());
		this.setKarteDetailName(mkd.getKarteDetailName());
		this.setDisplaySeq(mkd.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteClass(new MstKarteClass(rs.getInt("karte_class_id")));
		this.setKarteDetailId(rs.getInt("karte_detail_id"));
		this.setKarteDetailName(rs.getString("karte_detail_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * �J���e�ڍ׃}�X�^�Ƀf�[�^��o�^����B
	 * @return true - ����
	 * @param lastSeq �\�����̍ő�l
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
			
			this.setMaxKarteDetailID(con);
		}
		
		return	true;
	}
	
	/**
	 * �J���e�ڍׂh�c�̍ő�l���擾����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	private void setMaxKarteDetailID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxKarteDetailIDSQL());
		
		if(rs.next())
		{
			this.setKarteDetailId(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * �J���e�ڍ׃}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getDeleteSQL();
		}
		else
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * �J���e�ڍ׃}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getKarteDetailId() == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select mkc.karte_class_name, mkd.*\n" +
				"from mst_karte_detail mkd\n" +
				"left outer join mst_karte_class mkc\n" +
				"on mkc.karte_class_id = mkd.karte_class_id\n" +
				"and mkc.delete_date is null\n" +
                                "where mkd.delete_date is null\n" +
				"and mkd.karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_karte_detail\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_karte_detail\n" +
				"(karte_class_id, karte_detail_id, karte_detail_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + ",\n" +
				"(select coalesce(max(karte_detail_id), 0) + 1\n" +
				"from mst_karte_detail),\n" +
				SQLUtil.convertForSQL(this.getKarteDetailName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_karte_detail\n" +
				"set\n" +
				"karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + ",\n" +
				"karte_detail_name = " + SQLUtil.convertForSQL(this.getKarteDetailName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "\n" +
				"and karte_detail_id != " +
				SQLUtil.convertForSQL(this.getKarteDetailId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_detail\n" +
				"where delete_date is null\n" +
				"and karte_class_id = " +
				SQLUtil.convertForSQL(this.getKarteClass().getKarteClassId()) + "\n" +
				"and karte_detail_id != " +
				SQLUtil.convertForSQL(this.getKarteDetailId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String getDeleteSQL()
	{
		return	"update mst_karte_detail\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n";
	}
	
	/**
	 * �J���e�ڍׂh�c�̍ő�l���擾����SQL�����擾����B
	 * @return �J���e�ڍׂh�c�̍ő�l���擾����SQL��
	 */
	private static String getMaxKarteDetailIDSQL()
	{
		return	"select max(karte_detail_id) as max_id\n" +
				"from mst_karte_detail\n";
	}
	
	/**
	 * �J���e�Q�ƃ}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param con ConnectionWrapper
	 */
	public void loadKarteReference(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectKarteReferenceSQL());

		while(rs.next())
		{
			MstKarteReference	mkr	=	new	MstKarteReference();
			mkr.setData(rs);
			this.add(mkr);
		}

		rs.close();
	}
	
	/**
	 * �J���e�Q�ƃ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �J���e�Q�ƃ}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectKarteReferenceSQL()
	{
		return	"select *\n" +
			"from mst_karte_reference\n" +
			"where delete_date is null\n" +
			"and karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetailId()) + "\n" +
			"order by display_seq, karte_reference_id\n";
	}
}
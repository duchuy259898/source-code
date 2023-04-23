/*
 * MstKarteReference.java
 *
 * Created on 2008/09/23, 10:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �J���e�Q�ƃ}�X�^�f�[�^
 * @author saito
 */
public class MstKarteReference
{
	/**
	 * �J���e�ڍ�
	 */
	protected	MstKarteDetail	karteDetail		=	new MstKarteDetail();
	/**
	 * �J���e�Q�Ƃh�c
	 */
	protected Integer		karteReferenceId	=	null;
	/**
	 * �J���e�Q�Ɩ�
	 */
	protected String		karteReferenceName      =	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstKarteReference()
	{
	}

	/**
	 * �J���e�ڍׂ��擾����B
	 * @return �J���e�ڍ�
	 */
	public MstKarteDetail getKarteDetail()
	{
		return karteDetail;
	}

	/**
	 * �J���e�ڍׂ��Z�b�g����B
	 * @param karteDetail �J���e�ڍ�
	 */
	public void setKarteDetail(MstKarteDetail karteDetail)
	{
		this.karteDetail = karteDetail;
	}
	
	/**
	 * ������ɕϊ�����B�i�J���e�Q�Ɩ��j
	 * @return �J���e�Q�Ɩ�
	 */
	public String toString()
	{
		return karteReferenceName;
	}

	/**
	 * �J���e�Q�Ƃh�c���擾����B
	 * @return �J���e�Q�Ƃh�c
	 */
	public Integer getKarteReferenceId()
	{
		return karteReferenceId;
	}

	/**
	* �J���e�Q�Ƃh�c���Z�b�g����B
	* @param karteReferenceId �J���e�Q�Ƃh�c
	*/
	public void setKarteReferenceId(Integer karteReferenceId)
	{
		this.karteReferenceId = karteReferenceId;
	}

	/**
	 * �J���e�Q�Ɩ����擾����B
	 * @return �J���e�Q�Ɩ�
	 */
	public String getKarteReferenceName()
	{
		return karteReferenceName;
	}

	/**
	* �J���e�Q�Ɩ����Z�b�g����B
	* @param karteReferenceName �J���e�Q�Ɩ�
	*/
	public void setKarteReferenceName(String karteReferenceName)
	{
                this.karteReferenceName	=	karteReferenceName;
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
	 * �J���e�Q�ƃ}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mkr �J���e�Q�ƃ}�X�^�f�[�^
	 */
	public void setData(MstKarteReference mkr)
	{
                this.setKarteDetail(new MstKarteDetail(mkr.getKarteDetail().getKarteDetailId()));
		this.setKarteReferenceId(mkr.getKarteReferenceId());
		this.setKarteReferenceName(mkr.getKarteReferenceName());
		this.setDisplaySeq(mkr.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteDetail(new MstKarteDetail(rs.getInt("karte_detail_id")));
		this.setKarteReferenceId(rs.getInt("karte_reference_id"));
		this.setKarteReferenceName(rs.getString("karte_reference_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * �J���e�Q�ƃ}�X�^�Ƀf�[�^��o�^����B
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
			
			this.setMaxKarteReferenceID(con);
		}
		
		return	true;
	}
	
	/**
	 * �J���e�Q�Ƃh�c�̍ő�l���擾����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	private void setMaxKarteReferenceID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxKarteReferenceIDSQL());
		
		if(rs.next())
		{
			this.setKarteReferenceId(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * �J���e�Q�ƃ}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �J���e�Q�ƃ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getKarteReferenceId() == null)	return	false;
		
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
		return	"select mkd.karte_detail_name, mkr.*\n" +
				"from mst_karte_reference mkr\n" +
				"left outer join mst_karte_detail mkd\n" +
				"on mkd.karte_detail_id = mkr.karte_detail_id\n" +
				"and mkd.delete_date is null\n" +
                                "where mkr.delete_date is null\n" +
				"and mkr.karte_reference_id = " + SQLUtil.convertForSQL(this.getKarteReferenceId()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_karte_reference\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_karte_reference\n" +
				"(karte_detail_id, karte_reference_id, karte_reference_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + ",\n" +
				"(select coalesce(max(karte_reference_id), 0) + 1\n" +
				"from mst_karte_reference),\n" +
				SQLUtil.convertForSQL(this.getKarteReferenceName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_karte_reference\n" +
				"set\n" +
				"karte_detail_id = " + SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + ",\n" +
				"karte_reference_name = " + SQLUtil.convertForSQL(this.getKarteReferenceName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "\n" +
				"and karte_reference_id != " +
				SQLUtil.convertForSQL(this.getKarteReferenceId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_reference\n" +
				"where delete_date is null\n" +
				"and karte_detail_id = " +
				SQLUtil.convertForSQL(this.getKarteDetail().getKarteDetailId()) + "\n" +
				"and karte_reference_id != " +
				SQLUtil.convertForSQL(this.getKarteReferenceId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where karte_reference_id = " + SQLUtil.convertForSQL(this.getKarteReferenceId()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String getDeleteSQL()
	{
		return	"update mst_karte_reference\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	karte_reference_id = " + SQLUtil.convertForSQL(this.getKarteReferenceId()) + "\n";
	}
	
	/**
	 * �J���e�Q�Ƃh�c�̍ő�l���擾����SQL�����擾����B
	 * @return �J���e�Q�Ƃh�c�̍ő�l���擾����SQL��
	 */
	private static String getMaxKarteReferenceIDSQL()
	{
		return	"select max(karte_reference_id) as max_id\n" +
				"from mst_karte_reference\n";
	}
}
/*
 * MstKarteClass.java
 *
 * Created on 2008/09/22, 20:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �J���e���ރ}�X�^�f�[�^
 * @author saito
 */
public class MstKarteClass extends ArrayList<MstKarteDetail>
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
	public MstKarteClass()
	{
		super();
	}
        
	/**
	 * �R���X�g���N�^
	 * @param karteClassId �J���e���ނh�c
	 */
	public MstKarteClass(Integer karteClassId)
	{
		this.setKarteClassId(karteClassId);
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
	 * �I�u�W�F�N�g���r����B
	 * @param o �I�u�W�F�N�g
	 */
	public boolean equals(Object o)
	{
		if(o instanceof MstKarteClass)
		{
			MstKarteClass	mkc	=	(MstKarteClass)o;
			
			if(mkc.getKarteClassId() == karteClassId &&
					mkc.getKarteClassName().equals(karteClassName) &&
					mkc.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * �J���e���ރ}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mkc �J���e���ރ}�X�^�f�[�^
	 */
	public void setData(MstKarteClass mkc)
	{
		this.setKarteClassId(mkc.getKarteClassId());
		this.setKarteClassName(mkc.getKarteClassName());
		this.setDisplaySeq(mkc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
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
	 * �J���e���ރ}�X�^�Ƀf�[�^��o�^����B
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
		}
		
		return	true;
	}
	
	
	/**
	 * �J���e���ރ}�X�^����f�[�^���폜����B�i�_���폜�j
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * �J���e���ރ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getKarteClassId() == null || this.getKarteClassId() < 1)	return	false;
		
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
		return	"select *\n"
			+	"from mst_karte_class\n"
                        +	"where delete_date is null\n"
			+	"and karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_karte_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_karte_class\n" +
				"(karte_class_id, karte_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(karte_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getKarteClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_karte_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_karte_class\n" +
				"set\n" +
				"karte_class_name = " + SQLUtil.convertForSQL(this.getKarteClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null\n" +
				"and karte_class_id != " +
				SQLUtil.convertForSQL(this.getKarteClassId()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_karte_class\n" +
				"where delete_date is null\n" +
				"and karte_class_id != " +
				SQLUtil.convertForSQL(this.getKarteClassId()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_karte_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n";
	}
	
	/**
	 * �J���e�ڍ׃}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param con ConnectionWrapper
	 */
	public void loadKarteDetail(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectKarteDetailSQL());

		while(rs.next())
		{
			MstKarteDetail	mkd	=	new	MstKarteDetail();
			mkd.setData(rs);
			this.add(mkd);
		}

		rs.close();
	}
	
	/**
	 * �J���e�ڍ׃}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @return �J���e�ڍ׃}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectKarteDetailSQL()
	{
		return	"select *\n" +
			"from mst_karte_detail\n" +
			"where delete_date is null\n" +
			"and karte_class_id = " + SQLUtil.convertForSQL(this.getKarteClassId()) + "\n" +
			"order by display_seq, karte_detail_id\n";
	}
}

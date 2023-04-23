/*
 * MstFreeHeading.java
 *
 * Created on 2007/08/17, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author kanemoto
 */
public class MstFreeHeading {
	protected MstFreeHeadingClass freeHeadingClass = new MstFreeHeadingClass();  /** �t���[���ڕ��� */
	protected Integer	      freeHeadingID    = null;				/** �t���[���ڂh�c */
	protected String	      freeHeadingName  = "";				 /** �t���[���ږ� */
	protected Integer	      displaySeq       = null;				/** �\���� */
        protected String            freeHeadingText    =  "";

	/** Creates a new instance of MstFreeHeading */
	public MstFreeHeading()
	{
	}
        //An start add 20130405
        public String getFreeHeadingText() {
            return freeHeadingText;
        }

        public void setFreeHeadingText(String freeHeadingText) {
            this.freeHeadingText = freeHeadingText;
        }

	//An end add 20130401

	/**
	 * �t���[���ڕ��ނ��擾����B
	 * @return �t���[���ڕ���
	 */
	public MstFreeHeadingClass getFreeHeadingClass()
	{
		return freeHeadingClass;
	}

	/**
	 * �t���[���ڕ��ނ��Z�b�g����B
	 * @param freeHeadingClass �t���[���ڕ���
	 */
	public void setFreeHeadingClass(MstFreeHeadingClass freeHeadingClass)
	{
		this.freeHeadingClass = freeHeadingClass;
	}

	/**
	 * �t���[���ڂh�c���擾����B
	 * @return �t���[���ڂh�c
	 */
	public Integer getFreeHeadingID()
	{
		return freeHeadingID;
	}

	/**
	 * �t���[���ڂh�c���Z�b�g����B
	 * @param freeHeadingID �t���[���ڂh�c
	 */
	public void setFreeHeadingID(Integer freeHeadingID)
	{
		this.freeHeadingID = freeHeadingID;
	}

	/**
	 * �t���[���ږ����擾����B
	 * @return �t���[���ږ�
	 */
	public String getFreeHeadingName()
	{
		return freeHeadingName;
	}

	/**
	 * �t���[���ږ����Z�b�g����B
	 * @param freeHeadingName �t���[���ږ�
	 */
	public void setFreeHeadingName(String freeHeadingName)
	{
		this.freeHeadingName = freeHeadingName;
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
		this.displaySeq = displaySeq;
	}

	public String toString()
	{
		return	this.getFreeHeadingName();
	}

	/**
	 * �t���[���ڃ}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mt �t���[���ڃ}�X�^�f�[�^
	 */
	public void setData(MstFreeHeading mt)
	{
		this.setFreeHeadingClass(new MstFreeHeadingClass(mt.getFreeHeadingClass().getFreeHeadingClassID()));
		this.setFreeHeadingID(mt.getFreeHeadingID());
		this.setFreeHeadingName(mt.getFreeHeadingName());
		this.setDisplaySeq(mt.getDisplaySeq());
	}

	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setFreeHeadingClass(new MstFreeHeadingClass(rs.getInt("free_heading_class_id")));
		this.setFreeHeadingID(rs.getInt("free_heading_id"));
		this.setFreeHeadingName(rs.getString("free_heading_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}




	/**
	 * �f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @param data �ΏۂƂȂ�f�[�^
	 * @param dataIndex �f�[�^�̃C���f�b�N�X
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//���݂��Ȃ��f�[�^�̏ꍇ
		if(!this.isExist(con))
		{
			//�\���������͂���Ă���ꍇ
			if(0 <= this.getDisplaySeq())
			{
				//���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			//�f�[�^��Insert����
			if(con.executeUpdate(this.getInsertDataSQL()) != 1)
			{
				return	false;
			}
		}
		//���݂���f�[�^�̏ꍇ
		else
		{
			Integer		lastSeq		=	this.getLastDisplaySeq(con);

			//�\�������ύX����Ă���ꍇ
			if(this.getDisplaySeq() != lastSeq)
			{
				//���̕\�����ȍ~�̃f�[�^�̕\�������P���Z����
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				//�\���������͂���Ă���ꍇ
				if(0 <= this.getDisplaySeq())
				{
					//���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			//�f�[�^��Update����
			if(con.executeUpdate(this.getUpdateDataSQL()) != 1)
			{
				return	false;
			}
		}

		return	true;
	}

	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		boolean		result		=	false;

		if(this.getFreeHeadingClass() == null ||
				this.getFreeHeadingClass().getFreeHeadingClassID() == null ||
				this.getFreeHeadingID() == null)
		{
			return	result;
		}

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			result	=	true;
		}

		rs.close();

		return	result;
	}

	private Integer getLastDisplaySeq(ConnectionWrapper con) throws SQLException
	{
		Integer		result	=	-1;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			result	=	rs.getInt("display_seq");
		}

		rs.close();

		return	result;
	}

	/**
	 * �f�[�^���폜����B
	 * @param con ConnectionWrapper
	 * @param data �ΏۂƂȂ�f�[�^
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}

		if(con.executeUpdate(this.getDeleteDataSQL()) != 1)
		{
			return	false;
		}

		return	true;
	}

	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_free_heading\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id = " + SQLUtil.convertForSQL(this.getFreeHeadingID()) + "\n";
	}

	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_free_heading\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}

	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertDataSQL()
	{
		return	"insert into mst_free_heading\n" +
				"(free_heading_class_id, free_heading_id, free_heading_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + ",\n" +
				"coalesce(max(free_heading_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_free_heading\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n";
	}

	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateDataSQL()
	{
		return	"update mst_free_heading\n" +
				"set\n" +
				"free_heading_name = " + SQLUtil.convertForSQL(this.getFreeHeadingName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id != " +
				SQLUtil.convertForSQL(this.getFreeHeadingID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id != " +
				SQLUtil.convertForSQL(this.getFreeHeadingID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id = " + SQLUtil.convertForSQL(this.getFreeHeadingID()) + "\n";
	}

	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteDataSQL()
	{
		return	"update mst_free_heading\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id = " + SQLUtil.convertForSQL(this.getFreeHeadingID()) + "\n";
	}
}

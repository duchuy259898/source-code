/*
 * MstTechnicClass.java
 *
 * Created on 2006/05/24, 11:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �R�[�X���ރ}�X�^�f�[�^
 * @author furukawa
 */
public class MstCourseClass extends ArrayList<MstCourse>
{
	/**
	 * �R�[�X���ނh�c
	 */
	protected	Integer		courseClassID		=	null;
	/**
	 * �R�[�X���ޖ�
	 */
	protected	String		courseClassName	=	"";
	/**
	 * �R�[�X���ޖ�����
	 */
	protected	String		courseClassContractedName	=	"";
	/**
	 * �\����
	 */
	protected	Integer		displaySeq			=	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstCourseClass()
	{
	}
	/**
	 * �R���X�g���N�^
	 * @param technicClassID �R�[�X���ނh�c
	 */
	public MstCourseClass(Integer courseClassID)
	{
		this.setCourseClassID(courseClassID);
	}
	
	/**
	 * ������ɕϊ�����B�i�R�[�X���ޖ��j
	 * @return �R�[�X���ޖ�
	 */
	public String toString()
	{
		return	this.getCourseClassName();
	}

	/**
	 * �R�[�X���ނh�c���擾����B
	 * @return �R�[�X���ނh�c
	 */
	public Integer getCourseClassID()
	{
		return courseClassID;
	}

	/**
	 * �R�[�X���ނh�c���Z�b�g����B
	 * @param technicClassID �R�[�X���ނh�c
	 */
	public void setCourseClassID(Integer courseClassID)
	{
		this.courseClassID = courseClassID;
	}

	/**
	 * �R�[�X���ޖ����擾����B
	 * @return �R�[�X���ޖ�
	 */
	public String getCourseClassName()
	{
		return courseClassName;
	}

	/**
	 * �R�[�X���ޖ����Z�b�g����B
	 * @param courseClassName �R�[�X���ޖ�
	 */
	public void setCourseClassName(String courseClassName)
	{
		this.courseClassName = courseClassName;
	}

	/**
	 * �R�[�X���ޖ����̂��擾����B
	 * @return �R�[�X���ޖ�����
	 */
	public String getCourseClassContractedName()
	{
		if(courseClassContractedName == null){
			return "";
		}
		return courseClassContractedName;
	}

	/**
	 * �R�[�X���ޖ����̂��Z�b�g����B
	 * @param technicClassName �R�[�X���ޖ�����
	 */
	public void setCourseClassContractedName(String courseClassContractedName)
	{
		this.courseClassContractedName = courseClassContractedName;
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
	
	public boolean equals(Object o)
	{
		if(o instanceof MstCourseClass)
		{
			MstCourseClass	mtc	=	(MstCourseClass)o;
			
			if(mtc.getCourseClassID() == courseClassID &&
					mtc.getCourseClassName().equals(courseClassName) &&
					mtc.getCourseClassContractedName().equals( courseClassContractedName ) &&
					mtc.getDisplaySeq() == displaySeq)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * �R�[�X���ރ}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mtc �Z�p���ރ}�X�^�f�[�^
	 */
	public void setData(MstCourseClass mtc)
	{
		this.setCourseClassID(mtc.getCourseClassID());
		this.setCourseClassName(mtc.getCourseClassName());
		this.setCourseClassContractedName( mtc.getCourseClassContractedName() );
		this.setDisplaySeq(mtc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCourseClassID(rs.getInt("course_class_id"));
		this.setCourseClassName(rs.getString("course_class_name"));
		this.setCourseClassContractedName( rs.getString( "course_class_contracted_name" ) );
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * �R�[�X���ރ}�X�^�Ƀf�[�^��o�^����B
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
	 * �R�[�X���ރ}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �R�[�X���ރ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCourseClassID() == null || this.getCourseClassID() < 1)	return	false;
		
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
			+	"from mst_course_class\n"
			+	"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����B
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_course_class\n" +
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
		return	"insert into mst_course_class\n" +
				"(course_class_id, course_class_name, course_class_contracted_name, display_seq, \n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(course_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getCourseClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getCourseClassContractedName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_course_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_course_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_course_class\n" +
				"set\n" +
				"course_class_name = " + SQLUtil.convertForSQL(this.getCourseClassName()) + ",\n" +
				"course_class_contracted_name = " + SQLUtil.convertForSQL(this.getCourseClassContractedName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_course_class\n" +
				"where delete_date is null\n" +
				"and course_class_id != " +
				SQLUtil.convertForSQL(this.getCourseClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_course_class\n" +
				"where delete_date is null\n" +
				"and course_class_id != " +
				SQLUtil.convertForSQL(this.getCourseClassID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_course_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * �R�[�X���ރ}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param technicClassID �Z�p���ނh�c
	 */
	public void loadCourse(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectCourseSQL());

		while(rs.next())
		{
			MstCourse	mc	=	new	MstCourse();
			mc.setData(rs);
			this.add(mc);
		}

		rs.close();
	}
	
	/**
	 * �R�[�X�}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @param technicClassID �R�[�X���ނh�c
	 * @return �R�[�X���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectCourseSQL()
	{
		return	"select *\n" +
			"from mst_course\n" +
			"where delete_date is null\n" +
					"and course_class_id = " +
					SQLUtil.convertForSQL(this.getCourseClassID()) + "\n" +
			"order by display_seq, course_id\n";
	}
}

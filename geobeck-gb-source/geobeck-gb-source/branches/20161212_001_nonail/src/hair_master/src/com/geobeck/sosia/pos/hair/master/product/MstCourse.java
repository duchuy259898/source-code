/*
 * MstCourse.java
 *
 * Created on 2006/05/24, 11:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �R�[�X�}�X�^�f�[�^
 * @author katagiri
 */
public class MstCourse
{
	/**
	 * �R�[�X����
	 */
	protected	MstCourseClass	courseClass	=	new MstCourseClass();
	/**
	 * �R�[�X�h�c
	 */
	protected	Integer			courseID		=	null;
	/**
	 * �R�[�X��
	 */
	protected	String			courseName		=	"";
        /**
         * ������
         */
        protected       Integer                 num    = null;
	/**
	 * �P��
	 */
	protected	Long			price			=	null;
	/**
	 * �{�p����
	 */
	protected	Integer			operationTime	=	null;
	/**
	 * �\����
	 */
	protected	Integer			displaySeq		=	null;
        /**
         * �L������
         */
        protected       Integer                 praiseTimeLimit =   new Integer(0);
        /**
         * �L�������g�p�L��
         */
        protected       Boolean                 isPraiseTime    =   false;

        /** Creates a new instance of MstCourse */
	public MstCourse()
	{
	}
        
	/**
	 * �R�[�X���ނ��擾����B
	 * @return �R�[�X����
	 */
	public MstCourseClass getCourseClass()
	{
		return courseClass;
	}

	/**
	 * �R�[�X���ނ��Z�b�g����B
	 * @param courseClass �R�[�X����
	 */
	public void setCourseClass(MstCourseClass courseClass)
	{
		this.courseClass = courseClass;
	}

	/**
	 * �R�[�X�h�c���擾����B
	 * @return �R�[�X�h�c
	 */
	public Integer getCourseID()
	{
		return courseID;
	}

	/**
	 * �R�[�X�h�c���Z�b�g����B
	 * @param courseID �R�[�X�h�c
	 */
	public void setCourseID(Integer courseID)
	{
		this.courseID = courseID;
	}

	/**
	 * �R�[�X�����擾����B
	 * @return �R�[�X��
	 */
	public String getCourseName()
	{
		return courseName;
	}

	/**
	 * �R�[�X�����Z�b�g����B
	 * @param courseName �R�[�X��
	 */
	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}

        /**
         * �����񐔂��擾����
         * @return ������
         */
        public Integer getNum(){
            return num;
        }

        /**
         * �����񐔂��Z�b�g����
         * @param consumptionCount ������
         */
        public void setNum(Integer num){
            this.num = num;
        }

	/**
	 * �P�����擾����B
	 * @return �P��
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * �P�����Z�b�g����B
	 * @param price �P��
	 */
	public void setPrice(Long price)
	{
		this.price = price;
	}

	/**
	 * �{�p���Ԃ��擾����B
	 * @return �{�p����
	 */
	public Integer getOperationTime()
	{
		return operationTime;
	}

	/**
	 * �{�p���Ԃ��Z�b�g����B
	 * @param operationTime �{�p����
	 */
	public void setOperationTime(Integer operationTime)
	{
		this.operationTime = operationTime;
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

        /**
         * �L���������Z�b�g����
         * @param praiseTime
         */
        public void setPraiseTimeLimit(Integer praiseTime){
            this.praiseTimeLimit = praiseTime;
        }
        /**
         * �L���������擾����
         * @param praiseTime
         */
        public Integer getPraiseTimeLimit(){
            return this.praiseTimeLimit;
        }

        /**
         * �L�������g�p�L�����Z�b�g����
         * @param b
         */
        public void setPraiseTime(Boolean b){
            this.isPraiseTime = b;
        }
        /**
         * �L�������g�p�L�����擾����
         * @param b
         */
        public Boolean isPraiseTime(){
            return this.isPraiseTime;
        }

	public String toString()
	{
		return	this.getCourseName();
	}

	/**
	 * �Z�p�}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mt �Z�p�}�X�^�f�[�^
	 */
	public void setData(MstCourse mc)
	{
		this.setCourseClass(new MstCourseClass(mc.getCourseClass().getCourseClassID()));
		this.setCourseID(mc.getCourseID());
		this.setCourseName(mc.getCourseName());
                this.setPraiseTime(mc.isPraiseTime);
                this.setPraiseTimeLimit(mc.getPraiseTimeLimit());
                this.setNum(mc.getNum());
		this.setPrice(mc.getPrice());
		this.setOperationTime(mc.getOperationTime());
		this.setDisplaySeq(mc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setCourseClass(new MstCourseClass(rs.getInt("course_class_id")));
		this.setCourseID(rs.getInt("course_id"));
		this.setCourseName(rs.getString("course_name"));
                this.setPraiseTime(rs.getBoolean("is_praise_time"));
                this.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                this.setNum(rs.getInt("num"));
		this.setPrice(rs.getLong("price"));
		this.setOperationTime(rs.getInt("operation_time"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * �Z�p�}�X�^�Ƀf�[�^��o�^����B
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
                                if (0 < lastSeq)
                                {
                                    if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
                                    {
                                            return	false;
                                    }
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
			
			this.setMaxCourseID(con);
		}
		
		return	true;
	}
	
	private void setMaxCourseID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxCourseIDSQL());
		
		if(rs.next())
		{
			this.setCourseID(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * �Z�p�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �Z�p�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getCourseID() == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

        	/**
	 * �Z�p�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExistsByCourseName(ConnectionWrapper con) throws SQLException
	{
		if(this.getCourseName() == null || this.getCourseName().equals(""))	return	false;

		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByCourseNameSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_course\n" +
				"where	course_id = " + SQLUtil.convertForSQL(this.getCourseID()) + "\n";
	}

        	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectByCourseNameSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mcc.course_class_name");
            sql.append("     ,mc.*");
            sql.append(" from");
            sql.append("     mst_course mc");
            sql.append("         join mst_course_class mcc");
            sql.append("         using (course_class_id)");
            sql.append(" where");
            sql.append("         mc.delete_date is null");
            sql.append("     and mcc.delete_date is null");
            sql.append("     and course_name = " + SQLUtil.convertForSQL(this.getCourseName()));

            return sql.toString();
	}

	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" update mst_course");
                sql.append(" set");
                sql.append("     display_seq = display_seq " + (isIncrement ? "+" : "-") + " 1");
                sql.append(" where");
                sql.append("         delete_date is null");
                sql.append("     and course_class_id = " + SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()));
                sql.append("     and display_seq >= " + SQLUtil.convertForSQL(seq));
                sql.append("     and display_seq " + (isIncrement ? "+" : "-") + " 1 >= 0");
                
                if (!isIncrement) {
                    sql.append("     and not exists");
                    sql.append("            (");
                    sql.append("                 select 1");
                    sql.append("                 from");
                    sql.append("                     (");
                    sql.append("                         select");
                    sql.append("                             count(*) as cnt");
                    sql.append("                         from");
                    sql.append("                             mst_course");
                    sql.append("                         where");
                    sql.append("                                 course_class_id = " + SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()));
                    sql.append("                             and delete_date is null");
                    sql.append("                         group by");
                    sql.append("                             display_seq");
                    sql.append("                     ) t");
                    sql.append("                 where");
                    sql.append("                     cnt > 1");
                    sql.append("            )");
                }

                return sql.toString();
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_course\n" +
				"(course_class_id, course_id, course_name,\n" +
				"num, price, operation_time, display_seq, praise_time_limit, is_praise_time, \n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()) + ",\n" +
				"(select coalesce(max(course_id), 0) + 1\n" +
				"from mst_course),\n" +
				SQLUtil.convertForSQL(this.getCourseName()) + ",\n" +
				SQLUtil.convertForSQL(this.getNum()) + ",\n" +
				SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
				SQLUtil.convertForSQL(this.getOperationTime()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_course\n" +
				"where delete_date is null\n" +
				"and course_class_id = " +
				SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_course\n" +
				"where delete_date is null\n" +
				"and course_class_id = " +
				SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()) + "), 0) + 1 end,\n" +
                                SQLUtil.convertForSQL(this.getPraiseTimeLimit()) + ",\n" +
                                SQLUtil.convertForSQL(this.isPraiseTime()) + ",\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_course\n" +
				"set\n" +
				"course_class_id = " + SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()) + ",\n" +
				"course_name = " + SQLUtil.convertForSQL(this.getCourseName()) + ",\n" +
                                "num = " + SQLUtil.convertForSQL(this.getNum()) + ",\n" +
				"price = " + SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
				"operation_time = " + SQLUtil.convertForSQL(this.getOperationTime()) + ",\n" +
                                "praise_time_limit = " + SQLUtil.convertForSQL(this.getPraiseTimeLimit()) + ",\n" +
                                "is_praise_time = " + SQLUtil.convertForSQL(this.isPraiseTime()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_course\n" +
				"where delete_date is null\n" +
				"and course_class_id = " +
				SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()) + "\n" +
				"and course_id != " +
				SQLUtil.convertForSQL(this.getCourseID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_course\n" +
				"where delete_date is null\n" +
				"and course_class_id = " +
				SQLUtil.convertForSQL(this.getCourseClass().getCourseClassID()) + "\n" +
				"and course_id != " +
				SQLUtil.convertForSQL(this.getCourseID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	course_id = " + SQLUtil.convertForSQL(this.getCourseID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_course\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	course_id = " + SQLUtil.convertForSQL(this.getCourseID()) + "\n";
	}
	
	
	private static String getMaxCourseIDSQL()
	{
		return	"select max(course_id) as max_id\n" +
				"from mst_course\n";
	}
}

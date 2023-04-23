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
 * コース分類マスタデータ
 * @author furukawa
 */
public class MstCourseClass extends ArrayList<MstCourse>
{
	/**
	 * コース分類ＩＤ
	 */
	protected	Integer		courseClassID		=	null;
	/**
	 * コース分類名
	 */
	protected	String		courseClassName	=	"";
	/**
	 * コース分類名略称
	 */
	protected	String		courseClassContractedName	=	"";
	/**
	 * 表示順
	 */
	protected	Integer		displaySeq			=	null;
	
	/**
	 * コンストラクタ
	 */
	public MstCourseClass()
	{
	}
	/**
	 * コンストラクタ
	 * @param technicClassID コース分類ＩＤ
	 */
	public MstCourseClass(Integer courseClassID)
	{
		this.setCourseClassID(courseClassID);
	}
	
	/**
	 * 文字列に変換する。（コース分類名）
	 * @return コース分類名
	 */
	public String toString()
	{
		return	this.getCourseClassName();
	}

	/**
	 * コース分類ＩＤを取得する。
	 * @return コース分類ＩＤ
	 */
	public Integer getCourseClassID()
	{
		return courseClassID;
	}

	/**
	 * コース分類ＩＤをセットする。
	 * @param technicClassID コース分類ＩＤ
	 */
	public void setCourseClassID(Integer courseClassID)
	{
		this.courseClassID = courseClassID;
	}

	/**
	 * コース分類名を取得する。
	 * @return コース分類名
	 */
	public String getCourseClassName()
	{
		return courseClassName;
	}

	/**
	 * コース分類名をセットする。
	 * @param courseClassName コース分類名
	 */
	public void setCourseClassName(String courseClassName)
	{
		this.courseClassName = courseClassName;
	}

	/**
	 * コース分類名略称を取得する。
	 * @return コース分類名略称
	 */
	public String getCourseClassContractedName()
	{
		if(courseClassContractedName == null){
			return "";
		}
		return courseClassContractedName;
	}

	/**
	 * コース分類名略称をセットする。
	 * @param technicClassName コース分類名略称
	 */
	public void setCourseClassContractedName(String courseClassContractedName)
	{
		this.courseClassContractedName = courseClassContractedName;
	}
	
	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * 表示順をセットする。
	 * @param displaySeq 表示順
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
	 * コース分類マスタデータからデータをセットする。
	 * @param mtc 技術分類マスタデータ
	 */
	public void setData(MstCourseClass mtc)
	{
		this.setCourseClassID(mtc.getCourseClassID());
		this.setCourseClassName(mtc.getCourseClassName());
		this.setCourseClassContractedName( mtc.getCourseClassContractedName() );
		this.setDisplaySeq(mtc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
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
	 * コース分類マスタにデータを登録する。
	 * @return true - 成功
	 * @param lastSeq 表示順の最大値
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
	 * コース分類マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * コース分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_course_class\n"
			+	"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_course_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	course_class_id = " + SQLUtil.convertForSQL(this.getCourseClassID()) + "\n";
	}
	
	/**
	 * コース分類マスタデータをArrayListに読み込む。
	 * @param technicClassID 技術分類ＩＤ
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
	 * コースマスタデータを全て取得するＳＱＬ文を取得する
	 * @param technicClassID コース分類ＩＤ
	 * @return コース分類マスタデータを全て取得するＳＱＬ文
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

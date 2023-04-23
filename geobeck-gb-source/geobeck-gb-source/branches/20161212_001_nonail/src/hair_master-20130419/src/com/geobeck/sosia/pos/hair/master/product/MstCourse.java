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
 * コースマスタデータ
 * @author katagiri
 */
public class MstCourse
{
	/**
	 * コース分類
	 */
	protected	MstCourseClass	courseClass	=	new MstCourseClass();
	/**
	 * コースＩＤ
	 */
	protected	Integer			courseID		=	null;
	/**
	 * コース名
	 */
	protected	String			courseName		=	"";
        /**
         * 消化回数
         */
        protected       Integer                 num    = null;
	/**
	 * 単価
	 */
	protected	Long			price			=	null;
	/**
	 * 施術時間
	 */
	protected	Integer			operationTime	=	null;
	/**
	 * 表示順
	 */
	protected	Integer			displaySeq		=	null;
        /**
         * 有効期限
         */
        protected       Integer                 praiseTimeLimit =   new Integer(0);
        /**
         * 有効期限使用有無
         */
        protected       Boolean                 isPraiseTime    =   false;

        /** Creates a new instance of MstCourse */
	public MstCourse()
	{
	}
        
	/**
	 * コース分類を取得する。
	 * @return コース分類
	 */
	public MstCourseClass getCourseClass()
	{
		return courseClass;
	}

	/**
	 * コース分類をセットする。
	 * @param courseClass コース分類
	 */
	public void setCourseClass(MstCourseClass courseClass)
	{
		this.courseClass = courseClass;
	}

	/**
	 * コースＩＤを取得する。
	 * @return コースＩＤ
	 */
	public Integer getCourseID()
	{
		return courseID;
	}

	/**
	 * コースＩＤをセットする。
	 * @param courseID コースＩＤ
	 */
	public void setCourseID(Integer courseID)
	{
		this.courseID = courseID;
	}

	/**
	 * コース名を取得する。
	 * @return コース名
	 */
	public String getCourseName()
	{
		return courseName;
	}

	/**
	 * コース名をセットする。
	 * @param courseName コース名
	 */
	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}

        /**
         * 消化回数を取得する
         * @return 消化回数
         */
        public Integer getNum(){
            return num;
        }

        /**
         * 消化回数をセットする
         * @param consumptionCount 消化回数
         */
        public void setNum(Integer num){
            this.num = num;
        }

	/**
	 * 単価を取得する。
	 * @return 単価
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * 単価をセットする。
	 * @param price 単価
	 */
	public void setPrice(Long price)
	{
		this.price = price;
	}

	/**
	 * 施術時間を取得する。
	 * @return 施術時間
	 */
	public Integer getOperationTime()
	{
		return operationTime;
	}

	/**
	 * 施術時間をセットする。
	 * @param operationTime 施術時間
	 */
	public void setOperationTime(Integer operationTime)
	{
		this.operationTime = operationTime;
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

        /**
         * 有効期限をセットする
         * @param praiseTime
         */
        public void setPraiseTimeLimit(Integer praiseTime){
            this.praiseTimeLimit = praiseTime;
        }
        /**
         * 有効期限を取得する
         * @param praiseTime
         */
        public Integer getPraiseTimeLimit(){
            return this.praiseTimeLimit;
        }

        /**
         * 有効期限使用有無をセットする
         * @param b
         */
        public void setPraiseTime(Boolean b){
            this.isPraiseTime = b;
        }
        /**
         * 有効期限使用有無を取得する
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
	 * 技術マスタデータからデータをセットする。
	 * @param mt 技術マスタデータ
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
	 * ResultSetWrapperからデータをセットする
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
	 * 技術マスタにデータを登録する。
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
	 * 技術マスタからデータを削除する。（論理削除）
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
	 * 技術マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * 技術マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_course\n" +
				"where	course_id = " + SQLUtil.convertForSQL(this.getCourseID()) + "\n";
	}

        	/**
	 * Select文を取得する。
	 * @return Select文
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
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
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

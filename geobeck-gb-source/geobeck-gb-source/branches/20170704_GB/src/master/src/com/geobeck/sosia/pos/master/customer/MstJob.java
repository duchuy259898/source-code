/*
 * MstJob.java
 *
 * Created on 2006/04/24, 19:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.customer;

import com.geobeck.sosia.pos.master.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 職業マスタデータ
 * @author katagiri
 */
public class MstJob
{
	/**
	 * 職業ＩＤの最小値
	 */
	public static int		JOB_ID_MIN				=	1;
	/**
	 * 職業ＩＤの最大値
	 */
	public static int		JOB_ID_MAX				=	Integer.MAX_VALUE;
	/**
	 * 職業名の文字数の最大値
	 */
	public static int		JOB_NAME_MAX			=	20;
	/**
	 * 表示順の最小値
	 */
	public static int		DISPLAY_SEQ_MIN			=	0;
	/**
	 * 表示順の最大値
	 */
	public static int		DISPLAY_SEQ_MAX			=	9999;
	
	/**
	 * 職業ＩＤ
	 */
	private Integer		jobID		=	null;
	/**
	 * 職業名
	 */
	private	String		jobName		=	null;
	/**
	 * 表示順
	 */
	private Integer		displaySeq	=	null;
	
	/**
	 * コンストラクタ
	 */
	public MstJob()
	{
	}
	
	/**
	 * 文字列に変換する。（職業名）
	 * @return 職業名
	 */
	public String toString()
	{
		return jobName;
	}

	/**
	 * 職業ＩＤを取得する。
	 * @return 職業ＩＤ
	 */
	public Integer getJobID()
	{
		return jobID;
	}

	/**
	 * 職業ＩＤをセットする。
	 * @param jobID 職業ＩＤ
	 */
	public void setJobID(Integer jobID)
	{
		this.jobID = jobID;
	}

	/**
	 * 職業名を取得する。
	 * @return 職業名
	 */
	public String getJobName()
	{
		return jobName;
	}

	/**
	 * 職業名をセットする。
	 * @param jobName 職業名
	 */
	public void setJobName(String jobName)
	{
		if(jobName == null || jobName.length() <= MstJob.JOB_NAME_MAX)
		{
			this.jobName	=	jobName;
		}
		else
		{
			this.jobName	=	jobName.substring(0, MstJob.JOB_NAME_MAX);
		}
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
		if(displaySeq == null	||
				displaySeq < MstJob.DISPLAY_SEQ_MIN ||
				MstJob.DISPLAY_SEQ_MAX < displaySeq)
		{
			this.displaySeq	=	null;
		}
		else
		{
			this.displaySeq	=	displaySeq;
		}
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setJobID(null);
		this.setJobName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setJobID(rs.getInt("job_id"));
		this.setJobName(rs.getString("job_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * 職業マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getUpdateSQL();
		}
		else
		{
			sql	=	this.getInsertSQL();
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
	 * 職業マスタからデータを削除する。（論理削除）
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
	 * 職業マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getJobID() == null || this.getJobID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 職業マスタ取得用のＳＱＬ文を取得する。
	 * @return 職業マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_job\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, job_name\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_job\n"
			+	"where	job_id = " + SQLUtil.convertForSQL(this.getJobID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_job\n"
			+	"(job_id, job_name, display_seq,\n"
			+	"insert_date, update_date, delete_date)\n"
			+	"select\n"
			+	"coalesce(max(job_id), 0) + 1,\n"
			+	SQLUtil.convertForSQL(this.getJobName()) + ",\n"
			+	SQLUtil.convertForSQL(this.getDisplaySeq()) + ",\n"
			+	"current_timestamp, current_timestamp, null\n"
			+	"from mst_job\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_job\n"
			+	"set\n"
			+	"job_name = " + SQLUtil.convertForSQL(this.getJobName()) + ",\n"
			+	"display_seq = " + SQLUtil.convertForSQL(this.getDisplaySeq()) + ",\n"
			+	"update_date = current_timestamp\n"
			+	"where	job_id = " + SQLUtil.convertForSQL(this.getJobID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_job\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	job_id = " + SQLUtil.convertForSQL(this.getJobID()) + "\n";
	}
}

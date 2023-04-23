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
 * �E�ƃ}�X�^�f�[�^
 * @author katagiri
 */
public class MstJob
{
	/**
	 * �E�Ƃh�c�̍ŏ��l
	 */
	public static int		JOB_ID_MIN				=	1;
	/**
	 * �E�Ƃh�c�̍ő�l
	 */
	public static int		JOB_ID_MAX				=	Integer.MAX_VALUE;
	/**
	 * �E�Ɩ��̕������̍ő�l
	 */
	public static int		JOB_NAME_MAX			=	20;
	/**
	 * �\�����̍ŏ��l
	 */
	public static int		DISPLAY_SEQ_MIN			=	0;
	/**
	 * �\�����̍ő�l
	 */
	public static int		DISPLAY_SEQ_MAX			=	9999;
	
	/**
	 * �E�Ƃh�c
	 */
	private Integer		jobID		=	null;
	/**
	 * �E�Ɩ�
	 */
	private	String		jobName		=	null;
	/**
	 * �\����
	 */
	private Integer		displaySeq	=	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstJob()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�E�Ɩ��j
	 * @return �E�Ɩ�
	 */
	public String toString()
	{
		return jobName;
	}

	/**
	 * �E�Ƃh�c���擾����B
	 * @return �E�Ƃh�c
	 */
	public Integer getJobID()
	{
		return jobID;
	}

	/**
	 * �E�Ƃh�c���Z�b�g����B
	 * @param jobID �E�Ƃh�c
	 */
	public void setJobID(Integer jobID)
	{
		this.jobID = jobID;
	}

	/**
	 * �E�Ɩ����擾����B
	 * @return �E�Ɩ�
	 */
	public String getJobName()
	{
		return jobName;
	}

	/**
	 * �E�Ɩ����Z�b�g����B
	 * @param jobName �E�Ɩ�
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
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setJobID(null);
		this.setJobName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
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
	 * �E�ƃ}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �E�ƃ}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �E�ƃ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
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
	 * �E�ƃ}�X�^�擾�p�̂r�p�k�����擾����B
	 * @return �E�ƃ}�X�^�擾�p�̂r�p�k��
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_job\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, job_name\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_job\n"
			+	"where	job_id = " + SQLUtil.convertForSQL(this.getJobID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
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
	 * Update�����擾����B
	 * @return Update��
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
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_job\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	job_id = " + SQLUtil.convertForSQL(this.getJobID()) + "\n";
	}
}

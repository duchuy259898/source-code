/*
 * MstFirstComingMotive.java
 *
 * Created on 2008/07/13, 15:00
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
 * ���񗈓X���@�}�X�^�f�[�^
 * @author saito
 */
public class MstFirstComingMotive
{
	/**
	 * ��ʂh�c�̍ŏ��l
	 */
	public static int		FIRST_COMING_MOTIVE_CLASS_ID_MIN		=	1;
	/**
	 * ��ʂh�c�̍ő�l
	 */
	public static int		FIRST_COMING_MOTIVE_CLASS_ID_MAX		=	Integer.MAX_VALUE;
	/**
	 * ��ʖ��̕������̍ő�l
	 */
	public static int		FIRST_COMING_MOTIVE_NAME_MAX			=	30;
	/**
	 * �\�����̍ŏ��l
	 */
	public static int		DISPLAY_SEQ_MIN                                 =	0;
	/**
	 * �\�����̍ő�l
	 */
	public static int		DISPLAY_SEQ_MAX                                 =	9999;
	
	/**
	 * ��ʂh�c
	 */
	private Integer		firstComingMotiveClassId		=	null;
	/**
	 * ��ʖ�
	 */
	private	String		firstComingMotiveName                   =	null;
	/**
	 * �\����
	 */
	private Integer		displaySeq                              =	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstFirstComingMotive()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i��ʖ��j
	 * @return ��ʖ�
	 */
	public String toString()
	{
		return firstComingMotiveName;
	}

	/**
	 * ��ʂh�c���擾����B
	 * @return ��ʂh�c
	 */
	public Integer getFirstComingMotiveClassId()
	{
		return firstComingMotiveClassId;
	}

	/**
	* ��ʂh�c���Z�b�g����B
	* @param firstComingMotiveClassId ��ʂh�c
	*/
	public void setFirstComingMotiveClassId(Integer FirstComingMotiveClassId)
	{
		this.firstComingMotiveClassId = FirstComingMotiveClassId;
	}

	/**
	 * ��ʖ����擾����B
	 * @return ��ʖ�
	 */
	public String getFirstComingMotiveName()
	{
		return firstComingMotiveName;
	}

	/**
	* ��ʖ����Z�b�g����B
	* @param firstComingMotiveName ��ʖ�
	*/
	public void setFirstComingMotiveName(String FirstComingMotiveName)
	{
		if(FirstComingMotiveName == null || FirstComingMotiveName.length() <= MstFirstComingMotive.FIRST_COMING_MOTIVE_NAME_MAX)
		{
			this.firstComingMotiveName	=	FirstComingMotiveName;
		}
		else
		{
			this.firstComingMotiveName	=	FirstComingMotiveName.substring(0, MstFirstComingMotive.FIRST_COMING_MOTIVE_NAME_MAX);
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
				displaySeq < MstFirstComingMotive.DISPLAY_SEQ_MIN ||
				MstFirstComingMotive.DISPLAY_SEQ_MAX < displaySeq)
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
		this.setFirstComingMotiveClassId(null);
		this.setFirstComingMotiveName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setFirstComingMotiveClassId(rs.getInt("first_coming_motive_class_id"));
		this.setFirstComingMotiveName(rs.getString("first_coming_motive_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	/**
	 * ���񗈓X���@�}�X�^�Ƀf�[�^��o�^����B
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
	 * ���񗈓X���@�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * ���񗈓X���@�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getFirstComingMotiveClassId() == null || this.getFirstComingMotiveClassId() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
        public static List<MstFirstComingMotive> getAll(ConnectionWrapper con){
            List<MstFirstComingMotive> list = new ArrayList<MstFirstComingMotive>();
            try {
                ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());
                while(rs.next()){
                    MstFirstComingMotive obj = new MstFirstComingMotive();
                    obj.setData(rs);
                    list.add(obj);
                } 
            }catch(Exception e){
                e.printStackTrace();
                System.err.println("SQL = \n" + getSelectAllSQL());
            }
            return list;
        }
        
	/**
	 * ���񗈓X���@�}�X�^�擾�p�̂r�p�k�����擾����B
	 * @return ���񗈓X���@�}�X�^�擾�p�̂r�p�k��
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_first_coming_motive\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, first_coming_motive_name\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_first_coming_motive\n"
			+	"where	first_coming_motive_class_id = " + SQLUtil.convertForSQL(this.getFirstComingMotiveClassId()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_first_coming_motive\n"
			+	"(first_coming_motive_class_id, first_coming_motive_name, display_seq,\n"
			+	"insert_date, update_date, delete_date)\n"
			+	"select\n"
			+	"coalesce(max(first_coming_motive_class_id), 0) + 1,\n"
			+	SQLUtil.convertForSQL(this.getFirstComingMotiveName()) + ",\n"
			+	SQLUtil.convertForSQL(this.getDisplaySeq()) + ",\n"
			+	"current_timestamp, current_timestamp, null\n"
			+	"from mst_first_coming_motive\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_first_coming_motive\n"
			+	"set\n"
			+	"first_coming_motive_name = " + SQLUtil.convertForSQL(this.getFirstComingMotiveName()) + ",\n"
			+	"display_seq = " + SQLUtil.convertForSQL(this.getDisplaySeq()) + ",\n"
			+	"update_date = current_timestamp\n"
			+	"where	first_coming_motive_class_id = " + SQLUtil.convertForSQL(this.getFirstComingMotiveClassId()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_first_coming_motive\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	first_coming_motive_class_id = " + SQLUtil.convertForSQL(this.getFirstComingMotiveClassId()) + "\n";
	}
}

/*
 * MstResponse.java
 *
 * Created on 2007/08/29, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author kanemoto
 */
public class MstResponse {
	protected	Integer		responseID		=	null;	/* ���X�|���XID */
	protected	String		responseName		=	"";	/* ���X�|���X�� */
	protected	Integer		circulationType		=	null;	/* ���s�^�C�v 1:�X�^�b�t�z�z�^ 2:�t���[�y�[�p�[�^*/
	protected	Integer		displaySeq		=	null;	/* �\���� */
        protected String responseNo;

    public String getResponseNo() {
        return responseNo;
    }

    public void setResponseNo(String responseNo) {
        this.responseNo = responseNo;
    }
        protected       Integer   responseClassID = null;
	
	/** Creates a new instance of MstResponse */
	public MstResponse() {
	}
	
	/**
	 * ���X�|���X�����擾����
	 */
	public String toString()
	{
		return this.getResponseName();
	}
	
	/**
	 * ���X�|���XID���擾����B
	 * @return ���X�|���XID
	 */
	public Integer getResponseID()
	{
		return this.responseID;
	}

	/**
	 * ���X�|���XID���Z�b�g����B
	 * @param Integer ���X�|���XID
	 */
	public void setResponseID( Integer responseID )
	{
		this.responseID = responseID;
	}
	
	/**
	 * ���X�|���X�����擾����B
	 * @return ���X�|���X��
	 */
	public String getResponseName()
	{
		return this.responseName;
	}

	/**
	 * ���X�|���X�����Z�b�g����B
	 * @param String ���X�|���X��
	 */
	public void setResponseName( String responseName )
	{
		this.responseName = responseName;
	}
	
	/**
	 * ���s�^�C�v���擾����B
	 * @return ���s�^�C�v
	 */
	public Integer getCirculationType()
	{
		return this.circulationType;
	}

	/**
	 * ���s�^�C�v���Z�b�g����B
	 * @param circulationType ���s�^�C�v
	 */
	public void setCirculationType( Integer circulationType )
	{
		this.circulationType = circulationType;
	}
	
	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return this.displaySeq;
	}

	/**
	 * �\�������Z�b�g����B
	 * @param Integer �\����
	 */
	public void setDisplaySeq( Integer displaySeq )
	{
		this.displaySeq = displaySeq;
	}
	
        /**
	 * ���X�|���XID���擾����B
	 * @return ���X�|���XID
	 */
	public Integer getResponseClassID()
	{
		return this.responseClassID;
	}

	/**
	 * ���X�|���XID���Z�b�g����B
	 * @param Integer ���X�|���XID
	 */
	public void setResponseClassID( Integer responseClassID )
	{
		this.responseClassID = responseClassID;
	}
        
	/**
	 * �w��̃��X�|���X�����ꉻ���`�F�b�N����
	 * @return ����̃��X�|���X����True��Ԃ�
	 */
	public boolean equals(Object o)
	{
		if( o instanceof MstResponse )
		{
			MstResponse	mr	=	(MstResponse)o;
			
			if( mr.getResponseID() == this.responseID &&
					mr.getResponseName().equals( this.responseName ) &&
					mr.getCirculationType() == this.circulationType &&
					mr.getDisplaySeq() == displaySeq )
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * MstResponse����f�[�^���Z�b�g����B
	 * @param mr MstResponse
	 */
	public void setData( MstResponse mr )
	{
		this.setResponseID( mr.getResponseID() );
		this.setResponseName( mr.getResponseName() );
		this.setCirculationType( mr.getCirculationType() );
		this.setDisplaySeq( mr.getDisplaySeq() );
                this.setResponseNo(mr.getResponseNo());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData( ResultSetWrapper rs ) throws SQLException
	{
		this.setResponseID( rs.getInt( "response_id" ) );
		this.setResponseName( rs.getString( "response_name" ) );
		this.setCirculationType( rs.getInt( "circulation_type" ) );
		this.setDisplaySeq( rs.getInt( "display_seq" ) );
                this.setResponseNo(rs.getString( "response_no" ) );
                
	}
	
	/**
	 * ���X�|���X�}�X�^�Ƀf�[�^��o�^����B
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
	 * �Z�p���ރ}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �Z�p���ރ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if( this.getResponseID() == null || this.getResponseID() < 1 )	return	false;
		if( con == null )	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
        public void load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		rs.next();
                setData(rs);
	}
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_response\n" +
			"where\n" +
			"response_id = " + SQLUtil.convertForSQL( this.getResponseID() ) + "\n" +
			";\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����B
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_technic_class\n" +
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
		return
			"insert into mst_response\n" +
			"(response_id, response_name, circulation_type, display_seq,\n" +
			"insert_date, update_date, delete_date)\n" +
			"select\n" +
			"coalesce(max(response_id), 0) + 1,\n" +
			SQLUtil.convertForSQL( this.getResponseName() ) + ",\n" +
			SQLUtil.convertForSQL( this.getCirculationType() ) + ",\n" +
			"case\n" +
			"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
			" between 0 and coalesce(max(display_seq), 0) then " +
			SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
			"else coalesce((select max(display_seq)\n" +
			"from mst_response\n" +
			"where delete_date is null), 0) + 1 end,\n" +
			"current_timestamp, current_timestamp, null\n" +
			"from mst_response\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return
			"update mst_response\n" +
			"set\n" +
			"response_name = " + SQLUtil.convertForSQL( this.getResponseName() ) + ",\n" +
			"circulation_type = " + SQLUtil.convertForSQL( this.getCirculationType() ) + ",\n" +
			"display_seq = case\n" +
			"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
			" between 0 and coalesce((select max(display_seq)\n" +
			"from mst_response\n" +
			"where delete_date is null\n" +
			"and response_id != " +
			SQLUtil.convertForSQL( this.getResponseID() ) + "), 0) then " +
			SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
			"else coalesce((select max(display_seq)\n" +
			"from mst_response\n" +
			"where delete_date is null\n" +
			"and response_id != " +
			SQLUtil.convertForSQL( this.getResponseID() ) + "), 0) + 1 end,\n" +
			"update_date = current_timestamp\n" +
			"where	response_id = " + SQLUtil.convertForSQL( this.getResponseID() ) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return
			"update mst_response\n" +
			"set\n" +
			"update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where	response_id = " + SQLUtil.convertForSQL( this.getResponseID() ) + "\n";
	}
}

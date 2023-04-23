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
	protected	Integer		responseID		=	null;	/* レスポンスID */
	protected	String		responseName		=	"";	/* レスポンス名 */
	protected	Integer		circulationType		=	null;	/* 発行タイプ 1:スタッフ配布型 2:フリーペーパー型*/
	protected	Integer		displaySeq		=	null;	/* 表示順 */
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
	 * レスポンス名を取得する
	 */
	public String toString()
	{
		return this.getResponseName();
	}
	
	/**
	 * レスポンスIDを取得する。
	 * @return レスポンスID
	 */
	public Integer getResponseID()
	{
		return this.responseID;
	}

	/**
	 * レスポンスIDをセットする。
	 * @param Integer レスポンスID
	 */
	public void setResponseID( Integer responseID )
	{
		this.responseID = responseID;
	}
	
	/**
	 * レスポンス名を取得する。
	 * @return レスポンス名
	 */
	public String getResponseName()
	{
		return this.responseName;
	}

	/**
	 * レスポンス名をセットする。
	 * @param String レスポンス名
	 */
	public void setResponseName( String responseName )
	{
		this.responseName = responseName;
	}
	
	/**
	 * 発行タイプを取得する。
	 * @return 発行タイプ
	 */
	public Integer getCirculationType()
	{
		return this.circulationType;
	}

	/**
	 * 発行タイプをセットする。
	 * @param circulationType 発行タイプ
	 */
	public void setCirculationType( Integer circulationType )
	{
		this.circulationType = circulationType;
	}
	
	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return this.displaySeq;
	}

	/**
	 * 表示順をセットする。
	 * @param Integer 表示順
	 */
	public void setDisplaySeq( Integer displaySeq )
	{
		this.displaySeq = displaySeq;
	}
	
        /**
	 * レスポンスIDを取得する。
	 * @return レスポンスID
	 */
	public Integer getResponseClassID()
	{
		return this.responseClassID;
	}

	/**
	 * レスポンスIDをセットする。
	 * @param Integer レスポンスID
	 */
	public void setResponseClassID( Integer responseClassID )
	{
		this.responseClassID = responseClassID;
	}
        
	/**
	 * 指定のレスポンスが同一化をチェックする
	 * @return 同一のレスポンス時にTrueを返す
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
	 * MstResponseからデータをセットする。
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
	 * ResultSetWrapperからデータをセットする。
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
	 * レスポンスマスタにデータを登録する。
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
	 * 技術分類マスタからデータを削除する。（論理削除）
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
	 * 技術分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
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
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
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

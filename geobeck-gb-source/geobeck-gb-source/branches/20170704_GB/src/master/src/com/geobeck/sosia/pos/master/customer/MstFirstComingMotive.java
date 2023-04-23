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
 * 初回来店動機マスタデータ
 * @author saito
 */
public class MstFirstComingMotive
{
	/**
	 * 種別ＩＤの最小値
	 */
	public static int		FIRST_COMING_MOTIVE_CLASS_ID_MIN		=	1;
	/**
	 * 種別ＩＤの最大値
	 */
	public static int		FIRST_COMING_MOTIVE_CLASS_ID_MAX		=	Integer.MAX_VALUE;
	/**
	 * 種別名の文字数の最大値
	 */
	public static int		FIRST_COMING_MOTIVE_NAME_MAX			=	30;
	/**
	 * 表示順の最小値
	 */
	public static int		DISPLAY_SEQ_MIN                                 =	0;
	/**
	 * 表示順の最大値
	 */
	public static int		DISPLAY_SEQ_MAX                                 =	9999;
	
	/**
	 * 種別ＩＤ
	 */
	private Integer		firstComingMotiveClassId		=	null;
	/**
	 * 種別名
	 */
	private	String		firstComingMotiveName                   =	null;
	/**
	 * 表示順
	 */
	private Integer		displaySeq                              =	null;
	
	/**
	 * コンストラクタ
	 */
	public MstFirstComingMotive()
	{
	}
	
	/**
	 * 文字列に変換する。（種別名）
	 * @return 種別名
	 */
	public String toString()
	{
		return firstComingMotiveName;
	}

	/**
	 * 種別ＩＤを取得する。
	 * @return 種別ＩＤ
	 */
	public Integer getFirstComingMotiveClassId()
	{
		return firstComingMotiveClassId;
	}

	/**
	* 種別ＩＤをセットする。
	* @param firstComingMotiveClassId 種別ＩＤ
	*/
	public void setFirstComingMotiveClassId(Integer FirstComingMotiveClassId)
	{
		this.firstComingMotiveClassId = FirstComingMotiveClassId;
	}

	/**
	 * 種別名を取得する。
	 * @return 種別名
	 */
	public String getFirstComingMotiveName()
	{
		return firstComingMotiveName;
	}

	/**
	* 種別名をセットする。
	* @param firstComingMotiveName 種別名
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
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setFirstComingMotiveClassId(null);
		this.setFirstComingMotiveName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
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
	 * 初回来店動機マスタにデータを登録する。
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
	 * 初回来店動機マスタからデータを削除する。（論理削除）
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
	 * 初回来店動機マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * 初回来店動機マスタ取得用のＳＱＬ文を取得する。
	 * @return 初回来店動機マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_first_coming_motive\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, first_coming_motive_name\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_first_coming_motive\n"
			+	"where	first_coming_motive_class_id = " + SQLUtil.convertForSQL(this.getFirstComingMotiveClassId()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_first_coming_motive\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	first_coming_motive_class_id = " + SQLUtil.convertForSQL(this.getFirstComingMotiveClassId()) + "\n";
	}
}

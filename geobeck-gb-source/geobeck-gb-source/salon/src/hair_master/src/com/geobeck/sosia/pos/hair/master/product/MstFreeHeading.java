/*
 * MstFreeHeading.java
 *
 * Created on 2007/08/17, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author kanemoto
 */
public class MstFreeHeading {
	protected MstFreeHeadingClass freeHeadingClass = new MstFreeHeadingClass();  /** フリー項目分類 */
	protected Integer	      freeHeadingID    = null;				/** フリー項目ＩＤ */
	protected String	      freeHeadingName  = "";				 /** フリー項目名 */
	protected Integer	      displaySeq       = null;				/** 表示順 */
        protected String            freeHeadingText    =  "";

	/** Creates a new instance of MstFreeHeading */
	public MstFreeHeading()
	{
	}
        //An start add 20130405
        public String getFreeHeadingText() {
            return freeHeadingText;
        }

        public void setFreeHeadingText(String freeHeadingText) {
            this.freeHeadingText = freeHeadingText;
        }

	//An end add 20130401

	/**
	 * フリー項目分類を取得する。
	 * @return フリー項目分類
	 */
	public MstFreeHeadingClass getFreeHeadingClass()
	{
		return freeHeadingClass;
	}

	/**
	 * フリー項目分類をセットする。
	 * @param freeHeadingClass フリー項目分類
	 */
	public void setFreeHeadingClass(MstFreeHeadingClass freeHeadingClass)
	{
		this.freeHeadingClass = freeHeadingClass;
	}

	/**
	 * フリー項目ＩＤを取得する。
	 * @return フリー項目ＩＤ
	 */
	public Integer getFreeHeadingID()
	{
		return freeHeadingID;
	}

	/**
	 * フリー項目ＩＤをセットする。
	 * @param freeHeadingID フリー項目ＩＤ
	 */
	public void setFreeHeadingID(Integer freeHeadingID)
	{
		this.freeHeadingID = freeHeadingID;
	}

	/**
	 * フリー項目名を取得する。
	 * @return フリー項目名
	 */
	public String getFreeHeadingName()
	{
		return freeHeadingName;
	}

	/**
	 * フリー項目名をセットする。
	 * @param freeHeadingName フリー項目名
	 */
	public void setFreeHeadingName(String freeHeadingName)
	{
		this.freeHeadingName = freeHeadingName;
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

	public String toString()
	{
		return	this.getFreeHeadingName();
	}

	/**
	 * フリー項目マスタデータからデータをセットする。
	 * @param mt フリー項目マスタデータ
	 */
	public void setData(MstFreeHeading mt)
	{
		this.setFreeHeadingClass(new MstFreeHeadingClass(mt.getFreeHeadingClass().getFreeHeadingClassID()));
		this.setFreeHeadingID(mt.getFreeHeadingID());
		this.setFreeHeadingName(mt.getFreeHeadingName());
		this.setDisplaySeq(mt.getDisplaySeq());
	}

	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setFreeHeadingClass(new MstFreeHeadingClass(rs.getInt("free_heading_class_id")));
		this.setFreeHeadingID(rs.getInt("free_heading_id"));
		this.setFreeHeadingName(rs.getString("free_heading_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}




	/**
	 * データを登録する。
	 * @param con ConnectionWrapper
	 * @param data 対象となるデータ
	 * @param dataIndex データのインデックス
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//存在しないデータの場合
		if(!this.isExist(con))
		{
			//表示順が入力されている場合
			if(0 <= this.getDisplaySeq())
			{
				//入力された表示順以降のデータの表示順を１加算する
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			//データをInsertする
			if(con.executeUpdate(this.getInsertDataSQL()) != 1)
			{
				return	false;
			}
		}
		//存在するデータの場合
		else
		{
			Integer		lastSeq		=	this.getLastDisplaySeq(con);

			//表示順が変更されている場合
			if(this.getDisplaySeq() != lastSeq)
			{
				//元の表示順以降のデータの表示順を１減算する
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				//表示順が入力されている場合
				if(0 <= this.getDisplaySeq())
				{
					//入力された表示順以降のデータの表示順を１加算する
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			//データをUpdateする
			if(con.executeUpdate(this.getUpdateDataSQL()) != 1)
			{
				return	false;
			}
		}

		return	true;
	}

	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		boolean		result		=	false;

		if(this.getFreeHeadingClass() == null ||
				this.getFreeHeadingClass().getFreeHeadingClassID() == null ||
				this.getFreeHeadingID() == null)
		{
			return	result;
		}

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			result	=	true;
		}

		rs.close();

		return	result;
	}

	private Integer getLastDisplaySeq(ConnectionWrapper con) throws SQLException
	{
		Integer		result	=	-1;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			result	=	rs.getInt("display_seq");
		}

		rs.close();

		return	result;
	}

	/**
	 * データを削除する。
	 * @param con ConnectionWrapper
	 * @param data 対象となるデータ
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}

		if(con.executeUpdate(this.getDeleteDataSQL()) != 1)
		{
			return	false;
		}

		return	true;
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_free_heading\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id = " + SQLUtil.convertForSQL(this.getFreeHeadingID()) + "\n";
	}

	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_free_heading\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}

	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertDataSQL()
	{
		return	"insert into mst_free_heading\n" +
				"(free_heading_class_id, free_heading_id, free_heading_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + ",\n" +
				"coalesce(max(free_heading_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_free_heading\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n";
	}

	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateDataSQL()
	{
		return	"update mst_free_heading\n" +
				"set\n" +
				"free_heading_name = " + SQLUtil.convertForSQL(this.getFreeHeadingName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id != " +
				SQLUtil.convertForSQL(this.getFreeHeadingID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_free_heading\n" +
				"where delete_date is null\n" +
				"and free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id != " +
				SQLUtil.convertForSQL(this.getFreeHeadingID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id = " + SQLUtil.convertForSQL(this.getFreeHeadingID()) + "\n";
	}

	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteDataSQL()
	{
		return	"update mst_free_heading\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClass().getFreeHeadingClassID()) + "\n" +
				"and free_heading_id = " + SQLUtil.convertForSQL(this.getFreeHeadingID()) + "\n";
	}
}

/*
 * MstFreeHeadingClass.java
 *
 * Created on 2007/08/17, 16:33
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
 * フリー項目分類クラス
 * @author kanemoto
 */
public class MstFreeHeadingClass extends ArrayList<MstFreeHeading> {
    public static final	int			FREE_HEADING_TYPE_NUM		=	4;

    
    protected Integer  freeHeadingClassID = null;
    protected String   freeHeadingClassName = null;
    protected boolean  useFlg = false;
    private Integer  input_type = null;
    // GB Start add 20161117 #58629
    protected boolean reserveUseFlg = false;
    // GB End add 20161117 #58629
    /** Creates a new instance of MstFreeHeadingClass */
    public MstFreeHeadingClass() {
    }
    
    /**
     * コンストラクタ
     * @param freeHeadingClassID フリー項目分類ＩＤ
     */
    public MstFreeHeadingClass(Integer freeHeadingClassID)
    {
	    this.setFreeHeadingClassID(freeHeadingClassID);
    }
    
    /**
     * フリー項目分類IDを取得する
     */
    public Integer getFreeHeadingClassID()
    {
	return this.freeHeadingClassID;
    }
    
    /**
     * フリー項目分類IDをセットする
     */
    public void setFreeHeadingClassID( Integer freeHeadingClassID )
    {
	this.freeHeadingClassID = freeHeadingClassID;
    }
    
    /**
     * フリー項目分類名を取得する
     */
    public String getFreeHeadingClassName()
    {
	return this.freeHeadingClassName;
    }
    
    /**
     * フリー項目分類名をセットする
     */
    public void setFreeHeadingClassName( String freeHeadingClassName )
    {
	this.freeHeadingClassName = freeHeadingClassName;
    }
    /**
     * フリー項目分類使用フラグを取得する
     */
    public boolean getUseFlg()
    {
	return this.useFlg;
    }
    
    /**
     * フリー項目分類IDをセットする
     */
    public void setUseFlg( boolean useFlg )
    {
	this.useFlg = useFlg;
    }
    
    // GB Start add 20161117 #58629
    /**
     * フリー項目分類　予約画面での使用フラグを取得する
     */
    public boolean getReserveUseFlg()
    {
        return this.reserveUseFlg;
    }
    
    /**
     * フリー項目分類　予約画面での使用フラグをセットする
     */
    public void setReserveUseFlg(boolean reserveUseFlg)
    {
        this.reserveUseFlg = reserveUseFlg;
    }
    // GB End add 20161117 #58629

    /**
     * フリー項目分類マスタデータからデータをセットする。
     * @param mtc フリー項目分類マスタデータ
     */
    public void setData( MstFreeHeadingClass mfhc )
    {
	    this.setFreeHeadingClassID(mfhc.getFreeHeadingClassID());
	    this.setFreeHeadingClassName(mfhc.getFreeHeadingClassName());
	    this.setUseFlg(mfhc.getUseFlg());
            // IVS SANG START 20131103 [gbソース]ソースマージ
            this.setInput_type(mfhc.getInput_type());
            // IVS SANG END 20131103 [gbソース]ソースマージ
            // GB Start add 20161117 #58629
            this.setReserveUseFlg(mfhc.getReserveUseFlg());
            // GB End add 20161117 #58629
    }

    /**
     * ResultSetWrapperからデータをセットする
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
	    this.setFreeHeadingClassID(rs.getInt("free_heading_class_id"));
	    this.setFreeHeadingClassName(rs.getString("free_heading_class_name"));
	    this.setUseFlg(rs.getBoolean("use_type"));
            // IVS SANG START 20131103 [gbソース]ソースマージ
            this.setInput_type(rs.getInt("input_type"));
            // IVS SANG END 20131103 [gbソース]ソースマージ
            // GB Start add 20161117 #58629
            this.setReserveUseFlg(!rs.getBoolean("reserve_use_type")? false : rs.getBoolean("reserve_use_type"));
            // GB End add 20161117 #58629
    }

	/**
	 * フリー項目分類マスタにデータを登録する。
	 * @return true - 成功
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con ) throws SQLException
	{
		if(isExists(con))
		{
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		return	true;
	}
	
	
	/**
	 * フリー項目分類マスタからデータを削除する。（論理削除）
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
	 * フリー項目分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getFreeHeadingClassID() == null || this.getFreeHeadingClassID() < 1)	return	false;
		
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
			+	"from mst_free_heading_class\n"
			+	"where	free_heading_class_id = " + SQLUtil.convertForSQL( this.getFreeHeadingClassID() ) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
            // GB Start edit 20161117 #58629
            // mst_free_heading_classテーブルにreserve_use_typeカラム追加
            /* return	"insert into mst_free_heading_class\n" +
				"(free_heading_class_id, free_heading_class_name, use_type,\n" +
				"insert_date, update_date, delete_date,input_type)\n" +
				"select\n" +
				"coalesce(max(free_heading_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getUseFlg()) + ",\n" +
				"current_timestamp, current_timestamp, null\n" +
                                // IVS SANG START 20131103 [gbソース]ソースマージ
                                SQLUtil.convertForSQL(this.getInput_type()) + "\n" +
                                // IVS SANG END 20131103 [gbソース]ソースマージ
				"from mst_free_heading_class\n"; */
            return	"insert into mst_free_heading_class\n" +
				"(free_heading_class_id, free_heading_class_name, use_type,\n" +
				"insert_date, update_date, delete_date, input_type, reserve_use_type)\n" +
				"select\n" +
				"coalesce(max(free_heading_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getUseFlg()) + ",\n" +
				"current_timestamp, current_timestamp, null,\n" +
                                SQLUtil.convertForSQL(this.getInput_type()) + ",\n" +
                                SQLUtil.convertForSQL(this.getReserveUseFlg()) + "\n" +
				"from mst_free_heading_class\n";
            // GB End edit 20161117 #58629
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{	
            // GB Start edit 20161117 #58629
            // mst_free_heading_classテーブルにreserve_use_typeカラム追加
            /* return	"update mst_free_heading_class\n" +
				"set\n" +
				"free_heading_class_name = " + SQLUtil.convertForSQL(this.getFreeHeadingClassName()) + ",\n" +
				"use_type = " + SQLUtil.convertForSQL(this.getUseFlg()) + ",\n" +
				"update_date = current_timestamp,\n" +
                                // IVS SANG START 20131103 [gbソース]ソースマージ
                                " input_type = " + SQLUtil.convertForSQL(this.getInput_type()) + "\n" +
                                // IVS SANG START 20131103 [gbソース]ソースマージ
				"where	free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClassID()) + "\n"; */
            return	"update mst_free_heading_class\n" +
				"set\n" +
				"free_heading_class_name = " + SQLUtil.convertForSQL(this.getFreeHeadingClassName()) + ",\n" +
				"use_type = " + SQLUtil.convertForSQL(this.getUseFlg()) + ",\n" +
				"update_date = current_timestamp,\n" +
                                "input_type = " + SQLUtil.convertForSQL(this.getInput_type()) + ",\n" +
                                "reserve_use_type = " + SQLUtil.convertForSQL(this.getReserveUseFlg()) + "\n" + 
				"where	free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClassID()) + "\n";
            // GB End edit 20161117 #58629
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_free_heading_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClassID()) + "\n";
	}
	
	/**
	 * 技術マスタデータをArrayListに読み込む。
	 * @param technicClassID フリー項目分類ＩＤ
	 */
	public void loadFreeHeading(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectFreeHeadingSQL());

		while(rs.next())
		{
			MstFreeHeading	mt	=	new	MstFreeHeading();
			mt.setData(rs);
			this.add(mt);
		}

		rs.close();
	}
	
	/**
	 * 技術マスタデータを全て取得するＳＱＬ文を取得する
	 * @param technicClassID フリー項目分類ＩＤ
	 * @return 技術マスタデータを全て取得するＳＱＬ文
	 */
	public String getSelectFreeHeadingSQL()
	{
		return	"select *\n" +
			"from mst_free_heading\n" +
			"where delete_date is null\n" +
					"and free_heading_class_id = " +
					SQLUtil.convertForSQL(this.getFreeHeadingClassID()) + "\n" +
			"order by display_seq\n";
	}
        // IVS SANG START 20131103 [gbソース]ソースマージ
         /**
     * @return the input_type
     */
    public Integer getInput_type() {
        return input_type;
    }

    /**
     * @param input_type the input_type to set
     */
    public void setInput_type(Integer input_type) {
        this.input_type = input_type;
    }
    // IVS SANG END 20131103 [gbソース]ソースマージ
}

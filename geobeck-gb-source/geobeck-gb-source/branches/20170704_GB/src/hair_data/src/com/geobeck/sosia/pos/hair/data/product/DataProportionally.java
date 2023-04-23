/*
 * DataProportionally.java
 *
 * Created on 2006/05/26, 14:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.product;

import com.geobeck.sosia.pos.products.*;
import com.geobeck.sosia.pos.hair.master.product.*;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * 按分データ
 * @author katagiri
 */
public class DataProportionally
{
	protected Integer		dataProportionallyID	= null;						/** 按分データID */
	protected MstTechnic		technic			= null;			/** 技術         */
	protected MstProportionally	proportionally		= null;  /** 按分         */
	protected Integer		ratio			= null;						/** 割合         */
	
	/**
	 * コンストラクタ
	 */
	public DataProportionally()
	{
	}
	
	/**
	 * 按分データIDを取得する
	 * @return 按分データID
	 */
	public Integer getDataProportionallyID()
	{
		return dataProportionallyID;
	}
	
	/**
	 * 按分データIDをセットする。
	 * @param dataProportionallyID 按分データID
	 */
	public void setDataProportionallyID( Integer dataProportionallyID )
	{
		this.dataProportionallyID = dataProportionallyID;
	}
	
	/**
	 * 技術を取得する。
	 * @return 技術
	 */
	public MstTechnic getTechnic()
	{
		return technic;
	}
	
	/**
	 * 技術をセットする。
	 * @param technic 技術
	 */
	public void setTechnic(MstTechnic technic)
	{
		this.technic = technic;
	}
	
	/**
	 * 按分を取得する。
	 * @return 按分
	 */
	public MstProportionally getProportionally()
	{
		return proportionally;
	}
	
	/**
	 * 按分をセットする。
	 * @param proportionally 按分
	 */
	public void setProportionally(MstProportionally proportionally)
	{
		this.proportionally = proportionally;
	}
	
	/**
	 * 割合を取得する。
	 * @return 割合
	 */
	public Integer getRatio()
	{
		return ratio;
	}

	/**
	 * 割合をセットする。
	 * @param slipNo 割合
	 */
	public void setRatio(Integer ratio)
	{
		if( ratio == null ) ratio = 0;
		this.ratio = ratio;
	}
	
	/**
	 * DataProportionallyからデータをセットする
	 * @param dp DataProportionally
	 */
	public void setData( DataProportionally dp )
	{
	    // 按分データID
	    this.setDataProportionallyID( dp.getDataProportionallyID() );
	    // 技術データ
	    this.setTechnic( dp.getTechnic() );
	    // 按分データ
	    this.setProportionally( dp.getProportionally() );
	    // 割合データ
	    this.setRatio( dp.getRatio() );
	}
	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException 例外
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		// 按分データID
		this.setDataProportionallyID( rs.getInt("data_proportionally_id") );
		// 技術データ
		MstTechnicClass		mtc	= new MstTechnicClass();
		MstTechnic			mt	= new MstTechnic();
		mtc.setTechnicClassID(   rs.getInt("technic_class_id") );
		mtc.setTechnicClassName( rs.getString("technic_class_name") );
		mtc.setDisplaySeq(       rs.getInt("technic_class_display_seq") );
		mt.setTechnicClass(      mtc );
		mt.setTechnicID(         rs.getInt("technic_id") );
		mt.setTechnicNo(         rs.getString("technic_no") );
		mt.setTechnicName(       rs.getString("technic_name") );
		mt.setPrice(             rs.getLong("price") );
		mt.setOperationTime(     rs.getInt("operation_time") );
		mt.setDisplaySeq(        rs.getInt("technic_display_seq") );
		this.setTechnic( mt );
		// 按分データ
		MstProportionally mp = new MstProportionally();
		mp.setProportionallyID(    rs.getInt("proportionally_id") );
		mp.setProportionallyName(  rs.getString("proportionally_name") );
		mp.setProportionallyPoint( rs.getInt("proportionally_point") );
		mp.setDisplaySeq(          rs.getInt("proportionally_display_seq") );
		this.setProportionally( mp );
		// 割合データ
		this.setRatio( rs.getInt( "proportionally_ratio" ) );
	}
	
	/**
	 * 按分データを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean add(ConnectionWrapper con) throws SQLException
	{
	    // SQLの実行を行う
	    if( con.executeUpdate( this.getInsertSQL() ) == 1 ) return	true;
	    return	false;
	}
	
	/**
	 * 按分データを更新する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean update(ConnectionWrapper con) throws SQLException
	{
	    // SQLの実行を行う
	    if( con.executeUpdate( this.getUpdateSQL() ) == 1 ) return	true;
	    return	false;
	}
	
	/**
	 * 按分データを削除する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
	    // SQLの実行を行う
	    if( con.executeUpdate( this.getDeleteSQL() ) == 1 ) return	true;
	    return	false;
	}
	
	
	/**
	 * 按分データをInsertするＳＱＬ文を取得する。
	 * 
	 * insert into data_proportionally
	 * ( data_proportionally_id, technic_id, proportionally_id, proportionally_ratio, insert_date, update_date, delete_date)
	 * values(
	 * 	( select max( data_proportionally_id ) from data_proportionally ) + 1,
	 * 	技術ID,
	 * 	按分ID, 
	 * 	割り振り,
	 * 	current_timestamp, current_timestamp, null
	 * );
	 * 
	 * @return 按分データをInsertするＳＱＬ文
	 */
	public String getInsertSQL()
	{
		return
			"insert into data_proportionally\n" +
			"( data_proportionally_id, technic_id, proportionally_id, proportionally_ratio, insert_date, update_date, delete_date)\n" +
			"values(\n" +
			"( select coalesce(max( data_proportionally_id ), 0) from data_proportionally ) + 1,\n" +
			SQLUtil.convertForSQL( this.technic.getTechnicID() ) + ",\n" +
			SQLUtil.convertForSQL( this.proportionally.getProportionallyID() ) + ", \n" +
			SQLUtil.convertForSQL( this.getRatio() ) + ",\n" +
			"current_timestamp, current_timestamp, null\n" +
			");\n";
	}
	
	/**
	 * 按分データをUpdateするＳＱＬ文を取得する。
	 * update data_proportionally
	 * set proportionally_id = 按分ID, 
	 * 	proportionally_ratio = 割り振り
	 * where
	 * 	data_proportionally_id = データ按分ID;
	 * 
	 * @return 按分データをUpdateするＳＱＬ文
	 * 
	 */
	public String getUpdateSQL()
	{
	    return
		"update data_proportionally\n" +
		"set proportionally_id = " + SQLUtil.convertForSQL( this.proportionally.getProportionallyID() ) + ", \n" +
		"proportionally_ratio = " + SQLUtil.convertForSQL( this.getRatio() ) + ", \n" +
		"update_date = current_timestamp, \n" +
		"delete_date = null \n" +
		"where\n" +
		"data_proportionally_id = " + SQLUtil.convertForSQL( this.getDataProportionallyID() ) + ";\n";
	}
	
	/**
	 * 按分データをDeleteするＳＱＬ文を取得する。
	 * 
	 * update data_proportionally
	 * set delete_date = current_timestamp
	 * where data_proportionally_id = 按分データID;
	 * 
	 * @return 按分データを削除するＳＱＬ文
	 * 
	 */
	public String getDeleteSQL()
	{
		return
			"update data_proportionally\n" +
			"set delete_date = current_timestamp\n" +
			"where data_proportionally_id = " + SQLUtil.convertForSQL( this.getDataProportionallyID() ) + ";\n";
	}
	
}

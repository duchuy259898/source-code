/*
 * DataReservationProportionally.java
 *
 * Created on 2007/10/11, 12:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.reservation;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.master.product.*;

/**
 *
 * @author kanemoto
 */
public class DataReservationProportionally {
	/**
	 * 予約詳細
	 */
	protected	DataReservationDetail	reservationDetail		=	null;
	/**
	 * 按分
	 */
	protected	DataProportionally		proportionally			=	new DataProportionally();
	/**
	 * スタッフ
	 */
	protected	MstStaff			staff				=	new MstStaff();
	
	/**
	 * 指名
	 */
	protected	boolean				designated			= false;
	
	/** Creates a new instance of DataReservationProportionally */
	public DataReservationProportionally() {
	}
	
	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		if(reservationDetail != null)	return reservationDetail.getShop();
		else							return	null;
	}
	
	/**
	 * 予約Noを取得する
	 */
	public Integer getReservationNo()
	{
		if(reservationDetail != null)
		{
			return reservationDetail.getReservationNo();
		}
		else
		{
			return	null;
		}
	}
	
	/**
	 * 予約No.を取得する。
	 * @return 予約No.
	 */
	public Integer getReservationDetailNo()
	{
		if(reservationDetail != null)
		{
			return reservationDetail.getReservationDetailNo();
		}
		else
		{
			return	null;
		}
	}
	
	/**
	 * 予約詳細を取得する。
	 * @return 予約詳細
	 */
	public DataReservationDetail getReservationDetail()
	{
		return reservationDetail;
	}

	/**
	 * 予約詳細をセットする。
	 * @param reservationDetail 予約詳細
	 */
	public void setReservationDetail(DataReservationDetail reservationDetail)
	{
		this.reservationDetail = reservationDetail;
	}
	
	/**
	 * 技術を取得する
	 * @return 技術
	 */
	public MstTechnic getTechnic()
	{
		if(reservationDetail != null)	return reservationDetail.getTechnic();
		else							return	null;
	}
	
	/**
	 * 技術IDを取得する
	 * @return 技術ID
	 */
	public Integer getTechnicID()
	{
		if( reservationDetail != null ) {
			if( reservationDetail.getTechnic() != null ) return reservationDetail.getTechnic().getTechnicID();
		}
		return null;
	}
	
	/**
	 * 按分を取得する。
	 * @return 按分
	 */
	public DataProportionally getProportionally()
	{
		return proportionally;
	}

	/**
	 * 按分IDを取得する。
	 * @return 按分ID
	 */
	public Integer getDataProportionallyID()
	{
		if( proportionally != null ) return proportionally.getDataProportionallyID();
		return null;
	}

	/**
	 * 按分をセットする。
	 * @param technic 按分
	 */
	public void setProportionally(DataProportionally proportionally)
	{
		this.proportionally = proportionally;
	}

	/**
	 * スタッフを取得する。
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフをセットする。
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	/**
	 * 指名フラグを取得する
	 * @return designated 指名 true:指名 false:フリー
	 */
	public boolean getDesignated()
	{
		return designated;
	}
	
	/**
	 * 指名フラグをセットする
	 * @param designated 指名フラグ
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * 予約按分データをセットする。
	 * @param dr 予約按分データ
	 */
	public void set( DataReservationProportionally drp )
	{
		this.setReservationDetail(drp.getReservationDetail());
		this.setProportionally( drp.getProportionally() );
		this.setStaff(drp.getStaff());
		this.setDesignated( drp.getDesignated() );
	}
	
	/**
	 * データを登録する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
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
	 * データを削除する。（論理削除）
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
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
	 * データが存在するかチェックする。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(
			this.getReservationNo() == null ||
			this.getReservationDetailNo() == null ||
			this.getTechnicID() == null ||
			this.getDataProportionallyID() == null
		) return false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	
	/**
	 * Select文を取得する。
	 * @return 
	 */
	private String getSelectSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"data_reservation_proportionally\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			"and reservation_detail_no = " + SQLUtil.convertForSQL( this.getReservationDetailNo() ) + "\n" +
			"and data_proportionally_id = " + SQLUtil.convertForSQL( this.getDataProportionallyID() ) + "\n" +
			";\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return 
	 */
	private String	getInsertSQL()
	{
		return
			"insert into data_reservation_proportionally\n" +
			"( shop_id, reservation_no, reservation_detail_no, \n" +
			"data_proportionally_id, designated_flag, staff_id,\n" +
			"insert_date, update_date, delete_date)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getShop().getShopID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getReservationNo() ) + ",\n" +
			SQLUtil.convertForSQL( this.getReservationDetailNo() ) + ",\n" +
			SQLUtil.convertForSQL(this.getDataProportionallyID()) + ",\n" +
			SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
			( this.getStaff() != null ? SQLUtil.convertForSQL( this.getStaff().getStaffID() ) : "null" ) + ",\n" +
			"current_timestamp, current_timestamp, null);\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return 
	 */
	private String	getUpdateSQL()
	{
		return
			"update data_reservation_proportionally\n" +
			"set\n" + 
			"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ", \n" +
			"staff_id = " + ( this.getStaff() != null ? SQLUtil.convertForSQL( this.getStaff().getStaffID() ) : "null" ) + ", \n" +
			"update_date = current_timestamp, \n" +
			"delete_date = null\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			"and reservation_detail_no = " + SQLUtil.convertForSQL( this.getReservationDetailNo() ) + "\n" +
			"and data_proportionally_id = " + SQLUtil.convertForSQL( this.getDataProportionallyID() ) + "\n" +
			";\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 
	 */
	private String	getDeleteSQL()
	{
		return
			"update data_reservation_detail\n" +
			"set\n" +
			"update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			"and reservation_detail_no = " + SQLUtil.convertForSQL( this.getReservationDetailNo() ) + "\n" +
			"and data_proportionally_id = " + SQLUtil.convertForSQL( this.getDataProportionallyID() ) + "\n" +
			";\n";
	}	
}

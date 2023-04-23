/*
 * MstStaffTechnicTime.java
 *
 * Created on 2007/09/15, 13:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.sql.*;

/**
 *
 * @author kanemoto
 */
public class MstStaffTechnicTime {
	
	private		MstTechnic		technic			=	null;		/** 技術マスタ */
	private		MstStaff		staff			=	null;		/** スタッフマスタ */
	private		Integer			operationTime	=	null;		/** スタッフ施術時間 */

	/** Creates a new instance of MstStaffTechnicTime */
	public MstStaffTechnicTime() {
	}
	
	/**
	 * 技術を取得する
	 */
	public MstTechnic getTechnic()
	{
		return this.technic;
	}
	/**
	 * 技術を登録する
	 */
	public void setTechnic( MstTechnic technic )
	{
		this.technic = technic;
	}
	
	/**
	 * スタッフを取得する
	 */
	public MstStaff getStaff()
	{
		return this.staff;
	}
	/**
	 * スタッフを登録する
	 */
	public void setStaff( MstStaff staff )
	{
		this.staff = staff;
	}
	
	/**
	 * 施術時間を取得する
	 */
	public Integer getOperationTime()
	{
		return this.operationTime;
	}
	
	/**
	 * 基本施術時間を登録する
	 */
	public void setDefaultOperationTime()
	{
		this.setOperationTime( this.getTechnic().getOperationTime() );
	}
	
	/**
	 * 施術時間を登録する
	 */
	public void setOperationTime( Integer operationTime )
	{
		this.operationTime = operationTime;
	}
	
	/**
	 * MstStaffTechnicTimeからデータをセットする
	 */
	public void setData( MstStaffTechnicTime mstt )
	{
		this.setTechnic( mstt.getTechnic() );
		this.setStaff( mstt.getStaff() );
		this.setOperationTime( mstt.getOperationTime() );
	}
	
	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData( ResultSetWrapper rs ) throws SQLException
	{
		MstTechnicClass mtc = new MstTechnicClass();
		MstTechnic		mt		=	new	MstTechnic();
		MstStaff		ms		=	new	MstStaff();
		
		mtc.setData( rs );
		mt.setData( rs );
		mt.setTechnicClass( mtc );
		this.setTechnic( mt );
		ms.setData( rs );
		this.setStaff( ms );
		this.setOperationTime( rs.getInt( "staff_operation_time" ) );
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
		String sql;
		
		// データの有無により処理分け
		sql = ( !this.isExist( con ) ) ? this.getInsertDataSQL() : this.getUpdateDataSQL();
		
		// SQLを実行する
		if( con.executeUpdate( sql ) == 1 ) return true;
		return false;
	}
	
	public boolean isExist( ConnectionWrapper con ) throws SQLException
	{
		boolean		result		=	false;
		
		if(	this.getTechnic() == null ||
			this.getStaff() == null ||
			this.getOperationTime() == null	)
		{
			return	result;
		}
		
		ResultSetWrapper	rs	=	con.executeQuery( this.getSelectSQL() );
		
		if(rs.next())
		{
			result	=	true;
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
		if( !isExist( con ) ) return true;
		
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
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_staff_technic_time\n" +
			"where\n" +
			"technic_id = " + SQLUtil.convertForSQL( this.getTechnic().getTechnicID() ) + "\n" +
			"and staff_id = " + SQLUtil.convertForSQL( this.getStaff().getStaffID() ) + "\n" +
			";\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertDataSQL()
	{
		return
			"insert into mst_staff_technic_time\n" +
			"(technic_id, staff_id, operation_time, insert_date, update_date, delete_date)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getTechnic().getTechnicID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getStaff().getStaffID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getOperationTime() ) + ",\n" +
			"current_timestamp, current_timestamp, null );\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateDataSQL()
	{
		return
			"update mst_staff_technic_time\n" +
			"set\n" +
			"operation_time = " + SQLUtil.convertForSQL( this.getOperationTime() ) + ", \n" +
			"update_date = current_timestamp, \n" +
			"delete_date = null\n" +
			"where\n" +
			"technic_id = " + SQLUtil.convertForSQL( this.getTechnic().getTechnicID() ) + "\n" +
			"and staff_id = " + SQLUtil.convertForSQL( this.getStaff().getStaffID() ) + "\n" +
			";\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteDataSQL()
	{
		return
			"update mst_staff_technic_time\n" +
			"set\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"technic_id = " + SQLUtil.convertForSQL( this.getTechnic().getTechnicID() ) + "\n" +
			"and staff_id = " + SQLUtil.convertForSQL( this.getStaff().getStaffID() ) + "\n" +
			";\n";
	}
}

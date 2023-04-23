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
	
	private		MstTechnic		technic			=	null;		/** �Z�p�}�X�^ */
	private		MstStaff		staff			=	null;		/** �X�^�b�t�}�X�^ */
	private		Integer			operationTime	=	null;		/** �X�^�b�t�{�p���� */

	/** Creates a new instance of MstStaffTechnicTime */
	public MstStaffTechnicTime() {
	}
	
	/**
	 * �Z�p���擾����
	 */
	public MstTechnic getTechnic()
	{
		return this.technic;
	}
	/**
	 * �Z�p��o�^����
	 */
	public void setTechnic( MstTechnic technic )
	{
		this.technic = technic;
	}
	
	/**
	 * �X�^�b�t���擾����
	 */
	public MstStaff getStaff()
	{
		return this.staff;
	}
	/**
	 * �X�^�b�t��o�^����
	 */
	public void setStaff( MstStaff staff )
	{
		this.staff = staff;
	}
	
	/**
	 * �{�p���Ԃ��擾����
	 */
	public Integer getOperationTime()
	{
		return this.operationTime;
	}
	
	/**
	 * ��{�{�p���Ԃ�o�^����
	 */
	public void setDefaultOperationTime()
	{
		this.setOperationTime( this.getTechnic().getOperationTime() );
	}
	
	/**
	 * �{�p���Ԃ�o�^����
	 */
	public void setOperationTime( Integer operationTime )
	{
		this.operationTime = operationTime;
	}
	
	/**
	 * MstStaffTechnicTime����f�[�^���Z�b�g����
	 */
	public void setData( MstStaffTechnicTime mstt )
	{
		this.setTechnic( mstt.getTechnic() );
		this.setStaff( mstt.getStaff() );
		this.setOperationTime( mstt.getOperationTime() );
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
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
	 * �f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @param data �ΏۂƂȂ�f�[�^
	 * @param dataIndex �f�[�^�̃C���f�b�N�X
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String sql;
		
		// �f�[�^�̗L���ɂ�菈������
		sql = ( !this.isExist( con ) ) ? this.getInsertDataSQL() : this.getUpdateDataSQL();
		
		// SQL�����s����
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
	 * �f�[�^���폜����B
	 * @param con ConnectionWrapper
	 * @param data �ΏۂƂȂ�f�[�^
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * Select�����擾����B
	 * @return Select��
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
	 * Insert�����擾����B
	 * @return Insert��
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
	 * Update�����擾����B
	 * @return Update��
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
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
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

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
	 * �\��ڍ�
	 */
	protected	DataReservationDetail	reservationDetail		=	null;
	/**
	 * ��
	 */
	protected	DataProportionally		proportionally			=	new DataProportionally();
	/**
	 * �X�^�b�t
	 */
	protected	MstStaff			staff				=	new MstStaff();
	
	/**
	 * �w��
	 */
	protected	boolean				designated			= false;
	
	/** Creates a new instance of DataReservationProportionally */
	public DataReservationProportionally() {
	}
	
	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
		if(reservationDetail != null)	return reservationDetail.getShop();
		else							return	null;
	}
	
	/**
	 * �\��No���擾����
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
	 * �\��No.���擾����B
	 * @return �\��No.
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
	 * �\��ڍׂ��擾����B
	 * @return �\��ڍ�
	 */
	public DataReservationDetail getReservationDetail()
	{
		return reservationDetail;
	}

	/**
	 * �\��ڍׂ��Z�b�g����B
	 * @param reservationDetail �\��ڍ�
	 */
	public void setReservationDetail(DataReservationDetail reservationDetail)
	{
		this.reservationDetail = reservationDetail;
	}
	
	/**
	 * �Z�p���擾����
	 * @return �Z�p
	 */
	public MstTechnic getTechnic()
	{
		if(reservationDetail != null)	return reservationDetail.getTechnic();
		else							return	null;
	}
	
	/**
	 * �Z�pID���擾����
	 * @return �Z�pID
	 */
	public Integer getTechnicID()
	{
		if( reservationDetail != null ) {
			if( reservationDetail.getTechnic() != null ) return reservationDetail.getTechnic().getTechnicID();
		}
		return null;
	}
	
	/**
	 * �����擾����B
	 * @return ��
	 */
	public DataProportionally getProportionally()
	{
		return proportionally;
	}

	/**
	 * ��ID���擾����B
	 * @return ��ID
	 */
	public Integer getDataProportionallyID()
	{
		if( proportionally != null ) return proportionally.getDataProportionallyID();
		return null;
	}

	/**
	 * �����Z�b�g����B
	 * @param technic ��
	 */
	public void setProportionally(DataProportionally proportionally)
	{
		this.proportionally = proportionally;
	}

	/**
	 * �X�^�b�t���擾����B
	 * @return �X�^�b�t
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * �X�^�b�t���Z�b�g����B
	 * @param staff �X�^�b�t
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	/**
	 * �w���t���O���擾����
	 * @return designated �w�� true:�w�� false:�t���[
	 */
	public boolean getDesignated()
	{
		return designated;
	}
	
	/**
	 * �w���t���O���Z�b�g����
	 * @param designated �w���t���O
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * �\����f�[�^���Z�b�g����B
	 * @param dr �\����f�[�^
	 */
	public void set( DataReservationProportionally drp )
	{
		this.setReservationDetail(drp.getReservationDetail());
		this.setProportionally( drp.getProportionally() );
		this.setStaff(drp.getStaff());
		this.setDesignated( drp.getDesignated() );
	}
	
	/**
	 * �f�[�^��o�^����B
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
	 * �f�[�^���폜����B�i�_���폜�j
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
	 * �f�[�^�����݂��邩�`�F�b�N����B
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
	 * Select�����擾����B
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
	 * Insert�����擾����B
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
	 * Update�����擾����B
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
	 * �폜�pUpdate�����擾����B
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

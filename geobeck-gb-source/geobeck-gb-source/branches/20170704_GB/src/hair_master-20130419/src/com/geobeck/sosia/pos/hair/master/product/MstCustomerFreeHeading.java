/*
 * MstCustomerFreeHeading.java
 *
 * Created on 2007/08/20, 18:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;

/**
 *
 * @author kanemoto
 */
public class MstCustomerFreeHeading {
	protected	MstCustomer		mc	=	null;	/** �ڋq��� */
	protected	MstFreeHeading		mfh	=	null;	/** �t���[���� */ 

	/** Creates a new instance of MstCustomerFreeHeading */
	public MstCustomerFreeHeading() {
	}

	/**
	 * �ڋq���擾����B
	 * @return �ڋq
	 */
	public MstCustomer getMstCustomer()
	{
		return mc;
	}

	/**
	 * �ڋq���Z�b�g����B
	 * @param mc �ڋq
	 */
	public void setMstCustomer(MstCustomer mc)
	{
		this.mc = mc;
	}
    
	/**
	 * �t���[���ڋ敪���擾����B
	 * @return �t���[���ڋ敪
	 */
	public MstFreeHeading getMstFreeHeading()
	{
		return mfh;
	}

	/**
	 * �t���[���ڋ敪���Z�b�g����B
	 * @param mfh �t���[���ڋ敪
	 */
	public void setMstFreeHeading(MstFreeHeading mfh)
	{
		this.mfh = mfh;
	}
    
	/**
	 * �f�[�^���Z�b�g����
	 */
	public void setData( MstCustomerFreeHeading mcfh )
	{
	    this.setMstCustomer( this.getMstCustomer() );
	    this.setMstFreeHeading( this.getMstFreeHeading() );
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstFreeHeadingClass mfhc = new MstFreeHeadingClass();
		
		mc	=	new	MstCustomer();
		mfh	=	new	MstFreeHeading();
		this.getMstCustomer().setData( rs );
		mfhc.setData( rs );
		this.getMstFreeHeading().setData( rs );
		this.getMstFreeHeading().setFreeHeadingClass( mfhc );
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
		//���݂��Ȃ��f�[�^�̏ꍇ
		if(!this.isExist(con))
		{
			//�f�[�^��Insert����
			if(con.executeUpdate(this.getInsertDataSQL()) != 1)
			{
				return	false;
			}
		}
		//���݂���f�[�^�̏ꍇ
		else
		{
			//�f�[�^��Update����
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
		
		if(this.getMstCustomer() == null ||
				this.getMstFreeHeading() == null)
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
	
	/**
	 * �f�[�^���폜����B
	 * @param con ConnectionWrapper
	 * @param data �ΏۂƂȂ�f�[�^
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
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
			"mst_customer_free_heading\n" +
			"where\n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertDataSQL()
	{
		return
			"insert into mst_customer_free_heading\n" +
			"(customer_id, free_heading_class_id, free_heading_id, insert_date, \n" +
			"	update_date, delete_date)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingID() ) + ",\n" +
			"current_timestamp,current_timestamp, null);\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateDataSQL()
	{
		return
			"update mst_customer_free_heading\n" +
			"set free_heading_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingID() ) + ",\n" +
			"update_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteDataSQL()
	{
		return
			"update mst_customer_free_heading\n" +
			"set update_date = current_timestamp\n" +
			"delete_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
}

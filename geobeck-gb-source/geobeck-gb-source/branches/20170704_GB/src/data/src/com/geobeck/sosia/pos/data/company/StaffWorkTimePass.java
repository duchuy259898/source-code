/*
 * StaffWorkTimePass.java
 *
 * Created on 2007/08/10, 11:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.company;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author kanemoto
 */
public class StaffWorkTimePass {
	private		MstShop		shop		=	null;
	private		String		password	=	null;
	
	/** Creates a new instance of StaffWorkTimePass */
	public StaffWorkTimePass() {
	}
	
	/**
	 * �X�܂��擾����
	 * @return �X��
	 */
	public MstShop getShop()
	{
		return this.shop;
	}
	
	/**
	 * �X�܂�o�^����
	 * @param shop �X��
	 */
	 public void setShop( MstShop shop )
	 {
		this.shop = shop;
	}
	
	/**
	 * �p�X���[�h���擾����
	 * @return �p�X���[�h
	 */
	public String getPassword()
	{
		return this.password;
	}
	
	/**
	 * �p�X���[�h��o�^����
	 * @param shop �p�X���[�h
	 */
	 public void setPassword( String password )
	 {
		this.password = password;
	}
	
	/**
	 * �f�[�^���Z�b�g����
	 */
	public void setData( ConnectionWrapper con, MstShop shop ) throws SQLException
	{
		this.setShop( shop );
		this.setPassword( this.getPassword( con, this.getShop().getShopID() ) );
	}
	
	/**
	 * ���R�[�h�����݂��邩�𔻒肵�܂�
	* @param ConnectionWrapper con
	* @return ���R�[�h�����݂���ꍇ��True��Ԃ��܂�
	 */
	public boolean isExists( ConnectionWrapper con ) throws SQLException
	{
		boolean		result	=	false;
		
		if(con == null)	return	false;
		ResultSetWrapper	rs	=	con.executeQuery( this.getSelectExistsPasswordSQL() );

		if(rs.next()) result	=	true;
		rs.close();
		return	result;
	}
	
	/**
	* �p�X���[�h�������Ă��邩�𔻒肷��
	* @param ConnectionWrapper con
	* @return �p�X���[�h���������ꍇ��True��Ԃ��܂�
	*/
	public boolean isComparePassword( ConnectionWrapper con ) throws SQLException
	{
		boolean		result	=	false;
		
		if(con == null)	return	false;
		ResultSetWrapper	rs	=	con.executeQuery( this.getSelectComparePasswordSQL() );

		if(rs.next()) result	=	true;
		rs.close();
		return	result;
	}
	 
	/**
	 * �p�X���[�h���擾���܂�
	 */
	public String getPassword( ConnectionWrapper con, Integer shopID ) throws SQLException
	{
		String result = null;
		
		if( (con != null)&&(shopID !=null) )
		{
                    ResultSetWrapper rs = con.executeQuery( this.getSelectPasswordByShopIDSQL( shopID ) );
                    
                    // �f�[�^�����݂����
                    if ( rs.next() )
                    {
                        result = rs.getString( "work_time_password" );
                    }
                    rs.close();
		}

		return	result;
	}
	
	/**
	 * �p�X���[�h��ύX����
	 */
	public boolean updatePassword( ConnectionWrapper con, String oldPass, String newPass ) throws SQLException
	{
		String sql = "";

		// ���R�[�h�����݂���Ȃ�SQL���擾����
		if( isExists( con ) ) sql = this.getUpdatePasswordSQL( oldPass, newPass );
		else sql = this.getInsertPasswordSQL( newPass );
		
		if(con.executeUpdate(sql) == 1)
		{
			// �����p�X���[�h��ύX����
			this.setPassword( newPass );
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * �p�X���[�h���폜����
	 */
	public boolean deletePassword( ConnectionWrapper con ) throws SQLException
	{
		String sql = "";

		// ���R�[�h�����݂���Ȃ�SQL���擾����
		if( !isExists( con ) ) sql = this.getDeletePasswordSQL();
		else return false;
		
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
	 * ���R�[�h���݃`�F�b�N�pSQL���擾����
	 * @return ���R�[�h���݃`�F�b�N�pSQL
	 */
	public String getSelectExistsPasswordSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_work_time_password\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and delete_date is  null\n" +
			";\n";
	}
	
	/**
	 * �p�X���[�h�`�F�b�N�pSQL���擾����
	 * @return �p�X���[�h�`�F�b�N�pSQL
	 */
	public String getSelectComparePasswordSQL()
	{
		String retStr = "";
		
		retStr = 
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_work_time_password\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n";
		
		if( this.getPassword() == null ) retStr += "and work_time_password is null\n";
		else                                retStr += "and work_time_password = " + SQLUtil.convertForSQL( this.getPassword() ) + "\n";
		
		retStr += 
			"and delete_date is  null\n" +
			";\n";
		return retStr;
	}
	
	/**
	 * �X��ID����p�X���[�h�擾�pSQL���擾����
	 * @return �p�X���[�h
	 */
	public String getSelectPasswordByShopIDSQL( int shopID )
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_work_time_password\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( shopID ) + "\n" +
			"and delete_date is  null\n" +
			";\n";
	}
	
	/**
	 * �p�X���[�h�}���pSQL���擾����
	 * @return �}���pSQL
	 */
	public String getInsertPasswordSQL( String newPassword )
	{
	    return
		"insert into mst_work_time_password\n" +
		"(shop_id, work_time_password, insert_date, update_date, delete_date)\n" +
		"values(\n" +
		SQLUtil.convertForSQL( this.getShop().getShopID() ) + ",\n" +
		SQLUtil.convertForSQL( newPassword ) + ",\n" +
		"current_timestamp, current_timestamp, null );\n";
	}
	/**
	 * �p�X���[�h�ύX�pSQL���擾����
	 * @param  oldPassword ���p�X���[�h
	 * @param  newPassword �V�p�X���[�h
	 * @return �p�X���[�h�όٗpSQL
	 */
	public String getUpdatePasswordSQL( String oldPassword, String newPassword )
	{
		String retStr = "";
		
		retStr = 
			"update\n" +
			"mst_work_time_password\n" +
			"set work_time_password = " + SQLUtil.convertForSQL( newPassword ) + ", \n" +
			"update_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n";
		
		if( this.getPassword() == null ) retStr += "and work_time_password is null\n";
		else                                retStr += "and work_time_password = " + SQLUtil.convertForSQL( oldPassword ) + "\n";
		
		retStr +=
			"and delete_date is  null\n" +
			";\n";
		return retStr;
	}
	
	/**
	 * �p�X���[�h�폜�pSQL���擾����
	 * @return �p�X���[�h�폜�pSQL
	 */
	public String getDeletePasswordSQL()
	{
		return
			"update\n" +
			"mst_work_time_password\n" +
			"set update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL( this.getShop().getShopID() ) + "\n" +
			"and delete_date is  null\n" +
			";\n";
	}
}

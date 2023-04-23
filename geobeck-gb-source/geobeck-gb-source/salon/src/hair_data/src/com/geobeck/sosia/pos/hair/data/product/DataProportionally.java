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
 * ���f�[�^
 * @author katagiri
 */
public class DataProportionally
{
	protected Integer		dataProportionallyID	= null;						/** ���f�[�^ID */
	protected MstTechnic		technic			= null;			/** �Z�p         */
	protected MstProportionally	proportionally		= null;  /** ��         */
	protected Integer		ratio			= null;						/** ����         */
	
	/**
	 * �R���X�g���N�^
	 */
	public DataProportionally()
	{
	}
	
	/**
	 * ���f�[�^ID���擾����
	 * @return ���f�[�^ID
	 */
	public Integer getDataProportionallyID()
	{
		return dataProportionallyID;
	}
	
	/**
	 * ���f�[�^ID���Z�b�g����B
	 * @param dataProportionallyID ���f�[�^ID
	 */
	public void setDataProportionallyID( Integer dataProportionallyID )
	{
		this.dataProportionallyID = dataProportionallyID;
	}
	
	/**
	 * �Z�p���擾����B
	 * @return �Z�p
	 */
	public MstTechnic getTechnic()
	{
		return technic;
	}
	
	/**
	 * �Z�p���Z�b�g����B
	 * @param technic �Z�p
	 */
	public void setTechnic(MstTechnic technic)
	{
		this.technic = technic;
	}
	
	/**
	 * �����擾����B
	 * @return ��
	 */
	public MstProportionally getProportionally()
	{
		return proportionally;
	}
	
	/**
	 * �����Z�b�g����B
	 * @param proportionally ��
	 */
	public void setProportionally(MstProportionally proportionally)
	{
		this.proportionally = proportionally;
	}
	
	/**
	 * �������擾����B
	 * @return ����
	 */
	public Integer getRatio()
	{
		return ratio;
	}

	/**
	 * �������Z�b�g����B
	 * @param slipNo ����
	 */
	public void setRatio(Integer ratio)
	{
		if( ratio == null ) ratio = 0;
		this.ratio = ratio;
	}
	
	/**
	 * DataProportionally����f�[�^���Z�b�g����
	 * @param dp DataProportionally
	 */
	public void setData( DataProportionally dp )
	{
	    // ���f�[�^ID
	    this.setDataProportionallyID( dp.getDataProportionallyID() );
	    // �Z�p�f�[�^
	    this.setTechnic( dp.getTechnic() );
	    // ���f�[�^
	    this.setProportionally( dp.getProportionally() );
	    // �����f�[�^
	    this.setRatio( dp.getRatio() );
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException ��O
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		// ���f�[�^ID
		this.setDataProportionallyID( rs.getInt("data_proportionally_id") );
		// �Z�p�f�[�^
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
		// ���f�[�^
		MstProportionally mp = new MstProportionally();
		mp.setProportionallyID(    rs.getInt("proportionally_id") );
		mp.setProportionallyName(  rs.getString("proportionally_name") );
		mp.setProportionallyPoint( rs.getInt("proportionally_point") );
		mp.setDisplaySeq(          rs.getInt("proportionally_display_seq") );
		this.setProportionally( mp );
		// �����f�[�^
		this.setRatio( rs.getInt( "proportionally_ratio" ) );
	}
	
	/**
	 * ���f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean add(ConnectionWrapper con) throws SQLException
	{
	    // SQL�̎��s���s��
	    if( con.executeUpdate( this.getInsertSQL() ) == 1 ) return	true;
	    return	false;
	}
	
	/**
	 * ���f�[�^���X�V����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean update(ConnectionWrapper con) throws SQLException
	{
	    // SQL�̎��s���s��
	    if( con.executeUpdate( this.getUpdateSQL() ) == 1 ) return	true;
	    return	false;
	}
	
	/**
	 * ���f�[�^���폜����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
	    // SQL�̎��s���s��
	    if( con.executeUpdate( this.getDeleteSQL() ) == 1 ) return	true;
	    return	false;
	}
	
	
	/**
	 * ���f�[�^��Insert����r�p�k�����擾����B
	 * 
	 * insert into data_proportionally
	 * ( data_proportionally_id, technic_id, proportionally_id, proportionally_ratio, insert_date, update_date, delete_date)
	 * values(
	 * 	( select max( data_proportionally_id ) from data_proportionally ) + 1,
	 * 	�Z�pID,
	 * 	��ID, 
	 * 	����U��,
	 * 	current_timestamp, current_timestamp, null
	 * );
	 * 
	 * @return ���f�[�^��Insert����r�p�k��
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
	 * ���f�[�^��Update����r�p�k�����擾����B
	 * update data_proportionally
	 * set proportionally_id = ��ID, 
	 * 	proportionally_ratio = ����U��
	 * where
	 * 	data_proportionally_id = �f�[�^��ID;
	 * 
	 * @return ���f�[�^��Update����r�p�k��
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
	 * ���f�[�^��Delete����r�p�k�����擾����B
	 * 
	 * update data_proportionally
	 * set delete_date = current_timestamp
	 * where data_proportionally_id = ���f�[�^ID;
	 * 
	 * @return ���f�[�^���폜����r�p�k��
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

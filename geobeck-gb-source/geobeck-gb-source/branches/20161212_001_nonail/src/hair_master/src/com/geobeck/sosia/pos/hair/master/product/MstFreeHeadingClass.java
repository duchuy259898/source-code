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
 * �t���[���ڕ��ރN���X
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
     * �R���X�g���N�^
     * @param freeHeadingClassID �t���[���ڕ��ނh�c
     */
    public MstFreeHeadingClass(Integer freeHeadingClassID)
    {
	    this.setFreeHeadingClassID(freeHeadingClassID);
    }
    
    /**
     * �t���[���ڕ���ID���擾����
     */
    public Integer getFreeHeadingClassID()
    {
	return this.freeHeadingClassID;
    }
    
    /**
     * �t���[���ڕ���ID���Z�b�g����
     */
    public void setFreeHeadingClassID( Integer freeHeadingClassID )
    {
	this.freeHeadingClassID = freeHeadingClassID;
    }
    
    /**
     * �t���[���ڕ��ޖ����擾����
     */
    public String getFreeHeadingClassName()
    {
	return this.freeHeadingClassName;
    }
    
    /**
     * �t���[���ڕ��ޖ����Z�b�g����
     */
    public void setFreeHeadingClassName( String freeHeadingClassName )
    {
	this.freeHeadingClassName = freeHeadingClassName;
    }
    /**
     * �t���[���ڕ��ގg�p�t���O���擾����
     */
    public boolean getUseFlg()
    {
	return this.useFlg;
    }
    
    /**
     * �t���[���ڕ���ID���Z�b�g����
     */
    public void setUseFlg( boolean useFlg )
    {
	this.useFlg = useFlg;
    }
    
    // GB Start add 20161117 #58629
    /**
     * �t���[���ڕ��ށ@�\���ʂł̎g�p�t���O���擾����
     */
    public boolean getReserveUseFlg()
    {
        return this.reserveUseFlg;
    }
    
    /**
     * �t���[���ڕ��ށ@�\���ʂł̎g�p�t���O���Z�b�g����
     */
    public void setReserveUseFlg(boolean reserveUseFlg)
    {
        this.reserveUseFlg = reserveUseFlg;
    }
    // GB End add 20161117 #58629

    /**
     * �t���[���ڕ��ރ}�X�^�f�[�^����f�[�^���Z�b�g����B
     * @param mtc �t���[���ڕ��ރ}�X�^�f�[�^
     */
    public void setData( MstFreeHeadingClass mfhc )
    {
	    this.setFreeHeadingClassID(mfhc.getFreeHeadingClassID());
	    this.setFreeHeadingClassName(mfhc.getFreeHeadingClassName());
	    this.setUseFlg(mfhc.getUseFlg());
            // IVS SANG START 20131103 [gb�\�[�X]�\�[�X�}�[�W
            this.setInput_type(mfhc.getInput_type());
            // IVS SANG END 20131103 [gb�\�[�X]�\�[�X�}�[�W
            // GB Start add 20161117 #58629
            this.setReserveUseFlg(mfhc.getReserveUseFlg());
            // GB End add 20161117 #58629
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
	    this.setFreeHeadingClassID(rs.getInt("free_heading_class_id"));
	    this.setFreeHeadingClassName(rs.getString("free_heading_class_name"));
	    this.setUseFlg(rs.getBoolean("use_type"));
            // IVS SANG START 20131103 [gb�\�[�X]�\�[�X�}�[�W
            this.setInput_type(rs.getInt("input_type"));
            // IVS SANG END 20131103 [gb�\�[�X]�\�[�X�}�[�W
            // GB Start add 20161117 #58629
            this.setReserveUseFlg(!rs.getBoolean("reserve_use_type")? false : rs.getBoolean("reserve_use_type"));
            // GB End add 20161117 #58629
    }

	/**
	 * �t���[���ڕ��ރ}�X�^�Ƀf�[�^��o�^����B
	 * @return true - ����
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
	 * �t���[���ڕ��ރ}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �t���[���ڕ��ރ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
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
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_free_heading_class\n"
			+	"where	free_heading_class_id = " + SQLUtil.convertForSQL( this.getFreeHeadingClassID() ) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
            // GB Start edit 20161117 #58629
            // mst_free_heading_class�e�[�u����reserve_use_type�J�����ǉ�
            /* return	"insert into mst_free_heading_class\n" +
				"(free_heading_class_id, free_heading_class_name, use_type,\n" +
				"insert_date, update_date, delete_date,input_type)\n" +
				"select\n" +
				"coalesce(max(free_heading_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getFreeHeadingClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getUseFlg()) + ",\n" +
				"current_timestamp, current_timestamp, null\n" +
                                // IVS SANG START 20131103 [gb�\�[�X]�\�[�X�}�[�W
                                SQLUtil.convertForSQL(this.getInput_type()) + "\n" +
                                // IVS SANG END 20131103 [gb�\�[�X]�\�[�X�}�[�W
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
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{	
            // GB Start edit 20161117 #58629
            // mst_free_heading_class�e�[�u����reserve_use_type�J�����ǉ�
            /* return	"update mst_free_heading_class\n" +
				"set\n" +
				"free_heading_class_name = " + SQLUtil.convertForSQL(this.getFreeHeadingClassName()) + ",\n" +
				"use_type = " + SQLUtil.convertForSQL(this.getUseFlg()) + ",\n" +
				"update_date = current_timestamp,\n" +
                                // IVS SANG START 20131103 [gb�\�[�X]�\�[�X�}�[�W
                                " input_type = " + SQLUtil.convertForSQL(this.getInput_type()) + "\n" +
                                // IVS SANG START 20131103 [gb�\�[�X]�\�[�X�}�[�W
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
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_free_heading_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	free_heading_class_id = " + SQLUtil.convertForSQL(this.getFreeHeadingClassID()) + "\n";
	}
	
	/**
	 * �Z�p�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param technicClassID �t���[���ڕ��ނh�c
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
	 * �Z�p�}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @param technicClassID �t���[���ڕ��ނh�c
	 * @return �Z�p�}�X�^�f�[�^��S�Ď擾����r�p�k��
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
        // IVS SANG START 20131103 [gb�\�[�X]�\�[�X�}�[�W
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
    // IVS SANG END 20131103 [gb�\�[�X]�\�[�X�}�[�W
}

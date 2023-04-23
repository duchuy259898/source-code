/*
 * MstBed.java
 *
 * Created on 2006/05/24, 20:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �x�b�h�}�X�^�f�[�^
 * @author katagiri
 */
public class MstBed
{
	/**
	 * �X��
	 */
	protected	MstShop		shop		=	new MstShop();
	/**
	 * �x�b�h�h�c
	 */
	protected	Integer		bedID		=	null;
	/**
	 * �x�b�h��
	 */
	protected	String		bedName		=	"";
	/**
	 * ����
	 */
	protected	Integer		bedNum		=	null;
	/**
	 * �\����
	 */
	private Integer		displaySeq	=	null;
	
	/** Creates a new instance of MstBed */
	public MstBed()
	{
	}
	/**
	 * �R���X�g���N�^
	 * @param bedID �x�b�h�h�c
	 */
	public MstBed(Integer bedID)
	{
		this.setBedID(bedID);
	}
	
	/**
	 * ������ɕϊ�����B�i�x�b�h���j
	 * @return �x�b�h��
	 */
	public String toString()
	{
		return	this.getBedName();
	}

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * �X�܂��Z�b�g����B
	 * @param shop �X��
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * �x�b�h�h�c���擾����B
	 * @return �x�b�h�h�c
	 */
	public Integer getBedID()
	{
		return bedID;
	}

	/**
	 * �x�b�h�h�c���Z�b�g����B
	 * @param bedID �x�b�h�h�c
	 */
	public void setBedID(Integer bedID)
	{
		this.bedID = bedID;
	}

	/**
	 * �x�b�h�����擾����B
	 * @return �x�b�h��
	 */
	public String getBedName()
	{
		return bedName;
	}

	/**
	 * �x�b�h�����Z�b�g����B
	 * @param bedName �x�b�h��
	 */
	public void setBedName(String bedName)
	{
		this.bedName = bedName;
	}

	/**
	 * �������擾����B
	 * @return ����
	 */
	public Integer getBedNum()
	{
		return bedNum;
	}

	/**
	 * �������Z�b�g����B
	 * @param bedNum ����
	 */
	public void setBedNum(Integer bedNum)
	{
		this.bedNum = bedNum;
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq	=	displaySeq;
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setBedID(rs.getInt("bed_id"));
		this.setBedName(rs.getString("bed_name"));
		this.setBedNum(rs.getInt("bed_num"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * �x�b�h�}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				//�\������O�ɂ��炷
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					//�\��������ɂ��炷
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			sql	=	this.getUpdateSQL();
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				//�\��������ɂ��炷
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
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
	 * �x�b�h�}�X�^����f�[�^���폜����B�i�_���폜�j
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
		
		//�\������O�ɂ��炷
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
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
	 * �x�b�h�}�X�^�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if (this.getBedID() == null || this.getBedID() < 1) return false;
		
		if (con == null) return false;
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                
		if (rs.next()) {
                    this.setData(rs);
                    return true;
                }
                
                return false;
	}
        
	/**
	 * �x�b�h�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getBedID() == null || this.getBedID() < 1)	return	false;
		
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
		return	"select *\n" +
				"from mst_bed\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	bed_id = " + SQLUtil.convertForSQL(this.getBedID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_bed\n" +
				"(shop_id, bed_id, bed_name, bed_num, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				"coalesce(max(bed_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getBedName()) + ",\n" +
				SQLUtil.convertForSQL(this.getBedNum()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_bed\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_bed\n" +
				"set\n" +
				"bed_name = " + SQLUtil.convertForSQL(this.getBedName()) + ",\n" +
				"bed_num = " + SQLUtil.convertForSQL(this.getBedNum()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and bed_id != " +
				SQLUtil.convertForSQL(this.getBedID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_bed\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and bed_id != " +
				SQLUtil.convertForSQL(this.getBedID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	bed_id = " + SQLUtil.convertForSQL(this.getBedID()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_bed\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_bed\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	bed_id = " + SQLUtil.convertForSQL(this.getBedID()) + "\n";
	}
}

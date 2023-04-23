/*
 * MstProductClass.java
 *
 * Created on 2006/04/26, 18:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ���i�敪�}�X�^�f�[�^
 * @author katagiri
 */
public class MstProductClass
{
	/**
	 * ���i�敪�h�c�̍ŏ��l
	 */
	public static int		PRODUCT_CLASS_ID_MIN		=	1;
	/**
	 * ���i�敪�h�c�̍ő�l
	 */
	public static int		PRODUCT_CLASS_ID_MAX		=	Integer.MAX_VALUE;
	/**
	 * ���i�敪���̒����̍ő�l
	 */
	public static int		PRODUCT_CLASS_NAME_MAX		=	20;
	/**
	 * �\�����̍ŏ��l
	 */
	public static int		DISPLAY_SEQ_MIN				=	0;
	/**
	 * �\�����̍ő�l
	 */
	public static int		DISPLAY_SEQ_MAX				=	9999;
	
	/**
	 * ���i�敪�h�c
	 */
	private Integer		productClassID		=	null;
	/**
	 * ���i�敪��
	 */
	private	String		productClassName	=	null;
	/**
	 * �\����
	 */
	private Integer		displaySeq			=	null;
	
	/**
	 * �R���X�g���N�^
	 */
	public MstProductClass()
	{
	}

	/**
	 * ������ɕϊ�����B�i���i�敪���j
	 * @return ���i�敪��
	 */
	public String toString()
	{
		return productClassName;
	}

	/**
	 * ���i�敪�h�c���擾����B
	 * @return ���i�敪�h�c
	 */
	public Integer getProductClassID()
	{
		return productClassID;
	}

	/**
	 * ���i�敪�h�c���Z�b�g����B
	 * @param productClassID ���i�敪�h�c
	 */
	public void setProductClassID(Integer productClassID)
	{
		this.productClassID = productClassID;
	}

	/**
	 * ���i�敪�����擾����B
	 * @return ���i�敪��
	 */
	public String getProductClassName()
	{
		return productClassName;
	}

	/**
	 * ���i�敪�����Z�b�g����B
	 * @param productClassName ���i�敪��
	 */
	public void setProductClassName(String productClassName)
	{
		if(productClassName == null || productClassName.length() <= MstProductClass.PRODUCT_CLASS_NAME_MAX)
		{
			this.productClassName	=	productClassName;
		}
		else
		{
			this.productClassName	=	productClassName.substring(0, MstProductClass.PRODUCT_CLASS_NAME_MAX);
		}
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
		if(displaySeq == null	||
				displaySeq < MstProductClass.DISPLAY_SEQ_MIN ||
				MstProductClass.DISPLAY_SEQ_MAX < displaySeq)
		{
			this.displaySeq	=	null;
		}
		else
		{
			this.displaySeq	=	displaySeq;
		}
	}
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setProductClassID(null);
		this.setProductClassName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * ���i�敪�}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mpc ���i�敪�}�X�^�f�[�^
	 */
	public void setData(MstProductClass mpc)
	{
		this.setProductClassID(mpc.getProductClassID());
		this.setProductClassName(mpc.getProductClassName());
		this.setDisplaySeq(mpc.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProductClassID(rs.getInt("product_class_id"));
		this.setProductClassName(rs.getString("product_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * ���i�敪�}�X�^�Ƀf�[�^��o�^����B
	 * @param lastSeq ���̕\����
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	
	/**
	 * ���i�敪�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * ���i�敪�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getProductClassID() == null || this.getProductClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * ���i�敪�}�X�^�擾�p�̂r�p�k�����擾����B
	 * @return ���i�敪�}�X�^�擾�p�̂r�p�k��
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_product_class\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, product_class_name\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_product_class\n"
			+	"where	product_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����B
	 * @param seq �\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_product_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_product_class\n" +
				"(product_class_id, product_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(product_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getProductClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_product_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_product_class\n" +
				"set\n" +
				"product_class_name = " +
				SQLUtil.convertForSQL(this.getProductClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null\n" +
				"and product_class_id != " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product_class\n" +
				"where delete_date is null\n" +
				"and product_class_id != " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_product_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	product_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
}

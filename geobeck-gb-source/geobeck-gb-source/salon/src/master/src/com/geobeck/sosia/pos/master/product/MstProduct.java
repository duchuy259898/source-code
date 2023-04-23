/*
 * MstProduct.java
 *
 * Created on 2006/04/26, 20:15
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
 * ���i�}�X�^�f�[�^
 * @author katagiri
 */
public class MstProduct
{
	/**
	 * ���i�敪
	 */
	private	MstProductClass		productClass	=	null;
	/**
	 * ���i�h�c
	 */
	private	Integer				productID		=	null;
	/**
	 * ���i�R�[�h
	 */
	private	String				productNo		=	"";
	/**
	 * ���i��
	 */
	private	String				productName		=	"";
	/**
	 * �P��
	 */
	private	Long				price			=	null;
	/**
	 * �\����
	 */
	private	Integer				displaySeq		=	null;
	
	
	/**
	 * �R���X�g���N�^
	 */
	public MstProduct()
	{
	}

	/**
	 * ���i�敪���擾����B
	 * @return ���i�敪
	 */
	public MstProductClass getProductClass()
	{
		return productClass;
	}

	/**
	 * ���i�敪���擾����B
	 * @param productClass ���i�敪
	 */
	public void setProductClass(MstProductClass productClass)
	{
		this.productClass = productClass;
	}

	/**
	 * ���i�敪�h�c���擾����B
	 * @return ���i�敪�h�c
	 */
	public Integer getProductClassID()
	{
		if(productClass == null)	return	null;
		else	return productClass.getProductClassID();
	}

	/**
	 * ���i�敪�h�c���擾����B
	 * @param productClassID ���i�敪�h�c
	 */
	public void setProductClassID(Integer productClassID)
	{
		if(productClass == null)	productClass	=	new	MstProductClass();
		this.productClass.setProductClassID(productClassID);
	}

	/**
	 * ���i�h�c���擾����B
	 * @return ���i�h�c
	 */
	public Integer getProductID()
	{
		return productID;
	}

	/**
	 * ���i�h�c���擾����B
	 * @param productID ���i�h�c
	 */
	public void setProductID(Integer productID)
	{
		this.productID = productID;
	}

	/**
	 * ���i�R�[�h���擾����B
	 * @return ���i�R�[�h
	 */
	public String getProductNo()
	{
		return productNo;
	}

	/**
	 * ���i�R�[�h���擾����B
	 * @param productNo ���i�R�[�h
	 */
	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	/**
	 * ���i�����擾����B
	 * @return ���i��
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * ���i�����擾����B
	 * @param productName ���i��
	 */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	/**
	 * �P�����擾����B
	 * @return �P��
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * �P�����擾����B
	 * @param price �P��
	 */
	public void setPrice(Long price)
	{
		this.price = price;
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
	 * �\�������擾����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setProductClass(null);
		this.setProductID(null);
		this.setProductNo("");
		this.setProductName("");
		this.setPrice(null);
		this.setDisplaySeq(null);
	}
	
	/**
	 * ���i�}�X�^����A�ݒ肳��Ă��鏤�iID�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			this.setData(rs);
		}
		
		return	true;
	}
	
	/**
	 * ���i�}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mp ���i�}�X�^�f�[�^
	 */
	public void setData(MstProduct mp)
	{
		this.setProductClass(mp.getProductClass());
		this.setProductID(mp.getProductID());
		this.setProductNo(mp.getProductNo());
		this.setProductName(mp.getProductName());
		this.setPrice(mp.getPrice());
		this.setDisplaySeq(mp.getDisplaySeq());
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProductClassID(rs.getInt("product_class_id"));
		this.setProductID(rs.getInt("product_id"));
		this.setProductNo(rs.getString("product_no"));
		this.setProductName(rs.getString("product_name"));
		this.setPrice(rs.getLong("price"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * ���i�}�X�^�Ƀf�[�^��o�^����B
	 * @param lastSeq ���̕\����
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(this.getProductID() == null || this.getProductID().equals(""))	return	false;
		
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
	 * ���i�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * ���i�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getProductID() == null || this.getProductID().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * ���i�}�X�^�擾�p�̂r�p�k�����擾����B
	 * @param productClassID ���i�敪�h�c
	 * @return ���i�}�X�^�擾�p�̂r�p�k��
	 */
	public static String getSelectAllSQL(Integer productClassID)
	{
		return	"select		*\n" +
				"from		mst_product\n" +
				"where		delete_date is null\n" +
				"		and	product_class_id = " +
				SQLUtil.convertForSQL(productClassID) + "\n" +
				"order by	display_seq, product_id\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_product\n" +
				"where	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����B
	 * @param seq �\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_product\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_product\n" +
				"(product_class_id, product_id, product_no, product_name,\n" +
				"price, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getProductClassID()) + ",\n" +
				"(select coalesce(max(product_id), 0) + 1\n" +
				"from mst_product),\n" +
				SQLUtil.convertForSQL(this.getProductNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductName()) + ",\n" +
				SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_product\n" +
				"set\n" +
				"product_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + ",\n" +
				"product_no = " + SQLUtil.convertForSQL(this.getProductNo()) + ",\n" +
				"product_name = " + SQLUtil.convertForSQL(this.getProductName()) + ",\n" +
				"price = " + SQLUtil.convertForSQL(this.getPrice()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_product\n" +
				"where delete_date is null\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
				"and product_id != " +
				SQLUtil.convertForSQL(this.getProductID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n" +
				"and product_class_id = " +
				SQLUtil.convertForSQL(this.getProductClassID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_product\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	product_id = " + SQLUtil.convertForSQL(this.getProductID()) + "\n";
	}
}

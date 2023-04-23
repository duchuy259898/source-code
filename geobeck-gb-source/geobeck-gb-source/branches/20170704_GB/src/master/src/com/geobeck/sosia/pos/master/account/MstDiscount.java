/*
 * MstDiscount.java
 *
 * Created on 2006/06/06, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �����}�X�^�f�[�^
 * @author katagiri
 */
public class MstDiscount
{
        //�����敪
        public	static	final	int	DISCOUNT_DIVISION_ALL           =	0;
	public	static	final	int	DISCOUNT_DIVISION_TECHNIC	=	1;
	public	static	final	int	DISCOUNT_DIVISION_PRODUCT	=	2;
        //nhanvt start 20141219 New request #34216
        public	static	final	int	DISCOUNT_DIVISION_COURSE	=	5;
        //nhanvt end 20141219 New request #34216
        //�������@
	public	static	final	int	DISCOUNT_METHOD_VALUE	=	1;
	public	static	final	int	DISCOUNT_METHOD_RATE	=	2;
	/**
	 * �����h�c�̍ŏ��l
	 */
	public static int		DISCOUNT_ID_MIN			=	1;
	/**
	 * �����h�c�̍ő�l
	 */
	public static int		DISCOUNT_ID_MAX			=	Integer.MAX_VALUE;
	/**
	 * �������̒����̍ő�l
	 */
	public static int		DISCOUNT_NAME_MAX		=	10;
	/**
	 * �\�����̍ŏ��l
	 */
	public static int		DISPLAY_SEQ_MIN			=	0;
	/**
	 * �\�����̍ő�l
	 */
	public static int		DISPLAY_SEQ_MAX			=	9999;
	
	/**
	 * �����h�c
	 */
	private Integer		discountID		=	null;
	/**
	 * ������
	 */
	private	String		discountName		=	null;
	/**
	 * �\����
	 */
	private Integer		displaySeq	=	null;
	/**
	 * �����敪
	 */
	private	Integer	discountDivision			=	null;
	/**
	 * �������@
	 */
	private	Integer	discountMethod			=	null;
	/**
	 * ������
	 */
	private	Double				discountRate		=	0d;
	/**
	 * �������z
	 */
	private	Long		discountValue	=	null;
	
	/** Creates a new instance of MstDiscount */
	public MstDiscount()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�������j
	 * @return ������
	 */
	public String toString()
	{
		return discountName;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof MstDiscount)
		{
			if(obj != null)
			{
				return	((MstDiscount)obj).getDiscountID() == this.getDiscountID();
			}
		}
		
		return	false;
	}

	/**
	 * �����h�c���擾����B
	 * @return �����h�c
	 */
	public Integer getDiscountID()
	{
		return discountID;
	}

	/**
	 * �����h�c���Z�b�g����B
	 * @param discountID �����h�c
	 */
	public void setDiscountID(Integer discountID)
	{
		this.discountID = discountID;
	}

	/**
	 * ���������擾����B
	 * @return ������
	 */
	public String getDiscountName()
	{
		return discountName;
	}

	/**
	 * ���������Z�b�g����B
	 * @param discountName ������
	 */
	public void setDiscountName(String discountName)
	{
		if(discountName == null || discountName.length() <= MstDiscount.DISCOUNT_NAME_MAX)
		{
			this.discountName	=	discountName;
		}
		else
		{
			this.discountName	=	discountName.substring(0, MstDiscount.DISCOUNT_NAME_MAX);
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
                this.displaySeq	=	displaySeq;
	}

	/**
	 * �����敪���擾����
	 * @return �����敪
	 */
	public Integer getDiscountDivision()
	{
		return discountDivision;
	}

	/**
	 * �����敪��ݒ肷��
	 * @param discountDivision �����敪
	 */
	public void setDiscountDivision(Integer discountDivision)
	{
		this.discountDivision = discountDivision;
	}

	/**
	 * �����敪�����擾����B
	 * @return �����敪��
	 */
	public String getDiscountDivisionName()
	{
		switch(discountDivision)
		{
			case DISCOUNT_DIVISION_ALL:
				return	"�S��";
			case DISCOUNT_DIVISION_TECHNIC:
				return	"�Z�p";
			case DISCOUNT_DIVISION_PRODUCT:
				return	"���i";
                        //nhanvt start 20141219 New request #34216
                        case DISCOUNT_DIVISION_COURSE:
				return	"�R�[�X";
                        //nhanvt end 20141219 New request #34216
			default:
				return	"";
		}
	}

	/**
	 * �������@���擾����
	 * @return �������@
	 */
	public Integer getDiscountMethod()
	{
		return discountMethod;
	}

	/**
	 * �������@��ݒ肷��
	 * @param discountMethod �������@
	 */
	public void setDiscountMethod(Integer discountMethod)
	{
		this.discountMethod = discountMethod;
	}

	/**
	 * �������@�����擾����B
	 * @return �������@��
	 */
	public String getDiscountMethodName()
	{
		switch(discountMethod)
		{
			case DISCOUNT_METHOD_VALUE:
				return	"���z";
			case DISCOUNT_METHOD_RATE:
				return	"��";
			default:
				return	"";
		}
	}

	/**
	 * ���������擾����
	 * @return ������
	 */
	public Double getDiscountRate()
	{
		return discountRate;
	}

	/**
	 * ��������ݒ肷��
	 * @param discountRate ������
	 */
	public void setDiscountRate(Double discountRate)
	{
		this.discountRate = discountRate;
	}
	
	/**
	 * ������(%�\��)���擾����
	 * @return ������(%�\��)
	 */
	public Double getDiscountRatePercentage()
	{
		if(this.getDiscountRate() == null)
		{
			return	0d;
		}
		else
		{
			return	this.getDiscountRate() * 100d;
		}
	}

	/**
	 * �������z
	 * @return �������z
	 */
	public Long getDiscountValue()
	{
		return discountValue;
	}

	/**
	 * �������z
	 * @param discountValue �������z
	 */
	public void setDiscountValue(Long discountValue)
	{
		this.discountValue = discountValue;
	}

	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setDiscountID(null);
		this.setDiscountName("");
		this.setDisplaySeq(null);
		this.setDiscountDivision(null);
		this.setDiscountMethod(null);
		this.setDiscountRate(null);
		this.setDiscountValue(null);
	}
	
	/**
	 * ResultSetWrapper����f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setDiscountID(rs.getInt("discount_id"));
		this.setDiscountName(rs.getString("discount_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setDiscountDivision(rs.getInt("discount_division"));
		this.setDiscountMethod(rs.getInt("discount_method"));
		this.setDiscountRate(rs.getDouble("discount_rate"));
		this.setDiscountValue(rs.getLong("discount_value"));
	}
	
	/**
	 * ������ʃ}�X�^�Ƀf�[�^��o�^����B
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
	 * ������ʃ}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �����}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getDiscountID() == null || this.getDiscountID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * �����}�X�^�擾�p�̂r�p�k�����擾����B
	 * @return �����}�X�^�擾�p�̂r�p�k��
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n"
			+	"from		mst_discount\n"
			+	"where		delete_date is null\n"
			+	"order by	display_seq, discount_name\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n"
			+	"from mst_discount\n"
			+	"where	discount_id = " + SQLUtil.convertForSQL(this.getDiscountID()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_discount\n"
			+	"(discount_id, discount_name, display_seq,\n"
			+	"discount_division, discount_method, discount_rate, discount_value,\n"
			+	"insert_date, update_date, delete_date)\n"
			+	"select\n"
			+	"coalesce(max(discount_id), 0) + 1,\n"
			+	SQLUtil.convertForSQL(this.getDiscountName()) + ",\n"
			+	"case\n"
			+	"when " + SQLUtil.convertForSQL(this.getDisplaySeq())
			+	" between 0 and coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null), 0) then "
			+	SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
			+	"else coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null), 0) + 1 end,\n"
			+	SQLUtil.convertForSQL(this.getDiscountDivision()) + ",\n"
			+	SQLUtil.convertForSQL(this.getDiscountMethod()) + ",\n"
			+	SQLUtil.convertForSQL(this.getDiscountRate(), "0") + ",\n"
			+	SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n"
			+	"current_timestamp, current_timestamp, null\n"
			+	"from mst_discount\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_discount\n"
			+	"set\n"
			+	"discount_name = " + SQLUtil.convertForSQL(this.getDiscountName()) + ",\n"
			+	"display_seq = case\n"
			+	"when " + SQLUtil.convertForSQL(this.getDisplaySeq())
			+	" between 0 and coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null\n"
			+	"and discount_id != "
			+	SQLUtil.convertForSQL(this.getDiscountID()) + "), 0) then "
			+	SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
			+	"else coalesce((select max(display_seq)\n"
			+	"from mst_discount\n"
			+	"where delete_date is null\n"
			+	"and discount_id != "
			+	SQLUtil.convertForSQL(this.getDiscountID()) + "), 0) + 1 end,\n"
			+	"discount_division = " + SQLUtil.convertForSQL(this.getDiscountDivision()) + ",\n"
			+	"discount_method = " + SQLUtil.convertForSQL(this.getDiscountMethod()) + ",\n"
			+	"discount_rate = " + SQLUtil.convertForSQL(this.getDiscountRate(), "0") + ",\n"
			+	"discount_value = " + SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n"
			+	"update_date = current_timestamp\n"
			+	"where	discount_id = " + SQLUtil.convertForSQL(this.getDiscountID()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_discount\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_discount\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	discount_id = " + SQLUtil.convertForSQL(this.getDiscountID()) + "\n";
	}
}

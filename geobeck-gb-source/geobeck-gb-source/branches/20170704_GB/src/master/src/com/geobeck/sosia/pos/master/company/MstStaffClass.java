/*
 * MstStaffClass.java
 *
 * Created on 2006/04/25, 17:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �X�^�b�t�敪�f�[�^
 * @author katagiri
 */
public class MstStaffClass
{
	/**
	 * �X�^�b�t�敪�h�c
	 */
	private	Integer         staffClassID        =   null;
	/**
	 * �X�^�b�t�敪��
	 */
	private	String          staffClassName      =   "";
	/**
	 * �X�^�b�t�敪����
	 */
	protected String        staffClassContractedName    =   "";
	/**
	 * �\��\�\���t���O
	 */
        private Boolean         displayReservation  =   false;
	/**
	 * �\����
	 */
	private Integer         displaySeq          =   null;

	/**
	 * �R���X�g���N�^
	 */
	public MstStaffClass()
	{
	}
	
	/**
	 * ������ɕϊ�����B�i�X�^�b�t�敪���j
	 * @return �X�^�b�t�敪��
	 */
	public String toString()
	{
		return	this.getStaffClassName();
	}

	/**
	 * �X�^�b�t�敪�h�c���擾����B
	 * @return �X�^�b�t�敪�h�c
	 */
	public Integer getStaffClassID()
	{
		return staffClassID;
	}

	/**
	 * �X�^�b�t�敪�h�c���Z�b�g����B
	 * @param staffClassID �X�^�b�t�敪�h�c
	 */
	public void setStaffClassID(Integer staffClassID)
	{
		this.staffClassID = staffClassID;
	}

	/**
	 * �X�^�b�t�敪�����擾����B
	 * @return �X�^�b�t�敪��
	 */
	public String getStaffClassName()
	{
		return staffClassName;
	}

	/**
	 * �X�^�b�t�敪�����Z�b�g����B
	 * @param staffClassName �X�^�b�t�敪��
	 */
	public void setStaffClassName(String staffClassName)
	{
		this.staffClassName = staffClassName;
	}

        /**
	 * �X�^�b�t�敪���̂��擾����B
	 * @return �X�^�b�t�敪����
	 */
	public String getStaffClassContractedName()
	{
		return staffClassContractedName;
	}

	/**
	 * �X�^�b�t�敪���̂��Z�b�g����B
	 * @param staffClassContractedName �X�^�b�t�敪����
	 */
	public void setStaffClassContractedName(String staffClassContractedName)
	{
		this.staffClassContractedName = staffClassContractedName;
	}
        
	/**
	 * �\��\�\���t���O���擾����B
	 * @return �\��\�\���t���O
	 */
        public Boolean isDisplayReservation(){
                return this.displayReservation;
        }
	/**
	 * �\��\�\���t���O���Z�b�g����B
	 * @param displayReservation �\��\�\���t���O
	 */
        public void setDisplayReservation(Boolean displayReservation){
                this.displayReservation = displayReservation;
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
		this.displaySeq = displaySeq;
	}
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setStaffClassID(null);
		this.setStaffClassName("");
                this.setStaffClassContractedName("");
                this.setDisplayReservation(false);
                this.setDisplaySeq(0);
	}
	
	/**
	 * �X�^�b�t�敪�}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param msc �X�^�b�t�敪�}�X�^�f�[�^
	 */
	public void setData(MstStaffClass msc)
	{
		this.setStaffClassID(msc.getStaffClassID());
		this.setStaffClassName(msc.getStaffClassName());
		this.setStaffClassContractedName(msc.getStaffClassContractedName());
		this.setDisplayReservation(msc.isDisplayReservation());
		this.setDisplaySeq(msc.getDisplaySeq());
	}
        
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper 
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setStaffClassID(rs.getInt("staff_class_id"));
		this.setStaffClassName(rs.getString("staff_class_name"));
		this.setStaffClassContractedName(rs.getString("staff_class_contracted_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setDisplayReservation(rs.getBoolean("display_reservation"));
	}
	
	/**
	 * �X�^�b�t�敪�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 */
	public ArrayList<MstStaffClass> load(ConnectionWrapper con) throws SQLException
	{
		ArrayList<MstStaffClass> list = new ArrayList<MstStaffClass>();
                
		ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());

		while(rs.next())
		{
			MstStaffClass msc = new	MstStaffClass();
			msc.setData(rs);
			list.add(msc);
		}

		rs.close();
                
                return list;
	}

	/**
	 * �X�^�b�t�敪�}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �X�^�b�t�敪�}�X�^����f�[�^���폜����B�i�_���폜�j
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * �X�^�b�t�敪�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getStaffClassID() == null || this.getStaffClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectAllSQL()
	{
		return	"select *\n" +
				"from mst_staff_class\n" +
				"where delete_date is null\n" +
				"order by display_seq, staff_class_id\n";
	}
        
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_staff_class\n" +
				"where	staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n";
	}

	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_staff_class\n" +
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
		return	"insert into mst_staff_class\n" +
				"(staff_class_id, staff_class_name, staff_class_contracted_name, display_reservation, display_seq, \n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(staff_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getStaffClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffClassContractedName()) + ",\n" +
				SQLUtil.convertForSQL(this.isDisplayReservation()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_staff_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_staff_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_staff_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_staff_class\n" +
				"set\n" +
				"staff_class_name = " + SQLUtil.convertForSQL(this.getStaffClassName()) + ",\n" +
				"staff_class_contracted_name = " + SQLUtil.convertForSQL(this.getStaffClassContractedName()) + ",\n" +
				"display_reservation = " + SQLUtil.convertForSQL(this.isDisplayReservation()) + ",\n" +
                                "display_seq =\n" +
                                "    case\n" +
                                "        when " + SQLUtil.convertForSQL(this.getDisplaySeq()) + " between 0\n" +
                                "         and coalesce\n" +
                                "                ((\n" +
                                "                    select\n" +
                                "                        max(display_seq)\n" +
                                "                    from\n" +
                                "                        mst_staff_class\n" +
                                "                    where\n" +
                                "                            delete_date is null\n" +
                                "                        and staff_class_id != " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n" +
                                "                ), 0)\n" +
                                "        then " + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
                                "        else\n" +
                                "            coalesce\n" +
                                "                ((\n" +
                                "                    select\n" +
                                "                        max(display_seq)\n" +
                                "                    from\n" +
                                "                        mst_staff_class\n" +
                                "                    where\n" +
                                "                            delete_date is null\n" +
                                "                        and staff_class_id != " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n" +
                                "                ), 0) + 1\n" +
                                "    end,\n" +
				"update_date = current_timestamp\n" +
				"where	staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n";
	}

	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_staff_class\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n";
	}
}

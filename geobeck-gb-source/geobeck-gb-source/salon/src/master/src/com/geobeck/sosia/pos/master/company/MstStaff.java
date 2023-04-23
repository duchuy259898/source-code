/*
 * MstStaff.java
 *
 * Created on 2006/04/26, 9:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import javax.swing.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.logging.Level;

/**
 * �X�^�b�t�}�X�^�f�[�^
 * @author katagiri
 */
public class MstStaff
{
	/**
	 * �X�^�b�t�h�c
	 */
	private	Integer			staffID			=	null;
	/**
	 * �X�^�b�t�R�[�h
	 */
	private	String			staffNo			=	"";
	/**
	 * �X�^�b�t��
	 */
	private	String[]		staffName		=	{"", ""};
	/**
	 * �X�^�b�t�t���K�i
	 */
	private	String[]		staffKana		=	{"", ""};
	/**
	 * �X�֔ԍ�
	 */
	private	String			postalCode		=	"";
	/**
	 * �Z��
	 */
	private	String[]		address			=	{"", "", "", ""};
	/**
	 * �d�b�ԍ�
	 */
	private	String			phoneNumber		=	"";
	/**
	 * �g�єԍ�
	 */
	private	String			cellularNumber		=	"";
	/**
	 * ���[���A�h���X
	 */
	private	String			mailAddress		=	"";
	/**
	 * �ݐГX��
	 */
	private	MstShop			shop			=	null;
	/**
	 * �X�^�b�t�敪
	 */
	private	MstStaffClass	staffClass		=	null;
	/**
	 * �\����
	 */
	protected	Integer		displaySeq	=	null;
	/**
	 * �X�ܕʕ\���ݒ�
	 */
	private	boolean		display         =	true;
        
        // vtbphuong start add 20150513
        
        private         String          displayName = "";
        // vtbphuong end add 20150513

	/**
	 * �R���X�g���N�^
	 */
	public MstStaff()
	{
	}
	/**
	 * �R���X�g���N�^
	 * @param staffID �X�^�b�t�h�c
	 */
	public MstStaff(Integer staffID)
	{
		this.setStaffID(staffID);
	}
	
	/**
	 * ������ɕϊ�����B�i�X�^�b�t���j
	 * @return �X�^�b�t��
	 */
	public String toString()
	{
		return	this.getStaffName(0) + "�@" + this.getStaffName(1);
	}

	/**
	 * �X�^�b�t�h�c���擾����B
	 * @return �X�^�b�t�h�c
	 */
	public Integer getStaffID()
	{
		return staffID;
	}

	/**
	 * �X�^�b�t�h�c���Z�b�g����B
	 * @param staffID �X�^�b�t�h�c
	 */
	public void setStaffID(Integer staffID)
	{
		this.staffID = staffID;
	}

	/**
	 * �X�^�b�t�h�c���擾����B
	 * @return �X�^�b�t�h�c
	 */
	public String getStaffNo()
	{
		return staffNo;
	}

	/**
	 * �X�^�b�t�h�c���Z�b�g����B
	 * @param staffID �X�^�b�t�h�c
	 */
	public void setStaffNo(String staffNo)
	{
		this.staffNo = staffNo;
	}

	/**
	 * �X�^�b�t�����擾����B
	 * @return �X�^�b�t��
	 */
	public String[] getStaffName()
	{
		return staffName;
	}

	/**
	 * �X�^�b�t�����擾����B
	 * @param index �C���f�b�N�X
	 * @return �X�^�b�t��
	 */
	public String getStaffName(int index)
	{
		return staffName[index];
	}

	/**
	 * �X�^�b�t�����Z�b�g����B
	 * @param staffName �X�^�b�t��
	 */
	public void setStaffName(String[] staffName)
	{
		this.staffName = staffName;
	}

	/**
	 * �X�^�b�t�����Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param staffName �X�^�b�t��
	 */
	public void setStaffName(int index, String staffName)
	{
		this.staffName[index] = (staffName == null ? "" : staffName);
	}
	
	/**
	 * �X�^�b�t�����擾����B
	 * @return �X�^�b�t��
	 */
	public String getFullStaffName()
	{
		return	(staffName[0] == null ? "" : staffName[0]) +
				(staffName[0] != null && !staffName[0].equals("") &&
				staffName[1] != null && !staffName[1].equals("") ? "�@" : "")
				+	(staffName[1] == null  ? "" : staffName[1]);
	}

	/**
	 * �X�^�b�t�t���K�i���擾����B
	 * @return �X�^�b�t�t���K�i
	 */
	public String[] getStaffKana()
	{
		return staffKana;
	}
	
	/**
	 * �X�^�b�t�t���K�i���擾����B
	 * @param index �C���f�b�N�X
	 * @return �X�^�b�t�t���K�i
	 */
	public String getStaffKana(int index)
	{
		return staffKana[index];
	}

	/**
	 * �X�^�b�t�t���K�i���Z�b�g����B
	 * @param staffKana �X�^�b�t�t���K�i
	 */
	public void setStaffKana(String[] staffKana)
	{
		this.staffKana = staffKana;
	}
	
	/**
	 * �X�^�b�t�t���K�i���Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param staffKana �X�^�b�t�t���K�i
	 */
	public void setStaffKana(int index, String staffKana)
	{
		this.staffKana[index] = staffKana;
	}

	/**
	 * �X�֔ԍ����擾����B
	 * @return �X�֔ԍ�
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * �X�֔ԍ����Z�b�g����B
	 * @param postalCode �X�֔ԍ�
	 */
	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * �Z�����擾����B
	 * @return �Z��
	 */
	public String[] getAddress()
	{
		return address;
	}
	
	/**
	 * �Z�����擾����B
	 * @param index �C���f�b�N�X
	 * @return �Z��
	 */
	public String getAddress(int index)
	{
		return address[index];
	}

	/**
	 * �Z�����Z�b�g����B
	 * @param address �Z��
	 */
	public void setAddress(String[] address)
	{
		this.address = address;
	}

	/**
	 * �Z�����Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param address �Z��
	 */
	public void setAddress(int index, String address)
	{
		this.address[index] = address;
	}
	
	public String getFullAddress()
	{
		return	(address[0] == null ? "" : address[0]) +
				(address[1] == null ? "" : address[1]) +
				(address[2] == null ? "" : address[2]) +
				(address[3] == null ? "" : address[3]);
	}

	/**
	 * �d�b�ԍ����擾����B
	 * @return �d�b�ԍ�
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * �d�b�ԍ����Z�b�g����B
	 * @param phoneNumber �d�b�ԍ�
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * �g�єԍ����擾����B
	 * @return �g�єԍ�
	 */
	public String getCellularNumber()
	{
		return cellularNumber;
	}

	/**
	 * �g�єԍ����Z�b�g����B
	 * @param cellularNumber �g�єԍ�
	 */
	public void setCellularNumber(String cellularNumber)
	{
		this.cellularNumber = cellularNumber;
	}

	/**
	 * ���[���A�h���X���擾����B
	 * @return ���[���A�h���X
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * ���[���A�h���X���Z�b�g����B
	 * @param mailAddress ���[���A�h���X
	 */
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	/**
	 * �ݐГX�܂��擾����B
	 * @return �ݐГX��
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * �ݐГX�܂��Z�b�g����B
	 * @param shop �ݐГX��
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * �ݐГX�܂h�c���Z�b�g����B
	 * @param shopID �ݐГX�܂h�c
	 */
	public void setShopID(Integer shopID)
	{
		shop	=	null;
		shop	=	new MstShop();
		
		shop.setShopID(shopID);
	}

	/**
	 * �ݐГX�܂h�c���擾����B
	 * @return �ݐГX�܂h�c
	 */
	public Integer getShopID()
	{
		if(shop == null)	return	null;
		else	return shop.getShopID();
	}

	/**
	 * �ݐГX�ܖ����擾����B
	 * @return �ݐГX�ܖ�
	 */
	public String getShopName()
	{
		if(shop == null)	return	null;
		else	return shop.getShopName();
	}

	/**
	 * �ݐГX�ܖ����Z�b�g����B
	 * @param shopName �ݐГX�ܖ�
	 */
	public void setShopName(String shopName)
	{
		if(shop != null)	shop.setShopName(shopName);
	}

	/**
	 * �X�^�b�t�敪���擾����B
	 * @return �X�^�b�t�敪
	 */
	public MstStaffClass getStaffClass()
	{
		return staffClass;
	}

	/**
	 * �X�^�b�t�敪���Z�b�g����B
	 * @param staffClass �X�^�b�t�敪
	 */
	public void setStaffClass(MstStaffClass staffClass)
	{
		this.staffClass = staffClass;
	}

	/**
	 * �X�^�b�t�敪�h�c���Z�b�g����B
	 * @param staffClassID �X�^�b�t�敪�h�c
	 */
	public void setStaffClassID(Integer staffClassID)
	{
		staffClass	=	null;
		staffClass	=	new MstStaffClass();
		
		staffClass.setStaffClassID(staffClassID);
	}

	/**
	 * �X�^�b�t�敪�h�c���擾����B
	 * @return �X�^�b�t�敪�h�c
	 */
	public Integer getStaffClassID()
	{
		if(staffClass == null)	return	null;
		else	return staffClass.getStaffClassID();
	}

	/**
	 * �X�^�b�t�敪�����擾����B
	 * @return �X�^�b�t�敪��
	 */
	public String getStaffClassName()
	{
		if(staffClass == null)	return	null;
		else	return staffClass.getStaffClassName();
	}

	/**
	 * �X�^�b�t�敪�����Z�b�g����B
	 * @param staffClassName �X�^�b�t�敪��
	 */
	public void setStaffClassName(String staffClassName)
	{
		if(staffClass != null)	staffClass.setStaffClassName(staffClassName);
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
         * @return the display
         */
        public boolean isDisplay() {
            return display;
        }

        // vtbphuong start add 20150513

        public String getDisplayName() {
        return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

    // vtbphuong end add 20150513
    /**
     * @param display the display to set
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }

	public boolean equals(Object obj)
	{
            boolean result = false;

            if (obj instanceof MstStaff) {
                MstStaff temp = (MstStaff)obj;
                if (this.getStaffID() == null && temp.getStaffID() == null) {
                    result = true;
                } else {
                    try {
                        result = this.getStaffID().equals(temp.getStaffID());
                    } catch (Exception ignore) {
                    }
                }
            }

            return result;
	}
	
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
		this.setStaffID(null);
		this.setStaffName(0, "");
		this.setStaffName(1, "");
		this.setStaffKana(0, "");
		this.setStaffKana(1, "");
		this.setPostalCode("");
		this.setAddress(0, "");
		this.setAddress(1, "");
		this.setAddress(2, "");
		this.setAddress(3, "");
		this.setPhoneNumber("");
		this.setCellularNumber("");
		this.setMailAddress("");
		this.setShop(null);
		this.setStaffClass(null);
		this.setDisplaySeq(null);
                // vtbpuong start add 20150513
                this.setDisplayName("");
                // vtbphuong end add 20150513 
	}
	
	/**
	 * �X�^�b�t�}�X�^����A�ݒ肳��Ă���X�^�b�tID�̃f�[�^��ǂݍ��ށB
	 * @return true - ����
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			this.setData(rs);
		}
		else
		{
			return	false;
		}
		
		return	true;
	}
	
	
	/**
	 * �X�^�b�t�R�[�h�ɑΉ�����X�^�b�t�f�[�^�̔z����擾����B
	 * @param no �X�^�b�t�R�[�h
	 * @return �X�^�b�t�R�[�h�ɑΉ�����X�^�b�t�f�[�^�̔z��
	 */
	public static ArrayList<MstStaff> getMstStaffArrayByNo(
			ConnectionWrapper con,
			String no) throws SQLException
	{
		ArrayList<MstStaff> result = new ArrayList<MstStaff>();
		
		ResultSetWrapper rs = con.executeQuery(MstStaff.getMstStaffByNoSQL(no));

		while (rs.next()) {
                    
                    MstStaff ms = new MstStaff();

                    ms.setData(rs);

                    result.add(ms);
		}

		rs.close();
		
		return	result;
	}
	
	
	public static String getMstStaffByNoSQL(String no)
	{
		return	"select *\n" +
				"from mst_staff ms\n" +
				"where ms.delete_date is null\n" +
				"and ms.staff_no = '" + no + "'\n" +
				"order by ms.display_seq\n" +
                                "limit 1\n";
	}

	/**
	 * �X�^�b�tID�ɑΉ�����X�^�b�t�f�[�^�̔z����擾����B
	 * @param no �X�^�b�tID
	 * @return �X�^�b�tID�ɑΉ�����X�^�b�t�f�[�^�̔z��
	 */
	public static ArrayList<MstStaff> getMstStaffArrayByID(
			ConnectionWrapper con,
			Integer id) throws SQLException
	{
		ArrayList<MstStaff> result = new ArrayList<MstStaff>();
		
		ResultSetWrapper rs = con.executeQuery(MstStaff.getMstStaffByIdSQL(id));

		while (rs.next()) {
                    
                    MstStaff ms = new MstStaff();

                    ms.setData(rs);

                    result.add(ms);
		}

		rs.close();
		
		return	result;
	}
	
	
	public static String getMstStaffByIdSQL(Integer id)
	{
		return	"select *\n" +
				"from mst_staff ms\n" +
				"where ms.delete_date is null\n" +
				"and ms.staff_id = " + id + "\n" +
				"order by ms.display_seq\n";
	}
        
	/**
	 * �X�^�b�t�}�X�^����A�ݒ肳��Ă���X�^�b�tID�̃f�[�^��ǂݍ��ށB
	 * @return true - ����
	 */
	public boolean loadByNo(ConnectionWrapper con) throws SQLException
	{
		return	this.loadByNo(con, false);
	}
	
	
	public boolean loadByNo(ConnectionWrapper con, boolean isCheckShop) throws SQLException
	{
		boolean result = false;
		
		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByStaffNoSQL(isCheckShop));

		if(rs.next())
		{
			this.setData(rs);
			result	=	true;
		}
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * MstStaff�̃f�[�^��ǂݍ��ށB
	 * @param ms MstStaff
	 */
	public void setData( MstStaff ms )
	{
		this.setStaffID(ms.getStaffID() );
		this.setStaffNo(ms.getStaffNo() );
		this.setStaffName(0, ms.getStaffName(0) );
		this.setStaffName(1, ms.getStaffName(1) );
		this.setStaffKana(0, ms.getStaffKana(0) );
		this.setStaffKana(1, ms.getStaffKana(1) );
		this.setPostalCode(ms.getPostalCode() );
		this.setAddress(0, ms.getAddress(0) );
		this.setAddress(1, ms.getAddress(1) );
		this.setAddress(2, ms.getAddress(2) );
		this.setAddress(3, ms.getAddress(3) );
		this.setPhoneNumber(ms.getPhoneNumber() );
		this.setCellularNumber(ms.getCellularNumber() );
		this.setMailAddress(ms.getMailAddress() );
		this.setShopID(ms.getShopID() );
		this.setStaffClassID(ms.getStaffClassID() );
		this.setDisplaySeq(ms.getDisplaySeq() );
	}
	
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setStaffID(rs.getInt("staff_id"));
		this.setStaffNo(rs.getString("staff_no"));
		this.setStaffName(0, rs.getString("staff_name1"));
		this.setStaffName(1, rs.getString("staff_name2"));
		this.setStaffKana(0, rs.getString("staff_kana1"));
		this.setStaffKana(1, rs.getString("staff_kana2"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setCellularNumber(rs.getString("cellular_number"));
		this.setMailAddress(rs.getString("mail_address"));
		this.setShopID(rs.getInt("shop_id"));
		this.setStaffClassID(rs.getInt("staff_class_id"));
                
                int dispSeq = rs.getInt("display_seq");
		this.setDisplaySeq( rs.wasNull() ? null : dispSeq);

                try {
                    this.setDisplay(rs.getBoolean("display"));
                } catch (Exception e) {
                }
                
                // vtbphuong start add 20150513
                this.setDisplayName(rs.getString("display_name"));
                // vtbphuong end add 20150513 
	}
	
// 2017/01/04 �ڋq���� ADD START
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSet rs) throws SQLException
	{
		this.setStaffID(rs.getInt("staff_id"));
		this.setStaffNo(rs.getString("staff_no"));
		this.setStaffName(0, rs.getString("staff_name1"));
		this.setStaffName(1, rs.getString("staff_name2"));
		this.setStaffKana(0, rs.getString("staff_kana1"));
		this.setStaffKana(1, rs.getString("staff_kana2"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setCellularNumber(rs.getString("cellular_number"));
		this.setMailAddress(rs.getString("mail_address"));
		this.setShopID(rs.getInt("shop_id"));
		this.setStaffClassID(rs.getInt("staff_class_id"));
                
                int dispSeq = rs.getInt("display_seq");
		this.setDisplaySeq( rs.wasNull() ? null : dispSeq);

                try {
                    this.setDisplay(rs.getBoolean("display"));
                } catch (Exception e) {
                }
                
                // vtbphuong start add 20150513
                this.setDisplayName(rs.getString("display_name"));
                // vtbphuong end add 20150513 
	}
// 2017/01/04 �ڋq���� ADD END
        
	/**
	 * �X�^�b�t�}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//if(this.getStaffID() == null || this.getStaffID().equals(""))	return	false;
		
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
	 * �X�^�b�t�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * �X�^�b�t�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getStaffID() == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        
	/**
	 * �X�^�b�t�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper, result�@�X��=true ,�{��=false 
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExistStaff(ConnectionWrapper con, boolean result) throws SQLException
	{
		if(this.getStaffID() == null)	return	false;
		
		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByStaffIDSQL(result));

		if(rs.next())	return	true;
		else	return	false;
	}
	/**
	 * �X�^�b�t�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExistsByStaffNo(ConnectionWrapper con) throws SQLException
	{
		if(this.getStaffNo() == null || this.getStaffNo().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByStaffNoSQL(false));

		if(rs.next())	return	true;
		else	return	false;
	}
	//IVS_LVTu start add 2014/08/05 Mashu_���ɊǗ��t�H�[�}�b�g��荞��
        /**
         * check data exists
         * @param con ConnectionWrapper
         * @return boolean.
         * @throws SQLException java.sql.SQLException SQLException
         */
        public boolean isExistsByStaffNoByShopID(ConnectionWrapper con) throws SQLException
	{

		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByStaffNoByShopIDSQL());
		if(rs.next())
                {
                    this.setStaffID(rs.getInt("staff_id"));
                    return	true;
                }
		else	return	false;
	}
        /**
         * get string.
         * @return String.
         */
        private String getSelectByStaffNoByShopIDSQL()
	{
		return	"select *\n" +
				"from mst_staff\n" +
				"where staff_no = " + SQLUtil.convertForSQL(this.getStaffNo()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
                                "and delete_date is null ";
	}
        //IVS_LVTu end add 2014/08/05 Mashu_���ɊǗ��t�H�[�}�b�g��荞��
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_staff\n" +
				"where	staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + "\n";
	}
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectByStaffIDSQL(boolean isCheckShop)
	{
		return	"select *\n" +
				"from mst_staff\n" +
				"where	staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + "\n" +
				(isCheckShop ? "and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" : "");
	}
        
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectByStaffNoSQL(boolean isCheckShop)
	{
		return	"select *\n" +
				"from mst_staff\n" +
				"where delete_date is null and staff_no = " + SQLUtil.convertForSQL(this.getStaffNo()) + "\n" +
				(isCheckShop ? "and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" : "");
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_staff\n" +
				"(staff_id, staff_no, staff_name1, staff_name2, staff_kana1, staff_kana2,\n" +
				"postal_code, address1, address2, address3, address4,\n" +
				"phone_number, cellular_number, mail_address,\n" +
				"shop_id, staff_class_id, display_seq,\n" +
                                // vtbphuong start add 20150513  
                                 "display_name,\n" +
                                // vtbphuong end add 20150513 
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(ms.staff_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getStaffNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffName(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffName(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffKana(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffKana(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getCellularNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffClassID()) + ",\n" +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + ",\n" +
                                // vtbphuong start add 20150513 
                                SQLUtil.convertForSQL(this.getDisplayName()) + ",\n" +
                                // vtbphuong end add 20150513 
				"current_timestamp, current_timestamp, null\n" +
				"from mst_staff ms";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_staff\n" +
				"set\n" +
				"staff_no = " + SQLUtil.convertForSQL(this.getStaffNo()) + ",\n" +
				"staff_name1 = " + SQLUtil.convertForSQL(this.getStaffName(0)) + ",\n" +
				"staff_name2 = " + SQLUtil.convertForSQL(this.getStaffName(1)) + ",\n" +
				"staff_kana1 = " + SQLUtil.convertForSQL(this.getStaffKana(0)) + ",\n" +
				"staff_kana2 = " + SQLUtil.convertForSQL(this.getStaffKana(1)) + ",\n" +
				"postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				"address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				"address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				"address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				"address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				"phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				"cellular_number = " + SQLUtil.convertForSQL(this.getCellularNumber()) + ",\n" +
				"mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				"staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + ",\n" +
				"display_seq = " + SQLUtil.convertForSQL(this.getDisplaySeq()) + ",\n" +
                                // vtbphuong start add 20150513
                                "display_name = " + SQLUtil.convertForSQL(this.getDisplayName()) + ",\n" +
                                // vtbphuong end add 20150513
				"update_date = current_timestamp\n" +
				"where	staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_staff\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where	staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + "\n";
	}
	
	
	/**
	 * ���i�O�j�̃X�^�b�t�Ɉړ�����B
	 * @param con ConnectionWrapper
	 * @param isNext true - ���Afalse - �O
	 */
	public void moveStaff(ConnectionWrapper con, boolean isNext) throws SQLException
	{
		if(con == null)
		{
			return;
		}
		ResultSetWrapper	rs	=	con.executeQuery(this.getNextStaffID(isNext));

		if(rs.next())
		{
			this.setData(rs);
		}

		rs.close();
		rs	=	null;
	}
	
	/**
	 * ���i�O�j�̃X�^�b�t���擾����r�p�k�����擾����B
	 * @param isNext true - ���Afalse - �O
	 * @return ���i�O�j�̃X�^�b�t���擾����r�p�k��
	 */
	private String getNextStaffID(boolean isNext)
	{
		//�X�^�b�tID���ݒ肳��Ă��Ȃ��ꍇ�A��ԍŏ��̃X�^�b�tID���擾
		if(this.getStaffID() == null || this.getStaffID().equals(""))
		{
			return	"select *\n" +
					"from mst_staff\n" +
					"where staff_id = (\n" +
					"select min(staff_id) as staff_id\n" +
					"from mst_staff\n" +
					"where delete_date is null\n" +
					"and display_seq = (\n" +
					"select min(display_seq)\n" +
					"from mst_staff\n" +
					"where delete_date is null))\n" ;
		}
		else
		{
			//���̃X�^�b�t
			if(isNext)
			{
				return	"select * from mst_staff \n" +
                                        "where staff_id = (\n" +
                                        "select coalesce(min(staff_id),(\n" +
                                        "select min(staff_id) as staff_id from mst_staff\n" +
                                        "where delete_date is null\n" +
                                        "and shop_id = (\n" +
                                        "select min(shop_id) from mst_staff\n" +
                                        "where delete_date is null \n" +
                                        "and shop_id > "  + this.getShopID()+ ") \n" +
                                        "), \n" +
                                        "(select min(staff_id) as staff_id from mst_staff \n" +
                                        "where delete_date is null \n" +
                                        "and shop_id = ( \n" +
                                        "select min(shop_id) from mst_staff \n" +
                                        "where delete_date is null) \n" +
                                        ") \n" +
                                        ") as staff_id \n" +
                                        "from mst_staff \n" +
                                        "where delete_date is null \n" +
                                        "and shop_id = ( \n" +
                                        "select min(shop_id) from mst_staff \n" +
                                        "where delete_date is null \n" +
                                        "and shop_id >= "  + this.getShopID()+ ") \n" +
                                        "and staff_id > "  + SQLUtil.convertForSQL(this.getStaffID()) + " \n" +
                                        ") \n";

			}
			//�O�̃X�^�b�t
			else
			{
				return	"select * from mst_staff \n" +
                                        "where staff_id = (\n" +
                                        "select coalesce(max(staff_id),(\n" +
                                        "select max(staff_id) as staff_id from mst_staff\n" +
                                        "where delete_date is null\n" +
                                        "and shop_id = (\n" +
                                        "select max(shop_id) from mst_staff\n" +
                                        "where delete_date is null \n" +
                                        "and shop_id < "  + this.getShopID()+ ") \n" +
                                        "), \n" +
                                        "(select max(staff_id) as staff_id from mst_staff \n" +
                                        "where delete_date is null \n" +
                                        "and shop_id = ( \n" +
                                        "select max(shop_id) from mst_staff \n" +
                                        "where delete_date is null) \n" +
                                        ") \n" +
                                        ") as staff_id \n" +
                                        "from mst_staff \n" +
                                        "where delete_date is null \n" +
                                        "and shop_id = ( \n" +
                                        "select max(shop_id) from mst_staff \n" +
                                        "where delete_date is null \n" +
                                        "and shop_id <= "  + this.getShopID()+ ") \n" +
                                        "and staff_id < "  + SQLUtil.convertForSQL(this.getStaffID()) + " \n" +
                                        ") \n";
			}
		}
	}
	
	/**
	 * 
	 * @param cb 
	 */
	public static void addStaffDataToJComboBox(ConnectionWrapper con, JComboBox cb, Integer shopID) throws SQLException
	{
            addStaffDataToJComboBox(con, cb, shopID.toString());
	}
	
	/**
	 * 
	 * @param cb 
	 */
	public static void addStaffDataToJComboBox(ConnectionWrapper con, JComboBox cb, String shopList) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(MstStaff.getSelectSQL(shopList));

		while(rs.next())
		{
                    MstStaff ms = new MstStaff();
                    ms.setData(rs);
                    cb.addItem(ms);
		}
		
		rs.close();
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private static String getSelectSQL(String shopIDList)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     *");
            sql.append(" from");
            sql.append("     mst_staff");
            sql.append(" where");
            sql.append("         delete_date is null");
            sql.append("     and shop_id in (" + shopIDList + ")");
            sql.append(" order by");
            sql.append("      case when shop_id in (" + shopIDList + ") then 0 else 1 end");
            sql.append("     ,shop_id");
            sql.append("     ,display_seq");
            sql.append("     ,lpad(staff_no, 10, '0')");
            sql.append("     ,staff_id");

            return sql.toString();
	}
        
        //IVS_vtnhan start add 20140711 MASHU_�X�^�b�t���o�^
       /**
        * select max staff_id in table mst_staff
        * @param con
        * @return
        * @throws SQLException 
        */
	public Integer selectMaxStaffId(ConnectionWrapper con) throws SQLException
	{
            
                Integer maxStaffId = null;
		ResultSetWrapper rs = con.executeQuery(MstStaff.getMaxStaffId());
                while (rs.next()) {
                    maxStaffId = (rs.getInt("staff_id"));
                }
                
                return	maxStaffId;
		
	}
        
        
        
        /**
         * get max staff_id sql
         * @return 
         */
        private static String getMaxStaffId()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     coalesce(max(staff_id),1) as staff_id ");
            sql.append(" from");
            sql.append("     mst_staff");
            
            return sql.toString();
	}
        //IVS_vtnhan end add 20140711 MASHU_�X�^�b�t���o�^
        
        // IVS_Thanh start add 2014/07/07 Mashu_����v�\��
        /**
	 * get data staff relation
	 * @param con ConnectionWrapper,JComboBox cb,Integer shopCategoryID
	 * @throws java.sql.SQLException SQLException
	 * @author IVS_Thanh
         * @since 20140715
	 */
        public static void addRelationStaffDataToJComboBox(ConnectionWrapper con, JComboBox cb, Integer shopCategoryID, Integer shopID) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(MstStaff.getSelectRelationStaffSQL(shopCategoryID,shopID));

		while(rs.next())
		{
                    MstStaff ms = new MstStaff();
                    ms.setData(rs);
                    cb.addItem(ms);
		}
		
		rs.close();
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private static String getSelectRelationStaffSQL(Integer shopCategoryID,Integer shopID)
	{
            //IVS_vtnhan start add 20140909 Request #30487
//            StringBuilder sql = new StringBuilder(1000);
//            sql.append(" select \n");
//            sql.append("     * \n");
//            sql.append(" from \n");
//            sql.append("     mst_staff ms \n");
//            sql.append("     inner join mst_staff_relation msr using (staff_id) \n");
//            //sql.append("     inner join mst_staff_nondisplay msn on msn.staff_id = ms.staff_id and ms.shop_id = msn.shop_id \n");
//            sql.append(" where");
//            sql.append("         ms.delete_date is null");
//            sql.append("     and msr.shop_category_id = " + shopCategoryID  + " \n");
//            sql.append("     and ms.shop_id = " + shopID + " \n");
//            sql.append("     and staff_id not in (select  staff_id from mst_staff_nondisplay where shop_id = " + shopID + " ) \n");
//            sql.append(" order by \n");          
//            sql.append("     display_seq \n");
//            sql.append("     ,lpad(staff_no, 10, '0') \n");
//            sql.append("     ,ms.staff_id \n");
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select * from  (select");
            sql.append("      ms.*");
            sql.append("     ,msc.staff_class_name");
            sql.append("     ,(");
            sql.append("         select 1 from mst_staff_nondisplay");
            sql.append("         where shop_id in (" + shopID + ")");
            sql.append("           and staff_id = ms.staff_id");
            sql.append("     ) is null as display");
            sql.append(" from");
            sql.append("     mst_staff ms");
            sql.append("         left outer join mst_staff_class msc");
            sql.append("                      on msc.staff_class_id = ms.staff_class_id");
            sql.append("                     and msc.delete_date is null");
            sql.append(" where");
            sql.append("     ms.delete_date is null ) t");
         
            sql.append("         inner join mst_staff_relation relation ");
            sql.append("                      on t.staff_id = relation.staff_id");
            
            sql.append(" where relation.shop_category_id = " + shopCategoryID  + " and display is true \n");
          
            sql.append(" order by");
            
            if (! shopID.equals("")) {
                sql.append("  case when t.shop_id in (" + shopID + ") then 0 else 1 end,");
            }
            
            sql.append("      t.shop_id");
            sql.append("     ,t.display_seq");
            sql.append("     ,lpad(t.staff_no, 10, '0')");
            sql.append("     ,t.staff_id");

            return sql.toString();
            //IVS_vtnhan end add 20140909 Request #30487
	}
        // IVS_Thanh end add 2014/07/07 Mashu_����v�\��
        
        //IVS_LVTu start add 2015/06/16 #37295
	private static Map<String, ArrayList<MstStaff>> staffsMap = new HashMap<String, ArrayList<MstStaff>>(0);
	public static  ArrayList<MstStaff> getStaffs(Integer shopCategoryID, Integer shopID) {
		if (staffsMap.containsKey(shopCategoryID+"_"+shopID)) {
			return staffsMap.get(shopCategoryID+"_"+shopID);
		}
		return null;
	}
	public static void putStaffs(Integer shopCategoryID, Integer shopID,  ArrayList<MstStaff> staffs) {
		staffsMap.put(shopCategoryID+"_"+shopID, staffs);
	}
    public static void addStaffDataToJComboBox(JComboBox cb, Integer shopCategoryID, Integer shopID) 
	{
		
		if (getStaffs(shopCategoryID, shopID)!=null) {
			for ( MstStaff ms: getStaffs(shopCategoryID, shopID)) {
				cb.addItem(ms);
			}
		} else {
			try {
			ConnectionWrapper cons = SystemInfo.getConnection(); 
			ResultSetWrapper rs = cons.executeQuery(MstStaff.getRelationStaffSQL(shopCategoryID,shopID));
			while(rs.next())
			{
						MstStaff ms = new MstStaff();
						ms.setStaffID(rs.getInt("staff_id"));
						ms.setStaffNo(rs.getString("staff_no"));
						ms.setStaffName(0, rs.getString("staff_name1"));
						ms.setStaffName(1, rs.getString("staff_name2"));
						try {
							ms.setDisplay(rs.getBoolean("display"));
						} catch (Exception e) {
						}
						ms.setDisplayName(rs.getString("display_name"));
						cb.addItem(ms);
			}
			rs.close();
			} catch (SQLException e) {
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private static String getRelationStaffSQL(Integer shopCategoryID,Integer shopID)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select t.staff_id, t.staff_no, t.staff_name1 , t.staff_name2, t.display_name from  (select");
            sql.append("      ms.shop_id, ms.display_seq, ms.staff_id, ms.staff_name1 , ms.staff_name2, ms.staff_no, ms.display_name ");
            sql.append("     ,(");
            sql.append("         select 1 from mst_staff_nondisplay");
            sql.append("         where shop_id in (" + shopID + ")");
            sql.append("           and staff_id = ms.staff_id");
            sql.append("     ) is null as display");
            sql.append(" from");
            sql.append("     mst_staff ms");
            sql.append("         left outer join mst_staff_class msc");
            sql.append("                      on msc.staff_class_id = ms.staff_class_id");
            sql.append("                     and msc.delete_date is null");
            sql.append(" where");
            sql.append("     ms.delete_date is null ) t");
            sql.append("         inner join mst_staff_relation relation ");
            sql.append("                      on t.staff_id = relation.staff_id");
            sql.append(" where relation.shop_category_id = " + shopCategoryID  + " and display is true \n");
            sql.append(" order by");
            if (! shopID.equals("")) {
                sql.append("  case when t.shop_id in (" + shopID + ") then 0 else 1 end,");
            }
            sql.append("      t.shop_id");
            sql.append("     ,t.display_seq");
            sql.append("     ,lpad(t.staff_no, 10, '0')");
            sql.append("     ,t.staff_id");

            return sql.toString();
	}
        //IVS_LVTu end add 2015/06/16 #37295
}

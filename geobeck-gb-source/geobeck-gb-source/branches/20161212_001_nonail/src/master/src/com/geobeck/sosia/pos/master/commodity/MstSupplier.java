/*
 * MstSupplier.java
 *
 * Created on 2007/04/02, 16:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;


import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import javax.swing.JComboBox;
import javax.swing.JComboBox;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;


/**
 *
 * @author katagiri
 */
public class MstSupplier
{
	/**
	 * 郵便番号の文字数の最大値
	 */
	public static int		POSTAL_CODE_MAX			=	7;
	/**
	 * 電話番号の文字数の最大値
	 */
	public static int		PHONE_NUMBER_MAX		=	15;
	/**
	 * ＦＡＸ番号の文字数の最大値
	 */
	public static int		FAX_NUMBER_MAX			=	15;
	/**
	 * メールアドレスの文字数の最大値
	 */
	public static int		MAIL_ADDRESS_MAX		=	64;
	
	/**
	 * 仕入先ID
	 */
	protected	Integer		supplierID				=	null;
	/**
	 * 仕入先No.
	 */
	protected	Integer		supplierNo				=	null;
	/**
	 * 仕入先名
	 */
	protected	String		supplierName			=	"";
	/**
	 * 仕入区分
	 */
	protected	Integer		purchaseDivision		=	0;
	/**
	 * 郵便番号
	 */
	private	String			postalCode			=	"";
	/**
	 * 住所
	 */
	private	String[]		address				=	{"", "", "", ""};
	/**
	 * 電話番号
	 */
	private	String			phoneNumber			=	"";
	/**
	 * ＦＡＸ番号
	 */
	private	String			faxNumber			=	"";
	/**
	 * メールアドレス
	 */
	private	String			mailAddress			=	"";
	
	/** 支払サイト 締め日 */
	private Integer			cutoffDay			=	null;
	/** 支払サイト 支払月コード */
	private Integer			paymentClass		=	null;
	/** 支払サイト支払日 */
	private Integer			paymentDay			=	null;
	/**
	 * 仕入先担当者
	 */
	protected	String		supplierStaff			=	"";
	
	/** Creates a new instance of MstSupplier */
	public MstSupplier()
	{
	}

	/**
	 * 仕入先IDを取得する。
	 * @return 仕入先ID
	 */
	public Integer getSupplierID()
	{
		return supplierID;
	}

	/**
	 * 仕入先IDをセットする。
	 * @param supplierID 仕入先ID
	 */
	public void setSupplierID(Integer supplierID)
	{
		this.supplierID = supplierID;
	}

	/**
	 * 仕入先No.を取得する。
	 * @return 仕入先No.
	 */
	public Integer getSupplierNo()
	{
		return supplierNo;
	}

	/**
	 * 仕入先No.をセットする。
	 * @param supplierNo 仕入先No.
	 */
	public void setSupplierNo(Integer supplierNo)
	{
		this.supplierNo = supplierNo;
	}

	/**
	 * 仕入先No.を取得する。
	 * @return 仕入先No.
	 */
	public String getSupplierNoString()
	{
		if(supplierNo != null)
		{
			return	supplierNo.toString();
		}
		
		return "";
	}

	/**
	 * 仕入先名を取得する。
	 * @return 仕入先名
	 */
	public String getSupplierName()
	{
		return supplierName;
	}

	/**
	 * 仕入先名をセットする。
	 * @param supplierName 仕入先名
	 */
	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public Integer getPurchaseDivision()
	{
		return purchaseDivision;
	}

	public void setPurchaseDivision(Integer purchaseDivision)
	{
		this.purchaseDivision = purchaseDivision;
	}

	public String getPurchaseDivisionName()
	{
		return	MstSupplier.getPurchaseDivisionName(this.purchaseDivision);
	}

	public static String getPurchaseDivisionName(Integer purchaseDivision)
	{
		switch(purchaseDivision)
		{
			case 0:
				return	"掛仕入";
			case 1:
				return	"現金仕入";
		}
		
		return	"";
	}

	/**
	 * 郵便番号を取得する。
	 * @return 郵便番号
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * 郵便番号を取得する。
	 * @return 郵便番号
	 */
	public String getPostalCodeWithHyphen()
	{
		if(postalCode.equals(""))
		{
			return	"";
		}
		else
		{
			return postalCode.substring(0,3) + "-" + postalCode.substring(3);
		}
	}

	/**
	 * 郵便番号をセットする。
	 * @param postalCode 郵便番号
	 */
	public void setPostalCode(String postalCode)
	{
		if (postalCode == null) {
                    this.postalCode = "       ";
                } else if (postalCode.length() <= MstSupplier.POSTAL_CODE_MAX) {
                    this.postalCode = (postalCode + "       ").substring(0, 7);
		} else {
                    this.postalCode = postalCode.substring(0, MstSupplier.POSTAL_CODE_MAX);
		}
	}

	/**
	 * 住所を取得する。
	 * @return 住所
	 */
	public String[] getAddress()
	{
		return address;
	}

	/**
	 * 住所を取得する。
	 * @param index インデックス
	 * @return 住所
	 */
	public String getAddress(int index)
	{
		return address[index];
	}

	/**
	 * 住所
	 * @param address 住所
	 */
	public void setAddress(String[] address)
	{
		this.address = address;
	}

	/**
	 * 住所をセットする。
	 * @param index インデックス
	 * @param address 住所
	 */
	public void setAddress(int index, String address)
	{
		this.address[index] = address;
	}
	
	/**
	 * 住所を取得する。
	 * @return 住所
	 */
	public String getFullAddress()
	{
		return	(address[0] == null ? "" : address[0]) +
				(address[1] == null ? "" : address[1]) +
				(address[2] == null ? "" : address[2]) +
				(address[3] == null ? "" : address[3]);
	}

	/**
	 * 電話番号を取得する。
	 * @return 電話番号
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * 電話番号をセットする。
	 * @param phoneNumber 電話番号
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		if(phoneNumber == null || phoneNumber.length() <= MstSupplier.PHONE_NUMBER_MAX)
		{
			this.phoneNumber	=	phoneNumber;
		}
		else
		{
			this.phoneNumber	=	phoneNumber.substring(0, MstSupplier.PHONE_NUMBER_MAX);
		}
	}

	/**
	 * ＦＡＸ番号を取得する。
	 * @return ＦＡＸ番号
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * ＦＡＸ番号をセットする。
	 * @param faxNumber ＦＡＸ番号
	 */
	public void setFaxNumber(String faxNumber)
	{
		if(faxNumber == null || faxNumber.length() <= MstSupplier.FAX_NUMBER_MAX)
		{
			this.faxNumber	=	faxNumber;
		}
		else
		{
			this.faxNumber	=	faxNumber.substring(0, MstSupplier.FAX_NUMBER_MAX);
		}
	}

	/**
	 * メールアドレスを取得する。
	 * @return メールアドレス
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * メールアドレスをセットする。
	 * @param mailAddress メールアドレス
	 */
	public void setMailAddress(String mailAddress)
	{
		if(mailAddress == null || mailAddress.length() <= MstSupplier.MAIL_ADDRESS_MAX)
		{
			this.mailAddress	=	mailAddress;
		}
		else
		{
			this.mailAddress	=	mailAddress.substring(0, MstSupplier.MAIL_ADDRESS_MAX);
		}
	}
	
	/**
	 * MstSupplierからデータをセットする。
	 * @param mg MstSupplier
	 */
	public void setData(MstSupplier mg)
	{
		this.setSupplierID(mg.getSupplierID());
		this.setSupplierNo(mg.getSupplierNo());
		this.setSupplierName(mg.getSupplierName());
		this.setPurchaseDivision(mg.getPurchaseDivision());
		this.setPostalCode(mg.getPostalCode());
		this.setAddress(mg.getAddress());
		this.setPhoneNumber(mg.getPhoneNumber());
		this.setFaxNumber(mg.getFaxNumber());
		this.setMailAddress(mg.getMailAddress());
		this.setCutoffDay(mg.getCutoffDay());
		this.setPaymentClass(mg.getPaymentClass());
		this.setPaymentDay(mg.getPaymentDay());
		this.setSupplierStaff(mg.getSupplierStaff());
	}
	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException 例外
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setSupplierID(rs.getInt("supplier_id"));
		this.setSupplierNo(rs.getInt("supplier_no"));
		this.setSupplierName(rs.getString("supplier_name"));
		this.setPurchaseDivision(rs.getInt("purchase_division"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setFaxNumber(rs.getString("fax_number"));
		this.setMailAddress(rs.getString("mail_address"));
		this.setCutoffDay(rs.getInt("cutoff_day"));
		this.setPaymentClass(rs.getInt("payment_class"));
		this.setPaymentDay(rs.getInt("payment_day"));
		this.setSupplierStaff(rs.getString("supplier_staff"));
	}
	
	/**
	 * 文字列（仕入先名）を取得する。
	 * @return 文字列（仕入先名）
	 */
	public String toString()
	{
		return	this.getSupplierName();
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof MstSupplier)
		{
			if(((MstSupplier)obj).getSupplierID() == this.getSupplierID())
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * 仕入先マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSupplierID() == null || this.getSupplierID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 仕入先マスタに同一仕入先Noが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws SQLException DBアクセス失敗時
	 * @return true - 存在する
	 */
	public boolean existsSameSupplierNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByNoSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * データベースからデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			this.setData(rs);
		}
		
		rs.close();
		
		return	true;
	}
	
	/**
	 * データを取得するＳＱＬ文を取得する。
	 * @return データを取得するＳＱＬ文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" +
				"where supplier_id = " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n" +
				"and delete_date is null\n";
	}
	
	/**
	 * データを取得するＳＱＬ文を取得する。
	 * @return データを取得するＳＱＬ文
         *200809
	 */
	private static String getSelectAllSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" ;
	}
	
	/**
	 * データベースからデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
	 */
	public boolean loadByNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByNoSQL());
		
		if(rs.next())
		{
			this.setData(rs);
		}
		
		rs.close();
		
		return	true;
	}
	
	/**
	 * データを取得するＳＱＬ文を取得する。
	 * @return データを取得するＳＱＬ文
	 */
	private String getSelectByNoSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" +
				"where supplier_no = " + SQLUtil.convertForSQL(this.getSupplierNo()) + "\n" +
				"and delete_date is null\n";
	}
	
	
	/**
	 * 仕入先マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			return	(con.executeUpdate(this.getUpdateSQL()) == 1);
		}
		else
		{
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
			
			this.setSupplierID(getMaxSupplierID(con));
			
			return	true;
		}
	}
	
	
	/**
	 * 仕入先マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			if(con.executeUpdate(this.getDeleteSQL()) == 1)
			{
				return	true;
			}
		}
		
		return	false;
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_supplier\n" +
				"select\n" +
				"coalesce(max(supplier_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getSupplierNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getSupplierName()) + ",\n" +
				SQLUtil.convertForSQL(this.getPurchaseDivision()) + ",\n" +
				SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"current_timestamp, current_timestamp, null,\n" +
				SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentClass()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentDay()) + ",\n" +
				SQLUtil.convertForSQL(this.getSupplierStaff()) + "\n" +
				"from mst_supplier\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_supplier\n" +
				"set\n" +
				"supplier_no = " + SQLUtil.convertForSQL(this.getSupplierNo()) + ",\n" +
				"supplier_name = " + SQLUtil.convertForSQL(this.getSupplierName()) + ",\n" +
				"purchase_division = " + SQLUtil.convertForSQL(this.getPurchaseDivision()) + ",\n" +
				"postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				"address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				"address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				"address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				"address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				"phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				"fax_number = " + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				"mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"update_date = current_timestamp,\n" +
				"cutoff_day = " + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
				"payment_class = " + SQLUtil.convertForSQL(this.getPaymentClass()) + ",\n" +
				"payment_day = " + SQLUtil.convertForSQL(this.getPaymentDay()) + ",\n" +
				"supplier_staff = " + SQLUtil.convertForSQL(this.getSupplierStaff()) + "\n" +
				"where	supplier_id = " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_supplier\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where	supplier_id = " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n";
	}
	
	/**
	 * 仕入先IDの最大値を取得する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public static Integer getMaxSupplierID(ConnectionWrapper con) throws SQLException
	{
		Integer		result	=	null;
		
		if(con == null)	return	result;
		
		ResultSetWrapper	rs	=	con.executeQuery(getMaxSupplierIDSQL());

		if(rs.next())
		{
			result	=	rs.getInt("max_supplier_id");
		}
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * 
	 * @return 
	 */
	private static String getMaxSupplierIDSQL()
	{
		return	"select max(supplier_id) as max_supplier_id\n" +
				"from mst_supplier";
	}
	
	
	public boolean checkSupplierNo(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getCheckSupplierNoSQL());
		
		if(rs.next())
		{
			result	=	(rs.getInt("cnt") == 0);
		}
		else
		{
			result	=	true;
		}
		
		rs.close();
		
		return	result;
	}
	
	public String getCheckSupplierNoSQL()
	{
		return	"select count(*) as cnt\n" +
				"from mst_supplier\n" +
				"where delete_date is null\n" +
				"and supplier_no = " + SQLUtil.convertForSQL(this.getSupplierNo()) + "\n" +
				(this.getSupplierID() != null && 0 < this.getSupplierID() ?
					"and supplier_id != " + SQLUtil.convertForSQL(this.getSupplierID()) + "\n" :
					"");
	}
	
	public Integer getCutoffDay()
	{
		return this.cutoffDay;
	}
	
	public void setCutoffDay(Integer cutoffDay)
	{
		this.cutoffDay = cutoffDay;
	}
	
	public Integer getPaymentClass()
	{
		return this.paymentClass;
	}
	
	public void setPaymentClass(Integer paymentClass)
	{
		this.paymentClass = paymentClass;
	}
	
	public Integer getPaymentDay()
	{
		return this.paymentDay;
	}
	
	public void setPaymentDay(Integer paymentDay)
	{
		this.paymentDay = paymentDay;
	}
        
        public String getSupplierStaff() {
                return supplierStaff;
        }

        public void setSupplierStaff(String supplierStaff) {
                this.supplierStaff = supplierStaff;
        }
        
        /**
	 * 200809
	 * @param cb 
	 */
	public static void addSupplierDataToJComboBox(ConnectionWrapper con, JComboBox cb) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(MstSupplier.getSelectAllSQL());
		
		while(rs.next())
		{
			MstSupplier	ms	=	new MstSupplier();
			ms.setData(rs);
			
			cb.addItem(ms);
		}
		
		rs.close();
	}

}

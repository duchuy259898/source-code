/*
 * DataAdjustment.java
 *
 * Created on 2011/07/19, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

/**
 *
 * @author 
 */
public class DataCustomer
{
        private	Integer				customerId		=	null;
	private	String				customerNo		=	null;
        private	String				customerName		=	null;
        private	String				postalcode		=	null;
        private	String				address                 =	null;
        private	String				phoneNumber             =	null;
        private	String				cellularNumber          =	null;
	private	java.util.Date                  birthday		=	null;
        private	Integer				targetNumber            =	null;
        private	Integer				customerId_D		=	null;
	private	String				customerNo_D		=	null;
        private	String				customerName_D		=	null;
        private	Integer				visitNumber_D           =	null;
        private String                          postalcode_D            =	null;
        private String                          customer_kana_D         =	null;
	private	String				address1_D              =	null;
        private	String				address2_D              =	null;
        private	String				address3_D              =	null;
        private	String				address4_D              =	null;
        private	String				phoneNumber_D           =	null;
        private	String				cellularNumber_D        =	null;
	private	java.util.Date                  birthday_D		=	null;
        private	String                          pc_mail_address_D       =	null;
        private	String                          cellular_mail_address_D =	null;
        private	String                          sex_D                   =	null;
        private	String                          job_D                   =	null;
        private	String                          note_D                  =	null;

	/** Creates a new instance of DataCustomer */
	public DataCustomer()
	{
	}

        //ŒÚ‹qID
	public Integer getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Integer customerid)
	{
		this.customerId = customerid;
	}

        //ŒÚ‹qNO
	public String getCustomerNo()
	{
		return customerNo;
	}

	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}

        //ŒÚ‹q–¼
	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

        //—X•Ö”Ô†
	public String getPostalcode()
	{
		return postalcode;
	}

	public void setPostalcode(String postalcode)
	{
		this.postalcode = postalcode;
	}

        //ZŠ
	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

        //“d˜b”Ô†
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phonenumber)
	{
		this.phoneNumber = phonenumber;
	}

        //Œg‘Ñ”Ô†
	public String getCellularNumber()
	{
		return cellularNumber;
	}

	public void setCellularNumber(String cellularnumber)
	{
		this.cellularNumber = cellularnumber;
	}

        //¶”NŒ“ú
	public java.util.Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(java.util.Date birthday)
	{
		this.birthday = birthday;
	}

        //‘ÎÛÒ”
	public Integer getTargetNumber()
	{
		return targetNumber;
	}

	public void setTargetNumber(Integer targetNumber)
	{
		this.targetNumber = targetNumber;
	}

        //ŒÚ‹qIDiÚ×j
	public Integer getCustomerId_D()
	{
		return customerId_D;
	}

	public void setCustomerId_D(Integer customerId)
	{
		this.customerId_D = customerId;
	}

        //ŒÚ‹qNOiÚ×j
	public String getCustomerNo_D()
	{
		return customerNo_D;
	}

	public void setCustomerNo_D(String customerNo)
	{
		this.customerNo_D = customerNo;
	}

        //ŒÚ‹q–¼iÚ×j
	public String getCustomerName_D()
	{
		return customerName_D;
	}

	public void setCustomerName_D(String customerName)
	{
		this.customerName_D = customerName;
	}

        //–K–â‰ñ”iÚ×j
	public Integer getVisitNumber_D()
	{
		return visitNumber_D;
	}

	public void setVisitNumber_D(Integer visitNumber)
	{
		this.visitNumber_D = visitNumber;
	}

        //—X•Ö”Ô†
	public String getPostalcode_D()
	{
		return postalcode_D;
	}

	public void setPostalcode_D(String postalcode)
	{
		this.postalcode_D = postalcode;
	}

        //ZŠ1
	public String getAddress1_D()
	{
		return address1_D;
	}

	public void setAddress1_D(String address)
	{
		this.address1_D = address;
	}

        //ZŠ2
	public String getAddress2_D()
	{
		return address2_D;
	}

	public void setAddress2_D(String address)
	{
		this.address2_D = address;
	}

        //ZŠ3
	public String getAddress3_D()
	{
		return address3_D;
	}

	public void setAddress3_D(String address)
	{
		this.address3_D = address;
	}

        //ZŠ4
	public String getAddress4_D()
	{
		return address4_D;
	}

	public void setAddress4_D(String address)
	{
		this.address4_D = address;
	}

        //“d˜b”Ô†
	public String getPhoneNumber_D()
	{
		return phoneNumber_D;
	}

	public void setPhoneNumber_D(String phonenumber)
	{
		this.phoneNumber_D = phonenumber;
	}

        //Œg‘Ñ”Ô†
	public String getCellularNumber_D()
	{
		return cellularNumber_D;
	}

	public void setCellularNumber_D(String cellularnumber)
	{
		this.cellularNumber_D = cellularnumber;
	}

        //¶”NŒ“ú
	public java.util.Date getBirthday_D()
	{
		return birthday_D;
	}

	public void setBirthday_D(java.util.Date birthday)
	{
		this.birthday_D = birthday;
	}

        //PCƒ[ƒ‹
	public String getPc_mail_address_D()
	{
		return pc_mail_address_D;
	}

	public void setPc_mail_address_D(String pc_mail_address)
	{
		this.pc_mail_address_D = pc_mail_address;
	}

        //Œg‘Ñƒ[ƒ‹
	public String getCellular_mail_address_D()
	{
		return cellular_mail_address_D;
	}

	public void setCellular_mail_address_D(String cellular_mail_address)
	{
		this.cellular_mail_address_D = cellular_mail_address;
	}

        //‚Ó‚è‚ª‚È
	public String getCustomerKana_D()
	{
		return customer_kana_D;
	}

	public void setCustomerKana_D(String customer_Kana)
	{
		this.customer_kana_D = customer_Kana;
	}

        //«•Ê
	public String getSex_D()
	{
		return sex_D;
	}

	public void setSex_D(String sex)
	{
		this.sex_D = sex;
	}

        //E‹Æ
	public String getJob_D()
	{
		return job_D;
	}

	public void setJob_D(String job)
	{
		this.job_D = job;
	}

        //”õl
	public String getNote_D()
	{
		return note_D;
	}

        //”õl
	public void setNote_D(String note)
	{
		this.note_D = note;
	}
}

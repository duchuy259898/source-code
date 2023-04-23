/*
 * CollectedBillData.java
 *
 * Created on 2007/06/14, 19:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.data.account.*;

/**
 *
 * @author katagiri
 */
public class CollectedBillData extends DataPayment
{
	private	GregorianCalendar	salesDate			=	null;
	private MstCustomer			customer			=	null;
	private Long				collectedValue		=	0l;
        private	java.util.Date	salesDate1			=	null;
        
        //IVS_LVTu start add 2015/10/23 New request #43755
        private boolean flagScreenBill  = false;
        private long cashValue          =   0l;
        private long cardValue          =   0l;
        private long ecashValue         =   0l;
        private long vouchersValue      =   0l;

        public long getCashValue() {
            return cashValue;
        }

        public void setCashValue(long cashValue) {
            this.cashValue = cashValue;
        }

        public long getCardValue() {
            return cardValue;
        }

        public void setCardValue(long cardValue) {
            this.cardValue = cardValue;
        }

        public long getEcashValue() {
            return ecashValue;
        }

        public void setEcashValue(long ecashValue) {
            this.ecashValue = ecashValue;
        }

        public long getVouchersValue() {
            return vouchersValue;
        }

        public void setVouchersValue(long vouchersValue) {
            this.vouchersValue = vouchersValue;
        }

        public boolean isFlagScreenBill() {
            return flagScreenBill;
        }

        public void setFlagScreenBill(boolean flagScreenBill) {
            this.flagScreenBill = flagScreenBill;
        }
        //IVS_LVTu end add 2015/10/23 New request #43755
	
	/** Creates a new instance of CollectedBillData */
	public CollectedBillData()
	{
		super();
	}

	public GregorianCalendar getSalesDate()
	{
		return salesDate;
	}

	public void setSalesDate(GregorianCalendar salesDate)
	{
		this.salesDate = salesDate;
	}

	public MstCustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	public Long getCollectedValue()
	{
		return collectedValue;
	}

	public void setCollectedValue(Long collectedValue)
	{
		this.collectedValue = collectedValue;
	}
	
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);
               
		this.setSalesDate(rs.getGregorianCalendar("sales_date"));
		MstCustomer		mc	=	new MstCustomer();
		mc.setCustomerID(rs.getInt("customer_id"));
		mc.setCustomerNo(rs.getString("customer_no"));
		mc.setCustomerName(0, rs.getString("customer_name1"));
		mc.setCustomerName(1, rs.getString("customer_name2"));
		this.setCustomer(mc);
                //Luc start edit 20140714 [gb]îÑä|âÒé˚ÇÃâÒé˚çœÇ›É^ÉuÇÃâÒé˚ã‡äzÇÃïsîı
		//this.setCollectedValue(rs.getLong("collected_value"));
                this.setCollectedValue(rs.getLong("collected_value")-rs.getLong("change_value"));
                 //Luc start edit 20140714 [gb]îÑä|âÒé˚ÇÃâÒé˚çœÇ›É^ÉuÇÃâÒé˚ã‡äzÇÃïsîı
                //IVS_LVTu start add 2015/10/23 New request #43755
                if (this.flagScreenBill) {
                    this.cashValue      = rs.getLong("cash_value");
                    this.cardValue      = rs.getLong("card_value");
                    this.ecashValue     = rs.getLong("ecash_value");
                    this.vouchersValue  = rs.getLong("vouchers_value");
                }
                //IVS_LVTu end add 2015/10/23 New request #43755
	}
        public String getCustomerNo()
	{
	   
	    return this.getCustomer().getCustomerNo();
	}
        public String getCustomerName()
	{
	    return this.getCustomer().getFullCustomerName();
	}
        public String getStaffName()
	{
	    if(this.getStaff() != null) {
		return this.getStaff().getFullStaffName();
	    }

	    return null;
	}
        public Long getBill()
	{
		return this.getBillValue();
	}
        public Long getBillRest()
	{ //IVS NNTUAN START EDIT 20131028
                //return null;
            return getCollectedValue();
           //IVS NNTUAN START EDIT 20131028
	}
         
        public java.util.Date getSalesDate1()
	{
            this.salesDate1=salesDate.getTime();
            return this.salesDate1;
	}
        
}

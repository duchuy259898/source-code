/*
 * AccountData.java
 *
 * Created on 2006/05/19, 16:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.search.account;

import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * 伝票検索用精算データ
 * @author katagiri
 */
public class AccountData
{
	/**
	 * 伝票ヘッダデータ
	 */
	protected	DataSales			sales                       =	new DataSales(SystemInfo.getTypeID());
        
	protected	Integer                         reservationNo               =   null;
	
	protected	Long				totalValue                  =	0l;
	
	protected	Long				billValue                   =	0l;

        protected	Long				total                       =	0l;
	
	protected	Long				billTotal                   =	0l;

	protected	Long				paymentTotal                =	0l;

	protected	Long				paymentValue1               =	0l;
	protected	Long				paymentValue2               =	0l;
	protected	Long				paymentValue1Total          =	0l;
	protected	Long				paymentValue2Total          =	0l;
	protected	Integer				karteCount                  =	0;
	protected	Integer				proportionallyCount         =	0;
	protected	Integer				proportionallyInputCount    =	0;

       /**
	 * レジスタッフデータ
	 */
	protected	MstStaff			staff			=	new MstStaff();
	/**
	 * 主担当者データ
	 */
	protected	MstStaff			chargeStaff		=	new MstStaff();
        
	/**
	 * 主担当指名フラグ
	 */
        protected       Boolean                         designatedFlag         =       null;
        
	/**
	 * 支払金額
	 */
	protected	ArrayList<Long>		paymentValue	=	new ArrayList<Long>();

	/**
	 * 削除日
	 */
	protected	java.util.Date	deleteDate                              =	null;
        
	/**
	 * コンストラクタ
	 */
	public AccountData()
	{
	}

	/**
	 * 伝票ヘッダデータを取得する。
	 * @return 伝票ヘッダデータ
	 */
	public DataSales getSales()
	{
		return sales;
	}

	/**
	 * 伝票ヘッダデータをセットする。
	 * @param sales 伝票ヘッダデータ
	 */
	public void setSales(DataSales sales)
	{
		this.sales = sales;
	}

	public Long getTotalValue()
	{
		return totalValue;
	}

	public Long getBillValue()
	{
		return billValue;
	}

	public void setBillValue(Long billValue)
	{
		this.billValue = billValue;
	}

	/**
	 * レジスタッフデータを取得する。
	 * @return スタッフデータ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * レジスタッフデータをセットする。
	 * @param staff スタッフデータ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	/**
	 * 主担当者データを取得する。
	 * @return スタッフデータ
	 */
	public MstStaff getChargeStaff()
	{
		return chargeStaff;
	}

	/**
	 * 主担当者データをセットする。
	 * @param chargeStaff スタッフデータ
	 */
	public void setChargeStaff(MstStaff chargeStaff)
	{
		this.chargeStaff = chargeStaff;
	}

	/**
	 * 主担当指名フラグを取得する。
	 * @return 主担当指名フラグ
	 */
	public Boolean getDesignatedFlag()
	{
		return designatedFlag;
	}

	/**
	 * 主担当指名フラグをセットする。
	 * @param designatedFlag 主担当指名フラグ
	 */
	public void setDesignatedFlag(Boolean designatedFlag)
	{
		this.designatedFlag = designatedFlag;
	}
        
	/**
	 * 支払金額を取得する。
	 * @return 支払金額
	 */
	public ArrayList<Long> getPaymentValue()
	{
		return paymentValue;
	}

	/**
	 * 支払合計を取得する。
	 * @return 支払合計
	 */
	public Long getPaymentTotalAll()
	{
		return paymentTotal;
	}

	/**
	 * 現金支払を取得する。
	 * @return 現金支払
	 */
	public Long getPaymentValue1()
	{
	    Long result = 0l;
	    
	    if (paymentValue1 != 0l) {
                //IVS_TMTrong start edit 20150716 Bug #37392
                if(totalValue < 0){
                    result = paymentValue1;
                }else{
                            if (totalValue < getPaymentValue2()) {
                                result = totalValue;
                            } else {
                                Long tmpValue = totalValue - getPaymentValue2();
                                if (tmpValue < paymentValue1) {
                                    result = tmpValue;
                                } else {
                                    result = paymentValue1;
                                }
                            }
                }
                //IVS_TMTrong end edit 20150716 Bug #37392
	    }
	    
	    return result;
	}
	/**
	 * カード支払を取得する。
	 * @return カード支払
	 */
	public Long getPaymentValue2()
	{
	    Long result = 0l;

	    if (paymentValue2 != 0l) {

                //IVS_TMTrong start edit 20150716 Bug #37392
                if(totalValue < 0){
                    result = paymentValue2;
                } else{
                    if (totalValue < paymentValue2) {
                        result = totalValue;
                    } else {
                        result = paymentValue2;
                    }
                }
                //IVS_TMTrong end edit 20150716 Bug #37392
	    }

	    return result;
	}

	/**
	 * 現金支払合計を取得する。
	 * @return 現金支払合計
	 */
	public Long getPaymentValue1Total()
	{
		return paymentValue1Total;
	}
	/**
	 * カード支払合計を取得する。
	 * @return カード支払合計
	 */
	public Long getPaymentValue2Total()
	{
		return paymentValue2Total;
	}

        /**
	 * 按分件数フラグを取得する。
	 * @return 按分件数フラグ
	 */
	public Integer getProportionallyCount()
	{
		return proportionallyCount;
	}

        /**
	 * 按分登録済フラグを取得する。
	 * @return 按分登録済フラグ
	 */
	public Integer getProportionallyInputCount()
	{
		return proportionallyInputCount;
	}
        
	/**
	 * カルテ登録フラグを取得する。
	 * @return カルテ登録フラグ
	 */
	public Integer getKarteCount()
	{
		return karteCount;
	}
        
	/**
	 * 売掛合計を取得する。
	 * @return 売掛合計
	 */
	public Long getBillTotal()
	{
		return billTotal;
	}

	/**
	 * 請求合計を取得する。
	 * @return 請求合計
	 */
	public Long getTotal()
	{
		return total;
	}
        
	/**
	 * 削除日を取得する。
	 * @return 削除日
	 */
	public java.util.Date getDeleteDate()
	{
		return deleteDate;
	}

	/**
	 * 削除日をセットする。
	 * @param deleteDate 削除日
	 */
	public void setDeleteDate(java.util.Date deleteDate)
	{
		this.deleteDate = deleteDate;
	}
        
        public void addValue(AccountData ad, Long temp)
	{
		total			+=	ad.getTotalValue();
		billTotal		+=	ad.getBillValue();
		paymentTotal		+=	temp;

		paymentValue1Total	+=	ad.getPaymentValue1();
		paymentValue2Total	+=	ad.getPaymentValue2();
	}

        /**
	 * 支払金額をセットする。
	 * @param paymentValue 支払金額
	 */
	public void setPaymentValue(ArrayList<Long> paymentValue)
	{
		this.paymentValue = paymentValue;
	}
	
	public Long getPaymentTotal()
	{
		Long	temp	=	0l;
		
		for(Long pv : paymentValue)
		{
			temp	+=	pv;
		}
		
		if(totalValue < temp)
		{
			temp	=	totalValue;
		}
		
		return	temp;
	}
	
        public Integer getReservationNo() {
            return reservationNo;
        }

        public void setReservationNo(Integer reservationNo) {
            this.reservationNo = reservationNo;
        }
        
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @param paymentClasses 支払区分リストデータ
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs, MstPaymentClasses paymentClasses) throws SQLException
	{
		sales.getShop().setShopID(rs.getInt("shop_id"));
		sales.setSalesDate(rs.getDate("sales_date"));
		sales.setSlipNo(rs.getInt("slip_no"));
		sales.getCustomer().setCustomerID(rs.getInt("customer_id"));
		sales.getCustomer().setCustomerNo(rs.getString("customer_no"));
		sales.getCustomer().setCustomerName(0, rs.getString("customer_name1"));
		sales.getCustomer().setCustomerName(1, rs.getString("customer_name2"));
		totalValue	=	rs.getLong("total_value") - rs.getLong("alldiscount");
		billValue	=	rs.getLong("bill_value");
		staff.setStaffID(rs.getInt("staff_id"));
		staff.setStaffNo(rs.getString("staff_no"));
		staff.setStaffName(0, rs.getString("staff_name1"));
		staff.setStaffName(1, rs.getString("staff_name2"));
		setDesignatedFlag(rs.getBoolean("designated_flag"));
                
		chargeStaff.setStaffID(rs.getInt("chargeStaff_id"));
		chargeStaff.setStaffNo(rs.getString("chargeStaff_no"));
		chargeStaff.setStaffName(0, rs.getString("chargeStaff_name1"));
		chargeStaff.setStaffName(1, rs.getString("chargeStaff_name2"));

                // 現金
		this.paymentValue1 = rs.getLong("payment_value1");
                
                // 現金以外
		this.paymentValue2 = rs.getLong("payment_value2") + rs.getLong("payment_value3") + rs.getLong("payment_value4");

		this.proportionallyCount = rs.getInt("proportionally_count");
		this.proportionallyInputCount = rs.getInt("proportionally_input_count");
		this.karteCount = rs.getInt("karte_count");
                
		for(MstPaymentClass mpc : paymentClasses)
		{
			paymentValue.add(rs.getLong("payment_value" +
					mpc.getPaymentClassID().toString()));
		}
                
		this.setDeleteDate(rs.getDate("delete_date"));
                
                this.setReservationNo(rs.getInt("reservation_no"));
	}
}

/*
 * DataPayment.java
 *
 * Created on 2006/05/09, 9:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import com.geobeck.sosia.pos.master.company.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * 支払データ
 * @author katagiri
 */
public class DataPayment extends ArrayList<DataPaymentDetail>
{
	/**
	 * 店舗
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * 伝票No.
	 */
	protected	Integer			slipNo			=	null;
	/**
	 * 支払No.
	 */
	protected	Integer			paymentNo		=	null;
	/**
	 * 支払日
	 */
	protected	java.util.Date	paymentDate		=	null;
	/**
	 * スタッフ
	 */
	protected	MstStaff    staff   =	  new MstStaff();
	/**
	 * 売掛金額
	 */
	protected	Long	    billValue	=   0l;
	/**
	 * お釣り
	 */
	protected	Long	    changeValue	=   0l;
        /**
	 * 一時保存用フラグ　
	 */
        protected	boolean	    tempFlag	=   false;

	
	/**
	 * コンストラクタ
	 */
	public DataPayment()
	{
	}

	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * 店舗をセットする。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * 伝票No.を取得する。
	 * @return 伝票No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * 伝票No.をセットする。
	 * @param slipNo 伝票No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * 支払No.を取得する。
	 * @return 支払No.
	 */
	public Integer getPaymentNo()
	{
		return paymentNo;
	}

	/**
	 * 支払No.をセットする。
	 * @param paymentNo 支払No.
	 */
	public void setPaymentNo(Integer paymentNo)
	{
		this.paymentNo = paymentNo;
	}

	/**
	 * 支払日を取得する。
	 * @return 支払日
	 */
	public java.util.Date getPaymentDate()
	{
		return paymentDate;
	}

	/**
	 * 支払日をセットする。
	 * @param paymentDate 支払日
	 */
	public void setPaymentDate(java.util.Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	/**
	 * スタッフを取得する。
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフをセットする。
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	/**
	 * 売掛金額を取得する。
	 * @return 売掛金額
	 */
	public Long getBillValue()
	{
		return billValue;
	}

	/**
	 * 売掛金額をセットする。
	 * @param billValue 売掛金額
	 */
	public void setBillValue(Long billValue)
	{
		this.billValue = billValue;
	}

	public Long getChangeValue()
	{
		return changeValue;
	}

	public void setChangeValue(Long changeValue)
	{
		this.changeValue = changeValue;
	}

        /**
	 * 一時保存用フラグをセットする。
	 * @param　一時保存用フラグ
	 */
        public boolean getTempFlag()
        {
                return tempFlag ;
        }
        /**
	 * 一時保存用フラグを取得する。
	 * @param　一時保存用フラグ　
         * @return 一時保存用フラグ
         */        
        public void setTempFlag(boolean tempFlag)
        {
                this.tempFlag=tempFlag;
        }	

	
	/**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setPaymentNo(rs.getInt("payment_no"));
		this.setPaymentDate(rs.getDate("payment_date"));
		MstStaff	ms	=	new MstStaff();
		ms.setStaffID(rs.getInt("staff_id"));
		ms.setStaffName(0, rs.getString("staff_name1"));
		ms.setStaffName(1, rs.getString("staff_name2"));
		this.setStaff(ms);
		this.setBillValue(rs.getLong("bill_value"));
		this.setChangeValue(rs.getLong("change_value"));
	}
	
	/**
	 * 支払明細を追加する。
	 * @param mpc 支払区分
	 * @param mpm 支払方法
	 * @param value 金額
	 */
	public void addPaymentDetail(
			MstPaymentClass mpc,
			MstPaymentMethod mpm,
			Long value)
	{
		DataPaymentDetail dpd = new DataPaymentDetail();

		dpd.setPaymentMethod(mpm);

                if(mpc != null && dpd.getPaymentMethod() != null)
				dpd.getPaymentMethod().setPaymentClass(mpc);
		dpd.setPaymentValue(value);
		
		this.add(dpd);
	}
	
	/**
	 * 支払明細をセットする。
	 * @param index インデックス
	 * @param mpc 支払区分
	 * @param mpm 支払方法
	 * @param value 支払金額
	 */
	public void setPaymentDetail(int index,
			MstPaymentClass mpc,
			MstPaymentMethod mpm,
			Long value)
	{
		if(0 <= index && index < this.size())
		{
                    DataPaymentDetail dpd = this.get(index);

                    dpd.setPaymentMethod(mpm);
                    if(mpc != null && dpd.getPaymentMethod() != null) {
                        dpd.getPaymentMethod().setPaymentClass(mpc);
                    }
                    dpd.setPaymentValue(value);
		}
	}
	
	/**
	 * 支払金額の合計を取得する。
	 * @return 支払金額の合計
	 */
	public long getPaymentTotal()
	{
            Long total = 0l;

            //IVS_LVTu start edit 2016/06/21 Bug #51065
            for (DataPaymentDetail dpd : this) {
                //if(dpd.getPaymentMethod() != null){
                if(dpd.getPaymentValue() != null)
                    total += dpd.getPaymentValue();
                //}        
            }
            //IVS_LVTu end edit 2016/06/21 Bug #51065

            return total;
	}
	
	/**
	 * 支払金額の合計を取得する。
	 * @param paymentClass 支払区分
	 * @return 支払金額の合計
	 */
	public long getPaymentTotal(int paymentClass)
	{
		long	total	=	0;
		
		for(DataPaymentDetail dpd : this)
		{
			if(dpd.getPaymentMethod() != null &&
					dpd.getPaymentMethod().getPaymentClassID() == paymentClass)
			{
				total	+=	dpd.getPaymentValue();
			}
		}
		
		return	total;
	}
	
	/**
	 * 支払データと、明細を全て取得する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 */
	public boolean loadAll(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllSQL());

		if(rs.next())
		{
			this.setData(rs);

			rs.beforeFirst();

			while(rs.next())
			{
				DataPaymentDetail	dpd	=	new DataPaymentDetail();
				dpd.setData(rs);
                                
				this.add(dpd);
			}
		}

		rs.close();
		
		return	true;
	}
	
	
	/**
	 * 新しい支払No.をセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewPaymentNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewPaymentNoSQL());
		
		if(rs.next())
		{
			this.setPaymentNo(rs.getInt("new_payment_no"));
		}
		
		rs.close();
	}
	
	/**
	 * 支払データと、明細を全て登録する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con) throws SQLException
	{
		if(!this.regist(con))	return	false;

		//回収明細テーブル
                boolean flg = false;
		for(DataPaymentDetail dpd : this)
		{
                        //金額がない
			if(dpd.getPaymentValue() == null ||
					dpd.getPaymentValue() == 0)	continue;
                        
                        //同じpayment_noは１つ現金しか登録 - New request #35011
			if(dpd.paymentMethod.getPaymentClassID()==1 && flg) continue;
			
                        dpd.setShop(this.getShop());
			dpd.setSlipNo(this.getSlipNo());
			dpd.setPaymentNo(this.getPaymentNo());
			dpd.setNewSlipDetailNo(con);
			if(!dpd.regist(con))	return	false;			
                        dpd.setNewSlipDetailNo(con);
                        if(dpd.paymentMethod.getPaymentClassID()==1) {
                            flg = true;
                        }

		}
		
		return	true;
	}
	
	
	/**
	 * 支払データを登録する。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
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
	 * 支払データが存在するかを取得する。
	 * @param con ConnectionWrapper
	 * @return true - 存在する
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	/**
	 * 新しい支払No.を取得するＳＱＬ文を取得する。
	 * @return 新しい支払No.を取得するＳＱＬ文
	 */
	public String getNewPaymentNoSQL()
	{
		return	"select coalesce(max(payment_no), 0) + 1 as new_payment_no\n" +
				"from data_payment\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * 支払データを取得するＳＱＬ文を取得する。
	 * @return 支払データを取得するＳＱＬ文
	 */
	public String getSelectSQL()
	{
		return	"select *\n" +
				"from data_payment\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo());
	}
	
	/**
	 * 支払データをInsertするＳＱＬ文を取得する。
	 * @return 支払データをInsertするＳＱＬ文
	 */
	public String getInsertSQL()
	{
		return	"insert into data_payment\n" +
				"(shop_id, slip_no, payment_no, payment_date,\n" +
				"staff_id, bill_value, change_value,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentNo()) + ",\n" +
				SQLUtil.convertForSQLDateOnly(this.getPaymentDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				(this.getTempFlag() ? "0" : 
                                        SQLUtil.convertForSQL(this.getBillValue(), "0")) + ",\n" +
				SQLUtil.convertForSQL(this.getChangeValue(), "0") + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * 支払データをUpdateするＳＱＬ文を取得する。
	 * @return 支払データをUpdateするＳＱＬ文
	 */
	public String getUpdateSQL()
	{
		return	"update data_payment\n" +
				"set\n" +
				"payment_date = " + SQLUtil.convertForSQLDateOnly(this.getPaymentDate()) + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				"bill_value = " +( this.getTempFlag() ? "0" :
                                        SQLUtil.convertForSQL(this.getBillValue(), "0")) + ",\n" +
				"change_value = " + SQLUtil.convertForSQL(this.getChangeValue(), "0") + ",\n" +
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo());
	}
	
	
	/**
	 * 支払データを論理削除するＳＱＬ文を取得する。
	 * @return 支払データを論理削除するＳＱＬ文
	 */
	public String getSelectAllSQL()
	{
		return	"select dp.*,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"dpd.payment_detail_no,\n" +
				"mpm.payment_class_id,\n" +
				"mpc.payment_class_name,\n" +
				"dpd.payment_method_id,\n" +
				"mpm.payment_method_name,\n" +
				"mpm.prepaid,\n" +
				"dpd.payment_value\n" +
				"from data_payment dp\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dp.staff_id\n" +
				"left outer join data_payment_detail dpd\n" +
				"on dpd.shop_id = dp.shop_id\n" +
				"and dpd.slip_no = dp.slip_no\n" +
				"and dpd.payment_no = dp.payment_no\n" +
				"and dpd.delete_date is null\n" +
				"left outer join mst_payment_method mpm\n" +
				"on mpm.payment_method_id = dpd.payment_method_id\n" +
				"left outer join mst_payment_class mpc\n" +
				"on mpc.payment_class_id = mpm.payment_class_id\n" +
				"where dp.delete_date is null\n" +
				"and dp.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dp.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and dp.payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo()) + "\n";
	}

}

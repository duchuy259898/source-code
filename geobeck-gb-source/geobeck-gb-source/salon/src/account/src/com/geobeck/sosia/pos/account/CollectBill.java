/*
 * CollectBill.java
 *
 * Created on 2006/05/17, 20:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.data.account.DataSales;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * 売掛金回収処理
 * @author katagiri
 */
public class CollectBill
{
	/**
	 * 伝票データ
	 */
	protected DataSales sales = new	DataSales(SystemInfo.getTypeID());
	/**
	 * 支払データ
	 */
	protected DataPayment payment = new DataPayment();
	/**
	 * 売掛金
	 */
	protected Long billValue = 0l;
	
	/**
	 * 合計
	 */
	protected ArrayList<NameValue> total = new ArrayList<NameValue>();
	
	/**
	 * スタッフリスト
	 */
	protected MstStaffs staffs = null;
	
        protected Integer shopID = null;

        public Integer getShopID() {
            return shopID;
        }

        public void setShopID(Integer shopID) {
            this.shopID = shopID;
        }
	
	/** Creates a new instance of CollectBill */
	public CollectBill()
	{
	}

	/**
	 * 伝票データを取得する。
	 * @return 伝票データ
	 */
	public DataSales getSales()
	{
	    return sales;
	}

	/**
	 * 伝票データをセットする。
	 * @param sales 伝票データ
	 */
	public void setSales(DataSales sales)
	{
	    this.sales = sales;
	}

	/**
	 * 支払データを取得する。
	 * @return 支払データ
	 */
	public DataPayment getPayment()
	{
	    return payment;
	}

	/**
	 * 支払データをセットする。
	 * @param payment 支払データ
	 */
	public void setPayment(DataPayment payment)
	{
	    this.payment = payment;
	}

	/**
	 * 売掛金を取得する。
	 * @return 売掛金
	 */
	public Long getBillValue()
	{
	    return billValue;
	}

	/**
	 * 売掛金をセットする。
	 * @param billValue 売掛金
	 */
	public void setBillValue(Long billValue)
	{
	    this.billValue = billValue;
	}

	public MstStaffs getStaffs()
	{
	    if(staffs == null) {
		staffs = new MstStaffs();

		staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());

		try
		{
		    staffs.load(SystemInfo.getConnection(), true);
		}
		catch(SQLException e)
		{
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	    }
	    return staffs;
	}

	public void setStaffs(MstStaffs staffs)
	{
	    this.staffs = staffs;
	}
	
	
	/**
	 * 初期化処理を行う。
	 * @param slipNo 伝票No.
	 */
	public void init(Integer slipNo)
	{
	    sales.setSlipNo(slipNo);
	    this.load();
	    payment.setSlipNo(sales.getSlipNo());
	    this.initTotal();
	}
	
	/**
	 * データを読み込む。
	 */
	private void load()
	{
	    try
	    {
		ConnectionWrapper con = SystemInfo.getConnection();

		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

		if(rs.next()) {
		    sales.setSalesDate(rs.getDate("sales_date"));
		    sales.getCustomer().setCustomerID(rs.getInt("customer_id"));
		    sales.getCustomer().setCustomerNo(rs.getString("customer_no"));				
		    sales.getCustomer().setCustomerName(0, rs.getString("customer_name1"));
		    sales.getCustomer().setCustomerName(1, rs.getString("customer_name2"));
		    this.setBillValue(rs.getLong("bill_value"));
		}
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	}
	
	/**
	 * データ取得用のＳＱＬ文を取得する。
	 * @return データ取得用のＳＱＬ文
	 */
	private String getSelectSQL()
	{
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      ds.sales_date");
                sql.append("     ,ds.customer_id");
                sql.append("     ,mc.customer_no");
                sql.append("     ,mc.customer_name1");
                sql.append("     ,mc.customer_name2");
                //nhanvt start edit 20141110 New request #31431
                //sql.append("     ,last_dp.bill_value");
                sql.append(",(dp.bill_value - ");
                sql.append("    coalesce((SELECT sum(b.payment_value) AS payment_value ");
                sql.append("                                   FROM data_payment a ");
                sql.append("                                   INNER JOIN data_payment_detail b ON a.shop_id = b.shop_id ");
                sql.append("                                                AND a.slip_no = b.slip_no ");
                sql.append("                                                AND a.payment_no = b.payment_no ");
                sql.append("                                                AND b.payment_no <> 0 ");
                sql.append("                                                 AND b.delete_date IS NULL ");
                sql.append("                                    WHERE a.shop_id = ds.shop_id ");
                sql.append("                                            AND a.slip_no = ds.slip_no ");
                sql.append("                                            AND a.delete_date IS NULL");
                sql.append("    ),0)");
                sql.append(") AS bill_value ");
                //nhanvt end edit 20141110 New request #31431
                sql.append(" from");
                sql.append("     data_sales ds");
                sql.append("         left join mst_customer mc");
                sql.append("                on mc.customer_id = ds.customer_id");
                //nhanvt start edit 20141110 New request #31431
                sql.append("         left join data_payment dp");
                sql.append("                on dp.shop_id = ds.shop_id");
                sql.append("               and dp.slip_no = ds.slip_no");
                sql.append("               and dp.payment_no = 0");
                sql.append("               and dp.delete_date is null ");
                
                //nhanvt start edit
                /*sql.append("         left join");
                sql.append("             (");
                sql.append("                 select");
                sql.append("                     dp.*");
                sql.append("                 from");
                sql.append("                     data_payment dp");
                sql.append("                         inner join");
                sql.append("                             (");
                sql.append("                                 select");
                sql.append("                                      dp.shop_id");
                sql.append("                                     ,dp.slip_no");
                sql.append("                                     ,max(dp.payment_no) as payment_no");
                sql.append("                                 from");
                sql.append("                                     data_payment dp");
                sql.append("                                 where");
                sql.append("                                     dp.delete_date is null");
                sql.append("                                 group by");
                sql.append("                                      dp.shop_id");
                sql.append("                                     ,dp.slip_no");
                sql.append("                             ) last_dp");
                sql.append("                              on last_dp.shop_id = dp.shop_id");
                sql.append("                             and last_dp.slip_no = dp.slip_no");
                sql.append("                             and	last_dp.payment_no = dp.payment_no");
                sql.append("             ) last_dp");
                sql.append("              on last_dp.shop_id = ds.shop_id");
                sql.append("             and last_dp.slip_no = ds.slip_no");*/
                
                sql.append(" where");
                //IVS_LVTu start edit 2015/07/29 New request #41101
                if ( this.getShopID() == null ) {
                sql.append("         ds.shop_id = " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
                } else {
                    sql.append("         ds.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
                }
                //IVS_LVTu end edit 2015/07/29 New request #41101
                sql.append("     and ds.slip_no = " + SQLUtil.convertForSQL(sales.getSlipNo()));
                //sql.append("     and 0 < last_dp.bill_value");
                //nhanvt end edit 20141110 New request #31431
                sql.append(" order by");
                sql.append("      ds.sales_date");
                sql.append("     ,ds.slip_no");

                return sql.toString();
	}
	
	/**
	 * 合計を初期化する。
	 */
	private void initTotal()
	{
	    total.clear();
	    total.add(new NameValue("売掛金額", this.getBillValue()));
	    for(MstPaymentClass mpc : SystemInfo.getPaymentClasses()) {
		total.add(new NameValue(mpc.getPaymentClassName() + "支払", 0l));
	    }
	    total.add(new NameValue("お釣り", 0l));
	}
	
	/**
	 * 合計をセットする。
	 */
	public void setTotal()
	{
	    this.getTotal(0).setValue(this.getBillValue());
	    long temp = this.getBillValue();

	    int index = 1;
	    for(MstPaymentClass mpc : SystemInfo.getPaymentClasses()) {
		this.getTotal(index).setValue(this.getPayment().getPaymentTotal(mpc.getPaymentClassID()));
		if(index != 1) temp -= this.getTotal(index).getValue();
		index ++;
	    }

	    temp = this.getTotal(1).getValue() - temp;

	    this.getTotal(index).setValue(temp);

	    //売掛金をセット
	    if (temp < 0) {
		this.getPayment().setChangeValue(0l);
		this.getPayment().setBillValue(-temp);
	    } else {
		this.getPayment().setChangeValue(temp);
		this.getPayment().setBillValue(0l);
	    }
	}
	
	/**
	 * 合計を取得する。
	 * @return 合計
	 */
	public ArrayList<NameValue> getTotal()
	{
		return	total;
	}
	
	/**
	 * 合計を取得する。
	 * @param index インデックス
	 * @return 合計
	 */
	public NameValue getTotal(int index)
	{
	    if (0 <= index && index < total.size()) return total.get(index);
	    return null;
	}
	
	/**
	 * 登録処理を行う。
	 * @return true - 成功
	 */
	public boolean regist()
	{
	    boolean result = false;
		
	    try
	    {
		total.get(1).setValue(total.get(1).getValue() - total.get(total.size() - 1).getValue());

		//コネクションを取得
		ConnectionWrapper con = SystemInfo.getConnection();

		try
		{
		    //トランザクション開始
		    con.begin();

		    //回収Noを取得
		    this.getPayment().setNewPaymentNo(con);
		    if (this.getPayment().registAll(con)) {
			//トランザクションコミット
			con.commit();
			result = true;
		    }
		    else
		    {
			//トランザクションロールバック
			con.rollback();
		    }
		}
		catch(Exception e)
		{
		    //トランザクションロールバック
		    con.rollback();
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return result;
	}
}

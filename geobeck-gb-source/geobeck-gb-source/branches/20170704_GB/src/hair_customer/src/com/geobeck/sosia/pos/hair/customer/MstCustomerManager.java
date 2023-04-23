/*
 * MstCustomerManager.java
 *
 * Created on 2007/03/20, 9:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.system.*;
import javax.swing.JFrame;

/**
 *
 * @author katagiri
 */
public class MstCustomerManager extends MstCustomer
{
	/** Creates a new instance of MstCustomerManager */
	public MstCustomerManager()
	{
		super();
	}
	
	private ArrayList<MstJob>		jobList		=	new ArrayList<MstJob>();
	
	private	ArrayList<VisitRecord>	accounts	=	new ArrayList<VisitRecord>();
        
        private ArrayList<MstCustomer> customerList     =       new ArrayList<MstCustomer>();
        //nhanvt start edit 
        private MstAccountSetting	accountSetting	=	new MstAccountSetting();
        

   
        //nhanvt end edit
	public ArrayList<VisitRecord> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(ArrayList<VisitRecord> accounts)
	{
		this.accounts = accounts;
	}
	
	/**
	 * 顧客情報のリストを取得する
	 * @return 顧客情報のリスト
	 */        
        public ArrayList<MstCustomer> getCustomerList()
	{
		return customerList;
	}

	/**
	 * 顧客情報のリストをセットする
	 */
	public void setCustomerList(ArrayList<MstCustomer> customerList)
	{
		this.customerList = customerList;
	}

//        public Integer getVisitCount()
//	{
//		return	accounts.size();
//	}
	
	public Long getSalesTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getSalesTotal();
		}
		
		return	total;
	}
	
	/**
	 * 合計を取得する。
	 * @return 合計
	 */
	public Long getTechnicTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getTechnicTotal();
		}
		
		return	total;
	}
	
	/**
	 * 合計を取得する。
	 * @return 合計
	 */
	public Long getItemTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getItemTotal();
		}
		
		return	total;
	}

        //nhanvt start add
        public Long getCourseTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getCourseTotal();
		}
		
		return	total;
	}
        public Long getComputionCourseTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getComsumCourseTotal();
		}
		
		return	total;
	}
        //nhanvt end add

	/**
	 * 合計を取得する。
	 * @return 合計
	 */
	public Long getTechnicClameTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getTechnicClameTotal();
		}
		
		return	total;
	}

	/**
	 * 合計を取得する。
	 * @return 合計
	 */
	public Long getItemReturnedTotal()
	{
		Long	total	=	0l;
		
		for(VisitRecord vr : accounts)
		{
			total	+=	vr.getItemReturnedTotal();
		}
		
		return	total;
	}
        
	/**
	 * 職業マスタをデータベースから読み込む。
	 * @return true - 成功
	 */
	public boolean loadJobs()
	{
		jobList.clear();
		
		jobList.add(null);
		
		ConnectionWrapper con	=	SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(MstJob.getSelectAllSQL());
			
			while(rs.next())
			{
				MstJob	mj	=	new MstJob();
				
				mj.setData(rs);
				
				jobList.add(mj);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
	
	public boolean loadAccounts()
	{
	    accounts.clear();
	
	    ConnectionWrapper con = SystemInfo.getConnection();
		
	    try
	    {
		ResultSetWrapper rs = con.executeQuery(this.getLoadAccountsSQL());

		while(rs.next())
		{
		    VisitRecord vr = new VisitRecord(SystemInfo.getTypeID());

		    vr.setVisitRecordData(rs);

		    vr.setCustomer(this);

		    accounts.add(vr);
		}

		rs.close();
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return	true;
	}
	
	
	private String getLoadAccountsSQL()
	{
            accountSetting	=	SystemInfo.getAccountSetting();
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
	    sql.append("      mc.customer_no");
	    sql.append("     ,mc.customer_name1");
	    sql.append("     ,mc.customer_name2");
	    sql.append("     ,mc.sosia_id");
	    sql.append("     ,ds.shop_id");
	    sql.append("     ,ds.slip_no");
	    sql.append("     ,ds.sales_date");
	    sql.append("     ,ds.customer_id");
	    sql.append("     ,ds.designated_flag as charge_staff_designated_flag");
	    sql.append("     ,ds.staff_id as charge_staff_id");
	    sql.append("     ,ds.visited_memo");
	    sql.append("     ,ds.next_visit_date");
	    sql.append("     ,ms.staff_name1 as charge_staff_name1");
	    sql.append("     ,ms.staff_name2 as charge_staff_name2");
	    sql.append("     ,ds.visit_num");
	    sql.append("     ,dr.start_time");
            //メニュー表示
            if(accountSetting.getDisplayPriceType() == 1)
            {
                    sql.append("     ,ds.discount_sales_value_no_tax - dsd.consumption_value_total as sales_total");
            }
            else
            {
	    sql.append("     ,ds.discount_sales_value_in_tax - dsd.consumption_value_total as sales_total");
            }
            
	    sql.append("     ,dsd.technic_total");
	    sql.append("     ,dsd.item_total");
            sql.append("     ,dsd.course_total");
            sql.append("     ,dsd.consumption_value_total");
	    sql.append("     ,dsd.technic_clame_total");
	    sql.append("     ,dsd.item_returned_total");
	    sql.append("     ,mshop.shop_name");
	    sql.append("     ,(");
	    sql.append("         select");
	    sql.append("             min(b.reservation_datetime)");
	    sql.append("         from");
	    sql.append("             data_reservation as a");
	    sql.append("                 inner join data_reservation_detail b");
	    sql.append("                     using(shop_id, reservation_no)");
	    sql.append("         where");
	    sql.append("                 a.delete_date is null");
	    sql.append("             and b.delete_date is null");
	    sql.append("             and a.shop_id = dr.shop_id");
	    sql.append("             and a.customer_id = dr.customer_id");
	    sql.append("             and b.reservation_datetime >");
	    sql.append("                     (");
	    sql.append("                         select");
	    sql.append("                             max(reservation_datetime)");
	    sql.append("                         from");
	    sql.append("                             data_reservation_detail");
	    sql.append("                         where");
	    sql.append("                                 shop_id = dr.shop_id");
	    sql.append("                             and reservation_no = dr.reservation_no");
	    sql.append("                     )");
	    sql.append("      ) as nextReserveDate");
	    sql.append(" from");
	    sql.append("     mst_customer mc");
	    sql.append("         inner join view_data_sales_valid ds");
	    sql.append("                 on mc.customer_id = ds.customer_id");
	    sql.append("         inner join");
	    sql.append("             (");
	    sql.append("                 select");
	    sql.append("                      shop_id");
	    sql.append("                     ,slip_no");
	    sql.append("                     ,sum(case product_division when 1 then account_setting_value else 0 end) as technic_total");
	    sql.append("                     ,sum(case product_division when 2 then account_setting_value else 0 end) as item_total");
	    sql.append("                     ,sum(case product_division when 3 then account_setting_value else 0 end) as technic_clame_total");
	    sql.append("                     ,sum(case product_division when 4 then account_setting_value else 0 end) as item_returned_total");
             //nhanvt start add
            sql.append("                     ,sum(case product_division when 5 then account_setting_value else 0 end) as course_total");
            //nhanvt end add
	    sql.append("                     ,sum(case product_division when 6 then account_setting_value else 0 end) as consumption_value_total");
	    sql.append("                 from");
	    sql.append("                     view_data_sales_detail_valid");
	    sql.append("                 where");
	    sql.append("                     customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()));
	    sql.append("                 group by");
	    sql.append("                      shop_id");
	    sql.append("                     ,slip_no");
	    sql.append("             ) dsd");
	    sql.append("             on ds.shop_id = dsd.shop_id");
	    sql.append("            and ds.slip_no = dsd.slip_no");
	    sql.append("         left outer join data_reservation dr");
	    sql.append("                      on dr.shop_id = ds.shop_id");
	    sql.append("                     and dr.customer_id = mc.customer_id");
	    sql.append("                     and dr.slip_no = ds.slip_no");
	    sql.append("                     and dr.delete_date is null");
	    sql.append("         left outer join mst_staff ms");
	    sql.append("                      on ms.staff_id = ds.staff_id");
	    sql.append("         left outer join mst_shop mshop");
	    sql.append("                      on dsd.shop_id = mshop.shop_id");
	    sql.append(" where");
	    sql.append("     mc.customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()));
	    sql.append(" and ds.sales_date is not null");

            //コース消化のみのお会計情報取得クエリ
            sql.append(" union");
            sql.append(" select");
            sql.append("      mc.customer_no");
            sql.append("     ,mc.customer_name1");
            sql.append("     ,mc.customer_name2");
            sql.append("     ,mc.sosia_id");
            sql.append("     ,ds.shop_id");
            sql.append("     ,ds.slip_no");
            sql.append("     ,ds.sales_date");
            sql.append("     ,ds.customer_id");
            sql.append("     ,ds.designated_flag as charge_staff_designated_flag");
            sql.append("     ,ds.staff_id as charge_staff_id");
            sql.append("     ,ds.visited_memo");
            sql.append("     ,ds.next_visit_date");
            sql.append("     ,ms.staff_name1 as charge_staff_name1");
            sql.append("     ,ms.staff_name2 as charge_staff_name2");
            sql.append("     ,ds.visit_num");
            sql.append("     ,dr.start_time");
            sql.append("     ,0 as sales_total");
            sql.append("     ,0 as technic_total");
            sql.append("     ,0 as item_total");
            //nhanvt start add
            sql.append("     ,0 as course_total");
            sql.append("     ,0 as consumption_value_total");
            //nhanvt end add
            sql.append("     ,0 as technic_clame_total");
            sql.append("     ,0 as item_returned_total");
            sql.append("     ,shop.shop_name");
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             min(b.reservation_datetime)");
            sql.append("         from");
            sql.append("             data_reservation as a");
            sql.append("                 inner join data_reservation_detail b");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("         where");
            sql.append("                 a.delete_date is null");
            sql.append("             and b.delete_date is null");
            sql.append("             and a.shop_id = dr.shop_id");
            sql.append("             and a.customer_id = dr.customer_id");
            sql.append("             and b.reservation_datetime >");
            sql.append("                 (");
            sql.append("                     select");
            sql.append("                     max(reservation_datetime)");
            sql.append("                     from");
            sql.append("                     data_reservation_detail");
            sql.append("                     where");
            sql.append("                     shop_id = dr.shop_id");
            sql.append("                     and reservation_no = dr.reservation_no");
            sql.append("                 )");
            sql.append("     ) as nextReserveDate");
            sql.append(" from mst_customer mc");
            sql.append("    inner join data_sales ds");
            sql.append("         on mc.customer_id=ds.customer_id");
            sql.append("    left outer join data_sales_detail dsd");
            sql.append("         on ds.shop_id=dsd.shop_id and ds.slip_no=dsd.slip_no and dsd.delete_date is null");
            sql.append("     inner join data_contract_digestion dcd");
            sql.append("         on ds.shop_id=dcd.shop_id and ds.slip_no=dcd.slip_no");
            sql.append("     inner join data_contract dc");
            sql.append("         on dcd.shop_id=dc.shop_id and dcd.contract_no=dc.contract_no and dcd.contract_detail_no=dc.contract_detail_no");
            sql.append("     inner join data_reservation dr");
            sql.append("         on dcd.shop_id=dr.shop_id and dcd.slip_no=dr.slip_no");
            sql.append("     inner join mst_staff ms");
            sql.append("         on dcd.shop_id=ms.shop_id and dcd.staff_id=ms.staff_id");
            sql.append("     inner join mst_shop shop");
            sql.append("         on ds.shop_id=shop.shop_id");
            sql.append(" where ds.delete_date is null");
            sql.append(" and dcd.delete_date is null");
//            sql.append(" and dc.delete_date is null");
            sql.append(" and dsd.slip_no is null");
            sql.append(" and ds.sales_date is not null");
            sql.append(" and mc.customer_id=").append(SQLUtil.convertForSQL(this.getCustomerID()));

	    sql.append(" order by");
	    sql.append("      sales_date desc");
	    sql.append("     ,slip_no desc");

	    return sql.toString();
	}

        /**
	 * 顧客データを顧客IDから取得する。
	 * @param id 顧客ID
	 * @return 顧客データ
	 */
	public void setCustomerListByID(JFrame owner, ConnectionWrapper con, Integer id, Integer shopID) throws SQLException
	{
            ArrayList<MstCustomer> cusArray = MstCustomer.getMstCustomerArrayByID(con, id, shopID);
            setCustomerList(cusArray);
	}

        /**
	 * 顧客データを電話にかかってきた番号から取得する。
	 * @param no 電話番号
	 * @return 顧客データ
	 */
	public void setCustomerListByPhoneNo(JFrame owner, ConnectionWrapper con, String no, Integer shopID) throws SQLException
	{
		MstCustomer		customer	=	null;
		ArrayList<MstCustomer>	cusArray	=	MstCustomer.getMstCustomerArrayByPhoneNo(con, no, shopID); 
                
                setCustomerList(cusArray);
	}
}

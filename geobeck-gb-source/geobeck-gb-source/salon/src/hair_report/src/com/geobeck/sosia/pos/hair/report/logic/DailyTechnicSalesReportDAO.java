/*
 * DailyTechnicSalesReportDAO.java
 *
 * Created on 2008/09/18, 10:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.util.NumberUtil;
import com.geobeck.util.SQLUtil;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.ArrayList;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.report.beans.DailySalesReportBean;
import com.geobeck.sosia.pos.master.company.MstShop;


/**
 *
 * @author shiera.delusa
 */
public class DailyTechnicSalesReportDAO
{
    /** Creates a new instance of DailyTechnicSalesReportDAO */
    public DailyTechnicSalesReportDAO()
    {
    }
        

	public static String getStaffClass(Integer intStaffID)
	{
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		StringBuffer strSQL;
		String strRet;

		strRet = null;

        try
        {
 			strSQL = new StringBuffer();

			strSQL.append(" select");
			strSQL.append(" 	mst_staff_class.staff_class_name");
			strSQL.append(" from mst_staff");

			strSQL.append(" left join mst_staff_class on");
			strSQL.append(" 	mst_staff.staff_class_id = mst_staff_class.staff_class_id");

			strSQL.append(" where");
			strSQL.append(" 	mst_staff.delete_date is NULL");
			strSQL.append(" and	mst_staff.staff_id = " + SQLUtil.convertForSQL(intStaffID));
			strSQL.append(" and	mst_staff_class.delete_date is NULL");

            result = jdbcConnection.executeQuery( strSQL.toString() );
            if( result.next())
            {
                strRet = result.getString("staff_class_name");
            }
        }
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
		
		return strRet;
	}

	public static Integer getWorkingDays(Integer intYear, Integer intMonth, Integer intStaffID)
	{
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		Calendar calenInitialDay = Calendar.getInstance();
		Calendar calenFinalDay = Calendar.getInstance();
		StringBuffer strSQL;
		Integer intRet;

		calenInitialDay.set(intYear, intMonth - 1, 1);	// 対象年月初日
		calenFinalDay.set(intYear, intMonth, 0);	// 対象年月末日
		intRet = 0;

        try
        {
 			strSQL = new StringBuffer();
/*
			strSQL.append(" select");
			strSQL.append(" 	count(distinct D_WS.working_date) as total_workingdays");
			strSQL.append(" from data_working_staff as D_WS");

			strSQL.append(" left join mst_work_status as M_WS on");
			strSQL.append(" 	D_WS.work_id = M_WS.work_id");

			strSQL.append(" where");
			strSQL.append(" 	D_WS.delete_date is NULL");
			strSQL.append(" and	D_WS.staff_id = " + SQLUtil.convertForSQL(intStaffID));
			strSQL.append(" and	D_WS.working_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" and	M_WS.delete_date is NULL");
			strSQL.append(" and	M_WS.working in (1, 2)");
*/
                        strSQL.append(" select");
                        strSQL.append("     count(distinct sales_date) as total_workingdays");
                        strSQL.append(" from");
                        strSQL.append("     data_sales");
                        strSQL.append(" where");
                        strSQL.append("         delete_date is null");
                        strSQL.append("     and staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        strSQL.append("     and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        
            result = jdbcConnection.executeQuery( strSQL.toString() );
            if( result.next())
            {
                intRet = result.getInt("total_workingdays");
            }
        }
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }

		return intRet;
	}

    public static ArrayList getDailySalesReportBeanData(Integer intYear, Integer intMonth, Integer intStaffID, Integer intWorkingDays, boolean isCurrentShopOnly, MstShop shopInfo)
    {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		Calendar calenInitialDay = Calendar.getInstance();
		Calendar calenFinalDay = Calendar.getInstance();

        ArrayList<DailySalesReportBean> arrlist = new ArrayList<DailySalesReportBean>();
        DailySalesReportBean aBean = null;
		StringBuffer strSQL;

		int nRowCnt;
		long lTeqSales, lTeqRunningTotal;
		int nCustCnt, nCustRunningTotal;
		int nCustCnt_d, nCustRunningTotal_d;
		int nCustCnt_p, nCustRunningTotal_p;
		int nCustCnt_col, nCustRunningTotal_col;
		int nCustCnt_tr, nCustRunningTotal_tr;
		int nCustCnt_new, nCustRunningTotal_new;
		long lItemSales, lItemRunningtotal;

		
		calenInitialDay.set(intYear, intMonth - 1, 1);	// 対象年月初日
		calenFinalDay.set(intYear, intMonth, 0);	// 対象年月末日

		lTeqRunningTotal = 0;
		nCustRunningTotal = 0;
		nCustRunningTotal_d = 0;
		nCustRunningTotal_p = 0;
		nCustRunningTotal_col = 0;
		nCustRunningTotal_tr = 0;
		nCustRunningTotal_new = 0;
		lItemRunningtotal = 0;

        try 
        {
			strSQL = new StringBuffer();

			strSQL.append(" select");
			strSQL.append(" 	SUB_WORKINGDAYS.working_date");
			strSQL.append(" ,	coalesce(SUB_TEQSALES.teqsales, 0) as teqsales");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT.custcnt, 0) as custcnt");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_D.custcnt_d, 0) as custcnt_d");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_P.custcnt_p, 0) as custcnt_p");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_COL.custcnt_col, 0) as custcnt_col");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_TR.custcnt_tr, 0) as custcnt_tr");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_NEW.custcnt_new, 0) as custcnt_new");
			strSQL.append(" ,	coalesce(SUB_ITEMSALES.itemsales, 0) as itemsales");

			// 出勤日
			strSQL.append(" from");
			strSQL.append("     (");
                        strSQL.append("         select distinct");
                        strSQL.append("             sales_date as working_date");
                        strSQL.append("         from");
                        strSQL.append("             data_sales");
                        strSQL.append("         where");
                        strSQL.append("             delete_date is null");
                        strSQL.append("         and staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("         and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        
                        strSQL.append("         union");
                        
                        strSQL.append("         select distinct");
                        strSQL.append("              ds.sales_date");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid ds");
                        strSQL.append("                 inner join data_pioneer pio");
                        strSQL.append("                        on pio.shop_id = ds.shop_id");
                        strSQL.append("                       and pio.slip_no = ds.slip_no");
                        strSQL.append("         where");
                        strSQL.append("                 pio.delete_date is NULL");
                        strSQL.append("             and pio.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and pio.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("             and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and ds.product_division = 1");
                        strSQL.append("             and " + getNotExistsMonitorSQL());
                        
                        strSQL.append("         union");

                        strSQL.append("         select distinct");
                        strSQL.append("             sales_date");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid");
                        strSQL.append("         where");
                        strSQL.append("                 detail_staff_id = " + SQLUtil.convertForSQL(intStaffID));

                        if (isCurrentShopOnly) {
                            strSQL.append(" and shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }

                        strSQL.append("             and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and product_division = 2");
                        
			strSQL.append("     ) as SUB_WORKINGDAYS");

                        // 技術売上
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      sales_date");
                        strSQL.append("                     ,sum(discount_detail_value_no_tax) as teqsales");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid");
                        strSQL.append("                 where");
                        strSQL.append("                         staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and product_division = 1");
                        strSQL.append("                 group by");
                        strSQL.append("                     sales_date");
                        strSQL.append("             ) as SUB_TEQSALES");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_TEQSALES.sales_date");

                        // 客数
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      ds.sales_date");
                        strSQL.append("                     ,count(distinct ds.slip_no) as custcnt");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid ds");
                        strSQL.append("                 where");
                        strSQL.append("                         ds.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and ds.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and ds.product_division = 1");
                        strSQL.append("                     and " + getNotExistsMonitorSQL());
                        strSQL.append("                 group by");
                        strSQL.append("                     ds.sales_date");
                        strSQL.append("             ) as SUB_CUSTCNT");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_CUSTCNT.sales_date");

                        // 指名数
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      ds.sales_date");
                        strSQL.append("                     ,count(distinct ds.slip_no) as custcnt_d");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid ds");
                        strSQL.append("                 where");
                        strSQL.append("                         ds.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and ds.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and ds.product_division = 1");
                        strSQL.append("                     and ds.designated_flag = true");
                        strSQL.append("                     and " + getNotExistsMonitorSQL());
                        strSQL.append("                 group by");
                        strSQL.append("                     ds.sales_date");
                        strSQL.append("             ) as SUB_CUSTCNT_D");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_CUSTCNT_D.sales_date");
                        
                        // P（パーマ）
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      V_DS_DET.sales_date");
                        strSQL.append("                     ,sum(V_DS_DET.product_num) as custcnt_p");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid as V_DS_DET");
                        strSQL.append("                         left join mst_technic as M_TEQ");
                        strSQL.append("                                on V_DS_DET.product_id = M_TEQ.technic_id");
                        strSQL.append("                         left join mst_technic_class as M_TC");
                        strSQL.append("                                on M_TEQ.technic_class_id = M_TC.technic_class_id");
                        strSQL.append("                 where");
                        strSQL.append("                         V_DS_DET.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and V_DS_DET.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and V_DS_DET.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and V_DS_DET.product_division = 1");
                        strSQL.append("                     and M_TEQ.delete_date is NULL");
                        strSQL.append("                     and M_TC.delete_date is NULL");
                        strSQL.append("                     and M_TC.technic_class_name in ('パーマ', 'ストレート')");
                        strSQL.append("                 group by");
                        strSQL.append("                     V_DS_DET.sales_date");
                        strSQL.append("             ) as SUB_CUSTCNT_P");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_CUSTCNT_P.sales_date");

                        // Col（カラー）
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      V_DS_DET.sales_date");
                        strSQL.append("                     ,sum(V_DS_DET.product_num) as custcnt_col");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid as V_DS_DET");
                        strSQL.append("                         left join mst_technic as M_TEQ");
                        strSQL.append("                                on V_DS_DET.product_id = M_TEQ.technic_id");
                        strSQL.append("                         left join mst_technic_class as M_TC");
                        strSQL.append("                                on M_TEQ.technic_class_id = M_TC.technic_class_id");
                        strSQL.append("                 where");
                        strSQL.append("                         V_DS_DET.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and V_DS_DET.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and V_DS_DET.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and V_DS_DET.product_division = 1");
                        strSQL.append("                     and M_TEQ.delete_date is NULL");
                        strSQL.append("                     and M_TC.delete_date is NULL");
                        strSQL.append("                     and M_TC.technic_class_name = 'カラー'");
                        strSQL.append("                 group by");
                        strSQL.append("                     V_DS_DET.sales_date");
                        strSQL.append("             ) as SUB_CUSTCNT_COL");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_CUSTCNT_COL.sales_date");

                        // Tr（トリートメント）
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      V_DS_DET.sales_date");
                        strSQL.append("                     ,sum(V_DS_DET.product_num) as custcnt_tr");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid as V_DS_DET");
                        strSQL.append("                         left join mst_technic as M_TEQ");
                        strSQL.append("                                on V_DS_DET.product_id = M_TEQ.technic_id");
                        strSQL.append("                         left join mst_technic_class as M_TC");
                        strSQL.append("                                on M_TEQ.technic_class_id = M_TC.technic_class_id");
                        strSQL.append("                 where");
                        strSQL.append("                         V_DS_DET.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and V_DS_DET.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and V_DS_DET.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and V_DS_DET.product_division = 1");
                        strSQL.append("                     and M_TEQ.delete_date is NULL");
                        strSQL.append("                     and M_TC.delete_date is NULL");
                        strSQL.append("                     and M_TC.technic_class_name = 'トリートメント'");
                        strSQL.append("                 group by");
                        strSQL.append("                     V_DS_DET.sales_date");
                        strSQL.append("             ) as SUB_CUSTCNT_TR");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_CUSTCNT_TR.sales_date");

                        // New（新規客）
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      ds.sales_date");
                        strSQL.append("                     ,count(distinct ds.customer_id) as custcnt_new");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid ds");
                        strSQL.append("                         inner join data_pioneer pio");
                        strSQL.append("                                on pio.shop_id = ds.shop_id");
                        strSQL.append("                               and pio.slip_no = ds.slip_no");
                        strSQL.append("                 where");
                        strSQL.append("                         pio.delete_date is NULL");
                        strSQL.append("                     and pio.staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and pio.shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and ds.product_division = 1");
                        strSQL.append("                     and " + getNotExistsMonitorSQL());
                        strSQL.append("                 group by");
                        strSQL.append("                     ds.sales_date");
                        strSQL.append("             ) as SUB_CUSTCNT_NEW");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_CUSTCNT_NEW.sales_date");

                        // 日別売上
                        strSQL.append("         left join");
                        strSQL.append("             (");
                        strSQL.append("                 select");
                        strSQL.append("                      sales_date");
                        strSQL.append("                     ,sum(discount_detail_value_no_tax) as itemsales");
                        strSQL.append("                 from");
                        strSQL.append("                     view_data_sales_detail_valid");
                        strSQL.append("                 where");
                        strSQL.append("                         detail_staff_id = " + SQLUtil.convertForSQL(intStaffID));
                        
                        if (isCurrentShopOnly) {
                            strSQL.append(" and shop_id = " + SQLUtil.convertForSQL(shopInfo.getShopID()));
                        }
                        
                        strSQL.append("                     and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                     and product_division in (2, 4)");
                        strSQL.append("                 group by");
                        strSQL.append("                     sales_date");
                        strSQL.append("             ) as SUB_ITEMSALES");
                        strSQL.append("             on SUB_WORKINGDAYS.working_date = SUB_ITEMSALES.sales_date");

			strSQL.append(" order by");
			strSQL.append(" 	SUB_WORKINGDAYS.working_date asc");

                        result = jdbcConnection.executeQuery( strSQL.toString() );

			while ( result.next() )
			{
                            nRowCnt = arrlist.size() + 1;

                            aBean = new DailySalesReportBean();

                            aBean.setSalesDay( result.getDate("working_date") );

                            lTeqSales = result.getLong("teqsales");
                            lTeqRunningTotal += lTeqSales;
                            aBean.setTechnicSales( lTeqSales );
                            aBean.setTechnicRunningTotal( lTeqRunningTotal );
                            aBean.setTechnicSalesEstimate( NumberUtil.round( (double)lTeqRunningTotal / (double)nRowCnt * (double)intWorkingDays ) );

                            nCustCnt = result.getInt("custcnt");
                            nCustRunningTotal += nCustCnt;
                            aBean.setCustomers( nCustCnt );
                            aBean.setCustomersRunningTotal( nCustRunningTotal );
                            aBean.setCustomersEstimate( (int)NumberUtil.round( (double)nCustRunningTotal / (double)nRowCnt * (double)intWorkingDays ) );
                            aBean.setAverageFeePerCustomer( (nCustRunningTotal != 0)  ?  NumberUtil.round( (double)lTeqRunningTotal / (double)nCustRunningTotal )  :  0L );

                            nCustCnt_d = result.getInt("custcnt_d");
                            nCustRunningTotal_d += nCustCnt_d;
                            aBean.setNominations( nCustCnt_d );
                            aBean.setNominationsRunningTotal( nCustRunningTotal_d );
                            aBean.setNominationsPercentage( (nCustRunningTotal != 0)  ?  (double)nCustRunningTotal_d / (double)nCustRunningTotal  :  0.0 );

                            nCustCnt_p = result.getInt("custcnt_p");
                            nCustRunningTotal_p += nCustCnt_p;
                            aBean.setPEachDay( nCustCnt_p );
                            aBean.setPRunningTotal( nCustRunningTotal_p );
                            aBean.setPPercentage( (nCustRunningTotal != 0)  ?  (double)nCustRunningTotal_p / (double)nCustRunningTotal  :  0.0 );

                            nCustCnt_col = result.getInt("custcnt_col");
                            nCustRunningTotal_col += nCustCnt_col;
                            aBean.setColEachDay( nCustCnt_col );
                            aBean.setColRunningTotal( nCustRunningTotal_col );
                            aBean.setColPercentage( (nCustRunningTotal != 0)  ?  (double)nCustRunningTotal_col / (double)nCustRunningTotal  :  0.0 );

                            nCustCnt_tr = result.getInt("custcnt_tr");
                            nCustRunningTotal_tr += nCustCnt_tr;
                            aBean.setTrEachDay( nCustCnt_tr );
                            aBean.setTrRunningTotal( nCustRunningTotal_tr );
                            aBean.setTrPercentage( (nCustRunningTotal != 0)  ?  (double)nCustRunningTotal_tr / (double)nCustRunningTotal  :  0.0 );

                            nCustCnt_new = result.getInt("custcnt_new");
                            nCustRunningTotal_new += nCustCnt_new;
                            aBean.setNewEachDay( nCustCnt_new );
                            aBean.setNewRunningTotal( nCustRunningTotal_new );

                            lItemSales = result.getLong("itemsales");
                            lItemRunningtotal += lItemSales;
                            aBean.setItemSales( lItemSales );
                            aBean.setItemRunningTotal( lItemRunningtotal );

                            arrlist.add(aBean);
			}

            result.close();
        } 
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        } 

        return arrlist;
    }

    private static String getNotExistsMonitorSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" not exists");
        sql.append(" (");
        sql.append("     select 1");
        sql.append("     from");
        sql.append("         view_data_sales_detail_valid dsd");
        sql.append("             left join mst_technic mt");
        sql.append("                    on dsd.product_id = mt.technic_id");
        sql.append("             left join mst_technic_class mtc");
        sql.append("                    on mt.technic_class_id = mtc.technic_class_id");
        sql.append("     where");
        sql.append("             dsd.shop_id = ds.shop_id");
        sql.append("         and dsd.slip_no = ds.slip_no");
        sql.append("         and dsd.product_division = 1");
        sql.append("         and mtc.technic_class_name = 'モニタ'");
        sql.append(" )");
        
        return sql.toString();
    }
}

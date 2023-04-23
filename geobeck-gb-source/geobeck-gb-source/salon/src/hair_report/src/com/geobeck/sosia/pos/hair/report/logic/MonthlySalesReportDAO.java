/*
 * MonthlySalesReportDAO.java
 *
 * Created on 2008/09/19, 10:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.util.NumberUtil;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.report.beans.MonthlySalesReportBean;
/**
 *
 * @author shiera.delusa
 */
public class MonthlySalesReportDAO
{
    
    /** Creates a new instance of MonthlySalesReportDAO */
    public MonthlySalesReportDAO()
    {
    }

	public static Integer getOpenDays(Integer intYear, Integer intMonth, Integer intShopID)
	{
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		StringBuffer strSQL;
		Integer intRet;

		intRet = 0;

        try
        {
 			strSQL = new StringBuffer();

			strSQL.append(" select");
			strSQL.append(" 	coalesce(open_days, 0) as open_days");
			strSQL.append(" from data_target_result");

			strSQL.append(" where");
			strSQL.append(" 	delete_date is NULL");
			strSQL.append(" and	shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" and	year = " + SQLUtil.convertForSQL(intYear));
			strSQL.append(" and	month = " + SQLUtil.convertForSQL(intMonth));

            result = jdbcConnection.executeQuery( strSQL.toString() );
            if( result.next())
            {
                intRet = result.getInt("open_days");
            }
        }
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }

		return intRet;
	}

    public static ArrayList getMonthlySalesReportBeanData(Integer intYear, Integer intMonth, Integer intShopID, Integer intOpenDays)
    {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		Calendar calenInitialDay = Calendar.getInstance();
		Calendar calenFinalDay = Calendar.getInstance();

        ArrayList<MonthlySalesReportBean> arrlist = new ArrayList<MonthlySalesReportBean>();
        MonthlySalesReportBean aBean = null;
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
		int nCustCnt_child, nCustRunningTotal_child;

		
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
		nCustRunningTotal_child = 0;

        try
        {
			strSQL = new StringBuffer();

			strSQL.append(" select");
			strSQL.append(" 	SUB_SALESDAYS.sales_date");
			strSQL.append(" ,	coalesce(SUB_TEQSALES.teqsales, 0) as teqsales");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT.custcnt, 0) as custcnt");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_D.custcnt_d, 0) as custcnt_d");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_P.custcnt_p, 0) as custcnt_p");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_COL.custcnt_col, 0) as custcnt_col");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_TR.custcnt_tr, 0) as custcnt_tr");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_NEW.custcnt_new, 0) as custcnt_new");
			strSQL.append(" ,	coalesce(SUB_ITEMSALES.itemsales, 0) as itemsales");
			strSQL.append(" ,	coalesce(SUB_CUSTCNT_CHILD.custcnt_child, 0) as custcnt_child");


			strSQL.append(" /* 出勤日 */");
			strSQL.append(" from (");
			strSQL.append(" 	select distinct");
			strSQL.append(" 		sales_date");
			strSQL.append(" 	from view_data_sales_valid");

			strSQL.append(" 	where");
			strSQL.append(" 		sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" ) as SUB_SALESDAYS");


			strSQL.append(" /* 技術売上 */");
			strSQL.append(" left join (");
/*                        
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	sum(V_DS_DET.discount_detail_value_no_tax) as teqsales");
			strSQL.append(" 	from view_data_sales_valid as V_DS");

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");

			strSQL.append(" 	where");
			strSQL.append(" 		V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 1");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/
                        strSQL.append("         select");
                        strSQL.append("              sales_date");
                        strSQL.append("             ,sum(discount_detail_value_no_tax) as teqsales");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid");
                        strSQL.append("         where");
                        strSQL.append("                 shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and product_division = 1");
                        strSQL.append("         group by");
                        strSQL.append("         	sales_date");

			strSQL.append(" ) as SUB_TEQSALES on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_TEQSALES.sales_date");


			strSQL.append(" /* 客数 */");
			strSQL.append(" left join (");
                        strSQL.append("         select");
                        strSQL.append("              ds.sales_date");
                        strSQL.append("             ,count(distinct ds.slip_no) as custcnt");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid ds");
                        strSQL.append("         where");
                        strSQL.append("                 ds.shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and ds.product_division = 1");
                        strSQL.append("             and " + getNotExistsMonitorSQL());
                        strSQL.append("         group by");
                        strSQL.append("             ds.sales_date");
			strSQL.append(" ) as SUB_CUSTCNT on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT.sales_date");

			strSQL.append(" /* 指名数 */");
			strSQL.append(" left join (");
                        strSQL.append("         select");
                        strSQL.append("              ds.sales_date");
                        strSQL.append("             ,count(distinct ds.slip_no) as custcnt_d");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid ds");
                        strSQL.append("         where");
                        strSQL.append("                 ds.shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and ds.product_division = 1");
                        strSQL.append("             and ds.designated_flag = true");
                        strSQL.append("             and " + getNotExistsMonitorSQL());
                        strSQL.append("         group by");
                        strSQL.append("             ds.sales_date");
			strSQL.append(" ) as SUB_CUSTCNT_D on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT_D.sales_date");


			strSQL.append(" /* P（パーマ） */");
			strSQL.append(" left join (");
/*
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	count(distinct V_DS.customer_id) as custcnt_p");
			strSQL.append(" 	from view_data_sales_valid as V_DS");

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");

			strSQL.append(" 	left join mst_technic as M_TEQ on");
			strSQL.append(" 		V_DS_DET.product_id = M_TEQ.technic_id");

			strSQL.append(" 	left join mst_technic_class as M_TC on");
			strSQL.append(" 		M_TEQ.technic_class_id = M_TC.technic_class_id");

			strSQL.append(" 	where");
			strSQL.append(" 		V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 1");
			strSQL.append(" 	and	M_TEQ.delete_date is NULL");
			strSQL.append(" 	and	M_TC.delete_date is NULL");
			strSQL.append(" 	and	M_TC.technic_class_name = 'パーマ'");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/
                        strSQL.append("         select");
                        strSQL.append("              V_DS.sales_date");
                        strSQL.append("             ,sum(V_DS.product_num) as custcnt_p");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid as V_DS");
                        strSQL.append("                 left join mst_technic as M_TEQ");
                        strSQL.append("                        on V_DS.product_id = M_TEQ.technic_id");
                        strSQL.append("                 left join mst_technic_class as M_TC");
                        strSQL.append("                        on M_TEQ.technic_class_id = M_TC.technic_class_id");
                        strSQL.append("         where");
                        strSQL.append("                 V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and V_DS.product_division = 1");
                        strSQL.append("             and M_TEQ.delete_date is NULL");
                        strSQL.append("             and M_TC.delete_date is NULL");
                        strSQL.append("             and M_TC.technic_class_name in ('パーマ', 'ストレート')");
                        strSQL.append("         group by");
                        strSQL.append("             V_DS.sales_date");
                        
			strSQL.append(" ) as SUB_CUSTCNT_P on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT_P.sales_date");


			strSQL.append(" /* Col（カラー） */");
			strSQL.append(" left join (");
/*
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	count(distinct V_DS.customer_id) as custcnt_col");
			strSQL.append(" 	from view_data_sales_valid as V_DS");

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");

			strSQL.append(" 	left join mst_technic as M_TEQ on");
			strSQL.append(" 		V_DS_DET.product_id = M_TEQ.technic_id");

			strSQL.append(" 	left join mst_technic_class as M_TC on");
			strSQL.append(" 		M_TEQ.technic_class_id = M_TC.technic_class_id");

			strSQL.append(" 	where");
			strSQL.append(" 		V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 1");
			strSQL.append(" 	and	M_TEQ.delete_date is NULL");
			strSQL.append(" 	and	M_TC.delete_date is NULL");
			strSQL.append(" 	and	M_TC.technic_class_name = 'カラー'");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/
                        strSQL.append("         select");
                        strSQL.append("              V_DS.sales_date");
                        strSQL.append("             ,sum(V_DS.product_num) as custcnt_col");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid as V_DS");
                        strSQL.append("                 left join mst_technic as M_TEQ");
                        strSQL.append("                        on V_DS.product_id = M_TEQ.technic_id");
                        strSQL.append("                 left join mst_technic_class as M_TC");
                        strSQL.append("                        on M_TEQ.technic_class_id = M_TC.technic_class_id");
                        strSQL.append("         where");
                        strSQL.append("                 V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and V_DS.product_division = 1");
                        strSQL.append("             and M_TEQ.delete_date is NULL");
                        strSQL.append("             and M_TC.delete_date is NULL");
                        strSQL.append("             and M_TC.technic_class_name = 'カラー'");
                        strSQL.append("         group by");
                        strSQL.append("             V_DS.sales_date");

			strSQL.append(" ) as SUB_CUSTCNT_COL on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT_COL.sales_date");


			strSQL.append(" /* Tr（トリートメント） */");
			strSQL.append(" left join (");
/*
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	count(distinct V_DS.customer_id) as custcnt_tr");
			strSQL.append(" 	from view_data_sales_valid as V_DS");

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");

			strSQL.append(" 	left join mst_technic as M_TEQ on");
			strSQL.append(" 		V_DS_DET.product_id = M_TEQ.technic_id");

			strSQL.append(" 	left join mst_technic_class as M_TC on");
			strSQL.append(" 		M_TEQ.technic_class_id = M_TC.technic_class_id");

			strSQL.append(" 	where");
			strSQL.append(" 		V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 1");
			strSQL.append(" 	and	M_TEQ.delete_date is NULL");
			strSQL.append(" 	and	M_TC.delete_date is NULL");
			strSQL.append(" 	and	M_TC.technic_class_name = 'トリートメント'");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/
                        strSQL.append("         select");
                        strSQL.append("              V_DS.sales_date");
                        strSQL.append("             ,sum(V_DS.product_num) as custcnt_tr");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid as V_DS");
                        strSQL.append("                 left join mst_technic as M_TEQ");
                        strSQL.append("                        on V_DS.product_id = M_TEQ.technic_id");
                        strSQL.append("                 left join mst_technic_class as M_TC");
                        strSQL.append("                        on M_TEQ.technic_class_id = M_TC.technic_class_id");
                        strSQL.append("         where");
                        strSQL.append("                 V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and V_DS.product_division = 1");
                        strSQL.append("             and M_TEQ.delete_date is NULL");
                        strSQL.append("             and M_TC.delete_date is NULL");
                        strSQL.append("             and M_TC.technic_class_name = 'トリートメント'");
                        strSQL.append("         group by");
                        strSQL.append("             V_DS.sales_date");

			strSQL.append(" ) as SUB_CUSTCNT_TR on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT_TR.sales_date");


			strSQL.append(" /* New（新規客） */");
			strSQL.append(" left join (");
/*
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	count(distinct V_DS.customer_id) as custcnt_new");
			strSQL.append(" 	from data_pioneer as D_PIO");

			strSQL.append(" 	left join view_data_sales_valid as V_DS on");
			strSQL.append(" 		D_PIO.shop_id = V_DS.shop_id");
			strSQL.append(" 	and	D_PIO.slip_no = V_DS.slip_no");
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");
			strSQL.append(" 	and	V_DS_DET.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));

			strSQL.append(" 	left join mst_technic as M_TEQ on");
			strSQL.append(" 		V_DS_DET.product_id = M_TEQ.technic_id");

			strSQL.append(" 	left join mst_technic_class as M_TC on");
			strSQL.append(" 		M_TEQ.technic_class_id = M_TC.technic_class_id");

			strSQL.append(" 	where");
			strSQL.append(" 		D_PIO.delete_date is NULL");
			strSQL.append(" 	and	D_PIO.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 1");
			strSQL.append(" 	and	M_TEQ.delete_date is NULL");
			strSQL.append(" 	and	M_TC.delete_date is NULL");
			strSQL.append(" 	and	M_TC.technic_class_name <> 'モニタ'");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/
                        strSQL.append("         select");
                        strSQL.append("              ds.sales_date");
                        strSQL.append("             ,count(case when dsd.tech_flg > 0 and mc.customer_no <> '0' and get_visit_count(ds.customer_id ,ds.shop_id ,ds.sales_date ) = 1 then dsd.slip_no else null end) as custcnt_new");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_valid ds");
                        strSQL.append("                 inner join");
                        strSQL.append("                     (");
                        strSQL.append("                         select");
                        strSQL.append("                              shop_id");
                        strSQL.append("                             ,slip_no");
                        strSQL.append("                             ,sum(case when product_division IN(1,3) then 1 else 0 end) as tech_flg");
                        strSQL.append("                         from");
                        strSQL.append("                             view_data_sales_detail_valid");
                        strSQL.append("                         where");
                        strSQL.append("                                 shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("                             and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("                         group by");
                        strSQL.append("                              shop_id");
                        strSQL.append("                             ,slip_no");
                        strSQL.append("                     ) dsd");
                        strSQL.append("                      on ds.shop_id = dsd.shop_id");
                        strSQL.append("                     and ds.slip_no = dsd.slip_no");
                        strSQL.append("                 left join mst_customer mc");
                        strSQL.append("                     using(customer_id)");
                        strSQL.append("         group by");
                        strSQL.append("             ds.sales_date");

			strSQL.append(" ) as SUB_CUSTCNT_NEW on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT_NEW.sales_date");


			strSQL.append(" /* 日別売上 */");
			strSQL.append(" left join (");
/*
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	sum(V_DS_DET.discount_detail_value_no_tax) as itemsales");
			strSQL.append(" 	from view_data_sales_valid as V_DS");

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");

			strSQL.append(" 	where");
			strSQL.append(" 		V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 2");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/                        
                        strSQL.append("         select");
                        strSQL.append("              sales_date");
                        strSQL.append("             ,sum(discount_detail_value_no_tax) as itemsales");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid");
                        strSQL.append("         where");
                        strSQL.append("                 shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and product_division in (2, 4)");
                        strSQL.append("         group by");
                        strSQL.append("             sales_date");
                        
			strSQL.append(" ) as SUB_ITEMSALES on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_ITEMSALES.sales_date");


			strSQL.append(" /* Child客数 */");
			strSQL.append(" left join (");
/*                        
			strSQL.append(" 	select");
			strSQL.append(" 		V_DS.sales_date");
			strSQL.append(" 	,	count(distinct V_DS.slip_no) as custcnt_child");
			strSQL.append(" 	from view_data_sales_valid as V_DS");

			strSQL.append(" 	left join view_data_sales_detail_valid as V_DS_DET on");
			strSQL.append(" 		V_DS.shop_id = V_DS_DET.shop_id");
			strSQL.append(" 	and	V_DS.slip_no = V_DS_DET.slip_no");

			strSQL.append(" 	left join mst_technic as M_TEQ on");
			strSQL.append(" 		V_DS_DET.product_id = M_TEQ.technic_id");

			strSQL.append(" 	left join mst_technic_class as M_TEQC on");
			strSQL.append(" 		M_TEQ.technic_class_id = M_TEQC.technic_class_id");

			strSQL.append(" 	left join mst_customer as M_CUST on");
			strSQL.append(" 		V_DS.customer_id = M_CUST.customer_id");

			strSQL.append(" 	where");
			strSQL.append(" 		V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" 	and	V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
			strSQL.append(" 	and	V_DS_DET.product_division = 1");
			strSQL.append(" 	and	M_TEQ.delete_date is NULL");
			strSQL.append(" 	and	M_TEQC.delete_date is NULL");
			strSQL.append(" 	and	M_TEQC.technic_class_name <> 'モニタ'");
			strSQL.append(" 	and	M_CUST.delete_date is NULL");
			strSQL.append(" 	and	coalesce(M_CUST.customer_no, '0') = '0'");

			strSQL.append(" 	group by");
			strSQL.append(" 		V_DS.sales_date");
*/
                        
                        strSQL.append("         select");
                        strSQL.append("              V_DS.sales_date");
                        strSQL.append("             ,count(distinct V_DS.slip_no) as custcnt_child");
                        strSQL.append("         from");
                        strSQL.append("             view_data_sales_detail_valid as V_DS");
                        strSQL.append("                 left join mst_technic as M_TEQ");
                        strSQL.append("                        on V_DS.product_id = M_TEQ.technic_id");
                        strSQL.append("                 left join mst_technic_class as M_TEQC");
                        strSQL.append("                        on M_TEQ.technic_class_id = M_TEQC.technic_class_id");
                        strSQL.append("                 left join mst_customer as M_CUST");
                        strSQL.append("                        on V_DS.customer_id = M_CUST.customer_id");
                        strSQL.append("         where");
                        strSQL.append("                 V_DS.shop_id = " + SQLUtil.convertForSQL(intShopID));
                        strSQL.append("             and V_DS.sales_date between " + SQLUtil.convertForSQLDateOnly(calenInitialDay) + " and " + SQLUtil.convertForSQLDateOnly(calenFinalDay));
                        strSQL.append("             and V_DS.product_division = 1");
                        strSQL.append("             and M_TEQ.delete_date is NULL");
                        strSQL.append("             and M_TEQC.delete_date is NULL");
                        strSQL.append("             and M_TEQC.technic_class_name <> 'モニタ'");
                        strSQL.append("             and M_CUST.delete_date is NULL");
                        strSQL.append("             and coalesce(M_CUST.customer_no, '0') = '0'");
                        strSQL.append("         group by");
                        strSQL.append("             V_DS.sales_date");

			strSQL.append(" ) as SUB_CUSTCNT_CHILD on");
			strSQL.append(" 	SUB_SALESDAYS.sales_date = SUB_CUSTCNT_CHILD.sales_date");


			strSQL.append(" order by");
			strSQL.append(" 	SUB_SALESDAYS.sales_date asc");


            result = jdbcConnection.executeQuery( strSQL.toString() );

			while ( result.next() )
			{
				nRowCnt = arrlist.size() + 1;

				aBean = new MonthlySalesReportBean();

				aBean.setSalesDay( result.getDate("sales_date") );

				lTeqSales = result.getLong("teqsales");
				lTeqRunningTotal += lTeqSales;
				aBean.setTechnicSales( lTeqSales );
				aBean.setTechnicRunningTotal( lTeqRunningTotal );
				aBean.setTechnicSalesEstimate( NumberUtil.round( (double)lTeqRunningTotal / (double)nRowCnt * (double)intOpenDays ) );

				nCustCnt = result.getInt("custcnt");
				nCustRunningTotal += nCustCnt;
				aBean.setCustomers( nCustCnt );
				aBean.setCustomersRunningTotal( nCustRunningTotal );
				aBean.setCustomersEstimate( (int)NumberUtil.round( (double)nCustRunningTotal / (double)nRowCnt * (double)intOpenDays ) );
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
				aBean.setNewPercentage( (nCustRunningTotal != 0)  ?  (double)nCustRunningTotal_new / (double)nCustRunningTotal  :  0.0 );

				lItemSales = result.getLong("itemsales");
				lItemRunningtotal += lItemSales;
				aBean.setItemSales( lItemSales );
				aBean.setItemRunningTotal( lItemRunningtotal );
				aBean.setItemSalesEstimate( NumberUtil.round( (double)lItemRunningtotal / (double)nRowCnt * (double)intOpenDays ) );

				nCustCnt_child = result.getInt("custcnt_child");
				nCustRunningTotal_child += nCustCnt_child;
				aBean.setCustChild( nCustCnt_child );
				aBean.setCustChildRunningTotal( nCustRunningTotal_child );

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

	public static boolean establishPrevInfo(HashMap reportParams, Integer intYear, Integer intMonth, Integer intShopID)
	{
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		StringBuffer strSQL;
		long lTeqSales;
		int nCustCnt;
		int nPCnt;
		int nColCnt;
		int nTrCnt;
		int nNewCnt;

        try
        {
 			strSQL = new StringBuffer();

			strSQL.append(" select");
			strSQL.append(" 	result_technic");
			strSQL.append(" ,	result_in");
			strSQL.append(" ,	(result_p + result_stp) as result_perm");
			strSQL.append(" ,	result_hd");
			strSQL.append(" ,	result_tr");
			strSQL.append(" ,	result_new");
			strSQL.append(" ,	result_item");
			strSQL.append(" from data_target_result");

			strSQL.append(" where");
			strSQL.append(" 	delete_date is NULL");
			strSQL.append(" and	shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" and	year = " + SQLUtil.convertForSQL(intYear - 1));
			strSQL.append(" and	month = " + SQLUtil.convertForSQL(intMonth));

            result = jdbcConnection.executeQuery( strSQL.toString() );
			if( result.next())
			{
				lTeqSales = result.getLong("result_technic");
				reportParams.put( "prevTeqSales", lTeqSales );

				nCustCnt = result.getInt("result_in");
				reportParams.put( "prevCustCnt", nCustCnt );

				reportParams.put( "prevSalesPerCust", (nCustCnt != 0)  ?  NumberUtil.round( (double)lTeqSales / (double)nCustCnt )  :  0L );

				reportParams.put( "prevDCnt", 0 );	// 指名数 0固定
				reportParams.put( "prevDRate",  0.0 );	// 指名比率 0固定

				nPCnt = result.getInt("result_perm");
				reportParams.put( "prevPCnt", nPCnt );
				reportParams.put( "prevPRate", (nCustCnt != 0)  ?  (double)nPCnt / (double)nCustCnt  :  0.0 );

				nColCnt = result.getInt("result_hd");
				reportParams.put( "prevColCnt", nColCnt );
				reportParams.put( "prevColRate", (nCustCnt != 0)  ?  (double)nColCnt / (double)nCustCnt  :  0.0 );

				nTrCnt = result.getInt("result_tr");
				reportParams.put( "prevTrCnt", nTrCnt );
				reportParams.put( "prevTrRate", (nCustCnt != 0)  ?  (double)nTrCnt / (double)nCustCnt  :  0.0 );

				nNewCnt = result.getInt("result_new");
				reportParams.put( "prevNewCnt", nNewCnt );
				reportParams.put( "prevNewRate", (nCustCnt != 0)  ?  (double)nNewCnt / (double)nCustCnt  :  0.0 );

				reportParams.put( "prevItemSales", result.getLong("result_item") );
			}
			else
			{
				return false;
			}
        }
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
			return false;
        }

		return true;
	}

	public static boolean establishTgtInfo(HashMap reportParams, Integer intYear, Integer intMonth, Integer intShopID)
	{
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
		StringBuffer strSQL;
		long lTeqSales;
		int nCustCnt;
		int nPCnt;
		int nColCnt;
		int nTrCnt;
		int nNewCnt;

        try
        {
 			strSQL = new StringBuffer();

			strSQL.append(" select");
			strSQL.append(" 	target_technic");
			strSQL.append(" ,	target_in");
			strSQL.append(" ,	(target_p + target_stp) as target_perm");
			strSQL.append(" ,	target_hd");
			strSQL.append(" ,	target_tr");
			strSQL.append(" ,	target_new");
			strSQL.append(" ,	target_item");
			strSQL.append(" from data_target_result");

			strSQL.append(" where");
			strSQL.append(" 	delete_date is NULL");
			strSQL.append(" and	shop_id = " + SQLUtil.convertForSQL(intShopID));
			strSQL.append(" and	year = " + SQLUtil.convertForSQL(intYear));
			strSQL.append(" and	month = " + SQLUtil.convertForSQL(intMonth));

            result = jdbcConnection.executeQuery( strSQL.toString() );
			if( result.next())
			{
				lTeqSales = result.getLong("target_technic");
				reportParams.put( "tgtTeqSales", lTeqSales );

				nCustCnt = result.getInt("target_in");
				reportParams.put( "tgtCustCnt", nCustCnt );

				reportParams.put( "tgtSalesPerCust", (nCustCnt != 0)  ?  NumberUtil.round( (double)lTeqSales / (double)nCustCnt )  :  0L );

				reportParams.put( "tgtDCnt", 0 );	// 指名数 0固定
				reportParams.put( "tgtDRate",  0.0 );	// 指名比率 0固定

				nPCnt = result.getInt("target_perm");
				reportParams.put( "tgtPCnt", nPCnt );
				reportParams.put( "tgtPRate", (nCustCnt != 0)  ?  (double)nPCnt / (double)nCustCnt  :  0.0 );

				nColCnt = result.getInt("target_hd");
				reportParams.put( "tgtColCnt", nColCnt );
				reportParams.put( "tgtColRate", (nCustCnt != 0)  ?  (double)nColCnt / (double)nCustCnt  :  0.0 );

				nTrCnt = result.getInt("target_tr");
				reportParams.put( "tgtTrCnt", nTrCnt );
				reportParams.put( "tgtTrRate", (nCustCnt != 0)  ?  (double)nTrCnt / (double)nCustCnt  :  0.0 );

				nNewCnt = result.getInt("target_new");
				reportParams.put( "tgtNewCnt", nNewCnt );
				reportParams.put( "tgtNewRate", (nCustCnt != 0)  ?  (double)nNewCnt / (double)nCustCnt  :  0.0 );

				reportParams.put( "tgtItemSales", result.getLong("target_item") );
			}
			else
			{
				return false;
			}
        }
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
			return false;
        }

		return true;
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

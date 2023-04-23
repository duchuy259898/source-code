/*
 * ShopRankingReportDAO.java
 *
 * Created on 2008/09/25, 9:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.util.NumberUtil;
import com.geobeck.util.SQLUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Level;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.hair.report.beans.ShopRankingBean;

/**
 *
 * @author shiera.delusa
 */
public class ShopRankingReportDAO {
    
    /** Creates a new instance of ShopRankingReportDAO */
    public ShopRankingReportDAO() {
    }
    
    public static ShopRankingBean getShopInfo( ShopRankingBean bean, Date reportDate ) {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
//        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
//        String dateString = dateFormat.format( reportDate );
        Calendar calenCurrent = Calendar.getInstance();
        Calendar calenFDOM = Calendar.getInstance();
        
        StringBuilder sql;
        
        calenCurrent.setTime(reportDate);
        calenFDOM.setTime(reportDate);
        calenFDOM.set(Calendar.DAY_OF_MONTH, 1);
        
        try {
            /*******************************************************************
             * getting the shop name
             ******************************************************************/
            String sqlQuery = "SELECT shop_name from mst_shop WHERE shop_id=" + bean.getShopId()
            + " and mst_shop.delete_date is NULL";
            
            ResultSetWrapper result = jdbcConnection.executeQuery( sqlQuery );
            if( result.next()) {
                bean.setShopName( result.getString( "shop_name" ));
            }
            
            /*******************************************************************
             * getting the total item sales
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append("      sum(discount_detail_value_no_tax) as item_sales");
            sql.append("     ,sum(discount_detail_value_in_tax - discount_detail_value_no_tax) as item_tax");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid");
            sql.append(" where");
            sql.append("         sales_date = " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            sql.append("     and product_division in (2, 4)");
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setItemSales( result.getLong( "item_sales" ));
                bean.setItemTax( result.getLong( "item_tax" ));
            } else {
                bean.setItemSales(0L);
                bean.setItemTax(0L);
            }
            
            // Setting the total sales ( item sales + technic sales ) 指名料金、予約料金 0固定
            bean.setTotalSales( bean.getTechnicSales() + bean.getItemSales() + 0 + 0);
            
            /*******************************************************************
             * getting the total customers
             ******************************************************************/
            sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     count(distinct ds.slip_no) as customers_count");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid ds");
            sql.append(" where");
            sql.append("         ds.sales_date = " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            sql.append("     and ds.product_division = 1");
            sql.append("     and " + getNotExistsMonitorSQL());

            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setCustomers( result.getInt( "customers_count" ));
            } else {
                bean.setCustomers(0);
            }
            
            /*******************************************************************
             * getting the total new customers
             ******************************************************************/
            sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     count(distinct ds.slip_no) as newcustomers_count");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid ds");
            sql.append(" where");
            sql.append("         ds.sales_date = " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append("     and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1");
            sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            sql.append("     and ds.product_division = 1");
            sql.append("     and " + getNotExistsMonitorSQL());
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setNewCustomers( result.getInt( "newcustomers_count" ));
            } else {
                bean.setNewCustomers(0);
            }
            
            /*******************************************************************
             * getting the sales per staff
             ******************************************************************/
            if (bean.getStaffsOnDuty() != 0) {
                bean.setTechnicSalesPerStaff( NumberUtil.round((double)bean.getTechnicSales() / (double)bean.getStaffsOnDuty()) );
            } else {
                bean.setTechnicSalesPerStaff(0L);
            }
            
            /*******************************************************************
             * getting the customer count per staff
             ******************************************************************/
            if (bean.getStaffsOnDuty() != 0) {
                bean.setCustPerStaff( (int)NumberUtil.round( (double)bean.getCustomers() / (double)bean.getStaffsOnDuty() ) );
            } else {
                bean.setCustPerStaff(0);
            }
            
            /*******************************************************************
             * getting the tech sales per customer
             ******************************************************************/
            if (bean.getCustomers() != 0) {
                bean.setTechnicPrice( (int)NumberUtil.round( (double)bean.getTechnicSales() / (double)bean.getCustomers() ) );
            } else {
                bean.setTechnicPrice(0);
            }
            
            /*******************************************************************
             * getting the planning open days in the month
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append(" 	open_days");
            sql.append(" from data_target_result");
            
            sql.append(" where");
            sql.append(" 	delete_date is NULL");
            sql.append(" and	year = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.YEAR)));
            sql.append(" and	month = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.MONTH) + 1));
            sql.append(" and	shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setTotalWorkDays( result.getInt( "open_days" ));
            } else {
                bean.setTotalWorkDays(0);
            }
            
            /*******************************************************************
             * getting the current open days in the month
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append(" 	count(*) as current_opendays");
            sql.append(" from (");
            sql.append(" 	select");
            sql.append(" 		sales_date");
            sql.append(" 	from view_data_sales_valid");
            
            sql.append(" 	where");
            sql.append(" 		date_part('year', sales_date) = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.YEAR)));
            sql.append(" 	and	date_part('month', sales_date) = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.MONTH) + 1));
            sql.append(" 	and	date_part('day', sales_date) <= " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.DATE)));
            sql.append(" 	and	shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            
            sql.append(" 	group by");
            sql.append(" 		sales_date");
            sql.append(" ) as SUB_OPENDAY");
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setActualWorkDays( result.getInt( "current_opendays" ));
            } else {
                bean.setActualWorkDays(0);
            }
            
            /*******************************************************************
             * getting the remaining open days
             ******************************************************************/
            bean.setRemainingWorkDays( bean.getTotalWorkDays() - bean.getActualWorkDays() );
            
            
            /*******************************************************************
             * getting the sales so far
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append(" 	sum(V_DSdet.discount_detail_value_no_tax) as sales_sofar");
            sql.append(" from view_data_sales_detail_valid as V_DSdet");
            
            sql.append(" where");
            sql.append(" 	V_DSdet.sales_date >= " + SQLUtil.convertForSQLDateOnly(calenFDOM.getTime()));
            sql.append(" and	V_DSdet.sales_date <= " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append(" and	V_DSdet.shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            sql.append(" and	V_DSdet.product_division = 1");
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setTechnicAccumulated( result.getLong( "sales_sofar" ));
            }
            
            /*******************************************************************
             * getting the sales prediction
             ******************************************************************/
            if (bean.getActualWorkDays() != 0) {
                bean.setTechnicSalesEstimate( (long)NumberUtil.round( Math.floor((double)bean.getTechnicAccumulated() / (double)bean.getActualWorkDays()) * (double)bean.getTotalWorkDays() ) );
            } else {
                bean.setTechnicSalesEstimate(0L);
            }
            
            
            /*******************************************************************
             * getting the total customers so far in the month
             ******************************************************************/
            sql = new StringBuilder(1000);

            sql.append(" select");
            sql.append("     count(distinct ds.slip_no) as customers_count");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid ds");
            sql.append(" where");
            sql.append("         ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(calenFDOM.getTime()));
            sql.append("     and ds.sales_date <= " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));
            sql.append("     and ds.product_division = 1");
            sql.append("     and " + getNotExistsMonitorSQL());

            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setCustomersTotal( result.getInt( "customers_count" ));
            } else {
                bean.setCustomersTotal(0);
            }
            
            /*******************************************************************
             * getting the customer count prediction
             ******************************************************************/
            if (bean.getActualWorkDays() != 0) {
                bean.setCustomersEstimate( (int)NumberUtil.round( Math.floor((double)bean.getCustomersTotal() / (double)bean.getActualWorkDays()) * (double)bean.getTotalWorkDays() ) );
            } else {
                bean.setCustomersEstimate(0);
            }
            
            
            /*******************************************************************
             * getting the previous year sales
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append(" 	result_technic");
            sql.append(" from data_target_result");
            
            sql.append(" where");
            sql.append(" 	delete_date is NULL");
            sql.append(" and	year = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.YEAR) - 1));
            sql.append(" and	month = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.MONTH) + 1));
            sql.append(" and	shop_id = " + bean.getShopId());
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setPreviousYearSales( result.getLong( "result_technic" ));
            } else {
                bean.setPreviousYearSales(0L);
            }
            
            
            /*******************************************************************
             * getting the difference from prev
             ******************************************************************/
            bean.setLastYearDiff( bean.getTechnicSalesEstimate() - bean.getPreviousYearSales() );
            
            
            /*******************************************************************
             * getting the prev dif rate
             ******************************************************************/
            if (bean.getPreviousYearSales() != 0) {
                bean.setLastYearDiffRate( (double)( (double)bean.getTechnicSalesEstimate() / (double)bean.getPreviousYearSales() ) );
            } else {
                bean.setLastYearDiffRate(null);
            }
            
            
            /*******************************************************************
             * getting the target sales
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append(" 	target_technic");
            sql.append(" from data_target_result");
            
            sql.append(" where");
            sql.append(" 	delete_date is NULL");
            sql.append(" and	year = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.YEAR)));
            sql.append(" and	month = " + SQLUtil.convertForSQL(calenCurrent.get(Calendar.MONTH) + 1));
            sql.append(" and	shop_id = " + bean.getShopId());
            
            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setTargetSales( result.getLong( "target_technic" ));
            } else {
                bean.setTargetSales(0L);
            }
            
            
            /*******************************************************************
             * getting the difference from target
             ******************************************************************/
            bean.setTargetDifference( bean.getTechnicSalesEstimate() - bean.getTargetSales() );
            
            
            /*******************************************************************
             * getting the target dif rate
             ******************************************************************/
            if (bean.getTargetSales() != 0) {
                bean.setTargetDiffRate( ( (double)bean.getTechnicSalesEstimate() / (double)bean.getTargetSales() ) );
            } else {
                bean.setTargetDiffRate(null);
            }
            
            
            /*******************************************************************
             * getting the expenses
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append("     coalesce(sum(case when in_out then io_value else 0 end), 0)");
            sql.append("       - coalesce(sum(case when not in_out then io_value else 0 end), 0) as expense");
            sql.append(" from");
            sql.append("     data_cash_io");
            sql.append(" where");
            sql.append("         delete_date is null");
            sql.append("     and io_date = " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));

            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setOtherExpenses( result.getInt( "expense" ));
            } else {
                bean.setOtherExpenses(0);
            }
            
            
            /*******************************************************************
             * getting the expenses so far in the month
             ******************************************************************/
            sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append("     coalesce(sum(case when in_out then io_value else 0 end), 0)");
            sql.append("       - coalesce(sum(case when not in_out then io_value else 0 end), 0) as expense");
            sql.append(" from");
            sql.append("     data_cash_io");
            sql.append(" where");
            sql.append("         delete_date is null");
            sql.append("     and io_date >= " + SQLUtil.convertForSQLDateOnly(calenFDOM.getTime()));
            sql.append("     and io_date <= " + SQLUtil.convertForSQLDateOnly(reportDate));
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(bean.getShopId()));

            result = jdbcConnection.executeQuery(sql.toString());
            if( result.next()) {
                bean.setTotalExpenses( result.getLong( "expense" ));
            } else {
                bean.setTotalExpenses(0L);
            }
            
        } catch( Exception e ) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        
        return bean;
    }
    
    public static ArrayList<ShopRankingBean> identifyRanking( Date reportDateObj ) {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        
        ArrayList<ShopRankingBean> list = new ArrayList<ShopRankingBean>();
        
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     t.*");
        sql.append(" from");
        sql.append(" (");
        sql.append("     select");
        sql.append("          V_DSdet.shop_id as shop_id");
        sql.append("         ,sum(V_DSdet.discount_detail_value_no_tax) as technic_sales");
        sql.append("         ,sum(V_DSdet.discount_detail_value_in_tax - V_DSdet.discount_detail_value_no_tax) as technic_tax");
        sql.append("         ,(");
        sql.append("             select");
        sql.append("                 count(D_WS.staff_id) as staffcnt");
        sql.append("             from");
        sql.append("                 data_working_staff as D_WS");
        sql.append("                     left join mst_work_status as M_WS");
        sql.append("                            on D_WS.work_id = M_WS.work_id");
        sql.append("             where");
        sql.append("                     D_WS.delete_date is NULL");
        sql.append("                 and D_WS.working_date = max(V_DSdet.sales_date)");
        sql.append("                 and D_WS.shop_id = max(V_DSdet.shop_id)");
        sql.append("                 and M_WS.delete_date is NULL");
        sql.append("                 and M_WS.working in (1, 2)");
        sql.append("          ) as staffcnt");
        sql.append("     from");
        sql.append("         view_data_sales_detail_valid as V_DSdet");
        sql.append("             inner join mst_customer m_cs");
        sql.append("                     on m_cs.customer_id = V_DSdet.customer_id");
        sql.append("             inner join mst_technic m_tec");
        sql.append("                     on m_tec.technic_id = V_DSdet.product_id");
        sql.append("             inner join mst_technic_class m_tcc");
        sql.append("                     on m_tcc.technic_class_id = m_tec.technic_class_id");
        sql.append("     where");
        sql.append("             V_DSdet.sales_date = " + SQLUtil.convertForSQLDateOnly(reportDateObj));
        sql.append("         and V_DSdet.product_division = 1");
//        sql.append("         and m_tcc.technic_class_name <> 'モニタ'");
        sql.append("     group by");
        sql.append("         V_DSdet.shop_id");
        sql.append(" ) t");
        sql.append(" order by");
        sql.append("     case when staffcnt > 0 then round(technic_sales / staffcnt) else 0 end desc");

        try {
            ResultSetWrapper result = jdbcConnection.executeQuery(sql.toString());
            
            int ranking = 1;
            while( result.next() ) {
                ShopRankingBean info = new ShopRankingBean();
                info.setRanking( ranking );
                ranking++;
                info.setShopId( result.getInt( "shop_id" ));
                info.setTechnicSales( result.getLong("technic_sales"));
                info.setTechnicTax( result.getLong("technic_tax"));
                info.setStaffsOnDuty( result.getInt( "staffcnt" ));
                list.add( info );
            }
        } catch( Exception e ) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        
        return list;
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

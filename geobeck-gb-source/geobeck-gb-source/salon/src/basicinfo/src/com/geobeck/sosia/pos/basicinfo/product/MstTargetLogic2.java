/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author ntluc
 */
public class MstTargetLogic2 {
    private Integer shopId = null;
    private Integer targetYear = null;
    private Integer targetMonth = null;
    private Integer categoryId = null;
    
    public StringBuffer getIntroduceSQL(MstShop selectedShop,int SelectedYear, int SelectedMonth, int shopCategoryId) {
        //1.1.45.紹介人数（＠ＳＴ）
        //今年：
        StringBuffer sql = new StringBuffer();
        //業態が空白ではない場合は：
        sql = new StringBuffer();
        if (shopCategoryId > 0) {
            sql.append(" select \n");
            sql.append(" (	 \n");
            sql.append("         select  count (distinct ds.customer_id) \n");
            sql.append("         from view_data_sales_detail_valid ds	 \n");
            sql.append("         inner join mst_customer mc on ds.customer_id = mc.customer_id \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " \n");
            sql.append("         and date_part('month',sales_date) =  " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("        and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and mc.introducer_id is not null \n");
            sql.append(" and 			  ");
            sql.append(" (			  ");
            sql.append(" exists (			  ");
            sql.append(" 	select * from 		  ");
            sql.append(" 	view_data_sales_detail_valid ds1		  ");
            sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
            sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
            sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
            sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
            sql.append(" 	) 		  ");
            sql.append(" or 			  ");
            sql.append(" exists(			  ");
            sql.append(" 	select * from 		  ");
            sql.append(" 	view_data_sales_detail_valid ds1		  ");
            sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
            sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
            sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
            sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
            sql.append(" 	)		  ");
            sql.append(" ) 			  ");

            sql.append(" )/ \n");
            sql.append(" ( \n");
            sql.append("         select count(distinct sms.staff_id) \n");
            sql.append("         from data_sales ds \n");
            sql.append("         inner join data_sales_mainstaff sms on ds.shop_id = sms.shop_id and ds.slip_no = sms.slip_no \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " \n");
            sql.append("         and date_part('month',sales_date) =  " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("         and shop_category_id = " + shopCategoryId + " \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append(" ):: float as cus_count \n");
        } else {
            //業態が空白の場合は																						
            sql.append(" select \n");
            sql.append(" ( \n");
            sql.append("         select  count (distinct ds.customer_id)	 \n");
            sql.append("          from view_data_sales_detail_valid ds \n");
            sql.append("         inner join mst_customer mc on ds.customer_id = mc.customer_id \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "  \n");
            sql.append("         and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and mc.introducer_id is not null \n");
            sql.append(" )/	 \n");
            sql.append(" (	 \n");
            sql.append("         select  count (distinct staff_id)	 \n");
            sql.append("         from view_data_sales_detail_valid ds \n");
            sql.append("         inner join mst_customer mc on ds.customer_id = mc.customer_id \n");
            sql.append("         where date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "  \n");
            sql.append("         and date_part('month',sales_date) =  " + SQLUtil.convertForSQL(SelectedMonth) + "  \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and ds.product_division in (1,2) \n");
            sql.append(" )::float as cus_count	 \n");

        }
        return sql;
    }

    public StringBuffer getIntroduceLastYearSQL(MstShop selectedShop,int SelectedYear, int SelectedMonth, int shopCategoryId) {
        //1.1.45.紹介人数（＠ＳＴ）
        //業態が空白ではない場合は：
        StringBuffer sql = new StringBuffer();
        sql = new StringBuffer();
        if (shopCategoryId > 0) {
            sql.append(" select \n");
            sql.append(" ( \n");
            sql.append("         select  count (distinct ds.customer_id) \n");
            sql.append("         from view_data_sales_detail_valid ds	 \n");
            sql.append("         inner join mst_customer mc on ds.customer_id = mc.customer_id \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 \n");
            sql.append("         and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and mc.introducer_id is not null \n");
            sql.append(" and 			  ");
            sql.append(" (			  ");
            sql.append(" exists (			  ");
            sql.append(" 	select * from 		  ");
            sql.append(" 	view_data_sales_detail_valid ds1		  ");
            sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
            sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
            sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
            sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
            sql.append(" 	) 		  ");
            sql.append(" or 			  ");
            sql.append(" exists(			  ");
            sql.append(" 	select * from 		  ");
            sql.append(" 	view_data_sales_detail_valid ds1		  ");
            sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
            sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
            sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
            sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
            sql.append(" 	)		  ");
            sql.append(" ) 			  ");

            sql.append(" )/	 \n");
            sql.append(" (	 \n");
            sql.append("         select count(distinct sms.staff_id) \n");
            sql.append("         from data_sales ds	 \n");
            sql.append("         inner join data_sales_mainstaff sms on ds.shop_id = sms.shop_id and ds.slip_no = sms.slip_no  \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1  \n");
            sql.append("         and date_part('month',sales_date) =  " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and shop_category_id = " + shopCategoryId + " \n");
            sql.append(" ):: float as cus_count \n");
        } else {
            //業態が空白の場合は																							
            sql.append(" select \n");
            sql.append(" ( \n");
            sql.append("         select  count (distinct ds.customer_id) \n");
            sql.append("         from view_data_sales_detail_valid ds \n");
            sql.append("         inner join mst_customer mc on ds.customer_id = mc.customer_id \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 \n");
            sql.append("         and date_part('month',sales_date) =  " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and mc.introducer_id is not null \n");
            sql.append(" )/ \n");
            sql.append(" ( \n");
            sql.append("        select  count (distinct staff_id) \n");
            sql.append("         from view_data_sales_detail_valid ds \n");
            sql.append("         inner join mst_customer mc on ds.customer_id = mc.customer_id \n");
            sql.append("         where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 \n");
            sql.append("         and date_part('month',sales_date) =  " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append("         and ds.shop_id in (" + selectedShop.getShopID() + ")");
            sql.append("         and ds.product_division in (1,2) \n");
            sql.append("  )::float as cus_count	 \n");
        }
        return sql;
    }
    
    //Task #35027 GB_Mashu [Product][Code][Phase4]目標管理追加仕様-店舗実績
    //IVS_nahoang Start add 20150122
    /**
     * Get repeat90netNew past value for row 13 of excel report
     * @return double
     */
    public double getRepeat90NetnewInPast(){
                    //GB_Mashu
            //IVS_nahoang Start Edit 20150122
            StringBuilder sql = new StringBuilder();
            String StartDayOfSelected = "'" + this.getTargetYear() + "-" + this.getTargetMonth() + "-01'";
            sql.append(" select 			  ");
            sql.append(" ((select count(distinct ds.customer_id)			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" where date_part('year',sales_date) =" + this.getTargetYear() + " and date_part('month',sales_date)  = " + this.getTargetMonth());
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            if (this.getCategoryId() > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" and ds.customer_id not in 			  ");
            sql.append(" (			  ");
            sql.append(" select ds.customer_id 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" where sales_date < date " + StartDayOfSelected + "  and sales_date >=  date  " + StartDayOfSelected + " -interval'2 month'			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            if (this.getCategoryId()  > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )			  ");
            sql.append(" and ds.customer_id in 			  ");
            sql.append(" (			  ");
            sql.append(" select ds.customer_id 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join data_reservation re ");
            sql.append(" on ds.shop_id = re.shop_id");
            sql.append(" and ds.slip_no = re.slip_no");
            sql.append(" where  sales_date >=  date  " + StartDayOfSelected + " -interval'3 month'  			  ");
            sql.append(" and sales_date <=  date  " + StartDayOfSelected + " -interval'2 month' - interval '1 day'			  ");
            sql.append(" and visit_num = 1			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            sql.append(" and re.mobile_flag > 0");
            if (this.getCategoryId()  > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )			  ");
            sql.append(" 			  ");
            sql.append(" )*100/			  ");
            sql.append(" (			  ");
            sql.append(" select count(distinct ds.customer_id)			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join data_reservation re ");
            sql.append(" on ds.shop_id = re.shop_id");
            sql.append(" and ds.slip_no = re.slip_no");
            sql.append(" and ds.customer_id = re.customer_id");
            sql.append(" and re.delete_date is null");
            sql.append(" where date_part('year',sales_date) = date_part('year',date " +StartDayOfSelected+ " -interval'3 month') and date_part('month',sales_date)  = date_part('month',date " +StartDayOfSelected+ " -interval'3 month')");
            sql.append(" and visit_num = 1			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            sql.append(" and re.mobile_flag > 0");
            if (this.getCategoryId() > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )) as repeat_90_new_old			  ");

            double repeat90NetNewOld = 0;
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = con.executeQuery(sql.toString());
                if(rs.next()){
                    repeat90NetNewOld = rs.getDouble("repeat_90_new_old");
                }               
            } catch (SQLException e) {
            }
            
            return repeat90NetNewOld;
    }
    
   /**
    * Get repeat90netNew current value for row 13 of excel report
    * @return double
    */
    public double getRepeat90NetnewInPresent(){
        double repeat90NetNew = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        sql.append(" select repeat_90_netnew*100 as repeat_90_netnew from data_target where year =" + this.getTargetYear() + 
                " and month = " + this.getTargetMonth() + "  and shop_category_id = " + this.getCategoryId());
        sql.append(" and shop_id in (" + this.getShopId() + ")\n");
        
        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if(rs.next()){
                repeat90NetNew = rs.getDouble("repeat_90_netnew");
            }            
        } catch (Exception e) {
        }

        return repeat90NetNew;
    }
    
    public double getRepeat90NetnewInFuture(){
                    //GB_Mashu
            //IVS_nahoang Start Edit 20150122
            StringBuilder sql = new StringBuilder();
            String StartDayOfSelected = "'" + this.getTargetYear() + "-" + this.getTargetMonth() + "-01'";
            sql.append(" select 			  ");
            sql.append(" ((select count(distinct ds.customer_id)			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(this.getTargetYear()) + "-1 and date_part('month',sales_date)  = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            if (this.getCategoryId() > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" and ds.customer_id not in 			  ");
            sql.append(" (			  ");
            sql.append(" select ds.customer_id 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" where sales_date < date " + StartDayOfSelected + " -interval'1 year' and sales_date >=  date " + StartDayOfSelected + " -interval'2 month'-interval '1 year'			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            if (this.getCategoryId()  > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )			  ");
            sql.append(" and ds.customer_id in 			  ");
            sql.append(" (			  ");
            sql.append(" select ds.customer_id 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join data_reservation re ");
            sql.append(" on ds.shop_id = re.shop_id");
            sql.append(" and ds.slip_no = re.slip_no");
            sql.append(" where  sales_date >=  date " + StartDayOfSelected + " -interval'3 month'  -interval '1 year'			  ");
            sql.append(" and sales_date <=  date " + StartDayOfSelected + " -interval'2 month' - interval '1 day' -interval '1 year'			  ");
            sql.append(" and visit_num = 1			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            sql.append(" and re.mobile_flag > 0");
            if (this.getCategoryId()  > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )			  ");
            sql.append(" 			  ");
            sql.append(" )*100/			  ");
            sql.append(" (			  ");
            sql.append(" select count(distinct ds.customer_id)			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join data_reservation re ");
            sql.append(" on ds.shop_id = re.shop_id");
            sql.append(" and ds.slip_no = re.slip_no");
            sql.append(" and ds.customer_id = re.customer_id");
            sql.append(" and re.delete_date is null");
            sql.append(" where date_part('year',sales_date) = (date_part('year',date " +StartDayOfSelected+ " -interval'3 month') - 1) and date_part('month',sales_date)  = date_part('month',date " +StartDayOfSelected+ " -interval'3 month')");
            sql.append(" and visit_num = 1			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")");
            sql.append(" and re.mobile_flag > 0");
            if (this.getCategoryId() > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId()  + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )) as repeat_90_new_old			  ");

            double repeat90NetNewOld = 0;
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = con.executeQuery(sql.toString());
                if(rs.next()){
                    repeat90NetNewOld = rs.getDouble("repeat_90_new_old");
                }               
            } catch (SQLException e) {
            }
            
            return repeat90NetNewOld;
    }
    /**
     * Get dataTargetMotive field In Past for row 16 of excel report
     * @return double
     */
    public double getDataTargetMotiveInPast(){
        double dataTargetMotiveInPast = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select count( distinct slip_no) as dataTargetMotiveInPast 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(this.getTargetYear()) + "			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")			  ");
            sql.append(" and product_division in (1,3) ");
            sql.append(" and visit_num = 1			  ");
            if (this.getCategoryId() > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append("   and mc.first_coming_motive_class_id ");
            sql.append("  IN ");
            sql.append(" (select motive_id as dataTargetMotive from data_target_motive where year =" + SQLUtil.convertForSQL(this.getTargetYear()) +
                " and month = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "  and shop_category_id = " +this.getCategoryId());
            sql.append(" and shop_id in (" + this.getShopId() + ")\n");
            sql.append("  and own_flg = 1 )");               

        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                dataTargetMotiveInPast = rs.getDouble("dataTargetMotiveInPast");
            }
        } catch (Exception e) {
        }
        
        return dataTargetMotiveInPast;
    }
    
    /**
     * Get dataTargetMotive In Future field for row 16 of excel report
     * @return double 
     */
    public double getDataTargetMotiveInFuture(){
        double dataTargetMotiveInFuture = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select count( distinct slip_no) as dataTargetMotiveInFuture 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL((this.getTargetYear() - 1)) + "			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "			  ");
            sql.append(" and ds.shop_id in (" + this.getShopId() + ")			  ");
            sql.append(" and product_division in (1,3) ");
            sql.append(" and visit_num = 1			  ");
            if (this.getCategoryId() > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select * from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + this.getCategoryId() + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append("   and mc.first_coming_motive_class_id ");
            sql.append("  IN ");
            sql.append(" (select motive_id as dataTargetMotive from data_target_motive where year =" + SQLUtil.convertForSQL((this.getTargetYear() - 1)) +
                " and month = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "  and shop_category_id = " +this.getCategoryId());
            sql.append(" and shop_id in (" + this.getShopId() + ")\n");
            sql.append("  and own_flg = 1 )");               

        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                dataTargetMotiveInFuture = rs.getDouble("dataTargetMotiveInFuture");
            }
        } catch (Exception e) {
        }
        
        return dataTargetMotiveInFuture;
    }
    /**
     * Get dataTargetMotive field for row 16 of excel report
     * @return double
     */
    public double getDataTargetMotiveInPresent() {
        double dataTargetMotive = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(num) as dataTargetMotive from data_target_motive where year =" + SQLUtil.convertForSQL(this.getTargetYear()) +
                " and month = " + SQLUtil.convertForSQL(this.getTargetMonth()) + "  and shop_category_id = " +this.getCategoryId());
        sql.append(" and shop_id in (" + this.getShopId() + ")\n");
        sql.append("  and own_flg = 1");

        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                dataTargetMotive = rs.getDouble("dataTargetMotive");
            }
        } catch (Exception e) {
        }
        
        return dataTargetMotive;
    }
    
    /**
     * Get total staff is working (past and present) field  for row 31 of excel report
     * @return double
     */
    public int getTotalStaffIsWorking(){
        int totalProduct = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(distinct da.staff_id) as totalProduct");
        sql.append("   from data_schedule da");
        sql.append("   inner join mst_staff st");
        sql.append("   on da.staff_id = st.staff_id");
        sql.append("      where st.shop_id = "+ this.getShopId());
        sql.append("      and da.shift_id > 0");
        sql.append("      and da.delete_date is null");
        sql.append("      and date_part('year', da.schedule_date) =" + this.getTargetYear());
        sql.append("      and date_part('month', da.schedule_date) =" + this.getTargetMonth());

         try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                totalProduct = rs.getInt("totalProduct");
            }
        } catch (Exception e) {
        }
        
        return totalProduct;
    }
    
    /**
     * Get total staff is working (future) field  for row 31 of excel report
     * @return double
     */
    public int getTotalStaffIsWorkingInFuture(){
        int totalProductInFuture = 0;
        int yearInPast =  this.getTargetYear() - 1;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(distinct da.staff_id) as totalProduct");
        sql.append("   from data_schedule da");
        sql.append("   inner join mst_staff st");
        sql.append("   on da.staff_id = st.staff_id");
        sql.append("      where st.shop_id = "+ this.getShopId());
        sql.append("      and da.shift_id > 0");
        sql.append("      and da.delete_date is null");
        sql.append("      and date_part('year', da.schedule_date) =" + yearInPast);
        sql.append("      and date_part('month', da.schedule_date) =" + this.getTargetMonth());

         try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                totalProductInFuture = rs.getInt("totalProduct");
            }
        } catch (Exception e) {
        }
        
        return totalProductInFuture;
    }
    
    /**
     * Get purchasing ratio field(present) for row 38 of excel report
     * @return double
     */
    public double getPurchasingRateInPresent(){
        double purchasingRate = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        sql.append(" select item_sales_rate * 100 as purchasingRate from data_target where year =" + this.getTargetYear() +
                " and month = " + this.getTargetMonth() + "  and shop_category_id = " + this.getCategoryId());
        sql.append(" and shop_id in (" + this.getShopId() + ")\n");
        
        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if(rs.next()){
                purchasingRate = rs.getDouble("purchasingRate");
            }            
        } catch (Exception e) {
        }

        return purchasingRate;
    }
    
    /**
     * Get purchasing unit price field(present) for row 39 of excel report
     * @return int
     */
    public int getPurchasingUnitPriceInPresent(){
        int purchasingUnitPrice = 0;
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder();
        sql.append(" select item_sales_price  as purchasingUnitPrice from data_target where year =" + this.getTargetYear() +
                " and month = " + this.getTargetMonth() + "  and shop_category_id = " + this.getCategoryId());
        sql.append(" and shop_id in (" + this.getShopId() + ")\n");
        
        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if(rs.next()){
                purchasingUnitPrice = rs.getInt("purchasingUnitPrice");
            }            
        } catch (Exception e) {
        }

        return purchasingUnitPrice;
    }
    //IVS_nahoang End add 20150122

    
    public String sqlQueryRepeat(int shopID,int SelectedYear,int SelectedMonth,int shopCategoryId,int monthCount) {
        
        StringBuilder sql = new StringBuilder(1000);
        
        String StartDay = ""+ SelectedYear +"/"+ SelectedMonth +"/01";
        
        sql.append(" SELECT count(*) AS total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
       sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        //IVS_LVTu start add 2015/08/27 Bug #42305
        sql.append(" 							AND EXISTS ");
        sql.append(" 							  (SELECT 1  ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 							     AND dsd.slip_no = ds.slip_no ");
        sql.append(" 							     AND dsd.delete_date IS NULL ");
        sql.append(" 							     AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS reappearance, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 THEN 1 ELSE 0 END), 0) AS new_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND EXISTS ");
        sql.append(" 							  (SELECT 1  ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 							     AND dsd.slip_no = ds.slip_no ");
        sql.append(" 							     AND dsd.delete_date IS NULL ");
        sql.append(" 							     AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date ' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS new_reappearance, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 ");
        sql.append(" 					AND introducer_id IS NOT NULL THEN 1 ELSE 0 END), 0) AS introduce_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 ");
        sql.append(" 					AND introducer_id IS NOT NULL ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND EXISTS ");
        sql.append(" 							  (SELECT 1  ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 							     AND dsd.slip_no = ds.slip_no ");
        sql.append(" 							     AND dsd.delete_date IS NULL ");
        sql.append(" 							     AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date ' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS introduce_reappearance, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num BETWEEN 2 AND 3 THEN 1 ELSE 0 END), 0) AS sub_fixed_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num BETWEEN 2 AND 3 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND EXISTS ");
        sql.append(" 							  (SELECT 1  ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 							     AND dsd.slip_no = ds.slip_no ");
        sql.append(" 							     AND dsd.delete_date IS NULL ");
        sql.append(" 							     AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS sub_fixed_reappearance, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num >= 4 THEN 1 ELSE 0 END), 0) AS fixed_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num >= 4 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND EXISTS ");
        sql.append(" 							  (SELECT 1  ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 							     AND dsd.slip_no = ds.slip_no ");
        sql.append(" 							     AND dsd.delete_date IS NULL ");
        sql.append(" 							     AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS fixed_reappearance ");
        sql.append(" FROM ");
        sql.append("   (SELECT DISTINCT ds.sales_date ,ds.customer_id ,ds.insert_date ,mc.introducer_id ,mc.birthday ,mc.sex , ");
        sql.append(" 	 (SELECT count(slip_no) + coalesce(max(before_visit_num),0) ");
        sql.append(" 	  FROM data_sales ");
        sql.append(" 	  INNER JOIN mst_customer using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division = 1 ");
        sql.append(" 		   LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (1) ");
        sql.append(" 			 AND mstc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		   UNION ALL SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division IN (5, ");
        sql.append(" 										6) ");
        sql.append(" 		   LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (5, ");
        sql.append(" 										  6) ");
        sql.append(" 			 AND mscc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 			AND EXISTS ");
        sql.append(" 				(SELECT 1  ");
        sql.append(" 				FROM data_sales_detail dsd ");
        sql.append(" 				WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 				AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 				AND dsd.delete_date IS NULL ");
        sql.append(" 				AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 		AND data_sales.delete_date IS NULL ");
        sql.append(" 		AND customer_id = ds.customer_id ");
        //IVS_LVTu start edit 2015/09/09 New request #42498
        //sql.append(" 		AND data_sales.shop_id= ds.shop_id ");
        sql.append(" 		AND (data_sales.sales_date < ds.sales_date ");
        sql.append(" 			 OR (data_sales.sales_date = ds.sales_date ");
        sql.append(" 				 AND data_sales.insert_date <= ds.insert_date))) AS visit_num ");
        sql.append("    FROM data_sales ds ");
        sql.append("    JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("            And EXISTS(SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND product_division IN (1) ");
        sql.append(" 		  AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		UNION ALL SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND dsd.product_division IN (5, 6)  ");
        sql.append(" 		  AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 		  AND EXISTS ");
        sql.append(" 			(SELECT 1  ");
        sql.append(" 			FROM data_sales_detail dsd ");
        sql.append(" 			WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 			AND dsd.slip_no = ds.slip_no ");
        sql.append(" 			AND dsd.delete_date IS NULL ");
        sql.append(" 			AND dsd.product_division IN (1,5,6)) ");
        //IVS_LVTu end add 2015/08/27 Bug #42305
        sql.append(" 	 AND ds.delete_date IS NULL ");
        sql.append(" 	 AND mc.customer_no <> '0' ");
        sql.append(" 	 AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 	 AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'- interval '" + (monthCount - 1) +" month' -interval '1 day') t ");
        
        return sql.toString();
    }
    
    
    public String sqlQueryNetRepeat(int shopID,int SelectedYear,int SelectedMonth,int shopCategoryId,int monthCount) {
        
        StringBuilder sql = new StringBuilder(1000);
        
        String StartDay = ""+ SelectedYear +"/"+ SelectedMonth +"/01";
        
        sql.append(" SELECT  ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 and mobile_flag >0 THEN 1 ELSE 0 END), 0) AS net_new_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 and mobile_flag >0 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
       sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("                                                       And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        //IVS_LVTu start add 2015/08/27 Bug #42305
        sql.append(" 							AND EXISTS ");
        sql.append(" 							  (SELECT 1  ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 							     AND dsd.slip_no = ds.slip_no ");
        sql.append(" 							     AND dsd.delete_date IS NULL ");
        sql.append(" 							     AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date ' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS net_new_reappearance ");
        sql.append(" FROM ");
        sql.append("   (SELECT DISTINCT ds.sales_date ,ds.customer_id ,ds.insert_date ,mc.introducer_id ,mc.birthday ,mc.sex ,dr.mobile_flag, ");
        sql.append(" 	 (SELECT count(slip_no) + coalesce(max(before_visit_num),0) ");
        sql.append(" 	  FROM data_sales ");
        sql.append(" 	  INNER JOIN mst_customer using(customer_id) ");
       sql.append("       WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("               And EXISTS(SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division = 1 ");
        sql.append(" 		   LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (1) ");
        sql.append(" 			 AND mstc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		   UNION ALL SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division IN (5, ");
        sql.append(" 										6) ");
        sql.append(" 		   LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (5, ");
        sql.append(" 										  6) ");
        sql.append(" 			 AND mscc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 			AND EXISTS ");
        sql.append(" 			(SELECT 1  ");
        sql.append(" 			FROM data_sales_detail dsd ");
        sql.append(" 			WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			AND dsd.delete_date IS NULL ");
        sql.append(" 			AND dsd.product_division IN (1,5,6)) ");
        sql.append(" 		AND data_sales.delete_date IS NULL ");
        sql.append(" 		AND customer_id = ds.customer_id ");
        //sql.append(" 		AND data_sales.shop_id= ds.shop_id ");
        sql.append(" 		AND (data_sales.sales_date < ds.sales_date ");
        sql.append(" 			 OR (data_sales.sales_date = ds.sales_date ");
        sql.append(" 				 AND data_sales.insert_date <= ds.insert_date))) AS visit_num ");
        sql.append("    FROM data_sales ds ");
        sql.append("     inner join data_reservation dr on ds.slip_no= dr.slip_no and ds.shop_id = dr.shop_id ");
        sql.append("    JOIN mst_customer mc  on ds.customer_id = mc.customer_id  ");
        sql.append("    WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("    And EXISTS(SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND product_division IN (1) ");
        sql.append(" 		  AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		UNION ALL SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND dsd.product_division IN (5, 6)  ");
        sql.append(" 		  AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 		AND EXISTS ");
        sql.append(" 		(SELECT 1  ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		AND dsd.delete_date IS NULL ");
        sql.append(" 		AND dsd.product_division IN (1,5,6)) ");
        //IVS_LVTu end add 2015/08/27 Bug #42305
        sql.append(" 	 AND ds.delete_date IS NULL ");
        sql.append(" 	 AND mc.customer_no <> '0' ");
        sql.append(" 	 AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 	 AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'- interval '" + (monthCount - 1) +" month' -interval '1 day') t ");
        //IVS_LVTu end edit 2015/09/09 New request #42498
        
        return sql.toString();
    }
    public String getKarteSQL (String shopIdList,Integer karteDay,Integer selectedYear,Integer selectedMonth,Integer shopCategoryId,Boolean isPrev) {
        StringBuffer sql = new StringBuffer();
        String startDayOfMonth = selectedYear +"-"+selectedMonth+"-01";
        
        sql.append(" select count(distinct slip_no) as cnt from view_data_sales_detail_valid ds where \n");
        if(isPrev) {
            sql.append(" sales_date <=  date'"+startDayOfMonth+"' + interval '1 month' - interval '1 day' -interval '1 year'  \n");
            sql.append(" and sales_date >=    date'"+startDayOfMonth+"' + interval '1 month' - interval '1 day' - interval '"+karteDay+" day' - interval '1 year'\n");
        }else {
            sql.append(" sales_date <=  date'"+startDayOfMonth+"' + interval '1 month' - interval '1 day'  \n");
            sql.append(" and sales_date >=    date'"+startDayOfMonth+"' + interval '1 month' - interval '1 day' - interval '"+karteDay+" day'\n");
        }
        if(shopCategoryId != 0){
            sql.append(" and not exists (select 1 from data_sales_detail ds1  \n");
            sql.append(" where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id  \n");
            sql.append(" and ds1.product_division in(2,4)) \n");
            
        }
        sql.append(" and ds.shop_id in("+shopIdList+")");
        sql.append(" group by ds.shop_id \n");
        return sql.toString();
    }
    public String getKarteSettingSQL(String shopIdList,Integer selectedYear,Integer selectedMonth,Integer shopCategoryId) {
        StringBuffer sql = new StringBuffer();
        
        sql.append(" select coalesce(karte1_day,0) as karte1_day,coalesce(karte2_day,0) as karte2_day,  \n");
        sql.append(" coalesce(karte3_day,0) as karte3_day,coalesce(karte4_day,0) as karte4_day,  \n");
        sql.append(" coalesce(karte1_value,0) as karte1_value,coalesce(karte2_value,0) as karte2_value,  \n");
        sql.append(" coalesce(karte3_value,0) as karte3_value,coalesce(karte4_value,0) as karte4_value  \n");
        sql.append(" from   \n");
        sql.append(" data_target where year =  "+selectedYear+"  \n");
        sql.append(" and month =  "+selectedMonth+"  \n");
        sql.append(" and shop_id in ("+shopIdList+")   \n");
        sql.append(" and shop_category_id = "+shopCategoryId+"  \n");
        
        return sql.toString();
    }
    public String getNokoberuSQL (String ShopIdList,Integer selectedYear,Integer selectedMonth,Integer shopCategoryId,Boolean isPrev) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(distinct ds.customer_id) as cnt from view_data_sales_detail_valid ds  \n");
        if(isPrev) {
            sql.append(" where date_part('year',sales_date + interval '1 year') = "+selectedYear+" \n");
        }else {
            sql.append(" where date_part('year',sales_date) = "+selectedYear+"  \n");
        }
        sql.append(" and date_part('month',sales_date) ="+selectedMonth+" \n");
         sql.append(" and ds.product_division in(2)");
        if(shopCategoryId>0) {
            sql.append(" and exists(	 \n");
            sql.append(" 	select 1 from  \n");
            sql.append(" 	view_data_sales_detail_valid ds1  \n");
            sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n"); 	
            sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id  \n");
            sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
            sql.append(" 	and mic.shop_category_id = "+shopCategoryId+" \n");
            sql.append(" 	)	 \n");
        }
       
        return sql.toString();
     }
    
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Integer getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(Integer targetMonth) {
        this.targetMonth = targetMonth;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}

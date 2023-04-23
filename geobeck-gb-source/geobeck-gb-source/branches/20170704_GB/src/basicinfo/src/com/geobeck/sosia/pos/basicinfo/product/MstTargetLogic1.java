/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstShopRelation;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ntluc
 */
public class MstTargetLogic1 {
    public MstTargetLogic2 logic2 = new MstTargetLogic2();
    public void ExportExcel(String fileName,MstShop selectedShop,int SelectedYear,int SelectedMonth,int shopCategoryId,String shopCategoryName,int n){
        int StartMonth = 1;
        ConnectionWrapper con = SystemInfo.getConnection();
        ResultSetWrapper rs = null;
        StringBuffer sql = null;
        //テンプレートとなるファイルをセット
        JExcelApi jx = new JExcelApi(fileName);
        jx.setTemplateFile("/report/" + fileName + ".xls");
        
        try {
            //1.0.初月の値を取る。
            sql = new StringBuffer();
            sql.append(" select period_month from mst_account_setting ");

            rs = con.executeQuery(sql.toString());
            rs.next();
            if (rs.getInt("period_month") != 12) {
                StartMonth = rs.getInt("period_month")+1;
            }
           
            
            String StartDayOfSelected = "'" + SelectedYear + "-" + SelectedMonth + "-01'";

            //1.1.1年間総売上高																																

            //今年:
            //"+SQLUtil.convertForSQL(StartMonth)+">"+StartMonth+"場合：
             sql = new StringBuffer();
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append(" select sum(discount_detail_value_no_tax)  as sales_value from view_data_sales_detail_valid ds ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append(" and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
                //Luc start edit 20150629 #38737
                //sql.append(" and product_division in(1,2)");
                sql.append(" and product_division in(1,2,3,4)");
                //Luc end edit 20150629 #38737
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and  ");
                    sql.append(" ( ");
                    sql.append(" exists ( ");
                    sql.append("         select 1 from ");
                    sql.append("         view_data_sales_detail_valid ds1 ");
                    sql.append("         inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 ");
                    sql.append("         inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id ");
                    sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no ");
                    sql.append("         and mtc.shop_category_id = " + shopCategoryId + " ");
                    sql.append("         )  ");
                    sql.append(" or  ");
                    sql.append(" exists( ");
                    sql.append("         select 1 from ");
                    sql.append("         view_data_sales_detail_valid ds1 ");
                    sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  ");
                    sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id ");
                    sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no ");
                    sql.append("         and mic.shop_category_id = " + shopCategoryId + " ");
                    sql.append("         ) ");
                    sql.append(" ) ");
                }
            } else {
               
                sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds \n");
                sql.append(" where ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + ")\n");
                sql.append(" or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                //sql.append(" and product_division in(1,2)");
                sql.append(" and product_division in(1,2,3,4)");
                //Luc end edit 20150629 #38737
                if (shopCategoryId > 0) {
                    sql.append(" and \n");
                    sql.append(" (	\n");
                    sql.append(" exists (	\n");
                    sql.append("         select 1 from \n");
                    sql.append("         view_data_sales_detail_valid ds1\n");
                    sql.append("         inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                    sql.append("         inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                    sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append("         and mtc.shop_category_id = " + shopCategoryId + "	\n");
                    sql.append("         ) 	\n");
                    sql.append(" or \n");
                    sql.append(" exists(\n");
                    sql.append("         select 1 from\n");
                    sql.append("         view_data_sales_detail_valid ds1\n");
                    sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                    sql.append("         and mic.shop_category_id = " + shopCategoryId + "\n");
                    sql.append("         )\n");
                    sql.append(" )\n");
                }
            }
            double sales_value = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                sales_value = rs.getDouble("sales_value");
            } catch (Exception e) {
            }
            jx.setValue(9, 3, sales_value);
            //目標																														

            sql = new StringBuffer();
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                //"+SQLUtil.convertForSQL(StartMonth)+">"+StartMonth+"場合：
                sql.append(" select sum(technic+item)  as target_value from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + "  and month <=" + SQLUtil.convertForSQL(SelectedMonth) +(StartMonth!=1?(" and month >=" + SQLUtil.convertForSQL(StartMonth) + ""):""));
                sql.append(" and shop_category_id =" + shopCategoryId + "and shop_id in (" + selectedShop.getShopID() + ")");
            } else {
                sql.append(" select sum(technic+item)  as target_value	\n");
                sql.append(" from data_target where ((year = " + SQLUtil.convertForSQL(SelectedYear) + " and month<=" + SQLUtil.convertForSQL(SelectedMonth) + ")	\n");
                sql.append(" or( year =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and month >= " + SQLUtil.convertForSQL(StartMonth) + " ))	\n");
                sql.append(" and shop_category_id =" + shopCategoryId + "\n");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")	\n");

            }
            double target_value = 0;

            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                target_value = rs.getDouble("target_value");
            } catch (Exception e) {
            }
            jx.setValue(11, 3, target_value);
            if (n == 0) {
                //昨年
                //"+SQLUtil.convertForSQL(StartMonth)+">"+StartMonth+"場合：
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql = new StringBuffer();
                    sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds \n");
                    sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1\n");
                    sql.append(" and date_part('month',sales_date) <=  " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
                    if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                    sql.append(" and shop_id in (" + selectedShop.getShopID() + ") \n");
                    //sql.append(" and product_division in(1,2)");
                    sql.append(" and product_division in(1,2,3,4)");
                    //Luc end edit 20150629 #38737
                    if (shopCategoryId > 0) {
                        sql.append(" and  \n");
                        sql.append(" ( \n");
                        sql.append(" exists ( \n");
                        sql.append("         select 1 from \n");
                        sql.append("         view_data_sales_detail_valid ds1 \n");
                        sql.append("         inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                        sql.append("         inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                        sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
                        sql.append("         and mtc.shop_category_id = " + shopCategoryId + " \n");
                        sql.append("         ) 	 \n");
                        sql.append(" or \n");
                        sql.append(" exists( \n");
                        sql.append("         select 1 from  \n");
                        sql.append("         view_data_sales_detail_valid ds1 \n");
                        sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                        sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                        sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
                        sql.append("         and mic.shop_category_id = " + shopCategoryId + "	 \n");
                        sql.append("         )	 \n");
                        sql.append(" ) 	 \n");
                    }
                } else {
                    sql = new StringBuffer();
                    sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds\n");
                    sql.append(" where ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + ")\n");
                    sql.append(" or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-2  and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + "))\n");
                    sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                    //sql.append(" and product_division in(1,2)");
                    sql.append(" and product_division in(1,2,3,4)");
                    //Luc end edit 20150629 #38737
                    if (shopCategoryId > 0) {
                        sql.append(" and \n");
                        sql.append(" (\n");
                        sql.append(" exists (\n");
                        sql.append("         select 1 from\n");
                        sql.append("         view_data_sales_detail_valid ds1\n");
                        sql.append("         inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                        sql.append("         inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                        sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                        sql.append("         and mtc.shop_category_id = " + shopCategoryId + "\n");
                        sql.append("         )\n");
                        sql.append(" or \n");
                        sql.append(" exists(\n");
                        sql.append("         select 1 from\n");
                        sql.append("         view_data_sales_detail_valid ds1\n");
                        sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                        sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                        sql.append("         where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                        sql.append("         and mic.shop_category_id = " + shopCategoryId + "\n");
                        sql.append("         )\n");
                         sql.append("   )\n");
                    }

                }

                rs = con.executeQuery(sql.toString());
                rs.next();
                jx.setValue(13, 3, rs.getDouble("sales_value"));
            }

            //1.1.2.当月総売上高																																		
            //今年:	
            sql = new StringBuffer();
            sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds \n");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " \n");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")	 \n");
             //sql.append(" and product_division in(1,2)");
            sql.append(" and product_division in(1,2,3,4)");
            //Luc end edit 20150629 #38737
            if (shopCategoryId > 0) {
                sql.append(" and 	 \n");
                sql.append(" (	 \n");
                sql.append(" exists (	 \n");
                sql.append(" 	select 1 from 	 \n");
                sql.append(" 	view_data_sales_detail_valid ds1 \n");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1  \n");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + " \n");
                sql.append(" 	)  \n");
                sql.append(" or  \n");
                sql.append(" exists( \n");
                sql.append(" 	select 1 from  \n");
                sql.append(" 	view_data_sales_detail_valid ds1 \n");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id  \n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + " \n");
                sql.append(" 	) \n");
                sql.append(" ) 	 \n");
            }
            double sales_value_past = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                sales_value_past = rs.getDouble("sales_value");
            } catch (Exception e) {
            }
            jx.setValue(9, 4, sales_value_past);
            //目標
            sql = new StringBuffer();
            sql.append(" select sum(technic+item)  as sales_value from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + "  and month =" + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append(" and shop_category_id =" + shopCategoryId + " and shop_id in (" + selectedShop.getShopID() + ") \n");
            double sales_value_current = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                sales_value_current = rs.getDouble("sales_value");
            } catch (Exception e) {
            }
            jx.setValue(11, 4, sales_value_current);
            //昨年
            double sales_value_future = 0;
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds \n");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                //Luc start edit 20150629 #38737
                //sql.append(" and product_division in(1,2)");
                sql.append(" and product_division in(1,2,3,4)");
                //Luc end edit 20150629 #38737
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                if (shopCategoryId > 0) {
                    sql.append(" and \n");
                    sql.append(" (	\n");
                    sql.append(" exists (	\n");
                    sql.append(" 	select 1 from \n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 	) \n");
                    sql.append(" or\n");
                    sql.append(" exists(\n");
                    sql.append(" 	select 1 from\n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 	)\n");
                    sql.append(" ) \n");
                }
                
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    sales_value_future = rs.getDouble("sales_value");
                } catch (Exception e) {
                }
                jx.setValue(13, 4, sales_value_future);
            }
            //1.1.3.当月技術売上高																																		
            //今年:	
            sql = new StringBuffer();
            sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds\n");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            //Luc start edit 20150629 #38737
            //sql.append(" and product_division in(1)	\n");
            sql.append(" and product_division in(1,3)	\n");
            //Luc end edit 20150629 #38737
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            if (shopCategoryId > 0) {
                sql.append(" and exists (\n");
                sql.append(" 	select 1 from\n");
                sql.append(" 	view_data_sales_detail_valid ds1\n");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "\n");
                sql.append(" 	) \n");
            }
            sales_value = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                sales_value = rs.getDouble("sales_value");
            } catch (Exception e) {
            }
            jx.setValue(9, 5, sales_value);
            //目標
            sql = new StringBuffer();
            sql.append(" select sum(technic) as technic_value from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "   and shop_category_id = " + shopCategoryId + "\n");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(11, 5, rs.getDouble("technic_value"));
            //昨年	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds\n");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                //Luc start edit 20150629 #38737
                //sql.append(" and product_division in(1)	\n");
                sql.append(" and product_division in(1,3)	\n");
                //Luc end edit 20150629 #38737
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (\n");
                    sql.append(" 	select 1 from \n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 	) \n");
                }
                sales_value = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    sales_value = rs.getDouble("sales_value");
                } catch (Exception e) {
                }
                jx.setValue(13, 5, sales_value);
            }
            //1.1.4.当月店販売上高																																		

            //今年:	
            sql = new StringBuffer();
            sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds\n");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            //Luc start edit 20150624 #38252
            //sql.append(" and product_division in(2)\n");
            sql.append(" and product_division in(2,4)\n");
            //Luc end edit 20150624 #38252
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            if (shopCategoryId > 0) {
                sql.append(" and exists(\n");
                sql.append(" 	select 1 from\n");
                sql.append(" 	view_data_sales_detail_valid ds1\n");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "\n");
                sql.append(" 	)\n");
            }
            sales_value = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                sales_value = rs.getDouble("sales_value");
            } catch (Exception e) {
            }
            jx.setValue(9, 6, sales_value);
            //目標
            sql = new StringBuffer();
            //Luc start edit 20150629
            //IVS_LVTu start edit 2015/08/26 Bug #42273
            sql.append(" select coalesce(sum(item),0) as item_value from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "\n");
            //sql.append(" select coalesce(sum(item),0)+ coalesce (sum(technic),0) as item_value from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "\n");
            //IVS_LVTu end edit 2015/08/26 Bug #42273
            //Luc end edit 20150629 
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(11, 6, rs.getDouble("item_value"));
            //昨年
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select sum(discount_detail_value_no_tax) as sales_value from view_data_sales_detail_valid ds\n");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                //Luc start edit 20150629 #38252
                //sql.append(" and product_division in(2)	\n");
                sql.append(" and product_division in(2,4)	\n");
                //Luc end edit 20150629 #38252
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                if (shopCategoryId > 0) {
                    sql.append(" and exists(\n");
                    sql.append(" 	select 1 from \n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 	)\n");
                }
                sales_value = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    sales_value = rs.getDouble("sales_value");
                } catch (Exception e) {
                }
                jx.setValue(13, 6, sales_value);
            }
            //1.1.5デシル①売上高比率																																		
            //今年：																																
            //位10%																																
            //SQL:
            sql = new StringBuffer();
            sql.append("select count(*)/10::float as block\n");
            sql.append("from\n");
            sql.append("	(\n");
            sql.append("	select distinct customer_id ");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            sql.append("	and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "	\n");
            sql.append("	and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            if (shopCategoryId > 0) {
                sql.append("	and\n");
                sql.append("	(\n");
                sql.append("	exists (\n");
                sql.append("		select 1 from\n");
                sql.append("		view_data_sales_detail_valid ds1\n");
                sql.append("		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                sql.append("		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                sql.append("		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append("		and mtc.shop_category_id = " + shopCategoryId + "\n");
                sql.append("		)\n");
                sql.append("	or \n");
                sql.append("	exists(	\n");
                sql.append("		select 1 from\n");
                sql.append("		view_data_sales_detail_valid ds1\n");
                sql.append("		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                sql.append("		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                sql.append("		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append("		and mic.shop_category_id = " + shopCategoryId + "\n");
                sql.append("		)\n");
                sql.append("	) \n");
            }
            sql.append("	 group by ds.customer_id\n");
            sql.append("	) cnt\n");
            BigDecimal bd = null;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                bd = new BigDecimal(rs.getDouble("block"));
            } catch (Exception e) {
            }
            long block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            //売上高比率																																
            //SQL:	
            sql = new StringBuffer();
            sql.append("select\n");
            sql.append("((	\n");

            sql.append("	select  sum(sales_value) AS sales_value\n");
            sql.append("	from\n");
            sql.append("	(\n");
            sql.append("		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value\n");
            sql.append("           from view_data_sales_detail_valid  ds \n");
            sql.append("           JOIN mst_customer b USING (customer_id)");
            sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("            AND b.customer_no <> '0' ");
            sql.append("		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append("		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            if (shopCategoryId > 0) {
                sql.append("		and \n");
                sql.append("		(\n");
                sql.append("		exists (\n");
                sql.append("			select 1 from\n");
                sql.append("			view_data_sales_detail_valid ds1\n");
                sql.append("			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                sql.append("			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                sql.append("			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append("			and mtc.shop_category_id = " + shopCategoryId + "\n");
                sql.append("			) \n");
                sql.append("		or \n");
                sql.append("		exists(	\n");
                sql.append("			select 1 from \n");
                sql.append("			view_data_sales_detail_valid ds1\n");
                sql.append("			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                sql.append("			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                sql.append("			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                sql.append("			and mic.shop_category_id = " + shopCategoryId + "\n");
                sql.append("			)\n");
                sql.append("		)\n");
            }
            sql.append("		group by ds.customer_id\n");
            sql.append("		ORDER BY sales_value DESC \n");
            sql.append("		offset 0*" + block + "\n");
            sql.append("		limit " + block + "\n");
            sql.append("	)a\n");
            sql.append(")*100/\n");
            sql.append("(\n");
            sql.append("	select  sum(sales_value) AS sales_value	\n");
            sql.append("	from	\n");
            sql.append("	(\n");
            sql.append("		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value \n");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            sql.append("		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "	\n");
            sql.append("		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            if (shopCategoryId > 0) {
                sql.append("		and \n");
                sql.append("		(\n");
                sql.append("		exists (\n");
                sql.append("			select 1 from\n");
                sql.append("			view_data_sales_detail_valid ds1\n");
                sql.append("			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                sql.append("			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                sql.append("			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                sql.append("			and mtc.shop_category_id = " + shopCategoryId + "\n");
                sql.append("			)\n");
                sql.append("		or\n");
                sql.append("		exists(	\n");
                sql.append("			select 1 from \n");
                sql.append("			view_data_sales_detail_valid ds1\n");
                sql.append("			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                sql.append("			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                sql.append("			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                sql.append("			and mic.shop_category_id = " + shopCategoryId + "\n");
                sql.append("			)\n");
                sql.append("		) \n");
            }
            sql.append("	)a\n");
            sql.append(" )) as decyl_1_rate\n");

            double decyl_1_rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_1_rate = rs.getDouble("decyl_1_rate");
            } catch (Exception e) {
            }
            jx.setValue(9, 8, decyl_1_rate);
            //目標：
            sql = new StringBuffer();
            sql.append(" select decyl_1_rate from data_target where year = " + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "\n");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            rs = con.executeQuery(sql.toString());
            rs.next();
            decyl_1_rate = 0;
            try {
                decyl_1_rate = rs.getDouble("decyl_1_rate");
            } catch (Exception e) {
            }
            jx.setValue(11, 8, decyl_1_rate);
            //昨年：
            if (n == 0) {
                sql = new StringBuffer();
                sql.append("select count(*)/10::float as block\n");
                sql.append("from\n");
                sql.append("	(\n");
                sql.append("	select distinct customer_id ");
                sql.append("   from view_data_sales_detail_valid  ds \n");
                sql.append("   JOIN mst_customer b USING (customer_id)");
                sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("   AND b.customer_no <> '0' ");
                sql.append("	and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1	\n");
                sql.append("	and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                if (shopCategoryId > 0) {
                    sql.append("	and\n");
                    sql.append("	(\n");
                    sql.append("	exists (\n");
                    sql.append("		select 1 from\n");
                    sql.append("		view_data_sales_detail_valid ds1\n");
                    sql.append("		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                    sql.append("		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                    sql.append("		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append("		and mtc.shop_category_id = " + shopCategoryId + "\n");
                    sql.append("		)\n");
                    sql.append("	or \n");
                    sql.append("	exists(	\n");
                    sql.append("		select 1 from\n");
                    sql.append("		view_data_sales_detail_valid ds1\n");
                    sql.append("		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append("		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append("		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append("		and mic.shop_category_id = " + shopCategoryId + "\n");
                    sql.append("		)\n");
                    sql.append("	) \n");
                }
                sql.append("	 group by ds.customer_id\n");
                sql.append("	) cnt\n");
                bd = null;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    bd = new BigDecimal(rs.getDouble("block"));
                } catch (Exception e) {
                }

                block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                //比率:
                sql = new StringBuffer();
                sql.append(" select\n");
                sql.append(" ((\n");
                sql.append(" 	select  sum(sales_value) AS sales_value\n");
                sql.append(" 	from\n");
                sql.append(" 	(\n");
                sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value\n");
                sql.append(" from view_data_sales_detail_valid  ds \n");
                sql.append(" JOIN mst_customer b USING (customer_id)");
                sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("  AND b.customer_no <> '0' ");
                sql.append(" 		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                if (shopCategoryId > 0) {
                    sql.append(" 		and\n");
                    sql.append(" 		(\n");
                    sql.append(" 		exists (\n");
                    sql.append(" 			select 1 from\n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                    sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 			) \n");
                    sql.append(" 		or\n");
                    sql.append(" 		exists(	\n");
                    sql.append(" 			select 1 from\n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                    sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 			)\n");
                    sql.append(" 		)\n");
                }
                sql.append(" 		group by ds.customer_id\n");
                sql.append(" 		ORDER BY sales_value DESC\n");
                sql.append(" 		offset 0* " + block + "\n");
                sql.append(" 		limit " + block + "\n");
                sql.append(" 	)a\n");
                sql.append(" )*100/\n");
                sql.append(" (	\n");
                sql.append(" 	select  sum(sales_value) AS sales_value\n");
                sql.append(" 	from\n");
                sql.append(" 	(\n");
                sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value\n");
                sql.append("           from view_data_sales_detail_valid  ds \n");
                sql.append("           JOIN mst_customer b USING (customer_id)");
                sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("           AND b.customer_no <> '0' ");
                sql.append(" 		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                if (shopCategoryId > 0) {
                    sql.append(" 		and\n");
                    sql.append(" 		(\n");
                    sql.append(" 		exists (\n");
                    sql.append(" 			select 1 from\n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 			) \n");
                    sql.append(" 		or \n");
                    sql.append(" 		exists(\n");
                    sql.append(" 			select 1 from\n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no\n");
                    sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "\n");
                    sql.append(" 			)\n");
                    sql.append(" 		)\n");
                }
                sql.append(" 	)a\n");
                sql.append(" )) as decyl_1_rate \n");
                decyl_1_rate = 0;
                try {
                    rs = con.executeQuery(sql.toString());

                    rs.next();
                    decyl_1_rate = rs.getDouble("decyl_1_rate");
                } catch (Exception e) {
                }
                jx.setValue(13, 8, decyl_1_rate);
            }
            //1.1.6デシル②売上高比率	
            //位10%			
            //SQL:	
            sql = new StringBuffer();
            sql.append(" select count(*)/10::float as block			  ");
            sql.append(" from 			  ");
            sql.append(" 	(		  ");
            sql.append(" 	select distinct customer_id ");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            sql.append(" 	and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "		  ");
            sql.append(" 	and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" 	group by ds.customer_id		  ");
            sql.append(" 	) cnt		  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            bd = null;
            bd = new BigDecimal(rs.getDouble("block"));
            block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            //売上高比率			 
            // SQL:			 
            sql = new StringBuffer();
            sql.append(" select 			  ");
            sql.append(" ((			  ");
            sql.append(" 			  ");
            sql.append(" 	select  sum(sales_value) AS sales_value		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            sql.append(" 		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "	  ");
            sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		and 	  ");
                sql.append(" 		(	  ");
                sql.append(" 		exists (	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)   ");
                sql.append(" 		or 	  ");
                sql.append(" 		exists(	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)  ");
                sql.append(" 		) 	  ");
            }
            sql.append(" 		group by ds.customer_id	  ");
            sql.append(" 		ORDER BY sales_value DESC 	  ");
            sql.append(" 		offset 1* " + block + "	  ");
            sql.append(" 		limit " + block + "  ");
            sql.append(" 	)a		  ");
            sql.append(" )*100/			  ");
            sql.append(" (			  ");
            sql.append(" 	select  sum(sales_value) AS sales_value		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            sql.append(" 		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "	  ");
            sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		and 	  ");
                sql.append(" 		(	  ");
                sql.append(" 		exists (	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)   ");
                sql.append(" 		or 	  ");
                sql.append(" 		exists(	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)  ");
                sql.append(" 		) 	  ");
            }
            sql.append(" 	)a		  ");
            sql.append(" )) as decyl_2_rate			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(9, 9, rs.getDouble("decyl_2_rate"));

            //目標：	
            sql = new StringBuffer();
            sql.append(" select decyl_2_rate from data_target where year = " + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            double decyl_2_rate = 0;
            try {
                decyl_2_rate = rs.getDouble("decyl_2_rate");

            } catch (Exception e) {
            }
            jx.setValue(11, 9, decyl_2_rate);
            // 昨年：
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count(*)/10::float as block			  ");
                sql.append(" from 			  ");
                sql.append(" 	(		  ");
                sql.append(" 	select distinct customer_id ");
                sql.append(" from view_data_sales_detail_valid  ds \n");
                sql.append(" JOIN mst_customer b USING (customer_id)");
                sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("  AND b.customer_no <> '0' ");
                sql.append(" 	and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1		  ");
                sql.append(" 	and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" 	group by ds.customer_id		  ");
                sql.append(" 	) cnt		  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                bd = null;
                bd = new BigDecimal(rs.getDouble("block"));
                block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();

                sql = new StringBuffer();
                sql.append(" select 			  ");
                sql.append(" ((			  ");
                sql.append(" 			  ");
                sql.append(" 	select  sum(sales_value) AS sales_value		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
                sql.append("            from view_data_sales_detail_valid  ds \n");
                sql.append("           JOIN mst_customer b USING (customer_id)");
                sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("           AND b.customer_no <> '0' ");
                sql.append(" 		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1	  ");
                sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		and 	  ");
                    sql.append(" 		(	  ");
                    sql.append(" 		exists (	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)   ");
                    sql.append(" 		or 	  ");
                    sql.append(" 		exists(	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)  ");
                    sql.append(" 		) 	  ");
                }
                sql.append(" 		group by ds.customer_id	  ");
                sql.append(" 		ORDER BY sales_value DESC 	  ");
                sql.append(" 		offset 1* " + block + "	  ");
                sql.append(" 		limit " + block + "	  ");
                sql.append(" 	)a		  ");
                sql.append(" )*100/			  ");
                sql.append(" (			  ");
                sql.append(" 	select  sum(sales_value) AS sales_value		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
                sql.append("           from view_data_sales_detail_valid  ds \n");
                sql.append("           JOIN mst_customer b USING (customer_id)");
                sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("           AND b.customer_no <> '0' ");
                sql.append(" 		and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1	  ");
                sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		and 	  ");
                    sql.append(" 		(	  ");
                    sql.append(" 		exists (	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)   ");
                    sql.append(" 		or 	  ");
                    sql.append(" 		exists(	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)  ");
                    sql.append(" 		) 	  ");
                }
                sql.append(" 	)a		  ");
                sql.append(" )) as decyl_2_rate			  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_2_rate = 0;
                try {
                    decyl_2_rate = rs.getDouble("decyl_2_rate");

                } catch (Exception e) {
                }
                jx.setValue(13, 9, decyl_2_rate);
            }
            //1.1.7デシル③売上高比率					 
            //位10%			 
            //SQL:	
            sql = new StringBuffer();
            sql.append(" select count(*)/10::float as block			  ");
            sql.append(" from 			  ");
            sql.append(" 	(		  ");
            sql.append(" 	select distinct customer_id ");
            sql.append("   from view_data_sales_detail_valid  ds \n");
            sql.append("   JOIN mst_customer b USING (customer_id)");
            sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("   AND b.customer_no <> '0' ");
            sql.append(" 	and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "		  ");
            sql.append(" 	and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" 	group by ds.customer_id		  ");
            sql.append(" 	) as block		  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            bd = null;
            bd = new BigDecimal(rs.getDouble("block"));
            block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            //売上高比率			
            // SQL:	
            sql = new StringBuffer();
            sql.append(" select 			  ");
            sql.append(" (			  ");
            sql.append(" 			  ");
            sql.append(" 	select  sum(sales_value) AS sales_value		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
            sql.append("            from view_data_sales_detail_valid  ds \n");
            sql.append("           JOIN mst_customer b USING (customer_id)");
            sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("           AND b.customer_no <> '0' ");
            sql.append(" 		and  date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "	  ");
            sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		and 	  ");
                sql.append(" 		(	  ");
                sql.append(" 		exists (	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)   ");
                sql.append(" 		or 	  ");
                sql.append(" 		exists(	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)  ");
                sql.append(" 		) 	  ");
            }
            sql.append(" 		group by ds.customer_id	  ");
            sql.append(" 		ORDER BY sales_value DESC 	  ");
            sql.append(" 		offset 2*" + block + "	  ");
            sql.append(" 		limit " + block + "	  ");
            sql.append(" 	)a		  ");
            sql.append(" )*100/			  ");
            sql.append(" (			  ");
            sql.append(" 	select  sum(sales_value) AS sales_value		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
            sql.append("           from view_data_sales_detail_valid  ds \n");
            sql.append("           JOIN mst_customer b USING (customer_id)");
            sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("            AND b.customer_no <> '0' ");
            sql.append(" 		and  date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "	  ");
            sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		and 	  ");
                sql.append(" 		(	  ");
                sql.append(" 		exists (	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)   ");
                sql.append(" 		or 	  ");
                sql.append(" 		exists(	  ");
                sql.append(" 			select 1 from   ");
                sql.append(" 			view_data_sales_detail_valid ds1  ");
                sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                sql.append(" 			)  ");
                sql.append(" 		) 	  ");
            }
            sql.append(" 	)a		  ");
            sql.append(" ) as decyl_3_rate    ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(9, 10, rs.getDouble("decyl_3_rate"));
            //目標：
            sql = new StringBuffer();
            sql.append(" select decyl_3_rate from data_target where year = " + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            double decyl_3_rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_3_rate = rs.getDouble("decyl_3_rate");
            } catch (Exception e) {
            }
            jx.setValue(11, 10, decyl_3_rate);
            //昨年：
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count(*)/10::float as block			  ");
                sql.append(" from 			  ");
                sql.append(" 	(		  ");
                sql.append(" 	select distinct customer_id ");
                sql.append("   from view_data_sales_detail_valid  ds \n");
                sql.append("   JOIN mst_customer b USING (customer_id)");
                sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("   AND b.customer_no <> '0' ");
                sql.append(" 	and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1		  ");
                sql.append(" 	and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" 	group by ds.customer_id		  ");
                sql.append(" 	) as block		  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                bd = null;
                bd = new BigDecimal(rs.getDouble("block"));
                block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();

                sql = new StringBuffer();
                sql.append(" select 			  ");
                sql.append(" ((			  ");
                sql.append(" 	select  sum(sales_value) AS sales_value		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
                sql.append("           from view_data_sales_detail_valid  ds \n");
                sql.append("           JOIN mst_customer b USING (customer_id)");
                sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("           AND b.customer_no <> '0' ");
                sql.append(" 		and  date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1	  ");
                sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		and 	  ");
                    sql.append(" 		(	  ");
                    sql.append(" 		exists (	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)   ");
                    sql.append(" 		or 	  ");
                    sql.append(" 		exists(	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)  ");
                    sql.append(" 		) 	  ");
                }
                sql.append(" 		group by ds.customer_id	  ");
                sql.append(" 		ORDER BY sales_value DESC 	  ");
                sql.append(" 		offset 2* " + block + "	  ");
                sql.append(" 		limit " + block + "	  ");
                sql.append(" 	)a		  ");
                sql.append(" )*100/			  ");
                sql.append(" (			  ");
                sql.append(" 	select  sum(sales_value) AS sales_value		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select sum(coalesce(ds.discount_detail_value_no_tax,0)) as sales_value 	  ");
                sql.append("           from view_data_sales_detail_valid  ds \n");
                sql.append("           JOIN mst_customer b USING (customer_id)");
                sql.append("           where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("           AND b.customer_no <> '0' ");
                sql.append(" 		and  date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1	  ");
                sql.append(" 		and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		and 	  ");
                    sql.append(" 		(	  ");
                    sql.append(" 		exists (	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1   ");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mtc.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)   ");
                    sql.append(" 		or 	  ");
                    sql.append(" 		exists(	  ");
                    sql.append(" 			select 1 from   ");
                    sql.append(" 			view_data_sales_detail_valid ds1  ");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2    ");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id   ");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no   ");
                    sql.append(" 			and mic.shop_category_id = " + shopCategoryId + "  ");
                    sql.append(" 			)  ");
                    sql.append(" 		) 	  ");
                }
                sql.append(" 	)a		  ");
                sql.append(" )) as decyl_3_rate	  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                jx.setValue(13, 10, rs.getDouble("decyl_3_rate"));
            }
            //1.1.8女性客単価					
            //今年:	
            sql = new StringBuffer();
            sql.append(" select 			  ");
            sql.append(" (			  ");
            sql.append(" select sum(discount_detail_value_no_tax) 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
            sql.append(" and mc.sex = 2 			  ");
            sql.append(" and mc.customer_no <>'0' ");
            //Luc start edit 20150626 #38252
            //sql.append(" and ds.product_division in(1) ");
            sql.append(" and ds.product_division in(1,3) ");
            //Luc end edit 20150626 #38252
            if (shopCategoryId > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" )/			  ");
            sql.append(" (			  ");
            sql.append(" select count( distinct slip_no) as customer_count 			  ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
           //Luc start edit 20150626 #38252
            //sql.append(" and ds.product_division in(1) ");
            sql.append(" and ds.product_division in(1,3) ");
            //Luc end edit 20150626 #38252
            sql.append(" and mc.customer_no <>'0' ");
            sql.append(" and mc.sex = 2			  ");
            if (shopCategoryId > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" ) as female_unit_price			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(9, 11, rs.getDouble("female_unit_price"));
            //目標	
            sql = new StringBuffer();
            sql.append(" select coalesce(female_unit_price,0) as female_unit_price from data_target			  ");
            sql.append(" where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            double female_unit_price = 0;
            try {
                female_unit_price = rs.getDouble("female_unit_price");
            } catch (Exception e) {
            }
            jx.setValue(11, 11, female_unit_price);
            //昨年
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select 			  ");
                sql.append(" (			  ");
                sql.append(" select sum(discount_detail_value_no_tax) 			  ");
                sql.append(" from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
                sql.append(" and mc.sex = 2 			  ");
                sql.append(" and mc.customer_no <>'0' ");
               //Luc start edit 20150626 #38252
                //sql.append(" and ds.product_division in(1) ");
                sql.append(" and ds.product_division in(1,3) ");
                //Luc end edit 20150626 #38252
                if (shopCategoryId > 0) {
                    sql.append(" and 			  ");
                    sql.append(" (			  ");
                    sql.append(" exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                    sql.append(" or 			  ");
                    sql.append(" exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                    sql.append(" ) 			  ");
                }
                sql.append(" )/			  ");
                sql.append(" (			  ");
                sql.append(" select count( distinct slip_no) as customer_count 			  ");
                sql.append(" from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
                sql.append(" and mc.sex = 2			  ");
                sql.append(" and mc.customer_no <>'0' ");
               //Luc start edit 20150626 #38252
                //sql.append(" and ds.product_division in(1) ");
                sql.append(" and ds.product_division in(1,3) ");
                //Luc end edit 20150626 #38252
                if (shopCategoryId > 0) {
                    sql.append(" and 			  ");
                    sql.append(" (			  ");
                    sql.append(" exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                    sql.append(" or 			  ");
                    sql.append(" exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                    sql.append(" ) 			  ");
                }
                sql.append(" ) as female_unit_price			  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                try {
                    female_unit_price = rs.getDouble("female_unit_price");
                } catch (Exception e) {
                }
                jx.setValue(13, 11, female_unit_price);
            }
            //1.1.9.新規客数	

            //今年:	
            sql = new StringBuffer();
            //IVS_LVTu start add 2015/08/27 Bug #42305
            //IVS_LVTu start edit 2015/09/09 New request #42498
            /*sql.append("  SELECT coalesce(sum(CASE WHEN visit_num = 1 THEN 1 ELSE 0 END), 0) AS new_customer ");
            sql.append("  from ( ");
            sql.append("  select  ");
            sql.append("  (SELECT count(slip_no) + coalesce(max(before_visit_num),0) ");
            sql.append("        FROM data_sales ");
            sql.append("        INNER JOIN mst_customer using(customer_id) ");
            sql.append("        WHERE 1=1 ");
            if (shopCategoryId > 0) {
                sql.append("    And EXISTS(SELECT 1 ");
                sql.append(" 		FROM data_sales_detail dsd ");
                sql.append(" 		INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append(" 		LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
                sql.append(" 		WHERE dsd.shop_id = data_sales.shop_id ");
                sql.append(" 		  AND dsd.slip_no = data_sales.slip_no ");
                sql.append(" 		  AND dsd.delete_date IS NULL ");
                sql.append(" 		  AND product_division IN (1) ");
                sql.append(" 		  AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
                sql.append(" 		UNION ALL SELECT 1 ");
                sql.append(" 		FROM data_sales_detail dsd ");
                sql.append(" 		INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
                sql.append(" 		LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
                sql.append(" 		WHERE dsd.shop_id = data_sales.shop_id ");
                sql.append(" 		  AND dsd.slip_no = data_sales.slip_no ");
                sql.append(" 		  AND dsd.delete_date IS NULL ");
                sql.append(" 		  AND dsd.product_division IN (5, 6)  ");
                sql.append(" 		  AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
            }
            sql.append("          AND data_sales.delete_date IS NULL ");
            sql.append("          AND customer_id = ds.customer_id ");
            //IVS_LVTu start edit 2015/09/09 New request #42498
            //sql.append("          AND data_sales.shop_id= ds.shop_id ");
            //IVS_LVTu end edit 2015/09/09 New request #42498
            sql.append("          AND (data_sales.sales_date < ds.sales_date ");
            sql.append("               OR (data_sales.sales_date = ds.sales_date ");
            sql.append("                   AND data_sales.insert_date <= ds.insert_date)) ");
            sql.append("                   ) AS visit_num ");*/
            sql.append("  SELECT coalesce(sum(CASE WHEN get_visit_count(ds.customer_id, ds.sales_date) = 1 THEN 1 ELSE 0 END), 0) AS new_customer ");
            sql.append("     FROM data_sales ds ");
            sql.append("     inner jOIN mst_customer mc using(customer_id) ");
            sql.append("     WHERE 1=1 ");
//            sql.append(" select count( distinct slip_no) as new_customer 			  ");
//            sql.append(" from view_data_sales_detail_valid ds			  ");
//            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
//            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "			  ");
//            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
//            sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
//            //sql.append(" and product_division in (1,3) ");
//            sql.append(" and product_division in (1,5,6) ");
//            //Luc start edit 20150626 #38252
//            //sql.append(" and visit_num = 1			  ");
//            sql.append(" and customer_no <>'0'  ");
//            sql.append(" AND get_visit_count(ds.customer_id, ds.sales_date) = 1  ");
            
            //Luc end edit 20150626 #38252
            if (shopCategoryId > 0) {
//                sql.append(" and 			  ");
//                sql.append(" (			  ");
//                sql.append(" exists (			  ");
//                sql.append(" 	select 1 from 		  ");
//                sql.append(" 	view_data_sales_detail_valid ds1		  ");
//                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
//                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
//                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
//                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
//                sql.append(" 	) 		  ");
//                sql.append(" or 			  ");
//                sql.append(" exists(			  ");
//                sql.append(" 	select 1 from 		  ");
//                sql.append(" 	view_data_sales_detail_valid ds1		  ");
//                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
//                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
//                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
//                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
//                sql.append(" 	)		  ");
//                sql.append(" ) 			  ");
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
            sql.append(" 		  AND EXISTS  ");
            sql.append(" 		  (SELECT 1 ");
            sql.append(" 		  FROM data_sales_detail dsd ");
            sql.append(" 		  WHERE dsd.shop_id = ds.shop_id ");
            sql.append(" 		     AND dsd.slip_no = ds.slip_no ");
            sql.append(" 		  AND dsd.delete_date IS NULL ");
            sql.append(" 		  AND dsd.product_division IN (1,5,6)) ");
            sql.append(" 		  AND ds.delete_date IS NULL ");
            sql.append(" 		  AND mc.customer_no <> '0' ");
            sql.append(" 		  AND ds.shop_id IN (" + selectedShop.getShopID() + ") ");
            sql.append(" and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + " ");
            //sql.append(" 		   ) t ");
            //IVS_LVTu end edit 2015/09/09 New request #42498
            //IVS_LVTu end add 2015/08/27 Bug #42305
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            double new_customer = 0;
            try {
                new_customer = rs.getDouble("new_customer");
            } catch (Exception e) {
            }
            jx.setValue(9, 13, new_customer);
            //目標
            sql = new StringBuffer();
            sql.append(" select coalesce(new_customer,0) as new_customer from data_target			  ");
            sql.append(" where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  	 and shop_category_id = " + shopCategoryId + "		  ");

            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")			  ");
            sql.append(" 			  ");
            new_customer = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                new_customer = rs.getDouble("new_customer");
            } catch (Exception e) {
            }
            jx.setValue(11, 13, new_customer);

            //昨年	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count( distinct slip_no) as new_customer 			  ");
                sql.append(" from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
                sql.append(" and product_division in (1,3) ");
               //sql.append(" and visit_num = 1			  ");
                sql.append(" and customer_no <>'0'  ");
                sql.append(" AND get_visit_count(ds.customer_id, ds.sales_date) = 1  ");
                //Luc end edit 20150626 #38252
                if (shopCategoryId > 0) {
                    sql.append(" and 			  ");
                    sql.append(" (			  ");
                    sql.append(" exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                    sql.append(" or 			  ");
                    sql.append(" exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                    sql.append(" ) 			  ");
                }
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                new_customer = 0;
                try {
                    new_customer = rs.getDouble("new_customer");
                } catch (Exception e) {
                }
                jx.setValue(13, 13, new_customer);
            }
            //1.1.10新規比率					 
            //今年：	
            //Luc start delete 20150629 #38737
            //sql = new StringBuffer();
            //sql.append(" 	select 		  ");
            //sql.append(" 		(	  ");
            //sql.append(" 			select count( distinct slip_no) as customer_count   ");
            //sql.append(" 			from view_data_sales_detail_valid ds  ");
            //sql.append(" 			inner join mst_customer mc on ds.customer_id = mc.customer_id   ");
            //sql.append(" 			where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "  ");
            //sql.append(" 			and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "  ");
            //sql.append(" 			and ds.shop_id in (" + selectedShop.getShopID() + ")  ");
            //sql.append(" 			and product_division in (1,3)   ");
            //sql.append(" 			and visit_num = 1  ");
            //if (shopCategoryId > 0) {
            //    sql.append(" 			and exists (  ");
            //    sql.append(" 				select 1 from  ");
            //    sql.append(" 				view_data_sales_detail_valid ds1 ");
            //    sql.append(" 				inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1  ");
            //    sql.append(" 				inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id  ");
            //    sql.append(" 				where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  ");
            //    sql.append(" 				and mtc.shop_category_id = " + shopCategoryId + " ");
            //    sql.append(" 				)  ");
            //}
            //sql.append(" 		)*100/	  ");
            //sql.append(" 		(	  ");
            //sql.append(" 			select count( distinct slip_no) as customer_count   ");
            //sql.append(" 			from view_data_sales_detail_valid ds  ");
            //sql.append(" 			inner join mst_customer mc on ds.customer_id = mc.customer_id   ");
            //sql.append(" 			where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "  ");
            //sql.append(" 			and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "  ");
            //sql.append(" 			and ds.shop_id in (" + selectedShop.getShopID() + ")  ");
            //sql.append(" 			and product_division in (1,3)   ");
            //if (shopCategoryId > 0) {
            //    sql.append(" 			and exists (  ");
            //    sql.append(" 				select 1 from  ");
            //    sql.append(" 				view_data_sales_detail_valid ds1 ");
            //    sql.append(" 				inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1  ");
            //    sql.append(" 				inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id  ");
            //    sql.append(" 				where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  ");
            //    sql.append(" 				and mtc.shop_category_id = " + shopCategoryId + " ");
            //    sql.append(" 				)  ");
            //}
            //sql.append(" 		)::float as new_rate	  ");
            //sql.append(" 			  ");
            double new_rate = 0;
            //try {
            //    rs = con.executeQuery(sql.toString());
            //    rs.next();
            //    new_rate = rs.getDouble("new_rate");
            //} catch (SQLException e) {
            //}

            //jx.setValue(9, 14, new_rate);
            //IVS_LVTu start delete 2015/09/10 New request #42535
            //目標：
//            sql = new StringBuffer();
//            sql.append(" select coalesce(new_rate*100,0) as new_rate from data_target			  ");
//            sql.append(" where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  			  ");
//            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") and shop_category_id = " + shopCategoryId + "			  ");
//            sql.append(" 			  ");
//            rs = con.executeQuery(sql.toString());
//            rs.next();
//            new_rate = 0;
//            try {
//                new_rate = rs.getDouble("new_rate");
//            } catch (Exception e) {
//            }
//            jx.setValue(11, 14, new_rate);
            //IVS_LVTu end delete 2015/09/10 New request #42535
            //// 昨年：
            //if (n == 0) {
            //    sql = new StringBuffer();
            //    sql.append(" select 			  ");
            //    sql.append(" 		(	  ");
            //    sql.append(" 			select count( distinct slip_no) as customer_count   ");
            //    sql.append(" 			from view_data_sales_detail_valid ds  ");
            //    sql.append(" 			inner join mst_customer mc on ds.customer_id = mc.customer_id   ");
            //    sql.append(" 			where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1  ");
            //    sql.append(" 			and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "  ");
            //    sql.append(" 			and ds.shop_id in (" + selectedShop.getShopID() + ")  ");
            //    sql.append(" 			and product_division in (1)   ");
            //    sql.append(" 			and visit_num = 1  ");
            //    if (shopCategoryId > 0) {
            //        sql.append(" 			and exists (  ");
            //        sql.append(" 				select 1 from  ");
            //        sql.append(" 				view_data_sales_detail_valid ds1 ");
            //        sql.append(" 				inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1  ");
            //        sql.append(" 				inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id  ");
            //        sql.append(" 				where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  ");
            //        sql.append(" 				and mtc.shop_category_id = " + shopCategoryId + " ");
            //        sql.append(" 				)  ");
            //    }
            //    sql.append(" 		)*100/	  ");
            //    sql.append(" 		(	  ");
            //    sql.append(" 			select count( distinct slip_no) as customer_count   ");
            //    sql.append(" 			from view_data_sales_detail_valid ds  ");
            //    sql.append(" 			inner join mst_customer mc on ds.customer_id = mc.customer_id   ");
            //    sql.append(" 			where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1  ");
            //    sql.append(" 			and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "  ");
            //    sql.append(" 			and ds.shop_id in (" + selectedShop.getShopID() + ")  ");
            //    sql.append(" 			and product_division in (1,3)   ");
            //    if (shopCategoryId > 0) {
            //        sql.append(" 			and exists (  ");
            //        sql.append(" 				select 1 from  ");
            //        sql.append(" 				view_data_sales_detail_valid ds1 ");
            //        sql.append(" 				inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1  ");
            //        sql.append(" 				inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id  ");
            //        sql.append(" 				where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  ");
            //        sql.append(" 				and mtc.shop_category_id = " + shopCategoryId + " ");
            //        sql.append(" 				)  ");
            //    }
            //    sql.append(" 		)::float as new_rate	  ");
            //    new_rate = 0;
            //    try {
            //        rs = con.executeQuery(sql.toString());
            //        rs.next();
            //        new_rate = rs.getDouble("new_rate");
            //    } catch (SQLException e) {
            //    }

            //    jx.setValue(13, 14, new_rate);
            //}
            //10 テンプレートに　9÷32で算出してください。
            //Luc end edit 20150629 #38737
            //1.1.11店舗集客数
            sql = new StringBuffer();
            sql.append(" 			  ");
            sql.append(" select count( distinct slip_no) as num 		 ");
            sql.append(" from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
            sql.append(" where date_part('year',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "  ");
            sql.append(" and ds.product_division in (1,3) ");
            sql.append(" and visit_num = 1			  ");
            sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
            sql.append(" and mc.first_coming_motive_class_id is not null			  ");
            if (shopCategoryId > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
            }
            sql.append(" 			  ");
            double num = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                num = rs.getDouble("num");
            } catch (Exception e) {
            }
            jx.setValue(9, 15, num);
            //目標
            sql = new StringBuffer();
            sql.append(" select coalesce(sum(num),0) as num			  ");
            sql.append(" from data_target_motive 			  ");
            sql.append(" where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") and shop_category_id = " + shopCategoryId + "			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(11, 15, rs.getDouble("num"));
            //昨年：
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" 			  ");
                sql.append(" select count( distinct slip_no) as num			  ");
                sql.append(" from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id 			  ");
                sql.append(" where date_part('year',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 			  ");
                sql.append(" and date_part('month',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "  ");
                sql.append(" and ds.product_division in (1,3) ");
                sql.append(" and visit_num = 1			  ");
                sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")			  ");
                sql.append(" and mc.first_coming_motive_class_id is not null			  ");
                if (shopCategoryId > 0) {
                    sql.append(" and 			  ");
                    sql.append(" (			  ");
                    sql.append(" exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                    sql.append(" or 			  ");
                    sql.append(" exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                    sql.append(" ) 			  ");
                }
                sql.append(" 			  ");
                sql.append(" 			  ");
                num = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    num = rs.getDouble("num");
                } catch (Exception e) {
                }
                jx.setValue(13, 15, num);
            }

            //1.1.12.90日新規再来率,90日準固定再来率,90日固定再来率
            //今年
            double repeat_90_new = 0;
            double newTotal  = 0;
            double sub_fixed_total = 0;
            double sub_fixed_appear = 0;
            double fixed_total = 0;
            double fixed_appear = 0;
            try {
                //rs = con.executeQuery(sql.toString());
                rs = con.executeQuery(logic2.sqlQueryRepeat(selectedShop.getShopID(), SelectedYear, SelectedMonth, shopCategoryId, 3));
                //rs.next();
                if ( rs.next() ) {
                    newTotal = rs.getDouble("new_total");
                    repeat_90_new = rs.getDouble("new_reappearance");
                    sub_fixed_total = rs.getDouble("sub_fixed_total");
                    sub_fixed_appear = rs.getDouble("sub_fixed_reappearance");
                    fixed_total = rs.getDouble("fixed_total");
                    fixed_appear = rs.getDouble("fixed_reappearance");
                }
                //repeat_90_new = rs.getDouble("repeat_90_new");
            } catch (SQLException e) {
            }
            //jx.setValue(9, 16, repeat_90_new);
            jx.setValue(9, 16, newTotal != 0 ? repeat_90_new*100/newTotal : 0);
            jx.setValue(25, 2, sub_fixed_total != 0 ? sub_fixed_appear*100/sub_fixed_total : 0);
            jx.setValue(25, 3, fixed_total != 0 ? fixed_appear*100/fixed_total : 0);
                     
            //昨年：
            if (n == 0) {
                repeat_90_new = 0;
                newTotal  = 0;
                sub_fixed_total = 0;
                sub_fixed_appear = 0;
                fixed_total = 0;
                fixed_appear = 0;
                try {
                    rs = con.executeQuery(logic2.sqlQueryRepeat(selectedShop.getShopID(), SelectedYear-1, SelectedMonth, shopCategoryId, 3));
                    if ( rs.next() ) {
                        newTotal = rs.getDouble("new_total");
                        repeat_90_new = rs.getDouble("new_reappearance");
                        sub_fixed_total = rs.getDouble("sub_fixed_total");
                        sub_fixed_appear = rs.getDouble("sub_fixed_reappearance");
                        fixed_total = rs.getDouble("fixed_total");
                        fixed_appear = rs.getDouble("fixed_reappearance");
                    }
                    //repeat_90_new = rs.getDouble("repeat_90_new");
                } catch (SQLException e) {
                }
                jx.setValue(13, 16, newTotal != 0 ? repeat_90_new*100/newTotal : 0);
                jx.setValue(29, 2, sub_fixed_total != 0 ? sub_fixed_appear*100/sub_fixed_total : 0);
                jx.setValue(29, 3, fixed_total != 0 ? fixed_appear*100/fixed_total : 0);
            }
            
            //目標：
            //90日新規再来率
            repeat_90_new = 0;
            sql = new StringBuffer();
            sql.append(" select repeat_90_new*100 as repeat_90_new from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            rs = con.executeQuery(sql.toString());
            rs.next();
            try {
                repeat_90_new = rs.getDouble("repeat_90_new");
            } catch (Exception e) {
            }
            jx.setValue(11, 16, repeat_90_new);  
            
            //90日準固定再来率
            double repeat_90_semifix = 0;
            sql = new StringBuffer();
            sql.append(" 			  ");
            sql.append(" select coalesce(repeat_90_semifix,0)*100 as repeat_90_semifix 			  ");
            sql.append(" from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            repeat_90_semifix = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                repeat_90_semifix = rs.getDouble("repeat_90_semifix");
            } catch (SQLException e) {
            }
            jx.setValue(27, 2, repeat_90_semifix);
            
            //90日固定再来率
            double repeat_90_fix = 0;
            sql = new StringBuffer();
            sql.append(" select coalesce(repeat_90_fix,0)*100 as repeat_90_fix 			  ");
            sql.append(" from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "  			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            repeat_90_fix = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                repeat_90_fix = rs.getDouble("repeat_90_fix");
            } catch (Exception e) {
            }
            jx.setValue(27, 3, repeat_90_fix);
            
            //Task #35027 GB_Mashu [Product][Code][Phase4]目標管理追加仕様-店舗実績
            //IVS_nahoang Start add 20150122
            MstTargetLogic2 targetLogic = new MstTargetLogic2();
            targetLogic.setShopId(selectedShop.getShopID());
            targetLogic.setTargetYear(SelectedYear);
            targetLogic.setTargetMonth(SelectedMonth);
            targetLogic.setCategoryId(shopCategoryId);
            
            
            //1.1.13
            double net_new_total = 0;
            double net_new_reappear = 0;
            //今年
            try {
                    rs = con.executeQuery(logic2.sqlQueryNetRepeat(selectedShop.getShopID(), SelectedYear, SelectedMonth, shopCategoryId, 3));
                    if ( rs.next() ) {
                        net_new_total = rs.getDouble("net_new_total");
                        net_new_reappear = rs.getDouble("net_new_reappearance");
                    }
                    //repeat_90_new = rs.getDouble("repeat_90_new");
                } catch (SQLException e) {
                }
            jx.setValue(9, 17, net_new_total != 0 ? net_new_reappear*100/net_new_total : 0);
            
            
            double repeat90NerNewInPresent = 0;
            repeat90NerNewInPresent = targetLogic.getRepeat90NetnewInPresent();
            jx.setValue(11, 17, repeat90NerNewInPresent);
            
            //昨年
            if(n==0){
                try {
                         rs = con.executeQuery(logic2.sqlQueryNetRepeat(selectedShop.getShopID(), SelectedYear-1, SelectedMonth, shopCategoryId, 3));
                         if ( rs.next() ) {
                             net_new_total = rs.getDouble("net_new_total");
                             net_new_reappear = rs.getDouble("net_new_reappearance");
                         }
                         //repeat_90_new = rs.getDouble("repeat_90_new");
                     } catch (SQLException e) {
                     }
                 jx.setValue(13, 17, net_new_total != 0 ? net_new_reappear*100/net_new_total : 0);
            }
            
            //1.1.16
            double dataTargetMotive = 0;
            dataTargetMotive =  targetLogic.getDataTargetMotiveInPresent();
            jx.setValue(11, 21, dataTargetMotive);
            
            double dataTargetMotiveInPast = 0;
            dataTargetMotiveInPast = targetLogic.getDataTargetMotiveInPast();
            jx.setValue(9, 21, dataTargetMotiveInPast);
            
            if (n == 0) {
                double dataTargetMotiveInFuture = 0;
                dataTargetMotiveInFuture = targetLogic.getDataTargetMotiveInFuture();
                jx.setValue(13, 21, dataTargetMotiveInFuture);
            }
                       
            //1.1.31
            int totalStaff = 0;
            totalStaff = targetLogic.getTotalStaffIsWorking();
            
            double totalProductInPast = 0;
            if(totalStaff > 0){
                totalProductInPast = sales_value_past / totalStaff;
            }
            BigDecimal roundTotalProductInPast =  new BigDecimal(totalProductInPast);
            roundTotalProductInPast.setScale(0, BigDecimal.ROUND_HALF_UP);
            jx.setValue(25, 9, roundTotalProductInPast);
            
            double totalProductInPresent = 0;
            if(totalStaff > 0){
                totalProductInPresent = sales_value_current / totalStaff;
            }
            BigDecimal roundTotalProductInPresent =  new BigDecimal(totalProductInPresent);
            roundTotalProductInPresent.setScale(0, BigDecimal.ROUND_HALF_UP);
            jx.setValue(27, 9, roundTotalProductInPresent);
            
            if(n == 0){
                int totalStaffInFuture = targetLogic.getTotalStaffIsWorkingInFuture();
                double totalProductInFuture = 0;
                if(totalStaffInFuture > 0){
                    totalProductInFuture = sales_value_future / totalStaffInFuture;
                }
                BigDecimal roundTotalProductInFuture =  new BigDecimal(totalProductInFuture);
                roundTotalProductInFuture.setScale(0, BigDecimal.ROUND_HALF_UP);
                jx.setValue(29, 9, roundTotalProductInFuture);
            }
            
            //1.1.38
            double purchasingRate = 0;
            purchasingRate = targetLogic.getPurchasingRateInPresent();
            
            //Luc start edit 20150622 #37600
            //jx.setValue(27, 17, purchasingRate);
            jx.setValue(27, 23, purchasingRate);
            //Luc end  edit 20150622 #37600
            
            //1.1.39 
            int purchasingUnitPrice = 0;
            purchasingUnitPrice = targetLogic.getPurchasingUnitPriceInPresent();
            //Luc start edit 20150622 #37600
            //jx.setValue(27,18, purchasingUnitPrice);
            jx.setValue(27, 24, purchasingUnitPrice);
            //Luc end edit 20150622 #37600
            //IVS_nahoang End add 20150122
            
            //1.1.13.ネット集客90日新規再来率																								
            //空白で出力																						

            //1.1.14.失客掘り起し客数																								
            //今年：	
            sql = new StringBuffer();
            sql.append(" 	select count(distinct customer_id) as lost_count\n");
            sql.append(" 	from view_data_sales_detail_valid ds\n");
            sql.append(" 	where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date)  =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and ds.shop_id in (" + selectedShop.getShopID() + ")");
            if (shopCategoryId > 0) {
                sql.append(" 	and 	\n");
                sql.append(" 	(	\n");
                sql.append(" 	exists (	\n");
                sql.append(" 		select 1 from \n");
                sql.append(" 		view_data_sales_detail_valid ds1\n");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	\n");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	\n");
                sql.append(" 		and mtc.shop_category_id =  " + shopCategoryId + "	\n");
                sql.append(" 		) 	\n");
                sql.append(" 	or 	\n");
                sql.append(" 	exists(	\n");
                sql.append(" 		select 1 from \n");
                sql.append(" 		view_data_sales_detail_valid ds1\n");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	\n");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 		and mic.shop_category_id =  " + shopCategoryId + "	\n");
                sql.append(" 		)	\n");
                sql.append(" 	)\n");
            }
            //Luc start edit 20150525 Bug #37015
            //sql.append(" 	and customer_id not in \n");
            sql.append(" 	and not exists \n");
            //Luc end edit 20150525
            sql.append(" 	(	\n");
            sql.append(" 		select 1 \n");
            sql.append(" 		from view_data_sales_detail_valid ds1	\n");
            sql.append(" 		where ds1.sales_date < ds.sales_date and ds1.sales_date >  ds.sales_date - interval'6 month'\n");
            //Luc start add 20150525 Bug #37015
            sql.append("                and ds1.customer_id = ds.customer_id\n");
            //Luc end add 20150525 Bug #37015
            sql.append("                and ds1.shop_id in (" + selectedShop.getShopID() + ")");
            if (shopCategoryId > 0) {
                sql.append(" 		and \n");
                sql.append(" 		(	\n");
                sql.append(" 		exists (\n");
                sql.append(" 			select 1 from \n");
                sql.append(" 			view_data_sales_detail_valid ds1\n");
                sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 			and mtc.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 			)\n");
                sql.append(" 		or 	\n");
                sql.append(" 		exists(	\n");
                sql.append(" 			select 1 from 	\n");
                sql.append(" 			view_data_sales_detail_valid ds1\n");
                sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	\n");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 			and mic.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 			)\n");
                sql.append(" 		)\n");
            }
            sql.append(" 	)	\n");
            //Luc start edit 20150525 Bug #37015
            //sql.append(" 	and customer_id in \n");
            sql.append(" 	and exists \n");
            //Luc end edit 20150525 Bug #37015
            sql.append(" 	(	\n");
            sql.append(" 		select 1 \n");
            sql.append(" 		from view_data_sales_detail_valid ds1\n");
            sql.append(" 		where  	\n");
            sql.append(" 		ds1.sales_date <=  ds.sales_date -interval'6 month' \n");
            //Luc start add 20150525 Bug #37015
            sql.append("                and ds1.customer_id = ds.customer_id\n");
            //Luc end add 20150525 Bug #37015
            sql.append("                and ds1.shop_id in (" + selectedShop.getShopID() + ")");
            if (shopCategoryId > 0) {
                sql.append(" 		and 	\n");
                sql.append(" 		(\n");
                sql.append(" 		exists (\n");
                sql.append(" 			select 1 from \n");
                sql.append(" 			view_data_sales_detail_valid ds1\n");
                sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 			and mtc.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 			)\n");
                sql.append(" 		or \n");
                sql.append(" 		exists(	\n");
                sql.append(" 			select 1 from 	\n");
                sql.append(" 			view_data_sales_detail_valid ds1\n");
                sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 			and mic.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 			)\n");
                sql.append(" 		)\n");
            }
            sql.append(" 	)\n");
            double lost_count = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                lost_count = rs.getDouble("lost_count");

            } catch (Exception e) {
            }
            jx.setValue(9, 18, lost_count);
            //昨年：	
            if(n==0) {
                sql = new StringBuffer();
                sql.append(" 	select count(distinct customer_id) as lost_count\n");
                sql.append(" 	from view_data_sales_detail_valid ds\n");
                sql.append(" 	where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1  and date_part('month',sales_date)  =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append("        and ds.shop_id in (" + selectedShop.getShopID() + ")");
                if (shopCategoryId > 0) {
                    sql.append(" 	and \n");
                    sql.append(" 	(\n");
                    sql.append(" 	exists (\n");
                    sql.append(" 		select 1 from 	\n");
                    sql.append(" 		view_data_sales_detail_valid ds1\n");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	\n");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	\n");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 		and mtc.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 		) \n");
                    sql.append(" 	or 	\n");
                    sql.append(" 	exists(	\n");
                    sql.append(" 		select 1 from \n");
                    sql.append(" 		view_data_sales_detail_valid ds1\n");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 		and mic.shop_category_id =  " + shopCategoryId + "	\n");
                    sql.append(" 		)	\n");
                    sql.append(" 	)\n");
                }
               //Luc start edit 20150525 Bug #37015
                //sql.append(" 	and customer_id not in \n");
                sql.append(" 	and not exists \n");
                //Luc end edit 20150525
                sql.append(" 	(\n");
                sql.append(" 		select 1 \n");
                sql.append(" 		from view_data_sales_detail_valid ds1\n");
                sql.append(" 		where ds1.sales_date < ds.sales_date   and ds1.sales_date >  ds.sales_date - interval'6 month' \n");
                 //Luc start add 20150525 Bug #37015
                sql.append("                and ds1.customer_id = ds.customer_id\n");
                //Luc end add 20150525 Bug #37015
                sql.append("                and ds1.shop_id in (" + selectedShop.getShopID() + ")");
                if (shopCategoryId > 0) {
                    sql.append(" 		and \n");
                    sql.append(" 		(	\n");
                    sql.append(" 		exists (\n");
                    sql.append(" 			select 1 from \n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 			and mtc.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 			) \n");
                    sql.append(" 		or 	\n");
                    sql.append(" 		exists(	\n");
                    sql.append(" 			select 1 from \n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	\n");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	\n");
                    sql.append(" 			and mic.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 			)\n");
                    sql.append(" 		)\n");
                }
                sql.append(" 	)	\n");
                //Luc start edit 20150525 Bug #37015
                //sql.append(" 	and customer_id in \n");
                sql.append(" 	and exists \n");
                //Luc end edit 20150525 Bug #37015
                sql.append(" 	(\n");
                sql.append(" 		select 1 \n");
                sql.append(" 		from view_data_sales_detail_valid ds1\n");
                sql.append(" 		where  	\n");
                sql.append(" 		ds1.sales_date <=  ds.sales_date -interval'6 month' \n");
                 //Luc start add 20150525 Bug #37015
                sql.append("                and ds1.customer_id = ds.customer_id\n");
                //Luc end add 20150525 Bug #37015
                sql.append("                and ds1.shop_id in (" + selectedShop.getShopID() + ")");
                if (shopCategoryId > 0) {
                    sql.append(" 		and \n");
                    sql.append(" 		(	\n");
                    sql.append(" 		exists (	\n");
                    sql.append(" 			select 1 from\n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                    sql.append(" 			inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 			and mtc.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 			) \n");
                    sql.append(" 		or 	\n");
                    sql.append(" 		exists(\n");
                    sql.append(" 			select 1 from\n");
                    sql.append(" 			view_data_sales_detail_valid ds1\n");
                    sql.append(" 			inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append(" 			inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                    sql.append(" 			where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 			and mic.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 			)	\n");
                    sql.append(" 		)\n");
                }
                sql.append(" 	)\n");
                lost_count = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    lost_count = rs.getDouble("lost_count");

                } catch (Exception e) {
                }
                jx.setValue(13, 18, lost_count);
            }



            //1.1.19.指名客数																	
            //今年：	
            sql = new StringBuffer();
            sql.append(" select count(distinct slip_no) as cnt\n");
            sql.append(" from \n");
            sql.append(" view_data_sales_detail_valid ds\n");
            sql.append(" where date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date)  =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            //Luc start edit 20150626 #38252 NO.19
            //sql.append (" and ds.product_division = 1");
            sql.append (" and ds.product_division in (1,3)");
            //Luc end edit 20150626 #38252 NO.19
            if(shopCategoryId == 0) {
            sql.append(" and designated_flag = true \n");
            }
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")			  ");
            if (shopCategoryId > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	)		  ");
                sql.append(" ) 			  ");
                sql.append(" and exists (select 1 from data_sales_mainstaff dsmt where dsmt.shop_id = ds.shop_id and dsmt.slip_no = ds.slip_no and  designated_flag = true) ");
                
            }
            rs = con.executeQuery(sql.toString());
            rs.next();
            int count = 0;
            try {
                count = rs.getInt("cnt");
            } catch (Exception e) {
            }
            jx.setValue(9, 25, count);
            //昨年
            if(n==0) {
                sql = new StringBuffer();
                sql.append(" select count(distinct slip_no) as cnt\n");
                sql.append(" from \n");
                sql.append(" view_data_sales_detail_valid ds \n");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1\n");
                sql.append(" and date_part('month',sales_date)  =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                //Luc start edit 20150626 #38252 NO.19
                //sql.append (" and ds.product_division = 1");
                sql.append (" and ds.product_division in (1,3)");
                //Luc end edit 20150626 #38252 NO.19
                 if(shopCategoryId == 0) {
                sql.append(" and designated_flag = true \n");
                }
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")			  ");
                if (shopCategoryId > 0) {
                    sql.append(" and 			  ");
                    sql.append(" (			  ");
                    sql.append(" exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                    sql.append(" or 			  ");
                    sql.append(" exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                    sql.append(" ) 			  ");
                    sql.append(" and exists (select 1 from data_sales_mainstaff dsmt where dsmt.shop_id = ds.shop_id and dsmt.slip_no = ds.slip_no and  designated_flag = true) ");
                }
                rs = con.executeQuery(sql.toString());
                rs.next();
                count = 0;
                try {
                    count = rs.getInt("cnt");
                } catch (Exception e) {
                }
                jx.setValue(13, 25, count);
            }
            //1.1.20.120日固定再来率					

            //今年：	
            fixed_total = 0;
            fixed_appear = 0;
            double repeat_120_fix = 0;
            try {
                rs = con.executeQuery(logic2.sqlQueryRepeat(selectedShop.getShopID(), SelectedYear, SelectedMonth, shopCategoryId, 4));
                rs.next();
                fixed_total = rs.getDouble("fixed_total");
                fixed_appear = rs.getDouble("fixed_reappearance");
                //repeat_120_fix = rs.getDouble("repeat_120_fix");

            } catch (Exception e) {
            }
            jx.setValue(9, 26, fixed_total != 0 ? fixed_appear*100/fixed_total : 0);
            
            //目標：
            sql = new StringBuffer();
            sql.append(" select repeat_120_fix*100 as repeat_120_fix from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                repeat_120_fix = rs.getDouble("repeat_120_fix");

            } catch (Exception e) {
            }
            jx.setValue(11, 26, repeat_120_fix);
            
            //昨年：	
            if (n == 0) {
               
                fixed_total = 0;
                fixed_appear = 0;
                repeat_120_fix = 0;
                try {
                    rs = con.executeQuery(logic2.sqlQueryRepeat(selectedShop.getShopID(), SelectedYear-1, SelectedMonth, shopCategoryId, 4));
                    rs.next();
                    fixed_total = rs.getDouble("fixed_total");
                    fixed_appear = rs.getDouble("fixed_reappearance");
                } catch (Exception e) {
                }
                jx.setValue(13, 26, fixed_total != 0 ? fixed_appear*100/fixed_total : 0);
            }
            //1.1.21.180日固定再来率					
            //今年：	
            fixed_total = 0;
            fixed_appear = 0;
           
            try {
                rs = con.executeQuery(logic2.sqlQueryRepeat(selectedShop.getShopID(), SelectedYear, SelectedMonth, shopCategoryId, 6));
                rs.next();
                fixed_total = rs.getDouble("fixed_total");
                fixed_appear = rs.getDouble("fixed_reappearance");
            } catch (Exception e) {
            }
            jx.setValue(9, 27, fixed_total != 0 ? fixed_appear*100/fixed_total : 0);

            //目標：			 

            sql = new StringBuffer();
            sql.append(" select repeat_180_fix*100 as repeat_180_fix from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            double repeat_180_fix = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                repeat_180_fix = rs.getDouble("repeat_180_fix");
            } catch (SQLException e) {
            }
            jx.setValue(11, 27, repeat_180_fix);
            
            // 昨年：
            if (n == 0) {
                try {
                    rs = con.executeQuery(logic2.sqlQueryRepeat(selectedShop.getShopID(), SelectedYear-1, SelectedMonth, shopCategoryId, 6));
                    rs.next();
                    fixed_total = rs.getDouble("fixed_total");
                    fixed_appear = rs.getDouble("fixed_reappearance");

                } catch (Exception e) {
                }
                jx.setValue(13, 27, fixed_total != 0 ? fixed_appear*100/fixed_total : 0);
            }
            //1.1.23.技術に関するクレーム数				
            //今年：
            sql = new StringBuffer();
            //Luc start edit 20150626 #38252 NO.23
            //sql.append(" select count(distinct slip_no) as clarm_count from view_data_sales_detail_valid ds			  ");
            sql.append(" select count( slip_no) as clarm_count from view_data_sales_detail_valid ds			  ");
            //Luc end edit 20150626 #38252 NO.23
            sql.append(" where product_division = 3 and date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")			  ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=3 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            double clarm_count = 0;
            try {
                clarm_count = rs.getDouble("clarm_count");
            } catch (Exception e) {
            }
            jx.setValue(9, 29, clarm_count);
            //目標：	

            //昨年：
            if (n == 0) {
                sql = new StringBuffer();
                //Luc start edit 20150626 #38252 NO.23
                //sql.append(" select count(distinct slip_no) as clarm_count from view_data_sales_detail_valid ds			  ");
                sql.append(" select count( slip_no) as clarm_count from view_data_sales_detail_valid ds			  ");
                //Luc end edit 20150626 #38252 NO.23
                sql.append(" where product_division = 3 and date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "-1  and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")			  ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=3 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" 			  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                clarm_count = 0;
                try {
                    clarm_count = rs.getDouble("clarm_count");
                } catch (Exception e) {
                }
                jx.setValue(13, 29, clarm_count);

            }
            
            // 1.1.27.年間来店回数					
            //今年：	
            //"+SQLUtil.convertForSQL(SelectedMonth)+">="+StartMonth+"場合：	
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql = new StringBuffer();
                sql.append(" select  sum(visit_count) AS visit_count			  ");
                sql.append(" from			  ");
                sql.append(" (			  ");
                sql.append(" 	select count( distinct slip_no) AS visit_count		  ");
                sql.append(" 	from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                sql.append(" 			  ");
                sql.append(" )a			  ");
                sql.append(" 			  ");
            } else {
                sql = new StringBuffer();
                sql.append(" select  sum(visit_count) AS visit_count			  ");
                sql.append(" from			  ");
                sql.append(" (			  ");
                sql.append(" 	select count( distinct slip_no) AS visit_count		  ");
                sql.append(" 	from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + ") \n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                sql.append(" 			  ");
                sql.append(" )a			  ");
                sql.append(" 			  ");
            }
            rs = con.executeQuery(sql.toString());
            rs.next();
            double visit_count = 0;
            try {
                visit_count = rs.getDouble("visit_count");
            } catch (Exception e) {
            }
            jx.setValue(25, 4, visit_count);
            //目標：	
            sql = new StringBuffer();
            //"+SQLUtil.convertForSQL(SelectedMonth)+">="+StartMonth+"場合：			
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append(" select sum(customer) as visit_count from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + "  and month <=" + SQLUtil.convertForSQL(SelectedMonth) +(StartMonth!=1?(" and month >=" + SQLUtil.convertForSQL(StartMonth) + ""):""));
                sql.append(" and shop_category_id =" + shopCategoryId + "and shop_id in (" + selectedShop.getShopID() + ")");
            } else {
                //"+SQLUtil.convertForSQL(SelectedMonth)+"<"+StartMonth+"場合：	
                sql.append(" select sum(customer) as visit_count  from data_target 			  ");
                sql.append(" where ((year =" + SQLUtil.convertForSQL(SelectedYear) + " and  month <= " + SQLUtil.convertForSQL(SelectedMonth) + " ) 			  ");
                sql.append(" or (year =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and  month >= " + StartMonth + " )) 			  ");
                sql.append(" and shop_category_id = " + shopCategoryId + "			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            }
            rs = con.executeQuery(sql.toString());
            rs.next();
            visit_count = 0;
            try {
                visit_count = rs.getDouble("visit_count");
            } catch (Exception e) {
            }
            jx.setValue(27, 4, visit_count);
            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                //"+SQLUtil.convertForSQL(SelectedMonth)+">="+StartMonth+"場合：
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append(" select  sum(visit_count) AS visit_count			  ");
                    sql.append(" from			  ");
                    sql.append(" (			  ");
                    //IVS Luc start edit Bug #32510[Phase 4][IT][Exec][EDIT]帳票_店舗実績_phan nam nay va nay truoc hien thi gia tri khac nhau
                    sql.append(" 	select count(distinct slip_no) AS visit_count		  ");
                    //IVS Luc start edit Bug #32510[Phase 4][IT][Exec][EDIT]帳票_店舗実績_phan nam nay va nay truoc hien thi gia tri khac nhau
                    sql.append(" 	from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 		  ");
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                    if (shopCategoryId > 0) {
                        sql.append(" 	and 		  ");
                        sql.append(" 	(		  ");
                        sql.append(" 	exists (		  ");
                        sql.append(" 		select 1 from 	  ");
                        sql.append(" 		view_data_sales_detail_valid ds1	  ");
                        sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                        sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                        sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                        sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                        sql.append(" 		) 	  ");
                        sql.append(" 	or 		  ");
                        sql.append(" 	exists(		  ");
                        sql.append(" 		select 1 from 	  ");
                        sql.append(" 		view_data_sales_detail_valid ds1	  ");
                        sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                        sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                        sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                        sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                        sql.append(" 		)	  ");
                        sql.append(" 	)		  ");
                    }
                    sql.append(" 			  ");
                    sql.append(" )a			  ");
                    sql.append(" 			  ");
                } else {
                    //"+SQLUtil.convertForSQL(SelectedMonth)+"<"+StartMonth+"場合：
                    sql.append(" select  sum(visit_count) AS visit_count			  ");
                    sql.append(" from			  ");
                    sql.append(" (			  ");
                    sql.append(" 	select count( distinct slip_no) AS visit_count		  ");
                    sql.append(" 	from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 		  ");
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + ") \n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                    if (shopCategoryId > 0) {
                        sql.append(" 	and 		  ");
                        sql.append(" 	(		  ");
                        sql.append(" 	exists (		  ");
                        sql.append(" 		select 1 from 	  ");
                        sql.append(" 		view_data_sales_detail_valid ds1	  ");
                        sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                        sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                        sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                        sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                        sql.append(" 		) 	  ");
                        sql.append(" 	or 		  ");
                        sql.append(" 	exists(		  ");
                        sql.append(" 		select 1 from 	  ");
                        sql.append(" 		view_data_sales_detail_valid ds1	  ");
                        sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                        sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                        sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                        sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                        sql.append(" 		)	  ");
                        sql.append(" 	)		  ");
                    }
                    sql.append(" 			  ");
                    sql.append(" )a			  ");
                    sql.append(" 			  ");
                }
                rs = con.executeQuery(sql.toString());
                rs.next();
                visit_count = 0;
                try {
                    visit_count = rs.getDouble("visit_count");
                } catch (Exception e) {
                }
                jx.setValue(29, 4, visit_count);
            }
            //1.1.28.デシル①年間来店回数					 

            //今年：			
            //位10%	
            sql = new StringBuffer();
            sql.append(" select count(*)/10::float	as block		  ");
            sql.append(" from 			  ");
            sql.append(" 	(		  ");
            sql.append(" 	select distinct ds.customer_id \n");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	)		  ");
            }
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
            } else {
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
            }
            sql.append("   group by ds.customer_id		  ");
            sql.append(" 	) cnt		  ");
            sql.append(" 			  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            bd = null;
            bd = new BigDecimal(rs.getDouble("block"));
            block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            // 回数:	
            sql = new StringBuffer();
            sql.append(" select  sum(visit_count) AS decyl_1_num 			  ");
            sql.append(" from			  ");
            sql.append(" (			  ");
            sql.append(" 	select customer_id,count(DISTINCT slip_no) AS visit_count, sum(discount_detail_value_no_tax) as sales_value		  ");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
            } else {
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
            }
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	)		  ");
            }
            sql.append(" 	group by ds.customer_id		  ");
            sql.append(" 	ORDER BY sales_value DESC 		  ");
            sql.append(" 	offset 0*" + block + " 		  ");
            sql.append(" 	limit " + block + " 	  ");
            sql.append(" )a			  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            jx.setValue(25, 5, rs.getDouble("decyl_1_num"));
            //目標：	
            sql = new StringBuffer();
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append(" 			  ");
                sql.append(" select coalesce(sum(decyl_1_num),0) as decyl_1_num from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + "  and month <=" + SQLUtil.convertForSQL(SelectedMonth) +(StartMonth!=1?(" and month >=" + SQLUtil.convertForSQL(StartMonth) + ""):""));
                sql.append(" and shop_category_id =" + shopCategoryId + "and shop_id in (" + selectedShop.getShopID() + ")");
            } else {
                sql.append(" 			  ");
                sql.append(" select coalesce(sum(decyl_1_num),0) as decyl_1_num from data_target ");
                sql.append(" where ((year =" + SQLUtil.convertForSQL(SelectedYear) + " and month <=" + SQLUtil.convertForSQL(SelectedMonth) + " ) ");
                sql.append(" or (year =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and month >=" + SQLUtil.convertForSQL(StartMonth) + " )) ");
                sql.append(" and shop_category_id = " + shopCategoryId + "			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                sql.append(" 			  ");
            }
            double decyl_1_num = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_1_num = rs.getDouble("decyl_1_num");
            } catch (Exception e) {
            }
            jx.setValue(27, 5, decyl_1_num);
            // 昨年：
            //位10%	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count(*)/10::float	as block		  ");
                sql.append(" from 			  ");
                sql.append(" 	(		  ");
                sql.append(" 	select distinct customer_id");
                sql.append(" from view_data_sales_detail_valid  ds \n");
                sql.append(" JOIN mst_customer b USING (customer_id)");
                sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("  AND b.customer_no <> '0' ");
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                } else {
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                }
                sql.append("    group by ds.customer_id		  ");
                sql.append(" 	) cnt		  ");
                sql.append(" 			  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                rs.next();
                bd = null;
                bd = new BigDecimal(rs.getDouble("block"));
                block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                // 回数:	
                sql = new StringBuffer();
                sql.append(" select  sum(visit_count) AS decyl_1_num  ");
                sql.append(" from			  ");
                sql.append(" (			  ");
                sql.append(" 	select customer_id,count(DISTINCT slip_no) AS visit_count,sum(ds.discount_detail_value_no_tax) as sales_value 		  ");
                sql.append(" from view_data_sales_detail_valid  ds \n");
                sql.append(" JOIN mst_customer b USING (customer_id)");
                sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("  AND b.customer_no <> '0' ");
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                } else {
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                }
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                sql.append(" 	group by ds.customer_id		  ");
                sql.append(" 	ORDER BY sales_value DESC 		  ");
                sql.append(" 	offset 0*" + block + " 		  ");
                sql.append(" 	limit " + block + " 	  ");
                sql.append(" )a			  ");
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    decyl_1_num = rs.getDouble("decyl_1_num");
                } catch (Exception e) {
                }

                jx.setValue(29, 5, decyl_1_num);
            }
            //1.1.29デシル②年間来店回数					  
            //今年：			
            //位10%	
            sql = new StringBuffer();
            sql.append(" select count(*)/10::float	as block		  ");
            sql.append(" from 			  ");
            sql.append(" 	(		  ");
            sql.append(" 	select distinct customer_id ");
            sql.append("   from view_data_sales_detail_valid  ds \n");
            sql.append("   JOIN mst_customer b USING (customer_id)");
            sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("   AND b.customer_no <> '0' ");
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	)		  ");
            }
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
            } else {
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
            }
            sql.append("    group by customer_id		  ");
            sql.append(" 	) cnt		  ");
            sql.append(" 			  ");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            bd = null;
            bd = new BigDecimal(rs.getDouble("block"));
            block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            // 回数:	
            sql = new StringBuffer();
            sql.append(" select  sum(visit_count) AS decyl_2_num 			  ");
            sql.append(" from			  ");
            sql.append(" (			  ");
            sql.append(" 	select customer_id,count(DISTINCT slip_no) AS visit_count, sum(discount_detail_value_no_tax) as sales_value		  ");
            sql.append(" from view_data_sales_detail_valid  ds \n");
            sql.append(" JOIN mst_customer b USING (customer_id)");
            sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("  AND b.customer_no <> '0' ");
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
            } else {
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
            }
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	)		  ");
            }
            sql.append(" 	group by ds.customer_id		  ");
            sql.append(" 	ORDER BY sales_value DESC 		  ");
            sql.append(" 	offset 1*" + block + " 	  ");
            sql.append(" 	limit " + block + "  	  ");
            sql.append(" )a			  ");
            sql.append(" 			  ");
            double decyl_2_num = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_2_num = rs.getDouble("decyl_2_num");
            } catch (Exception e) {
            }
            jx.setValue(25, 6, decyl_2_num);
            //目標：	
            sql = new StringBuffer();
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append(" 			  ");
                sql.append(" select coalesce(sum(decyl_2_num),0) as decyl_2_num from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + "  and month <=" + SQLUtil.convertForSQL(SelectedMonth) +(StartMonth!=1?(" and month >=" + SQLUtil.convertForSQL(StartMonth) + ""):""));
                sql.append(" and shop_category_id =" + shopCategoryId + "and shop_id in (" + selectedShop.getShopID() + ")");
            } else {
                sql.append(" 			  ");
                sql.append(" select coalesce(sum(decyl_2_num),0) as decyl_2_num from data_target ");
                sql.append(" where ((year =" + SQLUtil.convertForSQL(SelectedYear) + " and month <=" + SQLUtil.convertForSQL(SelectedMonth) + " ) ");
                sql.append(" or (year =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and month >=" + SQLUtil.convertForSQL(StartMonth) + " )) ");
                sql.append(" and shop_category_id = " + shopCategoryId + "			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                sql.append(" 			  ");
            }
            decyl_2_num = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_2_num = rs.getDouble("decyl_2_num");
            } catch (Exception e) {
            }
            jx.setValue(27, 6, decyl_2_num);
            // 昨年：
            //位10%	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count(*)/10::float	as block		  ");
                sql.append(" from 			  ");
                sql.append(" 	(		  ");
                sql.append(" 	select distinct customer_id ");
                sql.append(" from view_data_sales_detail_valid  ds \n");
                sql.append(" JOIN mst_customer b USING (customer_id)");
                sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("  AND b.customer_no <> '0' ");
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                        sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                } else {
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                }
                sql.append("    group by ds.customer_id		  ");
                sql.append(" 	) cnt		  ");
                sql.append(" 			  ");
                sql.append(" 			  ");
                bd = null;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    bd = new BigDecimal(rs.getDouble("block"));
                } catch (Exception e) {
                }
                block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                // 回数:	
                sql = new StringBuffer();
                sql.append(" select  sum(visit_count) AS decyl_2_num   ");
                sql.append(" from			  ");
                sql.append(" (			  ");
                sql.append(" 	select customer_id,count(DISTINCT slip_no) AS visit_count,sum(ds.discount_detail_value_no_tax) as sales_value 		  ");
                sql.append(" from view_data_sales_detail_valid  ds \n");
                sql.append(" JOIN mst_customer b USING (customer_id)");
                sql.append(" where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("  AND b.customer_no <> '0' ");
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                        sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                } else {
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                }
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                sql.append(" 	group by ds.customer_id		  ");
                sql.append(" 	ORDER BY sales_value DESC 		  ");
                sql.append(" 	offset 1*" + block + " 		  ");
                sql.append(" 	limit " + block + "  	  ");
                sql.append(" )a			  ");
                decyl_2_num = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    decyl_2_num = rs.getDouble("decyl_2_num");
                } catch (Exception e) {
                }
                jx.setValue(29, 6, decyl_2_num);
            }
            //1.1.30.デシル③年間来店回数					
            //今年：			
            //位10%	
            sql = new StringBuffer();
            sql.append(" select count(*)/10::float	as block		  ");
            sql.append(" from 			  ");
            sql.append(" 	(		  ");
            sql.append(" 	select distinct customer_id ");
            sql.append("   from view_data_sales_detail_valid  ds \n");
            sql.append("   JOIN mst_customer b USING (customer_id)");
            sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("   AND b.customer_no <> '0' ");
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	)		  ");
            }
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
            } else {
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
            }
            sql.append("    group by ds.customer_id		  ");
            sql.append(" 	) cnt		  ");
            sql.append(" 			  ");
            sql.append(" 			  ");
            bd = null;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();

                bd = new BigDecimal(rs.getDouble("block"));
            } catch (Exception e) {
            }
            block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            // 回数:	
            sql = new StringBuffer();
            sql.append(" select  sum(visit_count) AS decyl_3_num 			  ");
            sql.append(" from			  ");
            sql.append(" (			  ");
            sql.append(" 	select customer_id,count(DISTINCT slip_no) AS visit_count, sum(discount_detail_value_no_tax) as sales_value		  ");
            sql.append("   from view_data_sales_detail_valid  ds \n");
            sql.append("   JOIN mst_customer b USING (customer_id)");
            sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append("   AND b.customer_no <> '0' ");
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear));
                sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                if(StartMonth != 1) {
                    sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                }
            } else {
                sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
            }
            if (shopCategoryId > 0) {
                sql.append(" 	and 		  ");
                sql.append(" 	(		  ");
                sql.append(" 	exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
                sql.append(" 	or 		  ");
                sql.append(" 	exists(		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		)	  ");
                sql.append(" 	)		  ");
            }
            sql.append(" 	group by ds.customer_id		  ");
            sql.append(" 	ORDER BY sales_value DESC 		  ");
            sql.append(" 	offset 2*" + block + " 		  ");
            sql.append(" 	limit " + block + "  		  ");
            sql.append(" )a			  ");
            sql.append(" 			  ");
            double decyl_3_num = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_3_num = rs.getDouble("decyl_3_num");
            } catch (Exception e) {
            }
            jx.setValue(25, 7, decyl_3_num);
            //目標：	
            sql = new StringBuffer();
            if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                sql.append(" 			  ");
                sql.append(" select coalesce(sum(decyl_3_num),0) as decyl_3_num from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + "  and month <=" + SQLUtil.convertForSQL(SelectedMonth) +(StartMonth!=1?(" and month >=" + SQLUtil.convertForSQL(StartMonth) + ""):""));
                sql.append(" and shop_category_id =" + shopCategoryId + "and shop_id in (" + selectedShop.getShopID() + ")");
            } else {
                sql.append(" 			  ");
                sql.append(" select coalesce(sum(decyl_3_num),0) as decyl_3_num from data_target ");
                sql.append(" where ((year =" + SQLUtil.convertForSQL(SelectedYear) + " and month <=" + SQLUtil.convertForSQL(SelectedMonth) + " ) ");
                sql.append(" or (year =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and month >=" + SQLUtil.convertForSQL(StartMonth) + " )) ");
                sql.append(" and shop_category_id = " + shopCategoryId + "			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
                sql.append(" 			  ");

            }
            decyl_3_num = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                decyl_3_num = rs.getDouble("decyl_3_num");
            } catch (Exception e) {
            }
            jx.setValue(27, 7, decyl_3_num);
            // 昨年：
            //位10%	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count(*)/10::float	as block		  ");
                sql.append(" from 			  ");
                sql.append(" 	(		  ");
                sql.append(" 	select distinct customer_id ");
                sql.append("   from view_data_sales_detail_valid  ds \n");
                sql.append("   JOIN mst_customer b USING (customer_id)");
                sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("   AND b.customer_no <> '0' ");
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                        sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                } else {
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                }
                sql.append("    group by ds.customer_id		  ");
                sql.append(" 	) cnt		  ");
                sql.append(" 			  ");
                sql.append(" 			  ");
                rs = con.executeQuery(sql.toString());
                bd = null;
                try {
                    rs.next();

                    bd = new BigDecimal(rs.getDouble("block"));
                } catch (Exception e) {
                }
                block = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                // 回数:	
                sql = new StringBuffer();
                sql.append(" select  sum(visit_count) AS decyl_3_num ");
                sql.append(" from			  ");
                sql.append(" (			  ");
                sql.append(" 	select customer_id,count(DISTINCT slip_no) AS visit_count,sum(ds.discount_detail_value_no_tax) as sales_value 		  ");
                sql.append("   from view_data_sales_detail_valid  ds \n");
                sql.append("   JOIN mst_customer b USING (customer_id)");
                sql.append("   where ds.shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append("   AND b.customer_no <> '0' ");
                if ((StartMonth != 1 && SelectedMonth >= StartMonth) ||(StartMonth == 1)) {
                    sql.append("    and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1");
                    sql.append("    and date_part('month',sales_date) <=" + SQLUtil.convertForSQL(SelectedMonth));
                    if(StartMonth != 1) {
                        sql.append("    and date_part('month',sales_date) >=" + StartMonth);
                    }
                } else {
                    sql.append("    and ((date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',sales_date) <= " + SQLUtil.convertForSQL(SelectedMonth) + " )\n");
                    sql.append("    or  (date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -2 and date_part('month',sales_date) >= " + SQLUtil.convertForSQL(StartMonth) + " ))\n");
                }
                if (shopCategoryId > 0) {
                    sql.append(" 	and 		  ");
                    sql.append(" 	(		  ");
                    sql.append(" 	exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                    sql.append(" 	or 		  ");
                    sql.append(" 	exists(		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	  ");
                    sql.append(" 		inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mic.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		)	  ");
                    sql.append(" 	)		  ");
                }
                sql.append(" 	group by ds.customer_id		  ");
                sql.append(" 	ORDER BY sales_value DESC 		  ");
                sql.append(" 	offset 2*" + block + "  		  ");
                sql.append(" 	limit " + block + " 		  ");
                sql.append(" )a			  ");
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    decyl_3_num = rs.getDouble("decyl_3_num");
                } catch (Exception e) {
                }
                jx.setValue(29, 7, decyl_3_num);
            }
            // 1.1.32.技術客数				
            //今年：	
            sql = new StringBuffer();
            sql.append(" select count( distinct slip_no)	as 	technic	  ");
            sql.append(" 	from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 		  ");
            sql.append(" AND date_part('year',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            //Luc start edit 20150626 #38252 NO.32 
            //sql.append(" and ds.product_division=1			  ");
            sql.append(" and ds.product_division in (1,3)			  ");
            //Luc start edit 20150626 #38252 NO.32 
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }

            double technic = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                technic = rs.getDouble("technic");
            } catch (Exception e) {
            }
            jx.setValue(25, 10, technic);
            //目標：
            sql = new StringBuffer();
            sql.append(" select sum(customer) as technic from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" 			  ");
            technic = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                technic = rs.getDouble("technic");
            } catch (Exception e) {
            }
            jx.setValue(27, 10, technic);

            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select count( distinct slip_no) as technic			  ");
                sql.append(" 	from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 		  ");
                sql.append(" AND date_part('year',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "- 1 and date_part('month',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                 //Luc start edit 20150626 #38252 NO.32 
                //sql.append(" and ds.product_division=1			  ");
                sql.append(" and ds.product_division in (1,3)			  ");
                //Luc start edit 20150626 #38252 NO.32 
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }

                technic = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    technic = rs.getDouble("technic");
                } catch (Exception e) {
                }
                jx.setValue(29, 10, technic);
            }

            //1.1.33.総客係数																																
            //今年：
            sql = new StringBuffer();
            sql.append(" select \n");
            sql.append(" (\n");
            sql.append(" select count(distinct slip_no)\n");
            sql.append(" from view_data_sales_detail_valid ds\n");
            sql.append(" where shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and product_division = 1	\n");
            if (shopCategoryId > 0) {
                 sql.append(" 	and exists (		  ");
                sql.append(" 		select 1 from 	  ");
                sql.append(" 		view_data_sales_detail_valid ds1	  ");
                sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                sql.append(" 		) 	  ");
            }
            sql.append("  )/\n");
            sql.append(" (	\n");
            sql.append(" select count( distinct staff_id) from data_schedule where date_part('year',schedule_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and  date_part('month',schedule_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and shift_id > 0	\n");
            sql.append(" )::float as value_staff\n");
            double value_staff = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                value_staff = rs.getDouble("value_staff");
            } catch (Exception e) {
            }
            jx.setValue(25, 11, value_staff);

            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select\n");
                sql.append(" (	\n");
                sql.append(" select count(distinct slip_no)\n");
                sql.append(" from view_data_sales_detail_valid ds\n");
                sql.append(" where shop_id in (" + selectedShop.getShopID() + ") 	\n");
                sql.append(" and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append(" and product_division = 1\n");
                if (shopCategoryId > 0) {
                    sql.append(" 	and exists (		  ");
                    sql.append(" 		select 1 from 	  ");
                    sql.append(" 		view_data_sales_detail_valid ds1	  ");
                    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                    sql.append(" 		) 	  ");
                }
                sql.append("  )/\n");
                sql.append(" (	\n");
                sql.append(" select count( distinct staff_id) from data_schedule where date_part('year',schedule_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" and  date_part('month',schedule_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append(" and shift_id > 0\n");
                sql.append(" )::float as value_staff\n");
                value_staff = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    value_staff = rs.getDouble("value_staff");
                } catch (Exception e) {
                }
                jx.setValue(29, 11, value_staff);
            }
             //1.1.34.稼動カルテ枚数
            int karte1Day = 0 ,karte2Day= 0,karte3Day = 0,karte4Day = 0;
            double karte1Value= 0,karte2Value = 0,karte3Value = 0,karte4Value = 0;
            try {
                rs = con.executeQuery(targetLogic.getKarteSettingSQL( selectedShop.getShopID().toString(), SelectedYear, SelectedMonth, shopCategoryId));
                rs.next();
                karte1Day = rs.getInt("karte1_day");
                karte2Day = rs.getInt("karte2_day");
                karte3Day = rs.getInt("karte3_day");
                karte4Day = rs.getInt("karte4_day");
                karte1Value = rs.getInt("karte1_value");
                karte2Value = rs.getInt("karte2_value");
                karte3Value = rs.getInt("karte3_value");
                karte4Value = rs.getInt("karte4_value");
            }catch(SQLException e) {}
            jx.setValue(19, 12,"稼動カルテ枚数（"+karte1Day+"日）");
            //今年
            
            try {
              rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte1Day, SelectedYear, SelectedMonth, shopCategoryId, false));
              rs.next();
              jx.setValue(25, 12, rs.getInt("cnt"));
            }catch(SQLException e){}
             //目標 
            jx.setValue(27, 12, karte1Value);
            //昨年
             if (n == 0) {
                try {
                 rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte1Day, SelectedYear, SelectedMonth, shopCategoryId, true));
                 rs.next();
                 jx.setValue(29, 12, rs.getInt("cnt"));
               }catch(SQLException e){}
             }
             //1.1.35.稼動カルテ枚数
            jx.setValue(19, 13,"稼動カルテ枚数（"+karte2Day+"日）");
            //今年
             try {
              rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte2Day, SelectedYear, SelectedMonth, shopCategoryId, false));
              rs.next();
              jx.setValue(25, 13, rs.getInt("cnt"));
            }catch(SQLException e){}
            //目標 
            jx.setValue(27, 13, karte2Value);
            //昨年
             if (n == 0) {
                try {
                 rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte2Day, SelectedYear, SelectedMonth, shopCategoryId, true));
                 rs.next();
                 jx.setValue(29, 13, rs.getInt("cnt"));
               }catch(SQLException e){}
             }
             //1.1.36.稼動カルテ枚数
             jx.setValue(19, 14,"稼動カルテ枚数（"+karte3Day+"日）");
            //今年
             try {
              rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte3Day, SelectedYear, SelectedMonth, shopCategoryId, false));
              rs.next();
              jx.setValue(25, 14, rs.getInt("cnt"));
            }catch(SQLException e){}
            //目標 
            jx.setValue(27, 14, karte3Value);
            //昨年
             if (n == 0) {
                try {
                 rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte3Day, SelectedYear, SelectedMonth, shopCategoryId, true));
                 rs.next();
                 jx.setValue(29, 14, rs.getInt("cnt"));
               }catch(SQLException e){}
             }
             //1.1.37.稼動カルテ枚数
             jx.setValue(19, 15,"稼動カルテ枚数（"+karte4Day+"日）");
            //今年
             try {
              rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte4Day, SelectedYear, SelectedMonth, shopCategoryId, false));
              rs.next();
              jx.setValue(25, 15, rs.getInt("cnt"));
            }catch(SQLException e){}
            //目標 
            jx.setValue(27, 15, karte4Value);
            //昨年
             if (n == 0) {
                try {
                 rs = con.executeQuery(targetLogic.getKarteSQL(selectedShop.getShopID().toString(), karte4Day, SelectedYear, SelectedMonth, shopCategoryId, true));
                 rs.next();
                 jx.setValue(29, 15, rs.getInt("cnt"));
               }catch(SQLException e){}
             }
             //1.1.38.延べ数
             //今年
             Integer nokoberu = 0;
             try {
                rs = con.executeQuery(targetLogic.getNokoberuSQL(selectedShop.getShopID().toString(), SelectedYear, SelectedMonth, shopCategoryId,false)); 
                rs.next();
                nokoberu = rs.getInt("cnt");
               jx.setValue(25, 16, nokoberu);
             }catch(Exception e){}
             //目標
             
             //昨年
             nokoberu = 0;
              if (n == 0) {
                try {
                   rs = con.executeQuery(targetLogic.getNokoberuSQL(selectedShop.getShopID().toString(), SelectedYear, SelectedMonth, shopCategoryId,true)); 
                   rs.next();
                   nokoberu = rs.getInt("cnt");
                  jx.setValue(29, 16, nokoberu);
                }catch(Exception e){}
              }
              //1.1.39.カウント数
             //今年
             nokoberu = 0;
             
             try {
                rs = con.executeQuery(targetLogic.getNokoberuSQL(selectedShop.getShopID().toString(), SelectedYear, SelectedMonth, 0,false)); 
                rs.next();
                nokoberu = rs.getInt("cnt");
               jx.setValue(25, 17, nokoberu);
             }catch(Exception e){}
             //目標
             
             //昨年
             nokoberu = 0;
              if (n == 0) {
                try {
                   rs = con.executeQuery(targetLogic.getNokoberuSQL(selectedShop.getShopID().toString(), SelectedYear, SelectedMonth, 0,true)); 
                   rs.next();
                   nokoberu = rs.getInt("cnt");
                  jx.setValue(29, 17, nokoberu);
                }catch(Exception e){}
              }
            //1.1.40.技術客単価
            //Luc start edit 20150629 #38737
            ////今年：	
            //sql = new StringBuffer();
            //sql.append(" select 			  ");
            //sql.append(" 	((select sum(discount_detail_value_no_tax)		  ");
            //sql.append(" 		from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 	  ");
            //sql.append(" 	AND date_part('year',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
            //sql.append(" 	and ds.product_division=1		  ");
            //if (shopCategoryId > 0) {
            //    sql.append(" 	and exists (		  ");
            //    sql.append(" 		select 1 from 	  ");
            //    sql.append(" 		view_data_sales_detail_valid ds1	  ");
            //    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
            //    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            //    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
            //    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            //    sql.append(" 		) 	  ");
            //}
            //sql.append(" 	)/		  ");
            //sql.append(" 	(select count( distinct slip_no)		  ");
            //sql.append(" 		from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 	  ");
            //sql.append(" 	AND date_part('year',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
            //sql.append(" 	and ds.product_division=1		  ");
            //if (shopCategoryId > 0) {
            //    sql.append(" 	and exists (		  ");
            //    sql.append(" 		select 1 from 	  ");
            //    sql.append(" 		view_data_sales_detail_valid ds1	  ");
            //    sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
            //    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            //    sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
            //    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            //    sql.append(" 		) 	  ");
            //}
            //sql.append(" 	)) as unit_price		  ");
            double unit_price = 0;
            //try {
            //    rs = con.executeQuery(sql.toString());
            //    rs.next();
            //    unit_price = rs.getDouble("unit_price");
            //} catch (Exception e) {
            //}
            //jx.setValue(25, 19, unit_price);
            //目標：
            sql = new StringBuffer();
            sql.append(" select unit_price from data_target where year = " + SQLUtil.convertForSQL(SelectedYear) + " and month =" + SQLUtil.convertForSQL(SelectedMonth) + " and shop_category_id = " + shopCategoryId + " 			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            unit_price = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                unit_price = rs.getDouble("unit_price");
            } catch (SQLException e) {
            }
            jx.setValue(27, 19, unit_price);
            //// 昨年：
            //if (n == 0) {
            //    sql = new StringBuffer();
            //    sql.append(" select 			  ");
            //    sql.append(" 	((select sum(discount_detail_value_no_tax)		  ");
            //    sql.append(" 		from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 	  ");
            //    sql.append(" 	AND date_part('year',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
            //    sql.append(" 	and ds.product_division=1		  ");
            //    if (shopCategoryId > 0) {
            //        sql.append(" 	and exists (		  ");
            //        sql.append(" 		select 1 from 	  ");
            //        sql.append(" 		view_data_sales_detail_valid ds1	  ");
            //        sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
            //        sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            //        sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
            //        sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            //        sql.append(" 		) 	  ");
            //    }
            //    sql.append(" 	)/		  ");
            //    sql.append(" 	(select count( distinct slip_no)		  ");
            //    sql.append(" 		from view_data_sales_detail_valid  ds where shop_id in (" + selectedShop.getShopID() + ") 	  ");
            //    sql.append(" 	AND date_part('year',ds.sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',ds.sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "		  ");
            //    sql.append(" 	and ds.product_division=1		  ");
            //    if (shopCategoryId > 0) {
            //        sql.append(" 	and exists (		  ");
            //        sql.append(" 		select 1 from 	  ");
            //        sql.append(" 		view_data_sales_detail_valid ds1	  ");
            //        sql.append(" 		inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 	  ");
            //        sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            //        sql.append(" 		where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 	  ");
            //        sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            //        sql.append(" 		) 	  ");
            //    }
            //    sql.append(" 	)) as unit_price		  ");
            //    unit_price = 0;
            //    try {
            //        rs = con.executeQuery(sql.toString());
            //        rs.next();
            //        unit_price = rs.getDouble("unit_price");
            //    } catch (Exception e) {
            //    }
            //    jx.setValue(29, 19, unit_price);
            //}
            //Luc end edit 20150629 #38737
            //1.1.41.パーマ比率（ＳＴＲ含む）					 
            //今年：
            sql = new StringBuffer();
            sql.append(" select 			  ");
            sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
            sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and product_division = 1			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            sql.append(" and mtc.technic_class_id in (33,40,67,68)			  ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" )*100/			  ");
            sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
            sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and product_division = 1			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" ))::float as parm_rate			  ");
            double rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("parm_rate");
            } catch (Exception e) {
            }


            jx.setValue(25, 20, rate);
            //目標：	
            sql = new StringBuffer();
            sql.append(" select rate*100 as rate from data_target_technic where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "   and technic_class_id in (33,40,67,68) and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            sql.append(" 			  ");
            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("rate");
            } catch (Exception e) {
            }
            jx.setValue(27, 20, rate);
            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select 			  ");
                sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
                sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and product_division = 1			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                sql.append(" and mtc.technic_class_id in (33,40,67,68)			  ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" )*100/			  ");
                sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
                sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and product_division = 1			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" ))::float as parm_rate			  ");
                rate = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    rate = rs.getDouble("parm_rate");
                } catch (Exception e) {
                }
                jx.setValue(29, 20, rate);
            }
            // 1.1.42.カラー比率					
            //今年：	
            sql = new StringBuffer();
            sql.append(" select 			  ");
            sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
            sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and product_division = 1			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            sql.append(" and mtc.technic_class_id in (32,66)			  ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" )*100/			  ");
            sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
            sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and product_division = 1			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" )) as color_rate			  ");
            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("color_rate");
            } catch (Exception e) {
            }
            jx.setValue(25, 21, rate);
            //目標：	
            sql = new StringBuffer();
            sql.append(" select rate*100 as color_rate from data_target_technic where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "   and technic_class_id in (32,66) and shop_category_id = " + shopCategoryId + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            sql.append(" 			  ");
            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("color_rate");
            } catch (Exception e) {
            }
            jx.setValue(27, 21, rate);
            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select 			  ");
                sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
                sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and product_division = 1			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                sql.append(" and mtc.technic_class_id in (32,66)			  ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" )*100/			  ");
                sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
                sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and product_division = 1			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" )) as color_rate			  ");
                rate = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    rate = rs.getDouble("color_rate");
                } catch (Exception e) {
                }
                jx.setValue(29, 21, rate);
            }
            //1.1.43.トリートメント比率（ＳＰＡ含む）					  
            //今年：	
            sql = new StringBuffer();
            sql.append(" select 			  ");
            sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
            sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and product_division = 1			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            sql.append(" and mtc.technic_class_id in (34,41,69,70)			  ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" )*100/			  ");
            sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
            sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and product_division = 1			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
            }
            sql.append(" )) as treat_rate			  ");
            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("treat_rate");
            } catch (Exception e) {
            }
            jx.setValue(25, 22, rate);
            //目標：
            sql = new StringBuffer();
            sql.append(" select rate*100 as treat_rate from data_target_technic where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + " and technic_class_id in (34,41,69,70) and shop_category_id =" + shopCategoryId + " 			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("treat_rate");
            } catch (Exception e) {
            }
            jx.setValue(27, 22, rate);
            //昨年：
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select 			  ");
                sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
                sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and product_division = 1			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                sql.append(" and mtc.technic_class_id in (34,41,69,70)			  ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" )*100/			  ");
                sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" inner join mst_technic mt on ds.product_id = mt.technic_id			  ");
                sql.append(" inner join mst_technic_class mtc on mt.technic_class_id = mtc.technic_class_id			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " -1			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and product_division = 1			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                }
                sql.append(" )		  ");
                sql.append(" ) as treat_rate	  ");
                sql.append(" 			  ");
                rate = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    rate = rs.getDouble("treat_rate");
                } catch (Exception e) {
                }
                jx.setValue(29, 22, rate);
            }
            //1.1.44.購買比率					 
            //今年：
            sql = new StringBuffer();
            sql.append(" select			  ");
            sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            //Luc start edit 20150626 #38252 NO.38
            //sql.append(" and product_division = 2			  ");
            sql.append(" and product_division in (2,4)			  ");
            //Luc end edit 20150626 #38252 NO.38
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and exists(			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	)		  ");
            }
            sql.append(" )*100/			  ");
            sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " 			  ");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and 			  ");
                sql.append(" (			  ");
                sql.append(" exists (			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	) 		  ");
                sql.append(" or 			  ");
                sql.append(" exists(			  ");
                sql.append(" 	select 1 from 		  ");
                sql.append(" 	view_data_sales_detail_valid ds1		  ");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                sql.append(" 	)		  ");
                sql.append(" )			  ");
            }
            sql.append(" ))	as cus_rate		  ");

            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("cus_rate");
            } catch (Exception e) {
            }
            jx.setValue(25, 23, rate);
            //目標：
            //            sql = new StringBuffer();
            //            sql.append(" select sum(item)/sum(customer) as rate from data_target where year = " + SQLUtil.convertForSQL(SelectedYear) + " and month =" + SQLUtil.convertForSQL(SelectedMonth) + " and shop_category_id = " + shopCategoryId + "			  ");
            //            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            //            sql.append(" 			  ");
            //            rate = 0;
            //            try {
            //                rs = con.executeQuery(sql.toString());
            //                rs.next();
            //                rate = rs.getDouble("cus_rate");
            //            } catch (Exception e) {
            //            }
            //            jx.setValue(26, 17, rate);
            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select			  ");
                sql.append(" ((select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                //Luc start edit 20150626 #38252 NO.38
                //sql.append(" and product_division = 2			  ");
                sql.append(" and product_division in (2,4)			  ");
                //Luc end edit 20150626 #38252 NO.38
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                }
                sql.append(" )*100/			  ");
                sql.append(" (select count(distinct slip_no) from view_data_sales_detail_valid ds			  ");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 			  ");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "			  ");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and 			  ");
                    sql.append(" (			  ");
                    sql.append(" exists (			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 		  ");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mtc.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	) 		  ");
                    sql.append(" or 			  ");
                    sql.append(" exists(			  ");
                    sql.append(" 	select 1 from 		  ");
                    sql.append(" 	view_data_sales_detail_valid ds1		  ");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  		  ");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id 		  ");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no 		  ");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + "		  ");
                    sql.append(" 	)		  ");
                    sql.append(" )			  ");
                }
                sql.append(" )) as cus_rate			  ");

                rate = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    rate = rs.getDouble("cus_rate");
                } catch (Exception e) {
                }
                jx.setValue(29, 23, rate);
            }

            //1.1.45.購買単価																								
            //今年：	
            sql = new StringBuffer(8000);
            sql.append(" select \n");
            sql.append(" (select sum(discount_detail_value_no_tax) from view_data_sales_detail_valid ds \n");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " \n");
            sql.append(" and date_part('month',sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            //Luc start edit 20150626 #38252 NO.39
            //sql.append(" and product_division = 2			  ");
            sql.append(" and product_division in (2,4)			  ");
            //Luc end edit 20150626 #38252 NO.39
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and exists( \n");
                sql.append(" 	select 1 from \n");
                sql.append(" 	view_data_sales_detail_valid ds1 \n");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id  \n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
                sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + " \n");
                sql.append(" 	) \n");
            }
            sql.append(" )	 \n");
            sql.append(" /(select count(distinct slip_no) from view_data_sales_detail_valid ds	 \n");
            sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + " \n");
            sql.append(" and date_part('month',sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + " \n");
            sql.append(" and product_division = 2	 \n");
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
            if (shopCategoryId > 0) {
                sql.append(" and exists(	 \n");
                sql.append(" 	select 1 from 	 \n");
                sql.append(" 	view_data_sales_detail_valid ds1 \n");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2   \n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
                sql.append(" 	and mic.shop_category_id = " + shopCategoryId + " \n");
                sql.append(" 	) \n");
            }
            
            sql.append(" ) as sales_unit_price \n");
            double sales_unit_price = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                sales_unit_price = rs.getDouble("sales_unit_price");
            } catch (Exception e) {
            }
            jx.setValue(25, 24, sales_unit_price);

            //目標：空白で出力																						

            //昨年：
            if(n == 0) {
                sql = new StringBuffer();
                sql.append(" select  \n");
                sql.append(" (select sum(discount_detail_value_no_tax) from view_data_sales_detail_valid ds \n");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 \n");
                sql.append(" and date_part('month',sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + " \n");
                //Luc start edit 20150626 #38252 NO.39
                //sql.append(" and product_division = 2			  ");
                sql.append(" and product_division in (2,4)			  ");
                //Luc end edit 20150626 #38252 NO.39
                 sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists( \n");
                    sql.append(" 	select 1 from  \n");
                    sql.append(" 	view_data_sales_detail_valid ds1 \n");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2   \n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id  \n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no  \n");
                    sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + " \n");
                    sql.append(" 	) \n");
                }
                sql.append(" ) \n");
                sql.append(" /(select count(distinct slip_no) from view_data_sales_detail_valid ds \n");
                sql.append(" where date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 \n");
                sql.append(" and date_part('month',sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + " \n");
                sql.append(" and product_division = 2 \n");
                sql.append(" and shop_id in (" + selectedShop.getShopID() + ") ");
                if (shopCategoryId > 0) {
                    sql.append(" and exists( \n");
                    sql.append(" 	select 1 from \n");
                    sql.append(" 	view_data_sales_detail_valid ds1 \n");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mic.shop_category_id = " + shopCategoryId + " \n");
                    sql.append(" 	) \n");
                }
                sql.append(" ) as sales_unit_price \n");
                sales_unit_price = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    sales_unit_price = rs.getDouble("sales_unit_price");
                } catch (Exception e) {
                }
                jx.setValue(29, 24, sales_unit_price);
            }
            //1.1.46.店販比率																							

            //	当月店販売上高/当月技術売上高(４÷３)																				

            //1.1.41.仕入原価率（業務原価÷技売）																							

            //今年：	
            sql = new StringBuffer();
            sql.append(" select \n");
            sql.append(" (select sum(sd.out_num*sd.cost_price) as in_amount\n");
            sql.append(" from data_slip_ship s\n");
            sql.append(" inner join data_slip_ship_detail sd on s.shop_id = sd.shop_id and s.slip_no = sd.slip_no\n");

            sql.append(" where s.shop_id in (" + selectedShop.getShopID() + ")  \n");
            sql.append(" and extract(year from ship_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and  extract(month from ship_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and item_use_division = 2	\n");
            if (shopCategoryId > 0) {
                sql.append(" and exists(\n");
                sql.append(" 	select 1 from \n");
                sql.append(" 	data_slip_ship_detail sd1\n");
                sql.append(" 	inner join mst_item mi on sd1.item_id = mi.item_id\n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                sql.append(" 	where sd1.slip_no = sd.slip_no and sd1.shop_id = sd.shop_id \n");
                sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 	)\n");
            }

            sql.append(" )/\n");
            sql.append(" (\n");
            sql.append(" select sum(discount_detail_value_no_tax)\n");
            sql.append(" from view_data_sales_detail_valid ds\n");
            sql.append(" where shop_id in (" + selectedShop.getShopID() + ") \n");
            sql.append(" and date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and product_division = 1	\n");
            if (shopCategoryId > 0) {
                sql.append(" and exists (\n");
                sql.append(" 	select 1 from\n");
                sql.append(" 	view_data_sales_detail_valid ds1\n");
                sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1\n");
                sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 	and mtc.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 	)\n");
            }
            sql.append("  ) as purchar_unit_price \n");
            double purchar_unit_price = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                purchar_unit_price = rs.getDouble("purchar_unit_price");
            } catch (Exception e) {
            }
            jx.setValue(25, 26, purchar_unit_price);

            //昨年：
            sql = new StringBuffer();
            if (n == 0) {
                sql.append(" select \n");
                sql.append(" (select sum(sd.out_num*sd.cost_price) as in_amount	\n");
                sql.append(" from data_slip_ship s\n");
                sql.append(" inner join data_slip_ship_detail sd on s.shop_id = sd.shop_id and s.slip_no = sd.slip_no\n");

                sql.append(" where s.shop_id in (" + selectedShop.getShopID() + ")  \n");
                sql.append(" and extract(year from ship_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1\n");
                sql.append(" and  extract(month from ship_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append(" and item_use_division = 2	\n");
                if (shopCategoryId > 0) {
                    sql.append(" and exists(\n");
                    sql.append(" 	select 1 from \n");
                    sql.append(" 	data_slip_ship_detail sd1\n");
                    sql.append(" 	inner join mst_item mi on sd1.item_id = mi.item_id\n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append(" 	where sd1.slip_no = sd.slip_no and sd1.shop_id = sd.shop_id \n");
                    sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 	)\n");
                }

                sql.append(" )/\n");
                sql.append(" (	\n");
                sql.append(" select sum(discount_detail_value_no_tax)\n");
                sql.append(" from view_data_sales_detail_valid ds \n");
                sql.append(" where shop_id in (" + selectedShop.getShopID() + ") 	\n");
                sql.append(" and date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "-1	\n");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append(" and product_division = 1	\n");
                if (shopCategoryId > 0) {
                    sql.append(" and exists (\n");
                    sql.append(" 	select 1 from \n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_technic mt on ds1.product_id = mt.technic_id and ds.product_division = ds1.product_division and ds1.product_division=1 \n");
                    sql.append(" 	inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id\n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mtc.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 	) \n");
                }
                sql.append("  ) as purchar_unit_price\n");
                purchar_unit_price = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    purchar_unit_price = rs.getDouble("purchar_unit_price");
                } catch (Exception e) {
                }
                jx.setValue(29, 26, purchar_unit_price);
            }
            //1.1.48.仕入原価率(店販原価÷店販売）																							

            //今年：	
            sql = new StringBuffer();
            sql.append(" select (\n");
            sql.append(" select sum(ds.product_num* si.cost_price)	\n");
            sql.append(" from view_data_sales_detail_valid ds\n");
            sql.append(" inner join mst_supplier_item si on ds.product_id = si.item_id \n");
            sql.append(" where shop_id in (" + selectedShop.getShopID() + ") 	\n");
            sql.append(" and date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and product_division = 2	\n");
            sql.append(" and si.delete_date is null\n");
            if (shopCategoryId > 0) {
                sql.append(" and exists(\n");
                sql.append(" 	select 1 from \n");
                sql.append(" 	view_data_sales_detail_valid ds1\n");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  	\n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 	)\n");
            }
            sql.append("  )\n");
            sql.append(" /	\n");
            sql.append(" (\n");
            sql.append(" select sum(discount_detail_value_no_tax)\n");
            sql.append(" from view_data_sales_detail_valid ds\n");
            sql.append(" where shop_id in (" + selectedShop.getShopID() + ") 	\n");
            sql.append(" and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "\n");
            sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
            sql.append(" and product_division = 2\n");
            if (shopCategoryId > 0) {
                sql.append(" and exists(\n");
                sql.append(" 	select 1 from\n");
                sql.append(" 	view_data_sales_detail_valid ds1\n");
                sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + "\n");
                sql.append(" 	)\n");
            }
            sql.append("  ) as item_parchar_unit_price\n");
            double item_parchar_unit_price = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                item_parchar_unit_price = rs.getDouble("item_parchar_unit_price");
            } catch (Exception e) {
            }
            jx.setValue(25, 27, item_parchar_unit_price);

            //昨年：	
            sql = new StringBuffer();
            if (n == 0) {
                sql.append(" select (\n");
                sql.append(" select sum(ds.product_num* si.cost_price)\n");
                sql.append(" from view_data_sales_detail_valid ds\n");
                sql.append(" inner join mst_supplier_item si on ds.product_id = si.item_id\n");
                sql.append(" where shop_id in (" + selectedShop.getShopID() + ") \n");
                sql.append(" and date_part('year',sales_date) = " + SQLUtil.convertForSQL(SelectedYear) + "-1	\n");
                sql.append(" and date_part('month',sales_date) =" + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append(" and product_division = 2\n");
                sql.append(" and si.delete_date is null	\n");
                if (shopCategoryId > 0) {
                    sql.append(" and exists(\n");
                    sql.append(" 	select 1 from\n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2 \n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 	)\n");
                }
                sql.append("  )	\n");
                sql.append(" /	\n");
                sql.append(" (	\n");
                sql.append(" select sum(discount_detail_value_no_tax)	\n");
                sql.append(" from view_data_sales_detail_valid ds\n");
                sql.append(" where shop_id in (" + selectedShop.getShopID() + ") 	\n");
                sql.append(" and date_part('year',sales_date) =" + SQLUtil.convertForSQL(SelectedYear) + "-1	\n");
                sql.append(" and date_part('month',sales_date) = " + SQLUtil.convertForSQL(SelectedMonth) + "\n");
                sql.append(" and product_division = 2\n");
                if (shopCategoryId > 0) {
                    sql.append(" and exists(\n");
                    sql.append(" 	select 1 from 	\n");
                    sql.append(" 	view_data_sales_detail_valid ds1\n");
                    sql.append(" 	inner join mst_item mi on ds1.product_id = mi.item_id and ds.product_division = ds1.product_division and ds1.product_division = 2  \n");
                    sql.append(" 	inner join mst_item_class mic on mic.item_class_id = mi.item_class_id \n");
                    sql.append(" 	where ds1.slip_no = ds.slip_no and ds1.shop_id = ds.shop_id and ds1.slip_detail_no = ds.slip_detail_no \n");
                    sql.append(" 	and mic.shop_category_id =  " + shopCategoryId + "\n");
                    sql.append(" 	)\n");
                }
                sql.append("  ) as item_parchar_unit_price	\n");
                item_parchar_unit_price = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    item_parchar_unit_price = rs.getDouble("item_parchar_unit_price");
                } catch (Exception e) {
                }
                jx.setValue(29, 27, item_parchar_unit_price);
            }

            //1.1.49.事前予約率					  
            //今年：
            sql = new StringBuffer();
            sql.append(" select			  ");
            sql.append(" ((			  ");
            sql.append(" 	select count(reservation_no)		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select drd.reservation_no,count(*)	  ");
            sql.append(" 		from data_reservation_detail drd	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            }
            sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
            sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
            sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
            sql.append(" 		and dr.preorder_flag = 1 	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            }
            //sql.append(" 		and dr. slip_no is not null ");
            sql.append(" 		group by drd.reservation_no	  ");
            sql.append(" 	)dr		  ");
            sql.append(" )*100			  ");
            sql.append(" /			  ");
            sql.append(" (	select count(reservation_no)		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select drd.reservation_no,count(*)	  ");
            sql.append(" 			  ");
            sql.append(" 		 from data_reservation_detail drd	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            }
            sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
            sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
            sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
            if (shopCategoryId > 0) {
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            }
            //sql.append(" 		and dr. slip_no is not null ");
            sql.append(" 		group by drd.reservation_no	  ");
            sql.append(" 	)dr		  ");
            sql.append(" ))::float as before_reserve			  ");
            sql.append(" 			  ");
            sql.append(" 			  ");
            //IVS Luc start add 20141110 [Phase 4][IT][Exec][EDIT]帳票_店舗実績_事前予約率
            double before_reserve = 0;
            //IVS Luc end add 20141110 [Phase 4][IT][Exec][EDIT]帳票_店舗実績_事前予約率
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                before_reserve = rs.getDouble("before_reserve");
            } catch (Exception e) {
            }
            jx.setValue(25, 29, before_reserve);
            //目標：
            sql = new StringBuffer();         
            sql.append(" select before_reserve*100 as before_reserve from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth) + "  and shop_category_id = " + shopCategoryId + "			  ");          
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            before_reserve = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                before_reserve = rs.getDouble("before_reserve");
            } catch (Exception e) {
            }
            jx.setValue(27, 29, before_reserve);
            //昨年：	
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select			  ");
                sql.append(" ((			  ");
                sql.append(" 	select count(reservation_no)		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select drd.reservation_no,count(*)	  ");
                sql.append(" 			  ");
                sql.append(" 		 from data_reservation_detail drd	  ");
                
                if (shopCategoryId > 0) {
                    sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                }
                sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
                sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + " -1  and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
                sql.append(" 		and dr.preorder_flag = 1 	  ");
                sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
                if (shopCategoryId > 0) {
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                }
                sql.append(" 		group by drd.reservation_no	  ");
                sql.append(" 	)dr		  ");
                sql.append(" )*100			  ");
                sql.append(" /			  ");
                sql.append(" (	select count(reservation_no)		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select drd.reservation_no,count(*)	  ");
                sql.append(" 			  ");
                sql.append(" 		 from data_reservation_detail drd	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                }
                sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
                sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
                sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
                if (shopCategoryId > 0) {
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                }
                sql.append(" 		group by drd.reservation_no	  ");
                sql.append(" 	)dr		  ");
                sql.append(" ))::float as before_reserve ");

                before_reserve = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    before_reserve = rs.getDouble("before_reserve");
                } catch (Exception e) {
                }
                jx.setValue(29, 29, before_reserve);
            }
            //1.1.50.次回予約率				
            //今年：	
            sql = new StringBuffer();
            sql.append(" select			  ");
            sql.append(" ((			  ");
            sql.append(" 	select count(reservation_no)		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select drd.reservation_no,count(*)	  ");
            sql.append(" 			  ");
            sql.append(" 		from data_reservation_detail drd	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            }
            sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
            sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
            sql.append(" 		and dr.next_flag= 1 	  ");
            sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
            if (shopCategoryId > 0) {
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            }
            //sql.append(" 		and dr. slip_no is not null ");
            sql.append(" 		group by drd.reservation_no	  ");
            sql.append(" 	)dr		  ");
            sql.append(" )*100			  ");
            sql.append(" /			  ");
            sql.append(" (	select count(reservation_no)		  ");
            sql.append(" 	from		  ");
            sql.append(" 	(		  ");
            sql.append(" 		select drd.reservation_no,count(*)	  ");
            sql.append(" 			  ");
            sql.append(" 		 from data_reservation_detail drd	  ");
            if (shopCategoryId > 0) {
                sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
            }
            sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
            sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + " and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
            sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
            sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
            if (shopCategoryId > 0) {
                sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
            }
            //sql.append(" 		and dr. slip_no is not null ");
            sql.append(" 		group by drd.reservation_no	  ");
            sql.append(" 	)dr		  ");
            sql.append(" ))::float as next_reserve			  ");
            sql.append(" 			  ");
            double next_reserve = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                next_reserve = rs.getDouble("next_reserve");
            } catch (Exception e) {
            }
            jx.setValue(25, 30, next_reserve);
            //目標：
            sql = new StringBuffer();
             //Luc start edit 20141111
            sql.append(" select next_reserve*100 as next_reserve from data_target where year =" + SQLUtil.convertForSQL(SelectedYear) + " and month = " + SQLUtil.convertForSQL(SelectedMonth)+ "  and shop_category_id = " + shopCategoryId  + "  			  ");
             //Luc start edit 20141111
            sql.append(" and shop_id in (" + selectedShop.getShopID() + ")\n");
            sql.append(" 			  ");
            rs = con.executeQuery(sql.toString());
            rs.next();
            next_reserve = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                next_reserve = rs.getDouble("next_reserve");
            } catch (Exception e) {
            }
            jx.setValue(27, 30, next_reserve);
            //昨年：
            if (n == 0) {
                sql = new StringBuffer();
                sql.append(" select			  ");
                sql.append(" ((			  ");
                sql.append(" 	select count(reservation_no)		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select drd.reservation_no,count(*)	  ");
                sql.append(" 			  ");
                sql.append(" 		 from data_reservation_detail drd	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                }
                sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
                sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + " -1  and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
                sql.append(" 		and dr.next_flag = 1 	  ");
               sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
                if (shopCategoryId > 0) {
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                }
                sql.append(" 		group by drd.reservation_no	  ");
                sql.append(" 	)dr		  ");
                sql.append(" )*100			  ");
                sql.append(" /			  ");
                sql.append(" (	select count(reservation_no)		  ");
                sql.append(" 	from		  ");
                sql.append(" 	(		  ");
                sql.append(" 		select drd.reservation_no,count(*)	  ");
                sql.append(" 			  ");
                sql.append(" 		 from data_reservation_detail drd	  ");
                if (shopCategoryId > 0) {
                    sql.append(" 		inner join mst_technic mt on drd.technic_id = mt.technic_id and course_flg is null	  ");
                    sql.append(" 		inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id 	  ");
                }
                sql.append(" 		inner join data_reservation dr on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no	  ");
                sql.append(" 		where date_part('year',reservation_datetime) =" + SQLUtil.convertForSQL(SelectedYear) + "-1 and date_part('month',reservation_datetime) = " + SQLUtil.convertForSQL(SelectedMonth) + "	  ");
                sql.append(" 		and drd.delete_date is null and dr.delete_date is null 	  ");
                sql.append(" 		and dr.shop_id in ("+selectedShop.getShopID()+")");
                if (shopCategoryId > 0) {
                    sql.append(" 		and mtc.shop_category_id = " + shopCategoryId + "	  ");
                }
                sql.append(" 		group by drd.reservation_no	  ");
                sql.append(" 	)dr		  ");
                sql.append(" ))::float as next_reserve			  ");
                next_reserve = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    next_reserve = rs.getDouble("next_reserve");
                } catch (Exception e) {
                }
                jx.setValue(29, 30, next_reserve);

            }
            
             //1.1.51.紹介人数（＠ＳＴ）
            //今年：
            MstTargetLogic2 targ = new MstTargetLogic2();
            sql = targ.getIntroduceSQL(selectedShop,SelectedYear, SelectedMonth, shopCategoryId);
            rate = 0;
            try {
                rs = con.executeQuery(sql.toString());
                rs.next();
                rate = rs.getDouble("cus_count");
            } catch (Exception e) {
            }
            jx.setValue(25, 31, rate);

            //昨年：	
            if(n==0) {
                sql = targ.getIntroduceLastYearSQL(selectedShop,SelectedYear, SelectedMonth, shopCategoryId);
                rate = 0;
                try {
                    rs = con.executeQuery(sql.toString());
                    rs.next();
                    rate = rs.getDouble("cus_count");
                } catch (Exception e) {
                }
                jx.setValue(29, 31, rate);
            }

            //期間
            jx.setValue(4, 32, String.valueOf(SelectedYear) + "年" + String.valueOf(SelectedMonth) + "月");
            jx.setValue(10, 32, shopCategoryName);
            //店舗
            jx.setValue(4, 34, selectedShop.getShopName());
            //店長

        } catch (SQLException e) {

            e.printStackTrace();

        }

        //ファイル出力
        jx.openWorkbook();
    }
    
}

/*
 * CustomerRankingList.java
 *
 * Created on 2008/07/20, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.master.company.MstShopSetting;
import java.util.*;
import java.util.logging.*;
import java.math.BigDecimal;
import java.text.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.SQLException;
/**
 * スタッフランキング一覧取得
 * クロス分析一覧取得
 * @author saito
 */
public class CustomerRankingList extends ArrayList<CustomerRanking>
{
        //税区分
        private final	int	TAX_TYPE_BLANK		=   1;	//税抜
	private final	int	TAX_TYPE_UNIT		=   2;	//税込

        private long limitCount = 0l;
        
        /**
	 * 店舗IDリスト
	 */
	private	String   shopIDList             =   null;
	/**
	 * 担当者ID
	 */
	private	Integer	staffID                 =   null;
        /**
	 * 対象期間(開始日)
	 */
	private	GregorianCalendar   termFrom    =   new GregorianCalendar();
	/**
	 * 対象期間(終了日)
	 */
	private	GregorianCalendar   termTo      =   new GregorianCalendar();
        
        private boolean isPastTotal            =  false;
	/**
	 * 税区分
	 */
	private	Integer	taxType                 =   null;
	/**
	 * 評価対象
	 */
	private	Integer	productDivision         =   null;
	
	/**
	 * 来店回数設定－ランク３
	 */
        private long rankF3 = 0l;
	/**
	 * 来店回数設定－ランク２
	 */
        private long rankF2 = 0l;
	/**
	 * 来店回数設定－ランク１
	 */
        private long rankF1 = 0l;
	/**
	 * 売上金額設定－ランク３
	 */
        private long rankM3 = 0l;
	/**
	 * 売上金額設定－ランク２
	 */
        private long rankM2 = 0l;
	/**
	 * 売上金額設定－ランク１
	 */
        private long rankM1 = 0l;
	/**
	 * 稼働客
	 */
        private boolean chkValid1 = false;
	/**
	 * 現役客
	 */
        private boolean chkValid2 = false;
	/**
	 * 離店客
	 */
        private boolean chkValid3 = false;
        
        //nhanvt start add 20141201 New request #33406
        private String listCategoryId = null;
        private boolean hideCategory = false;
        private Integer useShopCategory = null;
        

        public String getListCategoryId() {
            return listCategoryId;
        }

        public void setListCategoryId(String listCategoryId) {
            this.listCategoryId = listCategoryId;
        }

        public boolean isHideCategory() {
            return hideCategory;
        }

        public void setHideCategory(boolean hideCategory) {
            this.hideCategory = hideCategory;
        }

        public Integer getUseShopCategory() {
            return useShopCategory;
        }

        public void setUseShopCategory(Integer useShopCategory) {
            this.useShopCategory = useShopCategory;
        }
	//nhanvt end add 20141201 New request #33406
        
	/**
         * Creates a new instance of CustomerRankingList
         */
	public CustomerRankingList()
	{
	}
	
	/**
	 * 店舗IDリストを取得する。
	 * @return 店舗IDリスト
	 */
        public String getShopIDList() {
                return shopIDList;
        }
	/**
	 * 店舗IDリストをセットする。
	 * @param shopIDList 店舗IDリスト
	 */
        public void setShopIDList(String shopIDList) {
                this.shopIDList = shopIDList;
        }
	/**
	 * 担当者IDを取得する。
	 * @return 担当者ID
	 */
        public Integer getStaffID() {
                return staffID;
        }
	/**
         * 担当者IDをセットする。
         * @param staffID 担当者ID
         */
        public void setStaffID(Integer staffID) {
                this.staffID = staffID;
        }
	/**
	 * 対象期間(開始日)を取得する。
	 * @return 対象期間(開始日)
	 */
        public String getTermFrom() {
                return SQLUtil.convertForSQLDateOnly(termFrom);
        }
	/**
	 * 対象期間(開始日)をセットする。
	 * @param termFrom 対象期間(開始日)
	 */
        public void setTermFrom(java.util.Date termFrom) {
                this.termFrom.setTime(termFrom);
        }
	/**
	 * 対象期間(終了日)を取得する。
	 * @return 対象期間(終了日)
	 */
        public String getTermTo() {
                return SQLUtil.convertForSQLDateOnly(termTo);
        }
	/**
	 * 対象期間(終了日)をセットする。
	 * @param termTo 対象期間(終了日)
	 */
        public void setTermTo(java.util.Date termTo) {
                this.termTo.setTime(termTo);
        }
	/**
	 * 税区分を取得する。
	 * @return 税区分
	 */
        public int getTaxType() {
                return taxType;
        }
	/**
	 * 税区分をセットする。
	 * @param taxType 税区分
	 */
        public void setTaxType(int taxType) {
                this.taxType = taxType;
        }
	/**
	 * 評価対象を取得する。
	 * @return 評価対象
	 */
        public int getProductDivision() {
                return productDivision;
        }
	/**
	 * 評価対象をセットする。
	 * @param productDivision 評価対象
	 */
        public void setProductDivision(int productDivision) {
                this.productDivision = productDivision;
        }


        /**
         * デシル分析データを読み込む。
         */
	public void load_CustomerRanking()
	{
		try
		{
                    //対象データをワークテーブルに格納
                    this.getTargetList(false);

                    //ランキングリスト取得
                    this.getCustomerRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

        /**
         * クロス分析データを読み込む。
         */
	public void load_CrossAnalysis()
	{
		try
		{
                    //対象データをワークテーブルに格納
                    this.getTargetList(true);

                    //クロス分析リスト取得
                    this.getCrossAnalysisList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
        
	/**
	 * 対象データをワークテーブルに格納
	 * @exception Exception
	 */
	private void getTargetList(boolean isCrossAnalysis) throws Exception
	{
                ConnectionWrapper con = SystemInfo.getConnection();

                try {
                    con.execute("drop table wk_rank");
                } catch (Exception e) {
                }

                StringBuilder sql = new StringBuilder(1000);
                
                sql.append(" create temporary table wk_rank as");

                sql.append(" select");
                sql.append("      a.customer_id");
                sql.append("     ,max(coalesce(b.customer_no, '')) as customer_no");
                sql.append("     ,max(coalesce(b.customer_name1, '') || '　' || coalesce(b.customer_name2, '')) as customer_name");
                sql.append("     ,max(coalesce(b.pc_mail_address, '')) as pc_mail_address");
                sql.append("     ,max(coalesce(b.cellular_mail_address, '')) as cellular_mail_address");
                sql.append("     ,max(a.sales_date) as sales_date");
                
                // 来店回数
                if (isPastTotal) {
                    
                    // 過去累積を含む場合
                    sql.append("     ,(");
                    sql.append("         select");
                    sql.append("             count(ds.slip_no) + coalesce(max(mc.before_visit_num), 0)");
                    sql.append("         from");
                    sql.append("             data_sales ds");
                    sql.append("                 inner join mst_customer mc");
                    sql.append("                 using(customer_id)");
                    sql.append("         where");
                    sql.append("                 ds.customer_id = a.customer_id");
                    sql.append("             and ds.shop_id in (" + getShopIDList() + ")");
                    sql.append("             and ds.sales_date is not null");
                    sql.append("             and ds.delete_date is null");
                    //nhanvt start add 20141201 New request #33406
                    if( this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                        sql.append("      and   exists");
                        sql.append("             (");

                        //業態利用するかしないかチェック

                        sql.append("                 SELECT");
                        sql.append("                     1");
                        sql.append("                 FROM");
                        sql.append("                     data_sales_detail dsd");
                        sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                        sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                        sql.append("                 WHERE");
                        sql.append("                         dsd.shop_id = ds.shop_id");
                        sql.append("                     AND dsd.slip_no = ds.slip_no");
                        sql.append("                     AND dsd.delete_date is null");
                        sql.append("                     AND dsd.product_division in (1,3)");
                        //業態選択の場合
                        if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                            sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                        }
                        
                        sql.append("                  UNION ALL  ");
                        sql.append("                  SELECT ");
                        sql.append("                     1");
                        sql.append("                 FROM");
                        sql.append("                      data_sales_detail dsd");
                        sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                        sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                        sql.append("                  WHERE");
                        sql.append("                          dsd.shop_id = ds.shop_id");
                        sql.append("                      AND dsd.slip_no = ds.slip_no");
                        sql.append("                      AND dsd.delete_date is null");
                        sql.append("                      AND dsd.product_division in (2,4)");
                        //業態選択の場合
                        if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                            sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                        }
                        
                        sql.append("                 UNION ALL  ");

                        sql.append("                 SELECT ");
                        sql.append("                     1");
                        sql.append("                 FROM");
                        sql.append("                     data_sales_detail dsd");
                        sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id  ");
                        sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                        sql.append("                 WHERE");
                        sql.append("                         dsd.shop_id = ds.shop_id");
                        sql.append("                     AND dsd.slip_no = ds.slip_no");
                        sql.append("                     AND dsd.delete_date is null");
                        sql.append("                     AND dsd.product_division in (5,6)");
                        //業態選択の場合
                        if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                            sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                        }

                        sql.append(" ) ");
                    }
                    //nhanvt end add 20141201 New request #33406
                    
                    sql.append("     ) as visit_count");
                    
                } else {
                    
                    sql.append("     ,count(distinct slip_no) as visit_count");
                }
                
                sql.append("     ,(");
                sql.append("         select");
                sql.append("             ms.staff_name1 || '　' || ms.staff_name2");
                sql.append("         from");
                sql.append("             data_sales ds");
                sql.append("                 join mst_staff ms");
                sql.append("                 using(staff_id)");
                sql.append("         where");
                sql.append("                 ds.customer_id = a.customer_id");
                sql.append("             and ds.shop_id in (" + getShopIDList() + ")");
                sql.append("             and ds.sales_date = max(a.sales_date)");
                sql.append("             and ds.delete_date is null");
                //nhanvt start add 20141201 New request #33406
                if(this.getUseShopCategory() != null &&  this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //業態利用するかしないかチェック

                    sql.append("                 SELECT");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id  ");
                    sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = ds.shop_id");
                    sql.append("                     AND dsd.slip_no = ds.slip_no");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (1,3)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append("                  UNION ALL  ");
                    sql.append("                  SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                      data_sales_detail dsd");
                    sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                    sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                    sql.append("                  WHERE");
                    sql.append("                          dsd.shop_id = ds.shop_id");
                    sql.append("                      AND dsd.slip_no = ds.slip_no");
                    sql.append("                      AND dsd.delete_date is null");
                    sql.append("                      AND dsd.product_division in (2,4)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append("                 UNION ALL  ");

                    sql.append("                 SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                    sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = ds.shop_id");
                    sql.append("                     AND dsd.slip_no = ds.slip_no");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (5,6)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                //nhanvt end add 20141201 New request #33406
                sql.append("         order by");
                sql.append("              ds.insert_date desc");
                sql.append("             ,ds.slip_no desc");
                sql.append("         limit 1");
                sql.append("      ) as staff_name");
                sql.append("     ,(");
                sql.append("         select");
                sql.append("             ds.designated_flag");
                sql.append("         from");
                sql.append("             data_sales ds");
                sql.append("         where");
                sql.append("                 ds.customer_id = a.customer_id");
                sql.append("             and ds.shop_id in (" + getShopIDList() + ")");
                sql.append("             and ds.sales_date = max(a.sales_date)");
                sql.append("             and ds.delete_date is null");
                
                //nhanvt start add 20141201 New request #33406
                if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //業態利用するかしないかチェック

                    sql.append("                 SELECT");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                    sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = ds.shop_id");
                    sql.append("                     AND dsd.slip_no = ds.slip_no");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (1,3)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append("                  UNION ALL  ");
                    sql.append("                  SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                      data_sales_detail dsd");
                    sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                    sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                    sql.append("                  WHERE");
                    sql.append("                          dsd.shop_id = ds.shop_id");
                    sql.append("                      AND dsd.slip_no = ds.slip_no");
                    sql.append("                      AND dsd.delete_date is null");
                    sql.append("                      AND dsd.product_division in (2,4)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append("                 UNION ALL  ");

                    sql.append("                 SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                    sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = ds.shop_id");
                    sql.append("                     AND dsd.slip_no = ds.slip_no");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (5,6)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                //nhanvt end add 20141201 New request #33406
                
                
                sql.append("         order by");
                sql.append("              ds.insert_date desc");
                sql.append("             ,ds.slip_no desc");
                sql.append("         limit 1");
                sql.append("      ) as designated_flag");

                if (getProductDivision() == 0) {

                    //---------------------------------
                    //評価対象　全て
                    //---------------------------------
                    String salesValueColumn = "";
                    if (getTaxType() == TAX_TYPE_BLANK){
                        //全体割引後税抜き金額
                        salesValueColumn = "a.discount_sales_value_no_tax";
                    } else {
                        //全体割引後税込み金額
                        salesValueColumn = "a.discount_sales_value_in_tax";
                    }

                    // 売上金額
                    if (isPastTotal) {

                        // 過去累積を含む場合
                        sql.append("     ,(");
                        sql.append("         select");
                        sql.append("             sum(dsv.discount_sales_value_in_tax)");
                        sql.append("         from");
                        sql.append("             view_data_sales_valid dsv ");
                        sql.append("         where");
                        sql.append("                 dsv.shop_id in (" + getShopIDList() + ")");
                        sql.append("             and dsv.customer_id = a.customer_id");
                        sql.append("             and dsv.sales_date is not null");
                        
                         //nhanvt start add 20141201 New request #33406
                        if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                            sql.append("      and   exists");
                            sql.append("             (");

                            //業態利用するかしないかチェック

                            sql.append("                 SELECT");
                            sql.append("                     1");
                            sql.append("                 FROM");
                            sql.append("                     data_sales_detail dsd");
                            sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id  ");
                            sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                            sql.append("                 WHERE");
                            sql.append("                         dsd.shop_id = dsv.shop_id");
                            sql.append("                     AND dsd.slip_no = dsv.slip_no");
                            sql.append("                     and dsd.slip_detail_no = dsv.slip_detail_no   ");
                            sql.append("                     AND dsd.delete_date is null");
                            sql.append("                     AND dsd.product_division in (1,3)");
                            //業態選択の場合
                            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                            }

                            sql.append("                  UNION ALL  ");
                            sql.append("                  SELECT ");
                            sql.append("                     1");
                            sql.append("                 FROM");
                            sql.append("                      data_sales_detail dsd");
                            sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                            sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                            sql.append("                  WHERE");
                            sql.append("                          dsd.shop_id = dsv.shop_id");
                            sql.append("                      AND dsd.slip_no = dsv.slip_no");
                            sql.append("                     and dsd.slip_detail_no = dsv.slip_detail_no   ");
                            sql.append("                      AND dsd.delete_date is null");
                            sql.append("                      AND dsd.product_division in (2,4)");
                            //業態選択の場合
                            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                            }

                            sql.append("                 UNION ALL  ");

                            sql.append("                 SELECT ");
                            sql.append("                     1");
                            sql.append("                 FROM");
                            sql.append("                     data_sales_detail dsd");
                            sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                            sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                            sql.append("                 WHERE");
                            sql.append("                         dsd.shop_id = dsv.shop_id");
                            sql.append("                     AND dsd.slip_no = dsv.slip_no");
                            sql.append("                     and dsd.slip_detail_no = dsv.slip_detail_no   ");
                            sql.append("                     AND dsd.delete_date is null");
                            sql.append("                     AND dsd.product_division in (5,6)");
                            //業態選択の場合
                            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                            }

                            sql.append(" ) ");
                        }
                        //nhanvt end add 20141201 New request #33406

                        sql.append("     ) as sales_value");

                    } else {

                        sql.append(" ,sum(" + salesValueColumn + ") as sales_value");
                    }

                    sql.append(" from");
                    sql.append("     view_data_sales_valid a");
                    sql.append("         join mst_customer b using (customer_id)");
                    sql.append(" where");
                    sql.append("         a.shop_id in (" + getShopIDList() + ")");
                    if (getStaffID() != null) {
                        sql.append(" and a.staff_id = " + getStaffID().intValue());
                    }
                    sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
                    sql.append("     and b.customer_no <> '0'");
                    //2016/09/06 GB MOD #54449 Start
                    sql.append("     and b.delete_date is null");
                    //2016/09/06 GB MOD #54449 End
                     //nhanvt start add 20141201 New request #33406
                    if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                        sql.append("      and   exists");
                        sql.append("             (");

                        //業態利用するかしないかチェック

                        sql.append("                 SELECT");
                        sql.append("                     1");
                        sql.append("                 FROM");
                        sql.append("                     data_sales_detail dsd");
                        sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                        sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                        sql.append("                 WHERE");
                        sql.append("                         dsd.shop_id = a.shop_id");
                        sql.append("                     AND dsd.slip_no = a.slip_no");
                        //sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                        sql.append("                     AND dsd.delete_date is null");
                        sql.append("                     AND dsd.product_division in (1,3)");
                        //業態選択の場合
                        if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                            sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                        }

                        sql.append("                  UNION ALL  ");
                        sql.append("                  SELECT ");
                        sql.append("                     1");
                        sql.append("                 FROM");
                        sql.append("                      data_sales_detail dsd");
                        sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                        sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                        sql.append("                  WHERE");
                        sql.append("                          dsd.shop_id = a.shop_id");
                        sql.append("                      AND dsd.slip_no = a.slip_no");
                        //sql.append("                     and dsd.slip_detail_no = dsv.slip_detail_no   ");
                        sql.append("                      AND dsd.delete_date is null");
                        sql.append("                      AND dsd.product_division in (2,4)");
                        //業態選択の場合
                        if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                            sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                        }

                        sql.append("                 UNION ALL  ");

                        sql.append("                 SELECT ");
                        sql.append("                     1");
                        sql.append("                 FROM");
                        sql.append("                     data_sales_detail dsd");
                        sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                        sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                        sql.append("                 WHERE");
                        sql.append("                         dsd.shop_id = a.shop_id");
                        sql.append("                     AND dsd.slip_no = a.slip_no");
                        //sql.append("                     and dsd.slip_detail_no = dsv.slip_detail_no   ");
                        sql.append("                     AND dsd.delete_date is null");
                        sql.append("                     AND dsd.product_division in (5,6)");
                        //業態選択の場合
                        if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                            sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                        }

                        sql.append(" ) ");
                    }
                    //nhanvt end add 20141201 New request #33406
                    
                } else {
                    
                    //---------------------------------
                    //評価対象　技術または商品
                    //---------------------------------
                    if (getTaxType() == TAX_TYPE_BLANK){
                        //割引後税抜き金額
                        sql.append(" ,sum(discount_detail_value_no_tax) as sales_value");
                    } else {
                        //割引後税込み金額
                        sql.append(" ,sum(discount_detail_value_in_tax) as sales_value");
                    }

                    sql.append(" from");
                    sql.append("     view_data_sales_detail_valid a");
                    sql.append("         join mst_customer b using (customer_id)");
                    sql.append(" where");
                    sql.append("         a.shop_id in (" + getShopIDList() + ")");
                    if (getStaffID() != null) {
                        sql.append(" and a.staff_id = " + getStaffID().intValue());
                    }
                    sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
                    sql.append("     and b.customer_no <> '0'");
                    //2016/09/06 GB MOD #54449 Start
                    sql.append("     and b.delete_date is null");
                    //2016/09/06 GB MOD #54449 End
                    
                    if (getProductDivision() == 1) {
                        //技術・技術クレーム
                        sql.append(" and a.product_division in (1,3)");
                    } else {
                        //商品・商品返品
                        sql.append(" and a.product_division in (2,4)");
                    }
                    
                    //nhanvt start add 20141201 New request #33406
                    if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                        
                        if (getProductDivision() == 1) {
                            sql.append("      and   exists");
                            sql.append("             (");

                            //業態利用するかしないかチェック

                            sql.append("                 SELECT");
                            sql.append("                     1");
                            sql.append("                 FROM");
                            sql.append("                     data_sales_detail dsd");
                            sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                            sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                            sql.append("                 WHERE");
                            sql.append("                         dsd.shop_id = a.shop_id");
                            sql.append("                     AND dsd.slip_no = a.slip_no");
                            sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                            sql.append("                     AND dsd.delete_date is null");
                            sql.append("                     AND dsd.product_division in (1,3)");
                            //業態選択の場合
                            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                            }
                            
                            sql.append(" ) ");

                        }else{
                            sql.append("      and   exists");
                            sql.append("             (");
                            sql.append("                  SELECT ");
                            sql.append("                     1");
                            sql.append("                 FROM");
                            sql.append("                      data_sales_detail dsd");
                            sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                            sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                            sql.append("                  WHERE");
                            sql.append("                          dsd.shop_id = a.shop_id");
                            sql.append("                      AND dsd.slip_no = a.slip_no");
                            sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                            sql.append("                      AND dsd.delete_date is null");
                            sql.append("                      AND dsd.product_division in (2,4)");
                            //業態選択の場合
                            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                            }
                            sql.append(" ) ");
                        }
                    }
                    //nhanvt end add 20141201 New request #33406
                    
                }

                sql.append(" group by");
                sql.append("     a.customer_id");
                
                if (isCrossAnalysis && getProductDivision() == 0) {
                    
                    String visitCycle = "((extract(month from age(" + getTermTo() + "::date, " + getTermFrom() + "::date)) + 1) / count(*))";
                    
                    sql.append(" having");
                    sql.append("     (");
                    sql.append("         false");

                    MstShopSetting mss = MstShopSetting.getInstance();

                    if (isChkValid1()) {
                        sql.append(" or " + visitCycle + " <= " + mss.getValidCustomerPeriod1());
                    }

                    if (isChkValid2()) {
                        sql.append(" or (");
                        sql.append(visitCycle + " > " + mss.getValidCustomerPeriod1() + " and ");
                        sql.append(visitCycle + " <= " + mss.getValidCustomerPeriod2());
                        sql.append(" )");
                    }

                    if (isChkValid3()) {
                        sql.append(" or " + visitCycle + " > " + (mss.getValidCustomerPeriod2()));
                    }
                    sql.append("     )");
                }
                
                sql.append(" order by");
                sql.append("     sales_value desc");
                
                con.execute(sql.toString());
	}
//IVS_nahoang start add 20141029
    //GB_Mashu Bug #31925 [Phase4][Product][Code][Edit]目標詳細設定_ デシル目標設定 phan tien ky nam truoc dang lay data sai + so thu tu 0~2        
    public  final String orderSaleValue = "sales_value";
    public  final String orderVisitNum = "visit_count";
    public boolean dropTableTemp() {
        this.clear();
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.execute("drop table wk_rank");

        } catch (SQLException ex) {
            Logger.getLogger(CustomerRankingList.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void getTargetListByShopCategory(boolean isCrossAnalysis, Integer shopCategoryId, Integer year,
            Integer periodMonth, Integer shopId, String orderByItem) throws SQLException {
        ConnectionWrapper con = SystemInfo.getConnection();

        StringBuilder sql = new StringBuilder(1000);

        sql.append(" create temporary table wk_rank as");
        sql.append(" select");
        sql.append("      a.customer_id");
        sql.append("     ,max(coalesce(b.customer_no, '')) as customer_no");
        sql.append("     ,max(a.sales_date) as sales_date");
        sql.append("     ,count(distinct slip_no) as visit_count");
        sql.append(" ,sum(discount_detail_value_no_tax) as sales_value");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid a");
        sql.append("         join mst_customer b using (customer_id)");
        sql.append(" where");
        sql.append("         a.shop_id in (" + shopId + ")");

        //check period month
        if (periodMonth == 12) {
            sql.append("   and ((date_part('year',a.sales_date) = " + (year - 1) + " ");
        } else {
            sql.append("   and ((date_part('year',a.sales_date) = " + year + " - 1 ");
            //IVS_LVTu start edit 2014/12/03 Mashu_設定画面 change request 期末月 (1月～１２月)
            sql.append("   and date_part('month', a.sales_date) >" + periodMonth + ") ");
            sql.append("   or (date_part('year',a.sales_date) = " + year + " ");
            sql.append("   AND date_part('month', a.sales_date) <= " + periodMonth );
            //IVS_LVTu end edit 2014/12/03 Mashu_設定画面 change request 期末月 (1月～１２月)
        }
        sql.append("   )) ");

        sql.append("     and b.customer_no <> '0'");

        sql.append("  and a.product_division in (1,2)");
        //check shop category
        if (shopCategoryId != 0) {
            sql.append(" AND    exists (");
            sql.append("          (   SELECT 1 ");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("                                                            and dsd.product_division = a.product_division");
            sql.append("                                left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = a.shop_id");
            sql.append("                                AND dsd.slip_no = a.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (1)");
            sql.append("                                 AND mstc.shop_category_id = " + shopCategoryId + "");
            sql.append("                   )");
            sql.append("        union all");
            sql.append("                        (SELECT  1");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2  ");
            sql.append("                                                            and dsd.product_division = a.product_division");
            sql.append("                                left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = a.shop_id");
            sql.append("                                AND dsd.slip_no = a.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (2)");
            sql.append("                                 AND msic.shop_category_id =" + shopCategoryId + "");
            sql.append(" )");
            sql.append("  )  ");
        }
        //end check category.
        sql.append(" group by");
        sql.append("     a.customer_id");

        sql.append(" order by");
        sql.append("  " + orderByItem + "    desc");
        try {
            con.execute(sql.toString());
        } catch (Exception ex) {
        }
    }

    public CustomerRankingList getCustomerRankingListByCategory() throws SQLException {
        this.clear();

        ConnectionWrapper con = SystemInfo.getConnection();
        ResultSetWrapper rs = null;

        //ワークテーブルの件数取得
        long totalCount = 0;
        rs = con.executeQuery("select count(*) as cnt from wk_rank");
        while (rs.next()) {
            totalCount = rs.getLong("cnt");
        }

        if (totalCount == 0) {
            return null;
        }

        BigDecimal bd = null;
        bd = new BigDecimal(totalCount / 10d);
        this.setLimitCount(bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

        try {
            rs = con.executeQuery(this.getSelectCustomerRankingSQL(getLimitCount()));
        } catch (Exception ex) {
            Logger.getLogger(CustomerRankingList.class.getName()).log(Level.SEVERE, null, ex);
        }

        double salesTotal = 0d;
        double subTotal = 0d;

        while (rs.next()) {
            CustomerRanking temp = new CustomerRanking();
            temp.setData(rs);

            salesTotal += rs.getLong("sales_value");

            this.add(temp);
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);

        for (CustomerRanking temp : this) {

            if (salesTotal > 0) {
                // 売上シェア率
                bd = new BigDecimal(temp.getSalesValue() / salesTotal * 100);
                temp.setSalesShareRate(nf.format(bd.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()) );
            }
        }

        rs.close();
        return this;
    }
    // GB_Mashu IVS_nahoang end add 20141030
        	
	/**
	 * スタッフランキングのリストを取得する。
	 * @exception Exception
	 */
	private void getCustomerRankingList() throws Exception
	{
		this.clear();

                ConnectionWrapper con = SystemInfo.getConnection();
		ResultSetWrapper rs = null;
                
                //ワークテーブルの件数取得
                long totalCount = 0;
		rs = con.executeQuery("select count(*) as cnt from wk_rank");
		while (rs.next()) {
                    totalCount = rs.getLong("cnt");
		}
                
                if (totalCount == 0) return;

                BigDecimal bd = null;
                bd = new BigDecimal(totalCount / 10d);
                this.setLimitCount(bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());
                
		rs = con.executeQuery(this.getSelectCustomerRankingSQL(getLimitCount()));

                double salesTotal = 0d;
                double subTotal = 0d;
                
		while (rs.next()) {
                    CustomerRanking temp = new CustomerRanking();
                    temp.setData(rs);

                    salesTotal += rs.getLong("sales_value");

                    this.add(temp);
		}

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(1);
                
                for(CustomerRanking temp : this) {

                    if (temp.getTargetCount().doubleValue() > 0) {
                        // 客単価
                        bd = new BigDecimal(temp.getSalesValue().doubleValue() / temp.getTargetCount().doubleValue());
                        temp.setAvgUnitPrice(bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

                        // 平均来店回数
                        bd = new BigDecimal(temp.getVisitCount().doubleValue() / temp.getTargetCount().doubleValue());
                        temp.setAvgVisitCount(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    
                    if (temp.getAvgVisitCount().doubleValue() > 0) {
                        // 平均来店単価
                        bd = new BigDecimal(temp.getAvgUnitPrice().doubleValue() / temp.getAvgVisitCount().doubleValue());
                        temp.setAvgVisitPrice(bd.setScale(1, BigDecimal.ROUND_HALF_UP).longValue());
                    }

                    if (salesTotal > 0) {
                        // 売上シェア率
                        bd = new BigDecimal(temp.getSalesValue() / salesTotal * 100);
                        temp.setSalesShareRate(nf.format(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                        // 累計売上シェア率
                        subTotal += temp.getSalesValue();
                        bd = new BigDecimal(subTotal / salesTotal * 100);
                        temp.setTotalSalesShareRate(nf.format(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    }
                }
                
		rs.close();
	}
	
	/**
	 * スタッフランキング抽出用SQLを取得する。
	 * @return スタッフランキング抽出用SQL
	 * @exception Exception
	 */
	private String getSelectCustomerRankingSQL(long limitCount) throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
            
            for (int i = 0; i < 10; i++) {

                long offset = 0;
                if (i > 0) {
                    offset = (i * limitCount);
                }
                sql.append(" select");
                sql.append("      count(customer_id) as customer_count");
                sql.append("     ,sum(visit_count) as visit_count");
                sql.append("     ,sum(sales_value) as sales_value");
                sql.append(" from");
                sql.append("     (");
                sql.append("         select * from wk_rank");
                sql.append("         offset " + offset);
                if (i < 9) {
                    sql.append("     limit " + limitCount);
                }
                sql.append("     ) t");
                if (i < 9) {
                    sql.append(" union all");
                }
            }

            return sql.toString();
	}

	/**
	 * クロス分析のリストを取得する。
	 * @exception Exception
	 */
	private void getCrossAnalysisList() throws Exception
	{
            this.clear();

            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = null;

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      sum(case when visit_count >= " + getRankF3() + " and sales_value >= " + getRankM3() + " then 1 else 0 end) as f3m3");
            sql.append("     ,sum(case when visit_count >= " + getRankF3() + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f3m2");
            sql.append("     ,sum(case when visit_count >= " + getRankF3() + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f3m1");
            sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value >= " + getRankM3() + " then 1 else 0 end) as f2m3");
            sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f2m2");
            sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f2m1");
            sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value >= " + getRankM3() + " then 1 else 0 end) as f1m3");
            sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f1m2");
            sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f1m1");
            sql.append(" from");
            sql.append("     wk_rank");

            rs = con.executeQuery(sql.toString());
            if (rs.next()) {

                CustomerRanking temp = null;
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m3"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m2"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m1"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m3"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m2"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m1"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m3"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m2"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m1"));
                this.add(temp);
            }

            rs.close();
	}
        
    public long getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(long limitCount) {
        this.limitCount = limitCount;
    }

    public long getRankF3() {
        return rankF3;
    }

    public void setRankF3(long rankF3) {
        this.rankF3 = rankF3;
    }

    public long getRankF2() {
        return rankF2;
    }

    public void setRankF2(long rankF2) {
        this.rankF2 = rankF2;
    }

    public long getRankF1() {
        return rankF1;
    }

    public void setRankF1(long rankF1) {
        this.rankF1 = rankF1;
    }

    public long getRankM3() {
        return rankM3;
    }

    public void setRankM3(long rankM3) {
        this.rankM3 = rankM3;
    }

    public long getRankM2() {
        return rankM2;
    }

    public void setRankM2(long rankM2) {
        this.rankM2 = rankM2;
    }

    public long getRankM1() {
        return rankM1;
    }

    public void setRankM1(long rankM1) {
        this.rankM1 = rankM1;
    }

    public void setPastTotal(boolean isPastTotal) {
        this.isPastTotal = isPastTotal;
    }

    public boolean isChkValid1() {
        return chkValid1;
    }

    public void setChkValid1(boolean chkValid1) {
        this.chkValid1 = chkValid1;
    }

    public boolean isChkValid2() {
        return chkValid2;
    }

    public void setChkValid2(boolean chkValid2) {
        this.chkValid2 = chkValid2;
    }

    public boolean isChkValid3() {
        return chkValid3;
    }

    public void setChkValid3(boolean chkValid3) {
        this.chkValid3 = chkValid3;
    }
}

/*
 * ProductRankingList.java
 *
 * Created on 2008/07/20, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.util.*;
import java.util.logging.*;
import java.math.BigDecimal;
import java.text.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 * スタッフランキング一覧処理
 * @author saito
 */
public class ProductRankingList extends ArrayList<ProductRanking>
{
        //税区分
        private final	int	TAX_TYPE_BLANK		=   1;	//税抜
	private final	int	TAX_TYPE_UNIT		=   2;	//税込

	/**
	 * 技術・商品タブインデックス
	 */
	private	Integer	selectedTabIndex	=   null;
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
	/**
	 * 税区分
	 */
	private	Integer	taxType                 =   null;
	/**
	 * 分類ID
	 */
	private	Integer	prodClassID             =   null;
	/**
	 * 表示順
	 */
	private	Integer	orderDisplay            =   null;
	
        //nhanvt start add 20141216 New request #33406
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
	//nhanvt end add 20141216 New request #33406
        
	/**
         * Creates a new instance of ProductRankingList
         */
	public ProductRankingList()
	{
	}
	
	/**
	 * 選択されたタブインデックス取得する。
	 * @return 選択されたタブインデックス
	 */
        public int getSelectedTabIndex() {
                return selectedTabIndex;
        }
	/**
         * 選択されたタブインデックスをセットする。
         * @param selectedTabIndex 選択されたタブインデックス
         */
        public void setSelectedTabIndex(int selectedTabIndex) {
                this.selectedTabIndex = selectedTabIndex;
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
	 * 分類IDを取得する。
	 * @return 分類ID
	 */
        public Integer getProdClassID() {
                return prodClassID;
        }
	/**
         * 分類IDをセットする。
         * @param classID 分類ID
         */
        public void setProdClassID(Integer prodClassID) {
                this.prodClassID = prodClassID;
        }
	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
        public int getOrderDisplay() {
                return orderDisplay;
        }
	/**
         * 表示順をセットする。
         * @param orderDisplay 表示順
         */
        public void setOrderDisplay(int orderDisplay) {
                this.orderDisplay = orderDisplay;
        }

    /**
     * データを読み込む。
     */
	public void load()
	{
		try
		{
                    //ランキングリスト取得
                    this.getProductRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * スタッフランキングのリストを取得する。
	 * @exception Exception
	 */
	private void getProductRankingList() throws Exception
	{
		this.clear();
		
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectProductRankingSQL());

                double countTotal = 0;
                double salesTotal = 0;
                
		while(rs.next())
		{
			ProductRanking temp = new ProductRanking();
			temp.setData(rs);
                        
                        countTotal += rs.getLong("product_num");
                        salesTotal += rs.getLong("sales_value");
                        
			this.add(temp);
		}

                BigDecimal bd = null;
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);
                
                for(ProductRanking temp : this) {
                    
                    // 比率（数量）
                    if (countTotal == 0) {
                        bd = new BigDecimal(0);
                    } else {
                        bd = new BigDecimal(temp.getProdCount() / countTotal * 100);
                    }
                    temp.setCountRatio(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    
                    // 比率（売上）
                    if (salesTotal == 0) {
                        bd = new BigDecimal(0);
                    } else {
                        bd = new BigDecimal(temp.getProdSales() / salesTotal * 100);
                    }
                    temp.setSalesRatio(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                }
                
		rs.close();
	}
	
	/**
	 * スタッフランキング抽出用SQLを取得する。
	 * @return スタッフランキング抽出用SQL
	 * @exception Exception
	 */
	private String getSelectProductRankingSQL() throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.product_id");
            sql.append("     ,max(b.product_name)    as product_name");
            sql.append("     ,max(b.class_name)      as class_name");
            sql.append("     ,max(b.unit_price)      as unit_price");
            sql.append("     ,sum(a.product_num)     as product_num");
            
            if (getTaxType() == TAX_TYPE_BLANK){
                // 割引後税抜き金額
                sql.append(" ,sum(a.discount_detail_value_no_tax) as sales_value");
                // 税抜き割引金額
                sql.append(" ,sum(a.detail_value_no_tax - a.discount_detail_value_no_tax) as discount_value");
            } else {
                // 割引後税込み金額
                sql.append(" ,sum(a.discount_detail_value_in_tax) as sales_value");
                // 税込み割引金額
                sql.append(" ,sum(a.detail_value_in_tax - a.discount_detail_value_in_tax) as discount_value");
            }
            
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append("         join");
            sql.append("             (");
            
                if (this.getSelectedTabIndex() == 3){
                    //--------------------
                    // 技術
                    //--------------------
                    sql.append(" select");
                    sql.append("      a.technic_id          as product_id");
                    sql.append("     ,a.technic_name        as product_name");
                    sql.append("     ,b.technic_class_name  as class_name");

                    if (getTaxType() == TAX_TYPE_BLANK){
                        // 税抜
                        sql.append(" ,sign(a.price / (1.0 + get_tax_rate(current_date))) * ceil(abs(a.price / (1.0 + get_tax_rate(current_date)))) as unit_price");
                    } else {
                        // 税込
                        sql.append(" ,a.price        as unit_price");
                    }

                    sql.append("     ,1              as product_division");
                    sql.append(" from");
                    sql.append("     mst_technic a");
                    sql.append("         join mst_technic_class b");
                    sql.append("             using(technic_class_id)");
                    sql.append(" where true");
                    if (getProdClassID() != null) {
                        sql.append(" and a.technic_class_id = " + getProdClassID());
                    }

                } else if (this.getSelectedTabIndex() == 4) {
                    //--------------------
                    // 商品
                    //--------------------
                    sql.append(" select");
                    sql.append("      a.item_id          as product_id");
                    sql.append("     ,a.item_name        as product_name");
                    sql.append("     ,b.item_class_name  as class_name");
                    
                    if (getTaxType() == TAX_TYPE_BLANK){
                        // 税抜
                        sql.append(" ,sign(a.price / (1.0 + get_tax_rate(current_date))) * ceil(abs(a.price / (1.0 + get_tax_rate(current_date)))) as unit_price");
                    } else {
                        // 税込
                        sql.append(" ,a.price        as unit_price");
                    }
                    
                    sql.append("     ,2              as product_division");
                    sql.append(" from");
                    sql.append("     mst_item a");
                    sql.append("         join mst_item_class b");
                    sql.append("             using(item_class_id)");
                    sql.append(" where true");
                    if (getProdClassID() != null) {
                        sql.append(" and a.item_class_id = " + getProdClassID());
                    }
                }
                //Thanh start add 2013/03/18
                else if (this.getSelectedTabIndex() == 5) {
                    //--------------------
                    // 商品
                    //--------------------
                    sql.append(" select");
                    sql.append("      a.course_id          as product_id");
                    sql.append("     ,a.course_name        as product_name");
                    sql.append("     ,b.course_class_name  as class_name");
                    
                    if (getTaxType() == TAX_TYPE_BLANK){
                        // 税抜
                        sql.append(" ,sign(a.price / (1.0 + get_tax_rate(current_date))) * ceil(abs(a.price / (1.0 + get_tax_rate(current_date)))) as unit_price");
                    } else {
                        // 税込
                        sql.append(" ,a.price        as unit_price");
                    }
                    
                    sql.append("     ,5              as product_division");
                    sql.append(" from");
                    sql.append("     mst_course a");
                    sql.append("         join mst_course_class b");
                    sql.append("             using(course_class_id)");
                    sql.append(" where true");
                    if (getProdClassID() != null) {
                        sql.append(" and a.course_class_id = " + getProdClassID());
                    }
                }
             //Thanh end add 2013/03/18   
            sql.append("             ) b");
            sql.append("             using (product_id, product_division)");
            sql.append(" where");
            sql.append("         a.shop_id in (" + getShopIDList() + ")");
            if (getStaffID() != null) {
                sql.append(" and a.staff_id = " + getStaffID().intValue());
            }
            sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
            //nhanvt start add 20141216 New request #33406
            if(this.getUseShopCategory() != null &&  this.getUseShopCategory() == 1){
                
                if (this.getSelectedTabIndex() == 3){
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
                    sql.append("                     AND dsd.product_division in (1)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                   
                    sql.append(" ) ");
                } else if (this.getSelectedTabIndex() == 4) {
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
                    sql.append("                      AND dsd.product_division in (2)");
                    //業態選択の場合
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                
                
            }
            //nhanvt end add 20141216 New request #33406
            sql.append(" group by");
            sql.append("     a.product_id");
            
            sql.append(" order by");
            
            //表示順
            if (getOrderDisplay() == 0) {
                sql.append(" product_num desc");
            } else if (getOrderDisplay() == 1) {
                sql.append(" sales_value desc");
            }else {
                sql.append(" sales_value desc");
            }

            return sql.toString();
	}
}

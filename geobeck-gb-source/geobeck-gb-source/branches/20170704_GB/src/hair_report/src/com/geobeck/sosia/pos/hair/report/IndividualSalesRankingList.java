/*
 * IndividualSalesRankingList.java
 *
 * Created on 2013/01/23, 11:00
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
 * 店舗ランキング一覧処理
 * @author IVS_tttung
 */
public class IndividualSalesRankingList extends ArrayList<IndividualSalesRanKing>
{
        //税区分
        private final	int	TAX_TYPE_BLANK				=	1;      //税抜
	private final	int	TAX_TYPE_UNIT				=	2;      //税込
        //表示範囲
	private final	int	RANGE_DISPLAY_TO_10                     =	0;	//～10位
	private final	int	RANGE_DISPLAY_TO_20                     =	1;	//～20位
	private final	int	RANGE_DISPLAY_ALL                       =	2;	//全て

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
	private	Integer	taxType          =	null;
	/**
	 * 表示範囲
	 */
	private	Integer	rangeDisplay    =	null;
	/**
	 * 表示順
	 */
	private	Integer	orderDisplay      =	null;
        /**
	 * 担当者ID
	 */
	private	Integer	staffID                 =   null;
	
	/**
         * Creates a new instance of IndividualSalesRankingList
         */
	public IndividualSalesRankingList()
	{
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
	 * 表示範囲を取得する。
	 * @return 表示範囲
	 */
        public int getRangeDisplay() {
                return rangeDisplay;
        }
	/**
	 * 表示範囲をセットする。
	 * @param rangeDisplay 表示範囲
	 */
        public void setRangeDisplay(int rangeDisplay) {
                this.rangeDisplay = rangeDisplay;
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
     * データを読み込む。
     */
	public void load()
	{
		try
		{
                    //ランキングリスト取得
                    this.getIndividualSalesRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 店舗ランキングのリストを取得する。
	 * @exception Exception
	 */
	private void getIndividualSalesRankingList() throws Exception
	{
		this.clear();
		
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectIndividualSalesRankingSQL());

		while(rs.next())
		{
                    IndividualSalesRanKing temp = new IndividualSalesRanKing();
                    temp.setData(rs);
                    this.add(temp);
		}              
                // 技術新規比率
                for(IndividualSalesRanKing temp : this) {
                    temp.setTotalSales(temp.getTechSum() + temp.getNominaSum() + temp.getItemPrice() + temp.getClaimSum() + temp.getMisum() + temp.getMcsum() + temp.getConsumpsum());
                }
		rs.close();
	}
	
	/**
	 * 店舗ランキング抽出用SQLを取得する。
	 * @return 店舗ランキング抽出用SQL
	 * @exception Exception
	 */
	private String getSelectIndividualSalesRankingSQL() throws Exception
	{   
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number gross_margin_rate = null;
            Number spread_margin_rate = null;
            
            ConnectionWrapper con = SystemInfo.getConnection();
            String SQL = " select gross_margin_rate from mst_account_setting ";
            ResultSetWrapper rs = con.executeQuery(SQL);
            while (rs.next()) {
                gross_margin_rate = format.parse(rs.getString(1));///
            }
            
            SQL = " select spread_margin_rate from mst_account_setting ";
            rs = con.executeQuery(SQL);
            while (rs.next()) {
                spread_margin_rate = format.parse(rs.getString(1));///
            }
            
           StringBuilder sql = new StringBuilder(1000);
            sql.append("     select");
            sql.append("     	 b.staff_id");
            sql.append("     	,max(case when b.staff_id is null then '担当なし' else b.staff_name1 || '　' || b.staff_name2 end) as StaffName ");
            if (getTaxType() == TAX_TYPE_BLANK) {
                // 税抜き
                sql.append("     		 ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = FALSE then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Hodocojuts_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = FALSE then a.slip_no end) as Hodocojuts_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = TRUE then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Nomination_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = TRUE then a.slip_no end) as Nomination_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_no_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as sum_item_value ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as sum_item_value ");
                sql.append("     		 ,count(case when a.product_division = 2 then discount_detail_value_no_tax else 0 end) as count_item_value ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 3 then discount_detail_value_no_tax * ( " + gross_margin_rate + " )  else 0 end)) as ClaimValue  ");
                sql.append("     		 ,count (distinct case when a.product_division = 3 then a.slip_no else null end) as ClaimCount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as Misum ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Misum ");
                sql.append("     		 ,count (distinct case when a.product_division = 4 then a.slip_no else null end) as Micount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 5 then discount_detail_value_no_tax * ( " + spread_margin_rate + " ) else 0 end)) as Mcsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 5 then a.slip_no else null end) as Mccount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 6 then discount_detail_value_no_tax * ( 1 - ( " + spread_margin_rate + " )) else 0 end)) as Consumpsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 6 then a.slip_no else null end) as Consumpcount ");

            } else {
                // 税込み
                sql.append("     		 ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = FALSE then discount_detail_value_in_tax * ( " + gross_margin_rate + " ) else 0 end)) as Hodocojuts_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = FALSE then a.slip_no end) as Hodocojuts_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = TRUE then discount_detail_value_in_tax * ( " + gross_margin_rate + " ) else 0 end)) as Nomination_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = TRUE then a.slip_no end) as Nomination_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_in_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as sum_item_value ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_in_tax * ( " + gross_margin_rate + " ) else 0 end)) as sum_item_value ");
                sql.append("     		 ,count(case when a.product_division = 2 then discount_detail_value_no_tax else 0 end) as count_item_value ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 3 then discount_detail_value_in_tax * ( " + gross_margin_rate + " )  else 0 end)) as ClaimValue  ");
                sql.append("     		 ,count (distinct case when a.product_division = 3 then a.slip_no else null end) as ClaimCount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as Misum ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Misum ");
                sql.append("     		 ,count (distinct case when a.product_division = 4 then a.slip_no else null end) as Micount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 5 then discount_detail_value_in_tax * ( " + spread_margin_rate + " ) else 0 end)) as Mcsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 5 then a.slip_no else null end) as Mccount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 6 then discount_detail_value_in_tax * ( 1 - ( " + spread_margin_rate + " )) else 0 end)) as Consumpsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 6 then a.slip_no else null end) as Consumpcount ");
            }
            sql.append("         from");
            sql.append("             view_data_sales_detail_valid a");
            sql.append("             join mst_staff b ");
            sql.append("             on a.detail_staff_id = b.staff_id ");
            sql.append("     ");
            sql.append("     ");
            sql.append("         where");
            //sql.append("             a.shop_id in (0, 1, 2)   and");
            sql.append("             a.sales_date between " + getTermFrom() + " and " + getTermTo());
            if (getStaffID() != null) {
                sql.append(" and b.staff_id = " + getStaffID().intValue());
            }
            sql.append("         group by");
            sql.append("              b.staff_id");
            sql.append("         order by");
            switch (getOrderDisplay()) {
                case 0:
                    sql.append("  Hodocojuts_value desc ");
                    break;
                case 1:
                    sql.append("  Hodocojuts_Num desc ");
                    break;
                case 2:
                    sql.append("  Nomination_value desc ");
                    break;
                case 3:
                    sql.append("  Nomination_Num desc ");
                    break;
                case 4:
                    sql.append("  sum_item_value desc ");
                    break;
                case 5:
                    sql.append("  count_item_value desc ");
                    break;
                case 6:
                    sql.append("  ClaimValue desc ");
                    break;
                case 7:
                    sql.append("  ClaimCount desc ");
                    break;
                case 8:
                    sql.append("  Misum desc ");
                    break;
                case 9:
                    sql.append("  Micount desc ");
                    break;
                case 10:
                    sql.append("  Mcsum desc ");
                    break;
                case 11:
                    sql.append("  Mccount desc ");
                    break;
                case 12:
                    sql.append("  Consumpsum desc ");
                    break;
                case 13:
                    sql.append("  Consumpcount desc ");
                    break;
                default:
                    sql.append("  Hodocojuts_value desc");
                    break;
            }
            sql.append("             ,max(case when b.display_seq is null then 1 else 0 end) ");
            sql.append("             ,max(b.display_seq) ");
            sql.append("             ,max(lpad(b.staff_no, 10, '0')) ");
            sql.append("             ,b.staff_id desc");

            //表示範囲
            if (getRangeDisplay() == RANGE_DISPLAY_TO_10) {
                //～10位
                sql.append(" limit 10");
            } else if (getRangeDisplay() == RANGE_DISPLAY_TO_20) {
                //～20位
                sql.append(" limit 20");
            }
            return sql.toString();
	}
}

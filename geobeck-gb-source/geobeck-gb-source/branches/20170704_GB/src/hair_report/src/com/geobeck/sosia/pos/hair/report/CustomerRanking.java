/*
 * CustomerRanking.java
 *
 * Created on 2008/07/20, 16:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.sql.*;

import com.geobeck.sql.*;

/**
 * スタッフランキングデータ
 * @author saito
 */
public class CustomerRanking
{
        /**
         * 顧客数
         */
        protected   Long        targetCount = 0l;
        /**
         * 来店回数
         */
        protected   Long        visitCount = 0l;
        /**
         * 売上金額
         */
        protected   Long        salesValue = 0l;
        /**
         * 売上シェア率
         */
        protected   String      salesShareRate = "";
        /**
         * 累計売上シェア率
         */
        protected   String      totalSalesShareRate = "";
        /**
         * 客単価
         */
        protected   Long        avgUnitPrice = 0l;
        /**
         * 平均来店回数
         */
        protected   Double      avgVisitCount = 0d;
        /**
         * 平均来店単価
         */
        protected   Long        avgVisitPrice = 0l;
        /**
         * 詳細
         */
        protected   Object      moreInfo = null;

	/**
        * Creates a new instance of CustomerRanking
        */
	public CustomerRanking()
	{
	}
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            setTargetCount(rs.getLong("customer_count"));
            setVisitCount(rs.getLong("visit_count"));
            setSalesValue(rs.getLong("sales_value"));
	}

        /**
         * 顧客数を取得する。
         * @return 顧客数
         */
        public Long getTargetCount() {
            return targetCount;
        }

        /**
         * 顧客数をセットする。
         * @param targetCount 顧客数
         */
        public void setTargetCount(Long targetCount) {
            this.targetCount = targetCount;
        }

        /**
         * 来店回数を取得する。
         * @return 来店回数
         */
        public Long getVisitCount() {
            return visitCount;
        }

        /**
         * 来店回数をセットする。
         * @param visitCount 来店回数
         */
        public void setVisitCount(Long visitCount) {
            this.visitCount = visitCount;
        }

        /**
         * 売上金額を取得する。
         * @return 売上金額
         */
        public Long getSalesValue() {
            return salesValue;
        }

        /**
         * 売上金額をセットする。
         * @param salesValue 売上金額
         */
        public void setSalesValue(Long salesValue) {
            this.salesValue = salesValue;
        }

        /**
         * 売上シェア率を取得する。
         * @return 売上シェア率
         */
        public String getSalesShareRate() {
            return salesShareRate;
        }

        /**
         * 売上シェア率をセットする。
         * @param salesShareRate 売上シェア率
         */
        public void setSalesShareRate(String salesShareRate) {
            this.salesShareRate = salesShareRate;
        }

        /**
         * 累計売上シェア率を取得する。
         * @return 累計売上シェア率
         */
        public String getTotalSalesShareRate() {
            return totalSalesShareRate;
        }

        /**
         * 累計売上シェア率をセットする。
         * @param totalSalesShareRate 累計売上シェア率
         */
        public void setTotalSalesShareRate(String totalSalesShareRate) {
            this.totalSalesShareRate = totalSalesShareRate;
        }

        /**
         * 客単価を取得する。
         * @return 客単価
         */
        public Long getAvgUnitPrice() {
            return avgUnitPrice;
        }

        /**
         * 客単価をセットする。
         * @param avgUnitPrice 客単価
         */
        public void setAvgUnitPrice(Long avgUnitPrice) {
            this.avgUnitPrice = avgUnitPrice;
        }

        /**
         * 平均来店回数を取得する。
         * @return 平均来店回数
         */
        public Double getAvgVisitCount() {
            return avgVisitCount;
        }

        /**
         * 平均来店回数をセットする。
         * @param avgVisitCount 平均来店回数
         */
        public void setAvgVisitCount(Double avgVisitCount) {
            this.avgVisitCount = avgVisitCount;
        }

        /**
         * 平均来店単価を取得する。
         * @return 平均来店単価
         */
        public Long getAvgVisitPrice() {
            return avgVisitPrice;
        }

        /**
         * 平均来店単価をセットする。
         * @param avgVisitPrice 平均来店単価
         */
        public void setAvgVisitPrice(Long avgVisitPrice) {
            this.avgVisitPrice = avgVisitPrice;
        }

        /**
         * 詳細を取得する。
         * @return 詳細
         */
        public Object getMoreInfo() {
            return moreInfo;
        }

        /**
         * 詳細をセットする。
         * @param moreInfo 詳細
         */
        public void setMoreInfo(Object moreInfo) {
            this.moreInfo = moreInfo;
        }
}

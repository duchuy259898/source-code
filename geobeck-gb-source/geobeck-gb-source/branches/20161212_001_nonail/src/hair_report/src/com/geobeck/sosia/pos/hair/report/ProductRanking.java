/*
 * ProductRanking.java
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
public class ProductRanking
{
	/**
	 * 技術分類名・商品分類名
	 */
	protected	String			className        = "";
	/**
	 * 技術名・商品名
	 */
	protected	String			prodName        = "";
        /**
	 * 単価
	 */
	protected	Long			unitPrice       = 0l;
        /**
	 * 数量
	 */
	protected	Long			prodCount       = 0l;
        /**
	 * 売上合計
	 */
	protected	Long			prodSales       = 0l;
        /**
	 * 割引金額
	 */
	protected	Long			prodDiscount    = 0l;
        /**
	 * 比率(数量)
	 */
	protected	String			salesRatio      = "";
        /**
	 * 比率(売上)
	 */
	protected	String			countRatio      = "";

	/**
        * Creates a new instance of ProductRanking
        */
	public ProductRanking()
	{
	}
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            setClassName(rs.getString("class_name"));
            setProdName(rs.getString("product_name"));
            setUnitPrice(rs.getLong("unit_price"));
            setProdCount(rs.getLong("product_num"));
            setProdSales(rs.getLong("sales_value"));
            setProdDiscount(rs.getLong("discount_value"));
	}

        /**
         * 技術分類名を取得する。
         * @return 技術分類名
         */
        public String getClassName()
        {
        	return className;
        }

        /**
         * 技術分類名をセットする。
         * @param className 技術分類名
         */
        public void setClassName(String className)
        {
        	this.className = className;
        }

        /**
         * 技術名を取得する。
         * @return 技術名
         */
        public String getProdName()
        {
        	return prodName;
        }

        /**
         * 技術名をセットする。
         * @param prodName 技術名
         */
        public void setProdName(String prodName)
        {
        	this.prodName = prodName;
        }

        /**
        * 単価を取得する。
        * @return 単価
        */
        public Long getUnitPrice()
        {
            return unitPrice;
        }

        /**
        * 単価をセットする。
        * @param unitPrice 単価
        */
        public void setUnitPrice(Long unitPrice)
        {
            this.unitPrice = unitPrice;
        }
        
        /**
        * 数量を取得する。
        * @return 数量
        */
        public Long getProdCount()
        {
            return prodCount;
        }

        /**
        * 数量をセットする。
        * @param prodCount 数量
        */
        public void setProdCount(Long prodCount)
        {
            this.prodCount = prodCount;
        }
        
        /**
        * 売上合計を取得する。
        * @return 売上合計
        */
        public Long getProdSales()
        {
            return prodSales;
        }

        /**
        * 売上合計をセットする。
        * @param prodSales 売上合計
        */
        public void setProdSales(Long prodSales)
        {
            this.prodSales = prodSales;
        }

        /**
        * 割引金額を取得する。
        * @return 割引金額
        */
        public Long getProdDiscount()
        {
            return prodDiscount;
        }

        /**
        * 割引金額をセットする。
        * @param prodDiscount 割引金額
        */
        public void setProdDiscount(Long prodDiscount)
        {
            this.prodDiscount = prodDiscount;
        }
        
        /**
         * 比率(数量)を取得する。
         * @return 比率(数量)
         */
        public String getSalesRatio()
        {
        	return salesRatio;
        }

        /**
         * 比率(数量)をセットする。
         * @param salesRatio 比率(数量)
         */
        public void setSalesRatio(String salesRatio)
        {
        	this.salesRatio = salesRatio;
        }
        
        /**
         * 比率(売上)を取得する。
         * @return 比率(売上)
         */
        public String getCountRatio()
        {
        	return countRatio;
        }

        /**
         * 比率(売上)をセットする。
         * @param countRatio 比率(売上)
         */
        public void setCountRatio(String countRatio)
        {
        	this.countRatio = countRatio;
        }
}

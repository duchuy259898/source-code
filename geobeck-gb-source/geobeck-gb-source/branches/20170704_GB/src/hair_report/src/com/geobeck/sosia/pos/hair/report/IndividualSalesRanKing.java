/*
 * IndividualSalesRanKing.java
 *
 * Created on 2013/01/23, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.sql.*;

import com.geobeck.sql.*;

/**
 * 店舗ランキングデータ
 * @author IVS_tttung
 */
public class IndividualSalesRanKing
{
	/**
	 * スタッフ名
	 */
	protected	String	    staff_name		=   "";
	/**
	 * 技術粗利
	 */
	private	Long	    tech_sum		=   0l;
	/**
	 * 技術客数
	 */
	private	Long	    tech_count	=   0l;
	/**
	 * 指名粗利
	 */
	private	Long	    nomina_sum	=   0l;
	/**
	 * 指名数
	 */
	private	Long	    nomina_count		=   0l;        
        /**
	 * 店販粗利
	 */
        private	Long	    item_price		=   0l;   
        
        private	Long	    count_item_price		=   0l; 
	/**
	 * ｸﾚｰﾑ数
	 */
	protected Long	    claim_sum		=   0l;
	/**
	 * ｸﾚｰﾑ減益額
	 */
	protected Long	    claim_count		=   0l;
        
        
        private Long       Misum              =   0l;
        private Long       Micount            =   0l;
        private Long       Mcsum              =   0l;
        private Long       Mccount            =   0l;
        private Long       Consumpsum         =   0l;
        private Long       Consumpcount       =   0l;
	/**
	 * 売上合計
	 */
	private Long	    totalSales		=   0l;
        /**
	 * 技術新規比率
	 */
	//protected String    newCustomerCountRatio = "";
        
	/**
        * Creates a new instance of IndividualSalesRanKing
        */
	public IndividualSalesRanKing()
	{
	}
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		setStaffName(rs.getString("StaffName"));
		setTechSum(rs.getLong("Hodocojuts_value"));
		setTechniCount(rs.getLong("Hodocojuts_Num"));
		setNominaSum(rs.getLong("Nomination_value"));
		setNominaCount(rs.getLong("Nomination_Num"));
                setItemPrice(rs.getLong("sum_item_value"));
                setCount_item_price(rs.getLong("count_item_value"));
		setClaimSum(rs.getLong("ClaimValue"));
		setClaimCount(rs.getLong("ClaimCount"));                
                setMisum(rs.getLong("Misum"));
                setMicount(rs.getLong("Micount"));
                setMcsum(rs.getLong("Mcsum"));
                setMccount(rs.getLong("Mccount"));
                setConsumpsum(rs.getLong("Consumpsum"));
                setConsumpcount(rs.getLong("Consumpcount"));
	}

	/**
	 * 店舗名を取得する。
	 * @return 店舗名
	 */
	public String getStaffName()
	{
		return staff_name;
	}

	/**
	 * 店舗名をセットする。
	 * @param shopName 店舗名
	 */
	public void setStaffName(String staffName)
	{
		this.staff_name = staffName;
	}

	
	/**
	 * 総客数を取得する。
	 * @return 総客数
	 */
	public Long getTechSum() {
		return tech_sum;
	}

	/**
	 * 総客数をセットする。
	 * @param totalCount
	 */
	public void setTechSum(Long techsum) {
		this.tech_sum = techsum;
	}

	/**
	 * 会員数を取得する。
	 * @return 会員数
	 */
	public Long getTechCount() {
		return tech_count;
	}

	/**
	 * 会員数をセットする。
	 * @param customerCount
	 */
	public void setTechniCount(Long tech_count) {
		this.tech_count = tech_count;
	}

	/**
	 * 新規客数を取得する。
	 * @return 新規客数
	 */
	public Long getNominaSum() {
		return nomina_sum;
	}

	/**
	 * 新規客数をセットする。
	 * @param newCustomerCount
	 */
	public void setNominaSum(Long newCustomerCount) {
		this.nomina_sum = newCustomerCount;
	}

	/**
	 * 技術客数を取得する。
	 * @return 技術客数
	 */
	public Long getNominaCount() {
		return nomina_count;
	}

	/**
	 * 技術客数をセットする。
	 * @param techCount
	 */
	public void setNominaCount(Long nomina_count) {
		this.nomina_count = nomina_count;
	}
	
	/**
	 * 技術売上を取得する。
	 * @return 技術売上
	 */
	public Long getClaimSum()
	{
		return claim_sum;
	}

	/**
	 * 技術売上をセットする。
	 * @param techSales 技術売上
	 */
	public void setClaimSum(Long claim_sum)
	{
		this.claim_sum = claim_sum;
	}
	
	/**
	 * 技術客単価を取得する。
	 * @return 技術客単価
	 */
	public Long getTechUnitValue()
	{
	    if (getNominaCount() > 0)
	    {
		return getClaimSum() / getNominaCount();
	    }
	    else
	    {
		return 0l;
	    }
	}
        /**
	 * 客単価を取得する。
	 * @return 客単価
	 */
	public Long getUnitValue()
	{
	    if (getTechSum() > 0)
	    {
		return getTotalSales() / getTechSum();
	    }
	    else
	    {
		return 0l;
	    }
	}
	
	/**
	 * 商品売上を取得する。
	 * @return 商品売上
	 */
	public Long getClaimCount()
	{
		return claim_count;
	}

	/**
	 * 商品売上をセットする。
	 * @param itemSales 商品売上
	 */
	public void setClaimCount(Long claim_count)
	{
		this.claim_count = claim_count;
	}
	    /**
        * @return the item_price
        */
        public Long getItemPrice() {
            return item_price;
        }

        /**
         * @param item_price the item_price to set
         */
        public void setItemPrice(Long item_price) {
            this.item_price = item_price;
        }

	/**
	 * 総売上を取得する。
	 * @return 総売上
	 */
	public Long getTotalSales()
	{
		return totalSales;
	}

	/**
	 * 総売上をセットする。
	 * @param totalSales 総売上
	 */
	public void setTotalSales(Long totalSales)
	{
		this.totalSales = totalSales;
	}	

    /**
     * @return the Misum
     */
    public Long getMisum() {
        return Misum;
    }

    /**
     * @param Misum the Misum to set
     */
    public void setMisum(Long Misum) {
        this.Misum = Misum;
    }

    /**
     * @return the Micount
     */
    public Long getMicount() {
        return Micount;
    }

    /**
     * @param Micount the Micount to set
     */
    public void setMicount(Long Micount) {
        this.Micount = Micount;
    }

    /**
     * @return the Mcsum
     */
    public Long getMcsum() {
        return Mcsum;
    }

    /**
     * @param Mcsum the Mcsum to set
     */
    public void setMcsum(Long Mcsum) {
        this.Mcsum = Mcsum;
    }

    /**
     * @return the Mccount
     */
    public Long getMccount() {
        return Mccount;
    }

    /**
     * @param Mccount the Mccount to set
     */
    public void setMccount(Long Mccount) {
        this.Mccount = Mccount;
    }

    /**
     * @return the Consumpsum
     */
    public Long getConsumpsum() {
        return Consumpsum;
    }

    /**
     * @param Consumpsum the Consumpsum to set
     */
    public void setConsumpsum(Long Consumpsum) {
        this.Consumpsum = Consumpsum;
    }

    /**
     * @return the Consumpcount
     */
    public Long getConsumpcount() {
        return Consumpcount;
    }

    /**
     * @param Consumpcount the Consumpcount to set
     */
    public void setConsumpcount(Long Consumpcount) {
        this.Consumpcount = Consumpcount;
    }

    /**
     * @return the count_item_price
     */
    public Long getCount_item_price() {
        return count_item_price;
    }

    /**
     * @param count_item_price the count_item_price to set
     */
    public void setCount_item_price(Long count_item_price) {
        this.count_item_price = count_item_price;
    }
}

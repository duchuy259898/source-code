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
 * “X•Üƒ‰ƒ“ƒLƒ“ƒOƒf[ƒ^
 * @author IVS_tttung
 */
public class IndividualSalesRanKing
{
	/**
	 * ƒXƒ^ƒbƒt–¼
	 */
	protected	String	    staff_name		=   "";
	/**
	 * ‹Zp‘e—˜
	 */
	private	Long	    tech_sum		=   0l;
	/**
	 * ‹Zp‹q”
	 */
	private	Long	    tech_count	=   0l;
	/**
	 * w–¼‘e—˜
	 */
	private	Long	    nomina_sum	=   0l;
	/**
	 * w–¼”
	 */
	private	Long	    nomina_count		=   0l;        
        /**
	 * “X”Ì‘e—˜
	 */
        private	Long	    item_price		=   0l;   
        
        private	Long	    count_item_price		=   0l; 
	/**
	 * ¸Ú°Ñ”
	 */
	protected Long	    claim_sum		=   0l;
	/**
	 * ¸Ú°ÑŒ¸‰vŠz
	 */
	protected Long	    claim_count		=   0l;
        
        
        private Long       Misum              =   0l;
        private Long       Micount            =   0l;
        private Long       Mcsum              =   0l;
        private Long       Mccount            =   0l;
        private Long       Consumpsum         =   0l;
        private Long       Consumpcount       =   0l;
	/**
	 * ”„ã‡Œv
	 */
	private Long	    totalSales		=   0l;
        /**
	 * ‹ZpV‹K”ä—¦
	 */
	//protected String    newCustomerCountRatio = "";
        
	/**
        * Creates a new instance of IndividualSalesRanKing
        */
	public IndividualSalesRanKing()
	{
	}
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğæ“¾‚·‚éB
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
	 * “X•Ü–¼‚ğæ“¾‚·‚éB
	 * @return “X•Ü–¼
	 */
	public String getStaffName()
	{
		return staff_name;
	}

	/**
	 * “X•Ü–¼‚ğƒZƒbƒg‚·‚éB
	 * @param shopName “X•Ü–¼
	 */
	public void setStaffName(String staffName)
	{
		this.staff_name = staffName;
	}

	
	/**
	 * ‘‹q”‚ğæ“¾‚·‚éB
	 * @return ‘‹q”
	 */
	public Long getTechSum() {
		return tech_sum;
	}

	/**
	 * ‘‹q”‚ğƒZƒbƒg‚·‚éB
	 * @param totalCount
	 */
	public void setTechSum(Long techsum) {
		this.tech_sum = techsum;
	}

	/**
	 * ‰ïˆõ”‚ğæ“¾‚·‚éB
	 * @return ‰ïˆõ”
	 */
	public Long getTechCount() {
		return tech_count;
	}

	/**
	 * ‰ïˆõ”‚ğƒZƒbƒg‚·‚éB
	 * @param customerCount
	 */
	public void setTechniCount(Long tech_count) {
		this.tech_count = tech_count;
	}

	/**
	 * V‹K‹q”‚ğæ“¾‚·‚éB
	 * @return V‹K‹q”
	 */
	public Long getNominaSum() {
		return nomina_sum;
	}

	/**
	 * V‹K‹q”‚ğƒZƒbƒg‚·‚éB
	 * @param newCustomerCount
	 */
	public void setNominaSum(Long newCustomerCount) {
		this.nomina_sum = newCustomerCount;
	}

	/**
	 * ‹Zp‹q”‚ğæ“¾‚·‚éB
	 * @return ‹Zp‹q”
	 */
	public Long getNominaCount() {
		return nomina_count;
	}

	/**
	 * ‹Zp‹q”‚ğƒZƒbƒg‚·‚éB
	 * @param techCount
	 */
	public void setNominaCount(Long nomina_count) {
		this.nomina_count = nomina_count;
	}
	
	/**
	 * ‹Zp”„ã‚ğæ“¾‚·‚éB
	 * @return ‹Zp”„ã
	 */
	public Long getClaimSum()
	{
		return claim_sum;
	}

	/**
	 * ‹Zp”„ã‚ğƒZƒbƒg‚·‚éB
	 * @param techSales ‹Zp”„ã
	 */
	public void setClaimSum(Long claim_sum)
	{
		this.claim_sum = claim_sum;
	}
	
	/**
	 * ‹Zp‹q’P‰¿‚ğæ“¾‚·‚éB
	 * @return ‹Zp‹q’P‰¿
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
	 * ‹q’P‰¿‚ğæ“¾‚·‚éB
	 * @return ‹q’P‰¿
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
	 * ¤•i”„ã‚ğæ“¾‚·‚éB
	 * @return ¤•i”„ã
	 */
	public Long getClaimCount()
	{
		return claim_count;
	}

	/**
	 * ¤•i”„ã‚ğƒZƒbƒg‚·‚éB
	 * @param itemSales ¤•i”„ã
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
	 * ‘”„ã‚ğæ“¾‚·‚éB
	 * @return ‘”„ã
	 */
	public Long getTotalSales()
	{
		return totalSales;
	}

	/**
	 * ‘”„ã‚ğƒZƒbƒg‚·‚éB
	 * @param totalSales ‘”„ã
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

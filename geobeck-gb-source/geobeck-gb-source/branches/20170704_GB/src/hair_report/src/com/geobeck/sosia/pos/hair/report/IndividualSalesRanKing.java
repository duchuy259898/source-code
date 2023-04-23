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
 * �X�܃����L���O�f�[�^
 * @author IVS_tttung
 */
public class IndividualSalesRanKing
{
	/**
	 * �X�^�b�t��
	 */
	protected	String	    staff_name		=   "";
	/**
	 * �Z�p�e��
	 */
	private	Long	    tech_sum		=   0l;
	/**
	 * �Z�p�q��
	 */
	private	Long	    tech_count	=   0l;
	/**
	 * �w���e��
	 */
	private	Long	    nomina_sum	=   0l;
	/**
	 * �w����
	 */
	private	Long	    nomina_count		=   0l;        
        /**
	 * �X�̑e��
	 */
        private	Long	    item_price		=   0l;   
        
        private	Long	    count_item_price		=   0l; 
	/**
	 * �ڰѐ�
	 */
	protected Long	    claim_sum		=   0l;
	/**
	 * �ڰь��v�z
	 */
	protected Long	    claim_count		=   0l;
        
        
        private Long       Misum              =   0l;
        private Long       Micount            =   0l;
        private Long       Mcsum              =   0l;
        private Long       Mccount            =   0l;
        private Long       Consumpsum         =   0l;
        private Long       Consumpcount       =   0l;
	/**
	 * ���㍇�v
	 */
	private Long	    totalSales		=   0l;
        /**
	 * �Z�p�V�K�䗦
	 */
	//protected String    newCustomerCountRatio = "";
        
	/**
        * Creates a new instance of IndividualSalesRanKing
        */
	public IndividualSalesRanKing()
	{
	}
	
	/**
	 * ResultSetWrapper����f�[�^���擾����B
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
	 * �X�ܖ����擾����B
	 * @return �X�ܖ�
	 */
	public String getStaffName()
	{
		return staff_name;
	}

	/**
	 * �X�ܖ����Z�b�g����B
	 * @param shopName �X�ܖ�
	 */
	public void setStaffName(String staffName)
	{
		this.staff_name = staffName;
	}

	
	/**
	 * ���q�����擾����B
	 * @return ���q��
	 */
	public Long getTechSum() {
		return tech_sum;
	}

	/**
	 * ���q�����Z�b�g����B
	 * @param totalCount
	 */
	public void setTechSum(Long techsum) {
		this.tech_sum = techsum;
	}

	/**
	 * ��������擾����B
	 * @return �����
	 */
	public Long getTechCount() {
		return tech_count;
	}

	/**
	 * ��������Z�b�g����B
	 * @param customerCount
	 */
	public void setTechniCount(Long tech_count) {
		this.tech_count = tech_count;
	}

	/**
	 * �V�K�q�����擾����B
	 * @return �V�K�q��
	 */
	public Long getNominaSum() {
		return nomina_sum;
	}

	/**
	 * �V�K�q�����Z�b�g����B
	 * @param newCustomerCount
	 */
	public void setNominaSum(Long newCustomerCount) {
		this.nomina_sum = newCustomerCount;
	}

	/**
	 * �Z�p�q�����擾����B
	 * @return �Z�p�q��
	 */
	public Long getNominaCount() {
		return nomina_count;
	}

	/**
	 * �Z�p�q�����Z�b�g����B
	 * @param techCount
	 */
	public void setNominaCount(Long nomina_count) {
		this.nomina_count = nomina_count;
	}
	
	/**
	 * �Z�p������擾����B
	 * @return �Z�p����
	 */
	public Long getClaimSum()
	{
		return claim_sum;
	}

	/**
	 * �Z�p������Z�b�g����B
	 * @param techSales �Z�p����
	 */
	public void setClaimSum(Long claim_sum)
	{
		this.claim_sum = claim_sum;
	}
	
	/**
	 * �Z�p�q�P�����擾����B
	 * @return �Z�p�q�P��
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
	 * �q�P�����擾����B
	 * @return �q�P��
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
	 * ���i������擾����B
	 * @return ���i����
	 */
	public Long getClaimCount()
	{
		return claim_count;
	}

	/**
	 * ���i������Z�b�g����B
	 * @param itemSales ���i����
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
	 * ��������擾����B
	 * @return ������
	 */
	public Long getTotalSales()
	{
		return totalSales;
	}

	/**
	 * ��������Z�b�g����B
	 * @param totalSales ������
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

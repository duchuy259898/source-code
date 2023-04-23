/*
 * ShopRankingBean.java
 *
 * Created on 27 August 2008, 11:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author shiera.delusa
 */
public class ShopRankingBean
{
    private Integer shopId;
    private Integer ranking;
    private String shopName;
    private Long technicSales;
    private Long itemSales;
    private Long totalSales;
    private Integer customers;
    private Integer newCustomers;
    private Long technicSalesPerStaff;
    private Integer custPerStaff;
    private Integer technicPrice;
    private Integer staffsOnDuty;
    private Integer totalWorkDays;
    private Integer actualWorkDays;
    private Integer remainingWorkDays;
    private Long technicAccumulated;
    private Long technicSalesEstimate;
    private Integer customersTotal;
    private Integer customersEstimate;
    private Long previousYearSales;
    private Long lastYearDiff;
    private Double lastYearDiffRate;
    private Long targetSales;
    private Long targetDifference;
    private Double targetDiffRate;
    private Integer otherExpenses;
    private Long totalExpenses;
    private Long technicTax;
    private Long itemTax;
    
    /** Creates a new instance of ShopRankingBean */
    public ShopRankingBean()
    {        
    }

    public Integer getRanking()
    {
        return ranking;
    }

    public void setRanking(Integer ranking)
    {
        this.ranking = ranking;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public Long getTechnicSales()
    {
        return technicSales;
    }

    public void setTechnicSales(Long technicSales)
    {
        this.technicSales = technicSales;
    }

    public Long getItemSales()
    {
        return itemSales;
    }

    public void setItemSales(Long itemSales)
    {
        this.itemSales = itemSales;
    }

    public Long getTotalSales()
    {
        return totalSales;
    }

    public void setTotalSales(Long totalSales)
    {
        this.totalSales = totalSales;
    }

    public Integer getCustomers()
    {
        return customers;
    }

    public void setCustomers(Integer customers)
    {
        this.customers = customers;
    }

    public Integer getNewCustomers()
    {
        return newCustomers;
    }

    public void setNewCustomers(Integer newCustomers)
    {
        this.newCustomers = newCustomers;
    }

	public Long getTechnicSalesPerStaff()
	{
		return technicSalesPerStaff;
	}
	
	public void setTechnicSalesPerStaff(Long technicSalesPerStaff)
	{
		this.technicSalesPerStaff = technicSalesPerStaff;
	}

    public Integer getCustPerStaff()
    {
        return custPerStaff;
    }

    public void setCustPerStaff(Integer hitTechnicCustomers)
    {
        this.custPerStaff = hitTechnicCustomers;
    }


    public Integer getTechnicPrice()
    {
        return technicPrice;
    }

    public void setTechnicPrice(Integer technicPrice)
    {
        this.technicPrice = technicPrice;
    }

    public Integer getStaffsOnDuty()
    {
        return staffsOnDuty;
    }

    public void setStaffsOnDuty(Integer staffsOnDuty)
    {
        this.staffsOnDuty = staffsOnDuty;
    }

    public Integer getTotalWorkDays()
    {
        return totalWorkDays;
    }

    public void setTotalWorkDays(Integer totalWorkDays)
    {
        this.totalWorkDays = totalWorkDays;
    }

    public Integer getActualWorkDays()
    {
        return actualWorkDays;
    }

    public void setActualWorkDays(Integer actualWorkDays)
    {
        this.actualWorkDays = actualWorkDays;
    }

    public Integer getRemainingWorkDays()
    {
        return remainingWorkDays;
    }

    public void setRemainingWorkDays(Integer remainingWorkDays)
    {
        this.remainingWorkDays = remainingWorkDays;
    }

    public Long getTechnicAccumulated()
    {
        return technicAccumulated;
    }

    public void setTechnicAccumulated(Long technicAccumulated)
    {
        this.technicAccumulated = technicAccumulated;
    }

    public Long getTechnicSalesEstimate()
    {
        return technicSalesEstimate;
    }

    public void setTechnicSalesEstimate(Long technicSalesEstimate)
    {
        this.technicSalesEstimate = technicSalesEstimate;
    }

    public Integer getCustomersTotal()
    {
        return customersTotal;
    }

    public void setCustomersTotal(Integer customersTotal)
    {
        this.customersTotal = customersTotal;
    }

    public Integer getCustomersEstimate()
    {
        return customersEstimate;
    }

    public void setCustomersEstimate(Integer customersEstimate)
    {
        this.customersEstimate = customersEstimate;
    }

    public Long getPreviousYearSales()
    {
        return previousYearSales;
    }

    public void setPreviousYearSales(Long previousYearSales)
    {
        this.previousYearSales = previousYearSales;
    }

    public Long getLastYearDiff()
    {
        return lastYearDiff;
    }

    public void setLastYearDiff(Long lastYearDiff)
    {
        this.lastYearDiff = lastYearDiff;
    }

    public Double getLastYearDiffRate()
    {
        return lastYearDiffRate;
    }

    public void setLastYearDiffRate(Double lastYearDiffRate)
    {
        this.lastYearDiffRate = lastYearDiffRate;
    }

    public Long getTargetSales()
    {
        return targetSales;
    }

    public void setTargetSales(Long targetSales)
    {
        this.targetSales = targetSales;
    }

    public Long getTargetDifference()
    {
        return targetDifference;
    }

    public void setTargetDifference(Long targetDifference)
    {
        this.targetDifference = targetDifference;
    }

    public Double getTargetDiffRate()
    {
        return targetDiffRate;
    }

    public void setTargetDiffRate(Double targetDiffRate)
    {
        this.targetDiffRate = targetDiffRate;
    }

    public Integer getOtherExpenses()
    {
        return otherExpenses;
    }

    public void setOtherExpenses(Integer otherExpenses)
    {
        this.otherExpenses = otherExpenses;
    }

    public Long getTotalExpenses()
    {
        return totalExpenses;
    }

    public void setTotalExpenses(Long totalExpenses)
    {
        this.totalExpenses = totalExpenses;
    }

    public Integer getShopId()
    {
        return shopId;
    }

    public void setShopId(Integer shopId)
    {
        this.shopId = shopId;
    }

    public Long getTechnicTax() {
        return technicTax;
    }

    public void setTechnicTax(Long technicTax) {
        this.technicTax = technicTax;
    }

    public Long getItemTax() {
        return itemTax;
    }

    public void setItemTax(Long itemTax) {
        this.itemTax = itemTax;
    }
    
}

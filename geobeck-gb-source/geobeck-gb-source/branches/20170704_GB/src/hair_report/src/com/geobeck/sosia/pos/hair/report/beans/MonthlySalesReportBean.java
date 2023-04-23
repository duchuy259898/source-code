/*
 * MonthlySalesReportBean.java
 *
 * Created on 27 August 2008, 10:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.util.Date;

/**
 *
 * @author shiera.delusa
 */
public class MonthlySalesReportBean
{
    private Date salesDay; 
    private Long technicSales;
    private Long technicRunningTotal;
    private Long technicSalesEstimate;
    private Integer customers;
    private Integer customersRunningTotal;
    private Integer customersEstimate;
    private Long averageFeePerCustomer;
    private Integer nominations;
    private Integer nominationsRunningTotal;
    private Double nominationsPercentage;
    private Integer pEachDay;
    private Integer pRunningTotal;
    private Double pPercentage;
    private Integer colEachDay;
    private Integer colRunningTotal;
    private Double colPercentage;
    private Integer trEachDay;
    private Integer trRunningTotal;
    private Double trPercentage;
    private Integer newEachDay;
    private Integer newRunningTotal;
    private Double newPercentage;
	private Long itemSales;
	private Long itemRunningTotal;
    private Long itemSalesEstimate;
    private Integer custChild;
    private Integer custChildRunningTotal;

    /**
     * Creates a new instance of MonthlySalesReportBean
     */

	public Date getSalesDay()
	{
		return salesDay;
	}

	public void setSalesDay(Date salesDay)
	{
		this.salesDay = salesDay;
	}

	public Long getTechnicSales()
	{
		return technicSales;
	}

	public void setTechnicSales(Long technicSales)
	{
		this.technicSales = technicSales;
	}

	public Long getTechnicRunningTotal()
	{
		return technicRunningTotal;
	}

	public void setTechnicRunningTotal(Long technicRunningTotal)
	{
		this.technicRunningTotal = technicRunningTotal;
	}

	public Long getTechnicSalesEstimate()
	{
		return technicSalesEstimate;
	}

	public void setTechnicSalesEstimate(Long technicSalesEstimate)
	{
		this.technicSalesEstimate = technicSalesEstimate;
	}

	public Integer getCustomers()
	{
		return customers;
	}

	public void setCustomers(Integer customers)
	{
		this.customers = customers;
	}

	public Integer getCustomersRunningTotal()
	{
		return customersRunningTotal;
	}

	public void setCustomersRunningTotal(Integer customersRunningTotal)
	{
		this.customersRunningTotal = customersRunningTotal;
	}

	public Integer getCustomersEstimate()
	{
		return customersEstimate;
	}

	public void setCustomersEstimate(Integer customersEstimate)
	{
		this.customersEstimate = customersEstimate;
	}

	public Long getAverageFeePerCustomer()
	{
		return averageFeePerCustomer;
	}

	public void setAverageFeePerCustomer(Long averageFeePerCustomer)
	{
		this.averageFeePerCustomer = averageFeePerCustomer;
	}

	public Integer getNominations()
	{
		return nominations;
	}

	public void setNominations(Integer nominations)
	{
		this.nominations = nominations;
	}

	public Integer getNominationsRunningTotal()
	{
		return nominationsRunningTotal;
	}

	public void setNominationsRunningTotal(Integer nominationsRunningTotal)
	{
		this.nominationsRunningTotal = nominationsRunningTotal;
	}

	public Double getNominationsPercentage()
	{
		return nominationsPercentage;
	}

	public void setNominationsPercentage(Double nominationsPercentage)
	{
		this.nominationsPercentage = nominationsPercentage;
	}

	public Integer getPEachDay()
	{
		return pEachDay;
	}

	public void setPEachDay(Integer pEachDay)
	{
		this.pEachDay = pEachDay;
	}

	public Integer getPRunningTotal()
	{
		return pRunningTotal;
	}

	public void setPRunningTotal(Integer pRunningTotal)
	{
		this.pRunningTotal = pRunningTotal;
	}

	public Double getPPercentage()
	{
		return pPercentage;
	}

	public void setPPercentage(Double pPercentage)
	{
		this.pPercentage = pPercentage;
	}

	public Integer getColEachDay()
	{
		return colEachDay;
	}

	public void setColEachDay(Integer colEachDay)
	{
		this.colEachDay = colEachDay;
	}

	public Integer getColRunningTotal()
	{
		return colRunningTotal;
	}

	public void setColRunningTotal(Integer colRunningTotal)
	{
		this.colRunningTotal = colRunningTotal;
	}

	public Double getColPercentage()
	{
		return colPercentage;
	}

	public void setColPercentage(Double colPercentage)
	{
		this.colPercentage = colPercentage;
	}

	public Integer getTrEachDay()
	{
		return trEachDay;
	}

	public void setTrEachDay(Integer trEachDay)
	{
		this.trEachDay = trEachDay;
	}

	public Integer getTrRunningTotal()
	{
		return trRunningTotal;
	}

	public void setTrRunningTotal(Integer trRunningTotal)
	{
		this.trRunningTotal = trRunningTotal;
	}

	public Double getTrPercentage()
	{
		return trPercentage;
	}

	public void setTrPercentage(Double trPercentage)
	{
		this.trPercentage = trPercentage;
	}

	public Integer getNewEachDay()
	{
		return newEachDay;
	}

	public void setNewEachDay(Integer newEachDay)
	{
		this.newEachDay = newEachDay;
	}

	public Integer getNewRunningTotal()
	{
		return newRunningTotal;
	}

	public void setNewRunningTotal(Integer newRunningTotal)
	{
		this.newRunningTotal = newRunningTotal;
	}

	public Double getNewPercentage()
	{
		return newPercentage;
	}

	public void setNewPercentage(Double newPercentage)
	{
		this.newPercentage = newPercentage;
	}

	public Long getItemSales()
	{
		return itemSales;
	}

	public void setItemSales(Long itemSales)
	{
		this.itemSales = itemSales;
	}

	public Long getItemRunningTotal()
	{
		return itemRunningTotal;
	}

	public void setItemRunningTotal(Long itemRunningTotal)
	{
		this.itemRunningTotal = itemRunningTotal;
	}

	public Long getItemSalesEstimate()
	{
		return itemSalesEstimate;
	}

	public void setItemSalesEstimate(Long itemSalesEstimate)
	{
		this.itemSalesEstimate = itemSalesEstimate;
	}

	public Integer getCustChild()
	{
		return custChild;
	}

	public void setCustChild(Integer custChild)
	{
		this.custChild = custChild;
	}

	public Integer getCustChildRunningTotal()
	{
		return custChildRunningTotal;
	}

	public void setCustChildRunningTotal(Integer custChildRunningTotal)
	{
		this.custChildRunningTotal = custChildRunningTotal;
	}
}

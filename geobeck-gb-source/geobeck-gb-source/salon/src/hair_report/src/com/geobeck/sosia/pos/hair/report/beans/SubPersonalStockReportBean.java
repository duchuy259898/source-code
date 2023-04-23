/*
 * SubPersonalStockReportBean.java
 *
 * Created on 2008/08/27, 12:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class SubPersonalStockReportBean 
{
    private Integer countIndex;
    private String stockName;
    private Long stockPrice;
    
    /** Creates a new instance of SubPersonalStockReportBean */
    public SubPersonalStockReportBean() 
    {
    }
    
    public Integer getCountIndex()
    {
        return this.countIndex;
    }
    
    public String getStockName()
    {
        return this.stockName;
    }
    
    public Long getStockPrice()
    {
        return this.stockPrice;
    }
    
    public void setCountIndex(Integer index)
    {
        this.countIndex = index;
    }
    
    public void setStockName(String name)
    {
        this.stockName = name;
    }
    
    public void setStockPrice(Long price)
    {
        this.stockPrice = price;
    }
}

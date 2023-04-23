/*
 * StoreReportRateBean.java
 *
 * Created on 2008/08/25, 17:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class StoreReportRateBean 
{
    private String technicName;
	private Integer technicCount;
    private Double technicRate;
    
    /** Creates a new instance of StoreReportRateBean */
    public StoreReportRateBean()
    {
    }
    
    public String getTechnicName()
    {
        return technicName;
    }
    
    public void setTechnicName(String techName)
    {
        this.technicName = techName;
    }

    public Integer getTechnicCount()
    {
        return technicCount;
    }
	
    public void setTechnicCount(Integer technicCount)
    {
        this.technicCount = technicCount;
    }

    public Double getTechnicRate()
    {
        return technicRate;
    }
	
    public void setTechnicRate(Double techRate)
    {
        //System.out.println("rate" + techRate);
        this.technicRate = techRate;
    }
    
}

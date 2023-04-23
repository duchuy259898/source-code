/*
 * SubPersonalTechnicReport.java
 *
 * Created on 2008/08/27, 10:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class SubPersonalTechnicReportBean 
{
    private Integer countIndex;
    private String technicClassName;
    private String technicCount;
    private String technicAmount;
    
    /** Creates a new instance of SubPersonalTechnicReportBean  */
    public SubPersonalTechnicReportBean () 
    {
    }
    
    public Integer getCountIndex()
    {
        return this.countIndex;
    }
    
    public String getTechnicClassName()
    {
        return this.technicClassName;
    }
    
    public String getTechnicCount()
    {
        return this.technicCount;
    }
    
    public String getTechnicAmount()
    {
        return this.technicAmount;
    }
    
    public  void setCountIndex(Integer index)
    {
        this.countIndex = index;
    }
    
    public void setTechnicClassName(String name)
    {
        this.technicClassName = name;
    }
    
    public void setTechnicCount(String count)
    {
        this.technicCount = count;
    }
    
    public void setTechnicAmount(String amount)
    {
        this.technicAmount = amount;
    }
    
}


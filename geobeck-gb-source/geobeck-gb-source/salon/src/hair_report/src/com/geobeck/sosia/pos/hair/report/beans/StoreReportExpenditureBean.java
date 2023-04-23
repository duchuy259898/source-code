/*
 * StoreReportExpenditureBean.java
 *
 * Created on 2008/08/26, 14:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class StoreReportExpenditureBean
{
     private String expenditureName;
     private Long  expenditureValue;
     
    /** Creates a new instance of StoreReportExpenditureBean */
    public StoreReportExpenditureBean() 
    {
    }
    
    public String getExpenditureName()
    {
        return this.expenditureName;
    }

    public void setExpenditureName(String expenditureName) {
        this.expenditureName = expenditureName;
    }

    public Long getExpenditureValue() {
        return expenditureValue;
    }

    public void setExpenditureValue(Long expenditureValue) {
        this.expenditureValue = expenditureValue;
    }
    
    
}

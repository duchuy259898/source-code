/*
 * StoreReportAttendanceBean.java
 *
 * Created on 2008/08/26, 16:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class StoreReportAttendanceBean {
    
    public String inStaffName;
    public String outStaffName;
    
    /** Creates a new instance of StoreReportAttendanceBean */
    public StoreReportAttendanceBean() {
    }
    
    public String getInStaffName()
    {
        return this.inStaffName;
    }
    
    public String getOutStaffName()
    {
        return this.outStaffName;
    }
    
    public void setInStaffName(String name)
    {
        this.inStaffName = name;
    }
    
    public void setOutStaffName(String name)
    {
        this.outStaffName = name;
    }
    
}

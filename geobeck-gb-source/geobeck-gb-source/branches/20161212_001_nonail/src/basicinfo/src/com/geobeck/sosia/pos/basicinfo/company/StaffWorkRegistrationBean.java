/*
 * StaffWorkRegistrationBean.java
 *
 * Created on 2008/09/16, 15:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

import java.util.Date;
/**
 *
 * @author shiera.delusa
 */
public class StaffWorkRegistrationBean
{
    protected StaffBasicInfo basicInfo;
    private StaffWorkStatusBean workStatus;
    protected Date workingDate;
    
    /** Creates a new instance of StaffWorkRegistrationBean */
    public StaffWorkRegistrationBean()
    {
    }
    
    public StaffWorkRegistrationBean( StaffBasicInfo basicInfo, Date workingDate, int workId )
    {
        this.setBasicInfo( basicInfo );
        this.setWorkingDate( workingDate );
        this.setWorkId( workId );
    }
    
    public int getShopId()
    {
        return this.getBasicInfo().getShopId();
    }

    public void setShopId(int staffID )
    {
        this.getBasicInfo().setShopId( staffID );
    }
    
    public int getStaffId()
    {
        return this.getBasicInfo().getStaffId();
    }

    public void setStaffId(int staffID )
    {
        this.getBasicInfo().setStaffId( staffID );
    }

    public String getStaffNo()
    {
        return this.getBasicInfo().getStaffNo();
    }

    public void setStaffNo(String staffNo )
    {
        this.getBasicInfo().setStaffNo( staffNo );
    }

    public String getStaffName()
    {
        return this.getBasicInfo().getStaffName();
    }

    public void setStaffName(String staffName )
    {
        this.getBasicInfo().setStaffName( staffName );
    }
        
    public Date getWorkingDate()
    {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate)
    {
        this.workingDate = workingDate;
    }
    
    public int getWorkId()
    {
        return this.workStatus.getWorkId();
    }

    public void setWorkId(int workId)
    {
        this.workStatus.setWorkId( workId );
    }

    public StaffBasicInfo getBasicInfo()
    {
        return basicInfo;
    }

    public void setBasicInfo(StaffBasicInfo basicInfo)
    {
        this.basicInfo = basicInfo;
    }

    public StaffWorkStatusBean getWorkStatus()
    {
        return workStatus;
    }

    public void setWorkStatus(StaffWorkStatusBean workStatus)
    {
        this.workStatus = workStatus;
    }
}

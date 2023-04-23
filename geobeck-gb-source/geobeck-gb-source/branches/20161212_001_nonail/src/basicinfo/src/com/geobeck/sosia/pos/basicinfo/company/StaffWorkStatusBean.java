/*
 * StaffWorkStatusBean.java
 *
 * Created on 2008/09/16, 15:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

/**
 *
 * @author shiera.delusa
 */
public class StaffWorkStatusBean
{    
    protected int workId;
    protected String dispName;
    protected int workingFlag;

    /** Creates a new instance of StaffWorkStatusBean */
    public StaffWorkStatusBean()
    {
    }
        
    public int getWorkId()
    {
        return workId;
    }

    public void setWorkId(int workId)
    {
        this.workId = workId;
    }

    public String getDispName()
    {
        return dispName;
    }

    public void setDispName(String dispName)
    {
        this.dispName = dispName;
    }

    public int getWorkingFlag()
    {
        return workingFlag;
    }

    public void setWorkingFlag(int workingFlag)
    {
        this.workingFlag = workingFlag;
    }
}

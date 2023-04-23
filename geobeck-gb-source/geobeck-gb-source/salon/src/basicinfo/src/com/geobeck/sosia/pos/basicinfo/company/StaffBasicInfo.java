/*
 * StaffBasicInfo.java
 *
 * Created on 2008/09/16, 16:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

/**
 *
 * @author shiera.delusa
 */
public class StaffBasicInfo
{
    protected int shopId;
    protected int staffId;
	protected String staffNo;
    private String staffName;
    
    /** Creates a new instance of StaffBasicInfo */
    public StaffBasicInfo()
    {
    }
    
    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    
    public int getStaffId()
    {
        return staffId;
    }

    public void setStaffId(int staffId)
    {
        this.staffId = staffId;
    }

    public String getStaffNo()
    {
        return staffNo;
    }

    public void setStaffNo(String staffNo)
    {
        this.staffNo = staffNo;
    }
	
    public String getStaffName()
    {
        return staffName;
    }

    public void setStaffName(String staffName)
    {
        this.staffName = staffName;
    }

}

/*
 * SubPersonalServiceReportBean.java
 *
 * Created on 2008/08/27, 11:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class SubPersonalServiceReportBean 
{
    private int countIndex;
    private String helpName;
    private String helpId;
    
    /** Creates a new instance of SubPersonalServiceReportBean */
    public SubPersonalServiceReportBean() 
    {
    }
    
    public int getCountIndex()
    {
        return this.countIndex;
    }
    
    public String getHelpName()
    {
        return this.helpName;
    }
    
    public String getHelpID()
    {
        return this.helpId;
    }
    
    public void setCountIndex(int count)
    {
        this.countIndex = count;
    }
    
    public void setHelpName(String name)
    {
        this.helpName = name;
    }
    
    public void setHelpId(String id)
    {
        this.helpId = id;
    }
}

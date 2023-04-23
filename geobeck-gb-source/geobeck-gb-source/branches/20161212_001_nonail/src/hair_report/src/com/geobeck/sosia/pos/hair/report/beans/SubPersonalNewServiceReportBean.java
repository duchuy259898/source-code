/*
 * SubPersonalNewServiceReportBean.java
 *
 * Created on 2008/08/27, 14:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class SubPersonalNewServiceReportBean 
{

    private int countIndex;
    private String newHelpName;
    private String newHelpID;
    
    /** Creates a new instance of SubPersonalNewServiceReportBean */
    public SubPersonalNewServiceReportBean() 
    {
    }
  
    public int getCountIndex()
    {
        return this.countIndex;
    }
    
    public String getNewHelpName()
    {
        return this.newHelpName;
    }
    
    public String getNewHelpID()
    {
        return this.newHelpID;
    }
    
    public void setCountIndex(int count)
    {
        this.countIndex = count;
    }
    
    public void setNewHelpName(String name)
    {
        this.newHelpName = name;
    }
    
    public void setNewHelpId(String id)
    {
        this.newHelpID = id;
    }
    
    
}

/*
 * SalesDateBean.java
 *
 * Created on 2008/09/18, 12:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.util.ArrayList;
/**
 *
 * @author trino
 */
public class SalesDateBean
{
    private String year;
    private ArrayList<String> months = new ArrayList<String>();
    
    /** Creates a new instance of SalesDateBean */
    public SalesDateBean()
    {
    }
        
    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public ArrayList<String> getMonths()
    {
        return months;
    }

    public void setMonths(ArrayList<String> months)
    {
        this.months = months;
    }
        
    public void addMonth( String strMonth )
    {
        this.months.add( strMonth );
    }
    
    public String getMonth( int index )
    {
        return this.months.get( index );
    }
        
        
    public boolean isMonthPresent( String month )
    {
        int index = 0;
        String temp = null;

        while( index < this.months.size() )
        {
            temp = this.months.get( index++ ); 
            if( month.equalsIgnoreCase( temp ) )
            {
                return true;
            }
        }
        return false;
    }
    
}

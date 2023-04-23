/*
 * TechnicSalesReportLogic.java
 *
 * Created on 2008/09/18, 11:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import java.util.Date;
import java.util.logging.Level;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.report.beans.SalesDateBean;
import com.geobeck.util.SQLUtil;

/**
 *
 * @author shiera.delusa
 */
public class TechnicSalesReportLogic
{
    /** Creates a new instance of TechnicSalesReportLogic */
    public TechnicSalesReportLogic()
    {
    }
    
    public static int getShopId( String shopName )
    {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
        int shopId = -1;
        
        try 
        {
            String strQuery = "SELECT shop_id FROM mst_shop" +
                    " WHERE shop_name='" + shopName + "' AND delete_date IS null";
            result = jdbcConnection.executeQuery( strQuery );
            
            if( result.next() )
            {
                shopId = result.getInt( "shop_id" );
            }
            else
            {
                throw( new Exception( "shop_idÇ™å©Ç¬Ç©ÇËÇ‹ÇπÇÒÅB" ) );
            }
            
            result.close();
        } 
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        } 
        
        return shopId;
    }
    
    public static ArrayList getSalesDateBean( int shopId )
    {
        ArrayList<SalesDateBean> salesDateBeanList = new ArrayList<SalesDateBean>();
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
        
        SimpleDateFormat yearFormat = new SimpleDateFormat( "yyyy" );
        SimpleDateFormat monthFormat = new SimpleDateFormat( "MM" );
        Date date = null;
        
        String yearStr = null;
        String monthStr = null;
        SalesDateBean salesDateBeanClass = null;
        int currSalesDateIndex = -1;
        
        try 
        {
            StringBuilder sql = new StringBuilder(1000);
            
            sql.append(" select");
            sql.append("     date_trunc('month', sales_date) as sales_date");
            sql.append(" from");
            sql.append("     data_sales");
            sql.append(" where");
            sql.append("         delete_date is null");
            sql.append("     and sales_date is not null");
            if (shopId != 0) {
                sql.append(" and shop_id = " + SQLUtil.convertForSQL(shopId));
            }
            sql.append(" group by");
            sql.append("     date_trunc('month', sales_date)");
            sql.append(" order by");
            sql.append("     1 desc");

            result = jdbcConnection.executeQuery(sql.toString());
            
            while( result.next() )
            {
                date = result.getDate( "sales_date" );
                
                yearStr = yearFormat.format( date );
                monthStr = monthFormat.format( date );
                
                if( isYearPresent( salesDateBeanList, yearStr ) == false )
                {   
                    salesDateBeanClass = null;
                    salesDateBeanClass = new SalesDateBean();
                    salesDateBeanClass.setYear( yearStr );
                    salesDateBeanClass.addMonth( monthStr );
                    salesDateBeanList.add( salesDateBeanClass );
                    currSalesDateIndex++;
                }
                else
                {
                    salesDateBeanClass.setYear( yearStr );
                    if( salesDateBeanClass.isMonthPresent( monthStr ) == false )
                    {
                        salesDateBeanClass.addMonth( monthStr );
                    }
                    salesDateBeanList.set( currSalesDateIndex, salesDateBeanClass );
                }
            }
            
            result.close();
        } 
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        } 
        
        return salesDateBeanList;
    }
    
    private static boolean isYearPresent( ArrayList<SalesDateBean> SalesDateBeanList, String year )
    {
        int index = 0;
        SalesDateBean temp = null;
        
        while( index < SalesDateBeanList.size() )
        {
            temp = SalesDateBeanList.get( index++ ); 
            if( year.equals( temp.getYear() ) )
            {
                return true;
            }
        }
        return false;
    }
    
} //END OF CLASS TechnicSalesReportLogic

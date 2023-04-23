/*
 * DataTargetResultDAO.java
 *
 * Created on 2008/09/24, 11:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.util.SQLUtil;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.Calendar;
import java.util.ArrayList;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.NumberUtil;

/**
 *
 * @author trino
 */
public class DataTargetResultDAO
{
    static final protected int MONTHS_COUNT = 12;
    
    /** Creates a new instance of DataTargetResultDAO */
    public DataTargetResultDAO()
    {
    }
    
    public static boolean saveToDatabase( DataTargetResultBean info  )
    {
        if( isRegistered( info ) == false )
        {
            return insertData( info );
        }
        else
        {
            return updateData( info );
        }
    }
    
    public static boolean saveToDatabase( ArrayList<DataTargetResultBean> infoList  )
    {
        if( infoList.size() < MONTHS_COUNT )
            return false;
        
        DataTargetResultBean tempBean = null;
        for( int index=0; index < MONTHS_COUNT; index++ )
        {
            tempBean = infoList.get( index );
            if( isRegistered( tempBean ) )
            {
                if( DataTargetResultDAO.updateData( tempBean ) == false )
                    return false;
            }
            else
            {
                if( DataTargetResultDAO.insertData( tempBean ) == false )
                    return false;
            }
        }
        return true;
    }
    
    protected static boolean insertData( DataTargetResultBean newInfo )
    {
        String COMMA = ", ";
        boolean bAdded = false;
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        
        String sqlStatement = "INSERT INTO data_target_result VALUES( " +
                newInfo.getShopId() + COMMA + newInfo.getYear() + COMMA +
                newInfo.getMonth() + COMMA + newInfo.getResultTechnic() + COMMA +
                newInfo.getResultItem() + COMMA + newInfo.getResultIn() + COMMA +
                newInfo.getResultNew() + COMMA + newInfo.getResultK() + COMMA +
                newInfo.getResultSET() + COMMA + newInfo.getResultS() + COMMA +
                newInfo.getResultHD() + COMMA + newInfo.getResultP() + COMMA +
                newInfo.getResultSTP() + COMMA + newInfo.getResultTR() + COMMA +
                newInfo.getResultETC() + COMMA + newInfo.getResultCRM() + COMMA +
                newInfo.getResultMON() + COMMA + newInfo.getTargetTechnic() + COMMA +
                newInfo.getTargetItem() + COMMA + newInfo.getTargetIn() + COMMA +
                newInfo.getTargetNew() + COMMA + newInfo.getTargetK() + COMMA +
                newInfo.getTargetSET() + COMMA + newInfo.getTargetS() + COMMA +
                newInfo.getTargetHD() + COMMA + newInfo.getTargetP() + COMMA +
                newInfo.getTargetSTP() + COMMA + newInfo.getTargetTR() + COMMA +
                newInfo.getTargetETC() + COMMA + newInfo.getTargetCRM() + COMMA +
                newInfo.getTargetMON() + COMMA + newInfo.getOpenDays() + COMMA +
                "'" + dateFormat.format( now ) + "'" + COMMA + 
                "'" + dateFormat.format( now ) + "'" + COMMA +
                "null )";        
        try
        {
            dbAccess.execute( sqlStatement );
            bAdded = true;
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            bAdded = false;
        }
        
        return bAdded;
    }
    
    protected static boolean updateData( DataTargetResultBean newInfo )
    {
        boolean bUpdated = false;
        String COMMA = ", ";
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        
        String sqlStatement = "UPDATE data_target_result SET " +
                "result_technic=" + newInfo.getResultTechnic() + COMMA +
                "result_item=" + newInfo.getResultItem() + COMMA + 
                "result_in=" + newInfo.getResultIn() + COMMA +
                "result_new=" + newInfo.getResultNew() + COMMA +
                "result_k=" + newInfo.getResultK() + COMMA +
                "result_set=" + newInfo.getResultSET() + COMMA +
                "result_s=" + newInfo.getResultS() + COMMA +
                "result_hd=" + newInfo.getResultHD() + COMMA +
                "result_p=" + newInfo.getResultP() + COMMA +
                "result_stp=" + newInfo.getResultSTP() + COMMA +
                "result_tr=" + newInfo.getResultTR() + COMMA +
                "result_etc=" + newInfo.getResultETC() + COMMA +
                "result_crm=" + newInfo.getResultCRM() + COMMA +
                "result_mon=" + newInfo.getResultMON() + COMMA +
                "target_technic=" + newInfo.getTargetTechnic() + COMMA +
                "target_item=" + newInfo.getTargetItem() + COMMA +
                "target_in=" + newInfo.getTargetIn() + COMMA +
                "target_new=" + newInfo.getTargetNew() + COMMA +
                "target_k=" + newInfo.getTargetK() + COMMA +
                "target_set=" + newInfo.getTargetSET() + COMMA +
                "target_s=" + newInfo.getTargetS() + COMMA +
                "target_hd=" + newInfo.getTargetHD() + COMMA +
                "target_p=" + newInfo.getTargetP() + COMMA +
                "target_stp=" + newInfo.getTargetSTP() + COMMA +
                "target_tr=" + newInfo.getTargetTR() + COMMA +
                "target_etc=" + newInfo.getTargetETC() + COMMA +
                "target_crm=" + newInfo.getTargetCRM() + COMMA +
                "target_mon=" + newInfo.getTargetMON() + COMMA +
                "open_days=" + newInfo.getOpenDays() + COMMA +
                "update_date='" + dateFormat.format( now ) + "'" + 
                " WHERE shop_id=" + newInfo.getShopId() + " AND  year=" + newInfo.getYear() +
                " AND month=" + newInfo.getMonth() + " AND delete_date IS NULL";
        try
        {
            dbAccess.execute( sqlStatement );
            bUpdated = true;
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            bUpdated = false;
        }
        
        return bUpdated;
    }
    
    public static ArrayList<DataTargetResultBean> getYearData( int shopId, int year )
    {
        ArrayList<DataTargetResultBean> yearDataList = new ArrayList<DataTargetResultBean>(MONTHS_COUNT);
        DataTargetResultBean tempBean = null;
        
        for( int index = 0; index < MONTHS_COUNT; index++ )
        {
            yearDataList.add( getMonthData( shopId, year, index+1 ) );
        }
        
        return yearDataList;
    }
    
    protected static DataTargetResultBean getMonthData( int shopId, int year, int month )
    {
        DataTargetResultBean data = null;
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        String sqlQuery = "SELECT * FROM data_target_result WHERE " +
                " shop_id=" + SQLUtil.convertForSQL(shopId) + " AND  year=" + SQLUtil.convertForSQL(year) +
                " AND month=" + SQLUtil.convertForSQL(month) + " AND delete_date IS NULL";

        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            if( result.next() )
            {
                data = new DataTargetResultBean();
                data.setShopId( result.getInt( "shop_id" ) );
                data.setYear( result.getInt( "year" ) );
                data.setMonth( result.getInt( "month" ) );
                data.setResultTechnic( result.getLong( "result_technic" ) );
                data.setResultItem( result.getLong( "result_item" ) ); 
                data.setResultIn( result.getLong( "result_in" ) );
                data.setResultNew( result.getLong( "result_new" ) );
                data.setResultK( result.getLong( "result_k" ) );
                data.setResultSET( result.getLong( "result_set" ) );
                data.setResultS( result.getLong( "result_s" ) );
                data.setResultHD( result.getLong( "result_hd" ) );
                data.setResultP( result.getLong( "result_p" ) );
                data.setResultSTP( result.getLong( "result_stp" ) );
                data.setResultTR( result.getLong( "result_tr" ) );
                data.setResultETC( result.getLong( "result_etc" ) );
                data.setResultCRM( result.getLong( "result_crm" ) );
                data.setResultMON( result.getLong( "result_mon" ) );
                data.setTargetTechnic( result.getLong( "target_technic" ) );
                data.setTargetItem( result.getLong( "target_item" ) );
                data.setTargetIn( result.getLong( "target_in" ) );
                data.setTargetNew( result.getLong( "target_new" ) );
                data.setTargetK( result.getLong( "target_k" ) );
                data.setTargetSET( result.getLong( "target_set" ) );
                data.setTargetS( result.getLong( "target_s" ) );
                data.setTargetHD( result.getLong( "target_hd" ) );
                data.setTargetP( result.getLong( "target_p" ) );
                data.setTargetSTP( result.getLong( "target_stp" ) );
                data.setTargetTR( result.getLong( "target_tr" ) );
                data.setTargetETC( result.getLong( "target_etc" ) );
                data.setTargetCRM( result.getLong( "target_crm" ) );
                data.setTargetMON( result.getLong( "target_mon" ) );
                data.setOpenDays( result.getInt( "open_days" ) );  
                
                //”„ãŽÀÑ = ‹ZpŽÀÑ@{@¤•iŽÀÑ
                data.setResultTotal( data.getResultTechnic() + data.getResultItem() );            
                //“ü‹qŽÀÑ = ‹ZpŽÀÑ@/ “ü‹qŽÀÑ
                if( ( data.getResultTechnic() > 0 ) && ( data.getResultIn() > 0 ) )
                {
                    data.setResultAvgAmount( NumberUtil.round( (double)data.getResultTechnic() / (double)data.getResultIn()) );
                }
                //”„ã–Ú•W = ‹Zp–Ú•W@{ ¤•i–Ú•W
                data.setTargetTotal( data.getTargetTechnic() + data.getTargetItem() );        
                //“ü‹q–Ú•W = ‹Zp–Ú•W@/ “ü‹q–Ú•W
                if( ( data.getTargetTechnic() > 0 ) && ( data.getTargetIn() > 0 ) )
                {
                    data.setTargetAvgAmount( NumberUtil.round((double)data.getTargetTechnic() / (double)data.getTargetIn()) );
                }
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return data;
    }
    
    public static boolean isRegistered( DataTargetResultBean info )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        boolean bRegistered = false;
        String sqlQuery = "SELECT year FROM data_target_result WHERE month=" + 
                SQLUtil.convertForSQL(info.getMonth()) + " AND year=" + SQLUtil.convertForSQL(info.getYear()) + 
                " AND shop_id=" + SQLUtil.convertForSQL(info.getShopId()) + " AND delete_date IS NULL";        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            if( result.next() )
            {
                bRegistered = true;
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return bRegistered;
    }    
    
    /**
     * @param   month values must be 1-12 (January-December)
     */
    public static DataTargetResultBean getDataTarget( int shopId, int month, int year )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        DataTargetResultBean bean = null;
        
        String sqlQuery = "SELECT shop_id, year, month,target_technic, target_item, " +
                "target_in, target_new, target_k, target_set, target_s, target_hd, " +
                "target_p, target_stp, target_tr, target_etc, target_crm, target_mon " +
                "FROM data_target_result WHERE shop_id=" + SQLUtil.convertForSQL(shopId) + " AND month=" + SQLUtil.convertForSQL(month) +
                " AND year=" + SQLUtil.convertForSQL(year) + " AND delete_date IS NOT NULL";
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            if( result.next() )
            {
                bean.setShopId( result.getInt( "shop_id" ) );
                bean.setYear( result.getInt( "year" ) );
                bean.setMonth( result.getInt( "month" ) );
                bean.setTargetTechnic( result.getLong( "target_technic" ) );
                bean.setTargetItem( result.getLong( "target_item" ) );
                bean.setTargetIn( result.getLong( "target_in" ) );
                bean.setTargetNew( result.getLong( "target_new" ) );
                bean.setTargetK( result.getLong( "target_k" ) );
                bean.setTargetSET( result.getLong( "target_set" ) );
                bean.setTargetS( result.getLong( "target_s" ) );
                bean.setTargetHD( result.getLong( "target_hd" ) );
                bean.setTargetP( result.getLong( "target_p" ) );
                bean.setTargetSTP( result.getLong( "target_stp" ) );
                bean.setTargetTR( result.getLong( "target_tr" ) );
                bean.setTargetETC( result.getLong( "target_etc" ) );
                bean.setTargetCRM( result.getLong( "target_crm" ) );
                bean.setTargetMON( result.getLong( "target_mon" ) );
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return bean;
    }
        
    /**
     * @param   month values must be 1-12 (January-December)
     */
    public static DataTargetResultBean getDataResult( int shopId, int month, int year )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        DataTargetResultBean bean = null;
        
        String sqlQuery = "SELECT shop_id, year, month,result_technic, result_item, " +
                "result_in, result_new, result_k, result_set, result_s, result_hd, " +
                "result_p, result_stp, result_tr, result_etc, result_crm, result_mon " +
                "FROM data_target_result WHERE shop_id=" + SQLUtil.convertForSQL(shopId) + " AND month=" + SQLUtil.convertForSQL(month) +
                " AND year=" + SQLUtil.convertForSQL(year) + " AND delete_date IS NOT NULL";
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            if( result.next() )
            {
                bean.setShopId( result.getInt( "shop_id" ) );
                bean.setYear( result.getInt( "year" ) );
                bean.setMonth( result.getInt( "month" ) );
                bean.setResultTechnic( result.getLong( "result_technic" ) );
                bean.setResultItem( result.getLong( "result_item" ) );
                bean.setResultIn( result.getLong( "result_in" ) );
                bean.setResultNew( result.getLong( "result_new" ) );
                bean.setResultK( result.getLong( "result_k" ) );
                bean.setResultSET( result.getLong( "result_set" ) );
                bean.setResultS( result.getLong( "result_s" ) );
                bean.setResultHD( result.getLong( "result_hd" ) );
                bean.setResultP( result.getLong( "result_p" ) );
                bean.setResultSTP( result.getLong( "result_stp" ) );
                bean.setResultTR( result.getLong( "result_tr" ) );
                bean.setResultETC( result.getLong( "result_etc" ) );
                bean.setResultCRM( result.getLong( "result_crm" ) );
                bean.setResultMON( result.getLong( "result_mon" ) );
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return bean;
    }
    
    private int getCalendarMonthValue( int month )
    {
        switch( month )
        {
            case DataTargetResultBean.JANUARY:
                month = Calendar.JANUARY;
                break;
            case DataTargetResultBean.FEBRUARY:
                month = Calendar.FEBRUARY;
                break;
            case DataTargetResultBean.MARCH:
                month = Calendar.MARCH;
                break;
            case DataTargetResultBean.APRIL:
                month = Calendar.APRIL;
                break;
            case DataTargetResultBean.MAY:
                month = Calendar.MAY;
                break;
            case DataTargetResultBean.JUNE:
                month = Calendar.JUNE;
                break;
            case DataTargetResultBean.JULY:
                month = Calendar.JULY;
                break;
            case DataTargetResultBean.AUGUST:
                month = Calendar.AUGUST;
                break;
            case DataTargetResultBean.SEPTEMBER:
                month = Calendar.SEPTEMBER;
                break;
            case DataTargetResultBean.OCTOBER:
                month = Calendar.OCTOBER;
                break;
            case DataTargetResultBean.NOVEMBER:
                month = Calendar.NOVEMBER;
                break;
            case DataTargetResultBean.DECEMBER:
                month = Calendar.DECEMBER;
                break;
        }
        return month;
    }

	public static ArrayList<Integer> getExistingYear(int nShopId)
	{
		ConnectionWrapper dbAccess = SystemInfo.getConnection();
		ArrayList<Integer> arrExisitingYear = new ArrayList();
		String strSQL;

		arrExisitingYear.clear();

		strSQL = "";
		strSQL += "	(";
		strSQL += "		select distinct";
		strSQL += "			year";
		strSQL += "		from data_target_result";
		strSQL += "		where";
		strSQL += "			shop_id = " + SQLUtil.convertForSQL(nShopId);
		strSQL += " )";
		strSQL += " union distinct";
		strSQL += " (";
		strSQL += "		select cast( date_part('year', now()) as integer )";
		strSQL += " )";
//		strSQL += " union distinct";
//		strSQL += " (";
//		strSQL += "		select cast( date_part('year', now())  +  1   as integer )";
//		strSQL += " )";

		strSQL += " order by year desc";
                
		try
		{
			ResultSetWrapper result = dbAccess.executeQuery( strSQL );
			
			while ( result.next() )
			{
				arrExisitingYear.add( result.getInt( "year" ) );
			}
			result.close();
		}
		catch( Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return arrExisitingYear;
	}
}

/*
 * StaffWorkRegistrationDAO.java
 *
 * Created on 2008/09/16, 15:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.util.SQLUtil;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;

/**
 *
 * @author shiera.delusa
 */
public class StaffWorkRegistrationDAO
{
    
    /** Creates a new instance of StaffWorkRegistrationDAO */
    public StaffWorkRegistrationDAO()
    {
    }
    
    public static boolean registerWorkStatus( StaffWorkRegistrationBean info )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        
        String sqlString =
                "INSERT INTO data_working_staff(shop_id,working_date,staff_id,work_id,insert_date,update_date)" +
                " VALUES(" +
				SQLUtil.convertForSQL(info.getShopId()) + ", " +
				SQLUtil.convertForSQLDateOnly(info.getWorkingDate()) + "," +
                SQLUtil.convertForSQL(info.getStaffId()) + ", " + SQLUtil.convertForSQL(info.getWorkId()) + ", " +
                "now(), now() )";
        try
        {
            dbAccess.execute( sqlString );
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        return true;
    }
    
    public static boolean updateWorkStatus( StaffWorkRegistrationBean info )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        
		String sqlString =
			"UPDATE data_working_staff SET work_id=" + SQLUtil.convertForSQL(info.getWorkId()) + "," +
			" update_date= " + SQLUtil.convertForSQLDateOnly(new Date()) +
			" WHERE delete_date is NULL" +
			" AND shop_id=" + SQLUtil.convertForSQL(info.getShopId()) +
			" AND working_date = " + SQLUtil.convertForSQLDateOnly(info.getWorkingDate()) +
			" AND staff_id=" + SQLUtil.convertForSQL(info.getStaffId());

		try
		{
			dbAccess.execute( sqlString );
		}
		catch( Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
        
        return true;
    }
    
    public static ArrayList getRegisteredStaffs( Date specifiedDate, int shopId )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      working_date");
        sql.append("     ,staff_id");
        sql.append("     ,work_id");
        sql.append(" from");
        sql.append("     data_working_staff");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("     and working_date = " + SQLUtil.convertForSQLDateOnly(specifiedDate));
        sql.append(" order by");
        sql.append("     staff_id");

        ArrayList<StaffWorkRegistrationBean> registeredStaffList = new ArrayList<StaffWorkRegistrationBean>();
        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sql.toString() );
            StaffWorkRegistrationBean regInfo = null;
            StaffBasicInfo basicInfo = null;
            StaffWorkStatusBean workStatus = null;
            
            while( result.next() )
            {
                regInfo = new StaffWorkRegistrationBean();
                basicInfo = StaffWorkRegistrationDAO.getStaffBasicInfo( result.getInt("staff_id") );
                regInfo.setBasicInfo( basicInfo );
                workStatus =  StaffWorkRegistrationDAO.getStaffWorkStatus( result.getInt("work_id") );
                regInfo.setWorkStatus( workStatus );
                regInfo.setWorkingDate( result.getDate( "working_date" ) );
                registeredStaffList.add( regInfo );
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return registeredStaffList;
    }
    
    public static int getWorkingStaffsTotal( Date specifiedDate, int shopId )
    {
        int total = 0;
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        
        String sqlQuery = "SELECT work_id FROM data_working_staff" +
                " WHERE delete_date IS null AND shop_id=" + SQLUtil.convertForSQL(shopId) +
                " AND working_date = " + SQLUtil.convertForSQLDateOnly(specifiedDate);

        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            while( result.next() )
            {
                int workId = result.getInt( "work_id" );
                if( StaffWorkRegistrationDAO.getWorkingFlag( workId ) == 1 )
                {
                    total++;
                }
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return total;
    }
    
    public static final int getWorkingFlag( int workId )
    {
        int workingFlag = 0;
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        
        String sqlQuery = "SELECT working FROM mst_work_status" +
                " WHERE delete_date IS null AND work_id=" + SQLUtil.convertForSQL(workId);
        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            if( result.next() )
            {   
                workingFlag = result.getInt( "working" );
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return workingFlag;
    }
    
    public static StaffBasicInfo getStaffBasicInfo( int staffId )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      staff_id");
        sql.append("     ,staff_no");
        sql.append("     ,staff_name1");
        sql.append("     ,staff_name2");
        sql.append("     ,shop_id");
        sql.append(" from");
        sql.append("     mst_staff");
        sql.append(" where");
        sql.append("     staff_id = " + SQLUtil.convertForSQL(staffId));

        StaffBasicInfo staffInfo = null;
        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sql.toString() );
            String staffName = null;
            if( result.next() )
            {
                staffInfo = new StaffBasicInfo();
                staffInfo.setStaffId( result.getInt( "staff_id" ) );
				staffInfo.setStaffNo( result.getString( "staff_no" ) );
                staffName = result.getString( "staff_name1" );
                staffName += " ";
                staffName += result.getString( "staff_name2" );
                staffInfo.setStaffName( staffName );
                staffInfo.setShopId( result.getInt( "shop_id" ) );  
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return staffInfo;
    }
    
    public static StaffWorkStatusBean getStaffWorkStatus( int workId )
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        String sqlQuery = "SELECT work_id, disp_name, working FROM mst_work_status" +
                " WHERE delete_date is null AND work_id=" + SQLUtil.convertForSQL(workId);
        StaffWorkStatusBean workStatus = null;
        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            
            if( result.next() )
            {
                workStatus = new StaffWorkStatusBean();
                workStatus.setDispName( result.getString( "disp_name" ) );
                workStatus.setWorkId( result.getInt( "work_id" ) );
                workStatus.setWorkingFlag( result.getInt( "working" ) );
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return workStatus;
    }

    public static ArrayList getStaffList( int shopId )
    {
        return getStaffList(shopId, false);
    }
    
    public static ArrayList getStaffList( int shopId, boolean isAllStaff )
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      staff_id");
        sql.append("     ,staff_no");
        sql.append("     ,staff_name1");
        sql.append("     ,staff_name2");
        sql.append("     ,shop_id");
        sql.append(" from");
        sql.append("     mst_staff");
        sql.append(" where");
        sql.append("     delete_date is null");
        
        if (!isAllStaff) {
            sql.append(" and shop_id = " + SQLUtil.convertForSQL(shopId));
        }

        sql.append(" order by");
        sql.append("      case when shop_id = " + SQLUtil.convertForSQL(shopId) + " then 0 else 1 end");
        sql.append("     ,case when display_seq is null then 1 else 0 end");
        sql.append("     ,display_seq");
        sql.append("     ,lpad(staff_no, 10, '0')");
        sql.append("     ,staff_id");

        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        ArrayList<StaffBasicInfo> staffList = new ArrayList<StaffBasicInfo>();
        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sql.toString() );
            StaffBasicInfo staffInfo = null;
            String staffName = null;
            while( result.next() )
            {
                staffInfo = new StaffBasicInfo();
                staffInfo.setStaffId( result.getInt( "staff_id" ) );
				staffInfo.setStaffNo( result.getString( "staff_no" ) );
                staffName = result.getString( "staff_name1" );
                staffName += " ";
                staffName += result.getString( "staff_name2" );
                staffInfo.setStaffName( staffName );
                staffInfo.setShopId( result.getInt( "shop_id" ) );                
                staffList.add( staffInfo ); 
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return staffList;
    }
    
    public static ArrayList getAllStaffWorkStatus()
    {
        ConnectionWrapper dbAccess = SystemInfo.getConnection();
        String sqlQuery = "SELECT work_id, disp_name, working FROM mst_work_status" +
                " WHERE delete_date is null ORDER BY work_id";
        ArrayList<StaffWorkStatusBean> workStatusList = new ArrayList<StaffWorkStatusBean>();
        
        try
        {
            ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
            StaffWorkStatusBean workStatus = null;
            while( result.next() )
            {
                workStatus = new StaffWorkStatusBean();
                workStatus.setDispName( result.getString( "disp_name" ) );
                workStatus.setWorkId( result.getInt( "work_id" ) );
                workStatus.setWorkingFlag( result.getInt( "working" ) );
                workStatusList.add( workStatus );
            }
            result.close();
        }
        catch( Exception e)
	{
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return workStatusList;
    }
    
}

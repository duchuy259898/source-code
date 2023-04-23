/*
 * PointCardLayoutData.java
 *
 * Created on 2008/09/01, 17:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.system.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
/**
 *
 * @author shiera.delusa
 */
public class PointCardLayoutData {
    //CLASS CONSTANTS:
    public final static int INVALID_TEMPLATE_ID = -1;
    public final static int INVALID_DISPLAY_SEQ = -1;
    
    //CLASS VARIABLES:
    ConnectionWrapper dbConnection;
    
    /** Creates a new instance of PointCardLayoutData and initializes the database connection class */
    public PointCardLayoutData() {
        this.dbConnection = SystemInfo.getConnection();
    }
    
    /**
     * Checks if the Date parameters (fromDate,toDate) overlaps with the dates
     * of the point card templates in the database.
     * This method is used for new templates. (insert method)
     * @param fromDate  from_date input
     * @param toDate    to_date input
     * @return <code>true</code> if one of the dates overlaps with an existing data
     *         <code>false</code> if both of the dates don't overlap with the existing ones
     */
    public boolean doToFromDatesExist( int shopId, Date fromDate, Date toDate ) {
        boolean bExist = false;
        
        String sqlQuery = "SELECT from_date, to_date FROM mst_pointcard_layout" +
            " WHERE shop_id = " + SQLUtil.convertForSQL(shopId) +
            " AND (" +
            "(from_date BETWEEN " +
            SQLUtil.convertForSQL(fromDate) + " AND " +
            SQLUtil.convertForSQL(toDate) + ")" +
            " OR (to_date BETWEEN " +
            SQLUtil.convertForSQL(fromDate) + " AND " +
            SQLUtil.convertForSQL(toDate) + ")" +
            " OR (" + SQLUtil.convertForSQL(fromDate) +
            " BETWEEN from_date AND to_date )" +
            ") AND delete_date IS NULL" +
            " ORDER BY display_seq";
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( sqlQuery );
            if( result.next() ) {
                bExist = true;
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            bExist = true;
        }
        return bExist;
    }
    
    /**
     * Checks if the Date parameters (fromDate,toDate) overlaps with the dates
     * of the point card templates in the database.
     * This method is used to update templates. (update method)
     * @param fromDate          from_date input
     * @param toDate            to_date input
     * @param currTemplateId    the template_id of the data to be updated
     * @return <code>true</code> if one of the dates overlaps with an existing data
     *         <code>false</code> if both of the dates don't overlap with the existing ones
     */
    public boolean doToFromDatesExist( int shopId, Date fromDate, Date toDate, int currTemplateId ) {
        boolean bExist = false;
        
        String sqlQuery = "SELECT from_date, to_date FROM mst_pointcard_layout" +
            " WHERE shop_id = " + SQLUtil.convertForSQL(shopId) +
            " AND (" +
            "(from_date BETWEEN " +
            SQLUtil.convertForSQL(fromDate) + " AND " +
            SQLUtil.convertForSQL(toDate) + ")" +
            " OR (to_date BETWEEN " +
            SQLUtil.convertForSQL(fromDate) + " AND " +
            SQLUtil.convertForSQL(toDate) + ")" +
            " OR (" + SQLUtil.convertForSQL(fromDate) +
            " BETWEEN from_date AND to_date )" +
            ") AND delete_date IS NULL" +
            " AND template_id!=" + SQLUtil.convertForSQL(currTemplateId) +
            " ORDER BY display_seq";
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( sqlQuery );
            if( result.next() ) {
                return true;
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            bExist = true;
        }
        return bExist;
    }
    
    /**
     * Returns the last (highest) display sequence number from the database.
     * @return the last (highest) display sequence number registered
     *         else, returns INVALID_DISPLAY_SEQ (-1) if an error occurs
     */
    public int getLastDisplaySequence(int shopId) {
        int nextDispSeq = PointCardLayoutData.INVALID_DISPLAY_SEQ;
        String queryString = "SELECT MAX(display_seq) FROM mst_pointcard_layout " +
                " WHERE shop_id = " + SQLUtil.convertForSQL(shopId) +
                " AND   delete_date IS NULL";
        
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( queryString );
            if( result.next() ) {
                nextDispSeq = result.getInt( 1 );
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        return nextDispSeq;
    }
    
    /**
     * Checks for duplicate display_seq. (display_seq must be unique)
     * @param dispSeq   the display_seq to check for uniqueness
     * @return <code>true</code> if dispSeq is unique
     *         <code>false</code> if the same value exists
     */
    private boolean isUniqueDisplaySeq( int shopId, int dispSeq ) {
        boolean bUnique = true;
        String queryString = "SELECT display_seq FROM mst_pointcard_layout" +
            " WHERE shop_id = " + SQLUtil.convertForSQL(shopId) +
            " AND delete_date IS NULL" +
            " AND display_seq=" + SQLUtil.convertForSQL(dispSeq);
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( queryString );
            if( result.next() ) {
                result.close();
                bUnique = false;
            }
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            bUnique = false;
        }
        return bUnique;
    }
    
    /**
     * Inserts a new point card layout/template into the database.
     * @param   data  the PointCardLayoutBean that contains the new information
     * @return  <code>true</code> if data has been successfully inserted into the database
     *          <code>false</code> if an error occured
     */
    public boolean insertNewLayout( PointCardLayoutBean data ) {
        try {
            this.dbConnection.begin();
            if( isUniqueDisplaySeq( data.getShopId(), data.getDisplaySeq() ) == false ) {
                this.moveDownDisplaySequence( data.getShopId(), data.getDisplaySeq() );
            }
            
            String comma = ",";
            
            String queryString =
                "INSERT INTO mst_pointcard_layout(shop_id,template_id,template_title,from_date,to_date," +
                "comment1,comment2,comment3,comment4,comment5,comment6,comment7,comment8,comment9,insert_date,update_date,display_seq)" +
                " select " +
                SQLUtil.convertForSQL(data.getShopId()) +  comma +
                "coalesce(max(template_id), 0)+1" + comma +
                SQLUtil.convertForSQL(data.getTemplateTitle()) +  comma +
                SQLUtil.convertForSQL(data.getFromDate()) + comma +
                SQLUtil.convertForSQL(data.getToDate())   + comma +
                SQLUtil.convertForSQL(data.getComment1()) +  comma +
                SQLUtil.convertForSQL(data.getComment2()) +  comma +
                SQLUtil.convertForSQL(data.getComment3()) +  comma +
                SQLUtil.convertForSQL(data.getComment4()) +  comma +
                SQLUtil.convertForSQL(data.getComment5()) +  comma +
                SQLUtil.convertForSQL(data.getComment6()) +  comma +
                SQLUtil.convertForSQL(data.getComment7()) +  comma +
                SQLUtil.convertForSQL(data.getComment8()) +  comma +
                SQLUtil.convertForSQL(data.getComment9()) +  comma +
                "current_timestamp" + comma +
                "current_timestamp" + comma +
                SQLUtil.convertForSQL(data.getDisplaySeq()) +
                "from mst_pointcard_layout";
            this.dbConnection.execute( queryString );
            this.dbConnection.commit();
        } catch( Exception e) {
            try {
                this.dbConnection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns a list of all point card layouts in the database. (Not yet deleted)
     * @return  PointCardLayoutBean collection of all the layouts
     */
    public ArrayList getPointCardLayouts(int shopId) {
        ArrayList<PointCardLayoutBean> pointCardLayoutList = new ArrayList<PointCardLayoutBean>();
        String queryString = "SELECT * from mst_pointcard_layout " +
                " WHERE shop_id = " + SQLUtil.convertForSQL(shopId) + 
                " AND delete_date IS NULL ORDER BY display_seq";
        
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( queryString );
            PointCardLayoutBean bean = null;
            while( result.next() ) {
                bean = new PointCardLayoutBean();
                bean.setShopId( result.getInt( "shop_id" ) );
                bean.setTemplateId( result.getInt( "template_id" ) );
                bean.setTemplateTitle( result.getString( "template_title" ) );
                bean.setFromDate( result.getDate( "from_date" ) );
                bean.setToDate( result.getDate( "to_date" ) );
                bean.setComment1( result.getString( "comment1" ) );
                bean.setComment2( result.getString( "comment2" ) );
                bean.setComment3( result.getString( "comment3" ) );
                bean.setComment4( result.getString( "comment4" ) );
                bean.setComment5( result.getString( "comment5" ) );
                bean.setComment6( result.getString( "comment6" ) );
                bean.setComment7( result.getString( "comment7" ) );
                bean.setComment8( result.getString( "comment8" ) );
                bean.setComment9( result.getString( "comment9" ) );
                bean.setDisplaySeq( result.getInt( "display_seq" ) );
                pointCardLayoutList.add( bean );
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return pointCardLayoutList;
    }
    
    /**
     * This is called by the delete function in CardLayoutPanel.  This doesn't
     * delete the data from the database, rather, this just set the layout's
     * delete_date field.
     * @param deleteData    the pointcard template to mark as "deleted"
     * @return <code>true</code>    if the data has been successfully marked as "deleted"
     *         <code>false</code>   if an error occured
     */
    public boolean deleteCardLayout( PointCardLayoutBean deleteData ) {
        String comma = ",";
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        Date today = new Date();
        String sqlString = "UPDATE mst_pointcard_layout SET" +
            " delete_date=current_timestamp" +
            " where shop_id = " + SQLUtil.convertForSQL(deleteData.getShopId()) +
            " and   template_id = " + SQLUtil.convertForSQL(deleteData.getTemplateId());
        try {
            this.dbConnection.begin();
            this.dbConnection.execute( sqlString );
            this.moveUpDisplaySequence( deleteData.getShopId(), deleteData.getDisplaySeq() );
            this.dbConnection.commit();
        } catch( Exception e) {
            try {
                this.dbConnection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }
    
    /**
     * Changes the rest of the display sequence to avoid "gaps" in display sequence numbering.
     * This must be called after deleting a card layout template
     * @param deletedDispSeq  The sequence number of the deleted template
     * @return <code>true</code>    if no error occured
     *         <code>false</code>   if an error occured
     */
    private boolean moveUpDisplaySequence( int shopId, int deletedDispSeq ) throws SQLException {
        String sqlString = "SELECT template_id, display_seq FROM mst_pointcard_layout" +
            " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
            " AND   delete_date IS NULL AND display_seq > " + SQLUtil.convertForSQL(deletedDispSeq) +
            " ORDER BY display_seq";
        
        ResultSetWrapper result = this.dbConnection.executeQuery( sqlString );
        while( result.next() ) {
            sqlString = "UPDATE mst_pointcard_layout SET display_seq=" + ( deletedDispSeq++ ) +
                " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
                " AND   template_id=" + result.getInt( "template_id" );
                this.dbConnection.execute( sqlString );
        }
        result.close();
        
        return true;
    }
    
    /**
     * If the new display sequence already exists, this moves down all the other sequence numbers.
     * This must be called after inserting or updating a card layout template
     * @param fromDispSeq  The sequence number of the template to be replaced
     * @return <code>true</code>    if no error occured
     *         <code>false</code>   if an error occured
     */
    private boolean moveDownDisplaySequence( int shopId, int fromDispSeq ) throws SQLException {
        String sqlString = "SELECT template_id, display_seq FROM mst_pointcard_layout" +
            " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
            " AND   delete_date IS NULL AND display_seq > " + SQLUtil.convertForSQL(fromDispSeq-1) +
            " ORDER BY display_seq";
        ResultSetWrapper result = this.dbConnection.executeQuery( sqlString );
        while( result.next() ) {
            sqlString = "UPDATE mst_pointcard_layout SET display_seq=" + ( ++fromDispSeq ) +
                " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
                " AND   template_id =" + result.getInt( "template_id" );
            this.dbConnection.execute( sqlString );
        }
        return true;
    }
    
    /**
     * If the new display sequence already exists, this moves down all the other sequence numbers.
     * This must be called before inserting or updating a card layout template
     * @param newDispSeq   The sequence number of the template to be replaced
     * @param templateID    The template ID of the the data to update/relocate
     */
    private boolean rearrangeDisplaySequence( int shopId, int newDispSeq, int templateId ) throws SQLException {
        int currentDispSeq = getDisplaySeq( shopId, templateId );
        String sqlString = null;
        
        if( newDispSeq == currentDispSeq ) {
            return true;
        } else if( newDispSeq < currentDispSeq ) {
            sqlString = "SELECT template_id, display_seq FROM mst_pointcard_layout" +
                " WHERE delete_date IS NULL" +
                " AND (display_seq BETWEEN " + SQLUtil.convertForSQL(newDispSeq) +
                " AND " + SQLUtil.convertForSQL(currentDispSeq) + ")" +
                " AND shop_id  = "  + SQLUtil.convertForSQL(shopId) +
                " AND template_id!=" + SQLUtil.convertForSQL(templateId) +
                " ORDER BY display_seq";
        } else {
            sqlString = "SELECT template_id, display_seq FROM mst_pointcard_layout" +
                " WHERE delete_date IS NULL" +
                " AND (display_seq BETWEEN " + SQLUtil.convertForSQL(currentDispSeq) +
                " AND " + SQLUtil.convertForSQL(newDispSeq) + ")" +
                " AND shop_id  = "  + SQLUtil.convertForSQL(shopId) +
                " AND template_id!=" + SQLUtil.convertForSQL(templateId) +
                " ORDER BY display_seq";
        }
        
        ResultSetWrapper result = this.dbConnection.executeQuery( sqlString );
        if( newDispSeq < currentDispSeq ) {
            while( result.next() ) {
                sqlString = "UPDATE mst_pointcard_layout SET display_seq=" + ( ++newDispSeq ) +
                    " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
                    " AND   template_id=" + result.getInt( "template_id" );
                this.dbConnection.execute( sqlString );
            }
        } else {
            int dispSeq = this.getDisplaySeq( shopId, templateId );
            while( result.next() ) {
                sqlString = "UPDATE mst_pointcard_layout SET display_seq=" + ( dispSeq++ ) +
                    " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
                    " AND   template_id=" + result.getInt( "template_id" );
                this.dbConnection.execute( sqlString );
            }
        }
        return true;
    }
    
    /**
     * Gets the display_seq of the pointcard layout specified by the templateId
     * @param templateId the template_id of the pointcard layout to search for
     * @return INVALID_DISPLAY_SEQ (-1) when an error occurs
     */
    private int getDisplaySeq( int shopId, int templateId ) {
        int currentDispSeq = INVALID_DISPLAY_SEQ;
        String sqlString = "SELECT display_seq FROM mst_pointcard_layout " +
            " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
            " AND   template_id=" + SQLUtil.convertForSQL(templateId);
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( sqlString );
            if( result.next() ) {
                currentDispSeq = result.getInt( "display_seq" );
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return currentDispSeq;
    }
    
    /**
     * Updates a pointcard layout in the database.
     * @param updatedData   the pointcard layout to be updated.
     *                      This contains the new details of the pointcard layout.
     * @return <code>true</code>    if updated successfully
     *         <code>false</code>   if an error occured
     */
    public boolean updateCardLayout( PointCardLayoutBean updatedData ) {
        try {
            this.dbConnection.begin();
            if( isUniqueDisplaySeq( updatedData.getShopId(), updatedData.getDisplaySeq() ) == false ) {
                if( this.rearrangeDisplaySequence( updatedData.getShopId(), updatedData.getDisplaySeq(),
                    updatedData.getTemplateId() ) == false )
                    return false;
            }
            
            String comma = ",";
            
            if( this.isTheNullTemplate( updatedData.getShopId(), updatedData.getTemplateId() ) ) {
                updatedData.setFromDate(null);
                updatedData.setToDate(null);
            }
            
            String sqlString = "UPDATE mst_pointcard_layout SET" +
                " template_title="  + SQLUtil.convertForSQL(updatedData.getTemplateTitle()) + comma +
                " from_date="       + SQLUtil.convertForSQL(updatedData.getFromDate()) + comma +
                " to_date="         + SQLUtil.convertForSQL(updatedData.getToDate())   + comma +
                " comment1="        + SQLUtil.convertForSQL(updatedData.getComment1()) +  comma +
                " comment2="        + SQLUtil.convertForSQL(updatedData.getComment2()) +  comma +
                " comment3="        + SQLUtil.convertForSQL(updatedData.getComment3()) +  comma +
                " comment4="        + SQLUtil.convertForSQL(updatedData.getComment4()) +  comma +
                " comment5="        + SQLUtil.convertForSQL(updatedData.getComment5()) +  comma +
                " comment6="        + SQLUtil.convertForSQL(updatedData.getComment6()) +  comma +
                " comment7="        + SQLUtil.convertForSQL(updatedData.getComment7()) +  comma +
                " comment8="        + SQLUtil.convertForSQL(updatedData.getComment8()) +  comma +
                " comment9="        + SQLUtil.convertForSQL(updatedData.getComment9()) +  comma +
                " update_date=current_timestamp" + comma +
                " display_seq="     + updatedData.getDisplaySeq() +
                " WHERE shop_id  = "  + SQLUtil.convertForSQL(updatedData.getShopId()) +
                " AND   template_id=" + SQLUtil.convertForSQL(updatedData.getTemplateId());
            try {
                this.dbConnection.execute( sqlString );
                this.dbConnection.commit();
            } catch( Exception e) {
                this.dbConnection.rollback();
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    /**
     * Checks the database for the existence of the pointcard layout that has
     * <code>null</code> values for from_date and/or to_date.
     * @return  <code>true</code> if the layout exists
     *          <code>true</code> if the layout doesn't exist
     */
    public boolean isNullTemplatePresent(int shopId) {
        String queryString =
            "SELECT from_date,to_date FROM mst_pointcard_layout" +
            " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
            " AND   from_date IS NULL AND to_date IS NULL";
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( queryString );
            if( result.next() ) {
                result.close();
                return true;
            }
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return true;
        }
        return false;
    }
    
    public boolean isTheNullTemplate( int shopId, int templateId ) {
        String queryString =
            "SELECT from_date,to_date FROM mst_pointcard_layout" +
            " WHERE shop_id  = "  + SQLUtil.convertForSQL(shopId) +
            " AND   from_date IS NULL AND to_date IS NULL AND template_id="
            + SQLUtil.convertForSQL(templateId);
        try {
            ResultSetWrapper result = this.dbConnection.executeQuery( queryString );
            if( result.next() ) {
                result.close();
                return true;
            }
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return true;
        }
        return false;
    }
    
}

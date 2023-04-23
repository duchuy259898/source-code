/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author lvtu
 */
public class MstKANZASHIShift {
    
    Integer     shopID      = null;
    String      mediaID     = null;
    Integer     shiftID     = null;
    String      name        = null;
    //IVS_PTQUANG 2016/09/15 New request #54718
    private String      start_time        = null;
    //IVS_PTQUANG 2016/09/15 New request #54718
    private String      end_time          = null;
    Integer     sposShiftID   = null;

    public Integer getSposShiftID() {
        return sposShiftID;
    }

    public void setSposShiftID(Integer sposShiftID) {
        this.sposShiftID = sposShiftID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public Integer getShiftID() {
        return shiftID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    //IVS_PTQUANG 2016/09/15 New request #54718
    /**
     * @return the start_time
     */
    public String getStart_time() {
        return start_time;
    }
    //IVS_PTQUANG 2016/09/15 New request #54718
    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
    //IVS_PTQUANG 2016/09/15 New request #54718
    /**
     * @return the end_time
     */
    public String getEnd_time() {
        return end_time;
    }
    //IVS_PTQUANG 2016/09/15 New request #54718
    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
   
    @Override
    public String toString() {
        return this.name;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.shopID = rs.getInt("shop_id");
        this.mediaID = rs.getString("media_id");
        this.shiftID = rs.getInt("shift_id");
        this.name = rs.getString("name");
        //IVS_PTQUANG start 2016/09/15 New request #54718
        this.start_time = rs.getString("start_time");
        this.end_time = rs.getString("end_time");
        //IVS_PTQUANG end 2016/09/15 New request #54718
        this.sposShiftID = rs.getInt("spos_shift_id");
    }

    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (isExists(con)) {
            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }
        } else if (con.executeUpdate(this.getInsertSQL()) != 1) {
            return false;
        }

        return true;
    }
    
    public boolean delete(ConnectionWrapper con) throws SQLException {
        if (isExists(con)) {
            if (con.executeUpdate(this.getDeleteSQL()) != 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getShiftID()== null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectIDSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<MstKANZASHIShift> loadAll(ConnectionWrapper con) throws SQLException {
        ArrayList<MstKANZASHIShift> arrShift = new ArrayList<MstKANZASHIShift>();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        MstKANZASHIShift shift ;
        while (rs.next()) {
            shift = new MstKANZASHIShift();
            shift.setData(rs);
            arrShift.add(shift);
        }
        rs.close();
        
        return arrShift;
    }

    public String loadMaxDate(ConnectionWrapper con) throws SQLException {
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectMaxDateSQL());
        String maxDate = null ;
        while (rs.next()) {
            maxDate = rs.getString("max_date");
        }
        rs.close();
        
        return maxDate;
    }
        
    private String getSelectSQL() {
        return "select * \n"
                + "from mst_kanzashi_shift \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and delete_date is null \n";
    }

     private String getSelectIDSQL() {
        return "select * \n"
                + "from mst_kanzashi_shift \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n" 
                + "and shift_id = " + SQLUtil.convertForSQL(this.shiftID) + "\n";
    }
    
    private String getInsertSQL() {
        return "insert into mst_kanzashi_shift(shop_id, media_id, shift_id, name, start_time, end_time, spos_shift_id, insert_date, update_date, delete_date)\n"
                + "values (\n"
                + SQLUtil.convertForSQL(this.shopID) + ",\n"
                + SQLUtil.convertForSQL(this.mediaID) + ",\n"
                + SQLUtil.convertForSQL(this.shiftID) + ",\n"
                + SQLUtil.convertForSQL(this.name) + ",\n"
                //IVS_PTQUANG start 2016/09/15 New request #54718
                + SQLUtil.convertForSQL(this.start_time) + ",\n"
                + SQLUtil.convertForSQL(this.end_time) + ",\n"
                //IVS_PTQUANG end 2016/09/15 New request #54718
                + SQLUtil.convertForSQL(this.sposShiftID) + ",\n"
                + "current_timestamp, current_timestamp, null\n)";
    }

    /**
     * Update•¶‚ðŽæ“¾‚·‚éB
     *
     * @return Update•¶
     */
    private String getUpdateSQL() {
        return "update mst_kanzashi_shift\n"
                + "set\n"
                + "name = " + SQLUtil.convertForSQL(this.name) + ",\n"
                //IVS_PTQUANG start 2016/09/15 New request #54718
                + "\"start_time\" = " + SQLUtil.convertForSQL(this.start_time) + ",\n"
                + "\"end_time\" = " + SQLUtil.convertForSQL(this.end_time) + ",\n"
                //IVS_PTQUANG end 2016/09/15 New request #54718
                + "spos_shift_id = " + SQLUtil.convertForSQL(this.sposShiftID) + "\n"
                + " , update_date = current_timestamp \n"
                + " , delete_date = null \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and shift_id = " + SQLUtil.convertForSQL(this.shiftID) + "\n";
    }
    
    private String getDeleteSQL()
    {
        return "update mst_kanzashi_shift\n"
                + "set \n"
                + "update_date = current_timestamp, \n"
                + "delete_date = current_timestamp \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and shift_id = " + SQLUtil.convertForSQL(this.shiftID) + "\n";
    }
    
    private String getSelectMaxDateSQL() {
        return "SELECT max(max_date) AS max_date \n"
                + "FROM \n"
                + "(SELECT CASE \n"
                + "WHEN insert_date > delete_date \n"
                +   "OR delete_date IS NULL THEN max(insert_date) \n"
                +   "ELSE max(delete_date) \n"
                +   "END AS max_date \n"
                + "from mst_kanzashi_shift \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "GROUP BY insert_date,\n"
                + "delete_date) AS dt";
    }

}

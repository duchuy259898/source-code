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
public class MstKANZASHIStaff {
    Integer     shopID          = null;
    String      mediaID         = null;
    Integer     staffID         = null;
    String      name            = null;
    Integer     sposStaffID     = null;

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

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSposStaffID() {
        return sposStaffID;
    }

    public void setSposStaffID(Integer sposStaffID) {
        this.sposStaffID = sposStaffID;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.shopID = rs.getInt("shop_id");
        this.mediaID = rs.getString("media_id");
        this.staffID = rs.getInt("staff_id");
        this.name = rs.getString("name");
        this.sposStaffID = rs.getInt("spos_staff_id");
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
        if (this.getStaffID() == null) {
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

    public ArrayList<MstKANZASHIStaff> loadAll(ConnectionWrapper con) throws SQLException {
        ArrayList<MstKANZASHIStaff> arrStaff = new ArrayList<MstKANZASHIStaff>();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        MstKANZASHIStaff staff ;
        while (rs.next()) {
            staff = new MstKANZASHIStaff();
            staff.setData(rs);
            arrStaff.add(staff);
        }
        rs.close();
        
        return arrStaff;
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
                + "from mst_kanzashi_staff \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and delete_date is null \n";
    }
    
    private String getSelectIDSQL() {
        return "select * \n"
                + "from mst_kanzashi_staff \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n" 
                + "and staff_id = " + SQLUtil.convertForSQL(this.staffID) + "\n";
    }

    private String getInsertSQL() {
        return "insert into mst_kanzashi_staff\n"
                + "(shop_id, media_id, staff_id, name, spos_staff_id,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.shopID) + ",\n"
                + SQLUtil.convertForSQL(this.mediaID) + ",\n"
                + SQLUtil.convertForSQL(this.staffID) + ",\n"
                + SQLUtil.convertForSQL(this.name) + ",\n"
                + SQLUtil.convertForSQL(this.sposStaffID) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update•¶‚ðŽæ“¾‚·‚éB
     *
     * @return Update•¶
     */
    private String getUpdateSQL() {
        return "update mst_kanzashi_staff\n"
                + "set\n"
                + "name = " + SQLUtil.convertForSQL(this.name) + ",\n"
                + "spos_staff_id = " + SQLUtil.convertForSQL(this.sposStaffID) + "\n"
                + " , update_date = current_timestamp \n"
                + " , delete_date = null \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and staff_id = " + SQLUtil.convertForSQL(this.staffID) + "\n";
    }
    
    private String getDeleteSQL()
    {
        return "update mst_kanzashi_staff\n"
                + "set \n"
                + "update_date = current_timestamp, \n"
                + "delete_date = current_timestamp \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and staff_id = " + SQLUtil.convertForSQL(this.staffID) + "\n";
    }
    
    private String getSelectMaxDateSQL() {
        return "SELECT max(max_date) AS max_date \n"
                + "FROM \n"
                + "(SELECT CASE \n"
                + "WHEN insert_date > delete_date \n"
                +   "OR delete_date IS NULL THEN max(insert_date) \n"
                +   "ELSE max(delete_date) \n"
                +   "END AS max_date \n"
                + "from mst_kanzashi_staff \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "GROUP BY insert_date,\n"
                + "delete_date) AS dt";
    }
}

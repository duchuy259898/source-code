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
public class MstKANZASHIBed {

    Integer         shopID      = null;
    String          mediaID     = null;
    Integer         bedID       = null;
    String          name        = null;
    Integer         sposBedID   = null;

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

    public Integer getBedID() {
        return bedID;
    }

    public void setBedID(Integer bedID) {
        this.bedID = bedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSposBedID() {
        return sposBedID;
    }

    public void setSposBedID(Integer sposBedID) {
        this.sposBedID = sposBedID;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    public void setData(ResultSetWrapper rs) throws SQLException {
        this.shopID = rs.getInt("shop_id");
        this.mediaID = rs.getString("media_id");
        this.bedID = rs.getInt("bed_id");
        this.name = rs.getString("name");
        this.sposBedID = rs.getInt("spos_bed_id");
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
        if (this.getBedID() == null) {
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

    public ArrayList<MstKANZASHIBed> loadAll(ConnectionWrapper con) throws SQLException {
        ArrayList<MstKANZASHIBed> arrBed = new ArrayList<MstKANZASHIBed>();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        MstKANZASHIBed bed ;
        while (rs.next()) {
            bed = new MstKANZASHIBed();
            bed.setData(rs);
            arrBed.add(bed);
        }
        rs.close();
        
        return arrBed;
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
                + "from mst_kanzashi_bed \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and delete_date is null \n";
    }
    
     private String getSelectIDSQL() {
        return "select * \n"
                + "from mst_kanzashi_bed \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n" 
                + "and bed_id = " + SQLUtil.convertForSQL(this.bedID) + "\n";
    }

    private String getInsertSQL() {
        return "insert into mst_kanzashi_bed\n"
                + "(shop_id, media_id, bed_id, name, spos_bed_id,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.shopID) + ",\n"
                + SQLUtil.convertForSQL(this.mediaID) + ",\n"
                + SQLUtil.convertForSQL(this.bedID) + ",\n"
                + SQLUtil.convertForSQL(this.name) + ",\n"
                + SQLUtil.convertForSQL(this.sposBedID) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update•¶‚ðŽæ“¾‚·‚éB
     *
     * @return Update•¶
     */
    private String getUpdateSQL() {
        return "update mst_kanzashi_bed\n"
                + "set\n"
                + "name = " + SQLUtil.convertForSQL(this.name) + ",\n"
                + "spos_bed_id = " + SQLUtil.convertForSQL(this.sposBedID) + "\n"
                + " , update_date = current_timestamp \n"
                + " , delete_date = null \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and bed_id = " + SQLUtil.convertForSQL(this.bedID) + "\n";
    }
    
    private String getDeleteSQL()
    {
        return "update mst_kanzashi_bed\n"
                + "set \n"
                + "update_date = current_timestamp, \n"
                + "delete_date = current_timestamp \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and bed_id = " + SQLUtil.convertForSQL(this.bedID) + "\n";
    }
    
    private String getSelectMaxDateSQL() {
        return "SELECT max(max_date) AS max_date \n"
                + "FROM \n"
                + "(SELECT CASE \n"
                + "WHEN insert_date > delete_date \n"
                +   "OR delete_date IS NULL THEN max(insert_date) \n"
                +   "ELSE max(delete_date) \n"
                +   "END AS max_date \n"
                + "from mst_kanzashi_bed \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "GROUP BY insert_date,\n"
                + "delete_date) AS dt";
    }

}

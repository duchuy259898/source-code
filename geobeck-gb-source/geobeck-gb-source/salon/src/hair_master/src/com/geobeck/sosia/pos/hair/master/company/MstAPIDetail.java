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
public class MstAPIDetail {
    Integer     apiID           = null;
    String      mediaID         = "";
    String      mediaName       = "";
    Integer     displaySeq      = 0;

    public Integer getApiID() {
        return apiID;
    }

    public void setApiID(Integer apiID) {
        this.apiID = apiID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String MediaID) {
        this.mediaID = MediaID;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.apiID = rs.getInt("api_id");
        this.mediaID = rs.getString("media_id");
        this.mediaName = rs.getString("media_name");
        this.displaySeq = rs.getInt("display_seq");
    }

    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getApiID() == null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean load(ConnectionWrapper con) throws SQLException
    {
            if (this.getApiID()== null || this.getApiID() < 1) return false;

            if (con == null) return false;

            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

            if (rs.next()) {
                this.setData(rs);
                return true;
            }

            return false;
    }

    private String getSelectSQL() {
        return "select * \n"
                + "from mst_api_detail \n"
                + "where api_id = " + SQLUtil.convertForSQL(this.apiID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n";
    }
    
    @Override
    public String toString(){
        return mediaName;
    }
 
}

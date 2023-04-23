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

/**
 *
 * @author lvtu
 */
public class MstKANZASHIMedia {
    
    Integer     shopID      = null;
    String      mediaID     = null;
    boolean     useFlg      = true;
    String      loginID     = null;
    String      password    = null;
    String      storeID     = null;
    Integer     pullStatus  = null;
    
    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
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

    public boolean isUseFlg() {
        return useFlg;
    }

    public void setUseFlg(boolean useFlg) {
        this.useFlg = useFlg;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the pullStatus
     */
    public Integer getPullStatus() {
        return pullStatus;
    }

    /**
     * @param pullStatus the pullStatus to set
     */
    public void setPullStatus(Integer pullStatus) {
        this.pullStatus = pullStatus;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.shopID = rs.getInt("shop_id");
        this.mediaID = rs.getString("media_id");
        this.useFlg = rs.getBoolean("use_flg");
        this.loginID = rs.getString("login_id");
        this.password = rs.getString("password");
        this.storeID  = rs.getString("store_id");
        this.pullStatus = rs.getInt("pull_status");
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

    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getShopID() == null && this.getMediaID() == null) {
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

    public boolean load(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.setData(rs);
            rs.close();
            return true;
        }else {
            this.shopID = null;
            this.mediaID = null;
        }

        rs.close();
        return false;
    }
    
    //nhtvinh start add 20161021 New request #54239
    public Integer getSyncStatus(ConnectionWrapper con) {
        try{
            if (null != con) {

                ResultSetWrapper rs = con.executeQuery(this.getSyncStatusSql());

                if (rs.next()) {
                    return rs.getInt("sync_status");
                }
            }
        }catch(Exception e) {
            return null;
        }
        return null;
    }

    private String getSyncStatusSql() {
        return "select sync_status \n"
                + "from mst_kanzashi_media \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n";
    }
    //nhtvinh end add 20161021 New request #54239
    
    private String getSelectSQL() {
        return "select * \n"
                + "from mst_kanzashi_media \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n";
    }

    private String getInsertSQL() {
        return "insert into mst_kanzashi_media\n"
                + "(shop_id, media_id, use_flg, login_id, password, store_id,\n"
                + "insert_date, update_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.shopID) + ",\n"
                + SQLUtil.convertForSQL(this.mediaID) + ",\n"
                + SQLUtil.convertForSQL(this.useFlg) + ",\n"
                + SQLUtil.convertForSQL(this.loginID) + ",\n"
                + SQLUtil.convertForSQL(this.password) + ",\n"
                + SQLUtil.convertForSQL(this.storeID) + ",\n"
                + "current_timestamp, current_timestamp\n";
    }

    /**
     * Updateï∂ÇéÊìæÇ∑ÇÈÅB
     *
     * @return Updateï∂
     */
    private String getUpdateSQL() {
        return "update mst_kanzashi_media\n"
                + "set\n"
                + "use_flg = " + SQLUtil.convertForSQL(this.useFlg) + ",\n"
                + "login_id = " + SQLUtil.convertForSQL(this.loginID) + ",\n"
                + "password = " + SQLUtil.convertForSQL(this.password) + ",\n"
                + "store_id = " + SQLUtil.convertForSQL(this.storeID) + "\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n";
    }
}

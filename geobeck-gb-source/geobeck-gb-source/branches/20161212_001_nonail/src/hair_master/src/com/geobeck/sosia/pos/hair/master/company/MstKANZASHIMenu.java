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
public class MstKANZASHIMenu {
    Integer     shopID          = null;
    //String      mediaID         = null;
    Integer     menuID          = null;
    String      name            = null;
    Integer      price          = null;
    String      operationTime   = null;
    Integer     sposMenuID      = null;
    Integer     technicLassID   = null;

    public Integer getTechnicLassID() {
        return technicLassID;
    }

    public void setTechnicLassID(Integer technicLassID) {
        this.technicLassID = technicLassID;
    }

    public Integer getSposMenuID() {
        return sposMenuID;
    }

    public void setSposMenuID(Integer sposMenuID) {
        this.sposMenuID = sposMenuID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

//    public String getMediaID() {
//        return mediaID;
//    }
//
//    public void setMediaID(String mediaID) {
//        this.mediaID = mediaID;
//    }

    public Integer getMenuID() {
        return menuID;
    }

    public void setMenuID(Integer menuID) {
        this.menuID = menuID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.shopID = rs.getInt("shop_id");
        //this.mediaID = rs.getString("media_id");
        this.menuID = rs.getInt("menu_id");
        this.name = rs.getString("name");
        this.price = rs.getInt("price");
        this.operationTime = rs.getString("operation_time");
        this.sposMenuID = rs.getInt("spos_menu_id");
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
        if (this.getMenuID()== null) {
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

    public ArrayList<MstKANZASHIMenu> loadAll(ConnectionWrapper con) throws SQLException {
        ArrayList<MstKANZASHIMenu> arrMenu = new ArrayList<MstKANZASHIMenu>();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        MstKANZASHIMenu menu ;
        while (rs.next()) {
            menu = new MstKANZASHIMenu();
            menu.setData(rs);
            arrMenu.add(menu);
        }
        rs.close();
        
        return arrMenu;
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
                + "from mst_kanzashi_menu \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and delete_date is null \n";
    }
    
    private String getSelectIDSQL() {
        return "select * \n"
           + "from mst_kanzashi_menu \n"
           + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
           //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n" 
           + "and menu_id = " + SQLUtil.convertForSQL(this.menuID) + "\n";
    }

    private String getInsertSQL() {
        return "insert into mst_kanzashi_menu\n"
                + "(shop_id, menu_id, name, price, operation_time, spos_menu_id,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.shopID) + ",\n"
                //+ SQLUtil.convertForSQL(this.mediaID) + ",\n"
                + SQLUtil.convertForSQL(this.menuID) + ",\n"
                + SQLUtil.convertForSQL(this.name) + ",\n"
                + SQLUtil.convertForSQL(this.price) + ",\n"
                + SQLUtil.convertForSQL(this.operationTime) + ",\n"
                + SQLUtil.convertForSQL(this.sposMenuID) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update•¶‚ðŽæ“¾‚·‚éB
     *
     * @return Update•¶
     */
    private String getUpdateSQL() {
        return "update mst_kanzashi_menu\n"
                + "set\n"
                + "name = " + SQLUtil.convertForSQL(this.name) + ",\n"
                + "spos_menu_id = " + SQLUtil.convertForSQL(this.sposMenuID) + "\n"
                + " , update_date = current_timestamp \n"
                + " , delete_date = null \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and menu_id = " + SQLUtil.convertForSQL(this.menuID) + "\n";
    }
    
    private String getDeleteSQL()
    {
        return "update mst_kanzashi_menu\n"
                + "set \n"
                + "update_date = current_timestamp, \n"
                + "delete_date = current_timestamp \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "and menu_id = " + SQLUtil.convertForSQL(this.menuID) + "\n";
    }
    
    private String getSelectMaxDateSQL() {
        return "SELECT max(max_date) AS max_date \n"
                + "FROM \n"
                + "(SELECT CASE \n"
                + "WHEN insert_date > delete_date \n"
                +   "OR delete_date IS NULL THEN max(insert_date) \n"
                +   "ELSE max(delete_date) \n"
                +   "END AS max_date \n"
                + "from mst_kanzashi_menu \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n"
                //+ "and media_id = " + SQLUtil.convertForSQL(this.mediaID) + "\n"
                + "GROUP BY insert_date,\n"
                + "delete_date) AS dt";
    }
}

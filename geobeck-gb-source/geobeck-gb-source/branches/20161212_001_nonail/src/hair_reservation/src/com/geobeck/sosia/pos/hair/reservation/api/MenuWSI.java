/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ivs
 */
package com.geobeck.sosia.pos.hair.reservation.api;

public class MenuWSI {

    private String menu_id = "";
    private String menu_name = "";
    private String staff_name = "";
    private String price;
    public MenuWSI() {
        menu_id = "";
        menu_name = "";
        staff_name = "";
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuId() {
        return menu_id;
    }

    public void setMenuId(String menuId) {
        this.menu_id = menuId;
    }

    public String getMenuName() {
        return menu_name;
    }

    public void setMenuName(String menuName) {
        this.menu_name = menuName;
    }

    public String getStaffName() {
        return staff_name;
    }

    public void setStaffName(String staffName) {
        this.staff_name = staffName;
    }

}

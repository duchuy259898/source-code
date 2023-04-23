/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

/**
 *
 * @author ivs
 */
public class ComingInfo {

    private String coming_id;
    private String coming_date;
    private String coming_time;
    private String coming_status;
    private String reserve_route_id;
    private String reserve_type_cd;
    private String stylist_id;
    private String stylist_name;
    private String sb_customer_id;
    private String customer_no;
    private String customer_name;
    private String customer_name_kana;
    private CustomerTel customer_tel;
    private String customer_status;
    private String payment_price;
    private String use_point;
    private String last_upd_datetime;
   private MenuCouponInfo menu_coupon_info;

    public MenuCouponInfo getMenu_coupon_info() {
        return menu_coupon_info;
    }

    public void setMenu_coupon_info(MenuCouponInfo menu_coupon_info) {
        this.menu_coupon_info = menu_coupon_info;
    }
    
    public String getComing_id() {
        return coming_id;
    }

    public void setComing_id(String coming_id) {
        this.coming_id = coming_id;
    }

    public String getComing_date() {
        return coming_date;
    }

    public void setComing_date(String coming_date) {
        this.coming_date = coming_date;
    }

    public String getComing_time() {
        return coming_time;
    }

    public void setComing_time(String coming_time) {
        this.coming_time = coming_time;
    }

    public String getComing_status() {
        return coming_status;
    }

    public void setComing_status(String coming_status) {
        this.coming_status = coming_status;
    }

    public String getReserve_route_id() {
        return reserve_route_id;
    }

    public void setReserve_route_id(String reserve_route_id) {
        this.reserve_route_id = reserve_route_id;
    }

    public String getReserve_type_cd() {
        return reserve_type_cd;
    }

    public void setReserve_type_cd(String reserve_type_cd) {
        this.reserve_type_cd = reserve_type_cd;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public void setStylist_id(String stylist_id) {
        this.stylist_id = stylist_id;
    }

    public String getStylist_name() {
        return stylist_name;
    }

    public void setStylist_name(String stylist_name) {
        this.stylist_name = stylist_name;
    }

    public String getSb_customer_id() {
        return sb_customer_id;
    }

    public void setSb_customer_id(String sb_customer_id) {
        this.sb_customer_id = sb_customer_id;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_name_kana() {
        return customer_name_kana;
    }

    public void setCustomer_name_kana(String customer_name_kana) {
        this.customer_name_kana = customer_name_kana;
    }

    public CustomerTel getCustomer_tel() {
        return customer_tel;
    }

    public void setCustomer_tel(CustomerTel customer_tel) {
        this.customer_tel = customer_tel;
    }

   

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getPayment_price() {
        return payment_price;
    }

    public void setPayment_price(String payment_price) {
        this.payment_price = payment_price;
    }

    public String getUse_point() {
        return use_point;
    }

    public void setUse_point(String use_point) {
        this.use_point = use_point;
    }

    public String getLast_upd_datetime() {
        return last_upd_datetime;
    }

    public void setLast_upd_datetime(String last_upd_datetime) {
        this.last_upd_datetime = last_upd_datetime;
    }

    public ComingInfo(String coming_id, String coming_date, String coming_time, String coming_status, String reserve_route_id, String reserve_type_cd, String stylist_id, String stylist_name, String sb_customer_id, String customer_no, String customer_name, String customer_name_kana, CustomerTel customer_tel,  String custmoer_status, String payment_price, String use_point, String last_upd_datetime, MenuCouponInfo menu_coupon_info) {
        this.coming_id = coming_id;
        this.coming_date = coming_date;
        this.coming_time = coming_time;
        this.coming_status = coming_status;
        this.reserve_route_id = reserve_route_id;
        this.reserve_type_cd = reserve_type_cd;
        this.stylist_id = stylist_id;
        this.stylist_name = stylist_name;
        this.sb_customer_id = sb_customer_id;
        this.customer_no = customer_no;
        this.customer_name = customer_name;
        this.customer_name_kana = customer_name_kana;
        this.customer_tel = customer_tel;
       
        this.customer_status = custmoer_status;
        this.payment_price = payment_price;
        this.use_point = use_point;
        this.last_upd_datetime = last_upd_datetime;
        this.menu_coupon_info = menu_coupon_info;
    }

    public ComingInfo() {
        this.coming_id = "";
        this.coming_date = "";
        this.coming_time = "";
        this.coming_status = "";
        this.reserve_route_id = "";
        this.reserve_type_cd = "";
        this.stylist_id = "";
        this.stylist_name = "";
        this.sb_customer_id = "";
        this.customer_no = "";
        this.customer_name = "";
        this.customer_name_kana = "";
        this.customer_tel = new CustomerTel();
        this.customer_status = "";
        this.payment_price = "";
        this.use_point = "";
        this.last_upd_datetime = "";
        this.menu_coupon_info = new MenuCouponInfo();
    }
    
    
}

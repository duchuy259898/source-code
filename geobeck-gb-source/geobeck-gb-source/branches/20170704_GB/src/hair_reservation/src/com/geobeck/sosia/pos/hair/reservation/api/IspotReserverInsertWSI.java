/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ivs
 */
package com.geobeck.sosia.pos.hair.reservation.api;

import java.util.ArrayList;

public class IspotReserverInsertWSI {

    private String shop_id = "";
    private String sosia_reservation_id = "";
    private String entry_date = "";
    private String reserve_date = "";
    private ArrayList<MenuWSI> menu ;
    private String adjusts_price = "";
    private String payment_price = "";
    private String settlement_type = "";
    private String point_issue = "";
    private String user_id = "";
    private String email = "";
    private String phone = "";
    private String name = "";
    private String name_f = "";
    private String sex;
    private String message = "";

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public IspotReserverInsertWSI() {
        shop_id = "";
        sosia_reservation_id = "";
        entry_date = "";
        reserve_date = "";
        menu = new ArrayList<MenuWSI>();
        adjusts_price = "";
        payment_price = "";
        settlement_type = "";
        point_issue = "";
        user_id = "";
        email = "";
        phone = "";
        name = "";
        name_f = "";
        message = "";
    }

    public String getShopId() {
        return shop_id;
    }

    public void setShopId(String shopId) {
        this.shop_id = shopId;
    }

    public String getSosiaReservationId() {
        return sosia_reservation_id;
    }

    public void setSosiaReservationId(String sosiaReservationId) {
        this.sosia_reservation_id = sosiaReservationId;
    }

    public String getEntryDate() {
        return entry_date;
    }

    public void setEntryDate(String entryDate) {
        this.entry_date = entryDate;
    }

    public String getReserveDate() {
        return reserve_date;
    }

    public void setReserveDate(String reserveDate) {
        this.reserve_date = reserveDate;
    }

    public ArrayList<MenuWSI> getMenues() {
        return menu;
    }

    public void setMenues(ArrayList<MenuWSI> menues) {
        this.menu = menues;
    }

    public String getAdjustsPrice() {
        return adjusts_price;
    }

    public void setAdjustsPrice(String adjustsPrice) {
        this.adjusts_price = adjustsPrice;
    }

    public String getPaymentPrice() {
        return payment_price;
    }

    public void setPaymentPrice(String paymentPrice) {
        this.payment_price = paymentPrice;
    }

    public String getSettlementType() {
        return settlement_type;
    }

    public void setSettlementType(String settlementType) {
        this.settlement_type = settlementType;
    }

    public String getPointIssue() {
        return point_issue;
    }

    public void setPointIssue(String pointIssue) {
        this.point_issue = pointIssue;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameF() {
        return name_f;
    }

    public void setNameF(String nameF) {
        this.name_f = nameF;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

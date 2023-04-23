/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

import java.util.ArrayList;
/**
 *
 * @author ivs
 */
public class IspotReserverUpdateWSI {
    private String reserve_id;
    private String reserve_date;
    private ArrayList<MenuWSI> menu;
    private String adjusts_price;  
    private String point_use_discount;
    private String payment_price;
    private String settlement_type;
    private String point_issue;

    public String getReserveId() {
        return reserve_id;
    }

    public void setReserveId(String reserveId) {
        this.reserve_id = reserveId;
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

    public String getPointUseDiscount() {
        return point_use_discount;
    }

    public void setPointUseDiscount(String pointUseDiscount) {
        this.point_use_discount = pointUseDiscount;
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
    
}

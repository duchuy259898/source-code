/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

/**
 *
 * @author ivs
 */
public class CustomerCommingInfomationWSI {

    private String posId;
    private String password;
    private String posSalonId;
    private String comingDate;
    private String comingId;
    private String paymentTotalPriceFlg;
    private String diffDateTime;
    private String comingIdFlg;
    private String start;
    private String count;
    private String format;

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosSalonId() {
        return posSalonId;
    }

    public void setPosSalonId(String posSalonId) {
        this.posSalonId = posSalonId;
    }

    public String getComingDate() {
        return comingDate;
    }

    public void setComingDate(String comingDate) {
        this.comingDate = comingDate;
    }

    public String getComingId() {
        return comingId;
    }

    public void setComingId(String comingId) {
        this.comingId = comingId;
    }

    public String getPaymentTotalPriceFlg() {
        return paymentTotalPriceFlg;
    }

    public void setPaymentTotalPriceFlg(String paymentTotalPriceFlg) {
        this.paymentTotalPriceFlg = paymentTotalPriceFlg;
    }

    public String getDiffDateTime() {
        return diffDateTime;
    }

    public void setDiffDateTime(String diffDateTime) {
        this.diffDateTime = diffDateTime;
    }

    public String getComingIdFlg() {
        return comingIdFlg;
    }

    public void setComingIdFlg(String comingIdFlg) {
        this.comingIdFlg = comingIdFlg;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
}

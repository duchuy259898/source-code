/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.account.api;

/**
 *
 * @author lvut
 */
public class RegistPaymentWSI {

    private String posId;
    private String password;
    private String posSalonId;
    private String comingId;
    private String paymentPrice;
    private String format;

    public String getComingId() {
        return comingId;
    }

    public void setComingId(String comingId) {
        this.comingId = comingId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(String paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getPosSalonId() {
        return posSalonId;
    }

    public void setPosSalonId(String posSalonId) {
        this.posSalonId = posSalonId;
    }
}

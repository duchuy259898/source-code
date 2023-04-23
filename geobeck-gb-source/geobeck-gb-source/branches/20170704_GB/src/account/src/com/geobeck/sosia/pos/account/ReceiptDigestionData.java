/*
 * ReceiptData.java
 *
 * Created on 2007/10/23, 15:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.account;

import java.sql.Timestamp;

/**
 *
 * @author
 */
public class ReceiptDigestionData {

    private String productName = "";
    private Integer coProductNum = null;
    private Integer cdProductNum = null;
    private String staffName = "";
    private Integer productSum = null;
    private Integer productStill = null;
    private Timestamp expirationDate = null;

    /**
     * Creates a new instance of ReceiptData
     */
    public ReceiptDigestionData() {
    }

    public ReceiptDigestionData(String productName, Integer coProductNum, Integer cdProductNum, String staffName, Integer productSum, Timestamp expirationDate) {
        this.setProductName(productName);
        this.setCoProductNum(coProductNum);
        this.setCdProductNum(cdProductNum);
        this.setStaffName(staffName);
      //  this.setStaffName2(staffName2);
        this.setProductSum(productSum);
        this.setProductStill(coProductNum, productSum);
        this.setExpirationDate(expirationDate);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCoProductNum() {
        return coProductNum;
    }

    public void setCoProductNum(Integer coProductNum) {
        this.coProductNum = coProductNum;
    }

    public Integer getCdProductNum() {
        return cdProductNum;
    }

    public void setCdProductNum(Integer cdProductNum) {
        this.cdProductNum = cdProductNum;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getProductSum() {
        return productSum;
    }

    public void setProductSum(Integer productSum) {
        this.productSum = productSum;
    }

    public Integer getProductStill() {
        return productStill;
    }

    public void setProductStill(Integer productStill) {
        this.productStill = productStill;
    }
    public void setProductStill(Integer coProductNum, Integer productSum) {
        this.productStill= (int)(coProductNum-productSum) ;
    }
      public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
    
}

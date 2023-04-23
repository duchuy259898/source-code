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
import java.text.NumberFormat;

/**
 *
 * @author
 */
public class ReceiptDigestionData {
    
    // 20170804 nami start edit #21102 è¡âªâÒêîÇè¨êîÇ≈èoóÕ
    private String productName = "";
    private Integer coProductNum = null;
    //private Integer cdProductNum = null;
    private String cdProductNum = null;
    private String staffName = "";
    //private Integer productSum = null;
    private String productSum = null;
    //private Integer productStill = null;
    private String productStill = null;
    private Timestamp expirationDate = null;
    
    private NumberFormat nf = null;
    // 20170804 nami end edit #21102

    /**
     * Creates a new instance of ReceiptData
     */
    public ReceiptDigestionData() {
    }
    
    // 20170804 nami start edit #21102
    //public ReceiptDigestionData(String productName, Integer coProductNum, Integer cdProductNum, String staffName, Integer productSum, Timestamp expirationDate) {
    public ReceiptDigestionData(String productName, Integer coProductNum, Double cdProductNum, String staffName, Double productSum, Timestamp expirationDate) {
        
        nf = NumberFormat.getInstance();
        
        this.setProductName(productName);
        this.setCoProductNum(coProductNum);
        this.setCdProductNum(cdProductNum);
        this.setStaffName(staffName);
      //  this.setStaffName2(staffName2);
        this.setProductSum(productSum);
        this.setProductStill(coProductNum, productSum);
        this.setExpirationDate(expirationDate);
    }
    // 20170804 nami end edit #21102

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
    
    // 20170804 nami start edit #21102
    //    public Integer getCdProductNum() {
    //        return cdProductNum;
    //    }
    //
    //    public void setCdProductNum(Integer cdProductNum) {
    //        this.cdProductNum = cdProductNum;
    //    }
    
    public String getCdProductNum() {
        return cdProductNum;
    }

    public void setCdProductNum(Double cdProductNum) {
        this.cdProductNum = nf.format(cdProductNum);
    }
    // 20170804 nami end edit #21102

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    
    // 20170804 nami start edit #21102
    //    public Integer getProductSum() {
    //        return productSum;
    //    }
    //
    //    public void setProductSum(Integer productSum) {
    //        this.productSum = productSum;
    //    }
    //    
    //    public Integer getProductStill() {
    //        return productStill;
    //    }
    //    public void setProductStill(Integer productStill) {
    //        this.productStill = productStill;
    //    }
    //    public void setProductStill(Integer coProductNum, Integer productSum) {
    //        this.productStill= (int)(coProductNum-productSum) ;
    //    }
    
    public String getProductSum() {
        return productSum;
    }

    public void setProductSum(Double productSum) {
        this.productSum = nf.format(productSum);
    }
    
    public String getProductStill() {
        return productStill;
    }

    public void setProductStill(Double productStill) {
        this.productStill = nf.format(productStill);
    }

    public void setProductStill(Integer coProductNum, Double productSum) {
        this.productStill= nf.format(coProductNum-productSum);
    }
    // 20170804 nami end edit #21102
      public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
    
}

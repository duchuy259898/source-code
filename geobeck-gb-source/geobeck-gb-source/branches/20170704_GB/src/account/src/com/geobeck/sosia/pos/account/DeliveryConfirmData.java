/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.account;

/**
 *
 * @author Tran Thi Mai Loan
 */
public class DeliveryConfirmData {
    
        private	String        productName         = "";
	private	String        staffName           = "";
	private	Integer       purchaseNum         = null;
	private	Integer       amountNo         = null;
	private	Integer       remainNum       = null;
	private	Integer       productNumUsed           = null;

        public DeliveryConfirmData(String productName,String staffName,Integer purchaseNum,Integer productNumUsed,Integer totalpick ,Integer remainNum) {
            this.productName=productName;
            this.staffName=staffName;
            this.purchaseNum=purchaseNum;
            this.amountNo=totalpick;
            this.productNumUsed=productNumUsed;
            this.remainNum=remainNum;
        }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public Integer getAmountNum() {
        return amountNo;
    }

    public void setAmountNum(Integer totalpick) {
        this.amountNo = totalpick;
    }

    public Integer getProductNumUsed() {
        return productNumUsed;
    }

    public void setProductNumUsed(Integer productNumUsed) {
        this.productNumUsed = productNumUsed;
    }

    public Integer getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(Integer remainNum) {
        this.remainNum = remainNum;
    }
        
        
}

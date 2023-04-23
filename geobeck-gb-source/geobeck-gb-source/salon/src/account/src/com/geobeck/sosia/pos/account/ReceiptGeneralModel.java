/*
 * ReceiptGeneralModel.java
 *
 * Copyright (c) 1993-1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;

/**
 * @author IVS_PTQUANG add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
 */
public final class ReceiptGeneralModel {

    private final String CONST_TITLE_PAYMENT_1 = "カード";
    private final String CONST_TITLE_PAYMENT_2 = "電子マネー";
    private final String CONST_TITLE_PAYMENT_3 = "金券・その他";
    
    private MstCustomer customer;
    private Integer slipNo;
    private java.util.Date salesDate;
    private java.util.Date paymentDate;
    private MstStaff mstStaff;
    private Long totalAmount;
    private Long outOfValue = 0l;
    private Long prevAmount = 0l;
    private Long useAmount = 0l;
    private Long cashValue = 0l;
    private String cardTitle = "";
    private Long cardValue = 0l;
    private String eCashTitle = "";
    private Long eCashValue = 0l;
    private String giftTitle = "";
    private Long giftValue = 0l;

    /**
     * set data Constructor
     * @param _customer
     * @param _slipNo
     * @param _salesDate
     * @param _paymentDate
     * @param _mstStaff
     * @param _cardValue
     * @param _eCashValue
     * @param _outOfValue
     * @param _prevAmount
     * @param _useAmount
     * @param _giftValue
     * @param _billValue 
     */
    public ReceiptGeneralModel (
            MstCustomer _customer,
            Integer _slipNo,
            java.util.Date _salesDate,
            java.util.Date _paymentDate,
            MstStaff _mstStaff,
            Long _cardValue,
            Long _eCashValue,
            Long _outOfValue,
            Long _prevAmount,
            Long _useAmount,
            Long _giftValue,
            Long _billValue
    ) {
        this.setCustomer(_customer);
        this.setSlipNo(_slipNo);
        this.setSalesDate(_salesDate);
        this.setPaymentDate(_paymentDate);
        this.setMstStaff(_mstStaff);

        this.setTotalAmount(_cardValue);
        this.setOutOfValue(_prevAmount);
        this.setCashValue(_prevAmount);

        this.setCardValue(_eCashValue);
        this.seteCashValue(_giftValue);
        this.setGiftValue(_useAmount);
        this.setUseAmount(_billValue);
        this.setPrevAmount(_outOfValue);
    }
    /**
     * Print Report
     * @throws Exception 
     */
    public void printReceiptGeneral() throws Exception {
        PrintReceipt pr = new PrintReceipt();
        pr.setCustomer(this.getCustomer());
        pr.setSlipNo(this.getSlipNo());
        pr.setSalesDate(this.getSalesDate());
        pr.setPaymentDate(this.getPaymentDate());
        pr.setStaff(this.getMstStaff());
        pr.setTotalAmount(this.totalAmount + this.getUseAmount());
        pr.setOutOfValue(getPaymentDetails());
        pr.setPrevAmount(this.getPrevAmount());
        pr.setCashValue(this.outOfValue);
        pr.setCardValue(this.cardValue);
        pr.setECashValue(this.eCashValue);
        pr.setGiftValue(this.giftValue);
        pr.setCardTitle(CONST_TITLE_PAYMENT_1);
        pr.seteCashTitle(CONST_TITLE_PAYMENT_2);
        pr.setGiftTitle(CONST_TITLE_PAYMENT_3);
        pr.setUseAmount(this.getUseAmount());
        pr.add(new ReceiptData());

        switch (pr.getReceiptSetting().getReceiptSize()) {
            case 0:
                pr.setReceiptType(PrintReceipt.ReceiptType.LARGE);
                break;
            case 1:
                pr.setReceiptType(PrintReceipt.ReceiptType.NORMAL);
                break;
        }
        pr.print(true);
    }
    
    /**
     * @return 
     */
    private Long getPaymentDetails(){
        return this.cashValue + this.cardValue + this.eCashValue + this.giftValue;
    }
    
    /**
     * @return the paymentDate
     */
    public java.util.Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(java.util.Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the customer
     */
    public MstCustomer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(MstCustomer customer) {
        this.customer = customer;
    }

    /**
     * @return the slipNo
     */
    public Integer getSlipNo() {
        return slipNo;
    }

    /**
     * @param slipNo the slipNo to set
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * @return the salesDate
     */
    public java.util.Date getSalesDate() {
        return salesDate;
    }

    /**
     * @param salesDate the salesDate to set
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * @return the mstStaff
     */
    public MstStaff getMstStaff() {
        return mstStaff;
    }

    /**
     * @param mstStaff the mstStaff to set
     */
    public void setMstStaff(MstStaff mstStaff) {
        this.mstStaff = mstStaff;
    }

    /**
     * @return the totalAmount
     */
    public Long getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the outOfValue
     */
    public Long getOutOfValue() {
        return outOfValue;
    }

    /**
     * @param outOfValue the outOfValue to set
     */
    public void setOutOfValue(Long outOfValue) {
        this.outOfValue = outOfValue;
    }

    /**
     * @return the prevAmount
     */
    public Long getPrevAmount() {
        return prevAmount;
    }

    /**
     * @param prevAmount the prevAmount to set
     */
    public void setPrevAmount(Long prevAmount) {
        if(prevAmount > 0){
            this.prevAmount = prevAmount;
        } else {
            this.prevAmount = 0l;
        }
    }

    /**
     * @return the useAmount
     */
    public Long getUseAmount() {
        return useAmount;
    }

    /**
     * @param useAmount the useAmount to set
     */
    public void setUseAmount(Long useAmount) {
        if(useAmount > 0){
            this.useAmount = useAmount;
        }else{
            this.useAmount = 0l;
        }
    }

    /**
     * @return the cashValue
     */
    public Long getCashValue() {
        return cashValue;
    }

    /**
     * @param cashValue the cashValue to set
     */
    public void setCashValue(Long cashValue) {
        this.cashValue = cashValue;
    }

    /**
     * @return the cardTitle
     */
    public String getCardTitle() {
        return cardTitle;
    }

    /**
     * @param cardTitle the cardTitle to set
     */
    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    /**
     * @return the cardValue
     */
    public Long getCardValue() {
        return cardValue;
    }

    /**
     * @param cardValue the cardValue to set
     */
    public void setCardValue(Long cardValue) {
        this.cardValue = cardValue;
    }

    /**
     * @return the eCashTitle
     */
    public String geteCashTitle() {
        return eCashTitle;
    }

    /**
     * @param eCashTitle the eCashTitle to set
     */
    public void seteCashTitle(String eCashTitle) {
        this.eCashTitle = eCashTitle;
    }

    /**
     * @return the eCashValue
     */
    public Long geteCashValue() {
        return eCashValue;
    }

    /**
     * @param eCashValue the eCashValue to set
     */
    public void seteCashValue(Long eCashValue) {
        this.eCashValue = eCashValue;
    }

    /**
     * @return the giftTitle
     */
    public String getGiftTitle() {
        return giftTitle;
    }

    /**
     * @param giftTitle the giftTitle to set
     */
    public void setGiftTitle(String giftTitle) {
        this.giftTitle = giftTitle;
    }

    /**
     * @return the giftValue
     */
    public Long getGiftValue() {
        return giftValue;
    }

    /**
     * @param giftValue the giftValue to set
     */
    public void setGiftValue(Long giftValue) {
        this.giftValue = giftValue;
    }
}

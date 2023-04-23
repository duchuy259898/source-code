/*
 * PaymentAccordingBean.java
 *
 * Created on 2008/03/26, 21:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;


/**
 *
 * @author iida
 */
public class PaymentAccordingBean {
	
	private String date;	
	private Long technicPrice;
	private Long itemPrice;
	private Long itemCount;
	private Long totalSalesPrice;
	private Long allDiscount;
	private Long taxPrice;
	private Long allTotalSalesPrice;
	private Long cardPrice;
	private Long cashSalesPrice;
	private Long creditPrice;
	private Long collectionCashPrice;
	private Long collectionCreditPrice;
	private Long registerInOut;
	private Long baseValue;
	private Long logicCash;
	private Long physicsCash;
	
	/** Creates a new instance of PaymentAccordingBean */
	public PaymentAccordingBean() {
	}

	public String getDate() {
	    return date;
	}

	public void setDate(String date) {
	    this.date = date;
	}
	
	public Long getTechnicPrice() {
	    return technicPrice;
	}

	public void setTechnicPrice(Long technicPrice) {
	    this.technicPrice = technicPrice;
	}

	public Long getItemPrice() {
	    return itemPrice; 
	}
 
	public void setItemPrice(Long itemPrice) {
	    this.itemPrice = itemPrice;
	}

	public Long getItemCount() {
	    return itemCount; 
	}
 
	public void setItemCount(Long itemCount) {
	    this.itemCount = itemCount;
	}

	public Long getTotalSalesPrice() {
	    return totalSalesPrice; 
	}
 
	public void setTotalSalesPrice(Long totalSalesPrice) {
	    this.totalSalesPrice = totalSalesPrice;
	}

	public Long getAllDiscount() { 
	    return allDiscount;
	}
 
	public void setAllDiscount(Long allDiscount) {
	    this.allDiscount = allDiscount;
	}
	
	public Long getTaxPrice() { 
	    return taxPrice;
	}
 
	public void setTaxPrice(Long taxPrice) {
	    this.taxPrice = taxPrice;
	}

	public Long getAllTotalSalesPrice() { 
	    return allTotalSalesPrice;
	}
 
	public void setAllTotalSalesPrice(Long allTotalSalesPrice) {
	    this.allTotalSalesPrice = allTotalSalesPrice;
	}

	public Long getCardPrice() {
	    return cardPrice; 
	}
 
	public void setCardPrice(Long cardPrice) {
	    this.cardPrice = cardPrice;
	}

	public Long getCashSalesPrice() { 
	    return cashSalesPrice;
	}
 
	public void setCashSalesPrice(Long cashSalesPrice) {
	    this.cashSalesPrice = cashSalesPrice;
	}

	public Long getCreditPrice() {
	    return creditPrice; 
	}
 
	public void setCreditPrice(Long creditPrice) {
	    this.creditPrice = creditPrice;
	}

	public Long getCollectionCashPrice() {
	    return collectionCashPrice; 
	}
 
	public void setCollectionCashPrice(Long collectionCashPrice) {
	    this.collectionCashPrice = collectionCashPrice;
	}
	
	public Long getCollectionCreditPrice() {
	    return collectionCreditPrice; 
	}
 
	public void setCollectionCreditPrice(Long collectionCreditPrice) {
	    this.collectionCreditPrice = collectionCreditPrice;
	}

	public Long getRegisterInOut() {
	    return registerInOut; 
	}
 
	public void setRegisterInOut(Long registerInOut) {
	    this.registerInOut = registerInOut;
	}

	public Long getBaseValue() {
	    return baseValue; 
	}
 
	public void setBaseValue(Long baseValue) {
	    this.baseValue = baseValue;
	}

	public Long getLogicCash() {
	    return logicCash; 
	}
 
	public void setLogicCash(Long logicCash) {
	    this.logicCash = logicCash;
	}

	public Long getPhysicsCash() {
	    return physicsCash; 
	}
 
	public void setPhysicsCash(Long physicsCash) {
	    this.physicsCash = physicsCash;
	}

	
}

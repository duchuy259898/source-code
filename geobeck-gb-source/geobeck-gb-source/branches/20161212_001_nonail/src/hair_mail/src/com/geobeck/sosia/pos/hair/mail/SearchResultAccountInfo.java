/*
 * SearchResultAccountInfo.java
 *
 * Created on 2008/11/09, 2:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.mail;

import com.geobeck.sql.ResultSetWrapper;
import java.text.*;

public class SearchResultAccountInfo {

    private String shopName;
    private String slipNo;
    private String customerNo;
    private String customerName;
    private String salesDate;
    private String chargeStaff;
    private Boolean chargeDesignatedFlag;
    private String productDivision;
    private String productDivisionName;
    private String productName;
    private String categoryName;
    private Integer productValue;
    private String productNum;
    private Integer discountValue;
    private String technicStaff;
    private Boolean technicDesignatedFlag;
    private String reservationTime;
    private String startTime;
    private String visitTime;
    private String leaveTime;
    //IVS NNTUAN START ADD 20131008
    private String updateDate;
    //IVS NNTUAN END ADD 20131008
    private String waitTime;
    private String stayTime;
    private String responseName;
    private Integer totalPrice;
    private String visitedMemo;
    private String progressDay;
    private String nextReserveDate;

    private Integer moneyValue;
    private String  cardName;
    private Integer cardValue;
    private String  ecashName;
    private Integer ecashValue;
    private String  giftName;
    private Integer giftValue;
    private Integer billValue;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
    
    
    public void setData(ResultSetWrapper rs){
        try {
            setShopName( rs.getString("shop_name") != null ? rs.getString("shop_name") : "");
            setSlipNo( rs.getString("slip_no") != null ? rs.getString("slip_no") : "");
            setCustomerNo( rs.getString("customer_no") != null ? rs.getString("customer_no") : "");
            setCustomerName( rs.getString("customer_name") != null ? rs.getString("customer_name") : "");
            setSalesDate(rs.getDate("sales_date") != null ? dateFormat.format(rs.getDate("sales_date")) : "");
            setChargeDesignatedFlag( rs.getBoolean("charge_designated_flag"));
            setProductDivision( rs.getString("product_division") != null ? rs.getString("product_division") : "");
            setProductDivisionName( rs.getString("product_division_name") != null ? rs.getString("product_division_name") : "");
            setCategoryName( rs.getString("category_name") != null ? rs.getString("category_name") : "");
            setProductName( rs.getString("product_name") != null ? rs.getString("product_name") : "");
            setProductValue( (int)rs.getDouble("product_value"));
            setProductNum( rs.getString("product_num") + (rs.getBoolean("is_proportionally") ? "%" : ""));
            setDiscountValue( (int)rs.getDouble("discount_value") );
            setTotalPrice( (int)rs.getDouble("total_price") );
            setChargeStaff(rs.getString("charge_staff") != null ? rs.getString("charge_staff") : "");
            setTechnicStaff(rs.getString("technic_staff") != null ? rs.getString("technic_staff") : "");
            setTechnicDesinatedFlag( rs.getBoolean("technic_designated_flag") );
            setReserveTime(rs.getString("reservation_datetime"));
            setStartTime(rs.getString("start_time"));
            setVisitTime(rs.getString("visit_time"));
            setLeaveTime(rs.getString("leave_time"));
            setWaitTime( rs.getString("wait_time") != null ? rs.getString("wait_time") : "");
	    setStayTime( rs.getString("stay_time") != null ? rs.getString("stay_time") : "");
            setProgressDay( rs.getString("progress_day") != null ? rs.getString("progress_day") : "" );

            if (rs.getString("response_name") != null) {
                setResponseName( rs.getString("response_name").replace("{","").replace("}","").replace("\"","") );
            }
            
            setVisitedMemo( rs.getString("visited_memo") != null ? rs.getString("visited_memo") : "");
            setNextReserveDate(rs.getDate("next_reserve_date") != null ? dateFormat.format(rs.getDate("next_reserve_date")) : "");

            setMoneyValue( (int)rs.getDouble("money_value") );
            setCardName(rs.getString("card_name"));
            setCardValue( (int)rs.getDouble("card_value") );
            setEcashName(rs.getString("ecash_name"));
            setEcashValue( (int)rs.getDouble("ecash_value") );
            setGiftName(rs.getString("gift_name"));
            setGiftValue( (int)rs.getDouble("gift_value") );
            setBillValue( (int)rs.getDouble("bill_value") );

            //IVS NNTUAN START ADD 20131008
                setUpdateDate(rs.getDate("update_date").toString());
            //IVS NNTUAN END ADD 20131008
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getFree(){
	if (productDivision.equals("0")) {
	    return "";
	} else {
	    return chargeDesignatedFlag.booleanValue() ? "指名" : "フリー";
	}
    }
    
    public String getFreeTechnic(){
	if (productDivision.equals("0")) {
	    return "";
	} else {
	    return technicDesignatedFlag.booleanValue() ? "指名" : "フリー";
	}
    }
    
    public String getShopName() {
            return shopName;
    }
    public void setShopName(String shopName) {
            this.shopName = shopName;
    }
    public String getSlipNo() {
            return slipNo;
    }
    public void setSlipNo(String slipNo) {
            this.slipNo = slipNo;
    }
    public String getCustomerNo() {
            return customerNo;
    }
    public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
    }
    public String getCustomerName() {
            return customerName;
    }
    public void setCustomerName(String customerName) {
            this.customerName = customerName;
    }
    public String getSalesDate() {
            return salesDate;
    }
    public void setSalesDate(String salesDate) {
            this.salesDate = salesDate;
    }
    public void setChargeStaff(String staff){
        this.chargeStaff = staff;
    }
    public String getChargeStaff(){
        return this.chargeStaff;
    }
    public Boolean getChargeDesignatedFlag() {
            return chargeDesignatedFlag;
    }
    public void setChargeDesignatedFlag(Boolean chargeDesignatedFlag) {
            this.chargeDesignatedFlag = chargeDesignatedFlag;
    }
    public String getProductDivision() {
            return productDivision;
    }
    public void setProductDivision(String productDivision) {
            this.productDivision = productDivision;
    }
    public String getProductDivisionName() {
            return productDivisionName;
    }
    public void setProductDivisionName(String productDivisionName) {
            this.productDivisionName = productDivisionName;
    }
    public String getCategoryName() {
            return categoryName;
    }
    public void setCategoryName(String categryName) {
            this.categoryName = categryName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public String getProductName(){
        return this.productName;
    }
    public Integer getProductValue() {
            return productValue;
    }
    public void setProductValue(Integer productValue) {
            this.productValue = productValue;
    }
    public String getProductNum() {
            return productNum;
    }
    public void setProductNum(String productNum) {
            this.productNum = productNum;
    }
    public Integer getDiscountValue() {
            return discountValue;
    }
    public void setDiscountValue(Integer discountValue) {
            this.discountValue = discountValue;
    }
    public String getTechnicStaff() {
            return technicStaff;
    }
    public void setTechnicStaff(String technicStaff) {
            this.technicStaff = technicStaff;
    }
    public Boolean getTechnicDesinatedFlag() {
            return technicDesignatedFlag;
    }
    public void setTechnicDesinatedFlag(Boolean technicDesignatedFlag) {
            this.technicDesignatedFlag = technicDesignatedFlag;
    }
    public String getReserveTime() {
            return reservationTime;
    }
    public void setReserveTime(String reservationTime) {
            this.reservationTime = reservationTime;
    }
    public String getStartTime() {
            return startTime;
    }
    public void setStartTime(String startTime) {
            this.startTime = startTime;
    }
    public String getVisitTime() {
            return visitTime;
    }
    public void setVisitTime(String visitTime) {
            this.visitTime = visitTime;
    }
    public String getLeaveTime() {
            return leaveTime;
    }
    public void setLeaveTime(String leaveTime) {
            this.leaveTime = leaveTime;
    }
    //IVS NNTUAN START ADD 20131008
    public String getUpdateDate() {
            return updateDate;
    }
    public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
    }
    //IVS NNTUAN END ADD 20131008
    public void setWaitTime(String time){
        this.waitTime = time;
    }
    public String getWaitTime(){
        return this.waitTime;
    }
    public void setStayTime(String time){
        this.stayTime = time;
    }
    public String getStayTime(){
        return this.stayTime;
    }
    public String getResponseName() {
            return responseName;
    }
    public void setResponseName(String responseName) {
            this.responseName = responseName;
    }
    public Integer getTotalPrice() {
            return totalPrice;
    }
    public void setTotalPrice(Integer totalPrice) {
            this.totalPrice = totalPrice;
    }
    public String getVisitedMemo() {
            return visitedMemo;
    }
    public void setVisitedMemo(String visitMemo) {
            this.visitedMemo = visitMemo;
    }
    public String getProgressDay() {
            return progressDay;
    }
    public void setProgressDay(String progressDay) {
            this.progressDay = progressDay;
    }
    public String getNextReserveDate() {
            return nextReserveDate;
    }
    public void setNextReserveDate(String nextReserveDate) {
            this.nextReserveDate = nextReserveDate;
    }

    /**
     * @return the moneyValue
     */
    public Integer getMoneyValue() {
        return moneyValue;
    }

    /**
     * @param moneyValue the moneyValue to set
     */
    public void setMoneyValue(Integer moneyValue) {
        this.moneyValue = moneyValue;
    }

    /**
     * @return the cardName
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * @param cardName the cardName to set
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * @return the cardValue
     */
    public Integer getCardValue() {
        return cardValue;
    }

    /**
     * @param cardValue the cardValue to set
     */
    public void setCardValue(Integer cardValue) {
        this.cardValue = cardValue;
    }

    /**
     * @return the ecashName
     */
    public String getEcashName() {
        return ecashName;
    }

    /**
     * @param ecashName the ecashName to set
     */
    public void setEcashName(String ecashName) {
        this.ecashName = ecashName;
    }

    /**
     * @return the ecashValue
     */
    public Integer getEcashValue() {
        return ecashValue;
    }

    /**
     * @param ecashValue the ecashValue to set
     */
    public void setEcashValue(Integer ecashValue) {
        this.ecashValue = ecashValue;
    }

    /**
     * @return the giftName
     */
    public String getGiftName() {
        return giftName;
    }

    /**
     * @param giftName the giftName to set
     */
    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    /**
     * @return the giftValue
     */
    public Integer getGiftValue() {
        return giftValue;
    }

    /**
     * @param giftValue the giftValue to set
     */
    public void setGiftValue(Integer giftValue) {
        this.giftValue = giftValue;
    }

    /**
     * @return the billValue
     */
    public Integer getBillValue() {
        return billValue;
    }

    /**
     * @param billValue the billValue to set
     */
    public void setBillValue(Integer billValue) {
        this.billValue = billValue;
    }

}

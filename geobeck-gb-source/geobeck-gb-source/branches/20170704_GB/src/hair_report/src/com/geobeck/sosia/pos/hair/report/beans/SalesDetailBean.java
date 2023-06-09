/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author user
 */
public class SalesDetailBean implements Serializable {
    
    /** ショップID */
    private Integer shopId = null;

    /** 伝票番号 */
    private Integer slipNo = null;

    /** 伝票詳細番号 */
    private Integer slipDetailNo = null;

    /** 技術商品区分ＩＤ */
    private Integer productDivision = null;

    /** 技術商品ID */
    private Integer productId = null;

    /** 数量 */
    private Integer productNum = null;

    /** 金額 */
    private BigDecimal productValue = null;

    /** 割引率 */
    private BigDecimal discountRate = null;

    /** 割引金額 */
    private BigDecimal discountValue = null;

    /** 担当指名フラグ */
    private Boolean designatedFlag = null;

    /** 担当者ID */
    private Integer staffId = null;

    /** 登録日 */
    private Date insertDate = null;

    /** 更新日 */
    private Date updateDate = null;

    /** 削除日 */
    private Date deleteDate = null;

    /** アプローチフラグ */
    private Boolean approachedFlag = null;

    /** 契約No */
    private Integer contractNo = null;

    /** 契約詳細No */
    private Integer contractDetailNo = null;

    /** 契約元ショップID */
    private Integer contractShopId = null;

    /** アンケートID */
    private Integer enqueteId = null;

    /** 当日キャンセルフラグ */
    private Integer cancelFlag = null;

    /** 宅送フラグ */
    private Integer deliveryFlg = null;

    /** 宅送先 */
    private Integer deliveryAddress = null;

    /** 配送日 */
    private Date deliveryDate = null;
    
    /** 分類名 */
    private String technicName = null;

    /**
     * @return the shopId
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * @param shopId the shopId to set
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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
     * @return the slipDetailNo
     */
    public Integer getSlipDetailNo() {
        return slipDetailNo;
    }

    /**
     * @param slipDetailNo the slipDetailNo to set
     */
    public void setSlipDetailNo(Integer slipDetailNo) {
        this.slipDetailNo = slipDetailNo;
    }

    /**
     * @return the productDivision
     */
    public Integer getProductDivision() {
        return productDivision;
    }

    /**
     * @param productDivision the productDivision to set
     */
    public void setProductDivision(Integer productDivision) {
        this.productDivision = productDivision;
    }

    /**
     * @return the productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * @return the productNum
     */
    public Integer getProductNum() {
        return productNum;
    }

    /**
     * @param productNum the productNum to set
     */
    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    /**
     * @return the productValue
     */
    public BigDecimal getProductValue() {
        return productValue;
    }

    /**
     * @param productValue the productValue to set
     */
    public void setProductValue(BigDecimal productValue) {
        this.productValue = productValue;
    }

    /**
     * @return the discountRate
     */
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    /**
     * @param discountRate the discountRate to set
     */
    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the designatedFlag
     */
    public Boolean getDesignatedFlag() {
        return designatedFlag;
    }

    /**
     * @param designatedFlag the designatedFlag to set
     */
    public void setDesignatedFlag(Boolean designatedFlag) {
        this.designatedFlag = designatedFlag;
    }

    /**
     * @return the staffId
     */
    public Integer getStaffId() {
        return staffId;
    }

    /**
     * @param staffId the staffId to set
     */
    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    /**
     * @return the insertDate
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * @param insertDate the insertDate to set
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the deleteDate
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * @param deleteDate the deleteDate to set
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * @return the approachedFlag
     */
    public Boolean getApproachedFlag() {
        return approachedFlag;
    }

    /**
     * @param approachedFlag the approachedFlag to set
     */
    public void setApproachedFlag(Boolean approachedFlag) {
        this.approachedFlag = approachedFlag;
    }

    /**
     * @return the contractNo
     */
    public Integer getContractNo() {
        return contractNo;
    }

    /**
     * @param contractNo the contractNo to set
     */
    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * @return the contractDetailNo
     */
    public Integer getContractDetailNo() {
        return contractDetailNo;
    }

    /**
     * @param contractDetailNo the contractDetailNo to set
     */
    public void setContractDetailNo(Integer contractDetailNo) {
        this.contractDetailNo = contractDetailNo;
    }

    /**
     * @return the contractShopId
     */
    public Integer getContractShopId() {
        return contractShopId;
    }

    /**
     * @param contractShopId the contractShopId to set
     */
    public void setContractShopId(Integer contractShopId) {
        this.contractShopId = contractShopId;
    }

    /**
     * @return the enqueteId
     */
    public Integer getEnqueteId() {
        return enqueteId;
    }

    /**
     * @param enqueteId the enqueteId to set
     */
    public void setEnqueteId(Integer enqueteId) {
        this.enqueteId = enqueteId;
    }

    /**
     * @return the cancelFlag
     */
    public Integer getCancelFlag() {
        return cancelFlag;
    }

    /**
     * @param cancelFlag the cancelFlag to set
     */
    public void setCancelFlag(Integer cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    /**
     * @return the deliveryFlg
     */
    public Integer getDeliveryFlg() {
        return deliveryFlg;
    }

    /**
     * @param deliveryFlg the deliveryFlg to set
     */
    public void setDeliveryFlg(Integer deliveryFlg) {
        this.deliveryFlg = deliveryFlg;
    }

    /**
     * @return the deliveryAddress
     */
    public Integer getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * @param deliveryAddress the deliveryAddress to set
     */
    public void setDeliveryAddress(Integer deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 分類名を取得します。
     * 
     * @return the technicName
     */
    public String getTechnicName() {
        return technicName;
    }

    /**
     * 分類名を設定します。
     * 
     * @param technicName the technicName to set
     */
    public void setTechnicName(String technicName) {
        this.technicName = technicName;
    }

}

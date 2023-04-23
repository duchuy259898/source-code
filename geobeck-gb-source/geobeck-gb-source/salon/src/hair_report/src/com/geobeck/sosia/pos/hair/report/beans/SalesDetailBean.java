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
    
    /** ƒVƒ‡ƒbƒvID */
    private Integer shopId = null;

    /** “`•[”Ô† */
    private Integer slipNo = null;

    /** “`•[Ú×”Ô† */
    private Integer slipDetailNo = null;

    /** ‹Zp¤•i‹æ•ª‚h‚c */
    private Integer productDivision = null;

    /** ‹Zp¤•iID */
    private Integer productId = null;

    /** ”—Ê */
    private Integer productNum = null;

    /** ‹àŠz */
    private BigDecimal productValue = null;

    /** Š„ˆø—¦ */
    private BigDecimal discountRate = null;

    /** Š„ˆø‹àŠz */
    private BigDecimal discountValue = null;

    /** ’S“–w–¼ƒtƒ‰ƒO */
    private Boolean designatedFlag = null;

    /** ’S“–ÒID */
    private Integer staffId = null;

    /** “o˜^“ú */
    private Date insertDate = null;

    /** XV“ú */
    private Date updateDate = null;

    /** íœ“ú */
    private Date deleteDate = null;

    /** ƒAƒvƒ[ƒ`ƒtƒ‰ƒO */
    private Boolean approachedFlag = null;

    /** Œ_–ñNo */
    private Integer contractNo = null;

    /** Œ_–ñÚ×No */
    private Integer contractDetailNo = null;

    /** Œ_–ñŒ³ƒVƒ‡ƒbƒvID */
    private Integer contractShopId = null;

    /** ƒAƒ“ƒP[ƒgID */
    private Integer enqueteId = null;

    /** “–“úƒLƒƒƒ“ƒZƒ‹ƒtƒ‰ƒO */
    private Integer cancelFlag = null;

    /** ‘î‘—ƒtƒ‰ƒO */
    private Integer deliveryFlg = null;

    /** ‘î‘—æ */
    private Integer deliveryAddress = null;

    /** ”z‘—“ú */
    private Date deliveryDate = null;
    
    /** •ª—Ş–¼ */
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
     * •ª—Ş–¼‚ğæ“¾‚µ‚Ü‚·B
     * 
     * @return the technicName
     */
    public String getTechnicName() {
        return technicName;
    }

    /**
     * •ª—Ş–¼‚ğİ’è‚µ‚Ü‚·B
     * 
     * @param technicName the technicName to set
     */
    public void setTechnicName(String technicName) {
        this.technicName = technicName;
    }

}

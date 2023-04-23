/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class OriginalReportResultBean implements Serializable {
    
    /** ƒVƒ‡ƒbƒvID */
    private Integer shopId = null;

    /** “`•[”Ô† */
    private Integer slipNo = null;

    /** ”Ì”„“ú */
    private Date salesDate = null;

    /** ŒÚ‹qID */
    private Integer customerId = null;

    /** ’S“–w–¼ƒtƒ‰ƒO */
    private Boolean designatedFlag = null;

    /** å’S“–ÒID */
    private Integer staffId = null;

    /** —ˆ“X‰ñ” */
    private Integer visitNum = null;

    /** “o˜^“ú */
    private Date insertDate = null;

    /** XV“ú */
    private Date updateDate = null;

    /** íœ“ú */
    private Date deleteDate = null;

    /** —ˆ“Xƒƒ‚ */
    private String visitedMemo = null;

    /** Ÿ‰ñ—ˆ“X“ú */
    private Date nextVisitDate = null;

    /** ‘O‰ñ—ˆ“X“ú */
    private Date reappearanceDate = null;

    /** ó’”Ô†iˆÚs—pj */
    private Integer salesNo = null;
    
    /** —ˆ“X“®‹@ID */
    private Integer firstComingMotiveClassId = null;
    
    /** —ˆ“X“®‹@–¼ */
    private String firstComingMotiveName = null;
    
    /** ”Ì”„Ú×ƒf[ƒ^ƒŠƒXƒg */
    private List<SalesDetailBean> salesDetails = new ArrayList<>();

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
     * @return the salesDate
     */
    public Date getSalesDate() {
        return salesDate;
    }

    /**
     * @param salesDate the salesDate to set
     */
    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * @return the visitNum
     */
    public Integer getVisitNum() {
        return visitNum;
    }

    /**
     * @param visitNum the visitNum to set
     */
    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
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
     * @return the visitedMemo
     */
    public String getVisitedMemo() {
        return visitedMemo;
    }

    /**
     * @param visitedMemo the visitedMemo to set
     */
    public void setVisitedMemo(String visitedMemo) {
        this.visitedMemo = visitedMemo;
    }

    /**
     * @return the nextVisitDate
     */
    public Date getNextVisitDate() {
        return nextVisitDate;
    }

    /**
     * @param nextVisitDate the nextVisitDate to set
     */
    public void setNextVisitDate(Date nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    /**
     * @return the reappearanceDate
     */
    public Date getReappearanceDate() {
        return reappearanceDate;
    }

    /**
     * @param reappearanceDate the reappearanceDate to set
     */
    public void setReappearanceDate(Date reappearanceDate) {
        this.reappearanceDate = reappearanceDate;
    }

    /**
     * @return the salesNo
     */
    public Integer getSalesNo() {
        return salesNo;
    }

    /**
     * @param salesNo the salesNo to set
     */
    public void setSalesNo(Integer salesNo) {
        this.salesNo = salesNo;
    }

    /**
     * @return the firstComingMotiveClassId
     */
    public Integer getFirstComingMotiveClassId() {
        return firstComingMotiveClassId;
    }

    /**
     * @param firstComingMotiveClassId the firstComingMotiveClassId to set
     */
    public void setFirstComingMotiveClassId(Integer firstComingMotiveClassId) {
        this.firstComingMotiveClassId = firstComingMotiveClassId;
    }

    /**
     * @return the firstComingMotiveName
     */
    public String getFirstComingMotiveName() {
        return firstComingMotiveName;
    }

    /**
     * @param firstComingMotiveName the firstComingMotiveName to set
     */
    public void setFirstComingMotiveName(String firstComingMotiveName) {
        this.firstComingMotiveName = firstComingMotiveName;
    }

    /**
     * @return the salesDetails
     */
    public List<SalesDetailBean> getSalesDetails() {
        return salesDetails;
    }

    /**
     * @param salesDetails the salesDetails to set
     */
    public void setSalesDetails(List<SalesDetailBean> salesDetails) {
        this.salesDetails = salesDetails;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.account;

/**
 *
 * @author lvtu
 */
public class ContractStaffShare implements Cloneable {
    /**
    * ìXï‹ID
    */
   private     Integer         shopId               = null;
   /**
    * ì`ï[No.
    */
   private     Integer         slipNo               = null;
   /**
    * å_ñÒNO.
    */
   private     Integer         contractNo           = null;
   /**
    * ÉXÉ^ÉbÉtNo
    */
   private     Integer          contractDetailNo    = null;
   /**
    * å_ñÒíSìñé“ID
    */
   private     Integer         staffId              = null;
   /**
    * äÑçá
    */
   private     Integer          rate                = null;
   
   private     String       courseClassName         = "";
   
   private     String       courseName              = "";
   
   private     Integer       productNum             = null; 
   
   private     Long          productValue           = null;
   
   private     String         staffNo               = "";
   
   private     Integer         seqNum               = null;

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }


    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    public Integer getContractNo() {
        return contractNo;
    }

    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    public Integer getContractDetailNo() {
        return contractDetailNo;
    }

    public void setContractDetailNo(Integer contractDetailNo) {
        this.contractDetailNo = contractDetailNo;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
    
    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public ContractStaffShare clone() throws CloneNotSupportedException {
        return (ContractStaffShare)super.clone();
    }
    
     public Long getProductValue() {
        return productValue;
    }

    public void setProductValue(Long productValue) {
        this.productValue = productValue;
    }
    
    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

}

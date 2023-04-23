/*
 * ProductShare.java
 *
 * Created on 2006/05/09, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 商品シェア
 * @author geobeck
 */
public class ProductShare implements Cloneable
{
	/**
	 * 店舗ID
	 */
	private     Integer         shopId          = null;
	/**
	 * 伝票No.
	 */
	private     Integer         slipNo          = null;
	/**
	 * 売上日
	 */
	private     java.util.Date  salesDate       = null;
	/**
	 * 伝票明細No.
	 */
	private     Integer         slipDetailNo    = null;
	/**
	 * 連番
	 */
	private     Integer         seqNum          = null;
	/**
	 * スタッフID
	 */
	private     Integer         staffId         = null;
	/**
	 * スタッフNo
	 */
	private     String          staffNo         = null;
	/**
	 * 分類
	 */
	private     String          itemClassName   = null;
	/**
	 * 商品名
	 */
	private     String          itemName        = null;
        /**
	 * 数量
	 */
	private     Integer         productNum      = null;
	/**
	 * 金額
	 */
	private     Long            productValue    = null;
	/**
	 * 割引額
	 */
	private     Long            discountValue    = null;
	/**
	 * 割合
	 */
	private     Integer         ratio           = null;
	/**
	 * アプローチ
	 */
	private     Boolean         approachedFlag  = null;
	
	/**
	 * コンストラクタ
	 */
	public ProductShare()
	{
	}

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
         * @return the seqNum
         */
        public Integer getSeqNum() {
            return seqNum;
        }

        /**
         * @param seqNum the seqNum to set
         */
        public void setSeqNum(Integer seqNum) {
            this.seqNum = seqNum;
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
         * @return the staffNo
         */
        public String getStaffNo() {
            return staffNo;
        }

        /**
         * @param staffNo the staffNo to set
         */
        public void setStaffNo(String staffNo) {
            this.staffNo = staffNo;
        }

        /**
         * @return the itemClassName
         */
        public String getItemClassName() {
            return itemClassName;
        }

        /**
         * @param itemClassName the itemClassName to set
         */
        public void setItemClassName(String itemClassName) {
            this.itemClassName = itemClassName;
        }

        /**
         * @return the itemName
         */
        public String getItemName() {
            return itemName;
        }

        /**
         * @param itemName the itemName to set
         */
        public void setItemName(String itemName) {
            this.itemName = itemName;
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
        public Long getProductValue() {
            return productValue;
        }

        /**
         * @param productValue the productValue to set
         */
        public void setProductValue(Long productValue) {
            this.productValue = productValue;
        }

        /**
         * @return the discountValue
         */
        public Long getDiscountValue() {
            return discountValue;
        }

        /**
         * @param discountValue the discountValue to set
         */
        public void setDiscountValue(Long discountValue) {
            this.discountValue = discountValue;
        }

        /**
         * @return the ratio
         */
        public Integer getRatio() {
            return ratio;
        }

        /**
         * @param ratio the ratio to set
         */
        public void setRatio(Integer ratio) {
            this.ratio = ratio;
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

        public static void sort(List list) {
            Collections.sort(list, new Comparator<ProductShare>(){
                DecimalFormat df = new DecimalFormat("00000");
                public int compare(ProductShare p1, ProductShare p2) {
                    String s1 = df.format(p1.getSlipDetailNo()) + "-" + df.format(p1.getSeqNum());
                    String s2 = df.format(p2.getSlipDetailNo()) + "-" + df.format(p2.getSeqNum());
                    return s1.compareTo(s2);
                }
            });
        }

    public ProductShare clone() throws CloneNotSupportedException {
        return (ProductShare)super.clone();
    }
}

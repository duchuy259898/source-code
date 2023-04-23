/*
 * BusinessReportBean.java
 *
 * Created on 2006/05/19, 16:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

/**
 *
 * @author k-anayama
 */
public class BusinessReportBean {
    
    
    /** GEOBECK edit 20180824 #54164
     *  price,salesCount,totalPrice,discountPrice,classTotalPrice
     *  の５つの変数をint→longに
     */
    private int		productClassId;
    private String	productClassName;
    private String	productName;
    //private int		price;
    //private int		salesCount;
    //private int		totalPrice;
    //private int		discountPrice;
    private double	classRate;
    private double	totalRate;
    private double	customerCount;
    private double	customerCost;
    private long        price;
    private long        totalPrice;
    private long        classTotalPrice;
    private long        salesCount;
    private long        discountPrice;
    
    // 分類別プロパティ
    private String classSalesCount;
    private String classTotalRate;
    //private Integer classTotalPrice;

    /** Creates a new instance of BusinessReportBean */
    public BusinessReportBean() {
    }

    public int getProductClassId() {
	    return productClassId;
    }

    public void setProductClassId(int productClassId) {
	    this.productClassId = productClassId;
    }

    public String getProductClassName() {
	    return productClassName;
    }

    public void setProductClassName(String productClassName) {
	    this.productClassName = productClassName;
    }

    public String getProductName() {
	    return productName;
    }

    public void setProductName(String productName) {
	    this.productName = productName;
    }

    /* GEOBECK start edit 20180824 #54164 */
    //    public int getPrice() {
    //	    return price;
    //    }

    //    public void setPrice(int price) {
    //	    this.price = price;
    //    }
   public void setPrice(long price) {
	    this.price = price;
    }
    
    public long getPrice() {
	    return price;
    }
    /* GEOBECK end edit 20180824 #54164 */

    /* GEOBECK start add 20160824 #54164 */
    //    public int getSalesCount() {
    //	    return salesCount;
    //    }
    //
    //    public void setSalesCount(int salesCount) {
    //	    this.salesCount = salesCount;
    //    }
     
    public void setSalesCount(long salesCount) {
	    this.salesCount = salesCount;
    }
    
    public long getSalesCount() {
	    return salesCount;
    }
    /* GEOBECK end edit 20160824 #54164 */

    /* GEOBECK start edit 20180824 #54164 */
    //    public int getTotalPrice() {
    //	    return totalPrice;
    //    }
    //
    //    public void setTotalPrice(int totalPrice) {
    //	    this.totalPrice = totalPrice;
    //    }

    public void setTotalPrice(long totalPrice) {
	    this.totalPrice = totalPrice;
    }
    
    public long getTotalPrice() {
	    return totalPrice;
    }
    /* GEOBECK end edit 20180824 #54164 */

    /* GEOBECK start edit 20160824 #54164 */
    //    public int getDiscountPrice() {
    //	    return discountPrice;
    //    }
    //
    //    public void setDiscountPrice(int discountPrice) {
    //	    this.discountPrice = discountPrice;
    //    }
    
    public void setDiscountPrice(long discountPrice) {
	    this.discountPrice = discountPrice;
    }
    
    public long getDiscountPrice() {
	    return discountPrice;
    }
    /* GEOBECK start edit 20160824 #54164 */

    public double getClassRate() {
	    return classRate;
    }

    public void setClassRate(double classRate) {
	    this.classRate = classRate;
    }

    public double getTotalRate() {
	    return totalRate;
    }

    public void setTotalRate(double totalRate) {
	    this.totalRate = totalRate;
    }

    public String getClassSalesCount() {
	    return classSalesCount;
    }

    public void setClassSalesCount(String classSalesCount) {
	    this.classSalesCount = classSalesCount;
    }

    public String getClassTotalRate() {
	    return classTotalRate;
    }

    public void setClassTotalRate(String classTotalRate) {
	    this.classTotalRate = classTotalRate;
    }

    /* GEOBECK start edit 20160824 #54164 */
    //    public void setClassTotalPrice(Integer classTotalPrice) {
    //	    this.classTotalPrice = classTotalPrice;
    //    }
    //    
    //    public Integer getClassTotalPrice() {
    //	    return classTotalPrice;
    //    }
    
    public void setClassTotalPrice(long classTotalPrice) {
	    this.classTotalPrice = classTotalPrice;
    }
    
    public long getClassTotalPrice() {
	    return classTotalPrice;
    }
    /* GEOBECK end edit 20160824 #54164 */

    public double getCustomerCount() {
	    return customerCount;
    }

    public void setCustomerCount(double customerCount) {
	    this.customerCount = customerCount;
    }

	public double getCustomerCost() {
		return customerCost;
	}

	public void setCustomerCost(double customerCost) {
		this.customerCost = customerCost;
	}

}

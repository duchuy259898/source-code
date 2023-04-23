/*
 * StaffSalesListBean.java
 *
 * Created on 2008/09/25, 19:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author trino
 */
public class StaffSalesListBean
{
    
    private String dateReq;
    private Integer staffNo;
    private String staffName;
    private String category;
    private String productName;
    private Long estimate;
    private Long discount;
    private Integer volume;
    private Long amount;
    private Long total;
    
    /** Creates a new instance of StaffSalesListBean */
    public StaffSalesListBean()
    {
    }
    
    public String getDateReq(){
        return this.dateReq;
    }
    
    public void setDateReq(String date){
        this.dateReq = date;
    }
    
    public Integer getStaffNo(){
        return this.staffNo;
    }
    
    public void setStaffNo(Integer no){
        this.staffNo = no;
    }
    
    public String getStaffName(){
        return this.staffName;
    }
    
    public void setStaffName(String name){
        this.staffName = name;
    }
    
    public String getCategory(){
        return this.category;
    }
    
    public void setCategory(String type){
        this.category = type;
    }
    
    public String getProductName(){
        return this.productName;
    }
    
    public void setProductName(String name){
        this.productName = name;
    }
    
    public Long getEstimate(){
        return this.estimate;
    }
    
    public void setEstimate(Long amount){
        this.estimate = amount;
    }
    
    public Long getDiscount(){
        return this.discount;
    }
    
    public void setDiscount(Long amount){
        this.discount = amount;
    }
    
    public Integer getVolume(){
        return this.volume;
    }
    
    public void setVolume(Integer value){
        this.volume = value;
    }
    
    public Long getAmount(){
        return this.amount;
    }
    
    public void setAmount(Long value){
        this.amount = value;
    }
    
    public Long getTotal(){
        return this.total;
    }
    
    public void setTotal(Long value){
        this.total = value;
    }
}

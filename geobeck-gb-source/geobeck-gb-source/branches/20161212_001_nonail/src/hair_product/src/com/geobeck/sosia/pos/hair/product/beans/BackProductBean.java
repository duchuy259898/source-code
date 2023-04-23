/*
 * BackProductBean.java
 *
 * Created on 2008/09/25, 16:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author trino
 */
public class BackProductBean
{
    
    private String dateReq;
    private Integer customerNo;
    private String customerName;
    private String category;
    private String productName;
    private Long estimate;
    private Integer volume;
    private Long amount;
    
    /** Creates a new instance of BackProductBean */
    public BackProductBean()
    {
    }
    
    public String getDateReq(){
        return this.dateReq;
    }
    
    public void setDateReq(String date){
        this.dateReq = date;
    }
    
    public Integer getCustomerNo(){
        return this.customerNo;
    }
    
    public void setCustomerNo(Integer no){
        this.customerNo = no;
    }
    
    public String getCustomerName(){
        return this.customerName;
    }
    
    public void setCustomerName(String name){
        this.customerName = name;
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
    
    public Integer getVolume(){
        return this.volume;
    }
    
    public void setVolume(Integer cnt) {
        this.volume = cnt;
    }
    
    public Long getAmount(){
        return this.amount;
    }
    
    public void setAmount(Long total){
        this.amount = total;
    }
}

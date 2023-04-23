/*
 * PrintInventoryCategoryBean.java
 *
 * Created on 2008/09/25, 12:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author trino
 */
public class PrintInventoryCategoryBean
{
     private String category;
     private Integer term;
     private Integer storeInCnt;
     private Integer storeAttachCnt;
     private Long storeInAmnt;
     private Integer storeOutCnt;
     private Long storeOutAmnt;
     private Integer account;
     private Integer actualCnt;
     private Integer excessCnt;
     private Long totalAmnt;
     private Long excessAmnt;
     
    
    /** Creates a new instance of PrintInventoryCategoryBean */
    public PrintInventoryCategoryBean()
    {
         category = "";
         term = 0;
         storeInCnt = 0;
         storeAttachCnt = 0;
         storeInAmnt = 0L;
         storeOutCnt = 0;
         storeOutAmnt = 0L;
         account = 0;
         actualCnt = 0;
         excessCnt = 0;
         totalAmnt = 0L;
         excessAmnt = 0L;
    }
    
    public String getCategory(){
        return this.category;
    }
    
    public Integer getTerm(){
        return this.term;
    }
    
    public Integer getStoreInCnt(){
        return this.storeInCnt;
    }

    public Integer getStoreAttachCnt(){
        return this.storeAttachCnt;
    }
    
    public Long getStoreInAmnt(){
        return this.storeInAmnt;
    }
    
    public Integer getStoreOutCnt(){
        return this.storeOutCnt;
    }
    
    public Long getStoreOutAmnt(){
        return this.storeOutAmnt;
    }
    
    public  Integer getAccount(){
        return this.account;
    }
    
    public Integer getActualCnt(){
        return this.actualCnt;
    }
    
    public Integer getExcessCnt(){
        return this.excessCnt;
    }
    
    public Long getTotalAmnt(){
        return this.totalAmnt;
    }
    
    public Long getExcessAmnt(){
        return this.excessAmnt;
    }
    
    public void setCategory(String name){
        this.category = name;
    }
    
    public void setTerm(Integer cnt){
        this.term = cnt;
    }
    
    public void setStoreInCnt(Integer cnt){
        this.storeInCnt = cnt;
    }

    public void setStoreAttachCnt(Integer cnt){
        this.storeAttachCnt = cnt;
    }

    public void setStoreInAmnt(Long amount){
        this.storeInAmnt = amount;
    }
    
    public void setStoreOutCnt(Integer cnt){
        this.storeOutCnt = cnt;
    }
    
    public void setStoreOutAmnt(Long amount){
        this.storeOutAmnt = amount;
    }
    
    public void setAccount(Integer cnt){
        this.account = cnt;
    }
    
    public void setActualCnt(Integer cnt){
        this.actualCnt = cnt;
    }
            
    public void setExcessCnt(Integer cnt){
        this.excessCnt = cnt;
    }
    
    public void setTotalAmnt(Long amount){
        this.totalAmnt = amount;
    }
    
    public void setExcessAmnt(Long amount){
        this.excessAmnt = amount;
    }
}

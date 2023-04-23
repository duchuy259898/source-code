/*
 * PlaceListBean.java
 *
 * Created on 2008/09/25, 21:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author trino
 */
public class PlaceListBean
{
    private String location;
    private Integer term;
    private Integer inCnt;
    private String category;
    private String productName;
    private Integer suited;
    private Integer outCnt;
    private Integer currentCnt;
    private Integer differ;
    
    /** Creates a new instance of PlaceListBean */
    public PlaceListBean()
    {
    }
    
    public String getLocation(){
        return this.location;
    }
    
    public void setLocation(String loc){
        this.location = loc;
    }
    
    public Integer getTerm(){
        return this.term;
    }
    
    public void setTerm(Integer cnt){
        this.term = cnt;
    }
    
    public Integer getInCnt(){
        return this.inCnt;
    }
    
    public void setInCnt(Integer value){
        this.inCnt = value;
    }
    public String getCategory(){
        return this.category;
    }
    
    public void setCategory(String value){
        this.category = value;
    }
    
    public String getProductName(){
        return this.productName;
    }
    
    public void setProductName(String value){
        this.productName = value;
    }
    
    public Integer getSuited(){
        return this.suited;
    }
    
    public void setSuited(Integer value){
        this.suited = value;
    }
    
    public Integer getOutCnt(){
        return this.outCnt;
    }
    
    public void setOutCnt(Integer value){
        this.outCnt = value;
    }
    
    public Integer getCurrentCnt(){
        return this.currentCnt;
    }
    
    public void setCurrentCnt(Integer value){
        this.currentCnt = value;
    }
    
    public Integer getDiffer(){
        return this.differ;
    }
    
    public void setDiffer(Integer value){
        this.differ = value;
    }
}

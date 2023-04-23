/*
 * RateRankingStoreBean.java
 *
 * Created on 2008/09/22, 11:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author gloridel
 */
public class RateRankingStoreBean
{
    private Integer storeIndex;
    private Integer inChargeNo;
    private String storeName;
    private Integer count;
    private Double percent;
    
    /** コンストラクタ */
    public RateRankingStoreBean(String storeName){
        this.storeName = storeName;
    }
    
    public Integer getStoreIndex(){
        return this.storeIndex;
    }
    
    public Integer getInChargeNo(){
        return this.inChargeNo;
    }
    
    public String getStoreName(){
        return this.storeName;
    }
    
    public Integer getCount() {
        return this.count;
    }
    
    public Double getPercent(){
        return this.percent;
    }
    
    public void setStoreIndex(Integer index){
        this.storeIndex = index;
    }
    
    public void setInChargeNo(Integer num){
        this.inChargeNo = num;
    }
    
    public void setStoreName(String name){
        this.storeName = name;
    }
    
    public void setCount(Integer cnt){
        this.count = cnt;
    }
    
    public void setPercent(Double rate){
        this.percent = rate;
    }
}

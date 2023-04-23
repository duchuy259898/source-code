/*
 * RateRankingGoneStaffBean.java
 *
 * Created on 2008/09/22, 11:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author gloridel
 */
public class RateRankingStaffBean
{
    private Integer staffIndex;
    private String staffName;
    private Integer inChargeNo;
    private Integer count;
    private Double percent;
    private String storeName;
    
    /** コンストラクタ */
    public RateRankingStaffBean(){}
    
    /** コンストラクタ */
    public RateRankingStaffBean(String staffName){
        this.staffName = staffName;
    }
    
    /** コンストラクタ */
    public RateRankingStaffBean(String staffName, String storeName){
        this.staffName = staffName;
        this.storeName = storeName;
    }
    
    public Integer getStaffIndex(){
        return this.staffIndex;
    }
    
    public String getStaffName(){
        return this.staffName;
    }
    
    public Integer getInChargeNo(){
        return this.inChargeNo;
    }
    
    public Integer getCount(){
        return this.count;
    }
    
    public Double getPercent(){
        return this.percent;
    }
    
    public String getStoreName(){
        return this.storeName;
    }
    
    public void setStaffIndex(Integer index){
        this.staffIndex = index;
    }
    
    public void setStaffName(String name){
        this.staffName = name;
    }
    
    public void setInChargeNo(Integer num){
        this.inChargeNo = num;
    }
    
    public void setCount(Integer cnt){
        this.count = cnt;
    }
    
    public void setPercent(Double rate){
        this.percent = rate;
    }
    
    public void setStoreName(String name){
        this.storeName = name;
    }
}

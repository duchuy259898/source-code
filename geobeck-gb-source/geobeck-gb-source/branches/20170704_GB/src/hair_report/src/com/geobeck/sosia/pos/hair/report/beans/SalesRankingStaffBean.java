/*
 * SalesRankingStaffBean.java
 *
 * Created on 2008/09/19, 17:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author gloridel
 */
public class SalesRankingStaffBean
{
    private Integer rank;
    private String staffName;
    private Long sales;
    private String store;
    
    /** コンストラクタ */
    public SalesRankingStaffBean(String staffName, String store){
        this.staffName = staffName;
        this.store = store;
    }
    
    public Integer getRank(){
        return this.rank;
    }
    
    public String getStaffName(){
        return this.staffName;
    }
    
    public Long getSales(){
        return this.sales;
    }
    
    public String getStore(){
        return this.store;
    }
    
    public void setRank(Integer pos){
        this.rank = pos;
    }
    
    public void setStaffName(String name){
        this.staffName = name;
    }
    
    public void setSales(Long samount){
        this.sales = samount;
    }
    
    public void setStore(String name){
        this.store = name;
    }
    
}

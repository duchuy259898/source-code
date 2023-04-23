/*
 * SalesRankingStoreBean.java
 *
 * Created on 2008/09/19, 17:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class SalesRankingStoreBean
{
    /** 店名 */
    private String store;
    /** 順位 */
    private Integer rank;
    /** 売上 */
    private Long sales;
    /** スタッフ数 */
    private Long staffCount;
    /** １人当売上 */
    private Long salesPerPerson;
    
    /** コンストラクタ */
    public SalesRankingStoreBean(String store){
        this.store = store;
    }
    
    /** 店名を取得する */
    public String getStore(){return store;}
    
    /** 順位を取得する */
    public Integer getRank(){return rank;}
    
    /** 売上を取得する */
    public Long getSales(){return sales;}
    
    /** スタッフ数を取得する */
    public Long getStaffCount() {return staffCount;}

    /** １人当売上を取得する */
    public Long getSalesPerPerson(){return salesPerPerson;}
    
    /** 店名を設定する */
    public void setStore(String name){this.store = name;}
    
    /** 順位を設定する */
    public void setRank(Integer pos){this.rank = pos;}
    
    /** 売上を設定する */
    public void setSales(Long amount){this.sales = amount;}

    /** スタッフ数を設定する */
    public void setStaffCount(Long staffCount) {this.staffCount = staffCount;}

    /** １人当売上を設定できません */
    public void setSalesPerPerson(Long salesPerPerson){
        this.salesPerPerson = salesPerPerson;
    }
}

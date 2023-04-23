/*
 * PrintInventoryProductBean.java
 *
 * Created on 2008/09/25, 14:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author trino
 */
public class PrintInventoryProductBean
{
    private Integer  id;
    private Integer  classId;
    private String   category;
    private Integer  termProd;
    private Integer  storeInCnt;
    private Integer  storeAttachCnt;
    private Long     storeInAmnt;
    private Integer  storeOutCnt;
    private Long     storeOutAmnt;
    private Integer  account;
    private Integer  actualCnt;
    private Integer  excessCnt;
    private Long     excessAmnt;
    private Long     totalAmnt;
    private String   stockName;
    private Long     rawProductEst;
    
    
    /** Creates a new instance of PrintInventoryProductBean */
    public PrintInventoryProductBean()
    {
        id = 0;
        classId = 0;
        category = "";
        termProd = 0;
        storeInCnt = 0;
        storeAttachCnt = 0;
        storeInAmnt = 0L;
        storeOutCnt = 0;
        storeOutAmnt = 0L;
        account = 0;
        actualCnt = 0;
        excessCnt = 0;
        excessAmnt = 0L;
        totalAmnt = 0L;
        stockName = "";
        rawProductEst = 0L;
    }
    
    public String getCategory(){
        return this.category;
    }
    
    public void setCategory(String name){
        this.category = name;
    }
    
    public Integer getTermProd(){
        return this.termProd;
    }
    
    public void setTermProd(Integer cnt){
        this.termProd = cnt;
    }
    
    public Integer getStoreInCnt(){
        return this.storeInCnt;
    }
    
    public void setStoreInCnt(Integer cnt){
        this.storeInCnt = cnt;
    }

    public Integer getStoreAttachCnt(){
        return this.storeAttachCnt;
    }
    
    public void setStoreAttachCnt(Integer cnt){
        this.storeAttachCnt = cnt;
    }

    public Long getStoreInAmnt(){
        return this.storeInAmnt;
    }
    
    public void setStoreInAmnt(Long amount){
        this.storeInAmnt = amount;
    }
    
    public Integer getStoreOutCnt(){
        return this.storeOutCnt;
    }
    
    public void setStoreOutCnt(Integer cnt){
        this.storeOutCnt = cnt;
    }
    
    public Long getStoreOutAmnt(){
        return this.storeOutAmnt;
    }
    
    public void setStoreOutAmnt(Long amount){
        this.storeOutAmnt = amount;
    }
    
    public Integer getAccount(){
        return this.account;
    }
    
    public void setAccount(Integer acct){
        this.account = acct;
    }
    
    public Integer getActualCnt(){
        return this.actualCnt;
    }
    
    public void setActualCnt(Integer cnt){
        this.actualCnt = cnt;
    }
    
    public Integer getExcessCnt(){
        return this.excessCnt;
    }
    
    public void setExcessCnt(Integer cnt){
        this.excessCnt = cnt;
    }
    
    public Long getExcessAmnt(){
        return this.excessAmnt;
    }
    
    public void setExcessAmnt(Long amount){
        this.excessAmnt = amount;
    }
    
    public Long getTotalAmnt(){
        return this.totalAmnt;
    }
    
    public void setTotalAmnt(Long amount){
        this.totalAmnt = amount;
    }
    
    public String getStockName(){
        return this.stockName;
    }
    
    public void setStockName(String name){
        this.stockName = name;
    }
    
    public Long getRawProductEst(){
        return this.rawProductEst;
    }
    
    public void setRawProductEst(Long amount){
        this.rawProductEst = amount;
    }

    public int getClassId() {
        return classId;
    }
    
    public void setClassId(int num) {
        classId = num;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int num) {
        id = num;
    }

}

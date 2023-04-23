/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author IVS
 */
public class MonthPurchaseBean {
    
    private int yearLabel;
    
    private int monthLabel;
    
    private PurchaseManagementBean item;


     public int getYearLabel() {
        return yearLabel;
    }

    public void setYearLabel(int yearLabel) {
        this.yearLabel = yearLabel;
    }

    public int getMonthLabel() {
        return monthLabel;
    }

    public void setMonthLabel(int monthLabel) {
        this.monthLabel = monthLabel;
    }
    
    public PurchaseManagementBean getItem() {
        return item;
    }

    public void setItem(PurchaseManagementBean item) {
        this.item = item;
    }
}

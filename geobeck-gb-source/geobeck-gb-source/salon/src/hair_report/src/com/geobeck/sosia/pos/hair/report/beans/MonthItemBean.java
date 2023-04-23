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
public class MonthItemBean {
    
    private int yearLabel;
    
    private int monthLabel;
    
    private CustomerItemBean itemMale;
    
    private CustomerItemBean itemFemale;

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

    public CustomerItemBean getItemMale() {
        return itemMale;
    }

    public void setItemMale(CustomerItemBean itemMale) {
        this.itemMale = itemMale;
    }

    public CustomerItemBean getItemFemale() {
        return itemFemale;
    }

    public void setItemFemale(CustomerItemBean itemFemale) {
        this.itemFemale = itemFemale;
    }
}

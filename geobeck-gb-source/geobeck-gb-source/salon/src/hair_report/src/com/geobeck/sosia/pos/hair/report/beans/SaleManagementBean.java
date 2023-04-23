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
public class SaleManagementBean {
    
    //ãZèpëçîÑè„
    private long totalTechSales;
    //ãZèpéwñº
    private long techNomination;
    //ãZèpêVãK
    private long newTech;
    //è§ïiîÑè„
    private long itemSales;
    //ëçãqêîTech
    private long totalNumTechOfCustomers;
    //éwñº
    private long designatedMainCharge;
    //êVãK
    private long newCustomer;
    //èóê´
    private long female;
    //èóê´êlêî
    private int femaleCount;
    //íjê´
    private long male;
    //íjê´êlêî
    private int maleCount;
    //èóê´î‰ó¶
    private double femaleRate;
    //ëçãqêî
    private long totalNumCustomers;

    public long getTotalTechSales() {
        return totalTechSales;
    }

    public void setTotalTechSales(long totalTechSales) {
        this.totalTechSales = totalTechSales;
    }

    public long getTechNomination() {
        return techNomination;
    }

    public void setTechNomination(long techNomination) {
        this.techNomination = techNomination;
    }

    public long getNewTech() {
        return newTech;
    }

    public void setNewTech(long newTech) {
        this.newTech = newTech;
    }

    public long getItemSales() {
        return itemSales;
    }

    public void setItemSales(long itemSales) {
        this.itemSales = itemSales;
    }

    public long getTotalNumTechOfCustomers() {
        return totalNumTechOfCustomers;
    }

    public void setTotalNumTechOfCustomers(long totalNumTechOfCustomers) {
        this.totalNumTechOfCustomers = totalNumTechOfCustomers;
    }

    public long getDesignatedMainCharge() {
        return designatedMainCharge;
    }

    public void setDesignatedMainCharge(long designatedMainCharge) {
        this.designatedMainCharge = designatedMainCharge;
    }

    public long getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(long newCustomer) {
        this.newCustomer = newCustomer;
    }

    public long getFemale() {
        return female;
    }

    public void setFemale(long female) {
        this.female = female;
    }

    public long getMale() {
        return male;
    }

    public void setMale(long male) {
        this.male = male;
    }

    public double getFemaleRate() {
        return femaleRate;
    }

    public void setFemaleRate(double femaleRate) {
        this.femaleRate = femaleRate;
    }
    
    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }
    
    public long getTotalNumCustomers() {
        return totalNumCustomers;
    }

    public void setTotalNumCustomers(long totalNumCustomers) {
        this.totalNumCustomers = totalNumCustomers;
    }
}

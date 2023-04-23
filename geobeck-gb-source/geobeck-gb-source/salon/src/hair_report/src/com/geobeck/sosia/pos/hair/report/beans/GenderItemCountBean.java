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
public class GenderItemCountBean {
    
    //íjê´	
    private CustomerItemBean     male;
    //èóê´
    private CustomerItemBean     female;

    public CustomerItemBean getMale() {
        return male;
    }

    public void setMale(CustomerItemBean male) {
        this.male = male;
    }

    public CustomerItemBean getFemale() {
        return female;
    }

    public void setFemale(CustomerItemBean female) {
        this.female = female;
    }
    
}

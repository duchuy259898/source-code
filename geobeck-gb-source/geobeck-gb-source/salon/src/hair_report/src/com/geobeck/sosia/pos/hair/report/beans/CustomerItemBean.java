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
public class CustomerItemBean {
    
    //ãqêî
    private int numberOfCustomer;
    //éwñº
    private int designated;
    //êVãK
    private int cusNew;
    //éwñº
    ServiceItemBean customerOld;
    //êVãK
    ServiceItemBean customerNew;
    
    public int getNumberOfCustomer() {
        return numberOfCustomer;
    }

    public void setNumberOfCustomer(int numberOfCustomer) {
        this.numberOfCustomer = numberOfCustomer;
    }
    
    public int getDesignated() {
        return designated;
    }

    public void setDesignated(int designated) {
        this.designated = designated;
    }

    public int getCusNew() {
        return cusNew;
    }

    public void setCusNew(int cusNew) {
        this.cusNew = cusNew;
    }

    public ServiceItemBean getCustomerOld() {
        return customerOld;
    }

    public void setCustomerOld(ServiceItemBean customerOld) {
        this.customerOld = customerOld;
    }

    public ServiceItemBean getCustomerNew() {
        return customerNew;
    }

    public void setCustomerNew(ServiceItemBean customerNew) {
        this.customerNew = customerNew;
    }
}

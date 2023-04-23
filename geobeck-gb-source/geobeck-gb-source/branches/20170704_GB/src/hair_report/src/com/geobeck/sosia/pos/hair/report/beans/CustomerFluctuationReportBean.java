/*
 * CustomerFluctuationReportBean.java
 *
 * Created on 2008/08/27, 14:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

// include files for JRBeanCollection

/**
 *
 * @author trino
 */
public class CustomerFluctuationReportBean {
    private String staffName;
    
    private Integer[] inchargedCustomerCount;
    private Integer[] newCustomerCount;
    private Double[] newCustomerRate;
    private Integer[][] lostCustomerCount;
    private Double[][] lostCustomerRate;
    
    private Double[] inchargedCustomerCountAvg;
    private Integer[] newCustomerCountAvg;
    private Double[] newCustomerRateAvg;
    private Double[] lostCustomerCountAvg;
    private Double[] lostCustomerRateAvg;
    
    public String getStaffName() {
        return staffName;
    }
    
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    
    public Integer[] getInchargedCustomerCount() {
        return inchargedCustomerCount;
    }
    
    public void setInchargedCustomerCount(Integer[] inchargedCustomerCount) {
        this.inchargedCustomerCount = inchargedCustomerCount;
    }
    
    public Integer[] getNewCustomerCount() {
        return newCustomerCount;
    }
    
    public void setNewCustomerCount(Integer[] newCustomerCount) {
        this.newCustomerCount = newCustomerCount;
    }
    
    public Double[] getNewCustomerRate() {
        return newCustomerRate;
    }
    
    public void setNewCustomerRate(Double[] newCustomerRate) {
        this.newCustomerRate = newCustomerRate;
    }
    
    public Integer[][] getLostCustomerCount() {
        return lostCustomerCount;
    }
    
    public void setLostCustomerCount(Integer[][] lostCustomerCount) {
        this.lostCustomerCount = lostCustomerCount;
    }
    
    public Double[][] getLostCustomerRate() {
        return lostCustomerRate;
    }
    
    public void setLostCustomerRate(Double[][] lostCustomerRate) {
        this.lostCustomerRate = lostCustomerRate;
    }
    
    public Double[] getInchargedCustomerCountAvg() {
        return inchargedCustomerCountAvg;
    }
    
    public void setInchargedCustomerCountAvg(Double[] inchargedCustomerCountAvg) {
        this.inchargedCustomerCountAvg = inchargedCustomerCountAvg;
    }
    
    public Integer[] getNewCustomerCountAvg() {
        return newCustomerCountAvg;
    }
    
    public void setNewCustomerCountAvg(Integer[] newCustomerCountAvg) {
        this.newCustomerCountAvg = newCustomerCountAvg;
    }
    
    public Double[] getNewCustomerRateAvg() {
        return newCustomerRateAvg;
    }
    
    public void setNewCustomerRateAvg(Double[] newCustomerRateAvg) {
        this.newCustomerRateAvg = newCustomerRateAvg;
    }
    
    public Double[] getLostCustomerCountAvg() {
        return lostCustomerCountAvg;
    }
    
    public void setLostCustomerCountAvg(Double[] lostCustomerCountAvg) {
        this.lostCustomerCountAvg = lostCustomerCountAvg;
    }
    
    public Double[] getLostCustomerRateAvg() {
        return lostCustomerRateAvg;
    }
    
    public void setLostCustomerRateAvg(Double[] lostCustomerRateAvg) {
        this.lostCustomerRateAvg = lostCustomerRateAvg;
    }
}

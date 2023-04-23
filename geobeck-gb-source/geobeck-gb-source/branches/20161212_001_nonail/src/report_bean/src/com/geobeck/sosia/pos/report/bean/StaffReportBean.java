/*
 * StaffReportBean.java
 *
 * Created on 2006/05/22, 18:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

/**
 *
 * @author k-anayama
 */
public class StaffReportBean {
    
    private String staffName;
    private int customerCount1;
    private int salesPrice1;
    private int customerCount2;
    private int salesPrice2;
    private int customerCount3;
    private int salesPrice3;
    private int customerCount4;
    private int salesPrice4;
    private int customerCount5;
    private int salesPrice5;
    private int customerCount6;
    private int salesPrice6;
    private int customerCount7;
    private int salesPrice7;
    private int totalSalesPrice;
    
    /** Creates a new instance of StaffReportBean */
    public StaffReportBean() {
    }

    public void initField() {
	for (int i=0 ; i < 7 ; i++) {
	    this.setCustomerCount(i, 0);
	    this.setSalesPrice(i, 0);
	}
    }
    
    public void setCustomerCount(int no, int customerCount) {
	switch (no) {
	    case 1: this.setCustomerCount1(customerCount); break;
	    case 2: this.setCustomerCount2(customerCount); break;
	    case 3: this.setCustomerCount3(customerCount); break;
	    case 4: this.setCustomerCount4(customerCount); break;
	    case 5: this.setCustomerCount5(customerCount); break;
	    case 6: this.setCustomerCount6(customerCount); break;
	    case 7: this.setCustomerCount7(customerCount); break;
	}
    }
    
    public void setSalesPrice(int no, int salesPrice) {
	switch (no) {
	    case 1: this.setSalesPrice1(salesPrice); break;
	    case 2: this.setSalesPrice2(salesPrice); break;
	    case 3: this.setSalesPrice3(salesPrice); break;
	    case 4: this.setSalesPrice4(salesPrice); break;
	    case 5: this.setSalesPrice5(salesPrice); break;
	    case 6: this.setSalesPrice6(salesPrice); break;
	    case 7: this.setSalesPrice7(salesPrice); break;
	}
    }
    
    public String getStaffName() {
	    return staffName;
    }

    public void setStaffName(String staffName) {
	    this.staffName = staffName;
    }

    public int getCustomerCount1() {
	    return customerCount1;
    }

    public void setCustomerCount1(int customerCount1) {
	    this.customerCount1 = customerCount1;
    }

    public int getSalesPrice1() {
	    return salesPrice1;
    }

    public void setSalesPrice1(int salesPrice1) {
	    this.salesPrice1 = salesPrice1;
    }

    public int getCustomerCount2() {
	    return customerCount2;
    }

    public void setCustomerCount2(int customerCount2) {
	    this.customerCount2 = customerCount2;
    }

    public int getSalesPrice2() {
	    return salesPrice2;
    }

    public void setSalesPrice2(int salesPrice2) {
	    this.salesPrice2 = salesPrice2;
    }

    public int getCustomerCount3() {
	    return customerCount3;
    }

    public void setCustomerCount3(int customerCount3) {
	    this.customerCount3 = customerCount3;
    }

    public int getSalesPrice3() {
	    return salesPrice3;
    }

    public void setSalesPrice3(int salesPrice3) {
	    this.salesPrice3 = salesPrice3;
    }

    public int getCustomerCount4() {
	    return customerCount4;
    }

    public void setCustomerCount4(int customerCount4) {
	    this.customerCount4 = customerCount4;
    }

    public int getSalesPrice4() {
	    return salesPrice4;
    }

    public void setSalesPrice4(int salesPrice4) {
	    this.salesPrice4 = salesPrice4;
    }

    public int getCustomerCount5() {
	    return customerCount5;
    }

    public void setCustomerCount5(int customerCount5) {
	    this.customerCount5 = customerCount5;
    }

    public int getSalesPrice5() {
	    return salesPrice5;
    }

    public void setSalesPrice5(int salesPrice5) {
	    this.salesPrice5 = salesPrice5;
    }

    public int getCustomerCount6() {
	    return customerCount6;
    }

    public void setCustomerCount6(int customerCount6) {
	    this.customerCount6 = customerCount6;
    }

    public int getSalesPrice6() {
	    return salesPrice6;
    }

    public void setSalesPrice6(int salesPrice6) {
	    this.salesPrice6 = salesPrice6;
    }

    public int getCustomerCount7() {
	    return customerCount7;
    }

    public void setCustomerCount7(int customerCount7) {
	    this.customerCount7 = customerCount7;
    }

    public int getSalesPrice7() {
	    return salesPrice7;
    }

    public void setSalesPrice7(int salesPrice7) {
	    this.salesPrice7 = salesPrice7;
    }

    public int getTotalSalesPrice() {
	    return totalSalesPrice;
    }

    public void setTotalSalesPrice(int totalSalesPrice) {
	    this.totalSalesPrice = totalSalesPrice;
    }
    
}

/*
 * SalesReportBean.java
 *
 * Created on 2006/05/21, 4:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

/**
 *
 * @author k-anayama
 */
public class SalesReportBean {
    
    private String date;
    private int totalCustomerCount;
    private int totalSalesPrice;
    private int memberCustomerCount;
    private int memberSalesPrice;
    private int guestCustomerCount;
    private int guestSalesPrice;
    private int newCustomerCount;
    private int newSalesPrice;
    private int technicTotalPrice;
    private int chargeNamedTechnicPrice;
    private int chargeAllTechnicPrice;
    private int menuChargeNamedTechnicPrice;
    private int menuChargeAllTechnicPrice;
    private int itemTotalPrice;
    private int chargeNamedItemPrice;
    private int chargeAllItemPrice;
    private int menuChargeNamedItemPrice;
    private int menuChargeAllItemPrice;
    private boolean selectedShopFlag;
    private int discountTotalPrice;
    private int salesTotalPrice;
    private int customerCostRate;
    
    /** Creates a new instance of SalesReportBean */
    public SalesReportBean() {
    }

    public String getDate() {
	    return date;
    }

    public void setDate(String date) {
	    this.date = date;
    }

    public int getTotalCustomerCount() {
	    return totalCustomerCount;
    }

    public void setTotalCustomerCount(int totalCustomerCount) {
	    this.totalCustomerCount = totalCustomerCount;
    }

    public int getTotalSalesPrice() {
	    return totalSalesPrice;
    }

    public void setTotalSalesPrice(int totalSalesPrice) {
	    this.totalSalesPrice = totalSalesPrice;
    }

    public int getMemberCustomerCount() {
	    return memberCustomerCount;
    }

    public void setMemberCustomerCount(int memberCustomerCount) {
	    this.memberCustomerCount = memberCustomerCount;
    }

    public int getMemberSalesPrice() {
	    return memberSalesPrice;
    }

    public void setMemberSalesPrice(int memberSalesPrice) {
	    this.memberSalesPrice = memberSalesPrice;
    }

    public int getGuestCustomerCount() {
	    return guestCustomerCount;
    }

    public void setGuestCustomerCount(int guestCustomerCount) {
	    this.guestCustomerCount = guestCustomerCount;
    }

    public int getGuestSalesPrice() {
	    return guestSalesPrice;
    }

    public void setGuestSalesPrice(int guestSalesPrice) {
	    this.guestSalesPrice = guestSalesPrice;
    }

    public int getNewCustomerCount() {
	    return newCustomerCount;
    }

    public void setNewCustomerCount(int newCustomerCount) {
	    this.newCustomerCount = newCustomerCount;
    }

    public int getNewSalesPrice() {
	    return newSalesPrice;
    }

    public void setNewSalesPrice(int newSalesPrice) {
	    this.newSalesPrice = newSalesPrice;
    }

    public int getTechnicTotalPrice() {
	    return technicTotalPrice;
    }
  
    public void setTechnicTotalPrice(int technicTotalPrice) {
	    this.technicTotalPrice = technicTotalPrice;
    }

    public int getChargeNamedTechnicPrice() {
	    return chargeNamedTechnicPrice;
    }
  
    public void setChargeNamedTechnicPrice(int chargeNamedTechnicPrice) {
	    this.chargeNamedTechnicPrice = chargeNamedTechnicPrice;
    }

    public int getChargeAllTechnicPrice() {
	    return chargeAllTechnicPrice;
    }
  
    public void setChargeAllTechnicPrice(int chargeAllTechnicPrice) {
	    this.chargeAllTechnicPrice = chargeAllTechnicPrice;
    }

    public int getMenuChargeNamedTechnicPrice() {
	    return menuChargeNamedTechnicPrice;
    }
  
    public void setMenuChargeNamedTechnicPrice(int menuChargeNamedTechnicPrice) {
	    this.menuChargeNamedTechnicPrice = menuChargeNamedTechnicPrice;
    }

    public int getMenuChargeAllTechnicPrice() {
	    return menuChargeAllTechnicPrice;
    }
  
    public void setMenuChargeAllTechnicPrice(int menuChargeAllTechnicPrice) {
	    this.menuChargeAllTechnicPrice = menuChargeAllTechnicPrice;
    }

    public int getItemTotalPrice() {
	    return itemTotalPrice;
    }

    public void setItemTotalPrice(int itemTotalPrice) {
	    this.itemTotalPrice = itemTotalPrice;
    }

    public int getChargeNamedItemPrice() {
	    return chargeNamedItemPrice;
    }

    public void setChargeNamedItemPrice(int chargeNamedItemPrice) {
	    this.chargeNamedItemPrice = chargeNamedItemPrice;
    }
    
    public int getChargeAllItemPrice() {
	    return chargeAllItemPrice;
    }

    public void setChargeAllItemPrice(int chargeAllItemPrice) {
	    this.chargeAllItemPrice = chargeAllItemPrice ;
    }
    
    public int getMenuChargeNamedItemPrice() {
	    return menuChargeNamedItemPrice;
    }

    public void setMenuChargeNamedItemPrice(int menuChargeNamedItemPrice) {
	    this.menuChargeNamedItemPrice = menuChargeNamedItemPrice;
    }
    
    public int getMenuChargeAllItemPrice() {
	    return menuChargeAllItemPrice;
    }

    public void setMenuChargeAllItemPrice(int menuChargeAllItemPrice) {
	    this.menuChargeAllItemPrice = menuChargeAllItemPrice ;
    }

    public boolean getSelectedShopFlag(){
            return selectedShopFlag;
    } 
    
    public void setSelectedShopFlag(boolean selectedShopFlag){
            this.selectedShopFlag=selectedShopFlag;
    }
    
    public int getDiscountTotalPrice() {
	    return discountTotalPrice;
    }

    public void setDiscountTotalPrice(int discountTotalPrice) {
	    this.discountTotalPrice = discountTotalPrice;
    }

	public int getSalesTotalPrice() {
		return salesTotalPrice;
	}

	public void setSalesTotalPrice(int salesTotalPrice) {
		this.salesTotalPrice = salesTotalPrice;
	}

	public int getCustomerCostRate() {
		return customerCostRate;
	}

	public void setCustomerCostRate(int customerCostRate) {
		this.customerCostRate = customerCostRate;
	}
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author tmtrong
 */
public class ItemDeliveryHistoryBean {
        private Integer shop_id;
	private String operation_date;
	private String customer_no;
	private String customer_name;
	private String product_name;
	private Integer delivery_num;
        private Integer product_num;
	private String staffname;

    /**
     * @return the shop_id
     */
    public Integer getShop_id() {
        return shop_id;
    }

    /**
     * @param shop_id the shop_id to set
     */
    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    /**
     * @return the operation_date
     */
    public String getOperation_date() {
        return operation_date;
    }

    /**
     * @param operation_date the operation_date to set
     */
    public void setOperation_date(String operation_date) {
        this.operation_date = operation_date;
    }

    /**
     * @return the customer_no
     */
    public String getCustomer_no() {
        return customer_no;
    }

    /**
     * @param customer_no the customer_no to set
     */
    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    /**
     * @return the customer_name
     */
    public String getCustomer_name() {
        return customer_name;
    }

    /**
     * @param customer_name the customer_name to set
     */
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    /**
     * @return the product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @param product_name the product_name to set
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * @return the delivery_num
     */
    public Integer getDelivery_num() {
        return delivery_num;
    }

    /**
     * @return the product_num
     */
    public Integer getProduct_num() {
        return product_num;
    }

    /**
     * @param product_num the product_num to set
     */
    public void setProduct_num(Integer product_num) {
        this.product_num = product_num;
    }

    /**
     * @param delivery_num the delivery_num to set
     */
    public void setDelivery_num(Integer delivery_num) {
        this.delivery_num = delivery_num;
    }

    /**
     * @return the staffname
     */
    public String getStaffname() {
        return staffname;
    }

    /**
     * @param staffname the staffname to set
     */
    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }
        
    public void setData(ResultSetWrapper rs) throws SQLException{
        this.setShop_id(rs.getInt("shop_id"));
	this.setOperation_date(rs.getString("operation_date"));
	this.setCustomer_no(rs.getString("customer_no"));
	this.setCustomer_name(rs.getString("customer_name"));
	this.setProduct_name(rs.getString("product_name"));
	this.setDelivery_num(rs.getInt("delivery_num"));
        this.setProduct_num(rs.getInt("product_num"));
	this.setStaffname(rs.getString("staffname"));
    }
}

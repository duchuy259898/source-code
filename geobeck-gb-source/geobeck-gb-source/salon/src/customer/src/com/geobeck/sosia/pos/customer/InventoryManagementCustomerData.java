/*
 * InventoryManagementCustomerData.java
 *
 * Created on 2013/03/06, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import com.geobeck.sql.*;
import java.util.*;
import java.sql.SQLException;

/**
 *
 * @author ivs_tttung
 */
public class InventoryManagementCustomerData
{
    
    //data
    private Integer shop_id = null;
    private Integer slip_no = null;
    private Integer slip_detail_no = null;
    private Integer contract_detail_no = null;
    private Date sales_date = new Date();
    private Integer customer_id = null;
    private String customer_no = null;
    private String customer_name = null;    
    private String product_name = null;    
    private Integer sd_product_num = null;
    private Integer product_value = null;
    private Integer pp_product_num = null;
    private Integer product_num_used = null;
    private Integer productPrice = null;

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }
        
    public InventoryManagementCustomerData()
    {
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException
    {  
        
        this.setShop_id(rs.getInt("shop_id"));
        this.setSlip_no(rs.getInt("slip_no"));
        this.setSlip_detail_no(rs.getInt("slip_detail_no"));
        this.setSales_date(rs.getDate("sales_date"));
        this.setCustomer_no(rs.getString("customer_no"));
        this.setCustomer_name(rs.getString("customer_name"));
        this.setProduct_name(rs.getString("product_name"));
        this.setSd_product_num(rs.getInt("sp_product_num"));
        this.setProduct_value(rs.getInt("product_value"));
        this.setPp_product_num(rs.getInt("pp_product_num"));
        this.setProduct_num_used(this.getSd_product_num()-this.getPp_product_num());
        this.setCustomer_id(rs.getInt("customer_id"));
        this.setProductPrice(rs.getInt("price"));
    }
    
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
     * @return the slip_no
     */
    public Integer getSlip_no() {
        return slip_no;
    }

    /**
     * @param slip_no the slip_no to set
     */
    public void setSlip_no(Integer slip_no) {
        this.slip_no = slip_no;
    }

    /**
     * @return the slip_detail_no
     */
    public Integer getSlip_detail_no() {
        return slip_detail_no;
    }

    /**
     * @param slip_detail_no the slip_detail_no to set
     */
    public void setSlip_detail_no(Integer slip_detail_no) {
        this.slip_detail_no = slip_detail_no;
    }

    /**
     * @return the contract_detail_no
     */
    public Integer getContract_detail_no() {
        return contract_detail_no;
    }

    /**
     * @param contract_detail_no the contract_detail_no to set
     */
    public void setContract_detail_no(Integer contract_detail_no) {
        this.contract_detail_no = contract_detail_no;
    }

    /**
     * @return the sales_date
     */
    public Date getSales_date() {
        return sales_date;
    }

    /**
     * @param sales_date the sales_date to set
     */
    public void setSales_date(Date sales_date) {
        this.sales_date = sales_date;
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
     * @return the sd_product_num
     */
    public Integer getSd_product_num() {
        return sd_product_num;
    }

    /**
     * @param sd_product_num the sd_product_num to set
     */
    public void setSd_product_num(Integer sd_product_num) {
        this.sd_product_num = sd_product_num;
    }

    /**
     * @return the product_value
     */
    public Integer getProduct_value() {
        return product_value;
    }

    /**
     * @param product_value the product_value to set
     */
    public void setProduct_value(Integer product_value) {
        this.product_value = product_value;
    }

    /**
     * @return the pp_product_num
     */
    public Integer getPp_product_num() {
        return pp_product_num;
    }

    /**
     * @param pp_product_num the pp_product_num to set
     */
    public void setPp_product_num(Integer pp_product_num) {
        this.pp_product_num = pp_product_num;
    }

    /**
     * @return the product_num_used
     */
    public Integer getProduct_num_used() {
        return product_num_used;
    }

    /**
     * @param product_num_used the product_num_used to set
     */
    public void setProduct_num_used(Integer product_num_used) {
        this.product_num_used = product_num_used;
    }

    /**
     * @return the customer_id
     */
    public Integer getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id the customer_id to set
     */
    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }
    
}

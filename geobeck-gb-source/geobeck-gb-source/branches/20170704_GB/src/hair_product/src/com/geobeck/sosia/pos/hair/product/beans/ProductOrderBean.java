/*
 * ProductOrderBean.java
 *
 * Created on 2008/09/24, 16:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

/**
 *
 * @author takeda
 */
public class ProductOrderBean {

    public ProductOrderBean(Integer Index,
                                String ProductId,
                                String ProductName,
                                Integer ProductPrice,
                                Integer Quantity,
                                Integer Price) {
    
        this.index = Index;               
        this.productId = ProductId ;      
        this.productName = ProductName;  
        this.productPrice = ProductPrice;
        this.quantity = Quantity;       
        this.price = Price;          
        
    }
    
    private Integer index;          // �C���f�b�N�X
    private String productId;       // ���i�^��
    private String productName;     // ���i��
    private Integer productPrice;   // �P��
    private Integer quantity;       // ����
    private Integer price;          // �l�i

    /**
     * Getter for property productName.
     * @return Value of property productName.
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * Setter for property productName.
     * @param productName New value of property productName.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }


    /**
     * Getter for property index.
     * @return Value of property index.
     */
    public Integer getIndex() {
        return this.index;
    }

    /**
     * Setter for property index.
     * @param index New value of property index.
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Getter for property productId.
     * @return Value of property productId.
     */
    public String getProductId() {
        return this.productId;
    }

    /**
     * Setter for property productId.
     * @param productId New value of property productId.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }


    /**
     * Getter for property productPrice.
     * @return Value of property productPrice.
     */
    public Integer getProductPrice() {
        return this.productPrice;
    }

    /**
     * Setter for property productPrice.
     * @param productPrice New value of property productPrice.
     */
    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Getter for property quantity.
     * @return Value of property quantity.
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Setter for property quantity.
     * @param quantity New value of property quantity.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Getter for property price.
     * @return Value of property price.
     */
    public Integer getPrice() {
        return this.price;
    }

    /**
     * Setter for property price.
     * @param price New value of property price.
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.product.beans;
import java.util.Date;
/**
 *
 * @author tmtrong
 */
public class MoveStockReportBean {
    // �X�̋Ɩ��敪
    private String item_use_division;
    // �d����
    private String supplier_name;
    // ����
    private String item_class_name;
    // ���i��
    private String item_name;
     // �d���P��
    private Integer cost_price;
    // �̔����i
    private Integer price;
    //������X�ܖ�
    private String shop_name;
    // �o�ɒS����
    private String staff_name;
    // �o�ɓ�
    private Date ship_date;
    // �o�ɐ�
    private Integer out_num;
    // �m�F��
    private Date store_date;
    // �m�F�S����
    private String confirmer_name;

    /**
     * @return the item_use_division
     */
    public String getItem_use_division() {
        return item_use_division;
    }

    /**
     * @param item_use_division the item_use_division to set
     */
    public void setItem_use_division(String item_use_division) {
        this.item_use_division = item_use_division;
    }

    /**
     * @return the supplier_name
     */
    public String getSupplier_name() {
        return supplier_name;
    }

    /**
     * @param supplier_name the supplier_name to set
     */
    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    /**
     * @return the item_class_name
     */
    public String getItem_class_name() {
        return item_class_name;
    }

    /**
     * @param item_class_name the item_class_name to set
     */
    public void setItem_class_name(String item_class_name) {
        this.item_class_name = item_class_name;
    }

    /**
     * @return the item_name
     */
    public String getItem_name() {
        return item_name;
    }

    /**
     * @param item_name the item_name to set
     */
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    /**
     * @return the cost_price
     */
    public Integer getCost_price() {
        return cost_price;
    }

    /**
     * @param cost_price the cost_price to set
     */
    public void setCost_price(Integer cost_price) {
        this.cost_price = cost_price;
    }

    /**
     * @return the price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return the shop_name
     */
    public String getShop_name() {
        return shop_name;
    }

    /**
     * @param shop_name the shop_name to set
     */
    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    /**
     * @return the staff_name
     */
    public String getStaff_name() {
        return staff_name;
    }

    /**
     * @param staff_name the staff_name to set
     */
    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    /**
     * @return the ship_date
     */
    public Date getShip_date() {
        return ship_date;
    }

    /**
     * @param ship_date the ship_date to set
     */
    public void setShip_date(Date ship_date) {
        this.ship_date = ship_date;
    }

    /**
     * @return the out_num
     */
    public Integer getOut_num() {
        return out_num;
    }

    /**
     * @param out_num the out_num to set
     */
    public void setOut_num(Integer out_num) {
        this.out_num = out_num;
    }

    /**
     * @return the store_date
     */
    public Date getStore_date() {
        return store_date;
    }

    /**
     * @param store_date the store_date to set
     */
    public void setStore_date(Date store_date) {
        this.store_date = store_date;
    }

    /**
     * @return the confirmer_name
     */
    public String getConfirmer_name() {
        return confirmer_name;
    }

    /**
     * @param confirmer_name the confirmer_name to set
     */
    public void setConfirmer_name(String confirmer_name) {
        this.confirmer_name = confirmer_name;
    }
}

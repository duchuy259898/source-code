/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IVS
 */
public class CustomizePurchaseBean {
    
    private String name;
    
    private Integer id;
    
    private List<MonthPurchaseBean> monthItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MonthPurchaseBean> getMonthItems() {
        if (monthItems == null) {
            monthItems = new ArrayList<>();
        }
        return monthItems;
    }

    public void setMonthItems(List<MonthPurchaseBean> monthItems) {
        this.monthItems = monthItems;
    }
}

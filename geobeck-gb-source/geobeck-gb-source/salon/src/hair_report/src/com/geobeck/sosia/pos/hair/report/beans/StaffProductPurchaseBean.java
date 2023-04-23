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
public class StaffProductPurchaseBean {
    //スタッフ名
    private String name;
    //スタッフID
    private Integer id;
    private List<MonthProductPurchaseBean> monthItems;

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

    public List<MonthProductPurchaseBean> getMonthItems() {
        if (monthItems == null) {
            monthItems = new ArrayList<>();
        }
        return monthItems;
    }

    public void setMonthItems(List<MonthProductPurchaseBean> monthItems) {
        this.monthItems = monthItems;
    }
}

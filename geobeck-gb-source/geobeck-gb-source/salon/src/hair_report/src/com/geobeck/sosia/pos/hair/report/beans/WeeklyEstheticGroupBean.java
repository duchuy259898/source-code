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
public class WeeklyEstheticGroupBean {
    private String name;
    
    private Integer id;
    
    private List<WeeklyEstheticBean> weekItems;

    public WeeklyEstheticGroupBean() {
        
    }
    
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
    
    public List<WeeklyEstheticBean> getWeekItems() {
        if (weekItems == null) {
            weekItems = new ArrayList<>();
        }
        return weekItems;
    }

    public void setWeekItems(List<WeeklyEstheticBean> weekItems) {
        this.weekItems = weekItems;
    }
}

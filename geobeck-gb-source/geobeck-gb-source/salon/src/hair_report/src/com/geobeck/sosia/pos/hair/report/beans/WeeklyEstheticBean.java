/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author IVS
 */
public class WeeklyEstheticBean {
    
    private String firstDayOfWeek;
    
    private EstheticInWeekItemBean item;

    public String getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(String firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public EstheticInWeekItemBean getItem() {
        return item;
    }

    public void setItem(EstheticInWeekItemBean item) {
        this.item = item;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;

/**
 *
 * @author lvtu
 */
public class DataTarget {
    
    public DataTarget(){
    }
    
    private Integer shopId  = 0 ;
    private Integer shopCategoryId = 0;
    private Integer year = 0;
    private Integer month = 0;
    private Integer technic = 0;
    private Double BeforeReserve = 0d;
    private Double NextReserve = 0d;
    private Integer decyl_1Rate = 0;
    private Integer decyl_1Num = 0;
    private Integer decyl_2Rate = 0;
    private Integer decyl_2Num = 0;
    private Integer decyl_3Rate = 0;
    private Integer decyl_3Num = 0;

    public Integer getDecyl_1Rate() {
        return decyl_1Rate;
    }

    public void setDecyl_1Rate(Integer decyl_1Rate) {
        this.decyl_1Rate = decyl_1Rate;
    }

    public Integer getDecyl_1Num() {
        return decyl_1Num;
    }

    public void setDecyl_1Num(Integer decyl_1Num) {
        this.decyl_1Num = decyl_1Num;
    }

    public Integer getDecyl_2Rate() {
        return decyl_2Rate;
    }

    public void setDecyl_2Rate(Integer decyl_2Rate) {
        this.decyl_2Rate = decyl_2Rate;
    }

    public Integer getDecyl_2Num() {
        return decyl_2Num;
    }

    public void setDecyl_2Num(Integer decyl_2Num) {
        this.decyl_2Num = decyl_2Num;
    }

    public Integer getDecyl_3Rate() {
        return decyl_3Rate;
    }

    public void setDecyl_3Rate(Integer decyl_3Rate) {
        this.decyl_3Rate = decyl_3Rate;
    }

    public Integer getDecyl_3Num() {
        return decyl_3Num;
    }

    public void setDecyl_3Num(Integer decyl_3Num) {
        this.decyl_3Num = decyl_3Num;
    }

    public Double getBeforeReserve() {
        return BeforeReserve;
    }

    public void setBeforeReserve(Double BeforeReserve) {
        this.BeforeReserve = BeforeReserve;
    }

    public Double getNextReserve() {
        return NextReserve;
    }

    public void setNextReserve(Double NextReserve) {
        this.NextReserve = NextReserve;
    }

   
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
    
     public Integer getTechnic() {
        return technic;
    }

    public void setTechnic(Integer technic) {
        this.technic = technic;
    }
    
    
    /**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            this.setYear(rs.getInt("year"));
            this.setYear(rs.getInt("month"));
            this.setTechnic(rs.getInt("technic_value"));
	}
    
}

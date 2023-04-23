/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 業務報告（消化一覧）　コース情報
 * @author s_furukawa
 */
public class BusinessConsumptionReportCourseBean {
    //コースID
    private int courseId;
    //コース名
    private String courseName;
    //コースマスタに設定してある消化回数
    private int num;
    //コースマスタに設定してある価格（基本的な販売価格）
    private Integer basePrice = null;
    //店舗使用商品・技術マスタに設定してある価格(店頭での販売価格)
    private Integer salesPrice = null;
    //トータル消化回数
    private double totalConsumptionNum;
    //トータル消化金額
    private double totalConsumptionValue;

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getTotalConsumptionNum() {
        return totalConsumptionNum;
    }

    public void setTotalConsumptionNum(double totalConsumptionNum) {
        this.totalConsumptionNum = totalConsumptionNum;
    }

    public double getTotalConsumptionValue() {
        BigDecimal a = new BigDecimal(totalConsumptionValue);
        a = a.setScale(0, RoundingMode.HALF_UP);
        return a.longValue();
//        return new BigDecimal(getConsumptionPrice()).multiply(new BigDecimal(getTotalConsumptionNum())).doubleValue();
    }
    
    //IVS_LVTu start add 2016/03/11 Bug #49052
    public double getTotalConsumption() {
        return totalConsumptionValue;
    }
    //IVS_LVTu end add 2016/03/11 Bug #49052

    public void setTotalConsumptionValue(double totalConsumptionValue) {
        this.totalConsumptionValue = totalConsumptionValue;
    }

    /**
     * １消化に対する金額を返す
     * @return
     */
    public double getConsumptionPrice(){
        int price = basePrice;
        if(salesPrice != null){
            price = salesPrice;
        }
        // vtbphuong start add 20150519 
        if(num==0){
            return 0;
        }
        // vtbphuong end add 20150519

        return new BigDecimal(price).divide(new BigDecimal(num), 0, RoundingMode.UP).doubleValue();
    }
}

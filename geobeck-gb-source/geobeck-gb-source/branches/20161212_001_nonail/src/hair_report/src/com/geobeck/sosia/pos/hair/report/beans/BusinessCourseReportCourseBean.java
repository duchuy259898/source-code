/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author s_furukawa
 */
public class BusinessCourseReportCourseBean {
    //R[XID
    private int courseId;
    //R[X¼
    private String courseName;
    //î{¿i
    private Integer basePrice;
    //Ì¿i
    private Integer salesPrice;
    //g[^ãÊ
    private int totalSalesNum;
    //g[^ãz
    private int totalSalesValue;

    //LVTu start add 2015/03/20 Bug #35449
    private double totalSalesNumDouble;

    public double getTotalSalesNumDouble() {
        return totalSalesNumDouble;
    }

    public void setTotalSalesNumDouble(double totalSalesNumDouble) {
        this.totalSalesNumDouble = totalSalesNumDouble;
    }
    //LVTu end add 2015/03/20 Bug #35449
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

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getTotalDiscountValue() {
        return totalDiscountValue;
    }

    public void setTotalDiscountValue(int totalDiscountValue) {
        this.totalDiscountValue = totalDiscountValue;
    }

    public int getTotalSalesNum() {
        return totalSalesNum;
    }

    public void setTotalSalesNum(int totalSalesNum) {
        this.totalSalesNum = totalSalesNum;
    }

    public int getTotalSalesValue() {
        return totalSalesValue;
    }

    public void setTotalSalesValue(int totalSalesValue) {
        this.totalSalesValue = totalSalesValue;
    }
    //g[^øz
    private int totalDiscountValue;
}

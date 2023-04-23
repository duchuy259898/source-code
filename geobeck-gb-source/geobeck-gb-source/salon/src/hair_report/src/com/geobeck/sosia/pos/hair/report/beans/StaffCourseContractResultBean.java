/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author s_furukawa
 */
public class StaffCourseContractResultBean {
    //スタッフID
    private Integer staffId;
    //スタッフ名1
    private String staffName1;
    //スタッフ名2
    private String staffName2;
    //人数
    private Integer totalSalesNum;
    //売上額
    private Integer totalSalesValue;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffName1() {
        return staffName1;
    }

    public void setStaffName1(String staffName1) {
        this.staffName1 = staffName1;
    }

    public String getStaffName2() {
        return staffName2;
    }

    public String getStaffName(){
        return this.staffName1 + " " + this.staffName2;
    }

    public void setStaffName2(String staffName2) {
        this.staffName2 = staffName2;
    }

    public Integer getTotalSalesNum() {
        return totalSalesNum;
    }

    public void setTotalSalesNum(Integer totalSalesNum) {
        this.totalSalesNum = totalSalesNum;
    }

    public Integer getTotalSalesValue() {
        return totalSalesValue;
    }

    public void setTotalSalesValue(Integer totalSalesValue) {
        this.totalSalesValue = totalSalesValue;
    }
    
}

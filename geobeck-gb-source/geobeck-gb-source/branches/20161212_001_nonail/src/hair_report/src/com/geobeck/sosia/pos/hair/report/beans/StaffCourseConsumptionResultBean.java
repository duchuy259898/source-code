/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author s_furukawa
 */
public class StaffCourseConsumptionResultBean {
    //�X�^�b�tID
    private Integer staffId;
    //�X�^�b�t��1
    private String staffName1;
    //�X�^�b�t��2
    private String staffName2;
    //�l��
    private Integer totalCustomerNum;
    //�����z
    private Float totalConsumptionValue;

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

    public Integer getTotalCustomerNum() {
        return totalCustomerNum;
    }

    public void setTotalCustomerNum(Integer totalCustomerNum) {
        this.totalCustomerNum = totalCustomerNum;
    }

    public Float getTotalConsumptionValue() {
        return totalConsumptionValue;
    }

    public void setTotalConsumptionValue(Float totalConsumptionValue) {
        this.totalConsumptionValue = totalConsumptionValue;
    }
    
}

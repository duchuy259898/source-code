/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * �Ɩ��񍐁i�����ꗗ�j�@�R�[�X���
 * @author s_furukawa
 */
public class BusinessConsumptionReportCourseBean {
    //�R�[�XID
    private int courseId;
    //�R�[�X��
    private String courseName;
    //�R�[�X�}�X�^�ɐݒ肵�Ă��������
    private int num;
    //�R�[�X�}�X�^�ɐݒ肵�Ă��鉿�i�i��{�I�Ȕ̔����i�j
    private Integer basePrice = null;
    //�X�܎g�p���i�E�Z�p�}�X�^�ɐݒ肵�Ă��鉿�i(�X���ł̔̔����i)
    private Integer salesPrice = null;
    //�g�[�^��������
    private double totalConsumptionNum;
    //�g�[�^���������z
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
     * �P�����ɑ΂�����z��Ԃ�
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

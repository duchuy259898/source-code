/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * �Ɩ��񍐁i�����ꗗ�j�@�S�R�[�X���
 * @author s_furukawa
 */
public class BusinessConsumptionReportBean {
    //�R�[�X�N���XID
    private int courseClassId;
    //�R�[�X���ޖ�
    private String courseClassName;
    //�R�[�X���X�g
    private List<BusinessConsumptionReportCourseBean> courseList = new ArrayList<BusinessConsumptionReportCourseBean>();

    public int getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(int courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public List<BusinessConsumptionReportCourseBean> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<BusinessConsumptionReportCourseBean> courseList) {
        this.courseList = courseList;
    }

    public void addCourse(BusinessConsumptionReportCourseBean bean){
        this.courseList.add(bean);
    }
}

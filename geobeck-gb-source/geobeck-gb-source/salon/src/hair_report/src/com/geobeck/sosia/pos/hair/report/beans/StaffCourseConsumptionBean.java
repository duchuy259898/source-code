/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author s_furukawa
 */
public class StaffCourseConsumptionBean {
    //コース分類ID
    private int courseClassId;
    //コース分類名
    private String courseClassName;

    private List<StaffCourseConsumptionResultBean> courseConsumptionResultList = new ArrayList<StaffCourseConsumptionResultBean>();

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

    public List<StaffCourseConsumptionResultBean> getCourseConsumptionResultList() {
        return courseConsumptionResultList;
    }

    public void setCourseConsumptionResultList(List<StaffCourseConsumptionResultBean> courseConsumptionResultList) {
        this.courseConsumptionResultList = courseConsumptionResultList;
    }

    public void addCourseConsumptionResult(StaffCourseConsumptionResultBean bean){
        this.courseConsumptionResultList.add(bean);
    }
}

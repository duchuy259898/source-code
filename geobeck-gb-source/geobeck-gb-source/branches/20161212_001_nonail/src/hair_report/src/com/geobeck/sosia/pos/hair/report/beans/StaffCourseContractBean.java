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
public class StaffCourseContractBean {
    //コース分類ID
    private int courseClassId;
    //コース分類名
    private String courseClassName;

    private List<StaffCourseContractResultBean> courseContractResultList = new ArrayList<StaffCourseContractResultBean>();

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

    public List<StaffCourseContractResultBean> getCourseContractResultList() {
        return courseContractResultList;
    }

    public void setCourseContractResultList(List<StaffCourseContractResultBean> courseContractResultList) {
        this.courseContractResultList = courseContractResultList;
    }

    public void addCourseCOntractResult(StaffCourseContractResultBean bean){
        this.courseContractResultList.add(bean);
    }
}

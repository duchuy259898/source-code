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
    //�R�[�X����ID
    private int courseClassId;
    //�R�[�X���ޖ�
    private String courseClassName;
    // 20170322 Start add Bug #61234 [gb]�Z�p���с��R�[�X�_�񐬐т̌v�Z�s��
    //�R�[�X���ޕ\����
    private int courseClassDisplaySeq;
    // 20170322 End add Bug #61234

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
    
    // 20170322 Start add Bug #61234 [gb]�Z�p���с��R�[�X�_�񐬐т̌v�Z�s��
    public int getCourseClassDisplaySeq() {
        return courseClassDisplaySeq;
    }
    
    public void setCourseClassDisplaySeq(int courseClassDisplaySeq) {
        this.courseClassDisplaySeq = courseClassDisplaySeq;
    }
    // 20170322 End add Bug #61234

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

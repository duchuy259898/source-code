/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

import java.sql.Timestamp;

/**
 *
 * @author ivs
 */
public class DataTargetStaffNormalBean {
    
    //�X��ID
    private int shopId;
    
    //�X�^�b�tID
    private int staff_id;
    
    //�N
    private int year;
    
    //��
    private int month;
    
   
    //�Z�p����
    private int targetTechnic;
    private int targetCourse;
    private int targetDigestion;
    private int targetItem;
   
    
    //�Z�p�q��
    private int target_technic_num;
    
    //�w������
    private int target_nomination_value;
            
    //�w���q��
    private int target_nomination_num;
    
    //�V�K�q��
    private int target_new_num;
    
    //�Љ�q��
    private int target_introduction_num;
    
    //�X�̋q��
    private int target_item_num;
    
    //�X�^�b�t1�l�����蔄��
    private int target_staff_per_sales;
    
    //�X�^�b�t�l��
    private int target_staff_num;
    
    //�X�̔䗦
    private int target_item_rate;
    
    //����\��
    private int target_next_reserve_rate;
    
    //�\�񐬖�
    private int target_reserve_close_rate;
    
    //�V�K�ė����i1�J���j
    private int repert_30_new;
    
    //�V�K�ė����i45���j
    private int repert_45_new;
    
    //�V�K�ė����i2�J���j
    private int repert_60_new;
    
    //�V�K�ė����i3�J���j
    private int repert_90_new;
    
    //�V�K�ė����i4�����j
    private int repert_120_new;
    
    //�V�K�ė����i5�����j
    private int repert_150_new;
    
    //�V�K�ė����i6�����j
    private int repert_180_new;
    
    //�����ė����i1�J���j
    private int repert_30_fix;
    
    //�����ė����i45���j
    private int repert_45_fix;
    
    //�����ė����i2�J���j
    private int repert_60_fix;
    
    //�����ė����i3�J���j
    private int repert_90_fix;
    
    //�����ė����i4�����j
    private int repert_120_fix;
    
    //�����ė����i5�����j
    private int repert_150_fix;
    
    //�����ė����i6�����j
    private int repert_180_fix;

    
    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }
    public int getTargetCourse() {
        return targetCourse;
    }

    public void setTargetCourse(int targetCourse) {
        this.targetCourse = targetCourse;
    }

    public int getTargetDigestion() {
        return targetDigestion;
    }

    public void setTargetDigestion(int targetDigestion) {
        this.targetDigestion = targetDigestion;
    }
    public int getTarget_technic_num() {
        return target_technic_num;
    }
    
    public void setTarget_technic_num(int target_technic_num) {
        this.target_technic_num = target_technic_num;
    }

    public int getTarget_nomination_num() {
        return target_nomination_num;
    }

    public void setTarget_nomination_num(int target_nomination_num) {
        this.target_nomination_num = target_nomination_num;
    }

    public int getTarget_new_num() {
        return target_new_num;
    }

    public void setTarget_new_num(int target_new_num) {
        this.target_new_num = target_new_num;
    }

    public int getTarget_introduction_num() {
        return target_introduction_num;
    }

    public void setTarget_introduction_num(int target_introduction_num) {
        this.target_introduction_num = target_introduction_num;
    }

    public int getTarget_item_num() {
        return target_item_num;
    }

    public void setTarget_item_num(int target_item_num) {
        this.target_item_num = target_item_num;
    }

    public int getTarget_staff_per_sales() {
        return target_staff_per_sales;
    }

    public void setTarget_staff_per_sales(int target_staff_per_sales) {
        this.target_staff_per_sales = target_staff_per_sales;
    }

    public int getTarget_staff_num() {
        return target_staff_num;
    }

    public void setTarget_staff_num(int target_staff_num) {
        this.target_staff_num = target_staff_num;
    }

    public int getTarget_item_rate() {
        return target_item_rate;
    }

    public void setTarget_item_rate(int target_item_rate) {
        this.target_item_rate = target_item_rate;
    }

    public int getTarget_next_reserve_rate() {
        return target_next_reserve_rate;
    }

    public void setTarget_next_reserve_rate(int target_next_reserve_rate) {
        this.target_next_reserve_rate = target_next_reserve_rate;
    }

    public int getTarget_reserve_close_rate() {
        return target_reserve_close_rate;
    }

    public void setTarget_reserve_close_rate(int target_reserve_close_rate) {
        this.target_reserve_close_rate = target_reserve_close_rate;
    }

    public int getRepert_30_new() {
        return repert_30_new;
    }

    public void setRepert_30_new(int repert_30_new) {
        this.repert_30_new = repert_30_new;
    }

    public int getRepert_45_new() {
        return repert_45_new;
    }

    public void setRepert_45_new(int repert_45_new) {
        this.repert_45_new = repert_45_new;
    }

    public int getRepert_60_new() {
        return repert_60_new;
    }

    public void setRepert_60_new(int repert_60_new) {
        this.repert_60_new = repert_60_new;
    }

    public int getRepert_90_new() {
        return repert_90_new;
    }

    public void setRepert_90_new(int repert_90_new) {
        this.repert_90_new = repert_90_new;
    }

    public int getRepert_120_new() {
        return repert_120_new;
    }

    public void setRepert_120_new(int repert_120_new) {
        this.repert_120_new = repert_120_new;
    }

    public int getRepert_150_new() {
        return repert_150_new;
    }

    public void setRepert_150_new(int repert_150_new) {
        this.repert_150_new = repert_150_new;
    }

    public int getRepert_180_new() {
        return repert_180_new;
    }

    public void setRepert_180_new(int repert_180_new) {
        this.repert_180_new = repert_180_new;
    }

    public int getRepert_30_fix() {
        return repert_30_fix;
    }

    public void setRepert_30_fix(int repert_30_fix) {
        this.repert_30_fix = repert_30_fix;
    }

    public int getRepert_45_fix() {
        return repert_45_fix;
    }

    public void setRepert_45_fix(int repert_45_fix) {
        this.repert_45_fix = repert_45_fix;
    }

    public int getRepert_60_fix() {
        return repert_60_fix;
    }

    public void setRepert_60_fix(int repert_60_fix) {
        this.repert_60_fix = repert_60_fix;
    }

    public int getRepert_90_fix() {
        return repert_90_fix;
    }

    public void setRepert_90_fix(int repert_90_fix) {
        this.repert_90_fix = repert_90_fix;
    }

    public int getRepert_120_fix() {
        return repert_120_fix;
    }

    public void setRepert_120_fix(int repert_120_fix) {
        this.repert_120_fix = repert_120_fix;
    }

    public int getRepert_150_fix() {
        return repert_150_fix;
    }

    public void setRepert_150_fix(int repert_150_fix) {
        this.repert_150_fix = repert_150_fix;
    }

    public int getRepert_180_fix() {
        return repert_180_fix;
    }

    public void setRepert_180_fix(int repert_180_fix) {
        this.repert_180_fix = repert_180_fix;
    }
    
    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTargetTechnic() {
        return targetTechnic;
    }

    public void setTargetTechnic(int targetTechnic) {
        this.targetTechnic = targetTechnic;
    }

    public int getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(int targetItem) {
        this.targetItem = targetItem;
    }

    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Timestamp getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Timestamp deleteDate) {
        this.deleteDate = deleteDate;
    }
    private Timestamp insertDate;
    private Timestamp updateDate;
    private Timestamp deleteDate;
    public int getTarget_nomination_value() {
        return target_nomination_value;
    }

    public void setTarget_nomination_value(int target_nomination_value) {
        this.target_nomination_value = target_nomination_value;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author ivs
 */
public class SearchTargetResultBean {
    private ArrayList month;
    
    //�Z�p����
    private ArrayList targetTechnic;
    
    //���i����
    private ArrayList targetItem;
    
    //�_�񔄏�
    private ArrayList targetCourse;
    
    //�w������
    private ArrayList target_nomination_value;

    //��������
    private ArrayList targetDigestion;
     
    //�Z�p�q��
    private ArrayList target_technic_num;
    
    //�w���q��
    private ArrayList target_nomination_num;
    
    //�V�K�q��
    private ArrayList target_new_num;
    
    //�Љ�q��
    private ArrayList target_introduction_num;
    
    //�X�̋q��
    private ArrayList target_item_num;
    
    //�X�^�b�t1�l�����蔄��
    private ArrayList target_staff_per_sales;
    
    //�X�^�b�t�l��
    private ArrayList target_staff_num;
    
    //�X�̔䗦
    private ArrayList target_item_rate;
    
    //����\��
    private ArrayList target_next_reserve_rate;
    
    //�\�񐬖�
    private ArrayList target_reserve_close_rate;
    
    //�V�K�ė����i1�J���j
    private ArrayList repert_30_new;
    
    //�V�K�ė����i45���j
    private ArrayList repert_45_new;
    
    //�V�K�ė����i2�J���j
    private ArrayList repert_60_new;
    
    //�V�K�ė����i3�J���j
    private ArrayList repert_90_new;
    
    //�V�K�ė����i4�����j
    private ArrayList repert_120_new;
    
    //�V�K�ė����i5�����j
    private ArrayList repert_150_new;
    
    //�V�K�ė����i6�����j
    private ArrayList repert_180_new;
    
    //�����ė����i1�J���j
    private ArrayList repert_30_fix;
    
    //�����ė����i45���j
    private ArrayList repert_45_fix;
    
    //�����ė����i2�J���j
    private ArrayList repert_60_fix;
    
    //�����ė����i3�J���j
    private ArrayList repert_90_fix;
    
    //�����ė����i4�����j
    private ArrayList repert_120_fix;
    
    //�����ė����i5�����j
    private ArrayList repert_150_fix;
    
    //�����ė����i6�����j
    private ArrayList repert_180_fix;

    
    public ArrayList getTargetCourse() {
        return targetCourse;
    }

    public void setTargetCourse(ArrayList targetCourse) {
        this.targetCourse = targetCourse;
    }

    public ArrayList getTargetDigestion() {
        return targetDigestion;
    }

    public void setTargetDigestion(ArrayList targetDigestion) {
        this.targetDigestion = targetDigestion;
    }
   

    public SearchTargetResultBean() {
        month = new ArrayList();
        targetItem = new ArrayList();
        targetTechnic = new ArrayList();
        targetCourse = new ArrayList();
        targetDigestion = new ArrayList();
        target_technic_num = new ArrayList();
         target_nomination_value = new ArrayList();
        target_nomination_num= new ArrayList();
        target_new_num= new ArrayList();
        target_introduction_num = new ArrayList();
        target_item_num= new ArrayList();
        target_staff_per_sales= new ArrayList();
        target_staff_num= new ArrayList();
        target_item_rate= new ArrayList();
        target_next_reserve_rate= new ArrayList();
        target_reserve_close_rate= new ArrayList();
        repert_30_new= new ArrayList();
        repert_45_new= new ArrayList();
        repert_60_new= new ArrayList();
        repert_90_new= new ArrayList();
        repert_120_new= new ArrayList();
        repert_150_new= new ArrayList();
        repert_180_new= new ArrayList();
        repert_30_fix= new ArrayList();
        repert_45_fix= new ArrayList();
        repert_60_fix= new ArrayList();
        repert_90_fix= new ArrayList();
        repert_120_fix= new ArrayList();
        repert_150_fix= new ArrayList();
        repert_180_fix= new ArrayList();

    }
     public ArrayList getTarget_nomination_value() {
        return target_nomination_value;
    }

    public void setTarget_nomination_value(ArrayList target_nomination_value) {
        this.target_nomination_value = target_nomination_value;
    }
    public ArrayList getMonth() {
        return month;
    }

    public void setMonth(ArrayList month) {
        this.month = month;
    }

    public ArrayList getTargetTechnic() {
        return targetTechnic;
    }

    public void setTargetTechnic(ArrayList targetTechnic) {
        this.targetTechnic = targetTechnic;
    }

    public ArrayList getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(ArrayList targetItem) {
        this.targetItem = targetItem;
    }

    public ArrayList getTarget_technic_num() {
        return target_technic_num;
    }

    public void setTarget_technic_num(ArrayList target_technic_num) {
        this.target_technic_num = target_technic_num;
    }

    public ArrayList getTarget_nomination_num() {
        return target_nomination_num;
    }

    public void setTarget_nomination_num(ArrayList target_nomination_num) {
        this.target_nomination_num = target_nomination_num;
    }

    public ArrayList getTarget_new_num() {
        return target_new_num;
    }

    public void setTarget_new_num(ArrayList target_new_num) {
        this.target_new_num = target_new_num;
    }

    public ArrayList getTarget_introduction_num() {
        return target_introduction_num;
    }

    public void setTarget_introduction_num(ArrayList target_introduction_num) {
        this.target_introduction_num = target_introduction_num;
    }

    public ArrayList getTarget_item_num() {
        return target_item_num;
    }

    public void setTarget_item_num(ArrayList target_item_num) {
        this.target_item_num = target_item_num;
    }

    public ArrayList getTarget_staff_per_sales() {
        return target_staff_per_sales;
    }

    public void setTarget_staff_per_sales(ArrayList target_staff_per_sales) {
        this.target_staff_per_sales = target_staff_per_sales;
    }

    public ArrayList getTarget_staff_num() {
        return target_staff_num;
    }

    public void setTarget_staff_num(ArrayList target_staff_num) {
        this.target_staff_num = target_staff_num;
    }

    public ArrayList getTarget_item_rate() {
        return target_item_rate;
    }

    public void setTarget_item_rate(ArrayList target_item_rate) {
        this.target_item_rate = target_item_rate;
    }

    public ArrayList getTarget_next_reserve_rate() {
        return target_next_reserve_rate;
    }

    public void setTarget_next_reserve_rate(ArrayList target_next_reserve_rate) {
        this.target_next_reserve_rate = target_next_reserve_rate;
    }

    public ArrayList getTarget_reserve_close_rate() {
        return target_reserve_close_rate;
    }

    public void setTarget_reserve_close_rate(ArrayList target_reserve_close_rate) {
        this.target_reserve_close_rate = target_reserve_close_rate;
    }

    public ArrayList getRepert_30_new() {
        return repert_30_new;
    }

    public void setRepert_30_new(ArrayList repert_30_new) {
        this.repert_30_new = repert_30_new;
    }

    public ArrayList getRepert_45_new() {
        return repert_45_new;
    }

    public void setRepert_45_new(ArrayList repert_45_new) {
        this.repert_45_new = repert_45_new;
    }

    public ArrayList getRepert_60_new() {
        return repert_60_new;
    }

    public void setRepert_60_new(ArrayList repert_60_new) {
        this.repert_60_new = repert_60_new;
    }

    public ArrayList getRepert_90_new() {
        return repert_90_new;
    }

    public void setRepert_90_new(ArrayList repert_90_new) {
        this.repert_90_new = repert_90_new;
    }

    public ArrayList getRepert_120_new() {
        return repert_120_new;
    }

    public void setRepert_120_new(ArrayList repert_120_new) {
        this.repert_120_new = repert_120_new;
    }

    public ArrayList getRepert_150_new() {
        return repert_150_new;
    }

    public void setRepert_150_new(ArrayList repert_150_new) {
        this.repert_150_new = repert_150_new;
    }

    public ArrayList getRepert_180_new() {
        return repert_180_new;
    }

    public void setRepert_180_new(ArrayList repert_180_new) {
        this.repert_180_new = repert_180_new;
    }

    public ArrayList getRepert_30_fix() {
        return repert_30_fix;
    }

    public void setRepert_30_fix(ArrayList repert_30_fix) {
        this.repert_30_fix = repert_30_fix;
    }

    public ArrayList getRepert_45_fix() {
        return repert_45_fix;
    }

    public void setRepert_45_fix(ArrayList repert_45_fix) {
        this.repert_45_fix = repert_45_fix;
    }

    public ArrayList getRepert_60_fix() {
        return repert_60_fix;
    }

    public void setRepert_60_fix(ArrayList repert_60_fix) {
        this.repert_60_fix = repert_60_fix;
    }

    public ArrayList getRepert_90_fix() {
        return repert_90_fix;
    }

    public void setRepert_90_fix(ArrayList repert_90_fix) {
        this.repert_90_fix = repert_90_fix;
    }

    public ArrayList getRepert_120_fix() {
        return repert_120_fix;
    }

    public void setRepert_120_fix(ArrayList repert_120_fix) {
        this.repert_120_fix = repert_120_fix;
    }

    public ArrayList getRepert_150_fix() {
        return repert_150_fix;
    }

    public void setRepert_150_fix(ArrayList repert_150_fix) {
        this.repert_150_fix = repert_150_fix;
    }

    public ArrayList getRepert_180_fix() {
        return repert_180_fix;
    }

    public void setRepert_180_fix(ArrayList repert_180_fix) {
        this.repert_180_fix = repert_180_fix;
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author IVS
 */
public class ServiceItemBean {
    
    //�G�X�e
    private int esthetic;
    //�N���E����
    private int kure;
    //�v�`�E���P
    private int petit;
    //���N�E����
    private int healthAndBeauty;
    //�v���`�i
    private int platinum;
    //���
    private int hairGrowth;
    
    public ServiceItemBean(){
        
    }

    public ServiceItemBean(int esthetic, int kure, int petit, int healthAndBeauty, int platinum, int hairGrowth) {
        this.esthetic = esthetic;
        this.kure = kure;
        this.petit = petit;
        this.healthAndBeauty = healthAndBeauty;
        this.platinum = platinum;
        this.hairGrowth = hairGrowth;
    }

    public int getEsthetic() {
        return esthetic;
    }

    public void setEsthetic(int esthetic) {
        this.esthetic = esthetic;
    }

    public int getKure() {
        return kure;
    }

    public void setKure(int kure) {
        this.kure = kure;
    }

    public int getPetit() {
        return petit;
    }

    public void setPetit(int petit) {
        this.petit = petit;
    }

    public int getHealthAndBeauty() {
        return healthAndBeauty;
    }

    public void setHealthAndBeauty(int healthAndBeauty) {
        this.healthAndBeauty = healthAndBeauty;
    }

    public int getPlatinum() {
        return platinum;
    }

    public void setPlatinum(int platinum) {
        this.platinum = platinum;
    }

    public int getHairGrowth() {
        return hairGrowth;
    }

    public void setHairGrowth(int hairGrowth) {
        this.hairGrowth = hairGrowth;
    }
    
    
}

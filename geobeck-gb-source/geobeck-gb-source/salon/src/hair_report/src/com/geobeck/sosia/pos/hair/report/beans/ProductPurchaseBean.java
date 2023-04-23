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
public class ProductPurchaseBean {
    //毛髪ケア-金額
    int hairCareValue;
    //毛髪ケア-人数
    int hairCareNum;
    //頭皮ケア-金額
    int scalpCareValue;
    //頭皮ケア-人数
    int scalpCareNum;
    //美容機器-金額
    int beautyValue;
    //美容機器-人数
    int beautyNum;
    //スタイ-金額
    int bibValue;
    //スタイ-人数
    int bibNum;

    public int getHairCareValue() {
        return hairCareValue;
    }

    public void setHairCareValue(int hairCareValue) {
        this.hairCareValue = hairCareValue;
    }

    public int getHairCareNum() {
        return hairCareNum;
    }

    public void setHairCareNum(int hairCareNum) {
        this.hairCareNum = hairCareNum;
    }

    public int getScalpCareValue() {
        return scalpCareValue;
    }

    public void setScalpCareValue(int scalpCareValue) {
        this.scalpCareValue = scalpCareValue;
    }

    public int getScalpCareNum() {
        return scalpCareNum;
    }

    public void setScalpCareNum(int scalpCareNum) {
        this.scalpCareNum = scalpCareNum;
    }

    public int getBeautyValue() {
        return beautyValue;
    }

    public void setBeautyValue(int beautyValue) {
        this.beautyValue = beautyValue;
    }

    public int getBeautyNum() {
        return beautyNum;
    }

    public void setBeautyNum(int beautyNum) {
        this.beautyNum = beautyNum;
    }

    public int getBibValue() {
        return bibValue;
    }

    public void setBibValue(int bibValue) {
        this.bibValue = bibValue;
    }

    public int getBibNum() {
        return bibNum;
    }

    public void setBibNum(int bibNum) {
        this.bibNum = bibNum;
    }
}

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
public class ReservationManagementBean {
    
    //�\��
    private int reservationNum;
    //���q��
    private int customerTotalNum;
    //�V�\��
    private int newReservation;
    //���E
    private int withdrawal;

    public int getReservationNum() {
        return reservationNum;
    }

    public void setReservationNum(int reservationNum) {
        this.reservationNum = reservationNum;
    }
    
    public int getCustomerTotalNum() {
        return customerTotalNum;
    }

    public void setCustomerTotalNum(int customerTotalNum) {
        this.customerTotalNum = customerTotalNum;
    }
    
    public int getNewReservation() {
        return newReservation;
    }

    public void setNewReservation(int newReservation) {
        this.newReservation = newReservation;
    }
    
    public int getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(int withdrawal) {
        this.withdrawal = withdrawal;
    }
}

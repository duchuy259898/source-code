/*
 * enumPointcardProduct.java
 *
 * Created on 2008/10/01, 20:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.pointcard;

import com.geobeck.barcode.SerialParameters;

/**
 * �|�C���g�J�[�h���C�^�[���
 */
public enum enumPointcardProduct{
    SANWA_A31("SANWA ABS-A31"),
    STAR_TCP300("�X�^�[���� TCP300II"),
    NATEC_CRWPOS("�i�e�b�N CRW-POS");

    private String name;
    
    enumPointcardProduct(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
    public int getId(){
        return this.ordinal();
    }
    
    public static enumPointcardProduct indexOf(int id) {
        return enumPointcardProduct.values()[id];
    }
    
    /**
     * �R�l�N�V�����N���X���쐬
     */
    public static AbstractCardCommunication getConnection(Integer productId, SerialParameters param) {

        AbstractCardCommunication con;

        //�R�l�N�V�����̃C���X�^���X���擾
        if (productId == enumPointcardProduct.SANWA_A31.ordinal()) {
            
            // SANWA ABS-A31
            con =   new SanwaA31(param);
            
        } else if (productId == enumPointcardProduct.NATEC_CRWPOS.ordinal()) {

            // �i�e�b�N CRW-POS
            con =   new NatecCRWPOS(param);

        } else {

            // �X�^�[���� TCP300II (�w��Ȃ����̃f�t�H���g�Ƃ���)
            con =   new StarTCP300(param);
        }

        return con;
    }
    
    public static enumPointcardProduct getCurrent( AbstractCardCommunication con ){

        if (con instanceof SanwaA31) {
            return SANWA_A31;
        } else if (con instanceof NatecCRWPOS) {
            return NATEC_CRWPOS;
        } else {
            return STAR_TCP300;
        }
    }
}
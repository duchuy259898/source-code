/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;

/**
 * ��v�v�Z�ŗ��p���郆�[�e�B���e�B�B
 * 
 * @author mahara
 */
public final class AccountUtil {
    
    private AccountUtil() {
    }
    
    /**
     * �����_�ȉ���؂�̂Ă�B<br />
     * 
     * Math.floor() �ƈقȂ�A���z���}�C�i�X�l�̏ꍇ�͐��̕����ɐ؂�̂Ă�B<br />
     * 
     * <pre>
     * ��:
     *  100.5 �� 100
     * -100.5 �� 100
     * </pre>
     * 
     * @param value
     * @return 
     */
    public static double floor(double value) {
        if (value >= 0) {
            return Math.floor(value);
        }
        
        return - Math.floor(Math.abs(value));
    }
}

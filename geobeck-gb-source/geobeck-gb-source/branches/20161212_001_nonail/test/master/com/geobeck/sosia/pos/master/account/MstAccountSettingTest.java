/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import junit.framework.TestCase;

/**
 *
 * @author mahara
 */
public class MstAccountSettingTest extends TestCase {
    
    /**
     * pos_hair_rizap �ɓo�^����Ă���ݒ�B(2013-11-06)
     * @return 
     */
    private MstAccountSetting getRizapAccountSetting() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        accountSetting.setDisplayPriceType(0);  // ���j���[�\��: ����
        accountSetting.setDiscountType(0);      // �����ݒ�: 0�F�ō����犄��
        accountSetting.setAccountingUnit(0);    // ��v�P��: 0�F1�~�P��
        accountSetting.setRounding(0);          // �[���ۂ߁B0�F�؂�̂�
        accountSetting.setCutoffDay(31);        // �����B31�F����
        accountSetting.setReportPriceType(1);   // ���[�ŋ敪�B1�F�Ŕ�
        accountSetting.setPrevSalesUpdate(1);   // �O���ȑO�̃f�[�^�X�V
        accountSetting.setDisplayArea(null);    // �X�܂̃G���A�\��: TODO null �͂ǂ������Ӗ��H
        
        return accountSetting;
    }

    // �[���ۂ߃e�X�g
    public void test_round() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // 0�F�؂�̂�
        accountSetting.setRounding(0);
        
        assertEquals(10L, accountSetting.round(10.4D).longValue());
        assertEquals(10L, accountSetting.round(10.5D).longValue());
        
        // 1�F�l�̌ܓ�
        accountSetting.setRounding(1);
        
        assertEquals(10L, accountSetting.round(10.4D).longValue());
        assertEquals(11L, accountSetting.round(10.5D).longValue());
        
        // 2�F�؂�グ
        accountSetting.setRounding(2);
        
        assertEquals(11L, accountSetting.round(10.4D).longValue());
        assertEquals(11L, accountSetting.round(10.5D).longValue());
    }
    
    /**
     * ����Ōv�Z
     * 
     * (1) �ō����犄�� 
     * (2) ���׋��z, �����z���v���X
     * (3) �[���ۂ�: 0 (�؂�̂�)
     *      �� ���݂�0 (�؂�̂�) �ȊO���p���Ă��Ȃ��̂ŁA����݂̂��e�X�g����B
     */
    public void test_getTax_0_Plus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // �����ݒ�B0�F�ō����犄��
        accountSetting.setDiscountType(0);
        
        // �[���ۂ߁B0�F�؂�̂�
        accountSetting.setRounding(0);

        long value = 10000;
        double taxRate = 0.05;
        
        /* �����Ȃ�
         * �������z = (10000 - 0) = 10000
         * �Ŕ����z = 10000 / 1.05 = 9523.809523809524
         * �����   = 9523.809523809524 * 0.05 = 476.1904761904762 �� 476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(476, tax);
        }
        
        /* �������� (1)
         * �������z = (10000 - 1000) = 9000
         * �Ŕ����z = 9000 / 1.05 = 8571.428571428571
         * �����   = 8571.428571428571 * 0.05 = 428.5714285714286 �� 428
         */
        {
            long discount = 1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(428, tax);
        }
        
        /* �������� (2)
         * �������z = (10000 - 2000) = 8000
         * �Ŕ����z = 8000 / 1.05 = 7619.047619047619
         * �����   = 7619.047619047619 * 0.05 = 380.952380952381 �� 380
         */
        {
            long discount = 2000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(380, tax);
        }
        
        /* pos_hair_mahara3 - 2013/11/06 �`�[���
         * �������z = (298000 - 0) = 298000
         * �Ŕ����z = 298000 / 1.05 = 283809.5238095238
         * �����   = 283809.5238095238 * 0.05 = 14190.47619047619 �� 14190
         */
        {
            long value2 = 298000;
            long discount = 0;

            long tax = accountSetting.getTax(value2, discount, taxRate);

            assertEquals(14190, tax);
        }
    }

    /**
     * ����Ōv�Z - �ō����犄���B�������z�A�����z���}�C�i�X�̏ꍇ
     */
    public void test_getTax_0_Minus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // �[���ۂ߁B0�F�؂�̂�
        accountSetting.setRounding(0);

        // �����ݒ�B0�F�ō����犄��
        accountSetting.setDiscountType(0);
        
        long value = -10000;
        double taxRate = 0.05;
        
        /* �����Ȃ�
         * �������z = (-10000 - 0) = -10000
         * �Ŕ����z = -10000 / 1.05 = -9523.809523809524
         * �����   = -9523.809523809524 * 0.05 = -476.1904761904762 �� -476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-476, tax);
        }
        
        /* �������� (1)
         * �������z = (-10000 - -1000) = -9000
         * �Ŕ����z = -9000 / 1.05 = -8571.428571428571
         * �����   = -8571.428571428571 * 0.05 = -428.5714285714286 �� -428
         */
        {
            long discount = -1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-428, tax);
        }
        
        /* �������� (2)
         * �������z = (-10000 - -2000) = -8000
         * �Ŕ����z = -8000 / 1.05 = -7619.047619047619
         * �����   = -7619.047619047619 * 0.05 = -380.952380952381 �� -380
         */
        {
            long discount = -2000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-380, tax);
        }
        
        /* pos_hair_mahara3 - 2013/11/06 �`�[���
         * �������z = (298000 - 0) = 298000
         * �Ŕ����z = 298000 / 1.05 = 283809.5238095238
         * �����   = 283809.5238095238 * 0.05 = 14190.47619047619 �� 14190
         */
        {
            long value2 = -298000;
            long discount = 0;

            long tax = accountSetting.getTax(value2, discount, taxRate);

            assertEquals(-14190, tax);
        }
    }
    
    /**
     * ����Ōv�Z - �Ŕ����犄��
     */
    public void test_getTax_1_Plus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // �[���ۂ߁B0�F�؂�̂�
        accountSetting.setRounding(0);

        // �����ݒ�B1�F�Ŕ����犄��
        accountSetting.setDiscountType(1);
        
        long value = 10000;
        double taxRate = 0.05;
        
        /* �����Ȃ�
         * �Ŕ����z�ɑ΂������� = 1000 / 1.05 * 0.05 = 476.1904761904762 �� 476
         * �����z�ɑ΂�������  = 0 * 0.05 = 0
         * �{���̏���� = 476 - 0 = 476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(476, tax);
        }
        
        /* �������� (1)
         * �Ŕ����z�ɑ΂������� = 1000 / 1.05 * 0.05 = 476.1904761904762 �� 476
         * �����z�ɑ΂�������  = 1000 * 0.05 = 50
         * �{���̏���� = 476 - 50 = 426
         */
        {
            long discount = 1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(426, tax);
        }
        
        /* �������� (2)
         * �Ŕ����z�ɑ΂������� = 1000 / 1.05 * 0.05 = 476.1904761904762 �� 476
         * �����z�ɑ΂�������  = 1000 * 0.05 = 50.25 �� 50
         * �{���̏���� = 476 - 50 = 426
         */
        {
            long discount = 1005;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(426, tax);
        }
    }
    
    /**
     * ����Ōv�Z - �Ŕ����犄���B���׋��z�A�����z���}�C�i�X�̏ꍇ�B
     */
    public void test_getTax_1_Minus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // �[���ۂ߁B0�F�؂�̂�
        accountSetting.setRounding(0);

        // �����ݒ�B1�F�Ŕ����犄��
        accountSetting.setDiscountType(1);
        
        long value = -10000;
        double taxRate = 0.05;
        
        /* �����Ȃ�
         * �Ŕ����z�ɑ΂������� = -1000 / 1.05 * 0.05 = -476.1904761904762 �� -476
         * �����z�ɑ΂�������  = 0 * 0.05 = 0
         * �{���̏���� = -476 - 0 = -476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-476, tax);
        }
        
        /* �������� (1)
         * �Ŕ����z�ɑ΂������� = -1000 / 1.05 * 0.05 = -476.1904761904762 �� -476
         * �����z�ɑ΂�������  = -1000 * 0.05 = -50
         * �{���̏���� = -476 - -50 = -426
         */
        {
            long discount = -1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-426, tax);
        }
        
        /* �������� (2)
         * �Ŕ����z�ɑ΂������� = -1000 / 1.05 * 0.05 = -476.1904761904762 �� -476
         * �����z�ɑ΂�������  = -1000 * 0.05 = -50.25 �� -50
         * �{���̏���� = -476 - -50 = -426
         */
        {
            long discount = -1005;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-426, tax);
        }
    }
}

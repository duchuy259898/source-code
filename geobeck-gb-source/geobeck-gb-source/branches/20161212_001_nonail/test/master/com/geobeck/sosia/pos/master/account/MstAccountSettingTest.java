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
     * pos_hair_rizap に登録されている設定。(2013-11-06)
     * @return 
     */
    private MstAccountSetting getRizapAccountSetting() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        accountSetting.setDisplayPriceType(0);  // メニュー表示: 内税
        accountSetting.setDiscountType(0);      // 割引設定: 0：税込から割引
        accountSetting.setAccountingUnit(0);    // 会計単位: 0：1円単位
        accountSetting.setRounding(0);          // 端数丸め。0：切り捨て
        accountSetting.setCutoffDay(31);        // 締日。31：末日
        accountSetting.setReportPriceType(1);   // 帳票税区分。1：税抜
        accountSetting.setPrevSalesUpdate(1);   // 前月以前のデータ更新
        accountSetting.setDisplayArea(null);    // 店舗のエリア表示: TODO null はどういう意味？
        
        return accountSetting;
    }

    // 端数丸めテスト
    public void test_round() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // 0：切り捨て
        accountSetting.setRounding(0);
        
        assertEquals(10L, accountSetting.round(10.4D).longValue());
        assertEquals(10L, accountSetting.round(10.5D).longValue());
        
        // 1：四捨五入
        accountSetting.setRounding(1);
        
        assertEquals(10L, accountSetting.round(10.4D).longValue());
        assertEquals(11L, accountSetting.round(10.5D).longValue());
        
        // 2：切り上げ
        accountSetting.setRounding(2);
        
        assertEquals(11L, accountSetting.round(10.4D).longValue());
        assertEquals(11L, accountSetting.round(10.5D).longValue());
    }
    
    /**
     * 消費税計算
     * 
     * (1) 税込から割引 
     * (2) 明細金額, 割引額がプラス
     * (3) 端数丸め: 0 (切り捨て)
     *      ※ 現在は0 (切り捨て) 以外利用していないので、これのみをテストする。
     */
    public void test_getTax_0_Plus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // 割引設定。0：税込から割引
        accountSetting.setDiscountType(0);
        
        // 端数丸め。0：切り捨て
        accountSetting.setRounding(0);

        long value = 10000;
        double taxRate = 0.05;
        
        /* 割引なし
         * 請求金額 = (10000 - 0) = 10000
         * 税抜金額 = 10000 / 1.05 = 9523.809523809524
         * 消費税   = 9523.809523809524 * 0.05 = 476.1904761904762 ⇒ 476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(476, tax);
        }
        
        /* 割引あり (1)
         * 請求金額 = (10000 - 1000) = 9000
         * 税抜金額 = 9000 / 1.05 = 8571.428571428571
         * 消費税   = 8571.428571428571 * 0.05 = 428.5714285714286 ⇒ 428
         */
        {
            long discount = 1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(428, tax);
        }
        
        /* 割引あり (2)
         * 請求金額 = (10000 - 2000) = 8000
         * 税抜金額 = 8000 / 1.05 = 7619.047619047619
         * 消費税   = 7619.047619047619 * 0.05 = 380.952380952381 ⇒ 380
         */
        {
            long discount = 2000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(380, tax);
        }
        
        /* pos_hair_mahara3 - 2013/11/06 伝票より
         * 請求金額 = (298000 - 0) = 298000
         * 税抜金額 = 298000 / 1.05 = 283809.5238095238
         * 消費税   = 283809.5238095238 * 0.05 = 14190.47619047619 ⇒ 14190
         */
        {
            long value2 = 298000;
            long discount = 0;

            long tax = accountSetting.getTax(value2, discount, taxRate);

            assertEquals(14190, tax);
        }
    }

    /**
     * 消費税計算 - 税込から割引。請求金額、割引額がマイナスの場合
     */
    public void test_getTax_0_Minus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // 端数丸め。0：切り捨て
        accountSetting.setRounding(0);

        // 割引設定。0：税込から割引
        accountSetting.setDiscountType(0);
        
        long value = -10000;
        double taxRate = 0.05;
        
        /* 割引なし
         * 請求金額 = (-10000 - 0) = -10000
         * 税抜金額 = -10000 / 1.05 = -9523.809523809524
         * 消費税   = -9523.809523809524 * 0.05 = -476.1904761904762 ⇒ -476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-476, tax);
        }
        
        /* 割引あり (1)
         * 請求金額 = (-10000 - -1000) = -9000
         * 税抜金額 = -9000 / 1.05 = -8571.428571428571
         * 消費税   = -8571.428571428571 * 0.05 = -428.5714285714286 ⇒ -428
         */
        {
            long discount = -1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-428, tax);
        }
        
        /* 割引あり (2)
         * 請求金額 = (-10000 - -2000) = -8000
         * 税抜金額 = -8000 / 1.05 = -7619.047619047619
         * 消費税   = -7619.047619047619 * 0.05 = -380.952380952381 ⇒ -380
         */
        {
            long discount = -2000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-380, tax);
        }
        
        /* pos_hair_mahara3 - 2013/11/06 伝票より
         * 請求金額 = (298000 - 0) = 298000
         * 税抜金額 = 298000 / 1.05 = 283809.5238095238
         * 消費税   = 283809.5238095238 * 0.05 = 14190.47619047619 ⇒ 14190
         */
        {
            long value2 = -298000;
            long discount = 0;

            long tax = accountSetting.getTax(value2, discount, taxRate);

            assertEquals(-14190, tax);
        }
    }
    
    /**
     * 消費税計算 - 税抜から割引
     */
    public void test_getTax_1_Plus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // 端数丸め。0：切り捨て
        accountSetting.setRounding(0);

        // 割引設定。1：税抜から割引
        accountSetting.setDiscountType(1);
        
        long value = 10000;
        double taxRate = 0.05;
        
        /* 割引なし
         * 税抜金額に対する消費税 = 1000 / 1.05 * 0.05 = 476.1904761904762 ⇒ 476
         * 割引額に対する消費税  = 0 * 0.05 = 0
         * 本当の消費税 = 476 - 0 = 476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(476, tax);
        }
        
        /* 割引あり (1)
         * 税抜金額に対する消費税 = 1000 / 1.05 * 0.05 = 476.1904761904762 ⇒ 476
         * 割引額に対する消費税  = 1000 * 0.05 = 50
         * 本当の消費税 = 476 - 50 = 426
         */
        {
            long discount = 1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(426, tax);
        }
        
        /* 割引あり (2)
         * 税抜金額に対する消費税 = 1000 / 1.05 * 0.05 = 476.1904761904762 ⇒ 476
         * 割引額に対する消費税  = 1000 * 0.05 = 50.25 ⇒ 50
         * 本当の消費税 = 476 - 50 = 426
         */
        {
            long discount = 1005;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(426, tax);
        }
    }
    
    /**
     * 消費税計算 - 税抜から割引。明細金額、割引額がマイナスの場合。
     */
    public void test_getTax_1_Minus_Round0() {
        MstAccountSetting accountSetting = new MstAccountSetting();
        
        // 端数丸め。0：切り捨て
        accountSetting.setRounding(0);

        // 割引設定。1：税抜から割引
        accountSetting.setDiscountType(1);
        
        long value = -10000;
        double taxRate = 0.05;
        
        /* 割引なし
         * 税抜金額に対する消費税 = -1000 / 1.05 * 0.05 = -476.1904761904762 ⇒ -476
         * 割引額に対する消費税  = 0 * 0.05 = 0
         * 本当の消費税 = -476 - 0 = -476
         */
        {
            long discount = 0;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-476, tax);
        }
        
        /* 割引あり (1)
         * 税抜金額に対する消費税 = -1000 / 1.05 * 0.05 = -476.1904761904762 ⇒ -476
         * 割引額に対する消費税  = -1000 * 0.05 = -50
         * 本当の消費税 = -476 - -50 = -426
         */
        {
            long discount = -1000;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-426, tax);
        }
        
        /* 割引あり (2)
         * 税抜金額に対する消費税 = -1000 / 1.05 * 0.05 = -476.1904761904762 ⇒ -476
         * 割引額に対する消費税  = -1000 * 0.05 = -50.25 ⇒ -50
         * 本当の消費税 = -476 - -50 = -426
         */
        {
            long discount = -1005;

            long tax = accountSetting.getTax(value, discount, taxRate);

            assertEquals(-426, tax);
        }
    }
}

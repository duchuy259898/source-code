/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;

/**
 * 会計計算で利用するユーティリティ。
 * 
 * @author mahara
 */
public final class AccountUtil {
    
    private AccountUtil() {
    }
    
    /**
     * 小数点以下を切り捨てる。<br />
     * 
     * Math.floor() と異なり、金額がマイナス値の場合は正の方向に切り捨てる。<br />
     * 
     * <pre>
     * 例:
     *  100.5 ⇒ 100
     * -100.5 ⇒ 100
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

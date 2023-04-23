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
 * ポイントカードライター種別
 */
public enum enumPointcardProduct{
    SANWA_A31("SANWA ABS-A31"),
    STAR_TCP300("スター精密 TCP300II"),
    NATEC_CRWPOS("ナテック CRW-POS");

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
     * コネクションクラスを作成
     */
    public static AbstractCardCommunication getConnection(Integer productId, SerialParameters param) {

        AbstractCardCommunication con;

        //コネクションのインスタンスを取得
        if (productId == enumPointcardProduct.SANWA_A31.ordinal()) {
            
            // SANWA ABS-A31
            con =   new SanwaA31(param);
            
        } else if (productId == enumPointcardProduct.NATEC_CRWPOS.ordinal()) {

            // ナテック CRW-POS
            con =   new NatecCRWPOS(param);

        } else {

            // スター精密 TCP300II (指定なし時のデフォルトとする)
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
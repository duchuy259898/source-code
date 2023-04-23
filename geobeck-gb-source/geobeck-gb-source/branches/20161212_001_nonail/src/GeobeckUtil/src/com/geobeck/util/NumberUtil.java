/*
 * NumberUtil.java
 *
 * Created on 2008/10/07, 17:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

/**
 *
 * @author takeda
 */
public class NumberUtil {
    public static long round( double val ){
        long ret;
        if( val >= 0 ){
            ret = Math.round( val );
        }else{
            val -= 0.5F;
            ret = (long)Math.ceil( val );
        }
        return ret;
    }
    
    public static int round( float val ){
        int ret;
        if( val >= 0 ){
            ret = Math.round( val );
        }else{
            val -= 0.5F;
            ret = (int)Math.ceil( val );
        }
        return ret;
    }
}

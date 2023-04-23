/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;

import junit.framework.TestCase;

/**
 *
 * @author mahara
 */
public class AccountUtilTest extends TestCase {
    
    public void testFloor() {
        assertEquals(0.0, AccountUtil.floor(0));
        assertEquals(100.0, AccountUtil.floor(100.5));
        assertEquals(-100.0, AccountUtil.floor(-100.5));
    }
    
}

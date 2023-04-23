/*
 * CTIListener.java
 *
 * Created on 2008/02/18, 19:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.cti;

/**
 *CTIリスナークラス
 * @author murakami
 */
public interface CTIListener
{    
    /** Creates a new instance of CTIListener */
		public boolean readCTI(CTIEvent ce);    
}

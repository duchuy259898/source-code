/*
 * ShutdownThread.java
 *
 * Created on 2009/11/30, 18:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.main;

import com.geobeck.sosia.pos.login.Login;

/**
 * Windowsシャットダウン時にログイン情報を削除する
 * @author geobeck
 */
public class ShutdownThread extends Thread 
{
	/**
	 * Creates a new instance of ShutdownThread
	 */
	public ShutdownThread()
	{
            super();
	}
	
	
	public void run(){

            Login.logout();

        }

}

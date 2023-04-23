/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.envCheck;

/**
 *
 * @author mahara
 */
public final class EnvCheckConfiguration {
	/** 環境チェックプログラムのバージョン */
	public static final String setupFilename = "SosiaPosSetup-1.0.1.2.exe";
	
	/** MAC アドレスをチェックせず、常に環境チェックを行うか。 */
	public static final boolean alwaysMigrationCheck = false;
	
	private EnvCheckConfiguration() {
	}
}

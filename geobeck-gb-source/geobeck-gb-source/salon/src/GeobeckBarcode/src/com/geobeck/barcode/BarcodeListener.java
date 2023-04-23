/*
 * BarcodeListener.java
 *
 * Created on 2007/04/26, 16:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.barcode;

/**
 * バーコードリスナークラス
 * @author katagiri
 */
public interface BarcodeListener
{
	public boolean readBarcode(BarcodeEvent be);
}

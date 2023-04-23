/*
 * BarcodeEvent.java
 *
 * Created on 2007/04/26, 16:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.barcode;

import java.util.EventObject;
import java.awt.event.*;

/**
 * バーコードイベントクラス
 * @author katagiri
 */
public class BarcodeEvent extends EventObject
{
	/**
	 * バーコード
	 */
	protected	String		barcode		=	"";
	
	/**
	 * コンストラクタ
	 * @param source 
	 * @param barcode バーコード
	 */
	public BarcodeEvent(Object source, String barcode)
	{
		super(source);
		this.setBarcode(barcode);
	}

	/**
	 * バーコードを取得する
	 * @return バーコード
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * バーコードを設定する
	 * @param barcode バーコード
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}
	
}

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
 * �o�[�R�[�h�C�x���g�N���X
 * @author katagiri
 */
public class BarcodeEvent extends EventObject
{
	/**
	 * �o�[�R�[�h
	 */
	protected	String		barcode		=	"";
	
	/**
	 * �R���X�g���N�^
	 * @param source 
	 * @param barcode �o�[�R�[�h
	 */
	public BarcodeEvent(Object source, String barcode)
	{
		super(source);
		this.setBarcode(barcode);
	}

	/**
	 * �o�[�R�[�h���擾����
	 * @return �o�[�R�[�h
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * �o�[�R�[�h��ݒ肷��
	 * @param barcode �o�[�R�[�h
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}
	
}

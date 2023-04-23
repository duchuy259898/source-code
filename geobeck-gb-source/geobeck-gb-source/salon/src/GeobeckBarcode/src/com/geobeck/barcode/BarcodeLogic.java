/*
 * BarcodeLogic.java
 *
 * Created on 2007/10/16, 16:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.barcode;

import org.krysalis.barcode4j.*;

/**
 * �o�[�R�[�h���W�b�N�N���X
 * @author katagiri
 */
public class BarcodeLogic
{
	/**
	 * EAN13�̃R�[�h�̐��������`�F�b�N����
	 * @param code �R�[�h
	 * @throws org.krysalis.barcode4j.BarcodeException 
	 * @return 
	 */
	public static boolean checkEAN13CheckDigit(String code) throws BarcodeException
	{
		//�R�[�h��null�̏ꍇ
		if(code == null)
		{
			throw	new BarcodeException("code is null");
		}
		//13���̐��l���`�F�b�N����
		if(!code.matches("[0-9]{13}"))
		{
			throw	new BarcodeException("code is not 13-digit numeric string");
		}
		//�`�F�b�N�f�B�W�b�g���擾����
		Integer	cd	=	BarcodeLogic.getEAN13CheckDigit(code.substring(0, 12));
		//�`�F�b�N�f�B�W�b�g�̃`�F�b�N������
		return	cd.toString().equals(code.substring(12, 13));
	}
	
	/**
	 * �`�F�b�N�f�B�W�b�g��ǉ�����
	 * @param code �R�[�h
	 * @throws org.krysalis.barcode4j.BarcodeException 
	 * @return 
	 */
	public static String addEAN13CheckDigit(String code) throws BarcodeException
	{
		return	code + BarcodeLogic.getEAN13CheckDigit(code).toString();
	}
	
	/**
	 * �`�F�b�N�f�B�W�b�g���擾����
	 * @param code �R�[�h
	 * @throws org.krysalis.barcode4j.BarcodeException 
	 * @return 
	 */
	public static Integer getEAN13CheckDigit(String code) throws BarcodeException
	{
		//�R�[�h��null�̏ꍇ
		if(code == null)
		{
			throw	new BarcodeException("code is null");
		}
		//12���̐��l���`�F�b�N����
		if(!code.matches("[0-9]{12}"))
		{
			throw	new BarcodeException("code is not 12-digit numeric string");
		}
		
		String[]	sTemp	=	code.split("");
		Integer[]	iTemp	=	new Integer[12];
		
		for(int i = 0; i < 12; i ++)
		{
			iTemp[i]	=	new Integer(sTemp[i + 1]);
		}
		
		int		temp0	=	0;
		
		for(int i = 1; i < 12; i += 2)
		{
			temp0	+=	iTemp[i];
		}
		
		temp0	*=	3;
		
		int		temp1	=	0;
		
		for(int i = 0; i < 12; i += 2)
		{
			temp1	+=	iTemp[i];
		}
		
		Integer		temp	=	(10 - ((temp0 + temp1) % 10)) % 10;
		
		return	temp;
	}
}

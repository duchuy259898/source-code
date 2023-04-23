/*
 * TaxUtil.java
 *
 * Created on 2006/05/15, 18:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;


/**
 * �ŋ����[�e�B���e�B
 * @author katagiri
 */
public class TaxUtil
{
	
	/**
	 * �R���X�g���N�^
	 */
	public TaxUtil()
	{
	}
	
	
	/**
	 * �Ŋz���擾����B
	 * @param value ���z
	 * @return �Ŋz
	 */
	public static Long getTax(Long value, Double taxRate, int rounding)
	{
		Double	tax	=	0d;
		Long rs = 0l;
		if(taxRate == null)	return tax.longValue();
		
		tax	=	(value.doubleValue() / (1d + taxRate) * taxRate);
		
		switch(rounding)
		{
			//�؂�̂�
			case 1:
				rs		=	(long)Math.floor(tax);
				break;
			//�l�̌ܓ�
			case 2:
				rs		=	Math.round(tax);
				break;
			//�؂�グ
			case 3:
				rs		=	(long)Math.ceil((double)tax);
				break;
			default:
				rs		=	0l;
		}
		
		return	rs;
	}
	
	
	/**
	 * �Ŋz���擾����B
	 * @param value ���z
	 * @return �Ŋz
	 */
	public static Integer getTax(Integer value, Double taxRate, int rounding)
	{
		Integer	tax	=	0;
		
		if(taxRate == null)	return	tax;
		
		tax	=	(int)(value.doubleValue() / (1d + taxRate) * taxRate);
		
		switch(rounding)
		{
			//�؂�̂�
			case 1:
				tax		=	(int)Math.floor((double)tax);
				break;
			//�l�̌ܓ�
			case 2:
				tax		=	(int)Math.round((double)tax);
				break;
			//�؂�グ
			case 3:
				tax		=	(int)Math.ceil((double)tax);
				break;
			default:
				tax		=	0;
		}
		
		return	tax;
	}
	
	
	/**
	 * �Ŋz���擾����B
	 * @param value ���z
	 * @return �Ŋz
	 */
	public static Double getTax(Double value, Double taxRate, int rounding)
	{
		Double	tax	=	0d;
		
		if(taxRate == null)	return	tax;
		
		tax	=	value / (1d + taxRate) * taxRate;
		
		switch(rounding)
		{
			//�؂�̂�
			case 1:
				tax		=	Math.floor(tax);
				break;
			//�l�̌ܓ�
			case 2:
				tax		=	(double)Math.round(tax);
				break;
			//�؂�グ
			case 3:
				tax		=	Math.ceil(tax);
				break;
			default:
				tax		=	0d;
		}
		
		return	tax;
	}
}

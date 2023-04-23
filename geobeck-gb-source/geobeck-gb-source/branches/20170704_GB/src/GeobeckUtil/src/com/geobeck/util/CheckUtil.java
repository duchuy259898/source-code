/*
 * CheckUtil.java
 *
 * Created on 2004/04/27, 15:00
 */
package com.geobeck.util;
import	java.util.*;
/**
 * �f�[�^�̃`�F�b�N���s���N���X
 */
public class CheckUtil
{
	/**
	 * �����Ȃ��̃R���X�g���N�^
	 */	
	public CheckUtil()
	{
	}
	
	/**
	 * ������̒������`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @param length �Œ��̕�����
	 * @return value�̒�����length�ȉ��Ȃ�true�Alength��蒷�����false
	 */	
	public static boolean checkStringLength(String value, int length)
	{
		if(length < value.length())
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * ������̒������`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @param length_min �ŒZ�̕�����
	 * @param length_max �Œ��̕�����
	 * @return value�̒�����length_min�ȏ�length_max�ȉ��Ȃ�true�A����ȊO�Ȃ��false
	 */	
	public static boolean checkStringLength(String value, int length_min, int length_max)
	{
		if(value.length() < length_min || length_max < value.length())
		{
			if(length_min == length_max)
			{
				return	false;
			}
			else
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	/**
	 * ���p�����񂩃`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @return value�̕������S�Ĕ��p�Ȃ��true�A���p�ȊO�̕������܂܂�Ă����false
	 */	
	public static boolean is1ByteChars(String value)
	{
		return	value.matches("[ -~�-�]*");
	}
	
	/*	�S�p�`�F�b�N
	 *	value			�`�F�b�N���镶����
	 */
	/**
	 * �S�p�����񂩃`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @return value�̕������S�đS�p�Ȃ��true�A�S�p�ȊO�̕������܂܂�Ă����false
	 */	
	public static boolean is2ByteChars(String value)
	{
		return	value.matches("[^ -~�-�]*");
	}
	
	/**
	 * ���l���`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @return value�����l�Ȃ��true�A���l�ӊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean isNumeric(String value)
	{
		return	value.matches("-?[0-9]*[.]?[0-9]+");
	}
	
	/**
	 * �i���o�[���`�F�b�N����B
	 * @param value �`�F�b�N���镶����
	 * @return value��0����9�̕�����Ȃ��true�A0����9�̕�����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean isNumber(String value)
	{
		return	value.matches("[0-9]+");
	}
	
	/**
	 * ���l�͈̔͂��`�F�b�N����B
	 * @param value �`�F�b�N���鐔�l�B
	 * @param min_value �ŏ��l�B
	 * @param max_value �ő�l�B
	 * @return value��min_value�ȏ�max_value�ȉ��Ȃ�true�A����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean checkRange(byte value, byte min_value, byte max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * ���l�͈̔͂��`�F�b�N����B
	 * @param value �`�F�b�N���鐔�l�B
	 * @param min_value �ŏ��l�B
	 * @param max_value �ő�l�B
	 * @return value��min_value�ȏ�max_value�ȉ��Ȃ�true�A����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean checkRange(double value, double min_value, double max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * ���l�͈̔͂��`�F�b�N����B
	 * @param value �`�F�b�N���鐔�l�B
	 * @param min_value �ŏ��l�B
	 * @param max_value �ő�l�B
	 * @return value��min_value�ȏ�max_value�ȉ��Ȃ�true�A����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean checkRange(float value, float min_value, float max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * ���l�͈̔͂��`�F�b�N����B
	 * @param value �`�F�b�N���鐔�l�B
	 * @param min_value �ŏ��l�B
	 * @param max_value �ő�l�B
	 * @return value��min_value�ȏ�max_value�ȉ��Ȃ�true�A����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean checkRange(int value, int min_value, int max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * ���l�͈̔͂��`�F�b�N����B
	 * @param value �`�F�b�N���鐔�l�B
	 * @param min_value �ŏ��l�B
	 * @param max_value �ő�l�B
	 * @return value��min_value�ȏ�max_value�ȉ��Ȃ�true�A����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean checkRange(long value, long min_value, long max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * ���l�͈̔͂��`�F�b�N����B
	 * @param value �`�F�b�N���鐔�l�B
	 * @param min_value �ŏ��l�B
	 * @param max_value �ő�l�B
	 * @return value��min_value�ȏ�max_value�ȉ��Ȃ�true�A����ȊO�Ȃ�false��Ԃ��B
	 */	
	public static boolean checkRange(short value, short min_value, short max_value)
	{
		if(min_value <= value && value <= max_value)
		{
			return	true;
		}
		
		return	false;
	}
	
	/**
	 * ���t���`�F�b�N����B
	 * �uYYYY/MM/DD�v�A�uYYYYMMDD�v���Ή�
	 * @param value �`�F�b�N������t�̕�����
	 * @return value�����t�̕�����Ȃ��true�A���t�ȊO�̕�����Ȃ��false��Ԃ��B
	 */	
	public static boolean isDate(String value)
	{
		//YYYY/MM/DD
		if(value.matches("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}"))
		{
			return	isDateSlash(value);
		}
		//YYYYMMDD
		else if(value.matches("[0-9]{8}"))
		{
			return	isDateNonSlash(value);
		}
		
		return	false;
	}
	
	/**
	 * ���t���`�F�b�N����B
	 * �uYYYY/MM/DD�v
	 * @param value �`�F�b�N������t�̕�����
	 * @return value�����t�̕�����Ȃ��true�A���t�ȊO�̕�����Ȃ��false��Ԃ��B
	 */	
	public static boolean isDateSlash(String value)
	{
		//������̌`���`�F�b�N
		if(value.matches("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}"))
		{
			try
			{
				int		year	=	Integer.parseInt(value.substring(0, 4));
				int		month	=	Integer.parseInt(value.substring(5, 7));
				int		date	=	Integer.parseInt(value.substring(8, 10));
				return	isDate(year, month , date);
			}
			catch(Exception e)
			{
			}
		}
		
		return	false;
	}
	
	/**
	 * ���t���`�F�b�N����B
	 * �uYYYYMMDD�v
	 * @param value �`�F�b�N������t�̕�����
	 * @return value�����t�̕�����Ȃ��true�A���t�ȊO�̕�����Ȃ��false��Ԃ��B
	 */	
	public static boolean isDateNonSlash(String value)
	{
		//������̌`���`�F�b�N
		if(value.matches("[0-9]{8}"))
		{
			try
			{
				int		year	=	Integer.parseInt(value.substring(0, 4));
				int		month	=	Integer.parseInt(value.substring(4, 6));
				int		date	=	Integer.parseInt(value.substring(6, 8));
				return	isDate(year, month , date);
			}
			catch(Exception e)
			{
			}
		}
		
		return	false;
	}
	
	/**
	 * ���t���`�F�b�N����B
	 * @param year �`�F�b�N����N�B
	 * @param month �`�F�b�N���錎�B
	 * @param date �`�F�b�N������B
	 * @return �󂯎�����N���������t�Ƃ��đÓ��Ȃ��true�A�Ó��łȂ����false��Ԃ��B
	 */	
	public static boolean isDate(int year, int month, int date)
	{
		try
		{
			month	=	month - 1;
			
			GregorianCalendar	cal	=	new GregorianCalendar();
			
			cal.setLenient(false);
			cal.set(year, month - 1, date);
			
			//�s���ȓ��t���Z�b�g���ꂽ�ꍇ�A��O����������
			cal.getTime();
			
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
}

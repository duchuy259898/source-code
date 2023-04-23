/*
 * FormatterCreator.java
 *
 * Created on 2006/04/19, 9:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.text.*;
import java.text.*;

/**
 * Formatter���쐬����N���X
 * @author katagiri
 */
public class FormatterCreator
{
	
	/**
	 * �R���X�g���N�^
	 */
	public FormatterCreator()
	{
	}
	
	
	/**
	 * MaskFormatter���쐬����B
	 * @param formatString ������ݒ肷��}�X�N
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			formatter	=	new MaskFormatter(formatString);
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	
	/**
	 * MaskFormatter���쐬����B
	 * @param formatString ������ݒ肷��}�X�N
	 * @param validCharacters �g�p�\�ȕ����̐��K�\��
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString,
														String validCharacters)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			if(formatString != null)
			{
				formatter	=	new MaskFormatter(formatString);
			}
			else
			{
				formatter	=	new MaskFormatter();
			}
			
			if(validCharacters != null)
			{
				formatter.setValidCharacters(validCharacters);
			}
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	
	/**
	 * MaskFormatter���쐬����B
	 * @param formatString ������ݒ肷��}�X�N
	 * @param validCharacters �g�p�\�ȕ����̐��K�\��
	 * @param placeholder �l���}�X�N�����S�ɖ��߂Ă��Ȃ��ꍇ�Ɏg�p���镶����
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString,
														String validCharacters,
														String placeholder)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			if(formatString != null)
			{
				formatter	=	new MaskFormatter(formatString);
			}
			else
			{
				formatter	=	new MaskFormatter();
			}
			
			if(validCharacters != null)
			{
				formatter.setValidCharacters(validCharacters);
			}
			
			if(placeholder != null)
			{
				formatter.setPlaceholder(placeholder);
			}
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	
	/**
	 * MaskFormatter���쐬����B
	 * @param formatString ������ݒ肷��}�X�N
	 * @param validCharacters �g�p�\�ȕ����̐��K�\��
	 * @param placeholder �l���}�X�N�����S�ɖ��߂Ă��Ȃ��ꍇ�Ɏg�p���镶����
	 * @return MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String formatString,
														String validCharacters,
														char placeholder)
	{
		MaskFormatter	formatter	=	null;
		
		try
		{
			if(formatString != null)
			{
				formatter	=	new MaskFormatter(formatString);
			}
			else
			{
				formatter	=	new MaskFormatter();
			}
			
			if(validCharacters != null)
			{
				formatter.setValidCharacters(validCharacters);
			}
			
			formatter.setPlaceholderCharacter(placeholder);
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		
		return	formatter;
	}
	
	/**
	 * �����pMaskFormatter���쐬����B
	 * @return �����pMaskFormatter
	 */
	public static MaskFormatter createTimeFormatter()
	{
		return	FormatterCreator.createMaskFormatter("##:##",
												"0123456789",
												"0".charAt(0));
	}
	
	/**
	 * �X�֔ԍ��pMaskFormatter���쐬����B
	 * @return �X�֔ԍ��pMaskFormatter
	 */
	public static MaskFormatter createPostalCodeFormatter()
	{
		return	FormatterCreator.createMaskFormatter("###-####",
												"0123456789",
												"_".charAt(0));
	}
	
	/**
	 * �d�b�ԍ��pMaskFormatter���쐬����B
	 * @return �d�b�ԍ��pMaskFormatter
	 */
	public static MaskFormatter createPhoneNumberFormatter()
	{
		return	FormatterCreator.createMaskFormatter(null,
												"0123456789-#*");
	}
	
	
	/**
	 * NumberFormatter���쐬����B
	 * @param maximumIntegerDigits �������̍ő包��
	 * @param maximumFractionDigits �������̍ő包��
	 * @param minimumValue �ŏ��l
	 * @param maximumValue �ő�l
	 * @return NumberFormatter
	 */
	public static NumberFormatter createNumberFormatter(
			int maximumIntegerDigits,
			int maximumFractionDigits,
			double minimumValue,
			double maximumValue)
	{
		DecimalFormat	df	=	new DecimalFormat();
		df.setMaximumIntegerDigits(maximumIntegerDigits);
		df.setMaximumFractionDigits(maximumFractionDigits);
		NumberFormatter	nf	=	new NumberFormatter(df);
		nf.setMinimum(minimumValue);
		nf.setMaximum(maximumValue);
		
		return	nf;
	}
	
	
	/**
	 * �����l�݂̂�NumberFormatter���쐬����B
	 * @param maximumIntegerDigits �������̍ő包��
	 * @param minimumValue �ŏ��l
	 * @param maximumValue �ő�l
	 * @return �����l�݂̂�NumberFormatter
	 */
	public static NumberFormatter createNumberFormatter(
			int maximumIntegerDigits,
			int minimumValue,
			int maximumValue)
	{
		DecimalFormat	df	=	new DecimalFormat();
		df.setMinimumIntegerDigits(0);
		df.setMaximumIntegerDigits(maximumIntegerDigits);
		NumberFormatter	nf	=	new NumberFormatter(df);
		nf.setMinimum(minimumValue);
		nf.setMaximum(maximumValue);
		
		return	nf;
	}
	
	/**
	 * �����l�݂̂�NumberFormatter���쐬����B
	 * @param maximumIntegerDigits �������̍ő包��
	 * @param minimumValue �ŏ��l
	 * @param maximumValue �ő�l
	 * @return �����l�݂̂�NumberFormatter
	 */
	public static NumberFormatter createNumberFormatter(
			int maximumIntegerDigits,
			long minimumValue,
			long maximumValue)
	{
		DecimalFormat	df	=	new DecimalFormat();
		df.setMinimumIntegerDigits(0);
		df.setMaximumIntegerDigits(maximumIntegerDigits);
		NumberFormatter	nf	=	new NumberFormatter(df);
		nf.setMinimum(minimumValue);
		nf.setMaximum(maximumValue);
		
		return	nf;
	}
	
	/**
	 * DateFormatter���쐬����B
	 * @param pattern ���t�̏���
	 * @return DateFormatter
	 */
	public static DateFormatter createDateFormatter(
			String pattern)
	{
		DateFormat	df	=	new SimpleDateFormat(pattern);

		return	new DateFormatter(df);
	}
	
}

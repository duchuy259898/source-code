/*
 * CustomFilter.java
 *
 * Created on 2006/07/05, 12:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author katagiri
 */
public class CustomFilter extends DocumentFilter
{
	/**
	 * ���l�̐��K�\��
	 */
	public static final String	NUMBER			=	"0-9";
	
	/**
	 * �����̐��K�\��
	 */
	public static final String	INTEGER	=	"0-9-";
	
	/**
	 * ���l�̐��K�\��
	 */
	public static final String	NUMERIC			=	"0-9-\\.";
	
	/**
	 * �啶���A���t�@�x�b�g�̐��K�\��
	 */
	public static final String	ALPHABET_BIG	=	"A-Z";
	/**
	 * �������A���t�@�x�b�g�̐��K�\��
	 */
	public static final String	ALPHABET_SMALL	=	"a-z";
	/**
	 * �A���t�@�x�b�g�̐��K�\��
	 */
	public static final String	ALPHABET		=	"a-zA-Z";
	
	/**
	 * �p�����̐��K�\���i�n�C�t���܂ށj
	 */
	public static final String	ALPHAMERIC		=	"0-9a-zA-Z-";
	
	/**
	 * �L���̐��K�\��
	 */
	public static final String	SYMBOL			=	"\\p{Punct}";
	
	/**
	 * ���p�����̐��K�\��
	 */
	public static final String	HALF_CHAR		=	"\\p{Graph}";
	
	/**
	 * �d�b�ԍ��Ŏg���镶���̐��K�\��
	 */
	public static final String	PHONE_NUMBER	=	"0-9#\\*";
	
	/**
	 * ���[���A�h���X�Ŏg���镶���̐��K�\��
	 */
//	public static final String	MAIL_ADDRESS	=	"\\p{Alnum}!\\$\\%\\&\\*\\+-\\./=\\?@\\^\\_\\~";
	public static final String	MAIL_ADDRESS	=	"\\p{Alnum}-\\._@";
	
	/**
	 * �ő啶����
	 */
	protected	int		limit;
	
	/**
	 * �g�p�\�ȕ����̐��K�\��
	 */
	protected	String		validValues		=	"";

	/**
	 * �ő啶�������擾����B
	 * @return �ő啶����
	 */
	public int getLimit()
	{
		return limit;
	}

	/**
	 * �ő啶�������Z�b�g����B
	 * @param limit �ő啶����
	 */
	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	/**
	 * �g�p�\�ȕ����̐��K�\�����擾����B
	 * @return �g�p�\�ȕ����̐��K�\��
	 */
	public String getValidValues()
	{
		return validValues;
	}

	/**
	 * �g�p�\�ȕ����̐��K�\�����Z�b�g����B
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public void setValidValues(String validValues)
	{
		this.validValues = validValues;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param limit �ő啶����
	 */
	public CustomFilter(int limit)
	{
		this.limit	=	limit;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public CustomFilter(String validValues)
	{
		this.validValues	=	validValues;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param limit �ő啶����
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public CustomFilter(int limit, String validValues)
	{
		this.limit	=	limit;
		this.validValues	=	validValues;
	}
	
	/**
	 * offset ���� offset + length �܂ł̃e�L�X�g�̗̈���폜���Astring �ɒu�������܂��B
	 * @param fb Document �̕ύX�Ɏg�p���� FilterBypass
	 * @param offset Document �ł̈ʒu
	 * @param length �폜����e�L�X�g�̒���
	 * @param string �}������e�L�X�g�Bnull �̏ꍇ�A�e�L�X�g�͑}������Ȃ�
	 * @param attr �}�����ꂽ�e�L�X�g�̑��������� AttributeSet�Bnull ����
	 * @throws javax.swing.text.BadLocationException 
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offset,
			int length, String string, AttributeSet attr) throws BadLocationException
	{
		//�}������e�L�X�g��null�̏ꍇ�A�����𔲂���
		if(string == null)
		{
			super.replace(fb, offset, length, string, attr);
			return;
		}
		
		Document	doc			=	fb.getDocument();
		int			totalLen	=	doc.getLength();
		
		//���͉\�ȕ����ȊO���폜����
		string	=	this.checkString(string);
		
		int			len			=	string.length();
		
		//�ő啶������0�̏ꍇ
		if(limit == 0)
		{
			super.replace(fb, offset, length, string, attr);
		}
		//
		else if(limit <= totalLen - length)
		{
			if(0 <= limit - totalLen + length)
			{
				string	=	string.substring(0, limit - totalLen + length);
			}
			else
			{
				string	=	"";
			}
			super.replace(fb, offset, length, string, attr);
		}
		//
		else if(limit <= (offset + len))
		{
			string	=	string.substring(0, limit - totalLen + length);
			super.replace(fb, offset, length, string, attr);
		}
		//
		else if(limit <= (offset + len + totalLen - offset - length))
		{
			string	=	string.substring(0, limit - totalLen + length);
			super.replace(fb, offset, length, string, attr);
		}
		//
		else
		{
			super.replace(fb, offset, length, string, attr);
		}
	}
	
	/**
	 * ���͉\�ȕ������`�F�b�N����B
	 * @param string �`�F�b�N���镶��
	 * @return ���͉\�ȕ����̂�
	 */
	private String checkString(String string)
	{
		if(validValues.equals(""))
				return	string;
		
		StringBuffer	sb	=	new StringBuffer();
		
		for(int i = 0; i < string.length(); i ++)
		{
			char	ch	=	string.charAt(i);
			
			if(Character.toString(ch).matches("[" + this.getValidValues() + "]*"))
					sb.append(ch);
		}
		
		return	sb.toString();
	}
}

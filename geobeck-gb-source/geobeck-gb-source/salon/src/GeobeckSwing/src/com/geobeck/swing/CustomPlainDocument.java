/*
 * CustomPlainDocument.java
 *
 * Created on 2006/04/20, 9:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.text.*;

/**
 * �Ǝ���PlainDocument�N���X
 * @author katagiri
 */
public class CustomPlainDocument extends PlainDocument
{
	/**
	 * ���l�̐��K�\��
	 */
	public static final String	NUMERIC			=	"0-9";
	
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
	 * �p�����̐��K�\��
	 */
	public static final String	ALPHAMERIC		=	"\\p{Alnum}";
	
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
	public static final String	PHONE_NUMBER	=	"0-9#\\*-";
	
	/**
	 * ���[���A�h���X�Ŏg���镶���̐��K�\��
	 */
	public static final String	MAIL_ADDRESS	=	"\\p{Alnum}!\\$\\%\\&\\*\\+-\\./=\\?@\\^\\_\\~";
	
	/**
	 * �ő啶����
	 */
	protected	int			maxLength		=	0;
	
	/**
	 * �g�p�\�ȕ����̐��K�\��
	 */
	protected	String		validValues		=	"";
	
	/**
	 * �R���X�g���N�^
	 */
	public CustomPlainDocument()
	{
		this(0, "");
	}
	
	/**
	 * �R���X�g���N�^
	 * @param maxLength �ő啶����
	 */
	public CustomPlainDocument(int maxLength)
	{
		this(maxLength, "");
	}
	
	/**
	 * �R���X�g���N�^
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public CustomPlainDocument(String validValues)
	{
		this(0, validValues);
	}
	
	public CustomPlainDocument(DocumentFilter filter)
	{
		this(0, "", filter);
	}
	
	/**
	 * �R���X�g���N�^
	 * @param maxLength �ő啶����
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public CustomPlainDocument(int maxLength, String validValues)
	{
		this.setMaxLength(maxLength);
		this.setValidValues(validValues);
	}
	
	/**
	 * �R���X�g���N�^
	 * @param maxLength �ő啶����
	 * @param filter �t�B���^�[
	 */
	public CustomPlainDocument(int maxLength, DocumentFilter filter)
	{
		this(maxLength, "", filter);
	}
	
	/**
	 * �R���X�g���N�^
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 * @param filter �t�B���^�[
	 */
	public CustomPlainDocument(String validValues, DocumentFilter filter)
	{
		this(0, validValues, filter);
	}
	
	/**
	 * �R���X�g���N�^
	 * @param maxLength �ő啶����
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 * @param filter �t�B���^�[
	 */
	public CustomPlainDocument(int maxLength, String validValues,
			DocumentFilter filter)
	{
		this.setMaxLength(maxLength);
		this.setValidValues(validValues);
		this.setDocumentFilter(filter);
	}

	/**
	 * �ő啶�������擾����B
	 * @return �ő啶����
	 */
	public int getMaxLength()
	{
		return maxLength;
	}

	/**
	 * �ő啶�������Z�b�g����B
	 * @param maxLength �ő啶����
	 */
	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
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
	 * �������}������B
	 * @param offs �I�t�Z�b�g
	 * @param str �}�����镶����
	 * @param a 
	 */
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		if(this.isMatchValidValues(str) && this.checkLength())
		{
			super.insertString(offs, str, a);
		}
		else
		{
			super.insertString(offs, "", a);
		}
	}
	
	/**
	 * �g�p�\�ȕ����ƈ�v���邩�`�F�b�N����B
	 * @param str �`�F�b�N���镶����
	 * @return true - �n�j
	 */
	private boolean isMatchValidValues(String str)
	{
		if(this.getValidValues().equals(""))	return	true;
		
		return	str.matches("[" + this.getValidValues() + "]*");
	}
	
	/**
	 * ������̒����`�F�b�N
	 * @return true - �n�j
	 */
	private boolean checkLength()
	{
		if(this.getMaxLength() == 0)	return	true;
		
		return	(this.getLength() < this.getMaxLength());
	}
}

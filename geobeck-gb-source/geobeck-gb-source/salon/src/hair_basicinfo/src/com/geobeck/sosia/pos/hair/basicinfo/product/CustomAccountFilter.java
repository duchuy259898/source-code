/*
 * CustomFilter.java
 *
 * Created on 2006/07/05, 12:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;

import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author katagiri
 */
public class CustomAccountFilter extends DocumentFilter
{
	/**
	 * ���l�̐��K�\��
	 */
	public static final String	NUMBER			=	"0-9";

	/**
	 * �啶���A���t�@�x�b�g�̐��K�\��
	 */
	public static final String	ALPHABET_BIG            =	"A-Z";

	/**
	 * �L���̐��K�\��
	 */
	public static final String	SYMBOL			=	"()\\-,/.\\s";
	
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
	public CustomAccountFilter(int limit)
	{
		this.limit	=	limit;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public CustomAccountFilter(String validValues)
	{
		this.validValues	=	validValues;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param limit �ő啶����
	 * @param validValues �g�p�\�ȕ����̐��K�\��
	 */
	public CustomAccountFilter(int limit, String validValues)
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
//		if(validValues.equals(""))
//				return	string;
		
		StringBuffer	sb	=	new StringBuffer();
		
		for(int i = 0; i < string.length(); i ++)
		{
			char	ch	=	string.charAt(i);
			
			if(Character.toString(ch).matches("[" + NUMBER + "]*")
                                ||Character.toString(ch).matches("[" + ALPHABET_BIG + "]*")
                                ||Character.toString(ch).matches("[" + SYMBOL + "]*")
                                ||isHalfWidth(ch)
                                ||isSymbol(ch))
					sb.append(ch);
		}
		
		return	sb.toString();
	}
        /**
         * ���p�J�i���� 
         * @param c
         * @return 
         */
        public boolean isHalfWidth(char c)
        {
            // ���p�J�i���� 
            if(  '\uff61' <=  c && c <='\uff9f' ){
                return true ;
            }
            return false;
        }
        /**
         * ���_�E�����_
         */
        private boolean isSymbol(char c ) {
            
            if ( c == '�' || c == '�' || Character.isSpaceChar(c) ) {
			return true ;
            }
            return false;
        }
}

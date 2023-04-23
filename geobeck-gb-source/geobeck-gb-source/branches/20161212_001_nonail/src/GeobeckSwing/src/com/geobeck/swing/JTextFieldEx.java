/*
 * JTextFieldEx.java
 *
 * Created on 2006/07/12, 14:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.im.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * JTextField�̋@�\�g���N���X
 * @author katagiri
 */
public class JTextFieldEx extends JTextField
{
	/**
	 * ���{����͂����邩
	 */
	protected boolean		inputKanji		=	false;
	
	/** �R���X�g���N�^ */
	public JTextFieldEx()
	{
		super();
		this.addChangeIME();
	}
	/**
	 * 
	 * @param doc 
	 * @param text 
	 * @param columns 
	 */
	public JTextFieldEx(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param columns 
	 */
	public JTextFieldEx(int columns)
	{
		super(columns);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param text 
	 */
	public JTextFieldEx(String text)
	{
		super(text);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param text 
	 * @param columns 
	 */
	public JTextFieldEx(String text, int columns)
	{
		super(text, columns);
		this.addChangeIME();
	}
	
	/**
	 * IME��ύX����FocusListener��ǉ�
	 */
	private void addChangeIME()
	{
		this.addFocusListener(new java.awt.event.FocusAdapter()
		{
			public void focusGained(java.awt.event.FocusEvent evt)
			{
				changeIME(true);
			}
			public void focusLost(java.awt.event.FocusEvent evt)
			{
				changeIME(false);
			}
		});
	}

	/**
	 * ���{����͂����邩���擾����B
	 * @return ���{����͂����� true
	 */
	public boolean isInputKanji()
	{
		return inputKanji;
	}

	/**
	 * ���{����͂����邩���Z�b�g����B
	 * @param inputKanji ���{����͂����� true
	 */
	public void setInputKanji(boolean inputKanji)
	{
		this.inputKanji = inputKanji;
	}
	
	/**
	 * IME�̏�Ԃ�ύX����B
	 * @param isOn IME��On�ɂ��� true
	 */
	private void changeIME(boolean isOn)
	{
		if(isOn && this.isInputKanji())
		{
			this.getInputContext().setCharacterSubsets(new Character.Subset[] {InputSubset.KANJI});
		}
		else
		{
			this.getInputContext().setCharacterSubsets(null);
		}
	}
}

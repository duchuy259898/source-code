/*
 * JFormattedTextFieldEx.java
 *
 * Created on 2006/07/12, 14:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.im.*;
import javax.swing.*;
import java.text.*;

/**
 * JFormattedTextField�̋@�\�g���N���X
 * @author katagiri
 */
public class JFormattedTextFieldEx extends JFormattedTextField
{
	/**
	 * ���{����͂����邩
	 */
	protected boolean		inputKanji		=	false;
	
	/** �R���X�g���N�^ */
	public JFormattedTextFieldEx()
	{
		super();
		this.addChangeIME();
	}
	
	/**
	 * 
	 * @param format 
	 */
	public JFormattedTextFieldEx(Format format)
	{
		super(format);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param formatter 
	 */
	public JFormattedTextFieldEx(JFormattedTextField.AbstractFormatter formatter)
	{
		super(formatter);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param factory 
	 */
	public JFormattedTextFieldEx(JFormattedTextField.AbstractFormatterFactory factory)
	{
		super(factory);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param factory 
	 * @param currentValue 
	 */
	public JFormattedTextFieldEx(JFormattedTextField.AbstractFormatterFactory factory, Object currentValue)
	{
		super(factory, currentValue);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param value 
	 */
	public JFormattedTextFieldEx(Object value)
	{
		super(value);
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
		if(this.getInputContext() != null)
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
}

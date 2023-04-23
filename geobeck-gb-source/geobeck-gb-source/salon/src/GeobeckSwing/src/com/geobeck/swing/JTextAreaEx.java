/*
 * JTextAreaEx.java
 *
 * Created on 2006/07/12, 14:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.im.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * JTextArea�̋@�\�g���N���X
 * @author katagiri
 */
public class JTextAreaEx extends JTextArea
{
	/**
	 * ���{����͂����邩
	 */
	protected boolean		inputKanji		=	false;
	
	/** �R���X�g���N�^ */
	public JTextAreaEx()
	{
		super();
		this.addChangeIME();
	}
	/**
	 * 
	 * @param doc 
	 */
	public JTextAreaEx(Document doc)
	{
		super(doc);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param doc 
	 * @param text 
	 * @param rows 
	 * @param columns 
	 */
	public JTextAreaEx(Document doc, String text, int rows, int columns)
	{
		super(doc, text, rows, columns);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param rows 
	 * @param columns 
	 */
	public JTextAreaEx(int rows, int columns)
	{
		super(rows, columns);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param text 
	 */
	public JTextAreaEx(String text)
	{
		super(text);
		this.addChangeIME();
	}
	/**
	 * 
	 * @param text 
	 * @param rows 
	 * @param columns 
	 */
	public JTextAreaEx(String text, int rows, int columns)
	{
		super(text, rows, columns);
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
                if (this.getInputContext() == null) return;
                
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

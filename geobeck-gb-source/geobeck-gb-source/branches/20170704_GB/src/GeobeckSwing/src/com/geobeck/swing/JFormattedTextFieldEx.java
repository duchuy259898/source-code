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
 * JFormattedTextFieldの機能拡張クラス
 * @author katagiri
 */
public class JFormattedTextFieldEx extends JFormattedTextField
{
	/**
	 * 日本語入力をするか
	 */
	protected boolean		inputKanji		=	false;
	
	/** コンストラクタ */
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
	 * IMEを変更するFocusListenerを追加
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
	 * 日本語入力をするかを取得する。
	 * @return 日本語入力をする true
	 */
	public boolean isInputKanji()
	{
		return inputKanji;
	}

	/**
	 * 日本語入力をするかをセットする。
	 * @param inputKanji 日本語入力をする true
	 */
	public void setInputKanji(boolean inputKanji)
	{
		this.inputKanji = inputKanji;
	}
	
	/**
	 * IMEの状態を変更する。
	 * @param isOn IMEをOnにする true
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

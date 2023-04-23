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
 * JTextFieldの機能拡張クラス
 * @author katagiri
 */
public class JTextFieldEx extends JTextField
{
	/**
	 * 日本語入力をするか
	 */
	protected boolean		inputKanji		=	false;
	
	/** コンストラクタ */
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

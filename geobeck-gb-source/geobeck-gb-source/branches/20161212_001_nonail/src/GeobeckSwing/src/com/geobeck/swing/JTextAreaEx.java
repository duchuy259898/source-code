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
 * JTextAreaの機能拡張クラス
 * @author katagiri
 */
public class JTextAreaEx extends JTextArea
{
	/**
	 * 日本語入力をするか
	 */
	protected boolean		inputKanji		=	false;
	
	/** コンストラクタ */
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

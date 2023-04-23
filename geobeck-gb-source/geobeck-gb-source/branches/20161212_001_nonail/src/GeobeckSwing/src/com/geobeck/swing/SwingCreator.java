/*
 * SwingCreator.java
 *
 * Created on 2006/05/09, 16:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import javax.swing.*;
import javax.swing.text.*;
import java.text.*;

/**
 * Swingを作成するクラス
 * @author katagiri
 */
public class SwingCreator
{
	
	/**
	 * コンストラクタ
	 */
	public SwingCreator()
	{
	}
	
	
	/**
	 * JFormattedTextFieldを作成する。
	 * @param formatter フォーマッタ
	 * @param horizontalAlignment 文字列の水平位置
	 * @return JFormattedTextField
	 */
	public static JFormattedTextField createJFormattedTextField(
			JFormattedTextField.AbstractFormatter formatter,
			int horizontalAlignment)
	{
		JFormattedTextField	temp	=	new JFormattedTextField(formatter);
		temp.setHorizontalAlignment(horizontalAlignment);
		
		return	temp;
	}
	
	/**
	 * 画像のみのボタンを取得する。
	 * @param name 名称
	 * @param defaultIconName 通常時の画像のパス
	 * @param rolloverIconName ロールオーバー時の画像のパス
	 * @return 画像のみのボタン
	 */
	public static JButton createImageButton(String name, String defaultIconPath, String rolloverIconPath)
	{
		return	createImageButton(name, defaultIconPath, rolloverIconPath, null);
	}
	
	/**
	 * 画像のみのボタンを取得する。
	 * @param name 名称
	 * @param defaultIconName 通常時の画像のパス
	 * @param rolloverIconName ロールオーバー時の画像のパス
	 * @param rolloverIconName ロールオーバー時の画像のパス
	 * @return 画像のみのボタン
	 */
	public static JButton createImageButton(String name, String defaultIconPath, String rolloverIconPath, String disabledIconPath)
	{
		JButton		button	=	new JButton();
		if(name != null)
		{
			button.setToolTipText(name);
		}
		button.setIcon(new javax.swing.ImageIcon(
				SwingCreator.class.getResource(defaultIconPath)));
		if(rolloverIconPath != null)
		{
			button.setRolloverIcon(new javax.swing.ImageIcon(
					SwingCreator.class.getResource(rolloverIconPath)));
			button.setPressedIcon(button.getRolloverIcon());
		}
		if(disabledIconPath != null)
		{
			button.setDisabledIcon(new javax.swing.ImageIcon(
					SwingCreator.class.getResource(disabledIconPath)));
		}
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setSize(button.getIcon().getIconWidth(), button.getIcon().getIconHeight());
		
		return	button;
	}
}

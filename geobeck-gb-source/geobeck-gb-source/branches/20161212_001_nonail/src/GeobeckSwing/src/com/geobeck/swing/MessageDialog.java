/*
 * MessageDialog.java
 *
 * Created on 2006/04/26, 16:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import javax.swing.*;


/**
 * メッセージダイアログ
 * @author katagiri
 */
public class MessageDialog
{
	
	/**
	 * Creates a new instance of MessageDialog
	 */
	public MessageDialog()
	{
	}
	
	/**
	 * OKのみのメッセージボックスを表示する。
	 * @param parent 親コンポーネント
	 * @param message メッセージ
	 * @param title タイトル
	 * @param messageType メッセージの種類
	 */
	public static void showMessageDialog(Component parent, 
			Object message,
			String title,
			int messageType)
	{
		JOptionPane.showMessageDialog(parent,
				message,
				title,
				messageType);
	}
	
	/**
	 * OKのみのメッセージボックスを表示する。
	 * @param parentFrame 親フレーム
	 * @param message メッセージ
	 * @param messageType メッセージの種類
	 */
	public static void showMessageDialog(JFrame parentFrame, 
			Object message,
			int messageType)
	{
		JOptionPane.showMessageDialog(parentFrame,
				message,
				parentFrame.getTitle(),
				messageType);
	}
	
	/**
	 * OKのみのメッセージボックスを表示する。
	 * @param parentDialog 親ダイアログ
	 * @param message メッセージ
	 * @param messageType メッセージの種類
	 */
	public static void showMessageDialog(JDialog parentDialog, 
			Object message,
			int messageType)
	{
		JOptionPane.showMessageDialog(parentDialog,
				message,
				parentDialog.getTitle(),
				messageType);
	}
	
	/**
	 * YesNoのメッセージボックスを表示する。
	 * @param parent 親コンポーネント
	 * @param message メッセージ
	 * @param title タイトル
	 * @param messageType メッセージの種類
	 * @param initialValue メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoDialog(Component parent, 
			Object message,
			String title,
			int messageType,
			Object initialValue)
	{
		return	JOptionPane.showOptionDialog(parent,
					message,
					title,
					JOptionPane.YES_NO_OPTION,
					messageType,
					null, 
					null,
					initialValue);
	}
	
	/**
	 * YesNoのメッセージボックスを表示する。
	 * @param parent 親コンポーネント
	 * @param message メッセージ
	 * @param title タイトル
	 * @param messageType メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoDialog(Component parent, 
			Object message,
			String title,
			int messageType)
	{
		return	JOptionPane.showOptionDialog(parent,
					message,
					title,
					JOptionPane.YES_NO_OPTION,
					messageType,
					null, null, null);
	}
	
	/**
	 * YesNoのメッセージボックスを表示する。
	 * @param parentFrame 親フレーム
	 * @param message メッセージ
	 * @param messageType メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoDialog(JFrame parentFrame, 
			Object message,
			int messageType)
	{
		return	JOptionPane.showOptionDialog(parentFrame,
					message,
					parentFrame.getTitle(),
					JOptionPane.YES_NO_OPTION,
					messageType,
					null, null, null);
	}
	
	/**
	 * YesNoのメッセージボックスを表示する。
	 * @param parentDialog 親ダイアログ
	 * @param message メッセージ
	 * @param messageType メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoDialog(JDialog parentDialog, 
			Object message,
			int messageType)
	{
		return	JOptionPane.showOptionDialog(parentDialog,
					message,
					parentDialog.getTitle(),
					JOptionPane.YES_NO_OPTION,
					messageType,
					null, null, null);
	}
	
	/**
	 * YesNoCancelのメッセージボックスを表示する。
	 * @param parent 親コンポーネント
	 * @param message メッセージ
	 * @param title タイトル
	 * @param messageType メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoCancelDialog(Component parent, 
			Object message,
			String title,
			int messageType)
	{
		return	JOptionPane.showOptionDialog(parent,
					message,
					title,
					JOptionPane.YES_NO_CANCEL_OPTION,
					messageType,
					null, null, null);
	}
	
	/**
	 * YesNoCancelのメッセージボックスを表示する。
	 * @param parentFrame 親フレーム
	 * @param message メッセージ
	 * @param messageType メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoCancelDialog(JFrame parentFrame, 
			Object message,
			int messageType)
	{
		return	JOptionPane.showOptionDialog(parentFrame,
					message,
					parentFrame.getTitle(),
					JOptionPane.YES_NO_CANCEL_OPTION,
					messageType,
					null, null, null);
	}
	
	/**
	 * YesNoCancelのメッセージボックスを表示する。
	 * @param parentDialog 親ダイアログ
	 * @param message メッセージ
	 * @param messageType メッセージの種類
	 * @return 押されたボタンの種類
	 */
	public static int showYesNoCancelDialog(JDialog parentDialog, 
			Object message,
			int messageType)
	{
		return	JOptionPane.showOptionDialog(parentDialog,
					message,
					parentDialog.getTitle(),
					JOptionPane.YES_NO_CANCEL_OPTION,
					messageType,
					null, null, null);
	}
}

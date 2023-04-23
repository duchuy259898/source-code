/*
 * MoveNextField.java
 *
 * Created on 2006/04/19, 10:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Enterが押されたとき、次のコンポーネントにフォーカスを移動させるKeyListenner
 * @author katagiri
 */
public class MoveNextField implements KeyListener
{
	KeyboardFocusManager		kfm		=	null;
	
	/** Creates a new instance of MoveNextField */
	public MoveNextField()
	{
		kfm	=	KeyboardFocusManager.getCurrentKeyboardFocusManager();
	}
	
	/**
	 * キーを押しているときに呼び出されます。
	 * @param e 
	 */
	public void keyPressed(KeyEvent e)
	{
		//ENTERが押された場合
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			//シフトが押されている場合
			if(e.isShiftDown())
			{
				//前のコンポーネントにフォーカスを移動
				kfm.focusPreviousComponent();
			}
			else
			{
				//次のコンポーネントにフォーカスを移動
				kfm.focusNextComponent();
			}
		}
	}
	
	/**
	 * キーを離したときに呼び出されます。
	 * @param e 
	 */
	public void keyReleased(KeyEvent e)
	{
	}
	
	/**
	 * キーをタイプすると呼び出されます。
	 * @param e 
	 */
	public void keyTyped(KeyEvent e)
	{
	}
}

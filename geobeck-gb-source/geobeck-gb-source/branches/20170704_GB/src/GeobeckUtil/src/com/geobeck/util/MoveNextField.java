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
 * Enter�������ꂽ�Ƃ��A���̃R���|�[�l���g�Ƀt�H�[�J�X���ړ�������KeyListenner
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
	 * �L�[�������Ă���Ƃ��ɌĂяo����܂��B
	 * @param e 
	 */
	public void keyPressed(KeyEvent e)
	{
		//ENTER�������ꂽ�ꍇ
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			//�V�t�g��������Ă���ꍇ
			if(e.isShiftDown())
			{
				//�O�̃R���|�[�l���g�Ƀt�H�[�J�X���ړ�
				kfm.focusPreviousComponent();
			}
			else
			{
				//���̃R���|�[�l���g�Ƀt�H�[�J�X���ړ�
				kfm.focusNextComponent();
			}
		}
	}
	
	/**
	 * �L�[�𗣂����Ƃ��ɌĂяo����܂��B
	 * @param e 
	 */
	public void keyReleased(KeyEvent e)
	{
	}
	
	/**
	 * �L�[���^�C�v����ƌĂяo����܂��B
	 * @param e 
	 */
	public void keyTyped(KeyEvent e)
	{
	}
}

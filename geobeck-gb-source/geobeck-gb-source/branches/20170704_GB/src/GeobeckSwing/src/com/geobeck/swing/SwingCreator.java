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
 * Swing���쐬����N���X
 * @author katagiri
 */
public class SwingCreator
{
	
	/**
	 * �R���X�g���N�^
	 */
	public SwingCreator()
	{
	}
	
	
	/**
	 * JFormattedTextField���쐬����B
	 * @param formatter �t�H�[�}�b�^
	 * @param horizontalAlignment ������̐����ʒu
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
	 * �摜�݂̂̃{�^�����擾����B
	 * @param name ����
	 * @param defaultIconName �ʏ펞�̉摜�̃p�X
	 * @param rolloverIconName ���[���I�[�o�[���̉摜�̃p�X
	 * @return �摜�݂̂̃{�^��
	 */
	public static JButton createImageButton(String name, String defaultIconPath, String rolloverIconPath)
	{
		return	createImageButton(name, defaultIconPath, rolloverIconPath, null);
	}
	
	/**
	 * �摜�݂̂̃{�^�����擾����B
	 * @param name ����
	 * @param defaultIconName �ʏ펞�̉摜�̃p�X
	 * @param rolloverIconName ���[���I�[�o�[���̉摜�̃p�X
	 * @param rolloverIconName ���[���I�[�o�[���̉摜�̃p�X
	 * @return �摜�݂̂̃{�^��
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

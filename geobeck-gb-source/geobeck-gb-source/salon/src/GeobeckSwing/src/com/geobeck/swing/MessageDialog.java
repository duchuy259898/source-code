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
 * ���b�Z�[�W�_�C�A���O
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
	 * OK�݂̂̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parent �e�R���|�[�l���g
	 * @param message ���b�Z�[�W
	 * @param title �^�C�g��
	 * @param messageType ���b�Z�[�W�̎��
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
	 * OK�݂̂̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parentFrame �e�t���[��
	 * @param message ���b�Z�[�W
	 * @param messageType ���b�Z�[�W�̎��
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
	 * OK�݂̂̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parentDialog �e�_�C�A���O
	 * @param message ���b�Z�[�W
	 * @param messageType ���b�Z�[�W�̎��
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
	 * YesNo�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parent �e�R���|�[�l���g
	 * @param message ���b�Z�[�W
	 * @param title �^�C�g��
	 * @param messageType ���b�Z�[�W�̎��
	 * @param initialValue ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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
	 * YesNo�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parent �e�R���|�[�l���g
	 * @param message ���b�Z�[�W
	 * @param title �^�C�g��
	 * @param messageType ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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
	 * YesNo�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parentFrame �e�t���[��
	 * @param message ���b�Z�[�W
	 * @param messageType ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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
	 * YesNo�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parentDialog �e�_�C�A���O
	 * @param message ���b�Z�[�W
	 * @param messageType ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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
	 * YesNoCancel�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parent �e�R���|�[�l���g
	 * @param message ���b�Z�[�W
	 * @param title �^�C�g��
	 * @param messageType ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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
	 * YesNoCancel�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parentFrame �e�t���[��
	 * @param message ���b�Z�[�W
	 * @param messageType ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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
	 * YesNoCancel�̃��b�Z�[�W�{�b�N�X��\������B
	 * @param parentDialog �e�_�C�A���O
	 * @param message ���b�Z�[�W
	 * @param messageType ���b�Z�[�W�̎��
	 * @return �����ꂽ�{�^���̎��
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

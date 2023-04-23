/*
 * CellEditorCreator.java
 *
 * Created on 2006/05/09, 16:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.text.*;

/**
 * CellEditor���쐬����N���X
 * @author katagiri
 */
public class CellEditorCreator
{
	
	/** Creates a new instance of CellEditorCreator */
	public CellEditorCreator()
	{
	}
	
	/**
	 * JFormattedText�^��CellEditor���쐬����B
	 * @param formatter �t�H�[�}�b�^
	 * @param horizontalAlignment ������̐����z�u���@
	 * @return JFormattedText�^��CellEditor
	 */
	public static DefaultCellEditor createJFormattedTextFieldCellEditor(
			JFormattedTextField.AbstractFormatter formatter,
			int horizontalAlignment)
	{
		JFormattedTextField	temp	=	new JFormattedTextField(formatter);
		temp.setHorizontalAlignment(horizontalAlignment);
		
		return	new FTFCellEditor(temp);
	}
	
	
	/**
	 * JFormattedTextField�^�Z���p��CellEditor
	 */
	private static class FTFCellEditor extends DefaultCellEditor
	{
		/**
		 * �R���X�g���N�^
		 * @param ftf JFormattedTextField
		 */
		public FTFCellEditor(JFormattedTextField ftf)
		{
			super(ftf);
			this.setClickCountToStart(1);
		}
	}
}

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
 * CellEditorを作成するクラス
 * @author katagiri
 */
public class CellEditorCreator
{
	
	/** Creates a new instance of CellEditorCreator */
	public CellEditorCreator()
	{
	}
	
	/**
	 * JFormattedText型のCellEditorを作成する。
	 * @param formatter フォーマッタ
	 * @param horizontalAlignment 文字列の水平配置方法
	 * @return JFormattedText型のCellEditor
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
	 * JFormattedTextField型セル用のCellEditor
	 */
	private static class FTFCellEditor extends DefaultCellEditor
	{
		/**
		 * コンストラクタ
		 * @param ftf JFormattedTextField
		 */
		public FTFCellEditor(JFormattedTextField ftf)
		{
			super(ftf);
			this.setClickCountToStart(1);
		}
	}
}

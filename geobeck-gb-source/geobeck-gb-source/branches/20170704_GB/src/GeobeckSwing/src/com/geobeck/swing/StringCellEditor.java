/*
 * StringCellEditor.java
 *
 * Created on 2006/07/19, 14:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * StringópÇÃCellEditor
 * @author katagiri
 */
public class StringCellEditor extends DefaultCellEditor
{
	JTextField textField = new JTextField();

	public StringCellEditor(JTextField field)
	{
		super(field);
//		this.setClickCountToStart(0);
		this.textField = field;

		textField.addFocusListener(new FocusAdapter()
		{
			public void focusGained(FocusEvent e)
			{
				textField.selectAll();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
						 boolean isSelected, int row, int column){
		// TextFieldÇ…ì‡óeÇ™Ç†ÇÈèÍçáÇÕÅAÇªÇÃÇ‹Ç‹Ç∆Ç∑ÇÈ
		if (value != null)
		{
			textField.setText(value.toString());
		}
		else
		{
			textField.setText("");
		}

		textField.selectAll();
		return textField;
	}


	public String getCellEditorValue()
	{
		return	textField.getText();
	}
}

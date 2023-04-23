/*
 * IntegerCellEditor.java
 *
 * Created on 2006/07/19, 14:35
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
 * IntegerópÇÃCellEditor
 * @author katagiri
 */
public class IntegerCellEditor extends DefaultCellEditor
{
	JTextField textField = new JTextField();

	public IntegerCellEditor(JTextField field)
	{
		super(field);
//		this.setClickCountToStart(0);
		this.textField = field;
		textField.setHorizontalAlignment(JTextField.RIGHT);
		((PlainDocument)textField.getDocument()).setDocumentFilter(
				new CustomFilter(9, CustomFilter.INTEGER));

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


	public Integer getCellEditorValue()
	{
		if(textField.getText().equals(""))
				return	0;
		else
				return	Integer.parseInt(textField.getText());
	}
}
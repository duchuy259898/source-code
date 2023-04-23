/*
 * LostFocusEditingStopper.java
 *
 * Created on 2007/03/19, 15:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing.table;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * フォーカスを失ったときに編集を止める
 * @author katagiri
 */
public class LostFocusEditingStopper implements FocusListener
{
	public void focusLost(FocusEvent e)
	{
		JTable	table	=	(JTable)e.getSource();
		
		if(table.isEditing())
		{
			if(!(table.getCellEditor().getCellEditorValue() instanceof java.lang.Boolean ||
				table.getCellEditor().getCellEditorValue() instanceof java.lang.Number ||
				table.getCellEditor().getCellEditorValue() instanceof java.lang.String))
			{
				return;
			}
			
			table.getCellEditor().stopCellEditing();
		}
	}
	
	public void focusGained(FocusEvent e)
	{
	}
}

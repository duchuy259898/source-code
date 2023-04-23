/*
 * SelectText.java
 *
 * Created on 2006/07/05, 11:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author katagiri
 */
public class SelectText implements FocusListener
{
	public void focusGained(FocusEvent e)
	{
		if(e.getComponent() instanceof JTextField ||
				e.getComponent() instanceof JFormattedTextField)
		{
			JTextField	tf	=	(JTextField)e.getComponent();
			tf.selectAll();
		}
	}
	
	public void focusLost(FocusEvent e)
	{
	}
}

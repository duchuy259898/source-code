/*
 * SOSIASwingUtil.java
 *
 * Created on 2007/06/04, 11:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author katagiri
 */
public class SOSIASwingUtil
{
	public static final Color		EDITING_COLOR	=	new Color(236, 236, 236);
	
	/** Creates a new instance of SOSIASwingUtil */
	public SOSIASwingUtil()
	{
	}
	
	
	public static void setInputComponentColor(final Component c)
	{
		c.addFocusListener(new FocusAdapter()
		{
			public void focusGained(FocusEvent e)
			{
				c.setBackground(EDITING_COLOR);
			}
			
			public void focusLost(FocusEvent e)
			{
				c.setBackground(Color.white);
			}
		});
	}
}

/*
 * JInternalFrameEx.java
 *
 * Created on 2006/07/06, 20:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * JInternalFrameの機能拡張クラス
 * @author katagiri
 */
public class JInternalFrameEx extends JInternalFrame
{
	protected Component		opener		=	null;
	
	/** Creates a new instance of JInternalFrameEx */
	public JInternalFrameEx()
	{
		super();
	}
	
	public JInternalFrameEx(Component opener)
	{
		super();
		this.setOpener(opener);
	}

	public Component getOpener()
	{
		return opener;
	}

	public void setOpener(Component opener)
	{
		this.opener = opener;
	}
	
	public void showOpener()
	{
		if(this.getOpener() != null)
		{
			this.getOpener().setVisible(true);
		}
	}
}

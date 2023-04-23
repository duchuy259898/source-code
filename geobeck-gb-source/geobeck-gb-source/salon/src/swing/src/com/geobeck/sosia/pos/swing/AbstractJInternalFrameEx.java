/*
 * AbstractJInternalFrameEx.java
 *
 * Created on 2006/10/04, 17:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import com.geobeck.swing.*;

/**
 *
 * @author katagiri
 */
public abstract class AbstractJInternalFrameEx extends JInternalFrameEx
{
	protected AbstractMainFrame	parent		=	null;

	public AbstractMainFrame getParent()
	{
		return parent;
	}

	public void setParent(AbstractMainFrame parent)
	{
		this.parent = parent;
	}
}

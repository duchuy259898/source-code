/*
 * AbstractMainFrame.java
 *
 * Created on 2006/10/04, 17:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import javax.swing.*;

import com.geobeck.barcode.*;

/**
 *
 * @author katagiri
 */
public abstract class AbstractMainFrame extends javax.swing.JFrame implements BarcodeListener
{
	/*public abstract void showJInternalFrame(AbstractJInternalFrameEx jif);
	public abstract void showJInternalFrame(AbstractJInternalFrameEx jif, boolean isClear);
	public abstract void removeComponent();
	public abstract void removeComponent(Component com);*/
	
	private	BarcodeListener		preferentialBarcodeListener	=	null;

	public BarcodeListener getPreferentialBarcodeListener()
	{
		return preferentialBarcodeListener;
	}

	public void setPreferentialBarcodeListener(BarcodeListener preferentialBarcodeListener)
	{
		this.preferentialBarcodeListener = preferentialBarcodeListener;
	}
	
	public abstract void changeContents(AbstractImagePanelEx contents);
}

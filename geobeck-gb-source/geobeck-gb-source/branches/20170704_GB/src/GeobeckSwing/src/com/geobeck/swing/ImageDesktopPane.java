/*
 * ImageDesktopPane.java
 *
 * Created on 2006/07/14, 14:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import javax.swing.*;

/**
 * ”wŒi‰æ‘œ‚ğİ’è‚Å‚«‚éJDesktopPane
 * @author katagiri
 */
public class ImageDesktopPane extends JDesktopPane
{
	protected ImageIcon	image			=	new ImageIcon();
	
	/** Creates a new instance of ImageDesktopPane */
	public ImageDesktopPane()
	{
	}

	public ImageIcon getImage()
	{
		return image;
	}

	public void setImage(ImageIcon image)
	{
		this.image	=	image;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
//		g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
		if(image != null)
				g.drawImage(image.getImage(), 0, 0, this);
	}
	
}

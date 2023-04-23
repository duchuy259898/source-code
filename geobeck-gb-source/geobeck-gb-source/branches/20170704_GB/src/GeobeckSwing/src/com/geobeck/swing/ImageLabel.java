/*
 * ImageLabel.java
 *
 * Created on 2006/07/12, 16:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import javax.swing.*;

/**
 * ‰æ‘œ‚ğİ’è‚Å‚«‚éJLabel
 * @author katagiri
 */
public class ImageLabel extends JLabel
{
	protected ImageIcon	image			=	new ImageIcon();
	
	/** Creates a new instance of ImageLabel */
	public ImageLabel()
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
		if(image != null)
				//g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
				g.drawImage(image.getImage(), 0, 0, this);
	}
}

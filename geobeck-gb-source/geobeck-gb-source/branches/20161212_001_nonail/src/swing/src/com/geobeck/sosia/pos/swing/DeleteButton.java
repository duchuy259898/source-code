/*
 * DeleteButton.java
 *
 * Created on 2007/05/11, 16:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import javax.swing.*;

import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author katagiri
 */
public class DeleteButton extends JButton
{
	private	int		index	=	-1;
	
	/** Creates a new instance of DeleteButton */
	public DeleteButton(int index)
	{
		super();
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
		this.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setAlignmentY(0);
		this.setPreferredSize(new Dimension(48, 25));
		this.setSize(48, 25);
		this.setIndex(index);
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}
	
}

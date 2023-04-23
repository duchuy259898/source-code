/*
 * AbstractImagePanelEx.java
 *
 * Created on 2006/10/13, 16:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import javax.swing.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author katagiri
 */
public abstract class AbstractImagePanelEx extends ImagePanel
{
	protected	AbstractMainFrame	parentFrame		=	null;
	protected	String				path			=	"";
	protected	String				title			=	"";
	protected	String				titlePath		=	"";
	protected	Component			opener			=	null;
	
	public AbstractImagePanelEx()
	{
		this.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
	}
	
	public AbstractMainFrame getParentFrame()
	{
		return parentFrame;
	}
	
	public void setParentFrame(AbstractMainFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitlePath()
	{
		return titlePath;
	}

	public void setTitlePath(String titlePath)
	{
		this.titlePath = titlePath;
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
			this.parentFrame.changeContents((AbstractImagePanelEx)this.getOpener());
		}
	}
	
	public boolean isDialog()
	{
		if(this.getParent() != null &&
				this.getParent().getParent() != null &&
				this.getParent().getParent().getParent() != null &&
				this.getParent().getParent().getParent().getParent() != null &&
				this.getParent().getParent().getParent().getParent() instanceof JDialog)
		{
			return	true;
		}
		
		return	false;
	}
}

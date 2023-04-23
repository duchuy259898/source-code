/*
 * GroupTreeCellRenderer.java
 *
 * Created on 2006/08/03, 14:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class GroupTreeCellRenderer extends DefaultTreeCellRenderer
{
	/**
	 * Creates a new instance of GroupTreeCellRenderer
	 */
	public GroupTreeCellRenderer()
	{
		super();
	}
	
	public Component getTreeCellRendererComponent(
			JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(
				tree, value, selected, expanded,
				leaf, row, hasFocus);
		
		DefaultMutableTreeNode	node	=	(DefaultMutableTreeNode)value;
		
		//グループ
		if(node.getUserObject() instanceof MstGroup)
		{
			MstGroup	mg	=	(MstGroup)node.getUserObject();
			
			//最上位のグループの場合
			if(mg.getGroupID() == 1)
			{
				this.setIcon(SystemInfo.getRootIcon());
			}
			else
			{
				this.setIcon(SystemInfo.getGroupIcon());
			}
			
			this.setText(mg.getGroupName());
		}
		//店舗
		else if(node.getUserObject() instanceof MstShop)
		{
			MstShop	ms	=	(MstShop)node.getUserObject();
			this.setIcon(SystemInfo.getShopIcon());
			this.setText(ms.getShopName());
		}
		
		return	this;
	}
}

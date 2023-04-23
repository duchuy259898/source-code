/*
 * GroupComboBoxCellRenderer.java
 *
 * Created on 2006/09/06, 10:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class GroupComboBoxCellRenderer extends DefaultListCellRenderer
{
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) 
	{
		//�I������Ă���ꍇ
		if(isSelected)
		{
			this.setOpaque(true);
			this.setForeground(list.getSelectionForeground());
			this.setBackground(list.getSelectionBackground());
		}
		else
		{
			this.setOpaque(false);
			this.setForeground(list.getForeground());
			this.setBackground(list.getBackground());
		}
		
		//�O���[�v
		if(value instanceof MstGroup)
		{
			MstGroup	mg	=	(MstGroup)value;
			this.setText(mg.getGroupName());
			
			//�ŏ�ʂ̃O���[�v�̏ꍇ
			if(mg.getGroupID() == 1)
			{
				this.setIcon(SystemInfo.getRootIcon());
			}
			else
			{
				this.setIcon(SystemInfo.getGroupIcon());
			}
		}
		//�X��
		else if(value instanceof MstShop)
		{
			MstShop		ms	=	(MstShop)value;
			this.setText(ms.getShopName());
			this.setIcon(SystemInfo.getShopIcon());
		}
		
		return	this;
	}
}

/*
 * ImageTableCellRenderer.java
 *
 * Created on 2007/06/12, 11:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * �摜�pTableCellRenderer
 * @author katagiri
 */
public class ImageTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩
	 */
	protected boolean	adjust		=	false;
	
	/**
	 * �摜�̏c������ێ����邩
	 */
	protected boolean	keepProportion	=	false;
	
	/**
	 * �R���X�g���N�^
	 */
	public ImageTableCellRenderer()
	{
	}
	
	/**
	 * �R���X�g���N�^
	 * @param adjust �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩
	 * @param keepProportion �摜�̏c������ێ����邩
	 */
	public ImageTableCellRenderer(boolean adjust, boolean keepProportion)
	{
		this.setAdjust(adjust);
		this.setKeepProportion(keepProportion);
	}

	/**
	 * �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩���擾����
	 * @return �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩
	 */
	public boolean isAdjust()
	{
		return adjust;
	}

	/**
	 * �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩��ݒ肷��
	 * @param adjust �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩
	 */
	public void setAdjust(boolean adjust)
	{
		this.adjust = adjust;
	}

	/**
	 * �摜�̏c������ێ����邩���擾����
	 * @return �摜�̏c������ێ����邩
	 */
	public boolean isKeepProportion()
	{
		return keepProportion;
	}

	/**
	 * �摜�̏c������ێ����邩��ݒ肷��
	 * @param keepProportion �摜�̏c������ێ����邩
	 */
	public void setKeepProportion(boolean keepProportion)
	{
		this.keepProportion = keepProportion;
	}
	
	/**
	 * �Z���̒l��ݒ肷��
	 * @param value �Z���̒l
	 */
	protected void setValue(Object value)
	{
		if(value instanceof ImageIcon)
		{
			this.setIcon((ImageIcon)value);
		}
		else
		{
			super.setValue(value);
		}
	}
	
	/**
	 * �R���|�[�l���g��`�悷��
	 * �i�摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\������ꍇ�A�J��Ԃ��\���̐ݒ�͖��������j
	 * @param g �O���t�B�b�N�I�u�W�F�N�g
	 */
	public void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		
		//�摜���ݒ肳��Ă���ꍇ
		if(this.getIcon() != null)
		{
			ImageIcon	icon	=	(ImageIcon)this.getIcon();
			
			//�R���|�[�l���g�̃T�C�Y�ɍ��킹��ꍇ
			if(adjust)
			{
				//�c������ێ�����ꍇ
				if(keepProportion)
				{
					Double	hp	=	((Integer)getWidth()).doubleValue() / ((Integer)this.getIcon().getIconWidth()).doubleValue();
					Double	vp	=	((Integer)getHeight()).doubleValue() / ((Integer)this.getIcon().getIconHeight()).doubleValue();
					
					//�����ɍ��킹��ꍇ
					if(hp <= vp)
					{
						g.drawImage(icon.getImage(), 0, 0,
								getWidth(),
								((Double)(((Integer)this.getIcon().getIconHeight()).doubleValue() * hp)).intValue(),
								this);
					}
					//�����ɍ��킹��ꍇ
					else
					{
						g.drawImage(icon.getImage(), 0, 0,
								((Double)(((Integer)this.getIcon().getIconWidth()).doubleValue() * vp)).intValue(),
								getHeight(),
								this);
					}
				}
				else
				{
					g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
				}
			}
			else
			{
				g.drawImage(icon.getImage(), 0, 0, this);
			}
		}
		
		this.setIcon(null);
	}
}

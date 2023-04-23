/*
 * ImagePanel.java
 *
 * Created on 2006/07/12, 17:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import javax.swing.*;

/**
 * �w�i�ɉ摜��\���ł���JPanel
 * @author katagiri
 */
public class ImagePanel extends JPanel
{
	/**
	 * �摜
	 */
	protected ImageIcon	image		=	new ImageIcon();
	
	/**
	 * �摜���J��Ԃ��\�����邩
	 */
	protected boolean	repeat		=	false;
	/**
	 * �摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\�����邩
	 */
	protected boolean	adjust		=	false;
	
	protected boolean	keepProportion	=	false;
	
	/**
	 * �R���X�g���N�^
	 */
	public ImagePanel()
	{
	}

	/**
	 * �摜���擾����
	 * @return �摜
	 */
	public ImageIcon getImage()
	{
		return image;
	}

	/**
	 * �摜��ݒ肷��
	 * @param image �摜
	 */
	public void setImage(ImageIcon image)
	{
		this.image	=	image;
	}

	/**
	 * �摜���J��Ԃ��\�����邩���擾����
	 * @return �摜���J��Ԃ��\�����邩
	 */
	public boolean isRepeat()
	{
		return repeat;
	}

	/**
	 * �摜���J��Ԃ��\�����邩��ݒ肷��
	 * @param repeat �摜���J��Ԃ��\�����邩
	 */
	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
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

	public boolean isKeepProportion()
	{
		return keepProportion;
	}

	public void setKeepProportion(boolean keepProportion)
	{
		this.keepProportion = keepProportion;
	}
	
	/**
	 * �R���|�[�l���g��`�悷��
	 * �i�摜���R���|�[�l���g�̃T�C�Y�ɍ��킹�ĕ\������ꍇ�A�J��Ԃ��\���̐ݒ�͖��������j
	 * @param g �O���t�B�b�N�I�u�W�F�N�g
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//�摜���ݒ肳��Ă���ꍇ
		if(image != null)
		{
			//�R���|�[�l���g�̃T�C�Y�ɍ��킹��ꍇ
			if(adjust)
			{
				//�摜�̏c������ێ�����ꍇ
				if(keepProportion)
				{
					Double	hp	=	((Integer)getWidth()).doubleValue() / ((Integer)image.getIconWidth()).doubleValue();
					Double	vp	=	((Integer)getHeight()).doubleValue() / ((Integer)image.getIconHeight()).doubleValue();
					
					//�R���|�[�l���g�̉����ɍ��킹��ꍇ
					if(hp <= vp)
					{
						g.drawImage(image.getImage(), 0, 0,
								getWidth(),
								((Double)(((Integer)image.getIconHeight()).doubleValue() * hp)).intValue(),
								this);
					}
					//�R���|�[�l���g�̍����ɍ��킹��ꍇ
					else
					{
						g.drawImage(image.getImage(), 0, 0,
								((Double)(((Integer)image.getIconWidth()).doubleValue() * vp)).intValue(),
								getHeight(),
								this);
					}
				}
				else
				{
					g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
				}
			}
			else
			{
				//�摜���J��Ԃ��\������ꍇ
				if(repeat && 0 < image.getIconWidth() && 0 < image.getIconHeight())
				{
					for(int	x = 0; x < this.getWidth(); x += image.getIconWidth())
					{
						for(int y = 0; y < this.getHeight(); y += image.getIconHeight())
						{
							g.drawImage(image.getImage(), x, y, this);
						}
					}
				}
				else
				{
					g.drawImage(image.getImage(), 0, 0, this);
				}
			}
		}
	}
}

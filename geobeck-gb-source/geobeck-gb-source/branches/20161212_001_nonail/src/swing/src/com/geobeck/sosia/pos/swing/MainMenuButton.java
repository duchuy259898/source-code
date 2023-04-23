/*
 * MainMenuButton.java
 *
 * Created on 2007/02/07, 11:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.geobeck.swing.*;

/**
 * ���C�����j���[�̃{�^���p�N���X
 * @author katagiri
 */
public class MainMenuButton extends ImagePanel
{
	
	/**
	 * �{�^��
	 */
	private	JButton				button			=	null;
	/**
	 * �T�u���j���[
	 */
	private	JPanel				subMenu			=	null;
	
	private Color				subMenuColor	=	null;
	
	/**
	 * �����̍ő�l
	 */
	private	Integer				maxHeight		=	0;
	/**
	 * �T�u���j���[���J����Ă��邩
	 */
	private	Boolean				subMenuOpen		=	false;
	
	/**
	 * �R���X�g���N�^
	 * @param button �{�^��
	 */
	public MainMenuButton(JButton button, Color subMenuColor)
	{
		super();
		this.setLayout(null);
		this.setOpaque(false);
		//�{�^����ݒ�
		this.setButton(button);
		this.setSubMenuColor(subMenuColor);
		//�T�u���j���[��������
		subMenu	=	new JPanel()
		{
			public void paint(Graphics g)
			{
				g.setColor(getSubMenuColor());
				g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);
				super.paint(g);
			}
		};
		subMenu.setLayout(null);
		subMenu.setOpaque(false);
		this.add(subMenu);
		subMenu.setSize(this.getButton().getWidth() - 11, 8);
		subMenu.setBounds(11, this.getButton().getHeight() + 2,
				this.getButton().getWidth() - 11, 8);
	}

	/**
	 * �{�^�����擾����B
	 * @return �{�^��
	 */
	public JButton getButton()
	{
		return button;
	}

	/**
	 * �{�^����ݒ肷��B
	 * @param button �{�^��
	 */
	private void setButton(JButton button)
	{
		this.button = button;
		
		this.add(button);
		//���g�̃T�C�Y���{�^���̃T�C�Y�ɐݒ�
		this.setSize(this.getButton().getWidth(),
				this.getButton().getHeight());
		//�{�^���̈ʒu�ƃT�C�Y��ݒ�
		this.getButton().setBounds(0, 0,
				this.getButton().getWidth(),
				this.getButton().getHeight());
		
		maxHeight	=	this.getButton().getHeight();
	}

	public Color getSubMenuColor()
	{
		return subMenuColor;
	}

	private void setSubMenuColor(Color subMenuColor)
	{
		this.subMenuColor = subMenuColor;
	}
	
	/**
	 * �T�u���j���[�Ƀ{�^����ǉ�����B
	 * @param subMenuButton �T�u���j���[�̃{�^��
	 */
	public void addSubMenuButton(JButton subMenuButton)
	{
		subMenu.add(subMenuButton);
		subMenuButton.setBounds(4, subMenu.getHeight() - 4, 
				subMenuButton.getWidth(), subMenuButton.getHeight());
		subMenu.setSize(this.getButton().getWidth() - 11,
				subMenu.getHeight() + subMenuButton.getHeight());
		subMenu.setBounds(11, this.getButton().getHeight() + 2,
				subMenu.getWidth(), subMenu.getHeight());
		maxHeight	=	this.getButton().getHeight() + 2 + subMenu.getHeight();
	}

	/**
	 * �����̍ő�l���擾����B
	 * @return �����̍ő�l
	 */
	public Integer getMaxHeight()
	{
		return maxHeight;
	}

	/**
	 * �T�u���j���[���J����Ă��邩���擾����B
	 * @return true - �T�u���j���[���J����Ă���
	 */
	public Boolean isSubMenuOpen()
	{
		return subMenuOpen;
	}
	
	/**
	 * �T�u���j���[���J���B
	 * @param diff �����̕ω���
	 */
	public void openSubMenu(int diff)
	{
		if(this.getHeight() + diff < this.getMaxHeight())
		{
			this.setSize(this.getWidth(),
					this.getHeight() + diff);
		}
		else
		{
			this.setSize(this.getWidth(),
					this.getMaxHeight());
			subMenuOpen	=	true;
		}
	}
	
	/**
	 * �T�u���j���[�����B
	 * @param diff �����̕ω���
	 */
	public void closeSubMenu(int diff)
	{
		if(this.getButton().getHeight() < this.getHeight() - diff)
		{
			this.setSize(this.getWidth(),
					this.getHeight() - diff);
		}
		else
		{
			this.setSize(this.getWidth(),
					this.getButton().getHeight());
			subMenuOpen	=	false;
		}
	}
}

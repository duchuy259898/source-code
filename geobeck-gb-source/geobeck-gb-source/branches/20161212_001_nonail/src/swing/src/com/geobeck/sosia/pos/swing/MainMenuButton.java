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
 * メインメニューのボタン用クラス
 * @author katagiri
 */
public class MainMenuButton extends ImagePanel
{
	
	/**
	 * ボタン
	 */
	private	JButton				button			=	null;
	/**
	 * サブメニュー
	 */
	private	JPanel				subMenu			=	null;
	
	private Color				subMenuColor	=	null;
	
	/**
	 * 高さの最大値
	 */
	private	Integer				maxHeight		=	0;
	/**
	 * サブメニューが開かれているか
	 */
	private	Boolean				subMenuOpen		=	false;
	
	/**
	 * コンストラクタ
	 * @param button ボタン
	 */
	public MainMenuButton(JButton button, Color subMenuColor)
	{
		super();
		this.setLayout(null);
		this.setOpaque(false);
		//ボタンを設定
		this.setButton(button);
		this.setSubMenuColor(subMenuColor);
		//サブメニューを初期化
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
	 * ボタンを取得する。
	 * @return ボタン
	 */
	public JButton getButton()
	{
		return button;
	}

	/**
	 * ボタンを設定する。
	 * @param button ボタン
	 */
	private void setButton(JButton button)
	{
		this.button = button;
		
		this.add(button);
		//自身のサイズをボタンのサイズに設定
		this.setSize(this.getButton().getWidth(),
				this.getButton().getHeight());
		//ボタンの位置とサイズを設定
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
	 * サブメニューにボタンを追加する。
	 * @param subMenuButton サブメニューのボタン
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
	 * 高さの最大値を取得する。
	 * @return 高さの最大値
	 */
	public Integer getMaxHeight()
	{
		return maxHeight;
	}

	/**
	 * サブメニューが開かれているかを取得する。
	 * @return true - サブメニューが開かれている
	 */
	public Boolean isSubMenuOpen()
	{
		return subMenuOpen;
	}
	
	/**
	 * サブメニューを開く。
	 * @param diff 高さの変化量
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
	 * サブメニューを閉じる。
	 * @param diff 高さの変化量
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

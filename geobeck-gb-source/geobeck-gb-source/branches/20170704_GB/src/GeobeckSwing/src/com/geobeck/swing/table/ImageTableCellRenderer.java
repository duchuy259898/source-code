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
 * 画像用TableCellRenderer
 * @author katagiri
 */
public class ImageTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * 画像をコンポーネントのサイズに合わせて表示するか
	 */
	protected boolean	adjust		=	false;
	
	/**
	 * 画像の縦横比を維持するか
	 */
	protected boolean	keepProportion	=	false;
	
	/**
	 * コンストラクタ
	 */
	public ImageTableCellRenderer()
	{
	}
	
	/**
	 * コンストラクタ
	 * @param adjust 画像をコンポーネントのサイズに合わせて表示するか
	 * @param keepProportion 画像の縦横比を維持するか
	 */
	public ImageTableCellRenderer(boolean adjust, boolean keepProportion)
	{
		this.setAdjust(adjust);
		this.setKeepProportion(keepProportion);
	}

	/**
	 * 画像をコンポーネントのサイズに合わせて表示するかを取得する
	 * @return 画像をコンポーネントのサイズに合わせて表示するか
	 */
	public boolean isAdjust()
	{
		return adjust;
	}

	/**
	 * 画像をコンポーネントのサイズに合わせて表示するかを設定する
	 * @param adjust 画像をコンポーネントのサイズに合わせて表示するか
	 */
	public void setAdjust(boolean adjust)
	{
		this.adjust = adjust;
	}

	/**
	 * 画像の縦横比を維持するかを取得する
	 * @return 画像の縦横比を維持するか
	 */
	public boolean isKeepProportion()
	{
		return keepProportion;
	}

	/**
	 * 画像の縦横比を維持するかを設定する
	 * @param keepProportion 画像の縦横比を維持するか
	 */
	public void setKeepProportion(boolean keepProportion)
	{
		this.keepProportion = keepProportion;
	}
	
	/**
	 * セルの値を設定する
	 * @param value セルの値
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
	 * コンポーネントを描画する
	 * （画像をコンポーネントのサイズに合わせて表示する場合、繰り返し表示の設定は無視される）
	 * @param g グラフィックオブジェクト
	 */
	public void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		
		//画像が設定されている場合
		if(this.getIcon() != null)
		{
			ImageIcon	icon	=	(ImageIcon)this.getIcon();
			
			//コンポーネントのサイズに合わせる場合
			if(adjust)
			{
				//縦横比を維持する場合
				if(keepProportion)
				{
					Double	hp	=	((Integer)getWidth()).doubleValue() / ((Integer)this.getIcon().getIconWidth()).doubleValue();
					Double	vp	=	((Integer)getHeight()).doubleValue() / ((Integer)this.getIcon().getIconHeight()).doubleValue();
					
					//横幅に合わせる場合
					if(hp <= vp)
					{
						g.drawImage(icon.getImage(), 0, 0,
								getWidth(),
								((Double)(((Integer)this.getIcon().getIconHeight()).doubleValue() * hp)).intValue(),
								this);
					}
					//高さに合わせる場合
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

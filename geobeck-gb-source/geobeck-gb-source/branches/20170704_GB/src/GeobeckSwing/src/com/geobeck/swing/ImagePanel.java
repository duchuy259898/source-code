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
 * 背景に画像を表示できるJPanel
 * @author katagiri
 */
public class ImagePanel extends JPanel
{
	/**
	 * 画像
	 */
	protected ImageIcon	image		=	new ImageIcon();
	
	/**
	 * 画像を繰り返し表示するか
	 */
	protected boolean	repeat		=	false;
	/**
	 * 画像をコンポーネントのサイズに合わせて表示するか
	 */
	protected boolean	adjust		=	false;
	
	protected boolean	keepProportion	=	false;
	
	/**
	 * コンストラクタ
	 */
	public ImagePanel()
	{
	}

	/**
	 * 画像を取得する
	 * @return 画像
	 */
	public ImageIcon getImage()
	{
		return image;
	}

	/**
	 * 画像を設定する
	 * @param image 画像
	 */
	public void setImage(ImageIcon image)
	{
		this.image	=	image;
	}

	/**
	 * 画像を繰り返し表示するかを取得する
	 * @return 画像を繰り返し表示するか
	 */
	public boolean isRepeat()
	{
		return repeat;
	}

	/**
	 * 画像を繰り返し表示するかを設定する
	 * @param repeat 画像を繰り返し表示するか
	 */
	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
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

	public boolean isKeepProportion()
	{
		return keepProportion;
	}

	public void setKeepProportion(boolean keepProportion)
	{
		this.keepProportion = keepProportion;
	}
	
	/**
	 * コンポーネントを描画する
	 * （画像をコンポーネントのサイズに合わせて表示する場合、繰り返し表示の設定は無視される）
	 * @param g グラフィックオブジェクト
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//画像が設定されている場合
		if(image != null)
		{
			//コンポーネントのサイズに合わせる場合
			if(adjust)
			{
				//画像の縦横比を維持する場合
				if(keepProportion)
				{
					Double	hp	=	((Integer)getWidth()).doubleValue() / ((Integer)image.getIconWidth()).doubleValue();
					Double	vp	=	((Integer)getHeight()).doubleValue() / ((Integer)image.getIconHeight()).doubleValue();
					
					//コンポーネントの横幅に合わせる場合
					if(hp <= vp)
					{
						g.drawImage(image.getImage(), 0, 0,
								getWidth(),
								((Double)(((Integer)image.getIconHeight()).doubleValue() * hp)).intValue(),
								this);
					}
					//コンポーネントの高さに合わせる場合
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
				//画像を繰り返し表示する場合
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

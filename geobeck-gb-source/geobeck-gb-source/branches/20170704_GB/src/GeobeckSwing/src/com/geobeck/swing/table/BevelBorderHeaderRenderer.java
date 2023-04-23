/*
 * BevelBorderHeaderRenderer.java
 *
 * Created on 2006/07/24, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * JTableのヘッダの色を変更できるRenderer
 * @author katagiri
 */
public class BevelBorderHeaderRenderer extends DefaultTableCellRenderer
{
	/**
	 * 背景色と、ハイライト部・シャドウ部の色の差
	 */
	private static final int DIFFERENCE_OF_COLOR	=	50;
	
	/**
	 * コンストラクタ
	 * @param baseColor 背景色
	 */
	public BevelBorderHeaderRenderer(Color baseColor)
	{
		super();
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBackground(baseColor);
		this.setBorder(new BevelBorder(BevelBorder.RAISED,
				this.createHighlightColor(),
				this.createShadowColor()));
	}
	
	/**
	 * コンストラクタ
	 * @param baseColor 背景色
	 * @param highlight ハイライト
	 * @param shadow シャドウ
	 */
	public BevelBorderHeaderRenderer(Color baseColor, Color highlight, Color shadow)
	{
		super();
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBackground(baseColor);
		this.setBorder(new BevelBorder(BevelBorder.RAISED,
				highlight, shadow));
	}
	
	/**
	 * 
	 * @param table 
	 * @param value 
	 * @param isSelected 
	 * @param hasFocus 
	 * @param row 
	 * @param column 
	 * @return 
	 */
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		this.setValue((value == null) ? "" : value.toString());
		return	this;
	}
	
	/**
	 * ハイライト色を作る
	 * @return 
	 */
	private Color createHighlightColor()
	{
		Color	baseColor	=	this.getBackground();
		return	new Color(
				this.reviseValue(baseColor.getRed() + BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
				this.reviseValue(baseColor.getGreen() + BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
				this.reviseValue(baseColor.getBlue() + BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR));
	}
	
	/**
	 * シャドウ色を作る
	 * @return 
	 */
	private Color createShadowColor()
	{
		Color	baseColor	=	this.getBackground();
		return	new Color(
				this.reviseValue(baseColor.getRed() - BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
				this.reviseValue(baseColor.getGreen() - BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
				this.reviseValue(baseColor.getBlue() - BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR));
	}
	
	/**
	 * 色を指定する数値が範囲外の場合補正する
	 * @param value 色を指定する数値
	 * @return 補正後の色を指定する数値
	 */
	private static int	reviseValue(int value)
	{
		if(value < 0)	return	0;
		else if(255 < value)	return	255;
		else	return	value;
	}
}

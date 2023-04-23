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
 * JTable�̃w�b�_�̐F��ύX�ł���Renderer
 * @author katagiri
 */
public class BevelBorderHeaderRenderer extends DefaultTableCellRenderer
{
	/**
	 * �w�i�F�ƁA�n�C���C�g���E�V���h�E���̐F�̍�
	 */
	private static final int DIFFERENCE_OF_COLOR	=	50;
	
	/**
	 * �R���X�g���N�^
	 * @param baseColor �w�i�F
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
	 * �R���X�g���N�^
	 * @param baseColor �w�i�F
	 * @param highlight �n�C���C�g
	 * @param shadow �V���h�E
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
	 * �n�C���C�g�F�����
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
	 * �V���h�E�F�����
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
	 * �F���w�肷�鐔�l���͈͊O�̏ꍇ�␳����
	 * @param value �F���w�肷�鐔�l
	 * @return �␳��̐F���w�肷�鐔�l
	 */
	private static int	reviseValue(int value)
	{
		if(value < 0)	return	0;
		else if(255 < value)	return	255;
		else	return	value;
	}
}

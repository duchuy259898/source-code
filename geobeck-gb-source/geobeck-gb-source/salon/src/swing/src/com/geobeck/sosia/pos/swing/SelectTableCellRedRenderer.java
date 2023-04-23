/*
 * SelectTableCellRenderer.java
 *
 * Created on 2007/02/17, 14:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.swing;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * 選択のみのテーブルのCellRenderer
 * @author katagiri
 */
public class SelectTableCellRedRenderer extends DefaultTableCellRenderer
{
	/**
	 * セルの端とテキストのマージン
	 */
	private static final	int		SIDE_MARGIN		=	4;
	
	private	Object		value			=	null;
	/**
	 * 選択されているかどうか
	 */
	private	boolean		isSelected		=	false;
	
	/**
	 * 選択時の色
	 */
	private Color		selectedRowColor	=	null;
	
	/**
	 * 影の色０
	 */
	private Color		shadow0Color		=	null;
	/**
	 * 影の色１
	 */
	private Color		shadow1Color		=	null;
	/**
	 * ハイライトの色
	 */
	private Color		highlightColor		=	null;
	
	/**
	 * コンストラクタ
	 */
	public SelectTableCellRedRenderer()
	{
		super();
		//setSelectedRowColor(new Color(236, 236, 236));
		setSelectedRowColor(new Color(255, 210, 142));
		setShadow0Color(new Color(113, 113, 113));
		setShadow1Color(new Color(172, 172, 172));
		setHighlightColor(new Color(241, 241, 241));
	}
	
	public String getText()
	{
		if(super.getText() == null)
		{
			return	"";
		}
		else
		{
			return	super.getText();
		}
	}
	

	/**
	 * 選択時の色を取得する。
	 * @return 選択時の色
	 */
	public Color getSelectedRowColor()
	{
		return selectedRowColor;
	}

	/**
	 * 選択時の色を設定する。
	 * @param selectedRowColor 選択時の色
	 */
	public void setSelectedRowColor(Color selectedRowColor)
	{
		this.selectedRowColor = selectedRowColor;
	}

	/**
	 * 影の色０を取得する。
	 * @return 影の色０
	 */
	public Color getShadow0Color()
	{
		return shadow0Color;
	}

	/**
	 * 影の色０を設定する。
	 * @param shadow0Color 影の色０
	 */
	public void setShadow0Color(Color shadow0Color)
	{
		this.shadow0Color = shadow0Color;
	}

	/**
	 * 影の色１を取得する。
	 * @return 影の色１
	 */
	public Color getShadow1Color()
	{
		return shadow1Color;
	}

	/**
	 * 影の色１を設定する。
	 * @param shadow1Color 影の色１
	 */
	public void setShadow1Color(Color shadow1Color)
	{
		this.shadow1Color = shadow1Color;
	}

	/**
	 * ハイライトの色を取得する。
	 * @return ハイライトの色
	 */
	public Color getHighlightColor()
	{
		return highlightColor;
	}

	/**
	 * ハイライトの色を設定する。
	 * @param highlightColor ハイライトの色
	 */
	public void setHighlightColor(Color highlightColor)
	{
		this.highlightColor = highlightColor;
	}

	/**
	 * テーブルセルレンダリングを返します。
	 * @param table JTable
	 * @param value セルに割り当てる値
	 * @param isSelected セルが選択されている場合は true
	 * @param hasFocus フォーカスがある場合は true
	 * @param row 行
	 * @param column 列
	 * @return テーブルセルレンダリング
	 */
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		
		this.value	=	value;
		this.isSelected	=	isSelected;
		super.setForeground((isSelected ? table.getSelectionForeground() : table.getForeground()));
		super.setBackground((isSelected ? table.getSelectionBackground() : table.getBackground()));
		return this;
	}
	
	/**
	 * 数値かどうかを設定する。
	 * @param value 判定する値
	 */
	private boolean isNumeric()
	{
		if(value instanceof Integer || 
				value instanceof Byte || 
				value instanceof Short || 
				value instanceof Long || 
				value instanceof Float || 
				value instanceof Double)
			return	true;
		else
			return	false;
	}
	
	/**
	 * 小数かどうかを設定する。
	 * @param value 判定する値
	 */
	private boolean isDecimal()
	{
		if(value instanceof Float || 
				value instanceof Double)
			return	true;
		else
			return	false;
	}
	
	/**
	 * 描画処理を行う。
	 * @param g Graphics
	 */
	public void paint(Graphics g)
	{
		//選択されている場合、凹んでいるように背景を描画する
		if(isSelected)
		{
			g.setColor(selectedRowColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(shadow0Color);
			g.drawLine(0, 0, 0, this.getHeight() - 1);
			g.drawLine(0, 0, this.getWidth() - 1, 0);
			g.setColor(shadow1Color);
			g.drawLine(1, 1, 1, this.getHeight() - 2);
			g.drawLine(1, 1, this.getWidth() - 2, 1);
			g.setColor(highlightColor);
			g.drawLine(this.getWidth() - 1, 1, this.getWidth() - 1, this.getHeight() - 1);
			g.drawLine(1, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1);
		}
		//選択されていない場合
		else
		{
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		g.setColor(Color.black);
		
		String	temp	=	this.getText();
		
		if(this.isNumeric())
		{
			if(this.isDecimal())
			{
				temp	=	String.format("%1$,.2f", value);
			}
			else
			{
				temp	=	String.format("%1$,d", value);
			}
		}
		
		int	baseX	=	0;
		Rectangle2D	r2d	=	this.getFont().getStringBounds(temp,
				new FontRenderContext(new AffineTransform(), true, false));
		
		switch(this.getHorizontalAlignment())
		{
			case SwingConstants.LEADING:
				if(this.isNumeric())
				{
					baseX	=	this.getWidth() - ((Double)r2d.getWidth()).intValue() - SIDE_MARGIN;
				}
				else if(isDateTime(this.getText()) || isPostalCode(this.getText()))
				{
					baseX	=	(this.getWidth() - ((Double)r2d.getWidth()).intValue()) / 2;
				}
				else
				{
					baseX	=	SIDE_MARGIN;
				}
				break;
			case SwingConstants.LEFT:
				baseX	=	SIDE_MARGIN;
				break;
			case SwingConstants.CENTER:
				baseX	=	(this.getWidth() - ((Double)r2d.getWidth()).intValue()) / 2;
				break;
			case SwingConstants.RIGHT:
				baseX	=	this.getWidth() - ((Double)r2d.getWidth()).intValue() - SIDE_MARGIN;
				break;
		}
		
		int	baseY	=	-1;
		
		switch(this.getVerticalAlignment())
		{
			case SwingConstants.TOP:
				baseY	+=	this.getFont().getSize();
				break;
			case SwingConstants.CENTER:
				baseY	+=	(this.getHeight() + this.getFont().getSize()) / 2;
				break;
			case SwingConstants.BOTTOM:
				baseY	+=	this.getHeight();
				break;
		}
		
		g.setColor(this.getForeground());
		
		g.drawString(temp, baseX + (isSelected ? 1 : 0), baseY + (isSelected ? 1 : 0));
	}
	
	private static boolean isDateTime(String value)
	{
		return	value.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}") ||
				value.matches("[0-9]{4}/[0-9]{2}") ||
				value.matches("[0-9]{2}:[0-9]{2}");
	}
	
	private static boolean isPostalCode(String value)
	{
		return	value.matches("[0-9]{3}-[0-9]{4}");
	}
	
	
	/**
	 * tableにSelectedTableCellRendererを設定します。
	 * @param table JTable
	 */
	public static void setSelectTableCellRenderer(JTable table)
	{
		//table.setForeground(Color.black);
		
		table.setDefaultRenderer(Byte.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(Short.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(Integer.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(Long.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(Float.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(Double.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(String.class, new SelectTableCellRenderer());
		table.setDefaultRenderer(Object.class, new SelectTableCellRenderer());
	}
}

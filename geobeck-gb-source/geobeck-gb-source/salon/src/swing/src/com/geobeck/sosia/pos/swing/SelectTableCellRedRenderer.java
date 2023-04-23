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
 * �I���݂̂̃e�[�u����CellRenderer
 * @author katagiri
 */
public class SelectTableCellRedRenderer extends DefaultTableCellRenderer
{
	/**
	 * �Z���̒[�ƃe�L�X�g�̃}�[�W��
	 */
	private static final	int		SIDE_MARGIN		=	4;
	
	private	Object		value			=	null;
	/**
	 * �I������Ă��邩�ǂ���
	 */
	private	boolean		isSelected		=	false;
	
	/**
	 * �I�����̐F
	 */
	private Color		selectedRowColor	=	null;
	
	/**
	 * �e�̐F�O
	 */
	private Color		shadow0Color		=	null;
	/**
	 * �e�̐F�P
	 */
	private Color		shadow1Color		=	null;
	/**
	 * �n�C���C�g�̐F
	 */
	private Color		highlightColor		=	null;
	
	/**
	 * �R���X�g���N�^
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
	 * �I�����̐F���擾����B
	 * @return �I�����̐F
	 */
	public Color getSelectedRowColor()
	{
		return selectedRowColor;
	}

	/**
	 * �I�����̐F��ݒ肷��B
	 * @param selectedRowColor �I�����̐F
	 */
	public void setSelectedRowColor(Color selectedRowColor)
	{
		this.selectedRowColor = selectedRowColor;
	}

	/**
	 * �e�̐F�O���擾����B
	 * @return �e�̐F�O
	 */
	public Color getShadow0Color()
	{
		return shadow0Color;
	}

	/**
	 * �e�̐F�O��ݒ肷��B
	 * @param shadow0Color �e�̐F�O
	 */
	public void setShadow0Color(Color shadow0Color)
	{
		this.shadow0Color = shadow0Color;
	}

	/**
	 * �e�̐F�P���擾����B
	 * @return �e�̐F�P
	 */
	public Color getShadow1Color()
	{
		return shadow1Color;
	}

	/**
	 * �e�̐F�P��ݒ肷��B
	 * @param shadow1Color �e�̐F�P
	 */
	public void setShadow1Color(Color shadow1Color)
	{
		this.shadow1Color = shadow1Color;
	}

	/**
	 * �n�C���C�g�̐F���擾����B
	 * @return �n�C���C�g�̐F
	 */
	public Color getHighlightColor()
	{
		return highlightColor;
	}

	/**
	 * �n�C���C�g�̐F��ݒ肷��B
	 * @param highlightColor �n�C���C�g�̐F
	 */
	public void setHighlightColor(Color highlightColor)
	{
		this.highlightColor = highlightColor;
	}

	/**
	 * �e�[�u���Z�������_�����O��Ԃ��܂��B
	 * @param table JTable
	 * @param value �Z���Ɋ��蓖�Ă�l
	 * @param isSelected �Z�����I������Ă���ꍇ�� true
	 * @param hasFocus �t�H�[�J�X������ꍇ�� true
	 * @param row �s
	 * @param column ��
	 * @return �e�[�u���Z�������_�����O
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
	 * ���l���ǂ�����ݒ肷��B
	 * @param value ���肷��l
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
	 * �������ǂ�����ݒ肷��B
	 * @param value ���肷��l
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
	 * �`�揈�����s���B
	 * @param g Graphics
	 */
	public void paint(Graphics g)
	{
		//�I������Ă���ꍇ�A����ł���悤�ɔw�i��`�悷��
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
		//�I������Ă��Ȃ��ꍇ
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
	 * table��SelectedTableCellRenderer��ݒ肵�܂��B
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

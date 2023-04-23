package com.geobeck.swing;
/*
 * JTableEx.java
 *
 * Created on 2006/05/08, 14:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import java.util.*;

/**
 * 各種コンポーネントもセルに配置できるJTable
 * @author katagiri
 */
public class JTableEx extends JTable
{
	/**
	 * JTable
	 */
	protected JTable				table;
	/**
	 * セルレンダラ
	 */
	protected TableCellRenderer		genericRenderer	=	new GenericCellRenderer();
	/**
	 * セルエディタ
	 */
	protected TableCellEditor		genericEditor	=	new GenericCellEditor();
	/**
	 * 行の高さを自動調節するか
	 */
	protected ArrayList<Integer>	rowHeightFix	=	new ArrayList<Integer>();
	
	
	/**
	 * コンストラクタ
	 */
	public JTableEx()
	{
		super();
		init();
	}
	
	
	/**
	 * コンストラクタ
	 * @param row 行数
	 * @param col 列数
	 */
	public JTableEx(int row, int col)
	{
		this(new DefaultTableModel(row, col));
	}
	
	
	/**
	 * コンストラクタ
	 * @param model モデル
	 */
	public JTableEx(TableModel model)
	{
		super(model);
		init();
	}

	void init()
	{
		setCellSelectionEnabled(true);
		table = this;
		addMouseMotionListener(new	MouseEventDispatchForRollover());
		addMouseListener(new	MouseEventDispatchForPopupMenu());
		setUI(new BasicTableUI());
	}

	/**
	 * 行の高さを自動調節する行をセットする。
	 * @param row 行
	 */
	public void setRowHeightFix(int row)
	{
		rowHeightFix.add(new Integer(row));
	}
	
	
	/**
	 * 行の高さを自動調節するかを取得する。
	 * @param row 行
	 * @return true - 行の高さを自動調節する
	 */
	public boolean isRowHeightFix(int row)
	{
		for(int i = 0 ; i < rowHeightFix.size() ; i ++)
		{
			Integer ii = (Integer)rowHeightFix.get(i);
			if(row == ii.intValue())	return(true);
		}
		
		return(false);
	}
	
	/**
	 * セルレンダラ
	 */
	protected class GenericCellRenderer extends DefaultTableCellRenderer 
	{
		/**
		 * セルレンダラのコンポーネントを取得する。
		 * @param table JTable
		 * @param value 値
		 * @param isSelected 選択されているか
		 * @param hasFocus フォーカスがあるか
		 * @param row 行
		 * @param col 列
		 * @return セルレンダラのコンポーネント
		 */
        @Override
		public Component getTableCellRendererComponent(JTable table, Object value,
								boolean isSelected, boolean hasFocus, int row, int col) 
		{
			if(value instanceof JComponent == false)
			{
				return(super.getTableCellRendererComponent(table, value,
								isSelected, hasFocus, row, col));
			}
			
			JComponent c = (JComponent)value;
			
			if(isRowHeightFix(row) == false)
			{
				//ローの高さが固定でない ならば、テーブルの各行の高さを調整する
				// テーブルの各行の高さを調整する
				Dimension d = c.getPreferredSize();
				
				if(table.getRowHeight(row) < d.height)
				{
					table.setRowHeight(row, d.height);
				}
			}
			
			return c;
		}
	}

	/**
	 * セルエディタ
	 */
	protected class GenericCellEditor extends AbstractCellEditor implements TableCellEditor 
	{
		/**
		 * コンポーネント
		 */
		protected JComponent c = null;

		/**
		 * コンポーネントを取得する。
		 * @return コンポーネント
		 */
		public Object getCellEditorValue() 
		{
			return c;
		}
		
		
		/**
		 * セルエディタのコンポーネントを取得する。
		 * @param table JTable
		 * @param value 値
		 * @param isSelected 選択されているか
		 * @param row 行
		 * @param col 列
		 * @return セルエディタのコンポーネント
		 */
		public Component getTableCellEditorComponent(JTable table, Object value,
						boolean isSelected, int row, int col) 
		{
			c = (JComponent)value;
			return(c);
		}
	}

	/**
	 * セルレンダラを取得する。
	 * @param row 行
	 * @param col 列
	 * @return セルレンダラ
	 */
    @Override
	public TableCellRenderer getCellRenderer(int row, int col) 
	{
		TableCellRenderer renderer;
		Object o = getValueAt(row, col);
		
		if(o instanceof JComponent)
		{
			renderer = genericRenderer;
		}
		else
		{
			renderer = super.getCellRenderer(row, col);
		}
		
		return renderer;
	}

	/**
	 * セルエディタを取得する。
	 * @param row 行
	 * @param col 列
	 * @return セルエディタ
	 */
    @Override
	public TableCellEditor getCellEditor(int row, int col) 
	{
		TableCellEditor editor;
		Object o = getValueAt(row, col);
		
		if(o instanceof JComponent)
		{
			editor = genericEditor;
		}
		else
		{
			editor = super.getCellEditor(row, col);
		}
		
		return editor;
	}

	class MouseEventDispatchForRollover extends MouseMotionAdapter 
	{
		Object prevV = null;
		
        @Override
		public void mouseMoved(MouseEvent e)
		{
			Point	p	=	e.getPoint();
			int		row	=	table.rowAtPoint(p);
			int		col	=	table.columnAtPoint(p);
			
			if(row<0 || col<0) return;
			Object v = table.getValueAt(row, col);
			
			if(v != prevV)
			{
				if(v instanceof AbstractButton)
				{
					sendMouseEvent((JComponent)v, MouseEvent.MOUSE_ENTERED, e);
				}
				
				if(prevV instanceof AbstractButton)
				{
					sendMouseEvent((JComponent)prevV, MouseEvent.MOUSE_EXITED, e);
				}
			}
			
			prevV = v;
		}
		
		
		void sendMouseEvent(JComponent c, int eventId, MouseEvent e)
		{
			Point p = SwingUtilities.convertPoint(table, e.getX(), e.getY(), c);
			MouseEvent e2 = new MouseEvent(c, eventId,
					e.getWhen(), e.getModifiers(), p.x, p.y,
					e.getClickCount(), e.isPopupTrigger());
			c.dispatchEvent(e2);
			table.repaint();
		}
	}

	class MouseEventDispatchForPopupMenu extends MouseAdapter 
	{
        @Override
		public void mouseReleased(MouseEvent e)
		{
			if(SwingUtilities.isRightMouseButton(e) == false) return;
			Point p = e.getPoint();
			int row = table.rowAtPoint(p);
			int col = table.columnAtPoint(p);
			if(row<0 || col<0) return;
			Object v = table.getValueAt(row, col);
			if(v instanceof JComponent == false) return;

			JComponent c = (JComponent)v;
			JPopupMenu popup = c.getComponentPopupMenu();
			
			if(popup != null)
			{
				popup.show(table, p.x, p.y);
			}
		}
	}

	/**
	 * L&Fまたはセルのデータが変更されたという UIManager からの通知
	 */
	protected void updateUIorCells()
	{
		int numColumns = this.getColumnCount();
		int numRows = this.getRowCount();
		
		for( int col = 0 ; col < numColumns ; col ++ )
		{
			for( int row = 0 ; row < numRows; row ++ )
			{
				Object o = getValueAt(row, col);
				
				if(o instanceof JComponent)
				{
					JComponent component = (JComponent)o;
					component.updateUI();
				}
			}
		}
	}
	
	/**
	 * L&Fが変更されたというUIManagerからの通知
	 */
    @Override
	public void updateUI()
	{
		updateUIorCells();
		repaint();
	}
}

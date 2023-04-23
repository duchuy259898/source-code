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
 * �e��R���|�[�l���g���Z���ɔz�u�ł���JTable
 * @author katagiri
 */
public class JTableEx extends JTable
{
	/**
	 * JTable
	 */
	protected JTable				table;
	/**
	 * �Z�������_��
	 */
	protected TableCellRenderer		genericRenderer	=	new GenericCellRenderer();
	/**
	 * �Z���G�f�B�^
	 */
	protected TableCellEditor		genericEditor	=	new GenericCellEditor();
	/**
	 * �s�̍������������߂��邩
	 */
	protected ArrayList<Integer>	rowHeightFix	=	new ArrayList<Integer>();
	
	
	/**
	 * �R���X�g���N�^
	 */
	public JTableEx()
	{
		super();
		init();
	}
	
	
	/**
	 * �R���X�g���N�^
	 * @param row �s��
	 * @param col ��
	 */
	public JTableEx(int row, int col)
	{
		this(new DefaultTableModel(row, col));
	}
	
	
	/**
	 * �R���X�g���N�^
	 * @param model ���f��
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
	 * �s�̍������������߂���s���Z�b�g����B
	 * @param row �s
	 */
	public void setRowHeightFix(int row)
	{
		rowHeightFix.add(new Integer(row));
	}
	
	
	/**
	 * �s�̍������������߂��邩���擾����B
	 * @param row �s
	 * @return true - �s�̍������������߂���
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
	 * �Z�������_��
	 */
	protected class GenericCellRenderer extends DefaultTableCellRenderer 
	{
		/**
		 * �Z�������_���̃R���|�[�l���g���擾����B
		 * @param table JTable
		 * @param value �l
		 * @param isSelected �I������Ă��邩
		 * @param hasFocus �t�H�[�J�X�����邩
		 * @param row �s
		 * @param col ��
		 * @return �Z�������_���̃R���|�[�l���g
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
				//���[�̍������Œ�łȂ� �Ȃ�΁A�e�[�u���̊e�s�̍����𒲐�����
				// �e�[�u���̊e�s�̍����𒲐�����
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
	 * �Z���G�f�B�^
	 */
	protected class GenericCellEditor extends AbstractCellEditor implements TableCellEditor 
	{
		/**
		 * �R���|�[�l���g
		 */
		protected JComponent c = null;

		/**
		 * �R���|�[�l���g���擾����B
		 * @return �R���|�[�l���g
		 */
		public Object getCellEditorValue() 
		{
			return c;
		}
		
		
		/**
		 * �Z���G�f�B�^�̃R���|�[�l���g���擾����B
		 * @param table JTable
		 * @param value �l
		 * @param isSelected �I������Ă��邩
		 * @param row �s
		 * @param col ��
		 * @return �Z���G�f�B�^�̃R���|�[�l���g
		 */
		public Component getTableCellEditorComponent(JTable table, Object value,
						boolean isSelected, int row, int col) 
		{
			c = (JComponent)value;
			return(c);
		}
	}

	/**
	 * �Z�������_�����擾����B
	 * @param row �s
	 * @param col ��
	 * @return �Z�������_��
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
	 * �Z���G�f�B�^���擾����B
	 * @param row �s
	 * @param col ��
	 * @return �Z���G�f�B�^
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
	 * L&F�܂��̓Z���̃f�[�^���ύX���ꂽ�Ƃ��� UIManager ����̒ʒm
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
	 * L&F���ύX���ꂽ�Ƃ���UIManager����̒ʒm
	 */
    @Override
	public void updateUI()
	{
		updateUIorCells();
		repaint();
	}
}

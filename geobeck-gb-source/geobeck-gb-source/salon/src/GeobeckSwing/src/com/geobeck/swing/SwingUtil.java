/*
 * SwingUtil.java
 *
 * Created on 2006/05/26, 15:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 * Swing�p���[�e�B���e�B
 * @author katagiri
 */
public class SwingUtil
{
	public final static int ANCHOR_HCENTER		= 0x0001;	// �e�L�X�g�ƃC���[�W�̃A���J�[�|�C���g�ɐ����ɃZ���^�����O���邱�Ƃ�����
	public final static int ANCHOR_VCENTER		= 0x0002;	// �C���[�W�̃A���J�[�|�C���g�ɑ΂��Đ����ɃZ���^�����O���邱�Ƃ�����
	public final static int ANCHOR_LEFT			= 0x0004;	// �e�L�X�g�ƃC���[�W�̃A���J�[�|�C���g�����Ƃ��邱�Ƃ�����
	public final static int ANCHOR_RIGHT		= 0x0008;	// �e�L�X�g�ƃC���[�W�̃A���J�[�|�C���g���E�Ƃ��邱�Ƃ�����
	public final static int ANCHOR_TOP			= 0x0010;	// �e�L�X�g�ƃC���[�W�̃A���J�[�|�C���g���g�b�v�i��j�Ƃ��邱�Ƃ�����
	public final static int ANCHOR_BOTTOM		= 0x0020;	// �e�L�X�g�ƃC���[�W�̃A���J�[�|�C���g���{�g���i���j�Ƃ��邱�Ƃ�����
	
	/**
	 * �R���X�g���N�^
	 */
	public SwingUtil()
	{
	}
	
	
	/**
	 * JFrame����ʂ̒����ֈړ�����B
	 * @param frame JFrame
	 */
	public static void moveCenter(JFrame frame)
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = frame.getSize();
		frame.setLocation((screen.width - size.width) / 2,
				(screen.height - size.height) / 2);
	}
	
	
	/**
	 * JDialog����ʂ̒����ֈړ�����B
	 * @param dialog JDialog
	 */
	public static void moveCenter(JDialog dialog)
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = dialog.getSize();
		dialog.setLocation((screen.width - size.width) / 2,
				(screen.height - size.height) / 2);
	}
	
	/**
	 * JDialog����ʎw��ʒu�ֈړ�����B
	 * @param dialog JDialog
	 */
	public static void moveAnchor( JDialog dialog, int anchor )
	{
		int posX = 0;
		int posY = 0;
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = dialog.getSize();
		
		if( ( anchor & ANCHOR_HCENTER ) != 0 ) posX = (screen.width - size.width) / 2;			// �Z���^�����O
		if( ( anchor & ANCHOR_RIGHT   ) != 0 ) posX = (screen.width - size.width);				// �E��
		
		if( ( anchor & ANCHOR_VCENTER ) != 0 ) posY = (screen.height - size.height) / 2;			// �Z���^�����O
		if( ( anchor & ANCHOR_BOTTOM  ) != 0 ) posY = (screen.height - size.height);				// �E��
		
		dialog.setLocation( posX, posY );
	}
	
	/**
	 * JTextField�̕������I����Ԃɂ���B
	 * @param tf JTextField
	 */
	public static void selectAllText(JTextField tf)
	{
		tf.setSelectionStart(0);
		tf.setSelectionEnd(tf.getText().length());
	}
	
	/**
	 * JFormattedTextField�̕������I����Ԃɂ���B
	 * @param tf JFormattedTextField
	 */
	public static void selectAllText(JFormattedTextField tf)
	{
		tf.setSelectionStart(0);
		tf.setSelectionEnd(tf.getText().length());
	}
	
	
	/**
	 * �e�[�u�����N���A����B
	 * @param table JTable
	 */
	public static void clearTable(JTable table)
	{
		DefaultTableModel	model	=	(DefaultTableModel)table.getModel();
		
		//�S�s�폜
		model.setRowCount(0);
		table.removeAll();
	}
	
	
	public static void setJTableHeaderRenderer(JTable table,
			TableCellRenderer renderer)
	{
		TableColumnModel model = table.getColumnModel();
		
		for(int i = 0; i < model.getColumnCount(); i ++)
		{
			TableColumn	column	=	model.getColumn(i);
			column.setHeaderRenderer(renderer);
		}
	}
	
	public static void expandJTree(JTree tree)
	{
		int		row	=	0;
		
		while(row < tree.getRowCount())
		{
			tree.expandRow(row);
			row ++;
		}
	}
	
	public static void openDialog(Dialog owner, boolean modal, JPanel panel, String title)
	{
		JDialog		dialog	=	new JDialog(owner, modal);
		
		dialog.setTitle(title);
		setPanelToDialog(dialog, panel);
		
		dialog.setVisible(true);
	}
	
	public static void openDialog(Frame owner, boolean modal, JPanel panel, String title)
	{
		JDialog		dialog	=	new JDialog(owner, modal);
		
		dialog.setTitle(title);
		setPanelToDialog(dialog, panel);
		
		dialog.setVisible(true);
	}
	
	public static void openAnchorDialog(Frame owner, boolean modal, JPanel panel, String title, int anchor )
	{
		JDialog dialog	=	new JDialog(owner, modal);
		
		dialog.setTitle(title);
		setPanelToDialog(dialog, panel);
		moveAnchor( dialog, anchor );
		dialog.setVisible(true);
	}
	
	private static void setPanelToDialog(JDialog dialog, JPanel panel)
	{
		dialog.setSize(panel.getWidth() + 4, panel.getHeight() + 32);
		
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
	}
	
	public static Integer getJTextFieldIntValue(JTextField text)
	{
		if(text.getText().equals(""))
		{
			return	0;
		}
		
		return	Integer.parseInt(text.getText().replace(",", ""));
	}
	
	public static Long getJTextFieldLongValue(JTextField text)
	{
		if(text.getText().equals(""))
		{
			return	0l;
		}
		
		return	Long.parseLong(text.getText().replace(",", ""));
	}
	
	public static Double getJTextFieldDoubleValue(JTextField text)
	{
		if(text.getText().equals(""))
		{
			return	0d;
		}
		
		return	Double.parseDouble(text.getText().replace(",", ""));
	}
}

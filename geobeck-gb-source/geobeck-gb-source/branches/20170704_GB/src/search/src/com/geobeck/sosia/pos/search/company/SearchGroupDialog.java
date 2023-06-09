/*
 * SearchGroupDialog.java
 *
 * Created on 2006/08/03, 18:58
 */

package com.geobeck.sosia.pos.search.company;

import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.swing.company.GroupTreeCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import javax.swing.*;
import javax.swing.tree.*;

import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.swing.*;

/**
 *
 * @author  katagiri
 */
public class SearchGroupDialog extends javax.swing.JDialog
{
	
	/**
	 * Creates new form SearchGroupDialog
	 */
	public SearchGroupDialog(java.awt.Frame parent, boolean modal)
	{
		super(parent, modal);
		initComponents();
		addMouseCursorChange();
	}
	
	public SearchGroupDialog(java.awt.Frame parent, boolean modal, Integer removeID)
	{
		super(parent, modal);
		initComponents();
		addMouseCursorChange();
		this.removeNode(removeID);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        backPanel = new com.geobeck.swing.ImagePanel();
        groupScrollPane = new javax.swing.JScrollPane();
        groupTree = new JTree(SystemInfo.getGroup().createTreeNode(false));
        selectButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
        groupScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        groupTree.setCellRenderer(new GroupTreeCellRenderer());
        groupTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        SwingUtil.expandJTree(groupTree);
        groupScrollPane.setViewportView(groupTree);

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setFocusable(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                selectButtonActionPerformed(evt);
            }
        });

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                backButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, backPanelLayout.createSequentialGroup()
                        .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(groupScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(groupScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void backButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backButtonActionPerformed
	{//GEN-HEADEREND:event_backButtonActionPerformed
		this.setVisible(false);
	}//GEN-LAST:event_backButtonActionPerformed

	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
	{//GEN-HEADEREND:event_selectButtonActionPerformed
		this.setSelectedData();
		this.setVisible(false);
	}//GEN-LAST:event_selectButtonActionPerformed
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JScrollPane groupScrollPane;
    private javax.swing.JTree groupTree;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
	
	private	MstGroup	selectedData	=	null;
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(selectButton);
		SystemInfo.addMouseCursorChange(backButton);
	}
	
	private void removeNode(Integer removeID)
	{
		for(int row = 0; row < groupTree.getRowCount(); row ++)
		{
			DefaultMutableTreeNode	node	=	(DefaultMutableTreeNode)groupTree.getPathForRow(row).getLastPathComponent();
			
			if(node != null)
			{
				MstGroup	mg	=	(MstGroup)node.getUserObject();
				
				if(mg.getGroupID() == removeID)
				{
					DefaultTreeModel	model	=	(DefaultTreeModel)groupTree.getModel();
					model.removeNodeFromParent(node);
					//groupTree.removeSelectionRows(new int[]{row});
					return;
				}
			}
		}
	}
	
	private void setSelectedData()
	{
		DefaultMutableTreeNode	node	=	(DefaultMutableTreeNode)groupTree.getLastSelectedPathComponent();
		
		if(node != null)
		{
			selectedData	=	(MstGroup)node.getUserObject();
		}
		else
		{
			selectedData	=	new MstGroup();
		}
	}
	
	public MstGroup getSelectedData()
	{
		return	selectedData;
	}
}

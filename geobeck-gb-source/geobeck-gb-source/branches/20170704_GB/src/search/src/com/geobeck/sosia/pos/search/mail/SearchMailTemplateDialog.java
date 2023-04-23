/*
 * SearchMailTemplateDialog.java
 *
 * Created on 2006/10/31, 19:47
 */

package com.geobeck.sosia.pos.search.mail;

import java.util.*;
import java.util.logging.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.master.mail.*;

/**
 *
 * @author  katagiri
 */
public class SearchMailTemplateDialog extends javax.swing.JDialog
{
	
	/** Creates new form SearchMailTemplateDialog */
	public SearchMailTemplateDialog(java.awt.Frame parent, boolean modal,
			Integer groupID, Integer shopID)
	{
		super(parent, modal);
		this.setTitle("�e���v���[�g����");
		initComponents();
		this.init(groupID, shopID);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        backPanel = new com.geobeck.swing.ImagePanel();
        templateClassLabel = new javax.swing.JLabel();
        templateClass = new javax.swing.JComboBox();
        templateList1 = new com.geobeck.swing.JTableEx();
        templateTitleLabel = new javax.swing.JLabel();
        templateTitle = new javax.swing.JFormattedTextField();
        templateBodyLabel = new javax.swing.JLabel();
        templateBodyScrollPane = new javax.swing.JScrollPane();
        templateBody = new javax.swing.JTextArea();
        selectButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        templateListScrollPane1 = new javax.swing.JScrollPane();
        templateList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
        templateClassLabel.setText("\u5206\u985e");

        templateClass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        templateClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateClassActionPerformed(evt);
            }
        });

        templateList1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "�^�C�g��", "�\����"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        templateTitleLabel.setText("\u30bf\u30a4\u30c8\u30eb");

        templateTitle.setBackground(new java.awt.Color(255, 255, 255));
        templateTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        templateTitle.setEditable(false);

        templateBodyLabel.setText("\u672c\u6587");

        templateBodyScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        templateBody.setColumns(20);
        templateBody.setEditable(false);
        templateBody.setRows(5);
        templateBodyScrollPane.setViewportView(templateBody);

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setFocusable(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        templateListScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        templateList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "�^�C�g��"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        templateList.setSelectionBackground(new java.awt.Color(220, 220, 220));
        templateList.setSelectionForeground(new java.awt.Color(0, 0, 0));
        templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        templateList.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(templateList, SystemInfo.getTableHeaderRenderer());
        templateList.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(templateList);
        templateList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                templateListKeyReleased(evt);
            }
        });
        templateList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                templateListMouseReleased(evt);
            }
        });

        templateListScrollPane1.setViewportView(templateList);

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(templateList1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(backPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(templateBodyLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(templateBodyScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                    .add(backPanelLayout.createSequentialGroup()
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(backPanelLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(templateClassLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(templateTitleLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(templateTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, templateListScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                            .add(backPanelLayout.createSequentialGroup()
                                .add(templateClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 266, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(templateList1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(templateClassLabel)
                    .add(templateClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(templateListScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(templateTitleLabel)
                    .add(templateTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(templateBodyLabel)
                    .add(templateBodyScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void templateClassActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_templateClassActionPerformed
	{//GEN-HEADEREND:event_templateClassActionPerformed
		this.changeMailTemplateTable();
	}//GEN-LAST:event_templateClassActionPerformed

	private void backButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backButtonActionPerformed
	{//GEN-HEADEREND:event_backButtonActionPerformed
		this.setVisible(false);
	}//GEN-LAST:event_backButtonActionPerformed

	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
	{//GEN-HEADEREND:event_selectButtonActionPerformed
		this.setSelectedTemplate();
		this.setVisible(false);
	}//GEN-LAST:event_selectButtonActionPerformed

	private void templateListMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_templateListMouseReleased
	{//GEN-HEADEREND:event_templateListMouseReleased
		this.changeCurrentData();
	}//GEN-LAST:event_templateListMouseReleased

	private void templateListKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_templateListKeyReleased
	{//GEN-HEADEREND:event_templateListKeyReleased
		this.changeCurrentData();
	}//GEN-LAST:event_templateListKeyReleased
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JButton selectButton;
    private javax.swing.JTextArea templateBody;
    private javax.swing.JLabel templateBodyLabel;
    private javax.swing.JScrollPane templateBodyScrollPane;
    private javax.swing.JComboBox templateClass;
    private javax.swing.JLabel templateClassLabel;
    private javax.swing.JTable templateList;
    private com.geobeck.swing.JTableEx templateList1;
    private javax.swing.JScrollPane templateListScrollPane1;
    private javax.swing.JFormattedTextField templateTitle;
    private javax.swing.JLabel templateTitleLabel;
    // End of variables declaration//GEN-END:variables
	
	private	MstMailTemplateClasses		mmtcs			=	new MstMailTemplateClasses();
	private	MstMailTemplates			mmts			=	new MstMailTemplates();
	private	Integer						currentIndex	=	-1;
	
	private MstMailTemplate				selectedTemplate	=	null;
	
	private void setSelectedTemplate()
	{
		if(0 <= currentIndex)
		{
			selectedTemplate	=	mmts.get(currentIndex);
		}
	}
	
	public MstMailTemplate getSelectedTemplate()
	{
		return	selectedTemplate;
	}
	
	private void init(Integer groupID, Integer shopID)
	{
		mmtcs.setGroupID(groupID);
		mmtcs.setShopID(shopID);
		mmts.setGroupID(groupID);
		mmts.setShopID(shopID);
		this.initClass();
		this.changeMailTemplateTable();
	}
	
	private void clear()
	{
		currentIndex = -1;
		templateTitle.setText("");
		templateBody.setText("");
		selectedTemplate	=	null;
	}
	
	private void initClass()
	{
		try
		{
			mmtcs.load(SystemInfo.getConnection(), false);
			
			for(MstMailTemplateClass mmtc : mmtcs)
			{
				templateClass.addItem(mmtc);
			}
			
			if(0 < templateClass.getItemCount())
			{
				templateClass.setSelectedIndex(0);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �I���f�[�^���ύX���ꂽ�Ƃ��̏����B
	 */
	private void changeCurrentData()
	{
		int	index	=	templateList.getSelectedRow();
		
		if(0 <= index && index < mmts.size() && currentIndex != index)
		{
			currentIndex	=	index;
			//�I������Ă���f�[�^��\��
			this.showCurrentData();
		}
		
		//�f�[�^���I������Ă���ꍇ�A�X�V�E�폜�{�^�����g�p�\�ɂ���
		selectButton.setEnabled(0 <= currentIndex);
	}
	
	private void changeMailTemplateTable()
	{
		this.clear();
		
		try
		{
			if(0 <= templateClass.getSelectedIndex())
			{
				mmts.setMailTemplateClass(
						(MstMailTemplateClass)templateClass.getSelectedItem());
				mmts.load(SystemInfo.getConnection(), false);
				
				SwingUtil.clearTable(templateList);
				
				DefaultTableModel	model	=	(DefaultTableModel)templateList.getModel();
				
				for(MstMailTemplate mmt : mmts)
				{
					Object[]	rowData	=	{	mmt.getMailTemplateTitle(),
												mmt.getDisplaySeq()};
					model.addRow(rowData);
				}
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void showCurrentData()
	{
		MstMailTemplate		temp	=	mmts.get(currentIndex);
		
		templateTitle.setText(temp.getMailTemplateTitle());
		templateBody.setText(temp.getMailTemplateBody());
	}
}
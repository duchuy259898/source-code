/*
 * WildMailGearPanel.java
 *
 * Created on 2007/10/22, 14:53
 */

package com.geobeck.sosia.pos.mail;

import	java.awt.*;
import	java.net.*;
import	java.util.*;
import	javax.swing.table.*;
import	javax.swing.*;
import	com.geobeck.swing.*;
import	com.geobeck.util.*;
import	com.geobeck.sosia.pos.master.customer.*;
import	com.geobeck.sosia.pos.system.*;
import	com.geobeck.sosia.pos.util.*;

/**
 *
 * @author  kanemoto
 */
public class WildMailGearPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
	private		String						SEND_MAIL_URL = "S/mail/message/edit.html";
	private		ArrayList<MstCustomer>		customers	=	new ArrayList<MstCustomer>();
	private		boolean[]					sendFlag	=	null;
	
	/** Creates new form WildMailGearPanel */
	public WildMailGearPanel( ArrayList<MstCustomer> customers ) {
		initComponents();
		this.setSize(400, 691);
		this.setPath("���[���@�\ >> ��������");
		this.setTitle("���C���h���[���쐬");
		this.setCustomer( customers );
		this.initCustomersTable();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        backButton = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        customersScroolPane = new javax.swing.JScrollPane();
        customersTable = new com.geobeck.swing.JTableEx();

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/back_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/back_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        sendButton.setIcon(SystemInfo.getImageIcon("/button/mail/send_mail_off.jpg"));
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/send_mail_on.jpg"));
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        customersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "�A����", "�o��", "����"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customersTable.setColumnSelectionAllowed(false);
        customersTable.setRowSelectionAllowed(false);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customersTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(customersTable, SystemInfo.getTableHeaderRenderer());
        customersTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        customersTable.setDefaultRenderer(String.class, new TableCellAlignRenderer());

        customersScroolPane.setViewportView(customersTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customersScroolPane, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customersScroolPane, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
		this.sendMail();
	}//GEN-LAST:event_sendButtonActionPerformed

	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
		this.showOpener();
	}//GEN-LAST:event_backButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JScrollPane customersScroolPane;
    private com.geobeck.swing.JTableEx customersTable;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * �ڋq���X�g��o�^����
	 */
	private void setCustomer( ArrayList<MstCustomer> customers )
	{
		this.customers.clear();
		for( MstCustomer mc : customers )
		{
			this.customers.add( mc );
		}
		// ���M�t���O���쐬����
		sendFlag = new boolean[ this.customers.size() ];
		for( int i = 0; i < sendFlag.length; i++ ) {
			// �ڋq��SOSIA�A�����Ă���ꍇ�ɂ͏����o�̓t���O�Ƀ`�F�b�N������
			sendFlag[ i ] = ( customers.get( i ).getSosiaCustomer().isSosiaCustomer() );
		}
	}
	
	private JCheckBox getCheckBox( MstCustomer mc, boolean setFlag )
	{
		JCheckBox retBox = new JCheckBox();
		
		retBox.setOpaque( false );
		retBox.setSelected( setFlag );
		retBox.setHorizontalAlignment( JCheckBox.CENTER );
		
		// SOSIA��A�����[�U�Ȃ疳���ɂ���
		retBox.setEnabled( mc.getSosiaCustomer().isSosiaCustomer() );
		
        retBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                changeSelectedCustomer();
            }
        });
		
		
		return retBox;
	}
	
	private void initCustomersTable()
	{
		SwingUtil.clearTable(customersTable);
		
		DefaultTableModel	model	=	(DefaultTableModel)customersTable.getModel();
		
		for( int i = 0; i < customers.size(); i++ ) {
			MstCustomer mc = customers.get( i );
			model.addRow(
				new Object[] {
					( mc.getSosiaCustomer().isSosiaCustomer() ? "" : "���A��" ),
					getCheckBox( mc, sendFlag[ i ] ),
					mc.toString()				}
			);
		}
	}
	
	/**
	 * �I�������ڋq�̃��[�����M�`�F�b�N��؂�ւ���
	 */
	private void changeSelectedCustomer()
	{
		int selectedIndex = customersTable.getSelectedRow();
		
		if(0 <= customersTable.getSelectedRow())
		{
			sendFlag[ selectedIndex ] = ( (JCheckBox)customersTable.getValueAt( selectedIndex, 1 ) ).isSelected();
		}
	}
	
	private void sendMail()
	{
		String ids = getSendCustomerSosiaID();
		
		if( ids.compareTo( "" ) == 0 ) {
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(8000),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// �N���C�A���gPC����HTML���쐬����
		makeHTMLFile();
		// �f�t�H���g�u���E�U����WEB�u���E�U���N������
		InvokeBrowser();
	}
	
	
	
	
	private void makeHTMLFile() {
		FileControl fc = new FileControl();
		fc.writeFile( "./tempWildMail.html", getInvocationBrowserHTML().getBytes() );
		
		String[] files = fc.getFileList( "./" );
		for( String s : files ) {
			System.out.println( "file:" + s );
		}
	}
	
	private String getInvocationBrowserHTML() {
		return
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n" +
			"<HTML LANG=\"ja\">\n" +
			"<HEAD>\n" +
			"<META HTTP-EQUIV=\"content-type\" CONTENT=\"text/html; charset=shift_jis\">\n" +
			"<META HTTP-EQUIV=\"content-script-type\" CONTENT=\"text/javascript\">\n" +
			"<TITLE>���C���h�J�[�h���[��</TITLE>\n" +
			"</HEAD>\n" +
			"<BODY ONLOAD=\"document.AutoLogOn.submit()\">\n" +
			"<FORM ACTION=\"" + ( SystemInfo.getSosiaGearBaseURL() + SEND_MAIL_URL ) + "\" METHOD=\"POST\" NAME=\"AutoLogOn\">\n" +
			"<DIV>\n" +
			"<INPUT TYPE=\"HIDDEN\" NAME=\"username\" VALUE=\"" + SystemInfo.getSosiaGearID() + "\">\n" +
			"<INPUT TYPE=\"HIDDEN\" NAME=\"password\" VALUE=\"" + SystemInfo.getSosiaGearPassWord() + "\">\n" +
			"<INPUT TYPE=\"HIDDEN\" NAME=\"ptype\"    VALUE=\"fs\">\n" +
			"<INPUT TYPE=\"HIDDEN\" NAME=\"ids\"      VALUE=\"" + getSendCustomerSosiaID() + "\">\n" +
			"</DIV>\n" +
			"</FORM>\n" +
			"</BODY>\n" +
			"</HTML>\n";
	}
	
	private void InvokeBrowser() {
		// �T�|�[�g�ΏۊO
		if( !Desktop.isDesktopSupported() )
		{
			/*
			 * �f�t�H���g�u���E�U���ݒ肳��Ă��܂���
			 */
		    MessageDialog.showMessageDialog(
				this,
				MessageUtil.getMessage( 1400 ),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		try
		{
			DesktopUtil.runWebBrowser( new URI( "./tempWildMail.html" ) );
		}
		catch(Exception e)
		{
			System.out.println( "runWebBrowser Exception :: " + e );
		}
	}
	
	private String getSendCustomerSosiaID() {
		String retStr = "";
		boolean fCheck = true;
		
		for( int i = 0; i < customers.size(); i++ ) {
			if( sendFlag[ i ] ) {
				if( !fCheck ) retStr += ",";
				retStr += customers.get( i ).getSosiaCustomer().getSosiaID().toString();
				fCheck = false;
			}
		}
		return retStr;
	}
	
	/**
	 * ��̕���ݒ肷��B
	 */
	private void initTableColumnWidth() {
		customersTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		customersTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		customersTable.getColumnModel().getColumn(2).setPreferredWidth(200);
	}
	
	/**
	 * ��̕\���ʒu��ݒ肷��TableCellRenderer
	 */
	private class TableCellAlignRenderer extends DefaultTableCellRenderer
	{
		/** Creates a new instance of ReservationTableCellRenderer */
		public TableCellAlignRenderer()
		{
			super();
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
			
			switch(column)
			{
				case 0:
					super.setHorizontalAlignment(SwingConstants.CENTER);
					break;
				case 2:
					super.setHorizontalAlignment(SwingConstants.LEFT);
					break;
				default:
					super.setHorizontalAlignment(SwingConstants.CENTER);
					break;
			}
			
			//�����̐F��ύX
			if( column == 0 ) super.setForeground( new Color( 255, 0, 0 ) );
			else             super.setForeground( new Color( 0, 0, 0 ) );

			return this;
		}
	}
}
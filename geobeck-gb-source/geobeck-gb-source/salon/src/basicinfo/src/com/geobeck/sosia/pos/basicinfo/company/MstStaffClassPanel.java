/*
 * MstStaffClassPanel.java
 *
 * Created on 2006/10/20, 10:52
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstStaffClass;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author  katagiri
 */
public class MstStaffClassPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	private MstStaffClass msc = new MstStaffClass();
	private ArrayList<MstStaffClass> list;
	private Integer selIndex = -1;

	//IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
	private boolean isLoadDisplay= false;
	//IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
	
	/** Creates new form MstStaffClassPanel */
	public MstStaffClassPanel()
	{
		super();
		initComponents();
		addMouseCursorChange();
		this.setSize(450, 680);
		this.setPath("基本設定 >> 会社関連");
		this.setTitle("スタッフ区分登録");
		this.setListener();
		this.init();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        displayReservationGroup = new javax.swing.ButtonGroup();
        staffClassNameLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        displaySeq = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)displaySeq.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        renewButton = new javax.swing.JButton();
        staffClassesScrollPane = new javax.swing.JScrollPane();
        staffClasses = new javax.swing.JTable();
        displaySeqLabel = new javax.swing.JLabel();
        staffClassName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)staffClassName.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        deleteButton = new javax.swing.JButton();
        displayReservationLabel = new javax.swing.JLabel();
        rdoDisplayReservationEnable = new javax.swing.JRadioButton();
        rdoDisplayReservationDisable = new javax.swing.JRadioButton();
        staffClassContractedNameLabel = new javax.swing.JLabel();
        staffClassContractedName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)staffClassContractedName.getDocument()).setDocumentFilter(
            new CustomFilter(10));

        setFocusCycleRoot(true);
        addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                formHierarchyChanged(evt);
            }
        });

        staffClassNameLabel.setText("スタッフ区分名");

        addButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        displaySeq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        displaySeq.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setContentAreaFilled(false);
        renewButton.setEnabled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        staffClassesScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffClasses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "スタッフ区分名", "区分略称", "予約表表示", "表示順"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        staffClasses.setSelectionBackground(new java.awt.Color(220, 220, 220));
        staffClasses.setSelectionForeground(new java.awt.Color(0, 0, 0));
        staffClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffClasses.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(staffClasses, SystemInfo.getTableHeaderRenderer());
        staffClasses.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(staffClasses);
        staffClasses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                staffClassesMouseReleased(evt);
            }
        });
        staffClasses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                staffClassesKeyReleased(evt);
            }
        });

        staffClassesScrollPane.setViewportView(staffClasses);

        displaySeqLabel.setText("挿入位置");

        staffClassName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffClassName.setColumns(20);
        staffClassName.setInputKanji(true);

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setEnabled(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        displayReservationLabel.setText("予約表表示");

        displayReservationGroup.add(rdoDisplayReservationEnable);
        rdoDisplayReservationEnable.setSelected(true);
        rdoDisplayReservationEnable.setText("する");
        rdoDisplayReservationEnable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDisplayReservationEnable.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoDisplayReservationEnable.setOpaque(false);

        displayReservationGroup.add(rdoDisplayReservationDisable);
        rdoDisplayReservationDisable.setText("しない");
        rdoDisplayReservationDisable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDisplayReservationDisable.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoDisplayReservationDisable.setOpaque(false);

        staffClassContractedNameLabel.setText("区分略称");

        staffClassContractedName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffClassContractedName.setColumns(20);
        staffClassContractedName.setInputKanji(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(staffClassesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(displayReservationLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(staffClassNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(rdoDisplayReservationEnable)
                                .add(15, 15, 15)
                                .add(rdoDisplayReservationDisable))
                            .add(staffClassName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 168, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(15, 15, 15)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(staffClassContractedNameLabel)
                            .add(displaySeqLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(staffClassContractedName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(staffClassNameLabel)
                    .add(staffClassName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(staffClassContractedNameLabel)
                    .add(staffClassContractedName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(displayReservationLabel)
                    .add(rdoDisplayReservationEnable)
                    .add(rdoDisplayReservationDisable)
                    .add(displaySeqLabel)
                    .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(15, 15, 15)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(staffClassesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
	{//GEN-HEADEREND:event_deleteButtonActionPerformed

		//削除確認
		if(MessageDialog.showYesNoDialog(this,
				MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, list.get(selIndex).getStaffClassName()),
				"スタッフ区分登録",
				JOptionPane.WARNING_MESSAGE) != 0)
		{
			return;
		}

                this.delete();
	}//GEN-LAST:event_deleteButtonActionPerformed

	private void renewButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renewButtonActionPerformed
	{//GEN-HEADEREND:event_renewButtonActionPerformed
		if(this.checkInput())
		{
			this.regist(false);
		}
	}//GEN-LAST:event_renewButtonActionPerformed

	private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
	{//GEN-HEADEREND:event_addButtonActionPerformed
		if(this.checkInput())
		{
			this.regist(true);
		}
	}//GEN-LAST:event_addButtonActionPerformed

        private void staffClassesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffClassesMouseReleased
		this.changeCurrentData();
        }//GEN-LAST:event_staffClassesMouseReleased

        private void staffClassesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_staffClassesKeyReleased
		this.changeCurrentData();
        }//GEN-LAST:event_staffClassesKeyReleased

	private void formHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formHierarchyChanged
    	//IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す  
    	if ((evt.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
        	if (!evt.getComponent().isDisplayable() && isLoadDisplay) {
            	SystemInfo.MessageDialogGB(this, this.getTitle());
        	}
    	}
    	//IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
	} //GEN-LAST:event_formHierarchyChanged
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.ButtonGroup displayReservationGroup;
    private javax.swing.JLabel displayReservationLabel;
    private com.geobeck.swing.JFormattedTextFieldEx displaySeq;
    private javax.swing.JLabel displaySeqLabel;
    private javax.swing.JRadioButton rdoDisplayReservationDisable;
    private javax.swing.JRadioButton rdoDisplayReservationEnable;
    private javax.swing.JButton renewButton;
    private com.geobeck.swing.JFormattedTextFieldEx staffClassContractedName;
    private javax.swing.JLabel staffClassContractedNameLabel;
    private com.geobeck.swing.JFormattedTextFieldEx staffClassName;
    private javax.swing.JLabel staffClassNameLabel;
    private javax.swing.JTable staffClasses;
    private javax.swing.JScrollPane staffClassesScrollPane;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * スタッフ区分マスタ登録画面用FocusTraversalPolicy
	 */
	private	MstStaffClassFocusTraversalPolicy	ftp	=
			new MstStaffClassFocusTraversalPolicy();
	
	/**
	 * スタッフ区分マスタ登録画面用FocusTraversalPolicyを取得する。
	 * @return スタッフ区分マスタ登録画面用FocusTraversalPolicy
	 */
	public MstStaffClassFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(addButton);
		SystemInfo.addMouseCursorChange(renewButton);
		SystemInfo.addMouseCursorChange(deleteButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
		staffClassName.addKeyListener(SystemInfo.getMoveNextField());
		staffClassName.addFocusListener(SystemInfo.getSelectText());
		staffClassContractedName.addKeyListener(SystemInfo.getMoveNextField());
		staffClassContractedName.addFocusListener(SystemInfo.getSelectText());
                displaySeq.addKeyListener(SystemInfo.getMoveNextField());
		displaySeq.addFocusListener(SystemInfo.getSelectText());
	}
	
	/**
	 * 初期化処理を行う。
	 */
	private void init()
	{
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			list = msc.load(con);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		this.showData();
	}
	
	/**
	 * 再表示を行う。
	 */
	private void refresh()
	{
		//データベースからデータを読み込む
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			list = msc.load(con);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
                
		//テーブルにスタッフ区分マスタデータを表示する
		this.showData();
		//入力をクリアする
		this.clear();
		
		staffClassName.requestFocusInWindow();
	}
	
	/**
	 * 入力項目をクリアする。
	 */
	private void clear()
	{
		selIndex	=	-1;
		staffClassName.setText("");
		staffClassContractedName.setText("");
		rdoDisplayReservationEnable.setSelected(false);
		displaySeq.setText("");
		
		if(0 < staffClasses.getRowCount())
				staffClasses.removeRowSelectionInterval(0, staffClasses.getRowCount() - 1);
		
		this.changeCurrentData();
	}
	
	
	/**
	 * データを表示する。
	 */
	private void showData()
	{
		SwingUtil.clearTable(staffClasses);
		DefaultTableModel model = (DefaultTableModel)staffClasses.getModel();
		
		//全行削除
		model.setRowCount(0);
		staffClasses.removeAll();
		
		for(MstStaffClass msc : list)
		{
			Object[] rowData = { msc.getStaffClassName(),
					     msc.getStaffClassContractedName(),
					     msc.isDisplayReservation() ? "      ●" : "",
					     msc.getDisplaySeq()	};
			model.addRow(rowData);
		}
	}
	
	/**
	 * 選択データが変更されたときの処理を行う。
	 */
	private void changeCurrentData()
	{
		int index = staffClasses.getSelectedRow();
		
		if(0 <= index && index < list.size() && index != selIndex)
		{
			selIndex = index;

			//選択されているデータを表示
			this.showCurrentData();
		}
		
		renewButton.setEnabled(0 <= selIndex);
		deleteButton.setEnabled(0 <= selIndex);
	}
	
	/**
	 * 選択されたデータを入力項目に表示する。
	 */
	private void showCurrentData()
	{
		staffClassName.setText(list.get(selIndex).getStaffClassName());
		staffClassContractedName.setText(list.get(selIndex).getStaffClassContractedName());
                
                if(list.get( selIndex ).isDisplayReservation().booleanValue()){
                    this.rdoDisplayReservationEnable.setSelected(true);
                }else {
                    this.rdoDisplayReservationDisable.setSelected(true);
                }
                
		displaySeq.setText(list.get(selIndex).getDisplaySeq().toString());
	}
	
	
	/**
	 * 入力チェックを行う。
	 * @return 入力エラーがなければtrueを返す。
	 */
	private boolean checkInput()
	{
		//スタッフ区分名
		if(staffClassName.getText().equals(""))
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "スタッフ区分名"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			staffClassName.requestFocusInWindow();
			return	false;
		}
        
		// スタッフ区分略称
		if( ( !CheckUtil.checkStringLength( staffClassContractedName.getText(), 10 ) )||!CheckUtil.is1ByteChars( staffClassContractedName.getText() ) )
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(1201, "スタッフ区分略称", "半角10文字"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			staffClassContractedName.requestFocusInWindow();
			return	false;
		}
                
		return	true;
	}
	
	
	/**
	 * 入力されたデータを登録する。
	 * @param isAdd true - 追加処理
	 * @return true - 成功
	 */
	private boolean regist(boolean isAdd)
	{
		boolean result = false;
		MstStaffClass msc = new MstStaffClass();
		
		if(!isAdd && 0 <= selIndex)
		{
			msc.setData(list.get(selIndex));
		}
		
		msc.setStaffClassName(staffClassName.getText());
		msc.setStaffClassContractedName(staffClassContractedName.getText());
		msc.setDisplayReservation(rdoDisplayReservationEnable.isSelected());
		msc.setDisplaySeq((displaySeq.getText().equals("") ? -1 : Integer.parseInt(displaySeq.getText())));
		
		ConnectionWrapper con = SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
			if(msc.regist(con, (0 < selIndex ? list.get(selIndex).getDisplaySeq() : -1)))
			{
				con.commit();
				this.refresh();
				result = true;
				//IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
				isLoadDisplay = true;
				//IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	
	/**
	 * 選択されたデータを削除する。
	 * @return true - 成功
	 */
	private boolean delete()
	{
		boolean result = false;
		MstStaffClass msc = null;
		
		if (0 <= selIndex && selIndex < list.size()) {
                    msc = list.get(selIndex);
		}
		
		ConnectionWrapper con = SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
			if(msc.delete(con))
			{
				con.commit();
				this.refresh();
				result = true;
				//IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
				isLoadDisplay = true;
				//IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * スタッフ区分マスタ登録画面用FocusTraversalPolicy
	 */
	private class MstStaffClassFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent のあとでフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			if (aComponent.equals(staffClassName))
			{
				return staffClassContractedName;
			}
			else if (aComponent.equals(staffClassContractedName))
			{
				return displaySeq;
			}
			else if (aComponent.equals(displaySeq))
			{
				return displaySeq;
			}
			
			return staffClassName;
		}

		/**
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{
			if (aComponent.equals(staffClassName))
			{
				return staffClassName;
			}
			else if (aComponent.equals(displaySeq))
			{
				return staffClassContractedName;
			}
			else if (aComponent.equals(staffClassContractedName))
			{
				return staffClassName;
			}
			
			return staffClassName;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return staffClassName;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return displaySeq;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return staffClassName;
		}
		
		/**
		 * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
		 * show() または setVisible(true) の呼び出しで一度ウィンドウが表示されると、
		 * 初期化コンポーネントはそれ以降使用されません。
		 * 一度別のウィンドウに移ったフォーカスが再び設定された場合、
		 * または、一度非表示状態になったウィンドウが再び表示された場合は、
		 * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
		 * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
		 * @param window 初期コンポーネントが返されるウィンドウ
		 * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
		 */
		public Component getInitialComponent(Window window)
		{
			return staffClassName;
		}
	}
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		staffClasses.getColumnModel().getColumn(0).setPreferredWidth(200);
		staffClasses.getColumnModel().getColumn(1).setPreferredWidth(70);
		staffClasses.getColumnModel().getColumn(2).setPreferredWidth(50);
		staffClasses.getColumnModel().getColumn(3).setPreferredWidth(30);
	}
	
	
	/**
	 * 列の表示位置を設定するTableCellRenderer
	 */
	private class TableCellAlignRenderer extends DefaultTableCellRenderer
	{
		/** Creates a new instance of ReservationTableCellRenderer */
		public TableCellAlignRenderer()
		{
			super();
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
			
			switch(column)
			{
				case 1:
					super.setHorizontalAlignment(SwingConstants.RIGHT);
					break;
				default:
					super.setHorizontalAlignment(SwingConstants.LEFT);
					break;
			}

			return this;
		}
	}
}

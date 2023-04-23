/*
 * SimpleMasterPanel.java
 *
 * Created on 2006/10/18, 11:24
 */

package com.geobeck.sosia.pos.basicinfo.company;
import com.geobeck.sosia.pos.basicinfo.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

/**
 *
 * @author  katagiri
 */
public class MstResponsePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	//IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
    private boolean isLoadDisplay= false;
    //IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
	/** Creates new form SimpleMasterPanel */
	public MstResponsePanel(String masterName, String tableName,
								String idColName, String nameColName,
								int	nameLength,
								TableCellRenderer headerRenderer)
	{
		super();
		this.init(masterName, tableName, idColName, nameColName, nameLength);
		initComponents();
		addMouseCursorChange();
                this.setSize(780, 680);
		SwingUtil.setJTableHeaderRenderer(datas, headerRenderer);
		this.setListener();
		this.refresh();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        category_label = new javax.swing.JLabel();
        category_drop = new javax.swing.JComboBox();
        id_label = new javax.swing.JLabel();
        id_text = new com.geobeck.swing.JFormattedTextFieldEx();
        dataNameLabel = new javax.swing.JLabel();
        dataName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)dataName.getDocument()).setDocumentFilter(
            new CustomFilter(sm.getNameLength()));
        displaySeqLabel = new javax.swing.JLabel();
        displaySeq = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)displaySeq.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        shopSettingButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        renewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        datasScrollPane = new javax.swing.JScrollPane();
        datas = new javax.swing.JTable();

        setFocusCycleRoot(true);
        addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                formHierarchyChanged(evt);
            }
        });

        category_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        category_label.setText("反響分類 ");

        category_drop.setModel(new javax.swing.DefaultComboBoxModel());

        id_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        id_label.setText("反響ID");

        id_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        id_text.setColumns(4);
        id_text.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        dataNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dataNameLabel.setText(sm.getMasterName() + "名");

        dataName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        dataName.setColumns(sm.getNameLength());
        dataName.setInputKanji(true);

        displaySeqLabel.setText("挿入位置");

        displaySeq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        displaySeq.setColumns(4);
        displaySeq.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        shopSettingButton.setIcon(SystemInfo.getImageIcon("/button/master/shop_setting_off.jpg"));
        shopSettingButton.setBorderPainted(false);
        shopSettingButton.setPressedIcon(SystemInfo.getImageIcon("/button/master/shop_setting_on.jpg"));
        shopSettingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopSettingButtonActionPerformed(evt);
            }
        });

        addButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        addButton.setBorderPainted(false);
        addButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setEnabled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setEnabled(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        datasScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        datas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][][][]
            {
                {null, null,null,null}
            },
            new String []
            {
                "反響分類","反響ID",sm.getMasterName(),"表示順"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class,  java.lang.String.class,  java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false,false,false,false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        datas.setFocusTraversalPolicy(this.getFocusTraversalPolicy());
        datas.setSelectionBackground(new java.awt.Color(220, 220, 220));
        datas.setSelectionForeground(new java.awt.Color(0, 0, 0));
        datas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        datas.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(datas, SystemInfo.getTableHeaderRenderer());
        datas.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(datas);
        datas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                datasMouseReleased(evt);
            }
        });
        datas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datasKeyReleased(evt);
            }
        });
        datasScrollPane.setViewportView(datas);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(datasScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(shopSettingButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(category_label, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(id_label, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dataNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(id_text, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(category_drop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 266, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(dataName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 266, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(displaySeqLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(category_label)
                    .add(category_drop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(id_label)
                    .add(id_text, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(displaySeqLabel)
                    .add(dataName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dataNameLabel)
                    .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(shopSettingButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(datasScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .add(143, 143, 143))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
	{//GEN-HEADEREND:event_deleteButtonActionPerformed
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

	private void datasMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_datasMouseReleased
	{//GEN-HEADEREND:event_datasMouseReleased
		this.changeCurrentData();
	}//GEN-LAST:event_datasMouseReleased

	private void datasKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_datasKeyReleased
	{//GEN-HEADEREND:event_datasKeyReleased
		this.changeCurrentData();
	}//GEN-LAST:event_datasKeyReleased

        private void shopSettingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopSettingButtonActionPerformed

            MstUseResponsePanel p = new MstUseResponsePanel();
            p.setOpener(this);
            this.setVisible(false);
            parentFrame.changeContents(p);

        }//GEN-LAST:event_shopSettingButtonActionPerformed

    private void formHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formHierarchyChanged
        //IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す  
        if ((evt.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            if (!evt.getComponent().isShowing()&& isLoadDisplay) {
                SystemInfo.MessageDialogGB(this, this.getTitle());
                isLoadDisplay = false;
            }
        }
        //IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
    }//GEN-LAST:event_formHierarchyChanged
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JComboBox category_drop;
    private javax.swing.JLabel category_label;
    private com.geobeck.swing.JFormattedTextFieldEx dataName;
    private javax.swing.JLabel dataNameLabel;
    private javax.swing.JTable datas;
    private javax.swing.JScrollPane datasScrollPane;
    private javax.swing.JButton deleteButton;
    private com.geobeck.swing.JFormattedTextFieldEx displaySeq;
    private javax.swing.JLabel displaySeqLabel;
    private javax.swing.JLabel id_label;
    private com.geobeck.swing.JFormattedTextFieldEx id_text;
    private javax.swing.JButton renewButton;
    private javax.swing.JButton shopSettingButton;
    // End of variables declaration//GEN-END:variables
	
	private	SimpleMaster	sm				=	null;
        private  ArrayList<MstResponseClass> listResponseClass = new ArrayList<MstResponseClass>();
	private	Integer			currentIndex	=	-1;
	private Integer                 indexResponseClass =    -1 ;
	private	SimpleMasterFocusTraversalPolicy	ftp	=
			new SimpleMasterFocusTraversalPolicy();
	
	/**
	 * シンプルマスタ登録画面用FocusTraversalPolicyを取得する。
	 * @return  シンプルマスタ登録画面用FocusTraversalPolicy
	 */
	public SimpleMasterFocusTraversalPolicy getFocusTraversalPolicy()
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
		SystemInfo.addMouseCursorChange(shopSettingButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
		dataName.addKeyListener(SystemInfo.getMoveNextField());
		dataName.addFocusListener(SystemInfo.getSelectText());
                id_text.addKeyListener(SystemInfo.getMoveNextField());
		id_text.addFocusListener(SystemInfo.getSelectText());
                category_drop.addKeyListener(SystemInfo.getMoveNextField());
                category_drop.addFocusListener(SystemInfo.getSelectText());
		displaySeq.addKeyListener(SystemInfo.getMoveNextField());
		displaySeq.addFocusListener(SystemInfo.getSelectText());
	}
	
	/**
	 * 初期化処理を行う。
	 * @param masterName マスタの名称
	 * @param tableName テーブル名
	 * @param idColName ＩＤの列名
	 * @param nameColName 名称の列名
	 * @param nameLength 名称の最大文字数
	 */
	private void init(String masterName,
						String tableName,
						String idColName,
						String nameColName,
						int	nameLength)
	{
		this.setTitle(masterName + "登録");
		sm	=	new SimpleMaster(masterName, tableName,
									idColName, nameColName, nameLength);
               
	}
	
	/**
	 * 入力項目をクリアする。
	 */
	private void clear()
	{
		dataName.setText("");
		displaySeq.setText("");
                id_text.setText("");
                category_drop.setSelectedIndex(-1);
		if(0 < datas.getRowCount())
				datas.removeRowSelectionInterval(0, datas.getRowCount() - 1);
		currentIndex = -1;
                indexResponseClass = -1;
		this.changeCurrentData();
	}
	
	/**
	 * 再表示を行う。
	 */
	private void refresh()
	{
                //mst_response_classのデータを取る
                this.showMstResponseClass();
		//マスタをデータベースから読み込む
		sm.loadDataForResponse();
		//テーブルにマスタデータを表示する
		this.showTableData();
		//入力をクリアする
		this.clear();
		
		dataName.requestFocusInWindow();
	}
	
	
	/**
	 * テーブルにマスタデータを表示する。
	 */
	private void showTableData()
	{
		DefaultTableModel	model	=	(DefaultTableModel)datas.getModel();
		
		//全行削除
		model.setRowCount(0);
		datas.removeAll();
		
		for(MstData md : sm)
		{
			Object[]	rowData	=	{md.getResponse_class_name(),md.getResponse_no(),md.getName(),md.getDisplaySeq()};
			model.addRow(rowData);
		}
	}
         
        /**
	 * Loan add 20130227
	 */
	private void showMstResponseClass()
	{
	        try {
                    category_drop.removeAllItems();
                    MstResponseClass mrc=new MstResponseClass();
                    listResponseClass= mrc.loadAllResponseClass(SystemInfo.getConnection());
                    for (MstResponseClass mstResponseClass : listResponseClass) {
                        category_drop.addItem(mstResponseClass);
                    }
                                     
                } catch (SQLException ex) {
                    Logger.getLogger(MstResponsePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
	}
	
	/**
	 * 選択データが変更されたときの処理。
	 */
	private void changeCurrentData()
	{
		int	index	=	datas.getSelectedRow();
                boolean flag    = false;
		if(0 <= index && index < sm.size() && currentIndex != index)
		{
			currentIndex	=	index;
                        
                        for (int i = 0; i < category_drop.getItemCount(); i++) {
                          
                        if(((MstResponseClass)category_drop.getItemAt(i)).getResponseClassId() ==sm.get(index).getResponse_class_id()){
                            flag=true;
                            indexResponseClass=i;
                            break;
                        }
                        }
                        if (flag == false) {
                          indexResponseClass=-1;
                        }
                        
			//選択されているデータを表示
			this.showCurrentData();
		}
                
		
		//データが選択されている場合、更新・削除ボタンを使用可能にする
		renewButton.setEnabled(0 <= currentIndex);
		deleteButton.setEnabled(0 <= currentIndex);
	}
	
	/**
	 * 選択されているデータを表示する。
	 */
	private void showCurrentData()
	{
		dataName.setText(sm.get(currentIndex).getName());
		displaySeq.setText(sm.get(currentIndex).getDisplaySeq().toString());
                id_text.setText(sm.get(currentIndex).getResponse_no().toString().trim());
                category_drop.setSelectedIndex(indexResponseClass);
              
	}
	
	/**
	 * 入力チェックを行う。
	 * @return true - ＯＫ
	 */
	private boolean checkInput()
	{
		//名称
		if(dataName.getText().equals(""))
		{
			return	false;
		}
		if(id_text.getText().equals(""))
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "IDテキストボックス"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			id_text.requestFocusInWindow();
			return	false;
		}
		if( ( !CheckUtil.checkStringLength( id_text.getText().trim(), 5 ) )||!CheckUtil.is1ByteChars( id_text.getText().trim() ) )
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(1201, "IDテキストボックス", "半角5文字"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			id_text.requestFocusInWindow();
			return	false;
		}
                // ThuanNK 20140124
//                if (!CheckUtil.isNumber(displaySeq.getText())) {
//                     MessageDialog.showMessageDialog(this,
//                                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "挿入位置"),
//                                        this.getTitle(),
//                                        JOptionPane.ERROR_MESSAGE);
//                      displaySeq.requestFocusInWindow();
//                      return	false;
//                }
 
		return	true;
	}
        
        
	
	/**
	 * 登録処理を行う。
	 * @param isInsert true - Insert
	 */
	private void regist(boolean isInsert)
	{
            boolean checkData = true;
            //IDが重複していないかをチェック
            for (MstData mstData_rapa : sm) {
//                Integer currentResponseClassId = 0;
//                if ((MstResponseClass) category_drop.getSelectedItem() != null) {
//                    currentResponseClassId = ((MstResponseClass) category_drop.getSelectedItem()).getResponseClassId();
//                }
//                if (mstData_rapa.getResponse_no().trim().equalsIgnoreCase(id_text.getText().trim())
//                        && mstData_rapa.getResponse_class_id() == currentResponseClassId) {
                if (mstData_rapa.getResponse_no().trim().equalsIgnoreCase(id_text.getText().trim())){
                    if (isInsert == true) {//追加の場合
                        MessageDialog.showMessageDialog(
                                this,
                                "IDが重複しています。他のIDを入力してください。",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        id_text.requestFocusInWindow();
                        checkData = false;
                        break;
                    } else {//更新の場合
                        if (mstData_rapa.getID() != sm.get(currentIndex).getID()) {
                            MessageDialog.showMessageDialog(
                                    this,
                                    "IDが重複しています。他のIDを入力してください。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            id_text.requestFocusInWindow();
                            checkData = false;
                            break;
                        }
                    }
                }
            }

            if (checkData == true) {
                MstData temp = new MstData();

                //行が選択されている場合、元のデータをセットする
                if (0 <= currentIndex) {
                    temp.setID(sm.get(currentIndex).getID());
                    temp.setName(sm.get(currentIndex).getName());
                    temp.setDisplaySeq(sm.get(currentIndex).getDisplaySeq());
                    temp.setResponse_no(sm.get(currentIndex).getResponse_no());
                    temp.setResponse_class_id(sm.get(currentIndex).getResponse_class_id());

                }

                //入力されたデータをセット
                temp.setName(dataName.getText());
                temp.setDisplaySeq((displaySeq.getText().equals("") ? -1 : Integer.parseInt(displaySeq.getText())));
                temp.setResponse_class_id((MstResponseClass) category_drop.getSelectedItem() == null ? 0 : ((MstResponseClass) category_drop.getSelectedItem()).getResponseClassId());
                temp.setResponse_no(id_text.getText().trim());


                //データを登録
                if (sm.registForResponse(temp, (isInsert ? -1 : currentIndex))) {
                    this.refresh();
                	//IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
                	isLoadDisplay = true;
                	//IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
                } else {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
                            sm.getMasterName()),
                            sm.getMasterName() + "登録",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
	}
	
	/**
	 * 削除処理を行う。
	 */
	private void delete()
	{
		MstData		temp	=	sm.get(currentIndex);
		
		//削除確認
		if(MessageDialog.showYesNoDialog(this,
				MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, temp.getName()),
				sm.getMasterName() + "マスタ登録",
				JOptionPane.WARNING_MESSAGE) != 0)
		{
			return;
		}
		
		if(sm.delete(temp))
		{
			this.refresh();
            //IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
            isLoadDisplay = true;
            //IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
		}
		else
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED,
							sm.getMasterName()),
					sm.getMasterName() + "マスタ登録",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * シンプルマスタ登録画面用FocusTraversalPolicy
	 */
	private class SimpleMasterFocusTraversalPolicy
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
                        if (aComponent.equals(category_drop))
			{
				return id_text;
			}
                        else if (aComponent.equals(id_text))
			{
				return dataName;
			}
                        else if (aComponent.equals(dataName))
			{
				return displaySeq;
			}
                        else if (aComponent.equals(displaySeq))
			{
				return shopSettingButton;
			}
                        else if(aComponent.equals(shopSettingButton))
                        {
                                return addButton;
                        }
                        else if(aComponent.equals(addButton))
                        {
                                return renewButton;
                        }
                        else if(aComponent.equals(renewButton))
                        {
                                return deleteButton;
                        }
                        else if(aComponent.equals(deleteButton))
                        {
                                return category_drop;
                        }
			
			return category_drop;
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
			if (aComponent.equals(category_drop))
			{
				return deleteButton;
			}
                        else if (aComponent.equals(deleteButton))
			{
				return renewButton;
			}
                        else if (aComponent.equals(renewButton))
			{
				return addButton;
			}
			else if (aComponent.equals(addButton))
			{
				return shopSettingButton;
			}
                        else if (aComponent.equals(addButton))
			{
				return shopSettingButton;
			}
                        else if (aComponent.equals(shopSettingButton))
			{
				return displaySeq;
			}
                        else if (aComponent.equals(displaySeq))
			{
				return dataName;
			}
                        else if (aComponent.equals(dataName))
			{
				return id_text;
			}
                        else if (aComponent.equals(id_text))
                        {
                                return category_drop;
                        }
			
			return category_drop;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return category_drop;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return deleteButton;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return category_drop;
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
			return category_drop;
		}
	}
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		
		datas.getColumnModel().getColumn(1).setPreferredWidth(50);
                datas.getColumnModel().getColumn(2).setPreferredWidth(240);
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
				case 3:
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

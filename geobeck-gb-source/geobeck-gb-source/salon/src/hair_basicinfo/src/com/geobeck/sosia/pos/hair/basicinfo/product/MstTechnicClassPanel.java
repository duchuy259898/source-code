/*
 * MstTechnicClassPanel.java
 *
 * Created on 2006/10/20, 11:01
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;

import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import com.geobeck.sosia.pos.basicinfo.SimpleMasterDialog;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.master.MstData;
// IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
import com.geobeck.sosia.pos.master.company.MstShopCategory;
import com.geobeck.sosia.pos.master.company.MstShopCategorys;
// IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;

/**
 *
 * @author  katagiri
 */
public class MstTechnicClassPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	private MstTechnicClasses		mtcs		=	new MstTechnicClasses();
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
        private MstShopCategorys                mscs            =       new MstShopCategorys();
        private MstShopCategory                 msc             =       new MstShopCategory();
        private Integer useShopCategory                                 =       null;
        private Integer shopId                                          =       null;
        // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
	private Integer				selIndex	=	-1;

    //IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
    private boolean isLoadDisplay= false;
    //IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
	
	/** Creates new form MstTechnicClassPanel */
	public MstTechnicClassPanel()
	{
		super();
		initComponents();
		addMouseCursorChange();
		this.setSize(800, 680);
		this.setPath("基本設定 >> 技術マスタ");
		this.setTitle("技術分類登録");
		this.setListener();
                this.initTableColumnWidth();
		this.init();
                
                if (!SystemInfo.getSetteing().isUsePrepaid()) {
                    chkPrepaid.setVisible(false);
                }
	}

        private void initIntegration() {

            integration.removeAllItems();
            integration.addItem(null);
            
            SimpleMaster sm = new SimpleMaster(
                    "",
                    "mst_technic_integration",
                    "technic_integration_id",
                    "technic_integration_name", 0);

            sm.loadData();
  
            for (MstData md : sm) {
                integration.addItem(md);
            }

            integration.setSelectedIndex(0);
        }
        
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
        /**
         * 業態を初期化する。
         * @param  None
         * @return None
         * @author IVS_LeTheHieu
         * @since  20140703
         */
        private void initBusiness() {

            business.removeAllItems();
            business.addItem(null);
            
            SimpleMaster sb = new SimpleMaster(
                    "",
                    "mst_shop_category",
                    "shop_category_id",
                    "shop_class_name", 0);

            sb.loadData();
  
            for (MstData md : sb) {
                business.addItem(md);
            }

            business.setSelectedIndex(0);
        }
        // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        addButton = new javax.swing.JButton();
        technicClassesScrollPane = new javax.swing.JScrollPane();
        technicClasses = new javax.swing.JTable();
        displaySeq = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)displaySeq.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        technicClassName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)technicClassName.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        productButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        renewButton = new javax.swing.JButton();
        technicClassNameLabel = new javax.swing.JLabel();
        displaySeqLabel = new javax.swing.JLabel();
        technicClassContractedNameLabel = new javax.swing.JLabel();
        technicClassContractedName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)technicClassName.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        chkPrepaid = new javax.swing.JCheckBox();
        integrationButton = new javax.swing.JButton();
        technicClassContractedNameLabel1 = new javax.swing.JLabel();
        integration = new javax.swing.JComboBox();
        businesslbl = new javax.swing.JLabel();
        business = new javax.swing.JComboBox();

        addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                formHierarchyChanged(evt);
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

        technicClassesScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        technicClasses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "分類名", "分類略称", "表示順", "プリペイド", "統合分類", "業態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technicClasses.setSelectionBackground(new java.awt.Color(220, 220, 220));
        technicClasses.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technicClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicClasses.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(technicClasses, SystemInfo.getTableHeaderRenderer());
        technicClasses.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);

        SelectTableCellRenderer.setSelectTableCellRenderer(technicClasses);
        technicClasses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                technicClassesMouseReleased(evt);
            }
        });
        technicClasses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                technicClassesKeyReleased(evt);
            }
        });
        technicClassesScrollPane.setViewportView(technicClasses);
        if (technicClasses.getColumnModel().getColumnCount() > 0) {
            technicClasses.getColumnModel().getColumn(0).setPreferredWidth(80);
        }

        displaySeq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        displaySeq.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        technicClassName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClassName.setDocument(new CustomPlainDocument(20));
        technicClassName.setInputKanji(true);

        productButton.setIcon(SystemInfo.getImageIcon("/button/master/regist_technic_off.jpg"));
        productButton.setBorderPainted(false);
        productButton.setPressedIcon(SystemInfo.getImageIcon("/button/master/regist_technic_on.jpg"));
        productButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productButtonActionPerformed(evt);
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

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setEnabled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        technicClassNameLabel.setText("分類名");

        displaySeqLabel.setText("挿入位置");

        technicClassContractedNameLabel.setText("分類略称");

        technicClassContractedName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClassContractedName.setDocument(new CustomPlainDocument(20));
        technicClassContractedName.setInputKanji(true);

        chkPrepaid.setText("プリペイド");
        chkPrepaid.setOpaque(false);

        integrationButton.setIcon(SystemInfo.getImageIcon("/button/master/integration_reg_off.jpg"));
        integrationButton.setBorderPainted(false);
        integrationButton.setPressedIcon(SystemInfo.getImageIcon("/button/master/integration_reg_on.jpg"));
        integrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrationButtonActionPerformed(evt);
            }
        });

        technicClassContractedNameLabel1.setText("統合分類");

        integration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        businesslbl.setText("業態");

        business.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(technicClassContractedNameLabel)
                            .add(technicClassNameLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(technicClassName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 295, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(integration, 0, 189, Short.MAX_VALUE)
                                    .add(technicClassContractedName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(displaySeqLabel)
                                    .add(businesslbl))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(40, 40, 40)
                                        .add(chkPrepaid))
                                    .add(business, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                    .add(technicClassContractedNameLabel1)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(7, 7, 7)
                            .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(productButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(integrationButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, technicClassesScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 684, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(technicClassNameLabel)
                    .add(technicClassName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(technicClassContractedNameLabel)
                    .add(technicClassContractedName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(displaySeqLabel)
                    .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkPrepaid))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(technicClassContractedNameLabel1)
                            .add(integration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(businesslbl)
                            .add(business, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(productButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(integrationButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(technicClassesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
	if(this.checkInput()) {
	    this.regist(true);
	    this.refresh();
	}
    }//GEN-LAST:event_addButtonActionPerformed

    private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed
	if(this.checkInput()) {
	    this.regist(false);
	    this.refresh();
	}
    }//GEN-LAST:event_renewButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
	if(this.checkDelete()){
	    this.delete();
	    this.refresh();
	} else{
	    MessageDialog.showMessageDialog(this,
		MessageUtil.getMessage(7101, "分類","技術"),
		this.getTitle(),
		JOptionPane.ERROR_MESSAGE);
	}
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void productButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productButtonActionPerformed
	SystemInfo.getLogger().log(Level.INFO, "技術登録");
	MstTechnicPanel	mtp	=	new MstTechnicPanel();
	
	if(0 <= selIndex) {
	    mtp.setSelectedTechnicClassID(mtcs.get(selIndex).getTechnicClassID());
	}
	
	parentFrame.changeContents(mtp);
    }//GEN-LAST:event_productButtonActionPerformed

	private void technicClassesMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_technicClassesMouseReleased
	{//GEN-HEADEREND:event_technicClassesMouseReleased
		this.changeCurrentData();
	}//GEN-LAST:event_technicClassesMouseReleased

	private void technicClassesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_technicClassesKeyReleased
	{//GEN-HEADEREND:event_technicClassesKeyReleased
		this.changeCurrentData();
	}//GEN-LAST:event_technicClassesKeyReleased

        private void integrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_integrationButtonActionPerformed

            SimpleMasterDialog dlg = new SimpleMasterDialog(
                    "技術統合",
                    "mst_technic_integration",
                    "technic_integration_id",
                    "technic_integration_name",
                    20, SystemInfo.getTableHeaderRenderer());

            dlg.setOpener(this);
            SwingUtil.openAnchorDialog( null, true, dlg, "技術分類統合", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER );

            this.initIntegration();
            this.refresh();

        }//GEN-LAST:event_integrationButtonActionPerformed

    private void formHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formHierarchyChanged
        //IVS_LVTu start add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す  
        if ((evt.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            if (!evt.getComponent().isDisplayable() && isLoadDisplay) {
                SystemInfo.MessageDialogGB(this, this.getTitle());
            }
        }
        //IVS_LVTu end add 2017/08/15 #21596 [gb] マスタ登録のあと再起動アラートを出す
    }//GEN-LAST:event_formHierarchyChanged
	
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JComboBox business;
    private javax.swing.JLabel businesslbl;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkPrepaid;
    private javax.swing.JButton deleteButton;
    private com.geobeck.swing.JFormattedTextFieldEx displaySeq;
    private javax.swing.JLabel displaySeqLabel;
    private javax.swing.JComboBox integration;
    private javax.swing.JButton integrationButton;
    private javax.swing.JButton productButton;
    private javax.swing.JButton renewButton;
    private com.geobeck.swing.JFormattedTextFieldEx technicClassContractedName;
    private javax.swing.JLabel technicClassContractedNameLabel;
    private javax.swing.JLabel technicClassContractedNameLabel1;
    private com.geobeck.swing.JFormattedTextFieldEx technicClassName;
    private javax.swing.JLabel technicClassNameLabel;
    private javax.swing.JTable technicClasses;
    private javax.swing.JScrollPane technicClassesScrollPane;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * 技術分類マスタ登録画面用FocusTraversalPolicy
	 */
	private	MstTechnicClassFocusTraversalPolicy	ftp	=
			new MstTechnicClassFocusTraversalPolicy();
	
	/**
	 * 技術分類マスタ登録画面用FocusTraversalPolicyを取得する。
	 * @return 技術分類マスタ登録画面用FocusTraversalPolicy
	 */
	public MstTechnicClassFocusTraversalPolicy getFocusTraversalPolicy()
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
		SystemInfo.addMouseCursorChange(productButton);
		SystemInfo.addMouseCursorChange(integrationButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
		technicClassName.addKeyListener(SystemInfo.getMoveNextField());
		technicClassName.addFocusListener(SystemInfo.getSelectText());
		technicClassContractedName.addKeyListener(SystemInfo.getMoveNextField());
		technicClassContractedName.addFocusListener(SystemInfo.getSelectText());
		displaySeq.addKeyListener(SystemInfo.getMoveNextField());
		displaySeq.addFocusListener(SystemInfo.getSelectText());
		integration.addKeyListener(SystemInfo.getMoveNextField());
		integration.addFocusListener(SystemInfo.getSelectText());
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                business.addKeyListener(SystemInfo.getMoveNextField());
                business.addFocusListener(SystemInfo.getSelectText());
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
	}
        
	/**
	 * 初期化処理を行う。
	 */
	private void init()
	{

		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			mtcs.load(con);
                        
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
                
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                useShopCategory = SystemInfo.getCurrentShop().getUseShopCategory();
                shopId = SystemInfo.getCurrentShop().getShopID();
                if (shopId.equals(0)){
                    business.setVisible(true);
                    business.removeAllItems();
                    business.addItem(null);
                    // load business data
                    this.initBusiness();
                } else{
                    if (useShopCategory.equals(1)){
                        business.setVisible(true);
                        business.removeAllItems();
                        business.addItem(null);
                        // load business data
                        this.initBusiness();
                    }
                    else{
                        technicClasses.getColumnModel().getColumn(5).setMinWidth(0);
                        technicClasses.getColumnModel().getColumn(5).setMaxWidth(0);
                        technicClasses.getColumnModel().getColumn(5).setPreferredWidth(0);
                        businesslbl.setVisible(false);
                        business.removeAllItems();
                        business.setVisible(false);
                    }
                }
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                
                this.initIntegration();
                
		this.showData();
	}       
        
	/**
	 * 再表示を行う。
	 */
	private void refresh()
	{
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                mtcs.load(con);
            } catch(Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            //テーブルに商品区分マスタデータを表示する
            this.showData();

            //入力をクリアする
            this.clear();

            technicClassName.requestFocusInWindow();
	}
	
	/**
	 * 入力項目をクリアする。
	 */
	private void clear()
	{
		selIndex	=	-1;
		technicClassName.setText("");
		technicClassContractedName.setText("");
		displaySeq.setText("");
		chkPrepaid.setSelected(false);
                integration.setSelectedIndex(0);
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                if (shopId.equals(0)){
                   business.setSelectedIndex(0);
                } else{
                    if (useShopCategory.equals(1)){
                        business.setSelectedIndex(0);
                    }
                }
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                
		if(0 < technicClasses.getRowCount())
				technicClasses.removeRowSelectionInterval(0, technicClasses.getRowCount() - 1);
		
		this.changeCurrentData();
	}

	/**
	 * データを表示する。
	 */
	private void showData()
	{
		DefaultTableModel model = (DefaultTableModel)technicClasses.getModel();
		//全行削除
		model.setRowCount(0);
		technicClasses.removeAll();
		
		for(MstTechnicClass mtc : mtcs)
		{
			Object[] rowData = {
                            mtc.getTechnicClassName(),
                            mtc.getTechnicClassContractedName(),
                            mtc.getDisplaySeq(),
                            (mtc.isPrepaid() ? "       ●" : ""),
                            mtc.getIntegration(),
                            // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                            mtc.getBusiness()
                            // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録

                        };
			model.addRow(rowData);
		}

	}
        
	/**
	 * 選択データが変更されたときの処理を行う。
	 */
	private void changeCurrentData()
	{
		int	index	=	technicClasses.getSelectedRow();
		
		if(0 <= index && index < mtcs.size() && index != selIndex)
		{
			selIndex	=	index;
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
		technicClassName.setText(mtcs.get(selIndex).getTechnicClassName());
		technicClassContractedName.setText( mtcs.get( selIndex ).getTechnicClassContractedName() );
		displaySeq.setText(mtcs.get(selIndex).getDisplaySeq().toString());
                chkPrepaid.setSelected(mtcs.get(selIndex).isPrepaid());

                integration.setSelectedIndex(0);
                integration.setSelectedItem(mtcs.get(selIndex).getIntegration());
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                if (shopId.equals(0)){
                   business.setSelectedIndex(0);
                   business.setSelectedItem(mtcs.get(selIndex).getBusiness());
                } else{
                    if (useShopCategory.equals(1)){
                        business.setSelectedIndex(0);
                        business.setSelectedItem(mtcs.get(selIndex).getBusiness());
                    }
                }
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
        }
        
	/**
	 * 入力チェックを行う。
	 * @return 入力エラーがなければtrueを返す。
	 */
	private boolean checkInput()
	{
		//技術区分名
		if(technicClassName.getText().equals(""))
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "技術区分名"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			technicClassName.requestFocusInWindow();
			return	false;
		}
		
		//技術区分略称
		if(technicClassContractedName.getText().equals(""))
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "技術区分略称"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			technicClassContractedName.requestFocusInWindow();
			return	false;
		}
		if( ( !CheckUtil.checkStringLength( technicClassContractedName.getText(), 5 ) )||!CheckUtil.is1ByteChars( technicClassContractedName.getText() ) )
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(1201, "技術区分略称", "半角5文字"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			technicClassContractedName.requestFocusInWindow();
			return	false;
		}
		
		//表示順
		if(displaySeq.getText().equals("0"))
		{
			MessageDialog.showMessageDialog(this,
					"挿入位置は 1 以上を指定してください。",
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			displaySeq.requestFocusInWindow();
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
		boolean		result	=	false;
		MstTechnicClass	mtc	=	new MstTechnicClass();
		
		if(!isAdd && 0 <= selIndex)
		{
			mtc.setData(mtcs.get(selIndex));
		}
		
		mtc.setTechnicClassName(technicClassName.getText());
		mtc.setTechnicClassContractedName( technicClassContractedName.getText() );
		mtc.setDisplaySeq((displaySeq.getText().equals("") ? -1 : Integer.parseInt(displaySeq.getText())));
                mtc.setPrepaid(chkPrepaid.isSelected() ? 1 : 0);
                mtc.setIntegration((MstData)integration.getSelectedItem());
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                mtc.setBusiness((MstData)business.getSelectedItem());
                mtc.setMstShop(SystemInfo.getCurrentShop());
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
			if(mtc.regist(con, (0 < selIndex ? mtcs.get(selIndex).getDisplaySeq() : -1)))
			{
				con.commit();
				this.refresh();
				result	=	true;
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
		boolean		result	=	false;
		MstTechnicClass	mtc	=	null;
		
		if(0 <= selIndex && selIndex < mtcs.size())
		{
			mtc	=	mtcs.get(selIndex);
		}
		
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
			if(mtc.delete(con))
			{
				con.commit();
				this.refresh();
				result	=	true;
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
	 * 選択された分類に商品登録がされているか。
	 * @return true - 成功
	 */
	private boolean checkDelete()
	{
		boolean		result	=	false;
		MstTechnicClass	mtc	=	null;
		
		if(0 <= selIndex && selIndex < mtcs.size())
		{
			mtc	=	mtcs.get(selIndex);
		}
		
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		
		try
		{
			
			mtc.loadTechnic(con);
			if(mtc.size() == 0){
				result = true;
			}
			
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * 技術分類マスタ登録画面用FocusTraversalPolicy
	 */
	private class MstTechnicClassFocusTraversalPolicy
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
			if (aComponent.equals(technicClassName))
			{
				return displaySeq;
			}
			else if (aComponent.equals(displaySeq))
			{
				return displaySeq;
			}
			
			return technicClassName;
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
			if (aComponent.equals(technicClassName))
			{
				return technicClassName;
			}
			else if (aComponent.equals(displaySeq))
			{
				return technicClassName;
			}
			
			return technicClassName;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return technicClassName;
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
			return technicClassName;
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
			return technicClassName;
		}
	}
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		technicClasses.getColumnModel().getColumn(0).setPreferredWidth(240);
		technicClasses.getColumnModel().getColumn(5).setPreferredWidth(160);
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

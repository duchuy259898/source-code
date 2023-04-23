/*
 * MstUseDiscountPanel.java
 *
 * Created on 2007/01/17, 19:09
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.master.account.MstDiscount;
import com.geobeck.sosia.pos.master.account.MstDiscounts;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author geobeck
 */
public class MstUseDiscountPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private MstDiscounts mrsRef = null;
    private MstDiscounts mrsUse = null;
    
    /** Creates new form MstUseDiscountPanel */
    public MstUseDiscountPanel() {
        initComponents();
        addMouseCursorChange();
        SystemInfo.initGroupShopComponents(shop,2);
        this.setTitle("������ʓo�^");
        this.setSize(710, 680);
        this.init();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        registButton = new javax.swing.JButton();
        referenceProductsScrollPane = new javax.swing.JScrollPane();
        referenceProducts = new javax.swing.JTable();
        referenceLabel = new javax.swing.JLabel();
        selectProductsScrollPane = new javax.swing.JScrollPane();
        selectProducts = new javax.swing.JTable();
        selectLabel = new javax.swing.JLabel();
        shopLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        releaseButton = new javax.swing.JButton();
        releaseAllButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                shopItemStateChanged(evt);
            }
        });

        registButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.setBorderPainted(false);
        registButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButtonActionPerformed(evt);
            }
        });

        referenceProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "������ʖ�"
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
        referenceProducts.setSelectionForeground(new java.awt.Color(0, 0, 0));
        referenceProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referenceProducts.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(referenceProducts, SystemInfo.getTableHeaderRenderer());
        referenceProducts.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(referenceProducts);
        referenceProductsScrollPane.setViewportView(referenceProducts);

        referenceLabel.setText("�Q�ƃ��X�g");

        selectProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "������ʖ�"
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
        selectProducts.setSelectionForeground(new java.awt.Color(0, 0, 0));
        selectProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectProducts.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(selectProducts, SystemInfo.getTableHeaderRenderer());
        selectProducts.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(selectProducts);

        selectProductsScrollPane.setViewportView(selectProducts);

        selectLabel.setText("�I�����X�g");

        shopLabel.setText("�X��");

        jPanel1.setOpaque(false);

        selectButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        selectAllButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right2_off.jpg"));
        selectAllButton.setBorderPainted(false);
        selectAllButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right2_on.jpg"));
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        releaseButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left_off.jpg"));
        releaseButton.setBorderPainted(false);
        releaseButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left_on.jpg"));
        releaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseButtonActionPerformed(evt);
            }
        });

        releaseAllButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left2_off.jpg"));
        releaseAllButton.setBorderPainted(false);
        releaseAllButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left2_on.jpg"));
        releaseAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseAllButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 56, Short.MAX_VALUE)
                .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/back_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/back_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(shopLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(14, 14, 14)
                        .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 201, Short.MAX_VALUE)
                        .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(referenceLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .add(216, 216, 216))
                            .add(layout.createSequentialGroup()
                                .add(referenceProductsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(selectLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, selectProductsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(shopLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(selectLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(selectProductsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(referenceLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(referenceProductsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(103, 103, 103)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void shopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_shopItemStateChanged
        this.init();
    }//GEN-LAST:event_shopItemStateChanged
        
	private void registButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registButtonActionPerformed
	{//GEN-HEADEREND:event_registButtonActionPerformed

            if (selectProducts.getCellEditor() != null) selectProducts.getCellEditor().stopCellEditing();
            
            registButton.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if(this.regist()) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                    //IVS_LVTu start add 2017/08/22 #21596 [gb] �}�X�^�o�^�̂��ƍċN���A���[�g���o��
                    SystemInfo.MessageDialogGB(this, this.getTitle());
                    //IVS_LVTu end add 2017/08/22 #21596 [gb] �}�X�^�o�^�̂��ƍċN���A���[�g���o��
                } else {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }                
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

	}//GEN-LAST:event_registButtonActionPerformed
        
	private void releaseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_releaseButtonActionPerformed
	{//GEN-HEADEREND:event_releaseButtonActionPerformed
            this.moveProduct(false);
	}//GEN-LAST:event_releaseButtonActionPerformed
        
	private void releaseAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_releaseAllButtonActionPerformed
	{//GEN-HEADEREND:event_releaseAllButtonActionPerformed
            this.moveProductAll(false);
	}//GEN-LAST:event_releaseAllButtonActionPerformed
        
	private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectAllButtonActionPerformed
	{//GEN-HEADEREND:event_selectAllButtonActionPerformed
            this.moveProductAll(true);
	}//GEN-LAST:event_selectAllButtonActionPerformed
        
	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
	{//GEN-HEADEREND:event_selectButtonActionPerformed
            this.moveProduct(true);
	}//GEN-LAST:event_selectButtonActionPerformed
                
        private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
            this.showOpener();
}//GEN-LAST:event_backButtonActionPerformed
        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel referenceLabel;
    private javax.swing.JTable referenceProducts;
    private javax.swing.JScrollPane referenceProductsScrollPane;
    private javax.swing.JButton registButton;
    private javax.swing.JButton releaseAllButton;
    private javax.swing.JButton releaseButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JLabel selectLabel;
    private javax.swing.JTable selectProducts;
    private javax.swing.JScrollPane selectProductsScrollPane;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    // End of variables declaration//GEN-END:variables
    
    /**
     * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(registButton);
        SystemInfo.addMouseCursorChange(backButton);
        SystemInfo.addMouseCursorChange(selectButton);
        SystemInfo.addMouseCursorChange(selectAllButton);
        SystemInfo.addMouseCursorChange(releaseButton);
        SystemInfo.addMouseCursorChange(releaseAllButton);
    }
    
    /**
     * �������������s���B
     */
    private void init() {

        int shopId = ((MstShop)shop.getSelectedItem()).getShopID();
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            MstDiscounts mrs = new MstDiscounts(shopId);
            mrs.load_NotUse(con);
            mrsRef = mrs;
            mrs = new MstDiscounts(shopId);
            mrs.load_Use(con);
            mrsUse = mrs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.showItems();
    }
    
    /**
     * �f�[�^��\������B
     */
    private void showItems() {
        this.showItems(mrsRef, referenceProducts);
        this.showItems(mrsUse, selectProducts);
    }
    
    /**
     * �f�[�^��\������B
     * @param pc ����
     * @param table �e�[�u��
     */
    private void showItems(ArrayList<MstDiscount> list, JTable table) {

        Collections.sort(list, new ItemComparator());

        if (table.getCellEditor() != null) table.getCellEditor().stopCellEditing();
        SwingUtil.clearTable(table);
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();

        for (MstDiscount mr : list) {
            model.addRow(new Object[]{
                mr.getDiscountName()
            });
        }
    }
    
    /**
     * �I���i�����j����B
     * @param isSelect true�F�I���Afalse�F����
     */
    public void moveProduct(boolean isSelect) {

        JTable fromTable = (isSelect ? referenceProducts : selectProducts);

        int index = fromTable.getSelectedRow();
        if (index < 0) return;

        if (isSelect) {
            mrsUse.add(mrsRef.get(index));
            mrsRef.remove(index);
        } else {
            mrsRef.add(mrsUse.get(index));
            mrsUse.remove(index);
        }

        this.showItems();
    }
    
    /**
     * �S�đI���i�����j����B
     * @param isSelect true�F�I���Afalse�F����
     */
    public void moveProductAll(boolean isSelect) {

        JTable fromTable = (isSelect ? referenceProducts : selectProducts);

        if (fromTable.getRowCount() == 0) return;

        if (isSelect) {
            for (MstDiscount mr : mrsRef) mrsUse.add(mr);
            mrsRef.clear();
        } else {
            for (MstDiscount mr : mrsUse) mrsRef.add(mr);
            mrsUse.clear();
        }

        this.showItems();
    }
    
    
    /**
     * �o�^�������s���B
     * @return true - �����Afalse - ���s
     */
    public boolean regist() {
        boolean result = false;
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            mrsUse.regist(con);
            result = true;
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return result;
    }

    private class ItemComparator implements java.util.Comparator {
        public int compare(Object s, Object t) {
            return ((MstDiscount) s).getDisplaySeq() - ((MstDiscount) t).getDisplaySeq();
	}
    }

}
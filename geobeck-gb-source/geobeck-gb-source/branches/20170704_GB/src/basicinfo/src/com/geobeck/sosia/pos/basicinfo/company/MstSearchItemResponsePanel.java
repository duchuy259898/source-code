/*
 * MstSearchItemResponsePanel.java
 *
 * Created on 20130310, 11:24
 */
package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstResponse;
import com.geobeck.sosia.pos.hair.master.company.MstResponses;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author vtbPhuong
 */
public class MstSearchItemResponsePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private MstResponseClass mrc = new MstResponseClass();
    private MstResponses mr = new MstResponses();
    private ArrayList<MstResponse> listRespon;
    private ArrayList<MstResponseClass> listResponClass;
    private Integer responClassID = 0;
    private Integer responID = 0;
    // Start add 20130425 nakhoa
    private String responNo = "";
    // End add 20130425 nakhoa
    //Start add 20131104 lvut ���ނȂ�
    private MstResponseClass mrcNotType = new MstResponseClass();
    //End add 20131104 lvut ���ނȂ�
    public Integer getResponID() {
        return responID;
    }

    public void setResponID(Integer responID) {
        this.responID = responID;
    }

    // Start add 20130425 nakhoa
    public String getResponNo(){
        return this.responNo;
    }
    
    public void setResponNo(String responseNo){
        this.responNo = responseNo;
    }
    // End add 20130425 nakhoa
    /**
     * Creates new form MstSearchItemResponsePanel
     */
    public MstSearchItemResponsePanel() {
        super();
        initComponents();
        this.init();
        addMouseCursorChange();
        this.setSize(450, 472);
    }

    /**
     * Creates new form SimpleMasterPanel
     */
    public MstSearchItemResponsePanel(java.awt.Frame parent, boolean modal) {
        super();
        initComponents();
        this.init();
        addMouseCursorChange();
        this.setSize(450, 472);
    }

    public Integer getResponId() {
        return this.responID;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        datasScrollPane1 = new javax.swing.JScrollPane();
        responClass = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        datasScrollPane2 = new javax.swing.JScrollPane();
        responDetail = new javax.swing.JTable();

        setFocusCycleRoot(true);

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("��������");

        datasScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        responClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        responClass.setFocusTraversalPolicy(this.getFocusTraversalPolicy());
        responClass.setName(""); // NOI18N
        responClass.setSelectionBackground(new java.awt.Color(220, 220, 220));
        responClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        responClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        responClass.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(responClass, SystemInfo.getTableHeaderRenderer());
        responClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(responClass);
        responClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                responClassMouseReleased(evt);
            }
        });
        responClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                responClassKeyReleased(evt);
            }
        });
        datasScrollPane1.setViewportView(responClass);

        jLabel2.setText("��������");

        datasScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        responDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        responDetail.setFocusTraversalPolicy(this.getFocusTraversalPolicy());
        responDetail.setName(""); // NOI18N
        responDetail.setSelectionBackground(new java.awt.Color(220, 220, 220));
        responDetail.setSelectionForeground(new java.awt.Color(0, 0, 0));
        responDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        responDetail.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(responDetail, SystemInfo.getTableHeaderRenderer());
        responDetail.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(responDetail);
        responDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                responDetailMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                responDetailMouseReleased(evt);
            }
        });
        responDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                responDetailKeyReleased(evt);
            }
        });
        datasScrollPane2.setViewportView(responDetail);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(datasScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel2)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(datasScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 163, Short.MAX_VALUE)
                        .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(83, 83, 83))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(datasScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .add(datasScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        if (responDetail.getSelectedColumnCount() != 0) {
            this.responID = listRespon.get(responDetail.getSelectedRow()).getResponseID();
            // Start add 20130425 nakhoa
            this.responNo = listRespon.get(responDetail.getSelectedRow()).getResponseNo();
            // End add 20130425 nakhoa
            this.dispose();
        }
    }//GEN-LAST:event_selectButtonActionPerformed

    private void responClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_responClassKeyReleased
        MstResponseClass mrcSelect = this.getSelectedResponesClass();
        this.responClassID = mrcSelect.getResponseClassId();
        showDetailData();
    }//GEN-LAST:event_responClassKeyReleased

    private void responClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_responClassMouseReleased
        // TODO add your handling code here:
        MstResponseClass mrcSelect = this.getSelectedResponesClass();
        this.responClassID = mrcSelect.getResponseClassId();
        showDetailData();
    }//GEN-LAST:event_responClassMouseReleased

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
        
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void responDetailMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_responDetailMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_responDetailMouseReleased

    private void responDetailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_responDetailKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_responDetailKeyReleased

    private void responDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_responDetailMouseClicked

        if (evt.getClickCount() == 2) {
             this.responID = listRespon.get(responDetail.getSelectedRow()).getResponseID();
            // Start add 20130425 nakhoa
            this.responNo = listRespon.get(responDetail.getSelectedRow()).getResponseNo();
            // End add 20130425 nakhoa
            this.dispose();

        }
    }//GEN-LAST:event_responDetailMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane datasScrollPane1;
    private javax.swing.JScrollPane datasScrollPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTable responClass;
    private javax.swing.JTable responDetail;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables

    /**
     * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(closeButton);
        SystemInfo.addMouseCursorChange(selectButton);
    }

    /**
     * �������������s���B
     *
     * @param masterName �}�X�^�̖���
     * @param tableName �e�[�u����
     * @param idColName �h�c�̗�
     * @param nameColName ���̗̂�
     * @param nameLength ���̂̍ő啶����
     */
    private void init() {
        this.setTitle("�������ڌ���");
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            listResponClass = mrc.loadAllResponseClass(con);
            //Start add 20131104 lvut ���ނȂ�
            mrcNotType.setResponseClassId(0);
            mrcNotType.setetResponseClassName("���ނȂ�");
            listResponClass.add(0,mrcNotType);
            //End add 20131104 lvut ���ނȂ�
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        this.showClassData();
    }

    /**
     * �e�[�u���Ƀ}�X�^�f�[�^��\������B
     */
    private void showClassData() {
        DefaultTableModel modelName = (DefaultTableModel) responClass.getModel();

        //�S�s�폜
        modelName.setRowCount(0);
        responClass.removeAll();
        for (MstResponseClass mrc : listResponClass) {
            Object[] rowData = {mrc};
            modelName.addRow(rowData);
        }
        if (responClass.getRowCount() > 0) {
            responClass.setRowSelectionInterval(0, 0);
            this.responClassID = listResponClass.get(0).getResponseClassId();
            this.showDetailData();
        }

    }

    private void showDetailData() {
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            listRespon = mr.loadByClassID(con, this.responClassID);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        DefaultTableModel model = (DefaultTableModel) responDetail.getModel();
        //�S�s�폜
        model.setRowCount(0);
        responDetail.removeAll();
        for (MstResponse mr : listRespon) {
            Object[] rowData = {mr.getResponseNo(), mr.getResponseName()};
            model.addRow(rowData);
        }
    }

    /**
     * �I������Ă��镪�ނ��擾����B
     *
     * @return �I������Ă��镪��
     */
    public MstResponseClass getSelectedResponesClass() {
        if (responClass.getSelectedRow() < 0) {
            return null;
        }
        return (MstResponseClass) responClass.getValueAt(responClass.getSelectedRow(), 0);
    }
    /*
     * ��n��
     */

    public void dispose() {
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).dispose();
        }
    }
}
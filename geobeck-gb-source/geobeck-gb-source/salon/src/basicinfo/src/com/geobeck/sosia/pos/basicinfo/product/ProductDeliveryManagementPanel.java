/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.basicinfo.SelectSameNoData;
import com.geobeck.sosia.pos.hair.customer.MstCustomerPanel;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.Cursor;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

/**
 *
 * @author nakhoa
 */
public class ProductDeliveryManagementPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    /**
     * Creates new form ProductDeliveryManagementPanel
     */
    public ProductDeliveryManagementPanel() {
        initComponents();
        this.setSize(833, 691);
        this.setPath("¤iÇ");
        this.setTitle("¤iîzÇ");
        this.init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        NotShippjCheckBox1 = new javax.swing.JCheckBox();
        ShippjCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        searchCusButton = new javax.swing.JButton();
        customerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        customerName = new javax.swing.JTextField();
        clearButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbPeriodStart1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        productNameLabel6 = new javax.swing.JLabel();
        cmbPeriodEnd1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        mobileMembersScrollPane1 = new javax.swing.JScrollPane();
        searchProductDeliveryList = new com.geobeck.swing.JTableEx();
        outExcelButton = new javax.swing.JButton();
        checkAlljButton1 = new javax.swing.JButton();
        unCheckAlljButton2 = new javax.swing.JButton();
        updateStatusShipjButton3 = new javax.swing.JButton();
        updateStatusNoShipjButton4 = new javax.swing.JButton();

        jLabel1.setText("õð");

        NotShippjCheckBox1.setSelected(true);
        NotShippjCheckBox1.setText("¢­");

        ShippjCheckBox2.setText("­Ï");

        jLabel2.setText("ÚqNo.");

        searchCusButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
        searchCusButton.setBorderPainted(false);
        searchCusButton.setContentAreaFilled(false);
        searchCusButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
        searchCusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCusButtonActionPerformed(evt);
            }
        });

        customerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerNo.setColumns(15);
        customerNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNoFocusLost(evt);
            }
        });

        customerName.setEditable(false);

        clearButton.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
        clearButton.setBorderPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        searchButton.setIcon(SystemInfo.getImageIcon("/button/search/search_off.jpg"));
        searchButton.setBorderPainted(false);
        searchButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_on.jpg"));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("_ñú");

        cmbPeriodStart1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPeriodStart1.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        cmbPeriodStart1.setName("cmbPeriodStart1"); // NOI18N

        productNameLabel6.setText("   `");

        cmbPeriodEnd1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPeriodEnd1.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        cmbPeriodEnd1.setName("cmbPeriodEnd1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(cmbPeriodStart1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(productNameLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbPeriodEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(NotShippjCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ShippjCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(searchCusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addComponent(customerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbPeriodStart1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(NotShippjCheckBox1)
                                        .addComponent(ShippjCheckBox2))
                                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                        .addComponent(searchCusButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(customerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)))
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(productNameLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPeriodEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        mobileMembersScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        searchProductDeliveryList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "_ñú", "", "ÚqNo.", "Úq¼", "¤i¼", "Â", "óÔ", "`FbN"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchProductDeliveryList.setSelectionBackground(new java.awt.Color(255, 210, 142));
        searchProductDeliveryList.setSelectionForeground(new java.awt.Color(0, 0, 0));
        searchProductDeliveryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchProductDeliveryList.getTableHeader().setReorderingAllowed(false);
        this.initTableColumnWidth();
        SwingUtil.setJTableHeaderRenderer(searchProductDeliveryList, SystemInfo.getTableHeaderRenderer());
        searchProductDeliveryList.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(searchProductDeliveryList);
        searchProductDeliveryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchProductDeliveryListMouseClicked(evt);
            }
        });
        mobileMembersScrollPane1.setViewportView(searchProductDeliveryList);

        outExcelButton.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        outExcelButton.setBorderPainted(false);
        outExcelButton.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        outExcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outExcelButtonActionPerformed(evt);
            }
        });

        checkAlljButton1.setIcon(SystemInfo.getImageIcon("/button/select/select_all_off.jpg"));
        checkAlljButton1.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_all_on.jpg"));
        checkAlljButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAlljButton1ActionPerformed(evt);
            }
        });

        unCheckAlljButton2.setIcon(SystemInfo.getImageIcon("/button/select/release_all_off.jpg"));
        unCheckAlljButton2.setPressedIcon(SystemInfo.getImageIcon("/button/select/release_all_on.jpg"));
        unCheckAlljButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unCheckAlljButton2ActionPerformed(evt);
            }
        });

        updateStatusShipjButton3.setIcon(SystemInfo.getImageIcon("/button/select/change_dispatch_off.jpg"));
        updateStatusShipjButton3.setPressedIcon(SystemInfo.getImageIcon("/button/select/change_dispatch_on.jpg"));
        updateStatusShipjButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStatusShipjButton3ActionPerformed(evt);
            }
        });

        updateStatusNoShipjButton4.setIcon(SystemInfo.getImageIcon("/button/select/change_undispatch_off.jpg"));
        updateStatusNoShipjButton4.setPressedIcon(SystemInfo.getImageIcon("/button/select/change_undispatch_on.jpg"));
        updateStatusNoShipjButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStatusNoShipjButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(outExcelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkAlljButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unCheckAlljButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(updateStatusNoShipjButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateStatusShipjButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(mobileMembersScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(outExcelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(updateStatusShipjButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateStatusNoShipjButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkAlljButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unCheckAlljButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2)
                .addComponent(mobileMembersScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchCusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCusButtonActionPerformed
        try {
            SystemInfo.getLogger().log(Level.INFO, "Úqõ");
            SearchCustomerDialog		sc	=	new SearchCustomerDialog(parentFrame, true);
            sc.setShopID(SystemInfo.getCurrentShop().getShopID());
            sc.setVisible(true);
            if(sc.getSelectedCustomer() != null && !"".equals(sc.getSelectedCustomer().getCustomerID()))
            {
                mstCustomer = sc.getSelectedCustomer();
                this.showData(mstCustomer);
                customerName.requestFocusInWindow();
            }
            sc.dispose();
            sc = null;
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_searchCusButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        this.clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if(checkInput()){
            searchButton.setCursor(null);
            ShowProductDeliveryList();
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchProductDeliveryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchProductDeliveryListMouseClicked

    }//GEN-LAST:event_searchProductDeliveryListMouseClicked

    private void outExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outExcelButtonActionPerformed
        outExcelButton.setCursor(null);
        reportOutPut();
    }//GEN-LAST:event_outExcelButtonActionPerformed

    private void checkAlljButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAlljButton1ActionPerformed
        CheckAll(true);
    }//GEN-LAST:event_checkAlljButton1ActionPerformed

    private void unCheckAlljButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unCheckAlljButton2ActionPerformed
        CheckAll(false);
    }//GEN-LAST:event_unCheckAlljButton2ActionPerformed

    private void updateStatusShipjButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStatusShipjButton3ActionPerformed
        if(searchProductDeliveryList.getRowCount()==0) return;
        if(checkInput()){
            boolean result = updateStatus(2);
            if(!result){
                MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "¤iîzÇ"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            }else{
                MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS, "¤iîzÇ"),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                

            }
            ShowProductDeliveryList();
            
        }
    }//GEN-LAST:event_updateStatusShipjButton3ActionPerformed

    private void updateStatusNoShipjButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStatusNoShipjButton4ActionPerformed
        if(searchProductDeliveryList.getRowCount()==0) return;
        if(checkInput()){
            boolean result = updateStatus(1);
            if(!result){
                MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "¤iîzÇ"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            }else{
                MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS, "¤iîzÇ"),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);

            }
            ShowProductDeliveryList();
        }
    }//GEN-LAST:event_updateStatusNoShipjButton4ActionPerformed

    private void customerNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNoFocusLost
        MstCustomer cus = this.mstCustomer;
        boolean isChangedCustomerNo = !customerNo.getText().equals(cus.getCustomerNo());
        cus.setCustomerNo(customerNo.getText());
        if(cus.getCustomerNo().equals("0"))
        {
                cus = new MstCustomer();
                this.customerName.setEditable(true);
                this.customerName.requestFocusInWindow();
        }
        else
        {
                //ÚqNoðZbg·éB
                cus.setCustomerNo(this.customerNo.getText());
                ConnectionWrapper con = SystemInfo.getConnection();
                try {
                    if (isChangedCustomerNo) {
                        cus = SelectSameNoData.getMstCustomerByNo(
                                parentFrame,
                                SystemInfo.getConnection(),
                                this.customerNo.getText(),
                                (SystemInfo.getSetteing().isShareCustomer() ? 0 :SystemInfo.getCurrentShop().getShopID()));
                    } else {
                        cus = new MstCustomer(cus.getCustomerID());
                        cus.load(con);
                    }
                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
                if (cus == null) cus = new MstCustomer();
                this.customerName.setEditable(false);
        }
        this.mstCustomer = cus;
        showData(cus);
    }//GEN-LAST:event_customerNoFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox NotShippjCheckBox1;
    private javax.swing.JCheckBox ShippjCheckBox2;
    private javax.swing.JButton checkAlljButton1;
    private javax.swing.JButton clearButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbPeriodEnd1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbPeriodStart1;
    private javax.swing.JTextField customerName;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane mobileMembersScrollPane1;
    private javax.swing.JButton outExcelButton;
    private javax.swing.JLabel productNameLabel6;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton searchCusButton;
    private com.geobeck.swing.JTableEx searchProductDeliveryList;
    private javax.swing.JButton unCheckAlljButton2;
    private javax.swing.JButton updateStatusNoShipjButton4;
    private javax.swing.JButton updateStatusShipjButton3;
    // End of variables declaration//GEN-END:variables

    private     MstCustomer             mstCustomer     =       new MstCustomer();
    private     ArrayList<MstProductDeliveryManagement> list = new ArrayList<MstProductDeliveryManagement>();
    private     MstProductDeliveryManagement    pdm = new MstProductDeliveryManagement();
    /**
    * ú»ðs¤B
    */
    private void init()
    {
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        d.setMonth(d.getMonth() - 2);
        this.cmbPeriodStart1.setDate(d);
        this.cmbPeriodEnd1.setDate(new Date());
        this.clear();
        
    }
    
    private void initTableColumnWidth(){
        searchProductDeliveryList .getColumnModel().getColumn(0).setPreferredWidth(80);
        searchProductDeliveryList .getColumnModel().getColumn(1).setPreferredWidth(60);
        searchProductDeliveryList .getColumnModel().getColumn(2).setPreferredWidth(60);
        searchProductDeliveryList .getColumnModel().getColumn(3).setPreferredWidth(120);
        searchProductDeliveryList .getColumnModel().getColumn(4).setPreferredWidth(180);
        searchProductDeliveryList .getColumnModel().getColumn(5).setPreferredWidth(90);
        searchProductDeliveryList .getColumnModel().getColumn(6).setPreferredWidth(90);
        searchProductDeliveryList .getColumnModel().getColumn(7).setPreferredWidth(70);
    }

    private void clear(){
        customerNo.setText("");
        customerName.setText("");
        this.ShippjCheckBox2.setSelected(false);
        this.NotShippjCheckBox1.setSelected(true);
        SwingUtil.clearTable(searchProductDeliveryList);
    }
        
    private void showData(MstCustomer cus){
        if(cus == null) return;
        customerNo.setText(cus.getCustomerNo());
        StringBuilder cusName = new StringBuilder();
        cusName.append(cus.getCustomerName(0));
        cusName.append(" ");
        cusName.append(cus.getCustomerName(1));
        customerName.setText(cusName.toString());
    }
    
    /**
    * [Uõ{^ðæ¾·é
    */
    private JButton getUserSearchButton(final Integer customerID)
    {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                            "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
        button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                            "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));
        button.setSize(48, 25);

        button.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MstCustomerPanel mcp = null;

                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    mcp = new MstCustomerPanel(customerID, true, true);
                    SwingUtil.openAnchorDialog( (JFrame)null, true, mcp, "Úqîño^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                mcp = null;
            }
        });

        return button;
    }
    
    private String getStatusText(int status){
        if(status == 2){
            return "­Ï";
        }
        return "¢­";
    }
    
    private boolean getStatusCheckbox(int status){
        if(status == 1 || status == 2){
            return true;
        }else{
            return false;
        }
    }
    
    private String getCustomerName(MstCustomer cus){
        if(cus == null) return "";
        StringBuilder cusName = new StringBuilder();
        cusName.append(cus.getCustomerName(0));
        cusName.append(" ");
        cusName.append(cus.getCustomerName(1));
        return cusName.toString();
    }
    
    private String getDate(java.util.Date date){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        if(date != null){
            return df.format(date);
        }else{
            return "";
        }
    }
    
    private void CheckAll(boolean flag){
        DefaultTableModel model = (DefaultTableModel)searchProductDeliveryList.getModel();
        for(int i = 0;i< model.getRowCount();i++){
            model.setValueAt(flag, i, 7);
        }
    }
    
    private void ShowProductDeliveryList(){
        ConnectionWrapper con   = SystemInfo.getConnection();
        try{
            int flag = 3;
            if(this.NotShippjCheckBox1.isSelected() && !this.ShippjCheckBox2.isSelected()){
                flag = 1;
            }
            if(this.ShippjCheckBox2.isSelected()&& !this.NotShippjCheckBox1.isSelected()){
                flag = 2;
            }
            pdm.setStartDate(this.cmbPeriodStart1.getDate());
            pdm.setEndDate(this.cmbPeriodEnd1.getDate());
            if("".equals(this.customerNo.getText().trim())){
                pdm.setCustomer(null);
            }else{
                pdm.setCustomer(mstCustomer);
            }
            list = pdm.load(con, flag);
            
            DefaultTableModel model = (DefaultTableModel)searchProductDeliveryList.getModel();
            if( searchProductDeliveryList.getCellEditor() != null ) searchProductDeliveryList.getCellEditor().stopCellEditing();
            model.setRowCount(0);
            if(list != null && list.size() > 0){
                for(MstProductDeliveryManagement mpdm : list){
                   Object[] rowData = {
                       getDate(mpdm.getSalesDate()),
                       getUserSearchButton(mpdm.getCustomer().getCustomerID()),
                       mpdm.getCustomer().getCustomerNo(),
                       getCustomerName(mpdm.getCustomer()),
                       mpdm.getProductName(),
                       mpdm.getProductNum(),
                       getStatusText(mpdm.getStatus()),
                       getStatusCheckbox(mpdm.getStatus())
                   };
                   model.addRow(rowData);
                }
            }
            con.close();
        }catch(Exception ex){
            Logger.getLogger(MstCustomerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean updateStatus(Integer status){
        MstProductDeliveryManagement mpdm = new MstProductDeliveryManagement();
        Boolean ischeck ;
        boolean flag = true;
        ConnectionWrapper con   = SystemInfo.getConnection();
        try{
            con.begin();
            for(int i=0;i<searchProductDeliveryList.getRowCount();i++){
                mpdm = new MstProductDeliveryManagement();
                ischeck = (Boolean)searchProductDeliveryList.getValueAt(i, 7);
                if(ischeck == true){
                    mpdm = list.get(i);
                    flag = mpdm.updateStatus(con, status);
                    if(!flag){
                        con.rollback();
                        break;
                    }
                }
            }
            if(flag == true){
                con.commit();
                return true;
            }else{
                return false;
            }
        }catch(Exception ex){
            Logger.getLogger(MstCustomerPanel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
    * |[goÍðs¤
    */
    private void reportOutPut()
    {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //Úq
                this.productDeliveryOutPut();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void productDeliveryOutPut(){
        if(this.searchProductDeliveryList.getRowCount() == 0){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JExcelApi jx = new JExcelApi("¤iîzÇ");
        jx.setTemplateFile("/report/¤iîzÇê.xls");
        String header="¤iîzÇ";
        
        jx.setValue(3, 3, header);
        int outRow = 6;
        // ÇÁsZbg
        jx.insertRow(outRow, searchProductDeliveryList.getRowCount() - 1);
        
        DefaultTableModel model = (DefaultTableModel)searchProductDeliveryList.getModel();
        for (int row = 0; row < searchProductDeliveryList.getRowCount(); row++) {
            jx.setValue(1, outRow, model.getValueAt(row, 0));
            for (int col = 2; col < searchProductDeliveryList.getColumnCount() - 1; col++) {
                jx.setValue(col, outRow, model.getValueAt(row, col));
            }
            outRow++;
        }
        jx.openWorkbook();
    }
    
    private boolean checkInput(){
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        if(this.cmbPeriodStart1.getDate() != null && this.cmbPeriodEnd1.getDate() != null ){
            start.setTime(this.cmbPeriodStart1.getDate());
            end.setTime(this.cmbPeriodEnd1.getDate());
            if (start.compareTo(end) != 0) 
            {
                if (start.after(end)) 
                {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "_ñú"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    cmbPeriodStart1.requestFocusInWindow();
                    return false;
                }
            }
        }
        return true;
    }
}

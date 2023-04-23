/* IPOPanel.java
 * Created on 2012/10/30, 9:00
 */
package com.geobeck.sosia.pos.hair.report;

import java.text.DecimalFormat;
import javax.swing.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.report.bean.*;
import javax.swing.text.PlainDocument;

/**
 * îNä‘à⁄ìÆÉOÉâÉt
 *
 * @author ivs
 */
public final class IPOPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        analysisTypeGroup = new javax.swing.ButtonGroup();
        rangeGroup = new javax.swing.ButtonGroup();
        lblActiveCustomers = new javax.swing.JLabel();
        txtActiveCustomers = new JFormattedTextField(new DecimalFormat("#,###"));
        lblShop = new javax.swing.JLabel();
        lblMask = new javax.swing.JLabel();
        lblNeedsAttention3 = new javax.swing.JLabel();
        lblNeedsAttention = new javax.swing.JLabel();
        lbLostCustomers = new javax.swing.JLabel();
        lblMask1 = new javax.swing.JLabel();
        txtNeedsAttention2 = new JFormattedTextField(new DecimalFormat("#,###"));
        ((PlainDocument)txtNeedsAttention2.getDocument()).setDocumentFilter(new CustomFilter(2, CustomFilter.NUMBER));
        lblMonth2 = new javax.swing.JLabel();
        lblMask2 = new javax.swing.JLabel();
        txtCustomerRisk2 = new JFormattedTextField(new DecimalFormat("#,###"));
        ((PlainDocument)txtCustomerRisk2.getDocument()).setDocumentFilter(new CustomFilter(2, CustomFilter.NUMBER));
        lblMonth5 = new javax.swing.JLabel();
        lblMonth1 = new javax.swing.JLabel();
        txtNeedsAttention1 = new JFormattedTextField(new DecimalFormat("#,###"));
        ((PlainDocument)txtNeedsAttention1.getDocument()).setDocumentFilter(new CustomFilter(2, CustomFilter.NUMBER));
        lblMonth4 = new javax.swing.JLabel();
        txtCustomerRisk1 = new JFormattedTextField(new DecimalFormat("#,###"));
        lblMonth6 = new javax.swing.JLabel();
        txtLostCustomers = new JFormattedTextField(new DecimalFormat("#,###"));
        renewButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(500, 500));
        setMinimumSize(new java.awt.Dimension(100, 100));

        lblActiveCustomers.setText("ÉAÉNÉeÉBÉuå⁄ãq");

        txtActiveCustomers.setEditable(false);
        txtActiveCustomers.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtActiveCustomers.setEnabled(false);
        txtActiveCustomers.setVerifyInputWhenFocusTarget(false);

        lblShop.setText("Éñåé ");

        lblMask.setText("Å`");
        lblMask.setFocusCycleRoot(true);

        lblNeedsAttention3.setText("äÎåØå⁄ãq ");

        lblNeedsAttention.setText("óvíçà”å⁄ãq");

        lbLostCustomers.setText("é∏ãqå⁄ãq ");

        lblMask1.setText("Å`");
        lblMask1.setFocusCycleRoot(true);

        txtNeedsAttention2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtNeedsAttention2.setVerifyInputWhenFocusTarget(false);
        txtNeedsAttention2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtNeedsAttention2CaretUpdate(evt);
            }
        });

        lblMonth2.setText("Éñåé ");

        lblMask2.setText("Å`");
        lblMask2.setFocusCycleRoot(true);

        txtCustomerRisk2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtCustomerRisk2.setVerifyInputWhenFocusTarget(false);
        txtCustomerRisk2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtCustomerRisk2CaretUpdate(evt);
            }
        });

        lblMonth5.setText("Éñåé ");

        lblMonth1.setText("Éñåé ");

        txtNeedsAttention1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtNeedsAttention1.setVerifyInputWhenFocusTarget(false);
        txtNeedsAttention1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtNeedsAttention1CaretUpdate(evt);
            }
        });

        lblMonth4.setText("Éñåé ");

        txtCustomerRisk1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtCustomerRisk1.setEnabled(false);
        txtCustomerRisk1.setVerifyInputWhenFocusTarget(false);

        lblMonth6.setText("Éñåéà»è„ ");

        txtLostCustomers.setEditable(false);
        txtLostCustomers.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtLostCustomers.setEnabled(false);
        txtLostCustomers.setVerifyInputWhenFocusTarget(false);

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
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
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblActiveCustomers)
                            .add(lblNeedsAttention)
                            .add(lblNeedsAttention3))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(txtNeedsAttention1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(lblMonth1))
                                    .add(layout.createSequentialGroup()
                                        .add(txtLostCustomers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(lblMonth6))
                                    .add(layout.createSequentialGroup()
                                        .add(txtCustomerRisk1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(lblMonth4))))
                            .add(layout.createSequentialGroup()
                                .add(131, 131, 131)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblMask1)
                                    .add(lblMask)
                                    .add(lblMask2))))
                        .add(32, 32, 32)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(txtActiveCustomers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(lblShop)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(txtNeedsAttention2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(lblMonth2))
                                    .add(layout.createSequentialGroup()
                                        .add(txtCustomerRisk2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(lblMonth5)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 46, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                    .add(lbLostCustomers))
                .add(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(29, 29, 29)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtActiveCustomers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(1, 1, 1)
                        .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(15, 15, 15)
                        .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblActiveCustomers)
                            .add(lblMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(55, 55, 55)
                                .add(lblNeedsAttention3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(lbLostCustomers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(16, 16, 16)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(txtNeedsAttention2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblMonth2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(lblMask1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(txtNeedsAttention1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblMonth1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblNeedsAttention, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(txtCustomerRisk2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblMonth5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(lblMask2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(txtCustomerRisk1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblMonth4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(txtLostCustomers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblMonth6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed

        if (this.txtNeedsAttention1.getText().length() == 0) {
            MessageDialog.showMessageDialog(
                this,
                "ä˙ä‘Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE);
            this.txtNeedsAttention1.requestFocusInWindow();
            return;
        }

        if (this.txtNeedsAttention2.getText().length() == 0) {
            MessageDialog.showMessageDialog(
                this,
                "ä˙ä‘Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE);
            this.txtNeedsAttention2.requestFocusInWindow();
            return;
        }
        if (this.txtCustomerRisk2.getText().length() == 0) {
            MessageDialog.showMessageDialog(
                this,
                "ä˙ä‘Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE);
            this.txtCustomerRisk2.requestFocusInWindow();
            return;
        }
        int ret = MessageDialog.showYesNoDialog(
            this,
            MessageUtil.getMessage(103),
            this.getTitle(),
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.NO_OPTION);

        if (ret == JOptionPane.YES_OPTION) {
            this.regist();
            this.close();
        }
    }//GEN-LAST:event_renewButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void txtNeedsAttention1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtNeedsAttention1CaretUpdate
        this.txtActiveCustomers.setText(this.txtNeedsAttention1.getText());
    }//GEN-LAST:event_txtNeedsAttention1CaretUpdate

    private void txtNeedsAttention2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtNeedsAttention2CaretUpdate
        this.txtCustomerRisk1.setText(this.txtNeedsAttention2.getText());
    }//GEN-LAST:event_txtNeedsAttention2CaretUpdate

    private void txtCustomerRisk2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtCustomerRisk2CaretUpdate
        this.txtLostCustomers.setText(this.txtCustomerRisk2.getText());
    }//GEN-LAST:event_txtCustomerRisk2CaretUpdate
    private ReportParameterBean paramBean = new ReportParameterBean();
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup analysisTypeGroup;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel lbLostCustomers;
    private javax.swing.JLabel lblActiveCustomers;
    private javax.swing.JLabel lblMask;
    private javax.swing.JLabel lblMask1;
    private javax.swing.JLabel lblMask2;
    private javax.swing.JLabel lblMonth1;
    private javax.swing.JLabel lblMonth2;
    private javax.swing.JLabel lblMonth4;
    private javax.swing.JLabel lblMonth5;
    private javax.swing.JLabel lblMonth6;
    private javax.swing.JLabel lblNeedsAttention;
    private javax.swing.JLabel lblNeedsAttention3;
    private javax.swing.JLabel lblShop;
    private javax.swing.ButtonGroup rangeGroup;
    private javax.swing.JButton renewButton;
    private javax.swing.JFormattedTextField txtActiveCustomers;
    private javax.swing.JFormattedTextField txtCustomerRisk1;
    private javax.swing.JFormattedTextField txtCustomerRisk2;
    private javax.swing.JFormattedTextField txtLostCustomers;
    private javax.swing.JFormattedTextField txtNeedsAttention1;
    private javax.swing.JFormattedTextField txtNeedsAttention2;
    // End of variables declaration//GEN-END:variables

    public IPOPanel() {
        initComponents();
        this.setSize(505, 204);
        this.setPath("ä˙ä‘ê›íË ");
        this.setTitle("ä˙ä‘ê›íË");
        SystemInfo.addMouseCursorChange(closeButton);
        SystemInfo.addMouseCursorChange(renewButton);
        init();
    }
    public void setParaBean(ReportParameterBean paramBean)
    {
        this.paramBean=paramBean;
    }
    public ReportParameterBean getParaBean()
    {
        return this.paramBean;
    }
    public void init()
    {
        setKeyListener();
        MstShopSetting mss = new MstShopSetting(true);
        this.txtNeedsAttention1.setText(mss.getValidCustomerPeriod1().toString());
        this.txtNeedsAttention2.setText(mss.getValidCustomerPeriod2().toString());
        this.txtCustomerRisk2.setText(mss.getValidCustomerPeriod3().toString());
    }            
 
     private void regist() {
        MstShopSetting mss = new MstShopSetting(true);
        mss.setValidCustomerPeriod1(Integer.valueOf(txtNeedsAttention1.getText()));
        mss.setValidCustomerPeriod2(Integer.valueOf(txtNeedsAttention2.getText()));
        mss.setValidCustomerPeriod3(Integer.valueOf(txtCustomerRisk2.getText()));
        mss.regist1();
    }
   /**
     * É_ÉCÉAÉçÉOÇï¬Ç∂ÇÈ
     */
    private void close() {
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }
    
     private void setKeyListener() {
        this.txtNeedsAttention1.addKeyListener(SystemInfo.getMoveNextField());
        this.txtNeedsAttention1.addFocusListener(SystemInfo.getSelectText());
        this.txtNeedsAttention2.addKeyListener(SystemInfo.getMoveNextField());
        this.txtNeedsAttention2.addFocusListener(SystemInfo.getSelectText());
        this.txtCustomerRisk2.addKeyListener(SystemInfo.getMoveNextField());
        this.txtCustomerRisk2.addFocusListener(SystemInfo.getSelectText());
    }

}

  



   
  
   


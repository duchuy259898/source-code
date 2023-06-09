/*
 * CrossAnalysisPanel.java
 *
 * Created on 2011/06/12, 11:00
 */

package com.geobeck.sosia.pos.basicinfo.customer;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.SQLUtil;

/**
 *
 * @author geobeck
 */
public class MstCustomerRankSettingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    private	MstCustomerRankSettingFocusTraversalPolicy	ftp	=
			new MstCustomerRankSettingFocusTraversalPolicy();
        /** Creates new form StaffShopRankingPanel */
	public MstCustomerRankSettingPanel()
	{
            initComponents();
            addMouseCursorChange();
            this.setSize(820,680);
            this.setTitle("顧客ランク設定");
            this.setKeyListener();

            //初期化処理
            this.init();
	}
        
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
            SystemInfo.addMouseCursorChange(renewButton);
	}
        
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taxGroup = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        lblTargetPeriod = new javax.swing.JLabel();
        startDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        endDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel1 = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        lblTax1 = new javax.swing.JLabel();
        countFrom2 = new javax.swing.JTextField();
        ((PlainDocument)countFrom2.getDocument()).setDocumentFilter(new CustomFilter(4, CustomFilter.NUMERIC));
        rankName5 = new javax.swing.JLabel();
        lblTax2 = new javax.swing.JLabel();
        countFrom1 = new javax.swing.JTextField();
        ((PlainDocument)countFrom1.getDocument()).setDocumentFilter(new CustomFilter(4, CustomFilter.NUMERIC));
        valueFrom1 = new javax.swing.JTextField();
        ((PlainDocument)valueFrom1.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        lblTax10 = new javax.swing.JLabel();
        valueFrom2 = new javax.swing.JTextField();
        ((PlainDocument)valueFrom2.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        lblTax17 = new javax.swing.JLabel();
        lblTax18 = new javax.swing.JLabel();
        rankName1 = new javax.swing.JLabel();
        rankName2 = new javax.swing.JLabel();
        rankName3 = new javax.swing.JLabel();
        rankName4 = new javax.swing.JLabel();
        lblTax23 = new javax.swing.JLabel();
        lblTax24 = new javax.swing.JLabel();
        countFrom3 = new javax.swing.JTextField();
        ((PlainDocument)countFrom3.getDocument()).setDocumentFilter(new CustomFilter(4, CustomFilter.NUMERIC));
        lblTax4 = new javax.swing.JLabel();
        countFrom4 = new javax.swing.JTextField();
        ((PlainDocument)countFrom4.getDocument()).setDocumentFilter(new CustomFilter(4, CustomFilter.NUMERIC));
        lblTax5 = new javax.swing.JLabel();
        lblTax6 = new javax.swing.JLabel();
        countFrom5 = new javax.swing.JTextField();
        ((PlainDocument)countFrom5.getDocument()).setDocumentFilter(new CustomFilter(4, CustomFilter.NUMERIC));
        valueFrom3 = new javax.swing.JTextField();
        ((PlainDocument)valueFrom3.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        lblTax11 = new javax.swing.JLabel();
        valueFrom4 = new javax.swing.JTextField();
        ((PlainDocument)valueFrom4.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        lblTax12 = new javax.swing.JLabel();
        lblTax13 = new javax.swing.JLabel();
        valueFrom5 = new javax.swing.JTextField();
        ((PlainDocument)valueFrom5.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        renewButton = new javax.swing.JButton();

        setFocusCycleRoot(true);
        setOpaque(false);

        pnlMain.setOpaque(false);
        pnlMain.setLayout(null);

        lblTargetPeriod.setText("算出期間");
        lblTargetPeriod.setFocusCycleRoot(true);
        pnlMain.add(lblTargetPeriod);
        lblTargetPeriod.setBounds(20, 20, 60, 23);

        startDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        startDate.setFocusCycleRoot(true);
        startDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                startDateFocusGained(evt);
            }
        });
        pnlMain.add(startDate);
        startDate.setBounds(80, 20, 90, 23);

        endDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        endDate.setFocusCycleRoot(true);
        endDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                endDateFocusGained(evt);
            }
        });
        pnlMain.add(endDate);
        endDate.setBounds(190, 20, 90, 23);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("〜");
        jLabel1.setFocusCycleRoot(true);
        pnlMain.add(jLabel1);
        jLabel1.setBounds(170, 20, 20, 23);

        lblTax.setText("税区分");
        lblTax.setFocusCycleRoot(true);
        pnlMain.add(lblTax);
        lblTax.setBounds(20, 50, 60, 20);

        taxGroup.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("税込");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setFocusCycleRoot(true);
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);
        pnlMain.add(rdoTaxUnit);
        rdoTaxUnit.setBounds(80, 50, 50, 23);

        taxGroup.add(rdoTaxBlank);
        rdoTaxBlank.setText("税抜");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setFocusCycleRoot(true);
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);
        pnlMain.add(rdoTaxBlank);
        rdoTaxBlank.setBounds(140, 50, 50, 23);

        lblTax1.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        lblTax1.setText("売上金額");
        lblTax1.setFocusCycleRoot(true);
        pnlMain.add(lblTax1);
        lblTax1.setBounds(220, 100, 60, 23);

        countFrom2.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        countFrom2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        countFrom2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        countFrom2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                countFrom2FocusLost(evt);
            }
        });
        pnlMain.add(countFrom2);
        countFrom2.setBounds(110, 160, 50, 23);

        rankName5.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        rankName5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rankName5.setText("E");
        rankName5.setFocusCycleRoot(true);
        pnlMain.add(rankName5);
        rankName5.setBounds(60, 250, 30, 23);

        lblTax2.setText("回以上");
        lblTax2.setFocusCycleRoot(true);
        pnlMain.add(lblTax2);
        lblTax2.setBounds(170, 160, 43, 23);

        countFrom1.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        countFrom1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        countFrom1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        countFrom1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                countFrom1FocusLost(evt);
            }
        });
        pnlMain.add(countFrom1);
        countFrom1.setBounds(110, 130, 50, 23);

        valueFrom1.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        valueFrom1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valueFrom1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        valueFrom1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valueFrom1FocusLost(evt);
            }
        });
        pnlMain.add(valueFrom1);
        valueFrom1.setBounds(220, 130, 60, 23);

        lblTax10.setText("円以上");
        lblTax10.setFocusCycleRoot(true);
        pnlMain.add(lblTax10);
        lblTax10.setBounds(290, 160, 43, 23);

        valueFrom2.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        valueFrom2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valueFrom2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        valueFrom2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valueFrom2FocusLost(evt);
            }
        });
        pnlMain.add(valueFrom2);
        valueFrom2.setBounds(220, 160, 60, 23);

        lblTax17.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        lblTax17.setText("ランク");
        lblTax17.setFocusCycleRoot(true);
        pnlMain.add(lblTax17);
        lblTax17.setBounds(60, 100, 30, 23);

        lblTax18.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        lblTax18.setText("来店回数");
        lblTax18.setFocusCycleRoot(true);
        pnlMain.add(lblTax18);
        lblTax18.setBounds(110, 100, 60, 23);

        rankName1.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        rankName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rankName1.setText("A");
        rankName1.setFocusCycleRoot(true);
        pnlMain.add(rankName1);
        rankName1.setBounds(60, 130, 30, 23);

        rankName2.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        rankName2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rankName2.setText("B");
        rankName2.setFocusCycleRoot(true);
        pnlMain.add(rankName2);
        rankName2.setBounds(60, 160, 30, 23);

        rankName3.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        rankName3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rankName3.setText("C");
        rankName3.setFocusCycleRoot(true);
        pnlMain.add(rankName3);
        rankName3.setBounds(60, 190, 30, 23);

        rankName4.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        rankName4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rankName4.setText("D");
        rankName4.setFocusCycleRoot(true);
        pnlMain.add(rankName4);
        rankName4.setBounds(60, 220, 30, 23);

        lblTax23.setText("回以上");
        lblTax23.setFocusCycleRoot(true);
        pnlMain.add(lblTax23);
        lblTax23.setBounds(170, 130, 43, 23);

        lblTax24.setText("円以上");
        lblTax24.setFocusCycleRoot(true);
        pnlMain.add(lblTax24);
        lblTax24.setBounds(290, 130, 43, 23);

        countFrom3.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        countFrom3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        countFrom3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        countFrom3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                countFrom3FocusLost(evt);
            }
        });
        pnlMain.add(countFrom3);
        countFrom3.setBounds(110, 190, 50, 23);

        lblTax4.setText("回以上");
        lblTax4.setFocusCycleRoot(true);
        pnlMain.add(lblTax4);
        lblTax4.setBounds(170, 190, 43, 23);

        countFrom4.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        countFrom4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        countFrom4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        countFrom4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                countFrom4FocusLost(evt);
            }
        });
        pnlMain.add(countFrom4);
        countFrom4.setBounds(110, 220, 50, 23);

        lblTax5.setText("回以上");
        lblTax5.setFocusCycleRoot(true);
        pnlMain.add(lblTax5);
        lblTax5.setBounds(170, 220, 43, 23);

        lblTax6.setText("回以上");
        lblTax6.setFocusCycleRoot(true);
        pnlMain.add(lblTax6);
        lblTax6.setBounds(170, 250, 43, 23);

        countFrom5.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        countFrom5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        countFrom5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        countFrom5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                countFrom5FocusLost(evt);
            }
        });
        pnlMain.add(countFrom5);
        countFrom5.setBounds(110, 250, 50, 23);

        valueFrom3.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        valueFrom3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valueFrom3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        valueFrom3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valueFrom3FocusLost(evt);
            }
        });
        pnlMain.add(valueFrom3);
        valueFrom3.setBounds(220, 190, 60, 23);

        lblTax11.setText("円以上");
        lblTax11.setFocusCycleRoot(true);
        pnlMain.add(lblTax11);
        lblTax11.setBounds(290, 190, 43, 23);

        valueFrom4.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        valueFrom4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valueFrom4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        valueFrom4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valueFrom4FocusLost(evt);
            }
        });
        pnlMain.add(valueFrom4);
        valueFrom4.setBounds(220, 220, 60, 23);

        lblTax12.setText("円以上");
        lblTax12.setFocusCycleRoot(true);
        pnlMain.add(lblTax12);
        lblTax12.setBounds(290, 220, 43, 23);

        lblTax13.setText("円以上");
        lblTax13.setFocusCycleRoot(true);
        pnlMain.add(lblTax13);
        lblTax13.setBounds(290, 250, 43, 23);

        valueFrom5.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 14)); // NOI18N
        valueFrom5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valueFrom5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        valueFrom5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valueFrom5FocusLost(evt);
            }
        });
        pnlMain.add(valueFrom5);
        valueFrom5.setBounds(220, 250, 60, 23);

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });
        pnlMain.add(renewButton);
        renewButton.setBounds(340, 20, 92, 25);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(pnlMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 671, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(pnlMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 334, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void valueFrom2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueFrom2FocusLost
            numberCheck(valueFrom2);
    }//GEN-LAST:event_valueFrom2FocusLost

    private void valueFrom1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueFrom1FocusLost
            numberCheck(valueFrom1);
    }//GEN-LAST:event_valueFrom1FocusLost

    private void countFrom2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_countFrom2FocusLost
        numberCheck(countFrom2);
    }//GEN-LAST:event_countFrom2FocusLost

    private void countFrom1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_countFrom1FocusLost
        numberCheck(countFrom1);
    }//GEN-LAST:event_countFrom1FocusLost
    
    private void regist(){

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update mst_system");
            sql.append(" set");
            if (startDate.getDate() != null) {
                sql.append(" rank_start_date = " + SQLUtil.convertForSQLDateOnly(startDate.getDate()) );
            } else {
                sql.append(" rank_start_date = null" );
            }
            if (endDate.getDate() != null) {
                sql.append(" ,rank_end_date = " + SQLUtil.convertForSQLDateOnly(endDate.getDate()) );
            } else {
                sql.append(" ,rank_end_date = null" );
            }
            if (rdoTaxUnit.isSelected()) {
                sql.append(" ,rank_tax_type = 0");
            } else {
                sql.append(" ,rank_tax_type = 1");
            }
            con.executeUpdate(sql.toString());

            con.executeUpdate("update mst_customer_rank_setting set count_from = " + countFrom1.getText() + ", value_from = " + valueFrom1.getText() + " where id = 1");
            con.executeUpdate("update mst_customer_rank_setting set count_from = " + countFrom2.getText() + ", value_from = " + valueFrom2.getText() + " where id = 2");
            con.executeUpdate("update mst_customer_rank_setting set count_from = " + countFrom3.getText() + ", value_from = " + valueFrom3.getText() + " where id = 3");
            con.executeUpdate("update mst_customer_rank_setting set count_from = " + countFrom4.getText() + ", value_from = " + valueFrom4.getText() + " where id = 4");
            con.executeUpdate("update mst_customer_rank_setting set count_from = " + countFrom5.getText() + ", value_from = " + valueFrom5.getText() + " where id = 5");

            con.commit();

            SystemInfo.getSetteing().load(con);
            
            //登録しました
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(201),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ignore) {
            }
        }
    }
    
	private void endDateFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_endDateFocusGained
	{//GEN-HEADEREND:event_endDateFocusGained
            endDate.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_endDateFocusGained

	private void startDateFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_startDateFocusGained
	{//GEN-HEADEREND:event_startDateFocusGained
            startDate.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_startDateFocusGained

        private void countFrom3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_countFrom3FocusLost
            numberCheck(countFrom3);
        }//GEN-LAST:event_countFrom3FocusLost

        private void countFrom4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_countFrom4FocusLost
            numberCheck(countFrom4);
        }//GEN-LAST:event_countFrom4FocusLost

        private void countFrom5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_countFrom5FocusLost
            numberCheck(countFrom5);
        }//GEN-LAST:event_countFrom5FocusLost

        private void valueFrom3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueFrom3FocusLost
            numberCheck(valueFrom3);
        }//GEN-LAST:event_valueFrom3FocusLost

        private void valueFrom4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueFrom4FocusLost
            numberCheck(valueFrom4);
        }//GEN-LAST:event_valueFrom4FocusLost

        private void valueFrom5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueFrom5FocusLost
            numberCheck(valueFrom5);
        }//GEN-LAST:event_valueFrom5FocusLost

        private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed
            if (inputCheck()) {
                this.regist();
            }
}//GEN-LAST:event_renewButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField countFrom1;
    private javax.swing.JTextField countFrom2;
    private javax.swing.JTextField countFrom3;
    private javax.swing.JTextField countFrom4;
    private javax.swing.JTextField countFrom5;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo endDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblTargetPeriod;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTax1;
    private javax.swing.JLabel lblTax10;
    private javax.swing.JLabel lblTax11;
    private javax.swing.JLabel lblTax12;
    private javax.swing.JLabel lblTax13;
    private javax.swing.JLabel lblTax17;
    private javax.swing.JLabel lblTax18;
    private javax.swing.JLabel lblTax2;
    private javax.swing.JLabel lblTax23;
    private javax.swing.JLabel lblTax24;
    private javax.swing.JLabel lblTax4;
    private javax.swing.JLabel lblTax5;
    private javax.swing.JLabel lblTax6;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JLabel rankName1;
    private javax.swing.JLabel rankName2;
    private javax.swing.JLabel rankName3;
    private javax.swing.JLabel rankName4;
    private javax.swing.JLabel rankName5;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private javax.swing.JButton renewButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo startDate;
    private javax.swing.ButtonGroup taxGroup;
    private javax.swing.JTextField valueFrom1;
    private javax.swing.JTextField valueFrom2;
    private javax.swing.JTextField valueFrom3;
    private javax.swing.JTextField valueFrom4;
    private javax.swing.JTextField valueFrom5;
    // End of variables declaration//GEN-END:variables
	
	private void setKeyListener()
	{
		endDate.addKeyListener(SystemInfo.getMoveNextField());
		endDate.addFocusListener(SystemInfo.getSelectText());
		startDate.addKeyListener(SystemInfo.getMoveNextField());
		startDate.addFocusListener(SystemInfo.getSelectText());
                
		countFrom1.addKeyListener(SystemInfo.getMoveNextField());
		countFrom1.addFocusListener(SystemInfo.getSelectText());
		countFrom2.addKeyListener(SystemInfo.getMoveNextField());
		countFrom2.addFocusListener(SystemInfo.getSelectText());
		countFrom3.addKeyListener(SystemInfo.getMoveNextField());
		countFrom3.addFocusListener(SystemInfo.getSelectText());
		countFrom4.addKeyListener(SystemInfo.getMoveNextField());
		countFrom4.addFocusListener(SystemInfo.getSelectText());
		countFrom5.addKeyListener(SystemInfo.getMoveNextField());
		countFrom5.addFocusListener(SystemInfo.getSelectText());
                
		valueFrom1.addKeyListener(SystemInfo.getMoveNextField());
		valueFrom1.addFocusListener(SystemInfo.getSelectText());
		valueFrom2.addKeyListener(SystemInfo.getMoveNextField());
		valueFrom2.addFocusListener(SystemInfo.getSelectText());
		valueFrom3.addKeyListener(SystemInfo.getMoveNextField());
		valueFrom3.addFocusListener(SystemInfo.getSelectText());
		valueFrom4.addKeyListener(SystemInfo.getMoveNextField());
		valueFrom4.addFocusListener(SystemInfo.getSelectText());
		valueFrom5.addKeyListener(SystemInfo.getMoveNextField());
		valueFrom5.addFocusListener(SystemInfo.getSelectText());
        }
	
	/**
	 * 初期化処理を行う。
	 */
	private void init()
	{
            startDate.setDate(SystemInfo.getSetteing().getRankStartDate());
            endDate.setDate(SystemInfo.getSetteing().getRankEndDate());
            if (SystemInfo.getSetteing().getRankTaxType() == 0) {
                rdoTaxUnit.setSelected(true);
            } else {
                rdoTaxBlank.setSelected(true);
            }

            try {
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery("select * from mst_customer_rank_setting order by id");
                while (rs.next()) {
                    switch(rs.getInt("id")) {
			case 1:
                            rankName1.setText(rs.getString("rank_name"));
                            countFrom1.setText(rs.getString("count_from"));
                            valueFrom1.setText(rs.getString("value_from"));
                            break;
			case 2:
                            rankName2.setText(rs.getString("rank_name"));
                            countFrom2.setText(rs.getString("count_from"));
                            valueFrom2.setText(rs.getString("value_from"));
                            break;
			case 3:
                            rankName3.setText(rs.getString("rank_name"));
                            countFrom3.setText(rs.getString("count_from"));
                            valueFrom3.setText(rs.getString("value_from"));
                            break;
			case 4:
                            rankName4.setText(rs.getString("rank_name"));
                            countFrom4.setText(rs.getString("count_from"));
                            valueFrom4.setText(rs.getString("value_from"));
                            break;
			case 5:
                            rankName5.setText(rs.getString("rank_name"));
                            countFrom5.setText(rs.getString("count_from"));
                            valueFrom5.setText(rs.getString("value_from"));
                            break;
                    }
                }
                rs.close();

            } catch (Exception e) {
            }
        }
        
        private void numberCheck(JTextField fld) {
            if (!CheckUtil.isNumber(fld.getText())) {
                fld.setText("0");
            }
        }

        private boolean inputCheck() {
/*
            try {
                    Long.parseLong(countFrom1.getText());
                } catch (Exception e) {

                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店回数設定のランク３"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    countFrom1.requestFocusInWindow();
                    return false;
                }
                
                try {
                    Long.parseLong(countFrom2.getText());
                } catch (Exception e) {
                    
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店回数設定のランク２"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    countFrom2.requestFocusInWindow();
                    return false;
                }
            
                try {
                    Long.parseLong(count1From.getText());
                } catch (Exception e) {
                    
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店回数設定のランク１"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    count1From.requestFocusInWindow();
                    return false;
                }

                try {
                    Long.parseLong(valueFrom1.getText());
                } catch (Exception e) {
                    
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "売上金額設定のランク３"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    valueFrom1.requestFocusInWindow();
                    return false;
                }

                try {
                    Long.parseLong(valueFrom2.getText());
                } catch (Exception e) {
                    
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "売上金額設定のランク２"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    valueFrom2.requestFocusInWindow();
                    return false;
                }

                try {
                    Long.parseLong(price1From.getText());
                } catch (Exception e) {
                    
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "売上金額設定のランク１"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    price1From.requestFocusInWindow();
                    return false;
                }
                
                
                boolean isChecked = false;
                isChecked = isChecked || chkValid1.isSelected();
                isChecked = isChecked || chkValid2.isSelected();
                isChecked = isChecked || chkValid3.isSelected();
                if (!isChecked) {
                    MessageDialog.showMessageDialog(
                        this,
                        "有効顧客のいずれかをチェックしてください。",
                        this.getTitle(),
                        JOptionPane.WARNING_MESSAGE);

                    return false;
                }
*/
            return true;
        }
        
        
        public MstCustomerRankSettingFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}

        private class MstCustomerRankSettingFocusTraversalPolicy
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
			if (aComponent.equals(startDate))
			{
				return endDate;
			}
                        else if (aComponent.equals(endDate))
			{
				if(rdoTaxUnit.isSelected()){
                                    return rdoTaxUnit;
                                }
                                return rdoTaxBlank;
			}
			else if ( aComponent.equals(rdoTaxUnit) || aComponent.equals(rdoTaxBlank) )
			{
				return countFrom1;
			}
			else if ( aComponent.equals(countFrom1)  )
			{
				return valueFrom1;
			}
                        else if ( aComponent.equals(valueFrom1)  )
			{
				return countFrom2;
			}
                         else if ( aComponent.equals(countFrom2)  )
			{
				return valueFrom2;
			}
                         else if ( aComponent.equals(valueFrom2)  )
			{
				return countFrom3;
			}
                          else if ( aComponent.equals(countFrom3)  )
			{
				return valueFrom3;
			}
                         else if ( aComponent.equals(valueFrom3)  )
			{
				return countFrom4;
			}
                         else if ( aComponent.equals(countFrom4)  )
			{
				return valueFrom4;
			}
                         else if ( aComponent.equals(valueFrom4)  )
			{
				return countFrom5;
			}
                        else if ( aComponent.equals(countFrom5)  )
			{
				return valueFrom5;
			}
			return startDate;
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
			if (aComponent.equals(startDate))
			{
				return startDate;
			}
                        else if (aComponent.equals(endDate))
			{
                                return startDate;
			}
			else if ( aComponent.equals(rdoTaxUnit) || aComponent.equals(rdoTaxBlank) )
			{
				return endDate;
			}
			else if ( aComponent.equals(countFrom1)  )
			{
				if(rdoTaxUnit.isSelected()){
                                    return rdoTaxUnit;
                                }
                                return rdoTaxBlank;
			}
                        else if ( aComponent.equals(valueFrom1)  )
			{
				return countFrom1;
			}
                         else if ( aComponent.equals(countFrom2)  )
			{
				return valueFrom1;
			}
                         else if ( aComponent.equals(valueFrom2)  )
			{
				return countFrom2;
			}
                          else if ( aComponent.equals(countFrom3)  )
			{
				return valueFrom2;
			}
                         else if ( aComponent.equals(valueFrom3)  )
			{
				return countFrom3;
			}
                         else if ( aComponent.equals(valueFrom4)  )
			{
				return countFrom4;
			}
                         else if ( aComponent.equals(countFrom5)  )
			{
				return valueFrom4;
			}
                        else if ( aComponent.equals(valueFrom5)  )
			{
				return countFrom5;
			}
			return startDate;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return startDate;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return endDate;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return startDate;
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
			return startDate;
		}
		
	}
        
}

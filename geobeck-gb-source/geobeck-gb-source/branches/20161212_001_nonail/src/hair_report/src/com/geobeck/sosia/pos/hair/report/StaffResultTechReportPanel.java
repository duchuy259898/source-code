/*
 * StaffResultTechReportPanel.java
 *
 * Created on 2008/06/14, 19:30
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.*;
import javax.swing.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.hair.report.logic.ReportLogic;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sql.ConnectionWrapper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  saito
 */
public class StaffResultTechReportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{       
        // start add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
	private boolean isLoading = true;
        // end add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
	/** Creates new form StaffResultTechReportPanel */
        private MstShopCategorys		mscg		=	new MstShopCategorys();
        private MstShopCategorys mrsRef = new MstShopCategorys();
        private MstShopCategorys mrsUse = new MstShopCategorys();
        private int useShopCategory = 0;
        private boolean isHideCategory = false;
	public StaffResultTechReportPanel()
	{
		initComponents();
		addMouseCursorChange();
		this.setSize(820, 650);
		this.setPath("���[�o��");
		this.setTitle("�S���ʋZ�p����");
		this.setKeyListener();

                target.addItem(SystemInfo.getGroup());
                SystemInfo.getGroup().addGroupDataToJComboBox(target, 3);
                if (!SystemInfo.isGroup()) {
                    target.setSelectedItem(SystemInfo.getCurrentShop());
                    // start add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
                     //IVS NNTUAN START EDIT 20131028
                    /*
                    if(SystemInfo.getCurrentShop().getCourseFlag()==0){
                        jPanel5.setVisible(false);
                        jPanel6.setVisible(false);
                    }*/
                    if(SystemInfo.getCurrentShop().getShopID() != 0 ) {
                        if(SystemInfo.getCurrentShop().getCourseFlag()==0){
                            jPanel5.setVisible(false);
                            jPanel6.setVisible(false);
                        }
                    }else {
                        jPanel5.setVisible(true);
                        jPanel6.setVisible(true);
                    }
                     //IVS NNTUAN START EDIT 20131028
                }else{
                    if(isLoading){
                        jPanel5.setVisible(false);
                        jPanel6.setVisible(false);
                    }
                    jPanel5.setVisible(false);
                    jPanel6.setVisible(false);
                    if (target.getSelectedItem() instanceof MstGroup) {
                        MstGroup group = new MstGroup();
                        group = (MstGroup) target.getSelectedItem();
                        //LVTu start add 2015/12/01 Bug Bug #44791
//                        for(MstShop shop :group.getShops()) {
//                            if(shop.getCourseFlag()==1) {
//                                jPanel5.setVisible(true);
//                                jPanel6.setVisible(true);
//                                //IVS_LVTu start edit 2015/11/04 Bug #44067
//                                //return;
//                                //IVS_LVTu end edit 2015/11/04 Bug #44067
//                            }
//                        }
                        if (checkShopCourseFlag(group)) {
                            jPanel5.setVisible(true);
                            jPanel6.setVisible(true);
                        }
                        //LVTu end add 2015/12/01 Bug Bug #44791
                    }
                }
                // end add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
                //�Ώۊ��Ԃ̐ݒ�y�Z�p����z
                this.cmbTargetPeriodStartDate.setDate(new Date());
		this.cmbTargetPeriodEndDate.setDate(new Date());
                //�Ώۊ��Ԃ̐ݒ�y�S���ʕ��ލ\����z
		this.cmbTargetPeriodStartDate1.setDate(new Date());
		this.cmbTargetPeriodEndDate1.setDate(new Date());
                //�Ώۊ��Ԃ̐ݒ�y�����сz
		this.cmbTargetPeriodStartDate2.setDate(new Date());
		this.cmbTargetPeriodEndDate2.setDate(new Date());
                //�Ώۊ��Ԃ̐ݒ�y�R�[�X�_�񐬐сz
                this.cmbTargetPeriodStartDate4.setDate(new Date());
                this.cmbTargetPeriodEndDate4.setDate(new Date());
                //�Ώۊ��Ԃ̐ݒ�y�R�[�X�������сz
                this.cmbTargetPeriodStartDate5.setDate(new Date());
                this.cmbTargetPeriodEndDate5.setDate(new Date());

		//�Ŕ��A�ō��̏����ݒ�
		if(SystemInfo.getAccountSetting().getReportPriceType() == 0)
		{
			rdoTaxBlank.setSelected(false);
			rdoTaxUnit.setSelected(true);
		}
		else
		{
			rdoTaxBlank.setSelected(true);
			rdoTaxUnit.setSelected(false);
		}
                // start add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
                isLoading = false;
                // end add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
	}
        
        //LVTu start add 2015/12/01 Bug Bug #44791
        private boolean checkShopCourseFlag(MstGroup mg) {
                //�O���[�v
            if(mg.getShops().size()> 0) {
                for (MstShop mshop : mg.getShops()) {
                    if ( mshop.getCourseFlag().equals(1)) {
                        return true;
                    }
                }
            } else if ( mg.getGroups().size() > 0) {
                for ( int i = 0;i < mg.getGroups().size() ;i ++) {
                    return checkShopCourseFlag(mg.getGroups().get(i));
                }
            }
            return false;
        }
        //LVTu start add 2015/12/01 Bug Bug #44791
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        techGroup = new javax.swing.ButtonGroup();
        taxGroup = new javax.swing.ButtonGroup();
        countGroup = new javax.swing.ButtonGroup();
        customerGroup = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        lblTax = new javax.swing.JLabel();
        rdoTaxBlank = new javax.swing.JRadioButton();
        rdoTaxUnit = new javax.swing.JRadioButton();
        targetLabel = new javax.swing.JLabel();
        target = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jPanel1 = new javax.swing.JPanel();
        lblTargetPeriod = new javax.swing.JLabel();
        cmbTargetPeriodStartDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodStartDate.setDate(new java.util.Date());
        jLabel1 = new javax.swing.JLabel();
        cmbTargetPeriodEndDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodEndDate.setDate(new java.util.Date());
        btnOutput = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTargetPeriod1 = new javax.swing.JLabel();
        cmbTargetPeriodStartDate1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodStartDate1.setDate(new java.util.Date());
        jLabel2 = new javax.swing.JLabel();
        cmbTargetPeriodEndDate1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodEndDate1.setDate(new java.util.Date());
        rdoTech = new javax.swing.JRadioButton();
        rdoTechClaim = new javax.swing.JRadioButton();
        lblTargetPeriod3 = new javax.swing.JLabel();
        lblTargetPeriod4 = new javax.swing.JLabel();
        rdoSuryo = new javax.swing.JRadioButton();
        rdoHikaiin = new javax.swing.JRadioButton();
        rdoNinzu = new javax.swing.JRadioButton();
        rdoKaiin = new javax.swing.JRadioButton();
        btnOutput1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblTargetPeriod2 = new javax.swing.JLabel();
        cmbTargetPeriodStartDate2 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodStartDate2.setDate(new java.util.Date());
        jLabel3 = new javax.swing.JLabel();
        cmbTargetPeriodEndDate2 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodEndDate2.setDate(new java.util.Date());
        btnOutput2 = new javax.swing.JButton();
        orderDisplayLabel = new javax.swing.JLabel();
        cmbOrderDisplay = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jPanel5 = new javax.swing.JPanel();
        cmbTargetPeriodEndDate4 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodEndDate4.setDate(new java.util.Date());
        btnOutput4 = new javax.swing.JButton();
        cmbTargetPeriodStartDate4 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodStartDate4.setDate(new java.util.Date());
        jLabel5 = new javax.swing.JLabel();
        lblTargetPeriod6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnOutput5 = new javax.swing.JButton();
        cmbTargetPeriodStartDate5 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodStartDate5.setDate(new java.util.Date());
        lblTargetPeriod7 = new javax.swing.JLabel();
        cmbTargetPeriodEndDate5 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        this.cmbTargetPeriodEndDate5.setDate(new java.util.Date());
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        referenceCategoryLeftScrollPane = new javax.swing.JScrollPane();
        tblReferenceCategoryName = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        releaseButton = new javax.swing.JButton();
        releaseAllButton = new javax.swing.JButton();
        lblCategory = new javax.swing.JLabel();
        selectCategoryRightScrollPane = new javax.swing.JScrollPane();
        tblSelectCategoryName = new javax.swing.JTable();

        setFocusCycleRoot(true);

        pnlMain.setFocusCycleRoot(true);
        pnlMain.setOpaque(false);

        lblTax.setText("�ŋ敪");
        lblTax.setFocusCycleRoot(true);

        taxGroup.add(rdoTaxBlank);
        rdoTaxBlank.setText("�Ŕ�");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setFocusCycleRoot(true);
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);

        taxGroup.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("�ō�");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setFocusCycleRoot(true);
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);

        targetLabel.setText("�Ώ�");
        targetLabel.setFocusCycleRoot(true);

        target.setFocusCycleRoot(true);
        target.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                targetItemStateChanged(evt);
            }
        });
        target.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnlMainLayout = new org.jdesktop.layout.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblTax)
                    .add(targetLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlMainLayout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(rdoTaxUnit)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rdoTaxBlank))
                    .add(pnlMainLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(targetLabel)
                    .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTax)
                    .add(rdoTaxUnit)
                    .add(rdoTaxBlank))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("�Z�p����"));
        jPanel1.setOpaque(false);

        lblTargetPeriod.setText("�Ώۊ���");
        lblTargetPeriod.setFocusCycleRoot(true);

        cmbTargetPeriodStartDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDateFocusGained(evt);
            }
        });

        jLabel1.setText("�`");
        jLabel1.setFocusCycleRoot(true);

        cmbTargetPeriodEndDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDateFocusGained(evt);
            }
        });

        btnOutput.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput.setBorderPainted(false);
        btnOutput.setFocusCycleRoot(true);
        btnOutput.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(lblTargetPeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTargetPeriodStartDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTargetPeriodEndDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnOutput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnOutput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cmbTargetPeriodEndDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblTargetPeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTargetPeriodStartDate, 0, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("�S���ʕ��ލ\����"));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(396, 160));
        jPanel2.setLayout(null);

        lblTargetPeriod1.setText("�o�͑Ώ�");
        lblTargetPeriod1.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod1);
        lblTargetPeriod1.setBounds(10, 110, 60, 21);

        cmbTargetPeriodStartDate1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate1.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDate1FocusGained(evt);
            }
        });
        jPanel2.add(cmbTargetPeriodStartDate1);
        cmbTargetPeriodStartDate1.setBounds(80, 50, 88, 21);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("�`");
        jLabel2.setFocusCycleRoot(true);
        jPanel2.add(jLabel2);
        jLabel2.setBounds(170, 50, 20, 21);

        cmbTargetPeriodEndDate1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate1.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDate1FocusGained(evt);
            }
        });
        jPanel2.add(cmbTargetPeriodEndDate1);
        cmbTargetPeriodEndDate1.setBounds(190, 50, 88, 21);

        techGroup.add(rdoTech);
        rdoTech.setSelected(true);
        rdoTech.setText("�Z�p���ލ\����");
        rdoTech.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTech.setFocusCycleRoot(true);
        rdoTech.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTech.setOpaque(false);
        jPanel2.add(rdoTech);
        rdoTech.setBounds(10, 20, 140, 21);

        techGroup.add(rdoTechClaim);
        rdoTechClaim.setText("�Z�p�N���[�����ލ\����");
        rdoTechClaim.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechClaim.setFocusCycleRoot(true);
        rdoTechClaim.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTechClaim.setOpaque(false);
        rdoTechClaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTechClaimActionPerformed(evt);
            }
        });
        jPanel2.add(rdoTechClaim);
        rdoTechClaim.setBounds(159, 20, 170, 21);

        lblTargetPeriod3.setText("�Ώۊ���");
        lblTargetPeriod3.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod3);
        lblTargetPeriod3.setBounds(10, 50, 60, 21);

        lblTargetPeriod4.setText("�o�͏���");
        lblTargetPeriod4.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod4);
        lblTargetPeriod4.setBounds(10, 80, 60, 21);

        countGroup.add(rdoSuryo);
        rdoSuryo.setText("����");
        rdoSuryo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSuryo.setFocusCycleRoot(true);
        rdoSuryo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSuryo.setOpaque(false);
        rdoSuryo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSuryoActionPerformed(evt);
            }
        });
        jPanel2.add(rdoSuryo);
        rdoSuryo.setBounds(160, 80, 50, 21);

        customerGroup.add(rdoHikaiin);
        rdoHikaiin.setText("�����܂�");
        rdoHikaiin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoHikaiin.setFocusCycleRoot(true);
        rdoHikaiin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoHikaiin.setOpaque(false);
        rdoHikaiin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHikaiinActionPerformed(evt);
            }
        });
        jPanel2.add(rdoHikaiin);
        rdoHikaiin.setBounds(160, 110, 80, 21);

        countGroup.add(rdoNinzu);
        rdoNinzu.setSelected(true);
        rdoNinzu.setText("�l��");
        rdoNinzu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNinzu.setFocusCycleRoot(true);
        rdoNinzu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoNinzu.setOpaque(false);
        rdoNinzu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNinzuActionPerformed(evt);
            }
        });
        jPanel2.add(rdoNinzu);
        rdoNinzu.setBounds(80, 80, 50, 21);

        customerGroup.add(rdoKaiin);
        rdoKaiin.setSelected(true);
        rdoKaiin.setText("����̂�");
        rdoKaiin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoKaiin.setFocusCycleRoot(true);
        rdoKaiin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoKaiin.setOpaque(false);
        rdoKaiin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoKaiinActionPerformed(evt);
            }
        });
        jPanel2.add(rdoKaiin);
        rdoKaiin.setBounds(80, 110, 70, 21);

        btnOutput1.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput1.setBorderPainted(false);
        btnOutput1.setFocusCycleRoot(true);
        btnOutput1.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutput1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnOutput1);
        btnOutput1.setBounds(290, 50, 92, 25);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("�S���ʈ�����"));
        jPanel3.setOpaque(false);

        lblTargetPeriod2.setText("�Ώۊ���");
        lblTargetPeriod2.setFocusCycleRoot(true);

        cmbTargetPeriodStartDate2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate2.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDate2FocusGained(evt);
            }
        });

        jLabel3.setText("�`");
        jLabel3.setFocusCycleRoot(true);

        cmbTargetPeriodEndDate2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate2.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDate2FocusGained(evt);
            }
        });

        btnOutput2.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput2.setBorderPainted(false);
        btnOutput2.setFocusCycleRoot(true);
        btnOutput2.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutput2ActionPerformed(evt);
            }
        });

        orderDisplayLabel.setText("�\����");
        orderDisplayLabel.setFocusCycleRoot(true);

        cmbOrderDisplay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "���z", "�|�C���g" }));
        cmbOrderDisplay.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(orderDisplayLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTargetPeriod2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(cmbTargetPeriodStartDate2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbTargetPeriodEndDate2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnOutput2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(cmbOrderDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cmbTargetPeriodEndDate2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblTargetPeriod2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTargetPeriodStartDate2, 0, 21, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)))
                    .add(btnOutput2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(orderDisplayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbOrderDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("�R�[�X�_�񐬐�"));
        jPanel5.setOpaque(false);

        cmbTargetPeriodEndDate4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate4.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDate4FocusGained(evt);
            }
        });

        btnOutput4.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput4.setBorderPainted(false);
        btnOutput4.setFocusCycleRoot(true);
        btnOutput4.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutput4ActionPerformed(evt);
            }
        });

        cmbTargetPeriodStartDate4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate4.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDate4FocusGained(evt);
            }
        });

        jLabel5.setText("�`");
        jLabel5.setFocusCycleRoot(true);

        lblTargetPeriod6.setText("�Ώۊ���");
        lblTargetPeriod6.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(7, 7, 7)
                .add(lblTargetPeriod6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTargetPeriodStartDate4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTargetPeriodEndDate4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnOutput4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnOutput4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cmbTargetPeriodEndDate4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTargetPeriodStartDate4, 0, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(lblTargetPeriod6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("�R�[�X��������"));
        jPanel6.setOpaque(false);

        btnOutput5.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput5.setBorderPainted(false);
        btnOutput5.setFocusCycleRoot(true);
        btnOutput5.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutput5ActionPerformed(evt);
            }
        });

        cmbTargetPeriodStartDate5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate5.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDate5FocusGained(evt);
            }
        });

        lblTargetPeriod7.setText("�Ώۊ���");
        lblTargetPeriod7.setFocusCycleRoot(true);

        cmbTargetPeriodEndDate5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate5.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDate5FocusGained(evt);
            }
        });

        jLabel6.setText("�`");
        jLabel6.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(lblTargetPeriod7)
                .add(18, 18, 18)
                .add(cmbTargetPeriodStartDate5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTargetPeriodEndDate5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnOutput5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnOutput5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(cmbTargetPeriodEndDate5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(lblTargetPeriod7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(cmbTargetPeriodStartDate5, 0, 0, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setOpaque(false);

        tblReferenceCategoryName.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "�ƑԖ�", "�Ƒ�ID"
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
        tblReferenceCategoryName.setSelectionForeground(new java.awt.Color(0, 0, 0));
        //nhanvt
        tblReferenceCategoryName.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(tblReferenceCategoryName, SystemInfo.getTableHeaderRenderer());
        tblReferenceCategoryName.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(tblReferenceCategoryName);
        //nhanvt
        tblReferenceCategoryName.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblReferenceCategoryNameMouseMoved(evt);
            }
        });
        referenceCategoryLeftScrollPane.setViewportView(tblReferenceCategoryName);
        if (tblReferenceCategoryName.getColumnModel().getColumnCount() > 0) {
            tblReferenceCategoryName.getColumnModel().getColumn(0).setMinWidth(250);
            tblReferenceCategoryName.getColumnModel().getColumn(0).setMaxWidth(250);
            tblReferenceCategoryName.getColumnModel().getColumn(1).setMinWidth(0);
            tblReferenceCategoryName.getColumnModel().getColumn(1).setPreferredWidth(0);
            tblReferenceCategoryName.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        jPanel7.setOpaque(false);

        selectButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        selectAllButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right2_off.jpg"));
        selectAllButton.setBorderPainted(false);
        selectAllButton.setContentAreaFilled(false);
        selectAllButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right2_on.jpg"));
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        releaseButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left_off.jpg"));
        releaseButton.setBorderPainted(false);
        releaseButton.setContentAreaFilled(false);
        releaseButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left_on.jpg"));
        releaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseButtonActionPerformed(evt);
            }
        });

        releaseAllButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left2_off.jpg"));
        releaseAllButton.setBorderPainted(false);
        releaseAllButton.setContentAreaFilled(false);
        releaseAllButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left2_on.jpg"));
        releaseAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseAllButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        lblCategory.setText("�W�v�Ƒ�");

        tblSelectCategoryName.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "�ƑԖ�", "�Ƒ�ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class
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
        tblSelectCategoryName.setSelectionForeground(new java.awt.Color(0, 0, 0));
        //nhanvt
        tblSelectCategoryName.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(tblSelectCategoryName, SystemInfo.getTableHeaderRenderer());
        tblSelectCategoryName.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(tblSelectCategoryName);
        //TableColumnModel model = tblSelectCategoryName.getColumnModel();
        //model.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        //model.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
        //model.getColumn(4).setCellEditor(new IntegerCellEditor(new JTextField()));
        //model.getColumn(5).setCellEditor(new IntegerCellEditor(new JTextField()));
        //nhanvt
        tblSelectCategoryName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSelectCategoryNameMouseClicked(evt);
            }
        });
        tblSelectCategoryName.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblSelectCategoryNamePropertyChange(evt);
            }
        });
        selectCategoryRightScrollPane.setViewportView(tblSelectCategoryName);
        if (tblSelectCategoryName.getColumnModel().getColumnCount() > 0) {
            tblSelectCategoryName.getColumnModel().getColumn(0).setMinWidth(250);
            tblSelectCategoryName.getColumnModel().getColumn(0).setMaxWidth(250);
            tblSelectCategoryName.getColumnModel().getColumn(1).setMinWidth(0);
            tblSelectCategoryName.getColumnModel().getColumn(1).setPreferredWidth(0);
            tblSelectCategoryName.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(lblCategory)
                .add(18, 18, 18)
                .add(referenceCategoryLeftScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectCategoryRightScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblCategory)
                    .add(referenceCategoryLeftScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectCategoryRightScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 8, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlMain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(258, 258, 258))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(pnlMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

        private void targetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_targetItemStateChanged

            if(target.getSelectedItem() instanceof MstGroup || (this.getSelectedShop() != null && this.getSelectedShop().isProportionally())) {
                //�O���[�v�A���邢�͈����g�p����X�܂̂ݕ\������
                jPanel3.setVisible(true);
            } else {
                //��L�ȊO�̏ꍇ�A�\�����Ȃ�
                jPanel3.setVisible(false);
            }
            // start add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
            if(!isLoading){
                jPanel5.setVisible(false);
                jPanel6.setVisible(false);
                if(target.getSelectedItem() instanceof MstShop){
                     //IVS NNTUAN START EDIT 20131028
                    /*if(this.getSelectedShop().getCourseFlag() == 1){
                        jPanel5.setVisible(true);
                        jPanel6.setVisible(true);
                    }*/
                    if(this.getSelectedShop().getShopID() == 0 || this.getSelectedShop().getCourseFlag() == 1){
                        jPanel5.setVisible(true);
                        jPanel6.setVisible(true);
                    }
                     //IVS NNTUAN END EDIT 20131028
                }
            }
            // end add 20130805 nakhoa �𖱋@�\�g�p�L���̐���ǉ�

        }//GEN-LAST:event_targetItemStateChanged

    private void btnOutput2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutput2ActionPerformed

        if (!SystemInfo.checkAuthorityPassword(243)) return;
        
        btnOutput2.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.print3();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_btnOutput2ActionPerformed

    private void print3() {
        
        ReportLogic logic = new ReportLogic();
        ReportParameterBean paramBean = new ReportParameterBean();
        paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
         //Luc start add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
        String shopCategoryIdList = "";
        String shopCategoryNameList = "";
        if(mrsUse.size() >0){
           int count = 0;
           for(MstShopCategory category : mrsUse){
               if(category.getShopCategoryId() != null){
                   shopCategoryIdList += category.getShopCategoryId();
                   shopCategoryNameList += category.getShopClassName();
                   count++;
                   if(count < mrsUse.size()){
                       shopCategoryIdList += ",";
                       shopCategoryNameList += "�A";
                   }
               }
           }
        }
        paramBean.setCategoryIDList(shopCategoryIdList);
        paramBean.setCategoryNameList(shopCategoryNameList);
        paramBean.setUseShopCategory(useShopCategory);
        //Luc end add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
        //�O���[�v
        if(target.getSelectedItem() instanceof MstGroup) {
            MstGroup	mg	=	(MstGroup)target.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
        }
        //�X��
        else if(target.getSelectedItem() instanceof MstShop) {
            MstShop		ms	=	(MstShop)target.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }
        
        //�ΏۂƂȂ�X�܂����݂��Ȃ��ꍇ
        if(paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //�ŋ敪
        if(this.rdoTaxBlank.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// �ŋ敪(�Ŕ���)
        } else if(this.rdoTaxUnit.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// �ŋ敪(�ō���)
        }
        
        //�����̋敪�̐ݒ�
        Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
        paramBean.setDiscountType(discountType);
        
        //�Ώۊ���
        paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate2.getDateStr());
        paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate2.getDateStr());
        paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate2.getDate());
        paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate2.getDate());
        
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(this.cmbTargetPeriodStartDate2.getDate());
        end.setTime(this.cmbTargetPeriodEndDate2.getDate());
        
        if(start.compareTo(end) != 0) {
            //�Ώۊ��Ԃ��J�n�����I�����Ȃ�G���[
            if(start.after(end)) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        //�o�͏���
        boolean logicResult = true;
        try {
            //������
            logicResult = logic.outStaffReportProportionallyDistribution(paramBean,cmbOrderDisplay.getSelectedIndex());
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // �G���[��
        if(!logicResult) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cmbTargetPeriodEndDate2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDate2FocusGained
        cmbTargetPeriodEndDate2.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndDate2FocusGained

    private void cmbTargetPeriodStartDate2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDate2FocusGained
        cmbTargetPeriodStartDate2.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartDate2FocusGained

    private void btnOutput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutput1ActionPerformed

        if (!SystemInfo.checkAuthorityPassword(242)) return;

        btnOutput1.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.print2();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_btnOutput1ActionPerformed

    private void print2() {
        
        ReportLogic logic = new ReportLogic();
        ReportParameterBean paramBean = new ReportParameterBean();
        paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
         //Luc start add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
        String shopCategoryIdList = "";
        String shopCategoryNameList = "";
        if(mrsUse.size() >0){
           int count = 0;
           for(MstShopCategory category : mrsUse){
               if(category.getShopCategoryId() != null){
                   shopCategoryIdList += category.getShopCategoryId();
                   shopCategoryNameList += category.getShopClassName();
                   count++;
                   if(count < mrsUse.size()){
                       shopCategoryIdList += ",";
                       shopCategoryNameList += "�A";
                   }
               }
           }
        }
        paramBean.setCategoryIDList(shopCategoryIdList);
        paramBean.setCategoryNameList(shopCategoryNameList);
        paramBean.setUseShopCategory(useShopCategory);
        //Luc end add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
        //�O���[�v
        if(target.getSelectedItem() instanceof MstGroup) {
            MstGroup	mg	=	(MstGroup)target.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
        }
        //�X��
        else if(target.getSelectedItem() instanceof MstShop) {
            MstShop		ms	=	(MstShop)target.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }
        
        //�ΏۂƂȂ�X�܂����݂��Ȃ��ꍇ
        if(paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //�ŋ敪
        if(this.rdoTaxBlank.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// �ŋ敪(�Ŕ���)
        } else if(this.rdoTaxUnit.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// �ŋ敪(�ō���)
        }
        
        //�����̋敪�̐ݒ�
        Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
        paramBean.setDiscountType(discountType);
        
        //�Ώۊ���
        paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate1.getDateStr());
        paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate1.getDateStr());
        paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate1.getDate());
        paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate1.getDate());
        
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(this.cmbTargetPeriodStartDate1.getDate());
        end.setTime(this.cmbTargetPeriodEndDate1.getDate());
        
        if(start.compareTo(end) != 0) {
            //�Ώۊ��Ԃ��J�n�����I�����Ȃ�G���[
            if(start.after(end)) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int customerDivision = rdoKaiin.isSelected() ? 1 : 2;

        //�o�͏���
        boolean logicResult = true;
        try {
            if(this.rdoTech.isSelected()) {
                //�Z�p���ލ\����
                if(rdoNinzu.isSelected()){
                    //�l��
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,1,1,customerDivision);
                } else if (rdoSuryo.isSelected()){
                    //����
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,1,2,customerDivision);
                }
            } else if(this.rdoTechClaim.isSelected()) {
                //�Z�p�N���[�����ލ\����
                if(rdoNinzu.isSelected()){
                    //�l��
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,3,1,customerDivision);
                } else if (rdoSuryo.isSelected()){
                    //����
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,3,2,customerDivision);
                }
            } else {
                // �w�o�͑Ώۂ̕��ނ��I������Ă��܂���x
                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(1102, "�o�͑Ώۂ̕���"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // �G���[��
        if(!logicResult) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }        
    }
    
    private void cmbTargetPeriodEndDate1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDate1FocusGained
        cmbTargetPeriodEndDate1.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndDate1FocusGained

    private void cmbTargetPeriodStartDate1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDate1FocusGained
        cmbTargetPeriodStartDate1.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartDate1FocusGained

    private void cmbTargetPeriodEndDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDateFocusGained
        cmbTargetPeriodEndDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndDateFocusGained

    private void cmbTargetPeriodStartDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDateFocusGained
        cmbTargetPeriodStartDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartDateFocusGained

	private void btnOutputActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputActionPerformed
	{//GEN-HEADEREND:event_btnOutputActionPerformed

            
            if (!SystemInfo.checkAuthorityPassword(241)) return;

            btnOutput.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                this.print1();
                
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

	}//GEN-LAST:event_btnOutputActionPerformed

        private void rdoSuryoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSuryoActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_rdoSuryoActionPerformed

        private void rdoHikaiinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHikaiinActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_rdoHikaiinActionPerformed

        private void rdoNinzuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNinzuActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_rdoNinzuActionPerformed

        private void rdoKaiinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoKaiinActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_rdoKaiinActionPerformed

        private void cmbTargetPeriodStartDate4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDate4FocusGained
            // TODO add your handling code here:
        }//GEN-LAST:event_cmbTargetPeriodStartDate4FocusGained

        private void cmbTargetPeriodEndDate4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDate4FocusGained
            // TODO add your handling code here:
        }//GEN-LAST:event_cmbTargetPeriodEndDate4FocusGained

        private void btnOutput4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutput4ActionPerformed
            if (!SystemInfo.checkAuthorityPassword(242)) return;

            btnOutput4.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                this.print4();

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }//GEN-LAST:event_btnOutput4ActionPerformed

        private void cmbTargetPeriodStartDate5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDate5FocusGained
            // TODO add your handling code here:
        }//GEN-LAST:event_cmbTargetPeriodStartDate5FocusGained

        private void cmbTargetPeriodEndDate5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDate5FocusGained
            // TODO add your handling code here:
        }//GEN-LAST:event_cmbTargetPeriodEndDate5FocusGained

        private void btnOutput5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutput5ActionPerformed
            if (!SystemInfo.checkAuthorityPassword(242)) return;

            btnOutput1.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                this.print5();

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }//GEN-LAST:event_btnOutput5ActionPerformed

    private void rdoTechClaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTechClaimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoTechClaimActionPerformed

    private void tblReferenceCategoryNameMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReferenceCategoryNameMouseMoved

    }//GEN-LAST:event_tblReferenceCategoryNameMouseMoved

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_�S���ė�����
        this.moveMutiCategory(true);
        //IVS_vtnhan end add 20140723 MASHU_�S���ė�����
    }//GEN-LAST:event_selectButtonActionPerformed
       /**
      * move item
      * @param isSelect 
      */
      public void moveMutiCategory(boolean isSelect) {

        JTable fromTable = (isSelect ? tblReferenceCategoryName : tblSelectCategoryName);

        int index = fromTable.getSelectedRow();
        if (index < 0) return;
        ArrayList<MstShopCategory> tmp = new ArrayList<MstShopCategory>();
        if (isSelect) {
             int[]  selectedIndex = fromTable.getSelectedRows();
            if (selectedIndex.length > 0) {
                for (int i = 0; i < selectedIndex.length; i++) {
                    mrsUse.add(mrsRef.get(selectedIndex[i]));
                    tmp.add(mrsRef.get(selectedIndex[i]));
                }
                mrsRef.removeAll(tmp);
            }
    
        } else {
           
            int[]  selectedIndex = fromTable.getSelectedRows();
            if (selectedIndex.length > 0) {
                for (int i = 0; i < selectedIndex.length; i++) { 
                    mrsRef.add(mrsUse.get(selectedIndex[i]));
                    tmp.add(mrsUse.get(selectedIndex[i]));
                }
                 mrsUse.removeAll(tmp);
            }
           
        }

        this.showItems();
    }
       /**
     * show data on two table
     */
    private void showItems() {
        this.showItems(mrsRef, tblReferenceCategoryName);
        this.showItems(mrsUse, tblSelectCategoryName);
    }
    /**
     * show data detail on two table
     * @param list
     * @param table 
     */
    private void showItems(ArrayList<MstShopCategory> list, JTable table) {

        Collections.sort(list, new ItemComparator());

        if (table.getCellEditor() != null) table.getCellEditor().stopCellEditing();
        SwingUtil.clearTable(table);
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        for (MstShopCategory mr : list) {
            model.addRow(new Object[]{
                mr.getShopClassName()
            });
           
        }
    }
      /**
     * compare data
     */
   private class ItemComparator implements java.util.Comparator {
        public int compare(Object s, Object t) {
            return ((MstShopCategory) s).getDisplaySeq()- ((MstShopCategory) t).getDisplaySeq();
	}
   }
    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_�S���ė�����
        this.moveCategoryAll(true);
        //IVS_vtnhan end add 20140723 MASHU_�S���ė�����
    }//GEN-LAST:event_selectAllButtonActionPerformed
       
    /**
     * move all item
     * @param isSelect 
     */
    public void moveCategoryAll(boolean isSelect) {

        JTable fromTable = (isSelect ? tblReferenceCategoryName : tblSelectCategoryName);

        if (fromTable.getRowCount() == 0) return;

        if (isSelect) {
            for (MstShopCategory mr : mrsRef) mrsUse.add(mr);
            mrsRef.clear();
        } else {
            for (MstShopCategory mr : mrsUse) mrsRef.add(mr);
            mrsUse.clear();
        }

        this.showItems();
    }
    private void releaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_�S���ė�����
        this.moveMutiCategory(false);
        //IVS_vtnhan end add 20140723 MASHU_�S���ė�����
    }//GEN-LAST:event_releaseButtonActionPerformed

    private void releaseAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseAllButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_�S���ė�����
        this.moveCategoryAll(false);
        //IVS_vtnhan end add 20140723 MASHU_�S���ė�����
    }//GEN-LAST:event_releaseAllButtonActionPerformed

    private void tblSelectCategoryNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelectCategoryNameMouseClicked

    }//GEN-LAST:event_tblSelectCategoryNameMouseClicked

    private void tblSelectCategoryNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblSelectCategoryNamePropertyChange

    }//GEN-LAST:event_tblSelectCategoryNamePropertyChange

    private void targetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetActionPerformed
        // TODO add your handling code here:
         //luc 
        this.chargeDataCombo();
        //�ƑԂ�\������B
        showShopCategory();
        
    }//GEN-LAST:event_targetActionPerformed
    private void chargeDataCombo() {   

        mrsRef = new MstShopCategorys();
        mrsUse = new MstShopCategorys();
        //�O���[�v
        if(target.getSelectedItem() instanceof MstGroup) {
                displayForDesign(true, 1);
                useShopCategory = 1;
                isHideCategory = true;
                initCategoryWithGroupShop();
                //Luc start add 20150703 #39346
                MstGroup group = new MstGroup();
                group = (MstGroup) target.getSelectedItem();
                jPanel5.setVisible(false);
                jPanel6.setVisible(false);
                //LVTu start edit 2015/12/01 Bug #44791
//                for (MstShop shop : group.getShops()) {
//                    if (shop.getCourseFlag() == 1) {
//                        jPanel5.setVisible(true);
//                        jPanel6.setVisible(true);
//                        //IVS_LVTu start edit 2015/11/04 Bug #44067
//                        //return;
//                        //IVS_LVTu end edit 2015/11/04 Bug #44067
//                    }
//                }
                //Luc end add 20150703 #39346
                if (checkShopCourseFlag(group)) {
                    jPanel5.setVisible(true);
                    jPanel6.setVisible(true);
                }
                //LVTu end edit 2015/12/01 Bug #44791
                 
        }
        //�X��
        else if(target.getSelectedItem() instanceof MstShop) {
             MstShop ms = (MstShop)target.getSelectedItem();
             if(ms.getUseShopCategory().equals(1)){
                    displayForDesign(false, 1);
                    useShopCategory = 1;
                    isHideCategory = true;
                    initCategoryWithMutiShop();

             }else{

                    displayForDesign(false, 0);
                    useShopCategory = 0;
                    isHideCategory = false;

             }
            //Luc start add 20150703 #39346
            MstShop shop = new MstShop();
            shop = (MstShop) target.getSelectedItem();
            jPanel5.setVisible(false);
            jPanel6.setVisible(false);
            if (shop.getCourseFlag() == 1) {
                jPanel5.setVisible(true);
                jPanel6.setVisible(true);
                //IVS_TMTrong start edit 20150713 Bug #40218
                //return;
                //IVS_TMTrong end edit 20150713 Bug #40218
            }
            //Luc end add 20150703 #39346

        }
        this.showItems();
        }
    /**
        * init data for table �ƑԖ� left with muti shop
        */
       private void initCategoryWithMutiShop() {
            mrsRef = new MstShopCategorys();
            MstShop ms = (MstShop)target.getSelectedItem();
            if(ms.getShopID() != null){
               try
               {
                       mscg = new MstShopCategorys();
                       ConnectionWrapper	con	=	SystemInfo.getConnection();

                       mscg.loadByShop(con,ms.getShopID());

                       if(mscg.size() > 0){
                           mrsRef = mscg;
                       }

               }
               catch(SQLException e)
               {

                       SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
               }
            }

       }  
       /* init data for table �ƑԖ� left with group shop
        */
       private void initCategoryWithGroupShop() {
           mrsRef = new MstShopCategorys();
           SimpleMaster sm = new SimpleMaster(
                   "",
                   "mst_shop_category",
                   "shop_category_id",
                   "shop_class_name", 0);

           sm.loadData();
           for (MstData md : sm) {
               if(md != null){
                   MstShopCategory category = new MstShopCategory();
                   category.setShopCategoryId(md.getID());
                   category.setShopClassName(md.getName());
                   category.setDisplaySeq(md.getDisplaySeq());
                   mrsRef.add(category);
               }

           }

       } 
      /* control design screen follow combo �Ώ�
     * @param isGroup
     * @param useShopCategory 
     */
    public void displayForDesign(boolean isGroup, Integer useShopCategory){
       
        if(isGroup){
            if(SystemInfo.getSetteing().isUseShopCategory()) {
                SwingUtil.clearTable(tblReferenceCategoryName);
                SwingUtil.clearTable(tblSelectCategoryName);
                jPanel4.setVisible(true);
            }else {
                SwingUtil.clearTable(tblReferenceCategoryName);
                SwingUtil.clearTable(tblSelectCategoryName);
                jPanel4.setVisible(false);
            }
                     
        }else{
            MstShop ms = (MstShop)target.getSelectedItem();
            if(ms.getUseShopCategory().equals(1)){
                   SwingUtil.clearTable(tblReferenceCategoryName);
                   SwingUtil.clearTable(tblSelectCategoryName);
                   jPanel4.setVisible(true);
            }else{

                   SwingUtil.clearTable(tblReferenceCategoryName);
                   SwingUtil.clearTable(tblSelectCategoryName);
                   jPanel4.setVisible(false);
            }
        }

    }
	private void showShopCategory() {
            //�W�v�Ƒ�
            MstShop selectedShop =  null;
            MstGroup selectedGroup = null;
            if(target.getSelectedItem() instanceof MstGroup) {
              selectedGroup = (MstGroup) target.getSelectedItem();

                if(selectedGroup!=null && SystemInfo.getSetteing().isUseShopCategory()) {
                  jPanel4.setVisible(true);
                  jPanel4.setLocation(jPanel4.getX(),jPanel4.getY()+jPanel4.getHeight()+10);
                }
            }
            if(target.getSelectedItem() instanceof MstShop)
            {
                selectedShop = (MstShop)target.getSelectedItem();
                if(selectedShop.getUseShopCategory()==1){
                jPanel4.setVisible(true);
                jPanel4.setLocation(jPanel4.getX(),jPanel4.getY()+jPanel4.getHeight()+10);
                }else {

                jPanel4.setLocation(jPanel4.getX(),jPanel4.getY());
                jPanel4.setVisible(false);
                }
            }
        
        }
        private void print1() {
            ReportLogic logic = new ReportLogic();
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
            //Luc start add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
            String shopCategoryIdList = "";
            String shopCategoryNameList = "";
            if(mrsUse.size() >0){
               int count = 0;
               for(MstShopCategory category : mrsUse){
                   if(category.getShopCategoryId() != null){
                       shopCategoryIdList += category.getShopCategoryId();
                       shopCategoryNameList += category.getShopClassName();
                       count++;
                       if(count < mrsUse.size()){
                           shopCategoryIdList += ",";
                           shopCategoryNameList += "�A";
                       }
                   }
               }
            }
            paramBean.setCategoryIDList(shopCategoryIdList);
            paramBean.setCategoryNameList(shopCategoryNameList);
            paramBean.setUseShopCategory(useShopCategory);
            //Luc end add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
            //�O���[�v
            if(target.getSelectedItem() instanceof MstGroup)
            {
                    MstGroup	mg	=	(MstGroup)target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
            }
            //�X��
            else if(target.getSelectedItem() instanceof MstShop)
            {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
            }

            //�ΏۂƂȂ�X�܂����݂��Ȃ��ꍇ
            if(paramBean.getShopIDList().equals(""))
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if(this.rdoTaxBlank.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// �ŋ敪(�Ŕ���)
            }
            else if(this.rdoTaxUnit.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// �ŋ敪(�ō���)
            }

            //�����̋敪�̐ݒ�
            Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
            paramBean.setDiscountType(discountType);

            paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate.getDateStr());
            paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate.getDateStr());
            paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate.getDate());
            paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate.getDate());

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(this.cmbTargetPeriodStartDate.getDate());
            end.setTime(this.cmbTargetPeriodEndDate.getDate());

            if(start.compareTo(end) != 0)
            {
                    if(start.after(end))
                    {
                                    MessageDialog.showMessageDialog(this,
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                                                    this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    }
            }



            //�o�͏���
            boolean logicResult = true;
            // �Ɩ���
            try
            {

                    logicResult = logic.outStaffReportTechnicSales(paramBean);

            }
            catch (Exception e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(1099),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // �G���[��
            if(!logicResult)
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
            }            
        }
    
        /**
         * �S���ʋZ�p���с@�R�[�X�_�񐬐яo��
         */
        private void print4(){
            ReportLogic logic = new ReportLogic();
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
             //Luc start add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
            String shopCategoryIdList = "";
            String shopCategoryNameList = "";
            if(mrsUse.size() >0){
               int count = 0;
               for(MstShopCategory category : mrsUse){
                   if(category.getShopCategoryId() != null){
                       shopCategoryIdList += category.getShopCategoryId();
                       shopCategoryNameList += category.getShopClassName();
                       count++;
                       if(count < mrsUse.size()){
                           shopCategoryIdList += ",";
                           shopCategoryNameList += "�A";
                       }
                   }
               }
            }
            paramBean.setCategoryIDList(shopCategoryIdList);
            paramBean.setCategoryNameList(shopCategoryNameList);
            
            paramBean.setListCategoryId(shopCategoryIdList);
            paramBean.setListCategoryName(shopCategoryNameList);
            paramBean.setUseShopCategory(useShopCategory);
            //Luc end add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
            //�O���[�v
            if(target.getSelectedItem() instanceof MstGroup)
            {
                    MstGroup	mg	=	(MstGroup)target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
            }
            //�X��
            else if(target.getSelectedItem() instanceof MstShop)
            {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
            }

            //�ΏۂƂȂ�X�܂����݂��Ȃ��ꍇ
            if(paramBean.getShopIDList().equals(""))
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if(this.rdoTaxBlank.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// �ŋ敪(�Ŕ���)
            }
            else if(this.rdoTaxUnit.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// �ŋ敪(�ō���)
            }

            //�����̋敪�̐ݒ�
            Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
            paramBean.setDiscountType(discountType);

            paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate4.getDateStr());
            paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate4.getDateStr());
            paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate4.getDate());
            paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate4.getDate());

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(this.cmbTargetPeriodStartDate4.getDate());
            end.setTime(this.cmbTargetPeriodEndDate4.getDate());

            if(start.compareTo(end) != 0)
            {
                    if(start.after(end))
                    {
                                    MessageDialog.showMessageDialog(this,
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                                                    this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    }
            }


            //�o�͏���
            boolean logicResult = true;
            // �Ɩ���
            try
            {

                    logicResult = logic.outStaffReportCourseContract(paramBean);

            }
            catch (Exception e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(1099),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // �G���[��
            if(!logicResult)
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
            }

        }

        /**
         * �S���ʋZ�p���с@�R�[�X�������яo��
         */
        private void print5(){
            ReportLogic logic = new ReportLogic();
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
            //Luc start add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
            String shopCategoryIdList = "";
            String shopCategoryNameList = "";
            if(mrsUse.size() >0){
               int count = 0;
               for(MstShopCategory category : mrsUse){
                   if(category.getShopCategoryId() != null){
                       shopCategoryIdList += category.getShopCategoryId();
                       shopCategoryNameList += category.getShopClassName();
                       count++;
                       if(count < mrsUse.size()){
                           shopCategoryIdList += ",";
                           shopCategoryNameList += "�A";
                       }
                   }
               }
            }
            paramBean.setCategoryIDList(shopCategoryIdList);
            paramBean.setCategoryNameList(shopCategoryNameList);
            paramBean.setUseShopCategory(useShopCategory);
            //Luc end add 20141202 ticket #33404 �Ƒ�ID�̈ꗗ
            //�O���[�v
            if(target.getSelectedItem() instanceof MstGroup)
            {
                    MstGroup	mg	=	(MstGroup)target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
            }
            //�X��
            else if(target.getSelectedItem() instanceof MstShop)
            {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
            }

            //�ΏۂƂȂ�X�܂����݂��Ȃ��ꍇ
            if(paramBean.getShopIDList().equals(""))
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if(this.rdoTaxBlank.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// �ŋ敪(�Ŕ���)
            }
            else if(this.rdoTaxUnit.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// �ŋ敪(�ō���)
            }

            //�����̋敪�̐ݒ�
            Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
            paramBean.setDiscountType(discountType);

            paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate5.getDateStr());
            paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate5.getDateStr());
            paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate5.getDate());
            paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate5.getDate());

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(this.cmbTargetPeriodStartDate5.getDate());
            end.setTime(this.cmbTargetPeriodEndDate5.getDate());

            if(start.compareTo(end) != 0)
            {
                    if(start.after(end))
                    {
                                    MessageDialog.showMessageDialog(this,
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                                                    this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    }
            }


            //�o�͏���
            boolean logicResult = true;
            // �Ɩ���
            try
            {

                    logicResult = logic.outStaffReportCourseConsumption(paramBean);

            }
            catch (Exception e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(1099),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // �G���[��
            if(!logicResult)
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
            }
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutput;
    private javax.swing.JButton btnOutput1;
    private javax.swing.JButton btnOutput2;
    private javax.swing.JButton btnOutput4;
    private javax.swing.JButton btnOutput5;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbOrderDisplay;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEndDate;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEndDate1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEndDate2;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEndDate4;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEndDate5;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStartDate;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStartDate1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStartDate2;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStartDate4;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStartDate5;
    private javax.swing.ButtonGroup countGroup;
    private javax.swing.ButtonGroup customerGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblTargetPeriod;
    private javax.swing.JLabel lblTargetPeriod1;
    private javax.swing.JLabel lblTargetPeriod2;
    private javax.swing.JLabel lblTargetPeriod3;
    private javax.swing.JLabel lblTargetPeriod4;
    private javax.swing.JLabel lblTargetPeriod6;
    private javax.swing.JLabel lblTargetPeriod7;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel orderDisplayLabel;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JRadioButton rdoHikaiin;
    private javax.swing.JRadioButton rdoKaiin;
    private javax.swing.JRadioButton rdoNinzu;
    private javax.swing.JRadioButton rdoSuryo;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private javax.swing.JRadioButton rdoTech;
    private javax.swing.JRadioButton rdoTechClaim;
    private javax.swing.JScrollPane referenceCategoryLeftScrollPane;
    private javax.swing.JButton releaseAllButton;
    private javax.swing.JButton releaseButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JScrollPane selectCategoryRightScrollPane;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel target;
    private javax.swing.JLabel targetLabel;
    private javax.swing.ButtonGroup taxGroup;
    private javax.swing.JTable tblReferenceCategoryName;
    private javax.swing.JTable tblSelectCategoryName;
    private javax.swing.ButtonGroup techGroup;
    // End of variables declaration//GEN-END:variables
	
	private	StaffResultTechReportFocusTraversalPolicy	ftp	=
			new StaffResultTechReportFocusTraversalPolicy();
	
	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(btnOutput);
		SystemInfo.addMouseCursorChange(btnOutput1);
		SystemInfo.addMouseCursorChange(btnOutput2);
                SystemInfo.addMouseCursorChange(btnOutput4);
                SystemInfo.addMouseCursorChange(btnOutput5);
	}
	
	private void setKeyListener()
	{
		cmbOrderDisplay.addKeyListener(SystemInfo.getMoveNextField());
		cmbOrderDisplay.addFocusListener(SystemInfo.getSelectText());
		rdoTechClaim.addKeyListener(SystemInfo.getMoveNextField());
		rdoTech.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodEndDate2.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodEndDate2.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodStartDate2.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodStartDate2.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodEndDate1.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodEndDate1.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodStartDate1.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodStartDate1.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodEndDate.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodEndDate.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodStartDate.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodStartDate.addFocusListener(SystemInfo.getSelectText());
		rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
		rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
		target.addKeyListener(SystemInfo.getMoveNextField());
		target.addFocusListener(SystemInfo.getSelectText());
	}

        /**
	 * �I������Ă���X�܂��擾����B
	 * @return �I������Ă���X��
	 */
	private MstShop getSelectedShop()
	{
		if(0 <= target.getSelectedIndex())
			return	(MstShop)target.getSelectedItem();
		else
			return	null;
	}
	
	/**
	 * �S���ʋZ�p���щ�ʗpFocusTraversalPolicy���擾����B
	 * @return  �S���ʋZ�p���щ�ʗpFocusTraversalPolicy
	 */
	public StaffResultTechReportFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	
	/**
	 * �S���ʋZ�p���щ�ʗpFocusTraversalPolicy
	 */
	private class StaffResultTechReportFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponentStaffResultTechReportFocusTraversalPolicy�B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			if (aComponent.equals(target))
			{
				return this.getSelectedTaxType();
			}
                        else if (aComponent.equals(rdoTaxBlank) ||
					aComponent.equals(rdoTaxUnit))
			{
				return cmbTargetPeriodStartDate;
			}
			else if (aComponent.equals(cmbTargetPeriodStartDate))
			{
				return cmbTargetPeriodEndDate;
			}
			else if (aComponent.equals(cmbTargetPeriodEndDate))
			{
                                if(rdoTech.isSelected()){
                                    return rdoTech;
                                }
                                else{
                                    return rdoTechClaim;
                                }
			}
			else if (aComponent.equals(rdoTech) || 
                                 aComponent.equals(rdoTechClaim) )
			{
				return cmbTargetPeriodStartDate1;
			}
			else if (aComponent.equals(cmbTargetPeriodStartDate1))
			{
				return cmbTargetPeriodEndDate1;
			}
			else if (aComponent.equals(cmbTargetPeriodEndDate1))
			{
                            if(rdoNinzu.isSelected()){
                                return rdoNinzu;
                            }
                            else{
                                return rdoSuryo;
                            }
			}
			else if (aComponent.equals(rdoSuryo)||
                                 aComponent.equals(rdoNinzu) )
			{
                            if(rdoKaiin.isSelected()){
                                return rdoKaiin;
                            }
                            else{
                                return rdoHikaiin;
                            }
			}
                        else if (aComponent.equals(rdoKaiin)||
                                 aComponent.equals(rdoHikaiin) )
			{
                            return cmbTargetPeriodStartDate2;
			}
			else if (aComponent.equals(cmbTargetPeriodStartDate2))
			{
                            return cmbTargetPeriodEndDate2;
			}
                        else if (aComponent.equals(cmbTargetPeriodEndDate2))
			{
                            return cmbOrderDisplay;
			}
                        else if (aComponent.equals(cmbOrderDisplay))
			{
                            return cmbTargetPeriodStartDate4;
			}
                        else if (aComponent.equals(cmbTargetPeriodStartDate4))
			{
                            return cmbTargetPeriodEndDate4;
			}
                         else if (aComponent.equals(cmbTargetPeriodEndDate4))
			{
                            return cmbTargetPeriodStartDate5;
			}
                         else if (aComponent.equals(cmbTargetPeriodStartDate5))
			{
                            return cmbTargetPeriodEndDate5;
			}
                        
			return target;
		}

		/**
		 * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{

			if (aComponent.equals(target))
			{
				return cmbOrderDisplay;
			}
                        else if (aComponent.equals(rdoTaxBlank) ||
					aComponent.equals(rdoTaxUnit))
			{
				return target;
			}
			else if (aComponent.equals(cmbTargetPeriodStartDate))
			{
				return this.getSelectedTaxType();
			}
			else if (aComponent.equals(cmbTargetPeriodEndDate))
			{
				return cmbTargetPeriodStartDate;
			}
			else if (aComponent.equals(cmbTargetPeriodStartDate1))
			{
				return cmbTargetPeriodEndDate;
			}
			else if (aComponent.equals(cmbTargetPeriodEndDate1))
			{
				return cmbTargetPeriodStartDate1;
			}
                        else if (aComponent.equals(rdoTech) ||
					aComponent.equals(rdoTechClaim))
			{
				return cmbTargetPeriodEndDate1;
			}
			else if (aComponent.equals(cmbTargetPeriodEndDate2))
			{
				return cmbTargetPeriodStartDate2;
			}
			else if (aComponent.equals(cmbOrderDisplay))
			{
        			return cmbTargetPeriodEndDate2;
			}
			
			return target;
		}

		/**
		 * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return target;
		}

		/**
		 * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getLastComponent(Container aContainer)
		{
                        return cmbOrderDisplay;
		}

		/**
		 * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
		 * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return target;
		}
		
		/**
		 * �E�B���h�E���ŏ��ɕ\�����ꂽ�Ƃ��Ƀt�H�[�J�X���ݒ肳���R���|�[�l���g��Ԃ��܂��B
		 * show() �܂��� setVisible(true) �̌Ăяo���ň�x�E�B���h�E���\�������ƁA
		 * �������R���|�[�l���g�͂���ȍ~�g�p����܂���B
		 * ��x�ʂ̃E�B���h�E�Ɉڂ����t�H�[�J�X���Ăѐݒ肳�ꂽ�ꍇ�A
		 * �܂��́A��x��\����ԂɂȂ����E�B���h�E���Ăѕ\�����ꂽ�ꍇ�́A
		 * ���̃E�B���h�E�̍Ō�Ƀt�H�[�J�X���ݒ肳�ꂽ�R���|�[�l���g���t�H�[�J�X���L�҂ɂȂ�܂��B
		 * ���̃��\�b�h�̃f�t�H���g�����ł̓f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * @param window �����R���|�[�l���g���Ԃ����E�B���h�E
		 * @return �ŏ��ɃE�B���h�E���\�������Ƃ��Ƀt�H�[�J�X�ݒ肳���R���|�[�l���g�B�K�؂ȃR���|�[�l���g���Ȃ��ꍇ�� null
		 */
		public Component getInitialComponent(Window window)
		{
			return target;
		}
		
                /**
                 * �I�𒆂̐ŋ敪��Ԃ��܂��B
                 */
		private Component getSelectedTaxType()
		{
			if(rdoTaxUnit.isSelected())
			{
				return	rdoTaxUnit;
			}
			
			return	rdoTaxBlank;
		}
		
                /**
                 * �I�𒆂̕��ނ�Ԃ��܂��B
                 */
		private Component getSelectedTechType()
		{
			if(rdoTech.isSelected())
			{
				return	rdoTech;
                        }
                        else if(rdoTechClaim.isSelected()) {
        			return	rdoTechClaim;
                        }
			
                        return	rdoTech;
		}
	}
}

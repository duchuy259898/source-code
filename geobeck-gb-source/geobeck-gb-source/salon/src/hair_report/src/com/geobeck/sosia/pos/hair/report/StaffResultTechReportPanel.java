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
import com.geobeck.sosia.pos.hair.report.logic.ReportLogic2;
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
        //LVTu start add 2021/02/22 #19396
        private static final String HEADQUARTERS = "株式会社ビューティーコンテンツファクトリー";
        //LVTu end add 2021/02/22 #19396
        // start add 20130805 nakhoa 役務機能使用有無の制御追加
	private boolean isLoading = true;
        // end add 20130805 nakhoa 役務機能使用有無の制御追加
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
		this.setPath("帳票出力");
		this.setTitle("担当別技術成績");
		this.setKeyListener();

                target.addItem(SystemInfo.getGroup());
                SystemInfo.getGroup().addGroupDataToJComboBox(target, 3);
                if (!SystemInfo.isGroup()) {
                    target.setSelectedItem(SystemInfo.getCurrentShop());
                    // start add 20130805 nakhoa 役務機能使用有無の制御追加
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
                // end add 20130805 nakhoa 役務機能使用有無の制御追加
                //対象期間の設定【技術売上】
                this.cmbTargetPeriodStartDate.setDate(new Date());
		this.cmbTargetPeriodEndDate.setDate(new Date());
                //対象期間の設定【担当別分類構成比】
		this.cmbTargetPeriodStartDate1.setDate(new Date());
		this.cmbTargetPeriodEndDate1.setDate(new Date());
                //対象期間の設定【按分成績】
		this.cmbTargetPeriodStartDate2.setDate(new Date());
		this.cmbTargetPeriodEndDate2.setDate(new Date());
                //対象期間の設定【コース契約成績】
                this.cmbTargetPeriodStartDate4.setDate(new Date());
                this.cmbTargetPeriodEndDate4.setDate(new Date());
                //対象期間の設定【コース消化成績】
                this.cmbTargetPeriodStartDate5.setDate(new Date());
                this.cmbTargetPeriodEndDate5.setDate(new Date());

		//税抜、税込の初期設定
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
                // start add 20130805 nakhoa 役務機能使用有無の制御追加
                isLoading = false;
                // end add 20130805 nakhoa 役務機能使用有無の制御追加
	}
        
        //LVTu start add 2015/12/01 Bug Bug #44791
        private boolean checkShopCourseFlag(MstGroup mg) {
                //グループ
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
        staffGroup = new javax.swing.ButtonGroup();
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
        lblTargetPeriod5 = new javax.swing.JLabel();
        rdoSuryo = new javax.swing.JRadioButton();
        rdoHikaiin = new javax.swing.JRadioButton();
        rdoNinzu = new javax.swing.JRadioButton();
        rdoKaiin = new javax.swing.JRadioButton();
        rdoMainCharge = new javax.swing.JRadioButton();
        rdoTreatmentCharge = new javax.swing.JRadioButton();
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

        lblTax.setText("税区分");
        lblTax.setFocusCycleRoot(true);

        taxGroup.add(rdoTaxBlank);
        rdoTaxBlank.setText("税抜");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setFocusCycleRoot(true);
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);

        taxGroup.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("税込");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setFocusCycleRoot(true);
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);

        targetLabel.setText("対象");
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("技術成績"));
        jPanel1.setOpaque(false);

        lblTargetPeriod.setText("対象期間");
        lblTargetPeriod.setFocusCycleRoot(true);

        cmbTargetPeriodStartDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDateFocusGained(evt);
            }
        });

        jLabel1.setText("～");
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("担当別分類構成比"));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(396, 160));
        jPanel2.setLayout(null);

        lblTargetPeriod1.setText("出力対象");
        lblTargetPeriod1.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod1);
        lblTargetPeriod1.setBounds(10, 150, 60, 21);

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
        jLabel2.setText("～");
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
        rdoTech.setText("技術分類構成比");
        rdoTech.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTech.setFocusCycleRoot(true);
        rdoTech.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTech.setOpaque(false);
        jPanel2.add(rdoTech);
        rdoTech.setBounds(10, 20, 140, 21);

        techGroup.add(rdoTechClaim);
        rdoTechClaim.setText("技術クレーム分類構成比");
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

        lblTargetPeriod3.setText("対象期間");
        lblTargetPeriod3.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod3);
        lblTargetPeriod3.setBounds(10, 50, 60, 21);

        lblTargetPeriod4.setText("出力条件");
        lblTargetPeriod4.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod4);
        lblTargetPeriod4.setBounds(10, 119, 60, 21);

        lblTargetPeriod5.setText("担当区分");
        lblTargetPeriod5.setFocusCycleRoot(true);
        jPanel2.add(lblTargetPeriod5);
        lblTargetPeriod5.setBounds(10, 87, 60, 21);

        countGroup.add(rdoSuryo);
        rdoSuryo.setText("数量");
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
        rdoSuryo.setBounds(160, 119, 50, 21);

        customerGroup.add(rdoHikaiin);
        rdoHikaiin.setText("非会員含む");
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
        rdoHikaiin.setBounds(160, 150, 80, 21);

        countGroup.add(rdoNinzu);
        rdoNinzu.setSelected(true);
        rdoNinzu.setText("人数");
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
        rdoNinzu.setBounds(80, 119, 50, 21);

        customerGroup.add(rdoKaiin);
        rdoKaiin.setSelected(true);
        rdoKaiin.setText("会員のみ");
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
        rdoKaiin.setBounds(80, 150, 70, 21);

        staffGroup.add(rdoMainCharge);
        rdoMainCharge.setSelected(true);
        rdoMainCharge.setText("主担当");
        rdoMainCharge.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMainCharge.setFocusCycleRoot(true);
        rdoMainCharge.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoMainCharge.setOpaque(false);
        jPanel2.add(rdoMainCharge);
        rdoMainCharge.setBounds(80, 87, 65, 21);

        staffGroup.add(rdoTreatmentCharge);
        rdoTreatmentCharge.setText("施術担当");
        rdoTreatmentCharge.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTreatmentCharge.setFocusCycleRoot(true);
        rdoTreatmentCharge.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTreatmentCharge.setOpaque(false);
        jPanel2.add(rdoTreatmentCharge);
        rdoTreatmentCharge.setBounds(160, 87, 80, 21);

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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("担当別按分成績"));
        jPanel3.setOpaque(false);

        lblTargetPeriod2.setText("対象期間");
        lblTargetPeriod2.setFocusCycleRoot(true);

        cmbTargetPeriodStartDate2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate2.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDate2FocusGained(evt);
            }
        });

        jLabel3.setText("～");
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

        orderDisplayLabel.setText("表示順");
        orderDisplayLabel.setFocusCycleRoot(true);

        cmbOrderDisplay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "金額", "ポイント" }));
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

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("コース契約成績"));
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

        jLabel5.setText("～");
        jLabel5.setFocusCycleRoot(true);

        lblTargetPeriod6.setText("対象期間");
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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("コース消化成績"));
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

        lblTargetPeriod7.setText("対象期間");
        lblTargetPeriod7.setFocusCycleRoot(true);

        cmbTargetPeriodEndDate5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate5.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDate5FocusGained(evt);
            }
        });

        jLabel6.setText("～");
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
                "業態名", "業態ID"
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

        lblCategory.setText("集計業態");

        tblSelectCategoryName.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "業態名", "業態ID"
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
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 186, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

        private void targetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_targetItemStateChanged

            if(target.getSelectedItem() instanceof MstGroup || (this.getSelectedShop() != null && this.getSelectedShop().isProportionally())) {
                //グループ、あるいは按分を使用する店舗のみ表示する
                jPanel3.setVisible(true);
            } else {
                //上記以外の場合、表示しない
                jPanel3.setVisible(false);
            }
            // start add 20130805 nakhoa 役務機能使用有無の制御追加
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
            // end add 20130805 nakhoa 役務機能使用有無の制御追加

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
         //Luc start add 20141202 ticket #33404 業態IDの一覧
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
                       shopCategoryNameList += "、";
                   }
               }
           }
        }
        paramBean.setCategoryIDList(shopCategoryIdList);
        paramBean.setCategoryNameList(shopCategoryNameList);
        paramBean.setUseShopCategory(useShopCategory);
        //Luc end add 20141202 ticket #33404 業態IDの一覧
        //グループ
        if(target.getSelectedItem() instanceof MstGroup) {
            MstGroup	mg	=	(MstGroup)target.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
        }
        //店舗
        else if(target.getSelectedItem() instanceof MstShop) {
            MstShop		ms	=	(MstShop)target.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }
        
        //対象となる店舗が存在しない場合
        if(paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //税区分
        if(this.rdoTaxBlank.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// 税区分(税抜き)
        } else if(this.rdoTaxUnit.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// 税区分(税込み)
        }
        
        //割引の区分の設定
        Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
        paramBean.setDiscountType(discountType);
        
        //対象期間
        paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate2.getDateStr());
        paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate2.getDateStr());
        paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate2.getDate());
        paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate2.getDate());
        
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(this.cmbTargetPeriodStartDate2.getDate());
        end.setTime(this.cmbTargetPeriodEndDate2.getDate());
        
        if(start.compareTo(end) != 0) {
            //対象期間が開始日＞終了日ならエラー
            if(start.after(end)) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        //出力処理
        boolean logicResult = true;
        try {
            //按分成績
            logicResult = logic.outStaffReportProportionallyDistribution(paramBean,cmbOrderDisplay.getSelectedIndex());
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // エラー時
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
         //Luc start add 20141202 ticket #33404 業態IDの一覧
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
                       shopCategoryNameList += "、";
                   }
               }
           }
        }
        paramBean.setCategoryIDList(shopCategoryIdList);
        paramBean.setCategoryNameList(shopCategoryNameList);
        paramBean.setUseShopCategory(useShopCategory);
        //Luc end add 20141202 ticket #33404 業態IDの一覧
        //グループ
        if(target.getSelectedItem() instanceof MstGroup) {
            MstGroup	mg	=	(MstGroup)target.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
        }
        //店舗
        else if(target.getSelectedItem() instanceof MstShop) {
            MstShop		ms	=	(MstShop)target.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }
        
        //対象となる店舗が存在しない場合
        if(paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //税区分
        if(this.rdoTaxBlank.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// 税区分(税抜き)
        } else if(this.rdoTaxUnit.isSelected()) {
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// 税区分(税込み)
        }
        
        //割引の区分の設定
        Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
        paramBean.setDiscountType(discountType);
        
        //対象期間
        paramBean.setTargetStartDate(this.cmbTargetPeriodStartDate1.getDateStr());
        paramBean.setTargetEndDate(this.cmbTargetPeriodEndDate1.getDateStr());
        paramBean.setTargetStartDateObj(this.cmbTargetPeriodStartDate1.getDate());
        paramBean.setTargetEndDateObj(this.cmbTargetPeriodEndDate1.getDate());
        
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(this.cmbTargetPeriodStartDate1.getDate());
        end.setTime(this.cmbTargetPeriodEndDate1.getDate());
        
        if(start.compareTo(end) != 0) {
            //対象期間が開始日＞終了日ならエラー
            if(start.after(end)) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int customerDivision = rdoKaiin.isSelected() ? 1 : 2;

        //出力処理
        boolean logicResult = true;
        try {
            // IVS start add 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
             boolean isMainCharge = true;
            if(rdoMainCharge.isSelected()){
                isMainCharge = true;
            }
            else{
                isMainCharge = false;
            }
            // IVS end add 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
            if(this.rdoTech.isSelected()) {
                //技術分類構成比
                if(rdoNinzu.isSelected()){
                    //人数
                    // IVS start edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                    //logicResult = logic.outStaffReportClassificationRatio(paramBean,1,1,customerDivision);
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,1,1,customerDivision,isMainCharge);
                    // IVS end edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                } else if (rdoSuryo.isSelected()){
                    //数量
                    // IVS start edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                    //logicResult = logic.outStaffReportClassificationRatio(paramBean,1,2,customerDivision);
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,1,2,customerDivision,isMainCharge);
                    // IVS end edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                }
            } else if(this.rdoTechClaim.isSelected()) {
                //技術クレーム分類構成比
                if(rdoNinzu.isSelected()){
                    //人数
                    // IVS start edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                    //logicResult = logic.outStaffReportClassificationRatio(paramBean,3,1,customerDivision);
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,3,1,customerDivision,isMainCharge);
                    // IVS end edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                } else if (rdoSuryo.isSelected()){
                    //数量
                    // IVS start edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                    //logicResult = logic.outStaffReportClassificationRatio(paramBean,3,2,customerDivision);
                    logicResult = logic.outStaffReportClassificationRatio(paramBean,3,2,customerDivision,isMainCharge);
                    // IVS end edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                }
            } else {
                // 『出力対象の分類が選択されていません』
                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(1102, "出力対象の分類"),
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
        
        // エラー時
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
        //IVS_vtnhan start add 20140723 MASHU_担当再来分析
        this.moveMutiCategory(true);
        //IVS_vtnhan end add 20140723 MASHU_担当再来分析
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
        //IVS_vtnhan start add 20140723 MASHU_担当再来分析
        this.moveCategoryAll(true);
        //IVS_vtnhan end add 20140723 MASHU_担当再来分析
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
        //IVS_vtnhan start add 20140723 MASHU_担当再来分析
        this.moveMutiCategory(false);
        //IVS_vtnhan end add 20140723 MASHU_担当再来分析
    }//GEN-LAST:event_releaseButtonActionPerformed

    private void releaseAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseAllButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_担当再来分析
        this.moveCategoryAll(false);
        //IVS_vtnhan end add 20140723 MASHU_担当再来分析
    }//GEN-LAST:event_releaseAllButtonActionPerformed

    private void tblSelectCategoryNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelectCategoryNameMouseClicked

    }//GEN-LAST:event_tblSelectCategoryNameMouseClicked

    private void tblSelectCategoryNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblSelectCategoryNamePropertyChange

    }//GEN-LAST:event_tblSelectCategoryNamePropertyChange

    private void targetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetActionPerformed
        // TODO add your handling code here:
         //luc 
        this.chargeDataCombo();
        //業態を表示する。
        showShopCategory();
        
    }//GEN-LAST:event_targetActionPerformed
    private void chargeDataCombo() {   

        mrsRef = new MstShopCategorys();
        mrsUse = new MstShopCategorys();
        //グループ
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
        //店舗
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
        * init data for table 業態名 left with muti shop
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
       /* init data for table 業態名 left with group shop
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
      /* control design screen follow combo 対象
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
            //集計業態
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
            //Luc start add 20141202 ticket #33404 業態IDの一覧
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
                           shopCategoryNameList += "、";
                       }
                   }
               }
            }
            paramBean.setCategoryIDList(shopCategoryIdList);
            paramBean.setCategoryNameList(shopCategoryNameList);
            paramBean.setUseShopCategory(useShopCategory);
            //Luc end add 20141202 ticket #33404 業態IDの一覧
            //グループ
            if(target.getSelectedItem() instanceof MstGroup)
            {
                    MstGroup	mg	=	(MstGroup)target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
            }
            //店舗
            else if(target.getSelectedItem() instanceof MstShop)
            {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
            }

            //対象となる店舗が存在しない場合
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
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// 税区分(税抜き)
            }
            else if(this.rdoTaxUnit.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// 税区分(税込み)
            }

            //割引の区分の設定
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
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                                                    this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    }
            }



            //出力処理
            boolean logicResult = true;
            // 業務報告
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

            // エラー時
            if(!logicResult)
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
            }            
        }
    
        /**
         * 担当別技術成績　コース契約成績出力
         */
        private void print4(){
            ReportLogic logic = new ReportLogic();
            //LVTu start add 2021/02/22 #19396
            ReportLogic2 logic2 = new ReportLogic2();
            //LVTu end add 2021/02/22 #19396
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
             //Luc start add 20141202 ticket #33404 業態IDの一覧
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
                           shopCategoryNameList += "、";
                       }
                   }
               }
            }
            paramBean.setCategoryIDList(shopCategoryIdList);
            paramBean.setCategoryNameList(shopCategoryNameList);
            
            paramBean.setListCategoryId(shopCategoryIdList);
            paramBean.setListCategoryName(shopCategoryNameList);
            paramBean.setUseShopCategory(useShopCategory);
            //Luc end add 20141202 ticket #33404 業態IDの一覧
            //グループ
            if(target.getSelectedItem() instanceof MstGroup)
            {
                    MstGroup	mg	=	(MstGroup)target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
            }
            //店舗
            else if(target.getSelectedItem() instanceof MstShop)
            {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
            }

            //対象となる店舗が存在しない場合
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
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// 税区分(税抜き)
            }
            else if(this.rdoTaxUnit.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// 税区分(税込み)
            }

            //割引の区分の設定
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
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                                                    this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    }
            }


            //出力処理
            boolean logicResult = true;
            // 業務報告
            try
            {
                // LVTu start edit 2021/02/22 #19396
                if ( paramBean.getTargetName().equals(HEADQUARTERS) && SystemInfo.getCurrentShop().getShopID() == 0) {
                    logicResult = logic2.outStaffReportCourseContract(paramBean);
                } else {
                    logicResult = logic.outStaffReportCourseContract(paramBean);
                }
                // LVTu end edit 2021/02/22 #19396

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

            // エラー時
            if(!logicResult)
            {
                    MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(4001),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
            }

        }

        /**
         * 担当別技術成績　コース消化成績出力
         */
        private void print5(){
            ReportLogic logic = new ReportLogic();
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
            //Luc start add 20141202 ticket #33404 業態IDの一覧
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
                           shopCategoryNameList += "、";
                       }
                   }
               }
            }
            paramBean.setCategoryIDList(shopCategoryIdList);
            paramBean.setCategoryNameList(shopCategoryNameList);
            paramBean.setUseShopCategory(useShopCategory);
            //Luc end add 20141202 ticket #33404 業態IDの一覧
            //グループ
            if(target.getSelectedItem() instanceof MstGroup)
            {
                    MstGroup	mg	=	(MstGroup)target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
            }
            //店舗
            else if(target.getSelectedItem() instanceof MstShop)
            {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
            }

            //対象となる店舗が存在しない場合
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
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);	// 税区分(税抜き)
            }
            else if(this.rdoTaxUnit.isSelected())
            {
                    paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);	// 税区分(税込み)
            }

            //割引の区分の設定
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
                                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                                                    this.getTitle(),
                                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    }
            }


            //出力処理
            boolean logicResult = true;
            // 業務報告
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

            // エラー時
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
    private javax.swing.JLabel lblTargetPeriod5;
    private javax.swing.JLabel lblTargetPeriod6;
    private javax.swing.JLabel lblTargetPeriod7;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel orderDisplayLabel;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JRadioButton rdoHikaiin;
    private javax.swing.JRadioButton rdoKaiin;
    private javax.swing.JRadioButton rdoMainCharge;
    private javax.swing.JRadioButton rdoNinzu;
    private javax.swing.JRadioButton rdoSuryo;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private javax.swing.JRadioButton rdoTech;
    private javax.swing.JRadioButton rdoTechClaim;
    private javax.swing.JRadioButton rdoTreatmentCharge;
    private javax.swing.JScrollPane referenceCategoryLeftScrollPane;
    private javax.swing.JButton releaseAllButton;
    private javax.swing.JButton releaseButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JScrollPane selectCategoryRightScrollPane;
    private javax.swing.ButtonGroup staffGroup;
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
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
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
	 * 選択されている店舗を取得する。
	 * @return 選択されている店舗
	 */
	private MstShop getSelectedShop()
	{
		if(0 <= target.getSelectedIndex())
			return	(MstShop)target.getSelectedItem();
		else
			return	null;
	}
	
	/**
	 * 担当別技術成績画面用FocusTraversalPolicyを取得する。
	 * @return  担当別技術成績画面用FocusTraversalPolicy
	 */
	public StaffResultTechReportFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	
	/**
	 * 担当別技術成績画面用FocusTraversalPolicy
	 */
	private class StaffResultTechReportFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponentStaffResultTechReportFocusTraversalPolicy。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
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
                            // IVS start edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                            //if(rdoNinzu.isSelected()){
                                //return rdoNinzu;
                            //}
                            //else{
                                //return rdoSuryo;
                            //}
                            if(rdoMainCharge.isSelected()){
                                return rdoMainCharge;
                            }
                            else{
                                return rdoTreatmentCharge;
                            }
                            // IVS end edit 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
			}
                        // IVS start add 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
                        else if (aComponent.equals(rdoTreatmentCharge)||
                                 aComponent.equals(rdoMainCharge) )
			{
                            if(rdoNinzu.isSelected()){
                                return rdoNinzu;
                            }
                            else{
                                return rdoSuryo;
                            }
			}
                        // IVS end add 20220920 【担当別分類構成比】の出力条件に「担当区分（主担当・施術担当）」の条件を追加
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
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
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
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return target;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
                        return cmbOrderDisplay;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return target;
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
			return target;
		}
		
                /**
                 * 選択中の税区分を返します。
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
                 * 選択中の分類を返します。
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

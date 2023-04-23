/*
 * BusinessReportPanel.java
 *
 * Created on 2006/10/19, 10:39
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.report.bean.*;
import com.geobeck.sosia.pos.hair.report.logic.ReportLogic;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.util.*;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author katagiri
 */
public class BusinessReportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private MstStaffs staffs = null;
    private boolean isLoading = true;
    private MstShopCategorys		mscg		=	new MstShopCategorys();
    private MstShopCategorys mrsRef = new MstShopCategorys();
    private MstShopCategorys mrsUse = new MstShopCategorys();
    private int useShopCategory = 0;
    private boolean isHideCategory = false;

    /**
     * Creates new form BusinessReportPanel
     */
    public BusinessReportPanel() {
        initComponents();
        addMouseCursorChange();
        //IVS_TMTrong start edit 2015/10/07 New request #43061
        this.setSize(720, 500);
         //IVS_TMTrong end edit 2015/10/07 New request #43061
        this.setPath("帳票出力");
        this.setTitle("業務報告");
        this.setKeyListener();
        SystemInfo.initGroupShopComponents(target, 3);
        this.initStaff();
        cmbTargetPeriodStart.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        cmbTargetPeriodEnd.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());

        //税抜、税込の初期設定
        if (SystemInfo.getAccountSetting().getReportPriceType() == 0) {
            rdoTaxBlank.setSelected(false);
            rdoTaxUnit.setSelected(true);
        } else {
            rdoTaxBlank.setSelected(true);
            rdoTaxUnit.setSelected(false);
        }

        this.initDisplayControl();
        // start add 20130802 nakhoa 役務機能使用有無の制御追加

        if (target.getSelectedItem() != null) {
            rdoCourseReport.setVisible(false);
            rdoConsumptionCourseReport.setVisible(false);
            if (target.getSelectedItem() instanceof MstShop) {
                MstShop shop = new MstShop();
                shop = (MstShop) target.getSelectedItem();
                //IVS NNTUAN START EDIT 20131015
                //database still not support yet . Request 13506
                    /*
                 if(shop.getCourseFlag() == 1){
                 rdoCourseReport.setVisible(true);
                 rdoConsumptionCourseReport.setVisible(true);
                 }*/

                rdoCourseReport.setVisible(true);
                rdoConsumptionCourseReport.setVisible(true);

                //IVS NNTUAN END EDIT 20131015
            }            
            //Luc start add 20150703 #39346
            if (target.getSelectedItem() instanceof MstGroup) {
                MstGroup group = new MstGroup();
                group = (MstGroup) target.getSelectedItem();
                rdoCourseReport.setVisible(false);
                rdoConsumptionCourseReport.setVisible(false); 
                //LVTu start edit 2015/12/01 Bug Bug #44791
//                for(MstShop shop :group.getShops()) {
//                    if(shop.getCourseFlag()==1) {
//                        rdoCourseReport.setVisible(true);
//                        rdoConsumptionCourseReport.setVisible(true); 
//                        //LVTu start edit 2015/10/28 Bug #44067
//                        //return;
//                        //LVTu end edit 2015/10/28 Bug #44067
//                    }
//                }
                
                if (checkShopCourseFlag(group)) {
                    rdoCourseReport.setVisible(true);
                    rdoConsumptionCourseReport.setVisible(true); 
                }
                //LVTu end edit 2015/12/01 Bug Bug #44791
            }
            //Luc start add 20150703 #39346
        }

        isLoading = false;
        // end add 20130802 nakhoa 役務機能使用有無の制御追加

        showShopCategory();
        
        
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

    private void showShopCategory() {
        //集計業態
        MstShop selectedShop =  null;
        MstGroup selectedGroup = null;
        if(target.getSelectedItem() instanceof MstGroup) {
          selectedGroup = (MstGroup) target.getSelectedItem();
          
            if(selectedGroup!=null && SystemInfo.getSetteing().isUseShopCategory()) {
              jPanel4.setVisible(true);
              jPanel6.setLocation(jPanel6.getX(),jPanel4.getY()+jPanel4.getHeight()+10);
            }else {
                jPanel6.setLocation(jPanel6.getX(),jPanel4.getY());
                jPanel4.setVisible(false); 
            }
        }
        if(target.getSelectedItem() instanceof MstShop)
        {
            selectedShop = (MstShop)target.getSelectedItem();
            if(selectedShop.getUseShopCategory()==1){
            jPanel4.setVisible(true);
            jPanel6.setLocation(jPanel6.getX(),jPanel4.getY()+jPanel4.getHeight()+10);
            }else {
            
            jPanel6.setLocation(jPanel6.getX(),jPanel4.getY());
            jPanel4.setVisible(false);
            }
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reportTypeGroup = new javax.swing.ButtonGroup();
        taxGroup = new javax.swing.ButtonGroup();
        staffGroup = new javax.swing.ButtonGroup();
        newVisitGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        targetLabel = new javax.swing.JLabel();
        target = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblTargetPeriod = new javax.swing.JLabel();
        cmbTargetPeriodStart = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel1 = new javax.swing.JLabel();
        cmbTargetPeriodEnd = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jPanel4 = new javax.swing.JPanel();
        referenceCategoryLeftScrollPane = new javax.swing.JScrollPane();
        tblReferenceCategoryName = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        releaseButton = new javax.swing.JButton();
        releaseAllButton = new javax.swing.JButton();
        selectCategoryRightScrollPane = new javax.swing.JScrollPane();
        tblSelectCategoryName = new javax.swing.JTable();
        lblCategory = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblTax = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        lblStaffType = new javax.swing.JLabel();
        rdoMainStaff = new javax.swing.JRadioButton();
        rdoTechStaff = new javax.swing.JRadioButton();
        staff = new javax.swing.JComboBox();
        lblNewVisit = new javax.swing.JLabel();
        rdoNewVisitCurrent = new javax.swing.JRadioButton();
        rdoNewVisitAll = new javax.swing.JRadioButton();
        lblStaff = new javax.swing.JLabel();
        btnOutputExcel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        rdoBusinessReport = new javax.swing.JRadioButton();
        rdoTechnicalReport = new javax.swing.JRadioButton();
        rdoItemReport = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        rdoCourseReport = new javax.swing.JRadioButton();
        rdoConsumptionCourseReport = new javax.swing.JRadioButton();
        rdoSaleDetailReport = new javax.swing.JRadioButton();
        rdoSalesListReport = new javax.swing.JRadioButton();

        setFocusCycleRoot(true);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        targetLabel.setText("対象");
        jPanel1.add(targetLabel);
        targetLabel.setBounds(0, 10, 70, 20);

        target.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetActionPerformed(evt);
            }
        });
        jPanel1.add(target);
        target.setBounds(80, 10, 210, 20);

        lblTargetPeriod.setText("対象期間");
        jPanel1.add(lblTargetPeriod);
        lblTargetPeriod.setBounds(0, 40, 70, 21);

        cmbTargetPeriodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStart.setForeground(java.awt.Color.white);
        cmbTargetPeriodStart.setMaximumSize(new java.awt.Dimension(65, 20));
        cmbTargetPeriodStart.setMinimumSize(new java.awt.Dimension(65, 20));
        cmbTargetPeriodStart.setPreferredSize(new java.awt.Dimension(85, 20));
        cmbTargetPeriodStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartFocusGained(evt);
            }
        });
        jPanel1.add(cmbTargetPeriodStart);
        cmbTargetPeriodStart.setBounds(80, 40, 90, 21);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("～");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(170, 40, 20, 21);

        cmbTargetPeriodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndFocusGained(evt);
            }
        });
        jPanel1.add(cmbTargetPeriodEnd);
        cmbTargetPeriodEnd.setBounds(190, 40, 88, 21);

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

        jPanel5.setOpaque(false);

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

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

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

        lblCategory.setText("集計業態");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(lblCategory)
                .add(28, 28, 28)
                .add(referenceCategoryLeftScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectCategoryRightScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblCategory)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(selectCategoryRightScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .add(referenceCategoryLeftScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(0, 70, 720, 171);

        jPanel6.setOpaque(false);

        lblTax.setText("税区分");

        taxGroup.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("税込");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);

        taxGroup.add(rdoTaxBlank);
        rdoTaxBlank.setText("税抜");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);

        lblStaffType.setText("担当区分");

        staffGroup.add(rdoMainStaff);
        rdoMainStaff.setSelected(true);
        rdoMainStaff.setText("主担当");
        rdoMainStaff.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMainStaff.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoMainStaff.setOpaque(false);

        staffGroup.add(rdoTechStaff);
        rdoTechStaff.setText("施術担当");
        rdoTechStaff.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechStaff.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTechStaff.setOpaque(false);

        staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblNewVisit.setText("新規区分");

        newVisitGroup.add(rdoNewVisitCurrent);
        rdoNewVisitCurrent.setSelected(true);
        rdoNewVisitCurrent.setText("自店新規");
        rdoNewVisitCurrent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNewVisitCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoNewVisitCurrent.setOpaque(false);

        newVisitGroup.add(rdoNewVisitAll);
        rdoNewVisitAll.setText("全店新規");
        rdoNewVisitAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNewVisitAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoNewVisitAll.setOpaque(false);

        lblStaff.setText("担当者");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(lblStaffType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(28, 28, 28)
                        .add(rdoMainStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(20, 20, 20)
                        .add(rdoTechStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(lblStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(28, 28, 28)
                        .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(lblNewVisit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(28, 28, 28)
                        .add(rdoNewVisitCurrent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(rdoNewVisitAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(lblTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(28, 28, 28)
                        .add(rdoTaxUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(20, 20, 20)
                        .add(rdoTaxBlank, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(0, 422, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblStaffType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoMainStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTechStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblNewVisit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoNewVisitCurrent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoNewVisitAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTaxUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTaxBlank, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 15, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6);
        jPanel6.setBounds(0, 250, 710, 125);

        btnOutputExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutputExcel.setBorderPainted(false);
        btnOutputExcel.setContentAreaFilled(false);
        btnOutputExcel.setFocusCycleRoot(true);
        btnOutputExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutputExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputExcelActionPerformed(evt);
            }
        });

        jPanel2.setOpaque(false);

        reportTypeGroup.add(rdoBusinessReport);
        rdoBusinessReport.setSelected(true);
        rdoBusinessReport.setText("業務報告");
        rdoBusinessReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoBusinessReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoBusinessReport.setMaximumSize(new java.awt.Dimension(90, 20));
        rdoBusinessReport.setMinimumSize(new java.awt.Dimension(90, 20));
        rdoBusinessReport.setOpaque(false);
        rdoBusinessReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBusinessReportActionPerformed(evt);
            }
        });

        reportTypeGroup.add(rdoTechnicalReport);
        rdoTechnicalReport.setText("技術詳細");
        rdoTechnicalReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechnicalReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTechnicalReport.setOpaque(false);
        rdoTechnicalReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTechnicalReportActionPerformed(evt);
            }
        });

        reportTypeGroup.add(rdoItemReport);
        rdoItemReport.setText("商品詳細");
        rdoItemReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoItemReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoItemReport.setOpaque(false);
        rdoItemReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoItemReportActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(rdoBusinessReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(rdoTechnicalReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(rdoItemReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoBusinessReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTechnicalReport)
                    .add(rdoItemReport))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setOpaque(false);

        reportTypeGroup.add(rdoCourseReport);
        rdoCourseReport.setText("コース詳細");
        rdoCourseReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoCourseReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoCourseReport.setOpaque(false);
        rdoCourseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCourseReportActionPerformed(evt);
            }
        });

        reportTypeGroup.add(rdoConsumptionCourseReport);
        rdoConsumptionCourseReport.setText("消化一覧");
        rdoConsumptionCourseReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoConsumptionCourseReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoConsumptionCourseReport.setOpaque(false);
        rdoConsumptionCourseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoConsumptionCourseReportActionPerformed(evt);
            }
        });

        reportTypeGroup.add(rdoSaleDetailReport);
        rdoSaleDetailReport.setText("売上明細");
        rdoSaleDetailReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSaleDetailReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSaleDetailReport.setOpaque(false);
        rdoSaleDetailReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSaleDetailReportActionPerformed(evt);
            }
        });

        reportTypeGroup.add(rdoSalesListReport);
        rdoSalesListReport.setText("売上一覧");
        rdoSalesListReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSalesListReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSalesListReport.setOpaque(false);
        rdoSalesListReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSalesListReportActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(rdoCourseReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rdoConsumptionCourseReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(rdoSaleDetailReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(rdoSalesListReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rdoCourseReport)
                    .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(rdoSaleDetailReport)
                        .add(rdoConsumptionCourseReport)
                        .add(rdoSalesListReport)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(86, 86, 86)
                                .add(btnOutputExcel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 351, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(13, 13, 13)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOutputExcel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 403, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initDisplayControl() {

        rdoMainStaff.setEnabled(true);
        lblStaffType.setEnabled(true);
        lblStaff.setEnabled(true);
        staff.setEnabled(true);
        lblNewVisit.setEnabled(true);
        rdoNewVisitCurrent.setEnabled(true);
        rdoNewVisitAll.setEnabled(true);
        if (rdoBusinessReport.isSelected()) {
            rdoMainStaff.setSelected(true);
            rdoTechStaff.setEnabled(false);
            lblNewVisit.setEnabled(true);
            rdoNewVisitCurrent.setEnabled(true);
            rdoNewVisitAll.setEnabled(true);
        } else if (rdoSaleDetailReport.isSelected()) {
            // TODO add your handling code here:
            rdoMainStaff.setEnabled(false);
            lblStaffType.setEnabled(false);
            lblStaff.setEnabled(false);
            staff.setEnabled(false);
            lblNewVisit.setEnabled(false);
            rdoNewVisitCurrent.setEnabled(false);
            rdoNewVisitAll.setEnabled(false);
            rdoTechStaff.setEnabled(false);
        }
        //IVS_TMTrong start add 2015/10/02 New request #43049
        else if(rdoSalesListReport.isSelected()){
            rdoMainStaff.setEnabled(true);
            rdoTechStaff.setEnabled(false);
            rdoNewVisitCurrent.setEnabled(false);
            rdoNewVisitAll.setEnabled(false);
            //IVS_TMTrong start add 2015/10/07
            rdoMainStaff.setSelected(true);
            rdoNewVisitCurrent.setSelected(true);
            lblNewVisit.setEnabled(false);
            //IVS_TMTrong end add 2015/10/07
        }
        //IVS_TMTrong end add 2015/10/02 New request #43049
        else {
            rdoTechStaff.setEnabled(true);
            lblNewVisit.setEnabled(false);
            rdoNewVisitCurrent.setEnabled(false);
            rdoNewVisitAll.setEnabled(false);
        }


    }

    private void rdoItemReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoItemReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoItemReportActionPerformed

    private void rdoBusinessReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBusinessReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoBusinessReportActionPerformed

    private void btnOutputExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutputExcelActionPerformed

        //業務報告
        if (this.rdoBusinessReport.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(210)) {
                return;
            }
        }

        //技術詳細
        if (this.rdoTechnicalReport.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(211)) {
                return;
            }
        }

        //商品詳細
        if (this.rdoItemReport.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(212)) {
                return;
            }
        }

        //コース詳細
        if (this.rdoCourseReport.isSelected()) {
//                if (!SystemInfo.checkAuthorityPassword(210)) return;
        }

        //消化一覧
        if (this.rdoConsumptionCourseReport.isSelected()) {
//                if (!SystemInfo.checkAuthorityPassword(210)) return;
        }

        btnOutputExcel.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            ReportLogic logic = new ReportLogic();
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_BUSINESS);

            //割引の区分の設定
            Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
            paramBean.setDiscountType(discountType);

            // 税区分(税抜き)
            if (this.rdoTaxBlank.isSelected()) {
                paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);
            } // 税区分(税込み)
            else if (this.rdoTaxUnit.isSelected()) {
                paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);
            }

            if (this.rdoMainStaff.isSelected()) {
                paramBean.setStaffType(ReportParameterBean.STAFF_TYPE_MAIN);
            } else {
                paramBean.setStaffType(ReportParameterBean.STAFF_TYPE_TECH);
            }

            if (this.rdoNewVisitCurrent.isSelected()) {
                paramBean.setNewVisitType(ReportParameterBean.NEW_VISIT_CURRENT);
            } else {
                paramBean.setNewVisitType(ReportParameterBean.NEW_VISIT_ALL);
            }

            boolean logicResult = true;

            try {
                //グループ
                if (target.getSelectedItem() instanceof MstGroup) {
                    MstGroup mg = (MstGroup) target.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
                    paramBean.setUseShopCategory(1);
                } //店舗
                else if (target.getSelectedItem() instanceof MstShop) {
                    MstShop ms = (MstShop) target.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
                    paramBean.setUseShopCategory(ms.getUseShopCategory());
                }

                //対象となる店舗が存在しない場合
                if (paramBean.getShopIDList().equals("")) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(4001),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (0 < staff.getSelectedIndex()) {
                    MstStaff ms = (MstStaff) staff.getSelectedItem();
                    paramBean.setStaffId(ms.getStaffID());
                    paramBean.setStaffName(ms.getFullStaffName());
                }

                if (this.cmbTargetPeriodStart.getDate() == null
                        || this.cmbTargetPeriodEnd.getDate() == null) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();

                start.setTime(this.cmbTargetPeriodStart.getDate());
                end.setTime(this.cmbTargetPeriodEnd.getDate());
                if (start.compareTo(end) != 0) {
                    if (start.after(end)) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                paramBean.setTargetStartDate(this.cmbTargetPeriodStart.getDateStr());
                paramBean.setTargetEndDate(this.cmbTargetPeriodEnd.getDateStr());

                Calendar cal = Calendar.getInstance();
                cal.setTime(cmbTargetPeriodStart.getDate());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                paramBean.setTargetStartDateObj(cal.getTime());

                cal.setTime(cmbTargetPeriodEnd.getDate());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                paramBean.setTargetEndDateObj(cal.getTime());

                //業態IDの一覧
                 String lstShopCategoryId = "";
                String lstNameShopCategory = "";
                if(mrsUse.size() >0){
                    int count = 0;
                    for(MstShopCategory category : mrsUse){
                        if(category.getShopCategoryId() != null){
                            lstShopCategoryId += category.getShopCategoryId();
                            lstNameShopCategory += category.getShopClassName();
                            count++;
                            if(count < mrsUse.size()){
                                lstShopCategoryId += ",";
                                lstNameShopCategory += "、";
                            }
                        }
                    }
                }
                paramBean.setListCategoryId(lstShopCategoryId);
                paramBean.setListCategoryName(lstNameShopCategory);
                ConnectionWrapper cw = SystemInfo.getConnection();

                //出力処理

                //業務報告
                if (this.rdoBusinessReport.isSelected()) {
                    paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_BUSINESS_ALL);
                    logicResult = logic.viewBusinessReportForAll(paramBean);
                } //技術詳細
                else if (this.rdoTechnicalReport.isSelected()) {
                    paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_BUSINESS_TECHNIC);
                    logicResult = logic.viewBusinessReportForTechnical(paramBean, false);

                } //商品詳細
                else if (this.rdoItemReport.isSelected()) {
                    paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_BUSINESS_GOODS);
                    logicResult = logic.viewBusinessReportForItem(paramBean, false);
                } //コース詳細
                else if (this.rdoCourseReport.isSelected()) {
                    paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_BUSINESS_COURSE);
                    logicResult = logic.viewBusinessReportForCourse(paramBean);
                } //消化一覧
                else if (this.rdoConsumptionCourseReport.isSelected()) {
                    paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_BUSINESS_CONSUMPTION);
                    logicResult = logic.viewBusinessReportForConsumption(paramBean);
                } else if (this.rdoSaleDetailReport.isSelected()) {
                    paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_BUSINESS_CONSUMPTION);
                    int result  = logic.viewBusinessReportForSalesDetail(paramBean);
                    if(result<0){
                        MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    }else if(result ==0) {
                        MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "売上明細"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
                    }
                }
                //IVS_TMTrong start add 2015/10/02 New request #43049
                else if(this.rdoSalesListReport.isSelected()){
                    paramBean.setCourseFlag(this.getShopCorseFlag());
                    Integer result = logic.viewBusinessReportForSalesList(paramBean);
                    if(result==0){
                          MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "売上一覧"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
                    }else if(result==-1){
                        MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    }
                }
                //IVS_TMTrong end add 2015/10/02 New request #43049
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // エラー時
            if (!logicResult) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_btnOutputExcelActionPerformed

	private void cmbTargetPeriodEndFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_cmbTargetPeriodEndFocusGained
	{//GEN-HEADEREND:event_cmbTargetPeriodEndFocusGained
            cmbTargetPeriodEnd.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_cmbTargetPeriodEndFocusGained

	private void cmbTargetPeriodStartFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_cmbTargetPeriodStartFocusGained
	{//GEN-HEADEREND:event_cmbTargetPeriodStartFocusGained
            cmbTargetPeriodStart.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_cmbTargetPeriodStartFocusGained

        private void rdoCourseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCourseReportActionPerformed
            this.initDisplayControl();
        }//GEN-LAST:event_rdoCourseReportActionPerformed

        private void rdoTechnicalReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTechnicalReportActionPerformed
            this.initDisplayControl();
}//GEN-LAST:event_rdoTechnicalReportActionPerformed

        private void rdoConsumptionCourseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoConsumptionCourseReportActionPerformed
            this.initDisplayControl();
        }//GEN-LAST:event_rdoConsumptionCourseReportActionPerformed

    private void targetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetActionPerformed
        // start add 20130802 nakhoa 役務機能使用有無の制御追加
        if (!isLoading) {
            if (target.getSelectedItem() != null) {
                rdoCourseReport.setVisible(false);
                rdoConsumptionCourseReport.setVisible(false);
                if (target.getSelectedItem() instanceof MstShop) {
                    MstShop shop = new MstShop();
                    shop = (MstShop) target.getSelectedItem();
                    //IVS NNTUAN START EDIT 20131015
                    //database still not support yet . Request 13506
                /*
                     if(shop.getCourseFlag() == 1){
                     rdoCourseReport.setVisible(true);
                     rdoConsumptionCourseReport.setVisible(true);
                     }*/

                    rdoCourseReport.setVisible(true);
                    rdoConsumptionCourseReport.setVisible(true);
                    //IVS NNTUAN END EDIT 20131015
                }
               
            }
        }
        // end add 20130802 nakhoa 役務機能使用有無の制御追加
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
                rdoCourseReport.setVisible(false);
                rdoConsumptionCourseReport.setVisible(false); 
                //LVTu start edit 2015/12/01 Bug #44791
//                for (MstShop shop : group.getShops()) {
//                    if (shop.getCourseFlag() == 1) {
//                        rdoCourseReport.setVisible(true);
//                        rdoConsumptionCourseReport.setVisible(true);
//                        //LVTu start edit 2015/10/28 Bug #44067
//                        //return;
//                        //LVTu end edit 2015/10/28 Bug #44067
//                    }
//                }
               //Luc end add 20150703 #39346
                if (checkShopCourseFlag(group)) {
                    rdoCourseReport.setVisible(true);
                    rdoConsumptionCourseReport.setVisible(true); 
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
                rdoCourseReport.setVisible(false);
                rdoConsumptionCourseReport.setVisible(false); 
                if (shop.getCourseFlag() == 1) {
                    rdoCourseReport.setVisible(true);
                    rdoConsumptionCourseReport.setVisible(true);
                    //IVS_TMTrong start edit 20150713 Bug #40218
                    //return;
                    //IVS_TMTrong end edit 20150713 Bug #40218
                }
               
               //Luc end add 20150703 #39346

        }

        this.showItems();
        } 
        /**
     * control design screen follow combo 対象
     * @param isGroup
     * @param useShopCategory 
     */
    public void displayForDesign(boolean isGroup, Integer useShopCategory){
       
        if(isGroup){
            SwingUtil.clearTable(tblReferenceCategoryName);
            SwingUtil.clearTable(tblSelectCategoryName);
            jPanel4.setVisible(true);
                     
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
       
    //IVS_TMTrong start add 2015/10/02 New request #43049
    private Boolean getShopCorseFlag(){
        Boolean courseFlg = false;    
        if (target.getSelectedItem() instanceof MstGroup) {
            //グループ
            MstGroup mg = (MstGroup)target.getSelectedItem();
            //IVS_LVTu start edit 2015/12/03 New request #45059
//            for (MstShop shop : mg.getShops()) {
//                    if (shop.getCourseFlag() == 1) {
//                            courseFlg = true;
//                    }
//            }
            if (checkShopCourseFlag(mg)) {
                            courseFlg = true;
                    }
            //IVS_LVTu end edit 2015/12/03 New request #45059
        } else if(target.getSelectedItem() instanceof MstShop) {
            //店舗
            MstShop ms = (MstShop)target.getSelectedItem();;
            if (ms.getCourseFlag() == 1) {
                    courseFlg = true;
            }
        }
        return courseFlg;
    }
    //IVS_TMTrong end add 2015/10/02 New request #43049
    private void rdoSaleDetailReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSaleDetailReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoSaleDetailReportActionPerformed

    private void tblReferenceCategoryNameMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReferenceCategoryNameMouseMoved

    }//GEN-LAST:event_tblReferenceCategoryNameMouseMoved

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_担当再来分析
        this.moveMutiCategory(true);
        //IVS_vtnhan end add 20140723 MASHU_担当再来分析
    }//GEN-LAST:event_selectButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        //IVS_vtnhan start add 20140723 MASHU_担当再来分析
        this.moveCategoryAll(true);
        //IVS_vtnhan end add 20140723 MASHU_担当再来分析
    }//GEN-LAST:event_selectAllButtonActionPerformed

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
    
    private void tblSelectCategoryNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelectCategoryNameMouseClicked

    }//GEN-LAST:event_tblSelectCategoryNameMouseClicked

    private void tblSelectCategoryNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblSelectCategoryNamePropertyChange

    }//GEN-LAST:event_tblSelectCategoryNamePropertyChange

    private void rdoSalesListReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSalesListReportActionPerformed
         this.initDisplayControl();
    }//GEN-LAST:event_rdoSalesListReportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutputExcel;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEnd;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblNewVisit;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JLabel lblStaffType;
    private javax.swing.JLabel lblTargetPeriod;
    private javax.swing.JLabel lblTax;
    private javax.swing.ButtonGroup newVisitGroup;
    private javax.swing.JRadioButton rdoBusinessReport;
    private javax.swing.JRadioButton rdoConsumptionCourseReport;
    private javax.swing.JRadioButton rdoCourseReport;
    private javax.swing.JRadioButton rdoItemReport;
    private javax.swing.JRadioButton rdoMainStaff;
    private javax.swing.JRadioButton rdoNewVisitAll;
    private javax.swing.JRadioButton rdoNewVisitCurrent;
    private javax.swing.JRadioButton rdoSaleDetailReport;
    private javax.swing.JRadioButton rdoSalesListReport;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private javax.swing.JRadioButton rdoTechStaff;
    private javax.swing.JRadioButton rdoTechnicalReport;
    private javax.swing.JScrollPane referenceCategoryLeftScrollPane;
    private javax.swing.JButton releaseAllButton;
    private javax.swing.JButton releaseButton;
    private javax.swing.ButtonGroup reportTypeGroup;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JScrollPane selectCategoryRightScrollPane;
    private javax.swing.JComboBox staff;
    private javax.swing.ButtonGroup staffGroup;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel target;
    private javax.swing.JLabel targetLabel;
    private javax.swing.ButtonGroup taxGroup;
    private javax.swing.JTable tblReferenceCategoryName;
    private javax.swing.JTable tblSelectCategoryName;
    // End of variables declaration//GEN-END:variables
    private BusinessReportFocusTraversalPolicy ftp =
            new BusinessReportFocusTraversalPolicy();

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnOutputExcel);
    }

    private void setKeyListener() {
        cmbTargetPeriodEnd.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodEnd.addFocusListener(SystemInfo.getSelectText());
        cmbTargetPeriodStart.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodStart.addFocusListener(SystemInfo.getSelectText());
        rdoBusinessReport.addKeyListener(SystemInfo.getMoveNextField());
        rdoItemReport.addKeyListener(SystemInfo.getMoveNextField());
        rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
        rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
        rdoTechnicalReport.addKeyListener(SystemInfo.getMoveNextField());
        staff.addKeyListener(SystemInfo.getMoveNextField());
    }

    /**
     * レジ担当者を初期化する。
     */
    private void initStaff() {
        staff.addItem(new MstStaff());
        SystemInfo.initStaffComponent(staff);
        staff.setSelectedIndex(0);
    }

    /**
     * 業務報告画面用FocusTraversalPolicyを取得する。
     *
     * @return 業務報告画面用FocusTraversalPolicy
     */
    public BusinessReportFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    /**
     * 業務報告画面用FocusTraversalPolicy
     */
    private class BusinessReportFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         * aComponBusinessReportFocusTraversalPolicy。 aContainer は aComponent
         * のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         *
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            if (aComponent.equals(rdoBusinessReport)
                    || aComponent.equals(rdoTechnicalReport)
                    || aComponent.equals(rdoItemReport)
                    || aComponent.equals(rdoCourseReport)
                    || aComponent.equals(rdoConsumptionCourseReport)) {
                if (target.getItemCount() == 1) {
                    return cmbTargetPeriodStart;
                } else {
                    return target;
                }
            } else if (aComponent.equals(target)) {
                return cmbTargetPeriodStart;
            } else if (aComponent.equals(cmbTargetPeriodStart)) {
                return cmbTargetPeriodEnd;
            } else if (aComponent.equals(cmbTargetPeriodEnd)) {
                if (rdoMainStaff.isSelected()) {
                    return rdoMainStaff;
                } else {
                    return rdoTechStaff;
                }
            } else if (aComponent.equals(rdoMainStaff)
                    || aComponent.equals(rdoTechStaff)) {
                return staff;
            } else if (aComponent.equals(staff)) {
                if (rdoNewVisitCurrent.isEnabled() && rdoNewVisitCurrent.isSelected()) {
                    return rdoNewVisitCurrent;
                } else if (rdoNewVisitAll.isEnabled() && rdoNewVisitAll.isSelected()) {
                    return rdoNewVisitAll;
                } else {
                    return this.getSelectedTaxType();
                }
            } else if (aComponent.equals(rdoNewVisitCurrent)
                    || aComponent.equals(rdoNewVisitAll)) {
                return this.getSelectedTaxType();
            } else if (aComponent.equals(rdoTaxUnit)
                    || aComponent.equals(rdoTaxBlank)) {
                return this.getSelectedReportType();
            }
            return this.getSelectedReportType();
        }

        /**
         * aComponent の前にフォーカスを受け取る Component を返します。 aContainer は aComponent
         * のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         *
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            if (aComponent.equals(rdoBusinessReport)
                    || aComponent.equals(rdoTechnicalReport)
                    || aComponent.equals(rdoItemReport)
                    || aComponent.equals(rdoCourseReport)
                    || aComponent.equals(rdoConsumptionCourseReport)) {
                return this.getSelectedReportType();
            } else if (aComponent.equals(target)) {
                return this.getSelectedReportType();
            } else if (aComponent.equals(cmbTargetPeriodStart)) {
                if (target.getItemCount() > 1) {
                    return this.getSelectedTaxType();
                } else {
                    return target;
                }
            } else if (aComponent.equals(cmbTargetPeriodEnd)) {
                return cmbTargetPeriodStart;
            } else if (aComponent.equals(rdoMainStaff)
                    || aComponent.equals(rdoTechStaff)) {
                return cmbTargetPeriodEnd;
            } else if (aComponent.equals(staff)) {
                if (rdoMainStaff.isSelected()) {
                    return rdoMainStaff;
                }
                if (rdoTechStaff.isSelected()) {
                    return rdoTechStaff;
                }
            } else if (aComponent.equals(rdoNewVisitCurrent)
                    || aComponent.equals(rdoNewVisitAll)) {
                return staff;
            } else if (aComponent.equals(rdoTaxUnit)
                    || aComponent.equals(rdoTaxBlank)) {
                if (rdoNewVisitCurrent.isSelected()) {
                    return rdoNewVisitCurrent;
                }
                if (rdoNewVisitAll.isSelected()) {
                    return rdoNewVisitAll;
                }
            }
            return this.getSelectedReportType();
        }

        /**
         * トラバーサルサイクルの最初の Component を返します。 このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする
         * Component を判定するために使用します。
         *
         * @param aContainer 先頭の Component
         * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component
         * が見つからない場合は null
         */
        public Component getFirstComponent(Container aContainer) {
            return this.getSelectedReportType();
        }

        /**
         * トラバーサルサイクルの最後の Component を返します。 このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする
         * Component を判定するために使用します。
         *
         * @param aContainer aContainer - 最後の Component
         * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component
         * が見つからない場合は null
         */
        public Component getLastComponent(Container aContainer) {
            return cmbTargetPeriodEnd;
        }

        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。 aContainer
         * をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
         *
         * @param aContainer デフォルトの Component
         * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component
         * が見つからない場合は null
         */
        public Component getDefaultComponent(Container aContainer) {
            return this.getSelectedReportType();
        }

        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。 show() または setVisible(true)
         * の呼び出しで一度ウィンドウが表示されると、 初期化コンポーネントはそれ以降使用されません。
         * 一度別のウィンドウに移ったフォーカスが再び設定された場合、 または、一度非表示状態になったウィンドウが再び表示された場合は、
         * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
         * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
         *
         * @param window 初期コンポーネントが返されるウィンドウ
         * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
         */
        public Component getInitialComponent(Window window) {
            return this.getSelectedReportType();
        }

        private Component getSelectedReportType() {
            if (rdoTechnicalReport.isSelected()) {
                return rdoTechnicalReport;
            } else if (rdoItemReport.isSelected()) {
                return rdoItemReport;
            } else if (rdoCourseReport.isSelected()) {
                return rdoCourseReport;
            } else if (rdoConsumptionCourseReport.isSelected()) {
                return rdoConsumptionCourseReport;
            }
            return rdoBusinessReport;
        }

        private Component getSelectedTaxType() {
            if (rdoTaxUnit.isSelected()) {
                return rdoTaxUnit;
            }
            return rdoTaxBlank;
        }
    }
}

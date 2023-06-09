/*
 * AreaAndAttributeCustomerAnalytic.java
 *
 * Created on 2012/10/23, 13:00
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import com.geobeck.sosia.pos.hair.report.logic.AnalyticChartReportLogic;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.util.Calendar;
import java.util.logging.*;
import javax.swing.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.master.prefecture.*;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Luc
 */
public final class AreaAndAttributeCustomerAnalytic extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
     //nhanvt start add 20141225 New request #32798
     private MstShopCategorys mrsRef = new MstShopCategorys();
     private MstShopCategorys mrsUse = new MstShopCategorys();
     private MstShopCategorys		mscg		=	new MstShopCategorys();
     private int useShopCategory = 0;
     private boolean isHideCategory = false;
     //nhanvt end add 20141225 New request #32798
    /** Luc Add 20121023
     * Creates new form AreaAndAttributeCustomerAnalytic
     * 
     */
    public AreaAndAttributeCustomerAnalytic() {
        
        initComponents();
        this.setSize(958, 652);

        this.setPath("�ڋq��������");
        this.setTitle("�ڋq��������");
        
        SystemInfo.initGroupShopComponents(cmbTarget1, 3);    
        initPrefectureCombobox();
        rdoattributeAnalysis.setSelected(true);
        cmbPrefecture.setEnabled(false);
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
        countGroup = new javax.swing.ButtonGroup();
        customerGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        lblTarget1 = new javax.swing.JLabel();
        cmbTarget1 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblPeriod1 = new javax.swing.JLabel();
        cmbPeriodStart1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblSpe1 = new javax.swing.JLabel();
        cmbPeriodEnd1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblPrefecture = new javax.swing.JLabel();
        cmbPrefecture = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnExcelReport1 = new javax.swing.JButton();
        rdoattributeAnalysis = new javax.swing.JRadioButton();
        rdoAreaAnalysis = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        referenceCategoryLeftScrollPane = new javax.swing.JScrollPane();
        tblReferenceCategoryName = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        releaseButton = new javax.swing.JButton();
        releaseAllButton = new javax.swing.JButton();
        selectCategoryRightScrollPane = new javax.swing.JScrollPane();
        tblSelectCategoryName = new javax.swing.JTable();
        lblCategory = new javax.swing.JLabel();

        setFocusCycleRoot(true);
        setPreferredSize(new java.awt.Dimension(958, 652));

        lblTarget1.setText("�Ώ�");

        //shop.addItem(this.myShop);
        cmbTarget1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTarget1ActionPerformed(evt);
            }
        });

        lblPeriod1.setText("�Ώۊ���");
        lblPeriod1.setFocusCycleRoot(true);

        cmbPeriodStart1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPeriodStart1.setDate(new java.util.Date());

        lblSpe1.setText("�`");
        lblSpe1.setFocusCycleRoot(true);

        cmbPeriodEnd1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPeriodEnd1.setDate(new java.util.Date());

        lblPrefecture.setText("�s���{��");
        lblPrefecture.setFocusCycleRoot(true);

        //shop.addItem(this.myShop);

        btnExcelReport1.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcelReport1.setAlignmentX(0.5F);
        btnExcelReport1.setBorderPainted(false);
        btnExcelReport1.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcelReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelReportAreaActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoattributeAnalysis);
        rdoattributeAnalysis.setText("��������");
        rdoattributeAnalysis.setOpaque(false);
        rdoattributeAnalysis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoattributeAnalysisMouseClicked(evt);
            }
        });

        buttonGroup1.add(rdoAreaAnalysis);
        rdoAreaAnalysis.setText("�G���A����");
        rdoAreaAnalysis.setOpaque(false);
        rdoAreaAnalysis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoAreaAnalysisMouseClicked(evt);
            }
        });

        jPanel3.setOpaque(false);

        tblReferenceCategoryName.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "�ƑԖ�"
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

        jPanel4.setOpaque(false);

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

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
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
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "�ƑԖ�"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

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

        lblCategory.setText("�W�v�Ƒ�");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(lblCategory)
                .add(18, 18, 18)
                .add(referenceCategoryLeftScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectCategoryRightScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblCategory)
                    .add(selectCategoryRightScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(referenceCategoryLeftScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 268, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(rdoattributeAnalysis)
                                .add(4, 4, 4)
                                .add(rdoAreaAnalysis))
                            .add(layout.createSequentialGroup()
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                            .add(1, 1, 1)
                                            .add(lblPeriod1))
                                    .add(lblTarget1))
                                    .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(cmbTarget1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(layout.createSequentialGroup()
                                            .add(cmbPeriodStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(3, 3, 3)
                                            .add(lblSpe1)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(cmbPeriodEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                            .add(layout.createSequentialGroup()
                                .add(lblPrefecture)
                                .add(18, 18, 18)
                                .add(cmbPrefecture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .add(layout.createSequentialGroup()
                .add(176, 176, 176)
                .add(btnExcelReport1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoattributeAnalysis)
                    .add(rdoAreaAnalysis))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTarget1)
                    .add(cmbTarget1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblSpe1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbPeriodStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbPeriodEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPeriod1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbPrefecture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPrefecture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3)
                .add(btnExcelReport1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(319, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnExcelReportAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelReportAreaActionPerformed
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(this.cmbPeriodStart1.getDate());
                end.setTime(this.cmbPeriodEnd1.getDate());
                if (start.compareTo(end) != 0) 
                {
                    if (start.after(end)) 
                    {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
        AnalyticChartReportLogic logic = new AnalyticChartReportLogic();
        ReportParameterBean paramBean = new ReportParameterBean();
        paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_SALES);
        
       try {
            boolean logicResult = false;

            if (cmbTarget1.getSelectedItem() instanceof MstGroup) {

            //�O���[�v
            MstGroup mg = (MstGroup) cmbTarget1.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());

        } else if (cmbTarget1.getSelectedItem() instanceof MstShop) {
            
            //�X��
            MstShop ms = (MstShop) cmbTarget1.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }
         
        //nhanvt start add
        //�ΏۂƂȂ�X�܂����݂��Ȃ��ꍇ
	if(paramBean.getShopIDList().equals("")) {
		MessageDialog.showMessageDialog(this,
			MessageUtil.getMessage(4001),
			this.getTitle(),
			JOptionPane.ERROR_MESSAGE);
		return;
	}    
        //nhanvt end add
        String prefecture="";
        if(this.cmbPrefecture.getSelectedItem()!=null)
        {
            prefecture=this.cmbPrefecture.getSelectedItem().toString();
        }
        paramBean.setTargetStartDate(this.cmbPeriodStart1.getDateStr());
        paramBean.setTargetEndDate(this.cmbPeriodEnd1.getDateStr());
        paramBean.setTargetStartDateObj(this.cmbPeriodStart1.getDate());
        paramBean.setTargetEndDateObj(this.cmbPeriodEnd1.getDate());    
        
        //nhanvt start
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
                        lstNameShopCategory += "�A";
                    }
                }
            }
        }
        if(!lstShopCategoryId.equals("")){
            paramBean.setListCategoryId(lstShopCategoryId);
        }else{
            paramBean.setListCategoryId(null);
        }

        paramBean.setListCategoryName(lstNameShopCategory);
        paramBean.setIsHideCategory(isHideCategory);
        paramBean.setUseShopCategory(useShopCategory);
        //nhanvt end
        
        if (rdoAreaAnalysis.isSelected()) {
             logicResult=logic.viewSalesReportByAreaAnalyticChart(paramBean,prefecture);   
        }
        else
        {
            logicResult = logic.viewSalesReportByCustomerAttributeAnalyticChart(paramBean,"");
        }
       
         if (!logicResult) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }  
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }//GEN-LAST:event_btnExcelReportAreaActionPerformed

    private void rdoattributeAnalysisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoattributeAnalysisMouseClicked
        if (rdoattributeAnalysis.isSelected()) {
            cmbPrefecture.setEnabled(false);
        }
        
    }//GEN-LAST:event_rdoattributeAnalysisMouseClicked

    private void rdoAreaAnalysisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoAreaAnalysisMouseClicked
        if (rdoAreaAnalysis.isSelected()) {
            cmbPrefecture.setEnabled(true);
        }
    }//GEN-LAST:event_rdoAreaAnalysisMouseClicked

    private void tblReferenceCategoryNameMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReferenceCategoryNameMouseMoved

    }//GEN-LAST:event_tblReferenceCategoryNameMouseMoved

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        this.moveMutiCategory(true);
    }//GEN-LAST:event_selectButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        this.moveCategoryAll(true);
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void releaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseButtonActionPerformed
        this.moveMutiCategory(false);
    }//GEN-LAST:event_releaseButtonActionPerformed

    private void releaseAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseAllButtonActionPerformed
      this.moveCategoryAll(false);
    }//GEN-LAST:event_releaseAllButtonActionPerformed

    private void tblSelectCategoryNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelectCategoryNameMouseClicked

    }//GEN-LAST:event_tblSelectCategoryNameMouseClicked

    private void tblSelectCategoryNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblSelectCategoryNamePropertyChange

    }//GEN-LAST:event_tblSelectCategoryNamePropertyChange

    private void cmbTarget1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTarget1ActionPerformed
        this.chargeDataCombo();
    }//GEN-LAST:event_cmbTarget1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcelReport1;
    private javax.swing.ButtonGroup buttonGroup1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbPeriodEnd1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbPeriodStart1;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbPrefecture;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbTarget1;
    private javax.swing.ButtonGroup countGroup;
    private javax.swing.ButtonGroup customerGroup;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblPeriod1;
    private javax.swing.JLabel lblPrefecture;
    private javax.swing.JLabel lblSpe1;
    private javax.swing.JLabel lblTarget1;
    private javax.swing.JRadioButton rdoAreaAnalysis;
    private javax.swing.JRadioButton rdoattributeAnalysis;
    private javax.swing.JScrollPane referenceCategoryLeftScrollPane;
    private javax.swing.JButton releaseAllButton;
    private javax.swing.JButton releaseButton;
    private javax.swing.ButtonGroup reportTypeGroup;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JScrollPane selectCategoryRightScrollPane;
    private javax.swing.ButtonGroup taxGroup;
    private javax.swing.JTable tblReferenceCategoryName;
    private javax.swing.JTable tblSelectCategoryName;
    // End of variables declaration//GEN-END:variables
    
    /**
     * �s���{���R���{�{�b�N�X
     */
     public  void initPrefectureCombobox()
    {
           try
            {
		ConnectionWrapper con =SystemInfo.getBaseConnection();;
                MstPrefecture ms = new MstPrefecture();
                String sql = ms.getSelectSQL();
                ResultSetWrapper rs = con.executeQuery(sql);
                while(rs.next())
                {
                    ms.setData(rs);
                    cmbPrefecture.addItem(ms.getPrefectName());
                }
            }
            catch(Exception e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage());
            }
       
    }
     
     //nhanvt start add 20141225 New request #32798
     /**
        * init data for table �ƑԖ� left with group shop
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
        * init data for table �ƑԖ� left with muti shop
        */
       private void initCategoryWithMutiShop() {
            mrsRef = new MstShopCategorys();
            MstShop ms = (MstShop)cmbTarget1.getSelectedItem();
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

       /**
        * change combo and show again two table
        * change combo
        * @param evt 
        */
        private void chargeDataCombo() {   

            mrsRef = new MstShopCategorys();
            mrsUse = new MstShopCategorys();
            //�O���[�v
            if(cmbTarget1.getSelectedItem() instanceof MstGroup) {
                     displayForDesign(true, 1);
                      useShopCategory = 1;
                     isHideCategory = true;
                     initCategoryWithGroupShop();

            }
            //�X��
            else if(cmbTarget1.getSelectedItem() instanceof MstShop) {
                 MstShop ms = (MstShop)cmbTarget1.getSelectedItem();
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

            }

            this.showItems();
       } 

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

        Collections.sort(list, new AreaAndAttributeCustomerAnalytic.ItemComparator());

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
     * control design screen follow combo �Ώ�
     * @param isGroup
     * @param useShopCategory 
     */
    public void displayForDesign(boolean isGroup, Integer useShopCategory){
       
        if(isGroup){
            SwingUtil.clearTable(tblReferenceCategoryName);
            SwingUtil.clearTable(tblSelectCategoryName);
            lblCategory.setVisible(true);
            referenceCategoryLeftScrollPane.setVisible(true);
            tblReferenceCategoryName.setVisible(true);
            selectCategoryRightScrollPane.setVisible(true);
            tblSelectCategoryName.setVisible(true);
            selectAllButton.setVisible(true);
            selectButton.setVisible(true);
            releaseButton.setVisible(true);
            releaseAllButton.setVisible(true);
            jPanel3.setVisible(true);
            //IVS_LVTu start edit 20150302 New request #35208
            if(SystemInfo.getSetteing().isUseShopCategory()) {
                jPanel3.setVisible(true);
            }else {
                jPanel3.setVisible(false);
            }
            //IVS_LVTu end edit 20150302 New request #35208    
                     
        }else{
            MstShop ms = (MstShop)cmbTarget1.getSelectedItem();
            if(ms.getUseShopCategory().equals(1)){
                   SwingUtil.clearTable(tblReferenceCategoryName);
                   SwingUtil.clearTable(tblSelectCategoryName);
                   lblCategory.setVisible(true);
                   referenceCategoryLeftScrollPane.setVisible(true);
                   tblReferenceCategoryName.setVisible(true);
                   selectCategoryRightScrollPane.setVisible(true);
                   tblSelectCategoryName.setVisible(true);
                   selectAllButton.setVisible(true);
                   selectButton.setVisible(true);
                   releaseButton.setVisible(true);
                   releaseAllButton.setVisible(true);
                   jPanel3.setVisible(true);

            }else{

                   SwingUtil.clearTable(tblReferenceCategoryName);
                   SwingUtil.clearTable(tblSelectCategoryName);
                   lblCategory.setVisible(false);
                   referenceCategoryLeftScrollPane.setVisible(false);
                   tblReferenceCategoryName.setVisible(false);
                   selectCategoryRightScrollPane.setVisible(false);
                   tblSelectCategoryName.setVisible(false);
                   selectAllButton.setVisible(false);
                   selectButton.setVisible(false);
                   releaseButton.setVisible(false);
                   releaseAllButton.setVisible(false);
                   jPanel3.setVisible(false);

            }
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
     //nhanvt start add 20141225 New request #32798
}

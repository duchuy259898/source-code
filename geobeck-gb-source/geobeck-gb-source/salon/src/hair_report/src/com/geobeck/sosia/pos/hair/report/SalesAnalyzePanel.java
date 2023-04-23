/* SalesAnalyzePanel.java
 * Created on 2008/10/99, 90:99
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.hair.report.beans.SalesAnalyzeBean;
import com.geobeck.sosia.pos.hair.report.beans.SalesDateBean;
import com.geobeck.sosia.pos.hair.report.logic.SalesAnalyzeLogic;
import com.geobeck.sosia.pos.hair.report.logic.TechnicSalesReportLogic;
import com.geobeck.sosia.pos.report.util.ReportManager;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;
import org.apache.commons.lang.CharRange;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.util.*;


import java.awt.Font;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.SortOrder;

/** �N�Ԉړ��O���t
 * @author torino
 */
public class SalesAnalyzePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    /** �t�H�[���̏������i���������j */
// <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        analysisTypeGroup = new javax.swing.ButtonGroup();
        rangeGroup = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rdoTechnicSales = new javax.swing.JRadioButton();
        rdoCustomerCount = new javax.swing.JRadioButton();
        rdoSalesPerCustomer = new javax.swing.JRadioButton();
        lblTargetPeriod = new javax.swing.JLabel();
        cmbYear = new javax.swing.JComboBox();
        lblRange = new javax.swing.JLabel();
        rdoAutoRange = new javax.swing.JRadioButton();
        rdoManualRange = new javax.swing.JRadioButton();
        lblMax = new javax.swing.JLabel();
        txtMax = new JFormattedTextField(new DecimalFormat("#,###"));
        lblMin = new javax.swing.JLabel();
        txtMin = new JFormattedTextField(new DecimalFormat("#,###"));
        btnDisplay = new javax.swing.JButton();
        lblShop = new javax.swing.JLabel();
        cmbShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnPdf = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        splitpaneSelectedYear = new javax.swing.JSplitPane();
        lblSelectedYear = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSelectedYear = new javax.swing.JTable();
        splitpanePreviusYear1 = new javax.swing.JSplitPane();
        lblPreviusYear = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPreviusYear = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        
        
        setMaximumSize(new java.awt.Dimension(834, 691));
        setMinimumSize(new java.awt.Dimension(834, 691));
        lblTitle.setText("\u5e74\u9593\u79fb\u52d5\u30b0\u30e9\u30d5");
        
        jPanel1.setFocusCycleRoot(true);
        jPanel1.setOpaque(false);
        analysisTypeGroup.add(rdoTechnicSales);
        rdoTechnicSales.setSelected(true);
        rdoTechnicSales.setText("\u6280\u8853\u58f2\u4e0a");
        rdoTechnicSales.setBorder(null);
        rdoTechnicSales.setContentAreaFilled(false);
        rdoTechnicSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTechnicSalesActionPerformed(evt);
            }
        });
        
        
        analysisTypeGroup.add(rdoCustomerCount);
        rdoCustomerCount.setText("\u5165\u5ba2\u6570");
        rdoCustomerCount.setBorder(null);
        rdoCustomerCount.setContentAreaFilled(false);
        rdoCustomerCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCustomerCountActionPerformed(evt);
            }
        });
        
        
        analysisTypeGroup.add(rdoSalesPerCustomer);
        rdoSalesPerCustomer.setText("\u5ba2\u5358\u4fa1");
        rdoSalesPerCustomer.setBorder(null);
        rdoSalesPerCustomer.setContentAreaFilled(false);
        rdoSalesPerCustomer.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSalesPerCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSalesPerCustomerActionPerformed(evt);
            }
        });
        
        
        lblTargetPeriod.setText("\u96c6\u8a08\u671f\u9593");
        
        cmbYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbYearActionPerformed(evt);
            }
        });
        
        
        lblRange.setText("\u8868\u793a\u7bc4\u56f2");
        
        rangeGroup.add(rdoAutoRange);
        rdoAutoRange.setSelected(true);
        rdoAutoRange.setText("\u81ea\u52d5\u8abf\u6574");
        rdoAutoRange.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoAutoRange.setContentAreaFilled(false);
        rdoAutoRange.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoAutoRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAutoRangeActionPerformed(evt);
            }
        });
        
        
        rangeGroup.add(rdoManualRange);
        rdoManualRange.setText("\u624b\u5165\u529b");
        rdoManualRange.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoManualRange.setContentAreaFilled(false);
        rdoManualRange.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoManualRange.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoManualRangeStateChanged(evt);
            }
        });
        
        
        lblMax.setText("\u6700\u5927\u5024");
        
        txtMax.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtMax.setEnabled(false);
        txtMax.setVerifyInputWhenFocusTarget(false);
        
        lblMin.setText("\u6700\u5c0f\u5024");
        
        txtMin.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtMin.setEnabled(false);
        
        btnDisplay.setIcon(SystemInfo.getImageIcon( "/button/common/show_off.jpg" ));
        btnDisplay.setBorder(null);
        btnDisplay.setBorderPainted(false);
        btnDisplay.setIconTextGap(0);
        btnDisplay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnDisplay.setOpaque(false);
        btnDisplay.setPressedIcon(SystemInfo.getImageIcon( "/button/common/show_on.jpg" ));
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        
        
        lblShop.setText("\u5e97\u8217\u540d");
        
        cmbShop.setBorder(null);
        cmbShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShopActionPerformed(evt);
            }
        });
        
        
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                .add(rdoTechnicSales)
                .add(17, 17, 17)
                .add(rdoCustomerCount)
                .add(89, 89, 89))
                .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(lblShop)
                .add(lblTargetPeriod)
                .add(lblMax)
                .add(lblMin)
                .add(lblRange))
                .add(15, 15, 15)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(jPanel1Layout.createSequentialGroup()
                .add(txtMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(txtMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cmbYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cmbShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(133, 133, 133)))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(rdoSalesPerCustomer)
                .add(jPanel1Layout.createSequentialGroup()
                .add(rdoAutoRange)
                .add(30, 30, 30)
                .add(rdoManualRange)))))
                );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(rdoTechnicSales)
                .add(rdoCustomerCount)
                .add(rdoSalesPerCustomer))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, lblShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, cmbShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTargetPeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, cmbYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(lblRange)
                .add(rdoAutoRange)
                .add(rdoManualRange))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, lblMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, txtMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, lblMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, txtMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(btnDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
        
        btnPdf.setIcon(SystemInfo.getImageIcon("/button/print/output_pdf_off.jpg"));
        btnPdf.setBorderPainted(false);
        btnPdf.setEnabled(false);
        btnPdf.setOpaque(false);
        btnPdf.setPressedIcon(SystemInfo.getImageIcon("/button/print/output_pdf_on.jpg"));
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });
        
        
        btnExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcel.setBorderPainted(false);
        btnExcel.setEnabled(false);
        btnExcel.setOpaque(false);
        btnExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        
        
        splitpaneSelectedYear.setBackground(new java.awt.Color(255, 255, 255));
        splitpaneSelectedYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        splitpaneSelectedYear.setDividerLocation(16);
        splitpaneSelectedYear.setDividerSize(0);
        splitpaneSelectedYear.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitpaneSelectedYear.setEnabled(false);
        splitpaneSelectedYear.setPreferredSize(new java.awt.Dimension(810, 112));
        lblSelectedYear.setBackground(new java.awt.Color(204, 204, 204));
        lblSelectedYear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedYear.setText("\u5e74\u5ea6");
        lblSelectedYear.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(254, 254, 254), new java.awt.Color(154, 154, 154), new java.awt.Color(220, 220, 220)));
        lblSelectedYear.setMaximumSize(new java.awt.Dimension(51, 16));
        lblSelectedYear.setMinimumSize(new java.awt.Dimension(51, 16));
        lblSelectedYear.setOpaque(true);
        lblSelectedYear.setPreferredSize(new java.awt.Dimension(53, 16));
        splitpaneSelectedYear.setLeftComponent(lblSelectedYear);
        
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        tblSelectedYear.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {"����", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"�P�Q�������v", null, null, null, null, null, null, null, null, null, null, null, null},
                    {"�ړ��l", null, null, null, null, null, null, null, null, null, null, null, null}
        },
                new String [] {
            "", "�P��", "�Q��", "�R��", "�S��", "�T��", "�U��", "�V��", "�W��", "�X��", "�P�O��", "�P�P��", "�P�Q��"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSelectedYear.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblSelectedYear.setAutoscrolls(false);
        tblSelectedYear.setEnabled(false);
        tblSelectedYear.setFocusable(false);
        tblSelectedYear.setMaximumSize(new java.awt.Dimension(2147483647, 94));
        tblSelectedYear.setMinimumSize(new java.awt.Dimension(195, 94));
        tblSelectedYear.setName("\u524d\u5e74\u5ea6");
        tblSelectedYear.setPreferredSize(new java.awt.Dimension(0, 78));
        tblSelectedYear.setRowSelectionAllowed(false);
        tblSelectedYear.setUpdateSelectionOnSort(false);
        tblSelectedYear.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(tblSelectedYear);
        
        splitpaneSelectedYear.setRightComponent(jScrollPane1);
        
        
        splitpanePreviusYear1.setBackground(new java.awt.Color(255, 255, 255));
        splitpanePreviusYear1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        splitpanePreviusYear1.setDividerLocation(16);
        splitpanePreviusYear1.setDividerSize(0);
        splitpanePreviusYear1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitpanePreviusYear1.setEnabled(false);
        splitpanePreviusYear1.setPreferredSize(new java.awt.Dimension(100, 119));
        splitpanePreviusYear1.setRequestFocusEnabled(false);
        lblPreviusYear.setBackground(new java.awt.Color(204, 204, 204));
        lblPreviusYear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreviusYear.setText("\u5e74\u5ea6");
        lblPreviusYear.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(254, 254, 254), new java.awt.Color(154, 154, 154), new java.awt.Color(220, 220, 220)));
        lblPreviusYear.setMaximumSize(new java.awt.Dimension(51, 16));
        lblPreviusYear.setMinimumSize(new java.awt.Dimension(51, 16));
        lblPreviusYear.setOpaque(true);
        lblPreviusYear.setPreferredSize(new java.awt.Dimension(51, 16));
        lblPreviusYear.setRequestFocusEnabled(false);
        splitpanePreviusYear1.setTopComponent(lblPreviusYear);
        
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        tblPreviusYear.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {"�O�N�x����", "", "", "", "", "", "", "", "", "", "", "", ""}
        },
                new String [] {
            "", "�P��", "�Q��", "�R��", "�S��", "�T��", "�U��", "�V��", "�W��", "�X��", "�P�O��", "�P�P��", "�P�Q��"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPreviusYear.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblPreviusYear.setAutoscrolls(false);
        tblPreviusYear.setEnabled(false);
        tblPreviusYear.setFocusable(false);
        tblPreviusYear.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        tblPreviusYear.setMinimumSize(new java.awt.Dimension(195, 26));
        tblPreviusYear.setName("\u524d\u5e74\u5ea6");
        tblPreviusYear.setPreferredSize(new java.awt.Dimension(0, 26));
        tblPreviusYear.setVerifyInputWhenFocusTarget(false);
        jScrollPane2.setViewportView(tblPreviusYear);
        
        splitpanePreviusYear1.setRightComponent(jScrollPane2);
        
        
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 810, Short.MAX_VALUE)
                );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 289, Short.MAX_VALUE)
                );
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(lblTitle)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 328, Short.MAX_VALUE)
                .add(btnPdf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnExcel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(org.jdesktop.layout.GroupLayout.LEADING, splitpaneSelectedYear, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.LEADING, splitpanePreviusYear1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE))
                .addContainerGap())
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(btnExcel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnPdf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblTitle)
                .add(16, 16, 16)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(splitpaneSelectedYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(splitpanePreviusYear1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
                );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbYearActionPerformed
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
    }//GEN-LAST:event_cmbYearActionPerformed

    private void rdoSalesPerCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSalesPerCustomerActionPerformed
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
    }//GEN-LAST:event_rdoSalesPerCustomerActionPerformed

    private void rdoCustomerCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCustomerCountActionPerformed
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
    }//GEN-LAST:event_rdoCustomerCountActionPerformed
    
    /** Excel�o�̓{�^�� */
    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        if(valueAxis == null){
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
            return;
        }
        //ExportFile(SalesAnalyzeLogic.EXPORT_FILE_XLS);
        ExportXLSFile();
        btnExcel.setEnabled(false);
    }//GEN-LAST:event_btnExcelActionPerformed
    
    /** ����̓{�^���`�F�b�N�Ɠ��͗��̘A�� */
    private void rdoManualRangeStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoManualRangeStateChanged
        boolean isSelected = (evt.getStateChange() == ItemEvent.SELECTED);
        txtMax.setEnabled(isSelected);
        txtMin.setEnabled(isSelected);
    }//GEN-LAST:event_rdoManualRangeStateChanged
    
    /** �X�܃R���{�{�b�N�X */
    private void cmbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopActionPerformed
        int shopIdOld = shopId;
        getShopId();
        if(shopIdOld != shopId){
            initCmbYear();      // �W�v���ԃR���{�{�b�N�X��ݒ�
            
        }
        ClearGraphAndTable();   // �O���t�ƕ\���N���A����
    }//GEN-LAST:event_cmbShopActionPerformed
    
    /** �\���{�^�� */
    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        
        // <editor-fold desc="�R���{�{�b�N�X�ŔN���I������Ă��Ȃ��ꍇ">
        if(cmbYear.getSelectedIndex() == -1){
            // �W�v���Ԃ�I�����Ă��������B
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, lblTargetPeriod.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // </editor-fold>
        
        // <editor-fold desc="���͎�ʂ����W�I�{�^���̏�Ԃ���擾����">
        if(rdoTechnicSales.isSelected()){
            type = AnalysisTypes.TechnicSales;
        } else if (rdoCustomerCount.isSelected()){
            type = AnalysisTypes.CustomerCount;
        } else if(rdoSalesPerCustomer.isSelected()){
            type = AnalysisTypes.SalesPerCustomer;
        } else{
            type = null;
            return;
        }
        // </editor-fold>
        
        // ���͎�ʂ�\�ɔ��f������
        tblSelectedYear.getTableHeader().getColumnModel().getColumn(0).setHeaderValue(type);
        tblPreviusYear.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("�O�N�x" + type.toString());
        
        paramMap.put("ANALYZE_TYPE", type.toString());
        
        /** �R���{�{�b�N�X�őI������Ă����N�N���X */
        YearItem yearSelected = (YearItem) cmbYear.getSelectedItem();
        lblSelectedYear.setText(ToZenkaku(yearSelected.getString()) + "�N�x");
        lblPreviusYear.setText(ToZenkaku((yearSelected.getValue() - 1) + "�N�x"));
        
        int year = ((YearItem)cmbYear.getSelectedItem()).numYear;
        TechnicAndInBeans[] aSelectedYear = new TechnicAndInBeans[13];
        TechnicAndInBeans[] aPreviusYear = new TechnicAndInBeans[13];
        if((!getAnnualData(this.shopId, year, aSelectedYear)) &
                (!getAnnualData(this.shopId, year - 1, aPreviusYear))){
            // �o�͑Ώۃf�[�^������܂���B
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            ClearGraphAndTable(); // �O���t�ƕ\���N���A����
            return;
        }
        
        /** �O�N�x�����`�P�Q���̍��v(Long) */
        Long[] aLong = new Long[14];
        aLong[13] = 0L;
        /** �O�N�x�����`�P�Q���̍��v(Float) */
        Double[] aDouble = new Double[14];
        aDouble[13] = 0D;
        
        switch(type) {
            // <editor-fold desc="�Z�p����܂��͓��q���̏ꍇ">
            case TechnicSales:
            case CustomerCount:
                /** ���Z�� */
                LongAdder longSum = new LongAdder(0L);
                // ���N�̃f�[�^�̏���
                for(int i = 12; i > 0; i--){
                    TechnicAndInBeans data = aPreviusYear[i];
                    /** �Z�p����܂��͓��q�� */
                    Integer val = null;
                    if(data != null){
                        val = (type == AnalysisTypes.TechnicSales) ? data.getTechnic() : data.getIn();
                        beans[i - 1] = new SalesAnalyzeBean(ToZenkaku(i + "��"), val.doubleValue());
                    }else{
                        beans[i - 1] = new SalesAnalyzeBean(ToZenkaku(i + "��"));
                    }
                    if(val != null){
                        tblPreviusYear.setValueAt(nf.format(val), 0, i);
                        longSum.add(val.longValue());
                    } else{
                        tblPreviusYear.setValueAt(EMPTY_STRING, 0, i);
                        longSum.nullify();
                    }
                    aLong[i] = longSum.value;
                }
                // ���N�̃f�[�^�̏���
                longSum.zeroize(); //   �ȍ~�A�Y���N�x�P���`�����̍��v�Ƃ��Ďg�p
                for(int i = 1; i < 13; i++){
                    TechnicAndInBeans data = aSelectedYear[i];
                    /** �Z�p����܂��͓��q�� */
                    Integer val = null;
                    /** 12�������v */
                    Long sum12 = null;
                    /** 12�������� */
                    Double ave12 = Double.NaN;
                    if(data != null){
                        val = (type == AnalysisTypes.TechnicSales) ? data.getTechnic() : data.getIn();
                    }
                    if(val != null){
                        tblSelectedYear.setValueAt(nf.format(val), 0, i); // �Z�p����܂��͓��q����\��
                        longSum.add(val.longValue());
                        sum12 = longSum.getSum(aLong[i + 1]);
                        beans[i - 1].setResult(val.doubleValue());
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 0, i);         // �Z�p����܂��͓��q����\�����Ȃ�
                        longSum.nullify();
                        //beans[i - 1].setResult(Double.NaN);
                    }
                    if(sum12 != null){
                        ave12 = sum12.doubleValue() / 12D;
                        tblSelectedYear.setValueAt(nf.format(sum12), 1, i); // 12�������v��\��
                        tblSelectedYear.setValueAt(nf.format(ave12), 2, i); // 12�������ς�\��
                        beans[i - 1].setSum(sum12.doubleValue());
                        beans[i - 1].setAve((ave12 < 0) ? Math.ceil(ave12 - .5d) : Math.floor(ave12 + .5d));
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 1, i);           // 12�������v��\�����Ȃ�
                        tblSelectedYear.setValueAt(EMPTY_STRING, 2, i);           // 12�������ς�\�����Ȃ�
                    }
                }
                break;
                // </editor-fold>
                
                // <editor-fold desc="�q�P���̏ꍇ">
            case SalesPerCustomer:
                Double doubleSum = 0D;
                // ���N�̃f�[�^�̏���
                for(int i = 12; i > 0; i--){
                    TechnicAndInBeans data = aPreviusYear[i];
                    Double val = (data != null) ? data.getSalesPerCustomer() : Double.NaN;
                    tblPreviusYear.setValueAt((val.isNaN()) ? EMPTY_STRING : nf.format(val), 0, i);
                    aDouble[i] = (doubleSum += val);
                    beans[i - 1] = new SalesAnalyzeBean(
                            ToZenkaku(i + "��"),
                            (val < 0) ? Math.ceil(val - .5d) : Math.floor(val + .5d));
                }
                
                // ���N�̃f�[�^�̏���
                doubleSum = 0D;              // �ȍ~�A�Y���N�x�P���`�����̍��v�Ƃ��Ďg�p
                for(int i = 1; i < 13; i++){
                    TechnicAndInBeans data = aSelectedYear[i];
                    /** �q�P�� */
                    Double val = (data != null) ? data.getSalesPerCustomer() : Double.NaN;
                    /** 12�������v */
                    Double sum12 = null;
                    /** 12�������� */
                    Double ave12 = Double.NaN;
                    if(!val.isNaN()){
                        tblSelectedYear.setValueAt(nf.format(val), 0, i); // �q�P����\��
                        doubleSum += val;
                        sum12 = doubleSum + aDouble[i + 1];
                        beans[i - 1].setResult((val < 0) ? Math.ceil(val - .5d) : Math.floor(val + .5d));
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 0, i);         // �q�P����\�����Ȃ�
                        doubleSum = Double.NaN;
                        sum12 = Double.NaN;
                    }
                    if(!sum12.isNaN()){
                        ave12 = sum12 / 12D;
                        tblSelectedYear.setValueAt(nf.format(sum12), 1, i); // 12�������v��\��
                        tblSelectedYear.setValueAt(nf.format(ave12), 2, i); // 12�������ς�\��
                        beans[i - 1].setSum((sum12 < 0) ? Math.ceil(sum12 - .5d) : Math.floor(sum12 + .5d));
                        beans[i - 1].setAve((ave12 < 0) ? Math.ceil(ave12 - .5d) : Math.floor(ave12 + .5d));
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 1, i);           // 12�������v��\�����Ȃ�
                        tblSelectedYear.setValueAt(EMPTY_STRING, 2, i);           // 12�������ς�\�����Ȃ�
                    }
                }
                break;
                // </editor-fold>
        }
        // �����I�ɍĕ`�悳����
        tblSelectedYear.repaint();
        tblPreviusYear.repaint();
        tblSelectedYear.getTableHeader().repaint();
        tblPreviusYear.getTableHeader().repaint();
        
        // �O���t��`�悷��
        DrawGraph();
    }//GEN-LAST:event_btnDisplayActionPerformed
    
    /** �����������W�I�{�^�� */
    private void rdoAutoRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAutoRangeActionPerformed
                                                    }//GEN-LAST:event_rdoAutoRangeActionPerformed
    
    /** �Z�p���ド�W�I�{�^�� */
    private void rdoTechnicSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTechnicSalesActionPerformed
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
                                                    }//GEN-LAST:event_rdoTechnicSalesActionPerformed
    
    /** PDF�o�̓{�^�� */
    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        if(valueAxis == null){
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
            return;
        }
        ExportFile(SalesAnalyzeLogic.EXPORT_FILE_PDF);
        btnPdf.setEnabled(false);
    }//GEN-LAST:event_btnPdfActionPerformed
    
// <editor-fold desc="�f�U�C�i�ɂ��ϐ��錾">
// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup analysisTypeGroup;
    private javax.swing.JButton btnDisplay;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnPdf;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbShop;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMax;
    private javax.swing.JLabel lblMin;
    private javax.swing.JLabel lblPreviusYear;
    private javax.swing.JLabel lblRange;
    private javax.swing.JLabel lblSelectedYear;
    private javax.swing.JLabel lblShop;
    private javax.swing.JLabel lblTargetPeriod;
    private javax.swing.JLabel lblTitle;
    private javax.swing.ButtonGroup rangeGroup;
    private javax.swing.JRadioButton rdoAutoRange;
    private javax.swing.JRadioButton rdoCustomerCount;
    private javax.swing.JRadioButton rdoManualRange;
    private javax.swing.JRadioButton rdoSalesPerCustomer;
    private javax.swing.JRadioButton rdoTechnicSales;
    private javax.swing.JSplitPane splitpanePreviusYear1;
    private javax.swing.JSplitPane splitpaneSelectedYear;
    private javax.swing.JTable tblPreviusYear;
    private javax.swing.JTable tblSelectedYear;
    private javax.swing.JFormattedTextField txtMax;
    private javax.swing.JFormattedTextField txtMin;
// End of variables declaration//GEN-END:variables
// </editor-fold>
        
        // <editor-fold desc="�ϐ��錾">
        /** �󗓂ɕ\�����镶���� */
        private final String EMPTY_STRING = "";
        
        /** �\�����̕��͎�� */
        AnalysisTypes type;
        ///** �\�����̔N */
        //YearItem yearSelected = null;
        
        /** ���[�ɏo�͂���p�����[�^ */
        private HashMap<String,Object> paramMap = new HashMap<String,Object>();
        /** ���[�ɏo�͂���Beans */
        SalesAnalyzeBean[] beans = new SalesAnalyzeBean[12];
        
        /** ���l�t�H�[�}�b�^ */
        private NumberFormat nf;
        /** ���l�̊ۂߕ��i�l�̌ܓ����g�p�j */
        private final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
        
        /** �X�ܖ� */
        private String shopName;
        /** �X��ID */
        private int shopId = 0;
        
        /** �w�b�_�̍��� */
        private final Integer HEADER_HEIGHT = 16;
        /** �s�̍��� */
        private final Integer ROW_HEIGHT = SystemInfo.TABLE_ROW_HEIGHT;
        /** �ŏ��̗�̕� */
        private final Integer FIRST_COLUMN_WIDTH = 91;
        
        /** ��ʗp�O���t�̃t�H���g */
        private Font fontGraph = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        
        // �O���t�̒l��
        ValueAxis valueAxis = null;
        
        private BusinessReportFocusTraversalPolicy ftp;
        
        JCommonDrawableRenderer chartrenderer;
// </editor-fold>
        
        /** �R���X�g���N�^ */
        public SalesAnalyzePanel() {
            /** ���l�t�H�[�}�b�^�̏����� */
            nf = NumberFormat.getNumberInstance();  // �n��ˑ��̐��l�t�H�[�}�b�g
            nf.setRoundingMode(ROUNDING_MODE);      // �l�̌ܓ�
            nf.setMaximumFractionDigits(0);         // �����_�ȉ��O��
            
            /** �t�H�[���̏����� */
            initComponents();
            
            ftp = new BusinessReportFocusTraversalPolicy();
            setFocusCycleRoot(true);
            setFocusTraversalPolicy(getFocusTraversalPolicy());
            addMouseCursorChange();
            this.setSize(834, 691);
            this.setPath("����");
            this.setTitle("���㕪��");
            
            
            /** �L�[���X�i�[��o�^ */
            this.setKeyListener();
            /** �X�܃R���{�{�b�N�X������������(2:�X�܂̂�) */
            SystemInfo.initGroupShopComponents(cmbShop, 2);
            getShopId();
            
            txtMax.setValue(2000000L);
            txtMin.setValue(1000000L);
            
            /** �\�̃f�U�C���̏����� */
            initTable(tblSelectedYear);
            initTable(tblPreviusYear);
            initCmbYear();
        }
        
        /** �X�܃R���{�{�b�N�X����X��ID�E�X��ID���擾 */
        private void getShopId() {
            MstShop ms = new MstShop();
            if( 0 <= this.cmbShop.getSelectedIndex() ) {
                ms  = (MstShop)this.cmbShop.getSelectedItem();
                shopId    = ms.getShopID();
                shopName  = ms.getShopName();
            }
        }
        
        /** �W�v���ԃR���{�{�b�N�X�̏����� */
        private void initCmbYear() {
            this.cmbYear.removeAllItems();
            /** ���N��I�����邽�߂̃C���f�b�N�X */
            int index = -1;
            /** ���N�̕����� */
            String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            /** SalesDateBean�̃��X�g */
            ArrayList<SalesDateBean> listYaer = TechnicSalesReportLogic.getSalesDateBean(this.shopId);
            int i = 0;
            for (SalesDateBean beanYear : listYaer){
                String year = beanYear.getYear();
                this.cmbYear.addItem(new YearItem(year));
                if(year.equals(currentYear)){
                    index = i;
                }
                i++;
            }
            cmbYear.setSelectedIndex(index);
        }
        
        /** �N�R���{�{�b�N�X�ɓ����A�C�e�� */
        private class YearItem{
            /** �N�̕�����\�L */
            private String str;
            /** �N�̕�����\�L */
            private String year;
            /** �N�̐��l */
            private int numYear = 0;
            
            public YearItem(String year){
                try{
                    this.numYear = Integer.parseInt(year);
                }catch(NumberFormatException e){}
                this.year = year;
                this.str = year + "�N";
            }
            
            /** �����Ɂu�N�v�������N�̕�����\�L��Ԃ��܂� */
            public String toString(){ return str;}
            /** �N�𐔒l�ŕԂ��܂� */
            public int getValue(){return numYear;}
            /** �N�̕�����\�L��Ԃ��܂� */
            public String getString(){return year;}
        }
        
        /** �\�̃f�U�C���������� */
        private void initTable(JTable table){
            table.getTableHeader().setReorderingAllowed(false); // ��̕��ёւ��֎~
            table.getTableHeader().setResizingAllowed(false);   // ��̃��T�C�Y�֎~
            table.getTableHeader().setSize(999, HEADER_HEIGHT); // �w�b�_�̍�����ݒ�
            // �ŏ��̗�̕���ݒ�
            TableColumnModel model = table.getTableHeader().getColumnModel();
            model.getColumn(0).setMinWidth(FIRST_COLUMN_WIDTH);
            model.getColumn(0).setPreferredWidth(FIRST_COLUMN_WIDTH);
            table.getTableHeader().setColumnModel(model);
            
            table.setRowHeight(ROW_HEIGHT);                     // �s�̍�����ݒ�
            
            /** �w�b�_�̃����_����ݒ� */
            SwingUtil.setJTableHeaderRenderer(table, SystemInfo.getTableHeaderRenderer());
            
            /** �ʏ�Z���̃����_����ݒ� */
            model = table.getColumnModel();
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
//            cellRenderer.setBorder(javax.swing.BorderFactory.createBevelBorder(
//                    javax.swing.border.BevelBorder.RAISED,
//                    null,
//                    new java.awt.Color(254, 254, 254),
//                    new java.awt.Color(154, 154, 154),
//                    new java.awt.Color(220, 220, 220)));
            cellRenderer.setBackground(new java.awt.Color(204, 204, 204));
            model.getColumn(0).setCellRenderer(cellRenderer);
            
            cellRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.TRAILING);
            for(int i = 1; i < table.getColumnCount(); i++){
                model.getColumn(i).setCellRenderer(cellRenderer);
            }
        }
        
        /** �O���t��`�悷�� */
        private void DrawGraph(){
            DefaultCategoryDataset data = new DefaultCategoryDataset();
            
            double min = Double.MAX_VALUE;
            double max = 0d;
            
            jPanel2.removeAll(); // �ȑO�̃O���t���N���A����
            
            for (int i = 0; i < beans.length; i++) {
                Double result = beans[i].getResult();
                Double ave = beans[i].getAve();
                data.addValue(result, "����", beans[i].getMonth());
                data.addValue(ave, "�ړ��l", beans[i].getMonth());
                if(result != null && !Double.isNaN(result)){
                    min = Math.min(min,result);
                    max = Math.max(max,result);
                }
                if(ave != null && !Double.isNaN(ave)){
                    min = Math.min(min,ave);
                    max = Math.max(max,ave);
                }
            }
            
            if(min < max){
                max += (max - min) * 0.2d;
                min -= (max - min) * 0.05d;
                if(rdoAutoRange.isSelected()){
                    txtMax.setValue(max);
                    txtMin.setValue(min);
                }
            }else if(min == max){ // �l�����ׂē����ꍇ
                if(min == 0d){
                    max = 1d;
                }else{
                    min = 0d;
                    max *= 2d;
                }
            }else{
                chartrenderer = null;
                return; // �L���ȃf�[�^���ꌏ�����݂��Ȃ��ꍇ
            }
            
            if(rdoManualRange.isSelected()){
                try {
                    min = ((Long)txtMin.getValue()).doubleValue();
                } catch (Exception ex) {}
                try {
                    max = ((Long)txtMax.getValue()).doubleValue();
                } catch (Exception ex) {}
            }
            
            JFreeChart chart = ChartFactory.createLineChart(
                    "",                         // �^�C�g��
                    "",                         // �J�e�S�������x��
                    "",                         // �l�����x��
                    data,                       // �f�[�^
                    PlotOrientation.VERTICAL,   // ����
                    true,                       // �}��̗L��
                    false,                      // �c�[���`�b�v
                    false);                     // URLs
            
            CategoryPlot categoryPlot = chart.getCategoryPlot();
            // �l�����擾
            valueAxis = categoryPlot.getRangeAxis();
            valueAxis.setRange(min, max);
            // �P���`�P�Q���̃t�H���g���w��
            CategoryAxis domainAxis = categoryPlot.getDomainAxis();
            for (int i = 0; i < beans.length; i++) {
                domainAxis.setTickLabelFont(beans[i].getMonth(), fontGraph);
            }
            
            categoryPlot.setRowRenderingOrder(SortOrder.DESCENDING); // �t���ɕ`��i�ŏ��̍s����O�j
            categoryPlot.setAxisOffset(new RectangleInsets(-31,0,0,0));
            categoryPlot.setBackgroundAlpha(0f); // �w�i�𓧖���
            categoryPlot.setInsets(new RectangleInsets(0,0,0,3));
            
            // �����_�����擾
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setRangeGridlinePaint(Color.GRAY);
            plot.setDomainGridlinesVisible(false);
            //plot.setDomainGridlinePaint(Color.PINK);
            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            
            // �_���v���b�g����
            renderer.setSeriesShapesVisible(0,true);
            renderer.setSeriesShapesVisible(1,true);
            
            // �܂���𑾂�����
            renderer.setSeriesStroke(0, new BasicStroke(3f));
            renderer.setSeriesStroke(1, new BasicStroke(
                    3f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    1f,
                    new float[]{10f,5f},
                    5f));
            
            // �_���ł�������
            renderer.setSeriesOutlineStroke(0, new BasicStroke(5f));
            renderer.setSeriesOutlineStroke(1, new BasicStroke(5f));
            
            //// �F�̐ݒ�
            //renderer.setSeriesPaint(0, new Color(0x00008B));
            //renderer.setSeriesPaint(1, new Color(0x006400));
            
            // �}����擾����
            LegendTitle lt = chart.getLegend();
            
            lt.setPadding(3,3,3,3);             //�]���̐ݒ�
            lt.setPosition(RectangleEdge.TOP);  //�}��̐ݒu�ʒu
            lt.setMargin(8,114,0,0);
            lt.setHorizontalAlignment(HorizontalAlignment.LEFT);
            lt.setItemFont(fontGraph);
            //lt.setLegendItemGraphicEdge(RectangleEdge.TOP); // ���x���ɑ΂�����z�u�g�̔z�u
            //lt.setLegendItemGraphicLocation(RectangleAnchor.RIGHT); //���z�u�g�ɂ�������̔z�u
            //lt.setLegendItemGraphicPadding(new RectangleInsets(13,3,3,3)); //���z�u�g�ɂ�������̃}�[�W��
            //lt.setLegendItemGraphicAnchor(RectangleAnchor.CENTER); // ���ɑ΂���_�̈ʒu
            ChartPanel cpanel = new ChartPanel(chart);
            
            jPanel2.setLayout(new BorderLayout(0,0));
            jPanel2.add(cpanel, BorderLayout.CENTER);
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
//            try {
//                ChartUtilities.writeChartAsPNG(stream, chart, 756, 352);
//            } catch (IOException ex) {
//            }
//            chartrenderer = JRImageRenderer.getInstance(stream.toByteArray());
//            try {
//                stream.close();
//            } catch (IOException ex) {
//            }
            chartrenderer = new JCommonDrawableRenderer(chart);
            btnExcel.setEnabled(true);
            btnPdf.setEnabled(true);
        }
        
        /** �o�͂��� */
        private void ExportFile(int exportType){
            if(valueAxis != null){
                paramMap.put("RANGE",valueAxis.getRange()); // �͈͂��Z�b�g
            }
            paramMap.put("ANALYZE_TYPE",type.toString());   // ���͎�ʂ��Z�b�g
            paramMap.put("SHOP",shopId + shopName); // �X�܂��Z�b�g
            
            // �\�����̕\����N�x��������擾���ăZ�b�g
            paramMap.put("SELECTED_YEAR", lblSelectedYear.getText());
            paramMap.put("PREVIUS_YEAR", lblPreviusYear.getText());
            
            paramMap.put("CHART", chartrenderer); // �`���[�g�����_�����Z�b�g
            SalesAnalyzeLogic report = new SalesAnalyzeLogic();
            report.generateFile(Arrays.asList(beans), paramMap, valueAxis.getRange(), exportType);
            
            return;
        }
        
        /** �G�N�Z���t�@�C�����o�͂��� */
        private void ExportXLSFile(){
            InputStream Template = null;
            HSSFWorkbook workbook = null;
            try{
//                Template = new FileInputStream(
//                        ReportGeneratorLogic.getExportPath()+
//                        "SalesAnalyzeTemplate.xls");
                Template = getClass().getResourceAsStream("/reports/SalesAnalyzeTemplate.xls");
                workbook = new HSSFWorkbook(new POIFSFileSystem(Template));
            }catch(IOException e){
                SystemInfo.getLogger().log(Level.WARNING,
                        e.getLocalizedMessage(), e);
            }finally{
                try{
                    if(Template != null){
                        Template.close();
                    }
                }catch (IOException e){
                    SystemInfo.getLogger().log(Level.WARNING,
                            e.getLocalizedMessage(), e);
                }
            }
            
            HSSFSheet sheet = workbook.getSheetAt(0);
            
            HSSFCell cell;
            // ���͎�ʂ��Z�b�g
            cell = sheet.getRow(1).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(type.toString());
            
            cell = sheet.getRow(5).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(type.toString());
            
            cell = sheet.getRow(11).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue("�O�N"+type.toString());
            
            // �X�܂��Z�b�g
            cell = sheet.getRow(1).getCell((short)8);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(shopId + shopName);
            
            // �\�����̕\����N�x��������擾���ăZ�b�g
            cell = sheet.getRow(4).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(lblSelectedYear.getText());
            
            cell = sheet.getRow(10).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(lblPreviusYear.getText());
            
            /** ���т̍s */
            HSSFRow rowResult = sheet.getRow(6);
            /** 12�������v�̍s */
            HSSFRow rowSum = sheet.getRow(7);
            /** �ړ��l�̍s */
            HSSFRow rowAve = sheet.getRow(8);
            /** �O�N�x���т̍s */
            HSSFRow rowPreviousResult = sheet.getRow(12);
            
            // �l��\�ɏ�������
            for(int i = 0; i < 12; i++){
                short cellIndex = (short)(i + 1);
                Double val;
                val = beans[i].getResult();
                if(val == null || val.isNaN()){
                    rowResult.getCell(cellIndex).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    rowResult.getCell(cellIndex).setCellValue(val);
                }
                
                val = beans[i].getSum();
                if(val == null || val.isNaN()){
                    rowSum.getCell(cellIndex).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    rowSum.getCell(cellIndex).setCellValue(val);
                }
                
                val = beans[i].getAve();
                if(val == null || val.isNaN()){
                    rowAve.getCell(cellIndex).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    rowAve.getCell(cellIndex).setCellValue(val);
                }
                
                val = beans[i].getPreviusResult();
                if(val == null || val.isNaN()){
                    rowPreviousResult.getCell(cellIndex).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    rowPreviousResult.getCell(cellIndex).setCellValue(val);
                }
            }
            if(valueAxis != null){
                /** �\�����̃O���t�̍ő�l */
                double min = valueAxis.getLowerBound();
                /** �\�����̃O���t�̍ŏ��l */
                double max = valueAxis.getUpperBound();
                Double diff = max - min;
                // �ŏ��l��؂�̗ǂ����܂ŉ�����
                if(!diff.isNaN() && !diff.isInfinite() && diff > 0){
                    double tick = Math.pow(10d,
                            Math.max(Math.floor(Math.log10(diff)) - 1, 0));
                    if(diff / tick > 50){
                        tick *= 10;
                    }else if (diff / tick > 20){
                        tick *= 5;
                    }else if (diff / tick > 10){
                        tick *= 2;
                    }
                    min = Math.floor(min / tick) * tick;
                }
                // �ő�l���Z�b�g
                if(Double.isNaN(max)){
                    sheet.getRow(14).getCell((short)1).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    sheet.getRow(14).getCell((short)1).setCellValue(max);
                }
                // �ŏ��l���Z�b�g
                if(Double.isNaN(min)){
                    sheet.getRow(15).getCell((short)1).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    sheet.getRow(15).getCell((short)1).setCellValue(min);
                }
            }
            
            FileOutputStream streamOut = null;
            String exportFile = ReportManager.getExportPath() +
                    SalesAnalyzeLogic.REPORT +
                    new SimpleDateFormat("yyyyMMddhms").format(new java.util.Date()) +
                    ".xls";
            try{
                streamOut = new FileOutputStream(exportFile);
                workbook.write(streamOut);
            }catch(IOException e){
                System.out.println(e.toString());
            }finally{
                try {
                    if(streamOut != null){
                        streamOut.close();
                    }
                }catch(IOException e){
                    SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
            
            try{
                Runtime runtime = Runtime.getRuntime();
                runtime.exec( "cmd /c \"" + exportFile + "\"");
            }catch( java.io.IOException e ){
                SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        }
        
        /** String���̔��p������S�p�ɒu�����̂�Ԃ� */
        private String ToZenkaku(String str){
            char[] ca = str.toCharArray();
            CharRange numChar = new CharRange('0','9');
            for(int i = 0; i < str.length(); i++){
                if(numChar.contains(ca[i])){
                    ca[i] = (char)(ca[i] - '0' + '�O');
                }
            }
            return String.valueOf(ca);
        }
        
        /** null��NaN����������Z�� */
        private class LongAdder {
            /** �l */
            Long value = null;
            
            /** �R���X�g���N�^ */
            LongAdder(Long value){this.value = value;}
            
            /** �l���擾���� */
            Long getValue(){return value;}
            
            /** ���Z���� */
            LongAdder add(Long addend){
                if(value != null){
                    if(addend == null){
                        value = null;
                    } else{
                        value += addend;
                    }
                }
                return this;
            }
            
            /** ���Z���ʂ�Ԃ�(���g�͕ω����Ȃ�) */
            Long getSum(Long addend){
                if(value == null || addend == null){return null;} else{return value + addend;}
            }
            
            /** null�� */
            void nullify(){value = null;}
            /** �l��Zero�ɂ��� */
            LongAdder zeroize(){ value = 0L; return this;}
        }
        
        /** �O���t�ƕ\���N���A���� */
        private void ClearGraphAndTable(){
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
            chartrenderer = null;
            jPanel2.removeAll(); // �O���t�𖕏�
            valueAxis = null;
            // �\�̃N���A
            lblSelectedYear.setText("");
            lblPreviusYear.setText("");
            tblSelectedYear.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("");
            tblPreviusYear.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("");
            for(int i = 1; i < 13; i++){
                tblSelectedYear.setValueAt("",0,i);
                tblSelectedYear.setValueAt("",1,i);
                tblSelectedYear.setValueAt("",2,i);
                tblPreviusYear.setValueAt("",0,i);
            }
        }
        
        /** ���͎�ʂ̗񋓌^ */
        private enum AnalysisTypes{
            TechnicSales{
                @Override
                public String toString() {return "�Z�p����";}
            },
            CustomerCount{
                @Override
                public String toString() {return "���q��";}
            },
            SalesPerCustomer{
                @Override
                public String toString() {return "�q�P��";}
            }
        }
        
        /**  �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B*/
        private void addMouseCursorChange() {
            SystemInfo.addMouseCursorChange(btnDisplay);
            SystemInfo.addMouseCursorChange(btnPdf);
        }
        
        private void setKeyListener() {
            Iterator i = ftp.getIterator();
            while(i.hasNext()){
                Component co = (Component)i.next();
                if(co != null){
                    co.addKeyListener(SystemInfo.getMoveNextField());
                    co.addFocusListener(SystemInfo.getSelectText());
                }
            }
        }
        
        /** �Z�p����Ɠ��q����Beans */
        private class TechnicAndInBeans{
            /** �� */
            private Integer month = null;
            /** �Z�p���� */
            private Integer technic = null;
            /** ���q�� */
            private Integer in = null;
            
            /** �R���X�g���N�^ */
            public TechnicAndInBeans(Integer month, Integer technic, Integer in){
                setMonth(month);
                setTechnic(technic);
                setIn(in);
            }
            
            /** �����擾 */
            public Integer getMonth(){return this.month;}
            
            /** ����ݒ� */
            public void setMonth(Integer month) {this.month = month;}
            
            /** �Z�p������擾 */
            public Integer getTechnic() {return technic;}
            
            /** �Z�p�����ݒ� */
            public void setTechnic(Integer technic) {this.technic = technic;}
            
            /** ���q�����擾 */
            public Integer getIn() {return in;}
            
            /** ���q����ݒ� */
            public void setIn(Integer in) {this.in = in;}
            
            /** �q�P�����擾(�v�Z�ł��Ȃ��ꍇ��NaN��Ԃ��܂�) */
            public double getSalesPerCustomer(){
                if(in != null && in != 0 && technic != null){
                    return technic.doubleValue() / in.doubleValue();
                } else{
                    return Double.NaN;
                }
            }
        }
        
        /** �w�肵���N�̌��ԋZ�p���エ��ѓ��q�����擾���� */
        protected boolean getAnnualData(
                int shopId,
                int year,
                TechnicAndInBeans[] array){
            boolean retrieved = false;
            ConnectionWrapper dbAccess = SystemInfo.getConnection();
            String sqlQuery =
                    "SELECT  month, result_technic, result_in " +
                    "FROM data_target_result " +
                    "WHERE shop_id=" + SQLUtil.convertForSQL(shopId) +
                    " AND year=" + SQLUtil.convertForSQL(year) +
                    " AND delete_date IS NULL";
            try{
                ResultSetWrapper result = dbAccess.executeQuery( sqlQuery );
                
                while( result.next() ){
                    Integer month = result.getInt("month");
                    if(result.wasNull()){month=null;}
                    Integer technic = result.getInt("result_technic");
                    if(result.wasNull()){technic=null;}
                    Integer in = result.getInt("result_in");
                    if(result.wasNull()){in=null;}
                    if (month != null && month > 0 && month < 13){
                        array[month]= new TechnicAndInBeans(month, technic, in);
                        retrieved = true;
                    }
                }
                result.close();
            } catch( Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            return retrieved;
        }
        
        /** �Ɩ��񍐉�ʗpFocusTraversalPolicy���擾����B
         * @return  �Ɩ��񍐉�ʗpFocusTraversalPolicy
         */
        public BusinessReportFocusTraversalPolicy getFocusTraversalPolicy() {
            return	ftp;
        }
        
        /** �N�Ԉړ��O���t��ʗpFocusTraversalPolicy */
        private class BusinessReportFocusTraversalPolicy
                extends FocusTraversalPolicy {
            
            /** �R���g���[���̃��X�g */
            ArrayList<Component> controls = new ArrayList<Component>();
            
            public BusinessReportFocusTraversalPolicy(){
                controls.addAll(Arrays.asList(
                        cmbShop,
                        rdoTechnicSales,
                        rdoCustomerCount,
                        rdoSalesPerCustomer,
                        cmbYear,
                        rdoAutoRange,
                        rdoManualRange,
                        txtMax,
                        txtMin
//                    btnDisplay,
//                    btnPdf
                        ));
            }
            
            /**  �R���g���[���ꗗ��listIterator��Ԃ��܂��B */
            public ListIterator getListIterator(int index){
                return controls.listIterator(index);
            }
            
            /**  �R���g���[���ꗗ��listIterator��Ԃ��܂��B */
            public ListIterator getListIterator(){return getListIterator(0);}
            
            /** �R���g���[���ꗗ��Iterator��Ԃ��܂��B */
            public Iterator getIterator(){return controls.iterator();}
            
            /** aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B */
            public Component getComponentAfter(Container aContainer,
                    Component aComponent) {
                int index = controls.indexOf(aComponent);
                if(index < 0 || index >= controls.size()){
                    return null;
                }
                ListIterator i = controls.listIterator(index);
                i.next();
                if(!i.hasNext()) {
                    i = controls.listIterator();
                }
                while(i.nextIndex() != index) {
                    Component co = (Component) i.next();
                    if (co.isEnabled()) {
                        return co;
                    }
                    if(!i.hasNext()) {
                        i = controls.listIterator();
                    }
                }
                return null;
            }
            
            /** aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B */
            public Component getComponentBefore(Container aContainer,
                    Component aComponent) {
                int index = controls.indexOf(aComponent);
                if(index < 0 || index >= controls.size()){
                    return null;
                }
                ListIterator i = controls.listIterator(index);
                while(i.previousIndex() != index) {
                    if(!i.hasPrevious()) {
                        i = controls.listIterator(controls.size());
                    }
                    Component co = (Component) i.previous();
                    if (co.isEnabled()) {
                        return co;
                    }
                }
                return null;
            }
            
            /** �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B */
            public Component getFirstComponent(Container aContainer) {
                return getDefaultComponent(aContainer);
            }
            
            /** �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B */
            public Component getLastComponent(Container aContainer) {
                return getComponentBefore(aContainer, controls.get(0));
            }
            
            /** �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B */
            public Component getDefaultComponent(Container aContainer) {
                for(Component co : controls){
                    if( co.isEnabled() ){
                        return co;
                    }
                }
                return controls.get(0);
            }
            
            /** �E�B���h�E���ŏ��ɕ\�����ꂽ�Ƃ��Ƀt�H�[�J�X���ݒ肳���R���|�[�l���g��Ԃ��܂��B */
            public Component getInitialComponent(Window window) {
                for(Component co : controls){
                    if( co.isEnabled() ){
                        return co;
                    }
                }
                return controls.get(0);
            }
            
        }
}

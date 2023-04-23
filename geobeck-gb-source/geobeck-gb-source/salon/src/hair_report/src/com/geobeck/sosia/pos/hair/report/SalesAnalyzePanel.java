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

/** 年間移動グラフ
 * @author torino
 */
public class SalesAnalyzePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    /** フォームの初期化（自動生成） */
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
                    {"実績", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"１２ヶ月合計", null, null, null, null, null, null, null, null, null, null, null, null},
                    {"移動値", null, null, null, null, null, null, null, null, null, null, null, null}
        },
                new String [] {
            "", "１月", "２月", "３月", "４月", "５月", "６月", "７月", "８月", "９月", "１０月", "１１月", "１２月"
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
                    {"前年度実績", "", "", "", "", "", "", "", "", "", "", "", ""}
        },
                new String [] {
            "", "１月", "２月", "３月", "４月", "５月", "６月", "７月", "８月", "９月", "１０月", "１１月", "１２月"
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
    
    /** Excel出力ボタン */
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
    
    /** 手入力ボタンチェックと入力欄の連動 */
    private void rdoManualRangeStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoManualRangeStateChanged
        boolean isSelected = (evt.getStateChange() == ItemEvent.SELECTED);
        txtMax.setEnabled(isSelected);
        txtMin.setEnabled(isSelected);
    }//GEN-LAST:event_rdoManualRangeStateChanged
    
    /** 店舗コンボボックス */
    private void cmbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopActionPerformed
        int shopIdOld = shopId;
        getShopId();
        if(shopIdOld != shopId){
            initCmbYear();      // 集計期間コンボボックスを設定
            
        }
        ClearGraphAndTable();   // グラフと表をクリアする
    }//GEN-LAST:event_cmbShopActionPerformed
    
    /** 表示ボタン */
    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        
        // <editor-fold desc="コンボボックスで年が選択されていない場合">
        if(cmbYear.getSelectedIndex() == -1){
            // 集計期間を選択してください。
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, lblTargetPeriod.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // </editor-fold>
        
        // <editor-fold desc="分析種別をラジオボタンの状態から取得する">
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
        
        // 分析種別を表に反映させる
        tblSelectedYear.getTableHeader().getColumnModel().getColumn(0).setHeaderValue(type);
        tblPreviusYear.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("前年度" + type.toString());
        
        paramMap.put("ANALYZE_TYPE", type.toString());
        
        /** コンボボックスで選択されていた年クラス */
        YearItem yearSelected = (YearItem) cmbYear.getSelectedItem();
        lblSelectedYear.setText(ToZenkaku(yearSelected.getString()) + "年度");
        lblPreviusYear.setText(ToZenkaku((yearSelected.getValue() - 1) + "年度"));
        
        int year = ((YearItem)cmbYear.getSelectedItem()).numYear;
        TechnicAndInBeans[] aSelectedYear = new TechnicAndInBeans[13];
        TechnicAndInBeans[] aPreviusYear = new TechnicAndInBeans[13];
        if((!getAnnualData(this.shopId, year, aSelectedYear)) &
                (!getAnnualData(this.shopId, year - 1, aPreviusYear))){
            // 出力対象データがありません。
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            ClearGraphAndTable(); // グラフと表をクリアする
            return;
        }
        
        /** 前年度ｎ月～１２月の合計(Long) */
        Long[] aLong = new Long[14];
        aLong[13] = 0L;
        /** 前年度ｎ月～１２月の合計(Float) */
        Double[] aDouble = new Double[14];
        aDouble[13] = 0D;
        
        switch(type) {
            // <editor-fold desc="技術売上または入客数の場合">
            case TechnicSales:
            case CustomerCount:
                /** 加算器 */
                LongAdder longSum = new LongAdder(0L);
                // 去年のデータの処理
                for(int i = 12; i > 0; i--){
                    TechnicAndInBeans data = aPreviusYear[i];
                    /** 技術売上または入客数 */
                    Integer val = null;
                    if(data != null){
                        val = (type == AnalysisTypes.TechnicSales) ? data.getTechnic() : data.getIn();
                        beans[i - 1] = new SalesAnalyzeBean(ToZenkaku(i + "月"), val.doubleValue());
                    }else{
                        beans[i - 1] = new SalesAnalyzeBean(ToZenkaku(i + "月"));
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
                // 今年のデータの処理
                longSum.zeroize(); //   以降、該当年度１月～ｉ月の合計として使用
                for(int i = 1; i < 13; i++){
                    TechnicAndInBeans data = aSelectedYear[i];
                    /** 技術売上または入客数 */
                    Integer val = null;
                    /** 12ヶ月合計 */
                    Long sum12 = null;
                    /** 12ヶ月平均 */
                    Double ave12 = Double.NaN;
                    if(data != null){
                        val = (type == AnalysisTypes.TechnicSales) ? data.getTechnic() : data.getIn();
                    }
                    if(val != null){
                        tblSelectedYear.setValueAt(nf.format(val), 0, i); // 技術売上または入客数を表示
                        longSum.add(val.longValue());
                        sum12 = longSum.getSum(aLong[i + 1]);
                        beans[i - 1].setResult(val.doubleValue());
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 0, i);         // 技術売上または入客数を表示しない
                        longSum.nullify();
                        //beans[i - 1].setResult(Double.NaN);
                    }
                    if(sum12 != null){
                        ave12 = sum12.doubleValue() / 12D;
                        tblSelectedYear.setValueAt(nf.format(sum12), 1, i); // 12ヶ月合計を表示
                        tblSelectedYear.setValueAt(nf.format(ave12), 2, i); // 12ヶ月平均を表示
                        beans[i - 1].setSum(sum12.doubleValue());
                        beans[i - 1].setAve((ave12 < 0) ? Math.ceil(ave12 - .5d) : Math.floor(ave12 + .5d));
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 1, i);           // 12ヶ月合計を表示しない
                        tblSelectedYear.setValueAt(EMPTY_STRING, 2, i);           // 12ヶ月平均を表示しない
                    }
                }
                break;
                // </editor-fold>
                
                // <editor-fold desc="客単価の場合">
            case SalesPerCustomer:
                Double doubleSum = 0D;
                // 去年のデータの処理
                for(int i = 12; i > 0; i--){
                    TechnicAndInBeans data = aPreviusYear[i];
                    Double val = (data != null) ? data.getSalesPerCustomer() : Double.NaN;
                    tblPreviusYear.setValueAt((val.isNaN()) ? EMPTY_STRING : nf.format(val), 0, i);
                    aDouble[i] = (doubleSum += val);
                    beans[i - 1] = new SalesAnalyzeBean(
                            ToZenkaku(i + "月"),
                            (val < 0) ? Math.ceil(val - .5d) : Math.floor(val + .5d));
                }
                
                // 今年のデータの処理
                doubleSum = 0D;              // 以降、該当年度１月～ｉ月の合計として使用
                for(int i = 1; i < 13; i++){
                    TechnicAndInBeans data = aSelectedYear[i];
                    /** 客単価 */
                    Double val = (data != null) ? data.getSalesPerCustomer() : Double.NaN;
                    /** 12ヶ月合計 */
                    Double sum12 = null;
                    /** 12ヶ月平均 */
                    Double ave12 = Double.NaN;
                    if(!val.isNaN()){
                        tblSelectedYear.setValueAt(nf.format(val), 0, i); // 客単価を表示
                        doubleSum += val;
                        sum12 = doubleSum + aDouble[i + 1];
                        beans[i - 1].setResult((val < 0) ? Math.ceil(val - .5d) : Math.floor(val + .5d));
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 0, i);         // 客単価を表示しない
                        doubleSum = Double.NaN;
                        sum12 = Double.NaN;
                    }
                    if(!sum12.isNaN()){
                        ave12 = sum12 / 12D;
                        tblSelectedYear.setValueAt(nf.format(sum12), 1, i); // 12ヶ月合計を表示
                        tblSelectedYear.setValueAt(nf.format(ave12), 2, i); // 12ヶ月平均を表示
                        beans[i - 1].setSum((sum12 < 0) ? Math.ceil(sum12 - .5d) : Math.floor(sum12 + .5d));
                        beans[i - 1].setAve((ave12 < 0) ? Math.ceil(ave12 - .5d) : Math.floor(ave12 + .5d));
                    } else{
                        tblSelectedYear.setValueAt(EMPTY_STRING, 1, i);           // 12ヶ月合計を表示しない
                        tblSelectedYear.setValueAt(EMPTY_STRING, 2, i);           // 12ヶ月平均を表示しない
                    }
                }
                break;
                // </editor-fold>
        }
        // 明示的に再描画させる
        tblSelectedYear.repaint();
        tblPreviusYear.repaint();
        tblSelectedYear.getTableHeader().repaint();
        tblPreviusYear.getTableHeader().repaint();
        
        // グラフを描画する
        DrawGraph();
    }//GEN-LAST:event_btnDisplayActionPerformed
    
    /** 自動調整ラジオボタン */
    private void rdoAutoRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAutoRangeActionPerformed
                                                    }//GEN-LAST:event_rdoAutoRangeActionPerformed
    
    /** 技術売上ラジオボタン */
    private void rdoTechnicSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTechnicSalesActionPerformed
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
                                                    }//GEN-LAST:event_rdoTechnicSalesActionPerformed
    
    /** PDF出力ボタン */
    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        if(valueAxis == null){
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
            return;
        }
        ExportFile(SalesAnalyzeLogic.EXPORT_FILE_PDF);
        btnPdf.setEnabled(false);
    }//GEN-LAST:event_btnPdfActionPerformed
    
// <editor-fold desc="デザイナによる変数宣言">
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
        
        // <editor-fold desc="変数宣言">
        /** 空欄に表示する文字列 */
        private final String EMPTY_STRING = "";
        
        /** 表示中の分析種別 */
        AnalysisTypes type;
        ///** 表示中の年 */
        //YearItem yearSelected = null;
        
        /** 帳票に出力するパラメータ */
        private HashMap<String,Object> paramMap = new HashMap<String,Object>();
        /** 帳票に出力するBeans */
        SalesAnalyzeBean[] beans = new SalesAnalyzeBean[12];
        
        /** 数値フォーマッタ */
        private NumberFormat nf;
        /** 数値の丸め方（四捨五入を使用） */
        private final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
        
        /** 店舗名 */
        private String shopName;
        /** 店舗ID */
        private int shopId = 0;
        
        /** ヘッダの高さ */
        private final Integer HEADER_HEIGHT = 16;
        /** 行の高さ */
        private final Integer ROW_HEIGHT = SystemInfo.TABLE_ROW_HEIGHT;
        /** 最初の列の幅 */
        private final Integer FIRST_COLUMN_WIDTH = 91;
        
        /** 画面用グラフのフォント */
        private Font fontGraph = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        
        // グラフの値軸
        ValueAxis valueAxis = null;
        
        private BusinessReportFocusTraversalPolicy ftp;
        
        JCommonDrawableRenderer chartrenderer;
// </editor-fold>
        
        /** コンストラクタ */
        public SalesAnalyzePanel() {
            /** 数値フォーマッタの初期化 */
            nf = NumberFormat.getNumberInstance();  // 地域依存の数値フォーマット
            nf.setRoundingMode(ROUNDING_MODE);      // 四捨五入
            nf.setMaximumFractionDigits(0);         // 小数点以下０桁
            
            /** フォームの初期化 */
            initComponents();
            
            ftp = new BusinessReportFocusTraversalPolicy();
            setFocusCycleRoot(true);
            setFocusTraversalPolicy(getFocusTraversalPolicy());
            addMouseCursorChange();
            this.setSize(834, 691);
            this.setPath("分析");
            this.setTitle("売上分析");
            
            
            /** キーリスナーを登録 */
            this.setKeyListener();
            /** 店舗コンボボックスを初期化する(2:店舗のみ) */
            SystemInfo.initGroupShopComponents(cmbShop, 2);
            getShopId();
            
            txtMax.setValue(2000000L);
            txtMin.setValue(1000000L);
            
            /** 表のデザインの初期化 */
            initTable(tblSelectedYear);
            initTable(tblPreviusYear);
            initCmbYear();
        }
        
        /** 店舗コンボボックスから店舗ID・店舗IDを取得 */
        private void getShopId() {
            MstShop ms = new MstShop();
            if( 0 <= this.cmbShop.getSelectedIndex() ) {
                ms  = (MstShop)this.cmbShop.getSelectedItem();
                shopId    = ms.getShopID();
                shopName  = ms.getShopName();
            }
        }
        
        /** 集計期間コンボボックスの初期化 */
        private void initCmbYear() {
            this.cmbYear.removeAllItems();
            /** 今年を選択するためのインデックス */
            int index = -1;
            /** 今年の文字列 */
            String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            /** SalesDateBeanのリスト */
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
        
        /** 年コンボボックスに入れるアイテム */
        private class YearItem{
            /** 年の文字列表記 */
            private String str;
            /** 年の文字列表記 */
            private String year;
            /** 年の数値 */
            private int numYear = 0;
            
            public YearItem(String year){
                try{
                    this.numYear = Integer.parseInt(year);
                }catch(NumberFormatException e){}
                this.year = year;
                this.str = year + "年";
            }
            
            /** 末尾に「年」をつけた年の文字列表記を返します */
            public String toString(){ return str;}
            /** 年を数値で返します */
            public int getValue(){return numYear;}
            /** 年の文字列表記を返します */
            public String getString(){return year;}
        }
        
        /** 表のデザインを初期化 */
        private void initTable(JTable table){
            table.getTableHeader().setReorderingAllowed(false); // 列の並び替え禁止
            table.getTableHeader().setResizingAllowed(false);   // 列のリサイズ禁止
            table.getTableHeader().setSize(999, HEADER_HEIGHT); // ヘッダの高さを設定
            // 最初の列の幅を設定
            TableColumnModel model = table.getTableHeader().getColumnModel();
            model.getColumn(0).setMinWidth(FIRST_COLUMN_WIDTH);
            model.getColumn(0).setPreferredWidth(FIRST_COLUMN_WIDTH);
            table.getTableHeader().setColumnModel(model);
            
            table.setRowHeight(ROW_HEIGHT);                     // 行の高さを設定
            
            /** ヘッダのレンダラを設定 */
            SwingUtil.setJTableHeaderRenderer(table, SystemInfo.getTableHeaderRenderer());
            
            /** 通常セルのレンダラを設定 */
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
        
        /** グラフを描画する */
        private void DrawGraph(){
            DefaultCategoryDataset data = new DefaultCategoryDataset();
            
            double min = Double.MAX_VALUE;
            double max = 0d;
            
            jPanel2.removeAll(); // 以前のグラフをクリアする
            
            for (int i = 0; i < beans.length; i++) {
                Double result = beans[i].getResult();
                Double ave = beans[i].getAve();
                data.addValue(result, "実績", beans[i].getMonth());
                data.addValue(ave, "移動値", beans[i].getMonth());
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
            }else if(min == max){ // 値がすべて同じ場合
                if(min == 0d){
                    max = 1d;
                }else{
                    min = 0d;
                    max *= 2d;
                }
            }else{
                chartrenderer = null;
                return; // 有効なデータが一件も存在しない場合
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
                    "",                         // タイトル
                    "",                         // カテゴリ軸ラベル
                    "",                         // 値軸ラベル
                    data,                       // データ
                    PlotOrientation.VERTICAL,   // 向き
                    true,                       // 凡例の有無
                    false,                      // ツールチップ
                    false);                     // URLs
            
            CategoryPlot categoryPlot = chart.getCategoryPlot();
            // 値軸を取得
            valueAxis = categoryPlot.getRangeAxis();
            valueAxis.setRange(min, max);
            // １月～１２月のフォントを指定
            CategoryAxis domainAxis = categoryPlot.getDomainAxis();
            for (int i = 0; i < beans.length; i++) {
                domainAxis.setTickLabelFont(beans[i].getMonth(), fontGraph);
            }
            
            categoryPlot.setRowRenderingOrder(SortOrder.DESCENDING); // 逆順に描画（最初の行が手前）
            categoryPlot.setAxisOffset(new RectangleInsets(-31,0,0,0));
            categoryPlot.setBackgroundAlpha(0f); // 背景を透明に
            categoryPlot.setInsets(new RectangleInsets(0,0,0,3));
            
            // レンダラを取得
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setRangeGridlinePaint(Color.GRAY);
            plot.setDomainGridlinesVisible(false);
            //plot.setDomainGridlinePaint(Color.PINK);
            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            
            // 点をプロットする
            renderer.setSeriesShapesVisible(0,true);
            renderer.setSeriesShapesVisible(1,true);
            
            // 折れ線を太くする
            renderer.setSeriesStroke(0, new BasicStroke(3f));
            renderer.setSeriesStroke(1, new BasicStroke(
                    3f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    1f,
                    new float[]{10f,5f},
                    5f));
            
            // 点をでかくする
            renderer.setSeriesOutlineStroke(0, new BasicStroke(5f));
            renderer.setSeriesOutlineStroke(1, new BasicStroke(5f));
            
            //// 色の設定
            //renderer.setSeriesPaint(0, new Color(0x00008B));
            //renderer.setSeriesPaint(1, new Color(0x006400));
            
            // 凡例を取得する
            LegendTitle lt = chart.getLegend();
            
            lt.setPadding(3,3,3,3);             //余白の設定
            lt.setPosition(RectangleEdge.TOP);  //凡例の設置位置
            lt.setMargin(8,114,0,0);
            lt.setHorizontalAlignment(HorizontalAlignment.LEFT);
            lt.setItemFont(fontGraph);
            //lt.setLegendItemGraphicEdge(RectangleEdge.TOP); // ラベルに対する線配置枠の配置
            //lt.setLegendItemGraphicLocation(RectangleAnchor.RIGHT); //線配置枠における線の配置
            //lt.setLegendItemGraphicPadding(new RectangleInsets(13,3,3,3)); //線配置枠における線のマージン
            //lt.setLegendItemGraphicAnchor(RectangleAnchor.CENTER); // 線に対する点の位置
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
        
        /** 出力する */
        private void ExportFile(int exportType){
            if(valueAxis != null){
                paramMap.put("RANGE",valueAxis.getRange()); // 範囲をセット
            }
            paramMap.put("ANALYZE_TYPE",type.toString());   // 分析種別をセット
            paramMap.put("SHOP",shopId + shopName); // 店舗をセット
            
            // 表示中の表から年度文字列を取得してセット
            paramMap.put("SELECTED_YEAR", lblSelectedYear.getText());
            paramMap.put("PREVIUS_YEAR", lblPreviusYear.getText());
            
            paramMap.put("CHART", chartrenderer); // チャートレンダラをセット
            SalesAnalyzeLogic report = new SalesAnalyzeLogic();
            report.generateFile(Arrays.asList(beans), paramMap, valueAxis.getRange(), exportType);
            
            return;
        }
        
        /** エクセルファイルを出力する */
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
            // 分析種別をセット
            cell = sheet.getRow(1).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(type.toString());
            
            cell = sheet.getRow(5).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(type.toString());
            
            cell = sheet.getRow(11).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue("前年"+type.toString());
            
            // 店舗をセット
            cell = sheet.getRow(1).getCell((short)8);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(shopId + shopName);
            
            // 表示中の表から年度文字列を取得してセット
            cell = sheet.getRow(4).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(lblSelectedYear.getText());
            
            cell = sheet.getRow(10).getCell((short)0);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(lblPreviusYear.getText());
            
            /** 実績の行 */
            HSSFRow rowResult = sheet.getRow(6);
            /** 12ヶ月合計の行 */
            HSSFRow rowSum = sheet.getRow(7);
            /** 移動値の行 */
            HSSFRow rowAve = sheet.getRow(8);
            /** 前年度実績の行 */
            HSSFRow rowPreviousResult = sheet.getRow(12);
            
            // 値を表に書き込む
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
                /** 表示中のグラフの最大値 */
                double min = valueAxis.getLowerBound();
                /** 表示中のグラフの最小値 */
                double max = valueAxis.getUpperBound();
                Double diff = max - min;
                // 最小値を切りの良い数まで下げる
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
                // 最大値をセット
                if(Double.isNaN(max)){
                    sheet.getRow(14).getCell((short)1).setCellType(HSSFCell.CELL_TYPE_BLANK);
                }else{
                    sheet.getRow(14).getCell((short)1).setCellValue(max);
                }
                // 最小値をセット
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
        
        /** String中の半角数字を全角に置換ものを返す */
        private String ToZenkaku(String str){
            char[] ca = str.toCharArray();
            CharRange numChar = new CharRange('0','9');
            for(int i = 0; i < str.length(); i++){
                if(numChar.contains(ca[i])){
                    ca[i] = (char)(ca[i] - '0' + '０');
                }
            }
            return String.valueOf(ca);
        }
        
        /** nullをNaN扱いする加算器 */
        private class LongAdder {
            /** 値 */
            Long value = null;
            
            /** コンストラクタ */
            LongAdder(Long value){this.value = value;}
            
            /** 値を取得する */
            Long getValue(){return value;}
            
            /** 加算する */
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
            
            /** 加算結果を返す(自身は変化しない) */
            Long getSum(Long addend){
                if(value == null || addend == null){return null;} else{return value + addend;}
            }
            
            /** nullる */
            void nullify(){value = null;}
            /** 値をZeroにする */
            LongAdder zeroize(){ value = 0L; return this;}
        }
        
        /** グラフと表をクリアする */
        private void ClearGraphAndTable(){
            btnExcel.setEnabled(false);
            btnPdf.setEnabled(false);
            chartrenderer = null;
            jPanel2.removeAll(); // グラフを抹消
            valueAxis = null;
            // 表のクリア
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
        
        /** 分析種別の列挙型 */
        private enum AnalysisTypes{
            TechnicSales{
                @Override
                public String toString() {return "技術売上";}
            },
            CustomerCount{
                @Override
                public String toString() {return "入客数";}
            },
            SalesPerCustomer{
                @Override
                public String toString() {return "客単価";}
            }
        }
        
        /**  ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。*/
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
        
        /** 技術売上と入客数のBeans */
        private class TechnicAndInBeans{
            /** 月 */
            private Integer month = null;
            /** 技術売上 */
            private Integer technic = null;
            /** 入客数 */
            private Integer in = null;
            
            /** コンストラクタ */
            public TechnicAndInBeans(Integer month, Integer technic, Integer in){
                setMonth(month);
                setTechnic(technic);
                setIn(in);
            }
            
            /** 月を取得 */
            public Integer getMonth(){return this.month;}
            
            /** 月を設定 */
            public void setMonth(Integer month) {this.month = month;}
            
            /** 技術売上を取得 */
            public Integer getTechnic() {return technic;}
            
            /** 技術売上を設定 */
            public void setTechnic(Integer technic) {this.technic = technic;}
            
            /** 入客数を取得 */
            public Integer getIn() {return in;}
            
            /** 入客数を設定 */
            public void setIn(Integer in) {this.in = in;}
            
            /** 客単価を取得(計算できない場合はNaNを返します) */
            public double getSalesPerCustomer(){
                if(in != null && in != 0 && technic != null){
                    return technic.doubleValue() / in.doubleValue();
                } else{
                    return Double.NaN;
                }
            }
        }
        
        /** 指定した年の月間技術売上および入客数を取得する */
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
        
        /** 業務報告画面用FocusTraversalPolicyを取得する。
         * @return  業務報告画面用FocusTraversalPolicy
         */
        public BusinessReportFocusTraversalPolicy getFocusTraversalPolicy() {
            return	ftp;
        }
        
        /** 年間移動グラフ画面用FocusTraversalPolicy */
        private class BusinessReportFocusTraversalPolicy
                extends FocusTraversalPolicy {
            
            /** コントロールのリスト */
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
            
            /**  コントロール一覧のlistIteratorを返します。 */
            public ListIterator getListIterator(int index){
                return controls.listIterator(index);
            }
            
            /**  コントロール一覧のlistIteratorを返します。 */
            public ListIterator getListIterator(){return getListIterator(0);}
            
            /** コントロール一覧のIteratorを返します。 */
            public Iterator getIterator(){return controls.iterator();}
            
            /** aComponent のあとでフォーカスを受け取る Component を返します。 */
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
            
            /** aComponent の前にフォーカスを受け取る Component を返します。 */
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
            
            /** トラバーサルサイクルの最初の Component を返します。 */
            public Component getFirstComponent(Container aContainer) {
                return getDefaultComponent(aContainer);
            }
            
            /** トラバーサルサイクルの最後の Component を返します。 */
            public Component getLastComponent(Container aContainer) {
                return getComponentBefore(aContainer, controls.get(0));
            }
            
            /** フォーカスを設定するデフォルトコンポーネントを返します。 */
            public Component getDefaultComponent(Container aContainer) {
                for(Component co : controls){
                    if( co.isEnabled() ){
                        return co;
                    }
                }
                return controls.get(0);
            }
            
            /** ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。 */
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

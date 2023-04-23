/*
 * StoreReportPanel.java
 *
 * Created on 2012/11/23, 13:00cmbPeriodStart1
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.*;
import javax.swing.*;
import com.geobeck.swing.*;
import javax.swing.text.PlainDocument;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.hair.report.logic.ReportLogic;
import com.geobeck.sosia.pos.hair.report.logic.AnalyticChartReportLogic;

import com.geobeck.sosia.pos.util.*;
import java.text.SimpleDateFormat;
import com.geobeck.sql.*;
import com.geobeck.util.CheckUtil;
import java.awt.event.ActionEvent;

import java.util.GregorianCalendar;
import org.apache.commons.lang.math.NumberUtils;
import java.util.HashMap;

/**
 *
 * @author Nguyen Tan Luc
 */
public class StoreReportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    // start add 20130805 nakhoa 役務機能使用有無の制御追加
    private boolean isLoading = true;
    // end add 20130805 nakhoa 役務機能使用有無の制御追加
    
    
     private StoreReportFocusTraversalPolicy ftp =
            new StoreReportFocusTraversalPolicy();
    /**
     * Creates new form StoreReportPanel
     */
    
    public StoreReportPanel() {

        initComponents();
        this.setSize(980, 750);
        this.setPath("店舗分析");
        this.setTitle("店舗分析");
        init();
        changeTargetDate();

    }

    /**
     * init form
     */
    private void init() {
        
        DayShiftStateChanged();
        //lblSpe7.setVisible(false);
        Calendar cal = Calendar.getInstance();
        int nowYear;
        int nowMonth;

        //詳細な売り上げコントロールを初期設定する

        //期間を初期設定する
        this.cmbPeriodStart1.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        this.cmbPeriodEnd1.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        Calendar cdr = Calendar.getInstance();
        cdr.setTime(new Date());
        int nowYear1 = cdr.get(Calendar.YEAR);
        int nowMonth1 = cdr.get(Calendar.MONTH) + 1;
        this.cmbDayWayStartDate.setDate(nowYear1, nowMonth1, 1);
        this.cmbDayWayEndDate.setDate(new Date());
        this.cmbTargetPeriodMonthStart1.setSelectedIndex(0);
        this.initYearCombo(cmbTargetPeriodYearStart1, nowYear1);

        SystemInfo.initGroupShopComponents(cmbTarget1, 3);
        cmbStaff1.addItem(new MstStaff());
        SystemInfo.initStaffComponent(cmbStaff1);
        cmbStaff1.setSelectedIndex(0);

        //売上構成やその推移コントロールを初期設定する

        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        SystemInfo.initGroupShopComponents(cmbTarget2, 3);
        cmbStaff2.addItem(new MstStaff());
        SystemInfo.initStaffComponent(cmbStaff2);
        cmbStaff2.setSelectedIndex(0);
        nowYear = cal.get(Calendar.YEAR);
        nowMonth = cal.get(Calendar.MONTH);
        initYearCombo(cmbMonthWayYear, nowYear);
        this.cmbMonthWayMonth.setSelectedIndex(nowMonth);
        initYearCombo(cmbTargetPeriodYearStart2, nowYear);
        this.cmbTargetPeriodMonthStart2.setSelectedIndex(nowMonth);
        initYearCombo(cmbTargetPeriodYearEnd1, nowYear);
        this.cmbTargetPeriodMonthEnd1.setSelectedIndex(nowMonth);
        this.cmbTimePeriodStart.setDate(cal.getTime());
        this.cmbTimePeriodEnd.setDate(cal.getTime());

        //リピートや来店サイクルコントロールを初期設定する
        SystemInfo.initGroupShopComponents(cmbTarget3, 3);
        cmbStaff3.addItem(new MstStaff());
        SystemInfo.initStaffComponent(cmbStaff3);
        cmbStaff3.setSelectedIndex(0);
        initYearCombo(cmbTargetYear, nowYear);
        this.cmbTargetyMonth.setSelectedIndex(nowMonth);
        initYearCombo(cmbTargetPeriodYearStart3, nowYear);
        this.cmbTargetPeriodMonthStart3.setSelectedIndex(nowMonth);
        
        initYearCombo(cmbTargetPeriodYearStart4, nowYear);
        this.cmbTargetPeriodMonthStart4.setSelectedIndex(nowMonth);
        //宣伝や広告の効果分析コントロールを初期設定する

        this.cmbTargetPeriodStart4.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        this.cmbTargetPeriodEnd4.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        SystemInfo.initGroupShopComponents(cmbTarget4, 3);
        // start add 20130805 nakhoa 役務機能使用有無の制御追加
        rdoCourseReport.setVisible(false);
        rdoConsumptionCourseReport.setVisible(false);
        if(cmbTarget1.getSelectedItem() != null){
            if(cmbTarget1.getSelectedItem() instanceof MstShop){
                MstShop shop = new MstShop();
                shop = (MstShop)cmbTarget1.getSelectedItem();
                 //IVS NNTUAN START EDIT 20131028
               /* if(shop.getCourseFlag() == 1){
                    rdoCourseReport.setVisible(true);
                    rdoConsumptionCourseReport.setVisible(true);
                }*/
                if(shop.getShopID() == 0 || shop.getCourseFlag() == 1){
                    rdoCourseReport.setVisible(true);
                    rdoConsumptionCourseReport.setVisible(true);
                }
                 //IVS NNTUAN END EDIT 20131028
            }
        }
        isLoading = false;
        // end add 20130805 nakhoa 役務機能使用有無の制御追加
        
        // vtbphuong start add 20140723
         if(SystemInfo.getAccountSetting().getReportPriceType() == 0)
	    {
		    rdoTaxBlank1.setSelected(false);
                    rdoTaxBlank2.setSelected(false);
		    rdoTaxUnit1.setSelected(true);
                    rdoTaxUnit2.setSelected(true);
	    }
	    else
	    {
		    rdoTaxBlank1.setSelected(true);
                    rdoTaxBlank2.setSelected(true);
		    rdoTaxUnit1.setSelected(false);
                    rdoTaxUnit2.setSelected(false);
	    }
          // vtbphuong end add 20140723
    }

    /**
     * int data for year combobx
     *
     * @param cmb
     * @param nowYear
     */
    private void initYearCombo(final JComboBox cmb, int nowYear) {
        cmb.removeAllItems();

        int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            cmb.addItem(String.valueOf(y - i));
        }
        cmb.setSelectedItem(nowYear);
        cmb.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ((JTextField) cmb.getEditor().getEditorComponent()).selectAll();
            }
        });
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
        typeGroup = new javax.swing.ButtonGroup();
        aggregationGroup = new javax.swing.ButtonGroup();
        timeGroup = new javax.swing.ButtonGroup();
        outputGroup = new javax.swing.ButtonGroup();
        taxGroup2 = new javax.swing.ButtonGroup();
        aggregateGroup = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        buttonGroup8 = new javax.swing.ButtonGroup();
        btnPerformance = new javax.swing.ButtonGroup();
        pnlDetailSale = new javax.swing.JPanel();
        rdoBusinessReport = new javax.swing.JRadioButton();
        rdoTechnicalReport = new javax.swing.JRadioButton();
        rdoItemReport = new javax.swing.JRadioButton();
        lblTarget1 = new javax.swing.JLabel();
        cmbTarget1 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        cmbPeriodStart1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblSpe1 = new javax.swing.JLabel();
        cmbPeriodEnd1 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblPeriod1 = new javax.swing.JLabel();
        rdoTechStaff = new javax.swing.JRadioButton();
        rdoMainStaff = new javax.swing.JRadioButton();
        lblStaffType = new javax.swing.JLabel();
        lblStaff1 = new javax.swing.JLabel();
        cmbStaff1 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        rdoNewVisitCurrent = new javax.swing.JRadioButton();
        lblNewVisit = new javax.swing.JLabel();
        rdoNewVisitAll = new javax.swing.JRadioButton();
        rdoTaxBlank1 = new javax.swing.JRadioButton();
        rdoTaxUnit1 = new javax.swing.JRadioButton();
        lblTax1 = new javax.swing.JLabel();
        btnExcelReport1 = new javax.swing.JButton();
        rdoCourseReport = new javax.swing.JRadioButton();
        rdoConsumptionCourseReport = new javax.swing.JRadioButton();
        pnlTypeSale = new javax.swing.JPanel();
        rdoDayShift = new javax.swing.JRadioButton();
        rdoMonthShift = new javax.swing.JRadioButton();
        rdoTimeAnalysis = new javax.swing.JRadioButton();
        lblTarget2 = new javax.swing.JLabel();
        cmbDayWayStartDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblSpec2 = new javax.swing.JLabel();
        cmbDayWayEndDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblDayWay = new javax.swing.JLabel();
        rdoTaxBlank2 = new javax.swing.JRadioButton();
        rdoTaxUnit2 = new javax.swing.JRadioButton();
        lblTax2 = new javax.swing.JLabel();
        rdoVisitTime = new javax.swing.JRadioButton();
        lblSumMethod = new javax.swing.JLabel();
        rdoStartTime = new javax.swing.JRadioButton();
        rdoTimeCustom = new javax.swing.JRadioButton();
        rdoTimeFixed = new javax.swing.JRadioButton();
        lblTimeCond = new javax.swing.JLabel();
        rdoTypeShift = new javax.swing.JRadioButton();
        rdoSaleShift = new javax.swing.JRadioButton();
        rdoZChart = new javax.swing.JRadioButton();
        cmbStaff2 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblStaff2 = new javax.swing.JLabel();
        rdoDayWay = new javax.swing.JRadioButton();
        rdoMonthWay = new javax.swing.JRadioButton();
        cmbMonthWayMonth = new javax.swing.JComboBox();
        cmbMonthWayYear = new javax.swing.JComboBox();
        lblMonthWay = new javax.swing.JLabel();
        lblMonthWayYear = new javax.swing.JLabel();
        lblMonthWayMonth = new javax.swing.JLabel();
        cmbTargetPeriodYearStart1 = new javax.swing.JComboBox();
        lblPeriodYearStart = new javax.swing.JLabel();
        cmbTargetPeriodMonthStart1 = new javax.swing.JComboBox();
        lblPeriodMonthStart = new javax.swing.JLabel();
        cmbTargetPeriodYearEnd1 = new javax.swing.JComboBox();
        lblPeriodYearEnd = new javax.swing.JLabel();
        cmbTargetPeriodMonthEnd1 = new javax.swing.JComboBox();
        lblPeriodMonthEnd = new javax.swing.JLabel();
        lblSpe3 = new javax.swing.JLabel();
        lblTargetPeriod2 = new javax.swing.JLabel();
        rdoDay = new javax.swing.JRadioButton();
        rdoType = new javax.swing.JRadioButton();
        rdoWeek = new javax.swing.JRadioButton();
        lblSum = new javax.swing.JLabel();
        lblTimePeriod = new javax.swing.JLabel();
        cmbTimePeriodStart = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lbldis = new javax.swing.JLabel();
        cmbTimePeriodEnd = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        techItemClassLabel = new javax.swing.JLabel();
        rdoClassAll = new javax.swing.JRadioButton();
        rdoClassTech = new javax.swing.JRadioButton();
        rdoClassItem = new javax.swing.JRadioButton();
        rdoLeaveTime = new javax.swing.JRadioButton();
        lblOuptCond = new javax.swing.JLabel();
        rdoMoney = new javax.swing.JRadioButton();
        rdoPersonCount = new javax.swing.JRadioButton();
        lblPeriodYearEnd1 = new javax.swing.JLabel();
        lblPeriodMonthEnd1 = new javax.swing.JLabel();
        lblPeriodYearEnd2 = new javax.swing.JLabel();
        lblPeriodMonthEnd2 = new javax.swing.JLabel();
        lblSpe7 = new javax.swing.JLabel();
        lblTargetPeriod3 = new javax.swing.JLabel();
        cmbTargetPeriodYearStart2 = new javax.swing.JComboBox();
        cmbTargetPeriodMonthStart2 = new javax.swing.JComboBox();
        lblPeriodYearStart1 = new javax.swing.JLabel();
        lblPeriodMonthStart1 = new javax.swing.JLabel();
        pnlTypeInfluence1 = new javax.swing.JPanel();
        btnExcelReport2 = new javax.swing.JButton();
        btnPurposeSetting = new javax.swing.JButton();
        cmbTarget2 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        pnlTypeRepeat = new javax.swing.JPanel();
        rdoReappear = new javax.swing.JRadioButton();
        lblTarget3 = new javax.swing.JLabel();
        cmbTarget3 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        rdoTotalCount = new javax.swing.JRadioButton();
        rdoSimpleCount = new javax.swing.JRadioButton();
        rdoTechOtherCount = new javax.swing.JRadioButton();
        rdoTechSameCount = new javax.swing.JRadioButton();
        cmbStaff3 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblStaff3 = new javax.swing.JLabel();
        cmbTargetyMonth = new javax.swing.JComboBox();
        lblTargetYear = new javax.swing.JLabel();
        lblTargetMonth = new javax.swing.JLabel();
        cmbTargetYear = new javax.swing.JComboBox();
        lblTargetPeriod4 = new javax.swing.JLabel();
        lblTargetDate = new javax.swing.JLabel();
        lblFixedCount1 = new javax.swing.JLabel();
        lblMinFixedCount1 = new javax.swing.JLabel();
        txtFixedCount1 = new javax.swing.JTextField();
        ((PlainDocument)txtFixedCount1.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        lblCountCondition = new javax.swing.JLabel();
        lblTechType = new javax.swing.JLabel();
        lblTargetPeriod5 = new javax.swing.JLabel();
        lblTargetPeriod1 = new javax.swing.JLabel();
        cmbReappearanceSpan = new javax.swing.JComboBox();
        btnPeriodSetting = new javax.swing.JButton();
        btnExcelReport3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTargetPeriod7 = new javax.swing.JLabel();
        cmbTargetPeriodYearStart4 = new javax.swing.JComboBox();
        lblPeriodYearStart4 = new javax.swing.JLabel();
        cmbTargetPeriodMonthStart4 = new javax.swing.JComboBox();
        lblPeriodMonthStart4 = new javax.swing.JLabel();
        lblSpe5 = new javax.swing.JLabel();
        lable104 = new javax.swing.JLabel();
        lblPeriodYearEnd4 = new javax.swing.JLabel();
        lblPeriodMonthEnd4 = new javax.swing.JLabel();
        rdoActive = new javax.swing.JRadioButton();
        lblTargetPeriod6 = new javax.swing.JLabel();
        cmbTargetPeriodYearStart3 = new javax.swing.JComboBox();
        lblPeriodYearStart3 = new javax.swing.JLabel();
        cmbTargetPeriodMonthStart3 = new javax.swing.JComboBox();
        lblPeriodMonthStart3 = new javax.swing.JLabel();
        lblSpe4 = new javax.swing.JLabel();
        lblPeriodYearEnd3 = new javax.swing.JLabel();
        label100 = new javax.swing.JLabel();
        lblPeriodMonthEnd3 = new javax.swing.JLabel();
        lable101 = new javax.swing.JLabel();
        rdoReappearance = new javax.swing.JRadioButton();
        label101 = new javax.swing.JLabel();
        pnlTypeInfluence = new javax.swing.JPanel();
        btnExcelReport4 = new javax.swing.JButton();
        cmbTarget4 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblTarget8 = new javax.swing.JLabel();
        lblTargetPeriod8 = new javax.swing.JLabel();
        lblFixedCount2 = new javax.swing.JLabel();
        lblMinFixedCount2 = new javax.swing.JLabel();
        txtFixedCount2 = new javax.swing.JTextField();
        ((PlainDocument)txtFixedCount2.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        lblTitle = new javax.swing.JLabel();
        cmbTargetPeriodStart4 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblSpe6 = new javax.swing.JLabel();
        cmbTargetPeriodEnd4 = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblReportName = new javax.swing.JLabel();
        btnAnlysis = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setFocusCycleRoot(true);
        setPreferredSize(new java.awt.Dimension(900, 703));

        pnlDetailSale.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "期間で絞って詳細な売上を見たい！", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS UI Gothic", 1, 12))); // NOI18N
        pnlDetailSale.setOpaque(false);

        buttonGroup6.add(rdoBusinessReport);
        rdoBusinessReport.setSelected(true);
        rdoBusinessReport.setText("業務報告");
        rdoBusinessReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoBusinessReport.setFocusCycleRoot(true);
        rdoBusinessReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoBusinessReport.setOpaque(false);
        rdoBusinessReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoBusinessReportMouseExited(evt);
            }
        });
        rdoBusinessReport.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoBusinessReportStateChanged(evt);
            }
        });
        rdoBusinessReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBusinessReportActionPerformed(evt);
            }
        });

        buttonGroup6.add(rdoTechnicalReport);
        rdoTechnicalReport.setText("技術詳細");
        rdoTechnicalReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechnicalReport.setFocusCycleRoot(true);
        rdoTechnicalReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTechnicalReport.setOpaque(false);
        rdoTechnicalReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoTechnicalReportMouseExited(evt);
            }
        });
        rdoTechnicalReport.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoTechnicalReportStateChanged(evt);
            }
        });
        rdoTechnicalReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTechnicalReportActionPerformed(evt);
            }
        });

        buttonGroup6.add(rdoItemReport);
        rdoItemReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoItemReport.setFocusCycleRoot(true);
        rdoItemReport.setLabel("商品詳細");
        rdoItemReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoItemReport.setOpaque(false);
        rdoItemReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoItemReportMouseExited(evt);
            }
        });
        rdoItemReport.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoItemReportStateChanged(evt);
            }
        });
        rdoItemReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoItemReportActionPerformed(evt);
            }
        });

        lblTarget1.setText("対象");

        //shop.addItem(this.myShop);
        cmbTarget1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTarget1ItemStateChanged(evt);
            }
        });
        cmbTarget1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTarget1ActionPerformed(evt);
            }
        });

        cmbPeriodStart1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPeriodStart1.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodStart1.setDate(new java.util.Date());

        lblSpe1.setText("～");
        lblSpe1.setFocusCycleRoot(true);

        cmbPeriodEnd1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPeriodEnd1.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodEnd1.setDate(new java.util.Date());

        lblPeriod1.setText("対象期間");
        lblPeriod1.setFocusCycleRoot(true);

        buttonGroup5.add(rdoTechStaff);
        rdoTechStaff.setText("施術担当");
        rdoTechStaff.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechStaff.setContentAreaFilled(false);
        rdoTechStaff.setEnabled(false);
        rdoTechStaff.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup5.add(rdoMainStaff);
        rdoMainStaff.setSelected(true);
        rdoMainStaff.setText("主担当");
        rdoMainStaff.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMainStaff.setContentAreaFilled(false);
        rdoMainStaff.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblStaffType.setText("担当区分");

        lblStaff1.setText("担当者");

        //shop.addItem(this.myShop);

        buttonGroup4.add(rdoNewVisitCurrent);
        rdoNewVisitCurrent.setSelected(true);
        rdoNewVisitCurrent.setText("自店新規");
        rdoNewVisitCurrent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNewVisitCurrent.setContentAreaFilled(false);
        rdoNewVisitCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblNewVisit.setText("新規区分");

        buttonGroup4.add(rdoNewVisitAll);
        rdoNewVisitAll.setText("全店新規");
        rdoNewVisitAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNewVisitAll.setContentAreaFilled(false);
        rdoNewVisitAll.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup3.add(rdoTaxBlank1);
        rdoTaxBlank1.setSelected(true);
        rdoTaxBlank1.setText("税抜");
        rdoTaxBlank1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank1.setContentAreaFilled(false);
        rdoTaxBlank1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup3.add(rdoTaxUnit1);
        rdoTaxUnit1.setText("税込");
        rdoTaxUnit1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit1.setContentAreaFilled(false);
        rdoTaxUnit1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblTax1.setText("税区分");

        btnExcelReport1.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcelReport1.setBorderPainted(false);
        btnExcelReport1.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcelReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelReport1ActionPerformed(evt);
            }
        });

        buttonGroup6.add(rdoCourseReport);
        rdoCourseReport.setText("コース詳細");
        rdoCourseReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoCourseReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoCourseReport.setOpaque(false);
        rdoCourseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCourseReportActionPerformed(evt);
            }
        });

        buttonGroup6.add(rdoConsumptionCourseReport);
        rdoConsumptionCourseReport.setText("消化一覧");
        rdoConsumptionCourseReport.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoConsumptionCourseReport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoConsumptionCourseReport.setOpaque(false);
        rdoConsumptionCourseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoConsumptionCourseReportActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnlDetailSaleLayout = new org.jdesktop.layout.GroupLayout(pnlDetailSale);
        pnlDetailSale.setLayout(pnlDetailSaleLayout);
        pnlDetailSaleLayout.setHorizontalGroup(
            pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlDetailSaleLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlDetailSaleLayout.createSequentialGroup()
                        .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lblTarget1)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lblStaffType)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lblPeriod1))
                        .add(41, 41, 41)
                        .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cmbTarget1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(pnlDetailSaleLayout.createSequentialGroup()
                                .add(rdoNewVisitCurrent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(26, 26, 26)
                                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rdoTaxBlank1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(rdoNewVisitAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                    .add(lblNewVisit)
                    .add(pnlDetailSaleLayout.createSequentialGroup()
                        .add(lblTax1)
                        .add(52, 52, 52)
                        .add(rdoTaxUnit1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(pnlDetailSaleLayout.createSequentialGroup()
                        .add(rdoBusinessReport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rdoTechnicalReport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rdoItemReport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rdoCourseReport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rdoConsumptionCourseReport))
                    .add(pnlDetailSaleLayout.createSequentialGroup()
                        .add(lblStaff1)
                        .add(52, 52, 52)
                        .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlDetailSaleLayout.createSequentialGroup()
                                .add(rdoMainStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(26, 26, 26)
                                .add(rdoTechStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(cmbStaff1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(pnlDetailSaleLayout.createSequentialGroup()
                                .add(cmbPeriodStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblSpe1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmbPeriodEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlDetailSaleLayout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(btnExcelReport1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        pnlDetailSaleLayout.setVerticalGroup(
            pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlDetailSaleLayout.createSequentialGroup()
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoBusinessReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTechnicalReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoItemReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoCourseReport)
                    .add(rdoConsumptionCourseReport))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTarget1)
                    .add(cmbTarget1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblSpe1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbPeriodStart1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbPeriodEnd1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblPeriod1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoMainStaff)
                    .add(rdoTechStaff)
                    .add(lblStaffType))
                .add(4, 4, 4)
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblStaff1)
                    .add(cmbStaff1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoNewVisitCurrent)
                    .add(rdoNewVisitAll)
                    .add(lblNewVisit))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlDetailSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoTaxUnit1)
                    .add(rdoTaxBlank1)
                    .add(lblTax1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnExcelReport1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pnlTypeSale.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "売上構成やその推移を見たい！", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS UI Gothic", 1, 12))); // NOI18N
        pnlTypeSale.setOpaque(false);

        reportTypeGroup.add(rdoDayShift);
        rdoDayShift.setSelected(true);
        rdoDayShift.setText(" 日次推移");
        rdoDayShift.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDayShift.setFocusCycleRoot(true);
        rdoDayShift.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoDayShift.setOpaque(false);
        rdoDayShift.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoDayShiftMouseExited(evt);
            }
        });
        rdoDayShift.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoDayShiftStateChanged(evt);
            }
        });

        reportTypeGroup.add(rdoMonthShift);
        rdoMonthShift.setText("月次推移");
        rdoMonthShift.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMonthShift.setFocusCycleRoot(true);
        rdoMonthShift.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoMonthShift.setOpaque(false);
        rdoMonthShift.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoMonthShiftMouseExited(evt);
            }
        });
        rdoMonthShift.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoMonthShiftStateChanged(evt);
            }
        });

        reportTypeGroup.add(rdoTimeAnalysis);
        rdoTimeAnalysis.setText("時間帯分析");
        rdoTimeAnalysis.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTimeAnalysis.setFocusCycleRoot(true);
        rdoTimeAnalysis.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTimeAnalysis.setOpaque(false);
        rdoTimeAnalysis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoTimeAnalysisMouseExited(evt);
            }
        });
        rdoTimeAnalysis.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoTimeAnalysisStateChanged(evt);
            }
        });

        lblTarget2.setText("対象");

        cmbDayWayStartDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbDayWayStartDate.setEnabled(false);
        cmbDayWayStartDate.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodStart1.setDate(new java.util.Date());

        lblSpec2.setText("～");
        lblSpec2.setFocusCycleRoot(true);

        cmbDayWayEndDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbDayWayEndDate.setEnabled(false);
        cmbDayWayEndDate.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodEnd1.setDate(new java.util.Date());

        lblDayWay.setText("日数方式");
        lblDayWay.setEnabled(false);
        lblDayWay.setFocusCycleRoot(true);

        taxGroup2.add(rdoTaxBlank2);
        rdoTaxBlank2.setSelected(true);
        rdoTaxBlank2.setText("税抜");
        rdoTaxBlank2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank2.setContentAreaFilled(false);
        rdoTaxBlank2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        taxGroup2.add(rdoTaxUnit2);
        rdoTaxUnit2.setText("税込");
        rdoTaxUnit2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit2.setContentAreaFilled(false);
        rdoTaxUnit2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblTax2.setText("税区分");

        aggregationGroup.add(rdoVisitTime);
        rdoVisitTime.setSelected(true);
        rdoVisitTime.setText("受付時間");
        rdoVisitTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoVisitTime.setContentAreaFilled(false);
        rdoVisitTime.setEnabled(false);
        rdoVisitTime.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblSumMethod.setText("集計方法");
        lblSumMethod.setEnabled(false);

        aggregationGroup.add(rdoStartTime);
        rdoStartTime.setText("施術開始時間");
        rdoStartTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoStartTime.setContentAreaFilled(false);
        rdoStartTime.setEnabled(false);
        rdoStartTime.setMargin(new java.awt.Insets(0, 0, 0, 0));

        timeGroup.add(rdoTimeCustom);
        rdoTimeCustom.setText("個別設定");
        rdoTimeCustom.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTimeCustom.setContentAreaFilled(false);
        rdoTimeCustom.setEnabled(false);
        rdoTimeCustom.setMargin(new java.awt.Insets(0, 0, 0, 0));

        timeGroup.add(rdoTimeFixed);
        rdoTimeFixed.setSelected(true);
        rdoTimeFixed.setText("1時間");
        rdoTimeFixed.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTimeFixed.setContentAreaFilled(false);
        rdoTimeFixed.setEnabled(false);
        rdoTimeFixed.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblTimeCond.setText("時間条件");
        lblTimeCond.setEnabled(false);

        reportTypeGroup.add(rdoTypeShift);
        rdoTypeShift.setText("分類別売上推移(G)");
        rdoTypeShift.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTypeShift.setFocusCycleRoot(true);
        rdoTypeShift.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTypeShift.setOpaque(false);
        rdoTypeShift.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoTypeShiftMouseExited(evt);
            }
        });
        rdoTypeShift.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoTypeShiftStateChanged(evt);
            }
        });

        reportTypeGroup.add(rdoSaleShift);
        rdoSaleShift.setText(" 売上別推移(G)");
        rdoSaleShift.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSaleShift.setFocusCycleRoot(true);
        rdoSaleShift.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSaleShift.setOpaque(false);
        rdoSaleShift.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoSaleShiftMouseExited(evt);
            }
        });
        rdoSaleShift.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoSaleShiftStateChanged(evt);
            }
        });

        reportTypeGroup.add(rdoZChart);
        rdoZChart.setText("Zチャート(G)");
        rdoZChart.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoZChart.setFocusCycleRoot(true);
        rdoZChart.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoZChart.setOpaque(false);
        rdoZChart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoZChartMouseExited(evt);
            }
        });
        rdoZChart.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoZChartStateChanged(evt);
            }
        });

        cmbStaff2.setEnabled(false);
        //shop.addItem(this.myShop);

        lblStaff2.setText("主当者");
        lblStaff2.setEnabled(false);

        buttonGroup1.add(rdoDayWay);
        rdoDayWay.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDayWay.setEnabled(false);
        rdoDayWay.setFocusCycleRoot(true);
        rdoDayWay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoDayWay.setOpaque(false);
        rdoDayWay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoDayWayItemStateChanged(evt);
            }
        });
        rdoDayWay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDayWayActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoMonthWay);
        rdoMonthWay.setSelected(true);
        rdoMonthWay.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMonthWay.setEnabled(false);
        rdoMonthWay.setFocusCycleRoot(true);
        rdoMonthWay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoMonthWay.setOpaque(false);
        rdoMonthWay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoMonthWayItemStateChanged(evt);
            }
        });
        rdoMonthWay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMonthWayActionPerformed(evt);
            }
        });

        cmbMonthWayMonth.setMaximumRowCount(12);
        cmbMonthWayMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbMonthWayMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbMonthWayMonth.setEnabled(false);
        cmbMonthWayMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonthWayMonthItemStateChanged(evt);
            }
        });

        cmbMonthWayYear.setMaximumRowCount(12);
        cmbMonthWayYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbMonthWayYear.setEnabled(false);
        cmbMonthWayYear.setPreferredSize(new java.awt.Dimension(48, 20));
        cmbMonthWayYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonthWayYearItemStateChanged(evt);
            }
        });

        lblMonthWay.setText("月方針");
        lblMonthWay.setEnabled(false);
        lblMonthWay.setFocusCycleRoot(true);

        lblMonthWayYear.setText("年");
        lblMonthWayYear.setFocusCycleRoot(true);

        lblMonthWayMonth.setText("月");
        lblMonthWayMonth.setFocusCycleRoot(true);

        cmbTargetPeriodYearStart1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodYearStart1.setEnabled(false);
        cmbTargetPeriodYearStart1.setPreferredSize(new java.awt.Dimension(48, 20));

        lblPeriodYearStart.setText("年");
        lblPeriodYearStart.setFocusCycleRoot(true);

        cmbTargetPeriodMonthStart1.setMaximumRowCount(12);
        cmbTargetPeriodMonthStart1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbTargetPeriodMonthStart1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodMonthStart1.setEnabled(false);
        cmbTargetPeriodMonthStart1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodMonthStart1ItemStateChanged(evt);
            }
        });

        lblPeriodMonthStart.setText("月");
        lblPeriodMonthStart.setFocusCycleRoot(true);

        cmbTargetPeriodYearEnd1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodYearEnd1.setEnabled(false);
        cmbTargetPeriodYearEnd1.setPreferredSize(new java.awt.Dimension(48, 20));

        lblPeriodYearEnd.setText("年");
        lblPeriodYearEnd.setFocusCycleRoot(true);

        cmbTargetPeriodMonthEnd1.setMaximumRowCount(12);
        cmbTargetPeriodMonthEnd1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbTargetPeriodMonthEnd1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodMonthEnd1.setEnabled(false);

        lblPeriodMonthEnd.setText("月");
        lblPeriodMonthEnd.setFocusCycleRoot(true);

        lblSpe3.setText("～");
        lblSpe3.setFocusCycleRoot(true);

        lblTargetPeriod2.setText("対象期間");
        lblTargetPeriod2.setEnabled(false);

        aggregateGroup.add(rdoDay);
        rdoDay.setSelected(true);
        rdoDay.setText("日別集計");
        rdoDay.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDay.setContentAreaFilled(false);
        rdoDay.setEnabled(false);
        rdoDay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoDay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoDayStateChanged(evt);
            }
        });

        aggregateGroup.add(rdoType);
        rdoType.setText("分類別集計");
        rdoType.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoType.setContentAreaFilled(false);
        rdoType.setEnabled(false);
        rdoType.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoType.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoTypeStateChanged(evt);
            }
        });

        aggregateGroup.add(rdoWeek);
        rdoWeek.setText("曜日別集計");
        rdoWeek.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoWeek.setContentAreaFilled(false);
        rdoWeek.setEnabled(false);
        rdoWeek.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoWeek.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoWeekStateChanged(evt);
            }
        });

        lblSum.setText("集計方針");
        lblSum.setEnabled(false);

        lblTimePeriod.setText("対象期間");
        lblTimePeriod.setEnabled(false);
        lblTimePeriod.setFocusCycleRoot(true);

        cmbTimePeriodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTimePeriodStart.setEnabled(false);
        cmbTimePeriodStart.setFocusCycleRoot(true);
        cmbTimePeriodStart.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodStart1.setDate(new java.util.Date());
        cmbTimePeriodStart.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTimePeriodStartItemStateChanged(evt);
            }
        });

        lbldis.setText("～");
        lbldis.setFocusCycleRoot(true);

        cmbTimePeriodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTimePeriodEnd.setEnabled(false);
        cmbTimePeriodEnd.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodEnd1.setDate(new java.util.Date());

        techItemClassLabel.setText("分類種別");
        techItemClassLabel.setEnabled(false);

        typeGroup.add(rdoClassAll);
        rdoClassAll.setSelected(true);
        rdoClassAll.setText("すべて");
        rdoClassAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoClassAll.setContentAreaFilled(false);
        rdoClassAll.setEnabled(false);
        rdoClassAll.setMargin(new java.awt.Insets(0, 0, 0, 0));

        typeGroup.add(rdoClassTech);
        rdoClassTech.setText("技術分類");
        rdoClassTech.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoClassTech.setContentAreaFilled(false);
        rdoClassTech.setEnabled(false);
        rdoClassTech.setMargin(new java.awt.Insets(0, 0, 0, 0));

        typeGroup.add(rdoClassItem);
        rdoClassItem.setText("商品分類");
        rdoClassItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoClassItem.setContentAreaFilled(false);
        rdoClassItem.setEnabled(false);
        rdoClassItem.setMargin(new java.awt.Insets(0, 0, 0, 0));

        aggregationGroup.add(rdoLeaveTime);
        rdoLeaveTime.setText("精算時間");
        rdoLeaveTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoLeaveTime.setContentAreaFilled(false);
        rdoLeaveTime.setEnabled(false);
        rdoLeaveTime.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblOuptCond.setText("出力条件");
        lblOuptCond.setEnabled(false);

        outputGroup.add(rdoMoney);
        rdoMoney.setSelected(true);
        rdoMoney.setText("金額");
        rdoMoney.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMoney.setContentAreaFilled(false);
        rdoMoney.setEnabled(false);
        rdoMoney.setMargin(new java.awt.Insets(0, 0, 0, 0));

        outputGroup.add(rdoPersonCount);
        rdoPersonCount.setText("人数");
        rdoPersonCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoPersonCount.setContentAreaFilled(false);
        rdoPersonCount.setEnabled(false);
        rdoPersonCount.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblPeriodYearEnd1.setText("年");
        lblPeriodYearEnd1.setFocusCycleRoot(true);

        lblPeriodMonthEnd1.setText("月");
        lblPeriodMonthEnd1.setFocusCycleRoot(true);

        lblPeriodYearEnd2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPeriodYearEnd2.setText(" ");
        lblPeriodYearEnd2.setFocusCycleRoot(true);

        lblPeriodMonthEnd2.setText("aAA");
        lblPeriodMonthEnd2.setFocusCycleRoot(true);

        lblSpe7.setText("～");
        lblSpe7.setFocusCycleRoot(true);

        lblTargetPeriod3.setText("対象期間");

        cmbTargetPeriodYearStart2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodYearStart2.setPreferredSize(new java.awt.Dimension(48, 20));
        cmbTargetPeriodYearStart2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodYearStart2ItemStateChanged(evt);
            }
        });

        cmbTargetPeriodMonthStart2.setMaximumRowCount(12);
        cmbTargetPeriodMonthStart2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbTargetPeriodMonthStart2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodMonthStart2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodMonthStart2ItemStateChanged(evt);
            }
        });

        lblPeriodYearStart1.setText("年");
        lblPeriodYearStart1.setFocusCycleRoot(true);

        lblPeriodMonthStart1.setText("月");
        lblPeriodMonthStart1.setFocusCycleRoot(true);

        pnlTypeInfluence1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlTypeInfluence1.setOpaque(false);
        pnlTypeInfluence1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlTypeInfluence1MouseExited(evt);
            }
        });
        pnlTypeInfluence1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pnlTypeInfluence1MouseMoved(evt);
            }
        });

        btnExcelReport2.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcelReport2.setBorderPainted(false);
        btnExcelReport2.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcelReport2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelReport2ActionPerformed(evt);
            }
        });

        btnPurposeSetting.setIcon(SystemInfo.getImageIcon("/button/master/target_off.jpg"));
        btnPurposeSetting.setBorderPainted(false);
        btnPurposeSetting.setMaximumSize(new java.awt.Dimension(30, 9));
        btnPurposeSetting.setMinimumSize(new java.awt.Dimension(30, 9));
        btnPurposeSetting.setPreferredSize(new java.awt.Dimension(30, 9));
        btnPurposeSetting.setPressedIcon(SystemInfo.getImageIcon("/button/master/target_on.jpg"));
        btnPurposeSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurposeSettingActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnlTypeInfluence1Layout = new org.jdesktop.layout.GroupLayout(pnlTypeInfluence1);
        pnlTypeInfluence1.setLayout(pnlTypeInfluence1Layout);
        pnlTypeInfluence1Layout.setHorizontalGroup(
            pnlTypeInfluence1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeInfluence1Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(btnPurposeSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnExcelReport2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
        );
        pnlTypeInfluence1Layout.setVerticalGroup(
            pnlTypeInfluence1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
            .add(pnlTypeInfluence1Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(pnlTypeInfluence1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnExcelReport2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnPurposeSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        //shop.addItem(this.myShop);

        org.jdesktop.layout.GroupLayout pnlTypeSaleLayout = new org.jdesktop.layout.GroupLayout(pnlTypeSale);
        pnlTypeSale.setLayout(pnlTypeSaleLayout);
        pnlTypeSaleLayout.setHorizontalGroup(
            pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTypeSaleLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlTypeSaleLayout.createSequentialGroup()
                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblDayWay)
                                    .add(lblMonthWay))
                                .add(18, 18, 18)
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(rdoMonthWay)
                                    .add(rdoDayWay)))
                            .add(lblTargetPeriod3)
                            .add(lblTargetPeriod2)
                            .add(lblOuptCond))
                        .add(7, 7, 7)
                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                        .add(295, 295, 295)
                                        .add(lblPeriodMonthEnd1))
                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(rdoDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(rdoTaxUnit2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(36, 36, 36)
                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(rdoType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(18, 18, 18)
                                                .add(rdoWeek, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(rdoTaxBlank2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeSaleLayout.createSequentialGroup()
                                                .add(rdoMoney, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(27, 27, 27))
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(cmbTimePeriodStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(rdoVisitTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(10, 10, 10)))
                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(lbldis)
                                                .add(17, 17, 17)
                                                .add(cmbTimePeriodEnd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(8, 8, 8)
                                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(rdoTimeCustom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, rdoPersonCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(rdoClassTech, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                            .add(rdoStartTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(rdoLeaveTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                            .add(rdoClassItem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                                        .add(cmbTargetPeriodYearStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                        .add(lblPeriodYearStart))
                                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                                        .add(cmbMonthWayYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                        .add(lblMonthWayYear)))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeSaleLayout.createSequentialGroup()
                                                .add(cmbTargetPeriodYearStart2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(lblPeriodYearStart1)
                                                .add(4, 4, 4)))
                                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(cmbMonthWayMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(lblMonthWayMonth))
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(cmbTargetPeriodMonthStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(lblPeriodMonthStart)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lblSpe3)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(cmbTargetPeriodYearEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(lblPeriodYearEnd)
                                                .add(8, 8, 8)
                                                .add(cmbTargetPeriodMonthEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lblPeriodMonthEnd))
                                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                                .add(cmbTargetPeriodMonthStart2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(lblPeriodMonthStart1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lblSpe7)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lblPeriodYearEnd2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(lblPeriodYearEnd1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lblPeriodMonthEnd2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                    .add(pnlTypeSaleLayout.createSequentialGroup()
                                        .add(cmbDayWayStartDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(lblSpec2)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(cmbDayWayEndDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(cmbTarget2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(cmbStaff2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 201, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(0, 0, Short.MAX_VALUE))))
                    .add(pnlTypeSaleLayout.createSequentialGroup()
                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblTax2)
                            .add(lblSum)
                            .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(pnlTypeSaleLayout.createSequentialGroup()
                                    .add(lblTimeCond, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(rdoTimeFixed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblSumMethod)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, pnlTypeSaleLayout.createSequentialGroup()
                                    .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTimePeriod)
                                        .add(techItemClassLabel))
                                    .add(38, 38, 38)
                                    .add(rdoClassAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, rdoSaleShift, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlTypeSaleLayout.createSequentialGroup()
                                        .add(lblTarget2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(48, 48, 48))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, rdoDayShift, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rdoTypeShift)
                                    .add(rdoMonthShift, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(10, 10, 10)
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(rdoTimeAnalysis, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                    .add(rdoZChart, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .add(lblStaff2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeSaleLayout.createSequentialGroup()
                        .add(pnlTypeInfluence1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnlTypeSaleLayout.setVerticalGroup(
            pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTypeSaleLayout.createSequentialGroup()
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoDayShift, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoMonthShift, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTimeAnalysis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoSaleShift, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoTypeShift, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoZChart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTarget2)
                    .add(cmbTarget2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbStaff2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblStaff2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlTypeSaleLayout.createSequentialGroup()
                        .add(lblSpec2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cmbTargetPeriodYearStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPeriodYearStart)
                            .add(cmbTargetPeriodMonthStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPeriodMonthStart)
                            .add(lblSpe3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbTargetPeriodYearEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPeriodYearEnd)
                            .add(cmbTargetPeriodMonthEnd1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPeriodMonthEnd))
                        .add(8, 8, 8)
                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cmbTargetPeriodYearStart2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPeriodYearStart1)
                            .add(cmbTargetPeriodMonthStart2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPeriodMonthStart1)
                            .add(lblSpe7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblTargetPeriod3)
                            .add(lblPeriodYearEnd2)
                            .add(lblPeriodYearEnd1)
                            .add(lblPeriodMonthEnd2)
                            .add(lblPeriodMonthEnd1))
                        .add(4, 4, 4))
                    .add(pnlTypeSaleLayout.createSequentialGroup()
                        .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlTypeSaleLayout.createSequentialGroup()
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(cmbDayWayEndDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(cmbDayWayStartDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblDayWay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rdoMonthWay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(cmbMonthWayYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(cmbMonthWayMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblMonthWayYear)
                                        .add(lblMonthWayMonth))
                                    .add(lblMonthWay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(rdoDayWay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(lblTargetPeriod2)
                        .add(35, 35, 35)))
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTax2)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, rdoTaxUnit2)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, rdoTaxBlank2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblSum)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(rdoDay)
                        .add(rdoType)
                        .add(rdoWeek)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTimePeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cmbTimePeriodStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lbldis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cmbTimePeriodEnd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(techItemClassLabel)
                    .add(rdoClassAll)
                    .add(rdoClassTech)
                    .add(rdoClassItem))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSumMethod)
                    .add(rdoVisitTime)
                    .add(rdoStartTime)
                    .add(rdoLeaveTime))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTimeCond)
                    .add(rdoTimeFixed)
                    .add(rdoTimeCustom))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeSaleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rdoPersonCount)
                    .add(rdoMoney)
                    .add(lblOuptCond))
                .add(5, 5, 5)
                .add(pnlTypeInfluence1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        lblPeriodMonthEnd2.getAccessibleContext().setAccessibleName("");

        pnlTypeRepeat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "リピートや来店サイクルの分析を見たい！", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS UI Gothic", 1, 12))); // NOI18N
        pnlTypeRepeat.setOpaque(false);

        buttonGroup2.add(rdoReappear);
        rdoReappear.setSelected(true);
        rdoReappear.setText("再来分析表");
        rdoReappear.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoReappear.setFocusCycleRoot(true);
        rdoReappear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoReappear.setOpaque(false);
        rdoReappear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoReappearMouseExited(evt);
            }
        });
        rdoReappear.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoReappearStateChanged(evt);
            }
        });

        lblTarget3.setText("対象");

        //shop.addItem(this.myShop);

        buttonGroup7.add(rdoTotalCount);
        rdoTotalCount.setText("純客数");
        rdoTotalCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTotalCount.setContentAreaFilled(false);
        rdoTotalCount.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup7.add(rdoSimpleCount);
        rdoSimpleCount.setSelected(true);
        rdoSimpleCount.setText("延べ客数");
        rdoSimpleCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSimpleCount.setContentAreaFilled(false);
        rdoSimpleCount.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup8.add(rdoTechOtherCount);
        rdoTechOtherCount.setSelected(true);
        rdoTechOtherCount.setText("他分類でも再来も対象");
        rdoTechOtherCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechOtherCount.setContentAreaFilled(false);
        rdoTechOtherCount.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup8.add(rdoTechSameCount);
        rdoTechSameCount.setText("同分類の再来のみ対象");
        rdoTechSameCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTechSameCount.setContentAreaFilled(false);
        rdoTechSameCount.setMargin(new java.awt.Insets(0, 0, 0, 0));

        //shop.addItem(this.myShop);

        lblStaff3.setText("主担当者");

        cmbTargetyMonth.setMaximumRowCount(12);
        cmbTargetyMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbTargetyMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetyMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetyMonthItemStateChanged(evt);
            }
        });

        lblTargetYear.setText("年");
        lblTargetYear.setFocusCycleRoot(true);

        lblTargetMonth.setText("月");
        lblTargetMonth.setFocusCycleRoot(true);

        cmbTargetYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetYear.setPreferredSize(new java.awt.Dimension(48, 20));
        cmbTargetYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTargetYearActionPerformed(evt);
            }
        });

        lblTargetPeriod4.setText("集計期間");

        lblTargetDate.setText("yyyy/mm/dd ～ yyyy/mm/dd");
        lblTargetDate.setFocusCycleRoot(true);

        lblFixedCount1.setText("固定回数");

        lblMinFixedCount1.setText("回以上");

        txtFixedCount1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFixedCount1.setText("3");

        lblCountCondition.setText("人数条件");

        lblTechType.setText("技術分類別再来");

        lblTargetPeriod5.setText("評価期間");
        lblTargetPeriod5.setFocusCycleRoot(true);

        lblTargetPeriod1.setText("再来算出期間");

        cmbReappearanceSpan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1ヶ月", "45日", "2ヶ月", "3ヶ月", "4ヶ月", "5ヶ月", "6ヶ月" }));
        cmbReappearanceSpan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbReappearanceSpanItemStateChanged(evt);
            }
        });

        btnPeriodSetting.setIcon(SystemInfo.getImageIcon("/button/master/period_set_off.jpg"));
        btnPeriodSetting.setBorderPainted(false);
        btnPeriodSetting.setPressedIcon(SystemInfo.getImageIcon("/button/master/period_set_on.jpg"));
        btnPeriodSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPeriodSettingActionPerformed(evt);
            }
        });

        btnExcelReport3.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcelReport3.setBorderPainted(false);
        btnExcelReport3.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcelReport3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelReport3ActionPerformed(evt);
            }
        });

        lblTargetPeriod7.setText("対象期間");

        cmbTargetPeriodYearStart4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodYearStart4.setPreferredSize(new java.awt.Dimension(48, 20));
        cmbTargetPeriodYearStart4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodYearStart4ItemStateChanged(evt);
            }
        });

        lblPeriodYearStart4.setText("年");
        lblPeriodYearStart4.setFocusCycleRoot(true);

        cmbTargetPeriodMonthStart4.setMaximumRowCount(12);
        cmbTargetPeriodMonthStart4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbTargetPeriodMonthStart4.setSelectedIndex(1);
        cmbTargetPeriodMonthStart4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodMonthStart4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodMonthStart4ItemStateChanged(evt);
            }
        });

        lblPeriodMonthStart4.setText("月");
        lblPeriodMonthStart4.setFocusCycleRoot(true);

        lblSpe5.setText("～");
        lblSpe5.setFocusCycleRoot(true);

        lable104.setText("月");
        lable104.setFocusCycleRoot(true);

        lblPeriodYearEnd4.setText(" ");
        lblPeriodYearEnd4.setFocusCycleRoot(true);

        lblPeriodMonthEnd4.setText(" ");
        lblPeriodMonthEnd4.setFocusCycleRoot(true);

        buttonGroup2.add(rdoActive);
        rdoActive.setText("アクティブ顧客分析(G)");
        rdoActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoActive.setContentAreaFilled(false);
        rdoActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoActive.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoActiveMouseExited(evt);
            }
        });
        rdoActive.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoActiveStateChanged(evt);
            }
        });

        lblTargetPeriod6.setText("対象期間");

        cmbTargetPeriodYearStart3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodYearStart3.setPreferredSize(new java.awt.Dimension(48, 20));
        cmbTargetPeriodYearStart3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodYearStart3ItemStateChanged(evt);
            }
        });

        lblPeriodYearStart3.setText("年");
        lblPeriodYearStart3.setFocusCycleRoot(true);

        cmbTargetPeriodMonthStart3.setMaximumRowCount(12);
        cmbTargetPeriodMonthStart3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbTargetPeriodMonthStart3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbTargetPeriodMonthStart3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetPeriodMonthStart3ItemStateChanged(evt);
            }
        });

        lblPeriodMonthStart3.setText("月");
        lblPeriodMonthStart3.setFocusCycleRoot(true);

        lblSpe4.setText("～");
        lblSpe4.setFocusCycleRoot(true);

        lblPeriodYearEnd3.setText(" ");
        lblPeriodYearEnd3.setFocusCycleRoot(true);

        label100.setText("年");
        label100.setFocusCycleRoot(true);

        lblPeriodMonthEnd3.setText(" ");
        lblPeriodMonthEnd3.setFocusCycleRoot(true);

        lable101.setText("月");
        lable101.setFocusCycleRoot(true);

        buttonGroup2.add(rdoReappearance);
        rdoReappearance.setText("来店サイクル分析(G)");
        rdoReappearance.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoReappearance.setContentAreaFilled(false);
        rdoReappearance.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoReappearance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rdoReappearanceMouseExited(evt);
            }
        });
        rdoReappearance.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoReappearanceStateChanged(evt);
            }
        });

        label101.setText("年");
        label101.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(rdoActive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rdoReappearance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(lblTargetPeriod7)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(cmbTargetPeriodYearStart4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(lblTargetPeriod6)
                                .add(50, 50, 50)
                                .add(cmbTargetPeriodYearStart3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(lblPeriodYearStart4)
                                .add(5, 5, 5)
                                .add(cmbTargetPeriodMonthStart4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblPeriodMonthStart4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblSpe5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblPeriodYearEnd4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(label101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblPeriodMonthEnd4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(lblPeriodYearStart3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmbTargetPeriodMonthStart3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblPeriodMonthStart3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblSpe4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblPeriodYearEnd3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(label100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblPeriodMonthEnd3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lable104)
                            .add(lable101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(rdoReappearance)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(label100)
                        .add(lable101)
                        .add(lblPeriodMonthEnd3)
                        .add(lblPeriodYearEnd3))
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblTargetPeriod6)
                        .add(cmbTargetPeriodYearStart3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblPeriodYearStart3)
                        .add(cmbTargetPeriodMonthStart3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblPeriodMonthStart3)
                        .add(lblSpe4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rdoActive)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbTargetPeriodYearStart4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTargetPeriod7)
                    .add(lblPeriodYearStart4)
                    .add(cmbTargetPeriodMonthStart4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPeriodMonthStart4)
                    .add(lblSpe5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lable104)
                    .add(lblPeriodYearEnd4)
                    .add(lblPeriodMonthEnd4)
                    .add(label101))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout pnlTypeRepeatLayout = new org.jdesktop.layout.GroupLayout(pnlTypeRepeat);
        pnlTypeRepeat.setLayout(pnlTypeRepeatLayout);
        pnlTypeRepeatLayout.setHorizontalGroup(
            pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTypeRepeatLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlTypeRepeatLayout.createSequentialGroup()
                        .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(pnlTypeRepeatLayout.createSequentialGroup()
                                    .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(lblFixedCount1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(lblCountCondition)
                                        .add(lblTargetPeriod4)
                                        .add(lblTarget3))
                                    .add(43, 43, 43))
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeRepeatLayout.createSequentialGroup()
                                    .add(lblStaff3)
                                    .add(49, 49, 49)))
                            .add(pnlTypeRepeatLayout.createSequentialGroup()
                                .add(lblTargetPeriod5)
                                .add(51, 51, 51)))
                        .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlTypeRepeatLayout.createSequentialGroup()
                                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblTargetDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(cmbStaff3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(cmbTarget3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(pnlTypeRepeatLayout.createSequentialGroup()
                                        .add(txtFixedCount1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(lblMinFixedCount1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(57, 57, 57))
                            .add(pnlTypeRepeatLayout.createSequentialGroup()
                                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rdoTechOtherCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(rdoSimpleCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(3, 3, 3)
                                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rdoTotalCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(pnlTypeRepeatLayout.createSequentialGroup()
                                        .add(rdoTechSameCount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addContainerGap())))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeRepeatLayout.createSequentialGroup()
                        .add(btnPeriodSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(btnExcelReport3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(50, 50, 50))
                    .add(pnlTypeRepeatLayout.createSequentialGroup()
                        .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(rdoReappear)
                            .add(pnlTypeRepeatLayout.createSequentialGroup()
                                .add(lblTargetPeriod1)
                                .add(28, 28, 28)
                                .add(cmbReappearanceSpan, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(pnlTypeRepeatLayout.createSequentialGroup()
                                .add(100, 100, 100)
                                .add(cmbTargetYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(lblTargetYear)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(cmbTargetyMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(lblTargetMonth))
                            .add(lblTechType))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .add(pnlTypeRepeatLayout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        pnlTypeRepeatLayout.setVerticalGroup(
            pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTypeRepeatLayout.createSequentialGroup()
                .add(rdoReappear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbTarget3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTarget3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbStaff3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblStaff3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblTargetYear)
                        .add(lblTargetPeriod4)
                        .add(cmbTargetyMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblTargetMonth))
                    .add(cmbTargetYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbReappearanceSpan, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTargetPeriod1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTargetDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTargetPeriod5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFixedCount1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtFixedCount1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMinFixedCount1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCountCondition)
                    .add(rdoSimpleCount)
                    .add(rdoTotalCount))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTechType)
                    .add(rdoTechOtherCount)
                    .add(rdoTechSameCount))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeRepeatLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnPeriodSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnExcelReport3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        pnlTypeInfluence.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "宣伝や広告の効果分析をしたい！", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS UI Gothic", 1, 12))); // NOI18N
        pnlTypeInfluence.setOpaque(false);
        pnlTypeInfluence.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlTypeInfluenceMouseExited(evt);
            }
        });
        pnlTypeInfluence.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pnlTypeInfluenceMouseMoved(evt);
            }
        });

        btnExcelReport4.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcelReport4.setBorderPainted(false);
        btnExcelReport4.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcelReport4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelReport4ActionPerformed(evt);
            }
        });

        //shop.addItem(this.myShop);

        lblTarget8.setText("対象");

        lblTargetPeriod8.setText("対象期間");

        lblFixedCount2.setText("固定回数");

        lblMinFixedCount2.setText("回以上");

        txtFixedCount2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFixedCount2.setText("3");

        lblTitle.setText("反響分析");

        cmbTargetPeriodStart4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStart4.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodStart1.setDate(new java.util.Date());

        lblSpe6.setText("～");
        lblSpe6.setFocusCycleRoot(true);

        cmbTargetPeriodEnd4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEnd4.setPreferredSize(new java.awt.Dimension(88, 20));
        cmbPeriodEnd1.setDate(new java.util.Date());

        org.jdesktop.layout.GroupLayout pnlTypeInfluenceLayout = new org.jdesktop.layout.GroupLayout(pnlTypeInfluence);
        pnlTypeInfluence.setLayout(pnlTypeInfluenceLayout);
        pnlTypeInfluenceLayout.setHorizontalGroup(
            pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTypeInfluenceLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlTypeInfluenceLayout.createSequentialGroup()
                        .add(lblTitle)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(pnlTypeInfluenceLayout.createSequentialGroup()
                        .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(pnlTypeInfluenceLayout.createSequentialGroup()
                                .add(lblFixedCount2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(43, 43, 43)
                                .add(txtFixedCount2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(lblMinFixedCount2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(pnlTypeInfluenceLayout.createSequentialGroup()
                                .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblTargetPeriod8)
                                    .add(lblTarget8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(51, 51, 51)
                                .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(cmbTarget4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(pnlTypeInfluenceLayout.createSequentialGroup()
                                        .add(cmbTargetPeriodStart4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(lblSpe6)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(cmbTargetPeriodEnd4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlTypeInfluenceLayout.createSequentialGroup()
                        .add(btnExcelReport4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(50, 50, 50))))
        );
        pnlTypeInfluenceLayout.setVerticalGroup(
            pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTypeInfluenceLayout.createSequentialGroup()
                .add(lblTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTarget8)
                    .add(cmbTarget4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblTargetPeriod8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblSpe6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cmbTargetPeriodStart4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cmbTargetPeriodEnd4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlTypeInfluenceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFixedCount2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtFixedCount2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMinFixedCount2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnExcelReport4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        btnAnlysis.setIcon(SystemInfo.getImageIcon("/button/master/analysis_off.jpg"));
        btnAnlysis.setBorderPainted(false);
        btnAnlysis.setEnabled(false);
        btnAnlysis.setPressedIcon(SystemInfo.getImageIcon("/button/master/analysis_on.jpg"));
        btnAnlysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnlysisActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(pnlDetailSale, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnlTypeSale, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(314, 314, 314)
                        .add(btnAnlysis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(lblReportName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 305, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, pnlTypeRepeat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, pnlTypeInfluence, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .add(0, 13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(pnlDetailSale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlTypeSale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnAnlysis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(pnlTypeRepeat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlTypeInfluence, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblReportName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTypeInfluence.getAccessibleContext().setAccessibleName("宣伝や広告の効果分析をしたい");
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 期間で絞って詳細な売上のEXCELボタンを押下。
     *
     * @param evt
     */
    private void btnExcelReport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelReport1ActionPerformed

        //業務報告
        if (this.rdoBusinessReport.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(210)) {
                return;
            }
        }

        //技術詳細報告
        if (this.rdoTechnicalReport.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(211)) {
                return;
            }
        }

        //商品詳細報告
        if (this.rdoItemReport.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(212)) {
                return;
            }
        }
        btnExcelReport1.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ReportLogic logic = new ReportLogic();
            ReportParameterBean paramBean = new ReportParameterBean();
            paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_BUSINESS);
            //割引の区分の設定
            Integer discountType = SystemInfo.getAccountSetting().getDiscountType();
            paramBean.setDiscountType(discountType);
            // 税区分(税抜き)
            if (this.rdoTaxBlank1.isSelected()) {
                paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);
            } // 税区分(税込み)
            else if (this.rdoTaxUnit1.isSelected()) {
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
                if (cmbTarget1.getSelectedItem() instanceof MstGroup) {
                    MstGroup mg = (MstGroup) cmbTarget1.getSelectedItem();
                    paramBean.setTargetName(mg.getGroupName());
                    paramBean.setShopIDList(mg.getShopIDListAll());
                } //店舗
                else if (cmbTarget1.getSelectedItem() instanceof MstShop) {
                    MstShop ms = (MstShop) cmbTarget1.getSelectedItem();
                    paramBean.setTargetName(ms.getShopName());
                    paramBean.setShopIDList(ms.getShopID().toString());
                }

                //対象となる店舗が存在しない場合
                if (paramBean.getShopIDList().equals("")) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(4001),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (0 < cmbStaff1.getSelectedIndex()) {
                    MstStaff ms = (MstStaff) cmbStaff1.getSelectedItem();
                    paramBean.setStaffId(ms.getStaffID());
                    paramBean.setStaffName(ms.getFullStaffName());
                }
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                start.setTime(this.cmbPeriodStart1.getDate());
                end.setTime(this.cmbPeriodEnd1.getDate());
                if (start.compareTo(end) != 0) {
                    if (start.after(end)) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                paramBean.setTargetStartDate(this.cmbPeriodStart1.getDateStr());
                paramBean.setTargetEndDate(this.cmbPeriodEnd1.getDateStr());
                Calendar cal = Calendar.getInstance();
                cal.setTime(cmbPeriodStart1.getDate());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                paramBean.setTargetStartDateObj(cal.getTime());
                cal.setTime(cmbPeriodEnd1.getDate());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                paramBean.setTargetEndDateObj(cal.getTime());
                ConnectionWrapper cw = SystemInfo.getConnection();
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
            if (!logicResult) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnExcelReport1ActionPerformed

    /**
     * 売上構成やその推移のEXCELボタンを押下。
     *
     * @param evt
     */
    private void btnExcelReport2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelReport2ActionPerformed

        AnalyticChartReportLogic logic = new AnalyticChartReportLogic();
        ReportParameterBean paramBean = new ReportParameterBean();
        paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_SALES);
        //Luc start delete 20140331
        //        // start add 20130708 IVS [gbソース]分析帳票の表示制限について 
        //        StringBuilder message = new StringBuilder();
        //        message.append("本帳票について一部数字の調整が必要なものがありましたので見直しを行っております。\n");
        //        message.append("見直しが終わりましたら改めてご報告させていただきます。");
        //        if (this.rdoSaleShift.isSelected() || this.rdoTypeShift.isSelected() || this.rdoZChart.isSelected()) {
        //            if(!SystemInfo.getLoginID().equals("mstest") || !SystemInfo.getDatabase().contains("pos_hair_dev")){
        //                MessageDialog.showMessageDialog(
        //                        this,
        //                        message.toString(),
        //                        this.getTitle(),
        //                        JOptionPane.INFORMATION_MESSAGE);
        //                return;
        //            }
        //        }
        //        // end add 20130708 IVS [gbソース]分析帳票の表示制限について 
        //Luc end delete 20140331
        if (rdoDayShift.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(231)) {
                return;
            }
        }
        if (rdoMonthShift.isSelected()) {
            if (!SystemInfo.checkAuthorityPassword(232)) {
                return;
            }
        }
        if (rdoDayShift.isSelected() && rdoMonthWay.isSelected()) {
            if (!inputCheckYear(cmbMonthWayYear, "月方式の年")) {
                return;
            }
        } else if (rdoMonthShift.isSelected()) {
            if (!inputCheckYear(cmbMonthWayMonth, "開始年")) {
                return;
            }
            if (!inputCheckYear(cmbMonthWayMonth, "終了年")) {
                return;
            }
        }
        if (cmbTarget2.getSelectedItem() instanceof MstGroup) {
            //グループ
            MstGroup mg = (MstGroup) cmbTarget2.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
            //IVS_ptthu start add 20160524 Bug #50677
            for (MstShop shop : mg.getShops()) {
                if (shop.getCourseFlag() == 1) {
                    paramBean.setCourseFlag(true);
                }
            }
            //IVS_ptthu start end 20160524 Bug #50677
        } else if (cmbTarget2.getSelectedItem() instanceof MstShop) {

            //店舗
            MstShop ms = (MstShop) cmbTarget2.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
            //IVS_ptthu start add 20160524 Bug #50677
            if (ms.getCourseFlag() == 1) {
                paramBean.setCourseFlag(true);
            }
             //IVS_ptthu start end 20160524 Bug #50677
        }

        //対象となる店舗が存在しない場合
        if (paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.rdoTaxBlank2.isSelected()) {
            // 税区分(税抜き)
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_BLANK);
        } else if (this.rdoTaxUnit2.isSelected()) {
            // 税区分(税込み)
            paramBean.setTaxType(ReportParameterBean.TAX_TYPE_UNIT);
        }
        if (this.rdoDayWay.isSelected()) {
            paramBean.setDayTargetType(ReportParameterBean.TARGET_TYPE_DAY);
        } else if (this.rdoMonthWay.isSelected()) {
            paramBean.setDayTargetType(ReportParameterBean.TARGET_TYPE_MONTH);
        }

        if (0 < cmbStaff2.getSelectedIndex()) {
            MstStaff ms = (MstStaff) cmbStaff2.getSelectedItem();
            paramBean.setStaffId(ms.getStaffID());
            paramBean.setStaffName(ms.getFullStaffName());
        }
        if (this.rdoMoney.isSelected()) {

            paramBean.setTypeCondi(this.rdoMoney.getText());
        }
        if (rdoPersonCount.isSelected()) {
            paramBean.setTypeCondi(this.rdoPersonCount.getText());
        }
        boolean logicResult = false;
        
        try {

            if (0 < cmbStaff2.getSelectedIndex()) {
                MstStaff ms = (MstStaff) cmbStaff2.getSelectedItem();
                paramBean.setStaffId(ms.getStaffID());
                paramBean.setStaffName(ms.getFullStaffName());
            }
            if (this.rdoDayShift.isSelected()) {
                // 日次
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();

                if (this.rdoDayWay.isSelected()) {
                    int[] startAry = this.cmbDayWayStartDate.getDate_IntArray();
                    int[] endAry = this.cmbDayWayEndDate.getDate_IntArray();
                    start.set(startAry[0], startAry[1] - 1, startAry[2]);
                    end.set(endAry[0], endAry[1] - 1, endAry[2]);

                    if (start.compareTo(end) != 0 && start.after(end)) {
                        MessageDialog.showMessageDialog(
                                this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

            } else if (this.rdoMonthShift.isSelected()) {
                // 月次
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                start.set(Integer.parseInt((String) this.cmbTargetPeriodYearStart1.getSelectedItem().toString()), Integer.parseInt((String) this.cmbTargetPeriodMonthStart1.getSelectedItem()) - 1, 1);
                end.set(Integer.parseInt((String) this.cmbTargetPeriodYearEnd1.getSelectedItem().toString()), Integer.parseInt((String) this.cmbTargetPeriodMonthEnd1.getSelectedItem()) - 1, 1);

                if (start.compareTo(end) != 0 && start.after(end)) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            this.setDate(paramBean);
            if (this.rdoDayShift.isSelected()) {
                paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_SALES_DAY);
            } else if (this.rdoMonthShift.isSelected()) {
                paramBean.setSubReportType(ReportParameterBean.SUB_REPORT_SALES_MONTH);
            }

            //売上構成やその推移報告を出力処理
            //売上別推移  
            if (this.rdoSaleShift.isSelected()) {

                Calendar calStart = Calendar.getInstance();
                calStart.set(Integer.parseInt(this.cmbTargetPeriodYearStart2.getSelectedItem().toString()), this.cmbTargetPeriodMonthStart2.getSelectedIndex(), 1);

                Calendar calEnd = (Calendar) calStart.clone();
                calEnd.add(Calendar.YEAR, +1);
                calEnd.add(Calendar.DAY_OF_MONTH, -1);

                paramBean.setTargetStartDateObj(calStart.getTime());
                paramBean.setTargetEndDateObj(calEnd.getTime());

                logicResult = logic.viewSalesReportAll(paramBean);
            } //分類別売上推移
            else if (this.rdoTypeShift.isSelected()) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(Integer.parseInt(this.cmbTargetPeriodYearStart2.getSelectedItem().toString()), this.cmbTargetPeriodMonthStart2.getSelectedIndex(), 1);
                Calendar calEnd = (Calendar) calStart.clone();
                calEnd.add(Calendar.YEAR, +1);
                calEnd.add(Calendar.DAY_OF_MONTH, -1);

                paramBean.setTargetStartDateObj(calStart.getTime());
                paramBean.setTargetEndDateObj(calEnd.getTime());

                logicResult = logic.viewSalesReportByCategory(paramBean);

            } //Zチャート 
            else if (this.rdoZChart.isSelected()) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(Integer.parseInt(this.cmbTargetPeriodYearStart2.getSelectedItem().toString()), this.cmbTargetPeriodMonthStart2.getSelectedIndex(), 1);
                Calendar calEnd = (Calendar) calStart.clone();
                calEnd.add(Calendar.YEAR, +1);
                calEnd.add(Calendar.DAY_OF_MONTH, -1);

                paramBean.setTargetStartDateObj(calStart.getTime());
                paramBean.setTargetEndDateObj(calEnd.getTime());
                calStart.add(Calendar.YEAR, -1);
                calEnd.add(Calendar.YEAR, -1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                paramBean.setTargetStartDate(format.format(calStart.getTime()));
                paramBean.setTargetEndDate(format.format(calEnd.getTime()));
                calStart.add(Calendar.YEAR, -1);
                calEnd.add(Calendar.YEAR, -1);
                paramBean.setTargetStartMonth(format.format(calStart.getTime()));
                paramBean.setTargetEndMonth(format.format(calEnd.getTime()));
                logicResult = logic.viewSalesReportByZchart(paramBean);
            } //月次推移 
            else if (this.rdoMonthShift.isSelected()) {

                ReportLogic logic1 = new ReportLogic();
                // vtbphuong start add 20140926 Bug #30957
                paramBean.setCategoryIDList("");
                paramBean.setCategoryNameList("");
                // vtbphuong end add 20140926 Bug #30957
                logicResult = logic1.viewSalesReport(paramBean);
            } //日次推移  
            else if (this.rdoDayShift.isSelected()) {
                ReportLogic logic1 = new ReportLogic();
                // vtbphuong start add 20140926 Bug #30957
                paramBean.setCategoryIDList("");
                paramBean.setCategoryNameList("");
                // vtbphuong end add 20140926 Bug #30957
                logicResult = logic1.viewSalesReport(paramBean);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!logicResult) {
            // エラー時
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnExcelReport2ActionPerformed
    /**
     * setDate
     *
     * @param paramBean
     */
    private void setDate(ReportParameterBean paramBean) {

        if (cmbTarget2.getSelectedItem() instanceof MstShop) {
            MstShop ms = (MstShop) cmbTarget2.getSelectedItem();
            paramBean.setCutoffDay(ms.getCutoffDay());
        } else {
            paramBean.setCutoffDay(SystemInfo.getAccountSetting().getCutoffDay());
        }

        if (rdoDayShift.isSelected()) {

            //---------------------
            //日次推移
            //---------------------

            if (rdoDayWay.isSelected()) {

                //日数方式
                paramBean.setTargetStartDateObj(cmbDayWayStartDate.getDate());
                paramBean.setTargetEndDateObj(cmbDayWayEndDate.getDate());

            } else if (rdoMonthWay.isSelected()) {

                //月方式
                Calendar calStart = Calendar.getInstance();
                calStart.set(Integer.parseInt(cmbMonthWayYear.getSelectedItem().toString()), cmbMonthWayMonth.getSelectedIndex(), 1);
                Calendar calEnd = (Calendar) calStart.clone();
                calEnd.add(Calendar.MONTH, 1);
                calEnd.add(Calendar.DAY_OF_MONTH, -1);

                this.resetSpan(paramBean, calStart, calEnd);
                paramBean.setTargetStartDateObj(calStart.getTime());
                paramBean.setTargetEndDateObj(calEnd.getTime());
            }

        } else if (rdoMonthShift.isSelected()) {

            //---------------------
            //月次推移
            //---------------------
            Calendar calStart = Calendar.getInstance();
            calStart.set(Integer.parseInt(cmbTargetPeriodYearStart1.getSelectedItem().toString()), cmbTargetPeriodMonthStart1.getSelectedIndex(), 1);
            Calendar calEnd = (Calendar) calStart.clone();
            calEnd.add(Calendar.MONTH, 1);
            calEnd.add(Calendar.DAY_OF_MONTH, -1);
            this.resetSpan(paramBean, calStart, calEnd);
            paramBean.setTargetStartDateObj(calStart.getTime());

            calStart.set(Integer.parseInt(cmbTargetPeriodYearEnd1.getSelectedItem().toString()), cmbTargetPeriodMonthEnd1.getSelectedIndex(), 1);
            calEnd = (Calendar) calStart.clone();
            calEnd.add(Calendar.MONTH, 1);
            calEnd.add(Calendar.DAY_OF_MONTH, -1);
            this.resetSpan(paramBean, calStart, calEnd);
            paramBean.setTargetEndDateObj(calEnd.getTime());
        }
    }

    /**
     * resetSpan
     *
     * @param paramBean
     * @param calStart
     * @param calEnd
     */
    private void resetSpan(ReportParameterBean paramBean, Calendar calStart, Calendar calEnd) {

        if (paramBean.getCutoffDay() == 31) {
            return;
        }
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(calEnd.getTime());
        if (calTo.getActualMaximum(Calendar.DATE) <= paramBean.getCutoffDay()) {
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DATE));
        } else {
            calTo.set(Calendar.DAY_OF_MONTH, paramBean.getCutoffDay());
        }
        Calendar calFrom = (Calendar) calTo.clone();
        calFrom.add(Calendar.MONTH, -1);
        calFrom.add(Calendar.DAY_OF_MONTH, 1);

        calStart.setTime(calFrom.getTime());
        calEnd.setTime(calTo.getTime());
    }

    /**
     * inputCheckyear
     *
     * @param cmb
     * @param itemName
     * @return
     */
    private boolean inputCheckYear(JComboBox cmb, String itemName) {
        NumberUtils numUtil = new NumberUtils();
        if (!numUtil.isNumber(cmb.getSelectedItem().toString())) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, itemName),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmb.requestFocusInWindow();
            return false;
        }

        return true;
    }

    /**
     * 目標設定ボタンを押下。
     *
     * @param evt
     */
    private void btnPurposeSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurposeSettingActionPerformed
        // SearchDataTargetPanel tgp = new SearchDataTargetPanel();
//      TargetSettingPanel tsp = new TargetSettingPanel();
//        tsp.setSize(825, 600);
//        SwingUtil.openAnchorDialog(this.parentFrame, false, tsp, "目標設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER | SwingUtil.ANCHOR_TOP);
//        System.gc();
  
             TargetSettingPanel tsp = new TargetSettingPanel(this);
             parentFrame.changeContents(tsp);
    }//GEN-LAST:event_btnPurposeSettingActionPerformed
    /**
     * export Excel
     */
    private void exportReappearance() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        ReportLogic logic = new ReportLogic();
        ReportParameterBean paramBean = new ReportParameterBean();
        paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);
        //Luc start delete 20140331
        //        // start add 20130708 IVS [gbソース]分析帳票の表示制限について 
        //        StringBuilder message = new StringBuilder();
        //        message.append("本帳票について一部数字の調整が必要なものがありましたので見直しを行っております。\n");
        //        message.append("見直しが終わりましたら改めてご報告させていただきます。");
        //        if (this.rdoReappearance.isSelected() || this.rdoActive.isSelected()) {
        //            if(!SystemInfo.getLoginID().equals("mstest") || !SystemInfo.getDatabase().contains("pos_hair_dev")){
        //                MessageDialog.showMessageDialog(
        //                                this,
        //                                message.toString(),
        //                                this.getTitle(),
        //                                JOptionPane.INFORMATION_MESSAGE);
        //                        return;
        //            }
        //        }
        //        // end add 20130708 IVS [gbソース]分析帳票の表示制限について
        //Luc end delete 20140331
        //グループ
        if (cmbTarget3.getSelectedItem() instanceof MstGroup) {
            MstGroup mg = (MstGroup) cmbTarget3.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
        } //店舗
        else if (cmbTarget3.getSelectedItem() instanceof MstShop) {
            MstShop ms = (MstShop) cmbTarget3.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }
        //対象となる店舗が存在しない場合
        if (paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 主担当者
        if (cmbStaff3.getSelectedIndex() > 0) {
            paramBean.setStaffId(((MstStaff) cmbStaff3.getSelectedItem()).getStaffID());
            paramBean.setStaffName(((MstStaff) cmbStaff3.getSelectedItem()).getFullStaffName());
        } else {
            paramBean.setStaffId(null);
            paramBean.setStaffName("全体");
        }
        // 再来算出期間
        int reappearanceCount = getReappearanceSpan();
        paramBean.setReappearanceCountMessage(this.cmbReappearanceSpan.getSelectedItem().toString());
        // 固定回数
        int fixedCount = 5;
        try {
            fixedCount = Integer.parseInt(this.txtFixedCount1.getText());
        } catch (NumberFormatException e) {
        }
        paramBean.setFixedCount(fixedCount);
        // 人数条件
        String numberCondition = "延べ客数";
        if (this.rdoTotalCount.isSelected()) {
            numberCondition = "延べ客数";
        }
        if (this.rdoSimpleCount.isSelected()) {
            numberCondition = "純客数";
        }
        paramBean.setNumberCondition(numberCondition);
        // 技術分類再来
        paramBean.setSameTechnicClass(this.rdoTechSameCount.isSelected());
        // 集計期間を求める
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(Calendar.YEAR, Integer.parseInt(this.cmbTargetYear.getSelectedItem().toString()));
        } catch (Exception e) {
        }
        cal.set(Calendar.MONTH, cmbTargetyMonth.getSelectedIndex() - 1);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        java.util.Date targetDate = cal.getTime();

        // ○ヶ月前の月初
        Calendar calculationStart = Calendar.getInstance();
        calculationStart.setTime(targetDate);
        if (reappearanceCount == 45) {
            // 45日再来は2ヶ月前の15日
            calculationStart.add(Calendar.MONTH, -2);
            calculationStart.set(Calendar.DAY_OF_MONTH, 15);
        } else {
            // それ以外は○ヶ月前の1日
            calculationStart.add(Calendar.MONTH, (reappearanceCount * -1));
            calculationStart.set(Calendar.DAY_OF_MONTH, 1);
        }

        // 選んだ月の月末
        Calendar calculationEnd = Calendar.getInstance();
        calculationEnd.set(this.cmbTargetYear.getSelectedIndex(), cmbTargetyMonth.getSelectedIndex(), 1);// 選択月の翌月にして
        calculationEnd.add(Calendar.DAY_OF_MONTH, -1); // １日戻す

        // 再来対象期間
        // 開始日は集計開始日と同じ
        Calendar targetStart = Calendar.getInstance();
        targetStart.setTime(calculationStart.getTime());

        Calendar targetEnd = Calendar.getInstance();
        targetEnd.setTime(calculationStart.getTime());
        if (reappearanceCount == 45) {
            // 45日再来の終了は、翌月の14日
            targetEnd.add(Calendar.MONTH, 1);
            targetEnd.set(Calendar.DAY_OF_MONTH, 14);
        } else {
            // 終了は開始月の月末
            targetEnd.add(Calendar.MONTH, 1);         // 翌月にして
            targetEnd.add(Calendar.DAY_OF_MONTH, -1); // １日戻す
        }

        // 期間設定を締日基準にする
        if (reappearanceCount != 45) {
            this.resetSpan(targetStart, targetEnd);
            this.resetSpan(calculationStart, calculationEnd);
            calculationStart.setTime(targetStart.getTime());
        }

        paramBean.setTargetStartDate(sdf.format(targetStart.getTime()));
        paramBean.setTargetEndDate(sdf.format(targetEnd.getTime()));
        paramBean.setTargetStartDateObj(targetStart.getTime());
        paramBean.setTargetEndDateObj(targetEnd.getTime());

        paramBean.setCalculationStartDate(sdf.format(calculationStart.getTime()));
        paramBean.setCalculationEndDate(sdf.format(calculationEnd.getTime()));
        paramBean.setCalculationStartDateObj(calculationStart.getTime());
        paramBean.setCalculationEndDateObj(calculationEnd.getTime());

        //出力処理
        boolean logicResult = true;
        try {

            //来店サイクル分析 
            if (rdoReappearance.isSelected()) {

                Calendar calStart = Calendar.getInstance();
                calStart.set(Integer.parseInt(this.cmbTargetPeriodYearStart3.getSelectedItem().toString()), this.cmbTargetPeriodMonthStart3.getSelectedIndex(), 1);
                paramBean.setTargetStartDateObj(calStart.getTime());
                Calendar calEnd = (Calendar) calStart.clone();

                calEnd.set(Integer.parseInt(this.cmbTargetPeriodYearStart3.getSelectedItem().toString()) + 1, this.cmbTargetPeriodMonthStart3.getSelectedIndex(), 1);

                this.resetSpan(paramBean, calStart, calEnd);


                paramBean.setTargetEndDateObj(calEnd.getTime());

                logicResult = logic.CircleAnalysis(paramBean);
            }
            //再来分析表 
            if (rdoReappear.isSelected()) {
                if (inputCheckYear(cmbTargetYear, "集計期間")) {
                    btnExcelReport3.setCursor(null);
                    this.doOutput();
                }

            }
            //アクティブ顧客分析  
            if (rdoActive.isSelected()) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(Integer.parseInt(this.cmbTargetPeriodYearStart4.getSelectedItem().toString()), this.cmbTargetPeriodMonthStart4.getSelectedIndex(), 1);
                paramBean.setTargetStartDateObj(calStart.getTime());
                Calendar calEnd = (Calendar) calStart.clone();
                calEnd.add(Calendar.YEAR, +1);
                this.resetSpan(paramBean, calStart, calEnd);
                paramBean.setTargetEndDateObj(calEnd.getTime());
                logicResult = logic.ActiveCustomerAnalytic(paramBean);
            }
        } catch (Exception e) {

            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // エラー時
        if (!logicResult) {
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * getReappearanceSpan
     *
     * @return
     */
    private int getReappearanceSpan() {
        String textSpan = this.cmbReappearanceSpan.getSelectedItem().toString();

        if ("1ヶ月".equals(textSpan)) {
            return 1;
        } else if ("45日".equals(textSpan)) {
            return 45;
        } else if ("2ヶ月".equals(textSpan)) {
            return 2;
        } else if ("3ヶ月".equals(textSpan)) {
            return 3;
        } else if ("4ヶ月".equals(textSpan)) {
            return 4;
        } else if ("5ヶ月".equals(textSpan)) {
            return 5;
        } else {
            return 6;
        }
    }

    /**
     * resetSpan
     *
     * @param calStart
     * @param calEnd
     */
    private void resetSpan(Calendar calStart, Calendar calEnd) {

        int cutoffDay = 0;

        if (cmbTarget3.getSelectedItem() instanceof MstShop) {
            MstShop ms = (MstShop) cmbTarget3.getSelectedItem();
            cutoffDay = ms.getCutoffDay();
        } else {
            cutoffDay = SystemInfo.getAccountSetting().getCutoffDay();
        }
        if (cutoffDay == 31) {
            return;
        }

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(calEnd.getTime());

        if (calTo.getActualMaximum(Calendar.DATE) <= cutoffDay) {
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DATE));
        } else {
            calTo.set(Calendar.DAY_OF_MONTH, cutoffDay);
        }
        Calendar calFrom = (Calendar) calTo.clone();
        calFrom.add(Calendar.MONTH, -1);
        calFrom.add(Calendar.DAY_OF_MONTH, 1);
        calStart.setTime(calFrom.getTime());
        calEnd.setTime(calTo.getTime());
    }

    /**
     * checkReappearance
     *
     * @return
     */
    private boolean checkReappearance() {
        NumberUtils numUtil = new NumberUtils();
        if (!numUtil.isNumber(this.cmbTargetYear.getSelectedItem().toString())) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "対象年"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            Calendar cdr = Calendar.getInstance();
            this.cmbTargetYear.requestFocusInWindow();
            return false;
        }

        return true;
    }

    /**
     * 宣伝や広告の効果分析のEXCELLボタンを押下。
     *
     * @param evt
     */
    private void btnExcelReport4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelReport4ActionPerformed
        // TODO add your handling code here:
        // 入力チェックを行う
        if (!checkResponse()) {
            return;
        }
        // 出力を行う
        printResponse();

    }//GEN-LAST:event_btnExcelReport4ActionPerformed

    /**
     * 分析表示ボタン
     *
     * @param evt
     */
    private void btnAnlysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnlysisActionPerformed
        HashMap hashMap = new HashMap();
        hashMap.put("対象", this.cmbTarget2.getSelectedItem());
        hashMap.put("対象期間", this.cmbTimePeriodStart.getDateStr());
        hashMap.put("対象期間1", this.cmbTimePeriodEnd.getDateStr());
        if (this.rdoTaxUnit2.isSelected()) {
            hashMap.put(this.rdoTaxUnit2.getText(), new Boolean(this.rdoTaxUnit2.isSelected()));
        }
        if (this.rdoTaxBlank2.isSelected()) {
            hashMap.put(this.rdoTaxBlank2.getText(), new Boolean(this.rdoTaxBlank2.isSelected()));
        }

        if (this.rdoDay.isSelected()) {
            hashMap.put(this.rdoDay.getText(), new Boolean(this.rdoDay.isSelected()));
        }
        if (this.rdoType.isSelected()) {
            hashMap.put(this.rdoType.getText(), new Boolean(this.rdoType.isSelected()));
        }
        if (this.rdoWeek.isSelected()) {
            hashMap.put(this.rdoWeek.getText(), new Boolean(this.rdoWeek.isSelected()));
        }
        if (this.rdoClassAll.isSelected()) {
            hashMap.put(this.rdoClassAll.getText(), new Boolean(this.rdoClassAll.isSelected()));
        }
        if (this.rdoClassTech.isSelected()) {
            hashMap.put(this.rdoClassTech.getText(), new Boolean(this.rdoClassTech.isSelected()));
        }
        if (this.rdoClassItem.isSelected()) {
            hashMap.put(this.rdoClassItem.getText(), new Boolean(this.rdoClassItem.isSelected()));
        }

        if (this.rdoVisitTime.isSelected()) {
            hashMap.put(this.rdoVisitTime.getText(), new Boolean(this.rdoVisitTime.isSelected()));
        }
        if (this.rdoStartTime.isSelected()) {
            hashMap.put(this.rdoStartTime.getText(), new Boolean(this.rdoStartTime.isSelected()));
        }
        if (this.rdoLeaveTime.isSelected()) {
            hashMap.put(this.rdoLeaveTime.getText(), new Boolean(this.rdoLeaveTime.isSelected()));
        }

        if (this.rdoTimeFixed.isSelected()) {
            hashMap.put(this.rdoTimeFixed.getText(), new Boolean(this.rdoTimeFixed.isSelected()));
        }
        if (this.rdoTimeCustom.isSelected()) {
            hashMap.put(this.rdoTimeCustom.getText(), new Boolean(this.rdoTimeCustom.isSelected()));
        }

        if (this.rdoMoney.isSelected()) {
            hashMap.put(this.rdoMoney.getText(), new Boolean(this.rdoMoney.isSelected()));
        }
        if (this.rdoPersonCount.isSelected()) {
            hashMap.put(this.rdoPersonCount.getText(), new Boolean(this.rdoPersonCount.isSelected()));
        }

        TimeAnalysisPanel1 tgp = new TimeAnalysisPanel1(hashMap);
        SwingUtil.openAnchorDialog(this.parentFrame, false, tgp, "時間帯分析", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER | SwingUtil.ANCHOR_TOP);
        System.gc();
    }//GEN-LAST:event_btnAnlysisActionPerformed
    /**
     * 月次推移ラジオのじょうたいを変更する。
     *
     * @param evt
     */
    private void rdoMonthShiftStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoMonthShiftStateChanged
        this.lblReportName.setText("月別の売上状況を確認できます。");
        if (this.rdoMonthShift.isSelected()) {
            this.cmbDayWayEndDate.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbDayWayStartDate.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodYearStart1.setEnabled(this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodMonthStart1.setEnabled(this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodYearEnd1.setEnabled(this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodMonthEnd1.setEnabled(this.rdoMonthShift.isSelected());
            this.rdoDayWay.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoMonthWay.setEnabled(!this.rdoMonthShift.isSelected());
            this.btnAnlysis.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoVisitTime.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoTimeFixed.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoMoney.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoStartTime.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoTimeCustom.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoPersonCount.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoLeaveTime.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoDay.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoType.setEnabled(!this.rdoMonthShift.isSelected());
            this.rdoWeek.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbTimePeriodStart.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoMonthShift.isSelected());
            this.btnExcelReport2.setEnabled(this.rdoMonthShift.isSelected());
            this.techItemClassLabel.setEnabled(this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodYearStart2.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodMonthStart2.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbMonthWayYear.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbMonthWayMonth.setEnabled(!this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodYearEnd1.setVisible(this.rdoMonthShift.isSelected());
            this.cmbTargetPeriodMonthEnd1.setVisible(this.rdoMonthShift.isSelected());
            this.lblSpe3.setVisible(this.rdoMonthShift.isSelected());
            disableControl();
        }
    }//GEN-LAST:event_rdoMonthShiftStateChanged
    /**
     * 日次推移ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoDayShiftStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoDayShiftStateChanged

        DayShiftStateChanged();
        this.lblReportName.setText("日別の売上状況を確認できます。");

    }//GEN-LAST:event_rdoDayShiftStateChanged
    private void DayShiftStateChanged() {

        if (this.rdoDayShift.isSelected()) {
            this.rdoMonthWay.setEnabled(true);
            this.rdoDayWay.setEnabled(true);
            if (this.rdoMonthWay.isSelected()) {
                this.cmbMonthWayYear.setEnabled(true);
                this.cmbMonthWayMonth.setEnabled(true);
                this.cmbDayWayStartDate.setEnabled(false);
                this.cmbDayWayEndDate.setEnabled(false);
            } else if (this.rdoDayWay.isSelected()) {
                this.cmbMonthWayYear.setEnabled(false);
                this.cmbMonthWayMonth.setEnabled(false);
                this.cmbDayWayStartDate.setEnabled(true);
                this.cmbDayWayEndDate.setEnabled(true);
            }
            this.btnAnlysis.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoVisitTime.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoTimeFixed.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoMoney.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoStartTime.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoTimeCustom.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoPersonCount.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoLeaveTime.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoDay.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoType.setEnabled(!this.rdoDayShift.isSelected());
            this.rdoWeek.setEnabled(!this.rdoDayShift.isSelected());
            this.cmbTimePeriodStart.setEnabled(!this.rdoDayShift.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoDayShift.isSelected());
            this.btnExcelReport2.setEnabled(this.rdoDayShift.isSelected());
            this.techItemClassLabel.setEnabled(this.rdoDayShift.isSelected());
            this.cmbTargetPeriodYearStart2.setEnabled(!this.rdoDayShift.isSelected());
            this.cmbTargetPeriodMonthStart2.setEnabled(!this.rdoDayShift.isSelected());
            this.lblSpe3.setVisible(this.rdoDayShift.isSelected());
            this.cmbTargetPeriodYearEnd1.setVisible(this.rdoDayShift.isSelected());
            this.cmbTargetPeriodMonthEnd1.setVisible(this.rdoDayShift.isSelected());
            this.cmbTargetPeriodYearStart1.setEnabled(!this.rdoDayShift.isSelected());
            this.cmbTargetPeriodMonthStart1.setEnabled(!this.rdoDayShift.isSelected());
            this.cmbTargetPeriodYearEnd1.setEnabled(!this.rdoDayShift.isSelected());
            this.cmbTargetPeriodMonthEnd1.setEnabled(!this.rdoDayShift.isSelected());
            disableControl();
        }
    }

    /**
     * 日数方式ラジオを押下
     *
     * @param evt
     */
    private void rdoDayWayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDayWayActionPerformed
        this.cmbMonthWayYear.setEnabled(false);
        this.cmbMonthWayMonth.setEnabled(false);
        this.cmbDayWayStartDate.setEnabled(true);
        this.cmbDayWayEndDate.setEnabled(true);

    }//GEN-LAST:event_rdoDayWayActionPerformed
    /**
     * 月方針ラジオを押下
     *
     * @param evt
     */
    private void rdoMonthWayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMonthWayActionPerformed
        this.cmbMonthWayYear.setEnabled(true);
        this.cmbMonthWayMonth.setEnabled(true);
        this.cmbDayWayStartDate.setEnabled(false);
        this.cmbDayWayEndDate.setEnabled(false);
    }//GEN-LAST:event_rdoMonthWayActionPerformed

    /**
     * 売上別推移ラジオの状態をへ変更する。
     *
     * @param evt
     */
    private void rdoSaleShiftStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoSaleShiftStateChanged
        this.lblReportName.setText("技術商品別の月別の売上推移を確認できます。");
        if (this.rdoSaleShift.isSelected()) {
            this.cmbDayWayStartDate.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbDayWayEndDate.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoDayWay.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoMonthWay.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbMonthWayYear.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbMonthWayMonth.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbTargetPeriodYearStart1.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbTargetPeriodMonthStart1.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbTargetPeriodYearEnd1.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbTargetPeriodMonthEnd1.setEnabled(!this.rdoSaleShift.isSelected());
            this.btnAnlysis.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoVisitTime.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoTimeFixed.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoMoney.setEnabled(this.rdoSaleShift.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoStartTime.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoTimeCustom.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoPersonCount.setEnabled(this.rdoSaleShift.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoLeaveTime.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoDay.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoType.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoWeek.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbTimePeriodStart.setEnabled(!this.rdoSaleShift.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoSaleShift.isSelected());
            this.btnExcelReport2.setEnabled(this.rdoSaleShift.isSelected());
            this.techItemClassLabel.setEnabled(this.rdoSaleShift.isSelected());
            this.lblOuptCond.setEnabled(this.rdoSaleShift.isSelected());
            this.cmbTargetPeriodYearStart2.setEnabled(this.rdoSaleShift.isSelected());
            this.cmbTargetPeriodMonthStart2.setEnabled(this.rdoSaleShift.isSelected());
            this.rdoTaxBlank2.setSelected(this.rdoSaleShift.isSelected());
            this.lblSum.setEnabled(!this.rdoSaleShift.isSelected());
            this.lblTimePeriod.setEnabled(!this.rdoSaleShift.isSelected());
            this.techItemClassLabel.setEnabled(!this.rdoSaleShift.isSelected());
            this.lblSumMethod.setEnabled(!this.rdoSaleShift.isSelected());
            this.lblTimeCond.setEnabled(!this.rdoSaleShift.isSelected());
            this.rdoMoney.setSelected(this.rdoSaleShift.isSelected());
            disableControl();
        }

    }//GEN-LAST:event_rdoSaleShiftStateChanged
    /**
     * 分類別売上推移ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoTypeShiftStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoTypeShiftStateChanged
        this.lblReportName.setText("分類別の月別の売上推移を確認できます。");
        if (this.rdoTypeShift.isSelected()) {
            this.cmbDayWayStartDate.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbDayWayEndDate.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoDayWay.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoMonthWay.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbMonthWayYear.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbMonthWayMonth.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbTargetPeriodYearStart1.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbTargetPeriodMonthStart1.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbTargetPeriodYearEnd1.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbTargetPeriodMonthEnd1.setEnabled(!this.rdoTypeShift.isSelected());
            this.btnAnlysis.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoVisitTime.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoTimeFixed.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoMoney.setEnabled(this.rdoTypeShift.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoStartTime.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoTimeCustom.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoPersonCount.setEnabled(this.rdoTypeShift.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoLeaveTime.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoDay.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoType.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoWeek.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbTimePeriodStart.setEnabled(!this.rdoTypeShift.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoTypeShift.isSelected());
            this.btnExcelReport2.setEnabled(this.rdoTypeShift.isSelected());
            this.techItemClassLabel.setEnabled(this.rdoTypeShift.isSelected());
            this.cmbTargetPeriodYearStart2.setEnabled(this.rdoTypeShift.isSelected());
            this.cmbTargetPeriodMonthStart2.setEnabled(this.rdoTypeShift.isSelected());
            this.lblOuptCond.setEnabled(this.rdoTypeShift.isSelected());
            this.rdoTaxBlank2.setSelected(this.rdoTypeShift.isSelected());
            this.lblSum.setEnabled(!this.rdoTypeShift.isSelected());
            this.lblTimePeriod.setEnabled(!this.rdoTypeShift.isSelected());
            this.techItemClassLabel.setEnabled(!this.rdoTypeShift.isSelected());
            this.lblSumMethod.setEnabled(!this.rdoTypeShift.isSelected());
            this.lblTimeCond.setEnabled(!this.rdoTypeShift.isSelected());
            this.rdoMoney.setSelected(this.rdoTypeShift.isSelected());
            disableControl();

        }
    }//GEN-LAST:event_rdoTypeShiftStateChanged
    /**
     * Zチャートラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoZChartStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoZChartStateChanged
        this.lblReportName.setText("年間の月別と累計の売上推移を確認できます。");
        if (this.rdoZChart.isSelected()) {
            this.cmbDayWayStartDate.setEnabled(!this.rdoZChart.isSelected());
            this.cmbDayWayEndDate.setEnabled(!this.rdoZChart.isSelected());
            this.rdoDayWay.setEnabled(!this.rdoZChart.isSelected());
            this.rdoMonthWay.setEnabled(!this.rdoZChart.isSelected());
            this.cmbMonthWayYear.setEnabled(!this.rdoZChart.isSelected());
            this.cmbMonthWayMonth.setEnabled(!this.rdoZChart.isSelected());
            this.cmbTargetPeriodYearStart1.setEnabled(!this.rdoZChart.isSelected());
            this.cmbTargetPeriodMonthStart1.setEnabled(!this.rdoZChart.isSelected());
            this.cmbTargetPeriodYearEnd1.setEnabled(!this.rdoZChart.isSelected());
            this.cmbTargetPeriodMonthEnd1.setEnabled(!this.rdoZChart.isSelected());
            this.btnAnlysis.setEnabled(!this.rdoZChart.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoZChart.isSelected());
            this.rdoVisitTime.setEnabled(!this.rdoZChart.isSelected());
            this.rdoTimeFixed.setEnabled(!this.rdoZChart.isSelected());
            this.rdoMoney.setEnabled(!this.rdoZChart.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoZChart.isSelected());
            this.rdoStartTime.setEnabled(!this.rdoZChart.isSelected());
            this.rdoTimeCustom.setEnabled(!this.rdoZChart.isSelected());
            this.rdoPersonCount.setEnabled(!this.rdoZChart.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoZChart.isSelected());
            this.rdoLeaveTime.setEnabled(!this.rdoZChart.isSelected());
            this.rdoDay.setEnabled(!this.rdoZChart.isSelected());
            this.rdoType.setEnabled(!this.rdoZChart.isSelected());
            this.rdoWeek.setEnabled(!this.rdoZChart.isSelected());
            this.cmbTimePeriodStart.setEnabled(!this.rdoZChart.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoZChart.isSelected());
            this.btnExcelReport2.setEnabled(this.rdoZChart.isSelected());
            this.cmbTargetPeriodYearStart2.setEnabled(this.rdoZChart.isSelected());
            this.cmbTargetPeriodMonthStart2.setEnabled(this.rdoZChart.isSelected());
            this.lblOuptCond.setEnabled(!this.rdoZChart.isSelected());
            this.rdoTaxBlank2.setSelected(this.rdoZChart.isSelected());
            this.lblSum.setEnabled(!this.rdoZChart.isSelected());
            this.lblTimePeriod.setEnabled(!this.rdoZChart.isSelected());
            this.techItemClassLabel.setEnabled(!this.rdoZChart.isSelected());
            this.lblSumMethod.setEnabled(!this.rdoZChart.isSelected());
            this.lblTimeCond.setEnabled(!this.rdoZChart.isSelected());
            this.rdoMoney.setSelected(this.rdoZChart.isSelected());
            disableControl();
        }
    }//GEN-LAST:event_rdoZChartStateChanged
    /**
     * 時間帯分析ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoTimeAnalysisStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoTimeAnalysisStateChanged

        this.lblReportName.setText("時間帯別に売上状況を確認できます。");
        if (this.rdoTimeAnalysis.isSelected()) {
            this.cmbDayWayStartDate.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbDayWayEndDate.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.rdoDayWay.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.rdoMonthWay.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbMonthWayYear.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbMonthWayMonth.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodYearStart1.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodMonthStart1.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodYearEnd1.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodMonthEnd1.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.btnExcelReport2.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.btnAnlysis.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.rdoVisitTime.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoTimeFixed.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoMoney.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.rdoStartTime.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoTimeCustom.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoPersonCount.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.rdoLeaveTime.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoDay.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoType.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.rdoWeek.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.lblSum.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.lblTimePeriod.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.lblSumMethod.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.lblTimeCond.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.cmbTimePeriodStart.setEnabled(this.rdoTimeAnalysis.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.btnExcelReport2.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodYearStart2.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodMonthStart2.setEnabled(!this.rdoTimeAnalysis.isSelected());
            this.lblSpe3.setVisible(this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodYearEnd1.setVisible(this.rdoTimeAnalysis.isSelected());
            this.cmbTargetPeriodMonthEnd1.setVisible(this.rdoTimeAnalysis.isSelected());
            this.techItemClassLabel.setEnabled(!this.rdoTimeAnalysis.isSelected());
            setInitDate();
            disableControl();
        }
    }//GEN-LAST:event_rdoTimeAnalysisStateChanged
    /**
     * 業務報告ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoBusinessReportStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoBusinessReportStateChanged
        this.lblReportName.setText("お店の売上状況を確認できます");

    }//GEN-LAST:event_rdoBusinessReportStateChanged
    /**
     * 技術詳細ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoTechnicalReportStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoTechnicalReportStateChanged
        this.lblReportName.setText("技術の詳細売上を確認できます。");

    }//GEN-LAST:event_rdoTechnicalReportStateChanged
    /**
     * 商品詳細ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoItemReportStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoItemReportStateChanged
        this.lblReportName.setText("商品の詳細売上を確認できます。");

    }//GEN-LAST:event_rdoItemReportStateChanged
    /**
     * 宣伝や広告の効果分析パネルを入れる。
     *
     * @param evt
     */
    private void pnlTypeInfluenceMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTypeInfluenceMouseMoved
        this.lblReportName.setText("反響項目別の入客・売上状況を確認できます。");
        
    }//GEN-LAST:event_pnlTypeInfluenceMouseMoved
    /**
     * 日別集計ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoDayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoDayStateChanged
        if (this.rdoDay.isSelected()) {
            this.cmbTimePeriodStart.setEnabled(this.rdoDay.isSelected());
            this.cmbTimePeriodEnd.setEnabled(!this.rdoDay.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoDay.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoDay.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoDay.isSelected());
            this.rdoMoney.setEnabled(this.rdoDay.isSelected());
            this.rdoPersonCount.setEnabled(this.rdoDay.isSelected());
            this.techItemClassLabel.setEnabled(!this.rdoDay.isSelected());
            this.rdoTaxBlank2.setSelected(true);
            this.lblOuptCond.setEnabled(this.rdoDay.isSelected());
            setInitDate();
        }
    }//GEN-LAST:event_rdoDayStateChanged
    /**
     * 業務報告ラジオを出る。
     *
     * @param evt
     */
    private void rdoBusinessReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoBusinessReportMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoBusinessReportMouseExited
    /**
     * 技術詳細ラジオを出る。
     *
     * @param evt
     */
    private void rdoTechnicalReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTechnicalReportMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoTechnicalReportMouseExited
    /**
     * 商品詳細ラジオを出る。
     *
     * @param evt
     */
    private void rdoItemReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoItemReportMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoItemReportMouseExited
    /**
     * 日次推移ラジオを出る。
     *
     * @param evt
     */
    private void rdoDayShiftMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoDayShiftMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoDayShiftMouseExited
    /**
     * 月次推移ラジオを出る。
     *
     * @param evt
     */
    private void rdoMonthShiftMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoMonthShiftMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoMonthShiftMouseExited
    /**
     * 時間帯分析ラジオを出る。
     *
     * @param evt
     */
    private void rdoTimeAnalysisMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTimeAnalysisMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoTimeAnalysisMouseExited
    /**
     * 売上別推移ラジオを出る。
     *
     * @param evt
     */
    private void rdoSaleShiftMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoSaleShiftMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoSaleShiftMouseExited
    /**
     * 分類別売上推移ラジオを出る。
     *
     * @param evt
     */
    private void rdoTypeShiftMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTypeShiftMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoTypeShiftMouseExited

    /**
     * Zチャートラジオを出る。
     *
     * @param evt
     */
    private void rdoZChartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoZChartMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoZChartMouseExited

    /**
     * 開始の対象期間コンボボックスの状態を変更する。
     *
     * @param evt
     */
    private void cmbTimePeriodStartItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTimePeriodStartItemStateChanged
        setInitDate();
    }//GEN-LAST:event_cmbTimePeriodStartItemStateChanged
    /**
     * 分類別集計ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoTypeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoTypeStateChanged
        if (this.rdoType.isSelected()) {
            this.cmbTimePeriodEnd.setEnabled(this.rdoType.isSelected());
            this.rdoClassAll.setEnabled(this.rdoType.isSelected());
            this.rdoClassTech.setEnabled(this.rdoType.isSelected());
            this.rdoClassItem.setEnabled(this.rdoType.isSelected());
            this.rdoMoney.setEnabled(!this.rdoType.isSelected());
            this.rdoPersonCount.setEnabled(!this.rdoType.isSelected());
            this.lblOuptCond.setEnabled(!this.rdoType.isSelected());
            this.lblStaff2.setEnabled(!this.rdoType.isSelected());
            this.cmbStaff2.setEnabled(!this.rdoType.isSelected());
            this.rdoTaxBlank2.setSelected(this.rdoType.isSelected());
        }
    }//GEN-LAST:event_rdoTypeStateChanged
    /**
     * 曜日別集計ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoWeekStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoWeekStateChanged
        if (this.rdoWeek.isSelected()) {
            this.cmbTimePeriodEnd.setEnabled(this.rdoWeek.isSelected());
            this.rdoClassAll.setEnabled(!this.rdoWeek.isSelected());
            this.rdoClassTech.setEnabled(!this.rdoWeek.isSelected());
            this.rdoClassItem.setEnabled(!this.rdoWeek.isSelected());
            this.rdoMoney.setEnabled(this.rdoWeek.isSelected());
            this.rdoPersonCount.setEnabled(this.rdoWeek.isSelected());
            this.lblStaff2.setEnabled(!this.rdoWeek.isSelected());
            this.cmbStaff2.setEnabled(!this.rdoWeek.isSelected());
            this.rdoTaxBlank2.setSelected(this.rdoWeek.isSelected());
            this.lblOuptCond.setEnabled(this.rdoWeek.isSelected());

        }
    }//GEN-LAST:event_rdoWeekStateChanged
    /**
     * 年ンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbMonthWayYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonthWayYearItemStateChanged

        int startYear = Integer.parseInt(this.cmbMonthWayYear.getSelectedItem().toString()) + 1;
        this.lblPeriodYearEnd2.setText(String.valueOf(startYear));
        String month = lblPeriodMonthEnd2.getText();
        int year;

        if (month != "") {
            year = 0;
        } else {
            year = Integer.parseInt(lblPeriodMonthEnd2.getText());
        }
        if (Integer.parseInt(cmbMonthWayMonth.getSelectedItem().toString()) == 1 && year == 0) {
            this.lblPeriodYearEnd2.setText(String.valueOf(startYear - 1));
        }

    }//GEN-LAST:event_cmbMonthWayYearItemStateChanged
    /**
     * 月コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbMonthWayMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonthWayMonthItemStateChanged
        int startMonth = Integer.parseInt(this.cmbMonthWayMonth.getSelectedItem().toString()) - 1;
        int startYear = Integer.parseInt(this.cmbMonthWayYear.getSelectedItem().toString());
        if (startMonth == 0) {
            startMonth = 12;
            startYear = Integer.parseInt(this.cmbMonthWayYear.getSelectedItem().toString());
            this.lblPeriodYearEnd2.setText(String.valueOf(startYear));
            lblPeriodMonthEnd2.setText(String.valueOf(startMonth));
        } else {
            lblPeriodMonthEnd2.setText(String.valueOf(startMonth));
        }
        if (startYear == Integer.parseInt(lblPeriodYearEnd2.getText()) && (startMonth >= Integer.parseInt(this.lblPeriodMonthEnd2.getText()))) {
            int endYear = Integer.parseInt(lblPeriodYearEnd2.getText()) + 1;
            this.lblPeriodYearEnd2.setText(String.valueOf(endYear));

        }
        if (startMonth == 12 && Integer.parseInt(this.lblPeriodMonthEnd2.getText()) == 12) {
            int endYear = Integer.parseInt(lblPeriodYearEnd2.getText());
            this.lblPeriodYearEnd2.setText(String.valueOf(endYear - 1));
        }
    }//GEN-LAST:event_cmbMonthWayMonthItemStateChanged
    /**
     * 宣伝や広告の効果分析パネルを出る。
     *
     * @param evt
     */
    private void pnlTypeInfluenceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTypeInfluenceMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_pnlTypeInfluenceMouseExited

    /**
     * 対象期間の年コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodYearStart2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodYearStart2ItemStateChanged
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart2.getSelectedItem().toString()) + 1;
        this.lblPeriodYearEnd2.setText(String.valueOf(startYear));
        String month = lblPeriodMonthEnd2.getText();
        int year;
        if (month != "") {
            year = 0;
        } else {
            year = Integer.parseInt(lblPeriodMonthEnd2.getText());
        }
        if (Integer.parseInt(cmbTargetPeriodMonthStart2.getSelectedItem().toString()) == 1 && year == 0) {
            this.lblPeriodYearEnd2.setText(String.valueOf(startYear - 1));
        }
    }//GEN-LAST:event_cmbTargetPeriodYearStart2ItemStateChanged

    /**
     * 対象期間の月コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodMonthStart2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodMonthStart2ItemStateChanged
        selectDate();
    }//GEN-LAST:event_cmbTargetPeriodMonthStart2ItemStateChanged
    private void selectDate() {
        int startMonth = Integer.parseInt(this.cmbTargetPeriodMonthStart2.getSelectedItem().toString()) - 1;
        String a = this.cmbTargetPeriodYearStart2.getSelectedItem().toString();
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart2.getSelectedItem().toString());

        if (startMonth == 0) {
            startMonth = 12;
            startYear = Integer.parseInt(this.cmbTargetPeriodYearStart2.getSelectedItem().toString());
            this.lblPeriodYearEnd2.setText(String.valueOf(startYear));
            lblPeriodMonthEnd2.setText(String.valueOf(startMonth));
        } else {
            lblPeriodMonthEnd2.setText(String.valueOf(startMonth));
        }
        if (startYear == Integer.parseInt(lblPeriodYearEnd2.getText()) && (startMonth >= Integer.parseInt(this.lblPeriodMonthEnd2.getText()))) {
            int endYear = Integer.parseInt(lblPeriodYearEnd2.getText()) + 1;
            this.lblPeriodYearEnd2.setText(String.valueOf(endYear));

        }
        if (startMonth == 12 && Integer.parseInt(this.lblPeriodMonthEnd2.getText()) == 12) {
            int endYear = Integer.parseInt(lblPeriodYearEnd2.getText());
            this.lblPeriodYearEnd2.setText(String.valueOf(endYear - 1));
        }
    }
   /**
     * 月の対象期間コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodMonthStart1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodMonthStart1ItemStateChanged
        int startMonth = Integer.parseInt(this.cmbTargetPeriodMonthStart1.getSelectedItem().toString()) - 1;
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart1.getSelectedItem().toString());
        if (startMonth == 0) {
            startMonth = 12;
            startYear = Integer.parseInt(this.cmbTargetPeriodYearStart1.getSelectedItem().toString());
            this.lblPeriodYearEnd2.setText(String.valueOf(startYear));
            lblPeriodMonthEnd2.setText(String.valueOf(startMonth));
        } else {
            lblPeriodMonthEnd2.setText(String.valueOf(startMonth));
        }
        if (startYear == Integer.parseInt(lblPeriodYearEnd2.getText()) && (startMonth >= Integer.parseInt(this.lblPeriodMonthEnd2.getText()))) {
            int endYear = Integer.parseInt(lblPeriodYearEnd2.getText()) + 1;
            this.lblPeriodYearEnd2.setText(String.valueOf(endYear));

        }
        if (startMonth == 12 && Integer.parseInt(this.lblPeriodMonthEnd2.getText()) == 12) {
            int endYear = Integer.parseInt(lblPeriodYearEnd2.getText());
            this.lblPeriodYearEnd2.setText(String.valueOf(endYear - 1));
        }
    }//GEN-LAST:event_cmbTargetPeriodMonthStart1ItemStateChanged
    /**
     * 期間設定ボタンを押下。
     *
     * @param evt
     */
    private void btnPeriodSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPeriodSettingActionPerformed

        IPOPanel tgp = new IPOPanel();

        SwingUtil.openAnchorDialog(null, true, tgp, "期間設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        tgp = null;
        System.gc();
    }//GEN-LAST:event_btnPeriodSettingActionPerformed

    /**
     * アクティブ顧客分析ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoActiveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoActiveStateChanged
        this.lblReportName.setText("月別のお客様の状態を確認できます。");
    }//GEN-LAST:event_rdoActiveStateChanged
    /**
     * アクティブ顧客分析ラジオを出る。
     *
     * @param evt
     */
    private void rdoActiveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoActiveMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoActiveMouseExited
    /**
     * アクティブ顧客分析の対象期間の月コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodMonthStart4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodMonthStart4ItemStateChanged

        int startMonth = Integer.parseInt(this.cmbTargetPeriodMonthStart4.getSelectedItem().toString()) - 1;
        String a = this.cmbTargetPeriodYearStart4.getSelectedItem().toString();
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart4.getSelectedItem().toString());

        if (startMonth == 0) {
            startMonth = 12;
            startYear = Integer.parseInt(this.cmbTargetPeriodYearStart4.getSelectedItem().toString());
            this.lblPeriodYearEnd4.setText(String.valueOf(startYear));
            lblPeriodMonthEnd4.setText(String.valueOf(startMonth));
        } else {
            lblPeriodMonthEnd4.setText(String.valueOf(startMonth));
        }
        if (startYear == Integer.parseInt(lblPeriodYearEnd4.getText()) && (startMonth >= Integer.parseInt(this.lblPeriodMonthEnd4.getText()))) {
            int endYear = Integer.parseInt(lblPeriodYearEnd4.getText()) + 1;
            this.lblPeriodYearEnd4.setText(String.valueOf(endYear));

        }
        if (startMonth == 12 && Integer.parseInt(this.lblPeriodMonthEnd4.getText()) == 12) {
            int endYear = Integer.parseInt(lblPeriodYearEnd4.getText());
            this.lblPeriodYearEnd4.setText(String.valueOf(endYear - 1));
        }
    }//GEN-LAST:event_cmbTargetPeriodMonthStart4ItemStateChanged

    /**
     * アクティブ顧客分析の対象期間の年コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodYearStart4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodYearStart4ItemStateChanged
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart4.getSelectedItem().toString()) + 1;
        this.lblPeriodYearEnd4.setText(String.valueOf(startYear));
        String month = lblPeriodMonthEnd4.getText();
        int year;
        if (month != "") {
            year = 0;
        } else {
            year = Integer.parseInt(lblPeriodMonthEnd4.getText());
        }
        if (Integer.parseInt(cmbTargetPeriodMonthStart4.getSelectedItem().toString()) == 1 && year == 0) {
            this.lblPeriodYearEnd4.setText(String.valueOf(startYear - 1));
        }
    }//GEN-LAST:event_cmbTargetPeriodYearStart4ItemStateChanged

    /**
     * サイクル分析の対象期間の月コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodMonthStart3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodMonthStart3ItemStateChanged
        int startMonth = Integer.parseInt(this.cmbTargetPeriodMonthStart3.getSelectedItem().toString()) - 1;
        String a = this.cmbTargetPeriodYearStart3.getSelectedItem().toString();
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart3.getSelectedItem().toString());

        if (startMonth == 0) {
            startMonth = 12;
            startYear = Integer.parseInt(this.cmbTargetPeriodYearStart3.getSelectedItem().toString());
            this.lblPeriodYearEnd3.setText(String.valueOf(startYear));
            lblPeriodMonthEnd3.setText(String.valueOf(startMonth));
        } else {
            lblPeriodMonthEnd3.setText(String.valueOf(startMonth));
        }
        if (startYear == Integer.parseInt(lblPeriodYearEnd3.getText()) && (startMonth >= Integer.parseInt(this.lblPeriodMonthEnd3.getText()))) {
            int endYear = Integer.parseInt(lblPeriodYearEnd3.getText()) + 1;
            this.lblPeriodYearEnd3.setText(String.valueOf(endYear));

        }
        if (startMonth == 12 && Integer.parseInt(this.lblPeriodMonthEnd3.getText()) == 12) {
            int endYear = Integer.parseInt(lblPeriodYearEnd3.getText());
            this.lblPeriodYearEnd3.setText(String.valueOf(endYear - 1));
        }
    }//GEN-LAST:event_cmbTargetPeriodMonthStart3ItemStateChanged
    /**
     * 来店サイクル分析の対象期間の年コンボボックスの月方針の状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetPeriodYearStart3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetPeriodYearStart3ItemStateChanged
        int startYear = Integer.parseInt(this.cmbTargetPeriodYearStart3.getSelectedItem().toString()) + 1;
        this.lblPeriodYearEnd3.setText(String.valueOf(startYear));
        String month = lblPeriodMonthEnd3.getText();
        int year;
        if (month != "") {
            year = 0;
        } else {
            year = Integer.parseInt(lblPeriodMonthEnd3.getText());
        }
        if (Integer.parseInt(cmbTargetPeriodMonthStart3.getSelectedItem().toString()) == 1 && year == 0) {
            this.lblPeriodYearEnd3.setText(String.valueOf(startYear - 1));
        }
    }//GEN-LAST:event_cmbTargetPeriodYearStart3ItemStateChanged
    /**
     * 再来分析表の状態を変更する。
     *
     * @param evt
     */
    private void rdoReappearanceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoReappearanceStateChanged
        this.lblReportName.setText("月別のお客様の来店サイクルを確認できます。");
    }//GEN-LAST:event_rdoReappearanceStateChanged
    /**
     * 再来分析表ラジオを出る。
     *
     * @param evt
     */
    private void rdoReappearanceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoReappearanceMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoReappearanceMouseExited
    /**
     * 再来算出期間の状態を変更する。
     *
     * @param evt
     */
    private void cmbReappearanceSpanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbReappearanceSpanItemStateChanged
        changeTargetDate();
    }//GEN-LAST:event_cmbReappearanceSpanItemStateChanged
    /**
     * 再来算出期間の月コンボボックスの状態を変更する。
     *
     * @param evt
     */
    private void cmbTargetyMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetyMonthItemStateChanged
        changeTargetDate();
    }//GEN-LAST:event_cmbTargetyMonthItemStateChanged

    /**
     * 再来分析表の状態を変更。
     *
     * @param evt
     */
    private void rdoReappearStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoReappearStateChanged
        this.lblReportName.setText("お店の再来店率を確認できます。");
    }//GEN-LAST:event_rdoReappearStateChanged
    /**
     * 再来分析表ラジオを出る。
     *
     * @param evt
     */
    private void rdoReappearMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoReappearMouseExited
        this.lblReportName.setText("");
    }//GEN-LAST:event_rdoReappearMouseExited

    /**
     * 日数方式ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoDayWayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoDayWayItemStateChanged

        disableControl();
    }//GEN-LAST:event_rdoDayWayItemStateChanged
    /**
     * 月方針ラジオの状態を変更する。
     *
     * @param evt
     */
    private void rdoMonthWayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoMonthWayItemStateChanged

        disableControl();

    }//GEN-LAST:event_rdoMonthWayItemStateChanged

    /**
     * コース詳細ラジオを押下。
     *
     * @param evt
     */
    private void rdoCourseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCourseReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoCourseReportActionPerformed
    /**
     * 消化一覧ラジオを押下。
     *
     * @param evt
     */
    private void rdoConsumptionCourseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoConsumptionCourseReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoConsumptionCourseReportActionPerformed
    /**
     * 業務報告ラジオを押下。
     *
     * @param evt
     */
    private void rdoBusinessReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBusinessReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoBusinessReportActionPerformed

    /**
     * 技術詳細ラジオを押下。
     *
     * @param evt
     */
    private void rdoTechnicalReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTechnicalReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoTechnicalReportActionPerformed
    /**
     * 商品詳細ラジオを押下。
     *
     * @param evt
     */
    private void rdoItemReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoItemReportActionPerformed
        this.initDisplayControl();
    }//GEN-LAST:event_rdoItemReportActionPerformed

    private void pnlTypeInfluence1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTypeInfluence1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlTypeInfluence1MouseMoved

    private void pnlTypeInfluence1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTypeInfluence1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlTypeInfluence1MouseExited

    private void btnExcelReport3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelReport3ActionPerformed
        // TODO add your handling code here:
        if (!SystemInfo.checkAuthorityPassword(260)) {
            return;
        }

        if (checkReappearance()) {

            btnExcelReport3.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                // （再来分析表、来店サイクル分析、アクティブ顧客分析）台帳出力処理
                exportReappearance();
            } catch (Exception ex) {
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_btnExcelReport3ActionPerformed

    private void cmbTargetYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTargetYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTargetYearActionPerformed

    private void cmbTarget1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTarget1ActionPerformed
        
    }//GEN-LAST:event_cmbTarget1ActionPerformed

    private void cmbTarget1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTarget1ItemStateChanged
        // start add 20130805 nakhoa 役務機能使用有無の制御追加
        if(!isLoading){
            rdoCourseReport.setVisible(false);
            rdoConsumptionCourseReport.setVisible(false);
            if(cmbTarget1.getSelectedItem() != null){
                if(cmbTarget1.getSelectedItem() instanceof MstShop){
                    MstShop shop = new MstShop();
                    shop = (MstShop)cmbTarget1.getSelectedItem();
                    if(shop.getCourseFlag() == 1){
                        rdoCourseReport.setVisible(true);
                        rdoConsumptionCourseReport.setVisible(true);
                    }
                }
            }
        }
        // end add 20130805 nakhoa 役務機能使用有無の制御追加
    }//GEN-LAST:event_cmbTarget1ItemStateChanged

    private void initDisplayControl() {
        if (rdoBusinessReport.isSelected()) {
            rdoMainStaff.setSelected(true);
            rdoTechStaff.setEnabled(false);
            lblNewVisit.setEnabled(true);
            rdoNewVisitCurrent.setEnabled(true);
            rdoNewVisitAll.setEnabled(true);
        } else {
            rdoTechStaff.setEnabled(true);
            lblNewVisit.setEnabled(false);
            rdoNewVisitCurrent.setEnabled(false);
            rdoNewVisitAll.setEnabled(false);
        }
    }

    private void setInitDate() {
        if (this.cmbTimePeriodStart.getDate() == null) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.cmbTimePeriodStart.getDate());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        this.cmbTimePeriodStart.setDate(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, -0);
        this.cmbTimePeriodEnd.setDate(cal.getTime());

    }

    private void changeTargetDate() {

        // 再来期間
        int reappearanceCount = getReappearanceSpan();

        // 集計期間を求める
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(Calendar.YEAR, Integer.parseInt(this.cmbTargetYear.getSelectedItem().toString()));
        } catch (Exception e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmbTargetYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
            return;
        }

        cal.set(Calendar.MONTH, Integer.parseInt(cmbTargetyMonth.getSelectedItem().toString()) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        java.util.Date targetDate = null;
        try {
            targetDate = cal.getTime();
        } catch (Exception e) {
        }

        if (targetDate == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "対象期間"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmbTargetYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
            return;
        }

        // ○ヶ月前の月初
        Calendar calculationStart = Calendar.getInstance();
        calculationStart.setTime(targetDate);
        if (reappearanceCount == 45) {
            // 45日再来は2ヶ月前の15日
            calculationStart.add(Calendar.MONTH, -2);
            calculationStart.set(Calendar.DAY_OF_MONTH, 15);
        } else {
            // それ以外は○ヶ月前の1日
            calculationStart.add(Calendar.MONTH, (reappearanceCount * -1));
            calculationStart.set(Calendar.DAY_OF_MONTH, 1);
        }

        // 再来対象期間
        // 開始日は集計開始日と同じ
        Calendar targetStart = Calendar.getInstance();
        targetStart.setTime(calculationStart.getTime());

        Calendar targetEnd = Calendar.getInstance();
        targetEnd.setTime(targetStart.getTime());
        if (reappearanceCount == 45) {
            // 45日再来の終了は、翌月の14日
            targetEnd.add(Calendar.MONTH, 1);
            targetEnd.set(Calendar.DAY_OF_MONTH, 14);
        } else {
            // 終了は開始月の月末
            targetEnd.add(Calendar.MONTH, 1);
            targetEnd.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 期間設定を締日基準にする
        if (reappearanceCount != 45) {
            this.resetSpan(targetStart, targetEnd);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        this.lblTargetDate.setText(sdf.format(targetStart.getTime()) + " ～ " + sdf.format(targetEnd.getTime()));
         selectDate();
    }

    private boolean checkResponse() {
        // 対象期間 必須チェック
        if (this.cmbTargetPeriodStart4.getDate() == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "集計期間"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            this.cmbTargetPeriodStart4.requestFocusInWindow();
            return false;
        }

        if (this.cmbTargetPeriodEnd4.getDate() == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "集計期間"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            this.cmbTargetPeriodEnd4.requestFocusInWindow();
            return false;
        }

        //固定回数 必須チェック
        if (txtFixedCount2.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "固定回数"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtFixedCount2.requestFocusInWindow();
            return false;
        }

        //固定回数 数値チェック
        if (!txtFixedCount2.getText().equals("") && !CheckUtil.isNumber(txtFixedCount2.getText())) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "固定回数"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtFixedCount2.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void printResponse() {
        ResponseReport rr = new ResponseReport();
        Calendar cal = Calendar.getInstance();
        //グループ
        if (cmbTarget4.getSelectedItem() instanceof MstGroup) {
            MstGroup mg = (MstGroup) cmbTarget4.getSelectedItem();
            rr.setTargetName(mg.getGroupName());
            rr.setShopIDList(mg.getShopIDListAll());
        } //店舗
        else if (cmbTarget4.getSelectedItem() instanceof MstShop) {
            MstShop ms = (MstShop) cmbTarget4.getSelectedItem();
            rr.setTargetName(ms.getShopName());
            rr.setShopIDList(ms.getShopID().toString());
        }

        rr.setTargetStartDate(dateToGregorianCalendar(this.cmbTargetPeriodStart4.getDate()));
        rr.setTargetEndDate(dateToGregorianCalendar(this.cmbTargetPeriodEnd4.getDate()));
        rr.setFixedCount(new Integer(this.txtFixedCount2.getText()));

        rr.setReportData();

        // データが存在しない場合には出力を行わない
        if (0 < rr.size()) {
            // 帳票を出力する
            rr.print();
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1112, "表示レコード無し"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private GregorianCalendar dateToGregorianCalendar(java.util.Date date) {
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(date);
        return gCal;
    }

    private void doOutput() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        ReportLogic logic = new ReportLogic();
        ReportParameterBean paramBean = new ReportParameterBean();
        paramBean.setMainReportType(ReportParameterBean.MAIN_REPORT_STAFF);

        //グループ
        if (cmbTarget3.getSelectedItem() instanceof MstGroup) {
            MstGroup mg = (MstGroup) cmbTarget3.getSelectedItem();
            paramBean.setTargetName(mg.getGroupName());
            paramBean.setShopIDList(mg.getShopIDListAll());
        } //店舗
        else if (cmbTarget3.getSelectedItem() instanceof MstShop) {
            MstShop ms = (MstShop) cmbTarget3.getSelectedItem();
            paramBean.setTargetName(ms.getShopName());
            paramBean.setShopIDList(ms.getShopID().toString());
        }

        //対象となる店舗が存在しない場合
        if (paramBean.getShopIDList().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 主担当者
        if (cmbStaff3.getSelectedIndex() > 0) {
            paramBean.setStaffId(((MstStaff) cmbStaff3.getSelectedItem()).getStaffID());
            paramBean.setStaffName(((MstStaff) cmbStaff3.getSelectedItem()).getFullStaffName());
        } else {
            paramBean.setStaffId(null);
            paramBean.setStaffName("全体");
        }

        // 再来算出期間
        int reappearanceCount = getReappearanceSpan();
        paramBean.setReappearanceCountMessage(this.cmbReappearanceSpan.getSelectedItem().toString());

        // 固定回数
        int fixedCount = 5;
        try {
            fixedCount = Integer.parseInt(this.txtFixedCount1.getText());
        } catch (NumberFormatException e) {
        }
        paramBean.setFixedCount(fixedCount);

        // 人数条件
        String numberCondition = "純客数";
        if (this.rdoSimpleCount.isSelected()) {
            numberCondition = "延べ客数";
        }
        paramBean.setNumberCondition(numberCondition);

        // 技術分類再来
        paramBean.setSameTechnicClass(this.rdoTechSameCount.isSelected());

        // 集計期間を求める
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(Calendar.YEAR, Integer.parseInt(this.cmbTargetYear.getSelectedItem().toString()));
        } catch (Exception e) {
        }

        cal.set(Calendar.MONTH, Integer.parseInt(cmbTargetyMonth.getSelectedItem().toString()) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        java.util.Date targetDate = cal.getTime();

        // ○ヶ月前の月初
        Calendar calculationStart = Calendar.getInstance();
        calculationStart.setTime(targetDate);
        if (reappearanceCount == 45) {
            // 45日再来は2ヶ月前の15日
            calculationStart.add(Calendar.MONTH, -2);
            calculationStart.set(Calendar.DAY_OF_MONTH, 15);
        } else {
            // それ以外は○ヶ月前の1日
            calculationStart.add(Calendar.MONTH, (reappearanceCount * -1));
            calculationStart.set(Calendar.DAY_OF_MONTH, 1);
        }

        // 選んだ月の月末
        Calendar calculationEnd = Calendar.getInstance();
        calculationEnd.set(Integer.parseInt(this.cmbTargetYear.getSelectedItem().toString()), Integer.parseInt(cmbTargetyMonth.getSelectedItem().toString()), 1);// 選択月の翌月にして
        calculationEnd.add(Calendar.DAY_OF_MONTH, -1); // １日戻す

        // 再来対象期間
        // 開始日は集計開始日と同じ
        Calendar targetStart = Calendar.getInstance();
        targetStart.setTime(calculationStart.getTime());

        Calendar targetEnd = Calendar.getInstance();
        targetEnd.setTime(calculationStart.getTime());
        if (reappearanceCount == 45) {
            // 45日再来の終了は、翌月の14日
            targetEnd.add(Calendar.MONTH, 1);
            targetEnd.set(Calendar.DAY_OF_MONTH, 14);
        } else {
            // 終了は開始月の月末
            targetEnd.add(Calendar.MONTH, 1);         // 翌月にして
            targetEnd.add(Calendar.DAY_OF_MONTH, -1); // １日戻す
        }

        // 期間設定を締日基準にする
        if (reappearanceCount != 45) {
            this.resetSpan(targetStart, targetEnd);
            this.resetSpan(calculationStart, calculationEnd);
            calculationStart.setTime(targetStart.getTime());
        }

        paramBean.setTargetStartDate(sdf.format(targetStart.getTime()));
        paramBean.setTargetEndDate(sdf.format(targetEnd.getTime()));
        paramBean.setTargetStartDateObj(targetStart.getTime());
        paramBean.setTargetEndDateObj(targetEnd.getTime());

        paramBean.setCalculationStartDate(sdf.format(calculationStart.getTime()));
        paramBean.setCalculationEndDate(sdf.format(calculationEnd.getTime()));
        paramBean.setCalculationStartDateObj(calculationStart.getTime());
        paramBean.setCalculationEndDateObj(calculationEnd.getTime());

        paramBean.setNumberCondition(numberCondition);
        //nhanvt start 20141003 Bug #31167
        paramBean.setCategoryIDList("");
        //nhanvt end 20141003 Bug #31167
        //出力処理
        boolean logicResult = true;
        try {

            logicResult = logic.outStaffReportReappearanceOnePeriod(paramBean);

        } catch (Exception e) {

            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        // エラー時
        if (!logicResult) {
            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void disableControl() {
        if (rdoSaleShift.isSelected() || rdoTypeShift.isSelected() || rdoZChart.isSelected()) {
            this.lblDayWay.setEnabled(false);
            this.lblMonthWay.setEnabled(false);
            this.lblTargetPeriod2.setEnabled(false);
            this.lblStaff2.setEnabled(false);
            this.cmbStaff2.setEnabled(false);
            this.lblTargetPeriod3.setEnabled(true);
            this.rdoMoney.setEnabled(false);
            this.rdoPersonCount.setEnabled(false);
            this.lblOuptCond.setEnabled(false);
        }
        if (this.rdoDayShift.isSelected()) {
            this.lblTargetPeriod2.setEnabled(false);
            this.lblSum.setEnabled(false);
            this.lblTimePeriod.setEnabled(false);
            this.techItemClassLabel.setEnabled(false);
            this.lblSumMethod.setEnabled(false);
            this.lblTimeCond.setEnabled(false);
            this.lblOuptCond.setEnabled(false);
            this.lblTargetPeriod3.setEnabled(false);
            this.lblDayWay.setEnabled(false);
            this.cmbStaff2.setEnabled(true);
            this.lblStaff2.setEnabled(true);
            if (this.rdoDayWay.isSelected()) {
                this.lblDayWay.setEnabled(true);
                this.lblMonthWay.setEnabled(false);
            }
            if (this.rdoMonthWay.isSelected()) {
                this.lblDayWay.setEnabled(false);
                this.lblMonthWay.setEnabled(true);
            }

        }
        if (this.rdoMonthShift.isSelected()) {
            this.lblTargetPeriod2.setEnabled(false);
            this.lblSum.setEnabled(false);
            this.lblTimePeriod.setEnabled(false);
            this.techItemClassLabel.setEnabled(false);
            this.lblSumMethod.setEnabled(false);
            this.lblTimeCond.setEnabled(false);
            this.lblOuptCond.setEnabled(false);
            this.lblTargetPeriod3.setEnabled(false);
            this.lblDayWay.setEnabled(false);
            this.lblMonthWay.setEnabled(false);
            this.lblTargetPeriod2.setEnabled(true);
            this.cmbStaff2.setEnabled(true);
            this.lblStaff2.setEnabled(true);
        }

        if (this.rdoTimeAnalysis.isSelected()) {
            this.lblStaff2.setEnabled(false);
            this.cmbStaff2.setEnabled(false);
            this.lblTargetPeriod3.setEnabled(false);
        }
    }
    
      public StoreReportFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    /**
     * 業務報告画面用FocusTraversalPolicy
     */
    private class StoreReportFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         * aComponStoreReportFocusTraversalPolicy。 aContainer は aComponent
         * のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         *
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            if(aComponent.equals(rdoBusinessReport) ||
                    aComponent.equals(rdoTechnicalReport) ||
                    aComponent.equals(rdoItemReport) || 
                    aComponent.equals(rdoCourseReport) ||
                    aComponent.equals(rdoConsumptionCourseReport)){
                if(cmbTarget1.getItemCount() ==1){
                    return  cmbPeriodStart1;
                }
                return cmbTarget1;
            }else if(aComponent.equals(cmbTarget1)){
                return cmbPeriodStart1;
            }else if(aComponent.equals(cmbPeriodStart1)){
                return cmbPeriodEnd1;
            }else if(aComponent.equals(cmbPeriodEnd1)){
                if(rdoMainStaff.isSelected()){
                    return rdoMainStaff;
                }
                return rdoTechStaff;
            }else if(aComponent.equals(rdoMainStaff) ||
                    aComponent.equals(rdoTechStaff) ){
                return cmbStaff1;
             }else if(aComponent.equals(cmbStaff1)){
                if(rdoNewVisitCurrent.isSelected()){
                    return rdoNewVisitCurrent;
                }
                return rdoNewVisitAll;
              }else if( aComponent.equals(rdoNewVisitCurrent) || 
                       aComponent.equals(rdoNewVisitAll) ){
                if(rdoTaxUnit1.isSelected()){
                    return rdoTaxUnit1;
                }
                return rdoTaxBlank1;
             }else if( aComponent.equals(rdoTaxUnit1) || 
                       aComponent.equals(rdoTaxBlank1) ){
                if(rdoDayShift.isSelected()){
                    return rdoDayShift;
                } else if(rdoMonthShift.isSelected()){
                    return rdoMonthShift;
                } else if(rdoTimeAnalysis.isSelected()){
                    return rdoTimeAnalysis;
                } else if(rdoSaleShift.isSelected()){
                    return rdoSaleShift;
                } else if(rdoTypeShift.isSelected()){
                    return rdoTypeShift;
                }
                return rdoZChart; 
             }else if( aComponent.equals(rdoDayShift) || 
                       aComponent.equals(rdoMonthShift)){
                if(cmbTarget2.getItemCount() ==1){
                    if(cmbStaff2.isEnabled()){
                        return cmbStaff2;
                    }else{
                        if(rdoDayWay.isEnabled() && rdoDayWay.isSelected()){
                            return rdoDayWay;
                        }
                        else if(rdoMonthWay.isEnabled() && rdoMonthWay.isSelected()){
                            return rdoMonthWay;
                        }   
                    }
                }
                return cmbTarget2; 
             } else if( aComponent.equals(rdoTimeAnalysis))
             {
                if(rdoTaxUnit2.isEnabled() && rdoTaxUnit2.isSelected())
                {
                    return rdoTaxUnit2;
                }else if(rdoTaxBlank2.isEnabled() && rdoTaxBlank2.isSelected())
                {
                    return rdoTaxBlank2;
                }
             } else if(aComponent.equals(rdoTypeShift) ||  aComponent.equals(rdoSaleShift)||
                        aComponent.equals(rdoZChart)){
                 return cmbTargetPeriodYearStart2;
             }else if(aComponent.equals(cmbTarget2))
            {
                if(cmbStaff2.isEnabled()){
                        return cmbStaff2;
                    }else{
                        if(rdoDayWay.isEnabled() && rdoDayWay.isSelected()){
                            return rdoDayWay;
                        }
                        else if(rdoMonthWay.isEnabled() && rdoMonthWay.isSelected()){
                            return rdoMonthWay;
                        }   
                    }
            } else if(aComponent.equals(cmbStaff2)){
                 if(rdoDayWay.isEnabled() && rdoDayWay.isSelected()){
                            return rdoDayWay;
                 }else if(rdoMonthWay.isEnabled() && rdoMonthWay.isSelected()){
                            return cmbMonthWayYear;
                 }
                 return cmbTargetPeriodYearStart1;
            } else if(aComponent.equals(rdoDayWay)){
                return cmbDayWayStartDate;
            }else if(aComponent.equals(cmbDayWayStartDate)){
                return cmbDayWayEndDate;
            }else if(aComponent.equals(cmbDayWayEndDate)){
                if(rdoTaxUnit2.isEnabled() && rdoTaxUnit2.isSelected())
                {
                    return rdoTaxUnit2;
                }else if(rdoTaxBlank2.isEnabled() && rdoTaxBlank2.isSelected())
                {
                    return rdoTaxBlank2;
                }
            }else if(aComponent.equals(rdoTaxUnit2) || aComponent.equals(rdoTaxBlank2) ){
                if(rdoDay.isEnabled() && rdoDay.isSelected()){
                    return rdoDay;
                }else if(rdoType.isEnabled() && rdoType.isSelected()){
                    return rdoType;
                }else if(rdoWeek.isEnabled() && rdoWeek.isSelected()){
                    return rdoWeek;
                }else if(rdoWeek.isEnabled() && rdoWeek.isSelected()){
                    return rdoWeek;
                }else if(cmbTimePeriodStart.isEnabled()){
                    return cmbTimePeriodStart;
                }else if(cmbTimePeriodEnd.isEnabled()){
                    return cmbTimePeriodEnd;
                }else if(rdoClassAll.isEnabled() && rdoClassAll.isSelected()){
                    return rdoClassAll;
                }else if(rdoClassTech.isEnabled() && rdoClassTech.isSelected()){
                    return rdoClassTech;
                }else if(rdoClassItem.isEnabled() && rdoClassItem.isSelected()){
                    return rdoClassItem;
                }else if(rdoVisitTime.isEnabled() && rdoVisitTime.isSelected()){
                    return rdoVisitTime;
                }else if(rdoStartTime.isEnabled() && rdoStartTime.isSelected()){
                    return rdoStartTime;
                }else if(rdoLeaveTime.isEnabled() && rdoLeaveTime.isSelected()){
                    return rdoLeaveTime;
                }else if(rdoTimeFixed.isEnabled() && rdoTimeFixed.isSelected()){
                    return rdoTimeFixed;
                }else if(rdoTimeCustom.isEnabled() && rdoTimeCustom.isSelected()){
                    return rdoTimeCustom;
                }else if(rdoMoney.isEnabled() && rdoMoney.isSelected()){
                    return rdoMoney;
                }else if(rdoPersonCount.isEnabled() && rdoPersonCount.isSelected()){
                    return rdoPersonCount;
                }else if(rdoReappear.isEnabled() && rdoReappear.isSelected()){
                    return rdoReappear;
                }else if(rdoReappearance.isEnabled() && rdoReappearance.isSelected()){
                    return rdoReappearance;
                }
                return rdoActive;
            } else if( aComponent.equals(rdoMonthWay)){
                return cmbMonthWayYear;
            } else if( aComponent.equals(cmbMonthWayYear)){
                return cmbMonthWayMonth;
            }else if( aComponent.equals(cmbMonthWayMonth)){
               if(rdoTaxUnit2.isEnabled() && rdoTaxUnit2.isSelected())
                {
                    return rdoTaxUnit2;
                }else if(rdoTaxBlank2.isEnabled() && rdoTaxBlank2.isSelected())
                {
                    return rdoTaxBlank2;
                }
            }else if( aComponent.equals(cmbTargetPeriodYearStart1)){
                return cmbTargetPeriodMonthStart1;
            }else if( aComponent.equals(cmbTargetPeriodMonthStart1)){
                return cmbTargetPeriodYearEnd1;
            }else if( aComponent.equals(cmbTargetPeriodYearEnd1)){
                return cmbTargetPeriodMonthEnd1;
            }else if( aComponent.equals(cmbTargetPeriodMonthEnd1)){
               if(rdoTaxUnit2.isEnabled() && rdoTaxUnit2.isSelected())
                {
                    return rdoTaxUnit2;
                }else if(rdoTaxBlank2.isEnabled() && rdoTaxBlank2.isSelected())
                {
                    return rdoTaxBlank2;
                }
            }else if( aComponent.equals(cmbTargetPeriodYearStart2)){
                return cmbTargetPeriodMonthStart2;
            }else if( aComponent.equals(cmbTargetPeriodMonthStart2)){
                if(rdoTaxUnit2.isEnabled() && rdoTaxUnit2.isSelected())
                {
                    return rdoTaxUnit2;
                }else if(rdoTaxBlank2.isEnabled() && rdoTaxBlank2.isSelected())
                {
                    return rdoTaxBlank2;
                }
            }else if( aComponent.equals(rdoReappear)){
                if( cmbTarget3.getItemCount() ==1)
                {
                    return cmbStaff3;
                }
                return cmbTarget3;
            }else if( aComponent.equals(cmbTarget3)){
                return cmbStaff3;
            }else if( aComponent.equals(cmbStaff3)){
                return cmbTargetYear;
            }else if( aComponent.equals(cmbTargetYear)){
                return cmbTargetyMonth;
            }else if( aComponent.equals(cmbTargetyMonth)){
                return cmbReappearanceSpan;
            }else if( aComponent.equals(cmbReappearanceSpan)){
                return txtFixedCount1;
            }else if( aComponent.equals(txtFixedCount1)){
                if(rdoSimpleCount.isSelected()){
                    return rdoSimpleCount;
                }
                return rdoTotalCount;
            }else if( aComponent.equals(rdoSimpleCount) || aComponent.equals(rdoTotalCount)  ){
                 if(rdoTechOtherCount.isSelected()){
                    return rdoTechOtherCount;
                }
                return rdoTechSameCount;
                
            }else if( aComponent.equals(rdoTechOtherCount) || aComponent.equals(rdoTechSameCount)  ){
                if(cmbTarget4.getItemCount() == 1){
                    return cmbTargetPeriodStart4;
                }
                return cmbTarget4;
            }else if( aComponent.equals(cmbTargetPeriodStart4)  ){
                return cmbTargetPeriodEnd4;
            }else if( aComponent.equals(cmbTargetPeriodEnd4)  ){
                return txtFixedCount2;
            }else if( aComponent.equals(rdoReappearance)  ){
                return cmbTargetPeriodYearStart3;
            }else if( aComponent.equals(cmbTargetPeriodYearStart3)  ){
                return cmbTargetPeriodMonthStart3;
            }else if( aComponent.equals(cmbTargetPeriodMonthStart3)  ){
               if(cmbTarget4.getItemCount() == 1){
                    return cmbTargetPeriodStart4;
                }
                return cmbTarget4;
            }else if (aComponent.equals(rdoActive)){
                return cmbTargetPeriodYearStart4;
            }else if (aComponent.equals(cmbTargetPeriodYearStart4)){
                return cmbTargetPeriodMonthStart4;
            }else if (aComponent.equals(cmbTargetPeriodMonthStart4)){
                if(cmbTarget4.getItemCount() == 1){
                    return cmbTargetPeriodStart4;
                }
                return cmbTarget4;
            }else if(aComponent.equals(cmbTarget4)){
                return cmbTargetPeriodStart4;
            } else if(aComponent.equals(rdoDay)){
                return cmbTimePeriodStart;
            } else if(aComponent.equals(cmbTimePeriodStart)  ){
                if(rdoDay.isSelected())
                {
                    if (rdoVisitTime.isSelected()){
                        return rdoVisitTime;
                    }else if(rdoStartTime.isSelected()){
                            return rdoStartTime;
                    }   
                    return rdoLeaveTime;
                }
                return cmbTimePeriodEnd;
            } else if(aComponent.equals(rdoVisitTime) || aComponent.equals(rdoStartTime) || aComponent.equals(rdoLeaveTime)){
                if( rdoTimeFixed.isEnabled() && rdoTimeFixed.isSelected())
                {
                    return rdoTimeFixed;
                }
                else if( rdoTimeCustom.isEnabled() && rdoTimeCustom.isSelected())
                {
                    return rdoTimeCustom;
                }
                else if( rdoMoney.isEnabled() && rdoMoney.isSelected())
                {
                    return rdoMoney;
                }
                else if( rdoPersonCount.isEnabled() && rdoPersonCount.isSelected())
                {
                    return rdoPersonCount;
                }
                return rdoReappear;
            } else if(aComponent.equals(rdoTimeFixed) || aComponent.equals(rdoTimeCustom) ){
                if( rdoMoney.isEnabled() && rdoMoney.isSelected())
                {
                    return rdoMoney;
                }
                else if( rdoPersonCount.isEnabled() && rdoPersonCount.isSelected())
                {
                    return rdoPersonCount;
                }
                return rdoReappear;
                
            } else if(aComponent.equals(rdoMoney) || aComponent.equals(rdoPersonCount)){
                return rdoReappear;
            }else if(aComponent.equals(rdoType) || aComponent.equals(rdoWeek)  ){
                return cmbTimePeriodStart;
            }
            else if ( aComponent.equals(cmbTimePeriodEnd) && rdoType.isSelected())
            {
                if(rdoClassAll.isSelected()) {
                    return  rdoClassAll;
                }else if(rdoClassTech.isSelected()){
                    return rdoClassTech;
                }
                return rdoClassItem;
            }else if ( aComponent.equals(rdoClassAll) || aComponent.equals(rdoClassTech) || aComponent.equals(rdoClassItem))
            {
               if (rdoVisitTime.isSelected()){
                        return rdoVisitTime;
               }else if(rdoStartTime.isSelected()){
                        return rdoStartTime;
               }   
                       return rdoLeaveTime;
            } else if ( aComponent.equals(cmbTimePeriodEnd) && rdoWeek.isSelected())
            {
                if (rdoVisitTime.isSelected()){
                      return rdoVisitTime;
               }else if(rdoStartTime.isSelected()){
                      return rdoStartTime;
               }   
                     return rdoLeaveTime;
            }
            
            return this.getStartComponent();
        }
        private Component getStartComponent() 
        {
            if(rdoBusinessReport.isSelected()){
                return rdoBusinessReport;
            }else if(rdoTechnicalReport.isSelected()){
                return rdoTechnicalReport;
            }else if(rdoItemReport.isSelected()){
                return rdoItemReport;
            }else if(rdoCourseReport.isSelected()){
                return rdoCourseReport;
            }
            return rdoConsumptionCourseReport;
                
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
           
            return this.getStartComponent();
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
            return this.getStartComponent();
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
            return this.getStartComponent();
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
            return this.getStartComponent();
        }
        @Override
        public Component getLastComponent(Container aContainer) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup aggregateGroup;
    private javax.swing.ButtonGroup aggregationGroup;
    private javax.swing.JButton btnAnlysis;
    private javax.swing.JButton btnExcelReport1;
    private javax.swing.JButton btnExcelReport2;
    private javax.swing.JButton btnExcelReport3;
    private javax.swing.JButton btnExcelReport4;
    private javax.swing.ButtonGroup btnPerformance;
    private javax.swing.JButton btnPeriodSetting;
    private javax.swing.JButton btnPurposeSetting;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.ButtonGroup buttonGroup8;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbDayWayEndDate;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbDayWayStartDate;
    private javax.swing.JComboBox cmbMonthWayMonth;
    private javax.swing.JComboBox cmbMonthWayYear;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbPeriodEnd1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbPeriodStart1;
    private javax.swing.JComboBox cmbReappearanceSpan;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbStaff1;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbStaff2;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbStaff3;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbTarget1;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbTarget2;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbTarget3;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbTarget4;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEnd4;
    private javax.swing.JComboBox cmbTargetPeriodMonthEnd1;
    private javax.swing.JComboBox cmbTargetPeriodMonthStart1;
    private javax.swing.JComboBox cmbTargetPeriodMonthStart2;
    private javax.swing.JComboBox cmbTargetPeriodMonthStart3;
    private javax.swing.JComboBox cmbTargetPeriodMonthStart4;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStart4;
    private javax.swing.JComboBox cmbTargetPeriodYearEnd1;
    private javax.swing.JComboBox cmbTargetPeriodYearStart1;
    private javax.swing.JComboBox cmbTargetPeriodYearStart2;
    private javax.swing.JComboBox cmbTargetPeriodYearStart3;
    private javax.swing.JComboBox cmbTargetPeriodYearStart4;
    private javax.swing.JComboBox cmbTargetYear;
    private javax.swing.JComboBox cmbTargetyMonth;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTimePeriodEnd;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTimePeriodStart;
    private javax.swing.ButtonGroup countGroup;
    private javax.swing.ButtonGroup customerGroup;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel label100;
    private javax.swing.JLabel label101;
    private javax.swing.JLabel lable101;
    private javax.swing.JLabel lable104;
    private javax.swing.JLabel lblCountCondition;
    private javax.swing.JLabel lblDayWay;
    private javax.swing.JLabel lblFixedCount1;
    private javax.swing.JLabel lblFixedCount2;
    private javax.swing.JLabel lblMinFixedCount1;
    private javax.swing.JLabel lblMinFixedCount2;
    private javax.swing.JLabel lblMonthWay;
    private javax.swing.JLabel lblMonthWayMonth;
    private javax.swing.JLabel lblMonthWayYear;
    private javax.swing.JLabel lblNewVisit;
    private javax.swing.JLabel lblOuptCond;
    private javax.swing.JLabel lblPeriod1;
    private javax.swing.JLabel lblPeriodMonthEnd;
    private javax.swing.JLabel lblPeriodMonthEnd1;
    private javax.swing.JLabel lblPeriodMonthEnd2;
    private javax.swing.JLabel lblPeriodMonthEnd3;
    private javax.swing.JLabel lblPeriodMonthEnd4;
    private javax.swing.JLabel lblPeriodMonthStart;
    private javax.swing.JLabel lblPeriodMonthStart1;
    private javax.swing.JLabel lblPeriodMonthStart3;
    private javax.swing.JLabel lblPeriodMonthStart4;
    private javax.swing.JLabel lblPeriodYearEnd;
    private javax.swing.JLabel lblPeriodYearEnd1;
    private javax.swing.JLabel lblPeriodYearEnd2;
    private javax.swing.JLabel lblPeriodYearEnd3;
    private javax.swing.JLabel lblPeriodYearEnd4;
    private javax.swing.JLabel lblPeriodYearStart;
    private javax.swing.JLabel lblPeriodYearStart1;
    private javax.swing.JLabel lblPeriodYearStart3;
    private javax.swing.JLabel lblPeriodYearStart4;
    private javax.swing.JLabel lblReportName;
    private javax.swing.JLabel lblSpe1;
    private javax.swing.JLabel lblSpe3;
    private javax.swing.JLabel lblSpe4;
    private javax.swing.JLabel lblSpe5;
    private javax.swing.JLabel lblSpe6;
    private javax.swing.JLabel lblSpe7;
    private javax.swing.JLabel lblSpec2;
    private javax.swing.JLabel lblStaff1;
    private javax.swing.JLabel lblStaff2;
    private javax.swing.JLabel lblStaff3;
    private javax.swing.JLabel lblStaffType;
    private javax.swing.JLabel lblSum;
    private javax.swing.JLabel lblSumMethod;
    private javax.swing.JLabel lblTarget1;
    private javax.swing.JLabel lblTarget2;
    private javax.swing.JLabel lblTarget3;
    private javax.swing.JLabel lblTarget8;
    private javax.swing.JLabel lblTargetDate;
    private javax.swing.JLabel lblTargetMonth;
    private javax.swing.JLabel lblTargetPeriod1;
    private javax.swing.JLabel lblTargetPeriod2;
    private javax.swing.JLabel lblTargetPeriod3;
    private javax.swing.JLabel lblTargetPeriod4;
    private javax.swing.JLabel lblTargetPeriod5;
    private javax.swing.JLabel lblTargetPeriod6;
    private javax.swing.JLabel lblTargetPeriod7;
    private javax.swing.JLabel lblTargetPeriod8;
    private javax.swing.JLabel lblTargetYear;
    private javax.swing.JLabel lblTax1;
    private javax.swing.JLabel lblTax2;
    private javax.swing.JLabel lblTechType;
    private javax.swing.JLabel lblTimeCond;
    private javax.swing.JLabel lblTimePeriod;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbldis;
    private javax.swing.ButtonGroup outputGroup;
    private javax.swing.JPanel pnlDetailSale;
    private javax.swing.JPanel pnlTypeInfluence;
    private javax.swing.JPanel pnlTypeInfluence1;
    private javax.swing.JPanel pnlTypeRepeat;
    private javax.swing.JPanel pnlTypeSale;
    private javax.swing.JRadioButton rdoActive;
    private javax.swing.JRadioButton rdoBusinessReport;
    private javax.swing.JRadioButton rdoClassAll;
    private javax.swing.JRadioButton rdoClassItem;
    private javax.swing.JRadioButton rdoClassTech;
    private javax.swing.JRadioButton rdoConsumptionCourseReport;
    private javax.swing.JRadioButton rdoCourseReport;
    private javax.swing.JRadioButton rdoDay;
    private javax.swing.JRadioButton rdoDayShift;
    private javax.swing.JRadioButton rdoDayWay;
    private javax.swing.JRadioButton rdoItemReport;
    private javax.swing.JRadioButton rdoLeaveTime;
    private javax.swing.JRadioButton rdoMainStaff;
    private javax.swing.JRadioButton rdoMoney;
    private javax.swing.JRadioButton rdoMonthShift;
    private javax.swing.JRadioButton rdoMonthWay;
    private javax.swing.JRadioButton rdoNewVisitAll;
    private javax.swing.JRadioButton rdoNewVisitCurrent;
    private javax.swing.JRadioButton rdoPersonCount;
    private javax.swing.JRadioButton rdoReappear;
    private javax.swing.JRadioButton rdoReappearance;
    private javax.swing.JRadioButton rdoSaleShift;
    private javax.swing.JRadioButton rdoSimpleCount;
    private javax.swing.JRadioButton rdoStartTime;
    private javax.swing.JRadioButton rdoTaxBlank1;
    private javax.swing.JRadioButton rdoTaxBlank2;
    private javax.swing.JRadioButton rdoTaxUnit1;
    private javax.swing.JRadioButton rdoTaxUnit2;
    private javax.swing.JRadioButton rdoTechOtherCount;
    private javax.swing.JRadioButton rdoTechSameCount;
    private javax.swing.JRadioButton rdoTechStaff;
    private javax.swing.JRadioButton rdoTechnicalReport;
    private javax.swing.JRadioButton rdoTimeAnalysis;
    private javax.swing.JRadioButton rdoTimeCustom;
    private javax.swing.JRadioButton rdoTimeFixed;
    private javax.swing.JRadioButton rdoTotalCount;
    private javax.swing.JRadioButton rdoType;
    private javax.swing.JRadioButton rdoTypeShift;
    private javax.swing.JRadioButton rdoVisitTime;
    private javax.swing.JRadioButton rdoWeek;
    private javax.swing.JRadioButton rdoZChart;
    private javax.swing.ButtonGroup reportTypeGroup;
    private javax.swing.ButtonGroup taxGroup;
    private javax.swing.ButtonGroup taxGroup2;
    private javax.swing.JLabel techItemClassLabel;
    private javax.swing.ButtonGroup timeGroup;
    private javax.swing.JTextField txtFixedCount1;
    private javax.swing.JTextField txtFixedCount2;
    private javax.swing.ButtonGroup typeGroup;
    // End of variables declaration//GEN-END:variables

    
   
    
}


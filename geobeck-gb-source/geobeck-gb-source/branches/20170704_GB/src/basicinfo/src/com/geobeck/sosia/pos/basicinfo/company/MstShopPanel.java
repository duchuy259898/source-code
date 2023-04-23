/*
 * MstShopPanel.java
 *
 * Created on 2006/10/20, 10:07
 */
package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.basicinfo.WorkTimePasswordDialog;
import com.geobeck.sosia.pos.master.area.MstArea;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.system.MstSetting;
import com.geobeck.sosia.pos.search.*;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.swing.company.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.CheckUtil;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.*;
import javax.swing.tree.*;

/**
 *
 * @author  katagiri
 */
public class MstShopPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    /** Creates new form MstShopPanel */
    public MstShopPanel() {
        super();
        initComponents();
        addMouseCursorChange();
//        this.setSize(850, 700);
        this.setSize(900, 750);
        this.setPath("基本設定 >> 会社マスタ");
        this.setTitle("店舗情報登録");
        this.initTerm();
        this.setListener();
        this.initGroup();
        this.setGroup();
        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        this.initTabbIndexCombox();
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
        SystemInfo.initGroupShopComponents(parentGroup, 1);

        //会計単位非表示
        this.jLabel9.setVisible(false);
        this.unitOne.setVisible(false);
        this.unitTen.setVisible(false);

        //端数丸め非表示
        this.jLabel10.setVisible(false);
        this.roundDown.setVisible(false);
        this.round.setVisible(false);
        this.roundUp.setVisible(false);
        
        //店舗でログインしている場合
        if (SystemInfo.getCurrentShop().getShopID() != 0) {
            groupTreeScrollPane.setVisible(false);
            groupLabel.setVisible(false);
            parentGroup.setVisible(false);
//            this.setSize(480, this.getHeight());
            this.setSize(550, 700);
            this.loadShop();
            this.showData();
//			addButton.setEnabled(false);
            renewButton.setEnabled(true);
//			deleteButton.setEnabled(false);
            clearButton.setEnabled(false);

            // start add 20130114 nakhoa 店舗情報登
            this.cmbShopSort.setIcon(null);
            this.cmbShopSort.setText("");
            this.cmbShopSort.setEnabled(false);
            this.cmbArea.setIcon(null);
            this.cmbArea.setText("");
            this.cmbArea.setEnabled(false);
            this.jCheckBoxRecommendationFlag.setEnabled(false);
            // end add 20130114 nakhoa 店舗情報登


            //店舗情報が登録されていない場合はメール登録ボタン非表示
            if (postalCode.getText().equals("") && address1.getText().equals("") && address2.getText().equals("")
                    && address3.getText().equals("") && address4.getText().equals("") && phoneNumber.getText().equals("")
                    && faxNumber.getText().equals("") && mailAddress.getText().equals("") && cutoffDay.getText().equals("")
                    && coursedisplayday.getText().equals("")) //            if(shop==null)
            {
                RegistmailButton.setVisible(false);
            } else {
                RegistmailButton.setVisible(true);
            }
        } else {
            // start add 20130114 nakhoa 店舗情報登
            initArea();
            this.jCheckBoxRecommendationFlag.setEnabled(true);
            // end add 20130114 nakhoa 店舗情報登
        }
        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        // start Add 20130208 nakhoa SetVisible control if Ispot Customer
        if (!"".equals(SystemInfo.getIspotShopID())) {
            setVisibleControlIspotShop();
        }
        if (SystemInfo.getNSystem() != 1) {
            setVisibleNons();
        }
        // end Add 20130208 nakhoa SetVisible control if Ispot Customer
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
        // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
        this.initShopRelation();
        // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録
        //IVS_vtnhan start add 20140828 Request #30271
        MstSetting setting = SystemInfo.getSetteing();
        if(setting != null){
            if(!setting.isUseShopCategory()){
                jTabbedPane1.remove(jPanel7);
            }
        }
        //IVS_vtnhan end add 20140828 Request #30271
        
        //Luc start add 20160114
         if (SystemInfo.getNSystem() == 1) {
             jLabel45.setVisible(true);
             reservationBufferTime.setVisible(true);
             jLabel50.setVisible(true);
         }else {
             jLabel45.setVisible(false);
             reservationBufferTime.setVisible(false);
             jLabel50.setVisible(false);
         }
         //Luc end add 20160114
         
         //IVS_PTThu start add 2015/04/15 New request #49651
          if(SystemInfo.getSystemType()==1)
            {
                jLabel42.setVisible(false);
                shiftGearWorkingUse.setVisible(false);
                shiftGearWorkingNoUse.setVisible(false);
                reservationMenuUpdateUse.setVisible(false);
                jLabel31.setVisible(false);
                displayProportionallyNoUse.setVisible(false);
                displayProportionallyUse.setVisible(false);
                displayProportionallyLabel.setVisible(false);
                proportionallyNoUse.setVisible(false);
                proportionallyUse.setVisible(false);
                jLabel25.setVisible(false);
                reservationMenuUpdateNoUse.setVisible(false);
                jLabel39.setVisible(false);
                jLabel28.setVisible(false);
                coursedisplayday.setVisible(false);
                jLabel29.setVisible(false);           
            }
          //IVS_PTThu end add 2015/04/15 New request #49651
         
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundGroup = new javax.swing.ButtonGroup();
        unitGroup = new javax.swing.ButtonGroup();
        bedGroup = new javax.swing.ButtonGroup();
        proportionallyGroup = new javax.swing.ButtonGroup();
        autoNumberGroup = new javax.swing.ButtonGroup();
        reservationNullLineGroup = new javax.swing.ButtonGroup();
        displayProportionallyGroup = new javax.swing.ButtonGroup();
        reservationStaffShiftGroup = new javax.swing.ButtonGroup();
        shiftGearWorkingGroup = new javax.swing.ButtonGroup();
        birthGroup = new javax.swing.ButtonGroup();
        reservationMenuUpdateGroup = new javax.swing.ButtonGroup();
        designatedAssistGroup = new javax.swing.ButtonGroup();
        reservationStaffHolidayGroup = new javax.swing.ButtonGroup();
        buttonGroupCourseFlag = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        overBooking = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        groupTreeScrollPane = new javax.swing.JScrollPane();
        groupTree = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        unitOne = new javax.swing.JRadioButton();
        unitTen = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        roundDown = new javax.swing.JRadioButton();
        round = new javax.swing.JRadioButton();
        roundUp = new javax.swing.JRadioButton();
        jLabel21 = new javax.swing.JLabel();
        bedUse = new javax.swing.JRadioButton();
        bedNoUse = new javax.swing.JRadioButton();
        jLabel25 = new javax.swing.JLabel();
        proportionallyUse = new javax.swing.JRadioButton();
        proportionallyNoUse = new javax.swing.JRadioButton();
        displayProportionallyNoUse = new javax.swing.JRadioButton();
        displayProportionallyUse = new javax.swing.JRadioButton();
        displayProportionallyLabel = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        reservationStaffShiftUse = new javax.swing.JRadioButton();
        reservationNullLineUse = new javax.swing.JRadioButton();
        reservationNullLineNoUse = new javax.swing.JRadioButton();
        reservationStaffShiftNoUse = new javax.swing.JRadioButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        term = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        shiftGearWorkingUse = new javax.swing.JRadioButton();
        shiftGearWorkingNoUse = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        autoNumberUse = new javax.swing.JRadioButton();
        autoNumberNoUse = new javax.swing.JRadioButton();
        prefixStringLabel = new javax.swing.JLabel();
        prefixString = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)prefixString.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.ALPHAMERIC));
        seqLengthLabel1 = new javax.swing.JLabel();
        seqLength = new javax.swing.JComboBox();
        seqLengthLabel2 = new javax.swing.JLabel();
        numberingExampleLabel = new javax.swing.JLabel();
        numberingExample = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        reservationMenuUpdateUse = new javax.swing.JRadioButton();
        reservationMenuUpdateNoUse = new javax.swing.JRadioButton();
        jLabel42 = new javax.swing.JLabel();
        designatedAssistUse = new javax.swing.JRadioButton();
        designatedAssistNoUse = new javax.swing.JRadioButton();
        jLabel43 = new javax.swing.JLabel();
        reservationStaffHolidayUse = new javax.swing.JRadioButton();
        reservationStaffHolidayNoUse = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        reservationDuplicateWarningFlagNoUse = new javax.swing.JRadioButton();
        reservationDuplicateWarningFlagUse = new javax.swing.JRadioButton();
        jLabel44 = new javax.swing.JLabel();
        displayReservationRatoFlagUse = new javax.swing.JRadioButton();
        displayReservationRatoFlagNoUse = new javax.swing.JRadioButton();
        displayReaservationRatoRed = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)displayReaservationRatoRed.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMBER));
        reservationBufferTime = new javax.swing.JTextField();
        ((PlainDocument)reservationBufferTime.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMBER));
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        displayReaservationRatoYellow = new javax.swing.JTextField();
        ((PlainDocument)displayReaservationRatoYellow.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMBER));
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        courseFlagUse = new javax.swing.JRadioButton();
        courseFlagNoUse = new javax.swing.JRadioButton();
        jLabel53 = new javax.swing.JLabel();
        overBookingUse = new javax.swing.JRadioButton();
        overBookingNotUse = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        birthMonth = new javax.swing.JRadioButton();
        birthDay = new javax.swing.JRadioButton();
        birthAfterDays = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)birthAfterDays.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        birthBeforeDaysLabel = new javax.swing.JLabel();
        birthBeforeDays = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)birthBeforeDays.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        birthAfterDaysLabel = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cutoffDay = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(2, 1, 31));
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        openHour = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        openMinute = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        closeHour = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        closeMinute = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        coursedisplayday = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(2, 0, 99));
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        sexLabel6 = new javax.swing.JLabel();
        startTab = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        notUseCategory = new javax.swing.JRadioButton();
        useCategory = new javax.swing.JRadioButton();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        paymentsScrollPane = new javax.swing.JScrollPane();
        shopRelationTable = new com.geobeck.swing.JTableEx();
        jPanel3 = new javax.swing.JPanel();
        groupLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        parentGroup = new javax.swing.JComboBox();
        shopName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)shopName.getDocument()).setDocumentFilter(new CustomFilter(50));
        jLabel2 = new javax.swing.JLabel();
        postalCode = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createPostalCodeFormatter());
        searchAddressButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        address1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address1.getDocument()).setDocumentFilter(
            new CustomFilter(16));
        address2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address2.getDocument()).setDocumentFilter(
            new CustomFilter(64));
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        address3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address3.getDocument()).setDocumentFilter(
            new CustomFilter(128));
        phoneNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)phoneNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomPlainDocument.PHONE_NUMBER));
        faxNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)faxNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomPlainDocument.PHONE_NUMBER));
        address4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)address4.getDocument()).setDocumentFilter(
            new CustomFilter(128));
        mailAddress = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)mailAddress.getDocument()).setDocumentFilter(
            new CustomFilter(64, CustomFilter.MAIL_ADDRESS));
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        cmbAreaUpdate = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        cmbArea = new javax.swing.JButton();
        jCheckBoxRecommendationFlag = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        renewButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        RegistmailButton = new javax.swing.JButton();
        cmbShopSort = new javax.swing.JButton();

        setFocusCycleRoot(true);

        groupTreeScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        groupTree.setCellRenderer(new GroupTreeCellRenderer());
        groupTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        groupTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                groupTreeValueChanged(evt);
            }
        });
        groupTreeScrollPane.setViewportView(groupTree);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        jPanel5.setOpaque(false);

        jLabel9.setText("会計単位");

        unitGroup.add(unitOne);
        unitOne.setSelected(true);
        unitOne.setText("1円単位");
        unitOne.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        unitOne.setMargin(new java.awt.Insets(0, 0, 0, 0));
        unitOne.setOpaque(false);

        unitGroup.add(unitTen);
        unitTen.setText("10円単位");
        unitTen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        unitTen.setMargin(new java.awt.Insets(0, 0, 0, 0));
        unitTen.setOpaque(false);

        jLabel10.setText("端数丸め");

        roundGroup.add(roundDown);
        roundDown.setSelected(true);
        roundDown.setText("切り捨て");
        roundDown.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        roundDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
        roundDown.setOpaque(false);

        roundGroup.add(round);
        round.setText("四捨五入");
        round.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        round.setMargin(new java.awt.Insets(0, 0, 0, 0));
        round.setOpaque(false);

        roundGroup.add(roundUp);
        roundUp.setText("切り上げ");
        roundUp.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        roundUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        roundUp.setOpaque(false);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(122, 122, 122))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel9)
                            .add(unitOne)
                            .add(unitTen)
                            .add(roundUp)
                            .add(round)
                            .add(roundDown))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(unitOne)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(unitTen)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(roundDown)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(round)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(roundUp)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5);
        jPanel5.setBounds(510, 10, 90, 160);

        jLabel21.setText("施術台の使用");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(0, 10, 120, 20);

        bedGroup.add(bedUse);
        bedUse.setText("有");
        bedUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bedUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bedUse.setOpaque(false);
        jPanel2.add(bedUse);
        bedUse.setBounds(140, 10, 29, 20);

        bedGroup.add(bedNoUse);
        bedNoUse.setText("無");
        bedNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bedNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bedNoUse.setOpaque(false);
        jPanel2.add(bedNoUse);
        bedNoUse.setBounds(180, 10, 29, 20);

        jLabel25.setText("按分（作業工程）の使用");
        jPanel2.add(jLabel25);
        jLabel25.setBounds(0, 30, 130, 20);

        proportionallyGroup.add(proportionallyUse);
        proportionallyUse.setSelected(true);
        proportionallyUse.setText("有");
        proportionallyUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        proportionallyUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        proportionallyUse.setOpaque(false);
        proportionallyUse.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                proportionallyUseStateChanged(evt);
            }
        });
        jPanel2.add(proportionallyUse);
        proportionallyUse.setBounds(140, 30, 29, 20);

        proportionallyGroup.add(proportionallyNoUse);
        proportionallyNoUse.setText("無");
        proportionallyNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        proportionallyNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        proportionallyNoUse.setOpaque(false);
        jPanel2.add(proportionallyNoUse);
        proportionallyNoUse.setBounds(180, 30, 29, 20);

        displayProportionallyGroup.add(displayProportionallyNoUse);
        displayProportionallyNoUse.setText("無");
        displayProportionallyNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        displayProportionallyNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayProportionallyNoUse.setOpaque(false);
        jPanel2.add(displayProportionallyNoUse);
        displayProportionallyNoUse.setBounds(389, 30, 30, 20);

        displayProportionallyGroup.add(displayProportionallyUse);
        displayProportionallyUse.setSelected(true);
        displayProportionallyUse.setText("有");
        displayProportionallyUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        displayProportionallyUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayProportionallyUse.setOpaque(false);
        jPanel2.add(displayProportionallyUse);
        displayProportionallyUse.setBounds(350, 30, 40, 20);

        displayProportionallyLabel.setText("　　⇒　お会計画面表示");
        jPanel2.add(displayProportionallyLabel);
        displayProportionallyLabel.setBounds(210, 30, 130, 20);

        jLabel31.setText("お会計内容を予約表に反映");
        jPanel2.add(jLabel31);
        jLabel31.setBounds(230, 70, 140, 20);

        jLabel32.setText("予約表休日スタッフ表示");
        jPanel2.add(jLabel32);
        jLabel32.setBounds(0, 110, 140, 20);

        reservationStaffShiftGroup.add(reservationStaffShiftUse);
        reservationStaffShiftUse.setText("有");
        reservationStaffShiftUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationStaffShiftUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationStaffShiftUse.setOpaque(false);
        jPanel2.add(reservationStaffShiftUse);
        reservationStaffShiftUse.setBounds(140, 90, 29, 20);

        reservationNullLineGroup.add(reservationNullLineUse);
        reservationNullLineUse.setText("有");
        reservationNullLineUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationNullLineUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationNullLineUse.setOpaque(false);
        jPanel2.add(reservationNullLineUse);
        reservationNullLineUse.setBounds(140, 70, 29, 20);

        reservationNullLineGroup.add(reservationNullLineNoUse);
        reservationNullLineNoUse.setText("無");
        reservationNullLineNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationNullLineNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationNullLineNoUse.setOpaque(false);
        jPanel2.add(reservationNullLineNoUse);
        reservationNullLineNoUse.setBounds(180, 70, 29, 20);

        reservationStaffShiftGroup.add(reservationStaffShiftNoUse);
        reservationStaffShiftNoUse.setText("無");
        reservationStaffShiftNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationStaffShiftNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationStaffShiftNoUse.setOpaque(false);
        jPanel2.add(reservationStaffShiftNoUse);
        reservationStaffShiftNoUse.setBounds(180, 90, 29, 20);

        jLabel35.setForeground(java.awt.Color.gray);
        jLabel35.setText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        jPanel2.add(jLabel35);
        jLabel35.setBounds(0, 60, 490, 10);

        jLabel33.setText("指名入力アシスト");
        jPanel2.add(jLabel33);
        jLabel33.setBounds(220, 220, 90, 20);

        jLabel36.setForeground(java.awt.Color.gray);
        jLabel36.setText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        jPanel2.add(jLabel36);
        jLabel36.setBounds(0, 210, 490, 10);

        jLabel19.setText("予約表時間幅");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(230, 90, 80, 20);

        term.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel2.add(term);
        term.setBounds(310, 90, 40, 20);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("分");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(350, 90, 20, 20);

        shiftGearWorkingGroup.add(shiftGearWorkingUse);
        shiftGearWorkingUse.setText("有");
        shiftGearWorkingUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        shiftGearWorkingUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        shiftGearWorkingUse.setOpaque(false);
        jPanel2.add(shiftGearWorkingUse);
        shiftGearWorkingUse.setBounds(130, 220, 29, 20);

        shiftGearWorkingGroup.add(shiftGearWorkingNoUse);
        shiftGearWorkingNoUse.setText("無");
        shiftGearWorkingNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        shiftGearWorkingNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        shiftGearWorkingNoUse.setOpaque(false);
        jPanel2.add(shiftGearWorkingNoUse);
        shiftGearWorkingNoUse.setBounds(170, 220, 29, 20);

        jLabel26.setText("顧客No.自動採番");
        jPanel2.add(jLabel26);
        jLabel26.setBounds(0, 250, 120, 20);

        autoNumberGroup.add(autoNumberUse);
        autoNumberUse.setSelected(true);
        autoNumberUse.setText("有");
        autoNumberUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        autoNumberUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        autoNumberUse.setOpaque(false);
        autoNumberUse.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoNumberUseStateChanged(evt);
            }
        });
        jPanel2.add(autoNumberUse);
        autoNumberUse.setBounds(130, 250, 29, 20);

        autoNumberGroup.add(autoNumberNoUse);
        autoNumberNoUse.setText("無");
        autoNumberNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        autoNumberNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        autoNumberNoUse.setOpaque(false);
        jPanel2.add(autoNumberNoUse);
        autoNumberNoUse.setBounds(170, 250, 29, 20);

        prefixStringLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        prefixStringLabel.setText("接頭文字");
        jPanel2.add(prefixStringLabel);
        prefixStringLabel.setBounds(70, 280, 60, 20);

        prefixString.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        prefixString.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                prefixStringFocusLost(evt);
            }
        });
        jPanel2.add(prefixString);
        prefixString.setBounds(130, 280, 55, 20);

        seqLengthLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seqLengthLabel1.setText("連番桁数");
        jPanel2.add(seqLengthLabel1);
        seqLengthLabel1.setBounds(190, 280, 50, 20);

        seqLength.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        for (Integer i = 4; i < 10; i++) {
            seqLength.addItem(i);
        }
        seqLength.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                seqLengthItemStateChanged(evt);
            }
        });
        jPanel2.add(seqLength);
        seqLength.setBounds(240, 280, 42, 20);

        seqLengthLabel2.setText("桁");
        jPanel2.add(seqLengthLabel2);
        seqLengthLabel2.setBounds(290, 280, 20, 20);

        numberingExampleLabel.setText("採番例 ：");
        jPanel2.add(numberingExampleLabel);
        numberingExampleLabel.setBounds(310, 280, 46, 20);

        numberingExample.setText("ABCDEF123456789");
        numberingExample.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(numberingExample);
        numberingExample.setBounds(360, 280, 110, 20);

        jLabel37.setForeground(java.awt.Color.gray);
        jLabel37.setText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        jPanel2.add(jLabel37);
        jLabel37.setBounds(0, 240, 490, 10);

        jLabel38.setForeground(java.awt.Color.gray);
        jLabel38.setText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        jPanel2.add(jLabel38);
        jLabel38.setBounds(0, 0, 490, 10);

        jLabel41.setText("予約表空行表示");
        jPanel2.add(jLabel41);
        jLabel41.setBounds(0, 70, 120, 20);

        reservationMenuUpdateGroup.add(reservationMenuUpdateUse);
        reservationMenuUpdateUse.setText("する");
        reservationMenuUpdateUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationMenuUpdateUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationMenuUpdateUse.setOpaque(false);
        jPanel2.add(reservationMenuUpdateUse);
        reservationMenuUpdateUse.setBounds(400, 70, 40, 20);

        reservationMenuUpdateGroup.add(reservationMenuUpdateNoUse);
        reservationMenuUpdateNoUse.setSelected(true);
        reservationMenuUpdateNoUse.setText("しない");
        reservationMenuUpdateNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationMenuUpdateNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationMenuUpdateNoUse.setOpaque(false);
        jPanel2.add(reservationMenuUpdateNoUse);
        reservationMenuUpdateNoUse.setBounds(440, 70, 50, 20);

        jLabel42.setText("出退勤シフト連動");
        jPanel2.add(jLabel42);
        jLabel42.setBounds(0, 220, 120, 20);

        designatedAssistGroup.add(designatedAssistUse);
        designatedAssistUse.setText("有");
        designatedAssistUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        designatedAssistUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        designatedAssistUse.setOpaque(false);
        jPanel2.add(designatedAssistUse);
        designatedAssistUse.setBounds(362, 220, 29, 20);

        designatedAssistGroup.add(designatedAssistNoUse);
        designatedAssistNoUse.setText("無");
        designatedAssistNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        designatedAssistNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        designatedAssistNoUse.setOpaque(false);
        jPanel2.add(designatedAssistNoUse);
        designatedAssistNoUse.setBounds(402, 220, 29, 20);

        jLabel43.setText("予約表スタッフシフト表示");
        jPanel2.add(jLabel43);
        jLabel43.setBounds(0, 90, 140, 20);

        reservationStaffHolidayGroup.add(reservationStaffHolidayUse);
        reservationStaffHolidayUse.setText("有");
        reservationStaffHolidayUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationStaffHolidayUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationStaffHolidayUse.setOpaque(false);
        jPanel2.add(reservationStaffHolidayUse);
        reservationStaffHolidayUse.setBounds(140, 110, 29, 20);

        reservationStaffHolidayGroup.add(reservationStaffHolidayNoUse);
        reservationStaffHolidayNoUse.setText("無");
        reservationStaffHolidayNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationStaffHolidayNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationStaffHolidayNoUse.setOpaque(false);
        jPanel2.add(reservationStaffHolidayNoUse);
        reservationStaffHolidayNoUse.setBounds(180, 110, 29, 20);

        jLabel27.setText("二重予約警告(ダブルブッキング)");
        jLabel27.setMaximumSize(new java.awt.Dimension(160, 14));
        jLabel27.setMinimumSize(new java.awt.Dimension(160, 14));
        jLabel27.setPreferredSize(new java.awt.Dimension(160, 14));
        jPanel2.add(jLabel27);
        jLabel27.setBounds(230, 110, 170, 20);

        buttonGroup1.add(reservationDuplicateWarningFlagNoUse);
        reservationDuplicateWarningFlagNoUse.setActionCommand("無");
        reservationDuplicateWarningFlagNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationDuplicateWarningFlagNoUse.setLabel("無");
        reservationDuplicateWarningFlagNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationDuplicateWarningFlagNoUse.setOpaque(false);
        jPanel2.add(reservationDuplicateWarningFlagNoUse);
        reservationDuplicateWarningFlagNoUse.setBounds(440, 110, 29, 20);

        buttonGroup1.add(reservationDuplicateWarningFlagUse);
        reservationDuplicateWarningFlagUse.setText("有");
        reservationDuplicateWarningFlagUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reservationDuplicateWarningFlagUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reservationDuplicateWarningFlagUse.setOpaque(false);
        jPanel2.add(reservationDuplicateWarningFlagUse);
        reservationDuplicateWarningFlagUse.setBounds(400, 110, 29, 20);

        jLabel44.setText("それ以外緑色 ");
        jPanel2.add(jLabel44);
        jLabel44.setBounds(250, 150, 80, 20);

        buttonGroup2.add(displayReservationRatoFlagUse);
        displayReservationRatoFlagUse.setText("有");
        displayReservationRatoFlagUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        displayReservationRatoFlagUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayReservationRatoFlagUse.setOpaque(false);
        jPanel2.add(displayReservationRatoFlagUse);
        displayReservationRatoFlagUse.setBounds(140, 130, 29, 20);

        buttonGroup2.add(displayReservationRatoFlagNoUse);
        displayReservationRatoFlagNoUse.setText("無");
        displayReservationRatoFlagNoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        displayReservationRatoFlagNoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayReservationRatoFlagNoUse.setOpaque(false);
        jPanel2.add(displayReservationRatoFlagNoUse);
        displayReservationRatoFlagNoUse.setBounds(180, 130, 29, 20);
        jPanel2.add(displayReaservationRatoRed);
        displayReaservationRatoRed.setBounds(20, 150, 30, 19);

        reservationBufferTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        reservationBufferTime.setToolTipText("");
        jPanel2.add(reservationBufferTime);
        reservationBufferTime.setBounds(130, 180, 30, 20);

        jLabel45.setText("全予約バッファ時間 ");
        jPanel2.add(jLabel45);
        jLabel45.setBounds(0, 180, 110, 20);

        jLabel46.setText("%以上 赤色 ");
        jPanel2.add(jLabel46);
        jLabel46.setBounds(60, 150, 70, 20);

        jLabel48.setText("予約率表示 ");
        jPanel2.add(jLabel48);
        jLabel48.setBounds(0, 130, 120, 20);
        jPanel2.add(displayReaservationRatoYellow);
        displayReaservationRatoYellow.setBounds(130, 150, 30, 19);

        jLabel49.setText("%以上 黄色 ");
        jPanel2.add(jLabel49);
        jLabel49.setBounds(170, 150, 80, 20);

        jLabel50.setText("分");
        jPanel2.add(jLabel50);
        jLabel50.setBounds(180, 180, 20, 20);

        jLabel8.setText(" 役務機能の使用");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(240, 10, 90, 20);

        buttonGroupCourseFlag.add(courseFlagUse);
        courseFlagUse.setText("有");
        jPanel2.add(courseFlagUse);
        courseFlagUse.setBounds(346, 10, 37, 20);

        buttonGroupCourseFlag.add(courseFlagNoUse);
        courseFlagNoUse.setSelected(true);
        courseFlagNoUse.setText("無");
        jPanel2.add(courseFlagNoUse);
        courseFlagNoUse.setBounds(386, 10, 37, 20);

        jLabel53.setText("契約数以上の予約の取得可");
        jPanel2.add(jLabel53);
        jLabel53.setBounds(220, 255, 140, 13);

        overBooking.add(overBookingUse);
        overBookingUse.setText("有");
        jPanel2.add(overBookingUse);
        overBookingUse.setBounds(360, 250, 37, 21);

        overBooking.add(overBookingNotUse);
        overBookingNotUse.setText("無");
        jPanel2.add(overBookingNotUse);
        overBookingNotUse.setBounds(400, 250, 40, 21);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 306, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("詳細1 ", jPanel4);

        jLabel34.setText("<html>\n誕生日マーク表示設定<br>\n（フロント画面）\n</html>");

        birthGroup.add(birthMonth);
        birthMonth.setSelected(true);
        birthMonth.setText("誕生月");
        birthMonth.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        birthMonth.setMargin(new java.awt.Insets(0, 0, 0, 0));
        birthMonth.setOpaque(false);

        birthGroup.add(birthDay);
        birthDay.setText("誕生日の");
        birthDay.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        birthDay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        birthDay.setOpaque(false);
        birthDay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                birthDayStateChanged(evt);
            }
        });

        birthAfterDays.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        birthAfterDays.setColumns(4);
        birthAfterDays.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        birthBeforeDaysLabel.setText(" 日前  ～  誕生日の");

        birthBeforeDays.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        birthBeforeDays.setColumns(4);
        birthBeforeDays.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        birthAfterDaysLabel.setText(" 日後");

        jLabel40.setForeground(java.awt.Color.gray);
        jLabel40.setText("    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        jLabel11.setText("締め日");

        cutoffDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cutoffDay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("日");

        jLabel13.setText("営業時間");

        openHour.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        for(Integer i = 0; i <= 23; i ++)
        openHour.addItem(i);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("時");

        openMinute.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        for(Integer i = 0; i <= 55; i += 30)
        openMinute.addItem(i);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("分");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("～");

        closeHour.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        for(Integer i = 0; i <= 36; i ++)
        closeHour.addItem(i);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("時");

        closeMinute.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        for(Integer i = 0; i <= 55; i += 30)
        closeMinute.addItem(i);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("分");

        jLabel51.setForeground(java.awt.Color.gray);
        jLabel51.setText("    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        coursedisplayday.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        coursedisplayday.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        coursedisplayday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coursedisplaydayActionPerformed(evt);
            }
        });

        jLabel28.setText("有効期限が ");

        jLabel29.setText("日前の顧客を表示");

        jLabel39.setText("コース契約有効期限間近タブ 表示設定 (ﾌﾛﾝﾄ画面)");

        jLabel30.setText("顧客情報登録　");

        jLabel52.setText("初期タブ名 ");

        sexLabel6.setText("予約表等の顧客情報ボタン押下時の初期タブを指定できます。 ");

        startTab.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        startTab.setMaximumRowCount(20);
        startTab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        startTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTabActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 490, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 490, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(jLabel13)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(openHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(openMinute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(2, 2, 2)
                                .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(closeHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(3, 3, 3)
                                .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(closeMinute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(2, 2, 2)
                                .add(jLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                                    .add(jLabel34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jPanel6Layout.createSequentialGroup()
                                            .add(birthDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                            .add(birthAfterDays, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                            .add(birthBeforeDaysLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(18, 18, 18)
                                            .add(birthBeforeDays, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(birthMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(birthAfterDaysLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(jPanel6Layout.createSequentialGroup()
                                    .add(jLabel11)
                                    .add(31, 31, 31)
                                    .add(cutoffDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))
                .add(0, 0, Short.MAX_VALUE))
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel6Layout.createSequentialGroup()
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel30)
                                .add(jLabel52))
                            .add(0, 0, 0)
                            .add(startTab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(sexLabel6))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                            .add(jLabel28)
                            .add(18, 18, 18)
                            .add(coursedisplayday, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(304, 304, 304))))
                .add(94, 132, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jLabel51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(birthMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(birthAfterDaysLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(birthBeforeDays, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(birthBeforeDaysLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(birthAfterDays, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(birthDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(jLabel40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cutoffDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeMinute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(closeHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(openMinute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(openHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(15, 15, 15)
                .add(jLabel39)
                .add(0, 0, 0)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel29)
                    .add(coursedisplayday, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel28))
                .add(8, 8, 8)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(jLabel52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(sexLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(startTab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("詳細2", jPanel6);

        jLabel54.setText("トータルビューティ対応");
        jLabel54.setToolTipText("");

        buttonGroup3.add(notUseCategory);
        notUseCategory.setText("利用しない");
        notUseCategory.setEnabled(false);

        buttonGroup3.add(useCategory);
        useCategory.setText("利用する");
        useCategory.setEnabled(false);
        useCategory.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                useCategoryStateChanged(evt);
            }
        });

        jLabel55.setText("<利用業態>");
        jLabel55.setToolTipText("");

        jLabel56.setText("※利用する業態にチェックをいれてください");
        jLabel56.setToolTipText("");

        paymentsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        shopRelationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "業態", "選択"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        shopRelationTable.setToolTipText("");
        shopRelationTable.setRowSelectionAllowed(false);
        shopRelationTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
        shopRelationTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        shopRelationTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(shopRelationTable, SystemInfo.getTableHeaderRenderer());
        shopRelationTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(shopRelationTable);
        TableColumnModel model = shopRelationTable.getColumnModel();
        model.setColumnSelectionAllowed(false);
        this.initTableColumnWidth();
        shopRelationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                categoryCellMouseRelease(evt);
            }
        });
        paymentsScrollPane.setViewportView(shopRelationTable);

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(useCategory)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(notUseCategory))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(192, 192, 192)
                        .add(jLabel56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 229, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(paymentsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 495, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(notUseCategory)
                    .add(useCategory))
                .add(0, 0, 0)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(paymentsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("詳細3", jPanel7);
        jPanel7.setOpaque(true);

        jPanel3.setOpaque(false);

        groupLabel.setText("  所属グループ");
        groupLabel.setToolTipText("");

        jLabel1.setText("  店舗名");
        jLabel1.setToolTipText("");

        parentGroup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        parentGroup.setRenderer(new GroupComboBoxCellRenderer());

        shopName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shopName.setColumns(50);
        shopName.setInputKanji(true);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("  郵便番号");

        postalCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        postalCode.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        postalCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                postalCodeFocusLost(evt);
            }
        });

        searchAddressButton.setIcon(SystemInfo.getImageIcon("/button/search/search_address_off.jpg"));
        searchAddressButton.setBorderPainted(false);
        searchAddressButton.setContentAreaFilled(false);
        searchAddressButton.setFocusable(false);
        searchAddressButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_address_on.jpg"));
        searchAddressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchAddressButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("  都道府県");

        jLabel22.setText("市区町村");

        address1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        address1.setInputKanji(true);

        address2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        address2.setInputKanji(true);

        jLabel5.setText("FAX番号");

        jLabel4.setText("  電話番号");

        jLabel24.setText("  ビル名等");

        jLabel23.setText("  町域・番地");

        address3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        address3.setInputKanji(true);

        phoneNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        faxNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        faxNumber.setColumns(20);

        address4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        address4.setColumns(128);
        address4.setInputKanji(true);

        mailAddress.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mailAddress.setColumns(64);

        jLabel6.setText("  メールアドレス");

        jLabel7.setText("<html>\n※このメールアドレスがお客様へのメール送信元になります。<br>\n&nbsp;&nbsp;&nbsp;宛先不明によるエラーメールなどもこちらに届きます。\n</html>");

        jLabel47.setText("エリア");

        cmbArea.setIcon(SystemInfo.getImageIcon("/button/common/area_regist_off.jpg"));
        cmbArea.setToolTipText("");
        cmbArea.setAlignmentY(0.0F);
        cmbArea.setBorderPainted(false);
        cmbArea.setContentAreaFilled(false);
        cmbArea.setFocusable(false);
        cmbArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAreaActionPerformed(evt);
            }
        });

        jCheckBoxRecommendationFlag.setSelected(true);
        jCheckBoxRecommendationFlag.setText("お薦め");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(14, 14, 14)
                        .add(address1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(26, 26, 26)
                        .add(jLabel22)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(address2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(groupLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(14, 14, 14)
                        .add(parentGroup, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel23, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel24, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 321, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(3, 3, 3)
                                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .add(0, 71, Short.MAX_VALUE)
                                        .add(jCheckBoxRecommendationFlag)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(searchAddressButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel47)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(cmbAreaUpdate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(cmbArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .add(phoneNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(faxNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(58, 58, 58))
                                    .add(jPanel3Layout.createSequentialGroup()
                                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(mailAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(shopName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(address3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(address4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(postalCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(0, 267, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(groupLabel)
                    .add(parentGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(shopName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(9, 9, 9)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cmbArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbAreaUpdate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel47, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(searchAddressButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jLabel2)
                                .add(postalCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jCheckBoxRecommendationFlag)))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(address1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(address2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel22))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(address3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(address4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(faxNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(phoneNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(mailAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setOpaque(false);

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setContentAreaFilled(false);
        renewButton.setEnabled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        clearButton.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
        clearButton.setBorderPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        RegistmailButton.setIcon(SystemInfo.getImageIcon("/button/common/admin_mail_off.jpg"));
        RegistmailButton.setBorderPainted(false);
        RegistmailButton.setContentAreaFilled(false);
        RegistmailButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/admin_mail_on.jpg"));
        RegistmailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegistmailButtonActionPerformed(evt);
            }
        });

        cmbShopSort.setIcon(SystemInfo.getImageIcon("/button/common/display_seq_setting_off.jpg"));
        cmbShopSort.setActionCommand("店舗並び順設定");
        cmbShopSort.setBorderPainted(false);
        cmbShopSort.setContentAreaFilled(false);
        cmbShopSort.setFocusPainted(false);
        cmbShopSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShopSortActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(RegistmailButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(cmbShopSort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(RegistmailButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbShopSort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(groupTreeScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 264, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 680, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(618, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(7, 7, 7)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 340, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(groupTreeScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 667, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
                        
	private void groupTreeValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_groupTreeValueChanged
	{//GEN-HEADEREND:event_groupTreeValueChanged
            this.changeCurrentShop();
	}//GEN-LAST:event_groupTreeValueChanged
                                         // start add 20130114 nakhoa 店舗情報登
    /*
     * Load エリア
     */
    private void initArea() {
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            MstArea mstAresa = new MstArea();
            if (SystemInfo.getCurrentShop().getShopID() != 0) {
                mstAresa.setID(shop.getAreaId());
                if (shop.getAreaId() != null) {
                    mstAresa.getAreaByID(con);
                }
                this.cmbAreaUpdate.addItem(mstAresa);
            } else {
                areas = mstAresa.getArea(con);
                if (areas != null) {
                    this.cmbAreaUpdate.removeAllItems();
                    this.cmbAreaUpdate.addItem(null);
                    for (MstArea areeObj : areas) {
                        this.cmbAreaUpdate.addItem(areeObj);
                    }
                    // エリア
                    if (shop.getAreaId() != null) {
                        setAreaItemSelected(shop.getAreaId());
                    }
                }
            }
            con.close();
            this.setVisible();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    /*
     * Set エリア Item
     */
    private void setAreaItemSelected(int areaId) {
        MstArea mstArea = new MstArea();
        this.jLabel47.setText("");
        if (SystemInfo.getCurrentShop().getShopID() == 0) {
            this.jLabel47.setText("エリア");
            boolean flag = false;
            for (int index = 1; index < this.cmbAreaUpdate.getItemCount(); index++) {
                mstArea = (MstArea) this.cmbAreaUpdate.getItemAt(index);
                if (mstArea != null) {
                    if (mstArea.getID() == areaId) {
                        this.cmbAreaUpdate.setSelectedIndex(index);
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                this.cmbAreaUpdate.setSelectedIndex(0);
            }
        } else {
            mstArea.setID(shop.getAreaId());
            try {
                mstArea.getAreaByID(SystemInfo.getConnection());
                jLabel47.setText(" ");
                if (mstArea.getID() != null && mstArea.getName() != null) {
                    if (!"".equals(mstArea.getName())) {
                        jLabel47.setText(mstArea.getName() + "エリア");
                    }
                }
                this.cmbAreaUpdate.addItem(null);
                this.cmbAreaUpdate.setEditable(false);
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }
    
     /*
     * 店舗のエリア表示を有効にするか。
     */
    private void setVisible() {
        int displayArea = SystemInfo.getAccountSetting().getDisplayArea();
        if (displayArea != 1) {
            this.cmbShopSort.setIcon(null);
            this.cmbShopSort.setText("");
            this.cmbShopSort.setEnabled(false);
            this.cmbArea.setIcon(null);
            this.cmbArea.setText("");
            this.cmbArea.setEnabled(false);
            this.cmbArea.setVisible(false);
        }
    }

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAreaActionPerformed
        RegistAreaDialogNons dlg = new RegistAreaDialogNons(
                "エリア",
                "mst_area",
                "area_id",
                "area_name",
                20, SystemInfo.getTableHeaderRenderer());

        dlg.setOpener(this);
        SwingUtil.openAnchorDialog(null, true, dlg, "エリア登録", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        this.initArea();
    }//GEN-LAST:event_cmbAreaActionPerformed

    private void postalCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_postalCodeFocusLost
        String temp = postalCode.getText().replaceAll("[-_]", "");

        //郵便番号が入録されていない場合
        if (temp.equals("") || temp.length() != 7) {
            address1.setText("");
            address2.setText("");
            address3.setText("");
        } else {
            AddressUtil au = new AddressUtil();

            au.setPostalCode(temp);

            try {
                ConnectionWrapper con = SystemInfo.getBaseConnection();

                //住所が取得できた場合
                if (au.getDataByPostalCode(con)) {
                    address1.setText(au.getPrefectureName());
                    address2.setText(au.getCityName());
                    address3.setText(au.getTownName().replaceAll("（.+", ""));
                } //住所が取得できなかった場合住所をクリア
                else {
                    address1.setText("");
                    address2.setText("");
                    address3.setText("");
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }//GEN-LAST:event_postalCodeFocusLost

    private void searchAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchAddressButtonActionPerformed
        SearchAddressDialog sa = new SearchAddressDialog(parentFrame, true);
        sa.setVisible(true);

        if (!sa.getSelectedPrefecture().equals("")) {
            postalCode.setText(sa.getSelectedPostalCode());
            address1.setText(sa.getSelectedPrefecture());
            address2.setText(sa.getSelectedCity());
            address3.setText(sa.getSelectedTown().replaceAll("（.+", ""));
        }

        sa = null;
    }//GEN-LAST:event_searchAddressButtonActionPerformed

    private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed
        if (this.checkInput()) {
            this.regist(false);
        }
    }//GEN-LAST:event_renewButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        this.clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void RegistmailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegistmailButtonActionPerformed

        // パスワード認証
        if (WorkTimePasswordDialog.isAuthPassword()) {
            RegistManagerMailPanel p = new RegistManagerMailPanel();
            p.setOpener(this);
            SwingUtil.openAnchorDialog(null, true, p, "管理者メールアドレス登録", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        }
    }//GEN-LAST:event_RegistmailButtonActionPerformed

    private void cmbShopSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopSortActionPerformed
        MstSortStorePanelNons dlg = new MstSortStorePanelNons(
                "店舗マスタ",
                "mst_shop",
                "shop_id",
                "shop_name",
                20, SystemInfo.getTableHeaderRenderer());

        dlg.setOpener(this);
        SwingUtil.openAnchorDialog(null, true, dlg, "店舗並び順設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        this.initGroup();
        this.setGroup();
    }//GEN-LAST:event_cmbShopSortActionPerformed

    private void startTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTabActionPerformed
    }//GEN-LAST:event_startTabActionPerformed

    private void coursedisplaydayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coursedisplaydayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_coursedisplaydayActionPerformed

    private void birthDayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_birthDayStateChanged
        birthBeforeDays.setEnabled(birthDay.isSelected());
        birthAfterDays.setEnabled(birthDay.isSelected());
        birthBeforeDaysLabel.setEnabled(birthDay.isSelected());
        birthAfterDaysLabel.setEnabled(birthDay.isSelected());
    }//GEN-LAST:event_birthDayStateChanged

    private void seqLengthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_seqLengthItemStateChanged
        setNumberingExample();
    }//GEN-LAST:event_seqLengthItemStateChanged

    private void prefixStringFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prefixStringFocusLost
        setNumberingExample();
    }//GEN-LAST:event_prefixStringFocusLost

    private void autoNumberUseStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoNumberUseStateChanged
        prefixString.setEnabled(autoNumberUse.isSelected());
        seqLength.setEnabled(autoNumberUse.isSelected());
        numberingExample.setEnabled(autoNumberUse.isSelected());
        prefixStringLabel.setEnabled(autoNumberUse.isSelected());
        seqLengthLabel1.setEnabled(autoNumberUse.isSelected());
        seqLengthLabel2.setEnabled(autoNumberUse.isSelected());
        numberingExampleLabel.setEnabled(autoNumberUse.isSelected());
    }//GEN-LAST:event_autoNumberUseStateChanged

    private void proportionallyUseStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_proportionallyUseStateChanged
        displayProportionallyLabel.setEnabled(proportionallyUse.isSelected());
        displayProportionallyUse.setEnabled(proportionallyUse.isSelected());
        displayProportionallyNoUse.setEnabled(proportionallyUse.isSelected());
    }//GEN-LAST:event_proportionallyUseStateChanged

    private void useCategoryStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_useCategoryStateChanged
        boolean useCategoryFlg = useCategory.isSelected();
        DefaultTableModel model = (DefaultTableModel) shopRelationTable.getModel();
        for (int i = 0; i < listShopRelation.size(); i++) {
            JCheckBox selectCheckbox = (JCheckBox) model.getValueAt(i, 1);
            selectCheckbox.setEnabled(useCategoryFlg);
        }
    }//GEN-LAST:event_useCategoryStateChanged

    private void categoryCellMouseRelease(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoryCellMouseRelease
         if (shopRelationTable.getCellEditor() != null) {
            shopRelationTable.getCellEditor().stopCellEditing();
         }
    }//GEN-LAST:event_categoryCellMouseRelease

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton RegistmailButton;
    private com.geobeck.swing.JFormattedTextFieldEx address1;
    private com.geobeck.swing.JFormattedTextFieldEx address2;
    private com.geobeck.swing.JFormattedTextFieldEx address3;
    private com.geobeck.swing.JFormattedTextFieldEx address4;
    private javax.swing.ButtonGroup autoNumberGroup;
    private javax.swing.JRadioButton autoNumberNoUse;
    private javax.swing.JRadioButton autoNumberUse;
    private javax.swing.ButtonGroup bedGroup;
    private javax.swing.JRadioButton bedNoUse;
    private javax.swing.JRadioButton bedUse;
    private com.geobeck.swing.JFormattedTextFieldEx birthAfterDays;
    private javax.swing.JLabel birthAfterDaysLabel;
    private com.geobeck.swing.JFormattedTextFieldEx birthBeforeDays;
    private javax.swing.JLabel birthBeforeDaysLabel;
    private javax.swing.JRadioButton birthDay;
    private javax.swing.ButtonGroup birthGroup;
    private javax.swing.JRadioButton birthMonth;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroupCourseFlag;
    private javax.swing.JButton clearButton;
    private javax.swing.JComboBox closeHour;
    private javax.swing.JComboBox closeMinute;
    private javax.swing.JButton cmbArea;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbAreaUpdate;
    private javax.swing.JButton cmbShopSort;
    private javax.swing.JRadioButton courseFlagNoUse;
    private javax.swing.JRadioButton courseFlagUse;
    private com.geobeck.swing.JFormattedTextFieldEx coursedisplayday;
    private com.geobeck.swing.JFormattedTextFieldEx cutoffDay;
    private javax.swing.ButtonGroup designatedAssistGroup;
    private javax.swing.JRadioButton designatedAssistNoUse;
    private javax.swing.JRadioButton designatedAssistUse;
    private javax.swing.ButtonGroup displayProportionallyGroup;
    private javax.swing.JLabel displayProportionallyLabel;
    private javax.swing.JRadioButton displayProportionallyNoUse;
    private javax.swing.JRadioButton displayProportionallyUse;
    private com.geobeck.swing.JFormattedTextFieldEx displayReaservationRatoRed;
    private javax.swing.JTextField displayReaservationRatoYellow;
    private javax.swing.JRadioButton displayReservationRatoFlagNoUse;
    private javax.swing.JRadioButton displayReservationRatoFlagUse;
    private com.geobeck.swing.JFormattedTextFieldEx faxNumber;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JTree groupTree;
    private javax.swing.JScrollPane groupTreeScrollPane;
    private javax.swing.JCheckBox jCheckBoxRecommendationFlag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.geobeck.swing.JFormattedTextFieldEx mailAddress;
    private javax.swing.JRadioButton notUseCategory;
    private javax.swing.JLabel numberingExample;
    private javax.swing.JLabel numberingExampleLabel;
    private javax.swing.JComboBox openHour;
    private javax.swing.JComboBox openMinute;
    private javax.swing.ButtonGroup overBooking;
    private javax.swing.JRadioButton overBookingNotUse;
    private javax.swing.JRadioButton overBookingUse;
    private javax.swing.JComboBox parentGroup;
    private javax.swing.JScrollPane paymentsScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx phoneNumber;
    private com.geobeck.swing.JFormattedTextFieldEx postalCode;
    private com.geobeck.swing.JFormattedTextFieldEx prefixString;
    private javax.swing.JLabel prefixStringLabel;
    private javax.swing.ButtonGroup proportionallyGroup;
    private javax.swing.JRadioButton proportionallyNoUse;
    private javax.swing.JRadioButton proportionallyUse;
    private javax.swing.JButton renewButton;
    private javax.swing.JTextField reservationBufferTime;
    private javax.swing.JRadioButton reservationDuplicateWarningFlagNoUse;
    private javax.swing.JRadioButton reservationDuplicateWarningFlagUse;
    private javax.swing.ButtonGroup reservationMenuUpdateGroup;
    private javax.swing.JRadioButton reservationMenuUpdateNoUse;
    private javax.swing.JRadioButton reservationMenuUpdateUse;
    private javax.swing.ButtonGroup reservationNullLineGroup;
    private javax.swing.JRadioButton reservationNullLineNoUse;
    private javax.swing.JRadioButton reservationNullLineUse;
    private javax.swing.ButtonGroup reservationStaffHolidayGroup;
    private javax.swing.JRadioButton reservationStaffHolidayNoUse;
    private javax.swing.JRadioButton reservationStaffHolidayUse;
    private javax.swing.ButtonGroup reservationStaffShiftGroup;
    private javax.swing.JRadioButton reservationStaffShiftNoUse;
    private javax.swing.JRadioButton reservationStaffShiftUse;
    private javax.swing.JRadioButton round;
    private javax.swing.JRadioButton roundDown;
    private javax.swing.ButtonGroup roundGroup;
    private javax.swing.JRadioButton roundUp;
    private javax.swing.JButton searchAddressButton;
    private javax.swing.JComboBox seqLength;
    private javax.swing.JLabel seqLengthLabel1;
    private javax.swing.JLabel seqLengthLabel2;
    private javax.swing.JLabel sexLabel6;
    private javax.swing.ButtonGroup shiftGearWorkingGroup;
    private javax.swing.JRadioButton shiftGearWorkingNoUse;
    private javax.swing.JRadioButton shiftGearWorkingUse;
    private com.geobeck.swing.JFormattedTextFieldEx shopName;
    private com.geobeck.swing.JTableEx shopRelationTable;
    private javax.swing.JComboBox startTab;
    private javax.swing.JComboBox term;
    private javax.swing.ButtonGroup unitGroup;
    private javax.swing.JRadioButton unitOne;
    private javax.swing.JRadioButton unitTen;
    private javax.swing.JRadioButton useCategory;
    // End of variables declaration//GEN-END:variables
    // start add 20130114 nakhoa 店舗情報登
    private ArrayList<MstArea> areas = new ArrayList<MstArea>();
    // end add 20130114 nakhoa 店舗情報登
    
    /**
     * 店舗情報
     */
    private MstShop shop = new MstShop();
    private MstShopRelations listShopRelation = null;
    /**
     * 店舗情報登録画面用FocusTraversalPolicy
     */
    private MstShopFocusTraversalPolicy	ftp = new MstShopFocusTraversalPolicy();

    /**
     * 店舗情報登録画面用FocusTraversalPolicyを取得する。
     *
     * @return 店舗情報登録画面用FocusTraversalPolicy
     */
    public MstShopFocusTraversalPolicy	getFocusTraversalPolicy() {
        return	ftp;
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(searchAddressButton);
        SystemInfo.addMouseCursorChange(renewButton);
        SystemInfo.addMouseCursorChange(clearButton);
        SystemInfo.addMouseCursorChange(RegistmailButton);
    }

    /**
     * コンポーネントの各リスナーをセットする。
     */
    private void setListener() {
        address1.addKeyListener(SystemInfo.getMoveNextField());
        address1.addFocusListener(SystemInfo.getSelectText());
        address2.addKeyListener(SystemInfo.getMoveNextField());
        address2.addFocusListener(SystemInfo.getSelectText());
        address3.addKeyListener(SystemInfo.getMoveNextField());
        address3.addFocusListener(SystemInfo.getSelectText());
        address4.addKeyListener(SystemInfo.getMoveNextField());
        address4.addFocusListener(SystemInfo.getSelectText());
        closeHour.addKeyListener(SystemInfo.getMoveNextField());
        closeMinute.addKeyListener(SystemInfo.getMoveNextField());
        cutoffDay.addKeyListener(SystemInfo.getMoveNextField());
        cutoffDay.addFocusListener(SystemInfo.getSelectText());
        faxNumber.addKeyListener(SystemInfo.getMoveNextField());
        faxNumber.addFocusListener(SystemInfo.getSelectText());
        mailAddress.addKeyListener(SystemInfo.getMoveNextField());
        mailAddress.addFocusListener(SystemInfo.getSelectText());
        openHour.addKeyListener(SystemInfo.getMoveNextField());
        openMinute.addKeyListener(SystemInfo.getMoveNextField());
        phoneNumber.addKeyListener(SystemInfo.getMoveNextField());
        phoneNumber.addFocusListener(SystemInfo.getSelectText());
        postalCode.addKeyListener(SystemInfo.getMoveNextField());
        postalCode.addFocusListener(SystemInfo.getSelectText());
        bedUse.addKeyListener(SystemInfo.getMoveNextField());
        bedNoUse.addKeyListener(SystemInfo.getMoveNextField());
        proportionallyUse.addKeyListener(SystemInfo.getMoveNextField());
        proportionallyNoUse.addKeyListener(SystemInfo.getMoveNextField());
        displayProportionallyUse.addKeyListener(SystemInfo.getMoveNextField());
        displayProportionallyNoUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationNullLineUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationNullLineNoUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationMenuUpdateUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationMenuUpdateNoUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationStaffShiftUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationStaffShiftNoUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationStaffHolidayUse.addKeyListener(SystemInfo.getMoveNextField());
        reservationStaffHolidayNoUse.addKeyListener(SystemInfo.getMoveNextField());
        shiftGearWorkingUse.addKeyListener(SystemInfo.getMoveNextField());
        shiftGearWorkingNoUse.addKeyListener(SystemInfo.getMoveNextField());
        designatedAssistUse.addKeyListener(SystemInfo.getMoveNextField());
        designatedAssistNoUse.addKeyListener(SystemInfo.getMoveNextField());
        autoNumberUse.addKeyListener(SystemInfo.getMoveNextField());
        autoNumberNoUse.addKeyListener(SystemInfo.getMoveNextField());
        prefixString.addKeyListener(SystemInfo.getMoveNextField());
        prefixString.addFocusListener(SystemInfo.getSelectText());
        seqLength.addKeyListener(SystemInfo.getMoveNextField());
        round.addKeyListener(SystemInfo.getMoveNextField());
        roundDown.addKeyListener(SystemInfo.getMoveNextField());
        roundUp.addKeyListener(SystemInfo.getMoveNextField());
        shopName.addKeyListener(SystemInfo.getMoveNextField());
        shopName.addFocusListener(SystemInfo.getSelectText());
        term.addKeyListener(SystemInfo.getMoveNextField());
        unitOne.addKeyListener(SystemInfo.getMoveNextField());
        unitTen.addKeyListener(SystemInfo.getMoveNextField());
        birthMonth.addKeyListener(SystemInfo.getMoveNextField());
        birthDay.addKeyListener(SystemInfo.getMoveNextField());
        birthBeforeDays.addKeyListener(SystemInfo.getMoveNextField());
        birthBeforeDays.addFocusListener(SystemInfo.getSelectText());
        birthAfterDays.addKeyListener(SystemInfo.getMoveNextField());
        birthAfterDays.addFocusListener(SystemInfo.getSelectText());
        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        //An start add 20130322 有効期限間近
        coursedisplayday.addKeyListener(SystemInfo.getMoveNextField());
        coursedisplayday.addFocusListener(SystemInfo.getSelectText());
        startTab.addKeyListener(SystemInfo.getMoveNextField());
        startTab.addFocusListener(SystemInfo.getSelectText());
        //An end add 20130233 有効期限間近
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
    }

    private void changeCurrentShop() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) groupTree.getLastSelectedPathComponent();

        if (node != null) {
            //グループが選択された場合
            if (node.getUserObject() instanceof MstGroup) {
                shop = new MstShop();

                MstGroup group = (MstGroup) node.getUserObject();

                if (group != null) {
                    shop.setGroupID(group.getGroupID());
                }
            } //店舗が選択された場合
            else if (node.getUserObject() instanceof MstShop) {
                MstShop temp = (MstShop) node.getUserObject();

                if (temp != null) {
                    shop.setData(temp);
                }
            }
        } else {
            shop = new MstShop();
        }

        this.showData();
        // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
        this.initShopRelation();
        // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録

        renewButton.setEnabled(shop.getShopID() != null);
    }

    /**
     * 店舗マスタからデータを読み込む。
     *
     * @return 成功時trueを返す。
     */
    private boolean loadShop() {
        shop.clear();

        shop.setShopID(SystemInfo.getCurrentShop().getShopID());

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            if (!shop.load(con)) {
                return false;
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }
    
    // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
    /**
     * 店舗業態関連マスタデータを設定する
     */
    private void initShopRelation() {
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            listShopRelation = new MstShopRelations();
            listShopRelation.loadAllByShop(con, shop.getShopID());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        SwingUtil.clearTable(shopRelationTable);
        DefaultTableModel model = (DefaultTableModel) shopRelationTable.getModel();
        
        for (MstShopRelation single : listShopRelation) {
            Object[] temp = {single.getShopClassName(),
                getShopRelationCheckbox(single.getShopId() == 0 ? false : true, useCategory.isSelected())};
            model.addRow(temp);
        }
        if (shop.getUseShopCategory() == null || shop.getUseShopCategory() == 0) {
            notUseCategory.setSelected(true);
        } else {
            useCategory.setSelected(true);
        }
    }

    /**
     * 選択チェックボックスを取得する
     * @param relation 選択チェックボックスの状況
     * @param useCategoryFlg 業態利用フラグ
     * @return 選択チェックボックス
     */
    private JCheckBox getShopRelationCheckbox(boolean relation, boolean useCategoryFlg) {
        JCheckBox relationCheckbox = new JCheckBox();
        relationCheckbox.setSelected(relation);
        relationCheckbox.setOpaque(false);
        relationCheckbox.setHorizontalAlignment(SwingConstants.CENTER);
        relationCheckbox.setEnabled(useCategoryFlg);
        return relationCheckbox;
    }
    // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録
    
    private void initGroup() {
        try {
            //グループマスタをデータベースから読み込む
            SystemInfo.getGroup().loadData(SystemInfo.getConnection());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void setGroup() {
        //Tree
        DefaultTreeModel treeModel = new DefaultTreeModel(
                SystemInfo.getGroup().createTreeNode(true));
        groupTree.setModel(treeModel);
        SwingUtil.expandJTree(groupTree);
    }

    /**
     * 表示をクリアする
     */
    private void clear() {
        shop = new MstShop();
        shop.setGroupID(1);
        this.showData();
        // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
        this.initShopRelation();
        // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録
    }

    /**
     * shopのデータを表示する。
     */
    private void showData() {
        if (shop == null) {
            return;
        }

        shopName.setText(shop.getShopName());
        this.setParent(shop.getGroupID());
        postalCode.setText((shop.getPostalCode() == null ? "" : shop.getPostalCode()));
        address1.setText((shop.getAddress(0) == null ? "" : shop.getAddress(0)));
        address2.setText((shop.getAddress(1) == null ? "" : shop.getAddress(1)));
        address3.setText((shop.getAddress(2) == null ? "" : shop.getAddress(2)));
        address4.setText((shop.getAddress(3) == null ? "" : shop.getAddress(3)));
        phoneNumber.setText((shop.getPhoneNumber() == null ? "" : shop.getPhoneNumber()));
        faxNumber.setText((shop.getFaxNumber() == null ? "" : shop.getFaxNumber()));
        mailAddress.setText((shop.getMailAddress() == null ? "" : shop.getMailAddress()));

        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        //An start add 20130328 店舗情報登録
        this.startTab.setSelectedIndex(shop.getTabIndex());
        //An end add 20130328 店舗情報登録
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ

        //会計単位
        if (shop.getAccountingUnit() == null) {
            unitOne.setSelected(true);
            unitTen.setSelected(false);
        } else {
            switch (shop.getAccountingUnit()) {
                //１円単位
                case 1:
                    unitOne.setSelected(true);
                    break;
                //１０円単位
                case 2:
                    unitTen.setSelected(true);
                    break;
            }
        }
        //端数丸め
        if (shop.getRounding() == null) {
            roundDown.setSelected(true);
            round.setSelected(false);
            roundUp.setSelected(false);
        } else {
            switch (shop.getRounding()) {
                //切り捨て
                case 1:
                    roundDown.setSelected(true);
                    break;
                //四捨五入
                case 2:
                    round.setSelected(true);
                    break;
                //切り上げ
                case 3:
                    roundUp.setSelected(true);
                    break;
            }
        }
        if (shop.isBed()) {
            bedUse.setSelected(true);
        } else {
            bedNoUse.setSelected(true);
        }
        if (shop.isProportionally()) {
            proportionallyUse.setSelected(true);
        } else {
            proportionallyNoUse.setSelected(true);
        }
        if (shop.isDisplayProportionally()) {
            displayProportionallyUse.setSelected(true);
        } else {
            displayProportionallyNoUse.setSelected(true);
        }
        if (shop.isReservationNullLine()) {
            reservationNullLineUse.setSelected(true);
        } else {
            reservationNullLineNoUse.setSelected(true);
        }
        if (shop.isReservationMenuUpdate()) {
            reservationMenuUpdateUse.setSelected(true);
        } else {
            reservationMenuUpdateNoUse.setSelected(true);
        }
        if (shop.isReservationStaffShift()) {
            reservationStaffShiftUse.setSelected(true);
        } else {
            reservationStaffShiftNoUse.setSelected(true);
        }
        if (shop.isReservationStaffHoliday()) {
            reservationStaffHolidayUse.setSelected(true);
        } else {
            reservationStaffHolidayNoUse.setSelected(true);
        }
        if (shop.isShiftGearWorking()) {
            shiftGearWorkingUse.setSelected(true);
        } else {
            shiftGearWorkingNoUse.setSelected(true);
        }
        if (shop.isDesignatedAssist()) {
            designatedAssistUse.setSelected(true);
        } else {
            designatedAssistNoUse.setSelected(true);
        }
        if (shop.isAutoNumber()) {
            autoNumberUse.setSelected(true);
        } else {
            autoNumberNoUse.setSelected(true);
        }
        prefixString.setText((shop.getPrefixString() == null ? "" : shop.getPrefixString()));
        if (shop.getSeqLength() == null) {
            seqLength.setSelectedIndex(0);
        } else {
            seqLength.setSelectedItem(shop.getSeqLength());
        }
        setNumberingExample();

        //締め日
        cutoffDay.setText((shop.getCutoffDay() == null ? "" : shop.getCutoffDay().toString()));
        //開店時間（時）
        //An start add 20130322 有効期限間近
        coursedisplayday.setText((shop.getCourseDisplayDay() == null ? "" : shop.getCourseDisplayDay().toString()));
        //An end add 20130322 有効期限間近

        //開店時間（時）
        if (shop.getOpenHour() == null) {
            openHour.setSelectedIndex(0);
        } else {
            openHour.setSelectedItem(shop.getOpenHour());
        }
        //開店時間（分）
        if (shop.getOpenMinute() == null) {
            openMinute.setSelectedIndex(0);
        } else {
            openMinute.setSelectedItem(shop.getOpenMinute());
        }
        //閉店時間（時）
        if (shop.getCloseHour() == null) {
            closeHour.setSelectedIndex(0);
        } else {
            closeHour.setSelectedItem(shop.getCloseHour());
        }
        //閉店時間（分）
        if (shop.getCloseMinute() == null) {
            closeMinute.setSelectedIndex(0);
        } else {
            closeMinute.setSelectedItem(shop.getCloseMinute());
        }
        //表示単位
        if (shop.getTerm() == null) {
            term.setSelectedIndex(0);
        } else {
            term.setSelectedItem(shop.getTerm());
        }

        //誕生日マーク表示設定
        if (shop.isBirthMonthFlag()) {
            birthMonth.setSelected(true);
        } else {
            birthDay.setSelected(true);
        }
        birthBeforeDays.setText((shop.getBirthBeforeDays() == null ? "" : shop.getBirthBeforeDays().toString()));
        birthAfterDays.setText((shop.getBirthAfterDays() == null ? "" : shop.getBirthAfterDays().toString()));
        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        // 全予約バッファ時間
        
        if (shop.getReservationBufferTime() == null) {
            this.reservationBufferTime.setText("");
        } else {
            this.reservationBufferTime.setText(shop.getReservationBufferTime().toString());
        }

        // 二重予約警告(ﾀﾞﾌﾞﾙﾌﾞｯｯｷﾝｸﾞ)
        if (shop.isReservationDuplicateWarningFlag()) {
            reservationDuplicateWarningFlagUse.setSelected(true);
        } else {
            reservationDuplicateWarningFlagNoUse.setSelected(true);
        }
        // 予約率表示
        if (shop.isDisplayRreservationRatoFlag()) {
            displayReservationRatoFlagUse.setSelected(true);
        } else {
            displayReservationRatoFlagNoUse.setSelected(true);
        }
        // しきい値赤色
        if (shop.getDisplayReaservationRatoRed() == null) {
            this.displayReaservationRatoRed.setText("");
        } else {
            this.displayReaservationRatoRed.setText(shop.getDisplayReaservationRatoRed().toString());
        }
        // しきい値黄色
        if (shop.getDisplayReaservationRatoYellow() == null) {
            this.displayReaservationRatoYellow.setText("");
        } else {
            this.displayReaservationRatoYellow.setText(shop.getDisplayReaservationRatoYellow().toString());
        }

        // エリア
        if (shop.getAreaId() != null) {
            setAreaItemSelected(shop.getAreaId());
        } else {
            if (this.cmbAreaUpdate.getItemCount() > 0) {
                this.cmbAreaUpdate.setSelectedIndex(0);
            } else {
                try {
                    MstArea mstArea = new MstArea();
                    mstArea.getAreaByID(SystemInfo.getConnection());
                    this.cmbAreaUpdate.addItem(mstArea);
                    jLabel47.setText("");
                    if (mstArea.getID() != null && mstArea.getName() != null) {
                        if (!"".equals(mstArea.getName())) {
                            jLabel47.setText(mstArea.getName() + "エリア");
                        }
                    }
                } catch (Exception e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }
        }
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
        // start add 20130802 nakhoa 役務機能使用有無の制御追加
        if (shop.getShopID() != null) {
            if (shop.getCourseFlag() == 0) {
                this.courseFlagNoUse.setSelected(true);
            } else {
                this.courseFlagUse.setSelected(true);
            }
        } else {
            this.courseFlagNoUse.setSelected(true);
        }
        // end add 20130802 nakhoa 役務機能使用有無の制御追加
        //vtbphuong start add 20140421 Request #22456
        if (shop.getShopID() != null) {
            if (shop.getReservationOverbookingFlag() == 0) {
                this.overBookingUse.setSelected(true);

            } else {
                this.overBookingNotUse.setSelected(true);
            }
        } else {
            this.overBookingUse.setSelected(true);
        }
        //vtbphuong end add 20140421 Request #22456

    }
   
    /**
     * グループを設定する
     */
    private void setParent(Integer groupID) {
        for (int i = 0; i < parentGroup.getItemCount(); i++) {
            MstGroup mg = (MstGroup) parentGroup.getItemAt(i);

            if ((mg == null && groupID == null)
                    || (mg != null && mg.getGroupID() == groupID)) {
                parentGroup.setSelectedIndex(i);
                return;
            }
        }

        parentGroup.setSelectedIndex(0);
    }

    private void initTabbIndexCombox() {
        startTab.removeAllItems();
        startTab.addItem("基本情報");
        startTab.addItem("来店情報");
        startTab.addItem("契約履歴");
        startTab.addItem("DM・メール履歴");

        startTab.setSelectedIndex(0);

        //when save to use 
        //startTab.getSelectedIndex();





    }
    //An end add 20130325    店舗情報登録

    /**
     * 入力チェックを行う。
     *
     * @return 成功時trueを返す。
     */
    private boolean checkInput() {
        //店舗名
        if (shopName.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "店舗名"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            shopName.requestFocusInWindow();
            return false;
        }
        //締日
        if (cutoffDay.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "締日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cutoffDay.requestFocusInWindow();
            return false;
        }
        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        //An start add 20130322 有効期限間近
        if (coursedisplayday.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "有効期限が日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            coursedisplayday.requestFocusInWindow();
            return false;
        }
        //An end add 20130322 有効期限間近
        // start add 20130115 nakhoa 店舗情報登録
        // しきい値赤色
        if (displayReaservationRatoRed.getText().equals("")) {
            return true;
        } else {
            if (!CheckUtil.isNumber(displayReaservationRatoRed.getText())) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "しきい値赤色"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                displayReaservationRatoRed.requestFocusInWindow();
                return false;
            }
        }
        // しきい値黄色
        if (displayReaservationRatoYellow.getText().equals("")) {
            return true;
        } else {
            if (!CheckUtil.isNumber(displayReaservationRatoYellow.getText())) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "しきい値黄色"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                displayReaservationRatoYellow.requestFocusInWindow();
                return false;
            }
        }
        // 全予約バッファ時間
        if (reservationBufferTime.getText().equals("")) {
            return true;
        } else {
            if (!CheckUtil.isNumber(reservationBufferTime.getText())) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "全予約バッファ時間"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                reservationBufferTime.requestFocusInWindow();
                return false;
            }
        }
        // end add 20130115 nakhoa 店舗情報登録
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
        //営業時間
        if ((Integer) openHour.getSelectedIndex() > (Integer) closeHour.getSelectedIndex()
                || ((Integer) openHour.getSelectedIndex() == (Integer) closeHour.getSelectedIndex()
                && (Integer) openMinute.getSelectedIndex() > (Integer) closeMinute.getSelectedIndex())) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "営業時間"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            openHour.requestFocusInWindow();
            return false;
        }
        if ((Integer) closeHour.getSelectedIndex() - (Integer) openHour.getSelectedIndex() > 24
                || ((Integer) closeHour.getSelectedIndex() - (Integer) openHour.getSelectedIndex() == 24
                && (Integer) openMinute.getSelectedIndex() > (Integer) closeMinute.getSelectedIndex())) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "営業時間"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            openHour.requestFocusInWindow();
            return false;
        }

        return true;
    }
    // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
    // start Add 20130208 nakhoa SetVisible control if Ispot Customer

    private void setVisibleControlIspotShop() {
        this.jLabel25.setVisible(false);
        this.proportionallyUse.setVisible(false);
        this.proportionallyNoUse.setVisible(repeat);
        this.displayProportionallyLabel.setVisible(repeat);
        this.displayProportionallyUse.setVisible(repeat);
        this.displayProportionallyNoUse.setVisible(false);
        this.jLabel31.setVisible(false);
        this.reservationMenuUpdateUse.setVisible(false);
        this.reservationMenuUpdateNoUse.setVisible(false);
        this.jLabel42.setVisible(false);
        this.shiftGearWorkingUse.setVisible(false);
        this.shiftGearWorkingNoUse.setVisible(false);
        this.jLabel34.setVisible(false);
        this.birthMonth.setVisible(false);
        this.birthDay.setVisible(false);
        this.birthAfterDays.setVisible(false);
        this.birthBeforeDaysLabel.setVisible(false);
        this.birthBeforeDays.setVisible(false);
        this.birthAfterDaysLabel.setVisible(false);
        this.jLabel40.setVisible(false);
        this.cutoffDay.setLocation(60, 240);
        //An start add 20130322 有効期限間近
        this.coursedisplayday.setLocation(60, 240);
        //An end add 20130233 有効期限間近
        this.jLabel11.setLocation(0, 240);
        this.jLabel13.setLocation(0, 270);
        this.openHour.setLocation(60, 270);
        this.jLabel16.setLocation(100, 270);
        this.openMinute.setLocation(130, 270);
        this.jLabel17.setLocation(170, 270);
        this.closeHour.setLocation(220, 270);
        this.jLabel14.setLocation(260, 270);
        this.closeMinute.setLocation(290, 270);
        this.jLabel20.setLocation(330, 270);
        this.jLabel12.setLocation(100, 240);
        this.jLabel18.setLocation(190, 270);
        this.RegistmailButton.setIcon(null);
        this.RegistmailButton.setText("");
        this.RegistmailButton.setEnabled(false);
    }

    private void setVisibleNons() {
        this.jLabel48.setVisible(false);
        this.displayReservationRatoFlagUse.setVisible(false);
        this.displayReservationRatoFlagNoUse.setVisible(false);
        this.displayReaservationRatoRed.setVisible(false);
        this.displayReaservationRatoYellow.setVisible(false);
        this.jLabel46.setVisible(false);
        this.jLabel49.setVisible(false);
        this.jLabel44.setVisible(false);
    }
    // end Add 20130208 nakhoa SetVisible control if Ispot Customer
    // IVS SANG END INSERT 20131030 [gbソース]ソースマージ

    /**
     * shopに入力されたデータをセットする。
     */
    private void setData() {
        this.shop.setGroupID(((MstGroup) parentGroup.getSelectedItem()).getGroupID());
        this.shop.setShopName(this.shopName.getText());
        this.shop.setPostalCode(this.getPostalCode());
        this.shop.setAddress(0, this.address1.getText());
        this.shop.setAddress(1, this.address2.getText());
        this.shop.setAddress(2, this.address3.getText());
        this.shop.setAddress(3, this.address4.getText());
        this.shop.setPhoneNumber(this.phoneNumber.getText());
        this.shop.setFaxNumber(this.faxNumber.getText());
        this.shop.setMailAddress(this.mailAddress.getText());
        this.shop.setAccountingUnit(this.getAccountingUnit());
        this.shop.setRounding(this.getRounding());
        this.shop.setCutoffDay(this.getCutoffDay());
        this.shop.setOpenHour((Integer) openHour.getSelectedItem());
        this.shop.setOpenMinute((Integer) openMinute.getSelectedItem());
        this.shop.setCloseHour((Integer) closeHour.getSelectedItem());
        this.shop.setCloseMinute((Integer) closeMinute.getSelectedItem());
        this.shop.setTerm((Integer) term.getSelectedItem());
        this.shop.setBed(this.getBed());
        this.shop.setProportionally(this.getProportionally());
        this.shop.setDisplayProportionally(this.getDisplayProportionally());
        this.shop.setReservationNullLine(this.getReservationNullLine());
        this.shop.setReservationMenuUpdate(this.getReservationMenuUpdate());
        this.shop.setReservationStaffShift(this.getReservationStaffShift());
        this.shop.setReservationStaffHoliday(this.getReservationStaffHoliday());
        this.shop.setShiftGearWorking(this.getShiftGearWorking());
        this.shop.setDesignatedAssist(this.getDesignatedAssist());
        this.shop.setAutoNumber(this.getAutoNumber());
        this.shop.setPrefixString(this.prefixString.getText());
        this.shop.setSeqLength((Integer) seqLength.getSelectedItem());
        this.shop.setBirthMonthFlag(this.getBirthMonthFlag());
        this.shop.setBirthBeforeDays(this.getBirthBeforeDays());
        this.shop.setBirthAfterDays(this.getBirthAfterDays());
        // 二重予約警告(ﾀﾞﾌﾞﾙﾌﾞｯｯｷﾝｸﾞ)
        if (reservationDuplicateWarningFlagUse.isSelected()) {
            this.shop.setReservationDuplicateWarningFlag(1);
        } else {
            if (reservationDuplicateWarningFlagNoUse.isSelected()) {
                this.shop.setReservationDuplicateWarningFlag(0);
            } else {
                this.shop.setReservationDuplicateWarningFlag(null);
            }
        }
        // 予約率表示
        if (displayReservationRatoFlagUse.isSelected()) {
            this.shop.setDisplayRreservationRatoFlag(1);
        } else {
            if (displayReservationRatoFlagNoUse.isSelected()) {
                this.shop.setDisplayRreservationRatoFlag(0);
            } else {
                this.shop.setDisplayRreservationRatoFlag(null);
            }
        }
        // しきい値赤色
        if (!"".equals(this.displayReaservationRatoRed.getText().trim())) {
            this.shop.setDisplayReaservationRatoRed(Integer.parseInt(this.displayReaservationRatoRed.getText().trim(), 10));
        } else {
            this.shop.setDisplayReaservationRatoRed(null);
        }
        // しきい値黄色
        if (!"".equals(this.displayReaservationRatoYellow.getText().trim())) {
            this.shop.setDisplayReaservationRatoYellow(Integer.parseInt(this.displayReaservationRatoYellow.getText().trim(), 10));
        } else {
            this.shop.setDisplayReaservationRatoYellow(null);
        }
        // 全予約バッファ時間
        if (!"".equals(this.reservationBufferTime.getText().trim())) {
            this.shop.setReservationBufferTime(Integer.parseInt(this.reservationBufferTime.getText().trim(), 10));
        } else {
            this.shop.setReservationBufferTime(null);
        }
        // エリア
        if (this.cmbAreaUpdate.getSelectedIndex() > 0) {
            MstArea mstArea = (MstArea) this.cmbAreaUpdate.getSelectedItem();
            if (mstArea != null) {
                this.shop.setAreaId(mstArea.getID());
            }
        } else {
            this.shop.setAreaId(null);
        }
        // 推薦フラグ
        if (this.jCheckBoxRecommendationFlag.isSelected()) {
            this.shop.setRecommendationFlag(1);
        } else {
            this.shop.setRecommendationFlag(0);
        }
        // start add 20130802 nakhoa 役務機能使用有無の制御追加
        if (this.courseFlagUse.isSelected()) {
            this.shop.setCourseFlag(1);
        } else {
            this.shop.setCourseFlag(0);
        }
        // end add 20130802 nakhoa 役務機能使用有無の制御追加

        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        //An start add 20130322 有効期限間近
        this.shop.setCourseDisplayDay(this.getCourseDisplayDay());
        this.shop.setTabIndex(startTab.getSelectedIndex());
        //An end add 20130322 有効期限間近
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ

        //vtbphuong start add 20130421 Request #22456
        this.shop.setReservationOverbookingFlag(this.getReservationOverBooking());
        //vtbphuong end add 20130421 Request #22456
       
    }

    // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
    //An start add 20130322 有効期限間近
    private Integer getCourseDisplayDay() {
        if (this.coursedisplayday.getText().equals("")) {
            return null;
        } else {
            return Integer.valueOf(this.coursedisplayday.getText());
        }
    }
    //An end add 20130322 有効期限間近
    // IVS SANG END INSERT 20131030 [gbソース]ソースマージ

    /**
     * 入力されている郵便番号を取得する。
     *
     * @return 入力されている郵便番号
     */
    private String getPostalCode() {
        return this.postalCode.getText().replaceAll("[-_]", "");
    }

    /**
     * 選択されている会計単位を取得する。
     *
     * @return 選択されている会計単位
     */
    private Integer getAccountingUnit() {
        //１円単位
        if (this.unitOne.isSelected()) {
            return 1;
        } //１０円単位
        else if (this.unitTen.isSelected()) {
            return 2;
        } else {
            return null;
        }
    }

    /**
     * 選択されている端数丸めを取得する。
     *
     * @return 選択されている端数丸め
     */
    private Integer getRounding() {
        //切り捨て
        if (this.roundDown.isSelected()) {
            return 1;
        } //四捨五入
        else if (this.round.isSelected()) {
            return 2;
        } //切り上げ
        else if (this.roundUp.isSelected()) {
            return 3;
        } else {
            return null;
        }
    }

    private Integer getBed() {
        if (this.bedUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getProportionally() {
        if (this.proportionallyUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getDisplayProportionally() {
        if (this.displayProportionallyUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getReservationNullLine() {
        if (this.reservationNullLineUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getReservationMenuUpdate() {
        if (this.reservationMenuUpdateUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getReservationStaffShift() {
        if (this.reservationStaffShiftUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getReservationStaffHoliday() {
        if (this.reservationStaffHolidayUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getShiftGearWorking() {
        if (this.shiftGearWorkingUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getDesignatedAssist() {
        if (this.designatedAssistUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getAutoNumber() {
        if (this.autoNumberUse.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getBirthMonthFlag() {
        if (this.birthMonth.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer getBirthBeforeDays() {
        if (this.birthBeforeDays.getText().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(this.birthBeforeDays.getText());
        }
    }

    private Integer getBirthAfterDays() {
        if (this.birthAfterDays.getText().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(this.birthAfterDays.getText());
        }
    }

    /**
     * 入力されている締め日を取得する。
     *
     * @return 入力されている締め日
     */
    private Integer getCutoffDay() {
        if (this.cutoffDay.getText().equals("")) {
            return null;
        } else {
            return Integer.valueOf(this.cutoffDay.getText());
        }
    }

    private Integer getReservationOverBooking() {
        if (this.overBookingUse.isSelected()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * データを登録する。
     */
    private void regist(boolean isInsert) {
        this.setData();

        if (isInsert) {
            shop.setShopID(null);
        }

        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            con.begin();
            // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
            boolean isExist = validateDataInStaffOrBed(con,useCategory.isSelected(),shop.getShopID());
            if (isExist) {
                this.initShopRelation();
                return;
            }
            this.shop.setUseShopCategory(useCategory.isSelected() ? 1 : 0);
            // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録
            if (shop.regist(con) && registShopRelation(con,isExist)) {
                con.commit();

                this.initGroup();

                if (SystemInfo.getCurrentShop().getShopID() == 0) {
                    this.setGroup();
                    this.clear();
                } else {
                    SystemInfo.getCurrentShop().load(con);
                }

                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(7011),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                con.rollback();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "店舗マスタ"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        this.initShopRelation();
        //this.showData();
    }

    /**
     * 店舗業態関連マスタデータを登録する
     * @param con ConnectionWrapper
     * @param isExist boolean
     * @return success or fail
     */
    // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
    private boolean registShopRelation(ConnectionWrapper con, boolean isExist) throws SQLException {
        boolean result = true;
        boolean useCategoryFlg = useCategory.isSelected();
        if (shop.getUseShopCategory() == 0 || !isExist) {
            listShopRelation.deleteByShop(con, shop.getShopID());
            if (useCategoryFlg) {
                for (int i = 0; i < listShopRelation.size(); i++) {
                    DefaultTableModel model = (DefaultTableModel) shopRelationTable.getModel();
                    listShopRelation.get(i).setShopId(shop.getShopID());
                    if (((JCheckBox) model.getValueAt(i, 1)).isSelected()) {
                        result = listShopRelation.get(i).regist(con);
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Check data in staff and bed table
     * @param con ConnectionWrapper
     * @param useCategoryFlg boolean
     * @param shopID Integer
     * @return 結果　true or false
     * @throws SQLException 
     */
    private boolean validateDataInStaffOrBed(ConnectionWrapper con,boolean useCategoryFlg,Integer shopID) throws SQLException{
        if (shop.getUseShopCategory() == 1) {
            String categoryStr = "";
                if (useCategoryFlg) {
                    for (int i = 0; i < listShopRelation.size(); i++) {
                        DefaultTableModel model = (DefaultTableModel) shopRelationTable.getModel();
                        if (listShopRelation.get(i).getShopId() != 0 && !((JCheckBox) model.getValueAt(i, 1)).isSelected()) {
                            categoryStr += listShopRelation.get(i).getShopCategoryId();
                            if (listShopRelation.checkExistInStaffOrBed(con, categoryStr,shopID)) {
                                MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_MESSAGE),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                                return true;
                            }
                        }
                    }
                }else{
                    for (int i = 0; i < listShopRelation.size(); i++) {
                        if (listShopRelation.get(i).getShopId() != 0) {
                            categoryStr += listShopRelation.get(i).getShopCategoryId()+",";
                        }
                    }
                    if(!categoryStr.equals("")){
                        categoryStr = categoryStr.substring(0,categoryStr.length()-1);
                        if (listShopRelation.checkExistInStaffOrBed(con, categoryStr,shopID)) {
                            MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_REGIST_MESSAGE),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                            return true;
                        }
                    }
                }
        }
        return false;
    }
    // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録
    
    /**
     * 店舗マスタデータを削除する。
     */
    private void delete() {
        this.setData();

        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            con.begin();

            if (shop.delete(con)) {
                con.commit();

                this.initGroup();
                this.setGroup();
                this.clear();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_DELETE_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                con.rollback();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED, "店舗マスタ"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * 表示単位を初期化する。
     */
    private void initTerm() {
        term.addItem(10);
        term.addItem(15);
        term.addItem(20);
        term.addItem(30);
    }

    public MstShop getShop() {
        return shop;
    }

    /**
     * MstShopPanel用FocusTraversalPolicy
     */
    private class MstShopFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。 aContainer は aComponent
         * のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         *
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            if (aComponent.equals(shopName)) {
                return postalCode;
            } else if (aComponent.equals(postalCode)) {
                return address1;
            } else if (aComponent.equals(searchAddressButton)) {
                return address1;
            } else if (aComponent.equals(address1)) {
                return address2;
            } else if (aComponent.equals(address2)) {
                return address3;
            } else if (aComponent.equals(address3)) {
                return address4;
            } else if (aComponent.equals(address4)) {
                return phoneNumber;
            } else if (aComponent.equals(phoneNumber)) {
                return faxNumber;
            } else if (aComponent.equals(faxNumber)) {
                return mailAddress;
            } else if (aComponent.equals(mailAddress)) {
                if (unitTen.isSelected()) {
                    return unitTen;
                } else {
                    return unitOne;
                }
            } else if (aComponent.equals(unitOne)) {
                if (bedUse.isSelected()) {
                    return bedUse;
                } else {
                    return bedNoUse;
                }
            } else if (aComponent.equals(unitTen)) {
                if (bedUse.isSelected()) {
                    return bedUse;
                } else {
                    return bedNoUse;
                }
            } else if (aComponent.equals(bedUse)) {
                if (proportionallyUse.isSelected()) {
                    return proportionallyUse;
                } else {
                    return proportionallyNoUse;
                }
            } else if (aComponent.equals(bedNoUse)) {
                if (proportionallyUse.isSelected()) {
                    return proportionallyUse;
                } else {
                    return proportionallyNoUse;
                }
            } else if (aComponent.equals(proportionallyUse)) {
                if (round.isSelected()) {
                    return round;
                } else if (roundUp.isSelected()) {
                    return roundUp;
                } else {
                    return roundDown;
                }
            } else if (aComponent.equals(proportionallyNoUse)) {
                if (round.isSelected()) {
                    return round;
                } else if (roundUp.isSelected()) {
                    return roundUp;
                } else {
                    return roundDown;
                }
            } else if (aComponent.equals(prefixString)) {
                return seqLength;
            } else if (aComponent.equals(roundDown)) {
                return cutoffDay;
            } else if (aComponent.equals(round)) {
                return cutoffDay;
            } else if (aComponent.equals(roundUp)) {
                return cutoffDay;
            } else if (aComponent.equals(cutoffDay)) {
                return openHour;
            } else if (aComponent.equals(openHour)) {
                return openMinute;
            } else if (aComponent.equals(openMinute)) {
                return closeHour;
            } else if (aComponent.equals(closeHour)) {
                return closeMinute;
            } else if (aComponent.equals(seqLength)) {
                return birthAfterDays;
            } else if (aComponent.equals(birthAfterDays)) {
                return birthBeforeDays;
            } else if (aComponent.equals(birthBeforeDays)) {
                return cutoffDay;
            }

            return shopName;
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
            if (aComponent.equals(shopName)) {
                return shopName;
            } else if (aComponent.equals(postalCode)) {
                return shopName;
            } else if (aComponent.equals(searchAddressButton)) {
                return postalCode;
            } else if (aComponent.equals(address1)) {
                return postalCode;
            } else if (aComponent.equals(address2)) {
                return address1;
            } else if (aComponent.equals(address3)) {
                return address2;
            } else if (aComponent.equals(address4)) {
                return address3;
            } else if (aComponent.equals(phoneNumber)) {
                return address4;
            } else if (aComponent.equals(faxNumber)) {
                return phoneNumber;
            } else if (aComponent.equals(mailAddress)) {
                return faxNumber;
            } else if (aComponent.equals(unitOne)) {
                return mailAddress;
            } else if (aComponent.equals(unitTen)) {
                return mailAddress;
            } else if (aComponent.equals(bedUse)) {
                if (unitTen.isSelected()) {
                    return unitTen;
                } else {
                    return unitOne;
                }
            } else if (aComponent.equals(bedNoUse)) {
                if (unitTen.isSelected()) {
                    return unitTen;
                } else {
                    return unitOne;
                }
            } else if (aComponent.equals(proportionallyUse)) {
                if (bedUse.isSelected()) {
                    return bedUse;
                } else {
                    return bedNoUse;
                }
            } else if (aComponent.equals(proportionallyNoUse)) {
                if (bedUse.isSelected()) {
                    return bedUse;
                } else {
                    return bedNoUse;
                }
            } else if (aComponent.equals(roundDown)) {
                if (proportionallyUse.isSelected()) {
                    return proportionallyUse;
                } else {
                    return proportionallyNoUse;
                }
            } else if (aComponent.equals(round)) {
                if (proportionallyUse.isSelected()) {
                    return proportionallyUse;
                } else {
                    return proportionallyNoUse;
                }
            } else if (aComponent.equals(roundUp)) {
                if (proportionallyUse.isSelected()) {
                    return proportionallyUse;
                } else {
                    return proportionallyNoUse;
                }
            } else if (aComponent.equals(cutoffDay)) {
                return seqLength;
            } else if (aComponent.equals(seqLength)) {
                return prefixString;
            } else if (aComponent.equals(openHour)) {
                return cutoffDay;
            } else if (aComponent.equals(openMinute)) {
                return openHour;
            } else if (aComponent.equals(closeHour)) {
                return openMinute;
            } else if (aComponent.equals(closeMinute)) {
                return closeHour;
            } else if (aComponent.equals(term)) {
                return closeMinute;
            }
            
            return shopName;
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
            return shopName;
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
            return closeMinute;
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
            return shopName;
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
            return shopName;
        }
    }

    private void setNumberingExample() {

        String seq = "";

        for (int i = 1; i < (Integer) seqLength.getSelectedItem(); i++) {
            seq += "0";
        }

        numberingExample.setText(prefixString.getText() + seq + "1");
    }
    // IVS_TTMLoan start add 20140707 MASHU_店舗情報登録
    /**
     * JTableの列幅を初期化する。
     */
    private void initTableColumnWidth() {
        //列の幅を設定する。
        shopRelationTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        shopRelationTable.getColumnModel().getColumn(1).setPreferredWidth(85);
    }
    // IVS_TTMLoan end add 20140707 MASHU_店舗情報登録
}

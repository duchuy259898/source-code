/*
 * MediaSettingPanel.java
 *
 * Created on 2016/05/11
 */
package com.geobeck.sosia.pos.hair.basicinfo.company;

import static java.util.concurrent.TimeUnit.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections.map.LinkedMap;

import com.geobeck.sosia.pos.hair.master.company.APIBed;
import com.geobeck.sosia.pos.hair.master.company.APIBeds;
import com.geobeck.sosia.pos.hair.master.company.APIShift;
import com.geobeck.sosia.pos.hair.master.company.APIShifts;
import com.geobeck.sosia.pos.hair.master.company.APIStaff;
import com.geobeck.sosia.pos.hair.master.company.APIStaffs;
import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.hair.master.company.MstAPIDetail;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.company.MstBeds;
import com.geobeck.sosia.pos.hair.master.company.MstKANZASHIBed;
import com.geobeck.sosia.pos.hair.master.company.MstKANZASHIMedia;
import com.geobeck.sosia.pos.hair.master.company.MstKANZASHIShift;
import com.geobeck.sosia.pos.hair.master.company.MstKANZASHIStaff;
import com.geobeck.sosia.pos.master.company.MstShift;
import com.geobeck.sosia.pos.master.company.MstShifts;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.google.gson.Gson;

/**
 *
 * @author lvtu
 */
public class MediaSettingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private     final int STAFF_TABLE   = 1;

    private     final int BED_TABLE     = 2;

    private     final int SHIFT_TABLE   = 3;

    private     final String LABLE_INVALID = "無効";

    private     final int SHOP_TYPE_2   = 2;

    private     final int SHOP_TYPE_3   = 3;

    private     final int HTTP_RESULT_200   = 200;

    //IVS_LVTu start add 2016/12/05 New request #58830
    private     final int HTTP_RESULT_202   = 202;
    //IVS_LVTu end add 2016/12/05 New request #58830

    private     final int HTTP_RESULT_500   = 500;

    private     final int HTTP_RESULT_599   = 599;

    private     final int HTTP_RESULT_403   = 403;

    private     final int COUNT_RETRY       = 5;

    private     final int STATUS_ONE        = 1;

    //nhtvinh start add 20161021 New request #54239
    private     final int STATUS_TWO        = 2;
    //nhtvinh end add 20161021 New request #54239

    private     final int STATUS_THREE       = 3;

    private     final int STATUS_FOUR       = 4;

    private     final int STATUS_SIX        = 6;
    
    private     final int STATUS_SEVEN      = 7;

    private     final int STATUS_EIGHT      = 8;

    private     final int STATUS_NINE       = 9;

    private     final int STATUS_TEN        = 10;

    private     final int STATUS_101        = 101;

    private     final int STATUS_102        = 102;

    private     final int TAB_SALON           = 0;

    private     final int TAB_STAFF           = 1;

    private     final int TAB_BED            = 2;

    private     final int TAB_SHIFT          = 3;

    private     final int TAB_MEDIA          = 4;

    private     final int TAB_RESERVATION    = 5;
    
    //LVTu start add 2016/11/21 New request #54178
    //5秒ごとに再実行する
    private     final int FIVE_SECONDS       = 5000;
    //LVTu end add 2016/11/21 New request #54178

    private     MstBeds bedsPos         = new MstBeds();

    private     MstStaffs staffsPos     = new MstStaffs();

    private     MstShifts shiftsPos     = new MstShifts();

    private     MstShop shop            = new MstShop();

    private     MstAPI mstApi           = new MstAPI();

    private     String selectedMedia    = "";

    private     ArrayList<MstKANZASHIStaff> staffMedia = new ArrayList<MstKANZASHIStaff>();

    private     ArrayList<MstKANZASHIBed> bedMedia = new ArrayList<MstKANZASHIBed>();

    private     ArrayList<MstKANZASHIShift> shiftMedia = new ArrayList<MstKANZASHIShift>();

    private     MstKANZASHIMedia media = new MstKANZASHIMedia();

    private     LinkedHashMap<Integer, Integer> staffPosMap = new LinkedHashMap<Integer, Integer>();

    private     LinkedHashMap<Integer, ObjectID> bedPosMap = new LinkedHashMap<Integer, ObjectID>();

    private     LinkedHashMap<Integer, ObjectID> shiftPosMap = new LinkedHashMap<Integer, ObjectID>();

    private     MediaSalonSetting       mediaSalon      = new MediaSalonSetting();

    private     boolean     messageFirst                = true;

    //nhtvinh start add 20161021 New request #54239
    private ScheduledExecutorService scheduler = null;

    private final int CONSTANT_SCHEDULE_USR_MEDIA = 5;

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //nhtvinh end add 20161021 New request #54239

    public MediaSettingPanel() {
        initComponents();
        addMouseCursorChange();

        try {

            if (SystemInfo.getCurrentShop().getShopID() == 0) {
                SystemInfo.initGroupShopComponents(cmbShop, SHOP_TYPE_2);
            } else {
                SystemInfo.initGroupShopComponents(cmbShop, SHOP_TYPE_3);
            }
        } catch (NullPointerException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        this.initMedia();
        this.initTableWidth();
        this.setSize(840, 700);
        this.setPath("基本設定＞ｻｲﾄｺﾝﾄﾛｰﾙ設定");
        this.setTitle("ｻｲﾄｺﾝﾄﾛｰﾙ設定");

        initData();
        loadData();
        //loadDataAPI();
        showData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroupSalon = new javax.swing.ButtonGroup();
        targetLabel = new javax.swing.JLabel();
        cmbShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        mainTab = new javax.swing.JTabbedPane();
        tabSalon = new javax.swing.JPanel();
        salonPanel = new javax.swing.JPanel();
        rdoValid = new javax.swing.JRadioButton();
        rdoStop = new javax.swing.JRadioButton();
        lbMedia = new javax.swing.JLabel();
        btRegistSalon = new javax.swing.JButton();
        tabStaff = new javax.swing.JPanel();
        groupPwBusinessScrollPane1 = new javax.swing.JScrollPane();
        tbStaff = new com.geobeck.swing.JTableEx();
        InfoStaffButton = new javax.swing.JButton();
        registStaffButton = new javax.swing.JButton();
        lbStaffPos = new javax.swing.JLabel();
        targetLabel12 = new javax.swing.JLabel();
        targetLabel21 = new javax.swing.JLabel();
        targetLabel22 = new javax.swing.JLabel();
        groupPwBusinessScrollPane6 = new javax.swing.JScrollPane();
        tbStaffPos = new com.geobeck.swing.JTableEx();
        lbStaffMedia = new javax.swing.JLabel();
        tabBed = new javax.swing.JPanel();
        targetLabel13 = new javax.swing.JLabel();
        registBedButton = new javax.swing.JButton();
        InfoBedButton = new javax.swing.JButton();
        groupPwBusinessScrollPane2 = new javax.swing.JScrollPane();
        tbBed = new com.geobeck.swing.JTableEx();
        targetLabel23 = new javax.swing.JLabel();
        targetLabel24 = new javax.swing.JLabel();
        groupPwBusinessScrollPane7 = new javax.swing.JScrollPane();
        tbBedPos = new com.geobeck.swing.JTableEx();
        lbBedPos = new javax.swing.JLabel();
        lbBedMedia = new javax.swing.JLabel();
        tabShift = new javax.swing.JPanel();
        registShiftButton = new javax.swing.JButton();
        targetLabel16 = new javax.swing.JLabel();
        groupPwBusinessScrollPane3 = new javax.swing.JScrollPane();
        tbShift = new com.geobeck.swing.JTableEx();
        InfoShiftButton = new javax.swing.JButton();
        targetLabel25 = new javax.swing.JLabel();
        targetLabel26 = new javax.swing.JLabel();
        groupPwBusinessScrollPane8 = new javax.swing.JScrollPane();
        tbShiftPos = new com.geobeck.swing.JTableEx();
        lbBedPos1 = new javax.swing.JLabel();
        lbBedMedia1 = new javax.swing.JLabel();
        tabMediaSetting = new javax.swing.JPanel();
        rdoUse = new javax.swing.JRadioButton();
        targetLabel9 = new javax.swing.JLabel();
        registMediaButton = new javax.swing.JButton();
        txtID = new com.geobeck.swing.JFormattedTextFieldEx();
        targetLabel10 = new javax.swing.JLabel();
        lbPW = new javax.swing.JLabel();
        rdoNotUse = new javax.swing.JRadioButton();
        lbID = new javax.swing.JLabel();
        targetLabel19 = new javax.swing.JLabel();
        targetLabel20 = new javax.swing.JLabel();
        targetLabel3 = new javax.swing.JLabel();
        cmbMedia = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lbPW1 = new javax.swing.JLabel();
        txtStoreID = new com.geobeck.swing.JFormattedTextFieldEx();
        txtPW = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        reservationPanel = new javax.swing.JPanel();
        reservationDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel12 = new javax.swing.JLabel();
        btnRegistStartOff = new javax.swing.JButton();
        statusTabReservation = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setFont(new java.awt.Font("MS UI Gothic", 0, 11)); // NOI18N

        targetLabel.setText("店舗");

        cmbShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShopActionPerformed(evt);
            }
        });

        mainTab.setPreferredSize(new java.awt.Dimension(700, 441));
        mainTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabStateChanged(evt);
            }
        });

        buttonGroupSalon.add(rdoValid);
        rdoValid.setText("有効");
        rdoValid.setNextFocusableComponent(rdoStop);
        rdoValid.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoValidStateChanged(evt);
            }
        });

        buttonGroupSalon.add(rdoStop);
        rdoStop.setText("停止");
        rdoStop.setNextFocusableComponent(btRegistSalon);
        rdoStop.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoStopStateChanged(evt);
            }
        });

        lbMedia.setText("媒体連携");

        btRegistSalon.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btRegistSalon.setBorderPainted(false);
        btRegistSalon.setContentAreaFilled(false);
        btRegistSalon.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btRegistSalon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistSalonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout salonPanelLayout = new org.jdesktop.layout.GroupLayout(salonPanel);
        salonPanel.setLayout(salonPanelLayout);
        salonPanelLayout.setHorizontalGroup(
            salonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(salonPanelLayout.createSequentialGroup()
                .add(21, 21, 21)
                .add(lbMedia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(rdoValid)
                .add(18, 18, 18)
                .add(rdoStop)
                .add(107, 107, 107)
                .add(btRegistSalon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(342, Short.MAX_VALUE))
        );
        salonPanelLayout.setVerticalGroup(
            salonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(salonPanelLayout.createSequentialGroup()
                .add(30, 30, 30)
                .add(salonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btRegistSalon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(salonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(rdoValid)
                        .add(rdoStop)
                        .add(lbMedia)))
                .addContainerGap(549, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout tabSalonLayout = new org.jdesktop.layout.GroupLayout(tabSalon);
        tabSalon.setLayout(tabSalonLayout);
        tabSalonLayout.setHorizontalGroup(
            tabSalonLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabSalonLayout.createSequentialGroup()
                .add(salonPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabSalonLayout.setVerticalGroup(
            tabSalonLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(salonPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainTab.addTab("     基本設定     ", tabSalon);

        tabStaff.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tabStaffPropertyChange(evt);
            }
        });

        tbStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "媒体名", "スタッフ名", "選択", "状態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbStaff.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbStaff.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbStaff);
        tbStaff.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbStaff, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane1.setViewportView(tbStaff);

        InfoStaffButton.setIcon(SystemInfo.getImageIcon("/button/common/bookingInfo_off.jpg"));
        InfoStaffButton.setBorderPainted(false);
        InfoStaffButton.setContentAreaFilled(false);
        InfoStaffButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/bookingInfo_on.jpg"));
        InfoStaffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InfoStaffButtonActionPerformed(evt);
            }
        });

        registStaffButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registStaffButton.setBorderPainted(false);
        registStaffButton.setContentAreaFilled(false);
        registStaffButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registStaffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registStaffButtonActionPerformed(evt);
            }
        });

        lbStaffPos.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        lbStaffPos.setText("【POS側情報】");

        targetLabel12.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel12.setText("媒体側のスタッフ情報とSOSIA POSのスタッフ情報の結び付けを行います。");

        targetLabel21.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel21.setText("結び付けを行っていない場合はスタッフ情報の連携が行えませんのでご注意ください。");

        targetLabel22.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel22.setText("媒体側でスタッフ情報を登録・更新・削除した場合は「情報取得」ボタンを押下してください。");

        tbStaffPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "スタッフ名"
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
        tbStaffPos.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbStaffPos.getTableHeader().setReorderingAllowed(false);
        tbStaffPos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbStaffPos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtil.setJTableHeaderRenderer(tbStaffPos, SystemInfo.getTableHeaderRenderer());
        tbStaffPos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStaffPosMouseClicked(evt);
            }
        });
        tbStaffPos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbStaffPosKeyReleased(evt);
            }
        });
        groupPwBusinessScrollPane6.setViewportView(tbStaffPos);

        lbStaffMedia.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        lbStaffMedia.setText("【媒体側情報】");

        org.jdesktop.layout.GroupLayout tabStaffLayout = new org.jdesktop.layout.GroupLayout(tabStaff);
        tabStaff.setLayout(tabStaffLayout);
        tabStaffLayout.setHorizontalGroup(
            tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabStaffLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabStaffLayout.createSequentialGroup()
                        .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(targetLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(targetLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(targetLabel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 124, Short.MAX_VALUE)
                        .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, registStaffButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, InfoStaffButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .add(tabStaffLayout.createSequentialGroup()
                        .add(groupPwBusinessScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(26, 26, 26)
                        .add(groupPwBusinessScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 350, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(95, 95, 95))))
            .add(tabStaffLayout.createSequentialGroup()
                .add(21, 21, 21)
                .add(lbStaffPos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(127, 127, 127)
                .add(lbStaffMedia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        tabStaffLayout.setVerticalGroup(
            tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabStaffLayout.createSequentialGroup()
                .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(InfoStaffButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tabStaffLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(targetLabel12)))
                .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabStaffLayout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(registStaffButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(tabStaffLayout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(targetLabel21)
                        .add(3, 3, 3)
                        .add(targetLabel22)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lbStaffPos)
                    .add(lbStaffMedia))
                .add(1, 1, 1)
                .add(tabStaffLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(groupPwBusinessScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .add(groupPwBusinessScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .add(6, 6, 6))
        );

        mainTab.addTab("     スタッフ     ", tabStaff);

        tabBed.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tabBedPropertyChange(evt);
            }
        });

        targetLabel13.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel13.setText("媒体側の設備情報とSOSIA POSの施術台情報の結び付けを行います。");

        registBedButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registBedButton.setBorderPainted(false);
        registBedButton.setContentAreaFilled(false);
        registBedButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registBedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registBedButtonActionPerformed(evt);
            }
        });

        InfoBedButton.setIcon(SystemInfo.getImageIcon("/button/common/bookingInfo_off.jpg"));
        InfoBedButton.setBorderPainted(false);
        InfoBedButton.setContentAreaFilled(false);
        InfoBedButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/bookingInfo_on.jpg"));
        InfoBedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InfoBedButtonActionPerformed(evt);
            }
        });

        tbBed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "媒体名", "設備名", "選択", "状態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbBed.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbBed.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbBed);
        tbBed.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbBed.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbBed, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane2.setViewportView(tbBed);

        targetLabel23.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel23.setText("結び付けを行っていない場合は設備情報の連携が行えませんのでご注意ください。");

        targetLabel24.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel24.setText("媒体側で設備情報を登録・更新・削除した場合は「情報取得」ボタンを押下してください。");

        tbBedPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "施術台名"
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
        tbBedPos.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbBedPos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbBedPos, SystemInfo.getTableHeaderRenderer());
        tbBedPos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbBedPosMouseClicked(evt);
            }
        });
        tbBedPos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbBedPosKeyReleased(evt);
            }
        });
        groupPwBusinessScrollPane7.setViewportView(tbBedPos);

        lbBedPos.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        lbBedPos.setText("【POS側情報】");

        lbBedMedia.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        lbBedMedia.setText("【媒体側情報】");

        org.jdesktop.layout.GroupLayout tabBedLayout = new org.jdesktop.layout.GroupLayout(tabBed);
        tabBed.setLayout(tabBedLayout);
        tabBedLayout.setHorizontalGroup(
            tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabBedLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, tabBedLayout.createSequentialGroup()
                        .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(targetLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(targetLabel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 124, Short.MAX_VALUE)
                        .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, InfoBedButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, registBedButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(tabBedLayout.createSequentialGroup()
                        .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(targetLabel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tabBedLayout.createSequentialGroup()
                                .add(groupPwBusinessScrollPane7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(26, 26, 26)
                                .add(groupPwBusinessScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 400, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .add(tabBedLayout.createSequentialGroup()
                .add(21, 21, 21)
                .add(lbBedPos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(128, 128, 128)
                .add(lbBedMedia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabBedLayout.setVerticalGroup(
            tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabBedLayout.createSequentialGroup()
                .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(InfoBedButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tabBedLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(targetLabel13)))
                .add(3, 3, 3)
                .add(targetLabel23)
                .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabBedLayout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(targetLabel24))
                    .add(tabBedLayout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(registBedButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lbBedPos)
                    .add(lbBedMedia))
                .add(1, 1, 1)
                .add(tabBedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(groupPwBusinessScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .add(groupPwBusinessScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .add(6, 6, 6))
        );

        mainTab.addTab("       施術台       ", tabBed);

        tabShift.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tabShiftPropertyChange(evt);
            }
        });

        registShiftButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registShiftButton.setBorderPainted(false);
        registShiftButton.setContentAreaFilled(false);
        registShiftButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registShiftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registShiftButtonActionPerformed(evt);
            }
        });

        targetLabel16.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel16.setText("媒体側のシフトパターン情報とSOSIA POSの基本シフト情報の結び付けを行います。");

        tbShift.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "媒体名","シフト名","勤務時間", "選択", "状態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbShift.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbShift.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbShift);
        tbShift.setSelectionBackground(new java.awt.Color(204, 204, 204));
        //tbShift.setDefaultRenderer(String.class, new MediaTableCellRenderer());
        tbShift.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbShift, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane3.setViewportView(tbShift);

        InfoShiftButton.setIcon(SystemInfo.getImageIcon("/button/common/bookingInfo_off.jpg"));
        InfoShiftButton.setBorderPainted(false);
        InfoShiftButton.setContentAreaFilled(false);
        InfoShiftButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/bookingInfo_on.jpg"));
        InfoShiftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InfoShiftButtonActionPerformed(evt);
            }
        });

        targetLabel25.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel25.setText("結び付けを行っていない場合はシフト情報の連携が行えませんのでご注意ください。");

        targetLabel26.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        targetLabel26.setText("媒体側でシフトパターン情報を登録・更新・削除した場合は「情報取得」ボタンを押下してください。");

        tbShiftPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "基本シフト"
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
        tbShiftPos.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbShiftPos.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbShiftPos);
        tbShiftPos.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbShiftPos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbShiftPos, SystemInfo.getTableHeaderRenderer());
        tbShiftPos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbShiftPosMouseClicked(evt);
            }
        });
        tbShiftPos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbShiftPosKeyReleased(evt);
            }
        });
        groupPwBusinessScrollPane8.setViewportView(tbShiftPos);

        lbBedPos1.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        lbBedPos1.setText("【POS側情報】");

        lbBedMedia1.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
        lbBedMedia1.setText("【媒体側情報】");

        org.jdesktop.layout.GroupLayout tabShiftLayout = new org.jdesktop.layout.GroupLayout(tabShift);
        tabShift.setLayout(tabShiftLayout);
        tabShiftLayout.setHorizontalGroup(
            tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabShiftLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabShiftLayout.createSequentialGroup()
                        .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(targetLabel26, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                            .add(targetLabel25, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(targetLabel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 91, Short.MAX_VALUE)
                        .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, InfoShiftButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, registShiftButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(tabShiftLayout.createSequentialGroup()
                        .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tabShiftLayout.createSequentialGroup()
                                .add(11, 11, 11)
                                .add(lbBedPos1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(groupPwBusinessScrollPane8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tabShiftLayout.createSequentialGroup()
                                .add(39, 39, 39)
                                .add(lbBedMedia1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(tabShiftLayout.createSequentialGroup()
                                .add(26, 26, 26)
                                .add(groupPwBusinessScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 410, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tabShiftLayout.setVerticalGroup(
            tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabShiftLayout.createSequentialGroup()
                .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(InfoShiftButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tabShiftLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(targetLabel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabShiftLayout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(registShiftButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(tabShiftLayout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(targetLabel25)
                        .add(3, 3, 3)
                        .add(targetLabel26)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabShiftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabShiftLayout.createSequentialGroup()
                        .add(lbBedMedia1)
                        .add(1, 1, 1)
                        .add(groupPwBusinessScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, tabShiftLayout.createSequentialGroup()
                        .add(lbBedPos1)
                        .add(1, 1, 1)
                        .add(groupPwBusinessScrollPane8)))
                .add(6, 6, 6))
        );

        mainTab.addTab("シフトパターン", tabShift);

        buttonGroup1.add(rdoUse);
        rdoUse.setText("利用する");
        rdoUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoUse.setOpaque(false);

        targetLabel9.setText("<html>※媒体のログインID/PWをご入力ください。");

        registMediaButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registMediaButton.setBorderPainted(false);
        registMediaButton.setContentAreaFilled(false);
        registMediaButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registMediaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registMediaButtonActionPerformed(evt);
            }
        });

        txtID.setEditable(false);
        txtID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtID.setColumns(15);

        targetLabel10.setText("連携機能");

        lbPW.setText("パスワード");

        buttonGroup1.add(rdoNotUse);
        rdoNotUse.setText("利用しない");
        rdoNotUse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNotUse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoNotUse.setOpaque(false);
        rdoNotUse.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                rdoNotUsePropertyChange(evt);
            }
        });

        lbID.setText("ログインID");

        targetLabel19.setText(" パスワードは媒体側で変更した場合は必ずご変更をお願いします。");

        targetLabel20.setText(" 変更されていない場合は予約連携が行えませんのでご注意ください。\t\t\t ");

        targetLabel3.setText("媒体");

        cmbMedia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMediaActionPerformed(evt);
            }
        });

        lbPW1.setText("お店ID");

        txtStoreID.setEditable(false);
        txtStoreID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtStoreID.setColumns(15);

        org.jdesktop.layout.GroupLayout tabMediaSettingLayout = new org.jdesktop.layout.GroupLayout(tabMediaSetting);
        tabMediaSetting.setLayout(tabMediaSettingLayout);
        tabMediaSettingLayout.setHorizontalGroup(
            tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabMediaSettingLayout.createSequentialGroup()
                .add(31, 31, 31)
                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabMediaSettingLayout.createSequentialGroup()
                        .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(tabMediaSettingLayout.createSequentialGroup()
                                .add(lbPW, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtPW, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(tabMediaSettingLayout.createSequentialGroup()
                                .add(lbID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(tabMediaSettingLayout.createSequentialGroup()
                        .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(targetLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 654, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tabMediaSettingLayout.createSequentialGroup()
                                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(targetLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                                    .add(targetLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(tabMediaSettingLayout.createSequentialGroup()
                                        .add(cmbMedia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(registMediaButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(tabMediaSettingLayout.createSequentialGroup()
                                        .add(rdoUse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(rdoNotUse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
                    .add(tabMediaSettingLayout.createSequentialGroup()
                        .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tabMediaSettingLayout.createSequentialGroup()
                                .add(lbPW1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtStoreID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(tabMediaSettingLayout.createSequentialGroup()
                                .add(10, 10, 10)
                                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(targetLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 654, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(targetLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 644, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .add(0, 0, Short.MAX_VALUE))))
        );
        tabMediaSettingLayout.setVerticalGroup(
            tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabMediaSettingLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(registMediaButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(targetLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(cmbMedia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(18, 18, 18)
                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(rdoUse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(rdoNotUse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(targetLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lbID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lbPW, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPW, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabMediaSettingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lbPW1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtStoreID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21)
                .add(targetLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(3, 3, 3)
                .add(targetLabel19)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(targetLabel20)
                .addContainerGap(355, Short.MAX_VALUE))
        );

        mainTab.addTab("  媒体連携設定  ", tabMediaSetting);

        reservationDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        reservationDate.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        reservationDate.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());

        jLabel12.setText("以降の予約情報を連携します。");

        btnRegistStartOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistStartOffActionPerformed(evt);
            }
        });

        statusTabReservation.setFont(new java.awt.Font("MS UI Gothic", 0, 11)); // NOI18N
        statusTabReservation.setText("処理状況表示エリア");

        jLabel1.setText("※予約データを連携する前に、必ずスタッフ、施術台、シフトパターンの連携設定を行ってください。");

        jLabel2.setText("（SPOS→媒体）");

        org.jdesktop.layout.GroupLayout reservationPanelLayout = new org.jdesktop.layout.GroupLayout(reservationPanel);
        reservationPanel.setLayout(reservationPanelLayout);
        reservationPanelLayout.setHorizontalGroup(
            reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(reservationPanelLayout.createSequentialGroup()
                .add(18, 18, 18)
                .add(reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(reservationPanelLayout.createSequentialGroup()
                        .add(reservationDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel12)
                        .add(46, 46, 46)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnRegistStartOff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel1)
                    .add(statusTabReservation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 490, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(238, Short.MAX_VALUE))
        );
        reservationPanelLayout.setVerticalGroup(
            reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(reservationPanelLayout.createSequentialGroup()
                .add(12, 12, 12)
                .add(reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel12)
                        .add(jLabel2))
                    .add(reservationDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnRegistStartOff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(43, 43, 43)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(statusTabReservation)
                .addContainerGap(274, Short.MAX_VALUE))
        );

        btnRegistStartOff.setIcon(SystemInfo.getImageIcon("/button/reservation/regist_start_off.jpg"));
        btnRegistStartOff.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/regist_start_off.jpg"));
        SystemInfo.addMouseCursorChange(btnRegistStartOff);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(reservationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(reservationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 213, Short.MAX_VALUE))
        );

        mainTab.addTab("      予約連携      ", jPanel2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(targetLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(targetLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTab.getAccessibleContext().setAccessibleName("       基本設定       ");
    }// </editor-fold>//GEN-END:initComponents

    private void cmbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopActionPerformed
        this.shop = (MstShop) cmbShop.getSelectedItem();
        initData();
        this.loadData();
        this.showData();
        //selectedRowTable(tbStaffPos, tbStaffPos, tbStaff, staffPosMap, 1, 2);
        selectedRow(STAFF_TABLE);
        selectedRow(BED_TABLE);
        selectedRow(SHIFT_TABLE);
        //selectedRowTable(tbBedPos, tbBedPos, tbBed, bedPosMap, 1, 2);
        //selectedRowTable(tbShiftPos, tbShiftPos, tbShift, shiftPosMap, 1, 3);
//        if(media.isUseFlg()== false) {
//            showChangeUseFlg(media.isUseFlg());
//        }
    }//GEN-LAST:event_cmbShopActionPerformed

    private void tabBedPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tabBedPropertyChange
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        selectedRow(BED_TABLE);
        //selectedRowTable(tbBedPos, tbBedPos, tbBed, bedPosMap, 1, 2);
    }//GEN-LAST:event_tabBedPropertyChange

    private void tbBedPosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBedPosKeyReleased
        //clearChecked(tbBedPos, 1);
        selectedRow(BED_TABLE);
        //selectedRowTable(tbBedPos, tbBedPos, tbBed, bedPosMap, 1, 2);
    }//GEN-LAST:event_tbBedPosKeyReleased

    private void tbBedPosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBedPosMouseClicked
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        //clearChecked(tbBedPos, 1);
        selectedRow(BED_TABLE);
        //selectedRowTable(tbBedPos, tbBedPos, tbBed, bedPosMap, 1, 2);
    }//GEN-LAST:event_tbBedPosMouseClicked

    private void InfoBedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InfoBedButtonActionPerformed
        InfoBedButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            this.mstApi.getBedListAPI();
            loadDataBed();
            showDataBed();
            //selectedRowTable(tbBedPos, tbBedPos, tbBed, bedPosMap, 1, 2);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_InfoBedButtonActionPerformed

    private void registBedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registBedButtonActionPerformed
        registBedButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            regist(registBedButton);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_registBedButtonActionPerformed

    private void tabStaffPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tabStaffPropertyChange
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        selectedRow(STAFF_TABLE);
        //selectedRowTable(tbStaffPos, tbStaffPos, tbStaff, this.staffPosMap, 1, 2);
    }//GEN-LAST:event_tabStaffPropertyChange

    private void tbStaffPosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbStaffPosKeyReleased
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        //clearChecked(tbStaffPos, 1);
        selectedRow(STAFF_TABLE);
    }//GEN-LAST:event_tbStaffPosKeyReleased

    private void tbStaffPosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStaffPosMouseClicked
        //check use flag
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        //clearChecked(tbStaffPos, 1);
        selectedRow(STAFF_TABLE);
    }//GEN-LAST:event_tbStaffPosMouseClicked

    private void registStaffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registStaffButtonActionPerformed
        registStaffButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            regist(registStaffButton);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_registStaffButtonActionPerformed

    private void InfoStaffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InfoStaffButtonActionPerformed
        InfoStaffButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            this.mstApi.getStaffListAPI();
            loadDataStaff();
            this.showDataStaff();
            //selectedRowTable(tbStaffPos, tbStaffPos, tbStaff, staffPosMap, 1, 2);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_InfoStaffButtonActionPerformed

    private void cmbMediaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMediaActionPerformed
        this.selectedMedia = ((MstAPIDetail) this.cmbMedia.getSelectedItem()).getMediaID();
        loadDataMedia();
        clear();
        initTableWidth();
        showDataMedia();
    }//GEN-LAST:event_cmbMediaActionPerformed

    private void rdoNotUsePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_rdoNotUsePropertyChange
        //showChangeUseFlg(!rdoNotUse.isSelected());
    }//GEN-LAST:event_rdoNotUsePropertyChange

    private void registMediaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registMediaButtonActionPerformed
        if(txtPW.getText().trim().length() < 1) {
            MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "パスワード"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
                txtPW.requestFocusInWindow();
                return;
        }
        registMediaButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int result = -1;
            //LVTu start edit 2016/11/21 New request #54178
            result = sendUserMediaAPI(mstApi.getSalonID(), ((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID() , !(rdoUse.isSelected()), txtPW.getText().trim());
            if(result >= HTTP_RESULT_500 && result <= HTTP_RESULT_599) {
                for ( int i = 0;i < COUNT_RETRY;i ++) {
                    Thread.sleep(FIVE_SECONDS);
                    result = sendUserMediaAPI(mstApi.getSalonID(), ((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID() , !(rdoUse.isSelected()), txtPW.getText().trim());
                    if(!(result >= HTTP_RESULT_500 && result <= HTTP_RESULT_599)) {
                        break;
                    }
                }
            }
            //LVTu end edit 2016/11/21 New request #54178
            //IVS_LVTu start edit 2016/12/05 New request #58830
            if(result == HTTP_RESULT_200 || result == HTTP_RESULT_202) {
            //IVS_LVTu end edit 2016/12/05 New request #58830
                regist(registMediaButton);
                //showChangeUseFlg(media.isUseFlg());
            }else if((result >= HTTP_RESULT_500 && result <= HTTP_RESULT_599)) {
                MessageDialog.showMessageDialog(this,
                        "更新できませんでした。しばらく経ってから、\nもう一度処理してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }else if(result == HTTP_RESULT_403) {
                MessageDialog.showMessageDialog(this,
                        "パスワードが不正です。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }else {
                MessageDialog.showMessageDialog(this,
                        "エラーが発生しました。管理者に連絡してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_registMediaButtonActionPerformed

    private void btRegistSalonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistSalonActionPerformed
        Integer status = null;
        if(this.rdoValid.isSelected()) {
            status = STATUS_EIGHT;
        }else if(this.rdoStop.isSelected()) {
            status = STATUS_NINE;
        }
        btRegistSalon.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.mediaSalon.pushSalonAPI(status);
            //refresh data
            this.settingSalon();
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btRegistSalonActionPerformed

    private void mainTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabStateChanged
        if(mainTab.getSelectedIndex() != -1){
            int selectedTab = mainTab.getSelectedIndex();
            loadDataAPI(selectedTab);
            selectedRow(selectedTab);
        }
    }//GEN-LAST:event_mainTabStateChanged

    private void rdoValidStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoValidStateChanged
        changedRadio();
    }//GEN-LAST:event_rdoValidStateChanged

    private void rdoStopStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoStopStateChanged
        changedRadio();
    }//GEN-LAST:event_rdoStopStateChanged

    private void tabShiftPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tabShiftPropertyChange
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        selectedRow(SHIFT_TABLE);
        //selectedRowTable(tbShiftPos, tbShiftPos, tbShift, shiftPosMap, 1, 3);
    }//GEN-LAST:event_tabShiftPropertyChange

    private void tbShiftPosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbShiftPosKeyReleased
        //clearChecked(tbShiftPos, 1);
        selectedRow(SHIFT_TABLE);
        //selectedRowTable(tbShiftPos, tbShiftPos, tbShift, shiftPosMap, 1, 3);
    }//GEN-LAST:event_tbShiftPosKeyReleased

    private void tbShiftPosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbShiftPosMouseClicked
//        if(!this.media.isUseFlg()) {
//            return;
//        }
        selectedRow(SHIFT_TABLE);
        //clearChecked(tbShiftPos, 1);
        //selectedRowTable(tbShiftPos, tbShiftPos, tbShift, shiftPosMap, 1, 3);
    }//GEN-LAST:event_tbShiftPosMouseClicked

    private void InfoShiftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InfoShiftButtonActionPerformed
        InfoShiftButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            this.mstApi.getShiftListAPI();
            loadDataShift();
            this.showDataShift();
            //selectedRowTable(tbShiftPos, tbShiftPos, tbShift, shiftPosMap, 1, 3);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_InfoShiftButtonActionPerformed

    private void registShiftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registShiftButtonActionPerformed
        registShiftButton.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            regist(registShiftButton);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_registShiftButtonActionPerformed

    private void btnRegistStartOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistStartOffActionPerformed
        //IVS_LVTu start add 2016/11/07 New request #58479
        if(!checkInputReservation()) {
            return;
        }
        //IVS_LVTu end add 2016/11/07 New request #58479
        btnRegistStartOff.setCursor(null);
        try{
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            reservationDate.setEnabled(false);
            btnRegistStartOff.setEnabled(false);
            statusTabReservation.setText("処理中です。しばらくお待ちください。");

            Boolean apiResponse = this.mstApi.sendRegistReservationAllApi(SystemInfo.getLoginID(),
                                                                            reservationDate.getDate());
            if(!apiResponse) {
                if(!scheduler.isShutdown())
                    scheduler.shutdown();
                MessageDialog.showMessageDialog(this,
                            "媒体との連動ができませんでした。",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(scheduler.isShutdown()) {
                scheduler = Executors.newSingleThreadScheduledExecutor();
                handleStatusTimer();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnRegistStartOffActionPerformed

    private void changedRadio() {
        //IVS_LVTu start edit 2016/11/14 New request #58581
        if(null != this.mediaSalon.getStatus()) {
            switch (this.mediaSalon.getStatus()) {
                case STATUS_EIGHT:
                    if(rdoValid.isSelected()) {
                        this.btRegistSalon.setEnabled(false);
                    }else if(rdoStop.isSelected()) {
                        this.btRegistSalon.setEnabled(true);
                    }   
                    break;
                case STATUS_NINE:
                    if(rdoValid.isSelected()) {
                        this.btRegistSalon.setEnabled(true);
                    }else if(rdoStop.isSelected()) {
                        this.btRegistSalon.setEnabled(false);
                    }   
                    break;
                case STATUS_SEVEN:
                    if(rdoValid.isSelected()) {
                        this.btRegistSalon.setEnabled(true);
                    }else {
                        this.btRegistSalon.setEnabled(false);
                    }   
                    break;
                default:
                    break;
            }
        }
        //IVS_LVTu end edit 2016/11/14 New request #58581
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton InfoBedButton;
    private javax.swing.JButton InfoShiftButton;
    private javax.swing.JButton InfoStaffButton;
    private javax.swing.JButton btRegistSalon;
    private javax.swing.JButton btnRegistStartOff;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroupSalon;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbMedia;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbShop;
    private javax.swing.JScrollPane groupPwBusinessScrollPane1;
    private javax.swing.JScrollPane groupPwBusinessScrollPane2;
    private javax.swing.JScrollPane groupPwBusinessScrollPane3;
    private javax.swing.JScrollPane groupPwBusinessScrollPane6;
    private javax.swing.JScrollPane groupPwBusinessScrollPane7;
    private javax.swing.JScrollPane groupPwBusinessScrollPane8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbBedMedia;
    private javax.swing.JLabel lbBedMedia1;
    private javax.swing.JLabel lbBedPos;
    private javax.swing.JLabel lbBedPos1;
    private javax.swing.JLabel lbID;
    private javax.swing.JLabel lbMedia;
    private javax.swing.JLabel lbPW;
    private javax.swing.JLabel lbPW1;
    private javax.swing.JLabel lbStaffMedia;
    private javax.swing.JLabel lbStaffPos;
    private javax.swing.JTabbedPane mainTab;
    private javax.swing.JRadioButton rdoNotUse;
    private javax.swing.JRadioButton rdoStop;
    private javax.swing.JRadioButton rdoUse;
    private javax.swing.JRadioButton rdoValid;
    private javax.swing.JButton registBedButton;
    private javax.swing.JButton registMediaButton;
    private javax.swing.JButton registShiftButton;
    private javax.swing.JButton registStaffButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo reservationDate;
    private javax.swing.JPanel reservationPanel;
    private javax.swing.JPanel salonPanel;
    private javax.swing.JLabel statusTabReservation;
    private javax.swing.JPanel tabBed;
    private javax.swing.JPanel tabMediaSetting;
    private javax.swing.JPanel tabSalon;
    private javax.swing.JPanel tabShift;
    private javax.swing.JPanel tabStaff;
    private javax.swing.JLabel targetLabel;
    private javax.swing.JLabel targetLabel10;
    private javax.swing.JLabel targetLabel12;
    private javax.swing.JLabel targetLabel13;
    private javax.swing.JLabel targetLabel16;
    private javax.swing.JLabel targetLabel19;
    private javax.swing.JLabel targetLabel20;
    private javax.swing.JLabel targetLabel21;
    private javax.swing.JLabel targetLabel22;
    private javax.swing.JLabel targetLabel23;
    private javax.swing.JLabel targetLabel24;
    private javax.swing.JLabel targetLabel25;
    private javax.swing.JLabel targetLabel26;
    private javax.swing.JLabel targetLabel3;
    private javax.swing.JLabel targetLabel9;
    private javax.swing.JTable tbBed;
    private javax.swing.JTable tbBedPos;
    private javax.swing.JTable tbShift;
    private javax.swing.JTable tbShiftPos;
    private javax.swing.JTable tbStaff;
    private javax.swing.JTable tbStaffPos;
    private com.geobeck.swing.JFormattedTextFieldEx txtID;
    private javax.swing.JPasswordField txtPW;
    private com.geobeck.swing.JFormattedTextFieldEx txtStoreID;
    // End of variables declaration//GEN-END:variables

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(registMediaButton);
        SystemInfo.addMouseCursorChange(registStaffButton);
        SystemInfo.addMouseCursorChange(registBedButton);
        SystemInfo.addMouseCursorChange(registShiftButton);
        SystemInfo.addMouseCursorChange(InfoStaffButton);
        SystemInfo.addMouseCursorChange(InfoBedButton);
        SystemInfo.addMouseCursorChange(InfoShiftButton);
        SystemInfo.addMouseCursorChange(btRegistSalon);
    }

    void initStaffTable(int startRowDel) {
        //staff pos
        tbStaffPos = new com.geobeck.swing.JTableEx();

        tbStaffPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "スタッフ名"
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

        tbStaffPos.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbStaffPos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbStaffPos.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbStaffPos);
        tbStaffPos.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbStaffPos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbStaffPos, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane6.setViewportView(tbStaffPos);

        //staff media
        tbStaff = new com.geobeck.swing.JTableEx();

        tbStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "媒体名", "スタッフ名", "選択", "状態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbStaff.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbStaff.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbStaff);
        tbStaff.setSelectionBackground(new java.awt.Color(204, 204, 204));
        new MediaTableCellRenderer(STAFF_TABLE, startRowDel).setSelectTableCellRenderer(tbStaff);
        tbStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbStaff, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane1.setViewportView(tbStaff);


        tbStaffPos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStaffPosMouseClicked(evt);
            }
        });
        tbStaffPos.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbStaffPosKeyReleased(evt);
            }
        });

        initTableWidth();
    }

    void initBedTable(int indexStartDel) {

        tbBedPos = new com.geobeck.swing.JTableEx();

        tbBedPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "施術台名"
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

        tbBedPos.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbBedPos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbBedPos.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbBedPos);
        tbBedPos.setSelectionBackground(new java.awt.Color(204, 204, 204));

        tbBedPos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbBedPos, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane7.setViewportView(tbBedPos);


        tbBed = new com.geobeck.swing.JTableEx();

        tbBed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                 "媒体名", "設備名", "選択", "状態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbBed.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbBed.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbBed.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbBed);
        tbBed.setSelectionBackground(new java.awt.Color(204, 204, 204));
        new MediaTableCellRenderer(BED_TABLE, indexStartDel).setSelectTableCellRenderer(tbBed);
        tbBed.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbBed, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane2.setViewportView(tbBed);


        tbBedPos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbBedPosMouseClicked(evt);
            }
        });
        tbBedPos.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbBedPosKeyReleased(evt);
            }
        });

        initTableWidth();
    }

    void initShiftTable(int indexStartDel) {

        tbShiftPos = new com.geobeck.swing.JTableEx();

        tbShiftPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "基本シフト"
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

        tbShiftPos.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbShiftPos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbShiftPos.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbShiftPos);
        tbShiftPos.setSelectionBackground(new java.awt.Color(204, 204, 204));

        tbShiftPos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbShiftPos, SystemInfo.getTableHeaderRenderer());
        tbShiftPos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbShiftPosMouseClicked(evt);
            }
        });
        tbShiftPos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbShiftPosKeyReleased(evt);
            }
        });

        groupPwBusinessScrollPane8.setViewportView(tbShiftPos);



        tbShift = new com.geobeck.swing.JTableEx();

        tbShift.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "媒体名","シフト名","勤務時間", "選択", "状態"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbShift.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        tbShift.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbShift.getTableHeader().setReorderingAllowed(false);
        SelectTableCellRenderer.setSelectTableCellRenderer(tbShift);
        tbShift.setSelectionBackground(new java.awt.Color(204, 204, 204));
        //tbShift.setDefaultRenderer(String.class, new MediaTableCellRenderer());
        new MediaTableCellRenderer(SHIFT_TABLE, indexStartDel).setSelectTableCellRenderer(tbShift);
        tbShift.setSelectionForeground(new java.awt.Color(0, 0, 0));
        SwingUtil.setJTableHeaderRenderer(tbShift, SystemInfo.getTableHeaderRenderer());
        groupPwBusinessScrollPane3.setViewportView(tbShift);

        initTableWidth();
    }

    private void initTableWidth() {
        //
        tbStaffPos.getColumnModel().getColumn(0).setPreferredWidth(150);
        //tbStaffPos.getColumnModel().getColumn(1).setPreferredWidth(35);

        tbBedPos.getColumnModel().getColumn(0).setPreferredWidth(150);
        //tbBedPos.getColumnModel().getColumn(1).setPreferredWidth(35);

        tbStaff.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbStaff.getColumnModel().getColumn(1).setPreferredWidth(120);
        tbStaff.getColumnModel().getColumn(2).setPreferredWidth(35);
        tbStaff.getColumnModel().getColumn(3).setPreferredWidth(35);

        tbBed.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbBed.getColumnModel().getColumn(1).setPreferredWidth(120);
        tbBed.getColumnModel().getColumn(2).setPreferredWidth(35);
        tbBed.getColumnModel().getColumn(3).setPreferredWidth(35);

        tbShiftPos.getColumnModel().getColumn(0).setPreferredWidth(150);
        //tbShiftPos.getColumnModel().getColumn(1).setPreferredWidth(35);

        tbShift.getColumnModel().getColumn(0).setPreferredWidth(110);
        tbShift.getColumnModel().getColumn(1).setPreferredWidth(110);
        tbShift.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbShift.getColumnModel().getColumn(3).setPreferredWidth(40);
        tbShift.getColumnModel().getColumn(4).setPreferredWidth(40);

    }

    private boolean regist(JButton button) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();
        if (button.equals(registMediaButton)) {
            result = registMedia(con);
            System.out.println("media");
        } else if (button.equals(registStaffButton)) {
            result = registStaff(con);
            loadDataStaff();
            showDataStaff();
            System.out.println("staff");
        } else if (button.equals(registBedButton)) {
            result = registBed(con);
            loadDataBed();
            showDataBed();
            System.out.println("bed");
        } else if (button.equals(registShiftButton)) {
            result = registShift(con);
            loadDataShift();
            showDataShift();
            System.out.println("shift");
        }

        if(!result) {
            MessageDialog.showMessageDialog(
                        this,
                        "連携が設定されていません",
                        this.getTitle(),
                        JOptionPane.WARNING_MESSAGE);
        }

        if(result) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            }

        return result;
    }

    private void initData() {
        loadStaffPos();
        loadBedPos();
        loadShiftPos();
    }


    private void loadData() {
        loadDataMedia();
        loadDataStaff();
        loadDataBed();
        loadDataShift();
    }

    /**
     * load data when select tab
     * @param indexTab
     */
    private void loadDataAPI(int indexTab) {
        //nhtvinh start add 20161021 New request #54239
        if(null != scheduler)
            scheduler.shutdown();
        //nhtvinh end add 20161021 New request #54239

        //IVS_LVTU start edit 2017/01/16 #59397 [gb]かんざしAPI用管理画面構築（サイトコントロール設定）_タブ（スタッフ・施術台・シフトパターン）選択時の処理不備
        try {
            if (indexTab == TAB_STAFF && this.mstApi.getListStaff().isEmpty()) {
                this.mstApi.getStaffListAPI();
                //SPOSに登録されているデータに紐付くデータのみ。
                APIStaffs listStaffTemp = new APIStaffs();
                for (APIStaff staffAPI : this.mstApi.getListStaff()) {
                    for (MstKANZASHIStaff staffKanzashi : this.staffMedia) {
                        if(staffAPI.getId().equals(staffKanzashi.getStaffID())) {
                            listStaffTemp.add(staffAPI);
                        }
                    }
                }
                this.mstApi.getListStaff().clear();
                this.mstApi.setListStaff(listStaffTemp);

                showDataStaff();
            } else if (indexTab == TAB_BED && this.mstApi.getListBed().isEmpty()) {
                this.mstApi.getBedListAPI();
                //SPOSに登録されているデータに紐付くデータのみ。
                APIBeds listBedTemp = new APIBeds();
                for (APIBed bedAPI : this.mstApi.getListBed()) {
                    for (MstKANZASHIBed bedKanzashi : this.bedMedia) {
                        if (bedAPI.getId().equals(bedKanzashi.getBedID())) {
                            listBedTemp.add(bedAPI);
                        }
                    }
                }
                this.mstApi.getListBed().clear();
                this.mstApi.setListBed(listBedTemp);

                showDataBed();
            } else if (indexTab == TAB_SHIFT && this.mstApi.getListShift().isEmpty()) {
                this.mstApi.getShiftListAPI();
                //SPOSに登録されているデータに紐付くデータのみ。
                APIShifts listShiftTemp = new APIShifts();
                for (APIShift shiftAPI : this.mstApi.getListShift()) {
                    for (MstKANZASHIShift shiftKanzashi : this.shiftMedia) {
                        if (shiftAPI.getId().equals(shiftKanzashi.getShiftID())) {
                            listShiftTemp.add(shiftAPI);
                        }
                    }
                }
                this.mstApi.getListShift().clear();
                this.mstApi.setListShift(listShiftTemp);

                showDataShift();
            //nhtvinh start add 20161021 New request #54239
            } else if (indexTab == TAB_RESERVATION) {
                scheduler = Executors.newSingleThreadScheduledExecutor();
                this.mstApi.loadSalonStatus();
                showDataReservation();
            }
        //nhtvinh end add 20161021 New request #54239
        //showChangeUseFlg(this.media.isUseFlg());
        } catch (NullPointerException ex) {
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //IVS_LVTU end edit 2017/01/16 #59397 [gb]かんざしAPI用管理画面構築（サイトコントロール設定）_タブ（スタッフ・施術台・シフトパターン）選択時の処理不備
    }

    private void showData() {
        clear();
        initTableWidth();

        showDataMedia();
        showDataStaff();
        showDataBed();
        showDataShift();
        //showChangeUseFlg(this.media.isUseFlg());
    }

    private void initMedia() {
        try {
            mstApi.setApiID(SystemInfo.getUserAPI());
            ConnectionWrapper con = SystemInfo.getBaseConnection();
            mstApi.loadDetail(con);
            for (MstAPIDetail detail : mstApi) {
                cmbMedia.addItem(detail);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="load controls">
    private void loadStaffPos() {
        /**
         * * MstShift の値をテーブルに反映 **
         */
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            this.staffsPos.setShopIDList(shop.getShopID().toString());
            this.staffsPos.loadOnlyShop(con, true);

            con.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    private void loadBedPos() {
        /**
         * * MstShift の値をテーブルに反映 **
         */
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            this.bedsPos.setShop(shop);
            this.bedsPos.load(con);
            this.bedsPos.add(0, new MstBed());

            con.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    private void loadShiftPos() {
        /**
         * * MstShift の値をテーブルに反映 **
         */
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            this.shiftsPos.setShopId(this.shop.getShopID());
            this.shiftsPos.load(con, true);

            for ( int i = 0;i < this.shiftsPos.size();i ++) {
                if(this.shiftsPos.get(i).getShiftName() == null || "".equals(this.shiftsPos.get(i).getShiftName())
                    || this.shiftsPos.get(i).getStartTime() == null || "".equals(this.shiftsPos.get(i).getStartTime().trim())
                        || this.shiftsPos.get(i).getEndTime() == null || "".equals(this.shiftsPos.get(i).getEndTime().trim())
                        ||this.shiftsPos.get(i).getStartTime().trim().length() != 4 || this.shiftsPos.get(i).getEndTime().trim().length() != 4
                        ) {
                    this.shiftsPos.remove(i);
                    i --;
                }
            }
            this.shiftsPos.add(0, new MstShift());

            con.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    //</editor-fold>

    private void clear() {
        this.rdoUse.setSelected(true);
        this.txtID.setText("");
        this.txtPW.setText("");
        this.txtStoreID.setText("");
    }

    // <editor-fold defaultstate="collapsed" desc="基本設定">
    private boolean registMedia(ConnectionWrapper con) {
        boolean result = false;

        this.media.setShopID(this.shop.getShopID());
        this.media.setMediaID(this.selectedMedia);
        this.media.setUseFlg(this.rdoUse.isSelected());
        this.media.setLoginID(txtID.getText().trim());
        this.media.setPassword(this.txtPW.getText().trim());
        this.media.setStoreID(this.txtStoreID.getText().trim());
        try {
            con.begin();

            if (this.media.regist(con)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
                result = false;
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return result;
    }

        private void loadDataMedia() {
        this.media = new MstKANZASHIMedia();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            this.media.setShopID(this.shop.getShopID());
            this.media.setMediaID(this.selectedMedia);
            this.media.load(con);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    private void showDataMedia() {
        if(this.media.isUseFlg()) {
            this.rdoUse.setSelected(true);
        }else if(this.media.isUseFlg() == false){
            this.rdoNotUse.setSelected(true);
        }
        this.txtID.setText(this.media.getLoginID());
        this.txtPW.setText(this.media.getPassword());
        this.txtStoreID.setText(this.media.getStoreID());

        //check enable registMediaButton
        registMediaButton.setEnabled( this.media != null && this.media.getLoginID() != null && (this.media.getLoginID().length() > 0));
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="スタッフ">
    private boolean registStaff(ConnectionWrapper con) {
        boolean result = false;
        int colInvalid = 3;
        //check case when non data
        if(this.tbStaffPos.getRowCount() == 0 || this.tbStaff.getRowCount() == 0) {
            return result;
        }
        MstKANZASHIStaff staff;
        ArrayList<Integer> arrStaffID = new ArrayList<Integer>();
        for ( int i = 0;i < this.staffMedia.size();i ++) {
            arrStaffID.add(this.staffMedia.get(i).getStaffID());
        }
        try {
            con.begin();
            //insert , update
            for (Integer key : staffPosMap.keySet()) {
                Integer val = staffPosMap.get(key);
                if(val == null) {
                    continue;
                }
                for (int i = 0; i < tbStaff.getRowCount(); i++) {
                    if(((APIStaff)tbStaff.getValueAt(i, 1)).getMedia_ids().isEmpty()
                            || ((APIStaff)tbStaff.getValueAt(i, 1)).getMedia_ids().get(0).getMedia() == null
                            || tbStaff.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                        continue;
                    }
                    Integer staffMediaID = ((APIStaff)tbStaff.getValueAt(i, 1)).getId();
                    if(val.equals(staffMediaID)) {
                        staff = new MstKANZASHIStaff();
                        staff.setShopID(this.shop.getShopID());
                        if(arrStaffID.contains(staffMediaID)) {
                            arrStaffID.remove(staffMediaID);
                        }
                        staff.setStaffID(staffMediaID);
                        staff.setMediaID(((APIStaff)tbStaff.getValueAt(i, 1)).getMedia_ids().get(0).getMedia());
                        staff.setName(((APIStaff)tbStaff.getValueAt(i, 1)).getName());

                        staff.setSposStaffID(key);
                        if (staff.regist(con)) {
                            result = true;
                        } else {
                            con.rollback();
                            result = false;
                        }
                        break;
                    }
                }

            }
            //delete when 「mst_kanzashi_staff」に存在する　＆　「一覧に存在しない」
            for (int s = 0;s < this.staffMedia.size(); s ++) {
                for (int i = 0;i < arrStaffID.size();i ++) {
                    if(this.staffMedia.get(s).getStaffID().equals(arrStaffID.get(i))) {
                        if (this.staffMedia.get(s).delete(con)) {
                            result = true;
                        } else {
                            con.rollback();
                            result = false;
                        }
                        break;
                    }
                }
            }
            con.commit();

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return result;
    }

    private void loadDataStaff() {
        MstKANZASHIStaff staff = new MstKANZASHIStaff();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            staff.setShopID(this.shop.getShopID());
            //staff.setMediaID(this.selectedMedia);
            this.staffMedia = staff.loadAll(con);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    //nhtvinh start add 20161021 New request #54239
    private void showDataReservation() {
        try{

            Integer salonStatus = this.mstApi.getSalonStatus();
            if(null != salonStatus && salonStatus.equals(STATUS_EIGHT)){
                handleSalonSatusEight();
            } else{
                handleSalonSatusOtherEight();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleSalonSatusEight() {
        Integer syncStatus = null;
        try{
            ConnectionWrapper con = SystemInfo.getConnection();
            this.media.setShopID(this.shop.getShopID());
            this.media.setMediaID(this.selectedMedia);
            syncStatus = this.media.getSyncStatus(con);
        }catch (Exception e) {
            e.printStackTrace();
        }
        handleSyncStatus(syncStatus);
    }

    private void handleSalonSatusOtherEight() {
        reservationDate.setEnabled(false);
        btnRegistStartOff.setEnabled(false);
        statusTabReservation.setText("媒体連携が停止中です。");
    }

    private void handleSyncStatus(Integer syncStatus) {
        if(null == syncStatus){
            reservationDate.setEnabled(true);
            btnRegistStartOff.setEnabled(true);
            statusTabReservation.setText("");
            scheduler.shutdown();
            return;
        }
        System.out.println(dateFormat.format(new Date()) + " sync status is: "
                                + syncStatus);
        switch (syncStatus){
            case STATUS_ONE :
                reservationDate.setEnabled(false);
                btnRegistStartOff.setEnabled(false);
                statusTabReservation.setText("処理中です。しばらくお待ちください。");
                if(reservationPanel.isDisplayable()){
                    timerReservationStatus();
                }else{
                    scheduler.shutdown();
                }
                break;
            case STATUS_TWO:
                reservationDate.setEnabled(true);
                btnRegistStartOff.setEnabled(true);
                statusTabReservation.setText("処理が完了しました。");
                scheduler.shutdown();
                break;
            case STATUS_NINE:
                reservationDate.setEnabled(false);
                btnRegistStartOff.setEnabled(false);
                statusTabReservation.setText("エラーが発生しました。管理者に連絡してください。");
                scheduler.shutdown();
                break;
            default:
                reservationDate.setEnabled(true);
                btnRegistStartOff.setEnabled(true);
                statusTabReservation.setText("");
                scheduler.shutdown();
                break;
        }
    }

    private void timerReservationStatus() {
        final Runnable beeper = new Runnable() {
                @Override
                public void run() {
                    try {
                        handleStatusTimer();
                    } catch (RejectedExecutionException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (NullPointerException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (IllegalArgumentException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (Exception e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    }

                }
            };
            final ScheduledFuture<?> beeperHandle = scheduler.schedule(beeper, CONSTANT_SCHEDULE_USR_MEDIA , SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, 5, SECONDS);
    }

    private synchronized void handleStatusTimer(){
        Integer syncStatus = null;
        try{
            ConnectionWrapper con = SystemInfo.getConnection();
            this.media.setShopID(this.shop.getShopID());
            this.media.setMediaID(this.selectedMedia);
            syncStatus = this.media.getSyncStatus(con);
        }catch (Exception e) {
            e.printStackTrace();
        }
        handleSyncStatusTimer(syncStatus);
    }

    private void handleSyncStatusTimer(Integer syncStatus) {
        if(null == syncStatus){
            reservationDate.setEnabled(true);
            btnRegistStartOff.setEnabled(true);
            statusTabReservation.setText("");
            scheduler.shutdown();
            return;
        }
        System.out.println(dateFormat.format(new Date()) + " sync status is: "
                                        + syncStatus);
        switch (syncStatus){
            case STATUS_ONE :
                reservationDate.setEnabled(true);
                btnRegistStartOff.setEnabled(true);
                statusTabReservation.setText("処理中です。しばらくお待ちください。");
                if(reservationPanel.isDisplayable()){
                    timerReservationStatus();
                }else{
                    scheduler.shutdown();
                }
                break;
            case STATUS_TWO:
                reservationDate.setEnabled(true);
                btnRegistStartOff.setEnabled(true);
                statusTabReservation.setText("処理が完了しました。");
                scheduler.shutdown();
                break;
            case STATUS_NINE:
                reservationDate.setEnabled(false);
                btnRegistStartOff.setEnabled(false);
                statusTabReservation.setText("エラーが発生しました。管理者に連絡してください。");
                scheduler.shutdown();
                break;
            default:
                reservationDate.setEnabled(true);
                btnRegistStartOff.setEnabled(true);
                statusTabReservation.setText("");
                scheduler.shutdown();
                break;
        }
    }
    //nhtvinh end add 20161021 New request #54239

    private void showDataStaff() {
        try {

            SwingUtil.clearTable(tbStaff);
            SwingUtil.clearTable(tbStaffPos);
            staffPosMap.clear();

            //sort list staff API
            this.mstApi.getListStaff();
            Collections.sort(this.mstApi.getListStaff());
            int indexStartDel = 0;
            for (APIStaff staff : this.mstApi.getListStaff()) {
                if(staff.isIs_disabled()) {
                    break;
                }
                indexStartDel ++;
            }
            initStaffTable(indexStartDel);
            DefaultTableModel model = (DefaultTableModel) tbStaff.getModel();
            DefaultTableModel modelStaffPos = (DefaultTableModel) tbStaffPos.getModel();

            //show table pos
            //int rowIndx = 0;
            for (MstStaff staff : this.staffsPos) {

                if(staff.getStaffID() == null) {
                    continue;
                }
                // set all id staff pos
                //if exists in database then set id
                staffPosMap.put(staff.getStaffID(), null);
                for (MstKANZASHIStaff staffKanZaShi : this.staffMedia) {
                    if(staffKanZaShi.getSposStaffID() != null && staffKanZaShi.getSposStaffID() != 0
                        && staffKanZaShi.getSposStaffID().equals(staff.getStaffID())) {
                        staffPosMap.put(staff.getStaffID(), staffKanZaShi.getStaffID());
                        break;
                    }
                }

                Object[] rowData
                        = {
                            staff//,
                            //getCheckBoxStaff(tbStaffPos)
                        };
                modelStaffPos.addRow(rowData);
                //rowIndx ++;
            }

            for (APIStaff staff : this.mstApi.getListStaff()) {
                if(staff.isIs_disabled()) {
                    Object[] rowData
                            = {
                                getMediaName(staff.getMedia_ids().get(0).getMedia()),
                                staff,
                                getCheckBoxStaff(staff.getId()),
                                LABLE_INVALID
                            };
                    model.addRow(rowData);
                }else {
                    Object[] rowData
                            = {
                                getMediaName(staff.getMedia_ids().get(0).getMedia()),
                                staff,
                                getCheckBoxStaff(staff.getId()),
                                ""
                            };
                    model.addRow(rowData);
                }
            }
            //API deleted items
            for (MstKANZASHIStaff staffMedia : this.staffMedia) {
                boolean flagExists = false;
                for (APIStaff staff : this.mstApi.getListStaff()) {
                    if(staff.getId().equals(staffMedia.getStaffID())) {
                        flagExists = true;
                        break;
                    }
                }
                if(!flagExists) {
                    APIStaff staffDel = new APIStaff();
                    staffDel.setId(staffMedia.getStaffID());
                    staffDel.setName(staffMedia.getName());
                    Object[] rowData
                        = {
                            getMediaName(staffMedia.getMediaID()),
                            staffDel,
                            getCheckBoxStaff(staffMedia.getStaffID()),
                            LABLE_INVALID
                        };
                model.addRow(rowData);
                }
            }

            if(tbStaffPos.getRowCount() > 0) {
                tbStaffPos.setRowSelectionInterval(0, 0);
                tbStaffPos.setColumnSelectionInterval(0, 0);
                //selectedRowTable(tbStaffPos, tbStaffPos, tbStaff, this.staffPosMap, 1, 2);
                selectedRow(STAFF_TABLE);
            }

            tbStaff.repaint();
            groupPwBusinessScrollPane1.repaint();
            tabStaff.repaint();
            mainTab.repaint();
            //checkEnable(tbStaff);
        } catch (Exception ex) {
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="施術台">
    private boolean registBed(ConnectionWrapper con) {
        boolean result = false;
        int colInvalid = 3;
        if(this.tbBedPos.getRowCount() == 0 || this.tbBed.getRowCount() == 0) {
            return result;
        }
        MstKANZASHIBed bed ;
        ArrayList<Integer> arrBedID = new ArrayList<Integer>();
        for ( int i = 0;i < this.bedMedia.size();i ++) {
            arrBedID.add(this.bedMedia.get(i).getBedID());
        }
        try {
            con.begin();
            for (int i = 0; i < bedPosMap.values().size(); i++) {
                if(null != bedPosMap.values().toArray()[i] && ((ObjectID)bedPosMap.values().toArray()[i]).id != null) {
                    boolean flagStop = false;
                    for (int s = 0; s < tbBed.getRowCount(); s ++) {
                        if(((ObjectID)bedPosMap.values().toArray()[i]).id.equals(((APIBed)tbBed.getValueAt(s, 1)).getId())
                                && tbBed.getValueAt(s, colInvalid).toString().equals(LABLE_INVALID)) {
                            flagStop = true;
                            break;
                        }
                    }
                    if(flagStop) {
                        continue;
                    }

                    bed = new MstKANZASHIBed();
                    bed.setShopID(this.shop.getShopID());

                    arrBedID.remove(((ObjectID)bedPosMap.values().toArray()[i]).id);
                    bed.setBedID(((ObjectID)bedPosMap.values().toArray()[i]).id);
                    bed.setMediaID(((ObjectID)bedPosMap.values().toArray()[i]).media);
                    bed.setName(((ObjectID)bedPosMap.values().toArray()[i]).name);

                    bed.setSposBedID(((ObjectID)bedPosMap.values().toArray()[i]).idPos);
                    if (bed.regist(con)) {
                        result = true;
                    } else {
                        con.rollback();
                        result = false;
                    }
                }
            }
            //delete when 「mst_kanzashi_bed」に存在する　＆　「一覧に存在しない」
            for (MstKANZASHIBed bedKanZaShi: this.bedMedia) {
                for (int i = 0;i < arrBedID.size();i ++) {
                    if(bedKanZaShi.getBedID().equals(arrBedID.get(i))){
                        if (bedKanZaShi.delete(con)) {
                            result = true;
                        } else {
                            con.rollback();
                            result = false;
                        }
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    private void loadDataBed() {
        MstKANZASHIBed bed = new MstKANZASHIBed();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            bed.setShopID(this.shop.getShopID());
            //bed.setMediaID(this.selectedMedia);
            this.bedMedia = bed.loadAll(con);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void showDataBed() {
        try {

            SwingUtil.clearTable(tbBed);
            SwingUtil.clearTable(tbBedPos);
            bedPosMap.clear();

            //sort list bed API
            this.mstApi.getListBed();
            Collections.sort(this.mstApi.getListBed());
            int indexStartDel = 0;
            for (APIBed bed : this.mstApi.getListBed()) {
                if(bed.isIs_disabled()) {
                    break;
                }
                indexStartDel ++;
            }
            initBedTable(indexStartDel);
            DefaultTableModel model = (DefaultTableModel) tbBed.getModel();
            DefaultTableModel modelBedPos = (DefaultTableModel) tbBedPos.getModel();

            //show table pos
            //int rowIndx = 0;
            for (MstBed bed : this.bedsPos) {

                if(bed.getBedID() == null) {
                    continue;
                }
                // set all id bed pos
                //if exists in database then set id
                bedPosMap.put(bed.getBedID(), null);
                for (MstKANZASHIBed bedKaZanShi : this.bedMedia) {
                    if(bedKaZanShi.getSposBedID() != null && bedKaZanShi.getSposBedID() != 0
                        && bedKaZanShi.getSposBedID().equals(bed.getBedID())) {
                        bedPosMap.put(bed.getBedID(), new ObjectID(bedKaZanShi.getBedID() ,bedKaZanShi.getSposBedID(), bedKaZanShi.getMediaID(), bedKaZanShi.getName()));
                        break;
                    }
                }

                Object[] rowData
                        = {
                            bed//,
                            //getCheckBoxBed(tbBedPos)
                        };
                modelBedPos.addRow(rowData);
            }

            for (APIBed bed : this.mstApi.getListBed()) {
                if(bed.isIs_disabled()) {
                    Object[] rowData
                            = {
                                getMediaName(bed.getMedia_ids().get(0).getMedia()),
                                bed,
                                getCheckBoxBed(bed.getId()),
                                LABLE_INVALID
                            };
                    model.addRow(rowData);
                }else {
                    Object[] rowData
                            = {
                                getMediaName(bed.getMedia_ids().get(0).getMedia()),
                                bed,
                                getCheckBoxBed(bed.getId()),
                                ""
                            };
                    model.addRow(rowData);
                }

            }
            //API deleted items
            for (MstKANZASHIBed bedKanZaShi : this.bedMedia) {
                boolean flagExists = false;
                for (APIBed bed : this.mstApi.getListBed()) {
                    if(bed.getId().equals(bedKanZaShi.getBedID())) {
                        flagExists = true;
                        break;
                    }
                }
                if(!flagExists) {
                    APIBed bedDel = new APIBed();
                    bedDel.setId(bedKanZaShi.getBedID());
                    bedDel.setName(bedKanZaShi.getName());
                    Object[] rowData
                        = {
                            getMediaName(bedKanZaShi.getMediaID()),
                            bedDel,
                            getCheckBoxBed(bedKanZaShi.getBedID()),
                            LABLE_INVALID
                        };
                model.addRow(rowData);
                }
            }
            if(tbBedPos.getRowCount() > 0) {
                tbBedPos.setRowSelectionInterval(0, 0);
                tbBedPos.setColumnSelectionInterval(0, 0);
                //selectedRowTable(tbBedPos, tbBedPos, tbBed, bedPosMap, 1, 2);
                selectedRow(BED_TABLE);
            }

            tbBed.repaint();
            mainTab.repaint();
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="シフトパターン">
    private boolean registShift(ConnectionWrapper con) {
        boolean result = false;
        int colInvalid = 4;
        if(this.tbShiftPos.getRowCount() == 0 || this.tbShift.getRowCount() == 0) {
            return result;
        }
        MstKANZASHIShift shift ;
        ArrayList<Integer> arrShiftID = new ArrayList<Integer>();
        for ( int i = 0;i < this.shiftMedia.size();i ++) {
            arrShiftID.add(this.shiftMedia.get(i).getShiftID());
        }

        try {
            con.begin();

            for (int i = 0; i < this.shiftPosMap.values().size(); i++) {
                if(null != shiftPosMap.values().toArray()[i] && ((ObjectID)shiftPosMap.values().toArray()[i]).id != null) {
                    shift = new MstKANZASHIShift();
                    shift.setShopID(this.shop.getShopID());
                    for ( int row = 0;row < this.tbShift.getRowCount();row ++) {
                        // not in API or shift is deleted
                        if(((APIShift)tbShift.getValueAt(row, 1)).getMedia_ids().isEmpty()
                            || ((APIShift)tbShift.getValueAt(row, 1)).getMedia_ids().get(0).getMedia() == null
                            || tbShift.getValueAt(row, colInvalid).toString().equals(LABLE_INVALID)  ) {
                            continue;
                        }
                        if(((ObjectID)shiftPosMap.values().toArray()[i]).id.equals(((APIShift) this.tbShift.getValueAt(row, 1)).getId())) {
                            arrShiftID.remove(((APIShift) this.tbShift.getValueAt(row, 1)).getId());
                            shift.setShiftID(((APIShift) this.tbShift.getValueAt(row, 1)).getId());
                            shift.setMediaID(((APIShift) this.tbShift.getValueAt(row, 1)).getMedia_ids().get(0).getMedia());
                            shift.setName(((APIShift) this.tbShift.getValueAt(row, 1)).getName());
                            //IVS_PTQUANG start 2016/09/15 New request #54718
                            shift.setStart_time(((APIShift) this.tbShift.getValueAt(row, 1)).getStart_time());
                            shift.setEnd_time(((APIShift) this.tbShift.getValueAt(row, 1)).getEnd_time());
                            //IVS_PTQUANG end 2016/09/15 New request #54718
                            shift.setSposShiftID(((ObjectID)shiftPosMap.values().toArray()[i]).idPos);
                            if (shift.regist(con)) {
                                result = true;
                            } else {
                                con.rollback();
                                result = false;
                            }
                        }
                    }
                }
            }
            //delete when 「mst_kanzashi_shift」に存在する　＆　「一覧に存在しない」
            for(MstKANZASHIShift shiftKanZaShi: this.shiftMedia) {
                for (int i = 0;i < arrShiftID.size();i ++) {
                    if(shiftKanZaShi.getShiftID().equals(arrShiftID.get(i))) {
                        if (shiftKanZaShi.delete(con)) {
                            result = true;
                        } else {
                            con.rollback();
                            result = false;
                        }
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }

        return result;
    }

    private void loadDataShift() {
        MstKANZASHIShift shift = new MstKANZASHIShift();

        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            shift.setShopID(this.shop.getShopID());
            //shift.setMediaID(this.selectedMedia);
            this.shiftMedia = shift.loadAll(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void showDataShift() {

        try {

            SwingUtil.clearTable(tbShift);
            SwingUtil.clearTable(tbShiftPos);
            shiftPosMap.clear();

            //sort list shift API
            this.mstApi.getListShift();
            Collections.sort(this.mstApi.getListShift());
            int indexStartDel = 0;
            for (APIShift shift : this.mstApi.getListShift()) {
                if(shift.isIs_deleted()) {
                    break;
                }
                indexStartDel ++;
            }
            initShiftTable(indexStartDel);
            DefaultTableModel model = (DefaultTableModel) tbShift.getModel();
            DefaultTableModel modelShiftPos = (DefaultTableModel) tbShiftPos.getModel();

            //show table pos
            //int rowIndx = 0;
            for (MstShift shift : this.shiftsPos) {

                if(shift.getShiftId() == null) {
                    continue;
                }
                // set all id shift pos
                //if exists in database then set id
                shiftPosMap.put(shift.getShiftId(), null);
                for (MstKANZASHIShift shiftKanZaShi : this.shiftMedia) {
                    if(shiftKanZaShi.getSposShiftID() != null && shiftKanZaShi.getSposShiftID() != 0
                        && shiftKanZaShi.getSposShiftID().equals(shift.getShiftId())) {
                        shiftPosMap.put(shift.getShiftId(), new ObjectID(shiftKanZaShi.getShiftID(), shiftKanZaShi.getSposShiftID(), shiftKanZaShi.getMediaID(), shiftKanZaShi.getName()));
                        break;
                    }
                }

                Object[] rowData
                        = {
                            new ShiftObject(shift)//,
                            //getCheckBoxShift(tbShiftPos)
                        };
                modelShiftPos.addRow(rowData);
                //rowIndx ++;
            }

            for (APIShift shift : this.mstApi.getListShift()) {
                if(shift.isIs_deleted()) {
                    Object[] rowData
                            = {
                                getMediaName(shift.getMedia_ids().get(0).getMedia()),
                                shift,
                                workingHours(shift.getStart_time(), shift.getEnd_time()),
                                getCheckBoxShift(shift.getId()),
                                LABLE_INVALID
                            };
                    model.addRow(rowData);
                }else {
                    Object[] rowData
                            = {
                                getMediaName(shift.getMedia_ids().get(0).getMedia()),
                                shift,
                                workingHours(shift.getStart_time(), shift.getEnd_time()),
                                getCheckBoxShift(shift.getId()),
                                ""
                            };
                    model.addRow(rowData);
                }
            }

            //API deleted items
            for (MstKANZASHIShift shiftKanZaShi : this.shiftMedia) {
                boolean flagExists = false;
                for (APIShift shift : this.mstApi.getListShift()) {
                    if(shift.getId().equals(shiftKanZaShi.getShiftID())) {
                        flagExists = true;
                        break;
                    }
                }
                if(!flagExists) {
                    APIShift shiftDel = new APIShift();
                    shiftDel.setId(shiftKanZaShi.getShiftID());
                    shiftDel.setName(shiftKanZaShi.getName());
                    Object[] rowData
                        = {
                            getMediaName(shiftKanZaShi.getMediaID()),
                            shiftDel,
                            workingHours(shiftKanZaShi.getStart_time(), shiftKanZaShi.getEnd_time()),
                            getCheckBoxShift(shiftKanZaShi.getShiftID()),
                            LABLE_INVALID
                        };
                model.addRow(rowData);
                }
            }
            if(tbShiftPos.getRowCount() > 0) {
                tbShiftPos.setRowSelectionInterval(0, 0);
                tbShiftPos.setColumnSelectionInterval(0, 0);
                //selectedRowTable(tbShiftPos, tbShiftPos, tbShift, this.shiftPosMap, 1, 3);
                selectedRow(SHIFT_TABLE);
            }

            tbShift.repaint();
            mainTab.repaint();
        } catch (Exception e) {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
             Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private String workingHours(String startTime, String endTime) {
        if("99:91".equals(startTime)) {
            startTime = "営業開始時間";
        }
        if("99:92".equals(endTime)) {
            endTime = "営業終了時間";
        }

        return startTime + "～" + endTime;
    }

    //</editor-fold>

    /**
     * selected row table pos data
     * @param indexTable
     */
    private void selectedRow(int indexTable) {
        int colCheckBox = 2;
        int colInvalid  = 3;
        int rowSelectedIndex;
        try {
            switch (indexTable) {
                case STAFF_TABLE:
                    rowSelectedIndex = tbStaffPos.getSelectedRow();
                    if(rowSelectedIndex >= 0) {
                        Integer staffIDPos = ((MstStaff)tbStaffPos.getValueAt(rowSelectedIndex, 0)).getStaffID();
                        Integer value = staffPosMap.get(staffIDPos);
                        for (int i = 0;i < tbStaff.getRowCount() ;i ++) {
                            Integer staffMediaID = ((APIStaff)tbStaff.getValueAt(i, 1)).getId();
                            //clear checked
                            ((JCheckBox)tbStaff.getValueAt(i, colCheckBox)).setSelected(false);
                            ((JCheckBox)tbStaff.getValueAt(i, colCheckBox)).setEnabled(true);
                            // if contains value set enalble is false
                            if(staffPosMap.containsValue(staffMediaID) || tbStaff.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                                ((JCheckBox)tbStaff.getValueAt(i, colCheckBox)).setEnabled(false);
                            }
                            // enable and selected checkbox
                            if(value != null && value.equals(staffMediaID)) {
                                ((JCheckBox)tbStaff.getValueAt(i, colCheckBox)).setSelected(true);
                                if(!tbStaff.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                                    ((JCheckBox)tbStaff.getValueAt(i, colCheckBox)).setEnabled(true);
                                }
                            }
                            ((JCheckBox)tbStaff.getValueAt(i, colCheckBox)).repaint();
                            tbStaff.repaint();
                        }
                    }
                    break;
                case BED_TABLE:
                    rowSelectedIndex = tbBedPos.getSelectedRow();
                    if(rowSelectedIndex >= 0) {
                        Integer bedIDPos = ((MstBed)tbBedPos.getValueAt(rowSelectedIndex, 0)).getBedID();
                        ObjectID value = bedPosMap.get(bedIDPos);
                        for (int i = 0;i < tbBed.getRowCount() ;i ++) {
                            Integer bedMediaID = ((APIBed)tbBed.getValueAt(i, 1)).getId();
                            //clear checked
                            ((JCheckBox)tbBed.getValueAt(i, colCheckBox)).setSelected(false);
                            ((JCheckBox)tbBed.getValueAt(i, colCheckBox)).setEnabled(true);
                            // if contains value set enalble is false
                            boolean isExists = false;
                            for (Integer key : bedPosMap.keySet()) {
                                ObjectID val = bedPosMap.get(key);
                                if(null != val && val.id.equals(bedMediaID)) {
                                    isExists = true;
                                    break;
                                }
                            }
                            if(isExists || tbBed.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                                ((JCheckBox)tbBed.getValueAt(i, colCheckBox)).setEnabled(false);
                            }
                            // enable and selected checkbox
                            if(value != null && value.id.equals(bedMediaID)) {
                                ((JCheckBox)tbBed.getValueAt(i, colCheckBox)).setSelected(true);
                                if(!tbBed.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                                    ((JCheckBox)tbBed.getValueAt(i, colCheckBox)).setEnabled(true);
                                }
                            }
                            ((JCheckBox)tbBed.getValueAt(i, colCheckBox)).repaint();
                            tbBed.repaint();
                        }
                    }
                    break;
                case SHIFT_TABLE:
                    colCheckBox = 3;
                    colInvalid  = 4;
                    rowSelectedIndex = tbShiftPos.getSelectedRow();

                    if(rowSelectedIndex >= 0) {
                        Integer shiftIDPos = ((ShiftObject)tbShiftPos.getValueAt(rowSelectedIndex, 0)).getShift().getShiftId();
                        ObjectID value = shiftPosMap.get(shiftIDPos);
                        for (int i = 0;i < tbShift.getRowCount() ;i ++) {
                            Integer shiftMediaID = ((APIShift)tbShift.getValueAt(i, 1)).getId();
                            //clear checked
                            ((JCheckBox)tbShift.getValueAt(i, colCheckBox)).setSelected(false);
                            ((JCheckBox)tbShift.getValueAt(i, colCheckBox)).setEnabled(true);
                            // if contains value set enalble is false
                            boolean isExists = false;
                            for (Integer key : shiftPosMap.keySet()) {
                                ObjectID val = shiftPosMap.get(key);
                                if(null != val && val.id.equals(shiftMediaID)) {
                                    isExists = true;
                                    break;
                                }
                            }
                            if(isExists || tbShift.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                                ((JCheckBox)tbShift.getValueAt(i, colCheckBox)).setEnabled(false);
                            }
                            // enable and selected checkbox
                            if(value != null && value.id.equals(shiftMediaID)) {
                                ((JCheckBox)tbShift.getValueAt(i, colCheckBox)).setSelected(true);
                                if(!tbShift.getValueAt(i, colInvalid).toString().equals(LABLE_INVALID)) {
                                    ((JCheckBox)tbShift.getValueAt(i, colCheckBox)).setEnabled(true);
                                }
                            }
                            ((JCheckBox)tbShift.getValueAt(i, colCheckBox)).repaint();
                            tbShift.repaint();
                        }
                    }
                    break;
                default:
                    break;
            }
        }catch (Exception ex) {
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id is staff_id, bed_id, shift_id
     * @param isCheck check box
     * @param indexTable
     */
    private void changeSelected(Integer id,boolean isCheck, int indexTable) {
        int rowSelectedIndex;
        try {
            switch (indexTable) {
                case STAFF_TABLE:
                    rowSelectedIndex = tbStaffPos.getSelectedRow();
                    if(rowSelectedIndex >= 0) {
                        Integer staffIDPos = ((MstStaff)tbStaffPos.getValueAt(rowSelectedIndex, 0)).getStaffID();
                        if(isCheck) {
                            staffPosMap.put(staffIDPos, id);
                        }else {
                            staffPosMap.put(staffIDPos, null);
                        }
                        selectedRow(indexTable);
                    }
                    break;
                case BED_TABLE:
                    rowSelectedIndex = tbBedPos.getSelectedRow();
                    if(rowSelectedIndex >= 0) {
                        Integer bedIDPos = ((MstBed)tbBedPos.getValueAt(rowSelectedIndex, 0)).getBedID();
                        if(isCheck) {
                            APIBed apiBed = new APIBed();
                            for(APIBed bed: this.mstApi.getListBed()) {
                                if(bed.getId().equals(id)) {
                                    apiBed = bed;
                                    break;
                                }
                            }
                            bedPosMap.put(bedIDPos, new ObjectID(id, bedIDPos, apiBed.getMedia_ids().get(0).getMedia(), apiBed.getName()));
                        }else {
                            bedPosMap.put(bedIDPos, null);
                        }
                        selectedRow(indexTable);
                    }
                    break;
                case SHIFT_TABLE:
                    rowSelectedIndex = tbShiftPos.getSelectedRow();
                    if(rowSelectedIndex >= 0) {
                        Integer shiftIDPos = ((ShiftObject)tbShiftPos.getValueAt(rowSelectedIndex, 0)).getShift().getShiftId();
                        if(isCheck) {
                            APIShift apiShift = new APIShift();
                            for(APIShift shift: this.mstApi.getListShift()) {
                                if(shift.getId().equals(id)) {
                                    apiShift = shift;
                                    break;
                                }
                            }
                            shiftPosMap.put(shiftIDPos, new ObjectID(id, shiftIDPos, apiShift.getMedia_ids().get(0).getMedia(), apiShift.getName()));
                        }else {
                            shiftPosMap.put(shiftIDPos, null);
                        }
                        selectedRow(indexTable);
                    }
                    break;
                default:
                    break;
            }
        }catch(Exception ex) {
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JCheckBox getCheckBoxStaff(final Integer mediaStaffID) {
        final JCheckBox check = new JCheckBox();

        check.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        check.setHorizontalAlignment(JCheckBox.CENTER);

        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSelected(mediaStaffID, check.isSelected(), STAFF_TABLE);
            }
        });
        return check;
    }

    private JCheckBox getCheckBoxBed(final Integer mediaBedID) {
        final JCheckBox check = new JCheckBox();

        check.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        check.setHorizontalAlignment(JCheckBox.CENTER);

        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSelected(mediaBedID, check.isSelected(), BED_TABLE);
            }
        });
        return check;
    }

    private JCheckBox getCheckBoxShift(final int mediaShiftID) {
        final JCheckBox check = new JCheckBox();

        check.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        check.setHorizontalAlignment(JCheckBox.CENTER);

        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSelected(mediaShiftID, check.isSelected(), SHIFT_TABLE);
            }
        });
        return check;
    }

    private String getMediaName(String MediaID) {
        String mediaName = "";

        try {
        for (MstAPIDetail detail : mstApi) {
            if(detail.getMediaID().equals(MediaID)) {
                mediaName = detail.getMediaName();
            }
        }
        } catch (ArrayIndexOutOfBoundsException  e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch(ArithmeticException e)
        {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return mediaName;
    }

    /**
     * send is_suspended and password to API->POST /salon/{SalonID}/user-media/{Media}
     * then check response , if success return code 200, else return code <> 200
     * @param salonID
     * @param media
     * @param isSuspended
     * @param password
    * @return
    */
    public int sendUserMediaAPI(Integer salonID, String media, boolean isSuspended, String password){
        int HttpResult = -1;
        try{
            String apiUrl = mstApi.getApiUrl();
            String url = apiUrl + "/salon/" + salonID + "/user-media/" + media;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", mstApi.getApiAuthCode());
            con.setRequestProperty( "charset", "utf-8");

            //set parameter
            Map mapParam = new LinkedMap();
            mapParam.put("is_suspended", isSuspended);
            //LVTu start add 2016/11/22 New request #54178
            mapParam.put("login_check", true); //true（固定）
            //LVTu end add 2016/11/22 New request #54178
            mapParam.put("password", password);
            Gson gson = new Gson();
            String jsonParam = gson.toJson(mapParam);
            String urlParameters = jsonParam;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();

            //IVS_LVTu start edit 2016/12/05 New request #58830
              //get response
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
            HttpResult = con.getResponseCode();
//            if (HttpResult == 200) {
//                return HttpResult;
//            }
            //IVS_LVTu end edit 2016/12/05 New request #58830
        }catch(Exception e){
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
            return HttpResult;
        }
        return HttpResult;
    }

    /**
     * get data from API.
     */
    public void settingSalon() {
        this.mediaSalon.getSalonAPI();

        if(this.mediaSalon.getStatus() == this.STATUS_EIGHT) {
            System.out.println("status : 8");
            rdoValid.setSelected(true);
            btRegistSalon.setEnabled(false);
            this.mainTab.setEnabledAt(this.TAB_STAFF, true);
            this.mainTab.setEnabledAt(this.TAB_BED, true);
            this.mainTab.setEnabledAt(this.TAB_SHIFT, true);
            this.mainTab.setEnabledAt(this.TAB_MEDIA, true);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, true);
            rdoStop.requestFocusInWindow();
        }else if(this.mediaSalon.getStatus() == this.STATUS_NINE) {
            System.out.println("status : 9");
            rdoStop.setSelected(true);
            btRegistSalon.setEnabled(false);
            this.mainTab.setEnabledAt(this.TAB_STAFF, false);
            this.mainTab.setEnabledAt(this.TAB_BED, false);
            this.mainTab.setEnabledAt(this.TAB_SHIFT, false);
            this.mainTab.setEnabledAt(this.TAB_MEDIA, false);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, false);

            //message info
            if(messageFirst)
                MessageDialog.showMessageDialog(this,
                        "現在停止中です。\nサイトコントロール機能を利用するには、\n有効を選択後登録ボタンを押してください。",
                        "停止中",
                        JOptionPane.INFORMATION_MESSAGE);
            rdoValid.requestFocusInWindow();
        }else if(this.mediaSalon.getStatus() >= this.STATUS_ONE && this.mediaSalon.getStatus() <= this.STATUS_THREE) {
            System.out.println("status : 1-3");
            this.mainTab.setEnabledAt(this.TAB_SALON, false);
            this.mainTab.setEnabledAt(this.TAB_STAFF, false);
            this.mainTab.setEnabledAt(this.TAB_BED, false);
            this.mainTab.setEnabledAt(this.TAB_SHIFT, false);
            this.mainTab.setEnabledAt(this.TAB_MEDIA, false);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, false);

            //set all componet tab salon enalbled is false
            for ( Component c : tabSalon.getComponents()) {
                if(c.equals(salonPanel)) {
                    for ( Component cc : salonPanel.getComponents()) {
                        cc.setEnabled(false);
                    }
                }
            }
            //message info
            if(messageFirst)
                MessageDialog.showMessageDialog(this,
                        "導入処理が完了していません。",
                        "導入未完了",
                        JOptionPane.INFORMATION_MESSAGE);
            SiteControlIntroductionPanel controlIntroduct = new SiteControlIntroductionPanel();
            controlIntroduct.setOpener(this);
            this.setVisible(false);

            parentFrame.changeContents(controlIntroduct);
        //IVS_LVTu start edit 2016/11/14 New request #58581    
        }else if(this.mediaSalon.getStatus() >= this.STATUS_FOUR && this.mediaSalon.getStatus() <= this.STATUS_SIX) {
            System.out.println("status : 4-6");
            this.mainTab.setEnabledAt(this.TAB_SALON, false);
            this.mainTab.setEnabledAt(this.TAB_MEDIA, false);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, false);

            //index tab staff
            this.mainTab.setSelectedIndex(this.TAB_STAFF);

        }else if(this.mediaSalon.getStatus() == this.STATUS_SEVEN) { //　APIのサロンステータスが７の場合は、「基本設定タブ」を活性に変更。
            System.out.println("status : 7");
            this.mainTab.setEnabledAt(this.TAB_MEDIA, false);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, false);
            this.btRegistSalon.setEnabled(false);
        //IVS_LVTu end edit 2016/11/14 New request #58581        
        }else if(this.mediaSalon.getStatus() == this.STATUS_TEN) {
            System.out.println("status : 10");
            this.mainTab.setEnabledAt(this.TAB_SALON, false);
            this.mainTab.setEnabledAt(this.TAB_STAFF, false);
            this.mainTab.setEnabledAt(this.TAB_BED, false);
            this.mainTab.setEnabledAt(this.TAB_SHIFT, false);
            this.mainTab.setEnabledAt(this.TAB_MEDIA, false);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, false);

            //set all componet tab salon enalbled is false
            for ( Component c : tabSalon.getComponents()) {
                if(c.equals(salonPanel)) {
                    for ( Component cc : salonPanel.getComponents()) {
                        cc.setEnabled(false);
                    }
                }
            }
            //message info
            if(messageFirst)
                MessageDialog.showMessageDialog(this,
                        "サイトコントロール機能は解約済みです。",
                        "解約済み",
                        JOptionPane.INFORMATION_MESSAGE);
        }else if(this.mediaSalon.getStatus() == this.STATUS_101 || this.mediaSalon.getStatus() == this.STATUS_102) {
            System.out.println("status : 101-102");
            this.mainTab.setEnabledAt(this.TAB_SALON, false);
            this.mainTab.setEnabledAt(this.TAB_STAFF, false);
            this.mainTab.setEnabledAt(this.TAB_BED, false);
            this.mainTab.setEnabledAt(this.TAB_SHIFT, false);
            this.mainTab.setEnabledAt(this.TAB_MEDIA, false);
            this.mainTab.setEnabledAt(this.TAB_RESERVATION, false);

            //set all componet tab salon enalbled is false
            for ( Component c : tabSalon.getComponents()) {
                if(c.equals(salonPanel)) {
                    for ( Component cc : salonPanel.getComponents()) {
                        cc.setEnabled(false);
                    }
                }
            }
            //message info
            if(messageFirst)
                MessageDialog.showMessageDialog(this,
                        "エラーが発生しました。\nサポートまでご連絡ください。",
                        "システムエラー",
                        JOptionPane.INFORMATION_MESSAGE);
        }
        this.mainTab.repaint();
        //when regist then no message.
        messageFirst = false;
    }

    //IVS_LVTu start add 2016/11/07 New request #58479
    /**
     * 入力チェックを行う。
     * @return true - ＯＫ
     */
    private boolean checkInputReservation() {
        if(reservationDate.getDate() == null) {
            MessageDialog.showMessageDialog(this,
                                "予約連携日付を入力して下さい",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
            reservationDate.requestFocusInWindow();
            return false;
        }
        //Format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date reservationDay = null;
        Date today = null;
        //本日より6日以前の予約連携はできません
        int six = 6;
        try {
            reservationDay = sdf.parse(sdf.format(reservationDate.getDate()));
            today = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(MediaSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        //本日日付より6日以前
        if(TimeUnit.MILLISECONDS.toDays(today.getTime() - reservationDay.getTime()) >= six)
        {
            Calendar dtToday = Calendar.getInstance();

            dtToday.add(Calendar.DATE, - 5);
            int year = dtToday.get(Calendar.YEAR);
            int month = dtToday.get(Calendar.MONTH) + 1;
            int day = dtToday.get(Calendar.DAY_OF_MONTH);

            String mgs = "本日より6日以前の予約連携はできません。\r\n" + year + "年"+ String.valueOf(month < 10 ? "0" + month : month)
                    +"月" + String.valueOf(day < 10 ? "0" + day : day) +"日以降の日付を選択して下さい。";
            MessageDialog.showMessageDialog(this,
                            mgs,
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            reservationDate.requestFocusInWindow();
            return	false;
        }

        return true;
    }
    //IVS_LVTu end add 2016/11/07 New request #58479

    class ShiftObject {

        private MstShift shift;

        public MstShift getShift() {
            return shift;
        }

        public void setShift(MstShift shift) {
            this.shift = shift;
        }

        public ShiftObject( MstShift shift) {
            this.shift = shift;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if(this.shift.getShiftId() == null) {
                return "";
            }
            sb.append(shift.getShiftName() != null ? shift.getShiftName():"" ).append("(").append(getFomatTime(shift.getStartTime())).append("～").append(getFomatTime(shift.getEndTime())).append(")");

            return sb.toString();
        }

        private String getFomatTime(String coming_time) {
            return coming_time.substring(0, 2) + ":" + coming_time.substring(2, 4);
        }
    }

    class ObjectID {
        Integer id          = null;
        Integer idPos       = null;
        String  media       = null;
        String  name        = null;

        public ObjectID() {
        }

        public ObjectID(Integer id,Integer idPos, String idMedia, String name ) {
            this.id = id;
            this.idPos = idPos;
            this.media = idMedia;
            this.name = name;
        }


    }

    public class MediaTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * セルの端とテキストのマージン
         */
        private static final int SIDE_MARGIN = 4;
        private Object value = null;
        /**
         * 選択されているかどうか
         */
        private boolean isSelected = false;
        /**
         * 選択時の色
         */
        private Color selectedRowColor = null;
        /**
         * 影の色０
         */
        private Color shadow0Color = null;
        /**
         * 影の色１
         */
        private Color shadow1Color = null;
        /**
         * ハイライトの色
         */
        private Color highlightColor = null;

        private int rRow;
        private int rCol;
        private int type;
        private int rowDel;

        private Color cellBackGroundColor = Color.WHITE;

        public Color getCellBackGroundColor() {
            return cellBackGroundColor;
        }

        public void setCellBackGroundColor(Color cellBackGroundColor) {
            this.cellBackGroundColor = cellBackGroundColor;
        }
        private Color Colors[][];

        public Color[][] getColors() {
            return Colors;
        }

        public void setColors(Color[][] Colors) {
            this.Colors = Colors;
        }

        /**
         * コンストラクタ
         */
        public MediaTableCellRenderer(int type,int rowDel) {
            super();
            this.type = type;
            this.rowDel = rowDel;
            setSelectedRowColor(new Color(255, 210, 142));
            setShadow0Color(new Color(113, 113, 113));
            setShadow1Color(new Color(172, 172, 172));
            setHighlightColor(new Color(241, 241, 241));
        }

        public String getText() {
            if (super.getText() == null) {
                return "";
            } else {
                return super.getText();
            }
        }

        /**
         * 選択時の色を取得する。
         *
         * @return 選択時の色
         */
        public Color getSelectedRowColor() {
            return selectedRowColor;
        }

        /**
         * 選択時の色を設定する。
         *
         * @param selectedRowColor 選択時の色
         */
        public void setSelectedRowColor(Color selectedRowColor) {
            this.selectedRowColor = selectedRowColor;
        }

        /**
         * 影の色０を取得する。
         *
         * @return 影の色０
         */
        public Color getShadow0Color() {
            return shadow0Color;
        }

        /**
         * 影の色０を設定する。
         *
         * @param shadow0Color 影の色０
         */
        public void setShadow0Color(Color shadow0Color) {
            this.shadow0Color = shadow0Color;
        }

        /**
         * 影の色１を取得する。
         *
         * @return 影の色１
         */
        public Color getShadow1Color() {
            return shadow1Color;
        }

        /**
         * 影の色１を設定する。
         *
         * @param shadow1Color 影の色１
         */
        public void setShadow1Color(Color shadow1Color) {
            this.shadow1Color = shadow1Color;
        }

        /**
         * ハイライトの色を取得する。
         *
         * @return ハイライトの色
         */
        public Color getHighlightColor() {
            return highlightColor;
        }

        /**
         * ハイライトの色を設定する。
         *
         * @param highlightColor ハイライトの色
         */
        public void setHighlightColor(Color highlightColor) {
            this.highlightColor = highlightColor;
        }

        /**
         * テーブルセルレンダリングを返します。
         *
         * @param table JTable
         * @param value セルに割り当てる値
         * @param isSelected セルが選択されている場合は true
         * @param hasFocus フォーカスがある場合は true
         * @param row 行
         * @param column 列
         * @return テーブルセルレンダリング
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            this.value = value;
            this.isSelected = isSelected;
            rRow = row;
            rCol = column;
            super.setForeground((isSelected ? table.getSelectionForeground() : table.getForeground()));

            return this;
        }

        /**
         * 数値かどうかを設定する。
         *
         * @param value 判定する値
         */
        private boolean isNumeric() {
            if (value instanceof Integer
                    || value instanceof Byte
                    || value instanceof Short
                    || value instanceof Long
                    || value instanceof Float
                    || value instanceof Double) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 小数かどうかを設定する。
         *
         * @param value 判定する値
         */
        private boolean isDecimal() {
            if (value instanceof Float
                    || value instanceof Double) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 描画処理を行う。
         *
         * @param g Graphics
         */
        public void paint(Graphics g) {
            //選択されている場合、凹んでいるように背景を描画する
            if (isSelected) {
                g.setColor(selectedRowColor);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.setColor(shadow0Color);
                g.drawLine(0, 0, 0, this.getHeight() - 1);
                g.drawLine(0, 0, this.getWidth() - 1, 0);
                g.setColor(shadow1Color);
                g.drawLine(1, 1, 1, this.getHeight() - 2);
                g.drawLine(1, 1, this.getWidth() - 2, 1);
                g.setColor(highlightColor);
                g.drawLine(this.getWidth() - 1, 1, this.getWidth() - 1, this.getHeight() - 1);
                g.drawLine(1, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1);
            } //選択されていない場合
            else {

                g.setColor(Color.white);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            switch (this.type) {
                case STAFF_TABLE:
                    if (rRow >= rowDel) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    if(rCol == 3) {
                        setForeground(Color.RED);
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }

                    break;

                case BED_TABLE:
                    if (rRow >= rowDel) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    if(rCol == 3) {
                        setForeground(Color.RED);
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }

                    break;
                case SHIFT_TABLE:
                    if (rRow >= rowDel) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    if(rCol == 2 || rCol == 4) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                        if(rCol == 4) {
                            setForeground(Color.RED);
                        }
                    }else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    break;
                default:
                    break;
            }

            g.setColor(Color.black);

            String temp = this.getText();

            if (this.isNumeric()) {
                if (this.isDecimal()) {
                    temp = String.format("%1$,.2f", value);
                } else {
                    temp = String.format("%1$,d", value);
                }
            }

            int baseX = 0;
            Rectangle2D r2d = this.getFont().getStringBounds(temp,
                    new FontRenderContext(new AffineTransform(), true, false));

            switch (this.getHorizontalAlignment()) {
                case SwingConstants.LEADING:
                    if (this.isNumeric()) {
                        baseX = this.getWidth() - ((Double) r2d.getWidth()).intValue() - SIDE_MARGIN;
                    } else if (isDateTime(this.getText()) || isPostalCode(this.getText())) {
                        baseX = (this.getWidth() - ((Double) r2d.getWidth()).intValue()) / 2;
                    } else {
                        baseX = SIDE_MARGIN;
                    }
                    break;
                case SwingConstants.LEFT:
                    baseX = SIDE_MARGIN;
                    break;
                case SwingConstants.CENTER:
                    baseX = (this.getWidth() - ((Double) r2d.getWidth()).intValue()) / 2;
                    break;
                case SwingConstants.RIGHT:
                    baseX = this.getWidth() - ((Double) r2d.getWidth()).intValue() - SIDE_MARGIN;
                    break;
            }

            int baseY = -1;

            switch (this.getVerticalAlignment()) {
                case SwingConstants.TOP:
                    baseY += this.getFont().getSize();
                    break;
                case SwingConstants.CENTER:
                    baseY += (this.getHeight() + this.getFont().getSize()) / 2;
                    break;
                case SwingConstants.BOTTOM:
                    baseY += this.getHeight();
                    break;
            }

            g.setColor(this.getForeground());

            g.drawString(temp, baseX + (isSelected ? 1 : 0), baseY + (isSelected ? 1 : 0));
        }

        private boolean isDateTime(String value) {
            return value.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")
                    || value.matches("[0-9]{4}/[0-9]{2}")
                    || value.matches("[0-9]{2}:[0-9]{2}");
        }

        private boolean isPostalCode(String value) {
            return value.matches("[0-9]{3}-[0-9]{4}");
        }

        /**
         * tableにSelectedTableCellRendererを設定します。
         *
         * @param table JTable
         */
        public void setSelectTableCellRenderer(JTable table) {
            table.setForeground(Color.black);

            table.setDefaultRenderer(Byte.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(Short.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(Integer.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(Long.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(Float.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(Double.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(String.class, new MediaTableCellRenderer(type, rowDel));
            table.setDefaultRenderer(Object.class, new MediaTableCellRenderer(type, rowDel));
        }

    }
}

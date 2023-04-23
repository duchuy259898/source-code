/*
 * SiteControlIntroductionPanel.java
 * [gb]かんざしAPI用管理画面構築（サイトコントロール導入画面）
 *
 * Copyright (c) 1993-1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.hair.master.company.MstAPIDetail;
import com.geobeck.sosia.pos.hair.master.company.MstKANZASHIMedia;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.management.RuntimeErrorException;
import javax.swing.JOptionPane;
import org.apache.commons.collections.map.LinkedMap;

/**
 * @author IVS_PTQUANG add 2016/09/05 New request #54177
 */
public final class SiteControlIntroductionPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    //IVS_PTQUANG add 2016/09/05 New request #54177
    private MstAPI mstApi = new MstAPI();
    //IVS_PTQUANG add 2016/09/05 New request #54177
    MstKANZASHIMedia mdia = new MstKANZASHIMedia();
    // set value check control status modify
    private int valCheckControl = 0;
    /**
     ****************************** CONSTANTS **********************************
     */
    // Varialble HttpResponse 0
    private final int CONSTANT_HTTP_RESPONSE = 0;
    // verify Shop
    private final int CONSTANT_VERIFY_SHOP_0 = 0;
    // Value verify of method verifyData if = 0 data exists else null data
    private final int CONSTANT_VERIFY_DATA_0 = 0;
    // pull status = 0
    private final int CONSTANT_PULL_STATUS_0 = 0;
    // verifyData = 0
    private final int CONSTANT_VERIFY_STATUS_0 = 0;
    // verifyData = 1
    private final int CONSTANT_VERIFY_STATUS_1 = 1;
    // pull status = 1
    private final int CONSTANT_PULL_STATUS_1 = 1;
    // verifyData Init Enable / Disable Control while Pull Status = 1
    private final int CONSTANT_VERIFY_STATUS_INIT = 2;
    //Create common Update = 2
    private final int CONSTANT_UPDATE_2 = 2;
    // pull status = 2
    private final int CONSTANT_PULL_STATUS_2 = 2;
    // verify Shop
    private final int CONSTANT_VERIFY_SHOP_2 = 2;
    //HttpResponse 2
    private final int CONSTANT_STATUS_2 = 2;
    // verify Shop
    private final int CONSTANT_VERIFY_SHOP_3 = 3;
    //HttpResponse 3
    private final int CONSTANT_STATUS_3 = 3;
    //HttpResponse 4
    private final int CONSTANT_STATUS_4 = 4;
    // pull status 7 Update value 4
    private final int CONSTANT_PULL_STATUS_7 = 4;
    //HttpResponse 5
    private final int CONSTANT_STATUS_5 = 5;
    // Value Beep for Runnable User_media
    private final int CONSTANT_VALUE_BEEP = 5;
    // Value Scheduled Thread Pool
    private final int CONSTANT_SCHEDULE_THREAD_POOL = 5;
    // Runnable User_media
    private final int CONSTANT_SCHEDULE_USR_MEDIA = 5;
    // Runnable Introduction
    private final int CONSTANT_SCHEDULE_INTRODUCTION = 5;
    //HttpResponse 6
    private final int CONSTANT_STATUS_6 = 6;
    //HttpResponse 7
    private final int CONSTANT_STATUS_7 = 7;
    //HttpResponse 8
    private final int CONSTANT_STATUS_8 = 8;
    //HttpResponse 9
    private final int CONSTANT_UPDATE_9 = 9;
    //HttpResponse 7
    private final int CONSTANT_UPDATE_7 = 4;
    // pull status
    private final int CONSTANT_PULL_STATUS_9 = 9;
    // pull status
    private final int CONSTANT_PUSH_STATUS_10 = 10;
    //HttpResponse 101
    private final int CONSTANT_STATUS_101 = 101;
    //HttpResponse 102
    private final int CONSTANT_STATUS_102 = 102;
    //HttpResponse 200
    private final int CONSTANT_STATUS_200 = 200;
    //HttpResponse 201
    private final int CONSTANT_STATUS_201 = 201;
    //HttpResponse 202
    private final int CONSTANT_STATUS_202 = 202;
    // scheduleAtFixedRate
    private final int CONSTANT_SCHEDULE_SALON = 300;
    // Inittial = 1
    private final int CONSTANT_INITIAL_DELAY = 1;
    //HttpResponse 403
    private final int CONSTANT_STATUS_403 = 403;
    //HttpResponse 409
    private final int CONSTANT_STATUS_409 = 409;
    //HttpResponse 500
    private final int CONSTANT_STATUS_500 = 500;
    //HttpResponse 599
    private final int CONSTANT_STATUS_599 = 599;
    // scheduleAtFixedRate
    private final int CONSTANT_TIME_END_SCHEDULE = 3600;
    //Flag UserFlag in DB 
    private final boolean CONSTANT_STATUS_USER_FLAG = true;
    //Flag Status Control F/T
    private final boolean CONSTANT_STATUS_CONTROL_F = false;
    //Flag Status Control F/T
    private final boolean CONSTANT_STATUS_CONTROL_T = true;
    // Message pull success feedback 11/10/2016 IVS_PTQUANG
    private static final String CONSTANT_MESSAGE_PULL_SUCCESS = "媒体情報の登録が完了しました。";
    // Message pull success feedback 11/10/2016 IVS_PTQUANG
    //private static final String CONSTANT_MESSAGE_PULL_SUCCESS_2 = "導入処理が完了しました。";
    // Message Clean pull
    private static final String CONSTANT_MESSAGE_CLEAN_PULL = "";
    // Message pull fail 101
    private static final String CONSTANT_MESSAGE_PULL_FAIL = "エラーが発生しました。管理者に連絡してください。";
    // Message fail 403
    private static final String CONSTANT_MESSAGE_HTTP_RESPONSE_403 = "ログインID、パスワード、お店IDが不正です。";
    // Message pulling
    private static final String CONSTANT_MESSAGE_PULLING = "処理中です。しばらくお待ちください。";
    // Message pulling status 7
    private static final String CONSTANT_MESSAGE_PULL_7 = "予約情報の取込みが完了しました。";
    // Message beep 5 seconds fail
    private static final String CONSTANT_MESSAGE_BEEP_PULLING_FAIL = "しばらく経ってから、もう一度処理してください。";
    // IVS_PTQUANG start add New request #57992 [gb]かんざしAPI用管理画面構築（サイトコントロール導入画面）_導入手順１の入力チェック追加
    // Message of UserID
    private static final String CONSTANT_MESSAGE_USER_ID = "ログインIDが入力されていません。";
    // Message of Password
    private static final String CONSTANT_MESSAGE_PASSWORD = "パスワードが入力されていません。";
    // Message of StoreID
    private static final String CONSTANT_MESSAGE_STORE_ID = "お店IDが入力されていません。";
    // IVS_PTQUANG end add New request #57992 [gb]かんざしAPI用管理画面構築（サイトコントロール導入画面）_導入手順１の入力チェック追加
    /**
     *************************** END CONSTANTS *********************************
     */
    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177 Constructor
     *
     * @param control
     * @see Use call method checkVisibleForMenu check visible menu inside
     * MainFrame.java, Do not Timer in Constructor none parameter
     */
    public SiteControlIntroductionPanel(int control) {

        initComponents();
        try {
            if (SystemInfo.getCurrentShop().getShopID() == CONSTANT_VERIFY_SHOP_0) {
                SystemInfo.initGroupShopComponents(cmbShop, CONSTANT_VERIFY_SHOP_2);
            } else {
                SystemInfo.initGroupShopComponents(cmbShop, CONSTANT_VERIFY_SHOP_3);
            }
            if (SystemInfo.getUserAPI() != CONSTANT_VERIFY_STATUS_0) {
                mstApi.setApiID(SystemInfo.getUserAPI());
                ConnectionWrapper con = SystemInfo.getBaseConnection();
                if ((con.isClosed()) && (con == null)) {
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                } else {
                    mstApi.loadDetail(con);
                    for (MstAPIDetail detail : mstApi) {
                        cmbMedia.addItem(detail);
                    }
                }

                // Get connection DB
                ConnectionWrapper connMedia = SystemInfo.getConnection();
                if ((connMedia.isClosed()) && (connMedia == null)) {
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                } else {
                    this.load(connMedia);
                }
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (NullPointerException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177 Constructor
     */
    public SiteControlIntroductionPanel() {
        super();
        initComponents();
        this.setSize(840, 700);
        this.setPath("基本設定＞ｻｲﾄｺﾝﾄﾛｰﾙ導入");
        this.setTitle("ｻｲﾄｺﾝﾄﾛｰﾙ導入");
        try {
            if (SystemInfo.getCurrentShop().getShopID() == CONSTANT_VERIFY_SHOP_0) {
                SystemInfo.initGroupShopComponents(cmbShop, CONSTANT_VERIFY_SHOP_2);
            } else {
                SystemInfo.initGroupShopComponents(cmbShop, CONSTANT_VERIFY_SHOP_3);
            }
            this.initMedia();
        } catch (NullPointerException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Config load data first display Form
     */
    private void initMedia() {

        try {
            if (SystemInfo.getUserAPI() != CONSTANT_VERIFY_STATUS_0) {

                mstApi.setApiID(SystemInfo.getUserAPI());
                ConnectionWrapper con = SystemInfo.getBaseConnection();
                if ((con.isClosed()) && (con == null)) {
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                } else {
                    mstApi.loadDetail(con);
                    for (MstAPIDetail detail : mstApi) {
                        cmbMedia.addItem(detail);
                    }
                }
                // Get connection DB
                ConnectionWrapper connMedia = SystemInfo.getConnection();
                if ((connMedia.isClosed()) && (connMedia == null)) {
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                } else {
                    this.load(connMedia);
                }
                // Check Pull Status in Database == 2 Display Message
                Integer pullStatus = mdia.getPullStatus();
                lblSetDate.setText(fixValueDateFirstOfMonth());
                if (null != pullStatus) {
                    switch (pullStatus) {
                        // Pull status = 0 enable all control
                        case CONSTANT_PULL_STATUS_0:
                            verifyData(CONSTANT_VERIFY_STATUS_0);
                            lblMessage.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            break;
                        // Pull status = 1 enable button
                        case CONSTANT_PULL_STATUS_1:
                            this.executePullSalon(CONSTANT_VERIFY_STATUS_INIT);
                            verifyData(CONSTANT_VERIFY_STATUS_0);
                            lblMessage.setText(CONSTANT_MESSAGE_PULLING);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            break;
                        // Pull status = 2 end process
                        case CONSTANT_PULL_STATUS_2:
                            //lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS_2);
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            verifyData(CONSTANT_VERIFY_STATUS_0);
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            con.close();
                            System.gc();
                            break;
                        // Pull status = 2 end process
                        case CONSTANT_STATUS_3:
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            this.executePullSalon(CONSTANT_STATUS_3);
                            verifyData(CONSTANT_VERIFY_STATUS_0);
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            con.close();
                            System.gc();
                            break;
                        // Pull status = 7 end process
                        case CONSTANT_PULL_STATUS_7:
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                            verifyData(CONSTANT_VERIFY_STATUS_0);
                            statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_PULL_STATUS_7);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            con.close();
                            System.gc();
                            break;
                        // Pull status = 9 pull fail
                        case CONSTANT_PULL_STATUS_9:
                            this.executePullSalon(CONSTANT_VERIFY_DATA_0);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            registMediaButton.setEnabled(CONSTANT_STATUS_CONTROL_T);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            break;
                        // Pull status = 10 pull fail
                        case CONSTANT_PUSH_STATUS_10:
                            this.executePullSalon(CONSTANT_PUSH_STATUS_10);
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                            System.out.println("Load initMedia:" + " " + pullStatus);
                            break;
                        default: {
                            statusControlInit(CONSTANT_STATUS_CONTROL_F, valCheckControl);
                            break;
                        }
                    }
                } // Pull Status in DB = null set default enable control
                else {
                    lblMessage.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                    System.out.println("End");
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (NullPointerException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        targetLabel3 = new javax.swing.JLabel();
        cmbMedia = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        targetLabel9 = new javax.swing.JLabel();
        lbPW1 = new javax.swing.JLabel();
        registMediaButton = new javax.swing.JButton();
        txtStoreID = new com.geobeck.swing.JFormattedTextFieldEx();
        txtID = new com.geobeck.swing.JFormattedTextFieldEx();
        txtPW = new javax.swing.JPasswordField();
        lbPW = new javax.swing.JLabel();
        lbID = new javax.swing.JLabel();
        targetLabel19 = new javax.swing.JLabel();
        targetLabel = new javax.swing.JLabel();
        cmbShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblMessage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanelPush = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        lblSetDate = new javax.swing.JLabel();
        registMediaButton1 = new javax.swing.JButton();
        lblMessage1 = new javax.swing.JLabel();

        targetLabel3.setText("媒体");

        cmbMedia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMediaActionPerformed(evt);
            }
        });

        targetLabel9.setText("<html>※媒体～お店IDを入力後、登録ボタンを押下してください。");

        lbPW1.setText("お店ID");

        registMediaButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registMediaButton.setBorderPainted(false);
        registMediaButton.setContentAreaFilled(false);
        registMediaButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registMediaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registMediaButtonActionPerformed(evt);
            }
        });

        txtStoreID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtStoreID.setColumns(15);

        txtID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtID.setColumns(15);

        lbPW.setText("パスワード");

        lbID.setText("ログインID");

        targetLabel19.setText("処理状況が処理完了になるまでしばらくお待ちください。");

        targetLabel.setText("店舗");

        cmbShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShopActionPerformed(evt);
            }
        });

        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel2.setText("・       導入手順１ 媒体情報の登録");
        jLabel2.setToolTipText("");

        jPanelPush.setBackground(new java.awt.Color(235, 235, 235));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("・       導入手順２ 情報の紐付け");
        jLabel6.setToolTipText("");

        jLabel1.setText("サイトコントロール設定」メニューから設定画面を表示し、");

        jLabel3.setText("スタッフ、施術台、シフトパターンの紐付けを実施してください。");

        jLabel4.setText("・       導入手順３予約情報の取込み");

        registMediaButton1.setIcon(SystemInfo.getImageIcon("/button/reservation/regist_start_off.jpg"));
        registMediaButton1.setBorderPainted(false);
        registMediaButton1.setContentAreaFilled(false);
        registMediaButton1.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/regist_start_off.jpg"));
        registMediaButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registMediaButton1ActionPerformed(evt);
            }
        });

        lblMessage1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelPushLayout = new javax.swing.GroupLayout(jPanelPush);
        jPanelPush.setLayout(jPanelPushLayout);
        jPanelPushLayout.setHorizontalGroup(
            jPanelPushLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPushLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPushLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanelPushLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanelPushLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)))
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPushLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanelPushLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanelPushLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(lblSetDate)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPushLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelPushLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPushLayout.createSequentialGroup()
                        .addComponent(registMediaButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(173, 173, 173))
                    .addComponent(lblMessage1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanelPushLayout.setVerticalGroup(
            jPanelPushLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPushLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(29, 29, 29)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(lblSetDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(registMediaButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblMessage1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lbPW, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPW, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lbID, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbPW1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtStoreID, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(targetLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(targetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbMedia, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(targetLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(targetLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(registMediaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanelPush, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(targetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(targetLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMedia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbID, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPW, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPW, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPW1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStoreID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(targetLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(targetLabel19))
                    .addComponent(registMediaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelPush, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbMediaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMediaActionPerformed

    }//GEN-LAST:event_cmbMediaActionPerformed

    private void registMediaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registMediaButtonActionPerformed

        registMediaButton.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //IVS_PTQUANG start 2016/09/05 New request #54177
            ConnectionWrapper conn_media = SystemInfo.getConnection();
            //Load DB
            this.load(conn_media);
                boolean empInput = checkEmpty();
                if (!empInput) {
                    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CONSTANT_SCHEDULE_THREAD_POOL);
                    executorService.execute(new callMethodRunnable());
                    executorService.shutdown();
                    System.out.println("registMediaButtonActionPerformed");
                }

        } catch (RuntimeErrorException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (RejectedExecutionException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_registMediaButtonActionPerformed

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @throws Exception
     * @see Response 500 ~ 599 call Runnable User_Media Create New
     */
    private void statusUserMediaCreateNew() {
        try {
            // Insert data while check null data in DB
            getInsertSQL();
            // Introduction Pull method
            int checkIntroduction = pullIntroduction();
            // Response status Pull
            if (checkIntroduction == CONSTANT_STATUS_202) {
                // Start Pull SALON
                int checkSalon = pullSalon();
                System.out.println("statusUserMediaCreateNew:" + " " + checkSalon);
                if (checkSalon != CONSTANT_STATUS_200) {
                    if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                        TimerControllerSalon timer = new TimerControllerSalon();
                        timer.beepSalon();
                        statusControl(CONSTANT_STATUS_CONTROL_F);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);

                    } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_101) {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                        getUpdateSQL(CONSTANT_UPDATE_9);
                        statusControl(CONSTANT_STATUS_CONTROL_T);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                    } else if (checkSalon == CONSTANT_STATUS_102) {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                        //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                        statusControl(CONSTANT_STATUS_CONTROL_F);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                    } else if (checkSalon == CONSTANT_STATUS_2) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_3) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_5) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else if (checkSalon == CONSTANT_STATUS_6) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    }
                }
                // End Pull SALON
            } else if ((checkIntroduction >= CONSTANT_STATUS_500) && (checkIntroduction <= CONSTANT_STATUS_599)) {

                statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                TimerControllerIntroduction timer = new TimerControllerIntroduction();
                timer.beepIntroduction();

            } else {
                lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                // Enable control
                statusControl(CONSTANT_STATUS_CONTROL_F);
            }
            //End Pull INTRODUCTION HTTP = 200
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @throws Exception
     * @see Response 500 ~ 599 call Runnable User_Media exists
     */
    private void statusUserMediaExists() {
        try {
            // verifyData
            if (!verifyData(CONSTANT_VERIFY_STATUS_1)) {
                statusControl(CONSTANT_STATUS_CONTROL_F);
                // Insert data while check null data in DB
                getInsertSQL();
                // Start Pull SALON
                int checkSalon = pullSalon();
                 System.out.println("statusUserMediaExists:" + " " + checkSalon);
                if (checkSalon != CONSTANT_STATUS_200) {
                    if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                        TimerControllerSalon timer = new TimerControllerSalon();
                        timer.beepSalon();
                        statusControl(CONSTANT_STATUS_CONTROL_T);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                    } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_101) {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                        getUpdateSQL(CONSTANT_UPDATE_9);
                        //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                        statusControl(CONSTANT_STATUS_CONTROL_T);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                    } else if (checkSalon == CONSTANT_STATUS_102) {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                        //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                         statusControl(CONSTANT_STATUS_CONTROL_F);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                    } else if (checkSalon == CONSTANT_STATUS_2) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_3) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_5) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else if (checkSalon == CONSTANT_STATUS_6) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    }
                }
                // End Pull SALON HTTP = 200
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @throws Exception
     * @see Response 500 ~ 599 call RunnableIntroduction
     */
    private void statusIntroductionResponseOK() {
        // Start Pull SALON
        try {

            int checkSalon = pullSalon();
            System.out.println("statusIntroductionResponseOK:" + " " + checkSalon);
            if (checkSalon != CONSTANT_STATUS_200) {
                if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                    TimerControllerSalon timer = new TimerControllerSalon();
                    timer.beepSalon();
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                    this.executePullSalon(CONSTANT_PULL_STATUS_1);
                } else if (checkSalon == CONSTANT_STATUS_101) {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                    getUpdateSQL(CONSTANT_UPDATE_9);
                    //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                } else if (checkSalon == CONSTANT_STATUS_102) {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                    //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                } else if (checkSalon == CONSTANT_STATUS_2) {
                    this.executePullSalon(CONSTANT_PULL_STATUS_1);
                } else if (checkSalon == CONSTANT_STATUS_3) {
                    this.executePullSalon(CONSTANT_PULL_STATUS_1);
                } else if (checkSalon == CONSTANT_STATUS_5) {
                    this.executePullSalon(CONSTANT_STATUS_5);
                } else if (checkSalon == CONSTANT_STATUS_6) {
                    this.executePullSalon(CONSTANT_STATUS_5);
                } else {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        // End Pull SALON
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param status
     * @throws Exception
     * @see Response 500 ~ 599 call Runnable User_Media
     */
    private void callMethodStatusUserMedia(int status) throws Exception {

        switch (status) {
            case CONSTANT_STATUS_201:
                statusUserMediaCreateNew();
                break;
            case CONSTANT_STATUS_409:
                statusUserMediaExists();
                break;
            case CONSTANT_STATUS_403:
                lblMessage.setText(CONSTANT_MESSAGE_HTTP_RESPONSE_403);
                // Enable control
                statusControl(CONSTANT_STATUS_CONTROL_T);
                break;
            default:
                break;
        }
        // End Push MEDIA
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param status
     * @throws Exception
     * @see Response 500 ~ 599 call Runnable Introduction
     */
    private void callMethodStatusIntroduction(int status) throws Exception {

        switch (status) {
            case CONSTANT_STATUS_202:
                statusIntroductionResponseOK();
                break;
            default:
                break;
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @method callMethod
     * @throws SQLException
     */
    private void callMethod() throws Exception {

        // Start Push MEDIA
        int idMedia = pushMedia();
        if (idMedia == CONSTANT_STATUS_201) {
            // Insert data while check null data in DB
            getInsertSQL();
            // Introduction Pull method
            int checkIntroduction = pullIntroduction();
            System.out.println("callMethod > checkIntroduction:" + " " + checkIntroduction);
            // Response status Pull
            if (checkIntroduction == CONSTANT_STATUS_202) {
                // Start Pull SALON
                int checkSalon = pullSalon();
                System.out.println("callMethod > checkSalon:" + " " + checkSalon);
                if (checkSalon != CONSTANT_STATUS_200) {
                    if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                        TimerControllerSalon timer = new TimerControllerSalon();
                        timer.beepSalon();
                        statusControl(CONSTANT_STATUS_CONTROL_F);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                    } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_101) {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                        getUpdateSQL(CONSTANT_UPDATE_9);
                        //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                        statusControl(CONSTANT_STATUS_CONTROL_T);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                    } else if (checkSalon == CONSTANT_STATUS_102) {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                        //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                        statusControl(CONSTANT_STATUS_CONTROL_F);
                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                    } else if (checkSalon == CONSTANT_STATUS_2) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_3) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_5) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else if (checkSalon == CONSTANT_STATUS_6) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    }
                }
                // End Pull SALON
            } else if ((checkIntroduction >= CONSTANT_STATUS_500) && (checkIntroduction <= CONSTANT_STATUS_599)) {
                TimerControllerIntroduction timer = new TimerControllerIntroduction();
                timer.beepIntroduction();
            } else {
                lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                // Enable control
                statusControl(CONSTANT_STATUS_CONTROL_T);
            }
            //End Pull INTRODUCTION HTTP = 200
        } else if (idMedia == CONSTANT_STATUS_409) {
            System.out.println("CONSTANT_STATUS_409");
            
            statusControl(CONSTANT_STATUS_CONTROL_F);
            // verifyData
            if (!verifyData(CONSTANT_VERIFY_STATUS_1)) {
                // Insert data while check null data in DB
                getInsertSQL();
                // Introduction Pull method
                int checkIntroduction = pullIntroduction();
                System.out.println("callMethod > checkIntroduction:" + " " + checkIntroduction);
                // Response status Pull
                if (checkIntroduction == CONSTANT_STATUS_202) {
                    // Start Pull SALON
                    int checkSalon = pullSalon();
                    System.out.println("callMethod > checkSalon:" + " " + checkSalon);
                    if (checkSalon != CONSTANT_STATUS_200) {
                        if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                            TimerControllerSalon timer = new TimerControllerSalon();
                            timer.beepSalon();
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                        } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                            this.executePullSalon(CONSTANT_PULL_STATUS_1);
                        } else if (checkSalon == CONSTANT_STATUS_101) {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            getUpdateSQL(CONSTANT_UPDATE_9);
                            //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                        } else if (checkSalon == CONSTANT_STATUS_102) {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                            getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                            //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                        } else if (checkSalon == CONSTANT_STATUS_2) {
                            this.executePullSalon(CONSTANT_PULL_STATUS_1);
                        } else if (checkSalon == CONSTANT_STATUS_3) {
                            this.executePullSalon(CONSTANT_PULL_STATUS_1);
                        } else if (checkSalon == CONSTANT_STATUS_5) {
                            this.executePullSalon(CONSTANT_STATUS_5);
                        } else if (checkSalon == CONSTANT_STATUS_6) {
                            this.executePullSalon(CONSTANT_STATUS_5);
                        } else {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        }
                    }
                    // End Pull SALON
                } else if ((checkIntroduction >= CONSTANT_STATUS_500) && (checkIntroduction <= CONSTANT_STATUS_599)) {
                    TimerControllerIntroduction timer = new TimerControllerIntroduction();
                    timer.beepIntroduction();
                } else {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    // Enable control
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                }
            } else {
                
                getUpdateSQL(CONSTANT_PULL_STATUS_1);
                // Introduction Pull method
                int checkIntroduction = pullIntroduction();
                System.out.println("callMethod > checkIntroduction:" + " " + checkIntroduction);
                // Response status Pull
                if (checkIntroduction == CONSTANT_STATUS_202) {
                    // Start Pull SALON
                    int checkSalon = pullSalon();
                    System.out.println("callMethod > checkSalon:" + " " + checkSalon);
                    if (checkSalon != CONSTANT_STATUS_200) {
                        if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                            TimerControllerSalon timer = new TimerControllerSalon();
                            timer.beepSalon();
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                        } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                            this.executePullSalon(CONSTANT_PULL_STATUS_1);
                        } else if (checkSalon == CONSTANT_STATUS_101) {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            getUpdateSQL(CONSTANT_UPDATE_9);
                            //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                        } else if (checkSalon == CONSTANT_STATUS_102) {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                            getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                            //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                        } else if (checkSalon == CONSTANT_STATUS_2) {
                            this.executePullSalon(CONSTANT_PULL_STATUS_1);
                        } else if (checkSalon == CONSTANT_STATUS_3) {
                            this.executePullSalon(CONSTANT_PULL_STATUS_1);
                        } else if (checkSalon == CONSTANT_STATUS_5) {
                            this.executePullSalon(CONSTANT_STATUS_5);
                        } else if (checkSalon == CONSTANT_STATUS_6) {
                            this.executePullSalon(CONSTANT_STATUS_5);
                        } else {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                        }
                    }
                    // End Pull SALON
                } else if ((checkIntroduction >= CONSTANT_STATUS_500) && (checkIntroduction <= CONSTANT_STATUS_599)) {
                    TimerControllerIntroduction timer = new TimerControllerIntroduction();
                    timer.beepIntroduction();
                } else {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    // Enable control
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                }
            }
            
        } else if (idMedia == CONSTANT_STATUS_403) {
            lblMessage.setText(CONSTANT_MESSAGE_HTTP_RESPONSE_403);
            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
            // Enable control
            statusControl(CONSTANT_STATUS_CONTROL_T);
        } else if ((idMedia >= CONSTANT_STATUS_500) && (idMedia <= CONSTANT_STATUS_599)) {
            TimerControllerUserMedia timer = new TimerControllerUserMedia();
            timer.beepUserMedia();
            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
        } else {
            lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
            // Enable control
            statusControl(CONSTANT_STATUS_CONTROL_T);
        }

        // End Push MEDIA
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     * @see Runnable
     */
    class callMethodRunnable implements Runnable {

        private volatile boolean shutdown;

        @Override
        public void run() {
            if (!shutdown) {
                // Disable control
                statusControl(CONSTANT_STATUS_CONTROL_F);
                statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                // call text message first
                lblMessage.setText(CONSTANT_MESSAGE_PULLING);
                lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                try {
                    //Call API
                    callMethod();
                } catch (RuntimeException e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                } catch (InterruptedException e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                } catch (Exception e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                }
                Thread.currentThread().interrupt();
            }
        }

        /**
         * shutdown
         */
        public void shutdown() {
            shutdown = true;
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     * @see Runnable PullSalon
     */
    class callSalonPush implements Runnable {

        private volatile boolean shutdown;

        @Override
        public void run() {
            if (!shutdown) {
                // Disable control
                statusControl(CONSTANT_STATUS_CONTROL_F);
                lblMessage.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                lblMessage1.setText(CONSTANT_MESSAGE_PULLING);
                try {
                    TimerSalonMain time = new TimerSalonMain();
                    time.beepSalon();
                } catch (RuntimeException e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                } catch (Exception e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                }
                Thread.currentThread().interrupt();
            }
        }

        /**
         * shutdown
         */
        public void shutdown() {
            shutdown = true;
        }
    }
    
    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     * @see Runnable PullSalon
     */
    class callMethodMainRunnable implements Runnable {

        private volatile boolean shutdown;

        @Override
        public void run() {
            if (!shutdown) {
                // Disable control
                statusControl(CONSTANT_STATUS_CONTROL_F);
                lblMessage.setText(CONSTANT_MESSAGE_PULLING);
                lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                try {
                    TimerSalonMain time = new TimerSalonMain();
                    time.beepSalon();
                } catch (RuntimeException e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                } catch (Exception e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                }
                Thread.currentThread().interrupt();
            }
        }

        /**
         * shutdown
         */
        public void shutdown() {
            shutdown = true;
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     * @see Runnable, ScheduledExecutorService UserMedia
     */
    class TimerControllerUserMedia {

        private int iBeepUserMedia = 0;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CONSTANT_SCHEDULE_THREAD_POOL);

        /**
         * beepUserMedia
         */
        public void beepUserMedia() {
            final Runnable beeper = new Runnable() {
                @Override
                public void run() {
                    try {
                        iBeepUserMedia++;
                        if (iBeepUserMedia <= CONSTANT_VALUE_BEEP) {
                            int httpResponseUserMedia = pushMedia();
                            SystemInfo.getLogger().log(java.util.logging.Level.INFO, "Timer Call Push User_Media " +" Status: "+ httpResponseUserMedia);
                            System.out.println("TimerControllerUserMedia: " + " " + httpResponseUserMedia);
                            if ((httpResponseUserMedia <= CONSTANT_STATUS_500) || (httpResponseUserMedia >= CONSTANT_STATUS_599)) {
                                callMethodStatusUserMedia(httpResponseUserMedia);
                            }

                        } else {
                            lblMessage.setText(CONSTANT_MESSAGE_BEEP_PULLING_FAIL);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            scheduler.shutdown();
                        }

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
            final ScheduledFuture<?> beeperHandle
                    = scheduler.scheduleWithFixedDelay(beeper, CONSTANT_INITIAL_DELAY, CONSTANT_SCHEDULE_USR_MEDIA, SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, CONSTANT_TIME_END_SCHEDULE * CONSTANT_TIME_END_SCHEDULE, SECONDS);
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     */
    class TimerControllerIntroduction {

        private int iBeepIntroduction = 0;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CONSTANT_SCHEDULE_THREAD_POOL);

        /**
         * beepIntroduction
         */
        public void beepIntroduction() {
            final Runnable beeper = new Runnable() {
                
                @Override
                public void run() {
                
                    try {
                        iBeepIntroduction++;
                        if (iBeepIntroduction <= CONSTANT_VALUE_BEEP) {
                            int statusPullIntroduction = pullIntroduction();
                            SystemInfo.getLogger().log(java.util.logging.Level.INFO, "Timer Call Pull Introduction " +" Status: "+ statusPullIntroduction);
                            System.out.println("TimerControllerIntroduction: " + " " + statusPullIntroduction);
                            if ((statusPullIntroduction <= CONSTANT_STATUS_500) || (statusPullIntroduction >= CONSTANT_STATUS_599)) {
                                callMethodStatusIntroduction(statusPullIntroduction);
                            }

                        } else {
                            lblMessage.setText(CONSTANT_MESSAGE_BEEP_PULLING_FAIL);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            scheduler.shutdown();
                        }

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
            final ScheduledFuture<?> beeperHandle
                    = scheduler.scheduleWithFixedDelay(beeper, CONSTANT_INITIAL_DELAY, CONSTANT_SCHEDULE_INTRODUCTION, SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, CONSTANT_TIME_END_SCHEDULE * CONSTANT_TIME_END_SCHEDULE, SECONDS);
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177 method
     * TimerSalonMain
     */
    class TimerSalonMain {

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CONSTANT_SCHEDULE_THREAD_POOL);

        /**
         * beepSalon
         */
        public void beepSalon() {
            //Timer 5 minute
            final Runnable beeper = new Runnable() {
                @Override
                public void run() {
                    try {
                        //System.out.println("RUN SALON-----------------------------------------------------------------");
                        boolean checkExistsDb = checkStatusDb();
                        if (checkExistsDb) {
                            int pullSalon = pullSalon();
                            SystemInfo.getLogger().log(java.util.logging.Level.INFO, "Timer Call Pull Salon" +" Status: "+ pullSalon);
                            System.out.println("TimerSalonMain: " + " " + pullSalon);
                            if (pullSalon != CONSTANT_STATUS_200) {
                                switch (pullSalon) {
                                    case CONSTANT_STATUS_7:
                                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                                        getUpdateSQL(CONSTANT_UPDATE_7);
                                        statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                                        scheduler.shutdown();
                                        break;
                                    case CONSTANT_STATUS_8:
                                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                        getUpdateSQL(CONSTANT_UPDATE_7);
                                        statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                                        scheduler.shutdown();
                                        break;
                                    case CONSTANT_STATUS_4:
                                        lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                        getUpdateSQL(CONSTANT_UPDATE_2);
                                        statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_4);
                                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                                        scheduler.shutdown();
                                        break;
                                    case CONSTANT_STATUS_101:
                                        lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                                        lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                                        statusControl(CONSTANT_STATUS_CONTROL_T);
                                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                                        scheduler.shutdown();
                                        break;
                                    case CONSTANT_STATUS_102:
                                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                                        statusControl(CONSTANT_STATUS_CONTROL_F);
                                        statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                                        scheduler.shutdown();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                    } catch (RejectedExecutionException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (NullPointerException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (IllegalArgumentException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (SQLException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (Exception e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    }
                    SystemInfo.getLogger().log(java.util.logging.Level.INFO, "end method:beepSalon class: TimerControllerIntroduction");

                }
            };
            final ScheduledFuture<?> beeperHandle
                    = scheduler.scheduleWithFixedDelay(beeper, CONSTANT_INITIAL_DELAY, CONSTANT_SCHEDULE_SALON, SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, CONSTANT_TIME_END_SCHEDULE * CONSTANT_TIME_END_SCHEDULE, SECONDS);
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     */
    class TimerControllerSalon {

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CONSTANT_SCHEDULE_THREAD_POOL);

        /**
         * beepSalon
         */
        public void beepSalon() {
            // Timer 5 minute
            final Runnable beeper = new Runnable() {
                @Override
                public void run() {
                    try {
                        int pullSalon = pullSalon();
                        SystemInfo.getLogger().log(java.util.logging.Level.INFO, "Timer Call Pull Salon" +" Status: "+ pullSalon);
                        System.out.println("TimerControllerSalon: " + " " + pullSalon);
                        if (pullSalon != CONSTANT_STATUS_200) {
                            switch (pullSalon) {
                                case CONSTANT_STATUS_7:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                                    getUpdateSQL(CONSTANT_UPDATE_7);
                                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_8:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                                    getUpdateSQL(CONSTANT_UPDATE_7);
                                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_4:
                                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    getUpdateSQL(CONSTANT_UPDATE_2);
                                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_4);
                                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_101:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                                    statusControl(CONSTANT_STATUS_CONTROL_T);
                                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_102:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                                    statusControl(CONSTANT_STATUS_CONTROL_F);
                                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                                    scheduler.shutdown();
                                    break;
                                default:
                                    break;
                            }
                        }
                    } catch (RejectedExecutionException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (NullPointerException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (IllegalArgumentException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (SQLException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (Exception e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    }
                }
            };
            final ScheduledFuture<?> beeperHandle
                    = scheduler.scheduleWithFixedDelay(beeper,CONSTANT_INITIAL_DELAY, CONSTANT_SCHEDULE_SALON, SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, CONSTANT_TIME_END_SCHEDULE * CONSTANT_TIME_END_SCHEDULE, SECONDS);
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     */
    class TimerFiveSecondsIntroductionPush {

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CONSTANT_SCHEDULE_THREAD_POOL);

        /**
         * beepSalon
         */
        public void beepSalon() {
            // Timer 5 minute
            final Runnable beeper = new Runnable() {
                @Override
                public void run() {
                    
                    try {
                        // Start Pull SALON
                        int checkSalon = pullSalon();
                        SystemInfo.getLogger().log(java.util.logging.Level.INFO, "Timer Call Push Introduction " + "Status: " + checkSalon);
                        System.out.println(" TimerFiveSecondsIntroductionPush check Salon:" + " " + checkSalon);
                        if (checkSalon != CONSTANT_STATUS_200) {
                            switch (checkSalon) {
                                case CONSTANT_STATUS_7:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                                    getUpdateSQL(CONSTANT_UPDATE_7);
                                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_8:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                                    getUpdateSQL(CONSTANT_UPDATE_7);
                                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_4:
                                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    getUpdateSQL(CONSTANT_UPDATE_2);
                                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_4);
                                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_101:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                                    statusControl(CONSTANT_STATUS_CONTROL_T);
                                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                                    scheduler.shutdown();
                                    break;
                                case CONSTANT_STATUS_102:
                                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                                    statusControl(CONSTANT_STATUS_CONTROL_F);
                                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                                    scheduler.shutdown();
                                    break;
                                default:
                                    break;
                            }
                        }
                        // End Pull SALON
                    } catch (RejectedExecutionException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (NullPointerException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (IllegalArgumentException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (RuntimeErrorException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (Exception e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    }
                }
            };
            final ScheduledFuture<?> beeperHandle
                    = scheduler.scheduleWithFixedDelay(beeper, CONSTANT_INITIAL_DELAY, CONSTANT_SCHEDULE_SALON , SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, CONSTANT_TIME_END_SCHEDULE * CONSTANT_TIME_END_SCHEDULE, SECONDS);
        }
    }


    private void cmbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopActionPerformed

    }//GEN-LAST:event_cmbShopActionPerformed

    private void registMediaButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registMediaButton1ActionPerformed

        // Scheduled Executor Push Introduction while pull status = 2
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CONSTANT_SCHEDULE_THREAD_POOL);
        executorService.execute(new callRunnablePushIntroduction());
        executorService.shutdown();
        System.out.println("Click Button 2");
    }//GEN-LAST:event_registMediaButton1ActionPerformed

    // REGION PUSH INTRODUCTION feedback #54177 2016/10/11
    private int pushIntroduction() throws SQLException {
        int httpResult = CONSTANT_HTTP_RESPONSE;
        try {
            String firstDay = getFirstDay();
            Gson gson = new Gson();
            String url = mstApi.getApiUrl().trim() + "/salon/" + mstApi.getSalonID() + "/introduction/push";
            System.out.println("url" + "" + url);
            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.reservation+json");
            con.setRequestProperty("Authorization", mstApi.getApiAuthCode());
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            Map mapParam = new LinkedMap();
            mapParam.put("base_date", firstDay);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(gson.toJson(mapParam));
            writer.flush();
            httpResult = con.getResponseCode();
            System.out.println("Status Push Intruduction: " + " " + httpResult);

        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return httpResult;
    }

    
    /**
     * 
     */
    class callRunnablePush implements Runnable {

        private volatile boolean shutdown;

        @Override
        public void run() {
            if (!shutdown) {

                // Disable control
                statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                // call text message first
                lblMessage1.setText(CONSTANT_MESSAGE_PULLING);

                try {
                    //Call API
                    //getStatusPushIntroduction();
                    TimerSalonMain time = new TimerSalonMain();
                    time.beepSalon();
                    System.out.println("callRunnablePushIntroduction");
                } catch (RuntimeException e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                } catch (Exception e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                }
                Thread.currentThread().interrupt();
            }
        }

        /**
         * shutdown
         */
        public void shutdown() {
            shutdown = true;
        }
    }
    
    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     * @see Runnable
     */
    class callRunnablePushIntroduction implements Runnable {

        private volatile boolean shutdown;

        @Override
        public void run() {
            if (!shutdown) {

                // Disable control
                statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                // call text message first
                lblMessage1.setText(CONSTANT_MESSAGE_PULLING);

                try {
                    //Call API
                    getStatusPushIntroduction();
                    System.out.println("callRunnablePushIntroduction");
                } catch (RuntimeException e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                } catch (Exception e) {
                    SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                }
                Thread.currentThread().interrupt();
            }
        }

        /**
         * shutdown
         */
        public void shutdown() {
            shutdown = true;
        }
    }

    /**
     * @author IVS_PTQUANG add 2016/09/05 New request #54177
     */
    class TimerControllerIntroductionPush {

        private int iBeepIntroduction = 0;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CONSTANT_SCHEDULE_THREAD_POOL);

        /**
         * beepIntroduction
         */
        public void beepIntroduction() {
            final Runnable beeper = new Runnable() {
                @Override
                public void run() {

                    try {
                        iBeepIntroduction++;
                        if (iBeepIntroduction <= CONSTANT_VALUE_BEEP) {
                            int statusPullIntroduction = pushIntroduction();
                            System.out.println("TimerControllerIntroductionPush: " + " " + statusPullIntroduction);
                            if ((statusPullIntroduction <= CONSTANT_STATUS_500) || (statusPullIntroduction >= CONSTANT_STATUS_599)) {
                                callMethodStatusIntroductionPush(statusPullIntroduction);
                            }

                        } else {
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_BEEP_PULLING_FAIL);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            scheduler.shutdown();
                        }

                    } catch (RejectedExecutionException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (NullPointerException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (IllegalArgumentException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (RuntimeErrorException e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    } catch (Exception e) {
                        SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
                    }
                }
            };
            final ScheduledFuture<?> beeperHandle
                    = scheduler.scheduleWithFixedDelay(beeper,CONSTANT_INITIAL_DELAY, CONSTANT_SCHEDULE_INTRODUCTION, SECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, CONSTANT_TIME_END_SCHEDULE * CONSTANT_TIME_END_SCHEDULE, SECONDS);
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param status
     * @throws Exception
     * @see Response 500 ~ 599 call Runnable Introduction
     */
    private void callMethodStatusIntroductionPush(int status) throws Exception {

        switch (status) {
            case CONSTANT_STATUS_202:
                pushIntroductionResponseOK();
                break;
            default:
                break;
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @throws Exception
     * @see Response 500 ~ 599 call RunnableIntroduction
     */
    private void pushIntroductionResponseOK() {
        // Start Pull SALON
        try {

            int checkSalon = pullSalon();
            System.out.println("pushIntroductionResponseOK: " + " " + checkSalon);
            if (checkSalon != CONSTANT_STATUS_200) {
                if ((checkSalon >= CONSTANT_STATUS_500) && (checkSalon <= CONSTANT_STATUS_599)) {
                    TimerControllerSalon timer = new TimerControllerSalon();
                    timer.beepSalon();
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                } else if (checkSalon == CONSTANT_PULL_STATUS_1) {
                    this.executePullSalon(CONSTANT_PULL_STATUS_1);
                } else if (checkSalon == CONSTANT_STATUS_101) {
                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    getUpdateSQL(CONSTANT_UPDATE_9);
                    //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                } else if (checkSalon == CONSTANT_STATUS_102) {
                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                    getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                    //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                } else if (checkSalon == CONSTANT_STATUS_2) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_3) {
                        this.executePullSalon(CONSTANT_PULL_STATUS_1);
                    } else if (checkSalon == CONSTANT_STATUS_5) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else if (checkSalon == CONSTANT_STATUS_6) {
                        this.executePullSalon(CONSTANT_STATUS_5);
                    } else {
                        lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                        lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    }
            }
            // End Pull SALON
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    private boolean getStatusPushIntroduction() {
        boolean flag = false;
        try {
            flag = this.getUpdateSQL(CONSTANT_STATUS_3);
            System.out.println("getStatusPushIntroduction, update sql");
            if (flag == CONSTANT_STATUS_CONTROL_T) {
                int statusPush = pushIntroduction();
                System.out.println("getStatusPushIntroduction: " + " " + statusPush);
                if (statusPush == CONSTANT_STATUS_202) {

                    TimerFiveSecondsIntroductionPush timer = new TimerFiveSecondsIntroductionPush();
                    timer.beepSalon();

                } else if ((statusPush >= CONSTANT_STATUS_500) && (statusPush <= CONSTANT_STATUS_599)) {
                    TimerControllerIntroductionPush timerPushIntroduction = new TimerControllerIntroductionPush();
                    timerPushIntroduction.beepIntroduction();
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                } else {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return flag;
    }
    // ============================================ END REGION PUSH INTRODUCTION

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbMedia;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbShop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanelPush;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbID;
    private javax.swing.JLabel lbPW;
    private javax.swing.JLabel lbPW1;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblMessage1;
    private javax.swing.JLabel lblSetDate;
    private javax.swing.JButton registMediaButton;
    private javax.swing.JButton registMediaButton1;
    private javax.swing.JLabel targetLabel;
    private javax.swing.JLabel targetLabel19;
    private javax.swing.JLabel targetLabel3;
    private javax.swing.JLabel targetLabel9;
    private com.geobeck.swing.JFormattedTextFieldEx txtID;
    private javax.swing.JPasswordField txtPW;
    private com.geobeck.swing.JFormattedTextFieldEx txtStoreID;
    // End of variables declaration//GEN-END:variables

// REGION PULL USER_MEDIA, INTRODUCTION, SALON
    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @return Integer
     */
    @SuppressWarnings("null")
    public int pullSalon() {

        int httpResult = CONSTANT_HTTP_RESPONSE;
        ResponseStatusAPI responseAPI = null;
        
        try {
            String url = mstApi.getApiUrl().trim() + "/salon/" + mstApi.getSalonID();
            URL object = new URL(url);
            Gson gson = new Gson();

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.reservation+json");
            con.setRequestProperty("Authorization", mstApi.getApiAuthCode());
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            //display what returns the POST request
            StringBuilder sb = new StringBuilder();
            httpResult = con.getResponseCode();
            if (httpResult == CONSTANT_STATUS_200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                //System.out.println("Salon Response: " + sb.toString());
                responseAPI = gson.fromJson(sb.toString(), ResponseStatusAPI.class);
                // Response Status != null
                if (null != responseAPI.getStatus()) {
                    switch (responseAPI.getStatus()) {
                        case CONSTANT_STATUS_4:
                            //UPDATE PULL_STATUS = 2
                            this.getUpdateSQL(CONSTANT_UPDATE_2);
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_4);
                            registMediaButton1.setEnabled(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                            System.out.println("Status of Salon" + " " + responseAPI.getStatus());
                            break;
                        // PULL FINISH STATUS 7
                        case CONSTANT_STATUS_7:
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                            this.getUpdateSQL(CONSTANT_UPDATE_7);
                            statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                            System.out.println("Status of Salon" + " " + responseAPI.getStatus());
                            break;
                        // PULL FINISH STATUS 8
                        case CONSTANT_STATUS_8:
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_7);
                            this.getUpdateSQL(CONSTANT_UPDATE_7);
                            statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_7);
                            System.out.println("Status of Salon" + " " + responseAPI.getStatus());
                            break;
                        case CONSTANT_STATUS_101:
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                            lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                            getUpdateSQL(CONSTANT_UPDATE_9);
                            //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_101);
                            statusControl(CONSTANT_STATUS_CONTROL_T);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                            break;
                        case CONSTANT_STATUS_102:
                            lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                            lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                            getUpdateSQL(CONSTANT_PUSH_STATUS_10);
                            //statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_102);
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            statusButtonControlPush(CONSTANT_STATUS_CONTROL_T);
                            break;
                        default:
                            statusControl(CONSTANT_STATUS_CONTROL_F);
                            System.out.println("Status of Salon" + " " + responseAPI.getStatus());
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (IOException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (JsonSyntaxException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        
        // Get status = 1, 2, 3, 5, 6 with timer
        if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_PULL_STATUS_1)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_2)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_3)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_5)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_6)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_7)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_8)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_4)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_101)) {
            return responseAPI.getStatus();
        } else if ((httpResult == CONSTANT_STATUS_200) && (responseAPI.getStatus() == CONSTANT_STATUS_102)) {
            return responseAPI.getStatus();
        } else {
            return httpResult;
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @return
     */
    public int pullIntroduction() {

        int httpResult = CONSTANT_HTTP_RESPONSE;

        try {

            String url = mstApi.getApiUrl().trim() + "/salon/" + mstApi.getSalonID() + "/introduction/pull";
            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.reservation+json");
            con.setRequestProperty("Authorization", mstApi.getApiAuthCode());
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            httpResult = con.getResponseCode();
            System.out.println("Status Pull Intruduction: " + " " + httpResult);
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return httpResult;

    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Use Push User_Media API
     * @throws JsonSyntaxException, IOException, Exception
     * @return
     */
    public int pushMedia() {

        int httpResult = CONSTANT_HTTP_RESPONSE;
        Integer salonId = mstApi.getSalonID();
        String media = ((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID();
        String password = txtPW.getText();
        String store_id = txtStoreID.getText();
        String user_id = txtID.getText();

        try {
            String url = mstApi.getApiUrl().trim() + "/salon/" + salonId + "/user-media";
            //String url = "https://relax-api-stg.pp-dev.org/salon/14/user-media";
            URL object = new URL(url);
            Gson gson = new Gson();

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.reservation+json");
            con.setRequestProperty("Authorization", mstApi.getApiAuthCode());
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            Map mapParam = new LinkedMap();
            mapParam.put("is_suspended", false);
            mapParam.put("login_check", true);
            mapParam.put("media", media);
            mapParam.put("password", password);
            mapParam.put("store_id", store_id);
            mapParam.put("user_id", user_id);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(gson.toJson(mapParam));
            writer.flush();

            //System.out.println("user_media: " + gson.toJson(mapParam));
            httpResult = con.getResponseCode();

        } catch (JsonSyntaxException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (IOException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return httpResult;
    }

// ============================= END REGION PULL USER_MEDIA, INTRODUCTION, SALON
// REGION INSERT, UPDATE DATA
    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param con
     * @return true or false
     */
    public boolean load(ConnectionWrapper con) {

        boolean flagData = false;
        
        try {
            if (con == null) {
                flagData = false;
            }
            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

            if (rs.next()) {
                mdia.setData(rs);
                flagData = true;
            }
            rs.close();

        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (NullPointerException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        
        return flagData;
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Load Data verify exists
     * @return
     */
    public String getSelectSQL() {
        return "select * \n"
                + "from mst_kanzashi_media \n"
                + "where "
                + "shop_id = " + SQLUtil.convertForSQL(((MstShop) cmbShop.getSelectedItem()).getShopID()) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID()) + "\n";

    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Insert Data while in DB = null
     * @return
     */
    private boolean getInsertSQL() throws SQLException {
        int checkValueInsert = 1;
        int pullStatus = 1;
        ConnectionWrapper connect = SystemInfo.getConnection();
        String sql = "insert into mst_kanzashi_media\n"
                + "(shop_id, media_id, use_flg, login_id, password, store_id, pull_status,\n"
                + "insert_date, update_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(((MstShop) cmbShop.getSelectedItem()).getShopID()) + ",\n"
                + SQLUtil.convertForSQL(((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID()) + ",\n"
                + SQLUtil.convertForSQL(CONSTANT_STATUS_USER_FLAG) + ",\n"
                + SQLUtil.convertForSQL(txtID.getText()) + ",\n"
                + SQLUtil.convertForSQL(txtPW.getText()) + ",\n"
                + SQLUtil.convertForSQL(txtStoreID.getText()) + ",\n"
                + SQLUtil.convertForSQL(pullStatus) + ",\n"
                + "current_timestamp, current_timestamp\n";
        return connect.executeUpdate(sql) == checkValueInsert;
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Update Data while Status = 4 or Status = 101
     * @return true or false
     * @param pull_status
     */
    private boolean getUpdateSQL(Integer pull_status) throws SQLException {
        int checkValueUpdate = 1;
        ConnectionWrapper connect = SystemInfo.getConnection();
        String sql = "update mst_kanzashi_media\n"
                + "set\n"
                //+ "use_flg = " + SQLUtil.convertForSQL(true) + ",\n"
                + "pull_status = " + SQLUtil.convertForSQL(pull_status) + ",\n"
                + "update_date = current_timestamp \n"
                + "where shop_id = " + SQLUtil.convertForSQL(((MstShop) cmbShop.getSelectedItem()).getShopID()) + "\n"
                + "and media_id = " + SQLUtil.convertForSQL(((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID()) + "\n";
        return connect.executeUpdate(sql) == checkValueUpdate;

    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Check Status pull_status
     * @return true or false
     * @throws SQLException
     */
    public boolean checkStatusDb() {
        boolean flagCheckStatus = false;

        try {

            ConnectionWrapper connect = SystemInfo.getConnection();
            String sql = "select * \n"
                    + "from mst_kanzashi_media \n"
                    + "where "
                    + "shop_id = " + SQLUtil.convertForSQL(((MstShop) cmbShop.getSelectedItem()).getShopID()) + "\n"
                    + "and media_id = " + SQLUtil.convertForSQL(((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID()) + "\n";

            ResultSetWrapper rs = connect.executeQuery(sql);
            if (rs.next()) {
                Integer pullStatus = rs.getInt("pull_status");
                if (CONSTANT_PULL_STATUS_2 != pullStatus) {
                    flagCheckStatus = true;
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return flagCheckStatus;
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Check Status pull_status
     * @return true or false
     */
    public boolean checkVisibleForMenu() {
        boolean flagCheckStatus = false;

        try {

            ConnectionWrapper connect = SystemInfo.getConnection();
            String sql = "select * \n"
                    + "from mst_kanzashi_media \n"
                    + "where "
                    + "shop_id = " + SQLUtil.convertForSQL(((MstShop) cmbShop.getSelectedItem()).getShopID()) + "\n"
                    + "and media_id = " + SQLUtil.convertForSQL(((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID()) + "\n";

            ResultSetWrapper rs = connect.executeQuery(sql);
            if (rs.next()) {
                Integer pullStatus = rs.getInt("pull_status");
                if (CONSTANT_STATUS_4 == pullStatus) {
                    flagCheckStatus = true;
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return flagCheckStatus;
    }

// ===================================== END REGION SE:LECT, INSERT, UPDATE DATA
// REGION COMMON
    /**
     *
     * @return
     */
    private boolean checkEmpty() {

        String textId = txtID.getText();
        String textPassword = txtPW.getText(); 
        String textStoreId = txtStoreID.getText();
        boolean flag = false;
        if (textId.equals(CONSTANT_MESSAGE_CLEAN_PULL)) {
            // IVS_PTQUANG start add New request #57992 [gb]かんざしAPI用管理画面構築（サイトコントロール導入画面）_導入手順１の入力チェック追加
            MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_USER_ID,
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            txtID.requestFocusInWindow();
            flag = true;
        } else if (textPassword.equals(CONSTANT_MESSAGE_CLEAN_PULL)) {
            // IVS_PTQUANG start add New request #57992 [gb]かんざしAPI用管理画面構築（サイトコントロール導入画面）_導入手順１の入力チェック追加
            MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_PASSWORD,
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            txtPW.requestFocusInWindow();
            flag = true;
        } else if (textStoreId.equals(CONSTANT_MESSAGE_CLEAN_PULL)) {
            // IVS_PTQUANG start add New request #57992 [gb]かんざしAPI用管理画面構築（サイトコントロール導入画面）_導入手順１の入力チェック追加
            MessageDialog.showMessageDialog(this,
                        CONSTANT_MESSAGE_STORE_ID,
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            txtStoreID.requestFocusInWindow();
            flag = true;
        }
        return flag;
    }
    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     * @param executor 
     */
    private void getScheduledExecutorService(Runnable executor){
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CONSTANT_SCHEDULE_THREAD_POOL);
        executorService.execute(executor);
        executorService.shutdown();
    }
    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @see Automatic pull while status = 1
     */
    private void executePullSalon(int status) {

        switch (status) {

            case CONSTANT_VERIFY_DATA_0:
                boolean checkFlagStatus0 = verifyData(CONSTANT_VERIFY_DATA_0);
                if (checkFlagStatus0) {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    lblMessage1.setText(CONSTANT_MESSAGE_CLEAN_PULL);
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                }
                break;

            case CONSTANT_PULL_STATUS_1: {
                statusControl(CONSTANT_STATUS_CONTROL_F);
                getScheduledExecutorService(new callMethodMainRunnable());
                break;
            }

            case CONSTANT_STATUS_5: {
                statusControl(CONSTANT_STATUS_CONTROL_F);
                getScheduledExecutorService(new callSalonPush());
                break;
            }
            
            case CONSTANT_VERIFY_STATUS_INIT: {
                boolean checkFlag = verifyData(CONSTANT_VERIFY_STATUS_INIT);
                if (checkFlag) {
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    getScheduledExecutorService(new callMethodMainRunnable());
                }
                break;
            }
            case CONSTANT_STATUS_3: {
                boolean checkFlag = verifyData(CONSTANT_STATUS_3);
                if (checkFlag) {
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    getScheduledExecutorService(new callRunnablePush());
                }
                break;
            }

            case CONSTANT_PUSH_STATUS_10:
                boolean checkFlagStatus10 = verifyData(CONSTANT_VERIFY_DATA_0);
                if (checkFlagStatus10) {
                    lblMessage.setText(CONSTANT_MESSAGE_PULL_SUCCESS);
                    lblMessage1.setText(CONSTANT_MESSAGE_PULL_FAIL);
                    statusControl(CONSTANT_STATUS_CONTROL_T);
                    statusButtonControlPush(CONSTANT_STATUS_CONTROL_F);
                }
                break;
            default:
                break;
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param check
     */
    private void statusControlInit(boolean check, int status) {
        if (status == CONSTANT_VERIFY_DATA_0) {
            //Set Control inactive
            cmbMedia.setEnabled(check);
            txtID.setEnabled(check);
            txtPW.setEnabled(check);
            txtStoreID.setEnabled(check);
            //Set Control inactive [end]
        } else {
            //Set Control inactive 
            cmbMedia.setEnabled(check);
            txtID.setEnabled(check);
            txtPW.setEnabled(check);
            txtStoreID.setEnabled(check);
            registMediaButton.setEnabled(check);
            registMediaButton1.setEnabled(check);
            //Set Control inactive [end]
        }
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param check
     */
    private void statusControl(boolean check) {
        //Set Control inactive 
        txtID.setEnabled(check);
        txtPW.setEnabled(check);
        txtStoreID.setEnabled(check);
        registMediaButton.setEnabled(check);
        //Set Control inactive [end]
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param check
     */
    private void statusButtonControlPush(boolean check) {
        //Set Control inactive
        registMediaButton1.setEnabled(check);
        //Set Control inactive [end]
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @return
     */
    private boolean verifyData(int iCheck) {
        boolean flag = false;
        Integer shopIdClient = ((MstShop) cmbShop.getSelectedItem()).getShopID();
        Integer shopIdDb = mdia.getShopID();
        String mediaClient = ((MstAPIDetail) cmbMedia.getSelectedItem()).getMediaID();
        String mediaDb = mdia.getMediaID();
        if (Objects.equals(shopIdClient, shopIdDb) && Objects.equals(mediaClient, mediaDb)) {
            switch (iCheck) {
                case CONSTANT_VERIFY_STATUS_0:
                    txtID.setText(mdia.getLoginID());
                    txtPW.setText(mdia.getPassword());
                    txtStoreID.setText(mdia.getStoreID());
                    statusControl(CONSTANT_STATUS_CONTROL_F);
                    break;
                case CONSTANT_VERIFY_STATUS_INIT:
                    txtID.setText(mdia.getLoginID());
                    txtPW.setText(mdia.getPassword());
                    txtStoreID.setText(mdia.getStoreID());
                    statusControlInit(CONSTANT_STATUS_CONTROL_F, valCheckControl);
                    break;
                case CONSTANT_STATUS_3:
                    statusControlInit(CONSTANT_STATUS_CONTROL_F, CONSTANT_STATUS_3);
                    break;
                default:
                    break;
            }
            flag = true;
        }
        return flag;
    }

    /**
     * @see IVS_PTQUANG fix feedback 11/10/2016 #54177
     * @return
     */
    private String fixValueDateFirstOfMonth() {
        // set value default was 1
        int dayFirstOfMonth = 1;
        // format date (year/month/day)
        String formatDate = "yyyy/MM/dd";
        SimpleDateFormat dt = new SimpleDateFormat(formatDate);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dayFirstOfMonth);
        String firstDate = dt.format(c.getTime());
        return String.format("予約日時が当月の１日（%s）以降の予約情報を取込みます。", firstDate);
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @return
     */
    private String getFirstDay() {
        // set value default was 1
        int dayFirstOfMonth = 1;
        String formatDate = "yyyy/MM/dd";
        SimpleDateFormat dt = new SimpleDateFormat(formatDate);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayFirstOfMonth);
        return String.valueOf(dt.format(cal.getTime()));
    }

// =========================================================== END REGION COMMON
}

// REGION CLASS RESPONSE STATUS
/**
 * @author IVS_PTQUANG add 2016/09/05 New request #54177
 * @see class response status API
 */
class ResponseStatusAPI {

    private Integer status;

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
// ============================================ END REGION CLASS RESPONSE STATUS

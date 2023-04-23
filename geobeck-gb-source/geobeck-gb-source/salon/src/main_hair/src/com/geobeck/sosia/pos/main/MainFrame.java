/*
 * MainFrame.java
 *
 * Created on 2006/10/12, 16:32
 */
package com.geobeck.sosia.pos.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geobeck.barcode.BarcodeEvent;
import com.geobeck.cti.CTIEvent;
import com.geobeck.customerDisplay.CustomerDisplay;
import com.geobeck.sosia.pos.account.CashManagementPanel;
import com.geobeck.sosia.pos.account.PrintReceipt;
import com.geobeck.sosia.pos.account.RegisterCashIOPanel;
import com.geobeck.sosia.pos.basicinfo.EnvironmentalSettingPanel;
import com.geobeck.sosia.pos.basicinfo.SimpleMasterPanel;
import com.geobeck.sosia.pos.basicinfo.WorkTimePasswordDialog;
import com.geobeck.sosia.pos.basicinfo.account.MstCashClassPanel;
import com.geobeck.sosia.pos.basicinfo.account.MstCashMenuPanel;
import com.geobeck.sosia.pos.basicinfo.account.MstDiscountPanel;
import com.geobeck.sosia.pos.basicinfo.account.MstPaymentMethodPanel;
import com.geobeck.sosia.pos.basicinfo.account.MstTaxPanel;
import com.geobeck.sosia.pos.basicinfo.account.ReceiptSettingPanel;
import com.geobeck.sosia.pos.basicinfo.commodity.MstSupplierItemPanel;
import com.geobeck.sosia.pos.basicinfo.commodity.MstSupplierPanel;
import com.geobeck.sosia.pos.basicinfo.company.BasicShiftPanel;
import com.geobeck.sosia.pos.basicinfo.company.MstCompanyPanel;
import com.geobeck.sosia.pos.basicinfo.company.MstGroupPanel;
import com.geobeck.sosia.pos.basicinfo.company.MstResponseClassPanel;
import com.geobeck.sosia.pos.basicinfo.company.MstResponsePanel;
import com.geobeck.sosia.pos.basicinfo.company.MstShopPanel;
import com.geobeck.sosia.pos.basicinfo.company.MstStaffClassPanel;
import com.geobeck.sosia.pos.basicinfo.company.MstStaffPanel;
import com.geobeck.sosia.pos.basicinfo.company.StaffShiftPanel;
import com.geobeck.sosia.pos.basicinfo.company.StaffWorkRegistrationPanel;
import com.geobeck.sosia.pos.basicinfo.company.StaffWorkTimePanel;
import com.geobeck.sosia.pos.basicinfo.company.StaffWorkTimePasswordPanel;
import com.geobeck.sosia.pos.basicinfo.company.VisitKarteSettingPanel;
import com.geobeck.sosia.pos.basicinfo.customer.MstCustomerRankSettingPanel;
import com.geobeck.sosia.pos.basicinfo.customer.MstKarteClassPanel;
import com.geobeck.sosia.pos.basicinfo.customer.MstKarteDetailPanel;
import com.geobeck.sosia.pos.basicinfo.customer.MstKarteReferencePanel;
import com.geobeck.sosia.pos.basicinfo.product.DeliveryStatusManagementPanel;
import com.geobeck.sosia.pos.basicinfo.product.MstItemClassPanel;
import com.geobeck.sosia.pos.basicinfo.product.MstItemPanel;
import com.geobeck.sosia.pos.hair.basicinfo.company.SiteControlIntroductionPanel;
import com.geobeck.sosia.pos.basicinfo.product.MstTargetSettingPanel;
import com.geobeck.sosia.pos.basicinfo.product.MstTechnicRegistBulkPanel;
import com.geobeck.sosia.pos.basicinfo.product.MstUseProductPanel;
import com.geobeck.sosia.pos.basicinfo.product.ProductDeliveryManagementPanel;
import com.geobeck.sosia.pos.cti.CTICustomerDialog;
import com.geobeck.sosia.pos.hair.account.HairBillsListPanel;
import com.geobeck.sosia.pos.hair.account.HairInputAccountPanel;
import com.geobeck.sosia.pos.hair.account.HairRegister;
import com.geobeck.sosia.pos.hair.account.HairRegisterPanel;
import com.geobeck.sosia.pos.hair.account.HairSearchAccountPanel;
import com.geobeck.sosia.pos.hair.basicinfo.company.MstBedPanel;
import com.geobeck.sosia.pos.hair.basicinfo.company.TargetActualShopInfoFrame;
import com.geobeck.sosia.pos.hair.basicinfo.company.TargetPerformanceOperatingDayFrame;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstCourseClassPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstCoursePanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstCustomerIntegrationPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstFreeHeadingClassPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstFreeHeadingPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstProportionallyPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstStaffTechnicTimePanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstTechnicClassPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstTechnicPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstTechnicProportionallyPanel;
import com.geobeck.sosia.pos.hair.customer.HairRepeaterPanel;
import com.geobeck.sosia.pos.hair.customer.InventoryManagementCustomerListPanel;
import com.geobeck.sosia.pos.hair.basicinfo.company.MediaSettingPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.DataMonthMemberPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.DataMonthlyBatchPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.MstPlanPanel;
import com.geobeck.sosia.pos.hair.basicinfo.product.AccountTransferPanel;
import com.geobeck.sosia.pos.hair.customer.MobileMemberListPanel;
import com.geobeck.sosia.pos.hair.customer.MstCustomerPanel;
import com.geobeck.sosia.pos.hair.customer.NotMemberListPanel;
import com.geobeck.sosia.pos.hair.data.message.DataMessagesPanel;
import com.geobeck.sosia.pos.hair.data.reservation.DataReservation;
import com.geobeck.sosia.pos.hair.mail.DmHistoryPanel;
import com.geobeck.sosia.pos.hair.mail.HairMailSearchPanel;
import com.geobeck.sosia.pos.hair.mail.MstMailTemplatePanel;
import com.geobeck.sosia.pos.hair.master.company.APIUserMedia;
import com.geobeck.sosia.pos.hair.master.company.APIUserMedias;
import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.hair.master.company.MstResponses;
import com.geobeck.sosia.pos.hair.pointcard.CardLayoutPanel;
import com.geobeck.sosia.pos.hair.pointcard.PointCalculatePanel;
import com.geobeck.sosia.pos.hair.pointcard.PointCardCleaningPanel;
import com.geobeck.sosia.pos.hair.pointcard.TestCardReader;
import com.geobeck.sosia.pos.hair.product.BackProductPanel;
import com.geobeck.sosia.pos.hair.product.InventryPanel;
import com.geobeck.sosia.pos.hair.product.InventryPanel_TOM;
import com.geobeck.sosia.pos.hair.product.MaterialRatePanelAvg_TOM;
import com.geobeck.sosia.pos.hair.product.MaterialRatePanel_TOM;
import com.geobeck.sosia.pos.hair.product.MoveStockPanel;
import com.geobeck.sosia.pos.hair.product.PlacePanel;
import com.geobeck.sosia.pos.hair.product.PrintInventoryPanel;
import com.geobeck.sosia.pos.hair.product.RegisterCheckInVoucherPanel;
import com.geobeck.sosia.pos.hair.product.RegisterCheckOutVoucherPanel;
import com.geobeck.sosia.pos.hair.product.RegisterOrderSlipPanel;
import com.geobeck.sosia.pos.hair.product.RegisterStaffSalesPanel;
import com.geobeck.sosia.pos.hair.product.StaffSalesHistoryPanel;
import com.geobeck.sosia.pos.hair.product.StockList;
import com.geobeck.sosia.pos.hair.product.StoreShipPanel;
import com.geobeck.sosia.pos.hair.product.TotalInventryPanel;
import com.geobeck.sosia.pos.hair.product.TotalInventryPanel_TOM;
import com.geobeck.sosia.pos.hair.report.AreaAndAttributeCustomerAnalytic;
import com.geobeck.sosia.pos.hair.report.BusinessReportPanel;
import com.geobeck.sosia.pos.hair.report.CancelCourseListPanel;
import com.geobeck.sosia.pos.hair.report.ContractRateReportPanel;
import com.geobeck.sosia.pos.hair.report.CrossAnalysis5Panel;
import com.geobeck.sosia.pos.hair.report.CrossAnalysisPanel;
import com.geobeck.sosia.pos.hair.report.CustomerProblemSheetPanel;
import com.geobeck.sosia.pos.hair.report.DailySalesReportPanelTom;
import com.geobeck.sosia.pos.hair.report.EarningReportPanel;
import com.geobeck.sosia.pos.hair.report.EffectIndicatorAnalysisPanel;
import com.geobeck.sosia.pos.hair.report.ItemSalesRankingPanel;
import com.geobeck.sosia.pos.hair.report.ItemStockListPanel;
import com.geobeck.sosia.pos.hair.report.Ito3DCustomerAnalysisPanel;
import com.geobeck.sosia.pos.hair.report.ItoCustomerProblemSheetPanel;
import com.geobeck.sosia.pos.hair.report.KarteAnalysisPanel;
import com.geobeck.sosia.pos.hair.report.MasherAnalysis5Panel;
import com.geobeck.sosia.pos.hair.report.MedicalRecordAnalysisPanel;
import com.geobeck.sosia.pos.hair.report.PassBookReportPanel;
import com.geobeck.sosia.pos.hair.report.PostMaxResultsPanel;
import com.geobeck.sosia.pos.hair.report.ReappearancePredictionPanel;
import com.geobeck.sosia.pos.hair.report.ReappearanceReportPanel;
import com.geobeck.sosia.pos.hair.report.ReasonsRankPanel;
import com.geobeck.sosia.pos.hair.report.RepeaterReportPanelTom;
import com.geobeck.sosia.pos.hair.report.ReportCustomizePanel;
import com.geobeck.sosia.pos.hair.report.ResponseRepeatAnalyzetPanel;
import com.geobeck.sosia.pos.hair.report.ResponseReportPanel;
import com.geobeck.sosia.pos.hair.report.SalesAnalyzePanel;
import com.geobeck.sosia.pos.hair.report.SalesReportPanel;
import com.geobeck.sosia.pos.hair.report.SalesTransitionReportPanel;
import com.geobeck.sosia.pos.hair.report.StaffResultCustomerReportPanel;
import com.geobeck.sosia.pos.hair.report.StaffResultGoodsReportPanel;
import com.geobeck.sosia.pos.hair.report.StaffResultListReportPanel;
import com.geobeck.sosia.pos.hair.report.StaffResultRepeatReportPanel;
import com.geobeck.sosia.pos.hair.report.StaffResultTechReportPanel;
import com.geobeck.sosia.pos.hair.report.StaffShopRankingPanel;
import com.geobeck.sosia.pos.hair.report.StoreReportPanel;
import com.geobeck.sosia.pos.hair.report.TechnicSalesReportPanelTom;
import com.geobeck.sosia.pos.hair.report.TimeAnalysisPanel;
import com.geobeck.sosia.pos.hair.report.TurnOverAnlyzePanel;
import com.geobeck.sosia.pos.hair.report.ItemDeliveryHistoryPanel;
import com.geobeck.sosia.pos.hair.report.OriginalReportPanel;
import com.geobeck.sosia.pos.hair.report.ProposalReportPanel;
import com.geobeck.sosia.pos.hair.report.ReportPointPanel;
import com.geobeck.sosia.pos.hair.reservation.BoardCooperationPanel;
import com.geobeck.sosia.pos.hair.reservation.ReservationStatusPanel;
import com.geobeck.sosia.pos.hair.reservation.ReservationTimeTablePanel;
import com.geobeck.sosia.pos.hair.reservation.TimeSchedule;
import com.geobeck.sosia.pos.hair.reservation.api.CustomerCommingInfomationResultsWSI;
import com.geobeck.sosia.pos.hair.reservation.api.CustomerCommingInfomationWSI;
import com.geobeck.sosia.pos.login.Login;
import com.geobeck.sosia.pos.mail.MstAutoMailSettingPanel;
import com.geobeck.sosia.pos.mail.MstMailSignaturePanel;
import com.geobeck.sosia.pos.mail.MstMailTemplateClassPanel;
import com.geobeck.sosia.pos.master.MstUser;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.report.CusContractHistoryReportPanel;
import com.geobeck.sosia.pos.report.ExpirationDateListPanel;
import com.geobeck.sosia.pos.report.RegisterCashIOReportPanel;
import com.geobeck.sosia.pos.report.ServiceRemainingReportPanel;
import com.geobeck.sosia.pos.report.custom.ModestyReportPanel;
import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.swing.AbstractImagePanelEx;
import com.geobeck.sosia.pos.swing.MainMenuButton;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.ImagePanel;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingCreator;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;

import connectispotapi.ConnectIspotApi;

/**
 *
 * @author katagiri
 */
public class MainFrame extends com.geobeck.sosia.pos.swing.AbstractMainFrame
        implements com.geobeck.barcode.BarcodeListener, com.geobeck.cti.CTIListener {

    private int CLOCK_TIMER_INTERVAL = 500;
    private final int CONSTANT_CONSTRUCTOR_SITE_CONTROL = 0;
    private int MENU_TIMER_DELAY = 10;
    private int MENU_OPEN_SPEED = 20;
    private Map subMenuMap = new HashMap();
    private Panel subMenuPanel = null;
    private String dbUrl = SystemInfo.getConnection().getUrl();
    private String title = "";

    /** カスタマイズ判定用DB名 */
    private static final String DB_POS_HAIR_DEV = "pos_hair_dev";			// 開発DB
    private static final String DB_POS_HAIR_ACQUAGRZ = "pos_hair_acquagrz";	// アクア・グラツィエ様DB
    private static final String DB_POS_HAIR_PRIMO = "pos_hair_primo";		// モデスティ様DB

    private final String MEDIA_HPB = "HPB"; // 20170828 add #24239

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {




        initComponents();
        initContentsFocusListener();
        this.addChildrenListener(this);
        addMouseCursorChange();
        initClock();
        initMainMenu();
        initTitle();
//		menuArrow.setVisible(false);

        // レシートプリンタを設定している場合のみドロアOPENボタンを表示する
        PrintReceipt pr = new PrintReceipt();
        if (pr.getReceiptSetting().getPrinterName().length() == 0) {
            drawerButton.setVisible(false);
        }

        if (SystemInfo.isGroup()) {
            mobileButton.setVisible(false);
        }

        checkMobileData();

        initTimer();
        //nhanvt edit start 20141015 Bug #31136
        showMsgPanel(false);
        //this.mark.setVisible(false);
        // Start 20141117 do.hong.quang update Request#32307 [gb]緊急ポップアップ、予約ボタン等イメージ変更
//        this.mark.setIcon(SystemInfo.getImageIcon("/menu/reservation/i2.jpg"));
        mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info.jpg"));
//        this.mark.setEnabled(false);
        this.mark.setEnabled(true);
        // End 20141117 do.hong.quang update Request#32307 [gb]緊急ポップアップ、予約ボタン等イメージ変更
        setMark();
        //nhanvt edit end 20141015 Bug #31136
        SystemInfo.openCtiReaderConnection(this);
        SystemInfo.openBarcodeReaderConnection(this);
        SystemInfo.openPointcardConnection();
        SystemInfo.loadCustomerDisplayInfo();

        this.changeContents(this.getTopPage());

        //IVS_LVTu start edit 2016/03/14 New request #49137

        if ((!SystemInfo.getAuthority().getAuthoryty(9)) || SystemInfo.getSystemType().equals(2)) {
            workingButton.setVisible(false);
        }
        //IVS_LVTu end edit 2016/03/14 New request #49137
        if (SystemInfo.getPossSalonId() != null) {
            if (!SystemInfo.getPossSalonId().equals("")) {
                if (hasBookingInfo()) {
                    BoardCooperationPanel pnl = new BoardCooperationPanel();
                    SwingUtil.openAnchorDialog(this, true, pnl, "【サロンボード連携】", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                }
            }
        }

        // ログイン時間を保持
        calLoginTime = Calendar.getInstance();
        //nhanvt edit start 20141015 Bug #31136
        javax.swing.Timer t = new javax.swing.Timer(3600000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMark();
            }
        });
        t.start();
        //nhanvt edit end 20141015 Bug #31136
        initReponses();
        //Luc start add 20160405 #49485
        if(SystemInfo.getSystemType().equals(1)) {
            workingButton.setVisible(false);
        }
        //Luc end add 20160405 #49485
    }

    public boolean hasBookingInfo() {
        try {
            connectispotapi.ConnectIspotApi WS = new ConnectIspotApi();
            CustomerCommingInfomationWSI wsi = new CustomerCommingInfomationWSI();
            CustomerCommingInfomationResultsWSI results = new CustomerCommingInfomationResultsWSI();
            SimpleDateFormat dateCommFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar calComm = Calendar.getInstance();
            wsi.setPosId(SystemInfo.getPosId());
            wsi.setPassword(SystemInfo.getPosPassWord());
            wsi.setPosSalonId(SystemInfo.getPossSalonId());
            wsi.setComingDate(dateCommFormat.format(calComm.getTime()));
            wsi.setFormat("json");
            Gson gson = new Gson();

            String jsonResult = WS.getConnectIspotApiPort().commingInfomation(gson.toJson(wsi));
            if (!jsonResult.equals("")) {
                try {
                    results = gson.fromJson(jsonResult, CustomerCommingInfomationResultsWSI.class);
                } catch (Exception e) {
                    System.out.println("error at http://api-test.sosia.jp/hpb/ConnectIspotApi.php?wsdl-function:commingInfomation At https://psa.salonboard.com/CLA/bt/common/v1/search/comingInfo/doSearch");
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);

                }
            }
            if (results.getResults().getComing_info().size() == 0) {
                return false;
            }


        } catch (Exception e) {

            return false;
        }
        return true;
    }

    private void initReponses() {
        ConnectionWrapper con = SystemInfo.getConnection();
        MstResponses responses = new MstResponses();
        responses.setShopId(SystemInfo.getCurrentShop().getShopID());
        try {
            //Edit start 2013-10-31 Hoa
            responses.load2(con);
            //Edit end 2013-10-31 Hoa
            SystemInfo.setResponses(responses);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void showMsgPanel(boolean flag) {
        //VTBPHUONG change 20130625
        DataMessagesPanel dmp = new DataMessagesPanel(flag);
        if (dmp.isIsLoad()) {
            SwingUtil.openAnchorDialog(this, true, dmp, "お知らせ", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        }
        setMark();
        //VTBPHUONG change 20130625
    }

    /**
     * SBとの連携が一時停止になっていないか調べる
     * 20170828 nami add #24239
     *
     */
    public void checkUserMediaIsSuspended () {
        SystemInfo.getMstUser().setShopID(SystemInfo.getCurrentShop().getShopID());
        if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                MstAPI mstApi = new MstAPI();
                mstApi.getUserMediaAPI();
                for (APIUserMedia userMediaAPI : mstApi.getListUserMedia()) {
                    if(userMediaAPI.getMedia().equals(MEDIA_HPB)) {
                        if (userMediaAPI.getIs_suspended()) {
                            //媒体がHPB＆一時停止ならアラートを出す
                            MessageDialog.showMessageDialog(null,
                                //MessageUtil.getMessage(20006, "基本設定","サイトコントロール設定","媒体連携設定"),
                                "\n現在、サロンボードとの連携が一時停止になっています\nSOSIA POSにも変更後のサロンボードのパスワードを登録していただく必要があります。\n\n" +
                                        "■ 基本設定 ＞ サイトコントロール設定 ＞ 媒体連携設定\n\nからサロンボードのパスワードを入力して、登録ボタンを押して下さい。\n" +
                                        "登録後、サロンボードとの連携は自動的に再開します。\n\n" +
                                        "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
                                        "※登録するまでサロンボードとの連携は一時停止のままです。\n※一時停止中に登録された予約は自動的に連携されますが、休憩は連携されません。\n" +
                                        "※一時停止中に登録した休憩は再度登録して下さい。\n",
                                "サイトコントロール",
                                JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
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

        backPanel = new com.geobeck.swing.ImagePanel();
        contentsPanel = new javax.swing.JPanel();
        menuPanel = new com.geobeck.swing.ImagePanel();
        clockPanel = new javax.swing.JPanel();
        year10 = new com.geobeck.swing.ImageLabel();
        year1 = new com.geobeck.swing.ImageLabel();
        month1 = new com.geobeck.swing.ImageLabel();
        month10 = new com.geobeck.swing.ImageLabel();
        day1 = new com.geobeck.swing.ImageLabel();
        day10 = new com.geobeck.swing.ImageLabel();
        hour10 = new com.geobeck.swing.ImageLabel();
        hour1 = new com.geobeck.swing.ImageLabel();
        minute10 = new com.geobeck.swing.ImageLabel();
        minute1 = new com.geobeck.swing.ImageLabel();
        year100 = new com.geobeck.swing.ImageLabel();
        year1000 = new com.geobeck.swing.ImageLabel();
        colon = new com.geobeck.swing.ImageLabel();
        slash0 = new com.geobeck.swing.ImageLabel();
        slash1 = new com.geobeck.swing.ImageLabel();
        closeApplicationButton = new javax.swing.JButton();
        contentsName = new javax.swing.JLabel();
        mobileButton = new javax.swing.JButton();
        topLogo = new com.geobeck.swing.ImagePanel();
        drawerButton = new javax.swing.JButton();
        workingButton = new javax.swing.JButton();
        mark = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SOSIA POS SALON");
        setName("main"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        backPanel.setImage(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/background.jpg")));
        backPanel.setLayout(null);

        contentsPanel.setOpaque(false);
        contentsPanel.setLayout(null);
        backPanel.add(contentsPanel);
        contentsPanel.setBounds(177, 37, 833, 691);

        menuPanel.setOpaque(false);
        menuPanel.setLayout(null);
        backPanel.add(menuPanel);
        menuPanel.setBounds(0, 94, 165, 634);

        clockPanel.setOpaque(false);
        clockPanel.setLayout(null);
        clockPanel.add(year10);
        year10.setBounds(27, 7, 10, 10);
        clockPanel.add(year1);
        year1.setBounds(37, 7, 10, 10);
        clockPanel.add(month1);
        month1.setBounds(67, 7, 10, 10);
        clockPanel.add(month10);
        month10.setBounds(57, 7, 10, 10);
        clockPanel.add(day1);
        day1.setBounds(97, 7, 10, 10);
        clockPanel.add(day10);
        day10.setBounds(87, 7, 10, 10);
        clockPanel.add(hour10);
        hour10.setBounds(114, 1, 15, 17);
        clockPanel.add(hour1);
        hour1.setBounds(129, 1, 15, 17);
        clockPanel.add(minute10);
        minute10.setBounds(151, 1, 15, 17);
        clockPanel.add(minute1);
        minute1.setBounds(166, 1, 15, 17);
        clockPanel.add(year100);
        year100.setBounds(17, 7, 10, 10);
        clockPanel.add(year1000);
        year1000.setBounds(7, 7, 10, 10);
        clockPanel.add(colon);
        colon.setBounds(144, 1, 7, 17);

        slash0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/clock/number_s/slash.jpg")));
        clockPanel.add(slash0);
        slash0.setBounds(47, 7, 10, 10);

        slash1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/clock/number_s/slash.jpg")));
        clockPanel.add(slash1);
        slash1.setBounds(77, 7, 10, 10);

        backPanel.add(clockPanel);
        clockPanel.setBounds(820, 8, 188, 19);

        closeApplicationButton.setIcon(//new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/menu/close_application_off.jpg"))
            new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/menu/soft_close_off.jpg")));
        closeApplicationButton.setBorderPainted(false);
        closeApplicationButton.setContentAreaFilled(false);
        closeApplicationButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/menu/soft_close_on.jpg")));
        closeApplicationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeApplicationButtonActionPerformed(evt);
            }
        });
        backPanel.add(closeApplicationButton);
        closeApplicationButton.setBounds(6, 64, 60, 26);

        contentsName.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        contentsName.setForeground(new java.awt.Color(100, 100, 100));
        backPanel.add(contentsName);
        contentsName.setBounds(176, 5, 407, 26);

        mobileButton.setIcon(SystemInfo.getImageIcon("/menu/main/btn_reserve.jpg"));
        mobileButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        mobileButton.setBorderPainted(false);
        mobileButton.setContentAreaFilled(false);
        mobileButton.setPressedIcon(SystemInfo.getImageIcon("/menu/main/btn_reserve_on.jpg"));
        mobileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mobileDataActionPerformed(evt);
            }
        });
        backPanel.add(mobileButton);
        mobileButton.setBounds(675, 3, 65, 30);

        topLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/topLogo.jpg")));
        topLogo.setOpaque(false);
        topLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topLogoMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout topLogoLayout = new org.jdesktop.layout.GroupLayout(topLogo);
        topLogo.setLayout(topLogoLayout);
        topLogoLayout.setHorizontalGroup(
            topLogoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 160, Short.MAX_VALUE)
        );
        topLogoLayout.setVerticalGroup(
            topLogoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 60, Short.MAX_VALUE)
        );

        backPanel.add(topLogo);
        topLogo.setBounds(0, 0, 160, 60);

        drawerButton.setIcon(SystemInfo.getImageIcon("/menu/main/btn_drawer.jpg"));
        drawerButton.setBorderPainted(false);
        drawerButton.setContentAreaFilled(false);
        drawerButton.setFocusable(false);
        drawerButton.setPressedIcon(SystemInfo.getImageIcon("/menu/main/btn_drawer_on.jpg"));
        drawerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawerButtonActionPerformed(evt);
            }
        });
        backPanel.add(drawerButton);
        drawerButton.setBounds(587, 3, 85, 30);

        workingButton.setIcon(//new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/menu/back_to_main_off.jpg"))
            new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/menu/work_staff_off.jpg")));
        workingButton.setBorderPainted(false);
        workingButton.setContentAreaFilled(false);
        workingButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/menu/work_staff_on.jpg")));
        workingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workingButtonActionPerformed(evt);
            }
        });
        backPanel.add(workingButton);
        workingButton.setBounds(80, 64, 80, 26);

        mark.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                markMouseClicked(evt);
            }
        });
        mark.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                markAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        backPanel.add(mark);
        mark.setBounds(743, 3, 65, 30);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1016, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void workingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workingButtonActionPerformed

        // 出退勤登録画面
        StaffWorkTimePanel swtp = new StaffWorkTimePanel();
        changeContents(swtp);

    }//GEN-LAST:event_workingButtonActionPerformed

    private void topLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topLogoMouseClicked

        this.changeContents(this.getTopPage());
        this.hideSubMenu();
        timer.start();
        closeButton = openedButton;

    }//GEN-LAST:event_topLogoMouseClicked

    private void drawerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawerButtonActionPerformed

        if (SystemInfo.getReceiptPrinterType().equals(1)) {

            // EPSON

            try {

                String drawerOpenFile = "/EpsonDrawerOpen.exe";

                InputStream in = getClass().getResource(drawerOpenFile).openStream();
                OutputStream out = new FileOutputStream(ReportManager.getExportPath() + drawerOpenFile);

                try {
                    byte[] buf = new byte[1024];
                    int len = 0;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    out.flush();
                } finally {
                    out.close();
                    in.close();
                }

                PrintReceipt pr = new PrintReceipt();
                Runtime.getRuntime().exec(ReportManager.getExportPath() + drawerOpenFile + " " + pr.getReceiptSetting().getPrinterName());

            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        } else {

            // STAR

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            Doc doc = new SimpleDoc((new byte[]{0x1B, 0x07, 0x14, 0x14, 0x07}), flavor, new HashDocAttributeSet());
            DocPrintJob job = null;

            // レシートプリンタ名取得
            PrintReceipt pr = new PrintReceipt();
            String printerName = pr.getReceiptSetting().getPrinterName();

            if (printerName.length() > 0) {
                for (PrintService ps : PrintServiceLookup.lookupPrintServices(flavor, aset)) {
                    if (!printerName.equals(ps.getName())) {
                        continue;
                    }

                    job = ps.createPrintJob();
                    break;
                }
                try {
                    job.print(doc, aset);
                } catch (Exception e) {
                    //SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }
        }

    }//GEN-LAST:event_drawerButtonActionPerformed

        private void mobileDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mobileDataActionPerformed

            ReservationTimeTablePanel rrp = new ReservationTimeTablePanel();
            DataReservation dr = setMobile();

            if (dr != null) {

                changeContents(rrp);
                rrp.setDate(dr.getReserveDate());
                rrp.registReserve(dr, null);
                if (!isMobileData()) {
                    checkTimer.start();
                    mobileButton.setEnabled(false);
                   //Start 20141117 do.hong.quang update
//                   mobileButton.setIcon(SystemInfo.getImageIcon("/menu/customer/reserve_none.jpg"));
                   mobileButton.setIcon(SystemInfo.getImageIcon("/menu/main/btn_reserve_noreserve.jpg"));
                   //End 20141117 do.hong.quang update
                }

            } else {

                mobileButton.setEnabled(false);
                //Start 20141117 do.hong.quang update
//                   mobileButton.setIcon(SystemInfo.getImageIcon("/menu/customer/reserve_none.jpg"));
                   mobileButton.setIcon(SystemInfo.getImageIcon("/menu/main/btn_reserve_noreserve.jpg"));
                   //End 20141117 do.hong.quang update
            }

        }//GEN-LAST:event_mobileDataActionPerformed

	private void closeApplicationButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeApplicationButtonActionPerformed
	{//GEN-HEADEREND:event_closeApplicationButtonActionPerformed
            this.closeApplication();
	}//GEN-LAST:event_closeApplicationButtonActionPerformed

    /**
     *
     * @param evt
     */
	private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
	{//GEN-HEADEREND:event_formWindowClosing
            this.closeApplication();
	}//GEN-LAST:event_formWindowClosing

    private void markAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_markAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_markAncestorAdded

    private void markMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_markMouseClicked
        // TODO add your handling code here:
        // Start 20141117 do.hong.quang update
//        if(this.mark.isEnabled()){
            //nhanvt edit start 20141015 Bug #31136
//            mark.setIcon(SystemInfo.getImageIcon("/menu/reservation/mark.JPG"));
        setStatusIconAction();
        showMsgPanel(true);
//            showMsgPanel(false);
//            mark.setIcon(SystemInfo.getImageIcon("/menu/reservation/i2.jpg"));
           //nhanvt edit end 20141015 Bug #31136
//        }
        // End 20141117 do.hong.quang update
    }//GEN-LAST:event_markMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JPanel clockPanel;
    private javax.swing.JButton closeApplicationButton;
    private com.geobeck.swing.ImageLabel colon;
    private javax.swing.JLabel contentsName;
    private javax.swing.JPanel contentsPanel;
    private com.geobeck.swing.ImageLabel day1;
    private com.geobeck.swing.ImageLabel day10;
    private javax.swing.JButton drawerButton;
    private com.geobeck.swing.ImageLabel hour1;
    private com.geobeck.swing.ImageLabel hour10;
    private javax.swing.JLabel mark;
    private com.geobeck.swing.ImagePanel menuPanel;
    private com.geobeck.swing.ImageLabel minute1;
    private com.geobeck.swing.ImageLabel minute10;
    private javax.swing.JButton mobileButton;
    private com.geobeck.swing.ImageLabel month1;
    private com.geobeck.swing.ImageLabel month10;
    private com.geobeck.swing.ImageLabel slash0;
    private com.geobeck.swing.ImageLabel slash1;
    private com.geobeck.swing.ImagePanel topLogo;
    private javax.swing.JButton workingButton;
    private com.geobeck.swing.ImageLabel year1;
    private com.geobeck.swing.ImageLabel year10;
    private com.geobeck.swing.ImageLabel year100;
    private com.geobeck.swing.ImageLabel year1000;
    // End of variables declaration//GEN-END:variables
    //メインメニュー
    private MainMenuButton customerButton = null;
    private MainMenuButton reservationButton = null;
    private MainMenuButton accountButton = null;
    private MainMenuButton itemButton = null;
    private MainMenuButton reportButton = null;
    //Thanh add 2013/06/03
    private MainMenuButton analiticCustomerButton = null;
    private MainMenuButton menuRankingButton = null;
    //Thanh add 2013/06/03
    private MainMenuButton mailButton = null;
    private MainMenuButton reportManageButton = null;
    private MainMenuButton masterButton = null;
    private MainMenuButton helpButton = null;
    //商品管理
    private MainMenuButton productButton = null;
    private JButton productReportButton = null;
    private JButton productReportTomButton = null;
    private JPanel productReportSubMenu = null;
    private JPanel productReportTomSubMenu = null;
    private JButton inventoryReportButton = null;
    private JButton storeshipButton = null;
    private JButton productPlaceButton = null;
    private JButton totalInventryReportButton = null;
    private JButton staffSalesListReportButton = null;
    private JButton registerOrderSlipButton = null;
    private JButton registerCheckInVoucherButton = null;
    private JButton registerCheckOutVoucherButton = null;
    private JButton inventryPanelButton = null;
    private JButton inventryPanelTomButton = null;
    private JButton transferProductButton = null;
    private JButton stocklistButton = null;
    private JButton backProductButton = null;
    private JButton materialCostTomButton = null;
    private JButton totalInventoryTomButton = null;
    private JButton materialCostAveTomButton = null;
    private JPanel productStaffSalesMenu = null;
    private JButton productStaffSalesButton = null;
    private JButton staffSalesInputButton = null;
    private JButton staffSalesHistoryButton = null;
    //顧客管理
    private ImagePanel customerMenu = null;
    private JButton registCustomerButton = null;
    private JButton notMemberListButton = null;
    private JButton repeaterButton = null;
    private JButton mobileMemberListButton = null;
    //予約管理
    private ImagePanel reservationMenu = null;
    private JButton registReservatioButton = null;
    private JButton showStatusButton = null;
    //精算管理
    private ImagePanel accountMenu = null;
    private JButton inputAccountButton = null;
    private JButton searchAccountButton = null;
    private JButton receivableListButton = null;
    private JButton registerButton = null;
    private JButton registerCashButton = null;
    //商品管理
    private ImagePanel itemMenu = null;
    //帳票出力
    private ImagePanel reportMenu = null;
    private JButton businessReportButton = null;
    private JButton salesTotalButton = null;
    private JButton timeAnalysisButton = null;
    private JButton saleTransittionButton = null;
    private JButton staffResultButton = null;
    private JButton responseEffectButton = null;			// レスポンス分析
    private JButton rankingButton = null;			// ランキング
    private JButton registerCashIOButton = null;			// レジ入出金
    private JButton reappearanceButton = null;
    private JButton reappearancePredictionButton = null;
    private JButton crossAnalysisButton = null;
    private JButton baseCashButton = null;
    private JButton TurnOverAnalyzeButton = null;
    //Luc add Start 20121004
    private JButton customerAreaAnalysisButton = null;
    private JButton customerAtrributeAnalysisButton = null;
    //Luc add End 20121004
    private JButton productCustomerButton = null;
    //VTAn Start add 20140612
    private JButton showDeliverStatusButton = null;
    //VTAn End add 20140612
	//GBO Start add 20161110
    private JButton customCutoffReportButton = null;
	//GBO Start add 20161110

    //小口現金
    private JPanel baseCashMenu = null;
    private JButton cashClassButton = null;
    private JButton cashMenuButton = null;
    private JButton cashRegistButton = null;
    // カスタム帳票
    private JButton customSetteingButton = null;
    private JButton customSetteinTagergButton = null;
    private JPanel customReportMenu = null;
    private JButton dailyReportButton = null;
    private JButton monthlyReportButton = null;
    private JButton rankingReportButton = null;
    private JButton repeatReportButton = null;
    private JButton analysisReportButton = null;
    //メール機能
    private ImagePanel mailMenu = null;
    private JButton conditionedSearchButton = null;
    private JButton dmHistoryButton = null;
    private JButton templateClassButton = null;
    private JButton templateButton = null;
    private JButton signatureButton = null;
    private JButton autoMailButton = null;                   // 自動メール設定ボタン
    // 帳票管理
    private JButton effectIndicatorAnalysisButton = null;
    private JButton reasonsRankButton = null;
    private JButton itemSalesRankingButton = null;
    private JButton postMaxResultsButton = null;
    private JButton itemStockListButton = null;
    //基本情報
    private ImagePanel masterMenu = null;
    private JButton companyMasterButton = null;
    private JButton itemMasterButton = null;
    private JButton itemSiteControlIntroduction = null;
    private JButton technicMasterButton = null;
    private JButton customerMasterButton = null;
    private JButton accountMasterButton = null;
    private JButton csvImportButton = null;
    private JButton csvOutportButton = null;
    private JButton settingButton = null;
    private JButton pointcardButton = null;
    // カスタム設定
    private JButton customReportButton = null;
    private JPanel customSettingMenu = null;
    private JButton resultTargetButton = null;
    private JButton workingStaffButton = null;
    //会社マスタ
    private JPanel companyMasterMenu = null;
    private JButton mstCompanyButton = null;
    private JButton mstGroupButton = null;
    //IVS_LTThuc start add 20140707 MASHU_業態登録
    private JButton mstUseShopCategoryButton = null;
    //IVS_LTThuc start end 20140707 MASHU_業態登録
    private JButton mstAuthorityButton = null;
    private JButton mstShopButton = null;
    private JButton mstStaffClassButton = null;
    private JButton mstStaffButton = null;
    private JButton mstWorkTimePassButton = null;
    private JButton mstBedButton = null;
    private JButton mstResponseButton = null;		// 反響項目登録ボタン
    //Start add 20131102 lvut (rappa --> gb_source)
    private JButton mstResponseClassButton = null;
    //End add 20131102 lvut (rappa --> gb_source)
    private JButton mstBasicShiftButton = null;
    private JButton mstStaffShiftButton = null;
    //Thanh add start 20130415 顧客契約履歴
    private JButton cusContractHistoryButton = null;
    //Thanh add End 20130415 顧客契約履歴
    private JButton visitKarteSettingButton  = null; // 来店カルテ設定ボタン
    private JPanel customMenu = null;
    private JButton customButton = null;   //担当別成績一覧表
    //商品マスタ
    private JPanel itemMasterMenu = null;
    private JButton mstItemClassButton = null;
    private JButton mstItemButton = null;
    private JButton mstUseItemButton = null;
    private JButton mstSupplierButton = null;
    private JButton mstSupplierItemButton = null;
    private JButton mstDestockingDivisionButton = null;
    private JButton ProductDeliveryManagementButton = null;
    // vtbphuong start add 20150603
    private JButton passBookButton = null;
     // vtbphuong end add 20150603
    /**
     * 置場マスタ
     */
    private JButton mstPlaceButton = null;
    //技術マスタ
    private JPanel technicMasterMenu = null;
    private JButton mstTechnicClassButton = null;
    private JButton mstTechnicButton = null;
    private JButton mstProportionallyButton = null;	    // 按分マスタ登録
    private JButton mstTechProportionallyButton = null;	    // 技術按分登録
    private JButton mstUseTechnicButton = null;
    private JButton mstStaffTechnicTimeButton = null;		// スタッフ毎施術時間登録
    private JButton mstCourseClassButton = null; //コース契約分類登録
    private JButton mstCourseButton = null;  //コース登録
    private JButton mstUseCourseButton = null; //使用コース登録
	//マスタ一括登録 add start 2016/12/28
	private JButton mstTechnicRegistBulkButton = null; //技術一括登録
	//マスタ一括登録 add end 2016/12/28

    //顧客マスタ
    private JPanel customerMasterMenu = null;
    private JButton mstJobButton = null;
    private JButton mstFirstComingMotiveButton = null;   //初回来店動機登録
    private JButton mstCustomerRankSettingButton = null;   //顧客ランク設定
    private JButton mstKarteClassButton = null;   //カルテ分類登録
    private JButton mstKarteDetailButton = null;   //カルテ詳細登録
    private JButton mstKarteReferenceButton = null;   //カルテ参照項目登録
    private JButton mstFreeHeadingClassButton = null;		// フリー項目区分登録ボタン
    private JButton mstFreeHeadingButton = null;		// フリー項目登録ボタン
    private JButton mstCustomerIntegrationButton = null;		// 顧客統合
    //精算マスタ
    private JPanel accountMasterMenu = null;
    private JButton mstTaxButton = null;
    private JButton mstPaymentMethodButton = null;
    private JButton mstDiscountButton = null;
    private JButton receiptSettingButton = null;
    //顧客分析 add
    private JPanel customerEffectMenu = null;
    private JButton customerEffectButton = null;
    //売上分析 add
    private JPanel salesEffectMenu = null;
    private JButton storeButton = null;
    private JButton salesEffectButton = null;
    // 再来分析 add
    private JPanel reappearanceEffectMenu = null;
    private JButton reappearanceEffectButton = null;
    private JPanel repeatAnalysisMenu = null;
    private JButton repeatAnalysisButton = null;
    private JButton responseRepeatAnalyzetButton = null;
    // 動向分析
    private JButton reappearancePredictionEffectButton = null;
    // クロス分析
    private JButton crossAnalysisEffectButton = null;
    private JButton crossAnalysis5EffectButton = null;
    // サイクル分析
    private JButton karteAnalysisEffectButton = null;
    // 客数問題発見シートα
    private JButton customerProblemSheetButton = null;
    //スタッフ成績 add
    private JPanel staffResultMenu = null;
    private JButton staffResultListButton = null;   //担当別成績一覧表
    private JButton staffResultTechButton = null;   //担当別技術成績
    private JButton staffResultGoodsButton = null;   //担当別商品成績
    private JButton staffResultCustomerButton = null;   //担当別顧客成績
    private JButton staffResultRepeatButton = null;   //担当別再来成績
    //いとーさん分析
    private JButton itoCustomerProblemSheetButton = null;
    private JButton itoAnalitic3jigenButton = null;
    //Mail add
    private JButton mailMasterButton = null;
    private JPanel mailMasterMenu = null;
    //OXカスタム帳票
    private JButton salesComparisionButton = null;
    private JButton salesReportButton = null;
    // ポイントカード
    private JPanel pointcardMenu = null;
    private JButton pointcardTemplateButton = null;
    private JButton pointcardCalculationButton = null;
    private JButton pointcardInitSettingButton = null;
    private JButton pointcardTestButton = null;
    private javax.swing.Timer timer = null;
    private int mobile_check_time = 60 * 1000;
    private Calendar calLoginTime = null;
    private boolean isCheckLoginTime = true;
    private MainMenuButton clickedButton = null;
    private MainMenuButton openButton = null;
    private MainMenuButton openedButton = null;
    private MainMenuButton closeButton = null;
    private JPanel subMenu = null;
    private TopPagePanel topPage = null;
    private FocusAdapter fa = null;
    private KeyAdapter ka = null;
    private MouseAdapter ma = null;
    private javax.swing.Timer clockTimer = null;
    private ImageIcon[] numberImageS = new ImageIcon[10];
    private ImageIcon[] numberImageL = new ImageIcon[10];
    private boolean isChanging = false;
    private javax.swing.Timer checkTimer = null;
    private TimeSchedule ts = new TimeSchedule();
    // 役務帳票
    private JPanel serviceReportMenu = null;
    private JButton serviceReportButton = null;
    private JButton serviceListButton = null; // 役務残一覧
    private JButton cancelCourseListButton = null; // 解約一覧
    private JButton expirationListButton = null; // 有効期限一覧
     //IVS_LVTu start add 2015/11/06 New request #44110
    private JButton contractRateReportButton = null; // 契約率集計
     //IVS_LVTu end add 2015/11/06 New request #44110
    //IVS_LVTu start add 2015/11/21 New request #44111
    private JButton ProposalReportButton = null; // 契約率集計
     //IVS_LVTu end add 2015/11/21 New request #44111
    private JButton PointReportButton = null;
    private JButton SettingKANZASHI = null;

    //IVS_LVTu start add 2018/13/05 GB_Finc
    private JPanel manageMemberMasterMenu   = null;
    private JButton manageMemberMonButton   = null;   // 月会員管理
    private JButton mstPlanButton           = null;   //プラン登録
    private JButton dataMonMemberButton     = null;   //月会員登録
    private JButton dataMonthlyBatchButton  = null;   //一括処理
    //IVS start add 2020/04/01 口座振替連携
    private JButton accountTransferButton  = null;   //口座振替連携
    //IVS end add 2020/04/01 口座振替連携
    //IVS_LVTu end add 2018/13/05 GB_Finc
    //Start add 20220502 TUNEオリジナル帳票出力 の対応
     private JButton customizeReportButton  = null;   //カスタム帳票
    //End add 20220502 TUNEオリジナル帳票出力 の対応

    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(mobileButton);
        SystemInfo.addMouseCursorChange(drawerButton);
        SystemInfo.addMouseCursorChange(closeApplicationButton);
        SystemInfo.addMouseCursorChange(workingButton);
        SystemInfo.addMouseCursorChange(topLogo);
        //IVS_LVTu start add 2016/03/11 Bug #49034
        SystemInfo.addMouseCursorChange(mark);
        //IVS_LVTu end add 2016/03/11 Bug #49034
    }

    private int getMobileCheckTime() {
        return mobile_check_time;
    }

    /**
     * 終了処理を行う。
     */
    private void closeApplication() {
        int stayCount = 0;
        int workingCount = 0;
        StringBuilder msg = new StringBuilder(1000);

        if (!SystemInfo.isGroup()) {
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = con.executeQuery(this.getStayCheckSQL());
                if (rs.next()) {
                    stayCount = rs.getInt("cnt");
                }
                rs.close();

                rs = con.executeQuery(this.getWorkingCheckSQL());
                if (rs.next()) {
                    workingCount = rs.getInt("cnt");
                }
                rs.close();


            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            if (stayCount > 0 || workingCount > 0) {
                msg.append("---------------------------------------------\n");
                if (stayCount > 0) {
                    msg.append("※ 在店者が " + stayCount + " 人います。\n");
                }
                if (workingCount > 0) {
                    msg.append("※ 退勤していないスタッフがいます。\n");
                }
                msg.append("---------------------------------------------\n");
            }
        }

        msg.append(MessageUtil.getMessage(2));

        if (MessageDialog.showYesNoDialog(this,
                msg.toString(),
                this.getTitle(),
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
               //nhanvt start 20141229 edit New request #34634
                 //レジ管理用メール送信機能
                HairRegister register = new HairRegister();
                java.sql.Date result = register.checkMailSend();

                //未送信メールが存在
                if (result != null) {
                    //
                    if (MessageDialog.showYesNoDialog(this,
                            "レジ管理情報が未送信です。\n送信してもよろしいですか？",
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE,
                            JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION) {
                        GregorianCalendar date = new GregorianCalendar();
                        date.setTime(result);
                        register = new HairRegister(SystemInfo.getCurrentShop(), date);
                        ConnectionWrapper con = SystemInfo.getMailConnection();
                        if (con.getConnection() != null) {
                            if (register.sendMail(con)) {
                                MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(200, "メール送信完了"),
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);


                            } else {
                                MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(1099, ""),
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                       }else{
                            int ret = MessageDialog.showYesNoDialog(
                                        this,
                                        "レジ締めメールの送信に失敗しました。\n" +
                                        "時間をおいて再度、登録を行ってください。",
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE,
                                        JOptionPane.NO_OPTION);
                            if (ret == JOptionPane.YES_OPTION) return;
                       }
                       register.sendMailEndFlgOn(SystemInfo.getConnection(), date);

                    }

                }
                 //タイマーを止める
                timer.stop();
                timer = null;
                clockTimer.stop();
                clockTimer = null;

                SystemInfo.closeBarcodeReaderConnection();
                SystemInfo.closeCtiReaderConnection();

                //nhanvt end 20141229 edit New request #34634
                // カスタマーディスプレイの画面消去
                if (SystemInfo.isUseCustomerDisplay()) {
                    String comport = SystemInfo.getCustomerDisplayPort();
                    if (comport != null && !comport.equals("")) {
                        CustomerDisplay.getInstance(comport).clearScreen();
                    }
                }

                // 店舗ログインの場合は翌日以降の予約情報ダウンロード
                if (!SystemInfo.isGroup()) {
                    download_next_reservation();
                }

            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            SosiaPos.closeApplication();
        }
    }

    private String getStayCheckSQL() {

        MstShop shop = SystemInfo.getCurrentShop();

        Calendar calStart = Calendar.getInstance();
        if (calStart.get(Calendar.HOUR_OF_DAY) < shop.getOpenHour().intValue()) {
            calStart.add(Calendar.DAY_OF_MONTH, -1);
        }

        calStart.set(Calendar.HOUR_OF_DAY, shop.getOpenHour());
        calStart.set(Calendar.MINUTE, shop.getOpenMinute());
        Calendar calEnd = (Calendar) calStart.clone();
        calEnd.add(Calendar.DAY_OF_MONTH, 1);
        calEnd.set(Calendar.HOUR_OF_DAY, shop.getOpenHour());
        calEnd.set(Calendar.MINUTE, shop.getOpenMinute());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     count(*) as cnt");
        sql.append(" from");
        // IVS start add 2022/08/15 技術メニュー数は関係なく在店している純数を表示するよう修正
        sql.append(" (select distinct dr.shop_id, dr.reservation_no");
        sql.append(" from");
        // IVS end add 2022/08/15 技術メニュー数は関係なく在店している純数を表示するよう修正
        sql.append("     data_reservation dr");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("                 on drd.shop_id = dr.shop_id");
        sql.append("                and drd.reservation_no = dr.reservation_no");
        sql.append("                and drd.delete_date is null");
        // IVS start edit 2022/08/15 技術メニュー数は関係なく在店している純数を表示するよう修正
        //sql.append("         left join mst_customer mc");
        //sql.append("                on mc.customer_id = dr.customer_id");
        //sql.append("         left join mst_technic mt");
        //sql.append("                on mt.technic_id = drd.technic_id");
        //sql.append("         left join mst_technic_class mtc");
        //sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        //sql.append("         left join mst_staff ms");
        //sql.append("                on ms.staff_id = dr.staff_id");
        // IVS start edit 2022/08/15 技術メニュー数は関係なく在店している純数を表示するよう修正
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(calStart));
        sql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(calEnd));
        sql.append("     and dr.status in (2)");
        // IVS start add 2022/08/15 技術メニュー数は関係なく在店している純数を表示するよう修正
        sql.append(" ) temp");
        // IVS end add 2022/08/15 技術メニュー数は関係なく在店している純数を表示するよう修正

        return sql.toString();
    }

    private String getWorkingCheckSQL() {

        MstShop shop = SystemInfo.getCurrentShop();

        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) < shop.getOpenHour().intValue()) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      count(*) as cnt");
        sql.append(" from");
        sql.append("     mst_staff a");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      *");
        sql.append("                     ,case when recess_start_time1 is null then 0 else 1 end +");
        sql.append("                      case when recess_start_time2 is null then 0 else 1 end +");
        sql.append("                      case when recess_start_time3 is null then 0 else 1 end +");
        sql.append("                      case when recess_start_time4 is null then 0 else 1 end +");
        sql.append("                      case when recess_start_time5 is null then 0 else 1 end as recess_start_count");
        sql.append("                     ,case when recess_finish_time1 is null then 0 else 1 end +");
        sql.append("                      case when recess_finish_time2 is null then 0 else 1 end +");
        sql.append("                      case when recess_finish_time3 is null then 0 else 1 end +");
        sql.append("                      case when recess_finish_time4 is null then 0 else 1 end +");
        sql.append("                      case when recess_finish_time5 is null then 0 else 1 end as recess_finish_count");
        sql.append("                 from");
        sql.append("                     data_staff_work_time");
        sql.append("             ) b");
        sql.append("                on a.staff_id = b.staff_id");
        sql.append("               and b.working_date = " + SQLUtil.convertForSQLDateOnly(cal));
        sql.append("               and b.delete_date is null");
        sql.append("         left join data_schedule c");
        sql.append("                on a.staff_id = c.staff_id");
        sql.append("               and c.schedule_date = " + SQLUtil.convertForSQLDateOnly(cal));
        sql.append("         left join mst_shift d");
        sql.append("                on a.shop_id = d.shop_id");
        sql.append("               and c.shift_id = d.shift_id");
        sql.append(" where");
        sql.append("         a.delete_date is null");
        sql.append("     and " + SQLUtil.convertForSQL(shop.getShopID()) + " in (a.shop_id, b.shop_id)");
        sql.append("     and a.staff_id not in");
        sql.append("         (");
        sql.append("             select");
        sql.append("                 a.staff_id");
        sql.append("             from");
        sql.append("                 mst_staff a");
        sql.append("                     join data_staff_work_time b");
        sql.append("                     using (staff_id)");
        sql.append("             where");
        sql.append("                     a.delete_date is null");
        sql.append("                 and b.delete_date is null");
        sql.append("                 and b.shop_id <> " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("                 and b.working_date = " + SQLUtil.convertForSQLDateOnly(cal));
        sql.append("         )");
        sql.append("     and b.working_start_time is not null");
        sql.append("     and b.working_finish_time is null");

        return sql.toString();
    }

    private void download_next_reservation() {

        final String[] fn_strWDay = {"日", "月", "火", "水", "木", "金", "土"};

        String dirName = SystemInfo.getLogRoot() + "/../reserve";
        String fileName = dirName + "/予約情報.txt";

        try {

            (new File(dirName)).mkdir();

            File file = new File(fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
            SimpleDateFormat sdf2 = new SimpleDateFormat("H:mm");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Calendar calStart = (Calendar) cal.clone();
            cal.add(Calendar.DAY_OF_MONTH, 13);
            Calendar calEnd = (Calendar) cal.clone();

            bw.write(sdf.format(calStart.getTime()) + "～" + sdf.format(calEnd.getTime()) + "までの予約情報");
            bw.newLine();
            bw.newLine();
            bw.write("-----------------------------");
            bw.newLine();

            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getNextReservationSQL(calStart, calEnd));
            while (rs.next()) {
                cal.setTimeInMillis(rs.getTimestamp("reservation_datetime").getTime());
                bw.write(sdf.format(cal.getTime()) + "（" + fn_strWDay[cal.get(Calendar.DAY_OF_WEEK) - 1] + "）");
                bw.newLine();
                bw.write("[予約時間]" + sdf2.format(cal.getTime()));
                bw.newLine();
                bw.write("[顧客No]" + rs.getString("customer_no"));
                bw.newLine();
                bw.write("[顧客名]" + rs.getString("customer_name") + "（" + rs.getString("customer_kana") + "）");
                bw.newLine();
                bw.write("[予約内容]" + rs.getString("technic_name"));
                bw.newLine();
                // IVS SANG START INSERT 20131024
                bw.write("[コース契約]" + rs.getString("course_contract_name"));
                bw.newLine();
                bw.write("[消化コース]" + rs.getString("course_contract_digestion_name"));
                bw.newLine();
                // IVS SANG END INSERT 20131024
                bw.write("[主担当者]" + rs.getString("staff_name"));
                bw.write("（" + rs.getString("designated") + "）");
                bw.newLine();

                if (SystemInfo.getCurrentShop().isBed()) {
                    bw.write("[施術台]" + rs.getString("bed_name"));
                    bw.newLine();
                }

                bw.write("[予約メモ]");
                bw.newLine();
                bw.write(rs.getString("comment"));
                bw.newLine();
                bw.write("-----------------------------");
                bw.newLine();
            }
            rs.close();

            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNextReservationSQL(Calendar calStart, Calendar calEnd) {

        MstShop shop = SystemInfo.getCurrentShop();

        calStart.set(Calendar.HOUR_OF_DAY, shop.getOpenHour());
        calStart.set(Calendar.MINUTE, shop.getOpenMinute());
        calEnd.add(Calendar.DAY_OF_MONTH, 1);
        calEnd.set(Calendar.HOUR_OF_DAY, shop.getOpenHour());
        calEnd.set(Calendar.MINUTE, shop.getOpenMinute());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      t.*");
        sql.append("     ,array_to_string(array(");
        sql.append("         select");
        sql.append("             mt.technic_name");
        sql.append("         from");
        sql.append("             data_reservation_detail drd");
        // IVS SANG START EDIT 20131024
        //sql.append("                 left join mst_technic mt");
        sql.append("                 inner join mst_technic mt");
        // IVS SANG END EDIT 20131024
        sql.append("                 using(technic_id)");
        sql.append("         where ");
        // IVS SANG START INSERT 20131024
        sql.append("                 drd.course_flg is null and \n");
        // IVS SANG END INSERT 20131024
        sql.append("                 drd.delete_date is null");
        sql.append("             and drd.shop_id = t.shop_id");
        sql.append("             and drd.reservation_no = t.reservation_no");
        sql.append("         order by");
        sql.append("             drd.reservation_datetime");
        sql.append("     ), '、') as technic_name");
        sql.append("     ,coalesce(array_to_string(array(");
        sql.append("         select");
        sql.append("             mb.bed_name");
        sql.append("         from");
        sql.append("             data_reservation_detail drd");
        sql.append("                 join mst_bed mb");
        sql.append("                 using(shop_id, bed_id)");
        sql.append("         where ");
        sql.append("                 drd.delete_date is null");
        sql.append("             and drd.shop_id = t.shop_id");
        sql.append("             and drd.reservation_no = t.reservation_no");
        sql.append("         order by");
        sql.append("             drd.reservation_datetime");
        sql.append("     ), '、'), '') as bed_name,\n");
        // IVS SANG START INSERT 20131024

        sql.append("coalesce(array_to_string(array( \n");
        sql.append("    select             mc.course_name \n");
        sql.append("    from             data_reservation_detail drd \n");
        sql.append("        inner join mst_course mc on drd.technic_id = mc.course_id \n");
        sql.append("    where   drd.course_flg = 1 and \n");
        sql.append("        drd.delete_date is null\n");
        sql.append("        and drd.shop_id = t.shop_id \n");
        sql.append("        and drd.reservation_no = t.reservation_no \n");
        sql.append("    order by             drd.reservation_datetime     ), '、'), '') as course_contract_name, \n");

        sql.append("coalesce(array_to_string(array(  \n");
        sql.append("    select             mc.course_name   \n");
        sql.append("    from             data_reservation_detail drd   \n");
        sql.append("        inner join mst_course mc on drd.technic_id = mc.course_id \n");
        sql.append("    where   drd.course_flg = 2 and \n");
        sql.append("        drd.delete_date is null \n");
        sql.append("        and drd.shop_id = t.shop_id  \n");
        sql.append("        and drd.reservation_no = t.reservation_no  \n");
        sql.append("    order by             drd.reservation_datetime     ), '、'), '') as course_contract_digestion_name \n");
        // IVS SANG END INSERT 20131024
        sql.append(" from");
        sql.append(" (");
        sql.append("     select");
        sql.append("          dr.shop_id");
        sql.append("         ,dr.reservation_no");
        sql.append("         ,min(drd.reservation_datetime) as reservation_datetime");
        sql.append("         ,min(mc.customer_no) as customer_no");
        sql.append("         ,min(mc.customer_name1 || '　' || mc.customer_name2) as customer_name");
        sql.append("         ,min(mc.customer_kana1 || '　' || mc.customer_kana2) as customer_kana");
        sql.append("         ,coalesce(min(ms.staff_name1 || '　' || ms.staff_name2), '担当者なし') as staff_name");
        sql.append("         ,min(case when dr.designated_flag then '指' else 'F' end) as designated");
        sql.append("         ,min(dr.comment) as comment");
        sql.append("     from");
        sql.append("         data_reservation dr");
        sql.append("             join data_reservation_detail drd");
        sql.append("             using(shop_id, reservation_no)");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = dr.customer_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dr.staff_id");
        sql.append("     where");
        sql.append("             dr.delete_date is null");
        sql.append("         and drd.delete_date is null");
        sql.append("         and dr.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("         and drd.reservation_datetime >= " + SQLUtil.convertForSQL(calStart));
        sql.append("         and drd.reservation_datetime < " + SQLUtil.convertForSQL(calEnd));
        sql.append("         and dr.status in (1)");
        sql.append("     group by");
        sql.append("          dr.shop_id");
        sql.append("         ,dr.reservation_no");
        sql.append(" ) t");
        sql.append(" order by");
        sql.append("      reservation_datetime");
        sql.append("     ,reservation_no");

        return sql.toString();
    }

    /**
     * メニュー用タイマーを初期化する。
     */
    private void initTimer() {
        timer = new javax.swing.Timer(MENU_TIMER_DELAY,
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        repaintMainMenu();
                    }
                });

        timer.start();
    }

    /**
     * コンテンツのフォーカスリスナーを初期化する。
     */
    private void initContentsFocusListener() {
        //フォーカスを取得したとき
        fa = new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hideSubMenu();
            }
        };

        //キーが押されたとき
        ka = new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hideSubMenu();
            }
        };

        //マウスがクリックされたとき
        ma = new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hideSubMenu();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
            }
        };
    }

    /**
     * コンポーネントの子コンポーネントにリスナーを追加する。
     *
     * @param c コンポーネント
     */
    private void addChildrenListener(Component c) {
        //既に追加済の場合は処理を抜ける
        for (FocusListener fl : c.getFocusListeners()) {
            if (fl.equals(fa)) {
                return;
            }
        }

        c.addFocusListener(fa);
        c.addKeyListener(ka);
        c.addMouseListener(ma);

        if (c instanceof Container) {
            Container con = (Container) c;
            for (Component cc : con.getComponents()) {
                addChildrenListener(cc);
            }
        }
    }

    /**
     * 画像のボタンを作成する。
     *
     * @param name 名前
     * @param defaultIconPath 標準の画像のパス
     * @param rolloverIconPath ロールオーバー時の画像のパス
     * @param disabledIconPath 使用不可能時の画像のパス
     * @param isHideSubMenu クリック時にサブメニューを閉じるか。
     * @return 画像のボタン
     */
    private JButton createImageButton(String name,
            String defaultIconPath,
            String rolloverIconPath,
            String disabledIconPath,
            boolean isHideSubMenu) {
        JButton button = SwingCreator.createImageButton(
                null, defaultIconPath, rolloverIconPath, disabledIconPath);

        //マウスカーソルを変更するイベントリスナーを追加
        SystemInfo.addMouseCursorChange(button);

        //クリック時にサブメニューを閉じる場合
        if (isHideSubMenu) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    hideSubMenu();;
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return button;
    }

    /**
     * メインメニューのボタンにイベントリスナーを追加する。
     *
     * @param button イベントリスナーを追加するボタン
     */
    private void addMainMenuButtonEvent(final MainMenuButton button) {
        button.getButton().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (button.isSubMenuOpen()) {
                    hideSubMenu();
                    timer.start();
                    closeButton = button;
                } else {
                    hideSubMenu();
                    timer.start();
                    closeButton = openedButton;
                    openButton = button;
                }
            }
        });
    }

    /**
     * 顧客管理ボタンを取得する。
     *
     * @return 顧客管理ボタン
     */
    private MainMenuButton getCustomerButton() {
        if (customerButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                customerButton = new MainMenuButton(this.createImageButton(
                        "閉店業務",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/closed_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/closed_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                customerButton = new MainMenuButton(this.createImageButton(
                        "閉店業務",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/closed_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/closed_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(customerButton);
                customerButton.addSubMenuButton(this.getSearchAccountButton());//
                customerButton.addSubMenuButton(this.getRegisterButton());//

                if (SystemInfo.checkAuthority(61)) {
                    customerButton.addSubMenuButton(this.getBusinessReportButton());
                }
				// GBO add 20161110
				//モデスティ判定処理
				if (( SystemInfo.getDatabase().indexOf(DB_POS_HAIR_PRIMO) > -1) || ( SystemInfo.getDatabase().indexOf(DB_POS_HAIR_DEV) > -1)) {
					customerButton.addSubMenuButton(this.getCustomCutoffReportButton());
				}
            }

        }

        return customerButton;
    }

    /**
     * 予約管理ボタンを取得する。
     *
     * @return 予約管理ボタン
     */
    private MainMenuButton getReservationButton() {
        if (reservationButton == null) {
            reservationButton = new MainMenuButton(this.createImageButton(
                    "フロント業務",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/front_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/front_on.jpg",
                    null,
                    true),
                    SystemInfo.getMenuColor());

            this.addMainMenuButtonEvent(reservationButton);

            // 予約表
            reservationButton.addSubMenuButton(this.getRegistReservatioButton());

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                // フロント
                reservationButton.addSubMenuButton(this.getShowStatusButton());

                // スキップお会計（本部では表示しない）
                if (SystemInfo.getCurrentShop().getShopID() != 0) {
                    reservationButton.addSubMenuButton(this.getInputAccountButton());
                }
            }
            //VTAn Start add 20140612
            if (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev")) {
                reservationButton.addSubMenuButton(this.getShowDeliverStatusButton());
            }
            //VTAn End add 20140612
        }

        return reservationButton;
    }

    /**
     * 精算管理ボタンを取得する。
     *
     * @return 精算管理ボタン
     */
    private MainMenuButton getAccountButton() {
        if (accountButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                accountButton = new MainMenuButton(this.createImageButton(
                        "レジ金管理",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cash_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cash_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                accountButton = new MainMenuButton(this.createImageButton(
                        "レジ金管理",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/casher_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/casher_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(accountButton);

                accountButton.addSubMenuButton(this.getRegisterCashButton());

                //IVS_LVTu start edit 2015/07/29 New request #41101
                //if (SystemInfo.getCurrentShop().getShopID() != 0) {
                    accountButton.addSubMenuButton(this.getReceivableListButton());
                //}
                //IVS_LVTu end edit 2015/07/29 New request #41101

                if (SystemInfo.checkAuthority(60)) {
                    accountButton.addSubMenuButton(this.getRegisterCashIOButton());
                }

                // TODO
                if (SystemInfo.getDatabase().startsWith("pos_hair_ox2") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
                    accountButton.addSubMenuButton(this.getBaseCashButton());
                }

            }

        }

        return accountButton;
    }

    /**
     * 帳票出力ボタンを取得する。
     *
     * @return 帳票出力ボタン
     */
    private MainMenuButton getReportButton() {
        if (reportButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                reportButton = new MainMenuButton(this.createImageButton(
                        "分析帳票",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                reportButton = new MainMenuButton(this.createImageButton(
                        "分析帳票",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(reportButton);

//                // 顧客分析
//                if (SystemInfo.checkAuthority(62)) {
//                    boolean isVisible = false;
//                    isVisible = isVisible || SystemInfo.checkAuthority(70);
//                    isVisible = isVisible || SystemInfo.checkAuthority(71);
//                    isVisible = isVisible || SystemInfo.checkAuthority(72);
//                    isVisible = isVisible || SystemInfo.getSetteing().isItoAnalysis();
//                    if (isVisible) {
//                        reportButton.addSubMenuButton(this.getCustomerEffectButton());
//                    }
//                }

                //Start add 20220502 TUNEオリジナル帳票出力 の対応
                if (SystemInfo.getDatabase().equals("pos_hair_dev")
                        || SystemInfo.getDatabase().startsWith("pos_hair_tune")) {
                    reportButton.addSubMenuButton(this.getCustomizeReportmButton());
                }
                //End add 20220502 TUNEオリジナル帳票出力 の対応
                
                // 売上分析
                if (SystemInfo.checkAuthority(63)) {
                    boolean isVisible = false;
                    isVisible = isVisible || SystemInfo.checkAuthority(80);
                    isVisible = isVisible || SystemInfo.checkAuthority(81);
                    isVisible = isVisible || SystemInfo.getSetteing().isItoAnalysis();
                    if (isVisible) {
                        reportButton.addSubMenuButton(this.getSalesEffectButton());
                    }
                }

                // スタッフ成績
                if (SystemInfo.checkAuthority(64)) {
                    for (int i = 90; i <= 94; i++) {
                        if (SystemInfo.checkAuthority(i)) {
                            reportButton.addSubMenuButton(this.getStaffResultButton());
                            break;
                        }
                    }
                }

                //Thanh add start 20130129 カスタム帳票
                if (SystemInfo.getDatabase().equals("pos_hair_nons")
                        || SystemInfo.getDatabase().equals("pos_hair_nons_bak")
                        || SystemInfo.getDatabase().equals("pos_hair_dev")
                        || SystemInfo.getDatabase().equals("pos_hair_ox2")) {
                    reportButton.addSubMenuButton(this.getCustomButton());
                }
                //Thanh add start 20130129 カスタム帳

				// 20161227 add start オリジナル帳票出力画面
                if (SystemInfo.getDatabase().equals("pos_hair_nonail")
                        || SystemInfo.getDatabase().equals("pos_hair_nonailm")
                        || SystemInfo.getDatabase().equals("pos_hair_nonailf")
                        || SystemInfo.getDatabase().equals("pos_hair_nonail_dev")) {
                    reportButton.addSubMenuButton(this.getOriginalReportButton());
                }
                // 20161227 add end オリジナル帳票出力画面

                // Luc add start 20121004
                if (!SystemInfo.getSystemType().equals(2)) {
                reportButton.addSubMenuButton(this.getStoreButton());
                }
                //Luc add End 20121004

                // 再来分析
                if (SystemInfo.checkAuthority(65)) {
                    reportButton.addSubMenuButton(this.getReappearanceEffectButton());
                }

//                // ランキング
//                if (SystemInfo.checkAuthority(66)) {
//                    for (int i = 100; i <= 105; i++) {
//                        if (SystemInfo.checkAuthority(i)) {
//                            reportButton.addSubMenuButton(this.getRankingButton());
//                            break;
//                        }
//                    }
//                }
                //Son add End 20130328 反響分析
                // 反響分析
                if (0 < this.getRepeatAnalysisMenu().getComponentCount()) {
                    reportButton.addSubMenuButton(this.getRepeatAnalysisButton());
                }

                if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_charm")) {
                    reportButton.addSubMenuButton(this.getCustomReportSubButton());    // カスタム帳票
                }

                // Thanh add start 20130514 売上分析表
                //reportButton.addSubMenuButton(this.getTurnOverAnalyzeButton());
                // Thanh add End 20130514 店舗分析
                // vtbphuong start add 20140708 役務帳票
                if (SystemInfo.getCurrentShop().getShopID() != 0 && SystemInfo.getCurrentShop().getCourseFlag() != null && SystemInfo.getCurrentShop().getCourseFlag() == 1) {
                    reportButton.addSubMenuButton(this.getServiceReportButton());
                } else if (SystemInfo.getCurrentShop().getShopID() == 0) {
                    reportButton.addSubMenuButton(this.getServiceReportButton());
                }
                // vtbphuong end add  20140708 役務帳票
                //IVS_nahoang GB_Mashu_目標設定 Start add 20141021
                //nhanvt start 20150210 New request #35189
                //nhanvt start 20150310 New request #35475
                //IVS_LVTu start edit 2015/10/08 New request #43421
                if ((SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_dev"))
                        || (SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_mashu_dev"))
                        || (SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_mashu"))
                        //Luc start delete 20151209 #45167
                        //IVS_LVTu start edit 2016/03/04 New request #48976
                        || (SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_lhab"))
                        //IVS_LVTu end edit 2016/03/04 New request #48976
                        //Luc end delete 20151209 #45167
                        ) {
                    reportButton.addSubMenuButton(this.getItemTargetSettingButton());

                }
                //IVS_LVTu end edit 2015/10/08 New request #43421
                //nhanvt end 20150310 New request #35475
                //nhanvt end 20150210 New request #35189
                //IVS_nahoang GB_Mashu_目標設定 End add
                // 分析帳票⇒効果指標数値分析
                //nhanvt start 20150210 New request #35189
                if ((SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_dev"))) {
                    reportButton.addSubMenuButton(this.getEffectIndicatorAnalysisButton());
                }
                //nhanvt end 20150210 New request #35189
                // 分析帳票⇒過去最大実績集計
                if (!SystemInfo.getSystemType().equals(2)) {
                reportButton.addSubMenuButton(this.getPostMaxResultsButton());
                }

                //IVS_LVTu start add 2016/03/08 New request #48811
                // ポイント集計
                if (SystemInfo.isUsePointcard() || SystemInfo.getSetteing().isUsePrepaid()) {
                    reportButton.addSubMenuButton(this.getPointReportButton());
                }
                //IVS_LVTu end add 2016/03/08 New request #48811

            }
        }

        return reportButton;
    }

    //Thanh start add 2013/06/03
    /**
     * 帳票出力ボタンを取得する。
     *
     * @return 帳票出力ボタン
     */
    private MainMenuButton getAnaliticCustomerButton() {
        if (analiticCustomerButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {
                analiticCustomerButton = new MainMenuButton(this.createImageButton(
                        "顧客分析",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());
            } else {
                analiticCustomerButton = new MainMenuButton(this.createImageButton(
                        "顧客分析",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(analiticCustomerButton);

                //動向分析
                analiticCustomerButton.addSubMenuButton(this.getReappearancePredictionEffectButton());

                //クロス分析
                if (!SystemInfo.getSystemType().equals(2)) {
                analiticCustomerButton.addSubMenuButton(this.getCrossAnalysisEffectButton());

                //クロス分析5
                analiticCustomerButton.addSubMenuButton(this.getCrossAnalysis5EffectButton());
                }

                //Luc Add Start 20121004 エリア分析
                //analiticCustomerButton.addSubMenuButton(this.getCustomerAreaAnalysisButton());
                //Luc Add End 20121004 エリア分析

                //Luc Add Start 20121004 顧客属性分析
                if (!SystemInfo.getSystemType().equals(2)) {
                analiticCustomerButton.addSubMenuButton(this.getCustomerAnalysisAttributeButton());
                }
                //Luc Add End 20121004 顧客属性分析

                //カルテ分析
                if (SystemInfo.getSetteing().isItoAnalysis()) {
                    analiticCustomerButton.addSubMenuButton(this.getItoAnalitic3jigenButton());
                }

                //Thanh Add Start 20130415 顧客契約履歴
                if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_dev") || SystemInfo.getDatabase().startsWith("pos_hair_dev2") || SystemInfo.getDatabase().startsWith("pos_hair_nons_bak")) {
                    analiticCustomerButton.addSubMenuButton(this.getCusContractHistoryButton());
                }
                //Thanh Add End 20130415 顧客契約履歴
            //GB_Mashu Task #34581 [Product][Code][Phase3]マッシャー分析
            //IVS_nahoang start add 20141229
            //nhanvt start 20150210 New request #35189
            if ((SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_dev"))
                    //IVS_LVTu start edit 2015/04/24 New request #36475
                        //|| (SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_mashu_dev"))
                    || (SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_mashu"))
                    //IVS_LVTu end edit 2015/04/24 New request #36475
                        ) {
                analiticCustomerButton.addSubMenuButton(this.getMasherAnalysis5PanelButton());
            }
            //nhanvt end 20150210 New request #35189
            //IVS_nahoang end add 20141229
            }
        }

        return analiticCustomerButton;
    }

    private MainMenuButton getMenuRankingButton() {
        if (menuRankingButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {
                menuRankingButton = new MainMenuButton(this.createImageButton(
                        "ランキング",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());
            } else {
                menuRankingButton = new MainMenuButton(this.createImageButton(
                        "ランキング",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());
                this.addMainMenuButtonEvent(menuRankingButton);

                // ランキング
                if (SystemInfo.checkAuthority(66)) {
                    for (int i = 100; i <= 105; i++) {
                        if (SystemInfo.checkAuthority(i)) {
                            menuRankingButton.addSubMenuButton(this.getRankingButton());
                            break;
                        }
                    }
                }

                // ランキング⇒来店理由ランキング
                //nhanvt start edit 20150212 New request #35189
                if ((SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_dev")))
                {
                        menuRankingButton.addSubMenuButton(this.getReasonsRankButton());
                }
                //nhanvt end edit 20150212 New request #35189
                // ランキング⇒商品売上ランキング
                if (!SystemInfo.getSystemType().equals(2)) {
                menuRankingButton.addSubMenuButton(this.getItemSalesRankingButton());
                }
            }
        }

        return menuRankingButton;
    }
    //Thanh end add 2013/06/03

    /**
     * メール機能ボタンを取得する。
     *
     * @return メール機能ボタン
     */
    private MainMenuButton getMailButton() {
        if (mailButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                mailButton = new MainMenuButton(this.createImageButton(
                        "来店促進",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/promotiton_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/promotiton_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                mailButton = new MainMenuButton(this.createImageButton(
                        "来店促進",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/promotion_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/promotiton_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(mailButton);


                mailButton.addSubMenuButton(this.getConditionedSearchButton());
                mailButton.addSubMenuButton(this.getDmHistoryButton());

            }

        }

        return mailButton;
    }

    /**
     * 帳票管理ボタンを取得する。
     *
     * @return 帳票管理ボタン
     */
    private MainMenuButton getReportManageButton() {
        if (reportManageButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                reportManageButton = new MainMenuButton(this.createImageButton(
                        "帳票管理",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/form_management_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/form_management_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                reportManageButton = new MainMenuButton(this.createImageButton(
                        "帳票管理",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/form_management_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/form_management_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(reportManageButton);


                reportManageButton.addSubMenuButton(this.getItemStockListButton());

            }

        }

        return reportManageButton;
    }

    /**
     * 基本設定ボタンを取得する。
     *
     * @return 基本設定ボタン
     */
    private MainMenuButton getMasterButton() {
        if (masterButton == null) {
            masterButton = new MainMenuButton(this.createImageButton(
                    "基本設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/base_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/base_on.jpg",
                    null,
                    true),
                    /*
                     "基本設定",
                     "/images/" + SystemInfo.getSkinPackage() + "/menu/main/master_off.jpg",
                     "/images/" + SystemInfo.getSkinPackage() + "/menu/main/master_on.jpg",
                     null,
                     true),
                     */
                    SystemInfo.getMenuColor());

            this.addMainMenuButtonEvent(masterButton);

            if (0 < this.getCompanyMasterMenu().getComponentCount()) {
                masterButton.addSubMenuButton(this.getCompanyMasterButton());
            }

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                if (0 < this.getItemMasterMenu().getComponentCount()) {
                    masterButton.addSubMenuButton(this.getItemMasterButton());
                }
            }

            if (0 < this.getTechnicMasterMenu().getComponentCount()) {
                masterButton.addSubMenuButton(this.getTechnicMasterButton());
            }

            //顧客関連
            if (0 < this.getCustomerMasterMenu().getComponentCount()) {
                masterButton.addSubMenuButton(this.getCustomerMasterButton());
            }

            //IVS_LVTu start add 2018/13/05 GB_Finc
            if(SystemInfo.useCourse()) {
                if (0 < this.getManageMemberMasterMenu().getComponentCount()) {
                    masterButton.addSubMenuButton(this.getManageMemberMonButton());
                }
            }
            //IVS_LVTu end add 2018/13/05 GB_Finc

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                if (0 < this.getAccountMasterMenu().getComponentCount()) {
                    masterButton.addSubMenuButton(this.getAccountMasterButton());
                }
            }

            //メール関連
            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                if (0 < this.getMailMasterMenu().getComponentCount()) {
                    masterButton.addSubMenuButton(this.getMailMasterButton());
                }
            }

            // ポイントカード関連
            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                if (SystemInfo.isUsePointcard()) {
                    masterButton.addSubMenuButton(this.getPointcardSubButton());
                }
            }

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                //masterButton.addSubMenuButton(this.getCsvImportButton());
                masterButton.addSubMenuButton(this.getSettingButton());
            }

			// 2016/09/13 GB MOD Start #54560
			// アクア・グラツィエ様用 CSVインポート機能有効化
			// 2017/01/12 GB MOD Start #59343
//            if (SystemInfo.getDatabase().equals(DB_POS_HAIR_ACQUAGRZ) || SystemInfo.getDatabase().equals(DB_POS_HAIR_DEV)) {
//                masterButton.addSubMenuButton(this.getCsvImportButton());
//			}
			if (SystemInfo.getDatabase().equals(DB_POS_HAIR_ACQUAGRZ) || SystemInfo.getDatabase().equals(DB_POS_HAIR_DEV)
					|| SystemInfo.getLoginID().substring(0, 2).equals("gb")) {
                masterButton.addSubMenuButton(this.getCsvImportButton());
			}
			// 2017/01/12 GB MOD End #59343
			// 2016/09/13 GB MOD End #54560


            // Start add 20160825 nhtvinh
            if (null != SystemInfo.getUserAPI() && !SystemInfo.getUserAPI().equals(0) && SystemInfo.getCurrentShop().getShopID() != 0) {
                // IVS_PTQUANG start add 2016/09/05 New request #54177
                boolean flag = visibleControl();
                if (!(flag)) {
                    masterButton.addSubMenuButton(this.getItemSiteControlIntroduction());
                }
                 // IVS_PTQUANG end add 2016/09/05 New request #54177
            }
            // End add 20160825 nhtvinh
            if (!SystemInfo.getUserAPI().equals(0)) {
                masterButton.addSubMenuButton(this.getSettingKANZASHIButton());
            }

            // Start add 20130513 nakhoa
            if (SystemInfo.getDatabase().equals("pos_hair_ox2") || SystemInfo.getDatabase().equals("pos_hair_dev")) {
                masterButton.addSubMenuButton(this.getCustomSettingTargetButton());    // カスタム設定
            }
            // End add 20130513 nakhoa

            if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_charm") || SystemInfo.getDatabase().startsWith("pos_hair_ox2")) {
                masterButton.addSubMenuButton(this.getCustomSettingButton());    // カスタム設定
            }
        }

        return masterButton;
    }

    /**
     * IVS_PTQUANG add 2016/09/05 New request #54177
     * @return
     */
    private boolean visibleControl(){
        int variableControl = 0;
        SiteControlIntroductionPanel site = new SiteControlIntroductionPanel(variableControl);
        return site.checkVisibleForMenu();
    }

    /**
     * 未使用デザインで追加 ヘルプボタンを取得する。
     *
     * @return ヘルプボタン
     */
    private MainMenuButton getHelpButton() {
        if (helpButton == null) {
            helpButton = new MainMenuButton(this.createImageButton(
                    "ヘルプ",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/help_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/help_on.jpg",
                    null,
                    true),
                    /*
                     "ヘルプ",
                     "/images/" + SystemInfo.getSkinPackage() + "/menu/main/help_off.jpg",
                     "/images/" + SystemInfo.getSkinPackage() + "/menu/main/help_on.jpg",
                     null,
                     true),

                     */
                    SystemInfo.getMenuColor());

            this.addMainMenuButtonEvent(helpButton);
        }

        return helpButton;
    }

    /**
     * メインメニューにボタンを追加する。
     *
     * @param menuButton 追加するボタン
     * @param x ボタンのX座標
     */
    private void addMainMenu(MainMenuButton menuButton) {
        menuPanel.add(menuButton);
        menuButton.setBounds(0,
                59 * menuPanel.getComponents().length,
                menuButton.getWidth(),
                menuButton.getHeight());
    }

    /**
     * タイトルを初期化する。
     */
    private void initTitle() {

        String demoStr = "";
        //IVS_LVTu start delete 2016/02/23 New request #48339
//        if (Login.registeredMacAddress.startsWith("tlimit")) {
//            try {
//                SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
//                demoStr = " - デモ版の使用期限は " + f.format(DateFormat.getDateInstance().parse(Login.registeredMacAddress.substring(7).replace("-", "/"))) + " までです。";
//            } catch (Exception e) {
//            }
//        }
        //IVS_LVTu end delete 2016/02/23 New request #48339

        setTitle("SOSIA POS SALON");

        //本部、店舗での変更
        if (SystemInfo.getCurrentShop().getShopID() == 0) {
            setTitle(getTitle() + " 本部システム" + demoStr);
            //本部用ロゴ
            topLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/topLogo_Head.jpg")));
        } else {
            setTitle(getTitle() + " [ " + SystemInfo.getCurrentShop().getShopName() + " ]" + demoStr);
            //店舗用ロゴ
            topLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/topLogo.jpg")));
        }

        this.title = getTitle();
    }

    public void setMessageTitle(String msg) {
        setTitle(this.title + msg);
    }

    /**
     * メインメニューを初期化する。
     */
    private void initMainMenu() {
        //予約管理
        this.addMainMenu(this.getReservationButton());

        //精算管理
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getAccountButton());
        }

        //顧客管理
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getCustomerButton());
        }

        //帳票出力
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getReportButton());
        }

        //顧客分析
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getAnaliticCustomerButton());
        }
        //ランキング
        // IVS_LVTu start add 20150303 Bug #35227
        if(!SystemInfo.getSystemType().equals(1)) {
            if(SystemInfo.checkAuthority66(66)){
                this.addMainMenu(this.getMenuRankingButton());
            }
        }
        // IVS_LVTu end add 20150303 Bug #35227

        //基本設定
        this.addMainMenu(this.getMasterButton());

        if (SystemInfo.getSetteing().isMerchandise()) {
            //商品管理
            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                this.addMainMenu(this.getProductButton());
            }
        }

        //ヘルプ
//		this.addMainMenu(this.getHelpButton());


        //メール機能
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getMailButton());
        }

        // 商品在庫数一覧についてメニュー位置の変更
	    // 商品管理⇒帳票⇒商品在庫数一覧 に移動
        // 「帳票管理」のメニュー自体は何も無しになるので表示しない
        // 帳票管理
        // this.addMainMenu(this.getReportManageButton());
    }

    /**
     * サブメニュー用のJPanelを作成する。
     *
     * @return サブメニュー用のJPanel
     */
    private ImagePanel createSubMenu() {
        ImagePanel subMenu = new ImagePanel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                g2d.setColor(SystemInfo.getMenuColor());
                g2d.drawRect(0, 0, this.getWidth(), this.getHeight() - 1);
                g2d.drawRect(1, 1, this.getWidth() - 2, this.getHeight() - 3);
                g2d.drawRect(2, 2, this.getWidth() - 4, this.getHeight() - 5);
                g2d.drawRect(3, 3, this.getWidth() - 6, this.getHeight() - 7);
                super.paintComponents(g2d);
            }
        };
        subMenu.setLayout(null);
        subMenu.setOpaque(false);
        subMenu.setSize(149, 8);

        return subMenu;
    }

    /**
     * サブメニューにボタンを追加する。
     *
     * @param subMenu サブメニュー
     * @param menuButton 追加するボタン
     * @param x ボタンのX座標
     */
    private void addSubMenu(JPanel subMenu, JButton menuButton, Integer authorityIndex) {
        if (0 <= authorityIndex && !SystemInfo.getAuthority().getAuthoryty(authorityIndex)) {
            return;
        }

        subMenu.add(menuButton);
        menuButton.setBounds(4,
                subMenu.getHeight() - 4,
                menuButton.getWidth(),
                menuButton.getHeight());
        subMenu.setSize(subMenu.getWidth(),
                subMenu.getHeight() + menuButton.getHeight());
    }

    /**
     * コンテンツを変更する。
     *
     * @param contents コンテンツ
     */
    public void changeContents(AbstractImagePanelEx contents) {
        if (isChanging) {
            return;
        }
        //IVS_LVTu start add 2015/05/12 New request #36645
        if (SystemInfo.flagChangeContents) {
            //Luc start edit 20160411 #49521
            if (MessageDialog.showYesNoDialog(this,
                            "レジ締め内容が保存されていませんがよろしいですか？",
                            "レジ管理",
                            JOptionPane.OK_CANCEL_OPTION) != 0) {
                return;
            } else {
                SystemInfo.flagChangeContents = false;
            }
            //Luc end edit 20160411 #49521

        }
        //IVS_LVTu end add 2015/05/12 New request #36645
        isChanging = true;

        for (Component c : contentsPanel.getComponents()) {
            contentsPanel.remove(c);

            if (c.isVisible()) {
                c = null;
            }
        }

        hideSubMenu();

        //menuArrow.setVisible(true);
        //元々のタイトル文字列表示
        //contentsName.setText((contents.getPath().equals("") ? "" : contents.getPath() + " >> " + contents.getTitle()));
        // ATGS 画像を切り替える
        contentsName.setIcon(new javax.swing.ImageIcon(getClass().getResource(this.getContentsTitlePath(contents.getTitle()))));

        contents.setParentFrame(this);
        addChildrenListener(contents);
        contentsPanel.add(contents);
        contents.setVisible(true);
        contents.setBounds(0, 0, contents.getWidth(), contents.getHeight());
        contentsPanel.updateUI();
        isChanging = false;

        System.gc();
    }

    /**
     * コンテンツタイトルのIconPathを返す タイトル画像に変更対応
     *
     * @param 元々のタイトル文字列
     * @return 画像パス文字列
     */
    private String getContentsTitlePath(String contentsTitle) {
        StringBuilder strPath = new StringBuilder();
        //画像パス作成
        strPath.append("/images/");
        strPath.append(SystemInfo.getSkinPackage());
        strPath.append("/menu/title/");

        //元々のタイトル名より画像選択
        if (contentsTitle.equals("予約登録")) {//元々のタイトル名
            strPath.append("title_f_re.jpg");//対応する画像名
        } else if (contentsTitle.equals("出退勤登録")) {
            strPath.append("title_work_staff.jpg");
        } else if (contentsTitle.equals("フロント")) {
            strPath.append("title_f_zai.jpg");
        } else if (contentsTitle.equals("レジ精算")) {
            strPath.append("title_f_kaikei.jpg");
        } else if (contentsTitle.equals("レジ出納帳")) {
            strPath.append("title_r_regi.jpg");
        } else if (contentsTitle.equals("小口項目登録")) {
            strPath.append("title_r_cash_class.jpg");
        } else if (contentsTitle.equals("小口詳細登録")) {
            strPath.append("title_r_cash_menu.jpg");
        } else if (contentsTitle.equals("小口現金管理")) {
            strPath.append("title_r_cash.jpg");
        } else if (contentsTitle.equals("売掛回収処理")) {
            strPath.append("title_r_urikake.jpg");
        } else if (contentsTitle.equals("売掛金回収")) {
            strPath.append("title_r_urikake_shori.jpg");
        } else if (contentsTitle.equals("レジ入出金管理表")) {
            strPath.append("title_r_month_r.jpg");
        } else if (contentsTitle.equals("伝票検索")) {
            strPath.append("title_c_den.jpg");
        } else if (contentsTitle.equals("レジ管理")) {
            strPath.append("title_c_regi.jpg");
        } else if (contentsTitle.equals("業務報告")) {
            strPath.append("title_c_gyoumu.jpg");
        } else if (contentsTitle.equals("動向分析")) {
            strPath.append("title_a_kokyaku_raiten.jpg");
        } else if (contentsTitle.equals("クロス分析")) {
            strPath.append("title_a_kokyaku_cross.jpg");
        } else if (contentsTitle.equals("クロス分析5")) {
            strPath.append("title_a_kokyaku_cross5.jpg");
        } else if (contentsTitle.equals("サイクル分析")) {
            strPath.append("title_a_karte_analitic.jpg");
        } else if (contentsTitle.equals("客数問題発見シートα")) {
            strPath.append("title_a_kokyaku_problem.jpg");
        } else if (contentsTitle.equals("売上集計")) {
            strPath.append("title_a_uriage_nitigi.jpg");
        } else if (contentsTitle.equals("時間帯分析")) {
            strPath.append("title_analitic_time.jpg");
        } else if (contentsTitle.equals("担当別成績一覧表")) {
            strPath.append("title_a_staff_list.jpg");
        } else if (contentsTitle.equals("担当別技術成績")) {
            strPath.append("title_a_staff_skill.jpg");
        } else if (contentsTitle.equals("担当別商品成績")) {
            strPath.append("title_a_staff_goods.jpg");
        } else if (contentsTitle.equals("担当別顧客成績")) {
            strPath.append("title_a_staff_customer.jpg");
        } else if (contentsTitle.equals("担当別再来成績")) {
            strPath.append("title_a_staff_repeater.jpg");
        } else if (contentsTitle.equals("再来分析")) {
            strPath.append("title_b_analitic_legersheet-return-analysis.jpg");
        } else if (contentsTitle.equals("ランキング")) {
            strPath.append("title_a_ranking.jpg");
        } else if (contentsTitle.equals("レスポンス分析")) {
            strPath.append("title_a_hankyo.jpg");
        } else if (contentsTitle.equals("条件検索")) {
            strPath.append("title_p_jyoken.jpg");
        } else if (contentsTitle.equals("DM作成履歴")) {
            strPath.append("title_dm_history.jpg");
        } else if (contentsTitle.equals("メール作成")) {
            strPath.append("title_p_jyoken_mail.jpg");
        } else if (contentsTitle.equals("ハガキ作成")) {
            strPath.append("title_p_jyoken_hagaki.jpg");
        } else if (contentsTitle.equals("宛名ラベル作成")) {
            strPath.append("title_p_jyoken_takku.jpg");
        } else if (contentsTitle.equals("会社情報登録")) {
            strPath.append("title_b_c_company.jpg");
        } else if (contentsTitle.equals("グループ情報登録")) {
            strPath.append("title_b_c_group.jpg");
        } else if (contentsTitle.equals("店舗情報登録")) {
            strPath.append("title_b_c_shop.jpg");
        } else if (contentsTitle.equals("権限登録")) {
            strPath.append("title_b_c_kengen.jpg");
        } else if (contentsTitle.equals("スタッフ区分登録")) {
            strPath.append("title_b_c_staff_kubun.jpg");
        } else if (contentsTitle.equals("スタッフ情報登録")) {
            strPath.append("title_b_c_staff.jpg");
        } else if (contentsTitle.equals("出退勤パスワード変更")) {
            strPath.append("title_b_c_password.jpg");
        } else if (contentsTitle.equals("施術台登録")) {
            strPath.append("title_b_c_bed.jpg");
        } else if (contentsTitle.equals("反響項目登録")) {
            strPath.append("title_b_c_res.jpg");
        } else if (contentsTitle.equals("基本シフト登録")) {
            strPath.append("title_b_k_shift.jpg");
        } else if (contentsTitle.equals("スタッフシフト登録")) {
            strPath.append("title_b_k_staff_shift.jpg");
        } else if (contentsTitle.equals("商品分類登録")) {
            strPath.append("title_b_products_syohin.jpg");
        } else if (contentsTitle.equals("商品登録")) {
            strPath.append("title_b_products_shohin2.jpg");
        } else if (contentsTitle.equals("使用商品登録")) {
            strPath.append("title_b_products_shiyo.jpg");
        } else if (contentsTitle.equals("仕入先登録")) {
            strPath.append("title_b_products_shiire.jpg");
        } else if (contentsTitle.equals("在庫調整区分登録")) {
            strPath.append("title_b_products_shiire_chosei.jpg");
        } else if (contentsTitle.equals("技術分類登録")) {
            strPath.append("title_b_tech_gijyutsu_bunrui.jpg");
        } else if (contentsTitle.equals("技術登録")) {
            strPath.append("title_b_tech_gijyutsu.jpg");
        } else if (contentsTitle.equals("按分マスタ登録")) {
            strPath.append("title_b_tech_anbun_mst.jpg");
        } else if (contentsTitle.equals("按分登録")) {
            strPath.append("title_b_tech_anbun.jpg");
        } else if (contentsTitle.equals("使用技術登録")) {
            strPath.append("title_b_tech_siyou.jpg");
        } else if (contentsTitle.equals("コース契約分類登録")) {
            strPath.append("title_b_course_class.jpg");
        } else if (contentsTitle.equals("コース登録")) {
            strPath.append("title_b_course.jpg");
        } else if (contentsTitle.equals("使用コース登録")) {
            strPath.append("title_b_course_use.jpg");
		//マスタ一括登録 add start 2016/12/28
        } else if (contentsTitle.equals("技術一括登録")) {
            strPath.append("title_technic_regist_bulk.jpg");
		//マスタ一括登録 add end 2016/12/28
        } else if (contentsTitle.equals("スタッフ施術時間登録")) {
            strPath.append("title_b_tech_staff.jpg");
        } else if (contentsTitle.equals("顧客情報登録")) {
            strPath.append("title_b_costmer_kokyaku.jpg");
        } else if (contentsTitle.equals("非会員一覧")) {
            strPath.append("title_b_custmer_dis_kaiin.jpg");
            //LUC start add 顧客属性分析 20121023
        } else if (contentsTitle.equals("顧客属性分析")) {
            strPath.append("title_b_kokyaku_area.jpg");
        } //LUC end add 顧客属性分析 20121023
        //LUC start add エリア分析 20121023
        else if (contentsTitle.equals("エリア分析")) {
            strPath.append("title_analitic_customerarea.jpg");
        } //LUC end add エリア分析 20121023
        else if (contentsTitle.equals("店舗分析")) {
            strPath.append("title_b_analitic_shop.jpg");
        } else if (contentsTitle.equals(
                "ケータイ会員一覧")) {
            strPath.append("title_b_mobile_cust_list.jpg");
        } else if (contentsTitle.equals(
                "職業登録")) {
            strPath.append("title_b_costmer_syokugyo.jpg");
        } else if (contentsTitle.equals(
                "フリー項目区分登録")) {
            strPath.append("title_b_costmer_free_kubun.jpg");
        } else if (contentsTitle.equals(
                "フリー項目登録")) {
            strPath.append("title_b_costmer_free.jpg");
        } else if (contentsTitle.equals(
                "消費税登録")) {
            strPath.append("title_b_account_tax.jpg");
        } else if (contentsTitle.equals(
                "支払方法登録")) {
            strPath.append("title_b_account_shiharai.jpg");
        } else if (contentsTitle.equals(
                "割引種別登録")) {
            strPath.append("title_b_account_waribiki.jpg");
        } else if (contentsTitle.equals(
                "レシート設定")) {
            strPath.append("title_b_account_receipt.jpg");
        } else if (contentsTitle.equals(
                "テンプレート分類登録")) {
            strPath.append("title_b_mail_template_bunrui.jpg");
        } else if (contentsTitle.equals(
                "テンプレート登録")) {
            strPath.append("title_b_mail_template.jpg");
        } else if (contentsTitle.equals(
                "署名登録")) {
            strPath.append("title_b_mail_syomei.jpg");
        } else if (contentsTitle.equals(
                "CSVインポート")) {
            strPath.append("title_b_csv_in.jpg");
        } else if (contentsTitle.equals(
                "CSVエクスポート")) {
            strPath.append("title_b_exp.jpg");
        } else if (contentsTitle.equals(
                "環境設定")) {
            strPath.append("title_b_kankyo.jpg");
        } else if (contentsTitle.equals(
                "初回来店動機登録")) {
            strPath.append("title_b_costmer_f-motive.jpg");
        } else if (contentsTitle.equals(
                "顧客ランク設定")) {
            strPath.append("title_b_customer_rank.jpg");
        } else if (contentsTitle.equals(
                "カルテ分類登録")) {
            strPath.append("title_b_costmer_grouping-chart.jpg");
        } else if (contentsTitle.equals(
                "カルテ詳細登録")) {
            strPath.append("title_b_costmer_detail-chart.jpg");
        } else if (contentsTitle.equals(
                "カルテ参照登録")) {
            strPath.append("title_b_costmer_ref[1].item-chart.jpg");
        } else if (contentsTitle.equals(
                "置場登録")) {
            strPath.append("title_b_products_locater_entry.jpg");
        } else if (contentsTitle.equals(
                "ポイント計算式登録")) {
            strPath.append("title_b_card_count-setup.jpg");
        } else if (contentsTitle.equals(
                "カードテンプレート登録")) {
            strPath.append("title_b_card_template.jpg");
        } else if (contentsTitle.equals(
                "初期設定画面")) {
            strPath.append("title_b_default.jpg");
        } else if (contentsTitle.equals(
                "平均材料比率(TOM)")) {
            strPath.append("title_b_marcherdise_ledgersheet-custom_ingredients-rate-years.jpg");
        } else if (contentsTitle.equals(
                "合計棚卸表")) {
            strPath.append("title_b_marcherdise_ledgersheet_stock-take-totallist.jpg");
        } else if (contentsTitle.equals(
                "合計棚卸表(TOM)")) {
            strPath.append("title_b_marcherdise_ledgersheet-custom_stock-take-totallist.jpg");
        } else if (contentsTitle.equals(
                "材料比率(TOM)")) {
            strPath.append("title_b_marcherdise_ledgersheet-custom_stock-totallist.jpg");
        } else if (contentsTitle.equals(
                "入出庫一覧表")) {
            strPath.append("title_b_marcherdise_ledgersheet_list-input-output.jpg");
        } else if (contentsTitle.equals(
                "置場別在庫票")) {
            strPath.append("title_b_marcherdise_ledgersheet_locaterlist.jpg");
        } else if (contentsTitle.equals(
                "返品一覧表")) {
            strPath.append("title_b_marcherdise_ledgersheet_return-list.jpg");
        } else if (contentsTitle.equals(
                "スタッフ販売明細")) {
            strPath.append("title_b_marcherdise_ledgersheet_staff-sales-check.jpg");
        } else if (contentsTitle.equals(
                "棚卸表")) {
            strPath.append("title_b_marcherdise_ledgersheet_stock-list.jpg");
        } else if (contentsTitle.equals(
                "発注書作成")) {
            strPath.append("title_b_marcherdise_order-drawup.jpg");
        } else if (contentsTitle.equals(
                "入庫伝票作成")) {
            strPath.append("title_b_marcherdise_in-check.jpg");
        } else if (contentsTitle.equals(
                "出庫伝票作成")) {
            strPath.append("title_b_marcherdise_out-check.jpg");
        } else if (contentsTitle.equals(
                "スタッフ販売履歴")) {
            strPath.append("title_b_marcherdise_staff-sales_career.jpg");
        } else if (contentsTitle.equals(
                "スタッフ販売入力")) {
            strPath.append("title_b_marcherdise_staff-sales_input.jpg");
        } else if (contentsTitle.equals(
                "棚卸")) {
            strPath.append("title_b_marcherdise_stock-take.jpg");
        } else if (contentsTitle.equals(
                "店舗間移動")) {
            strPath.append("title_b_marcherdise_between-shop.jpg");
        } else if (contentsTitle.equals(
                "日計売上")) {
            strPath.append("title_b_analitic_legersheet-custom_daily-totalsales.jpg");
        } else if (contentsTitle.equals(
                "月間売上")) {
            strPath.append("title_b_analitic_legersheet-custom_monthly-totalsales.jpg");
        } else if (contentsTitle.equals(
                "カスタム帳票>>ランキング")) {
            strPath.append("title_b_analitic_legersheet-custom_ranking.jpg");
        } else if (contentsTitle.equals(
                "カスタム帳票>>再来分析")) {
            strPath.append("title_b_analitic_legersheet-custom_return-analysis.jpg");
        } else if (contentsTitle.equals(
                "売上分析")) {
            strPath.append("title_b_analitic_legersheet-custom_sales-analysis.jpg");
        } else if (contentsTitle.equals(
                "出勤登録")) {
            strPath.append("title_b_legersheet-custom_checkinwork.jpg");
        } else if (contentsTitle.equals(
                "在庫一覧確認画面")) {
            strPath.append("title_b_marcherdise_stock-check.jpg");
        } else if (contentsTitle.equals(
                "売上構成分析")) {
            strPath.append("title_ito_customer_analitics.jpg");
        } else if (contentsTitle.equals(
                "カルテ分析")) {
            strPath.append("title_ito_3d_customer_analitics.jpg");
        } else if (contentsTitle.equals(
                "自動メール設定")) {
            strPath.append("title_b_automail_setting.jpg");
        } else if (contentsTitle.equals(
                "顧客統合")) {
            strPath.append("title_b_customer_integration.jpg");
        } else if (contentsTitle.equals(
                "比較集計表")) {
            strPath.append("title_a_custom_sales_comparison.jpg");
        } else if (contentsTitle.equals(
                "来店カルテ分析")) {
            strPath.append("title_a_raiten_karte_report.jpg");
        } //Start add 20131102 lvut (merg rappa ->gb_source)
        else if (contentsTitle.equals("反響分類登録")) {
            strPath.append("title_k_response_class.jpg");
        } //End add 20131102 lvut (merg rappa ->gb_source)
        // IVS SANG START INSERT 20131209 [gbソース]商品受渡確認書のマージ
        else if (contentsTitle.equals("顧客別在庫管理")) {
            strPath.append("title_s_custmer_stock.jpg");
        } else if (contentsTitle.equals("商品宅配管理")) {
            strPath.append("title_s_item_delivery.jpg");
        } // endadd 20130319 nakhoa 商品宅配管理
        // IVS SANG END INSERT 20131209 [gbソース]商品受渡確認書のマージ
        //VTAn Start add 20140328 目標＆実績＆稼働日数
        else if (contentsTitle.equals("目標＆実績＆稼働日数")) {
            strPath.append("title_b_Target.jpg");
        } //VTAn End add 20140328 目標＆実績＆稼働日数
        else if (contentsTitle.equals("反響リピート分析")) {
            strPath.append("title_a_hankyo_repeat.jpg");
        } else if (contentsTitle.equals("顧客契約履歴")) {
            strPath.append("title_a_cus_contract_history.jpg");
        } //VTAN start add 売上推移表 20140422
        else if (contentsTitle.equals("売上推移表")) {
            strPath.append("title_a_custom_history.jpg");
        } else if (contentsTitle.equals("売上分析表")) {
            strPath.append("title_a_custom_sales_report.jpg");
        }//VTAN add 売上推移表 20140422
        //VTAN Start add 20140612
        //IVS_LTThuc start add 20140714
        else if (contentsTitle.equals("業態登録")) {
            strPath.append("title_k_category.jpg");
        }
        else if (contentsTitle.equals("配送ステータス管理")) {
            strPath.append("title_f_delivery_status.jpg");
        } //VTAN End add 20140612
        //IVS_nahoang GB_Mashu_目標設定 Start add 20141022
        else if (contentsTitle.equals("目標設定")) {
            strPath.append("title_a_target.jpg");
        } //IVS_nahoang GB_Mashu_目標設定 End add
        // vtbphuong start add 20140708
        else if (contentsTitle.equals("役務残一覧")) {
            strPath.append("title_b_service.jpg");
        }else if (contentsTitle.equals("有効期限一覧")) {
            strPath.append("title_b_service_expiration.jpg");
        } else if (contentsTitle.equals("解約一覧")) {
            strPath.append("title_b_service_cancel.jpg");
        } else if (contentsTitle.equals("効果指標数値分析")) {
        	strPath.append("title_form_management.jpg");
        } else if (contentsTitle.equals("来店理由ランキング")) {
        	strPath.append("title_reasons_rank.jpg");
        } else if (contentsTitle.equals("売れ筋ランキング")) {
        	strPath.append("title_selling_rank.jpg");
        //nhanvt start edit 20150210 New request #35190
        } else if (contentsTitle.equals("ギネス集計")) {
        	//strPath.append("title_highest_past_sales.jpg");
            strPath.append("title_a_past_max_record.jpg");
        //nhanvt end edit 20150210 New request #35190
        } else if (contentsTitle.equals("商品在庫一覧")) {
        	strPath.append("title_i_item_stock_list.jpg");
        //IVS_LVTu start add 20150109
        } else if (contentsTitle.equals("独自ランク分析")) {
        	strPath.append("title_c_rank.jpg");
        }
        //IVS_LVTu end add 20150109
        // vtbphuong end add 20140708
        else if (contentsTitle.equals("パスブック集計")) {
        	strPath.append("title_b_passbook.jpg");
        }
         //start add TMTrong on 20150706 New request #38963
        //IVS_LVTu start edit 2015/11/06 New request #44110
        else if(contentsTitle.equals("商品受渡一覧")){
            strPath.append("title_s_item_delivery_history.jpg");
        }else if(contentsTitle.equals("契約率集計")){
            strPath.append("title_b_contract_rate.jpg");
        }else if(contentsTitle.equals("提案書一覧")){
            strPath.append("title_b_proposal_list.jpg");
        }else if(contentsTitle.equals("ポイント集計")){
            strPath.append("title_b_analitic_point.jpg");
        }else if(contentsTitle.equals("ｻｲﾄｺﾝﾄﾛｰﾙ設定")){
            strPath.append("title_media_setting.jpg");
        }
        //IVS_LVTu end edit 2015/11/06 New request #44110
        //end add TMTrong on 20150706 New request #38963

        //IVS_NHTVINH start add 2016/08/25 New request #54237
        else if(contentsTitle.equals("ｻｲﾄｺﾝﾄﾛｰﾙ導入")){
            strPath.append("title_media_install.jpg");
        }
        //IVS_NHTVINH end add 2016/08/25 New request #54237

		// 20161227 add start オリジナル帳票出力画面
        else if(contentsTitle.equals("オリジナル帳票出力")){
            strPath.append("title_analitic_original_report.jpg");
        }
        // 20161227 add end オリジナル帳票出力画面

        // 20170413 add #61376
        else if(contentsTitle.equals("来店カルテ設定")) {
            strPath.append("title_visit_karte.jpg");
        }
        //IVS_LVTu start add 2018/13/05 GB_Finc
        else if(contentsTitle.equals("プラン登録")) {
            strPath.append("title_monthly_plan.jpg");
        }else if(contentsTitle.equals("月会員登録")) {
            strPath.append("title_monthly_customer.jpg");
        }else if(contentsTitle.equals("一括処理")) {
            strPath.append("title_monthly_batch.jpg");
        }else if(contentsTitle.equals("口座振替連携")) {
            strPath.append("title_monthly_relation.jpg");
        }
        //IVS_LVTu end add 2018/13/05 GB_Finc
        //Start add 20220502 TUNEオリジナル帳票出力 の対応
        else if(contentsTitle.equals("カスタム帳票")){
            strPath.append("title_b_custom_report.jpg");
        }
        //End add 20220502 TUNEオリジナル帳票出力 の対応

        else {//取得出来ない場合
            strPath.append("");//画像は出ない
        }

        return strPath.toString();
    }

    /**
     * 顧客登録ボタンを取得する。
     *
     * @return 顧客登録ボタン
     */
    private JButton getRegistCustomerButton() {
        if (registCustomerButton == null) {
            registCustomerButton = this.createImageButton(
                    "顧客情報登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/customer_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/customer_on.jpg",
                    null,
                    false);

            registCustomerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    registCustomerButton.getModel().setRollover(false);
                    MstCustomerPanel mcp = new MstCustomerPanel();
                    changeContents(mcp);
                }
            });
        }

        return registCustomerButton;
    }

    /**
     * 非会員一覧ボタンを取得する。
     *
     * @return 非会員一覧ボタン
     */
    private JButton getNotMemberListButton() {
        if (notMemberListButton == null) {
            notMemberListButton = this.createImageButton(
                    "非会員一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/not_member_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/not_member_on.jpg",
                    null,
                    false);
            /*
             "非会員一覧",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/not_member_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/not_member_on.jpg",
             null,
             false);

             */
            notMemberListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    NotMemberListPanel nmlp = new NotMemberListPanel();
                    changeContents(nmlp);
                }
            });
        }

        return notMemberListButton;
    }

    /**
     * ケータイ会員一覧ボタンを取得する。
     *
     * @return ケータイ会員一覧ボタン
     */
    private JButton getMobileMemberListButton() {
        if (mobileMemberListButton == null) {
            mobileMemberListButton = this.createImageButton(
                    "ケータイ会員一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/mobile_cust_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/mobile_cust_on.jpg",
                    null,
                    false);

            mobileMemberListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MobileMemberListPanel mmlp = new MobileMemberListPanel();
                    changeContents(mmlp);
                }
            });
        }

        return mobileMemberListButton;
    }

    /**
     * 再来状況ボタンを取得する。
     *
     * @return 再来状況ボタン
     */
    private JButton getRepeaterButton() {
        if (repeaterButton == null) {
            repeaterButton = this.createImageButton(
                    "再来状況",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/analitic_customer_circumstantial_repeat_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/analitic_customer_circumstantial_repeat_on.jpg",
                    null,
                    false);
            /*
             "再来状況",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/circumstantial_repeater_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/circumstantial_repeater_on.jpg",
             null,
             false);

             */
            repeaterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    HairRepeaterPanel hrp = new HairRepeaterPanel();
                    changeContents(hrp);
                }
            });
        }
        return repeaterButton;
    }

    /**
     * 予約登録ボタンを取得する。
     *
     * @return 予約登録ボタン
     */
    private JButton getRegistReservatioButton() {
        if (registReservatioButton == null) {

            registReservatioButton =
                    this.createImageButton(
                    "予約",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/reservation/front_reservation_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/reservation/front_resevation_on.jpg",
                    null,
                    true);

            registReservatioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    registReservatioButton.setCursor(null);

                    try {

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                        ReservationTimeTablePanel rrp = new ReservationTimeTablePanel();
                        changeContents(rrp);

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }

                }
            });
        }

        return registReservatioButton;
    }

    /**
     * フロントボタンを取得する。
     *
     * @return フロントボタン
     */
    private JButton getShowStatusButton() {
        if (showStatusButton == null) {

            showStatusButton =
                    this.createImageButton(
                    "フロント",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/reservation/front_visit_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/reservation/front_visit_on.jpg",
                    null,
                    true);

            showStatusButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    showStatusButton.setCursor(null);

                    try {

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                        ReservationStatusPanel rsp = new ReservationStatusPanel();
                        changeContents(rsp);

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            });
        }

        return showStatusButton;
    }

    //VTAn Start add 20140612
    private JButton getShowDeliverStatusButton() {
        if (showDeliverStatusButton == null) {

            showDeliverStatusButton =
                    this.createImageButton(
                    "配送ステータス管理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/delivery_status_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/delivery_status_on.jpg",
                    null,
                    true);

            showDeliverStatusButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    showStatusButton.setCursor(null);

                    try {

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                        DeliveryStatusManagementPanel dsmp = new DeliveryStatusManagementPanel();
                        changeContents(dsmp);

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            });
        }

        return showDeliverStatusButton;
    }
    //VTAn End add 20140612

    /**
     * レジ精算ボタンを取得する。
     *
     * @return レジ精算ボタン
     */
    private JButton getInputAccountButton() {
        if (inputAccountButton == null) {

            inputAccountButton =
                    this.createImageButton(
                    "レジ精算",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/front_cash_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/front_cash_on.jpg",
                    null,
                    true);

            inputAccountButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    inputAccountButton.setCursor(null);

                    try {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                        HairInputAccountPanel riap = new HairInputAccountPanel();
                        HairInputAccountPanel.setEditFlag(1);
                        //一時保存ボタン使用不可
                        //riap.changeTempRegistBtnNotUse();
                        changeContents(riap);

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            });
        }

        return inputAccountButton;
    }

    /**
     * 伝票検索ボタンを取得する。
     *
     * @return 伝票検索ボタン
     */
    private JButton getSearchAccountButton() {
        if (searchAccountButton == null) {
            searchAccountButton = this.createImageButton(
                    "精算伝票検索",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/closed_buying_accout_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/closed_buying_accout_on.jpg",
                    null,
                    true);

            searchAccountButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    HairSearchAccountPanel rsap = new HairSearchAccountPanel();
                    changeContents(rsap);
                }
            });
        }

        return searchAccountButton;
    }

    /**
     * 売掛一覧ボタンを取得する。
     *
     * @return 売掛一覧ボタン
     */
    private JButton getReceivableListButton() {
        if (receivableListButton == null) {
            receivableListButton = this.createImageButton(
                    "売掛回収",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_recivable_list_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_recivable_list_on.jpg",
                    null,
                    true);
            /*
             "売掛回収処理",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/account/receivable_list_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/account/receivable_list_on.jpg",
             null,
             true);
             */
            receivableListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    HairBillsListPanel rblp = new HairBillsListPanel();
                    changeContents(rblp);
                }
            });
        }

        return receivableListButton;
    }

    /**
     * レジ管理ボタンを取得する。
     *
     * @return 売掛一覧ボタン
     */
    private JButton getRegisterButton() {
        if (registerButton == null) {
            registerButton = this.createImageButton(
                    "レジ締め",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/closed_casher_management_table_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/closed_casher_management_table_on.jpg",
                    null,
                    true);
            /*
             "レジ管理",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/account/register_control_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/account/register_control_on.jpg",
             null,
             true);
             */
            registerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    HairRegisterPanel rp = new HairRegisterPanel();
                    changeContents(rp);
                }
            });
        }

        return registerButton;
    }

    /**
     * レジ入出金ボタンを取得する。
     *
     * @return レジ入出金ボタン
     */
    private JButton getRegisterCashButton() {
        if (registerCashButton == null) {
            registerCashButton = this.createImageButton(
                    "レジ出納帳",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_register_cash_io_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_register_cash_io_on.jpg",
                    null,
                    true);
            /*
             "レジ入出金",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/account/register_io_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/account/register_io_on.jpg",
             null,
             true);
             */
            registerCashButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    RegisterCashIOPanel rcp = new RegisterCashIOPanel();
                    changeContents(rcp);
                }
            });
        }

        return registerCashButton;
    }

    /**
     * Thanh add
     *
     * @return スタッフ成績ボタン
     */
    private JButton getCustomButton() {
        if (customButton == null) {
            customButton = this.createImageButton(
                    "スタッフ成績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/merchandise_leger_sheet_custom_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/merchandise_leger_sheet_custom_on.jpg",
                    null,
                    true);
            /*
             "スタッフ成績",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/staff_result_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/staff_result_on.jpg",
             null,
             true);
             */
            customButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(customButton.getLocationOnScreen(),
                            customButton.getWidth(),
                            getCustomMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return customButton;
    }

    /**
     * 20161227 add オリジナル帳票出力画面
     *
     * @return オリジナル帳票出力画面ボタン
     */
    private JButton getOriginalReportButton() {
        if (customButton == null) {
            customButton = this.createImageButton(
                    "オリジナル帳票",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_original_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_original_report_on.jpg",
                    null,
                    true);

            customButton.addActionListener(new ActionListener() {
				@Override
                public void actionPerformed(ActionEvent ae) {
                    OriginalReportPanel panel = new OriginalReportPanel();
                    changeContents(panel);
                }
            });
        }

        return customButton;
    }

    /**
     * 業務報告ボタンを取得する。
     *
     * @return 業務報告ボタン
     */
    private JButton getBusinessReportButton() {
        if (businessReportButton == null) {
            businessReportButton = this.createImageButton(
                    "業務報告",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/closed_bisiness_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/closed_bisiness_report_on.jpg",
                    null,
                    true);
            /*
             "業務報告",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/business_report_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/business_report_on.jpg",
             null,
             true);

             */
            businessReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    BusinessReportPanel brp = new BusinessReportPanel();
                    changeContents(brp);
                }
            });
        }

        return businessReportButton;
    }
    /**
	 * [モデスティカスタマイズ]
     * カスタム帳票ボタンを取得する。
     *
     * @return カスタム帳票ボタン
     */
    private JButton getCustomCutoffReportButton() {
        if (customCutoffReportButton == null ) {
            customCutoffReportButton = this.createImageButton(
                    "カスタム帳票",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/custom_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/custom_report_on.jpg",
                    null,
                    true);

            customCutoffReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ModestyReportPanel brp = new ModestyReportPanel();
                    changeContents(brp);
                }
            });
        }

        return customCutoffReportButton;
    }

    /**
     * 売上集計ボタンを取得する。
     *
     * @return 売上集計ボタン
     */
    private JButton getSalesTotalButton() {
        if (salesTotalButton == null) {
            salesTotalButton = this.createImageButton(
                    "日次推移",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/transition_daymonth_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/transition_daymonth_on.jpg",
                    null,
                    false);

            salesTotalButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SalesReportPanel srp = new SalesReportPanel();
                    changeContents(srp);
                }
            });
        }

        return salesTotalButton;
    }

    //
    /* パスブック集計
    */

    private JButton getPassBookButton() {
        if (passBookButton == null) {
            passBookButton = this.createImageButton(
                    "パスブック集計",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/passbook_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/passbook_on.jpg",
                    null,
                    false);

            passBookButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                   PassBookReportPanel pbr = new PassBookReportPanel();
                    changeContents(pbr);
                }
            });
        }

        return passBookButton;
    }

    /**
     * 時間帯分析ボタンを取得する。
     *
     * @return 時間帯分析ボタン
     */
    private JButton getTimeAnalysisButton() {
        if (timeAnalysisButton == null) {
            timeAnalysisButton = this.createImageButton(
                    "時間帯分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_time_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_time_on.jpg",
                    null,
                    false);

            timeAnalysisButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(230)) {
                        return;
                    }

                    TimeAnalysisPanel p = new TimeAnalysisPanel();
                    changeContents(p);
                }
            });
        }

        return timeAnalysisButton;
    }

    /**
     * Thanh add
     *
     * @return 担当別成績一覧表ボタン
     */
    private JButton getSaleTransittionButton() {
        if (saleTransittionButton == null) {
            saleTransittionButton = this.createImageButton(
                    "売上推移表",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/sale_Transittion_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/sale_Transittion_on.jpg",
                    null,
                    false);
            saleTransittionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SalesTransitionReportPanel srp = new SalesTransitionReportPanel();
                    changeContents(srp);
                }
            });
        }

        return saleTransittionButton;
    }
    /* スタッフ成績ボタンを取得する。
     * @return スタッフ成績ボタン
     */

    private JButton getStaffResultButton() {
        if (staffResultButton == null) {
            staffResultButton = this.createImageButton(
                    "スタッフ成績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_on.jpg",
                    null,
                    true);
            /*
             "スタッフ成績",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/staff_result_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/staff_result_on.jpg",
             null,
             true);
             */
            staffResultButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(staffResultButton.getLocationOnScreen(),
                            staffResultButton.getWidth(),
                            getStaffResultMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return staffResultButton;
    }

    /**
     * 売上集計ボタンを取得する。
     *
     * @return 売上集計ボタン
     */
    private JButton getReappearanceButton() {
        if (reappearanceButton == null) {
            reappearanceButton = this.createImageButton(
                    "再来分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_on.jpg",
                    null,
                    false);

            reappearanceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ReappearanceReportPanel rrp = new ReappearanceReportPanel();
                    changeContents(rrp);
                }
            });
        }

        return reappearanceButton;
    }

    /**
     * 再来分析ボタンを取得する。
     *
     * @return 売上集計ボタン
     */
    private JButton getReappearancePredictionButton() {
        if (reappearancePredictionButton == null) {
            reappearancePredictionButton = this.createImageButton(
                    "動向分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_on.jpg",
                    null,
                    false);

            reappearancePredictionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ReappearancePredictionPanel rrp = new ReappearancePredictionPanel();
                    changeContents(rrp);
                }
            });
        }

        return reappearancePredictionButton;
    }

    /**
     * 担当別成績一覧表ボタンを取得する。
     *
     * @return 担当別成績一覧表ボタン
     */
    private JButton getStaffResultListButton() {
        if (staffResultListButton == null) {
            staffResultListButton = this.createImageButton(
                    "担当別成績一覧表",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_on.jpg",
                    null,
                    false);
            staffResultListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    StaffResultListReportPanel srp = new StaffResultListReportPanel();
                    changeContents(srp);
                }
            });
        }

        return staffResultListButton;
    }

    /**
     * 担当別技術成績ボタンを取得する。
     *
     * @return 担当別技術成績ボタン
     */
    private JButton getStaffResultTechButton() {
        if (staffResultTechButton == null) {
            staffResultTechButton = this.createImageButton(
                    "担当別技術成績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_technique_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_technique_on.jpg",
                    null,
                    false);
            staffResultTechButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    StaffResultTechReportPanel srp = new StaffResultTechReportPanel();
                    changeContents(srp);
                }
            });
        }

        return staffResultTechButton;
    }

    /**
     * 担当別商品成績ボタンを取得する。
     *
     * @return 担当別商品成績ボタン
     */
    private JButton getStaffResultGoodsButton() {
        if (staffResultGoodsButton == null) {
            staffResultGoodsButton = this.createImageButton(
                    "担当別商品成績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_goods_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_goods_on.jpg",
                    null,
                    false);
            staffResultGoodsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    StaffResultGoodsReportPanel srp = new StaffResultGoodsReportPanel();
                    changeContents(srp);
                }
            });
        }

        return staffResultGoodsButton;
    }

    /**
     * 担当別顧客成績ボタンを取得する。
     *
     * @return 担当別顧客成績ボタン
     */
    private JButton getStaffResultCustomerButton() {
        if (staffResultCustomerButton == null) {
            staffResultCustomerButton = this.createImageButton(
                    "担当別顧客成績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_c-manegement_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_c-manegement_on.jpg",
                    null,
                    false);
            staffResultCustomerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    StaffResultCustomerReportPanel srp = new StaffResultCustomerReportPanel();
                    changeContents(srp);
                }
            });
        }

        return staffResultCustomerButton;
    }

    /**
     * 担当別再来成績ボタンを取得する。
     *
     * @return 担当別再来成績ボタン
     */
    private JButton getStaffResultRepeatButton() {
        if (staffResultRepeatButton == null) {
            staffResultRepeatButton = this.createImageButton(
                    "担当別再来成績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_repeat_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_repeat_on.jpg",
                    null,
                    false);
            staffResultRepeatButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    StaffResultRepeatReportPanel srp = new StaffResultRepeatReportPanel();
                    changeContents(srp);
                }
            });
        }

        return staffResultRepeatButton;
    }

    /**
     * ランキングボタンを取得する。
     *
     * @return ランキング
     */
    private JButton getRankingButton() {
        if (rankingButton == null) {
            rankingButton = this.createImageButton(
                    "ランキング",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_response_ranking_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_response_ranking_on.jpg",
                    null,
                    true);
            rankingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (SystemInfo.checkAuthorityPassword(270)) {
                        StaffShopRankingPanel ssrp = new StaffShopRankingPanel();
                        changeContents(ssrp);
                    }
                }
            });
        }

        return rankingButton;
    }

    /**
     * レスポンス分析ボタンを取得する。
     *
     * @return レスポンス分析
     */
    private JButton getResponseEffectButton() {
        if (responseEffectButton == null) {
            responseEffectButton = this.createImageButton(
                    "レスポンス分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_on.jpg",
                    null,
                    false);
            /*
             "レスポンス分析",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_effect_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_effect_on.jpg",
             null,
             true);

             */
            responseEffectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ResponseReportPanel rrp = new ResponseReportPanel(SystemInfo.getCurrentShop());
                    changeContents(rrp);
                }
            });
        }

        return responseEffectButton;
    }

    /**
     * 反響リピート分析ボタンを取得する。
     *
     * @return レスポンス分析 getResponseRepeatAnalyzetButton
     */
    private JButton getResponseRepeatAnalyzetButton() {
        if (responseRepeatAnalyzetButton == null) {
            responseRepeatAnalyzetButton = this.createImageButton(
                    "反響リピート分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_repeat_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_repeat_on.jpg",
                    null,
                    false);
            /*
             "レスポンス分析",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_effect_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_effect_on.jpg",
             null,
             true);

             */
            responseRepeatAnalyzetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ResponseRepeatAnalyzetPanel rrap = new ResponseRepeatAnalyzetPanel();
                    changeContents(rrap);
                }
            });
        }

        return responseRepeatAnalyzetButton;
    }

    /**
     * いとーさん分析 売上構成分析ボタンを取得する。
     *
     * @return 売上構成分析ボタン
     */
    private JButton getItoCustomerProblemSheetButton() {
        if (itoCustomerProblemSheetButton == null) {
            itoCustomerProblemSheetButton = this.createImageButton(
                    "売上構成分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/ito_customer_problem_sheet_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/ito_customer_problem_sheet_on.jpg",
                    null,
                    false);
            itoCustomerProblemSheetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ItoCustomerProblemSheetPanel panel = new ItoCustomerProblemSheetPanel();
                    changeContents(panel);
                }
            });
        }

        return itoCustomerProblemSheetButton;
    }

    /**
     * カルテ分析ボタンを取得する。
     *
     * @return カルテ分析ボタン
     */
    private JButton getItoAnalitic3jigenButton() {
        if (itoAnalitic3jigenButton == null) {
            itoAnalitic3jigenButton = this.createImageButton(
                    "カルテ分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/ito_analitic_3jigen_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/ito_analitic_3jigen_on.jpg",
                    null,
                    false);
            itoAnalitic3jigenButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Ito3DCustomerAnalysisPanel panel = new Ito3DCustomerAnalysisPanel();
                    changeContents(panel);
                }
            });
        }

        return itoAnalitic3jigenButton;
    }

    /**
     * レジ入出金ボタンを取得する。
     *
     * @return レジ入出金ボタン
     */
    private JButton getRegisterCashIOButton() {
        if (registerCashIOButton == null) {
            registerCashIOButton = this.createImageButton(
                    "月間レジ出納帳",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/casher_register_cash_io_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/casher_register_cash_io_on.jpg",
                    null,
                    true);
            /*
             "レジ入出金表",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/register_cash_io_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/report/register_cash_io_on.jpg",
             null,
             true);

             */
            registerCashIOButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    RegisterCashIOReportPanel rciorp = new RegisterCashIOReportPanel();
                    changeContents(rciorp);
                }
            });
        }

        return registerCashIOButton;
    }

    /**
     * 小口現金ボタンを取得する。
     *
     * @return 小口現金ボタン
     */
    private JButton getBaseCashButton() {
        if (baseCashButton == null) {
            baseCashButton = this.createImageButton(
                    "小口現金",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/base_cash_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/base_cash_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/base_cash_on.jpg",
                    true);

            baseCashButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(baseCashButton.getLocationOnScreen(),
                            baseCashButton.getWidth(),
                            getBaseCashMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return baseCashButton;
    }

    /**
     * 小口現金メニューを取得する。
     *
     * @return 小口現金メニュー
     */
    public JPanel getBaseCashMenu() {
        if (baseCashMenu == null) {
            baseCashMenu = this.createSubMenu();

            //小口項目登録
            this.addSubMenu(baseCashMenu, this.getCashClassButton(), -1);

            //小口詳細登録
            this.addSubMenu(baseCashMenu, this.getCashMenuButton(), -1);

            //小口現金管理
            this.addSubMenu(baseCashMenu, this.getCashRegistButton(), -1);

        }

        return baseCashMenu;
    }

    /**
     * 小口項目登録ボタンを取得する。
     *
     * @return 小口項目登録ボタン
     */
    public JButton getCashClassButton() {
        if (cashClassButton == null) {
            cashClassButton = this.createImageButton(
                    "小口項目登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/cash_class_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/cash_class_on.jpg",
                    null,
                    false);

            cashClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    cashClassButton.getModel().setRollover(false);
                    MstCashClassPanel panel = new MstCashClassPanel();
                    changeContents(panel);
                }
            });
        }

        return cashClassButton;
    }

    /**
     * 小口詳細登録ボタンを取得する。
     *
     * @return 小口詳細登録ボタン
     */
    public JButton getCashMenuButton() {
        if (cashMenuButton == null) {
            cashMenuButton = this.createImageButton(
                    "小口詳細登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/cash_menu_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/cash_menu_on.jpg",
                    null,
                    false);

            cashMenuButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    cashMenuButton.getModel().setRollover(false);
                    MstCashMenuPanel mcmp = new MstCashMenuPanel();

                    if (mcmp.checkClassRegisted()) {
                        changeContents(mcmp);
                    } else {
                        MessageDialog.showMessageDialog(mcmp,
                                MessageUtil.getMessage(7004, "小口項目"),
                                mcmp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mcmp = null;

                        mstKarteClassButton.getModel().setRollover(false);
                        MstCashClassPanel panel = new MstCashClassPanel();
                        changeContents(panel);
                    }
                }
            });
        }

        return cashMenuButton;
    }

    /**
     * 小口現金管理ボタンを取得する。
     *
     * @return 小口現金管理ボタン
     */
    public JButton getCashRegistButton() {
        if (cashRegistButton == null) {
            cashRegistButton = this.createImageButton(
                    "小口現金管理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/cash_regist_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/cash_regist_on.jpg",
                    null,
                    false);

            cashRegistButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    cashRegistButton.getModel().setRollover(false);
                    CashManagementPanel panel = new CashManagementPanel();
                    changeContents(panel);
                }
            });
        }

        return cashRegistButton;
    }

    /**
     * 条件検索ボタンを取得する。
     *
     * @return 条件検索ボタン
     */
    private JButton getConditionedSearchButton() {
        if (conditionedSearchButton == null) {
            conditionedSearchButton = this.createImageButton(
                    "条件検索",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/promotion_condition_search_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/promotion_condition_search_on.jpg",
                    null,
                    true);
            /*
             "条件検索",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/conditioned_search_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/conditioned_search_on.jpg",
             null,
             true);

             */
            conditionedSearchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    HairMailSearchPanel rmsp = new HairMailSearchPanel();
                    changeContents(rmsp);
                }
            });
        }

        return conditionedSearchButton;
    }

    /**
     * DM作成履歴ボタンを取得する。
     *
     * @return DM作成履歴ボタン
     */
    private JButton getDmHistoryButton() {
        if (dmHistoryButton == null) {
            dmHistoryButton = this.createImageButton(
                    "DM作成履歴",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/promotion_dm_history_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/promotion_dm_history_on.jpg",
                    null,
                    true);

            dmHistoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    DmHistoryPanel p = new DmHistoryPanel();
                    changeContents(p);
                }
            });
        }

        return dmHistoryButton;
    }

    /**
     * 効果指標数値分析ボタンを取得する。
     *
     * @return 効果指標数値分析ボタン
     */
    private JButton getEffectIndicatorAnalysisButton() {
        if (effectIndicatorAnalysisButton == null) {
        	effectIndicatorAnalysisButton = this.createImageButton(
                    "効果指標数値分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/effect_index_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/effect_index_on.jpg",
                    null,
                    true);

        	effectIndicatorAnalysisButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                	EffectIndicatorAnalysisPanel p = new EffectIndicatorAnalysisPanel();
                    changeContents(p);
                }
            });
        }

        return effectIndicatorAnalysisButton;
    }

    /**
     * 来店理由ランキングボタンを取得する。
     *
     * @return 来店理由ランキングボタン
     */
    private JButton getReasonsRankButton() {
        if (reasonsRankButton == null) {
        	reasonsRankButton = this.createImageButton(
                    "効果指標数値分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/reasons_rank_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/reasons_rank_on.jpg",
                    null,
                    true);

        	reasonsRankButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                	ReasonsRankPanel p = new ReasonsRankPanel();
                    changeContents(p);
                }
            });
        }

        return reasonsRankButton;
    }

    /**
     * 売れ筋ランキングボタンを取得する。
     *
     * @return 売れ筋ランキングボタン
     */
    private JButton getItemSalesRankingButton() {
        if (itemSalesRankingButton == null) {
        	itemSalesRankingButton = this.createImageButton(
                    "売れ筋ランキング",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/selling_rank_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/selling_rank_on.jpg",
                    null,
                    true);

        	itemSalesRankingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                	ItemSalesRankingPanel p = new ItemSalesRankingPanel();
                    changeContents(p);
                }
            });
        }

        return itemSalesRankingButton;
    }

    /**
     * 過去最大実績ボタンを取得する。
     *
     * @return 売れ筋ランキングボタン
     */
    private JButton getPostMaxResultsButton() {
        if (postMaxResultsButton == null) {
        	postMaxResultsButton = this.createImageButton(
                    "過去最大実績",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/past_max_record_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/past_max_record_on.jpg",
                    null,
                    true);

        	postMaxResultsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                	PostMaxResultsPanel p = new PostMaxResultsPanel();
                    changeContents(p);
                }
            });
        }

        return postMaxResultsButton;
    }

    //IVS_LVTu start add 2016/03/08 New request #48811
    /**
     * メニュー、出力画面
     *
     * @return
     */
    private JButton getPointReportButton() {
        if (PointReportButton == null) {
        	PointReportButton = this.createImageButton(
                    "ポイント集計",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/point_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/point_on.jpg",
                    null,
                    true);

        	PointReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ReportPointPanel p = new ReportPointPanel();
                    changeContents(p);
                }
            });
        }

        return PointReportButton;
    }
    //IVS_LVTu end add 2016/03/08 New request #48811

    /**
     * 商品在庫一覧ボタンを取得する。
     *
     * @return 商品在庫一覧ボタン
     */
    private JButton getItemStockListButton() {
        if (itemStockListButton == null) {
        	itemStockListButton = this.createImageButton(
                    "商品在庫一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/inventory_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report_manage/inventory_on.jpg",
                    null,
                    true);

        	itemStockListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                	ItemStockListPanel p = new ItemStockListPanel();
                    changeContents(p);
                }
            });
        }

        return itemStockListButton;
    }

    /**
     * テンプレート分類登録ボタンを取得する。
     *
     * @return テンプレート分類登録ボタン
     */
    private JButton getTemplateClassButton() {
        if (templateClassButton == null) {
            templateClassButton = this.createImageButton(
                    "テンプレート分類登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_grouping_template_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_grouping_template_entry_on.jpg",
                    null,
                    false);
            /*
             "テンプレート分類登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/template_class_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/template_class_on.jpg",
             null,
             false);
             */
            templateClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MstMailTemplateClassPanel mmtcp = new MstMailTemplateClassPanel();
                    changeContents(mmtcp);
                }
            });
        }

        return templateClassButton;
    }

    /**
     * テンプレート登録ボタンを取得する。
     *
     * @return テンプレート登録ボタン
     */
    private JButton getTemplateButton() {
        if (templateButton == null) {
            templateButton = this.createImageButton(
                    "テンプレート登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_template_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_template_entry_on.jpg",
                    null,
                    false);
            /*
             "テンプレート登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/template_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/template_on.jpg",
             null,
             false);

             */
            templateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MstMailTemplatePanel mmtp = new MstMailTemplatePanel();
                    changeContents(mmtp);
                }
            });
        }

        return templateButton;
    }

    /**
     * 署名登録ボタンを取得する。
     *
     * @return 署名登録ボタン
     */
    private JButton getSignatureButton() {
        if (signatureButton == null) {
            signatureButton = this.createImageButton(
                    "署名登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_sign_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_sign_entry_on.jpg",
                    null,
                    false);
            /*
             "署名登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/signature_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/signature_on.jpg",
             null,
             false);

             */
            signatureButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MstMailSignaturePanel mmsp = new MstMailSignaturePanel();
                    changeContents(mmsp);
                }
            });
        }

        return signatureButton;
    }
//add

    /**
     * メール関連ボタンを取得する。
     *
     * @return メール関連ボタン
     */
    public JButton getMailMasterButton() {
        if (mailMasterButton == null) {
            mailMasterButton = this.createImageButton(
                    "メール関連",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_mail_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_mail_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_mail_on.jpg",
                    true);

            mailMasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(mailMasterButton.getLocationOnScreen(),
                            mailMasterButton.getWidth(),
                            getMailMasterMenu());//
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return mailMasterButton;
    }

    /**
     * メール関連メニューを取得する。
     *
     * @return メール関連メニュー
     */
    public JPanel getMailMasterMenu() {
        if (mailMasterMenu == null) {
            mailMasterMenu = this.createSubMenu();

            //テンプレート分類登録
            this.addSubMenu(mailMasterMenu, this.getTemplateClassButton(), 51);

            //テンプレート登録
            this.addSubMenu(mailMasterMenu, this.getTemplateButton(), 52);

            //署名登録
            this.addSubMenu(mailMasterMenu, this.getSignatureButton(), 50);

            // 自動メール設定画面
            this.addSubMenu(mailMasterMenu, this.getImageButton(autoMailButton, "/menu/mail/automail_setting", "自動メール設定",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            changeContents(new MstAutoMailSettingPanel());
                        }
                    }), -1);

        }

        return mailMasterMenu;
    }
    // IVS SANG START INSERT 20131211 [gbソース]商品受渡確認書のマージ

    private JButton getProductDeliveryManagementButton() {
        if (ProductDeliveryManagementButton == null) {
            ProductDeliveryManagementButton = this.createImageButton(
                    "商品宅配管理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/item_delivery_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/item_delivery_on.jpg",
                    null,
                    false);
            /*
             "テンプレート分類登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/template_class_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/template_class_on.jpg",
             null,
             false);
             */
            ProductDeliveryManagementButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ProductDeliveryManagementPanel mstpdm = new ProductDeliveryManagementPanel();
                    changeContents(mstpdm);
                }
            });
        }

        return ProductDeliveryManagementButton;
    }
    // IVS SANG END INSERT 20131211 [gbソース]商品受渡確認書のマージ
//add end

    /**
     * 会社マスタボタンを取得する。
     *
     * @return 会社マスタボタン
     */
    public JButton getCompanyMasterButton() {
        if (companyMasterButton == null) {
            companyMasterButton = this.createImageButton(
                    "会社関連",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_company_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_company_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_company_on.jpg",
                    true);
            /*
             "会社マスタ",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company_master_on.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company_master_on.jpg",
             true);

             */

            companyMasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(companyMasterButton.getLocationOnScreen(),
                            companyMasterButton.getWidth(),
                            getCompanyMasterMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return companyMasterButton;
    }

    /**
     * 商品マスタボタンを取得する。
     *
     * @return 商品マスタボタン
     */
    public JButton getItemMasterButton() {
        if (itemMasterButton == null) {
            itemMasterButton = this.createImageButton(
                    "商品関連",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_goods_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_goods_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_goods_on.jpg",
                    true);
            /*
             "商品マスタ",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item_master_on.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item_master_on.jpg",
             true);

             */
            itemMasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(itemMasterButton.getLocationOnScreen(),
                            itemMasterButton.getWidth(),
                            getItemMasterMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return itemMasterButton;
    }

    //IVS_LVTu start add 2018/13/05 GB_Finc
    /**
     * 月会員管理
     *
     * @return 月会員管理ボタン
     */
    public JButton getManageMemberMonButton() {
        if (manageMemberMonButton == null) {
            manageMemberMonButton = this.createImageButton(
                    "月会員管理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/monthly_plan_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/monthly_plan_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/monthly_plan_on.jpg",
                    true);

            manageMemberMonButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(manageMemberMonButton.getLocationOnScreen(),
                            manageMemberMonButton.getWidth(),
                            getManageMemberMasterMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return manageMemberMonButton;
    }
    //IVS_LVTu end add 2018/13/05 GB_Finc


    // Start add 20160825 IVS_NHTVINH
    public JButton getItemSiteControlIntroduction() {

        if (itemSiteControlIntroduction == null) {
            itemSiteControlIntroduction = this.createImageButton(
                    "ｻｲﾄｺﾝﾄﾛｰﾙ導入",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/media_install_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/media_install_on.jpg",
                    null,
                    false);

            itemSiteControlIntroduction.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SiteControlIntroductionPanel scp = new SiteControlIntroductionPanel();
                    changeContents(scp);
                }
            });
        }

        return itemSiteControlIntroduction;
    }
    // End add 20160825 IVS_NHTVINH

    //IVS_nahoang GB_Mashu_目標設定 Start add 20141021
    private JButton targetSettingButton = null;

    public JButton getItemTargetSettingButton() {
        if (targetSettingButton == null) {
            targetSettingButton = this.createImageButton(
                    "目標設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_target_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_target_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_target_on.jpg",
                    true);
            targetSettingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    targetSettingButton.setCursor(null);
                    try {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        targetSettingButton.getModel().setRollover(false);
                        MstTargetSettingPanel mgcp = new MstTargetSettingPanel();
                        changeContents(mgcp);
                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }

                }
            });
        }

        return targetSettingButton;

    }

    //IVS_nahoang GB_Mashu_目標設定 End add

    /**
     * 技術マスタボタンを取得する。
     *
     * @return 技術マスタボタン
     */
    public JButton getTechnicMasterButton() {
        if (technicMasterButton == null) {
            technicMasterButton = this.createImageButton(
                    "技術関連",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_technic_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_technic_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_technic_on.jpg",
                    true);
            /*
             "技術マスタ",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic_master_on.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic_master_on.jpg",
             true);
             */
            technicMasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(technicMasterButton.getLocationOnScreen(),
                            technicMasterButton.getWidth(),
                            getTechnicMasterMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return technicMasterButton;
    }
//add

    /**
     * 顧客分析を取得する。
     *
     * @return 顧客分析ボタン
     */
    public JButton getCustomerEffectButton() {
        if (customerEffectButton == null) {
            customerEffectButton = this.createImageButton(
                    "顧客分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/analitic_customer_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/analitic_customer_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/analitic_customer_on.jpg",
                    true);
            customerEffectButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(customerEffectButton.getLocationOnScreen(),
                            customerEffectButton.getWidth(),
                            getCustomerEffectMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return customerEffectButton;
    }

    /**
     * 売上分析を取得する。
     *
     * @return 売上分析ボタン
     */
    public JButton getSalesEffectButton() {
        if (salesEffectButton == null) {
            salesEffectButton = this.createImageButton(
                    "売上分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_sales_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_sales_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_sales_on.jpg",
                    true);
            salesEffectButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(salesEffectButton.getLocationOnScreen(),
                            salesEffectButton.getWidth(),
                            getSalesEffectMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return salesEffectButton;
    }

    /**
     * 反響分析を取得する。
     *
     * @return 反響分析ボタン
     */
    public JButton getRepeatAnalysisButton() {
        if (repeatAnalysisButton == null) {
            repeatAnalysisButton = this.createImageButton(
                    "反響分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_response_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_response_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_response_on.jpg",
                    true);
            repeatAnalysisButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(repeatAnalysisButton.getLocationOnScreen(),
                            repeatAnalysisButton.getWidth(),
                            getRepeatAnalysisMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return repeatAnalysisButton;
    }

    //Luc add start 20121004 店舗分析ボタン
    public JButton getStoreButton() {
        if (storeButton == null) {
            storeButton = this.createImageButton(
                    "店舗分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_store_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_store_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_store_on.jpg",
                    true);

            storeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    StoreReportPanel rrp = new StoreReportPanel();
                    changeContents(rrp);
                }
            });
        }

        return storeButton;
    }
    //Luc add End 20121004 店舗分析ボタン

    /**
     *
     * 再来分析を取得する。
     *
     * @return 売上分析ボタン
     */
    public JButton getReappearanceEffectButton() {
        if (reappearanceEffectButton == null) {
            reappearanceEffectButton = this.createImageButton(
                    "再来分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_reappearance_on.jpg",
                    true);

            reappearanceEffectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ReappearanceReportPanel rrp = new ReappearanceReportPanel();
                    changeContents(rrp);
                }
            });
//			reappearanceEffectButton.addMouseListener(new java.awt.event.MouseAdapter()
//			{
//				public void mouseEntered(java.awt.event.MouseEvent evt)
//				{
//					showSubMenu(reappearanceEffectButton.getLocationOnScreen(),
//							reappearanceEffectButton.getWidth(),
//							getReappearanceEffectMenu());
//				}
//				public void mouseExited(java.awt.event.MouseEvent evt)
//				{
//				}
//			});
        }

        return reappearanceEffectButton;
    }
//add end

    /**
     * 動向分析を取得する。
     *
     * @return 動向分析ボタン
     */
    public JButton getReappearancePredictionEffectButton() {
        if (reappearancePredictionEffectButton == null) {
            reappearancePredictionEffectButton = this.createImageButton(
                    "動向分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_estimate_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_estimate_on.jpg",
                    null,
                    false);

            reappearancePredictionEffectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(220)) {
                        return;
                    }

                    ReappearancePredictionPanel rrp = new ReappearancePredictionPanel();
                    changeContents(rrp);
                }
            });
        }

        return reappearancePredictionEffectButton;
    }

    /**
     * クロス分析を取得する。
     *
     * @return クロス分析ボタン
     */
    public JButton getCrossAnalysisEffectButton() {
        if (crossAnalysisEffectButton == null) {
            crossAnalysisEffectButton = this.createImageButton(
                    "クロス分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_cross_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_cross_on.jpg",
                    null,
                    false);

            crossAnalysisEffectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(221)) {
                        return;
                    }

                    CrossAnalysisPanel panel = new CrossAnalysisPanel();
                    changeContents(panel);
                }
            });
        }

        return crossAnalysisEffectButton;
    }

    /**
     * クロス分析5を取得する。
     *
     * @return クロス分析5ボタン
     */
    public JButton getCrossAnalysis5EffectButton() {
        if (crossAnalysis5EffectButton == null) {
            crossAnalysis5EffectButton = this.createImageButton(
                    "クロス分析5",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_cross5_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_cross5_on.jpg",
                    null,
                    false);

            crossAnalysis5EffectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(222)) {
                        return;
                    }

                    CrossAnalysis5Panel panel = new CrossAnalysis5Panel();
                    changeContents(panel);
                }
            });
        }

        return crossAnalysis5EffectButton;
    }

    //GB_Mashu Task #34581 [Product][Code][Phase3]マッシャー分析
    //IVS_nahoang start add 20141229
    private JButton masherAnalysis5PanelButton = null;
    public JButton getMasherAnalysis5PanelButton() {
        if (masherAnalysis5PanelButton  == null) {
            masherAnalysis5PanelButton  = this.createImageButton(
                    "マッシャー分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/cur_rank_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/cus_rank_on.jpg",
                    null,
                    false);

            masherAnalysis5PanelButton .addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MasherAnalysis5Panel panel = new MasherAnalysis5Panel();
                    changeContents(panel);
                }
            });
        }

        return masherAnalysis5PanelButton ;
    }
    //IVS_nahoang end add 20141229
    // Luc add start 20121004 顧客属性分析
    public JButton getCustomerAnalysisAttributeButton() {
        if (customerAtrributeAnalysisButton == null) {
            customerAtrributeAnalysisButton = this.createImageButton(
                    "顧客属性分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_customer_attribute_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_customer_attribute_on.jpg",
                    null,
                    false);

            customerAtrributeAnalysisButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(222)) {
                        return;
                    }
                    AreaAndAttributeCustomerAnalytic panel = new AreaAndAttributeCustomerAnalytic();
                    //AreaAndAtributeCustomerAnalytic panel = new AreaAndAtributeCustomerAnalytic();
                    changeContents(panel);
                }
            });
        }

        return customerAtrributeAnalysisButton;
    }
    //Luc add End 20121004 顧客属性分析

    //Luc add End 20121004 エリア分析
    public JButton getCustomerAreaAnalysisButton() {
        if (customerAreaAnalysisButton == null) {
            customerAreaAnalysisButton = this.createImageButton(
                    "エリア分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_customer_area_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_customer_area_on.jpg",
                    null,
                    false);

            customerAreaAnalysisButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(222)) {
                        return;
                    }

                    AreaAndAttributeCustomerAnalytic panel = new AreaAndAttributeCustomerAnalytic();
                    changeContents(panel);
                }
            });
        }

        return customerAreaAnalysisButton;
    }
    //Luc add End 20121004 エリア分析

    /**
     * サイクル分析を取得する。
     *
     * @return サイクル分析ボタン
     */
    public JButton getKarteAnalysisEffectButton() {
        if (karteAnalysisEffectButton == null) {
            karteAnalysisEffectButton = this.createImageButton(
                    "サイクル分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/karte_analitic_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/karte_analitic_on.jpg",
                    null,
                    false);

            karteAnalysisEffectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    KarteAnalysisPanel panel = new KarteAnalysisPanel();
                    changeContents(panel);
                }
            });
        }

        return karteAnalysisEffectButton;
    }

    /**
     * 客数問題発見シートαを取得する。
     *
     * @return 客数問題発見シートαボタン
     */
    public JButton getCustomerProblemSheetButton() {
        if (customerProblemSheetButton == null) {
            customerProblemSheetButton = this.createImageButton(
                    "客数問題発見シートα",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/customer_problem_sheet_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/customer_problem_sheet_on.jpg",
                    null,
                    false);

            customerProblemSheetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    CustomerProblemSheetPanel panel = new CustomerProblemSheetPanel();
                    changeContents(panel);
                }
            });
        }

        return customerProblemSheetButton;
    }

    /**
     * 顧客マスタボタンを取得する。
     *
     * @return 顧客マスタボタン
     */
    public JButton getCustomerMasterButton() {
        if (customerMasterButton == null) {
            customerMasterButton = this.createImageButton(
                    "顧客関連",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_customer_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_customer_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_customer_on.jpg",
                    true);
            /*
             "顧客マスタ",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer_master_on.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer_master_on.jpg",
             true);

             */
            customerMasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(customerMasterButton.getLocationOnScreen(),
                            customerMasterButton.getWidth(),
                            getCustomerMasterMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return customerMasterButton;
    }

    /**
     * 精算マスタボタンを取得する。
     *
     * @return 精算マスタボタン
     */
    public JButton getAccountMasterButton() {
        if (accountMasterButton == null) {
            accountMasterButton = this.createImageButton(
                    "精算関連",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_account_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_account_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_account_on.jpg",
                    true);
            /*
             "精算マスタ",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account_master_on.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account_master_on.jpg",
             true);
             */

            accountMasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(accountMasterButton.getLocationOnScreen(),
                            accountMasterButton.getWidth(),
                            getAccountMasterMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return accountMasterButton;
    }

    /**
     * CSVインポートボタンを取得する。
     *
     * @return CSVインポートボタン
     */
    public JButton getCsvImportButton() {
        if (csvImportButton == null) {
            csvImportButton = this.createImageButton(
                    "CSVインポート",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_import_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_import_on.jpg",
                    null,
                    true);
            /*
             "CSVインポート",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/csv_import_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/csv_import_on.jpg",
             null,
             true);
             */
            csvImportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    com.geobeck.sosia.pos.hair.csv.CSVImportPanel csvip = new com.geobeck.sosia.pos.hair.csv.CSVImportPanel();
                    changeContents(csvip);
                }
            });
        }

        return csvImportButton;
    }

    /**
     * CSVエクスポートボタンを取得する。
     *
     * @return CSVアウトポートボタン
     */
    public JButton getCsvOutportButton() {
        if (csvOutportButton == null) {
            csvOutportButton = this.createImageButton(
                    "CSVエクスポート",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_export_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_export_on.jpg",
                    null,
                    true);
            /*
             "CSVエクスポート",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/csv_export_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/csv_export_on.jpg",
             null,
             true);
             */
            csvOutportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    com.geobeck.sosia.pos.hair.csv.CSVExportPanel csvep = new com.geobeck.sosia.pos.hair.csv.CSVExportPanel();
                    changeContents(csvep);
                }
            });
        }

        return csvOutportButton;
    }

    /**
     * 環境設定ボタンを取得する。
     *
     * @return 環境設定ボタン
     */
    public JButton getSettingButton() {
        if (settingButton == null) {
            settingButton = this.createImageButton(
                    "環境設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_environmental_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_environmental_setting_on.jpg",
                    null,
                    true);
            /*
             "環境設定",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/environmental_setting_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/environmental_setting_on.jpg",
             null,
             true);
             */
            settingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    EnvironmentalSettingPanel esp = new EnvironmentalSettingPanel();
                    changeContents(esp);
                }
            });
        }

        return settingButton;
    }

    public JButton getSettingKANZASHIButton() {
        if (SettingKANZASHI == null) {
            SettingKANZASHI = this.createImageButton(
                    "ｻｲﾄｺﾝﾄﾛｰﾙ設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/media_setting_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/media_setting_on.jpg",
                    null,
                    true);
            SettingKANZASHI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MediaSettingPanel esp = new MediaSettingPanel();
                    changeContents(esp);
                    esp.settingSalon();
                }
            });
        }

        return SettingKANZASHI;
    }

    /**
     * 会社マスタメニューを取得する。
     *
     * @return 会社マスタメニュー
     */
    public JPanel getCompanyMasterMenu() {
        if (companyMasterMenu == null) {
            companyMasterMenu = this.createSubMenu();

            //会社情報登録
            if ((SystemInfo.getGroup().getGroupID() == 1 && SystemInfo.getCurrentShop().getShopID() == 0) || (SystemInfo.checkAuthority(0))) {
                this.addSubMenu(companyMasterMenu, this.getMstCompanyButton(), -1);
            }

            //グループ登録
            if (SystemInfo.isGroup()) {
                this.addSubMenu(companyMasterMenu, this.getMstGroupButton(), -1);
            }

            // IVS_LTThuc start add 20140707 MASHU_業態登録
            if(SystemInfo.getCurrentShop().getShopID()==0) {
                this.addSubMenu(companyMasterMenu,this.getMstUseShopCategoryButton(),3);
            }else{
                if(SystemInfo.getCurrentShop().getUseShopCategory()==1){
                      this.addSubMenu(companyMasterMenu,this.getMstUseShopCategoryButton(),3);
                }


            }
            //IVS_LTThuc start end 20140707 MASHU_業態登録

            //店舗情報登録
            this.addSubMenu(companyMasterMenu, this.getMstShopButton(), 1);

            //マスタ登録権限
            if (!SystemInfo.getSystemType().equals(1)) {
                if ((SystemInfo.getCurrentShop().getShopID() == 0) || (SystemInfo.checkAuthority(2))) {
                    //if (!SystemInfo.isReservationOnly()) {
                    //IVS_LVTu start edit 2016/03/14 New request #49137
                    //if (!SystemInfo.getSystemType().equals(2)) {
                        this.addSubMenu(companyMasterMenu, this.getMstAuthorityButton(), -1);
                    //}
                    //IVS_LVTu end edit 2016/03/14 New request #49137
                }
            }

            // 出退勤パスワード変更
            if (SystemInfo.checkAuthority(3)) {
                //if (!SystemInfo.isReservationOnly()) {
                if (!SystemInfo.getSystemType().equals(1)) {
                    this.addSubMenu(companyMasterMenu, this.getMstWorkTimePassButton(), -1);
                }
            }

            //スタッフ区分登録
            this.addSubMenu(companyMasterMenu, this.getMstStaffClassButton(), 4);

            //スタッフ情報登録
            this.addSubMenu(companyMasterMenu, this.getMstStaffButton(), 5);

            //施術台登録
            if (SystemInfo.getCurrentShop().getShopID() == 0 || SystemInfo.getCurrentShop().isBed()) //本部、あるいは施術台を使用する店舗のみ表示する
            {
                this.addSubMenu(companyMasterMenu, this.getMstBedButton(), 6);
            }
            //Start add 20131102 lvut (merg rappa-->gb_source)
            //反響項分登録ボタン
            if (SystemInfo.checkAuthority(7)) {
                //if (!SystemInfo.isReservationOnly()) {
                //IVS_ptthu edit 25/04/2016
                //if (!SystemInfo.getSystemType().equals(1)) {
                    this.addSubMenu(companyMasterMenu, this.getMstResponseClassButton(), -1);
                //}
                //IVS_ptthu end edit 25/04/2016

            }
            //End add 20131102 lvut (merg rappa-->gb_source)
            // 反響項目登録ボタン
            if (SystemInfo.checkAuthority(7)) {
                //if (!SystemInfo.isReservationOnly()) {
                //IVS_ptthu edit 25/04/2016
                //if (!SystemInfo.getSystemType().equals(1)) {
                    this.addSubMenu(companyMasterMenu, this.getMstResponseButton(), -1);
                //}
                //IVS_ptthu end edit 25/04/2016
            }

            // 基本シフト登録
            this.addSubMenu(companyMasterMenu, this.getMstBasicShiftButton(), 8);

            // スタッフシフト登録
            this.addSubMenu(companyMasterMenu, this.getMstStaffShiftButton(), 8);

            // 20170413 add 来店カルテ設定 #61376
            if (SystemInfo.getUseVisitKarte()) {
                this.addSubMenu(companyMasterMenu, this.getVisitKarteSettingButton(), -1);
            }
        }

        return companyMasterMenu;
    }

    /**
     * 会社情報登録ボタンを取得する。
     *
     * @return 会社情報登録ボタン
     */
    public JButton getMstCompanyButton() {
        if (mstCompanyButton == null) {
            mstCompanyButton = this.createImageButton(
                    "会社情報登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_entry_on.jpg",
                    null,
                    false);
            /*
             "会社情報登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/company_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/company_master_on.jpg",
             null,
             false);

             */
            mstCompanyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstCompanyButton.getModel().setRollover(false);
                    MstCompanyPanel mcp = new MstCompanyPanel();
                    changeContents(mcp);
                }
            });
        }

        return mstCompanyButton;
    }

    /**
     * グループ登録ボタンを取得する。
     *
     * @return グループ登録ボタン
     */
    public JButton getMstGroupButton() {
        if (mstGroupButton == null) {
            mstGroupButton = this.createImageButton(
                    "グループ情報登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_group_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_group_entry_on.jpg",
                    null,
                    false);
            /*
             "グループ情報登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_on.jpg",
             null,
             false);

             */
            mstGroupButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstGroupButton.getModel().setRollover(false);
                    MstGroupPanel mgp = new MstGroupPanel();
                    mgp.setTitle("グループ情報登録");
                    changeContents(mgp);
                }
            });
        }

        return mstGroupButton;
    }
    //IVS_LTThuc start add 20140707 MASHU_業態登録
     public JButton getMstUseShopCategoryButton() {
        if (mstUseShopCategoryButton == null) {
            mstUseShopCategoryButton = this.createImageButton(
                    "業態登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/category_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/category_on.jpg",
                    null,
                    false);
            /*
             "グループ情報登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_on.jpg",
             null,
             false);

             */
            mstUseShopCategoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                     mstUseShopCategoryButton.getModel().setRollover(false);
                     SimpleMasterPanel smp = new SimpleMasterPanel(
                            "業態", "mst_shop_category", "shop_category_id",
                            "shop_class_name", 40,
                            SystemInfo.getTableHeaderRenderer());
                     smp.setPath("基本設定 >> 会社関連");

                    changeContents(smp);
                }
            });
        }

        return mstUseShopCategoryButton;
    }
    //IVS_LTThuc start end 20140707 MASHU_業態登録

    /**
     * マスタ登録権限ボタンを取得する。
     *
     * @return マスタ登録権限ボタン
     */
    public JButton getMstAuthorityButton() {
        if (mstAuthorityButton == null) {
            mstAuthorityButton = this.createImageButton(
                    "権限登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_conpetence_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_conpetence_entry_on.jpg",
                    null,
                    false);
            /*
             "マスタ登録権限",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/authority_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/authority_master_on.jpg",
             null,
             false);
             */

            mstAuthorityButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    // パスワード認証
                    if (WorkTimePasswordDialog.isAuthPassword()) {
                        mstAuthorityButton.getModel().setRollover(false);
                        com.geobeck.sosia.pos.hair.basicinfo.company.MstAuthorityPanel map = new com.geobeck.sosia.pos.hair.basicinfo.company.MstAuthorityPanel();
                        changeContents(map);

                    }
                }
            });
        }

        return mstAuthorityButton;
    }

    // Thanh add start 20130415 顧客契約履歴
    public JButton getCusContractHistoryButton() {
        if (cusContractHistoryButton == null) {
            cusContractHistoryButton = this.createImageButton(
                    "顧客契約履歴",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/cus_contract_history_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/cus_contract_history_on.jpg",
                    null,
                    false);

            cusContractHistoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!SystemInfo.checkAuthorityPassword(222)) {
                        return;
                    }
                    CusContractHistoryReportPanel panel = new CusContractHistoryReportPanel();
                    changeContents(panel);
                }
            });
        }

        return cusContractHistoryButton;
    }
    // Thanh add End 20130415 顧客契約履歴

    /**
     * 店舗情報登録ボタンを取得する。
     *
     * @return 店舗情報登録ボタン
     */
    public JButton getMstShopButton() {
        if (mstShopButton == null) {
            mstShopButton = this.createImageButton(
                    "店舗情報登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_shop_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_shop_entry_on.jpg",
                    null,
                    false);
            /*
             "店舗情報登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/shop_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/shop_master_on.jpg",
             null,
             false);
             */

            mstShopButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstShopButton.getModel().setRollover(false);
                    MstShopPanel msp = new MstShopPanel();
                    changeContents(msp);
                }
            });
        }

        return mstShopButton;
    }

    /**
     * スタッフ区分登録ボタンを取得する。
     *
     * @return スタッフ区分登録ボタン
     */
    public JButton getMstStaffClassButton() {
        if (mstStaffClassButton == null) {
            mstStaffClassButton = this.createImageButton(
                    "スタッフ区分登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_staff_divide_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_staff_divide_entry_on.jpg",
                    null,
                    false);

            mstStaffClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstStaffClassButton.getModel().setRollover(false);
                    MstStaffClassPanel mgcp = new MstStaffClassPanel();
                    changeContents(mgcp);
                }
            });
        }

        return mstStaffClassButton;
    }

    /**
     * スタッフ情報登録ボタンを取得する。
     *
     * @return スタッフ情報登録ボタン
     */
    public JButton getMstStaffButton() {
        if (mstStaffButton == null) {
            mstStaffButton = this.createImageButton(
                    "スタッフ情報登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_staff_intelligence_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_staff_intelligence_entry_on.jpg",
                    null,
                    false);
            /*
             "スタッフ情報登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/staff_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/staff_master_on.jpg",
             null,
             false);
             */

            mstStaffButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstStaffButton.getModel().setRollover(false);
                    MstStaffPanel msp = new MstStaffPanel();
                    changeContents(msp);
                }
            });
        }

        return mstStaffButton;
    }

    /**
     * 出退勤パスワード変更ボタンを取得する。
     *
     * @return 出退勤パスワード変更ボタン
     */
    public JButton getMstWorkTimePassButton() {
        if (mstWorkTimePassButton == null) {
            mstWorkTimePassButton = this.createImageButton(
                    "出退勤パスワード変更",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/change_password_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/change_password_on.jpg",
                    null,
                    false);
            /*
             "出退勤パスワード変更",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/work_time_pass_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/work_time_pass_master_on.jpg",
             null,
             false);

             */

            mstWorkTimePassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstWorkTimePassButton.getModel().setRollover(false);
                    StaffWorkTimePasswordPanel swtpp = new StaffWorkTimePasswordPanel();
                    changeContents(swtpp);
                }
            });
        }

        return mstWorkTimePassButton;
    }

    /**
     * 施術台登録ボタンを取得する。
     *
     * @return 施術台登録ボタン
     */
    public JButton getMstBedButton() {
        if (mstBedButton == null) {
            mstBedButton = this.createImageButton(
                    "施術台登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_bed_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_bed_entry_on.jpg",
                    null,
                    false);
            /*
             "施術台登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/bed_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/bed_master_on.jpg",
             null,
             false);
             */

            mstBedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstBedButton.getModel().setRollover(false);
                    MstBedPanel mbp = new MstBedPanel();
                    changeContents(mbp);
                }
            });
        }

        return mstBedButton;
    }
    //Start add 20131102 lvut (merg rappa --> gb_source)

    /**
     * 反響項目登録ボタンを取得する。
     *
     * @return 反響項目登録ボタン
     */
    public JButton getMstResponseClassButton() {
        if (mstResponseClassButton == null) {
            mstResponseClassButton = this.createImageButton(
                    "反響分類登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/regist_response_class_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/regist_response_class_on.jpg",
                    null,
                    false);

            mstResponseClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstResponseClassButton.getModel().setRollover(false);
                    MstResponseClassPanel smp = new MstResponseClassPanel(
                            "反響分類", "mst_response_class",
                            "response_class_id", "response_class_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("基本設定 >> 会社関連");
                    changeContents(smp);
                }
            });
        }

        return mstResponseClassButton;
    }

    //End add 20131102 lvut (merg rappa --> gb_source)
    /**
     * 反響項目登録ボタンを取得する。
     *
     * @return 反響項目登録ボタン
     */
    public JButton getMstResponseButton() {

        if (mstResponseButton == null) {
            mstResponseButton = this.createImageButton(
                    "反響項目登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_response_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_response_entry_on.jpg",
                    null,
                    false);

            mstResponseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstResponseButton.getModel().setRollover(false);
                    MstResponsePanel smp = new MstResponsePanel(
                            "反響項目", "mst_response",
                            "response_id", "response_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("基本設定 >> 会社マスタ");
                    changeContents(smp);
                }
            });
        }

        return mstResponseButton;
    }

    /**
     * 基本シフト登録ボタンを取得する。
     *
     * @return 基本シフト登録ボタン
     */
    public JButton getMstBasicShiftButton() {
        if (mstBasicShiftButton == null) {
            mstBasicShiftButton = this.createImageButton(
                    "基本シフト登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/default_shift_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/default_shift_on.jpg",
                    null,
                    false);

            // 押下イベント登録
            mstBasicShiftButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstBasicShiftButton.getModel().setRollover(false);
                    BasicShiftPanel bsp = new BasicShiftPanel();
                    changeContents(bsp);
                }
            });
        }

        return mstBasicShiftButton;
    }

    /**
     * スタッフシフト登録ボタンを取得する。
     *
     * @return スタッフシフト登録ボタン
     */
    public JButton getMstStaffShiftButton() {
        if (mstStaffShiftButton == null) {
            mstStaffShiftButton = this.createImageButton(
                    "スタッフシフト登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/staff_shift_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/staff_shift_on.jpg",
                    null,
                    false);

            // 押下イベント登録
            mstStaffShiftButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstStaffShiftButton.getModel().setRollover(false);
                    StaffShiftPanel ssp = new StaffShiftPanel();
                    changeContents(ssp);
                }
            });
        }

        return mstStaffShiftButton;
    }

    /**
     * 商品マスタメニューを取得する。
     *
     * @return 商品マスタメニュー
     */
    public JPanel getItemMasterMenu() {
        if (itemMasterMenu == null) {
            itemMasterMenu = this.createSubMenu();

            //商品分類登録
            this.addSubMenu(itemMasterMenu, this.getMstItemClassButton(), 10);

            //商品登録
            this.addSubMenu(itemMasterMenu, this.getMstItemButton(), 11);

            //使用商品登録
            this.addSubMenu(itemMasterMenu, this.getMstUseItemButton(), 12);

//			//仕入先登録
//			this.addSubMenu(itemMasterMenu, this.getMstSupplierButton(), 13 );

            //仕入先登録
            this.addSubMenu(itemMasterMenu, this.getMstSupplierItemButton(), 13);

            //在庫調整区分登録
            this.addSubMenu(itemMasterMenu, this.getMstDestockingDivisionButton(), 14);

            // 置場マスタ
            this.addSubMenu(itemMasterMenu, this.getMstPlaceButton(), 15);
        }

        return itemMasterMenu;
    }

    /**
     * 商品分類登録ボタンを取得する。
     *
     * @return 商品分類登録ボタン
     */
    public JButton getMstItemClassButton() {
        if (mstItemClassButton == null) {
            mstItemClassButton = this.createImageButton(
                    "商品分類登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_classify_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_classify_entry_on.jpg",
                    null,
                    false);
            /*
             "商品分類登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/item_class_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/item_class_master_on.jpg",
             null,
             false);
             */
            mstItemClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstItemClassButton.getModel().setRollover(false);
                    MstItemClassPanel mgcp = new MstItemClassPanel();
                    changeContents(mgcp);
                }
            });
        }

        return mstItemClassButton;
    }

    /**
     * 商品登録ボタンを取得する。
     *
     * @return 商品登録ボタン
     */
    public JButton getMstItemButton() {
        if (mstItemButton == null) {
            mstItemButton = this.createImageButton(
                    "商品登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_entry_classify_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_entry_classify_on.jpg",
                    null,
                    false);
            /*
             "商品登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/item_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/item_master_on.jpg",
             null,
             false);
             */
            mstItemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstItemButton.getModel().setRollover(false);
                    MstItemPanel mgp = new MstItemPanel();

                    if (mgp.checkClassRegisted()) {
                        changeContents(mgp);
                    } else {
                        MessageDialog.showMessageDialog(mgp,
                                MessageUtil.getMessage(7004, "商品分類"),
                                mgp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mgp = null;

                        MstItemClassPanel mgcp = new MstItemClassPanel();
                        changeContents(mgcp);
                    }
                }
            });
        }

        return mstItemButton;
    }

    /**
     * 使用商品登録ボタンを取得する。
     *
     * @return 使用商品登録ボタン
     */
    public JButton getMstUseItemButton() {
        if (mstUseItemButton == null) {
            mstUseItemButton = this.createImageButton(
                    "使用商品登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_employ_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_employ_entry_on.jpg",
                    null,
                    false);
            /*
             "使用商品登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/use_item_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/use_item_master_on.jpg",
             null,
             false);
             */
            mstUseItemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseItemButton.getModel().setRollover(false);
                    MstUseProductPanel mup = new MstUseProductPanel(2);
                    mup.setTitle("使用商品登録");
                    changeContents(mup);
                }
            });
        }

        return mstUseItemButton;
    }

    /**
     * 仕入先登録ボタンを取得する。
     *
     * @return 仕入先登録ボタン
     */
    public JButton getMstSupplierButton() {
        if (mstSupplierButton == null) {
            mstSupplierButton = this.createImageButton(
                    "仕入先登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_on.jpg",
                    null,
                    false);
            /*
             "仕入先登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/supplier_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/supplier_master_on.jpg",
             null,
             false);
             */
            mstSupplierButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstSupplierButton.getModel().setRollover(false);
                    MstSupplierPanel msp = new MstSupplierPanel();
                    changeContents(msp);
                }
            });
        }

        return mstSupplierButton;
    }

    /**
     * 仕入先登録ボタンを取得する。
     *
     * @return 仕入先登録ボタン
     */
    public JButton getMstSupplierItemButton() {
        if (mstSupplierItemButton == null) {
            mstSupplierItemButton = this.createImageButton(
                    "仕入先登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_on.jpg",
                    null,
                    false);
            /*
             "仕入先登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/supplier_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/supplier_master_on.jpg",
             null,
             false);
             */
            mstSupplierItemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstSupplierItemButton.getModel().setRollover(false);
                    MstSupplierItemPanel msp = new MstSupplierItemPanel();
                    changeContents(msp);
                }
            });
        }

        return mstSupplierItemButton;
    }

    /**
     * 在庫調整区分登録ボタンを取得する。
     *
     * @return 在庫調整区分登録ボタン
     */
    public JButton getMstDestockingDivisionButton() {
        if (mstDestockingDivisionButton == null) {
            mstDestockingDivisionButton = this.createImageButton(
                    "在庫調整区分登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_adjust-division_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_adjust-division_on.jpg",
                    null,
                    false);
            /*
             "在庫調整区分登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/destocking_division_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/destocking_division_master_on.jpg",
             null,
             false);
             */

            mstDestockingDivisionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstDestockingDivisionButton.getModel().setRollover(false);
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "在庫調整区分", "mst_destocking_division", "destocking_division_id",
                            "destocking_division_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("基本設定 >> 商品マスタ");
                    changeContents(smp);
                }
            });
        }

        return mstDestockingDivisionButton;
    }

    /**
     * 技術マスタメニューを取得する。
     *
     * @return 技術マスタメニュー
     */
    public JPanel getTechnicMasterMenu() {
        if (technicMasterMenu == null) {
            technicMasterMenu = this.createSubMenu();

            //技術分類登録
            this.addSubMenu(technicMasterMenu, this.getMstTechnicClassButton(), 20);

            //技術登録
            this.addSubMenu(technicMasterMenu, this.getMstTechnicButton(), 21);

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                //按分マスタ登録
                if (SystemInfo.getCurrentShop().getShopID() == 0 || SystemInfo.getCurrentShop().isProportionally()) //本部、あるいは按分を使用する店舗のみ表示する
                {
                    this.addSubMenu(technicMasterMenu, this.getMstProportionallyButton(), 22);
                }

                //技術按分登録
                if (SystemInfo.getCurrentShop().getShopID() == 0 || SystemInfo.getCurrentShop().isProportionally()) //本部、あるいは按分を使用する店舗のみ表示する
                {
                    this.addSubMenu(technicMasterMenu, this.getMstTechProportionallyButton(), 22);
                }
            }

            //使用技術登録
            this.addSubMenu(technicMasterMenu, this.getMstUseTechnicButton(), 23);

            // この画面の設定値はどこにも反映されていないため一旦メニューから削除する
            // スタッフ毎施術時間登録
            //this.addSubMenu(technicMasterMenu, this.getMstStaffTechnicTimeButton(), 24 );
            // start edit 20130802 nakhoa 役務機能使用有無の制御追加
            //IVS NNTUAN START EDIT 20131028
            /*
             if(SystemInfo.getCurrentShop().getCourseFlag() != null){
             if(SystemInfo.getCurrentShop().getCourseFlag() == 1){
             //コース契約分類登録
             this.addSubMenu(technicMasterMenu, this.getMstCourseClassButton(), 25);

             //コース登録
             this.addSubMenu(technicMasterMenu, this.getMstCourseButton(), 26);

             //使用コース登録
             this.addSubMenu(technicMasterMenu, this.getMstUseCourseButton(), 27);
             }
             }*/
            if (SystemInfo.getCurrentShop().getShopID() != 0) {
                if (SystemInfo.getCurrentShop().getCourseFlag() != null) {
                    if (SystemInfo.getCurrentShop().getCourseFlag() == 1) {
                        //コース契約分類登録
                        this.addSubMenu(technicMasterMenu, this.getMstCourseClassButton(), 25);

                        //コース登録
                        this.addSubMenu(technicMasterMenu, this.getMstCourseButton(), 26);

                        //使用コース登録
                        this.addSubMenu(technicMasterMenu, this.getMstUseCourseButton(), 27);
                    }
                }
            } else {
                //コース契約分類登録
                this.addSubMenu(technicMasterMenu, this.getMstCourseClassButton(), 25);

                //コース登録
                this.addSubMenu(technicMasterMenu, this.getMstCourseButton(), 26);

                //使用コース登録
                this.addSubMenu(technicMasterMenu, this.getMstUseCourseButton(), 27);

				//マスタ一括登録 add start 2016/12/28
				if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
					//技術一括登録
					this.addSubMenu(technicMasterMenu, this.getMstTechnicRegistBulkButton(), 28);
				}
				//マスタ一括登録 add end 2016/12/28
            }
            //IVS NNTUAN END EDIT 20131028
            // end edit 20130802 nakhoa 役務機能使用有無の制御追加
        }

        return technicMasterMenu;
    }

    /**
     * 技術分類登録ボタンを取得する。
     *
     * @return 技術分類登録ボタン
     */
    public JButton getMstTechnicClassButton() {
        if (mstTechnicClassButton == null) {
            mstTechnicClassButton = this.createImageButton(
                    "技術分類登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_classify_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_classify_entry_on.jpg",
                    null,
                    false);
            /*
             "技術分類登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_class_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_class_master_on.jpg",
             null,
             false);

             */
            mstTechnicClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstTechnicClassButton.getModel().setRollover(false);
                    MstTechnicClassPanel mtcp = new MstTechnicClassPanel();
                    changeContents(mtcp);
                }
            });
        }

        return mstTechnicClassButton;
    }

    /**
     * 技術登録ボタンを取得する。
     *
     * @return 技術登録ボタン
     */
    public JButton getMstTechnicButton() {
        if (mstTechnicButton == null) {
            mstTechnicButton = this.createImageButton(
                    "技術登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_entry_on.jpg",
                    null,
                    false);
            /*
             "技術登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_master_on.jpg",
             null,
             false);
             */
            mstTechnicButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstTechnicButton.getModel().setRollover(false);
                    MstTechnicPanel mtp = new MstTechnicPanel();

                    if (mtp.checkClassRegisted()) {
                        changeContents(mtp);
                    } else {
                        MessageDialog.showMessageDialog(mtp,
                                MessageUtil.getMessage(7004, "技術分類"),
                                mtp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mtp = null;

                        MstTechnicClassPanel mtcp = new MstTechnicClassPanel();
                        changeContents(mtcp);
                    }
                }
            });
        }

        return mstTechnicButton;
    }

    /**
     * 按分マスタ登録ボタンを取得する。
     *
     * @return 按分マスタ登録ボタン
     */
    public JButton getMstProportionallyButton() {
        if (mstProportionallyButton == null) {
            mstProportionallyButton = this.createImageButton(
                    "按分マスタ登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_mst_money_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_mst_money_entry_on.jpg",
                    null,
                    false);
            /*
             "按分マスタ登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/proportionally_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/proportionally_master_on.jpg",
             null,
             false);
             */
            mstProportionallyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstProportionallyButton.getModel().setRollover(false);
                    MstProportionallyPanel mpp = new MstProportionallyPanel();
                    changeContents(mpp);
                }
            });
        }

        return mstProportionallyButton;
    }

    /**
     * 技術按分登録ボタンを取得する。
     *
     * @return 技術按分登録ボタン
     */
    public JButton getMstTechProportionallyButton() {
        if (mstTechProportionallyButton == null) {
            mstTechProportionallyButton = this.createImageButton(
                    "技術按分登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_money_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_money_entry_on.jpg",
                    null,
                    false);
            /*
             "技術按分登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/proportionally_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/proportionally_master_on.jpg",
             null,
             false);
             */
            mstTechProportionallyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstTechProportionallyButton.getModel().setRollover(false);
                    MstTechnicProportionallyPanel mpp = new MstTechnicProportionallyPanel();
                    changeContents(mpp);
                }
            });
        }

        return mstTechProportionallyButton;
    }

    /**
     * 使用技術登録ボタンを取得する。
     *
     * @return 使用技術登録ボタン
     */
    public JButton getMstUseTechnicButton() {
        if (mstUseTechnicButton == null) {
            mstUseTechnicButton = this.createImageButton(
                    "使用技術登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_employ_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_employ_entry_on.jpg",
                    null,
                    false);
            /*
             "使用技術登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/use_technic_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/use_technic_master_on.jpg",
             null,
             false);

             */
            mstUseTechnicButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseTechnicButton.getModel().setRollover(false);
                    MstUseProductPanel mup = new MstUseProductPanel(1);
                    mup.setTitle("使用技術登録");
                    mup.setPath("基本設定 >> 技術マスタ");
                    changeContents(mup);
                }
            });
        }

        return mstUseTechnicButton;
    }

    /**
     * コース契約分類登録ボタンを取得する。
     *
     * @return コース契約分類登録ボタン
     */
    public JButton getMstCourseClassButton() {
        if (mstCourseClassButton == null) {
            mstCourseClassButton = this.createImageButton(
                    "コース契約分類登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_class_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_class_on.jpg",
                    null,
                    false);

            mstCourseClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstCourseClassButton.getModel().setRollover(false);
                    MstCourseClassPanel mccp = new MstCourseClassPanel();
                    mccp.setTitle("コース契約分類登録");
                    mccp.setPath("基本設定 >> 技術マスタ");
                    changeContents(mccp);
                }
            });
        }

        return mstCourseClassButton;
    }

    /**
     * コース契約分類登録ボタンを取得する。
     *
     * @return コース契約分類登録ボタン
     */
    public JButton getMstCourseButton() {
        if (mstCourseButton == null) {
            mstCourseButton = this.createImageButton(
                    "コース登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_on.jpg",
                    null,
                    false);

            mstCourseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstCourseButton.getModel().setRollover(false);
                    MstCoursePanel mccp = new MstCoursePanel();
                    mccp.setTitle("コース登録");
                    mccp.setPath("基本設定 >> 技術マスタ");
                    changeContents(mccp);
                }
            });
        }

        return mstCourseButton;
    }

    /**
     * スタッフ成績メニューを取得する。
     *
     * @return スタッフ成績メニュー
     */
    public JPanel getCustomMenu() {
        if (customMenu == null) {
            customMenu = this.createSubMenu();
            if (SystemInfo.getDatabase().equals("pos_hair_nons")
                    || SystemInfo.getDatabase().equals("pos_hair_nons_bak") || SystemInfo.getDatabase().equals("pos_hair_dev")) {
                //売上推移表
                this.addSubMenu(customMenu, this.getSaleTransittionButton(), 95);


                //this.addSubMenu(customMenu, this.getVisitReporttListButton(), 106);

                //this.addSubMenu(customMenu, this.TrendRateRepeatButton(), 107);
            }
            if (SystemInfo.getDatabase().equals("pos_hair_ox2")
                    || SystemInfo.getDatabase().equals("pos_hair_dev")) {
                this.addSubMenu(customMenu, this.SalesComparisionButton(), 108);
                this.addSubMenu(customMenu, this.TurnOverAnalyzeButton(), 109);
                this.addSubMenu(customMenu, this.SalesReportButton(), 110);
            }


        }
        return customMenu;
    }

    /**
     * 使用コース登録ボタンを取得する。
     *
     * @return 使用技術登録ボタン
     */
    public JButton getMstUseCourseButton() {
        if (mstUseCourseButton == null) {
            mstUseCourseButton = this.createImageButton(
                    "使用コース登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_use_course_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_use_course_on.jpg",
                    null,
                    false);

            mstUseCourseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseCourseButton.getModel().setRollover(false);
                    MstUseProductPanel mcp = new MstUseProductPanel(3);
                    mcp.setTitle("使用コース登録");
                    mcp.setPath("基本設定 >> 技術マスタ");
                    changeContents(mcp);
                }
            });
        }

        return mstUseCourseButton;
    }

	//マスタ一括登録 add start 2016/12/28
	/**
     * 技術一括登録ボタンを取得する。
     *
     * @return 技術一括登録ボタン
     */
    public JButton getMstTechnicRegistBulkButton() {
        if (mstTechnicRegistBulkButton == null) {
            mstTechnicRegistBulkButton = this.createImageButton(
                    "技術一括登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_regist_bulk_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_regist_bulk_on.jpg",
                    null,
                    false);

 			mstTechnicRegistBulkButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseCourseButton.getModel().setRollover(false);
                    MstTechnicRegistBulkPanel mcp = new MstTechnicRegistBulkPanel(3);
                    mcp.setTitle("技術一括登録");
                    mcp.setPath("基本設定 >> 技術マスタ");
                    changeContents(mcp);
                }
            });
        }

        return mstTechnicRegistBulkButton;
    }
	//マスタ一括登録 add end 2016/12/28

    /**
     * 使用技術登録ボタンを取得する。
     *
     * @return 使用技術登録ボタン
     */
    public JButton getMstStaffTechnicTimeButton() {
        if (mstStaffTechnicTimeButton == null) {
            mstStaffTechnicTimeButton = this.createImageButton(
                    "スタッフ毎施術時間登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_staff_settime_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_staff_settime_entry_on.jpg",
                    null,
                    false);
            /*
             "スタッフ毎施術時間登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/staff_technic_time_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/staff_technic_time_on.jpg",
             null,
             false);
             */
            mstStaffTechnicTimeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstStaffTechnicTimeButton.getModel().setRollover(false);
                    MstStaffTechnicTimePanel msttp = new MstStaffTechnicTimePanel();
                    changeContents(msttp);
                }
            });
        }

        return mstStaffTechnicTimeButton;
    }

    /**
     * 来店カルテ設定ボタンを取得する。
     * 20170413 add #61376
     *
     * @return 来店カルテ設定ボタン
     */
    public JButton getVisitKarteSettingButton() {

        if (visitKarteSettingButton == null) {
            visitKarteSettingButton = this.createImageButton(
                    "来店カルテ設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/visit_karte_setting_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/visit_karte_setting_on.jpg",
                    null,
                    false);

            visitKarteSettingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    visitKarteSettingButton.getModel().setRollover(false);
                    VisitKarteSettingPanel vksp = new VisitKarteSettingPanel(
							"来店カルテ設定");
					vksp.setPath("基本設定 >> 会社関連");
					changeContents(vksp);
                                        vksp.checkAutoNumbering();
                }
            });
        }

        return visitKarteSettingButton;
    }

//add
    /**
     * 顧客分析メニューを取得する。
     *
     * @return 顧客分析メニュー
     */
    public JPanel getCustomerEffectMenu() {
        if (customerEffectMenu == null) {
            customerEffectMenu = this.createSubMenu();

            //動向分析
            this.addSubMenu(customerEffectMenu, this.getReappearancePredictionEffectButton(), 70);

            //TODO 顧客分析サブメニュー サイクル分析
            //this.addSubMenu(customerEffectMenu, this.getKarteAnalysisEffectButton(), -1 );

            //クロス分析
            if (!SystemInfo.getSystemType().equals(2)) {
            this.addSubMenu(customerEffectMenu, this.getCrossAnalysisEffectButton(), 71);

            //クロス分析5
            this.addSubMenu(customerEffectMenu, this.getCrossAnalysis5EffectButton(), 72);
            }

            //Luc Add Start 20121004 エリア分析
            this.addSubMenu(customerEffectMenu, this.getCustomerAreaAnalysisButton(), 74);
            //Luc Add End 20121004 エリア分析

            //Luc Add Start 20121004 顧客属性分析
            if (!SystemInfo.getSystemType().equals(2)) {
            this.addSubMenu(customerEffectMenu, this.getCustomerAnalysisAttributeButton(), 73);
            }
            //Luc Add End 20121004 顧客属性分析

            //GB_Mashu Task #34581 [Product][Code][Phase3]マッシャー分析
            //IVS_nahoang start add 20141229
            this.addSubMenu(customerEffectMenu, this.getMasherAnalysis5PanelButton(), 75);
            //IVS_nahoang end add 20141229
            //カルテ分析
            if (SystemInfo.getSetteing().isItoAnalysis()) {
                this.addSubMenu(customerEffectMenu, this.getItoAnalitic3jigenButton(), -1);
            }

            //Thanh Add Start 20130415 顧客契約履歴
            if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_dev") || SystemInfo.getDatabase().startsWith("pos_hair_dev2") || SystemInfo.getDatabase().startsWith("pos_hair_nons_bak")) {
                this.addSubMenu(customerEffectMenu, this.getCusContractHistoryButton(), 75);
            }
            //Thanh Add End 20130415 顧客契約履歴


            //TODO 顧客分析サブメニュー 客数問題発見シートα
            //this.addSubMenu(customerEffectMenu, this.getCustomerProblemSheetButton(), -1 );
        }
        return customerEffectMenu;
    }

    /**
     * 売上分析メニューを取得する。
     *
     * @return 売上分析メニュー
     */
    public JPanel getSalesEffectMenu() {
        if (salesEffectMenu == null) {
            salesEffectMenu = this.createSubMenu();

            //時間帯分析
            this.addSubMenu(salesEffectMenu, this.getTimeAnalysisButton(), 80);

            //売上構成分析
            if (SystemInfo.getSetteing().isItoAnalysis()) {
                this.addSubMenu(salesEffectMenu, this.getItoCustomerProblemSheetButton(), -1);
            }

            //売上推移
            this.addSubMenu(salesEffectMenu, this.getSalesTotalButton(), 81);
            //Luc start edit 20150623
            //  パスブック集計 vtbphuong start add 20150603
            //IVS_TMTrong start edit 20150730 New request #41198
            //if(SystemInfo.getDatabase().startsWith("pos_hair_missionf_dev") ) {
            if(SystemInfo.getDatabase().startsWith("pos_hair_missionf") ||SystemInfo.getDatabase().startsWith("pos_hair_missionf_dev") ) {
            //IVS_TMTrong end edit 20150730 New request #41198
            //Luc end edit 20150623
                this.addSubMenu(salesEffectMenu, this.getPassBookButton(), 96);
            }
           // vtbphuong end add 20150603
        }
        return salesEffectMenu;
    }

    private JButton SalesComparisionButton() {
        if (salesComparisionButton == null) {
            salesComparisionButton = this.createImageButton(
                    "比較集計表",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/custom_sales_comparison_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/custom_sales_comparison_on.jpg",
                    null,
                    false);
            salesComparisionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    // StaffResultListReportPanel srp = new StaffResultListReportPanel();
                    EarningReportPanel brp = new EarningReportPanel();
                    changeContents(brp);
                }
            });
        }

        return salesComparisionButton;
    }

    private JButton TurnOverAnalyzeButton() {
        if (TurnOverAnalyzeButton == null) {
            TurnOverAnalyzeButton = this.createImageButton(
                    "売上分析表",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/turnOverAnalyze_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/turnOverAnalyze_on.jpg",
                    null,
                    false);
            TurnOverAnalyzeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    TurnOverAnlyzePanel rrp = new TurnOverAnlyzePanel();
                    changeContents(rrp);
                }
            });
        }

        return TurnOverAnalyzeButton;
    }

    private JButton SalesReportButton() {
        if (salesReportButton == null) {
            salesReportButton = this.createImageButton(
                    "来店カルテ分析",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/raiten_karte_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/raiten_karte_report_on.jpg",
                    null,
                    false);
            salesReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MedicalRecordAnalysisPanel brp = new MedicalRecordAnalysisPanel();
                    changeContents(brp);
                }
            });
        }

        return salesReportButton;
    }

//    public JButton getTurnOverAnalyzeButton() {
//        if (TurnOverAnalyzeButton == null) {
//            TurnOverAnalyzeButton = this.createImageButton(
//                    "売上分析表",
//                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/turnOverAnalyze_off.jpg",
//                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/turnOverAnalyze_on.jpg",
//                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/turnOverAnalyze_on.jpg",
//                    true);
//
//            TurnOverAnalyzeButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent ae) {
//
//                    TurnOverAnlyzePanel rrp = new TurnOverAnlyzePanel();
//                    changeContents(rrp);
//                }
//            });
//        }
//
//        return TurnOverAnalyzeButton;
//    }
    public JPanel getRepeatAnalysisMenu() {
        if (repeatAnalysisMenu == null) {
            repeatAnalysisMenu = this.createSubMenu();
            //反響分析
            this.addSubMenu(repeatAnalysisMenu, this.getResponseEffectButton(), 80);
            //反響リピート
            // 20170705 GB Start Edit #17672 [GB内対応][gb] 反響リピート分析のメニュー項目を非表示にする
            // this.addSubMenu(repeatAnalysisMenu, this.getResponseRepeatAnalyzetButton(), 81);
            // 20170705 GB End Edit #17672 [GB内対応][gb] 反響リピート分析のメニュー項目を非表示にする
        }
        return repeatAnalysisMenu;

    }

    /**
     * 再来分析メニューを取得する。
     *
     * @return 売上分析メニュー
     */
    public JPanel getReappearanceEffectMenu() {
        if (reappearanceEffectMenu == null) {
            reappearanceEffectMenu = this.createSubMenu();

            // 再来分析 add
            this.addSubMenu(reappearanceEffectMenu, this.getReappearanceButton(), -1);
        }
        return reappearanceEffectMenu;
    }

    /**
     * スタッフ成績メニューを取得する。
     *
     * @return スタッフ成績メニュー
     */
    public JPanel getStaffResultMenu() {
        if (staffResultMenu == null) {
            staffResultMenu = this.createSubMenu();

            //担当別成績一覧表
            this.addSubMenu(staffResultMenu, this.getStaffResultListButton(), 90);
            //担当別技術成績
            this.addSubMenu(staffResultMenu, this.getStaffResultTechButton(), 91);
            //担当別商品成績
            this.addSubMenu(staffResultMenu, this.getStaffResultGoodsButton(), 92);
            //担当別顧客成績
            this.addSubMenu(staffResultMenu, this.getStaffResultCustomerButton(), 93);
            //担当別再来成績
            this.addSubMenu(staffResultMenu, this.getStaffResultRepeatButton(), 94);
        }
        return staffResultMenu;
    }

//add end
    /**
     * 顧客マスタメニューを取得する。
     *
     * @return 顧客マスタメニュー
     */
    public JPanel getCustomerMasterMenu() {
        if (customerMasterMenu == null) {
            customerMasterMenu = this.createSubMenu();

            //顧客情報登録 add
            this.addSubMenu(customerMasterMenu, this.getRegistCustomerButton(), 33);

            //非会員一覧 add
            this.addSubMenu(customerMasterMenu, this.getNotMemberListButton(), 34);

            //ケータイ会員一覧
            boolean isAddMenu = false;
            if (SystemInfo.getCurrentShop().getShopID() == 0) {
                isAddMenu = (SystemInfo.getSosiaGearShopList().size() > 0);
            } else {
                isAddMenu = SystemInfo.isSosiaGearEnable();
            }
            if (isAddMenu) {
                this.addSubMenu(customerMasterMenu, this.getMobileMemberListButton(), -1);
            }

            // 職業登録ボタン
            this.addSubMenu(customerMasterMenu, this.getMstJobButton(), 30);

            // 初回来店動機登録ボタン
            this.addSubMenu(customerMasterMenu, this.getMstFirstComingMotiveButton(), 38);

            // 顧客ランク設定
            if(!SystemInfo.getSystemType().equals(1)) {
                this.addSubMenu(customerMasterMenu, this.getMstCustomerRankSettingButton(), 39);
            }

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                // カルテ分類登録ボタン
                this.addSubMenu(customerMasterMenu, this.getMstKarteClassButton(), 35);

                // カルテ詳細登録ボタン
                this.addSubMenu(customerMasterMenu, this.getMstKarteDetailButton(), 36);

                // カルテ参照項目登録ボタン
                this.addSubMenu(customerMasterMenu, this.getMstKarteReferenceButton(), 37);
            }

            // フリー項目区分登録ボタン
            this.addSubMenu(customerMasterMenu, this.getMstFreeHeadingClassButton(), 31);

            // フリー項目登録ボタン
            this.addSubMenu(customerMasterMenu, this.getMstFreeHeadingButton(), 32);

            // 顧客統合ボタン
            this.addSubMenu(customerMasterMenu, this.getMstCustomerIntegrationButton(), -1);
        }

        return customerMasterMenu;
    }

    /**
     * 職業登録ボタンを取得する。
     *
     * @return 職業登録ボタン
     */
    private JButton getMstJobButton() {
        if (mstJobButton == null) {
            mstJobButton = this.createImageButton(
                    "職業登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_on.jpg",
                    null,
                    false);
            /*
             "職業登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_on.jpg",
             null,
             false);
             */
            mstJobButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "職業", "mst_job", "job_id", "job_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("基本設定 >> 顧客マスタ");
                    changeContents(smp);
                }
            });
        }

        return mstJobButton;
    }

    /**
     * 初回来店動機登録ボタンを取得する。
     *
     * @return 初回来店動機登録ボタン
     */
    private JButton getMstFirstComingMotiveButton() {
        if (mstFirstComingMotiveButton == null) {
            mstFirstComingMotiveButton = this.createImageButton(
                    "初回来店動機登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_f_motive.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_f_motive_on.jpg",
                    null,
                    false);
            mstFirstComingMotiveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "初回来店動機", "mst_first_coming_motive", "first_coming_motive_class_id", "first_coming_motive_name", 30,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("基本設定 >> 顧客マスタ");
                    changeContents(smp);
                }
            });
        }

        return mstFirstComingMotiveButton;
    }

    /**
     * 顧客ランク設定ボタンを取得する。
     *
     * @return 顧客ランク設定ボタン
     */
    private JButton getMstCustomerRankSettingButton() {
        if (mstCustomerRankSettingButton == null) {
            mstCustomerRankSettingButton = this.createImageButton(
                    "顧客ランク設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/customer_rank_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/customer_rank_on.jpg",
                    null,
                    false);
            mstCustomerRankSettingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstCustomerRankSettingButton.getModel().setRollover(false);
                    MstCustomerRankSettingPanel p = new MstCustomerRankSettingPanel();
                    changeContents(p);
                }
            });
        }

        return mstCustomerRankSettingButton;
    }

    /**
     * カルテ分類登録ボタンを取得する。
     *
     * @return カルテ分類登録ボタン
     */
    private JButton getMstKarteClassButton() {
        if (mstKarteClassButton == null) {
            mstKarteClassButton = this.createImageButton(
                    "カルテ分類登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_classified_chart_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_classified_chart_on.jpg",
                    null,
                    false);
            mstKarteClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstKarteClassButton.getModel().setRollover(false);
                    MstKarteClassPanel mkcp = new MstKarteClassPanel();
                    changeContents(mkcp);
                }
            });
        }

        return mstKarteClassButton;
    }

    /**
     * カルテ詳細登録ボタンを取得する。
     *
     * @return カルテ詳細登録ボタン
     */
    private JButton getMstKarteDetailButton() {
        if (mstKarteDetailButton == null) {
            mstKarteDetailButton = this.createImageButton(
                    "カルテ詳細登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_details_chart_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_details_chart_on.jpg",
                    null,
                    false);
            mstKarteDetailButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstKarteDetailButton.getModel().setRollover(false);
                    MstKarteDetailPanel mkdp = new MstKarteDetailPanel();

                    if (mkdp.checkClassRegisted()) {
                        changeContents(mkdp);
                    } else {
                        MessageDialog.showMessageDialog(mkdp,
                                MessageUtil.getMessage(7004, "カルテ分類"),
                                mkdp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mkdp = null;

                        mstKarteClassButton.getModel().setRollover(false);
                        MstKarteClassPanel mkcp = new MstKarteClassPanel();
                        changeContents(mkcp);
                    }
                }
            });
        }

        return mstKarteDetailButton;
    }

    /**
     * カルテ参照項目登録ボタンを取得する。
     *
     * @return カルテ参照項目登録ボタン
     */
    private JButton getMstKarteReferenceButton() {
        if (mstKarteReferenceButton == null) {
            mstKarteReferenceButton = this.createImageButton(
                    "カルテ参照項目登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_reference_chart_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_reference_chart_on.jpg",
                    null,
                    false);
            mstKarteReferenceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstKarteReferenceButton.getModel().setRollover(false);
                    MstKarteReferencePanel mkrp = new MstKarteReferencePanel();

                    if (mkrp.checkClassRegisted()) {
                        changeContents(mkrp);
                    } else {
                        MessageDialog.showMessageDialog(mkrp,
                                MessageUtil.getMessage(7004, "カルテ詳細"),
                                mkrp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mkrp = null;

                        MstKarteDetailPanel mkdp = new MstKarteDetailPanel();

                        if (mkdp.checkClassRegisted()) {
                            changeContents(mkdp);
                        } else {
                            MessageDialog.showMessageDialog(mkdp,
                                    MessageUtil.getMessage(7004, "カルテ分類"),
                                    mkdp.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            mkdp = null;

                            mstKarteClassButton.getModel().setRollover(false);
                            MstKarteClassPanel mkcp = new MstKarteClassPanel();
                            changeContents(mkcp);
                        }
                    }
                }
            });
        }

        return mstKarteReferenceButton;
    }

    /**
     * フリー項目区分登録ボタンを取得する。
     *
     * @return フリー項目区分登録ボタン
     */
    public JButton getMstFreeHeadingClassButton() {
        if (mstFreeHeadingClassButton == null) {
            mstFreeHeadingClassButton = this.createImageButton(
                    "フリー項目区分登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_class_master_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_class_master_on.jpg",
                    null,
                    false);
            /*
             "フリー項目区分登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_class_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_class_master_on.jpg",
             null,
             false);
             */
            mstFreeHeadingClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstFreeHeadingButton.getModel().setRollover(false);
                    MstFreeHeadingClassPanel mfhcp = new MstFreeHeadingClassPanel();
                    changeContents(mfhcp);
                }
            });
        }

        return mstFreeHeadingClassButton;
    }

    /**
     * フリー項目情報登録ボタンを取得する。
     *
     * @return フリー項目情報登録ボタン
     */
    public JButton getMstFreeHeadingButton() {
        if (mstFreeHeadingButton == null) {
            mstFreeHeadingButton = this.createImageButton(
                    "フリー項目登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_master_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_master_on.jpg",
                    null,
                    false);
            mstFreeHeadingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstFreeHeadingButton.getModel().setRollover(false);
                    MstFreeHeadingPanel mfhp = new MstFreeHeadingPanel();

                    // 有効フリー項目区分が存在するかを確認する
                    if (mfhp.isRequires()) {
                        changeContents(mfhp);
                    } else {
                        MessageDialog.showMessageDialog(mfhp,
                                MessageUtil.getMessage(7004, "フリー項目区分"),
                                mfhp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mfhp = null;

                        MstFreeHeadingClassPanel mfhcp = new MstFreeHeadingClassPanel();
                        changeContents(mfhcp);
                    }
                }
            });
        }

        return mstFreeHeadingButton;
    }

    /**
     * 顧客統合ボタンを取得する。
     *
     * @return 顧客統合ボタン
     */
    public JButton getMstCustomerIntegrationButton() {
        if (mstCustomerIntegrationButton == null) {
            mstCustomerIntegrationButton = this.createImageButton(
                    "顧客統合",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/customer_integration_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/customer_integration_on.jpg",
                    null,
                    false);
            mstCustomerIntegrationButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstFreeHeadingButton.getModel().setRollover(false);
                    MstCustomerIntegrationPanel mfhcp = new MstCustomerIntegrationPanel();
                    changeContents(mfhcp);
                }
            });
        }

        return mstCustomerIntegrationButton;
    }

    /**
     * 精算マスタメニューを取得する。
     *
     * @return 精算マスタメニュー
     */
    public JPanel getAccountMasterMenu() {
        if (accountMasterMenu == null) {
            accountMasterMenu = this.createSubMenu();

            //消費税登録
            this.addSubMenu(accountMasterMenu, this.getMstTaxButton(), 40);

            //支払方法登録
            this.addSubMenu(accountMasterMenu, this.getMstPaymentMethodButton(), 41);

            //割引種別登録
            this.addSubMenu(accountMasterMenu, this.getMstDiscountButton(), 42);

            //レシート設定
            this.addSubMenu(accountMasterMenu, this.getReceiptSettingButton(), -1);
        }

        return accountMasterMenu;
    }

    /**
     * 消費税登録ボタンを取得する。
     *
     * @return 消費税登録ボタン
     */
    public JButton getMstTaxButton() {
        if (mstTaxButton == null) {
            mstTaxButton = this.createImageButton(
                    "消費税登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_duty_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_duty_entry_on.jpg",
                    null,
                    false);
            /*
             "消費税登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/tax_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/tax_master_on.jpg",
             null,
             false);
             */
            mstTaxButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstTaxButton.getModel().setRollover(false);
                    MstTaxPanel mtp = new MstTaxPanel();
                    changeContents(mtp);
                }
            });
        }

        return mstTaxButton;
    }

    /**
     * 支払方法登録ボタンを取得する。
     *
     * @return 支払方法登録ボタン
     */
    public JButton getMstPaymentMethodButton() {
        if (mstPaymentMethodButton == null) {
            mstPaymentMethodButton = this.createImageButton(
                    "支払方法登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_paying_proccess_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_paying_proccess_entry_on.jpg",
                    null,
                    false);
            /*
             "支払方法登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/payment_method_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/payment_method_master_on.jpg",
             null,
             false);
             */
            mstPaymentMethodButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstPaymentMethodButton.getModel().setRollover(false);
                    MstPaymentMethodPanel mpmp = new MstPaymentMethodPanel();
                    changeContents(mpmp);
                }
            });
        }

        return mstPaymentMethodButton;
    }

    /**
     * 割引種別登録ボタンを取得する。
     *
     * @return 割引種別登録ボタン
     */
    public JButton getMstDiscountButton() {
        if (mstDiscountButton == null) {
            mstDiscountButton = this.createImageButton(
                    "割引種別登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_discount_assort_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_discount_assort_entry_on.jpg",
                    null,
                    false);
            /*
             "割引種別登録",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/discount_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/discount_master_on.jpg",
             null,
             false);

             */
            mstDiscountButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstDiscountButton.getModel().setRollover(false);
                    MstDiscountPanel mdp = new MstDiscountPanel();
                    changeContents(mdp);
                }
            });
        }

        return mstDiscountButton;
    }

    /**
     * レシート設定ボタンを取得する。
     *
     * @return レシート設定ボタン
     */
    public JButton getReceiptSettingButton() {
        if (receiptSettingButton == null) {
            receiptSettingButton = this.createImageButton(
                    "レシート設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_setup_receipt_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_setup_receipt_on.jpg",
                    null,
                    false);
            /*
             "レシート設定",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/receipt_setting_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/receipt_setting_on.jpg",
             null,
             false);
             */
            receiptSettingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    receiptSettingButton.getModel().setRollover(false);
                    ReceiptSettingPanel rsp = new ReceiptSettingPanel();
                    changeContents(rsp);
                }
            });
        }

        return receiptSettingButton;
    }

    /**
     * メインメニューを再描画する。
     */
    private void repaintMainMenu() {
        //メニューを開く
        if (openButton != null) {
            openButton.openSubMenu(MENU_OPEN_SPEED);

            if (openButton.isSubMenuOpen()) {
                openedButton = openButton;
                openButton = null;
            }
        }
        //メニューを閉じる
        if (closeButton != null) {
            closeButton.closeSubMenu(MENU_OPEN_SPEED);

            if (!closeButton.isSubMenuOpen()) {
                closeButton = null;
            }
        }
        //処理が終わっていれば、タイマーを止める
        if (openButton == null && closeButton == null) {
            timer.stop();
        }

        int y = 0;

        //メニューの座標を設定する
        for (int i = 0; i < menuPanel.getComponentCount(); i++) {
            menuPanel.getComponent(i).setBounds(
                    0,
                    y,
                    menuPanel.getComponent(i).getWidth(),
                    menuPanel.getComponent(i).getHeight());
            y += menuPanel.getComponent(i).getHeight() + 3;
        }

        menuPanel.updateUI();
    }

    /**
     * サブメニューを表示する。
     */
    private void showSubMenu(Point loc, Integer width, JPanel selectedSubMenu) {
        subMenu = selectedSubMenu;

        int offsetY = 33;

        // 会社関連メニューの場合
        if (subMenu.equals(companyMasterMenu)) {
            // 最下行のメニューが表示されないため表示位置を上に移動
            int rowCount = (selectedSubMenu.getComponentCount() - 10);
            if (rowCount > 0) {
                offsetY += (25 * rowCount);
            }
        }

        // 顧客情報メニューの場合
        if (subMenu.equals(customerMasterMenu)) {
            // 最下行のメニューが表示されないため表示位置を上に移動
            int rowCount = (selectedSubMenu.getComponentCount() - 7);
            if (rowCount > 0) {
                offsetY += (25 * rowCount);
            }
        }

        if (isTopPage() && isWebPage()) {

            if (subMenuMap.containsKey(selectedSubMenu)) {
                subMenuPanel = (Panel) subMenuMap.get(selectedSubMenu);
            } else {

                subMenuPanel = new Panel();
                subMenuPanel.setSize(selectedSubMenu.getSize());
                subMenuPanel.setLayout(selectedSubMenu.getLayout());

                for (int i = 0; i < selectedSubMenu.getComponentCount(); i++) {
                    JButton a = (JButton) selectedSubMenu.getComponent(i);

                    JButton button = new JButton();

                    button.setBounds(a.getBounds());
                    button.setToolTipText(a.getToolTipText());
                    button.setIcon(a.getIcon());
                    button.setRolloverIcon(a.getRolloverIcon());
                    button.setPressedIcon(a.getPressedIcon());
                    button.setDisabledIcon(a.getDisabledIcon());
                    button.setBorder(a.getBorder());
                    button.setSize(a.getSize());
                    for (int j = 0; j < a.getMouseListeners().length; j++) {
                        button.addMouseListener(a.getMouseListeners()[j]);
                    }
                    for (int j = 0; j < a.getActionListeners().length; j++) {
                        button.addActionListener(a.getActionListeners()[j]);
                    }

                    subMenuPanel.setBackground(new Color(254, 153, 0));
                    subMenuPanel.add(button);

                }

                subMenuMap.put(selectedSubMenu, subMenuPanel);

            }


            this.add(subMenuPanel, 0);
            Point thisLoc = this.getLocationOnScreen();
            subMenuPanel.setBounds(((Double) (loc.getX() - thisLoc.getX())).intValue() + width + 10,
                    ((Double) (loc.getY() - thisLoc.getY())).intValue() - offsetY,
                    selectedSubMenu.getWidth(),
                    selectedSubMenu.getHeight());

            subMenuPanel.repaint();

        } else {

            this.add(subMenu, 0);
            Point thisLoc = this.getLocationOnScreen();
            subMenu.setBounds(((Double) (loc.getX() - thisLoc.getX())).intValue() + width + 10,
                    ((Double) (loc.getY() - thisLoc.getY())).intValue() - offsetY,
                    subMenu.getWidth(),
                    subMenu.getHeight());

            subMenu.updateUI();
        }

        this.repaint();

    }

    /**
     * サブメニューを消す。
     */
    private void hideSubMenu() {
        if (subMenuPanel != null) {
            this.remove(subMenuPanel);
        }

        if (subMenu != null) {
            this.remove(subMenu);
            subMenu.updateUI();
        }

        this.repaint();
    }

    /**
     * トップページ用パネルを取得する
     */
    private TopPagePanel getTopPage() {
        if (topPage == null || isWebPage()) {
            topPage = new TopPagePanel();
        }

        return topPage;
    }

    private boolean isTopPage() {

        boolean result = false;

        if (contentsPanel.getComponentCount() > 0) {
            result = contentsPanel.getComponent(0).equals(topPage);
        }

        return result;
    }

    private boolean isWebPage() {

        return topPage != null && topPage.getName() != null;
    }

    /**
     * 時計関連の初期化を行う。
     */
    private void initClock() {
        //時計用の画像を読み込む
        this.loadClockImage();

        //タイマーをセット
        clockTimer = new javax.swing.Timer(CLOCK_TIMER_INTERVAL,
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        refreshClock();
                    }
                });


        //タイマースタート
        clockTimer.start();
    }

    /**
     * 時計用の画像を取得する。
     */
    private void loadClockImage() {
        //数字の画像を読み込む
        for (Integer i = 0; i < 10; i++) {
            numberImageS[i] = new ImageIcon(getClass().getResource(
                    "/images/" + SystemInfo.getSkinPackage() + "/clock/number_s/" + i.toString() + ".jpg"));
            numberImageL[i] = new ImageIcon(getClass().getResource(
                    "/images/" + SystemInfo.getSkinPackage() + "/clock/number_l/" + i.toString() + ".jpg"));
        }

        slash0.setImage(new ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/clock/number_s/slash.jpg")));

        slash1.setImage(new ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/clock/number_s/slash.jpg")));

        colon.setImage(new ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/clock/number_l/colon.jpg")));
    }

    /**
     * 時計を更新する。
     */
    private void refreshClock() {
        GregorianCalendar gc = new GregorianCalendar();
        //年
        year1000.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 1000, false));
        year100.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 100, false));
        year10.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 10, false));
        year1.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 1, false));
        //月
        month10.setImage(this.getNumberImage(gc.get(Calendar.MONTH) + 1, 10, false));
        month1.setImage(this.getNumberImage(gc.get(Calendar.MONTH) + 1, 1, false));
        //日
        day10.setImage(this.getNumberImage(gc.get(Calendar.DAY_OF_MONTH), 10, false));
        day1.setImage(this.getNumberImage(gc.get(Calendar.DAY_OF_MONTH), 1, false));
        //時
        hour10.setImage(this.getNumberImage(gc.get(Calendar.HOUR_OF_DAY), 10, true));
        hour1.setImage(this.getNumberImage(gc.get(Calendar.HOUR_OF_DAY), 1, true));
        //コロン
        colon.setVisible(500 <= gc.get(Calendar.MILLISECOND));
        //分
        minute10.setImage(this.getNumberImage(gc.get(Calendar.MINUTE), 10, true));
        minute1.setImage(this.getNumberImage(gc.get(Calendar.MINUTE), 1, true));

        clockPanel.updateUI();
    }

    /**
     * 数字の画像を取得する。
     */
    private ImageIcon getNumberImage(Integer number, Integer digit, boolean isLarge) {
        Integer temp = number % (digit * 10);
        temp /= digit;

        if (isLarge) {
            return numberImageL[temp];
        } else {
            return numberImageS[temp];
        }
    }

    /**
     * マスタ登録権限をチェックする。
     *
     * @return true -
     */
    private boolean checkAuthority(int index) {
        if (SystemInfo.checkAuthority(index)) {
            return true;
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(3),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * バーコードが読み込まれたときの処理。
     */
    public boolean readBarcode(BarcodeEvent be) {
        return true;
    }

    /**
     * ログインしてからの経過時間チェック
     */
    private void checkLoginTime() {

        long time = Calendar.getInstance().getTimeInMillis() - calLoginTime.getTimeInMillis();
        time = time / 1000 / 3600;

        if (time >= 24) {

            isCheckLoginTime = false;

            String msg = "";
            msg += "SOSIA POS を起動してから 24時間 が経過しました。\n";
            msg += "システムの最新情報を反映させるために再起動をお願いします。";

            MessageDialog.showMessageDialog(
                    this, msg, this.getTitle(), JOptionPane.INFORMATION_MESSAGE);

        }
    }

    /**
     * 指定の時間毎にモバイル予約が入っているか監視。
     */
    private void checkMobileData() {

        setMobileData();
        checkTimer = new javax.swing.Timer(this.getMobileCheckTime(), new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMobileData();
                if (isCheckLoginTime) {
                    checkLoginTime();
                }
            }
        });

        checkTimer.start();
    }

    /**
     * モバイル予約が入っている場合、ボタンを点滅させる。
     */
    private void setMobileData() {

        if (isMobileData()) {
            mobileButton.setEnabled(true);
//複数台で運用時にボタンが消えない不具合対応	でタイマー停止をやめる。
//                  if(checkTimer != null)     checkTimer.stop();
            // Start 20141117 do.hong.quang update Request#32307
//            mobileButton.setIcon(SystemInfo.getImageIcon("/menu/customer/reserve_off.jpg"));
            mobileButton.setIcon(SystemInfo.getImageIcon("/menu/main/btn_reserve.jpg"));
            // End 20141117 do.hong.quang update Request#32307
            //mobileButton.setIcon(SystemInfo.getImageIcon("/menu/customer/mobile_reservation.gif"));

        } else {
            mobileButton.setEnabled(false);
            // Start 20141117 do.hong.quang update Request#32307
//            mobileButton.setIcon(SystemInfo.getImageIcon("/menu/customer/reserve_none.jpg"));
            mobileButton.setIcon(SystemInfo.getImageIcon("/menu/main/btn_reserve_noreserve.jpg"));
            // End 20141117 do.hong.quang update Request#32307

        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        if (dbUrl.startsWith("jdbc:postgresql://192.168.10.18:5432")) {

            // CTIテスト用
            mobile_check_time = 2000;
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = con.executeQuery("select * from action_cti");
                String tel = "";
                if (rs.next()) {
                    tel = rs.getString("tel");
                }
                con.executeUpdate("truncate table action_cti");

                if (tel.length() > 0) {
                    com.geobeck.sosia.pos.cti.CTICustomerDialog ccd = new com.geobeck.sosia.pos.cti.CTICustomerDialog(tel, this);
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    private boolean isMobileData() {

        SystemInfo.getBaseConnection();
        ConnectionWrapper con = SystemInfo.getConnection();
        boolean isMobileReservation = false;

        try {

            ResultSetWrapper rs = con.executeQuery(this.getMobileDataSQL());

            if (rs.next()) {
                isMobileReservation = (rs.getInt("mobileCount") != 0 ? true : false);
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return isMobileReservation;
    }

    private String getMobileDataSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     count(mobile_flag) as mobileCount");
        sql.append(" from");
        sql.append("     data_reservation");
        sql.append(" where");
        sql.append("         shop_id = " + SystemInfo.getCurrentShop().getShopID());
        sql.append("     and mobile_flag in (" + ts.getMobileFlag() + "," + ts.getMobileCancelFlag() + ")");
        sql.append("     and (mobile_flag = " + ts.getMobileCancelFlag() + " or (mobile_flag <> " + ts.getMobileCancelFlag() + " and delete_date is null))");

        return sql.toString();
    }

    private DataReservation setMobile() {
        ConnectionWrapper con = SystemInfo.getConnection();
//                  DataReservation	dr	=	new     DataReservation();
        DataReservation dr = null;
        try {
            ResultSetWrapper rs = con.executeQuery(this.getMobileDataSQL2());

            if (rs.next()) {
                dr = new DataReservation();

                MstShop ms = new MstShop();
                ms.setShopID(rs.getInt("shop_id"));
                dr.setShop(ms);

                dr.setReservationNo(rs.getInt("reservation_no"));
                dr.setReserveDate(rs.getDate("reservation_datetime"));
                dr.setDesignated(rs.getBoolean("designated_flag"));

                MstStaff mst = new MstStaff();
                mst.setStaffID(rs.getInt("charge_staff_id"));
                mst.setStaffNo(rs.getString("charge_staff_no"));
                mst.setStaffName(0, rs.getString("charge_staff_name1"));
                mst.setStaffName(1, rs.getString("charge_staff_name2"));
                dr.setStaff(mst);

                MstCustomer mc = new MstCustomer();
                mc.setCustomerID(rs.getInt("customer_id"));
                mc.setCustomerNo(rs.getString("customer_no"));
                mc.setCustomerName(0, rs.getString("customer_name1"));
                mc.setCustomerName(1, rs.getString("customer_name2"));
                dr.setCustomer(mc);

                dr.setStatus(rs.getInt("status"));
                dr.setMobileFlag(rs.getInt("mobile_flag"));
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return dr;
    }

    private String getMobileDataSQL2() {

        return "select dr.shop_id,\n"
                + "dr.reservation_no,\n"
                + "dr.designated_flag,\n"
                + "dr.staff_id as charge_staff_id,\n"
                + "cms.staff_no as charge_staff_no,\n"
                + "cms.staff_name1 as charge_staff_name1,\n"
                + "cms.staff_name2 as charge_staff_name2,\n"
                + "drd.reservation_detail_no,\n"
                + "drd.reservation_datetime,\n"
                + "case when drd.staff_id = 0 then null else drd.staff_id end as staff_id,\n"
                + "ms.staff_no,\n"
                + "ms.staff_name1 || ms.staff_name2 as staff_name,\n"
                + "ms.staff_name1,\n"
                + "ms.staff_name2,\n"
                + "case when drd.bed_id = 0 then null else drd.bed_id end as bed_id,\n"
                + "mb.bed_name,\n"
                + "dr.customer_id,\n"
                + "mc.customer_no,\n"
                + "mc.customer_name1,\n"
                + "mc.customer_name2,\n"
                + //				"drd.technic_id,\n" +
                //				"mt.technic_class_id,\n" +
                //				"mtc.technic_class_name,\n" +
                //				"mt.technic_no,\n" +
                //				"mt.technic_name,\n" +
                //				"mt.operation_time,\n" +
                "dr.status ,\n"
                + "dr.mobile_flag \n"
                + "from data_reservation dr\n"
                + "left outer join mst_staff cms\n"
                + "on cms.staff_id = dr.staff_id\n"
                + "inner join data_reservation_detail drd\n"
                + "on drd.shop_id = dr.shop_id \n"
                + "and drd.reservation_no = dr.reservation_no\n"
                + //"and drd.delete_date is null\n" +
                "left outer join mst_staff ms\n"
                + "on ms.staff_id = drd.staff_id\n"
                + "left outer join mst_customer mc\n"
                + "on mc.customer_id = dr.customer_id\n"
                + "left outer join mst_bed mb\n"
                + "on mb.bed_id = drd.bed_id\n"
                + //				"left outer join mst_technic mt\n" +
                //				"on mt.technic_id = drd.technic_id\n" +
                //				"left outer join mst_technic_class mtc\n" +
                //				"on mtc.technic_class_id = mt.technic_class_id\n" +
                "where (dr.mobile_flag = " + ts.getMobileCancelFlag() + " or (dr.mobile_flag <> " + ts.getMobileCancelFlag() + " and dr.delete_date is null))\n"
                + "and dr.shop_id = " + SystemInfo.getCurrentShop().getShopID() + "\n"
                + "and dr.mobile_flag in (" + ts.getMobileFlag() + "," + ts.getMobileCancelFlag() + ")\n"
                + "order by reservation_datetime,reservation_no,reservation_detail_no\n";
        //                               "order by drd.reservation_no, case when drd.reservation_detail_no=1 then drd.reservation_datetime end";
    }

    /*
     *CTIが読み込まれたときの処理。
     */
    public boolean readCTI(CTIEvent ce) {
        System.out.println("getCTI :" + ce.getCTI());
        String ctiCustomerData = ce.takeCustomerData();
        System.out.println("ctiCustomerData :" + ctiCustomerData);
        CTICustomerDialog ccd = new CTICustomerDialog(ctiCustomerData, this);

        return true;
    }

    /**
     * 置場サブメニューのボタンを返す
     */
    public JButton getMstPlaceButton() {
        if (mstPlaceButton == null) {
            mstPlaceButton = this.createImageButton(
                    "置場マスタ",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/marcherdise_locatorlist_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/marcherdise_locatorlist_entry_on.jpg",
                    null,
                    false);

            mstPlaceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstPlaceButton.getModel().setRollover(false);
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "置場", "mst_place", "place_id",
                            "place_name", 40,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("基本設定 >> 商品マスタ");
                    changeContents(smp);
                }
            });
        }

        return mstPlaceButton;
    }

    /**
     * ポイントカードボタンを取得する。
     *
     * @return ポイントカードボタン
     */
    public JButton getPointcardSubButton() {
        if (pointcardButton == null) {
            pointcardButton = this.createImageButton(
                    "ポイントカード",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/merchandise_card_setup_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/merchandise_card_setup_on.jpg",
                    null,
                    true);

            pointcardButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(pointcardButton.getLocationOnScreen(),
                            pointcardButton.getWidth(),
                            getPointcardMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return pointcardButton;
    }

    /**
     * ポイントカード用メニューを取得する。
     *
     * @return ポイントカード用メニュー
     */
    public JPanel getPointcardMenu() {
        // ポイントカード用
        if (pointcardMenu == null) {
            pointcardMenu = this.createSubMenu();

            this.addSubMenu(pointcardMenu, this.getPointardTemplateButton(), -1);
            this.addSubMenu(pointcardMenu, this.getPointardCalculationButton(), -1);
            this.addSubMenu(pointcardMenu, this.getPointardInitSettingButton(), -1);
//                this.addSubMenu(pointcardMenu, this.getPointardTestButton(), -1 );

        }
        return pointcardMenu;
    }

    /**
     * ポイントカードテンプレート登録ボタンを取得する。
     *
     * @return ポイントカードテンプレート登録ボタン
     */
    public JButton getPointardTemplateButton() {
        if (pointcardTemplateButton == null) {
            pointcardTemplateButton = this.createImageButton(
                    "テンプレート登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/base_promotion_temp_setup_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/base_promotion_temp_setup_on.jpg",
                    null,
                    false);

            pointcardTemplateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    pointcardTemplateButton.getModel().setRollover(false);
                    CardLayoutPanel pnl = new CardLayoutPanel();
                    changeContents(pnl);
                }
            });
        }

        return pointcardTemplateButton;
    }

    /**
     * ポイントカード計算式登録ボタンを取得する。
     *
     * @return ポイントカード計算式登録ボタン
     */
    public JButton getPointardCalculationButton() {
        if (pointcardCalculationButton == null) {
            pointcardCalculationButton = this.createImageButton(
                    "ポイント計算式登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/base_promotion_card_setup_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/base_promotion_card_setup_on.jpg",
                    null,
                    false);

            pointcardCalculationButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    pointcardCalculationButton.getModel().setRollover(false);
                    PointCalculatePanel pnl = new PointCalculatePanel();
                    changeContents(pnl);
                }
            });
        }

        return pointcardCalculationButton;
    }

    /**
     * ポイントカード初期設定ボタンを取得する。
     *
     * @return ポイントカード初期設定ボタン
     */
    public JButton getPointardInitSettingButton() {
        if (pointcardInitSettingButton == null) {
            pointcardInitSettingButton = this.createImageButton(
                    "カード初期化",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/base_promotion_default_setting_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/base_promotion_default_setting_on.jpg",
                    null,
                    false);

            pointcardInitSettingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    pointcardInitSettingButton.getModel().setRollover(false);
                    PointCardCleaningPanel pnl = new PointCardCleaningPanel();
                    changeContents(pnl);
                }
            });
        }

        return pointcardInitSettingButton;
    }

    /**
     * カードテストボタンを取得する。
     *
     * @return カード印字設定ボタン
     */
    public JButton getPointardTestButton() {
        if (pointcardTestButton == null) {
            pointcardTestButton = this.createImageButton(
                    "カードテスト",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/cardprint_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/pointcard/cardprint_on.jpg",
                    null,
                    false);

            pointcardTestButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    pointcardTestButton.getModel().setRollover(false);
                    TestCardReader pnl = new TestCardReader();
                    changeContents(pnl);
                }
            });
        }

        return pointcardTestButton;
    }

    /**
     * 商品管理ボタンを追加する
     *
     * @return 商品管理ボタン
     */
    private MainMenuButton getProductButton() {

        if (productButton == null) {

            productButton = new MainMenuButton(this.createImageButton(
                    "商品管理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/marcherdise_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/marcherdise_on.jpg",
                    null,
                    true),
                    SystemInfo.getMenuColor());

            this.addMainMenuButtonEvent(productButton);
            productButton.addSubMenuButton(this.getRegisterOrderSlipButton());
            productButton.addSubMenuButton(this.getRegisterCheckInVoucherButton());
            productButton.addSubMenuButton(this.getRegisterCheckOutVoucherButton());

            if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_charm")) {
                productButton.addSubMenuButton(this.getInventryTomPanelButton());
            } else {
                productButton.addSubMenuButton(this.getInventryPanelButton());
            }

            productButton.addSubMenuButton(this.getProductReportButton());
            if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_charm")) {
                productButton.addSubMenuButton(this.getProductReportTomButton());
            }

            if (SystemInfo.getSetteing().isTransferProduct()) {
                // 店舗間移動
                productButton.addSubMenuButton(this.getTransferProductPanelButton());
                // 在庫確認
                productButton.addSubMenuButton(this.getStockListButton());
            }
            // IVS SANG START INSERT 20131209 [gbソース]商品受渡確認書のマージ
            // 顧客別在庫管理
            // IVS SANG START INSERT 20131218 [gbソース]商品管理⇒顧客別在庫管理、商品宅送管理の表示制御
            //IVS VTBPHUONG START CHANGE 20140502 Request #22768
            //if(SystemInfo.getCurrentShop().getCourseFlag() != null && SystemInfo.getCurrentShop().getCourseFlag() == 1) {
            // IVS SANG END INSERT 20131218 [gbソース]商品管理⇒顧客別在庫管理、商品宅送管理の表示制御
            //IVS_LVTu start edit 2017/09/18 #25966 [gb]基本設定＞会社関連＞店舗情報登録に商品受渡機能使用フラグの追加
            if (SystemInfo.getCurrentShop().getDeliveryFlag()!= null
                      && SystemInfo.getCurrentShop().getDeliveryFlag().intValue() == 1){
                productButton.addSubMenuButton(this.getProductCustomerButton());
            }
            //IVS_LVTu end edit 2017/09/18 #25966 [gb]基本設定＞会社関連＞店舗情報登録に商品受渡機能使用フラグの追加
            //}
            //IVS VTBPHUONG END CHANGE 20140502 Request #22768
            // start add 20130314 nakhoa 商品宅配管理
            if (SystemInfo.getCurrentShop().getShopID() == 0) {
                // IVS SANG START INSERT 20131218 [gbソース]商品管理⇒顧客別在庫管理、商品宅送管理の表示制御
                if (SystemInfo.getNSystem() == 2) {
                    // IVS SANG END INSERT 20131218 [gbソース]商品管理⇒顧客別在庫管理、商品宅送管理の表示制御
                    productButton.addSubMenuButton(this.getProductDeliveryManagementButton());
                }
            }
            // endadd 201303014 nakhoa 商品宅配管理
            // IVS SANG END INSERT 20131209 [gbソース]商品受渡確認書のマージ
        }

        return productButton;
    }

    // IVS SANG START INSERT 20131209 [gbソース]商品受渡確認書のマージ
    private JButton getProductCustomerButton() {
        if (productCustomerButton == null) {

            productCustomerButton =
                    this.createImageButton(
                    "顧客別在庫管理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custmer_stock_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custmer_stock_on.jpg",
                    null,
                    true);

            productCustomerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    productCustomerButton.setCursor(null);
                    try {

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        InventoryManagementCustomerListPanel rrp = new InventoryManagementCustomerListPanel();
                        changeContents(rrp);
                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }

                }
            });
        }

        return productCustomerButton;
    }
    // IVS SANG START INSERT 20131209 [gbソース]商品受渡確認書のマージ

    /**
     * メニュー 発注書入力
     */
    private JButton getRegisterOrderSlipButton() {
        if (registerOrderSlipButton == null) {
            registerOrderSlipButton = this.createImageButton(
                    "発注書作成", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_order_drawup_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_order_drawup_on.jpg", null, true);

            registerOrderSlipButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    RegisterOrderSlipPanel pnl = new RegisterOrderSlipPanel();
                    changeContents(pnl);
                }
            });
        }

        return registerOrderSlipButton;
    }

    /**
     * メニュー 入庫入力
     */
    public JButton getRegisterCheckInVoucherButton() {
        if (registerCheckInVoucherButton == null) {
            registerCheckInVoucherButton = this.createImageButton(
                    "入庫伝票作成", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_in_check_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_in_check_on.jpg", null, true);

            registerCheckInVoucherButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    RegisterCheckInVoucherPanel pnl = new RegisterCheckInVoucherPanel();
                    changeContents(pnl);
                }
            });
        }

        return registerCheckInVoucherButton;
    }

    /**
     * メニュー 出庫入力
     */
    public JButton getRegisterCheckOutVoucherButton() {
        if (registerCheckOutVoucherButton == null) {
            registerCheckOutVoucherButton = this.createImageButton(
                    "出庫伝票作成", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_out_check_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_out_check_on.jpg", null, true);

            registerCheckOutVoucherButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    RegisterCheckOutVoucherPanel pnl = new RegisterCheckOutVoucherPanel();
                    changeContents(pnl);
                }
            });
        }

        return registerCheckOutVoucherButton;
    }

    /**
     * メニュー 棚卸
     */
    public JButton getInventryPanelButton() {
        if (inventryPanelButton == null) {
            inventryPanelButton = this.createImageButton(
                    "棚卸", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_on.jpg", null, true);

            inventryPanelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    InventryPanel pnl = new InventryPanel();
                    changeContents(pnl);
                }
            });
        }

        return inventryPanelButton;
    }

    /**
     * メニュー 棚卸(TOM)
     */
    public JButton getInventryTomPanelButton() {
        if (inventryPanelTomButton == null) {
            inventryPanelTomButton = this.createImageButton(
                    "棚卸", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_on.jpg", null, true);

            inventryPanelTomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    InventryPanel_TOM pnl = new InventryPanel_TOM();
                    changeContents(pnl);
                }
            });
        }

        return inventryPanelTomButton;
    }

    /**
     * メニュー 店舗間移動
     */
    public JButton getTransferProductPanelButton() {
        if (transferProductButton == null) {
            transferProductButton = this.createImageButton(
                    "店舗間移動", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_between_shop_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_between_shop_on.jpg", null, true);

            transferProductButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    MoveStockPanel pnl = new MoveStockPanel();
                    changeContents(pnl);
                }
            });
        }

        return transferProductButton;
    }

    /**
     * 在庫一覧確認
     */
    public JButton getStockListButton() {
        if (stocklistButton == null) {
            stocklistButton = this.createImageButton(
                    "在庫一覧確認", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/stock-check_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/stock-check_on.jpg", null, true);

            stocklistButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    StockList pnl = new StockList();
                    changeContents(pnl);
                }
            });
        }

        return stocklistButton;
    }

    /**
     * 商品管理帳票ボタンを取得する。
     *
     * @return 商品管理帳票ボタン
     */
    public JButton getProductReportButton() {
        if (productReportButton == null) {
            productReportButton = this.createImageButton(
                    "帳票",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_ledger_sheet_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_ledger_sheet_on.jpg",
                    null,
                    true);

            productReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            	 public void mouseEntered(java.awt.event.MouseEvent evt) {
                	 Point x = new Point();
                     x.setLocation(productReportButton.getLocationOnScreen().getX(), productReportButton.getLocationOnScreen().getY() - 100);
                    showSubMenu(x,
                            productReportButton.getWidth(),
                            getProductReportSubMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return productReportButton;
    }

    /**
     * 商品管理帳票ボタンを取得する。
     *
     * @return 商品管理帳票ボタン
     */
    public JButton getProductReportTomButton() {
        if (productReportTomButton == null) {
            productReportTomButton = this.createImageButton(
                    "帳票(TOM)",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_leger_sheet_custom_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_leger_sheet_custom_on.jpg",
                    null,
                    true);

            productReportTomButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(productReportTomButton.getLocationOnScreen(),
                            productReportTomButton.getWidth(),
                            getProductReportTomSubMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return productReportTomButton;
    }

    /**
     * 商品管理帳票ボタンを取得する。
     *
     * @return 商品管理帳票ボタン
     */
    public JButton getProductStaffSalesButton() {
        if (productStaffSalesButton == null) {
            productStaffSalesButton = this.createImageButton(
                    "スタッフ販売",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_staff_sales_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_staff_sales_on.jpg",
                    null,
                    true);

            productStaffSalesButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(productStaffSalesButton.getLocationOnScreen(),
                            productStaffSalesButton.getWidth(),
                            getProductStaffSalesMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return productStaffSalesButton;
    }

    /**
     *
     */
    public JPanel getProductStaffSalesMenu() {
        // 商品管理スタッフ販売サブメニュー
        if (productStaffSalesMenu == null) {
            productStaffSalesMenu = this.createSubMenu();

            // 販売入力
            staffSalesInputButton = this.getImageButton(staffSalesInputButton, "/menu/product/merchedise_staff_sales_input", "スタッフ販売入力",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    staffSalesInputButton.getModel().setRollover(false);
                            changeContents(new RegisterStaffSalesPanel());
                        }
                    });
            this.addSubMenu(productStaffSalesMenu, staffSalesInputButton, -1);

            // 販売履歴
            staffSalesHistoryButton = this.getImageButton(staffSalesHistoryButton, "/menu/product/merchedise_staff_sales_career", "スタッフ販売履歴",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    staffSalesHistoryButton.getModel().setRollover(false);
                            changeContents(new StaffSalesHistoryPanel());
                        }
                    });
            this.addSubMenu(productStaffSalesMenu, staffSalesHistoryButton, -1);
        }

        return productStaffSalesMenu;
    }

    /**
     * 商品管理帳票サブメニューを取得する。
     *
     * @return 商品管理帳票サブメニュー
     */
    public JPanel getProductReportSubMenu() {
        //商品管理帳票サブメニュー
        if (productReportSubMenu == null) {
            productReportSubMenu = this.createSubMenu();

            // 入出庫一覧表
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/marcherdise_list_input-output", "入出庫一覧表",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new StoreShipPanel());
                        }
                    }), -1);
            // 置場別在庫票
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/marcherdise_locatorlist", "置場別在庫票",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new PlacePanel());
                        }
                    }), -1);
            // 返品一覧表
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/marcherdise_return_list", "返品一覧表",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new BackProductPanel());
                        }
                    }), -1);
            // 棚卸表
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/merchedise_stock_take_list", "棚卸表",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new PrintInventoryPanel());
                        }
                    }), -1);
            // 合計棚卸表
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/merchedise_stock_take_list_total", "合計棚卸表",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new TotalInventryPanel());
                        }
                    }), -1);
//                // スタッフ販売一覧表
//                this.addSubMenu(productReportSubMenu,
//                            this.getImageButton(materialCostTomButton, "/menu/product/merchedise_staff_sales_check", "スタッフ販売一覧表",
//                                        new ActionListener() {
//                                            public void actionPerformed(ActionEvent ae) {
////                                                    materialCostTomButton.getModel().setRollover(false);
//                                                    changeContents(new StaffSalesListPanel());
//                                                }
//                                            }
//                                        ), -1 );

            // start add 20141128 g_wang 商品在庫数一覧
            // 商品在庫数一覧
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/item_stock_list", "商品在庫数一覧",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new ItemStockListPanel());
                        }
                    }), -1);
            // end add 20141128 g_wang 商品在庫数一覧

            //IVS_TMTrong start add 20150707 New request #38966
            //商品受渡一覧
            //IVS_LVTu start edit 2017/09/18 #25966 [gb]基本設定＞会社関連＞店舗情報登録に商品受渡機能使用フラグの追加
            boolean flagDelivery = false;
            if(SystemInfo.getCurrentShop().getShopID()==0) {
                for(MstShop shop:SystemInfo.getGroup().getShops()) {
                    if(shop.getDeliveryFlag()==1) {
                        flagDelivery = true;
                    }
                }
             }
            if(SystemInfo.getCurrentShop().getShopID()!=0 && SystemInfo.getCurrentShop().getDeliveryFlag()==1) {
                flagDelivery = true;
            }
            if (flagDelivery) {
             this.addSubMenu(productReportSubMenu,
                        this.getImageButton(materialCostTomButton, "/menu/product/item_delivery_history", "商品受渡一覧",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
    //                                                    materialCostTomButton.getModel().setRollover(false);
                                changeContents(new ItemDeliveryHistoryPanel());
                            }
                        }), -1);
            }
            //IVS_LVTu end edit 2017/09/18 #25966 [gb]基本設定＞会社関連＞店舗情報登録に商品受渡機能使用フラグの追加
            //IVS_TMTrong end add 20150707 New request #38966
        }


        return productReportSubMenu;
    }

    /**
     * 商品管理帳票サブメニューを取得する。
     *
     * @return 商品管理帳票サブメニュー
     */
    public JPanel getProductReportTomSubMenu() {
        //商品管理帳票サブメニュー
        if (productReportTomSubMenu == null) {
            productReportTomSubMenu = this.createSubMenu();

            // 材料比率(TOM)
            this.addSubMenu(productReportTomSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/mercherdise_ingredients_rate", "材料比率(TOM)",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new MaterialRatePanel_TOM());
                        }
                    }), -1);

            // 合計棚卸(TOM)
            this.addSubMenu(productReportTomSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/merchedise_stock_take_list_total", "合計棚卸(TOM)",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new TotalInventryPanel_TOM());
                        }
                    }), -1);

            // 平均材料比率(TOM)
            this.addSubMenu(productReportTomSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/mercherdise_ingredients_rate_years", "平均材料比率(TOM)",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new MaterialRatePanelAvg_TOM());
                        }
                    }), -1);
        }
        return productReportTomSubMenu;
    }

    /**
     * カスタム帳票ボタンを取得する。
     *
     * @return カスタム帳票ボタン
     */
    public JButton getCustomReportSubButton() {
        if (customReportButton == null) {
            customReportButton = this.createImageButton(
                    "カスタム帳票",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/merchandise_leger_sheet_custom_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/merchandise_leger_sheet_custom_on.jpg",
                    null,
                    true);

            customReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(customReportButton.getLocationOnScreen(),
                            customReportButton.getWidth(),
                            getcCustomReportMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return customReportButton;
    }

    /**
     * カスタム帳票メニューを取得する。
     *
     * @return カスタム帳票メニュー
     */
    public JPanel getcCustomReportMenu() {
        //テスト用
        if (customReportMenu == null) {
            customReportMenu = this.createSubMenu();


            // TOM帳票
            // 日計出力
            this.addSubMenu(customReportMenu, this.getImageButton(dailyReportButton, "/menu/report/daily_report", "日計出力",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    dailyReportButton.getModel().setRollover(false);
                            changeContents(new DailySalesReportPanelTom());
                        }
                    }), -1);

            // 月間画面
            this.addSubMenu(customReportMenu, this.getImageButton(monthlyReportButton, "/menu/report/monthly_report", "月間画面",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    monthlyReportButton.getModel().setRollover(false);
                            changeContents(new TechnicSalesReportPanelTom());
                        }
                    }), -1);

            // ランキング
//            this.addSubMenu(customReportMenu, this.getImageButton(rankingReportButton, "/menu/report/ranking_report", "ランキング",
//                    new ActionListener() {
//                        public void actionPerformed(ActionEvent ae) {
//                                                   rankingReportButton.getModel().setRollover(false);
//                            changeContents(new RankingReportPanelTom());
//                        }
//                    }), -1);

            // 再来分析
            this.addSubMenu(customReportMenu, this.getImageButton(repeatReportButton, "/menu/report/repeater_report", "再来分析",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    repeatReportButton.getModel().setRollover(false);
                            changeContents(new RepeaterReportPanelTom());
                        }
                    }), -1);

            // 売上分析
            this.addSubMenu(customReportMenu, this.getImageButton(analysisReportButton, "/menu/report/sales_report", "売上分析",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    analysisReportButton.getModel().setRollover(false);
                            changeContents(new SalesAnalyzePanel());
                        }
                    }), -1);

        }
        return customReportMenu;
    }

    /**
     * カスタム設定ボタンを取得する。
     *
     * @return カスタム設定ボタン
     */
    public JButton getCustomSettingButton() {
        if (customSetteingButton == null) {
            customSetteingButton = this.createImageButton(
                    "カスタム設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custom_setting_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custom_setting_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custom_setting_on.jpg",
                    true);

            customSetteingButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(customSetteingButton.getLocationOnScreen(),
                            customSetteingButton.getWidth(),
                            getcCustomSettingMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return customSetteingButton;
    }

    // vtbphuong start add 20140708  役務帳票
    /**
     * カスタム設定ボタンを取得する。
     *
     * @return カスタム設定ボタン
     */
    public JButton getServiceReportButton() {
        if (serviceReportButton == null) {
            serviceReportButton = this.createImageButton(
                    "役務帳票",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_report_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_report_on.jpg",
                    true);
            serviceReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(serviceReportButton.getLocationOnScreen(),
                            serviceReportButton.getWidth(),
                            getServiceReportMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return serviceReportButton;
    }

    public JPanel getServiceReportMenu() {
        if (serviceReportMenu == null) {
            serviceReportMenu = this.createSubMenu();
            // 201803 GB edit start #43652 [GB内対応][gb]役務帳票メニューが表示されない
            // 権限関係なく表示する
            //    this.addSubMenu(serviceReportMenu, this.getServiceListButton(), 0);
            //    this.addSubMenu(serviceReportMenu, this.getCancelCourseListButton(), 0);
            //    this.addSubMenu(serviceReportMenu, this.getExpirationListButton(), 0);
            this.addSubMenu(serviceReportMenu, this.getServiceListButton(), -1);
            this.addSubMenu(serviceReportMenu, this.getCancelCourseListButton(),-1);
            this.addSubMenu(serviceReportMenu, this.getExpirationListButton(), -1);
            // 201803 GB edit end #43652 [GB内対応][gb]役務帳票メニューが表示されない
             //IVS_LVTu start add 2015/11/06 New request #44110
            if (SystemInfo.getDatabase().startsWith("pos_hair_natura") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
                // 201803 GB edit start #43652 [GB内対応][gb]役務帳票メニューが表示されない
                //    this.addSubMenu(serviceReportMenu, this.getContractReportButton(), 0);
                //    this.addSubMenu(serviceReportMenu, this.getProposalReportButton(), 0);
                this.addSubMenu(serviceReportMenu, this.getContractReportButton(), -1);
                this.addSubMenu(serviceReportMenu, this.getProposalReportButton(), -1);
                // 201803 GB edit end #43652 [GB内対応][gb]役務帳票メニューが表示されない
            }
             //IVS_LVTu end add 2015/11/06 New request #44110
            if (SystemInfo.getDatabase().startsWith("pos_hair_garden")) {
                this.addSubMenu(serviceReportMenu, this.getContractReportButton(), -1);
            }
        }
        return serviceReportMenu;

    }
    // 役務残一覧

    private JButton getServiceListButton() {
        if (serviceListButton == null) {
            serviceListButton = this.createImageButton(
                    "役務残一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_list_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_list_on.jpg",
                    null,
                    false);
            serviceListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ServiceRemainingReportPanel rrap = new ServiceRemainingReportPanel();
                    changeContents(rrap);
                }
            });
        }

        return serviceListButton;
    }
    // 有効期限一覧
     private JButton getExpirationListButton() {
        if (expirationListButton == null) {
            expirationListButton = this.createImageButton(
                    "有効期限一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_expiration_list_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_expiration_list_on.jpg",

                    null,
                    false);
            expirationListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ExpirationDateListPanel edlp = new ExpirationDateListPanel();
                    changeContents(edlp);
                }
            });
        }

        return expirationListButton;
    }
    //解約一覧

    private JButton getCancelCourseListButton() {
        if (cancelCourseListButton == null) {
            cancelCourseListButton = this.createImageButton(
                    "解約一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_cancel_list_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/service_cancel_list_on.jpg",
                    null,
                    false);
            cancelCourseListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    CancelCourseListPanel content = new CancelCourseListPanel();
                    changeContents(content);
                }
            });
        }

        return cancelCourseListButton;
    }
    // vtbphuong end add 20140708

     //IVS_LVTu start add 2015/11/06 New request #44110
    //契約率集計
    private JButton getContractReportButton() {
        if (contractRateReportButton == null) {
            contractRateReportButton = this.createImageButton(
                    "契約率集計",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/contract_rate_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/contract_rate_on.jpg",
                    null,
                    false);
            contractRateReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ContractRateReportPanel content = new ContractRateReportPanel();
                    changeContents(content);
                }
            });
        }

        return contractRateReportButton;
    }
     //IVS_LVTu end add 2015/11/06 New request #44110

    //IVS_LVTu start add 2015/11/19 New request #44111
    //契約率集計
    private JButton getProposalReportButton() {
        if (ProposalReportButton == null) {
            ProposalReportButton = this.createImageButton(
                    "提案書一覧",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/proposal_list_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/proposal_list_on.jpg",
                    null,
                    false);
            ProposalReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ProposalReportPanel content = new ProposalReportPanel();
                    changeContents(content);
                }
            });
        }

        return ProposalReportButton;
    }
     //IVS_LVTu end add 2015/11/19 New request #44111

    // Start add 20130513 nakhoa
    public JButton getCustomSettingTargetButton() {
        if (customSetteinTagergButton == null) {
            customSetteinTagergButton = this.createImageButton(
                    "カスタム設定",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custom_setting_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custom_setting_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/custom_setting_on.jpg",
                    true);

            customSetteinTagergButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showSubMenu(customSetteinTagergButton.getLocationOnScreen(),
                            customSetteinTagergButton.getWidth(),
                            getcCustomSettingTargetMenu());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                }
            });
        }

        return customSetteinTagergButton;
    }
    // End add 20130513 nakhoa

    /**
     * カスタム設定メニューを取得する。
     *
     * @return カスタム設定メニュー
     */
    public JPanel getcCustomSettingMenu() {
        //テスト用
        if (customSettingMenu == null) {
            customSettingMenu = this.createSubMenu();

            // 目標＆実績＆稼働日数
            this.addSubMenu(customSettingMenu, this.getImageButton(resultTargetButton, "/menu/report/target_result", "目標＆実績＆稼働日数",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            hideSubMenu();
                            (new TargetActualShopInfoFrame()).setVisible(true);
                        }
                    }), -1);

            // 出勤登録画面
            this.addSubMenu(customSettingMenu, this.getImageButton(workingStaffButton, "/menu/report/working_staff", "出勤登録画面",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    workingStaffButton.getModel().setRollover(false);
                            changeContents(new StaffWorkRegistrationPanel());
                        }
                    }), -1);

        }
        return customSettingMenu;
    }

    // Start add 20130513 nakhoa
    public JPanel getcCustomSettingTargetMenu() {
        //テスト用
        if (customSettingMenu == null) {
            customSettingMenu = this.createSubMenu();

            // 20130417 Phuong start add
            // 月目標＆実績＆稼働日数
            this.addSubMenu(customSettingMenu, this.getImageButton(resultTargetButton, "/menu/report/target_result", "目標＆実績＆稼働日数",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            hideSubMenu();
                            //(new TargetPerformanceOperatingDayFrame()).setVisible(true);
                            changeContents(new TargetPerformanceOperatingDayFrame());
                        }
                    }), -1);
            // 20130417 Phuong end add
        }
        return customSettingMenu;
    }
    // End add 20130513 nakhoa

    public JButton getImageButton(JButton btn, String imageName, String name, ActionListener action) {
        if (btn == null) {
            btn = this.createImageButton(
                    name,
                    "/images/" + SystemInfo.getSkinPackage() + imageName + "_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + imageName + "_on.jpg",
                    null,
                    false);

            btn.addActionListener(action);
        }

        return btn;
    }

    //IVS_LVTu start add 2018/13/05 GB_Finc
    /**
     * 月会員管理
     *
     * @return 月会員管理マスタメニュー
     */
    public JPanel getManageMemberMasterMenu() {
        if (manageMemberMasterMenu == null) {
            manageMemberMasterMenu = this.createSubMenu();

            //プラン登録
            this.addSubMenu(manageMemberMasterMenu, this.getMstPlanButton(), -1);

            //月会員登録
            this.addSubMenu(manageMemberMasterMenu, this.getDataMonMemberButton(), -1);

            //月会員登録
            this.addSubMenu(manageMemberMasterMenu, this.getDataMonthlyBatchButton(), -1);
            //IVS start add 2020/04/01 口座振替連携
            //口座振替連携
            if (SystemInfo.getSetteing().isUseAccountTransfer()) {
                this.addSubMenu(manageMemberMasterMenu, this.getTransferLinkButton(), -1);
            }
            //IVS end add 2020/04/01 口座振替連携
        }

        return manageMemberMasterMenu;
    }

    /**
     * プラン登録ボタンを取得する。
     *
     * @return プラン登録ボタン
     */
    public JButton getMstPlanButton() {
        if (mstPlanButton == null) {
            mstPlanButton = this.createImageButton(
                    "プラン登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_plan_regist_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_plan_regist_on.jpg",
                    null,
                    false);

            mstPlanButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstPlanButton.getModel().setRollover(false);
                    MstPlanPanel mccp = new MstPlanPanel();
                    mccp.setTitle("プラン登録");
                    mccp.setPath("基本設定＞月会員管理");
                    changeContents(mccp);
                }
            });
        }

        return mstPlanButton;
    }

    /**
     * 月会員登録ボタンを取得する。
     *
     * @return 月会員登録ボタン
     */
    public JButton getDataMonMemberButton() {
        if (dataMonMemberButton == null) {
            dataMonMemberButton = this.createImageButton(
                    "月会員登録",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_customer_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_customer_on.jpg",
                    null,
                    false);

            dataMonMemberButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dataMonMemberButton.getModel().setRollover(false);
                    DataMonthMemberPanel dmmp = new DataMonthMemberPanel();
                    dmmp.setTitle("月会員登録");
                    dmmp.setPath("基本設定＞月会員管理");
                    changeContents(dmmp);
                }
            });
        }

        return dataMonMemberButton;
    }

    /**
     * 一括処理ボタンを取得する。
     *
     * @return プ一括処理ボタン
     */
    public JButton getDataMonthlyBatchButton() {
        if (dataMonthlyBatchButton == null) {
            dataMonthlyBatchButton = this.createImageButton(
                    "一括処理",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_batch_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_batch_on.jpg",
                    null,
                    false);

            dataMonthlyBatchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dataMonthlyBatchButton.getModel().setRollover(false);
                    DataMonthlyBatchPanel dmbp = new DataMonthlyBatchPanel();
                    dmbp.setTitle("一括処理");
                    dmbp.setPath("基本設定＞月会員管理");
                    changeContents(dmbp);
                }
            });
        }

        return dataMonthlyBatchButton;
    }
    //IVS_LVTu end add 2018/13/05 GB_Finc
    //IVS start add 2020/04/01 口座振替連携
    /**
     * 口座振替連携
     *
     * @return プ口座振替連携ボタン
     */
    public JButton getTransferLinkButton() {
        if (accountTransferButton == null) {
            accountTransferButton = this.createImageButton(
                    "口座振替連携",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_relation_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_relation_on.jpg",
                    null,
                    false);

            accountTransferButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    accountTransferButton.getModel().setRollover(false);
                    AccountTransferPanel tlp = new AccountTransferPanel();
                    tlp.setTitle("口座振替連携");
                    tlp.setPath("基本設定＞月会員管理");
                    changeContents(tlp);
                }
            });
        }

        return accountTransferButton;
    }
    //IVS end add 2020/04/01 口座振替連携

    //Start add 20220425 TUNEオリジナル帳票出力 の対応
    /**
     * カスタム帳票
     *
     * @return プカスタム帳票ボタン
     */
    private JButton getCustomizeReportmButton() {
        if (customizeReportButton == null) {
            customizeReportButton = this.createImageButton(
                    "カスタム帳票",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/custom_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/custom_report_on.jpg",
                    null,
                    true);

            customizeReportButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    customizeReportButton.getModel().setRollover(false);
                    ReportCustomizePanel rcp = new ReportCustomizePanel();
                    changeContents(rcp);
                }
            });
        }

        return customizeReportButton;
    }
    //ENd add 20220425 TUNEオリジナル帳票出力 の対応

    private void setMark() {
        DataMessagesPanel dmp = new DataMessagesPanel(false);
        if (dmp.isIsLoad()) {
            // Start 20141117 do.hong.quang update Request#32307
//             mark.setIcon(SystemInfo.getImageIcon("/menu/reservation/i2.jpg"));
            mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info_unread.jpg"));
            // End 20141117 do.hong.quang update Request#32307
            //this.mark.setVisible(true);
            this.mark.setEnabled(true);

        } else {

            this.mark.setEnabled(false);
            // Start 20141117 do.hong.quang update Request#32307
//             mark.setIcon(SystemInfo.getImageIcon("/menu/reservation/i2.jpg"));
            mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info.jpg"));
//            this.mark.setEnabled(false);
            this.mark.setEnabled(true);
            // End 20141117 do.hong.quang update Request#32307

        }


    }

    private boolean isMark() {
        //nhanvt edit start 20141015 Bug #31136
        ConnectionWrapper con = SystemInfo.getBaseConnection();
        //nhanvt end start 20141015 Bug #31136
        int count = 0;
        ResultSetWrapper rs = null;
        try {
            rs = con.executeQuery(this.getMarkMsgDataSQL());
            while (rs.next()) {
                count = rs.getInt("total");
            }
            if (count < 1) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }


    }

    private String getMarkMsgDataSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*)  as total  from data_message ");
        sql.append("  where view_flg = 1  ");
        sql.append(" and viewable_date <= current_timestamp  ");
        sql.append("and delete_date is null ");
        return sql.toString();
    }
    // Start 20141117 do.hong.quang add
    /**
     * Function set Icon image to display
     * @return
     */
    private boolean setStatusIconAction(){
        boolean bFlag = this.mark.getIcon().toString().contains("btn_info.jpg");
        if(bFlag==true){
            mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info_on.jpg"));
            return true;
        }
        bFlag = this.mark.getIcon().toString().contains("btn_info_on.jpg");
        if(bFlag==true){
            mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info.jpg"));
            return true;
        }
        bFlag = this.mark.getIcon().toString().contains("btn_info_unread.jpg");
        if(bFlag==true){
            mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info_unread_on.jpg"));
            return true;
        }
        bFlag = this.mark.getIcon().toString().contains("btn_info_unread_on.jpg");
        if(bFlag==true){
            mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info_unread.jpg"));
            return true;
        }
        return false;
    }
    // End 20141117 do.hong.quang add

}

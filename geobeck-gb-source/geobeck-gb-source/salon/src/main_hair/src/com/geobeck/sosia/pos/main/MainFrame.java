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

    /** �J�X�^�}�C�Y����pDB�� */
    private static final String DB_POS_HAIR_DEV = "pos_hair_dev";			// �J��DB
    private static final String DB_POS_HAIR_ACQUAGRZ = "pos_hair_acquagrz";	// �A�N�A�E�O���c�B�G�lDB
    private static final String DB_POS_HAIR_PRIMO = "pos_hair_primo";		// ���f�X�e�B�lDB

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

        // ���V�[�g�v�����^��ݒ肵�Ă���ꍇ�̂݃h���AOPEN�{�^����\������
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
        // Start 20141117 do.hong.quang update Request#32307 [gb]�ً}�|�b�v�A�b�v�A�\��{�^�����C���[�W�ύX
//        this.mark.setIcon(SystemInfo.getImageIcon("/menu/reservation/i2.jpg"));
        mark.setIcon(SystemInfo.getImageIcon("/menu/main/btn_info.jpg"));
//        this.mark.setEnabled(false);
        this.mark.setEnabled(true);
        // End 20141117 do.hong.quang update Request#32307 [gb]�ً}�|�b�v�A�b�v�A�\��{�^�����C���[�W�ύX
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
                    SwingUtil.openAnchorDialog(this, true, pnl, "�y�T�����{�[�h�A�g�z", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                }
            }
        }

        // ���O�C�����Ԃ�ێ�
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
            SwingUtil.openAnchorDialog(this, true, dmp, "���m�点", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        }
        setMark();
        //VTBPHUONG change 20130625
    }

    /**
     * SB�Ƃ̘A�g���ꎞ��~�ɂȂ��Ă��Ȃ������ׂ�
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
                            //�}�̂�HPB���ꎞ��~�Ȃ�A���[�g���o��
                            MessageDialog.showMessageDialog(null,
                                //MessageUtil.getMessage(20006, "��{�ݒ�","�T�C�g�R���g���[���ݒ�","�}�̘A�g�ݒ�"),
                                "\n���݁A�T�����{�[�h�Ƃ̘A�g���ꎞ��~�ɂȂ��Ă��܂�\nSOSIA POS�ɂ��ύX��̃T�����{�[�h�̃p�X���[�h��o�^���Ă��������K�v������܂��B\n\n" +
                                        "�� ��{�ݒ� �� �T�C�g�R���g���[���ݒ� �� �}�̘A�g�ݒ�\n\n����T�����{�[�h�̃p�X���[�h����͂��āA�o�^�{�^���������ĉ������B\n" +
                                        "�o�^��A�T�����{�[�h�Ƃ̘A�g�͎����I�ɍĊJ���܂��B\n\n" +
                                        "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
                                        "���o�^����܂ŃT�����{�[�h�Ƃ̘A�g�͈ꎞ��~�̂܂܂ł��B\n���ꎞ��~���ɓo�^���ꂽ�\��͎����I�ɘA�g����܂����A�x�e�͘A�g����܂���B\n" +
                                        "���ꎞ��~���ɓo�^�����x�e�͍ēx�o�^���ĉ������B\n",
                                "�T�C�g�R���g���[��",
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

        // �o�ދΓo�^���
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

            // ���V�[�g�v�����^���擾
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
    //���C�����j���[
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
    //���i�Ǘ�
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
    //�ڋq�Ǘ�
    private ImagePanel customerMenu = null;
    private JButton registCustomerButton = null;
    private JButton notMemberListButton = null;
    private JButton repeaterButton = null;
    private JButton mobileMemberListButton = null;
    //�\��Ǘ�
    private ImagePanel reservationMenu = null;
    private JButton registReservatioButton = null;
    private JButton showStatusButton = null;
    //���Z�Ǘ�
    private ImagePanel accountMenu = null;
    private JButton inputAccountButton = null;
    private JButton searchAccountButton = null;
    private JButton receivableListButton = null;
    private JButton registerButton = null;
    private JButton registerCashButton = null;
    //���i�Ǘ�
    private ImagePanel itemMenu = null;
    //���[�o��
    private ImagePanel reportMenu = null;
    private JButton businessReportButton = null;
    private JButton salesTotalButton = null;
    private JButton timeAnalysisButton = null;
    private JButton saleTransittionButton = null;
    private JButton staffResultButton = null;
    private JButton responseEffectButton = null;			// ���X�|���X����
    private JButton rankingButton = null;			// �����L���O
    private JButton registerCashIOButton = null;			// ���W���o��
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

    //��������
    private JPanel baseCashMenu = null;
    private JButton cashClassButton = null;
    private JButton cashMenuButton = null;
    private JButton cashRegistButton = null;
    // �J�X�^�����[
    private JButton customSetteingButton = null;
    private JButton customSetteinTagergButton = null;
    private JPanel customReportMenu = null;
    private JButton dailyReportButton = null;
    private JButton monthlyReportButton = null;
    private JButton rankingReportButton = null;
    private JButton repeatReportButton = null;
    private JButton analysisReportButton = null;
    //���[���@�\
    private ImagePanel mailMenu = null;
    private JButton conditionedSearchButton = null;
    private JButton dmHistoryButton = null;
    private JButton templateClassButton = null;
    private JButton templateButton = null;
    private JButton signatureButton = null;
    private JButton autoMailButton = null;                   // �������[���ݒ�{�^��
    // ���[�Ǘ�
    private JButton effectIndicatorAnalysisButton = null;
    private JButton reasonsRankButton = null;
    private JButton itemSalesRankingButton = null;
    private JButton postMaxResultsButton = null;
    private JButton itemStockListButton = null;
    //��{���
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
    // �J�X�^���ݒ�
    private JButton customReportButton = null;
    private JPanel customSettingMenu = null;
    private JButton resultTargetButton = null;
    private JButton workingStaffButton = null;
    //��Ѓ}�X�^
    private JPanel companyMasterMenu = null;
    private JButton mstCompanyButton = null;
    private JButton mstGroupButton = null;
    //IVS_LTThuc start add 20140707 MASHU_�Ƒԓo�^
    private JButton mstUseShopCategoryButton = null;
    //IVS_LTThuc start end 20140707 MASHU_�Ƒԓo�^
    private JButton mstAuthorityButton = null;
    private JButton mstShopButton = null;
    private JButton mstStaffClassButton = null;
    private JButton mstStaffButton = null;
    private JButton mstWorkTimePassButton = null;
    private JButton mstBedButton = null;
    private JButton mstResponseButton = null;		// �������ړo�^�{�^��
    //Start add 20131102 lvut (rappa --> gb_source)
    private JButton mstResponseClassButton = null;
    //End add 20131102 lvut (rappa --> gb_source)
    private JButton mstBasicShiftButton = null;
    private JButton mstStaffShiftButton = null;
    //Thanh add start 20130415 �ڋq�_�񗚗�
    private JButton cusContractHistoryButton = null;
    //Thanh add End 20130415 �ڋq�_�񗚗�
    private JButton visitKarteSettingButton  = null; // ���X�J���e�ݒ�{�^��
    private JPanel customMenu = null;
    private JButton customButton = null;   //�S���ʐ��шꗗ�\
    //���i�}�X�^
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
     * �u��}�X�^
     */
    private JButton mstPlaceButton = null;
    //�Z�p�}�X�^
    private JPanel technicMasterMenu = null;
    private JButton mstTechnicClassButton = null;
    private JButton mstTechnicButton = null;
    private JButton mstProportionallyButton = null;	    // ���}�X�^�o�^
    private JButton mstTechProportionallyButton = null;	    // �Z�p���o�^
    private JButton mstUseTechnicButton = null;
    private JButton mstStaffTechnicTimeButton = null;		// �X�^�b�t���{�p���ԓo�^
    private JButton mstCourseClassButton = null; //�R�[�X�_�񕪗ޓo�^
    private JButton mstCourseButton = null;  //�R�[�X�o�^
    private JButton mstUseCourseButton = null; //�g�p�R�[�X�o�^
	//�}�X�^�ꊇ�o�^ add start 2016/12/28
	private JButton mstTechnicRegistBulkButton = null; //�Z�p�ꊇ�o�^
	//�}�X�^�ꊇ�o�^ add end 2016/12/28

    //�ڋq�}�X�^
    private JPanel customerMasterMenu = null;
    private JButton mstJobButton = null;
    private JButton mstFirstComingMotiveButton = null;   //���񗈓X���@�o�^
    private JButton mstCustomerRankSettingButton = null;   //�ڋq�����N�ݒ�
    private JButton mstKarteClassButton = null;   //�J���e���ޓo�^
    private JButton mstKarteDetailButton = null;   //�J���e�ڍדo�^
    private JButton mstKarteReferenceButton = null;   //�J���e�Q�ƍ��ړo�^
    private JButton mstFreeHeadingClassButton = null;		// �t���[���ڋ敪�o�^�{�^��
    private JButton mstFreeHeadingButton = null;		// �t���[���ړo�^�{�^��
    private JButton mstCustomerIntegrationButton = null;		// �ڋq����
    //���Z�}�X�^
    private JPanel accountMasterMenu = null;
    private JButton mstTaxButton = null;
    private JButton mstPaymentMethodButton = null;
    private JButton mstDiscountButton = null;
    private JButton receiptSettingButton = null;
    //�ڋq���� add
    private JPanel customerEffectMenu = null;
    private JButton customerEffectButton = null;
    //���㕪�� add
    private JPanel salesEffectMenu = null;
    private JButton storeButton = null;
    private JButton salesEffectButton = null;
    // �ė����� add
    private JPanel reappearanceEffectMenu = null;
    private JButton reappearanceEffectButton = null;
    private JPanel repeatAnalysisMenu = null;
    private JButton repeatAnalysisButton = null;
    private JButton responseRepeatAnalyzetButton = null;
    // ��������
    private JButton reappearancePredictionEffectButton = null;
    // �N���X����
    private JButton crossAnalysisEffectButton = null;
    private JButton crossAnalysis5EffectButton = null;
    // �T�C�N������
    private JButton karteAnalysisEffectButton = null;
    // �q����蔭���V�[�g��
    private JButton customerProblemSheetButton = null;
    //�X�^�b�t���� add
    private JPanel staffResultMenu = null;
    private JButton staffResultListButton = null;   //�S���ʐ��шꗗ�\
    private JButton staffResultTechButton = null;   //�S���ʋZ�p����
    private JButton staffResultGoodsButton = null;   //�S���ʏ��i����
    private JButton staffResultCustomerButton = null;   //�S���ʌڋq����
    private JButton staffResultRepeatButton = null;   //�S���ʍė�����
    //���Ɓ[���񕪐�
    private JButton itoCustomerProblemSheetButton = null;
    private JButton itoAnalitic3jigenButton = null;
    //Mail add
    private JButton mailMasterButton = null;
    private JPanel mailMasterMenu = null;
    //OX�J�X�^�����[
    private JButton salesComparisionButton = null;
    private JButton salesReportButton = null;
    // �|�C���g�J�[�h
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
    // �𖱒��[
    private JPanel serviceReportMenu = null;
    private JButton serviceReportButton = null;
    private JButton serviceListButton = null; // �𖱎c�ꗗ
    private JButton cancelCourseListButton = null; // ���ꗗ
    private JButton expirationListButton = null; // �L�������ꗗ
     //IVS_LVTu start add 2015/11/06 New request #44110
    private JButton contractRateReportButton = null; // �_�񗦏W�v
     //IVS_LVTu end add 2015/11/06 New request #44110
    //IVS_LVTu start add 2015/11/21 New request #44111
    private JButton ProposalReportButton = null; // �_�񗦏W�v
     //IVS_LVTu end add 2015/11/21 New request #44111
    private JButton PointReportButton = null;
    private JButton SettingKANZASHI = null;

    //IVS_LVTu start add 2018/13/05 GB_Finc
    private JPanel manageMemberMasterMenu   = null;
    private JButton manageMemberMonButton   = null;   // ������Ǘ�
    private JButton mstPlanButton           = null;   //�v�����o�^
    private JButton dataMonMemberButton     = null;   //������o�^
    private JButton dataMonthlyBatchButton  = null;   //�ꊇ����
    //IVS start add 2020/04/01 �����U�֘A�g
    private JButton accountTransferButton  = null;   //�����U�֘A�g
    //IVS end add 2020/04/01 �����U�֘A�g
    //IVS_LVTu end add 2018/13/05 GB_Finc
    //Start add 20220502 TUNE�I���W�i�����[�o�� �̑Ή�
     private JButton customizeReportButton  = null;   //�J�X�^�����[
    //End add 20220502 TUNE�I���W�i�����[�o�� �̑Ή�

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
     * �I���������s���B
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
                    msg.append("�� �ݓX�҂� " + stayCount + " �l���܂��B\n");
                }
                if (workingCount > 0) {
                    msg.append("�� �ދ΂��Ă��Ȃ��X�^�b�t�����܂��B\n");
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
                 //���W�Ǘ��p���[�����M�@�\
                HairRegister register = new HairRegister();
                java.sql.Date result = register.checkMailSend();

                //�����M���[��������
                if (result != null) {
                    //
                    if (MessageDialog.showYesNoDialog(this,
                            "���W�Ǘ���񂪖����M�ł��B\n���M���Ă���낵���ł����H",
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
                                        MessageUtil.getMessage(200, "���[�����M����"),
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
                                        "���W���߃��[���̑��M�Ɏ��s���܂����B\n" +
                                        "���Ԃ������čēx�A�o�^���s���Ă��������B",
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE,
                                        JOptionPane.NO_OPTION);
                            if (ret == JOptionPane.YES_OPTION) return;
                       }
                       register.sendMailEndFlgOn(SystemInfo.getConnection(), date);

                    }

                }
                 //�^�C�}�[���~�߂�
                timer.stop();
                timer = null;
                clockTimer.stop();
                clockTimer = null;

                SystemInfo.closeBarcodeReaderConnection();
                SystemInfo.closeCtiReaderConnection();

                //nhanvt end 20141229 edit New request #34634
                // �J�X�^�}�[�f�B�X�v���C�̉�ʏ���
                if (SystemInfo.isUseCustomerDisplay()) {
                    String comport = SystemInfo.getCustomerDisplayPort();
                    if (comport != null && !comport.equals("")) {
                        CustomerDisplay.getInstance(comport).clearScreen();
                    }
                }

                // �X�܃��O�C���̏ꍇ�͗����ȍ~�̗\����_�E�����[�h
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
        // IVS start add 2022/08/15 �Z�p���j���[���͊֌W�Ȃ��ݓX���Ă��鏃����\������悤�C��
        sql.append(" (select distinct dr.shop_id, dr.reservation_no");
        sql.append(" from");
        // IVS end add 2022/08/15 �Z�p���j���[���͊֌W�Ȃ��ݓX���Ă��鏃����\������悤�C��
        sql.append("     data_reservation dr");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("                 on drd.shop_id = dr.shop_id");
        sql.append("                and drd.reservation_no = dr.reservation_no");
        sql.append("                and drd.delete_date is null");
        // IVS start edit 2022/08/15 �Z�p���j���[���͊֌W�Ȃ��ݓX���Ă��鏃����\������悤�C��
        //sql.append("         left join mst_customer mc");
        //sql.append("                on mc.customer_id = dr.customer_id");
        //sql.append("         left join mst_technic mt");
        //sql.append("                on mt.technic_id = drd.technic_id");
        //sql.append("         left join mst_technic_class mtc");
        //sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        //sql.append("         left join mst_staff ms");
        //sql.append("                on ms.staff_id = dr.staff_id");
        // IVS start edit 2022/08/15 �Z�p���j���[���͊֌W�Ȃ��ݓX���Ă��鏃����\������悤�C��
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(calStart));
        sql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(calEnd));
        sql.append("     and dr.status in (2)");
        // IVS start add 2022/08/15 �Z�p���j���[���͊֌W�Ȃ��ݓX���Ă��鏃����\������悤�C��
        sql.append(" ) temp");
        // IVS end add 2022/08/15 �Z�p���j���[���͊֌W�Ȃ��ݓX���Ă��鏃����\������悤�C��

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

        final String[] fn_strWDay = {"��", "��", "��", "��", "��", "��", "�y"};

        String dirName = SystemInfo.getLogRoot() + "/../reserve";
        String fileName = dirName + "/�\����.txt";

        try {

            (new File(dirName)).mkdir();

            File file = new File(fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy�NM��d��");
            SimpleDateFormat sdf2 = new SimpleDateFormat("H:mm");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Calendar calStart = (Calendar) cal.clone();
            cal.add(Calendar.DAY_OF_MONTH, 13);
            Calendar calEnd = (Calendar) cal.clone();

            bw.write(sdf.format(calStart.getTime()) + "�`" + sdf.format(calEnd.getTime()) + "�܂ł̗\����");
            bw.newLine();
            bw.newLine();
            bw.write("-----------------------------");
            bw.newLine();

            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getNextReservationSQL(calStart, calEnd));
            while (rs.next()) {
                cal.setTimeInMillis(rs.getTimestamp("reservation_datetime").getTime());
                bw.write(sdf.format(cal.getTime()) + "�i" + fn_strWDay[cal.get(Calendar.DAY_OF_WEEK) - 1] + "�j");
                bw.newLine();
                bw.write("[�\�񎞊�]" + sdf2.format(cal.getTime()));
                bw.newLine();
                bw.write("[�ڋqNo]" + rs.getString("customer_no"));
                bw.newLine();
                bw.write("[�ڋq��]" + rs.getString("customer_name") + "�i" + rs.getString("customer_kana") + "�j");
                bw.newLine();
                bw.write("[�\����e]" + rs.getString("technic_name"));
                bw.newLine();
                // IVS SANG START INSERT 20131024
                bw.write("[�R�[�X�_��]" + rs.getString("course_contract_name"));
                bw.newLine();
                bw.write("[�����R�[�X]" + rs.getString("course_contract_digestion_name"));
                bw.newLine();
                // IVS SANG END INSERT 20131024
                bw.write("[��S����]" + rs.getString("staff_name"));
                bw.write("�i" + rs.getString("designated") + "�j");
                bw.newLine();

                if (SystemInfo.getCurrentShop().isBed()) {
                    bw.write("[�{�p��]" + rs.getString("bed_name"));
                    bw.newLine();
                }

                bw.write("[�\�񃁃�]");
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
        sql.append("     ), '�A') as technic_name");
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
        sql.append("     ), '�A'), '') as bed_name,\n");
        // IVS SANG START INSERT 20131024

        sql.append("coalesce(array_to_string(array( \n");
        sql.append("    select             mc.course_name \n");
        sql.append("    from             data_reservation_detail drd \n");
        sql.append("        inner join mst_course mc on drd.technic_id = mc.course_id \n");
        sql.append("    where   drd.course_flg = 1 and \n");
        sql.append("        drd.delete_date is null\n");
        sql.append("        and drd.shop_id = t.shop_id \n");
        sql.append("        and drd.reservation_no = t.reservation_no \n");
        sql.append("    order by             drd.reservation_datetime     ), '�A'), '') as course_contract_name, \n");

        sql.append("coalesce(array_to_string(array(  \n");
        sql.append("    select             mc.course_name   \n");
        sql.append("    from             data_reservation_detail drd   \n");
        sql.append("        inner join mst_course mc on drd.technic_id = mc.course_id \n");
        sql.append("    where   drd.course_flg = 2 and \n");
        sql.append("        drd.delete_date is null \n");
        sql.append("        and drd.shop_id = t.shop_id  \n");
        sql.append("        and drd.reservation_no = t.reservation_no  \n");
        sql.append("    order by             drd.reservation_datetime     ), '�A'), '') as course_contract_digestion_name \n");
        // IVS SANG END INSERT 20131024
        sql.append(" from");
        sql.append(" (");
        sql.append("     select");
        sql.append("          dr.shop_id");
        sql.append("         ,dr.reservation_no");
        sql.append("         ,min(drd.reservation_datetime) as reservation_datetime");
        sql.append("         ,min(mc.customer_no) as customer_no");
        sql.append("         ,min(mc.customer_name1 || '�@' || mc.customer_name2) as customer_name");
        sql.append("         ,min(mc.customer_kana1 || '�@' || mc.customer_kana2) as customer_kana");
        sql.append("         ,coalesce(min(ms.staff_name1 || '�@' || ms.staff_name2), '�S���҂Ȃ�') as staff_name");
        sql.append("         ,min(case when dr.designated_flag then '�w' else 'F' end) as designated");
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
     * ���j���[�p�^�C�}�[������������B
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
     * �R���e���c�̃t�H�[�J�X���X�i�[������������B
     */
    private void initContentsFocusListener() {
        //�t�H�[�J�X���擾�����Ƃ�
        fa = new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hideSubMenu();
            }
        };

        //�L�[�������ꂽ�Ƃ�
        ka = new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hideSubMenu();
            }
        };

        //�}�E�X���N���b�N���ꂽ�Ƃ�
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
     * �R���|�[�l���g�̎q�R���|�[�l���g�Ƀ��X�i�[��ǉ�����B
     *
     * @param c �R���|�[�l���g
     */
    private void addChildrenListener(Component c) {
        //���ɒǉ��ς̏ꍇ�͏����𔲂���
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
     * �摜�̃{�^�����쐬����B
     *
     * @param name ���O
     * @param defaultIconPath �W���̉摜�̃p�X
     * @param rolloverIconPath ���[���I�[�o�[���̉摜�̃p�X
     * @param disabledIconPath �g�p�s�\���̉摜�̃p�X
     * @param isHideSubMenu �N���b�N���ɃT�u���j���[����邩�B
     * @return �摜�̃{�^��
     */
    private JButton createImageButton(String name,
            String defaultIconPath,
            String rolloverIconPath,
            String disabledIconPath,
            boolean isHideSubMenu) {
        JButton button = SwingCreator.createImageButton(
                null, defaultIconPath, rolloverIconPath, disabledIconPath);

        //�}�E�X�J�[�\����ύX����C�x���g���X�i�[��ǉ�
        SystemInfo.addMouseCursorChange(button);

        //�N���b�N���ɃT�u���j���[�����ꍇ
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
     * ���C�����j���[�̃{�^���ɃC�x���g���X�i�[��ǉ�����B
     *
     * @param button �C�x���g���X�i�[��ǉ�����{�^��
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
     * �ڋq�Ǘ��{�^�����擾����B
     *
     * @return �ڋq�Ǘ��{�^��
     */
    private MainMenuButton getCustomerButton() {
        if (customerButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                customerButton = new MainMenuButton(this.createImageButton(
                        "�X�Ɩ�",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/closed_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/closed_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                customerButton = new MainMenuButton(this.createImageButton(
                        "�X�Ɩ�",
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
				//���f�X�e�B���菈��
				if (( SystemInfo.getDatabase().indexOf(DB_POS_HAIR_PRIMO) > -1) || ( SystemInfo.getDatabase().indexOf(DB_POS_HAIR_DEV) > -1)) {
					customerButton.addSubMenuButton(this.getCustomCutoffReportButton());
				}
            }

        }

        return customerButton;
    }

    /**
     * �\��Ǘ��{�^�����擾����B
     *
     * @return �\��Ǘ��{�^��
     */
    private MainMenuButton getReservationButton() {
        if (reservationButton == null) {
            reservationButton = new MainMenuButton(this.createImageButton(
                    "�t�����g�Ɩ�",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/front_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/front_on.jpg",
                    null,
                    true),
                    SystemInfo.getMenuColor());

            this.addMainMenuButtonEvent(reservationButton);

            // �\��\
            reservationButton.addSubMenuButton(this.getRegistReservatioButton());

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                // �t�����g
                reservationButton.addSubMenuButton(this.getShowStatusButton());

                // �X�L�b�v����v�i�{���ł͕\�����Ȃ��j
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
     * ���Z�Ǘ��{�^�����擾����B
     *
     * @return ���Z�Ǘ��{�^��
     */
    private MainMenuButton getAccountButton() {
        if (accountButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                accountButton = new MainMenuButton(this.createImageButton(
                        "���W���Ǘ�",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cash_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cash_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                accountButton = new MainMenuButton(this.createImageButton(
                        "���W���Ǘ�",
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
     * ���[�o�̓{�^�����擾����B
     *
     * @return ���[�o�̓{�^��
     */
    private MainMenuButton getReportButton() {
        if (reportButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                reportButton = new MainMenuButton(this.createImageButton(
                        "���͒��[",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                reportButton = new MainMenuButton(this.createImageButton(
                        "���͒��[",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/analitic_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(reportButton);

//                // �ڋq����
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

                //Start add 20220502 TUNE�I���W�i�����[�o�� �̑Ή�
                if (SystemInfo.getDatabase().equals("pos_hair_dev")
                        || SystemInfo.getDatabase().startsWith("pos_hair_tune")) {
                    reportButton.addSubMenuButton(this.getCustomizeReportmButton());
                }
                //End add 20220502 TUNE�I���W�i�����[�o�� �̑Ή�
                
                // ���㕪��
                if (SystemInfo.checkAuthority(63)) {
                    boolean isVisible = false;
                    isVisible = isVisible || SystemInfo.checkAuthority(80);
                    isVisible = isVisible || SystemInfo.checkAuthority(81);
                    isVisible = isVisible || SystemInfo.getSetteing().isItoAnalysis();
                    if (isVisible) {
                        reportButton.addSubMenuButton(this.getSalesEffectButton());
                    }
                }

                // �X�^�b�t����
                if (SystemInfo.checkAuthority(64)) {
                    for (int i = 90; i <= 94; i++) {
                        if (SystemInfo.checkAuthority(i)) {
                            reportButton.addSubMenuButton(this.getStaffResultButton());
                            break;
                        }
                    }
                }

                //Thanh add start 20130129 �J�X�^�����[
                if (SystemInfo.getDatabase().equals("pos_hair_nons")
                        || SystemInfo.getDatabase().equals("pos_hair_nons_bak")
                        || SystemInfo.getDatabase().equals("pos_hair_dev")
                        || SystemInfo.getDatabase().equals("pos_hair_ox2")) {
                    reportButton.addSubMenuButton(this.getCustomButton());
                }
                //Thanh add start 20130129 �J�X�^����

				// 20161227 add start �I���W�i�����[�o�͉��
                if (SystemInfo.getDatabase().equals("pos_hair_nonail")
                        || SystemInfo.getDatabase().equals("pos_hair_nonailm")
                        || SystemInfo.getDatabase().equals("pos_hair_nonailf")
                        || SystemInfo.getDatabase().equals("pos_hair_nonail_dev")) {
                    reportButton.addSubMenuButton(this.getOriginalReportButton());
                }
                // 20161227 add end �I���W�i�����[�o�͉��

                // Luc add start 20121004
                if (!SystemInfo.getSystemType().equals(2)) {
                reportButton.addSubMenuButton(this.getStoreButton());
                }
                //Luc add End 20121004

                // �ė�����
                if (SystemInfo.checkAuthority(65)) {
                    reportButton.addSubMenuButton(this.getReappearanceEffectButton());
                }

//                // �����L���O
//                if (SystemInfo.checkAuthority(66)) {
//                    for (int i = 100; i <= 105; i++) {
//                        if (SystemInfo.checkAuthority(i)) {
//                            reportButton.addSubMenuButton(this.getRankingButton());
//                            break;
//                        }
//                    }
//                }
                //Son add End 20130328 ��������
                // ��������
                if (0 < this.getRepeatAnalysisMenu().getComponentCount()) {
                    reportButton.addSubMenuButton(this.getRepeatAnalysisButton());
                }

                if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_charm")) {
                    reportButton.addSubMenuButton(this.getCustomReportSubButton());    // �J�X�^�����[
                }

                // Thanh add start 20130514 ���㕪�͕\
                //reportButton.addSubMenuButton(this.getTurnOverAnalyzeButton());
                // Thanh add End 20130514 �X�ܕ���
                // vtbphuong start add 20140708 �𖱒��[
                if (SystemInfo.getCurrentShop().getShopID() != 0 && SystemInfo.getCurrentShop().getCourseFlag() != null && SystemInfo.getCurrentShop().getCourseFlag() == 1) {
                    reportButton.addSubMenuButton(this.getServiceReportButton());
                } else if (SystemInfo.getCurrentShop().getShopID() == 0) {
                    reportButton.addSubMenuButton(this.getServiceReportButton());
                }
                // vtbphuong end add  20140708 �𖱒��[
                //IVS_nahoang GB_Mashu_�ڕW�ݒ� Start add 20141021
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
                //IVS_nahoang GB_Mashu_�ڕW�ݒ� End add
                // ���͒��[�ˌ��ʎw�W���l����
                //nhanvt start 20150210 New request #35189
                if ((SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_dev"))) {
                    reportButton.addSubMenuButton(this.getEffectIndicatorAnalysisButton());
                }
                //nhanvt end 20150210 New request #35189
                // ���͒��[�ˉߋ��ő���яW�v
                if (!SystemInfo.getSystemType().equals(2)) {
                reportButton.addSubMenuButton(this.getPostMaxResultsButton());
                }

                //IVS_LVTu start add 2016/03/08 New request #48811
                // �|�C���g�W�v
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
     * ���[�o�̓{�^�����擾����B
     *
     * @return ���[�o�̓{�^��
     */
    private MainMenuButton getAnaliticCustomerButton() {
        if (analiticCustomerButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {
                analiticCustomerButton = new MainMenuButton(this.createImageButton(
                        "�ڋq����",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());
            } else {
                analiticCustomerButton = new MainMenuButton(this.createImageButton(
                        "�ڋq����",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/cust_analitic_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

                this.addMainMenuButtonEvent(analiticCustomerButton);

                //��������
                analiticCustomerButton.addSubMenuButton(this.getReappearancePredictionEffectButton());

                //�N���X����
                if (!SystemInfo.getSystemType().equals(2)) {
                analiticCustomerButton.addSubMenuButton(this.getCrossAnalysisEffectButton());

                //�N���X����5
                analiticCustomerButton.addSubMenuButton(this.getCrossAnalysis5EffectButton());
                }

                //Luc Add Start 20121004 �G���A����
                //analiticCustomerButton.addSubMenuButton(this.getCustomerAreaAnalysisButton());
                //Luc Add End 20121004 �G���A����

                //Luc Add Start 20121004 �ڋq��������
                if (!SystemInfo.getSystemType().equals(2)) {
                analiticCustomerButton.addSubMenuButton(this.getCustomerAnalysisAttributeButton());
                }
                //Luc Add End 20121004 �ڋq��������

                //�J���e����
                if (SystemInfo.getSetteing().isItoAnalysis()) {
                    analiticCustomerButton.addSubMenuButton(this.getItoAnalitic3jigenButton());
                }

                //Thanh Add Start 20130415 �ڋq�_�񗚗�
                if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_dev") || SystemInfo.getDatabase().startsWith("pos_hair_dev2") || SystemInfo.getDatabase().startsWith("pos_hair_nons_bak")) {
                    analiticCustomerButton.addSubMenuButton(this.getCusContractHistoryButton());
                }
                //Thanh Add End 20130415 �ڋq�_�񗚗�
            //GB_Mashu Task #34581 [Product][Code][Phase3]�}�b�V���[����
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
                        "�����L���O",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());
            } else {
                menuRankingButton = new MainMenuButton(this.createImageButton(
                        "�����L���O",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_off.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/ranking_on.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());
                this.addMainMenuButtonEvent(menuRankingButton);

                // �����L���O
                if (SystemInfo.checkAuthority(66)) {
                    for (int i = 100; i <= 105; i++) {
                        if (SystemInfo.checkAuthority(i)) {
                            menuRankingButton.addSubMenuButton(this.getRankingButton());
                            break;
                        }
                    }
                }

                // �����L���O�˗��X���R�����L���O
                //nhanvt start edit 20150212 New request #35189
                if ((SystemInfo.getDatabase().toLowerCase().startsWith("pos_hair_dev")))
                {
                        menuRankingButton.addSubMenuButton(this.getReasonsRankButton());
                }
                //nhanvt end edit 20150212 New request #35189
                // �����L���O�ˏ��i���ド���L���O
                if (!SystemInfo.getSystemType().equals(2)) {
                menuRankingButton.addSubMenuButton(this.getItemSalesRankingButton());
                }
            }
        }

        return menuRankingButton;
    }
    //Thanh end add 2013/06/03

    /**
     * ���[���@�\�{�^�����擾����B
     *
     * @return ���[���@�\�{�^��
     */
    private MainMenuButton getMailButton() {
        if (mailButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                mailButton = new MainMenuButton(this.createImageButton(
                        "���X���i",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/promotiton_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/promotiton_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                mailButton = new MainMenuButton(this.createImageButton(
                        "���X���i",
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
     * ���[�Ǘ��{�^�����擾����B
     *
     * @return ���[�Ǘ��{�^��
     */
    private MainMenuButton getReportManageButton() {
        if (reportManageButton == null) {
            //if (SystemInfo.isReservationOnly()) {
            if (SystemInfo.getSystemType().equals(1)) {

                reportManageButton = new MainMenuButton(this.createImageButton(
                        "���[�Ǘ�",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/form_management_gray.jpg",
                        "/images/" + SystemInfo.getSkinPackage() + "/menu/main/form_management_gray.jpg",
                        null,
                        true),
                        SystemInfo.getMenuColor());

            } else {

                reportManageButton = new MainMenuButton(this.createImageButton(
                        "���[�Ǘ�",
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
     * ��{�ݒ�{�^�����擾����B
     *
     * @return ��{�ݒ�{�^��
     */
    private MainMenuButton getMasterButton() {
        if (masterButton == null) {
            masterButton = new MainMenuButton(this.createImageButton(
                    "��{�ݒ�",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/base_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/base_on.jpg",
                    null,
                    true),
                    /*
                     "��{�ݒ�",
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

            //�ڋq�֘A
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

            //���[���֘A
            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                if (0 < this.getMailMasterMenu().getComponentCount()) {
                    masterButton.addSubMenuButton(this.getMailMasterButton());
                }
            }

            // �|�C���g�J�[�h�֘A
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
			// �A�N�A�E�O���c�B�G�l�p CSV�C���|�[�g�@�\�L����
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
                masterButton.addSubMenuButton(this.getCustomSettingTargetButton());    // �J�X�^���ݒ�
            }
            // End add 20130513 nakhoa

            if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_charm") || SystemInfo.getDatabase().startsWith("pos_hair_ox2")) {
                masterButton.addSubMenuButton(this.getCustomSettingButton());    // �J�X�^���ݒ�
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
     * ���g�p�f�U�C���Œǉ� �w���v�{�^�����擾����B
     *
     * @return �w���v�{�^��
     */
    private MainMenuButton getHelpButton() {
        if (helpButton == null) {
            helpButton = new MainMenuButton(this.createImageButton(
                    "�w���v",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/help_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/main/help_on.jpg",
                    null,
                    true),
                    /*
                     "�w���v",
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
     * ���C�����j���[�Ƀ{�^����ǉ�����B
     *
     * @param menuButton �ǉ�����{�^��
     * @param x �{�^����X���W
     */
    private void addMainMenu(MainMenuButton menuButton) {
        menuPanel.add(menuButton);
        menuButton.setBounds(0,
                59 * menuPanel.getComponents().length,
                menuButton.getWidth(),
                menuButton.getHeight());
    }

    /**
     * �^�C�g��������������B
     */
    private void initTitle() {

        String demoStr = "";
        //IVS_LVTu start delete 2016/02/23 New request #48339
//        if (Login.registeredMacAddress.startsWith("tlimit")) {
//            try {
//                SimpleDateFormat f = new SimpleDateFormat("yyyy�NMM��dd��");
//                demoStr = " - �f���ł̎g�p������ " + f.format(DateFormat.getDateInstance().parse(Login.registeredMacAddress.substring(7).replace("-", "/"))) + " �܂łł��B";
//            } catch (Exception e) {
//            }
//        }
        //IVS_LVTu end delete 2016/02/23 New request #48339

        setTitle("SOSIA POS SALON");

        //�{���A�X�܂ł̕ύX
        if (SystemInfo.getCurrentShop().getShopID() == 0) {
            setTitle(getTitle() + " �{���V�X�e��" + demoStr);
            //�{���p���S
            topLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/topLogo_Head.jpg")));
        } else {
            setTitle(getTitle() + " [ " + SystemInfo.getCurrentShop().getShopName() + " ]" + demoStr);
            //�X�ܗp���S
            topLogo.setImage(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/topLogo.jpg")));
        }

        this.title = getTitle();
    }

    public void setMessageTitle(String msg) {
        setTitle(this.title + msg);
    }

    /**
     * ���C�����j���[������������B
     */
    private void initMainMenu() {
        //�\��Ǘ�
        this.addMainMenu(this.getReservationButton());

        //���Z�Ǘ�
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getAccountButton());
        }

        //�ڋq�Ǘ�
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getCustomerButton());
        }

        //���[�o��
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getReportButton());
        }

        //�ڋq����
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getAnaliticCustomerButton());
        }
        //�����L���O
        // IVS_LVTu start add 20150303 Bug #35227
        if(!SystemInfo.getSystemType().equals(1)) {
            if(SystemInfo.checkAuthority66(66)){
                this.addMainMenu(this.getMenuRankingButton());
            }
        }
        // IVS_LVTu end add 20150303 Bug #35227

        //��{�ݒ�
        this.addMainMenu(this.getMasterButton());

        if (SystemInfo.getSetteing().isMerchandise()) {
            //���i�Ǘ�
            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                this.addMainMenu(this.getProductButton());
            }
        }

        //�w���v
//		this.addMainMenu(this.getHelpButton());


        //���[���@�\
        if(!SystemInfo.getSystemType().equals(1)) {
            this.addMainMenu(this.getMailButton());
        }

        // ���i�݌ɐ��ꗗ�ɂ��ă��j���[�ʒu�̕ύX
	    // ���i�Ǘ��˒��[�ˏ��i�݌ɐ��ꗗ �Ɉړ�
        // �u���[�Ǘ��v�̃��j���[���͉̂��������ɂȂ�̂ŕ\�����Ȃ�
        // ���[�Ǘ�
        // this.addMainMenu(this.getReportManageButton());
    }

    /**
     * �T�u���j���[�p��JPanel���쐬����B
     *
     * @return �T�u���j���[�p��JPanel
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
     * �T�u���j���[�Ƀ{�^����ǉ�����B
     *
     * @param subMenu �T�u���j���[
     * @param menuButton �ǉ�����{�^��
     * @param x �{�^����X���W
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
     * �R���e���c��ύX����B
     *
     * @param contents �R���e���c
     */
    public void changeContents(AbstractImagePanelEx contents) {
        if (isChanging) {
            return;
        }
        //IVS_LVTu start add 2015/05/12 New request #36645
        if (SystemInfo.flagChangeContents) {
            //Luc start edit 20160411 #49521
            if (MessageDialog.showYesNoDialog(this,
                            "���W���ߓ��e���ۑ�����Ă��܂��񂪂�낵���ł����H",
                            "���W�Ǘ�",
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
        //���X�̃^�C�g��������\��
        //contentsName.setText((contents.getPath().equals("") ? "" : contents.getPath() + " >> " + contents.getTitle()));
        // ATGS �摜��؂�ւ���
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
     * �R���e���c�^�C�g����IconPath��Ԃ� �^�C�g���摜�ɕύX�Ή�
     *
     * @param ���X�̃^�C�g��������
     * @return �摜�p�X������
     */
    private String getContentsTitlePath(String contentsTitle) {
        StringBuilder strPath = new StringBuilder();
        //�摜�p�X�쐬
        strPath.append("/images/");
        strPath.append(SystemInfo.getSkinPackage());
        strPath.append("/menu/title/");

        //���X�̃^�C�g�������摜�I��
        if (contentsTitle.equals("�\��o�^")) {//���X�̃^�C�g����
            strPath.append("title_f_re.jpg");//�Ή�����摜��
        } else if (contentsTitle.equals("�o�ދΓo�^")) {
            strPath.append("title_work_staff.jpg");
        } else if (contentsTitle.equals("�t�����g")) {
            strPath.append("title_f_zai.jpg");
        } else if (contentsTitle.equals("���W���Z")) {
            strPath.append("title_f_kaikei.jpg");
        } else if (contentsTitle.equals("���W�o�[��")) {
            strPath.append("title_r_regi.jpg");
        } else if (contentsTitle.equals("�������ړo�^")) {
            strPath.append("title_r_cash_class.jpg");
        } else if (contentsTitle.equals("�����ڍדo�^")) {
            strPath.append("title_r_cash_menu.jpg");
        } else if (contentsTitle.equals("���������Ǘ�")) {
            strPath.append("title_r_cash.jpg");
        } else if (contentsTitle.equals("���|�������")) {
            strPath.append("title_r_urikake.jpg");
        } else if (contentsTitle.equals("���|�����")) {
            strPath.append("title_r_urikake_shori.jpg");
        } else if (contentsTitle.equals("���W���o���Ǘ��\")) {
            strPath.append("title_r_month_r.jpg");
        } else if (contentsTitle.equals("�`�[����")) {
            strPath.append("title_c_den.jpg");
        } else if (contentsTitle.equals("���W�Ǘ�")) {
            strPath.append("title_c_regi.jpg");
        } else if (contentsTitle.equals("�Ɩ���")) {
            strPath.append("title_c_gyoumu.jpg");
        } else if (contentsTitle.equals("��������")) {
            strPath.append("title_a_kokyaku_raiten.jpg");
        } else if (contentsTitle.equals("�N���X����")) {
            strPath.append("title_a_kokyaku_cross.jpg");
        } else if (contentsTitle.equals("�N���X����5")) {
            strPath.append("title_a_kokyaku_cross5.jpg");
        } else if (contentsTitle.equals("�T�C�N������")) {
            strPath.append("title_a_karte_analitic.jpg");
        } else if (contentsTitle.equals("�q����蔭���V�[�g��")) {
            strPath.append("title_a_kokyaku_problem.jpg");
        } else if (contentsTitle.equals("����W�v")) {
            strPath.append("title_a_uriage_nitigi.jpg");
        } else if (contentsTitle.equals("���ԑѕ���")) {
            strPath.append("title_analitic_time.jpg");
        } else if (contentsTitle.equals("�S���ʐ��шꗗ�\")) {
            strPath.append("title_a_staff_list.jpg");
        } else if (contentsTitle.equals("�S���ʋZ�p����")) {
            strPath.append("title_a_staff_skill.jpg");
        } else if (contentsTitle.equals("�S���ʏ��i����")) {
            strPath.append("title_a_staff_goods.jpg");
        } else if (contentsTitle.equals("�S���ʌڋq����")) {
            strPath.append("title_a_staff_customer.jpg");
        } else if (contentsTitle.equals("�S���ʍė�����")) {
            strPath.append("title_a_staff_repeater.jpg");
        } else if (contentsTitle.equals("�ė�����")) {
            strPath.append("title_b_analitic_legersheet-return-analysis.jpg");
        } else if (contentsTitle.equals("�����L���O")) {
            strPath.append("title_a_ranking.jpg");
        } else if (contentsTitle.equals("���X�|���X����")) {
            strPath.append("title_a_hankyo.jpg");
        } else if (contentsTitle.equals("��������")) {
            strPath.append("title_p_jyoken.jpg");
        } else if (contentsTitle.equals("DM�쐬����")) {
            strPath.append("title_dm_history.jpg");
        } else if (contentsTitle.equals("���[���쐬")) {
            strPath.append("title_p_jyoken_mail.jpg");
        } else if (contentsTitle.equals("�n�K�L�쐬")) {
            strPath.append("title_p_jyoken_hagaki.jpg");
        } else if (contentsTitle.equals("�������x���쐬")) {
            strPath.append("title_p_jyoken_takku.jpg");
        } else if (contentsTitle.equals("��Џ��o�^")) {
            strPath.append("title_b_c_company.jpg");
        } else if (contentsTitle.equals("�O���[�v���o�^")) {
            strPath.append("title_b_c_group.jpg");
        } else if (contentsTitle.equals("�X�܏��o�^")) {
            strPath.append("title_b_c_shop.jpg");
        } else if (contentsTitle.equals("�����o�^")) {
            strPath.append("title_b_c_kengen.jpg");
        } else if (contentsTitle.equals("�X�^�b�t�敪�o�^")) {
            strPath.append("title_b_c_staff_kubun.jpg");
        } else if (contentsTitle.equals("�X�^�b�t���o�^")) {
            strPath.append("title_b_c_staff.jpg");
        } else if (contentsTitle.equals("�o�ދ΃p�X���[�h�ύX")) {
            strPath.append("title_b_c_password.jpg");
        } else if (contentsTitle.equals("�{�p��o�^")) {
            strPath.append("title_b_c_bed.jpg");
        } else if (contentsTitle.equals("�������ړo�^")) {
            strPath.append("title_b_c_res.jpg");
        } else if (contentsTitle.equals("��{�V�t�g�o�^")) {
            strPath.append("title_b_k_shift.jpg");
        } else if (contentsTitle.equals("�X�^�b�t�V�t�g�o�^")) {
            strPath.append("title_b_k_staff_shift.jpg");
        } else if (contentsTitle.equals("���i���ޓo�^")) {
            strPath.append("title_b_products_syohin.jpg");
        } else if (contentsTitle.equals("���i�o�^")) {
            strPath.append("title_b_products_shohin2.jpg");
        } else if (contentsTitle.equals("�g�p���i�o�^")) {
            strPath.append("title_b_products_shiyo.jpg");
        } else if (contentsTitle.equals("�d����o�^")) {
            strPath.append("title_b_products_shiire.jpg");
        } else if (contentsTitle.equals("�݌ɒ����敪�o�^")) {
            strPath.append("title_b_products_shiire_chosei.jpg");
        } else if (contentsTitle.equals("�Z�p���ޓo�^")) {
            strPath.append("title_b_tech_gijyutsu_bunrui.jpg");
        } else if (contentsTitle.equals("�Z�p�o�^")) {
            strPath.append("title_b_tech_gijyutsu.jpg");
        } else if (contentsTitle.equals("���}�X�^�o�^")) {
            strPath.append("title_b_tech_anbun_mst.jpg");
        } else if (contentsTitle.equals("���o�^")) {
            strPath.append("title_b_tech_anbun.jpg");
        } else if (contentsTitle.equals("�g�p�Z�p�o�^")) {
            strPath.append("title_b_tech_siyou.jpg");
        } else if (contentsTitle.equals("�R�[�X�_�񕪗ޓo�^")) {
            strPath.append("title_b_course_class.jpg");
        } else if (contentsTitle.equals("�R�[�X�o�^")) {
            strPath.append("title_b_course.jpg");
        } else if (contentsTitle.equals("�g�p�R�[�X�o�^")) {
            strPath.append("title_b_course_use.jpg");
		//�}�X�^�ꊇ�o�^ add start 2016/12/28
        } else if (contentsTitle.equals("�Z�p�ꊇ�o�^")) {
            strPath.append("title_technic_regist_bulk.jpg");
		//�}�X�^�ꊇ�o�^ add end 2016/12/28
        } else if (contentsTitle.equals("�X�^�b�t�{�p���ԓo�^")) {
            strPath.append("title_b_tech_staff.jpg");
        } else if (contentsTitle.equals("�ڋq���o�^")) {
            strPath.append("title_b_costmer_kokyaku.jpg");
        } else if (contentsTitle.equals("�����ꗗ")) {
            strPath.append("title_b_custmer_dis_kaiin.jpg");
            //LUC start add �ڋq�������� 20121023
        } else if (contentsTitle.equals("�ڋq��������")) {
            strPath.append("title_b_kokyaku_area.jpg");
        } //LUC end add �ڋq�������� 20121023
        //LUC start add �G���A���� 20121023
        else if (contentsTitle.equals("�G���A����")) {
            strPath.append("title_analitic_customerarea.jpg");
        } //LUC end add �G���A���� 20121023
        else if (contentsTitle.equals("�X�ܕ���")) {
            strPath.append("title_b_analitic_shop.jpg");
        } else if (contentsTitle.equals(
                "�P�[�^�C����ꗗ")) {
            strPath.append("title_b_mobile_cust_list.jpg");
        } else if (contentsTitle.equals(
                "�E�Ɠo�^")) {
            strPath.append("title_b_costmer_syokugyo.jpg");
        } else if (contentsTitle.equals(
                "�t���[���ڋ敪�o�^")) {
            strPath.append("title_b_costmer_free_kubun.jpg");
        } else if (contentsTitle.equals(
                "�t���[���ړo�^")) {
            strPath.append("title_b_costmer_free.jpg");
        } else if (contentsTitle.equals(
                "����œo�^")) {
            strPath.append("title_b_account_tax.jpg");
        } else if (contentsTitle.equals(
                "�x�����@�o�^")) {
            strPath.append("title_b_account_shiharai.jpg");
        } else if (contentsTitle.equals(
                "������ʓo�^")) {
            strPath.append("title_b_account_waribiki.jpg");
        } else if (contentsTitle.equals(
                "���V�[�g�ݒ�")) {
            strPath.append("title_b_account_receipt.jpg");
        } else if (contentsTitle.equals(
                "�e���v���[�g���ޓo�^")) {
            strPath.append("title_b_mail_template_bunrui.jpg");
        } else if (contentsTitle.equals(
                "�e���v���[�g�o�^")) {
            strPath.append("title_b_mail_template.jpg");
        } else if (contentsTitle.equals(
                "�����o�^")) {
            strPath.append("title_b_mail_syomei.jpg");
        } else if (contentsTitle.equals(
                "CSV�C���|�[�g")) {
            strPath.append("title_b_csv_in.jpg");
        } else if (contentsTitle.equals(
                "CSV�G�N�X�|�[�g")) {
            strPath.append("title_b_exp.jpg");
        } else if (contentsTitle.equals(
                "���ݒ�")) {
            strPath.append("title_b_kankyo.jpg");
        } else if (contentsTitle.equals(
                "���񗈓X���@�o�^")) {
            strPath.append("title_b_costmer_f-motive.jpg");
        } else if (contentsTitle.equals(
                "�ڋq�����N�ݒ�")) {
            strPath.append("title_b_customer_rank.jpg");
        } else if (contentsTitle.equals(
                "�J���e���ޓo�^")) {
            strPath.append("title_b_costmer_grouping-chart.jpg");
        } else if (contentsTitle.equals(
                "�J���e�ڍדo�^")) {
            strPath.append("title_b_costmer_detail-chart.jpg");
        } else if (contentsTitle.equals(
                "�J���e�Q�Ɠo�^")) {
            strPath.append("title_b_costmer_ref[1].item-chart.jpg");
        } else if (contentsTitle.equals(
                "�u��o�^")) {
            strPath.append("title_b_products_locater_entry.jpg");
        } else if (contentsTitle.equals(
                "�|�C���g�v�Z���o�^")) {
            strPath.append("title_b_card_count-setup.jpg");
        } else if (contentsTitle.equals(
                "�J�[�h�e���v���[�g�o�^")) {
            strPath.append("title_b_card_template.jpg");
        } else if (contentsTitle.equals(
                "�����ݒ���")) {
            strPath.append("title_b_default.jpg");
        } else if (contentsTitle.equals(
                "���ύޗ��䗦(TOM)")) {
            strPath.append("title_b_marcherdise_ledgersheet-custom_ingredients-rate-years.jpg");
        } else if (contentsTitle.equals(
                "���v�I���\")) {
            strPath.append("title_b_marcherdise_ledgersheet_stock-take-totallist.jpg");
        } else if (contentsTitle.equals(
                "���v�I���\(TOM)")) {
            strPath.append("title_b_marcherdise_ledgersheet-custom_stock-take-totallist.jpg");
        } else if (contentsTitle.equals(
                "�ޗ��䗦(TOM)")) {
            strPath.append("title_b_marcherdise_ledgersheet-custom_stock-totallist.jpg");
        } else if (contentsTitle.equals(
                "���o�Ɉꗗ�\")) {
            strPath.append("title_b_marcherdise_ledgersheet_list-input-output.jpg");
        } else if (contentsTitle.equals(
                "�u��ʍ݌ɕ[")) {
            strPath.append("title_b_marcherdise_ledgersheet_locaterlist.jpg");
        } else if (contentsTitle.equals(
                "�ԕi�ꗗ�\")) {
            strPath.append("title_b_marcherdise_ledgersheet_return-list.jpg");
        } else if (contentsTitle.equals(
                "�X�^�b�t�̔�����")) {
            strPath.append("title_b_marcherdise_ledgersheet_staff-sales-check.jpg");
        } else if (contentsTitle.equals(
                "�I���\")) {
            strPath.append("title_b_marcherdise_ledgersheet_stock-list.jpg");
        } else if (contentsTitle.equals(
                "�������쐬")) {
            strPath.append("title_b_marcherdise_order-drawup.jpg");
        } else if (contentsTitle.equals(
                "���ɓ`�[�쐬")) {
            strPath.append("title_b_marcherdise_in-check.jpg");
        } else if (contentsTitle.equals(
                "�o�ɓ`�[�쐬")) {
            strPath.append("title_b_marcherdise_out-check.jpg");
        } else if (contentsTitle.equals(
                "�X�^�b�t�̔�����")) {
            strPath.append("title_b_marcherdise_staff-sales_career.jpg");
        } else if (contentsTitle.equals(
                "�X�^�b�t�̔�����")) {
            strPath.append("title_b_marcherdise_staff-sales_input.jpg");
        } else if (contentsTitle.equals(
                "�I��")) {
            strPath.append("title_b_marcherdise_stock-take.jpg");
        } else if (contentsTitle.equals(
                "�X�܊Ԉړ�")) {
            strPath.append("title_b_marcherdise_between-shop.jpg");
        } else if (contentsTitle.equals(
                "���v����")) {
            strPath.append("title_b_analitic_legersheet-custom_daily-totalsales.jpg");
        } else if (contentsTitle.equals(
                "���Ԕ���")) {
            strPath.append("title_b_analitic_legersheet-custom_monthly-totalsales.jpg");
        } else if (contentsTitle.equals(
                "�J�X�^�����[>>�����L���O")) {
            strPath.append("title_b_analitic_legersheet-custom_ranking.jpg");
        } else if (contentsTitle.equals(
                "�J�X�^�����[>>�ė�����")) {
            strPath.append("title_b_analitic_legersheet-custom_return-analysis.jpg");
        } else if (contentsTitle.equals(
                "���㕪��")) {
            strPath.append("title_b_analitic_legersheet-custom_sales-analysis.jpg");
        } else if (contentsTitle.equals(
                "�o�Γo�^")) {
            strPath.append("title_b_legersheet-custom_checkinwork.jpg");
        } else if (contentsTitle.equals(
                "�݌Ɉꗗ�m�F���")) {
            strPath.append("title_b_marcherdise_stock-check.jpg");
        } else if (contentsTitle.equals(
                "����\������")) {
            strPath.append("title_ito_customer_analitics.jpg");
        } else if (contentsTitle.equals(
                "�J���e����")) {
            strPath.append("title_ito_3d_customer_analitics.jpg");
        } else if (contentsTitle.equals(
                "�������[���ݒ�")) {
            strPath.append("title_b_automail_setting.jpg");
        } else if (contentsTitle.equals(
                "�ڋq����")) {
            strPath.append("title_b_customer_integration.jpg");
        } else if (contentsTitle.equals(
                "��r�W�v�\")) {
            strPath.append("title_a_custom_sales_comparison.jpg");
        } else if (contentsTitle.equals(
                "���X�J���e����")) {
            strPath.append("title_a_raiten_karte_report.jpg");
        } //Start add 20131102 lvut (merg rappa ->gb_source)
        else if (contentsTitle.equals("�������ޓo�^")) {
            strPath.append("title_k_response_class.jpg");
        } //End add 20131102 lvut (merg rappa ->gb_source)
        // IVS SANG START INSERT 20131209 [gb�\�[�X]���i��n�m�F���̃}�[�W
        else if (contentsTitle.equals("�ڋq�ʍ݌ɊǗ�")) {
            strPath.append("title_s_custmer_stock.jpg");
        } else if (contentsTitle.equals("���i��z�Ǘ�")) {
            strPath.append("title_s_item_delivery.jpg");
        } // endadd 20130319 nakhoa ���i��z�Ǘ�
        // IVS SANG END INSERT 20131209 [gb�\�[�X]���i��n�m�F���̃}�[�W
        //VTAn Start add 20140328 �ڕW�����с��ғ�����
        else if (contentsTitle.equals("�ڕW�����с��ғ�����")) {
            strPath.append("title_b_Target.jpg");
        } //VTAn End add 20140328 �ڕW�����с��ғ�����
        else if (contentsTitle.equals("�������s�[�g����")) {
            strPath.append("title_a_hankyo_repeat.jpg");
        } else if (contentsTitle.equals("�ڋq�_�񗚗�")) {
            strPath.append("title_a_cus_contract_history.jpg");
        } //VTAN start add ���㐄�ڕ\ 20140422
        else if (contentsTitle.equals("���㐄�ڕ\")) {
            strPath.append("title_a_custom_history.jpg");
        } else if (contentsTitle.equals("���㕪�͕\")) {
            strPath.append("title_a_custom_sales_report.jpg");
        }//VTAN add ���㐄�ڕ\ 20140422
        //VTAN Start add 20140612
        //IVS_LTThuc start add 20140714
        else if (contentsTitle.equals("�Ƒԓo�^")) {
            strPath.append("title_k_category.jpg");
        }
        else if (contentsTitle.equals("�z���X�e�[�^�X�Ǘ�")) {
            strPath.append("title_f_delivery_status.jpg");
        } //VTAN End add 20140612
        //IVS_nahoang GB_Mashu_�ڕW�ݒ� Start add 20141022
        else if (contentsTitle.equals("�ڕW�ݒ�")) {
            strPath.append("title_a_target.jpg");
        } //IVS_nahoang GB_Mashu_�ڕW�ݒ� End add
        // vtbphuong start add 20140708
        else if (contentsTitle.equals("�𖱎c�ꗗ")) {
            strPath.append("title_b_service.jpg");
        }else if (contentsTitle.equals("�L�������ꗗ")) {
            strPath.append("title_b_service_expiration.jpg");
        } else if (contentsTitle.equals("���ꗗ")) {
            strPath.append("title_b_service_cancel.jpg");
        } else if (contentsTitle.equals("���ʎw�W���l����")) {
        	strPath.append("title_form_management.jpg");
        } else if (contentsTitle.equals("���X���R�����L���O")) {
        	strPath.append("title_reasons_rank.jpg");
        } else if (contentsTitle.equals("����؃����L���O")) {
        	strPath.append("title_selling_rank.jpg");
        //nhanvt start edit 20150210 New request #35190
        } else if (contentsTitle.equals("�M�l�X�W�v")) {
        	//strPath.append("title_highest_past_sales.jpg");
            strPath.append("title_a_past_max_record.jpg");
        //nhanvt end edit 20150210 New request #35190
        } else if (contentsTitle.equals("���i�݌Ɉꗗ")) {
        	strPath.append("title_i_item_stock_list.jpg");
        //IVS_LVTu start add 20150109
        } else if (contentsTitle.equals("�Ǝ������N����")) {
        	strPath.append("title_c_rank.jpg");
        }
        //IVS_LVTu end add 20150109
        // vtbphuong end add 20140708
        else if (contentsTitle.equals("�p�X�u�b�N�W�v")) {
        	strPath.append("title_b_passbook.jpg");
        }
         //start add TMTrong on 20150706 New request #38963
        //IVS_LVTu start edit 2015/11/06 New request #44110
        else if(contentsTitle.equals("���i��n�ꗗ")){
            strPath.append("title_s_item_delivery_history.jpg");
        }else if(contentsTitle.equals("�_�񗦏W�v")){
            strPath.append("title_b_contract_rate.jpg");
        }else if(contentsTitle.equals("��ď��ꗗ")){
            strPath.append("title_b_proposal_list.jpg");
        }else if(contentsTitle.equals("�|�C���g�W�v")){
            strPath.append("title_b_analitic_point.jpg");
        }else if(contentsTitle.equals("��ĺ��۰ِݒ�")){
            strPath.append("title_media_setting.jpg");
        }
        //IVS_LVTu end edit 2015/11/06 New request #44110
        //end add TMTrong on 20150706 New request #38963

        //IVS_NHTVINH start add 2016/08/25 New request #54237
        else if(contentsTitle.equals("��ĺ��۰ٓ���")){
            strPath.append("title_media_install.jpg");
        }
        //IVS_NHTVINH end add 2016/08/25 New request #54237

		// 20161227 add start �I���W�i�����[�o�͉��
        else if(contentsTitle.equals("�I���W�i�����[�o��")){
            strPath.append("title_analitic_original_report.jpg");
        }
        // 20161227 add end �I���W�i�����[�o�͉��

        // 20170413 add #61376
        else if(contentsTitle.equals("���X�J���e�ݒ�")) {
            strPath.append("title_visit_karte.jpg");
        }
        //IVS_LVTu start add 2018/13/05 GB_Finc
        else if(contentsTitle.equals("�v�����o�^")) {
            strPath.append("title_monthly_plan.jpg");
        }else if(contentsTitle.equals("������o�^")) {
            strPath.append("title_monthly_customer.jpg");
        }else if(contentsTitle.equals("�ꊇ����")) {
            strPath.append("title_monthly_batch.jpg");
        }else if(contentsTitle.equals("�����U�֘A�g")) {
            strPath.append("title_monthly_relation.jpg");
        }
        //IVS_LVTu end add 2018/13/05 GB_Finc
        //Start add 20220502 TUNE�I���W�i�����[�o�� �̑Ή�
        else if(contentsTitle.equals("�J�X�^�����[")){
            strPath.append("title_b_custom_report.jpg");
        }
        //End add 20220502 TUNE�I���W�i�����[�o�� �̑Ή�

        else {//�擾�o���Ȃ��ꍇ
            strPath.append("");//�摜�͏o�Ȃ�
        }

        return strPath.toString();
    }

    /**
     * �ڋq�o�^�{�^�����擾����B
     *
     * @return �ڋq�o�^�{�^��
     */
    private JButton getRegistCustomerButton() {
        if (registCustomerButton == null) {
            registCustomerButton = this.createImageButton(
                    "�ڋq���o�^",
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
     * �����ꗗ�{�^�����擾����B
     *
     * @return �����ꗗ�{�^��
     */
    private JButton getNotMemberListButton() {
        if (notMemberListButton == null) {
            notMemberListButton = this.createImageButton(
                    "�����ꗗ",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/not_member_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/not_member_on.jpg",
                    null,
                    false);
            /*
             "�����ꗗ",
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
     * �P�[�^�C����ꗗ�{�^�����擾����B
     *
     * @return �P�[�^�C����ꗗ�{�^��
     */
    private JButton getMobileMemberListButton() {
        if (mobileMemberListButton == null) {
            mobileMemberListButton = this.createImageButton(
                    "�P�[�^�C����ꗗ",
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
     * �ė��󋵃{�^�����擾����B
     *
     * @return �ė��󋵃{�^��
     */
    private JButton getRepeaterButton() {
        if (repeaterButton == null) {
            repeaterButton = this.createImageButton(
                    "�ė���",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/analitic_customer_circumstantial_repeat_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/analitic_customer_circumstantial_repeat_on.jpg",
                    null,
                    false);
            /*
             "�ė���",
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
     * �\��o�^�{�^�����擾����B
     *
     * @return �\��o�^�{�^��
     */
    private JButton getRegistReservatioButton() {
        if (registReservatioButton == null) {

            registReservatioButton =
                    this.createImageButton(
                    "�\��",
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
     * �t�����g�{�^�����擾����B
     *
     * @return �t�����g�{�^��
     */
    private JButton getShowStatusButton() {
        if (showStatusButton == null) {

            showStatusButton =
                    this.createImageButton(
                    "�t�����g",
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
                    "�z���X�e�[�^�X�Ǘ�",
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
     * ���W���Z�{�^�����擾����B
     *
     * @return ���W���Z�{�^��
     */
    private JButton getInputAccountButton() {
        if (inputAccountButton == null) {

            inputAccountButton =
                    this.createImageButton(
                    "���W���Z",
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
                        //�ꎞ�ۑ��{�^���g�p�s��
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
     * �`�[�����{�^�����擾����B
     *
     * @return �`�[�����{�^��
     */
    private JButton getSearchAccountButton() {
        if (searchAccountButton == null) {
            searchAccountButton = this.createImageButton(
                    "���Z�`�[����",
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
     * ���|�ꗗ�{�^�����擾����B
     *
     * @return ���|�ꗗ�{�^��
     */
    private JButton getReceivableListButton() {
        if (receivableListButton == null) {
            receivableListButton = this.createImageButton(
                    "���|���",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_recivable_list_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_recivable_list_on.jpg",
                    null,
                    true);
            /*
             "���|�������",
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
     * ���W�Ǘ��{�^�����擾����B
     *
     * @return ���|�ꗗ�{�^��
     */
    private JButton getRegisterButton() {
        if (registerButton == null) {
            registerButton = this.createImageButton(
                    "���W����",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/closed_casher_management_table_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/closed_casher_management_table_on.jpg",
                    null,
                    true);
            /*
             "���W�Ǘ�",
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
     * ���W���o���{�^�����擾����B
     *
     * @return ���W���o���{�^��
     */
    private JButton getRegisterCashButton() {
        if (registerCashButton == null) {
            registerCashButton = this.createImageButton(
                    "���W�o�[��",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_register_cash_io_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/account/casher_register_cash_io_on.jpg",
                    null,
                    true);
            /*
             "���W���o��",
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
     * @return �X�^�b�t���у{�^��
     */
    private JButton getCustomButton() {
        if (customButton == null) {
            customButton = this.createImageButton(
                    "�X�^�b�t����",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/merchandise_leger_sheet_custom_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/merchandise_leger_sheet_custom_on.jpg",
                    null,
                    true);
            /*
             "�X�^�b�t����",
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
     * 20161227 add �I���W�i�����[�o�͉��
     *
     * @return �I���W�i�����[�o�͉�ʃ{�^��
     */
    private JButton getOriginalReportButton() {
        if (customButton == null) {
            customButton = this.createImageButton(
                    "�I���W�i�����[",
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
     * �Ɩ��񍐃{�^�����擾����B
     *
     * @return �Ɩ��񍐃{�^��
     */
    private JButton getBusinessReportButton() {
        if (businessReportButton == null) {
            businessReportButton = this.createImageButton(
                    "�Ɩ���",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/closed_bisiness_report_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/closed_bisiness_report_on.jpg",
                    null,
                    true);
            /*
             "�Ɩ���",
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
	 * [���f�X�e�B�J�X�^�}�C�Y]
     * �J�X�^�����[�{�^�����擾����B
     *
     * @return �J�X�^�����[�{�^��
     */
    private JButton getCustomCutoffReportButton() {
        if (customCutoffReportButton == null ) {
            customCutoffReportButton = this.createImageButton(
                    "�J�X�^�����[",
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
     * ����W�v�{�^�����擾����B
     *
     * @return ����W�v�{�^��
     */
    private JButton getSalesTotalButton() {
        if (salesTotalButton == null) {
            salesTotalButton = this.createImageButton(
                    "��������",
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
    /* �p�X�u�b�N�W�v
    */

    private JButton getPassBookButton() {
        if (passBookButton == null) {
            passBookButton = this.createImageButton(
                    "�p�X�u�b�N�W�v",
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
     * ���ԑѕ��̓{�^�����擾����B
     *
     * @return ���ԑѕ��̓{�^��
     */
    private JButton getTimeAnalysisButton() {
        if (timeAnalysisButton == null) {
            timeAnalysisButton = this.createImageButton(
                    "���ԑѕ���",
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
     * @return �S���ʐ��шꗗ�\�{�^��
     */
    private JButton getSaleTransittionButton() {
        if (saleTransittionButton == null) {
            saleTransittionButton = this.createImageButton(
                    "���㐄�ڕ\",
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
    /* �X�^�b�t���у{�^�����擾����B
     * @return �X�^�b�t���у{�^��
     */

    private JButton getStaffResultButton() {
        if (staffResultButton == null) {
            staffResultButton = this.createImageButton(
                    "�X�^�b�t����",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/analitic_staff_grades_on.jpg",
                    null,
                    true);
            /*
             "�X�^�b�t����",
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
     * ����W�v�{�^�����擾����B
     *
     * @return ����W�v�{�^��
     */
    private JButton getReappearanceButton() {
        if (reappearanceButton == null) {
            reappearanceButton = this.createImageButton(
                    "�ė�����",
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
     * �ė����̓{�^�����擾����B
     *
     * @return ����W�v�{�^��
     */
    private JButton getReappearancePredictionButton() {
        if (reappearancePredictionButton == null) {
            reappearancePredictionButton = this.createImageButton(
                    "��������",
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
     * �S���ʐ��шꗗ�\�{�^�����擾����B
     *
     * @return �S���ʐ��шꗗ�\�{�^��
     */
    private JButton getStaffResultListButton() {
        if (staffResultListButton == null) {
            staffResultListButton = this.createImageButton(
                    "�S���ʐ��шꗗ�\",
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
     * �S���ʋZ�p���у{�^�����擾����B
     *
     * @return �S���ʋZ�p���у{�^��
     */
    private JButton getStaffResultTechButton() {
        if (staffResultTechButton == null) {
            staffResultTechButton = this.createImageButton(
                    "�S���ʋZ�p����",
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
     * �S���ʏ��i���у{�^�����擾����B
     *
     * @return �S���ʏ��i���у{�^��
     */
    private JButton getStaffResultGoodsButton() {
        if (staffResultGoodsButton == null) {
            staffResultGoodsButton = this.createImageButton(
                    "�S���ʏ��i����",
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
     * �S���ʌڋq���у{�^�����擾����B
     *
     * @return �S���ʌڋq���у{�^��
     */
    private JButton getStaffResultCustomerButton() {
        if (staffResultCustomerButton == null) {
            staffResultCustomerButton = this.createImageButton(
                    "�S���ʌڋq����",
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
     * �S���ʍė����у{�^�����擾����B
     *
     * @return �S���ʍė����у{�^��
     */
    private JButton getStaffResultRepeatButton() {
        if (staffResultRepeatButton == null) {
            staffResultRepeatButton = this.createImageButton(
                    "�S���ʍė�����",
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
     * �����L���O�{�^�����擾����B
     *
     * @return �����L���O
     */
    private JButton getRankingButton() {
        if (rankingButton == null) {
            rankingButton = this.createImageButton(
                    "�����L���O",
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
     * ���X�|���X���̓{�^�����擾����B
     *
     * @return ���X�|���X����
     */
    private JButton getResponseEffectButton() {
        if (responseEffectButton == null) {
            responseEffectButton = this.createImageButton(
                    "���X�|���X����",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_on.jpg",
                    null,
                    false);
            /*
             "���X�|���X����",
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
     * �������s�[�g���̓{�^�����擾����B
     *
     * @return ���X�|���X���� getResponseRepeatAnalyzetButton
     */
    private JButton getResponseRepeatAnalyzetButton() {
        if (responseRepeatAnalyzetButton == null) {
            responseRepeatAnalyzetButton = this.createImageButton(
                    "�������s�[�g����",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_repeat_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/response_repeat_on.jpg",
                    null,
                    false);
            /*
             "���X�|���X����",
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
     * ���Ɓ[���񕪐� ����\�����̓{�^�����擾����B
     *
     * @return ����\�����̓{�^��
     */
    private JButton getItoCustomerProblemSheetButton() {
        if (itoCustomerProblemSheetButton == null) {
            itoCustomerProblemSheetButton = this.createImageButton(
                    "����\������",
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
     * �J���e���̓{�^�����擾����B
     *
     * @return �J���e���̓{�^��
     */
    private JButton getItoAnalitic3jigenButton() {
        if (itoAnalitic3jigenButton == null) {
            itoAnalitic3jigenButton = this.createImageButton(
                    "�J���e����",
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
     * ���W���o���{�^�����擾����B
     *
     * @return ���W���o���{�^��
     */
    private JButton getRegisterCashIOButton() {
        if (registerCashIOButton == null) {
            registerCashIOButton = this.createImageButton(
                    "���ԃ��W�o�[��",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/casher_register_cash_io_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/report/casher_register_cash_io_on.jpg",
                    null,
                    true);
            /*
             "���W���o���\",
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
     * ���������{�^�����擾����B
     *
     * @return ���������{�^��
     */
    private JButton getBaseCashButton() {
        if (baseCashButton == null) {
            baseCashButton = this.createImageButton(
                    "��������",
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
     * �����������j���[���擾����B
     *
     * @return �����������j���[
     */
    public JPanel getBaseCashMenu() {
        if (baseCashMenu == null) {
            baseCashMenu = this.createSubMenu();

            //�������ړo�^
            this.addSubMenu(baseCashMenu, this.getCashClassButton(), -1);

            //�����ڍדo�^
            this.addSubMenu(baseCashMenu, this.getCashMenuButton(), -1);

            //���������Ǘ�
            this.addSubMenu(baseCashMenu, this.getCashRegistButton(), -1);

        }

        return baseCashMenu;
    }

    /**
     * �������ړo�^�{�^�����擾����B
     *
     * @return �������ړo�^�{�^��
     */
    public JButton getCashClassButton() {
        if (cashClassButton == null) {
            cashClassButton = this.createImageButton(
                    "�������ړo�^",
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
     * �����ڍדo�^�{�^�����擾����B
     *
     * @return �����ڍדo�^�{�^��
     */
    public JButton getCashMenuButton() {
        if (cashMenuButton == null) {
            cashMenuButton = this.createImageButton(
                    "�����ڍדo�^",
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
                                MessageUtil.getMessage(7004, "��������"),
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
     * ���������Ǘ��{�^�����擾����B
     *
     * @return ���������Ǘ��{�^��
     */
    public JButton getCashRegistButton() {
        if (cashRegistButton == null) {
            cashRegistButton = this.createImageButton(
                    "���������Ǘ�",
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
     * ���������{�^�����擾����B
     *
     * @return ���������{�^��
     */
    private JButton getConditionedSearchButton() {
        if (conditionedSearchButton == null) {
            conditionedSearchButton = this.createImageButton(
                    "��������",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/promotion_condition_search_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/promotion_condition_search_on.jpg",
                    null,
                    true);
            /*
             "��������",
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
     * DM�쐬�����{�^�����擾����B
     *
     * @return DM�쐬�����{�^��
     */
    private JButton getDmHistoryButton() {
        if (dmHistoryButton == null) {
            dmHistoryButton = this.createImageButton(
                    "DM�쐬����",
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
     * ���ʎw�W���l���̓{�^�����擾����B
     *
     * @return ���ʎw�W���l���̓{�^��
     */
    private JButton getEffectIndicatorAnalysisButton() {
        if (effectIndicatorAnalysisButton == null) {
        	effectIndicatorAnalysisButton = this.createImageButton(
                    "���ʎw�W���l����",
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
     * ���X���R�����L���O�{�^�����擾����B
     *
     * @return ���X���R�����L���O�{�^��
     */
    private JButton getReasonsRankButton() {
        if (reasonsRankButton == null) {
        	reasonsRankButton = this.createImageButton(
                    "���ʎw�W���l����",
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
     * ����؃����L���O�{�^�����擾����B
     *
     * @return ����؃����L���O�{�^��
     */
    private JButton getItemSalesRankingButton() {
        if (itemSalesRankingButton == null) {
        	itemSalesRankingButton = this.createImageButton(
                    "����؃����L���O",
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
     * �ߋ��ő���у{�^�����擾����B
     *
     * @return ����؃����L���O�{�^��
     */
    private JButton getPostMaxResultsButton() {
        if (postMaxResultsButton == null) {
        	postMaxResultsButton = this.createImageButton(
                    "�ߋ��ő����",
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
     * ���j���[�A�o�͉��
     *
     * @return
     */
    private JButton getPointReportButton() {
        if (PointReportButton == null) {
        	PointReportButton = this.createImageButton(
                    "�|�C���g�W�v",
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
     * ���i�݌Ɉꗗ�{�^�����擾����B
     *
     * @return ���i�݌Ɉꗗ�{�^��
     */
    private JButton getItemStockListButton() {
        if (itemStockListButton == null) {
        	itemStockListButton = this.createImageButton(
                    "���i�݌Ɉꗗ",
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
     * �e���v���[�g���ޓo�^�{�^�����擾����B
     *
     * @return �e���v���[�g���ޓo�^�{�^��
     */
    private JButton getTemplateClassButton() {
        if (templateClassButton == null) {
            templateClassButton = this.createImageButton(
                    "�e���v���[�g���ޓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_grouping_template_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_grouping_template_entry_on.jpg",
                    null,
                    false);
            /*
             "�e���v���[�g���ޓo�^",
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
     * �e���v���[�g�o�^�{�^�����擾����B
     *
     * @return �e���v���[�g�o�^�{�^��
     */
    private JButton getTemplateButton() {
        if (templateButton == null) {
            templateButton = this.createImageButton(
                    "�e���v���[�g�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_template_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_template_entry_on.jpg",
                    null,
                    false);
            /*
             "�e���v���[�g�o�^",
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
     * �����o�^�{�^�����擾����B
     *
     * @return �����o�^�{�^��
     */
    private JButton getSignatureButton() {
        if (signatureButton == null) {
            signatureButton = this.createImageButton(
                    "�����o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_sign_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/mail/base_promotion_sign_entry_on.jpg",
                    null,
                    false);
            /*
             "�����o�^",
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
     * ���[���֘A�{�^�����擾����B
     *
     * @return ���[���֘A�{�^��
     */
    public JButton getMailMasterButton() {
        if (mailMasterButton == null) {
            mailMasterButton = this.createImageButton(
                    "���[���֘A",
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
     * ���[���֘A���j���[���擾����B
     *
     * @return ���[���֘A���j���[
     */
    public JPanel getMailMasterMenu() {
        if (mailMasterMenu == null) {
            mailMasterMenu = this.createSubMenu();

            //�e���v���[�g���ޓo�^
            this.addSubMenu(mailMasterMenu, this.getTemplateClassButton(), 51);

            //�e���v���[�g�o�^
            this.addSubMenu(mailMasterMenu, this.getTemplateButton(), 52);

            //�����o�^
            this.addSubMenu(mailMasterMenu, this.getSignatureButton(), 50);

            // �������[���ݒ���
            this.addSubMenu(mailMasterMenu, this.getImageButton(autoMailButton, "/menu/mail/automail_setting", "�������[���ݒ�",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            changeContents(new MstAutoMailSettingPanel());
                        }
                    }), -1);

        }

        return mailMasterMenu;
    }
    // IVS SANG START INSERT 20131211 [gb�\�[�X]���i��n�m�F���̃}�[�W

    private JButton getProductDeliveryManagementButton() {
        if (ProductDeliveryManagementButton == null) {
            ProductDeliveryManagementButton = this.createImageButton(
                    "���i��z�Ǘ�",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/item_delivery_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/customer/item_delivery_on.jpg",
                    null,
                    false);
            /*
             "�e���v���[�g���ޓo�^",
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
    // IVS SANG END INSERT 20131211 [gb�\�[�X]���i��n�m�F���̃}�[�W
//add end

    /**
     * ��Ѓ}�X�^�{�^�����擾����B
     *
     * @return ��Ѓ}�X�^�{�^��
     */
    public JButton getCompanyMasterButton() {
        if (companyMasterButton == null) {
            companyMasterButton = this.createImageButton(
                    "��Њ֘A",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_company_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_company_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_company_on.jpg",
                    true);
            /*
             "��Ѓ}�X�^",
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
     * ���i�}�X�^�{�^�����擾����B
     *
     * @return ���i�}�X�^�{�^��
     */
    public JButton getItemMasterButton() {
        if (itemMasterButton == null) {
            itemMasterButton = this.createImageButton(
                    "���i�֘A",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_goods_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_goods_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_goods_on.jpg",
                    true);
            /*
             "���i�}�X�^",
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
     * ������Ǘ�
     *
     * @return ������Ǘ��{�^��
     */
    public JButton getManageMemberMonButton() {
        if (manageMemberMonButton == null) {
            manageMemberMonButton = this.createImageButton(
                    "������Ǘ�",
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
                    "��ĺ��۰ٓ���",
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

    //IVS_nahoang GB_Mashu_�ڕW�ݒ� Start add 20141021
    private JButton targetSettingButton = null;

    public JButton getItemTargetSettingButton() {
        if (targetSettingButton == null) {
            targetSettingButton = this.createImageButton(
                    "�ڕW�ݒ�",
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

    //IVS_nahoang GB_Mashu_�ڕW�ݒ� End add

    /**
     * �Z�p�}�X�^�{�^�����擾����B
     *
     * @return �Z�p�}�X�^�{�^��
     */
    public JButton getTechnicMasterButton() {
        if (technicMasterButton == null) {
            technicMasterButton = this.createImageButton(
                    "�Z�p�֘A",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_technic_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_technic_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_technic_on.jpg",
                    true);
            /*
             "�Z�p�}�X�^",
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
     * �ڋq���͂��擾����B
     *
     * @return �ڋq���̓{�^��
     */
    public JButton getCustomerEffectButton() {
        if (customerEffectButton == null) {
            customerEffectButton = this.createImageButton(
                    "�ڋq����",
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
     * ���㕪�͂��擾����B
     *
     * @return ���㕪�̓{�^��
     */
    public JButton getSalesEffectButton() {
        if (salesEffectButton == null) {
            salesEffectButton = this.createImageButton(
                    "���㕪��",
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
     * �������͂��擾����B
     *
     * @return �������̓{�^��
     */
    public JButton getRepeatAnalysisButton() {
        if (repeatAnalysisButton == null) {
            repeatAnalysisButton = this.createImageButton(
                    "��������",
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

    //Luc add start 20121004 �X�ܕ��̓{�^��
    public JButton getStoreButton() {
        if (storeButton == null) {
            storeButton = this.createImageButton(
                    "�X�ܕ���",
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
    //Luc add End 20121004 �X�ܕ��̓{�^��

    /**
     *
     * �ė����͂��擾����B
     *
     * @return ���㕪�̓{�^��
     */
    public JButton getReappearanceEffectButton() {
        if (reappearanceEffectButton == null) {
            reappearanceEffectButton = this.createImageButton(
                    "�ė�����",
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
     * �������͂��擾����B
     *
     * @return �������̓{�^��
     */
    public JButton getReappearancePredictionEffectButton() {
        if (reappearancePredictionEffectButton == null) {
            reappearancePredictionEffectButton = this.createImageButton(
                    "��������",
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
     * �N���X���͂��擾����B
     *
     * @return �N���X���̓{�^��
     */
    public JButton getCrossAnalysisEffectButton() {
        if (crossAnalysisEffectButton == null) {
            crossAnalysisEffectButton = this.createImageButton(
                    "�N���X����",
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
     * �N���X����5���擾����B
     *
     * @return �N���X����5�{�^��
     */
    public JButton getCrossAnalysis5EffectButton() {
        if (crossAnalysis5EffectButton == null) {
            crossAnalysis5EffectButton = this.createImageButton(
                    "�N���X����5",
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

    //GB_Mashu Task #34581 [Product][Code][Phase3]�}�b�V���[����
    //IVS_nahoang start add 20141229
    private JButton masherAnalysis5PanelButton = null;
    public JButton getMasherAnalysis5PanelButton() {
        if (masherAnalysis5PanelButton  == null) {
            masherAnalysis5PanelButton  = this.createImageButton(
                    "�}�b�V���[����",
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
    // Luc add start 20121004 �ڋq��������
    public JButton getCustomerAnalysisAttributeButton() {
        if (customerAtrributeAnalysisButton == null) {
            customerAtrributeAnalysisButton = this.createImageButton(
                    "�ڋq��������",
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
    //Luc add End 20121004 �ڋq��������

    //Luc add End 20121004 �G���A����
    public JButton getCustomerAreaAnalysisButton() {
        if (customerAreaAnalysisButton == null) {
            customerAreaAnalysisButton = this.createImageButton(
                    "�G���A����",
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
    //Luc add End 20121004 �G���A����

    /**
     * �T�C�N�����͂��擾����B
     *
     * @return �T�C�N�����̓{�^��
     */
    public JButton getKarteAnalysisEffectButton() {
        if (karteAnalysisEffectButton == null) {
            karteAnalysisEffectButton = this.createImageButton(
                    "�T�C�N������",
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
     * �q����蔭���V�[�g�����擾����B
     *
     * @return �q����蔭���V�[�g���{�^��
     */
    public JButton getCustomerProblemSheetButton() {
        if (customerProblemSheetButton == null) {
            customerProblemSheetButton = this.createImageButton(
                    "�q����蔭���V�[�g��",
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
     * �ڋq�}�X�^�{�^�����擾����B
     *
     * @return �ڋq�}�X�^�{�^��
     */
    public JButton getCustomerMasterButton() {
        if (customerMasterButton == null) {
            customerMasterButton = this.createImageButton(
                    "�ڋq�֘A",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_customer_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_customer_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_customer_on.jpg",
                    true);
            /*
             "�ڋq�}�X�^",
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
     * ���Z�}�X�^�{�^�����擾����B
     *
     * @return ���Z�}�X�^�{�^��
     */
    public JButton getAccountMasterButton() {
        if (accountMasterButton == null) {
            accountMasterButton = this.createImageButton(
                    "���Z�֘A",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_account_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_account_on.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_account_on.jpg",
                    true);
            /*
             "���Z�}�X�^",
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
     * CSV�C���|�[�g�{�^�����擾����B
     *
     * @return CSV�C���|�[�g�{�^��
     */
    public JButton getCsvImportButton() {
        if (csvImportButton == null) {
            csvImportButton = this.createImageButton(
                    "CSV�C���|�[�g",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_import_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_import_on.jpg",
                    null,
                    true);
            /*
             "CSV�C���|�[�g",
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
     * CSV�G�N�X�|�[�g�{�^�����擾����B
     *
     * @return CSV�A�E�g�|�[�g�{�^��
     */
    public JButton getCsvOutportButton() {
        if (csvOutportButton == null) {
            csvOutportButton = this.createImageButton(
                    "CSV�G�N�X�|�[�g",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_export_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_csv_export_on.jpg",
                    null,
                    true);
            /*
             "CSV�G�N�X�|�[�g",
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
     * ���ݒ�{�^�����擾����B
     *
     * @return ���ݒ�{�^��
     */
    public JButton getSettingButton() {
        if (settingButton == null) {
            settingButton = this.createImageButton(
                    "���ݒ�",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_environmental_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/base_environmental_setting_on.jpg",
                    null,
                    true);
            /*
             "���ݒ�",
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
                    "��ĺ��۰ِݒ�",
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
     * ��Ѓ}�X�^���j���[���擾����B
     *
     * @return ��Ѓ}�X�^���j���[
     */
    public JPanel getCompanyMasterMenu() {
        if (companyMasterMenu == null) {
            companyMasterMenu = this.createSubMenu();

            //��Џ��o�^
            if ((SystemInfo.getGroup().getGroupID() == 1 && SystemInfo.getCurrentShop().getShopID() == 0) || (SystemInfo.checkAuthority(0))) {
                this.addSubMenu(companyMasterMenu, this.getMstCompanyButton(), -1);
            }

            //�O���[�v�o�^
            if (SystemInfo.isGroup()) {
                this.addSubMenu(companyMasterMenu, this.getMstGroupButton(), -1);
            }

            // IVS_LTThuc start add 20140707 MASHU_�Ƒԓo�^
            if(SystemInfo.getCurrentShop().getShopID()==0) {
                this.addSubMenu(companyMasterMenu,this.getMstUseShopCategoryButton(),3);
            }else{
                if(SystemInfo.getCurrentShop().getUseShopCategory()==1){
                      this.addSubMenu(companyMasterMenu,this.getMstUseShopCategoryButton(),3);
                }


            }
            //IVS_LTThuc start end 20140707 MASHU_�Ƒԓo�^

            //�X�܏��o�^
            this.addSubMenu(companyMasterMenu, this.getMstShopButton(), 1);

            //�}�X�^�o�^����
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

            // �o�ދ΃p�X���[�h�ύX
            if (SystemInfo.checkAuthority(3)) {
                //if (!SystemInfo.isReservationOnly()) {
                if (!SystemInfo.getSystemType().equals(1)) {
                    this.addSubMenu(companyMasterMenu, this.getMstWorkTimePassButton(), -1);
                }
            }

            //�X�^�b�t�敪�o�^
            this.addSubMenu(companyMasterMenu, this.getMstStaffClassButton(), 4);

            //�X�^�b�t���o�^
            this.addSubMenu(companyMasterMenu, this.getMstStaffButton(), 5);

            //�{�p��o�^
            if (SystemInfo.getCurrentShop().getShopID() == 0 || SystemInfo.getCurrentShop().isBed()) //�{���A���邢�͎{�p����g�p����X�܂̂ݕ\������
            {
                this.addSubMenu(companyMasterMenu, this.getMstBedButton(), 6);
            }
            //Start add 20131102 lvut (merg rappa-->gb_source)
            //���������o�^�{�^��
            if (SystemInfo.checkAuthority(7)) {
                //if (!SystemInfo.isReservationOnly()) {
                //IVS_ptthu edit 25/04/2016
                //if (!SystemInfo.getSystemType().equals(1)) {
                    this.addSubMenu(companyMasterMenu, this.getMstResponseClassButton(), -1);
                //}
                //IVS_ptthu end edit 25/04/2016

            }
            //End add 20131102 lvut (merg rappa-->gb_source)
            // �������ړo�^�{�^��
            if (SystemInfo.checkAuthority(7)) {
                //if (!SystemInfo.isReservationOnly()) {
                //IVS_ptthu edit 25/04/2016
                //if (!SystemInfo.getSystemType().equals(1)) {
                    this.addSubMenu(companyMasterMenu, this.getMstResponseButton(), -1);
                //}
                //IVS_ptthu end edit 25/04/2016
            }

            // ��{�V�t�g�o�^
            this.addSubMenu(companyMasterMenu, this.getMstBasicShiftButton(), 8);

            // �X�^�b�t�V�t�g�o�^
            this.addSubMenu(companyMasterMenu, this.getMstStaffShiftButton(), 8);

            // 20170413 add ���X�J���e�ݒ� #61376
            if (SystemInfo.getUseVisitKarte()) {
                this.addSubMenu(companyMasterMenu, this.getVisitKarteSettingButton(), -1);
            }
        }

        return companyMasterMenu;
    }

    /**
     * ��Џ��o�^�{�^�����擾����B
     *
     * @return ��Џ��o�^�{�^��
     */
    public JButton getMstCompanyButton() {
        if (mstCompanyButton == null) {
            mstCompanyButton = this.createImageButton(
                    "��Џ��o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_entry_on.jpg",
                    null,
                    false);
            /*
             "��Џ��o�^",
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
     * �O���[�v�o�^�{�^�����擾����B
     *
     * @return �O���[�v�o�^�{�^��
     */
    public JButton getMstGroupButton() {
        if (mstGroupButton == null) {
            mstGroupButton = this.createImageButton(
                    "�O���[�v���o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_group_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_group_entry_on.jpg",
                    null,
                    false);
            /*
             "�O���[�v���o�^",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_on.jpg",
             null,
             false);

             */
            mstGroupButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstGroupButton.getModel().setRollover(false);
                    MstGroupPanel mgp = new MstGroupPanel();
                    mgp.setTitle("�O���[�v���o�^");
                    changeContents(mgp);
                }
            });
        }

        return mstGroupButton;
    }
    //IVS_LTThuc start add 20140707 MASHU_�Ƒԓo�^
     public JButton getMstUseShopCategoryButton() {
        if (mstUseShopCategoryButton == null) {
            mstUseShopCategoryButton = this.createImageButton(
                    "�Ƒԓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/category_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/category_on.jpg",
                    null,
                    false);
            /*
             "�O���[�v���o�^",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/group_info_master_on.jpg",
             null,
             false);

             */
            mstUseShopCategoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                     mstUseShopCategoryButton.getModel().setRollover(false);
                     SimpleMasterPanel smp = new SimpleMasterPanel(
                            "�Ƒ�", "mst_shop_category", "shop_category_id",
                            "shop_class_name", 40,
                            SystemInfo.getTableHeaderRenderer());
                     smp.setPath("��{�ݒ� >> ��Њ֘A");

                    changeContents(smp);
                }
            });
        }

        return mstUseShopCategoryButton;
    }
    //IVS_LTThuc start end 20140707 MASHU_�Ƒԓo�^

    /**
     * �}�X�^�o�^�����{�^�����擾����B
     *
     * @return �}�X�^�o�^�����{�^��
     */
    public JButton getMstAuthorityButton() {
        if (mstAuthorityButton == null) {
            mstAuthorityButton = this.createImageButton(
                    "�����o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_conpetence_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_conpetence_entry_on.jpg",
                    null,
                    false);
            /*
             "�}�X�^�o�^����",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/authority_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/authority_master_on.jpg",
             null,
             false);
             */

            mstAuthorityButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    // �p�X���[�h�F��
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

    // Thanh add start 20130415 �ڋq�_�񗚗�
    public JButton getCusContractHistoryButton() {
        if (cusContractHistoryButton == null) {
            cusContractHistoryButton = this.createImageButton(
                    "�ڋq�_�񗚗�",
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
    // Thanh add End 20130415 �ڋq�_�񗚗�

    /**
     * �X�܏��o�^�{�^�����擾����B
     *
     * @return �X�܏��o�^�{�^��
     */
    public JButton getMstShopButton() {
        if (mstShopButton == null) {
            mstShopButton = this.createImageButton(
                    "�X�܏��o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_shop_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_shop_entry_on.jpg",
                    null,
                    false);
            /*
             "�X�܏��o�^",
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
     * �X�^�b�t�敪�o�^�{�^�����擾����B
     *
     * @return �X�^�b�t�敪�o�^�{�^��
     */
    public JButton getMstStaffClassButton() {
        if (mstStaffClassButton == null) {
            mstStaffClassButton = this.createImageButton(
                    "�X�^�b�t�敪�o�^",
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
     * �X�^�b�t���o�^�{�^�����擾����B
     *
     * @return �X�^�b�t���o�^�{�^��
     */
    public JButton getMstStaffButton() {
        if (mstStaffButton == null) {
            mstStaffButton = this.createImageButton(
                    "�X�^�b�t���o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_staff_intelligence_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_staff_intelligence_entry_on.jpg",
                    null,
                    false);
            /*
             "�X�^�b�t���o�^",
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
     * �o�ދ΃p�X���[�h�ύX�{�^�����擾����B
     *
     * @return �o�ދ΃p�X���[�h�ύX�{�^��
     */
    public JButton getMstWorkTimePassButton() {
        if (mstWorkTimePassButton == null) {
            mstWorkTimePassButton = this.createImageButton(
                    "�o�ދ΃p�X���[�h�ύX",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/change_password_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/change_password_on.jpg",
                    null,
                    false);
            /*
             "�o�ދ΃p�X���[�h�ύX",
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
     * �{�p��o�^�{�^�����擾����B
     *
     * @return �{�p��o�^�{�^��
     */
    public JButton getMstBedButton() {
        if (mstBedButton == null) {
            mstBedButton = this.createImageButton(
                    "�{�p��o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_bed_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_bed_entry_on.jpg",
                    null,
                    false);
            /*
             "�{�p��o�^",
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
     * �������ړo�^�{�^�����擾����B
     *
     * @return �������ړo�^�{�^��
     */
    public JButton getMstResponseClassButton() {
        if (mstResponseClassButton == null) {
            mstResponseClassButton = this.createImageButton(
                    "�������ޓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/regist_response_class_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/regist_response_class_on.jpg",
                    null,
                    false);

            mstResponseClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstResponseClassButton.getModel().setRollover(false);
                    MstResponseClassPanel smp = new MstResponseClassPanel(
                            "��������", "mst_response_class",
                            "response_class_id", "response_class_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("��{�ݒ� >> ��Њ֘A");
                    changeContents(smp);
                }
            });
        }

        return mstResponseClassButton;
    }

    //End add 20131102 lvut (merg rappa --> gb_source)
    /**
     * �������ړo�^�{�^�����擾����B
     *
     * @return �������ړo�^�{�^��
     */
    public JButton getMstResponseButton() {

        if (mstResponseButton == null) {
            mstResponseButton = this.createImageButton(
                    "�������ړo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_response_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/base_company_response_entry_on.jpg",
                    null,
                    false);

            mstResponseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstResponseButton.getModel().setRollover(false);
                    MstResponsePanel smp = new MstResponsePanel(
                            "��������", "mst_response",
                            "response_id", "response_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("��{�ݒ� >> ��Ѓ}�X�^");
                    changeContents(smp);
                }
            });
        }

        return mstResponseButton;
    }

    /**
     * ��{�V�t�g�o�^�{�^�����擾����B
     *
     * @return ��{�V�t�g�o�^�{�^��
     */
    public JButton getMstBasicShiftButton() {
        if (mstBasicShiftButton == null) {
            mstBasicShiftButton = this.createImageButton(
                    "��{�V�t�g�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/default_shift_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/default_shift_on.jpg",
                    null,
                    false);

            // �����C�x���g�o�^
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
     * �X�^�b�t�V�t�g�o�^�{�^�����擾����B
     *
     * @return �X�^�b�t�V�t�g�o�^�{�^��
     */
    public JButton getMstStaffShiftButton() {
        if (mstStaffShiftButton == null) {
            mstStaffShiftButton = this.createImageButton(
                    "�X�^�b�t�V�t�g�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/staff_shift_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/company/staff_shift_on.jpg",
                    null,
                    false);

            // �����C�x���g�o�^
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
     * ���i�}�X�^���j���[���擾����B
     *
     * @return ���i�}�X�^���j���[
     */
    public JPanel getItemMasterMenu() {
        if (itemMasterMenu == null) {
            itemMasterMenu = this.createSubMenu();

            //���i���ޓo�^
            this.addSubMenu(itemMasterMenu, this.getMstItemClassButton(), 10);

            //���i�o�^
            this.addSubMenu(itemMasterMenu, this.getMstItemButton(), 11);

            //�g�p���i�o�^
            this.addSubMenu(itemMasterMenu, this.getMstUseItemButton(), 12);

//			//�d����o�^
//			this.addSubMenu(itemMasterMenu, this.getMstSupplierButton(), 13 );

            //�d����o�^
            this.addSubMenu(itemMasterMenu, this.getMstSupplierItemButton(), 13);

            //�݌ɒ����敪�o�^
            this.addSubMenu(itemMasterMenu, this.getMstDestockingDivisionButton(), 14);

            // �u��}�X�^
            this.addSubMenu(itemMasterMenu, this.getMstPlaceButton(), 15);
        }

        return itemMasterMenu;
    }

    /**
     * ���i���ޓo�^�{�^�����擾����B
     *
     * @return ���i���ޓo�^�{�^��
     */
    public JButton getMstItemClassButton() {
        if (mstItemClassButton == null) {
            mstItemClassButton = this.createImageButton(
                    "���i���ޓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_classify_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_classify_entry_on.jpg",
                    null,
                    false);
            /*
             "���i���ޓo�^",
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
     * ���i�o�^�{�^�����擾����B
     *
     * @return ���i�o�^�{�^��
     */
    public JButton getMstItemButton() {
        if (mstItemButton == null) {
            mstItemButton = this.createImageButton(
                    "���i�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_entry_classify_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_entry_classify_on.jpg",
                    null,
                    false);
            /*
             "���i�o�^",
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
                                MessageUtil.getMessage(7004, "���i����"),
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
     * �g�p���i�o�^�{�^�����擾����B
     *
     * @return �g�p���i�o�^�{�^��
     */
    public JButton getMstUseItemButton() {
        if (mstUseItemButton == null) {
            mstUseItemButton = this.createImageButton(
                    "�g�p���i�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_employ_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_employ_entry_on.jpg",
                    null,
                    false);
            /*
             "�g�p���i�o�^",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/use_item_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/use_item_master_on.jpg",
             null,
             false);
             */
            mstUseItemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseItemButton.getModel().setRollover(false);
                    MstUseProductPanel mup = new MstUseProductPanel(2);
                    mup.setTitle("�g�p���i�o�^");
                    changeContents(mup);
                }
            });
        }

        return mstUseItemButton;
    }

    /**
     * �d����o�^�{�^�����擾����B
     *
     * @return �d����o�^�{�^��
     */
    public JButton getMstSupplierButton() {
        if (mstSupplierButton == null) {
            mstSupplierButton = this.createImageButton(
                    "�d����o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_on.jpg",
                    null,
                    false);
            /*
             "�d����o�^",
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
     * �d����o�^�{�^�����擾����B
     *
     * @return �d����o�^�{�^��
     */
    public JButton getMstSupplierItemButton() {
        if (mstSupplierItemButton == null) {
            mstSupplierItemButton = this.createImageButton(
                    "�d����o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_entry_on.jpg",
                    null,
                    false);
            /*
             "�d����o�^",
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
     * �݌ɒ����敪�o�^�{�^�����擾����B
     *
     * @return �݌ɒ����敪�o�^�{�^��
     */
    public JButton getMstDestockingDivisionButton() {
        if (mstDestockingDivisionButton == null) {
            mstDestockingDivisionButton = this.createImageButton(
                    "�݌ɒ����敪�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_adjust-division_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/base_goods_buying_adjust-division_on.jpg",
                    null,
                    false);
            /*
             "�݌ɒ����敪�o�^",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/destocking_division_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/destocking_division_master_on.jpg",
             null,
             false);
             */

            mstDestockingDivisionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstDestockingDivisionButton.getModel().setRollover(false);
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "�݌ɒ����敪", "mst_destocking_division", "destocking_division_id",
                            "destocking_division_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("��{�ݒ� >> ���i�}�X�^");
                    changeContents(smp);
                }
            });
        }

        return mstDestockingDivisionButton;
    }

    /**
     * �Z�p�}�X�^���j���[���擾����B
     *
     * @return �Z�p�}�X�^���j���[
     */
    public JPanel getTechnicMasterMenu() {
        if (technicMasterMenu == null) {
            technicMasterMenu = this.createSubMenu();

            //�Z�p���ޓo�^
            this.addSubMenu(technicMasterMenu, this.getMstTechnicClassButton(), 20);

            //�Z�p�o�^
            this.addSubMenu(technicMasterMenu, this.getMstTechnicButton(), 21);

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                //���}�X�^�o�^
                if (SystemInfo.getCurrentShop().getShopID() == 0 || SystemInfo.getCurrentShop().isProportionally()) //�{���A���邢�͈����g�p����X�܂̂ݕ\������
                {
                    this.addSubMenu(technicMasterMenu, this.getMstProportionallyButton(), 22);
                }

                //�Z�p���o�^
                if (SystemInfo.getCurrentShop().getShopID() == 0 || SystemInfo.getCurrentShop().isProportionally()) //�{���A���邢�͈����g�p����X�܂̂ݕ\������
                {
                    this.addSubMenu(technicMasterMenu, this.getMstTechProportionallyButton(), 22);
                }
            }

            //�g�p�Z�p�o�^
            this.addSubMenu(technicMasterMenu, this.getMstUseTechnicButton(), 23);

            // ���̉�ʂ̐ݒ�l�͂ǂ��ɂ����f����Ă��Ȃ����߈�U���j���[����폜����
            // �X�^�b�t���{�p���ԓo�^
            //this.addSubMenu(technicMasterMenu, this.getMstStaffTechnicTimeButton(), 24 );
            // start edit 20130802 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
            //IVS NNTUAN START EDIT 20131028
            /*
             if(SystemInfo.getCurrentShop().getCourseFlag() != null){
             if(SystemInfo.getCurrentShop().getCourseFlag() == 1){
             //�R�[�X�_�񕪗ޓo�^
             this.addSubMenu(technicMasterMenu, this.getMstCourseClassButton(), 25);

             //�R�[�X�o�^
             this.addSubMenu(technicMasterMenu, this.getMstCourseButton(), 26);

             //�g�p�R�[�X�o�^
             this.addSubMenu(technicMasterMenu, this.getMstUseCourseButton(), 27);
             }
             }*/
            if (SystemInfo.getCurrentShop().getShopID() != 0) {
                if (SystemInfo.getCurrentShop().getCourseFlag() != null) {
                    if (SystemInfo.getCurrentShop().getCourseFlag() == 1) {
                        //�R�[�X�_�񕪗ޓo�^
                        this.addSubMenu(technicMasterMenu, this.getMstCourseClassButton(), 25);

                        //�R�[�X�o�^
                        this.addSubMenu(technicMasterMenu, this.getMstCourseButton(), 26);

                        //�g�p�R�[�X�o�^
                        this.addSubMenu(technicMasterMenu, this.getMstUseCourseButton(), 27);
                    }
                }
            } else {
                //�R�[�X�_�񕪗ޓo�^
                this.addSubMenu(technicMasterMenu, this.getMstCourseClassButton(), 25);

                //�R�[�X�o�^
                this.addSubMenu(technicMasterMenu, this.getMstCourseButton(), 26);

                //�g�p�R�[�X�o�^
                this.addSubMenu(technicMasterMenu, this.getMstUseCourseButton(), 27);

				//�}�X�^�ꊇ�o�^ add start 2016/12/28
				if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
					//�Z�p�ꊇ�o�^
					this.addSubMenu(technicMasterMenu, this.getMstTechnicRegistBulkButton(), 28);
				}
				//�}�X�^�ꊇ�o�^ add end 2016/12/28
            }
            //IVS NNTUAN END EDIT 20131028
            // end edit 20130802 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
        }

        return technicMasterMenu;
    }

    /**
     * �Z�p���ޓo�^�{�^�����擾����B
     *
     * @return �Z�p���ޓo�^�{�^��
     */
    public JButton getMstTechnicClassButton() {
        if (mstTechnicClassButton == null) {
            mstTechnicClassButton = this.createImageButton(
                    "�Z�p���ޓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_classify_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_classify_entry_on.jpg",
                    null,
                    false);
            /*
             "�Z�p���ޓo�^",
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
     * �Z�p�o�^�{�^�����擾����B
     *
     * @return �Z�p�o�^�{�^��
     */
    public JButton getMstTechnicButton() {
        if (mstTechnicButton == null) {
            mstTechnicButton = this.createImageButton(
                    "�Z�p�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_entry_on.jpg",
                    null,
                    false);
            /*
             "�Z�p�o�^",
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
                                MessageUtil.getMessage(7004, "�Z�p����"),
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
     * ���}�X�^�o�^�{�^�����擾����B
     *
     * @return ���}�X�^�o�^�{�^��
     */
    public JButton getMstProportionallyButton() {
        if (mstProportionallyButton == null) {
            mstProportionallyButton = this.createImageButton(
                    "���}�X�^�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_mst_money_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_mst_money_entry_on.jpg",
                    null,
                    false);
            /*
             "���}�X�^�o�^",
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
     * �Z�p���o�^�{�^�����擾����B
     *
     * @return �Z�p���o�^�{�^��
     */
    public JButton getMstTechProportionallyButton() {
        if (mstTechProportionallyButton == null) {
            mstTechProportionallyButton = this.createImageButton(
                    "�Z�p���o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_money_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_divide_money_entry_on.jpg",
                    null,
                    false);
            /*
             "�Z�p���o�^",
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
     * �g�p�Z�p�o�^�{�^�����擾����B
     *
     * @return �g�p�Z�p�o�^�{�^��
     */
    public JButton getMstUseTechnicButton() {
        if (mstUseTechnicButton == null) {
            mstUseTechnicButton = this.createImageButton(
                    "�g�p�Z�p�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_employ_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_employ_entry_on.jpg",
                    null,
                    false);
            /*
             "�g�p�Z�p�o�^",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/use_technic_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/use_technic_master_on.jpg",
             null,
             false);

             */
            mstUseTechnicButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseTechnicButton.getModel().setRollover(false);
                    MstUseProductPanel mup = new MstUseProductPanel(1);
                    mup.setTitle("�g�p�Z�p�o�^");
                    mup.setPath("��{�ݒ� >> �Z�p�}�X�^");
                    changeContents(mup);
                }
            });
        }

        return mstUseTechnicButton;
    }

    /**
     * �R�[�X�_�񕪗ޓo�^�{�^�����擾����B
     *
     * @return �R�[�X�_�񕪗ޓo�^�{�^��
     */
    public JButton getMstCourseClassButton() {
        if (mstCourseClassButton == null) {
            mstCourseClassButton = this.createImageButton(
                    "�R�[�X�_�񕪗ޓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_class_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_class_on.jpg",
                    null,
                    false);

            mstCourseClassButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstCourseClassButton.getModel().setRollover(false);
                    MstCourseClassPanel mccp = new MstCourseClassPanel();
                    mccp.setTitle("�R�[�X�_�񕪗ޓo�^");
                    mccp.setPath("��{�ݒ� >> �Z�p�}�X�^");
                    changeContents(mccp);
                }
            });
        }

        return mstCourseClassButton;
    }

    /**
     * �R�[�X�_�񕪗ޓo�^�{�^�����擾����B
     *
     * @return �R�[�X�_�񕪗ޓo�^�{�^��
     */
    public JButton getMstCourseButton() {
        if (mstCourseButton == null) {
            mstCourseButton = this.createImageButton(
                    "�R�[�X�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_course_on.jpg",
                    null,
                    false);

            mstCourseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstCourseButton.getModel().setRollover(false);
                    MstCoursePanel mccp = new MstCoursePanel();
                    mccp.setTitle("�R�[�X�o�^");
                    mccp.setPath("��{�ݒ� >> �Z�p�}�X�^");
                    changeContents(mccp);
                }
            });
        }

        return mstCourseButton;
    }

    /**
     * �X�^�b�t���у��j���[���擾����B
     *
     * @return �X�^�b�t���у��j���[
     */
    public JPanel getCustomMenu() {
        if (customMenu == null) {
            customMenu = this.createSubMenu();
            if (SystemInfo.getDatabase().equals("pos_hair_nons")
                    || SystemInfo.getDatabase().equals("pos_hair_nons_bak") || SystemInfo.getDatabase().equals("pos_hair_dev")) {
                //���㐄�ڕ\
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
     * �g�p�R�[�X�o�^�{�^�����擾����B
     *
     * @return �g�p�Z�p�o�^�{�^��
     */
    public JButton getMstUseCourseButton() {
        if (mstUseCourseButton == null) {
            mstUseCourseButton = this.createImageButton(
                    "�g�p�R�[�X�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_use_course_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_use_course_on.jpg",
                    null,
                    false);

            mstUseCourseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseCourseButton.getModel().setRollover(false);
                    MstUseProductPanel mcp = new MstUseProductPanel(3);
                    mcp.setTitle("�g�p�R�[�X�o�^");
                    mcp.setPath("��{�ݒ� >> �Z�p�}�X�^");
                    changeContents(mcp);
                }
            });
        }

        return mstUseCourseButton;
    }

	//�}�X�^�ꊇ�o�^ add start 2016/12/28
	/**
     * �Z�p�ꊇ�o�^�{�^�����擾����B
     *
     * @return �Z�p�ꊇ�o�^�{�^��
     */
    public JButton getMstTechnicRegistBulkButton() {
        if (mstTechnicRegistBulkButton == null) {
            mstTechnicRegistBulkButton = this.createImageButton(
                    "�Z�p�ꊇ�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_regist_bulk_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/technic_regist_bulk_on.jpg",
                    null,
                    false);

 			mstTechnicRegistBulkButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstUseCourseButton.getModel().setRollover(false);
                    MstTechnicRegistBulkPanel mcp = new MstTechnicRegistBulkPanel(3);
                    mcp.setTitle("�Z�p�ꊇ�o�^");
                    mcp.setPath("��{�ݒ� >> �Z�p�}�X�^");
                    changeContents(mcp);
                }
            });
        }

        return mstTechnicRegistBulkButton;
    }
	//�}�X�^�ꊇ�o�^ add end 2016/12/28

    /**
     * �g�p�Z�p�o�^�{�^�����擾����B
     *
     * @return �g�p�Z�p�o�^�{�^��
     */
    public JButton getMstStaffTechnicTimeButton() {
        if (mstStaffTechnicTimeButton == null) {
            mstStaffTechnicTimeButton = this.createImageButton(
                    "�X�^�b�t���{�p���ԓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_staff_settime_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/technic/base_technical_staff_settime_entry_on.jpg",
                    null,
                    false);
            /*
             "�X�^�b�t���{�p���ԓo�^",
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
     * ���X�J���e�ݒ�{�^�����擾����B
     * 20170413 add #61376
     *
     * @return ���X�J���e�ݒ�{�^��
     */
    public JButton getVisitKarteSettingButton() {

        if (visitKarteSettingButton == null) {
            visitKarteSettingButton = this.createImageButton(
                    "���X�J���e�ݒ�",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/visit_karte_setting_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/custom/visit_karte_setting_on.jpg",
                    null,
                    false);

            visitKarteSettingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    visitKarteSettingButton.getModel().setRollover(false);
                    VisitKarteSettingPanel vksp = new VisitKarteSettingPanel(
							"���X�J���e�ݒ�");
					vksp.setPath("��{�ݒ� >> ��Њ֘A");
					changeContents(vksp);
                                        vksp.checkAutoNumbering();
                }
            });
        }

        return visitKarteSettingButton;
    }

//add
    /**
     * �ڋq���̓��j���[���擾����B
     *
     * @return �ڋq���̓��j���[
     */
    public JPanel getCustomerEffectMenu() {
        if (customerEffectMenu == null) {
            customerEffectMenu = this.createSubMenu();

            //��������
            this.addSubMenu(customerEffectMenu, this.getReappearancePredictionEffectButton(), 70);

            //TODO �ڋq���̓T�u���j���[ �T�C�N������
            //this.addSubMenu(customerEffectMenu, this.getKarteAnalysisEffectButton(), -1 );

            //�N���X����
            if (!SystemInfo.getSystemType().equals(2)) {
            this.addSubMenu(customerEffectMenu, this.getCrossAnalysisEffectButton(), 71);

            //�N���X����5
            this.addSubMenu(customerEffectMenu, this.getCrossAnalysis5EffectButton(), 72);
            }

            //Luc Add Start 20121004 �G���A����
            this.addSubMenu(customerEffectMenu, this.getCustomerAreaAnalysisButton(), 74);
            //Luc Add End 20121004 �G���A����

            //Luc Add Start 20121004 �ڋq��������
            if (!SystemInfo.getSystemType().equals(2)) {
            this.addSubMenu(customerEffectMenu, this.getCustomerAnalysisAttributeButton(), 73);
            }
            //Luc Add End 20121004 �ڋq��������

            //GB_Mashu Task #34581 [Product][Code][Phase3]�}�b�V���[����
            //IVS_nahoang start add 20141229
            this.addSubMenu(customerEffectMenu, this.getMasherAnalysis5PanelButton(), 75);
            //IVS_nahoang end add 20141229
            //�J���e����
            if (SystemInfo.getSetteing().isItoAnalysis()) {
                this.addSubMenu(customerEffectMenu, this.getItoAnalitic3jigenButton(), -1);
            }

            //Thanh Add Start 20130415 �ڋq�_�񗚗�
            if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_dev") || SystemInfo.getDatabase().startsWith("pos_hair_dev2") || SystemInfo.getDatabase().startsWith("pos_hair_nons_bak")) {
                this.addSubMenu(customerEffectMenu, this.getCusContractHistoryButton(), 75);
            }
            //Thanh Add End 20130415 �ڋq�_�񗚗�


            //TODO �ڋq���̓T�u���j���[ �q����蔭���V�[�g��
            //this.addSubMenu(customerEffectMenu, this.getCustomerProblemSheetButton(), -1 );
        }
        return customerEffectMenu;
    }

    /**
     * ���㕪�̓��j���[���擾����B
     *
     * @return ���㕪�̓��j���[
     */
    public JPanel getSalesEffectMenu() {
        if (salesEffectMenu == null) {
            salesEffectMenu = this.createSubMenu();

            //���ԑѕ���
            this.addSubMenu(salesEffectMenu, this.getTimeAnalysisButton(), 80);

            //����\������
            if (SystemInfo.getSetteing().isItoAnalysis()) {
                this.addSubMenu(salesEffectMenu, this.getItoCustomerProblemSheetButton(), -1);
            }

            //���㐄��
            this.addSubMenu(salesEffectMenu, this.getSalesTotalButton(), 81);
            //Luc start edit 20150623
            //  �p�X�u�b�N�W�v vtbphuong start add 20150603
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
                    "��r�W�v�\",
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
                    "���㕪�͕\",
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
                    "���X�J���e����",
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
//                    "���㕪�͕\",
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
            //��������
            this.addSubMenu(repeatAnalysisMenu, this.getResponseEffectButton(), 80);
            //�������s�[�g
            // 20170705 GB Start Edit #17672 [GB���Ή�][gb] �������s�[�g���͂̃��j���[���ڂ��\���ɂ���
            // this.addSubMenu(repeatAnalysisMenu, this.getResponseRepeatAnalyzetButton(), 81);
            // 20170705 GB End Edit #17672 [GB���Ή�][gb] �������s�[�g���͂̃��j���[���ڂ��\���ɂ���
        }
        return repeatAnalysisMenu;

    }

    /**
     * �ė����̓��j���[���擾����B
     *
     * @return ���㕪�̓��j���[
     */
    public JPanel getReappearanceEffectMenu() {
        if (reappearanceEffectMenu == null) {
            reappearanceEffectMenu = this.createSubMenu();

            // �ė����� add
            this.addSubMenu(reappearanceEffectMenu, this.getReappearanceButton(), -1);
        }
        return reappearanceEffectMenu;
    }

    /**
     * �X�^�b�t���у��j���[���擾����B
     *
     * @return �X�^�b�t���у��j���[
     */
    public JPanel getStaffResultMenu() {
        if (staffResultMenu == null) {
            staffResultMenu = this.createSubMenu();

            //�S���ʐ��шꗗ�\
            this.addSubMenu(staffResultMenu, this.getStaffResultListButton(), 90);
            //�S���ʋZ�p����
            this.addSubMenu(staffResultMenu, this.getStaffResultTechButton(), 91);
            //�S���ʏ��i����
            this.addSubMenu(staffResultMenu, this.getStaffResultGoodsButton(), 92);
            //�S���ʌڋq����
            this.addSubMenu(staffResultMenu, this.getStaffResultCustomerButton(), 93);
            //�S���ʍė�����
            this.addSubMenu(staffResultMenu, this.getStaffResultRepeatButton(), 94);
        }
        return staffResultMenu;
    }

//add end
    /**
     * �ڋq�}�X�^���j���[���擾����B
     *
     * @return �ڋq�}�X�^���j���[
     */
    public JPanel getCustomerMasterMenu() {
        if (customerMasterMenu == null) {
            customerMasterMenu = this.createSubMenu();

            //�ڋq���o�^ add
            this.addSubMenu(customerMasterMenu, this.getRegistCustomerButton(), 33);

            //�����ꗗ add
            this.addSubMenu(customerMasterMenu, this.getNotMemberListButton(), 34);

            //�P�[�^�C����ꗗ
            boolean isAddMenu = false;
            if (SystemInfo.getCurrentShop().getShopID() == 0) {
                isAddMenu = (SystemInfo.getSosiaGearShopList().size() > 0);
            } else {
                isAddMenu = SystemInfo.isSosiaGearEnable();
            }
            if (isAddMenu) {
                this.addSubMenu(customerMasterMenu, this.getMobileMemberListButton(), -1);
            }

            // �E�Ɠo�^�{�^��
            this.addSubMenu(customerMasterMenu, this.getMstJobButton(), 30);

            // ���񗈓X���@�o�^�{�^��
            this.addSubMenu(customerMasterMenu, this.getMstFirstComingMotiveButton(), 38);

            // �ڋq�����N�ݒ�
            if(!SystemInfo.getSystemType().equals(1)) {
                this.addSubMenu(customerMasterMenu, this.getMstCustomerRankSettingButton(), 39);
            }

            //if (!SystemInfo.isReservationOnly()) {
            if (!SystemInfo.getSystemType().equals(1)) {
                // �J���e���ޓo�^�{�^��
                this.addSubMenu(customerMasterMenu, this.getMstKarteClassButton(), 35);

                // �J���e�ڍדo�^�{�^��
                this.addSubMenu(customerMasterMenu, this.getMstKarteDetailButton(), 36);

                // �J���e�Q�ƍ��ړo�^�{�^��
                this.addSubMenu(customerMasterMenu, this.getMstKarteReferenceButton(), 37);
            }

            // �t���[���ڋ敪�o�^�{�^��
            this.addSubMenu(customerMasterMenu, this.getMstFreeHeadingClassButton(), 31);

            // �t���[���ړo�^�{�^��
            this.addSubMenu(customerMasterMenu, this.getMstFreeHeadingButton(), 32);

            // �ڋq�����{�^��
            this.addSubMenu(customerMasterMenu, this.getMstCustomerIntegrationButton(), -1);
        }

        return customerMasterMenu;
    }

    /**
     * �E�Ɠo�^�{�^�����擾����B
     *
     * @return �E�Ɠo�^�{�^��
     */
    private JButton getMstJobButton() {
        if (mstJobButton == null) {
            mstJobButton = this.createImageButton(
                    "�E�Ɠo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_on.jpg",
                    null,
                    false);
            /*
             "�E�Ɠo�^",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_off.jpg",
             "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/job_master_on.jpg",
             null,
             false);
             */
            mstJobButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "�E��", "mst_job", "job_id", "job_name", 20,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("��{�ݒ� >> �ڋq�}�X�^");
                    changeContents(smp);
                }
            });
        }

        return mstJobButton;
    }

    /**
     * ���񗈓X���@�o�^�{�^�����擾����B
     *
     * @return ���񗈓X���@�o�^�{�^��
     */
    private JButton getMstFirstComingMotiveButton() {
        if (mstFirstComingMotiveButton == null) {
            mstFirstComingMotiveButton = this.createImageButton(
                    "���񗈓X���@�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_f_motive.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/entry_f_motive_on.jpg",
                    null,
                    false);
            mstFirstComingMotiveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "���񗈓X���@", "mst_first_coming_motive", "first_coming_motive_class_id", "first_coming_motive_name", 30,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("��{�ݒ� >> �ڋq�}�X�^");
                    changeContents(smp);
                }
            });
        }

        return mstFirstComingMotiveButton;
    }

    /**
     * �ڋq�����N�ݒ�{�^�����擾����B
     *
     * @return �ڋq�����N�ݒ�{�^��
     */
    private JButton getMstCustomerRankSettingButton() {
        if (mstCustomerRankSettingButton == null) {
            mstCustomerRankSettingButton = this.createImageButton(
                    "�ڋq�����N�ݒ�",
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
     * �J���e���ޓo�^�{�^�����擾����B
     *
     * @return �J���e���ޓo�^�{�^��
     */
    private JButton getMstKarteClassButton() {
        if (mstKarteClassButton == null) {
            mstKarteClassButton = this.createImageButton(
                    "�J���e���ޓo�^",
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
     * �J���e�ڍדo�^�{�^�����擾����B
     *
     * @return �J���e�ڍדo�^�{�^��
     */
    private JButton getMstKarteDetailButton() {
        if (mstKarteDetailButton == null) {
            mstKarteDetailButton = this.createImageButton(
                    "�J���e�ڍדo�^",
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
                                MessageUtil.getMessage(7004, "�J���e����"),
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
     * �J���e�Q�ƍ��ړo�^�{�^�����擾����B
     *
     * @return �J���e�Q�ƍ��ړo�^�{�^��
     */
    private JButton getMstKarteReferenceButton() {
        if (mstKarteReferenceButton == null) {
            mstKarteReferenceButton = this.createImageButton(
                    "�J���e�Q�ƍ��ړo�^",
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
                                MessageUtil.getMessage(7004, "�J���e�ڍ�"),
                                mkrp.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        mkrp = null;

                        MstKarteDetailPanel mkdp = new MstKarteDetailPanel();

                        if (mkdp.checkClassRegisted()) {
                            changeContents(mkdp);
                        } else {
                            MessageDialog.showMessageDialog(mkdp,
                                    MessageUtil.getMessage(7004, "�J���e����"),
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
     * �t���[���ڋ敪�o�^�{�^�����擾����B
     *
     * @return �t���[���ڋ敪�o�^�{�^��
     */
    public JButton getMstFreeHeadingClassButton() {
        if (mstFreeHeadingClassButton == null) {
            mstFreeHeadingClassButton = this.createImageButton(
                    "�t���[���ڋ敪�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_class_master_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_class_master_on.jpg",
                    null,
                    false);
            /*
             "�t���[���ڋ敪�o�^",
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
     * �t���[���ڏ��o�^�{�^�����擾����B
     *
     * @return �t���[���ڏ��o�^�{�^��
     */
    public JButton getMstFreeHeadingButton() {
        if (mstFreeHeadingButton == null) {
            mstFreeHeadingButton = this.createImageButton(
                    "�t���[���ړo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_master_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/customer/free_heading_master_on.jpg",
                    null,
                    false);
            mstFreeHeadingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstFreeHeadingButton.getModel().setRollover(false);
                    MstFreeHeadingPanel mfhp = new MstFreeHeadingPanel();

                    // �L���t���[���ڋ敪�����݂��邩���m�F����
                    if (mfhp.isRequires()) {
                        changeContents(mfhp);
                    } else {
                        MessageDialog.showMessageDialog(mfhp,
                                MessageUtil.getMessage(7004, "�t���[���ڋ敪"),
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
     * �ڋq�����{�^�����擾����B
     *
     * @return �ڋq�����{�^��
     */
    public JButton getMstCustomerIntegrationButton() {
        if (mstCustomerIntegrationButton == null) {
            mstCustomerIntegrationButton = this.createImageButton(
                    "�ڋq����",
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
     * ���Z�}�X�^���j���[���擾����B
     *
     * @return ���Z�}�X�^���j���[
     */
    public JPanel getAccountMasterMenu() {
        if (accountMasterMenu == null) {
            accountMasterMenu = this.createSubMenu();

            //����œo�^
            this.addSubMenu(accountMasterMenu, this.getMstTaxButton(), 40);

            //�x�����@�o�^
            this.addSubMenu(accountMasterMenu, this.getMstPaymentMethodButton(), 41);

            //������ʓo�^
            this.addSubMenu(accountMasterMenu, this.getMstDiscountButton(), 42);

            //���V�[�g�ݒ�
            this.addSubMenu(accountMasterMenu, this.getReceiptSettingButton(), -1);
        }

        return accountMasterMenu;
    }

    /**
     * ����œo�^�{�^�����擾����B
     *
     * @return ����œo�^�{�^��
     */
    public JButton getMstTaxButton() {
        if (mstTaxButton == null) {
            mstTaxButton = this.createImageButton(
                    "����œo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_duty_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_duty_entry_on.jpg",
                    null,
                    false);
            /*
             "����œo�^",
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
     * �x�����@�o�^�{�^�����擾����B
     *
     * @return �x�����@�o�^�{�^��
     */
    public JButton getMstPaymentMethodButton() {
        if (mstPaymentMethodButton == null) {
            mstPaymentMethodButton = this.createImageButton(
                    "�x�����@�o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_paying_proccess_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_paying_proccess_entry_on.jpg",
                    null,
                    false);
            /*
             "�x�����@�o�^",
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
     * ������ʓo�^�{�^�����擾����B
     *
     * @return ������ʓo�^�{�^��
     */
    public JButton getMstDiscountButton() {
        if (mstDiscountButton == null) {
            mstDiscountButton = this.createImageButton(
                    "������ʓo�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_discount_assort_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_discount_assort_entry_on.jpg",
                    null,
                    false);
            /*
             "������ʓo�^",
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
     * ���V�[�g�ݒ�{�^�����擾����B
     *
     * @return ���V�[�g�ݒ�{�^��
     */
    public JButton getReceiptSettingButton() {
        if (receiptSettingButton == null) {
            receiptSettingButton = this.createImageButton(
                    "���V�[�g�ݒ�",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_setup_receipt_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/account/base_account_setup_receipt_on.jpg",
                    null,
                    false);
            /*
             "���V�[�g�ݒ�",
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
     * ���C�����j���[���ĕ`�悷��B
     */
    private void repaintMainMenu() {
        //���j���[���J��
        if (openButton != null) {
            openButton.openSubMenu(MENU_OPEN_SPEED);

            if (openButton.isSubMenuOpen()) {
                openedButton = openButton;
                openButton = null;
            }
        }
        //���j���[�����
        if (closeButton != null) {
            closeButton.closeSubMenu(MENU_OPEN_SPEED);

            if (!closeButton.isSubMenuOpen()) {
                closeButton = null;
            }
        }
        //�������I����Ă���΁A�^�C�}�[���~�߂�
        if (openButton == null && closeButton == null) {
            timer.stop();
        }

        int y = 0;

        //���j���[�̍��W��ݒ肷��
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
     * �T�u���j���[��\������B
     */
    private void showSubMenu(Point loc, Integer width, JPanel selectedSubMenu) {
        subMenu = selectedSubMenu;

        int offsetY = 33;

        // ��Њ֘A���j���[�̏ꍇ
        if (subMenu.equals(companyMasterMenu)) {
            // �ŉ��s�̃��j���[���\������Ȃ����ߕ\���ʒu����Ɉړ�
            int rowCount = (selectedSubMenu.getComponentCount() - 10);
            if (rowCount > 0) {
                offsetY += (25 * rowCount);
            }
        }

        // �ڋq��񃁃j���[�̏ꍇ
        if (subMenu.equals(customerMasterMenu)) {
            // �ŉ��s�̃��j���[���\������Ȃ����ߕ\���ʒu����Ɉړ�
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
     * �T�u���j���[�������B
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
     * �g�b�v�y�[�W�p�p�l�����擾����
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
     * ���v�֘A�̏��������s���B
     */
    private void initClock() {
        //���v�p�̉摜��ǂݍ���
        this.loadClockImage();

        //�^�C�}�[���Z�b�g
        clockTimer = new javax.swing.Timer(CLOCK_TIMER_INTERVAL,
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        refreshClock();
                    }
                });


        //�^�C�}�[�X�^�[�g
        clockTimer.start();
    }

    /**
     * ���v�p�̉摜���擾����B
     */
    private void loadClockImage() {
        //�����̉摜��ǂݍ���
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
     * ���v���X�V����B
     */
    private void refreshClock() {
        GregorianCalendar gc = new GregorianCalendar();
        //�N
        year1000.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 1000, false));
        year100.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 100, false));
        year10.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 10, false));
        year1.setImage(this.getNumberImage(gc.get(Calendar.YEAR), 1, false));
        //��
        month10.setImage(this.getNumberImage(gc.get(Calendar.MONTH) + 1, 10, false));
        month1.setImage(this.getNumberImage(gc.get(Calendar.MONTH) + 1, 1, false));
        //��
        day10.setImage(this.getNumberImage(gc.get(Calendar.DAY_OF_MONTH), 10, false));
        day1.setImage(this.getNumberImage(gc.get(Calendar.DAY_OF_MONTH), 1, false));
        //��
        hour10.setImage(this.getNumberImage(gc.get(Calendar.HOUR_OF_DAY), 10, true));
        hour1.setImage(this.getNumberImage(gc.get(Calendar.HOUR_OF_DAY), 1, true));
        //�R����
        colon.setVisible(500 <= gc.get(Calendar.MILLISECOND));
        //��
        minute10.setImage(this.getNumberImage(gc.get(Calendar.MINUTE), 10, true));
        minute1.setImage(this.getNumberImage(gc.get(Calendar.MINUTE), 1, true));

        clockPanel.updateUI();
    }

    /**
     * �����̉摜���擾����B
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
     * �}�X�^�o�^�������`�F�b�N����B
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
     * �o�[�R�[�h���ǂݍ��܂ꂽ�Ƃ��̏����B
     */
    public boolean readBarcode(BarcodeEvent be) {
        return true;
    }

    /**
     * ���O�C�����Ă���̌o�ߎ��ԃ`�F�b�N
     */
    private void checkLoginTime() {

        long time = Calendar.getInstance().getTimeInMillis() - calLoginTime.getTimeInMillis();
        time = time / 1000 / 3600;

        if (time >= 24) {

            isCheckLoginTime = false;

            String msg = "";
            msg += "SOSIA POS ���N�����Ă��� 24���� ���o�߂��܂����B\n";
            msg += "�V�X�e���̍ŐV���𔽉f�����邽�߂ɍċN�������肢���܂��B";

            MessageDialog.showMessageDialog(
                    this, msg, this.getTitle(), JOptionPane.INFORMATION_MESSAGE);

        }
    }

    /**
     * �w��̎��Ԗ��Ƀ��o�C���\�񂪓����Ă��邩�Ď��B
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
     * ���o�C���\�񂪓����Ă���ꍇ�A�{�^����_�ł�����B
     */
    private void setMobileData() {

        if (isMobileData()) {
            mobileButton.setEnabled(true);
//������ŉ^�p���Ƀ{�^���������Ȃ��s��Ή�	�Ń^�C�}�[��~����߂�B
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

            // CTI�e�X�g�p
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
     *CTI���ǂݍ��܂ꂽ�Ƃ��̏����B
     */
    public boolean readCTI(CTIEvent ce) {
        System.out.println("getCTI :" + ce.getCTI());
        String ctiCustomerData = ce.takeCustomerData();
        System.out.println("ctiCustomerData :" + ctiCustomerData);
        CTICustomerDialog ccd = new CTICustomerDialog(ctiCustomerData, this);

        return true;
    }

    /**
     * �u��T�u���j���[�̃{�^����Ԃ�
     */
    public JButton getMstPlaceButton() {
        if (mstPlaceButton == null) {
            mstPlaceButton = this.createImageButton(
                    "�u��}�X�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/marcherdise_locatorlist_entry_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/item/marcherdise_locatorlist_entry_on.jpg",
                    null,
                    false);

            mstPlaceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstPlaceButton.getModel().setRollover(false);
                    SimpleMasterPanel smp = new SimpleMasterPanel(
                            "�u��", "mst_place", "place_id",
                            "place_name", 40,
                            SystemInfo.getTableHeaderRenderer());
                    smp.setPath("��{�ݒ� >> ���i�}�X�^");
                    changeContents(smp);
                }
            });
        }

        return mstPlaceButton;
    }

    /**
     * �|�C���g�J�[�h�{�^�����擾����B
     *
     * @return �|�C���g�J�[�h�{�^��
     */
    public JButton getPointcardSubButton() {
        if (pointcardButton == null) {
            pointcardButton = this.createImageButton(
                    "�|�C���g�J�[�h",
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
     * �|�C���g�J�[�h�p���j���[���擾����B
     *
     * @return �|�C���g�J�[�h�p���j���[
     */
    public JPanel getPointcardMenu() {
        // �|�C���g�J�[�h�p
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
     * �|�C���g�J�[�h�e���v���[�g�o�^�{�^�����擾����B
     *
     * @return �|�C���g�J�[�h�e���v���[�g�o�^�{�^��
     */
    public JButton getPointardTemplateButton() {
        if (pointcardTemplateButton == null) {
            pointcardTemplateButton = this.createImageButton(
                    "�e���v���[�g�o�^",
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
     * �|�C���g�J�[�h�v�Z���o�^�{�^�����擾����B
     *
     * @return �|�C���g�J�[�h�v�Z���o�^�{�^��
     */
    public JButton getPointardCalculationButton() {
        if (pointcardCalculationButton == null) {
            pointcardCalculationButton = this.createImageButton(
                    "�|�C���g�v�Z���o�^",
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
     * �|�C���g�J�[�h�����ݒ�{�^�����擾����B
     *
     * @return �|�C���g�J�[�h�����ݒ�{�^��
     */
    public JButton getPointardInitSettingButton() {
        if (pointcardInitSettingButton == null) {
            pointcardInitSettingButton = this.createImageButton(
                    "�J�[�h������",
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
     * �J�[�h�e�X�g�{�^�����擾����B
     *
     * @return �J�[�h�󎚐ݒ�{�^��
     */
    public JButton getPointardTestButton() {
        if (pointcardTestButton == null) {
            pointcardTestButton = this.createImageButton(
                    "�J�[�h�e�X�g",
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
     * ���i�Ǘ��{�^����ǉ�����
     *
     * @return ���i�Ǘ��{�^��
     */
    private MainMenuButton getProductButton() {

        if (productButton == null) {

            productButton = new MainMenuButton(this.createImageButton(
                    "���i�Ǘ�",
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
                // �X�܊Ԉړ�
                productButton.addSubMenuButton(this.getTransferProductPanelButton());
                // �݌Ɋm�F
                productButton.addSubMenuButton(this.getStockListButton());
            }
            // IVS SANG START INSERT 20131209 [gb�\�[�X]���i��n�m�F���̃}�[�W
            // �ڋq�ʍ݌ɊǗ�
            // IVS SANG START INSERT 20131218 [gb�\�[�X]���i�Ǘ��ˌڋq�ʍ݌ɊǗ��A���i��Ǘ��̕\������
            //IVS VTBPHUONG START CHANGE 20140502 Request #22768
            //if(SystemInfo.getCurrentShop().getCourseFlag() != null && SystemInfo.getCurrentShop().getCourseFlag() == 1) {
            // IVS SANG END INSERT 20131218 [gb�\�[�X]���i�Ǘ��ˌڋq�ʍ݌ɊǗ��A���i��Ǘ��̕\������
            //IVS_LVTu start edit 2017/09/18 #25966 [gb]��{�ݒ聄��Њ֘A���X�܏��o�^�ɏ��i��n�@�\�g�p�t���O�̒ǉ�
            if (SystemInfo.getCurrentShop().getDeliveryFlag()!= null
                      && SystemInfo.getCurrentShop().getDeliveryFlag().intValue() == 1){
                productButton.addSubMenuButton(this.getProductCustomerButton());
            }
            //IVS_LVTu end edit 2017/09/18 #25966 [gb]��{�ݒ聄��Њ֘A���X�܏��o�^�ɏ��i��n�@�\�g�p�t���O�̒ǉ�
            //}
            //IVS VTBPHUONG END CHANGE 20140502 Request #22768
            // start add 20130314 nakhoa ���i��z�Ǘ�
            if (SystemInfo.getCurrentShop().getShopID() == 0) {
                // IVS SANG START INSERT 20131218 [gb�\�[�X]���i�Ǘ��ˌڋq�ʍ݌ɊǗ��A���i��Ǘ��̕\������
                if (SystemInfo.getNSystem() == 2) {
                    // IVS SANG END INSERT 20131218 [gb�\�[�X]���i�Ǘ��ˌڋq�ʍ݌ɊǗ��A���i��Ǘ��̕\������
                    productButton.addSubMenuButton(this.getProductDeliveryManagementButton());
                }
            }
            // endadd 201303014 nakhoa ���i��z�Ǘ�
            // IVS SANG END INSERT 20131209 [gb�\�[�X]���i��n�m�F���̃}�[�W
        }

        return productButton;
    }

    // IVS SANG START INSERT 20131209 [gb�\�[�X]���i��n�m�F���̃}�[�W
    private JButton getProductCustomerButton() {
        if (productCustomerButton == null) {

            productCustomerButton =
                    this.createImageButton(
                    "�ڋq�ʍ݌ɊǗ�",
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
    // IVS SANG START INSERT 20131209 [gb�\�[�X]���i��n�m�F���̃}�[�W

    /**
     * ���j���[ ����������
     */
    private JButton getRegisterOrderSlipButton() {
        if (registerOrderSlipButton == null) {
            registerOrderSlipButton = this.createImageButton(
                    "�������쐬", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_order_drawup_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_order_drawup_on.jpg", null, true);

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
     * ���j���[ ���ɓ���
     */
    public JButton getRegisterCheckInVoucherButton() {
        if (registerCheckInVoucherButton == null) {
            registerCheckInVoucherButton = this.createImageButton(
                    "���ɓ`�[�쐬", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_in_check_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_in_check_on.jpg", null, true);

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
     * ���j���[ �o�ɓ���
     */
    public JButton getRegisterCheckOutVoucherButton() {
        if (registerCheckOutVoucherButton == null) {
            registerCheckOutVoucherButton = this.createImageButton(
                    "�o�ɓ`�[�쐬", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_out_check_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_out_check_on.jpg", null, true);

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
     * ���j���[ �I��
     */
    public JButton getInventryPanelButton() {
        if (inventryPanelButton == null) {
            inventryPanelButton = this.createImageButton(
                    "�I��", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_on.jpg", null, true);

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
     * ���j���[ �I��(TOM)
     */
    public JButton getInventryTomPanelButton() {
        if (inventryPanelTomButton == null) {
            inventryPanelTomButton = this.createImageButton(
                    "�I��", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_stock_take_on.jpg", null, true);

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
     * ���j���[ �X�܊Ԉړ�
     */
    public JButton getTransferProductPanelButton() {
        if (transferProductButton == null) {
            transferProductButton = this.createImageButton(
                    "�X�܊Ԉړ�", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_between_shop_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/merchandise_between_shop_on.jpg", null, true);

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
     * �݌Ɉꗗ�m�F
     */
    public JButton getStockListButton() {
        if (stocklistButton == null) {
            stocklistButton = this.createImageButton(
                    "�݌Ɉꗗ�m�F", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/stock-check_off.jpg", "/images/" + SystemInfo.getSkinPackage() + "/menu/product/stock-check_on.jpg", null, true);

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
     * ���i�Ǘ����[�{�^�����擾����B
     *
     * @return ���i�Ǘ����[�{�^��
     */
    public JButton getProductReportButton() {
        if (productReportButton == null) {
            productReportButton = this.createImageButton(
                    "���[",
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
     * ���i�Ǘ����[�{�^�����擾����B
     *
     * @return ���i�Ǘ����[�{�^��
     */
    public JButton getProductReportTomButton() {
        if (productReportTomButton == null) {
            productReportTomButton = this.createImageButton(
                    "���[(TOM)",
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
     * ���i�Ǘ����[�{�^�����擾����B
     *
     * @return ���i�Ǘ����[�{�^��
     */
    public JButton getProductStaffSalesButton() {
        if (productStaffSalesButton == null) {
            productStaffSalesButton = this.createImageButton(
                    "�X�^�b�t�̔�",
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
        // ���i�Ǘ��X�^�b�t�̔��T�u���j���[
        if (productStaffSalesMenu == null) {
            productStaffSalesMenu = this.createSubMenu();

            // �̔�����
            staffSalesInputButton = this.getImageButton(staffSalesInputButton, "/menu/product/merchedise_staff_sales_input", "�X�^�b�t�̔�����",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    staffSalesInputButton.getModel().setRollover(false);
                            changeContents(new RegisterStaffSalesPanel());
                        }
                    });
            this.addSubMenu(productStaffSalesMenu, staffSalesInputButton, -1);

            // �̔�����
            staffSalesHistoryButton = this.getImageButton(staffSalesHistoryButton, "/menu/product/merchedise_staff_sales_career", "�X�^�b�t�̔�����",
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
     * ���i�Ǘ����[�T�u���j���[���擾����B
     *
     * @return ���i�Ǘ����[�T�u���j���[
     */
    public JPanel getProductReportSubMenu() {
        //���i�Ǘ����[�T�u���j���[
        if (productReportSubMenu == null) {
            productReportSubMenu = this.createSubMenu();

            // ���o�Ɉꗗ�\
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/marcherdise_list_input-output", "���o�Ɉꗗ�\",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new StoreShipPanel());
                        }
                    }), -1);
            // �u��ʍ݌ɕ[
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/marcherdise_locatorlist", "�u��ʍ݌ɕ[",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new PlacePanel());
                        }
                    }), -1);
            // �ԕi�ꗗ�\
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/marcherdise_return_list", "�ԕi�ꗗ�\",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new BackProductPanel());
                        }
                    }), -1);
            // �I���\
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/merchedise_stock_take_list", "�I���\",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new PrintInventoryPanel());
                        }
                    }), -1);
            // ���v�I���\
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/merchedise_stock_take_list_total", "���v�I���\",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new TotalInventryPanel());
                        }
                    }), -1);
//                // �X�^�b�t�̔��ꗗ�\
//                this.addSubMenu(productReportSubMenu,
//                            this.getImageButton(materialCostTomButton, "/menu/product/merchedise_staff_sales_check", "�X�^�b�t�̔��ꗗ�\",
//                                        new ActionListener() {
//                                            public void actionPerformed(ActionEvent ae) {
////                                                    materialCostTomButton.getModel().setRollover(false);
//                                                    changeContents(new StaffSalesListPanel());
//                                                }
//                                            }
//                                        ), -1 );

            // start add 20141128 g_wang ���i�݌ɐ��ꗗ
            // ���i�݌ɐ��ꗗ
            this.addSubMenu(productReportSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/item_stock_list", "���i�݌ɐ��ꗗ",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new ItemStockListPanel());
                        }
                    }), -1);
            // end add 20141128 g_wang ���i�݌ɐ��ꗗ

            //IVS_TMTrong start add 20150707 New request #38966
            //���i��n�ꗗ
            //IVS_LVTu start edit 2017/09/18 #25966 [gb]��{�ݒ聄��Њ֘A���X�܏��o�^�ɏ��i��n�@�\�g�p�t���O�̒ǉ�
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
                        this.getImageButton(materialCostTomButton, "/menu/product/item_delivery_history", "���i��n�ꗗ",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
    //                                                    materialCostTomButton.getModel().setRollover(false);
                                changeContents(new ItemDeliveryHistoryPanel());
                            }
                        }), -1);
            }
            //IVS_LVTu end edit 2017/09/18 #25966 [gb]��{�ݒ聄��Њ֘A���X�܏��o�^�ɏ��i��n�@�\�g�p�t���O�̒ǉ�
            //IVS_TMTrong end add 20150707 New request #38966
        }


        return productReportSubMenu;
    }

    /**
     * ���i�Ǘ����[�T�u���j���[���擾����B
     *
     * @return ���i�Ǘ����[�T�u���j���[
     */
    public JPanel getProductReportTomSubMenu() {
        //���i�Ǘ����[�T�u���j���[
        if (productReportTomSubMenu == null) {
            productReportTomSubMenu = this.createSubMenu();

            // �ޗ��䗦(TOM)
            this.addSubMenu(productReportTomSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/mercherdise_ingredients_rate", "�ޗ��䗦(TOM)",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new MaterialRatePanel_TOM());
                        }
                    }), -1);

            // ���v�I��(TOM)
            this.addSubMenu(productReportTomSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/merchedise_stock_take_list_total", "���v�I��(TOM)",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    materialCostTomButton.getModel().setRollover(false);
                            changeContents(new TotalInventryPanel_TOM());
                        }
                    }), -1);

            // ���ύޗ��䗦(TOM)
            this.addSubMenu(productReportTomSubMenu,
                    this.getImageButton(materialCostTomButton, "/menu/product/mercherdise_ingredients_rate_years", "���ύޗ��䗦(TOM)",
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
     * �J�X�^�����[�{�^�����擾����B
     *
     * @return �J�X�^�����[�{�^��
     */
    public JButton getCustomReportSubButton() {
        if (customReportButton == null) {
            customReportButton = this.createImageButton(
                    "�J�X�^�����[",
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
     * �J�X�^�����[���j���[���擾����B
     *
     * @return �J�X�^�����[���j���[
     */
    public JPanel getcCustomReportMenu() {
        //�e�X�g�p
        if (customReportMenu == null) {
            customReportMenu = this.createSubMenu();


            // TOM���[
            // ���v�o��
            this.addSubMenu(customReportMenu, this.getImageButton(dailyReportButton, "/menu/report/daily_report", "���v�o��",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    dailyReportButton.getModel().setRollover(false);
                            changeContents(new DailySalesReportPanelTom());
                        }
                    }), -1);

            // ���ԉ��
            this.addSubMenu(customReportMenu, this.getImageButton(monthlyReportButton, "/menu/report/monthly_report", "���ԉ��",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    monthlyReportButton.getModel().setRollover(false);
                            changeContents(new TechnicSalesReportPanelTom());
                        }
                    }), -1);

            // �����L���O
//            this.addSubMenu(customReportMenu, this.getImageButton(rankingReportButton, "/menu/report/ranking_report", "�����L���O",
//                    new ActionListener() {
//                        public void actionPerformed(ActionEvent ae) {
//                                                   rankingReportButton.getModel().setRollover(false);
//                            changeContents(new RankingReportPanelTom());
//                        }
//                    }), -1);

            // �ė�����
            this.addSubMenu(customReportMenu, this.getImageButton(repeatReportButton, "/menu/report/repeater_report", "�ė�����",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
//                                                    repeatReportButton.getModel().setRollover(false);
                            changeContents(new RepeaterReportPanelTom());
                        }
                    }), -1);

            // ���㕪��
            this.addSubMenu(customReportMenu, this.getImageButton(analysisReportButton, "/menu/report/sales_report", "���㕪��",
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
     * �J�X�^���ݒ�{�^�����擾����B
     *
     * @return �J�X�^���ݒ�{�^��
     */
    public JButton getCustomSettingButton() {
        if (customSetteingButton == null) {
            customSetteingButton = this.createImageButton(
                    "�J�X�^���ݒ�",
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

    // vtbphuong start add 20140708  �𖱒��[
    /**
     * �J�X�^���ݒ�{�^�����擾����B
     *
     * @return �J�X�^���ݒ�{�^��
     */
    public JButton getServiceReportButton() {
        if (serviceReportButton == null) {
            serviceReportButton = this.createImageButton(
                    "�𖱒��[",
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
            // 201803 GB edit start #43652 [GB���Ή�][gb]�𖱒��[���j���[���\������Ȃ�
            // �����֌W�Ȃ��\������
            //    this.addSubMenu(serviceReportMenu, this.getServiceListButton(), 0);
            //    this.addSubMenu(serviceReportMenu, this.getCancelCourseListButton(), 0);
            //    this.addSubMenu(serviceReportMenu, this.getExpirationListButton(), 0);
            this.addSubMenu(serviceReportMenu, this.getServiceListButton(), -1);
            this.addSubMenu(serviceReportMenu, this.getCancelCourseListButton(),-1);
            this.addSubMenu(serviceReportMenu, this.getExpirationListButton(), -1);
            // 201803 GB edit end #43652 [GB���Ή�][gb]�𖱒��[���j���[���\������Ȃ�
             //IVS_LVTu start add 2015/11/06 New request #44110
            if (SystemInfo.getDatabase().startsWith("pos_hair_natura") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
                // 201803 GB edit start #43652 [GB���Ή�][gb]�𖱒��[���j���[���\������Ȃ�
                //    this.addSubMenu(serviceReportMenu, this.getContractReportButton(), 0);
                //    this.addSubMenu(serviceReportMenu, this.getProposalReportButton(), 0);
                this.addSubMenu(serviceReportMenu, this.getContractReportButton(), -1);
                this.addSubMenu(serviceReportMenu, this.getProposalReportButton(), -1);
                // 201803 GB edit end #43652 [GB���Ή�][gb]�𖱒��[���j���[���\������Ȃ�
            }
             //IVS_LVTu end add 2015/11/06 New request #44110
            if (SystemInfo.getDatabase().startsWith("pos_hair_garden")) {
                this.addSubMenu(serviceReportMenu, this.getContractReportButton(), -1);
            }
        }
        return serviceReportMenu;

    }
    // �𖱎c�ꗗ

    private JButton getServiceListButton() {
        if (serviceListButton == null) {
            serviceListButton = this.createImageButton(
                    "�𖱎c�ꗗ",
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
    // �L�������ꗗ
     private JButton getExpirationListButton() {
        if (expirationListButton == null) {
            expirationListButton = this.createImageButton(
                    "�L�������ꗗ",
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
    //���ꗗ

    private JButton getCancelCourseListButton() {
        if (cancelCourseListButton == null) {
            cancelCourseListButton = this.createImageButton(
                    "���ꗗ",
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
    //�_�񗦏W�v
    private JButton getContractReportButton() {
        if (contractRateReportButton == null) {
            contractRateReportButton = this.createImageButton(
                    "�_�񗦏W�v",
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
    //�_�񗦏W�v
    private JButton getProposalReportButton() {
        if (ProposalReportButton == null) {
            ProposalReportButton = this.createImageButton(
                    "��ď��ꗗ",
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
                    "�J�X�^���ݒ�",
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
     * �J�X�^���ݒ胁�j���[���擾����B
     *
     * @return �J�X�^���ݒ胁�j���[
     */
    public JPanel getcCustomSettingMenu() {
        //�e�X�g�p
        if (customSettingMenu == null) {
            customSettingMenu = this.createSubMenu();

            // �ڕW�����с��ғ�����
            this.addSubMenu(customSettingMenu, this.getImageButton(resultTargetButton, "/menu/report/target_result", "�ڕW�����с��ғ�����",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            hideSubMenu();
                            (new TargetActualShopInfoFrame()).setVisible(true);
                        }
                    }), -1);

            // �o�Γo�^���
            this.addSubMenu(customSettingMenu, this.getImageButton(workingStaffButton, "/menu/report/working_staff", "�o�Γo�^���",
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
        //�e�X�g�p
        if (customSettingMenu == null) {
            customSettingMenu = this.createSubMenu();

            // 20130417 Phuong start add
            // ���ڕW�����с��ғ�����
            this.addSubMenu(customSettingMenu, this.getImageButton(resultTargetButton, "/menu/report/target_result", "�ڕW�����с��ғ�����",
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
     * ������Ǘ�
     *
     * @return ������Ǘ��}�X�^���j���[
     */
    public JPanel getManageMemberMasterMenu() {
        if (manageMemberMasterMenu == null) {
            manageMemberMasterMenu = this.createSubMenu();

            //�v�����o�^
            this.addSubMenu(manageMemberMasterMenu, this.getMstPlanButton(), -1);

            //������o�^
            this.addSubMenu(manageMemberMasterMenu, this.getDataMonMemberButton(), -1);

            //������o�^
            this.addSubMenu(manageMemberMasterMenu, this.getDataMonthlyBatchButton(), -1);
            //IVS start add 2020/04/01 �����U�֘A�g
            //�����U�֘A�g
            if (SystemInfo.getSetteing().isUseAccountTransfer()) {
                this.addSubMenu(manageMemberMasterMenu, this.getTransferLinkButton(), -1);
            }
            //IVS end add 2020/04/01 �����U�֘A�g
        }

        return manageMemberMasterMenu;
    }

    /**
     * �v�����o�^�{�^�����擾����B
     *
     * @return �v�����o�^�{�^��
     */
    public JButton getMstPlanButton() {
        if (mstPlanButton == null) {
            mstPlanButton = this.createImageButton(
                    "�v�����o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_plan_regist_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_plan_regist_on.jpg",
                    null,
                    false);

            mstPlanButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    mstPlanButton.getModel().setRollover(false);
                    MstPlanPanel mccp = new MstPlanPanel();
                    mccp.setTitle("�v�����o�^");
                    mccp.setPath("��{�ݒ聄������Ǘ�");
                    changeContents(mccp);
                }
            });
        }

        return mstPlanButton;
    }

    /**
     * ������o�^�{�^�����擾����B
     *
     * @return ������o�^�{�^��
     */
    public JButton getDataMonMemberButton() {
        if (dataMonMemberButton == null) {
            dataMonMemberButton = this.createImageButton(
                    "������o�^",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_customer_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_customer_on.jpg",
                    null,
                    false);

            dataMonMemberButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dataMonMemberButton.getModel().setRollover(false);
                    DataMonthMemberPanel dmmp = new DataMonthMemberPanel();
                    dmmp.setTitle("������o�^");
                    dmmp.setPath("��{�ݒ聄������Ǘ�");
                    changeContents(dmmp);
                }
            });
        }

        return dataMonMemberButton;
    }

    /**
     * �ꊇ�����{�^�����擾����B
     *
     * @return �v�ꊇ�����{�^��
     */
    public JButton getDataMonthlyBatchButton() {
        if (dataMonthlyBatchButton == null) {
            dataMonthlyBatchButton = this.createImageButton(
                    "�ꊇ����",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_batch_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_batch_on.jpg",
                    null,
                    false);

            dataMonthlyBatchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dataMonthlyBatchButton.getModel().setRollover(false);
                    DataMonthlyBatchPanel dmbp = new DataMonthlyBatchPanel();
                    dmbp.setTitle("�ꊇ����");
                    dmbp.setPath("��{�ݒ聄������Ǘ�");
                    changeContents(dmbp);
                }
            });
        }

        return dataMonthlyBatchButton;
    }
    //IVS_LVTu end add 2018/13/05 GB_Finc
    //IVS start add 2020/04/01 �����U�֘A�g
    /**
     * �����U�֘A�g
     *
     * @return �v�����U�֘A�g�{�^��
     */
    public JButton getTransferLinkButton() {
        if (accountTransferButton == null) {
            accountTransferButton = this.createImageButton(
                    "�����U�֘A�g",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_relation_off.jpg",
                    "/images/" + SystemInfo.getSkinPackage() + "/menu/master/member/monthly_relation_on.jpg",
                    null,
                    false);

            accountTransferButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    accountTransferButton.getModel().setRollover(false);
                    AccountTransferPanel tlp = new AccountTransferPanel();
                    tlp.setTitle("�����U�֘A�g");
                    tlp.setPath("��{�ݒ聄������Ǘ�");
                    changeContents(tlp);
                }
            });
        }

        return accountTransferButton;
    }
    //IVS end add 2020/04/01 �����U�֘A�g

    //Start add 20220425 TUNE�I���W�i�����[�o�� �̑Ή�
    /**
     * �J�X�^�����[
     *
     * @return �v�J�X�^�����[�{�^��
     */
    private JButton getCustomizeReportmButton() {
        if (customizeReportButton == null) {
            customizeReportButton = this.createImageButton(
                    "�J�X�^�����[",
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
    //ENd add 20220425 TUNE�I���W�i�����[�o�� �̑Ή�

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

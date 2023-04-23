/*
 * HairInputAccountPanel.java
 *
 * Created on 2006/10/18, 20:03
 */
package com.geobeck.sosia.pos.hair.account;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.comm.PortInUseException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.geobeck.barcode.BarcodeEvent;
import com.geobeck.barcode.BarcodeListener;
import com.geobeck.customerDisplay.CustomerDisplay;
import com.geobeck.sosia.pos.account.BillsList;
import com.geobeck.sosia.pos.account.CollectBillPanel;
import com.geobeck.sosia.pos.account.NameValue;
import com.geobeck.sosia.pos.account.PrintReceipt;
import com.geobeck.sosia.pos.account.ReceiptData;
import com.geobeck.sosia.pos.account.SearchAccountPanel;
import com.geobeck.sosia.pos.basicinfo.SelectSameNoData;
import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import com.geobeck.sosia.pos.basicinfo.company.MstSearchItemResponsePanel;
import com.geobeck.sosia.pos.basicinfo.customer.SosiaGearDialog;
import com.geobeck.sosia.pos.data.account.DataPaymentDetail;
import com.geobeck.sosia.pos.hair.basicinfo.product.MainStaffForSettingBusiness;
import com.geobeck.sosia.pos.hair.customer.DataProposal;
import com.geobeck.sosia.pos.hair.customer.DataProposalDetail;
import com.geobeck.sosia.pos.hair.customer.MstCustomerPanel;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourseClass;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.data.account.CourseClass;
import com.geobeck.sosia.pos.hair.data.account.CourseClasses;
import com.geobeck.sosia.pos.hair.data.account.CunsumptionCourseClasses;
import com.geobeck.sosia.pos.hair.data.account.DataSales;
import com.geobeck.sosia.pos.hair.data.account.DataSalesDetail;
import com.geobeck.sosia.pos.hair.data.account.DataSalesMainstaff;
import com.geobeck.sosia.pos.hair.data.account.DataSalesProportionally;
import com.geobeck.sosia.pos.hair.data.company.DataResponseEffect;
import com.geobeck.sosia.pos.hair.data.company.DataResponseIssue;
import com.geobeck.sosia.pos.hair.data.company.DataResponseIssues;
import com.geobeck.sosia.pos.hair.data.reservation.DataReservationDetail;
import com.geobeck.sosia.pos.hair.data.reservation.DataReservationMainstaff;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.company.MstBeds;
import com.geobeck.sosia.pos.hair.master.company.MstResponse;
import com.geobeck.sosia.pos.hair.master.company.MstResponses;
import com.geobeck.sosia.pos.hair.pointcard.PointData;
import com.geobeck.sosia.pos.hair.pointcard.PrepaidData;
import com.geobeck.sosia.pos.hair.reservation.RegistReservation;
import com.geobeck.sosia.pos.hair.reservation.ReservationStatusPanel;
import com.geobeck.sosia.pos.hair.search.product.SearchHairProductOpener;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.master.account.MstDiscount;
import com.geobeck.sosia.pos.master.account.MstPaymentClass;
import com.geobeck.sosia.pos.master.account.MstPaymentClasses;
import com.geobeck.sosia.pos.master.account.MstPaymentMethod;
import com.geobeck.sosia.pos.master.account.MstTax;
import com.geobeck.sosia.pos.master.account.MstTaxs;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstShopRelation;
import com.geobeck.sosia.pos.master.company.MstShopRelations;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.master.customer.MstDataDeliveryProduct;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.products.Product;
import com.geobeck.sosia.pos.products.ProductClass;
import com.geobeck.sosia.pos.products.ProductClasses;
import com.geobeck.sosia.pos.search.account.SearchAccountDialog;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import com.geobeck.sosia.pos.search.customer.SearchProposal;
import com.geobeck.sosia.pos.swing.SelectTableCellRedRenderer;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.FormatUtil;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.DoubleCellEditor;
import com.geobeck.swing.FormatterCreator;
import com.geobeck.swing.IntegerCellEditor;
import com.geobeck.swing.JFormattedTextFieldEx;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.swing.table.BevelBorderHeaderRenderer;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.SQLUtil;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import javax.swing.DefaultCellEditor;

import jp.co.flatsoft.fscomponent.FSCalenderCombo;

/**
 *
 * @author katagiri
 */
public class HairInputAccountPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
        implements SearchHairProductOpener, BarcodeListener {
    //割引区分

    public static final int DISCOUNT_DIVISION_ALL = 0;
    public static final int DISCOUNT_DIVISION_TECHNIC = 1;
    public static final int DISCOUNT_DIVISION_PRODUCT = 2;
    //割引方法
    public static final int DISCOUNT_METHOD_VALUE = 1;
    public static final int DISCOUNT_METHOD_RATE = 2;
    /**
     * 行の高さ
     */
    private final int ROW_HEIGHT = 27;
    /**
     * 列変数
     */
    private int pointCol = 0;       //ポイント
    private int rateCol = 0;        //割合
    private int taxCol = 0;     // 税率
    private int staffNoCol = 0;     // staff NO
    private int staffCol = 0;       //担当者
    private int designatedCol = 0;  //指名
    private int approachedCol = 0;  //アプローチ
    private int bedCol = 0;         //施術台
    private int pickupCol = 0;      //  宅送
    private int restNumCol = 0;//残回数

    // add by ltthuc
    private int tempRow;
    private int tempCol;
    private int tempPaymentRow;
    private int tempPaymentCol;
    private int tempProduct;
    // end add ltthuc
    /**
     * レスポンスマスタ
     */
    private MstResponses mrs = new MstResponses();
    private DataResponseIssues drs = new DataResponseIssues();
    private MstShop targetShop = null;
    //IVS_LVTu start add 2015/10/09 Bug #43295
    public static MstShop refShop = null;
    //IVS_LVTu end add 2015/10/09 Bug #43295
    /**
     * 施術台マスタ
     */
    private MstBeds beds = new MstBeds();
    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
    private ArrayList<MstBed> arrBeds = new ArrayList<MstBed>();
    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示

    /**
     * 施術台リスト
     */
    private HairInputAccount ia = new HairInputAccount();
    // 伝票詳細登録数
    private int detailCount = 0;
    //指名:true or フリー：false 指名フリー状態保持
    private boolean shimeiFreeFlag = false;
    //レーシートボタン対応 出力:true 出力しない:false レシート出力状態保持
    private boolean printReceiptFlsg = false;
    private boolean isLoading = false;
    private boolean editDataContract = false;
    //add by ltthuc 2014/06/25
    private boolean isDatabaseName;
    private boolean isFirstShow = false;
    private CustomerDisplay customerDisplay = null;
    private String responseNo1 = "";
    private String responseNo2 = "";
    //add by ltthuc
    private String tempPayment;
    String temp = null;
    // end add ltthuc
    private boolean changeCus = false;
    private boolean stat = true;
    private boolean contractStatus = false;
    private boolean isChangeContract = false;
    //add by ltthuc 2014/06/26
    private boolean isMousePayMentClick = false;
    private boolean edited = false;
    private boolean paymentEdited = false;

    // Thanh start add 2014/07/10 Mashu
    private String integrationId = "";
    private String itemintegrationId = "";
    private String integrationIdClame = "";
    private String itemIntegrationIdClame = "";
    private String courseintegrationId = "";
    // Thanh end add 2014/07/10 Mashu

    /**
     * 伝票入力画面用FocusTraversalPolicy
     */
    //add by ltthuc 2014/06/06
    boolean prepaClassId = false;
    boolean firstCheckHideTab = false;
    boolean payMentIsPrepaClassId = false;
    private static int editFlag;
    //add by ltthuc 2014/06
    //add by ltthuc 2014/06/25
    ArrayList<String> arrPaymentValue;
    ArrayList<Integer> arrPayMentMethodId;
    ArrayList<DataPaymentDetail> arrPayMentMethodName;
    ArrayList<Integer> arrayPrepaId;
    ArrayList<Integer> arrInitPayMentMothod;
    public static ArrayList<Integer> arrPaymentMethodId_1;
    //end add ltthuc
    private InputAccountFocusTraversalPolicy ftp =
            new InputAccountFocusTraversalPolicy();
    private Long sum = 0L;
    private Long sumOfUseValue = 0L;
    boolean blnFlag = false;
    Long sumSaleValues = 0L;
    Long sumUseValues = 0L;
    Long sumRemainValue = 0L;
    private PrepaidData prepaidData;

     MstShopRelations arrMsRelation = new MstShopRelations();

     public ArrayList<DataReservationMainstaff> arrDataReservationMainstaff = new ArrayList<DataReservationMainstaff>();

     public ArrayList<DataSalesMainstaff> lastSaleMainstaffs = new ArrayList<DataSalesMainstaff>();
     MstAccountSetting ms = new MstAccountSetting();
     //IVS_LVTu start add 2015/10/07 New request #43038
     private boolean returnButtonFlag = false;

     //IVS_LVTu start add 2015/11/26 New request #44111
     // only use flag set product_num from table Proposal detail
     private boolean    flagProposalItem   = false;
     private int        productNum         = 0;
     DataProposal dtProposal               = null;
     //IVS_LVTu end add 2015/11/26 New request #44111

    public boolean isReturnButtonFlag() {
        return returnButtonFlag;
    }

    public void setReturnButtonFlag(boolean flag) {
        returnButtonFlag = flag;
    }
    //IVS_LVTu end add 2015/10/07 New request #43038
    /**
     * Creates new form HairInputAccountPanel
     */
    public HairInputAccountPanel() {
        super();

        isLoading = true;
        //add by ltthuc 2014/06/18
        arrPayMentMethodId = new ArrayList<Integer>();
        // IVS SANG START EDIT 20131017
        try {
            //IVS_LVTu start add 2015/10/09 Bug #43295
            // targetShop = SystemInfo.getCurrentShop();
            if (refShop != null && refShop.getShopID() != null ) {
                targetShop = (MstShop) refShop.clone();
                refShop = null;
            }else {
                targetShop = (MstShop) SystemInfo.getCurrentShop().clone();
            }
            //IVS_LVTu end add 2015/10/09 Bug #43295
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();
        addMouseCursorChange();
        this.setSize(933, 691);
        this.setPath("精算管理");
        this.setTitle("レジ精算");
        this.setListener();
        //マスタ一括登録 start
        initComponentsForSelf();
        //マスタ一括登録 end

        initResponse();
        this.init();

        //技術クレームと商品返品のタブを赤文字に
        productDivision.setForegroundAt(2, Color.RED);
        productDivision.setForegroundAt(3, Color.RED);
        //nhanvt start edit 20141119 New request #32601
        // Tom, Origami 以外は非表示
        if (!(SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_origami") || SystemInfo.getDatabase().startsWith("pos_hair_charm")|| SystemInfo.getDatabase().startsWith("pos_hair_alles") )) {
            jLabel3.setVisible(false);
            txtfPioneerCode.setVisible(false);
            cboexPioneerName.setVisible(false);
        }
        //nhanvt end edit 20141119 New request #32601
        isLoading = false;
        //add by ltthuc 2014/07/18;

        //end add by ltthuc
        ia.setShop(targetShop);
        this.loadReceiptSetting();
        //Luc start edit 20150814 #41948
        //pointPanel.setVisible(SystemInfo.isUsePointcard());
        if(!SystemInfo.isUsePointcard()) {
            setting.removeTabAt(1);
        }
        //Luc start edit 20150814 #41948
        if (!SystemInfo.isUseBarcodeReader()) {
            moveButtonPanel1.setVisible(false);
            // connect to BCreader // #18069 移動
            SystemInfo.openBarcodeReaderConnection((BarcodeListener) this);
        }

        //IVS NNTUAN START EDIT 20131028
        // start add 20130805 nakhoa 役務機能使用有無の制御追加
        /*
         if (targetShop.getCourseFlag() != null) {
         if (targetShop.getCourseFlag() == 0) {
         productDivision.remove(jPanel5);
         productDivision.remove(jPanel17);
         }
         } else {
         productDivision.remove(jPanel5);
         productDivision.remove(jPanel17);
         }*/
        // end add 20130805 nakhoa 役務機能使用有無の制御追加
        if (targetShop.getShopID() != 0) {
            if (targetShop.getCourseFlag() != null) {
                if (targetShop.getCourseFlag() == 0) {
                    productDivision.remove(coursePanel);
                    productDivision.remove(consumCoursePanel);
                }
            } else {
                productDivision.remove(coursePanel);
                productDivision.remove(consumCoursePanel);
            }
        }
        //IVS NNTUAN END EDIT 20131028
        //not display
        //Start add 20131030 lvut
        // searchButton.setVisible(false);
        // searchButton1.setVisible(false);
        //End add 20131030 lvut
        //add by ltthuc
        // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
        //nhanvt start edit 20150225 Bug #35219
        initBusinessCategory();
        setVisibleIntegration();
        // IVS_Thanh end add 2014/07/11 Mashu_お会計表示

        if (SystemInfo.getDatabase().equals("pos_hair_missionf")
                || SystemInfo.getDatabase().equals("pos_hair_missionf_dev")) {
            lblPassBookBalance.setVisible(true);
            txtPassBookBalance.setVisible(true);
        } else {
            lblPassBookBalance.setVisible(false);
            txtPassBookBalance.setVisible(false);
        }
        //業態主担当
       jPanel2.setLocation(jPanel5.getLocation().x,jPanel5.getLocation().y);
       //nhanvt start add 20150326 Bug #35729
       ms = SystemInfo.getAccountSetting();
//        try { // #18069 不要な処理
//            ms.load(SystemInfo.getConnection());
            ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
//        } catch (SQLException ex) {
//            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
       ia.setMsSetting(ms);
       //nhanvt end add 20150326 Bug #35729

        if ((SystemInfo.getDatabase().startsWith("pos_hair_natura") || SystemInfo.getDatabase().startsWith("pos_hair_dev"))) {
            this.btCallProposal.setVisible(true);
        }else {
            this.btCallProposal.setVisible(false);
        }
        //IVS_LVTu end add 2015/11/23 New request #44111

        //施術時間の変更 start
        if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
            opeTimePanel.setVisible(true);
            txtCondition.setVisible(true);
            condtionLabel.setVisible(true);
        } else {
            opeTimePanel.setVisible(false);
            txtCondition.setVisible(false);
            condtionLabel.setVisible(false);
        }
        //施術時間の変更 end

    }

    public HairInputAccountPanel(MstShop refShop) {
        this();
        this.targetShop = refShop;
    }

    //Luc start add #20150814
    private void initBusinessCategory() {
        if (ia.getSales().getShop() != null) {
            if(ia.getSales().getShop().getUseShopCategory()!=null) {
                if (ia.getSales().getShop().getUseShopCategory() == 1) {
                    loadMainStaff();
                }else
                {
                    try {
                        //IVS_LVTu start edit 2015/09/01 New request #41948
                        //setting.removeTabAt(0);
                        if ( technicPanel1.isVisible() == true) {
                            setting.remove(technicPanel1);
                        }
                        //IVS_LVTu end edit 2015/09/01 New request #41948
                    }catch (Exception e) {
                    }

                }
                //Luc end edit 20150814 #41948
            }
        }
        if(setting.getTabCount() == 0){
           setting.setVisible(false);
        }
         mainStaffButton.setVisible(false);
    }
    //Luc end add #20150814

    private void initPointPrepaid() {
        //コネクションを取得
        ConnectionWrapper con = SystemInfo.getConnection();

        sumSaleValues = 0L;
        sumUseValues = 0L;
        if (ia.getSales() != null) {
            if (ia.getSales().getCustomer() != null) {
                if (ia.getSales().getCustomer().getCustomerNo() != null) {
                    if (!ia.getSales().getCustomer().getCustomerNo().equals("0")) {
                        String sql = "";
                        if (ia.getSales().getSlipNo() != null) {
                            sql = "  select  sum(sales_value) as sumSales, sum(use_value) as sumUse "
                            +" from data_prepaid d"
                            +" inner join data_sales ds on d.slip_no = ds.slip_no and d.shop_id = ds.shop_id"
                            +" where d.customer_id  = "+SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID())+" and d.delete_date IS NULL"
                            +" and d.slip_no < "+  ia.getSales().getSlipNo() +""
                            +" and ds.sales_date <= (select sales_date from data_sales where slip_no = "+ia.getSales().getSlipNo()+" and shop_id="+ia.getSales().getShop().getShopID()+" )"
                            +" group by d.customer_id  ";

                        } else {
                            sql = "select sum(sales_value) as sumSales, sum(use_value) as sumUse from data_prepaid where customer_id  = "
                                    + SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID()) + " and delete_date IS NULL group by customer_id ";
                        }
                        try {
                            ResultSetWrapper rs = con.executeQuery(sql.toString());
                            if (rs.next()) {
                                sumSaleValues = rs.getLong("sumSales");
                                sumUseValues = rs.getLong("sumUse");
                            }
                            blnFlag = prepaidData.checkDataPrepaid(con, ia.getSales().getSlipNo(), ia.getShop().getShopID());
                            con.close();
                        } catch (Exception e) {
                            System.err.println("SQL = " + sql);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void showPointPrepaid() {

        Long balanceValue = 0L;
        Long sumOfSaleValue = 0L;
       sumRemainValue = sumSaleValues - sumUseValues;
        prepaidData = new PrepaidData();



//        if ((blnFlag == true) && (HairInputAccountPanel.getEditFlag() == 0)) {
//            sumOfUseValue = prepaidData.getUseValue();
//            sumOfSaleValue = prepaidData.getSalesValue();
//            try {
//                balanceValue = sumRemainValue;
//            } catch (Exception e) {
//            }
//            try {
//                balanceValue += sumOfSaleValue;
//            } catch (Exception e) {
//            }
//            try {
//                balanceValue -= sumOfUseValue;
//            } catch (Exception e) {
//            }
//        } else {

        //get number of use but not  self

//                for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
//
//                    if (dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPrepaid() != null) {
//                        Integer id = dpd.getPaymentMethod().getPrepaid();
//
//                        if (id.equals(1)) {
//                            try {
//                                sumOfUseValue += dpd.getPaymentValue().longValue();
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                }
//
//        for (DataSalesDetail dsd : ia.getSales()) {
//            Integer id = dsd.getProduct().getProductClass().getPrepa_class_id();
//            if (id != null && id.equals(1)) {
//                try {
//                    sumOfSaleValue += dsd.getProduct().getPrepaid_price().longValue() * dsd.getProductNum().intValue();
//                } catch (Exception e) {
//                }
//            }
//        }
        try {
            balanceValue = sumRemainValue;
        } catch (Exception e) {
        }
        // vtbphuong start delete 20140828 Bug #29973
//        try {
//            balanceValue += sumOfSaleValue;
//        } catch (Exception e) {
//        }
//        try {
//            balanceValue -= sumOfUseValue;
//        } catch (Exception e) {
//        }
        // vtbphuong end delete 20140828 Bug #29973


        txtPassBookBalance.setText(FormatUtil.decimalFormat(balanceValue));
//        txtPrevRemainValue.setText(String.valueOf(sumRemainValue));
//        txtSalesValue.setText(String.valueOf(sumOfSaleValue));
//        txtUseValue.setText(String.valueOf(sumOfUseValue));
//        txtNowRemainValue.setText(String.valueOf(totalValue));
    }

    /**
     * 一時保存ボタンを不可にする
     */
    public void changeTempRegistBtnNotUse() {
        tempRegistButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        charge = new javax.swing.ButtonGroup();
        registPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        slipNoLabel = new javax.swing.JLabel();
        slipNo = new com.geobeck.swing.JFormattedTextFieldEx();
        customerInfoButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        salesDate = new FSCalenderCombo(SystemInfo.getSystemDate());
        customerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        staff = new javax.swing.JComboBox();
        chargeStaffLabel = new javax.swing.JLabel();
        chargeStaff = new javax.swing.JComboBox();
        changeSosiaGearButton = new javax.swing.JButton();
        staffNo = new javax.swing.JTextField();
        chargeStaffNo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        slipNoLabel1 = new javax.swing.JLabel();
        reservationType = new com.geobeck.swing.JFormattedTextFieldEx();
        searchCustomerButton = new javax.swing.JButton();
        tempRegistButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        mainStaffButton = new javax.swing.JButton();
        productsScrollPane = new javax.swing.JScrollPane();
        products = new com.geobeck.swing.JTableEx();
        productDivision = new javax.swing.JTabbedPane();
        technicPanel = new javax.swing.JPanel();
        technicScrollPane = new javax.swing.JScrollPane();
        technic = new javax.swing.JTable();
        technicClassScrollPane = new javax.swing.JScrollPane();
        technicClass = new javax.swing.JTable();
        integrationLabel3 = new javax.swing.JLabel();
        integration = new javax.swing.JComboBox();
        condtionLabel = new javax.swing.JLabel();
        txtCondition = new javax.swing.JTextField();
        itemPanel = new javax.swing.JPanel();
        itemScrollPane = new javax.swing.JScrollPane();
        item = new javax.swing.JTable();
        itemClassScrollPane = new javax.swing.JScrollPane();
        itemClass = new javax.swing.JTable();
        integrationLabel5 = new javax.swing.JLabel();
        itemintegration = new javax.swing.JComboBox();
        technicClamePanel = new javax.swing.JPanel();
        technicClameScrollPane = new javax.swing.JScrollPane();
        technicClame = new javax.swing.JTable();
        technicClameClassScrollPane = new javax.swing.JScrollPane();
        technicClameClass = new javax.swing.JTable();
        integrationLabel4 = new javax.swing.JLabel();
        integrationClame = new javax.swing.JComboBox();
        itemReturnedPanel = new javax.swing.JPanel();
        itemReturnedScrollPane = new javax.swing.JScrollPane();
        itemReturned = new javax.swing.JTable();
        itemReturnedClassScrollPane = new javax.swing.JScrollPane();
        itemReturnedClass = new javax.swing.JTable();
        integrationLabel6 = new javax.swing.JLabel();
        itemIntegrationClame = new javax.swing.JComboBox();
        coursePanel = new javax.swing.JPanel();
        itemReturnedClassScrollPane2 = new javax.swing.JScrollPane();
        courseClass = new javax.swing.JTable();
        itemReturnedScrollPane2 = new javax.swing.JScrollPane();
        course = new javax.swing.JTable();
        courseintegration = new javax.swing.JComboBox();
        integrationLabel7 = new javax.swing.JLabel();
        consumCoursePanel = new javax.swing.JPanel();
        itemReturnedClassScrollPane3 = new javax.swing.JScrollPane();
        consumptionCourseClass = new javax.swing.JTable();
        itemReturnedScrollPane3 = new javax.swing.JScrollPane();
        consumptionCourse = new javax.swing.JTable();
        prepaidPanel = new javax.swing.JPanel();
        itemClassScrollPane1 = new javax.swing.JScrollPane();
        itemClass1 = new javax.swing.JTable();
        itemScrollPane1 = new javax.swing.JScrollPane();
        item1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        accountRegistButton = new javax.swing.JButton();
        paymentsScrollPane = new javax.swing.JScrollPane();
        payments = new com.geobeck.swing.JTableEx();
        jPanel3 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        changeValue = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel29 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel30 = new javax.swing.JLabel();
        paymentTotal = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        totalValue = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        discountsScrollPane = new javax.swing.JScrollPane();
        discounts = new com.geobeck.swing.JTableEx();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        billValue = new com.geobeck.swing.JFormattedTextFieldEx();
        collectBillButton = new javax.swing.JButton();
        responseItemLabel = new javax.swing.JLabel();
        responseItemComboBox1 = new javax.swing.JComboBox();
        responseItemLabel2 = new javax.swing.JLabel();
        responseItemComboBox2 = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        totalPrice = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        discountValue = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        totalPrice2 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        allDiscountValue = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        taxValue = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblPassBookBalance = new javax.swing.JLabel();
        txtfPioneerCode = new javax.swing.JTextField();
        cboexPioneerName = new javax.swing.JComboBox();
        searchButton = new javax.swing.JButton();
        searchButton1 = new javax.swing.JButton();
        responseItemText1 = new com.geobeck.swing.JFormattedTextFieldEx();
        responseItemText2 = new com.geobeck.swing.JFormattedTextFieldEx();
        txtPassBookBalance = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel3 = new javax.swing.JLabel();
        btCallProposal = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        moveButtonPanel1 = new javax.swing.JPanel();
        txtBarcode = new javax.swing.JTextField();
        slipNoLabel2 = new javax.swing.JLabel();
        discountSettingButton = new javax.swing.JButton();
        setting = new javax.swing.JTabbedPane();
        technicPanel1 = new javax.swing.JPanel();
        searchResultScrollPane = new javax.swing.JScrollPane();
        tblReservationMainStaff = new com.geobeck.swing.JTableEx();
        itemPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        pointPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        genzaiPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel14 = new javax.swing.JLabel();
        pointScrollPane = new javax.swing.JScrollPane();
        point = new com.geobeck.swing.JTableEx();
        jLabel31 = new javax.swing.JLabel();
        opeTimePanel = new javax.swing.JPanel();
        txtOpeMinute = new javax.swing.JTextField();
        ((PlainDocument)txtOpeMinute.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMBER));
        opeTimeLabel = new javax.swing.JLabel();
        txtOpeSecond = new javax.swing.JTextField();
        opeMinuteLabel = new javax.swing.JLabel();
        opeSecondLabel = new javax.swing.JLabel();
        moveButtonPanel = new javax.swing.JPanel();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        countLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(246, 246, 246));
        setFocusCycleRoot(true);
        setOpaque(false);

        registPanel.setOpaque(false);

        org.jdesktop.layout.GroupLayout registPanelLayout = new org.jdesktop.layout.GroupLayout(registPanel);
        registPanel.setLayout(registPanelLayout);
        registPanelLayout.setHorizontalGroup(
            registPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        registPanelLayout.setVerticalGroup(
            registPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setOpaque(false);

        slipNoLabel.setText("伝票No.");

        slipNo.setEditable(false);
        slipNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        slipNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        customerInfoButton.setIcon(SystemInfo.getImageIcon("/button/account/cust_view_off.jpg"));
        customerInfoButton.setBorderPainted(false);
        customerInfoButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/cust_view_on.jpg"));
        customerInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInfoButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("レジ担当");

        jLabel1.setText(" 売上日");

        salesDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        salesDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                salesDateItemStateChanged(evt);
            }
        });
        salesDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salesDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                salesDateFocusLost(evt);
            }
        });

        customerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerNo.setColumns(15);
        customerNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNoFocusLost(evt);
            }
        });
        customerNo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                customerNoPropertyChange(evt);
            }
        });

        customerName1.setEditable(false);
        customerName1.setBorder(null);
        customerName1.setInputKanji(true);
        customerName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerName1ActionPerformed(evt);
            }
        });

        customerName2.setEditable(false);
        customerName2.setBorder(null);
        customerName2.setInputKanji(true);
        customerName2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerName2ActionPerformed(evt);
            }
        });

        staff.setMaximumRowCount(20);
        staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffActionPerformed(evt);
            }
        });

        chargeStaffLabel.setText("主担当");

        chargeStaff.setMaximumRowCount(20);
        chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffActionPerformed(evt);
            }
        });

        changeSosiaGearButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        changeSosiaGearButton.setContentAreaFilled(false);
        changeSosiaGearButton.setEnabled(false);
        changeSosiaGearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSosiaGearButtonActionPerformed(evt);
            }
        });

        staffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                staffNoFocusLost(evt);
            }
        });

        chargeStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chargeStaffNoFocusLost(evt);
            }
        });

        jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        slipNoLabel1.setText(" 予約種別");

        reservationType.setEditable(false);
        reservationType.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        reservationType.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        searchCustomerButton.setIcon(SystemInfo.getImageIcon("/button/account/cust_search_off.jpg"));
        searchCustomerButton.setBorderPainted(false);
        searchCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/cust_search_on.jpg"));
        searchCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCustomerButtonActionPerformed(evt);
            }
        });

        tempRegistButton.setIcon(SystemInfo.getImageIcon("/button/account/tempAccount_off.jpg"));
        tempRegistButton.setBorderPainted(false);
        tempRegistButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/tempAccount_on.jpg"));
        tempRegistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempRegistButtonActionPerformed(evt);
            }
        });

        clearButton.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
        clearButton.setBorderPainted(false);
        clearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        mainStaffButton.setIcon(SystemInfo.getImageIcon("/button/common/category_staff_off.jpg"));
        mainStaffButton.setBorderPainted(false);
        mainStaffButton.setContentAreaFilled(false);
        mainStaffButton.setFocusable(false);
        mainStaffButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/category_staff_on.jpg"));
        mainStaffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainStaffButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(slipNoLabel)
                    .add(searchCustomerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(slipNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(salesDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(slipNoLabel1)
                        .add(2, 2, 2)
                        .add(reservationType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(tempRegistButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(customerInfoButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(5, 5, 5)
                        .add(customerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(8, 8, 8)
                        .add(changeSosiaGearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(chargeStaffLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mainStaffButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(staffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(40, 40, 40))))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {chargeStaff, staff}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.linkSize(new java.awt.Component[] {chargeStaffNo, staffNo}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.linkSize(new java.awt.Component[] {changeSosiaGearButton, jButton1}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(slipNoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(slipNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(salesDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(reservationType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(slipNoLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, tempRegistButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(customerNo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(changeSosiaGearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jPanel1Layout.createSequentialGroup()
                                    .add(1, 1, 1)
                                    .add(customerInfoButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                            .add(7, 7, 7)
                            .add(searchCustomerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(chargeStaffLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, mainStaffButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, staff)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, staffNo)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {changeSosiaGearButton, customerNo, jButton1}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel1Layout.linkSize(new java.awt.Component[] {chargeStaff, staff}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel1Layout.linkSize(new java.awt.Component[] {chargeStaffNo, staffNo}, org.jdesktop.layout.GroupLayout.VERTICAL);

        productsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        productsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "区分", "分類", "商品/技術名/コース名", "単価", "数量", "金額", "割引", "合計", "按分名", "ﾎﾟｲﾝﾄ", "割合", "残", "税率", "NO", "担当者", "指", "AP", "宅送", "施術台", "削除"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true, false, false, true, true, false, true, true, true, true, true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        products.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        products.setSelectionBackground(new java.awt.Color(255, 210, 142));
        products.setSelectionForeground(new java.awt.Color(0, 0, 0));
        products.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(products, SystemInfo.getTableHeaderRenderer());
        products.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        //SelectTableCellRenderer.setSelectTableCellRenderer(products);

        this.initProductsColumn();

        TableColumnModel productsModel = products.getColumnModel();
        productsModel.getColumn(3).setCellEditor(new DoubleCellEditor(new JTextField()));
        //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        productsModel.getColumn(4).setCellEditor(new HairInputAccountCellEditor(new JTextField()));
        //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        productsModel.getColumn(5).setCellEditor(new IntegerCellEditor(new JTextField()));
        productsModel.getColumn(6).setCellEditor(new DoubleCellEditor(new JTextField()));
        //Luc start edit 20150908 #42432
        if(this.getSelectedShop().isProportionally() && this.getSelectedShop().isDisplayProportionally()) {
            productsModel.getColumn(9).setCellEditor(new IntegerCellEditor(new JTextField()));
        }
        //Luc end edit 20150908 #42432
        productsModel.getColumn(10).setCellEditor(new IntegerCellEditor(new JTextField()));
        products.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                productsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                productsFocusLost(evt);
            }
        });
        products.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productsMouseReleased(evt);
            }
        });
        products.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                productsPropertyChange(evt);
            }
        });
        products.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                productsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                productsKeyTyped(evt);
            }
        });
        productsScrollPane.setViewportView(products);
        /*
        products.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (products.getColumnModel().getColumnCount() > 0) {
            products.getColumnModel().getColumn(15).setResizable(false);
        }
        */

        productDivision.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                productDivisionStateChanged(evt);
            }
        });

        technicPanel.setOpaque(false);

        technicScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        technic.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "技術名", "単価"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technic.setSelectionBackground(new java.awt.Color(255, 210, 142));
        technic.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technic.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technic.getTableHeader().setReorderingAllowed(false);
        technic.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(technic, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(technic);
        technic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                technicMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                technicMousePressed(evt);
            }
        });
        technicScrollPane.setViewportView(technic);

        technicClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        technicClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "技術分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technicClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        technicClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technicClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicClass.getTableHeader().setReorderingAllowed(false);
        technicClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(technicClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(technicClass);
        technicClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                technicClassMouseReleased(evt);
            }
        });
        technicClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                technicClassKeyReleased(evt);
            }
        });
        technicClassScrollPane.setViewportView(technicClass);

        integrationLabel3.setBackground(new java.awt.Color(204, 204, 204));
        integrationLabel3.setText("大項目");
        integrationLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        integrationLabel3.setOpaque(true);

        integration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        integration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrationActionPerformed(evt);
            }
        });

        condtionLabel.setText("技術名");

        txtCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConditionActionPerformed(evt);
            }
        });
        txtCondition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtConditionKeyReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout technicPanelLayout = new org.jdesktop.layout.GroupLayout(technicPanel);
        technicPanel.setLayout(technicPanelLayout);
        technicPanelLayout.setHorizontalGroup(
            technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicPanelLayout.createSequentialGroup()
                .add(technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(technicClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(integration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, integrationLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(technicPanelLayout.createSequentialGroup()
                        .add(condtionLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCondition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(115, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, technicScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        technicPanelLayout.setVerticalGroup(
            technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicPanelLayout.createSequentialGroup()
                .add(integrationLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(3, 3, 3)
                .add(integration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(technicClassScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, technicPanelLayout.createSequentialGroup()
                .add(technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(condtionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtCondition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(technicScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        productDivision.addTab("技術選択", technicPanel);

        itemPanel.setOpaque(false);

        itemScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        item.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "商品名", "単価"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        item.setSelectionBackground(new java.awt.Color(255, 210, 142));
        item.setSelectionForeground(new java.awt.Color(0, 0, 0));
        item.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        item.getTableHeader().setReorderingAllowed(false);
        item.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(item, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(item);
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemMouseClicked(evt);
            }
        });
        itemScrollPane.setViewportView(item);

        itemClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        itemClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "商品分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        itemClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemClass.getTableHeader().setReorderingAllowed(false);
        itemClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(itemClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(itemClass);
        itemClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemClassMouseReleased(evt);
            }
        });
        itemClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemClassKeyReleased(evt);
            }
        });
        itemClassScrollPane.setViewportView(itemClass);

        integrationLabel5.setBackground(new java.awt.Color(204, 204, 204));
        integrationLabel5.setText("大項目");
        integrationLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        integrationLabel5.setOpaque(true);

        itemintegration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemintegration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemintegrationActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout itemPanelLayout = new org.jdesktop.layout.GroupLayout(itemPanel);
        itemPanel.setLayout(itemPanelLayout);
        itemPanelLayout.setHorizontalGroup(
            itemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemPanelLayout.createSequentialGroup()
                .add(itemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(itemClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, itemintegration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, integrationLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 322, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        itemPanelLayout.setVerticalGroup(
            itemPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
            .add(itemPanelLayout.createSequentialGroup()
                .add(integrationLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(itemintegration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(itemClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        productDivision.addTab("商品選択", itemPanel);

        technicClameScrollPane.setBackground(new java.awt.Color(255, 255, 204));
        technicClameScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClameScrollPane.getViewport().setOpaque(true);
        //technicClameScrollPane.getViewport().setBackground(new Color(255,255,204));

        technicClame.setForeground(new java.awt.Color(255, 0, 0));
        technicClame.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "技術名", "単価"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technicClame.setSelectionBackground(new java.awt.Color(255, 210, 142));
        technicClame.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technicClame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicClame.getTableHeader().setReorderingAllowed(false);
        technicClame.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(technicClame, SystemInfo.getTableHeaderRenderer());
        //SelectTableCellRenderer.setSelectTableCellRenderer(technicClame);
        SelectTableCellRedRenderer.setSelectTableCellRenderer(technicClame);
        technicClame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                technicClameMouseClicked(evt);
            }
        });
        technicClameScrollPane.setViewportView(technicClame);

        technicClameClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClameClassScrollPane.getViewport().setOpaque(true);
        //technicClameClassScrollPane.getViewport().setBackground(new Color(255,255,204));

        technicClameClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "技術分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technicClameClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        technicClameClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technicClameClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicClameClass.getTableHeader().setReorderingAllowed(false);
        technicClameClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(technicClameClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(technicClameClass);
        technicClameClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                technicClameClassMouseReleased(evt);
            }
        });
        technicClameClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                technicClameClassKeyReleased(evt);
            }
        });
        technicClameClassScrollPane.setViewportView(technicClameClass);

        integrationLabel4.setBackground(new java.awt.Color(204, 204, 204));
        integrationLabel4.setText("大項目");
        integrationLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        integrationLabel4.setOpaque(true);

        integrationClame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        integrationClame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrationClameActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout technicClamePanelLayout = new org.jdesktop.layout.GroupLayout(technicClamePanel);
        technicClamePanel.setLayout(technicClamePanelLayout);
        technicClamePanelLayout.setHorizontalGroup(
            technicClamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicClamePanelLayout.createSequentialGroup()
                .add(technicClamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(technicClameClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(technicClamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(integrationLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(integrationClame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(technicClameScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                .add(0, 0, 0))
        );
        technicClamePanelLayout.setVerticalGroup(
            technicClamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicClameScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
            .add(technicClamePanelLayout.createSequentialGroup()
                .add(integrationLabel4)
                .add(1, 1, 1)
                .add(integrationClame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(technicClameClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        productDivision.addTab("技術クレーム", technicClamePanel);

        itemReturnedScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemReturnedScrollPane.setForeground(new java.awt.Color(236, 233, 216));
        itemReturnedScrollPane.setOpaque(false);
        itemReturnedScrollPane.getViewport().setOpaque(true);
        //itemReturnedScrollPane.getViewport().setBackground(new Color(255,255,204));

        itemReturned.setForeground(new java.awt.Color(255, 0, 0));
        itemReturned.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "商品名", "単価"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemReturned.setOpaque(false);
        itemReturned.setSelectionBackground(new java.awt.Color(255, 210, 142));
        itemReturned.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemReturned.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemReturned.getTableHeader().setReorderingAllowed(false);
        itemReturned.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(itemReturned, SystemInfo.getTableHeaderRenderer());
        //SelectTableCellRenderer.setSelectTableCellRenderer(itemReturned);
        SelectTableCellRedRenderer.setSelectTableCellRenderer(itemReturned);
        itemReturned.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemReturnedMouseClicked(evt);
            }
        });
        itemReturnedScrollPane.setViewportView(itemReturned);

        itemReturnedClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemReturnedClassScrollPane.getViewport().setOpaque(true);
        //itemReturnedClassScrollPane.getViewport().setBackground(new Color(255,255,204));

        itemReturnedClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "商品分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemReturnedClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        itemReturnedClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemReturnedClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemReturnedClass.getTableHeader().setReorderingAllowed(false);
        itemReturnedClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(itemReturnedClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(itemReturnedClass);
        itemReturnedClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemReturnedClassMouseReleased(evt);
            }
        });
        itemReturnedClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemReturnedClassKeyReleased(evt);
            }
        });
        itemReturnedClassScrollPane.setViewportView(itemReturnedClass);

        integrationLabel6.setBackground(new java.awt.Color(204, 204, 204));
        integrationLabel6.setText("大項目");
        integrationLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        integrationLabel6.setOpaque(true);

        itemIntegrationClame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemIntegrationClame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemIntegrationClameActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout itemReturnedPanelLayout = new org.jdesktop.layout.GroupLayout(itemReturnedPanel);
        itemReturnedPanel.setLayout(itemReturnedPanelLayout);
        itemReturnedPanelLayout.setHorizontalGroup(
            itemReturnedPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemReturnedPanelLayout.createSequentialGroup()
                .add(itemReturnedPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(itemReturnedClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(itemReturnedPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(integrationLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(itemIntegrationClame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemReturnedScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );
        itemReturnedPanelLayout.setVerticalGroup(
            itemReturnedPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemReturnedScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
            .add(itemReturnedPanelLayout.createSequentialGroup()
                .add(integrationLabel6)
                .add(1, 1, 1)
                .add(itemIntegrationClame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(itemReturnedClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        productDivision.addTab("商品返品", itemReturnedPanel);

        itemReturnedClassScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemReturnedClassScrollPane.getViewport().setOpaque(true);
        //itemReturnedClassScrollPane.getViewport().setBackground(new Color(255,255,204));

        courseClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "コース分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        courseClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        courseClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        courseClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseClass.getTableHeader().setReorderingAllowed(false);
        courseClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(courseClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(courseClass);
        courseClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                courseClassMouseReleased(evt);
            }
        });
        courseClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                courseClassKeyReleased(evt);
            }
        });
        itemReturnedClassScrollPane2.setViewportView(courseClass);

        itemReturnedScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemReturnedScrollPane2.setForeground(new java.awt.Color(236, 233, 216));
        itemReturnedScrollPane2.setOpaque(false);
        itemReturnedScrollPane.getViewport().setOpaque(true);
        //itemReturnedScrollPane.getViewport().setBackground(new Color(255,255,204));

        course.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "コース名", "契約回数", "単価"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        course.setOpaque(false);
        course.setSelectionBackground(new java.awt.Color(255, 210, 142));
        course.setSelectionForeground(new java.awt.Color(0, 0, 0));
        course.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        course.getTableHeader().setReorderingAllowed(false);
        course.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(course, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRedRenderer.setSelectTableCellRenderer(course);
        course.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                courseMouseClicked(evt);
            }
        });
        itemReturnedScrollPane2.setViewportView(course);
        if (course.getColumnModel().getColumnCount() > 0) {
            course.getColumnModel().getColumn(0).setPreferredWidth(250);
        }

        courseintegration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        courseintegration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseintegrationActionPerformed(evt);
            }
        });

        integrationLabel7.setBackground(new java.awt.Color(204, 204, 204));
        integrationLabel7.setText("大項目");
        integrationLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        integrationLabel7.setOpaque(true);

        org.jdesktop.layout.GroupLayout coursePanelLayout = new org.jdesktop.layout.GroupLayout(coursePanel);
        coursePanel.setLayout(coursePanelLayout);
        coursePanelLayout.setHorizontalGroup(
            coursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(coursePanelLayout.createSequentialGroup()
                .add(coursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(itemReturnedClassScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, coursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, courseintegration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, integrationLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemReturnedScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );
        coursePanelLayout.setVerticalGroup(
            coursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemReturnedScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
            .add(coursePanelLayout.createSequentialGroup()
                .add(integrationLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(courseintegration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(itemReturnedClassScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        productDivision.addTab("コース契約", coursePanel);

        itemReturnedClassScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemReturnedClassScrollPane.getViewport().setOpaque(true);
        //itemReturnedClassScrollPane.getViewport().setBackground(new Color(255,255,204));

        consumptionCourseClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "コース分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        consumptionCourseClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        consumptionCourseClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        consumptionCourseClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consumptionCourseClass.getTableHeader().setReorderingAllowed(false);
        consumptionCourseClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(consumptionCourseClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(consumptionCourseClass);
        consumptionCourseClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                consumptionCourseClassMouseReleased(evt);
            }
        });
        consumptionCourseClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                consumptionCourseClassKeyReleased(evt);
            }
        });
        itemReturnedClassScrollPane3.setViewportView(consumptionCourseClass);

        itemReturnedScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemReturnedScrollPane3.setForeground(new java.awt.Color(236, 233, 216));
        itemReturnedScrollPane3.setOpaque(false);
        itemReturnedScrollPane.getViewport().setOpaque(true);
        //itemReturnedScrollPane.getViewport().setBackground(new Color(255,255,204));

        consumptionCourse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "コース名", "契約日", "残回数"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        consumptionCourse.setOpaque(false);
        consumptionCourse.setSelectionBackground(new java.awt.Color(255, 210, 142));
        consumptionCourse.setSelectionForeground(new java.awt.Color(0, 0, 0));
        consumptionCourse.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consumptionCourse.getTableHeader().setReorderingAllowed(false);
        consumptionCourse.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(consumptionCourse, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRedRenderer.setSelectTableCellRenderer(consumptionCourse);

        DefaultTableCellRenderer column1CellRender = new DefaultTableCellRenderer();
        column1CellRender.setHorizontalAlignment(SwingConstants.CENTER);
        consumptionCourse.getColumnModel().getColumn(1).setCellRenderer(column1CellRender);

        DefaultTableCellRenderer column2CellRender = new DefaultTableCellRenderer();
        column2CellRender.setHorizontalAlignment(SwingConstants.RIGHT);
        consumptionCourse.getColumnModel().getColumn(2).setCellRenderer(column2CellRender);
        consumptionCourse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                consumptionCourseMouseClicked(evt);
            }
        });
        itemReturnedScrollPane3.setViewportView(consumptionCourse);
        if (consumptionCourse.getColumnModel().getColumnCount() > 0) {
            consumptionCourse.getColumnModel().getColumn(0).setPreferredWidth(200);
            consumptionCourse.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        org.jdesktop.layout.GroupLayout consumCoursePanelLayout = new org.jdesktop.layout.GroupLayout(consumCoursePanel);
        consumCoursePanel.setLayout(consumCoursePanelLayout);
        consumCoursePanelLayout.setHorizontalGroup(
            consumCoursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(consumCoursePanelLayout.createSequentialGroup()
                .add(itemReturnedClassScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemReturnedScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );
        consumCoursePanelLayout.setVerticalGroup(
            consumCoursePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemReturnedClassScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
            .add(itemReturnedScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
        );

        productDivision.addTab("コース消化", consumCoursePanel);

        itemClassScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        itemClass1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "商品分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemClass1.setSelectionBackground(new java.awt.Color(255, 210, 142));
        itemClass1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemClass1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemClass1.getTableHeader().setReorderingAllowed(false);
        itemClass1.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(itemClass1, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(itemClass1);
        itemClass1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemClass1MouseReleased(evt);
            }
        });
        itemClass1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemClass1KeyReleased(evt);
            }
        });
        itemClassScrollPane1.setViewportView(itemClass1);

        itemScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        item1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "商品名", "単価"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        item1.setSelectionBackground(new java.awt.Color(255, 210, 142));
        item1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        item1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        item1.getTableHeader().setReorderingAllowed(false);
        item1.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(item1, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(item1);
        item1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item1MouseClicked(evt);
            }
        });
        itemScrollPane1.setViewportView(item1);

        org.jdesktop.layout.GroupLayout prepaidPanelLayout = new org.jdesktop.layout.GroupLayout(prepaidPanel);
        prepaidPanel.setLayout(prepaidPanelLayout);
        prepaidPanelLayout.setHorizontalGroup(
            prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(prepaidPanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(itemClassScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );
        prepaidPanelLayout.setVerticalGroup(
            prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemClassScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, itemScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        productDivision.addTab("パスブック", prepaidPanel);

        jPanel7.setOpaque(false);
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
        });
        jPanel7.setLayout(null);

        accountRegistButton.setIcon(SystemInfo.getImageIcon("/button/account/account_off.png")
        );
        accountRegistButton.setBorderPainted(false);
        accountRegistButton.setContentAreaFilled(false);
        accountRegistButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/account_on.png"));
        accountRegistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountRegistButtonActionPerformed(evt);
            }
        });
        accountRegistButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                accountRegistButtonKeyPressed(evt);
            }
        });
        jPanel7.add(accountRegistButton);
        accountRegistButton.setBounds(700, 7, 100, 50);

        paymentsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        payments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "支払区分", "支払方法", "金額", "残額"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        payments.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        payments.setSelectionBackground(new java.awt.Color(255, 210, 142));
        payments.setSelectionForeground(new java.awt.Color(0, 0, 0));
        payments.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(payments, SystemInfo.getTableHeaderRenderer());
        payments.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(payments);
        TableColumnModel model = payments.getColumnModel();
        model.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        payments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentsMouseClicked(evt);
            }
        });
        payments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                paymentsFocusGained(evt);
            }
        });
        payments.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                paymentsPropertyChange(evt);
            }
        });
        payments.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paymentsKeyPressed(evt);
            }
        });
        paymentsScrollPane.setViewportView(payments);

        jPanel7.add(paymentsScrollPane);
        paymentsScrollPane.setBounds(490, 70, 315, 135);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(null);

        jLabel24.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 20)); // NOI18N
        jLabel24.setText("円");
        jPanel3.add(jLabel24);
        jLabel24.setBounds(190, 60, 20, 20);

        changeValue.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        changeValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        changeValue.setText("0");
        jPanel3.add(changeValue);
        changeValue.setBounds(82, 60, 100, 20);
        jPanel3.add(jSeparator7);
        jSeparator7.setBounds(0, 55, 220, 2);

        jLabel29.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        jLabel29.setText("お釣り");
        jPanel3.add(jLabel29);
        jLabel29.setBounds(10, 60, 60, 20);
        jPanel3.add(jSeparator8);
        jSeparator8.setBounds(0, 80, 220, 2);

        jLabel30.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        jLabel30.setText("お預り");
        jPanel3.add(jLabel30);
        jLabel30.setBounds(10, 35, 60, 20);

        paymentTotal.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        paymentTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paymentTotal.setText("0");
        jPanel3.add(paymentTotal);
        paymentTotal.setBounds(82, 35, 100, 20);

        jLabel27.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 20)); // NOI18N
        jLabel27.setText("円");
        jPanel3.add(jLabel27);
        jLabel27.setBounds(190, 35, 20, 20);

        jPanel4.setBackground(new java.awt.Color(255, 204, 0));
        jPanel4.setLayout(null);

        jLabel15.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        jLabel15.setIcon(SystemInfo.getImageIcon("/button/account/totalValue.jpg"));
        jLabel15.setText("請求金額");
        jPanel4.add(jLabel15);
        jLabel15.setBounds(10, 0, 76, 30);

        totalValue.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        totalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalValue.setText("0");
        jPanel4.add(totalValue);
        totalValue.setBounds(82, 0, 100, 30);

        jLabel12.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 20)); // NOI18N
        jLabel12.setText("円");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(190, 0, 20, 30);

        jPanel3.add(jPanel4);
        jPanel4.setBounds(0, 0, 220, 30);

        jPanel7.add(jPanel3);
        jPanel3.setBounds(250, 120, 220, 100);

        jLabel8.setText("＜全体割引＞");
        jPanel7.add(jLabel8);
        jLabel8.setBounds(0, 75, 200, 14);

        discountsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        discounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "割引名", "金額"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        discounts.setSelectionBackground(new java.awt.Color(255, 210, 142));
        discounts.setSelectionForeground(new java.awt.Color(0, 0, 0));
        discounts.getTableHeader().setReorderingAllowed(false);
        discounts.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        TableColumnModel discountsModel = discounts.getColumnModel();
        discountsModel.getColumn(1).setCellEditor(new DoubleCellEditor(new JTextField()));
        SwingUtil.setJTableHeaderRenderer(discounts, SystemInfo.getTableHeaderRenderer());
        discounts.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                discountsFocusGained(evt);
            }
        });
        discounts.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                discountsPropertyChange(evt);
            }
        });
        discountsScrollPane.setViewportView(discounts);

        jPanel7.add(discountsScrollPane);
        discountsScrollPane.setBounds(0, 90, 200, 47);

        jLabel13.setText("※全体割引は税込での割引となります。");
        jPanel7.add(jLabel13);
        jLabel13.setBounds(0, 140, 198, 14);

        jLabel5.setText("※明細割引後の合計金額からの割引となります。");
        jPanel7.add(jLabel5);
        jLabel5.setBounds(0, 160, 242, 14);

        jLabel4.setText("売掛金");
        jPanel7.add(jLabel4);
        jLabel4.setBounds(0, 180, 40, 25);

        billValue.setEditable(false);
        billValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        billValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel7.add(billValue);
        billValue.setBounds(40, 180, 80, 25);

        collectBillButton.setIcon(SystemInfo.getImageIcon("/button/account/collect_bill_off.jpg"));
        collectBillButton.setBorderPainted(false);
        collectBillButton.setContentAreaFilled(false);
        collectBillButton.setEnabled(false);
        collectBillButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/collect_bill_on.jpg"));
        collectBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collectBillButtonActionPerformed(evt);
            }
        });
        jPanel7.add(collectBillButton);
        collectBillButton.setBounds(130, 180, 92, 25);

        responseItemLabel.setText("反響①");
        jPanel7.add(responseItemLabel);
        responseItemLabel.setBounds(0, 10, 40, 22);

        responseItemComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        responseItemComboBox1.setMaximumSize(new java.awt.Dimension(133, 25));
        responseItemComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                responseItemComboBox1ActionPerformed(evt);
            }
        });
        jPanel7.add(responseItemComboBox1);
        responseItemComboBox1.setBounds(90, 10, 110, 22);

        responseItemLabel2.setText("反響②");
        jPanel7.add(responseItemLabel2);
        responseItemLabel2.setBounds(0, 40, 40, 22);

        responseItemComboBox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        responseItemComboBox2.setMaximumSize(new java.awt.Dimension(133, 25));
        responseItemComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                responseItemComboBox2ActionPerformed(evt);
            }
        });
        jPanel7.add(responseItemComboBox2);
        responseItemComboBox2.setBounds(90, 40, 110, 22);

        jPanel6.setBackground(new java.awt.Color(255, 255, 153));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(null);

        jLabel6.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("明細合計");
        jPanel6.add(jLabel6);
        jLabel6.setBounds(20, 0, 70, 20);

        jPanel7.add(jPanel6);
        jPanel6.setBounds(250, 10, 110, 20);

        jPanel8.setBackground(new java.awt.Color(255, 255, 153));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(null);

        jLabel18.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("明細割引");
        jPanel8.add(jLabel18);
        jLabel18.setBounds(20, 0, 70, 20);

        jPanel7.add(jPanel8);
        jPanel8.setBounds(250, 30, 110, 20);

        jPanel9.setBackground(new java.awt.Color(255, 255, 153));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel9.setLayout(null);

        totalPrice.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 16)); // NOI18N
        totalPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalPrice.setText("0");
        jPanel9.add(totalPrice);
        totalPrice.setBounds(10, 0, 80, 20);

        jLabel7.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("円");
        jPanel9.add(jLabel7);
        jLabel7.setBounds(90, 0, 20, 20);

        jPanel7.add(jPanel9);
        jPanel9.setBounds(360, 10, 110, 20);

        jPanel10.setBackground(new java.awt.Color(255, 255, 153));
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel10.setLayout(null);

        discountValue.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 16)); // NOI18N
        discountValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountValue.setText("0");
        jPanel10.add(discountValue);
        discountValue.setBounds(10, 0, 80, 20);

        jLabel22.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("円");
        jPanel10.add(jLabel22);
        jLabel22.setBounds(90, 0, 20, 20);

        jPanel7.add(jPanel10);
        jPanel10.setBounds(360, 30, 110, 20);

        jPanel11.setBackground(new java.awt.Color(255, 153, 102));
        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel11.setLayout(null);

        totalPrice2.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 16)); // NOI18N
        totalPrice2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalPrice2.setText("0");
        jPanel11.add(totalPrice2);
        totalPrice2.setBounds(10, 0, 80, 20);

        jLabel23.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("円");
        jPanel11.add(jLabel23);
        jLabel23.setBounds(90, 0, 20, 20);

        jPanel7.add(jPanel11);
        jPanel11.setBounds(360, 50, 110, 20);

        jPanel12.setBackground(new java.awt.Color(255, 153, 102));
        jPanel12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(null);

        jLabel19.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("明細売上(税込)");
        jPanel12.add(jLabel19);
        jLabel19.setBounds(5, 0, 100, 20);

        jPanel7.add(jPanel12);
        jPanel12.setBounds(250, 50, 110, 20);

        jPanel13.setBackground(new java.awt.Color(255, 153, 102));
        jPanel13.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel13.setLayout(null);

        jLabel20.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("全体割引(税込)");
        jPanel13.add(jLabel20);
        jLabel20.setBounds(5, 0, 100, 20);

        jPanel7.add(jPanel13);
        jPanel13.setBounds(250, 70, 110, 20);

        jPanel14.setBackground(new java.awt.Color(255, 153, 102));
        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel14.setLayout(null);

        allDiscountValue.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 16)); // NOI18N
        allDiscountValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        allDiscountValue.setText("0");
        jPanel14.add(allDiscountValue);
        allDiscountValue.setBounds(10, 0, 80, 20);

        jLabel25.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("円");
        jPanel14.add(jLabel25);
        jLabel25.setBounds(90, 0, 20, 20);

        jPanel7.add(jPanel14);
        jPanel14.setBounds(360, 70, 110, 20);

        jPanel15.setBackground(new java.awt.Color(255, 255, 153));
        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel15.setLayout(null);

        jLabel21.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("（消費税）");
        jPanel15.add(jLabel21);
        jLabel21.setBounds(20, 0, 70, 20);

        jPanel7.add(jPanel15);
        jPanel15.setBounds(250, 90, 110, 20);

        jPanel16.setBackground(new java.awt.Color(255, 255, 153));
        jPanel16.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel16.setLayout(null);

        taxValue.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 16)); // NOI18N
        taxValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxValue.setText("0");
        jPanel16.add(taxValue);
        taxValue.setBounds(10, 0, 80, 20);

        jLabel26.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("円");
        jPanel16.add(jLabel26);
        jLabel26.setBounds(90, 0, 20, 20);

        jPanel7.add(jPanel16);
        jPanel16.setBounds(360, 90, 110, 20);

        jLabel17.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 15)); // NOI18N
        jLabel17.setText("＜お預り内訳＞");
        jPanel7.add(jLabel17);
        jLabel17.setBounds(490, 46, 110, 20);

        lblPassBookBalance.setText("パスブック残高");
        jPanel7.add(lblPassBookBalance);
        lblPassBookBalance.setBounds(490, 20, 80, 24);

        txtfPioneerCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtfPioneerCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtfPioneerCodeFocusLost(evt);
            }
        });
        jPanel7.add(txtfPioneerCode);
        txtfPioneerCode.setBounds(530, 10, 40, 24);

        cboexPioneerName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cboexPioneerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboexPioneerNameActionPerformed(evt);
            }
        });
        jPanel7.add(cboexPioneerName);
        cboexPioneerName.setBounds(580, 10, 100, 24);

        searchButton.setIcon(SystemInfo.getImageIcon("/button/account/cust_search_off.jpg"));
        searchButton.setBorderPainted(false);
        searchButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/cust_search_on.jpg"));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel7.add(searchButton);
        searchButton.setBounds(206, 10, 40, 25);

        searchButton1.setIcon(SystemInfo.getImageIcon("/button/account/cust_search_off.jpg"));
        searchButton1.setBorderPainted(false);
        searchButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/cust_search_on.jpg"));
        searchButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(searchButton1);
        searchButton1.setBounds(206, 40, 40, 25);

        responseItemText1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        responseItemText1.setColumns(15);
        responseItemText1.setMinimumSize(new java.awt.Dimension(2, 26));
        responseItemText1.setPreferredSize(new java.awt.Dimension(122, 20));
        responseItemText1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                responseItemText1FocusLost(evt);
            }
        });
        responseItemText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                responseItemText1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                responseItemText1KeyReleased(evt);
            }
        });
        jPanel7.add(responseItemText1);
        responseItemText1.setBounds(39, 10, 50, 22);

        responseItemText2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        responseItemText2.setColumns(15);
        responseItemText2.setMinimumSize(new java.awt.Dimension(2, 26));
        responseItemText2.setPreferredSize(new java.awt.Dimension(122, 20));
        responseItemText2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                responseItemText2FocusLost(evt);
            }
        });
        responseItemText2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                responseItemText2KeyPressed(evt);
            }
        });
        jPanel7.add(responseItemText2);
        responseItemText2.setBounds(39, 40, 50, 22);

        txtPassBookBalance.setEditable(false);
        txtPassBookBalance.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtPassBookBalance.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel7.add(txtPassBookBalance);
        txtPassBookBalance.setBounds(580, 20, 100, 25);

        jLabel3.setText("開拓者");
        jPanel7.add(jLabel3);
        jLabel3.setBounds(490, 10, 40, 24);

        btCallProposal.setIcon(SystemInfo.getImageIcon("/button/common/proposal_off.jpg"));
        btCallProposal.setBorderPainted(false);
        btCallProposal.setPressedIcon(SystemInfo.getImageIcon("/button/common/proposal_on.jpg"));
        btCallProposal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCallProposalActionPerformed(evt);
            }
        });
        jPanel7.add(btCallProposal);
        btCallProposal.setBounds(610, 40, 85, 25);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        jLabel16.setText("※APにチェックを入れるとアプローチ成績に反映されます。");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(98, 186, 299, 14);

        moveButtonPanel1.setFocusCycleRoot(true);
        moveButtonPanel1.setOpaque(false);
        moveButtonPanel1.setLayout(null);

        txtBarcode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarcodeActionPerformed(evt);
            }
        });
        moveButtonPanel1.add(txtBarcode);
        txtBarcode.setBounds(70, 0, 80, 20);

        slipNoLabel2.setText("バーコード");
        moveButtonPanel1.add(slipNoLabel2);
        slipNoLabel2.setBounds(0, 0, 70, 20);

        jPanel2.add(moveButtonPanel1);
        moveButtonPanel1.setBounds(0, 0, 150, 25);

        discountSettingButton.setIcon(SystemInfo.getImageIcon("/button/account/discount_off.jpg"));
        discountSettingButton.setBorderPainted(false);
        discountSettingButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/discount_on.jpg"));
        discountSettingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountSettingButtonActionPerformed(evt);
            }
        });
        jPanel2.add(discountSettingButton);
        discountSettingButton.setBounds(0, 178, 92, 25);

        setting.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                settingStateChanged(evt);
            }
        });

        technicPanel1.setOpaque(false);

        searchResultScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        searchResultScrollPane.setFocusable(false);

        tblReservationMainStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "業態名", "スタッフNO", "スタッフ", "指名", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblReservationMainStaff.setSelectionBackground(new java.awt.Color(255, 210, 142));
        tblReservationMainStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblReservationMainStaff.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblReservationMainStaff.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(tblReservationMainStaff, SystemInfo.getTableHeaderRenderer());
        tblReservationMainStaff.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(tblReservationMainStaff);
        JFormattedTextFieldEx staffNoTextFiled = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)staffNoTextFiled.getDocument()).setDocumentFilter(
            new CustomFilter(10, CustomFilter.ALPHAMERIC));
        tblReservationMainStaff.getColumnModel().getColumn(4).setMinWidth(0);
        tblReservationMainStaff.getColumnModel().getColumn(4).setPreferredWidth(0);
        tblReservationMainStaff.getColumnModel().getColumn(4).setMaxWidth(0);
        tblReservationMainStaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReservationMainStaffMouseClicked(evt);
            }
        });
        tblReservationMainStaff.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblReservationMainStaffFocusLost(evt);
            }
        });
        tblReservationMainStaff.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblReservationMainStaffPropertyChange(evt);
            }
        });
        searchResultScrollPane.setViewportView(tblReservationMainStaff);

        org.jdesktop.layout.GroupLayout technicPanel1Layout = new org.jdesktop.layout.GroupLayout(technicPanel1);
        technicPanel1.setLayout(technicPanel1Layout);
        technicPanel1Layout.setHorizontalGroup(
            technicPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicPanel1Layout.createSequentialGroup()
                .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 15, Short.MAX_VALUE))
        );
        technicPanel1Layout.setVerticalGroup(
            technicPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(technicPanel1Layout.createSequentialGroup()
                .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 3, Short.MAX_VALUE))
        );

        setting.addTab("業態主担当設定", technicPanel1);

        itemPanel1.setOpaque(false);

        jPanel5.setOpaque(false);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 340, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 120, Short.MAX_VALUE)
        );

        pointPanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.blue));
        pointPanel.setOpaque(false);

        jLabel9.setText("現在ポイント");

        genzaiPoint.setEditable(false);
        genzaiPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel14.setText("ポイント");

        pointScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pointScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        point.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品・技術名", "使用ポイント", "割引金額"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        point.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        point.setSelectionBackground(new java.awt.Color(255, 210, 142));
        point.setSelectionForeground(new java.awt.Color(0, 0, 0));
        point.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(point, SystemInfo.getTableHeaderRenderer());
        point.setRowHeight(24);

        this.initPointColumn();

        TableColumnModel pointModel = point.getColumnModel();
        pointModel.getColumn(1).setCellEditor(new IntegerCellEditor(new JTextField()));
        pointModel.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        point.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pointPropertyChange(evt);
            }
        });
        pointScrollPane.setViewportView(point);

        jLabel31.setForeground(java.awt.Color.red);
        jLabel31.setText("※ 精算ボタン押下後、割引金額は明細割引に統合されます。");

        org.jdesktop.layout.GroupLayout pointPanelLayout = new org.jdesktop.layout.GroupLayout(pointPanel);
        pointPanel.setLayout(pointPanelLayout);
        pointPanelLayout.setHorizontalGroup(
            pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pointPanelLayout.createSequentialGroup()
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pointPanelLayout.createSequentialGroup()
                        .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pointPanelLayout.createSequentialGroup()
                                .add(150, 150, 150)
                                .add(jLabel9)
                                .add(4, 4, 4)
                                .add(genzaiPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10)
                                .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(pointScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 340, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pointPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 340, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pointPanelLayout.setVerticalGroup(
            pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pointPanelLayout.createSequentialGroup()
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(pointPanelLayout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(genzaiPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(1, 1, 1)
                .add(pointScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel31)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout itemPanel1Layout = new org.jdesktop.layout.GroupLayout(itemPanel1);
        itemPanel1.setLayout(itemPanel1Layout);
        itemPanel1Layout.setHorizontalGroup(
            itemPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(itemPanel1Layout.createSequentialGroup()
                .add(pointPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 341, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(38, 38, 38)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        itemPanel1Layout.setVerticalGroup(
            itemPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, itemPanel1Layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(itemPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pointPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setting.addTab("ポイント割引設定", itemPanel1);

        jPanel2.add(setting);
        setting.setBounds(0, 28, 350, 150);

        opeTimePanel.setFocusCycleRoot(true);
        opeTimePanel.setOpaque(false);
        opeTimePanel.setLayout(null);

        txtOpeMinute.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtOpeMinute.setNextFocusableComponent(txtOpeSecond);
        txtOpeMinute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOpeMinuteActionPerformed(evt);
            }
        });
        opeTimePanel.add(txtOpeMinute);
        txtOpeMinute.setBounds(50, 0, 40, 20);

        opeTimeLabel.setText("施術時間");
        opeTimePanel.add(opeTimeLabel);
        opeTimeLabel.setBounds(0, 0, 50, 20);

        txtOpeSecond.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        ((PlainDocument)txtOpeSecond.getDocument()).setDocumentFilter(
            new SecondFilter());
        txtOpeSecond.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOpeSecondActionPerformed(evt);
            }
        });
        opeTimePanel.add(txtOpeSecond);
        txtOpeSecond.setBounds(110, 0, 30, 20);

        opeMinuteLabel.setText("分");
        opeTimePanel.add(opeMinuteLabel);
        opeMinuteLabel.setBounds(90, 0, 11, 20);

        opeSecondLabel.setText("秒");
        opeTimePanel.add(opeSecondLabel);
        opeSecondLabel.setBounds(140, 0, 11, 20);

        jPanel2.add(opeTimePanel);
        opeTimePanel.setBounds(160, 0, 160, 25);

        moveButtonPanel.setFocusCycleRoot(true);
        moveButtonPanel.setOpaque(false);
        moveButtonPanel.setLayout(null);

        prevButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left_off.jpg"));
        prevButton.setBorderPainted(false);
        prevButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left_on.jpg"));
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });
        moveButtonPanel.add(prevButton);
        prevButton.setBounds(0, 0, 48, 25);

        nextButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right_off.jpg"));
        nextButton.setBorderPainted(false);
        nextButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right_on.jpg"));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        moveButtonPanel.add(nextButton);
        nextButton.setBounds(140, 0, 48, 25);

        countLabel.setFont(new java.awt.Font("MS UI Gothic", 1, 14)); // NOI18N
        countLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        countLabel.setText("9999 / 9999");
        countLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        moveButtonPanel.add(countLabel);
        countLabel.setBounds(50, 0, 90, 25);

        jPanel2.add(moveButtonPanel);
        moveButtonPanel.setBounds(140, 0, 208, 25);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 822, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 813, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(productDivision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 453, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(productsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 805, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(registPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(74, 74, 74)
                                .add(productDivision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, 0)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 205, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(productsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 223, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(154, 154, 154)
                        .add(registPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        productDivision.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    /**
     * コンポーネントの初期化（自動生成以外）
     */
    private void initComponentsForSelf() {
        //マスタ一括登録 start
        if (!SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
            //技術名絞り込み機能無し状態のレイアウトを生成
            org.jdesktop.layout.GroupLayout technicPanelLayout = new org.jdesktop.layout.GroupLayout(technicPanel);
            technicPanel.setLayout(technicPanelLayout);
            technicPanelLayout.setHorizontalGroup(
                technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(technicPanelLayout.createSequentialGroup()
                    .add(technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(technicClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(integration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, integrationLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(technicScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 322, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            technicPanelLayout.setVerticalGroup(
                technicPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(technicScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .add(technicPanelLayout.createSequentialGroup()
                    .add(integrationLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, 0)
                    .add(integration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, 0)
                    .add(technicClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            );
        }
        //マスタ一括登録 end
    }

    private void productsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productsFocusLost

        if (4 <= products.getSelectedColumn() && products.getSelectedColumn() <= 6) {
            if (products.getCellEditor() != null) {
                products.getCellEditor().stopCellEditing();
                products.requestFocusInWindow();
                setEditFlagIs1(temp, products, tempRow, tempCol);
            }
        }

    }//GEN-LAST:event_productsFocusLost

    private void pointPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pointPropertyChange

        if (point.getSelectedRow() >= 0 && point.getSelectedColumn() == 2) {
            this.setTotal();
            this.showCustomerDisplayToSalesValue();
        }

    }//GEN-LAST:event_pointPropertyChange

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed

        nextButton.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.moveSales(true);

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_nextButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed

        prevButton.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.moveSales(false);

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_prevButtonActionPerformed

	private void cboexPioneerNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cboexPioneerNameActionPerformed
	{//GEN-HEADEREND:event_cboexPioneerNameActionPerformed

            MstStaff ms = (MstStaff) cboexPioneerName.getSelectedItem();

            if (ms != null) {
                if (ms.getStaffID() != null) {
                    txtfPioneerCode.setText(ms.getStaffNo());
                    ia.getSales().setPioneerCode(ms.getStaffID());
                } else {
                    txtfPioneerCode.setText("");
                    ia.getSales().setPioneerCode(0);
                }
            }

	}//GEN-LAST:event_cboexPioneerNameActionPerformed

	private void txtfPioneerCodeFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtfPioneerCodeFocusLost
	{//GEN-HEADEREND:event_txtfPioneerCodeFocusLost

            if (!txtfPioneerCode.getText().equals("")) {
                this.setPioneerStaff(txtfPioneerCode.getText());
            } else {
                cboexPioneerName.setSelectedIndex(0);
            }

	}//GEN-LAST:event_txtfPioneerCodeFocusLost

    private void setPioneerStaff(String staffNo) {
        cboexPioneerName.setSelectedIndex(0);

        for (int i = 1; i < cboexPioneerName.getItemCount(); i++) {
            if (((MstStaff) cboexPioneerName.getItemAt(i)).getStaffNo().equals(staffNo)) {
                cboexPioneerName.setSelectedIndex(i);
                break;
            }
        }
    }

    private void accountRegistButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accountRegistButtonKeyPressed
        if ((evt.getKeyCode() == evt.VK_ENTER)
                && (evt.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
            //EnterキーとShiftキーが押下された場合
            //支払方法へ
            this.requestFocusToPayments();
        }

    }//GEN-LAST:event_accountRegistButtonKeyPressed

    private void changeShimeiFreeFlag(boolean flg) {
        if (flg) {
            jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
            jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
            this.shimeiFreeFlag = true;
        } else {
            jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
            jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
            this.shimeiFreeFlag = false;
        }
    }

    private void itemReturnedClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemReturnedClassMouseReleased
        this.showProducts(itemReturnedClass, itemReturned);
    }//GEN-LAST:event_itemReturnedClassMouseReleased

    private void itemReturnedClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemReturnedClassKeyReleased
        this.showProducts(itemReturnedClass, itemReturned);
    }//GEN-LAST:event_itemReturnedClassKeyReleased

    private void itemReturnedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemReturnedMouseClicked

        if ((evt.getClickCount() % 2) == 0) {
            if (0 <= itemReturned.getSelectedRow()) {
                this.addSelectedProduct(4, this.getSelectedProduct(itemReturned));
                this.requestFocusToPayments();
            }
        }

    }//GEN-LAST:event_itemReturnedMouseClicked

    private void technicClameClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_technicClameClassKeyReleased
        this.showProducts(technicClameClass, technicClame);
    }//GEN-LAST:event_technicClameClassKeyReleased

        private void technicClameClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_technicClameClassMouseReleased
        this.showProducts(technicClameClass, technicClame);
        }//GEN-LAST:event_technicClameClassMouseReleased

        private void technicClameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_technicClameMouseClicked

        if ((evt.getClickCount() % 2) == 0) {
            if (0 <= technicClame.getSelectedRow()) {
                this.addSelectedProduct(3, this.getSelectedProduct(technicClame));
                this.requestFocusToPayments();
            }
        }

        }//GEN-LAST:event_technicClameMouseClicked
    private void setMainStaffData() {
        ArrayList<DataReservationMainstaff> reservationMainstaff = new ArrayList<DataReservationMainstaff>();
                //arrDataReservationMainstaff.clear();
                for (int i = 0; i < tblReservationMainStaff.getRowCount(); i++) {
                    DataReservationMainstaff drm = new DataReservationMainstaff();
                    MstShopRelation msr = (MstShopRelation)tblReservationMainStaff.getValueAt(i, 4);
                    drm.setShopCategoryId(msr.getShopCategoryId());
                    JComboBox staffCombo = (JComboBox)tblReservationMainStaff.getValueAt(i, 2);
                    drm.setStaff((MstStaff) staffCombo.getSelectedItem());
                    drm.setDesignated((Boolean) tblReservationMainStaff.getValueAt(i, 3));

                    if (((MstStaff)staffCombo.getSelectedItem()).getStaffID() == null) {
                        continue;
                    }
                    reservationMainstaff.add(drm);
                    //arrDataReservationMainstaff.add(drm);
                }
                arrDataReservationMainstaff = reservationMainstaff;
                ia.setReservationMainstaffs(arrDataReservationMainstaff);
                setDataSaleMainStaff(arrDataReservationMainstaff);
    }
    private void accountRegister(boolean isTempRegister) {

        accountRegistButton.setCursor(null);
        //add by ltthuc 2014/06/26
        if (products.getRowCount() > tempProduct || products.getRowCount() < tempProduct) {
            setEditFlag(1);
        }

        int m;
        m = getEditFlag();
        // end add by ltthuc
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            arrPayMentMethodName = new ArrayList<DataPaymentDetail>();

            arrayPrepaId = new ArrayList<Integer>();
            // JTableで入力後にEnterキーによる確定がされていない場合を考慮して以下の処理を行う
            if (products.getCellEditor() != null) {
                products.getCellEditor().stopCellEditing();
                if (products.getCellEditor() != null) {
                    products.getCellEditor().stopCellEditing();
                    //add by ltthuc 2014/06/20
                    if (getEditFlag() == 0) {
                        setEditFlagIs1(temp, products, tempRow, tempCol);
                    }
                }
            }
            if (discounts.getCellEditor() != null) {
                discounts.getCellEditor().stopCellEditing();
            }
            if (payments.getCellEditor() != null) {
                payments.getCellEditor().stopCellEditing();

                if (getEditFlag() == 0) {
                    setEditFlagIs1(tempPayment, payments, tempPaymentRow, tempPaymentCol);
                }
            }
            //end add by ltthuc
            if (point.getCellEditor() != null) {
                point.getCellEditor().stopCellEditing();
            }

            long cardPayment = 0;
            int row = 0;
            // Start add 20130516 nakhoa
            boolean selectMethod = false;
            Integer paymentMethodIdOle = -1;

            // End add 20130516 nakhoa
            for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
                //add by ltthuc 2014/06/25
                if (getEditFlag() == 0) {
                    if (dpd.getPaymentMethod() != null) {
                        if (!arrInitPayMentMothod.get(row).equals(dpd.getPaymentMethod().getPaymentMethodID())) {
                            setEditFlag(1);
                        }
                    }
                }
                //end add by ltthuc
                if (dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPaymentClass() != null) {
                    //start add 20140612 Task #24575
                    // add dpd to ArrayList<DataPaymentDetail> arrPayMentMethodName
                    arrPayMentMethodName.add(dpd);
                    arrayPrepaId.add(dpd.getPaymentMethod().getPrepaid());
                    //end add by ltthuc
                    Integer id = dpd.getPaymentMethod().getPaymentClassID();
                    dpd.getPaymentValue();
                    //add by ltthuc 2014/06/25



                    // カードまたは電子マネーの場合
                    //IVS_Ptthu start edit Bug #51065
                    if (id != null && (id.equals(2) || id.equals(3))) {
                    //IVS_Ptthu end edit Bug #51065
                        cardPayment += dpd.getPaymentValue().longValue();
                    }
                    // Start add 20130516 nakhoa
                    // Check PaymentMethod IsCredit
                    if (row < payments.getRowCount()) {
                        if (payments.getValueAt(row, 1) instanceof JComboBox) {
                            JComboBox cb = (JComboBox) payments.getValueAt(row, 1);
                            if (cb.getSelectedIndex() > 0) {
                                selectMethod = true;
                            } else {
                                selectMethod = false;
                            }
                        }
                    } else {
                        selectMethod = false;
                    }
                    if (selectMethod && cardPayment > 0 && paymentMethodIdOle != dpd.getPaymentMethod().getPaymentMethodID()) {
                        ia.getSales().addCardName(dpd.getPaymentMethod().getPaymentMethodName());
                        paymentMethodIdOle = dpd.getPaymentMethod().getPaymentMethodID();
                    }
                    // End add 20130516 nakhoa
                }
                row++;
            }

            // カードまたは電子マネーの支払額が請求金額より多い場合
            if (cardPayment != 0 && ia.getTotal(3).getValue().longValue() < cardPayment) {
                MessageDialog.showMessageDialog(this, "カードまたは電子マネーの支払額が請求金額より多くなっています", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                payments.requestFocusInWindow();
                return;
            }

            if (isTempRegister) {
                ia.getSales().setTempFlag(true);
            } else {
                ia.getSales().setTempFlag(false);
            }
            //Luc start add 20141127 New request #33189 [gb]トータルビューティ版お会計の制御変更
            //Luc start edit 20150817 #41948
            //if(SystemInfo.getDatabase().contains("pos_hair_mashu")){
            //    setMainStaffData();
            //}
            if (ia.getSales().getShop() != null) {
                if(ia.getSales().getShop().getUseShopCategory()!=null) {
                    if (ia.getSales().getShop().getUseShopCategory() == 1) {
                        setMainStaffData();
                    }
                }
            }
            //Luc end edit 20150817 #41948

            //Luc end add 20141127 New request #33189 [gb]トータルビューティ版お会計の制御変更
            //入力チェック
            //start change by ltthuc 2014/06/10 Task #24575
            //check value to show message or not
            if ((arrPaymentMethodId_1.contains(1) != true) || ((arrPaymentMethodId_1.contains(1) == true) && checkChoosePrepaidAndInputMoney() == true)) {
                if (this.checkInput()) {
                    SystemInfo.getLogger().log(Level.INFO, "精算用ダイアログ表示");

                    this.hairInputAccountSetup();

                    boolean isNewAccount = slipNo.getText().equals("＜新規＞");

                    //施術時間の変更 start
                    ia.setOpeMinute(txtOpeMinute.getText());
                    ia.setOpeSecond(txtOpeSecond.getText());
                    //施術時間の変更 end

                    // 新規伝票の場合は予約情報を登録する
                    if (isNewAccount) {

                        // スキップお会計の場合は前月以前のデータ更新チェックを行う
                        if (this.getOpener() == null) {
                            if (!checkPrevSalesUpdate(salesDate.getDate())) {
                                MessageDialog.showMessageDialog(
                                        this,
                                        "当月以前の日付は登録できません。",
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }

                        // 一時保存を行い伝票番号を発行する
                        ia.setStatus(2);

                        //一時保存を行う為、ここで一時保存フラグをtrueにする。
                        //ia.getSales().setTempFlag(isTempRegister);
                        ia.getSales().setTempFlag(true);
                        //vtbphuong start add 20131213
                        ia.getSales().setChangePickup(true);
                        //vtbphuong end add 20131213

                        if (!ia.regist()) {
                            return;
                        }

                        slipNo.setText(ia.getSales().getSlipNo().toString());

                        // スキップお会計の場合は予約データを作成する
                        if (this.getOpener() == null) {
                            salesDate.setEnabled(false);
                            RegistReservation rr = new RegistReservation();
                            rr.setShop(this.getSelectedShop());
                            rr.setCustomer(ia.getSales().getCustomer());
                            rr.getReservation().setDesignated(this.shimeiFreeFlag);
                            rr.getReservation().setStaff(ia.getSales().getStaff());
                            GregorianCalendar cal = new GregorianCalendar();

                            cal.set(salesDate.getDate_IntArray()[0],
                                    salesDate.getDate_IntArray()[1] - 1,
                                    salesDate.getDate_IntArray()[2]);

                            //cal.set(Calendar.HOUR_OF_DAY,this.getSelectedShop().getOpenHour());
                            //cal.set(Calendar.MINUTE,this.getSelectedShop().getOpenMinute());

                            rr.getReservation().setSlipNo(Integer.valueOf(slipNo.getText()));
                            rr.getReservation().setVisitTime(cal);
                            rr.getReservation().setStartTime(cal);
                            rr.getReservation().setLeaveTime(cal);

                            DataReservationDetail drd = new DataReservationDetail();
                            drd.setReservationDatetime(cal);
                            drd.getTechnic().setOperationTime(0);
                            rr.getReservation().add(drd);

                            rr.getReservation().setTotalTime(0);
                            rr.getReservation().setStatus(ia.getStatus());
                            rr.getReservation().setSubStatus(ia.getSubStatus());
                            rr.receipt();

                            ia.setSkipSales(true);

                            ia.setReservationNo(rr.getReservation().getReservationNo());
                            ia.getSales().setReservationDatetime(new Timestamp(rr.getReservation().get(0).getReservationDatetime().getTimeInMillis()));

                        }

                    } else {
                        //Luc start edit 20150629 38253
                        //if (ia.getStatus() != 3) {
                        //    //一時保存フラグの再設定
                        //    ia.getSales().setTempFlag(true);
                        //    //vtbphuong start add 20131213
                        //    ia.getSales().setChangePickup(true);
                        //    //vtbphuong end add 20131213
                        //    if (!ia.regist()) {
                        //        return;
                        //    }
                        //}
                        if (ia.getStatus() != 3) {
                            //一時保存フラグの再設定
                            ia.getSales().setTempFlag(true);
                            //vtbphuong start add 20131213
                            ia.getSales().setChangePickup(true);
                            //vtbphuong end add 20131213
                        }

                         if (!ia.regist()) {
                                return;
                        }
                        //Luc end edit 20150629 38253
                    }
                    //施術時間の変更 start add
                    if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
                        if (!ia.updateReservationTime()) {
                            return;
                        }
                    }
                    //施術時間の変更 end add

                    if (isTempRegister) {

                        MessageDialog.showMessageDialog(
                                this,
                                MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                                this.getTitle(),
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {

                        try {
                            if (SystemInfo.isUseCustomerDisplay()) {
                                CustomerDisplay cd = this.getCustomerDisplay();
                                cd.clearScreen();
                                cd.putStr("ｵｼﾊﾗｲ", 'l', 0);
                                cd.putStr(Long.toString(ia.getSales().getPayment(0).getPaymentTotal()), 'r', 0);
                                cd.putStr("ｵﾂﾘ", 'l', 1);
                                cd.putStr(Long.toString(ia.getSales().getPayment(0).getChangeValue()), 'r', 1);
                            }
                        } catch (Exception e) {
                        }

                        //------------------------
                        // 退店画面表示
                        //------------------------

                        ia.getSales().setTempFlag(false);

                        // ポイント割引設定の値を反映
                        long discountPoint = 0l;
                        if (SystemInfo.isUsePointcard() && point.getRowCount() > 0) {
                            for (int i = 0; i < point.getRowCount(); i++) {
                                discountPoint += Long.valueOf(point.getValueAt(i, 1).toString()).longValue();
                            }
                        }

                        //IVS NNTUAN START 20131129
                        //IVS_LVTu start edit 2017/09/18 #25966 [gb]基本設定＞会社関連＞店舗情報登録に商品受渡機能使用フラグの追加
                        if (SystemInfo.getCurrentShop().getDeliveryFlag()!= null
                                && SystemInfo.getCurrentShop().getDeliveryFlag().intValue() == 1) {
                        //IVS_LVTu end edit 2017/09/18 #25966 [gb]基本設定＞会社関連＞店舗情報登録に商品受渡機能使用フラグの追加
                            boolean flagDilivery = false;
                            MstDataDeliveryProduct sm = new MstDataDeliveryProduct();
                            ArrayList<MstDataDeliveryProduct> listProduct = new ArrayList<MstDataDeliveryProduct>();
                            listProduct = sm.load(SystemInfo.getConnection(), ia.getSales().getCustomer().getCustomerID(), SystemInfo.getCurrentShop().getShopID(), ia.getSales().getSlipNo());
                            if (!listProduct.isEmpty()) {
                                flagDilivery = true;
                            }
                            // start add 20131209 vtbphuong 商品受渡

                            if (flagDilivery) {
                                // HairInputAccountProductDelivery hicpd = new HairInputAccountProductDelivery(ia.getSales().getCustomer(), ia.getSales().getSlipNo(), false);
                                HairInputAccountProductDelivery hicpd = new HairInputAccountProductDelivery(ia.getSales().getCustomer(), ia.getSales().getSlipNo(), false, ia.getShop().getShopID());
                                SwingUtil.openAnchorDialog(null, true, hicpd, "【商品受渡画面】", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
                                hicpd.setVisible(false);
                                ((JDialog) hicpd.getParent().getParent().getParent().getParent()).dispose();
                                sm = null;
                                hicpd = null;
                            }
                            // endadd 20131209 vtbphuong 商品受渡
                        }
                        //IVS NNTUAN END 20131129


                        HairInputAccountDetailDialog dialog = new HairInputAccountDetailDialog(parentFrame, true, ia, discountPoint);
                        dialog.setNewAccount(isNewAccount);

                        // 退店画面を開く前にポイント割引設定をクリアし明細行を再表示する（既に明細割引に統合済みのため）
                        if (SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 0 || SystemInfo.getPointOutputType() == 2)) {
                            SwingUtil.clearTable(products);
                            detailCount = 0;
                            arrPaymentMethodId_1 = new ArrayList<Integer>();
                            for (DataSalesDetail dsd : ia.getSales()) {

                                this.addProductRow(dsd, true, false);
                            }
                        }
                        dialog.setCustomerDisplay(this.getCustomerDisplay());
                        dialog.setVisible(true);

                        if (!dialog.isClose()) {
                            this.init();

                            //伝票検索から開かれている場合
                            if (this.getOpener() instanceof SearchAccountPanel) {
                                SearchAccountPanel sap = (SearchAccountPanel) this.getOpener();
                                sap.searchAccount();
                                sap.showAccount();
                            }

                            if (this.getOpener() instanceof SearchAccountDialog) {
                                SearchAccountDialog sad = (SearchAccountDialog) this.getOpener();
                                sad.searchAccount();
                                sad.showAccount();
                            }
                            this.showOpener();

                            // フロント画面更新
                            if (this.getOpener() instanceof ReservationStatusPanel) {
                                ((ReservationStatusPanel) this.getOpener()).refresh();
                            }

                        }
                        dialog.dispose();

                    }

                    productDivision.requestFocusInWindow();
                    //コース消化に表示されているリストをクリア
                    SwingUtil.clearTable(consumptionCourseClass);
                    SwingUtil.clearTable(consumptionCourse);
                    //コース契約前のコース分類Mapを空にする
                    consumptionCouserClassMap.clear();
                    //コース契約前のコースMapを空にする
                    consumptionCourseMap.clear();
                    //IVS NNTUAN START ADD 20131028
                    stateChange();
                    //IVS NNTUAN END ADD 20131028
                    if(isTempRegister){
                        loadTempData(ia.getShop(),
                                        ia.getSales().getSlipNo(),
                                        ia.reservationNo,
                                        ia.getSubStatus());
                        SwingUtil.clearTable(products);
                        showData();
                        salesDate.setEnabled(false);
                    }

                }

            } else if (arrPaymentMethodId_1.contains(1) == true && checkChoosePrepaidAndInputMoney() == false) {
                //if(arrPayMentMethodName.contains(paymentMethodName)==false){
                MessageDialog.showMessageDialog(this,
                        "プリペイド限定商品が選択されている場合、支払方法にプリペイドを選択してください",
                        this.getTitle(), JOptionPane.ERROR_MESSAGE);
            }
            //end change by ltthuc
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

        private void accountRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountRegistButtonActionPerformed
        changeCus = false;
        this.accountRegister(false);

        }//GEN-LAST:event_accountRegistButtonActionPerformed

	private void productsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productsKeyPressed
            if (products.getSelectedRow() == products.getRowCount() - 1
                    && products.getSelectedColumn() == 6
                    && evt.getKeyCode() == evt.VK_ENTER) {

                if (tempRegistButton.isVisible()) {
                    payments.setColumnSelectionInterval(2, 2);
                    payments.setRowSelectionInterval(0, 0);
                    payments.requestFocusInWindow();
                    products.setColumnSelectionAllowed(false);
                    products.setRowSelectionAllowed(false);
                }
            }
            //add by ltthuc 2014/06/20
            if (temp != null && evt.getKeyCode() == evt.VK_ENTER) {
                if (products.getCellEditor() != null) {
                    products.getCellEditor().stopCellEditing();

                    if (getEditFlag() == 0) {
                        setEditFlagIs1(temp, products, tempRow, tempCol);
                    }
                }
                //end add by ltthuc
            }

	}//GEN-LAST:event_productsKeyPressed

	private void paymentsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentsFocusGained
            if (payments.getInputContext() != null) {
                payments.getInputContext().setCharacterSubsets(null);

            }
	}//GEN-LAST:event_paymentsFocusGained

	private void paymentsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_paymentsPropertyChange
            this.changePaymentValue();
	}//GEN-LAST:event_paymentsPropertyChange

	private void technicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_technicMouseClicked

            if ((evt.getClickCount() % 2) == 0) {
                if (0 <= technic.getSelectedRow()) {
                    this.addSelectedProduct(1, this.getSelectedProduct(technic));
                    this.requestFocusToPayments();
                }
            }

	}//GEN-LAST:event_technicMouseClicked

	private void technicClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_technicClassKeyReleased
        this.showProducts(technicClass, technic);
}//GEN-LAST:event_technicClassKeyReleased

private void technicClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_technicClassMouseReleased
        this.showProducts(technicClass, technic);
}//GEN-LAST:event_technicClassMouseReleased

private void itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemMouseClicked

    if ((evt.getClickCount() % 2) == 0) {
        if (0 <= item.getSelectedRow()) {
            this.addSelectedProduct(2, this.getSelectedProduct(item));
            this.requestFocusToPayments();
        }
    }

}//GEN-LAST:event_itemMouseClicked

private void itemClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemClassKeyReleased
    this.showProducts(itemClass, item);
}//GEN-LAST:event_itemClassKeyReleased

private void itemClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemClassMouseReleased
    this.showProducts(itemClass, item);
}//GEN-LAST:event_itemClassMouseReleased

private void productDivisionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_productDivisionStateChanged
    stateChange();
}//GEN-LAST:event_productDivisionStateChanged

private void stateChange() {
    if (productDivision.getSelectedIndex() == 0) {
        this.showProducts(technicClass, technic);
    } else if (productDivision.getSelectedIndex() == 1) {
        prepaClassId = false;
        this.showProducts(itemClass, item);

    } else if (productDivision.getSelectedIndex() == 2) {
        this.showProducts(technicClameClass, technicClame);
    } else if (productDivision.getSelectedIndex() == 3) {
        this.showProducts(itemReturnedClass, itemReturned);
    } else if (productDivision.getSelectedIndex() == 4) {
        this.showCourse(courseClass, course);
    } else if (productDivision.getSelectedIndex() == 5) {
        if (ia.getSales().getCustomer() != null && ia.getSales().getCustomer().getCustomerID() != null) {
            //顧客が選択されている場合のみ販売データからコースの購入・消化状況を取得
            try {
                //購入コース取得
                ConnectionWrapper con = SystemInfo.getConnection();
                //vtbphuong start change 20140425 Bug #22496
                if (salesDate.getDate() == null) {
                    consumptionCourseClasses.loadConsumptionCourseClass(con, this.getSelectedShopID(), ia.getSales().getCustomer().getCustomerID());
                } else {
                    consumptionCourseClasses.loadConsumptionCourseClass(con, this.getSelectedShopID(), ia.getSales().getCustomer().getCustomerID(), salesDate.getDate());
                }

                //vtbphuong end change 20140425 Bug #22496
                this.setConsumptionDataSales(consumptionCourseClasses, consumptionCourseClass);

                this.showConsumptionCourse(consumptionCourseClass, consumptionCourse);
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    } //add by ltthuc 2014/06/05
    else if (productDivision.getSelectedIndex() == 6) {
        prepaClassId = true;
        showProducts(itemClass1, item1);

    }
}
/**
 * レスポンス項目別タイトルを取得する
 */
private String getResponseIssueTitle(DataResponseIssue dri) {
    return ((dri.getResponse().getCirculationType() == 1) ? getStaffName(dri) : getCirculationMonthlyDate(dri));
}

/**
 * スタッフ名を取得する
 */
private String getStaffName(DataResponseIssue dri) {
    String staffName = "";
    if (dri.getStaff() == null) {
        staffName = "???? ????";
    }
    staffName = dri.getStaff().toString();

    return staffName + String.format("(%1$tY/%1$tm/%1$td)", dri.getRegistDate());
}

/**
 * 発行年月を取得する
 */
private String getCirculationMonthlyDate(DataResponseIssue dri) {
    if (dri == null) {
        return "??/??";
    }
    return String.format("%1$tY/%1$tm号", dri.getCirculationMonthlyDate());
}

private void collectBillButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_collectBillButtonActionPerformed
{//GEN-HEADEREND:event_collectBillButtonActionPerformed
    //売掛が１件だけ
    if (ia.getBill().getSlipNo() != null) {
        SystemInfo.getLogger().log(Level.INFO, "売掛回収");
        CollectBillPanel cbp = new CollectBillPanel();
        cbp.setOpener(this);
        cbp.init(ia.getBill().getSlipNo());
        //nhanvt start add 20150311 Bug #35485
        BillsList bl = new BillsList();
        bl.setTermFrom(null);
        bl.setTermTo(null);
        bl.setSlipNo(ia.getBill().getSlipNo());
        bl.load2(ia.getSales().getCustomer().getCustomerID());
        cbp.init1(bl);
         //nhanvt end add 20150311 Bug #35485
        this.setVisible(false);
        parentFrame.changeContents(cbp);
    } else {
        SystemInfo.getLogger().log(Level.INFO, "売掛一覧");
        HairBillsListPanel blp = new HairBillsListPanel(ia.getSales().getCustomer().getCustomerID());
        blp.setOpener(this);
        this.setVisible(false);
        parentFrame.changeContents(blp);
    }

    this.setBill();
    // レスポンス
    setResponse();


}//GEN-LAST:event_collectBillButtonActionPerformed

private void discountsFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_discountsFocusGained
{//GEN-HEADEREND:event_discountsFocusGained
    if (discounts.getInputContext() != null) {
        discounts.getInputContext().setCharacterSubsets(null);
    }
}//GEN-LAST:event_discountsFocusGained

private void discountsPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_discountsPropertyChange
{//GEN-HEADEREND:event_discountsPropertyChange
    this.changeDiscountValue();
}//GEN-LAST:event_discountsPropertyChange

private void productsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productsFocusGained
    if (products.getInputContext() != null) {

        products.getInputContext().setCharacterSubsets(null);

    }
}//GEN-LAST:event_productsFocusGained

private void productsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_productsPropertyChange
    this.changeProducts();
    //IVS_LVTu start edit 2015/06/09 #36636

    int col = products.getSelectedColumn();
    int row = products.getSelectedRow();

    if ( col < 0 || row<0) {
        return;
    }
    if ( products.getValueAt(row, col) instanceof JComboBox ) {
        return;
    }
    if ( products.getValueAt(row, col) instanceof JCheckBox ) {
        return;
    }
    this.setTotal();

    //int row = products.getSelectedRow();
    //int col = products.getSelectedColumn();
    //IVS_LVTu end edit 2015/06/09 #36636
    if (row >= 0 && 4 <= col && col <= 6) {
        this.showCustomerDisplayToSalesValue();
    }
    //showPointPrepaid();

}//GEN-LAST:event_productsPropertyChange

    private void txtBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarcodeActionPerformed

    if (txtBarcode.getText().length() > 0) {
        this.setBarcodeProduct(txtBarcode.getText());
        txtBarcode.setText("");
        txtBarcode.requestFocusInWindow();
    }

}//GEN-LAST:event_txtBarcodeActionPerformed

    private void productsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsMouseReleased
    int col = products.getSelectedColumn();
    //IVS_LVTu start edit 2015/05/11 Bug #36644
    int row =  products.getSelectedRow();
//    if (col != 11 && col != 14) {
//        products.requestFocusInWindow();
//    }

        try {
            if (!(products.getValueAt(row, col) instanceof JComboBox)) {
                products.requestFocusInWindow();
            }
        }catch (Exception e) {

        }
        //IVS_LVTu end edit 2015/05/11 Bug #36644
    }//GEN-LAST:event_productsMouseReleased

    private void discountSettingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountSettingButtonActionPerformed

    discountSettingButton.setCursor(null);

    try {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        DiscountSettingDialog ds = new DiscountSettingDialog(parentFrame, true);
        ds.setVisible(true);

        if (ds.isOK()) {

            int discountDivision = 0;
            if (ds.getDiscountDivision() == DiscountSettingDialog.DISCOUNT_DIVISION_TECHNIC) {
                discountDivision = 1;
            } else if (ds.getDiscountDivision() == DiscountSettingDialog.DISCOUNT_DIVISION_PRODUCT) {
                discountDivision = 2;
            }
            // vtbphuong start add 20150511
            else if (ds.getDiscountDivision() == DiscountSettingDialog.DISCOUNT_DIVISION_COURSE) {
                discountDivision = 5;
            }
            // vtbphuong end add 20150511

            double value = 0;
            if (ds.getDiscountMethod() == DiscountSettingDialog.DISCOUNT_METHOD_VALUE) {
                // 金額
                value = ds.getDiscountValue();
            } else {
                // パーセント
                value = ds.getDiscountRate() / 100d;
            }

            for (int row = 0; row < products.getRowCount(); row++) {
                String s = ((JComboBox) products.getValueAt(row, 0)).getSelectedItem().toString();
                if (discountDivision == 1) {
                    if (s.equals("技術")) {
                        products.setValueAt(value, row, 6);
                    }
                }else if (discountDivision == 2) {
                    if (s.equals("商品")) {
                        products.setValueAt(value, row, 6);
                    }
                }
                // vtbphuong start add 20150511
                else if (discountDivision == 5) {
                    if (s.equals("契約")) {
                        products.setValueAt(value, row, 6);
                    }
                }
                // vtbphuong end add 20150511
                else {
                    // vtbphuong start add   20150604
                     if (!s.equals("消化")) {
                          products.setValueAt(value, row, 6);
                     }
                    // vtbphuong end add   20150604
                   // products.setValueAt(value, row, 6);
                }

                products.changeSelection(row, 6, false, false);
                this.changeProducts();
            }

            this.setTotal();
            this.showCustomerDisplayToSalesValue();
        }

        ds = null;

    } finally {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    }//GEN-LAST:event_discountSettingButtonActionPerformed

    private void courseClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseClassMouseReleased
    this.showCourse(courseClass, course);
    }//GEN-LAST:event_courseClassMouseReleased

    private void courseClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_courseClassKeyReleased
    this.showCourse(courseClass, course);
    }//GEN-LAST:event_courseClassKeyReleased

    private void courseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseMouseClicked
    if ((evt.getClickCount() % 2) == 0) {
        if (0 <= course.getSelectedRow()) {
            this.addSelectedCourse(5, this.getSelectedCourse(course));
            //コース契約を選択したら購入してなくても消化できるようにコース消化リストに追加する
            this.addConsumptionCourse(consumptionCourseClass, consumptionCourse, this.getSelectedCourse(course));
            this.requestFocusToPayments();
        }
    }
    }//GEN-LAST:event_courseMouseClicked

    private void consumptionCourseClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consumptionCourseClassMouseReleased
    this.showConsumptionCourse(consumptionCourseClass, consumptionCourse);
    }//GEN-LAST:event_consumptionCourseClassMouseReleased

    private void consumptionCourseClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_consumptionCourseClassKeyReleased
    this.showConsumptionCourse(consumptionCourseClass, consumptionCourse);
    }//GEN-LAST:event_consumptionCourseClassKeyReleased

    private void consumptionCourseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consumptionCourseMouseClicked
    if ((evt.getClickCount() % 2) == 0) {
        if (0 <= consumptionCourse.getSelectedRow()) {
            this.addSelectedConsumptionCourse(6, this.getSelectedConsumptionCourse(consumptionCourse));
            this.requestFocusToPayments();
        }
    }
    }//GEN-LAST:event_consumptionCourseMouseClicked

private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
    SystemInfo.getLogger().log(Level.INFO, "反響項目検索");
    MstSearchItemResponsePanel searchItemResponse = new MstSearchItemResponsePanel();
    SwingUtil.openAnchorDialog(null, true, searchItemResponse, "【反響項目検索】", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
    searchItemResponse.setVisible(true);
    boolean flag = false;
    String responseNo = "";
    responseNo = searchItemResponse.getResponNo();
    int responeId = 0;
    responeId = searchItemResponse.getResponId();
    MstResponse temp = new MstResponse();
    if (responeId != 0) {
        for (int index = 1; index < responseItemComboBox1.getItemCount(); index++) {
            temp = new MstResponse();
            temp = (MstResponse) this.responseItemComboBox1.getItemAt(index);
            if (temp.getResponseID() == responeId) {
                this.responseItemComboBox1.setSelectedIndex(index);
                this.responseItemText1.setText(responseNo);
                responseNo1 = responseNo;
                flag = true;
                break;
            }
        }
        if (!flag) {
            this.responseItemText1.setText("");
            responseNo1 = "";
            this.responseItemComboBox1.setSelectedIndex(0);
        }
    }
    searchItemResponse.dispose();
    searchItemResponse = null;
}//GEN-LAST:event_searchButtonActionPerformed

private void searchButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButton1ActionPerformed
    SystemInfo.getLogger().log(Level.INFO, "反響項目検索");
    MstSearchItemResponsePanel searchItemResponse = new MstSearchItemResponsePanel();
    SwingUtil.openAnchorDialog(null, true, searchItemResponse, "【反響項目検索】", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
    searchItemResponse.setVisible(true);
    boolean flag = false;
    String responseNo = "";
    responseNo = searchItemResponse.getResponNo();
    int responeId = 0;
    responeId = searchItemResponse.getResponId();
    MstResponse temp = new MstResponse();
    if (responeId != 0) {
        for (int index = 1; index < responseItemComboBox2.getItemCount(); index++) {
            temp = new MstResponse();
            temp = (MstResponse) this.responseItemComboBox2.getItemAt(index);
            if (temp.getResponseID() == responeId) {
                this.responseItemComboBox2.setSelectedIndex(index);
                this.responseItemText2.setText(responseNo);
                responseNo2 = responseNo;
                flag = true;
                break;
            }
        }
        if (!flag) {
            this.responseItemText2.setText("");
            responseNo2 = "";
            this.responseItemComboBox2.setSelectedIndex(0);
        }
    }
    searchItemResponse.dispose();
    searchItemResponse = null;
}//GEN-LAST:event_searchButton1ActionPerformed

private void responseItemText1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_responseItemText1FocusLost
    boolean flag = false;
    if (responseNo1.equals(responseItemText1.getText().trim())) {
        return;
    }
    if (!"".equals(responseItemText1.getText().trim())) {
        String responeNo = responseItemText1.getText().trim();
        for (int index = 1; index < this.responseItemComboBox1.getItemCount(); index++) {
            MstResponse temp = new MstResponse();
            temp = (MstResponse) this.responseItemComboBox1.getItemAt(index);
            if (temp.getResponseNo().trim().equals(responeNo)) {
                this.responseItemComboBox1.setSelectedIndex(index);
                responseNo1 = responeNo;
                flag = true;
                break;
            }
        }
        if (!flag) {
            this.responseItemText1.setText("");
            responseNo1 = "";
            this.responseItemComboBox1.setSelectedIndex(0);
        }
    } else {
        this.responseItemText1.setText("");
        responseNo1 = "";
        this.responseItemComboBox1.setSelectedIndex(0);
    }
}//GEN-LAST:event_responseItemText1FocusLost

private void responseItemText1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_responseItemText1KeyPressed
    if ((evt.getKeyCode() == evt.VK_ENTER)) {
        responseItemText2.requestFocusInWindow();
    }
}//GEN-LAST:event_responseItemText1KeyPressed

private void responseItemText1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_responseItemText1KeyReleased
    if ((evt.getKeyCode() == evt.VK_ENTER)) {
        responseItemText2.requestFocusInWindow();
    }
}//GEN-LAST:event_responseItemText1KeyReleased

private void responseItemText2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_responseItemText2FocusLost
    boolean flag = false;
    if (responseNo2.equals(responseItemText2.getText().trim())) {
        return;
    }
    if (!"".equals(responseItemText2.getText().trim())) {
        String responeNo = responseItemText2.getText().trim();
        for (int index = 1; index < this.responseItemComboBox2.getItemCount(); index++) {
            MstResponse temp = new MstResponse();
            temp = (MstResponse) this.responseItemComboBox2.getItemAt(index);
            if (temp.getResponseNo().trim().equals(responeNo)) {
                this.responseItemComboBox2.setSelectedIndex(index);
                responseNo2 = responeNo;
                flag = true;
                break;
            }
        }
        if (!flag) {
            this.responseItemText2.setText("");
            responseNo2 = "";
            this.responseItemComboBox2.setSelectedIndex(0);
        }
    } else {
        this.responseItemText2.setText("");
        responseNo2 = "";
        this.responseItemComboBox2.setSelectedIndex(0);
    }
}//GEN-LAST:event_responseItemText2FocusLost

private void responseItemText2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_responseItemText2KeyPressed
    if ((evt.getKeyCode() == evt.VK_ENTER)) {
        responseItemComboBox2.requestFocusInWindow();
    }
}//GEN-LAST:event_responseItemText2KeyPressed

private void responseItemComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_responseItemComboBox1ActionPerformed
    // TODO add your handling code here:
    MstResponse temp = new MstResponse();
    temp = (MstResponse) this.responseItemComboBox1.getSelectedItem();
    if (this.responseItemComboBox1.getSelectedIndex() > 0) {
        this.responseItemText1.setText(temp.getResponseNo());
        responseNo1 = temp.getResponseNo();
    } else {
        this.responseItemText1.setText("");
    }

}//GEN-LAST:event_responseItemComboBox1ActionPerformed

private void responseItemComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_responseItemComboBox2ActionPerformed
    // TODO add your handling code here:
    MstResponse temp = new MstResponse();
    temp = (MstResponse) this.responseItemComboBox2.getSelectedItem();
    if (this.responseItemComboBox2.getSelectedIndex() > 0) {
        this.responseItemText2.setText(temp.getResponseNo());
        responseNo2 = temp.getResponseNo();
    } else {
        this.responseItemText2.setText("");
    }
}//GEN-LAST:event_responseItemComboBox2ActionPerformed

private void itemClass1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemClass1MouseReleased
    // TODO add your handling code here:
    showProducts(itemClass1, item1);
}//GEN-LAST:event_itemClass1MouseReleased

private void itemClass1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemClass1KeyReleased
    // TODO add your handling code here:
    showProducts(itemClass1, item1);
}//GEN-LAST:event_itemClass1KeyReleased

private void item1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item1MouseClicked
    if ((evt.getClickCount() % 2) == 0) {
        if (0 <= item1.getSelectedRow()) {
            this.addSelectedProduct(2, this.getSelectedProduct(item1));
            this.requestFocusToPayments();
          //  showPointPrepaid();
        }
    }
}//GEN-LAST:event_item1MouseClicked

private void productsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsMouseClicked
    // add by ltthuc 2014/06/17

    products.getInputContext().setCharacterSubsets(null);
    setEditFlagIs1(temp, products, tempRow, tempCol);

    tempRow = products.getSelectedRow();
    tempCol = products.getSelectedColumn();
    if (tempRow != -1 && products.getValueAt(tempRow, tempCol) != null) {
        Object o = products.getValueAt(tempRow, tempCol);
        temp = o.toString();
    }

    //end add by ltthuc
}//GEN-LAST:event_productsMouseClicked

private void paymentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentsMouseClicked
    // add by ltthuc 2014/06/17
    isMousePayMentClick = true;
    if (tempPayment != null) {
        setEditFlagIs1(tempPayment, payments, tempPaymentRow, tempPaymentCol);
    }
    tempPaymentRow = payments.getSelectedRow();
    tempPaymentCol = payments.getSelectedColumn();
    Object o = payments.getValueAt(tempPaymentRow, tempPaymentCol);
    if (!(o instanceof JButton)) {
        tempPayment = o.toString();
    }
    //end add by ltthuc
}//GEN-LAST:event_paymentsMouseClicked

private void productsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productsKeyTyped
    // TODO add your handling code here:
}//GEN-LAST:event_productsKeyTyped

private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
    // TODO add your handling code here:
}//GEN-LAST:event_jPanel7MouseClicked

private void technicMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_technicMousePressed
    // TODO add your handling code here:
}//GEN-LAST:event_technicMousePressed

private void productsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productsKeyReleased
    //add by ltthuc 2014/06/19
    //end add by ltthuc
}//GEN-LAST:event_productsKeyReleased

private void paymentsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentsKeyPressed
    //add by ltthuc 2014/06/20
    if (tempPayment != null && evt.getKeyCode() == evt.VK_ENTER) {
        /* if (payments.getCellEditor() != null) {
         payments.getCellEditor().stopCellEditing();

         if(getEditFlag()==0){
         setEditFlagIs1(tempPayment, payments, tempPaymentRow, tempPaymentCol);
         }
         }*/
    }
}//GEN-LAST:event_paymentsKeyPressed

private void paymentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentsFocusLost
    // TODO add your handling code here:
    // if(payments.getCellEditor()!=null){
    //   payments.getCellEditor().stopCellEditing();
    // }
}//GEN-LAST:event_paymentsFocusLost

//IVS_Thanh start add 2014/07/10 Mashu_お会計表示
private void integrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_integrationActionPerformed
    integrationId = "";
    if (0 < integration.getSelectedIndex()) {
        MstData mstdata = (MstData) integration.getSelectedItem();
        integrationId = mstdata.getID().toString();
    }
    initProductClasses();
}//GEN-LAST:event_integrationActionPerformed

private void itemintegrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemintegrationActionPerformed
    // TODO add your handling code here:
    itemintegrationId = "";
    if (0 < itemintegration.getSelectedIndex()) {
        MstData mstdata = (MstData) itemintegration.getSelectedItem();
        itemintegrationId = mstdata.getID().toString();
    }
    initProductClasses();
}//GEN-LAST:event_itemintegrationActionPerformed

private void integrationClameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_integrationClameActionPerformed
    integrationIdClame = "";
    if (0 < integrationClame.getSelectedIndex()) {
        MstData mstdata = (MstData) integrationClame.getSelectedItem();
        integrationIdClame = mstdata.getID().toString();
    }
    initProductClasses();
}//GEN-LAST:event_integrationClameActionPerformed

private void itemIntegrationClameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemIntegrationClameActionPerformed
    itemIntegrationIdClame = "";
    if (0 < itemIntegrationClame.getSelectedIndex()) {
        MstData mstdata = (MstData) itemIntegrationClame.getSelectedItem();
        itemIntegrationIdClame = mstdata.getID().toString();
    }
    initProductClasses();
}//GEN-LAST:event_itemIntegrationClameActionPerformed

private void courseintegrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseintegrationActionPerformed
    // TODO add your handling code here:
    courseintegrationId = "";
    if (0 < courseintegration.getSelectedIndex()) {
        MstData mstdata = (MstData) courseintegration.getSelectedItem();
        courseintegrationId = mstdata.getID().toString();
    }
    initProductClasses();
}//GEN-LAST:event_courseintegrationActionPerformed

private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
    if (this.getOpener() instanceof SearchAccountDialog) {
        SearchAccountDialog sad = (SearchAccountDialog) this.getOpener();
        sad.searchAccount();
        sad.showAccount();
    } else if (this.getOpener() instanceof HairInputAccountOpener) {
        HairInputAccountOpener riao = (HairInputAccountOpener) this.getOpener();
        riao.refresh();
    }
    this.showOpener();
}//GEN-LAST:event_closeButtonActionPerformed

private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
    this.delete();
}//GEN-LAST:event_deleteButtonActionPerformed

private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
    if (this.canChangeKeys()) {
        if (MessageDialog.showYesNoDialog(this,
            MessageUtil.getMessage(3002),
            this.getTitle(),
            JOptionPane.QUESTION_MESSAGE) != 0) {
        return;
    }
    }

    this.init();
}//GEN-LAST:event_clearButtonActionPerformed

private void tempRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempRegistButtonActionPerformed

    this.accountRegister(true);
}//GEN-LAST:event_tempRegistButtonActionPerformed

private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed

    searchCustomerButton.setCursor(null);

    try {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // START ADD 20131028 lvut
        String customerOld = customerNo.getText();
        // START ADD 20131028 lvut
        SystemInfo.getLogger().log(Level.INFO, "顧客検索");
        SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true);
        sc.setShopID(this.getSelectedShopID());
        sc.setVisible(true);

        if (sc.getSelectedCustomer() != null
            && !sc.getSelectedCustomer().getCustomerID().equals("")) {

            customerNo.setText(sc.getSelectedCustomer().getCustomerNo());
            customerName1.setText(sc.getSelectedCustomer().getCustomerName(0));
            customerName2.setText(sc.getSelectedCustomer().getCustomerName(1));
            // START EDIT 20131028 lvut
            changeCus = !customerOld.equals(customerNo.getText());
            if (customerOld.equals("")) {
                changeCus = false;
            }
            if (ia.getSales().getCustomer() != null && ia.getSales().getCustomer().getCustomerNo() != null && changeCus) {
                // IVS SANG START 20131114 [gb]予約登録画面、お会計画面の制御調整
                // this.init();
                this.clearProduct();
                // IVS SANG END 20131114 [gb]予約登録画面、お会計画面の制御調整
                //IVS_LVTu start add 2015/11/26 New request #44111
                for ( int i = ia.getSales().size() -1 ;i >= 0;i --) {
                    if (ia.getSales().get(i).isProposalFalg()) {
                        clearData(i);
                    }
                }
                if(ia.getSales() != null && ia.getSales().getCustomer() != null
                    && ia.getSales().getCustomer().getCustomerID() != null) {
                        this.btCallProposal.setEnabled(true);
                    }else {
                        this.btCallProposal.setEnabled(false);
                }
                //IVS_LVTu end add 2015/11/26 New request #44111
            }
            //END EDIT 20131028 lvut
            // 前回指名担当表示
            this.showNowPoint(sc.getSelectedCustomer());
            ia.getSales().setCustomer(sc.getSelectedCustomer());
            this.changeCustomerNameEditable(customerNo.getText().equals("0"));
            this.setBill();
            // レスポンス
            setResponse();
            if (ia.getSales() == null) {
                this.setLastTimeStaff(ia.getSales().getCustomer());
            } else {
                if (ia.getSales().getSlipNo() == null || (!customerOld.equals(customerNo.getText()))) {
                    this.setLastTimeStaff(ia.getSales().getCustomer());
                }

            }

            staff.requestFocusInWindow();
            //IVS_LVTu start add 2015/12/27 Bug #45224
            if (productDivision.getSelectedIndex() == 5) {
                stateChange();
            }
            //IVS_LVTu end add 2015/12/27 Bug #45224
        }

        // SOSIA連動にあわせて連動ボタンを変更する
        updateSosiaGearButton();

        sc = null;

        //IVS_LVTu start add 2015/11/26 New request #44111
        if(ia.getSales() != null && ia.getSales().getCustomer() != null
            && ia.getSales().getCustomer().getCustomerID() != null) {
                this.btCallProposal.setEnabled(true);
            }else {
                this.btCallProposal.setEnabled(false);
        }
        //IVS_LVTu end add 2015/11/26 New request #44111

    } finally {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    initPointPrepaid();
    showPointPrepaid();
}//GEN-LAST:event_searchCustomerButtonActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if (!this.shimeiFreeFlag) {
        //			jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
        //			jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
        //			this.shimeiFreeFlag=true;
        changeShimeiFreeFlag(true);
    } else {
        //			jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
        //			jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
        //			this.shimeiFreeFlag=false;
        changeShimeiFreeFlag(false);
    }
    this.setChargeStaffAndChargeNamed(false);
    this.requestFocusToPayments();
}//GEN-LAST:event_jButton1ActionPerformed

private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

    if (!chargeStaffNo.getText().equals("")) {
        this.setChargeStaff(chargeStaffNo.getText());
        this.getFocusTraversalPolicy().getComponentAfter(this, chargeStaffNo);
    } else {
        chargeStaff.setSelectedIndex(0);
    }
}//GEN-LAST:event_chargeStaffNoFocusLost

private void staffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staffNoFocusLost

    if (!staffNo.getText().equals("")) {
        this.setStaff(staffNo.getText());
        this.getFocusTraversalPolicy().getComponentAfter(this, staffNo);
    } else {
        staffNo.setText("");
        staff.setSelectedIndex(0);
    }
}//GEN-LAST:event_staffNoFocusLost

private void changeSosiaGearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSosiaGearButtonActionPerformed
    changeSosiaGear();
}//GEN-LAST:event_changeSosiaGearButtonActionPerformed

private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

    //IVS_LVTu start edit 2015/07/10 Bug #40203
    MstStaff ms = (MstStaff) chargeStaff.getSelectedItem();

    if (ms != null) {
        if (ms.getStaffID() != null) {
            chargeStaffNo.setText(ms.getStaffNo());
        }

        if (chargeStaff.getSelectedIndex() == 0) {
            chargeStaffNo.setText("");
        }
        //IVS_LVTu start add 2015/06/26 New request #38256
        // if use_shop_category then select staff combobox lost focus
        if (ia.getSales().getShop() != null) {
            if(ia.getSales().getShop().getUseShopCategory()!=null) {
                if (ia.getSales().getShop().getUseShopCategory() == 1) {
                    return;
                }
            }
        }
        //IVS_LVTu end add 2015/06/26 New request #38256
        //IVS_LVTu end edit 2015/07/10 Bug #40203
        if (0 < ia.getSales().size()) {
            this.setChargeStaffAndChargeNamed(true);
        }
    }
}//GEN-LAST:event_chargeStaffActionPerformed

private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed

    MstStaff ms = (MstStaff) staff.getSelectedItem();

    if (ms != null) {
        if (ms.getStaffID() != null) {
            staffNo.setText(ms.getStaffNo());
        }

        if (staff.getSelectedIndex() == 0) {
            staffNo.setText("");
        }

        ia.getSales().getPayment(0).setStaff(ms);
    }
}//GEN-LAST:event_staffActionPerformed

private void customerName2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerName2ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_customerName2ActionPerformed

private void customerName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerName1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_customerName1ActionPerformed

private void customerNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNoFocusLost
    Integer cusOld = null;
    //nhanvt start add 20150326 Bug #35729
    ia.setMsSetting(ms);
    if (ia.getSales() != null && ia.getSales().getCustomer() != null) {
        cusOld = ia.getSales().getCustomer().getCustomerID();
        ia.getSales().setAccountSetting(ms);
    }
    //nhanvt end add 20150326 Bug #35729
    this.setCustomer();
    this.setBill();
    // レスポンス
    setResponse();
    initPointPrepaid();
    showPointPrepaid();
    if (cusOld == null || ia.getSales() == null) {
        this.setLastTimeStaff(ia.getSales().getCustomer());
    } else {
        if ((cusOld != null && !cusOld.equals(ia.getSales().getCustomer().getCustomerID())) || ia.getSales().getSlipNo() == null && ia.getSales().getReservationDatetime() == null) {
            this.setLastTimeStaff(ia.getSales().getCustomer());
        }
    }
    //IVS_LVTu start add 2015/12/27 Bug #45224
    if (((cusOld != null && !cusOld.equals(ia.getSales().getCustomer().getCustomerID()))
            || cusOld == null && ia.getSales().getCustomer().getCustomerID() != null )
            && productDivision.getSelectedIndex() == 5) {
            stateChange();
    }
    //IVS_LVTu end add 2015/12/27 Bug #45224
}//GEN-LAST:event_customerNoFocusLost

private void salesDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesDateFocusLost
    ia.getSales().setSalesDate(salesDate.getDate());
    ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
    // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
    ia.getTaxs(ia.getSales().getSalesDate());
}//GEN-LAST:event_salesDateFocusLost

private void salesDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesDateFocusGained
    if (salesDate.getInputContext() != null) {
        salesDate.getInputContext().setCharacterSubsets(null);
    }
}//GEN-LAST:event_salesDateFocusGained

private void salesDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_salesDateItemStateChanged
    // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
    if (ia.getSales().getSalesDate() != null && ia.getSales().getSalesDate().compareTo(salesDate.getDate()) != 0) {
        if (isChangeApplyDate()) {
            this.changeTaxBySalesDate();
            ia.getTaxs(ia.getSales().getSalesDate());
        }
    }
    ia.getSales().setSalesDate(salesDate.getDate());
    ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
	//20190927
	stateChange();
	//明細行に期限切れ消化コースの明細があれば削除する
	for (int i = ia.getSales().size(); i > 0 ; i--) {
		DataSalesDetail d = ia.getSales().get(i-1);
		if (d.getProductDivision() == 6
				&& d.getConsumptionCourse().getValidDate() != null
				&& salesDate.getDate().compareTo(d.getConsumptionCourse().getValidDate()) > 0 ) {
			deleteProduct(i - 1);
            break;
		}
	}
}//GEN-LAST:event_salesDateItemStateChanged

private void customerInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInfoButtonActionPerformed

    customerInfoButton.setCursor(null);

    try {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        MstCustomer customer = ia.getSales().getCustomer();
        if (customer.getCustomerID() != null) {

            MstCustomerPanel mcp = null;

            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                mcp = new MstCustomerPanel(customer.getCustomerID(), false, false, true);
                SwingUtil.openAnchorDialog(this.parentFrame, true, mcp, "顧客情報", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            ((JDialog) mcp.getParent().getParent().getParent().getParent()).dispose();

            customerNo.setText(mcp.getCustomer().getCustomerNo());
            this.setCustomer();
            if (ia.getSales() == null) {
                this.setLastTimeStaff(ia.getSales().getCustomer());
            } else {
                if (ia.getSales().getSlipNo() == null) {
                    this.setLastTimeStaff(ia.getSales().getCustomer());
                }

            }
        }

    } finally {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}//GEN-LAST:event_customerInfoButtonActionPerformed

    private void loadMainStaff() {
        if ((ia.getSales().getSlipNo() != null && ia.getSales().getSlipNo() > 0) || (ia.reservationNo!= null && ia.reservationNo > 0)) {
            arrDataReservationMainstaff = ia.getReservationMainstaffs();
            if (ia.isTempSave()) {
                initDataTemp();
            }
            else
            {
                if (ia.getSales().getSlipNo()!=null && ia.getSales().getSlipNo() > 0) {
                    initDataSale(ia.getSales().getSlipNo());
                }
                else
                {
                     initDataReservation(ia.reservationNo);

                }
            }
        }else {
           this.arrDataReservationMainstaff = ia.getReservationMainstaffs();
           if (this.arrDataReservationMainstaff.size() > 0 && ia.isTempSave()) {
               initDataTemp();
           }
           else
           {
               initMainStaffData();
           }
        }
        ia.getSales().getSalesMainstaffs().clear();
        ia.setReservationMainstaffs(arrDataReservationMainstaff);
        setDataSaleMainStaff(arrDataReservationMainstaff);
    }
    private void initMainStaffData() {
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            arrMsRelation.clear();
            arrMsRelation.loadByShop(con,ia.getShop().getShopID());
            if (ia.getSales().getCustomer().getCustomerID()!= null && ia.getSales().getCustomer().getCustomerID() > 0) {
                loadLastSalesMainStaffByShopCategoryID();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        showMainStaffData();
    }
     /**
     * Load last sales by customerID
     * @author IVS_Thanh
     * @since 20140715
     */
    private void loadLastSalesMainStaffByShopCategoryID()
    {
            ResultSetWrapper rs;
            try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select \n");
            sql.append("      dsm.*,ms.staff_no \n");
            sql.append(" from \n");
            sql.append("     data_sales as ds \n");
            sql.append("    inner join data_sales_mainstaff dsm using(shop_id,slip_no) \n");
            sql.append("    left join mst_staff ms on ms.staff_id = dsm.staff_id \n");
            sql.append(" where \n");
            sql.append("         ds.delete_date is null \n");
            sql.append("     and dsm.delete_date is null \n");
            sql.append("     and ds.sales_date is not null \n");
            sql.append("     and ds.customer_id = " + SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID()));
            sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            //sql.append(" and slip_no = (select slip_no from data_sales ds1 inner join data_sales_mainstaff using(shop_id,slip_no) where sales_date is not null and ds1.delete_date \n");
            //sql.append(" is null and shop_id = ds.shop_id and customer_id = ds.customer_id  \n");
            sql.append(" and slip_no = (select max(slip_no) from data_sales where sales_date is not null and delete_date \n");
            sql.append(" is null and shop_id = ds.shop_id and customer_id = ds.customer_id ) \n");
            sql.append(" order by \n");
            sql.append("      ds.sales_date desc \n");
            sql.append("     ,ds.slip_no desc \n");
            //sql.append(" limit 1");

            //前回の伝票NOを取得
            rs = con.executeQuery(sql.toString());
            while (rs.next()) {
                DataSalesMainstaff dsm = new DataSalesMainstaff();
                dsm.setData(rs);
                this.lastSaleMainstaffs.add(dsm);
            }
            rs.close();
            } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }

//	private static Map<Integer, java.util.List<Object[]>> mainStaffRowCache
//						= new HashMap<Integer,  java.util.List<Object[]>>();

	private void showMainStaffData() {
        if (tblReservationMainStaff.getCellEditor() != null) {
            tblReservationMainStaff.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(tblReservationMainStaff);
        DefaultTableModel model = (DefaultTableModel) tblReservationMainStaff.getModel();

//		if (mainStaffRowCache.containsKey(targetShop.getShopID())) {
//			for (Object[] rowData : mainStaffRowCache.get(targetShop.getShopID())) {
//				model.addRow(rowData);
//			}
//		} else {
//			java.util.List<Object[]> cacheList = new ArrayList<Object[]>(0);
			for (MstShopRelation msr : arrMsRelation) {
				DataSalesMainstaff ms = getStaffIDByShopCategoryId(msr.getShopCategoryId());
				//IVS_LVTu start edit 2015/06/16 #37295
				JComboBox cmb = getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId());
				Object[] rowData = {
					msr.getShopClassName(),
	//                getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? "" : ms.getStaff().getStaffNo(),
	//                getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId()),
	//                getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? false : ms.getDesignated(),
					cmb.getSelectedIndex() == 0 ? "" : ms.getStaff().getStaffNo(),
					cmb,
					cmb.getSelectedIndex() == 0 ? false : ms.getDesignated(),
					msr
				};
				//IVS_LVTu end edit 2015/06/16 #37295
				model.addRow(rowData);
//				cacheList.add(rowData);
			}
//			mainStaffRowCache.put(targetShop.getShopID(), cacheList);
//		}
    }

        private DataSalesMainstaff getStaffIDByShopCategoryId(Integer shopCategoryId)
    {
        for (int i = 0; i < lastSaleMainstaffs.size(); i++) {
            if (lastSaleMainstaffs.get(i).getShopCategoryId() == shopCategoryId) {
                //return lastSaleMainstaffs.get(i).getStaff().getStaffID();
                return lastSaleMainstaffs.get(i);
            }
        }
        return new DataSalesMainstaff();
    }
       private void initDataTemp() {
        try {
////SystemInfo.info("initDataTemp start");
			if (SystemInfo.getShopRelations()==null) {
				ConnectionWrapper con = SystemInfo.getConnection();
				arrMsRelation.clear();
				arrMsRelation.loadByShop(con,ia.getShop().getShopID());
				SystemInfo.setShopRelations(arrMsRelation);
			} else {
				arrMsRelation = (MstShopRelations)SystemInfo.getShopRelations();
			}
            SwingUtil.clearTable(tblReservationMainStaff);
            DefaultTableModel model = (DefaultTableModel) tblReservationMainStaff.getModel();
            for (MstShopRelation msr : arrMsRelation) {
                DataReservationMainstaff ms = getStaffByShopCategoryId(msr.getShopCategoryId());
                //IVS_LVTu start edit 2015/06/16 #37295
                JComboBox cmb = getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId());
                Object[] rowData = {
                    msr.getShopClassName(),
//                    getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? "" : ms.getStaff().getStaffNo(),
//                    getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId()),
//                    getStaffComboBox(ms.getStaff().getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? false :ms.getDesignated(),
                    cmb.getSelectedIndex() == 0 ? "" : ms.getStaff().getStaffNo(),
                    cmb,
                    cmb.getSelectedIndex() == 0 ? false :ms.getDesignated(),
                    msr
                };
                //IVS_LVTu end edit 2015/06/16 #37295
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

//SystemInfo.info("initDataTemp end");
    }
    private DataReservationMainstaff getStaffByShopCategoryId(Integer shopCategoryId)
    {
        for (int i = 0; i < arrDataReservationMainstaff.size(); i++) {
            if (arrDataReservationMainstaff.get(i).getShopCategoryId() == shopCategoryId) {
                //return lastSaleMainstaffs.get(i).getStaff().getStaffID();
                return arrDataReservationMainstaff.get(i);
            }
        }
        return new DataReservationMainstaff();
    }
         /**
     * Load data sale main staff by slipNO
     * @param slipNO the order number
     * @author IVS_Thanh
     * @since 20140715
     */
    private void initDataSale(Integer slipNo) {
//SystemInfo.info("initDataSale start");
        ResultSetWrapper rs;
            try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select \n");
            sql.append("     dsm.shop_id,dsm.designated_flag,dsm.staff_id,staff_no,msc.shop_category_id,msc.shop_class_name \n");
            sql.append(" from \n");
            sql.append("    mst_shop_category msc \n");
            sql.append("    inner join mst_shop_relation msr on msc.shop_category_id = msr.shop_category_id \n");
            sql.append("    left join  data_sales_mainstaff dsm on msr.shop_category_id = dsm.shop_category_id  \n");
            sql.append("    and msr.shop_id = dsm.shop_id  \n");
             sql.append("     and dsm.slip_no = " + SQLUtil.convertForSQL(slipNo));
            sql.append("    left join mst_staff ms using(staff_id) \n");
            sql.append(" where \n");
            sql.append("     dsm.delete_date is null \n");
            sql.append("     and msc.delete_date is null \n");
            sql.append("     and msr.shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append(" order by \n");
            sql.append("      msc.display_seq \n");
            //前回の伝票NOを取得
            rs = con.executeQuery(sql.toString());
            boolean flag = false;
            SwingUtil.clearTable(tblReservationMainStaff);
            DefaultTableModel model = (DefaultTableModel) tblReservationMainStaff.getModel();
            while (rs.next()) {
                flag = true;
                MstShopRelation msr = new MstShopRelation();
                msr.setShopClassName(rs.getString("shop_class_name"));
                MstStaff ms = new MstStaff();
                ms.setStaffID(rs.getInt("staff_id"));
                ms.setStaffNo(rs.getString("staff_no"));
                msr.setShopCategoryId(rs.getInt("shop_category_id"));
                //IVS_LVTu start edit 2015/06/16 #37295
                JComboBox cmb = getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId());
                Object[] rowData = {
                    msr.getShopClassName(),
//                    getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? "" : ms.getStaffNo(),
//                    getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId()),
//                    getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? false : rs.getBoolean("designated_flag"),
                    cmb.getSelectedIndex() == 0 ? "" : ms.getStaffNo(),
                    cmb,
                    cmb.getSelectedIndex() == 0 ? false : rs.getBoolean("designated_flag"),
                    msr
                };
                //IVS_LVTu end edit 2015/06/16 #37295
                model.addRow(rowData);
            }
            rs.close();
//            if (!flag) {
//               initData();
//            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
//SystemInfo.info("initDataSale end");
    }

	/**
     * スタッフ選択用JComboBoxを取得する。
     *
     * @param staffID 選択状態にするスタッフＩＤ
     * @return スタッフ選択用JComboBox
     */
    private JComboBox getStaffComboBox(Integer staffID, Integer shopCategoryID) {
        JComboBox staffCombo = new JComboBox();
        staffCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffCombo.addItem(new MstStaff());
        //IVS_LVTu start edit 2015/06/16 #37295
        //MstStaff.addRelationStaffDataToJComboBox(con,staffCombo,shopCategoryID,ia.getShop().getShopID());
        MstStaff.addStaffDataToJComboBox(staffCombo,shopCategoryID,ia.getShop().getShopID());
        //IVS_LVTu end edit 2015/06/16 #37295
        staffCombo.setSelectedIndex(0);
        if(staffID!=null){
            this.setStaff(staffCombo, staffID);
        }
        staffCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
				//SystemInfo.info("staffCombo actionPerformed");
                //Luc start edit 20150914 #42626
                //if (tblReservationMainStaff.getSelectedColumn() == 2) {
                   changeStaff();
                   //IVS_LVTu start add 2015/06/26 New request #38256
                   setMainStaff();
                   //IVS_LVTu end add 2015/06/26 New request #38256
                   //Luc start add 20150915 #42626
                   setDesignate();
                   //Luc start add 20150915 #42626
                //}
                //Luc end edit 20150914 #42626
            }
        });

       return staffCombo;

    }
    /**
     * 指定したスタッフＩＤをスタッフ選択用JComboBoxで選択状態にする。
     *
     * @param staffCombo スタッフ選択用JComboBox
     * @param staffID スタッフＩＤ
     */
    private void setStaff(JComboBox staffCombo, Integer staffID) {

        for (int i = 0; i < staffCombo.getItemCount(); i++) {

            MstStaff ms = (MstStaff) staffCombo.getItemAt(i);

            //空白をセット
            if (ms.getStaffID() == null) {

                staffCombo.setSelectedIndex(0);

            } else if (ms.getStaffID().equals(staffID)) {

                staffCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void changeStaff() {
        //Luc start edit 20150914 #42626
        JComboBox staffCombo = (JComboBox) tblReservationMainStaff.getValueAt(tblReservationMainStaff.getSelectedRow(), 2);
        //Luc end edit 20150914 #42626
        MstStaff ms = (MstStaff) staffCombo.getSelectedItem();
        tblReservationMainStaff.setValueAt(ms.getStaffNo(), tblReservationMainStaff.getSelectedRow(), 1);
    }

    /* Luc start delete 20150914 #42626
    private Object getCurrentCellObject() {
        return tblReservationMainStaff.getValueAt(tblReservationMainStaff.getSelectedRow(),
                tblReservationMainStaff.getSelectedColumn());
    }
    Luc start delete 20150914 #42626*/
    private void initDataReservation(Integer reservationNo) {
//SystemInfo.info("initDataReservation start");
        ResultSetWrapper rs;
            try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select \n");
            sql.append("     drm.shop_id,drm.designated_flag,drm.staff_id,staff_no,msc.shop_category_id,msc.shop_class_name \n");
            sql.append(" from \n");
            sql.append("    mst_shop_category msc \n");
            sql.append("    inner join mst_shop_relation msr on msc.shop_category_id = msr.shop_category_id \n");
            sql.append("    left join  data_reservation_mainstaff drm on msr.shop_category_id = drm.shop_category_id  \n");
            sql.append("    and msr.shop_id = drm.shop_id  \n");
            sql.append("     and drm.reservation_no = " + SQLUtil.convertForSQL(reservationNo));
            sql.append("    left join mst_staff ms using(staff_id) \n");
            sql.append(" where \n");
            sql.append("     drm.delete_date is null \n");
            sql.append("     and msc.delete_date is null \n");
            sql.append("     and msr.shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append(" order by \n");
            sql.append("      msc.display_seq \n");
            //前回の伝票NOを取得
            rs = con.executeQuery(sql.toString());
            boolean flag = false;
            SwingUtil.clearTable(tblReservationMainStaff);
            DefaultTableModel model = (DefaultTableModel) tblReservationMainStaff.getModel();
            while (rs.next()) {
                flag = true;
                MstShopRelation msr = new MstShopRelation();
                msr.setShopClassName(rs.getString("shop_class_name"));
                MstStaff ms = new MstStaff();
                ms.setStaffID(rs.getInt("staff_id"));
                ms.setStaffNo(rs.getString("staff_no"));
                msr.setShopCategoryId(rs.getInt("shop_category_id"));
                JComboBox cmb = getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId());
                Object[] rowData = {
                    msr.getShopClassName(),
//                    getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? "" : ms.getStaffNo(),
//                    getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId()),
//                    getStaffComboBox(ms.getStaffID(),msr.getShopCategoryId()).getSelectedIndex() == 0 ? false : rs.getBoolean("designated_flag"),
                    cmb.getSelectedIndex() == 0 ? "" : ms.getStaffNo(),
                    cmb,
                    cmb.getSelectedIndex() == 0 ? false : rs.getBoolean("designated_flag"),

                    msr
                };
                model.addRow(rowData);
            }
            rs.close();
//            if (!flag) {
//                initData();
//            }
            } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
//SystemInfo.info("initDataReservation end");
    }
    private void mainStaffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainStaffButtonActionPerformed
       if ((ia.getSales().getSlipNo() != null && ia.getSales().getSlipNo() > 0) || (ia.reservationNo!= null && ia.reservationNo > 0)) {
            MainStaffForSettingBusiness dlg = new MainStaffForSettingBusiness(ia.getShop(),ia);
            SwingUtil.openAnchorDialog(this.parentFrame, true, dlg, "業態主担当設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
            if (dlg.flgSave) {
                ia.setTempSave(dlg.flgSave);
                ia.getSales().getSalesMainstaffs().clear();
                ia.setReservationMainstaffs(dlg.arrDataReservationMainstaff);
                setDataSaleMainStaff(dlg.arrDataReservationMainstaff);
            }

        }
        else
        {
            MainStaffForSettingBusiness dlg = new MainStaffForSettingBusiness(targetShop,ia.getSales().getCustomer().getCustomerID(),ia.getReservationMainstaffs(),ia.isTempSave());
            SwingUtil.openAnchorDialog(this.parentFrame, true, dlg, "業態主担当設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
            if (dlg.flgSave) {
                ia.setTempSave(dlg.flgSave);
                ia.getSales().getSalesMainstaffs().clear();
                ia.setReservationMainstaffs(dlg.arrDataReservationMainstaff);
                setDataSaleMainStaff(dlg.arrDataReservationMainstaff);
                //IVS_LVTu start add 2015/06/26 New request #38256
                setMainStaff();
                //IVS_LVTu end add 2015/06/26 New request #38256
            }
        }
        }//GEN-LAST:event_mainStaffButtonActionPerformed

        private void tblReservationMainStaffFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblReservationMainStaffFocusLost
            setStaffNo();
        }//GEN-LAST:event_tblReservationMainStaffFocusLost

        private void tblReservationMainStaffPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblReservationMainStaffPropertyChange
            setStaffNo();
        }//GEN-LAST:event_tblReservationMainStaffPropertyChange

        private void tblReservationMainStaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReservationMainStaffMouseClicked
            int row = tblReservationMainStaff.getSelectedRow();
            int col = tblReservationMainStaff.getSelectedColumn();
            if ( row >= 0 && col == 3 ) {
                setMainStaff();
                setDesignate();
            }

        }//GEN-LAST:event_tblReservationMainStaffMouseClicked

        private void settingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_settingStateChanged
            // TODO add your handling code here:
        }//GEN-LAST:event_settingStateChanged

        //IVS_LVTu start add 2015/11/26 New request #44111
        private void btCallProposalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCallProposalActionPerformed
        if (ia.getSales() == null || ia.getSales().getCustomer() == null ||ia.getSales().getCustomer().getCustomerID() == null) {
            return;
        }
        searchCustomerButton.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SystemInfo.getLogger().log(Level.INFO, "提案書一覧");
            //check exists 1 row DataProposal
            boolean checkExists = false;
            for ( DataSalesDetail dsd : ia.getSales()) {
                if ( dsd.isProposalFalg()) {
                    checkExists = true;
                }
            }
            if (!checkExists) {
                dtProposal = null;
            }
            SearchProposal sc = new SearchProposal(parentFrame, true,ia.getSales().getCustomer().getCustomerID(), ia.getShop().getShopID(), ia.getSales().getSlipNo(), dtProposal);
            sc.setVisible(true);
            // get DataProposal selected
            dtProposal = sc.getSelectedProposal();
            if ( dtProposal == null || dtProposal.size() == 0) {
                return;
            }
            //set Proposal Id
            ia.getSales().setProposalID(dtProposal.getProposalID());
            // clear data when exists slip_no
//            if (ia.getSales() != null && ia.getSales().getSlipNo() != null) {
                for ( int i = ia.getSales().size() -1 ;i >= 0;i --) {
                    clearData(i);
                }
//            }else {
//                //clear data when clip_no is null
//                for ( int i = ia.getSales().size() -1 ;i >= 0;i --) {
//                    if (ia.getSales().get(i).isProposalFalg()) {
//                        clearData(i);
//                    }
//                }
//            }

            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs ;

            for (DataProposalDetail dtProposalDetail : dtProposal) {
                // Item
                if ( dtProposalDetail.getProductDivision().equals(2)) {
                    Product	p	=	new Product();
                    String sql = "SELECT mi.prepa_id,\n" +
                                "mi.prepaid_price,\n" +
                                "mi.item_id AS product_id,\n" +
                                "mi.item_no AS product_no,\n" +
                                "mi.item_name AS product_name,\n" +
                                "mi.price,\n" +
                                "mi.price AS base_price,\n" +
                                "mi.display_seq AS base_display_seq,\n" +
                                "mi.display_seq,\n" +
                                "0 AS operation_time,\n" +
                                "0 AS prepa_class_id\n" +
                         "FROM mst_item mi\n" +
                         "WHERE mi.item_id =" + SQLUtil.convertForSQL(dtProposalDetail.getProductID()) + "\n";


                        rs = con.executeQuery(sql);
                        if(rs.next()){
                            p.setData(rs, 2);
                            //set product_num and product_value from table data_proposal_detail
                            p.setPrice(dtProposalDetail.getProductValue());
                            this.productNum = dtProposalDetail.getProductNum();
                        }
                        rs.close();
                        // set Item class
                        for ( int i = 0;i < itemClameClasses.size() ;i ++ ) {
                            if (itemClameClasses.get(i).getProductClassID().equals(dtProposalDetail.getClassID())) {
                                p.setProductClass(itemClameClasses.get(i));
                                break;
                            }
                        }

                        //set table
                        flagProposalItem = true;
                        this.addSelectedProduct(2, p);
                        //when product_num value reset false
                        flagProposalItem = false;
                        productNum = 0;

                    //course
                }else if ( dtProposalDetail.getProductDivision().equals(5)){
                    Course	cc	=	new Course();
                    String sql = "SELECT mc.course_id,\n" +
                                    "       mc.course_name,\n" +
                                    "       mc.num,\n" +
                                    "       mc.price,\n" +
                                    "       mc.price AS base_price,\n" +
                                    "       mc.display_seq AS base_display_seq,\n" +
                                    "       mc.operation_time,\n" +
                                    "       mc.praise_time_limit,\n" +
                                    "       mc.display_seq,\n" +
                                    "       mc.is_praise_time\n" +
                                    "FROM mst_course mc\n" +
                                    "WHERE mc.course_id =" + SQLUtil.convertForSQL(dtProposalDetail.getProductID()) + "\n";

                    rs = con.executeQuery(sql);
                    if(rs.next()){
                        cc.setData(rs);

                        //set product_num and product_value from table data_proposal_detail
                        cc.setPrice(dtProposalDetail.getProductValue());
                        cc.setNum(dtProposalDetail.getProductNum());
                    }
                    rs.close();

                    // set Item class
                    for ( int i = 0;i < courseClasses.size() ;i ++ ) {
                        if (courseClasses.get(i).getCourseClassId().equals(dtProposalDetail.getClassID())) {
                            cc.setCourseClass(courseClasses.get(i));
                            break;
                        }
                    }
                    //set table
                    this.addSelectedCourse(5, cc);
                    //コース契約を選択したら購入してなくても消化できるようにコース消化リストに追加する
                    this.addConsumptionCourse(consumptionCourseClass, consumptionCourse, cc);
                }
                //set flag when select proposal
                ia.getSales().get(ia.getSales().size() - 1).setProposalFalg(true);
            }
            sc = null;
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btCallProposalActionPerformed

    private void customerNoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_customerNoPropertyChange
        if(ia.getSales() != null && ia.getSales().getCustomer() != null
                && ia.getSales().getCustomer().getCustomerID() != null) {
            this.btCallProposal.setEnabled(true);
        }else {
            this.btCallProposal.setEnabled(false);
        }
    }//GEN-LAST:event_customerNoPropertyChange

    private void txtOpeMinuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOpeMinuteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOpeMinuteActionPerformed

    private void txtOpeSecondActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOpeSecondActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOpeSecondActionPerformed

    private void txtConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConditionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConditionActionPerformed

    private void txtConditionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConditionKeyReleased
        this.showProducts(technicClass, technic);
    }//GEN-LAST:event_txtConditionKeyReleased

    private void clearData(Integer row) {
        int index = this.getProductsIndex(0, row);    // 選択技術
        if (getEditFlag() == 0) {
            setEditFlag(1);
        }
        DataSalesDetail dsd = ia.getSales().get(index);
        int size = dsd.size();

        if (products.getCellEditor() != null) {
            products.getCellEditor().stopCellEditing();
        }

        if(dsd.getProduct().getPrepa_id()!=null){
             arrPaymentMethodId_1.remove(dsd.getProduct());
        }
        ia.getSales().remove(index);

        int removeConsumptionCourse = 0;
        //コース契約前のコースを明細から削除する場合はコース消化リストからもコース情報を削除する
        if (dsd.getProductDivision() == 5 && dsd.getTmpContractNo() != null && dsd.getTmpContractNo().trim().length() > 0) {
            String tmpContractNo = dsd.getTmpContractNo();
            Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(dsd.getCourse().getCourseClass().getCourseClassId());
            //消化コースリストから削除
            try {
                courseMap.remove(tmpContractNo);
            }catch (Exception e) {}

            if ((courseMap == null || courseMap.isEmpty() )) {
                //消化コースリストが空の場合はコース分類リストからコース分類を削除する
                Integer courseClassId = dsd.getCourse().getCourseClass().getCourseClassId();
                try {
                    consumptionCouserClassMap.remove(courseClassId);
                }catch (Exception e) {}

                //このコースのコース分類に契約済みコースが紐付いているかどうかチェック
                boolean hasConsumptionCourse = false;
                for (ConsumptionCourseClass consumptionCourseClass : consumptionCourseClasses) {
                    if (consumptionCourseClass.getSlipNo() != null
                            && courseClassId.equals(consumptionCourseClass.getCourseClassId())) {
                        //契約済コースが紐付いている場合
                        hasConsumptionCourse = true;
                        break;
                    }
                }

                if (!hasConsumptionCourse) {
                    //未契約コース、契約済みコースのどちらも紐付いていないコース分類がある
                    for (ConsumptionCourseClass consumptionCourseClass : consumptionCourseClasses) {
                        if (courseClassId.equals(consumptionCourseClass.getCourseClassId())) {
                            try {
                                consumptionCourseClasses.remove(consumptionCourseClass);
                            }catch (Exception e){}
                            break;
                        }
                    }
                }
            }

            //明細行に削除したコースに対する消化コースの明細があれば削除する
            for (int i = ia.getSales().size(); i > 0; i--) {
                DataSalesDetail d = ia.getSales().get(i - 1);
                if (d.getProductDivision() == 6
                        && tmpContractNo.equals(d.getTmpContractNo())) {
                    ia.getSales().remove(i - 1);
                    break;
                }
            }

            this.setConsumptionDataSales(consumptionCourseClasses, consumptionCourseClass);
            this.showConsumptionCourse(consumptionCourseClass, consumptionCourse);

            DefaultTableModel model = (DefaultTableModel) products.getModel();
            for (int i = model.getRowCount(); i > 0; i--) {
                Object o = model.getValueAt(i - 1, 2);
                if (o instanceof ConsumptionCourse) {
                    if (tmpContractNo.equals(((ConsumptionCourse) o).getTmpContractNo())) {
                        //仮契約番号が等しい場合はこの明細行を削除
                        model.removeRow(i - 1);
                        this.detailCount--;
                        removeConsumptionCourse++;
                    }
                }
            }
        }

        DefaultTableModel model = (DefaultTableModel) products.getModel();

        if (this.getSelectedShop().isDisplayProportionally()) {
            // 按分メニュー表示あり
            for (int i = 0; i < size + 1; i++) {
                model.removeRow(row);
            }
        } else {
            // 按分メニュー表示なし
            model.removeRow(row);
        }

        for (; row < products.getRowCount(); row++) {
            for (int i = 0; i < removeConsumptionCourse + 1; i++) {
                this.decTableTechIndex(row);
            }
        }

        this.detailCount--;

        // ポイント割引設定に反映
        if (SystemInfo.isUsePointcard()) {
            showDiscountPoint();
        }

        this.setTotal();
        this.showCustomerDisplayToSalesValue();
    }

    //IVS_LVTu end add 2015/11/26 New request #44111

    private void setStaffNo() {
         int col = tblReservationMainStaff.getSelectedColumn();
         int row = tblReservationMainStaff.getSelectedRow();
         if (row < 0 || col < 0) {
            return;
         }
         if (col == 1) {
             String StaffNo = tblReservationMainStaff.getValueAt(row, col).toString();
             if (!StaffNo.equals(""))
             {
                 JComboBox staffCombo = (JComboBox)tblReservationMainStaff.getValueAt(row, 2);
                 this.setStaff(staffCombo, StaffNo);
                 if (staffCombo.getSelectedIndex() == 0) {
                     tblReservationMainStaff.setValueAt("", row, col);
                     tblReservationMainStaff.setValueAt(staffCombo, row, 2);
                     return;
                 }
                 tblReservationMainStaff.setValueAt(staffCombo, row, 2);
             }
             else
             {
                 JComboBox staffCombo = (JComboBox)tblReservationMainStaff.getValueAt(row, 2);
                 staffCombo.setSelectedIndex(0);
                 tblReservationMainStaff.setValueAt(staffCombo, row, 2);
             }
         }
    }

private JComboBox setStaff(JComboBox staffCombo, String staffNo) {

        for (int i = 0; i < staffCombo.getItemCount(); i++) {

            MstStaff ms = (MstStaff) staffCombo.getItemAt(i);

            //空白をセット
            if (ms.getStaffID() == null) {

                staffCombo.setSelectedIndex(0);

            } else if (ms.getStaffNo().equals(staffNo)) {

                staffCombo.setSelectedIndex(i);
                return staffCombo;
            }
        }
        return staffCombo;
    }
    /**
     * set data for data sale main staff
     * @author IVS_Thanh
     * @since 20140715
     */
    private void setDataSaleMainStaff(ArrayList<DataReservationMainstaff> arrDRM) {
        for (DataReservationMainstaff drm :arrDRM) {
            DataSalesMainstaff dsm = new DataSalesMainstaff();
            dsm.setDesignated(drm.getDesignated());
            dsm.setShopCategoryId(drm.getShopCategoryId());
            dsm.setShop(drm.getShop());
            dsm.setStaff(drm.getStaff());
            ia.getSales().getSalesMainstaffs().add(dsm);
        }
    }

    /**
     * set visible for Integration
     * @author IVS_Thanh
     * @since 20140715
     */
    private void setVisibleIntegration() {
        int setLayout = SystemInfo.getAccountSetting().getDisplaySwitchTechnic();
        if (setLayout != 3) {
            this.integration.setVisible(false);
            this.integrationLabel3.setVisible(false);
            itemintegration.setVisible(false);
            integrationLabel5.setVisible(false);
            itemIntegrationClame.setVisible(false);
            integrationLabel6.setVisible(false);
            this.integrationClame.setVisible(false);
            this.integrationLabel4.setVisible(false);
            this.courseintegration.setVisible(false);
            this.integrationLabel7.setVisible(false);
        }
    }
    //IVS_Thanh end add 2014/07/10 Mashu_お会計表示

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accountRegistButton;
    private javax.swing.JLabel allDiscountValue;
    private com.geobeck.swing.JFormattedTextFieldEx billValue;
    private javax.swing.JButton btCallProposal;
    private javax.swing.JComboBox cboexPioneerName;
    private javax.swing.JButton changeSosiaGearButton;
    private javax.swing.JLabel changeValue;
    private javax.swing.ButtonGroup charge;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JLabel chargeStaffLabel;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton collectBillButton;
    private javax.swing.JLabel condtionLabel;
    private javax.swing.JPanel consumCoursePanel;
    private javax.swing.JTable consumptionCourse;
    private javax.swing.JTable consumptionCourseClass;
    private javax.swing.JLabel countLabel;
    private javax.swing.JTable course;
    private javax.swing.JTable courseClass;
    private javax.swing.JPanel coursePanel;
    private javax.swing.JComboBox courseintegration;
    private javax.swing.JButton customerInfoButton;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton discountSettingButton;
    private javax.swing.JLabel discountValue;
    private com.geobeck.swing.JTableEx discounts;
    private javax.swing.JScrollPane discountsScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx genzaiPoint;
    private javax.swing.JComboBox integration;
    private javax.swing.JComboBox integrationClame;
    private javax.swing.JLabel integrationLabel3;
    private javax.swing.JLabel integrationLabel4;
    private javax.swing.JLabel integrationLabel5;
    private javax.swing.JLabel integrationLabel6;
    private javax.swing.JLabel integrationLabel7;
    private javax.swing.JTable item;
    private javax.swing.JTable item1;
    private javax.swing.JTable itemClass;
    private javax.swing.JTable itemClass1;
    private javax.swing.JScrollPane itemClassScrollPane;
    private javax.swing.JScrollPane itemClassScrollPane1;
    private javax.swing.JComboBox itemIntegrationClame;
    private javax.swing.JPanel itemPanel;
    private javax.swing.JPanel itemPanel1;
    private javax.swing.JTable itemReturned;
    private javax.swing.JTable itemReturnedClass;
    private javax.swing.JScrollPane itemReturnedClassScrollPane;
    private javax.swing.JScrollPane itemReturnedClassScrollPane2;
    private javax.swing.JScrollPane itemReturnedClassScrollPane3;
    private javax.swing.JPanel itemReturnedPanel;
    private javax.swing.JScrollPane itemReturnedScrollPane;
    private javax.swing.JScrollPane itemReturnedScrollPane2;
    private javax.swing.JScrollPane itemReturnedScrollPane3;
    private javax.swing.JScrollPane itemScrollPane;
    private javax.swing.JScrollPane itemScrollPane1;
    private javax.swing.JComboBox itemintegration;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lblPassBookBalance;
    private javax.swing.JButton mainStaffButton;
    private javax.swing.JPanel moveButtonPanel;
    private javax.swing.JPanel moveButtonPanel1;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel opeMinuteLabel;
    private javax.swing.JLabel opeSecondLabel;
    private javax.swing.JLabel opeTimeLabel;
    private javax.swing.JPanel opeTimePanel;
    private javax.swing.JLabel paymentTotal;
    private com.geobeck.swing.JTableEx payments;
    private javax.swing.JScrollPane paymentsScrollPane;
    private com.geobeck.swing.JTableEx point;
    private javax.swing.JPanel pointPanel;
    private javax.swing.JScrollPane pointScrollPane;
    private javax.swing.JPanel prepaidPanel;
    private javax.swing.JButton prevButton;
    private javax.swing.JTabbedPane productDivision;
    private com.geobeck.swing.JTableEx products;
    private javax.swing.JScrollPane productsScrollPane;
    private javax.swing.JPanel registPanel;
    private com.geobeck.swing.JFormattedTextFieldEx reservationType;
    private javax.swing.JComboBox responseItemComboBox1;
    private javax.swing.JComboBox responseItemComboBox2;
    private javax.swing.JLabel responseItemLabel;
    private javax.swing.JLabel responseItemLabel2;
    private com.geobeck.swing.JFormattedTextFieldEx responseItemText1;
    private com.geobeck.swing.JFormattedTextFieldEx responseItemText2;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo salesDate;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton searchButton1;
    private javax.swing.JButton searchCustomerButton;
    private javax.swing.JScrollPane searchResultScrollPane;
    private javax.swing.JTabbedPane setting;
    private com.geobeck.swing.JFormattedTextFieldEx slipNo;
    private javax.swing.JLabel slipNoLabel;
    private javax.swing.JLabel slipNoLabel1;
    private javax.swing.JLabel slipNoLabel2;
    private javax.swing.JComboBox staff;
    private javax.swing.JTextField staffNo;
    private javax.swing.JLabel taxValue;
    private com.geobeck.swing.JTableEx tblReservationMainStaff;
    private javax.swing.JTable technic;
    private javax.swing.JTable technicClame;
    private javax.swing.JTable technicClameClass;
    private javax.swing.JScrollPane technicClameClassScrollPane;
    private javax.swing.JPanel technicClamePanel;
    private javax.swing.JScrollPane technicClameScrollPane;
    private javax.swing.JTable technicClass;
    private javax.swing.JScrollPane technicClassScrollPane;
    private javax.swing.JPanel technicPanel;
    private javax.swing.JPanel technicPanel1;
    private javax.swing.JScrollPane technicScrollPane;
    private javax.swing.JButton tempRegistButton;
    private javax.swing.JLabel totalPrice;
    private javax.swing.JLabel totalPrice2;
    private javax.swing.JLabel totalValue;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtCondition;
    private javax.swing.JTextField txtOpeMinute;
    private javax.swing.JTextField txtOpeSecond;
    private com.geobeck.swing.JFormattedTextFieldEx txtPassBookBalance;
    private javax.swing.JTextField txtfPioneerCode;
    // End of variables declaration//GEN-END:variables
    private boolean changeKeys = true;
    private ProductClasses technicClasses = new ProductClasses();
    // IVS_Thanh start add 2014/07/10 Mashu お会計表示
    private ProductClasses technicClameClasses = new ProductClasses();
    private ProductClasses itemClameClasses = new ProductClasses();
    // IVS_Thanh end add 2014/07/10 Mashu お会計表示
    private ProductClasses itemClasses = new ProductClasses();
    private CourseClasses courseClasses = new CourseClasses();
    private CunsumptionCourseClasses consumptionCourseClasses = new CunsumptionCourseClasses();
    private ArrayList<com.geobeck.sosia.pos.data.account.DataSales> targetList = new ArrayList<com.geobeck.sosia.pos.data.account.DataSales>();
    private int currentIndex = 0;
    private ArrayList<Integer> pointIndexList = new ArrayList<Integer>();
    //コース契約前のコース分類
    private Map<Integer, ConsumptionCourseClass> consumptionCouserClassMap = new HashMap<Integer, ConsumptionCourseClass>();
    //コース契約前のコース
    private Map<Integer, Map<String, ConsumptionCourse>> consumptionCourseMap = new HashMap<Integer, Map<String, ConsumptionCourse>>();

    /**
     * 伝票入力画面用FocusTraversalPolicyを取得する。
     *
     * @return 伝票入力画面用FocusTraversalPolicy
     */
    public InputAccountFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    public void setOpener(Component opener) {
        this.opener = opener;
        closeButton.setVisible(this.getOpener() != null);
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(tempRegistButton);
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(clearButton);
        SystemInfo.addMouseCursorChange(accountRegistButton);
        SystemInfo.addMouseCursorChange(customerInfoButton);
        SystemInfo.addMouseCursorChange(searchCustomerButton);
        SystemInfo.addMouseCursorChange(collectBillButton);
        SystemInfo.addMouseCursorChange(closeButton);
        SystemInfo.addMouseCursorChange(prevButton);
        SystemInfo.addMouseCursorChange(nextButton);
        SystemInfo.addMouseCursorChange(discountSettingButton);
        SystemInfo.addMouseCursorChange(btCallProposal);
    }

    /**
     * コンポーネントの各リスナーをセットする。
     */
    private void setListener() {
        customerNo.addKeyListener(SystemInfo.getMoveNextField());
        customerNo.addFocusListener(SystemInfo.getSelectText());
        customerName1.addKeyListener(SystemInfo.getMoveNextField());
        customerName2.addKeyListener(SystemInfo.getMoveNextField());
        salesDate.addKeyListener(SystemInfo.getMoveNextField());
        salesDate.addFocusListener(SystemInfo.getSelectText());
        slipNo.addKeyListener(SystemInfo.getMoveNextField());
        slipNo.addFocusListener(SystemInfo.getSelectText());
        staff.addKeyListener(SystemInfo.getMoveNextField());
        staff.addFocusListener(SystemInfo.getSelectText());
        staffNo.addKeyListener(SystemInfo.getMoveNextField());
        staffNo.addFocusListener(SystemInfo.getSelectText());
        chargeStaffNo.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaffNo.addFocusListener(SystemInfo.getSelectText());
        chargeStaff.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaff.addFocusListener(SystemInfo.getSelectText());


//		products.addFocusListener(SystemInfo.getLostFocusEditingStopper());
//		discounts.addFocusListener(SystemInfo.getLostFocusEditingStopper());
//		payments.addFocusListener(SystemInfo.getLostFocusEditingStopper());

        cboexPioneerName.addKeyListener(SystemInfo.getMoveNextField());
        cboexPioneerName.addFocusListener(SystemInfo.getSelectText());
        txtfPioneerCode.addKeyListener(SystemInfo.getMoveNextField());
        txtfPioneerCode.addFocusListener(SystemInfo.getSelectText());
    }

    private void setButtonStatus() {
        //Start edit 20131030 lvut
        stat = (this.getSelectedShopID() != 0);

        if (tempRegistButton.isEnabled()) {
            tempRegistButton.setEnabled(stat);
        }

//        if (!stat) {
//            tempRegistButton.setIcon(null);
//        }

        deleteButton.setEnabled(stat);
//        if (!stat) {
//            deleteButton.setIcon(null);
//        }

        clearButton.setEnabled(stat);
//        if (!stat) {
//            clearButton.setIcon(null);
//        }
        //End edit 20131030 lvut
        collectBillButton.setVisible(stat);
        //IVS_TMTrong start add 2015/10/05 New request #43038
        closeButton.setEnabled(false);
        //IVS_TMTrong end add 2015/10/05 New request #43038
    }

    /**
     * 選択されている店舗を取得する。
     *
     * @return 選択されている店舗
     */
    private MstShop getSelectedShop() {
        return targetShop;
    }

    /**
     * 選択されている店舗のIDを取得する。
     *
     * @return 選択されている店舗のID
     */
    private Integer getSelectedShopID() {
        MstShop ms = this.getSelectedShop();

        if (ms != null) {
            return ms.getShopID();
        } else {
            return 0;
        }
    }

    public Integer getProductsIndex(int typeNo) {
        return this.getProductsIndex(typeNo, products.getSelectedRow());
    }
      //nhanvt start edit fix bug list okaike => Exception
//    public Integer getProductsIndex(int typeNo, int row) {
//        Integer retInteger = null;
//        JComboBox combo;
//        if (row < 0) {
//            return null;
//        }
//
//        combo = (JComboBox) products.getValueAt(row, 0);
//
//        if (combo.getItemAt(typeNo + 1) != null) {
//            retInteger = (Integer) combo.getItemAt(typeNo + 1);
//        }
//
//
//        if(retInteger == null){
//            retInteger = 0;
//        }else{
//            if(retInteger == -1){
//                retInteger = 0;
//            }
//        }
//
//        return retInteger;
//    }

    public Integer getProductsIndex(int typeNo, int row) {
        Integer retInteger = null;
        JComboBox combo;
        if (row < 0) {
            return null;
        }

        combo = (JComboBox) products.getValueAt(row, 0);

        if (combo.getItemAt(typeNo + 1) != null) {
            retInteger = (Integer) combo.getItemAt(typeNo + 1);
        }
        if(retInteger != null){
             if(retInteger == -1){
                retInteger = 0;
            }

        }

        return retInteger;
    }
   //nhanvt end edit fix bug list okaike => Exception
    public void addSelectedProduct(Integer productDivision, Product product) {
        int addNo = -1;
        int no = 0;

        for (DataSalesDetail dsd : ia.getSales()) {
            if ((productDivision != 1)
                    && (productDivision != 2)
                    && (productDivision != 3)
                    && (productDivision != 4)
                    && (dsd.getProductDivision().intValue() == productDivision.intValue())
                    && (dsd.getProduct().getProductID().intValue() == product.getProductID().intValue())) {
                addNo = no;
            }
            no++;
        }

        // 新規の内容ならレコードを追加する
        //IVS_LVTu start add 2015/06/26 New request #38256
        JComboBox cmb = new JComboBox();
        boolean check = false;
        if (addNo == -1) {
            boolean isCat = false;
            if (ia.getSales().getShop() != null) {
                if(ia.getSales().getShop().getUseShopCategory()!=null) {
                    if (ia.getSales().getShop().getUseShopCategory() == 1) {
                        //業態主担当設定がある
                        isCat = true;
                        if ( mainStaffButton.isVisible() == false && tblReservationMainStaff.isVisible() == true ){
                            for ( int i = 0;i < tblReservationMainStaff.getRowCount();i ++) {
                                MstShopRelation msr = (MstShopRelation) tblReservationMainStaff.getValueAt(i, 4);
                                Object o = tblReservationMainStaff.getValueAt(i, 2);
                                Object ocheck = tblReservationMainStaff.getValueAt(i, 3);
                                if (product.getProductClass().getShopCategoryID() != null && product.getProductClass().getShopCategoryID().equals(msr.getShopCategoryId())) {
                                    if ( o instanceof JComboBox ) {
                                        cmb = (JComboBox) o;
                                        if ( cmb.getSelectedItem() instanceof MstStaff) {
                                            MstStaff ms = (MstStaff)cmb.getSelectedItem();
                                        }
                                    }
                                    if ( ocheck instanceof Boolean ){
                                        check = (Boolean) ocheck;
                                    }
                                }
                            }
                        }else if ( mainStaffButton.isVisible() == true ) {
                            cmb = getStaffComboBox(-1);
                            //boolean flagExists = false;
                            for ( int i = 0;i < this.ia.getReservationMainstaffs().size();i ++) {
                                if (product.getProductClass().getShopCategoryID() != null && product.getProductClass().getShopCategoryID().equals(this.ia.getReservationMainstaffs().get(i).getShopCategoryId())) {
                                    this.setStaff(cmb, this.ia.getReservationMainstaffs().get(i).getStaff().getStaffID());
                                    check = this.ia.getReservationMainstaffs().get(i).getDesignated();
                                }
                            }

                        }else {
                            cmb = getStaffComboBox(-1);
                            this.setStaff(cmb, -1);
                        }
                        if ( cmb.getSelectedItem() == null) {
                            cmb = getStaffComboBox(-1);
                        }
                    }else {
                        //業態主担当設定がない
                        check = (this.shimeiFreeFlag&&this.getSelectedShop().isDesignatedAssist());
                    }
                }
            }
            //IVS_LVTu end add 2015/06/26 New request #38256
            // ivs_Hoa edit start 20141119 Bug #32603 [gb]指名入力アシストを無にした場合のお会計の制御不備
            ia.addSalesDetail(
                    productDivision,
                    product,
                    //IVS_LVTu start edit 2015/06/26 New request #38256
                    //(MstStaff) chargeStaff.getSelectedItem(),
                    isCat == true ? (MstStaff)cmb.getSelectedItem() : (MstStaff) chargeStaff.getSelectedItem(),
                    //IVS_LVTu end edit 2015/06/26 New request #38256
                    //(this.shimeiFreeFlag&&this.getSelectedShop().isDesignatedAssist()));
                    //Luc start edit 20150818 #41948
                    //(isCat == true && check && productDivision == 1) ? check:(this.shimeiFreeFlag&&this.getSelectedShop().isDesignatedAssist()));
                     (check));
                    //Luc end edit 20150818 #41948
            // ivs_Hoa edit end  20141119 Bug #32603 [gb]指名入力アシストを無にした場合のお会計の制御不備
            //IVS_LVTu start add 2015/11/26 New request #44111
            //set setProductNum from data_proposal_detail
            if (productDivision.equals(2) && flagProposalItem) {
                ia.getSales().get(ia.getSales().size() - 1).setProductNum(productNum);
            }
            //IVS_LVTu end add 2015/11/26 New request #44111
            // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
            ia.getSales().get(ia.getSales().size() - 1).setTaxRate(ia.getTaxRate());
            this.addProductRow(ia.getSales().get(ia.getSales().size() - 1), false);
        } else {

            ia.getSales().get(addNo).setProductNum(ia.getSales().get(addNo).getProductNum() + 1);
            products.setValueAt(ia.getSales().get(addNo).getProductNum(), addNo, 4);
            this.changeProducts();
        }

        this.setTotal();
    }

    public void addSelectedCourse(Integer productDivision, Course course) {
        //IVS_LVTu start add 2015/06/26 New request #38256
        JComboBox cmb = new JComboBox();
        boolean check = false;
        boolean isCat = false;
        if (ia.getSales().getShop() != null) {
            if(ia.getSales().getShop().getUseShopCategory()!=null) {
                if (ia.getSales().getShop().getUseShopCategory() == 1) {
                    //業態主担当設定がある
                    isCat = true;
                    if ( mainStaffButton.isVisible() == false && tblReservationMainStaff.isVisible() == true ){
                        for ( int i = 0;i < tblReservationMainStaff.getRowCount();i ++) {
                            MstShopRelation msr = (MstShopRelation) tblReservationMainStaff.getValueAt(i, 4);
                            Object o = tblReservationMainStaff.getValueAt(i, 2);
                            Object ocheck = tblReservationMainStaff.getValueAt(i, 3);
                            if (course.getCourseClass().getShopCategoryID() != null && course.getCourseClass().getShopCategoryID().equals(msr.getShopCategoryId())) {
                                if ( o instanceof JComboBox ) {
                                    cmb = (JComboBox) o;
                                    if ( cmb.getSelectedItem() instanceof MstStaff) {
                                        MstStaff ms = (MstStaff)cmb.getSelectedItem();
                                    }
                                }
                                if ( ocheck instanceof Boolean ){
                                    check = (Boolean) ocheck;
                                }

                            }
                        }
                    }else if ( mainStaffButton.isVisible() == true ) {
                        cmb = getStaffComboBox(-1);
                        //boolean flagExists = false;
                        for ( int i = 0;i < this.ia.getReservationMainstaffs().size();i ++) {
                            if (course.getCourseClass().getShopCategoryID() != null && course.getCourseClass().getShopCategoryID().equals(this.ia.getReservationMainstaffs().get(i).getShopCategoryId())) {
                                this.setStaff(cmb, this.ia.getReservationMainstaffs().get(i).getStaff().getStaffID());
                                check = this.ia.getReservationMainstaffs().get(i).getDesignated();
                            }
                        }
                    }else {
                        cmb = getStaffComboBox(-1);
                        this.setStaff(cmb, -1);
                    }
                    if ( cmb.getSelectedItem() == null) {
                        cmb = getStaffComboBox(-1);
                    }
                }else {
                    //業態主担当設定がない
                    check = this.shimeiFreeFlag&& this.getSelectedShop().isDesignatedAssist();
                }
            }
        }
        //IVS_LVTu end add 2015/06/26 New request #38256
        // ivs_Hoa edit start 20141119 Bug #32603 [gb]指名入力アシストを無にした場合のお会計の制御不備
        ia.addSalesDetail(
                productDivision,
                course,
                //IVS_LVTu start edit 2015/06/26 New request #38256
                //(MstStaff) chargeStaff.getSelectedItem(),
                isCat == true ? (MstStaff)cmb.getSelectedItem() : (MstStaff) chargeStaff.getSelectedItem(),
                //IVS_LVTu end edit 2015/06/26 New request #38256
                //this.shimeiFreeFlag&& this.getSelectedShop().isDesignatedAssist(),
                //Luc start edit 20150818 41948
                //(flag == true && check) ? check : this.shimeiFreeFlag&& this.getSelectedShop().isDesignatedAssist(),
                (check),
                //Luc end edit 20150818 41948
                generateTmpContractNo(course));
        // ivs_Hoa edit endt 20141119 Bug #32603 [gb]指名入力アシストを無にした場合のお会計の制御不備
        ia.getSales().get(ia.getSales().size() - 1).setTaxRate(ia.getTaxRate());
        this.addCourseRow(ia.getSales().get(ia.getSales().size() - 1), false);

        this.setTotal();
    }

    private String generateTmpContractNo(Course course) {
        int courseSize = 0;
        if (consumptionCourseMap.containsKey(course.getCourseClass().getCourseClassId())) {
            courseSize = consumptionCourseMap.get(course.getCourseClass().getCourseClassId()).size();
        }

        return course.getCourseId() + "_" + String.valueOf(courseSize);
    }

    public void addSelectedConsumptionCourse(Integer productDivision, ConsumptionCourse consumptionCourse) {
        int addNo = -1;
        int no = 0;
        for (DataSalesDetail dsd : ia.getSales()) {
            if ((productDivision != 1)
                    && (productDivision != 2)
                    && (productDivision != 3)
                    && (productDivision != 4)
                    && (productDivision != 5)
                    && (dsd.getProductDivision().intValue() == productDivision.intValue()) //				( dsd.getProductDivision().intValue() == productDivision.intValue() )&&
                    //				( dsd.getConsumptionCourse().getSlipNo().intValue() == consumptionCourse.getSlipNo().intValue() ) &&
                    //				( dsd.getConsumptionCourse().getCourseId().intValue() == consumptionCourse.getCourseId().intValue() )
                    ) {
                //nhanvt start edit 20141212 Bug #33936
                if (dsd.getConsumptionCourse().getSlipNo() == null) {
                    //契約番号がない場合（コース契約前）

                    if (consumptionCourse.getSlipNo() != null
                             && consumptionCourse.getCourseId().equals(dsd.getConsumptionCourse().getCourseId())
                             && consumptionCourse.getContractShopId().equals(dsd.getConsumptionCourse().getContractShopId())
                             && consumptionCourse.getContractNo().equals(dsd.getConsumptionCourse().getContractNo())
                             && consumptionCourse.getContractDetailNo().equals(dsd.getConsumptionCourse().getContractDetailNo())
                             ) {
                        addNo = no;
                        no++;
                        continue;
                     }
//                    if (consumptionCourse.getSlipNo() != null) {
//                        no++;
//                        continue;
//                    }


                    //仮契約番号がない場合（コース契約済だが消化を予約した状態）
                    if (dsd.getTmpContractNo() == null) {
                        no++;
                        continue;
                    }

                    if (dsd.getTmpContractNo().equals(consumptionCourse.getTmpContractNo())) {

                        addNo = no;
                        no++;
                        continue;
                    }
                } else {
                    //契約番号がある場合（すでにコース契約済）
                    if (consumptionCourse.getSlipNo() == null) {
                        no++;
                        continue;
                    }

//                    if ((dsd.getConsumptionCourse().getSlipNo().intValue() == consumptionCourse.getSlipNo().intValue())
//                            && (dsd.getConsumptionCourse().getCourseId().intValue() == consumptionCourse.getCourseId().intValue())) {
//                        addNo = no;
//                        no++;
//                        continue;
//                    }

                    if (consumptionCourse.getCourseId().equals(dsd.getConsumptionCourse().getCourseId())
                             && consumptionCourse.getContractShopId().equals(dsd.getConsumptionCourse().getContractShopId())
                             && consumptionCourse.getContractNo().equals(dsd.getConsumptionCourse().getContractNo())
                             && consumptionCourse.getContractDetailNo().equals(dsd.getConsumptionCourse().getContractDetailNo())
                             ) {
                        addNo = no;
                        no++;
                        continue;
                     }
                }
            }
            //nhanvt end edit 20141212 Bug #33936

            no++;
        }

        // 新規の内容ならレコードを追加する
        if (addNo == -1) {
            //IVS_LVTu start add 2015/06/26 New request #38256
            JComboBox cmb = new JComboBox();
            boolean check = false;
            boolean isCat = false;
            if (ia.getSales().getShop() != null) {
                if(ia.getSales().getShop().getUseShopCategory()!=null) {
                    if (ia.getSales().getShop().getUseShopCategory() == 1) {
                         //業態主担当設定がある
                        isCat = true;
                        if ( mainStaffButton.isVisible() == false && tblReservationMainStaff.isVisible() == true ){
                            for ( int i = 0;i < tblReservationMainStaff.getRowCount();i ++) {
                                MstShopRelation msr = (MstShopRelation) tblReservationMainStaff.getValueAt(i, 4);
                                Object o = tblReservationMainStaff.getValueAt(i, 2);
                                Object ocheck = tblReservationMainStaff.getValueAt(i, 3);
                                if (consumptionCourse.getConsumptionCourseClass().getShopCategoryID() != null && consumptionCourse.getConsumptionCourseClass().getShopCategoryID().equals(msr.getShopCategoryId())) {
                                    if ( o instanceof JComboBox ) {
                                        cmb = (JComboBox) o;
                                        if ( cmb.getSelectedItem() instanceof MstStaff) {
                                            MstStaff ms = (MstStaff)cmb.getSelectedItem();
                                        }
                                    }
                                    if ( ocheck instanceof Boolean ){
                                        check = (Boolean) ocheck;
                                    }
                                }
                            }
                        }else if ( mainStaffButton.isVisible() == true ) {
                            cmb = getStaffComboBox(-1);
                            //boolean flagExists = false;
                            for ( int i = 0;i < this.ia.getReservationMainstaffs().size();i ++) {
                                if (consumptionCourse.getConsumptionCourseClass().getShopCategoryID() != null && consumptionCourse.getConsumptionCourseClass().getShopCategoryID().equals(this.ia.getReservationMainstaffs().get(i).getShopCategoryId())) {
                                    this.setStaff(cmb, this.ia.getReservationMainstaffs().get(i).getStaff().getStaffID());
                                    check = this.ia.getReservationMainstaffs().get(i).getDesignated();
                                }
                            }
                        }else {
                            cmb = getStaffComboBox(-1);
                            this.setStaff(cmb, -1);
                        }
                        if ( cmb.getSelectedItem() == null) {
                            cmb = getStaffComboBox(-1);
                        }
                    }else {
                        //業態主担当設定がない
                        check = this.shimeiFreeFlag&& this.getSelectedShop().isDesignatedAssist();
                    }
                }
            }
            if ( cmb.getSelectedItem() == null) {
                cmb = getStaffComboBox(-1);
            }
            //IVS_LVTu end add 2015/06/26 New request #38256
            // ivs_Hoa edit start 20141119 Bug #32603 [gb]指名入力アシストを無にした場合のお会計の制御不備
            consumptionCourse.setConsumptionRestNum(consumptionCourse.getConsumptionRestNum() - 1);

            ia.addSalesDetail(
                    productDivision,
                    consumptionCourse,
                    //IVS_LVTu start edit 2015/06/26 New request #38256
                    //(MstStaff) chargeStaff.getSelectedItem(),
                    isCat == true ? (MstStaff)cmb.getSelectedItem() : (MstStaff) chargeStaff.getSelectedItem(),
                    //IVS_LVTu end edit 2015/06/26 New request #38256
                    //this.shimeiFreeFlag&&this.getSelectedShop().isDesignatedAssist(),
                    //Luc start edit 20150818 #41948
                    //(flag == true && check) ? check : this.shimeiFreeFlag&&this.getSelectedShop().isDesignatedAssist(),
                    (check),
                    //Luc end edit 20150818 #41948
                    consumptionCourse.getTmpContractNo());
            this.addConsumptionCourseRow(ia.getSales().get(ia.getSales().size() - 1), false);
            // ivs_Hoa edit end 20141119 Bug #32603 [gb]指名入力アシストを無にした場合のお会計の制御不備
            ia.getSales().get(ia.getSales().size() - 1).setProductNum(ia.getSales().get(ia.getSales().size() - 1).getProductNum() + 1);
        } else {
            //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
//          if(ia.getSales().get(addNo).getConsumptionCourse().getConsumptionRestNum()-1 >=0) {
                ia.getSales().get(addNo).setProductNum(ia.getSales().get(addNo).getProductNum() + 1);
                ia.getSales().get(addNo).setConsumptionNum(ia.getSales().get(addNo).getConsumptionNum() + 1);
                //Luc start add 20160219 #48422
                ia.getSales().get(addNo).getConsumptionCourse().setConsumptionRestNum(ia.getSales().get(addNo).getConsumptionCourse().getConsumptionRestNum()-1);
                //Luc end add 20160219 #48422
                products.setValueAt( ia.getSales().get(addNo).getConsumptionNum(), addNo, 4);
                //nhanvt start add 20141216 Bug #33936
                Double countD = 0.0;
                Object obj = products.getValueAt(addNo, 4);
                if (obj != null) {
                    if (!"".equals(obj)) {
                        countD = Double.parseDouble(obj.toString());

                    }
                }
                obj = new Object();
                obj = products.getValueAt(addNo, 5);
                Double price = Double.parseDouble(obj.toString());
                Double priceTotal = price * countD;
                products.setValueAt(priceTotal, addNo, 7);
                //Luc start add 20160219 #48422
                try {
                products.setValueAt(ia.getSales().get(addNo).getConsumptionCourse().getConsumptionRestNum(),addNo,restNumCol);
                }catch(Exception ex) {}
                //Luc end add 20160219 #48422
                //nhanvt end add 20141216 Bug #33936
                this.changeProducts();
//            }
            //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        }

//                this.setTotalWithoutPayment();
        this.setTotal();
    }

    public void addSelectedItem(MstItem mi) {
        int index = ia.addSalesDetail(mi);

        if (index < 0) {
            this.addProductRow(ia.getSales().get(ia.getSales().size() - 1), false);
        } else {
            this.addProductNum(index);
        }

        this.setTotal();
    }

    //IVS_Thanh start add 2014/07/10 Mashu_お会計表示
    /**
     * init data for technic class combobox
     * @author IVS_Thanh
     * @since 20140715
     */
    private void initIntegration() {

        integration.removeAllItems();
        integration.addItem(null);
        integrationClame.removeAllItems();
        integrationClame.addItem(null);

        SimpleMaster sm = new SimpleMaster(
                "",
                "mst_technic_integration",
                "technic_integration_id",
                "technic_integration_name", 0);

        sm.loadData();

        for (MstData md : sm) {
            integration.addItem(md);
            integrationClame.addItem(md);
        }
        integration.setSelectedIndex(0);
        integrationClame.setSelectedIndex(0);

    }
    /**
     * init data for item class combobox
     * @author IVS_Thanh
     * @since 20140715
     */
    private void initItemIntegration() {

        itemintegration.removeAllItems();
        itemintegration.addItem(null);
        itemIntegrationClame.removeAllItems();
        itemIntegrationClame.addItem(null);
        SimpleMaster sm = new SimpleMaster(
                "",
                "mst_item_integration",
                "item_integration_id",
                "item_integration_name", 0);

        sm.loadData();

        for (MstData md : sm) {
            itemintegration.addItem(md);
            itemIntegrationClame.addItem(md);
        }
        itemintegration.setSelectedIndex(0);
        itemIntegrationClame.setSelectedIndex(0);
    }
    /**
     * init data for course class combobox
     * @author IVS_Thanh
     * @since 20140715
     */
    private void  initCourseIntegration() {

        courseintegration.removeAllItems();
        courseintegration.addItem(null);
        SimpleMaster sm = new SimpleMaster(
                "",
                "mst_course_integration",
                "course_integration_id",
                "course_integration_name", 0);

        sm.loadData();

        for (MstData md : sm) {
            courseintegration.addItem(md);

        }
        courseintegration.setSelectedIndex(0);
    }
     //IVS_Thanh end add 2014/07/10 Mashu_お会計表示

    private void initProductClasses() {

        try {

            ConnectionWrapper con = SystemInfo.getConnection();
// キャッシュ化
			technicClasses = ProductClasses.getCachedProducts(con, ProductClasses.TYPE_TECHNIC, this.getSelectedShopID(), integrationId);
			itemClasses = ProductClasses.getCachedProducts(con, ProductClasses.TYPE_ITEM, this.getSelectedShopID(), itemintegrationId);
			courseClasses = CourseClasses.getCachedProducts(con, CourseClasses.TYPE_COURCE, this.getSelectedShopID(), courseintegrationId);
			technicClameClasses = ProductClasses.getCachedProducts(con, ProductClasses.TYPE_TECHNIC_CLAME, this.getSelectedShopID(), integrationIdClame);
			itemClameClasses = ProductClasses.getCachedProducts(con, ProductClasses.TYPE_ITEM_CLAME, this.getSelectedShopID(), itemIntegrationIdClame);

//            technicClasses.setProductDivision(1);
//            //technicClasses.load(con, this.getSelectedShopID());
//            //IVS_Thanh start add 2014/07/10 Mashu_お会計表示
//            technicClasses.load(con, this.getSelectedShopID(), integrationId);
//            technicClameClasses.load(con, this.getSelectedShopID(), integrationIdClame);
//            //IVS_Thanh end add 2014/07/10 Mashu_お会計表示
//
//            itemClasses.setProductDivision(2);
//            //IVS_Thanh start add 2014/07/10 Mashu_お会計表示
//            itemClameClasses.setProductDivision(2);
//             //IVS_Thanh end add 2014/07/10 Mashu_お会計表示
//            //add by ltthuc 2014/06/25
//            isDatabaseName = SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev");
//            if(isDatabaseName==true){
//                  itemClasses.loadItemClasses(con, this.getSelectedShopID(),itemintegrationId);
//                   //IVS_Thanh start add 2014/07/10 Mashu_お会計表示
//                  itemClameClasses.loadItemClasses(con, this.getSelectedShopID(),itemIntegrationIdClame);
//                   //IVS_Thanh end add 2014/07/10 Mashu_お会計表示
//              }else{
//			itemClasses.loadItemClasses(con, this.getSelectedShopID(),itemintegrationId);
//			 //IVS_Thanh start add 2014/07/10 Mashu_お会計表示
//			itemClameClasses.loadItemClasses(con, this.getSelectedShopID(),itemIntegrationIdClame);
//			 //IVS_Thanh end add 2014/07/10 Mashu_お会計表示
//              }
//
//            //end add by ltthuc
//            courseClasses.setProductDivision(3);
//            //IVS_Thanh start edit 2014/07/10 Mashu_お会計表示
//            courseClasses.load(con, this.getSelectedShopID(),courseintegrationId);
//            //IVS_Thanh end edit 2014/07/10 Mashu_お会計表示

            //TODO 商品区分をセットする必要はない
//                consumptionCourseClasses.setProductDivision(4);
            //TODO 初期化時にデータを読み込む必要はない
//                consumptionCourseClasses.load(con, this.getSelectedShopID());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        this.setProductClasses(technicClasses, technicClass);
        this.setProductClasses(itemClasses, itemClass);
        this.setProductClasses(technicClameClasses, technicClameClass);
        this.setProductClasses(itemClameClasses, itemReturnedClass);
        this.setProductClasses(itemClasses, item);
        // if( isDatabaseName==true){
        this.setProductClasses1(itemClasses, item1);
        // }

        try {

            if (hidePrepaTab() == true) {
                this.setProductClasses1(itemClasses, itemClass1);
                firstCheckHideTab = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setCourseClasses(courseClasses, courseClass);
//            this.setProductClasses(consumptionCourseClasses, consumptionCourseClass);

        //IVS_Thanh start edit 2014/07/10 Mashu_お会計表示
        this.showProducts(technicClass, technic);
// タブ切り替え時に読込
//        this.showProducts(itemClass, item);
//        this.showProducts(technicClameClass, technicClame);
//        this.showProducts(itemReturnedClass, itemReturned);
//        this.showCourse(courseClass, course);
        //IVS_Thanh end edit 2014/07/10 Mashu_お会計表示
    }

    private void setProductClasses(ProductClasses productClasses, JTable classTable) {
        SwingUtil.clearTable(classTable);
        DefaultTableModel model = (DefaultTableModel) classTable.getModel();
        if (classTable.equals(itemReturnedClass) == false) {
            for (ProductClass pc : productClasses) {
                if (pc.getPrepa_class_id() == null || pc.getPrepa_class_id() != 1) {
                    model.addRow(new Object[]{pc});
                }
            }
        } else {
            for (ProductClass pc : productClasses) {
                model.addRow(new Object[]{pc});
            }
        }
        if (0 < classTable.getRowCount()) {
            classTable.setRowSelectionInterval(0, 0);
        }
    }

    private void setProductClasses1(ProductClasses productClasses, JTable classTable) {
        SwingUtil.clearTable(classTable);
        DefaultTableModel model = (DefaultTableModel) classTable.getModel();

        for (ProductClass pc : productClasses) {
            if (pc.getPrepa_class_id() == 1) {
                model.addRow(new Object[]{pc});
            }
        }

        if (0 < classTable.getRowCount()) {
            classTable.setRowSelectionInterval(0, 0);
        }
    }

    private void setCourseClasses(CourseClasses courseClasses, JTable classTable) {
        SwingUtil.clearTable(classTable);
        DefaultTableModel model = (DefaultTableModel) classTable.getModel();

        for (CourseClass pc : courseClasses) {
            model.addRow(new Object[]{pc});
        }

        if (0 < classTable.getRowCount()) {
            classTable.setRowSelectionInterval(0, 0);
        }
    }

    private void setConsumptionDataSales(CunsumptionCourseClasses dataSaleses, JTable classTable) {
        SwingUtil.clearTable(classTable);
        DefaultTableModel model = (DefaultTableModel) classTable.getModel();

        if (!consumptionCouserClassMap.isEmpty()) {

            Map<Integer, ConsumptionCourseClass> contractedConsumptionCourseClassMap = new HashMap<Integer, ConsumptionCourseClass>();
            for (int i = 0; i < dataSaleses.size(); i++) {
                ConsumptionCourseClass c = dataSaleses.get(i);
                contractedConsumptionCourseClassMap.put(c.getCourseClassId(), c);
            }

            //コース契約前だが、明細にコース契約データがある場合はこのコースも消化コースリストに追加する
            for (ConsumptionCourseClass consumptionCourseClass : consumptionCouserClassMap.values()) {
                if (dataSaleses.isEmpty()) {
                    dataSaleses.insertElementAt(consumptionCourseClass, 0);
                    continue;
                }

                if (contractedConsumptionCourseClassMap.containsKey(consumptionCourseClass.getCourseClassId())) {
                    //すでに消化コース分類リストに同一のコース分類が存在する場合
                    continue;
                }

                boolean add = false;
                for (int i = 0; i < dataSaleses.size(); i++) {
                    ConsumptionCourseClass c = dataSaleses.get(i);

                   // if (consumptionCourseClass.getDisplaySeq() < c.getDisplaySeq()) {
                     if ( consumptionCourseClass.getDisplaySeq() != null && consumptionCourseClass.getDisplaySeq() < c.getDisplaySeq()) {
                        dataSaleses.insertElementAt(consumptionCourseClass, i);
                        contractedConsumptionCourseClassMap.put(consumptionCourseClass.getCourseClassId(), consumptionCourseClass);
                        add = true;
                        break;
                    }
                }

                if (!add) {
                    dataSaleses.add(consumptionCourseClass);
                    contractedConsumptionCourseClassMap.put(consumptionCourseClass.getCourseClassId(), consumptionCourseClass);
                }
            }
        }

        for (ConsumptionCourseClass ccc : dataSaleses) {
            model.addRow(new Object[]{ccc});
        }

        if (0 < classTable.getRowCount()) {
            classTable.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * 商品のリストを表示する。
     */
    private void showProducts(JTable productClassesTable, JTable productsTable) {
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        boolean flag = false;
        //全行削除
        SwingUtil.clearTable(productsTable);

        //選択されている分類を取得
        ProductClass pc = this.getSelectedProductClass(productClassesTable);

        if (pc == null) {
            return;
        }

        try {

            ConnectionWrapper con = SystemInfo.getConnection();

            //分類に属する商品を取得
            int division = productDivision.getSelectedIndex() + 1;

            // クレーム・返品の際でも取ってくる商品は同じ
            if (division == 3) {
                division = 1;
            } else if (division == 4) {
                division = 2;
            } else if (division == 5) {
                //コース契約
                division = 3;
            } else if (division == 6) {
                //コース消化
                division = 4;
            } // start add by ltthuc 20140610 Task #24575
            else if (division == 7) {
                flag = true;
                division = 2;
            }
            if (productClassesTable.equals(itemReturnedClass) == false) {
                if (isDatabaseName == true) {
                    pc.loadProductsMission(con, division, this.getSelectedShopID(), flag);
                } else {
                    pc.loadProductsMission(con, division, this.getSelectedShopID(), flag);
                }
            } else {
                pc.loadProducts(con, division, this.getSelectedShopID());
            }
            //end add by ltthuc

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        //テーブルに商品を追加
        String condtion = txtCondition.getText();

        //技術以外を選択時は絞り込み条件を使用しない
        int tempDivision = productDivision.getSelectedIndex() + 1;
        if (tempDivision != 1) {
            condtion = "";
        }

        for (Product mj : pc) {
            if ("".equals(condtion) || mj.getProductName().startsWith(condtion)) {
                Object[] rowData = {mj, ia.getAccountSetting().getDisplayValue(mj.getPrice(), ia.getTaxRate())};
                model.addRow(rowData);
            }
        }
    }

    private void showCourse(JTable courseClassesTable, JTable courseTable) {
        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
        //全行削除
        SwingUtil.clearTable(courseTable);

        //選択されている分類を取得
        CourseClass cc = this.getSelectedCourseClass(courseClassesTable);

        if (cc == null) {
            return;
        }

        try {

            ConnectionWrapper con = SystemInfo.getConnection();

            //分類に属する商品を取得
            int division = productDivision.getSelectedIndex() + 1;

            if (division != 5) {
                //コース消化タブ以外は処理しない
                return;
            }

            if (division == 5) {
                //コース消化
                //上記メソッド（showProducts）にならって4という値をセット
                division = 3;
            }
            cc.loadCourse(con, this.getSelectedShopID(), cc.getCourseClassId());

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        //テーブルに商品を追加
        for (Course c : cc) {
            //IVS_TMTrong start edit 2015/09/03 New request #42427
            //Object[] rowData = {c, ia.getAccountSetting().getDisplayValue(c.getPrice(), ia.getTaxRate())};
            Object[] rowData = {c, c.getNum(),ia.getAccountSetting().getDisplayValue(c.getPrice(), ia.getTaxRate())};
            //IVS_TMTrong end edit 2015/09/03 New request #42427
            model.addRow(rowData);
        }
    }

    /**
     * 消化コースのリストを表示する。
     */
    private void showConsumptionCourse(JTable consumptionCourseClassesTable, JTable consumptionCourseTable) {
        DefaultTableModel model = (DefaultTableModel) consumptionCourseTable.getModel();
        //全行削除
        SwingUtil.clearTable(consumptionCourseTable);

        //選択されている分類を取得
        ConsumptionCourseClass ccc = this.getSelectedConsumptionCourseClass(consumptionCourseClassesTable);

        if (ccc == null) {
            return;
        }

        try {

            ConnectionWrapper con = SystemInfo.getConnection();

            //分類に属する商品を取得
            int division = productDivision.getSelectedIndex() + 1;

            if (division != 6) {
                //コース消化タブ以外は処理しない
                return;
            }

            if (division == 6) {
                //コース消化
                //上記メソッド（showProducts）にならって4という値をセット
                division = 4;
            }
            //コース消化表示エラー対応
            // Start edit 2013-10-30 Hoa
            //vtbphuong start change 20140425 Bug #22496
            if (salesDate.getDate() == null) {
                ccc.loadConsumptionCourseWithSalesDate(con, this.getSelectedShopID(), ia.getSales().getCustomer().getCustomerID(), ccc.getCourseClassId());
            } else {
                ccc.loadConsumptionCourseWithSalesDate(con, this.getSelectedShopID(), ia.getSales().getCustomer().getCustomerID(), ccc.getCourseClassId(), salesDate.getDate());
            }
            //vtbphuong end change 20140425 Bug #22496
            // end edit 2013-10-30 Hoa
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        //IVS_LVTU start add 2017/08/31 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
        if (!consumptionCourseMap.isEmpty()) {
            if (consumptionCourseMap.containsKey(ccc.getCourseClassId())) {
                Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(ccc.getCourseClassId());
                for (Map.Entry<String, ConsumptionCourse> e : courseMap.entrySet()) {
                    ConsumptionCourse cc = e.getValue();
                    if(cc.getSlipNo() != null) {
                        for (ConsumptionCourse ccIdx : ccc) {
                            if(cc.getSlipNo().equals(ccIdx.getSlipNo())
                                    && cc.getContractShopId().equals(ccIdx.getContractShopId())
                                    && cc.getContractNo().equals(ccIdx.getContractNo())
                                    && cc.getContractDetailNo().equals(ccIdx.getContractDetailNo())){
                                ccc.remove(ccIdx);
                                break;
                            }
                        }
                    }
                }
            }
        }
        //IVS_LVTU end add 2017/08/31 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される

        //コース未契約で明細行にレコードがある場合はそのコースもリストに追加する
        if (!consumptionCourseMap.isEmpty()) {
            if (consumptionCourseMap.containsKey(ccc.getCourseClassId())) {
                Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(ccc.getCourseClassId());
                //未契約コースはリストの最後に追加
                ccc.addAll(courseMap.values());
                System.out.println("*******************ccc");
                for (Map.Entry<String, ConsumptionCourse> e : courseMap.entrySet()) {
                    System.out.println("**************************:" + e.getKey() + "/" + e.getValue().getCourseName() + ":" + e.getValue().getTmpContractNo());
                }
            }
        }

        //テーブルに商品を追加
        NumberFormat nf = NumberFormat.getInstance();
        //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        ConsumptionCourse cc;
        for (ConsumptionCourse ccIdx : ccc) {
            try {
                cc = ccIdx.clone();
				// 20190926 
				if(salesDate.getDate() == null ||  
						(cc.getValidDate()==null || 
						 cc.getValidDate()!=null && salesDate.getDate().compareTo(cc.getValidDate()) < 1 ) ){

                cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
                //IVS_TMTrong start edit 2015/09/03 New request #42427
                Object[] rowData = {cc,
                    String.format("%1$tY/%1$tm/%1$td", cc.getSalesDate()!=null?cc.getSalesDate():salesDate.getDate()),
                    nf.format(cc.getConsumptionRestNum())};  
                //IVS_TMTrong end edit 2015/09/03 New request #42427
                model.addRow(rowData);
				} else {
					                Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, "除外");
				}
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
    }

    /**
     * 消化コースのリストに追加する
     *
     * @param consumptionCourseClassesTable
     * @param consumptionCourseTable
     */
    public void addConsumptionCourse(JTable consumptionCourseClassTable, JTable consumptionCourseTable, Course course) {
        CourseClass courseClass = course.getCourseClass();

        DefaultTableModel consumptionCourseClassesModel = (DefaultTableModel) consumptionCourseClassTable.getModel();

        ConsumptionCourseClass consumptionCourseClass = null;

        //未契約コース分類を格納する
        if (consumptionCouserClassMap.containsKey(courseClass.getCourseClassId())) {
            consumptionCourseClass = consumptionCouserClassMap.get(courseClass.getCourseClassId());
        } else {
            //消化コース分類リストにすでにセットされているかどうか
            boolean existsCourseClass = false;
            for (int i = 0; i < consumptionCourseClassesModel.getRowCount(); i++) {
                if (((ConsumptionCourseClass) consumptionCourseClassesModel.getValueAt(i, 0)).getCourseClassId().equals(courseClass.getCourseClassId())) {
                    existsCourseClass = true;
                    break;
                }
            }

            consumptionCourseClass = new ConsumptionCourseClass();
            consumptionCourseClass.setCourseClassId(courseClass.getCourseClassId());
            consumptionCourseClass.setCourseClassName(courseClass.getCourseClassName());
            consumptionCourseClass.setShopId(this.getSelectedShopID());
            consumptionCourseClass.setSlipNo(null);
            consumptionCourseClass.setDisplaySeq(courseClass.getDisplaySeq());
            //IVS_LVTu start add 2015/06/26 New request #38256
            consumptionCourseClass.setShopCategoryID(courseClass.getShopCategoryID());
            //IVS_LVTu end add 2015/06/26 New request #38256

            consumptionCouserClassMap.put(consumptionCourseClass.getCourseClassId(), consumptionCourseClass);

            if (!existsCourseClass) {
                //消化コース分類リストにセットするための消化コース分類データを生成する

                Object[] rowData = {consumptionCourseClass};
                consumptionCourseClassesModel.addRow(rowData);
            }
        }


        //消化コースリストにセットするための消化コースデータを生成する
        ConsumptionCourse consumptionCourse = new ConsumptionCourse();
        consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
        consumptionCourse.setShopId(this.getSelectedShopID());
        consumptionCourse.setSlipNo(null);
        consumptionCourse.setCourseId(course.getCourseId());
        consumptionCourse.setCourseName(course.getCourseName());
        //価格はコースの販売価格/消化回数をセット
        //IVS_LVTu start edit 2015/05/30 Bug #37095
        //long price = new BigDecimal(course.getPrice()).divide(new BigDecimal(course.getNum().longValue()), 0, RoundingMode.UP).longValue();
        //long basePrice = new BigDecimal(course.getBasePrice()).divide(new BigDecimal(course.getNum().longValue()), 0, RoundingMode.UP).longValue();
        long price = 0;
        long basePrice = 0;
        if ( course.getNum().longValue() != 0 ) {
            price = new BigDecimal(course.getPrice()).divide(new BigDecimal(course.getNum().longValue()), 0, RoundingMode.UP).longValue();
            basePrice = new BigDecimal(course.getBasePrice()).divide(new BigDecimal(course.getNum().longValue()), 0, RoundingMode.UP).longValue();
        }
        consumptionCourse.setPrice(price);
        //IVS_LVTu end edit 2015/05/30 Bug #37095
        consumptionCourse.setBasePrice(basePrice);
        consumptionCourse.setOperationTime(course.getOperationTime());
        consumptionCourse.setPraiseTime(course.isIsPraiseTime());
        consumptionCourse.setPraiseTimeLimit(course.getPraiseTimeLimit());
        consumptionCourse.setNum(course.getNum());
        consumptionCourse.setConsumptionNum(0d);
        consumptionCourse.setConsumptionRestNum(course.getNum().doubleValue());
        consumptionCourse.setContractNo(null);
        consumptionCourse.setContractDetailNo(null);
        consumptionCourse.setStaffId(null);
        consumptionCourse.setTmpContractNo(generateTmpContractNo(course));
        // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
        consumptionCourse.setTaxRate(ia.getTaxRate());

        //未契約コースを格納する
        Map<String, ConsumptionCourse> courseMap = null;
        if (consumptionCourseMap.containsKey(consumptionCourseClass.getCourseClassId())) {
            courseMap = consumptionCourseMap.get(consumptionCourseClass.getCourseClassId());
        } else {
            courseMap = new HashMap<String, ConsumptionCourse>();
        }
        courseMap.put(generateTmpContractNo(course), consumptionCourse);
        consumptionCourseMap.put(consumptionCourseClass.getCourseClassId(), courseMap);

        //追加したコースが1つ目の場合
        if (consumptionCourseClassTable.getRowCount() == 1) {
            //消化コースをセットする
            DefaultTableModel consumptionCourseModel = (DefaultTableModel) consumptionCourseTable.getModel();

            NumberFormat nf = NumberFormat.getInstance();
            //IVS_TMTrong start edit 2015/09/03 New request #42427
            //Object[] consumptionCourseRowData = {consumptionCourse, nf.format(consumptionCourse.getConsumptionRestNum())};
            Object[] consumptionCourseRowData = {consumptionCourse,
                            String.format("%1$tY/%1$tm/%1$td", consumptionCourse.getSalesDate()!=null?consumptionCourse.getSalesDate():salesDate.getDate()),
                            nf.format(consumptionCourse.getConsumptionRestNum())};
            //IVS_TMTrong end edit 2015/09/03 New request #42427
            consumptionCourseModel.addRow(consumptionCourseRowData);
        }
    }

    /**
     * 選択されている分類を取得する。
     *
     * @return 選択されている分類
     */
    public ProductClass getSelectedProductClass(JTable productClassesTable) {
        if (productClassesTable.getSelectedRow() < 0) {
            return null;
        }

        return (ProductClass) productClassesTable.getValueAt(productClassesTable.getSelectedRow(), 0);
    }

    /**
     * 選択されている分類を取得する。
     *
     * @return 選択されている分類
     */
    public CourseClass getSelectedCourseClass(JTable courseClassesTable) {
        if (courseClassesTable.getSelectedRow() < 0) {
            return null;
        }

        return (CourseClass) courseClassesTable.getValueAt(courseClassesTable.getSelectedRow(), 0);
    }

    /**
     * 選択されている分類を取得する。
     *
     * @return 選択されている分類
     */
    public ConsumptionCourseClass getSelectedConsumptionCourseClass(JTable consumptionCourseClassesTable) {
        if (consumptionCourseClassesTable.getSelectedRow() < 0) {
            return null;
        }

        return (ConsumptionCourseClass) consumptionCourseClassesTable.getValueAt(consumptionCourseClassesTable.getSelectedRow(), 0);
    }

    private Product getSelectedProduct(JTable table) {
        int row = table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        return (Product) table.getValueAt(row, 0);
    }

    private Course getSelectedCourse(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        Course course = new Course();
        Course tem = (Course)table.getValueAt(row, 0);
        course.setBasePrice(tem.getBasePrice());
        course.setCourseClass(tem.getCourseClass());
        course.setCourseId(tem.getCourseId());
        course.setCourseName(tem.getCourseName());
        course.setDisplaySeq(tem.getDisplaySeq());
        course.setIsPraiseTime(tem.isIsPraiseTime());
        course.setNum(tem.getNum());
        course.setOperationTime(tem.getOperationTime());
        course.setPraiseTimeLimit(tem.getPraiseTimeLimit());
        course.setPrice(tem.getPrice());
        course.setSecurityTimeLimit(tem.getSecurityTimeLimit());
        return course;
    }

    private ConsumptionCourse getSelectedConsumptionCourse(JTable table) {
        int row = table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        return (ConsumptionCourse) table.getValueAt(row, 0);
    }

    /**
     * 明細の列を初期化する。
     */
    private void initProductsColumn() {
        //列幅調整用変数
        int proportionallyWidth = 0;
        int bedWidth1 = 0;
        int bedWidth2 = 0;
        int bedWidth3 = 0;

        //列幅調整用変数の設定
        if (!this.getSelectedShop().isProportionally() || !this.getSelectedShop().isDisplayProportionally()) {
            //按分無の場合、以下の列幅を広げる
            proportionallyWidth = 135; //商品/技術名
        }
        if (!this.getSelectedShop().isBed()) {
            //施術台無の場合、以下の列幅を広げる
            bedWidth1 = 10; //分類
            bedWidth2 = 30; //商品/技術名
            bedWidth3 = 30; //担当者
        }

        //列の幅を設定する。
        products.getColumnModel().getColumn(0).setPreferredWidth(35);		// 区分
        products.getColumnModel().getColumn(1).setPreferredWidth(60 + bedWidth1);                       // 分類
        products.getColumnModel().getColumn(2).setPreferredWidth(50 + proportionallyWidth + bedWidth2); // 商品/技術名
        products.getColumnModel().getColumn(3).setPreferredWidth(45);		// 単価
        products.getColumnModel().getColumn(4).setPreferredWidth(30);		// 数量
        products.getColumnModel().getColumn(5).setPreferredWidth(45);		// 金額
        products.getColumnModel().getColumn(6).setPreferredWidth(50);		// 割引
        products.getColumnModel().getColumn(7).setPreferredWidth(60);		// 合計
        products.getColumnModel().getColumn(8).setPreferredWidth(70);		// 按分名
        products.getColumnModel().getColumn(9).setPreferredWidth(35);		// 按分ポイント
        products.getColumnModel().getColumn(10).setPreferredWidth(30);		// 按分割合
        products.getColumnModel().getColumn(11).setPreferredWidth(30);          // 残回数
        //Luc start edit 20150907 #42432
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
//        products.getColumnModel().getColumn(12).setPreferredWidth(80 + bedWidth3);                      // 担当者
//        products.getColumnModel().getColumn(13).setPreferredWidth(28);		// 指名
//        products.getColumnModel().getColumn(14).setPreferredWidth(28);		// アプローチ
        // vtbphuong start change 20131209 #42432
        //products.getColumnModel().getColumn(15).setPreferredWidth(28);          // 宅送
//        products.getColumnModel().getColumn(15).setPreferredWidth(70);		// 施術台
//        products.getColumnModel().getColumn(16).setPreferredWidth(40);		// 削除
//        products.getColumnModel().getColumn(16).setResizable(false);
        //products.getColumnModel().getColumn(16).setPreferredWidth(70);		// 施術台
        //products.getColumnModel().getColumn(17).setPreferredWidth(40);		// 削除
        //products.getColumnModel().getColumn(17).setResizable(false);

        products.getColumnModel().getColumn(12).setPreferredWidth(50);          // 税率
        products.getColumnModel().getColumn(13).setPreferredWidth(60);          // NO
        products.getColumnModel().getColumn(14).setPreferredWidth(80 + bedWidth3);   // 担当者
        products.getColumnModel().getColumn(15).setPreferredWidth(28);		// 指名
        products.getColumnModel().getColumn(16).setPreferredWidth(28);		// アプローチ
        products.getColumnModel().getColumn(17).setPreferredWidth(28);          // 宅送
        products.getColumnModel().getColumn(18).setPreferredWidth(70);		// 施術台
        products.getColumnModel().getColumn(19).setPreferredWidth(40);		// 削除
        products.getColumnModel().getColumn(18).setResizable(false);

        //Luc end edit 20150907 #42432
        // vtbphuong end change 20131209

        //列削除
        if (!this.getSelectedShop().isProportionally() || !this.getSelectedShop().isDisplayProportionally()) {
            //按分無の場合、以下の列を削除する
            products.removeColumn(products.getColumn("按分名"));
            products.removeColumn(products.getColumn("ﾎﾟｲﾝﾄ"));
            products.removeColumn(products.getColumn("割合"));
        }
        if (!this.getSelectedShop().isBed()) {
            //施術台無の場合、以下の列幅を広げる
            products.removeColumn(products.getColumn("施術台"));
        }

        if (SystemInfo.getNSystem() != 2) {
            products.getColumn("宅送").setWidth(0);
            products.getColumn("宅送").setMinWidth(0);
            products.getColumn("宅送").setMaxWidth(0);
            products.getColumn("宅送").setPreferredWidth(0);
            products.doLayout();
            products.setAutoResizeMode(1);
        }
    }

    private void initPointColumn() {
        //列の幅を設定する。
        point.getColumnModel().getColumn(0).setPreferredWidth(160);
        point.getColumnModel().getColumn(1).setPreferredWidth(80);
        point.getColumnModel().getColumn(2).setPreferredWidth(80);
    }

    /**
     * 初期化処理を行う。
     */
    private void init() {
//SystemInfo.info("init start");
        ia.init();

//SystemInfo.info("setTaxRate start");
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
        // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
        ia.getTaxs(salesDate.getDate());

//SystemInfo.info("setTaxRate end");
        //IVS_Thanh start add 2014/07/10 Mashu_お会計表示
        if (ia.getAccountSetting().getDisplaySwitchTechnic() == 3) {
            initIntegration();
            initItemIntegration();
            initCourseIntegration();
        }
        //IVS_Thanh end add 2014/07/10 Mashu_お会計表示

        this.initStaff();
        this.initChargeStaff();
        initPioneerName();
        this.initProducts();
        this.initDiscount();
        this.initPayments();
        this.initTotal();
        this.clear();
        this.setButtonStatus();
        this.initProductClasses();
        ia.getSales().setSalesDate(salesDate.getDate());
        registPanel.setVisible(this.getSelectedShopID() != 0);

        // 施術台リストを取得する
        this.getMstBeds();
        //add by ltthuc 2014/06/11

//SystemInfo.info("アイテムと技術のテーブル幅を初期化 start");
        arrPaymentMethodId_1 = new ArrayList<Integer>();
        int m = editFlag;
        // アイテムと技術のテーブル幅を初期化
        technic.getColumnModel().getColumn(0).setPreferredWidth(200);
        technic.getColumnModel().getColumn(1).setPreferredWidth(50);
        item.getColumnModel().getColumn(0).setPreferredWidth(200);
        item.getColumnModel().getColumn(1).setPreferredWidth(50);
        technicClame.getColumnModel().getColumn(0).setPreferredWidth(200);
        technicClame.getColumnModel().getColumn(1).setPreferredWidth(50);
        itemReturned.getColumnModel().getColumn(0).setPreferredWidth(200);
        itemReturned.getColumnModel().getColumn(1).setPreferredWidth(50);

        payments.getColumnModel().getColumn(3).setPreferredWidth(65);

        // 詳細登録数をクリアする
        detailCount = 0;

//SystemInfo.info("連動ボタンを初期化する start");
        // 連動ボタンを初期化する
        if (SystemInfo.isSosiaGearEnable()) {
            changeSosiaGearButton.setIcon(null);
            changeSosiaGearButton.setPressedIcon(null);
            changeSosiaGearButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            changeSosiaGearButton.setContentAreaFilled(false);
            changeSosiaGearButton.setBorderPainted(true);
            changeSosiaGearButton.setEnabled(false);
        } // SOSIA連動不可の場合には連動ボタンを消去
        else {
            changeSosiaGearButton.setVisible(false);
        }

//SystemInfo.info("列変数の設定 start");
        //列変数の設定
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
        this.settingProductColumn();
        moveButtonPanel.setVisible(false);

        salesDate.setEnabled(true);

//		shopLabel.setVisible(false);
//		shop.setVisible(false);
        customerNo.setText("");
        customerName1.setText("");
        customerName2.setText("");
        initPointPrepaid();
        showPointPrepaid();
        //Luc start edit 20150817 #41948
        //if(SystemInfo.getDatabase().contains("pos_hair_mashu")){

//SystemInfo.info("setShop start");
        ia.setShop(SystemInfo.getCurrentShop());
        if (ia.getSales().getShop() != null) {
            if(ia.getSales().getShop().getUseShopCategory()!=null) {
                if (ia.getSales().getShop().getUseShopCategory() == 1) {
                    MstShopRelations arrMsRelation = new MstShopRelations();
                    arrDataReservationMainstaff = new ArrayList<DataReservationMainstaff>();
                    lastSaleMainstaffs = new ArrayList<DataSalesMainstaff>();
                    initMainStaffData();
                }
            }
        }
        //Luc end edit 20150817 #41948
        //施術時間の変更 start
        txtOpeMinute.setText("");
        txtOpeSecond.setText("");
        //施術時間の変更 end
        //マスタ一括登録 start
        txtCondition.setText("");
        //マスタ一括登録 end
//SystemInfo.info("init end");
    }

    /**
     * 主担当を取得する
     */
    private void initChargeStaff() {
        chargeStaff.removeAllItems();

        for (MstStaff ms : ia.getStaffs()) {
            if (ms.isDisplay()) {
                chargeStaff.addItem(ms);
            }
        }

        chargeStaff.setSelectedIndex(0);
    }

    /**
     * 開拓社名ｺﾝﾎﾞを初期化する
     */
    private void initPioneerName() {
        cboexPioneerName.removeAllItems();

        for (MstStaff ms : ia.getStaffs()) {
            cboexPioneerName.addItem(ms);
        }

        cboexPioneerName.setSelectedIndex(0);
    }

    /**
     * 施術台リストを取得する
     */
    private void getMstBeds() {
// 20170704 yasumoto change start
//        ConnectionWrapper con = SystemInfo.getConnection();
//        beds.setShop(targetShop);
//
//        try {
//            //IVS_LVTu start edit 2015/06/16 #37295
//            //beds.load(con);
//            beds.loadBedInputData(con);
//            //IVS_LVTu end edit 2015/06/16 #37295
//        } catch (SQLException e) {
//            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
//        }
	beds = SystemInfo.getBeds(targetShop);
// 20170704 yasumoto change end
    }

    // IVS_Thanh start add 2014/07/14 Mashu お会計表示
    /**
     * 施術台リストを取得する
     */
    private void getMstBedsByShopCategoryID(Integer shopCategoryID) {
        arrBeds.clear();
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_bed mb");
        sql.append("     inner join mst_bed_relation mbr using (bed_id,shop_id)");
        sql.append("     INNER JOIN mst_shop_relation msr ON  mbr.shop_category_id = msr.shop_category_id");
        sql.append("     and  mbr.shop_id = msr.shop_id");
        sql.append(" where");
        sql.append("         mb.delete_date is null");
        sql.append("     and mb.shop_id = " + SQLUtil.convertForSQL(targetShop.getShopID()));
        sql.append("     and mbr.shop_category_id = " + SQLUtil.convertForSQL(shopCategoryID));
        sql.append(" order by");
        sql.append("      display_seq");
        sql.append("     ,bed_name");
        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            while (rs.next()) {
                MstBed mtc = new MstBed();
                mtc.setData(rs);
                this.arrBeds.add(mtc);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    // IVS_Thanh start add 2014/07/14 Mashu お会計表示

    /**
     * レジ担当者を初期化する。
     */
    private void initStaff() {
        staff.removeAllItems();

        for (MstStaff ms : ia.getStaffs()) {
            //nhanvt start edit 20150123 New request #35011
            //if (ms.isDisplay()) {
                staff.addItem(ms);
            //}
             //nhanvt start edit 20150123 New request #35011
        }

        staff.setSelectedIndex(0);
    }

    /**
     * 売上明細を初期化する。
     */
    private void initProducts() {
        SwingUtil.clearTable(point);
        pointIndexList.clear();

        //----------------------------------------
        // productsテーブルの初期化処理
        //----------------------------------------
        products.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
                },
                //IVS_LVTU start edit 2017/09/12 #25664 [gb]お会計画面：明細部の見切れを調整
                new String[]{
                    "区分", "分類", "商品/技術名/コース名", "単価", "数量", "金額", "割引", "合計", "按分名", "ﾎﾟｲﾝﾄ", "割合", "残", "税率", "NO", "担当者", "指", "AP", "宅送", "施術台", "削除"
                }) {
                //IVS_LVTU end edit 2017/09/12 #25664 [gb]お会計画面：明細部の見切れを調整
                // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
            Class[] types = new Class[]{
                java.lang.Object.class, //"区分"
                java.lang.Object.class, //"分類"
                java.lang.String.class, //"技術/商品/コース名"
                java.lang.Double.class, //"単価"
                java.lang.Double.class, //"数量"
                java.lang.Double.class, //"金額"
                java.lang.Double.class, //"割引"
                java.lang.Double.class, //"合計"
                java.lang.String.class, //"按分名"
                java.lang.Integer.class, //"ﾎﾟｲﾝﾄ"
                java.lang.Integer.class, //"割合"
                java.lang.Double.class, //"残回数"
                java.lang.Object.class, //"税率"
                java.lang.String.class, //"NO"
                java.lang.Object.class, //"担当者"
                java.lang.Boolean.class, //"指名"
                java.lang.Boolean.class, //"AP"
                java.lang.Boolean.class, //"宅送"
                java.lang.Object.class, //"施術台"
                java.lang.String.class//"削除"
            };
            boolean[] canEdit = new boolean[]{
                false, //"区分"
                false, //"分類"
                false, //"技術/商品/コース名"
                false, //"単価"
                true, //"数量"
                true, //"金額"
                true, //"割引"
                false, //"合計"
                false, //"按分名"
                true, //"ﾎﾟｲﾝﾄ"
                true, //"割合"
                false, //"残回数"
                true, //"税率"
                true, //"NO"
                true, //"担当者"
                true, //"指名"
                true, //"AP"
                true,//"宅送"
                true, //"施術台"
                true//"削除"
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        products.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        products.setSelectionBackground(new java.awt.Color(255, 210, 142));
        products.setSelectionForeground(new java.awt.Color(0, 0, 0));
        products.getTableHeader().setReorderingAllowed(false);
        this.initProductsColumn();
        products.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);

        TableColumnModel productsModel = products.getColumnModel();
        productsModel.getColumn(3).setCellEditor(new DoubleCellEditor(new JTextField()));
        //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        productsModel.getColumn(4).setCellEditor(new HairInputAccountCellEditor(new JTextField()));//数量
        //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        productsModel.getColumn(5).setCellEditor(new DoubleCellEditor(new JTextField()));
        productsModel.getColumn(6).setCellEditor(new DoubleCellEditor(new JTextField()));
        //Luc start edit 20150908 #42432
        if(this.getSelectedShop().isProportionally() && this.getSelectedShop().isDisplayProportionally()) {
        productsModel.getColumn(9).setCellEditor(new IntegerCellEditor(new JTextField()));
        }
        //Luc end edit 20150908 #42432
        productsModel.getColumn(10).setCellEditor(new IntegerCellEditor(new JTextField()));

        SwingUtil.setJTableHeaderRenderer(products, SystemInfo.getTableHeaderRenderer());
        products.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                productsFocusGained(evt);
            }
        });
        products.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //IVS_LVTu start delete 2015/06/09 #36636
                //productsPropertyChange(evt);
                //IVS_LVTu end delete 2015/06/09 #36636


            }
        });
        products.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                productsKeyPressed(evt);
            }
        });

        SwingUtil.clearTable(products);
        detailCount = 0;
    }

    private JComboBox getProductDivisionName(DataSalesDetail dsd, int dIndex, int pIndex) {
        JComboBox techName = new JComboBox();
        techName.removeAllItems();
        techName.addItem(pIndex == -1 ? dsd.getProductDivisionName() : "");
        techName.addItem(dIndex);
        techName.addItem(-1 < pIndex ? pIndex : null);
        techName.setSelectedIndex(0);
        techName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        techName.setOpaque(false);
        return techName;
    }

    /**
     * 指名フラグを取得する
     */
    private JCheckBox getStaffDesignatedFlag(boolean designated) {
        JCheckBox designatedFlg = new JCheckBox();
        //nhanvt start edit 20141107 Bug #32098
//        if (ia.getSales().getSlipNo() != null || this.getSelectedShop().isDesignatedAssist()) {
//            designatedFlg.setSelected(designated);
//        }
        designatedFlg.setSelected(designated);
         //nhanvt end edit 20141107 Bug #32098
        designatedFlg.setOpaque(false);
        designatedFlg.setHorizontalAlignment(SwingConstants.CENTER);
        designatedFlg.addActionListener(
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        changeTechnicStaffDesignated();
                    }
                });
        return designatedFlg;
    }

    /**
     * 指名データを変更する
     */
    private void changeTechnicStaffDesignated() {
        int index = this.getProductsIndex(0);
        int row = products.getSelectedRow();
        DataSalesDetail dsd = (DataSalesDetail) ia.getSales().get(index);

        int col = designatedCol;
        if (dsd.getProductDivision() == 2) {
            col = approachedCol;
        }
        boolean flag = ((JCheckBox) products.getValueAt(row, col)).isSelected();

        if (0 < dsd.size()) {
            // 按分が存在するなら１行目はメニュー指名、２行目以降は按分指名
            if (getProductsIndex(1) == null) {
                //１行目
                dsd.setDesignated(flag);
            } else if (0 <= getProductsIndex(1)) {
                //２行目以降
                dsd.get(this.getProductsIndex(1)).setDesignated(flag);
            }
        } else {
            // 按分が存在しなければメニュー指名
            dsd.setDesignated(flag);
        }
    }

    /**
     * アプローチフラグを取得する
     */
    private JCheckBox getStaffApproachedFlag(boolean approached) {
        JCheckBox approachedFlg = new JCheckBox();
        approachedFlg.setSelected(approached);
        approachedFlg.setOpaque(false);
        approachedFlg.setHorizontalAlignment(SwingConstants.CENTER);
        approachedFlg.addActionListener(
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        changeTechnicStaffApproached();
                    }
                });
        return approachedFlg;
    }

    /**
     * 宅送フラグを取得する
     */
    private JCheckBox getPickupCheckbox(boolean pickupChecked) {
        JCheckBox pickup = new JCheckBox();
        pickup.setSelected(pickupChecked);
        pickup.setOpaque(false);
        pickup.setHorizontalAlignment(SwingConstants.CENTER);
        pickup.addActionListener(
                new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        changePickupChecked();
                    }
                });
        return pickup;
    }

    /**
     * アプローチデータを変更する
     */
    private void changeTechnicStaffApproached() {
        int index = this.getProductsIndex(0);
        int row = products.getSelectedRow();
        DataSalesDetail dsd = (DataSalesDetail) ia.getSales().get(index);
        boolean flag = ((JCheckBox) products.getValueAt(row, approachedCol)).isSelected();

        if (0 < dsd.size()) {
            // 按分が存在するなら１行目はメニュー指名、２行目以降は按分指名
            if (getProductsIndex(1) == null) {
                //１行目
                dsd.setApproached(flag);
            }

        } else {
            // 按分が存在しなければメニュー指名
            dsd.setApproached(flag);
        }
    }

    private void changePickupChecked() {
        int index = this.getProductsIndex(0);
        int row = products.getSelectedRow();
        DataSalesDetail dsd = (DataSalesDetail) ia.getSales().get(index);
        boolean flag = ((JCheckBox) products.getValueAt(row, pickupCol)).isSelected();

        if (0 < dsd.size()) {
            // 按分が存在するなら１行目はメニュー指名、２行目以降は按分指名
            if (getProductsIndex(1) == null) {
                //１行目
                dsd.setPickUp(flag);
            }

        } else {
            // 按分が存在しなければメニュー指名
            dsd.setPickUp(flag);
        }
    }
    
    /**
     * 率を表示
     * IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
     */
    private JComboBox getTaxComboBox(Integer indx) {
        final JComboBox taxCombo = new JComboBox(ia.getTaxs(ia.getSales().getSalesDate()).toArray());
        if (indx.intValue() < 0) {
            indx = new Integer(0);
        }
        taxCombo.setSelectedIndex(indx);
        taxCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        //スタッフが変更されたときの処理を追加
        taxCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeProducts();
                setTotal();
            }
        });
        
        return taxCombo;
    }

    /**
     * スタッフコンボを取得する
     */
    private JComboBox getStaffComboBox(Integer staffID) {
        final JComboBox staffCombo = new JComboBox(ia.getStaffs().toArray());
        if (staffID.intValue() < 0) {
            staffID = new Integer(0);
        }
        staffCombo.setSelectedIndex(staffID);
        staffCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        //スタッフが変更されたときの処理を追加
        staffCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeProducts();
                // setTotal();
            }
        });

        return staffCombo;
    }

    /**
     * ベッド選択用JComboBoxを取得する。
     *
     * @param bedID 選択状態にするベッドＩＤ
     * @return ベッド選択用JComboBox
     */
    private JComboBox getBedComboBox(Integer bedID) {
        JComboBox bedCombo = new JComboBox();
        bedCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        bedCombo.addItem(new MstBed());
        bedCombo.setSelectedIndex(0);

        for (MstBed bed : beds) {
            bedCombo.addItem(bed);
        }

        this.setBed(bedCombo, bedID);

        bedCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeProducts();
                setTotal();
            }
        });

        return bedCombo;
    }

    // IVS_Thanh start add 2014/07/14 Mashu お会計表示
    /**
     * ベッド選択用JComboBoxを取得する。
     *
     * @param bedID,shopCategoryID 選択状態にするベッドＩＤ
     * @return ベッド選択用JComboBox
     */
    private JComboBox getBedComboBox(Integer bedID,Integer shopCategoryID) {
        JComboBox bedCombo = new JComboBox();
        bedCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        bedCombo.addItem(new MstBed());
        bedCombo.setSelectedIndex(0);
        if (targetShop.getUseShopCategory() == 1 && (shopCategoryID !=null && shopCategoryID > 0)) {
            this.getMstBedsByShopCategoryID(shopCategoryID);
             for (MstBed bed : arrBeds) {
                bedCombo.addItem(bed);
            }
        }
        else
        {
            for (MstBed bed : beds) {
                bedCombo.addItem(bed);
            }
        }

        this.setBed(bedCombo, bedID);

        bedCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeProducts();
                //IVS_LVTu start delete 2015/06/11 #36636
                //setTotal();
                //IVS_LVTu start delete 2015/06/11 #36636
            }
        });

        return bedCombo;
    }
    // IVS_Thanh end add 2014/07/14 Mashu お会計表示

    /**
     * 指定したベッドＩＤをベッド選択用JComboBoxで選択状態にする。
     *
     * @param bedCombo ベッド選択用JComboBox
     * @param bedID ベッドＩＤ
     */
    private void setBed(JComboBox bedCombo, Integer bedID) {
        for (int i = 0; i < bedCombo.getItemCount(); i++) {
            // start add 2017/06/13 #4095
            //if (((MstBed) bedCombo.getItemAt(i)).getBedID() == bedID) {
            if (String.valueOf(((MstBed) bedCombo.getItemAt(i)).getBedID()).equals(String.valueOf(bedID))) {
            // end add 2017/06/13 #4095
                bedCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * 削除ボタンを取得する
     */
    private JButton getDeleteButton() {
        JButton delButton = new JButton();
        delButton.setBorderPainted(false);
        delButton.setContentAreaFilled(false);
        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
        delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
        delButton.setSize(48, 25);
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProduct();
                tempRow = products.getSelectedRow();
                tempCol = products.getSelectedColumn();
                //showPointPrepaid();
            }
        });
        return delButton;
    }

    /**
     * NULLフィールドを取得する
     */
    private JFormattedTextField getNullField() {
        JFormattedTextField ftf = new JFormattedTextField(
                FormatterCreator.createMaskFormatter("", null, ""));
        ftf.setHorizontalAlignment(JTextField.CENTER);
        ftf.addFocusListener(SystemInfo.getSelectText());
        ftf.setBorder(null);
        ftf.setText("");
        return ftf;
    }

    /**
     * 売上明細を１行追加する。
     *
     * @param dsd 売上明細
     */
    private void addProductRow(DataSalesDetail dsd, boolean lordFlg) {
        this.addProductRow(dsd, lordFlg, true);
    }

    private void addProductRow(DataSalesDetail dsd, boolean lordFlg, boolean isCustomerDisplayCalculate) {
//SystemInfo.info("addProductRow start");
		DefaultTableModel model = (DefaultTableModel) products.getModel();
        DataSalesProportionally dsp = null;
        //add by ltthuc 2014/06/17
        if (payments.getCellEditor() != null) {
            payments.getCellEditor().stopCellEditing();
        }
        Product mp = dsd.getProduct();
        //add by ltthuc 2014/06/11 Task #24575
        arrPaymentMethodId_1.add(mp.getPrepa_id());
        //end add ltthuc
        //売上明細
        // 技術クレームと返品のときだけ単価を変更
        if (!lordFlg) {
            switch (dsd.getProductDivision().intValue()) {
                case 3:
                    dsd.setProductValue(new Long(0));
                    break;
                case 4:
                    dsd.setProductValue(new Long(dsd.getProductValue() * -1));
            }
        }

        try {

            if (isCustomerDisplayCalculate && mp != null) {
                Long price = dsd.getProductValue() * dsd.getProductNum() - dsd.getDiscountValue();
                if (price != null && SystemInfo.isUseCustomerDisplay()) {
                    CustomerDisplay cd = this.getCustomerDisplay();
                    if (cd != null) {
                        cd.clearScreen();
                        cd.putStr(Long.toString(price), 'r', 0);
                        cd.putStr("ｼｮｳｹｲ", 'l', 1);
                        cd.putStr(Long.toString(ia.getTotal(3).getValue() + price), 'r', 1);
                    }
                }
            }

        } catch (PortInUseException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
//SystemInfo.info("model.addRow start");
        model.addRow(new Object[]{
                    getProductDivisionName(dsd, detailCount, -1),
                    //Luc start edit 20150129
                    dsd.getProductDivision()== 5 ? dsd.getCourse().getCourseClass() :
                    (dsd.getProductDivision()== 6? dsd.getConsumptionCourse().getConsumptionCourseClass():mp.getProductClass()),

                    dsd.getProductDivision() == 5 ? dsd.getCourse().getCourseClass():
                    (dsd.getProductDivision() == 6?dsd.getConsumptionCourse().getCourseName():mp.getProductName()),
                    //Luc end edit 20150129

                    //Luc start edit 20150527 ticket 37040
                    dsd.getProductDivision() == 3 ? 0 : ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    //dsd.getProductDivision() == 3 ? 0 : ia.getAccountSetting().getDisplayValue(dsd.getProductValue(),dsd.getProductNum().longValue(),dsd.getDiscountValue(), ia.getTaxRate()),
                    //Luc end  edit 20150527 ticket 37040

                    dsd.getProductNum(),

                    //Luc start edit 20150527 ticket 37040
                    ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    //ia.getAccountSetting().getDisplayValue(dsd.getProductValue(),dsd.getProductNum().longValue(),dsd.getDiscountValue(), ia.getTaxRate()),
                    //Luc end  edit 20150527 ticket 37040

                    dsd.getDiscount(),
                    //Luc start edit 20150527 ticket 37040
                    //ia.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate()),
                    ia.getAccountSetting().getDisplayValue(dsd.getValue(),dsd.getProductNum().longValue(), dsd.getDiscountValue(), dsd.getTaxRate()),
                    //Luc end  edit 20150527 ticket 37040
                    getNullField(),
                    getNullField(),
                    getNullField(),
                    getNullField(),
                    // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
                    getTaxComboBox(ia.getIndexByTaxRate(dsd.getTaxRate())),
                    //Luc start add 20150907 #42432
                     //スタッブNO
                    dsd.getStaff().getStaffNo(),
                    //スタッブコンボボックス
                    //Luc start add 20150907 #42432
                    getStaffComboBox(ia.getStaffs().getIndexByID(dsd.getStaff().getStaffID())),
                    //			getStaffDesignatedFlag( dsd.getDesignated() ),
                    //			dsd.getProductDivision() == 1 ? getStaffApproachedFlag( dsd.getApproached() ) : getNullField(),
                    //Luc start edit 20150225 Bug #35220
                    //(dsd.getProductDivision() == 1 || dsd.getProductDivision() == 5) ? getStaffDesignatedFlag(dsd.getDesignated()) : getNullField(),
                    (dsd.getProductDivision() == 1 || dsd.getProductDivision() == 5 ||dsd.getProductDivision() == 6) ? getStaffDesignatedFlag(dsd.getDesignated()) : getNullField(),
                    //Luc start edit 20150225 Bug #35220
                    (dsd.getProductDivision() == 1 || dsd.getProductDivision() == 5) ? getStaffApproachedFlag(dsd.getApproached()) : dsd.getProductDivision() == 2 ? getStaffDesignatedFlag(dsd.getDesignated()) : getNullField(),
                    // vtbphuong start add 20131209
                    //(dsd.getProductDivision() == 2) ? getPickupCheckbox(false) : getNullField(),
                    //(dsd.getProductDivision() == 2) ? getPickupCheckbox(dsd.getPickUp()) : getNullField(),
                    (dsd.getProductDivision() == 2 && SystemInfo.getNSystem() == 2) ? getPickupCheckbox(dsd.getPickUp()) : getNullField(),
                    //vtbphuong end add 20131209
                    // IVS_Thanh start edit 2014/07/14 Mashu_お会計表示
                    //dsd.getProductDivision().intValue() == 1 ? getBedComboBox(dsd.getBed().getBedID()) : getNullField(),
                    dsd.getProductDivision().intValue() == 1 ? getBedComboBox(dsd.getBed().getBedID(),dsd.getProduct().getProductClass().getShopCategoryID()) : getNullField(),
                    // IVS_Thanh start edit 2014/07/14 Mashu_お会計表示
                    getDeleteButton()
                });
        //スタッフ
        ia.getSales().get(detailCount).setStaff(dsd.getStaff());
        //指名
        ia.getSales().get(detailCount).setDesignated(dsd.getDesignated());
        //アプローチ
        ia.getSales().get(detailCount).setApproached(dsd.getApproached());

        //調査用ろぐ
//        //SystemInfo.info( "売上明細：" + mp.getProductClass().getProductClassName() + " : " + mp.getProductName());
//        //SystemInfo.info( "売上明細：" + ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), ia.getTaxRate())
//                + " : " + dsd.getDiscount()
//                + " : " + ia.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate()));

        //按分
        //IVS_LVTu start edit 2015/10/07 Bug #43006
        //if (this.getSelectedShop().isDisplayProportionally()) {
        if (this.getSelectedShop().isDisplayProportionally() || (dsd.getShop().isDisplayProportionally() && this.getSelectedShop().getShopID() == 0)) {
        //IVS_LVTu end edit 2015/10/07 Bug #43006
            for (int proNum = 0; proNum < dsd.size(); proNum++) {
                dsp = dsd.get(proNum);
//SystemInfo.info("明細按分"+proNum);
                model.addRow(new Object[]{
                            getProductDivisionName(dsd, detailCount, proNum),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            dsp.getProportionally().getProportionally().getProportionallyName(),
                            dsp.getPoint(),
                            dsp.getRate(),
                            getNullField(),
                            getNullField(),
                            //Luc start edit 20150914 #42626
                            dsd.get(proNum).getStaff().getStaffNo(),
                            //Luc end add 20150914 #42626
                            getStaffComboBox(ia.getStaffs().getIndexByID(dsd.get(proNum).getStaff().getStaffID())),
                            getStaffDesignatedFlag(dsd.get(proNum).getDesignated()),
                            getNullField(),
                            getNullField()
                        });

                //スタッフ
                ia.getSales().get(detailCount).get(proNum).setStaff(dsp.getStaff());
                //指名
                ia.getSales().get(detailCount).get(proNum).setDesignated(dsp.getDesignated());
			}

        }

        //伝票詳細登録数
        detailCount++;
        // ポイント割引設定に反映
        if (SystemInfo.isUsePointcard()) {
            this.showDiscountPoint();
        }
    }

    /**
     * 売上明細を１行追加する。
     *
     * @param dsd 売上明細
     */
    //Luc start add 20141205 Bug #33551
    private boolean isCoursIsUse( Integer contractNo,Integer contractDetailNo,Integer contractShopId) {
        boolean flg = false;
        StringBuilder sql = new StringBuilder();
        sql.append(" Select * from ");
        sql.append("  data_reservation_detail drd inner join data_reservation dr ");
        sql.append("  on drd.shop_id = dr.shop_id and drd.reservation_no = dr.reservation_no  ");
        sql.append("  where  ");
        sql.append(" reservation_datetime >= current_date  ");
        sql.append(" and contract_no = " + contractNo);
        sql.append(" and contract_shop_id = " + contractShopId);
        sql.append(" and contract_detail_no =" + contractDetailNo);
        sql.append(" and dr.delete_date is null and drd.delete_date is null and drd.course_flg = 2");

        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            while (rs.next()) {
                flg = true;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

        return flg;
    }
    //Luc end add 20141205 Bug #33551
    private void addCourseRow(DataSalesDetail dsd, boolean lordFlg) {
        DefaultTableModel model = (DefaultTableModel) products.getModel();
        DataSalesProportionally dsp = null;
        Course c = dsd.getCourse();
        //Start edit 20131105 lvut show course(producd_division in (7,9))
        boolean bedSelectable = false;
        if (dsd.getProductDivision().intValue() == 1
                || dsd.getProductDivision().intValue() == 5
                || dsd.getProductDivision().intValue() == 7
                || dsd.getProductDivision().intValue() == 6) {
            bedSelectable = true;
        }
        //Luc start edit 20141226 fix error when load contract were cancel
        if( dsd.getDataContract()!=null) {
            if((dsd.getSlipNo()!=null && dsd.getDataContract().getContractNo() != null
                && dsd.getDataContract().getContractDetailNo()!= null && dsd.getShop().getShopID()!=null
                && isCoursIsUse(dsd.getDataContract().getContractNo(), dsd.getDataContract().getContractDetailNo(), dsd.getShop().getShopID()))) {
                dsd.setIsEditable(false);

            }
        }else {
            if(dsd.getSlipNo()!=null) {
                dsd.setIsEditable(false);
            }
        }
        //Luc start edit 20141226 fix error when load contract were cancel

        //End edit 20131105 lvut show course(producd_division in (7,9))
        //売上明細
        model.addRow(new Object[]{
                    getProductDivisionName(dsd, detailCount, -1),
                    c.getCourseClass(),
                    c,
                    dsd.getProductDivision() == 3 ? 0 : ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    c.getNum(),
                    ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    dsd.getDiscount(),
                    ia.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), dsd.getTaxRate()),
                    getNullField(),
                    getNullField(),
                    getNullField(),
                    getNullField(),
                    // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
                    getTaxByProductDivision(dsd),
                    //Luc start add 20150907 #42432
                     //スタッブNO
                    dsd.getStaff().getStaffNo(),
                    //スタッブコンボボックス
                    //Luc start add 20150907 #42432
                    getStaffComboBox(ia.getStaffs().getIndexByID(dsd.getStaff().getStaffID())),
                    getStaffDesignatedFlag(dsd.getDesignated()), //指名のチェックボックスは表示
                    getNullField(), //APのチェックボックスは表示しない
                    // vtbphuong start add 20131210
                    getNullField(),
                    // vtbphuong end add 20131210
                    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                    //bedSelectable ? getBedComboBox(dsd.getBed().getBedID()) : getNullField(),
                    bedSelectable ? getBedComboBox(dsd.getBed().getBedID(),dsd.getCourse().getCourseClass().getShopCategoryID()) : getNullField(),
                    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                    dsd.isEditable() ? getDeleteButton() : getNullField()
                });

        //スタッフ
        ia.getSales().get(detailCount).setStaff(dsd.getStaff());
        //指名
        ia.getSales().get(detailCount).setDesignated(dsd.getDesignated());
        //アプローチ
        ia.getSales().get(detailCount).setApproached(dsd.getApproached());

        //調査用ろぐ
//        SystemInfo.getLogger().log(Level.INFO, "売上明細：" + c.getCourseClass().getCourseClassName() + " : " + c.getCourseName());
//        SystemInfo.getLogger().log(Level.INFO, "売上明細：" + ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), ia.getTaxRate())
//                + " : " + dsd.getDiscount()
//                + " : " + ia.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate()));

        //按分
        if (this.getSelectedShop().isDisplayProportionally()) {

            for (int proNum = 0; proNum < dsd.size(); proNum++) {
                dsp = dsd.get(proNum);

                model.addRow(new Object[]{
                            getProductDivisionName(dsd, detailCount, proNum),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            dsp.getProportionally().getProportionally().getProportionallyName(),
                            dsp.getPoint(),
                            dsp.getRate(),
                            getNullField(),
                            getNullField(),
                            getStaffComboBox(ia.getStaffs().getIndexByID(dsd.get(proNum).getStaff().getStaffID())),
                            getStaffDesignatedFlag(dsd.get(proNum).getDesignated()),
                            getNullField(),
                            getNullField()
                        });

                //スタッフ
                ia.getSales().get(detailCount).get(proNum).setStaff(dsp.getStaff());
                //指名
                ia.getSales().get(detailCount).get(proNum).setDesignated(dsp.getDesignated());
            }

        }

        //伝票詳細登録数
        detailCount++;

        // ポイント割引設定に反映
        if (SystemInfo.isUsePointcard()) {
            this.showDiscountPoint();
        }
    }

    /**
     * 売上明細を１行追加する。
     *
     * @param dsd 売上明細
     */
    private void addConsumptionCourseRow(DataSalesDetail dsd, boolean lordFlg) {
        DefaultTableModel model = (DefaultTableModel) products.getModel();
        DataSalesProportionally dsp = null;
        ConsumptionCourse cc = dsd.getConsumptionCourse();

        boolean bedSelectable = false;
        if (dsd.getProductDivision().intValue() == 1
                || dsd.getProductDivision().intValue() == 5
                || dsd.getProductDivision().intValue() == 6) {
            bedSelectable = true;
        }
        // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
        // 売上日時点で有効な通常税率
        dsd.setTaxRate(cc.getTaxRate() == null ? ia.getTaxRate() : cc.getTaxRate());

        //売上明細
        model.addRow(new Object[]{
                    getProductDivisionName(dsd, detailCount, -1),
                    cc.getConsumptionCourseClass(),
                    //			cc.getCourseName(),
                    cc,
                    dsd.getProductDivision() == 3 ? 0 : ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    //			dsd.getProductNum(),
                    dsd.getConsumptionNum(),
                    ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), dsd.getTaxRate()),
                    dsd.getDiscount(),
                    ia.getAccountSetting().getDisplayValue(dsd.getValueForConsumption(), dsd.getDiscountValue(), dsd.getTaxRate()),
                    getNullField(),
                    getNullField(),
                    getNullField(),
                    cc.getConsumptionRestNum(),
                    getNullField(),
                    //Luc start add 20150907 #42432
                     //スタッブNO
                    dsd.getStaff().getStaffNo(),
                    //スタッブコンボボックス
                    //Luc start add 20150907 #42432
                    getStaffComboBox(ia.getStaffs().getIndexByID(dsd.getStaff().getStaffID())),
                    //			getStaffDesignatedFlag( dsd.getDesignated() ),
                    //			dsd.getProductDivision() == 1 ? getStaffApproachedFlag( dsd.getApproached() ) : getNullField(),

                    getStaffDesignatedFlag(dsd.getDesignated()), //指名のチェックボックスは表示
                    getNullField(), //APのチェックボックスは表示しない
                    // vtbphuong start add 20131210
                    getNullField(),
                    // vtbphuong end add 20131210
                    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                    //bedSelectable ? getBedComboBox(dsd.getBed().getBedID()) : getNullField(),
                    bedSelectable ? getBedComboBox(dsd.getBed().getBedID(),dsd.getConsumptionCourse().getConsumptionCourseClass().getShopCategoryID()) : getNullField(),
                    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                    getDeleteButton()
                });

        //スタッフ
        ia.getSales().get(detailCount).setStaff(dsd.getStaff());
        //指名
        ia.getSales().get(detailCount).setDesignated(dsd.getDesignated());
        //アプローチ
        ia.getSales().get(detailCount).setApproached(dsd.getApproached());

        //調査用ろぐ
//        SystemInfo.getLogger().log(Level.INFO, "売上明細：" + cc.getConsumptionCourseClass().getCourseClassName() + " : " + cc.getCourseName());
//        SystemInfo.getLogger().log(Level.INFO, "売上明細：" + ia.getAccountSetting().getDisplayValue(dsd.getProductValue(), ia.getTaxRate())
//                + " : " + dsd.getDiscount()
//                + " : " + ia.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate()));

        //按分
        if (this.getSelectedShop().isDisplayProportionally()) {

            for (int proNum = 0; proNum < dsd.size(); proNum++) {
                dsp = dsd.get(proNum);

                model.addRow(new Object[]{
                            getProductDivisionName(dsd, detailCount, proNum),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            getNullField(),
                            dsp.getProportionally().getProportionally().getProportionallyName(),
                            dsp.getPoint(),
                            dsp.getRate(),
                            getNullField(),
                            getNullField(),
                            getStaffComboBox(ia.getStaffs().getIndexByID(dsd.get(proNum).getStaff().getStaffID())),
                            getStaffDesignatedFlag(dsd.get(proNum).getDesignated()),
                            getNullField(),
                            getNullField()
                        });

                //スタッフ
                ia.getSales().get(detailCount).get(proNum).setStaff(dsp.getStaff());
                //指名
                ia.getSales().get(detailCount).get(proNum).setDesignated(dsp.getDesignated());
            }

        }

        //伝票詳細登録数
        detailCount++;

        // ポイント割引設定に反映
        if (SystemInfo.isUsePointcard()) {
            this.showDiscountPoint();
        }
    }

    private void addProductNum(int row) {
        int index = getProductsIndex(0);
        Integer temp = (Integer) products.getValueAt(row, 5);

        products.setValueAt(++temp, row, 5);

        products.setValueAt(ia.getSales().get(index).getValue(), row, 6);
        products.setValueAt(ia.getSales().get(index).getTotal(), row, 8);
    }

    /**
     * 売上明細の内容を変更したときの処理
     *
     */
    private void changeProducts() {
        int row = products.getSelectedRow();
        int col = products.getSelectedColumn();
        if (row < 0 || col < 0) {
            return;
        }

        int index = getProductsIndex(0);

        if (col == 3) {
            //単価
            if (getProductsIndex(1) == null) {
                //Long price = Double.valueOf(products.getValueAt(row, col).toString()).longValue();
                //ia.getSales().get( index ).setProductValue(price);
            }
        } else if (col == 4) {
            //数量
            if (getProductsIndex(1) == null) {
                if (ia.getSales().get(index).getProductDivision() == 6) {
                    //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
                    //コース消化の場合
                    Double num = 0d;
                    Double numOld = 0d;
                    if (ia.getSales().get(index).getConsumptionNum() != null) {
                        numOld = ia.getSales().get(index).getConsumptionNum();
                    }
                    try {
                        num = (Double) products.getValueAt(row, col);
                        ConsumptionCourse cc = ia.getSales().get(index).getConsumptionCourse();
                        products.setValueAt(((cc.getConsumptionRestNum() + numOld) - num), row, restNumCol);
                        cc.setConsumptionRestNum(((cc.getConsumptionRestNum() + numOld) - num));
                        //IVS_LVTU start edit 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                        ia.getSales().get(index).setProductNum(num.intValue());
                        ia.getSales().get(index).setConsumptionNum(num);
                        ia.getSales().get(index).getConsumptionCourse().setConsumptionRestNum(cc.getConsumptionRestNum());
						//IVS_LVTU end edit 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                    //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
                    } catch (Exception e) {
                    }
                    ia.getSales().get(index).setConsumptionNum(num);
                } else if (ia.getSales().get(index).getProductDivision() == 5) {
                    //コース契約の場合
                    if (!ia.getSales().get(index).isEditable()) {
                        products.setValueAt(ia.getSales().get(index).getCourse().getNum(), row, col);
                        editDataContract = true;
                        return;
                    }
                    Object o = products.getValueAt(row, col);
                    if (o instanceof Integer) {
                        ia.getSales().get(index).getCourse().setNum(((Integer) o).intValue());
                    } else if (o instanceof Double) {
                        ia.getSales().get(index).getCourse().setNum(((Double) o).intValue());
                    }

                    DataSalesDetail dsd = ia.getSales().get(index);
                    if (dsd.getTmpContractNo() != null) {
                        //未契約コースの消化回数を変更した場合

                        //消化コースリストの残回数も変更する
                        for (int i = 0; i < consumptionCourse.getRowCount(); i++) {
                            ConsumptionCourse c = (ConsumptionCourse) consumptionCourse.getValueAt(i, 0);
                            if (dsd.getTmpContractNo().equals(c.getTmpContractNo())) {
                                //明細行の未契約コースに紐付く消化コースのレコード
                                c.setNum(ia.getSales().get(index).getCourse().getNum());
                                c.setConsumptionRestNum(c.getNum() - c.getConsumptionNum());
                                //IVS_TMTrong start edit 2015/10/22 New request #42427
                                //consumptionCourse.setValueAt(c, i, 0);
                                //consumptionCourse.setValueAt(NumberFormat.getInstance().format(c.getConsumptionRestNum()), i, 1);
                                consumptionCourse.setValueAt(c, i, 0);
                                consumptionCourse.setValueAt(NumberFormat.getInstance().format(c.getConsumptionRestNum()), i, 2);
                                //IVS_TMTrong end edit 2015/10/22 New request #42427
                                break;
                            }
                        }

                        //明細行に消化コースのデータがあればその金額も変更する
                        if (consumptionCourseMap.containsKey(dsd.getCourse().getCourseClass().getCourseClassId())) {
                            Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(dsd.getCourse().getCourseClass().getCourseClassId());
                            if (courseMap.containsKey(dsd.getTmpContractNo())) {
                                //契約前コースに対する消化コース明細がある場合

                                //契約前コースに対する消化コースの元データの値を変更
                                DataSalesDetail target = null;
                                for (DataSalesDetail d : ia.getSales()) {
                                    if (d.getConsumptionCourse() != null
                                            && dsd.getTmpContractNo().equals(d.getTmpContractNo())) {
                                        //コース消化のデータで仮契約番号が同じデータ

										//IVS_LVTU start edit 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                                        //long price = new BigDecimal(Double.valueOf(products.getValueAt(row, 5).toString())).divide(new BigDecimal(products.getValueAt(row, col).toString()), 0, RoundingMode.UP).longValue();
                                        //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                        long price = 0l;
                                        if(!"".equals(products.getValueAt(row, col).toString()) && Double.valueOf(products.getValueAt(row, col).toString()) != 0) {
                                            price = getConsumptionDisplayValue(index, row);
                                        }
                                        //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                        d.setProductValue(price);
//                                      Long num =new BigDecimal(products.getValueAt(row, col).toString()).longValue();
//                                      Long use = new BigDecimal(d.getProductNum()).longValue();
                                        double num = new BigDecimal(products.getValueAt(row, col).toString()).doubleValue();
                                        double use = d.getConsumptionNum();
                                        Double balance =  new Double(num-use);
                                        d.getConsumptionCourse().setConsumptionRestNum(balance);
										//IVS_LVTU end edit 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される

                                        target = d;
                                        break;
                                    }


                                }

                                //契約前コースに対する消化コースの表示値を変更
                                for (int i = 0; i < products.getRowCount(); i++) {
                                    if (products.getValueAt(i, 2) instanceof ConsumptionCourse) {
                                        ConsumptionCourse c = (ConsumptionCourse) products.getValueAt(i, 2);
                                        if (dsd.getTmpContractNo().equals(c.getTmpContractNo())) {
                                            c.setNum(ia.getSales().get(index).getCourse().getNum());
                                            //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                            long price = 0l;
                                            if(!"".equals(products.getValueAt(row, col).toString()) && Double.valueOf(products.getValueAt(row, col).toString()) != 0) {
                                                price = getConsumptionDisplayValue(index, row);
                                            }
                                            //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                            c.setPrice(price);
                                            products.setValueAt(c, i, 2);
                                            products.setValueAt(c.getConsumptionRestNum(), i, restNumCol);

                                            products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getProductValue(), ia.getSales().get(index).getTaxRate()), i, 3);
                                            products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getProductValue(), ia.getSales().get(index).getTaxRate()), i, 5);
                                            products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getValueForConsumption(), target.getDiscountValue(), ia.getSales().get(index).getTaxRate()), i, 7);
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                    }
					//IVS_LVTU start add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                    changeDataConsumptionCourse(row, col);
					//IVS_LVTU end add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                } else {
                    //コース消化以外の場合
                    Object o = products.getValueAt(row, col);
                    if (o instanceof Integer) {
                        ia.getSales().get(index).setProductNum(((Integer) o).intValue());
                    } else if (o instanceof Double) {
                        ia.getSales().get(index).setProductNum(((Double) o).intValue());
                    }
                }
            }
        } else if (col == 5) {
            //金額
            if (getProductsIndex(1) == null) {
                if (ia.getSales().get(index).getProductDivision() == 5) {
                    if (!ia.getSales().get(index).isEditable()) {
                        products.setValueAt(ia.getSales().get(index).getProductValue(), row, col);
                        editDataContract = true;
                        return;
                    }
                }

                Long num = Double.valueOf(products.getValueAt(row, col).toString()).longValue();
                if (ia.getAccountSetting().getDisplayPriceType() == 1) {
                    //IVS_LVTU start edit 2017/05/04 Bug #50673 [gb]お会計画面、明細の合計金額の計算不備
                    //Double a = new Double(Math.floor(num + (num * ia.getTaxRate())));
                    long signum = (long) Math.signum(num);
                    if (signum == 0) {
                        signum = 1;
                    }
                    //IVS_LVTU end edit 2017/05/04 Bug #50673 [gb]お会計画面、明細の合計金額の計算不備
                    Double a = signum * (new Double(Math.floor(Math.abs(num) + (Math.abs(num) * ia.getSales().get(index).getTaxRate()))));
                    ia.getSales().get(index).setProductValue(a.longValue());
                } else {
                    ia.getSales().get(index).setProductValue(num);
                }

                if (ia.getSales().get(index).getProductDivision() == 5
                        && ia.getSales().get(index).getTmpContractNo() != null) {
                    //コース契約前のデータの場合
                    DataSalesDetail dsd = ia.getSales().get(index);

                    if (consumptionCourseMap.containsKey(dsd.getCourse().getCourseClass().getCourseClassId())) {
                        Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(dsd.getCourse().getCourseClass().getCourseClassId());
                        if (courseMap.containsKey(dsd.getTmpContractNo())) {
                            //契約前コースに対する消化コース明細がある場合
                            DataSalesDetail target = null;

                            //契約前コースに対する消化コースの元データの値を変更
                            for (DataSalesDetail d : ia.getSales()) {
                                if (d.getConsumptionCourse() != null
                                        && dsd.getTmpContractNo().equals(d.getTmpContractNo())) {
									//IVS_LVTU start edit 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
//                                    double courseValue = ia.getAccountSetting().getDisplayValue(
//                                        ia.getSales().get(index).getValue(),ia.getSales().get(index).getProductNum().longValue(),
//                                        ia.getSales().get(index).getDiscountValue(), ia.getSales().get(index).getTaxRate());
                                    //コース消化のデータで仮契約番号が同じデータ
                                    //long price = new BigDecimal(Double.valueOf(products.getValueAt(row, col).toString())).divide(new BigDecimal(dsd.getCourse().getNum().longValue()), 0, RoundingMode.UP).longValue();
                                    //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                    long price = 0l;
                                    if(dsd.getCourse().getNum() != 0) {
                                        price = getConsumptionDisplayValue(index, row);
                                    }
                                    //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                    d.setProductValue(price);
                                    target = d;
                                    break;
									//IVS_LVTU end edit 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                                }
                            }

                            //契約前コースに対する消化コースの表示値を変更
                            for (int i = 0; i < products.getRowCount(); i++) {
                                if (products.getValueAt(i, 2) instanceof ConsumptionCourse) {
                                    ConsumptionCourse c = (ConsumptionCourse) products.getValueAt(i, 2);
                                    if (dsd.getTmpContractNo().equals(c.getTmpContractNo())) {
										//IVS_LVTU start add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                                        //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                        long price = 0l;
                                        if(!"".equals(products.getValueAt(row, 4).toString()) && Double.valueOf(products.getValueAt(row, 4).toString()) != 0) {
                                            price = getConsumptionDisplayValue(index, row);
                                        }
                                        //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
										//IVS_LVTU end add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                                        c.setPrice(price);
                                        products.setValueAt(c, i, 2);
                                        products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getProductValue(), ia.getSales().get(index).getTaxRate()), i, 3);
                                        products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getProductValue(), ia.getSales().get(index).getTaxRate()), i, 5);
                                        products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getValueForConsumption(), target.getDiscountValue(), ia.getSales().get(index).getTaxRate()), i, 7);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
				//IVS_LVTU start add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                changeDataConsumptionCourse(row, col);
				//IVS_LVTU end add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                SystemInfo.getLogger().log(Level.INFO, "金額変更(税込)：" + " row=" + row + ", col=" + col + " ：" + ia.getSales().get(index).getProductValue());
            }
        } else if (col == 6) {
            //割引
            if (getProductsIndex(1) == null) {
				//IVS_LVTU start add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                //コース契約の場合
                if (ia.getSales().get(index).getProductDivision() == 5) {
                    if (!ia.getSales().get(index).isEditable()) {
                        products.setValueAt(ia.getSales().get(index).getDiscount(), row, col);
                        editDataContract = true;
                        return;
                    }
                }
				//IVS_LVTU end add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                Double discount = (Double) products.getValueAt(row, col);
                //nhanvt start add 20150326 Bug #35729
                ia.getSales().get(index).setAccountSetting(ms);
                //nhanvt end add 20150326 Bug #35729
                ia.getSales().get(index).setDiscount(discount);

                if (ia.getAccountSetting().getDiscountType() == 1
                        && 0 < Math.abs(discount) && Math.abs(discount) < 1) {
                    Long temp = ia.getSales().get(index).getValue();

                    ia.getSales().get(index).setDiscountValue(
                            ia.getAccountSetting().getDiscountValue(temp, discount, ia.getSales().get(index).getTaxRate()));
                }
				//IVS_LVTU start add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
                if (ia.getSales().get(index).getProductDivision() == 5
                        && ia.getSales().get(index).getTmpContractNo() != null) {
                    //コース契約前のデータの場合
                    DataSalesDetail dsd = ia.getSales().get(index);

                    if (consumptionCourseMap.containsKey(dsd.getCourse().getCourseClass().getCourseClassId())) {
                        Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(dsd.getCourse().getCourseClass().getCourseClassId());
                        if (courseMap.containsKey(dsd.getTmpContractNo())) {
                            //契約前コースに対する消化コース明細がある場合
                            DataSalesDetail target = null;

                            //契約前コースに対する消化コースの元データの値を変更
                            for (DataSalesDetail d : ia.getSales()) {
                                if (d.getConsumptionCourse() != null
                                        && dsd.getTmpContractNo().equals(d.getTmpContractNo())) {
//                                    double courseValue = ia.getAccountSetting().getDisplayValue(
//                                        ia.getSales().get(index).getValue(),ia.getSales().get(index).getProductNum().longValue(),
//                                        ia.getSales().get(index).getDiscountValue(), ia.getTaxRate());
                                    //コース消化のデータで仮契約番号が同じデータ
                                    //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                    long price = 0l;
                                    if(dsd.getCourse().getNum() != 0) {
                                        price = getConsumptionDisplayValue(index, row);
                                    }
                                    //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                    d.setProductValue(price);
                                    target = d;
                                    break;
                                }
                            }

                            //契約前コースに対する消化コースの表示値を変更
                            for (int i = 0; i < products.getRowCount(); i++) {
                                if (products.getValueAt(i, 2) instanceof ConsumptionCourse) {
                                    ConsumptionCourse c = (ConsumptionCourse) products.getValueAt(i, 2);
                                    if (dsd.getTmpContractNo().equals(c.getTmpContractNo())) {
                                        //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                        long price = 0l;
                                        if(!"".equals(products.getValueAt(row, 4).toString()) && Double.valueOf(products.getValueAt(row, 4).toString()) != 0) {
                                            price = getConsumptionDisplayValue(index, row);
                                        }
                                        //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
                                        c.setPrice(price);
                                        products.setValueAt(c, i, 2);
                                        products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getProductValue(), ia.getSales().get(index).getTaxRate()), i, 3);
                                        products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getProductValue(), ia.getSales().get(index).getTaxRate()), i, 5);
                                        products.setValueAt(ia.getAccountSetting().getDisplayValue(target.getValueForConsumption(), target.getDiscountValue(), ia.getSales().get(index).getTaxRate()), i, 7);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                changeDataConsumptionCourse(row, col);
                //IVS_LVTU end add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
            }
        } else if (col == taxCol) {
            // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
            if (!(products.getValueAt(row, taxCol) instanceof JComboBox)) {
                return;
            }
            //税率
            if (getProductsIndex(1) == null) {
                
                if (ia.getSales().get(index).getProductDivision() == 5) {
                    if (!ia.getSales().get(index).isEditable()) {
                        JComboBox taxCombobox = (JComboBox) products.getValueAt(row, taxCol);
                        taxCombobox.setSelectedIndex(ia.getIndexByTaxRate(ia.getSales().get(index).getTaxRate()));
                        editDataContract = true;
                        return;
                    }
                }

                JComboBox taxCombobox = (JComboBox) products.getValueAt(row, taxCol);
                if (taxCombobox == null || taxCombobox.getSelectedItem() == null) {
                    return;
                }
                Double taxeRate = ((MstTax) taxCombobox.getSelectedItem()).getTaxRate();
                ia.getSales().get(index).setTaxRate(taxeRate);
                
                Long price = Double.valueOf(products.getValueAt(row, 5).toString()).longValue();
                if (ia.getAccountSetting().getDisplayPriceType() == 1) {
                    long signum = (long) Math.signum(price);
                    if (signum == 0) {
                        signum = 1;
                    }
                    Double a = signum * (new Double(Math.floor(Math.abs(price) + (Math.abs(price) * ia.getSales().get(index).getTaxRate()))));
                    ia.getSales().get(index).setProductValue(a.longValue());
                } else {
                    ia.getSales().get(index).setProductValue(price);
                }
            }
            changeDataConsumptionCourse(row, taxCol);
        } else if (col == pointCol) {
            //ポイント
            if (getProductsIndex(1) != null && 0 <= getProductsIndex(1)) {
                Integer point = (Integer) products.getValueAt(row, col);
                ia.getSales().get(index).get(getProductsIndex(1)).setPoint(point);
            }
        } else if (col == rateCol) {
            //割合
            if (getProductsIndex(1) != null && 0 <= getProductsIndex(1)) {
                Integer rate = (Integer) products.getValueAt(row, col);
                ia.getSales().get(index).get(getProductsIndex(1)).setRate(rate);
            }
        }else if(col == staffNoCol) {
            if(products.getValueAt(row, staffNoCol)!=null) {
           String StaffNo =  products.getValueAt(row, staffNoCol).toString();
           JComboBox staffCombobox = (JComboBox) products.getValueAt(row, staffCol);
           for(int i= 0;i<staffCombobox.getItemCount();i++) {
               MstStaff staff = (MstStaff) (staffCombobox.getItemAt(i));
               if(StaffNo.trim().equals(staff.getStaffNo().trim())) {
                   staffCombobox.setSelectedItem(staff);
                        //IVS_LVTu start add 2015/09/18 Bug #42735
                        //担当者
                        DataSalesDetail dsd = (DataSalesDetail) ia.getSales().get(index);
                        if (0 < dsd.size()) {
                            if (getProductsIndex(1) == null) {
                   ia.getSales().get(index).setStaff(staff);
                            } else if (0 <= getProductsIndex(1)) {
                                ia.getSales().get(index).get(getProductsIndex(1)).setStaff(staff);
                            }
                        } else {
                            ia.getSales().get(index).setStaff(staff);
                        }
                        //IVS_LVTu end add 2015/09/18 Bug #42735
                   return;
               }
           }
            }
        }else if (col == staffCol) {
            //担当者
            DataSalesDetail dsd = (DataSalesDetail) ia.getSales().get(index);
            if (0 < dsd.size()) {
                if (getProductsIndex(1) == null) {
                    ia.getSales().get(index).setStaff((MstStaff) ((JComboBox) products.getValueAt(row, col)).getSelectedItem());
                } else if (0 <= getProductsIndex(1)) {
                    ia.getSales().get(index).get(getProductsIndex(1)).setStaff((MstStaff) ((JComboBox) products.getValueAt(row, col)).getSelectedItem());
                }
            } else {
                ia.getSales().get(index).setStaff((MstStaff) ((JComboBox) products.getValueAt(row, col)).getSelectedItem());
            }
            //Luc start add 20150907 #42432
            MstStaff selectStaff = (MstStaff) ((JComboBox) products.getValueAt(row, col)).getSelectedItem();
            products.setValueAt(selectStaff.getStaffNo(), row, staffCol-1);
            //Luc end add 20150907 #42432
        } else if (col == bedCol) {
            //施術台
            DataSalesDetail dsd = (DataSalesDetail) ia.getSales().get(index);
            if (getProductsIndex(1) == null) {
                if (dsd.getProductDivision().intValue() == 1
                        || dsd.getProductDivision().intValue() == 5
                        || dsd.getProductDivision().intValue() == 6) {
                    ia.getSales().get(index).setBed((MstBed) ((JComboBox) products.getValueAt(row, col)).getSelectedItem());
                }
            }
        }

        if (getProductsIndex(1) == null) {
            if (ia.getSales().get(index).getProductDivision() == 6) {
                //コース消化の場合
                products.setValueAt(ia.getAccountSetting().getDisplayValue(
                        ia.getSales().get(index).getValueForConsumption(),
                        ia.getSales().get(index).getDiscountValue(), ia.getSales().get(index).getTaxRate()), row, 7);
            } else {
                //コース消化以外の場合
                //Luc start edit 20150527 ticket 37040
                //products.setValueAt(ia.getAccountSetting().getDisplayValue(
                //        ia.getSales().get(index).getValue(),
                //        ia.getSales().get(index).getDiscountValue(), ia.getTaxRate()), row, 7);

                 products.setValueAt(ia.getAccountSetting().getDisplayValue(
                        ia.getSales().get(index).getValue(),ia.getSales().get(index).getProductNum().longValue(),
                        ia.getSales().get(index).getDiscountValue(), ia.getSales().get(index).getTaxRate()), row, 7);
                 //Luc end edit 20150527 ticket 37040
            }

        }

    }

    //IVS_LVTU start add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
    /**
     * change data consumption course.
     * @param row selected row product menu.
     * @param col selected column product menu.
     */
     private void changeDataConsumptionCourse(int row, int col) {
        int index = getProductsIndex(0, row);
        DataSalesDetail dsd = ia.getSales().get(index);
        if (dsd.getTmpContractNo() != null && dsd.getProductDivision() == 5) {
            Course course = (Course)products.getValueAt(row, 2);
            Map<String, ConsumptionCourse> ccMap = consumptionCourseMap.get(course.getCourseClass().getCourseClassId());
            ConsumptionCourse cc = ccMap.get(dsd.getTmpContractNo());
            
            Object o = products.getValueAt(row, col);
            double courseValue = 0l;
            long price  = 0l;
            if(col == 4) {
                if (o instanceof Integer) {
                    cc.setNum(((Integer) o));
                } else if (o instanceof Double) {
                    cc.setNum(((Double) o).intValue());
                }
            }
            courseValue = ia.getAccountSetting().getDisplayValue(
                    ia.getSales().get(index).getProductValue(),
                    ia.getSales().get(index).getDiscountValue(), ia.getTaxRate());
            //IVS_LVTU start edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
            if(!"".equals(products.getValueAt(row, 4).toString()) && Double.valueOf(products.getValueAt(row, 4).toString()) != 0) {
                price = getConsumptionDisplayValue(index, row);
            }
            //IVS_LVTU end edit 2017/09/25 #26790 [gb]お会計画面：数量が０回の契約の数量等が変更できない
            cc.setPrice(price);
            cc.setTaxRate(ia.getSales().get(index).getTaxRate());
            
            if (productDivision.getSelectedIndex() == 5) {
                //消化コースリストの残回数も変更する
                for (int i = 0; i < consumptionCourse.getRowCount(); i++) {
                    ConsumptionCourse c = (ConsumptionCourse) consumptionCourse.getValueAt(i, 0);
                    if (dsd.getTmpContractNo().equals(c.getTmpContractNo())) {
                        //明細行の未契約コースに紐付く消化コースのレコード
                        if(col == 4) {
                            c.setNum(cc.getNum());
                        }
                        c.setPrice(price);
                        break;
                    }
                }
            }
        }
    }
    //IVS_LVTU end add 2017/08/29 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される

    /**
     * 指定行の技術インデックスをデクリメントする
     */
    private void decTableTechIndex(int row) {
        Integer setInteger = null;
        JComboBox combo;
        if (row < 0) {
            return;
        }
        combo = (JComboBox) products.getValueAt(row, 0);
        if (combo == null) {
            return;
        }
        setInteger = (Integer) combo.getItemAt(1) - 1;
        combo.removeItemAt(1);
        combo.insertItemAt(setInteger, 1);
    }

    /**
     * 売上明細を１行削除する。
     *
     * @param index 削除する明細のインデックス
     * @param evt
     */
    private void deleteProduct() {
        int row = products.getSelectedRow();	// 選択行
        int index = this.getProductsIndex(0);    // 選択技術
        //add by ltthuc 2014/06/26
        if (getEditFlag() == 0) {
            setEditFlag(1);
        }
        //end add by ltthuc
        DataSalesDetail dsd = ia.getSales().get(index);
        int size = dsd.size();

        if (products.getCellEditor() != null) {
            products.getCellEditor().stopCellEditing();
        }
        //add by ltthuc 2014/06/11
//        int tabId = productDivision.getSelectedIndex();
//        if (tabId == 1 || tabId == 6 || tabId == 3) {
//            arrPaymentMethodId_1.remove(row);
//        }
        if(dsd.getProduct().getPrepa_id()!=null){
             arrPaymentMethodId_1.remove(dsd.getProduct());
        }
        ia.getSales().remove(index);

        int removeConsumptionCourse = 0;
        //コース契約前のコースを明細から削除する場合はコース消化リストからもコース情報を削除する
        if (dsd.getProductDivision() == 5 && dsd.getTmpContractNo() != null && dsd.getTmpContractNo().trim().length() > 0) {
            String tmpContractNo = dsd.getTmpContractNo();
            Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(dsd.getCourse().getCourseClass().getCourseClassId());
            //消化コースリストから削除
            try {
                courseMap.remove(tmpContractNo);
            }catch (Exception e) {}

            if ((courseMap == null || courseMap.isEmpty() )) {
                //消化コースリストが空の場合はコース分類リストからコース分類を削除する
                Integer courseClassId = dsd.getCourse().getCourseClass().getCourseClassId();
                try {
                    consumptionCouserClassMap.remove(courseClassId);
                }catch (Exception e) {}

                //このコースのコース分類に契約済みコースが紐付いているかどうかチェック
                boolean hasConsumptionCourse = false;
                for (ConsumptionCourseClass consumptionCourseClass : consumptionCourseClasses) {
                    if (consumptionCourseClass.getSlipNo() != null
                            && courseClassId.equals(consumptionCourseClass.getCourseClassId())) {
                        //契約済コースが紐付いている場合
                        hasConsumptionCourse = true;
                        break;
                    }
                }

                if (!hasConsumptionCourse) {
                    //未契約コース、契約済みコースのどちらも紐付いていないコース分類がある
                    for (ConsumptionCourseClass consumptionCourseClass : consumptionCourseClasses) {
                        if (courseClassId.equals(consumptionCourseClass.getCourseClassId())) {
                            try {
                                consumptionCourseClasses.remove(consumptionCourseClass);
                            }catch (Exception e){}
                            break;
                        }
                    }
                }
            }

            //明細行に削除したコースに対する消化コースの明細があれば削除する
            for (int i = ia.getSales().size(); i > 0; i--) {
                DataSalesDetail d = ia.getSales().get(i - 1);
                if (d.getProductDivision() == 6
                        && tmpContractNo.equals(d.getTmpContractNo())) {
                    ia.getSales().remove(i - 1);
                    break;
                }
            }

            this.setConsumptionDataSales(consumptionCourseClasses, consumptionCourseClass);
            this.showConsumptionCourse(consumptionCourseClass, consumptionCourse);

            DefaultTableModel model = (DefaultTableModel) products.getModel();
            for (int i = model.getRowCount(); i > 0; i--) {
                Object o = model.getValueAt(i - 1, 2);
                if (o instanceof ConsumptionCourse) {
                    if (tmpContractNo.equals(((ConsumptionCourse) o).getTmpContractNo())) {
                        //仮契約番号が等しい場合はこの明細行を削除
                        model.removeRow(i - 1);
                        this.detailCount--;
                        removeConsumptionCourse++;
                    }
                }
            }
        //IVS_LVTU start add 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        } else if (dsd.getProductDivision() == 6) {
            ConsumptionCourse cc = null;
            for (int i = 0;i < this.consumptionCourse.getRowCount();i ++) {
                if (this.consumptionCourse.getValueAt(i, 0) instanceof ConsumptionCourse) {
                    cc = (ConsumptionCourse)this.consumptionCourse.getValueAt(i, 0);
                }
                if (cc.getTmpContractNo() != null
                        && dsd.getTmpContractNo() != null
                        && cc.getTmpContractNo().equals(dsd.getTmpContractNo())) {
                    cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
                    break;
                } else if(cc != null
                        && cc.getContractNo() != null
                        && cc.getContractDetailNo() != null
                        && cc.getCourseId().equals(dsd.getConsumptionCourse().getCourseId())
                        && cc.getContractShopId().equals(dsd.getConsumptionCourse().getContractShopId())
                        && cc.getContractNo().equals(dsd.getConsumptionCourse().getContractNo())
                        && cc.getContractDetailNo().equals(dsd.getConsumptionCourse().getContractDetailNo())
                        ) {
                    cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
                    break;
                }
            }
        }
        //IVS_LVTU end add 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備

        DefaultTableModel model = (DefaultTableModel) products.getModel();

        if (this.getSelectedShop().isDisplayProportionally()) {
            // 按分メニュー表示あり
            for (int i = 0; i < size + 1; i++) {
                model.removeRow(row);
            }
        } else {
            // 按分メニュー表示なし
            model.removeRow(row);
        }

        for (; row < products.getRowCount(); row++) {
            for (int i = 0; i < removeConsumptionCourse + 1; i++) {
                this.decTableTechIndex(row);
            }
        }

        this.detailCount--;

        // ポイント割引設定に反映
        if (SystemInfo.isUsePointcard()) {
            showDiscountPoint();
        }

        this.setTotal();
        this.showCustomerDisplayToSalesValue();

    }

    /**
     * 割引部の初期化処理を行う。
     */
    private void initDiscount() {
        //全行削除
        SwingUtil.clearTable(discounts);

        JComboBox dCombo = new JComboBox();
        dCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        dCombo.addItem(new MstDiscount());
        for (MstDiscount temp : ia.getDiscounts()) {
            dCombo.addItem(temp);
        }

        //割引が変更されたときの処理を追加
        dCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                changeDiscountValue();
            }
        });

        DefaultTableModel model = (DefaultTableModel) discounts.getModel();

        model.addRow(new Object[]{dCombo, 0d});
    }

    /**
     * 割引が変更されたときの処理を行う。
     */
    private void changeDiscountValue() {
        int row = discounts.getSelectedRow();
        int col = discounts.getSelectedColumn();

        if (row < 0 || col < 0) {
            return;
        }

        DataSalesDetail dsd = ia.getSales().getDiscount();

        //割引種別
        JComboBox combo = (JComboBox) discounts.getValueAt(row, 0);
        MstDiscount md = (MstDiscount) combo.getSelectedItem();

        //割引種別が変更された場合
        if (col == 0) {
            if (dsd != null && md.getDiscountID() != null) {
                dsd.getProduct().setProductID((md == null ? null : md.getDiscountID()));
            }
            if (md.getDiscountMethod() != null) {
                //割引金額、割引額の設定
                if (md.getDiscountMethod() == DISCOUNT_METHOD_VALUE) {
                    discounts.setValueAt(md.getDiscountValue() != null ? md.getDiscountValue().doubleValue() : new Double(0), row, 1);
                } else {
                    discounts.setValueAt(md.getDiscountRate() != null ? md.getDiscountRate() : new Double(0), row, 1);
                }
            } else {
                discounts.setValueAt(new Double(0), row, 1);
            }
        }

        //割引金額（割引率の場合は金額を求める）
        Double value = (Double) discounts.getValueAt(row, 1);

        if (dsd != null) {
            dsd.setProductValue(ia.getSales().getSalesTotal());
            dsd.setDiscountDivision(md.getDiscountDivision());
            dsd.setAllDiscount(value, md.getDiscountMethod());
            //IVS_LVTu edit 2019/09/06  SPOS増税対応
            dsd.setTaxRate(ia.getTaxRate());
        }

        this.setTotal();
        this.showCustomerDisplayToSalesValue();
    }

    /**
     * 支払方法を初期化する。
     *
     */
    private void initPayments() {
        this.initPaymentCells();
        this.addNewPaymentRow(true);
    }

    /**
     * 支払方法のセルを初期化する。
     *
     */
    private void initPaymentCells() {
        DefaultTableModel model = (DefaultTableModel) payments.getModel();

        //全行削除
        model.setRowCount(0);
        payments.removeAll();
        //add by ltthuc 2014/06/25


        //end add by ltthuc;
        for (MstPaymentClass mpc : ia.getPaymentClasses()) {
            JComboBox classes = new JComboBox(new Object[]{mpc});
            classes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            Object methods = this.getPaymentMethodObject(mpc);
            arrPayMentMethodId.add(mpc.getPaymentClassID());
            Object[] rowData = {new JLabel(" " + mpc.toString()), methods, 0l, getCashInsertButton()};
            model.addRow(rowData);

            ia.getSales().getPayment(0).addPaymentDetail(mpc, (mpc.size() == 1 ? mpc.get(0) : null), 0l);
            //add by ltthuc 2014/06/25
        }
        //add by ltthuc 2014/06/25
        //end add by ltthuc
    }

    /**
     * 支払方法に新しい行を追加する。
     *
     */
    private void addNewPaymentRow(boolean isAddData) {
        JComboBox classes = new JComboBox();
        classes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        Object methods = null;
        MstPaymentClass mpc = null;

        for (MstPaymentClass temp : ia.getPaymentClasses()) {
            if (temp.getPaymentClassID() != 1) {
                classes.addItem(temp.getPaymentClassName());

            }

            if (classes.getItemCount() == 1) {
                mpc = temp;
                methods = this.getPaymentMethodObject(temp);

            }
        }
        //支払区分が変更されたときの処理を追加
        classes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                paymentActionPerformed(evt);

            }
        });

        DefaultTableModel model = (DefaultTableModel) payments.getModel();

        model.addRow(new Object[]{classes, methods, 0l, getCashInsertButton()});

        if (isAddData) {
            ia.getSales().getPayment(0).addPaymentDetail(
                    mpc,
                    (mpc == null || 0 == mpc.size() ? null : mpc.get(0)),
                    0l);






        }

    }

    /**
     * 残額ボタンを取得する
     */
    private JButton getCashInsertButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/account/cash_insert_off.jpg")));
        button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/account/cash_insert_on.jpg")));

        button.setMargin(new Insets(0, 0, 0, 0));

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setBalance();
                //add by ltthuc 2014/06/26
                isMousePayMentClick = false;
                //end add by ltthuc
                String paymentValue;
                //add by ltthuc 2014/07/18
                if (getEditFlag() == 0) {
                    int row = payments.getSelectedRow();
                    int col = payments.getSelectedColumn();
                    if (payments.getCellEditor() != null) {
                        payments.getCellEditor().stopCellEditing();
                    }
                    paymentValue = payments.getValueAt(row, col - 1).toString();
                    if (!paymentValue.equals(arrPaymentValue.get(row))) {
                        setEditFlag(1);

                        // setEditFlagIs1(tempPayment, payments, tempPaymentRow, tempPaymentCol);
                    }
                }
                //end add by ltthuc
            }
        });
        return button;
    }

    private void setBalance() {
        long value = ((NameValue) ia.getTotal(5)).getValue().longValue();
        if (value < 0) {
            int row = payments.getSelectedRow();
            long currentValue = Long.parseLong(payments.getValueAt(row, 2).toString());
            payments.setValueAt(currentValue + Math.abs(value), row, 2);
            payments.changeSelection(row, 2, false, false);
            this.changePaymentValue();
        }
    }

    /**
     * 支払方法のTableModel
     *
     */
    private class PaymentTableModel extends DefaultTableModel {

        /**
         * 支払区分
         */
        MstPaymentClasses mpcs = null;

        /**
         * コンストラクタ
         *
         * @param mpcs 支払区分のリスト
         */
        public PaymentTableModel(MstPaymentClasses mpcs) {
            super(new String[]{"支払区分", "支払方法", "金額"}, 0);
            this.mpcs = mpcs;
            if (this.mpcs == null) {
                this.mpcs = new MstPaymentClasses();
            }
        }

        /**
         * 列のクラスを取得する。
         *
         * @param col 列
         * @return 列のクラス
         */
        public Class getColumnClass(int col) {
            if (col < 0 || this.getValueAt(0, col) == null) {
                return Object.class;
            }
            return this.getValueAt(0, col).getClass();
        }

        /**
         * セルが編集可能かを取得する。
         *
         * @param row 行
         * @param col 列
         * @return true - 編集可能
         */
        public boolean isCellEditable(int row, int col) {
            //支払区分の支払区分数以下の行の場合
            if (col == 0 && row < mpcs.size()) {
                return false;
            }
            //支払方法の支払方法が１件の場合
            if (col == 1 && this.getValueAt(row, col).getClass().getName().equals("java.lang.String")) {
                return false;
            }

            return true;
        }
    }

    /**
     * 支払方法が変更されたときの処理。
     *
     * @param evt
     */
    public void paymentActionPerformed(java.awt.event.ActionEvent evt) {
        int row = payments.getSelectedRow();
        int col = payments.getSelectedColumn();
        String s;
        s = payments.getValueAt(row, col).toString();
        if (row < 0 || col < 0 || col > 1) {
            return;
        }

        MstPaymentClass mpc = null;
        MstPaymentMethod mpm = null;

        //支払区分が固定の行の場合
        if (0 <= row && row < ia.getPaymentClasses().size()) {
            mpc = ia.getPaymentClasses().get(row);
        } else {
            JComboBox mpccb = (JComboBox) payments.getValueAt(row, 0);
            mpccb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            int index = mpccb.getSelectedIndex() + 1;

            if (0 < index && index <= ia.getPaymentClasses().size()) {
                mpc = ia.getPaymentClasses().get(index);
            }
        }

        switch (col) {
            //支払区分
            case 0:
                if (mpc != null) {
                    Object methods = this.getPaymentMethodObject(mpc);
                    payments.setValueAt(methods, row, 1);
                }

                break;
            //支払方法
            case 1:
                if (mpc != null && mpc.size() != 0) {
                    if (mpc.size() == 1) {
                        mpm = mpc.get(0);
                    } else {
                        JComboBox mpccb = (JComboBox) payments.getValueAt(row, 1);
                        mpccb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                        int index = mpccb.getSelectedIndex() - 1;

                        if (0 <= index && index < mpc.size()) {
                            mpm = mpc.get(index);
                        }
                    }
                }

                break;
        }

        Double value = Double.valueOf(payments.getValueAt(row, 2).toString());

        ia.getSales().getPayment(0).setPaymentDetail(row, mpc, mpm, value.longValue());

        this.setTotal();
    }

    /**
     * 支払方法にセットするオブジェクトを取得する。
     *
     * @param mpc 支払区分
     * @return 支払方法にセットするオブジェクト
     */
    private Object getPaymentMethodObject(MstPaymentClass mpc) {
        Object methods = null;

        //支払方法が無い場合
        if (mpc.size() == 0) {
            methods = new JLabel();
        } //支払方法が１件の場合
        else if (mpc.size() == 1) {
            methods = new JLabel(" " + mpc.get(0).getPaymentMethodName());
        } else {
            methods = new JComboBox();
            ((JComboBox) methods).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            ((JComboBox) methods).addItem(new MstPaymentMethod());

            for (MstPaymentMethod mpm : mpc) {
                ((JComboBox) methods).addItem(mpm.getPaymentMethodName());
            }

            //支払方法が変更されたときの処理を追加
            ((JComboBox) methods).addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    paymentActionPerformed(evt);


                }
            });
        }

        return methods;
    }

    /**
     * 合計を初期化する。
     */
    private void initTotal() {
        // 明細合計
        totalPrice.setText("0");
        // 明細割引
        discountValue.setText("0");
        // 明細売上
        totalPrice2.setText("0");
        // 全体割引
        allDiscountValue.setText("0");
        // 消費税
        taxValue.setText("0");
        // 請求金額
        totalValue.setText("0");
        // お預かり金額
        paymentTotal.setText("0");
        // お釣り
        changeValue.setText("0");
    }

    /**
     * 合計をセットする。
     */
    private void setTotal() {
        if (point.getCellEditor() != null) {
            point.getCellEditor().stopCellEditing();
        }

        // ポイント割引設定の値を反映
        if (SystemInfo.isUsePointcard() && point.getRowCount() > 0) {

            for (int i = 0; i < products.getRowCount(); i++) {
                if (products.getValueAt(i, 6) instanceof Double) {
                    ia.getSales().get(getProductsIndex(0, i)).setDiscount((Double) products.getValueAt(i, 6));
                }
            }

            for (int i = 0; i < point.getRowCount(); i++) {
                Long pointDiscount = Long.valueOf(point.getValueAt(i, 2).toString());
                if (pointDiscount != 0) {
                    Long discountValue = ia.getSales().get(pointIndexList.get(i)).getDiscountValue();
                    ia.getSales().get(pointIndexList.get(i)).setDiscount((double) (pointDiscount + discountValue));
                }
            }

        }


        ia.setTotal();

        // お預かり合計
        paymentTotal.setText(FormatUtil.decimalFormat(ia.getPaymentTotal()));

        int row = 0;

        // 明細合計
        totalPrice.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(0)).getValue()));

        // 明細割引
        discountValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(1)).getValue()));

        // 明細売上
        Long tmpPrice = ((NameValue) ia.getTotal(3)).getValue() + ((NameValue) ia.getTotal(2)).getValue() + ia.getCunsumptionTotal();
        totalPrice2.setText(FormatUtil.decimalFormat(tmpPrice));

        // 全体割引
        allDiscountValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(2)).getValue()));

        // 明細割引＋全体割引をdiscountValueに設定する
        //Long tmpDiscount=( (NameValue)ia.getTotal( 1 ) ).getValue()+( (NameValue)ia.getTotal( 2 ) ).getValue();
        //discountValue.setText(FormatUtil.decimalFormat( tmpDiscount) );

        // 消費税
        taxValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(4)).getValue()));

        // 請求金額
        totalValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(3)).getValue()));

        // お釣り
        changeValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(5)).getValue()));
    }

    /**
     * 合計をセットする。 ただし請求金額には反映しない
     */
    private void setTotalWithoutPayment() {
        if (point.getCellEditor() != null) {
            point.getCellEditor().stopCellEditing();
        }

        // ポイント割引設定の値を反映
        if (SystemInfo.isUsePointcard() && point.getRowCount() > 0) {

            for (int i = 0; i < products.getRowCount(); i++) {
                if (products.getValueAt(i, 6) instanceof Double) {
                    ia.getSales().get(getProductsIndex(0, i)).setDiscount((Double) products.getValueAt(i, 6));
                }
            }

            for (int i = 0; i < point.getRowCount(); i++) {
                Long pointDiscount = Long.valueOf(point.getValueAt(i, 2).toString());
                if (pointDiscount != 0) {
                    Long discountValue = ia.getSales().get(pointIndexList.get(i)).getDiscountValue();
                    ia.getSales().get(pointIndexList.get(i)).setDiscount((double) (pointDiscount + discountValue));
                }
            }

        }


        ia.setTotal();

        // お預かり合計
        paymentTotal.setText(FormatUtil.decimalFormat(ia.getPaymentTotal()));

        int row = 0;

        // 明細合計
        totalPrice.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(0)).getValue()));

        // 明細割引
        discountValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(1)).getValue()));

        // 明細売上
        Long tmpPrice = ((NameValue) ia.getTotal(3)).getValue() + ((NameValue) ia.getTotal(2)).getValue();
        totalPrice2.setText(FormatUtil.decimalFormat(tmpPrice));

        // 全体割引
        allDiscountValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(2)).getValue()));

        // 明細割引＋全体割引をdiscountValueに設定する
        //Long tmpDiscount=( (NameValue)ia.getTotal( 1 ) ).getValue()+( (NameValue)ia.getTotal( 2 ) ).getValue();
        //discountValue.setText(FormatUtil.decimalFormat( tmpDiscount) );

        // 消費税
        taxValue.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(4)).getValue()));

        // 請求金額
//		totalValue.setText(FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 3 ) ).getValue()) );

        // お釣り
//		changeValue.setText(FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 5 ) ).getValue()) );
    }

    /**
     * 顧客をセットする。
     */
    private void setCustomer() {
        MstCustomer cus = ia.getSales().getCustomer();
        boolean isChangedCustomerNo = !customerNo.getText().equals(cus.getCustomerNo());
        changeCus = !customerNo.getText().equals(cus.getCustomerNo());
        //IVS NNTUAN START EDIT 20131021
        if (changeCus && !(cus.getCustomerNo() == null || cus.getCustomerNo().equals(""))) {
            // IVS SANG START EDIT  20131114 [gb]予約登録画面、お会計画面の制御調整
            //this.init();
            this.clearProduct();
            // IVS SANG END EDIT  20131114 [gb]予約登録画面、お会計画面の制御調整
        }
        cus.setCustomerNo(customerNo.getText());
        if (cus.getCustomerNo().equals("0") && cus.getCustomerID() == null) {
            cus = new MstCustomer();

            this.changeCustomerNameEditable(true);

            customerName1.requestFocusInWindow();
        } else {
            //顧客Noをセットする。
            cus.setCustomerNo(this.customerNo.getText());

            ConnectionWrapper con = SystemInfo.getConnection();

            try {

                if (isChangedCustomerNo) {
                    cus = SelectSameNoData.getMstCustomerByNo(
                            parentFrame,
                            SystemInfo.getConnection(),
                            this.customerNo.getText(),
                            (SystemInfo.getSetteing().isShareCustomer() ? 0 : this.getSelectedShopID()));

                } else {
                    cus = new MstCustomer(cus.getCustomerID());
                    cus.load(con);
                }

            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            if (cus == null) {
                cus = new MstCustomer();
            }

            this.changeCustomerNameEditable(false);
        }

        customerName1.setText(cus.getCustomerName(0));
        customerName2.setText(cus.getCustomerName(1));

        // 前回指名担当表示
        //this.setLastTimeStaff(cus);

        this.showNowPoint(cus);

        ia.getSales().setCustomer(cus);

        // SOSIA連動にあわせて連動ボタンを変更する
        updateSosiaGearButton();

    }

    private void setStaff() {
        MstStaff ms = new MstStaff();
        //スタッフNoをセットする。
        ms.setStaffNo(this.staffNo.getText());
        try {
            ms = SelectSameNoData.getMstStaffByNo(
                    parentFrame,
                    SystemInfo.getConnection(),
                    this.staffNo.getText());

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        ia.getSales().getPayment(0).setStaff(ms);
        this.requestFocusToPayments();

    }

    private void changeCustomerNameEditable(boolean isEditable) {
        customerName1.setEditable(isEditable);
        customerName2.setEditable(isEditable);
        customerName1.setFocusable(isEditable);
        customerName2.setFocusable(isEditable);
    }

    /**
     * スタッフをセットする。
     */
    private void setStaff(String staffNo) {
        staff.setSelectedIndex(0);

        for (int i = 1; i < staff.getItemCount(); i++) {

            if (((MstStaff) staff.getItemAt(i)).getStaffNo().equals(staffNo)) {
                staff.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * 主担当者をセットする。
     */
    private void setChargeStaff(String staffNo) {
        chargeStaff.setSelectedIndex(0);

        for (int i = 1; i < chargeStaff.getItemCount(); i++) {

            if (((MstStaff) chargeStaff.getItemAt(i)).getStaffNo().equals(staffNo)) {
                chargeStaff.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * スタッフをセットする。
     */
    private void setStaff(Integer staffID) {
        staff.setSelectedIndex(0);

        for (int i = 1; i < staff.getItemCount(); i++) {

            if (((MstStaff) staff.getItemAt(i)).getStaffID().equals(staffID)) {
                staff.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * 主担当者をセットする。
     */
    private void setChargeStaff(Integer staffID) {
        chargeStaff.setSelectedIndex(0);

        for (int i = 1; i < chargeStaff.getItemCount(); i++) {

            if (((MstStaff) chargeStaff.getItemAt(i)).getStaffID().equals(staffID)) {
                chargeStaff.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * 支払金額が変更されたときの処理。
     */
    private void changePaymentValue() {
        int row = payments.getSelectedRow();
        int col = payments.getSelectedColumn();
        //add by ltthuc 2014/06/26


        if (row < 0 || col < 0) {
            return;
        }

        //金額の場合
        if (col == 2) {

            Long value = Double.valueOf(payments.getValueAt(row, col).toString()).longValue();

            // 最終行で金額が入力された場合
            if (value != null && 0 < value && row == payments.getRowCount() - 1) {
                //新規行を追加
                this.addNewPaymentRow(true);
            }

            DataPaymentDetail dpd = ia.getSales().getPayment(0).get(row);

            //IVS_LVTu start edit 2016/06/21 Bug #51065
            if(row > 0 && payments.getValueAt(row, 1) instanceof JComboBox) {
                JComboBox mpccb = (JComboBox) payments.getValueAt(row, 1);
                if(mpccb.getSelectedItem() instanceof MstPaymentMethod && ((MstPaymentMethod)mpccb.getSelectedItem()).getPaymentMethodID() == null) {
                    dpd.setPaymentMethod(null);
                }
            }
            //IVS_LVTu end edit 2016/06/21 Bug #51065

            if (dpd != null) {
                if (dpd.getPaymentMethod() == null) {
                    if (row < ia.getPaymentClasses().size()
                            && ia.getPaymentClasses().get(row).size() > 0) {
                        //IVS_Ptthu start edit Bug #51065
                        if (row == 0 || ( payments.getValueAt(row, 1) instanceof JLabel && !((JLabel) (payments.getValueAt(row , 1))).getText().equals(""))) {
                            dpd.setPaymentMethod(ia.getPaymentClasses().get(row).get(0));
                        } else {
                            dpd.setPaymentMethod(null);
                        }
                        //IVS_Ptthu end edit Bug #51065
                    }
                }
                dpd.setPaymentValue(value);
            }
        }

        this.setTotal();
    }

    /**
     * 入力チェックを行う。
     *
     * @return true - OK
     */
    private boolean checkInput() {
        boolean isTempFlag = ia.getSales().getTempFlag();

        try {
            //売上日
            if (salesDate.getDate() == null) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                        "売上日"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                salesDate.requestFocusInWindow();
                return false;
            }

            //顧客
            if (customerNo.getText().equals("")) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "顧客"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                customerNo.requestFocusInWindow();
                return false;
            }

            ConnectionWrapper con = SystemInfo.getConnection();

            //非会員
            if (customerNo.getText().equals("0")) {
                ia.getSales().getCustomer().setCustomerNo("0");
                ia.getSales().getCustomer().setCustomerName(0, customerName1.getText());
                ia.getSales().getCustomer().setCustomerName(1, customerName2.getText());
            } else {
                MstCustomer mc = new MstCustomer();
                if (ia.getSales().getCustomer().getCustomerID() != null) {
                    mc.setCustomerID(ia.getSales().getCustomer().getCustomerID());

                    if (!mc.isExists(con)) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_INPUT_NOT_EXIST,
                                "顧客コード" + customerNo.getText()),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        customerNo.requestFocusInWindow();
                        return false;

                    }
                }
            }

            /*MstCustomer	mc	=	new MstCustomer(ria.getSales().getCustomer().getCustomerID());

             if(!mc.isExists(con))
             {
             MessageDialog.showMessageDialog(this,
             MessageUtil.getMessage(MessageUtil.ERROR_INPUT_NOT_EXIST, "顧客"),
             this.iaME_NAME,
             JOptionPane.ERROR_MESSAGE);
             customerNo.requestFocusInWindow();
             return	false;
             }*/

            //レジ担当者
            if (staff.getSelectedIndex() == 0) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "レジ担当者"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                staff.requestFocusInWindow();
                return false;
            }
            //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
            //担当者 - 消化の数量
            DataSalesDetail dsdTemp;
            for (int i = 0; i < ia.getSales().size(); i++) {
                dsdTemp = ia.getSales().get(i);
                if (ia.getSales().get(i).getProductDivision() == 6) {
                    if (ia.getSales().get(i).getStaff().getStaffID() == null) {
                        MessageDialog.showMessageDialog(this,
                                "消化メニューの担当者が選択されていません。",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        staff.requestFocusInWindow();
                        return false;

                    }
                    //消化の数量は１以上で入力して下さい。
                    if (dsdTemp.getConsumptionNum() <= 0) {
                        MessageDialog.showMessageDialog(this,
                            String.valueOf(MessageUtil.getMessage(20004)),
                            this.getTitle(), 
                            JOptionPane.ERROR_MESSAGE);
                        products.requestFocusInWindow();
                        return false;
                    }
                }
            }
            //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
            //伝票明細
            if (products.getRowCount() == 0) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "商品"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            //伝票明細内容
            for (DataSalesDetail dsd : ia.getSales()) {
                // 指名
                if ((dsd.getDesignated()) && ((dsd.getStaff().getStaffID() == null) || (dsd.getStaff().getStaffID() == 0))) {
                    String msg = MessageUtil.getMessage(5123);

                    if (dsd.getProductDivision() == 2) {
                        msg = msg.replace("施術担当指名", "商品担当アプローチ");
                    }

                    MessageDialog.showMessageDialog(this,
                            msg,
                            this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    products.requestFocusInWindow();
                    return false;
                }

                // アプローチ
                if ((dsd.getApproached()) && ((dsd.getStaff().getStaffID() == null) || (dsd.getStaff().getStaffID() == 0))) {
                    MessageDialog.showMessageDialog(this,
                            String.valueOf(MessageUtil.getMessage(5123)).replace("担当指名", "担当アプローチ"),
                            this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    products.requestFocusInWindow();
                    return false;
                }

                for (DataSalesProportionally dsp : dsd) {
                    // 按分指名
                    if ((dsp.getDesignated()) && ((dsp.getStaff().getStaffID() == null) || (dsp.getStaff().getStaffID() == 0))) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(5124),
                                this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        products.requestFocusInWindow();
                        return false;
                    }
                }
            }

            //全体割引時は割引名の選択が必要
            Double value = (Double) discounts.getValueAt(0, 1);
            if (value != 0d) {
                JComboBox combo = (JComboBox) discounts.getValueAt(0, 0);
                if (combo.getSelectedIndex() <= 0) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(3005),
                            this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    discounts.requestFocusInWindow();
                    return false;
                }

            }
            //IVS_PTThu start edit 20160719 New request #52737
            // GB Start edit 20161108 Bug #58499
//            long paymentValue   = 0l;
//            if (checkMethod() == 1) {
//                boolean flag = true;
//                for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
//                    if (dpd.getPaymentValue() != null && 0 < dpd.getPaymentValue()) {
//                        if (dpd.getPaymentMethod() == null
//                                || dpd.getPaymentMethod().getPaymentMethodID() == null) {
//                            paymentValue += dpd.getPaymentValue();
//                            dpd.setPaymentValue(null);
//                        }
//                    }
//                }
//                for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
//                    if (flag && dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPaymentMethodID() != null) {
//                        dpd.setPaymentValue(dpd.getPaymentValue() + paymentValue);
//                        flag = false;
//                    }
//                }
//            }

            //IVS_LVTu start edit 2016/11/03 Bug #58397
            //if (!isTempFlag) {
                //支払
//                for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
//                    if (dpd.getPaymentValue() != null && 0 < dpd.getPaymentValue()) {
//                        int count = 0;
//                        if (dpd.getPaymentMethod() == null
//                                || dpd.getPaymentMethod().getPaymentMethodID() == null) {
//                            if (checkMethod() == 1) {
//                                if (count == 0) {
//                                    for (int i = 0; i < paymentclass(); i++) {
//                                        if (payments.getValueAt(i, 1) instanceof JLabel
//                                                && !((JLabel) (payments.getValueAt(i, 1))).getText().equals("")) {
//                                            dpd.setPaymentMethod(ia.getPaymentClasses().get(i).get(0));
//                                        }
//                                    }
//                                }
//                            }
//                            else
//                            {
//                                MessageDialog.showMessageDialog(this,
//                                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支払方法"),
//                                        this.getTitle(),
//                                        JOptionPane.ERROR_MESSAGE);
//                                return false;
//                            }
//                        }
//                        if (dpd.getPaymentMethod().getPaymentClassID() == null) {
//                            MessageDialog.showMessageDialog(this,
//                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支払区分"),
//                                    this.getTitle(),
//                                    JOptionPane.ERROR_MESSAGE);
//                            return false;
//                        }
//                        count++;
//                        //IVS_PTThu end edit 20160719 New request #52737
//                    }
//                }


            for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
                    if (dpd.getPaymentValue() != null && 0 < dpd.getPaymentValue()) {
                        if (dpd.getPaymentMethod() == null
                                || dpd.getPaymentMethod().getPaymentMethodID() == null) {

                                MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支払方法"),
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE);
                                return false;
                        }
                        if (dpd.getPaymentMethod().getPaymentClassID() == null) {
                            MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支払区分"),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }

            //}
            // GB End edit 20161108 Bug #58499
            //IVS_LVTu end edit 2016/11/03 Bug #58397

            //一括割引
            Integer discountDivision = ia.getSales().getDiscount().getDiscountDivision();
            Long salesTotal = null;

            if (discountDivision != null) {
                if (discountDivision == DISCOUNT_DIVISION_ALL) {
                    //全体割引の場合、技術 + 商品の合計を取得
                    salesTotal = ia.getSales().getTechItemSalesTotal();
                    //nhanvt start add 20150206 Bug #35176
                    for (DataSalesDetail dsd : ia.getSales()) {
                        if (dsd.getProductDivision() == 1 || dsd.getProductDivision() == 2 || dsd.getProductDivision() == 5) {
                          salesTotal += ia.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate());
                        }

                    }


                } else {
                    //技術(商品)割引の場合、技術(商品)のみの合計を取得
                    salesTotal = ia.getSales().getSalesTotal(discountDivision);
                    //nhanvt start add 20150206 Bug #35176
                    if(discountDivision == 1){
                        for (DataSalesDetail dsd : ia.getSales()) {
                            if (dsd.getProductDivision() == 1) {
                              salesTotal += ia.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate());
                            }

                        }
                    }
                    if(discountDivision == 2){
                        for (DataSalesDetail dsd : ia.getSales()) {
                            if (dsd.getProductDivision() == 2) {
                              salesTotal += ia.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate());
                            }

                        }
                    }
                    if(discountDivision == 5){
                        for (DataSalesDetail dsd : ia.getSales()) {
                            if (dsd.getProductDivision() == 5) {
                              salesTotal += ia.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), ia.getTaxRate());
                            }

                        }
                    }

                    //nhanvt end add 20150206 Bug #35176
                }
                if (salesTotal - ia.getTotal(2).getValue() < 0) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(3101, getDiscountDivisionName(discountDivision)),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    discounts.requestFocusInWindow();
                    return false;
                }
            }
            //支払金額
            Long temp = 0l;
            for (int i = 5; i < ia.getTotal().size() - 2; i++) {
                temp += ia.getTotal(i).getValue();
            }
            if (ia.getTotal(2).getValue() < temp && !isTempFlag) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(3000),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                payments.requestFocusInWindow();
                return false;
            }


            //売掛金回収データの削除確認
            if (ia.getSales().isExistCollectedBill(con)) {
                if (MessageDialog.showYesNoDialog(this,
                        MessageUtil.getMessage(3001),
                        this.getTitle(),
                        JOptionPane.QUESTION_MESSAGE) != 0) {
                    return false;
                }
            }

            //Luc start edit 20150817
            //if(SystemInfo.getDatabase().contains("pos_hair_mashu")){
            if (ia.getSales().getShop() != null) {
                if(ia.getSales().getShop().getUseShopCategory()!=null) {
                    if (ia.getSales().getShop().getUseShopCategory() == 1) {
                        boolean flag = false;
                        for (int i = 0; i < tblReservationMainStaff.getRowCount(); i++) {
                            flag = false;
                            JComboBox staffCombo = (JComboBox)tblReservationMainStaff.getValueAt(i, 2);
                            flag = (Boolean) tblReservationMainStaff.getValueAt(i, 3);
                            if ((flag == true)&&(((MstStaff)staffCombo.getSelectedItem()).getStaffID() == null)){
                                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.REGIST_WARNING_MESSAGE, "業態主担当設定"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                                return false;
                           }
                        }
                    }
                }
            }
            // レスポンス選択
            DataResponseEffect[] dre = new DataResponseEffect[2];
            dre[0] = new DataResponseEffect();
            dre[0].setResponse((MstResponse) responseItemComboBox1.getSelectedItem());
            dre[0].setDataResponseDate(new GregorianCalendar());
            dre[0].setResponseIssue(null);
            dre[1] = new DataResponseEffect();
            dre[1].setResponse((MstResponse) responseItemComboBox2.getSelectedItem());
            dre[1].setDataResponseDate(new GregorianCalendar());
            dre[1].setResponseIssue(null);
            ia.setResponseEffect(dre);

            //請求金額がマイナスのとき
            if ((ia.getTotal(3).getValue() < 0) && ia.getTotal(5).getValue() != 0) {
                if (MessageDialog.showYesNoDialog(this,
                        "お預り金額が入力されていません。\n返金処理を行ってよろしいですか？",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE) == 0) {
                    //請求金額の値を支払い金額にセットする
                    Long paymentVal = Long.parseLong(payments.getValueAt(0, 2).toString()) - ia.getTotal(5).getValue().longValue();
                    payments.setValueAt(paymentVal, 0, 2);
                    //IVS_LVTU start add 2017/08/25 #23400 [gb]お会計画面＞現金欄以外にフォーカスがあると返金処理が正しく行われない
                    payments.setRowSelectionInterval(0, 0);
                    //IVS_LVTU end add 2017/08/25 #23400 [gb]お会計画面＞現金欄以外にフォーカスがあると返金処理が正しく行われない
                    //IVS_LVTU start add 2017/08/29 #23400 [gb]お会計画面＞現金欄以外にフォーカスがあると返金処理が正しく行われない
                    payments.setColumnSelectionInterval(2, 2);
                    //IVS_LVTU end add 2017/08/29 #23400 [gb]お会計画面＞現金欄以外にフォーカスがあると返金処理が正しく行われない
                    this.changePaymentValue();

                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        //売掛金発生確認
        if (ia.getTotal(ia.getTotal().size() - 1).getValue() < 0 && !isTempFlag) {
            if (MessageDialog.showYesNoDialog(this,
                    MessageUtil.getMessage(3003),
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) != 0) {
                return false;
            }
        }

        //HashMap course = new HashMap();
        //Integer num = 0;
        // 201711 GB edit start #29218 消化回数小数回対応
        //Integer consumptionNum = 0;
        Integer reservationNum = 0;
        double consumptionRestNum = 0; // 消化残回数
        // 201711 GB edit end #29218 消化回数小数回対応
        //Integer courseId;

        for (int i = 0; i < ia.getSales().size(); i++) {
            if (ia.getSales().get(i).getConsumptionCourse() != null) {

//                if (course.containsKey(ia.getSales().get(i).getConsumptionCourse().getCourseId())) {
//
//                    num = (Integer) course.get(ia.getSales().get(i).getConsumptionCourse().getCourseId());
//                    num += ia.getSales().get(i).getConsumptionNum().intValue();
//                    course.put(ia.getSales().get(i).getConsumptionCourse().getCourseId(), num);
//                } else {
//                   course.put(ia.getSales().get(i).getConsumptionCourse().getCourseId(), ia.getSales().get(i).getConsumptionNum().intValue());
//                }
                
                ConsumptionCourse conCourse = ia.getSales().get(i).getConsumptionCourse();
                
                // 201712 GB add start #32954 お会計：契約日以前の消化はできないようにする
                Date contractDate; //契約日
                Date contractConsumptionDate = salesDate.getDate(); //消化日（売上日）
                if(conCourse.getContractNo() == null) {
                    // 契約内消化
                    contractDate = contractConsumptionDate;
                }else {
                    contractDate = conCourse.getSalesDate();
                }
                if(contractDate != null && contractConsumptionDate != null) {
                    if(contractDate.compareTo(contractConsumptionDate) > 0) {
                        // 契約日が消化日より未来
                        MessageDialog.showMessageDialog(this,
                                        "売上日時点で契約していないコースが消化されています。",
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }
                // 201712 GB add end #32954 お会計：契約日以前の消化はできないようにする
                
                if (conCourse.getContractNo() == null) {
                    // 契約内消化
                    reservationNum = 0;
                    double digestionNum  = ia.getSales().get(i).getConsumptionNum(); // この伝票で消化する消化回数
                    consumptionRestNum = conCourse.getNum().doubleValue();
                    consumptionRestNum = consumptionRestNum - digestionNum;
                } else {
                    reservationNum = getTotalNumReservationOfCourse(ia.getSales().getCustomer().getCustomerID(), conCourse.getCourseId(), conCourse.getContractShopId(), conCourse.getContractNo(), conCourse.getContractDetailNo(), ia.reservationNo);
                    // 201711 GB edit start #29218                    
                    //consumptionRestNum = getTotalRestNumOfCourse(ia.getSales().getSlipNo(), ia.getSales().getCustomer().getCustomerID(), conCourse.getCourseId(), conCourse.getContractShopId(), conCourse.getContractNo(), conCourse.getContractDetailNo());
                    consumptionRestNum = conCourse.getConsumptionRestNum();
                    // 201711 GB edit end #29218
                }
                
                // 201711 GB edit start #29218
                //        if (reservationNum > consumptionNum) {
                //            //vtbphuong start edit 20140421 Request #22456
                //            if (this.getSelectedShop().getReservationOverbookingFlag() == 0) {
                //                if (MessageDialog.showYesNoDialog(
                //                        this,
                //                        "予約数が契約数をオーバーしています。追加しますか？",
                //                        this.getTitle(),
                //                        JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
                //                    return false;
                //                }
                //            } else if (this.getSelectedShop().getReservationOverbookingFlag() == 1) {
                //                MessageDialog.showMessageDialog(this,
                //                        " 契約残数以上の予約は取得できません。",
                //                        this.getTitle(),
                //                        JOptionPane.INFORMATION_MESSAGE);
                //                return false;
                //            }
                //        }
                
                // 「契約数以上の予約の取得可」のチェックが「有」でも「無」でも、消化数が契約数をオーバーするなら精算不可
                if(consumptionRestNum < 0) {
                    MessageDialog.showMessageDialog(this,
                                    "消化回数が契約回数をオーバーするため登録できません。",
                                    this.getTitle(),
                                    JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                
                if(reservationNum > 0) {
                    // 未来予約あり
                     double tmpConsumptionNum = Math.ceil(consumptionRestNum); //消化回数切り上げ
                     if(reservationNum > tmpConsumptionNum) {
                         // 残回数以上に消化予約がある
                         if (this.getSelectedShop().getReservationOverbookingFlag() == 0) {
                            if (MessageDialog.showYesNoDialog(
                                    this,
                                    "未来予約を含む消化回数が契約回数をオーバーしますが、よろしいですか？",
                                    this.getTitle(),
                                    JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
                                return false;
                            }
                        }else if (this.getSelectedShop().getReservationOverbookingFlag() == 1) {
                            MessageDialog.showMessageDialog(this,
                                    "未来予約を含む消化回数が契約回数をオーバーするため登録できません。",
                                    this.getTitle(),
                                    JOptionPane.INFORMATION_MESSAGE);
                            return false;
                        }
                    }
                }
                // 201711 GB edit end #29218
                
            }
        }

//        ArrayList keys = new ArrayList(course.keySet());
//        ArrayList values = new ArrayList(course.values());
//        for (int i = 0; i < course.size(); i++) {
//            courseId = (Integer) keys.get(i);
//            reservationNum = getTotalNumReservationOfCourse(ia.getSales().getCustomer().getCustomerID(), courseId,ia.reservationNo);
//            consumptionNum = getTotalRestNumOfCourse(ia.getSales().getCustomer().getCustomerID(), courseId);
//            if ((Integer)values.get(i)+reservationNum > consumptionNum) {
//                //VTAn Start Edit 20140421 Request #22456
////                 if (MessageDialog.showYesNoDialog(
////                        this,
////                        "予約数が契約数をオーバーしています。追加しますか？",
////                        this.getTitle(),
////                        JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
////                    return false;
////                }
//
//                if (this.getSelectedShop().getReservationOverbookingFlag() == 0) {
//                    if (MessageDialog.showYesNoDialog(
//                            this,
//                            "予約数が契約数をオーバーしています。追加しますか？",
//                            this.getTitle(),
//                            JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
//                        return false;
//                    }
//                } else if (this.getSelectedShop().getReservationOverbookingFlag() == 1) {
//                    MessageDialog.showMessageDialog(this,
//                            " 契約残数以上の予約は取得できません。",
//                            this.getTitle(),
//                            JOptionPane.INFORMATION_MESSAGE);
//                    return false;
//                }
//                //VTAn End Edit 20140421 Request #22456
//            }
//
//        }
        //ivs luc start add 契約回数が0回のコースが存在する。
        for(int i=0;i<ia.getSales().size();i++) {
            if(ia.getSales().get(i).getProductDivision()==5 && ia.getSales().get(i).getCourse().getNum()==0){
                      MessageDialog.showMessageDialog(this,
                                "契約回数が0回のコースが存在します。契約回数は1回以上で設定してください。",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        return false;
            }
        }
        //ivs luc end add 契約回数が0回のコースが存在する。
        
        //施術時間の変更 start add
        if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
            boolean allProduct = true;
            for (DataSalesDetail dsd : ia.getSales()) {
                if (dsd.getProductDivision() != DISCOUNT_DIVISION_PRODUCT) {
                    allProduct = false;
                    break;
                }
            }
            if ( allProduct ) {
                //明細がすべて商品の場合は施術時間が空白時補完する
                if (txtOpeMinute.getText().equals("")) {
                    txtOpeMinute.setText("0");
                }
                if (txtOpeSecond.getText().equals("")) {
                    txtOpeSecond.setText("0");
                }
            } else {
                //施術時間（分）
                if ((txtOpeMinute.getText().equals("0") && txtOpeSecond.getText().equals("00")) ||
                        (txtOpeMinute.getText().equals("0") && txtOpeSecond.getText().equals("0"))) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "施術時間"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    txtOpeMinute.requestFocusInWindow();
                    return false;
                }
            }
            //施術時間（分）
            if (txtOpeMinute.getText().equals("")) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "施術時間"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtOpeMinute.requestFocusInWindow();
                return false;
            }

            //施術時間（秒）
            if (txtOpeSecond.getText().equals("")) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "施術時間"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtOpeSecond.requestFocusInWindow();
                return false;
            }
        }
        //施術時間の変更 end add
        return true;
    }

    private String getTotalRestNumOfCourseSQL(Integer slipNo, int customerId, int courseId, int contractShopID, int contractNo, int contractDetailNo) {
        StringBuilder sql = new StringBuilder(1000);

//        sql.append(" select cast(sum(product_num) - sum(consumtionnum)as int) as num \n");
//        sql.append(" from \n");
//        sql.append(" ( \n");
//        sql.append("    select  sum(distinct dc.product_num) as product_num,coalesce(sum(dcd.product_num), 0) as consumtionnum  \n");
//        sql.append("    from     data_sales ds         inner join data_contract dc \n");
//        sql.append("    on ds.slip_no = dc.slip_no                and ds.shop_id = dc.shop_id and dc.delete_date is null \n");
//        sql.append("    and (dc.contract_status is null or dc.contract_status = 2 )\n");
//        sql.append("    inner join mst_course mc                 on dc.product_id = mc.course_id          \n");
//        sql.append("    inner join mst_course_class mcc                 on mc.course_class_id = mcc.course_class_id\n");
//        sql.append("    left join data_contract_digestion dcd                on dc.shop_id = dcd.contract_shop_id \n");
//        sql.append("    and dc.contract_no = dcd.contract_no \n");
//        sql.append("    and dc.contract_detail_no = dcd.contract_detail_no  \n");
//        sql.append("    where         ds.sales_date is not null  \n");
//        sql.append("    and         ds.customer_id = " + customerId + "  \n");
//        sql.append("    and ( dc.valid_date is null or dc.valid_date >= current_date)  \n");
//        sql.append("    and course_id = " + courseId + " \n");
//        // vtbphuong start add 20140422 Request #22456
//        sql.append("    and dcd.contract_shop_id  = " + contractShopID + " \n");
//         sql.append("    and dcd.contract_no  = " + contractNo + " \n");
//         sql.append("    and dcd.contract_detail_no  = " + contractDetailNo + " \n");
//        // vtbphuong start add 20140422 Request #22456
//        sql.append("    group by mc.course_id,dc.slip_no,dc.shop_id,dc.product_num  \n");
//        sql.append(" )a");

        
        // 201710 GB del start #29218 消化回数小数回対応
        //sql.append(" SELECT cast(sum(product_num) - sum(consumtionnum)AS int) AS num \n");
        sql.append(" SELECT sum(product_num) - sum(consumtionnum) AS num \n");
        sql.append("FROM \n");
        sql.append("( \n");
        sql.append("	SELECT  dc.product_num AS product_num, \n");
        sql.append("sum( coalesce (dcd.product_num,0)) AS consumtionnum  \n");
        sql.append("FROM data_contract_digestion dcd  \n");
        sql.append("RIGHT JOIN DATA_CONTRACT dc ON dc.shop_id = dcd.contract_shop_id \n");
        sql.append("AND dc.contract_no = dcd.contract_no \n");
        sql.append("   AND dc.contract_detail_no = dcd.contract_detail_no \n");
        sql.append("Where dc.shop_id = ").append(contractShopID).append("  \n");
        sql.append("     AND dc.contract_no = ").append(contractNo).append("   \n");
        sql.append("     AND dc.contract_detail_no = ").append(contractDetailNo).append("   \n");
        sql.append("and dc.delete_date is null  \n");
        sql.append("and dcd.delete_date is null  \n");
        // 201710 GB del start #29218 契約伝票内の消化時の不具合
        //        if (slipNo != null && !String.valueOf(slipNo).equals("")) {
        //            sql.append("and dcd.slip_no <> " + SQLUtil.convertForSQL(slipNo) + " \n");
        //        }
        // 201710 GB del end #29218
//        sql.append("AND ( select sales_date from data_sales");
//        sql.append("     where  slip_no = dcd.slip_no  and shop_id = dc.shop_id   ) IS NOT NULL  ");
//        sql.append("AND ( select delete_date from data_sales");
//        sql.append("     where  slip_no = dcd.slip_no  and shop_id = dc.shop_id) IS NULL  ");
        sql.append("group by dc.product_num  \n");
        sql.append(")a \n");

        return sql.toString();
    }

    private String getTotalNumReservationOfCourseSQL(Integer customerId, Integer courseId, Integer contractShopID, Integer contractNo, Integer contractDetailNo, Integer reservationNo) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select count(*) as num from data_reservation_detail rd \n");
        sql.append(" inner join data_reservation r on r.reservation_no = rd.reservation_no and r.shop_id = rd.shop_id \n");
        sql.append(" where rd.course_flg = 2 and rd.technic_id = " + courseId + " \n");
        sql.append(" and r.customer_id =" + customerId + "\n");
        sql.append(" and rd.contract_shop_id =" + contractShopID + "\n");
        sql.append(" and rd.contract_no =" + contractNo + "\n");
        sql.append(" and rd.contract_detail_no =" + contractDetailNo + "\n");
        // vtbphuong start add  20140821 Request #30085
       // sql.append(" and r.status<3 \n");
         sql.append(" and  r.status< 3 and to_char(rd.reservation_datetime , 'YYYYMMDD' ) >= to_char(current_timestamp , 'YYYYMMDD' )   \n");
        // vtbphuong end add 20140821 Request #30085
        sql.append(" and r.delete_date is null and rd.delete_date is null \n");
        if (reservationNo != null) {
            sql.append(" and r.reservation_no not in(" + reservationNo + ")");
        }
        //Luc start Bug #29976 [gb]契約数以上の登録をしていないのにお会計時にアラートが発生する
        if((this.ia.getSales()!=null && ia.getSales().getSlipNo()!=null)){
             sql.append(" and r.slip_no  not in(" + ia.getSales().getSlipNo() + ")");
        }
        //Luc end Bug #29976 [gb]契約数以上の登録をしていないのにお会計時にアラートが発生する
        return sql.toString();
    }
//    public Integer getTotalRestNumOfCourse(int customerId, int courseId) {
//        Integer result = 0;
//        try {
//            ConnectionWrapper con = SystemInfo.getConnection();
//            ResultSetWrapper rs = con.executeQuery(getTotalRestNumOfCourseSQL(customerId, courseId));
//            if (rs.next()) {
//
//                result = rs.getInt("num");
//            }
//        } catch (SQLException e) {
//            System.out.print(e);
//        }
//        return result;
//    }
//    public Integer getTotalNumReservationOfCourse(int customerId, int courseId,Integer reservationNo) {
//        Integer result = 0;
//        try {
//            ConnectionWrapper con = SystemInfo.getConnection();
//            ResultSetWrapper rs = con.executeQuery(getTotalNumReservationOfCourseSQL(customerId, courseId,reservationNo));
//            if (rs.next()) {
//
//                result = rs.getInt("num");
//            }
//        } catch (SQLException e) {
//            System.out.print(e);
//        }
//        return result;
//    }
    
    /**
     * 消化残回数取得
     * 201710 GB edit #29218 消化回数小数回対応
     * 
     * @param slipNo
     * @param customerId
     * @param courseId
     * @param contractShopID
     * @param contractNo
     * @param contractDetailNo
     * @return 
     */
    //public Integer getTotalRestNumOfCourse(Integer slipNo, int customerId, int courseId, int contractShopID, int contractNo, int contractDetailNo) {
    public double getTotalRestNumOfCourse(Integer slipNo, int customerId, int courseId, int contractShopID, int contractNo, int contractDetailNo) {
        // 201710 GB edit start #29218
        // Integer result = 0;
        double result = 0;
        // 201710 GB edit end #29218
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(getTotalRestNumOfCourseSQL(slipNo, customerId, courseId, contractShopID, contractNo, contractDetailNo));
            if (rs.next()) {
                
                // 201710 GB edit start #29218
                //result = rs.getInt("num");
                result = rs.getDouble("num");
                // 201710 GB edit end #29218
            }
        } catch (SQLException e) {
            System.out.print(e);
        }
        return result;
    }

    public Integer getTotalNumReservationOfCourse(int customerId, int courseId, int contractShopID, int contractNo, int contractDetailNo, Integer reservationNo) {
        Integer result = 0;
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(getTotalNumReservationOfCourseSQL(customerId, courseId, contractShopID, contractNo, contractDetailNo, reservationNo));
            if (rs.next()) {

                result = rs.getInt("num");
            }
        } catch (SQLException e) {
            System.out.print(e);
        }
        return result;
    }

    /**
     * 割引区分名を取得する。
     *
     * @param discountDivision 割引区分
     * @return 割引区分名
     */
    private String getDiscountDivisionName(Integer discountDivision) {
        switch (discountDivision) {
            case DISCOUNT_DIVISION_ALL:
                return "全体";
            case DISCOUNT_DIVISION_TECHNIC:
                return "技術";
            case DISCOUNT_DIVISION_PRODUCT:
                return "商品";
            default:
                return "";
        }
    }

    private void hairInputAccountSetup() {
        ia.getSales().setShop(targetShop);
        //ia.getSales().setDesignated(chargeNamed.isSelected());
        //ATGS 指名orフリー設定
        ia.getSales().setDesignated(this.shimeiFreeFlag);

        ia.getSales().setStaff((MstStaff) chargeStaff.getSelectedItem());
        System.out.println("●主担当者セット\n" + (MstStaff) chargeStaff.getSelectedItem());
        ia.getSales().setSlipNo((slipNo.getText().equals("＜新規＞")
                || slipNo.getText().equals("") ? null
                : Integer.parseInt(slipNo.getText())));
        ia.getSales().setSalesDate(salesDate.getDate());

        for (int i = 0; i < products.getRowCount(); i++) {
            int index = getProductsIndexForStaffAndChargeNamed(i, 0);

            this.setMenuStaffAndChargeNamedData(index, i);
        }
    }

//	private void regist()
//	{
//            ia.setStatus(2);
//            ia.getSales().setTempFlag(true);
//
//            ia.getSales().setShop(targetShop);
//            ia.getSales().setDesignated(this.shimeiFreeFlag);
//            ia.getSales().setStaff((MstStaff)chargeStaff.getSelectedItem());
//            ia.getSales().setSlipNo((slipNo.getText().equals("＜新規＞") || slipNo.getText().equals("") ? null : Integer.parseInt(slipNo.getText())));
//            ia.getSales().setSalesDate(salesDate.getDate());
//
//            for (int i = 0 ; i < products.getRowCount() ; i++) {
//                int index = getProductsIndexForStaffAndChargeNamed( i , 0);
//                this.setMenuStaffAndChargeNamedData( index , i );
//            }
//
//            if (ia.regist()) {
//                //this.printReceipt();
//                //slipNo.setText(ia.getSales().getSlipNo().toString());
//                MessageDialog.showMessageDialog(this,
//                                MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
//                                this.getTitle(),
//                                JOptionPane.INFORMATION_MESSAGE);
//
//                //他の画面から開かれている場合
//                if (this.getOpener() instanceof HairInputAccountOpener) {
//                    HairInputAccountOpener riao = (HairInputAccountOpener)this.getOpener();
//                    riao.refresh();
//                    this.showOpener();
//                } else if (this.getOpener() instanceof SearchAccountPanel) {
//                    //伝票検索から開かれている場合
//                    SearchAccountPanel sap = (SearchAccountPanel)this.getOpener();
//                    sap.searchAccount();
//                    sap.showAccount();
//                    this.showOpener();
//                } else {
//                    this.init();
//                    this.showOpener();
//                }
//            } else {
//                MessageDialog.showMessageDialog(this,
//                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "伝票データ"),
//                        this.getTitle(),
//                        JOptionPane.ERROR_MESSAGE);
//            }
//	}
    /**
     * 売掛金をセットする。
     */
    public void setBill() {
        //売掛金を読み込む
        ia.loadBill(ia.getSales().getCustomer().getCustomerID());

        billValue.setText(ia.getBill().getBillValue().toString());

        collectBillButton.setEnabled(0l < ia.getBill().getBillValue());
    }

    /**
     * レスポンスをセットする。
     */
    public void setResponse() {
        initResponse();
        //Edit start 2013-10-31 lvut

        // if(ia.getStatus() <3 && this.getOpener() != null){
        // responseItemComboBox1.setSelectedItem(ia.getSales().getResponse1());
        //responseItemComboBox2.setSelectedItem(ia.getSales().getResponse2());

        // }else{
        if (ia.getResponseEffect()[1].getResponse() != null) {
            for (MstResponse mr : mrs) {
                if (mr.getResponseID().equals(ia.getResponseEffect()[1].getResponse().getResponseID())) {
                    responseItemComboBox1.setSelectedItem(mr);
                }
            }
        } else {
            responseItemComboBox1.setSelectedItem(ia.getSales().getResponse1());
        }

        if (ia.getResponseEffect()[0].getResponse() != null) {
            for (MstResponse mr : mrs) {
                if (mr.getResponseID().equals(ia.getResponseEffect()[0].getResponse().getResponseID())) {
                    responseItemComboBox2.setSelectedItem(mr);
                }
            }
        } else {
            responseItemComboBox2.setSelectedItem(ia.getSales().getResponse2());
        }
        //}
        //Edit end 2013-10-31 lvut
    }

    /**
     * 入力項目をクリアする。
     */
    private void clear() {
        //Start edit 20131030 lvut
        if (this.canChangeKeys() && !changeCus) {
            slipNo.setText("＜新規＞");
            salesDate.setDate(this.getSelectedShop().getSystemDate().getTime());
            reservationType.setText("");
            customerNo.setText("");
            customerName1.setText("");
            customerName2.setText("");
            genzaiPoint.setText("");
        }
        //End edit 20131030 lvut

        chargeStaff.setSelectedIndex(0);
        staff.setSelectedIndex(0);
        billValue.setText("");
        collectBillButton.setEnabled(false);
        responseItemComboBox1.setSelectedIndex(0);
        responseItemComboBox2.setSelectedIndex(0);

        // 開拓者
        cboexPioneerName.setSelectedIndex(0);

        // 主担当指名フラグ
        changeShimeiFreeFlag(false);

        // 反響項目
        ia.getResponseEffect()[0].setResponse(null);
        ia.getResponseEffect()[1].setResponse(null);

        // 技術・商品選択欄
        productDivision.setSelectedIndex(0);

        // ポイント
        SwingUtil.clearTable(point);
        pointIndexList.clear();

        //IVS_Thanh start add 2014/07/30 Mashu_お会計表示
        ia.getReservationMainstaffs().clear();
        //IVS_Thanh start add 2014/07/30 Mashu_お会計表示

        this.requestFocusToPayments();
    }

    /**
     * 伝票データを読み込む。
     *
     * @param slipNo 伝票No.
     */
    public void load(MstShop shop, Integer slipNo) {
        moveButtonPanel.setVisible(targetList.size() > 1);
        setEnabledPrevNextButton();

        // ログイン店舗以外を閲覧する場合
        boolean isCurrentShop = false;
        isCurrentShop = isCurrentShop || SystemInfo.isGroup();
        isCurrentShop = isCurrentShop || targetShop.getShopID().equals(shop.getShopID());
        //Luc start delete 20150930 #43006
        //if (!isCurrentShop) {
            // 店舗コンボの内容を更新する
            targetShop = shop;
        //}
        //Luc end delete 20150930  #43006
        //Luc start add 20150930 #43006
        SwingUtil.clearTable(products);
        this.initProducts();
        //列変数の設定
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
        this.settingProductColumn();
        //nhanvt start add 20150326 Bug #35729
        ia.setMsSetting(ms);
        //nhanvt end add 20150326 Bug #35729
        ia.load(shop, slipNo);
        ia.setStatus(3);
        tempRegistButton.setEnabled(false);
        // SOSIA連動にあわせて連動ボタンを変更する
        updateSosiaGearButton();

        initPointPrepaid();
        showPointPrepaid();
        //IVS_LVTU start add 2017/08/31 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
        initDataConsumptionCourse();
        //IVS_LVTU end add 2017/08/31 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される

    }

    //IVS_LVTU start add 2017/08/31 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される
    /**
     * まだ一回も消化していない契約明細は
     */
    private void initDataConsumptionCourse() {
        for(DataSalesDetail dsd : ia.getSales()) {
            if(dsd.getProductDivision() == 5 && dsd.isEditable()) {
                Course course = dsd.getCourse();
                CourseClass courseClass = course.getCourseClass();
                ConsumptionCourseClass consumptionCourseClass = null;

                //未契約コース分類を格納する
                if (consumptionCouserClassMap.containsKey(courseClass.getCourseClassId())) {
                    consumptionCourseClass = consumptionCouserClassMap.get(courseClass.getCourseClassId());
                } else {
                    consumptionCourseClass = new ConsumptionCourseClass();
                    consumptionCourseClass.setCourseClassId(courseClass.getCourseClassId());
                    consumptionCourseClass.setCourseClassName(courseClass.getCourseClassName());
                    consumptionCourseClass.setShopId(this.getSelectedShopID());
                    consumptionCourseClass.setSlipNo(null);
                    consumptionCourseClass.setDisplaySeq(courseClass.getDisplaySeq());
                    consumptionCourseClass.setShopCategoryID(courseClass.getShopCategoryID());

                    consumptionCouserClassMap.put(consumptionCourseClass.getCourseClassId(), consumptionCourseClass);

                }
                //消化コースリストにセットするための消化コースデータを生成する
                ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                consumptionCourse.setShopId(this.getSelectedShopID());
                consumptionCourse.setSlipNo(dsd.getSlipNo());
                consumptionCourse.setCourseId(course.getCourseId());
                consumptionCourse.setCourseName(course.getCourseName());

                long price = 0;
                long basePrice = 0;
                if (course.getNum().longValue() != 0) {
                    price = new BigDecimal(dsd.getDataContract().getProductValue()).divide(new BigDecimal(course.getNum().longValue()), 0, RoundingMode.UP).longValue();
                    basePrice = new BigDecimal(dsd.getDataContract().getProductValue()).divide(new BigDecimal(course.getNum().longValue()), 0, RoundingMode.UP).longValue();
                }
                consumptionCourse.setPrice(price);
                consumptionCourse.setBasePrice(basePrice);
                consumptionCourse.setOperationTime(course.getOperationTime());
                consumptionCourse.setPraiseTime(course.isIsPraiseTime());
                consumptionCourse.setPraiseTimeLimit(course.getPraiseTimeLimit());
                consumptionCourse.setNum(course.getNum());
                consumptionCourse.setConsumptionNum(0d);
                consumptionCourse.setConsumptionRestNum(course.getNum().doubleValue());
                consumptionCourse.setContractShopId(dsd.getDataContract().getShop().getShopID());
                consumptionCourse.setContractNo(dsd.getDataContract().getContractNo());
                consumptionCourse.setContractDetailNo(dsd.getDataContract().getContractDetailNo());
                consumptionCourse.setStaffId(null);
                consumptionCourse.setTmpContractNo(generateTmpContractNo(course));
                consumptionCourse.setTaxRate(dsd.getTaxRate());

                dsd.setTmpContractNo(consumptionCourse.getTmpContractNo());

                //未契約コースを格納する
                Map<String, ConsumptionCourse> courseMap = null;
                if (consumptionCourseMap.containsKey(consumptionCourseClass.getCourseClassId())) {
                    courseMap = consumptionCourseMap.get(consumptionCourseClass.getCourseClassId());
                } else {
                    courseMap = new HashMap<String, ConsumptionCourse>();
                }
                courseMap.put(generateTmpContractNo(course), consumptionCourse);
                consumptionCourseMap.put(consumptionCourseClass.getCourseClassId(), courseMap);
            }
        }
    }
    //IVS_LVTU end add 2017/08/31 #23407 [gb]お会計画面＞契約の金額を変更⇒その契約を同一伝票で消化⇒消化金額が契約金額変更前の数字で表示＆登録される

    private boolean isCancelCourse() {
        boolean flg = false;
        // vtbhuong start change 20140617 Bug #25283
        //String sql = "Select * from view_data_sales_detail_valid where product_division = 8 and shop_id=" + ia.getShop().getShopID() + " and slip_no=" + ia.getSales().getSlipNo() + "";
        String sql = " Select slip_no from view_data_sales_detail_valid where product_division = 8 and shop_id=" + ia.getShop().getShopID() + " and slip_no=" + ia.getSales().getSlipNo() + " \n ";
        sql += " Union all \n ";
        sql += " Select slip_no from data_contract where  shop_id=" + ia.getShop().getShopID() + " and slip_no=" + ia.getSales().getSlipNo() + "  and delete_date IS NOT NULL \n  ";

        // vtbhuong start change 20140617 Bug #25283
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            ResultSetWrapper rs = con.executeQuery(sql);
            if (rs.next()) {
                flg = true;
            }
        } catch (SQLException e) {
        }
        return flg;
    }

    /**
     * 一時保存用　伝票データを読み込む。
     *
     * @param slipNo 伝票No.
     */
    public void loadTempData(MstShop shop, Integer slipNo, Integer reservationNo, Integer subStatus) {
        ia.load(shop, slipNo);
        ia.setSubStatus(subStatus);
        ia.setReservationNo(reservationNo);
        ia.setStatus(2);
        // SOSIA連動にあわせて連動ボタンを変更する
        updateSosiaGearButton();
        // vtbphuong start add 20150518
        for (DataSalesDetail dsd : ia.getSales()) {

            if (dsd.getProductDivision() == 5) {
                //予約データにコース契約がある場合
                Course course = dsd.getCourse();
                //仮契約番号発行
                dsd.setTmpContractNo(generateTmpContractNo(course));
                //消化コースクラス・消化コースリストに追加。
                this.addConsumptionCourse(consumptionCourseClass, consumptionCourse, course);
            }
        }
        // vtbphuong end add 20150518


    }

    /**
     * 販売日をセットする。
     *
     * @param salesDate　販売日
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate.setDate(salesDate);
    }

    /**
     * 予約データから伝票データを表示する。
     *
     * @param reservationNo 表示する予約データの予約No.
     */
    public void loadReservation(Integer shopID, Integer reservationNo, Integer subStatus) {
        ia.setSubStatus(subStatus);
        ia.loadReservation(shopID, reservationNo);
        //レジスタッフをセット
        //staff.setSelectedIndex(ia.getStaffs().getIndexByID( ia.getSales().getStaff().getStaffID() ));
        staff.setSelectedItem(ia.getSales().getStaff());

        // SOSIA連動にあわせて連動ボタンを変更する
        updateSosiaGearButton();
    }

    public void setReservationConsumptionCourseMap() {
        for (DataSalesDetail dsd : ia.getSales()) {
            if (dsd.getProductDivision() == 5) {
                //予約データにコース契約がある場合
                Course course = dsd.getCourse();
                //仮契約番号発行
                dsd.setTmpContractNo(generateTmpContractNo(course));
                //消化コースクラス・消化コースリストに追加。
                this.addConsumptionCourse(consumptionCourseClass, consumptionCourse, course);
            }
        }
    }

    private Boolean checkPrevSalesUpdate(java.util.Date dt) {

        boolean result = true;

        if (!SystemInfo.getAccountSetting().isPrevSalesUpdate()) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(dt);

            if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)
                    || cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {

                result = false;
            }
        }

        return result;
    }

    /**
     * 売上データを表示する。
     */
    public void showData() {
//SystemInfo.info("showData start");
        contractStatus = false;
        targetShop = ia.getSales().getShop();

        // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
        try {
            ia.getSales().getShop().load(SystemInfo.getConnection());
        }catch (Exception e){

        }
        //Luc start edit 20141128 New request #33189 [gb]トータルビューティ版お会計の制御変更
        //if (ia.getSales().getShop().getUseShopCategory() == 1) {

        //Luc start edit 20150817 #41948
        //if (ia.getSales().getShop().getUseShopCategory() == 1 && !SystemInfo.getDatabase().contains("pos_hair_mashu")) {
        //Luc end edit 20141128 New request #33189 [gb]トータルビューティ版お会計の制御変更
        //    mainStaffButton.setVisible(true);
        //}
        //else
        //{
        //    mainStaffButton.setVisible(false);
        //}
        // IVS_Thanh end add 2014/07/11 Mashu_お会計表示
//SystemInfo.info("initBusinessCategory start");
        initBusinessCategory();
         //Luc start edit 20150817 #41948

        if (ia.getSales().getSlipNo() != null) {
            slipNo.setText(ia.getSales().getSlipNo().toString());
            //IVS_TMTrong start add 2015/10/05 New request #43038
            closeButton.setEnabled(true);
            //IVS_TMTrong end add 2015/10/05 New request #43038
        }
        //IVS_LVTu start add 2015/10/07 New request #43038
        if ( isReturnButtonFlag() ) {
            closeButton.setEnabled(true);
        }
        //IVS_LVTu end add 2015/10/07 New request #43038
        if(ia.getSales().getSalesDate()!=null) {
            salesDate.setDate(ia.getSales().getSalesDate());
        }

        if (ia.getSales().getMobileFlag() != null) {
            reservationType.setText("WEB予約");
        } else if (ia.getSales().getNextFlag() != null && ia.getSales().getNextFlag().equals(1)) {
            reservationType.setText("前回来店");
        } else if (ia.getSales().getPreorderFlag() != null && ia.getSales().getPreorderFlag().equals(1)) {
            reservationType.setText("事前予約");
        }

        customerNo.setText(ia.getSales().getCustomer().getCustomerNo());
        customerName1.setText(ia.getSales().getCustomer().getCustomerName(0));
        customerName2.setText(ia.getSales().getCustomer().getCustomerName(1));
        this.showNowPoint(ia.getSales().getCustomer());

        // 主担当を表示する
        //chargeNamed.setSelected( ia.getSales().getDesignated() );
        //chargeFree.setSelected( !ia.getSales().getDesignated() );
        //ATGS
//		this.shimeiFreeFlag=ia.getSales().getDesignated();
        //画像が変わっていなかったので修正
        changeShimeiFreeFlag(ia.getSales().getDesignated());

        // start edit 20130806 nakhoa
        if (ia.getSales().getStaff() != null) {
            //IVS_ptthu start edit 20160531 Bug #50933
            this.setChargeStaff(ia.getSales().getStaff().getStaffID());
            //IVS_ptthu end edit 20160531 Bug #50933
        }
        // end edit 20130806 nakhoa

        this.setStaff(ia.getSales().getPayment(0).getStaff().getStaffID());

        //日付・顧客等の変更可不可を設定
        salesDate.setEnabled(ia.getStatus() == 3 ? false : this.canChangeKeys());

//		searchAccount.setEnabled(this.canChangeKeys());

//		searchCustomerButton.setEnabled(this.canChangeKeys());
//		customerNo.setEnabled(this.canChangeKeys());
        //売掛金
        this.setBill();
        // レスポンス
        setResponse();

        //伝票詳細
        detailCount = 0;
        MstTaxs taxList = ia.getTaxs(salesDate.getDate());
//		SwingUtil.clearTable(products);
        for (DataSalesDetail dsd : ia.getSales()) {

            // IVS_LVTU add 2019/09/19 [gb]SPOS増税対応
            if (dsd.getTaxRate() == null || dsd.getTaxRate() <= 0) {
                int index = ia.getIndexByTaxRate(dsd.getTaxRate()) == -1 ? 0 : ia.getIndexByTaxRate(dsd.getTaxRate());
                dsd.setTaxRate(taxList.get(index).getTaxRate());
            }
            if (dsd.getProductDivision() == 5 || dsd.getProductDivision() == 7
                    || dsd.getProductDivision() == 8 || dsd.getProductDivision() == 9) {
                //コース契約の場合


                this.addCourseRow(dsd, true);
                //Start add 20131105 lvut (hide 精算 when contrac_status =1,2 )
                if (dsd.getDataContract() != null) {
                    if (dsd.getDataContract().getContractStatus() != null) {
                        if (dsd.getDataContract().getContractStatus() == 1 || dsd.getDataContract().getContractStatus() == 2) {
                            contractStatus = true;
                        }
                    }
                    if (dsd.getDataContract().getDeleteDate() != null) {
                        contractStatus = true;
                    }
                }
                //End add 20131105 lvut (hide 精算 when contrac_status =1,2 )
            } else if (dsd.getProductDivision() == 6) {
                //コース消化の場合
                this.addConsumptionCourseRow(dsd, true);
                if (dsd.getConsumptionCourse() != null) {
                    if (dsd.getConsumptionCourse().getContractStatus() != null) {
                        if (dsd.getConsumptionCourse().getContractStatus() == 1) {
                            contractStatus = true;
                        }
                        if (dsd.getConsumptionCourse().getContractStatus() == 2) {
                            contractStatus = false;
                        }
                        if (dsd.getConsumptionCourse().getDelDate() != null) {
                            contractStatus = true;
                        }
                    }
                }
            } else {
                this.addProductRow(dsd, true);
            }
            //Luc start add 20150615 37394
            if(dsd.getProductDivision()==7) {
               isChangeContract = true;
            }
            //Luc end add 20150615 37394

        }

        tempProduct = products.getRowCount();

        int row = 0;

        //割引
        this.initDiscount();

        DataSalesDetail dsd = ia.getSales().getDiscount();

        JComboBox temp = (JComboBox) discounts.getValueAt(row, 0);
        temp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));


        boolean isSelected = false;

//SystemInfo.info("temp.getItemCount LOOP start");
        for (int i = 1; i < temp.getItemCount(); i++) {
            MstDiscount md = (MstDiscount) temp.getItemAt(i);
            if (md.getDiscountID() == dsd.getProduct().getProductID()) {
                temp.setSelectedIndex(i);
                discounts.setValueAt(dsd.getDiscount(), row, 1);
                isSelected = true;
                break;
            }
        }

        //施術時間の変更 2017/01/26 start add
        if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {

            if (ia.getOpeMinute() != null) {
                txtOpeMinute.setText(ia.getOpeMinute());
            }
            if (ia.getOpeSecond() != null) {
                txtOpeSecond.setText(ia.getOpeSecond());
            }
        }
        //施術時間の変更 2017/01/26 end add

        // 割引項目が削除されている場合
        if (dsd.getProduct().getProductID() != null && !isSelected) {

//SystemInfo.info("割引項目が削除されている場合");
            try {
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("     *");
                sql.append(" from");
                sql.append("     mst_discount");
                sql.append(" where");
                sql.append("     discount_id = " + SQLUtil.convertForSQL(dsd.getProduct().getProductID()));

                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

                if (rs.next()) {
                    MstDiscount md = new MstDiscount();
                    md.setData(rs);
                    temp.addItem(md);
                    temp.setSelectedIndex(temp.getItemCount() - 1);
                    discounts.setValueAt(dsd.getDiscount(), row, 1);
                }

                rs.close();

            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        discounts.repaint();

        row = 0;

//SystemInfo.info("支払");
        //支払
        // 伝票番号が有効な場合に限り支払いデータを入力する
        if (ia.getSales().getPayment(0).getSlipNo() != null) {
            for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
                if (dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPaymentClass() != null) {
                    if (row == payments.getRowCount()) {
                        this.addNewPaymentRow(false);
                    }

                    //現金以外の行の場合
                    if (0 < row) {
                        //支払区分を変更できる行の場合
                        if (payments.getValueAt(row, 0) instanceof JComboBox) {
                            JComboBox cb = (JComboBox) payments.getValueAt(row, 0);
                            cb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                            this.setJComboBoxItem(cb, dpd.getPaymentMethod().getPaymentClass().getPaymentClassName());
                            // 20170308 GB Start Add #59940　[gb]精算済みの伝票をお会計画面で表示時、支払方法が空欄になる。
                            int index = cb.getSelectedIndex() + 1;
                            if (index > 1) {
                                this.setPaymentMethodCombo(row, index);
                            }
                            // 20170308 GB End Add #59940
                        }

                        //支払方法を変更できる行の場合
                        if (payments.getValueAt(row, 1) instanceof JComboBox) {
                            JComboBox cb = (JComboBox) payments.getValueAt(row, 1);
                            cb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                            this.setJComboBoxItem(cb, dpd.getPaymentMethod().getPaymentMethodName());
                        }
                    }

                    payments.setValueAt(dpd.getPaymentValue(), row, 2);
                }

                row++;
            }
        }

        /*if(row == payments.getRowCount())
         {
         this.addNewPaymentRow(true);
         }*/

        payments.repaint();

//SystemInfo.info("setTotal");
        //合計
        this.setTotal();

        // 店舗ログインかつ、他店舗の情報を参照している場合は編集不可にする
        if (!SystemInfo.isGroup()) {
            if (!this.getSelectedShopID().equals(ia.getSales().getShop().getShopID())) {
                searchCustomerButton.setEnabled(false);
                clearButton.setEnabled(false);
                deleteButton.setEnabled(false);
                accountRegistButton.setEnabled(false);
            }
        }

        // 開拓者
        this.setPioneerName(ia.getSales().getPioneerCode());

        // 前月以前のデータ更新チェック
        if (ia.getSales().getSalesDate() != null && !checkPrevSalesUpdate(ia.getSales().getSalesDate())) {
            MessageDialog.showMessageDialog(
                    this,
                    "当月以外のデータ更新は行えません。",
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
            accountRegistButton.setEnabled(false);
            deleteButton.setEnabled(false);
            clearButton.setEnabled(false);
        } else {
            //Start edit 20131030 lvut
            if (stat) {
                accountRegistButton.setEnabled(true);
                deleteButton.setEnabled(true);
                clearButton.setEnabled(true);
            }
            //End edit 20131030 lvut
        }

        //Add start 2013-10-30 Hoa
        //在店したデータは顧客が変更できないようにする
        // IVS SANG START DELETE 20131114 [gb]予約登録画面、お会計画面の制御調整
        //if (ia.getStatus() > 1) {
        //    searchCustomerButton.setEnabled(false);
        //    customerNo.setEditable(false);
        //}
        // IVS SANG END DELETE 20131114 [gb]予約登録画面、お会計画面の制御調整
        // not display
        // searchButton.setVisible(false);
        // searchButton1.setVisible(false);
        //Add end 2013-10-30 Hoa
        if (contractStatus) {
            accountRegistButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

//SystemInfo.info("showCustomerDisplayToSalesValue");
        this.showCustomerDisplayToSalesValue();

        // check is Cancel course
        if (isCancelCourse()||isChangeContract) {
            deleteButton.setEnabled(false);
            accountRegistButton.setEnabled(false);
        } else {
            deleteButton.setEnabled(true);
            accountRegistButton.setEnabled(true);
        }

//SystemInfo.info(";;;;;;;;;");
        //add by ltthuc 2014/06/25
        arrPaymentValue = new ArrayList<String>();
        arrInitPayMentMothod = new ArrayList<Integer>();
        int size = ia.getSales().getPayment(0).size();

//SystemInfo.info("ia.getSales().getPayment(0).size() LOOP start");
        for (int i = 0; i < size; i++) {
            DataPaymentDetail data = ia.getSales().getPayment(0).get(i);

            if (data.getPaymentMethod() != null) {
                arrPaymentValue.add(ia.getSales().getPayment(0).get(i).getPaymentValue().toString());

                arrInitPayMentMothod.add(ia.getSales().getPayment(0).get(i).getPaymentMethod().getPaymentMethodID());
            } else {
                if (i == 0) {
                    arrInitPayMentMothod.add(1);
                } else {
                    arrInitPayMentMothod.add(-1);
                }
                arrPaymentValue.add("0");


            }
            //end add by ltthuc
        }
        //
        //Luc start edit 20150817 #41948
        //if(SystemInfo.getDatabase().contains("pos_hair_mashu")) {
//2度呼ばれるので削除
//        if (ia.getSales().getShop() != null) {
//            if(ia.getSales().getShop().getUseShopCategory()!=null) {
//                if (ia.getSales().getShop().getUseShopCategory() == 1) {
//                    loadMainStaff();
//                }
//            }
//        }
        //Luc start edit 20150817 #41948
    }

    /**
     * JComboBoxの選択されているItemを設定する。
     *
     * @param cb JComboBox
     * @param item 選択するItemの文字列
     */
    private void setJComboBoxItem(JComboBox cb, String item) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).toString().equals(item)) {
                cb.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * 伝票データの削除処理を行う。
     */
    private void delete() {
        if (MessageDialog.showYesNoDialog(
                this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, "伝票No." + slipNo.getText()),
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE) != 0) {
            return;
        }

        boolean result = false;



        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            if (ia.getSales().isExistDataContractDigestion(con)) {
                //削除しようとしている明細内にあるコース契約データに対しコース消化されているかどうか
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(3200),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            //IVS_LVTU start add 2017/08/23 #22930 [gb]お会計画面＞予約済の契約が削除できてしまう
            if (ia.getSales().isExistReservationDigestion(con)) {
                //未来日付で予約が登録されているため削除できません。
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(20005),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            //IVS_LVTU end add 2017/08/23 #22930 [gb]お会計画面＞予約済の契約が削除できてしまう
            result = ia.getSales().deleteAll(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        if (result) {
            this.init();
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.INFO_DELETE_SUCCESS, "伝票データ"),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED, "伝票データ"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }

        if (targetList.size() > 1) {
            targetList.remove(currentIndex);
            currentIndex = -1;
            this.moveSales(true);
        }

    }

    /**
     * 日付、顧客を変更できるかを取得する。
     *
     * @return 変更できる場合 true
     */
    public boolean canChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(boolean changeKeys) {
        this.changeKeys = changeKeys;
    }

    private class TotalCellRenderer extends DefaultTableCellRenderer {

        private Font defaultFont = null;
        private Font bigFont = null;

        public TotalCellRenderer() {
            super();
            defaultFont = getFont();
            bigFont = defaultFont.deriveFont(Font.BOLD, 18.0f);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            setHorizontalAlignment(SwingConstants.RIGHT);

            if ((row == 3 || row == 5) && column == 1) {
                this.setFont(bigFont);
            } else {
                this.setFont(defaultFont);
            }

            return this;
        }
    }

    private void requestFocusToPayments() {
        payments.setRowSelectionInterval(0, 0);
        payments.setColumnSelectionInterval(2, 2);
        payments.requestFocusInWindow();
    }

    /**
     * SOSIA連動ボタンを更新する
     */
    private void updateSosiaGearButton() {
        MstCustomer cus = ia.getSales().getCustomer();
        // SOSIA連動不可の場合には連動処理を行わない
        if (!SystemInfo.isSosiaGearEnable()) {
            return;
        }

        if (cus.getCustomerNo() == null || cus.getCustomerNo().equals("")) {
            changeSosiaGearButton.setIcon(null);
            changeSosiaGearButton.setPressedIcon(null);
            changeSosiaGearButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            changeSosiaGearButton.setContentAreaFilled(false);
            changeSosiaGearButton.setEnabled(false);
            return;
        }

        // SOSIA連動にあわせて連動ボタンを変更する
        if (!cus.getSosiaCustomer().isSosiaCustomer()) {
            // 連動OFF
            changeSosiaGearButton.setIcon(SystemInfo.getImageIcon("/button/common/SOSIA_regist_off.png"));
            changeSosiaGearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/SOSIA_regist_on.png"));

        } else {
            // 連動ON
            //changeSosiaGearButton.setIcon(SystemInfo.getImageIcon("/button/common/sosia_gear_active_off.jpg"));
            //changeSosiaGearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/sosia_gear_active_on.jpg"));
            changeSosiaGearButton.setIcon(SystemInfo.getImageIcon("/button/common/SOSIA_mail_off.png"));
            changeSosiaGearButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/SOSIA_mail_on.png"));

        }

        changeSosiaGearButton.setBorderPainted(false);
        changeSosiaGearButton.setOpaque(false);
        changeSosiaGearButton.setBackground(new Color(236, 233, 216));
        changeSosiaGearButton.setEnabled(true);
    }

    /**
     * SOSIA連動ダイアログを開く
     */
    private void changeSosiaGear() {
        SosiaGearDialog sgd = new SosiaGearDialog(ia.getSales().getCustomer());
        SwingUtil.openAnchorDialog(this.parentFrame, true, sgd, "SOSIA連動", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        sgd = null;
        updateSosiaGearButton();
    }

    /**
     * 伝票入力画面用FocusTraversalPolicy
     */
    private class InputAccountFocusTraversalPolicy
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
            if (aComponent.equals(salesDate)) {
                return customerNo;
            } else if (aComponent.equals(customerNo)) {
                return customerName1.isEditable() ? customerName1 : chargeStaffNo;
            } else if (aComponent.equals(customerName1)) {
                return customerName2;
            } else if (aComponent.equals(customerName2)) {
                return chargeStaffNo;
            } else if (aComponent.equals(chargeStaffNo)) {
                return staffNo;
            } else if (aComponent.equals(staffNo)) {
                if (moveButtonPanel1.isVisible()) {
                    return txtBarcode;
                } else {
                    if (productDivision.getSelectedComponent().equals(technicPanel)) {
                        return technicClass;
                    } else if (productDivision.getSelectedComponent().equals(itemPanel)) {
                        return itemClass;
                    } else if (productDivision.getSelectedComponent().equals(technicClamePanel)) {
                        return technicClameClass;
                    } else if (productDivision.getSelectedComponent().equals(itemReturnedPanel)) {
                        return itemReturnedClass;
                    } else if (productDivision.getSelectedComponent().equals(coursePanel)) {
                        return courseClass;
                    } else if (productDivision.getSelectedComponent().equals(consumCoursePanel)) {
                        if (consumptionCourseClass.getRowCount() > 0) {
                            return consumptionCourseClass;
                        } else {
                            return responseItemText1;
                        }
                    } else if (productDivision.getSelectedComponent().equals(prepaidPanel)) {
                        if (itemClass1.getRowCount() > 0) {
                            return itemClass1;
                        } else {
                            return responseItemText1;
                        }
                    }
                }

            } else if (aComponent.equals(txtBarcode)) {
                if (productDivision.getSelectedComponent().equals(technicPanel)) {
                    return technicClass;
                } else if (productDivision.getSelectedComponent().equals(itemPanel)) {
                    return itemClass;
                } else if (productDivision.getSelectedComponent().equals(technicClamePanel)) {
                    return technicClameClass;
                } else if (productDivision.getSelectedComponent().equals(itemReturnedPanel)) {
                    return itemReturnedClass;
                } else if (productDivision.getSelectedComponent().equals(coursePanel)) {
                    return courseClass;
                } else if (productDivision.getSelectedComponent().equals(consumCoursePanel)) {
                    if (consumptionCourseClass.getRowCount() > 0) {
                        return consumptionCourseClass;
                    } else {
                        return responseItemText1;
                    }
                } else if (productDivision.getSelectedComponent().equals(prepaidPanel)) {
                    if (itemClass1.getRowCount() > 0) {
                        return itemClass1;
                    } else {
                        return responseItemText1;
                    }
                }
            } else if (aComponent.equals(technicClass)) {
                return responseItemText1;
            } else if (aComponent.equals(itemClass)) {
                return responseItemText1;
            } else if (aComponent.equals(technicClameClass)) {
                return responseItemText1;
            } else if (aComponent.equals(itemReturnedClass)) {
                return responseItemText1;
            } else if (aComponent.equals(courseClass)) {
                return responseItemText1;
            } else if (aComponent.equals(consumptionCourseClass)) {
                return responseItemText1;
            } else if (aComponent.equals(itemClass1)) {
                return responseItemText1;
            } else if (aComponent.equals(responseItemText1)) {
                return responseItemComboBox1;
            } else if (aComponent.equals(responseItemComboBox1)) {
                return responseItemText2;
            } else if (aComponent.equals(responseItemText2)) {
                return responseItemComboBox2;
            } else if (aComponent.equals(responseItemComboBox2)) {
                if (SystemInfo.getDatabase().startsWith("pos_hair_tom") || SystemInfo.getDatabase().startsWith("pos_hair_origami") || SystemInfo.getDatabase().startsWith("pos_hair_charm")) {
                    //Tom, Origami
                    return txtfPioneerCode;
                } else {
                    return getFirstComp();
                }
            } else if (aComponent.equals(txtfPioneerCode)) {
                return getFirstComp();
            }
            //else if (aComponent.equals(payments)) {
            //    return accountRegistButton;
            //} else if (aComponent.equals(accountRegistButton)) {
            //  return getFirstComp();
            // }

            //return aComponent;
            return getFirstComp();
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
            if (aComponent.equals(customerNo)) {
                return accountRegistButton;
            } else if (aComponent.equals(chargeStaffNo)) {
                return customerNo;
            } else if (aComponent.equals(staffNo)) {
                if (canChangeKeys()) {
                    return chargeStaffNo;
                } else {
                    return accountRegistButton;
                }
            } else if (aComponent.equals(txtfPioneerCode)) {
                return staffNo;
            } else if (aComponent.equals(payments)) {
                return txtfPioneerCode;
            } else if (aComponent.equals(accountRegistButton)) {
                return payments;
            }

            return aComponent;
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
            return getFirstComp();
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
            return accountRegistButton;
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
            return getFirstComp();
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
            return getFirstComp();
        }
    }

    public boolean readBarcode(BarcodeEvent be) {
        if (SystemInfo.isGroup()) {
            return false;
        }
        if (be.getBarcode().equals("")) {
            return false;
        }

        this.setBarcodeProduct(be.getBarcode());

        return false;
    }

    private void loadReceiptSetting() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     print_receipt");
        sql.append(" from");
        sql.append("     mst_receipt_setting");
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getSelectedShop().getShopID()));

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

            if (rs.next()) {
                // デフォルト設定読み込み
                this.printReceiptFlsg = rs.getBoolean("print_receipt");
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void printReceipt() {
        //レシート出力しない時対応
        if (!this.printReceiptFlsg || ia.getStatus() == 2) {
            return;
        }

        PrintReceipt pr = new PrintReceipt();

        if (!pr.canPrint()) {
            return;
        }

        pr.setCustomer(ia.getSales().getCustomer());

        for (DataSalesDetail dsd : ia.getSales()) {
            if (dsd.getProductDivision() == 0) {
                continue;
            }

            pr.add(new ReceiptData(
                    dsd.getProductDivision(),
                    dsd.getProduct().getProductName(),
                    dsd.getProductNum(),
                    dsd.getProductValue(),
                    SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
                    true, true));
        }

        pr.setSubtotal(ia.getTotal(3).getValue() - ia.getTotal(4).getValue());
        pr.setTax(ia.getTotal(4).getValue());
        pr.setSumtotal(ia.getTotal(3).getValue());
//		pr.setDiscount(- ia.getSales().getDiscount().getDiscountValue());
        Long tmpDiscount = ((NameValue) ia.getTotal(1)).getValue() + ((NameValue) ia.getTotal(2)).getValue();
        pr.setDiscount(-tmpDiscount);
        pr.setOutOfValue(ia.getPaymentTotal());
        pr.setChangeValue(ia.getTotal(5).getValue());
        pr.setStaff(ia.getSales().getPayment(0).getStaff());

        pr.print();
    }

    private Long taxFilter(Long value) {
        Long result = value;

        if (ia.getAccountSetting().getDisplayPriceType() == 1) {
            result -= SystemInfo.getTax(result, salesDate.getDate());
        }

        return result;
    }

    /**
     * 主担当者、指名フラグを明細区分に表示する
     *
     * @param changeFlag true:主担当者反映 false:フラグを反映
     */
    public void setChargeStaffAndChargeNamed(boolean changeFlag) {

//                if( products.getRowCount()!= 0 && (JComboBox)products.getValueAt( 0 , 11 ) != null ){
//                        for (int i = 0 ; i < products.getRowCount() ; i++){
        if (products.getRowCount() != 0 && (JComboBox) products.getValueAt(0, staffCol) != null) {
            int productsRowCnt = 0;     //画面の明細行数
            int detailRowCnt = 0;	    //DataSalesDetailの行数
            for (DataSalesDetail dsd : ia.getSales()) {

                int col = designatedCol;
                if (dsd.getProductDivision() == 2) {
                    col = approachedCol;
                }

                for (int preIndex = 0; preIndex < dsd.size() + 1; preIndex++) {
                    if (changeFlag) {
                        //スタッフ用
                        if (0 == dsd.size() || (0 < dsd.size() && preIndex == 0)) {
                            // 売上明細のみ反映
                            JComboBox combo = (JComboBox) products.getValueAt(productsRowCnt, staffCol);

                            //combo.setSelectedIndex(chargeStaff.getSelectedIndex());
                            combo.setSelectedItem(chargeStaff.getSelectedItem());

                            products.setValueAt(combo, productsRowCnt, staffCol);
                            ia.getSales().get(detailRowCnt).setStaff((MstStaff) chargeStaff.getSelectedItem());
                        }
                    } else {
                        //指名フラグ用
                        //Luc start edit 20150721 #40804
                        //if (this.getSelectedShop().isDesignatedAssist()) {
                        if (this.getSelectedShop().isDesignatedAssist() && ia.getSales().getShop().getUseShopCategory() != 1) {
                        //Luc start edit 20150721 #40804
                            if (0 == dsd.size() || (0 < dsd.size() && preIndex == 0)) {
                                // 売上明細のみ反映
                                //nhanvt start edit 20141119 Exception
                                if(dsd.getProductDivision() == 1 || dsd.getProductDivision() == 5 || dsd.getProductDivision() == 6){
                                    JCheckBox chargeNameflag = (JCheckBox) products.getValueAt(productsRowCnt, col);
                                    chargeNameflag.setSelected(this.shimeiFreeFlag);
                                    products.setValueAt(chargeNameflag, productsRowCnt, col);
                                    ia.getSales().get(detailRowCnt).setDesignated(this.shimeiFreeFlag);
                                }else{
                                     ia.getSales().get(detailRowCnt).setDesignated(false);
                                }
                                //nhanvt end edit 20141119 Exception
                            }
                        }
                    }

                    if (this.getSelectedShop().isDisplayProportionally()) {
                        productsRowCnt++;
                    }
                }

                if (!this.getSelectedShop().isDisplayProportionally()) {
                    productsRowCnt++;
                }

                detailRowCnt++;
            }
        }
    }

    public Integer getProductsIndexForStaffAndChargeNamed(int selectedRow, int typeNo) {
        Integer retInteger = null;
        JComboBox combo;
        combo = (JComboBox) products.getValueAt(selectedRow, 0);
        if (combo.getItemAt(typeNo + 1) != null) {
            retInteger = (Integer) combo.getItemAt(typeNo + 1);
        }
         //nhanvt start add fix bug list okaike => Exception
        if(retInteger != null){
            if(retInteger == -1){
                retInteger = 0;
            }
        }
         //nhanvt end add fix bug list okaike => Exception
        return retInteger;
    }

    /**
     * 精算データをセットする
     *
     * @param index インデックス
     * @param row　明細区分の行
     */
    public void setMenuStaffAndChargeNamedData(int index, int row) {

        int col = designatedCol;
        if (ia.getSales().get(index).getProductDivision() == 2) {
            col = approachedCol;
        }

        if (ia.getSales().get(index).size() == 0) {

            ia.getSales().get(index).setStaff(
                    (MstStaff) ((JComboBox) products.getValueAt(row, staffCol)).getSelectedItem());
            if (products.getValueAt(row, col) instanceof JCheckBox) {
                ia.getSales().get(index).setDesignated(
                        ((JCheckBox) products.getValueAt(row, col)).isSelected());
            }

            //按分データ用
        } else {
            Integer dataNo = getProductsIndexForStaffAndChargeNamed(row, 1);

            //スタッフ用
            if (dataNo == null) {
                ia.getSales().get(index).setStaff(
                        (MstStaff) ((JComboBox) products.getValueAt(row, staffCol)).getSelectedItem());
            } else if (dataNo <= 0) {
                ia.getSales().get(index).get(dataNo).setStaff(
                        (MstStaff) ((JComboBox) products.getValueAt(row, staffCol)).getSelectedItem());
            }

            //指名フラグ用
            if (dataNo == null) {
                ia.getSales().get(index).setDesignated(
                        ((JCheckBox) products.getValueAt(row, col)).isSelected());
            } else if (dataNo <= 0) {
                ia.getSales().get(index).get(dataNo).setDesignated(
                        ((JCheckBox) products.getValueAt(row, col)).isSelected());
            }

        }

    }
    /**
     * TableHeaderRenderer
     */
    private BevelBorderHeaderRenderer tableHeaderRenderer = null;

    /**
     * テーブルヘッダーのレンダラーを取得する
     *
     * @return テーブルヘッダーのレンダラー
     */
    public BevelBorderHeaderRenderer getTableHeaderRenderer() {
        if (tableHeaderRenderer == null) {
            tableHeaderRenderer = new BevelBorderHeaderRenderer(
                    this.getTableColor());
        }
        return tableHeaderRenderer;
    }

    /**
     * 色の設定
     */
    public Color getTableColor() {
        return new Color(204, 204, 204);
    }

    /**
     * 有効な先頭項目を返します。
     */
    private Component getFirstComp() {
//        if (canChangeKeys()) {
//            return customerNo;
//        } else {
//            return staffNo;
//        }
        if (salesDate.isEnabled()) {
            return salesDate;
        }
        return customerNo;
    }

    private Component getTabSelected() {
//        if (canChangeKeys()) {
//            return customerNo;
//        } else {
//            return staffNo;
//        }
        if (salesDate.isEnabled()) {
            return salesDate;
        }
        return customerNo;
    }

    private void setPioneerName(Integer intPioneerCode) {
        cboexPioneerName.setSelectedIndex(0);

        for (int i = 1; i < cboexPioneerName.getItemCount(); ++i) {
            MstStaff ms = (MstStaff) cboexPioneerName.getItemAt(i);

            if (ms.getStaffID().equals(intPioneerCode)) {
                cboexPioneerName.setSelectedIndex(i);
            }
        }
    }

    public void setTargetList(ArrayList<com.geobeck.sosia.pos.data.account.DataSales> targetList) {
        this.targetList = targetList;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    private void moveSales(boolean isNext) {

        if (isNext) {
            currentIndex++;
        } else {
            currentIndex--;
        }

        ia.init();
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
        ia.getTaxs(salesDate.getDate());
        this.initDiscount();
        this.initPayments();
        this.initTotal();
        this.clear();
        SwingUtil.clearTable(products);
        ia.getSales().setSalesDate(salesDate.getDate());

        MstShop shop = targetList.get(currentIndex).getShop();
        Integer slipNo = targetList.get(currentIndex).getSlipNo();

        try {
            this.load(shop, slipNo);
            this.showData();
        } catch (Exception e) {
        }

    }

    private void setEnabledPrevNextButton() {
        prevButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < targetList.size() - 1);
        countLabel.setText((currentIndex + 1) + " / " + targetList.size());
    }

    private void showNowPoint(MstCustomer cus) {

        if (SystemInfo.isUsePointcard()) {
            // 現在ポイントを表示する
            Long NowPont = PointData.getNowPoint(cus.getCustomerID());
            if (NowPont == null) {
                genzaiPoint.setText("");
            } else {
                genzaiPoint.setText(String.valueOf(NowPont));
            }
        }

    }

    private void showDiscountPoint() {

        int row = products.getSelectedRow();
        int col = products.getSelectedColumn();

        SwingUtil.clearTable(point);

        DefaultTableModel model = (DefaultTableModel) point.getModel();

        pointIndexList.clear();

        for (int i = 0; i < products.getRowCount(); i++) {

            String s = ((JComboBox) products.getValueAt(i, 0)).getSelectedItem().toString();
            if (s.equals("技術") || s.equals("商品")) {
                products.changeSelection(i, col, false, false);

                pointIndexList.add(getProductsIndex(0));

                model.addRow(new Object[]{
                            products.getValueAt(i, 2),
                            0,
                            0
                        });
            }

        }

        products.changeSelection(row, col, false, false);
    }

    /**
     * 前回担当セット
     *
     * @param cus 顧客データ
     */
    private void setLastTimeStaff(MstCustomer cus) {

        if (chargeStaff.getSelectedIndex() > 0) {
            return;
        }

        DataSales lastSales = new DataSales(SystemInfo.getTypeID());

        ResultSetWrapper rs;

        try {

            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      ds.shop_id");
            sql.append("     ,ds.slip_no");
            sql.append(" from");
            sql.append("     data_sales as ds");
            sql.append(" where");
            sql.append("         ds.delete_date is null");
            sql.append("     and ds.sales_date is not null");
            sql.append("     and ds.customer_id = " + SQLUtil.convertForSQL(cus.getCustomerID()));
            sql.append(" order by");
            sql.append("      ds.sales_date desc");
            sql.append("     ,ds.slip_no desc");
            sql.append(" limit 1");

            //前回の伝票NOを取得
            rs = con.executeQuery(sql.toString());
            Integer lastShopID = null;
            Integer lastSlipNo = null;
            while (rs.next()) {
                lastShopID = rs.getInt("shop_id");
                lastSlipNo = rs.getInt("slip_no");
            }
            rs.close();

            //前回伝票データを取得
            lastSales = new DataSales(SystemInfo.getTypeID());
            if (lastShopID != null && lastSlipNo != null) {
                lastSales.clear();
                MstShop shop = new MstShop();
                shop.setShopID(lastShopID);
                //nhanvt start add 20150326 Bug #35729
                lastSales.setAccountSetting(ms);

                //nhanvt end add 20150326 Bug #35729
                lastSales.setShop(shop);
                lastSales.setSlipNo(lastSlipNo);
                lastSales.loadAll(con);
            }

            if (lastSales.getStaff() != null) {
                this.setChargeStaff(lastSales.getStaff().getStaffNo());
                this.changeShimeiFreeFlag(lastSales.getDesignated());
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void setBarcodeProduct(String barcode) {

        productDivision.setSelectedIndex(1);

        try {

            int itemClassId = 0;
            int itemId = 0;

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      item_class_id");
            sql.append("     ,item_id");
            sql.append(" from");
            sql.append("     mst_item");
            sql.append(" where");
            sql.append("     jan_code = " + SQLUtil.convertForSQL(barcode));
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            if (rs.next()) {
                itemClassId = rs.getInt("item_class_id");
                itemId = rs.getInt("item_id");
            }
            rs.close();

            for (int i = 0; i < itemClass.getRowCount(); i++) {
                ProductClass pc = (ProductClass) itemClass.getValueAt(i, 0);
                if (itemClassId == pc.getProductClassID()) {
                    itemClass.setRowSelectionInterval(i, i);
                    this.showProducts(itemClass, item);
                    break;
                }
            }

            for (int i = 0; i < item.getRowCount(); i++) {
                Product p = (Product) item.getValueAt(i, 0);
                if (p.getProductID().equals(itemId)) {
                    this.addSelectedProduct(2, p);
                    this.requestFocusToPayments();
                    break;
                }
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }

    private void initResponse() {
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            // start edit20130606 IVS
//            if (ia.getSales().getSlipNo() != null) {
//                mrs.setShopId(ia.getShop().getShopID());
//                mrs.setSlipNo(ia.getSales().getSlipNo());
//                // Start edit 20130426 nakhoa
//                mrs.load(con,true);
//                // End edit 20130426 nakhoa
//            } else {
//                mrs.setShopId(this.getSelectedShopID());
//                mrs.setSlipNo(null);
//                // Start edit 20130426 nakhoa
//                mrs.load(con,true);
//                // End edit 20130426 nakhoa
//            }
            mrs = SystemInfo.getResponses();
            //start edit20130606 IVS

            con.close();

            //Luc start edit 20130606
            //MstResponse mr = new MstResponse();
            //mr.setResponseID(-1);
            //mr.setResponseName("");
            //mr.setDisplaySeq(-1);
            //mrs.add(0, mr);

            MstResponse mr = new MstResponse();
            mr.setResponseID(-1);
            mr.setResponseName("");
            mr.setDisplaySeq(-1);
            responseItemComboBox1.removeAllItems();
            responseItemComboBox2.removeAllItems();
            responseItemComboBox1.addItem(mr);
            responseItemComboBox2.addItem(mr);
            //Luc end edit 20130606

            for (MstResponse r : mrs) {
                responseItemComboBox1.addItem(r);
                responseItemComboBox2.addItem(r);
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private CustomerDisplay getCustomerDisplay() {
        if (customerDisplay == null && SystemInfo.isUseCustomerDisplay()) {
            String comport = SystemInfo.getCustomerDisplayPort();
            if (comport != null && !comport.equals("")) {
                customerDisplay = CustomerDisplay.getInstance(comport);
            }
        }
        return customerDisplay;
    }

    private void showCustomerDisplayToSalesValue() {
        try {

            if (SystemInfo.isUseCustomerDisplay()) {
                CustomerDisplay cd = this.getCustomerDisplay();
                if (cd != null) {
                    cd.clearScreen();
                    cd.putStr("ｼｮｳｹｲ", 'l', 1);
                    cd.putStr(Long.toString(ia.getTotal(3).getValue()), 'r', 1);
                }
            }

        } catch (PortInUseException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // IVS SANG START INSERT 20131114 [gb]予約登録画面、お会計画面の制御調整

    private void clearProduct() {
        // IVS SANG START DELETE 20131118 [gb]予約登録画面、お会計画面の制御調整
        // slipNo.setText("＜新規＞");
        // ia.getSales().setNewAccount(true);
        // ia.getSales().setSlipNo(null);
        // ia.getSales().setIssuedContractNo(null);
        // this.initPayments();
        // requestFocusToPayments();
        // IVS SANG START DELETE 20131118 [gb]予約登録画面、お会計画面の制御調整
        ArrayList<Integer> deleteDataSales = new ArrayList<Integer>();
        int rowIndex = 0;
        for (DataSalesDetail dsd : ia.getSales()) {
            // IVS SANG START DELETE 20131118 [gb]予約登録画面、お会計画面の制御調整
            // if(dsd.getProductDivision() != null && dsd.getProductDivision() == 5) {
            // dsd.setDataContract(null);
            // }
            // dsd.setSlipNo(null);
            // dsd.setSlipDetailNo(null);
            // IVS SANG END DELETE 20131118 [gb]予約登録画面、お会計画面の制御調整
            if (dsd.getProductDivision() != null && dsd.getProductDivision() == 6) {
                deleteDataSales.add(rowIndex);
            }
            rowIndex++;
        }
        int indexDelete = 0;
        for (Integer index : deleteDataSales) {
            deleteProduct(index - indexDelete);
            indexDelete++;
        }
    }

    /**
     * 売上明細を１行削除する。
     *
     * @param index 削除する明細のインデックス
     * @param evt
     */
    private void deleteProduct(int row) {
        // int row = products.getSelectedRow();	// 選択行
        int index = this.getProductsIndex(0, row);    // 選択技術

        DataSalesDetail dsd = ia.getSales().get(index);
        int size = dsd.size();

        //if (products.getCellEditor() != null) {
        //    products.getCellEditor().stopCellEditing();
        //}

        ia.getSales().remove(index);

        int removeConsumptionCourse = 0;
        //コース契約前のコースを明細から削除する場合はコース消化リストからもコース情報を削除する
        if (dsd.getProductDivision() == 5 && dsd.getTmpContractNo() != null && dsd.getTmpContractNo().trim().length() > 0) {
            String tmpContractNo = dsd.getTmpContractNo();
            Map<String, ConsumptionCourse> courseMap = consumptionCourseMap.get(dsd.getCourse().getCourseClass().getCourseClassId());
            //消化コースリストから削除
            courseMap.remove(tmpContractNo);

            if (courseMap.isEmpty()) {
                //消化コースリストが空の場合はコース分類リストからコース分類を削除する
                Integer courseClassId = dsd.getCourse().getCourseClass().getCourseClassId();
                consumptionCouserClassMap.remove(courseClassId);

                //このコースのコース分類に契約済みコースが紐付いているかどうかチェック
                boolean hasConsumptionCourse = false;
                for (ConsumptionCourseClass consumptionCourseClass : consumptionCourseClasses) {
                    if (consumptionCourseClass.getSlipNo() != null
                            && courseClassId.equals(consumptionCourseClass.getCourseClassId())) {
                        //契約済コースが紐付いている場合
                        hasConsumptionCourse = true;
                        break;
                    }
                }

                if (!hasConsumptionCourse) {
                    //未契約コース、契約済みコースのどちらも紐付いていないコース分類がある
                    for (ConsumptionCourseClass consumptionCourseClass : consumptionCourseClasses) {
                        if (courseClassId.equals(consumptionCourseClass.getCourseClassId())) {
                            consumptionCourseClasses.remove(consumptionCourseClass);
                            break;
                        }
                    }
                }
            }

            //明細行に削除したコースに対する消化コースの明細があれば削除する
            for (int i = ia.getSales().size(); i > 0; i--) {
                DataSalesDetail d = ia.getSales().get(i - 1);
                if (d.getProductDivision() == 6
                        && tmpContractNo.equals(d.getTmpContractNo())) {
                    ia.getSales().remove(i - 1);
                    break;
                }
            }

            this.setConsumptionDataSales(consumptionCourseClasses, consumptionCourseClass);
            this.showConsumptionCourse(consumptionCourseClass, consumptionCourse);

            DefaultTableModel model = (DefaultTableModel) products.getModel();
            for (int i = model.getRowCount(); i > 0; i--) {
                Object o = model.getValueAt(i - 1, 2);
                if (o instanceof ConsumptionCourse) {
                    if (tmpContractNo.equals(((ConsumptionCourse) o).getTmpContractNo())) {
                        //仮契約番号が等しい場合はこの明細行を削除
                        model.removeRow(i - 1);
                        this.detailCount--;
                        removeConsumptionCourse++;
                    }
                }
            }
        }

        DefaultTableModel model = (DefaultTableModel) products.getModel();

        if (this.getSelectedShop().isDisplayProportionally()) {
            // 按分メニュー表示あり
            for (int i = 0; i < size + 1; i++) {
                model.removeRow(row);
            }
        } else {
            // 按分メニュー表示なし
            model.removeRow(row);
        }

        for (; row < products.getRowCount(); row++) {
            for (int i = 0; i < removeConsumptionCourse + 1; i++) {
                this.decTableTechIndex(row);
            }
        }

        this.detailCount--;

        // ポイント割引設定に反映
        if (SystemInfo.isUsePointcard()) {
            showDiscountPoint();
        }

        this.setTotal();
        this.showCustomerDisplayToSalesValue();
        //showPointPrepaid();
    }
    //add by ltthuc 20140610 Task #24575

    boolean hidePrepaTab() throws SQLException {
        boolean use_prepaid = SystemInfo.getSetteing().isUsePrepaid();
        boolean isCheckPrepaTab = true;
        boolean isDatabaseName = SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev");

        if (isDatabaseName == false || use_prepaid == false) {

            isCheckPrepaTab = false;
            if (firstCheckHideTab == false) {
                productDivision.removeTabAt(6);
                firstCheckHideTab = true;
            }
        }
        return isCheckPrepaTab;
    }
    //end add by ltthuc

//add by ltthuc 20140610 Task #24575
    private boolean checkChoosePrepaidAndInputMoney() {
        int size = arrPayMentMethodName.size();
        boolean bl = true;
        for (int i = 0; i < size; i++) {
            DataPaymentDetail dpd = arrPayMentMethodName.get(i);
            if (dpd.getPaymentMethod().getPrepaid() == 1 && dpd.getPaymentValue().longValue() == 0) {
                bl = false;
                break;
            }
            arrPayMentMethodName.contains(1);

        }
        if (arrayPrepaId.contains(1) == false) {
            bl = false;
        }

        return bl;

    }
    // end add ltthuc

    public static int getEditFlag() {
        return editFlag;
    }

    public static void setEditFlag(int Flag) {
        editFlag = Flag;
    }
    //add by ltthuc 2014/06/17

    void setEditFlagIs1(String s, JTable table, int row, int col) {
        if (s != null && edited == false && row != -1) {
            if (s.equals(table.getValueAt(row, col).toString()) == false) {
                if (edited == false) {
                    setEditFlag(1);
                    edited = true;
                }
            }

        }
    }
    //end add by ltthuc
    //IVS_LVTu start add 2015/06/26 New request #38256
    public void setMainStaff() {
        JComboBox cmb = new JComboBox();
        boolean flag = false;
        if (ia.getSales().getShop() != null) {
            if(ia.getSales().getShop().getUseShopCategory()!=null) {
                if (ia.getSales().getShop().getUseShopCategory() == 1) {
                    flag = true;
                    if ( tblReservationMainStaff.isVisible() == true ){
                        for ( int i = 0;i < tblReservationMainStaff.getRowCount();i ++) {
                            MstShopRelation msr = (MstShopRelation) tblReservationMainStaff.getValueAt(i, 4);
                            Object o = tblReservationMainStaff.getValueAt(i, 2);
                            for(int j = 0; j < products.getRowCount(); j ++) {
                                Object oProduct = products.getValueAt(j, 1);
                                if ( oProduct instanceof ProductClass ) {
                                   ProductClass product = (ProductClass) oProduct;
                                   if (product.getShopCategoryID() != null && product.getShopCategoryID().equals(msr.getShopCategoryId())) {
                                        if ( o instanceof JComboBox ) {
                                            cmb = (JComboBox) o;
                                            if ( cmb.getSelectedItem() instanceof MstStaff) {
                                                for(int k = 0; k < products.getColumnCount()-1; k ++) {
                                                    Object ob = products.getValueAt(j, k);
                                                    if ( ob instanceof JComboBox) {
                                                        JComboBox jcmb = (JComboBox) ob;
                                                        if ( jcmb.getSelectedItem() instanceof MstStaff ){
                                                            jcmb.setSelectedItem(cmb.getSelectedItem());
                                                            products.setValueAt(jcmb, j, k);
                                                            //Luc start add 20150909 #42432
                                                            products.setValueAt(((MstStaff)jcmb.getSelectedItem()).getStaffNo(), j, staffNoCol);
                                                            //Luc end add 20150909 #42432

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else if ( oProduct instanceof CourseClass) {
                                    CourseClass course = (CourseClass) oProduct;
                                   if (course.getShopCategoryID() != null && course.getShopCategoryID().equals(msr.getShopCategoryId())) {
                                        if ( o instanceof JComboBox ) {
                                            cmb = (JComboBox) o;
                                            if ( cmb.getSelectedItem() instanceof MstStaff) {
                                                for(int k = 0; k < products.getColumnCount()-1; k ++) {
                                                    Object ob = products.getValueAt(j, k);
                                                    if ( ob instanceof JComboBox) {
                                                        JComboBox jcmb = (JComboBox) ob;
                                                        if ( jcmb.getSelectedItem() instanceof MstStaff ){
                                                            jcmb.setSelectedItem(cmb.getSelectedItem());
                                                            products.setValueAt(jcmb, j, k);
                                                            //Luc start add 20150909 #42432
                                                            products.setValueAt(((MstStaff)jcmb.getSelectedItem()).getStaffNo(), j, staffNoCol);
                                                            //Luc end add 20150909 #42432
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if ( oProduct instanceof ConsumptionCourseClass) {
                                    ConsumptionCourseClass consumptionCourse = (ConsumptionCourseClass) oProduct;
                                   if (consumptionCourse.getShopCategoryID() != null && consumptionCourse.getShopCategoryID().equals(msr.getShopCategoryId())) {
                                        if ( o instanceof JComboBox ) {
                                            cmb = (JComboBox) o;
                                            if ( cmb.getSelectedItem() instanceof MstStaff) {
                                                for(int k = 0; k < products.getColumnCount()-1; k ++) {
                                                    Object ob = products.getValueAt(j, k);
                                                    if ( ob instanceof JComboBox) {
                                                        JComboBox jcmb = (JComboBox) ob;
                                                        if ( jcmb.getSelectedItem() instanceof MstStaff ){
                                                            jcmb.setSelectedItem(cmb.getSelectedItem());
                                                            products.setValueAt(jcmb, j, k);
                                                            //Luc start add 20150909 #42432
                                                            products.setValueAt(((MstStaff)jcmb.getSelectedItem()).getStaffNo(), j, staffNoCol);
                                                            //Luc start add 20150909 #42432
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    int rows = 0;
                    int proportionallyCount = 0;
                    //IVS_LVTu start edit 2015/09/15 Bug #42626
                    for (int k=0;k<products.getRowCount();k++) {
                        //按分の場合
                        if(((JComboBox)products.getValueAt(rows, 0)).getSelectedItem().equals("")) {
                                    proportionallyCount++;
                        } else {
                        Object obj = products.getValueAt(rows, staffCol);
                        if ( obj instanceof JComboBox) {
                            JComboBox combo = (JComboBox) obj;
                            ia.getSales().get(rows-proportionallyCount).setStaff((MstStaff) combo.getSelectedItem());
                        }
                        }
                       //IVS_LVTu end edit 2015/09/15 Bug #42626
                        rows ++;
                    }
                }
            }
        }
    }
    // GB Start del 20161108 Bug #58499
    //IVS_PTThu start edit 20160719 New request #52737
//    private int checkMethod() throws SQLException
//    {
//        int method = 0;
//        ResultSetWrapper rs;
//        ConnectionWrapper con = SystemInfo.getConnection();
//        StringBuilder sql = new StringBuilder(1000);
//        sql.append(" select count(payment_method_id) as Method ");
//        sql.append(" from mst_payment_method");
//        sql.append(" where delete_date is null");
//        rs = con.executeQuery(sql.toString());
//        while (rs.next()) {
//            method = rs.getInt("Method");
//        }
//        rs.close();
//        return method;
//    }
//    private int paymentclass() throws SQLException
//    {
//        int classID = 0;
//        ResultSetWrapper rs;
//        ConnectionWrapper con = SystemInfo.getConnection();
//        StringBuilder sql = new StringBuilder(1000);
//        sql.append(" select count(payment_class_id) as paymentclass");
//        sql.append(" from mst_payment_class");
//        sql.append(" where delete_date is null");
//        rs = con.executeQuery(sql.toString());
//        while (rs.next()) {
//            classID = rs.getInt("paymentclass");
//        }
//        rs.close();
//        return classID;
//    }
    //IVS_PTThu end edit 20160719 New request #52737
    // GB End del 20161108 Bug #58499
    //IVS_LVTu end add 2015/06/26 New request #38256
    private void setDesignate() {
        //int rows = 0;
        Boolean chck = false;
        int selectIndex = tblReservationMainStaff.getSelectedRow();
        if (ia.getSales().getShop() != null) {
            if(ia.getSales().getShop().getUseShopCategory()!=null) {
                if (ia.getSales().getShop().getUseShopCategory() == 1) {

                    if ( tblReservationMainStaff.isVisible() == true ){
                        Object oCheck = tblReservationMainStaff.getValueAt(selectIndex, 3);
                        if ( oCheck instanceof Boolean ) {
                            chck = (Boolean) oCheck;
                        }
                        JComboBox cmbStaff = (JComboBox)   tblReservationMainStaff.getValueAt(selectIndex, 2);
                        if((((MstStaff)cmbStaff.getSelectedItem()).getStaffID()!=null) || (((MstStaff)cmbStaff.getSelectedItem()).getStaffID()==null &&!chck) ) {
                            MstShopRelation msr = (MstShopRelation) tblReservationMainStaff.getValueAt(selectIndex, 4);

                            int proportionallyCount= 0;
                            for(int j = 0; j < products.getRowCount(); j ++) {
                                //按分の場合
                                if(((JComboBox)products.getValueAt(j, 0)).getSelectedItem().equals("")) {
                                    proportionallyCount++;
                                }
                                Object oProduct = products.getValueAt(j, 1);
                                if ( oProduct instanceof ProductClass ) {
                                   ProductClass product = (ProductClass) oProduct;
                                   if (product.getShopCategoryID() != null && product.getShopCategoryID().equals(msr.getShopCategoryId())) {
                                       //IVS_LVTu start edit 2015/09/15 Bug #42626
                                        //check ProductDivision = 1
                                       if( ia.getSales().get(j-proportionallyCount).getProductDivision() == 1 ) {
                                            JCheckBox jcheck = (JCheckBox)products.getValueAt(j, staffCol+1);
                                            //products.setValueAt(chck, j, staffCol+1);
                                            jcheck.setSelected(chck);
                                            products.setValueAt(jcheck, j, staffCol+1);
                                            //Luc start edit 20150914 #42628
                                            //ia.getSales().get(j).setDesignated(chck);
                                            ia.getSales().get(j-proportionallyCount).setDesignated(chck);
                                            //Luc start edit 20150914 #42628
                                       }

                                   }
                                }
                                if ( oProduct instanceof CourseClass ) {
                                  CourseClass course = (CourseClass) oProduct;
                                   if (course.getShopCategoryID() != null && course.getShopCategoryID().equals(msr.getShopCategoryId())) {
                                       JCheckBox jcheck = (JCheckBox)products.getValueAt(j, staffCol+1);
                                       //products.setValueAt(chck, j, staffCol+1);
                                       jcheck.setSelected(chck);
                                       products.setValueAt(jcheck, j, staffCol+1);
                                      //Luc start edit 20150914 #42628
                                       //ia.getSales().get(j).setDesignated(chck);
                                       ia.getSales().get(j-proportionallyCount).setDesignated(chck);
                                       //Luc start edit 20150914 #42628

                                   }
                                }
                                if ( oProduct instanceof ConsumptionCourseClass ) {
                                    ConsumptionCourseClass consumptionCourse = (ConsumptionCourseClass) oProduct;
                                   if (consumptionCourse.getShopCategoryID() != null && consumptionCourse.getShopCategoryID().equals(msr.getShopCategoryId())) {
                                       JCheckBox jcheck = (JCheckBox)products.getValueAt(j, staffCol+1);
                                       //products.setValueAt(chck, j, staffCol+1);
                                       jcheck.setSelected(chck);
                                       products.setValueAt(jcheck, j, staffCol+1);
                                       //IVS_LVTu end edit 2015/09/15 Bug #42626
                                      //Luc start edit 20150914 #42628
                                       //ia.getSales().get(j).setDesignated(chck);
                                       ia.getSales().get(j-proportionallyCount).setDesignated(chck);
                                       //Luc start edit 20150914 #42628
            }
        }
    }
}
    }
}
            }
        }
    }

    public class SecondFilter extends DocumentFilter {

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {

            if (text != null) {
                Document doc = fb.getDocument();
                int currentLength = doc.getLength();
                String currentContent = doc.getText(0, currentLength);
                String before = currentContent.substring(0, offset);
                String after = currentContent.substring(length + offset, currentLength);

                int beforeLen = before.getBytes().length;
                int afterLen = after.getBytes().length;

                //桁数チェック
                if ( 2 <= beforeLen + afterLen ) {
                    text = "";
                }

                String value = before + text + after;

                //入力値が数値の場合は数値範囲チェック
                if (CheckUtil.isNumber(value)) {
                    int intValue = Integer.parseInt(value);
                    if (!CheckUtil.checkRange(intValue, 0, 59)) {
                        text = "";
                    }
                } else {
                    text = "";
                }
            }

            super.replace( fb, offset, length, text, attrs );
        }
    }
    //IVS_LVTU start add 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
    /**
     * editor table products
     */
    private class HairInputAccountCellEditor extends DefaultCellEditor {
		JTextField textField = new JTextField();

		public HairInputAccountCellEditor(JTextField field) {
			super(field);
			this.textField = field;
			textField.setHorizontalAlignment(JTextField.RIGHT);
			((PlainDocument)textField.getDocument()).setDocumentFilter(
				new HairInputAccountFilter(9, CustomFilter.NUMERIC));

			textField.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					textField.selectAll();
				}
			});
		}

        @Override
		public Component getTableCellEditorComponent(JTable table, Object value,
						 boolean isSelected, int row, int column) {
			// TextFieldに内容がある場合は、そのままとする
			if (value != null) {
				textField.setText(value.toString());
			} else {
				textField.setText("");
			}

			textField.selectAll();

			return textField;
		}

        @Override
		public Double getCellEditorValue() {
			if (textField.getText().equals("") || textField.getText().equals("."))
				return	0d;
			else
				return	Double.parseDouble(textField.getText());
		}
	}

    /**
     * filter input data table products
     */
	private class HairInputAccountFilter extends CustomFilter {
        /**
         * validation for numbers after dot
         */
        private static final char   DOT         = '.';
        /**
         * number after dot
         */
        private static final int    SCALE       = 2;
        /**
         * limit number
         */
        private int                 limitOld    = 9;

        public HairInputAccountFilter (int limit, String validValues) {
            super(limit, validValues);
            limitOld = limit;
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            //挿入するテキストがnullの場合、処理を抜ける
            if (string == null) {
                super.replace(fb, offset, length, string, attr);
                return;
            }

            Document doc = fb.getDocument();
            int totalLen = doc.getLength();
            // index at dot
            int indexDot = doc.getText(0, totalLen).indexOf(DOT);
            if (indexDot >= 0) {
                indexDot ++;
                limit = indexDot + SCALE;
            } else {
                limit = limitOld;
            }

            //入力可能な文字以外を削除する
            string = this.checkString(string);

            int len = string.length();

            //最大文字数が0の場合
            if (limit == 0) {
                super.replace(fb, offset, length, string, attr);
            } else if (limit <= totalLen - length) {
            	if (0 <= limit - totalLen + length) {
                	string = string.substring(0, limit - totalLen + length);
                } else {
                    string = "";
                }
                super.replace(fb, offset, length, string, attr);
            } else if (limit <= (offset + len)) {
                string = string.substring(0, limit - totalLen + length);
                super.replace(fb, offset, length, string, attr);
            } else if (limit <= (offset + len + totalLen - offset - length)) {
                string = string.substring(0, limit - totalLen + length);
                super.replace(fb, offset, length, string, attr);
            } else {
                super.replace(fb, offset, length, string, attr);
            }
        }

	     /**
		 * 入力可能な文字かチェックする。
		 * @param string チェックする文字
		 * @return 入力可能な文字のみ
		 */
		private String checkString(String string) {
			if (validValues.equals("")) return string;

			StringBuffer	sb	=	new StringBuffer();

			for (int i = 0; i < string.length(); i ++) {
				char ch	= string.charAt(i);

				if (Character.toString(ch).matches("[" + this.getValidValues() + "]*"))
					sb.append(ch);
			}

			return	sb.toString();
		}
    }
//IVS_LVTU end add 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備

    // 20170308 GB Start Add #59940　[gb]精算済みの伝票をお会計画面で表示時、支払方法が空欄になる。
    /**
     * 支払区分に対応した支払方法に変更
     *
     * @param row     変更する行番号
     * @param classID 支払区分
     */
    public void setPaymentMethodCombo(Integer row, Integer classID) {

        if (row < 4) {
            return;
        }

        MstPaymentClass mpc = null;
        Object methods      = null;


        // 支払区分取得
        mpc = ia.getPaymentClasses().get(classID);

        // 支払方法オブジェクト取得
        methods = this.getPaymentMethodObject(mpc);

        DefaultTableModel model = (DefaultTableModel) payments.getModel();

        model.setValueAt(methods, row, 1);
    }
    // 20170308 GB End Add #59940
    
    /**
     * コース消化の場合
     */
    private long getConsumptionDisplayValue(final int index, final int row) {
        long priceDisplay;
        long productValue;
        if(SystemInfo.getAccountSetting().getDiscountType() == 0 ) {
            productValue =  ia.getSales().get(index).getProductValue() - ia.getSales().get(index).getDiscountValue();
        }else {
            Double value = Math.floor(ia.getSales().get(index).getDiscountValue() + (ia.getSales().get(index).getDiscountValue() * ia.getTaxRate()));
            productValue =  ia.getSales().get(index).getProductValue() - value.longValue() ;
        }
        priceDisplay = new BigDecimal(productValue).divide(new BigDecimal(Double.valueOf(products.getValueAt(row, 4).toString())), 0, RoundingMode.UP).longValue();
        
        return priceDisplay;
    }
    
    /**
     * 列変数の設定
     */
    private void settingProductColumn() {
        
        if (this.getSelectedShop().isProportionally() && this.getSelectedShop().isDisplayProportionally()) {
            //按分有の場合
            pointCol = 9;       //ポイント
            rateCol = 10;       //割合
            taxCol = 12;     // 税率
            staffCol = 14;      //担当者
            designatedCol = 15; //指名
            approachedCol = 16; //アプローチ
            if (this.getSelectedShop().isBed()) {
                //施術台有の場合
                pickupCol = 17;
                bedCol = 18; //施術台

            } else {
                //施術台無の場合
                pickupCol = -1;
                bedCol = -1; //施術台
            }
            //IVS_LVTU start edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
            restNumCol = 11;
            //IVS_LVTU end edit 2017/08/09 #14354 [gb]お会計画面　消化の残回数の計算不備
        } else {
            //按分無の場合
            pointCol = -1;      //ポイント
            rateCol = -1;       //割合
            taxCol = 9;        // 税率
            staffCol = 11;       //担当者
            designatedCol = 12;  //指名
            approachedCol = 13; //アプローチ
            if (this.getSelectedShop().isBed()) {
                //施術台有の場合
                pickupCol = 14;
                bedCol = 15; //施術台
            } else {
                //施術台無の場合
                pickupCol = -1;
                bedCol = -1; //施術台
            }
             //Luc start add 20160219 #48422
            restNumCol = 8;
            //Luc end add 20160219 #48422
        }
        //Luc start add 20150907 #42432
        staffNoCol = staffCol - 1;
        //Luc end add 20150907 #42432
    }
    
    /**
     * change tax by sales date
     */
    private void changeTaxBySalesDate() {
        
        MstTaxs taxList = ia.getTaxs(salesDate.getDate());
        DefaultTableModel model = (DefaultTableModel) products.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object o = products.getValueAt(i, taxCol);
            if (o instanceof JComboBox) {
                JComboBox cmbTax = (JComboBox) o;
                cmbTax.removeAllItems();
                
                for (MstTax tax : taxList) {
                    cmbTax.addItem(tax);
                }
            }
            this.changeProductsByTax(i);
        }
        setTotal();
    }
    
    /**
     * change products by tax
     * @param row 
     */
    private void changeProductsByTax(int row) {
        if (!(products.getValueAt(row, taxCol) instanceof JComboBox)) {
            return;
        }
        
        int index = getProductsIndex(0, row);
        //税率
        if (getProductsIndex(1) == null) {

            if (ia.getSales().get(index).getProductDivision() == 5) {
                if (!ia.getSales().get(index).isEditable()) {
                    JComboBox taxCombobox = (JComboBox) products.getValueAt(row, taxCol);
                    taxCombobox.setSelectedIndex(ia.getIndexByTaxRate(ia.getSales().get(index).getTaxRate()));
                    editDataContract = true;
                    return;
                }
            }

            JComboBox taxCombobox = (JComboBox) products.getValueAt(row, taxCol);
            if (taxCombobox == null || taxCombobox.getSelectedItem() == null) {
                return;
            }
            Double taxeRate = ((MstTax) taxCombobox.getSelectedItem()).getTaxRate();

            ia.getSales().get(index).setTaxRate(taxeRate);
            Long price = Double.valueOf(products.getValueAt(row, 5).toString()).longValue();
            if (ia.getAccountSetting().getDisplayPriceType() == 1) {
                long signum = (long) Math.signum(price);
                if (signum == 0) {
                    signum = 1;
                }
                Double a = signum * (new Double(Math.floor(Math.abs(price) + (Math.abs(price) * ia.getSales().get(index).getTaxRate()))));
                ia.getSales().get(index).setProductValue(a.longValue());
            } else {
                ia.getSales().get(index).setProductValue(price);
            }
            changeDataConsumptionCourse(row, taxCol);
        }
        if (getProductsIndex(1, row) == null) {
            if (ia.getSales().get(index).getProductDivision() == 6) {
                //コース消化の場合
                products.setValueAt(ia.getAccountSetting().getDisplayValue(
                        ia.getSales().get(index).getValueForConsumption(),
                        ia.getSales().get(index).getDiscountValue(), ia.getTaxRate()), row, 7);
            } else {
                 products.setValueAt(ia.getAccountSetting().getDisplayValue(
                        ia.getSales().get(index).getValue(),ia.getSales().get(index).getProductNum().longValue(),
                        ia.getSales().get(index).getDiscountValue(), ia.getSales().get(index).getTaxRate()), row, 7);
            }

        }
    }
    
    /**
     * check apply date of tax
     * @return 
     */
    private boolean isChangeApplyDate() {
        try {
            MstTax taxNew = new MstTax();
            taxNew.getTaxBySaleDate(SystemInfo.getConnection(), salesDate.getDate());
            MstTax taxOld = ia.getTaxs(ia.getSales().getSalesDate()).get(0);
            if (!(taxNew.getApplyDate() != null && taxNew.getApplyDate().compareTo(taxOld.getApplyDate()) == 0)) {
                
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(HairInputAccountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * get value for 税率 column
     * @param dsd
     * @return 
     */
    private Object getTaxByProductDivision(DataSalesDetail dsd) {
        
        if (dsd.getProductDivision() == 7 || dsd.getProductDivision() == 8 || dsd.getProductDivision() == 9) {
            MstTax tax = new MstTax();
            tax.setTaxRate(dsd.getTaxRate());
            
            JLabel label = new JLabel(tax.toString(), SwingConstants.CENTER);
            
            return label;
        } else {
            return getTaxComboBox(ia.getIndexByTaxRate(dsd.getTaxRate()));
        }
    } 
}

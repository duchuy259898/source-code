/*
 * HairInputAccountDetailDialog.java
 *
 * Created on 2006/04/20, 14:13
 */
package com.geobeck.sosia.pos.hair.account;

import com.geobeck.customerDisplay.CustomerDisplay;
import com.geobeck.sosia.pos.hair.pointcard.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.text.SimpleDateFormat;

import com.geobeck.sosia.pos.data.account.DataPaymentDetail;
import com.geobeck.sosia.pos.hair.data.account.*;
import com.geobeck.sosia.pos.account.*;
import com.geobeck.sosia.pos.hair.account.api.RegistPaymentResultWSI;
import com.geobeck.sosia.pos.hair.account.api.RegistPaymentResultsWSI;
import com.geobeck.sosia.pos.hair.account.api.RegistPaymentWSI;
import com.geobeck.sosia.pos.hair.data.course.DataContractDigestion;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.hair.report.util.JPOIApiSaleTransittion;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.hair.reservation.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.master.customer.MstDataDeliveryProduct;
import com.google.gson.Gson;
import connectispotapi.ConnectIspotApi;
import java.util.ArrayList;
import java.util.List;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

/**
 * @author katagiri
 */
public class HairInputAccountDetailDialog extends javax.swing.JDialog {

    /**
     * 店舗ＩＤ
     */
    private Integer shopID = null;
    /**
     * 選択された顧客ＩＤ
     */
    private String selectedID = "";
    private HairInputAccount ia = null;
    private boolean printReceiptFlsg = false;
    private boolean printNextInfoFlsg = true;
    private java.util.Date nextReserveDate = null;
    private String nextReserveMenu = "";
    private ReservationTimeTablePanel rtt = null;
    /**
     * 予約登録パネル
     */
    private int praiseDay = -1; // 賞美期限の日数
    private MstReceiptSetting receiptSetting = null;
    // 閉じるボタンを押したかどうか
    private boolean isClose = false;
    private PointData point = null;
    private long discountUsePoint = 0l;
    private CustomerDisplay customerDisplay = null;
    private boolean isNewAccount = false;
    private int flagDisgtion = 0;
    //
    private String comingId = "";
    private Integer payment = 0;
    //nahoang Add start 20140609
    private Long sum = 0L;
    private Long sumOfSaleValue = 0L;
    private Long sumOfUseValue = 0L;
    private Long totalValue = 0L;
    private PrepaidData prepaidData;
    public static final String DATABASE_NAME = "pos_hair_missionf";

    //nahoang Add end 20140609
    /**
     * コンストラクタ
     *
     * @param parent
     * @param modal
     */
    public HairInputAccountDetailDialog(java.awt.Frame parent, boolean modal, HairInputAccount ia, long discountUsePoint) {
        super(parent, modal);
        initComponents();

        SwingUtil.setJTableHeaderRenderer(products, SystemInfo.getTableHeaderRenderer());

        this.addMouseCursorChange();

        this.setSize(720, 600);

        this.ia = ia;

        SwingUtil.moveCenter(this);

        this.initTableColumn();
        this.addProducts();

        this.setInfomationDate();
        this.loadNextReserve();

        loadReceiptSetting();

        this.changeMessage();

        nextVisitEnable(true);
        nextReserveEnable(false);
        jLabel2.setText("～");

        memo.setText(ia.getSales().getVisitedMemo());

        this.setDiscountUsePoint(discountUsePoint);

        // ポイントボタン
        if (!(SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 0 || SystemInfo.getPointOutputType() == 2))) {
            printCard.setVisible(false);
        }

        // ポイント情報
        if ((SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 1 || SystemInfo.getPointOutputType() == 2))) {

            displayPointInfo(ia, discountUsePoint);

        } else {
            pointPanel.setVisible(false);
        }

        // プリペイドボタン
        prepaidCard.setVisible(SystemInfo.getSetteing().isUsePrepaid());

        // TODO
        if (SystemInfo.getDatabase().startsWith("pos_hair_ox2") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
            // キャッシュバックボタン 表示
            cashbackButton.setVisible(true);
        } else {
            // キャッシュバックボタン 非表示
            cashbackButton.setVisible(false);
        }
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak") || SystemInfo.getDatabase().startsWith("pos_hair_rizap")) {
            // キャッシュバックボタン 表示
            registButton3.setVisible(true);
        } else {
            // キャッシュバックボタン 非表示
            registButton3.setVisible(false);
        }
        //Start add 20131014 lvut  HPB PPOS
        String sqlCheck = "select coming_id, payment_value from data_reservation " + "\n"
                + "where shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()) + "\n"
                + "and reservation_no = " + SQLUtil.convertForSQL(ia.reservationNo) + "\n";
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            ResultSetWrapper rs = con.executeQuery(sqlCheck);
            if (rs.next()) {
                comingId = rs.getString("coming_id");
                payment = rs.getInt("payment_value");
            }

        } catch (Exception ex) {
        }
        if (comingId == null) {
            hpbPanel.setVisible(false);
        } else {
            txtTotalAmount.setText(FormatUtil.decimalFormat(((NameValue) ia.getTotal(3)).getValue()));
            txtAtShopTotalAmout.setText(FormatUtil.decimalFormat(payment));
        }
        //End add 20131014 lvut HPB PPOS
        //HPB
        hpbPanel.setVisible(false);
        this.setSize(720, 490);
//        if (pointPanel.isVisible()) {
//            this.setSize(960, 520);
//            message.setSize(4, 22);
//
//        }
        if (SystemInfo.getPossSalonId() != null && !SystemInfo.getPossSalonId().equals("")
                && comingId != null && !comingId.equals("")) {
            hpbPanel.setVisible(true);
            this.setSize(720, 600);
        }

        //nahoang Add start 20140610
        prepaidPanel.setVisible(false);
        MstCustomer customer = new MstCustomer();
        txtSalesValue.setText(String.valueOf(sumOfSaleValue));
        txtUseValue.setText(String.valueOf(sumOfUseValue));

        if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals(DATABASE_NAME) || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
            prepaidCard.setVisible(false);
            prepaidPanel.setVisible(true);
            displayPointPrepaid(ia, customer);
            if (pointPanel.isVisible()) {
                this.setSize(890, 490);
            }

        }
        //nahoang Add end

    }

    //nahoang Add start 20140609
    private void updatePrepaidChange() {
        Long NowPoint = 0L;

        if (txtSalesValue.getText().equals("")) {
            txtSalesValue.setText("0");
        }

        if (txtUseValue.getText().equals("")) {
            txtUseValue.setText("0");
        }

        NowPoint = Long.valueOf(txtPrevRemainValue.getText());
        sumOfUseValue = Long.valueOf(txtUseValue.getText());
        sumOfSaleValue = Long.valueOf(txtSalesValue.getText());

        totalValue = NowPoint + sumOfSaleValue - sumOfUseValue;
        txtNowRemainValue.setText(String.valueOf(totalValue));

    }

    private void displayPointPrepaid(HairInputAccount ia, MstCustomer customer) {
        //コネクションを取得
        ConnectionWrapper con = SystemInfo.getConnection();
        Long sumSaleValues = 0L;
        Long sumUseValues = 0L;
        Long sumRemainValue = 0L;
        boolean blnFlag = false;

        String sql = "select sum(sales_value) as sumSales, sum(use_value) as sumUse from data_prepaid where customer_id  = "
                + SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID()) + " and delete_date IS NULL and slip_no <> " + ia.getSales().getSlipNo() + " group by customer_id ";

        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                sumSaleValues = rs.getLong("sumSales");
                sumUseValues = rs.getLong("sumUse");
            }
            sumRemainValue = sumSaleValues - sumUseValues;
            prepaidData = new PrepaidData();
            blnFlag = prepaidData.checkDataPrepaid(con, ia.getSales().getSlipNo(), ia.getShop().getShopID());

            con.close();
            if ((blnFlag == true) && (HairInputAccountPanel.getEditFlag() == 0)) {
                sumOfUseValue = prepaidData.getUseValue();
                sumOfSaleValue = prepaidData.getSalesValue();

                totalValue = sumRemainValue + sumOfSaleValue - sumOfUseValue;
            } else {
                for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {

                    if (dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPrepaid() != null) {
                        Integer id = dpd.getPaymentMethod().getPrepaid();

                        if (id.equals(1)) {
                            sumOfUseValue += dpd.getPaymentValue().longValue();
                        }
                    }
                }

                for (DataSalesDetail dsd : ia.getSales()) {
                    Integer id = dsd.getProduct().getProductClass().getPrepa_class_id();
                    if (id != null && id.equals(1)) {
                        sumOfSaleValue += dsd.getProduct().getPrepaid_price().longValue() * dsd.getProductNum().intValue();
                    }
                }
                totalValue = sumRemainValue + sumOfSaleValue - sumOfUseValue;
            }
        } catch (Exception e) {
            System.err.println("SQL = " + sql);
            e.printStackTrace();
        }

        txtPrevRemainValue.setText(String.valueOf(sumRemainValue));
        txtSalesValue.setText(String.valueOf(sumOfSaleValue));
        txtUseValue.setText(String.valueOf(sumOfUseValue));
        txtNowRemainValue.setText(String.valueOf(totalValue));
    }
    //nahoang Add end 20140609

    private void displayPointInfo(HairInputAccount ia, long discountUsePoint) {
        point = PointData.getPointData(ia.getShop().getShopID(), ia.getSales().getSlipNo());
        if (point == null) {
            long newPoint = CalculatePoint.calcPoint(ia.getSales());
            txtAddPoint.setText(String.valueOf(newPoint));
            txtUsePoint.setText(String.valueOf(discountUsePoint));
        } else {
            txtAddPoint.setText(String.valueOf(point.getSuppliedPoint()));
            txtUsePoint.setText(String.valueOf(point.getUsePoint() + discountUsePoint));
        }
        Long NowPoint = PointData.getNowPoint(ia.getSales().getCustomer().getCustomerID());
        // 現在ポイントを表示する
        if (NowPoint == null) {
            txtPrevPoint.setText("");
        } else {
            point = PointData.getPointData(ia.getShop().getShopID(), ia.getSales().getSlipNo());
            if (point != null) {
                // 伝票登録済みデータの場合は、その時の発行ポイントをのぞいたものを計算
                NowPoint -= (point.getSuppliedPoint() - point.getUsePoint());
            }
            txtPrevPoint.setText(String.valueOf(NowPoint));
        }
        updatePreviewPoint();
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(printCard);
        SystemInfo.addMouseCursorChange(prepaidCard);
        SystemInfo.addMouseCursorChange(backButton2);
        SystemInfo.addMouseCursorChange(receiptButton);
        SystemInfo.addMouseCursorChange(registButton2);
        SystemInfo.addMouseCursorChange(reserveButton);
        SystemInfo.addMouseCursorChange(cashbackButton);
    }

    private void loadReceiptSetting() {
        receiptSetting = new MstReceiptSetting();

        receiptSetting.setShop(ia.getShop());

        try {
            receiptSetting.load(SystemInfo.getConnection());
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        //printReceipt.setSelected(receiptSetting.isPrintReceipt());
        //ATGS デフォルト設定読み込み
        this.printReceiptFlsg = receiptSetting.isPrintReceipt();
        if (this.printReceiptFlsg) {//起動時に印刷するだった場合
            receiptPrint.setIcon(SystemInfo.getImageIcon("/button/account/no-receipt_off.png"));
        } else {
            receiptPrint.setIcon(SystemInfo.getImageIcon("/button/account/no-receipt_on.png"));
        }

        this.printNextInfoFlsg = receiptSetting.isPrintNextInfo();
    }

    private void addProducts() {

        DefaultTableModel model = (DefaultTableModel) products.getModel();

        int tmp = Integer.MAX_VALUE;

        for (DataSalesDetail dsd : ia.getSales()) {

            // クレーム・返品時はやらない
            if (dsd.getProductDivision() == 1) {

                // さらに短い期間があればそれにする
                if (dsd.getProduct().isPraiseTime() && tmp > dsd.getProduct().getPraiseTimeLimit()) {
                    tmp = dsd.getProduct().getPraiseTimeLimit();
                    praiseDay = tmp;
                }

                model.addRow(new Object[]{
                            dsd.getProductName(),
                            (dsd.getProduct().isPraiseTime() ? dsd.getProduct().getPraiseTimeLimit() : "")
                        });
            }
        }

        java.util.GregorianCalendar limit = new java.util.GregorianCalendar();
        limit.setTime(ia.getSales().getSalesDate());
        if (praiseDay > 0) {
            limit.add(Calendar.DAY_OF_MONTH, praiseDay);
            limitDate.setDate(limit.getTime());
        } else {
        }

    }

    private void initTableColumn() {
        //列の幅を設定する。
        products.getColumnModel().getColumn(0).setPreferredWidth(120);
        products.getColumnModel().getColumn(1).setPreferredWidth(25);

    }

    private void changeMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd(E)");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd(E) kk:mm");

        String m = "";

        if (this.printNextInfoFlsg) {
            if (this.rdoNextReserveInfo.isSelected()) {
                m = "<次回予約案内>\n"
                        + (nextReserveDate == null ? "" : sdf2.format(nextReserveDate) + "～ ")
                        + (nextReserveMenu.length() > 0 ? ("\n" + nextReserveMenu) : "") + "\n\n";
            } else {
                m = "<次回来店案内期間>\n"
                        + sdf.format(startDate.getDate()) + "～" + sdf.format(endDate.getDate()) + "\n\n";
            }
        }

        //レシート設定
        PrintReceipt pr = new PrintReceipt();

        m += pr.getReceiptSetting().getMessage();

        message.setText(m);
        message.setCaretPosition(0);
    }

    /**
     * 選択された顧客ＩＤを取得する。
     *
     * @return 選択された顧客ＩＤ
     */
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    /**
     * 選択された顧客ＩＤを取得する。
     *
     * @return 選択された顧客ＩＤ
     */
    public String getSelectedID() {
        return selectedID;
    }

    /**
     * 選択された顧客ＩＤをセットする。
     *
     * @param selectedID 選択された顧客ＩＤ
     */
    public void setSelectedID(String selectedID) {
        this.selectedID = selectedID;
    }

    public boolean isNewAccount() {
        return isNewAccount;
    }

    public void setNewAccount(boolean isNewAccount) {
        this.isNewAccount = isNewAccount;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        memo = new com.geobeck.swing.JTextAreaEx();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        products = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        limitDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        startDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        endDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rdoNextVisitInfo = new javax.swing.JRadioButton();
        rdoNextReserveInfo = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        reserveButton = new javax.swing.JButton();
        lblNextReserve = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        message = new com.geobeck.swing.JTextAreaEx();
        pointPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtPrevPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        txtAddPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtAddPoint.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        txtUsePoint = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtUsePoint.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        txtTotalPoint = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cashbackButton = new javax.swing.JButton();
        hpbPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtTotalAmount = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel26 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAtShopTotalAmout = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel27 = new javax.swing.JLabel();
        checkNext = new javax.swing.JCheckBox();
        hpbPanel1 = new javax.swing.JPanel();
        registButton2 = new javax.swing.JButton();
        chkPrintCopy = new javax.swing.JCheckBox();
        registButton3 = new javax.swing.JButton();
        receiptPrint = new javax.swing.JButton();
        chkPrintCustomer = new javax.swing.JCheckBox();
        receiptButton = new javax.swing.JButton();
        prepaidPanel = new javax.swing.JPanel();
        lblPrevRemainValueLabel = new javax.swing.JLabel();
        lblSalesValueLabel = new javax.swing.JLabel();
        lblUseValueLabel = new javax.swing.JLabel();
        lblNowRemainValueLabel = new javax.swing.JLabel();
        txtPrevRemainValue = new com.geobeck.swing.JFormattedTextFieldEx();
        txtSalesValue = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtSalesValue.getDocument()).setDocumentFilter(
            new CustomFilter(9, CustomFilter.NUMBER));
        txtUseValue = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtUseValue.getDocument()).setDocumentFilter(
            new CustomFilter(9, CustomFilter.NUMBER));
        txtNowRemainValue = new com.geobeck.swing.JFormattedTextFieldEx();
        lblYen1 = new javax.swing.JLabel();
        lblYen2 = new javax.swing.JLabel();
        lblYen3 = new javax.swing.JLabel();
        lblYen4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        prepaidCard = new javax.swing.JButton();
        backButton2 = new javax.swing.JButton();
        printCard = new javax.swing.JButton();
        rdNoReciptPrinting = new javax.swing.JRadioButton();

        setTitle("退店画面");
        setName("searchCustomerFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "来店メモ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION)));

        memo.setColumns(20);
        memo.setRows(5);
        memo.setInputKanji(true);
        jScrollPane1.setViewportView(memo);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "次回来店案内", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION)));

        products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "技術名", "日数"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(products);

        jLabel1.setText("賞美期限");

        limitDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        limitDate.setDate(new java.util.Date());
        limitDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                limitDateItemStateChanged(evt);
            }
        });
        limitDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                limitDateFocusGained(evt);
            }
        });

        startDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        startDate.setDate(new java.util.Date());
        startDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startDateItemStateChanged(evt);
            }
        });
        startDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                startDateFocusGained(evt);
            }
        });

        endDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        endDate.setDate(new java.util.Date());
        endDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                endDateItemStateChanged(evt);
            }
        });
        endDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                endDateFocusGained(evt);
            }
        });

        jLabel3.setText("次回来店案内期間");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(limitDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(jPanel2Layout.createSequentialGroup()
                .add(startDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(endDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(limitDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .add(endDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(startDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .add(68, 68, 68))
        );

        buttonGroup.add(rdoNextVisitInfo);
        rdoNextVisitInfo.setSelected(true);
        rdoNextVisitInfo.setText("次回来店案内");
        rdoNextVisitInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNextVisitInfo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoNextVisitInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNextVisitInfoActionPerformed(evt);
            }
        });

        buttonGroup.add(rdoNextReserveInfo);
        rdoNextReserveInfo.setText("次回予約案内");
        rdoNextReserveInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoNextReserveInfo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoNextReserveInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNextReserveInfoActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("次回予約案内"));

        reserveButton.setIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_off.jpg"));
        reserveButton.setBorderPainted(false);
        reserveButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_on.jpg"));
        reserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveButtonActionPerformed(evt);
            }
        });

        lblNextReserve.setBackground(new java.awt.Color(204, 204, 204));
        lblNextReserve.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lblNextReserve.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNextReserve.setOpaque(true);

        jLabel4.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("予約日");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(reserveButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblNextReserve, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(reserveButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNextReserve, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("レシート印字メッセージ"));

        message.setColumns(20);
        message.setRows(5);
        message.setInputKanji(true);
        jScrollPane2.setViewportView(message);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addContainerGap())
        );

        pointPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "ポイント情報", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION)));

        jLabel16.setText("前回ポイント");

        jLabel17.setText("加算ポイント");

        jLabel18.setText("使用ポイント");

        jLabel19.setText("ポイント合計");

        txtPrevPoint.setEditable(false);
        txtPrevPoint.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtPrevPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtAddPoint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtAddPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAddPoint.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAddPointFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAddPointFocusLost(evt);
            }
        });
        txtAddPoint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddPointKeyPressed(evt);
            }
        });

        txtUsePoint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtUsePoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUsePoint.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsePointFocusLost(evt);
            }
        });
        txtUsePoint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsePointKeyPressed(evt);
            }
        });

        txtTotalPoint.setEditable(false);
        txtTotalPoint.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTotalPoint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel20.setText("ポイント");

        jLabel21.setText("ポイント");

        jLabel22.setText("ポイント");

        jLabel23.setText("ポイント");

        cashbackButton.setIcon(SystemInfo.getImageIcon("/button/account/cashback_off.jpg"));
        cashbackButton.setBorderPainted(false);
        cashbackButton.setFocusable(false);
        cashbackButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/cashback_on.jpg"));
        cashbackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashbackButtonbackPrevious(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pointPanelLayout = new org.jdesktop.layout.GroupLayout(pointPanel);
        pointPanel.setLayout(pointPanelLayout);
        pointPanelLayout.setHorizontalGroup(
            pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pointPanelLayout.createSequentialGroup()
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel18, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel17, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel19, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(txtTotalPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtUsePoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtPrevPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .add(txtAddPoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel20)
                    .add(jLabel21)
                    .add(jLabel22)
                    .add(jLabel23)))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pointPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(cashbackButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        pointPanelLayout.setVerticalGroup(
            pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pointPanelLayout.createSequentialGroup()
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel16)
                    .add(jLabel20)
                    .add(txtPrevPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel17)
                    .add(jLabel21)
                    .add(txtAddPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(jLabel22)
                    .add(txtUsePoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(pointPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19)
                    .add(jLabel23)
                    .add(txtTotalPoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(cashbackButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        hpbPanel.setLayout(null);

        jLabel6.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("会計金額 ");
        hpbPanel.add(jLabel6);
        jLabel6.setBounds(90, 10, 80, 30);

        txtTotalAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtTotalAmount.setColumns(15);
        txtTotalAmount.setMinimumSize(new java.awt.Dimension(2, 26));
        txtTotalAmount.setPreferredSize(new java.awt.Dimension(122, 20));
        txtTotalAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalAmountFocusLost(evt);
            }
        });
        hpbPanel.add(txtTotalAmount);
        txtTotalAmount.setBounds(171, 11, 61, 26);

        jLabel26.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("円");
        hpbPanel.add(jLabel26);
        jLabel26.setBounds(240, 20, 20, 20);

        jLabel5.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("来店処理金額 ");
        hpbPanel.add(jLabel5);
        jLabel5.setBounds(70, 40, 97, 30);

        txtAtShopTotalAmout.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtAtShopTotalAmout.setColumns(15);
        txtAtShopTotalAmout.setMinimumSize(new java.awt.Dimension(2, 26));
        txtAtShopTotalAmout.setPreferredSize(new java.awt.Dimension(122, 20));
        txtAtShopTotalAmout.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAtShopTotalAmoutFocusLost(evt);
            }
        });
        hpbPanel.add(txtAtShopTotalAmout);
        txtAtShopTotalAmout.setBounds(171, 43, 61, 26);

        jLabel27.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("円");
        hpbPanel.add(jLabel27);
        jLabel27.setBounds(240, 50, 20, 20);

        checkNext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        checkNext.setText("<html>この金額をホットペッパービューティの来店処理 　<br>　金額とする。（予約時金額を超えていた場合は、 　<br>　自動で予約予約時金額で来店処理します） </html>");
        checkNext.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkNext.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkNext.setOpaque(false);
        checkNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNextActionPerformed(evt);
            }
        });
        hpbPanel.add(checkNext);
        checkNext.setBounds(280, 0, 340, 80);

        hpbPanel1.setLayout(null);

        registButton2.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton2.setBorderPainted(false);
        registButton2.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButton2ActionPerformed(evt);
            }
        });
        hpbPanel1.add(registButton2);
        registButton2.setBounds(320, 40, 92, 25);

        chkPrintCopy.setText("控え印刷");
        chkPrintCopy.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkPrintCopy.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkPrintCopy.setOpaque(false);
        hpbPanel1.add(chkPrintCopy);
        chkPrintCopy.setBounds(170, 10, 100, 24);

        registButton3.setIcon(SystemInfo.getImageIcon("/button/print/printExcel_off.jpg"));
        registButton3.setBorderPainted(false);
        registButton3.setPressedIcon(SystemInfo.getImageIcon("/button/print/printExcel_on.jpg"));
        registButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButton3ActionPerformed(evt);
            }
        });
        hpbPanel1.add(registButton3);
        registButton3.setBounds(490, 10, 85, 25);

        receiptPrint.setIcon(SystemInfo.getImageIcon("/button/account/no-receipt_off.png")
        );
        receiptPrint.setBorderPainted(false);
        receiptPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receiptPrintActionPerformed(evt);
            }
        });
        hpbPanel1.add(receiptPrint);
        receiptPrint.setBounds(370, 10, 100, 24);

        chkPrintCustomer.setSelected(true);
        chkPrintCustomer.setText("顧客名印刷");
        chkPrintCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkPrintCustomer.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkPrintCustomer.setOpaque(false);
        hpbPanel1.add(chkPrintCustomer);
        chkPrintCustomer.setBounds(170, 40, 100, 24);

        receiptButton.setIcon(SystemInfo.getImageIcon("/button/account/receipt_off.png")
        );
        receiptButton.setBorderPainted(false);
        receiptButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/receipt_on.png"));
        receiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receiptButtonActionPerformed(evt);
            }
        });
        hpbPanel1.add(receiptButton);
        receiptButton.setBounds(280, 10, 72, 24);

        prepaidPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("パスブック情報"));

        lblPrevRemainValueLabel.setText("前回残");

        lblSalesValueLabel.setText("販売額");

        lblUseValueLabel.setText("利用額");

        lblNowRemainValueLabel.setText("現在残");

        txtPrevRemainValue.setEditable(false);
        txtPrevRemainValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtPrevRemainValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtSalesValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtSalesValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSalesValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesValueFocusLost(evt);
            }
        });
        txtSalesValue.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtSalesValuePropertyChange(evt);
            }
        });
        txtSalesValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSalesValueKeyPressed(evt);
            }
        });

        txtUseValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtUseValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUseValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUseValueFocusLost(evt);
            }
        });
        txtUseValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUseValueKeyPressed(evt);
            }
        });

        txtNowRemainValue.setEditable(false);
        txtNowRemainValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtNowRemainValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblYen1.setText("円");

        lblYen2.setText("円");

        lblYen3.setText("円");

        lblYen4.setText("円");

        org.jdesktop.layout.GroupLayout prepaidPanelLayout = new org.jdesktop.layout.GroupLayout(prepaidPanel);
        prepaidPanel.setLayout(prepaidPanelLayout);
        prepaidPanelLayout.setHorizontalGroup(
            prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, prepaidPanelLayout.createSequentialGroup()
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblUseValueLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblSalesValueLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblPrevRemainValueLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblNowRemainValueLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(14, 14, 14)
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, txtSalesValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, txtUseValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtNowRemainValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtPrevRemainValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lblYen1)
                    .add(lblYen2)
                    .add(lblYen3)
                    .add(lblYen4)))
        );
        prepaidPanelLayout.setVerticalGroup(
            prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(prepaidPanelLayout.createSequentialGroup()
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblPrevRemainValueLabel)
                    .add(lblYen1)
                    .add(txtPrevRemainValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSalesValueLabel)
                    .add(lblYen2)
                    .add(txtSalesValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUseValueLabel)
                    .add(lblYen3)
                    .add(txtUseValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(prepaidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNowRemainValueLabel)
                    .add(lblYen4)
                    .add(txtNowRemainValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        prepaidCard.setIcon(SystemInfo.getImageIcon("/button/common/prepaid_off.jpg"));
        prepaidCard.setBorderPainted(false);
        prepaidCard.setFocusable(false);
        prepaidCard.setPressedIcon(SystemInfo.getImageIcon("/button/common/prepaid_on.jpg"));
        prepaidCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prepaidCardbackPrevious(evt);
            }
        });

        backButton2.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton2.setBorderPainted(false);
        backButton2.setFocusable(false);
        backButton2.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButton2backPrevious(evt);
            }
        });

        printCard.setIcon(SystemInfo.getImageIcon("/button/common/point_off.jpg"));
        printCard.setBorderPainted(false);
        printCard.setFocusable(false);
        printCard.setPressedIcon(SystemInfo.getImageIcon("/button/common/point_on.jpg"));
        printCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printCardbackPrevious(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(prepaidCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(printCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(8, 8, 8)
                .add(backButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(prepaidCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(printCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(backButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0))
        );

        buttonGroup.add(rdNoReciptPrinting);
        rdNoReciptPrinting.setText("レシート印字なし");
        rdNoReciptPrinting.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdNoReciptPrintingStateChanged(evt);
            }
        });
        rdNoReciptPrinting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNoReciptPrintingActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(hpbPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 698, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(14, 14, 14)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rdoNextReserveInfo)
                                    .add(layout.createSequentialGroup()
                                        .add(rdoNextVisitInfo)
                                        .add(18, 18, 18)
                                        .add(rdNoReciptPrinting)))))
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pointPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(prepaidPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(hpbPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(4, 4, 4))
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(prepaidPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(pointPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(rdoNextVisitInfo)
                            .add(rdNoReciptPrinting))
                        .add(3, 3, 3)
                        .add(rdoNextReserveInfo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(11, 11, 11)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE)
                .add(hpbPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hpbPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

// カード印刷ダイアログを表示する
	private void printCardbackPrevious(java.awt.event.ActionEvent evt)//GEN-FIRST:event_printCardbackPrevious
	{//GEN-HEADEREND:event_printCardbackPrevious
            if (rdoNextVisitInfo.isSelected()) {
                // 次回来店案内にてカード印刷ダイアログを表示する
                CardPointPrintPanel.ShowDialog(
                        this,
                        ia.getSales(),
                        nextReserveDate,
                        startDate.getDate(),
                        endDate.getDate(),
                        limitDate.getDate(),
                        CardPointPrintPanel.NEXT_TYPE_NEXT_DATE,
                        this.discountUsePoint);

            } else {
                // 次回予約情報にてカード印刷ダイアログを表示する
                CardPointPrintPanel.ShowDialog(
                        this,
                        ia.getSales(),
                        nextReserveDate,
                        startDate.getDate(),
                        endDate.getDate(),
                        limitDate.getDate(),
                        CardPointPrintPanel.NEXT_TYPE_RESERVE_DATE,
                        this.discountUsePoint);
            }
	}//GEN-LAST:event_printCardbackPrevious

	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
            this.isClose = true;
            this.removeAll();
	}//GEN-LAST:event_formWindowClosing

    private void backButton2backPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton2backPrevious
        this.isClose = true;
        this.setVisible(false);
        this.removeAll();
    }//GEN-LAST:event_backButton2backPrevious

    private void nextVisitEnable(boolean status) {
        this.products.setEnabled(status);
        this.limitDate.setEditable(status);
        this.startDate.setEnabled(status);
        this.endDate.setEnabled(status);
    }

    private void nextReserveEnable(boolean status) {
        this.lblNextReserve.setEnabled(status);
        this.reserveButton.setEnabled(status);
        this.jLabel4.setEnabled(status);
    }

    private void rdoNextReserveInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNextReserveInfoActionPerformed
        nextVisitEnable(false);
        nextReserveEnable(true);
        this.changeMessage();
    }//GEN-LAST:event_rdoNextReserveInfoActionPerformed

    private void rdoNextVisitInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNextVisitInfoActionPerformed
        nextVisitEnable(true);
        nextReserveEnable(false);
        this.changeMessage();
    }//GEN-LAST:event_rdoNextVisitInfoActionPerformed

    private void endDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_endDateFocusGained
        endDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_endDateFocusGained

    private void endDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_endDateItemStateChanged
        this.changeMessage();
    }//GEN-LAST:event_endDateItemStateChanged

    private void startDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_startDateFocusGained
        startDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_startDateFocusGained

    private void startDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startDateItemStateChanged
        this.changeMessage();
    }//GEN-LAST:event_startDateItemStateChanged

    private void limitDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_limitDateFocusGained
        limitDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_limitDateFocusGained

    private void limitDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_limitDateItemStateChanged
        this.setInfomationDate();
//	this.changeMessage();
    }//GEN-LAST:event_limitDateItemStateChanged

    private boolean checkInput() {
        double remain = 0;
        try {
            remain = Double.parseDouble(txtNowRemainValue.getText());
        } catch (Exception e) {
        }
        if (remain < 0) {
             MessageDialog.showMessageDialog(this,
                            "パスブックの残高がマイナスになっています。\n閉じるを押してお会計情報を見なおしてください。",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }
    private void registButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registButton2ActionPerformed

        registButton2.setCursor(null);

        if (checkInput()) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                ia.setVisitedMemo(memo.getText());
                if (praiseDay > 0) {
                    ia.setNextVisitDate(limitDate.getDate());
                }

                ConnectionWrapper con = SystemInfo.getConnection();

                try {
                    con.begin();
                    //nahoang Add start 20140610
                    if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                        prepaidData = new PrepaidData();
                        prepaidData.setShopId(ia.getShop().getShopID());
                        prepaidData.setCustomerId(ia.getSales().getCustomer().getCustomerID());
                        prepaidData.setSlipNo(ia.getSales().getSlipNo());
                        prepaidData.setSalesValue(sumOfSaleValue);
                        prepaidData.setUseValue(sumOfUseValue);
                        prepaidData.regist(con);
                        //remove by ltthuc 2014/06/19
                        HairInputAccountPanel.setEditFlag(0);
                        //end add
                        //nahoang Add end
                    }
                   
                } catch (Exception e) {
                }

                // 前回のデータに再来日(この日付)をセットする
                String sql = "select shop_id, max(slip_no) as slip_no from data_sales " + "\r\n"
                        + "where shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()) + "\r\n"
                        + "and customer_id = " + SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID()) + "\r\n"
                        + "group by shop_id";
                try {
                    ResultSetWrapper rs = con.executeQuery(sql);
                    if (rs.next()) {
                        sql = "update data_sales set reappearance_date = " + SQLUtil.convertForSQL(ia.getSales().getSalesDate()) + "\r\n"
                                + " where shop_id = " + SQLUtil.convertForSQL(rs.getInt("shop_id")) + " and slip_no = " + SQLUtil.convertForSQL(rs.getLong("slip_no"));
                        con.executeUpdate(sql);

                    }
                } catch (Exception e) {
                    System.err.println("SQL = \r\n" + sql);
                    e.printStackTrace();
                    try {
                        con.rollback();
                    } catch (Exception e2) {
                    }
                }
                //HPB 【来店時金額登録ＡＰＩ】を実行

                if (SystemInfo.getPossSalonId() != null) {
                    if (!SystemInfo.getPossSalonId().equals("")) {
                        if (comingId != null) {

                            connectispotapi.ConnectIspotApi WS = new ConnectIspotApi();
                            RegistPaymentWSI wsi = new RegistPaymentWSI();
                            RegistPaymentResultsWSI results = new RegistPaymentResultsWSI();
                            wsi.setPosId("P00007");
                            wsi.setPassword("beauty_geobeck_pos12");
                            wsi.setPosSalonId("PS00000007");
                            wsi.setComingId(comingId);
                            wsi.setPaymentPrice(txtTotalAmount.getText().replaceAll(",", ""));
                            wsi.setFormat("json");
                            Gson gson = new Gson();
                            String jsonResult = WS.getConnectIspotApiPort().paymentPrice(gson.toJson(wsi));
                            if (!jsonResult.equals("")) {
                                try {
                                    results = gson.fromJson(jsonResult, RegistPaymentResultsWSI.class);
                                } catch (Exception e) {
                                    System.out.println("error at http://api-test.sosia.jp/hpb/ConnectIspotApi.php?wsdl-function:commingInfomation At https://wwwtst.beauty.psa.d.hotpepper.jp/CLA/bt/common/v1/search/comingInfo/doSearch");
                                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                                    return;
                                }
                            } else {
                                try {
                                    con.rollback();
                                    MessageDialog.showMessageDialog(
                                            this,
                                            "接続が出来ないので、もう一度やってみてください。",
                                            this.getTitle(),
                                            JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                } catch (Exception e2) {
                                }
                            }
                            if (results != null) {
                                if (!results.getResults().getReturn_code().equalsIgnoreCase("0000") && !results.getResults().getReturn_code().equalsIgnoreCase("0001")) {
                                    String msg = "";
                                    if (results.getResults().getMessage() != null) {
                                        for (int i = 0; i < results.getResults().getMessage().size(); i++) {
                                            msg = msg + results.getResults().getMessage().get(i) + "\n";
                                        }
                                        MessageDialog.showMessageDialog(
                                                this,
                                                msg,
                                                this.getTitle(),
                                                JOptionPane.INFORMATION_MESSAGE);
                                        return;
                                    }
                                }

                            }

                        }
                    }
                }
                //HPB 【来店時金額登録ＡＰＩ】を実行

                ia.setStatus(3);
                //vtbphuong start add 20131213 
                ia.getSales().setChangePickup(false);
                //vtbphuong end add 20131213 
                if (ia.regist(con)) {
                    //お会計編集の場合
//                    if(!isNewAccount){
//                        //コース契約履歴削除
//                        ia.deleteCourseContract(con);
//                        //コース消化履歴削除
//                        ia.deleteConsumptionCourse(con);
//                        //コース契約履歴更新
//                        ia.registCourseContract(con);
//                        //コース消化履歴更新
//                        ia.registConsumptionCourse(con);
//                    }

                    // ポイント登録処理
                    if ((SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 1 || SystemInfo.getPointOutputType() == 2))) {
                        this.registPoint();
                    }

                    // レシート出力
                    this.printReceipt();
                    if (chkPrintCopy.isSelected()) {
                        // 控え印刷
                        this.printReceipt();
                    }

                    try {
                        con.commit();
                    } catch (Exception e) {
                    }

                    /*
                     * MessageDialog.showMessageDialog(this,
                     * MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                     * this.getTitle(), JOptionPane.INFORMATION_MESSAGE);
                     */
                    try {
                        if (SystemInfo.isUseCustomerDisplay()) {
                            CustomerDisplay cd = this.getCustomerDisplay();
                            cd.clearScreen();
                        }
                    } catch (Exception e) {
                    }
                    
                    //nhanvt start add 20141120 Bug #32638
                    StringBuilder sql1 = new StringBuilder();

                    sql1.append(" UPDATE ");
                    sql1.append("     data_sales_detail");
                    sql1.append(" SET ");
                    sql1.append("    contract_shop_id = contract_data_temp.shop_id,");
                    sql1.append("    contract_no = contract_data_temp.contract_no,");
                    sql1.append("    contract_detail_no = contract_data_temp.contract_detail_no");
                    sql1.append(" FROM ");
                    sql1.append("     (");
                    sql1.append("      SELECT dc.shop_id, dc.slip_no,dc.contract_no,dc.contract_detail_no, dc.product_id, dcd.staff_id");
                    sql1.append("      FROM  data_contract dc ");
                   
                    sql1.append("      inner join data_contract_digestion dcd on dcd.contract_shop_id = dc.shop_id ");
                    sql1.append("         and dcd.contract_no = dc.contract_no and dcd.contract_detail_no = dc.contract_detail_no ");
                    
                    sql1.append("      WHERE ");
                    sql1.append("            dc.shop_id = " + ia.getSales().getShop().getShopID());
                    sql1.append(" and dc.slip_no = " + ia.getSales().getSlipNo());
                    sql1.append("     ) ");
                    sql1.append("     AS contract_data_temp ");
                    sql1.append("WHERE data_sales_detail.shop_id = contract_data_temp.shop_id  ");
                    sql1.append("      and data_sales_detail.slip_no = contract_data_temp.slip_no ");
                    sql1.append("      and data_sales_detail.product_division = 6 ");
                    sql1.append("      and data_sales_detail.product_id = contract_data_temp.product_id ");
                    sql1.append("      and ( data_sales_detail.contract_shop_id is null ");
                    sql1.append("      or data_sales_detail.contract_no is null ");
                    sql1.append("      or  data_sales_detail.contract_detail_no is null ) ");
                    sql1.append("      and data_sales_detail.staff_id = contract_data_temp.staff_id");
                    sql1.append("      and data_sales_detail.shop_id = " + ia.getSales().getShop().getShopID());
                    sql1.append("      and data_sales_detail.slip_no  = " + ia.getSales().getSlipNo());
                    try {
                        if (con.executeUpdate(sql1.toString()) >= 0) {
                            con.commit();
                        } else {
                            con.rollback();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(HairInputAccountDetailDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //nhanvt end add 20141120 Bug #32638
                    
                } else {
                    try {
                        con.rollback();
                    } catch (Exception e) {
                    }
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "伝票データ"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }

                // お会計内容を予約表に反映
                this.reservationMenuUpdate(con);
                this.isClose = false;
                this.setVisible(false);
                //add by ltthuc 2014/06/06 set paymentMethodName = null
                HairInputAccountPanel.arrPaymentMethodId_1 = null;

                // vtbphuong start change 20131216 「退店画面」で「登録」をクリックした後に「受渡画面」を表示しないようにしてください。
//          
//            if (SystemInfo.getCurrentShop().getCourseFlag() != null
//                    && SystemInfo.getCurrentShop().getCourseFlag().intValue() == 1) {
//
//                boolean flagDilivery = false;
//                MstDataDeliveryProduct sm = new MstDataDeliveryProduct();
//                ArrayList<MstDataDeliveryProduct> listProduct = new ArrayList<MstDataDeliveryProduct>();
//                listProduct = sm.load(SystemInfo.getConnection(), ia.getSales().getCustomer().getCustomerID(), SystemInfo.getCurrentShop().getShopID(), ia.getSales().getSlipNo());
//                if (!listProduct.isEmpty()) {
//                    flagDilivery = true;
//                }
//                if (flagDilivery) {
//                    HairInputAccountProductDelivery hicpd = new HairInputAccountProductDelivery(ia.getSales().getCustomer(), ia.getSales().getSlipNo(), false);
//                    SwingUtil.openAnchorDialog(null, true, hicpd, "【商品受渡画面】", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
//                }
//            }
                //// vtbphuong end change 20131216 「退店画面」で「登録」をクリックした後に「受渡画面」を表示しないようにしてください。
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

    }//GEN-LAST:event_registButton2ActionPerformed

    private void reserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveButtonActionPerformed

        rtt = new ReservationTimeTablePanel();

        rtt.setOpenByCheckout(true);

        rtt.setCustomer(ia.getSales().getCustomer());

        rtt.setDate(ia.getSales().getSalesDate());

        rtt.load();

        rtt.setNextReservation(true);

        /*
         * ダイアログで開く
         */
        SwingUtil.openAnchorDialog(null, true, rtt, "予約登録", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);

        rtt = null;

        System.gc();

        this.loadNextReserve();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd(E) kk:mm");

        //レシート設定
        PrintReceipt pr = new PrintReceipt();
        if (this.rdoNextReserveInfo.isSelected()) {
            message.setText("<次回予約案内>\n"
                    + (nextReserveDate == null ? "" : sdf.format(nextReserveDate) + "～ ")
                    + (nextReserveMenu.length() > 0 ? ("\n" + nextReserveMenu) : "") + "\n\n"
                    + pr.getReceiptSetting().getMessage());
            message.setCaretPosition(0);
        }

    }//GEN-LAST:event_reserveButtonActionPerformed

    private void receiptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receiptButtonActionPerformed
        if (this.printReceiptFlsg) {
            this.printReceiptFlsg = false;
            receiptPrint.setIcon(SystemInfo.getImageIcon("/button/account/no-receipt_on.png"));
        } else {
            this.printReceiptFlsg = true;
            receiptPrint.setIcon(SystemInfo.getImageIcon("/button/account/no-receipt_off.png"));
        }
    }//GEN-LAST:event_receiptButtonActionPerformed

    private void txtUsePointKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsePointKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            rdoNextVisitInfo.requestFocusInWindow();
        }
}//GEN-LAST:event_txtUsePointKeyPressed

    private void txtUsePointFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsePointFocusLost
        updatePreviewPoint();
}//GEN-LAST:event_txtUsePointFocusLost

    private void txtAddPointKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddPointKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtUsePoint.requestFocusInWindow();
        }
}//GEN-LAST:event_txtAddPointKeyPressed

    private void txtAddPointFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddPointFocusLost
        updatePreviewPoint();
}//GEN-LAST:event_txtAddPointFocusLost

    private void prepaidCardbackPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prepaidCardbackPrevious

        long prepaidValue = 0;

        for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
            if (dpd.getPaymentMethod() == null) {
                continue;
            }
            if (dpd.getPaymentMethod().isPrepaid()) {
                prepaidValue += dpd.getPaymentValue().longValue();
            }
        }

        CardPrepaidPrintPanel.ShowDialog(this, ia.getSales(), prepaidValue);

    }//GEN-LAST:event_prepaidCardbackPrevious

    private void cashbackButtonbackPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashbackButtonbackPrevious

        CashbackDialog cd = new CashbackDialog(this, true);
        cd.setShopID(ia.getShop().getShopID());
        cd.setCustomerID(ia.getSales().getCustomer().getCustomerID());
        cd.setCustomerName(ia.getSales().getCustomer().getFullCustomerName());
        cd.setNowPoint(Long.valueOf(txtTotalPoint.getText()));
        cd.setVisible(true);

        // ポイント情報再表示
        displayPointInfo(ia, discountUsePoint);

    }//GEN-LAST:event_cashbackButtonbackPrevious

    private void registButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registButton3ActionPerformed

        int rowDiv5 = 17;
        int rowDiv2 = 25;
        Integer itemDiv2 = 0;
        Integer itemDiv5 = 0;
        boolean flagPrintExcel = false;
        for (DataSalesDetail dsd : ia.getSales()) {
            if (dsd.getProductDivision() == 2) {
                itemDiv2 += 1;
                flagPrintExcel = true;
                continue;
            }
            if (dsd.getProductDivision() == 5) {
                itemDiv5 += 1;
                flagPrintExcel = true;
            }

        }

        if (!flagPrintExcel) {
            return;
        }

        JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion("利用契約書");
        jx.setTemplateFile("/reports/利用契約書.xls");

        jx.setCellValue(2, 3, ia.getSales().getCustomer().getCustomerNo().toString());
        jx.setCellValue(2, 4, ia.getSales().getSlipNo());
        jx.setCellValue(4, 4, ia.getSales().getShop().getShopName());
        jx.setCellValue(9, 3, ia.getSales().getSalesDate());
        if (itemDiv2 > 7) {
            jx.insertRow(26, 27, itemDiv2 - 7);
        }
        if (itemDiv5 > 5) {
            jx.insertRow(18, 19, itemDiv5 - 5);
            rowDiv2 = rowDiv2 + itemDiv5 - 5;
        }
        for (DataSalesDetail dsd2 : ia.getSales()) {
            if (dsd2.getProductDivision().intValue() == 2) {
                jx.setCellValue(1, rowDiv2, dsd2.getProduct().getProductClass().getProductClassName());
                jx.setCellValue(3, rowDiv2, dsd2.getProduct().getProductName());
                jx.setCellValue(5, rowDiv2, ia.getAccountSetting().getDisplayValue(dsd2.getProductValue(), ia.getTaxRate()));
                jx.setCellValue(8, rowDiv2, dsd2.getProductNum());
                jx.setCellValue(9, rowDiv2, ia.getAccountSetting().getDisplayValue(dsd2.getProductValue(), ia.getTaxRate()) * dsd2.getProductNum());
                rowDiv2 = rowDiv2 + 1;

            }
            if (dsd2.getProductDivision().intValue() == 5) {
                jx.setCellValue(1, rowDiv5, dsd2.getCourse().getCourseClass().getCourseClassName());
                jx.setCellValue(3, rowDiv5, dsd2.getCourse().getCourseName());
                jx.setCellValue(5, rowDiv5, dsd2.getCourse().getNum());
                // vtbphuong start change 20140909 Bug #30440  
//                String sqlValid = "select   to_date(date_trunc('day', current_date  )"
//                        + " + cast( praise_time_limit || ' months' as interval),'YYYY/MM/DD HH24:MI') -1 as valid_date"
//                        + "  from mst_course ";
//                
                 String sqlValid = "select   to_date( CAST ( date_trunc('day', current_date  )"
                        + " + cast( praise_time_limit || ' months' as interval) AS text) ,'YYYY/MM/DD HH24:MI') -1 as valid_date"
                        + "  from mst_course "; 
                  // vtbphuong start change 20140909 Bug #30440  
                sqlValid += " where course_id = " + SQLUtil.convertForSQL(dsd2.getProduct().getProductID());

                try {
                    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sqlValid.toString());
                    if (rs.first()) {
                        jx.setCellValue(8, rowDiv5, rs.getDate("valid_date"));
                    }
                } catch (Exception ex) {
                }
                jx.setCellValue(7, rowDiv5, "回");
                // jx.setCellValue(9, rowDiv5, dsd2.getTotal());
                jx.setCellValue(9, rowDiv5, ia.getAccountSetting().getDisplayValue(dsd2.getProductValue(), ia.getTaxRate()));
                rowDiv5 = rowDiv5 + 1;
            }
        }
        int iaddRow = 0;
        if (itemDiv2 > 7) {
            iaddRow += itemDiv2 - 7;
        }
        if (itemDiv5 > 5) {
            iaddRow += itemDiv5 - 5;
        }
        long cashValue = 0l;
        for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
            if (dpd.getPaymentMethod() == null) {
                continue;
            }
            switch (dpd.getPaymentMethod().getPaymentClassID().intValue()) {
                case 1:
                    if (dpd.getPaymentValue() > 0) {
                        jx.setCellValue(5, 38 + iaddRow, dpd.getPaymentValue());
                        cashValue += dpd.getPaymentValue();
                    }
                    break;
                case 2:
                    if (dpd.getPaymentValue() > 0) {
                        jx.setCellValue(5, 39 + iaddRow, dpd.getPaymentValue());
                        cashValue += dpd.getPaymentValue();
                    }
                    break;
                case 3:
                    if (dpd.getPaymentValue() > 0) {
                        jx.setCellValue(5, 40 + iaddRow, dpd.getPaymentValue());
                        cashValue += dpd.getPaymentValue();
                    }
                    break;
                case 4:
                    if (dpd.getPaymentValue() > 0) {
                        jx.setCellValue(5, 41 + iaddRow, dpd.getPaymentValue());
                        cashValue += dpd.getPaymentValue();
                    }
                    break;
            }

        }
        // jx.setCellValue(5, 42+iaddRow, ia.getSales().getSalesTotal() - cashValue);
        jx.setCellValue(5, 42 + iaddRow, ia.getTotal(3).getValue() - cashValue);

        // shop
        jx.setCellValue(7, 46 + iaddRow, ia.getSales().getShop().getShopName());
        String str1 = "";
        //nhanvt start edit 20141204 Bug #33491
        if(ia.getSales().getShop().getPostalCode() != null && ia.getSales().getShop().getPostalCode() != "" && ia.getSales().getShop().getPostalCode().trim().length() >= 7){
           str1 = "〒" + ia.getSales().getShop().getPostalCode().substring(0, 3);
           str1 += "-" + ia.getSales().getShop().getPostalCode().substring(3); 
        }
        str1 += ia.getSales().getShop().getAddress()[0] !=null?ia.getSales().getShop().getAddress()[0]: "";
        str1 += ia.getSales().getShop().getAddress()[1] !=null?ia.getSales().getShop().getAddress()[1]: "";
        str1 += ia.getSales().getShop().getAddress()[2] !=null?ia.getSales().getShop().getAddress()[2]: "";
        jx.setCellValue(7, 47 + iaddRow, str1);
        jx.setCellValue(7, 48 + iaddRow, ia.getSales().getShop().getAddress()[3] !=null?ia.getSales().getShop().getAddress()[3]: "");
        //nhanvt end edit 20141204 Bug #33491
        jx.setCellValue(7, 51 + iaddRow, ia.getSales().getStaff().getFullStaffName());
        jx.setFormularActive();

        jx.openWorkbook();

    }//GEN-LAST:event_registButton3ActionPerformed

    private void receiptPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receiptPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_receiptPrintActionPerformed

    private void txtTotalAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalAmountFocusLost
    }//GEN-LAST:event_txtTotalAmountFocusLost

    private void txtAtShopTotalAmoutFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAtShopTotalAmoutFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAtShopTotalAmoutFocusLost

    private void checkNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNextActionPerformed
    }//GEN-LAST:event_checkNextActionPerformed

    private void txtSalesValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesValueFocusLost
        updatePrepaidChange();
    }//GEN-LAST:event_txtSalesValueFocusLost

    private void txtSalesValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesValueKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            rdoNextVisitInfo.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtSalesValueKeyPressed

    private void txtUseValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUseValueFocusLost
        updatePrepaidChange();
    }//GEN-LAST:event_txtUseValueFocusLost

    private void txtUseValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUseValueKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            rdoNextVisitInfo.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtUseValueKeyPressed

    private void txtSalesValuePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtSalesValuePropertyChange
        // TODO add your handling code here:
        // sumOfSaleValue = Long.valueOf(txtSalesValue.getValue().toString()).longValue();      
    }//GEN-LAST:event_txtSalesValuePropertyChange

    private void txtAddPointFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAddPointFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddPointFocusGained

    private void rdNoReciptPrintingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdNoReciptPrintingStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_rdNoReciptPrintingStateChanged

    private void rdNoReciptPrintingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNoReciptPrintingActionPerformed
        // TODO add your handling code here:
        message.setText("");
    }//GEN-LAST:event_rdNoReciptPrintingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton2;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton cashbackButton;
    private javax.swing.JCheckBox checkNext;
    private javax.swing.JCheckBox chkPrintCopy;
    private javax.swing.JCheckBox chkPrintCustomer;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo endDate;
    private javax.swing.JPanel hpbPanel;
    private javax.swing.JPanel hpbPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblNextReserve;
    private javax.swing.JLabel lblNowRemainValueLabel;
    private javax.swing.JLabel lblPrevRemainValueLabel;
    private javax.swing.JLabel lblSalesValueLabel;
    private javax.swing.JLabel lblUseValueLabel;
    private javax.swing.JLabel lblYen1;
    private javax.swing.JLabel lblYen2;
    private javax.swing.JLabel lblYen3;
    private javax.swing.JLabel lblYen4;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo limitDate;
    private com.geobeck.swing.JTextAreaEx memo;
    private com.geobeck.swing.JTextAreaEx message;
    private javax.swing.JPanel pointPanel;
    private javax.swing.JButton prepaidCard;
    private javax.swing.JPanel prepaidPanel;
    private javax.swing.JButton printCard;
    private javax.swing.JTable products;
    private javax.swing.JRadioButton rdNoReciptPrinting;
    private javax.swing.JRadioButton rdoNextReserveInfo;
    private javax.swing.JRadioButton rdoNextVisitInfo;
    private javax.swing.JButton receiptButton;
    private javax.swing.JButton receiptPrint;
    private javax.swing.JButton registButton2;
    private javax.swing.JButton registButton3;
    private javax.swing.JButton reserveButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo startDate;
    private com.geobeck.swing.JFormattedTextFieldEx txtAddPoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtAtShopTotalAmout;
    private com.geobeck.swing.JFormattedTextFieldEx txtNowRemainValue;
    private com.geobeck.swing.JFormattedTextFieldEx txtPrevPoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtPrevRemainValue;
    private com.geobeck.swing.JFormattedTextFieldEx txtSalesValue;
    private com.geobeck.swing.JFormattedTextFieldEx txtTotalAmount;
    private com.geobeck.swing.JFormattedTextFieldEx txtTotalPoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtUsePoint;
    private com.geobeck.swing.JFormattedTextFieldEx txtUseValue;
    // End of variables declaration//GEN-END:variables
    private MoveNextField mnf = new MoveNextField();

    private void setInfomationDate() {
        java.util.GregorianCalendar limit = new java.util.GregorianCalendar();
        limit.setTime(limitDate.getDate());

        limit.add(Calendar.DAY_OF_MONTH, -5);
        startDate.setDate(limit.getTime());

        limit.add(Calendar.DAY_OF_MONTH, 10);
        endDate.setDate(limit.getTime());
    }

    private void loadNextReserve() {
        //コネクションを取得
        ConnectionWrapper con = SystemInfo.getConnection();

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     min(a.reservation_datetime) as next_date");
        sql.append(" from");
        sql.append("     data_reservation_detail a");
        sql.append("         join data_reservation b");
        sql.append("             using(shop_id, reservation_no)");
        sql.append(" where");
        sql.append("         a.delete_date is null");
        sql.append("     and b.delete_date is null");
        sql.append("     and a.shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
        sql.append("     and b.customer_id = " + SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID()));
        sql.append("     and b.status = 1");
        sql.append("     and a.reservation_datetime > now()");

        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next() && rs.getDate("next_date") != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm～");
                nextReserveDate = rs.getTimestamp("next_date");

                this.lblNextReserve.setText(sdf.format(nextReserveDate));
            }
        } catch (Exception e) {
            System.err.println("SQL = " + sql);
            e.printStackTrace();
        }

        sql.setLength(0);
        sql.append(" select");
        // start edit 20130703 nakhoa
        // sql.append("     c.technic_name");
        // data_Reservation_detailのcourse_flgが入っている場合はmst_courseから名称を取得してください
        sql.append("     case when a.course_flg is null then c.technic_name \n");
        sql.append("     else mc.course_name end as technic_name \n");
        // end edite 30130703 nakhoa
        sql.append(" from");
        sql.append("     data_reservation_detail a");
        sql.append("         join data_reservation b");
        sql.append("             using(shop_id, reservation_no)");
        sql.append("         left join mst_technic c");
        sql.append("             using(technic_id)");
        // start add 20130703 nakhoa
        sql.append("        left join mst_course mc ");
        sql.append("            on a.technic_id = mc.course_id ");
        // end add 20130703 nakhoa
        sql.append(" where");
        sql.append("         a.delete_date is null");
        sql.append("     and b.delete_date is null");
        sql.append("     and a.shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
        sql.append("     and b.customer_id = " + SQLUtil.convertForSQL(ia.getSales().getCustomer().getCustomerID()));
        sql.append("     and b.status = 1");
        sql.append("     and a.reservation_datetime ::DATE = " + SQLUtil.convertForSQL(nextReserveDate) + ":: DATE");
        sql.append(" order by");
        sql.append("     a.reservation_datetime");

        try {
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            while (rs.next()) {
                if (nextReserveMenu.length() > 0) {
                    nextReserveMenu += "、";
                }
                nextReserveMenu += rs.getString("technic_name");
            }

        } catch (Exception e) {
            System.err.println("SQL = " + sql);
            e.printStackTrace();
        }

    }

    public boolean isClose() {
        return this.isClose;
    }

    private void printReceipt() {
        //レシート出力しない場合は処理を抜ける
        if (!this.printReceiptFlsg) {
            return;
        }

        PrintReceipt pr = new PrintReceipt();
        PrintReceiptDigestion prd = new PrintReceiptDigestion();

        //プリンタ情報が存在しない場合は処理を抜ける
        if (!pr.canPrint()) {
            return;
        }

        // 印刷処理
        if (pr.getReceiptSetting().getReceiptSize().equals(2)) {
            printReceiptSpecial(pr);
        } else {
            printReceiptGeneral(pr);
        }

        printReceiptDigestion(prd);

    }

    private void printReceiptSpecial(PrintReceipt pr) {

        List<DataSalesDetail> list = new ArrayList<DataSalesDetail>();
        for (DataSalesDetail dsd : ia.getSales()) {
            if (dsd.getProductDivision() == 0) {
                continue;
            }
            list.add(dsd);
        }
        int startValue = list.size() + 1;
        int endValue = Double.valueOf(Math.ceil(list.size() / 5d) * 5).intValue();
        for (int i = startValue; i <= endValue; i++) {
            list.add(null);
        }

        int totalPage = list.size() / 5;

        for (int p = 0; p < totalPage; p++) {

            pr.setCustomer(ia.getSales().getCustomer());

            pr.clear();
            for (int r = 0; r < 5; r++) {
                DataSalesDetail dsd = (DataSalesDetail) list.get((p * 5) + r);
                if (dsd != null) {
                    if (dsd.getProductDivision() == 5) {
                        //コース契約の場合
                        pr.add(new ReceiptData(
                                dsd.getProductDivision(),
                                dsd.getCourse().getCourseClass().getCourseClassName(),
                                dsd.getCourse().getCourseName(),
                                dsd.getProductNum(),
                                dsd.getProductValue(),
                                //SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
                                ia.getAccountSetting().getTax(dsd.getProductValue(), dsd.getDiscountValue(), SystemInfo.getTaxRate(ia.getSales().getSalesDate())),
                                ia.getAccountSetting().getDisplayPriceType() == 1,
                                ia.getAccountSetting().getDiscountType() == 1));
                    } else if (dsd.getProductDivision() == 6) {
                        //コース消化の場合
//                        pr.add(new ReceiptData(
//                                dsd.getProductDivision(),
//                                dsd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassName(),
//                                dsd.getConsumptionCourse().getCourseName(),
//                                dsd.getConsumptionNum(),
//                                dsd.getProductValue(),
//                                SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
//                                ia.getAccountSetting().getDisplayPriceType() == 1,
//                                ia.getAccountSetting().getDiscountType() == 1));
                        continue;
                    } else {
                        pr.add(new ReceiptData(
                                dsd.getProductDivision(),
                                dsd.getProduct().getProductClass().getProductClassName(),
                                dsd.getProduct().getProductName(),
                                dsd.getProductNum(),
                                dsd.getProductValue(),
                                //SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
                                ia.getAccountSetting().getTax(dsd.getProductValue(), dsd.getDiscountValue(), SystemInfo.getTaxRate(ia.getSales().getSalesDate())),
                                ia.getAccountSetting().getDisplayPriceType() == 1,
                                ia.getAccountSetting().getDiscountType() == 1));
                    }
                } else {
                    pr.add(null);
                }
            }

            pr.setDiscount(-ia.getTotal(1).getValue());

            if (ia.getAccountSetting().getDisplayPriceType() == 0) {
                //内税表示
                pr.setSubtotal(ia.getTotal(2).getValue() + ia.getTotal(3).getValue());

            } else {
                //外税表示
                if (ia.getAccountSetting().getDiscountType() == 0) {
                    // 税込額から割引
                    Double taxRate = SystemInfo.getTaxRate(ia.getSales().getSalesDate());
                    long discount = (long) Math.floor(ia.getTotal(1).getValue() / (1d + taxRate));
                    pr.setDiscount(-1 * discount);
                    pr.setSubtotal(ia.getTotal(0).getValue() - discount);

                } else {
                    // 税抜額から割引
                    pr.setSubtotal(ia.getTotal(0).getValue() - ia.getTotal(1).getValue());
                }
                pr.setSubtotalInTax(ia.getTotal(2).getValue() + ia.getTotal(3).getValue());
            }

            pr.setTax(ia.getTotal(4).getValue());
            pr.setSumtotal(ia.getTotal(3).getValue());
            pr.setAllDiscount(-ia.getTotal(2).getValue());
            pr.setOutOfValue(ia.getPaymentTotal());
            pr.setChangeValue(ia.getTotal(5).getValue());
            pr.setStaff(ia.getSales().getPayment(0).getStaff());
            pr.setChargeStaff(ia.getSales().getStaff());
            pr.setMessage(this.message.getText());
            pr.setSlipNo(ia.getSales().getSlipNo());

            long cashValue = 0l;
            long cardValue = 0l;
            long eCashValue = 0l;
            long giftValue = 0l;

            for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
                if (dpd.getPaymentMethod() == null) {
                    continue;
                }

                switch (dpd.getPaymentMethod().getPaymentClassID().intValue()) {
                    case 1:
                        cashValue += dpd.getPaymentValue();
                        break;
                    case 2:
                        cardValue += dpd.getPaymentValue();
                        // Start Edit 20130516 nakhoa
                        // pr.setCardTitle(dpd.getPaymentMethod().getPaymentMethodName());
                        StringBuilder cardTitle = new StringBuilder();
                        for (String cardNama : ia.getSales().getCardName()) {
                            cardTitle.append(cardNama);
                            break;
                        }
                        pr.setCardTitle(cardTitle.toString());
                        // End Edit 20130516 nakhoa
                        break;
                    case 3:
                        eCashValue += dpd.getPaymentValue();
                        pr.seteCashTitle(dpd.getPaymentMethod().getPaymentMethodName());
                        break;
                    case 4:
                        giftValue += dpd.getPaymentValue();
                        pr.setGiftTitle(dpd.getPaymentMethod().getPaymentMethodName());
                        break;
                }
            }
            pr.setCashValue(cashValue);
            pr.setCardValue(cardValue);
            pr.setECashValue(eCashValue);
            pr.setGiftValue(giftValue);

            // ポイント情報
            if ((SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 1 || SystemInfo.getPointOutputType() == 2))) {
                if (CheckUtil.isNumber(txtPrevPoint.getText())) {
                    pr.setPrevPoint(Integer.valueOf(txtPrevPoint.getText()));
                }
                if (CheckUtil.isNumber(txtAddPoint.getText())) {
                    pr.setAddPoint(Integer.valueOf(txtAddPoint.getText()));
                }
                if (CheckUtil.isNumber(txtUsePoint.getText())) {
                    pr.setUsePoint(Integer.valueOf(txtUsePoint.getText()));
                }
                if (CheckUtil.isNumber(txtTotalPoint.getText())) {
                    pr.setTotalPoint(Integer.valueOf(txtTotalPoint.getText()));
                }
            }

            pr.setReceiptType(PrintReceipt.ReceiptType.SPECIAL);

            pr.setPrintCustomer(chkPrintCustomer.isSelected());

            pr.setSalesDate(ia.getSales().getSalesDate());
            pr.setPageNo((p + 1) + "/" + totalPage);
            pr.setLastPage((p + 1) == totalPage);
            //VTAn Start add 20140612 
            if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals(DATABASE_NAME) || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                pr.setPrevAmount(Long.valueOf(txtPrevRemainValue.getText()));
                pr.setAddAmount(Long.valueOf(txtSalesValue.getText()));
                pr.setUseAmount(Long.valueOf(txtUseValue.getText()));
                pr.setTotalAmount(Long.valueOf(txtNowRemainValue.getText()));

            }
            //VTAn End add 20140612

            pr.print(MediaSizeName.JIS_B5, OrientationRequested.PORTRAIT);
        }
    }

    private void printReceiptGeneral(PrintReceipt pr) {
        PrintReceiptDigestion prd = new PrintReceiptDigestion();

        pr.setCustomer(ia.getSales().getCustomer());

        for (DataSalesDetail dsd : ia.getSales()) {

            if (dsd.getProductDivision() == 0) {
                continue;
            }

            if (dsd.getProductDivision() == 5) {
                //コース契約の場合
                pr.add(new ReceiptData(
                        dsd.getProductDivision(),
                        dsd.getCourse().getCourseClass().getCourseClassName(),
                        dsd.getCourse().getCourseName(),
                        dsd.getProductNum(),
                        dsd.getProductValue(),
                        //SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
                        ia.getAccountSetting().getTax(dsd.getProductValue(), dsd.getDiscountValue(), SystemInfo.getTaxRate(ia.getSales().getSalesDate())),
                        ia.getAccountSetting().getDisplayPriceType() == 1,
                        ia.getAccountSetting().getDiscountType() == 1));
            } else if (dsd.getProductDivision() == 6) {
                //コース消化の場合
//                pr.add(new ReceiptData(
//                        dsd.getProductDivision(),
//                        dsd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassName(),
//                        dsd.getConsumptionCourse().getCourseName(),
//                        dsd.getConsumptionNum(),
//                        dsd.getProductValue(),
//                        SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
//                        ia.getAccountSetting().getDisplayPriceType() == 1,
//                        ia.getAccountSetting().getDiscountType() == 1));
                continue;
            } else {
                pr.add(new ReceiptData(
                        dsd.getProductDivision(),
                        dsd.getProduct().getProductClass().getProductClassName(),
                        dsd.getProduct().getProductName(),
                        dsd.getProductNum(),
                        dsd.getProductValue(),
                        //SystemInfo.getTax(dsd.getProductValue(), ia.getSales().getSalesDate()),
                        ia.getAccountSetting().getTax(dsd.getProductValue(), dsd.getDiscountValue(), SystemInfo.getTaxRate(ia.getSales().getSalesDate())),
                        ia.getAccountSetting().getDisplayPriceType() == 1,
                        ia.getAccountSetting().getDiscountType() == 1));
            }
        }

        pr.setDiscount(-ia.getTotal(1).getValue());

        if (ia.getAccountSetting().getDisplayPriceType() == 0) {
            //内税表示
            pr.setSubtotal(ia.getTotal(2).getValue() + ia.getTotal(3).getValue());

        } else {
            //外税表示
            if (ia.getAccountSetting().getDiscountType() == 0) {
                // 税込額から割引
                Double taxRate = SystemInfo.getTaxRate(ia.getSales().getSalesDate());
                long discount = (long) Math.floor(ia.getTotal(1).getValue() / (1d + taxRate));
                pr.setDiscount(-1 * discount);
                pr.setSubtotal(ia.getTotal(0).getValue() - discount);

            } else {
                // 税抜額から割引
                pr.setSubtotal(ia.getTotal(0).getValue() - ia.getTotal(1).getValue());
            }
            pr.setSubtotalInTax(ia.getTotal(2).getValue() + ia.getTotal(3).getValue());
        }
        if (pr.isEmpty()) {
            return;
        }
        pr.setTax(ia.getTotal(4).getValue());
        pr.setSumtotal(ia.getTotal(3).getValue());
        pr.setAllDiscount(-ia.getTotal(2).getValue());
        pr.setOutOfValue(ia.getPaymentTotal());
        pr.setChangeValue(ia.getTotal(5).getValue());
        pr.setStaff(ia.getSales().getPayment(0).getStaff());
        pr.setChargeStaff(ia.getSales().getStaff());
        pr.setMessage(this.message.getText());
        pr.setSlipNo(ia.getSales().getSlipNo());

        long cashValue = 0l;
        long cardValue = 0l;
        long eCashValue = 0l;
        long giftValue = 0l;
        for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
            if (dpd.getPaymentMethod() == null) {
                continue;
            }

            switch (dpd.getPaymentMethod().getPaymentClassID().intValue()) {
                case 1:
                    cashValue += dpd.getPaymentValue();
                    break;
                case 2:
                    cardValue += dpd.getPaymentValue();
                    // Start Edit 20130516 nakhoa
                    // pr.setCardTitle(dpd.getPaymentMethod().getPaymentMethodName());
                    StringBuilder cardTitle = new StringBuilder();
                    for (String cardNama : ia.getSales().getCardName()) {
                        cardTitle.append(cardNama);
                        break;
                    }
                    pr.setCardTitle(cardTitle.toString());
                    // End Edit 20130516 nakhoa
                    break;
                case 3:
                    eCashValue += dpd.getPaymentValue();
                    pr.seteCashTitle(dpd.getPaymentMethod().getPaymentMethodName());
                    break;
                case 4:
                    giftValue += dpd.getPaymentValue();
                    pr.setGiftTitle(dpd.getPaymentMethod().getPaymentMethodName());
                    break;
            }
        }
        pr.setCashValue(cashValue);
        pr.setCardValue(cardValue);
        pr.setECashValue(eCashValue);
        pr.setGiftValue(giftValue);

        // ポイント情報
        if ((SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 1 || SystemInfo.getPointOutputType() == 2))) {
            if (CheckUtil.isNumber(txtPrevPoint.getText())) {
                pr.setPrevPoint(Integer.valueOf(txtPrevPoint.getText()));
            }
            if (CheckUtil.isNumber(txtAddPoint.getText())) {
                pr.setAddPoint(Integer.valueOf(txtAddPoint.getText()));
            }
            if (CheckUtil.isNumber(txtUsePoint.getText())) {
                pr.setUsePoint(Integer.valueOf(txtUsePoint.getText()));
            }
            if (CheckUtil.isNumber(txtTotalPoint.getText())) {
                pr.setTotalPoint(Integer.valueOf(txtTotalPoint.getText()));
            }
        }
        //VTAn Start add 20140612
        if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals(DATABASE_NAME) || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
            pr.setPrevAmount(Long.valueOf(txtPrevRemainValue.getText()));
            pr.setAddAmount(Long.valueOf(txtSalesValue.getText()));
            pr.setUseAmount(Long.valueOf(txtUseValue.getText()));
            pr.setTotalAmount(Long.valueOf(txtNowRemainValue.getText()));
        }
        //VtAn end add 20140612

        switch (pr.getReceiptSetting().getReceiptSize()) {
            case 0:
                pr.setReceiptType(PrintReceipt.ReceiptType.LARGE);
                break;
            case 1:
                pr.setReceiptType(PrintReceipt.ReceiptType.NORMAL);
                break;
        }

        pr.setPrintCustomer(chkPrintCustomer.isSelected());

        pr.print();

    }

    private void printReceiptDigestion(PrintReceiptDigestion prd) {

        prd.setCustomer(ia.getSales().getCustomer());
        prd.setStaff(ia.getSales().getPayment(0).getStaff());

        for (DataSalesDetail dsd : ia.getSales()) {

            if (dsd.getConsumptionCourse() != null) {
                // if (dsd.getConsumptionCourse().getContractNo() != null) {

                DataContractDigestion dcd = new DataContractDigestion(dsd);
                System.out.println("伝票番号[" + dcd.getSlipNo() + "]");
                setDigestionReceipt(dsd, prd, dcd.getSlipNo());
                //  }
            }
        }

        if (flagDisgtion == 1) {

            prd.setMessage(message.getText());
            switch (prd.getReceiptSetting().getReceiptSize()) {
                case 0:
                    prd.setReceiptType(PrintReceiptDigestion.ReceiptType.LARGE);
                    break;
                case 1:
                    prd.setReceiptType(PrintReceiptDigestion.ReceiptType.NORMAL);
                    break;
            }

            prd.print();
        }

    }

    private void updatePreviewPoint() {

        Integer NowPoint = 0;
        Integer AddPoint = 0;
        Integer UsePoint = 0;

        if (CheckUtil.isNumber(txtPrevPoint.getText())) {
            NowPoint = Integer.valueOf(txtPrevPoint.getText());
        }
        if (CheckUtil.isNumber(txtAddPoint.getText())) {
            AddPoint = Integer.valueOf(txtAddPoint.getText());
        }
        if (CheckUtil.isNumber(txtUsePoint.getText())) {
            UsePoint = Integer.valueOf(txtUsePoint.getText());
        }
        Integer TotalPoint = NowPoint + AddPoint - UsePoint;
        if (TotalPoint == null) {
            txtTotalPoint.setText("");
        } else {
            txtTotalPoint.setText(String.valueOf(TotalPoint));
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setFontSize(attr, 18);
            StyleConstants.setBold(attr, true);
        }
    }

    private void registPoint() {

        long Supplied = 0;
        long Use = 0;
        if (CheckUtil.isNumber(txtAddPoint.getText())) {
            Supplied = Integer.valueOf(txtAddPoint.getText());
        }
        if (CheckUtil.isNumber(txtUsePoint.getText())) {
            Use = Integer.valueOf(txtUsePoint.getText());
        }

        if (point != null) {
            // 既存データの更新を行う
            this.point.setUsePoint(Use);
            this.point.setSuppliedPoint(Supplied);
            this.point.setCustomerId(ia.getSales().getCustomer().getCustomerID());
            if (!this.point.update()) {
                return;
            }
        } else {
            // ポイント増減があるときのみ、ポイントを登録
            if (PointData.getPointData(ia.getShop().getShopID(), ia.getSales().getSlipNo()) == null) {
                if (Supplied > 0 || Use > 0) {
                    // 新規登録
                    PointData data = new PointData(
                            ia.getShop().getShopID(),
                            ia.getSales().getCustomer().getCustomerID(),
                            ia.getSales().getSlipNo(),
                            ia.getSales().getPayment(0).getPaymentNo(),
                            Use,
                            Supplied);

                    if (!data.insertPointCalculation()) {
                        return;
                    }
                }
            } else {
                // 既存データの更新を行う
                PointData data = new PointData(
                        ia.getShop().getShopID(),
                        ia.getSales().getCustomer().getCustomerID(),
                        ia.getSales().getSlipNo(),
                        ia.getSales().getPayment(0).getPaymentNo(),
                        Use,
                        Supplied);
                if (!data.update()) {
                    return;
                }
            }
        }
    }

    public void setDiscountUsePoint(long discountUsePoint) {
        this.discountUsePoint = discountUsePoint;
    }

    private void reservationMenuUpdate(ConnectionWrapper con) {

        //Luc start edit 20151002 #43006
        //if (!SystemInfo.getCurrentShop().isReservationMenuUpdate()) {
        if (!ia.getShop().isReservationMenuUpdate()) {
        //Luc start edit 20151002 #43006
            return;
        }

        try {

            // 精算データに技術メニューが存在する場合のみ予約データを上書きする
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select count(*) as cnt");
            sql.append("   from data_sales_detail");
            sql.append("  where shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append("    and slip_no = " + SQLUtil.convertForSQL(ia.getSales().getSlipNo()));
            sql.append("    and product_division = 1");
            sql.append("    and delete_date is null");
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                if (rs.getInt("cnt") == 0) {
                    return;
                }
            }

            // 予約No.を取得する
            int reservationNo = 0;
            sql.setLength(0);
            sql.append(" select");
            sql.append("     reservation_no");
            sql.append(" from");
            sql.append("     data_reservation");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append("     and slip_no = " + SQLUtil.convertForSQL(ia.getSales().getSlipNo()));
            sql.append("     and delete_date is null");
            sql.append(" limit 1");
            rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                reservationNo = rs.getInt("reservation_no");
            }

            // 開始時間を取得する
            Calendar cal = Calendar.getInstance();
            sql.setLength(0);
            sql.append(" select");
            sql.append("     min(reservation_datetime) as reservation_datetime");
            sql.append(" from");
            sql.append("     data_reservation_detail");
            sql.append(" where");
            sql.append("         delete_date is null");
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append("     and reservation_no = " + reservationNo);
            rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                cal.setTime(rs.getTimestamp("reservation_datetime"));
            }

            // トランザクション開始
            con.begin();

            // 既存の予約データ削除
            sql.setLength(0);
            sql.append(" delete from data_reservation_detail");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append("     and reservation_no = " + reservationNo);
            con.executeUpdate(sql.toString());

            // 予約データ登録
            sql.setLength(0);
            sql.append(" select");
            sql.append("      shop_id");
            sql.append("     ," + reservationNo + " as reservation_no");
            sql.append("     ,slip_detail_no");
            sql.append("     ,product_division");
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             count(*)");
            sql.append("         from");
            sql.append("             data_sales_detail");
            sql.append("         where");
            sql.append("                 shop_id = dsd.shop_id");
            sql.append("             and slip_no = dsd.slip_no");
            sql.append("             and slip_detail_no <= dsd.slip_detail_no");
            sql.append("      ) as reservation_detail_no");
            sql.append("     ," + SQLUtil.convertForSQL(cal) + "::timestamp +");
            sql.append("         cast(");
            sql.append("                 (");
            sql.append("                     select");
            sql.append("                         coalesce(sum(case a.product_division when 1 then b.operation_time when 6 then c.operation_time end), 0)");
            sql.append("                     from");
            sql.append("                         data_sales_detail a");
            sql.append("                             left join mst_technic b");
            sql.append("                                     on a.product_id = b.technic_id");
            sql.append("                             left join mst_course c");
            sql.append("                                     on a.product_id = c.course_id");
            sql.append("                     where");
            sql.append("                             a.product_division in (1,6) ");
            sql.append("                         and a.shop_id = dsd.shop_id");
            sql.append("                         and a.slip_no = dsd.slip_no");
            sql.append("                         and a.slip_detail_no < dsd.slip_detail_no");
            sql.append("                 )");
            sql.append("         || ' minutes' as interval) as reservation_datetime");
            sql.append("     ,product_id as technic_id");
            sql.append("     ,designated_flag");
            sql.append("     ,staff_id");
            sql.append("     ,current_timestamp as insert_date");
            sql.append("     ,current_timestamp as update_date");
            sql.append("     ,null as delete_date");
            //nhanvt start edit 20150310 Bug #35462
            //sql.append("     ,(");
            //sql.append("         select");
            //sql.append("             operation_time");
            //sql.append("         from");
            //sql.append("             mst_technic");
            //sql.append("         where");
            //sql.append("             technic_id = dsd.product_id");
            //sql.append("      ) as operation_time");
            
           sql.append("      ,(  ");
           sql.append("      case ");
           sql.append("              when dsd.product_division = 6 then mc.operation_time ");
           sql.append("              when dsd.product_division = 1 then  mt.operation_time ");
           sql.append("              else 0		");
           sql.append("      end ");
           sql.append("      )              AS operation_time ");
           sql.append("      ,(  ");
           sql.append("      case ");
           sql.append("              when dsd.product_division = 6 then 2 ");
           sql.append("              else null		");
           sql.append("      end ");


           sql.append("      )              AS course_flg, ");
           sql.append("      contract_no,   ");
           sql.append("      contract_detail_no,   ");
           sql.append("      contract_shop_id   ");
            
            sql.append(" from");
            sql.append("     data_sales_detail dsd");
            sql.append("     left join mst_technic mt on mt.technic_id = dsd.product_id ");
            sql.append("     left join mst_course mc on mc.course_id = dsd.product_id ");
            sql.append(" where");
            sql.append("         dsd.shop_id = " + SQLUtil.convertForSQL(ia.getShop().getShopID()));
            sql.append("     and dsd.slip_no = " + SQLUtil.convertForSQL(ia.getSales().getSlipNo()));
            sql.append("     and dsd.product_division in(1,6)");
            sql.append("     and dsd.delete_date is null");
            sql.append(" order by");
            sql.append("     dsd.slip_detail_no");
            //nhanvt end edit 20150310 Bug #35462
            int reservationDetailNo = 1;

            rs = con.executeQuery(sql.toString());
            while (rs.next()) {

                int bedId = 0;
                for (int i = 0; i < ia.getSales().size(); i++) {
                    if (ia.getSales().get(i).getSlipDetailNo().intValue() == rs.getInt("slip_detail_no")) {
                        if (ia.getSales().get(i).getBed().getBedID() != null) {
                            bedId = ia.getSales().get(i).getBed().getBedID().intValue();
                            break;
                        }
                    }
                }

                sql.setLength(0);
                sql.append(" insert into data_reservation_detail (");
                sql.append("      shop_id");
                sql.append("     ,reservation_no");
                sql.append("     ,reservation_detail_no");
                sql.append("     ,reservation_datetime");
                sql.append("     ,technic_id");
                sql.append("     ,bed_id");
                sql.append("     ,designated_flag");
                sql.append("     ,staff_id");
                sql.append("     ,insert_date");
                sql.append("     ,update_date");
                sql.append("     ,operation_time");
                if(rs.getInt("product_division") == 6){
                    sql.append("     ,course_flg");
                    sql.append("     ,contract_no");
                    sql.append("     ,contract_detail_no");
                    sql.append("     ,contract_shop_id");
                }
                
                sql.append(" ) values (");
                sql.append(SQLUtil.convertForSQL(ia.getShop().getShopID()));
                sql.append("," + reservationNo);
                sql.append("," + reservationDetailNo++);
                sql.append("," + SQLUtil.convertForSQL(rs.getTimestamp("reservation_datetime")));
                sql.append("," + rs.getInt("technic_id"));
                sql.append("," + bedId);
                sql.append("," + rs.getBoolean("designated_flag"));
                sql.append("," + rs.getInt("staff_id"));
                sql.append(",current_timestamp");
                sql.append(",current_timestamp");
                
                sql.append("," + rs.getInt("operation_time"));
                if(rs.getInt("product_division") == 6){
                    sql.append("," + rs.getInt("course_flg"));
                    sql.append("," + rs.getInt("contract_no"));
                    sql.append("," + rs.getInt("contract_detail_no"));
                    sql.append("," + rs.getInt("contract_shop_id"));
                }
                
                sql.append(" )");
                con.executeUpdate(sql.toString());
               
            }

            con.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ignore) {
            }
        }

    }

    /**
     * @return the customerDisplay
     */
    public CustomerDisplay getCustomerDisplay() {
        return customerDisplay;
    }

    /**
     * @param customerDisplay the customerDisplay to set
     */
    public void setCustomerDisplay(CustomerDisplay customerDisplay) {
        this.customerDisplay = customerDisplay;
    }

    public PrintReceiptDigestion setDigestionReceipt(DataSalesDetail dsd, PrintReceiptDigestion pr, Integer slipNo) {
        // if (dsd.getConsumptionCourse().getContractNo() != null) {
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            // vtbphuong start change 20140626 Bug #26172
            // ResultSetWrapper rs = con.executeQuery(pr.getReceiptDigestionSQL(dsd.getConsumptionCourse().getContractNo(), slipNo, dsd.getConsumptionCourse().getContractDetailNo()));
            ResultSetWrapper rs = con.executeQuery(pr.getReceiptDigestionSQL(dsd.getConsumptionCourse().getContractNo(), slipNo, dsd.getConsumptionCourse().getContractDetailNo(), dsd.getContractShopId(), dsd.getShop().getShopID()));
            // vtbphuong end change 20140626 Bug #26172

            while (rs.next()) {
                flagDisgtion = 1;
                String staffName = "";
                if ( dsd.getConsumptionCourse().getContractNo() == null) {
                    staffName = rs.getString("staff_name1") == null ? "" : rs.getString("staff_name1") + " " + rs.getString("staff_name2") == null ? "" : rs.getString("staff_name2") ;
                }else {
                    staffName = rs.getString("staff_name") == null ? "" : rs.getString("staff_name");
                }
                pr.add(new ReceiptDigestionData(
                        rs.getString("product_name"),
                        rs.getInt("co_product_num"),
                        rs.getInt("cd_product_num"),
                       // rs.getString("staff_name1") == null ? "" : rs.getString("staff_name1"), rs.getString("staff_name2") == null ? "" : rs.getString("staff_name2"),
                        // rs.getString("staff_name") == null ? "" : rs.getString("staff_name"),
                        staffName,
                        rs.getInt("product_sum"),
                        rs.getTimestamp("valid_date")));
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        }

        //  }
        return pr;
    }
}

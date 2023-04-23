/*
 * ContractChangePanel.java
 *
 * Created on 2013/03/20, 20:03
 */
package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.customerDisplay.CustomerDisplay;
import com.geobeck.sosia.pos.account.*;
import com.geobeck.sosia.pos.basicinfo.*;
import com.geobeck.sosia.pos.basicinfo.customer.*;
import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.data.account.DataPaymentDetail;
import com.geobeck.sosia.pos.hair.data.account.*;
import com.geobeck.sosia.pos.hair.data.company.*;
import com.geobeck.sosia.pos.hair.data.course.DataContract;
import com.geobeck.sosia.pos.hair.data.course.DataContractDigestion;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.company.*;
import com.geobeck.sosia.pos.hair.pointcard.PointData;
import com.geobeck.sosia.pos.hair.search.product.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.products.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.swing.table.BevelBorderHeaderRenderer;
import com.geobeck.util.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.*;
import javax.comm.PortInUseException;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import jp.co.flatsoft.fscomponent.*;

/**
 *
 * @author ivs
 */
public class ContractCanCelPanel extends AbstractImagePanelEx implements SearchHairProductOpener {

    /**
     * 行の高さ
     */
    private final int ROW_HEIGHT = 27;
    /**
     * 列変数
     */
    private int staffCol = 0;       //担当者
    private int designatedCol = 0;  //指名
    private int approachedCol = 0;  //アプローチ       
    private ArrayList<DataContract> arrDS = new ArrayList<DataContract>();
    private DataContract dContract = new DataContract();
    private DataContractDigestion dContractDigestion = new DataContractDigestion();
    private int CusID;
    private int slipNo;
    private int contractNo;
    private int contractDeatailNo;
    private int slipNoNew;
    private int shopID;
    private double totalCourseBefore = 0;
    private double totalProductBefore = 0;
    private double totalCourseAfter = 0;
    private double totalProductAfter = 0;
    private double serviceCharge = 0;
    private Double totalRequestValue = 0d;
    /**
     * レスポンスマスタ
     */
    private MstResponses mrs = new MstResponses();
    private MstShop targetShop = null;
    private ContractChangeAccount ia = new ContractChangeAccount();
    // 伝票詳細登録数
    private int detailCount = 0;
    //指名:true or フリー：false 指名フリー状態保持
    private boolean shimeiFreeFlag = false;
    //レーシートボタン対応 出力:true 出力しない:false レシート出力状態保持
    private boolean printReceiptFlsg = false;
    private CustomerDisplay customerDisplay = null;
     private MstCustomer customer = new MstCustomer();
     private boolean flag = false;
    /**
     * 伝票入力画面用FocusTraversalPolicy
     */
    private InputAccountFocusTraversalPolicy ftp =
            new InputAccountFocusTraversalPolicy();

    /**
     * Creates new form HairInputAccountPanel
     */
    public ContractCanCelPanel(Integer CusID, Integer contractNo, Integer contractDeatailNo, Integer ShopID) {
        super();
        this.CusID = CusID;
        // this.slipNo = slipNo;
        this.contractNo = contractNo;
        this.contractDeatailNo = contractDeatailNo;
        this.shopID = ShopID;
        targetShop = SystemInfo.getCurrentShop();

        initComponents();
        addMouseCursorChange();
        this.setSize(845, 500);
        this.setTitle("解約画面");
        this.setListener();

        initResponse();
        this.init();

        ia.setShop(targetShop);
        this.loadReceiptSetting();
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            ArrayList cusArray = ia.getMstCustomerArrayByNo(con, CusID);
            MstCustomer customer = new MstCustomer();
            customer = (MstCustomer) cusArray.get(0);
            CusNameLabel.setText(customer.getFullCustomerName());
            CusNoLabel.setText(customer.getCustomerNo());
            ia.getSales().setCustomer(customer);
        } catch (Exception e) {
        }

    }
    
      public ContractCanCelPanel(MstCustomer cus, DataContract dc) {
        super();
        this.dContract = dc;
        this.customer = cus;
        targetShop = SystemInfo.getCurrentShop();

        initComponents();
        addMouseCursorChange();
        this.setSize(845, 500);
        this.setTitle("解約画面");
        this.setListener();
        this.init();

        ia.setShop(targetShop);
        CusNameLabel.setText(customer.getFullCustomerName());
        CusNoLabel.setText(customer.getCustomerNo());
        ia.getSales().setCustomer(cus);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        charge = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        registPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        slipNoLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        salesDate = new FSCalenderCombo(SystemInfo.getSystemDate());
        staff = new javax.swing.JComboBox();
        staffNo = new javax.swing.JTextField();
        registButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        alertMarkLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        CusNameLabel = new com.geobeck.swing.JFormattedTextFieldEx();
        CusNoLabel = new com.geobeck.swing.JFormattedTextFieldEx();
        jPanel7 = new javax.swing.JPanel();
        paymentsScrollPane = new javax.swing.JScrollPane();
        payments = new com.geobeck.swing.JTableEx();
        jLabel17 = new javax.swing.JLabel();
        responseItemLabel8 = new javax.swing.JLabel();
        responseItemLabel9 = new javax.swing.JLabel();
        responseItemLabel13 = new javax.swing.JLabel();
        responseItemLabel14 = new javax.swing.JLabel();
        totalCourseLabel1 = new javax.swing.JLabel();
        requestValueText123 = new com.geobeck.swing.JFormattedTextFieldEx();
        serviceChargeText = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)serviceChargeText.getDocument()).setDocumentFilter(
            new CustomFilter(12, CustomFilter.NUMBER));
        beforeTotalCourseLabel = new com.geobeck.swing.JFormattedTextFieldEx();
        productsScrollPane1 = new javax.swing.JScrollPane();
        contractTable = new com.geobeck.swing.JTableEx();
        jLabel9 = new javax.swing.JLabel();
        totalCourseLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(246, 246, 246));
        setFocusCycleRoot(true);

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

        slipNoLabel.setText("解約日");

        jLabel2.setText("担当者");

        salesDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        salesDate.setPreferredSize(new java.awt.Dimension(88, 20));
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

        staff.setMaximumRowCount(20);
        staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffActionPerformed(evt);
            }
        });

        staffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffNoActionPerformed(evt);
            }
        });
        staffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                staffNoFocusLost(evt);
            }
        });

        registButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.setBorderPainted(false);
        registButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        alertMarkLabel.setBackground(new java.awt.Color(255, 140, 180));
        alertMarkLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alertMarkLabel.setPreferredSize(new java.awt.Dimension(39, 20));

        jLabel1.setText("会員No. ");

        CusNameLabel.setEditable(false);
        CusNameLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        CusNameLabel.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        CusNoLabel.setEditable(false);
        CusNoLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        CusNoLabel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(alertMarkLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(321, 321, 321))
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(slipNoLabel)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(salesDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(CusNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(CusNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(staffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(43, 43, 43)))
                .add(121, 121, 121)
                .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {deleteButton, registButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(slipNoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(salesDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(CusNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(CusNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(staffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(alertMarkLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .add(60, 60, 60))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {deleteButton, registButton}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(null);

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
        model.getColumn(2).setCellEditor(new IntegerCellCustomEditor(new JTextField()));
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
        paymentsScrollPane.setBounds(490, 40, 315, 135);

        jLabel17.setFont(new java.awt.Font("HGPｺﾞｼｯｸE", 0, 18)); // NOI18N
        jLabel17.setText("＜お預り内訳＞");
        jPanel7.add(jLabel17);
        jLabel17.setBounds(490, 16, 130, 24);

        responseItemLabel8.setText("解約コース");
        jPanel7.add(responseItemLabel8);
        responseItemLabel8.setBounds(270, 50, 60, 22);

        responseItemLabel9.setText("（マイナスの場合は返金）");
        jPanel7.add(responseItemLabel9);
        responseItemLabel9.setBounds(270, 150, 140, 22);

        responseItemLabel13.setText("請求金額");
        jPanel7.add(responseItemLabel13);
        responseItemLabel13.setBounds(270, 130, 60, 22);

        responseItemLabel14.setText("変更手数料");
        jPanel7.add(responseItemLabel14);
        responseItemLabel14.setBounds(270, 80, 60, 22);

        totalCourseLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalCourseLabel1.setPreferredSize(new java.awt.Dimension(77, 19));
        jPanel7.add(totalCourseLabel1);
        totalCourseLabel1.setBounds(690, 0, 77, 19);

        requestValueText123.setEditable(false);
        requestValueText123.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        requestValueText123.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        requestValueText123.setText("0");
        jPanel7.add(requestValueText123);
        requestValueText123.setBounds(360, 130, 90, 19);

        serviceChargeText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        serviceChargeText.setColumns(3);
        serviceChargeText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        serviceChargeText.setText("0");
        serviceChargeText.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                serviceChargeTextCaretUpdate(evt);
            }
        });
        serviceChargeText.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                serviceChargeTextPropertyChange(evt);
            }
        });
        jPanel7.add(serviceChargeText);
        serviceChargeText.setBounds(360, 80, 90, 20);

        beforeTotalCourseLabel.setEditable(false);
        beforeTotalCourseLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        beforeTotalCourseLabel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        beforeTotalCourseLabel.setText("0");
        jPanel7.add(beforeTotalCourseLabel);
        beforeTotalCourseLabel.setBounds(360, 50, 90, 20);

        productsScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        productsScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        productsScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        contractTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "変更", "区分", "コース/商品名", "契約金額", "消化済金額", "差引金額", "消化/契約消化", "有効期限", "担当者", "staffID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        contractTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        contractTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
        contractTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        contractTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(contractTable, SystemInfo.getTableHeaderRenderer());
        contractTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        //SelectTableCellRenderer.setSelectTableCellRenderer(courseAfterTable);
        contractTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contractTableFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                contractTableFocusLost(evt);
            }
        });
        contractTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                contractTableMouseReleased(evt);
            }
        });
        contractTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                contractTablePropertyChange(evt);
            }
        });
        contractTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contractTableKeyPressed(evt);
            }
        });
        productsScrollPane1.setViewportView(contractTable);
        contractTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (contractTable.getColumnModel().getColumnCount() > 0) {
            contractTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            contractTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            contractTable.getColumnModel().getColumn(9).setMinWidth(0);
            contractTable.getColumnModel().getColumn(9).setPreferredWidth(0);
            contractTable.getColumnModel().getColumn(9).setMaxWidth(0);
        }

        jLabel9.setText("コース契約金額");
        jLabel9.setPreferredSize(new java.awt.Dimension(77, 19));

        totalCourseLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalCourseLabel.setPreferredSize(new java.awt.Dimension(77, 19));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("円");

        jLabel29.setText("契約コース");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel29)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(totalCourseLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(productsScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 802, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(registPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(154, 154, 154)
                        .add(registPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(17, 17, 17)
                        .add(jLabel29)
                        .add(4, 4, 4)
                        .add(productsScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(totalCourseLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(1, 1, 1)
                        .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 明細の列を初期化する。
     */
    private void initProductsColumn() {
        //列の幅を設定する。
    }

    /**
     * 売上明細を初期化する。
     */
    private void initProducts() {
        pointIndexList.clear();

        detailCount = 0;
    }

	private void paymentsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentsFocusGained
            if (payments.getInputContext() != null) {
                payments.getInputContext().setCharacterSubsets(null);
            }
	}//GEN-LAST:event_paymentsFocusGained

	private void paymentsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_paymentsPropertyChange
          

            this.changePaymentValue();
	}//GEN-LAST:event_paymentsPropertyChange

	private void paymentsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentsKeyPressed

            if (payments.getSelectedRow() == 0
                    && payments.getSelectedColumn() == 0
                    && (evt.getKeyCode() == evt.VK_ENTER || evt.getKeyCode() == evt.VK_TAB)
                    && (evt.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
                //先頭行の先頭列かつ、
                //Enterキー or TabキーとShiftキーが押下された場合
                //レジ担当者へ
                staffNo.requestFocusInWindow();
            }
            if (payments.getSelectedRow() == payments.getRowCount() - 1
                    && payments.getSelectedColumn() == 2
                    && (evt.getKeyCode() == evt.VK_ENTER || evt.getKeyCode() == evt.VK_TAB)
                    && (evt.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == 0) {
                //最終行の最終列かつ、
                //Enterキー or Tabキーが押下された場合(Shiftキーの押下はなし)
                //精算ボタンへ
            }

	}//GEN-LAST:event_paymentsKeyPressed

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

    private void contractTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contractTableMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_contractTableMouseReleased

    private void contractTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contractTableFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_contractTableFocusGained

    private void contractTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contractTableFocusLost
    }//GEN-LAST:event_contractTableFocusLost

    private void contractTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_contractTablePropertyChange
        // TODO add your handling code here:
        this.setTotal();
    }//GEN-LAST:event_contractTablePropertyChange

    private void contractTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contractTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_contractTableKeyPressed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_deleteButtonActionPerformed

    /*
     * 後始末
     */
    public void dispose() {
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).dispose();
        }
    }

    private void registButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registButtonActionPerformed
        try {
            if ( payments.getCellEditor() != null) {
                payments.getCellEditor().stopCellEditing();
            }
            regist();
        } catch (SQLException ex) {
            Logger.getLogger(ContractCanCelPanel.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_registButtonActionPerformed

    private void regist() throws SQLException {
        if (!this.checkInput()) {
            return;
        }
        boolean flag = false;
        for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
//            if (dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPaymentClass() != null) {
//
//                Integer id = dpd.getPaymentMethod().getPaymentClassID();
//
//                // カードまたは電子マネーの場合
//                if (id.equals(2) || id.equals(3)) {
//                    //cardPayment += dpd.getPaymentValue().longValue();
//                }
//            }
             // vtbphuong start change 20140703 Request #26445
            if (dpd != null) {
                if (dpd.getPaymentMethod() == null && (dpd.getPaymentValue() !=0 )) {                    
//                         dpd.setPaymentMethod(ia.getPaymentClasses().get(0).get(0)); 
                    flag  = true;
                    MessageDialog.showMessageDialog(
                    this,
                    "支払方法を選択してください。",
                    this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    return ;
                     
                }
            }
             // vtbphuong end change 20140703 Request #26445
        }
        if(!flag){
            DataPaymentDetail dpd = ia.getSales().getPayment(0).get(0);
            dpd.setPaymentMethod(ia.getPaymentClasses().get(0).get(0));
           // dpd.setPaymentValue(((Double)totalRequestValue).longValue());
        }
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
//        //コネクションを取得

            con.begin();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (MessageDialog.showYesNoDialog(this,
                    "該当の契約を削除します。本当によろしいですか。",
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
            DefaultTableModel contractModel = (DefaultTableModel) contractTable.getModel();

            for (int i = 0; i < contractModel.getRowCount(); i++) {
                dContract = (DataContract) contractModel.getValueAt(i, 9);
                if ((contractModel.getValueAt(i, 0).toString()).equals("true")) {
                    dContractDigestion.setSlipNo(dContract.getSlipNo());
                    dContractDigestion.setContractNo(dContract.getContractNo());
                    dContractDigestion.setContractDetailNo(dContract.getContractDetailNo());
                    dContractDigestion.setProductNum(dContract.getProductNum1());
                    dContract.setContractStatus(dContract.getContractStatus());
                    // dContract.UpdateContractStatus(con); 

                    //Luc start add 20140514
                    ia.getSales().setShop(dContract.getShop());
                    
                    DataSalesDetail d = new DataSalesDetail();
                    d.setShop(ia.getShop());
                    d.setSlipNo(slipNo);
                    d.setProduct(8, dContract.getProduct());//コース解約
                    // vtbphuong start change 20140617 Bug #25282
//                    d.setProductNum(dContract.getProductNum()-dContract.getProductNum1().intValue());
//                    d.setProductValue(-(dContract.getProductValue()/dContract.getProductNum())*(dContract.getProductNum()-dContract.getProductNum1().intValue()));

                    d.setProductNum(1);
                    //Luc start edit 20140619
                    //d.setProductValue(Long.parseLong(beforeTotalCourseLabel.getText().toString().replace(",", "")) *(-1));
                    Double productValue = (Double)contractModel.getValueAt(i, 5);
                    d.setProductValue(-productValue.longValue());
                    d.setContractShopId(dContract.getShop().getShopID());
                    d.setContractNo(dContract.getContractNo());
                    d.setContractDetailNo(dContract.getContractDetailNo());
                    //IVS_LVTU start add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
                    d.setStaff(dContract.getStaff());
                    //IVS_LVTU end add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
                    //Luc end edit 20140619
                    // vtbphuong end change 20140617 Bug #25282
                    d.setTaxRate(dContract.getTaxRate());
                    ia.getSales().add(d);
                    
                    //Luc end add 20140514
                    
                    // vtbphuong start change 20140616 Bug #25288
                     if (dContract.isExitsContract(con)) {
                         con.executeUpdate(dContract.getUpdateDataContractLogicallySQL());
                    }
                    //IVS_LVTU start delete 2017/08/07 #21214 [gb] 解約処理 契約と同一伝票内で消化がある契約を解約すると消化が全て論理削除される
//                    if (dContract.isExitsContractDigestion(con)) {
//                         con.executeUpdate(dContract.getUpdateDataContractDigestionSQL());
//                     }
                    //IVS_LVTU end delete 2017/08/07 #21214 [gb] 解約処理 契約と同一伝票内で消化がある契約を解約すると消化が全て論理削除される
                   
                    }
                    
             }

//            if (dContract.isExitsContract(con)) {
//                con.executeUpdate(dContract.getUpdateDataContractLogicallySQL());
//
//            }
//            if (dContract.isExitsContractDigestion(con)) {
//                con.executeUpdate(dContract.getUpdateDataContractDigestionSQL());
//            }
            // vtbphuong end change 20140616 Bug #25288
            Double serviceCharg = 0d;
            try {
                serviceCharg = Double.parseDouble(serviceChargeText.getText());
            } catch (Exception e) {
            }
            if (serviceCharg > 0) {
                DataSalesDetail d = new DataSalesDetail();
                d = new DataSalesDetail();
                d.setShop(ia.getShop());
                d.setSlipNo(slipNo);
                Product p = new Product();
                p.setProductID(-2);
                d.setProduct(9, p);//コース解約
                d.setProductNum(1);
                d.setProductValue(serviceCharg.longValue());
                //IVS_LVTU start add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
                d.setStaff(ia.getSales().getStaff());
                //IVS_LVTU end add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
                d.setTaxRate(ia.getTaxRate());
                ia.getSales().add(d);
            }

            //売上明細テーブルにデーだを登録する。
            ia.getSales().setNewSlipNo(con);

            ia.getSales().regist(con); 
            for (DataSalesDetail dsd : ia.getSales()) {
                dsd.setSlipNo(ia.getSales().getSlipNo());
                dsd.setNewSlipDetailNo(con);

                dsd.regist(con);
            }
            // 支払テーブルにデータを登録する。

            Long paymentValue = 0l;
            for (DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
                try {
                    paymentValue += dpd.getPaymentValue();
                } catch (Exception e) {
                }
            }
            DataPayment dp = ia.hairSales.getPayment(0);
            dp.setShop(ia.getShop());
            dp.setSlipNo(ia.getSales().getSlipNo());
            dp.setTempFlag(false);
            dp.setBillValue(0L);
            // IVS start change 20140925 Bug #30952
//            dp.setPaymentDate(null);
//            Calendar cal = Calendar.getInstance();
//            dp.setPaymentDate(cal.getTime());
            dp.setPaymentDate(salesDate.getDate());
             // IVS end change 20140925 Bug #30952
            dp.registAll(con);

            con.commit();
            con.close();
            MessageDialog.showMessageDialog(this,
                    "解約しました。",
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
            //トランザクションコミット


        } catch (SQLException e) {

            MessageDialog.showMessageDialog(this,
                    "更新不成功",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            con.rollback();
            con.close();
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.setVisible(false);
            this.dispose();
        }
    }
    private void salesDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesDateFocusLost
        ia.getSales().setSalesDate(salesDate.getDate());
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
    }//GEN-LAST:event_salesDateFocusLost

    private void salesDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesDateFocusGained
        if (salesDate.getInputContext() != null) {
            salesDate.getInputContext().setCharacterSubsets(null);
        }
    }//GEN-LAST:event_salesDateFocusGained

    private void salesDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_salesDateItemStateChanged
        ia.getSales().setSalesDate(salesDate.getDate());
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
    }//GEN-LAST:event_salesDateItemStateChanged

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

    private void staffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staffNoFocusLost

        if (!staffNo.getText().equals("")) {

            MstStaff ms = new MstStaff();

            this.setStaff(staff, staffNo.getText());
            ms.setStaffID(((MstStaff) staff.getSelectedItem()).getStaffID());

        } else {

            staff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_staffNoFocusLost

    private void serviceChargeTextPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_serviceChargeTextPropertyChange
        setTotal();
    }//GEN-LAST:event_serviceChargeTextPropertyChange

    private void staffNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffNoActionPerformed
        // TODO add your handling code here:
        if (!staffNo.getText().equals("")) {

            MstStaff ms = new MstStaff();

            this.setStaff(staff, staffNo.getText());
            ms.setStaffID(((MstStaff) staff.getSelectedItem()).getStaffID());

        } else {

            staff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_staffNoActionPerformed

    private void serviceChargeTextCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_serviceChargeTextCaretUpdate
        //IVS_LVTu start add 2015/06/01 Bug #37075
        setTotal();
        //IVS_LVTu end add 2015/06/01 Bug #37075
    }//GEN-LAST:event_serviceChargeTextCaretUpdate
    private void setStaff(JComboBox staffCombo, String staffNo) {

        for (int i = 0; i < staffCombo.getItemCount(); i++) {

            MstStaff ms = (MstStaff) staffCombo.getItemAt(i);

            //空白をセット
            if (ms.getStaffID() == null) {

                staffCombo.setSelectedIndex(0);

            } else if (ms.getStaffNo().equals(staffNo)) {

                staffCombo.setSelectedIndex(i);
                return;
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.swing.JFormattedTextFieldEx CusNameLabel;
    private com.geobeck.swing.JFormattedTextFieldEx CusNoLabel;
    private javax.swing.JLabel alertMarkLabel;
    private com.geobeck.swing.JFormattedTextFieldEx beforeTotalCourseLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup charge;
    private com.geobeck.swing.JTableEx contractTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private com.geobeck.swing.JTableEx payments;
    private javax.swing.JScrollPane paymentsScrollPane;
    private javax.swing.JScrollPane productsScrollPane1;
    private javax.swing.JButton registButton;
    private javax.swing.JPanel registPanel;
    private com.geobeck.swing.JFormattedTextFieldEx requestValueText123;
    private javax.swing.JLabel responseItemLabel13;
    private javax.swing.JLabel responseItemLabel14;
    private javax.swing.JLabel responseItemLabel8;
    private javax.swing.JLabel responseItemLabel9;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo salesDate;
    private com.geobeck.swing.JFormattedTextFieldEx serviceChargeText;
    private javax.swing.JLabel slipNoLabel;
    private javax.swing.JComboBox staff;
    private javax.swing.JTextField staffNo;
    private javax.swing.JLabel totalCourseLabel;
    private javax.swing.JLabel totalCourseLabel1;
    // End of variables declaration//GEN-END:variables
    private boolean changeKeys = true;
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

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(registButton);
    }

    /**
     * コンポーネントの各リスナーをセットする。
     */
    private void setListener() {
        salesDate.addKeyListener(SystemInfo.getMoveNextField());
        salesDate.addFocusListener(SystemInfo.getSelectText());
        staff.addKeyListener(SystemInfo.getMoveNextField());
        staff.addFocusListener(SystemInfo.getSelectText());
        staffNo.addKeyListener(SystemInfo.getMoveNextField());
        staffNo.addFocusListener(SystemInfo.getSelectText());

//		products.addFocusListener(SystemInfo.getLostFocusEditingStopper());
//		discounts.addFocusListener(SystemInfo.getLostFocusEditingStopper());
//		payments.addFocusListener(SystemInfo.getLostFocusEditingStopper());

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

    public Integer getProductsIndex(int typeNo, int row) {
        Integer retInteger = null;
        JComboBox combo;
        if (row < 0) {
            return null;
        }


        return retInteger;
    }

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


        ia.getSales().get(addNo).setProductNum(ia.getSales().get(addNo).getProductNum() + 1);




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
                if (dsd.getConsumptionCourse().getSlipNo() == null) {
                    //契約番号がない場合（コース契約前）
                    if (consumptionCourse.getSlipNo() != null) {
                        continue;
                    }
                    //仮契約番号がない場合（コース契約済だが消化を予約した状態）
                    if (dsd.getTmpContractNo() == null) {
                        continue;
                    }

                    if (dsd.getTmpContractNo().equals(consumptionCourse.getTmpContractNo())) {
                        addNo = no;
                    }
                } else {
                    //契約番号がある場合（すでにコース契約済）
                    if (consumptionCourse.getSlipNo() == null) {
                        continue;
                    }

                    if ((dsd.getConsumptionCourse().getSlipNo().intValue() == consumptionCourse.getSlipNo().intValue())
                            && (dsd.getConsumptionCourse().getCourseId().intValue() == consumptionCourse.getCourseId().intValue())) {
                        addNo = no;
                    }
                }
            }
            no++;
        }

        // 新規の内容ならレコードを追加する

        ia.getSales().get(addNo).setProductNum(ia.getSales().get(addNo).getProductNum() + 1);



//                this.setTotalWithoutPayment();
        this.setTotal();
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

    private Course getSelectedCourse(JTable table) {
        int row = table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        return (Course) table.getValueAt(row, 0);
    }

    /**
     * 初期化処理を行う。
     */
    private void init() {
        this.setListener();
        ia.init();
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));

        this.initStaff();
        this.initDiscount();
        this.initPayments();
        this.initTotal();
        this.initProducts();
        //this.initProductClasses();
        this.loadContract();
        ia.getSales().setSalesDate(salesDate.getDate());
        registPanel.setVisible(this.getSelectedShopID() != 0);

        payments.getColumnModel().getColumn(3).setPreferredWidth(65);

        // 詳細登録数をクリアする
        detailCount = 0;

        salesDate.setEnabled(true);

//		shopLabel.setVisible(false);
//		shop.setVisible(false);
    }

    // start add 20130110 nakhoa お会計
        /*
     * Set メインメニュー
     */
    private void initIntegration() {



        SimpleMaster sm = new SimpleMaster(
                "",
                "mst_technic_integration",
                "technic_integration_id",
                "technic_integration_name", 0);

        sm.loadData();

    }

    /**
     * レジ担当者を初期化する。
     */
    private void initStaff() {
        staff.removeAllItems();

        for (MstStaff ms : ia.getStaffs()) {
            if (ms.isDisplay()) {
                staff.addItem(ms);
            }
        }

        staff.setSelectedIndex(0);
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
     * スタッフコンボを取得する
     */
    private JComboBox getStaffComboBox(Integer staffID) {
        JComboBox staffCombo = new JComboBox(ia.getStaffs().toArray());
        if (staffID.intValue() < 0) {
            staffID = new Integer(0);
        }
        staffCombo.setSelectedIndex(staffID);

        staffCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        //スタッフが変更されたときの処理を追加
        staffCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                setTotal();
            }
        });
        return staffCombo;
    }

    /**
     * 削除ボタンを取得する
     */
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
     * 売上明細の内容を変更したときの処理
     *
     */
    /**
     * 指定行の技術インデックスをデクリメントする
     */
    /**
     * 割引部の初期化処理を行う。
     */
    private void initDiscount() {

        JComboBox dCombo = new JComboBox();
        dCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        dCombo.addItem(new MstDiscount());
        for (MstDiscount temp : ia.getDiscounts()) {
            dCombo.addItem(temp);
        }

        //割引が変更されたときの処理を追加
        dCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                //changeDiscountValue();
            }
        });

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

        for (MstPaymentClass mpc : ia.getPaymentClasses()) {
            JComboBox classes = new JComboBox(new Object[]{mpc});
            classes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            Object methods = this.getPaymentMethodObject(mpc);

            Object[] rowData = {new JLabel(" " + mpc.toString()), methods, 0l, getCashInsertButton()};
            model.addRow(rowData);

            ia.getSales().getPayment(0).addPaymentDetail(mpc, (mpc.size() == 1 ? mpc.get(0) : null), 0l);
        }
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

            }
        });
        return button;
    }

    private void setBalance() {
//            long value = ((NameValue)ia.getTotal( 0 ) ).getValue().longValue();
//            if (value < 0) {
//                int row = payments.getSelectedRow();
//                long currentValue = Long.parseLong(payments.getValueAt(row, 2).toString());
//                payments.setValueAt(currentValue + Math.abs(value), row, 2);
//                payments.changeSelection(row, 2, false, false);
//                changePaymentValue();                
//            }
        //if (totalRequestValue > 0) {
            int row = payments.getSelectedRow();
            //long currentValue = Long.parseLong(payments.getValueAt(row, 2).toString());
            //Luc start edit 20160105 #45225
            //Double dcurrentValue = Double.valueOf(payments.getValueAt(row, 2).toString());
            //long currentValue = dcurrentValue.longValue();
            //payments.setValueAt(currentValue + totalRequestValue.longValue(), row, 2);
            //payments.changeSelection(row, 2, false, false);
            Double totalPayment = 0d;
            for(int i=0;i<payments.getRowCount();i++) {
                if(i!=row) {
                    try {
                        totalPayment += Double.valueOf(payments.getValueAt(i, 2).toString());
                    }catch (Exception e) {}
                }
            }
            Double remain = totalRequestValue -totalPayment;
            payments.setValueAt(remain.longValue() , row, 2);
            payments.changeSelection(row, 2, false, false);
            //Luc end edit 20160105 #45225
            changePaymentValue();
        //} 
    }

    @Override
    public void addSelectedCourse(Integer intgr, Course course) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    }

    private void setTotal() {

        //totalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseBefore));

        //Double totalAfter = 0.0;
        DefaultTableModel contractModel = (DefaultTableModel) contractTable.getModel();
        totalCourseBefore = 0;
        for (int i = 0; i < contractModel.getRowCount(); i++) {
            if ((contractTable.getValueAt(i, 0).toString()).equals("true")) {
                totalCourseBefore += Double.valueOf(contractModel.getValueAt(i, 5).toString());
            }
        }

        totalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseBefore));
        beforeTotalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseBefore));
        //IVS_LVTu start edit 2015/06/01 Bug #37075
        if (!"".equals(serviceChargeText.getText().trim())) {
            serviceCharge = Double.valueOf(serviceChargeText.getText());
        }else {
            serviceCharge = 0d;
        }
        //totalRequestValue = totalCourseAfter - totalCourseBefore + serviceCharge - ia.getPaymentTotal();
        totalRequestValue = totalCourseAfter - totalCourseBefore + serviceCharge;
        
        //IVS_LVTu end edit 2015/06/01 Bug #37075
        requestValueText123.setText(FormatUtil.decimalFormat(totalRequestValue));


    }

    /**
     * 支払金額が変更されたときの処理。
     */
    private void changePaymentValue() {
        if (payments.getCellEditor() != null) {
               payments.getCellEditor().stopCellEditing();
           }
        int row = payments.getSelectedRow();
        int col = payments.getSelectedColumn();

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

            if (dpd != null) {
                 // vtbphuong start change 20140703 Request #26445
//                if (dpd.getPaymentMethod() == null) {
//                    if (row < ia.getPaymentClasses().size()
//                            && ia.getPaymentClasses().get(row).size() > 0) {
//                       
//                       // dpd.setPaymentMethod(ia.getPaymentClasses().get(row).get(0));
//                         dpd.setPaymentMethod(ia.getPaymentClasses().get(0).get(0));            
//                    }
//                }
                 // vtbphuong end change 20140703 Request #26445

                dpd.setPaymentValue(value);
            }
           
            
        }
        
        this.setTotal();
    }

    /**
     * 売掛金をセットする。
     */
    public void setBill() {
        //売掛金を読み込む
        ia.loadBill(ia.getSales().getCustomer().getCustomerID());

    }

    /**
     * 伝票データを読み込む。
     *
     * @param slipNo 伝票No.
     */
    public void load(MstShop shop, Integer slipNo) {



        // ログイン店舗以外を閲覧する場合
        boolean isCurrentShop = false;
        isCurrentShop = isCurrentShop || SystemInfo.isGroup();
        isCurrentShop = isCurrentShop || targetShop.getShopID().equals(shop.getShopID());
        if (!isCurrentShop) {
            // 店舗コンボの内容を更新する
            targetShop = shop;
        }

        ia.load(shop, slipNo);

    }

    /**
     * 販売日をセットする。
     *
     * @param salesDate　販売日
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate.setDate(salesDate);
    }

    public void setReservationConsumptionCourseMap() {
        for (DataSalesDetail dsd : ia.getSales()) {
            if (dsd.getProductDivision() == 5) {
                //予約データにコース契約がある場合
                Course course = dsd.getCourse();
                //仮契約番号発行
                dsd.setTmpContractNo(generateTmpContractNo(course));
                //消化コースクラス・消化コースリストに追加。

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
     * SOSIA連動ダイアログを開く
     */
    private void changeSosiaGear() {
        SosiaGearDialog sgd = new SosiaGearDialog(ia.getSales().getCustomer());
        SwingUtil.openAnchorDialog(this.parentFrame, true, sgd, "SOSIA連動", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        sgd = null;

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

            return aComponent;
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
            return getFirstComp();
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
    
    public class IntegerCellCustomEditor extends DefaultCellEditor
    {
            JTextField textField = new JTextField();

            public IntegerCellCustomEditor(JTextField field)
            {
                    super(field);
    //		this.setClickCountToStart(0);
                    this.textField = field;
                    textField.setHorizontalAlignment(JTextField.RIGHT);
                    ((PlainDocument)textField.getDocument()).setDocumentFilter(
                                    new CustomFilter(9, CustomFilter.INTEGER));

                    textField.addFocusListener(new FocusAdapter()
                    {
                            public void focusGained(FocusEvent e)
                            {
                                    textField.selectAll();
                            }
                    });
            }

            public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column){
                    // TextFieldに内容がある場合は、そのままとする
                    if (value != null)
                    {
                            textField.setText(value.toString());
                    }
                    else
                    {
                            textField.setText("");
                    }

                    textField.selectAll();
                    return textField;
            }


            public Integer getCellEditorValue()
            {
                    if(textField.getText().equals("") || textField.getText().equals("-"))
                                    return	0;
                    else
                                    return	Integer.parseInt(textField.getText());
            }
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

        return staffNo;

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
        this.initDiscount();
        this.initPayments();
        this.initTotal();
        ia.getSales().setSalesDate(salesDate.getDate());

        MstShop shop = targetList.get(currentIndex).getShop();
        Integer slipNo = targetList.get(currentIndex).getSlipNo();

        try {
            this.load(shop, slipNo);
        } catch (Exception e) {
        }

    }

    private void initResponse() {
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            if (ia.getSales().getSlipNo() != null) {
                mrs.setShopId(ia.getShop().getShopID());
                mrs.setSlipNo(ia.getSales().getSlipNo());
                mrs.load_Use(con);
            } else {
                mrs.setShopId(this.getSelectedShopID());
                mrs.setSlipNo(null);
                mrs.load_Use(con);
            }

            con.close();
            MstResponse mr = new MstResponse();
            mr.setResponseID(-1);
            mr.setResponseName("");
            mr.setDisplaySeq(-1);
            mrs.add(0, mr);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void loadContract() {
        if (contractTable.getCellEditor() != null) {
            contractTable.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(contractTable);

        DefaultTableModel model = (DefaultTableModel) contractTable.getModel();
        ResultSetWrapper rs;
        try {
            // && contractDeatailNo == rs.getInt("contract_detail_no")
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            //前回の伝票NOを取得           
            rs = con.executeQuery(getContractSQL());
            int i = 0;
            while (rs.next()) {
                DataContract dc = new DataContract();
                dc.setValidDate(rs.getDate("valid_date"));
                dc.setLimitDate(rs.getDate("limit_date"));
                dc.setContractStatus(rs.getInt("contract_status"));
                dc.setSlipNo(rs.getInt("slip_no"));
                dc.getShop().setShopID(rs.getInt("shop_id"));
                dc.setContractNo(rs.getInt("contract_no"));
                dc.setContractDetailNo(rs.getInt("contract_detail_no"));
                dc.setAlterContractDetailNo(rs.getInt("contract_detail_no"));
                String strTaxRate = rs.getString("tax_rate");
                if( strTaxRate == null ) {
                    dc.setTaxRate(SystemInfo.getTaxRate(rs.getDate("sales_date")));
                } else {
                    dc.setTaxRate(rs.getDouble("tax_rate"));
                }
                Product pr = new Product();
                pr.setProductID(rs.getInt("product_id"));
                dc.setProduct(pr);
                dc.setProductNum(rs.getInt("product_num"));
                dc.setProductValue(rs.getLong("product_value"));
                dc.setProductNum1(rs.getDouble("consumption_num"));
                MstStaff ms = new MstStaff();
                ms.setStaffID(rs.getInt("staff_id"));
                ms.setStaffName(0, rs.getString("staff_name1"));
                ms.setStaffName(1, rs.getString("staff_name2"));
                dc.setStaff(ms);
//                this.setDataContract(rs);  
                if (dContract.getContractNo() == rs.getInt("contract_no") && dContract.getContractDetailNo() == rs.getInt("contract_detail_no")) {
                    flag = true;
                } else {
                    flag = false;
                }
                NumberFormat nf = NumberFormat.getInstance();
                long unitPrice = new BigDecimal(dc.getProductValue()).divide(new BigDecimal(dc.getProductNum()), 0, RoundingMode.UP).longValue();
                double price = new BigDecimal(unitPrice).multiply(new BigDecimal(rs.getDouble("consumption_num"))).setScale(0, RoundingMode.UP).doubleValue();
              
                java.util.Date date = new java.util.Date();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                date = cal.getTime();
                
                if (rs.getInt("contract_status") == 1) {
                    continue;
                } else if (rs.getDate("valid_date") != null && dc.getValidDate().compareTo(date) < 0 || rs.getDate("limit_date") != null && dc.getLimitDate().compareTo(date) < 0) {
                    continue;

                } else if (dc.getProductNum() - (rs.getDouble("consumption_num")) != 0.0) {
                    Object[] rowData = {true,
                        rs.getString("course_class_name"),
                        rs.getString("course_name"),
                        rs.getInt("product_value"),
                        price,
                        rs.getInt("product_value") - price,
                        rs.getDouble("consumption_num") + "/" + rs.getDouble("product_num"),
                        DateUtil.format(rs.getDate("valid_date"), "yyyy/MM/dd"),
                        rs.getString("staff_name1") + "　" + rs.getString("staff_name2"),
                        dc,};
                    model.addRow(rowData);
                }
                i++;
                //IVS_LVTU start add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
                if(ia.getSales().getStaff() == null || ia.getSales().getStaff().getStaffID() == null) {
                    Integer staffID = rs.getInt("main_staff_id") == 0 ? null: rs.getInt("main_staff_id");
                    ia.getSales().setStaff(new MstStaff(staffID));
                }
                //IVS_LVTU end add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
            }
            rs.close();
            setTotal();

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }

    /**
     * ResultSetWrapperのデータをセットする。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException 例外
     */
    public DataContract setDataContract(ResultSetWrapper rs, DataContract dContract) throws SQLException {
        dContract = new DataContract();
        dContract.setValidDate(rs.getDate("valid_date"));
        dContract.setLimitDate(rs.getDate("limit_date"));
        dContract.setContractStatus(rs.getInt("contract_status"));
        dContract.setSlipNo(rs.getInt("slip_no"));
        dContract.getShop().setShopID(rs.getInt("shop_id"));
        dContract.setContractNo(rs.getInt("contract_no"));
        dContract.setContractDetailNo(rs.getInt("contract_detail_no"));
        dContract.setAlterContractDetailNo(rs.getInt("contract_detail_no"));
        Product pr = new Product();
        pr.setProductID(rs.getInt("product_id"));
        dContract.setProduct(pr);
        dContract.setProductNum(rs.getInt("product_num"));
        dContract.setProductValue(rs.getLong("product_value"));
        dContract.setProductNum1(rs.getDouble("consumption_num"));
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
        dContract.setStaff(ms);
        arrDS.add(dContract);
        return dContract;
    }

    private String getContractSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("  dc.contract_status");
        sql.append(" ,dc.limit_date");
        sql.append(" ,dc.delete_date");
        sql.append(" ,ds.customer_id");
        sql.append(" ,ds.shop_id");
        sql.append(" ,ds.slip_no");
        //IVS_LVTU start add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
        sql.append(" ,ds.staff_id as main_staff_id");
        //IVS_LVTU end add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
        sql.append(" ,dc.contract_no");
        sql.append(" ,dc.contract_detail_no");
        sql.append("    ,dc.product_id");
        sql.append("    ,ds.sales_date");
        sql.append(" ,mc.course_name");
        sql.append(" ,mcc.course_class_name");
        sql.append(" ,dc.product_value");
        sql.append(" ,dc.valid_date");
        sql.append("    ,dc.product_num");
        sql.append("    ,dc.tax_rate");
        // IVS SANG START EDIT 20131105 契約一覧に表示されている契約を変更しようとしても変更画面にデータが表示されない。	
        // sql.append("    ,ms.staff_name1");	
        // sql.append(" ,ms.staff_name2");	
        sql.append("     ,coalesce(ms.staff_name1, '') as staff_name1");
        sql.append("     ,coalesce(ms.staff_name2, '') as staff_name2");
        // IVS SANG END EDIT 20131105 契約一覧に表示されている契約を変更しようとしても変更画面にデータが表示されない。;
        sql.append(" ,ms.staff_id");
        sql.append(" ,sum(coalesce(dcd.product_num,0)) as consumption_num");
        sql.append(" from data_sales ds");
        sql.append("    inner join data_contract dc");
        sql.append("    on ds.shop_id = dc.shop_id and ds.slip_no = dc.slip_no");
        sql.append("    inner join mst_course mc");
        sql.append("    on mc.course_id = dc.product_id");
        sql.append("  inner join mst_course_class mcc");
        sql.append("  on mcc.course_class_id = mc.course_class_id");
        // IVS SANG START EDIT 20131105 契約一覧に表示されている契約を変更しようとしても変更画面にデータが表示されない。	
        // sql.append("    inner join mst_staff ms");
        sql.append("    left join mst_staff ms");
        // IVS SANG START EDIT 20131105 契約一覧に表示されている契約を変更しようとしても変更画面にデータが表示されない。	
        sql.append("    on ms.staff_id = dc.staff_id");
        sql.append("    left outer join data_contract_digestion dcd");
        sql.append(" on dc.shop_id = dcd.contract_shop_id     and dc.contract_no = dcd.contract_no     and dc.contract_detail_no = dcd.contract_detail_no ");
        sql.append(" WHERE");
        sql.append(" ds.customer_id =" + this.customer.getCustomerID());
        sql.append("  and  dc.contract_no =" + this.dContract.getContractNo());
        sql.append(" and dc.delete_date is null ");
        sql.append(" group by ");
        sql.append(" ds.customer_id,ds.shop_id,ds.slip_no,dc.contract_no,dc.contract_detail_no,dc.product_id,ds.sales_date,mc.course_name,mcc.course_class_name,dc.product_value,dc.valid_date,dc.product_num,ms.staff_name1,ms.staff_name2,ms.staff_id, mc.course_id,dc.contract_status,dc.limit_date,dc.delete_date");
        sql.append(" ,dc.tax_rate");
        //IVS_LVTU start add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
        sql.append(",ds.staff_id");
        //IVS_LVTU end add 2017/09/08 #25308 [gb]解約処理 解約伝票に、契約時のスタッフIDを登録する
        sql.append(" order by");
        sql.append(" ds.sales_date, mc.course_name, mc.course_id");
        return sql.toString();
    }

    private boolean checkInput() {

        //IVS_LVTU start add 2017/08/03 #9906 [gb]契約解約画面にてチェックなしで解約してご会計へ行く契約追加して清算したら例外発生した。
        boolean flagChecked = false;
        //IVS_LVTU end add 2017/08/03 #9906 [gb]契約解約画面にてチェックなしで解約してご会計へ行く契約追加して清算したら例外発生した。
        //担当者
        if (staff.getSelectedIndex() < 1) {
            MessageDialog.showMessageDialog(
                    this,
                    "担当者は必須です。",
                    this.getTitle(), JOptionPane.ERROR_MESSAGE);
            staffNo.requestFocusInWindow();
            return false;
        }
        //IVS_LVTu start add 2015/06/01 Bug #37075
//        // check input payment
        long paymentValue = 0;
        long totalValue = Long.valueOf(requestValueText123.getText().toString().replace(",", "")).longValue();
        for ( int i = 0;i < payments.getRowCount() ; i ++) {
            paymentValue = paymentValue + Long.valueOf(payments.getValueAt(i, 2).toString()).longValue();
        }
        //IVS_TMTrong start edit 20150713 Bug #40230
        //if ( paymentValue == 0 || (paymentValue != totalValue)) {
        if ( (paymentValue == 0 && totalValue != 0) || (paymentValue != totalValue)) {
        //IVS_TMTrong end edit 20150713 Bug #40230
            MessageDialog.showMessageDialog(
                    this,
                    "請求金額とお預り金額が一致しません。\n" +
                    "請求金額と一致するよう入力してください。",
                    this.getTitle(), JOptionPane.ERROR_MESSAGE);
            payments.requestFocusInWindow();
            return false;
        }
        //IVS_LVTu end add 2015/06/01 Bug #37075
        for ( int i = 0;i < payments.getRowCount();i ++) {
            if ("-".equals(payments.getValueAt(i, 2).toString().trim())) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "金額"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                payments.requestFocusInWindow();
                return false;
            }
        }
        //IVS_LVTU start add 2017/08/03 #9906 [gb]契約解約画面にてチェックなしで解約してご会計へ行く契約追加して清算したら例外発生した。
        for (int i = 0;i < contractTable.getRowCount();i ++) {
            if (contractTable.getValueAt(i, 0) instanceof Boolean && ((Boolean)contractTable.getValueAt(i, 0)) == true) {
                flagChecked = ((Boolean)contractTable.getValueAt(i, 0));
                break;
            }
        }
        //チェックボックスにチェックが１つもついていなければ、アラートを出して登録処理はされないようにする。
        if(!flagChecked) {
            MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(20003),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                contractTable.requestFocusInWindow();
                return false;
        }
        //IVS_LVTU end add 2017/08/03 #9906 [gb]契約解約画面にてチェックなしで解約してご会計へ行く契約追加して清算したら例外発生した。
        return true;
    }
}

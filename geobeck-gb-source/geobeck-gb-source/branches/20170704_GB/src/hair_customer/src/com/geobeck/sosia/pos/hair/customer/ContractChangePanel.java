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
import com.geobeck.sosia.pos.data.account.DataCashIO;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import javax.comm.PortInUseException;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import jp.co.flatsoft.fscomponent.*;

/**
 *
 * @author  ivs
 */
public class ContractChangePanel extends AbstractImagePanelEx implements SearchHairProductOpener
{
       	
	/**
	 * 行の高さ
	 */
	private final int		ROW_HEIGHT	=	27;
	
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
        private int slipNoNew;
        private int shopID;
        private int contractNo;
        private int contractDeatailNo;
        private long totalCourseBefore = 0;
        private long totalProductBefore = 0;
        private long totalCourseAfter = 0;
        private long totalProductAfter = 0;
        private long serviceCharge = 0;
        private Long totalRequestValue = 0l;
        //IVS_PTQUANG start add 2017/06/16 #16151 【契約変更処理画面】売掛になった際に支払額がだぶって登録される
        private static String MESSAGE_LESS_THAN_BILLING = "お預かり金額が不足しています。売掛処理を行いますか？";
        //IVS_PTQUANG end add 2017/06/16 #16151 【契約変更処理画面】売掛になった際に支払額がだぶって登録される
        //private boolean flag;
	/**
	 * レスポンスマスタ
	 */
	private MstResponses mrs = new MstResponses();	

        private MstShop targetShop = null;

	private ContractChangeAccount	ia	=	new ContractChangeAccount();
	
	// 伝票詳細登録数
	private	int						detailCount = 0;
	
	//指名:true or フリー：false 指名フリー状態保持
	private boolean shimeiFreeFlag = false;
	
	//レーシートボタン対応 出力:true 出力しない:false レシート出力状態保持
	private boolean printReceiptFlsg = false;

        private CustomerDisplay customerDisplay = null;
        
	/**
	 * 伝票入力画面用FocusTraversalPolicy
	 */
	private	InputAccountFocusTraversalPolicy	ftp	=
			new InputAccountFocusTraversalPolicy();
        //LVTu start add 2015/03/15 Task #36708
	MstAccountSetting ms = new MstAccountSetting();
         //LVTu end add 2015/03/15 Task #36708
	/** Creates new form HairInputAccountPanel */
	public ContractChangePanel(Integer CusID, Integer contractNo,Integer contractDeatailNo, Integer ShopID)
	{
		super();
                this.CusID = CusID;
                //this.slipNo = slipNo;
                this.shopID = ShopID;
                this.contractNo =contractNo;
                this.contractDeatailNo=contractDeatailNo;
                targetShop = SystemInfo.getCurrentShop();

		initComponents();
		addMouseCursorChange();
                this.setSize(845, 691);		
		this.setTitle("契約変更処理");
		this.setListener();
		
                initResponse();
		this.init();
                initContractColumn();
             //LVTu start add 2015/03/15 Task #36708
            ms = SystemInfo.getAccountSetting(); 
             try {
                 ms.load(SystemInfo.getConnection());
                 ms.setTaxRate(SystemInfo.getTaxRate(SystemInfo.getSystemDate()));
             } catch (SQLException ex) {
                 Logger.getLogger(ContractChangePanel.class.getName()).log(Level.SEVERE, null, ex);
             }
       
             //LVTu end add 2015/03/15 Task #36708
                ia.setShop(targetShop);
                this.loadReceiptSetting(); 
                ConnectionWrapper con = SystemInfo.getConnection(); 
                try
                {
                    ArrayList cusArray = ia.getMstCustomerArrayByNo(con, CusID); 
                    MstCustomer customer = new MstCustomer();
                    customer = (MstCustomer)cusArray.get(0);
                    CusNameLabel.setText(customer.getFullCustomerName());
                    CusNoLabel.setText(customer.getCustomerNo());
                }
                catch(Exception e){}
		
	}  
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
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
        productsScrollPane = new javax.swing.JScrollPane();
        courseAfterTable = new com.geobeck.swing.JTableEx();
        jPanel7 = new javax.swing.JPanel();
        paymentsScrollPane = new javax.swing.JScrollPane();
        payments = new com.geobeck.swing.JTableEx();
        jLabel17 = new javax.swing.JLabel();
        responseItemLabel3 = new javax.swing.JLabel();
        responseItemLabel6 = new javax.swing.JLabel();
        responseItemLabel7 = new javax.swing.JLabel();
        responseItemLabel8 = new javax.swing.JLabel();
        responseItemLabel9 = new javax.swing.JLabel();
        DifferenceAmount = new com.geobeck.swing.JFormattedTextFieldEx();
        responseItemLabel12 = new javax.swing.JLabel();
        responseItemLabel13 = new javax.swing.JLabel();
        responseItemLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        totalCourseLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        requestValueText123 = new com.geobeck.swing.JFormattedTextFieldEx();
        serviceChargeText = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)serviceChargeText.getDocument()).setDocumentFilter(
            new CustomFilter(12, CustomFilter.NUMBER));
        beforeTotalCourseLabel = new com.geobeck.swing.JFormattedTextFieldEx();
        AfterTotalCourseLabel = new com.geobeck.swing.JFormattedTextFieldEx();
        productsScrollPane1 = new javax.swing.JScrollPane();
        contractTable = new com.geobeck.swing.JTableEx();
        jLabel9 = new javax.swing.JLabel();
        totalCourseLabel = new javax.swing.JLabel();
        deleteButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
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

        slipNoLabel.setText("契約日");

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
        staff.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                staffFocusLost(evt);
            }
        });

        staffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
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
                .addContainerGap()
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
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(alertMarkLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(121, 121, 121)
                .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {deleteButton, registButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(slipNoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(salesDate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(alertMarkLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .add(60, 60, 60))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(CusNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(CusNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(staffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {deleteButton, registButton}, org.jdesktop.layout.GroupLayout.VERTICAL);

        productsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        productsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        courseAfterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "コース/商品名", "金額", "消化", "合計", "担当者", "削除", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        courseAfterTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
        courseAfterTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        courseAfterTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(courseAfterTable, SystemInfo.getTableHeaderRenderer());
        courseAfterTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(courseAfterTable);
        this.initProductsColumn();
        TableColumnModel productsModel = courseAfterTable.getColumnModel();
        productsModel.getColumn(1).setCellEditor(new IntegerCellEditor(new JTextField()));
        productsModel.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        courseAfterTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                courseAfterTableFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                courseAfterTableFocusLost(evt);
            }
        });
        courseAfterTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                courseAfterTableMouseReleased(evt);
            }
        });
        courseAfterTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                courseAfterTablePropertyChange(evt);
            }
        });
        courseAfterTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                courseAfterTableKeyPressed(evt);
            }
        });
        productsScrollPane.setViewportView(courseAfterTable);
        courseAfterTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (courseAfterTable.getColumnModel().getColumnCount() > 0) {
            courseAfterTable.getColumnModel().getColumn(6).setMinWidth(0);
            courseAfterTable.getColumnModel().getColumn(6).setPreferredWidth(0);
            courseAfterTable.getColumnModel().getColumn(6).setMaxWidth(0);
        }

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

        responseItemLabel3.setText("変更後");
        jPanel7.add(responseItemLabel3);
        responseItemLabel3.setBounds(0, 110, 70, 22);

        responseItemLabel6.setText("変更前");
        jPanel7.add(responseItemLabel6);
        responseItemLabel6.setBounds(0, 40, 70, 22);

        responseItemLabel7.setText("契約コース");
        jPanel7.add(responseItemLabel7);
        responseItemLabel7.setBounds(0, 130, 60, 22);

        responseItemLabel8.setText("差額");
        jPanel7.add(responseItemLabel8);
        responseItemLabel8.setBounds(270, 50, 60, 22);

        responseItemLabel9.setText("（マイナスの場合は返金）");
        jPanel7.add(responseItemLabel9);
        responseItemLabel9.setBounds(270, 150, 140, 22);

        DifferenceAmount.setEditable(false);
        DifferenceAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        DifferenceAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        DifferenceAmount.setText("0");
        jPanel7.add(DifferenceAmount);
        DifferenceAmount.setBounds(360, 50, 90, 19);

        responseItemLabel12.setText("契約コース");
        jPanel7.add(responseItemLabel12);
        responseItemLabel12.setBounds(0, 60, 60, 22);

        responseItemLabel13.setText("請求金額");
        jPanel7.add(responseItemLabel13);
        responseItemLabel13.setBounds(270, 130, 60, 22);

        responseItemLabel14.setText("変更手数料");
        jPanel7.add(responseItemLabel14);
        responseItemLabel14.setBounds(270, 80, 60, 22);

        jLabel10.setText("コース契約金額");
        jLabel10.setPreferredSize(new java.awt.Dimension(77, 19));
        jPanel7.add(jLabel10);
        jLabel10.setBounds(570, 0, 77, 19);

        totalCourseLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalCourseLabel1.setPreferredSize(new java.awt.Dimension(77, 19));
        jPanel7.add(totalCourseLabel1);
        totalCourseLabel1.setBounds(670, 0, 77, 19);

        jLabel5.setText("円");
        jPanel7.add(jLabel5);
        jLabel5.setBounds(760, 0, 20, 20);

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
        beforeTotalCourseLabel.setBounds(70, 60, 100, 20);

        AfterTotalCourseLabel.setEditable(false);
        AfterTotalCourseLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        AfterTotalCourseLabel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        AfterTotalCourseLabel.setText("0");
        jPanel7.add(AfterTotalCourseLabel);
        AfterTotalCourseLabel.setBounds(70, 130, 100, 19);

        productsScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
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

        deleteButton1.setIcon(SystemInfo.getImageIcon("/button/select/select_course_off.jpg"));
        deleteButton1.setBorderPainted(false);
        deleteButton1.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_course_on.jpg"));
        deleteButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton1ActionPerformed(evt);
            }
        });

        jLabel16.setText("変更後");

        jLabel28.setText("契約コース");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("円");

        jLabel18.setText("変更前");

        jLabel29.setText("契約コース");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel18)
                            .add(jLabel29)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jLabel28, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(jLabel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .add(90, 90, 90)
                                .add(deleteButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(productsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 802, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(productsScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 802, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(totalCourseLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(37, 37, 37)))
                .add(registPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(154, 154, 154)
                        .add(registPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(7, 7, 7)
                        .add(jLabel18)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel29)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(productsScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(totalCourseLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(deleteButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(jLabel16)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel28)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(productsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

        
        
        /**
	 * 明細の列を初期化する。
	 */
	private void initProductsColumn()
	{
                //列の幅を設定する。
                //products.getColumnModel().getColumn(0).setPreferredWidth(80);		// 区分
                courseAfterTable.getColumnModel().getColumn(0).setPreferredWidth(250);                       // 分類
                courseAfterTable.getColumnModel().getColumn(1).setPreferredWidth(110); // 商品/技術名       
                courseAfterTable.getColumnModel().getColumn(2).setPreferredWidth(75);		// 数量
                courseAfterTable.getColumnModel().getColumn(3).setPreferredWidth(110);		// 金額
                courseAfterTable.getColumnModel().getColumn(4).setPreferredWidth(160);		// 割引              
                courseAfterTable.getColumnModel().getColumn(5).setPreferredWidth(80);		// 削除
                courseAfterTable.getColumnModel().getColumn(5).setResizable(false);             
                courseAfterTable.getColumnModel().getColumn(6).setPreferredWidth(0);	       
	}
        //IVS_LVTu start add 2015/12/28 Bug #45225
        private void initContractColumn() {
            contractTable.getColumnModel().getColumn(0).setMinWidth(0);
            contractTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            contractTable.getColumnModel().getColumn(0).setMaxWidth(0);
            contractTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            contractTable.getColumnModel().getColumn(9).setMinWidth(0);
            contractTable.getColumnModel().getColumn(9).setPreferredWidth(0);
            contractTable.getColumnModel().getColumn(9).setMaxWidth(0);
        }
        //IVS_LVTu end add 2015/12/28 Bug #45225
        /**
	 * 売上明細を初期化する。
	 */
	private void initProducts()
	{           
            pointIndexList.clear();            
            TableColumnModel productsModel = courseAfterTable.getColumnModel();

            SwingUtil.setJTableHeaderRenderer(courseAfterTable, SystemInfo.getTableHeaderRenderer());
          
            courseAfterTable.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    courseAfterTableFocusGained(evt);
                }
            });
            courseAfterTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    courseAfterTablePropertyChange(evt);
                }
            });
            courseAfterTable.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    courseAfterTableKeyPressed(evt);
                }
            });
	 
	    SwingUtil.clearTable(courseAfterTable);
	    detailCount = 0;
	}


	private void paymentsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentsFocusGained
		if(payments.getInputContext() != null) payments.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_paymentsFocusGained

	private void paymentsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_paymentsPropertyChange
		this.changePaymentValue();
	}//GEN-LAST:event_paymentsPropertyChange

	private void paymentsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentsKeyPressed

            if(payments.getSelectedRow() == 0 &&
                    payments.getSelectedColumn() == 0 &&
                    (evt.getKeyCode() == evt.VK_ENTER || evt.getKeyCode() == evt.VK_TAB) &&
                    (evt.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    //先頭行の先頭列かつ、
                    //Enterキー or TabキーとShiftキーが押下された場合
                    //レジ担当者へ
                    staffNo.requestFocusInWindow();
            }
            if(payments.getSelectedRow() == payments.getRowCount() - 1 &&
                    payments.getSelectedColumn() == 2 &&
                    (evt.getKeyCode() == evt.VK_ENTER || evt.getKeyCode() == evt.VK_TAB) &&
                    (evt.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == 0) {
                    //最終行の最終列かつ、
                    //Enterキー or Tabキーが押下された場合(Shiftキーの押下はなし)
                    //精算ボタンへ
                  
            }

	}//GEN-LAST:event_paymentsKeyPressed

	/**
	 * レスポンス項目別タイトルを取得する
	 */
	private String getResponseIssueTitle( DataResponseIssue dri )
	{
		return ( ( dri.getResponse().getCirculationType() == 1 ) ? getStaffName( dri ) : getCirculationMonthlyDate( dri ) );
	}
	
	/**
	 * スタッフ名を取得する
	 */
	private String getStaffName( DataResponseIssue dri )
	{
		String staffName = "";
		if( dri.getStaff() == null ) staffName = "???? ????";
		staffName = dri.getStaff().toString();
		
		return staffName + String.format( "(%1$tY/%1$tm/%1$td)", dri.getRegistDate() );
	}
	
	/**
	 * 発行年月を取得する
	 */
	private String getCirculationMonthlyDate(  DataResponseIssue dri )
	{
	    if( dri == null ) return "??/??";
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
        /*
         if(this.canChangeKeys())
         {
         if(MessageDialog.showYesNoDialog(this,
         MessageUtil.getMessage(3002),
         this.getTitle(),
         JOptionPane.QUESTION_MESSAGE) != 0)
         {
         return;
         }
         }
         */
        // JTableで入力後にEnterキーによる確定がされていない場合を考慮して以下の処理を行う
        if (!this.checkInput()) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel();
        if (model.getRowCount() == 0) {
            MessageDialog.showMessageDialog(this,
                    "変更後のコースがありません。追加して下さい。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (MessageDialog.showYesNoDialog(this,
                "入力内容で契約を変更します。本当によろしいですか。",
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        //IVS_PTQUANG start add 2017/06/16 #16151 【契約変更処理画面】売掛になった際に支払額がだぶって登録される
        else {
            if (ia.getPaymentTotal() < (totalCourseAfter - totalCourseBefore + serviceCharge)) {
            	if(MessageDialog.showYesNoDialog(this, MESSAGE_LESS_THAN_BILLING, this.getTitle(), JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
                {
                    return;
                }
            }
        }
        //IVS_PTQUANG end add 2017/06/16 #16151 【契約変更処理画面】売掛になった際に支払額がだぶって登録される
        if (courseAfterTable.getCellEditor() != null) {
            courseAfterTable.getCellEditor().stopCellEditing();
        }

        /*
         for(DataPaymentDetail dpd : ia.getSales().getPayment(0)) {
         if(dpd.getPaymentMethod() != null && dpd.getPaymentMethod().getPaymentClass() != null) {

         Integer id = dpd.getPaymentMethod().getPaymentClassID();

         // カードまたは電子マネーの場合
         //if (id.equals(2) || id.equals(3)) {
         //cardPayment += dpd.getPaymentValue().longValue();
         //}
         }
		 
         }*/
        //IVS_LVTu start add 2015/12/28 Bug #45225
        Integer beforeContractShopID    = dContract.getShop().getShopID();
        Integer beforeContractNo        = dContract.getContractNo();

        try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            con.begin();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DefaultTableModel contractModel = (DefaultTableModel) contractTable.getModel();
            for (int i = 0; i < contractModel.getRowCount(); i++) {
                dContract = (DataContract) contractModel.getValueAt(i, 9);
                //if ((contractModel.getValueAt(i, 0).toString()).equals("true")) {
                    dContractDigestion.setSlipNo(dContract.getSlipNo());
                    dContractDigestion.setContractNo(dContract.getContractNo());
                    dContractDigestion.setContractDetailNo(dContract.getContractDetailNo());
                    dContractDigestion.setProductNum(dContract.getProductNum1());
                    //dContract.setContractStatus(2); edit 2013/05/16
                    dContract.setContractStatus(1);
                    dContract.UpdateContractStatus(con);

                    //IVS NNTUAN START ADD 20131105
                    try {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        if (dContract.isExitsContract(con)) {
                            if (con.executeUpdate(dContract.getUpdateDataContractLogicallySQL()) <= 0) {
                                con.rollback();
                                return;
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(MstCustomerPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    //IVS NNTUAN END ADD 20131105
                //}
            }
            
            //get valid_date
            GregorianCalendar calDate = new GregorianCalendar();
            if (salesDate.getDate() != null) {
                calDate.setTime(salesDate.getDate());
            }
            

            DataSales dsSales = new DataSales();           
            dsSales.setShop(targetShop);
            dsSales.setNewSlipNo(con);
            //2016/09/06 GB MOD #54449 Start
            //更新先の店舗の契約No最大値+1を取得するよう変更
            //Integer contractNo = dContract.loadMaxContractNo(con, shopID) + 1;
            Integer contractNo = dContract.loadMaxContractNo(con, targetShop.getShopID()) + 1;
            //2016/09/06 GB MOD #54449 Start
            slipNoNew = dsSales.getSlipNo();
            for (int i = 0; i < model.getRowCount(); i++) {
                //dContract.setSlipNo(rh.getSlipNo());
                //dContract.setSlipNo(rh.getShop().getShopID());
                //dContract.deleteDataContractLogically(con,dContract.getShop().getShopID(),dContract.getSlipNo());
                //dContractDigestion.deleteDataContractDisgestionLogLogically(con,dContract.getShop().getShopID(),dContract.getSlipNo());
                
                DataReservationDetail dr = (DataReservationDetail) (model.getValueAt(i, 6));
                Product p = new Product(); 
                p.setProductID(dr.getCourse().getCourseId());
                //IVS NNTUAN START ADD 20131105
                dContract = new DataContract();
                //dContract.setContractNo(dContract.loadMaxContractNo(con, shopID) );
                dContract.setContractNo(contractNo);
                //IVS NNTUAN END ADD 20131105
                dContract.setShop(targetShop);
                dContract.setSlipNo(slipNoNew);
                dContract.setServiceCharge(Integer.valueOf(serviceChargeText.getText()));
                dContract.setRequestValue(totalRequestValue);
                dContract.setStaff((MstStaff) ((JComboBox) courseAfterTable.getValueAt(i, 4)).getSelectedItem());
                //dContract.setContractStatus(1);   edit 2013/05/16 
                dContract.setContractStatus(2);
                dContract.setProduct(p);
                dContract.setProductValue(Long.valueOf(model.getValueAt(i, 3).toString()));
                dContract.setProductNum(Integer.valueOf(model.getValueAt(i, 2).toString()));
                dContract.setContractDetailNo(dContract.loadMaxContractDetailNo(con, dContract.getShop().getShopID(), dContract.getContractNo()) + 1);
                if (calDate != null && dr.getCourse().getPraiseTimeLimit() != null && dr.getCourse().getPraiseTimeLimit() > 0) {
                    calDate.add(Calendar.MONTH, dr.getCourse().getPraiseTimeLimit());
                    dContract.setValidDate(calDate.getTime());
                }
                dContract.setBeforeContractShopID(beforeContractShopID);
                dContract.setBeforeContractNo(beforeContractNo);
                //IVS_LVTu end add 2015/12/28 Bug #45225
                //dContractDigestion.setContractDetailNo(dContractDigestion.loadMaxContractDetailNo(con,dContract.getShop().getShopID(),dContract.getContractNo()) + 1);
                if (!dContract.registDataContract(con)) {
                    con.rollback();
                    return;
                }
                if (i == 0) {
                    dsSales = new DataSales();
                    dsSales.setShop(targetShop);
                    dsSales.setSlipNo(slipNoNew);
                    dsSales.setSalesDate(ia.getSales().getSalesDate());
                    MstCustomer msCus = new MstCustomer();
                    msCus.setCustomerID(CusID);
                    dsSales.setCustomer(msCus);
                    dsSales.setStaff((MstStaff) ((JComboBox) courseAfterTable.getValueAt(i, 4)).getSelectedItem());
                    dsSales.setType(3);
                    if (!dsSales.registDataSale(con)) {
                        con.rollback();
                        return;
                    }
                }


                DataSalesDetail dsDetail = new DataSalesDetail();
                 //LVTu start add 2015/03/15 Task #36708
                dsDetail.setAccountSetting(ms);
                 //LVTu end add 2015/03/15 Task #36708
                dsDetail.setSlipNo(slipNoNew);
                dsDetail.setShop(targetShop);
                dsDetail.setNewSlipDetailNo(con);
                // 7 = コース変更
                dsDetail.setProductDivision(7);
                dsDetail.setProductValue(Long.valueOf(model.getValueAt(i, 3).toString()));
                //EDIT START 2013-11-05 HOA
                //データ計算間違いで修正
                //dsDetail.setProductNum(Integer.valueOf(model.getValueAt(i, 2).toString()));
                dsDetail.setProductNum(1);
                //EDIT END 2013-11-05 HOA
                //DataReservationDetail dr = (DataReservationDetail)(model.getValueAt(i, 6));
                //Product p = new Product();
                //p.setProductID(dr.getCourse().getCourseId());
                dsDetail.setProduct(p);
                dsDetail.setStaff((MstStaff) ((JComboBox) courseAfterTable.getValueAt(i, 4)).getSelectedItem());
                if (!dsDetail.regist(con)) {
                    con.rollback();
                    return;
                }

                 /*
                 dContractDigestion.setShop(dContract.getShop());

                 dContractDigestion.setStaff((MstStaff)((JComboBox)courseAfterTable.getValueAt( i , 6 )).getSelectedItem());
                 if ( !dContractDigestion.regist(con)) {
                 con.rollback();
                 return; 
                 }*/
            }
                        
            if (model.getRowCount() > 0) {
                DataSalesDetail dsDetail = new DataSalesDetail();
                 //LVTu start add 2015/03/15 Task #36708
                dsDetail.setAccountSetting(ms);
                 //LVTu end add 2015/03/15 Task #36708
                dsDetail.setSlipNo(slipNoNew);
                dsDetail.setShop(targetShop);
                // 9 = 手数料
                dsDetail.setProductDivision(9);
                dsDetail.setNewSlipDetailNo(con);
                dsDetail.setProductValue(Long.valueOf(serviceChargeText.getText()));
                dsDetail.setStaff((MstStaff) ((JComboBox) courseAfterTable.getValueAt(0, 4)).getSelectedItem());
                Product p = new Product();
                p.setProductID(-1);
                dsDetail.setProduct(p);
                //IVS NNTUAN START EDIT 20131105
                /*if (!dsDetail.regist(con)) {
                    con.rollback();
                    return;
                }*/
                if (dsDetail.getProductValue() > 0) {
                    if (!dsDetail.regist(con)) {
                        con.rollback();
                        return;
                    }
                }
                //IVS NNTUAN END EDIT 20131105
                ia.getSales().setSlipNo(slipNoNew);
                if (!ia.registContract(con)) {
                    con.rollback();
                    return;
                }
                MstStaff ms = (MstStaff) staff.getSelectedItem();
                //IVS_TMTrong start edit 20150715 New request #40229
//                DataCashIO dcio = new DataCashIO();
//                dcio.setStaff(ms);
//                if (totalCourseAfter - totalCourseBefore < 0) {
//                    dcio.setIn(false);
//                } else {
//                    dcio.setIn(true);
//                }
                //IVS_TMTrong end edit 20150715 New request #40229
                String UserFor = "契約変更：（";
                boolean bUseFor = false;
                DataPayment dp = ia.hairSales.getPayment(0);
               
                for (int i = 0; i < dp.size(); i++) {
                    if (dp.get(i).getPaymentValue() > 0) {
                        UserFor += dp.get(i).getPaymentMethod().getPaymentMethodName() + "、";
                        bUseFor = true;
                    }
                }
                if (!bUseFor) {
                    UserFor = "";
                } else {
                    UserFor = UserFor.substring(0, UserFor.length() - 1) + "　" + (ia.getPaymentTotal() - totalRequestValue) + "円）";
                }
                if (totalRequestValue > 0) {
                    dp.setShop(targetShop);
                    dp.setSlipNo(slipNoNew);
                    dp.setTempFlag(false);
                    dp.setBillValue(totalRequestValue);
                    //IVS_PTQUANG start edit 2017/06/16 #16151 【契約変更処理画面】売掛になった際に支払額がだぶって登録される
                    //if (!dp.registAll(con)) {
                    if (!dp.regist(con)) {
                    //IVS_PTQUANG end edit 2017/06/16 #16151 【契約変更処理画面】売掛になった際に支払額がだぶって登録される
                        con.rollback();
                        return;
                    }
                }
                //IVS_TMTrong start edit 20150715 New request #40229
//                dcio.setUseFor(UserFor);
//                dcio.setShop(targetShop);
//                dcio.setValue(0);
                //IVS_TMTrong end edit 20150715 New request #40229
                java.util.Date ioDate;
                ioDate = salesDate.getDate();
                GregorianCalendar io = new GregorianCalendar();
                io.setTime(ioDate);
                //IVS_TMTrong start edit 20150715 New request #40229
//                dcio.setIoDate(io);
                //IVS_TMTrong end end 20150715 New request #40229
                //IVS NNTUAN START EDIT 20131105
                /*
                if (Long.parseLong(payments.getValueAt(0, 2).toString()) > 0) {
                    dcio.setValue(Integer.parseInt(payments.getValueAt(0, 2).toString()));
                }
                if (!dcio.regist(con)) {
                    con.rollback();
                    return;
                }
                */
                //IVS_TMTrong start edit 20150715 New request #40229
//                if (Long.parseLong(payments.getValueAt(0, 2).toString()) > 0) {
//                    dcio.setValue(Integer.parseInt(payments.getValueAt(0, 2).toString()));
//                } else {
//                     dcio.setValue(Integer.valueOf(totalRequestValue.toString()));
//                }
//                dcio.regist(con);
                //IVS_TMTrong end edit 20150715 New request #40229
                //IVS NNTUAN START EDIT 20131105
            }

            //トランザクションコミット
            con.commit();
            con.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.setVisible(false);
            this.dispose();
        }
       
     
    }//GEN-LAST:event_registButtonActionPerformed

    private void salesDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesDateFocusLost
        ia.getSales().setSalesDate(salesDate.getDate());
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
    }//GEN-LAST:event_salesDateFocusLost

    private void salesDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesDateFocusGained
        if(salesDate.getInputContext() != null)
        salesDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_salesDateFocusGained

    private void salesDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_salesDateItemStateChanged
        ia.getSales().setSalesDate(salesDate.getDate());
        ia.setTaxRate(SystemInfo.getTaxRate(salesDate.getDate()));
    }//GEN-LAST:event_salesDateItemStateChanged

    private void deleteButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton1ActionPerformed
//        if (!flag) {        
//            MessageDialog.showMessageDialog(this,
//                        "変更するコースにチェックを付けて下さい。",
//                        this.getTitle(),
//                        JOptionPane.ERROR_MESSAGE);          
//            return;
//        }
        SystemInfo.getLogger().log(Level.INFO, "技術検索");
                    SearchHairProductDialog spd = new SearchHairProductDialog(parentFrame, true, this, 1, null,null,true);
                    spd.setVisible(true);
                    spd.dispose();
                    spd = null;                           
        //showDataDetail(contractTable,products,0);
    }//GEN-LAST:event_deleteButton1ActionPerformed

    private void courseAfterTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_courseAfterTableFocusGained
        if(courseAfterTable.getInputContext() != null)
        courseAfterTable.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_courseAfterTableFocusGained

    private void courseAfterTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_courseAfterTableFocusLost

        if (3== courseAfterTable.getSelectedColumn() ) {
            if ( courseAfterTable.getCellEditor() != null ) {
                courseAfterTable.getCellEditor().stopCellEditing();
                courseAfterTable.requestFocusInWindow();
            }
        }

    }//GEN-LAST:event_courseAfterTableFocusLost

    private void courseAfterTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_courseAfterTableKeyPressed
        if(courseAfterTable.getSelectedRow() == courseAfterTable.getRowCount() - 1 &&
            courseAfterTable.getSelectedColumn() == 1 &&
            evt.getKeyCode() == evt.VK_ENTER)
        {

            payments.setColumnSelectionInterval( 2, 2 );
            payments.setRowSelectionInterval( 0, 0 );
            payments.requestFocusInWindow();
            courseAfterTable.setColumnSelectionAllowed( true );
            courseAfterTable.setRowSelectionAllowed( true );
            courseAfterTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_courseAfterTableKeyPressed

    private void courseAfterTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_courseAfterTablePropertyChange

        this.changeProducts();
        

        int row	= courseAfterTable.getSelectedRow();
        int col = courseAfterTable.getSelectedColumn();
        //Double totalAfter = 0.0;
        if (row >= 0 && (1 == col)) {
            DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel();  
            //dContract = (DataContract) model.getValueAt()           
            int a = Integer.valueOf(model.getValueAt(row,1).toString());
            //Double b = Double.valueOf(model.getValueAt(row,3).toString());
            //products.setValueAt( a - b, row, 5 );
            courseAfterTable.setValueAt( a, row, 3 );
            courseAfterTable.updateUI(); 
        }
        this.setTotal();
    }//GEN-LAST:event_courseAfterTablePropertyChange

    private void courseAfterTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseAfterTableMouseReleased

        int col = courseAfterTable.getSelectedColumn();
        if (col != 11 && col != 14) {
            courseAfterTable.requestFocusInWindow();
        }

    }//GEN-LAST:event_courseAfterTableMouseReleased

    private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed

        MstStaff ms= (MstStaff)staff.getSelectedItem();

        if ( ms != null) {
            if (ms.getStaffID() != null) {
                staffNo.setText(ms.getStaffNo());
            }

            if (staff.getSelectedIndex() == 0) {
                staffNo.setText("");
            }

            ia.getSales().getPayment(0).setStaff(ms);
        }
         DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel(); 
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(getStaffComboBox( ia.getStaffs().getIndexByID( ms.getStaffID()) ), i, 4);
        }
    }//GEN-LAST:event_staffActionPerformed

    private void staffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staffNoFocusLost

        if (!staffNo.getText().equals("")) {
            this.setStaff(staffNo.getText());
            this.getFocusTraversalPolicy().getComponentAfter(this,staffNo);
        }else{
            staffNo.setText("");
            staff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_staffNoFocusLost

    private void staffFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staffFocusLost
        // TODO add your handling code here:
         MstStaff ms= (MstStaff)staff.getSelectedItem();

        if ( ms != null) {
            if (ms.getStaffID() != null) {
                staffNo.setText(ms.getStaffNo());
            }

            if (staff.getSelectedIndex() == 0) {
                staffNo.setText("");
            }

            ia.getSales().getPayment(0).setStaff(ms);
        }
         DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel(); 
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(getStaffComboBox( ia.getStaffs().getIndexByID( ms.getStaffID()) ), i, 4);
        }
    }//GEN-LAST:event_staffFocusLost

    private void serviceChargeTextPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_serviceChargeTextPropertyChange
        setTotal();
    }//GEN-LAST:event_serviceChargeTextPropertyChange

    private void serviceChargeTextCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_serviceChargeTextCaretUpdate
        setTotal();
    }//GEN-LAST:event_serviceChargeTextCaretUpdate


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.swing.JFormattedTextFieldEx AfterTotalCourseLabel;
    private com.geobeck.swing.JFormattedTextFieldEx CusNameLabel;
    private com.geobeck.swing.JFormattedTextFieldEx CusNoLabel;
    private com.geobeck.swing.JFormattedTextFieldEx DifferenceAmount;
    private javax.swing.JLabel alertMarkLabel;
    private com.geobeck.swing.JFormattedTextFieldEx beforeTotalCourseLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup charge;
    private com.geobeck.swing.JTableEx contractTable;
    private com.geobeck.swing.JTableEx courseAfterTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private com.geobeck.swing.JTableEx payments;
    private javax.swing.JScrollPane paymentsScrollPane;
    private javax.swing.JScrollPane productsScrollPane;
    private javax.swing.JScrollPane productsScrollPane1;
    private javax.swing.JButton registButton;
    private javax.swing.JPanel registPanel;
    private com.geobeck.swing.JFormattedTextFieldEx requestValueText123;
    private javax.swing.JLabel responseItemLabel12;
    private javax.swing.JLabel responseItemLabel13;
    private javax.swing.JLabel responseItemLabel14;
    private javax.swing.JLabel responseItemLabel3;
    private javax.swing.JLabel responseItemLabel6;
    private javax.swing.JLabel responseItemLabel7;
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
	
	private boolean	changeKeys = true;	

        private ArrayList<com.geobeck.sosia.pos.data.account.DataSales> targetList = new ArrayList<com.geobeck.sosia.pos.data.account.DataSales>();
        private int currentIndex = 0;

        private ArrayList<Integer> pointIndexList = new ArrayList<Integer>();

        //コース契約前のコース分類
        private Map<Integer, ConsumptionCourseClass> consumptionCouserClassMap = new HashMap<Integer, ConsumptionCourseClass>();
        //コース契約前のコース
        private Map<Integer, Map<String, ConsumptionCourse>> consumptionCourseMap = new HashMap<Integer, Map<String, ConsumptionCourse>>();
	/**
	 * 伝票入力画面用FocusTraversalPolicyを取得する。
	 * @return 伝票入力画面用FocusTraversalPolicy
	 */
	public InputAccountFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
        
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{		
		SystemInfo.addMouseCursorChange(deleteButton);
		SystemInfo.addMouseCursorChange(registButton);							
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{		
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
	 * @return 選択されている店舗
	 */
	private MstShop getSelectedShop()
	{
            return targetShop;
	}
	
	/**
	 * 選択されている店舗のIDを取得する。
	 * @return 選択されている店舗のID
	 */
	private Integer getSelectedShopID()
	{
		MstShop	ms	=	this.getSelectedShop();
		
		if(ms != null)
			return	ms.getShopID();
		else
			return	0;
	}

	public Integer getProductsIndex( int typeNo )
	{
                return this.getProductsIndex(typeNo, courseAfterTable.getSelectedRow());
	}
        
	public Integer getProductsIndex( int typeNo, int row )
	{
		Integer retInteger = null;
		JComboBox combo;
		if ( row < 0 ) {
		    return null;
		}
		
		combo = (JComboBox)courseAfterTable.getValueAt( row, 0 );

                if ( combo.getItemAt( typeNo + 1 ) != null ) {
		    retInteger = (Integer)combo.getItemAt( typeNo + 1 );
		}
		
		return retInteger;
	}
	
	public void addSelectedProduct(Integer productDivision, Product product)
	{
		int addNo = -1;
		int no = 0;

		for ( DataSalesDetail dsd : ia.getSales() ) {
			if( ( productDivision != 1 )&&
                                ( productDivision != 2 ) &&
                                ( productDivision != 3 ) &&
                                ( productDivision != 4 ) &&
				( dsd.getProductDivision().intValue() == productDivision.intValue() )&&
				( dsd.getProduct().getProductID().intValue() == product.getProductID().intValue() )
			)
			{
				addNo = no;
			}
			no++;
		}
                
	
                    ia.getSales().get( addNo ).setProductNum( ia.getSales().get( addNo ).getProductNum() + 1 );
                    courseAfterTable.setValueAt( ia.getSales().get( addNo ).getProductNum(), addNo, 4 );
                    this.changeProducts();
	

		this.setTotal();
	}

      	public void addSelectedCourse(Integer productDivision, Course course)
	{               

                //this.addCourseRow(ia.getSales().get(ia.getSales().size() - 1), false);
                DataReservationDetail drd = new DataReservationDetail();
                drd.setCourse(course);
                drd.setCourseFlg(1); //コース契約の場合は1
                DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel();
                model.addRow(new Object[]{
			drd.getCourse().getCourseName(),
			drd.getCourse().getPrice(),
			drd.getCourse().getNum(),
			drd.getCourse().getPrice(),                    
			//getStaffComboBox( ia.getStaffs().getIndexByID( Integer.valueOf(hModel.getValueAt(i, 9).toString()) ) ),
                        getStaffComboBox( ia.getStaffs().getIndexByID( dContract.getStaff().getStaffID() ) ),
			getDeleteButton(),
                        drd
                        });
                this.setTotal();
	}

        private String generateTmpContractNo(Course course){
            int courseSize = 0;
            if(consumptionCourseMap.containsKey(course.getCourseClass().getCourseClassId())){
                courseSize = consumptionCourseMap.get(course.getCourseClass().getCourseClassId()).size();
            }
            
            return course.getCourseId() + "_" + String.valueOf(courseSize);
        }

       	public void addSelectedConsumptionCourse(Integer productDivision, ConsumptionCourse consumptionCourse)
	{
		int addNo = -1;
		int no = 0;
                for ( DataSalesDetail dsd : ia.getSales() ) {
			if( ( productDivision != 1 )&&
                                ( productDivision != 2 ) &&
                                ( productDivision != 3 ) &&
                                ( productDivision != 4 ) &&
                                ( productDivision != 5 ) &&
				( dsd.getProductDivision().intValue() == productDivision.intValue() )
//				( dsd.getProductDivision().intValue() == productDivision.intValue() )&&
//				( dsd.getConsumptionCourse().getSlipNo().intValue() == consumptionCourse.getSlipNo().intValue() ) &&
//				( dsd.getConsumptionCourse().getCourseId().intValue() == consumptionCourse.getCourseId().intValue() )
			)
			{
                                if (dsd.getConsumptionCourse().getSlipNo() == null) {
                                    //契約番号がない場合（コース契約前）
                                    if (consumptionCourse.getSlipNo() != null) {
                                        continue;
                                    }
                                    //仮契約番号がない場合（コース契約済だが消化を予約した状態）
                                    if(dsd.getTmpContractNo() == null){
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

                                    if (( dsd.getConsumptionCourse().getSlipNo().intValue() == consumptionCourse.getSlipNo().intValue() ) &&
                                    ( dsd.getConsumptionCourse().getCourseId().intValue() == consumptionCourse.getCourseId().intValue() ) ){
            				addNo = no;
                                    }
                                }
			}
			no++;
		}

                // 新規の内容ならレコードを追加する
		
                    ia.getSales().get( addNo ).setProductNum( ia.getSales().get( addNo ).getProductNum() + 1 );
                    courseAfterTable.setValueAt( ia.getSales().get( addNo ).getProductNum(), addNo, 4 );
                    this.changeProducts();
	

//                this.setTotalWithoutPayment();
                this.setTotal();
	}
        	
	private void setCourseClasses(CourseClasses courseClasses, JTable classTable)
	{
            SwingUtil.clearTable(classTable);
            DefaultTableModel model = (DefaultTableModel)classTable.getModel();

            for (CourseClass pc : courseClasses) {
                model.addRow(new Object[]{ pc });
            }

            if (0 < classTable.getRowCount()) {
                classTable.setRowSelectionInterval(0, 0);
            }
	} 

        private Course getSelectedCourse(JTable table)
        {
		int row = table.getSelectedRow();

		if (row < 0) {
                    return	null;
		}

		return (Course)table.getValueAt(row, 0);
        }


	/**
	 * 初期化処理を行う。
	 */
	private void init()
	{
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
	private void initStaff()
	{
            staff.removeAllItems();

            for (MstStaff ms : ia.getStaffs()) {
                if (ms.isDisplay()) {
                    staff.addItem(ms);
                }
            }

            staff.setSelectedIndex(0);
	}
	

	
	private JComboBox getProductDivisionName( DataSalesDetail dsd, int dIndex, int pIndex )
	{
		JComboBox techName = new JComboBox();
		techName.removeAllItems();
		techName.addItem( pIndex == -1 ? dsd.getProductDivisionName() : "" );
		techName.addItem( dIndex );
		techName.addItem( -1 < pIndex ? pIndex : null ) ;
		techName.setSelectedIndex( 0 );
		techName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		techName.setOpaque( false );
		return techName;
	}	
	
	

	

        
	/**
	 * スタッフコンボを取得する
	 */
	private JComboBox getStaffComboBox( Integer staffID )
	{
		JComboBox staffCombo = new JComboBox(ia.getStaffs().toArray());
                if (staffID.intValue() < 0){
                    staffID = new Integer(0);
                }
		staffCombo.setSelectedIndex( staffID );
		
                staffCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
		
		//スタッフが変更されたときの処理を追加
		staffCombo.addActionListener(new java.awt.event.ActionListener()
		{
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                            changeProducts();
                            setTotal();
                    }
		});
		return staffCombo;
	}
	
	/**
	 * 削除ボタンを取得する
	 */
	private JButton getDeleteButton()
	{
		JButton		delButton	=	new JButton();
		delButton.setBorderPainted(false);
		delButton.setContentAreaFilled(false);
		delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
		delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
		delButton.setSize(48, 25);
		delButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				deleteProduct();
			}
		});
		return delButton;
	}
	
	/**
	 * NULLフィールドを取得する
	 */
	private JFormattedTextField getNullField()
	{
		JFormattedTextField		ftf	=	new JFormattedTextField(
				FormatterCreator.createMaskFormatter( "", null, "" ) );
		ftf.setHorizontalAlignment(JTextField.CENTER);
		ftf.addFocusListener( SystemInfo.getSelectText() );
		ftf.setBorder(null);
		ftf.setText("");
		return	ftf;
	}
	
	/**
	 * 売上明細の内容を変更したときの処理
	 **/
	private void changeProducts()
	{
		int	row		=	courseAfterTable.getSelectedRow();
		int	col		=	courseAfterTable.getSelectedColumn();
		if(row < 0 || col < 0)	return;

             if (col == staffCol) {
                 /*
                    //担当者
                		DataSalesDetail dsd		= (DataSalesDetail)ia.getSales().get( index );
                                if( 0 < dsd.size() ) {
                                        if( getProductsIndex( 1 ) == null )
                                            {
                                    ia.getSales().get( index ).setStaff((MstStaff)((JComboBox)products.getValueAt( row , col )).getSelectedItem());
                                        } else if( 0 <= getProductsIndex( 1 )) {
                                ia.getSales().get( index ).get( getProductsIndex( 1 ) ).setStaff((MstStaff)((JComboBox)products.getValueAt( row , col )).getSelectedItem());
                                        }
                                } else {
                    ia.getSales().get( index ).setStaff((MstStaff)((JComboBox)products.getValueAt( row , col )).getSelectedItem());
                                }
                                */
                } 
	}
	
	/**
	 * 指定行の技術インデックスをデクリメントする
	 */
	private void decTableTechIndex( int row )
	{
		Integer setInteger = null;
		JComboBox combo;
		if( row < 0 ) return ;
		combo = (JComboBox)courseAfterTable.getValueAt( row, 0 );
		if (combo == null) return;
		setInteger = (Integer)combo.getItemAt( 1 ) - 1;
		combo.removeItemAt( 1 );
		combo.insertItemAt( setInteger, 1 );
	}
	
	/**
	 * 売上明細を１行削除する。
	 * @param index 削除する明細のインデックス
	 * @param evt 
	 */
	private void deleteProduct()
	{
	    int row = courseAfterTable.getSelectedRow();	// 選択行
              if ( courseAfterTable.getCellEditor() != null ) {
		courseAfterTable.getCellEditor().stopCellEditing();
	    }
	    DefaultTableModel model = (DefaultTableModel)courseAfterTable.getModel();
                model.removeRow(row);          
           

	    this.detailCount--;

            this.setTotal();
            //this.showCustomerDisplayToSalesValue();
	}
	
	/**
	 * 割引部の初期化処理を行う。
	 */
	private void initDiscount()
	{          

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
	 **/
	private void initPayments()
	{
            this.initPaymentCells();
            this.addNewPaymentRow(true);
	}
	
	/**
	 * 支払方法のセルを初期化する。
	 **/
	private void initPaymentCells()
	{
		DefaultTableModel model = (DefaultTableModel)payments.getModel();
		
		//全行削除
		model.setRowCount(0);
		payments.removeAll();
		
		for (MstPaymentClass mpc : ia.getPaymentClasses())
                {
                    JComboBox classes = new JComboBox( new Object[]{ mpc });
                    classes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                    Object methods = this.getPaymentMethodObject(mpc);
                    
                    Object[] rowData = { new JLabel(" " + mpc.toString()), methods, 0l, getCashInsertButton() };
                    model.addRow(rowData);

                    ia.getSales().getPayment(0).addPaymentDetail(mpc,(mpc.size() == 1 ? mpc.get(0) : null), 0l);
		}
	}
	
	/**
	 * 支払方法に新しい行を追加する。
	 **/
	private void addNewPaymentRow(boolean isAddData)
	{
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
		classes.addActionListener(new java.awt.event.ActionListener()
		{
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        paymentActionPerformed(evt);
                    }
		});
		
		DefaultTableModel model = (DefaultTableModel)payments.getModel();
		
		model.addRow(new Object[]{ classes, methods, 0l, getCashInsertButton() });
		
		if (isAddData)
                {
                    ia.getSales().getPayment(0).addPaymentDetail(
                                        mpc,
					(mpc == null || 0 == mpc.size() ? null : mpc.get(0)),
					0l);
		}
	}
	
	/**
	 * 残額ボタンを取得する
	 */
	private JButton getCashInsertButton()
	{
		JButton button = new JButton();
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/account/cash_insert_off.jpg")));
		button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/account/cash_insert_on.jpg")));

                button.setMargin(new Insets(0,0,0,0));
                
		button.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            setBalance();
                         
			}
		});
		return button;
	}
        
        private void setBalance()
        {
            /*
            long value = ((NameValue)ia.getTotal( 5 ) ).getValue().longValue();
            if (value > 0) {
                            
            }*/
            //if (totalRequestValue > 0) {
                int row = payments.getSelectedRow();
                long currentValue = Long.parseLong(payments.getValueAt(row, 2).toString());
                payments.setValueAt(currentValue + totalRequestValue, row, 2);
                payments.changeSelection(row, 2, false, false);
                changePaymentValue();  
            //}
             
        }
        
	/**
	 * 支払方法のTableModel
	 **/
	private class PaymentTableModel extends DefaultTableModel
	{
		/**
		 * 支払区分
		 */
		MstPaymentClasses mpcs	=	null;
		
		/**
		 * コンストラクタ
		 * @param mpcs 支払区分のリスト
		 */
		public PaymentTableModel(MstPaymentClasses mpcs)
		{
			super(new String[]{"支払区分", "支払方法", "金額"}, 0);
			this.mpcs = mpcs;
			if(this.mpcs == null)
					this.mpcs	=	new MstPaymentClasses();
		}
		
		/**
		 * 列のクラスを取得する。
		 * @param col 列
		 * @return 列のクラス
		 */
		public Class getColumnClass(int col)
		{
			if(col < 0 || this.getValueAt(0, col) == null)	return	Object.class;
			return	this.getValueAt(0, col).getClass();
		}
		
		/**
		 * セルが編集可能かを取得する。
		 * @param row 行
		 * @param col 列
		 * @return true - 編集可能
		 */
		public boolean isCellEditable(int row, int col)
		{
			//支払区分の支払区分数以下の行の場合
			if(col == 0 && row < mpcs.size())
			{
				return	false;
			}
			//支払方法の支払方法が１件の場合
			if(col == 1 && this.getValueAt(row, col).getClass().getName().equals("java.lang.String"))
			{
				return	false;
			}
			
			return	true;
		}
	}
	
	
	/**
	 * 支払方法が変更されたときの処理。
	 * @param evt 
	 */
	public void paymentActionPerformed(java.awt.event.ActionEvent evt)
	{
		int row = payments.getSelectedRow();
		int col = payments.getSelectedColumn();
		
		if(row < 0 || col < 0 || col > 1) return;
		
		MstPaymentClass mpc = null;
		MstPaymentMethod mpm = null;
		
		//支払区分が固定の行の場合
		if(0 <= row && row < ia.getPaymentClasses().size())
		{
                    mpc = ia.getPaymentClasses().get(row);
		}
		else
		{
			JComboBox mpccb = (JComboBox)payments.getValueAt(row, 0);
			mpccb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
			int index = mpccb.getSelectedIndex() + 1;
			
			if(0 < index && index <= ia.getPaymentClasses().size())
			{
                            mpc = ia.getPaymentClasses().get(index);
			}
		}
		
		switch(col)
		{
			//支払区分
			case 0:
				if(mpc != null)
				{
                                    Object methods = this.getPaymentMethodObject(mpc);
                                    payments.setValueAt(methods, row, 1);
				}
				
				break;
			//支払方法
			case 1:
				if(mpc != null && mpc.size() != 0)
				{
                                    if(mpc.size() == 1)
                                    {
                                        mpm = mpc.get(0);
                                    }
                                    else
                                    {
                                        JComboBox mpccb = (JComboBox)payments.getValueAt(row, 1);
                                        mpccb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                                        int index = mpccb.getSelectedIndex() - 1;

                                        if(0 <= index && index < mpc.size())
                                        {
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
	 * @param mpc 支払区分
	 * @return 支払方法にセットするオブジェクト
	 */
	private Object getPaymentMethodObject(MstPaymentClass mpc)
	{
		Object	methods	=	null;
		
		//支払方法が無い場合
		if(mpc.size() == 0)
		{
			methods	= new JLabel();
		}
		//支払方法が１件の場合
		else if(mpc.size() == 1)
		{
			methods	= new JLabel(" " + mpc.get(0).getPaymentMethodName());
		}
		else
		{
			methods	=	new JComboBox();
			((JComboBox)methods).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
			((JComboBox)methods).addItem(new MstPaymentMethod());
			
			for(MstPaymentMethod mpm : mpc)
			{
				((JComboBox)methods).addItem(mpm.getPaymentMethodName());
			}

			//支払方法が変更されたときの処理を追加
			((JComboBox)methods).addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent evt)
				{
					paymentActionPerformed(evt);
				}
			});
		}
		
		return	methods;
	}
	
	
	/**
	 * 合計を初期化する。
	 */
	private void initTotal()
	{
            //totalProductLabel1.setText( "0" );  
            totalCourseLabel1.setText( "0" ); 
            //totalProductLabel.setText( "0" ); 
    /*
            // 明細合計
            totalPrice.setText( "0" );     
            // 明細売上
            serviceCharge.setText( "0" );
            // 全体割引
            allDiscountValue.setText( "0" );
            // 消費税
            taxValue.setText( "0" );
            // 請求金額
            totalValue.setText( "0" );
            // お預かり金額
            paymentTotal.setText("0");
            // お釣り
            changeValue.setText( "0" );
            */ 
	}
	
	/**
	 * 合計をセットする。
	 */
	private void setTotal()
	{
            //flag = false;       
            //totalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseBefore));
            
            //Double totalAfter = 0.0;
             DefaultTableModel contractModel = (DefaultTableModel) contractTable.getModel(); 
             totalCourseBefore = 0;
             for (int i = 0; i < contractModel.getRowCount(); i++) {
                //if ((contractTable.getValueAt(i, 0).toString()).equals("true")) {
                 totalCourseBefore += (Double)(contractModel.getValueAt(i,5));
                 //flag = true;
                //}                
            }
             
            totalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseBefore));
            beforeTotalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseBefore));
            
            DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel();  
            totalCourseAfter = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                totalCourseAfter += Long.parseLong(courseAfterTable.getValueAt(i, 3).toString() );
            }
            totalCourseLabel1.setText(FormatUtil.decimalFormat(totalCourseAfter));
            AfterTotalCourseLabel.setText(FormatUtil.decimalFormat(totalCourseAfter));
            //beforeTotalCourseLabel3.setText(FormatUtil.decimalFormat(totalProductAfter));
            //beforeTotalCourseLabel4.setText(FormatUtil.decimalFormat(totalCourseAfter + totalProductAfter));
            
            DifferenceAmount.setText(FormatUtil.decimalFormat((totalCourseAfter + totalProductAfter) - (totalCourseBefore + totalProductBefore)));
            if (!"".equals(serviceChargeText.getText().trim())) {
                Double serviceChargeTemp = Double.valueOf((serviceChargeText.getText()));
                serviceCharge = serviceChargeTemp.longValue();
            } else {
                serviceCharge = 0l;
            }
            //IVS_LVTu start edit 2016/01/13 New request #46408
            totalRequestValue = totalCourseAfter - totalCourseBefore +  serviceCharge - ia.getPaymentTotal();
            //totalRequestValue = totalCourseAfter - totalCourseBefore +  serviceCharge;
            requestValueText123.setText(FormatUtil.decimalFormat(totalCourseAfter - totalCourseBefore +  serviceCharge));
            //IVS_LVTu end edit 2016/01/13 New request #46408
            
            //ia.setTotal();
            /*
		
		
		// お預かり合計
		paymentTotal.setText(FormatUtil.decimalFormat( ia.getPaymentTotal()) );
		
		int row = 0;
		
		// 明細合計
		totalPrice.setText(FormatUtil.decimalFormat(((NameValue)ia.getTotal( 0 )).getValue()) );

		// 明細売上
                Long tmpPrice = ((NameValue)ia.getTotal( 3 )).getValue() + ((NameValue)ia.getTotal( 2 )).getValue() + ia.getCunsumptionTotal();
		serviceCharge.setText(FormatUtil.decimalFormat(tmpPrice));

		// 全体割引
		allDiscountValue.setText(FormatUtil.decimalFormat(((NameValue)ia.getTotal( 2 ) ).getValue()));
                
		// 明細割引＋全体割引をdiscountValueに設定する
		//Long tmpDiscount=( (NameValue)ia.getTotal( 1 ) ).getValue()+( (NameValue)ia.getTotal( 2 ) ).getValue();
		//discountValue.setText(FormatUtil.decimalFormat( tmpDiscount) );

		// 消費税
		taxValue.setText( FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 4 ) ).getValue()) );

		// 請求金額
		totalValue.setText(FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 3 ) ).getValue()) );

                // お釣り
		changeValue.setText(FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 5 ) ).getValue()) );
                */
	}

      	/**
	 * 合計をセットする。
         * ただし請求金額には反映しない
	 */
	private void setTotalWithoutPayment()
	{
                /*
		ia.setTotal();

		// お預かり合計
		paymentTotal.setText(FormatUtil.decimalFormat( ia.getPaymentTotal()) );

		int row = 0;

		// 明細合計
		totalPrice.setText(FormatUtil.decimalFormat(((NameValue)ia.getTotal( 0 )).getValue()) );

		// 明細売上
                Long tmpPrice = ((NameValue)ia.getTotal( 3 )).getValue() + ((NameValue)ia.getTotal( 2 )).getValue();
		serviceCharge.setText(FormatUtil.decimalFormat(tmpPrice));

		// 全体割引
		allDiscountValue.setText(FormatUtil.decimalFormat(((NameValue)ia.getTotal( 2 ) ).getValue()));

		// 明細割引＋全体割引をdiscountValueに設定する
		//Long tmpDiscount=( (NameValue)ia.getTotal( 1 ) ).getValue()+( (NameValue)ia.getTotal( 2 ) ).getValue();
		//discountValue.setText(FormatUtil.decimalFormat( tmpDiscount) );

		// 消費税
		taxValue.setText( FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 4 ) ).getValue()) );

		// 請求金額
//		totalValue.setText(FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 3 ) ).getValue()) );

		// お釣り
//		changeValue.setText(FormatUtil.decimalFormat(( (NameValue)ia.getTotal( 5 ) ).getValue()) );
                * */
	}

        private void setStaff(){
                MstStaff ms = new MstStaff();
		//スタッフNoをセットする。
		ms.setStaffNo(this.staffNo.getText());
                			try
			{
				ms = SelectSameNoData.getMstStaffByNo(
						parentFrame,
						SystemInfo.getConnection(),
						this.staffNo.getText());
						
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
               ia.getSales().getPayment(0).setStaff(ms);
		this.requestFocusToPayments();
                                                        
        }	
	
	
	/**
	 * スタッフをセットする。
	 */
	private void setStaff(String staffNo)
	{      
	    staff.setSelectedIndex(0);

	    for (int i = 1; i < staff.getItemCount(); i++) {
		
		if (((MstStaff)staff.getItemAt(i)).getStaffNo().equals(staffNo)) {
		    staff.setSelectedIndex(i);
		    break;
		}
	    }
	}
	

	/**
	 * スタッフをセットする。
	 */
	private void setStaff(Integer staffID)
	{      
	    staff.setSelectedIndex(0);

	    for (int i = 1; i < staff.getItemCount(); i++) {
		
		if (((MstStaff)staff.getItemAt(i)).getStaffID().equals(staffID)) {
		    staff.setSelectedIndex(i);
		    break;
		}
	    }
	}
	
	
	/**
	 * 支払金額が変更されたときの処理。
	 */
	private void changePaymentValue()
	{
            int row = payments.getSelectedRow();
            int col = payments.getSelectedColumn();

            if(row < 0 || col < 0)	return;

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
                    if (dpd.getPaymentMethod() == null) {
                        if (row < ia.getPaymentClasses().size() &&
                            ia.getPaymentClasses().get(row).size() > 0)
                        {
                            dpd.setPaymentMethod(ia.getPaymentClasses().get(row).get(0));
                        }
                    }
                    dpd.setPaymentValue(value);
                }
            }

            this.setTotal();
	}
	
	/**
	 * 売掛金をセットする。
	 */
	public void setBill()
	{
		//売掛金を読み込む
		ia.loadBill(ia.getSales().getCustomer().getCustomerID());		
		
	}
        
	/**
	 * 伝票データを読み込む。
	 * @param slipNo 伝票No.
	 */
	public void load(MstShop shop, Integer slipNo)
	{
                
                
                
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
	 * @param salesDate　販売日
	 */       
        public void setSalesDate(java.util.Date salesDate)
        {
               this.salesDate.setDate(salesDate);
        }
	

        public void setReservationConsumptionCourseMap(){
            for(DataSalesDetail dsd : ia.getSales()) {
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

                if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
                    cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {

                    result = false;
                }
            }

            return result;
        }
	
	/**
	 * JComboBoxの選択されているItemを設定する。
	 * @param cb JComboBox
	 * @param item 選択するItemの文字列
	 */
	private void setJComboBoxItem(JComboBox cb, String item)
	{
		for(int i = 0; i < cb.getItemCount(); i ++)
		{
			if(cb.getItemAt(i).toString().equals(item))
			{
				cb.setSelectedIndex(i);
				return;
			}
		}
	}
	
	/**
	 * 伝票データの削除処理を行う。
	 */
	private void delete()
	{		
		
		boolean result = false;



		try
		{
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
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		if(result)
		{
			this.init();
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.INFO_DELETE_SUCCESS, "伝票データ"),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
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
	 * @return 変更できる場合 true
	 */
	public boolean canChangeKeys()
	{
		return changeKeys;
	}

	public void setChangeKeys(boolean changeKeys)
	{
		this.changeKeys = changeKeys;
	}
	
	private class TotalCellRenderer extends DefaultTableCellRenderer
	{
		private	Font	defaultFont		=	null;
		private	Font	bigFont			=	null;
		
		public TotalCellRenderer()
		{
			super();
			defaultFont	=	getFont();
			bigFont		=	defaultFont.deriveFont(Font.BOLD, 18.0f);
		}
		
		public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			
			setHorizontalAlignment(SwingConstants.RIGHT);
			
			if((row == 3 || row == 5) && column == 1)
			{
				this.setFont(bigFont);
			}
			else
			{
				this.setFont(defaultFont);
			}
			
			return	this;
		}
	}
	
	
	private void requestFocusToPayments()
	{
		payments.setRowSelectionInterval(0, 0);
		payments.setColumnSelectionInterval(2, 2);
		payments.requestFocusInWindow();
	}	
	
	
	/**
	 * SOSIA連動ダイアログを開く
	 */
	private	void changeSosiaGear()
	{
		SosiaGearDialog sgd	=	new SosiaGearDialog( ia.getSales().getCustomer() );
		SwingUtil.openAnchorDialog( this.parentFrame, true, sgd, "SOSIA連動", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
		sgd	=	null;
		
	}
	
	/**
	 * 伝票入力画面用FocusTraversalPolicy
	 */
	private class InputAccountFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent のあとでフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			
			return aComponent;
		}

		/**
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{
			
			return aComponent;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
                        return getFirstComp();
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return getFirstComp();
		}
		
		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
                        return getFirstComp();
		}
		
		/**
		 * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
		 * show() または setVisible(true) の呼び出しで一度ウィンドウが表示されると、
		 * 初期化コンポーネントはそれ以降使用されません。
		 * 一度別のウィンドウに移ったフォーカスが再び設定された場合、
		 * または、一度非表示状態になったウィンドウが再び表示された場合は、
		 * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
		 * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
		 * @param window 初期コンポーネントが返されるウィンドウ
		 * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
		 */
		public Component getInitialComponent(Window window)
		{
                        return getFirstComp();
		}
	}
        
        public class IntegerCellCustomEditor extends DefaultCellEditor
        {
                JTextField textField = new JTextField();

                public IntegerCellCustomEditor(JTextField field)
                {
                        super(field);
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
	
	private void loadReceiptSetting()
	{
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
                    
		} catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void printReceipt()
	{		
		PrintReceipt pr = new PrintReceipt();
		
		if (!pr.canPrint()) {
                    return;
		}
		
		pr.setCustomer(ia.getSales().getCustomer());
		
		for(DataSalesDetail dsd : ia.getSales())
		{
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
		Long tmpDiscount=( (NameValue)ia.getTotal( 1 ) ).getValue()+( (NameValue)ia.getTotal( 2 ) ).getValue();
		pr.setDiscount(- tmpDiscount);
		pr.setOutOfValue(ia.getPaymentTotal());
		pr.setChangeValue(ia.getTotal(5).getValue());
		pr.setStaff(ia.getSales().getPayment(0).getStaff());
		
		pr.print();
	}
	
	private Long taxFilter(Long value)
	{
		Long result = value;
		
		if (ia.getAccountSetting().getDisplayPriceType() == 1) {
                    result -= SystemInfo.getTax(result, salesDate.getDate());
		}
		
		return result;
	} 
        
       
	/**
	 * TableHeaderRenderer
	 */
	private BevelBorderHeaderRenderer	tableHeaderRenderer	=	null;

	/**
	 * テーブルヘッダーのレンダラーを取得する
	 * @return テーブルヘッダーのレンダラー
	 */
	public  BevelBorderHeaderRenderer getTableHeaderRenderer()
	{
		if(tableHeaderRenderer == null)
		{
			tableHeaderRenderer	=	new BevelBorderHeaderRenderer(
					this.getTableColor());
		}
		return tableHeaderRenderer;
	}
	/**
	 *色の設定
	 */
	public Color getTableColor()
	{
		return	new Color(204,204,204);
	}
        /**
         * 有効な先頭項目を返します。
         */
        private Component getFirstComp()
        {
            
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
        SwingUtil.clearTable(courseAfterTable);
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
            mr.setResponseID( -1 );
            mr.setResponseName( "" );
            mr.setDisplaySeq( -1 );
            mrs.add( 0, mr );

        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private void loadContract()
    {        
        if (contractTable.getCellEditor() != null) {
		contractTable.getCellEditor().stopCellEditing();
	}
	 SwingUtil.clearTable(contractTable);
                
	 DefaultTableModel model = (DefaultTableModel)contractTable.getModel();       
        ResultSetWrapper rs;        
        try {

            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            //前回の伝票NOを取得           
            rs = con.executeQuery(getContractSQL(CusID,slipNo));  
            int i = 0;
            while (rs.next()) {
                this.setDataContract(rs);  
//                if (contractNo == rs.getInt("contract_no") && contractDeatailNo == rs.getInt("contract_detail_no")) {
//                    flag = true;
//                }
//                else
//                {
//                    flag=false;
//                }
                NumberFormat nf = NumberFormat.getInstance();
                //Luc start edit 20150630 #38886
                //long unitPrice = new BigDecimal(dContract.getProductValue()).divide(new BigDecimal(dContract.getProductNum()), 0, RoundingMode.UP).longValue();
                long unitPrice = 0;
                try {
                    unitPrice =  new BigDecimal(dContract.getProductValue()).divide(new BigDecimal(dContract.getProductNum()), 0, RoundingMode.UP).longValue();
                }catch (Exception e) {}
                //Luc end edit 20150630 #38886
                double price = new BigDecimal(unitPrice).multiply(new BigDecimal(rs.getDouble("consumption_num"))).setScale(0, RoundingMode.UP).doubleValue();
                java.util.Date date = new java.util.Date();
                
                if (rs.getInt("contract_status") == 1) {
                    continue;
                } else if ((rs.getDate("valid_date") != null && dContract.getValidDate().compareTo(date) < 0) || (rs.getDate("limit_date") != null && dContract.getLimitDate().compareTo(date) < 0)) {
                    continue;

                } else if (dContract.getProductNum() - (rs.getDouble("consumption_num")) != 0.0) {
                    //IVS_LVTu start add 2015/06/04 Task #36708
                    arrDS.add(dContract); 
                    //IVS_LVTu end add 2015/06/04 Task #36708
                    Object[] rowData = {true,
                        rs.getString("course_class_name"),
                        rs.getString("course_name"),
                        rs.getInt("product_value"),
                        price,
                        rs.getInt("product_value") - price,
                        rs.getDouble("consumption_num") + "/" + rs.getDouble("product_num"),
                        DateUtil.format(rs.getDate("valid_date"), "yyyy/MM/dd"),
                        rs.getString("staff_name1") + "　" + rs.getString("staff_name2"),
                        arrDS.get(i),};
                    model.addRow(rowData);
                    //IVS_LVTu start add 2015/06/04 Task #36708
                    i++;
                    //IVS_LVTu end add 2015/06/04 Task #36708
                }
                //IVS_LVTu start delete 2015/06/04 Task #36708
                //i++;
                //IVS_LVTu end delete 2015/06/04 Task #36708
            }
            rs.close();  
            setTotal();          

        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
    }
        /**
	 * ResultSetWrapperのデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException 例外
	 */
	public void setDataContract(ResultSetWrapper rs) throws SQLException
	{
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
                ms.setStaffName( 0 , rs.getString( "staff_name1" ));
                ms.setStaffName( 1 , rs.getString( "staff_name2" ));
		dContract.setStaff( ms ); 
                //IVS_LVTu start delete 2015/06/04 Task #36708
                //arrDS.add(dContract);
                //IVS_LVTu start delete 2015/06/04 Task #36708
	}    
    
    private String getContractSQL(Integer CusID, Integer SlipNo)
	{               
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");            
	    sql.append(" ds.customer_id");         
	    sql.append(" ,ds.shop_id");
	    sql.append(" ,ds.slip_no");
            sql.append(" ,dc.contract_no");
            sql.append(" ,dc.contract_detail_no");
            sql.append("    ,dc.product_id");
            sql.append("    ,ds.sales_date");
            sql.append(" ,mc.course_name");
            sql.append(" ,mcc.course_class_name");
            //sql.append(" ,dr.product_division");
            sql.append(" ,dc.product_value");
            sql.append(" ,dc.contract_status");  
            sql.append(" ,dc.limit_date"); 
            sql.append(" ,dc.valid_date");
            sql.append("    ,dc.product_num");
            // IVS SANG START EDIT 20131105 契約一覧に表示されている契約を変更しようとしても変更画面にデータが表示されない。	
            // sql.append("    ,ms.staff_name1");	
            // sql.append(" ,ms.staff_name2");	
            sql.append("     ,coalesce(ms.staff_name1, '') as staff_name1");	
            sql.append("     ,coalesce(ms.staff_name2, '') as staff_name2");	
            // IVS SANG END EDIT 20131105 契約一覧に表示されている契約を変更しようとしても変更画面にデータが表示されない。	
            sql.append(" ,ms.staff_id");
            sql.append(" ,sum(coalesce(dcd.product_num,0)) as consumption_num");
            sql.append(" from data_sales ds");
            sql.append("    inner join data_contract dc");
            sql.append("    on ds.shop_id = dc.shop_id and ds.slip_no = dc.slip_no");
            //sql.append(" left join data_sales_detail dr");
            //sql.append("  on dc.product_id = dr.product_id and ds.slip_no = dr.slip_no");
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
	    sql.append(" on dc.shop_id = dcd.shop_id     and dc.contract_no = dcd.contract_no     and dc.contract_detail_no = dcd.contract_detail_no ");
	    sql.append(" WHERE");
	    sql.append(" ds.customer_id =" + CusID);
            sql.append(" and dc.contract_no = " + SQLUtil.convertForSQL(this.contractNo) );
            //sql.append(" and ds.slip_no =" + SlipNo); 
            sql.append(" and ds.shop_id =" + SQLUtil.convertForSQL(this.shopID));
            sql.append(" and dc.delete_date is null");
            //sql.append(" and (dc.contract_status <>1 or dc.contract_status is null)");
            //sql.append(" and dc.contract_status not in (1)"); 
            //sql.append(" and (dc.valid_date is null or limit_date is null or date(dc.valid_date) >date(current_timestamp) or date(dc.limit_date) >date(current_timestamp)) "); 
	    sql.append(" group by");
	    sql.append(" ds.customer_id,ds.shop_id,ds.slip_no,dc.contract_no,dc.contract_detail_no,dc.product_id,ds.sales_date,mc.course_name,mcc.course_class_name,dc.product_value,dc.valid_date,dc.product_num,ms.staff_name1,ms.staff_name2,ms.staff_id, mc.course_id,dc.contract_status,dc.limit_date");
	    sql.append(" order by");
	    sql.append(" ds.sales_date, mc.course_name, mc.course_id");
	    return sql.toString();
        }
    
     private void showDataDetail(javax.swing.JTable table,javax.swing.JTable detailTable,int rowIndex)
	{
		SwingUtil.clearTable(detailTable);
		//rcioDetail.setShop(this.getSelectedShop());
		//rcioDetail.setDate(this.getSelectedDate());
		//this.clear();
                DefaultTableModel model = (DefaultTableModel) courseAfterTable.getModel();                
                DefaultTableModel hModel = (DefaultTableModel) table.getModel();
                
                totalCourseAfter = 0;
                for (int i = 0; i < hModel.getRowCount(); i++) {
                    //DataSales rh = (DataSales) hModel.getValueAt(i, 9);
                     dContract = (DataContract) hModel.getValueAt(i, 9);
                    if ((hModel.getValueAt(i, 0).toString()).equals("true")) {
                        model.addRow(new Object[]{
			hModel.getValueAt(i, 1).toString(),
			hModel.getValueAt(i, 2).toString(),			
			dContract.getProductValue(),
			0,
			dContract.getProductNum(),
			dContract.getProductValue(),                    
			//getStaffComboBox( ia.getStaffs().getIndexByID( Integer.valueOf(hModel.getValueAt(i, 9).toString()) ) ),
                        getStaffComboBox( ia.getStaffs().getIndexByID( dContract.getStaff().getStaffID() ) ),
			getDeleteButton(),
                        dContract
                        });
                    }
                    
                    totalCourseAfter += Double.valueOf(dContract.getProductValue());
                } 
                setTotal();
	}
      private boolean checkInput() {

        //担当者
        if (staff.getSelectedIndex() < 1) {
            MessageDialog.showMessageDialog(
                    this,
                    "担当者が未入力です。",
                    this.getTitle(), JOptionPane.ERROR_MESSAGE);
            staffNo.requestFocusInWindow();
            return false;
        }

        return true;
    }
}

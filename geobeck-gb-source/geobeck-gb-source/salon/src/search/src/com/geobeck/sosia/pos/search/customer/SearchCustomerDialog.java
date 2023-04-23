/*
 * SearchCustomerDialog.java
 *
 * Created on 2006/04/20, 14:13
 */

package com.geobeck.sosia.pos.search.customer;

import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.basicinfo.company.VisitKarteSetting;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.basicinfo.company.VisitKarteDialog;


/**
 * 顧客検索画面
 * @author katagiri
 */
public class SearchCustomerDialog extends javax.swing.JDialog
{
	/**
	 * 店舗ＩＤ
	 */
	private Integer shopID = null;
	/**
	 * 顧客No
	 */
	private	String searchCustomerNo = "";
	/**
	 * 顧客名（姓）
	 */
	private	String searchCustomerName1 = "";
	/**
	 * 顧客名（名）
	 */
	private	String searchCustomerName2 = "";
	/**
	 * ふりがな（姓）
	 */
	private	String searchCustomerKana1 = "";
	/**
	 * ふりがな（名）
	 */
	private	String searchCustomerKana2 = "";
	/**
	 * 参照専用フラグ
	 */
	private	boolean isReferenceOnly = false;
	/**
	 * 選択店舗ＩＤ（スタッフ絞込用）
	 */
	private Integer staffShopID = null;
	/**
	 * コース契約と共同購入者濾過
	 */
    private Integer filterStaffID = null;
    private Integer filterShopID = null;
    private Integer filterContractNo = null; 
    private Integer filterContractDetailNo = null; 
	/**
	 * コンストラクタ
	 * @param parent 
	 * @param modal 
	 */
	public SearchCustomerDialog(java.awt.Frame parent, boolean modal, boolean isReferenceOnly)
        {
            this(parent, modal, isReferenceOnly, null);
        }
	public SearchCustomerDialog(java.awt.Frame parent, boolean modal, boolean isReferenceOnly, Integer staffShopID)
	{       
            this(parent, modal, staffShopID);
            this.isReferenceOnly = isReferenceOnly;
            selectButton.setVisible(!this.isReferenceOnly);
	}
	public SearchCustomerDialog(java.awt.Frame parent, boolean modal)
        {
            this(parent, modal, null);
        }
	public SearchCustomerDialog(java.awt.Frame parent, boolean modal, Integer staffID,Integer shopID,Integer contractNo,Integer contractDetailNo)
	    {
			super(parent, modal);
	        this.filterStaffID = staffID;
	        this.filterShopID = shopID;
	        this.filterContractNo = contractNo;
	        this.filterContractDetailNo = contractDetailNo;
	        init();
	    }
	public SearchCustomerDialog(java.awt.Frame parent, boolean modal, Integer staffShopID)
	{       
            super(parent, modal);
            this.staffShopID = staffShopID;
            init();
	}
    
	/**
	 * コンストラクタ
	 * @param parent 
	 * @param modal 
	 */
	public SearchCustomerDialog(java.awt.Dialog parent, boolean modal, boolean isReferenceOnly)
	{
            this(parent, modal);
            this.isReferenceOnly = isReferenceOnly;
            selectButton.setVisible(!this.isReferenceOnly);
	}
	public SearchCustomerDialog(java.awt.Dialog parent, boolean modal)
	{
            super(parent, modal);
            init();
	}
        
        // 共通初期化処理
        private void init() {
            initComponents();
            addMouseCursorChange();
            SystemInfo.initGropuShopComponentsForCustomer(shop, 2);
            this.setStaffs();
            this.setListener();
            SwingUtil.moveCenter(this);
            this.clear();
// 2016/12/24 顧客メモ ADD START
            // 顧客メモ機能無効時は、メモ登録、メモ一覧カラムは表示しない
            if(!this.isCustomerMemoEnabled()) {
                searchResult.removeColumn(searchResult.getColumn("フィード登録"));
                searchResult.removeColumn(searchResult.getColumn("フィード一覧"));
            }
// 2016/12/24 顧客メモ ADD END
            
            // 20170413 add #61376
            if(!SystemInfo.getUseVisitKarte()) {
                searchResult.removeColumn(searchResult.getColumn("二次元コード表示"));
            }

        }
        
	/**
	 * 選択された顧客ＩＤを取得する。
	 * @return 選択された顧客ＩＤ
	 */
	public void setShopID(Integer shopID)
	{
		this.shopID = shopID;
	}

        /**
	 * ユーザ検索ボタンを取得する
	 */
	private JButton getUserSearchButton(final Integer customerID)
	{
		JButton		searchButton	=	new JButton();
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
		searchButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));
		searchButton.setSize(48, 25);
		searchButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            MstCustomerPanel mcp = null;
                            
                            try {

                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                mcp = new MstCustomerPanel(customerID, true, true);
                                SwingUtil.openAnchorDialog( (JFrame)null, true, mcp, "顧客情報", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                            
                            mcp = null;

                            //searchCustomer();

                        }
		});
		return searchButton;
	}        
        
// 2016/12/24 顧客メモ ADD START
        /**
	 * メモ登録ボタンを取得する
	 */
	private JButton getRegistMemoButton(final MstCustomer customer)
	{
		JButton	registMemoButton = new JButton();
		registMemoButton.setBorderPainted(false);
		registMemoButton.setContentAreaFilled(false);
		registMemoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/reservation/memo_add_off.jpg")));
		registMemoButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/reservation/memo_add_on.jpg")));
		registMemoButton.setSize(31, 25);
		registMemoButton.addActionListener(new java.awt.event.ActionListener()
		{
                        @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            try {
                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                CustomerMemoRegistDialog cmrd = new CustomerMemoRegistDialog(null, true, customer);
                                Rectangle rect = getBounds();
                                cmrd.setBounds(rect.x, rect.y, rect.width, rect.height);
                                cmrd.setVisible(true);
                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
		});
		return registMemoButton;
	}
        
        /**
	 * メモ一覧ボタンを取得する
	 */
	private JButton getMemoListButton(final MstCustomer customer)
	{
		JButton	memoListButton = new JButton();
		memoListButton.setBorderPainted(false);
		memoListButton.setContentAreaFilled(false);
		memoListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/reservation/memo_add_off.jpg")));
		memoListButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/reservation/memo_add_on.jpg")));
		memoListButton.setSize(31, 25);
		memoListButton.addActionListener(new java.awt.event.ActionListener()
		{
                        @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            try {
                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                CustomerMemoListDialog cmld = new CustomerMemoListDialog(null, true, customer);
                                Rectangle rect = getOwner().getBounds();
                                cmld.setBounds(rect.x - 5, rect.y - 5, rect.width + 10, rect.height + 10);
                                cmld.setVisible(true);
                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
		});
		return memoListButton;
	}
// 2016/12/24 顧客メモ ADD END
        
        
        /**
         * QR表示ボタン取得
         * 20170413 add #61376
         */
        private JButton getQrDisplayButton(final MstCustomer customer, boolean flag) {
                
                JButton	qrDisplayButton = new JButton();
		qrDisplayButton.setBorderPainted(false);
		qrDisplayButton.setContentAreaFilled(false);
		qrDisplayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/custom/qr_small_off.jpg")));
		qrDisplayButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/custom/qr_small_on.jpg")));
		qrDisplayButton.setSize(91, 25);
                qrDisplayButton.setEnabled(flag);
		qrDisplayButton.addActionListener(new java.awt.event.ActionListener()
		{
                        @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            try {
                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                Integer customerID = customer.getCustomerID();
                                if(customerID > 0 && customerID != null) {
                                    VisitKarteDialog.ShowDialog(null, customerID, null);
                                }
                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
		});
		return qrDisplayButton;
        }
        
        /**
         * QR表示ボタン活性・非活性判定
         * 20170413 add #61376
         * @return true:活性 false:非活性
         */
        private boolean getQrDisplayButtonEnabled() {
            VisitKarteSetting vks = new VisitKarteSetting();
            return vks.isExists();
        }

        
        
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backPanel = new com.geobeck.swing.ImagePanel();
        searchResultScrollPane = new javax.swing.JScrollPane();
        searchResult = new com.geobeck.swing.JTableEx();
        cusKanaLabel = new javax.swing.JLabel();
        phoneNumberLabel = new javax.swing.JLabel();
        cellularNumberLabel = new javax.swing.JLabel();
        cusNameLabel = new javax.swing.JLabel();
        selectButton = new javax.swing.JButton();
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerName1.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        customerKana1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana1.getDocument()).setDocumentFilter(new CustomFilter(20));
        phoneNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)phoneNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        cellularNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)cellularNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));
        customerKana2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerKana2.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerName2.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        searchButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        customerNo1 = new com.geobeck.swing.JTextFieldEx();
        ((PlainDocument)customerNo1.getDocument()).setDocumentFilter(new CustomFilter(15));
        customerNo2 = new com.geobeck.swing.JTextFieldEx();
        ((PlainDocument)customerNo2.getDocument()).setDocumentFilter(new CustomFilter(15));
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        phoneNumberLabel1 = new javax.swing.JLabel();
        phoneNumberLabel2 = new javax.swing.JLabel();
        cellularNumberLabel1 = new javax.swing.JLabel();
        chargeStaffNo = new javax.swing.JTextField();
        chargeStaff = new javax.swing.JComboBox();
        chkNotMember = new javax.swing.JCheckBox();
        lblMail = new javax.swing.JLabel();
        txtMail = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)cellularNumber.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.PHONE_NUMBER));

        setTitle("顧客検索");
        setName("searchCustomerFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        backPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
        backPanel.setPreferredSize(new java.awt.Dimension(700, 560));
        backPanel.setRepeat(true);

        searchResultScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        searchResultScrollPane.setFocusable(false);

        searchResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "顧客情報", "顧客No.", "氏名", "前回来店日", "前回主担当", "生年月日", "電話番号", "携帯番号", "フィード登録", "フィード一覧", "二次元コード表示"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchResult.setToolTipText("");
        searchResult.setSelectionBackground(new java.awt.Color(255, 210, 142));
        searchResult.setSelectionForeground(new java.awt.Color(0, 0, 0));
        searchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResult.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(searchResult, SystemInfo.getTableHeaderRenderer());
        searchResult.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        searchResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchResultMouseClicked(evt);
            }
        });
        searchResultScrollPane.setViewportView(searchResult);
        searchResult.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        cusKanaLabel.setText("顧客No.");

        phoneNumberLabel.setText("電話番号");

        cellularNumberLabel.setText("携帯番号");

        cusNameLabel.setText("氏名");

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setFocusable(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCustomer(evt);
            }
        });

        customerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerName1.setColumns(20);
        customerName1.setInputKanji(true);

        customerKana1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerKana1.setColumns(20);
        customerKana1.setFocusCycleRoot(true);
        customerKana1.setInputKanji(true);

        phoneNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        phoneNumber.setColumns(20);

        cellularNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cellularNumber.setColumns(20);

        customerKana2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerKana2.setColumns(20);
        customerKana2.setInputKanji(true);

        customerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerName2.setColumns(20);
        customerName2.setInputKanji(true);

        searchButton.setIcon(SystemInfo.getImageIcon("/button/search/search_off.jpg"));
        searchButton.setBorderPainted(false);
        searchButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_on.jpg"));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setFocusable(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backPrevious(evt);
            }
        });

        jLabel1.setText("店舗");

        jLabel2.setText("ふりがな");

        jLabel3.setText("～");

        customerNo1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerNo1.setColumns(15);
        customerNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNo1FocusLost(evt);
            }
        });

        customerNo2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerNo2.setColumns(15);

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        phoneNumberLabel1.setForeground(java.awt.Color.red);
        phoneNumberLabel1.setText("※ 対象人数が１０００人を超える場合は、");

        phoneNumberLabel2.setForeground(java.awt.Color.red);
        phoneNumberLabel2.setText("　　最初の１０００人のみ表示されます。");

        cellularNumberLabel1.setText("前回主担当");

        chargeStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chargeStaffNoFocusLost(evt);
            }
        });

        chargeStaff.setMaximumRowCount(20);
        chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaff.setFocusable(false);
        chargeStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffActionPerformed(evt);
            }
        });

        chkNotMember.setText("非会員（顧客No. 0）を検索対象に含める");
        chkNotMember.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkNotMember.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkNotMember.setOpaque(false);

        lblMail.setText("メールアドレス");

        txtMail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtMail.setColumns(20);

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(searchResultScrollPane)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(cellularNumberLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .add(cellularNumberLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(phoneNumberLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cusKanaLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cusNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblMail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(txtMail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 257, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(12, 12, 12)
                        .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(backPanelLayout.createSequentialGroup()
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(customerKana1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(customerNo1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(14, 14, 14)
                        .add(jLabel3)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(backPanelLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(backPanelLayout.createSequentialGroup()
                                .add(13, 13, 13)
                                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(customerKana2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, backPanelLayout.createSequentialGroup()
                                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(customerNo2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 120, Short.MAX_VALUE)
                                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                            .add(phoneNumberLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .add(phoneNumberLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                    .add(backPanelLayout.createSequentialGroup()
                        .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(backPanelLayout.createSequentialGroup()
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cellularNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(phoneNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(chkNotMember)))
                .addContainerGap())
        );

        backPanelLayout.linkSize(new java.awt.Component[] {cellularNumber, customerKana1, customerKana2, customerName1, customerName2, customerNo1, customerNo2, phoneNumber, shop}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                            .add(customerKana2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(customerKana1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cusNameLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(customerNo2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(customerNo1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3)
                            .add(cusKanaLabel)))
                    .add(backPanelLayout.createSequentialGroup()
                        .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(48, 48, 48)
                        .add(phoneNumberLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(phoneNumberLabel2)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(phoneNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(phoneNumberLabel))
                        .add(8, 8, 8)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, cellularNumber, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                            .add(cellularNumberLabel)))
                    .add(backPanelLayout.createSequentialGroup()
                        .add(chkNotMember, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(8, 8, 8)))
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(backPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtMail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(backPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblMail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chargeStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(cellularNumberLabel1)
                        .add(chargeStaffNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        backPanelLayout.linkSize(new java.awt.Component[] {cellularNumber, customerKana1, customerKana2, customerName1, customerName2, customerNo1, customerNo2, phoneNumber, shop}, org.jdesktop.layout.GroupLayout.VERTICAL);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

        MstStaff ms= (MstStaff)chargeStaff.getSelectedItem();
        
        if (chargeStaff.getSelectedIndex() == 0) {
            chargeStaffNo.setText("");
        }
        
        if (ms.getStaffID() != null) {
            chargeStaffNo.setText(ms.getStaffNo());
        }
        
        if(!this.isShowing()) {
            return;
        }
    }//GEN-LAST:event_chargeStaffActionPerformed

    private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

        if (!chargeStaffNo.getText().equals("")) {
            
            MstStaff ms = new MstStaff();
            
            this.setStaff(chargeStaff, chargeStaffNo.getText());
            ms.setStaffID(((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
            
        } else {
            
            chargeStaff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_chargeStaffNoFocusLost

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.doSearchCustomer();
    }//GEN-LAST:event_formWindowOpened

    private void customerNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNo1FocusLost
        if (customerNo2.getText().length() == 0) {
            customerNo2.setText(customerNo1.getText());
        }
    }//GEN-LAST:event_customerNo1FocusLost

	private void searchResultMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_searchResultMouseClicked
	{//GEN-HEADEREND:event_searchResultMouseClicked

            if (this.isReferenceOnly) return;
            
            if (evt.getClickCount() == 2) {
                if (0 <= searchResult.getSelectedRow()) {
                    this.setSelectedCustomer();
                    this.setVisible(false);
                }
            }
	}//GEN-LAST:event_searchResultMouseClicked

	/**
	 * 検索ボタンが押されたときの処理
	 */
	private void searchButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_searchButtonActionPerformed
	{//GEN-HEADEREND:event_searchButtonActionPerformed
            searchButton.setCursor(null);
             
            this.searchCustomer();
	}//GEN-LAST:event_searchButtonActionPerformed

	/**
	 * 選択ボタンが押されたときの処理
	 */
	private void selectCustomer(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectCustomer
	{//GEN-HEADEREND:event_selectCustomer

            if (!this.isReferenceOnly) {
                this.setSelectedCustomer();
            }

            this.setVisible(false);

	}//GEN-LAST:event_selectCustomer

	/**
	 * 戻るボタンが押されたときの処理
	 */
	private void backPrevious(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backPrevious
	{//GEN-HEADEREND:event_backPrevious
		this.setVisible(false);
	}//GEN-LAST:event_backPrevious
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel backPanel;
    private com.geobeck.swing.JFormattedTextFieldEx cellularNumber;
    private javax.swing.JLabel cellularNumberLabel;
    private javax.swing.JLabel cellularNumberLabel1;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JCheckBox chkNotMember;
    private javax.swing.JLabel cusKanaLabel;
    private javax.swing.JLabel cusNameLabel;
    private com.geobeck.swing.JFormattedTextFieldEx customerKana1;
    private com.geobeck.swing.JFormattedTextFieldEx customerKana2;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private com.geobeck.swing.JTextFieldEx customerNo1;
    private com.geobeck.swing.JTextFieldEx customerNo2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblMail;
    private com.geobeck.swing.JFormattedTextFieldEx phoneNumber;
    private javax.swing.JLabel phoneNumberLabel;
    private javax.swing.JLabel phoneNumberLabel1;
    private javax.swing.JLabel phoneNumberLabel2;
    private javax.swing.JButton searchButton;
    private com.geobeck.swing.JTableEx searchResult;
    private javax.swing.JScrollPane searchResultScrollPane;
    private javax.swing.JButton selectButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private com.geobeck.swing.JFormattedTextFieldEx txtMail;
    // End of variables declaration//GEN-END:variables
	
	private MstCustomer selectedCustomer = null; 
	
	private ArrayList<MstCustomer> customers = new ArrayList<MstCustomer>();
	
	private	MoveNextField mnf = new MoveNextField();
        
        
	/**
	 * 顧客検索画面FocusTraversalPolicy
	 */
	private	MstCustomerFocusTraversalPolicy	ftp	=
			new MstCustomerFocusTraversalPolicy();
	
	/**
	 * 顧客検索画面FocusTraversalPolicyを取得する。
	 * @return 顧客検索画面FocusTraversalPolicy
	 */
	public MstCustomerFocusTraversalPolicy	getFocusTraversalPolicy()
	{
		return ftp;
	}
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{  
		SystemInfo.addMouseCursorChange(searchButton);
		SystemInfo.addMouseCursorChange(selectButton);
		SystemInfo.addMouseCursorChange(backButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
		cellularNumber.addKeyListener(SystemInfo.getMoveNextField());
		cellularNumber.addFocusListener(SystemInfo.getSelectText());
		customerKana1.addKeyListener(SystemInfo.getMoveNextField());
		customerKana1.addFocusListener(SystemInfo.getSelectText());
		customerKana2.addKeyListener(SystemInfo.getMoveNextField());
		customerKana2.addFocusListener(SystemInfo.getSelectText());
		customerName1.addKeyListener(SystemInfo.getMoveNextField());
		customerName1.addFocusListener(SystemInfo.getSelectText());
		customerName2.addKeyListener(SystemInfo.getMoveNextField());
		customerName2.addFocusListener(SystemInfo.getSelectText());
		phoneNumber.addKeyListener(SystemInfo.getMoveNextField());
		phoneNumber.addFocusListener(SystemInfo.getSelectText());
                customerNo1.addKeyListener(SystemInfo.getMoveNextField());
                customerNo1.addFocusListener(SystemInfo.getSelectText());
                customerNo2.addKeyListener(SystemInfo.getMoveNextField());
             	customerNo2.addFocusListener(SystemInfo.getSelectText());
		chargeStaffNo.addKeyListener(SystemInfo.getMoveNextField());
		chargeStaffNo.addFocusListener(SystemInfo.getSelectText());
	}
	
	/**
	 * 入力項目をクリアする。
	 */
	private void clear()
	{
	    customerName1.setText("");
	    customerName2.setText("");
	    customerKana1.setText("");
	    customerKana2.setText("");
	    phoneNumber.setText("");
	    cellularNumber.setText("");
            chargeStaffNo.setText("");
            chargeStaff.setSelectedIndex(0);
            
	    if (searchResult.getCellEditor() != null) {
		searchResult.getCellEditor().stopCellEditing();
	    }

	    SwingUtil.clearTable(searchResult);

	    //DefaultTableModel model = (DefaultTableModel)searchResult.getModel();
	}
	
	
	/**
	 * 検索ボタンを押したときの処理を行う。
	 */
	private void searchCustomer()
	{
	    if (searchResult.getCellEditor() != null) {
		searchResult.getCellEditor().stopCellEditing();
	    }

	    //顧客マスタデータリストをクリア
	    customers.clear();
	    
	    SwingUtil.clearTable(searchResult);
                
	    DefaultTableModel model = (DefaultTableModel)searchResult.getModel();
	    
	    //コネクションを取得
	    ConnectionWrapper con = SystemInfo.getConnection();

	    try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //条件SQL取得
                String whereSql = getWhereSQL();

                //前回売上データ一時テーブル作成                
                createTableLastSalesData(con, whereSql);

		//顧客マスタからデータを取得
               
		ResultSetWrapper rs = con.executeQuery(this.getSearchSQL(whereSql));
                
                // 20170413 add #61376
                boolean qrButtonEnabled = getQrDisplayButtonEnabled();

		while (rs.next()) {

		    MstCustomer mc = new MstCustomer();
		    //ResultSetからデータを取得
		    mc.setData(rs);
		    customers.add(mc);
			
		    Object[] rowData = { getUserSearchButton(mc.getCustomerID()),
					 mc.getCustomerNo(),
					 mc.getFullCustomerName(),
					 DateUtil.format(rs.getDate("sales_date"), "yyyy/MM/dd"),
					 rs.getString("staff_name"),
					 mc.getBirthdayString(),
					 mc.getPhoneNumber(),
					 mc.getCellularNumber(),
// 2016/12/24 顧客メモ ADD START
                                         getRegistMemoButton(mc),
                                         getMemoListButton(mc)
// 2016/12/24 顧客メモ ADD END
                                        // 20170413 add #61376
                                        , getQrDisplayButton(mc, qrButtonEnabled)

                                        };
		    model.addRow(rowData);
		}

		rs.close();

                searchResult.requestFocusInWindow();
                searchResult.changeSelection(0, 1, false, false);
                
	    } catch(SQLException e) {
                
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                
            } finally {
                
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }

	}

        private void createTableLastSalesData(ConnectionWrapper con, String whereSql) throws SQLException {

            try {
                //nhanvt start edit 20141014 Request #31115
                 ResultSetWrapper rs = con.executeQuery("SELECT TABLENAME FROM PG_TABLES WHERE TABLENAME = 'wk_last_sales_data'");
                if(rs.next())
                {
                    con.execute("drop table wk_last_sales_data");
                }
                rs.close();
                //nhanvt end edit 20141014 Request #31115
                
            } catch (Exception e) {
            }            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" create temporary table wk_last_sales_data as");
	    sql.append(" SELECT");
	    sql.append("      a.customer_id");
	    sql.append("     ,a.sales_date");
	    sql.append("     ,a.staff_id");
	    sql.append("     ,staff_name1 || ' ' || staff_name2 as staff_name");
	    sql.append(" FROM");
	    sql.append("     data_sales a");
	    sql.append("         join");
	    sql.append("             (");
	    sql.append("                 SELECT");
	    sql.append("                      a.shop_id");
	    sql.append("                     ,a.sales_date");
	    sql.append("                     ,a.customer_id");
	    sql.append("                     ,max(a.slip_no) as slip_no");
	    sql.append("                 FROM");
	    sql.append("                     data_sales a");
	    sql.append("                         inner join");
	    sql.append("                         (");
	    sql.append("                             SELECT");
	    sql.append("                                  customer_id");
	    sql.append("                                 ,max(to_char(sales_date, 'yyyy.mm.dd hh24:mi:ss') || to_char(insert_date, 'yyyy.mm.dd hh24:mi:ss')) as sales_date");
	    sql.append("                             FROM");
	    sql.append("                                 data_sales");
	    sql.append("                             WHERE");
	    sql.append("                                     sales_date is not null");
	    sql.append("                                 AND delete_date is null");
	    sql.append("                                 AND customer_id in (select customer_id from mst_customer mc" + whereSql + ")");
	    sql.append("                             GROUP BY");
	    sql.append("                                 customer_id");
	    sql.append("                         ) b");
	    sql.append("                         on a.customer_id = b.customer_id");
	    sql.append("                        and (to_char(a.sales_date, 'yyyy.mm.dd hh24:mi:ss') || to_char(a.insert_date, 'yyyy.mm.dd hh24:mi:ss')) = b.sales_date");
	    sql.append("                     WHERE");
	    sql.append("                         a.delete_date is null");
	    sql.append("                     GROUP BY");
	    sql.append("                          a.shop_id");
	    sql.append("                         ,a.sales_date");
	    sql.append("                         ,a.customer_id");
	    sql.append("             ) b");
	    sql.append("             using(shop_id, customer_id, sales_date, slip_no)");
	    sql.append("         join mst_shop");
	    sql.append("             using(shop_id)");
	    sql.append("         left join mst_staff");
	    sql.append("                      on a.staff_id = mst_staff.staff_id");
	    sql.append(" WHERE");
	    sql.append("         a.delete_date is null");

            con.execute(sql.toString());
           
          

        }

	private String getWhereSQL()
	{
            StringBuilder whereSql = new StringBuilder(1000);
	    whereSql.append(" where");
	    whereSql.append("         mc.delete_date is null");
	    whereSql.append("     and mc.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()));

            if (! chkNotMember.isSelected()) {
                whereSql.append("     and mc.customer_no <> '0'");
            }
            if (this.filterStaffID != null) {
            	whereSql.append("     and mc.customer_id !=" + this.filterStaffID);
            }
            if (this.filterShopID != null && this.filterContractNo != null && this.filterContractDetailNo != null){
            	whereSql.append("     and mc.customer_id not in (select customer_id from data_contract_share where shop_id="+SQLUtil.convertForSQL(this.filterShopID)+" and contract_no="+SQLUtil.convertForSQL(this.filterContractNo) +" and contract_detail_no="+SQLUtil.convertForSQL(this.filterContractDetailNo)+" and delete_date is null)");
            }

	    if (!customerNo1.getText().equals("")) {
                String s = customerNo1.getText();
                if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                    whereSql.append(" and translate(mc.customer_no, '0123456789', '') = ''");
                    whereSql.append(" and mc.customer_no::text::numeric >= " + s);
                } else {
                    whereSql.append(" and mc.customer_no >= '" + s + "'");
                }
	    }
	    if (!customerNo2.getText().equals("")) {
                String s = customerNo2.getText();
                if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                    whereSql.append(" and translate(mc.customer_no, '0123456789', '') = ''");
                    whereSql.append(" and mc.customer_no::text::numeric <= " + s);
                } else {
                    whereSql.append(" and mc.customer_no <= '" + s + "'");
                }
	    }

            if (!customerName1.getText().equals("")) {
		whereSql.append(" and mc.customer_name1 like '" + customerName1.getText() + "%'");
	    }
	    if (!customerName2.getText().equals("")) {
		whereSql.append(" and mc.customer_name2 like '" + customerName2.getText() + "%'");
	    }

	    if (!customerKana1.getText().equals("")) {
		whereSql.append(" and mc.customer_kana1 like '" + customerKana1.getText() + "%'");
	    }
	    if (!customerKana2.getText().equals("")) {
		whereSql.append(" and mc.customer_kana2 like '" + customerKana2.getText() + "%'");
	    }

	    if (!phoneNumber.getText().equals("")) {
		whereSql.append(" and mc.phone_number like '" + phoneNumber.getText() + "%'");
	    }

	    if (!cellularNumber.getText().equals("")) {
		whereSql.append(" and mc.cellular_number like '" + cellularNumber.getText() + "%'");
	    }
            //nhanvt start 20141014 add Request #31115
            if (!txtMail.getText().equals("")) {
		whereSql.append(" and ( lower(mc.pc_mail_address) like '%" + txtMail.getText().toLowerCase() + "%' ");
                whereSql.append(" or lower(mc.cellular_mail_address) like '%" + txtMail.getText().toLowerCase() + "%' ) ");
	    }
            //nhanvt end 20141014 add Request #31115
	    
	    return whereSql.toString();
        }

	/**
	 * 顧客検索用ＳＱＬ文を取得する。
	 * @return 顧客検索用ＳＱＬ文
	 */
	private String getSearchSQL(String whereSql)
	{               
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
	    sql.append("      mc.*");
	    sql.append("     ,ds.sales_date");
	    sql.append("     ,ds.staff_name");
	    sql.append(" from");
	    sql.append("     mst_customer mc");
	    sql.append("         left join wk_last_sales_data ds");
	    sql.append("                on mc.customer_id = ds.customer_id");

	    sql.append(whereSql);

	    if (chargeStaff.getSelectedIndex() > 0) {
		sql.append(" and ds.staff_id = " + ((MstStaff)chargeStaff.getSelectedItem()).getStaffID());
	    }
            
	    sql.append(" order by");
	    sql.append("      mc.customer_no");
	    sql.append("     ,mc.customer_id");

	    sql.append(" limit 1000");

	    return sql.toString();
        }
	
	/**
	 * 選択された顧客を取得する。
	 * @return 選択された顧客
	 */
	public MstCustomer getSelectedCustomer()
	{
		return	selectedCustomer;
	}
	
	/**
	 * 選択された顧客をセットする。
	 */
	private void setSelectedCustomer()
	{
	    if (0 <= searchResult.getSelectedRow()) {
		selectedCustomer = customers.get(searchResult.getSelectedRow());
	    }
	}

	/**
	 * 顧客検索画面FocusTraversalPolicy
	 */
	private class MstCustomerFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * 
		 * @param focustomerCycleRoot 
		 * @param aComponent 
		 * @return 
		 */
		public Component getComponentAfter(Container focustomerCycleRoot,
										   Component aComponent)
		{
                        if (aComponent.equals(customerNo1))
			{
                           	return customerNo2;
			}
			else if (aComponent.equals(customerNo2))
			{
				return phoneNumber;		              
                        }                  
                        else if (aComponent.equals(customerName1))
			{
				return customerName2;
			}
			else if (aComponent.equals(customerName2))
			{
				return customerNo1;
			}
			else if (aComponent.equals(customerKana1))
			{
				return customerKana2;
			}
			else if (aComponent.equals(customerKana2))
			{
				return customerName1;
			}
			else if (aComponent.equals(phoneNumber))
			{
				return cellularNumber;
			}
			else if (aComponent.equals(cellularNumber))
			{
				return chargeStaffNo;
			}
			else if (aComponent.equals(chargeStaffNo))
			{
				//return chargeStaff;
//                                if(shop.getItemCount() == 1){
//                                    return customerKana1;
//                                }
//                                return shop;
                            return searchButton;
			}
			else if (aComponent.equals(chargeStaff))
			{
//				if(shop.getItemCount() == 1){
//                                    return customerKana1;
//                                }
//                                return shop;
                            return searchButton;
			}
			
			return  getStartComponent();
		}

		/**
		 * 
		 * @param focustomerCycleRoot 
		 * @param aComponent 
		 * @return 
		 */
		public Component getComponentBefore(Container focustomerCycleRoot,
											Component aComponent)
		{

 			if (aComponent.equals(customerNo1))
			{
				return customerName2;
			}
			else if (aComponent.equals(customerNo2))
			{
				return customerNo1;
			}
                        else if (aComponent.equals(customerName1))
			{
				return customerKana2;
			}
			else if (aComponent.equals(customerName2))
			{
				return customerName1;
			}
			else if (aComponent.equals(customerKana1))
			{
				return customerKana1;
			}
			else if (aComponent.equals(customerKana2))
			{
				return customerKana1;
			}
			else if (aComponent.equals(phoneNumber))
			{
				return customerNo2;
			}
			else if (aComponent.equals(cellularNumber))
			{
				return phoneNumber;
			}
			else if (aComponent.equals(searchButton))
			{
				return chargeStaffNo;
			}
			else if (aComponent.equals(chargeStaffNo))
			{
				return cellularNumber;
			}
			else if (aComponent.equals(searchResult))
			{
				return searchButton;
			}
			
			return customerName1;
		}

		/**
		 * 
		 * @param focustomerCycleRoot 
		 * @return 
		 */
		public Component getDefaultComponent(Container focustomerCycleRoot)
		{
			return  getStartComponent();
		}

		/**
		 * 
		 * @param focustomerCycleRoot 
		 * @return 
		 */
		public Component getLastComponent(Container focustomerCycleRoot)
		{
			return searchResult;
		}

		/**
		 * 
		 * @param focustomerCycleRoot 
		 * @return 
		 */
		public Component getFirstComponent(Container focustomerCycleRoot)
		{
			return  getStartComponent();
		}
                public Component getStartComponent()
		{
			if(shop.getItemCount() ==1){
                            return customerKana1;
                        }
                        return shop;
		}
	}
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
// 2016/12/24 顧客メモ MOD START
		searchResult.getColumnModel().getColumn(0).setPreferredWidth(68);
//		searchResult.getColumnModel().getColumn(1).setPreferredWidth(80);
                searchResult.getColumnModel().getColumn(1).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(2).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(3).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(4).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(5).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(6).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(7).setPreferredWidth(80);
// 2016/12/24 顧客メモ MOD END
// 2016/12/24 顧客メモ ADD START
                searchResult.getColumnModel().getColumn(8).setPreferredWidth(80);
                searchResult.getColumnModel().getColumn(9).setPreferredWidth(80);
// 2016/12/24 顧客メモ ADD END
                // 20170413 add #61376
                searchResult.getColumnModel().getColumn(10).setPreferredWidth(100);
	}
	
	
       	/**
	 * 選択されている店舗を取得する。
	 * @return 選択されている店舗
	 */
	private MstShop getSelectedShop()
	{
            if (0 <= shop.getSelectedIndex()) {
                return (MstShop)shop.getSelectedItem();
            } else {
                return null;
            }
	}
	
	/**
	 * 選択されている店舗のIDを取得する。
	 * @return 選択されている店舗のID
	 */
	private Integer getSelectedShopID()
	{
            if (SystemInfo.getSetteing().isShareCustomer()) {
                return 0;
            }
		
            MstShop ms = this.getSelectedShop();
		
            if (ms != null) {
                return ms.getShopID();
            } else {
                return 0;
            }
	}

        public void setSearchCustomerNo(String searchCustomerNo) {
            this.searchCustomerNo = searchCustomerNo;
        }

        public void setSearchCustomerName1(String searchCustomerName1) {
            this.searchCustomerName1 = searchCustomerName1;
        }

        public void setSearchCustomerName2(String searchCustomerName2) {
            this.searchCustomerName2 = searchCustomerName2;
        }

        public void setSearchCustomerKana1(String searchCustomerKana1) {
            this.searchCustomerKana1 = searchCustomerKana1;
        }

        public void setSearchCustomerKana2(String searchCustomerKana2) {
            this.searchCustomerKana2 = searchCustomerKana2;
        }

        private boolean existSearchCondition() {

            boolean result = false;

            result = result || this.searchCustomerName1.length() > 0;
            result = result || this.searchCustomerName2.length() > 0;
            result = result || this.searchCustomerKana1.length() > 0;
            result = result || this.searchCustomerKana2.length() > 0;
            result = result || this.searchCustomerNo.length() > 0;

            return result;
        }

        private void setSearchCondition() {
            this.customerName1.setText(this.searchCustomerName1);
            this.customerName2.setText(this.searchCustomerName2);
            this.customerKana1.setText(this.searchCustomerKana1);
            this.customerKana2.setText(this.searchCustomerKana2);
            this.customerNo1.setText(this.searchCustomerNo);
            this.customerNo2.setText(this.searchCustomerNo);
        }

        public void doSearchCustomer() {
            if (existSearchCondition()) {
                setSearchCondition();
                this.searchCustomer();
            }
        }
        
	private void setStaffs()
	{
            MstStaffs staffs = new MstStaffs();

            if (this.staffShopID != null) {
                staffs.setShopIDList(this.staffShopID.toString());
            } else {
                staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
            }

            try {
                
                staffs.load(SystemInfo.getConnection(), true);

                chargeStaff.removeAllItems();
		for (MstStaff ms : staffs) {
                    if (ms.isDisplay()) {
                        chargeStaff.addItem(ms);
                    }
		}
                
		chargeStaff.setSelectedIndex(0);
                
                
            } catch(Exception e) {
                
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
	}
        
	/**
	 * 指定したスタッフＩＤをスタッフ選択用JComboBoxで選択状態にする。
	 * @param staffCombo スタッフ選択用JComboBox
	 * @param staffID スタッフＩＤ
	 */
	private void setStaff(JComboBox staffCombo, Integer staffID) {
                
	    for (int i = 0; i < staffCombo.getItemCount(); i++) {

		 MstStaff ms  = (MstStaff)staffCombo.getItemAt(i);

		 //空白をセット
		if (ms.getStaffID() == null) {
		     
		   staffCombo.setSelectedIndex(0);
		   
		} else if ( ms.getStaffID().equals(staffID)) {
		     
		    staffCombo.setSelectedIndex(i);
		    return;
		}
	    }
	}

	private void setStaff(JComboBox staffCombo, String staffNo) {
                
	    for (int i = 0; i < staffCombo.getItemCount(); i++) {

		 MstStaff ms  = (MstStaff)staffCombo.getItemAt(i);

		 //空白をセット
		if (ms.getStaffID() == null) {
		     
		   staffCombo.setSelectedIndex(0);
		   
		} else if ( ms.getStaffNo().equals(staffNo)) {
		     
		    staffCombo.setSelectedIndex(i);
		    return;
		}
	    }
	}
       
// 2017/01/02 顧客メモ ADD START
    /**
     * 顧客メモ機能有効判定
     * 
     * @return 顧客メモ有効時、true。無効時、false
     */
    private boolean isCustomerMemoEnabled() {
        // ログインDBがpos_hair_nonailX以外であれば顧客メモ機能は無効
        return SystemInfo.getDatabase().startsWith("pos_hair_nonail");
    }
// 2017/01/02 顧客メモ ADD END
}

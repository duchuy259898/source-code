/*
 * AccountTransferPanel.java
 *
 * Created on 2020/03/23
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;
// <editor-fold defaultstate="collapsed" desc="口座振替連携">
import com.geobeck.sosia.pos.basicinfo.SelectSameNoData;
import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.data.account.DataPaymentDetail;
import com.geobeck.sosia.pos.hair.customer.MstCustomerPanel;
import com.geobeck.sosia.pos.hair.data.member.DataAccountInfo;
import com.geobeck.sosia.pos.hair.data.member.DataAccountInfoList;
import com.geobeck.sosia.pos.hair.data.member.DataMonthlyBatchLogs;
import com.geobeck.sosia.pos.hair.data.member.DataTransferResult;
import com.geobeck.sosia.pos.hair.data.member.DataTransferResultDetail;
import com.geobeck.sosia.pos.hair.data.member.MstAccountTransferSetting;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.account.MstPaymentClass;
import com.geobeck.sosia.pos.master.account.MstPaymentClasses;
import com.geobeck.sosia.pos.master.account.MstPaymentMethod;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import com.geobeck.swing.JTableEx;
import com.geobeck.swing.filechooser.WildcardFileFilter;
import com.geobeck.util.CheckUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.PlainDocument;
import org.apache.commons.lang.StringUtils;

// </editor-fold>

/**
 *口座振替連携
 * @author  lvtu
 */

public class AccountTransferPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    /**銀行コードの最大長*/
    final int                           MAX_LEN_BANK_CODE               = 4;
    /**最大長のブランチコード*/
    final int                           MAX_LEN_BRANCH_CODE             = 3;
    /**委託者コードの最大長*/
    final int                           MAX_LEN_CONSIGNOR_NUMBER        = 6;
    /**区分の最大長*/
    final int                           MAX_LEN_DIVISION_NUMBER         = 2;
    /**最大長のアカウント番号*/
    final int                           MAX_LEN_ACCOUNT_NUMBER          = 7;
    /** header checked process*/
    final int                           COL_CHECK                       = 11;
    /**ヘッダーレコード */
    final int                           HEADER_RECORD                   = 1;
    /**ヘッダーレコード */
    final int                           DATA_RECORD                     = 2;
    /**ヘッダーレコード */
    final int                           TRAILER_RECORD                  = 8;
    /**ヘッダーレコード */
    final int                           END_RECORD                      = 9;
    
    /**https://zengin.ajtw.net/をブラウザで開く*/
    final String                        URL_SEARCH                      = "https://zengin.ajtw.net/";
    
    private Integer                     selIndex                        = -1;
    //顧客検索
    private MstCustomer                 customer                        = new MstCustomer();
    //口座情報
    private DataAccountInfoList		accountInfoList			= new DataAccountInfoList();
    //一括処理ログ
    DataMonthlyBatchLogs                dataMonthlyBatchLogs            = new DataMonthlyBatchLogs();
    //口座振替基本情報
    MstAccountTransferSetting           mstAccountTransferSetting       = new MstAccountTransferSetting();
    //請求・結果情報ヘッダ
    DataTransferResult                  dataTransferResult              = new DataTransferResult();
    String                              csvPath                         = StringUtils.EMPTY;
    

    /** コンストラクタ */
    public AccountTransferPanel() {
        initComponents();
        this.setSize(834, 691);
        this.setPath("基本設定＞月会員管理");
        this.setTitle("口座振替連携");
        init();
    }
    
    /* コンボボックスの内容初期化など */
    private void init() {
        
        setKeyListener();
        setFocusListener();
        addMouseCursorChange();
        refreshDataAccountInfo();
        /** 請求・結果 */
        initBillingAndResult();
        //基本情報
        loadAccountTransferSetting();
        initPaymentMethod();
    }
    
    /** Enter押下時フォーカス移動の設定 */
    private void setKeyListener() {
        //口座情報登録
        customerNo.addKeyListener(SystemInfo.getMoveNextField());
        txtAccountCustomerNo.addKeyListener(SystemInfo.getMoveNextField());
        txtBankCode.addKeyListener(SystemInfo.getMoveNextField());
        txtBankName.addKeyListener(SystemInfo.getMoveNextField());
        txtBranchCode.addKeyListener(SystemInfo.getMoveNextField());
        txtBranchName.addKeyListener(SystemInfo.getMoveNextField());
        rdoDepositNormal.addKeyListener(new KeyListenerForRadioButton(
                null, rdoDepositInterim,
                txtBranchName, txtAccountNumber));
        rdoDepositInterim.addKeyListener(new KeyListenerForRadioButton(
                rdoDepositNormal, null,
                txtBranchName, txtAccountNumber));
        txtAccountNumber.addKeyListener(SystemInfo.getMoveNextField());
        txtAccountName.addKeyListener(SystemInfo.getMoveNextField());
        // 請求・結果
        rdoProcessingBilling.addKeyListener(new KeyListenerForRadioButton(
                null, rdoProcessingResult,
                txtTotalbilledAmount, cmbDateTargetMonth));
        rdoProcessingResult.addKeyListener(new KeyListenerForRadioButton(
                rdoProcessingBilling, null,
                txtTotalbilledAmount, cmbDateTargetMonth));
        cmbDateTargetMonth.addKeyListener(SystemInfo.getMoveNextField());
        cmbYear.addKeyListener(SystemInfo.getMoveNextField());
        cmbMonth.addKeyListener(SystemInfo.getMoveNextField());
        txtTrasferDay.addKeyListener(SystemInfo.getMoveNextField());
        txtTotalNumberOfBills.addKeyListener(SystemInfo.getMoveNextField());
        txtTotalbilledAmount.addKeyListener(SystemInfo.getMoveNextField());
        dateCollectionDate.addKeyListener(SystemInfo.getMoveNextField());
        //委託者情報登録
        txtConsignorCode.addKeyListener(SystemInfo.getMoveNextField());
        txtDivision.addKeyListener(SystemInfo.getMoveNextField());
        txtConsignorName.addKeyListener(SystemInfo.getMoveNextField());
        cmbTransferCusDate.addKeyListener(SystemInfo.getMoveNextField());
        cmbPayment.addKeyListener(SystemInfo.getMoveNextField());
    }
    
    /** フォーカス取得時テキスト全選択の設定 */
    private void setFocusListener(){
        txtAccountCustomerNo.addFocusListener(SystemInfo.getSelectText());
        txtBankCode.addFocusListener(SystemInfo.getSelectText());
        txtBankName.addFocusListener(SystemInfo.getSelectText());
        txtBranchCode.addFocusListener(SystemInfo.getSelectText());
        txtBranchName.addFocusListener(SystemInfo.getSelectText());
        txtAccountNumber.addFocusListener(SystemInfo.getSelectText());
        txtAccountName.addFocusListener(SystemInfo.getSelectText());
        // 請求・結果
        txtTrasferDay.addFocusListener(SystemInfo.getSelectText());
        txtTotalNumberOfBills.addFocusListener(SystemInfo.getSelectText());
        txtTotalbilledAmount.addFocusListener(SystemInfo.getSelectText());
        //委託者情報登録
        txtConsignorCode.addFocusListener(SystemInfo.getSelectText());
        txtDivision.addFocusListener(SystemInfo.getSelectText());
        txtConsignorName.addFocusListener(SystemInfo.getSelectText());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DivisionGroup1 = new javax.swing.ButtonGroup();
        DepositTypeGroup = new javax.swing.ButtonGroup();
        DivisionGroup2 = new javax.swing.ButtonGroup();
        ProcessingGroup = new javax.swing.ButtonGroup();
        jTabbedPaneRoot = new javax.swing.JTabbedPane();
        panelRegistAccount = new javax.swing.JPanel();
        panelScrollAccounts = new javax.swing.JScrollPane();
        panelAccounts = new com.geobeck.swing.JTableEx();
        panelFormAccount = new javax.swing.JPanel();
        customerNoLabel = new javax.swing.JLabel();
        customerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        rdoDepositNormal = new javax.swing.JRadioButton();
        customerNameLable = new javax.swing.JLabel();
        rdoDepositInterim = new javax.swing.JRadioButton();
        searchCustomerButton = new javax.swing.JButton();
        lbDepositType = new javax.swing.JLabel();
        txtBankCode = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtBankCode.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        renewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        btBankSearch = new javax.swing.JButton();
        txtBankName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtBankName.getDocument()).setDocumentFilter(
            new CustomFilter(15));
        lbBankCode = new javax.swing.JLabel();
        lbBankName = new javax.swing.JLabel();
        lbBranchCode = new javax.swing.JLabel();
        txtBranchCode = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtBranchCode.getDocument()).setDocumentFilter(
            new CustomFilter(3, CustomFilter.NUMERIC));
        txtBranchName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtBranchName.getDocument()).setDocumentFilter(
            new CustomFilter(15));
        lbBranchName = new javax.swing.JLabel();
        lbAccountNumber = new javax.swing.JLabel();
        txtAccountNumber = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtAccountNumber.getDocument()).setDocumentFilter(
            new CustomFilter(7, CustomFilter.NUMERIC));
        lbAccountHolder = new javax.swing.JLabel();
        txtAccountName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtAccountName.getDocument()).setDocumentFilter(
            new CustomAccountFilter(30));
        lbBranchCodeMemo = new javax.swing.JLabel();
        lbAccountNumberMemo = new javax.swing.JLabel();
        lbAccountHolderMemo = new javax.swing.JLabel();
        btClear = new javax.swing.JButton();
        lbAccountHolderMemo1 = new javax.swing.JLabel();
        txtAccountCustomerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        lbAccountCustomerNo = new javax.swing.JLabel();
        lbAccountCustomerNoNotes = new javax.swing.JLabel();
        panelBilling = new javax.swing.JPanel();
        panelScrollBillingResult = new javax.swing.JScrollPane();
        panelBillingResult = new com.geobeck.swing.JTableEx();
        panelDetailBilling = new javax.swing.JPanel();
        btFileSelection = new javax.swing.JButton();
        rdoProcessingBilling = new javax.swing.JRadioButton();
        rdoProcessingResult = new javax.swing.JRadioButton();
        btInvoiceList = new javax.swing.JButton();
        lbProcessing = new javax.swing.JLabel();
        lbTargetMonth = new javax.swing.JLabel();
        yeaLabel = new javax.swing.JLabel();
        monthLable = new javax.swing.JLabel();
        cmbMonth = new javax.swing.JComboBox();
        cmbYear = new javax.swing.JComboBox();
        lbTransferDate = new javax.swing.JLabel();
        lbTotalNumberOfBills = new javax.swing.JLabel();
        txtTotalNumberOfBills = new com.geobeck.swing.JFormattedTextFieldEx(SystemInfo.getDecimalFormatter());
        lbTotalbilledAmount = new javax.swing.JLabel();
        txtTotalbilledAmount = new com.geobeck.swing.JFormattedTextFieldEx(SystemInfo.getDecimalFormatter());
        lbTotalNumberOfBillsMemo = new javax.swing.JLabel();
        lbTotalbilledAmountMemo = new javax.swing.JLabel();
        btRegist = new javax.swing.JButton();
        btExcelOutput = new javax.swing.JButton();
        btBillingDataOutput = new javax.swing.JButton();
        btResultData = new javax.swing.JButton();
        dateCollectionDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lbCollectionDate = new javax.swing.JLabel();
        dayLable = new javax.swing.JLabel();
        txtTrasferDay = new com.geobeck.swing.JFormattedTextFieldEx();
        cmbDateTargetMonth = new javax.swing.JComboBox();
        txtCSVPath = new com.geobeck.swing.JFormattedTextFieldEx();
        panelRegisterCustomerInfo = new javax.swing.JPanel();
        registerCustomerInfo = new javax.swing.JPanel();
        btnRegistCustomer = new javax.swing.JButton();
        TransferCusDateMemo = new javax.swing.JLabel();
        cmbTransferCusDate = new javax.swing.JComboBox();
        lbTransferCusDate = new javax.swing.JLabel();
        lbConsignorCode = new javax.swing.JLabel();
        txtConsignorCode = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtConsignorCode.getDocument()).setDocumentFilter(
            new CustomFilter(6, CustomFilter.NUMERIC));
        lbConsignorCodeMemo = new javax.swing.JLabel();
        lbClassificationMemo = new javax.swing.JLabel();
        lbClassification = new javax.swing.JLabel();
        txtDivision = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtDivision.getDocument()).setDocumentFilter(
            new CustomFilter(2, CustomFilter.NUMERIC));
        lbConsignorNameMemo = new javax.swing.JLabel();
        lbConsignorName = new javax.swing.JLabel();
        txtConsignorName = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtConsignorName.getDocument()).setDocumentFilter(
            new CustomAccountFilter(30));
        cmbPayment = new JComboBox();
        paymentLabel = new javax.swing.JLabel();
        paymentLableMemo = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        jTabbedPaneRoot.setPreferredSize(new java.awt.Dimension(626, 459));

        panelRegistAccount.setOpaque(false);

        panelScrollAccounts.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        panelAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "顧客情報", "顧客No", "顧客名", "銀行コード", "銀行名", "支店コード", "支店名", "預金種別", "口座番号", "口座名義"
            }) {
                Class[] types = new Class [] {
                    Object.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class};
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
            panelAccounts.setSelectionBackground(new java.awt.Color(255, 210, 142));
            panelAccounts.setSelectionForeground(new java.awt.Color(0, 0, 0));
            panelAccounts.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(panelAccounts, SystemInfo.getTableHeaderRenderer());
            panelAccounts.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            initTableAccountsInfoColumnWidth();
            SelectTableCellRenderer.setSelectTableCellRenderer(panelAccounts);
            panelAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    panelAccountsMouseReleased(evt);
                }
            });
            panelAccounts.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    panelAccountsKeyReleased(evt);
                }
            });
            panelScrollAccounts.setViewportView(panelAccounts);

            customerNoLabel.setText("顧客No.");

            customerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            customerNo.setColumns(15);
            customerNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
            customerNo.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    customerNoFocusLost(evt);
                }
            });

            customerName2.setEditable(false);
            customerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            customerName2.setFocusable(false);
            customerName2.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
            customerName2.setInputKanji(true);

            customerName1.setEditable(false);
            customerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            customerName1.setFocusable(false);
            customerName1.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
            customerName1.setInputKanji(true);

            DepositTypeGroup.add(rdoDepositNormal);
            rdoDepositNormal.setSelected(true);
            rdoDepositNormal.setText("普通");
            rdoDepositNormal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDepositNormal.setContentAreaFilled(false);
            rdoDepositNormal.setMargin(new java.awt.Insets(0, 0, 0, 0));

            customerNameLable.setText("顧客名");

            DepositTypeGroup.add(rdoDepositInterim);
            rdoDepositInterim.setText("当座");
            rdoDepositInterim.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDepositInterim.setContentAreaFilled(false);
            rdoDepositInterim.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoDepositInterim.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rdoDepositInterimActionPerformed(evt);
                }
            });

            searchCustomerButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
            searchCustomerButton.setBorderPainted(false);
            searchCustomerButton.setContentAreaFilled(false);
            searchCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
            searchCustomerButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    searchCustomerButtonActionPerformed(evt);
                }
            });

            lbDepositType.setText("預金種別");

            txtBankCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtBankCode.setColumns(30);

            renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
            renewButton.setBorderPainted(false);
            renewButton.setEnabled(false);
            renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
            renewButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    renewButtonActionPerformed(evt);
                }
            });

            deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
            deleteButton.setBorderPainted(false);
            deleteButton.setEnabled(false);
            deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
            deleteButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deleteButtonActionPerformed(evt);
                }
            });

            addButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
            addButton.setBorderPainted(false);
            addButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
            addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addButtonActionPerformed(evt);
                }
            });

            btBankSearch.setIcon(SystemInfo.getImageIcon("/button/search/code_search_off.jpg"));
            btBankSearch.setBorderPainted(false);
            btBankSearch.setPressedIcon(SystemInfo.getImageIcon("/button/search/code_search_on.jpg"));
            btBankSearch.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btBankSearchActionPerformed(evt);
                }
            });

            txtBankName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtBankName.setColumns(30);
            txtBankName.setInputKanji(true);

            lbBankCode.setText("銀行コード");

            lbBankName.setText("銀行名");

            lbBranchCode.setText("支店コード");

            txtBranchCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtBranchCode.setColumns(30);

            txtBranchName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtBranchName.setColumns(30);
            txtBranchName.setInputKanji(true);

            lbBranchName.setText("支店名");

            lbAccountNumber.setText("口座番号");

            txtAccountNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtAccountNumber.setColumns(30);

            lbAccountHolder.setText("口座名義");

            txtAccountName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtAccountName.setColumns(30);

            lbBranchCodeMemo.setText("＊郵便局の場合は、通帳記号の中3桁");

            lbAccountNumberMemo.setText("＊郵便局の場合は、通帳番号の上7桁");

            lbAccountHolderMemo.setText("＊30文字（半角カナ大文字、半角英数大文字）、");

            btClear.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
            btClear.setBorderPainted(false);
            btClear.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
            btClear.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btClearActionPerformed(evt);
                }
            });

            lbAccountHolderMemo1.setText("カナ小文字は不可");
            lbAccountHolderMemo1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            ((PlainDocument)txtAccountCustomerNo.getDocument()).setDocumentFilter(
                new CustomFilter(14, CustomFilter.NUMERIC));
            txtAccountCustomerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtAccountCustomerNo.setColumns(30);
            txtAccountCustomerNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N

            lbAccountCustomerNo.setText("振替用顧客No");

            lbAccountCustomerNoNotes.setText("＊数字のみ14桁まで（口座振替依頼書記載の顧客Noを登録してください）");

            javax.swing.GroupLayout panelFormAccountLayout = new javax.swing.GroupLayout(panelFormAccount);
            panelFormAccount.setLayout(panelFormAccountLayout);
            panelFormAccountLayout.setHorizontalGroup(
                panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFormAccountLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                            .addComponent(customerNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(customerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(searchCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lbAccountCustomerNo, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                .addComponent(customerNameLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbBankCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbBranchCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbBranchName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtBankCode, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                                            .addComponent(txtBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lbBranchCodeMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lbAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbDepositType, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                                    .addComponent(rdoDepositNormal)
                                                    .addGap(15, 15, 15)
                                                    .addComponent(rdoDepositInterim))
                                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                                    .addComponent(txtAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(lbAccountNumberMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                                            .addComponent(lbAccountHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtAccountName, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lbAccountHolderMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                                    .addGap(10, 10, 10)
                                                    .addComponent(lbAccountHolderMemo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                .addComponent(txtBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtAccountCustomerNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormAccountLayout.createSequentialGroup()
                                            .addComponent(customerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(6, 6, 6)
                                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(btBankSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(customerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(txtBankName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbAccountCustomerNoNotes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormAccountLayout.createSequentialGroup()
                            .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(renewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            panelFormAccountLayout.setVerticalGroup(
                panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFormAccountLayout.createSequentialGroup()
                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(customerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                            .addGap(13, 13, 13)
                            .addComponent(customerNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(6, 6, 6)
                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(customerNameLable, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(customerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(customerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(6, 6, 6)
                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbAccountCustomerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAccountCustomerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbAccountCustomerNoNotes, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtBankCode, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbBankCode, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btBankSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbAccountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbAccountNumberMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtAccountName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbAccountHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbBranchCodeMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbAccountHolderMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(panelFormAccountLayout.createSequentialGroup()
                                    .addComponent(lbAccountHolderMemo1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(renewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(panelFormAccountLayout.createSequentialGroup()
                            .addGroup(panelFormAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbDepositType, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoDepositNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoDepositInterim, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE))))
            );

            javax.swing.GroupLayout panelRegistAccountLayout = new javax.swing.GroupLayout(panelRegistAccount);
            panelRegistAccount.setLayout(panelRegistAccountLayout);
            panelRegistAccountLayout.setHorizontalGroup(
                panelRegistAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRegistAccountLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(panelRegistAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelScrollAccounts)
                        .addComponent(panelFormAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 805, Short.MAX_VALUE)))
            );
            panelRegistAccountLayout.setVerticalGroup(
                panelRegistAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRegistAccountLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(panelFormAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)
                    .addComponent(panelScrollAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jTabbedPaneRoot.addTab("　口座情報登録　", panelRegistAccount);

            panelBilling.setOpaque(false);

            panelScrollBillingResult.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            panelScrollBillingResult.setAutoscrolls(true);

            panelBillingResult.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "顧客No", "顧客名", "銀行コード", "銀行名", "支店コード", "支店名", "預金種別", "口座番号", "口座名義", "請求金額", "振替結果", ""
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false, false, true
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            panelBillingResult.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            panelBillingResult.setSelectionBackground(new java.awt.Color(255, 210, 142));
            panelBillingResult.setSelectionForeground(new java.awt.Color(0, 0, 0));
            panelBillingResult.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(panelBillingResult, SystemInfo.getTableHeaderRenderer());
            panelBillingResult.getColumnModel().getColumn(this.COL_CHECK).setHeaderRenderer(this.getTableHeaderRenderer(panelBillingResult.getTableHeader()));
            panelBillingResult.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            initTableBillingResultColumnWidth();
            SelectTableCellRenderer.setSelectTableCellRenderer(panelBillingResult);
            panelScrollBillingResult.setViewportView(panelBillingResult);

            btFileSelection.setIcon(SystemInfo.getImageIcon("/button/select/file_select_off.jpg")
            );
            btFileSelection.setBorderPainted(false);
            btFileSelection.setPressedIcon(SystemInfo.getImageIcon("/button/select/file_select_on.jpg")
            );
            btFileSelection.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btFileSelectionActionPerformed(evt);
                }
            });

            ProcessingGroup.add(rdoProcessingBilling);
            rdoProcessingBilling.setSelected(true);
            rdoProcessingBilling.setText("請求データ出力");
            rdoProcessingBilling.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoProcessingBilling.setContentAreaFilled(false);
            rdoProcessingBilling.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoProcessingBilling.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    rdoProcessingBillingItemStateChanged(evt);
                }
            });

            ProcessingGroup.add(rdoProcessingResult);
            rdoProcessingResult.setText("振替結果取込");
            rdoProcessingResult.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoProcessingResult.setContentAreaFilled(false);
            rdoProcessingResult.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoProcessingResult.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    rdoProcessingResultItemStateChanged(evt);
                }
            });

            btInvoiceList.setIcon(SystemInfo.getImageIcon("/button/custom/claim_target_list_off.jpg")
            );
            btInvoiceList.setBorderPainted(false);
            btInvoiceList.setPressedIcon(SystemInfo.getImageIcon("/button/custom/claim_target_list_on.jpg")
            );
            btInvoiceList.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btInvoiceListActionPerformed(evt);
                }
            });

            lbProcessing.setText("処理区分");

            lbTargetMonth.setText("対象月");

            yeaLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            yeaLabel.setText("年");

            monthLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            monthLable.setText("月");

            cmbMonth.setMaximumRowCount(13);
            cmbMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            cmbYear.setMaximumRowCount(12);
            cmbYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            lbTransferDate.setText("口座振替日");

            lbTotalNumberOfBills.setText("請求合計件数");

            ((PlainDocument)txtTotalNumberOfBills.getDocument()).setDocumentFilter(
                new CustomFilter(9, CustomFilter.NUMERIC));
            txtTotalNumberOfBills.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtTotalNumberOfBills.setColumns(30);
            txtTotalNumberOfBills.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtTotalNumberOfBills.setEnabled(false);
            txtTotalNumberOfBills.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

            lbTotalbilledAmount.setText("請求合計金額");

            ((PlainDocument)txtTotalbilledAmount.getDocument()).setDocumentFilter(
                new CustomFilter(9, CustomFilter.NUMERIC));
            txtTotalbilledAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtTotalbilledAmount.setColumns(30);
            txtTotalbilledAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtTotalbilledAmount.setEnabled(false);
            txtTotalbilledAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

            lbTotalNumberOfBillsMemo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbTotalNumberOfBillsMemo.setText("件");

            lbTotalbilledAmountMemo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbTotalbilledAmountMemo.setText("円");

            btRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg")
            );
            btRegist.setBorderPainted(false);
            btRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg")
            );
            btRegist.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btRegistActionPerformed(evt);
                }
            });

            btExcelOutput.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg")
            );
            btExcelOutput.setBorderPainted(false);
            btExcelOutput.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg")
            );
            btExcelOutput.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btExcelOutputActionPerformed(evt);
                }
            });

            btBillingDataOutput.setIcon(SystemInfo.getImageIcon("/button/print/invoice_data_output_off.jpg")
            );
            btBillingDataOutput.setBorderPainted(false);
            btBillingDataOutput.setPressedIcon(SystemInfo.getImageIcon("/button/print/invoice_data_output_on.jpg")
            );
            btBillingDataOutput.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btBillingDataOutputActionPerformed(evt);
                }
            });

            btResultData.setIcon(SystemInfo.getImageIcon("/button/select/result_data_input_off.jpg")
            );
            btResultData.setBorderPainted(false);
            btResultData.setPressedIcon(SystemInfo.getImageIcon("/button/select/result_data_input_on.jpg")
            );
            btResultData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btResultDataActionPerformed(evt);
                }
            });

            dateCollectionDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            dateCollectionDate.setDate(new java.util.Date());

            lbCollectionDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbCollectionDate.setText("回収日");

            dayLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            dayLable.setText("日");

            ((PlainDocument)txtTrasferDay.getDocument()).setDocumentFilter(
                new CustomFilter(2, CustomFilter.NUMERIC));
            txtTrasferDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtTrasferDay.setColumns(30);
            txtTrasferDay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtTrasferDay.setInputKanji(true);

            cmbDateTargetMonth.setMaximumRowCount(12);
            cmbDateTargetMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            txtCSVPath.setBorder(null);
            txtCSVPath.setColumns(30);
            txtCSVPath.setEnabled(false);
            txtCSVPath.setInputKanji(true);

            javax.swing.GroupLayout panelDetailBillingLayout = new javax.swing.GroupLayout(panelDetailBilling);
            panelDetailBilling.setLayout(panelDetailBillingLayout);
            panelDetailBillingLayout.setHorizontalGroup(
                panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelDetailBillingLayout.createSequentialGroup()
                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelDetailBillingLayout.createSequentialGroup()
                            .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelDetailBillingLayout.createSequentialGroup()
                                    .addComponent(lbTotalbilledAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTotalbilledAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbTotalbilledAmountMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelDetailBillingLayout.createSequentialGroup()
                                    .addComponent(lbProcessing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rdoProcessingBilling, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(rdoProcessingResult, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(77, 77, 77))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDetailBillingLayout.createSequentialGroup()
                                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDetailBillingLayout.createSequentialGroup()
                                            .addComponent(lbTotalNumberOfBills, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtTotalNumberOfBills, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lbTotalNumberOfBillsMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lbTargetMonth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btExcelOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btBillingDataOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                        .addGroup(panelDetailBillingLayout.createSequentialGroup()
                            .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelDetailBillingLayout.createSequentialGroup()
                                    .addComponent(lbTransferDate, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(yeaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelDetailBillingLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(cmbDateTargetMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(monthLable, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTrasferDay, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dayLable, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btInvoiceList, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(158, 158, 158)))
                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btResultData, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btFileSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelDetailBillingLayout.createSequentialGroup()
                            .addComponent(lbCollectionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateCollectionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtCSVPath, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );
            panelDetailBillingLayout.setVerticalGroup(
                panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelDetailBillingLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoProcessingBilling, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoProcessingResult, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelDetailBillingLayout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbTargetMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbDateTargetMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDetailBillingLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtCSVPath, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTransferDate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelDetailBillingLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(yeaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(monthLable, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTrasferDay, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dayLable, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btInvoiceList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btFileSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelDetailBillingLayout.createSequentialGroup()
                            .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTotalNumberOfBills, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbTotalNumberOfBills, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbTotalNumberOfBillsMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbCollectionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(5, 5, 5)
                            .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btBillingDataOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btResultData, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btExcelOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelDetailBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTotalbilledAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbTotalbilledAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbTotalbilledAmountMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(dateCollectionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            javax.swing.GroupLayout panelBillingLayout = new javax.swing.GroupLayout(panelBilling);
            panelBilling.setLayout(panelBillingLayout);
            panelBillingLayout.setHorizontalGroup(
                panelBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBillingLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(panelBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelScrollBillingResult)
                        .addComponent(panelDetailBilling, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(0, 0, 0))
            );
            panelBillingLayout.setVerticalGroup(
                panelBillingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBillingLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(panelDetailBilling, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)
                    .addComponent(panelScrollBillingResult, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGap(0, 0, 0))
            );

            jTabbedPaneRoot.addTab("　請求・結果　", panelBilling);

            panelRegisterCustomerInfo.setOpaque(false);

            btnRegistCustomer.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg")
            );
            btnRegistCustomer.setBorderPainted(false);
            btnRegistCustomer.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg")
            );
            btnRegistCustomer.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnRegistCustomerActionPerformed(evt);
                }
            });

            TransferCusDateMemo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            TransferCusDateMemo.setText("日");

            cmbTransferCusDate.setMaximumRowCount(12);
            initComboboxTransferDate();
            cmbTransferCusDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            lbTransferCusDate.setText("振替日");

            lbConsignorCode.setText("委託者コード");

            txtConsignorCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtConsignorCode.setColumns(30);

            lbConsignorCodeMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbConsignorCodeMemo.setText("＊6桁");

            lbClassificationMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbClassificationMemo.setText("＊2桁");

            lbClassification.setText("区分");

            txtDivision.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtDivision.setColumns(30);

            lbConsignorNameMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbConsignorNameMemo.setText("＊40文字（半角カナ大文字、半角英数大文字）、カナ小文字は不可");

            lbConsignorName.setText("委託者名");

            txtConsignorName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtConsignorName.setColumns(30);
            txtConsignorName.setInputKanji(true);

            cmbPayment.setMaximumRowCount(12);
            cmbPayment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            paymentLabel.setText("支払方法");

            paymentLableMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            paymentLableMemo.setText("＊結果取込時の売掛処理の消込時の支払方法");

            javax.swing.GroupLayout registerCustomerInfoLayout = new javax.swing.GroupLayout(registerCustomerInfo);
            registerCustomerInfo.setLayout(registerCustomerInfoLayout);
            registerCustomerInfoLayout.setHorizontalGroup(
                registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                            .addComponent(lbConsignorCode, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtConsignorCode, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbConsignorCodeMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnRegistCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                            .addComponent(lbClassification, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtDivision, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbClassificationMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                            .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lbTransferCusDate, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                .addComponent(paymentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                                    .addComponent(cmbTransferCusDate, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(TransferCusDateMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                                    .addComponent(cmbPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(paymentLableMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                            .addComponent(lbConsignorName, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtConsignorName, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbConsignorNameMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(170, 170, 170))
            );
            registerCustomerInfoLayout.setVerticalGroup(
                registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtConsignorCode, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbConsignorCode, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbConsignorCodeMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnRegistCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDivision, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbClassification, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbClassificationMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtConsignorName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbConsignorName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbConsignorNameMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbTransferCusDate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(registerCustomerInfoLayout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(TransferCusDateMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(cmbTransferCusDate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(registerCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(paymentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(paymentLableMemo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(488, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout panelRegisterCustomerInfoLayout = new javax.swing.GroupLayout(panelRegisterCustomerInfo);
            panelRegisterCustomerInfo.setLayout(panelRegisterCustomerInfoLayout);
            panelRegisterCustomerInfoLayout.setHorizontalGroup(
                panelRegisterCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(registerCustomerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            panelRegisterCustomerInfoLayout.setVerticalGroup(
                panelRegisterCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(registerCustomerInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            jTabbedPaneRoot.addTab("　委託者情報登録　", panelRegisterCustomerInfo);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPaneRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPaneRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPaneRoot.getAccessibleContext().setAccessibleName("　口座情報登録　");
        }// </editor-fold>//GEN-END:initComponents

                                                   
        private void btInvoiceListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInvoiceListActionPerformed
            if (this.checkInputTransferResult()) {
                this.loadTransferResult();
                if (panelBillingResult.getRowCount() <= 0) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "請求・結果"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }//GEN-LAST:event_btInvoiceListActionPerformed

    private void btFileSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFileSelectionActionPerformed
        this.openCSVFile();
    }//GEN-LAST:event_btFileSelectionActionPerformed

    private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed
        SystemInfo.getLogger().log(Level.INFO, "顧客検索");
        SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true);
        sc.setVisible(true);

        if (sc.getSelectedCustomer() != null
            && !sc.getSelectedCustomer().getCustomerID().equals(StringUtils.EMPTY)) {
            customerNo.setText(sc.getSelectedCustomer().getCustomerNo());
            this.customer = sc.getSelectedCustomer();
            customerName1.setText(sc.getSelectedCustomer().getCustomerName(0));
            customerName2.setText(sc.getSelectedCustomer().getCustomerName(1));
        }
        sc.dispose();
        sc = null;
    }//GEN-LAST:event_searchCustomerButtonActionPerformed

    private void customerNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNoFocusLost
        this.setCustomer();
    }//GEN-LAST:event_customerNoFocusLost

    private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed
        if (this.checkInputAccountInfo(false)) {
            if (this.registAccountInfo()) {
                this.refreshDataAccountInfo();
            } else {
                MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "口座情報登録"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_renewButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (this.deleteAccountInfo()) {
            this.refreshDataAccountInfo();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (this.checkInputAccountInfo(true)) {
            if (this.registAccountInfo()) {
                this.refreshDataAccountInfo();
            } else {
                MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "口座振替連携"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void btBankSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBankSearchActionPerformed
        try {
            try {
                openWebpage(new URL(URL_SEARCH).toURI());
            } catch (MalformedURLException ex) {
                Logger.getLogger(AccountTransferPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(AccountTransferPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btBankSearchActionPerformed

    private void btRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistActionPerformed
        if ( checkInputAccountResult() ) {
            boolean isExistsCheck = false;
            for (int row = 0;row < panelBillingResult.getRowCount(); row ++) {
            boolean isCheck = (Boolean)this.panelBillingResult.getValueAt(row, COL_CHECK);
            
            if ( isCheck ) {
                isExistsCheck = true;
                break;
            }
            }
            if ( !isExistsCheck ) {
                MessageDialog.showMessageDialog(this,
                                            MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "対象データ"),
                                            this.getTitle(),
                                            JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.registTransferResult()) {
                MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                                    this.getTitle(),
                                    JOptionPane.INFORMATION_MESSAGE);
                loadTransferResult();
            } else {
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "口座振替連携"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btRegistActionPerformed

    private void btExcelOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelOutputActionPerformed
        btExcelOutput.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.exportExcel();
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btExcelOutputActionPerformed

    private void btBillingDataOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBillingDataOutputActionPerformed
        btBillingDataOutput.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if ( checkInputAccountResult() ) {
                this.saveAs();
            }
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btBillingDataOutputActionPerformed

    private void btResultDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResultDataActionPerformed
        btResultData.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (this.csvPath.equals(StringUtils.EMPTY)) {
                MessageDialog.showMessageDialog(this,
                                        "ファイルが選択されていません。",
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dataTransferResult.isEmpty()) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(4001),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
            }
            if (dateCollectionDate.getDate() == null) {
                MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "回収日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                dateCollectionDate.requestFocusInWindow();
                return;
            }
            CsvImportTransferResult csvImport = new CsvImportTransferResult(this, this.csvPath);
            if (csvImport.excuteReaderAndCheckImport()) {
                csvImport.setDataTransferResult(this.dataTransferResult);
                if (this.updateTransferResult()) {
                    MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);
                    loadTransferResult();
                    btResultData.setEnabled(false);
                } else {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "口座振替連携"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btResultDataActionPerformed

    private void btnRegistCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistCustomerActionPerformed

        if (this.checkInputAccountTransferSetting()) {
            if (this.registAccountTransferSetting()) {
                MessageDialog.showMessageDialog(this,
                                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                                            this.getTitle(),
                                            JOptionPane.INFORMATION_MESSAGE);
                this.loadAccountTransferSetting();
            } else {
                MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "口座振替基本情報"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }//GEN-LAST:event_btnRegistCustomerActionPerformed

    private void rdoDepositInterimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDepositInterimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDepositInterimActionPerformed

    private void panelAccountsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAccountsMouseReleased
        this.changeCurrentData();
    }//GEN-LAST:event_panelAccountsMouseReleased

    private void panelAccountsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panelAccountsKeyReleased
        this.changeCurrentData();
    }//GEN-LAST:event_panelAccountsKeyReleased

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        this.clearAccountInfo();
    }//GEN-LAST:event_btClearActionPerformed

    private void rdoProcessingBillingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoProcessingBillingItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            this.changeComponentEnabled(Boolean.FALSE);
        }
    }//GEN-LAST:event_rdoProcessingBillingItemStateChanged

    private void rdoProcessingResultItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoProcessingResultItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            this.changeComponentEnabled(Boolean.TRUE);
        }
    }//GEN-LAST:event_rdoProcessingResultItemStateChanged
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="デザイナによる変数宣言">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup DepositTypeGroup;
    private javax.swing.ButtonGroup DivisionGroup1;
    private javax.swing.ButtonGroup DivisionGroup2;
    private javax.swing.ButtonGroup ProcessingGroup;
    private javax.swing.JLabel TransferCusDateMemo;
    private javax.swing.JButton addButton;
    private javax.swing.JButton btBankSearch;
    private javax.swing.JButton btBillingDataOutput;
    private javax.swing.JButton btClear;
    private javax.swing.JButton btExcelOutput;
    private javax.swing.JButton btFileSelection;
    private javax.swing.JButton btInvoiceList;
    private javax.swing.JButton btRegist;
    private javax.swing.JButton btResultData;
    private javax.swing.JButton btnRegistCustomer;
    private javax.swing.JComboBox cmbDateTargetMonth;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JComboBox cmbPayment;
    private javax.swing.JComboBox cmbTransferCusDate;
    private javax.swing.JComboBox cmbYear;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private javax.swing.JLabel customerNameLable;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo;
    private javax.swing.JLabel customerNoLabel;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo dateCollectionDate;
    private javax.swing.JLabel dayLable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTabbedPane jTabbedPaneRoot;
    private javax.swing.JLabel lbAccountCustomerNo;
    private javax.swing.JLabel lbAccountCustomerNoNotes;
    private javax.swing.JLabel lbAccountHolder;
    private javax.swing.JLabel lbAccountHolderMemo;
    private javax.swing.JLabel lbAccountHolderMemo1;
    private javax.swing.JLabel lbAccountNumber;
    private javax.swing.JLabel lbAccountNumberMemo;
    private javax.swing.JLabel lbBankCode;
    private javax.swing.JLabel lbBankName;
    private javax.swing.JLabel lbBranchCode;
    private javax.swing.JLabel lbBranchCodeMemo;
    private javax.swing.JLabel lbBranchName;
    private javax.swing.JLabel lbClassification;
    private javax.swing.JLabel lbClassificationMemo;
    private javax.swing.JLabel lbCollectionDate;
    private javax.swing.JLabel lbConsignorCode;
    private javax.swing.JLabel lbConsignorCodeMemo;
    private javax.swing.JLabel lbConsignorName;
    private javax.swing.JLabel lbConsignorNameMemo;
    private javax.swing.JLabel lbDepositType;
    private javax.swing.JLabel lbProcessing;
    private javax.swing.JLabel lbTargetMonth;
    private javax.swing.JLabel lbTotalNumberOfBills;
    private javax.swing.JLabel lbTotalNumberOfBillsMemo;
    private javax.swing.JLabel lbTotalbilledAmount;
    private javax.swing.JLabel lbTotalbilledAmountMemo;
    private javax.swing.JLabel lbTransferCusDate;
    private javax.swing.JLabel lbTransferDate;
    private javax.swing.JLabel monthLable;
    private com.geobeck.swing.JTableEx panelAccounts;
    private javax.swing.JPanel panelBilling;
    private com.geobeck.swing.JTableEx panelBillingResult;
    private javax.swing.JPanel panelDetailBilling;
    private javax.swing.JPanel panelFormAccount;
    private javax.swing.JPanel panelRegistAccount;
    private javax.swing.JPanel panelRegisterCustomerInfo;
    private javax.swing.JScrollPane panelScrollAccounts;
    private javax.swing.JScrollPane panelScrollBillingResult;
    private javax.swing.JLabel paymentLabel;
    private javax.swing.JLabel paymentLableMemo;
    private javax.swing.JRadioButton rdoDepositInterim;
    private javax.swing.JRadioButton rdoDepositNormal;
    private javax.swing.JRadioButton rdoProcessingBilling;
    private javax.swing.JRadioButton rdoProcessingResult;
    private javax.swing.JPanel registerCustomerInfo;
    private javax.swing.JButton renewButton;
    private javax.swing.JButton searchCustomerButton;
    private com.geobeck.swing.JFormattedTextFieldEx txtAccountCustomerNo;
    private com.geobeck.swing.JFormattedTextFieldEx txtAccountName;
    private com.geobeck.swing.JFormattedTextFieldEx txtAccountNumber;
    private com.geobeck.swing.JFormattedTextFieldEx txtBankCode;
    private com.geobeck.swing.JFormattedTextFieldEx txtBankName;
    private com.geobeck.swing.JFormattedTextFieldEx txtBranchCode;
    private com.geobeck.swing.JFormattedTextFieldEx txtBranchName;
    private com.geobeck.swing.JFormattedTextFieldEx txtCSVPath;
    private com.geobeck.swing.JFormattedTextFieldEx txtConsignorCode;
    private com.geobeck.swing.JFormattedTextFieldEx txtConsignorName;
    private com.geobeck.swing.JFormattedTextFieldEx txtDivision;
    private com.geobeck.swing.JFormattedTextFieldEx txtTotalNumberOfBills;
    private com.geobeck.swing.JFormattedTextFieldEx txtTotalbilledAmount;
    private com.geobeck.swing.JFormattedTextFieldEx txtTrasferDay;
    private javax.swing.JLabel yeaLabel;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="口座情報登録">
    /**
     * 再表示を行う。
     */
    private void refreshDataAccountInfo()
    {
            //データベースからデータを読み込む
            try
            {
                    ConnectionWrapper	con	=	SystemInfo.getConnection();

                    accountInfoList.load(con);
            }
            catch(SQLException e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            //データを表示する。
            this.showDataAccountInfo();
            //入力をクリアする
            this.clearAccountInfo();
    }
    
    /**
    * データを表示する。
    */
   private void showDataAccountInfo()
   {
           DefaultTableModel	model	=	(DefaultTableModel)panelAccounts.getModel();

           //全行削除
           model.setRowCount(0);
           panelAccounts.removeAll();
           for(DataAccountInfo info : accountInfoList)
           {

               Object[]	rowData	=	{	getUserSearchButton(info.getCustomer().getCustomerID()),
                                                   info,
                                                   info.getCustomer().getFullCustomerName(),
                                                   info.getBankCode(),
                                                   info.getBankName(),
                                                   info.getBranchCode(),
                                                   info.getBranchName(),
                                                   DataAccountInfo.AccountType.getAccountType(Integer.parseInt(info.getAccountType())).getText(),
                                                   info.getAccountNumber(),
                                                   info.getAccountName()};
               model.addRow(rowData);
           }
   }
   /**
    * 入力されたデータを登録する。
    * @return true - 成功
    */
   private boolean registAccountInfo()
   {
       boolean		result	=	false;
       DataAccountInfo		accountInfo		=	new DataAccountInfo();
       this.setData(accountInfo);

       ConnectionWrapper	con	=	SystemInfo.getConnection();

       try
       {
               con.begin();
               if(accountInfo.regist(con))
               {
                       con.commit();
                       result	=	true;
               }
               else
               {
                       con.rollback();
               }
       } catch(SQLException e)
       {
               SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
       }

       return	result;
   }
   /**
     * 選択されたデータを削除する。
     *
     * @return true - 成功
     */
    private boolean deleteAccountInfo() {
        boolean result = false;
        DataAccountInfo accountInfo = new DataAccountInfo();

        if (0 <= selIndex && selIndex < accountInfoList.size()) {
            accountInfo = accountInfoList.get(selIndex);
        }

        ConnectionWrapper con = SystemInfo.getConnection();

        try {

            con.begin();

            if (accountInfo.delete(con)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }
    /**
     * 口座情報からデータをセットする。
     *
     * @param accountInfo 口座情報
     */
    private void setData(DataAccountInfo accountInfo) {
        accountInfo.setCustomer(customer);
        accountInfo.setAccountCustomerNo(txtAccountCustomerNo.getText());
        accountInfo.setBankCode(txtBankCode.getText());
        accountInfo.setBankName(txtBankName.getText());
        accountInfo.setBranchCode(txtBranchCode.getText());
        accountInfo.setBranchName(txtBranchName.getText());
        accountInfo.setAccountType(rdoDepositNormal.isSelected() ? String.valueOf(DataAccountInfo.AccountType.NORMAL.getValue()): String.valueOf(DataAccountInfo.AccountType.POPULAR.getValue()));
        accountInfo.setAccountNumber(txtAccountNumber.getText());
        accountInfo.setAccountName(txtAccountName.getText());
    }
   /**
    * 入力項目をクリアする。
    */
    private void clearAccountInfo()
    {
            selIndex = -1;
            customerName1.setText(StringUtils.EMPTY);
            customerName2.setText(StringUtils.EMPTY);
            customerNo.setText(StringUtils.EMPTY);
            txtAccountCustomerNo.setText(StringUtils.EMPTY);
            txtBankCode.setText(StringUtils.EMPTY);
            txtBankName.setText(StringUtils.EMPTY);
            txtBranchCode.setText(StringUtils.EMPTY);
            txtBranchName.setText(StringUtils.EMPTY);
            rdoDepositNormal.setSelected(Boolean.TRUE);
            txtAccountNumber.setText(StringUtils.EMPTY);
            txtAccountName.setText(StringUtils.EMPTY);
            //ボタンのEnabledを変更する。
            this.changeButtonEnabled(false);

           if(0 < panelAccounts.getRowCount())
                           panelAccounts.removeRowSelectionInterval(0, panelAccounts.getRowCount() - 1);

    }
    /**
    * 入力チェックを行う。
    * @return 入力エラーがなければtrueを返す。
    */
   private boolean checkInputAccountInfo(boolean isAdd)
   {
        try {
            //顧客No.
            if(this.customer == null || this.customer.getCustomerID() == null)
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "顧客No."),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                customerNo.requestFocusInWindow();
                return	false;
            }
            if (isAdd) {
                //既に同一の顧客IDの情報が存在している場合は以下アラートを表示
                DataAccountInfo accountInfo = new DataAccountInfo();
                accountInfo.setCustomer(customer);
                if (accountInfo.isExists(SystemInfo.getConnection())) {
                    MessageDialog.showMessageDialog(this,
                            "既に口座情報が登録されています。",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    customerNo.requestFocusInWindow();
                    return	false;
                }
            }
            //口座振替用顧客NO
            if(txtAccountCustomerNo.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "口座振替用顧客NO"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountCustomerNo.requestFocusInWindow();
                return	false;
            }
            if(!CheckUtil.isNumber(txtAccountCustomerNo.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座振替用顧客NO"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountCustomerNo.requestFocusInWindow();
                return	false;
            }
            if(!CheckUtil.checkStringLength(txtAccountCustomerNo.getText(), 14))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座振替用顧客NO"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountCustomerNo.requestFocusInWindow();
                return	false;
            }
            //銀行コード
            if(txtBankCode.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "銀行コード"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankCode.requestFocusInWindow();
                return	false;
            }
            //半角数字4桁。4桁に満たない場合は「追加」「更新」時に以下アラートを表示
            if(!(txtBankCode.getText().trim().length() == MAX_LEN_BANK_CODE))
            {
                MessageDialog.showMessageDialog(this,
                        "銀行コードは4桁で入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankCode.requestFocusInWindow();
                return	false;
            }
            //銀行名
            if(txtBankName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "銀行名"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankName.requestFocusInWindow();
                return	false;
            }
            //支店コード
            if(txtBranchCode.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支店コード"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchCode.requestFocusInWindow();
                return	false;
            }
            //支店コード
            if(!(txtBranchCode.getText().trim().length() == MAX_LEN_BRANCH_CODE))
            {
                MessageDialog.showMessageDialog(this,
                        "支店コードは3桁で入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchCode.requestFocusInWindow();
                return	false;
            }
            //支店名
            if(txtBranchName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支店名"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchName.requestFocusInWindow();
                return	false;
            }
            //口座番号
            if(txtAccountNumber.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "口座番号"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountNumber.requestFocusInWindow();
                return	false;
            }
            if(!(txtAccountNumber.getText().trim().length() == MAX_LEN_ACCOUNT_NUMBER))
            {
                MessageDialog.showMessageDialog(this,
                        "口座番号は7桁で入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountNumber.requestFocusInWindow();
                return	false;
            }
            //口座名義
            if(txtAccountName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "口座名義"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountName.requestFocusInWindow();
                return	false;
            }
            //銀行コード
            if(!CheckUtil.isNumber(txtBankCode.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "銀行コード"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankCode.requestFocusInWindow();
                return	false;
            }
            //支店コード
            if(!CheckUtil.isNumber(txtBranchCode.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "支店コード"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchCode.requestFocusInWindow();
                return	false;
            }
            //口座番号
            if(!CheckUtil.isNumber(txtAccountNumber.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座番号"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountNumber.requestFocusInWindow();
                return	false;
            }
            
            return	true;
        } catch (SQLException ex) {
            Logger.getLogger(AccountTransferPanel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
   
       /**
     * 選択データが変更されたときの処理を行う。
     */
    private void changeCurrentData() {
        int index = panelAccounts.getSelectedRow();

        if (0 <= index && index < accountInfoList.size() && index != selIndex) {
            selIndex = index;
            //選択されているデータを表示
            this.showCurrentData();
        }

        this.changeButtonEnabled(0 <= selIndex);
    }

    /**
     * 選択されたデータを入力項目に表示する。
     */
    private void showCurrentData() {
        DataAccountInfo info = accountInfoList.get(selIndex);
        
        this.customer = info.getCustomer();
        customerNo.setText(this.customer.getCustomerNo());
        customerName1.setText(this.customer.getCustomerName(0));
        customerName2.setText(this.customer.getCustomerName(1));
        txtAccountCustomerNo.setText(info.getAccountCustomerNo());
        txtBankCode.setText(info.getBankCode());
        txtBankName.setText(info.getBankName());
        txtBranchCode.setText(info.getBranchCode());
        txtBranchName.setText(info.getBranchName());
        txtAccountNumber.setText(info.getAccountNumber());
        txtAccountName.setText(info.getAccountName());
    }

    /**
     * ボタンのEnabledを変更する。
     *
     * @param enabled Enabled
     */
    private void changeButtonEnabled(boolean enabled) {
        searchCustomerButton.setEnabled(!enabled);
        customerNo.setEnabled(!enabled);
        addButton.setEnabled(!enabled);
        renewButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }
    /**
     * https://zengin.ajtw.net/をブラウザで開く
     * @param uri
     * @return 
     */
    private static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (IOException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        return false;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="請求・結果">
    /**
     * init 請求・結果
     */
    private void initBillingAndResult() {
        changeComponentEnabled(Boolean.FALSE);
        //対象月
        loadTargetMonth();
        // 口座振替日
        loadTransferDate();
        //請求・結果情報ヘッダ
        //loadTransferResult();
    }
    /**
    * 入力チェックを行う。
    * @return 入力エラーがなければtrueを返す。
    */
    private boolean checkInputAccountResult()
    {

        //口座振替日
        if (cmbYear.getSelectedIndex() <= 0) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmbYear.requestFocusInWindow();
            return false;
        }
        //口座振替日
        if (cmbMonth.getSelectedIndex() <= 0) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmbMonth.requestFocusInWindow();
            return false;
        }
        //口座振替日
        if(txtTrasferDay.getText().equals(StringUtils.EMPTY))
        {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtTrasferDay.requestFocusInWindow();
            return	false;
        }
        //口座振替日
        if(!CheckUtil.isNumber(txtTrasferDay.getText()))
        {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtTrasferDay.requestFocusInWindow();
            return	false;
        }
        //口座振替日
        try {
            if (!isDate(Integer.parseInt(cmbYear.getSelectedItem().toString()), Integer.parseInt(cmbMonth.getSelectedItem().toString()), Integer.parseInt(txtTrasferDay.getText()))) {
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                txtTrasferDay.requestFocusInWindow();
                return	false;
            }
        }catch(Exception e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtTrasferDay.requestFocusInWindow();
            return	false;
        }

        return	true;
    }
    /**
     * 請求・結果情報ヘッダ
     */
    private void loadTransferResult() {
        try
            {
                    ConnectionWrapper	con	=	SystemInfo.getConnection();

                    dataTransferResult = new DataTransferResult();
                    dataTransferResult.setTargetMonth(getTargetMonth());
                    dataTransferResult.load(con, rdoProcessingBilling.isSelected()? DataTransferResult.EXPORT_DATA_REQUEST : DataTransferResult.IMPORT_DATA_RESULT);
            }
            catch(SQLException e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        this.showDataTransferResult();
    }
    /**
    * データを表示する。
    */
   private void showDataTransferResult()
   {
           DefaultTableModel	model	=	(DefaultTableModel)panelBillingResult.getModel();

           //全行削除
           model.setRowCount(0);
           panelBillingResult.removeAll();
           this.uncheckHeader();
           long totalbilledAmount = 0l;
           for(DataTransferResultDetail detail : dataTransferResult)
           {

               Object[]	rowData	=	{          detail.getDataAccountInfo().getCustomer().getCustomerNo(),
                                                   detail.getDataAccountInfo().getCustomer().getFullCustomerName(),
                                                   detail.getDataAccountInfo().getBankCode(),
                                                   detail.getDataAccountInfo().getBankName(),
                                                   detail.getDataAccountInfo().getBranchCode(),
                                                   detail.getDataAccountInfo().getBranchName(),
                                                   DataAccountInfo.AccountType.getAccountType(Integer.parseInt(detail.getDataAccountInfo().getAccountType())).getText(),
                                                   detail.getDataAccountInfo().getAccountNumber(),
                                                   detail.getDataAccountInfo().getAccountName(),
                                                   detail.getBillingAmount(),
                                                   detail,
                                                    false};
               model.addRow(rowData);
               totalbilledAmount += detail.getBillingAmount();
           }
           txtTotalNumberOfBills.setText(String.valueOf(dataTransferResult.size()));
           txtTotalbilledAmount.setText(String.valueOf(totalbilledAmount));
   }
    /**
    * 入力されたデータを登録する。
    * @return true - 成功
    */
   private boolean registTransferResult()
   {
       boolean		result	=	false;

       ConnectionWrapper	con	=	SystemInfo.getConnection();

       DataTransferResult transferResult = new DataTransferResult(dataTransferResult);
       setDataTransferResult(transferResult);
       try
       {
               con.begin();
               if(transferResult.registAll(con))
               {
                       con.commit();
                       result	=	true;
               }
               else
               {
                       con.rollback();
               }
       } catch(SQLException e)
       {
               SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
       }

       return	result;
   }
   /**
    * 入力されたデータを登録する。
    * @return true - 成功
    */
   private boolean updateTransferResult()
   {
       ConnectionWrapper	con	=	SystemInfo.getConnection();

       try
       {
               con.begin();
               DataPayment dp;
               DataPaymentDetail dpd;
               if(!dataTransferResult.registAll(con))
               {
                    con.rollback();
                    return false;
               }
               for(DataTransferResultDetail detail : dataTransferResult)
               {
                   if (detail.getResultCode() != null && detail.getResultCode().equals(DataTransferResultDetail.TRANSFERRED)) {
                        detail.loadPayments(con);
                        if( !detail.getPayments().isEmpty() ) {
                            dp = detail.getPayments().get(detail.getPayments().size() -1);
                            dpd = dp.get(0);
                            if (!dpd.isExists(con)) {
                                 dp.setPaymentNo(detail.getNewPaymentNo(con));
                                 dp.setPaymentDate(dateCollectionDate.getDate());

                                 MstPaymentMethod paymentMethod = new MstPaymentMethod();
                                 paymentMethod.setPaymentMethodID(mstAccountTransferSetting.getPaymentMethod());
                                 paymentMethod.load(con);
                                 dpd.setPaymentMethod(paymentMethod);
                                 dpd.setPaymentValue(dp.getBillValue());
                                 dp.setBillValue(0l);

                                 if (!dp.registAll(con)) {
                                     con.rollback();
                                     return false;
                                 }
                            }
                        }
                   }
               }
                //トランザクションコミット
                con.commit();
                con.close();
       } catch(SQLException e)
       {
               SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
       }

       return true;
   }
    /**
     * 請求・結果情報ヘッダからデータをセットする。
     *
     * @param trasfer 請求・結果情報
     */
    private void setDataTransferResult(DataTransferResult trasfer) {
        trasfer.setTransferYear(cmbYear.getSelectedItem().toString());
        trasfer.setTransferMonth(cmbMonth.getSelectedItem().toString());
        trasfer.setTransferDate(txtTrasferDay.getText());
        
        int totalNumber =   0;
        int totalAmount =   0;
        for (int row = 0;row < panelBillingResult.getRowCount(); row ++) {
            boolean isCheck = (Boolean)this.panelBillingResult.getValueAt(row, COL_CHECK);
            
            if ( isCheck ) {
                
                DataTransferResultDetail detail = (DataTransferResultDetail)this.panelBillingResult.getValueAt(row, (COL_CHECK - 1));
                trasfer.add(detail);
                
                totalNumber ++;
                totalAmount += detail.getBillingAmount();
            }
        }
        trasfer.setTotalNum(totalNumber);
        trasfer.setTotalAmount(totalAmount);
    }
    /**
     * 対象月
     */
    private void loadTargetMonth() {
        try
            {
                    ConnectionWrapper	con	=	SystemInfo.getConnection();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -6);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    
                    dataMonthlyBatchLogs.setTargetMonth(cal.getTime());
                    dataMonthlyBatchLogs.load(con);
            }
            catch(SQLException e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        this.cmbDateTargetMonth.removeAllItems();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        this.cmbDateTargetMonth.addItem(StringUtils.EMPTY);
        for (int i = 0; i < this.dataMonthlyBatchLogs.size(); i++) {
            this.cmbDateTargetMonth.addItem(df.format(this.dataMonthlyBatchLogs.get(i).getTargetMonth()));
        }
    }
    /**
     * ・請求データ出力の場合
     *  年、月は当月を初期表示、日は基本情報登録で設定した振替日を表示
     *・振替結果取込の場合
     *  対象月に基づいて登録された年月日を表示
     */
    private void loadTransferDate() {
        this.cmbYear.removeAllItems();
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        //年
        this.cmbYear.addItem(StringUtils.EMPTY);
        for (int i = 0; i < 5; i++) {
            this.cmbYear.addItem(String.valueOf(nowYear - i));
        }
        this.cmbYear.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
        //月
        this.cmbMonth.addItem(StringUtils.EMPTY);
        for (int mon = 1; mon <= 12; mon++) {
            this.cmbMonth.addItem(String.valueOf(mon));
        }
        this.cmbMonth.setSelectedItem(String.valueOf((cal.get(Calendar.MONTH)+1)));
        //日
        try
            {
                    ConnectionWrapper	con	=	SystemInfo.getConnection();

                    mstAccountTransferSetting.load(con);
            }
            catch(SQLException e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        if (mstAccountTransferSetting.getTransferDate() == null ) {
            this.txtTrasferDay.setText(StringUtils.EMPTY);
        } else {
            this.txtTrasferDay.setText(mstAccountTransferSetting.getTransferDate());
        }
    }
    
    /**
     * uncheck checkbox header
     */
    private void uncheckHeader() {
        if(panelBillingResult.getColumnModel().getColumn(this.COL_CHECK).getHeaderRenderer() instanceof BevelBorderHeaderRenderer) {
            ((BevelBorderHeaderRenderer)panelBillingResult.getColumnModel().getColumn(this.COL_CHECK).getHeaderRenderer()).check.setSelected(Boolean.FALSE);
        }
    }

    /**
     * 成分のEnabledを変更する。
     *
     * @param enabled Enabled
     */
    private void changeComponentEnabled(boolean isImport) {
        cmbYear.setEnabled(!isImport);
        cmbMonth.setEnabled(!isImport);
        txtTrasferDay.setEnabled(!isImport);
        btRegist.setEnabled(!isImport);
        btBillingDataOutput.setEnabled(!isImport);
        btFileSelection.setEnabled(isImport);
        dateCollectionDate.setEnabled(isImport);
        btResultData.setEnabled(isImport);
        dataTransferResult.clear();
        if (isImport) {
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setHeaderRenderer(SystemInfo.getTableHeaderRenderer());
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setPreferredWidth(0);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setMaxWidth(0);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setMinWidth(0);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setWidth(0);
            panelBillingResult.getColumnModel().getColumn(10).setPreferredWidth(130);
            
            if (dateCollectionDate.getDate() == null ) {
                dateCollectionDate.setDate(new java.util.Date());
            }
        } else {
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setHeaderRenderer(this.getTableHeaderRenderer(panelBillingResult.getTableHeader()));
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setPreferredWidth(50);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setMaxWidth(50);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setMinWidth(50);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setWidth(50);
            
            panelBillingResult.getColumnModel().getColumn(COL_CHECK-1).setPreferredWidth(90);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK-1).setWidth(90);
            
            dateCollectionDate.setDate((Date) null);
        }
        if (isImport) {
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setHeaderRenderer(SystemInfo.getTableHeaderRenderer());
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setPreferredWidth(0);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setMaxWidth(0);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setMinWidth(0);
            panelBillingResult.getColumnModel().getColumn(COL_CHECK).setWidth(0);
            panelBillingResult.getColumnModel().getColumn(10).setPreferredWidth(130);
        }
        
        DefaultTableModel	model	=	(DefaultTableModel)panelBillingResult.getModel();
        model.setRowCount(0);
        panelBillingResult.removeAll();
    }
    private String getFileName() {
        StringBuilder fileName = new StringBuilder(StringUtils.EMPTY);
        if (mstAccountTransferSetting.getConsignorCode() != null ) {
            fileName.append("S");
            fileName.append(mstAccountTransferSetting.getConsignorCode().substring(0, 5));
        }
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
            fileName.append(df.format(new Date()));
        
        return fileName.toString();
    }
    private void saveAs() {
    if (dataTransferResult.isEmpty()) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
    }
    if (mstAccountTransferSetting == null || mstAccountTransferSetting.getConsignorCode() == null ) {
        MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "委託者コード"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
        return;
    }
    boolean isExistsCheck = false;
    if (rdoProcessingBilling.isSelected()) {
        for (int row = 0;row < panelBillingResult.getRowCount(); row ++) {
            boolean isCheck = (Boolean)this.panelBillingResult.getValueAt(row, COL_CHECK);

            if ( isCheck ) {
                isExistsCheck = true;
                break;
            }
        }
        if ( !isExistsCheck  ) {
            MessageDialog.showMessageDialog(this,
                                        MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "対象データ"),
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
      FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
      final JFileChooser saveAsFileChooser = new JFileChooser();
      saveAsFileChooser.setApproveButtonText("Save");
      saveAsFileChooser.setFileFilter(extensionFilter);
      saveAsFileChooser.setSelectedFile(new File(getFileName()));
      int actionDialog = saveAsFileChooser.showOpenDialog(this);
      if (actionDialog != JFileChooser.APPROVE_OPTION) {
         return;
      }

      File file = saveAsFileChooser.getSelectedFile();
      if (!file.getName().endsWith(".txt")) {
         file = new File(file.getAbsolutePath() + ".txt");
      }

      BufferedWriter bw = null;
      StringBuilder lineData = new StringBuilder(StringUtils.EMPTY);
      try {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("SJIS")));
        //ヘッダーレコード
        lineData.append(String.valueOf(HEADER_RECORD));//データ区分
        lineData.append("91");          //種別コード
        lineData.append("1");           //コード区分
        lineData.append(mstAccountTransferSetting.getConsignorCode()).append(mstAccountTransferSetting.getDivision()).append("00");//委託者コード
        //委託者名
        lineData.append(padLeftSpaces(mstAccountTransferSetting.getConsignorName(), 40)); //半角カナ大文字および半角英数大文字で左詰め　※カナ小文字は不可。下記参照。
        //振替日
        int trasferDay = Integer.parseInt(txtTrasferDay.getText()) ;
        lineData.append(Integer.parseInt(cmbMonth.getSelectedItem().toString()) > 9 ? cmbMonth.getSelectedItem().toString() : "0" + cmbMonth.getSelectedItem().toString()).append(trasferDay > 9 ? trasferDay : "0" + trasferDay);
        //ダミー
        lineData.append(padLeftSpaces(StringUtils.EMPTY, 62));
        bw.write(lineData.toString());
        bw.newLine();
        //データレコード
        lineData.setLength(0);
        int totalNumber     = 0;
        int totalAmount     = 0;
        for (int row = 0;row < panelBillingResult.getRowCount(); row ++) {
            boolean isCheck = (Boolean)this.panelBillingResult.getValueAt(row, COL_CHECK);
            if (!isCheck) {
                continue;
            }
            DataTransferResultDetail detail = (DataTransferResultDetail) panelBillingResult.getValueAt(row, COL_CHECK - 1);
            //データ区分
            lineData.append(String.valueOf(DATA_RECORD));
            //振替銀行番号
            lineData.append(detail.getBankCode());
            //振替銀行名
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 15));
            //振替銀行支店番号
            lineData.append(detail.getBranchCode());
            //振替銀行支店名
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 15));
            //ダミー
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 4));
            //預金種別
            lineData.append(detail.getDataAccountInfo().getAccountType());
            //口座番号
            lineData.append(detail.getDataAccountInfo().getAccountNumber());
            //預金者名
            lineData.append(padLeftSpaces(detail.getDataAccountInfo().getAccountName(), 30));
            //振替金額
            lineData.append(padRightZeros(detail.getBillingAmount().toString(), 10));
            //新規コード
            lineData.append("1");
            //顧客番号
            lineData.append("0");
            lineData.append(mstAccountTransferSetting.getConsignorCode().substring(0, 5));
            //口座振替用顧客NO
            lineData.append(detail.getAccountCustomerNo());
            //振替結果コード
            lineData.append("0");
            //ダミー
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 8));
            
            bw.write(lineData.toString());
            bw.newLine();
            lineData.setLength(0);
            
            totalNumber ++;
            totalAmount += detail.getBillingAmount();
        }
        
        //トレーラーレコード
        lineData.setLength(0);
        //データ区分
        lineData.append(String.valueOf(TRAILER_RECORD));
        //請求合計件数
        lineData.append(padRightZeros(String.valueOf(totalNumber), 6));
        //請求合計金額
        lineData.append(padRightZeros(String.valueOf(totalAmount), 12));
        //振替済合計件数
        lineData.append(padRightZeros(StringUtils.EMPTY, 6));
        //振替済合計金額
        lineData.append(padRightZeros(StringUtils.EMPTY, 12));
        //振替不能合計件数
        lineData.append(padRightZeros(StringUtils.EMPTY, 6));
        //振替不能合計金額
        lineData.append(padRightZeros(StringUtils.EMPTY, 12));
        //ダミー
        lineData.append(padLeftSpaces(StringUtils.EMPTY, 65));
        bw.write(lineData.toString());
        bw.newLine();
        
        //エンドレコード
        lineData.setLength(0);
        //データ区分
        lineData.append(String.valueOf(END_RECORD));
        //ダミー
        lineData.append(padLeftSpaces(StringUtils.EMPTY, 119));
        bw.write(lineData.toString());
        //請求データ出力のファイルの最終行について改行を入れてほしいです
        bw.newLine();

      } catch (IOException e) {
         SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
      } finally {
         if (bw != null) {
            try {
               bw.close();
            } catch (IOException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
         }
      }
   }
    
    /**
     * 左スペースを埋める
     * @param inputString
     * @param length
     * @return 
     */
    public String padLeftSpaces(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(inputString);
        while (sb.length() < length) {
            sb.append(' ');
        }

        return sb.toString();
    }
    
    /**
     * 右ゼロを埋める
     * @param inputString
     * @param length
     * @return 
     */
    public String padRightZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
    
    	/**
	 * CSVファイルを開く
	 */
	private void openCSVFile()
	{
		JFileChooser	jfc	=	new JFileChooser();
		WildcardFileFilter filter = new WildcardFileFilter("*.csv", "CSVファイル");
		jfc.setFileFilter(filter);

		if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			
			File	f	=	new File(jfc.getSelectedFile().getAbsolutePath());
			
			//ファイルが存在する場合
			if(f.exists())
			{
				 this.csvPath = jfc.getSelectedFile().getAbsolutePath();
                                 txtCSVPath.setText(this.csvPath);
			} else {
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(1200,
						f.getAbsolutePath()),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
        
    /**
     * EXCEL出力
     */
    private void exportExcel() {
        
        if ( rdoProcessingBilling.isSelected() ) {
            boolean isDate = true;
            try {
                if (!isDate(Integer.parseInt(cmbYear.getSelectedItem().toString()), Integer.parseInt(cmbMonth.getSelectedItem().toString()), Integer.parseInt(txtTrasferDay.getText()))) {
                    isDate = false;
                }
            }catch(Exception e) {
                isDate = false;
            }
            if ( !isDate ) {
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "口座振替日"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                txtTrasferDay.requestFocusInWindow();
                return;
            }
        }
        if (panelBillingResult.getRowCount() == 0) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mstAccountTransferSetting == null || mstAccountTransferSetting.getTransferDate() == null ) {
            MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "振替日"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JExcelApi jx = new JExcelApi("請求データ出力");
        jx.setTemplateFile("/reports/請求データ出力.xls");

        // ヘッダ出力
        //対象月
        jx.setValue(2, 3, (dataTransferResult.getTargetMonth() != null) ? dataTransferResult.getTargetMonth() : StringUtils.EMPTY);
        //口座振替日
        if ( rdoProcessingBilling.isSelected() ) {
            int month = Integer.parseInt(cmbMonth.getSelectedItem().toString()) - 1;
            Calendar calendar = new GregorianCalendar(Integer.parseInt(cmbYear.getSelectedItem().toString()), month ,Integer.parseInt(txtTrasferDay.getText()));
            jx.setValue(2, 4, calendar.getTime());
        } else {
            if (isDate(Integer.parseInt(dataTransferResult.getTransferYear()), Integer.parseInt(dataTransferResult.getTransferMonth()), Integer.parseInt(dataTransferResult.getTransferDate()))) {
                int month = Integer.parseInt(dataTransferResult.getTransferMonth()) - 1;
                Calendar calendar = new GregorianCalendar(Integer.parseInt(dataTransferResult.getTransferYear()), month , Integer.parseInt(dataTransferResult.getTransferDate()));
                jx.setValue(2, 4, calendar.getTime());
            } else {
                jx.setValue(2, 4, StringUtils.EMPTY);
            }
        }
        //請求合計件数
        jx.setValue(2, 5, txtTotalNumberOfBills.getText() != null ? Long.valueOf(txtTotalNumberOfBills.getText()) : StringUtils.EMPTY);
        //請求合計金額
        jx.setValue(2, 6, txtTotalbilledAmount.getText() != null ? Long.valueOf(txtTotalbilledAmount.getText()) : StringUtils.EMPTY);

        int outRow = 9;

        // 追加行数セット
        jx.insertRow(outRow, panelBillingResult.getRowCount() - 1);

        // データセット
        DefaultTableModel model = (DefaultTableModel) panelBillingResult.getModel();
        for (int row = 0; row < panelBillingResult.getRowCount(); row ++) {
            for (int col = 0; col < panelBillingResult.getColumnCount()-1; col ++) {
                //請求金額
                if (col == 9) {
                    if (CheckUtil.isNumeric(model.getValueAt(row, col).toString())) {
                        Long value = Long.valueOf(model.getValueAt(row, col).toString());
                        jx.setValue(col + 1, outRow, value);
                    } else {
                        jx.setValue(col + 1, outRow, model.getValueAt(row, col).toString());
                    }
                } else {
                    jx.setValue(col + 1, outRow, model.getValueAt(row, col).toString());
                }
            }
            outRow++;
        }

        jx.openWorkbook();
    }
    
    /**
     * 対象月
     * @return 
     */
    private java.util.Date getTargetMonth() {
        try {
            if (this.cmbDateTargetMonth.getSelectedItem().toString().equals(StringUtils.EMPTY)) {
                return null;
            }
            java.util.Date date = new SimpleDateFormat("yyyy/MM/dd").parse(this.cmbDateTargetMonth.getSelectedItem().toString());
            
            return date;
        } catch (ParseException ex) {
            Logger.getLogger(AccountTransferPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean checkInputTransferResult() {
       try {
            //対象月
            if (cmbDateTargetMonth.getSelectedIndex() <= 0) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "対象月"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                cmbDateTargetMonth.requestFocusInWindow();
                return false;
            }
            
            return	true;
        } catch (Exception ex) {
            Logger.getLogger(AccountTransferPanel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
   }
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="基本情報登録">
        /**
         * リスト内容は「14」「27」でいずれかを選択する
         */
        private void initComboboxTransferDate() {
            cmbTransferCusDate.removeAllItems();
            cmbTransferCusDate.addItem("14");
            cmbTransferCusDate.addItem("27");
        }
        /**
         * 基本情報ロード
         */
        private void loadAccountTransferSetting() {
            try
            {
                    ConnectionWrapper	con	=	SystemInfo.getConnection();

                    mstAccountTransferSetting.load(con);
                    showAccountTransferSetting();
            }
            catch(SQLException e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        /**
         * データの基本情報を表示
         */
        private void showAccountTransferSetting() {
            
            if (mstAccountTransferSetting != null && mstAccountTransferSetting.getConsignorCode() != null) {
                txtConsignorCode.setEditable(false);
                txtConsignorCode.setText(mstAccountTransferSetting.getConsignorCode());
                txtDivision.setText(mstAccountTransferSetting.getDivision());
                txtConsignorName.setText(mstAccountTransferSetting.getConsignorName());
                for ( int i = 0; i < cmbTransferCusDate.getItemCount();i ++) {
                    Object item = cmbTransferCusDate.getItemAt(i);
                    if (item.toString().equals(mstAccountTransferSetting.getTransferDate())) {
                        cmbTransferCusDate.setSelectedItem(mstAccountTransferSetting.getTransferDate());
                    }
                }
                //口座振替日
                this.txtTrasferDay.setText(mstAccountTransferSetting.getTransferDate());
            }
        }
        
    /**
    * 入力されたデータを登録する。
    * @return true - 成功
    */
   private boolean registAccountTransferSetting()
   {
       boolean		result	=	false;
       MstAccountTransferSetting	accountTransferSetting		=	new MstAccountTransferSetting();
       this.setDataAccountTransferSetting(accountTransferSetting);

       ConnectionWrapper	con	=	SystemInfo.getConnection();

       try
       {
               con.begin();
               if(accountTransferSetting.regist(con))
               {
                       con.commit();
                       result	=	true;
               }
               else
               {
                       con.rollback();
               }
       } catch(SQLException e)
       {
               SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
       }

       return	result;
   }
   
   private boolean checkInputAccountTransferSetting() {
       try {
           //委託者コード
           if(txtConsignorCode.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "委託者コード"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtConsignorCode.requestFocusInWindow();
                return	false;
            }
            //委託者コード
            if(!(txtConsignorCode.getText().trim().length() == MAX_LEN_CONSIGNOR_NUMBER))
            {
                MessageDialog.showMessageDialog(this,
                        "委託者コードは6桁で入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtConsignorCode.requestFocusInWindow();
                return	false;
            }
            //区分
           if(txtDivision.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "区分"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtDivision.requestFocusInWindow();
                return	false;
            }
           if(!(txtDivision.getText().length() == MAX_LEN_DIVISION_NUMBER))
            {
                MessageDialog.showMessageDialog(this,
                        "区分は2桁で入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtDivision.requestFocusInWindow();
                return	false;
            }
           //委託者コード
           if(txtConsignorName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "委託者名"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtConsignorName.requestFocusInWindow();
                return	false;
            }
            //支払方法
            if (cmbPayment.getSelectedIndex() <= 0) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "支払方法"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                cmbPayment.requestFocusInWindow();
                return false;
            }
            
            return	true;
        } catch (Exception ex) {
            Logger.getLogger(AccountTransferPanel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
   }
   
   private void setDataAccountTransferSetting (MstAccountTransferSetting accountTransferSetting) {
       accountTransferSetting.setConsignorCode(txtConsignorCode.getText());
       accountTransferSetting.setConsignorName(txtConsignorName.getText());
       accountTransferSetting.setDivision(txtDivision.getText());
       accountTransferSetting.setTransferDate(cmbTransferCusDate.getSelectedItem().toString());
       accountTransferSetting.setPaymentMethod(((MstPaymentMethod)cmbPayment.getSelectedItem()).getPaymentMethodID());
   }
   
   private void initPaymentMethod() {
            try {
            ConnectionWrapper con = SystemInfo.getConnection();

            MstPaymentClasses mp = new MstPaymentClasses();
            mp.loadClasses(con);
            
            this.prefixPaymentMethodName(mp);

            con.close();

            this.cmbPayment.addItem(new MstPaymentMethod());
            for (MstPaymentClass paymentClass : mp) {
                for (MstPaymentMethod paymentMethod : paymentClass) {
                    this.cmbPayment.addItem(paymentMethod);
                }
            }
            
            for ( int i = 0;i < this.cmbPayment.getItemCount(); i ++) {
                MstPaymentMethod method = (MstPaymentMethod)this.cmbPayment.getItemAt(i);
                if ( method != null && method.getPaymentMethodID() != null && this.mstAccountTransferSetting.getPaymentMethod() != null) {
                    if (this.mstAccountTransferSetting.getPaymentMethod().equals(method.getPaymentMethodID())) {
                        this.cmbPayment.setSelectedItem(method);
                    }
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
   }
    /**
     * プレフィックス支払い方法クラスを設定する
     */
    private void prefixPaymentMethodName(MstPaymentClasses mp) {
        for(MstPaymentClass methodClass : mp) {
            if(methodClass.size() > 1) {
                for (MstPaymentMethod method : methodClass) {
                    method.setPaymentMethodName(methodClass.getPaymentClassName() + "）" + method.getPaymentMethodName());
                }
            }
        }
    }
    // </editor-fold>
    
    /**
     * 顧客をセットする。
     */
    private void setCustomer() {

        MstCustomer cus = new MstCustomer();
        boolean isChangedCustomerNo = !customerNo.getText().equals(this.customer.getCustomerNo());
        
        cus.setCustomerNo(customerNo.getText());

        try {
            if(isChangedCustomerNo) {
                cus = SelectSameNoData.getMstCustomerByNo(
                        parentFrame,
                        SystemInfo.getConnection(),
                        this.customerNo.getText(),
                        (SystemInfo.getSetteing().isShareCustomer() ? 0 : SystemInfo.getCurrentShop().getShopID()));
            }else {
                    cus = new MstCustomer(this.customer.getCustomerID());
                    cus.load(SystemInfo.getConnection());
                }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        if(!customerNo.getText().equals(StringUtils.EMPTY)){
            this.customer = cus;
            customerName1.setText(cus.getCustomerName(0));
            customerName2.setText(cus.getCustomerName(1));
        }else {
            this.customer = new MstCustomer();
            customerName1.setText(StringUtils.EMPTY);
            customerName2.setText(StringUtils.EMPTY);
        }

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
                    }
            });
            return searchButton;
    }

    	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableAccountsInfoColumnWidth()
	{
		//列の幅を設定する。
		panelAccounts.getColumnModel().getColumn(0).setPreferredWidth(50);
		panelAccounts.getColumnModel().getColumn(1).setPreferredWidth(55);
		panelAccounts.getColumnModel().getColumn(2).setPreferredWidth(70);
		panelAccounts.getColumnModel().getColumn(3).setPreferredWidth(55);
		panelAccounts.getColumnModel().getColumn(4).setPreferredWidth(70);
                panelAccounts.getColumnModel().getColumn(5).setPreferredWidth(55);
                panelAccounts.getColumnModel().getColumn(6).setPreferredWidth(70);
                panelAccounts.getColumnModel().getColumn(7).setPreferredWidth(50);
                panelAccounts.getColumnModel().getColumn(8).setPreferredWidth(55);
                panelAccounts.getColumnModel().getColumn(9).setPreferredWidth(70);
	}
        
        /**
	 * JTableの列幅を初期化する。
	 */
	private void initTableBillingResultColumnWidth()
	{
		//列の幅を設定する。
		panelBillingResult.getColumnModel().getColumn(0).setPreferredWidth(60);
		panelBillingResult.getColumnModel().getColumn(1).setPreferredWidth(80);
		panelBillingResult.getColumnModel().getColumn(2).setPreferredWidth(60);
		panelBillingResult.getColumnModel().getColumn(3).setPreferredWidth(80);
		panelBillingResult.getColumnModel().getColumn(4).setPreferredWidth(60);
                panelBillingResult.getColumnModel().getColumn(5).setPreferredWidth(80);
                panelBillingResult.getColumnModel().getColumn(6).setPreferredWidth(55);
                panelBillingResult.getColumnModel().getColumn(7).setPreferredWidth(60);
                panelBillingResult.getColumnModel().getColumn(8).setPreferredWidth(80);
                panelBillingResult.getColumnModel().getColumn(9).setPreferredWidth(55);
                panelBillingResult.getColumnModel().getColumn(10).setPreferredWidth(90);
                panelBillingResult.getColumnModel().getColumn(11).setPreferredWidth(30);
	}
    
    /** FocusTraversalPolicy */
    private FocusTraversalPolicy traversalPolicy =
            new AccountTransferPanelFocusTraversalPolicy();
    
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return	traversalPolicy;
    }
    
    /** ボタンマウスオーバー時のマウスポインタを設定 */
    private void addMouseCursorChange() {
        //口座情報登録
        SystemInfo.addMouseCursorChange(searchCustomerButton);
        SystemInfo.addMouseCursorChange(btBankSearch);
        SystemInfo.addMouseCursorChange(addButton);
        SystemInfo.addMouseCursorChange(renewButton);
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(btClear);
        
        //請求・結果
        SystemInfo.addMouseCursorChange(btInvoiceList);
        SystemInfo.addMouseCursorChange(btRegist);
        SystemInfo.addMouseCursorChange(btExcelOutput);
        SystemInfo.addMouseCursorChange(btBillingDataOutput);
        SystemInfo.addMouseCursorChange(btFileSelection);
        SystemInfo.addMouseCursorChange(btResultData);
        
        //委託者情報登録
        SystemInfo.addMouseCursorChange(btnRegistCustomer);
    }
    
    	/**
	 * 日付かチェックする。
	 * @param year チェックする年。
	 * @param month チェックする月。
	 * @param date チェックする日。
	 * @return 受け取った年月日が日付として妥当ならばtrue、妥当でなければfalseを返す。
	 */	
	public static boolean isDate(int year, int month, int date)
	{
		try
		{
			month	=	month - 1;
			
			GregorianCalendar	cal	=	new GregorianCalendar();
			
			cal.setLenient(false);
			cal.set(year, month, date);
			
			//不正な日付がセットされた場合、例外が発生する
			cal.getTime();
			
			return	true;
		}
		catch(Exception e)
		{
			return	false;
		}
	}
    
    class SelectionListener implements TableModelListener {
        JTableEx table;
        
        // It is necessary to keep the table since it is not possible
        // to determine the table from the event's source
        SelectionListener(JTableEx table) {
            this.table = table;
        }
        public void tableChanged(TableModelEvent e) {
            int column = e.getColumn();
            int row = e.getFirstRow();
        }
    }
    
    /** ラジオボタン用KeyListener */
    private class KeyListenerForRadioButton implements KeyListener {
        KeyboardFocusManager kfm = null;
        JRadioButton prevRadioButton;
        JRadioButton nextRadioButton;
        Component prevComponent;
        Component nextComponent;
        
        /** コンストラクタ
         * @parameter prevRadioButton グループ内でひとつ前のラジオボタン
         * @parameter nextRadioButton グループ内でひとつ後のラジオボタン
         * @parameter prevComponent Shift+Enterによるフォーカスの移動先
         * @parameter nextComponent Enterによるフォーカスの移動先
         */
        public KeyListenerForRadioButton(
                JRadioButton prevRadioButton,
                JRadioButton nextRadioButton,
                Component prevComponent,
                Component nextComponent) {
            kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            this.prevRadioButton = prevRadioButton;
            this.nextRadioButton = nextRadioButton;
            this.prevComponent = prevComponent;
            this.nextComponent = nextComponent;
        }
        
        public void keyPressed(KeyEvent e) {
            //ENTERが押された場合
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                //シフトが押されている場合
                if(e.isShiftDown()) {
                    //前のコンポーネントにフォーカスを移動
                    prevComponent.requestFocusInWindow();
                } else {
                    //次のコンポーネントにフォーカスを移動
                    nextComponent.requestFocusInWindow();
                }
            }else if(e.getKeyCode() == KeyEvent.VK_UP ||
                    e.getKeyCode() == KeyEvent.VK_LEFT){
                if(prevRadioButton != null){
                    prevRadioButton.requestFocusInWindow();
                    prevRadioButton.doClick();
                }
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
                    e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(nextRadioButton != null){
                    nextRadioButton.requestFocusInWindow();
                    nextRadioButton.doClick();
                }
            }
        }
        
        public void keyReleased(KeyEvent e){}
        
        public void keyTyped(KeyEvent e){}
    }
    
    // <editor-fold desc="フォーカス順制御等">
    private class AccountTransferPanelFocusTraversalPolicy extends FocusTraversalPolicy {
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
         */
        public Component getComponentAfter(Container aContainer, Component aComponent) {

            //口座情報登録
            if(aComponent.equals(customerNo)){
                return txtAccountCustomerNo;
            }else if(aComponent.equals(txtAccountCustomerNo)){
                return txtBankCode;
            }else if(aComponent.equals(txtBankCode)){
                return txtBankName;
            }else if(aComponent.equals(txtBankName)){
                return txtBranchCode;
            }else if(aComponent.equals(txtBranchCode)){
                return txtBranchName;
            }else if(aComponent.equals(txtBranchName)){
                return txtAccountNumber;
            }else if(aComponent.equals(txtAccountNumber)){
                return txtAccountName;
            }else if(aComponent.equals(txtAccountName)){
                return customerNo;
            }
            // 請求・結果
            if(aComponent.equals(cmbDateTargetMonth)){
                return cmbYear;
            }else if(aComponent.equals(cmbYear)){
                return cmbMonth;
            }else if(aComponent.equals(cmbMonth)){
                return txtTrasferDay;
            }else if(aComponent.equals(txtTrasferDay)){
                return txtTotalNumberOfBills;
            }else if(aComponent.equals(txtTotalNumberOfBills)){
                return txtTotalbilledAmount;
            }else if(aComponent.equals(txtTotalbilledAmount)){
                return dateCollectionDate;
            }else if(aComponent.equals(dateCollectionDate)){
                return cmbDateTargetMonth;
            }
            //委託者情報登録
            if(aComponent.equals(txtConsignorCode)){
                return txtDivision;
            }else if(aComponent.equals(txtDivision)){
                return txtConsignorName;
            }else if(aComponent.equals(txtConsignorName)){
                return cmbTransferCusDate;
            }else if(aComponent.equals(cmbTransferCusDate)){
                return cmbPayment;
            }else if(aComponent.equals(cmbPayment)){
                return txtConsignorCode;
            }

            return null;
        }
        
        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
         */
        public Component getComponentBefore(Container aContainer, Component aComponent) {
            //口座情報登録
            if(aComponent.equals(customerNo)){
                return txtAccountName;
            }else if(aComponent.equals(txtAccountCustomerNo)){
                return customerNo;
            }else if(aComponent.equals(txtBankCode)){
                return txtAccountCustomerNo;
            }else if(aComponent.equals(txtBankName)){
                return txtBankCode;
            }else if(aComponent.equals(txtBranchCode)){
                return txtBankName;
            }else if(aComponent.equals(txtBranchName)){
                return txtBranchCode;
            }else if(aComponent.equals(txtAccountNumber)){
                return txtBranchName;
            }else if(aComponent.equals(txtAccountName)){
                return txtAccountNumber;
            }
            // 請求・結果
            if(aComponent.equals(cmbDateTargetMonth)){
                return dateCollectionDate;
            }else if(aComponent.equals(cmbYear)){
                return cmbDateTargetMonth;
            }else if(aComponent.equals(cmbMonth)){
                return cmbYear;
            }else if(aComponent.equals(txtTrasferDay)){
                return cmbMonth;
            }else if(aComponent.equals(txtTotalNumberOfBills)){
                return txtTrasferDay;
            }else if(aComponent.equals(txtTotalbilledAmount)){
                return txtTotalNumberOfBills;
            }else if(aComponent.equals(dateCollectionDate)){
                return txtTotalbilledAmount;
            }
            //委託者情報登録
            if(aComponent.equals(txtConsignorCode)){
                return cmbPayment;
            }else if(aComponent.equals(txtDivision)){
                return txtConsignorCode;
            }else if(aComponent.equals(txtConsignorName)){
                return txtDivision;
            }else if(aComponent.equals(cmbTransferCusDate)){
                return txtConsignorName;
            }else if(aComponent.equals(cmbPayment)){
                return cmbTransferCusDate;
            }
            return null;
        }
        
        /**
         * トラバーサルサイクルの最初の Component を返します。
         * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
         * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
         */
        public Component getFirstComponent(Container aContainer) {
            return getDefaultComponent(aContainer);
        }
        
        /**
         * トラバーサルサイクルの最後の Component を返します。
         * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
         * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
         */
        public Component getLastComponent(Container aContainer) {
            if(aContainer.equals(panelRegistAccount)){
                return deleteButton;
            }else if(aContainer.equals(panelBilling)){
                return dateCollectionDate;
            }else if(aContainer.equals(panelRegisterCustomerInfo)){
                return cmbPayment;
            }
            return null;
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
         * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
         */
        public Component getDefaultComponent(Container aContainer) {
            if(aContainer.equals(panelRegistAccount)){
                return customerNo;
            }else if(aContainer.equals(panelBilling)){
                return cmbDateTargetMonth;
            }else if(aContainer.equals(panelRegisterCustomerInfo)){
                return txtConsignorCode;
            }
            return null;
//            return shop1;
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
        public Component getInitialComponent(Window window) {
            return customerNo;
        }
    }
    /**
     * テーブルヘッダーのレンダラーを取得する
     *
     * @param header
     * @return テーブルヘッダーのレンダラー
     */
    public BevelBorderHeaderRenderer getTableHeaderRenderer(JTableHeader header) {
        BevelBorderHeaderRenderer tableHeaderRenderer = null;
        if (tableHeaderRenderer == null) {
            tableHeaderRenderer = new BevelBorderHeaderRenderer(header);
        }
        return tableHeaderRenderer;
    }
    /**
     * JTableのヘッダの色を変更できるRenderer
     *
     * @author katagiri
     */
    public class BevelBorderHeaderRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        /**
         * 背景色と、ハイライト部・シャドウ部の色の差
         */
        private static final int DIFFERENCE_OF_COLOR = 50;

        private final JCheckBox check = new JCheckBox();

        /**
         * コンストラクタ
         *
         * @param header
         */
        public BevelBorderHeaderRenderer(JTableHeader header) {
            super();
            Color baseColor = new Color(204, 204, 204);
            check.setOpaque(false);
            this.setIcon(new CheckBoxIcon(check));
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setBackground(baseColor);
            this.setBorder(new BevelBorder(BevelBorder.RAISED,
                    this.createHighlightColor(),
                    this.createShadowColor()));

            header.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    JTable table = ((JTableHeader) e.getSource()).getTable();
                    TableColumnModel columnModel = table.getColumnModel();
                    int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                    int modelColumn = table.convertColumnIndexToModel(viewColumn);
                    if (modelColumn == COL_CHECK) {
                        check.setSelected(!check.isSelected());
                        TableModel m = table.getModel();
                        Boolean f = check.isSelected();
                        for (int i = 0; i < m.getRowCount(); i++) {
                            m.setValueAt(f, i, modelColumn);
                        }
                        ((JTableHeader) e.getSource()).repaint();
                    }
                }
            });
        }

        /**
         * コンストラクタ
         *
         * @param baseColor 背景色
         * @param highlight ハイライト
         * @param shadow シャドウ
         */
        public BevelBorderHeaderRenderer(Color baseColor, Color highlight, Color shadow) {
            super();
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setBackground(baseColor);
            this.setBorder(new BevelBorder(BevelBorder.RAISED,
                    highlight, shadow));
        }

        /**
         *
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            this.setValue((value == null) ? StringUtils.EMPTY : value.toString());
            return this;
        }

        /**
         * ハイライト色を作る
         *
         * @return
         */
        private Color createHighlightColor() {
            Color baseColor = this.getBackground();
            return new Color(
                    this.reviseValue(baseColor.getRed() + BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
                    this.reviseValue(baseColor.getGreen() + BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
                    this.reviseValue(baseColor.getBlue() + BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR));
        }

        /**
         * シャドウ色を作る
         *
         * @return
         */
        private Color createShadowColor() {
            Color baseColor = this.getBackground();
            return new Color(
                    this.reviseValue(baseColor.getRed() - BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
                    this.reviseValue(baseColor.getGreen() - BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR),
                    this.reviseValue(baseColor.getBlue() - BevelBorderHeaderRenderer.DIFFERENCE_OF_COLOR));
        }

        /**
         * 色を指定する数値が範囲外の場合補正する
         *
         * @param value 色を指定する数値
         * @return 補正後の色を指定する数値
         */
        private int reviseValue(int value) {
            if (value < 0) {
                return 0;
            } else if (255 < value) {
                return 255;
            } else {
                return value;
            }
        }

        private class CheckBoxIcon implements Icon {

            private final JCheckBox check;

            public CheckBoxIcon(JCheckBox check) {
                this.check = check;
            }

            @Override
            public int getIconWidth() {
                return check.getPreferredSize().width;
            }

            @Override
            public int getIconHeight() {
                return check.getPreferredSize().height;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                SwingUtilities.paintComponent(
                        g, check, (Container) c, x, y, getIconWidth(), getIconHeight());
            }
        }
    }
}
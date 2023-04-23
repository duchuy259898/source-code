/*
 * AccountTransferPanel.java
 *
 * Created on 2020/03/23
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;
// <editor-fold defaultstate="collapsed" desc="�����U�֘A�g">
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
 *�����U�֘A�g
 * @author  lvtu
 */

public class AccountTransferPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    /**��s�R�[�h�̍ő咷*/
    final int                           MAX_LEN_BANK_CODE               = 4;
    /**�ő咷�̃u�����`�R�[�h*/
    final int                           MAX_LEN_BRANCH_CODE             = 3;
    /**�ϑ��҃R�[�h�̍ő咷*/
    final int                           MAX_LEN_CONSIGNOR_NUMBER        = 6;
    /**�敪�̍ő咷*/
    final int                           MAX_LEN_DIVISION_NUMBER         = 2;
    /**�ő咷�̃A�J�E���g�ԍ�*/
    final int                           MAX_LEN_ACCOUNT_NUMBER          = 7;
    /** header checked process*/
    final int                           COL_CHECK                       = 11;
    /**�w�b�_�[���R�[�h */
    final int                           HEADER_RECORD                   = 1;
    /**�w�b�_�[���R�[�h */
    final int                           DATA_RECORD                     = 2;
    /**�w�b�_�[���R�[�h */
    final int                           TRAILER_RECORD                  = 8;
    /**�w�b�_�[���R�[�h */
    final int                           END_RECORD                      = 9;
    
    /**https://zengin.ajtw.net/���u���E�U�ŊJ��*/
    final String                        URL_SEARCH                      = "https://zengin.ajtw.net/";
    
    private Integer                     selIndex                        = -1;
    //�ڋq����
    private MstCustomer                 customer                        = new MstCustomer();
    //�������
    private DataAccountInfoList		accountInfoList			= new DataAccountInfoList();
    //�ꊇ�������O
    DataMonthlyBatchLogs                dataMonthlyBatchLogs            = new DataMonthlyBatchLogs();
    //�����U�֊�{���
    MstAccountTransferSetting           mstAccountTransferSetting       = new MstAccountTransferSetting();
    //�����E���ʏ��w�b�_
    DataTransferResult                  dataTransferResult              = new DataTransferResult();
    String                              csvPath                         = StringUtils.EMPTY;
    

    /** �R���X�g���N�^ */
    public AccountTransferPanel() {
        initComponents();
        this.setSize(834, 691);
        this.setPath("��{�ݒ聄������Ǘ�");
        this.setTitle("�����U�֘A�g");
        init();
    }
    
    /* �R���{�{�b�N�X�̓��e�������Ȃ� */
    private void init() {
        
        setKeyListener();
        setFocusListener();
        addMouseCursorChange();
        refreshDataAccountInfo();
        /** �����E���� */
        initBillingAndResult();
        //��{���
        loadAccountTransferSetting();
        initPaymentMethod();
    }
    
    /** Enter�������t�H�[�J�X�ړ��̐ݒ� */
    private void setKeyListener() {
        //�������o�^
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
        // �����E����
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
        //�ϑ��ҏ��o�^
        txtConsignorCode.addKeyListener(SystemInfo.getMoveNextField());
        txtDivision.addKeyListener(SystemInfo.getMoveNextField());
        txtConsignorName.addKeyListener(SystemInfo.getMoveNextField());
        cmbTransferCusDate.addKeyListener(SystemInfo.getMoveNextField());
        cmbPayment.addKeyListener(SystemInfo.getMoveNextField());
    }
    
    /** �t�H�[�J�X�擾���e�L�X�g�S�I���̐ݒ� */
    private void setFocusListener(){
        txtAccountCustomerNo.addFocusListener(SystemInfo.getSelectText());
        txtBankCode.addFocusListener(SystemInfo.getSelectText());
        txtBankName.addFocusListener(SystemInfo.getSelectText());
        txtBranchCode.addFocusListener(SystemInfo.getSelectText());
        txtBranchName.addFocusListener(SystemInfo.getSelectText());
        txtAccountNumber.addFocusListener(SystemInfo.getSelectText());
        txtAccountName.addFocusListener(SystemInfo.getSelectText());
        // �����E����
        txtTrasferDay.addFocusListener(SystemInfo.getSelectText());
        txtTotalNumberOfBills.addFocusListener(SystemInfo.getSelectText());
        txtTotalbilledAmount.addFocusListener(SystemInfo.getSelectText());
        //�ϑ��ҏ��o�^
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
                "�ڋq���", "�ڋqNo", "�ڋq��", "��s�R�[�h", "��s��", "�x�X�R�[�h", "�x�X��", "�a�����", "�����ԍ�", "�������`"
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

            customerNoLabel.setText("�ڋqNo.");

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
            rdoDepositNormal.setText("����");
            rdoDepositNormal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDepositNormal.setContentAreaFilled(false);
            rdoDepositNormal.setMargin(new java.awt.Insets(0, 0, 0, 0));

            customerNameLable.setText("�ڋq��");

            DepositTypeGroup.add(rdoDepositInterim);
            rdoDepositInterim.setText("����");
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

            lbDepositType.setText("�a�����");

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

            lbBankCode.setText("��s�R�[�h");

            lbBankName.setText("��s��");

            lbBranchCode.setText("�x�X�R�[�h");

            txtBranchCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtBranchCode.setColumns(30);

            txtBranchName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtBranchName.setColumns(30);
            txtBranchName.setInputKanji(true);

            lbBranchName.setText("�x�X��");

            lbAccountNumber.setText("�����ԍ�");

            txtAccountNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtAccountNumber.setColumns(30);

            lbAccountHolder.setText("�������`");

            txtAccountName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtAccountName.setColumns(30);

            lbBranchCodeMemo.setText("���X�֋ǂ̏ꍇ�́A�ʒ��L���̒�3��");

            lbAccountNumberMemo.setText("���X�֋ǂ̏ꍇ�́A�ʒ��ԍ��̏�7��");

            lbAccountHolderMemo.setText("��30�����i���p�J�i�啶���A���p�p���啶���j�A");

            btClear.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg"));
            btClear.setBorderPainted(false);
            btClear.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg"));
            btClear.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btClearActionPerformed(evt);
                }
            });

            lbAccountHolderMemo1.setText("�J�i�������͕s��");
            lbAccountHolderMemo1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            ((PlainDocument)txtAccountCustomerNo.getDocument()).setDocumentFilter(
                new CustomFilter(14, CustomFilter.NUMERIC));
            txtAccountCustomerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtAccountCustomerNo.setColumns(30);
            txtAccountCustomerNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N

            lbAccountCustomerNo.setText("�U�֗p�ڋqNo");

            lbAccountCustomerNoNotes.setText("�������̂�14���܂Łi�����U�ֈ˗����L�ڂ̌ڋqNo��o�^���Ă��������j");

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

            jTabbedPaneRoot.addTab("�@�������o�^�@", panelRegistAccount);

            panelBilling.setOpaque(false);

            panelScrollBillingResult.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            panelScrollBillingResult.setAutoscrolls(true);

            panelBillingResult.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "�ڋqNo", "�ڋq��", "��s�R�[�h", "��s��", "�x�X�R�[�h", "�x�X��", "�a�����", "�����ԍ�", "�������`", "�������z", "�U�֌���", ""
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
            rdoProcessingBilling.setText("�����f�[�^�o��");
            rdoProcessingBilling.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoProcessingBilling.setContentAreaFilled(false);
            rdoProcessingBilling.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoProcessingBilling.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    rdoProcessingBillingItemStateChanged(evt);
                }
            });

            ProcessingGroup.add(rdoProcessingResult);
            rdoProcessingResult.setText("�U�֌��ʎ捞");
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

            lbProcessing.setText("�����敪");

            lbTargetMonth.setText("�Ώی�");

            yeaLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            yeaLabel.setText("�N");

            monthLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            monthLable.setText("��");

            cmbMonth.setMaximumRowCount(13);
            cmbMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            cmbYear.setMaximumRowCount(12);
            cmbYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            lbTransferDate.setText("�����U�֓�");

            lbTotalNumberOfBills.setText("�������v����");

            ((PlainDocument)txtTotalNumberOfBills.getDocument()).setDocumentFilter(
                new CustomFilter(9, CustomFilter.NUMERIC));
            txtTotalNumberOfBills.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtTotalNumberOfBills.setColumns(30);
            txtTotalNumberOfBills.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtTotalNumberOfBills.setEnabled(false);
            txtTotalNumberOfBills.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

            lbTotalbilledAmount.setText("�������v���z");

            ((PlainDocument)txtTotalbilledAmount.getDocument()).setDocumentFilter(
                new CustomFilter(9, CustomFilter.NUMERIC));
            txtTotalbilledAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtTotalbilledAmount.setColumns(30);
            txtTotalbilledAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            txtTotalbilledAmount.setEnabled(false);
            txtTotalbilledAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

            lbTotalNumberOfBillsMemo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbTotalNumberOfBillsMemo.setText("��");

            lbTotalbilledAmountMemo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbTotalbilledAmountMemo.setText("�~");

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
            lbCollectionDate.setText("�����");

            dayLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            dayLable.setText("��");

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

            jTabbedPaneRoot.addTab("�@�����E���ʁ@", panelBilling);

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
            TransferCusDateMemo.setText("��");

            cmbTransferCusDate.setMaximumRowCount(12);
            initComboboxTransferDate();
            cmbTransferCusDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            lbTransferCusDate.setText("�U�֓�");

            lbConsignorCode.setText("�ϑ��҃R�[�h");

            txtConsignorCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtConsignorCode.setColumns(30);

            lbConsignorCodeMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbConsignorCodeMemo.setText("��6��");

            lbClassificationMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbClassificationMemo.setText("��2��");

            lbClassification.setText("�敪");

            txtDivision.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtDivision.setColumns(30);

            lbConsignorNameMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lbConsignorNameMemo.setText("��40�����i���p�J�i�啶���A���p�p���啶���j�A�J�i�������͕s��");

            lbConsignorName.setText("�ϑ��Җ�");

            txtConsignorName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            txtConsignorName.setColumns(30);
            txtConsignorName.setInputKanji(true);

            cmbPayment.setMaximumRowCount(12);
            cmbPayment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            paymentLabel.setText("�x�����@");

            paymentLableMemo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            paymentLableMemo.setText("�����ʎ捞���̔��|�����̏������̎x�����@");

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

            jTabbedPaneRoot.addTab("�@�ϑ��ҏ��o�^�@", panelRegisterCustomerInfo);

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

            jTabbedPaneRoot.getAccessibleContext().setAccessibleName("�@�������o�^�@");
        }// </editor-fold>//GEN-END:initComponents

                                                   
        private void btInvoiceListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInvoiceListActionPerformed
            if (this.checkInputTransferResult()) {
                this.loadTransferResult();
                if (panelBillingResult.getRowCount() <= 0) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "�����E����"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }//GEN-LAST:event_btInvoiceListActionPerformed

    private void btFileSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFileSelectionActionPerformed
        this.openCSVFile();
    }//GEN-LAST:event_btFileSelectionActionPerformed

    private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed
        SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
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
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�������o�^"),
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
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�����U�֘A�g"),
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
                                            MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "�Ώۃf�[�^"),
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
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�����U�֘A�g"),
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
                                        "�t�@�C�����I������Ă��܂���B",
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
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�����"),
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
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�����U�֘A�g"),
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
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�����U�֊�{���"),
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
    
    // <editor-fold defaultstate="collapsed" desc="�f�U�C�i�ɂ��ϐ��錾">
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
    
    // <editor-fold defaultstate="collapsed" desc="�������o�^">
    /**
     * �ĕ\�����s���B
     */
    private void refreshDataAccountInfo()
    {
            //�f�[�^�x�[�X����f�[�^��ǂݍ���
            try
            {
                    ConnectionWrapper	con	=	SystemInfo.getConnection();

                    accountInfoList.load(con);
            }
            catch(SQLException e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            //�f�[�^��\������B
            this.showDataAccountInfo();
            //���͂��N���A����
            this.clearAccountInfo();
    }
    
    /**
    * �f�[�^��\������B
    */
   private void showDataAccountInfo()
   {
           DefaultTableModel	model	=	(DefaultTableModel)panelAccounts.getModel();

           //�S�s�폜
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
    * ���͂��ꂽ�f�[�^��o�^����B
    * @return true - ����
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
     * �I�����ꂽ�f�[�^���폜����B
     *
     * @return true - ����
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
     * ������񂩂�f�[�^���Z�b�g����B
     *
     * @param accountInfo �������
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
    * ���͍��ڂ��N���A����B
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
            //�{�^����Enabled��ύX����B
            this.changeButtonEnabled(false);

           if(0 < panelAccounts.getRowCount())
                           panelAccounts.removeRowSelectionInterval(0, panelAccounts.getRowCount() - 1);

    }
    /**
    * ���̓`�F�b�N���s���B
    * @return ���̓G���[���Ȃ����true��Ԃ��B
    */
   private boolean checkInputAccountInfo(boolean isAdd)
   {
        try {
            //�ڋqNo.
            if(this.customer == null || this.customer.getCustomerID() == null)
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�ڋqNo."),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                customerNo.requestFocusInWindow();
                return	false;
            }
            if (isAdd) {
                //���ɓ���̌ڋqID�̏�񂪑��݂��Ă���ꍇ�͈ȉ��A���[�g��\��
                DataAccountInfo accountInfo = new DataAccountInfo();
                accountInfo.setCustomer(customer);
                if (accountInfo.isExists(SystemInfo.getConnection())) {
                    MessageDialog.showMessageDialog(this,
                            "���Ɍ�����񂪓o�^����Ă��܂��B",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    customerNo.requestFocusInWindow();
                    return	false;
                }
            }
            //�����U�֗p�ڋqNO
            if(txtAccountCustomerNo.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�����U�֗p�ڋqNO"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountCustomerNo.requestFocusInWindow();
                return	false;
            }
            if(!CheckUtil.isNumber(txtAccountCustomerNo.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����U�֗p�ڋqNO"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountCustomerNo.requestFocusInWindow();
                return	false;
            }
            if(!CheckUtil.checkStringLength(txtAccountCustomerNo.getText(), 14))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����U�֗p�ڋqNO"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountCustomerNo.requestFocusInWindow();
                return	false;
            }
            //��s�R�[�h
            if(txtBankCode.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "��s�R�[�h"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankCode.requestFocusInWindow();
                return	false;
            }
            //���p����4���B4���ɖ����Ȃ��ꍇ�́u�ǉ��v�u�X�V�v���Ɉȉ��A���[�g��\��
            if(!(txtBankCode.getText().trim().length() == MAX_LEN_BANK_CODE))
            {
                MessageDialog.showMessageDialog(this,
                        "��s�R�[�h��4���œ��͂��Ă��������B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankCode.requestFocusInWindow();
                return	false;
            }
            //��s��
            if(txtBankName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "��s��"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankName.requestFocusInWindow();
                return	false;
            }
            //�x�X�R�[�h
            if(txtBranchCode.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�x�X�R�[�h"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchCode.requestFocusInWindow();
                return	false;
            }
            //�x�X�R�[�h
            if(!(txtBranchCode.getText().trim().length() == MAX_LEN_BRANCH_CODE))
            {
                MessageDialog.showMessageDialog(this,
                        "�x�X�R�[�h��3���œ��͂��Ă��������B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchCode.requestFocusInWindow();
                return	false;
            }
            //�x�X��
            if(txtBranchName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�x�X��"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchName.requestFocusInWindow();
                return	false;
            }
            //�����ԍ�
            if(txtAccountNumber.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�����ԍ�"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountNumber.requestFocusInWindow();
                return	false;
            }
            if(!(txtAccountNumber.getText().trim().length() == MAX_LEN_ACCOUNT_NUMBER))
            {
                MessageDialog.showMessageDialog(this,
                        "�����ԍ���7���œ��͂��Ă��������B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountNumber.requestFocusInWindow();
                return	false;
            }
            //�������`
            if(txtAccountName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�������`"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtAccountName.requestFocusInWindow();
                return	false;
            }
            //��s�R�[�h
            if(!CheckUtil.isNumber(txtBankCode.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "��s�R�[�h"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBankCode.requestFocusInWindow();
                return	false;
            }
            //�x�X�R�[�h
            if(!CheckUtil.isNumber(txtBranchCode.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�x�X�R�[�h"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtBranchCode.requestFocusInWindow();
                return	false;
            }
            //�����ԍ�
            if(!CheckUtil.isNumber(txtAccountNumber.getText()))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����ԍ�"),
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
     * �I���f�[�^���ύX���ꂽ�Ƃ��̏������s���B
     */
    private void changeCurrentData() {
        int index = panelAccounts.getSelectedRow();

        if (0 <= index && index < accountInfoList.size() && index != selIndex) {
            selIndex = index;
            //�I������Ă���f�[�^��\��
            this.showCurrentData();
        }

        this.changeButtonEnabled(0 <= selIndex);
    }

    /**
     * �I�����ꂽ�f�[�^����͍��ڂɕ\������B
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
     * �{�^����Enabled��ύX����B
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
     * https://zengin.ajtw.net/���u���E�U�ŊJ��
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
    
    // <editor-fold defaultstate="collapsed" desc="�����E����">
    /**
     * init �����E����
     */
    private void initBillingAndResult() {
        changeComponentEnabled(Boolean.FALSE);
        //�Ώی�
        loadTargetMonth();
        // �����U�֓�
        loadTransferDate();
        //�����E���ʏ��w�b�_
        //loadTransferResult();
    }
    /**
    * ���̓`�F�b�N���s���B
    * @return ���̓G���[���Ȃ����true��Ԃ��B
    */
    private boolean checkInputAccountResult()
    {

        //�����U�֓�
        if (cmbYear.getSelectedIndex() <= 0) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�����U�֓�"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmbYear.requestFocusInWindow();
            return false;
        }
        //�����U�֓�
        if (cmbMonth.getSelectedIndex() <= 0) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�����U�֓�"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            cmbMonth.requestFocusInWindow();
            return false;
        }
        //�����U�֓�
        if(txtTrasferDay.getText().equals(StringUtils.EMPTY))
        {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�����U�֓�"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtTrasferDay.requestFocusInWindow();
            return	false;
        }
        //�����U�֓�
        if(!CheckUtil.isNumber(txtTrasferDay.getText()))
        {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����U�֓�"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtTrasferDay.requestFocusInWindow();
            return	false;
        }
        //�����U�֓�
        try {
            if (!isDate(Integer.parseInt(cmbYear.getSelectedItem().toString()), Integer.parseInt(cmbMonth.getSelectedItem().toString()), Integer.parseInt(txtTrasferDay.getText()))) {
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����U�֓�"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                txtTrasferDay.requestFocusInWindow();
                return	false;
            }
        }catch(Exception e) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����U�֓�"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtTrasferDay.requestFocusInWindow();
            return	false;
        }

        return	true;
    }
    /**
     * �����E���ʏ��w�b�_
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
    * �f�[�^��\������B
    */
   private void showDataTransferResult()
   {
           DefaultTableModel	model	=	(DefaultTableModel)panelBillingResult.getModel();

           //�S�s�폜
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
    * ���͂��ꂽ�f�[�^��o�^����B
    * @return true - ����
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
    * ���͂��ꂽ�f�[�^��o�^����B
    * @return true - ����
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
                //�g�����U�N�V�����R�~�b�g
                con.commit();
                con.close();
       } catch(SQLException e)
       {
               SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
       }

       return true;
   }
    /**
     * �����E���ʏ��w�b�_����f�[�^���Z�b�g����B
     *
     * @param trasfer �����E���ʏ��
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
     * �Ώی�
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
     * �E�����f�[�^�o�͂̏ꍇ
     *  �N�A���͓����������\���A���͊�{���o�^�Őݒ肵���U�֓���\��
     *�E�U�֌��ʎ捞�̏ꍇ
     *  �Ώی��Ɋ�Â��ēo�^���ꂽ�N������\��
     */
    private void loadTransferDate() {
        this.cmbYear.removeAllItems();
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        //�N
        this.cmbYear.addItem(StringUtils.EMPTY);
        for (int i = 0; i < 5; i++) {
            this.cmbYear.addItem(String.valueOf(nowYear - i));
        }
        this.cmbYear.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
        //��
        this.cmbMonth.addItem(StringUtils.EMPTY);
        for (int mon = 1; mon <= 12; mon++) {
            this.cmbMonth.addItem(String.valueOf(mon));
        }
        this.cmbMonth.setSelectedItem(String.valueOf((cal.get(Calendar.MONTH)+1)));
        //��
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
     * ������Enabled��ύX����B
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
                            MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "�ϑ��҃R�[�h"),
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
                                        MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "�Ώۃf�[�^"),
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
        //�w�b�_�[���R�[�h
        lineData.append(String.valueOf(HEADER_RECORD));//�f�[�^�敪
        lineData.append("91");          //��ʃR�[�h
        lineData.append("1");           //�R�[�h�敪
        lineData.append(mstAccountTransferSetting.getConsignorCode()).append(mstAccountTransferSetting.getDivision()).append("00");//�ϑ��҃R�[�h
        //�ϑ��Җ�
        lineData.append(padLeftSpaces(mstAccountTransferSetting.getConsignorName(), 40)); //���p�J�i�啶������є��p�p���啶���ō��l�߁@���J�i�������͕s�B���L�Q�ƁB
        //�U�֓�
        int trasferDay = Integer.parseInt(txtTrasferDay.getText()) ;
        lineData.append(Integer.parseInt(cmbMonth.getSelectedItem().toString()) > 9 ? cmbMonth.getSelectedItem().toString() : "0" + cmbMonth.getSelectedItem().toString()).append(trasferDay > 9 ? trasferDay : "0" + trasferDay);
        //�_�~�[
        lineData.append(padLeftSpaces(StringUtils.EMPTY, 62));
        bw.write(lineData.toString());
        bw.newLine();
        //�f�[�^���R�[�h
        lineData.setLength(0);
        int totalNumber     = 0;
        int totalAmount     = 0;
        for (int row = 0;row < panelBillingResult.getRowCount(); row ++) {
            boolean isCheck = (Boolean)this.panelBillingResult.getValueAt(row, COL_CHECK);
            if (!isCheck) {
                continue;
            }
            DataTransferResultDetail detail = (DataTransferResultDetail) panelBillingResult.getValueAt(row, COL_CHECK - 1);
            //�f�[�^�敪
            lineData.append(String.valueOf(DATA_RECORD));
            //�U�֋�s�ԍ�
            lineData.append(detail.getBankCode());
            //�U�֋�s��
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 15));
            //�U�֋�s�x�X�ԍ�
            lineData.append(detail.getBranchCode());
            //�U�֋�s�x�X��
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 15));
            //�_�~�[
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 4));
            //�a�����
            lineData.append(detail.getDataAccountInfo().getAccountType());
            //�����ԍ�
            lineData.append(detail.getDataAccountInfo().getAccountNumber());
            //�a���Җ�
            lineData.append(padLeftSpaces(detail.getDataAccountInfo().getAccountName(), 30));
            //�U�֋��z
            lineData.append(padRightZeros(detail.getBillingAmount().toString(), 10));
            //�V�K�R�[�h
            lineData.append("1");
            //�ڋq�ԍ�
            lineData.append("0");
            lineData.append(mstAccountTransferSetting.getConsignorCode().substring(0, 5));
            //�����U�֗p�ڋqNO
            lineData.append(detail.getAccountCustomerNo());
            //�U�֌��ʃR�[�h
            lineData.append("0");
            //�_�~�[
            lineData.append(padLeftSpaces(StringUtils.EMPTY, 8));
            
            bw.write(lineData.toString());
            bw.newLine();
            lineData.setLength(0);
            
            totalNumber ++;
            totalAmount += detail.getBillingAmount();
        }
        
        //�g���[���[���R�[�h
        lineData.setLength(0);
        //�f�[�^�敪
        lineData.append(String.valueOf(TRAILER_RECORD));
        //�������v����
        lineData.append(padRightZeros(String.valueOf(totalNumber), 6));
        //�������v���z
        lineData.append(padRightZeros(String.valueOf(totalAmount), 12));
        //�U�֍ύ��v����
        lineData.append(padRightZeros(StringUtils.EMPTY, 6));
        //�U�֍ύ��v���z
        lineData.append(padRightZeros(StringUtils.EMPTY, 12));
        //�U�֕s�\���v����
        lineData.append(padRightZeros(StringUtils.EMPTY, 6));
        //�U�֕s�\���v���z
        lineData.append(padRightZeros(StringUtils.EMPTY, 12));
        //�_�~�[
        lineData.append(padLeftSpaces(StringUtils.EMPTY, 65));
        bw.write(lineData.toString());
        bw.newLine();
        
        //�G���h���R�[�h
        lineData.setLength(0);
        //�f�[�^�敪
        lineData.append(String.valueOf(END_RECORD));
        //�_�~�[
        lineData.append(padLeftSpaces(StringUtils.EMPTY, 119));
        bw.write(lineData.toString());
        //�����f�[�^�o�͂̃t�@�C���̍ŏI�s�ɂ��ĉ��s�����Ăق����ł�
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
     * ���X�y�[�X�𖄂߂�
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
     * �E�[���𖄂߂�
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
	 * CSV�t�@�C�����J��
	 */
	private void openCSVFile()
	{
		JFileChooser	jfc	=	new JFileChooser();
		WildcardFileFilter filter = new WildcardFileFilter("*.csv", "CSV�t�@�C��");
		jfc.setFileFilter(filter);

		if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			
			File	f	=	new File(jfc.getSelectedFile().getAbsolutePath());
			
			//�t�@�C�������݂���ꍇ
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
     * EXCEL�o��
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
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�����U�֓�"),
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
                                MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA, "�U�֓�"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JExcelApi jx = new JExcelApi("�����f�[�^�o��");
        jx.setTemplateFile("/reports/�����f�[�^�o��.xls");

        // �w�b�_�o��
        //�Ώی�
        jx.setValue(2, 3, (dataTransferResult.getTargetMonth() != null) ? dataTransferResult.getTargetMonth() : StringUtils.EMPTY);
        //�����U�֓�
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
        //�������v����
        jx.setValue(2, 5, txtTotalNumberOfBills.getText() != null ? Long.valueOf(txtTotalNumberOfBills.getText()) : StringUtils.EMPTY);
        //�������v���z
        jx.setValue(2, 6, txtTotalbilledAmount.getText() != null ? Long.valueOf(txtTotalbilledAmount.getText()) : StringUtils.EMPTY);

        int outRow = 9;

        // �ǉ��s���Z�b�g
        jx.insertRow(outRow, panelBillingResult.getRowCount() - 1);

        // �f�[�^�Z�b�g
        DefaultTableModel model = (DefaultTableModel) panelBillingResult.getModel();
        for (int row = 0; row < panelBillingResult.getRowCount(); row ++) {
            for (int col = 0; col < panelBillingResult.getColumnCount()-1; col ++) {
                //�������z
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
     * �Ώی�
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
            //�Ώی�
            if (cmbDateTargetMonth.getSelectedIndex() <= 0) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�Ώی�"),
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
        
    // <editor-fold defaultstate="collapsed" desc="��{���o�^">
        /**
         * ���X�g���e�́u14�v�u27�v�ł����ꂩ��I������
         */
        private void initComboboxTransferDate() {
            cmbTransferCusDate.removeAllItems();
            cmbTransferCusDate.addItem("14");
            cmbTransferCusDate.addItem("27");
        }
        /**
         * ��{��񃍁[�h
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
         * �f�[�^�̊�{����\��
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
                //�����U�֓�
                this.txtTrasferDay.setText(mstAccountTransferSetting.getTransferDate());
            }
        }
        
    /**
    * ���͂��ꂽ�f�[�^��o�^����B
    * @return true - ����
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
           //�ϑ��҃R�[�h
           if(txtConsignorCode.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�ϑ��҃R�[�h"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtConsignorCode.requestFocusInWindow();
                return	false;
            }
            //�ϑ��҃R�[�h
            if(!(txtConsignorCode.getText().trim().length() == MAX_LEN_CONSIGNOR_NUMBER))
            {
                MessageDialog.showMessageDialog(this,
                        "�ϑ��҃R�[�h��6���œ��͂��Ă��������B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtConsignorCode.requestFocusInWindow();
                return	false;
            }
            //�敪
           if(txtDivision.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�敪"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtDivision.requestFocusInWindow();
                return	false;
            }
           if(!(txtDivision.getText().length() == MAX_LEN_DIVISION_NUMBER))
            {
                MessageDialog.showMessageDialog(this,
                        "�敪��2���œ��͂��Ă��������B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtDivision.requestFocusInWindow();
                return	false;
            }
           //�ϑ��҃R�[�h
           if(txtConsignorName.getText().equals(StringUtils.EMPTY))
            {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�ϑ��Җ�"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                txtConsignorName.requestFocusInWindow();
                return	false;
            }
            //�x�����@
            if (cmbPayment.getSelectedIndex() <= 0) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�x�����@"),
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
     * �v���t�B�b�N�X�x�������@�N���X��ݒ肷��
     */
    private void prefixPaymentMethodName(MstPaymentClasses mp) {
        for(MstPaymentClass methodClass : mp) {
            if(methodClass.size() > 1) {
                for (MstPaymentMethod method : methodClass) {
                    method.setPaymentMethodName(methodClass.getPaymentClassName() + "�j" + method.getPaymentMethodName());
                }
            }
        }
    }
    // </editor-fold>
    
    /**
     * �ڋq���Z�b�g����B
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
     * ���[�U�����{�^�����擾����
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
                            SwingUtil.openAnchorDialog( (JFrame)null, true, mcp, "�ڋq���", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                        } finally {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }

                        mcp = null;
                    }
            });
            return searchButton;
    }

    	/**
	 * JTable�̗񕝂�����������B
	 */
	private void initTableAccountsInfoColumnWidth()
	{
		//��̕���ݒ肷��B
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
	 * JTable�̗񕝂�����������B
	 */
	private void initTableBillingResultColumnWidth()
	{
		//��̕���ݒ肷��B
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
     * FocusTraversalPolicy���擾����B
     * @return FocusTraversalPolicy
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return	traversalPolicy;
    }
    
    /** �{�^���}�E�X�I�[�o�[���̃}�E�X�|�C���^��ݒ� */
    private void addMouseCursorChange() {
        //�������o�^
        SystemInfo.addMouseCursorChange(searchCustomerButton);
        SystemInfo.addMouseCursorChange(btBankSearch);
        SystemInfo.addMouseCursorChange(addButton);
        SystemInfo.addMouseCursorChange(renewButton);
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(btClear);
        
        //�����E����
        SystemInfo.addMouseCursorChange(btInvoiceList);
        SystemInfo.addMouseCursorChange(btRegist);
        SystemInfo.addMouseCursorChange(btExcelOutput);
        SystemInfo.addMouseCursorChange(btBillingDataOutput);
        SystemInfo.addMouseCursorChange(btFileSelection);
        SystemInfo.addMouseCursorChange(btResultData);
        
        //�ϑ��ҏ��o�^
        SystemInfo.addMouseCursorChange(btnRegistCustomer);
    }
    
    	/**
	 * ���t���`�F�b�N����B
	 * @param year �`�F�b�N����N�B
	 * @param month �`�F�b�N���錎�B
	 * @param date �`�F�b�N������B
	 * @return �󂯎�����N���������t�Ƃ��đÓ��Ȃ��true�A�Ó��łȂ����false��Ԃ��B
	 */	
	public static boolean isDate(int year, int month, int date)
	{
		try
		{
			month	=	month - 1;
			
			GregorianCalendar	cal	=	new GregorianCalendar();
			
			cal.setLenient(false);
			cal.set(year, month, date);
			
			//�s���ȓ��t���Z�b�g���ꂽ�ꍇ�A��O����������
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
    
    /** ���W�I�{�^���pKeyListener */
    private class KeyListenerForRadioButton implements KeyListener {
        KeyboardFocusManager kfm = null;
        JRadioButton prevRadioButton;
        JRadioButton nextRadioButton;
        Component prevComponent;
        Component nextComponent;
        
        /** �R���X�g���N�^
         * @parameter prevRadioButton �O���[�v���łЂƂO�̃��W�I�{�^��
         * @parameter nextRadioButton �O���[�v���łЂƂ�̃��W�I�{�^��
         * @parameter prevComponent Shift+Enter�ɂ��t�H�[�J�X�̈ړ���
         * @parameter nextComponent Enter�ɂ��t�H�[�J�X�̈ړ���
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
            //ENTER�������ꂽ�ꍇ
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                //�V�t�g��������Ă���ꍇ
                if(e.isShiftDown()) {
                    //�O�̃R���|�[�l���g�Ƀt�H�[�J�X���ړ�
                    prevComponent.requestFocusInWindow();
                } else {
                    //���̃R���|�[�l���g�Ƀt�H�[�J�X���ړ�
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
    
    // <editor-fold desc="�t�H�[�J�X�����䓙">
    private class AccountTransferPanelFocusTraversalPolicy extends FocusTraversalPolicy {
        /**
         * aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
         * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
         * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
         * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
         */
        public Component getComponentAfter(Container aContainer, Component aComponent) {

            //�������o�^
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
            // �����E����
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
            //�ϑ��ҏ��o�^
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
         * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
         * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
         * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
         * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
         */
        public Component getComponentBefore(Container aContainer, Component aComponent) {
            //�������o�^
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
            // �����E����
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
            //�ϑ��ҏ��o�^
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
         * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
         * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
         * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
         */
        public Component getFirstComponent(Container aContainer) {
            return getDefaultComponent(aContainer);
        }
        
        /**
         * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
         * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
         * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
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
         * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
         * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
         * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
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
         * �E�B���h�E���ŏ��ɕ\�����ꂽ�Ƃ��Ƀt�H�[�J�X���ݒ肳���R���|�[�l���g��Ԃ��܂��B
         * show() �܂��� setVisible(true) �̌Ăяo���ň�x�E�B���h�E���\�������ƁA
         * �������R���|�[�l���g�͂���ȍ~�g�p����܂���B
         * ��x�ʂ̃E�B���h�E�Ɉڂ����t�H�[�J�X���Ăѐݒ肳�ꂽ�ꍇ�A
         * �܂��́A��x��\����ԂɂȂ����E�B���h�E���Ăѕ\�����ꂽ�ꍇ�́A
         * ���̃E�B���h�E�̍Ō�Ƀt�H�[�J�X���ݒ肳�ꂽ�R���|�[�l���g���t�H�[�J�X���L�҂ɂȂ�܂��B
         * ���̃��\�b�h�̃f�t�H���g�����ł̓f�t�H���g�R���|�[�l���g��Ԃ��܂��B
         * @param window �����R���|�[�l���g���Ԃ����E�B���h�E
         * @return �ŏ��ɃE�B���h�E���\�������Ƃ��Ƀt�H�[�J�X�ݒ肳���R���|�[�l���g�B�K�؂ȃR���|�[�l���g���Ȃ��ꍇ�� null
         */
        public Component getInitialComponent(Window window) {
            return customerNo;
        }
    }
    /**
     * �e�[�u���w�b�_�[�̃����_���[���擾����
     *
     * @param header
     * @return �e�[�u���w�b�_�[�̃����_���[
     */
    public BevelBorderHeaderRenderer getTableHeaderRenderer(JTableHeader header) {
        BevelBorderHeaderRenderer tableHeaderRenderer = null;
        if (tableHeaderRenderer == null) {
            tableHeaderRenderer = new BevelBorderHeaderRenderer(header);
        }
        return tableHeaderRenderer;
    }
    /**
     * JTable�̃w�b�_�̐F��ύX�ł���Renderer
     *
     * @author katagiri
     */
    public class BevelBorderHeaderRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        /**
         * �w�i�F�ƁA�n�C���C�g���E�V���h�E���̐F�̍�
         */
        private static final int DIFFERENCE_OF_COLOR = 50;

        private final JCheckBox check = new JCheckBox();

        /**
         * �R���X�g���N�^
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
         * �R���X�g���N�^
         *
         * @param baseColor �w�i�F
         * @param highlight �n�C���C�g
         * @param shadow �V���h�E
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
         * �n�C���C�g�F�����
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
         * �V���h�E�F�����
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
         * �F���w�肷�鐔�l���͈͊O�̏ꍇ�␳����
         *
         * @param value �F���w�肷�鐔�l
         * @return �␳��̐F���w�肷�鐔�l
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
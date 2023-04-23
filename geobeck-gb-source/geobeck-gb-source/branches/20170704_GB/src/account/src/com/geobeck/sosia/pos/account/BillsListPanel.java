/*
 * BillsListPanel.java
 *
 * Created on 2006/10/18, 19:45
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.basicinfo.SelectSameNoData;
import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.master.account.MstReceiptSetting;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.swing.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author  katagiri
 */
public class BillsListPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	private	BillsListFocusTraversalPolicy ftp = new BillsListFocusTraversalPolicy();
	
	private BillsList bl = new BillsList();
	
	/**
	 * 顧客ＩＤ
	 */
	private Integer customerID = null;
        // IVS_PTQUANG start add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
        protected MstReceiptSetting mstReceiptSetting = null;
        private static String MESSAGE_COMFIRM_REPORT = "ご入金確認書を印刷しますか？";
        // IVS_PTQUANG   end add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
	/** Creates new form BillsListPanel */
	public BillsListPanel()
	{
		super();
		initComponents();
		addMouseCursorChange();
                //IVS_LVTu start edit 2015/10/23 New request #43755
		this.setSize(830, 700);
		closeButton.setVisible(false);
		this.setPath("精算管理");
		this.setTitle("売掛回収処理");
                //IVS_LVTu start add 2015/07/29 New request #41101
                SystemInfo.initGroupShopComponents(shop, 2);
                //IVS_LVTu start add 2015/07/29 New request #41101
                counselingSheetButton.setEnabled(true);
                //IVS_LVTu end add 2015/07/29 New request #41101
		this.init();
                setListener();
                //IVS_LVTu end edit 2015/10/23 New request #43755
	}
        
        
        //nhanvt start add 20141104 Request #31293
        //IVS_LVTu start edit 2015/10/23 New request #43755
        /**
         * create table
         */
        public void initCollect(){
        
            collects = new  com.geobeck.swing.JTableEx();

            collects.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "売上日", "伝票No.", "顧客No.", "顧客名", "回収日", "レジ担当", "回収金額", "現金", "カード", "電子マネー", "金券他", "売掛残", "レシート", "取消"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, true
            };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });

            collects.setSelectionForeground(new java.awt.Color(0, 0, 0));


            collects.getTableHeader().setReorderingAllowed(false);
            collects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            SwingUtil.setJTableHeaderRenderer(collects, SystemInfo.getTableHeaderRenderer());
            collects.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(collects);
            collects.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    collectsMouseReleased(evt);
                }
            });
            collects.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    collectsKeyReleased(evt);
                }
            });

            jScrollPane1.setViewportView(collects);



        }
        //IVS_LVTu end edit 2015/10/23 New request #43755
      
	//IVS_PTQUANG start add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
    /**
     * 
     * @param billData
     * @return 
     */
    private JButton getPrintButton(final CollectedBillData billData) {

        JButton printButton = null;
        printButton = new JButton();
        printButton.setBorderPainted(false);
        printButton.setContentAreaFilled(false);
        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/reprint_off.jpg")));
        printButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/reprint_on.jpg")));

        printButton.setSize(48, 25);
        String title = this.getTitle();
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                       
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            boolean comfirmMessage = comfirmMessageDialog();
                            if(comfirmMessage == true)
                            {
                                try {
                                    ReceiptGeneralModel receiptModel = new ReceiptGeneralModel(
                                            billData.getCustomer(),
                                            billData.getSlipNo(),
                                            billData.getSalesDate1(),
                                            billData.getPaymentDate(),
                                            billData.getStaff(),
                                            billData.getCollectedValue(),
                                            billData.getCardValue(),
                                            billData.getChangeValue(),
                                            billData.getCashValue(),
                                            billData.getVouchersValue(),
                                            billData.getEcashValue(),
                                            billData.getBillValue()
                                    );
                                    receiptModel.printReceiptGeneral();                            
                                } catch (Exception ex) {
                                    Logger.getLogger(BillsListPanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        return printButton;
    }

    private boolean comfirmMessageDialog() {
        int flag = 0;
        int result = MessageDialog.showYesNoDialog(this,
                MESSAGE_COMFIRM_REPORT,
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_OPTION);
        return flag == result;
    }
	//IVS_PTQUANG end add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい

    /**
     * button cancel
     * @param billData
     * @return 
     */
    private JButton getCancelButton(final CollectedBillData billData) {
        
        JButton delButton = null;
        delButton = new JButton();
        delButton.setBorderPainted(false);
        delButton.setContentAreaFilled(false);

        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/remove_off.jpg")));
        delButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/remove_on.jpg")));

        delButton.setSize(48, 25);
        String title = this.getTitle();
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                       
                try {

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            if(cancelBill(billData)){
                                initCollect();
                                init();
                                showData();
                            }

                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

            }
        });
            
        

        return delButton;
    }
    
    
    /**
     * cancel bill
     * @param billData
     * @return 
     */
    public boolean cancelBill(CollectedBillData billData){
        if(MessageDialog.showYesNoDialog(this,
							"売掛回収を取消してよろしいですか？",
							this.getTitle(),
							JOptionPane.QUESTION_MESSAGE) != 0)
        {
                return false;
        }
        ConnectionWrapper	con	=	SystemInfo.getConnection();
        boolean result = false;
        try
        {
                con.begin();
                if(deleteAll(con, billData)){
                    result = true;
                }else{
                    result = false;
                }              
                 
               if(result){
                    con.commit();
                    
               }else{
                   con.rollback();
               }
             
        }
        catch(SQLException e)
        {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                
            }
            
        }
        return true;
        
    }
    
    
    /**
     * sql delete payment
     * @param billData
     * @return 
     */
    public String getDeletePaymentSQL(CollectedBillData billData)
    {
        
            StringBuilder sql = new StringBuilder();
            sql.append(" update data_payment\n ");
            sql.append(" set delete_date = current_timestamp\n ");
            sql.append(" where delete_date is null\n ");
            sql.append("and shop_id = " + SQLUtil.convertForSQL(billData.getShop().getShopID()) + "\n");
            sql.append("and slip_no = " + SQLUtil.convertForSQL(billData.getSlipNo()) + "\n");
            sql.append("and payment_no = " + SQLUtil.convertForSQL(billData.getPaymentNo()) + "\n");
           
            return	sql.toString();
    }
	
	/**
         * sql delete payment detail
         * @param billData
         * @return 
         */
	public String getDeletePaymentDetailSQL(CollectedBillData billData)
	{
		StringBuilder sql = new StringBuilder();
                sql.append(" update data_payment_detail\n ");
                sql.append(" set delete_date = current_timestamp\n ");
                sql.append(" where delete_date is null\n ");
                sql.append("and shop_id = " + SQLUtil.convertForSQL(billData.getShop().getShopID()) + "\n");
                sql.append("and slip_no = " + SQLUtil.convertForSQL(billData.getSlipNo()) + "\n");
                sql.append("and payment_no = " + SQLUtil.convertForSQL(billData.getPaymentNo()) + "\n");
           
            return	sql.toString();
	}
        
        /**
         * delete payment
         * @param con
         * @param billData
         * @return
         * @throws SQLException 
         */
	public boolean deletePayment(ConnectionWrapper con,  CollectedBillData billData) throws SQLException
	{
		con.execute(this.getDeletePaymentSQL(billData));
		
		return	true;
	}
	
	
	/**
         * delete payment detail
         * @param con
         * @param billData
         * @return
         * @throws SQLException 
         */
	public boolean deletePaymentDetail(ConnectionWrapper con,  CollectedBillData billData) throws SQLException
	{
		con.execute(this.getDeletePaymentDetailSQL(billData));
		
		return	true;
	}
        
        /**
         * delete all
         * @param con
         * @param billData
         * @return
         * @throws SQLException 
         */
	public boolean deleteAll(ConnectionWrapper con, CollectedBillData billData ) throws SQLException
	{
		//売上明細、支払、支払明細
		if(!this.deletePayment(con,billData ))
				return	false;
		//売上ヘッダ
		if(!this.deletePaymentDetail(con,billData))
				return	false;
		
		return	true;
	}
        //nhanvt end add 20141104 Request #31293
        //IVS_LVTu start edit 2015/10/23 New request #43755
        
	public BillsListPanel(Integer customerID)
	{
		super();
		initComponents();
		addMouseCursorChange();
                //nhanvt start edit 20150209 Bug #35179
		//this.setSize(576, 408);
                //this.setSize(800, 680);
                this.setSize(830, 700);
                if(this.getOpener() instanceof InputAccountPanel){
                }else{
                    Date date = null;
                    this.termFrom.setDate(date);
                    this.termTo.setDate(date);
                    printButton.setVisible(false);
                }
                //nhanvt end edit 20150209 Bug #35179
		this.setCustomerID(customerID);
		closeButton.setVisible(true);
		this.setPath("精算管理");
		this.setTitle("売掛金一覧");
		this.init();
                setListener();
                if(this.bl.get(0) != null && this.bl.get(0).getSales() != null && this.bl.get(0).getSales().getCustomer() != null) {
                    customerNo.setText(this.bl.get(0).getSales().getCustomer().getCustomerNo()!=null?this.bl.get(0).getSales().getCustomer().getCustomerNo():"");
                    customerName1.setText(this.bl.get(0).getSales().getCustomer().getCustomerName(0)!=null?this.bl.get(0).getSales().getCustomer().getCustomerName(0):"");
                    customerName2.setText(this.bl.get(0).getSales().getCustomer().getCustomerName(1)!=null?this.bl.get(0).getSales().getCustomer().getCustomerName(1):"");
                }
	}
        //IVS_LVTu end edit 2015/10/23 New request #43755
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        collectButton = new javax.swing.JButton();
        counselingSheetButton = new javax.swing.JButton();
        showButton = new javax.swing.JButton();
        listTab = new javax.swing.JTabbedPane();
        billsScrollPane = new javax.swing.JScrollPane();
        bills = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        collects = new javax.swing.JTable();
        termFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lbTo = new javax.swing.JLabel();
        termTo = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        shopLabel = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lbCondition = new javax.swing.JLabel();
        rdoSalesDate = new javax.swing.JRadioButton();
        rdoPaymentDate = new javax.swing.JRadioButton();
        lblNote = new javax.swing.JLabel();
        customerNo = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)customerNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, CustomFilter.ALPHAMERIC));
        customerName1 = new com.geobeck.swing.JFormattedTextFieldEx();
        customerName2 = new com.geobeck.swing.JFormattedTextFieldEx();
        searchCustomerButton = new javax.swing.JButton();
        lblStaffType2 = new javax.swing.JLabel();
        ckUnDate = new javax.swing.JCheckBox();
        lblPeriod = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        printButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        jPanel2.setOpaque(false);

        collectButton.setIcon(SystemInfo.getImageIcon("/button/account/collect_bill_off.jpg"));
        collectButton.setBorderPainted(false);
        collectButton.setContentAreaFilled(false);
        collectButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/collect_bill_on.jpg"));
        collectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collectButtonActionPerformed(evt);
            }
        });

        counselingSheetButton.setIcon(SystemInfo.getImageIcon("/button/common/print_off.jpg"));
        counselingSheetButton.setBorderPainted(false);
        counselingSheetButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/print_on.jpg"));
        counselingSheetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counselingSheetButtonActionPerformed(evt);
            }
        });

        showButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        showButton.setBorderPainted(false);
        showButton.setContentAreaFilled(false);
        showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        showButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(showButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(counselingSheetButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(collectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(3, 3, 3))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(counselingSheetButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(collectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(showButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        listTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                listTabStateChanged(evt);
            }
        });

        billsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        bills.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "売上日", "伝票No.", "顧客No.", "顧客名", "レジ担当者", "売掛金額", "売掛残"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bills.setSelectionBackground(new java.awt.Color(220, 220, 220));
        bills.setSelectionForeground(new java.awt.Color(0, 0, 0));
        bills.getTableHeader().setReorderingAllowed(false);
        bills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtil.setJTableHeaderRenderer(bills, SystemInfo.getTableHeaderRenderer());
        this.initTableColumnWidth();
        bills.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(bills);
        bills.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                billsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                billsMouseReleased(evt);
            }
        });
        bills.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                billsKeyReleased(evt);
            }
        });
        billsScrollPane.setViewportView(bills);

        listTab.addTab("未回収", billsScrollPane);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        collects.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "売上日", "伝票No.", "顧客No.", "顧客名", "回収日", "レジ担当", "回収金額", "現金", "カード", "電子マネー", "金券他", "売掛残", "レシート", "取消"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        collects.setSelectionForeground(new java.awt.Color(0, 0, 0));
        collects.getTableHeader().setReorderingAllowed(false);
        collects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtil.setJTableHeaderRenderer(collects, SystemInfo.getTableHeaderRenderer());
        collects.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(collects);
        collects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                collectsMouseReleased(evt);
            }
        });
        collects.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                collectsKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(collects);
        this.initCollect();

        listTab.addTab("回収済", jScrollPane1);

        termFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        termFrom.setForeground(java.awt.Color.white);
        termFrom.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        termFrom.setMaximumSize(new java.awt.Dimension(65, 20));
        termFrom.setMinimumSize(new java.awt.Dimension(65, 20));
        termFrom.setPreferredSize(new java.awt.Dimension(85, 20));

        lbTo.setText("  ～");

        termTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        termTo.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());

        shopLabel.setText("店舗");

        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });

        lbCondition.setText("出力条件");

        buttonGroup1.add(rdoSalesDate);
        rdoSalesDate.setSelected(true);
        rdoSalesDate.setText("売上日");
        rdoSalesDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSalesDate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSalesDate.setOpaque(false);

        buttonGroup1.add(rdoPaymentDate);
        rdoPaymentDate.setText("回収日");
        rdoPaymentDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoPaymentDate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoPaymentDate.setOpaque(false);

        lblNote.setText("※回収日で検索する場合は「回収済」タブを選択してください。 ");

        customerNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        customerNo.setColumns(15);
        customerNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNoFocusLost(evt);
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

        searchCustomerButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
        searchCustomerButton.setBorderPainted(false);
        searchCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
        searchCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCustomerButtonActionPerformed(evt);
            }
        });

        lblStaffType2.setText("顧客No.");

        ckUnDate.setText("期間指定なし");
        ckUnDate.setOpaque(false);
        ckUnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckUnDateActionPerformed(evt);
            }
        });

        lblPeriod.setText("対象期間");

        jPanel3.setOpaque(false);

        printButton.setIcon(SystemInfo.getImageIcon("/button/print/output_pdf_off.jpg"));
        printButton.setBorderPainted(false);
        printButton.setContentAreaFilled(false);
        printButton.setPressedIcon(SystemInfo.getImageIcon("/button/print/output_pdf_on.jpg"));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(printButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(3, 3, 3))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(printButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(listTab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 822, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(shopLabel)
                                .add(28, 28, 28)
                                .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(lbCondition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, 0)
                                .add(rdoSalesDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(rdoPaymentDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(4, 4, 4)
                                .add(lblNote))
                            .add(layout.createSequentialGroup()
                                .add(lblPeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, 0)
                                .add(termFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lbTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(termTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(ckUnDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(137, 137, 137)
                                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(lblStaffType2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, 0)
                                .add(customerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10)
                                .add(searchCustomerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(167, 167, 167)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .add(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(shopLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lbCondition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoSalesDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rdoPaymentDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNote, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblPeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(termTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(ckUnDate)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, lbTo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, termFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(lblStaffType2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(customerNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(customerName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(customerName2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(searchCustomerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(listTab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void printButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_printButtonActionPerformed
	{//GEN-HEADEREND:event_printButtonActionPerformed
		this.print();
	}//GEN-LAST:event_printButtonActionPerformed

	private void billsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billsMousePressed
	    if( evt.getClickCount() == 2 ) {
		if( collectButton.isEnabled() ) {
		    SystemInfo.getLogger().log(Level.INFO, "売掛回収");
		    this.collectBill();
		}
	    }
	}//GEN-LAST:event_billsMousePressed

	private void listTabStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_listTabStateChanged
	{//GEN-HEADEREND:event_listTabStateChanged
		//IVS_LVTu start edit 2015/10/23 New request #43755
                if(listTab.getSelectedIndex()==0)
                {
                    this.changeCollectButtonEnabled();
                    rdoPaymentDate.setEnabled(false);
                    rdoSalesDate.setSelected(true);
                }
                
                else if (listTab.getSelectedIndex()==1) {
                    rdoPaymentDate.setEnabled(true);
                }
                //IVS_LVTu end edit 2015/10/23 New request #43755
               
	}//GEN-LAST:event_listTabStateChanged

	private void showButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showButtonActionPerformed
	{//GEN-HEADEREND:event_showButtonActionPerformed
		this.init();
                //IVS_LVTu start add 2015/07/29 New request #41101
                counselingSheetButton.setEnabled(true);
                //IVS_LVTu end add 2015/07/29 New request #41101
	}//GEN-LAST:event_showButtonActionPerformed

	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
	{//GEN-HEADEREND:event_closeButtonActionPerformed
		//parent.removeComponent(this);
		this.setBill();
		this.showOpener();
	}//GEN-LAST:event_closeButtonActionPerformed

	private void billsMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_billsMouseReleased
	{//GEN-HEADEREND:event_billsMouseReleased
		this.changeCollectButtonEnabled();
	}//GEN-LAST:event_billsMouseReleased

	private void billsKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_billsKeyReleased
	{//GEN-HEADEREND:event_billsKeyReleased
		this.changeCollectButtonEnabled();
	}//GEN-LAST:event_billsKeyReleased

	private void collectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_collectButtonActionPerformed
	{//GEN-HEADEREND:event_collectButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "売掛回収");
		this.collectBill();
	}//GEN-LAST:event_collectButtonActionPerformed

    private void collectsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_collectsMouseReleased
       
    }//GEN-LAST:event_collectsMouseReleased

    private void collectsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_collectsKeyReleased
       
    }//GEN-LAST:event_collectsKeyReleased

    private void counselingSheetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counselingSheetButtonActionPerformed
        this.print();
    }//GEN-LAST:event_counselingSheetButtonActionPerformed

    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        //IVS_LVTu start add 2015/07/29 New request #41101
        counselingSheetButton.setEnabled(false);
        this.bl.setShop_id(this.getSelectedShop().getShopID());
        //IVS_LVTu end add 2015/07/29 New request #41101
    }//GEN-LAST:event_shopActionPerformed

    private void customerNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNoFocusLost
        //IVS_LVTu start add 2015/10/23 New request #43755
        setCustomer();
        //IVS_LVTu end add 2015/10/23 New request #43755
    }//GEN-LAST:event_customerNoFocusLost

    private void customerName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerName1ActionPerformed

    private void customerName2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerName2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerName2ActionPerformed

    private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed
        //IVS_LVTu start add 2015/10/23 New request #43755
        searchCustomerButton.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SystemInfo.getLogger().log(Level.INFO, "顧客検索");
            SearchCustomerDialog sc = new SearchCustomerDialog(parentFrame, true);
            sc.setShopID(this.getSelectedShop().getShopID());
            sc.setVisible(true);

            if (sc.getSelectedCustomer() != null
                && !sc.getSelectedCustomer().getCustomerID().equals("")) {

                customerNo.setText(sc.getSelectedCustomer().getCustomerNo());
                customerName1.setText(sc.getSelectedCustomer().getCustomerName(0));
                customerName2.setText(sc.getSelectedCustomer().getCustomerName(1));
                this.customerID = sc.getSelectedCustomer().getCustomerID();
            }
            sc = null;

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        //IVS_LVTu end add 2015/10/23 New request #43755
    }//GEN-LAST:event_searchCustomerButtonActionPerformed

    private void ckUnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckUnDateActionPerformed
        //IVS_LVTu start add 2015/10/23 New request #43755
        if (ckUnDate.isSelected()) {
            lblPeriod.setEnabled(false);
            termFrom.setEditable(false);
            termTo.setEditable(false);
            lbTo.setEnabled(false);
        } else {
            lblPeriod.setEnabled(true);
            termFrom.setEditable(true);
            termTo.setEditable(true);
            lbTo.setEnabled(true);
        }
        //IVS_LVTu end add 2015/10/23 New request #43755
    }//GEN-LAST:event_ckUnDateActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable bills;
    private javax.swing.JScrollPane billsScrollPane;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox ckUnDate;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton collectButton;
    private javax.swing.JTable collects;
    private javax.swing.JButton counselingSheetButton;
    private com.geobeck.swing.JFormattedTextFieldEx customerName1;
    private com.geobeck.swing.JFormattedTextFieldEx customerName2;
    private com.geobeck.swing.JFormattedTextFieldEx customerNo;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbCondition;
    private javax.swing.JLabel lbTo;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblPeriod;
    private javax.swing.JLabel lblStaffType2;
    private javax.swing.JTabbedPane listTab;
    private javax.swing.JButton printButton;
    private javax.swing.JRadioButton rdoPaymentDate;
    private javax.swing.JRadioButton rdoSalesDate;
    private javax.swing.JButton searchCustomerButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JButton showButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo termFrom;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo termTo;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(closeButton);
		SystemInfo.addMouseCursorChange(collectButton);
                //IVS_LVTu start add 2015/10/23 New request #43755
                SystemInfo.addMouseCursorChange(printButton);
		SystemInfo.addMouseCursorChange(showButton);
                SystemInfo.addMouseCursorChange(counselingSheetButton);
		SystemInfo.addMouseCursorChange(searchCustomerButton);
                //IVS_LVTu end add 2015/10/23 New request #43755
	}
	
	/**
	 * 顧客ＩＤ取得する。
	 * @return 顧客ＩＤ
	 */
	public Integer getCustomerID()
	{
		return customerID;
	}

	/**
	 * 顧客ＩＤをセットする。
	 * @param customerID 顧客ＩＤ
	 */
	public void setCustomerID(Integer customerID)
	{
		this.customerID = customerID;
	}
	
	
	/**
	 * 売掛金一覧画面用のFocusTraversalPolicyを取得する。
	 * @return 売掛金一覧画面用のFocusTraversalPolicy
	 */
	public BillsListFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	
	/**
	 * 初期化処理を行う。
	 */
	public void init()
	{
                //IVS_LVTu start add 2015/07/29 New request #41101
                bl.setMstShop(getSelectedShop());
                //IVS_LVTu end add 2015/07/29 New request #41101
		bl.setTermFrom(null);
		bl.setTermTo(null);
                GregorianCalendar dateFrom = new GregorianCalendar();
		GregorianCalendar dateTo = new GregorianCalendar();
                //IVS_LVTu start edit 2015/10/23 New request #43755
		if (this.termFrom.getDate() != null && this.termFrom.isEditable() == true) {
		    dateFrom.setTime(this.termFrom.getDate());
		    bl.setTermFrom(dateFrom);
		}
		
		if (this.termTo.getDate() != null && this.termTo.isEditable() == true) {
		    dateTo.setTime(this.termTo.getDate());
		    bl.setTermTo(dateTo);
		}
                
                if ( rdoSalesDate.isSelected()) {
                    bl.setCondition(1);
                }else {
                    bl.setCondition(2);
                }
                //IVS_LVTu end edit 2015/10/23 New request #43755
		
		bl.load(customerID);
		
		this.showData();
	}
	
	/**
	 * PDF出力ボタンの表示を変更する
	 */
	public void changePrintButtonVisible( boolean flg )
	{
		printButton.setVisible( flg );
	}
	
	/**
	 * 回収処理ボタンの使用可不可の切り替えを行う。
	 */
	private void changeCollectButtonEnabled()
	{
		printButton.setEnabled(listTab.getSelectedIndex() == 0 &&
				0 <= bills.getSelectedRow());
		collectButton.setEnabled(listTab.getSelectedIndex() == 0 &&
				0 <= bills.getSelectedRow());
                
	}
       /* private void changeCollectButtonEnabled1()
	{
	
                printButton.setEnabled(listTab.getSelectedIndex() == 1 &&
				0 <= collects.getSelectedRow());
	}*/
	
	/**
	 * 売掛金データを表示する。
	 */
	private void showData()
	{
		this.showBillData();
		this.showCollectedBillData();
		this.changeCollectButtonEnabled();
	}
	
	/**
	 * 売掛金データを表示する。
	 */
	private void showBillData()
	{
	    SwingUtil.clearTable(bills);
	    DefaultTableModel model = (DefaultTableModel)bills.getModel();

	    for(Bill b : bl) {
		Object[] rowData = { String.format("%1$tY/%1$tm/%1$td",
					b.getSales().getSalesDate()),
					b.getSales().getSlipNo().toString(),
					b.getSales().getCustomer().getCustomerNo(),
					b.getSales().getCustomer().getFullCustomerName(),
					b.getStaff().getFullStaffName(),
					b.getBill(),
					b.getBillRest() };
		model.addRow(rowData);
	    }
	}
	
	/**
	 * 回収済売掛金データを表示する。
	 */
	private void showCollectedBillData()
	{
	    SwingUtil.clearTable(collects);
	    DefaultTableModel model = (DefaultTableModel)collects.getModel();

	    for(CollectedBillData cbd : bl.getCollectedBills()) {
		Object[] rowData = { String.format("%1$tY/%1$tm/%1$td",
					cbd.getSalesDate()),
					cbd.getSlipNo().toString(),
					cbd.getCustomer().getCustomerNo(),
					cbd.getCustomer().getFullCustomerName(),
					String.format("%1$tY/%1$tm/%1$td", cbd.getPaymentDate()),
					cbd.getStaff().getFullStaffName(),
					cbd.getCollectedValue(),
                                        //IVS_LVTu start add 2015/10/23 New request #43755
                                        cbd.getCashValue()<=cbd.getCollectedValue()?cbd.getCashValue():cbd.getCollectedValue(),
                                        cbd.getCardValue(),
                                        cbd.getEcashValue(),
                                        cbd.getVouchersValue(),
                                        //IVS_LVTu end add 2015/10/23 New request #43755
                                        //nhanvt start edit 20150311 Bug #35481
					(cbd.getBillValue() <0 ? 0:cbd.getBillValue()) ,
                                        //nhanvt end edit 20150311 Bug #35481
                                        //nhanvt start add 20141104 Request #31293
                                        getPrintButton(cbd),
                                        getCancelButton(cbd)
                                        //nhanvt end add 20141104 Request #31293
                };
		model.addRow(rowData);
	    }
	}
	
	/**
	 * 売掛金回収処理へ移行する。
	 */
	private void collectBill()
	{
	    int row = bills.getSelectedRow();
		
	    //伝票が選択されている場合
	    if (0 <= row) {
		//伝票No.を取得
		Integer selSlipNo = bl.get(row).getSales().getSlipNo();
			
		if(selSlipNo != null) {
		    CollectBillPanel cbp = new CollectBillPanel(this);
                    //IVS_LVTu start add 2015/07/29 New request #41101
                    cbp.getCb().setShopID(getSelectedShop().getShopID());
                    cbp.setMstShop(getSelectedShop());
                    //IVS_LVTu end add 2015/07/29 New request #41101
		    cbp.init(selSlipNo);
                    cbp.init1(bl);
		    this.setVisible(false);
		    parentFrame.changeContents(cbp);
		}
	    }
	}
	/**
	 * 売掛金一覧画面用のFocusTraversalPolicy
	 */
	private class BillsListFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent のBillsListFocusTraversalPolicy。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			return bills;
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
			return bills;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return bills;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
                    //IVS_LVTu start edit 2015/10/23 New request #43755
                    if (aContainer.equals(customerNo)) {
                        return showButton;
                    }
                    return bills;
                    //IVS_LVTu end edit 2015/10/23 New request #43755
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return bills;
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
			return bills;
		}
	}

	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
	    //列の幅を設定する。
	    bills.getColumnModel().getColumn(0).setPreferredWidth(50);
	    bills.getColumnModel().getColumn(1).setPreferredWidth(40);
	    bills.getColumnModel().getColumn(2).setPreferredWidth(50);
	    bills.getColumnModel().getColumn(3).setPreferredWidth(80);
	    bills.getColumnModel().getColumn(4).setPreferredWidth(80);
	    bills.getColumnModel().getColumn(5).setPreferredWidth(50);
	    bills.getColumnModel().getColumn(6).setPreferredWidth(50);
	}
	
	/**
	 * テーブルのTableCellRenderer
	 */
	private class TableCellAlignRenderer extends DefaultTableCellRenderer
	{
		/** Creates a new instance of ReservationTableCellRenderer */
		public TableCellAlignRenderer()
		{
			super();
		}

		/**
		 * テーブルセルレンダリングを返します。
		 * @param table JTable
		 * @param value セルに割り当てる値
		 * @param isSelected セルが選択されている場合は true
		 * @param hasFocus フォーカスがある場合は true
		 * @param row 行
		 * @param column 列
		 * @return テーブルセルレンダリング
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			
			switch(column)
			{
				case 0:
					super.setHorizontalAlignment(SwingConstants.CENTER);
					break;
				case 1:
				case 5:
				case 6:
					super.setHorizontalAlignment(SwingConstants.RIGHT);
					break;
				default:
					super.setHorizontalAlignment(SwingConstants.LEFT);
					break;
			}

			return this;
		}
	}
	
	
	/**
	 * このパネルを開いたパネルの売掛金に値を設定する。
	 */
	protected void setBill()
	{
	    InputAccountPanel iap = (InputAccountPanel)this.getOpener();
	    iap.setBill();
	}
	
	private void print()
	{
	    if(listTab.getSelectedIndex()==0)
            {
                if(this.bills.getRowCount()>0)
                {
                    // bl.print();
                     bl.printExcel1();
                }
            }
            else
            {
                if(this.collects.getRowCount()>0)
                {
                    // bl.print1();
                    bl.printExcel2();
                }
            }
	}
        //IVS_LVTu start add 2015/07/29 New request #41101
        /**
	 * 選択されている伝票No.を取得する。
	 * @return 選択されている伝票No.
	 */
	public MstShop getSelectedShop()
	{
                if (shop.getSelectedItem() == null) {
                    return SystemInfo.getCurrentShop();
                } else {
                    return (MstShop)shop.getSelectedItem();
                }
	}
        //IVS_LVTu end add 2015/07/29 New request #41101
        
        //IVS_LVTu start add 2015/10/23 New request #43755
        /**
     * 顧客をセットする。
     */
    private void setCustomer() {
        MstCustomer cus = new MstCustomer();
        
        cus.setCustomerNo(customerNo.getText());
        
        cus.setCustomerNo(this.customerNo.getText());

        try {
                cus = SelectSameNoData.getMstCustomerByNo(
                        parentFrame,
                        SystemInfo.getConnection(),
                        this.customerNo.getText(),
                        (SystemInfo.getSetteing().isShareCustomer() ? 0 : this.getSelectedShop().getShopID()));

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        if (cus == null) {
            cus = new MstCustomer();
        }
        this.customerID = cus.getCustomerID();

        customerName1.setText(cus.getCustomerName(0));
        customerName2.setText(cus.getCustomerName(1));
        showButton.requestFocusInWindow();

    }
    private void setListener() {
        customerNo.addKeyListener(SystemInfo.getMoveNextField());
        customerNo.addFocusListener(SystemInfo.getSelectText());
    }
    //IVS_LVTu end add 2015/10/23 New request #43755
}

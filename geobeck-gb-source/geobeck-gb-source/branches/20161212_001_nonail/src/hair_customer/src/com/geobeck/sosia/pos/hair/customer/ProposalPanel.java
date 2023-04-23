/*
 * ProposalPanel.java
 *
 * Created on 2015/11/16
 */
package com.geobeck.sosia.pos.hair.customer;

import com.ibm.icu.text.SimpleDateFormat;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.products.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.hair.data.account.Course;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;


/**
 *
 * @author lvtu
 */
public class ProposalPanel extends AbstractImagePanelEx implements SelectProductOpener {

    DataProposal dtProposal     = new DataProposal();
    MstCustomer cus             = new MstCustomer();
    MstShop shop                = new MstShop();
    final Integer  EDIT         = 1;
    final Integer  REPLICA      = 2;
    
    /**
     * FocusTraversalPolicy
     */
    private ProposalFocusTraversalPolicy ftp = new ProposalFocusTraversalPolicy();

    /**
     * 顧客情報登録画面用FocusTraversalPolicyを取得する。
     *
     * @return 顧客情報登録画面用FocusTraversalPolicy
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }
    
    public ProposalPanel(MstCustomer cus, DataProposal dt, Integer flag) {
        initComponents();
        this.initTableColumnWidth();
        this.setPath("顧客管理");
        this.setSize(752, 412);
        addMouseCursorChange();
        this.setKeyListener();
        this.cus = cus; 
        
        // shop 
        if (dt != null && dt.getShopID() != null) {
            try {
                //call button edit and replica from screen MstCustomerPanel
                this.shop.setShopID(dt.getShopID());
                this.shop.load(SystemInfo.getConnection());
                this.dtProposal = dt;
            } catch (SQLException ex) {
                Logger.getLogger(ProposalPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else {
            //call new button
            this.shop = SystemInfo.getCurrentShop();
        }
        
        init();
        if (dt != null) {
            showData(flag);
        }
        if (flag == this.EDIT) {
            btRegist.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
            btRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
            btOutExcel.setEnabled(true);
        }else {
            btOutExcel.setEnabled(false);
        }
        
    }
    
    public void init() {
        //顧客No.
        lbCustomerNo.setText(this.cus.getCustomerNo());
        //氏名
        customerName.setText(this.cus.getFullCustomerName());
        //有効期限
        cmbValidDate.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        //作成者
        this.initStaff();
        
        this.dtProposal.setShopID(this.shop.getShopID());
        this.dtProposal.setCustomerID(this.cus.getCustomerID());
    }
    
    public void showData(Integer flag) {
        ConnectionWrapper	con	=	SystemInfo.getConnection();
        if(con == null)
        {
            MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            this.dtProposal.loadAll(con);
        } catch(SQLException e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (Exception ex) {
            Logger.getLogger(ProposalPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //show data 
        if ( flag.equals(REPLICA)) {
            this.dtProposal.setProposalID(null);
            this.dtProposal.setShopID(SystemInfo.getCurrentShop().getShopID());
        }else {
            //提案書名
            txtProposal.setText(this.dtProposal.getProposalName().trim());
        }
        
        //作成者
        for ( int i = 1;i < cmbstaff.getItemCount(); i ++) {
            if (((MstStaff) cmbstaff.getItemAt(i)).getStaffID().equals(this.dtProposal.getStaffID())){
                cmbstaff.setSelectedIndex(i);
            }
        }
        
        //提案メモ
        note.setText(this.dtProposal.getProposalMemo());
        
        //有効期限
        cmbValidDate.setDate(this.dtProposal.getProposalValidDate());
        
        this.dtProposal.sort();
        //set table
        showDataTable();
    }
    
    //show data table
    private void showDataTable() {

        DefaultTableModel model = (DefaultTableModel) searchResult.getModel();

        if (searchResult.getCellEditor() != null) {
            searchResult.getCellEditor().stopCellEditing();
        }
        SwingUtil.clearTable(searchResult);
        // add table
        for (DataProposalDetail dpd : this.dtProposal) {
            model.addRow(
                new Object[]{
                    dpd.getProductDivision() == 2 ? "商品" : "コース",
                    dpd.getClassName(),
                    dpd.getProductName(),
                    dpd.getProductValue(),
                    dpd.getProductNum(),
                    dpd.getProductDivision() == 2 ? dpd.getProductValue()*dpd.getProductNum() : dpd.getProductValue() ,
                    this.getDeleteButton()
                });
        }
    }
    private void registData() {

        // set data
        this.dtProposal.setShopID(this.shop.getShopID());
        this.dtProposal.setProposalName(txtProposal.getText().trim());
        this.dtProposal.setStaffID(((MstStaff) cmbstaff.getSelectedItem()).getStaffID());
        this.dtProposal.setCustomerID(this.cus.getCustomerID());
        this.dtProposal.setProposalValidDate(cmbValidDate.getDate());
        this.dtProposal.setProposalMemo(note.getText().trim());
        
        //set data_proposal_detail when change data table
        for (int i = 0;i < searchResult.getRowCount();i ++ ) {
            this.dtProposal.get(i).setProductValue(Long.parseLong(searchResult.getValueAt(i, 3).toString()));
            this.dtProposal.get(i).setProductNum(Integer.parseInt(searchResult.getValueAt(i, 4).toString()));
        }
        ConnectionWrapper con = SystemInfo.getConnection();

        if (con == null) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            con.begin();

            //登録成功
            if (this.dtProposal.registAll(con)) {
                con.commit();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);

            } //登録失敗
            else {
                con.rollback();
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "顧客データ"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

    }
    public boolean checkInput() {
        if ( txtProposal.getText().trim().equals("")) {
            MessageDialog.showMessageDialog(this,
                        "提案書名が入力されていません。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            txtProposal.requestFocusInWindow();
            return false;
        }else if(((MstStaff)cmbstaff.getSelectedItem()).getStaffID() == null) {
            MessageDialog.showMessageDialog(this,
                        "作成者が選択されていません。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            cmbstaff.requestFocusInWindow();
            return false;
        }else if((cmbValidDate.getDate()) == null) {
            MessageDialog.showMessageDialog(this,
                        "有効期限が設定されていません。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            cmbValidDate.requestFocusInWindow();
            return false;
        }else if (searchResult.getModel().getRowCount() <= 0) {
            MessageDialog.showMessageDialog(this,
                        "メニューが選択されていません。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            return false;
        }else if (cmbValidDate.getDate().before(SystemInfo.getCurrentShop().getSystemDate().getTime())) {
            MessageDialog.showMessageDialog(this,
                        "提案書の有効期限は当日以降の日付けを選択してください",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            cmbValidDate.requestFocusInWindow();
            return false;
        }
        return true;
    }
    /**
     * レジ担当者を初期化する。
     */
    private void initStaff() {
        cmbstaff.addItem(new MstStaff());
        SystemInfo.initStaffComponent(cmbstaff);
        cmbstaff.setSelectedIndex(0);
    }
    
    private void outExcel(){
        btOutExcel.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            try {

                if (0 < cmbstaff.getSelectedIndex()) {
                    MstStaff ms = (MstStaff) cmbstaff.getSelectedItem();
                }
                
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy/MM/dd");
        
            JExcelApi jx = new JExcelApi("提案書");
            jx.setTemplateFile("/reports/提案書.xls");
            jx.setValue(11, 3, format.format(this.dtProposal.getProposalDate()));
            jx.setValue(11, 4, format.format(this.dtProposal.getProposalValidDate()));
            
            jx.setValue(3, 6, this.cus.getFullCustomerName() + "様（会員番号：" + this.cus.getCustomerNo() + ")" );
            jx.setValue(10, 7, this.shop.getShopName());
            MstStaff mstaff = new MstStaff();
            mstaff.setStaffID(this.dtProposal.getStaffID());
            mstaff.load(SystemInfo.getConnection());
            jx.setValue(10, 8, mstaff.getFullStaffName());
            jx.setValue(10, 9,  (this.shop.getAddress(0).equals("") ? "" : (this.shop.getAddress(0))) + this.shop.getAddress(1));
            jx.setValue(10, 10, this.shop.getAddress(2));
            jx.setValue(10, 11, this.shop.getAddress(3));
            jx.setValue(10, 12, this.shop.getPhoneNumber());

            int row = 15;
            
             for (DataProposalDetail dpd : this.dtProposal){

                jx.setValue(2, row, dpd.getProductName());
                jx.setValue(7, row, dpd.getProductNum());
                jx.setValue(8, row, dpd.getProductValue());
                if ( dpd.getProductDivision().equals(2)) {
                    jx.setValue(9, row, dpd.getProductNum()*dpd.getProductValue() );
                }else if(dpd.getProductDivision().equals(5)) {
                    jx.setValue(9, row, dpd.getProductValue() );
                }
                
                row ++ ;
             }

            jx.openWorkbook();
  
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        charge = new javax.swing.ButtonGroup();
        lbValidDate = new javax.swing.JLabel();
        searchResultScrollPane = new javax.swing.JScrollPane();
        searchResult = new com.geobeck.swing.JTableEx();
        cmbValidDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        cmbstaff = new javax.swing.JComboBox();
        txtProposal = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtProposal.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        courseNoLabel6 = new javax.swing.JLabel();
        btOutExcel = new javax.swing.JButton();
        noteScrollPane = new javax.swing.JScrollPane();
        note = new com.geobeck.swing.JTextAreaEx();
        courseNoLabel2 = new javax.swing.JLabel();
        cusNameLabel5 = new javax.swing.JLabel();
        btRegist = new javax.swing.JButton();
        customerName = new javax.swing.JLabel();
        btSearch = new javax.swing.JButton();
        lbCustomerNo = new javax.swing.JLabel();
        lbProposal = new javax.swing.JLabel();
        lbstaff = new javax.swing.JLabel();
        btClose = new javax.swing.JButton();

        setFocusCycleRoot(true);
        setFocusTraversalPolicyProvider(true);
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(752, 412));

        lbValidDate.setText("有効期限");

        searchResultScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        searchResultScrollPane.setFocusable(false);

        searchResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "区分", "分類", "コース／商品名", "単価", "回数／数量", "金額", "削除"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchResult.setSelectionBackground(new java.awt.Color(255, 210, 142));
        searchResult.setSelectionForeground(new java.awt.Color(0, 0, 0));
        searchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResult.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(searchResult, SystemInfo.getTableHeaderRenderer());
        searchResult.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(searchResult);
        TableColumnModel searchResultModel = searchResult.getColumnModel();
        searchResultModel.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
        searchResultModel.getColumn(4).setCellEditor(new IntegerCellEditor(new JTextField()));
        searchResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchResultMouseClicked(evt);
            }
        });
        searchResult.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                searchResultPropertyChange(evt);
            }
        });
        searchResultScrollPane.setViewportView(searchResult);

        cmbValidDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbValidDate.setForeground(java.awt.Color.white);
        cmbValidDate.setMaximumSize(new java.awt.Dimension(65, 20));
        cmbValidDate.setMinimumSize(new java.awt.Dimension(65, 20));
        cmbValidDate.setPreferredSize(new java.awt.Dimension(85, 20));

        cmbstaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        txtProposal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtProposal.setColumns(20);
        txtProposal.setInputKanji(true);

        courseNoLabel6.setText("提案メモ");

        btOutExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btOutExcel.setBorderPainted(false);
        btOutExcel.setFocusable(false);
        btOutExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btOutExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOutExcelbackPrevious(evt);
            }
        });

        noteScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        note.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        note.setColumns(20);
        note.setLineWrap(true);
        note.setRows(10);
        note.setTabSize(4);
        note.setInputKanji(true);
        noteScrollPane.setViewportView(note);

        courseNoLabel2.setText("顧客No.");

        cusNameLabel5.setText("氏名");

        btRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btRegist.setBorderPainted(false);
        btRegist.setFocusable(false);
        btRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistbackPrevious(evt);
            }
        });

        customerName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        customerName.setPreferredSize(new java.awt.Dimension(39, 20));

        btSearch.setIcon(SystemInfo.getImageIcon("/button/common/select_menu_off.jpg"));
        btSearch.setBorderPainted(false);
        btSearch.setFocusable(false);
        btSearch.setPressedIcon(SystemInfo.getImageIcon("/button/common/select_menu_on.jpg"));
        btSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchbackPrevious(evt);
            }
        });

        lbCustomerNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbCustomerNo.setPreferredSize(new java.awt.Dimension(39, 20));

        lbProposal.setText("提案書名");

        lbstaff.setText("作成者");

        btClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btClose.setBorderPainted(false);
        btClose.setFocusable(false);
        btClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchResultScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(courseNoLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cusNameLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(customerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbCustomerNo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbValidDate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbValidDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lbstaff, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbstaff, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbProposal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProposal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noteScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(courseNoLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btOutExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lbCustomerNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(courseNoLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProposal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbProposal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(courseNoLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cusNameLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbstaff, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbstaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbValidDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(cmbValidDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(noteScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(btOutExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchResultScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void customerNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNoFocusGained
       
    }//GEN-LAST:event_customerNoFocusGained

    private void searchResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchResultMouseClicked

    }//GEN-LAST:event_searchResultMouseClicked

    private void btOutExcelbackPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOutExcelbackPrevious
        this.dtProposal.sort();
        outExcel();
    }//GEN-LAST:event_btOutExcelbackPrevious

    private void btRegistbackPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistbackPrevious
        if (searchResult.getCellEditor() != null) {
            searchResult.getCellEditor().stopCellEditing();
        }
        //入力チェック
        if (!this.checkInput()) {
            return;
        }
        try {
            registData();
            this.dtProposal.loadAll(SystemInfo.getConnection());
            btOutExcel.setEnabled(true);
        } catch (Exception ex) {
            Logger.getLogger(ProposalPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btRegistbackPrevious

    private void btSearchbackPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchbackPrevious
        SelectProductDialog spd = new SelectProductDialog(parentFrame, true, this);
        spd.setVisible(true);
    }//GEN-LAST:event_btSearchbackPrevious

    private void searchResultPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_searchResultPropertyChange
        int row = searchResult.getSelectedRow();
        if ( searchResult.getRowCount() <= 0 || row < 0) {
            return;
        }
        long priceValue =   0l;
        long numValue   =   0l;
        long total      =   0l;
        
        priceValue  = Long.parseLong( searchResult.getValueAt(row, 3).toString());
        numValue       = Long.parseLong( searchResult.getValueAt(row, 4).toString());
        if (this.dtProposal.get(row).getProductDivision().equals(2)) {
            total       = priceValue * numValue;
        } else if (this.dtProposal.get(row).getProductDivision().equals(5)) {
            total = priceValue;
        }
        
        searchResult.setValueAt(total, row, 5);
    }//GEN-LAST:event_searchResultPropertyChange

    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btCloseActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClose;
    private javax.swing.JButton btOutExcel;
    private javax.swing.JButton btRegist;
    private javax.swing.JButton btSearch;
    private javax.swing.ButtonGroup charge;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbValidDate;
    private javax.swing.JComboBox cmbstaff;
    private javax.swing.JLabel courseNoLabel2;
    private javax.swing.JLabel courseNoLabel6;
    private javax.swing.JLabel cusNameLabel5;
    private javax.swing.JLabel customerName;
    private javax.swing.JLabel lbCustomerNo;
    private javax.swing.JLabel lbProposal;
    private javax.swing.JLabel lbValidDate;
    private javax.swing.JLabel lbstaff;
    private com.geobeck.swing.JTextAreaEx note;
    private javax.swing.JScrollPane noteScrollPane;
    private com.geobeck.swing.JTableEx searchResult;
    private javax.swing.JScrollPane searchResultScrollPane;
    private com.geobeck.swing.JFormattedTextFieldEx txtProposal;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addSelectedProduct(Integer productDivision, Product selectedProduct) {
        if (searchResult.getCellEditor() != null) {
            searchResult.getCellEditor().stopCellEditing();
        }

        // add DataProposal
        DataProposalDetail dtProposalDetail = new DataProposalDetail();
        dtProposalDetail.setClassID(selectedProduct.getProductClass().getProductClassID());
        dtProposalDetail.setProductDivision(2);
        dtProposalDetail.setProductID(selectedProduct.getProductID());
        dtProposalDetail.setProductValue(selectedProduct.getPrice());
        dtProposalDetail.setProductNum(1);
        dtProposalDetail.setClassName(selectedProduct.getProductClass().getProductClassName());
        dtProposalDetail.setProductName(selectedProduct.getProductName());

        dtProposal.add(dtProposalDetail);
        //set table
        showDataTable();
    }

    @Override
    public void addSelectedCourse(Integer productDivision, Course selectedCourse) {
        if (searchResult.getCellEditor() != null) {
            searchResult.getCellEditor().stopCellEditing();
        }

         // add DataProposal
        DataProposalDetail dtProposalDetail = new DataProposalDetail();
        dtProposalDetail.setClassID(selectedCourse.getCourseClass().getCourseClassId());
        dtProposalDetail.setProductDivision(5);
        dtProposalDetail.setProductID(selectedCourse.getCourseId());
        dtProposalDetail.setProductValue(selectedCourse.getPrice());
        dtProposalDetail.setProductNum(selectedCourse.getNum());
        dtProposalDetail.setClassName(selectedCourse.getCourseClass().getCourseClassName());
        dtProposalDetail.setProductName(selectedCourse.getCourseName());

        dtProposal.add(dtProposalDetail);
        //set table
        showDataTable();
    }
    
     /**
     * 削除ボタンを取得する
     */
    private JButton getDeleteButton() {
        JButton deleteButton = new JButton();
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
        deleteButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
        deleteButton.setSize(48, 25);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRow();
            }
        });

        return deleteButton;
    }
    /**
     * 削除ボタンが押されたときの処理を行う。
     */
    private void deleteRow() {

        DefaultTableModel model = (DefaultTableModel) searchResult.getModel();
        int row = searchResult.getSelectedRow();
        if (row < 0) {
            return;
        }

        if (searchResult.getCellEditor() != null) {
            searchResult.getCellEditor().stopCellEditing();
        }

        //remove table
        model.removeRow(row);
        
        //remove list
        dtProposal.remove(row);
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btSearch);
        SystemInfo.addMouseCursorChange(btRegist);
        SystemInfo.addMouseCursorChange(btOutExcel);
        SystemInfo.addMouseCursorChange(btClose);
    }
    
    private void setKeyListener() {
        txtProposal.addKeyListener(SystemInfo.getMoveNextField());
        txtProposal.addFocusListener(SystemInfo.getSelectText());
        cmbstaff.addKeyListener(SystemInfo.getMoveNextField());
        note.addFocusListener(SystemInfo.getSelectText());
        cmbValidDate.addKeyListener(SystemInfo.getMoveNextField());
    }
    
    /**
	 * 提案書作成画面FocusTraversalPolicy
	 */
	private class ProposalFocusTraversalPolicy
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
			if (aComponent.equals(txtProposal))
			{
				return cmbstaff;
			}else if(aComponent.equals(cmbstaff))
                        {
                            return cmbValidDate;
                        }else if(aComponent.equals(cmbValidDate))
                        {
                            return note;
                        }
                        return txtProposal;
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
 			if (aComponent.equals(txtProposal))
			{
				return txtProposal;
			}else if(aComponent.equals(cmbValidDate))
                        {
                            return cmbstaff;
                        }else if(aComponent.equals(note))
                        {
                            return cmbValidDate;
                        }
                        return txtProposal;
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
			return note;
		}

		/**
		 * 
		 * @param focustomerCycleRoot 
		 * @return 
		 */
		public Component getFirstComponent(Container focustomerCycleRoot)
		{
			return txtProposal;
		}
                public Component getStartComponent()
		{
			return txtProposal;
		}
	}
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		searchResult.getColumnModel().getColumn(0).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(1).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(2).setPreferredWidth(240);
		searchResult.getColumnModel().getColumn(3).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(4).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(5).setPreferredWidth(80);
		searchResult.getColumnModel().getColumn(6).setPreferredWidth(50);
	}
}

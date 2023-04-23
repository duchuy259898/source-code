/*
 * BillsListPanel.java
 *
 * Created on 2006/10/18, 19:45
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.basicinfo.SelectSameNoData;
import com.geobeck.sosia.pos.master.company.MstShop;
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
	 * �ڋq�h�c
	 */
	private Integer customerID = null;
        
	/** Creates new form BillsListPanel */
	public BillsListPanel()
	{
		super();
		initComponents();
		addMouseCursorChange();
                //IVS_LVTu start edit 2015/10/23 New request #43755
		this.setSize(830, 700);
		closeButton.setVisible(false);
		this.setPath("���Z�Ǘ�");
		this.setTitle("���|�������");
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
        {null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "�����", "�`�[No.", "�ڋqNo.", "�ڋq��", "�����", "���W�S��", "������z", "����", "�J�[�h", "�d�q�}�l�[", "������", "���|�c", "���"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true
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
							"���|�����������Ă�낵���ł����H",
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
		//���㖾�ׁA�x���A�x������
		if(!this.deletePayment(con,billData ))
				return	false;
		//����w�b�_
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
		this.setPath("���Z�Ǘ�");
		this.setTitle("���|���ꗗ");
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
                "�����", "�`�[No.", "�ڋqNo.", "�ڋq��", "���W�S����", "���|���z", "���|�c"
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

        listTab.addTab("�����", billsScrollPane);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        collects.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "�����", "�`�[No.", "�ڋqNo.", "�ڋq��", "�����", "���W�S��", "������z", "����", "�J�[�h", "�d�q�}�l�[", "������", "���|�c", "���"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true
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

        listTab.addTab("�����", jScrollPane1);

        termFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        termFrom.setForeground(java.awt.Color.white);
        termFrom.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
        termFrom.setMaximumSize(new java.awt.Dimension(65, 20));
        termFrom.setMinimumSize(new java.awt.Dimension(65, 20));
        termFrom.setPreferredSize(new java.awt.Dimension(85, 20));

        lbTo.setText("  �`");

        termTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        termTo.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());

        shopLabel.setText("�X��");

        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });

        lbCondition.setText("�o�͏���");

        buttonGroup1.add(rdoSalesDate);
        rdoSalesDate.setSelected(true);
        rdoSalesDate.setText("�����");
        rdoSalesDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSalesDate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSalesDate.setOpaque(false);

        buttonGroup1.add(rdoPaymentDate);
        rdoPaymentDate.setText("�����");
        rdoPaymentDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoPaymentDate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoPaymentDate.setOpaque(false);

        lblNote.setText("��������Ō�������ꍇ�́u����ρv�^�u��I�����Ă��������B ");

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

        lblStaffType2.setText("�ڋqNo.");

        ckUnDate.setText("���Ԏw��Ȃ�");
        ckUnDate.setOpaque(false);
        ckUnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckUnDateActionPerformed(evt);
            }
        });

        lblPeriod.setText("�Ώۊ���");

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
		    SystemInfo.getLogger().log(Level.INFO, "���|���");
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
		SystemInfo.getLogger().log(Level.INFO, "���|���");
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
            SystemInfo.getLogger().log(Level.INFO, "�ڋq����");
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
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
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
	 * �ڋq�h�c�擾����B
	 * @return �ڋq�h�c
	 */
	public Integer getCustomerID()
	{
		return customerID;
	}

	/**
	 * �ڋq�h�c���Z�b�g����B
	 * @param customerID �ڋq�h�c
	 */
	public void setCustomerID(Integer customerID)
	{
		this.customerID = customerID;
	}
	
	
	/**
	 * ���|���ꗗ��ʗp��FocusTraversalPolicy���擾����B
	 * @return ���|���ꗗ��ʗp��FocusTraversalPolicy
	 */
	public BillsListFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	
	/**
	 * �������������s���B
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
	 * PDF�o�̓{�^���̕\����ύX����
	 */
	public void changePrintButtonVisible( boolean flg )
	{
		printButton.setVisible( flg );
	}
	
	/**
	 * ��������{�^���̎g�p�s�̐؂�ւ����s���B
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
	 * ���|���f�[�^��\������B
	 */
	private void showData()
	{
		this.showBillData();
		this.showCollectedBillData();
		this.changeCollectButtonEnabled();
	}
	
	/**
	 * ���|���f�[�^��\������B
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
	 * ����ϔ��|���f�[�^��\������B
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
                                        getCancelButton(cbd)
                                        //nhanvt end add 20141104 Request #31293
                };
		model.addRow(rowData);
	    }
	}
	
	/**
	 * ���|����������ֈڍs����B
	 */
	private void collectBill()
	{
	    int row = bills.getSelectedRow();
		
	    //�`�[���I������Ă���ꍇ
	    if (0 <= row) {
		//�`�[No.���擾
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
	 * ���|���ꗗ��ʗp��FocusTraversalPolicy
	 */
	private class BillsListFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent ��BillsListFocusTraversalPolicy�B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			return bills;
		}

		/**
		 * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{
			return bills;
		}

		/**
		 * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return bills;
		}

		/**
		 * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
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
		 * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
		 * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return bills;
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
		public Component getInitialComponent(Window window)
		{
			return bills;
		}
	}

	/**
	 * JTable�̗񕝂�����������B
	 */
	private void initTableColumnWidth()
	{
	    //��̕���ݒ肷��B
	    bills.getColumnModel().getColumn(0).setPreferredWidth(50);
	    bills.getColumnModel().getColumn(1).setPreferredWidth(40);
	    bills.getColumnModel().getColumn(2).setPreferredWidth(50);
	    bills.getColumnModel().getColumn(3).setPreferredWidth(80);
	    bills.getColumnModel().getColumn(4).setPreferredWidth(80);
	    bills.getColumnModel().getColumn(5).setPreferredWidth(50);
	    bills.getColumnModel().getColumn(6).setPreferredWidth(50);
	}
	
	/**
	 * �e�[�u����TableCellRenderer
	 */
	private class TableCellAlignRenderer extends DefaultTableCellRenderer
	{
		/** Creates a new instance of ReservationTableCellRenderer */
		public TableCellAlignRenderer()
		{
			super();
		}

		/**
		 * �e�[�u���Z�������_�����O��Ԃ��܂��B
		 * @param table JTable
		 * @param value �Z���Ɋ��蓖�Ă�l
		 * @param isSelected �Z�����I������Ă���ꍇ�� true
		 * @param hasFocus �t�H�[�J�X������ꍇ�� true
		 * @param row �s
		 * @param column ��
		 * @return �e�[�u���Z�������_�����O
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
	 * ���̃p�l�����J�����p�l���̔��|���ɒl��ݒ肷��B
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
	 * �I������Ă���`�[No.���擾����B
	 * @return �I������Ă���`�[No.
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
     * �ڋq���Z�b�g����B
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

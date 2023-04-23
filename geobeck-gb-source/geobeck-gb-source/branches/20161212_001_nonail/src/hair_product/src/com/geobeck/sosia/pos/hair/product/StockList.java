/*
 * RegisterCheckInVoucherPanel.java
 *
 * Created on 2008/09/17, 9:56
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.master.product.MstItems;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  ryu
 */
public class StockList extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private MstItemClasses mics = new MstItemClasses();
    
    /**
     * Creates new form RegisterCheckInVoucherPanel
     */
    public StockList() {
        this.setSize(834, 691);
        this.setPath("商品管理 >> 在庫一覧確認");
        this.setTitle("在庫一覧確認画面");
        initComponents();
        init();
        
        ftp = new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
    }
    
    static public void ShowDialog(Frame owner) {
        StockList dlg = new StockList();
        dlg.ShowCloseBtn();
        SwingUtil.openAnchorDialog(owner, true, dlg, "在庫一覧確認画面", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
    }
    
    private void ShowCloseBtn() {
        closeButton.setVisible(true);
    }
    
    /*
     *コンボボックスの内容初期化など
     */
    private void init() {
        int nCurrentShopID;
        
        // 全店舗をコンボボックスにセット
        SystemInfo.getGroup().addGroupDataToJComboBox(cboShop_ByShop, 2);
        cboShop_ByShop.setSelectedIndex(0);
        
        // 自店舗を初期選択
        nCurrentShopID = SystemInfo.getCurrentShop().getShopID();
        if (nCurrentShopID != 0) {
            for (int i = 0; i < cboShop_ByShop.getItemCount(); ++i) {
                if ( ((MstShop)cboShop_ByShop.getItemAt(i)).getShopID() == nCurrentShopID ) {
                    cboShop_ByShop.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        String strRowHeader[] = {"区分", "分類", "商品名", "合計"};
        
        DefaultTableModel tableModel = new DefaultTableModel(strRowHeader, 0) {
            public Class getColumnClass(int columnIndex) {
                if (columnIndex <= 2) {
                    return String.class;
                } else {
                    return Integer.class;
                }
            }
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        tblProductDetail_ByItem.setModel(tableModel);
        tblProductDetail_ByItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblProductDetail_ByItem.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(tblProductDetail_ByItem, SystemInfo.getTableHeaderRenderer());
        
        
        // テーブルのカラムサイズを調整
        initDetailColumn();
        
        // ボタンの上にマウスカーソルが乗った時にカーソルを変更する
        addMouseCursorChange();
        // enterキーで項目を移動する
        setKeyListener();
        
        //前回棚卸日をセットする
        setInventoryDate(SystemInfo.getConnection());
        
    }
    
    private void setInventoryDate(ConnectionWrapper con) {

        inventoryDateGyoumu.setText("");
        inventoryDateTenpan.setText("");

        try {

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      to_char(max(case when inventory_division = 2 then inventory_date end), 'yyyy年MM月dd日') as inventoryDateGyoumu");
            sql.append("     ,to_char(max(case when inventory_division = 1 then inventory_date end), 'yyyy年MM月dd日') as inventoryDateTenpan");
            sql.append(" from");
            sql.append("     data_inventory");
            sql.append(" where");
            sql.append("         delete_date  is null");
            sql.append("     and fixed = 1");
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(((MstShop)cboShop_ByShop.getSelectedItem()).getShopID()));
            sql.append("     and inventory_date < " + SQLUtil.convertForSQLDateOnly(Calendar.getInstance().getTime()));

            ResultSetWrapper rs = con.executeQuery(sql.toString());

            while (rs.next()) {
                inventoryDateGyoumu.setText(rs.getString("inventoryDateGyoumu"));
                inventoryDateTenpan.setText(rs.getString("inventoryDateTenpan"));
            }

            rs.close();

        } catch (Exception e) {
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        btngItemDiv_ByShop = new javax.swing.ButtonGroup();
        btngItemDiv_ByItem = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelForShopSale = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductDetail_ByShop = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        cboShop_ByShop = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        rdoItemDiv_Operation_ByShop = new javax.swing.JRadioButton();
        rdoItemDiv_ShopSale_ByShop = new javax.swing.JRadioButton();
        rdoItemDiv_All_ByShop = new javax.swing.JRadioButton();
        displayButton_ByShop = new javax.swing.JButton();

        try
        {
            ConnectionWrapper	con	=	SystemInfo.getConnection();

            mics.load(con);

            con.close();

            mics.add(0, new MstItemClass());
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        cboItemCategory_ByShop = new JComboBox(new DefaultComboBoxModel(mics.toArray())
            {
                public void setSelectedItem(Object o)
                {
                    super.setSelectedItem(o);
                }
            });
            jPanel2 = new javax.swing.JPanel();
            jLabel9 = new javax.swing.JLabel();
            jLabel10 = new javax.swing.JLabel();
            inventoryDateGyoumu = new javax.swing.JLabel();
            jLabel11 = new javax.swing.JLabel();
            inventoryDateTenpan = new javax.swing.JLabel();
            panelForBuiness = new javax.swing.JPanel();
            cboItem_ByItem = new javax.swing.JComboBox();
            jScrollPane4 = new javax.swing.JScrollPane();
            tblProductDetail_ByItem = new javax.swing.JTable();
            jLabel6 = new javax.swing.JLabel();
            jLabel7 = new javax.swing.JLabel();

            try
            {
                ConnectionWrapper	con	=	SystemInfo.getConnection();

                mics.load(con);

                con.close();

                mics.add(0, new MstItemClass());
            }
            catch(SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            cboItemCategory_ByItem = new JComboBox(new DefaultComboBoxModel(mics.toArray())
                {
                    public void setSelectedItem(Object o)
                    {
                        super.setSelectedItem(o);
                    }
                });
                jLabel8 = new javax.swing.JLabel();
                rdoItemDiv_All_ByItem = new javax.swing.JRadioButton();
                rdoItemDiv_ShopSale_ByItem = new javax.swing.JRadioButton();
                rdoItemDiv_Operation_ByItem = new javax.swing.JRadioButton();
                displayButton_ByItem = new javax.swing.JButton();
                jPanel1 = new javax.swing.JPanel();
                closeButton = new javax.swing.JButton();
                closeButton.setVisible(false);

                setFocusCycleRoot(true);
                setFocusTraversalPolicy(getFocusTraversalPolicy());
                panelForShopSale.setOpaque(false);
                jScrollPane2.setBorder(null);
                tblProductDetail_ByShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                tblProductDetail_ByShop.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "区分", "分類", "商品名", "仕入価格", "販売価格", "在庫数"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                tblProductDetail_ByShop.setSelectionBackground(new java.awt.Color(255, 210, 142));
                tblProductDetail_ByShop.setSelectionForeground(new java.awt.Color(0, 0, 0));
                tblProductDetail_ByShop.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tblProductDetail_ByShop.getTableHeader().setReorderingAllowed(false);
                SwingUtil.setJTableHeaderRenderer(tblProductDetail_ByShop, SystemInfo.getTableHeaderRenderer());
                tblProductDetail_ByShop.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                SelectTableCellRenderer.setSelectTableCellRenderer(tblProductDetail_ByShop);

                jScrollPane2.setViewportView(tblProductDetail_ByShop);

                jLabel5.setText("\u5e97\u8217");

                cboShop_ByShop.setMaximumRowCount(15);
                cboShop_ByShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                cboShop_ByShop.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cboShop_ByShopActionPerformed(evt);
                    }
                });

                jLabel3.setText("\u5546\u54c1\u5206\u985e");

                jLabel4.setText("\u5546\u54c1\u533a\u5206");

                btngItemDiv_ByShop.add(rdoItemDiv_Operation_ByShop);
                rdoItemDiv_Operation_ByShop.setText("\u696d\u52d9\u7528");
                rdoItemDiv_Operation_ByShop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rdoItemDiv_Operation_ByShop.setMargin(new java.awt.Insets(0, 0, 0, 0));
                rdoItemDiv_Operation_ByShop.setOpaque(false);

                btngItemDiv_ByShop.add(rdoItemDiv_ShopSale_ByShop);
                rdoItemDiv_ShopSale_ByShop.setText("\u5e97\u8ca9\u7528");
                rdoItemDiv_ShopSale_ByShop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rdoItemDiv_ShopSale_ByShop.setMargin(new java.awt.Insets(0, 0, 0, 0));
                rdoItemDiv_ShopSale_ByShop.setOpaque(false);

                btngItemDiv_ByShop.add(rdoItemDiv_All_ByShop);
                rdoItemDiv_All_ByShop.setSelected(true);
                rdoItemDiv_All_ByShop.setText("\u5168\u3066");
                rdoItemDiv_All_ByShop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rdoItemDiv_All_ByShop.setMargin(new java.awt.Insets(0, 0, 0, 0));
                rdoItemDiv_All_ByShop.setOpaque(false);

                displayButton_ByShop.setIcon(SystemInfo.getImageIcon( "/button/common/show_off.jpg" ));
                displayButton_ByShop.setBorder(null);
                displayButton_ByShop.setIconTextGap(0);
                displayButton_ByShop.setMargin(new java.awt.Insets(0, 0, 0, 0));
                displayButton_ByShop.setPressedIcon(SystemInfo.getImageIcon( "/button/common/show_on.jpg" ));
                displayButton_ByShop.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        displayButton_ByShopActionPerformed(evt);
                    }
                });

                cboItemCategory_ByShop.setMaximumRowCount(15);
                cboItemCategory_ByShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                jPanel2.setLayout(null);

                jPanel2.setOpaque(false);
                jLabel9.setForeground(java.awt.Color.red);
                jLabel9.setText("\u3010\u524d\u56de\u68da\u5378\u65e5\u3011");
                jPanel2.add(jLabel9);
                jLabel9.setBounds(10, 0, 72, 13);

                jLabel10.setForeground(java.awt.Color.red);
                jLabel10.setText("\u696d\u52d9\u7528 \uff1a");
                jPanel2.add(jLabel10);
                jLabel10.setBounds(30, 20, 46, 13);

                inventoryDateGyoumu.setForeground(java.awt.Color.red);
                inventoryDateGyoumu.setText("2008\u5e7412\u670831\u65e5");
                jPanel2.add(inventoryDateGyoumu);
                inventoryDateGyoumu.setBounds(80, 20, 84, 13);

                jLabel11.setForeground(java.awt.Color.red);
                jLabel11.setText("\u5e97\u8ca9\u7528 \uff1a");
                jPanel2.add(jLabel11);
                jLabel11.setBounds(30, 40, 46, 13);

                inventoryDateTenpan.setForeground(java.awt.Color.red);
                inventoryDateTenpan.setText("2008\u5e7412\u670831\u65e5");
                jPanel2.add(inventoryDateTenpan);
                inventoryDateTenpan.setBounds(80, 40, 84, 13);

                javax.swing.GroupLayout panelForShopSaleLayout = new javax.swing.GroupLayout(panelForShopSale);
                panelForShopSale.setLayout(panelForShopSaleLayout);
                panelForShopSaleLayout.setHorizontalGroup(
                    panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelForShopSaleLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelForShopSaleLayout.createSequentialGroup()
                                .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(23, 23, 23)
                                .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboShop_ByShop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(panelForShopSaleLayout.createSequentialGroup()
                                        .addComponent(rdoItemDiv_All_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoItemDiv_ShopSale_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoItemDiv_Operation_ByShop))
                                    .addComponent(cboItemCategory_ByShop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(83, 83, 83)
                                .addComponent(displayButton_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE))
                        .addContainerGap())
                );
                panelForShopSaleLayout.setVerticalGroup(
                    panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelForShopSaleLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelForShopSaleLayout.createSequentialGroup()
                                .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboShop_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboItemCategory_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rdoItemDiv_Operation_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoItemDiv_ShopSale_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoItemDiv_All_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9))
                            .addGroup(panelForShopSaleLayout.createSequentialGroup()
                                .addGroup(panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(displayButton_ByShop, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jTabbedPane1.addTab("\u3000\u5e97\u8217\u5225\u3000", panelForShopSale);

                panelForBuiness.setOpaque(false);
                cboItem_ByItem.setMaximumRowCount(15);
                cboItem_ByItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                jScrollPane4.setBorder(null);
                tblProductDetail_ByItem.setSelectionBackground(new java.awt.Color(255, 210, 142));
                tblProductDetail_ByItem.setSelectionForeground(new java.awt.Color(0, 0, 0));
                tblProductDetail_ByItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tblProductDetail_ByItem.getTableHeader().setReorderingAllowed(false);
                SwingUtil.setJTableHeaderRenderer(tblProductDetail_ByItem, SystemInfo.getTableHeaderRenderer());
                tblProductDetail_ByItem.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                SelectTableCellRenderer.setSelectTableCellRenderer(tblProductDetail_ByItem);

                jScrollPane4.setViewportView(tblProductDetail_ByItem);

                jLabel6.setText("\u5546\u54c1\u540d");

                jLabel7.setText("\u5546\u54c1\u5206\u985e");

                cboItemCategory_ByItem.setMaximumRowCount(15);
                cboItemCategory_ByItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                cboItemCategory_ByItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cboItemCategory_ByItemActionPerformed(evt);
                    }
                });

                jLabel8.setText("\u5546\u54c1\u533a\u5206");

                btngItemDiv_ByItem.add(rdoItemDiv_All_ByItem);
                rdoItemDiv_All_ByItem.setSelected(true);
                rdoItemDiv_All_ByItem.setText("\u5168\u3066");
                rdoItemDiv_All_ByItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rdoItemDiv_All_ByItem.setMargin(new java.awt.Insets(0, 0, 0, 0));
                rdoItemDiv_All_ByItem.setOpaque(false);

                btngItemDiv_ByItem.add(rdoItemDiv_ShopSale_ByItem);
                rdoItemDiv_ShopSale_ByItem.setText("\u5e97\u8ca9\u7528");
                rdoItemDiv_ShopSale_ByItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rdoItemDiv_ShopSale_ByItem.setMargin(new java.awt.Insets(0, 0, 0, 0));
                rdoItemDiv_ShopSale_ByItem.setOpaque(false);

                btngItemDiv_ByItem.add(rdoItemDiv_Operation_ByItem);
                rdoItemDiv_Operation_ByItem.setText("\u696d\u52d9\u7528");
                rdoItemDiv_Operation_ByItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rdoItemDiv_Operation_ByItem.setMargin(new java.awt.Insets(0, 0, 0, 0));
                rdoItemDiv_Operation_ByItem.setOpaque(false);

                displayButton_ByItem.setIcon(SystemInfo.getImageIcon( "/button/common/show_off.jpg" ));
                displayButton_ByItem.setBorder(null);
                displayButton_ByItem.setIconTextGap(0);
                displayButton_ByItem.setMargin(new java.awt.Insets(0, 0, 0, 0));
                displayButton_ByItem.setPressedIcon(SystemInfo.getImageIcon( "/button/common/show_on.jpg" ));
                displayButton_ByItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        displayButton_ByItemActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout panelForBuinessLayout = new javax.swing.GroupLayout(panelForBuiness);
                panelForBuiness.setLayout(panelForBuinessLayout);
                panelForBuinessLayout.setHorizontalGroup(
                    panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelForBuinessLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                            .addGroup(panelForBuinessLayout.createSequentialGroup()
                                .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelForBuinessLayout.createSequentialGroup()
                                        .addComponent(rdoItemDiv_All_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoItemDiv_ShopSale_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoItemDiv_Operation_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(66, 66, 66)
                                        .addComponent(displayButton_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelForBuinessLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboItemCategory_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cboItem_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                );
                panelForBuinessLayout.setVerticalGroup(
                    panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelForBuinessLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelForBuinessLayout.createSequentialGroup()
                                .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboItemCategory_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                                    .addComponent(cboItem_ByItem, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoItemDiv_All_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoItemDiv_ShopSale_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoItemDiv_Operation_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9))
                            .addGroup(panelForBuinessLayout.createSequentialGroup()
                                .addComponent(displayButton_ByItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jTabbedPane1.addTab("\u3000\u5546\u54c1\u5225\u3000", panelForBuiness);

                jTabbedPane1.getAccessibleContext().setAccessibleName("");

                jPanel1.setOpaque(false);
                closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
                closeButton.setFocusable(false);
                closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
                closeButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        closeButtonbackPrevious(evt);
                    }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(44, Short.MAX_VALUE)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                        .addContainerGap())
                );
            }// </editor-fold>//GEN-END:initComponents
    
	private void displayButton_ByItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayButton_ByItemActionPerformed
	{//GEN-HEADEREND:event_displayButton_ByItemActionPerformed

            displayButton_ByItem.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                this.displayByItem();

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            
	}//GEN-LAST:event_displayButton_ByItemActionPerformed
        
        private void displayByItem() {

            DefaultTableModel model = (DefaultTableModel)tblProductDetail_ByItem.getModel();
            ConnectionWrapper dbConnection = SystemInfo.getConnection();
            ResultSetWrapper rs;
            Integer intItemClassID;
            Integer intItemID;
            Integer intItemDiv;
            //ArrayList<Integer> arrShopID = new ArrayList<Integer>();
            //ArrayList<String> arrShopName = new ArrayList<String>();
            MstItemClass itemclass;
            MstItem item;
            Integer intRowSum;
            Date today = new Date();
            
            model.setRowCount(0);
            model.setColumnCount(4);
            
            itemclass = (MstItemClass)cboItemCategory_ByItem.getSelectedItem();
            if (itemclass != null) {
                intItemClassID = itemclass.getItemClassID();
            } else {
                intItemClassID = null;
            }
            
            item = (MstItem)cboItem_ByItem.getSelectedItem();
            if (item != null) {
                intItemID = item.getItemID();
            } else {
                intItemID = null;
            }
            
            
            if (rdoItemDiv_ShopSale_ByItem.isSelected()) {
                intItemDiv = 1;
            } else if (rdoItemDiv_Operation_ByItem.isSelected()) {
                intItemDiv = 2;
            } else {
                intItemDiv = null;
            }
            
            try {
                //rs = dbConnection.executeQuery(this.getSQLSelectAllShop());
                //
                //while (rs.next()) {
                //    arrShopID.add(rs.getInt("shop_id"));
                //    arrShopName.add(rs.getString("shop_name"));
                //}
                //rs.close();
                
                ArrayList<MstShop>  shops = SystemInfo.getGroup().getShops();
                
//			rs = dbConnection.executeQuery(this.getSQLSelectStockList(arrShopID.get(0), intItemClassID, intItemID, intItemDiv));
                rs = dbConnection.executeQuery(this.getSQLSelectItemInfo(intItemClassID, intItemID, intItemDiv, null));
                
                while (rs.next()) {
                    model.addRow(
                            new Object[] {
                        (rs.getInt("inventory_division") == 1) ? "店販用" : "業務用",
                        rs.getString("item_class_contracted_name"),
                        rs.getString("item_name"),
                        //rs.getLong("cost_price"),
                    }
                    );
                }
                
                rs.close();
                
                
                for (int i = 0; i < shops.size(); ++i) {
                    MstShop ms = shops.get(i);
                    rs = dbConnection.executeQuery(this.getSQLSelectItemInfo(intItemClassID, intItemID, intItemDiv, null));
                    
                    ArrayList<Integer> arrStockCount = new ArrayList<Integer>();
                    
                    while (rs.next()) {
                        arrStockCount.add(this.getItemStock( ms.getShopID(), rs.getInt("item_id"), rs.getInt("inventory_division")));
                    }
                    
                    model.addColumn(ms.getShopName(), arrStockCount.toArray());
                    
                    rs.close();
                }
                
                for (int row = 0; row < this.tblProductDetail_ByItem.getRowCount(); ++row) {
                    intRowSum = 0;
                    
                    for (int col = 4; col < shops.size() + 4; ++col) {
                        intRowSum += (Integer)this.tblProductDetail_ByItem.getValueAt(row, col);
                    }
                    
                    this.tblProductDetail_ByItem.setValueAt(intRowSum, row, 3);
                }
                
                tblProductDetail_ByItem.getColumnModel().getColumn(0).setPreferredWidth(50);
                tblProductDetail_ByItem.getColumnModel().getColumn(1).setPreferredWidth(70);
                tblProductDetail_ByItem.getColumnModel().getColumn(2).setPreferredWidth(243);
                //tblProductDetail_ByItem.getColumnModel().getColumn(3).setPreferredWidth(70);
                
                for (int col = 3; col < shops.size() + 3; ++col) {
                    tblProductDetail_ByItem.getColumnModel().getColumn(col).setPreferredWidth(60);
                }
                
                SwingUtil.setJTableHeaderRenderer(tblProductDetail_ByItem, SystemInfo.getTableHeaderRenderer());
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
	private void cboItemCategory_ByItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cboItemCategory_ByItemActionPerformed
	{//GEN-HEADEREND:event_cboItemCategory_ByItemActionPerformed
            MstItemClass itemclass = (MstItemClass)cboItemCategory_ByItem.getSelectedItem();
            MstItems items;
            
            try {
                if (cboItemCategory_ByItem.getSelectedIndex() > 0) {
                    items = new MstItems(((MstItemClass)cboItemCategory_ByItem.getSelectedItem()).getItemClassID());
                } else {
                    items = new MstItems();
                }
                
                items.loadAll(SystemInfo.getConnection());
                
                cboItem_ByItem.removeAllItems();
                cboItem_ByItem.addItem(null);
                
                for (int i = 0; i < items.size(); ++i) {
                    cboItem_ByItem.addItem(items.get(i));
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
	}//GEN-LAST:event_cboItemCategory_ByItemActionPerformed
                        
	private void displayButton_ByShopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayButton_ByShopActionPerformed
	{//GEN-HEADEREND:event_displayButton_ByShopActionPerformed

            displayButton_ByShop.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                this.displayByShop();

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            
	}//GEN-LAST:event_displayButton_ByShopActionPerformed
        
        
        private void displayByShop() {

            DefaultTableModel model = (DefaultTableModel)tblProductDetail_ByShop.getModel();
            ConnectionWrapper dbConnection = SystemInfo.getConnection();
            ResultSetWrapper rsItemInfo;
            Integer intItemClassID;
            Integer intShopID;
            Integer intItemDiv;
            Date today = new Date();
            
            model.setRowCount(0);
            
            intShopID = ( (MstShop)cboShop_ByShop.getSelectedItem() ).getShopID();
            intItemClassID  =  ( (MstItemClass)cboItemCategory_ByShop.getSelectedItem() ).getItemClassID();
            
            if (rdoItemDiv_ShopSale_ByShop.isSelected()) {
                intItemDiv = 1;
            } else if (rdoItemDiv_Operation_ByShop.isSelected()) {
                intItemDiv = 2;
            } else {
                intItemDiv = null;
            }
            
            try {
                
                MstShop shop = (MstShop) cboShop_ByShop.getSelectedItem();
                rsItemInfo = dbConnection.executeQuery(this.getSQLSelectItemInfo(intItemClassID, null, intItemDiv, shop.getShopID()));
                
                while(rsItemInfo.next()) {
                    model.addRow(
                            new Object[] {
                        (rsItemInfo.getInt("inventory_division") == 1) ? "店販用" : "業務用",
                        rsItemInfo.getString("item_class_name"),
                        rsItemInfo.getString("item_name"),
                        rsItemInfo.getLong("cost_price"),
                        rsItemInfo.getLong("item_price"),
                        rsItemInfo.getLong("stockcount")
                    }
                    );
                }
                rsItemInfo.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        
        
        
        
	private void closeButtonbackPrevious(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonbackPrevious
	{//GEN-HEADEREND:event_closeButtonbackPrevious
            if(this.isDialog()) {
                ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
            } else {
                this.setVisible(false);
            }
	}//GEN-LAST:event_closeButtonbackPrevious
        
	private void cboShop_ByShopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cboShop_ByShopActionPerformed
	{//GEN-HEADEREND:event_cboShop_ByShopActionPerformed

            //前回棚卸日をセットする
            setInventoryDate(SystemInfo.getConnection());
            
	}//GEN-LAST:event_cboShop_ByShopActionPerformed
        
        private LocalFocusTraversalPolicy   ftp;
        /**
         * FocusTraversalPolicyを取得する。
         * @return FocusTraversalPolicy
         */
        public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
            return  ftp;
        }
        
        private String getSQLSelectAllShop() {
            StringBuffer strSQL = new StringBuffer();
            
            strSQL.append(" select");
            strSQL.append(" 	shop_id");
            strSQL.append(" ,	shop_name");
            strSQL.append(" from mst_shop");
            
            strSQL.append(" where");
            strSQL.append(" 	delete_date is NULL");
            
            strSQL.append(" order by");
            strSQL.append(" 	shop_id, shop_id asc");
            
            return strSQL.toString();
        }
        
        
        private String getSQLSelectItemInfo(Integer intItemClassID, Integer intItemID, Integer intItemDiv, Integer shopId) {
            StringBuffer strSQL = new StringBuffer();
            
            strSQL.append(" select");
            strSQL.append("   base_inv.inventory_division");
            strSQL.append(" , base.item_class_id ");
            strSQL.append(" , base.class_seq ");
            strSQL.append(" , base.item_class_name");
            strSQL.append(" , base.item_class_contracted_name ");
            strSQL.append(" , base.item_seq ");
            strSQL.append(" , base.item_id ");
            strSQL.append(" , base.item_name ");
            strSQL.append(" , coalesce(base.price, 0) as item_price");
            strSQL.append(" , coalesce(base.cost_price, 0) as cost_price ");
            
            if( shopId != null ){
                strSQL.append(" , get_item_stock(");
                strSQL.append("	" + SQLUtil.convertForSQL(shopId));
                strSQL.append("	, base.item_id, base_inv.inventory_division");
//---- 2013/04/23 GB MOD START
                strSQL.append("	, to_date(to_char(current_timestamp,'yyyy-mm-dd'), 'yyyy-mm-dd')");
//                strSQL.append("	, current_date");
//---- 2013/04/23 GB MOD END
                strSQL.append(" ) as stockcount");
            }
            
            strSQL.append(" from ");
            strSQL.append(" ( ");
            strSQL.append("   select distinct ");
            strSQL.append("     m_itc.item_class_id ");
            strSQL.append("   , m_itc.display_seq as class_seq ");
            strSQL.append("   , m_itc.item_class_name");
            strSQL.append("   , m_itc.item_class_contracted_name");
            strSQL.append("   , m_it.item_id ");
            strSQL.append("   , m_it.display_seq as item_seq ");
            strSQL.append("   , m_it.item_name ");
            strSQL.append("   , m_it.price");
            strSQL.append("   , m_si.cost_price ");
            strSQL.append("   from  mst_supplier_item m_si");
            
            strSQL.append("   left join mst_item m_it ");
            strSQL.append("   on m_it.item_id = m_si.item_id ");
            
            strSQL.append("   left join mst_item_class m_itc ");
            strSQL.append("   on m_itc.item_class_id = m_it.item_class_id ");
            
            strSQL.append( "  ,     mst_use_product     m_up \n" );
            
            if( shopId == null ){
                strSQL.append("   where");
            }else{
                strSQL.append( "  where m_up.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n" );
                strSQL.append( "  and   m_up.product_division = 2 \n" );
                strSQL.append( "  and   m_up.product_id = m_si.item_id \n" );
                strSQL.append("   and  ");
            }
            
            strSQL.append("       m_it.delete_date is null");
            strSQL.append("   and m_itc.delete_date is null ");
            strSQL.append("   and m_si.delete_date is null");
            strSQL.append(" ) base ");
            
            strSQL.append(" left join (");
            strSQL.append(" 	select 1 as inventory_division");
            strSQL.append(" union	select 2 as inventory_division");
            strSQL.append(" ) as base_inv on TRUE");
            
            strSQL.append(" where");
            strSQL.append("		base_inv.inventory_division is not NULL");
            
            if (intItemClassID != null) {
                strSQL.append(" and	base.item_class_id  = " + SQLUtil.convertForSQL(intItemClassID));
            }
            
            if (intItemID != null) {
                strSQL.append(" and	base.item_id  = " + SQLUtil.convertForSQL(intItemID));
            }
            
            if (intItemDiv != null) {
                strSQL.append(" and	base_inv.inventory_division = " + SQLUtil.convertForSQL(intItemDiv));
            }
            
            
            strSQL.append(" order by");
            strSQL.append("   base_inv.inventory_division");
            strSQL.append(" , base.class_seq, base.item_class_id ");
            strSQL.append(" , base.item_seq, base.item_id ");
            
            
            return strSQL.toString();
        }
        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btngItemDiv_ByItem;
    private javax.swing.ButtonGroup btngItemDiv_ByShop;
    private javax.swing.JComboBox cboItemCategory_ByItem;
    private javax.swing.JComboBox cboItemCategory_ByShop;
    private javax.swing.JComboBox cboItem_ByItem;
    private javax.swing.JComboBox cboShop_ByShop;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton displayButton_ByItem;
    private javax.swing.JButton displayButton_ByShop;
    private javax.swing.JLabel inventoryDateGyoumu;
    private javax.swing.JLabel inventoryDateTenpan;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelForBuiness;
    private javax.swing.JPanel panelForShopSale;
    private javax.swing.JRadioButton rdoItemDiv_All_ByItem;
    private javax.swing.JRadioButton rdoItemDiv_All_ByShop;
    private javax.swing.JRadioButton rdoItemDiv_Operation_ByItem;
    private javax.swing.JRadioButton rdoItemDiv_Operation_ByShop;
    private javax.swing.JRadioButton rdoItemDiv_ShopSale_ByItem;
    private javax.swing.JRadioButton rdoItemDiv_ShopSale_ByShop;
    private javax.swing.JTable tblProductDetail_ByItem;
    private javax.swing.JTable tblProductDetail_ByShop;
    // End of variables declaration//GEN-END:variables
    
    
    
    
    /**
     *
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(closeButton);
        SystemInfo.addMouseCursorChange(displayButton_ByItem);
        SystemInfo.addMouseCursorChange(displayButton_ByShop);
    }
    
    /**
     *
     */
    private void setKeyListener() {
        cboShop_ByShop.addKeyListener(SystemInfo.getMoveNextField());
        cboShop_ByShop.addFocusListener(SystemInfo.getSelectText());
        cboItemCategory_ByItem.addKeyListener(SystemInfo.getMoveNextField());
        cboItemCategory_ByItem.addFocusListener(SystemInfo.getSelectText());
        cboItemCategory_ByShop.addKeyListener(SystemInfo.getMoveNextField());
        cboItemCategory_ByShop.addFocusListener(SystemInfo.getSelectText());
        cboItem_ByItem.addKeyListener(SystemInfo.getMoveNextField());
        cboItem_ByItem.addFocusListener(SystemInfo.getSelectText());
    }
    
    /**
     * 明細の列を初期化する。
     */
    private void initDetailColumn() {
        //列の幅を設定する。
        tblProductDetail_ByShop.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblProductDetail_ByShop.getColumnModel().getColumn(1).setPreferredWidth(110);
        tblProductDetail_ByShop.getColumnModel().getColumn(2).setPreferredWidth(263);
        tblProductDetail_ByShop.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblProductDetail_ByShop.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblProductDetail_ByShop.getColumnModel().getColumn(5).setPreferredWidth(80);
    }
    
    
    private Integer getItemStock(Integer intShopID, Integer intItemID, Integer intItemDiv) {
        ConnectionWrapper dbConnection = SystemInfo.getConnection();
        ResultSetWrapper rs;
        StringBuffer strSQL;
        Integer intStockCount;
        
        intStockCount = 0;
        
        try {
            strSQL = new StringBuffer();
            
            strSQL.append(" select get_item_stock(");
            strSQL.append("	  " + SQLUtil.convertForSQL(intShopID));
            strSQL.append("	, " + SQLUtil.convertForSQL(intItemID));
            strSQL.append("	, " + SQLUtil.convertForSQL(intItemDiv));
//---- 2013/04/23 GB MOD START
            strSQL.append("	, to_date(to_char(current_timestamp,'yyyy-mm-dd'), 'yyyy-mm-dd')");
//            strSQL.append("	, current_date");
//---- 2013/04/23 GB MOD END
            strSQL.append(" ) as stockcount");
            
            rs = dbConnection.executeQuery(strSQL.toString());
            
            if (rs.next()) {
                intStockCount = rs.getInt("stockcount");
            }
            
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return intStockCount;
    }
    
    private class LocalFocusTraversalPolicy extends FocusTraversalPolicy {
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy() {
            controls.add(cboShop_ByShop);
            controls.add(cboItemCategory_ByShop);
            controls.add(rdoItemDiv_All_ByShop);
            controls.add(rdoItemDiv_ShopSale_ByShop);
            controls.add(rdoItemDiv_Operation_ByShop);
            controls.add(displayButton_ByShop);
            controls.add(tblProductDetail_ByShop);
            controls.add(cboItemCategory_ByItem);
            controls.add(cboItem_ByItem);
            controls.add(rdoItemDiv_All_ByItem);
            controls.add(rdoItemDiv_ShopSale_ByItem);
            controls.add(rdoItemDiv_Operation_ByItem);
            controls.add(displayButton_ByItem);
            controls.add(tblProductDetail_ByItem);
            controls.add(closeButton);
            
            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(cboShop_ByShop);
        };
        
        
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
         */
        public Component getComponentAfter(Container aContainer, Component aComponent) {
            boolean find = false;
            for(Component co : controls){
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
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
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for( int ii = controls.size(); ii>0; ii-- ){
                Component co = controls.get(ii-1);
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * トラバーサルサイクルの最初の Component を返します。
         */
        public Component getFirstComponent(Container aContainer) {
            return getDefaultComponent(aContainer);
        }
        
        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer) {
            return getComponentBefore(aContainer, controls.get(0));
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer) {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
        
        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
         */
        public Component getInitialComponent(Window window) {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
    }
}




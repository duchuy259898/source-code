/*
 * MoveStockPanel.java
 *
 * Created on 2008/10/20
 */

package com.geobeck.sosia.pos.hair.product;
// <editor-fold defaultstate="collapsed" desc="インポート文">
import com.geobeck.sosia.pos.hair.data.product.DataSlipShip;
import com.geobeck.sosia.pos.hair.data.product.DataSlipShipDetail;
import com.geobeck.sosia.pos.basicinfo.commodity.EditabeTableCellRenderer;
import com.geobeck.sosia.pos.hair.product.beans.MoveStockReportBean;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sosia.pos.master.commodity.MstSuppliers;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.master.product.MstUseProduct;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.IntegerCellEditor;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import com.geobeck.swing.JTableEx;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;
import jp.co.flatsoft.fscomponent.FSCalenderCombo;

// </editor-fold>

/**
 *
 * @author  trino
 */
/** 商品店舗間移動パネル */
public class MoveStockPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private enum SearchMode { NONE, SLIP_SHIP };
    private MstItemClasses classes = new MstItemClasses();
    private MstSuppliers mss = new MstSuppliers();
    private MstUseProduct mup = new MstUseProduct();
    //IVS_TMTrong start add 2015/09/28 New request #42933
    public List<MoveStockReportBean> lstMoveStockReportBean= new ArrayList<MoveStockReportBean>();
    //IVS_TMTrong end add 2015/09/28 New request #42933
    
    /** 削除ボタンOnアイコン */
    ImageIcon iconTrashOn =
            SystemInfo.getImageIcon("/button/common/trash_on.jpg");
    /** 削除ボタンOffアイコン */
    ImageIcon iconTrashOff =
            SystemInfo.getImageIcon("/button/common/trash_off.jpg");
    
    /** コンストラクタ */
    public MoveStockPanel() {
        this.setSize(834, 691);
        initComponents();
        this.setPath("商品管理");
        this.setTitle("店舗間移動");
        init();
        shop1.requestFocusInWindow();
    }
    
    /* コンボボックスの内容初期化など */
    private void init() {

        SystemInfo.initGroupShopComponents(shop1, 2);
        SystemInfo.initGroupShopComponents(shop2, 2);
        SystemInfo.initGroupShopComponents(shop3, 2);
        shopOther.addItem(null);
        SystemInfo.getGroup().addGroupDataToJComboBox(shopOther, 2);
        SystemInfo.getGroup().addGroupDataToJComboBox(destination, 2);
        // スタッフ選択コンボボックスの先頭に空のスタッフを設定
        confirmerName1.addItem(new MstStaff());
        confirmerName2.addItem(new MstStaff());
        dispatcherName2.addItem(new MstStaff());
        dispatcherName3.addItem(new MstStaff());
        // スタッフ選択コンボボックスのスタッフを追加
        SystemInfo.initStaffComponent(confirmerName1);
        SystemInfo.initStaffComponent(confirmerName2);
        SystemInfo.initStaffComponent(dispatcherName2);
        SystemInfo.initStaffComponent(dispatcherName3);
        
        initItemClass();
        
        initDetailList(tblDetail3);
        setKeyListener();
        setFocusListener();
        addMouseCursorChange();
        initTblDetail1();
        initTblDetail2();
        initTblDetail3();
        
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
            sql.append("     and shop_id = " + SQLUtil.convertForSQL(((MstShop)shop3.getSelectedItem()).getShopID()));
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
    
    /** Enter押下時フォーカス移動の設定 */
    private void setKeyListener(){
        shop1.addKeyListener(SystemInfo.getMoveNextField());
        confirmDate.addKeyListener(SystemInfo.getMoveNextField());
        rdoDivisionAll1.addKeyListener(new KeyListenerForRadioButton(
                null, rdoDivisionSale1,
                confirmDate, rdoDirectionIn1));
        rdoDivisionSale1.addKeyListener(new KeyListenerForRadioButton(
                rdoDivisionAll1, rdoDivisionWork1,
                confirmDate, rdoDirectionIn1));
        rdoDivisionWork1.addKeyListener(new KeyListenerForRadioButton(
                rdoDivisionSale1, null,
                confirmDate, rdoDirectionIn1));
        rdoDirectionIn1.addKeyListener(new KeyListenerForRadioButton(
                null, rdoDirectionOut1,
                rdoDivisionWork1, btnShow1));
        rdoDirectionOut1.addKeyListener(new KeyListenerForRadioButton(
                rdoDirectionIn1, null,
                rdoDivisionWork1, btnShow1));
        btnShow1.addKeyListener(SystemInfo.getMoveNextField());
        btnRegist.addKeyListener(SystemInfo.getMoveNextField());
        confirmerCode1.addKeyListener(SystemInfo.getMoveNextField());
        confirmerName1.addKeyListener(SystemInfo.getMoveNextField());
        
        shop2.addKeyListener(SystemInfo.getMoveNextField());
        receivedDateFrom.addKeyListener(SystemInfo.getMoveNextField());
        receivedDateTo.addKeyListener(SystemInfo.getMoveNextField());
        dispatcherCode2.addKeyListener(SystemInfo.getMoveNextField());
        dispatcherName2.addKeyListener(SystemInfo.getMoveNextField());
        confirmerCode2.addKeyListener(SystemInfo.getMoveNextField());
        confirmerName2.addKeyListener(SystemInfo.getMoveNextField());
        shopOther.addKeyListener(SystemInfo.getMoveNextField());
        rdoDivisionAll2.addKeyListener(new KeyListenerForRadioButton(
                null, rdoDivisionSale2,
                shopOther, rdoDirectionIn2));
        rdoDivisionSale2.addKeyListener(new KeyListenerForRadioButton(
                rdoDivisionAll2, rdoDivisionWork2,
                shopOther, rdoDirectionIn2));
        rdoDivisionWork2.addKeyListener(new KeyListenerForRadioButton(
                rdoDivisionSale2, null,
                shopOther, rdoDirectionIn2));
        rdoDirectionIn2.addKeyListener(new KeyListenerForRadioButton(
                null, rdoDirectionOut2,
                rdoDivisionWork2, btnShow2));
        rdoDirectionOut2.addKeyListener(new KeyListenerForRadioButton(
                rdoDirectionIn2, null,
                rdoDivisionWork2, btnShow2));
        btnShow2.addKeyListener(SystemInfo.getMoveNextField());
        
        shop3.addKeyListener(SystemInfo.getMoveNextField());
        destination.addKeyListener(SystemInfo.getMoveNextField());
        btnStockList.addKeyListener(SystemInfo.getMoveNextField());
        btnDispatch.addKeyListener(SystemInfo.getMoveNextField());
    }
    
    /** フォーカス取得時テキスト全選択の設定 */
    private void setFocusListener(){
        confirmerCode1.addFocusListener(SystemInfo.getSelectText());
        confirmerCode2.addFocusListener(SystemInfo.getSelectText());
        dispatcherCode2.addFocusListener(SystemInfo.getSelectText());
        dispatcherCode3.addFocusListener(SystemInfo.getSelectText());
    }
    
    private void initItemClass() {
        try {
            classes.loadAll(SystemInfo.getConnection());
            for (int i = 0; i < classes.size(); i++) {
                MstItemClass class1 = classes.get(i);
                
                if (i == tblProduct1.getModel().getRowCount()) {
                    ((DefaultTableModel)tblProduct1.getModel()).addRow(new Vector());
                }
                tblProduct1.setValueAt(class1, i, 0) ;
                
                if (i == tblProduct2.getModel().getRowCount()) {
                    ((DefaultTableModel)tblProduct2.getModel()).addRow(new Vector());
                }
                tblProduct2.setValueAt(class1, i, 0) ;
            }
            
            if (tblProduct1.getRowCount() > 0) {
                tblProduct1.setRowSelectionInterval(0, 0);
                tblProductDoClick(1);
            }
            if (tblProduct2.getRowCount() > 0) {
                tblProduct2.setRowSelectionInterval(0, 0);
                tblProductDoClick(2);
            }
            
        } catch (RuntimeException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initDetailList(JTableEx tbl) {
        //add listener to detail info
        SelectionListener listener = new SelectionListener(tbl);
        tbl.getModel().addTableModelListener(listener);
        
        //編集コラム属性を設置する
//        JFormattedTextField ftf = new javax.swing.JFormattedTextField();
//        ((PlainDocument)ftf.getDocument()).setDocumentFilter(
//                new CustomFilter(20, CustomFilter.NUMERIC));
        
        TableColumnModel tcm = tbl.getColumnModel();
        tcm.getColumn(6).setCellRenderer(new EditabeTableCellRenderer(Integer.class, "1"));
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DivisionGroup1 = new javax.swing.ButtonGroup();
        DirectionGroup1 = new javax.swing.ButtonGroup();
        DivisionGroup2 = new javax.swing.ButtonGroup();
        DirectionGroup2 = new javax.swing.ButtonGroup();
        jTabbedPaneRoot = new javax.swing.JTabbedPane();
        panelUnfinished = new javax.swing.JPanel();
        labelShop1 = new javax.swing.JLabel();
        shop1 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnRegist = new javax.swing.JButton();
        labelConfirmDate = new javax.swing.JLabel();
        confirmDate = new FSCalenderCombo(SystemInfo.getSystemDate());
        labelDivision1 = new javax.swing.JLabel();
        rdoDivisionAll1 = new javax.swing.JRadioButton();
        rdoDivisionSale1 = new javax.swing.JRadioButton();
        rdoDivisionWork1 = new javax.swing.JRadioButton();
        labelDirection1 = new javax.swing.JLabel();
        rdoDirectionIn1 = new javax.swing.JRadioButton();
        rdoDirectionOut1 = new javax.swing.JRadioButton();
        btnShow1 = new javax.swing.JButton();
        labelConfirmer1 = new javax.swing.JLabel();
        confirmerCode1 = new javax.swing.JFormattedTextField();
        ((PlainDocument)confirmerCode1.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.ALPHAMERIC));
        confirmerName1 = new javax.swing.JComboBox();
        paneDetail1 = new javax.swing.JScrollPane();
        tblDetail1 = new com.geobeck.swing.JTableEx();
        panelFinished = new javax.swing.JPanel();
        labelShop2 = new javax.swing.JLabel();
        shop2 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        labelReceivedDate = new javax.swing.JLabel();
        receivedDateFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        tilde1 = new javax.swing.JLabel();
        receivedDateTo = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        labelSendDate = new javax.swing.JLabel();
        sendDateFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        tilde2 = new javax.swing.JLabel();
        sendDateTo = new FSCalenderCombo((Date)null);
        labelShopOther = new javax.swing.JLabel();
        shopOther = new javax.swing.JComboBox();
        labelDivision2 = new javax.swing.JLabel();
        rdoDivisionAll2 = new javax.swing.JRadioButton();
        rdoDivisionSale2 = new javax.swing.JRadioButton();
        rdoDivisionWork2 = new javax.swing.JRadioButton();
        labelDirection2 = new javax.swing.JLabel();
        rdoDirectionIn2 = new javax.swing.JRadioButton();
        rdoDirectionOut2 = new javax.swing.JRadioButton();
        btnShow2 = new javax.swing.JButton();
        labelDispatcher2 = new javax.swing.JLabel();
        dispatcherCode2 = new javax.swing.JFormattedTextField();
        ((PlainDocument)dispatcherCode2.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.ALPHAMERIC));
        dispatcherName2 = new javax.swing.JComboBox();
        labelConfirmer2 = new javax.swing.JLabel();
        confirmerCode2 = new javax.swing.JFormattedTextField();
        ((PlainDocument)confirmerCode2.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.ALPHAMERIC));
        confirmerName2 = new javax.swing.JComboBox();
        paneDetail2 = new javax.swing.JScrollPane();
        tblDetail2 = new com.geobeck.swing.JTableEx();
        btnOutput = new javax.swing.JButton();
        panelStart = new javax.swing.JPanel();
        labelShop3 = new javax.swing.JLabel();
        shop3 = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        dispatcherCode3 = new javax.swing.JFormattedTextField();
        ((PlainDocument)dispatcherCode3.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.ALPHAMERIC));
        dispatcherName3 = new javax.swing.JComboBox();
        labelDestination = new javax.swing.JLabel();
        destination = new javax.swing.JComboBox();
        btnStockList = new javax.swing.JButton();
        btnDispatch = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelForShopSale = new javax.swing.JPanel();
        paneProduct1 = new javax.swing.JScrollPane();
        tblProduct1 = new com.geobeck.swing.JTableEx();
        paneProductDetail1 = new javax.swing.JScrollPane();
        tblProductDetail1 = new com.geobeck.swing.JTableEx();
        panelForBuiness = new javax.swing.JPanel();
        paneProduct2 = new javax.swing.JScrollPane();
        tblProductDetail2 = new com.geobeck.swing.JTableEx();
        paneProductDetail2 = new javax.swing.JScrollPane();
        tblProduct2 = new com.geobeck.swing.JTableEx();
        panelDetail3 = new javax.swing.JScrollPane();
        tblDetail3 = new com.geobeck.swing.JTableEx();
        labelDispatcher3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        inventoryDateGyoumu = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        inventoryDateTenpan = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        jTabbedPaneRoot.setPreferredSize(new java.awt.Dimension(626, 459));

        panelUnfinished.setOpaque(false);

        labelShop1.setText("店舗");

        shop1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        btnRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg")
        );
        btnRegist.setBorderPainted(false);
        btnRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg")
        );
        btnRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistActionPerformed(evt);
            }
        });

        labelConfirmDate.setText("確認日");

        confirmDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        confirmDate.setDate(new java.util.Date());

        labelDivision1.setText("商品区分");

        DivisionGroup1.add(rdoDivisionAll1);
        rdoDivisionAll1.setSelected(true);
        rdoDivisionAll1.setText("全て");
        rdoDivisionAll1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDivisionAll1.setContentAreaFilled(false);
        rdoDivisionAll1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        DivisionGroup1.add(rdoDivisionSale1);
        rdoDivisionSale1.setText("店販用");
        rdoDivisionSale1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDivisionSale1.setContentAreaFilled(false);
        rdoDivisionSale1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        DivisionGroup1.add(rdoDivisionWork1);
        rdoDivisionWork1.setText("業務用");
        rdoDivisionWork1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDivisionWork1.setContentAreaFilled(false);
        rdoDivisionWork1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        labelDirection1.setText("出庫区分");

        DirectionGroup1.add(rdoDirectionIn1);
        rdoDirectionIn1.setSelected(true);
        rdoDirectionIn1.setText("入庫");
        rdoDirectionIn1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDirectionIn1.setContentAreaFilled(false);
        rdoDirectionIn1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        DirectionGroup1.add(rdoDirectionOut1);
        rdoDirectionOut1.setText("出庫");
        rdoDirectionOut1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDirectionOut1.setContentAreaFilled(false);
        rdoDirectionOut1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        btnShow1.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg")
        );
        btnShow1.setBorderPainted(false);
        btnShow1.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg")
        );
        btnShow1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShow1ActionPerformed(evt);
            }
        });

        labelConfirmer1.setText("確認担当者");

        confirmerCode1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        confirmerCode1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                confirmerCode1FocusLost(evt);
            }
        });

        confirmerName1.setMaximumRowCount(12);
        confirmerName1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        confirmerName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmerName1ActionPerformed(evt);
            }
        });

        paneDetail1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tblDetail1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "区分", "分類", "商品名", "仕入価格", "販売価格", "出庫元店舗", "出庫担当者", "出庫日", "入庫前在庫", "入庫数", "現在庫", "確認"
            }) {
                Class[] types = new Class [] {
                    String.class,
                    String.class,
                    String.class,
                    Integer.class,
                    Integer.class,
                    String.class,
                    String.class,
                    String.class,
                    Integer.class,
                    Integer.class,
                    Integer.class,
                    Boolean.class};
                //boolean[] canEdit = new boolean [] {
                    //    false, false, false, false, false, false, false, false, false, false, false, true
                    //};
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == 11 && tblDetail1IsCheckable;
                }
            });
            tblDetail1.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblDetail1.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblDetail1.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblDetail1, SystemInfo.getTableHeaderRenderer());
            tblDetail1.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblDetail1);
            paneDetail1.setViewportView(tblDetail1);

            javax.swing.GroupLayout panelUnfinishedLayout = new javax.swing.GroupLayout(panelUnfinished);
            panelUnfinished.setLayout(panelUnfinishedLayout);
            panelUnfinishedLayout.setHorizontalGroup(
                panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelUnfinishedLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                            .addComponent(paneDetail1, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                            .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                    .addComponent(labelShop1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(15, 15, 15)
                                    .addComponent(shop1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                    .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                            .addComponent(labelDivision1)
                                            .addGap(15, 15, 15)
                                            .addComponent(rdoDivisionAll1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(rdoDivisionSale1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                            .addComponent(labelDirection1)
                                            .addGap(15, 15, 15)
                                            .addComponent(rdoDirectionIn1)
                                            .addGap(15, 15, 15)
                                            .addComponent(rdoDirectionOut1)))
                                    .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(rdoDivisionWork1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                            .addGap(76, 76, 76)
                                            .addComponent(btnShow1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                    .addGap(148, 148, 148)
                                    .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelConfirmDate, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                        .addComponent(labelConfirmer1, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                                            .addComponent(confirmerCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(confirmerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(confirmDate, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUnfinishedLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(180, 180, 180))))))
            );
            panelUnfinishedLayout.setVerticalGroup(
                panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelUnfinishedLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelUnfinishedLayout.createSequentialGroup()
                            .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelShop1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(shop1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(labelConfirmDate, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(rdoDivisionSale1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                    .addComponent(rdoDivisionWork1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                    .addComponent(rdoDivisionAll1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                    .addComponent(labelDivision1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                    .addComponent(confirmDate, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)))
                            .addGap(6, 6, 6)
                            .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(confirmerCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(confirmerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelConfirmer1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelUnfinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelDirection1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoDirectionIn1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoDirectionOut1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(btnShow1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(paneDetail1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPaneRoot.addTab("　未完了　", panelUnfinished);

            panelFinished.setOpaque(false);

            labelShop2.setText("店舗");

            shop2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            labelReceivedDate.setText("確認日付");

            receivedDateFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            receivedDateFrom.setDate(new java.util.Date());

            tilde1.setText("～");

            receivedDateTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            receivedDateTo.setDate(new java.util.Date());

            labelSendDate.setText("出庫期間");

            sendDateFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            sendDateFrom.setDate(new java.util.Date());
            sendDateFrom.setDate((Date)null);

            tilde2.setText("～");

            sendDateTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            sendDateTo.setDate(new java.util.Date());
            sendDateTo.setDate((Date)null);

            labelShopOther.setText("出庫店舗");

            shopOther.setMaximumRowCount(12);

            labelDivision2.setText("商品区分");

            DivisionGroup2.add(rdoDivisionAll2);
            rdoDivisionAll2.setSelected(true);
            rdoDivisionAll2.setText("全て");
            rdoDivisionAll2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDivisionAll2.setContentAreaFilled(false);
            rdoDivisionAll2.setMargin(new java.awt.Insets(0, 0, 0, 0));

            DivisionGroup2.add(rdoDivisionSale2);
            rdoDivisionSale2.setText("店販用");
            rdoDivisionSale2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDivisionSale2.setContentAreaFilled(false);
            rdoDivisionSale2.setMargin(new java.awt.Insets(0, 0, 0, 0));

            DivisionGroup2.add(rdoDivisionWork2);
            rdoDivisionWork2.setText("業務用");
            rdoDivisionWork2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDivisionWork2.setContentAreaFilled(false);
            rdoDivisionWork2.setMargin(new java.awt.Insets(0, 0, 0, 0));

            labelDirection2.setText("出庫区分");

            DirectionGroup2.add(rdoDirectionIn2);
            rdoDirectionIn2.setSelected(true);
            rdoDirectionIn2.setText("入庫");
            rdoDirectionIn2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDirectionIn2.setContentAreaFilled(false);
            rdoDirectionIn2.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoDirectionIn2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rdoDirectionIn2ActionPerformed(evt);
                }
            });

            DirectionGroup2.add(rdoDirectionOut2);
            rdoDirectionOut2.setText("出庫");
            rdoDirectionOut2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDirectionOut2.setContentAreaFilled(false);
            rdoDirectionOut2.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoDirectionOut2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rdoDirectionOut2ActionPerformed(evt);
                }
            });

            btnShow2.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg")
            );
            btnShow2.setBorderPainted(false);
            btnShow2.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg")
            );
            btnShow2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnShow2ActionPerformed(evt);
                }
            });

            labelDispatcher2.setText("出庫担当者");

            dispatcherCode2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            dispatcherCode2.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    dispatcherCode2FocusLost(evt);
                }
            });

            dispatcherName2.setMaximumRowCount(12);
            dispatcherName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            dispatcherName2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispatcherName2ActionPerformed(evt);
                }
            });

            labelConfirmer2.setText("確認担当者");

            confirmerCode2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            confirmerCode2.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    confirmerCode2FocusLost(evt);
                }
            });

            confirmerName2.setMaximumRowCount(12);
            confirmerName2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            confirmerName2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    confirmerName2ActionPerformed(evt);
                }
            });

            paneDetail2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblDetail2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "区分", "仕入先", "分類", "商品名", "仕入価格", "販売価格", "出庫店舗", "出庫担当者", "出庫日", "出庫数", "確認日", "確認担当者"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblDetail2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
            tblDetail2.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblDetail2.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblDetail2.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblDetail2, SystemInfo.getTableHeaderRenderer());
            tblDetail2.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblDetail2);
            paneDetail2.setViewportView(tblDetail2);

            btnOutput.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg")
            );
            btnOutput.setBorderPainted(false);
            btnOutput.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg")
            );
            btnOutput.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnOutputActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout panelFinishedLayout = new javax.swing.GroupLayout(panelFinished);
            panelFinished.setLayout(panelFinishedLayout);
            panelFinishedLayout.setHorizontalGroup(
                panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFinishedLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(paneDetail2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                        .addGroup(panelFinishedLayout.createSequentialGroup()
                            .addComponent(labelDirection2)
                            .addGap(20, 20, 20)
                            .addComponent(rdoDirectionIn2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rdoDirectionOut2)
                            .addGap(133, 133, 133)
                            .addComponent(btnShow2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelFinishedLayout.createSequentialGroup()
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelShop2)
                                .addComponent(labelReceivedDate)
                                .addComponent(labelSendDate)
                                .addComponent(labelShopOther))
                            .addGap(20, 20, 20)
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(shop2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelFinishedLayout.createSequentialGroup()
                                    .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelFinishedLayout.createSequentialGroup()
                                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(receivedDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(sendDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(7, 7, 7)
                                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(tilde1)
                                                .addComponent(tilde2))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(receivedDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(sendDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(shopOther, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(66, 66, 66)
                                    .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panelFinishedLayout.createSequentialGroup()
                                            .addComponent(labelDispatcher2)
                                            .addGap(10, 10, 10)
                                            .addComponent(dispatcherCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(dispatcherName2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(panelFinishedLayout.createSequentialGroup()
                                            .addComponent(labelConfirmer2)
                                            .addGap(10, 10, 10)
                                            .addComponent(confirmerCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(confirmerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGroup(panelFinishedLayout.createSequentialGroup()
                            .addComponent(labelDivision2)
                            .addGap(20, 20, 20)
                            .addComponent(rdoDivisionAll2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rdoDivisionSale2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rdoDivisionWork2)))
                    .addContainerGap())
            );
            panelFinishedLayout.setVerticalGroup(
                panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFinishedLayout.createSequentialGroup()
                    .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFinishedLayout.createSequentialGroup()
                            .addGap(100, 100, 100)
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelShopOther, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(shopOther, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelDivision2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoDivisionAll2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoDivisionSale2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoDivisionWork2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelFinishedLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(shop2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelShop2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(receivedDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tilde1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                .addComponent(labelReceivedDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(confirmerCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(confirmerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(labelConfirmer2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(receivedDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(sendDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tilde2, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                    .addComponent(sendDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelSendDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelDispatcher2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dispatcherCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dispatcherName2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(labelDirection2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoDirectionIn2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoDirectionOut2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnShow2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(paneDetail2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPaneRoot.addTab("　完了　", panelFinished);

            panelStart.setOpaque(false);

            labelShop3.setText("店舗");

            shop3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            shop3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    shop3ActionPerformed(evt);
                }
            });

            dispatcherCode3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            dispatcherCode3.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    dispatcherCode3FocusLost(evt);
                }
            });

            dispatcherName3.setMaximumRowCount(12);
            dispatcherName3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            dispatcherName3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispatcherName3ActionPerformed(evt);
                }
            });

            labelDestination.setText("対象店舗");
            labelDestination.setRequestFocusEnabled(false);

            destination.setMaximumRowCount(12);
            destination.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    destinationActionPerformed(evt);
                }
            });

            btnStockList.setIcon(SystemInfo.getImageIcon("/button/product/zaikolist_off.jpg"));
            btnStockList.setPressedIcon(SystemInfo.getImageIcon("/button/product/zaikolist_on.jpg"));
            btnStockList.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnStockListActionPerformed(evt);
                }
            });

            btnDispatch.setIcon(SystemInfo.getImageIcon("/button/product/delivery_off.jpg"));
            btnDispatch.setPressedIcon(SystemInfo.getImageIcon("/button/product/delivery_on.jpg"));
            btnDispatch.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnDispatchActionPerformed(evt);
                }
            });

            paneProduct1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblProduct1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "商品分類"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblProduct1.setRequestFocusEnabled(false);
            tblProduct1.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblProduct1.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblProduct1.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblProduct1, SystemInfo.getTableHeaderRenderer());
            tblProduct1.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblProduct1);
            tblProduct1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    tblProduct1MouseReleased(evt);
                }
            });
            tblProduct1.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    tblProduct1KeyReleased(evt);
                }
            });
            paneProduct1.setViewportView(tblProduct1);

            paneProductDetail1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblProductDetail1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "商品名"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblProductDetail1.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblProductDetail1.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblProductDetail1.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblProductDetail1, SystemInfo.getTableHeaderRenderer());
            tblProductDetail1.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblProductDetail1);
            tblProductDetail1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tblProductDetail1MouseClicked(evt);
                }
            });
            paneProductDetail1.setViewportView(tblProductDetail1);

            javax.swing.GroupLayout panelForShopSaleLayout = new javax.swing.GroupLayout(panelForShopSale);
            panelForShopSale.setLayout(panelForShopSaleLayout);
            panelForShopSaleLayout.setHorizontalGroup(
                panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelForShopSaleLayout.createSequentialGroup()
                    .addComponent(paneProduct1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(paneProductDetail1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
            );
            panelForShopSaleLayout.setVerticalGroup(
                panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(paneProduct1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(paneProductDetail1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
            );

            jTabbedPane1.addTab("店販用", panelForShopSale);

            paneProduct2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblProductDetail2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "商品名"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblProductDetail2.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblProductDetail2.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblProductDetail2.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblProductDetail2, SystemInfo.getTableHeaderRenderer());
            tblProductDetail2.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblProductDetail2);
            tblProductDetail2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tblProductDetail2MouseClicked(evt);
                }
            });
            paneProduct2.setViewportView(tblProductDetail2);

            paneProductDetail2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblProduct2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "商品分類"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblProduct2.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblProduct2.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblProduct2.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblProduct2, SystemInfo.getTableHeaderRenderer());
            tblProduct2.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblProduct2);
            tblProduct2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    tblProduct2MouseReleased(evt);
                }
            });
            tblProduct2.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    tblProduct2KeyReleased(evt);
                }
            });
            paneProductDetail2.setViewportView(tblProduct2);

            javax.swing.GroupLayout panelForBuinessLayout = new javax.swing.GroupLayout(panelForBuiness);
            panelForBuiness.setLayout(panelForBuinessLayout);
            panelForBuinessLayout.setHorizontalGroup(
                panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelForBuinessLayout.createSequentialGroup()
                    .addComponent(paneProductDetail2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(paneProduct2, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
            );
            panelForBuinessLayout.setVerticalGroup(
                panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(paneProductDetail2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(paneProduct2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            );

            jTabbedPane1.addTab("業務用", panelForBuiness);

            panelDetail3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblDetail3.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "区分", "分類", "商品名", "仕入価格", "販売価格", "現在数", "移動数", ""
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, true, true
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblDetail3.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblDetail3.setSelectionForeground(new java.awt.Color(0, 0, 0));
            SwingUtil.setJTableHeaderRenderer(tblDetail3, SystemInfo.getTableHeaderRenderer());
            panelDetail3.setViewportView(tblDetail3);

            labelDispatcher3.setText("出庫担当者");

            jPanel1.setOpaque(false);

            jLabel6.setForeground(java.awt.Color.red);
            jLabel6.setText("【前回棚卸日】");

            jLabel9.setForeground(java.awt.Color.red);
            jLabel9.setText("業務用 ：");

            inventoryDateGyoumu.setForeground(java.awt.Color.red);
            inventoryDateGyoumu.setText("2008年12月31日");

            jLabel8.setForeground(java.awt.Color.red);
            jLabel8.setText("店販用 ：");

            inventoryDateTenpan.setForeground(java.awt.Color.red);
            inventoryDateTenpan.setText("2008年12月31日");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(inventoryDateTenpan))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(inventoryDateGyoumu)))))
                    .addContainerGap(55, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(inventoryDateGyoumu, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(inventoryDateTenpan, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout panelStartLayout = new javax.swing.GroupLayout(panelStart);
            panelStart.setLayout(panelStartLayout);
            panelStartLayout.setHorizontalGroup(
                panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelDetail3, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                        .addGroup(panelStartLayout.createSequentialGroup()
                            .addGroup(panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelDestination)
                                .addComponent(labelShop3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStartLayout.createSequentialGroup()
                                    .addComponent(shop3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                    .addComponent(labelDispatcher3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dispatcherCode3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dispatcherName3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(destination, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                            .addComponent(btnStockList, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDispatch, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelStartLayout.createSequentialGroup()
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(55, 55, 55)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            panelStartLayout.setVerticalGroup(
                panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelShop3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(shop3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelDispatcher3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dispatcherCode3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dispatcherName3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDispatch, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnStockList, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelStartLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelDestination, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(destination, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelStartLayout.createSequentialGroup()
                            .addGap(190, 190, 190)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panelDetail3, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane1.getAccessibleContext().setAccessibleName("");

            jTabbedPaneRoot.addTab("　出庫　", panelStart);

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
        }// </editor-fold>//GEN-END:initComponents

    private void destinationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationActionPerformed
        SwingUtil.clearTable(tblDetail3);
    }//GEN-LAST:event_destinationActionPerformed

    // <editor-fold defaultstate="collapsed" desc="デザイナによるイベントハンドラ">
    private void shop3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shop3ActionPerformed
        showItemStock(null);
    }//GEN-LAST:event_shop3ActionPerformed
    
    private void dispatcherName3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dispatcherName3ActionPerformed
        UIUtil.outputStaff(dispatcherName3, dispatcherCode3);
    }//GEN-LAST:event_dispatcherName3ActionPerformed
    
    private void dispatcherCode3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dispatcherCode3FocusLost
        UIUtil.selectStaff(dispatcherCode3, dispatcherName3);
    }//GEN-LAST:event_dispatcherCode3FocusLost
    
    private void rdoDirectionOut2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDirectionOut2ActionPerformed
        labelShopOther.setText("入庫店舗");
    }//GEN-LAST:event_rdoDirectionOut2ActionPerformed
    
    private void rdoDirectionIn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDirectionIn2ActionPerformed
        labelShopOther.setText("出庫店舗");
    }//GEN-LAST:event_rdoDirectionIn2ActionPerformed
    
    private void btnDispatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDispatchActionPerformed
        dispatch();
    }//GEN-LAST:event_btnDispatchActionPerformed
    
    private void btnStockListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockListActionPerformed
        transitToStockList();
    }//GEN-LAST:event_btnStockListActionPerformed
    
    private void dispatcherName2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dispatcherName2ActionPerformed
        UIUtil.outputStaff(dispatcherName2, dispatcherCode2);
    }//GEN-LAST:event_dispatcherName2ActionPerformed
    
    private void dispatcherCode2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dispatcherCode2FocusLost
        UIUtil.selectStaff(dispatcherCode2, dispatcherName2);
    }//GEN-LAST:event_dispatcherCode2FocusLost
    
        private void confirmerName2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmerName2ActionPerformed
            UIUtil.outputStaff(confirmerName2, confirmerCode2);
        }//GEN-LAST:event_confirmerName2ActionPerformed
        
        private void confirmerCode2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_confirmerCode2FocusLost
            UIUtil.selectStaff(confirmerCode2, confirmerName2);
        }//GEN-LAST:event_confirmerCode2FocusLost
        
        private void btnShow2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow2ActionPerformed
            show2();
        }//GEN-LAST:event_btnShow2ActionPerformed
        
        private void confirmerName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmerName1ActionPerformed
            UIUtil.outputStaff(confirmerName1, confirmerCode1);
        }//GEN-LAST:event_confirmerName1ActionPerformed
        
        private void confirmerCode1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_confirmerCode1FocusLost
            UIUtil.selectStaff(confirmerCode1, confirmerName1);
        }//GEN-LAST:event_confirmerCode1FocusLost
        
        private void btnShow1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow1ActionPerformed
            show1();
        }//GEN-LAST:event_btnShow1ActionPerformed
        
	private void tblProduct1MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblProduct1MouseReleased
	{//GEN-HEADEREND:event_tblProduct1MouseReleased
            tblProductDoClick(1);
	}//GEN-LAST:event_tblProduct1MouseReleased
        
	private void tblProduct1KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblProduct1KeyReleased
	{//GEN-HEADEREND:event_tblProduct1KeyReleased
            tblProductDoClick(1);
	}//GEN-LAST:event_tblProduct1KeyReleased
        
	private void tblProduct2MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblProduct2MouseReleased
	{//GEN-HEADEREND:event_tblProduct2MouseReleased
            tblProductDoClick(2);
	}//GEN-LAST:event_tblProduct2MouseReleased
        
	private void tblProduct2KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblProduct2KeyReleased
	{//GEN-HEADEREND:event_tblProduct2KeyReleased
            tblProductDoClick(2);
	}//GEN-LAST:event_tblProduct2KeyReleased
        
        private void btnRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistActionPerformed
            if(checkInput()) {
                regist();
            }
        }//GEN-LAST:event_btnRegistActionPerformed
        private boolean checkInput() {
            if(confirmDate.getDate() == null) {
                 MessageDialog.showMessageDialog(this,
                        "確認日を入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                confirmDate.requestFocusInWindow();
                return false;
            }
            return true;
        }
    private void tblProductDetail2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductDetail2MouseClicked

        if ((evt.getClickCount() % 2) == 0 && evt.getButton() == 1) {
            addItemToDetailTable(2);
        }
    }//GEN-LAST:event_tblProductDetail2MouseClicked
    
    private void tblProductDetail1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductDetail1MouseClicked

        if ((evt.getClickCount() % 2) == 0 && evt.getButton() == 1) {
            addItemToDetailTable(1);
        }
    }//GEN-LAST:event_tblProductDetail1MouseClicked

    private void btnOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutputActionPerformed
        btnOutput.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (jTabbedPaneRoot.getSelectedIndex() == 1) {

                if (lstMoveStockReportBean.size() > 0) {
                    this.printMoveStock();
                } else {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(4001),
                            this.getTitle() + " - 分類別集計",
                            JOptionPane.ERROR_MESSAGE);
                }
            } 
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnOutputActionPerformed
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="デザイナによる変数宣言">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup DirectionGroup1;
    private javax.swing.ButtonGroup DirectionGroup2;
    private javax.swing.ButtonGroup DivisionGroup1;
    private javax.swing.ButtonGroup DivisionGroup2;
    private javax.swing.JButton btnDispatch;
    private javax.swing.JButton btnOutput;
    private javax.swing.JButton btnRegist;
    private javax.swing.JButton btnShow1;
    private javax.swing.JButton btnShow2;
    private javax.swing.JButton btnStockList;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo confirmDate;
    private javax.swing.JFormattedTextField confirmerCode1;
    private javax.swing.JFormattedTextField confirmerCode2;
    private javax.swing.JComboBox confirmerName1;
    private javax.swing.JComboBox confirmerName2;
    private javax.swing.JComboBox destination;
    private javax.swing.JFormattedTextField dispatcherCode2;
    private javax.swing.JFormattedTextField dispatcherCode3;
    private javax.swing.JComboBox dispatcherName2;
    private javax.swing.JComboBox dispatcherName3;
    private javax.swing.JLabel inventoryDateGyoumu;
    private javax.swing.JLabel inventoryDateTenpan;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPaneRoot;
    private javax.swing.JLabel labelConfirmDate;
    private javax.swing.JLabel labelConfirmer1;
    private javax.swing.JLabel labelConfirmer2;
    private javax.swing.JLabel labelDestination;
    private javax.swing.JLabel labelDirection1;
    private javax.swing.JLabel labelDirection2;
    private javax.swing.JLabel labelDispatcher2;
    private javax.swing.JLabel labelDispatcher3;
    private javax.swing.JLabel labelDivision1;
    private javax.swing.JLabel labelDivision2;
    private javax.swing.JLabel labelReceivedDate;
    private javax.swing.JLabel labelSendDate;
    private javax.swing.JLabel labelShop1;
    private javax.swing.JLabel labelShop2;
    private javax.swing.JLabel labelShop3;
    private javax.swing.JLabel labelShopOther;
    private javax.swing.JScrollPane paneDetail1;
    private javax.swing.JScrollPane paneDetail2;
    private javax.swing.JScrollPane paneProduct1;
    private javax.swing.JScrollPane paneProduct2;
    private javax.swing.JScrollPane paneProductDetail1;
    private javax.swing.JScrollPane paneProductDetail2;
    private javax.swing.JScrollPane panelDetail3;
    private javax.swing.JPanel panelFinished;
    private javax.swing.JPanel panelForBuiness;
    private javax.swing.JPanel panelForShopSale;
    private javax.swing.JPanel panelStart;
    private javax.swing.JPanel panelUnfinished;
    private javax.swing.JRadioButton rdoDirectionIn1;
    private javax.swing.JRadioButton rdoDirectionIn2;
    private javax.swing.JRadioButton rdoDirectionOut1;
    private javax.swing.JRadioButton rdoDirectionOut2;
    private javax.swing.JRadioButton rdoDivisionAll1;
    private javax.swing.JRadioButton rdoDivisionAll2;
    private javax.swing.JRadioButton rdoDivisionSale1;
    private javax.swing.JRadioButton rdoDivisionSale2;
    private javax.swing.JRadioButton rdoDivisionWork1;
    private javax.swing.JRadioButton rdoDivisionWork2;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo receivedDateFrom;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo receivedDateTo;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo sendDateFrom;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo sendDateTo;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop1;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop2;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop3;
    private javax.swing.JComboBox shopOther;
    private com.geobeck.swing.JTableEx tblDetail1;
    private com.geobeck.swing.JTableEx tblDetail2;
    private com.geobeck.swing.JTableEx tblDetail3;
    private com.geobeck.swing.JTableEx tblProduct1;
    private com.geobeck.swing.JTableEx tblProduct2;
    private com.geobeck.swing.JTableEx tblProductDetail1;
    private com.geobeck.swing.JTableEx tblProductDetail2;
    private javax.swing.JLabel tilde1;
    private javax.swing.JLabel tilde2;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
    // <editor-fold desc="未完了タブ">
    /** アイテムのリスト */
    ArrayList<Integer> itemIdList;
    /** 出庫店舗のリスト */
    ArrayList<Integer> shopIdList;
    /** 出庫伝票番号のリスト */
    ArrayList<Integer> slipNoList;
    /** 出庫伝票詳細番号のリスト */
    ArrayList<Integer> slipDetailNoList;
    /** 仕入先IDのリスト */
    ArrayList<Integer> supplierIdList;
    /** 表の確認欄の編集可否 */
    boolean tblDetail1IsCheckable;
    /** 表示ボタン押下時に選択されていた自店舗ID */
    Integer shopIdSelf = null;
    /** 表示(未完了タブ) */
    private void show1(){
        /** 自店舗IDのカラム名＝ */
        String self_eq_;
        /** 相手("dest"または"src") */
        String other;
        /** 店販業務区分の条件式 */
        String division;
        /** 在庫の式 */
        String stock;
        
        // 店舗選択のチェック
        MstShop shopSelf = (MstShop)shop1.getSelectedItem();
        if(shopSelf == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelShop1.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            shop1.requestFocusInWindow();
            return;
        }
        shopIdSelf = shopSelf.getShopID();
        
        // 店販業務区分の条件式を設定する
        if(rdoDivisionAll1.isSelected()){
            division="";
        }else if(rdoDivisionSale1.isSelected()){
            division = "        sd.item_use_division IN (1, 3) AND\n";
        }else if(rdoDivisionWork1.isSelected()){
            division = "        sd.item_use_division IN (2, 3) AND\n";
        }else{
            //商品区分を選択してください
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDivision1.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDivisionAll1.requestFocusInWindow();
            return;
        }
        
        // 出庫区分毎の処理
        if(rdoDirectionIn1.isSelected()){
            /** 入庫 */
            TableColumnModel model = tblDetail1.getTableHeader().getColumnModel();
            model.getColumn(5).setHeaderValue("出庫元店舗");
            model.getColumn(8).setHeaderValue("入庫前在庫");
            model.getColumn(9).setHeaderValue("入庫数");
            model.getColumn(10).setHeaderValue("現在庫");
            tblDetail1IsCheckable = true;
            btnRegist.setEnabled(true);
            confirmerCode1.setEnabled(true);
            confirmerName1.setEnabled(true);
            self_eq_ = "m.dest_shop_id = ";
            other = "src";
            stock = "    --入庫前在庫\n" +
                    "    get_item_stock(m.dest_shop_id, m.item_id, sd.item_use_division, CAST(current_timestamp AS timestamp without time zone)) AS stock\n";
        }else if(rdoDirectionOut1.isSelected()){
            /** 出庫 */
            TableColumnModel model = tblDetail1.getTableHeader().getColumnModel();
            model.getColumn(5).setHeaderValue("入庫先店舗");
            model.getColumn(8).setHeaderValue("出庫前在庫");
            model.getColumn(9).setHeaderValue("出庫数");
            model.getColumn(10).setHeaderValue("在庫数");
//            tblDetail1IsCheckable = false;
            tblDetail1IsCheckable = true;
            btnRegist.setEnabled(false);
            confirmerCode1.setEnabled(false);
            confirmerName1.setEnabled(false);
            self_eq_ = "m.src_shop_id = ";
            other = "dest";
            stock = "    --出庫直後在庫\n" +
                    "    get_item_stock(m.src_shop_id, m.item_id, sd.item_use_division, s.ship_date) AS stock\n";
        }else{
            //出庫区分を選択してください
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDirection1.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDirectionIn1.requestFocusInWindow();
            return;
        }
        tblDetail1.getTableHeader().repaint();
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
                    "SELECT\n" +
                    "    --店販業務区分\n" +
                    "    sd.item_use_division,\n" +
                    "    --分類\n" +
                    "    (SELECT\n" +
                    "        c.item_class_name\n" +
                    "    FROM\n" +
                    "        mst_item_class AS c\n" +
                    "    WHERE\n" +
                    "        c.item_class_id = i.item_class_id\n" +
                    "    ) AS item_class_name,\n" +
                    "    --商品名\n" +
                    "    i.item_name,\n" +
                    "    --仕入単価\n" +
                    "    sd.cost_price,\n" +
                    "    --販売価格\n" +
                    "    i.price,\n" +
                    "    --相手方店舗名\n" +
                    "    (SELECT\n" +
                    "        mst_shop.shop_name\n" +
                    "    FROM\n" +
                    "        mst_shop AS mst_shop\n" +
                    "    WHERE\n" +
                    "        mst_shop.shop_id = m." + other + "_shop_id AND\n" +
                    "        mst_shop.delete_date IS NULL\n" +
                    "    ) AS shop_name,\n" +
                    "    --出庫担当者\n" +
                    "    (SELECT\n" +
                    "        (mst_staff.staff_name1 || '　' || mst_staff.staff_name2)\n" +
                    "    FROM\n" +
                    "        mst_staff\n" +
                    "    WHERE\n" +
                    "        mst_staff.staff_id = s.staff_id AND\n" +
                    "        mst_staff.delete_date IS NULL\n" +
                    "    ) AS staff_name,\n" +
                    "    --出庫日\n" +
                    "    s.ship_date,\n" +
                    "    --出庫数\n" +
                    "    sd.out_num,\n" +
                    "    --アイテムID\n" +
                    "    m.item_id,\n" +
                    "    --出庫元店舗ID\n" +
                    "    m.src_shop_id,\n" +
                    "    --出庫伝票番号\n" +
                    "    m.src_slip_no,\n" +
                    "    --出庫伝票詳細番号\n" +
                    "    sd.slip_detail_no,\n" +
                    "    --仕入先ID\n" +
                    "    s.supplier_id,\n" +
                    /**/ stock +
                    "FROM\n" +
                    "    data_move_stock AS m,\n" +
                    "    data_slip_ship_detail AS sd\n" +
                    "        LEFT JOIN mst_item as i\n" +
                    "        ON i.item_id = sd.item_id,\n" +
                    "    data_slip_ship AS s\n" +
                    "WHERE\n" +
                    "    m.src_shop_id = sd.shop_id AND\n" +
                    "    m.src_slip_no = sd.slip_no AND\n" +
                    "    m.item_id = sd.item_id AND\n" +
                    "    m.delete_date IS NULL AND\n" +
                    /**/ division +
                    "    sd.delete_date IS NULL AND\n" +
                    /**/ self_eq_ + shopIdSelf+ "AND\n" +
                    "    m.dest_slip_no IS NULL AND --未完了\n" +
                    "    s.shop_id = m.src_shop_id AND\n" +
                    "    s.slip_no = m.src_slip_no AND\n" +
                    "    s.delete_date IS NULL");
            // リストのクリア
            itemIdList = new ArrayList<Integer>();
            shopIdList = new ArrayList<Integer>();
            slipNoList = new ArrayList<Integer>();
            slipDetailNoList = new ArrayList<Integer>();
            supplierIdList = new ArrayList<Integer>();
            // 表のクリア
            ((DefaultTableModel)tblDetail1.getModel()).setRowCount(0);
            
            /** 表に追加する行の項目リスト */
            Vector vec;
            /** 汎用int */
            int i;
            /** 入出庫数 */
            Integer num;
            /** アイテムID */
            int itemId;
            /** 出庫日 */
            Date shipDate;

            if(rdoDirectionIn1.isSelected()) {
                tblDetail1.getTableHeader().getColumnModel().getColumn(tblDetail1.getColumnCount() - 1).setHeaderValue("確認");
            } else {
                tblDetail1.getTableHeader().getColumnModel().getColumn(tblDetail1.getColumnCount() - 1).setHeaderValue("削除");
            }

            while(rs.next()){
                // アイテムID
                itemIdList.add(itemId = rs.getInt("item_id"));
                // 出庫元店舗ID
                shopIdList.add(rs.getInt("src_shop_id"));
                // 出庫伝票番号
                slipNoList.add(rs.getInt("src_slip_no"));
                // 出庫伝票詳細番号
                slipDetailNoList.add(rs.getInt("slip_detail_no"));
                // 出庫伝票詳細番号
                supplierIdList.add(rs.getInt("supplier_id"));
                
                vec = new Vector();
                // 店販業務区分
                vec.add(getUseDivisionString(rs.getInt("item_use_division")));
                // 分類
                vec.add(rs.getString("item_class_name"));
                // 商品名
                vec.add(rs.getString("item_name"));
                // 仕入単価
                i = rs.getInt("cost_price");
                vec.add(rs.wasNull() ? null : i);
                // 販売価格
                i = rs.getInt("price");
                vec.add(rs.wasNull() ? null : i);
                //相手方店舗名
                vec.add(rs.getString("shop_name"));
                // 出庫担当者
                vec.add(rs.getString("staff_name"));
                // 出庫日
                vec.add(shipDate = rs.getDate("ship_date"));
                
                num = rs.getInt("out_num"); // 入出庫数
                num = (rs.wasNull() ? null : num);
                i = rs.getInt("stock"); // 入庫前在庫または出庫前在庫
                if(rs.wasNull()){
                    vec.add(null);
                    vec.add(num);
                    vec.add(null);
                }else{
                    if(rdoDirectionIn1.isSelected()){
                        vec.add(i);
                        vec.add(num);
                        vec.add((long)i + num.longValue());
                    }else{
                        vec.add((long)i + num.longValue());
                        vec.add(num);
                        vec.add(i);
                    }
                }

                if(rdoDirectionOut1.isSelected()) {
                    vec.add(getDirectionOutDeleteButton());
                }

                //行を追加する
                ((DefaultTableModel)tblDetail1.getModel()).addRow(vec);
            }
            rs.close();
        } catch (SQLException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }
    
    /** 登録 */
    private void regist(){
        /** 確認担当者ID */
        Integer confirmerStaffId = null;
        int rowCount = tblDetail1.getRowCount();
        ArrayList<Integer> checkedIndice = new ArrayList<Integer>();
        if(rowCount < 1){
            MessageDialog.showMessageDialog(this,
                    "入庫確認を行う対象が表示されていません。",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            btnShow1.requestFocusInWindow();
            return;
        }
        for (int i = 0; i < rowCount; i++) {
            Object obj = tblDetail1.getValueAt(i, 11);
            if(obj != null && (Boolean)obj){
                checkedIndice.add(i);
            }
        }
        if(checkedIndice.size() < 1){
            MessageDialog.showMessageDialog(this,
                    "確認欄にチェックを入れて下さい。",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        /** 入庫先店舗 */
        if(shopIdSelf == null){
            MessageDialog.showMessageDialog(this,
                    "自店舗IDが不明のため登録できません。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        /** 確認日 */
        String strDate = confirmDate.getDateStr("-");
        if(strDate == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelConfirmDate.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            confirmDate.requestFocusInWindow();
            return ;
        }
        
        // 確認担当者の必須チェック
        if(confirmerName1.getSelectedIndex() > 0){
            confirmerStaffId =
                    ((MstStaff)confirmerName1.getSelectedItem()).getStaffID();
        }
        if(confirmerStaffId == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelConfirmer1.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            confirmerName1.requestFocusInWindow();
            return ;
        }
        
        if (MessageDialog.showYesNoDialog(this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_REGIST, "入庫確認"),
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        try{
            ConnectionWrapper con = SystemInfo.getConnection();
            
            int slipNoMax = getMaxDestSlipNo(con, shopIdSelf);
            if(slipNoMax == Integer.MAX_VALUE){
                MessageDialog.showMessageDialog(this,
                        "伝票番号が登録可能な最大の番号を超えるため登録できませんでした。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // トランザクション開始
                con.begin();
                /** 仕入先ID→入庫伝票番号のマップ */
                HashMap<Integer, Integer> slipNoMap
                        = new HashMap<Integer, Integer>();
                /** 仕入先ID→入庫伝票詳細番号のマップ */
                HashMap<Integer, Integer> slipDetailNoMap
                        = new HashMap<Integer, Integer>();
                /** 出庫元伝票詳細 */
                //              DataSlipShipDetail sd;
                /** アイテム */
//                MstSupplierItem item;
                
                /** 選択された行 */
                int row;
                /** 商品ID */
                int itemId;
                /** 出庫元店舗ID */
                int srcShopId;
                /** 出庫伝票番号 */
                int srcSlipNo;
                /** 出庫伝票詳細番号 */
                int srcDetailSlipNo;
                /** 仕入れ先ID */
                int supplierId;
                /** 入庫伝票番号 */
                int destSlipNo;
                /** 入庫伝票詳細番号 */
                int destDetailSlipNo;
                
                for (Iterator it = checkedIndice.iterator(); it.hasNext();) {
                    row = (Integer)it.next();
                    itemId = itemIdList.get(row);
                    srcShopId = shopIdList.get(row);
                    srcSlipNo = slipNoList.get(row);
                    srcDetailSlipNo = slipDetailNoList.get(row);
                    supplierId = supplierIdList.get(row);
                    
                    if(slipNoMap.containsKey(supplierId)){
                        destSlipNo = slipNoMap.get(supplierId);
                        destDetailSlipNo = slipDetailNoMap.get(supplierId);
                        if (destDetailSlipNo == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "伝票詳細番号が登録可能な最大の番号を超えるため登録できませんでした。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipDetailNoMap.put(supplierId, ++destDetailSlipNo);
                    }else{
                        if (slipNoMax == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "伝票番号が登録可能な最大の番号を超えるため登録できませんでした。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipNoMap.put(supplierId, destSlipNo = ++slipNoMax);
                        slipDetailNoMap.put(supplierId, destDetailSlipNo = 1);
                        // 入庫伝票を登録する
                        insertSlipStore(con, srcShopId, srcSlipNo,
                                shopIdSelf, destSlipNo, confirmerStaffId);
                    }
                    
                    updateDataMoveStock(con, srcShopId, srcSlipNo, itemId,
                            shopIdSelf, destSlipNo);
                    insertSlipStoreDetail(con, srcShopId, srcSlipNo, srcDetailSlipNo,
                            shopIdSelf, destSlipNo, destDetailSlipNo);
                }
                // コミット
                con.commit();
                // 入庫確認した行を削除する
                for (int i = checkedIndice.size(); i > 0; ) {
                    row = checkedIndice.get(--i);
                    itemIdList.remove(row);
                    shopIdList.remove(row);
                    slipNoList.remove(row);
                    slipDetailNoList.remove(row);
                    supplierIdList.remove(row);
                    ((DefaultTableModel)tblDetail1.getModel()).removeRow(row);
                }
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.close();
            }
        } catch (RuntimeException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "入庫伝票"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    /** 入庫伝票を登録する */
    private boolean insertSlipStore(ConnectionWrapper con,
            int srcShopId, int srcSlipNo,
            int destShopId, int destSlipNo, int confirmerId)throws SQLException{
        if(con.executeUpdate(
                "INSERT INTO data_slip_store(\n" +
                "    shop_id,\n" +
                "    slip_no,\n" +
                "    supplier_id,\n" +
                "    store_date,\n" +
                "    staff_id,\n" +
                "    ship_slip_no,\n" +
                "    discount,\n" +
                "    insert_date,\n" +
                "    update_date)\n" +
                "SELECT\n" +
                "    " + destShopId + ", -- 入庫店舗ID\n" +
                "    " + destSlipNo + ", -- 入庫伝票番号\n" +
                "    s.supplier_id,\n" +
                "    "+SQLUtil.convertForSQL(confirmDate.getDate())+",\n" + // 入庫日(零時零分にする)
                "    " + confirmerId + ", -- 入庫担当者\n" +
                "    s.slip_no,\n" +
                "    0,\n" +
                "    CURRENT_TIMESTAMP,\n" +
                "    CURRENT_TIMESTAMP\n" +
                "FROM\n" +
                "    data_slip_ship AS s\n" +
                "WHERE\n" +
                "    s.shop_id = " + srcShopId + " AND\n" +
                "    s.slip_no = " + srcSlipNo) != 1) {
            return false;
        }
        return true;
    }
    
    /** 入庫伝票詳細を登録する */
    private boolean insertSlipStoreDetail(ConnectionWrapper con,
            int srcShopId, int srcSlipNo, int srcSlipDetailNo,
            int destShopId, int destSlipNo, int destSlipDetailNo
            )throws SQLException{
        if(con.executeUpdate(
                "INSERT INTO data_slip_store_detail(\n" +
                "    shop_id,\n" +
                "    slip_no,\n" +
                "    slip_detail_no,\n" +
                "    item_id,\n" +
                "    item_use_division,\n" +
                "    in_num,\n" +
                "    attach_num,\n" +
                "    cost_price,\n" +
                "    in_class,\n" +
                "    insert_date,\n" +
                "    update_date)\n" +
                "SELECT\n" +
                "    " + destShopId + ", -- 入庫店舗ID\n" +
                "    " + destSlipNo + ", -- 入庫伝票番号\n" +
                "    " + destSlipDetailNo + ", -- 入庫伝票詳細番号\n" +
                "    sd.item_id,\n" +
                "    sd.item_use_division,\n" +
                "    sd.out_num,\n" +
                "    0,\n" + // 添付数
                "    sd.cost_price,\n" +
                "    sd.out_class,\n" +
                "    CURRENT_TIMESTAMP,\n" +
                "    CURRENT_TIMESTAMP\n" +
                "FROM\n" +
                "    data_slip_ship_detail AS sd\n" +
                "WHERE\n" +
                "    sd.shop_id = " + srcShopId + " AND\n" +
                "    sd.slip_no = " + srcSlipNo +" AND\n" +
                "    sd.slip_detail_no = " + srcSlipDetailNo) != 1) {
            return false;
        }
        return true;
    }
    
    /** 明細を初期化する。 */
    private void initTblDetail1() {
        DefaultTableColumnModel model =
                (DefaultTableColumnModel)tblDetail1.getColumnModel();
        model.getColumn(0).setPreferredWidth(45);//区分
        model.getColumn(1).setPreferredWidth(70);//分類
        model.getColumn(2).setPreferredWidth(160);//商品名
        model.getColumn(3).setPreferredWidth(54);//仕入価格
        model.getColumn(4).setPreferredWidth(54);//販売価格
        model.getColumn(5).setPreferredWidth(66);//出庫元店舗または入庫先店舗
        model.getColumn(6).setPreferredWidth(66);//出庫担当者
        model.getColumn(7).setPreferredWidth(66);//出庫日
        model.getColumn(8).setPreferredWidth(66);//入庫前在庫または出庫前在庫
        model.getColumn(9).setPreferredWidth(45);//入庫数または出庫数
        model.getColumn(10).setPreferredWidth(45);//現在庫または現在数
        model.getColumn(11).setPreferredWidth(35);//確認
    }
    // </editor-fold>
    
    // <editor-fold desc="完了タブ">
    /** 表示(完了) */
    private void show2(){
        /** 入庫期間From */
        String recvFrom = receivedDateFrom.getDateStr("-");
        /** 入庫期間To */
        String recvTo = receivedDateTo.getDateStr("-");
        /** 出庫期間From */
        String sendFrom = sendDateFrom.getDateStr("-");
        /** 出庫期間To */
        String sendTo = sendDateTo.getDateStr("-");
        /** 自店舗IDのカラム名＝ */
        String self_eq_;
        /** 相手店舗IDのカラム名＝ */
        String other_eq_;
        /** 相手("dest"または"src") */
        String other;
        /** 出庫担当者ID */
        Integer dispatcherStaffId;
        /** 確認担当者ID */
        Integer confirmerStaffId;
        
        /** 条件式 */
        StringBuilder expression = new StringBuilder();
        /** 店販業務区分の条件式 */
        String division;
        
        /** 自店舗 */
        MstShop shop = (MstShop)shop2.getSelectedItem();
        if(shop == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelShop2.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            shop2.requestFocusInWindow();
            return;
        }
        
        // 店販業務区分の条件式を設定する
        if(rdoDivisionAll2.isSelected()){
            division="";
        }else if(rdoDivisionSale2.isSelected()){
            division = "        sd.item_use_division IN (1, 3) AND\n";
        }else if(rdoDivisionWork2.isSelected()){
            division = "        sd.item_use_division IN (2, 3) AND\n";
        }else{
            //商品区分を選択してください
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDivision2.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDivisionAll2.requestFocusInWindow();
            return;
        }
        
        // 出庫区分毎の処理
        if(rdoDirectionIn2.isSelected()){
            TableColumnModel model = tblDetail2.getTableHeader().getColumnModel();
            //IVS_TMTrong start add 2015/09/29 New request #42933
            //model.getColumn(5).setHeaderValue("出庫店舗");
            model.getColumn(6).setHeaderValue("出庫店舗");
            //IVS_TMTrong end add 2015/09/29 New request #42933
            self_eq_ = "m.dest_shop_id = ";
            other_eq_ = "m.src_shop_id = ";
            other = "src";
        }else if(rdoDirectionOut2.isSelected()){
            TableColumnModel model = tblDetail2.getTableHeader().getColumnModel();
            //IVS_TMTrong start add 2015/09/29 New request #42933
            //model.getColumn(5).setHeaderValue("入庫店舗");
            model.getColumn(6).setHeaderValue("入庫店舗");
            //IVS_TMTrong end add 2015/09/29 New request #42933
            self_eq_ = "m.src_shop_id = ";
            other_eq_ = "m.dest_shop_id = ";
            other = "dest";
        }else{
            //出庫区分を選択してください
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDirection2.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDirectionIn2.requestFocusInWindow();
            return;
        }
        tblDetail2.getTableHeader().repaint();
        
        // 入庫From
        if(recvFrom != null){
            expression
                    .append("d.store_date >= date'")
                    .append(recvFrom)
                    .append("' AND\n");
        }
        // 入庫To
        if(recvTo != null){
            expression
                    .append("date_trunc('day', d.store_date) <= date'")
                    .append(recvTo)
                    .append("' AND\n");
        }
        // 出庫From
        if(sendFrom != null){
            expression
                    .append("s.ship_date >= date'")
                    .append(sendFrom)
                    .append("' AND\n");
        }
        // 出庫To
        if(sendTo != null){
            expression
                    .append("date_trunc('day', s.ship_date) <= date'")
                    .append(sendTo)
                    .append("' AND\n");
        }
        /** 相手方店舗 */
        MstShop shopOtherSelection = (MstShop)shopOther.getSelectedItem();
        if(shopOtherSelection != null){
            expression
                    .append(other_eq_)
                    .append(shopOtherSelection.getShopID())
                    .append(" AND\n");
        }
        /** 出庫担当者 */
        if(dispatcherName2.getSelectedIndex() > 0){
            dispatcherStaffId =
                    ((MstStaff)dispatcherName2.getSelectedItem()).getStaffID();
            if(dispatcherStaffId != null){
                expression
                        .append("s.staff_id = ")
                        .append(dispatcherStaffId)
                        .append(" AND\n");
            }
        }
        // 確認担当者
        if(confirmerName2.getSelectedIndex() > 0){
            confirmerStaffId =
                    ((MstStaff)confirmerName2.getSelectedItem()).getStaffID();
            if(confirmerStaffId != null){
                expression
                        .append("d.staff_id = ")
                        .append(confirmerStaffId)
                        .append(" AND\n");
            }
        }
        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
                    "SELECT\n" +
                    "    --店販業務区分\n" +
                    "    sd.item_use_division,\n" +
                    //IVS_TMTrong start add 2015/09/28 New request #42933
                    "    --仕入先\n" +
                    "   ms.supplier_name AS supplier_name,\n"+
                    //IVS_TMTrong end add 2015/09/28 New request #42933
                    "    --分類\n" +
                    "    (SELECT\n" +
                    "        c.item_class_name\n" +
                    "    FROM\n" +
                    "        mst_item_class AS c\n" +
                    "    WHERE\n" +
                    "        c.item_class_id = i.item_class_id\n" +
                    "    ) AS item_class_name,\n" +
                    "    --商品名\n" +
                    "    i.item_name,\n" +
                    "    --仕入単価\n" +
                    "    sd.cost_price,\n" +
                    "    --販売価格\n" +
                    "    i.price,\n" +
                    "    --相手方店舗名\n" +
                    "    (SELECT\n" +
                    "        mst_shop.shop_name\n" +
                    "    FROM\n" +
                    "        mst_shop AS mst_shop\n" +
                    "    WHERE\n" +
                    "        mst_shop.shop_id = m." + other + "_shop_id AND\n" +
                    "        mst_shop.delete_date IS NULL\n" +
                    "    ) AS shop_name,\n" +
                    "    --出庫担当者\n" +
                    "    (SELECT\n" +
                    "        (mst_staff.staff_name1 || '　' || mst_staff.staff_name2)\n" +
                    "    FROM\n" +
                    "        mst_staff\n" +
                    "    WHERE\n" +
                    "        mst_staff.staff_id = s.staff_id AND\n" +
                    "        mst_staff.delete_date IS NULL\n" +
                    "    ) AS staff_name,\n" +
                    "    --出庫日\n" +
                    "    s.ship_date,\n" +
                    "    --出庫数\n" +
                    "    sd.out_num,\n" +
                    "    --確認日\n" +
                    "    d.store_date,\n" +
                    "    --確認担当者\n" +
                    "    (SELECT\n" +
                    "        (mst_staff.staff_name1 || '　' || mst_staff.staff_name2)\n" +
                    "    FROM\n" +
                    "        mst_staff\n" +
                    "    WHERE\n" +
                    "        mst_staff.staff_id = d.staff_id AND\n" +
                    "        mst_staff.delete_date IS NULL\n" +
                    "    ) AS confirmer_name\n" +
                    "FROM\n" +
                    "    data_move_stock AS m,\n" +
                    "    data_slip_store AS d,\n" +
                    //IVS_TMTrong start add 2015/09/30 New request #42933
                    "    mst_supplier as ms,\n"+
                    //IVS_TMTrong end add 2015/09/28 New request #42933
                    "    data_slip_ship_detail AS sd\n" +
                    "        LEFT JOIN mst_item as i\n" +
                    "        ON i.item_id = sd.item_id,\n" +
                    "    data_slip_ship AS s\n" +
                    "WHERE\n" +
                    "    m.src_shop_id = sd.shop_id AND\n" +
                    "    m.src_slip_no = sd.slip_no AND\n" +
                    "    m.item_id = sd.item_id AND\n" +
                    "    m.delete_date IS NULL AND\n" +
                    /**/ division +
                    /**/ expression.toString() +
                    "    sd.delete_date IS NULL AND\n" +
                    /**/ self_eq_ + shop.getShopID()+ "AND\n" +
                    "    d.shop_id = m.dest_shop_id AND\n" +
                    "    d.slip_no = m.dest_slip_no AND\n" +
                    "    d.delete_date IS NULL AND\n" +
                    "    s.shop_id = m.src_shop_id AND\n" +
                    "    s.slip_no = m.src_slip_no AND\n" +
                    "    s.delete_date IS NULL\n"+
                    //IVS_TMTrong start add 2015/09/30 New request #42933
                    "    AND ms.supplier_id = d.supplier_id");
                    //IVS_TMTrong end add 2015/09/30 New request #42933
            // 表のクリア
            ((DefaultTableModel)tblDetail2.getModel()).setRowCount(0);
            
            /** 表に追加する行の項目リスト */
            Vector vec;
            int i;
            //IVS_TMTrong start add 2015/09/28 New request #42933
            lstMoveStockReportBean.clear();
            //IVS_TMTrong end add 2015/09/28 New request #42933
            while(rs.next()){
                vec = new Vector();
                // 店販業務区分
                vec.add(getUseDivisionString(rs.getInt("item_use_division")));
                //IVS_TMTrong start add 2015/09/28 New request #42933
                // 仕入先
                vec.add(rs.getString("supplier_name"));
                //IVS_TMTrong end add 2015/09/28 New request #42933
                // 分類
                vec.add(rs.getString("item_class_name"));
                // 商品名
                vec.add(rs.getString("item_name"));
                // 仕入単価
                i = rs.getInt("cost_price");
                vec.add(rs.wasNull() ? null : i);
                // 販売価格
                i = rs.getInt("price");
                vec.add(rs.wasNull() ? null : i);
                //相手方店舗名
                vec.add(rs.getString("shop_name"));
                // 出庫担当者
                vec.add(rs.getString("staff_name"));
                // 出庫日
                
                if(rs.getDate("ship_date")!=null){
                    String shipDate = new SimpleDateFormat("yyyy/MM/dd").format(rs.getDate("ship_date"));
                    vec.add(shipDate);
                }
                else{
                    vec.add("");
                }
                // 出庫数
                i = rs.getInt("out_num");
                vec.add(rs.wasNull() ? null : i);
                // 確認日
                if(rs.getDate("store_date")!=null){
                    String storeDate = new SimpleDateFormat("yyyy/MM/dd").format(rs.getDate("store_date"));
                    vec.add(storeDate);
                }
                else{
                    vec.add("");
                }
                // 確認担当者
                vec.add(rs.getString("confirmer_name"));
                
                //行を追加する
                ((DefaultTableModel)tblDetail2.getModel()).addRow(vec);
                
                //IVS_TMTrong start add 2015/09/28 New request #42933
                MoveStockReportBean moveStockReportData = new MoveStockReportBean();
                moveStockReportData.setItem_use_division(getUseDivisionString(rs.getInt("item_use_division")));
                moveStockReportData.setSupplier_name(rs.getString("supplier_name"));
                moveStockReportData.setItem_class_name(rs.getString("item_class_name"));
                moveStockReportData.setItem_name(rs.getString("item_name"));
                i = rs.getInt("cost_price");
                moveStockReportData.setCost_price(rs.wasNull()?null:i);
                i = rs.getInt("price");
                moveStockReportData.setPrice(rs.wasNull()?null:i);
                moveStockReportData.setShop_name(rs.getString("shop_name"));
                moveStockReportData.setStaff_name(rs.getString("staff_name"));
                moveStockReportData.setShip_date(rs.getDate("ship_date"));
                i = rs.getInt("out_num");
                moveStockReportData.setOut_num(rs.wasNull()?null:i);
                moveStockReportData.setStore_date(rs.getDate("store_date"));
                moveStockReportData.setConfirmer_name(rs.getString("confirmer_name"));
                
                lstMoveStockReportBean.add(moveStockReportData);
                //IVS_TMTrong end add 2015/09/28 New request #42933
            }
            rs.close();
        } catch (SQLException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }
    
    /** 明細を初期化する。 */
    private void initTblDetail2() {
        DefaultTableColumnModel model =
                (DefaultTableColumnModel)tblDetail2.getColumnModel();
        model.getColumn(0).setPreferredWidth(40);//区分
        //IVS_TMTrong start add 2015/09/28 New request #42933
         model.getColumn(1).setPreferredWidth(72);//仕入先
        //IVS_TMTrong end add 2015/09/28 New request #42933
        //IVS_TMTrong start edit 2015/09/28 New request #42933
        model.getColumn(2).setPreferredWidth(72);//分類
        model.getColumn(3).setPreferredWidth(190);//商品名
        model.getColumn(4).setPreferredWidth(53);//仕入価格
        model.getColumn(5).setPreferredWidth(53);//販売価格
        model.getColumn(6).setPreferredWidth(53);//出庫店舗
        model.getColumn(7).setPreferredWidth(64);//出庫担当者
        model.getColumn(8).setPreferredWidth(65);//出庫日
        model.getColumn(9).setPreferredWidth(40);//出庫数
        model.getColumn(10).setPreferredWidth(65);//確認日
        model.getColumn(11).setPreferredWidth(64);//確認担当者
        //IVS_TMTrong end edit 2015/09/28 New request #42933
    }
    // </editor-fold>
    
    // <editor-fold desc="出庫タブ">
    /** 商品を明細にいっこいれる */
    private void addItemToDetailTable(int itemUseDivision){

        JTableEx tblProductDetail;
        JTableEx tblProduct;

        switch (itemUseDivision){
            case 1:
                tblProductDetail = tblProductDetail1;
                tblProduct = tblProduct1;
                break;
            case 2:
                tblProductDetail = tblProductDetail2;
                tblProduct = tblProduct2;
                break;
            default:
                return;
        }

        MstItem item =(MstItem)tblProductDetail.getValueAt(tblProductDetail.getSelectedRow(),0);

        try {
            mup.setProductDivision(2);
            mup.setShop((MstShop)destination.getSelectedItem());
            mup.setProductID(item.getItemID());
            if (!mup.isExists(SystemInfo.getConnection())) {
                MessageDialog.showMessageDialog(
                    this,
                    "出庫先店舗で使用商品として登録されていません。",
                    this.getTitle(),
                    JOptionPane.WARNING_MESSAGE);

                return;
            }
            
        } catch (Exception ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        
        int rowCount = tblDetail3.getRowCount();
        
        for (int i = 0; i < rowCount; i++) {
            TableRecord tobj = (TableRecord) tblDetail3.getValueAt(i, 0);
            if (itemUseDivision == tobj.getSelectItemUseDivision() && item.getItemID() == tobj.getItemId()) {
                //あった場合、移動数に１を足す
                tblDetail3.setValueAt((Integer)tblDetail3.getValueAt(i, 6) + 1, i, 6);
                return;
            }
        }

        //なかった場合、明細行を追加する
        addOneDetailInfo(tblProductDetail, itemUseDivision);
        return;
    }
    
    /** 明細行を追加する */
    private void addOneDetailInfo(JTableEx tblProductDetail, int itemUseDivision) {
        /** 新規明細行の項目リスト */
        Vector vec = new Vector();
        
        //用途を設置する
        MstSupplierItem item = (MstSupplierItem) tblProductDetail
                .getValueAt(tblProductDetail.getSelectedRow(), 0);
        
        TableRecord rec = new TableRecord();
        rec.setItemId(item.getItemID());
        rec.setSelectItemUseDivision(itemUseDivision);
        vec.add(rec); // 区分
        
        MstItemClass mstItemClass = classes.lookup(item.getItemClass().getItemClassID());
        vec.add(mstItemClass); // 分類
        vec.add(item); // 商品名
        vec.add(item.getCostPrice()); // 仕入価格
        vec.add(item.getPrice()); // 販売価格
        
        MstShop mstShop = (MstShop) shop3.getSelectedItem();
        if (mstShop == null){
            vec.add(null);
        }else{
            vec.add(getItemStock(null, mstShop.getShopID(), item.getItemID(),
                    itemUseDivision)); // 現在数
        }
        vec.add(1); // 移動数(初期値は１)
        
        JButton btnDelete = new JButton(iconTrashOff);
        btnDelete.setPressedIcon(iconTrashOn);
        vec.add(getDeleteButton()); // 削除ボタン
        
        int rowIndex = findInsertPoint(tblDetail3, itemUseDivision, item);
        //一行を追加する
        ((DefaultTableModel)tblDetail3.getModel()).insertRow(rowIndex, vec);
    }
    
    /** 削除ボタンを取得する */
    private JButton getDeleteButton() {
        JButton btnDelete = new JButton(iconTrashOff);
        btnDelete.setPressedIcon(iconTrashOn);
        btnDelete.setBorderPainted(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setSize(48, 25);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProduct();
            }
        });
        return btnDelete;
    }
    
    /** 明細を１行削除する。 */
    private void deleteProduct() {
        //int index = this.getProductsIndex( 0 ); // 選択技術
        //DataSalesDetail dsd = ia.getSales().get( index );
        //int size = dsd.size();
        //ia.getSales().remove( index );
        // セルの編集を終わらせる
        if( tblDetail3.getCellEditor() != null ){
            tblDetail3.getCellEditor().stopCellEditing();
        }
        int row = tblDetail3.getSelectedRow();    // 選択行
        ((DefaultTableModel)tblDetail3.getModel()).removeRow(row);
        
        DefaultTableModel model = (DefaultTableModel)tblDetail3.getModel();
        
//        for( int i = 0; ( i == 0 || i < size ); i++ ){
//            model.removeRow(row);
//        }
    }

    /** 削除ボタンを取得する */
    private JButton getDirectionOutDeleteButton() {
        JButton btnDelete = new JButton(iconTrashOff);
        btnDelete.setPressedIcon(iconTrashOn);
        btnDelete.setBorderPainted(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setSize(48, 25);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteDirectionOutProduct();
            }
        });
        return btnDelete;
    }

    /** 明細を１行削除する。 */
    private void deleteDirectionOutProduct() {
        if( tblDetail1.getCellEditor() != null ){
            tblDetail1.getCellEditor().stopCellEditing();
        }
        int row = tblDetail1.getSelectedRow();    // 選択行

        ConnectionWrapper con = SystemInfo.getConnection();

        // 確認済みチェック
        try {

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select 1");
            sql.append(" from");
            sql.append("     data_move_stock");
            sql.append(" where");
            sql.append("         src_shop_id = " + SQLUtil.convertForSQL(shopIdList.get(row)));
            sql.append("     and src_slip_no = " + SQLUtil.convertForSQL(slipNoList.get(row)));
            sql.append("     and dest_slip_no is not null");
            sql.append("     and delete_date is null");

            boolean isConfirm = false;
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            isConfirm = rs.next();
            rs.close();

            if (isConfirm) {
                MessageDialog.showMessageDialog(
                        this,
                        "この商品は、すでに確認されています。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int ret = MessageDialog.showYesNoDialog(
                        this,
                        "削除します。よろしいですか？",
                        this.getTitle(),
                        JOptionPane.QUESTION_MESSAGE);

            if (ret == JOptionPane.NO_OPTION) return;

            con.begin();

            sql.setLength(0);
            sql.append(" update data_slip_ship set delete_date = current_timestamp");
            sql.append(" where shop_id = " + SQLUtil.convertForSQL(shopIdList.get(row)));
            sql.append("   and slip_no = " + SQLUtil.convertForSQL(slipNoList.get(row)));
            con.executeUpdate(sql.toString());

            sql.setLength(0);
            sql.append(" update data_slip_ship_detail set delete_date = current_timestamp");
            sql.append(" where shop_id = " + SQLUtil.convertForSQL(shopIdList.get(row)));
            sql.append("   and slip_no = " + SQLUtil.convertForSQL(slipNoList.get(row)));
            con.executeUpdate(sql.toString());

            sql.setLength(0);
            sql.append(" update data_move_stock set delete_date = current_timestamp");
            sql.append(" where src_shop_id = " + SQLUtil.convertForSQL(shopIdList.get(row)));
            sql.append("   and src_slip_no = " + SQLUtil.convertForSQL(slipNoList.get(row)));
            con.executeUpdate(sql.toString());
            
            con.commit();

            ((DefaultTableModel)tblDetail1.getModel()).removeRow(row);

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (Exception ignore) {}
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }

        this.show1();
    }

    /** 明細を初期化する。 */
    private void initTblDetail3() {
        DefaultTableColumnModel model =
                (DefaultTableColumnModel)tblDetail3.getColumnModel();
        tblDetail3.getTableHeader().setReorderingAllowed(false);
        model.getColumn(0).setPreferredWidth(40);//区分
        model.getColumn(1).setPreferredWidth(72);//分類
        model.getColumn(2).setPreferredWidth(357);//商品名
        model.getColumn(3).setPreferredWidth(60);//仕入価格
        model.getColumn(4).setPreferredWidth(60);//販売価格
        model.getColumn(5).setPreferredWidth(53);//現在数
        model.getColumn(6).setPreferredWidth(53);//移動数
        model.getColumn(7).setPreferredWidth(50);//削除
        model.getColumn(7).setResizable(false);
        model.getColumn(6).setCellEditor(new IntegerCellEditor(new JTextField()));
        SelectTableCellRenderer.setSelectTableCellRenderer(tblDetail3);
    }
    
    /** 在庫一覧 */
    private void transitToStockList(){
        StockList pnl = new StockList();
        //changeContents(pnl);
        pnl.ShowDialog(null);
        //SwingUtil.openAnchorDialog( null, true, pnl, "在庫一覧", SwingUtil.ANCHOR_LEFT|SwingUtil.ANCHOR_VCENTER );
    }
    
    /** 出庫 */
    private void dispatch(){

        int srcId;
        int destId;

        /** 出庫元店舗 */
        MstShop srcShop = (MstShop)shop3.getSelectedItem();
        if(srcShop == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelShop3.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            shop3.requestFocusInWindow();
            return;
        }
        srcId = srcShop.getShopID();
        
        /** 入庫先店舗 */
        MstShop destShop = (MstShop)destination.getSelectedItem();
        if(destShop == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "対象店舗"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            destination.requestFocusInWindow();
            return;
        }
        destId = destShop.getShopID();
        
        /** 出庫担当者ID */
        Integer dispatcherStaffId = null;
        if(dispatcherName3.getSelectedIndex() > 0){
            dispatcherStaffId =
                    ((MstStaff)dispatcherName3.getSelectedItem()).getStaffID();
        }
        if(dispatcherStaffId == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(
                    MessageUtil.ERROR_INPUT_EMPTY,
                    labelDispatcher3.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            dispatcherName3.requestFocusInWindow();
            return;
        }
        // セルの編集を終わらせる
        if( tblDetail3.getCellEditor() != null ){
            tblDetail3.getCellEditor().stopCellEditing();
        }
        // 移動数のチェック
        /** 行数 */
        int rowCount = tblDetail3.getRowCount();
        for(int i = 0; i < rowCount; i++){
            Integer stockNum = (Integer)tblDetail3.getValueAt(i, 5);
            Integer outNum = (Integer)tblDetail3.getValueAt(i, 6);
            if(outNum == null || outNum < 1){
                MessageDialog.showMessageDialog(this,
                        "移動数に０以下が入力されています。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }else if(stockNum == null || outNum > stockNum){
                MessageDialog.showMessageDialog(this,
                        "移動数が現在数を超えています。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // 入力チェック終わり
        
        if (MessageDialog.showYesNoDialog(this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_REGIST, "出庫伝票"),
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        
        try{
            ConnectionWrapper con = SystemInfo.getConnection();
            
            int slipNoMax = getMaxSrcSlipNo(con, srcId);
            if(slipNoMax == Integer.MAX_VALUE){
                MessageDialog.showMessageDialog(this,
                        "伝票番号が登録可能な最大の番号を超えるため登録できませんでした。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // トランザクション開始
                con.begin();
                /** 仕入先ID→出庫伝票番号のマップ */
                HashMap<Integer, Integer> slipNoMap
                        = new HashMap<Integer, Integer>();
                /** 仕入先ID→出庫伝票詳細番号のマップ */
                HashMap<Integer, Integer> slipDetailNoMap
                        = new HashMap<Integer, Integer>();
                /** 出庫元伝票詳細 */
                DataSlipShipDetail sd;
                /** アイテム */
                MstSupplierItem item;
                /** 仕入れ先ID */
                int supplierId;
                /** 伝票番号 */
                int slipNo;
                /** 伝票詳細番号 */
                int slipDetailNo;
                
                for (int i = 0; i < rowCount; i++) {
                    item = (MstSupplierItem)tblDetail3.getValueAt(i, 2);
                    supplierId = item.getSupplier().getSupplierID();
                    if(slipNoMap.containsKey(supplierId)){
                        slipNo = slipNoMap.get(supplierId);
                        slipDetailNo = slipDetailNoMap.get(supplierId);
                        if (slipDetailNo == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "伝票詳細番号が登録可能な最大の番号を超えるため登録できませんでした。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipDetailNoMap.put(supplierId, ++slipDetailNo);
                    }else{
                        if (slipNoMax == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "伝票番号が登録可能な最大の番号を超えるため登録できませんでした。",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipNoMap.put(supplierId, slipNo = ++slipNoMax);
                        slipDetailNoMap.put(supplierId, slipDetailNo = 1);
                        // 出庫伝票を登録する
                        insertDataSlipShip(con, srcId, slipNo, supplierId, dispatcherStaffId);
                    }
                    
                    // 出庫伝票詳細を登録する
                    insertDataSlipShip(con, srcId, slipNo, slipDetailNo,
                            item, (Integer)tblDetail3.getValueAt(i, 6),
                            ((TableRecord)tblDetail3.getValueAt(i, 0)).getSelectItemUseDivision());
                    // 店舗間移動レコードを登録する
                    insertDataMoveStock(con, srcId, slipNo, item.getItemID(), destId);
                }
                
                // コミット
                con.commit();
                
                /** 在庫数を再表示する */
                showItemStock(con);
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.close();
            }
        } catch (RuntimeException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "出庫伝票"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    /** 在庫数を再表示する */
    private void showItemStock(ConnectionWrapper con) {
        if(con == null){
            con = SystemInfo.getConnection();
        }
        MstShop mstShop = (MstShop) shop3.getSelectedItem();
        if (mstShop == null){
            return;
        }
        int rowCount = tblDetail3.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            TableRecord r = (TableRecord) tblDetail3.getValueAt(i, 0);
            tblDetail3.setValueAt(getItemStock(con,mstShop.getShopID(),
                    r.getItemId(), r.getSelectItemUseDivision()), i, 5);
        }
    }
    
    /** 在庫数を取得する */
    private Integer getItemStock(ConnectionWrapper con,
            int shopId, int itemId , int itemDiv) {
        Integer stock = null;
        if(con == null){
            con = SystemInfo.getConnection();
        }
        try {
            ResultSetWrapper rs = con.executeQuery(
                    "SELECT get_item_stock("
                    + shopId + ","
                    + itemId + ","
                    + itemDiv + ", CAST(current_timestamp AS timestamp without time zone))"
                    );
            if(rs.next()){
                stock = rs.getInt(1);
                stock = rs.wasNull() ? null : stock;
            }
            rs.close();
        } catch (SQLException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return stock;
    }
    
    /**現在登録されている最大の出庫伝票番号を取得する
     *@param con コネクション
     *@param shopId 店舗ID
     *@throws SQLException
     */
    private int getMaxSrcSlipNo(ConnectionWrapper con, int shopId) throws SQLException {
        
        int maxSlipNo = 0;
        
        String sql = "select coalesce(max(slip_no), 0) from data_slip_ship where shop_id = " + shopId;
        ResultSetWrapper rs = con.executeQuery(sql);
        
        if(rs.next()){
            maxSlipNo = rs.getInt(1);
        }
        rs.close();
        return maxSlipNo;
    }
    
    /**現在登録されている最大の入庫伝票番号を取得する
     *@param con コネクション
     *@param shopId 店舗ID
     *@throws SQLException
     */
    private int getMaxDestSlipNo(ConnectionWrapper con, int shopId) throws SQLException {

        int maxSlipNo = 0;
        String sql = "select coalesce(max(slip_no), 0) from data_slip_store where shop_id = " + shopId;
        ResultSetWrapper rs = con.executeQuery(sql);
        
        if(rs.next()){
            maxSlipNo = rs.getInt(1);
        }
        rs.close();
        return maxSlipNo;
    }
    
    /** 出庫伝票を登録する
     *@param con コネクション
     *@param shopId 出庫側店舗ID
     *@param slipNo 出庫側伝票番号
     *@param supplierId 仕入先ID
     *@param staffId 出庫担当者ID
     */
    private void insertDataSlipShip(ConnectionWrapper con,
            int shopId, int slipNo, int supplierId, int staffId) throws SQLException{
        DataSlipShip s = new DataSlipShip();
        s.setShopId(shopId); // 店舗ID
        s.setSlipNo(slipNo); // 伝票番号
        s.setSupplierId(supplierId); // 仕入先ID
        s.setShipDate(new Date()); // 出庫日
        s.setStaffId(staffId); // 出庫担当者ID
        s.insert(con);
    }
    
    /** 出庫伝票詳細を登録する
     *@param con コネクション
     *@param shopId 出庫側店舗ID
     *@param slipNo 出庫側伝票番号
     *@param slipDetailNo 伝票詳細番号
     *@param item 商品
     *@param outNum 出庫数
     *@param useDivision 店販業務区分
     */
    private void insertDataSlipShip(ConnectionWrapper con,
            int shopId, int slipNo, int slipDetailNo,
            MstSupplierItem item, int outNum, int useDivision) throws SQLException{
        DataSlipShipDetail sd = new DataSlipShipDetail();
        sd.setShopId(shopId); // 出庫側店舗ID
        sd.setSlipNo(slipNo); // 伝票番号
        sd.setSlipDetailNo(slipDetailNo); // 伝票詳細番号
        sd.setItemId(item.getItemID()); // 商品ID
        sd.setItemUseDivision(useDivision); // 店販業務区分
        sd.setOutNum(outNum); // 出庫数
        sd.setCostPrice(item.getCostPrice()); // 仕入単価
        sd.setOutClass(6); // 出庫区分 6:店舗間移動
        sd.insert(con);
    }
    
    /** 店舗間移動レコードを登録する
     *@param con コネクション
     *@param shopId 出庫側店舗ID
     *@param slipNo 出庫側伝票番号
     *@param itemId アイテムID
     */
    private boolean insertDataMoveStock(ConnectionWrapper con,
            int shopId, int slipNo, int itemId, int destId) throws SQLException{
        if(con.executeUpdate(
                "INSERT INTO data_move_stock (\n" +
                "    src_shop_id,\n" +
                "    src_slip_no,\n" +
                "    item_id,\n" +
                "    dest_shop_id,\n" +
                "    insert_date,\n" +
                "    update_date)\n" +
                "VALUES (\n" +
                "    " + shopId + ",\n" +
                "    " + slipNo + ",\n" +
                "    " + itemId + ",\n" +
                "    " + destId + ",\n" +
                "    current_timestamp,\n" +
                "    current_timestamp)\n") != 1) {
            return false;
        }
        return true;
    }
    
    /** 店舗間移動レコードを更新する
     *@param con コネクション
     *@param srcShopId 出庫側店舗ID
     *@param srcSlipNo 出庫伝票番号
     *@param itemId 商品ID
     *@param destShopId 入庫側店舗ID
     *@param destSlipNo 入庫伝票番号
     */
    private boolean updateDataMoveStock(
            ConnectionWrapper con, int srcShopId, int srcSlipNo, int itemId,
            int destShopId, int destSlipNo) throws SQLException{
        if(con.executeUpdate(
                "UPDATE data_move_stock\n" +
                "SET\n" +
                "    dest_shop_id = " + destShopId + ",\n" +
                "    dest_slip_no = " + destSlipNo + ",\n" +
                "    update_date = current_timestamp\n" +
                "WHERE\n" +
                "    src_shop_id = " + srcShopId + " AND\n" +
                "    src_slip_no = " + srcSlipNo + " AND\n" +
                "    item_id = " + itemId) != 1){
            return false;
        }
        return true;
    }
    
    /** 明細行の挿入場所を取得する */
    private int findInsertPoint(JTableEx tblDetail3, int itemUseDivision, MstItem item) {
        int itemClassDispSeq = classes.lookup(item.getItemClass().getItemClassID()).getDisplaySeq();
        int itemDispSeq = item.getDisplaySeq();
        /** 行数 */
        int rowCount = tblDetail3.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int rowItemUseDivision = ((TableRecord) tblDetail3.getValueAt(i, 0)).getSelectItemUseDivision();
            if (rowItemUseDivision > itemUseDivision) {
                return i;
            } else if (rowItemUseDivision == itemUseDivision) {
                MstItem rowItem = (MstItem) tblDetail3.getValueAt(i, 2);
                MstItemClass cls = classes.lookup(rowItem.getItemClass().getItemClassID());
                if (cls.getDisplaySeq() > itemClassDispSeq) {
                    return i;
                } else if (cls.getDisplaySeq() == itemClassDispSeq) {
                    if (rowItem.getDisplaySeq() > itemDispSeq) {
                        return i;
                    }
                }
            }
        }
        return rowCount;
    }
    
    /** 商品リストを更新する */
    private void tblProductDoClick(int use_division) {
        JTableEx tblProduct;
        JTableEx tblProductDetail;
        switch (use_division){
            case 1:
                tblProduct = tblProduct1;
                tblProductDetail = tblProductDetail1;
                break;
            case 2:
                tblProduct = tblProduct2;
                tblProductDetail = tblProductDetail2;
                break;
            default:
                return;
        }
        
        //商品リストを初期化する
        removeRowAll(tblProductDetail);
        
        if (tblProduct.getSelectedRow() < 0) {
            return;
        }
        
        MstItemClass mic = (MstItemClass)tblProduct
                .getValueAt(tblProduct.getSelectedRow(),0);
        
        if (mic == null) {
            return;
        }
        try {
            MstSupplierItemList msis = new MstSupplierItemList(
                    mic.getItemClassID(), use_division, SystemInfo.getConnection());
            for (int i = 0; i < msis.size(); i++) {
                if (i == tblProductDetail.getModel().getRowCount()) {
                    ((DefaultTableModel)tblProductDetail.getModel()).addRow(new Vector());
                }
                tblProductDetail.setValueAt(msis.get(i), i, 0) ;
            }
        } catch (RuntimeException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
// </editor-fold>
    
    /** JTableの全ての行を削除する。 */
    private void removeRowAll(JTableEx tbl) {
        DefaultTableModel model = (DefaultTableModel)tbl.getModel();
        int rowCount =  model.getRowCount();
        for (int i = rowCount; i > 0;) {
            model.removeRow(--i);
        }
    }
    
    /** FocusTraversalPolicy */
    private FocusTraversalPolicy traversalPolicy =
            new MoveStockPanelFocusTraversalPolicy();
    
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return	traversalPolicy;
    }
    
    /** ボタンマウスオーバー時のマウスポインタを設定 */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnRegist);
        SystemInfo.addMouseCursorChange(btnShow1);
        SystemInfo.addMouseCursorChange(btnShow2);
        SystemInfo.addMouseCursorChange(btnStockList);
        SystemInfo.addMouseCursorChange(btnDispatch);
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
    
    /** 区分・商品ペア（区分表示用） */
    private static class TableRecord {
        /** 商品区分 */
        private int selectItemUseDivision;
        /** アイテムID */
        private Integer itemId;
        
        /** 商品区分を取得する */
        public int getSelectItemUseDivision() {return selectItemUseDivision;}
        
        /** アイテムIDを取得する */
        public Integer getItemId() {return itemId;}
        
        /** 商品区分を設定する */
        public void setSelectItemUseDivision(int itemUseDivision) {
            this.selectItemUseDivision = itemUseDivision;
        }
        
        /** アイテムIDを設定する */
        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }
        
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof TableRecord)) {
                return false;
            }
            
            TableRecord r = (TableRecord) obj;
            return selectItemUseDivision == r.getSelectItemUseDivision() &&
                    itemId.equals(r.getItemId());
        }
        
        public String toString() {
            return getUseDivisionString(selectItemUseDivision);
        }
    }
    
    /** 店販業務区分の文字列を */
    private static String getUseDivisionString(int useDivision) {
        switch (useDivision) {
            case 1:
                return "店販用";
            case 2:
                return "業務用";
            case 3:
                return "店販＆業務用";
            default:
                return "";
        }
    }
    
    /** 商品用プライベートクラス */
    private class MstSupplierItemList extends ArrayList<MstSupplierItem> {
        /** コンストラクタ */
        public MstSupplierItemList(
                int item_class_id,
                int item_use_division,
                ConnectionWrapper con)throws SQLException{
            ResultSetWrapper rs = con.executeQuery(
                    "SELECT msi.*,mi.*\n" +
                    //IVS_LVTu start edit 2015/09/03 New request #42445 
                    //"FROM mst_item mi, mst_supplier_item msi\n" +
                    " FROM mst_item mi " +
                    " inner join mst_supplier_item msi on mi.item_id = msi.item_id " +
                    " inner join mst_use_product mup on mi.item_id = mup.product_id  " +
                    " and mup.product_division = 2  " +
                    " and mup.delete_date IS NULL " +
                    " and mup.shop_id = " + SQLUtil.convertForSQL( ((MstShop)shop3.getSelectedItem()).getShopID()) + "\n" +
                    "WHERE\n" +
                    " mi.item_id = msi.item_id\n" +
                    " AND mi.item_class_id = " + SQLUtil.convertForSQL(item_class_id) + "\n" +
                    " AND mi.item_use_division IN (3, " + SQLUtil.convertForSQL(item_use_division) + ")\n" +
                    " AND msi.delete_date IS NULL\n" +
   
                    //" AND mi.delete_date IS NULL\n");
                    " AND mi.delete_date IS NULL\n" +
                    " order by mup.display_seq, mi.item_id\n");
                    //IVS_LVTu start edit 2015/09/03 New request #42445
            while(rs.next()) {
                MstSupplierItem msi = new MstSupplierItem();
                msi.setData(rs);
                msi.setCostPrice(rs.getInt("cost_price"));
                this.add(msi);
            }
            rs.close();
//            } catch (SQLException ex) {
//                SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
//            }
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
    private class MoveStockPanelFocusTraversalPolicy extends FocusTraversalPolicy {
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
         */
        public Component getComponentAfter(Container aContainer, Component aComponent) {
//            if (aContainer.equals(panelUnfinished)) {
            if(aComponent.equals(shop1)){
                 if(rdoDivisionAll1.isSelected()){
                     return rdoDivisionAll1;
                 }else if(rdoDivisionSale1.isSelected()){
                     return rdoDivisionSale1;
                 }return rdoDivisionWork1;
            }else if(aComponent.equals(rdoDivisionAll1)||
                    aComponent.equals(rdoDivisionSale1)||
                    aComponent.equals(rdoDivisionWork1)){
                return confirmDate;
            }else if(aComponent.equals(confirmDate)){
               if(rdoDirectionIn1.isSelected()){
                   return rdoDirectionIn1;
               } return rdoDirectionOut1;
            }else if(aComponent.equals(rdoDirectionIn1) ||
                    aComponent.equals(rdoDirectionOut1) ){
                return confirmerCode1;
            }else if(aComponent.equals(confirmerCode1)){
                return confirmerName1;
            }else if(aComponent.equals(confirmerName1)){
                if(shop1.getItemCount()==1){
                    if(rdoDivisionAll1.isSelected()){
                         return rdoDivisionAll1;
                    }else if(rdoDivisionSale1.isSelected()){
                         return rdoDivisionSale1;
                    }return rdoDivisionWork1;
                }
                return shop1;
            }
            if(aComponent.equals(shop2)){
                return receivedDateFrom;
            }else if(aComponent.equals(receivedDateFrom)){
                return receivedDateTo;
            }else if(aComponent.equals(receivedDateTo)){
                return confirmerCode2;
            }else if(aComponent.equals(confirmerCode2)){
                return confirmerName2;
            }else if(aComponent.equals(confirmerName2)){
                return sendDateFrom;
            }else if(aComponent.equals(sendDateFrom)){
                return sendDateTo;
            }else if(aComponent.equals(sendDateTo)){
                return dispatcherCode2;
            }else if(aComponent.equals(dispatcherCode2)){
                return dispatcherName2;
            }else if(aComponent.equals(dispatcherName2)){
                return shopOther;
            }else if(aComponent.equals(shopOther)){
                if(rdoDivisionAll2.isSelected()){
                    return rdoDivisionAll2;
                }else if(rdoDivisionSale2.isSelected()){
                    return rdoDivisionSale2;
                }return rdoDivisionWork2;
            }else if(aComponent.equals(rdoDivisionAll2) ||
                    aComponent.equals(rdoDivisionSale2)||
                    aComponent.equals(rdoDivisionWork2)){
                 if(rdoDirectionIn2.isSelected()){
                     return rdoDirectionIn2;
                 }return rdoDirectionOut2;
            }else if(aComponent.equals(rdoDirectionIn2) || aComponent.equals(rdoDirectionOut2) ){
                if(shop2.getItemCount() ==1){
                    return receivedDateFrom;
                }
                return shop2;
            }

            if(aComponent.equals(shop3)){
                return dispatcherCode3;
            }else if(aComponent.equals(dispatcherCode3)){
                return dispatcherName3;
            }else if(aComponent.equals(dispatcherName3)){
                return destination;
            }else if(aComponent.equals(destination)){
                if(shop3.getItemCount()==1)
                {
                    return dispatcherCode3;
                }
                return shop3;
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
//            if (aContainer.equals(panelUnfinished)) {
            if(aComponent.equals(confirmerName1)){
                return btnRegist;
            }else if(aComponent.equals(confirmerCode1)){
                return btnRegist;
            }else if(aComponent.equals(btnRegist)){
                return btnShow1;
            }else if(aComponent.equals(btnShow1)){
                return rdoDirectionOut1;
            }else if(aComponent.equals(rdoDirectionOut1)){
                return rdoDirectionIn1;
            }else if(aComponent.equals(rdoDirectionIn1)){
                return rdoDivisionWork1;
            }else if(aComponent.equals(rdoDivisionWork1)){
                return rdoDivisionSale1;
            }else if(aComponent.equals(rdoDivisionSale1)){
                return rdoDivisionAll1;
            }else if(aComponent.equals(rdoDivisionAll1)){
                return confirmDate;
            }else if(aComponent.equals(confirmDate)){
                return shop1;
            }else if(aComponent.equals(shop1)){
                return confirmerCode1;
            }
//            } else if(aContainer.equals(panelFinished)){
            if(aComponent.equals(btnShow2)){
                return rdoDirectionOut2;
            }else if(aComponent.equals(rdoDirectionOut2)){
                return rdoDirectionIn2;
            }else if(aComponent.equals(rdoDirectionIn2)){
                return rdoDivisionWork2;
            }else if(aComponent.equals(rdoDivisionWork2)){
                return rdoDivisionSale2;
            }else if(aComponent.equals(rdoDivisionSale2)){
                return rdoDivisionAll2;
            }else if(aComponent.equals(rdoDivisionAll2)){
                return shopOther;
            }else if(aComponent.equals(shopOther)){
                return confirmerCode2;
            }else if(aComponent.equals(confirmerName2)){
                return dispatcherCode2;
            }else if(aComponent.equals(confirmerCode2)){
                return dispatcherCode2;
            }else if(aComponent.equals(dispatcherName2)){
                return sendDateTo;
            }else if(aComponent.equals(dispatcherCode2)){
                return sendDateTo;
            }else if(aComponent.equals(sendDateTo)){
                return sendDateFrom;
            }else if(aComponent.equals(sendDateFrom)){
                return receivedDateTo;
            }else if(aComponent.equals(receivedDateTo)){
                return receivedDateFrom;
            }else if(aComponent.equals(receivedDateFrom)){
                return shop2;
            }else if(aComponent.equals(shop2)){
                return btnShow2;
            }
//            } else if(aContainer.equals(panelStart)){
            if(aComponent.equals(jTabbedPane1)){
                return btnDispatch;
            }else if(aComponent.equals(btnDispatch)){
                return btnStockList;
            }else if(aComponent.equals(btnStockList)){
                return destination;
            }else if(aComponent.equals(destination)){
                return dispatcherCode3;
            }else if(aComponent.equals(dispatcherName3)){
                return shop3;
            }else if(aComponent.equals(dispatcherCode3)){
                return shop3;
            }else if(aComponent.equals(shop3)){
                return jTabbedPane1;
            }
//            }
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
            if(aContainer.equals(panelUnfinished)){
                return confirmerCode1;
            }else if(aContainer.equals(panelFinished)){
                return btnShow2;
            }else if(aContainer.equals(panelStart)){
                return btnDispatch;
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
            if(aContainer.equals(panelUnfinished)){
                return shop1;
            }else if(aContainer.equals(panelFinished)){
                return shop2;
            }else if(aContainer.equals(panelStart)){
                return shop3;
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
            return shop1;
        }
    }
    //IVS_TMTrong start add 2015/09/28 New request #42933
    private void printMoveStock(){
        JExcelApi jx = new JExcelApi("店舗間移動一覧.xls");
        jx.setTemplateFile("/reports/店舗間移動一覧.xls");
        jx.setTargetSheet(1);
        
        String shopName = null;
        if (shop2.getSelectedItem() instanceof MstGroup) {
            //グループ
            MstGroup mg = (MstGroup) shop2.getSelectedItem();
            shopName = mg.getGroupName();
        } else {
            // 店舗
            MstShop ms = (MstShop) shop2.getSelectedItem();
            shopName = ms.getShopName();
        }
        // ヘッダ
        jx.setValue(2, 3, shopName);
        //IVS_TMTrong start add 2015/09/30 New request #42933
        if(receivedDateFrom.getDateStr() == null && receivedDateTo.getDateStr() == null){
            jx.setValue(2, 4, "");
        }else{
            jx.setValue(2, 4, receivedDateFrom.getDateStr("/") + " ～ " + receivedDateTo.getDateStr("/"));
        }
        //IVS_TMTrong end add 2015/09/30 New request #42933
        
        if(sendDateFrom.getDateStr() == null && sendDateTo.getDateStr() == null){
            jx.setValue(2, 5, "");
        }else{
            jx.setValue(2, 5, sendDateFrom.getDateStr("/") + " ～ " + sendDateTo.getDateStr("/"));
        }
        
        if(shopOther.getSelectedIndex()>0){
            jx.setValue(2, 6, ((MstShop)shopOther.getSelectedItem()).getShopName());
        }
        else{
            jx.setValue(2, 6, "");
        }
        
        String CategoryName=null;
        if(rdoDivisionAll2.isSelected()){
            CategoryName="全て";
        }else if(rdoDivisionSale2.isSelected()){
            CategoryName="店販用";
        }else if(rdoDivisionWork2.isSelected()){
            CategoryName="業務用";
        }
        
        jx.setValue(2, 7, CategoryName);
        jx.setValue(2, 8, rdoDirectionIn2.isSelected()?"入庫":"出庫");
        //IVS_TMTrong start add 2015/09/30 New request #42933
        String confirmerName2StaffName = ((MstStaff)confirmerName2.getSelectedItem()).getStaffName()[0] +" "+
                ((MstStaff)confirmerName2.getSelectedItem()).getStaffName()[1];
        String dispatcherName2StaffName = ((MstStaff)dispatcherName2.getSelectedItem()).getStaffName()[0] +" "+
                ((MstStaff)dispatcherName2.getSelectedItem()).getStaffName()[1];
        //IVS_TMTrong end add 2015/09/30 New request #42933
        if(confirmerName2.getSelectedIndex()>0){
            //IVS_TMTrong start add 2015/09/30 New request #42933
            //jx.setValue(5, 4, ((MstStaff)confirmerName2.getSelectedItem()).getStaffNo());
            jx.setValue(5, 4, confirmerName2StaffName);
            //IVS_TMTrong start add 2015/09/30 New request #42933
        }
        else{
            jx.setValue(5, 4, "");
        }
        
        if(dispatcherName2.getSelectedIndex()>0){
            //IVS_TMTrong start add 2015/09/30 New request #42933
            //jx.setValue(5, 5, ((MstStaff)dispatcherName2.getSelectedItem()).getStaffNo());
            jx.setValue(5, 5, dispatcherName2StaffName);
            //IVS_TMTrong end add 2015/09/30 New request #42933
        }
        else{
            jx.setValue(5, 5, "");
        }
        
        //IVS_TMTrong start add 2015/09/30 New request #42933
        if(rdoDirectionIn2.isSelected()){
            jx.setValue(1, 6, "出庫店舗：");
        }else{
            jx.setValue(1, 6, "入庫店舗：");
        }
        //IVS_TMTrong end add 2015/09/30 New request #42933
        
        if(rdoDirectionIn2.isSelected()){
           jx.setValue(7, 10, "出庫店舗");
        }else if(rdoDirectionOut2.isSelected()){
            jx.setValue(7, 10, "入庫店舗");
        }
        
        int row = 11;
        //IVS_TMTrong start add 2015/09/29 New request #42933
        jx.insertRow(row, lstMoveStockReportBean.size()-1);
        //IVS_TMTrong end add 2015/09/29 New request #42933
        // データセット
        for (MoveStockReportBean item : lstMoveStockReportBean) {
            // 店販業務区分
            jx.setValue(1, row, item.getItem_use_division());
            // 仕入先
            jx.setValue(2, row, item.getSupplier_name());
            // 分類
            jx.setValue(3, row, item.getItem_class_name());
            // 商品名
            jx.setValue(4, row, item.getItem_name());
            // 仕入単価
            jx.setValue(5, row, item.getCost_price());
            // 販売価格
            jx.setValue(6, row, item.getPrice());
            //相手方店舗名
            jx.setValue(7, row, item.getShop_name());
            // 出庫担当者
            jx.setValue(8, row, item.getStaff_name());
            // 出庫日
            jx.setValue(9, row, item.getShip_date());
            // 出庫数
            jx.setValue(10, row, item.getOut_num());
            // 確認日
            jx.setValue(11, row, item.getStore_date());
            // 確認担当者
            jx.setValue(12, row, item.getConfirmer_name());
            row++;
        }  
        // 横、縦 1ページに印刷
        jx.getTargetSheet().getSettings().setFitWidth(1);
        jx.getTargetSheet().getSettings().setFitHeight(1);

        jx.openWorkbook();
    }
    //IVS_TMTrong end add 2015/09/28 New request #42933
}
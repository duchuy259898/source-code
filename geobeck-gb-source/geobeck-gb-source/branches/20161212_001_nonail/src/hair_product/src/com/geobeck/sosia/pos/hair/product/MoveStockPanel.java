/*
 * MoveStockPanel.java
 *
 * Created on 2008/10/20
 */

package com.geobeck.sosia.pos.hair.product;
// <editor-fold defaultstate="collapsed" desc="�C���|�[�g��">
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
/** ���i�X�܊Ԉړ��p�l�� */
public class MoveStockPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private enum SearchMode { NONE, SLIP_SHIP };
    private MstItemClasses classes = new MstItemClasses();
    private MstSuppliers mss = new MstSuppliers();
    private MstUseProduct mup = new MstUseProduct();
    //IVS_TMTrong start add 2015/09/28 New request #42933
    public List<MoveStockReportBean> lstMoveStockReportBean= new ArrayList<MoveStockReportBean>();
    //IVS_TMTrong end add 2015/09/28 New request #42933
    
    /** �폜�{�^��On�A�C�R�� */
    ImageIcon iconTrashOn =
            SystemInfo.getImageIcon("/button/common/trash_on.jpg");
    /** �폜�{�^��Off�A�C�R�� */
    ImageIcon iconTrashOff =
            SystemInfo.getImageIcon("/button/common/trash_off.jpg");
    
    /** �R���X�g���N�^ */
    public MoveStockPanel() {
        this.setSize(834, 691);
        initComponents();
        this.setPath("���i�Ǘ�");
        this.setTitle("�X�܊Ԉړ�");
        init();
        shop1.requestFocusInWindow();
    }
    
    /* �R���{�{�b�N�X�̓��e�������Ȃ� */
    private void init() {

        SystemInfo.initGroupShopComponents(shop1, 2);
        SystemInfo.initGroupShopComponents(shop2, 2);
        SystemInfo.initGroupShopComponents(shop3, 2);
        shopOther.addItem(null);
        SystemInfo.getGroup().addGroupDataToJComboBox(shopOther, 2);
        SystemInfo.getGroup().addGroupDataToJComboBox(destination, 2);
        // �X�^�b�t�I���R���{�{�b�N�X�̐擪�ɋ�̃X�^�b�t��ݒ�
        confirmerName1.addItem(new MstStaff());
        confirmerName2.addItem(new MstStaff());
        dispatcherName2.addItem(new MstStaff());
        dispatcherName3.addItem(new MstStaff());
        // �X�^�b�t�I���R���{�{�b�N�X�̃X�^�b�t��ǉ�
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
        
        //�O��I�������Z�b�g����
        setInventoryDate(SystemInfo.getConnection());
        
    }
    
    private void setInventoryDate(ConnectionWrapper con) {

        inventoryDateGyoumu.setText("");
        inventoryDateTenpan.setText("");

        try {

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      to_char(max(case when inventory_division = 2 then inventory_date end), 'yyyy�NMM��dd��') as inventoryDateGyoumu");
            sql.append("     ,to_char(max(case when inventory_division = 1 then inventory_date end), 'yyyy�NMM��dd��') as inventoryDateTenpan");
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
    
    /** Enter�������t�H�[�J�X�ړ��̐ݒ� */
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
    
    /** �t�H�[�J�X�擾���e�L�X�g�S�I���̐ݒ� */
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
        
        //�ҏW�R����������ݒu����
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

        labelShop1.setText("�X��");

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

        labelConfirmDate.setText("�m�F��");

        confirmDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        confirmDate.setDate(new java.util.Date());

        labelDivision1.setText("���i�敪");

        DivisionGroup1.add(rdoDivisionAll1);
        rdoDivisionAll1.setSelected(true);
        rdoDivisionAll1.setText("�S��");
        rdoDivisionAll1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDivisionAll1.setContentAreaFilled(false);
        rdoDivisionAll1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        DivisionGroup1.add(rdoDivisionSale1);
        rdoDivisionSale1.setText("�X�̗p");
        rdoDivisionSale1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDivisionSale1.setContentAreaFilled(false);
        rdoDivisionSale1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        DivisionGroup1.add(rdoDivisionWork1);
        rdoDivisionWork1.setText("�Ɩ��p");
        rdoDivisionWork1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDivisionWork1.setContentAreaFilled(false);
        rdoDivisionWork1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        labelDirection1.setText("�o�ɋ敪");

        DirectionGroup1.add(rdoDirectionIn1);
        rdoDirectionIn1.setSelected(true);
        rdoDirectionIn1.setText("����");
        rdoDirectionIn1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoDirectionIn1.setContentAreaFilled(false);
        rdoDirectionIn1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        DirectionGroup1.add(rdoDirectionOut1);
        rdoDirectionOut1.setText("�o��");
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

        labelConfirmer1.setText("�m�F�S����");

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
                "�敪", "����", "���i��", "�d�����i", "�̔����i", "�o�Ɍ��X��", "�o�ɒS����", "�o�ɓ�", "���ɑO�݌�", "���ɐ�", "���݌�", "�m�F"
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

            jTabbedPaneRoot.addTab("�@�������@", panelUnfinished);

            panelFinished.setOpaque(false);

            labelShop2.setText("�X��");

            shop2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            labelReceivedDate.setText("�m�F���t");

            receivedDateFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            receivedDateFrom.setDate(new java.util.Date());

            tilde1.setText("�`");

            receivedDateTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            receivedDateTo.setDate(new java.util.Date());

            labelSendDate.setText("�o�Ɋ���");

            sendDateFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            sendDateFrom.setDate(new java.util.Date());
            sendDateFrom.setDate((Date)null);

            tilde2.setText("�`");

            sendDateTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            sendDateTo.setDate(new java.util.Date());
            sendDateTo.setDate((Date)null);

            labelShopOther.setText("�o�ɓX��");

            shopOther.setMaximumRowCount(12);

            labelDivision2.setText("���i�敪");

            DivisionGroup2.add(rdoDivisionAll2);
            rdoDivisionAll2.setSelected(true);
            rdoDivisionAll2.setText("�S��");
            rdoDivisionAll2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDivisionAll2.setContentAreaFilled(false);
            rdoDivisionAll2.setMargin(new java.awt.Insets(0, 0, 0, 0));

            DivisionGroup2.add(rdoDivisionSale2);
            rdoDivisionSale2.setText("�X�̗p");
            rdoDivisionSale2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDivisionSale2.setContentAreaFilled(false);
            rdoDivisionSale2.setMargin(new java.awt.Insets(0, 0, 0, 0));

            DivisionGroup2.add(rdoDivisionWork2);
            rdoDivisionWork2.setText("�Ɩ��p");
            rdoDivisionWork2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDivisionWork2.setContentAreaFilled(false);
            rdoDivisionWork2.setMargin(new java.awt.Insets(0, 0, 0, 0));

            labelDirection2.setText("�o�ɋ敪");

            DirectionGroup2.add(rdoDirectionIn2);
            rdoDirectionIn2.setSelected(true);
            rdoDirectionIn2.setText("����");
            rdoDirectionIn2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdoDirectionIn2.setContentAreaFilled(false);
            rdoDirectionIn2.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rdoDirectionIn2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rdoDirectionIn2ActionPerformed(evt);
                }
            });

            DirectionGroup2.add(rdoDirectionOut2);
            rdoDirectionOut2.setText("�o��");
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

            labelDispatcher2.setText("�o�ɒS����");

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

            labelConfirmer2.setText("�m�F�S����");

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
                    "�敪", "�d����", "����", "���i��", "�d�����i", "�̔����i", "�o�ɓX��", "�o�ɒS����", "�o�ɓ�", "�o�ɐ�", "�m�F��", "�m�F�S����"
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

            jTabbedPaneRoot.addTab("�@�����@", panelFinished);

            panelStart.setOpaque(false);

            labelShop3.setText("�X��");

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

            labelDestination.setText("�ΏۓX��");
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
                    "���i����"
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
                    "���i��"
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

            jTabbedPane1.addTab("�X�̗p", panelForShopSale);

            paneProduct2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblProductDetail2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "���i��"
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
                    "���i����"
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

            jTabbedPane1.addTab("�Ɩ��p", panelForBuiness);

            panelDetail3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblDetail3.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "�敪", "����", "���i��", "�d�����i", "�̔����i", "���ݐ�", "�ړ���", ""
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

            labelDispatcher3.setText("�o�ɒS����");

            jPanel1.setOpaque(false);

            jLabel6.setForeground(java.awt.Color.red);
            jLabel6.setText("�y�O��I�����z");

            jLabel9.setForeground(java.awt.Color.red);
            jLabel9.setText("�Ɩ��p �F");

            inventoryDateGyoumu.setForeground(java.awt.Color.red);
            inventoryDateGyoumu.setText("2008�N12��31��");

            jLabel8.setForeground(java.awt.Color.red);
            jLabel8.setText("�X�̗p �F");

            inventoryDateTenpan.setForeground(java.awt.Color.red);
            inventoryDateTenpan.setText("2008�N12��31��");

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

            jTabbedPaneRoot.addTab("�@�o�Ɂ@", panelStart);

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

    // <editor-fold defaultstate="collapsed" desc="�f�U�C�i�ɂ��C�x���g�n���h��">
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
        labelShopOther.setText("���ɓX��");
    }//GEN-LAST:event_rdoDirectionOut2ActionPerformed
    
    private void rdoDirectionIn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDirectionIn2ActionPerformed
        labelShopOther.setText("�o�ɓX��");
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
                        "�m�F������͂��Ă��������B",
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
                            this.getTitle() + " - ���ޕʏW�v",
                            JOptionPane.ERROR_MESSAGE);
                }
            } 
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnOutputActionPerformed
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="�f�U�C�i�ɂ��ϐ��錾">
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
    
    // <editor-fold desc="�������^�u">
    /** �A�C�e���̃��X�g */
    ArrayList<Integer> itemIdList;
    /** �o�ɓX�܂̃��X�g */
    ArrayList<Integer> shopIdList;
    /** �o�ɓ`�[�ԍ��̃��X�g */
    ArrayList<Integer> slipNoList;
    /** �o�ɓ`�[�ڍהԍ��̃��X�g */
    ArrayList<Integer> slipDetailNoList;
    /** �d����ID�̃��X�g */
    ArrayList<Integer> supplierIdList;
    /** �\�̊m�F���̕ҏW�� */
    boolean tblDetail1IsCheckable;
    /** �\���{�^���������ɑI������Ă������X��ID */
    Integer shopIdSelf = null;
    /** �\��(�������^�u) */
    private void show1(){
        /** ���X��ID�̃J�������� */
        String self_eq_;
        /** ����("dest"�܂���"src") */
        String other;
        /** �X�̋Ɩ��敪�̏����� */
        String division;
        /** �݌ɂ̎� */
        String stock;
        
        // �X�ܑI���̃`�F�b�N
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
        
        // �X�̋Ɩ��敪�̏�������ݒ肷��
        if(rdoDivisionAll1.isSelected()){
            division="";
        }else if(rdoDivisionSale1.isSelected()){
            division = "        sd.item_use_division IN (1, 3) AND\n";
        }else if(rdoDivisionWork1.isSelected()){
            division = "        sd.item_use_division IN (2, 3) AND\n";
        }else{
            //���i�敪��I�����Ă�������
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDivision1.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDivisionAll1.requestFocusInWindow();
            return;
        }
        
        // �o�ɋ敪���̏���
        if(rdoDirectionIn1.isSelected()){
            /** ���� */
            TableColumnModel model = tblDetail1.getTableHeader().getColumnModel();
            model.getColumn(5).setHeaderValue("�o�Ɍ��X��");
            model.getColumn(8).setHeaderValue("���ɑO�݌�");
            model.getColumn(9).setHeaderValue("���ɐ�");
            model.getColumn(10).setHeaderValue("���݌�");
            tblDetail1IsCheckable = true;
            btnRegist.setEnabled(true);
            confirmerCode1.setEnabled(true);
            confirmerName1.setEnabled(true);
            self_eq_ = "m.dest_shop_id = ";
            other = "src";
            stock = "    --���ɑO�݌�\n" +
                    "    get_item_stock(m.dest_shop_id, m.item_id, sd.item_use_division, CAST(current_timestamp AS timestamp without time zone)) AS stock\n";
        }else if(rdoDirectionOut1.isSelected()){
            /** �o�� */
            TableColumnModel model = tblDetail1.getTableHeader().getColumnModel();
            model.getColumn(5).setHeaderValue("���ɐ�X��");
            model.getColumn(8).setHeaderValue("�o�ɑO�݌�");
            model.getColumn(9).setHeaderValue("�o�ɐ�");
            model.getColumn(10).setHeaderValue("�݌ɐ�");
//            tblDetail1IsCheckable = false;
            tblDetail1IsCheckable = true;
            btnRegist.setEnabled(false);
            confirmerCode1.setEnabled(false);
            confirmerName1.setEnabled(false);
            self_eq_ = "m.src_shop_id = ";
            other = "dest";
            stock = "    --�o�ɒ���݌�\n" +
                    "    get_item_stock(m.src_shop_id, m.item_id, sd.item_use_division, s.ship_date) AS stock\n";
        }else{
            //�o�ɋ敪��I�����Ă�������
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
                    "    --�X�̋Ɩ��敪\n" +
                    "    sd.item_use_division,\n" +
                    "    --����\n" +
                    "    (SELECT\n" +
                    "        c.item_class_name\n" +
                    "    FROM\n" +
                    "        mst_item_class AS c\n" +
                    "    WHERE\n" +
                    "        c.item_class_id = i.item_class_id\n" +
                    "    ) AS item_class_name,\n" +
                    "    --���i��\n" +
                    "    i.item_name,\n" +
                    "    --�d���P��\n" +
                    "    sd.cost_price,\n" +
                    "    --�̔����i\n" +
                    "    i.price,\n" +
                    "    --������X�ܖ�\n" +
                    "    (SELECT\n" +
                    "        mst_shop.shop_name\n" +
                    "    FROM\n" +
                    "        mst_shop AS mst_shop\n" +
                    "    WHERE\n" +
                    "        mst_shop.shop_id = m." + other + "_shop_id AND\n" +
                    "        mst_shop.delete_date IS NULL\n" +
                    "    ) AS shop_name,\n" +
                    "    --�o�ɒS����\n" +
                    "    (SELECT\n" +
                    "        (mst_staff.staff_name1 || '�@' || mst_staff.staff_name2)\n" +
                    "    FROM\n" +
                    "        mst_staff\n" +
                    "    WHERE\n" +
                    "        mst_staff.staff_id = s.staff_id AND\n" +
                    "        mst_staff.delete_date IS NULL\n" +
                    "    ) AS staff_name,\n" +
                    "    --�o�ɓ�\n" +
                    "    s.ship_date,\n" +
                    "    --�o�ɐ�\n" +
                    "    sd.out_num,\n" +
                    "    --�A�C�e��ID\n" +
                    "    m.item_id,\n" +
                    "    --�o�Ɍ��X��ID\n" +
                    "    m.src_shop_id,\n" +
                    "    --�o�ɓ`�[�ԍ�\n" +
                    "    m.src_slip_no,\n" +
                    "    --�o�ɓ`�[�ڍהԍ�\n" +
                    "    sd.slip_detail_no,\n" +
                    "    --�d����ID\n" +
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
                    "    m.dest_slip_no IS NULL AND --������\n" +
                    "    s.shop_id = m.src_shop_id AND\n" +
                    "    s.slip_no = m.src_slip_no AND\n" +
                    "    s.delete_date IS NULL");
            // ���X�g�̃N���A
            itemIdList = new ArrayList<Integer>();
            shopIdList = new ArrayList<Integer>();
            slipNoList = new ArrayList<Integer>();
            slipDetailNoList = new ArrayList<Integer>();
            supplierIdList = new ArrayList<Integer>();
            // �\�̃N���A
            ((DefaultTableModel)tblDetail1.getModel()).setRowCount(0);
            
            /** �\�ɒǉ�����s�̍��ڃ��X�g */
            Vector vec;
            /** �ėpint */
            int i;
            /** ���o�ɐ� */
            Integer num;
            /** �A�C�e��ID */
            int itemId;
            /** �o�ɓ� */
            Date shipDate;

            if(rdoDirectionIn1.isSelected()) {
                tblDetail1.getTableHeader().getColumnModel().getColumn(tblDetail1.getColumnCount() - 1).setHeaderValue("�m�F");
            } else {
                tblDetail1.getTableHeader().getColumnModel().getColumn(tblDetail1.getColumnCount() - 1).setHeaderValue("�폜");
            }

            while(rs.next()){
                // �A�C�e��ID
                itemIdList.add(itemId = rs.getInt("item_id"));
                // �o�Ɍ��X��ID
                shopIdList.add(rs.getInt("src_shop_id"));
                // �o�ɓ`�[�ԍ�
                slipNoList.add(rs.getInt("src_slip_no"));
                // �o�ɓ`�[�ڍהԍ�
                slipDetailNoList.add(rs.getInt("slip_detail_no"));
                // �o�ɓ`�[�ڍהԍ�
                supplierIdList.add(rs.getInt("supplier_id"));
                
                vec = new Vector();
                // �X�̋Ɩ��敪
                vec.add(getUseDivisionString(rs.getInt("item_use_division")));
                // ����
                vec.add(rs.getString("item_class_name"));
                // ���i��
                vec.add(rs.getString("item_name"));
                // �d���P��
                i = rs.getInt("cost_price");
                vec.add(rs.wasNull() ? null : i);
                // �̔����i
                i = rs.getInt("price");
                vec.add(rs.wasNull() ? null : i);
                //������X�ܖ�
                vec.add(rs.getString("shop_name"));
                // �o�ɒS����
                vec.add(rs.getString("staff_name"));
                // �o�ɓ�
                vec.add(shipDate = rs.getDate("ship_date"));
                
                num = rs.getInt("out_num"); // ���o�ɐ�
                num = (rs.wasNull() ? null : num);
                i = rs.getInt("stock"); // ���ɑO�݌ɂ܂��͏o�ɑO�݌�
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

                //�s��ǉ�����
                ((DefaultTableModel)tblDetail1.getModel()).addRow(vec);
            }
            rs.close();
        } catch (SQLException ex) {
            SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }
    
    /** �o�^ */
    private void regist(){
        /** �m�F�S����ID */
        Integer confirmerStaffId = null;
        int rowCount = tblDetail1.getRowCount();
        ArrayList<Integer> checkedIndice = new ArrayList<Integer>();
        if(rowCount < 1){
            MessageDialog.showMessageDialog(this,
                    "���Ɋm�F���s���Ώۂ��\������Ă��܂���B",
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
                    "�m�F���Ƀ`�F�b�N�����ĉ������B",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        /** ���ɐ�X�� */
        if(shopIdSelf == null){
            MessageDialog.showMessageDialog(this,
                    "���X��ID���s���̂��ߓo�^�ł��܂���B",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        /** �m�F�� */
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
        
        // �m�F�S���҂̕K�{�`�F�b�N
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
                MessageUtil.getMessage(MessageUtil.CONFIRM_REGIST, "���Ɋm�F"),
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        try{
            ConnectionWrapper con = SystemInfo.getConnection();
            
            int slipNoMax = getMaxDestSlipNo(con, shopIdSelf);
            if(slipNoMax == Integer.MAX_VALUE){
                MessageDialog.showMessageDialog(this,
                        "�`�[�ԍ����o�^�\�ȍő�̔ԍ��𒴂��邽�ߓo�^�ł��܂���ł����B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // �g�����U�N�V�����J�n
                con.begin();
                /** �d����ID�����ɓ`�[�ԍ��̃}�b�v */
                HashMap<Integer, Integer> slipNoMap
                        = new HashMap<Integer, Integer>();
                /** �d����ID�����ɓ`�[�ڍהԍ��̃}�b�v */
                HashMap<Integer, Integer> slipDetailNoMap
                        = new HashMap<Integer, Integer>();
                /** �o�Ɍ��`�[�ڍ� */
                //              DataSlipShipDetail sd;
                /** �A�C�e�� */
//                MstSupplierItem item;
                
                /** �I�����ꂽ�s */
                int row;
                /** ���iID */
                int itemId;
                /** �o�Ɍ��X��ID */
                int srcShopId;
                /** �o�ɓ`�[�ԍ� */
                int srcSlipNo;
                /** �o�ɓ`�[�ڍהԍ� */
                int srcDetailSlipNo;
                /** �d�����ID */
                int supplierId;
                /** ���ɓ`�[�ԍ� */
                int destSlipNo;
                /** ���ɓ`�[�ڍהԍ� */
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
                                    "�`�[�ڍהԍ����o�^�\�ȍő�̔ԍ��𒴂��邽�ߓo�^�ł��܂���ł����B",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipDetailNoMap.put(supplierId, ++destDetailSlipNo);
                    }else{
                        if (slipNoMax == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "�`�[�ԍ����o�^�\�ȍő�̔ԍ��𒴂��邽�ߓo�^�ł��܂���ł����B",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipNoMap.put(supplierId, destSlipNo = ++slipNoMax);
                        slipDetailNoMap.put(supplierId, destDetailSlipNo = 1);
                        // ���ɓ`�[��o�^����
                        insertSlipStore(con, srcShopId, srcSlipNo,
                                shopIdSelf, destSlipNo, confirmerStaffId);
                    }
                    
                    updateDataMoveStock(con, srcShopId, srcSlipNo, itemId,
                            shopIdSelf, destSlipNo);
                    insertSlipStoreDetail(con, srcShopId, srcSlipNo, srcDetailSlipNo,
                            shopIdSelf, destSlipNo, destDetailSlipNo);
                }
                // �R�~�b�g
                con.commit();
                // ���Ɋm�F�����s���폜����
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
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "���ɓ`�["),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    /** ���ɓ`�[��o�^���� */
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
                "    " + destShopId + ", -- ���ɓX��ID\n" +
                "    " + destSlipNo + ", -- ���ɓ`�[�ԍ�\n" +
                "    s.supplier_id,\n" +
                "    "+SQLUtil.convertForSQL(confirmDate.getDate())+",\n" + // ���ɓ�(�뎞�땪�ɂ���)
                "    " + confirmerId + ", -- ���ɒS����\n" +
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
    
    /** ���ɓ`�[�ڍׂ�o�^���� */
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
                "    " + destShopId + ", -- ���ɓX��ID\n" +
                "    " + destSlipNo + ", -- ���ɓ`�[�ԍ�\n" +
                "    " + destSlipDetailNo + ", -- ���ɓ`�[�ڍהԍ�\n" +
                "    sd.item_id,\n" +
                "    sd.item_use_division,\n" +
                "    sd.out_num,\n" +
                "    0,\n" + // �Y�t��
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
    
    /** ���ׂ�����������B */
    private void initTblDetail1() {
        DefaultTableColumnModel model =
                (DefaultTableColumnModel)tblDetail1.getColumnModel();
        model.getColumn(0).setPreferredWidth(45);//�敪
        model.getColumn(1).setPreferredWidth(70);//����
        model.getColumn(2).setPreferredWidth(160);//���i��
        model.getColumn(3).setPreferredWidth(54);//�d�����i
        model.getColumn(4).setPreferredWidth(54);//�̔����i
        model.getColumn(5).setPreferredWidth(66);//�o�Ɍ��X�܂܂��͓��ɐ�X��
        model.getColumn(6).setPreferredWidth(66);//�o�ɒS����
        model.getColumn(7).setPreferredWidth(66);//�o�ɓ�
        model.getColumn(8).setPreferredWidth(66);//���ɑO�݌ɂ܂��͏o�ɑO�݌�
        model.getColumn(9).setPreferredWidth(45);//���ɐ��܂��͏o�ɐ�
        model.getColumn(10).setPreferredWidth(45);//���݌ɂ܂��͌��ݐ�
        model.getColumn(11).setPreferredWidth(35);//�m�F
    }
    // </editor-fold>
    
    // <editor-fold desc="�����^�u">
    /** �\��(����) */
    private void show2(){
        /** ���Ɋ���From */
        String recvFrom = receivedDateFrom.getDateStr("-");
        /** ���Ɋ���To */
        String recvTo = receivedDateTo.getDateStr("-");
        /** �o�Ɋ���From */
        String sendFrom = sendDateFrom.getDateStr("-");
        /** �o�Ɋ���To */
        String sendTo = sendDateTo.getDateStr("-");
        /** ���X��ID�̃J�������� */
        String self_eq_;
        /** ����X��ID�̃J�������� */
        String other_eq_;
        /** ����("dest"�܂���"src") */
        String other;
        /** �o�ɒS����ID */
        Integer dispatcherStaffId;
        /** �m�F�S����ID */
        Integer confirmerStaffId;
        
        /** ������ */
        StringBuilder expression = new StringBuilder();
        /** �X�̋Ɩ��敪�̏����� */
        String division;
        
        /** ���X�� */
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
        
        // �X�̋Ɩ��敪�̏�������ݒ肷��
        if(rdoDivisionAll2.isSelected()){
            division="";
        }else if(rdoDivisionSale2.isSelected()){
            division = "        sd.item_use_division IN (1, 3) AND\n";
        }else if(rdoDivisionWork2.isSelected()){
            division = "        sd.item_use_division IN (2, 3) AND\n";
        }else{
            //���i�敪��I�����Ă�������
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDivision2.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDivisionAll2.requestFocusInWindow();
            return;
        }
        
        // �o�ɋ敪���̏���
        if(rdoDirectionIn2.isSelected()){
            TableColumnModel model = tblDetail2.getTableHeader().getColumnModel();
            //IVS_TMTrong start add 2015/09/29 New request #42933
            //model.getColumn(5).setHeaderValue("�o�ɓX��");
            model.getColumn(6).setHeaderValue("�o�ɓX��");
            //IVS_TMTrong end add 2015/09/29 New request #42933
            self_eq_ = "m.dest_shop_id = ";
            other_eq_ = "m.src_shop_id = ";
            other = "src";
        }else if(rdoDirectionOut2.isSelected()){
            TableColumnModel model = tblDetail2.getTableHeader().getColumnModel();
            //IVS_TMTrong start add 2015/09/29 New request #42933
            //model.getColumn(5).setHeaderValue("���ɓX��");
            model.getColumn(6).setHeaderValue("���ɓX��");
            //IVS_TMTrong end add 2015/09/29 New request #42933
            self_eq_ = "m.src_shop_id = ";
            other_eq_ = "m.dest_shop_id = ";
            other = "dest";
        }else{
            //�o�ɋ敪��I�����Ă�������
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                    labelDirection2.getText()),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            rdoDirectionIn2.requestFocusInWindow();
            return;
        }
        tblDetail2.getTableHeader().repaint();
        
        // ����From
        if(recvFrom != null){
            expression
                    .append("d.store_date >= date'")
                    .append(recvFrom)
                    .append("' AND\n");
        }
        // ����To
        if(recvTo != null){
            expression
                    .append("date_trunc('day', d.store_date) <= date'")
                    .append(recvTo)
                    .append("' AND\n");
        }
        // �o��From
        if(sendFrom != null){
            expression
                    .append("s.ship_date >= date'")
                    .append(sendFrom)
                    .append("' AND\n");
        }
        // �o��To
        if(sendTo != null){
            expression
                    .append("date_trunc('day', s.ship_date) <= date'")
                    .append(sendTo)
                    .append("' AND\n");
        }
        /** ������X�� */
        MstShop shopOtherSelection = (MstShop)shopOther.getSelectedItem();
        if(shopOtherSelection != null){
            expression
                    .append(other_eq_)
                    .append(shopOtherSelection.getShopID())
                    .append(" AND\n");
        }
        /** �o�ɒS���� */
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
        // �m�F�S����
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
                    "    --�X�̋Ɩ��敪\n" +
                    "    sd.item_use_division,\n" +
                    //IVS_TMTrong start add 2015/09/28 New request #42933
                    "    --�d����\n" +
                    "   ms.supplier_name AS supplier_name,\n"+
                    //IVS_TMTrong end add 2015/09/28 New request #42933
                    "    --����\n" +
                    "    (SELECT\n" +
                    "        c.item_class_name\n" +
                    "    FROM\n" +
                    "        mst_item_class AS c\n" +
                    "    WHERE\n" +
                    "        c.item_class_id = i.item_class_id\n" +
                    "    ) AS item_class_name,\n" +
                    "    --���i��\n" +
                    "    i.item_name,\n" +
                    "    --�d���P��\n" +
                    "    sd.cost_price,\n" +
                    "    --�̔����i\n" +
                    "    i.price,\n" +
                    "    --������X�ܖ�\n" +
                    "    (SELECT\n" +
                    "        mst_shop.shop_name\n" +
                    "    FROM\n" +
                    "        mst_shop AS mst_shop\n" +
                    "    WHERE\n" +
                    "        mst_shop.shop_id = m." + other + "_shop_id AND\n" +
                    "        mst_shop.delete_date IS NULL\n" +
                    "    ) AS shop_name,\n" +
                    "    --�o�ɒS����\n" +
                    "    (SELECT\n" +
                    "        (mst_staff.staff_name1 || '�@' || mst_staff.staff_name2)\n" +
                    "    FROM\n" +
                    "        mst_staff\n" +
                    "    WHERE\n" +
                    "        mst_staff.staff_id = s.staff_id AND\n" +
                    "        mst_staff.delete_date IS NULL\n" +
                    "    ) AS staff_name,\n" +
                    "    --�o�ɓ�\n" +
                    "    s.ship_date,\n" +
                    "    --�o�ɐ�\n" +
                    "    sd.out_num,\n" +
                    "    --�m�F��\n" +
                    "    d.store_date,\n" +
                    "    --�m�F�S����\n" +
                    "    (SELECT\n" +
                    "        (mst_staff.staff_name1 || '�@' || mst_staff.staff_name2)\n" +
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
            // �\�̃N���A
            ((DefaultTableModel)tblDetail2.getModel()).setRowCount(0);
            
            /** �\�ɒǉ�����s�̍��ڃ��X�g */
            Vector vec;
            int i;
            //IVS_TMTrong start add 2015/09/28 New request #42933
            lstMoveStockReportBean.clear();
            //IVS_TMTrong end add 2015/09/28 New request #42933
            while(rs.next()){
                vec = new Vector();
                // �X�̋Ɩ��敪
                vec.add(getUseDivisionString(rs.getInt("item_use_division")));
                //IVS_TMTrong start add 2015/09/28 New request #42933
                // �d����
                vec.add(rs.getString("supplier_name"));
                //IVS_TMTrong end add 2015/09/28 New request #42933
                // ����
                vec.add(rs.getString("item_class_name"));
                // ���i��
                vec.add(rs.getString("item_name"));
                // �d���P��
                i = rs.getInt("cost_price");
                vec.add(rs.wasNull() ? null : i);
                // �̔����i
                i = rs.getInt("price");
                vec.add(rs.wasNull() ? null : i);
                //������X�ܖ�
                vec.add(rs.getString("shop_name"));
                // �o�ɒS����
                vec.add(rs.getString("staff_name"));
                // �o�ɓ�
                
                if(rs.getDate("ship_date")!=null){
                    String shipDate = new SimpleDateFormat("yyyy/MM/dd").format(rs.getDate("ship_date"));
                    vec.add(shipDate);
                }
                else{
                    vec.add("");
                }
                // �o�ɐ�
                i = rs.getInt("out_num");
                vec.add(rs.wasNull() ? null : i);
                // �m�F��
                if(rs.getDate("store_date")!=null){
                    String storeDate = new SimpleDateFormat("yyyy/MM/dd").format(rs.getDate("store_date"));
                    vec.add(storeDate);
                }
                else{
                    vec.add("");
                }
                // �m�F�S����
                vec.add(rs.getString("confirmer_name"));
                
                //�s��ǉ�����
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
    
    /** ���ׂ�����������B */
    private void initTblDetail2() {
        DefaultTableColumnModel model =
                (DefaultTableColumnModel)tblDetail2.getColumnModel();
        model.getColumn(0).setPreferredWidth(40);//�敪
        //IVS_TMTrong start add 2015/09/28 New request #42933
         model.getColumn(1).setPreferredWidth(72);//�d����
        //IVS_TMTrong end add 2015/09/28 New request #42933
        //IVS_TMTrong start edit 2015/09/28 New request #42933
        model.getColumn(2).setPreferredWidth(72);//����
        model.getColumn(3).setPreferredWidth(190);//���i��
        model.getColumn(4).setPreferredWidth(53);//�d�����i
        model.getColumn(5).setPreferredWidth(53);//�̔����i
        model.getColumn(6).setPreferredWidth(53);//�o�ɓX��
        model.getColumn(7).setPreferredWidth(64);//�o�ɒS����
        model.getColumn(8).setPreferredWidth(65);//�o�ɓ�
        model.getColumn(9).setPreferredWidth(40);//�o�ɐ�
        model.getColumn(10).setPreferredWidth(65);//�m�F��
        model.getColumn(11).setPreferredWidth(64);//�m�F�S����
        //IVS_TMTrong end edit 2015/09/28 New request #42933
    }
    // </editor-fold>
    
    // <editor-fold desc="�o�Ƀ^�u">
    /** ���i�𖾍ׂɂ���������� */
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
                    "�o�ɐ�X�܂Ŏg�p���i�Ƃ��ēo�^����Ă��܂���B",
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
                //�������ꍇ�A�ړ����ɂP�𑫂�
                tblDetail3.setValueAt((Integer)tblDetail3.getValueAt(i, 6) + 1, i, 6);
                return;
            }
        }

        //�Ȃ������ꍇ�A���׍s��ǉ�����
        addOneDetailInfo(tblProductDetail, itemUseDivision);
        return;
    }
    
    /** ���׍s��ǉ����� */
    private void addOneDetailInfo(JTableEx tblProductDetail, int itemUseDivision) {
        /** �V�K���׍s�̍��ڃ��X�g */
        Vector vec = new Vector();
        
        //�p�r��ݒu����
        MstSupplierItem item = (MstSupplierItem) tblProductDetail
                .getValueAt(tblProductDetail.getSelectedRow(), 0);
        
        TableRecord rec = new TableRecord();
        rec.setItemId(item.getItemID());
        rec.setSelectItemUseDivision(itemUseDivision);
        vec.add(rec); // �敪
        
        MstItemClass mstItemClass = classes.lookup(item.getItemClass().getItemClassID());
        vec.add(mstItemClass); // ����
        vec.add(item); // ���i��
        vec.add(item.getCostPrice()); // �d�����i
        vec.add(item.getPrice()); // �̔����i
        
        MstShop mstShop = (MstShop) shop3.getSelectedItem();
        if (mstShop == null){
            vec.add(null);
        }else{
            vec.add(getItemStock(null, mstShop.getShopID(), item.getItemID(),
                    itemUseDivision)); // ���ݐ�
        }
        vec.add(1); // �ړ���(�����l�͂P)
        
        JButton btnDelete = new JButton(iconTrashOff);
        btnDelete.setPressedIcon(iconTrashOn);
        vec.add(getDeleteButton()); // �폜�{�^��
        
        int rowIndex = findInsertPoint(tblDetail3, itemUseDivision, item);
        //��s��ǉ�����
        ((DefaultTableModel)tblDetail3.getModel()).insertRow(rowIndex, vec);
    }
    
    /** �폜�{�^�����擾���� */
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
    
    /** ���ׂ��P�s�폜����B */
    private void deleteProduct() {
        //int index = this.getProductsIndex( 0 ); // �I���Z�p
        //DataSalesDetail dsd = ia.getSales().get( index );
        //int size = dsd.size();
        //ia.getSales().remove( index );
        // �Z���̕ҏW���I��点��
        if( tblDetail3.getCellEditor() != null ){
            tblDetail3.getCellEditor().stopCellEditing();
        }
        int row = tblDetail3.getSelectedRow();    // �I���s
        ((DefaultTableModel)tblDetail3.getModel()).removeRow(row);
        
        DefaultTableModel model = (DefaultTableModel)tblDetail3.getModel();
        
//        for( int i = 0; ( i == 0 || i < size ); i++ ){
//            model.removeRow(row);
//        }
    }

    /** �폜�{�^�����擾���� */
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

    /** ���ׂ��P�s�폜����B */
    private void deleteDirectionOutProduct() {
        if( tblDetail1.getCellEditor() != null ){
            tblDetail1.getCellEditor().stopCellEditing();
        }
        int row = tblDetail1.getSelectedRow();    // �I���s

        ConnectionWrapper con = SystemInfo.getConnection();

        // �m�F�ς݃`�F�b�N
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
                        "���̏��i�́A���łɊm�F����Ă��܂��B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int ret = MessageDialog.showYesNoDialog(
                        this,
                        "�폜���܂��B��낵���ł����H",
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

    /** ���ׂ�����������B */
    private void initTblDetail3() {
        DefaultTableColumnModel model =
                (DefaultTableColumnModel)tblDetail3.getColumnModel();
        tblDetail3.getTableHeader().setReorderingAllowed(false);
        model.getColumn(0).setPreferredWidth(40);//�敪
        model.getColumn(1).setPreferredWidth(72);//����
        model.getColumn(2).setPreferredWidth(357);//���i��
        model.getColumn(3).setPreferredWidth(60);//�d�����i
        model.getColumn(4).setPreferredWidth(60);//�̔����i
        model.getColumn(5).setPreferredWidth(53);//���ݐ�
        model.getColumn(6).setPreferredWidth(53);//�ړ���
        model.getColumn(7).setPreferredWidth(50);//�폜
        model.getColumn(7).setResizable(false);
        model.getColumn(6).setCellEditor(new IntegerCellEditor(new JTextField()));
        SelectTableCellRenderer.setSelectTableCellRenderer(tblDetail3);
    }
    
    /** �݌Ɉꗗ */
    private void transitToStockList(){
        StockList pnl = new StockList();
        //changeContents(pnl);
        pnl.ShowDialog(null);
        //SwingUtil.openAnchorDialog( null, true, pnl, "�݌Ɉꗗ", SwingUtil.ANCHOR_LEFT|SwingUtil.ANCHOR_VCENTER );
    }
    
    /** �o�� */
    private void dispatch(){

        int srcId;
        int destId;

        /** �o�Ɍ��X�� */
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
        
        /** ���ɐ�X�� */
        MstShop destShop = (MstShop)destination.getSelectedItem();
        if(destShop == null){
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�ΏۓX��"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            destination.requestFocusInWindow();
            return;
        }
        destId = destShop.getShopID();
        
        /** �o�ɒS����ID */
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
        // �Z���̕ҏW���I��点��
        if( tblDetail3.getCellEditor() != null ){
            tblDetail3.getCellEditor().stopCellEditing();
        }
        // �ړ����̃`�F�b�N
        /** �s�� */
        int rowCount = tblDetail3.getRowCount();
        for(int i = 0; i < rowCount; i++){
            Integer stockNum = (Integer)tblDetail3.getValueAt(i, 5);
            Integer outNum = (Integer)tblDetail3.getValueAt(i, 6);
            if(outNum == null || outNum < 1){
                MessageDialog.showMessageDialog(this,
                        "�ړ����ɂO�ȉ������͂���Ă��܂��B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }else if(stockNum == null || outNum > stockNum){
                MessageDialog.showMessageDialog(this,
                        "�ړ��������ݐ��𒴂��Ă��܂��B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // ���̓`�F�b�N�I���
        
        if (MessageDialog.showYesNoDialog(this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_REGIST, "�o�ɓ`�["),
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        
        try{
            ConnectionWrapper con = SystemInfo.getConnection();
            
            int slipNoMax = getMaxSrcSlipNo(con, srcId);
            if(slipNoMax == Integer.MAX_VALUE){
                MessageDialog.showMessageDialog(this,
                        "�`�[�ԍ����o�^�\�ȍő�̔ԍ��𒴂��邽�ߓo�^�ł��܂���ł����B",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // �g�����U�N�V�����J�n
                con.begin();
                /** �d����ID���o�ɓ`�[�ԍ��̃}�b�v */
                HashMap<Integer, Integer> slipNoMap
                        = new HashMap<Integer, Integer>();
                /** �d����ID���o�ɓ`�[�ڍהԍ��̃}�b�v */
                HashMap<Integer, Integer> slipDetailNoMap
                        = new HashMap<Integer, Integer>();
                /** �o�Ɍ��`�[�ڍ� */
                DataSlipShipDetail sd;
                /** �A�C�e�� */
                MstSupplierItem item;
                /** �d�����ID */
                int supplierId;
                /** �`�[�ԍ� */
                int slipNo;
                /** �`�[�ڍהԍ� */
                int slipDetailNo;
                
                for (int i = 0; i < rowCount; i++) {
                    item = (MstSupplierItem)tblDetail3.getValueAt(i, 2);
                    supplierId = item.getSupplier().getSupplierID();
                    if(slipNoMap.containsKey(supplierId)){
                        slipNo = slipNoMap.get(supplierId);
                        slipDetailNo = slipDetailNoMap.get(supplierId);
                        if (slipDetailNo == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "�`�[�ڍהԍ����o�^�\�ȍő�̔ԍ��𒴂��邽�ߓo�^�ł��܂���ł����B",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipDetailNoMap.put(supplierId, ++slipDetailNo);
                    }else{
                        if (slipNoMax == Integer.MAX_VALUE){
                            MessageDialog.showMessageDialog(this,
                                    "�`�[�ԍ����o�^�\�ȍő�̔ԍ��𒴂��邽�ߓo�^�ł��܂���ł����B",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return;
                        }
                        slipNoMap.put(supplierId, slipNo = ++slipNoMax);
                        slipDetailNoMap.put(supplierId, slipDetailNo = 1);
                        // �o�ɓ`�[��o�^����
                        insertDataSlipShip(con, srcId, slipNo, supplierId, dispatcherStaffId);
                    }
                    
                    // �o�ɓ`�[�ڍׂ�o�^����
                    insertDataSlipShip(con, srcId, slipNo, slipDetailNo,
                            item, (Integer)tblDetail3.getValueAt(i, 6),
                            ((TableRecord)tblDetail3.getValueAt(i, 0)).getSelectItemUseDivision());
                    // �X�܊Ԉړ����R�[�h��o�^����
                    insertDataMoveStock(con, srcId, slipNo, item.getItemID(), destId);
                }
                
                // �R�~�b�g
                con.commit();
                
                /** �݌ɐ����ĕ\������ */
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
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�o�ɓ`�["),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    /** �݌ɐ����ĕ\������ */
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
    
    /** �݌ɐ����擾���� */
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
    
    /**���ݓo�^����Ă���ő�̏o�ɓ`�[�ԍ����擾����
     *@param con �R�l�N�V����
     *@param shopId �X��ID
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
    
    /**���ݓo�^����Ă���ő�̓��ɓ`�[�ԍ����擾����
     *@param con �R�l�N�V����
     *@param shopId �X��ID
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
    
    /** �o�ɓ`�[��o�^����
     *@param con �R�l�N�V����
     *@param shopId �o�ɑ��X��ID
     *@param slipNo �o�ɑ��`�[�ԍ�
     *@param supplierId �d����ID
     *@param staffId �o�ɒS����ID
     */
    private void insertDataSlipShip(ConnectionWrapper con,
            int shopId, int slipNo, int supplierId, int staffId) throws SQLException{
        DataSlipShip s = new DataSlipShip();
        s.setShopId(shopId); // �X��ID
        s.setSlipNo(slipNo); // �`�[�ԍ�
        s.setSupplierId(supplierId); // �d����ID
        s.setShipDate(new Date()); // �o�ɓ�
        s.setStaffId(staffId); // �o�ɒS����ID
        s.insert(con);
    }
    
    /** �o�ɓ`�[�ڍׂ�o�^����
     *@param con �R�l�N�V����
     *@param shopId �o�ɑ��X��ID
     *@param slipNo �o�ɑ��`�[�ԍ�
     *@param slipDetailNo �`�[�ڍהԍ�
     *@param item ���i
     *@param outNum �o�ɐ�
     *@param useDivision �X�̋Ɩ��敪
     */
    private void insertDataSlipShip(ConnectionWrapper con,
            int shopId, int slipNo, int slipDetailNo,
            MstSupplierItem item, int outNum, int useDivision) throws SQLException{
        DataSlipShipDetail sd = new DataSlipShipDetail();
        sd.setShopId(shopId); // �o�ɑ��X��ID
        sd.setSlipNo(slipNo); // �`�[�ԍ�
        sd.setSlipDetailNo(slipDetailNo); // �`�[�ڍהԍ�
        sd.setItemId(item.getItemID()); // ���iID
        sd.setItemUseDivision(useDivision); // �X�̋Ɩ��敪
        sd.setOutNum(outNum); // �o�ɐ�
        sd.setCostPrice(item.getCostPrice()); // �d���P��
        sd.setOutClass(6); // �o�ɋ敪 6:�X�܊Ԉړ�
        sd.insert(con);
    }
    
    /** �X�܊Ԉړ����R�[�h��o�^����
     *@param con �R�l�N�V����
     *@param shopId �o�ɑ��X��ID
     *@param slipNo �o�ɑ��`�[�ԍ�
     *@param itemId �A�C�e��ID
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
    
    /** �X�܊Ԉړ����R�[�h���X�V����
     *@param con �R�l�N�V����
     *@param srcShopId �o�ɑ��X��ID
     *@param srcSlipNo �o�ɓ`�[�ԍ�
     *@param itemId ���iID
     *@param destShopId ���ɑ��X��ID
     *@param destSlipNo ���ɓ`�[�ԍ�
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
    
    /** ���׍s�̑}���ꏊ���擾���� */
    private int findInsertPoint(JTableEx tblDetail3, int itemUseDivision, MstItem item) {
        int itemClassDispSeq = classes.lookup(item.getItemClass().getItemClassID()).getDisplaySeq();
        int itemDispSeq = item.getDisplaySeq();
        /** �s�� */
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
    
    /** ���i���X�g���X�V���� */
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
        
        //���i���X�g������������
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
    
    /** JTable�̑S�Ă̍s���폜����B */
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
     * FocusTraversalPolicy���擾����B
     * @return FocusTraversalPolicy
     */
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return	traversalPolicy;
    }
    
    /** �{�^���}�E�X�I�[�o�[���̃}�E�X�|�C���^��ݒ� */
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
    
    /** �敪�E���i�y�A�i�敪�\���p�j */
    private static class TableRecord {
        /** ���i�敪 */
        private int selectItemUseDivision;
        /** �A�C�e��ID */
        private Integer itemId;
        
        /** ���i�敪���擾���� */
        public int getSelectItemUseDivision() {return selectItemUseDivision;}
        
        /** �A�C�e��ID���擾���� */
        public Integer getItemId() {return itemId;}
        
        /** ���i�敪��ݒ肷�� */
        public void setSelectItemUseDivision(int itemUseDivision) {
            this.selectItemUseDivision = itemUseDivision;
        }
        
        /** �A�C�e��ID��ݒ肷�� */
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
    
    /** �X�̋Ɩ��敪�̕������ */
    private static String getUseDivisionString(int useDivision) {
        switch (useDivision) {
            case 1:
                return "�X�̗p";
            case 2:
                return "�Ɩ��p";
            case 3:
                return "�X�́��Ɩ��p";
            default:
                return "";
        }
    }
    
    /** ���i�p�v���C�x�[�g�N���X */
    private class MstSupplierItemList extends ArrayList<MstSupplierItem> {
        /** �R���X�g���N�^ */
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
    private class MoveStockPanelFocusTraversalPolicy extends FocusTraversalPolicy {
        /**
         * aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
         * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
         * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
         * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
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
         * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
         * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
         * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
         * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
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
         * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
         * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
         * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
         * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
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
            return shop1;
        }
    }
    //IVS_TMTrong start add 2015/09/28 New request #42933
    private void printMoveStock(){
        JExcelApi jx = new JExcelApi("�X�܊Ԉړ��ꗗ.xls");
        jx.setTemplateFile("/reports/�X�܊Ԉړ��ꗗ.xls");
        jx.setTargetSheet(1);
        
        String shopName = null;
        if (shop2.getSelectedItem() instanceof MstGroup) {
            //�O���[�v
            MstGroup mg = (MstGroup) shop2.getSelectedItem();
            shopName = mg.getGroupName();
        } else {
            // �X��
            MstShop ms = (MstShop) shop2.getSelectedItem();
            shopName = ms.getShopName();
        }
        // �w�b�_
        jx.setValue(2, 3, shopName);
        //IVS_TMTrong start add 2015/09/30 New request #42933
        if(receivedDateFrom.getDateStr() == null && receivedDateTo.getDateStr() == null){
            jx.setValue(2, 4, "");
        }else{
            jx.setValue(2, 4, receivedDateFrom.getDateStr("/") + " �` " + receivedDateTo.getDateStr("/"));
        }
        //IVS_TMTrong end add 2015/09/30 New request #42933
        
        if(sendDateFrom.getDateStr() == null && sendDateTo.getDateStr() == null){
            jx.setValue(2, 5, "");
        }else{
            jx.setValue(2, 5, sendDateFrom.getDateStr("/") + " �` " + sendDateTo.getDateStr("/"));
        }
        
        if(shopOther.getSelectedIndex()>0){
            jx.setValue(2, 6, ((MstShop)shopOther.getSelectedItem()).getShopName());
        }
        else{
            jx.setValue(2, 6, "");
        }
        
        String CategoryName=null;
        if(rdoDivisionAll2.isSelected()){
            CategoryName="�S��";
        }else if(rdoDivisionSale2.isSelected()){
            CategoryName="�X�̗p";
        }else if(rdoDivisionWork2.isSelected()){
            CategoryName="�Ɩ��p";
        }
        
        jx.setValue(2, 7, CategoryName);
        jx.setValue(2, 8, rdoDirectionIn2.isSelected()?"����":"�o��");
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
            jx.setValue(1, 6, "�o�ɓX�܁F");
        }else{
            jx.setValue(1, 6, "���ɓX�܁F");
        }
        //IVS_TMTrong end add 2015/09/30 New request #42933
        
        if(rdoDirectionIn2.isSelected()){
           jx.setValue(7, 10, "�o�ɓX��");
        }else if(rdoDirectionOut2.isSelected()){
            jx.setValue(7, 10, "���ɓX��");
        }
        
        int row = 11;
        //IVS_TMTrong start add 2015/09/29 New request #42933
        jx.insertRow(row, lstMoveStockReportBean.size()-1);
        //IVS_TMTrong end add 2015/09/29 New request #42933
        // �f�[�^�Z�b�g
        for (MoveStockReportBean item : lstMoveStockReportBean) {
            // �X�̋Ɩ��敪
            jx.setValue(1, row, item.getItem_use_division());
            // �d����
            jx.setValue(2, row, item.getSupplier_name());
            // ����
            jx.setValue(3, row, item.getItem_class_name());
            // ���i��
            jx.setValue(4, row, item.getItem_name());
            // �d���P��
            jx.setValue(5, row, item.getCost_price());
            // �̔����i
            jx.setValue(6, row, item.getPrice());
            //������X�ܖ�
            jx.setValue(7, row, item.getShop_name());
            // �o�ɒS����
            jx.setValue(8, row, item.getStaff_name());
            // �o�ɓ�
            jx.setValue(9, row, item.getShip_date());
            // �o�ɐ�
            jx.setValue(10, row, item.getOut_num());
            // �m�F��
            jx.setValue(11, row, item.getStore_date());
            // �m�F�S����
            jx.setValue(12, row, item.getConfirmer_name());
            row++;
        }  
        // ���A�c 1�y�[�W�Ɉ��
        jx.getTargetSheet().getSettings().setFitWidth(1);
        jx.getTargetSheet().getSettings().setFitHeight(1);

        jx.openWorkbook();
    }
    //IVS_TMTrong end add 2015/09/28 New request #42933
}
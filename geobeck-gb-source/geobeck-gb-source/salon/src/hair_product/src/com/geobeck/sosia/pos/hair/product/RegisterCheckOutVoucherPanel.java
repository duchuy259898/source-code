/*
 * CheckInVoucher.java
 *
 * Created on 2008/09/17, 9:56
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.data.product.DataSlipShip;
import com.geobeck.sosia.pos.hair.data.product.DataSlipShipDetail;
import com.geobeck.sosia.pos.hair.data.product.DataSlipShipDetails;
import com.geobeck.sosia.pos.hair.data.product.DataSlipStore;
import com.geobeck.sosia.pos.hair.data.product.DataSlipStoreDetail;
import com.geobeck.sosia.pos.basicinfo.commodity.EditabeTableCellRenderer;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sosia.pos.master.commodity.MstSuppliers;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.master.commodity.MstSupplierItems;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.IntegerCellEditor;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.CheckUtil;
import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;

/**
 *
 * @author  ryu
 */
public class RegisterCheckOutVoucherPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	private enum SearchMode { NONE, SLIP_SHIP };

	//�蓮�����ŃC�x���g�����s����
	private int eventFlg = 0;
	//�V�K�t���O 0:����/1�F�V�K
	private int newFlag = 1;
	
	private MstItemClasses	classes	=	new MstItemClasses();
	private MstSuppliers	mss		=	new MstSuppliers();
	
	private StockCalculator	stock;
	
	/** Creates new form CheckInVoucher */
	public RegisterCheckOutVoucherPanel()
	{
		this.setSize(850, 800);
		this.setPath("���i�Ǘ� >> �o�ɓ`�[�쐬");
		this.setTitle("�o�ɓ`�[�쐬");
		initComponents();
		
		init();
		clear();
	}
	
	/*
	 *�R���{�{�b�N�X�̓��e�������Ȃ�
	 */
	private void init()
	{
		SystemInfo.initGroupShopComponents(shop, 2);
		personOutName.addItem(new MstStaff());
		SystemInfo.initStaffComponent(personOutName);
		
		initItemClass();
		
		initDetailList(tblDetailInfo);
		setKeyListener();
		addMouseCursorChange();
		
		// ���i���ނ̐擪���ڂ�I����Ԃɂ���
		tblProduct1.setRowSelectionInterval(0, 0);
		tblProduct2.setRowSelectionInterval(0, 0);
		
		//�O��I�������Z�b�g����
		setInventoryDate(SystemInfo.getConnection());
                
                try {
                    ConnectionWrapper con = SystemInfo.getConnection();
                    calcStock(con);
                } catch (Exception e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
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
		sql.append("     and shop_id = " + SQLUtil.convertForSQL(((MstShop)shop.getSelectedItem()).getShopID()));
		sql.append("     and inventory_date < " + SQLUtil.convertForSQLDateOnly(checkOutDate.getDate()));

		ResultSetWrapper rs = con.executeQuery(sql.toString());

		while (rs.next()) {
		    inventoryDateGyoumu.setText(rs.getString("inventoryDateGyoumu"));
		    inventoryDateTenpan.setText(rs.getString("inventoryDateTenpan"));
		}

		rs.close();
		
	    } catch (Exception e) {
	    }
	}
	
	private void initDetailList(JTable tbl)
	{
		//add listener to detail info
		SelectionListener listener = new SelectionListener(tbl);
		tbl.getModel().addTableModelListener(listener);
		
		//�ҏW�R����������ݒu����
		JFormattedTextField ftf = new javax.swing.JFormattedTextField();
		((PlainDocument)ftf.getDocument()).setDocumentFilter(
			new CustomFilter(20, CustomFilter.NUMERIC));
		
		JComboBox combo = new JComboBox();
		combo.setBorder(BorderFactory.createEmptyBorder());
		
		combo.addItem(new OutClass(0, ""));
		combo.addItem(new OutClass(1, "�Ɩ��o��"));
		combo.addItem(new OutClass(2, "�X�́ˋƖ�"));
		combo.addItem(new OutClass(3, "�Ɩ��˓X��"));
		combo.addItem(new OutClass(4, "�݌ɒ���"));
		
		TableColumnModel tcm = tblDetailInfo.getColumnModel();
		tcm.getColumn(4).setCellEditor(new IntegerCellEditor(ftf));
		tcm.getColumn(5).setCellEditor(new DefaultCellEditor(combo));
		tcm.getColumn(4).setCellRenderer(new EditabeTableCellRenderer(Integer.class, "0"));
		tcm.getColumn(5).setCellRenderer(new EditabeTableCellRenderer(OutClass.class));

		tcm.getColumn(0).setPreferredWidth(45);		// �敪
		tcm.getColumn(1).setPreferredWidth(120);	// ����
		tcm.getColumn(2).setPreferredWidth(210);	// ���i
		tcm.getColumn(3).setPreferredWidth(60);		// ���ݐ�
		tcm.getColumn(4).setPreferredWidth(60);		// �o�ɐ�
		tcm.getColumn(5).setPreferredWidth(90);		// �o�ɋ敪
		tcm.getColumn(6).setPreferredWidth(48);		// �폜
		tcm.getColumn(6).setResizable(false);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnSearchSlip = new javax.swing.JButton();
        btnRegist = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        try
        {
            ConnectionWrapper	con	=	SystemInfo.getConnection();

            mss.load(con, false);

            con.close();

            mss.add(0, new MstSupplier());
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        supplierName = new JComboBox(new DefaultComboBoxModel(mss.toArray())
            {
                public void setSelectedItem(Object o)
                {
                    super.setSelectedItem(o);
                }
            });
            jLabel4 = new javax.swing.JLabel();
            checkOutDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
            jLabel5 = new javax.swing.JLabel();
            personOutName = new javax.swing.JComboBox();
            jPanel4 = new javax.swing.JPanel();
            voucherNo = new javax.swing.JTextField();
            jTabbedPane1 = new javax.swing.JTabbedPane();
            //IVS_LVTu start add 2015/09/06 New request #42897
            jTabbedPane1.setUI(new CustomTabbedPaneUI());
            //IVS_LVTu end add 2015/09/06 New request #42897
            panelForBuiness = new javax.swing.JPanel();
            jScrollPane1 = new javax.swing.JScrollPane();
            tblProductDetail2 = new javax.swing.JTable();
            jScrollPane6 = new javax.swing.JScrollPane();
            tblProduct2 = new javax.swing.JTable();
            panelForShopSale = new javax.swing.JPanel();
            jScrollPane2 = new javax.swing.JScrollPane();
            tblProductDetail1 = new javax.swing.JTable();
            jScrollPane7 = new javax.swing.JScrollPane();
            tblProduct1 = new javax.swing.JTable();
            jPanel1 = new javax.swing.JPanel();
            supplierNo = new javax.swing.JFormattedTextField();
            ((PlainDocument)supplierNo.getDocument()).setDocumentFilter(
                new CustomFilter(20, CustomFilter.ALPHAMERIC));
            personOutCd = new javax.swing.JFormattedTextField();
            ((PlainDocument)personOutCd.getDocument()).setDocumentFilter(
                new CustomFilter(20, CustomFilter.ALPHAMERIC));
            panelDetail1 = new javax.swing.JScrollPane();
            tblDetailInfo = new com.geobeck.swing.JTableEx();
            inventoryDateGyoumu = new javax.swing.JLabel();
            jLabel7 = new javax.swing.JLabel();
            jLabel6 = new javax.swing.JLabel();
            jLabel8 = new javax.swing.JLabel();
            jLabel9 = new javax.swing.JLabel();
            inventoryDateTenpan = new javax.swing.JLabel();
            shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();

            setFocusCycleRoot(true);

            jLabel1.setText("�X��");

            jLabel2.setText("�`�[NO");

            btnSearchSlip.setIcon(SystemInfo.getImageIcon("/button/search/search_slip_off.jpg")
            );
            btnSearchSlip.setBorderPainted(false);
            btnSearchSlip.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_slip_on.jpg")
            );
            btnSearchSlip.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnSearchSlipActionPerformed(evt);
                }
            });

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

            btnDelete.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg")
            );
            btnDelete.setBorderPainted(false);
            btnDelete.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg")
            );
            btnDelete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnDeleteActionPerformed(evt);
                }
            });

            btnClear.setIcon(SystemInfo.getImageIcon("/button/common/clear_off.jpg")
            );
            btnClear.setBorderPainted(false);
            btnClear.setPressedIcon(SystemInfo.getImageIcon("/button/common/clear_on.jpg")
            );
            btnClear.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnClearActionPerformed(evt);
                }
            });

            jLabel3.setText("�d����");

            supplierName.setMaximumRowCount(15);
            supplierName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            supplierName.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    supplierNameActionPerformed(evt);
                }
            });

            jLabel4.setText("�o�ɓ�");

            checkOutDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            checkOutDate.setDate(new java.util.Date());
            checkOutDate.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    checkOutDateItemStateChanged(evt);
                }
            });

            jLabel5.setText("�o�ɒS����");

            personOutName.setMaximumRowCount(15);
            personOutName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            personOutName.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    personOutNameActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 341, Short.MAX_VALUE)
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 138, Short.MAX_VALUE)
            );

            voucherNo.setEditable(false);
            voucherNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            voucherNo.setText("�@�@<�V�K>");
            voucherNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
            jScrollPane1.setViewportView(tblProductDetail2);

            jScrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
            jScrollPane6.setViewportView(tblProduct2);

            javax.swing.GroupLayout panelForBuinessLayout = new javax.swing.GroupLayout(panelForBuiness);
            panelForBuiness.setLayout(panelForBuinessLayout);
            panelForBuinessLayout.setHorizontalGroup(
                panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelForBuinessLayout.createSequentialGroup()
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
            );
            panelForBuinessLayout.setVerticalGroup(
                panelForBuinessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
            );

            jTabbedPane1.addTab("�Ɩ��p", panelForBuiness);

            jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
            jScrollPane2.setViewportView(tblProductDetail1);

            jScrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
            jScrollPane7.setViewportView(tblProduct1);

            javax.swing.GroupLayout panelForShopSaleLayout = new javax.swing.GroupLayout(panelForShopSale);
            panelForShopSale.setLayout(panelForShopSaleLayout);
            panelForShopSaleLayout.setHorizontalGroup(
                panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelForShopSaleLayout.createSequentialGroup()
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
            );
            panelForShopSaleLayout.setVerticalGroup(
                panelForShopSaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
            );

            jTabbedPane1.addTab("�X�̗p", panelForShopSale);

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 809, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 161, Short.MAX_VALUE)
            );

            supplierNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            supplierNo.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    supplierNoFocusLost(evt);
                }
            });

            personOutCd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            personOutCd.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    personOutCdFocusLost(evt);
                }
            });

            panelDetail1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            tblDetailInfo.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "�敪", "����", "���i��", "���ݐ�", "�o�ɐ�", "�o�ɋ敪", "�폜"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, true, true, true
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblDetailInfo.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblDetailInfo.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tblDetailInfo.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(tblDetailInfo, SystemInfo.getTableHeaderRenderer());
            tblDetailInfo.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            SelectTableCellRenderer.setSelectTableCellRenderer(tblDetailInfo);
            panelDetail1.setViewportView(tblDetailInfo);

            inventoryDateGyoumu.setForeground(java.awt.Color.red);
            inventoryDateGyoumu.setText("2008�N12��31��");

            jLabel7.setForeground(java.awt.Color.red);
            jLabel7.setText("<html>�����ݐ��͑O��I�����̎��݌ɐ�����<br>�@ ���o�ɐ��̍����Ōv�Z���Ă��܂��B</html>");

            jLabel6.setForeground(java.awt.Color.red);
            jLabel6.setText("�y�O��I�����z");

            jLabel8.setForeground(java.awt.Color.red);
            jLabel8.setText("�X�̗p �F");

            jLabel9.setForeground(java.awt.Color.red);
            jLabel9.setText("�Ɩ��p �F");

            inventoryDateTenpan.setForeground(java.awt.Color.red);
            inventoryDateTenpan.setText("2008�N12��31��");

            shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            shop.setMaximumRowCount(15);
            shop.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    shopItemStateChanged(evt);
                }
            });
            shop.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    shopActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(1263, 1263, 1263)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(2935, 2935, 2935)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(28, 28, 28)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel8)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(inventoryDateTenpan))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel9)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(inventoryDateGyoumu))))
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel1))
                                    .addGap(16, 16, 16)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(voucherNo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnSearchSlip, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(144, 144, 144)
                                            .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(7, 7, 7)
                                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(supplierNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(supplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(24, 24, 24)
                                            .addComponent(jLabel4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(checkOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(27, 27, 27)
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(personOutCd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(personOutName, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(panelDetail1, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(1392, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(voucherNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnSearchSlip, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(supplierNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(supplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(personOutCd, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(personOutName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(checkOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(inventoryDateGyoumu, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(inventoryDateTenpan, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(15, 15, 15)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)))
                    .addGap(17, 17, 17)
                    .addComponent(panelDetail1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(363, 363, 363)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(669, 669, 669)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.getAccessibleContext().setAccessibleName("");
        }// </editor-fold>//GEN-END:initComponents

    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
//���i���X�g���X�V����
        tblProduct1DoClick();
        tblProduct2DoClick();
        SwingUtil.clearTable(tblDetailInfo);
    }//GEN-LAST:event_shopActionPerformed
	
	private void tblProduct1MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblProduct1MouseReleased
	{//GEN-HEADEREND:event_tblProduct1MouseReleased
		tblProduct1DoClick();
	}//GEN-LAST:event_tblProduct1MouseReleased
	
	private void tblProduct1KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblProduct1KeyReleased
	{//GEN-HEADEREND:event_tblProduct1KeyReleased
		tblProduct1DoClick();
	}//GEN-LAST:event_tblProduct1KeyReleased
	
	private void tblProduct2MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblProduct2MouseReleased
	{//GEN-HEADEREND:event_tblProduct2MouseReleased
		tblProduct2DoClick();
	}//GEN-LAST:event_tblProduct2MouseReleased
	
	private void tblProduct2KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblProduct2KeyReleased
	{//GEN-HEADEREND:event_tblProduct2KeyReleased
		tblProduct2DoClick();
	}//GEN-LAST:event_tblProduct2KeyReleased
	
	private void checkOutDateItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_checkOutDateItemStateChanged
	{//GEN-HEADEREND:event_checkOutDateItemStateChanged
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			calcStock(con);
			showStock();
			setInventoryDate(con);
		}
		catch (RuntimeException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		}
		catch (Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1099),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}//GEN-LAST:event_checkOutDateItemStateChanged
	
	private void personOutNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_personOutNameActionPerformed
	{//GEN-HEADEREND:event_personOutNameActionPerformed
		UIUtil.outputStaff(personOutName, personOutCd);
	}//GEN-LAST:event_personOutNameActionPerformed
	
	private void personOutCdFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_personOutCdFocusLost
	{//GEN-HEADEREND:event_personOutCdFocusLost
		UIUtil.selectStaff(personOutCd, personOutName);
	}//GEN-LAST:event_personOutCdFocusLost
	
	private void supplierNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_supplierNameActionPerformed
	{//GEN-HEADEREND:event_supplierNameActionPerformed

            supplierName.hidePopup();

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                UIUtil.outputSupplier(supplierName, supplierNo);
                
		//���i���X�g���X�V����
		tblProduct1DoClick();
		tblProduct2DoClick();

		SwingUtil.clearTable(tblDetailInfo);
                
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
                
	}//GEN-LAST:event_supplierNameActionPerformed
	
	private void supplierNoFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_supplierNoFocusLost
	{//GEN-HEADEREND:event_supplierNoFocusLost
		UIUtil.selectSupplier(supplierNo, supplierName);

		//���i���X�g���X�V����
		tblProduct1DoClick();
		tblProduct2DoClick();

		SwingUtil.clearTable(tblDetailInfo);
	}//GEN-LAST:event_supplierNoFocusLost
	
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
		clear();
    }//GEN-LAST:event_btnClearActionPerformed
	
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
		if (MessageDialog.showYesNoDialog(this,
			MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, "�o�ɓ`�["),
			this.getTitle(),
			JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
		{
			return;
		}
		
		//�`�[�����폜����
		DataSlipShip dss =  new DataSlipShip();
		dss.setShopId(((MstShop)shop.getSelectedItem()).getShopID());
		//�V�K�̏ꍇ�A�폜�ł��܂���
		if (!CheckUtil.isNumeric(voucherNo.getText()))
		{
			clear();
			return;
		}
		dss.setSlipNo(Integer.parseInt(voucherNo.getText()));
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			try
			{
				con.begin();

				// �R�t�����ɓ`�[�����폜����
				DataSlipStore.deleteAllByShipSlipNo(con, dss.getShopId(), dss.getSlipNo());

				//�`�[�ڍ׏����폜����
				DataSlipShipDetail dssd = new DataSlipShipDetail();
				dssd.setShopId(dss.getShopId());
				dssd.setSlipNo(dss.getSlipNo());

				dssd.delete(con);

				// �`�[�����폜����
				dss.delete(con);

				con.commit();

				//������Ԃɖ߂�
				clear();

				calcStock(con);

				MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.INFO_DELETE_SUCCESS),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
			}
			catch (Exception e)
			{
				con.rollback();
				throw e;
			}
			finally
			{
				con.close();
			}
		}
		catch (RuntimeException ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw ex;
		}
		catch (Exception ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED, "�o�ɓ`�["),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
		}
    }//GEN-LAST:event_btnDeleteActionPerformed
	
    private void btnRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistActionPerformed
                //IVS_LVTu start add 2015/10/14 Bug #43499
                if(tblDetailInfo.isEditing())
                {
                    tblDetailInfo.getCellEditor().stopCellEditing();
                }
                //IVS_LVTu end add 2015/10/14 Bug #43499
		//�`�[�����X�V����
		if (!inputCheck())
		{
			return;
		}
		
		if (MessageDialog.showYesNoDialog(this,
			MessageUtil.getMessage(MessageUtil.CONFIRM_REGIST, "�o�ɓ`�["),
			this.getTitle(),
			JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
		{
			return;
		}
		
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			try
			{
				con.begin();
				
				DataSlipShip dss = new DataSlipShip();
				//�V�K�̏ꍇ
				if (getNewFlag() == 1)
				{
					dss.setShopId(((MstShop)shop.getSelectedItem()).getShopID());
					
					//�`�[NO�𐶐�����
					dss.setNewSlipNo(con);
					
					//==================
					//�ڍד��e��ݒu����
					//==================
					// �d����
					dss.setSupplierId(UIUtil.getSupplierID(supplierName));
					
					// �o�ɓ��t
					dss.setShipDate(checkOutDate.getDate());
					
					// �S����
					MstStaff mstStaff = (MstStaff) personOutName.getSelectedItem();
					dss.setStaffId(mstStaff.getStaffID());
					
					//�}������
					dss.insert(con);
				}
				else
				{
					dss.setShopId(((MstShop)shop.getSelectedItem()).getShopID());
					dss.setSlipNo(Integer.parseInt(voucherNo.getText()));
					// �d����
					dss.setSupplierId(UIUtil.getSupplierID(supplierName));
					// �o�ɓ��t
					dss.setShipDate(checkOutDate.getDate());
					// �S����
					MstStaff mstStaff = (MstStaff) personOutName.getSelectedItem();
					dss.setStaffId(mstStaff.getStaffID());

					dss.update(con);
				}

				//�`�[�ڍ׏����X�V����
				updateSlipInfo(con, tblDetailInfo, dss);

				//====================
				// ���ɓ`�[���쐬����
				//====================
				// ���ɓ`�[
				DataSlipStore slipStore = new DataSlipStore();
				slipStore.setShopId(dss.getShopId());
				slipStore.setShipSlipNo(dss.getSlipNo());
				if (!slipStore.loadByShipSlipNo(con))
				{
					slipStore.setNewSlipNo(con);
				}

				slipStore.setSupplierId(dss.getSupplierId());
				slipStore.setStoreDate(dss.getShipDate());
				slipStore.setStaffId(dss.getStaffId());
				slipStore.setDiscount(0);
				slipStore.setOrderSlipNo(null);
				
				List<DataSlipStoreDetail> storeDetailList = new ArrayList<DataSlipStoreDetail>();
				DataSlipShipDetail[] detailArray = dss.getDetail();
				for (DataSlipShipDetail dssd : detailArray)
				{
					int outnum = dssd.getOutNum();
					if (outnum == 0)
					{
						continue;
					}

					if (dssd.getOutClass() == 2)
					{
						// �X�́ˋƖ�
						DataSlipStoreDetail d = new DataSlipStoreDetail();
						d.setShopId(slipStore.getShopId());
						d.setSlipNo(slipStore.getSlipNo());
						d.setSlipDetailNo(storeDetailList.size() + 1);
						d.setItemId(dssd.getItemId());
						d.setItemUseDivision(2);
						d.setInNum(outnum);
						d.setInClass(dssd.getOutClass());
						d.setCostPrice(dssd.getCostPrice());
						storeDetailList.add(d);
					}
					else if (dssd.getOutClass() == 3)
					{
						// �Ɩ��˓X��
						DataSlipStoreDetail d = new DataSlipStoreDetail();
						d.setShopId(slipStore.getShopId());
						d.setSlipNo(slipStore.getSlipNo());
						d.setSlipDetailNo(storeDetailList.size() + 1);
						d.setItemId(dssd.getItemId());
						d.setItemUseDivision(1);
						d.setInNum(outnum);
						d.setInClass(dssd.getOutClass());
						d.setCostPrice(dssd.getCostPrice());
						storeDetailList.add(d);
					}
				}

				DataSlipStore.physicalDeleteByShipSlipNo(con, slipStore.getShopId(), slipStore.getShipSlipNo());
				DataSlipStoreDetail.physicalDelete(con, slipStore.getShopId(), slipStore.getSlipNo());
				if (storeDetailList.size() > 0)
				{
					slipStore.insert(con);

					for (DataSlipStoreDetail d : storeDetailList)
					{
						if (d.isExists(con))
						{
							d.update(con);
						}
						else
						{
							d.insert(con);
						}
					}
				}
				
				con.commit();

				calcStock(con);
				loadSlip(con, dss.getShopId(), dss.getSlipNo());
				setNewFlag(0);

				MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
			}
			catch (Exception e)
			{
				con.rollback();
				throw e;
			}
			finally
			{
				con.close();
			}
		}
		catch (RuntimeException ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw ex;
		}
		catch (Exception ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "�o�ɓ`�["),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
    }//GEN-LAST:event_btnRegistActionPerformed
	
    private void btnSearchSlipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSlipActionPerformed
		loadSlip();
    }//GEN-LAST:event_btnSearchSlipActionPerformed

	/**
	 * FocusTraversalPolicy
	 */
	private	FocusTraversalPolicy traversalPolicy	=
		new RegisterCheckOutVoucherPanelFocusTraversalPolicy();
	
	/**
	 * FocusTraversalPolicy���擾����B
	 * @return FocusTraversalPolicy
	 */
	public FocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	traversalPolicy;
	}

	private boolean inputCheck()
	{
		if (supplierName.getSelectedIndex() <= 0)
		{
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�d����"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
			supplierName.requestFocusInWindow();
			return	false;
		}

		if (checkOutDate.getDate() == null)
		{
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�o�ɓ�"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
			checkOutDate.requestFocusInWindow();
			return	false;
		}

		if (personOutName.getSelectedIndex() <= 0)
		{
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�o�ɒS����"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
			personOutName.requestFocusInWindow();
			return	false;
		}

		if (tblDetailInfo.getRowCount() == 0)
		{
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "�o�ɂ��鏤�i"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return	false;
		}

		//�o�ɐ��f�t�H���g�l��ݒu����
		for (int i = 0; i < tblDetailInfo.getRowCount(); i++)
		{
			Integer def = 0;
			if (tblDetailInfo.getValueAt(i, 4) != null)
			{
				def  = (Integer) tblDetailInfo.getValueAt(i, 4);
			}
			if (def == null)
			{
				tblDetailInfo.setValueAt(0, i, 4);
			}
		}
		for (int i = 0; i < tblDetailInfo.getRowCount(); i++)
		{
			// �o�ɐ����擾����
			Integer outCnt = (Integer) tblDetailInfo.getValueAt(i, 4);
			if (outCnt == null || outCnt == 0)
			{
				continue;
			}
			// �o�ɐ���0�łȂ���΁A�o�ɋ敪���`�F�b�N����
			OutClass oc = (OutClass)tblDetailInfo.getValueAt(i, 5);
			if (oc == null || oc.getId() == 0)
			{
				//error
				MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�o�ɋ敪"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
				tblDetailInfo.requestFocusInWindow();
				return false;
				
			}
		}

		//�o�ɐ������͂���Ă��邩�̃`�F�b�N
		for (int i = 0; i < tblDetailInfo.getRowCount(); i++)
		{
			Integer num = (Integer) tblDetailInfo.getValueAt(i,4);
			if (num != null && num < 0)
			{
                            MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�o�ɐ�"),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                            return false;
			}
		}

		boolean valid = false;
		for (int i = 0; i < tblDetailInfo.getRowCount(); i++)
		{
			Integer num = (Integer) tblDetailInfo.getValueAt(i,4);
			if (num != null && num > 0)
			{
				valid = true;
				break;
			}
		}

		if (!valid)
		{
			//error
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "�o�ɐ�"),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		for (int i = 0; i < tblDetailInfo.getRowCount(); i++)
		{
			//�o�ɐ������ݐ������`�F�b�N����
			int nowCount = (tblDetailInfo.getValueAt(i,3) == null ? 0 : (Integer) tblDetailInfo.getValueAt(i,3));
			int outCount = (tblDetailInfo.getValueAt(i,4) == null ? 0 : (Integer) tblDetailInfo.getValueAt(i,4));
			
			if (outCount > 0 && outCount > nowCount)
			{
				//error
				int result = MessageDialog.showYesNoDialog(this,
					MessageUtil.getMessage(12001),
					this.getTitle(),
					JOptionPane.QUESTION_MESSAGE);
				if (result != JOptionPane.YES_OPTION)
				{
					return  false;
				}
				else
				{
					return  true;
				}
			}
		}
		
		return true;
	}
	
	private void updateSlipInfo(ConnectionWrapper con, JTable tbl,DataSlipShip dss) throws SQLException
	{
		DataSlipShipDetail.physicalDelete(con, dss.getShopId(), dss.getSlipNo());

		int currentDetailNo = 0;
		for (int i = 0; i < tbl.getRowCount(); i++)
		{
			DataSlipShipDetail dssd = new DataSlipShipDetail();
			dssd.setShopId(dss.getShopId());
			dssd.setSlipNo(dss.getSlipNo());
			
			MstSupplierItem item = (MstSupplierItem)tbl.getValueAt(i,2);
			dssd.setItemId(item.getItemID());
			
			//�o�ɐ�
			int num = tbl.getValueAt(i,4) != null ? (Integer) tbl.getValueAt(i,4) : 0;
			if (num == 0)
			{
				continue;
			}

			dssd.setOutNum(num);
			dssd.setCostPrice(item.getCostPrice());

			//�o�ɋ敪�@�b���̂���
			dssd.setOutClass((((OutClass)tbl.getValueAt(i,5))).getId());
			dssd.setItemUseDivision(((TableRecord)tbl.getValueAt(i,0)).getSelectItemUseDivision());

			//�V�K
			if (currentDetailNo == 0)
			{
				dssd.setNewSlipDetailNo(con);
				currentDetailNo = dssd.getSlipDetailNo();
			}
			else
			{
				dssd.setSlipDetailNo(++currentDetailNo);
			}

			//detail no create
			dssd.insert(con);
			dss.addDetail(dssd);
		}
	}
	
    private void tblProductDetail2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductDetail2MouseClicked
		if (evt.getClickCount() == 2 && evt.getButton() == 1)
		{
			//�ǉ����i�����X�g�ɑ��݂��ǂ����m�F����
			if (!singleCheck(tblDetailInfo, (MstItem) tblProductDetail2.getValueAt(tblProductDetail2.getSelectedRow(),0), 2))
			{
				return;
			}
			//�C�x���g�t���b�O��ݒu����
			setEventFlg(0);
			
			MstItem item = (MstItem)tblProductDetail2.getValueAt(tblProductDetail2.getSelectedRow(), 0);
                        
                        try {
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            addOneDetailInfo(tblDetailInfo,tblProduct2,tblProductDetail2, 2);
                        } finally {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
			
			//�C�x���g�t���b�O��ݒu����
			setEventFlg(1);
		}
		
    }//GEN-LAST:event_tblProductDetail2MouseClicked
	
	private void tblProduct2DoClick()
	{
		//���i���X�g������������
		initJTable(tblProductDetail2);
		
		if (tblProduct2.getSelectedRow() < 0)
		{
			return;
		}
		
		MstItemClass mic = (MstItemClass)tblProduct2.getValueAt(tblProduct2.getSelectedRow(),0);
		
		if (mic == null || supplierNo.getText() == null || "".equals(supplierNo.getText()))
		{
			return;
		}

		MstShop mstShop = (MstShop) shop.getSelectedItem();

		MstSupplierItems msis = new MstSupplierItems(mic.getItemClassID(), 2, UIUtil.getSupplierID(supplierName));
		msis.setShopId(mstShop.getShopID());
		try
		{
			msis.load(SystemInfo.getConnection());
			for (int i = 0; i < msis.size(); i++)
			{
				if (i == tblProductDetail2.getModel().getRowCount())
				{
					((DefaultTableModel)tblProductDetail2.getModel()).addRow(new Vector());
				}
				tblProductDetail2.setValueAt(msis.get(i), i, 0) ;
			}
		}
		catch (RuntimeException ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw ex;
		}
		catch (Exception ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1099),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
    private void tblProductDetail1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductDetail1MouseClicked
		if (evt.getClickCount() == 2 && evt.getButton() == 1)
		{
			//�ǉ����i�����X�g�ɑ��݂��ǂ����m�F����
			if (!singleCheck(tblDetailInfo,(MstItem) tblProductDetail1.getValueAt(tblProductDetail1.getSelectedRow(), 0), 1))
			{
				return;
			}
			
			//�C�x���g�t���b�O��ݒu����
			setEventFlg(0);
			
			MstItem item = (MstItem)tblProductDetail1.getValueAt(tblProductDetail1.getSelectedRow(), 0);
                        
                        try {
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            addOneDetailInfo(tblDetailInfo, tblProduct1, tblProductDetail1, 1);
                        } finally {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
			
			//�C�x���g�t���b�O��ݒu����
			setEventFlg(1);
		}
		
    }//GEN-LAST:event_tblProductDetail1MouseClicked

    // IVS start add 20220920 �{�����O�C�����ɏ����\���̓��t�̂܂܂��ƌ��ݐ�����������
    private void shopItemStateChanged(java.awt.event.ItemEvent evt) {
        try
        {
                ConnectionWrapper con = SystemInfo.getConnection();
                calcStock(con);
                setInventoryDate(con);
        }
        catch (RuntimeException e)
        {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                throw e;
        }
        catch (Exception e)
        {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
        }
    }
    // IVS end add 20220920 �{�����O�C�����ɏ����\���̓��t�̂܂܂��ƌ��ݐ�����������
	
	private void addOneDetailInfo(JTable tblDetailInfo, JTable tblProduct, JTable tblProductDetail, int itemUseDivision)
	{
		Vector vec = new Vector();
		
		//�p�r��ݒu����
		MstItem item = (MstItem) tblProductDetail.getValueAt(tblProductDetail.getSelectedRow(), 0);
		
		TableRecord rec = new TableRecord();
		rec.setItemId(item.getItemID());
		rec.setSelectItemUseDivision(itemUseDivision);
                rec.setItemUseDivision(item.getItemUseDivision());
		vec.add(rec);

		MstItemClass mstItemClass = classes.lookup(item.getItemClass().getItemClassID());
		vec.add(mstItemClass);
		vec.add(item);

		//���ݐ���ݒu����
                try {
                    ConnectionWrapper con = SystemInfo.getConnection();

                    int stockValue = stock.getStockFromDB(con, rec.getItemId(), rec.getSelectItemUseDivision());
                    vec.add(stockValue);

                } catch (Exception e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }

		// �o�ɐ�
		vec.add(null);

		// �o�ɋ敪
		// �Ɩ��p�Ȃ�f�t�H���g���Ɩ��o�ɂ�
		if (itemUseDivision == 2)
		{
			vec.add(new OutClass(1,"�Ɩ��o��"));
		}
		else
		{
			vec.add(null);
		}

		// �폜�{�^��
		vec.add(getDeleteButton());

		//��s��ǉ�����
		//int rowIndex = findInsertPoint(tblDetailInfo, itemUseDivision, item);
		//((DefaultTableModel)tblDetailInfo.getModel()).insertRow(rowIndex, vec);
		((DefaultTableModel)tblDetailInfo.getModel()).addRow(vec);
                
                tblDetailInfo.changeSelection(tblDetailInfo.getRowCount() - 1, 4, false, false);
                tblDetailInfo.requestFocusInWindow();
	}

	private int findInsertPoint(JTable tblDetailInfo, int itemUseDivision, MstItem item)
	{
		int itemClassDispSeq = classes.lookup(item.getItemClass().getItemClassID()).getDisplaySeq();
		int itemDispSeq = item.getDisplaySeq();
		
		int cnt = tblDetailInfo.getRowCount();
		for (int i = 0; i < cnt; i++)
		{
			// �Ɩ��p����ɕ\��
			int rowItemUseDivision = ((TableRecord) tblDetailInfo.getValueAt(i, 0)).getSelectItemUseDivision();
			if (rowItemUseDivision < itemUseDivision)
			{
				return i;
			}
			else if (rowItemUseDivision == itemUseDivision)
			{
				MstItem rowItem = (MstItem) tblDetailInfo.getValueAt(i, 2);
				MstItemClass cls = classes.lookup(rowItem.getItemClass().getItemClassID());
				if (cls.getDisplaySeq() > itemClassDispSeq)
				{
					return i;
				}
				else if (cls.getDisplaySeq() == itemClassDispSeq)
				{
					if (rowItem.getDisplaySeq() > itemDispSeq)
					{
						return i;
					}
				}
			}
		}
		
		return cnt;
	}

	private void tblProduct1DoClick()
	{
		//���i���X�g������������
		initJTable(tblProductDetail1);
		
		if (tblProduct1.getSelectedRow() < 0)
		{
			return;
		}
		
		MstItemClass mic = (MstItemClass)tblProduct1.getValueAt(tblProduct1.getSelectedRow(),0);
		
		if (mic == null || supplierNo.getText() == null || "".equals(supplierNo.getText()))
		{
			return;
		}

		MstShop mstShop = (MstShop) shop.getSelectedItem();

		MstSupplierItems msis = new MstSupplierItems(mic.getItemClassID(), 1, UIUtil.getSupplierID(supplierName));
		msis.setShopId(mstShop.getShopID());
		try
		{
			msis.load(SystemInfo.getConnection());

			for (int i = 0; i < msis.size(); i++)
			{
				if (i == tblProductDetail1.getModel().getRowCount())
				{
					((DefaultTableModel)tblProductDetail1.getModel()).addRow(new Vector());
				}
				tblProductDetail1.setValueAt(msis.get(i), i, 0) ;
			}
		}
		catch (RuntimeException ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw ex;
		}
		catch (Exception ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1099),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * �`�[�����{�^������������
	 */
	public void loadSlip()
	{
		SystemInfo.getLogger().log(Level.INFO, "�`�[����");
		SearchSlipDialog	ssp	=	new SearchSlipDialog(parentFrame, true, SearchSlip.SlipType.SHIP);
		ssp.setShop((MstShop) shop.getSelectedItem());
		ssp.setVisible(true);
		
		//�`�[���I�����ꂽ�ꍇ
		if(ssp.getSelectedSlip() != null)
		{
			SlipData sd = ssp.getSelectedSlip();

			try
			{
				ConnectionWrapper con = SystemInfo.getConnection();
				try
				{
					loadSlip(con, sd.getShopID(), sd.getSlipNo());
					//�V�K�t���O��ݒu����
					setNewFlag(0);
				}
				finally
				{
					con.close();
				}
			}
			catch (RuntimeException ex)
			{
				ssp.dispose();
				SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
				throw ex;
			}
			catch (Exception ex)
			{
				SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
				MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(1099),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			}
		}

		ssp.dispose();
	}

	public void loadSlip(ConnectionWrapper con, int shopId, int slipNo) throws SQLException
	{
		DataSlipShip data = new DataSlipShip();
		data.setShopId(shopId);
		data.setSlipNo(slipNo);

		data.load(con);
		this.showSlipData(con, data);

		enableSearchCondition(SearchMode.SLIP_SHIP);
	}

	/*
	 *�������ʂ�\������
	 */
	private void showSlipData(ConnectionWrapper con, DataSlipShip data) throws SQLException
	{
		//clear
		clear();

		//�X��
		MstShop mstShop = new MstShop();
		mstShop.setShopID(data.getShopId());
		shop.setSelectedItem(mstShop);
		//�`�[NO
		voucherNo.setText(data.getSlipNo()+"");
		//�d����
		MstSupplier mstSupplier = new MstSupplier();
		mstSupplier.setSupplierID(data.getSupplierId());
		supplierName.setSelectedItem(mstSupplier);

		//�o�ɓ�
		checkOutDate.setDate(data.getShipDate());

		//�o�ɒS����
		MstStaff staff = new MstStaff(data.getStaffId());
		staff.load(con);
		personOutCd.setText(staff.getStaffNo());
                UIUtil.selectStaff(personOutCd, personOutName);
                
		//�`�[�ڍ׏��
		DataSlipShipDetails dssd = new DataSlipShipDetails();
		dssd.setShopId(data.getShopId());
		dssd.setSlipNo(data.getSlipNo());

		dssd.load(con, false);

		for (int i = 0; i < dssd.size(); i++ )
		{
			((DefaultTableModel)tblDetailInfo.getModel()).addRow(new Vector());

			DataSlipShipDetail detail = dssd.get(i);
			//�敪
			TableRecord rec = new TableRecord();
			rec.setItemId(detail.getItemId());
			rec.setSelectItemUseDivision(detail.getItemUseDivision());
                        //IVS_LVTu start add 2015/10/16 Bug #43499
                        rec.setItemUseDivision(detail.getItemDivision());
                        //IVS_LVTu end add 2015/10/16 Bug #43499

			tblDetailInfo.setValueAt(rec, i, 0);

			//���ށE���i���E�d�����i�i�ō��j�E�d�����i�i�Ŕ��j
			MstSupplierItem item = MstSupplierItem.findByItemId(con, detail.getItemId());
			if (item == null)
			{
				continue;
			}

			tblDetailInfo.setValueAt(classes.lookup(item.getItemClass().getItemClassID()), i, 1);
			tblDetailInfo.setValueAt(item, i, 2);

			//�o�ɐ�
			tblDetailInfo.setValueAt(new Integer(detail.getOutNum()), i, 4);

			//�݌ɋ敪
			OutClass oc = null;
			switch ( detail.getOutClass())
			{
				case 1:
					oc = new OutClass(1,"�Ɩ��o��");
					break;
				case 2:
					oc = new OutClass(2,"�X�́ˋƖ�");
					break;
				case 3:
					oc = new OutClass(3,"�Ɩ��˓X��");
					break;
				case 4:
					oc = new OutClass(4,"�݌ɒ���");
					break;
				default:
					break;
			}

			tblDetailInfo.setValueAt(oc, i, 5);

			//�폜�{�^��
			tblDetailInfo.setValueAt(getDeleteButton(), i, 6);
		}

		showStock();
	}
	
	private void initItemClass()
	{
		try
		{
			classes.loadAll(SystemInfo.getConnection());
			for (int i = 0; i < classes.size(); i++)
			{
				MstItemClass class1 = classes.get(i);

				if (i == tblProduct1.getModel().getRowCount())
				{
					((DefaultTableModel)tblProduct1.getModel()).addRow(new Vector());
				}
				tblProduct1.setValueAt(class1, i, 0) ;

				if (i == tblProduct2.getModel().getRowCount())
				{
					((DefaultTableModel)tblProduct2.getModel()).addRow(new Vector());
				}
				tblProduct2.setValueAt(class1, i, 0) ;
			}
		}
		catch (RuntimeException ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw ex;
		}
		catch (Exception ex)
		{
			SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1099),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initJTable(JTable tbl)
	{
		//clear
		DefaultTableModel model = (DefaultTableModel)tbl.getModel();
		int count =  model.getRowCount();
		for (int i = count - 1; i >= 0; i--)
		{
			model.removeRow(i);
		}
		
	}
	
	private boolean singleCheck(JTable tbl, MstItem itm, int itemUseDivision)
	{
		for (int i = 0; i < tbl.getRowCount(); i++)
		{
			TableRecord tobj = (TableRecord) tbl.getValueAt(i, 0);
			
			if (itemUseDivision == tobj.getSelectItemUseDivision() &&
				itm.getItemID().equals(tobj.getItemId()))
			{
				return false;
			}
		}
		
		return true;
	}

	public int getEventFlg()
	{
		return eventFlg;
	}
	
	public void setEventFlg(int eventFlg)
	{
		this.eventFlg = eventFlg;
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRegist;
    private javax.swing.JButton btnSearchSlip;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo checkOutDate;
    private javax.swing.JLabel inventoryDateGyoumu;
    private javax.swing.JLabel inventoryDateTenpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane panelDetail1;
    private javax.swing.JPanel panelForBuiness;
    private javax.swing.JPanel panelForShopSale;
    private javax.swing.JFormattedTextField personOutCd;
    private javax.swing.JComboBox personOutName;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JComboBox supplierName;
    private javax.swing.JFormattedTextField supplierNo;
    private com.geobeck.swing.JTableEx tblDetailInfo;
    private javax.swing.JTable tblProduct1;
    private javax.swing.JTable tblProduct2;
    private javax.swing.JTable tblProductDetail1;
    private javax.swing.JTable tblProductDetail2;
    private javax.swing.JTextField voucherNo;
    // End of variables declaration//GEN-END:variables
	
	/*
	 * �`�[�����̏ꍇ�A���������̓��͋֎~�ł�
	 */
	private void disableSearchCondition()
	{
		if (shop.getItemCount() > 1)
		{
			shop.setEnabled(false);
		}
		supplierNo.setEditable(false);
		supplierName.setEnabled(false);
		checkOutDate.setEditable(true);
		personOutCd.setEditable(true);
		personOutName.setEnabled(true);
	}
	
	public int getNewFlag()
	{
		return newFlag;
	}
	
	public void setNewFlag(int newFlag)
	{
		this.newFlag = newFlag;
	}
	
	/*
	 *�V�K��Ԃɖ߂�
	 */
	private void clear()
	{
		voucherNo.setText("���V�K��");
		
		supplierNo.setText("");
		supplierName.setSelectedIndex(0);
		
		checkOutDate.setDate(new java.util.Date());
		personOutCd.setText("");
		personOutName.setSelectedIndex(0);
		
		SwingUtil.clearTable(tblProductDetail1);
		SwingUtil.clearTable(tblProductDetail2);
		SwingUtil.clearTable(tblDetailInfo);

		enableSearchCondition(SearchMode.NONE);

		//�V�K�t���O��ݒu����
		setNewFlag(1);
	}
	
	/**
	 * �݌ɐ����擾����
	 */
	private void calcStock(ConnectionWrapper con) throws SQLException
	{
		MstShop mstShop = (MstShop) shop.getSelectedItem();
		Date date = checkOutDate.getDate();
		
		//stock = StockCalculator.calcStock(con, mstShop.getShopID(), date);
		stock = new StockCalculator(mstShop.getShopID(), date);
	}
	
	/**
	 * �݌ɐ���\������
	 */
	private void showStock()
	{
            try {
                ConnectionWrapper con = SystemInfo.getConnection();

		int cnt = tblDetailInfo.getRowCount();
		for (int i = 0; i < cnt; i++)
		{
			TableRecord r = (TableRecord) tblDetailInfo.getValueAt(i, 0);
			int stockValue = stock.getStockFromDB(con, r.getItemId(), r.getSelectItemUseDivision());

			tblDetailInfo.setValueAt(stockValue, i, 3);
		}
                
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
	}

	/*
	 * �`�[�������邢�͔����������̏ꍇ�A���������̓��͋֎~�ł�
	 */
	private void enableSearchCondition(SearchMode mode)
	{
		switch (mode)
		{
			case NONE:
				if (shop.getItemCount() > 1)
				{
					shop.setEnabled(true);
				}
				supplierNo.setEditable(true);
				supplierName.setEnabled(true);
				checkOutDate.setEditable(true);
				personOutCd.setEditable(true);
				personOutName.setEnabled(true);
				btnRegist.setEnabled(true);
				btnDelete.setEnabled(false);
				break;
			case SLIP_SHIP:
				if (shop.getItemCount() > 1)
				{
					shop.setEnabled(false);
				}
				supplierNo.setEditable(false);
				supplierName.setEnabled(false);
				checkOutDate.setEditable(true);
				personOutCd.setEditable(true);
				personOutName.setEnabled(true);
				btnRegist.setEnabled(true);
				btnDelete.setEnabled(true);
				break;
		}
	}

	/**
	 *
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(btnSearchSlip);
		SystemInfo.addMouseCursorChange(btnRegist);
		SystemInfo.addMouseCursorChange(btnDelete);
		SystemInfo.addMouseCursorChange(btnClear);
	}

	/**
	 *
	 */
	private void setKeyListener()
	{
		shop.addKeyListener(SystemInfo.getMoveNextField());
		shop.addFocusListener(SystemInfo.getSelectText());
		supplierName.addKeyListener(SystemInfo.getMoveNextField());
		supplierNo.addKeyListener(SystemInfo.getMoveNextField());
		supplierNo.addFocusListener(SystemInfo.getSelectText());
		checkOutDate.addKeyListener(SystemInfo.getMoveNextField());
		checkOutDate.addFocusListener(SystemInfo.getSelectText());
		personOutCd.addKeyListener(SystemInfo.getMoveNextField());
		personOutCd.addFocusListener(SystemInfo.getSelectText());
		personOutName.addKeyListener(SystemInfo.getMoveNextField());
	}

	/**
	 * �폜�{�^�����擾����
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
	 * ���ׂ��P�s�폜����B
	 */
	private void deleteProduct()
	{
		DefaultTableModel	model	=	(DefaultTableModel) tblDetailInfo.getModel();
		int					row = tblDetailInfo.getSelectedRow();		// �I���s
		int					modelRow = tblDetailInfo.convertRowIndexToModel(row);

		if( tblDetailInfo.getCellEditor() != null ) tblDetailInfo.getCellEditor().stopCellEditing();
		model.removeRow(modelRow);
	}


	class SelectionListener implements TableModelListener
	{
		JTable table;

		// It is necessary to keep the table since it is not possible
		// to determine the table from the event's source
		SelectionListener(JTable table)
		{
			this.table = table;
		}


		public void tableChanged(TableModelEvent e)
		{
			if (tblDetailInfo.getRowCount() == 0)
			{
				return;
			}

			//�o�ɋ敪
			final int OUT_CLASS_COLUMN = 5;

			//�������܂��A�C�e���ǉ����A�������Ȃ�
			if (getEventFlg() == 0)
			{
				return;
			}

			int column = e.getColumn();
			int row = e.getFirstRow();

			if (row >= tblDetailInfo.getRowCount())
			{
				return;
			}

			if (tblDetailInfo.getValueAt(row, OUT_CLASS_COLUMN) == null)
			{
				return;
			}

			if (column == OUT_CLASS_COLUMN)
			{
				//�o�ɋ敪���������ǂ����𔻒f����
				TableRecord rec = (TableRecord) tblDetailInfo.getValueAt(row, 0);
				int value = ((OutClass)tblDetailInfo.getValueAt(row,OUT_CLASS_COLUMN)).getId();
				
				//�`�F�b�N���Ȃ�
				if (value == 0)
				{
					return;
				}
				
				boolean errFlg = false;
				if (rec.getSelectItemUseDivision() == 1)
				{
					if (value != 2 && value != 4)
					{
						errFlg = true;
					}
				}
				else
				{
					if (value != 1 && value != 3 && value != 4)
					{
						errFlg = true;
					}
				}

                                if (rec.getItemUseDivision() != 3)
                                {
					if (value != 1 && value != 4)
					{
						errFlg = true;
					}
                                }

				if (errFlg)
				{
                                    //IVS_LVTu start edit 2015/10/14 Bug #43499
                                    if (tblDetailInfo.getCellEditor() != null) {
					tblDetailInfo.getCellEditor().cancelCellEditing();
                                    }
                                    //IVS_LVTu end edit 2015/10/14 Bug #43499
					MessageDialog.showMessageDialog(this.table,MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�o�ɋ敪"),
						"�G���[",
						JOptionPane.ERROR_MESSAGE);
					tblDetailInfo.setValueAt(new OutClass(0,""),row,OUT_CLASS_COLUMN);
				}
			}
		}
	}

	/**
	 *
	 */
	private static class OutClass
	{
		private int id;
		private String name;
		/** Creates a new instance of OutClass */
		public OutClass()
		{
		}

		public OutClass(int id, String name)
		{
			setId(id);
			setName(name);
		}

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return this.name;
		}

		public boolean equals(OutClass oc)
		{
			if (this.getId() == oc.getId()) return true;
			else return false;
		}
	}

	/**
	 *
	 */
	private static class TableRecord
	{
		private int selectItemUseDivision;
		private int itemUseDivision;
		private Integer itemId;

		public void setSelectItemUseDivision(int itemUseDivision)
		{
			this.selectItemUseDivision = itemUseDivision;
		}

		public int getSelectItemUseDivision()
		{
			return this.selectItemUseDivision;
		}

		public void setItemUseDivision(int itemUseDivision)
		{
			this.itemUseDivision = itemUseDivision;
		}

		public int getItemUseDivision()
		{
			return this.itemUseDivision;
		}

		public Integer getItemId()
		{
			return this.itemId;
		}

		public void setItemId(Integer itemId)
		{
			this.itemId = itemId;
		}

		public boolean equals(Object obj)
		{
			if (obj == null)
			{
				return false;
			}
			
			if (!(obj instanceof TableRecord))
			{
				return false;
			}
			
			TableRecord r = (TableRecord) obj;
			return this.selectItemUseDivision == r.getSelectItemUseDivision() && itemId.equals(r.getItemId());
		}

		public String toString()
		{
			switch (selectItemUseDivision)
			{
				case 1:
					return "�X�̗p";
				case 2:
					return "�Ɩ��p";
				default:
					return "";
			}
		}
	}

	private class RegisterCheckOutVoucherPanelFocusTraversalPolicy extends FocusTraversalPolicy
	{
		/**
		 * aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentAfter(Container aContainer, Component aComponent)
		{
			if (aComponent.equals(shop))
			{
				return supplierNo;
			}
			else if (aComponent.equals(supplierNo))
			{
				return supplierName;
			}
			else if (aComponent.equals(supplierName))
			{
				return checkOutDate;
			}
			else if (aComponent.equals(checkOutDate))
			{
				return personOutCd;
			}
			else if (aComponent.equals(personOutCd))
			{
				return personOutName;
			}
			else if (aComponent.equals(personOutName))
			{
				return shop;
			}
			
			return shop;
		}
		
		/**
		 * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentBefore(Container aContainer, Component aComponent)
		{
			if (aComponent.equals(personOutName))
			{
				return personOutCd;
			}
			else if (aComponent.equals(personOutCd))
			{
				return checkOutDate;
			}
			else if (aComponent.equals(checkOutDate))
			{
				return supplierName;
			}
			else if (aComponent.equals(supplierName))
			{
				return supplierNo;
			}
			else if (aComponent.equals(supplierNo))
			{
				return shop;
			}
			
			return shop;
		}
		
		/**
		 * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return shop;
		}
		
		/**
		 * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return personOutName;
		}
		
		/**
		 * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
		 * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return shop;
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
			return shop;
		}
	}
}




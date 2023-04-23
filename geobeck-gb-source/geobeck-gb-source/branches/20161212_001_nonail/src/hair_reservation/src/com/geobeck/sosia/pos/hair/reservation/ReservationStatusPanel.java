/*
 * ReservationStatusPanel.java
 *
 * Created on 2006/10/18, 15:03
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.pointcard.CardPointPrintPanel;
import com.geobeck.sosia.pos.hair.pointcard.InsertCardDialog;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.logging.*;
import java.text.*;

import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.account.PrintReceipt;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.hair.account.*;
import com.geobeck.sosia.pos.hair.customer.MstCustomerPanel;
import com.geobeck.sosia.pos.hair.data.account.DataSales;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.hair.master.company.MstResponses;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.util.JapaneseCalendar;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import org.apache.commons.collections.map.LinkedMap;
import sun.util.BuddhistCalendar;

/**
 *
 * @author  katagiri
 */
public class ReservationStatusPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx implements HairInputAccountOpener
{
	
	private final int		TIMER_DELAY	=	15000;
        //�Z�p���i�敪�h�c
	public	static	final	int	PRODUCT_DIVISION_DISCOUNT	=	0;
	public	static	final	int	PRODUCT_DIVISION_TECHNIC	=	1;
	public	static	final	int	PRODUCT_DIVISION_ITEM		=	2;
	
	/**
	 * �\��X�e�[�^�X��ʏ����N���X
	 */
	private	ReservationStatus rs			=   new ReservationStatus();
	
	/**
	 * �\�����̓��t
	 */
	private	GregorianCalendar currentDate		=   new GregorianCalendar();
	
	/**
	 * �`�[�w�b�_�f�[�^
	 */
	private	DataSales lastSales			=   new	DataSales(SystemInfo.getTypeID());

	private static ReservationTimeTablePanel rrtp  =    null;

        private boolean isLoading = false; 
        
        //IVS_NHTVINH start add 2016/10/12 New request #57738 [gb]���񂴂�API�p_�@�\�ǉ��i�t�����g��ʁj
        private RegistReservation rr = null;
        //IVS_NHTVINH start add 2016/10/12 New request #57738
        
	/** Creates new form ReservationStatusPanel */
	public ReservationStatusPanel()
	{
		super();
                
                isLoading = true;
                
		initComponents();
		addMouseCursorChange();
		this.setSize(833, 691);
		this.setPath("�\��Ǘ�");
		this.setTitle("�t�����g");
		this.setListener();
                
		this.initTableColumnWidth();
		rs.setShop((0 < shop.getItemCount() ? (MstShop)shop.getItemAt(0) : new MstShop()));
		this.init();

                //�X�ܑI��p�R���{�{�b�N�X�̐ݒ�
                if(SystemInfo.getCurrentShop().getShopID() == 0){
                    SystemInfo.initGroupShopComponents(shop, 2);
                }else{
                    // �����O���[�v�̓X�܂�ݒ�
                     if(SystemInfo.getDatabase().startsWith("pos_hair_solaria") && SystemInfo.getCurrentShop().getShopID() > 0 ){
                        shop.addItem(SystemInfo.getCurrentShop());
                    }else{
                        for (int i = 0; i < SystemInfo.getGroup().getShops().size(); i++) {
                            shop.addItem(SystemInfo.getGroup().getShops().get(i));
                        }
                        shop.setSelectedItem(SystemInfo.getCurrentShop());
                    }
                }
                
                // �c�[���`�b�v�\���b���ݒ�
		ToolTipManager tp = ToolTipManager.sharedInstance();
		tp.setDismissDelay(8000);

                isLoading = false;

                this.shopActionPerformed(null);
                
                reservationCnt.setBackground(Color.WHITE);
                zaitenCnt.setBackground(Color.WHITE);
                taitenCnt.setBackground(Color.WHITE);
                totalCnt.setBackground(Color.WHITE);

	}
	
	/**
	 * ��S���҂��Z�b�g����B
	 */
	private void setChargeStaff(String staffNo)
	{     
	    chargeStaff.setSelectedIndex(0);
		
	    for (int i = 1; i < chargeStaff.getItemCount(); i++) {
		if (((MstStaff)chargeStaff.getItemAt(i)).getStaffNo() .equals(staffNo)) {
		    chargeStaff.setSelectedIndex(i);
		    break;
		}
	    }
	}
        
        /** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        status = new javax.swing.JTabbedPane();
        reservationPanel = new javax.swing.JPanel();
        reservationScroll = new javax.swing.JScrollPane();
        reservation = new com.geobeck.swing.JTableEx()
        {
            public String getToolTipText(MouseEvent e)
            {
                int row = rowAtPoint(e.getPoint());
                TableModel m = getModel();
                return getReservationToolTip(row);
            }
        };
        printReservationListButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        statusScroll = new javax.swing.JScrollPane();
        stay = new com.geobeck.swing.JTableEx()
        {
            public String getToolTipText(MouseEvent e)
            {
                int col = columnAtPoint(e.getPoint());
                if (col < 1 || col > 3) return null;

                int row = rowAtPoint(e.getPoint());
                TableModel m = getModel();
                return getReservationToolTip(row);
            }
        };
        countTotalScrollPane = new javax.swing.JScrollPane();
        countTotal = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        customerRankLabel = new javax.swing.JLabel();
        salesPanel = new javax.swing.JPanel();
        finishScroll = new javax.swing.JScrollPane();
        sales = new javax.swing.JTable();
        salesTotalScrollPane = new javax.swing.JScrollPane();
        salesTotal = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        cancelStayButton = new javax.swing.JButton();
        registButton = new javax.swing.JButton();
        changeButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        stayButton = new javax.swing.JButton();
        cancelStartButton = new javax.swing.JButton();
        counselingSheetButton = new javax.swing.JButton();
        cardInButton = new javax.swing.JButton();
        shopLabel = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        date = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        showButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        chargeStaffNo = new javax.swing.JTextField();
        chargeStaff = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        reservationCntLabel = new javax.swing.JLabel();
        reservationCnt = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        zaitenCntLabel = new javax.swing.JLabel();
        zaitenCnt = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        taitenCntLabel = new javax.swing.JLabel();
        taitenCnt = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        totalCntLabel = new javax.swing.JLabel();
        totalCnt = new javax.swing.JTextField();
        staffSearchShow = new javax.swing.JButton();
        printCard = new javax.swing.JButton();
        searchCustomerButton = new javax.swing.JButton();
        callHistoryButton = new javax.swing.JButton();

        status.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        status.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                statusStateChanged(evt);
            }
        });

        reservationPanel.setOpaque(false);

        reservationScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        reservation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "�ڋq���", "�\�񎞊�", "�ڋqNo.", "�ڋq", "���X��", "���j���[", "��S����", "�w��", "�\��ԍ�"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reservation.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        reservation.setSelectionBackground(new java.awt.Color(220, 220, 220));
        reservation.setSelectionForeground(new java.awt.Color(0, 0, 0));
        reservation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservation.getTableHeader().setReorderingAllowed(false);
        reservation.setDefaultRenderer(String.class, new ReservationTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(reservation, SystemInfo.getTableHeaderRenderer());
        reservation.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        reservation.getTableHeader().addMouseListener(
            new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    addSortCol(e);
                }
            });
            reservation.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    reservationMouseClicked(evt);
                }
            });
            reservationScroll.setViewportView(reservation);
            reservation.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            printReservationListButton.setIcon(SystemInfo.getImageIcon("/button/common/print_off.jpg"));
            printReservationListButton.setBorderPainted(false);
            printReservationListButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/print_on.jpg"));
            printReservationListButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    printReservationListButtonActionPerformed(evt);
                }
            });

            org.jdesktop.layout.GroupLayout reservationPanelLayout = new org.jdesktop.layout.GroupLayout(reservationPanel);
            reservationPanel.setLayout(reservationPanelLayout);
            reservationPanelLayout.setHorizontalGroup(
                reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, reservationPanelLayout.createSequentialGroup()
                    .addContainerGap(709, Short.MAX_VALUE)
                    .add(printReservationListButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .add(reservationScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
            );
            reservationPanelLayout.setVerticalGroup(
                reservationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(reservationPanelLayout.createSequentialGroup()
                    .add(printReservationListButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(5, 5, 5)
                    .add(reservationScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE))
            );

            status.addTab("     �\��     ", reservationPanel);

            statusPanel.setOpaque(false);

            statusScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            stay.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null}
                },
                new String [] {
                    "�ڋq���", "�ڋqNo.", "�ڋq��", "���X��", "���j���[", "��S����", "�w��", "�\�񎞊�", "�ݓX����", "�J�n����", "�{�p����", "�I������"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    true, false, false, false, false, false, false, false, false, true, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if(columnIndex==9){
                        Object o = stay.getValueAt(rowIndex, columnIndex);
                        if( o instanceof JButton){
                            return true;
                        }
                        return false;
                    }
                    return canEdit [columnIndex];
                }
            });
            stay.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
            stay.setSelectionBackground(new java.awt.Color(220, 220, 220));
            stay.setSelectionForeground(new java.awt.Color(0, 0, 0));
            stay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            stay.getTableHeader().setReorderingAllowed(false);
            stay.setDefaultRenderer(String.class, new ReservationTableCellRenderer());
            SwingUtil.setJTableHeaderRenderer(stay, SystemInfo.getTableHeaderRenderer());
            stay.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            stay.getTableHeader().addMouseListener(
                new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent e)
                    {
                        addSortCol(e);
                    }
                });
                stay.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        stayMouseClicked(evt);
                    }
                });
                statusScroll.setViewportView(stay);
                stay.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                countTotalScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                countTotalScrollPane.setFocusable(false);
                countTotal.getTableHeader().setReorderingAllowed(false);
                countTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                SwingUtil.setJTableHeaderRenderer(countTotal, SystemInfo.getTableHeaderRenderer());

                countTotal.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null, null}
                    },
                    new String [] {
                        "���", "����", "�V�K", "2��ȏ�", "���q��"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                countTotal.setFocusable(false);
                countTotal.setRequestFocusEnabled(false);
                countTotal.setRowSelectionAllowed(false);
                countTotal.getTableHeader().setReorderingAllowed(false);
                //countTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                countTotal.setRowHeight(20);
                TableColumnModel countTotalModel = countTotal.getColumnModel();
                countTotalModel.getColumn(0).setCellEditor(new DoubleCellEditor(new JTextField()));
                countTotalModel.getColumn(1).setCellEditor(new DoubleCellEditor(new JTextField()));
                countTotalModel.getColumn(2).setCellEditor(new DoubleCellEditor(new JTextField()));
                countTotalModel.getColumn(3).setCellEditor(new DoubleCellEditor(new JTextField()));
                countTotalModel.getColumn(4).setCellEditor(new DoubleCellEditor(new JTextField()));
                SwingUtil.setJTableHeaderRenderer(countTotal, SystemInfo.getTableHeaderRenderer());
                countTotalScrollPane.setViewportView(countTotal);

                jLabel10.setText("�y���q�l���z");

                customerRankLabel.setForeground(java.awt.Color.blue);
                customerRankLabel.setText("<html>\n�� �ڋq�����N�ݒ�����Ă���ꍇ�́A<br>\n�@�@�ڋq���̐擪�Ƀ����N���\������܂��B\n</html>\n");

                org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
                statusPanel.setLayout(statusPanelLayout);
                statusPanelLayout.setHorizontalGroup(
                    statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(customerRankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 327, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(43, 43, 43)
                        .add(jLabel10)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(countTotalScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 366, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(statusScroll)
                );
                statusPanelLayout.setVerticalGroup(
                    statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                        .add(statusScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(customerRankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(countTotalScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                );

                status.addTab("�@�ݓX�E�ޓX�@", statusPanel);

                salesPanel.setOpaque(false);

                finishScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                finishScroll.setOpaque(false);

                sales.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null, null}
                    },
                    new String [] {
                        "�`�[No.", "�ڋqNo.", "�ڋq", "���X��", "��S����", "�w��", "�Z�p", "���i", "�R�[�X�_��", "���׊���", "�S�̊���", "�������z", "�����"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false, false, false, false, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                sales.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
                sales.setSelectionBackground(new java.awt.Color(220, 220, 220));
                sales.setSelectionForeground(new java.awt.Color(0, 0, 0));
                sales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                sales.getTableHeader().setReorderingAllowed(false);
                sales.setDefaultRenderer(String.class, new ReservationTableCellRenderer());
                SwingUtil.setJTableHeaderRenderer(sales, SystemInfo.getTableHeaderRenderer());
                sales.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                sales.getTableHeader().addMouseListener(
                    new MouseAdapter()
                    {
                        public void mouseClicked(MouseEvent e)
                        {
                            addSortCol(e);
                        }
                    });
                    sales.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            salesMouseClicked(evt);
                        }
                    });
                    finishScroll.setViewportView(sales);

                    salesTotalScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    salesTotalScrollPane.setFocusable(false);
                    salesTotal.getTableHeader().setReorderingAllowed(false);
                    salesTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                    SwingUtil.setJTableHeaderRenderer(salesTotal, SystemInfo.getTableHeaderRenderer());

                    salesTotal.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                            {null, null, null, null, null, null, null}
                        },
                        new String [] {
                            "�Z�p", "���i", "�R�[�X�_��", "���׊���", "�S�̊���", "�������z", "�����"
                        }
                    ) {
                        Class[] types = new Class [] {
                            java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
                    salesTotal.setFocusable(false);
                    salesTotal.setRequestFocusEnabled(false);
                    salesTotal.setRowSelectionAllowed(false);
                    salesTotal.getTableHeader().setReorderingAllowed(false);
                    //salesTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                    salesTotal.setRowHeight(20);
                    TableColumnModel salesTotalModel = salesTotal.getColumnModel();
                    salesTotalModel.getColumn(0).setCellEditor(new DoubleCellEditor(new JTextField()));
                    salesTotalModel.getColumn(1).setCellEditor(new DoubleCellEditor(new JTextField()));
                    salesTotalModel.getColumn(2).setCellEditor(new DoubleCellEditor(new JTextField()));
                    salesTotalModel.getColumn(3).setCellEditor(new DoubleCellEditor(new JTextField()));
                    salesTotalModel.getColumn(4).setCellEditor(new DoubleCellEditor(new JTextField()));
                    salesTotalModel.getColumn(5).setCellEditor(new DoubleCellEditor(new JTextField()));
                    SwingUtil.setJTableHeaderRenderer(salesTotal, SystemInfo.getTableHeaderRenderer());
                    salesTotalScrollPane.setViewportView(salesTotal);

                    org.jdesktop.layout.GroupLayout salesPanelLayout = new org.jdesktop.layout.GroupLayout(salesPanel);
                    salesPanel.setLayout(salesPanelLayout);
                    salesPanelLayout.setHorizontalGroup(
                        salesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, salesPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(salesTotalScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 492, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(finishScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                    );
                    salesPanelLayout.setVerticalGroup(
                        salesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, salesPanelLayout.createSequentialGroup()
                            .add(finishScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(salesTotalScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    );

                    status.addTab("�@ ����ꗗ �@", salesPanel);

                    jPanel2.setOpaque(false);
                    jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    cancelStayButton.setIcon(SystemInfo.getImageIcon("/button/reservation/cancel_stay_off.jpg"));
                    cancelStayButton.setBorderPainted(false);
                    cancelStayButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/cancel_stay_on.jpg"));
                    cancelStayButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            cancelStayButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(cancelStayButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 0, 92, 25));

                    registButton.setIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_off.jpg"));
                    registButton.setBorderPainted(false);
                    registButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/regist_reservation_on.jpg"));
                    registButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            registButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(registButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 92, 25));

                    changeButton.setIcon(SystemInfo.getImageIcon("/button/reservation/change_reservation_off.jpg"));
                    changeButton.setBorderPainted(false);
                    changeButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/change_reservation_on.jpg"));
                    changeButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            changeButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(changeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 92, 25));

                    deleteButton.setIcon(SystemInfo.getImageIcon("/button/reservation/delete_reservation_off.jpg"));
                    deleteButton.setBorderPainted(false);
                    deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/delete_reservation_on.jpg"));
                    deleteButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            deleteButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 92, 25));

                    finishButton.setIcon(SystemInfo.getImageIcon("/button/account/account_off.jpg"));
                    finishButton.setBorderPainted(false);
                    finishButton.setPressedIcon(SystemInfo.getImageIcon("/button/account/account_on.jpg"));
                    finishButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            finishButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(finishButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 92, 25));

                    stayButton.setIcon(SystemInfo.getImageIcon("/button/reservation/stay_off.jpg"));
                    stayButton.setBorderPainted(false);
                    stayButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/stay_on.jpg"));
                    stayButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            stayButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(stayButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, 92, 25));

                    cancelStartButton.setIcon(SystemInfo.getImageIcon("/button/reservation/cancel_start_off.jpg"));
                    cancelStartButton.setBorderPainted(false);
                    cancelStartButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/cancel_start_on.jpg"));
                    cancelStartButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            cancelStartButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(cancelStartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 92, 25));

                    counselingSheetButton.setIcon(SystemInfo.getImageIcon("/button/common/print_off.jpg"));
                    counselingSheetButton.setBorderPainted(false);
                    counselingSheetButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/print_on.jpg"));
                    counselingSheetButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            counselingSheetButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(counselingSheetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 92, 25));

                    cardInButton.setIcon(SystemInfo.getImageIcon("/button/common/card_in_off.jpg"));
                    cardInButton.setBorderPainted(false);
                    cardInButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/card_in_on.jpg"));
                    cardInButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            cardInButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(cardInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 92, 25));

                    shopLabel.setText("�X��");
                    jPanel2.add(shopLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 20));

                    shop.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            shopActionPerformed(evt);
                        }
                    });
                    jPanel2.add(shop, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 144, -1));

                    date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    date.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
                    date.addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(java.awt.event.ItemEvent evt) {
                            dateItemStateChanged(evt);
                        }
                    });
                    date.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            dateFocusGained(evt);
                        }
                    });
                    jPanel2.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 21));

                    showButton.setIcon(SystemInfo.getImageIcon("/button/common/today_off.jpg"));
                    showButton.setBorderPainted(false);
                    showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/today_on.jpg"));
                    showButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            showButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(showButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 92, 25));

                    jLabel9.setText("��S����");
                    jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

                    chargeStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            chargeStaffNoFocusLost(evt);
                        }
                    });
                    chargeStaffNo.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            chargeStaffNoActionPerformed(evt);
                        }
                    });
                    jPanel2.add(chargeStaffNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 40, 25));

                    chargeStaff.setMaximumRowCount(20);
                    chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                    chargeStaff.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            chargeStaffActionPerformed(evt);
                        }
                    });
                    jPanel2.add(chargeStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 112, 24));

                    jPanel1.setOpaque(false);

                    jLabel3.setText("�ݓX�i�I�����Ԋԋ߁E���߁j");

                    jLabel2.setText("�ݓX");

                    jLabel4.setBackground(new java.awt.Color(255, 255, 255));
                    jLabel4.setForeground(new java.awt.Color(255, 60, 60));
                    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    jLabel4.setText("��");
                    jLabel4.setOpaque(true);

                    jLabel5.setText("�ޓX");

                    jLabel1.setBackground(new java.awt.Color(255, 255, 255));
                    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    jLabel1.setText("��");
                    jLabel1.setOpaque(true);

                    jLabel6.setBackground(new java.awt.Color(255, 255, 255));
                    jLabel6.setForeground(new java.awt.Color(20, 80, 200));
                    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    jLabel6.setText("��");
                    jLabel6.setOpaque(true);

                    jLabel7.setBackground(new java.awt.Color(255, 255, 255));
                    jLabel7.setForeground(new java.awt.Color(20, 128, 20));
                    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    jLabel7.setText("��");
                    jLabel7.setOpaque(true);

                    jLabel8.setText("�{�p��");

                    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
                    jPanel1.setLayout(jPanel1Layout);
                    jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 148, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(5, 5, 5)
                            .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
                    );

                    jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, -1, 15));

                    jPanel3.setOpaque(false);

                    jPanel4.setBackground(new java.awt.Color(255, 255, 0));
                    jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(128, 128, 128)));

                    reservationCntLabel.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                    reservationCntLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    reservationCntLabel.setText("�\��Ґ�");

                    reservationCnt.setEditable(false);
                    reservationCnt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                    reservationCnt.setText("0");

                    org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
                    jPanel4.setLayout(jPanel4Layout);
                    jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                            .add(reservationCntLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(reservationCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    );
                    jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(reservationCntLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(reservationCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    );

                    jPanel5.setBackground(new java.awt.Color(204, 255, 102));
                    jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(128, 128, 128)));

                    zaitenCntLabel.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                    zaitenCntLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    zaitenCntLabel.setText("�ݓX�Ґ�");

                    zaitenCnt.setEditable(false);
                    zaitenCnt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                    zaitenCnt.setText("0");

                    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
                    jPanel5.setLayout(jPanel5Layout);
                    jPanel5Layout.setHorizontalGroup(
                        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                            .add(zaitenCntLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(zaitenCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    );
                    jPanel5Layout.setVerticalGroup(
                        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel5Layout.createSequentialGroup()
                            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(zaitenCntLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(zaitenCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    jPanel6.setBackground(new java.awt.Color(196, 225, 255));
                    jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(128, 128, 128)));

                    taitenCntLabel.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                    taitenCntLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    taitenCntLabel.setText("�ޓX�Ґ�");

                    taitenCnt.setEditable(false);
                    taitenCnt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                    taitenCnt.setText("0");

                    org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
                    jPanel6.setLayout(jPanel6Layout);
                    jPanel6Layout.setHorizontalGroup(
                        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                            .add(taitenCntLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(taitenCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    );
                    jPanel6Layout.setVerticalGroup(
                        jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel6Layout.createSequentialGroup()
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(taitenCntLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(taitenCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    jPanel7.setBackground(new java.awt.Color(255, 153, 0));
                    jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(128, 128, 128)));

                    totalCntLabel.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                    totalCntLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    totalCntLabel.setText("���q��");

                    totalCnt.setEditable(false);
                    totalCnt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                    totalCnt.setText("0");

                    org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
                    jPanel7.setLayout(jPanel7Layout);
                    jPanel7Layout.setHorizontalGroup(
                        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                            .add(totalCntLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .add(3, 3, 3)
                            .add(totalCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    );
                    jPanel7Layout.setVerticalGroup(
                        jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel7Layout.createSequentialGroup()
                            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(totalCntLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(totalCnt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
                    jPanel3.setLayout(jPanel3Layout);
                    jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel3Layout.createSequentialGroup()
                            .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(0, 0, 0)
                            .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(0, 0, 0)
                            .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(0, 0, 0)
                            .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(0, 0, 0))
                    );
                    jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    );

                    jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, -1, 25));

                    staffSearchShow.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
                    staffSearchShow.setBorderPainted(false);
                    staffSearchShow.setContentAreaFilled(false);
                    staffSearchShow.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
                    staffSearchShow.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            staffSearchShowActionPerformed(evt);
                        }
                    });
                    jPanel2.add(staffSearchShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 92, 25));

                    printCard.setIcon(SystemInfo.getImageIcon("/button/common/point_off.jpg"));
                    printCard.setBorderPainted(false);
                    printCard.setFocusable(false);
                    printCard.setPressedIcon(SystemInfo.getImageIcon("/button/common/point_on.jpg"));
                    printCard.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            printCardbackPrevious(evt);
                        }
                    });
                    jPanel2.add(printCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 92, 25));

                    searchCustomerButton.setIcon(SystemInfo.getImageIcon("/button/search/search_customer_off.jpg"));
                    searchCustomerButton.setBorderPainted(false);
                    searchCustomerButton.setPressedIcon(SystemInfo.getImageIcon("/button/search/search_customer_on.jpg"));
                    searchCustomerButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            searchCustomerButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(searchCustomerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 92, 25));

                    callHistoryButton.setIcon(SystemInfo.getImageIcon("/button/reservation/call_history_off.jpg"));
                    callHistoryButton.setBorderPainted(false);
                    callHistoryButton.setPressedIcon(SystemInfo.getImageIcon("/button/reservation/call_history_on.jpg"));
                    callHistoryButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            callHistoryButtonActionPerformed(evt);
                        }
                    });
                    jPanel2.add(callHistoryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 92, 25));

                    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
                    this.setLayout(layout);
                    layout.setHorizontalGroup(
                        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .addContainerGap()
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(status, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 818, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    layout.setVerticalGroup(
                        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(status)
                            .addContainerGap())
                    );
                }// </editor-fold>//GEN-END:initComponents
    
    private void printReservationListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReservationListButtonActionPerformed

        printReservationListButton.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.printReservationList();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//GEN-LAST:event_printReservationListButtonActionPerformed

    private void searchCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerButtonActionPerformed

        SearchCustomerDialog sc = new SearchCustomerDialog( parentFrame, true, true, ((MstShop)shop.getSelectedItem()).getShopID());
        sc.setShopID( ( SystemInfo.getSetteing().isShareCustomer() ? 0 : ( (MstShop)shop.getSelectedItem() ).getShopID() ) );
        sc.setVisible(true);
        sc.dispose();
        sc = null;
        
    }//GEN-LAST:event_searchCustomerButtonActionPerformed

    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        
        if (isLoading) return;

        this.initStaff();
        this.initButton();
        rs.setShop(this.getSelectedShop());
	currentDate.setTime(date.getDate());
	this.changeButtonEnabled();
	this.showData();
        //Add start 2013-10-31 Hoa
        ConnectionWrapper con = SystemInfo.getConnection();
        MstResponses responses = new MstResponses();
        responses.setShopId(this.getSelectedShop().getShopID());
        try {
            responses.load2(con);
            SystemInfo.setResponses(responses);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        //Add End 2013-10-31 Hoa
        
    }//GEN-LAST:event_shopActionPerformed

    private void printCardbackPrevious(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printCardbackPrevious
        
        // ���񗈓X�ē��ɂăJ�[�h����_�C�A���O��\������
        CardPointPrintPanel.ShowDialog(this.parentFrame, (MstShop)shop.getSelectedItem(), CardPointPrintPanel.NEXT_TYPE_RESERVE_DATE);
    }//GEN-LAST:event_printCardbackPrevious

	private void cardInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardInButtonActionPerformed
            Integer customer_id;
            // �J�[�h�}���҂���ʂ�\��
            customer_id = InsertCardDialog.ShowReadDialog(parentFrame);
            if( customer_id != null ){
                showReservation(customer_id);
            }
	}//GEN-LAST:event_cardInButtonActionPerformed

    private void staffSearchShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffSearchShowActionPerformed
        showData();
    }//GEN-LAST:event_staffSearchShowActionPerformed

    private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

	if (!chargeStaffNo.getText().equals("")) {
	    this.setChargeStaff(chargeStaffNo.getText());
	 }else{
	    chargeStaff.setSelectedIndex(0);
	}
	
    }//GEN-LAST:event_chargeStaffNoFocusLost

    private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

	MstStaff ms= (MstStaff)chargeStaff.getSelectedItem();
                
	if (ms != null) {
	    if (ms.getStaffID() != null) {
		chargeStaffNo.setText(ms.getStaffNo());
	    }
	    
	    if ( chargeStaff.getSelectedIndex() == 0) {
		chargeStaffNo.setText("");
	    }
	}
    }//GEN-LAST:event_chargeStaffActionPerformed

        
	private void counselingSheetButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_counselingSheetButtonActionPerformed
	{//GEN-HEADEREND:event_counselingSheetButtonActionPerformed
		this.printCounselingSheet();
	}//GEN-LAST:event_counselingSheetButtonActionPerformed

	private void salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesMouseClicked
		if( evt.getClickCount() == 2 )
		{
			this.changeSales();
		}
	}//GEN-LAST:event_salesMouseClicked

	private void dateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dateItemStateChanged
		if(date.getDate() != null &&
				String.format("%1$tY/%1$tm/%1$td", date.getDate()) != String.format("%1$tY/%1$tm/%1$td", currentDate))
		{
			if(this.checkInput())
			{       
				rs.setShop(this.getSelectedShop());
				currentDate.setTime(date.getDate());
				this.changeButtonEnabled();
				this.showData();
			}
		}
	}//GEN-LAST:event_dateItemStateChanged

	private void stayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stayMouseClicked
//		GregorianCalendar	today	=	new GregorianCalendar();
//		boolean	isToday	=	(date.getDate(today.YEAR) == today.get(today.YEAR) &&
//				date.getDate(today.MONTH) == today.get(today.MONTH) + 1 &&
//				date.getDate(today.DAY_OF_MONTH) == today.get(today.DAY_OF_MONTH));
		
		// �ݓX���R�[�h���_�u���N���b�N���ɂ͌���Ԃɉ����ď�Ԃ�ύX����
//		if( ( evt.getClickCount() == 2 )&&( isToday ) )
//			switch( getStayStatus() )
//			{
//				// �{�p�҂���Ԃ̏ꍇ�ɂ͎{�p���ɑJ�ڂ���
//				case 1:
//					this.changeStayToStart();
//					break;
//				// �{�p���̏ꍇ�ɂ͐��Z�ɑJ�ڂ���
//				case 2:
//                                         DataReservation dr = rs.getDatas().get(stay.getSelectedRow());
//                                         rs.setSlipNo(dr);                                    
//				     //���Z�ꎞ�ۑ������Ă���ꍇ
//				    if(stay.getSelectedRowCount() > 0 &&
//					      rs.getDatas().get(stay.getSelectedRow()).getSlipNo() !=0 &&                            
//						    rs.getDatas().get(stay.getSelectedRow()).getStatus() ==2){   
//					this.changeStay();	    
//				    
//				    }else{
//					this.finish();
//				    }
//				    
//				    break;
//			}
		// �ݓX���R�[�h���_�u���N���b�N���͐��Z�{�^���������Ɠ��l
		if(  evt.getClickCount() == 2  )
		{
                         //���Z�ꎞ�ۑ������Ă���ꍇ
			if(stay.getSelectedRowCount() > 0 ){
                                  DataReservation dr = rs.getDatas().get(stay.getSelectedRow());
                                  rs.setSlipNo(dr);
                                  if(rs.getDatas().get(stay.getSelectedRow()).getSlipNo() !=0 &&                            
                                        rs.getDatas().get(stay.getSelectedRow()).getStatus() ==2){                            
                             
                                        this.changeStay();

                                  }else{
                                         this.finish();
                                  }       
                        }
		}
	}//GEN-LAST:event_stayMouseClicked

	private void reservationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reservationMouseClicked

            Calendar today = SystemInfo.getCurrentShop().getSystemDate();
            boolean isToday = (date.getDate(today.YEAR) == today.get(today.YEAR) &&
				date.getDate(today.MONTH) == today.get(today.MONTH) + 1 &&
				date.getDate(today.DAY_OF_MONTH) == today.get(today.DAY_OF_MONTH));
		
		// �\�񃌃R�[�h���_�u���N���b�N���ɍݓX��ԂɕύX����
		if( ( evt.getClickCount() == 2 )&&( isToday ) )
		{
			this.changeReservationToStay();
		}
	}//GEN-LAST:event_reservationMouseClicked

	private void cancelStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelStartButtonActionPerformed
		this.cancelStart();
	}//GEN-LAST:event_cancelStartButtonActionPerformed

	private void cancelStayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelStayButtonActionPerformed
	{//GEN-HEADEREND:event_cancelStayButtonActionPerformed
		this.cancelStay();
	}//GEN-LAST:event_cancelStayButtonActionPerformed

	private void showButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showButtonActionPerformed
	{//GEN-HEADEREND:event_showButtonActionPerformed
		date.setDate(SystemInfo.getCurrentShop().getSystemDate().getTime());
	}//GEN-LAST:event_showButtonActionPerformed

	private void dateFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_dateFocusGained
	{//GEN-HEADEREND:event_dateFocusGained
		date.getInputContext().setCharacterSubsets(null);
	}//GEN-LAST:event_dateFocusGained

	private void finishButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_finishButtonActionPerformed
	{//GEN-HEADEREND:event_finishButtonActionPerformed
                //�ݓX��ޓX�^�u�p
                if(status.getSelectedIndex() == 1){
                    //IVS_LVTu start add 2016/01/18 New request #46600
                    if(stay.getSelectedRowCount() <= 0) {
                        showMeg();
                    }
                    //IVS_LVTu end add 2016/01/18 New request #46600
                         //���Z�ꎞ�ۑ������Ă���ꍇ
			if(stay.getSelectedRowCount() > 0 ){
                                  DataReservation dr = rs.getDatas().get(stay.getSelectedRow());
                                  rs.setSlipNo(dr);
                                  if(rs.getDatas().get(stay.getSelectedRow()).getSlipNo() !=0 &&                            
                                        rs.getDatas().get(stay.getSelectedRow()).getStatus() ==2){                            
                             
                                        this.changeStay();     
                                  }else{
                                         this.finish();
                                  }       
                        }
                //����ꗗ�^�u�p
		}else if(status.getSelectedIndex() == 2){
			this.changeSales();
		}
//		this.refresh();
	}//GEN-LAST:event_finishButtonActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
	{//GEN-HEADEREND:event_deleteButtonActionPerformed
		this.deleteReservation();
	}//GEN-LAST:event_deleteButtonActionPerformed

	private void changeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_changeButtonActionPerformed
	{//GEN-HEADEREND:event_changeButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "�\��m�F");
		this.changeReservation();
	}//GEN-LAST:event_changeButtonActionPerformed

	private void registButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registButtonActionPerformed
	{//GEN-HEADEREND:event_registButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "�\��o�^");

                MstShop ms = (MstShop)shop.getSelectedItem();
                //Luc start edit 20150323 Bug #35698
		//if (rrtp == null || (! rrtp.getName().equals(ms.getShopID().toString()))) {
		//    this.rrtp = new ReservationTimeTablePanel(this, (MstShop)shop.getSelectedItem());
		//    this.rrtp.setOpener(this);
                //    this.rrtp.setName(ms.getShopID().toString());
		//}
		
                //this.rrtp.setDate(date.getDate());
		//this.rrtp.registReserve( null, null );
              
                DataReservation dr = null;
                GregorianCalendar clickTime = new BuddhistCalendar();
                RegistReservationDialog frm = new RegistReservationDialog(ms, date.getDate(), rrtp, clickTime);
                SwingUtil.openAnchorDialog( this.parentFrame, true, frm,  "�\��o�^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
                boolean isReserved = frm.isReserved();
                if(isReserved) {
                    this.refresh();               
                }
                //Luc start edit 20150323 Bug #35698

	}//GEN-LAST:event_registButtonActionPerformed

	private void stayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stayButtonActionPerformed
	{//GEN-HEADEREND:event_stayButtonActionPerformed
		this.changeReservationToStay();
	}//GEN-LAST:event_stayButtonActionPerformed

	private void statusStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_statusStateChanged
	{//GEN-HEADEREND:event_statusStateChanged
            if (isLoading) return;
            this.showData();
	}//GEN-LAST:event_statusStateChanged

        private void callHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_callHistoryButtonActionPerformed

            CallHistoryDialog ch = new CallHistoryDialog(parentFrame, (MstShop)shop.getSelectedItem());
            ch.setVisible(true);
            ch.dispose();
            ch = null;

        }//GEN-LAST:event_callHistoryButtonActionPerformed

    private void chargeStaffNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chargeStaffNoActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton callHistoryButton;
    private javax.swing.JButton cancelStartButton;
    private javax.swing.JButton cancelStayButton;
    private javax.swing.JButton cardInButton;
    private javax.swing.JButton changeButton;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JButton counselingSheetButton;
    private javax.swing.JTable countTotal;
    private javax.swing.JScrollPane countTotalScrollPane;
    private javax.swing.JLabel customerRankLabel;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo date;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton finishButton;
    private javax.swing.JScrollPane finishScroll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton printCard;
    private javax.swing.JButton printReservationListButton;
    private javax.swing.JButton registButton;
    private com.geobeck.swing.JTableEx reservation;
    private javax.swing.JTextField reservationCnt;
    private javax.swing.JLabel reservationCntLabel;
    private javax.swing.JPanel reservationPanel;
    private javax.swing.JScrollPane reservationScroll;
    private javax.swing.JTable sales;
    private javax.swing.JPanel salesPanel;
    private javax.swing.JTable salesTotal;
    private javax.swing.JScrollPane salesTotalScrollPane;
    private javax.swing.JButton searchCustomerButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JButton showButton;
    private javax.swing.JButton staffSearchShow;
    private javax.swing.JTabbedPane status;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JScrollPane statusScroll;
    private com.geobeck.swing.JTableEx stay;
    private javax.swing.JButton stayButton;
    private javax.swing.JTextField taitenCnt;
    private javax.swing.JLabel taitenCntLabel;
    private javax.swing.JTextField totalCnt;
    private javax.swing.JLabel totalCntLabel;
    private javax.swing.JTextField zaitenCnt;
    private javax.swing.JLabel zaitenCntLabel;
    // End of variables declaration//GEN-END:variables
	
	
	private	javax.swing.Timer	timer	=	null;
	
	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
            SystemInfo.addMouseCursorChange(showButton);
            SystemInfo.addMouseCursorChange(stayButton);
            SystemInfo.addMouseCursorChange(cancelStayButton);
            SystemInfo.addMouseCursorChange(callHistoryButton);
            SystemInfo.addMouseCursorChange(cancelStartButton);
            SystemInfo.addMouseCursorChange(registButton);
            SystemInfo.addMouseCursorChange(changeButton);
            SystemInfo.addMouseCursorChange(deleteButton);
            SystemInfo.addMouseCursorChange(finishButton);
            SystemInfo.addMouseCursorChange(staffSearchShow);
            SystemInfo.addMouseCursorChange(cardInButton);
            SystemInfo.addMouseCursorChange(printCard);
            SystemInfo.addMouseCursorChange(counselingSheetButton);
            SystemInfo.addMouseCursorChange(searchCustomerButton);
            SystemInfo.addMouseCursorChange(printReservationListButton);
	}
	
	/**
	 * �R���|�[�l���g�̊e���X�i�[���Z�b�g����B
	 */
	private void setListener()
	{
		date.addKeyListener(SystemInfo.getMoveNextField());
		date.addFocusListener(SystemInfo.getSelectText());
        chargeStaffNo.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaffNo.addFocusListener(SystemInfo.getSelectText());
        chargeStaff.addKeyListener(SystemInfo.getMoveNextField());
        chargeStaff.addFocusListener(SystemInfo.getSelectText());
	}

        private void initStaff() {
            chargeStaff.removeAllItems();
            HairInputAccount ia = new HairInputAccount();
            for (MstStaff ms : ia.getStaffs(getSelectedShop())) {
                if (ms.isDisplay()) {
                    chargeStaff.addItem(ms);
                }
            }
            if (chargeStaff.getItemCount() > 0) {
                chargeStaff.setSelectedIndex(0);
            }
        }

	/**
	 * �������������s���B
	 */
	private void init()
	{
		// ��S���҂�ǉ�����
		this.initStaff();
            
                currentDate.setTime(date.getDate());
		
		this.initButton();
		
		//�^�C�}�[���Z�b�g
/*		timer	=	new javax.swing.Timer(TIMER_DELAY,
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						refresh();
					}
				});
		//�^�C�}�[�X�^�[�g
		timer.start();
*/		
                status.setSelectedIndex( 1 );


                if (SystemInfo.getSetteing().getRankStartDate() != null && SystemInfo.getSetteing().getRankEndDate() != null) {
                    stay.getTableHeader().getColumnModel().getColumn(2).setHeaderValue("[�����N] �ڋq��");
                } else {
                    customerRankLabel.setVisible(false);
                }
	}
	
	private void initButton()
	{
		boolean	buttonVisible = true;
                // �X�܃��O�C�����ǂ���
                buttonVisible = buttonVisible && !SystemInfo.isGroup();
                // ���X�܂��ǂ���
                // IVS SANG START EDIT 20131017
                // buttonVisible = buttonVisible && SystemInfo.getCurrentShop().equals(this.getSelectedShop());
                Integer shopId = this.getSelectedShop() == null ? null : this.getSelectedShop().getShopID();
                buttonVisible = buttonVisible && SystemInfo.getCurrentShop().getShopID().equals(shopId);
		// IVS SANG END EDIT 20131017
                
                
		stayButton.setVisible(buttonVisible);
		cancelStayButton.setVisible(buttonVisible);
		cancelStartButton.setVisible(buttonVisible);
		changeButton.setVisible(buttonVisible);
		deleteButton.setVisible(buttonVisible);
		finishButton.setVisible(buttonVisible);

		//registButton.setVisible(buttonVisible);
                
                if( SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 0 || SystemInfo.getPointOutputType() == 2) ){
                    printCard.setVisible(buttonVisible);
                    cardInButton.setVisible(buttonVisible);
                } else {
                    printCard.setVisible(false);
                    cardInButton.setVisible(false);
                }

                callHistoryButton.setVisible(SystemInfo.isUseCtiReader());
	}
	
	/**
	 * �I������Ă���X�܂��擾����B
	 * @return �I������Ă���X��
	 */
	private MstShop getSelectedShop()
	{
            if (shop != null && 0 <= shop.getSelectedIndex()) {
                return (MstShop)shop.getSelectedItem();
            } else {
                return null;
            }
	}
	
	/**
	 * �I������Ă���X�܂�ID���擾����B
	 * @return �I������Ă���X�܂�ID
	 */
	private Integer getSelectedShopID()
	{
            MstShop ms = this.getSelectedShop();
		
            if (ms != null) {
                return ms.getShopID();
            } else {
                return 0;
            }
	}
	
	/**
	 * �e�[�u���̍ĕ\�����s���B
	 */
	public void refresh()
	{
		JTable		table	=	null;
		switch(status.getSelectedIndex())
		{
			//�\��
			case 0:
				table	=	reservation;
				break;
			//�ݓX�E�ޓX
			case 1:
				table	=	stay;
				break;
			//����ꗗ
			case 2:
				table	=	sales;
				break;
		}
		
		//�I������Ă���f�[�^�̗\��No.���擾
		Integer	selNo	=	this.getSelectedNo(table);
		
		//�f�[�^���ĕ\��
		showData();
		
		if(selNo == null)	return;
		
		//�I������Ă����f�[�^���đI������
		for(int row = 0; row < table.getRowCount(); row ++)
		{
			//����ꗗ
			if(status.getSelectedIndex() == 2)
			{
				if(rs.getSales().get(row).getSlipNo() == selNo)
				{
					table.addRowSelectionInterval(row, row);
					break;
				}
			}
			//�\��A�ݓX�E�ޓX
			else
			{
				if(rs.getDatas().get(row).getReservationNo() == selNo)
				{
					table.addRowSelectionInterval(row, row);
					break;
				}
			}
		}
	}
	
	/**
	 * �I������Ă���f�[�^��No.���擾����B
	 * @param table �ΏۂƂȂ�JTable
	 * @return �I������Ă���f�[�^��No.
	 */
	private Integer getSelectedNo(JTable table)
	{
		if(table.getSelectedRow() < 0)
		{
			return	null;
		}
		
		//����ꗗ
		if(status.getSelectedIndex() == 2)
		{
			return	rs.getSales().get(table.getSelectedRow()).getSlipNo();
		}
		//�\��A�ݓX�A�{�p�E�ޓX
		else
		{
			return	rs.getDatas().get(table.getSelectedRow()).getReservationNo();
		}
	}
	
	/**
	 * ���̓`�F�b�N���s���B
	 * @return OK - true
	 */
	private boolean checkInput()
	{
		//�\���
		if(date.getDate() == null)
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
					"�\���"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
			date.requestFocusInWindow();
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * �f�[�^��\������B
	 */
	private void showData()
	{
		
                int index = status.getSelectedIndex();
                
                this.changeButtonEnabled();
		
                this.changeChargeBoxEnable(index);
                
                int staffID = -1;
		if (this.chargeStaff.getSelectedIndex() > 0) {
		    staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();
		}
                
                //���(�\��E�ݓX�E�ޓX)���Ƃ̋q����ǂݍ���
                rs.loadStatusCount(currentDate);
                reservationCnt.setText(FormatUtil.decimalFormat(rs.getReservationCnt()));
                zaitenCnt.setText(FormatUtil.decimalFormat(rs.getZaitenCnt()));
                taitenCnt.setText(FormatUtil.decimalFormat(rs.getTaitenCnt()));
                totalCnt.setText(FormatUtil.decimalFormat(rs.getTotalCnt()));

                try {

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    switch(index)
                    {
                            //�\��
                            case 0:
                                    rs.loadReservation(currentDate, 1, staffID);
                                    this.showReservation();
                                    break;
                            //�ݓX
                            case 1:
                                    rs.loadReservation(currentDate, 2, staffID);
                                    this.showStay();
                                    break;
                            //�ޓX
                            case 2:                                
                                    rs.loadSales(currentDate, staffID);
                                    this.showSales();
                                    break;
                    }
                    
                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
	}
	
        private void changeChargeBoxEnable(int status){

            // �^�u�̐؂�ւ����ɃX�^�b�t��I�����Ă������ɂ���
            //if(status != 2 && this.chargeStaff.getSelectedIndex() > 0){
            //    this.chargeStaff.setSelectedIndex(0);
            //}
            
            switch(status){
                case 0:
                    //this.chargeStaff.setEnabled(false);
                    //this.chargeStaffNo.setEnabled(false);
                    break;
                case 1:
                    //this.chargeStaff.setEnabled(false);
                    //this.chargeStaffNo.setEnabled(false);
                    break;
                case 2:
                    this.chargeStaff.setEnabled(true);
                    this.chargeStaffNo.setEnabled(true);
                    break;
            }
        }
        
	/**
	 * �{�^���̎g�p�s��ύX����B
	 */
	private void changeButtonEnabled()
	{
		boolean	enabled	=	(status.getSelectedIndex() == 0);
		GregorianCalendar	today	=	new GregorianCalendar();
		boolean	isToday	=	(date.getDate(today.YEAR) == today.get(today.YEAR) &&
				date.getDate(today.MONTH) == today.get(today.MONTH) + 1 &&
				date.getDate(today.DAY_OF_MONTH) == today.get(today.DAY_OF_MONTH));
		
		stayButton.setEnabled(enabled && isToday);
		cancelStayButton.setEnabled(status.getSelectedIndex() == 1);
		cancelStartButton.setEnabled(status.getSelectedIndex() == 1);
		changeButton.setEnabled(enabled);
		deleteButton.setEnabled(enabled);
		finishButton.setEnabled(1 <= status.getSelectedIndex());
		counselingSheetButton.setEnabled(status.getSelectedIndex() <= 1);
		
		jLabel1.setVisible(status.getSelectedIndex() == 1);
		jLabel2.setVisible(status.getSelectedIndex() == 1);
		jLabel3.setVisible(status.getSelectedIndex() == 1);
		jLabel4.setVisible(status.getSelectedIndex() == 1);
		jLabel5.setVisible(status.getSelectedIndex() == 1);
		jLabel6.setVisible(status.getSelectedIndex() == 1);
		jLabel7.setVisible(status.getSelectedIndex() == 1);
		jLabel8.setVisible(status.getSelectedIndex() == 1);
                
                //staffSearchShow.setEnabled(status.getSelectedIndex() == 2);
	}
	
	/**
	 * �\��f�[�^��\������B
	 */
	private void showReservation()
	{
		DefaultTableModel	model	=	(DefaultTableModel)reservation.getModel();
		if( reservation.getCellEditor() != null ) reservation.getCellEditor().stopCellEditing();
		model.setRowCount(0);
		SwingUtil.clearTable(reservation);
		
		for(DataReservation dr : rs.getDatas())
		{
			model.addRow(new Object[]{
                                                    getCustomerInfoButton(dr.getCustomer().getCustomerID(), dr.getCustomer().getBirthday()),
                                                    this.formatTime(dr.get(0).getReservationDatetime()),
                                                    dr.getCustomer().getCustomerNo(),
                                                    dr.getCustomer(),
                                                    dr.getCustomer().getVisitCount(),
                                                    dr.getTechnicClassContractedName(),
                                                    (dr.getStaff() == null ? "": dr.getStaff().getFullStaffName()),
                                                    (dr.getDesignated()? "��" : ""),
                                                    //IVS_LVTu start edit 2015/10/08 New request #43283
                                                    dr.getReservationNo().toString()	});
                                                    //IVS_LVTu end edit 2015/10/08 New request #43283
		}
                
                printReservationListButton.setEnabled(reservation.getRowCount() > 0);
	}
	
	/**
     * �w�肵���ڋq�̗\�����\������
     * @param customer 
     */
    private void showReservation(Integer customer_id){

        DataReservation seldr = null;
        
        ReservationStatus rss = new ReservationStatus();
        rss.loadReservation(currentDate, 1);

        for (DataReservation dr : rss.getDatas())
        {
            if (dr.getCustomer().getCustomerID().equals(customer_id)) {
                seldr = dr;
                break;
            }
        }

        if ( this.getSelectedShop() == null ) return;

        RegistReservationDialog rrp = new RegistReservationDialog( this.getSelectedShop(), date.getDate() );
        
        if ( seldr == null ) {
            // �ڋq���������p���ŗ\���ʂ��J��
            MstCustomer customer = new MstCustomer(customer_id);
            try {
                customer.load(SystemInfo.getConnection());
                rrp.setCustomer(customer);            
            } catch (SQLException ex) {
                customer = null;
            }
        } else {
            rrp.LoadReservation( seldr );
        }
        
        SwingUtil.openAnchorDialog( this.parentFrame, true, rrp, "�\��o�^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

        boolean isReserved = rrp.isReserved();

        rrp.releaceMemory();
        rrp = null;
        System.gc();

        if (isReserved) {
            
            this.refresh();
            
        } else {

            if (seldr == null) return;
            
            if (seldr.getStatus() == 1) {
                //�m�F���b�Z�[�W
                if (MessageDialog.showYesNoDialog(
                        this,
                        MessageUtil.getMessage(5126),
                        this.getTitle(),
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    // �ݓX�ɂ���
                    ConnectionWrapper con = SystemInfo.getConnection();

                    seldr.setStatus(2);
                    seldr.setSubStatus(1);

                    try {
                        if (seldr.updateStatus(con)) {

                            this.refresh();

                            MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(5002),
                                    this.getTitle(),
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            MessageDialog.showMessageDialog(this,
                                    MessageUtil.getMessage(5110),
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(5110),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }	
                
            } else {
                rrp.LoadReservation( seldr );
            }
        }

    }
    
	/**
	 * �ݓX�f�[�^��\������B
	 */
	private void showStay()
	{
                long count[] = {0,0,0,0,0};
            
		DefaultTableModel	model	=	(DefaultTableModel)stay.getModel();
		if( stay.getCellEditor() != null ) stay.getCellEditor().stopCellEditing();
		model.setRowCount(0);
		SwingUtil.clearTable(stay);
		
		for(DataReservation dr : rs.getDatas())
		{
			GregorianCalendar	finishTime	=	new GregorianCalendar();
			
			// 2007/09/21
			// �{�p�I�����Ԃ̕ύX
			//   �{�p�҂�(�\��J�n���ԑO) �F �{�p�\�񎞊� + �{�p����
			//   �{�p�҂�(�\��J�n���Ԍ�) �F ���ݎ���     + �{�p����
			//   �{�p��                   �F �{�p�J�n���� + �{�p����
			//   �{�p�I����               �F �I������
			//finishTime.setTime( ( dr.getStartTime() == null ?  Calendar.getInstance().getTime() : dr.getStartTime().getTime() ) );
			//finishTime.add(finishTime.MINUTE, dr.get(dr.size() - 1).getTechnic().getOperationTime());
			
			// �ݓX
			if( dr.getStatus() == 2 )
			{
				// �{�p�҂����
				if( dr.getSubStatus() == 1 ) 
				{
					finishTime.setTimeInMillis( ( Calendar.getInstance().getTimeInMillis() < dr.get(0).getReservationDatetime().getTimeInMillis() ?  dr.get(0).getReservationDatetime().getTimeInMillis() : Calendar.getInstance().getTimeInMillis() ) );
				}
				// �{�p��
				else 
				{
					finishTime.setTimeInMillis( dr.getStartTime().getTimeInMillis() );
				}
				// �ݓX���͎{�p���Ԃ����Z����
				finishTime.add( finishTime.MINUTE, dr.getTotalTime() );
			}
			else if ( dr.getStatus() == 3 )
			{
				// �ޓX
				finishTime = dr.getLeaveTime();
			}
			
			model.addRow(new Object[]{
                                getCustomerInfoButton(dr.getCustomer().getCustomerID(), dr.getCustomer().getBirthday()),
				dr.getCustomer().getCustomerNo(),
				dr.getCustomer(),
                                dr.getCustomer().getVisitCount(),
//				dr.getSlipNo() > 0 ? dr.getTechnicClassContractedNameList() : dr.getTechnicClassContractedName(),
                                dr.getTechnicClassContractedName(),
				(dr.getStaff() == null ? "": dr.getStaff().getFullStaffName()),
				(dr.getDesignated()? "��" : ""),
				this.formatTime(dr.get(0).getReservationDatetime()),
				this.formatTime(dr.getVisitTime()),
				(dr.getStartTime() == null ? getStartButton(): this.formatTime(dr.getStartTime())),
				dr.getTotalTime().toString(),
				this.formatTime(finishTime)
			});
                        
                        
                        if (dr.getVisitTime() != null) {

                            if ( dr.getCustomer().getCustomerNo() != null &&
                                 !dr.getCustomer().getCustomerNo().equals("0") ) {

                                // ���
                                count[0] += 1;

                                if (dr.getCustomer().getVisitCount() < 2) {
                                    // �V�K
                                    count[2] += 1;
                                } else {
                                    // 2��ȏ�
                                    count[3] += 1;
                                }
                                
                            } else {
                                // ����()
                                count[1] += 1;
                            }
                            
                            count[4] += 1;
                        }
                        
		}
                
                //---------------------------
                // ���q�l���\��
                //---------------------------
                DefaultTableModel cmodel = (DefaultTableModel)countTotal.getModel();
                for (int i = 0; i < 5; i++) {
                    cmodel.setValueAt(count[i], 0, i);
                }
	}
	
	/**
	 * ����f�[�^��\������B
	 */
	private void showSales()
	{
            //IVS_LVTu start edit 2015/10/05 New request #43048
		SwingUtil.clearTable(sales);
		DefaultTableModel	model	=	(DefaultTableModel)sales.getModel();
		Double taxRate = SystemInfo.getTaxRate(SystemInfo.getSystemDate());

                NumberFormat nf = NumberFormat.getInstance();
                
		for(SalesData sd : rs.getSales())
		{
                        //IVS_LVTu start edit 2015/10/08 New request #43283
			model.addRow(new Object[]{  sd.getSlipNo().toString(),
                        //IVS_LVTu end edit 2015/10/08 New request #43283
                                                        sd.getCustomer().getCustomerNo(),
                                                        sd.getCustomer(),
                                                        sd.getCustomer().getVisitCount(),
                                                        (sd.getStaff() == null ? "": sd.getStaff().getFullStaffName()),
                                                        (sd.getDesignated()? "��" : ""),
                                                        nf.format(sd.getTechnic()),
                                                        nf.format(sd.getItem()),
                                                        nf.format(sd.getValueCourse()),
                                                        nf.format(sd.getDiscount()),
                                                        nf.format(sd.getOverallDiscount()),
                                                        nf.format(sd.getTotalValue()),
                                                        nf.format(sd.getTax())	});
		}
                if ( rs.getShop().getCourseFlag() != null && rs.getShop().getCourseFlag() == 1) {
                    sales.getColumn("�R�[�X�_��").setWidth(80);
                    sales.getColumn("�R�[�X�_��").setMinWidth(80);
                    sales.getColumn("�R�[�X�_��").setMaxWidth(80);
                    sales.getColumn("�R�[�X�_��").setPreferredWidth(80);
                } else {
                    sales.getColumn("�R�[�X�_��").setWidth(0);
                    sales.getColumn("�R�[�X�_��").setMinWidth(0);
                    sales.getColumn("�R�[�X�_��").setMaxWidth(0);
                    sales.getColumn("�R�[�X�_��").setPreferredWidth(0);
                }

		this.showSalesTotal();
                //IVS_LVTu end edit 2015/10/05 New request #43048
	}
	
	private void showSalesTotal()
	{
                //IVS_LVTu start edit 2015/10/07 New request #43048
		salesTotal.setValueAt(rs.getTotal().getTechnic(), 0, 0);
		salesTotal.setValueAt(rs.getTotal().getItem(), 0, 1);
                salesTotal.setValueAt(rs.getTotal().getValueCourse(), 0, 2);
		salesTotal.setValueAt(rs.getTotal().getDiscount(), 0, 3);
		salesTotal.setValueAt(rs.getTotal().getOverallDiscount(), 0, 4);
//		salesTotal.setValueAt(rs.getTotal().getTotal(), 0, 4);
		salesTotal.setValueAt(rs.getTotal().getTotalValue(), 0, 5);
		salesTotal.setValueAt(rs.getTotal().getTax(), 0, 6);
                
                if ( rs.getShop().getCourseFlag() != null && rs.getShop().getCourseFlag() == 1) {
                    salesTotal.getColumn("�R�[�X�_��").setWidth(70);
                    salesTotal.getColumn("�R�[�X�_��").setMinWidth(70);
                    salesTotal.getColumn("�R�[�X�_��").setMaxWidth(70);
                    salesTotal.getColumn("�R�[�X�_��").setPreferredWidth(70);
                } else {
                    salesTotal.getColumn("�R�[�X�_��").setWidth(0);
                    salesTotal.getColumn("�R�[�X�_��").setMinWidth(0);
                    salesTotal.getColumn("�R�[�X�_��").setMaxWidth(0);
                    salesTotal.getColumn("�R�[�X�_��").setPreferredWidth(0);
                }
                //IVS_LVTu end edit 2015/10/07 New request #43048
	}
	
	/**
	 * ���Ԃ𕶎���ɕϊ�����B
	 * @param time ����
	 * @return ���Ԃ̕�����
	 */
	private String formatTime(GregorianCalendar time)
	{
		if(time == null)	return	"";
		int		hour	=	time.get(time.HOUR_OF_DAY);
		GregorianCalendar	temp	=	new GregorianCalendar();
		temp.setTime(currentDate.getTime());
		temp.add(time.DAY_OF_MONTH, 1);
		if(String.format("%1$tY/%1$tm/%1$td", time).equals(String.format("%1$tY/%1$tm/%1$td", temp)))
				hour	+=	24;
		int		minute	=	time.get(time.MINUTE);
		return	String.format("%1$2d:%2$02d", hour, minute);
	}
	
	/**
	 * �\��ꗗ��ToolTip��������擾����B
	 * @param row �\��ꗗ�̍s
	 * @return �\��ꗗ��ToolTip������
	 */
	private String getReservationToolTip(int row)
	{
                DataReservation dr = rs.getDatas().get(row);
                MstCustomer cus = dr.getCustomer();
                loadLastTimeSalesData(cus);
                //IVS_LVTu start add 2015/09/03 New request #42428
                //����\���
                Date nextReservationDate = getNextReservationDate(rs.getDatas().get(row).getShop().getShopID(), cus.getCustomerID());
                String strNextDate = "";
                if (nextReservationDate != null ) {
                strNextDate = DateUtil.format(nextReservationDate, "yyyy�NM��d�� (E)");
                }
                
                StringBuffer sb = new StringBuffer();
                sb.append("<html><table>");
                sb.append("<tr align=left><td><b>[�ڋq��]</b>�@" + cus.getFullCustomerName() + "�i" + cus.getFullCustomerKana() + "�j</td></tr>");
                sb.append("<tr align=left><td><b>[�a����]</b>�@");
                if (cus.getBirthday() != null) {
                    Integer	ageTemp	=	DateUtil.calcAge(
                                new com.ibm.icu.util.GregorianCalendar(),
                                cus.getBirthday());
                    //IVS_TMTrong start edit 2015/10/19 New request #43511
                    //IVS_LVTu start edit 2015/11/13 New request #44270
                    if(ageTemp>0){
                        //sb.append(cus.getBirthdayString("yyyy�NM��d��") + " (" + ageTemp + "��)");
                        sb.append(cus.getBirthdayString2() + " (" + ageTemp + "��)");
                    }else{
                        //sb.append(cus.getBirthdayString("yyyy�NM��d��"));
                        sb.append(cus.getBirthdayString2());
                    }
                    //IVS_LVTu end edit 2015/11/13 New request #44270
                    //IVS_TMTrong end edit 2015/10/19 New request #43511
                }
                sb.append("</td></tr>");
                //TEL
                String strPhoneNumber = cus.getCellularNumber();
                if (strPhoneNumber == null || strPhoneNumber.equals("")) {
                    if ( cus.getPhoneNumber() != null ) {
                        strPhoneNumber = cus.getPhoneNumber().equals("") ? "": cus.getPhoneNumber() ;
                    }else {
                        strPhoneNumber = "";
                    }
                }
                sb.append("<tr align=left><td><b>[TEL]</b>�@" + strPhoneNumber + "</td></tr>");
                
                if(lastSales != null) {
                    sb.append("<tr align=left><td><b>[�O�񗈓X��]</b>�@" + (lastSales.getSalesDate() == null ? "" :DateUtil.format(lastSales.getSalesDate(), "yyyy�NM��d�� (E)")) + "</td></tr>");
                    sb.append("<tr align=left><td><b>[����\���]</b>�@" + strNextDate + "</td></tr>");        
                    long days = 0;
                    if (lastSales.getSalesDate() != null) {
                        //�~���b�P�ʂō������߂�
                        days = date.getDate().getTime() - lastSales.getSalesDate().getTime();
                        //�����Ɋ��Z����i�~���b�����j
                        days = days / ( 24 * 60 * 60 * 1000 );
                    }
                    sb.append("<tr align=left><td><b>[�o�ߓ���]</b>�@" + days + "��</td></tr>");

                    String staffStr = (lastSales.getStaff() == null ? "" :lastSales.getStaff().getFullStaffName() + (lastSales.getDesignated() ? " (�w)" : " (F)"));
                    sb.append("<tr align=left><td><b>[�O��S����]</b>�@" + staffStr + "</td></tr>");
                }else {
                    sb.append("<tr align=left><td><b>[����\���]</b>�@" + strNextDate + "</td></tr>");
                }
                //IVS_LVTu end add 2015/09/03 New request #42428
                if(lastSales != null) {
                    sb.append("<tr align=left><td><b>[�\�񃁃�]</b></td></tr>");
                    sb.append("<tr align=left><td>" + formatHTMLString(insertNewLineString(dr.getComment())) + "</td></tr>");
                    sb.append("<tr align=left><td><b>[�O�񃁃�]</b></td></tr>");
                    sb.append("<tr align=left><td>" + formatHTMLString(insertNewLineString(lastSales.getVisitedMemo())) + "</td></tr>");
                    sb.append("<tr align=left><td><b>[�O��Z�p���e]</b></td></tr>");
                    for(int i = 0; i < lastSales.size(); i ++)
                        if(lastSales.get(i).getProductDivision() == PRODUCT_DIVISION_TECHNIC)
                            sb.append("<tr align=left><td>��" + lastSales.get(i).getProductName() + "</td></tr>");

                    sb.append("<tr align=left><td><b>[�O��w�����i]</b></td></tr>");
                    for(int i = 0; i < lastSales.size(); i ++)
                        if(lastSales.get(i).getProductDivision() == PRODUCT_DIVISION_ITEM)
                            sb.append("<tr align=left><td>��" + lastSales.get(i).getProductName() + "</td></tr>");
                }

                try {
                    ConnectionWrapper con = SystemInfo.getConnection();
                    
                    ArrayList list = null;
                    if (SystemInfo.getSetteing().isShareCustomer()) {
                        list = cus.getIntroduceList(con, cus.getCustomerID(), 0);
                    } else {
                        list = cus.getIntroduceList(con, cus.getCustomerID(), this.getSelectedShop().getShopID());
                    }
                    sb.append("<tr align=left><td><b>[�Љ�Ґ�]�@");
                    sb.append(list.size());
                    sb.append("�l</b></td></tr>");

                    sb.append("<tr align=left><td><b>[�݌v������z]�@��");
                    sb.append(FormatUtil.decimalFormat(getSalesTotal(cus.getCustomerID())));
                    sb.append("�~</b></td></tr>");
                    
                } catch (Exception e) {
                }

                sb.append("</table></html>");
		return sb.toString();
	}
	
	private Long getSalesTotal(Integer customerID)
	{
            Long result = 0l;
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("     coalesce(sum(discount_sales_value_in_tax), 0) as sales_total");
            sql.append(" from");
            sql.append("     view_data_sales_valid");
            sql.append(" where");
            sql.append("         sales_date is not null");
            sql.append("     and customer_id = " + SQLUtil.convertForSQL(customerID));

	    try
	    {
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

		if (rs.next()) {
                    result = rs.getLong("sales_total");
		}

		rs.close();
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return result;
	}
        
	/**
	 * �O��̓`�[�f�[�^��ǂݍ��ށB
	 * @param cus �ڋq�f�[�^
	 */
	private void loadLastTimeSalesData( MstCustomer cus ) {
		ResultSetWrapper	rs;
		try {
			//�R�l�N�V�������擾
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
                        //�O��̓`�[NO���擾
                        rs	=	con.executeQuery(this.getLastTimeSlipNoSelectSQL( cus ));
                        Integer lastSlipNo = null;
			while( rs.next() ) {
                            lastSlipNo = rs.getInt("slip_no");
			}
			rs.close();
                        
                        //�O��`�[�f�[�^���擾
			lastSales =	new DataSales(SystemInfo.getTypeID());
                        if (lastSlipNo != null) {
                            lastSales.clear();
                            lastSales.setShop(this.getSelectedShop());
                            lastSales.setSlipNo(lastSlipNo);
                            lastSales.loadAll(con);
                        }
		} catch(SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �O��̓`�[�f�[�^���擾����SQL���擾
	 * @param cus �ڋq�f�[�^
	 * @return �O��̓`�[�f�[�^���擾����SQL
	 */
	private String getLastTimeSlipNoSelectSQL( MstCustomer cus ) {
//            System.out.println("cus =" + cus);
//            System.out.println("shop = " + this.getSelectedShop());
		return String.format(
			"select\n" +
			"ds.slip_no\n" +
			"from\n" +
			"data_sales as ds\n" +
			"where\n" +
			"ds.delete_date is null\n" +
			"and ds.shop_id = " + SQLUtil.convertForSQL( this.getSelectedShop().getShopID() ) + "\n" +
			"and ds.customer_id = " + SQLUtil.convertForSQL( cus.getCustomerID() ) + "\n" +
                        "and ds.sales_date < '%1$tY/%1$tm/%1$td'\n" +
			"order by\n" +
			"ds.sales_date desc,\n" +
			"ds.slip_no desc\n" +
			"limit 1\n" +
			";\n", currentDate);
	}
	
	/**
	 * HTML�p�ɕҏW������������擾
	 * @param s ������
	 * @return TML�p�ɕҏW����������
	 */
	public static String formatHTMLString(String s)
        {
            StringBuffer stringbuffer = new StringBuffer();
            if(s == null)
                return s;
            for(int i = 0; i < s.length(); i++)
                if("<".equals(s.substring(i, i + 1)))
                    stringbuffer.append("&lt;");
                else
                if(">".equals(s.substring(i, i + 1)))
                    stringbuffer.append("&gt;");
                else
                if("\n".equals(s.substring(i, i + 1)))
                    stringbuffer.append("<br>");
                else
                    stringbuffer.append(s.substring(i, i + 1));

            return stringbuffer.toString();
        }
	
	/**
	 * �ݓX�������s���B
	 */
	private void changeReservationToStay()
	{
            //�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
            if (reservation.getSelectedRowCount() <= 0) return;

            //�Љ�җ��X���\��
            this.introducerInfo(rs.getDatas().get(reservation.getSelectedRow()));

            //�ݓX�ς݃`�F�b�N
            //if (isVisited(this.getSelectedShop().getShopID(), rs.getDatas().get(reservation.getSelectedRow()).getCustomer(), "��t����")) return;

            if (rs.changeReservationToStay(reservation.getSelectedRows())) {

                this.showData();

            } else {

                MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(5110,
                                                "�\��f�[�^�̍ݓX"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
            }
            
            status.setSelectedIndex(1);
	}
        /*
         * �Љ�ҏ��̕\��
         */
	public void introducerInfo(DataReservation dr)
	{
            String introducermsg = "";
            String msg = "";

            MstCustomer cus = dr.getCustomer();

            introducermsg = MessageUtil.getMessage(5128,cus.getFullCustomerName().replace("�@", " "));

            try {
                ConnectionWrapper con = SystemInfo.getConnection();

                ArrayList list = null;

                if (SystemInfo.getSetteing().isShareCustomer()) {
                     list = cus.getIntroduceList(con, cus.getCustomerID(), 0);
                } else {
                    list = cus.getIntroduceList(con, cus.getCustomerID(), this.getSelectedShop().getShopID());
                }

                /*�Љ��*/
                for (int cnt1 = 0; cnt1 < list.size(); cnt1++) {
                    MstCustomer mcus = (MstCustomer)list.get(cnt1);

                    try {
                         StringBuilder sql = new StringBuilder(1000);
                         sql.append(" select");
                         sql.append("     a.reappearance_date");
                         sql.append(" from");
                         sql.append("     data_sales a");
                         sql.append(" where");
                         sql.append("         a.customer_id = " + SQLUtil.convertForSQL(mcus.getCustomerID()));
                         sql.append("     and get_visit_count(a.customer_id, a.shop_id) = 1");
                         sql.append("     and a.reappearance_date is not null");
                         sql.append("     and (select count(*)");
                         sql.append("            from data_sales b");
                         sql.append("           where b.customer_id = " + SQLUtil.convertForSQL(cus.getCustomerID()));
                         sql.append("             and b.sales_date >= a.reappearance_date) = 0");
                         sql.append("  order by a.reappearance_date");

                         //�R�l�N�V�������擾
                         ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

                         SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy,MM,dd");

                         while (rs.next()) {

                                String strdate = dateFormat.format(rs.getDate("reappearance_date"));
                                String[] strAry = strdate.split(",");
                                String wfrm =changeDateFmt(strAry);
                                msg = msg + MessageUtil.getMessage(5129
                                                        ,mcus.getCustomerNo()
                                                        ,mcus.getFullCustomerName().replace("�@", " ")
                                                        ,mcus.getFullCustomerKana().replace("�@", " ")
                                                        ,wfrm);
                                break;
                         }
                         rs.close();

                    } catch(Exception e) {
                         SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }
                }
            } catch (Exception e) {
            }

            if( msg != "" ){
                introducermsg = introducermsg + msg + "\n";
                String[] closemsg = {"�@����@"};
                int ret = JOptionPane.showOptionDialog(this,
                                introducermsg,
                                "�Љ�җ��X���",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                closemsg,
                                null);
            }
 
        }
        /*
         * �a��ϊ�
         */
        private String changeDateFmt(String[] strdate){
            int iyear = 0;
            int imonth = 0;
            int iday = 0;
            for (int cnt2=0; cnt2<strdate.length; cnt2++) {
                switch(cnt2)
                {
			case 0:
                                iyear = Integer.parseInt(strdate[cnt2]);
                                break;
			case 1:
                                imonth =  Integer.parseInt(strdate[cnt2]);
                                imonth--;
                                break;
			case 2:
                                iday = Integer.parseInt(strdate[cnt2]);
                                break;
			default:
                                break;
		}
            }

            Calendar gCal = new java.util.GregorianCalendar(iyear,imonth,iday);
            Locale locale = new Locale("ja", "JP", "JP");
            DateFormat df = new SimpleDateFormat("GGGGyy�NM��d��", locale);
            df.setTimeZone(gCal.getTimeZone());
            String wfrm = df.format(gCal.getTime());

            return wfrm;
        }
	
	private void cancelStay()
	{
		//�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
		if(stay.getSelectedRowCount() <= 0) {
                    //IVS_LVTu start add 2016/01/18 New request #46600
                    showMeg();
                    //IVS_LVTu end add 2016/01/18 New request #46600
				return;
                }
		
		for(Integer i : stay.getSelectedRows())
		{
			DataReservation dr = rs.getDatas().get(i);
			
			if(dr.getStatus() != 2 || dr.getSlipNo() !=0 )
			{
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(5113),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if(MessageDialog.showYesNoDialog(this,
				MessageUtil.getMessage(5003),
				this.getTitle(), JOptionPane.QUESTION_MESSAGE) != 0)
		{
			return;
		}
		
		if(rs.cancelStay(stay.getSelectedRows()))
		{
			this.showData();
		}
		else
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(5114),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * �{�p�J�n�������s���B
	 */
	private void changeStayToStart()
	{
		//�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
		if(stay.getSelectedRowCount() <= 0)
				return;
		
		for(Integer i : stay.getSelectedRows())
		{
			DataReservation dr = rs.getDatas().get(i);
			
			// �{�p�I���f�[�^�͎{�p�J�n��Ԃɖ߂��Ȃ�
			if( dr.getStatus() != 2 )
			{
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(5113),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// �{�p�J�n��ԂȂ�͂���
			if( dr.getSubStatus() != 1 )
			{
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(5121),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if(rs.changeStayToStart(stay.getSelectedRows()))
		{
			this.showData();
		}
		else
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(5117,
							"�ݓX�f�[�^�̊J�n"),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * �{�p�L�����Z���������s��
	 */
	private void cancelStart()
	{
		//�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
		if(stay.getSelectedRowCount() <= 0) {
                    //IVS_LVTu start add 2016/01/18 New request #46600
                    showMeg();
                    //IVS_LVTu end add 2016/01/18 New request #46600
                    return;
                }
		
		for(Integer i : stay.getSelectedRows())
		{
			DataReservation dr = rs.getDatas().get(i);
			
			if( (dr.getStatus() != 2)||(dr.getSubStatus() != 2 ) )
			{
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(5122),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if(MessageDialog.showYesNoDialog(this,
				MessageUtil.getMessage(5119),
				this.getTitle(), JOptionPane.QUESTION_MESSAGE) != 0)
		{
			return;
		}
		
		if(rs.cancelStart(stay.getSelectedRows()))
		{
			this.showData();
		}
		else
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(5120),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * �\��m�F�������s���B
	 */
	private void changeReservation()
	{
		//�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
		if(reservation.getSelectedRowCount() <= 0)
				return;
		
		//�قȂ�ڋq�̃f�[�^���I������Ă���ꍇ�A�����𔲂���
		if(!this.checkSelectedRows(reservation))
		{
			return;
		}
		
		if( this.getSelectedShop() == null ) return;
		RegistReservationDialog	rrp	=	new RegistReservationDialog( this.getSelectedShop(), date.getDate() );
		rrp.LoadReservation( rs.getDatas().get(reservation.getSelectedRow()) );
		SwingUtil.openAnchorDialog( this.parentFrame, true, rrp, "�\��m�F", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );
		
		boolean		isReserved	=	rrp.isReserved();
		
		rrp.releaceMemory();
		((JDialog)rrp.getParent().getParent().getParent().getParent()).dispose();
		rrp	=	null;
		System.gc();
		
		if(isReserved)
		{
			this.refresh();
		}
	}
	
	/**
	 * �\��폜�������s���B
	 */
	private void deleteReservation()
	{
		//�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
		if(reservation.getSelectedRowCount() <= 0)
				return;
                
                //IVS_NHTVINH start update 2016/10/12 New request #57738 [gb]���񂴂�API�p_�@�\�ǉ��i�t�����g��ʁj
		int index = reservation.getSelectedRow();
                if(index <= rs.getDatas().size()) {
                    
                    DataReservation dataReservation = rs.getDatas().get(index);
                    String cusName = dataReservation.getCustomer().getCustomerName()[0] 
                            + " " + dataReservation.getCustomer().getCustomerName()[1];
                    String selectvalues[] = {" �͂�(Y) ", " ������(N) "};
                    
                    int ret = JOptionPane.showOptionDialog(
                                            this,
                                            MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, cusName + " �l�̗\��"),
                                            this.getTitle(),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.WARNING_MESSAGE,
                                            null,
                                            selectvalues,
                                            selectvalues[0]);
                    if (ret == 1) {
                        return;
                    }
                
                    if(rs.deleteReservation(reservation.getSelectedRows())) {
                            this.showData();
                            
                            int shopId = dataReservation.getShop().getShopID();
                            int reservationNo = dataReservation.getReservationNo();
                            if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                                rr = new RegistReservation();
                                if(!rr.sendReservationAPI(SystemInfo.getLoginID(),shopId, reservationNo)) {
                                    MessageDialog.showMessageDialog(this,
                                        "�}�̂Ƃ̘A�����ł��܂���ł����B",
                                        this.getTitle(),
                                        JOptionPane.ERROR_MESSAGE);
                                }else {
                                    MessageDialog.showMessageDialog(this,
                                        "�\���o�^���܂����B",
                                        this.getTitle(),
                                        JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            
                        //IVS_NHTVINH end update 2016/10/12 New request #57738
                    }else {
                            MessageDialog.showMessageDialog(this,
                                            MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED,
                                                            "�\��f�[�^"),
                                            this.getTitle(),
                                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                            
	}
	
	/**
	 * ���Z�����ֈڍs����B
	 */
	private void finish()
	{
             //�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
            if (stay.getSelectedRowCount() <= 0) return;
		
            //�����ڋq�̃f�[�^���I������Ă��邩�`�F�b�N
            if(!this.checkSelectedRows(stay)) return;

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //IVS_LVTu start add 2015/10/09 Bug #43295
                HairInputAccountPanel.refShop = this.getSelectedShop();
                //IVS_LVTu end add 2015/10/09 Bug #43295
                HairInputAccountPanel riap = new HairInputAccountPanel();
                HairInputAccountPanel.setEditFlag(0);
                //IVS_LVTu start add 2015/10/07 New request #43038
                riap.setReturnButtonFlag(true);
                //IVS_LVTu end add 2015/10/07 New request #43038
                riap.setOpener(this);
                riap.setChangeKeys(false);
                // start edit 20130806 nakhoa
//                riap.loadReservation(SystemInfo.getCurrentShop().getShopID(),
//                                       this.getSelectedNo(stay),
//                                       rs.getDatas().get(stay.getSelectedRow()).getSubStatus());
                riap.loadReservation(((MstShop)shop.getSelectedItem()).getShopID(),
                                       this.getSelectedNo(stay),
                                       rs.getDatas().get(stay.getSelectedRow()).getSubStatus());
                // end edit 20130806 nakhoa
                riap.setReservationConsumptionCourseMap();
                riap.showData();
                SystemInfo.getLogger().log(Level.INFO, "�`�[����");
                this.setVisible(false);
                parentFrame.changeContents(riap);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}
	
	/**
	 * �`�[�f�[�^�ύX�����ֈڍs����B
	 */
	private void changeSales()
	{
             //�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
            if(sales.getSelectedRowCount() <= 0) {
                //IVS_LVTu start add 2016/01/18 New request #46600
                showMeg();
                //IVS_LVTu end add 2016/01/18 New request #46600
                return;
            }

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //IVS_LVTu start add 2015/10/09 Bug #43295
                HairInputAccountPanel.refShop = this.getSelectedShop();
                //IVS_LVTu end add 2015/10/09 Bug #43295
                HairInputAccountPanel riap = new HairInputAccountPanel();
                HairInputAccountPanel.setEditFlag(0);
                riap.setOpener(this);
                riap.setChangeKeys(false);
                riap.load(rs.getShop(), rs.getSales().get(sales.getSelectedRow()).getSlipNo());

                riap.showData();
                this.setVisible(false);
                parentFrame.changeContents(riap);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}
        
 	/**
	 * �ꎞ�ۑ��f�[�^�𐸎Z�����ֈڍs����B
	 */
       private void changeStay()
	{
           //�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
            if(stay.getSelectedRowCount() <= 0) return;

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //IVS_LVTu start add 2015/10/09 Bug #43295
                HairInputAccountPanel.refShop = this.getSelectedShop();
                //IVS_LVTu end add 2015/10/09 Bug #43295
                HairInputAccountPanel riap = new HairInputAccountPanel();
                HairInputAccountPanel.setEditFlag(0);
                riap.setOpener(this);
                riap.setChangeKeys(false);

                riap.loadTempData(rs.getShop(),
                                    rs.getDatas().get(stay.getSelectedRow()).getSlipNo(),
                                    this.getSelectedNo(stay),
                                    rs.getDatas().get(stay.getSelectedRow()).getSubStatus());

                riap.showData();
                riap.setSalesDate(date.getDate());

                SystemInfo.getLogger().log(Level.INFO, "�`�[����");
                this.setVisible(false);
                parentFrame.changeContents(riap);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}
	
	/**
	 * �����ڋq�̃f�[�^���I������Ă��邩�`�F�b�N����B
	 * @return �����ڋq�I������Ă���ꍇ�� true
	 * @param table �`�F�b�N����JTable
	 */
	private boolean checkSelectedRows(JTable table)
	{
		int[]	selIndex	=	table.getSelectedRows();
		Integer	cusID		=	rs.getDatas().get(table.getSelectedRow()).getCustomer().getCustomerID();
		
		for(int i : selIndex)
		{
			//�ޓX�f�[�^�̏ꍇ
			if(rs.getDatas().get(i).getStatus() == 3)
			{
                            if (MessageDialog.showYesNoDialog(this,
                                        "���ɂ���v�ς݂ł����A����v���e�̏C�����s���܂����H",
                                        this.getTitle(),
                                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                                try {

                                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                    
                                    //IVS_LVTu start add 2015/10/09 Bug #43295
                                    HairInputAccountPanel.refShop = this.getSelectedShop();
                                    //IVS_LVTu end add 2015/10/09 Bug #43295
                                    HairInputAccountPanel riap = new HairInputAccountPanel();
                                    riap.setOpener(this);
                                    // add by ltthuc 2014/06/17
                                    HairInputAccountPanel.setEditFlag(0);
                                    riap.setChangeKeys(false);
                                    //add by ltthuc 2014/06
                                    riap.load(rs.getShop(), rs.getDatas().get( table.getSelectedRow()).getSlipNo());
                                    riap.showData();
                                    this.setVisible(false);
                                    parentFrame.changeContents(riap);

                                } finally {
                                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                }
                                
                            }
                            
                            return false;
                            
 			}
			//�ڋqID���Ⴄ�ꍇ
			if(!cusID.equals(rs.getDatas().get(i).getCustomer().getCustomerID()))
			{
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(5111),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		return	true;
	}
	
	/**
	 * �w��ݓX���R�[�h�̍ݓX�󋵂��擾����
	 */
	private int getStayStatus()
	{
		//�f�[�^���I������Ă��Ȃ��ꍇ�A�����𔲂���
		if(stay.getSelectedRowCount() <= 0) return -1;
		
		for(Integer i : stay.getSelectedRows())
		{
			DataReservation dr = rs.getDatas().get(i);
			
			// �ݓX��Ԃ̃f�[�^�ӊO�͖���
			if( dr.getStatus() != 2 )
			{
				return -1;
			}
			// �ݓX��Ԃ�Ԃ�
			return dr.getSubStatus();
		}
		return -1;
	}
	
	/**
	 * �\��e�[�u���p��TableCellRenderer
	 */
	public class ReservationTableCellRenderer extends SelectTableCellRenderer
	{
		/**
		 * ������Ԃ�����^�C�~���O�i�~���b�j
		 */
		private static final int	ALERT_TIME	=	5 * 60 * 1000;
		
		/** Creates a new instance of ReservationTableCellRenderer */
		public ReservationTableCellRenderer()
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
			
			//�����̐����z�u��ύX
			switch(status.getSelectedIndex())
			{
				//�\��
				case 0:
					switch(column)
					{
						case 0:
						case 1:
						case 7:
							super.setHorizontalAlignment(SwingConstants.CENTER);
							break;
						case 4:
							super.setHorizontalAlignment(SwingConstants.RIGHT);
							break;
						default:
							super.setHorizontalAlignment(SwingConstants.LEFT);
							break;
					}
					break;
				//�ݓX
				case 1:
					switch(column)
					{
						case 3:
							super.setHorizontalAlignment(SwingConstants.RIGHT);
							break;
						case 0:
						case 6:
						case 7:
						case 8:
						case 9:
						case 10:
						case 11:
							super.setHorizontalAlignment(SwingConstants.CENTER);
							break;
						default:
							super.setHorizontalAlignment(SwingConstants.LEFT);
							break;
					}
					
					//�����̐F��ύX
					super.setForeground(this.getForegroundColor(row));
					break;
				//����ꗗ
                                //IVS_LVTu start edit 2015/10/08 New request #43283
				case 2:
					switch(column)
					{
						case 0:
                                                        super.setHorizontalAlignment(SwingConstants.LEFT);
							break;
                                                case 3:
						case 6:
						case 7:
						case 8:
						case 9:
						case 10:
						case 11:
							super.setHorizontalAlignment(SwingConstants.RIGHT);
							break;
						case 5:
							super.setHorizontalAlignment(SwingConstants.CENTER);
							break;
                                                case 12:
							super.setHorizontalAlignment(SwingConstants.RIGHT);
							break;
						default:
							super.setHorizontalAlignment(SwingConstants.LEFT);
							break;
                                            //IVS_LVTu end edit 2015/10/08 New request #43283        
					}
					break;
			}

			return this;
		}
		
		/**
		 * ������Ԃ����邩�ǂ������擾����B
		 * @param row �s
		 * @return true - �Ԃ�����
		 */
		private Color getForegroundColor(int row)
		{
			if(0 <= row && row < rs.getDatas().size())
			{
				DataReservation	dr = rs.getDatas().get(row);
				//�ޓX���Ă���ꍇ
				if(dr.getStatus() == 3)
				{
					return	new Color(20, 80, 200);
				}
				//�I���������擾
				GregorianCalendar finishTime = new GregorianCalendar();

				//�ڋq���
				if (dr.getStatus() == 2 && dr.getSubStatus() == 2)
				{
				    // �ݓX��ԂŎ{�p���̏ꍇ
				    finishTime.setTime(dr.getStartTime().getTime());
				}

//				finishTime.setTime(dr.get(dr.size() - 1).getReservationDatetime().getTime());

                                for (int i = 0; i < dr.size(); i++) {
                                    if (dr.get(i).getCourseFlg() == null) {
                                        finishTime.add(finishTime.MINUTE, dr.get(i).getTechnic().getOperationTime());
                                    } else if ((Integer)dr.get(i).getCourseFlg() == 1) {
                                        finishTime.add(finishTime.MINUTE, dr.get(i).getCourse().getOperationTime());
                                    } else if ((Integer)dr.get(i).getCourseFlg() == 2) {
                                        finishTime.add(finishTime.MINUTE, dr.get(i).getConsumptionCourse().getOperationTime());
                                    }
                                }

				//���ݎ������擾
				GregorianCalendar currentTime = new GregorianCalendar();
				//���ݎ����ƏI�������̍����擾
				long dif = finishTime.getTimeInMillis()
								- currentTime.getTimeInMillis();
				//���ݎ����ƏI�������̍����w�莞�Ԉȉ��̏ꍇ
				if(dif <= ALERT_TIME)
				{
					return	new Color(255, 60, 60);
				}
				//�{�p���̏ꍇ
				if(dr.getSubStatus() == 2)
				{
					return	new Color( 20, 128, 20 );
				}
			}
			
			return	Color.black;
		}
	}
	
	/**
	 * JTable�̗񕝂�����������B
	 */
	private void initTableColumnWidth()
	{
		//��̕���ݒ肷��B
                //�\��^�u
                reservation.getColumnModel().getColumn(0).setPreferredWidth(60);        //�ڋq���
                reservation.getColumnModel().getColumn(1).setPreferredWidth(60);        //�\�񎞊�
		reservation.getColumnModel().getColumn(2).setPreferredWidth(80);        //�ڋqNo.
		reservation.getColumnModel().getColumn(3).setPreferredWidth(136);       //�ڋq
		reservation.getColumnModel().getColumn(4).setPreferredWidth(60);        //���X��
		reservation.getColumnModel().getColumn(5).setPreferredWidth(100);        //���j���[
		reservation.getColumnModel().getColumn(6).setPreferredWidth(90);        //��S����
		reservation.getColumnModel().getColumn(7).setPreferredWidth(30);        //�w��
		reservation.getColumnModel().getColumn(8).setPreferredWidth(90);        //�\��ԍ�
		
                //�ݓX�E�ޓX�^�u
                stay.getColumnModel().getColumn(0).setPreferredWidth(75);		// �ڋq���
                stay.getColumnModel().getColumn(1).setPreferredWidth(80);		// �ڋqNo
		stay.getColumnModel().getColumn(2).setPreferredWidth(116);		// �ڋq
		stay.getColumnModel().getColumn(3).setPreferredWidth(60);		// ���X��
		stay.getColumnModel().getColumn(4).setPreferredWidth(100);		// ���j���[
		stay.getColumnModel().getColumn(5).setPreferredWidth(100);		// ��S����
                stay.getColumnModel().getColumn(6).setPreferredWidth(30);		// �w��		
		stay.getColumnModel().getColumn(7).setPreferredWidth(60);		// �\�񎞊�
		stay.getColumnModel().getColumn(8).setPreferredWidth(60);		// �ݓX����
		stay.getColumnModel().getColumn(9).setPreferredWidth(60);		// �J�n����
		stay.getColumnModel().getColumn(10).setPreferredWidth(60);		// �{�p����
		stay.getColumnModel().getColumn(11).setPreferredWidth(60);		// �I������

                //����ꗗ�^�u
		sales.getColumnModel().getColumn(0).setPreferredWidth(60);
		sales.getColumnModel().getColumn(1).setPreferredWidth(70);		
		sales.getColumnModel().getColumn(2).setPreferredWidth(100);		
		sales.getColumnModel().getColumn(3).setPreferredWidth(60);		
		sales.getColumnModel().getColumn(4).setPreferredWidth(80);		
		sales.getColumnModel().getColumn(5).setPreferredWidth(30);		
		sales.getColumnModel().getColumn(6).setPreferredWidth(80);
		sales.getColumnModel().getColumn(7).setPreferredWidth(80);
		sales.getColumnModel().getColumn(8).setPreferredWidth(80);
		sales.getColumnModel().getColumn(9).setPreferredWidth(80);
		sales.getColumnModel().getColumn(10).setPreferredWidth(80);
		sales.getColumnModel().getColumn(11).setPreferredWidth(80);		
	}
        
        /**
	 * �ڋq���{�^�����擾����
	 */
	private JButton getCustomerInfoButton(final Integer customerID, JapaneseCalendar birthday)
	{
		JButton		customerButton	=	new JButton();
		customerButton.setBorderPainted(false);
		customerButton.setContentAreaFilled(false);
                if (checkCustomerBirthday(birthday)) {
                    //�ڋq�̒a�������w�肵�����t�̑O��R�O���ȓ��̏ꍇ
                    customerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/reservation/birthday_off.jpg")));
                    customerButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/reservation/birthday_on.jpg")));
                } else {
                    //��L�ȊO�̏ꍇ
                    customerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/reservation/customer_off.jpg")));
                    customerButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/reservation/customer_on.jpg")));
                }
		customerButton.setSize(48, 25);
		customerButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
			    MstCustomerPanel mcp = null;
                            
                            try {
                                
                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                mcp = new MstCustomerPanel(customerID , false, false, true);                                
                                SwingUtil.openAnchorDialog( null, true, mcp, "�ڋq���", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }

			    //�ڋq��񂪍X�V���ꂽ�ꍇ�A��ʂ����t���b�V��
                            boolean isRenewed = mcp.isRenewed();

			    ((JDialog)mcp.getParent().getParent().getParent().getParent()).dispose();
			    
                            if(isRenewed) {
                                refresh();
                            }
			    
			}
		});
		return customerButton;
	}        	
        
        /**
	 * �{�p�J�n�{�^�����擾����
	 */
	private JButton getStartButton()
	{
		JButton		startButton	=	new JButton();
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
                startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                "/images/" + SystemInfo.getSkinPackage() + "/button/reservation/start_off.jpg")));
                startButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                                "/images/" + SystemInfo.getSkinPackage() + "/button/reservation/start_on.jpg")));
		startButton.setSize(48, 25);
		startButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            switch( getStayStatus() )
                            {
                                    // �{�p�҂���Ԃ̏ꍇ�ɂ͎{�p���ɑJ�ڂ���
                                    case 1:
                                            changeStayToStart();
                                            break;
                            }
			}
		});
		return startButton;
	}        	
        
        /**
	 * �ڋq�̒a�������w�肵�����t�̑O��R�O���ȓ����ǂ����̃`�F�b�N
	 */
	private boolean checkCustomerBirthday(JapaneseCalendar birthday)
	{
            
            if (birthday == null) return false;
            
            boolean isBirthday = false;

            Calendar calStart = Calendar.getInstance();
            calStart.setTime(date.getDate());
            
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(date.getDate());
            
            MstShop ms = ((MstShop)shop.getSelectedItem());
            
            if (ms.isBirthMonthFlag()) {
                // �a����
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                calEnd.set(Calendar.DAY_OF_MONTH, 1);
                calEnd.add(Calendar.MONTH, 1);
                calEnd.add(Calendar.DAY_OF_MONTH, -1);
            } else {
                // �a���� �O��̓����w��
                calStart.add(Calendar.DAY_OF_MONTH, -1 * ms.getBirthBeforeDays());
                calEnd.add(Calendar.DAY_OF_MONTH, ms.getBirthAfterDays());
            }

            Calendar calBirth = Calendar.getInstance();
            calBirth.setTime(birthday.getTime());
            calBirth.set(Calendar.YEAR, calStart.get(Calendar.YEAR));

            if (calBirth.before(calStart)) {
                calBirth.add(Calendar.YEAR, 1);
            }

            isBirthday = isBirthday || calStart.equals(calBirth);
            isBirthday = isBirthday || calEnd.equals(calBirth);
            isBirthday = isBirthday || (calStart.before(calBirth) && calEnd.after(calBirth));
            
            return isBirthday;
	}        	
	
	private void addSortCol(MouseEvent e)
	{
/*
		JTable	table	=	null;
		
		switch(status.getSelectedIndex())
		{
			case 0:
				table	=	reservation;
				break;
			case 1:
				table	=	stay;
				break;
			case 2:
				table	=	sales;
				break;
			default:
				return;
		}
		
		Integer		col	=	table.getTableHeader().columnAtPoint(e.getPoint());
		
		if(0 <= col)
		{
			switch(status.getSelectedIndex())
			{
				case 0:
				case 1:
					rs.setReservationSortCol(col);
					break;
				case 2:
					rs.setSalesSortCol(col);
					break;
			}
			
			this.showData();
		}
*/
	}
	
	private void printCounselingSheet()
	{
		JTable table = null;
		
		switch(status.getSelectedIndex())
		{
                    case 0:
                            table = reservation;
                            break;
                    case 1:
                            table = stay;
                            break;
                    default:
                            return;
		}

                try {
                    
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    status.requestFocusInWindow();
                    
                    CounselingSheet cs = new CounselingSheet(this.getSelectedShop());

                    cs.setSalesDate(date.getDate());

                    for (Integer i : table.getSelectedRows()) {
                        cs.addData(rs.getDatas().get(i));
                    }

                    if (cs.size() == 0) {
                        //IVS_LVTu start add 2016/01/18 New request #46600
                        showMeg();
                        //IVS_LVTu end add 2016/01/18 New request #46600
                        return;
                    }

                    if (cs.load()) {
                        
                        PrintReceipt pr = new PrintReceipt();
                        if (pr.getReceiptSetting().isPrintCounselingSheet()) {
                            
                            // ���V�[�g�T�C�Y��80�~���̏ꍇ�͑I�����b�Z�[�W��\������
                            String selectvalues[] = {" �ڋq�V�[�g ", " ���q�l�V�[�g ", " �L�����Z�� "};
                            int ret = JOptionPane.showOptionDialog(
                                            this,
                                            "�ڋq�V�[�g�A���q�l�V�[�g�̂ǂ�����󎚂��܂����H",
                                            this.getTitle(),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            selectvalues,
                                            selectvalues[2]);
                            
                            if (ret == 0) {
                                
                                // �ڋq�V�[�g
                                cs.printServiceSheet();
                                
                            } else if (ret == 1) {
                                
                                // ���q�l�V�[�g
                                cs.printCustomerSheet();
                            }
                            
                        } else {

                            // ���V�[�g�T�C�Y��58�~���̏ꍇ��A4�T�C�Y�̃J�E���Z�����O�V�[�g���󎚂���
                            cs.printCounselingSheet();
                        }
                    }
                    
                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
	}
	
        private void dateChange(){
            	if(date.getDate() != null &&
				String.format("%1$tY/%1$tm/%1$td", date.getDate()) != String.format("%1$tY/%1$tm/%1$td", currentDate))
		{
			if(this.checkInput())
			{       
				rs.setShop(this.getSelectedShop());
				currentDate.setTime(date.getDate());
				this.changeButtonEnabled();
				this.showData();
			}
		}
        }
	
	public void setDate(java.util.Date d){
            date.setDate(d);
            dateChange();
        }
        
        
        public static boolean isVisited(Integer shopId, MstCustomer mc, Date date, String procName) {
            
            boolean result = false;
            
            // ���ꊳ�҂�������ɕ����񗈉@�����ꍇ
            int status = 0;
            try {
                Calendar cal = SystemInfo.getCurrentShop().getSystemDate(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("     a.status");
                sql.append(" from");
                sql.append("     data_reservation a");
                sql.append("         join data_reservation_detail b");
                sql.append("         using(shop_id, reservation_no)");
                sql.append(" where");
                sql.append("         a.shop_id = " + SQLUtil.convertForSQL(shopId));
                sql.append("     and a.customer_id = " + SQLUtil.convertForSQL(mc.getCustomerID()));
                sql.append("     and date_trunc('day', b.reservation_datetime) = " + SQLUtil.convertForSQL(cal));
                sql.append("     and a.delete_date is null");
                sql.append("     and b.delete_date is null");
                sql.append(" order by");
                sql.append("     a.status desc");
                sql.append(" limit 1");
                
                //�R�l�N�V�������擾
                ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                if (rs.next()) {
                    status = rs.getInt("status");
                }
                rs.close();

            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            if (status > 0) {
                if(MessageDialog.showYesNoDialog(null,
                        "���� �u" + mc.getFullCustomerName() + "�v�l ��" + (status > 1 ? "�ݓX" : "�\��") + "����Ă��܂����A" + procName + "���s���܂����H",
                        procName,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
                        result = true;
                }
            }

            return result;
        }
        
        private void printReservationList() {

            JExcelApi jx = new JExcelApi("�\��ꗗ");
            jx.setTemplateFile("/reports/�\��ꗗ.xls");

            // �w�b�_
            SimpleDateFormat format = new SimpleDateFormat("yyyy�NMM��dd��");
            jx.setValue(1, 2, format.format(date.getDate()));
            jx.setValue(2, 4, ((MstShop)shop.getSelectedItem()).getShopName());

            int row = 7;

            // �ǉ��s���Z�b�g
            jx.insertRow(row, reservation.getRowCount() - 1);

            // �f�[�^�Z�b�g
            for (int i = 0; i < reservation.getRowCount(); i++) {
                jx.setValue(1, row, reservation.getValueAt(i, 1).toString());
                jx.setValue(2, row, reservation.getValueAt(i, 2).toString());
                jx.setValue(3, row, reservation.getValueAt(i, 3).toString());
                jx.setValue(4, row, (Long)reservation.getValueAt(i, 4));
                jx.setValue(5, row, reservation.getValueAt(i, 5).toString());
                jx.setValue(6, row, reservation.getValueAt(i, 6).toString());
                jx.setValue(7, row, reservation.getValueAt(i, 7).toString());
                //IVS_LVTu start edit 2015/10/08 New request #43283
                jx.setValue(8, row, reservation.getValueAt(i, 8).toString());
                //IVS_LVTu end edit 2015/10/08 New request #43283

                row++;
            }

            jx.openWorkbook();

        }

        public static String insertNewLineString(String s) {
            StringBuilder result = new StringBuilder();
            while (s.length() > 20) {
                result.append(s.substring(0, 20));
                result.append("\n");
                s = s.substring(20);
            }
            result.append(s);
            return result.toString();
        }
        //IVS_LVTu start add 2015/09/07 New request #42428
        public Date getNextReservationDate(Integer shop_id, Integer customer_id) {
            Date nextReservationDate = null;
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select drd.reservation_datetime ");
            sql.append(" from data_reservation dr ");
            sql.append(" inner join data_reservation_detail drd using(shop_id,reservation_no) ");
            sql.append(" where  ");
            sql.append(" dr.customer_id = " + SQLUtil.convertForSQL(customer_id));
            sql.append(" and dr.delete_date is null ");
            sql.append(" and drd.delete_date is null ");
            sql.append(" and dr.status = 1 ");
            sql.append(" and dr.shop_id = " + SQLUtil.convertForSQL(shop_id));
            sql.append(" and drd.reservation_detail_no =  ");
            sql.append(" (select min(reservation_detail_no) from data_reservation_detail  ");
            sql.append(" where dr.shop_id = shop_id and reservation_no = dr.reservation_no and delete_date is null ");
            sql.append(" and reservation_datetime > date_trunc('day', CURRENT_TIMESTAMP + interval '1day') ");
            sql.append(" ) ");
            sql.append(" order by drd.reservation_datetime asc ");
            sql.append(" limit 1 ");
            
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            if (con == null) {
                return null;
            }   
            try
            {
                ResultSetWrapper rs = con.executeQuery(sql.toString());
                if (rs.next()) {
                    nextReservationDate = rs.getDate("reservation_datetime");
                }

            }
            catch(SQLException e)
            {
                 SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            return nextReservationDate;
        }
        //IVS_LVTu end add 2015/09/07 New request #42428
        //IVS_LVTu start add 2016/01/18 New request #46600
        private void showMeg() {
            MessageDialog.showMessageDialog(this,
                                    "�ڋq���I������Ă��܂���B",
                                    this.getTitle(),
                                    JOptionPane.ERROR_MESSAGE);
        }
        //IVS_LVTu end add 2016/01/18 New request #46600
}

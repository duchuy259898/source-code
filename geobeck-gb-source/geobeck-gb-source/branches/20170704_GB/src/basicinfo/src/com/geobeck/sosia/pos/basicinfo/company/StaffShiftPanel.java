/*
 * StaffShiftPanel.java
 *
 * Created on 2009/04/28, 15:24
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.master.company.MstShift;
import com.geobeck.sosia.pos.master.company.MstShifts;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.CheckUtil;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import org.apache.commons.collections.map.LinkedMap;

/**
 *
 * @author  trino
 */
public class StaffShiftPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    private static final String[] fn_strWDay = {
        "日"
    ,   "月"
    ,   "火"
    ,   "水"
    ,   "木"
    ,   "金"
    ,   "土"
    };
    
    private JLabel[] lblBasicShift = new JLabel[26];
    private MstShifts basicshiftsInShop = new MstShifts();
    private ArrayList<String> arrTgtYM;
    //IVS_LVTu start add New request #48969
    private boolean flagCmb = false;
    private Integer indexItemYM = -1;
    private int prevIndex = -1;
    //IVS_LVTu end add New request #48969
    private Integer indexItemShop = -1;
    //IVS_PTQUANG start add 2016/09/05 New request #54490
    protected MstAPI mstApi = new MstAPI();
    //IVS_PTQUANG start add 2016/09/05 New request #54490
    boolean flag = true;
    /**
     * Creates new form DailySalesReportPanelTom
     */
    public StaffShiftPanel()
    {
        initComponents();
        initRequisites();

        this.setTitle("スタッフシフト登録");
        this.setPath("会社関連");
        this.setSize(830,680);

        SystemInfo.getLogger().log(Level.INFO, this.getTitle());
        
        addMouseCursorChange();

        ftp = new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());

        SystemInfo.initGroupShopComponents(cblShop, 2);
        initCboTgtYM();

        this.showData();
        //IVS_LVTu start add New request #48969
        flagCmb = true;
        btnShow.setVisible(false);
        //IVS_LVTu end add New request #48969
    }
    
    private void initRequisites()
    {
        this.lblBasicShift[0] = this.lblBasicShiftA;
        this.lblBasicShift[1] = this.lblBasicShiftB;
        this.lblBasicShift[2] = this.lblBasicShiftC;
        this.lblBasicShift[3] = this.lblBasicShiftD;
        this.lblBasicShift[4] = this.lblBasicShiftE;
        this.lblBasicShift[5] = this.lblBasicShiftF;
        this.lblBasicShift[6] = this.lblBasicShiftG;
        this.lblBasicShift[7] = this.lblBasicShiftH;
        this.lblBasicShift[8] = this.lblBasicShiftI;
        this.lblBasicShift[9] = this.lblBasicShiftJ;
        this.lblBasicShift[10] = this.lblBasicShiftK;
        this.lblBasicShift[11] = this.lblBasicShiftL;
        this.lblBasicShift[12] = this.lblBasicShiftM;
        this.lblBasicShift[13] = this.lblBasicShiftN;
        this.lblBasicShift[14] = this.lblBasicShiftO;
        this.lblBasicShift[15] = this.lblBasicShiftP;
        this.lblBasicShift[16] = this.lblBasicShiftQ;
        this.lblBasicShift[17] = this.lblBasicShiftR;
        this.lblBasicShift[18] = this.lblBasicShiftS;
        this.lblBasicShift[19] = this.lblBasicShiftT;
        this.lblBasicShift[20] = this.lblBasicShiftU;
        this.lblBasicShift[21] = this.lblBasicShiftV;
        this.lblBasicShift[22] = this.lblBasicShiftW;
        this.lblBasicShift[23] = this.lblBasicShiftX;
        this.lblBasicShift[24] = this.lblBasicShiftY;
        this.lblBasicShift[25] = this.lblBasicShiftZ;
    }
    
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnShow);
        SystemInfo.addMouseCursorChange(btnPrint);
        SystemInfo.addMouseCursorChange(btnRegist);
        SystemInfo.addMouseCursorChange(btnApply);
    }

    private void initCboTgtYM()
    {
        this.cboTgtYM.removeAllItems();

        try
        {
            ConnectionWrapper con = SystemInfo.getConnection();
            
            Integer intShopID = ((MstShop)this.cblShop.getSelectedItem()).getShopID();
            this.arrTgtYM = DataSchedules.getAllSchedulesAndComingYearInYM(con, intShopID);
            
            con.close();
        }
        catch (SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return;
        }

        for (int i = 0; i < this.arrTgtYM.size(); ++i)
        {
            this.cboTgtYM.addItem(
                this.arrTgtYM.get(i).substring(0, 4) + "年"
                + Integer.parseInt(this.arrTgtYM.get(i).substring(4, 6)) + "月"
            );
        }

        // 今月を選択状態に
        this.cboTgtYM.setSelectedIndex(this.arrTgtYM.size() - 12);
    }

    private void showData()
    {
        if (this.cboTgtYM.getSelectedIndex() < 0) return;

        MstShop shop;
        shop = (MstShop)this.cblShop.getSelectedItem();
        
        // 基本シフト時間ラベルを表示
        showBasicShifts(shop.getShopID());
        
        // スタッフシフト表を表示
        showStaffShift( shop, this.arrTgtYM.get(this.cboTgtYM.getSelectedIndex()) );
    }
    
    private void showStaffShift(MstShop shop, String strTgtYM)
    {
        
        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Calendar calen = Calendar.getInstance();

            // 対象年月の初日をセット
            calen.set(
                Integer.parseInt(strTgtYM.substring(0, 4))
            ,   Integer.parseInt(strTgtYM.substring(4, 6)) - 1
            ,   1
            );

            // 全コンポーネント削除
            SwingUtil.clearTable(this.tblDayHeader);
            SwingUtil.clearTable(this.tblStaffShift);

            // 対象年月の日数
            final int fn_nDaysInMonth = calen.getActualMaximum(Calendar.DAY_OF_MONTH);

            JLabel[] lblDayOfMonthName = new JLabel[fn_nDaysInMonth];
            JLabel[] lblDayOfWeekName = new JLabel[fn_nDaysInMonth];
            JButton[] btnRecess = new JButton[fn_nDaysInMonth];
            JButton[] btnRegis = new JButton[fn_nDaysInMonth];
            JLabel lbl;

            // ヘッダー行の各列内容を定義
            for (int day = 0; day < fn_nDaysInMonth; ++day)
            {
                // 日付
                lbl = new JLabel();

                lbl.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
                lbl.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                lbl.setOpaque(true);
                lbl.setText( String.valueOf(day + 1) );
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                lblDayOfMonthName[day] = lbl;

                // 曜日
                calen.set(Calendar.DAY_OF_MONTH, day + 1);
                lbl = new JLabel();

                lbl.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
                lbl.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                lbl.setOpaque(true);
                lbl.setText( StaffShiftPanel.fn_strWDay[calen.get(Calendar.DAY_OF_WEEK) - 1] );
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                lblDayOfWeekName[day] = lbl;

                // 休憩ボタン
                btnRecess[day] = new JButton("休憩");
                btnRecess[day].setMargin(new Insets(0, 0, 0, 0));
                btnRecess[day].addActionListener(GetBtnRecessAL(day + 1));
                
                //Add Button Quang
                btnRegis[day] = new JButton("ﾍﾙﾌﾟ");
                btnRegis[day].setMargin(new Insets(0, 0, 0, 0));
                btnRegis[day].addActionListener(GetBtnRegisAL(day + 1));
            }

            this.tblDayHeader.setModel(GetDTMDayHeader(fn_nDaysInMonth));
            this.tblStaffName.setModel(GetDTMStaffName());
            this.tblStaffShift.setModel(GetDTMStaffShift(fn_nDaysInMonth));

            DefaultTableModel dtmDayHeader = (DefaultTableModel)this.tblDayHeader.getModel();
            DefaultTableModel dtmStaffName = (DefaultTableModel)this.tblStaffName.getModel();
            DefaultTableModel dtmStaffShift = (DefaultTableModel)this.tblStaffShift.getModel();

            dtmDayHeader.setColumnCount(fn_nDaysInMonth);
            dtmStaffName.setColumnCount(1);
            dtmStaffShift.setColumnCount(fn_nDaysInMonth);

            // 日付行追加
            dtmDayHeader.addRow(lblDayOfMonthName);

            // 曜日行追加
            dtmDayHeader.addRow(lblDayOfWeekName);

            // 休憩ボタン行追加
            dtmDayHeader.addRow(btnRecess);
            
            dtmDayHeader.addRow(btnRegis);
            
            MstStaffs staffsInShop = new MstStaffs();
            DataSchedules schedulesInDate = new DataSchedules();

            staffsInShop.setShopIDList(String.valueOf(shop.getShopID()));
            schedulesInDate.setShop(shop);

            try
            {
                ConnectionWrapper con = SystemInfo.getConnection();

                // 自店スタッフ達を取得
                staffsInShop.loadOnlyShop(con, false);

                Map mapShift = schedulesInDate.loadAll(con, calen);
                Map mapRecess = schedulesInDate.loadRecess(con, calen);
                
                Map mapShift1 = schedulesInDate.loadAllDataScheduleDetail(con, calen);
                Map mapRecess1 = schedulesInDate.loadDataScheduleDetail(con, calen);
                
                this.cboStaff.removeAllItems();
                this.cboShift.removeAllItems();
                    
                for (int iStaff = 0; iStaff < staffsInShop.size(); ++iStaff)
                {
                    // スタッフ名テーブル
                    MstStaff[] objStaffNameRowData = new MstStaff[1];

                    objStaffNameRowData[0] = staffsInShop.get(iStaff);
                    dtmStaffName.addRow(objStaffNameRowData);

                    // スタッフシフトテーブル
                    JComboBox[] cboStaffShiftRowData = new JComboBox[fn_nDaysInMonth];
                  
                    for (int day = 0; day < fn_nDaysInMonth; ++day)
                    {
                        JComboBox cbo;
                        Boolean isRecess = null;
                        Boolean isRecess1 = null;
                        cbo = new JComboBox(this.basicshiftsInShop.toArray());
                        cbo.setRenderer(new MyListCellRenderer(cbo.getRenderer()));

                        cbo.setSelectedIndex(0);

                        ArrayList key = new ArrayList();
                        key.add(staffsInShop.get(iStaff).getStaffID());
                        key.add(day + 1);
                        
                        Integer shiftId = (Integer)mapShift.get(key);
                        Integer shiftId1 = (Integer)mapShift1.get(key);
                        
                        if (shiftId != null)
                        {
                            for (int i = 0; i < cbo.getItemCount(); ++i)
                            {
                                if ( ((MstShift)cbo.getItemAt(i)).getShiftId().equals(shiftId))
                                {
                                    cbo.setSelectedIndex(i);
                                    
                                    isRecess = (Boolean)mapRecess.get(key);                                    
                                    if (isRecess != null && isRecess.booleanValue()) {
                                        cbo.setBackground(Color.GREEN);
                                    }
                                    break;
                                }
                            }
                        }
                        if(shiftId1 != null)
                        {
                            for (int i = 0; i < cbo.getItemCount(); ++i)
                            {
                                if ( ((MstShift)cbo.getItemAt(i)).getShiftId().equals(shiftId1))
                                {
                                    cbo.setSelectedIndex(i);
                                    
                                    isRecess1 = (Boolean)mapRecess1.get(key);                                    
                                    if (isRecess1 != null && isRecess1.booleanValue()) {
                                        cbo.setBackground(new java.awt.Color(146, 204, 243));
                                    }                                
                                    if ((isRecess1 != null && isRecess1.booleanValue()) && (isRecess != null && isRecess.booleanValue())) {
                                        cbo.setBackground(new java.awt.Color(160, 70, 160));
                                    }
                                    break;
                                }
                            }
                        }

                        // レンダラー用コンポーネント追加
                        cbo.setBounds(45 * day, SystemInfo.TABLE_ROW_HEIGHT * iStaff, 45 - 1, SystemInfo.TABLE_ROW_HEIGHT - 1);
                        this.tblStaffShift.add(cbo);

                        cboStaffShiftRowData[day] = cbo;
                    }

                    dtmStaffShift.addRow(cboStaffShiftRowData);
                    
                    this.cboStaff.addItem(staffsInShop.get(iStaff));
                }

                for (int i = 0 ; i < basicshiftsInShop.size(); i++) {
                    this.cboShift.addItem(basicshiftsInShop.get(i));
                }
                
                con.close();
            }
            catch (SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return;
            }

            this.tblStaffName.getColumnModel().getColumn(0).setCellRenderer(SystemInfo.getTableHeaderRenderer());
            this.tblStaffName.getColumnModel().getColumn(0).setPreferredWidth(100);

            for (int day = 0; day < fn_nDaysInMonth; ++day)
            {
                this.tblDayHeader.getColumnModel().getColumn(day).setCellRenderer(SystemInfo.getTableHeaderRenderer());
                this.tblDayHeader.getColumnModel().getColumn(day).setPreferredWidth(45);

                this.tblStaffShift.getColumnModel().getColumn(day).setPreferredWidth(45);
            }
            
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private static class MyListCellRenderer extends DefaultListCellRenderer {
        private final ListCellRenderer lcr;
        public MyListCellRenderer(ListCellRenderer lcr) {
            this.lcr = lcr;
        }
        public Component getListCellRendererComponent(JList list,Object value, int index,boolean isSelected, boolean cellHasFocus) {
           JLabel cmp = (JLabel)lcr.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
           cmp.setOpaque(true);
           return cmp;
       }
   }
    // ptquang
     private ActionListener GetBtnRegisAL(final Integer day)
    {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                String strTgtYM;
                Calendar calen = Calendar.getInstance();
                
                strTgtYM = arrTgtYM.get(cboTgtYM.getSelectedIndex());
                
                calen.set(
                    Integer.parseInt(strTgtYM.substring(0, 4))
                ,   Integer.parseInt(strTgtYM.substring(4, 6)) - 1
                ,   day
                );

                RegistShopEmployeePanel.ShowDialog(parentFrame, (MstShop)cblShop.getSelectedItem(), calen.getTime() );
                
                showData();
            }
        };
    }
    
    private ActionListener GetBtnRecessAL(final Integer day)
    {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                String strTgtYM;
                Calendar calen = Calendar.getInstance();
                
                strTgtYM = arrTgtYM.get(cboTgtYM.getSelectedIndex());
                
                calen.set(
                    Integer.parseInt(strTgtYM.substring(0, 4))
                ,   Integer.parseInt(strTgtYM.substring(4, 6)) - 1
                ,   day
                );

                RegistRecessTotalPanel.ShowDialog(parentFrame, (MstShop)cblShop.getSelectedItem(), calen.getTime());
                
                showData();
            }
        };
    }

    private DefaultTableModel GetDTMDayHeader(final Integer intColumns)
    {
        return new DefaultTableModel(
            new Object [][]
            {

            },
            new String[intColumns]
        ) {
            public Class getColumnClass(int columnIndex)
            {
                return Object.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                if (rowIndex <= 1)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };
    }
    
    private DefaultTableModel GetDTMStaffName()
    {
        return new DefaultTableModel(
            new Object [][]
            {

            },
            new String[1]
        ) {
            public Class getColumnClass(int columnIndex)
            {
                return Object.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        };
    }
    
    private DefaultTableModel GetDTMStaffShift(final Integer intColumns)
    {
        return new DefaultTableModel(
            new Object [][]
            {

            },
            new String[intColumns]
        ) {
            public Class getColumnClass(int columnIndex)
            {
                return Object.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return true;
            }
        };
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnReportType = new javax.swing.ButtonGroup();
        jLabel8 = new javax.swing.JLabel();
        cblShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnRegist = new javax.swing.JButton();
        scrDayHeader = new javax.swing.JScrollPane();
        tblDayHeader = new com.geobeck.swing.JTableEx();
        jLabel9 = new javax.swing.JLabel();
        cboTgtYM = new javax.swing.JComboBox();
        lblBasicShiftBreak = new javax.swing.JLabel();
        lblBasicShiftA = new javax.swing.JLabel();
        lblBasicShiftB = new javax.swing.JLabel();
        lblBasicShiftC = new javax.swing.JLabel();
        lblBasicShiftD = new javax.swing.JLabel();
        lblBasicShiftE = new javax.swing.JLabel();
        lblBasicShiftF = new javax.swing.JLabel();
        lblBasicShiftG = new javax.swing.JLabel();
        lblBasicShiftH = new javax.swing.JLabel();
        lblBasicShiftI = new javax.swing.JLabel();
        lblBasicShiftJ = new javax.swing.JLabel();
        scrStaffShift = new javax.swing.JScrollPane();
        tblStaffShift = new com.geobeck.swing.JTableEx();
        scrStaffName = new javax.swing.JScrollPane();
        tblStaffName = new com.geobeck.swing.JTableEx();
        lblStaffHeader = new javax.swing.JLabel();
        lblBasicShiftK = new javax.swing.JLabel();
        lblBasicShiftL = new javax.swing.JLabel();
        lblBasicShiftM = new javax.swing.JLabel();
        lblBasicShiftS = new javax.swing.JLabel();
        lblBasicShiftY = new javax.swing.JLabel();
        lblBasicShiftN = new javax.swing.JLabel();
        lblBasicShiftT = new javax.swing.JLabel();
        lblBasicShiftZ = new javax.swing.JLabel();
        lblBasicShiftO = new javax.swing.JLabel();
        lblBasicShiftU = new javax.swing.JLabel();
        lblBasicShiftP = new javax.swing.JLabel();
        lblBasicShiftV = new javax.swing.JLabel();
        lblBasicShiftQ = new javax.swing.JLabel();
        lblBasicShiftW = new javax.swing.JLabel();
        lblBasicShiftR = new javax.swing.JLabel();
        lblBasicShiftX = new javax.swing.JLabel();
        btnShow = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cboStaff = new javax.swing.JComboBox();
        cboShift = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("対象");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        cblShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(127, 157, 185)));
        cblShop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cblShopMouseClicked(evt);
            }
        });
        cblShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cblShopActionPerformed(evt);
            }
        });
        add(cblShop, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 130, -1));

        btnRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btnRegist.setBorderPainted(false);
        btnRegist.setContentAreaFilled(false);
        btnRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btnRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistActionPerformed(evt);
            }
        });
        add(btnRegist, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 120, 92, 25));

        scrDayHeader.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrDayHeader.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrDayHeader.getHorizontalScrollBar().addAdjustmentListener(
            new AdjustmentListener()
            {
                public void adjustmentValueChanged(AdjustmentEvent e)
                {
                    scrStaffShift.getHorizontalScrollBar().setValue(e.getValue());
                }
            });

            tblDayHeader.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    ""
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tblDayHeader.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblDayHeader.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tblDayHeader.setSelectionForeground(new java.awt.Color(0, 0, 0));
            //technicTime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //technicTime.getTableHeader().setReorderingAllowed(false);
            //technicTime.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
            //SwingUtil.setJTableHeaderRenderer(technicTime, SystemInfo.getTableHeaderRenderer());
            //technicTime.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            //this.initTableColumnWidth();
            //SelectTableCellRenderer.setSelectTableCellRenderer(technicTime);
            //tblStaffShift.getTableHeader().setReorderingAllowed(false);
            //this.initTableColumnWidth();
            this.tblDayHeader.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
            //TableColumnModel columnmodel = tblStaffShift.getColumnModel();
            //columnmodel.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
            //SwingUtil.setJTableHeaderRenderer(tblStaffShift, SystemInfo.getTableHeaderRenderer());
            this.tblDayHeader.setTableHeader(null);
            scrDayHeader.setViewportView(tblDayHeader);

            add(scrDayHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 155, 700, 105));

            jLabel9.setText("対象月");
            add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 38, 40, 20));

            cboTgtYM.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cboTgtYMMouseClicked(evt);
                }
            });
            cboTgtYM.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cboTgtYMActionPerformed(evt);
                }
            });
            add(cboTgtYM, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 38, 100, -1));

            lblBasicShiftBreak.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftBreak.setText("●－休み");
            add(lblBasicShiftBreak, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 45, -1, -1));

            lblBasicShiftA.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftA.setText("Ａ－12:34～23:45");
            add(lblBasicShiftA, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, -1, -1));

            lblBasicShiftB.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftB.setText("Ｂ－12:34～23:45");
            add(lblBasicShiftB, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 28, -1, -1));

            lblBasicShiftC.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftC.setText("Ｃ－12:34～23:45");
            add(lblBasicShiftC, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 46, -1, -1));

            lblBasicShiftD.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftD.setText("Ｄ－12:34～23:45");
            add(lblBasicShiftD, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 64, -1, -1));

            lblBasicShiftE.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftE.setText("Ｅ－12:34～23:45");
            add(lblBasicShiftE, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 82, -1, -1));

            lblBasicShiftF.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftF.setText("Ｆ－12:34～23:45");
            add(lblBasicShiftF, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, -1, -1));

            lblBasicShiftG.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftG.setText("Ｇ－12:34～23:45");
            add(lblBasicShiftG, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 10, -1, -1));

            lblBasicShiftH.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftH.setText("Ｈ－12:34～23:45");
            add(lblBasicShiftH, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 28, -1, -1));

            lblBasicShiftI.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftI.setText("Ｉ－12:34～23:45");
            add(lblBasicShiftI, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 46, -1, -1));

            lblBasicShiftJ.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
            lblBasicShiftJ.setText("Ｊ－12:34～23:45");
            add(lblBasicShiftJ, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 64, -1, -1));

            scrStaffShift.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrStaffShift.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.scrStaffShift.getVerticalScrollBar().addAdjustmentListener(
                new AdjustmentListener()
                {
                    public void adjustmentValueChanged(AdjustmentEvent e)
                    {
                        scrStaffName.getVerticalScrollBar().setValue(e.getValue());
                    }
                });

                this.scrStaffShift.getHorizontalScrollBar().addAdjustmentListener(
                    new AdjustmentListener()
                    {
                        public void adjustmentValueChanged(AdjustmentEvent e)
                        {
                            scrDayHeader.getHorizontalScrollBar().setValue(e.getValue());
                        }
                    });

                    tblStaffShift.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {

                        }
                    ));
                    tblStaffShift.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                    tblStaffShift.setSelectionBackground(new java.awt.Color(255, 210, 142));
                    tblStaffShift.setSelectionForeground(new java.awt.Color(0, 0, 0));
                    //technicTime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    //technicTime.getTableHeader().setReorderingAllowed(false);
                    //technicTime.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
                    //SwingUtil.setJTableHeaderRenderer(technicTime, SystemInfo.getTableHeaderRenderer());
                    //technicTime.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                    //this.initTableColumnWidth();
                    //SelectTableCellRenderer.setSelectTableCellRenderer(technicTime);
                    //tblStaffShift.getTableHeader().setReorderingAllowed(false);
                    //this.initTableColumnWidth();
                    tblStaffShift.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                    //TableColumnModel columnmodel = tblStaffShift.getColumnModel();
                    //columnmodel.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
                    //SwingUtil.setJTableHeaderRenderer(tblStaffShift, SystemInfo.getTableHeaderRenderer());
                    tblStaffShift.setTableHeader(null);
                    scrStaffShift.setViewportView(tblStaffShift);

                    add(scrStaffShift, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 265, 700, 365));

                    scrStaffName.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrStaffName.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                    this.scrStaffName.getVerticalScrollBar().addAdjustmentListener(
                        new AdjustmentListener()
                        {
                            public void adjustmentValueChanged(AdjustmentEvent e)
                            {
                                scrStaffShift.getVerticalScrollBar().setValue(e.getValue());
                            }
                        });

                        tblStaffName.setModel(new javax.swing.table.DefaultTableModel(
                            new Object [][] {

                            },
                            new String [] {
                                ""
                            }
                        ) {
                            boolean[] canEdit = new boolean [] {
                                false
                            };

                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit [columnIndex];
                            }
                        });
                        tblStaffName.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                        tblStaffName.setSelectionBackground(new java.awt.Color(255, 210, 142));
                        tblStaffName.setSelectionForeground(new java.awt.Color(0, 0, 0));
                        //technicTime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        //technicTime.getTableHeader().setReorderingAllowed(false);
                        //technicTime.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
                        //SwingUtil.setJTableHeaderRenderer(technicTime, SystemInfo.getTableHeaderRenderer());
                        //technicTime.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                        //this.initTableColumnWidth();
                        //SelectTableCellRenderer.setSelectTableCellRenderer(technicTime);
                        //tblStaffShift.getTableHeader().setReorderingAllowed(false);
                        //this.initTableColumnWidth();
                        this.tblStaffName.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
                        //TableColumnModel columnmodel = tblStaffShift.getColumnModel();
                        //columnmodel.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
                        //SwingUtil.setJTableHeaderRenderer(tblStaffShift, SystemInfo.getTableHeaderRenderer());
                        this.tblStaffName.setTableHeader(null);
                        scrStaffName.setViewportView(tblStaffName);

                        add(scrStaffName, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 265, 100, 365));

                        lblStaffHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                        lblStaffHeader.setText("スタッフ名");
                        lblStaffHeader.setName(""); // NOI18N
                        lblStaffHeader.setOpaque(true);
                        lblStaffHeader.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
                        lblStaffHeader.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
                        lblStaffHeader.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                        //        lblStaffHeader.setOpaque(true);
                        //		lblStaffHeader.setText("スタッフ名");
                        lblStaffHeader.setHorizontalAlignment(SwingConstants.CENTER);
                        add(lblStaffHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 155, 100, 105));

                        lblBasicShiftK.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftK.setText("Ｋ－12:34～23:45");
                        add(lblBasicShiftK, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 82, -1, -1));

                        lblBasicShiftL.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftL.setText("Ｌ－12:34～23:45");
                        add(lblBasicShiftL, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 100, -1, -1));

                        lblBasicShiftM.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftM.setText("Ｍ－12:34～23:45");
                        add(lblBasicShiftM, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 10, -1, -1));

                        lblBasicShiftS.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftS.setText("Ｓ－12:34～23:45");
                        add(lblBasicShiftS, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 10, -1, -1));

                        lblBasicShiftY.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftY.setText("Ｙ－12:34～23:45");
                        add(lblBasicShiftY, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 10, -1, -1));

                        lblBasicShiftN.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftN.setText("Ｎ－12:34～23:45");
                        add(lblBasicShiftN, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 28, -1, -1));

                        lblBasicShiftT.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftT.setText("Ｔ－12:34～23:45");
                        add(lblBasicShiftT, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 28, -1, -1));

                        lblBasicShiftZ.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftZ.setText("Ｚ－12:34～23:45");
                        add(lblBasicShiftZ, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 28, -1, -1));

                        lblBasicShiftO.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftO.setText("Ｏ－12:34～23:45");
                        add(lblBasicShiftO, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 46, -1, -1));

                        lblBasicShiftU.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftU.setText("Ｕ－12:34～23:45");
                        add(lblBasicShiftU, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 46, -1, -1));

                        lblBasicShiftP.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftP.setText("Ｐ－12:34～23:45");
                        add(lblBasicShiftP, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 64, -1, -1));

                        lblBasicShiftV.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftV.setText("Ｖ－12:34～23:45");
                        add(lblBasicShiftV, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 64, -1, -1));

                        lblBasicShiftQ.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftQ.setText("Ｑ－12:34～23:45");
                        add(lblBasicShiftQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 82, -1, -1));

                        lblBasicShiftW.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftW.setText("Ｗ－12:34～23:45");
                        add(lblBasicShiftW, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 82, -1, -1));

                        lblBasicShiftR.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftR.setText("Ｒ－12:34～23:45");
                        add(lblBasicShiftR, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 100, -1, -1));

                        lblBasicShiftX.setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
                        lblBasicShiftX.setText("Ｘ－12:34～23:45");
                        add(lblBasicShiftX, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 100, -1, -1));

                        btnShow.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
                        btnShow.setBorderPainted(false);
                        btnShow.setContentAreaFilled(false);
                        btnShow.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
                        btnShow.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnShowActionPerformed(evt);
                            }
                        });
                        add(btnShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 35, 92, 25));

                        jPanel1.setBackground(new java.awt.Color(146, 204, 243));
                        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 80, 12, 18));

                        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                        jLabel10.setText("－休憩ﾍﾙﾌﾟ設定済み");
                        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 100, 115, 18));

                        btnApply.setIcon(SystemInfo.getImageIcon("/button/common/apply_off.jpg"));
                        btnApply.setBorderPainted(false);
                        btnApply.setContentAreaFilled(false);
                        btnApply.setPressedIcon(SystemInfo.getImageIcon("/button/common/apply_on.jpg"));
                        btnApply.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnApplyActionPerformed(evt);
                            }
                        });
                        add(btnApply, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 92, 25));

                        jLabel11.setText("スタッフ名");
                        add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 50, 20));

                        jLabel12.setText("シフト");
                        add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 30, 20));

                        add(cboStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 105, 100, -1));

                        add(cboShift, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 50, -1));

                        jLabel13.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                        jLabel13.setText("＜シフト 一括設定＞");
                        add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 87, 120, -1));

                        jLabel14.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                        jLabel14.setForeground(java.awt.Color.gray);
                        jLabel14.setText("━━━━━━━━━━━━━━━━━━━");
                        add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 260, -1));

                        btnPrint.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
                        btnPrint.setBorderPainted(false);
                        btnPrint.setContentAreaFilled(false);
                        btnPrint.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
                        btnPrint.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnPrintActionPerformed(evt);
                            }
                        });
                        add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(532, 120, 92, 25));

                        jLabel15.setForeground(java.awt.Color.blue);
                        jLabel15.setText("※休憩時間の設定は「登録」ボタンを押してシフトを確定してから設定してください。");
                        add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 655, 400, 20));

                        jLabel16.setText("　 －休憩設定済み");
                        add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 59, 100, 18));

                        jPanel2.setBackground(java.awt.Color.green);
                        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 59, 12, 18));

                        jPanel3.setBackground(new java.awt.Color(160, 70, 160));
                        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 100, 12, 18));

                        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                        jLabel18.setText("－ﾍﾙﾌﾟ設定済み");
                        add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 80, 95, 18));
                    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        printData();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed

        String msg = "";
        msg += "　スタッフ 「" + ((MstStaff)cboStaff.getSelectedItem()).getFullStaffName() + "」 のシフトを\n";
        msg += "　すべて 「" + ((MstShift)cboShift.getSelectedItem()).getShiftName() + "」 に設定します。よろしいですか？";
        msg += "\n\n";
        msg += "※ 適用後は必ず「登録」ボタンを押して確定ください。";
        msg += "\n\n";
        
        if (MessageDialog.showYesNoDialog(
                this, msg, this.getTitle(),
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION)
        {
            for (int i = 0; i < tblStaffShift.getColumnCount(); i++) {
                JComboBox cbo = (JComboBox)tblStaffShift.getValueAt(cboStaff.getSelectedIndex(), i);
                cbo.setSelectedIndex(cboShift.getSelectedIndex());
            }
        }

    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        this.showData();
    }//GEN-LAST:event_btnShowActionPerformed

    private void showBasicShifts(Integer intShopID)
    {
        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            MstShifts shiftsAllInShop = new MstShifts();
            ConnectionWrapper con;

            // 全基本シフトの表示を「未設定」にしておく
            for (int i = 0; i < this.lblBasicShift.length; ++i)
            {
                this.lblBasicShift[i].setText( (char)('Ａ' + i) + "－未設定" );
            }

            this.basicshiftsInShop.clear();

            // 「休」用インスタンスを先頭に追加
            this.basicshiftsInShop.add(MstShift.createRecessShiftInstance(intShopID));

            try
            {
               con = SystemInfo.getConnection();

               shiftsAllInShop.setShopId(intShopID);
               shiftsAllInShop.load(con, false);

               for (MstShift eachshift : shiftsAllInShop)
               {
                   String strShiftName;

                   strShiftName = eachshift.getShiftName();
                   if (strShiftName != null  &&  strShiftName.length() > 0)
                   {
                       int iShift;

                       iShift = strShiftName.charAt(0) - 'A';
                       if (iShift >= 0  &&  iShift < this.lblBasicShift.length)
                       {
                           String strStartTime, strEndTime;

                           strStartTime = eachshift.getStartTime();
                           strEndTime = eachshift.getEndTime();

                           boolean isTime = true;
                           isTime = isTime && strStartTime != null;
                           isTime = isTime && CheckUtil.isNumber(strStartTime);
                           isTime = isTime && strEndTime != null;
                           isTime = isTime && CheckUtil.isNumber(strEndTime);
                           isTime = isTime && !strStartTime.equals("0000") ;
                           isTime = isTime && !strEndTime.equals("0000");
                                                      
                           if (isTime)
                           {
                               this.lblBasicShift[iShift].setText(
                                   String.format("%c－%s:%s～%s:%s"
                                   ,    strShiftName.charAt(0) - 'A' + 'Ａ'
                                   ,    strStartTime.substring(0, 2)
                                   ,    strStartTime.substring(2, 4)
                                   ,    strEndTime.substring(0, 2)
                                   ,    strEndTime.substring(2, 4)
                                   )
                               );

                               // 有効なシフトのみ cbo用 MstShifts変数に追加
                               this.basicshiftsInShop.add(eachshift);
                           }
                       }
                   }
               }

               con.close();
            }
            catch (SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return;
            }
            
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    /**
     * IVS_PTQUANG End add New request #54491
     * @param date
     * @return 
     */
    private String convertStringToDate(String date)
    {
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String splitDates = year +"/" + month;
        return splitDates;
    }
    
    /**
     * 
     * @param intShopID
     * @param strTgtYM
     * @return 
     */
    private boolean registerStaffShift(Integer intShopID, String strTgtYM)
    {
        ConnectionWrapper con;

        con	= SystemInfo.getConnection();
    
        try
        {
       
            
            con.begin();
            
            DataSchedules schedulesInMonth = new DataSchedules();
            Calendar calen = Calendar.getInstance();
            
            // 対象年月の初日をセット
            calen.set(
                Integer.parseInt(strTgtYM.substring(0, 4))
            ,   Integer.parseInt(strTgtYM.substring(4, 6)) - 1
            ,   1
            );
            ArrayList<Integer> arrIDStaff = new ArrayList<Integer>();
            for (int row = 0; row < this.tblStaffShift.getRowCount(); ++row)
            {
                int nStaffID;
                // この行のスタッフID を取得しておく
                nStaffID = ((MstStaff)this.tblStaffName.getValueAt(row, 0)).getStaffID();
                arrIDStaff.add(nStaffID);
                DataSchedule schedule = null;
                for (int col = 0; col < this.tblStaffShift.getColumnCount(); ++col)
                {
                    schedule = new DataSchedule();
                    
                    calen.set(Calendar.DAY_OF_MONTH, col + 1);
                    
                    schedule.setShopId(intShopID);
                    schedule.setStaffId(nStaffID);
                    schedule.setScheduleDate(calen.getTime());
                    schedule.setBasicshift( (MstShift)( (JComboBox)this.tblStaffShift.getValueAt(row, col) ).getSelectedItem() );
                    
                    schedulesInMonth.add(schedule);
                    
                }
                
        }
            schedulesInMonth.registSchedule(con);
            con.commit();
            con.close();
            //IVS_LVTu start edit 2016/11/18 New request #58626
            //IVS_PTQUANG Start add New request #54490 
            Integer shopID = ((MstShop)cblShop.getSelectedItem()).getShopID();
            SystemInfo.getMstUser().setShopID(shopID);
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                this.flag = true;
                for (Integer staffID : arrIDStaff) {
                    //IVS_LVTu start edit 2016/11/29 Bug #58788
                    //DataSchedule schedule = schedulesInMonth.getSchedule(staffID);
                    //if (SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                    //if (schedule.getBasicshift().getShiftId() != 0) {
                    String loginId = SystemInfo.getLoginID();
                    String formatDate = convertStringToDate(strTgtYM);
                    if(!sendScheduleAPI(loginId, staffID, formatDate, shopID)) {
                        this.flag = false;
                    }
                    //}
                    //}
                    //IVS_LVTu start edit 2016/11/29 Bug #58788
                    //IVS_PTQUANG End add New request #54490
                }
            }
        }
        //IVS_LVTu end edit 2016/11/17 New request #58626
        catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            
            try
            {
                if (con.isBeginTran())
                {
                    con.rollback();
                }
                
                con.close();
            }
            catch (SQLException sql_e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            return false;
        }
        
        // 基本シフト時間ラベルを再表示
        showBasicShifts(intShopID);
        
        return true;
    }
    
    private void btnRegistActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRegistActionPerformed
    {//GEN-HEADEREND:event_btnRegistActionPerformed

        //IVS_LVTu start edit 2016/11/17 New request #58626
        if (!verifyRegisteration()) return;
        //IVS_LVTu start add New request #48969
        //IVS_LVTU start add 2017/05/10 #9942 [gb]ヘルプ登録後にシフトを「休日」にした場合、予約表示が表示できない
        //入力チェック
        if (!this.checkInput()) {
            return;
        }
        //IVS_LVTU end add 2017/05/10 #9942 [gb]ヘルプ登録後にシフトを「休日」にした場合、予約表示が表示できない
        if( !checkMessage("のシフトを登録します")) {
           return;
        }
        //IVS_LVTu end add New request #48969
        //if (!verifyRegisteration()) return;
        //IVS_LVTu end edit 2016/11/17 New request #58626
        btnRegist.setCursor(null);
   
        
        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if (registerStaffShift( ((MstShop)cblShop.getSelectedItem()).getShopID(), this.arrTgtYM.get(this.cboTgtYM.getSelectedIndex())))
            {
                
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                    this.getTitle(),
                   
                    JOptionPane.INFORMATION_MESSAGE);
                    SystemInfo.getLogger().log(Level.INFO, this.getTitle() + "：" + MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS));
                    //IVS_PTQUANG Start add New request #54490   
                    if(this.flag == false)
                    {
                         MessageDialog.showMessageDialog(this,"媒体との連動ができませんでした。",this.getTitle(),JOptionPane.ERROR_MESSAGE);
                    }
                    //IVS_PTQUANG End add New request #54490   
            }
            else
            {
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "スタッフシフト"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                    SystemInfo.getLogger().log(Level.INFO, this.getTitle() + "：" + MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "スタッフシフト"));
            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
   
        /**
         * IVS_PTQUANG start add 2016/09/08 New request #54490
         * @param login_id
         * @param reservation_no
         * @param shop_id
         * @return 
         */
        private Boolean sendScheduleAPI(String login_id, int staff_id, String schedule_date, Integer shop_id){
            try{
                
                MstAPI api = new MstAPI(0);
                String apiUrl = api.getApiUrl().trim();
                String url = apiUrl + "/s/send/schedule.php";
                
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
                con.setRequestProperty( "charset", "utf-8");
                 
                //set parameter
                Map mapParam = new LinkedMap();
                mapParam.put("login_id", login_id);
                mapParam.put("staff_id", staff_id);
                //IVS_LVTu start add 2016/11/17 New request #58626
                mapParam.put("shop_id", shop_id);
                //IVS_LVTu end add 2016/11/17 New request #58626
                mapParam.put("schedule_date", schedule_date);
                Gson gson = new Gson(); 
                String jsonParam = gson.toJson(mapParam); 
                String urlParameters = "param=" + jsonParam;
                
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
                
                //get response
                BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
                if(response.toString().contains("\"code\":200")){
                    return true;
                }
            }catch(Exception e){
                return false;
            }
            return false;

    }//GEN-LAST:event_btnRegistActionPerformed

    //IVS_LVTu start add New request #48969
    private void cboTgtYMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTgtYMActionPerformed
        if(prevIndex == cboTgtYM.getSelectedIndex()) {
            return;
        }
        prevIndex = cboTgtYM.getSelectedIndex();
        if(flagCmb) {
            if( checkMessage("を表示します。よろしいですか？")) {
                this.showData();
            }else {
                if(this.indexItemYM > -1)
                    this.cboTgtYM.setSelectedIndex(this.indexItemYM);
                    prevIndex = this.indexItemYM;
            }
        }
        
    }//GEN-LAST:event_cboTgtYMActionPerformed

    private void cboTgtYMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTgtYMMouseClicked
        this.indexItemYM = this.cboTgtYM.getSelectedIndex();
        
    }//GEN-LAST:event_cboTgtYMMouseClicked

    private void cblShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cblShopActionPerformed
        //IVS_LVTu start add Bug #49141
        if(flagCmb) {
            MstShop shop;
            shop = (MstShop)this.cblShop.getSelectedItem();
            
            if( checkShopMessage(shop.getShopName() + "を表示します。よろしいですか？")) {
                this.showData();
            }else {
                if(this.indexItemShop > -1)
                    this.cblShop.setSelectedIndex(this.indexItemShop);
            }
        }
        //IVS_LVTu end add Bug #49141
    }//GEN-LAST:event_cblShopActionPerformed

    private void cblShopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cblShopMouseClicked
        this.indexItemShop =  this.cblShop.getSelectedIndex();
    }//GEN-LAST:event_cblShopMouseClicked

    //IVS_LVTU start add 2017/05/10 #9942 [gb]ヘルプ登録後にシフトを「休日」にした場合、予約表示が表示できない
    /**
    * 入力チェックを行う。
    * @return true - ＯＫ
    */
    private boolean checkInput() {
        int indexItem   = this.cboTgtYM.getSelectedIndex();
        int year        = Integer.parseInt(this.arrTgtYM.get(indexItem).substring(0, 4).toString());
        int month       = Integer.parseInt(this.arrTgtYM.get(indexItem).substring(4, 6).toString()) - 1;

        try {
            Calendar calen = Calendar.getInstance();

            int shopID = ((MstShop)this.cblShop.getSelectedItem()).getShopID();
            DataScheduleDetail scheduleDetail = new DataScheduleDetail();
            List<DataScheduleDetail> dsScheduleDetail = scheduleDetail.loadScheduleDetail(SystemInfo.getConnection(), calen, shopID);

            // 対象年月の初日をセット
            calen.set(year, month, 1);
            //message : date + FullStaffName
            StringBuilder messages = new StringBuilder(1000);
            // Format date yyyy/MM/dd.
            Format formatterDate = new SimpleDateFormat("yyyy/MM/dd");
            // Format date dd-M-yyyy hh:mm:ss.
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Boolean flagResult = true;
            for (int row = 0; row < this.tblStaffShift.getRowCount(); ++row) {
                int nStaffID;
                String staffName;
                // この行のスタッフID を取得しておく
                nStaffID = ((MstStaff)this.tblStaffName.getValueAt(row, 0)).getStaffID();
                staffName = ((MstStaff)this.tblStaffName.getValueAt(row, 0)).getFullStaffName();
                for (int col = 0; col < this.tblStaffShift.getColumnCount(); ++col) {
                    calen.set(Calendar.DAY_OF_MONTH, col + 1);
                    String strDateCurrent = formatterDate.format(calen.getTime());

                    MstShift mshift = (MstShift)( (JComboBox)this.tblStaffShift.getValueAt(row, col) ).getSelectedItem();

                    if (mshift.getShiftId() != null) {
                        if (0 == mshift.getShiftId()) {
                            for (DataScheduleDetail dsd : dsScheduleDetail) {
                                String strDateSchedule = formatterDate.format(dsd.getScheduleDate());
                                if (dsd.getStaffID().equals(nStaffID) && strDateSchedule.equals(strDateCurrent)) {
                                    messages.append("\n").append(formatterDate.format(dsd.getScheduleDate())).append("　").append(staffName);
                                    flagResult = false;
                                }
                            }
                        } else {
                            String startTime;//開始時間
                            String endTime;//終了時間
                            if (mshift.getShiftId() == null || mshift.getShiftId() == 0) {
                                startTime   = "00:00";
                                endTime     = "00:00";
                            } else {
                                startTime   = mshift.getStartTime().substring(0, 2) + ":" + mshift.getStartTime().substring(2, mshift.getStartTime().length());
                                endTime     = mshift.getEndTime().substring(0, 2) + ":" + mshift.getEndTime().substring(2, mshift.getEndTime().length());
                            }

                            String strStartShift = formatterDate.format(calen.getTime()) + " " + startTime + ":00";
                            String strEndTime = formatterDate.format(calen.getTime()) + " " + endTime + ":00";

                            Date startShift = sdf.parse(strStartShift);
                            Date endShift = sdf.parse(strEndTime);
                            for (DataScheduleDetail dsd : dsScheduleDetail) {
                                String strDateSchedule = formatterDate.format(dsd.getScheduleDate());
                                // check staff and schedule
                                if (dsd.getStaffID().equals(nStaffID) && strDateSchedule.equals(strDateCurrent)) {
                                    // check time.
                                    if(dsd.getExtStartTime().before(startShift) || dsd.getExtEndTime().after(endShift)) {
                                        messages.append("\n").append(formatterDate.format(dsd.getScheduleDate())).append("　").append(staffName);
                                        flagResult = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!flagResult) {
                MessageDialog.showMessageDialog(this, 
                        MessageUtil.getMessage(20002) + messages, 
                        this.getTitle(), 
                        JOptionPane.ERROR_MESSAGE);
                return flagResult;
            }
        } catch (SQLException ex) {
        	Logger.getLogger(StaffShiftPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
        	Logger.getLogger(StaffShiftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    //IVS_LVTU end add 2017/05/10 #9942 [gb]ヘルプ登録後にシフトを「休日」にした場合、予約表示が表示できない

    private boolean checkMessage(String message) {
        int indexItem   = this.cboTgtYM.getSelectedIndex();
        int year        = Integer.parseInt( this.arrTgtYM.get(indexItem).substring(0, 4).toString());
        int month       = Integer.parseInt( this.arrTgtYM.get(indexItem).substring(4, 6).toString());

        try {
            String strDate = calendarHeader(year, month);
            //cboTgtYM.requestFocus(true);
            cboTgtYM.setFocusable(false);
            cboTgtYM.updateUI();
            if(MessageDialog.showYesNoDialog(this,
                    strDate + message,
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) != 0)
            {
                return false;
            }else {
                return true;
            }
        } catch (ParseException ex) {
            Logger.getLogger(StaffShiftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    //IVS_LVTu start add Bug #49141
    private boolean checkShopMessage(String message) {

        try {
            cblShop.setFocusable(false);
            cblShop.updateUI();
            if(MessageDialog.showYesNoDialog(this,
                    message,
                    this.getTitle(),
                    JOptionPane.QUESTION_MESSAGE) != 0)
            {
                return false;
            }else {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(StaffShiftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    //IVS_LVTu end add Bug #49141
    
    public String calendarHeader(int year, int month) throws ParseException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String dateInString = year + "-" + month +"-01";
	Date date = sdf.parse(dateInString);
  
        Locale locale = new Locale("ja", "JP", "JP");
        DateFormat format = new SimpleDateFormat("yyyy年 MM月");
        //System.out.println("DATE: " + format.format(date));
        return format.format(date);
    }
    //IVS_LVTu end add New request #48969
    private boolean verifyRegisteration()
    {
        if (this.tblStaffShift.getRowCount() <= 0)
        {
            MessageDialog.showMessageDialog(this,
                MessageUtil.getMessage(MessageUtil.ERROR_NO_DATA),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            
            return false;
        }
        
        //IVS_LVTu start add 2016/11/17 New request #58626
        Integer shopID = ((MstShop)cblShop.getSelectedItem()).getShopID();
        SystemInfo.getMstUser().setShopID(shopID);
        if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            Format formatter = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calen = Calendar.getInstance();

            // 対象年月の初日をセット
            calen.set(
                Integer.parseInt(this.arrTgtYM.get(this.cboTgtYM.getSelectedIndex()).substring(0, 4))
            ,   Integer.parseInt(this.arrTgtYM.get(this.cboTgtYM.getSelectedIndex()).substring(4, 6)) - 1
            ,   1
            );
            boolean flagOverlap = false;
            StringBuilder messages = new StringBuilder(1000);
            messages.append("既に休憩登録されている為、シフト変更できません。");
            for (int row = 0; row < this.tblStaffShift.getRowCount(); ++row)
            {
                int nStaffID;
                String staffName;
                // この行のスタッフID を取得しておく
                nStaffID = ((MstStaff)this.tblStaffName.getValueAt(row, 0)).getStaffID();
                staffName = ((MstStaff)this.tblStaffName.getValueAt(row, 0)).getFullStaffName();
                DataSchedule schedule = null;
                for (int col = 0; col < this.tblStaffShift.getColumnCount(); ++col)
                {
                    schedule = new DataSchedule();

                    calen.set(Calendar.DAY_OF_MONTH, col + 1);

                    schedule.setShopId(shopID);
                    schedule.setStaffId(nStaffID);
                    schedule.setScheduleDate(calen.getTime());
                    schedule.setBasicshift( (MstShift)( (JComboBox)this.tblStaffShift.getValueAt(row, col) ).getSelectedItem() );

                    int startTime;//開始時間
                    int endTime;//終了時間
                    if(schedule.getBasicshift().getShiftId() == null || schedule.getBasicshift().getShiftId() == 0) {
                        startTime   = 0;
                        endTime     = 0;
                    }else {
                        startTime   = Integer.parseInt(schedule.getBasicshift().getStartTime());
                        endTime     = Integer.parseInt(schedule.getBasicshift().getEndTime());
                    }

                    try {
                        if(DataSchedules.checkRecessOverlap(SystemInfo.getConnection(), schedule.getStaffId(), schedule.getScheduleDate(), startTime, endTime)) {
                            messages.append("\n").append(formatter.format(schedule.getScheduleDate())).append("　").append(staffName);
                            flagOverlap = true;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(StaffShiftPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if(flagOverlap) {
                MessageDialog.showMessageDialog(this,
                    messages,
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        //IVS_LVTu end add 2016/11/17 New request #58626

        return true;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRegist;
    private javax.swing.ButtonGroup btnReportType;
    private javax.swing.JButton btnShow;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cblShop;
    private javax.swing.JComboBox cboShift;
    private javax.swing.JComboBox cboStaff;
    private javax.swing.JComboBox cboTgtYM;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBasicShiftA;
    private javax.swing.JLabel lblBasicShiftB;
    private javax.swing.JLabel lblBasicShiftBreak;
    private javax.swing.JLabel lblBasicShiftC;
    private javax.swing.JLabel lblBasicShiftD;
    private javax.swing.JLabel lblBasicShiftE;
    private javax.swing.JLabel lblBasicShiftF;
    private javax.swing.JLabel lblBasicShiftG;
    private javax.swing.JLabel lblBasicShiftH;
    private javax.swing.JLabel lblBasicShiftI;
    private javax.swing.JLabel lblBasicShiftJ;
    private javax.swing.JLabel lblBasicShiftK;
    private javax.swing.JLabel lblBasicShiftL;
    private javax.swing.JLabel lblBasicShiftM;
    private javax.swing.JLabel lblBasicShiftN;
    private javax.swing.JLabel lblBasicShiftO;
    private javax.swing.JLabel lblBasicShiftP;
    private javax.swing.JLabel lblBasicShiftQ;
    private javax.swing.JLabel lblBasicShiftR;
    private javax.swing.JLabel lblBasicShiftS;
    private javax.swing.JLabel lblBasicShiftT;
    private javax.swing.JLabel lblBasicShiftU;
    private javax.swing.JLabel lblBasicShiftV;
    private javax.swing.JLabel lblBasicShiftW;
    private javax.swing.JLabel lblBasicShiftX;
    private javax.swing.JLabel lblBasicShiftY;
    private javax.swing.JLabel lblBasicShiftZ;
    private javax.swing.JLabel lblStaffHeader;
    private javax.swing.JScrollPane scrDayHeader;
    private javax.swing.JScrollPane scrStaffName;
    private javax.swing.JScrollPane scrStaffShift;
    private com.geobeck.swing.JTableEx tblDayHeader;
    private com.geobeck.swing.JTableEx tblStaffName;
    private com.geobeck.swing.JTableEx tblStaffShift;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
    
    private LocalFocusTraversalPolicy   ftp;
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy()
    {
        return  ftp;
    }
    
    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
                    extends FocusTraversalPolicy
    {
        ArrayList<Component> controls = new ArrayList<Component>();
        
        public LocalFocusTraversalPolicy()
        {
            controls.add(cblShop);
            controls.add(cboTgtYM);
            
            for( Component control : controls ){
                control.addKeyListener(SystemInfo.getMoveNextField());
                control.addFocusListener(SystemInfo.getSelectText());
            }
            
            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(cblShop);
            controls.add(cboTgtYM);
        };
            
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                                           Component aComponent)
        {
//            boolean find = false;
//            for(Component co : controls){
//                if( find ){
//                    if( co.isEnabled() ){
//                        return co;
//                    }
//                } else if (aComponent.equals(co)){
//                    find = true;
//                }
//            }
//            return null;
            if(aComponent.equals(cblShop)){
                return cboTgtYM;
            }else if(aComponent.equals(cboTgtYM))
            {
                return cboStaff;
            }
            else if(aComponent.equals(cboStaff))
            {
                return cboShift;
            }
            return this.getStartComponent();
        }

        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         */
        public Component getComponentBefore(Container aContainer,
                                            Component aComponent)
        {
           /* boolean find = false;
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
            return null;*/
            return this.getStartComponent();
        }

        /**
         * トラバーサルサイクルの最初の Component を返します。
         */
        public Component getFirstComponent(Container aContainer)
        {
            return getDefaultComponent(aContainer);
        }

        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer)
        {
            return cboShift;
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer)
        {
//            for(Component co : controls){
//                if( co.isEnabled() ){
//                    return co;
//                }
//            }
//            return controls.get(0);
             return this.getStartComponent();
        }
        
        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
         */
        public Component getInitialComponent(Window window)
        {
//            for(Component co : controls){
//                if( co.isEnabled() ){
//                    return co;
//                }
//            }
//            return controls.get(0);
            return this.getStartComponent();
        }
        private  Component getStartComponent(){
            if(cblShop.getItemCount() ==1)
            {
                return cboTgtYM;
            }
            return cblShop;
        }
    }
    
    private void printData() {

        JExcelApi jx = new JExcelApi("シフト一覧");
        jx.setTemplateFile("/report/シフト一覧.xls");

        // ヘッダ出力
        jx.setValue(4, 3, ((MstShop)cblShop.getSelectedItem()).getShopName());
        jx.setValue(4, 4, cboTgtYM.getSelectedItem().toString());
        jx.setValue(13, 3, getShiftTime(lblBasicShiftA));
        jx.setValue(13, 4, getShiftTime(lblBasicShiftB));
        jx.setValue(13, 5, getShiftTime(lblBasicShiftC));
        jx.setValue(13, 6, getShiftTime(lblBasicShiftD));
        jx.setValue(13, 7, getShiftTime(lblBasicShiftE));
        jx.setValue(13, 8, getShiftTime(lblBasicShiftF));
        jx.setValue(18, 3, getShiftTime(lblBasicShiftG));
        jx.setValue(18, 4, getShiftTime(lblBasicShiftH));
        jx.setValue(18, 5, getShiftTime(lblBasicShiftI));
        jx.setValue(18, 6, getShiftTime(lblBasicShiftJ));
        jx.setValue(18, 7, getShiftTime(lblBasicShiftK));
        jx.setValue(18, 8, getShiftTime(lblBasicShiftL));
        jx.setValue(23, 3, getShiftTime(lblBasicShiftM));
        jx.setValue(23, 4, getShiftTime(lblBasicShiftN));
        jx.setValue(23, 5, getShiftTime(lblBasicShiftO));
        jx.setValue(23, 6, getShiftTime(lblBasicShiftP));
        jx.setValue(23, 7, getShiftTime(lblBasicShiftQ));
        jx.setValue(23, 8, getShiftTime(lblBasicShiftR));
        jx.setValue(28, 3, getShiftTime(lblBasicShiftS));
        jx.setValue(28, 4, getShiftTime(lblBasicShiftT));
        jx.setValue(28, 5, getShiftTime(lblBasicShiftU));
        jx.setValue(28, 6, getShiftTime(lblBasicShiftV));
        jx.setValue(28, 7, getShiftTime(lblBasicShiftW));
        jx.setValue(28, 8, getShiftTime(lblBasicShiftX));
        jx.setValue(33, 3, getShiftTime(lblBasicShiftY));
        jx.setValue(33, 4, getShiftTime(lblBasicShiftZ));
        
        String strTgtYM = this.arrTgtYM.get(this.cboTgtYM.getSelectedIndex());
        Calendar calen = Calendar.getInstance();
        // 対象年月の初日をセット
        calen.set(
            Integer.parseInt(strTgtYM.substring(0, 4))
           ,Integer.parseInt(strTgtYM.substring(4, 6)) - 1
           ,1
        );
        // 対象年月の日数
        final int fn_nDaysInMonth = calen.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 0; day < fn_nDaysInMonth; ++day)
        {
            calen.set(Calendar.DAY_OF_MONTH, day + 1);
            jx.setValue(day + 6, 10, calen.getTime());
        }
        
        int col = 6;
        int row = 12;
        
        MstShop shop = (MstShop)this.cblShop.getSelectedItem();
        MstStaffs staffsInShop = new MstStaffs();
        DataSchedules schedulesInDate = new DataSchedules();

        staffsInShop.setShopIDList(String.valueOf(shop.getShopID()));
        schedulesInDate.setShop(shop);

        try
        {
            ConnectionWrapper con = SystemInfo.getConnection();

            // 自店スタッフ達を取得
            staffsInShop.loadOnlyShop(con, false);

            Map mapShift = schedulesInDate.loadAll(con, calen);

            int maxRow = staffsInShop.size() - 2;
            //IVS_vtnhan start edit 20140903 Request #30335
            if (maxRow > 0) {
                // 追加行数セット
                jx.insertRow(row, maxRow);
                for (int i = 0; i < maxRow; i++) {
                    jx.mergeCells(1, row + i + 1, 5, row + i + 1);
                }
            }
            //IVS_vtnhan end edit 20140903 Request #30335
            
            for (int iStaff = 0; iStaff < staffsInShop.size(); ++iStaff)
            {
                // スタッフ名テーブル
                MstStaff staff = staffsInShop.get(iStaff);

                jx.setValue(1, row + iStaff, staff.getFullStaffName());

                for (int day = 0; day < fn_nDaysInMonth; ++day)
                {
                    jx.setValue(col + day, row + iStaff, "●");
                    
                    ArrayList key = new ArrayList();
                    key.add(staffsInShop.get(iStaff).getStaffID());
                    key.add(day + 1);

                    Integer shiftId = (Integer)mapShift.get(key);

                    if (shiftId != null)
                    {
                        for (int i = 0; i < this.basicshiftsInShop.size(); ++i)
                        {
                            if ( ((MstShift)this.basicshiftsInShop.get(i)).getShiftId().equals(shiftId))
                            {
                                jx.setValue(col + day, row + iStaff, this.basicshiftsInShop.get(i).toString());
                                break;
                            }
                        }
                    }
                }
            }
            //IVS_vtnhan start add 20140903 Request #30335
            if(staffsInShop.size() == 1){
                jx.removeRow(row + 1);
            }
            //IVS_vtnhan end add 20140903 Request #30335
            con.close();
        }
        catch (SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return;
        }

        jx.openWorkbook();
    }
    
    private String getShiftTime(JLabel label) {
        String s = label.getText();
        return s.substring(s.indexOf("－") + 1);
    }
}

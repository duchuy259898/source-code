package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.company.MstShopCategory;
import com.geobeck.sosia.pos.master.company.MstShopCategorys;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.awt.Cursor;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.ListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author lvtu
 */
public class MstRankSettingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    private MstRanks ranks                  = new MstRanks();
    private MstShopCategorys mscs           = new MstShopCategorys();
    private Integer     shopCategoryId      = 0;
    private final Integer TABLE_ROW_HEIGHT  = 30;
    
    
    public MstRankSettingPanel()
    {
        initComponents();
        intShopCategory();
        this.setSize(760,460);
        this.setPath("顧客分析＞＞マッシャー分析");
        this.setTitle("ランク設定");
        addMouseCursorChange();
        ftp = new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());

        this.showData();
        btnRegist.setEnabled(true);
    }
    
    
    
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnShow);
        SystemInfo.addMouseCursorChange(btnRegist);
        SystemInfo.addMouseCursorChange(btnClose);
    }

    private void intShopCategory() {

        categoryCbx.removeAllItems();
        categoryCbx.addItem("");
        
        SimpleMaster sm = new SimpleMaster(
                "",
                "mst_shop_category",
                "shop_category_id",
                "shop_class_name", 0);

        sm.loadData();
        for (MstData md : sm) {
            if (md != null) {
                MstShopCategory category = new MstShopCategory();
                category.setShopCategoryId(md.getID());
                category.setShopClassName(md.getName());
                category.setDisplaySeq(md.getDisplaySeq());
                mscs.add(category);
            }

        }
            
        for (MstShopCategory item : mscs) {
            categoryCbx.addItem(item);
        }
        categoryCbx.setSelectedIndex(0);
    }

    private void showData()
    {
       try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            MstRankSetting mstRankSetting = new MstRankSetting();
            MstRanks    mstRanks =  new MstRanks();
            
            mstRankSetting.setShopCategoryId(this.shopCategoryId);
            
            // 全コンポーネント削除
            SwingUtil.clearTable(this.tbl1);
            SwingUtil.clearTable(this.tbl2);
            
            DefaultTableModel dtmtb1 = (DefaultTableModel)this.tbl1.getModel();
            DefaultTableModel dtmtb2 = (DefaultTableModel)this.tbl2.getModel();
            
            dtmtb1.setColumnCount(5);
            dtmtb2.setColumnCount(5);

            try
            {
                ConnectionWrapper con = SystemInfo.getConnection();

                mstRankSetting.LoadByShopCategoryId(con);
                mstRanks.load(con);
                //MstRankItemGroup mrig = new MstRankItemGroup();
                MasherRanking5List mr5l = new MasherRanking5List();
                MstRank mstRank = new MstRank();
                
                //mrig.selectDataByShopCategoryId(con, shopCategoryId);
                mr5l.getGroupNameRanking(shopCategoryId);
                mstRank.setRankId(-1);
                mstRank.setRankName("");
                mstRanks.add(0, mstRank);
                // set value lable
                //if("".equals(mrig.getItemGroupName()))
                if("".equals(mr5l.getItemGroupName()))
                {
                    lblRankAdvancedSetting.setText("");
                }
                lblRankAdvancedSetting.setText(mr5l.getItemGroupName());
                for (int row = 0; row < 5; ++row)
                {
                    // スタッフシフトテーブル
                    JComboBox[] cboRowData = new JComboBox[5];
                    for (int col = 0; col < 5; ++col)
                    {
                        JComboBox cbo;
                        

                        cbo = new JComboBox(mstRanks.toArray());
                        cbo.setRenderer(new MyListCellRenderer(cbo.getRenderer()));
                        if(mstRanks.size() > 0)
                        {
                            cbo.setSelectedIndex(0);
                        }
                        
                        Integer shopCategoryId = null;
                        // column x5y5 not null in database.
                        if(mstRankSetting.getX5y5() != null)
                        {
                            shopCategoryId = mstRankSetting.getShopCategoryId();
                        }
                        
                        if (shopCategoryId != null && mstRanks.size() > 0)
                        {
                            Integer[][] arr = mstRankSetting.coverToArray(5,5);
                            for (int i = 1; i < cbo.getItemCount(); ++i)
                            {
                                if ( ((MstRank)cbo.getItemAt(i)).getRankId().equals(arr[row][col]) ) 
                                {
                                    cbo.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }

                        // レンダラー用コンポーネント追加
                        cbo.setBounds(100 * col, TABLE_ROW_HEIGHT * row, 100 - 1, TABLE_ROW_HEIGHT - 1);
                        this.tbl1.add(cbo);

                        cboRowData[col] = cbo;
                    }
                    
                    dtmtb1.addRow(cboRowData);
                    
                }
                
                
                for( int row = 0;row < 2;row ++)
                {
                    // スタッフシフトテーブル
                    JComboBox[] cboRowData = new JComboBox[5];
                    for( int col = 0; col < 5 ;col ++)
                    {
                        JComboBox cbo;

                        cbo = new JComboBox(mstRanks.toArray());
                        cbo.setRenderer(new MyListCellRenderer(cbo.getRenderer()));
                        if(mstRanks.size() > 0)
                        {
                            cbo.setSelectedIndex(0);
                        }
                        
                        Integer shopCategoryId = null;
                        // column x5y5 not null in database.
                        if(mstRankSetting.getX5y5() != null)
                        {
                            shopCategoryId = mstRankSetting.getShopCategoryId();
                        }
                        
                        if (shopCategoryId != null && mstRanks.size() > 0)
                        {
                            Integer[][] arr = mstRankSetting.coverToArray(2,5);
                            for (int i = 1; i < cbo.getItemCount(); ++i)
                            {
                                if ( ((MstRank)cbo.getItemAt(i)).getRankId().equals(arr[row][col]) ) 
                                {
                                    cbo.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }

                        cbo.setBounds(100 * col, TABLE_ROW_HEIGHT * row, 100 - 1, TABLE_ROW_HEIGHT - 1);
                        this.tbl2.add(cbo);

                        cboRowData[col] = cbo;
                    }
                    
                    dtmtb2.addRow(cboRowData);
                }
                
            }
            catch (SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return;
            }

            for (int i = 0; i < 5; ++i)
            {
                this.tbl1.getColumnModel().getColumn(i).setMaxWidth(100);
                this.tbl1.getColumnModel().getColumn(i).setMinWidth(100);
                
                this.tbl2.getColumnModel().getColumn(i).setMaxWidth(100);
                this.tbl2.getColumnModel().getColumn(i).setMinWidth(100);
                
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnReportType = new javax.swing.ButtonGroup();
        btnRegist = new javax.swing.JButton();
        btnShow = new javax.swing.JButton();
        categoryCbx = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblCategory1 = new javax.swing.JLabel();
        lblCol5 = new javax.swing.JLabel();
        tbl1 = new com.geobeck.swing.JTableEx();
        lblTax26 = new javax.swing.JLabel();
        lblCol1 = new javax.swing.JLabel();
        lblCol4 = new javax.swing.JLabel();
        lblTax29 = new javax.swing.JLabel();
        lblCol2 = new javax.swing.JLabel();
        lblTechnology = new javax.swing.JLabel();
        lblRow5 = new javax.swing.JLabel();
        lblRow4 = new javax.swing.JLabel();
        lblRow3 = new javax.swing.JLabel();
        lblRow2 = new javax.swing.JLabel();
        lblRow1 = new javax.swing.JLabel();
        lblTax37 = new javax.swing.JLabel();
        lblRankAdvancedSetting = new javax.swing.JLabel();
        lblBuy = new javax.swing.JLabel();
        lblNotBuy = new javax.swing.JLabel();
        lblCo5 = new javax.swing.JLabel();
        lblCo4 = new javax.swing.JLabel();
        lblCo3 = new javax.swing.JLabel();
        lblCo2 = new javax.swing.JLabel();
        lblCo1 = new javax.swing.JLabel();
        tbl2 = new com.geobeck.swing.JTableEx();
        lblCategory = new javax.swing.JLabel();
        lblTable1 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(782, 532));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btnRegist.setBorderPainted(false);
        btnRegist.setContentAreaFilled(false);
        btnRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btnRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistActionPerformed(evt);
            }
        });
        add(btnRegist, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 92, 25));

        btnShow.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        btnShow.setBorderPainted(false);
        btnShow.setContentAreaFilled(false);
        btnShow.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });
        add(btnShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 92, 25));

        categoryCbx.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        categoryCbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryCbxActionPerformed(evt);
            }
        });
        add(categoryCbx, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 180, 25));

        lblCategory1.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        lblCategory1.setText("＜表2＞");
        add(lblCategory1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, 20));

        lblCol5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCol5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCol5.setText("5");
        lblCol5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblCol5.setFocusCycleRoot(true);
        lblCol5.setOpaque(true);
        lblCol5.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
        lblCol5.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
        //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        //        lblStaffHeader.setOpaque(true);
        //		lblStaffHeader.setText("スタッフ名");
        lblCol5.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblCol5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 100, 30));

        tbl1.setBackground(new java.awt.Color(240, 240, 240));
        tbl1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
            }) {
                public Class getColumnClass(int columnIndex) {
                    return Object.class;
                }
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return true;
                }
            });
            tbl1.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
            tbl1.setSelectionBackground(new java.awt.Color(255, 210, 142));
            tbl1.setSelectionForeground(new java.awt.Color(0, 0, 0));
            tbl1.setRowHeight(TABLE_ROW_HEIGHT);
            tbl1.setTableHeader(null);
            add(tbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 500, 150));

            lblTax26.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
            lblTax26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblTax26.setText("<HTML>来<br>店<br>周<br>期</HTML>");
            lblTax26.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblTax26.setFocusCycleRoot(true);
            lblTax26.setOpaque(true);
            lblTax26.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblTax26.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //       lblTax26.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblTax26.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblTax26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 80, 150));

            lblCol1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCol1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCol1.setText("1");
            lblCol1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCol1.setFocusCycleRoot(true);
            lblCol1.setOpaque(true);
            lblCol1.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCol1.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCol1.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCol1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 90, 100, 30));

            lblCol4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCol4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCol4.setText("4");
            lblCol4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCol4.setFocusCycleRoot(true);
            lblCol4.setOpaque(true);
            lblCol4.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCol4.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCol4.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCol4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 100, 30));

            lblTax29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblTax29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblTax29.setText("3");
            lblTax29.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblTax29.setFocusCycleRoot(true);
            lblTax29.setOpaque(true);
            lblTax29.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblTax29.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblTax29.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblTax29, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 100, 30));

            lblCol2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCol2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCol2.setText("2");
            lblCol2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCol2.setFocusCycleRoot(true);
            lblCol2.setOpaque(true);
            lblCol2.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCol2.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCol2.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCol2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 100, 30));

            lblTechnology.setFont(new java.awt.Font("MS PGothic", 1, 12)); // NOI18N
            lblTechnology.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblTechnology.setText("技術売上");
            lblTechnology.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblTechnology.setFocusCycleRoot(true);
            lblTechnology.setOpaque(true);
            lblTechnology.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblTechnology.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());

            lblTechnology.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblTechnology, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 180, 30));

            lblRow5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblRow5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblRow5.setText("5");
            lblRow5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblRow5.setFocusCycleRoot(true);
            lblRow5.setOpaque(true);
            lblRow5.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblRow5.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblRow5.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblRow5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 100, 30));

            lblRow4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblRow4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblRow4.setText("4");
            lblRow4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblRow4.setFocusCycleRoot(true);
            lblRow4.setOpaque(true);
            lblRow4.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblRow4.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblRow4.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblRow4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 100, 30));

            lblRow3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblRow3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblRow3.setText("3");
            lblRow3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblRow3.setFocusCycleRoot(true);
            lblRow3.setOpaque(true);
            lblRow3.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblRow3.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblRow3.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblRow3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 100, 30));

            lblRow2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblRow2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblRow2.setText("2");
            lblRow2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblRow2.setFocusCycleRoot(true);
            lblRow2.setOpaque(true);
            lblRow2.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblRow2.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblRow2.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblRow2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 100, 30));

            lblRow1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblRow1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblRow1.setText("1");
            lblRow1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblRow1.setFocusCycleRoot(true);
            lblRow1.setOpaque(true);
            lblRow1.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblRow1.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblRow1.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblRow1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 100, 30));

            lblTax37.setFont(new java.awt.Font("MS PGothic", 1, 12)); // NOI18N
            lblTax37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblTax37.setText("売上");
            lblTax37.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblTax37.setFocusCycleRoot(true);
            lblTax37.setOpaque(true);
            lblTax37.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblTax37.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblTax37.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblTax37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 180, 30));

            lblRankAdvancedSetting.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
            lblRankAdvancedSetting.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblRankAdvancedSetting.setText("A");
            lblRankAdvancedSetting.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblRankAdvancedSetting.setFocusCycleRoot(true);
            lblRankAdvancedSetting.setOpaque(true);
            lblRankAdvancedSetting.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblRankAdvancedSetting.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //       lblTax26.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblRankAdvancedSetting.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblRankAdvancedSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 80, 60));

            lblBuy.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
            lblBuy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblBuy.setText("買っている");
            lblBuy.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblBuy.setFocusCycleRoot(true);
            lblBuy.setOpaque(true);
            lblBuy.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblBuy.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblBuy.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 350, 100, 30));

            lblNotBuy.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
            lblNotBuy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblNotBuy.setText("買っていない");
            lblNotBuy.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblNotBuy.setFocusCycleRoot(true);
            lblNotBuy.setOpaque(true);
            lblNotBuy.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblNotBuy.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblNotBuy.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblNotBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 380, 100, 30));

            lblCo5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCo5.setText("5");
            lblCo5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCo5.setFocusCycleRoot(true);
            lblCo5.setOpaque(true);
            lblCo5.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCo5.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCo5.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 100, 30));

            lblCo4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCo4.setText("4");
            lblCo4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCo4.setFocusCycleRoot(true);
            lblCo4.setOpaque(true);
            lblCo4.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCo4.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCo4.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, 100, 30));

            lblCo3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCo3.setText("3");
            lblCo3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCo3.setFocusCycleRoot(true);
            lblCo3.setOpaque(true);
            lblCo3.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCo3.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCo3.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 320, 100, 30));

            lblCo2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCo2.setText("2");
            lblCo2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCo2.setFocusCycleRoot(true);
            lblCo2.setOpaque(true);
            lblCo2.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCo2.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCo2.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 100, 30));

            lblCo1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblCo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblCo1.setText("1");
            lblCo1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            lblCo1.setFocusCycleRoot(true);
            lblCo1.setOpaque(true);
            lblCo1.setBounds(0, 0, 102, SystemInfo.TABLE_ROW_HEIGHT * 3);
            lblCo1.setBackground(SystemInfo.getTableHeaderRenderer().getBackground());
            //        lblCol5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            //        lblStaffHeader.setOpaque(true);
            //		lblStaffHeader.setText("スタッフ名");
            lblCo1.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblCo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 320, 100, 30));

            tbl2.setBackground(new java.awt.Color(240, 240, 240));
            tbl2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            tbl2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                },
                new String [] {
                }) {
                    public Class getColumnClass(int columnIndex) {
                        return Object.class;
                    }
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return true;
                    }
                });
                tbl2.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                tbl2.setSelectionBackground(new java.awt.Color(255, 210, 142));
                tbl2.setSelectionForeground(new java.awt.Color(0, 0, 0));
                tbl2.setRowHeight(TABLE_ROW_HEIGHT);
                add(tbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, 500, 60));

                lblCategory.setText("業態");
                add(lblCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 25));

                lblTable1.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
                lblTable1.setText("＜表１＞");
                add(lblTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 20));

                btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
                btnClose.setBorderPainted(false);
                btnClose.setContentAreaFilled(false);
                btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
                btnClose.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnCloseActionPerformed(evt);
                    }
                });
                add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 92, 25));
            }// </editor-fold>//GEN-END:initComponents

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
         
        btnShow.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if(this.categoryCbx.getSelectedItem().equals("") )
            {
                this.shopCategoryId = 0;
            }
            else
            {
                MstShopCategory shopCategory;
                shopCategory = (MstShopCategory)this.categoryCbx.getSelectedItem();

                this.shopCategoryId = shopCategory.getShopCategoryId();
            }
            this.showData();
            btnRegist.setEnabled(true);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnShowActionPerformed

    private void btnRegistActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRegistActionPerformed
    {//GEN-HEADEREND:event_btnRegistActionPerformed
        if(!checkselected())
        {
            return;
        }
        btnRegist.setCursor(null);
        
        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (regist( ))
            {
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
                    SystemInfo.getLogger().log(Level.INFO, this.getTitle() + "：" + MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS));
            }
            else
            {
                MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ランク設定"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                    SystemInfo.getLogger().log(Level.INFO, this.getTitle() + "：" + MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ランク設定"));
            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }
    private boolean checkselected()
    {
        //check combobox table 1
        for( int i = 0;i < tbl1.getRowCount();i ++ )
        {
            for( int j = 0; j < tbl1.getColumnCount(); j ++)
            {
                if(((MstRank)( (JComboBox)this.tbl1.getValueAt(i, j) ).getSelectedItem()).getRankName().equals(""))
                {
                    MessageDialog.showMessageDialog(this,
                    "設定されていない項目があります。\n全ての項目に設定を行ってください。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        //check combobox table 2
        for( int i = 0;i < tbl2.getRowCount();i ++ )
        {
            for( int j = 0; j < tbl2.getColumnCount(); j ++)
            {
                if(((MstRank)( (JComboBox)this.tbl2.getValueAt(i, j) ).getSelectedItem()).getRankName().equals(""))
                {
                    MessageDialog.showMessageDialog(this,
                     "設定されていない項目があります。\n全ての項目に設定を行ってください。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
    private boolean regist()
    {
        if( ( (JComboBox)this.tbl1.getValueAt(0, 0) ).getSelectedItem() == null)
            return false;
        ConnectionWrapper con;

        con	= SystemInfo.getConnection();
        
        try
        {
            con.begin();
            
            MstRankSetting mstRanksetting = new MstRankSetting();
            
            mstRanksetting.setShopCategoryId(this.shopCategoryId);
            mstRanksetting.setX5y5(((MstRank)( (JComboBox)this.tbl1.getValueAt(0, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setX4y5(((MstRank)( (JComboBox)this.tbl1.getValueAt(0, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setX3y5(((MstRank)( (JComboBox)this.tbl1.getValueAt(0, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setX2y5(((MstRank)( (JComboBox)this.tbl1.getValueAt(0, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setX1y5(((MstRank)( (JComboBox)this.tbl1.getValueAt(0, 4) ).getSelectedItem()).getRankId());
            
            mstRanksetting.setX5y4(((MstRank)( (JComboBox)this.tbl1.getValueAt(1, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setX4y4(((MstRank)( (JComboBox)this.tbl1.getValueAt(1, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setX3y4(((MstRank)( (JComboBox)this.tbl1.getValueAt(1, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setX2y4(((MstRank)( (JComboBox)this.tbl1.getValueAt(1, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setX1y4(((MstRank)( (JComboBox)this.tbl1.getValueAt(1, 4) ).getSelectedItem()).getRankId());
            
            mstRanksetting.setX5y3(((MstRank)( (JComboBox)this.tbl1.getValueAt(2, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setX4y3(((MstRank)( (JComboBox)this.tbl1.getValueAt(2, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setX3y3(((MstRank)( (JComboBox)this.tbl1.getValueAt(2, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setX2y3(((MstRank)( (JComboBox)this.tbl1.getValueAt(2, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setX1y3(((MstRank)( (JComboBox)this.tbl1.getValueAt(2, 4) ).getSelectedItem()).getRankId());
            
            mstRanksetting.setX5y2(((MstRank)( (JComboBox)this.tbl1.getValueAt(3, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setX4y2(((MstRank)( (JComboBox)this.tbl1.getValueAt(3, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setX3y2(((MstRank)( (JComboBox)this.tbl1.getValueAt(3, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setX2y2(((MstRank)( (JComboBox)this.tbl1.getValueAt(3, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setX1y2(((MstRank)( (JComboBox)this.tbl1.getValueAt(3, 4) ).getSelectedItem()).getRankId());
            
            mstRanksetting.setX5y1(((MstRank)( (JComboBox)this.tbl1.getValueAt(4, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setX4y1(((MstRank)( (JComboBox)this.tbl1.getValueAt(4, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setX3y1(((MstRank)( (JComboBox)this.tbl1.getValueAt(4, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setX2y1(((MstRank)( (JComboBox)this.tbl1.getValueAt(4, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setX1y1(((MstRank)( (JComboBox)this.tbl1.getValueAt(4, 4) ).getSelectedItem()).getRankId());
            
            mstRanksetting.setItem_x5y2(((MstRank)( (JComboBox)this.tbl2.getValueAt(0, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x4y2(((MstRank)( (JComboBox)this.tbl2.getValueAt(0, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x3y2(((MstRank)( (JComboBox)this.tbl2.getValueAt(0, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x2y2(((MstRank)( (JComboBox)this.tbl2.getValueAt(0, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x1y2(((MstRank)( (JComboBox)this.tbl2.getValueAt(0, 4) ).getSelectedItem()).getRankId());
            
            mstRanksetting.setItem_x5y1(((MstRank)( (JComboBox)this.tbl2.getValueAt(1, 0) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x4y1(((MstRank)( (JComboBox)this.tbl2.getValueAt(1, 1) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x3y1(((MstRank)( (JComboBox)this.tbl2.getValueAt(1, 2) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x2y1(((MstRank)( (JComboBox)this.tbl2.getValueAt(1, 3) ).getSelectedItem()).getRankId());
            mstRanksetting.setItem_x1y1(((MstRank)( (JComboBox)this.tbl2.getValueAt(1, 4) ).getSelectedItem()).getRankId());
        
            mstRanksetting.regist(con);
            con.commit();
        }
        catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            
            try
            {
                if (con.isBeginTran())
                {
                    con.rollback();
                }
                
            }
            catch (SQLException sql_e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            return false;
        }

        return true;
    }//GEN-LAST:event_btnRegistActionPerformed

    private void categoryCbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryCbxActionPerformed
        btnRegist.setEnabled(false);
    }//GEN-LAST:event_categoryCbxActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    // <editor-fold defaultstate="collapsed" desc=" Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRegist;
    private javax.swing.ButtonGroup btnReportType;
    private javax.swing.JButton btnShow;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel categoryCbx;
    private javax.swing.JLabel lblBuy;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblCategory1;
    private javax.swing.JLabel lblCo1;
    private javax.swing.JLabel lblCo2;
    private javax.swing.JLabel lblCo3;
    private javax.swing.JLabel lblCo4;
    private javax.swing.JLabel lblCo5;
    private javax.swing.JLabel lblCol1;
    private javax.swing.JLabel lblCol2;
    private javax.swing.JLabel lblCol4;
    private javax.swing.JLabel lblCol5;
    private javax.swing.JLabel lblNotBuy;
    private javax.swing.JLabel lblRankAdvancedSetting;
    private javax.swing.JLabel lblRow1;
    private javax.swing.JLabel lblRow2;
    private javax.swing.JLabel lblRow3;
    private javax.swing.JLabel lblRow4;
    private javax.swing.JLabel lblRow5;
    private javax.swing.JLabel lblTable1;
    private javax.swing.JLabel lblTax26;
    private javax.swing.JLabel lblTax29;
    private javax.swing.JLabel lblTax37;
    private javax.swing.JLabel lblTechnology;
    private com.geobeck.swing.JTableEx tbl1;
    private com.geobeck.swing.JTableEx tbl2;
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
            controls.add(categoryCbx);
            
            for( Component control : controls ){
                control.addKeyListener(SystemInfo.getMoveNextField());
                control.addFocusListener(SystemInfo.getSelectText());
            }
            
            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(categoryCbx);
        };
            
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                                           Component aComponent)
        {

            if(aComponent.equals(categoryCbx)){
                return btnShow;
            }else if(aComponent.equals(btnShow))
            {
                return btnRegist;
            }
            else if(aComponent.equals(btnRegist))
            {
                return categoryCbx;
            }
            return this.getStartComponent();
        }

        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         */
        public Component getComponentBefore(Container aContainer,
                                            Component aComponent)
        {
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
            return categoryCbx;
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer)
        {
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
            if(categoryCbx.getItemCount() ==1)
            {
                return categoryCbx;
            }
            return btnShow;
        }
    }
    
}

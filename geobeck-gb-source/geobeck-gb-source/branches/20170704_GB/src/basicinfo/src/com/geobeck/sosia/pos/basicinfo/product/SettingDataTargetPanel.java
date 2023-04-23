/*
 * SettingDataTargetPanel.java
 *
 * Created on 2014/10/05
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.hair.master.product.DataTargetTechnic;
import com.geobeck.sosia.pos.hair.product.MinusCellRedRenderer;
import com.geobeck.sosia.pos.hair.report.CustomerRanking;
import com.geobeck.sosia.pos.hair.report.CustomerRankingList;
import com.geobeck.sosia.pos.master.customer.MstFirstComingMotive;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.util.CheckUtil;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

/**
 *
 * @author lvtu
 */
public class SettingDataTargetPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private Integer shopId          = null;
    private String mstShopName      = "";
    private String Date             = "";
    private Integer month           = null;
    private Integer periodMonth     = null;
    private Integer year            = null;
    private Integer shopCategory    = null;
    private DataTargets dts;
    private Integer selIndex        = -1;
    private Integer countRow        = 0;
    private Integer countCol        = 0;
    private Integer countCol1       = 0;
    List<MstFirstComingMotive> arrmfcm = new ArrayList<MstFirstComingMotive>();

    int step = 0;
    int flag = 0;
    int flagNextPage = 0;
    
    private SettingDataTargetFocusTraversalPolicy ftp =
            new SettingDataTargetFocusTraversalPolicy();

    /**
     * ãZèpÉ}ÉXÉ^ìoò^âÊñ ópFocusTraversalPolicyÇéÊìæÇ∑ÇÈÅB
     *
     * @return ãZèpÉ}ÉXÉ^ìoò^âÊñ ópFocusTraversalPolicy
     */
    public SettingDataTargetFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    /**
     * Creates new form SettingDataTargetPanel
     */
    public SettingDataTargetPanel() {
    }

    //IVS_nahoang GB_Mashu_ñ⁄ïWê›íË Start add 20141023
    private String categoryName = "";

    public SettingDataTargetPanel(Integer periodMonth, Integer year, Integer shopCategory, String categoryName, Integer mstShopId, String mstShopName) {
        super();
        initComponents();
        //IVS_LVTu start edit 2014/11/28 Mashu_ê›íËâÊñ 
        //this.setSize(1024, 750);
        this.setSize(1016, 705);
        //IVS_LVTu start edit 2014/11/28 Mashu_ê›íËâÊñ 
        this.periodMonth = periodMonth;
        this.year = year;
        this.shopCategory = shopCategory;
        this.categoryName = categoryName;
        this.shopId = mstShopId;
        this.mstShopName = mstShopName;
        //IVS_LVTu start add 2014/10/23 Mashu_ê›íËâÊñ 
        lbShopValue.setText(mstShopName);
        String strDate = null;
        //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        String  temp = "0" +(periodMonth +1) ;
        String  temp1 = "0" +(periodMonth) ;
        if (this.periodMonth != 12) {
            this.Date = this.year + "-" + temp.substring(temp.length()-2) + "-01";
            this.month = periodMonth + 1;
            strDate = this.year + "îNìxÅi" + this.year + "/" +  temp.substring(temp.length()-2) +"Å`" + (this.year + 1) + "/" + temp1.substring(temp1.length()-2) +"Åj";
        //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        } else {
            this.Date = this.year + "-01-01";
            this.month = 1;
            strDate = this.year + "îNìxÅi" + this.year + "/01Å`" + this.year + "/12Åj";
        }
        lbDateValue.setText(strDate);
        lbShopCategoryvalue.setText(this.categoryName);
        addMouseCursorChange();
        btBack.setEnabled(false);
        //PanelRegist.setVisible(false);
        btRegist.setEnabled(false);
        //IVS_LVTu end add 2014/10/23 Mashu_ê›íËâÊñ 
        setListener();
        this.init();

    }

    //IVS_nahoang GB_Mashu_ñ⁄ïWê›íË End add
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(Integer shopCategory) {
        this.shopCategory = shopCategory;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelTop = new javax.swing.JPanel();
        lbshop = new javax.swing.JLabel();
        lbDate = new javax.swing.JLabel();
        btBack = new javax.swing.JButton();
        btNext = new javax.swing.JButton();
        lbShopCategory = new javax.swing.JLabel();
        lbShopCategoryvalue = new javax.swing.JLabel();
        lbShopValue = new javax.swing.JLabel();
        lbDateValue = new javax.swing.JLabel();
        btRegist = new javax.swing.JButton();
        PanelPage1 = new javax.swing.JPanel();
        itemClassContractedNameLabel1 = new javax.swing.JLabel();
        itemClassContractedNameLabel3 = new javax.swing.JLabel();
        itemClassContractedNameLabel5 = new javax.swing.JLabel();
        itemClassContractedNameLabel6 = new javax.swing.JLabel();
        itemClassContractedNameLabel11 = new javax.swing.JLabel();
        itemClassContractedNameLabel10 = new javax.swing.JLabel();
        itemClassContractedNameLabel2 = new javax.swing.JLabel();
        itemClassContractedNameLabel9 = new javax.swing.JLabel();
        paneTable1 = new javax.swing.JScrollPane();
        table_1 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel52 = new javax.swing.JLabel();
        itemClassContractedNameLabel7 = new javax.swing.JLabel();
        itemClassContractedNameLabel12 = new javax.swing.JLabel();
        itemClassContractedNameLabel21 = new javax.swing.JLabel();
        itemClassContractedNameLabel22 = new javax.swing.JLabel();
        itemClassContractedNameLabel17 = new javax.swing.JLabel();
        itemClassContractedNameLabel18 = new javax.swing.JLabel();
        itemClassContractedNameLabel20 = new javax.swing.JLabel();
        itemClassContractedNameLabel14 = new javax.swing.JLabel();
        itemClassContractedNameLabel13 = new javax.swing.JLabel();
        itemClassContractedNameLabel15 = new javax.swing.JLabel();
        paneTable4 = new javax.swing.JScrollPane();
        table_4 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel53 = new javax.swing.JLabel();
        paneTable2 = new javax.swing.JScrollPane();
        table_2 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel23 = new javax.swing.JLabel();
        itemClassContractedNameLabel24 = new javax.swing.JLabel();
        itemClassContractedNameLabel26 = new javax.swing.JLabel();
        panetable3 = new javax.swing.JScrollPane();
        table_3 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel51 = new javax.swing.JLabel();
        itemClassContractedNameLabel28 = new javax.swing.JLabel();
        itemClassContractedNameLabel54 = new javax.swing.JLabel();
        itemClassContractedNameLabel25 = new javax.swing.JLabel();
        itemClassContractedNameLabel16 = new javax.swing.JLabel();
        paneTable5 = new javax.swing.JScrollPane();
        table_5 = new com.geobeck.swing.JTableEx();
        txttechnic = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txttechnic.getDocument()).setDocumentFilter(
            new CustomFilter(9, CustomFilter.NUMBER));
        txtaverage = new JFormattedTextFieldEx(SystemInfo.getDecimalFormatter());
        txtSaleTarget = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtSaleTarget.getDocument()).setDocumentFilter(
            new CustomFilter(9, CustomFilter.NUMBER));
        txtaverage1 = new JFormattedTextFieldEx(SystemInfo.getDecimalFormatter());
        itemClassContractedNameLabel57 = new javax.swing.JLabel();
        panetable4 = new javax.swing.JScrollPane();
        tbTargetItem = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel70 = new javax.swing.JLabel();
        itemClassContractedNameLabel71 = new javax.swing.JLabel();
        PanelPage2 = new javax.swing.JPanel();
        itemClassContractedNameLabel45 = new javax.swing.JLabel();
        itemClassContractedNameLabel46 = new javax.swing.JLabel();
        itemClassContractedNameLabel59 = new javax.swing.JLabel();
        paneTable8 = new javax.swing.JScrollPane();
        table_8 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel61 = new javax.swing.JLabel();
        itemClassContractedNameLabel63 = new javax.swing.JLabel();
        itemClassContractedNameLabel72 = new javax.swing.JLabel();
        paneTable10 = new javax.swing.JScrollPane();
        table_10 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel73 = new javax.swing.JLabel();
        paneTable9 = new javax.swing.JScrollPane();
        table_9 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel77 = new javax.swing.JLabel();
        paneTable6 = new javax.swing.JScrollPane();
        table_6 = new com.geobeck.swing.JTableEx();
        paneTable_7 = new javax.swing.JScrollPane();
        table_7 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel38 = new javax.swing.JLabel();
        itemClassContractedNameLabel37 = new javax.swing.JLabel();
        itemClassContractedNameLabel41 = new javax.swing.JLabel();
        itemClassContractedNameLabel40 = new javax.swing.JLabel();
        paneTable_12 = new javax.swing.JScrollPane();
        table_12 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel47 = new javax.swing.JLabel();
        paneTable11 = new javax.swing.JScrollPane();
        table_11 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel58 = new javax.swing.JLabel();
        itemClassContractedNameLabel62 = new javax.swing.JLabel();
        itemClassContractedNameLabel65 = new javax.swing.JLabel();
        itemClassContractedNameLabel66 = new javax.swing.JLabel();
        paneTable11_sum = new javax.swing.JScrollPane();
        table_11_sum = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel74 = new javax.swing.JLabel();
        tbKarte = new com.geobeck.swing.JTableEx();
        PanelPage3 = new javax.swing.JPanel();
        itemClassContractedNameLabel48 = new javax.swing.JLabel();
        itemClassContractedNameLabel64 = new javax.swing.JLabel();
        paneTable15 = new javax.swing.JScrollPane();
        table_15 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel75 = new javax.swing.JLabel();
        paneTable14 = new javax.swing.JScrollPane();
        Table_14 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel78 = new javax.swing.JLabel();
        paneTable13 = new javax.swing.JScrollPane();
        table_13 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel43 = new javax.swing.JLabel();
        paneTable17 = new javax.swing.JScrollPane();
        table_17 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel50 = new javax.swing.JLabel();
        paneTable16 = new javax.swing.JScrollPane();
        table_16 = new com.geobeck.swing.JTableEx();
        paneTable18 = new javax.swing.JScrollPane();
        table_18 = new com.geobeck.swing.JTableEx();
        itemClassContractedNameLabel55 = new javax.swing.JLabel();
        itemClassContractedNameLabel56 = new javax.swing.JLabel();
        itemClassContractedNameLabel60 = new javax.swing.JLabel();
        itemClassContractedNameLabel39 = new javax.swing.JLabel();
        itemClassContractedNameLabel67 = new javax.swing.JLabel();
        itemClassContractedNameLabel68 = new javax.swing.JLabel();
        itemClassContractedNameLabel69 = new javax.swing.JLabel();
        paneTable18_sum = new javax.swing.JScrollPane();
        table_18_sum = new com.geobeck.swing.JTableEx();

        setFocusCycleRoot(true);
        setPreferredSize(new java.awt.Dimension(1010, 2500));

        PanelTop.setPreferredSize(new java.awt.Dimension(1000, 68));

        lbshop.setText("ìXï‹");

        lbDate.setText("ëŒè€");

        btBack.setIcon(SystemInfo.getImageIcon("/button/common/back_small_off.jpg"));
        btBack.setBorderPainted(false);
        btBack.setContentAreaFilled(false);
        btBack.setPressedIcon(SystemInfo.getImageIcon("/button/common/back_small_on.jpg"));
        btBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBackActionPerformed(evt);
            }
        });

        btNext.setIcon(SystemInfo.getImageIcon("/button/common/next_off.jpg"));
        btNext.setBorderPainted(false);
        btNext.setContentAreaFilled(false);
        btNext.setPressedIcon(SystemInfo.getImageIcon("/button/common/next_on.jpg"));
        btNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextActionPerformed(evt);
            }
        });

        lbShopCategory.setText("ã∆ë‘");

        lbShopCategoryvalue.setText("   ");

        lbShopValue.setText("    ");

        lbDateValue.setText("     ");

        btRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btRegist.setBorderPainted(false);
        btRegist.setContentAreaFilled(false);
        btRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout PanelTopLayout = new org.jdesktop.layout.GroupLayout(PanelTop);
        PanelTop.setLayout(PanelTopLayout);
        PanelTopLayout.setHorizontalGroup(
            PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PanelTopLayout.createSequentialGroup()
                .addContainerGap()
                .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lbDate)
                    .add(lbshop)
                    .add(lbShopCategory))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PanelTopLayout.createSequentialGroup()
                        .add(lbShopCategoryvalue)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(PanelTopLayout.createSequentialGroup()
                        .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lbShopValue)
                            .add(lbDateValue))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(btBack, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(btNext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(btRegist, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelTopLayout.setVerticalGroup(
            PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PanelTopLayout.createSequentialGroup()
                .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PanelTopLayout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(PanelTopLayout.createSequentialGroup()
                                .add(lbShopValue)
                                .add(2, 2, 2)
                                .add(lbDateValue))
                            .add(PanelTopLayout.createSequentialGroup()
                                .add(lbshop)
                                .add(3, 3, 3)
                                .add(lbDate)
                                .add(3, 3, 3)
                                .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(lbShopCategory)
                                    .add(lbShopCategoryvalue)))))
                    .add(PanelTopLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(PanelTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btNext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btBack, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btRegist, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelPage1.setPreferredSize(new java.awt.Dimension(1000, 750));

        itemClassContractedNameLabel1.setText("<html><b>ÅÉãZèpîÑè„ñ⁄ïWê›íËÅÑ</b></html>");

        itemClassContractedNameLabel3.setText("ãZèpîÑè„ñ⁄ïWÇì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB");

        itemClassContractedNameLabel5.setText("â~");

        itemClassContractedNameLabel6.setText("ÅÀåéïΩãœ");

        itemClassContractedNameLabel11.setText("ãZîÑñ⁄à¿ÇéQçlÇ…ãZîÑåéñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB");

        itemClassContractedNameLabel10.setText("åéäiç∑ÇéQçlÇ…ÇµÇƒñàåéÇäÑÇËêUÇÈÇ∆â∫ãLÇÃÇÊÇ§Ç…Ç»ÇËÇ‹Ç∑ÅB");

        itemClassContractedNameLabel2.setText("á@");

        itemClassContractedNameLabel9.setText("áA");

        paneTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        table_1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé", "îNä‘", "ç∑äz"
            }) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    switch(rowIndex)
                    {
                        case 3:
                        if(0 < columnIndex && columnIndex < 13 )
                        {
                            return true;
                        }
                        break;
                        default:
                        break;
                    }
                    return false;
                }
            });
            table_1.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
            table_1.setSelectionBackground(new java.awt.Color(255, 210, 142));
            table_1.setSelectionForeground(new java.awt.Color(0, 0, 0));
            table_1.getTableHeader().setReorderingAllowed(false);
            SwingUtil.setJTableHeaderRenderer(table_1, SystemInfo.getTableHeaderRenderer());
            table_1.setRowHeight(20);

            TableColumnModel tableModel = table_1.getColumnModel();
            //tableModel.getColumn(0).setCellRenderer(new EditabeTableCellRenderer(String.class, "0", 1));
            tableModel.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(4).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(5).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(6).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(7).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(8).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(9).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(10).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(11).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(12).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
            tableModel.getColumn(14).setCellRenderer(new MinusCellRedRenderer());

            //SelectTableCellRenderer.setSelectTableCellRenderer(table_1);
            table_1.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    table_1FocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    table_1FocusLost(evt);
                }
            });
            table_1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    table_1PropertyChange(evt);
                }
            });
            paneTable1.setViewportView(table_1);
            table_1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            itemClassContractedNameLabel52.setText("Å¶ç∑äzóìÇ™0â~à»è„Ç…Ç»ÇÍÇŒOKÇ≈Ç∑ÅB");

            itemClassContractedNameLabel7.setText("â~");

            itemClassContractedNameLabel12.setText("<html><b>ÅÉìXîÃîÑè„ñ⁄ïWê›íËÅÑ</b></html>");

            itemClassContractedNameLabel21.setText("ìXîÃîÑè„ñ⁄ïWÇì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB");

            itemClassContractedNameLabel22.setText("á@");

            itemClassContractedNameLabel17.setText("â~");

            itemClassContractedNameLabel18.setText("ÅÀåéïΩãœ");

            itemClassContractedNameLabel20.setText("â~");

            itemClassContractedNameLabel14.setText("åéäiç∑ÇéQçlÇ…ÇµÇƒñàåéÇäÑÇËêUÇÈÇ∆â∫ãLÇÃÇÊÇ§Ç…Ç»ÇËÇ‹Ç∑");

            itemClassContractedNameLabel13.setText("ìXîÃñ⁄à¿ÇéQçlÇ…ãZîÑåéñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

            itemClassContractedNameLabel15.setText("áA");

            paneTable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            table_4.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                },
                new String [] {
                    "", "ãZèpãqíPâø", "èóê´íPâø", "íjê´íPâø"
                }) {
                    Class[] types = new Class [] {
                        String.class,
                        Integer.class,
                        Integer.class,
                        Integer.class,
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false
                    };
                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        switch(rowIndex)
                        {
                            case 1:
                            if( 1< columnIndex && columnIndex < 4 )
                            {
                                return true;
                            }
                            default:
                            break;
                        }
                        return false;
                    }
                });
                table_4.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                table_4.setSelectionBackground(new java.awt.Color(255, 210, 142));
                table_4.setSelectionForeground(new java.awt.Color(0, 0, 0));
                table_4.getTableHeader().setReorderingAllowed(false);
                SwingUtil.setJTableHeaderRenderer(table_4, SystemInfo.getTableHeaderRenderer());
                table_4.setRowHeight(20);
                //SelectTableCellRenderer.setSelectTableCellRenderer(table_4);

                TableColumnModel tableModel4 = table_4.getColumnModel();
                //tableModel4.getColumn(1).setCellEditor(new IntegerCellEditor(new JTextField()));
                tableModel4.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                tableModel4.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                table_4.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        table_4FocusGained(evt);
                    }
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        table_4FocusLost(evt);
                    }
                });
                table_4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        table_4PropertyChange(evt);
                    }
                });
                paneTable4.setViewportView(table_4);
                table_4.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                itemClassContractedNameLabel53.setText("Å¶ç∑äzóìÇ™0â~à»è„Ç…Ç»ÇÍÇŒOKÇ≈Ç∑ÅB");

                paneTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                table_2.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {},
                    new String [] {
                        "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé", "îNä‘", "ç∑äz"
                    }) {
                        Class[] types = new Class [] {
                            String.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class,
                            Integer.class};
                        boolean[] canEdit = new boolean [] {
                            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
                        };
                        public Class getColumnClass(int columnIndex) {
                            return types [columnIndex];
                        }
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            switch(rowIndex)
                            {
                                case 3:
                                if(0 < columnIndex && columnIndex < 13 )
                                {
                                    return true;
                                }
                                break;
                                default:
                                break;
                            }
                            return false;
                        }
                    });
                    table_2.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                    table_2.setSelectionBackground(new java.awt.Color(255, 210, 142));
                    table_2.setSelectionForeground(new java.awt.Color(0, 0, 0));
                    table_2.getTableHeader().setReorderingAllowed(false);
                    SwingUtil.setJTableHeaderRenderer(table_2, SystemInfo.getTableHeaderRenderer());
                    table_2.setRowHeight(20);

                    TableColumnModel tableModel2 = table_2.getColumnModel();
                    tableModel2.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(4).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(5).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(6).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(7).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(8).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(9).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(10).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(11).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(12).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                    tableModel2.getColumn(14).setCellRenderer(new MinusCellRedRenderer());
                    table_2.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            table_2FocusGained(evt);
                        }
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            table_2FocusLost(evt);
                        }
                    });
                    table_2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                            table_2PropertyChange(evt);
                        }
                    });
                    paneTable2.setViewportView(table_2);
                    table_2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                    itemClassContractedNameLabel23.setText("<html><b>ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ</b></html>");

                    itemClassContractedNameLabel24.setText("ëÂÇ‹Ç©Ç»èóê´ãqêîî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

                    itemClassContractedNameLabel26.setText("ëÂÇ‹Ç©Ç»ãZèpãqíPâøñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

                    panetable3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                    table_3.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {},
                        new String [] {
                            "", "èóê´ãqêîî‰ó¶"
                        }) {
                            Class[] types = new Class [] {
                                String.class,
                                Integer.class
                            };
                            boolean[] canEdit = new boolean [] {
                                false, false
                            };
                            public Class getColumnClass(int columnIndex) {
                                return types [columnIndex];
                            }
                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                switch(rowIndex)
                                {
                                    case 1:
                                    if( columnIndex == 1 )
                                    {
                                        return true;
                                    }
                                    default:
                                    break;
                                }
                                return false;
                            }
                            public void setValueAt(Object value, int rowIndex, int columnIndex)
                            {
                                if (rowIndex == 1 && columnIndex == 1)
                                {
                                    //Object ob = (Object)value.toString().substring(0, 3);
                                    if("".equals(value.toString()))
                                    {
                                        super.setValueAt(0, rowIndex, columnIndex);
                                    }
                                    else
                                    {
                                        value = (Object)Integer.parseInt(value.toString());
                                        super.setValueAt(value, rowIndex, columnIndex);
                                    }
                                }
                            }
                        });
                        table_3.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                        table_3.setSelectionBackground(new java.awt.Color(255, 210, 142));
                        table_3.setSelectionForeground(new java.awt.Color(0, 0, 0));
                        table_3.getTableHeader().setReorderingAllowed(false);
                        SwingUtil.setJTableHeaderRenderer(table_3, SystemInfo.getTableHeaderRenderer());
                        table_3.setRowHeight(20);

                        TableColumnModel tableModel3 = table_3.getColumnModel();
                        tableModel3.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField(), 3, 2));

                        //((PlainDocument)table_3.getCellEditor(1, 1)).setDocumentFilter(
                            //                            new CustomFilter(3, CustomFilter.NUMERIC));
                        table_3.addFocusListener(new java.awt.event.FocusAdapter() {
                            public void focusGained(java.awt.event.FocusEvent evt) {
                                table_3FocusGained(evt);
                            }
                            public void focusLost(java.awt.event.FocusEvent evt) {
                                table_3FocusLost(evt);
                            }
                        });
                        table_3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                table_3PropertyChange(evt);
                            }
                        });
                        panetable3.setViewportView(table_3);
                        table_3.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                        itemClassContractedNameLabel51.setText("Å¶ãZèpãqíPâøÅÅÅièóê´î‰ó¶Å~èóê´íPâøÅjÅ{Åiíjê´î‰ó¶Å~íjê´íPâøÅj");

                        itemClassContractedNameLabel28.setText("<html><b>ÅÉãZèpãqêîñ⁄ïWÅÑ</b></html>");

                        itemClassContractedNameLabel54.setText("Å¶äeåéÇÃãZîÑåéñ⁄ïWÅÄãZèpãqíPâøÇ©ÇÁéZèo");

                        itemClassContractedNameLabel25.setText("á@");

                        itemClassContractedNameLabel16.setText("áA");

                        paneTable5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                        table_5.setModel(new javax.swing.table.DefaultTableModel(
                            new Object [][] {},
                            new String [] {
                                "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé", "îNä‘"
                            }) {
                                Class[] types = new Class [] {
                                    String.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class,
                                    Integer.class};
                                boolean[] canEdit = new boolean [] {
                                    false, false, false, false, false, false, false, false, false, false, false, false, false, false
                                };
                                public Class getColumnClass(int columnIndex) {
                                    return types [columnIndex];
                                }
                                public boolean isCellEditable(int rowIndex, int columnIndex) {
                                    return false;
                                }
                            });
                            table_5.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                            table_5.setSelectionBackground(new java.awt.Color(255, 210, 142));
                            table_5.setSelectionForeground(new java.awt.Color(0, 0, 0));
                            table_5.getTableHeader().setReorderingAllowed(false);
                            SwingUtil.setJTableHeaderRenderer(table_5, SystemInfo.getTableHeaderRenderer());
                            table_5.setRowHeight(20);
                            //SelectTableCellRenderer.setSelectTableCellRenderer(table_5);
                            //TableColumnModel tableModel5 = table_5.getColumnModel();
                            //tableModel5.getColumn(0).setCellRenderer(new EditabeTableCellRenderer(String.class, "0", 1));
                            table_5.addFocusListener(new java.awt.event.FocusAdapter() {
                                public void focusGained(java.awt.event.FocusEvent evt) {
                                    table_5FocusGained(evt);
                                }
                                public void focusLost(java.awt.event.FocusEvent evt) {
                                    table_5FocusLost(evt);
                                }
                            });
                            paneTable5.setViewportView(table_5);
                            table_5.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                            txttechnic.setBackground(new java.awt.Color(255, 210, 142));
                            txttechnic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                            txttechnic.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                            txttechnic.addCaretListener(new javax.swing.event.CaretListener() {
                                public void caretUpdate(javax.swing.event.CaretEvent evt) {
                                    txttechnicCaretUpdate(evt);
                                }
                            });

                            txtaverage.setEditable(false);
                            txtaverage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                            txtaverage.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

                            txtSaleTarget.setBackground(new java.awt.Color(255, 210, 142));
                            txtSaleTarget.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                            txtSaleTarget.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                            txtSaleTarget.addCaretListener(new javax.swing.event.CaretListener() {
                                public void caretUpdate(javax.swing.event.CaretEvent evt) {
                                    txtSaleTargetCaretUpdate(evt);
                                }
                            });

                            txtaverage1.setEditable(false);
                            txtaverage1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                            txtaverage1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

                            itemClassContractedNameLabel57.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                            panetable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                            tbTargetItem.setModel(new javax.swing.table.DefaultTableModel(
                                new Object [][] {},
                                new String [] {
                                    "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé"
                                }) {
                                    Class[] types = new Class [] {
                                        String.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class,
                                        Integer.class};
                                    boolean[] canEdit = new boolean [] {
                                        false, false, false, false, false, false, false, false, false, false, false, false, false
                                    };
                                    public Class getColumnClass(int columnIndex) {
                                        return types [columnIndex];
                                    }
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {

                                        if( columnIndex >= 1 )
                                        {
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                TableColumnModel tbTargetItemModel = tbTargetItem.getColumnModel();
                                tbTargetItemModel.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(4).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(5).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(6).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(7).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(8).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(9).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(10).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(11).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItemModel.getColumn(12).setCellEditor(new CustomCellEditor(new JTextField() , 9, 3));
                                tbTargetItem.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                tbTargetItem.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                tbTargetItem.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                tbTargetItem.getTableHeader().setReorderingAllowed(false);
                                SwingUtil.setJTableHeaderRenderer(tbTargetItem, SystemInfo.getTableHeaderRenderer());
                                tbTargetItem.setRowHeight(20);

                                //TableColumnModel tbTargetItemModel = tbTargetItem.getColumnModel();
                                //tbTargetItemModel.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField(), 3, 2));
                                //tbTargetItemModel.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField(), 9, 1));
                                // set data cell
                                //((PlainDocument)tbTargetItem.getCellEditor(0, 1)).setDocumentFilter(
                                    //                            new CustomFilter(3, CustomFilter.NUMERIC));
                                //((PlainDocument)tbTargetItem.getCellEditor(1, 1)).setDocumentFilter(
                                    //                            new CustomFilter(9, CustomFilter.NUMERIC));
                                tbTargetItem.addFocusListener(new java.awt.event.FocusAdapter() {
                                    public void focusGained(java.awt.event.FocusEvent evt) {
                                        tbTargetItemFocusGained(evt);
                                    }
                                    public void focusLost(java.awt.event.FocusEvent evt) {
                                        tbTargetItemFocusLost(evt);
                                    }
                                });
                                panetable4.setViewportView(tbTargetItem);

                                itemClassContractedNameLabel70.setText("<html><b>ÅÉìXîÃçwîÉî‰ó¶ÅEíPâøñ⁄ïWê›íËÅÑ </b></html>");

                                itemClassContractedNameLabel71.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                org.jdesktop.layout.GroupLayout PanelPage1Layout = new org.jdesktop.layout.GroupLayout(PanelPage1);
                                PanelPage1.setLayout(PanelPage1Layout);
                                PanelPage1Layout.setHorizontalGroup(
                                    PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(PanelPage1Layout.createSequentialGroup()
                                        .add(27, 27, 27)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(PanelPage1Layout.createSequentialGroup()
                                                .add(paneTable1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addContainerGap())
                                            .add(PanelPage1Layout.createSequentialGroup()
                                                .add(itemClassContractedNameLabel52)
                                                .add(0, 0, Short.MAX_VALUE))))
                                    .add(PanelPage1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(PanelPage1Layout.createSequentialGroup()
                                                .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(PanelPage1Layout.createSequentialGroup()
                                                        .add(itemClassContractedNameLabel25)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(itemClassContractedNameLabel57)
                                                            .add(PanelPage1Layout.createSequentialGroup()
                                                                .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                    .add(itemClassContractedNameLabel24)
                                                                    .add(panetable3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                .add(30, 30, 30)
                                                                .add(itemClassContractedNameLabel16)))
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(itemClassContractedNameLabel51)
                                                            .add(itemClassContractedNameLabel26)
                                                            .add(paneTable4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 343, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                                    .add(itemClassContractedNameLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(PanelPage1Layout.createSequentialGroup()
                                                        .add(itemClassContractedNameLabel2)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(itemClassContractedNameLabel3)
                                                            .add(PanelPage1Layout.createSequentialGroup()
                                                                .add(txttechnic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(itemClassContractedNameLabel5)
                                                                .add(18, 18, 18)
                                                                .add(itemClassContractedNameLabel6)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(txtaverage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(itemClassContractedNameLabel7))))
                                                    .add(PanelPage1Layout.createSequentialGroup()
                                                        .add(itemClassContractedNameLabel9)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(itemClassContractedNameLabel11)
                                                            .add(itemClassContractedNameLabel10)))
                                                    .add(itemClassContractedNameLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(PanelPage1Layout.createSequentialGroup()
                                                        .add(itemClassContractedNameLabel22)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(PanelPage1Layout.createSequentialGroup()
                                                                .add(txtSaleTarget, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(itemClassContractedNameLabel17)
                                                                .add(18, 18, 18)
                                                                .add(itemClassContractedNameLabel18)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(txtaverage1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(itemClassContractedNameLabel20))
                                                            .add(itemClassContractedNameLabel21)))
                                                    .add(itemClassContractedNameLabel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(0, 0, Short.MAX_VALUE))
                                            .add(PanelPage1Layout.createSequentialGroup()
                                                .add(itemClassContractedNameLabel15)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(paneTable2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                    .add(PanelPage1Layout.createSequentialGroup()
                                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                            .add(itemClassContractedNameLabel13)
                                                            .add(itemClassContractedNameLabel14)
                                                            .add(itemClassContractedNameLabel53)
                                                            .add(panetable4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 835, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                        .add(0, 0, Short.MAX_VALUE)))))
                                        .addContainerGap())
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, PanelPage1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(PanelPage1Layout.createSequentialGroup()
                                                .add(18, 18, 18)
                                                .add(paneTable5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                            .add(PanelPage1Layout.createSequentialGroup()
                                                .add(itemClassContractedNameLabel28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(itemClassContractedNameLabel54)))
                                        .add(69, 69, 69))
                                    .add(PanelPage1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(itemClassContractedNameLabel70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(488, 488, 488)
                                        .add(itemClassContractedNameLabel71)
                                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                                PanelPage1Layout.setVerticalGroup(
                                    PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(PanelPage1Layout.createSequentialGroup()
                                        .add(2, 2, 2)
                                        .add(itemClassContractedNameLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(1, 1, 1)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel2)
                                            .add(itemClassContractedNameLabel3))
                                        .add(0, 0, 0)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel5)
                                            .add(itemClassContractedNameLabel6)
                                            .add(itemClassContractedNameLabel7)
                                            .add(txttechnic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(txtaverage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(2, 2, 2)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel9)
                                            .add(itemClassContractedNameLabel10))
                                        .add(1, 1, 1)
                                        .add(itemClassContractedNameLabel11)
                                        .add(0, 0, 0)
                                        .add(paneTable1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(0, 0, 0)
                                        .add(itemClassContractedNameLabel52)
                                        .add(2, 2, 2)
                                        .add(itemClassContractedNameLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(0, 0, 0)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel22)
                                            .add(itemClassContractedNameLabel21))
                                        .add(0, 0, 0)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel17)
                                            .add(itemClassContractedNameLabel18)
                                            .add(itemClassContractedNameLabel20)
                                            .add(txtSaleTarget, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(txtaverage1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(2, 2, 2)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel14)
                                            .add(itemClassContractedNameLabel15))
                                        .add(0, 0, 0)
                                        .add(itemClassContractedNameLabel13)
                                        .add(0, 0, 0)
                                        .add(paneTable2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(0, 0, 0)
                                        .add(itemClassContractedNameLabel53)
                                        .add(2, 2, 2)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(itemClassContractedNameLabel71))
                                        .add(0, 0, 0)
                                        .add(panetable4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(2, 2, 2)
                                        .add(itemClassContractedNameLabel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(1, 1, 1)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                .add(itemClassContractedNameLabel24)
                                                .add(itemClassContractedNameLabel25)
                                                .add(itemClassContractedNameLabel16))
                                            .add(itemClassContractedNameLabel26))
                                        .add(0, 0, 0)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                            .add(panetable3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                            .add(paneTable4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                                        .add(1, 1, 1)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel51)
                                            .add(itemClassContractedNameLabel57))
                                        .add(2, 2, 2)
                                        .add(PanelPage1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(itemClassContractedNameLabel28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(itemClassContractedNameLabel54))
                                        .add(0, 0, 0)
                                        .add(paneTable5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(129, Short.MAX_VALUE))
                                );

                                PanelPage2.setPreferredSize(new java.awt.Dimension(1000, 750));

                                itemClassContractedNameLabel45.setText("<html><b>ÅÉäeçƒóàó¶ÅAéñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWê›íËÅÑ</b></html>");

                                itemClassContractedNameLabel46.setText("çƒóàó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                itemClassContractedNameLabel59.setText("á@");

                                paneTable8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                table_8.setModel(new javax.swing.table.DefaultTableModel(
                                    new Object [][] {
                                    },
                                    new String [] {
                                        "", "éñëOó\ñÒó¶", "éüâÒó\ñÒó¶"
                                    }) {
                                        Class[] types = new Class [] {
                                            String.class,
                                            Integer.class,
                                            Integer.class,
                                        };
                                        boolean[] canEdit = new boolean [] {
                                            false, false, false
                                        };
                                        public Class getColumnClass(int columnIndex) {
                                            return types [columnIndex];
                                        }
                                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                            switch(rowIndex)
                                            {
                                                case 1:
                                                if(0 < columnIndex && columnIndex < 3 )
                                                {
                                                    return true;
                                                }
                                                break;
                                                default:
                                                break;
                                            }
                                            return false;
                                        }
                                        public void setValueAt(Object value, int rowIndex, int columnIndex)
                                        {
                                            if (rowIndex == 1)
                                            {
                                                if("".equals(value.toString()))
                                                {
                                                    super.setValueAt(0, rowIndex, columnIndex);
                                                }
                                                else
                                                {
                                                    value = (Object)Integer.parseInt(value.toString());
                                                    super.setValueAt(value, rowIndex, columnIndex);
                                                }
                                            }
                                        }
                                    });
                                    table_8.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                    table_8.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                    table_8.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                    table_8.getTableHeader().setReorderingAllowed(false);
                                    SwingUtil.setJTableHeaderRenderer(table_8, SystemInfo.getTableHeaderRenderer());
                                    table_8.setRowHeight(20);
                                    TableColumnModel tableModel8 = table_8.getColumnModel();
                                    tableModel8.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                    tableModel8.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                    table_8.addFocusListener(new java.awt.event.FocusAdapter() {
                                        public void focusGained(java.awt.event.FocusEvent evt) {
                                            table_8FocusGained(evt);
                                        }
                                        public void focusLost(java.awt.event.FocusEvent evt) {
                                            table_8FocusLost(evt);
                                        }
                                    });
                                    paneTable8.setViewportView(table_8);
                                    table_8.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                    itemClassContractedNameLabel61.setText("Å¶åªèÛñ⁄à¿ÇÕíºãﬂÇÃçƒóàó¶Çé©ìÆìIÇ…ï\é¶ÇµÇ‹Ç∑ÅB");

                                    itemClassContractedNameLabel63.setText("<html><b>ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ</b></html>");

                                    itemClassContractedNameLabel72.setText("Å¶ãZèpãqêîñ⁄ïWÅ~êVãKî‰ó¶ñ⁄ïWÇ©ÇÁéZèo");

                                    paneTable10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                    table_10.setModel(new javax.swing.table.DefaultTableModel(
                                        new Object [][] {
                                        },
                                        new String [] {
                                            "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé", "îNä‘"
                                        }) {
                                            Class[] types = new Class [] {
                                                String.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class,
                                                Integer.class};
                                            boolean[] canEdit = new boolean [] {
                                                false, false, false, false, false, false, false, false, false, false, false, false, false, false
                                            };
                                            public Class getColumnClass(int columnIndex) {
                                                return types [columnIndex];
                                            }
                                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                return canEdit [columnIndex];
                                            }
                                        });
                                        table_10.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                        table_10.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                        table_10.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                        table_10.getTableHeader().setReorderingAllowed(false);
                                        SwingUtil.setJTableHeaderRenderer(table_10, SystemInfo.getTableHeaderRenderer());
                                        table_10.setRowHeight(20);
                                        SelectTableCellRenderer.setSelectTableCellRenderer(table_10);
                                        table_10.addFocusListener(new java.awt.event.FocusAdapter() {
                                            public void focusGained(java.awt.event.FocusEvent evt) {
                                                table_10FocusGained(evt);
                                            }
                                            public void focusLost(java.awt.event.FocusEvent evt) {
                                                table_10FocusLost(evt);
                                            }
                                        });
                                        table_10.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                                table_10PropertyChange(evt);
                                            }
                                        });
                                        paneTable10.setViewportView(table_10);
                                        table_10.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                        itemClassContractedNameLabel73.setText("<html><b>ÅÉî}ëÃï êVãKñ⁄ïWê›íËÅÑ</b></html>");

                                        paneTable9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                        table_9.setModel(new javax.swing.table.DefaultTableModel(
                                            new Object [][] {
                                            },
                                            new String [] {
                                                "", "ëOä˙", "ñ⁄ïW"
                                            }) {
                                                Class[] types = new Class [] {
                                                    String.class,
                                                    Integer.class,
                                                    Integer.class
                                                };
                                                boolean[] canEdit = new boolean [] {
                                                    false, false, false
                                                };
                                                public Class getColumnClass(int columnIndex) {
                                                    return types [columnIndex];
                                                }
                                                public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                    switch(rowIndex)
                                                    {
                                                        case 0:
                                                        if(columnIndex == 2 )
                                                        {
                                                            return true;
                                                        }
                                                        break;
                                                        default:
                                                        break;
                                                    }
                                                    return false;
                                                }

                                            });
                                            table_9.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                            table_9.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                            table_9.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                            table_9.getTableHeader().setReorderingAllowed(false);
                                            SwingUtil.setJTableHeaderRenderer(table_9, SystemInfo.getTableHeaderRenderer());
                                            table_9.setRowHeight(20);
                                            TableColumnModel tableModel9 = table_9.getColumnModel();
                                            tableModel9.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                            table_9.addFocusListener(new java.awt.event.FocusAdapter() {
                                                public void focusGained(java.awt.event.FocusEvent evt) {
                                                    table_9FocusGained(evt);
                                                }
                                                public void focusLost(java.awt.event.FocusEvent evt) {
                                                    table_9FocusLost(evt);
                                                }
                                            });
                                            table_9.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                                    table_9PropertyChange(evt);
                                                }
                                            });
                                            paneTable9.setViewportView(table_9);
                                            table_9.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                            itemClassContractedNameLabel77.setText("<html><b>ÅÉÉfÉVÉãñ⁄ïWê›íËÅÑ</b></html>");

                                            paneTable6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                                            paneTable6.setPreferredSize(new java.awt.Dimension(300, 752));

                                            table_6.setModel(new javax.swing.table.DefaultTableModel(
                                                new Object [][] {
                                                },
                                                new String [] {
                                                    "åªèÛñ⁄à¿", "90ì˙", "120ì˙", "180ì˙"
                                                }
                                            ) {
                                                Class[] types = new Class [] {
                                                    String.class,
                                                    Integer.class,
                                                    Integer.class,
                                                    Integer.class
                                                };
                                                boolean[] canEdit = new boolean [] {
                                                    false, false, false, false
                                                };
                                                public Class getColumnClass(int columnIndex) {
                                                    return types [columnIndex];
                                                }
                                                public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                    return canEdit [columnIndex];
                                                }
                                            });
                                            table_6.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                            table_6.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                            table_6.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                            table_6.getTableHeader().setReorderingAllowed(false);
                                            SwingUtil.setJTableHeaderRenderer(table_6, SystemInfo.getTableHeaderRenderer());
                                            table_6.setRowHeight(20);
                                            table_6.addFocusListener(new java.awt.event.FocusAdapter() {
                                                public void focusGained(java.awt.event.FocusEvent evt) {
                                                    table_6FocusGained(evt);
                                                }
                                                public void focusLost(java.awt.event.FocusEvent evt) {
                                                    table_6FocusLost(evt);
                                                }
                                            });
                                            paneTable6.setViewportView(table_6);
                                            table_6.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                            paneTable_7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                                            paneTable_7.setPreferredSize(new java.awt.Dimension(300, 752));

                                            table_7.setModel(new javax.swing.table.DefaultTableModel(
                                                new Object [][] {
                                                },
                                                new String [] {
                                                    "ñ⁄ïW", "90ì˙", "120ì˙", "180ì˙"
                                                }
                                            ) {
                                                Class[] types = new Class [] {
                                                    String.class,
                                                    Integer.class,
                                                    Integer.class,
                                                    Integer.class
                                                };
                                                boolean[] canEdit = new boolean [] {
                                                    false, true, true, true
                                                };
                                                public Class getColumnClass(int columnIndex) {
                                                    return types [columnIndex];
                                                }
                                                public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                    return canEdit [columnIndex];
                                                }
                                                public void setValueAt(Object value, int rowIndex, int columnIndex)
                                                {
                                                    if (0 < columnIndex)
                                                    {
                                                        if("".equals(value.toString()))
                                                        {
                                                            super.setValueAt(0, rowIndex, columnIndex);
                                                        }
                                                        else
                                                        {
                                                            value = (Object)Integer.parseInt(value.toString());
                                                            super.setValueAt(value, rowIndex, columnIndex);
                                                        }
                                                    }
                                                }
                                            });
                                            table_7.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                            table_7.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                            table_7.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                            table_7.getTableHeader().setReorderingAllowed(false);
                                            SwingUtil.setJTableHeaderRenderer(table_7, SystemInfo.getTableHeaderRenderer());
                                            table_7.setRowHeight(20);
                                            TableColumnModel tableModel7 = table_7.getColumnModel();
                                            tableModel7.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                            tableModel7.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                            tableModel7.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                            paneTable_7.setViewportView(table_7);
                                            table_7.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                            itemClassContractedNameLabel38.setText("éñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                            itemClassContractedNameLabel37.setText("áA");

                                            itemClassContractedNameLabel41.setText("åéïΩãœêlêîÇÕîNä‘ãZèpãqñ⁄ïWÅÄ12Å~êVãKî‰ó¶Ç©ÇÁéZèo");

                                            itemClassContractedNameLabel40.setText("Å¶");

                                            paneTable_12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                            table_12.setModel(new javax.swing.table.DefaultTableModel(
                                                new Object [][] {},
                                                new String [] {
                                                    "ÉfÉVÉã", "ëOä˙êlêî", "ëOä˙îÑè„çÇ", "ëOä˙î‰ó¶", "ñ⁄ïWî‰ó¶", "ëOä˙óàìXâÒêî", "ñ⁄ïWóàìXâÒêî"
                                                }) {
                                                    Class[] types = new Class [] {
                                                        String.class,
                                                        Integer.class,
                                                        Integer.class,
                                                        Integer.class,
                                                        Integer.class,
                                                        Integer.class,
                                                        Integer.class
                                                    };
                                                    boolean[] canEdit = new boolean [] {
                                                        false, false, false, false, true, false, true
                                                    };
                                                    public Class getColumnClass(int columnIndex) {
                                                        return types [columnIndex];
                                                    }
                                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                        return canEdit [columnIndex];
                                                    }

                                                });
                                                table_12.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                table_12.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                table_12.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                table_12.getTableHeader().setReorderingAllowed(false);
                                                SwingUtil.setJTableHeaderRenderer(table_12, SystemInfo.getTableHeaderRenderer());
                                                table_12.setRowHeight(20);
                                                TableColumnModel tableModel12 = table_12.getColumnModel();
                                                tableModel12.getColumn(4).setCellEditor(new CustomCellEditor(new JTextField(),3 , 2));
                                                tableModel12.getColumn(6).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                table_12.addFocusListener(new java.awt.event.FocusAdapter() {
                                                    public void focusGained(java.awt.event.FocusEvent evt) {
                                                        table_12FocusGained(evt);
                                                    }
                                                    public void focusLost(java.awt.event.FocusEvent evt) {
                                                        table_12FocusLost(evt);
                                                    }
                                                });
                                                paneTable_12.setViewportView(table_12);
                                                table_12.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                itemClassContractedNameLabel47.setText("ÉfÉVÉãÇÃè„à ÇPÅCÇQÅCÇRÇÃîÑè„çÇñ⁄ïWãyÇ—1êlÇÃóàìXâÒêîñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                paneTable11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                table_11.setModel(new javax.swing.table.DefaultTableModel(
                                                    new Object [][] {
                                                    },
                                                    new String [] {
                                                        "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé", "îNä‘","é©óÕèWãq"
                                                    }) {
                                                        Class[] types = new Class [] {
                                                            String.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Integer.class,
                                                            Object.class};
                                                        boolean[] canEdit = new boolean [] {
                                                            false, false, false, false, false, false, false, false, false, false, false, false, false, false,true
                                                        };
                                                        public Class getColumnClass(int columnIndex) {
                                                            return types [columnIndex];
                                                        }
                                                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                            if( rowIndex < countRow)
                                                            {
                                                                if( ( 0 < columnIndex && columnIndex < 13) || columnIndex == 14 )
                                                                {
                                                                    return true;
                                                                }
                                                            }
                                                            return false;
                                                        }
                                                    });
                                                    table_11.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                    table_11.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                    table_11.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                    table_11.getTableHeader().setReorderingAllowed(false);
                                                    SwingUtil.setJTableHeaderRenderer(table_11, SystemInfo.getTableHeaderRenderer());
                                                    table_11.setRowHeight(20);
                                                    TableColumnModel tableModel11 = table_11.getColumnModel();
                                                    tableModel11.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(4).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(5).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(6).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(7).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(8).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(9).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(10).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(11).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    tableModel11.getColumn(12).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                    table_11.addFocusListener(new java.awt.event.FocusAdapter() {
                                                        public void focusGained(java.awt.event.FocusEvent evt) {
                                                            table_11FocusGained(evt);
                                                        }
                                                        public void focusLost(java.awt.event.FocusEvent evt) {
                                                            table_11FocusLost(evt);
                                                        }
                                                    });
                                                    table_11.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                                            table_11PropertyChange(evt);
                                                        }
                                                    });
                                                    paneTable11.setViewportView(table_11);
                                                    table_11.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                    itemClassContractedNameLabel58.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                    itemClassContractedNameLabel62.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                    itemClassContractedNameLabel65.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                    itemClassContractedNameLabel66.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                    paneTable11_sum.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                    table_11_sum.setModel(new javax.swing.table.DefaultTableModel(
                                                        new Object [][] {
                                                        },
                                                        new String [] {
                                                        }) {
                                                            public Class getColumnClass(int columnIndex) {
                                                                return Object.class;
                                                            }
                                                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                return false;
                                                            }

                                                        });
                                                        table_11_sum.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                        table_11_sum.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                        table_11_sum.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                        table_11_sum.setTableHeader(null);
                                                        table_11_sum.setRowHeight(20);
                                                        table_11_sum.addFocusListener(new java.awt.event.FocusAdapter() {
                                                            public void focusGained(java.awt.event.FocusEvent evt) {
                                                                table_11_sumFocusGained(evt);
                                                            }
                                                            public void focusLost(java.awt.event.FocusEvent evt) {
                                                                table_11_sumFocusLost(evt);
                                                            }
                                                        });
                                                        paneTable11_sum.setViewportView(table_11_sum);
                                                        table_11_sum.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                        itemClassContractedNameLabel74.setText("<html><b>ÅÉâ“ìÆÉJÉãÉeñáêîÅÑ</b></html>");

                                                        tbKarte.setBackground(new java.awt.Color(240, 240, 240));
                                                        tbKarte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                                                        tbKarte.setModel(new javax.swing.table.DefaultTableModel(
                                                            new Object [][] {
                                                            },
                                                            new String [] {
                                                            }) {
                                                                Class[] types = new Class [] {
                                                                    String.class,
                                                                    Integer.class,
                                                                    Integer.class,
                                                                    Integer.class,
                                                                    Integer.class
                                                                };
                                                                public Class getColumnClass(int columnIndex) {
                                                                    return types [columnIndex];
                                                                }
                                                                public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                    if ( columnIndex == 0 ) {
                                                                        return false;
                                                                    }
                                                                    return true;
                                                                }
                                                            });
                                                            tbKarte.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                            tbKarte.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                            tbKarte.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                            tbKarte.setRowHeight(20);
                                                            DefaultTableModel tbKarteModel = ((DefaultTableModel) tbKarte.getModel());
                                                            tbKarteModel.setColumnCount(5);
                                                            TableColumnModel tableColModel = tbKarte.getColumnModel();
                                                            tableColModel.getColumn(1).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                            tableColModel.getColumn(2).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                            tableColModel.getColumn(3).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));
                                                            tableColModel.getColumn(4).setCellEditor(new CustomCellEditor(new JTextField() , 9, 1));

                                                            org.jdesktop.layout.GroupLayout PanelPage2Layout = new org.jdesktop.layout.GroupLayout(PanelPage2);
                                                            PanelPage2.setLayout(PanelPage2Layout);
                                                            PanelPage2Layout.setHorizontalGroup(
                                                                PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                .add(PanelPage2Layout.createSequentialGroup()
                                                                    .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(paneTable9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                            .add(4, 4, 4)
                                                                            .add(itemClassContractedNameLabel65))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(10, 10, 10)
                                                                            .add(itemClassContractedNameLabel45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(10, 10, 10)
                                                                            .add(itemClassContractedNameLabel59)
                                                                            .add(5, 5, 5)
                                                                            .add(itemClassContractedNameLabel46)
                                                                            .add(492, 492, 492)
                                                                            .add(itemClassContractedNameLabel37)
                                                                            .add(5, 5, 5)
                                                                            .add(itemClassContractedNameLabel38))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(paneTable6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                            .add(18, 18, 18)
                                                                            .add(paneTable_7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                            .add(18, 18, 18)
                                                                            .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                .add(paneTable8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 259, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                .add(itemClassContractedNameLabel58)))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(10, 10, 10)
                                                                            .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                .add(PanelPage2Layout.createSequentialGroup()
                                                                                    .add(17, 17, 17)
                                                                                    .add(itemClassContractedNameLabel61))
                                                                                .add(itemClassContractedNameLabel63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                            .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                .add(PanelPage2Layout.createSequentialGroup()
                                                                                    .add(63, 63, 63)
                                                                                    .add(itemClassContractedNameLabel62))
                                                                                .add(PanelPage2Layout.createSequentialGroup()
                                                                                    .add(381, 381, 381)
                                                                                    .add(itemClassContractedNameLabel74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                                .add(PanelPage2Layout.createSequentialGroup()
                                                                                    .add(381, 381, 381)
                                                                                    .add(tbKarte, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 327, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(itemClassContractedNameLabel40)
                                                                            .add(5, 5, 5)
                                                                            .add(itemClassContractedNameLabel41))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(paneTable10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 963, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(itemClassContractedNameLabel72))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(10, 10, 10)
                                                                            .add(itemClassContractedNameLabel73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(paneTable11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 963, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(paneTable11_sum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 963, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(10, 10, 10)
                                                                            .add(itemClassContractedNameLabel77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(itemClassContractedNameLabel47))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(paneTable_12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 642, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(27, 27, 27)
                                                                            .add(itemClassContractedNameLabel66)))
                                                                    .add(8, 8, 8))
                                                            );
                                                            PanelPage2Layout.setVerticalGroup(
                                                                PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                .add(PanelPage2Layout.createSequentialGroup()
                                                                    .add(2, 2, 2)
                                                                    .add(itemClassContractedNameLabel45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(1, 1, 1)
                                                                    .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(itemClassContractedNameLabel59)
                                                                        .add(itemClassContractedNameLabel46)
                                                                        .add(itemClassContractedNameLabel37)
                                                                        .add(itemClassContractedNameLabel38))
                                                                    .add(1, 1, 1)
                                                                    .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(paneTable6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                        .add(paneTable_7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(paneTable8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                            .add(0, 0, 0)
                                                                            .add(itemClassContractedNameLabel58)))
                                                                    .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(itemClassContractedNameLabel61)
                                                                            .add(2, 2, 2)
                                                                            .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                                                .add(itemClassContractedNameLabel63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                .add(itemClassContractedNameLabel74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                                                        .add(itemClassContractedNameLabel62))
                                                                    .add(2, 2, 2)
                                                                    .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(paneTable9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                        .add(PanelPage2Layout.createSequentialGroup()
                                                                            .add(17, 17, 17)
                                                                            .add(itemClassContractedNameLabel65))
                                                                        .add(tbKarte, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                    .add(1, 1, 1)
                                                                    .add(PanelPage2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(itemClassContractedNameLabel40)
                                                                        .add(itemClassContractedNameLabel41))
                                                                    .add(2, 2, 2)
                                                                    .add(paneTable10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(0, 0, 0)
                                                                    .add(itemClassContractedNameLabel72)
                                                                    .add(2, 2, 2)
                                                                    .add(itemClassContractedNameLabel73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(2, 2, 2)
                                                                    .add(paneTable11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(0, 0, 0)
                                                                    .add(paneTable11_sum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(1, 1, 1)
                                                                    .add(itemClassContractedNameLabel77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(1, 1, 1)
                                                                    .add(itemClassContractedNameLabel47)
                                                                    .add(1, 1, 1)
                                                                    .add(paneTable_12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                    .add(0, 0, 0)
                                                                    .add(itemClassContractedNameLabel66))
                                                            );

                                                            PanelPage3.setPreferredSize(new java.awt.Dimension(1000, 750));

                                                            itemClassContractedNameLabel48.setText("<html><b>ÅÉäeÉÅÉjÉÖÅ[î‰ó¶ñ⁄ïWê›íËÅÑ</b></html>");

                                                            itemClassContractedNameLabel64.setText("<html><b>ÅÉäeÉÅÉjÉÖÅ[íPâøñ⁄ïWê›íËÅÑ</b></html>");

                                                            paneTable15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                            table_15.setModel(new javax.swing.table.DefaultTableModel(
                                                                new Object [][] {},
                                                                new String [] {
                                                                }) {
                                                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                        return false;
                                                                    }
                                                                });
                                                                table_15.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                table_15.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                table_15.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                table_15.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

                                                                table_15.setInheritsPopupMenu(true);
                                                                table_15.getTableHeader().setReorderingAllowed(false);
                                                                SwingUtil.setJTableHeaderRenderer(table_15, SystemInfo.getTableHeaderRenderer());
                                                                table_15.setRowHeight(20);
                                                                SelectTableCellRenderer.setSelectTableCellRenderer(table_15);
                                                                table_15.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                    public void focusGained(java.awt.event.FocusEvent evt) {
                                                                        table_15FocusGained(evt);
                                                                    }
                                                                    public void focusLost(java.awt.event.FocusEvent evt) {
                                                                        table_15FocusLost(evt);
                                                                    }
                                                                });
                                                                paneTable15.setViewportView(table_15);
                                                                table_15.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                itemClassContractedNameLabel75.setText("<html><b>ÅÉãZèpîÑè„ñ⁄ïWÇ∆å©çûÇ›ÇÃç∑àŸÅÑ</b></html>");

                                                                paneTable14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                                Table_14.setModel(new javax.swing.table.DefaultTableModel(
                                                                    new Object [][] {},
                                                                    new String [] {
                                                                    }) {
                                                                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                            if( rowIndex == 1)
                                                                            {
                                                                                if( 0 < columnIndex && columnIndex <= countCol)
                                                                                {
                                                                                    return true;
                                                                                }
                                                                            }
                                                                            return false;
                                                                        }
                                                                    });
                                                                    Table_14.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                    Table_14.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                    Table_14.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                    Table_14.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

                                                                    Table_14.setInheritsPopupMenu(true);
                                                                    Table_14.getTableHeader().setReorderingAllowed(false);
                                                                    SwingUtil.setJTableHeaderRenderer(Table_14, SystemInfo.getTableHeaderRenderer());
                                                                    Table_14.setRowHeight(20);
                                                                    SelectTableCellRenderer.setSelectTableCellRenderer(Table_14);
                                                                    Table_14.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                        public void focusGained(java.awt.event.FocusEvent evt) {
                                                                            Table_14FocusGained(evt);
                                                                        }
                                                                        public void focusLost(java.awt.event.FocusEvent evt) {
                                                                            Table_14FocusLost(evt);
                                                                        }
                                                                    });
                                                                    Table_14.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                                                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                                                            Table_14PropertyChange(evt);
                                                                        }
                                                                    });
                                                                    paneTable14.setViewportView(Table_14);
                                                                    Table_14.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                    itemClassContractedNameLabel78.setText("<html><b>ÅÉè§ïiï î‰ó¶ñ⁄ïWê›íËÅÑ</b></html>");

                                                                    paneTable13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
                                                                    paneTable13.setPreferredSize(new java.awt.Dimension(300, 752));

                                                                    table_13.setModel(new javax.swing.table.DefaultTableModel(
                                                                        new Object [][] {},
                                                                        new String [] {
                                                                        }) {
                                                                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                                if( rowIndex == 1)
                                                                                {
                                                                                    if( 0 < columnIndex && columnIndex <= countCol)
                                                                                    {
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return false;
                                                                            }
                                                                            public void setValueAt(Object value, int rowIndex, int columnIndex)
                                                                            {
                                                                                if (0 < columnIndex && columnIndex <= countCol)
                                                                                {
                                                                                    if("".equals(value.toString()))
                                                                                    {
                                                                                        super.setValueAt(0, rowIndex, columnIndex);
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        value = (Object)Integer.parseInt(value.toString());
                                                                                        super.setValueAt(value, rowIndex, columnIndex);
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                        table_13.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                        table_13.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                        table_13.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                        table_13.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

                                                                        table_13.setInheritsPopupMenu(true);
                                                                        table_13.getTableHeader().setReorderingAllowed(false);
                                                                        SwingUtil.setJTableHeaderRenderer(table_13, SystemInfo.getTableHeaderRenderer());
                                                                        table_13.setRowHeight(20);
                                                                        //TableColumnModel tableModel13 = table_13.getColumnModel();
                                                                        //for( int i = 1;i <= countCol;i ++)
                                                                        //{
                                                                            //   tableModel13.getColumn(i).setCellEditor(new CustomCellEditor(new JTextField(),3));
                                                                            //}
                                                                        table_13.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                            public void focusGained(java.awt.event.FocusEvent evt) {
                                                                                table_13FocusGained(evt);
                                                                            }
                                                                            public void focusLost(java.awt.event.FocusEvent evt) {
                                                                                table_13FocusLost(evt);
                                                                            }
                                                                        });
                                                                        table_13.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                                                            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                                                                table_13PropertyChange(evt);
                                                                            }
                                                                        });
                                                                        paneTable13.setViewportView(table_13);
                                                                        table_13.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                        itemClassContractedNameLabel43.setText("<html><b>ÅÉäeÉÅÉjÉÖÅ[îÑè„çÇå©çûÇ›ÅÑ</b></html>");

                                                                        paneTable17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                                        table_17.setModel(new javax.swing.table.DefaultTableModel(
                                                                            new Object [][] {},
                                                                            new String [] {
                                                                            }) {
                                                                                public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                                    if( rowIndex == 2)
                                                                                    {
                                                                                        if( 0 < columnIndex && columnIndex <= countCol1)
                                                                                        {
                                                                                            return true;
                                                                                        }
                                                                                    }
                                                                                    return false;
                                                                                }
                                                                            });
                                                                            table_17.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                            table_17.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                            table_17.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                            table_17.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

                                                                            table_17.setInheritsPopupMenu(true);
                                                                            table_17.getTableHeader().setReorderingAllowed(false);
                                                                            SwingUtil.setJTableHeaderRenderer(table_17, SystemInfo.getTableHeaderRenderer());
                                                                            table_17.setRowHeight(20);
                                                                            //TableColumnModel tableModel17 = table_17.getColumnModel();
                                                                            //for( int i = 1;i <= countCol1;i ++)
                                                                            //{
                                                                                //    tableModel17.getColumn(i).setCellEditor(new CustomCellEditor(new JTextField(),3));
                                                                                //}
                                                                            table_17.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                                public void focusGained(java.awt.event.FocusEvent evt) {
                                                                                    table_17FocusGained(evt);
                                                                                }
                                                                                public void focusLost(java.awt.event.FocusEvent evt) {
                                                                                    table_17FocusLost(evt);
                                                                                }
                                                                            });
                                                                            table_17.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                                                                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                                                                    table_17PropertyChange(evt);
                                                                                }
                                                                            });
                                                                            paneTable17.setViewportView(table_17);
                                                                            table_17.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                            itemClassContractedNameLabel50.setText("Å¶ç∑àŸÇ™É}ÉCÉiÉXÇ…Ç»Ç¡ÇƒÇ¢ÇÈèÍçáÇÕÉÅÉjÉÖÅ[î‰ó¶ãyÇ—ÉÅÉjÉÖÅ[íPâøñ⁄ïWÇ…Ç¬Ç¢Çƒå©íºÇµÇÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                                            paneTable16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                                            table_16.setModel(new javax.swing.table.DefaultTableModel(
                                                                                new Object [][] {},
                                                                                new String [] {
                                                                                    "ñ⁄ïW", "å©çûÇ›", "ç∑àŸ"
                                                                                }) {
                                                                                    Class[] types = new Class [] {
                                                                                        Integer.class,
                                                                                        Integer.class,
                                                                                        Integer.class
                                                                                    };
                                                                                    boolean[] canEdit = new boolean [] {
                                                                                        false, false, false
                                                                                    };
                                                                                    public Class getColumnClass(int columnIndex) {
                                                                                        return types [columnIndex];
                                                                                    }
                                                                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                                        return false;
                                                                                    }
                                                                                });
                                                                                table_16.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                                table_16.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                                table_16.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                                table_16.getTableHeader().setReorderingAllowed(false);
                                                                                SwingUtil.setJTableHeaderRenderer(table_16, SystemInfo.getTableHeaderRenderer());
                                                                                table_16.setRowHeight(20);
                                                                                SelectTableCellRenderer.setSelectTableCellRenderer(table_16);
                                                                                table_16.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                                    public void focusGained(java.awt.event.FocusEvent evt) {
                                                                                        table_16FocusGained(evt);
                                                                                    }
                                                                                    public void focusLost(java.awt.event.FocusEvent evt) {
                                                                                        table_16FocusLost(evt);
                                                                                    }
                                                                                });
                                                                                paneTable16.setViewportView(table_16);
                                                                                table_16.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                                paneTable18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                                                table_18.setModel(new javax.swing.table.DefaultTableModel(
                                                                                    new Object [][] {},
                                                                                    new String [] {
                                                                                        "", "1åé", "2åé", "3åé", "4åé", "5åé", "6åé", "7åé", "8åé", "9åé", "10åé", "11åé", "12åé", "îNä‘"
                                                                                    }) {
                                                                                        Class[] types = new Class [] {
                                                                                            String.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class,
                                                                                            Integer.class};
                                                                                        boolean[] canEdit = new boolean [] {
                                                                                            false, false, false, false, false, false, false, false, false, false, false, false, false, false
                                                                                        };
                                                                                        public Class getColumnClass(int columnIndex) {
                                                                                            return types [columnIndex];
                                                                                        }
                                                                                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                                            return false;
                                                                                        }
                                                                                    });
                                                                                    table_18.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                                    table_18.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                                    table_18.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                                    table_18.getTableHeader().setReorderingAllowed(false);
                                                                                    SwingUtil.setJTableHeaderRenderer(table_18, SystemInfo.getTableHeaderRenderer());
                                                                                    table_18.setRowHeight(20);
                                                                                    SelectTableCellRenderer.setSelectTableCellRenderer(table_18);
                                                                                    table_18.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                                        public void focusGained(java.awt.event.FocusEvent evt) {
                                                                                            table_18FocusGained(evt);
                                                                                        }
                                                                                        public void focusLost(java.awt.event.FocusEvent evt) {
                                                                                            table_18FocusLost(evt);
                                                                                        }
                                                                                    });
                                                                                    paneTable18.setViewportView(table_18);
                                                                                    table_18.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                                    itemClassContractedNameLabel55.setText("ï™óﬁï Ç…ñ⁄ïWî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                                                    itemClassContractedNameLabel56.setText("ñ⁄ïWî‰ó¶Ç…ëŒÇ∑ÇÈñàåéÇÃìXîÃîÑè„ñ⁄ïWÇÕâ∫ãLÇ…Ç»ÇËÇ‹Ç∑");

                                                                                    itemClassContractedNameLabel60.setText("á@");

                                                                                    itemClassContractedNameLabel39.setText("áA");

                                                                                    itemClassContractedNameLabel67.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                                                    itemClassContractedNameLabel68.setText("Å¶î‰ó¶ÇÕêÆêîÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");

                                                                                    itemClassContractedNameLabel69.setText("Å¶ç\ê¨î‰ó¶ÇÕçáåvÇ≈100%Ç…Ç»ÇÈÇÊÇ§Ç…ê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB");

                                                                                    paneTable18_sum.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

                                                                                    table_18_sum.setModel(new javax.swing.table.DefaultTableModel(
                                                                                        new Object [][] {
                                                                                        },
                                                                                        new String [] {
                                                                                        }) {
                                                                                            public Class getColumnClass(int columnIndex) {
                                                                                                return Object.class;
                                                                                            }
                                                                                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                                                return false;
                                                                                            }
                                                                                        });
                                                                                        table_18_sum.setPreferredScrollableViewportSize(new java.awt.Dimension(1000, 750));
                                                                                        table_18_sum.setSelectionBackground(new java.awt.Color(255, 210, 142));
                                                                                        table_18_sum.setSelectionForeground(new java.awt.Color(0, 0, 0));
                                                                                        table_18_sum.setTableHeader(null);
                                                                                        table_18_sum.setRowHeight(20);
                                                                                        table_18_sum.addFocusListener(new java.awt.event.FocusAdapter() {
                                                                                            public void focusGained(java.awt.event.FocusEvent evt) {
                                                                                                table_18_sumFocusGained(evt);
                                                                                            }
                                                                                            public void focusLost(java.awt.event.FocusEvent evt) {
                                                                                                table_18_sumFocusLost(evt);
                                                                                            }
                                                                                        });
                                                                                        paneTable18_sum.setViewportView(table_18_sum);
                                                                                        table_18_sum.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                                                                                        org.jdesktop.layout.GroupLayout PanelPage3Layout = new org.jdesktop.layout.GroupLayout(PanelPage3);
                                                                                        PanelPage3.setLayout(PanelPage3Layout);
                                                                                        PanelPage3Layout.setHorizontalGroup(
                                                                                            PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                            .add(PanelPage3Layout.createSequentialGroup()
                                                                                                .addContainerGap()
                                                                                                .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                    .add(PanelPage3Layout.createSequentialGroup()
                                                                                                        .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                            .add(itemClassContractedNameLabel48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                            .add(itemClassContractedNameLabel64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                            .add(itemClassContractedNameLabel75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                                                        .add(108, 108, 108))
                                                                                                    .add(PanelPage3Layout.createSequentialGroup()
                                                                                                        .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                            .add(itemClassContractedNameLabel43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                            .add(PanelPage3Layout.createSequentialGroup()
                                                                                                                .add(itemClassContractedNameLabel39)
                                                                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                                                                                .add(itemClassContractedNameLabel56)))
                                                                                                        .add(0, 0, Short.MAX_VALUE))
                                                                                                    .add(PanelPage3Layout.createSequentialGroup()
                                                                                                        .add(18, 18, 18)
                                                                                                        .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                            .add(PanelPage3Layout.createSequentialGroup()
                                                                                                                .add(itemClassContractedNameLabel67)
                                                                                                                .add(0, 0, Short.MAX_VALUE))
                                                                                                            .add(paneTable13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                                        .addContainerGap())))
                                                                                            .add(PanelPage3Layout.createSequentialGroup()
                                                                                                .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, PanelPage3Layout.createSequentialGroup()
                                                                                                        .add(27, 27, 27)
                                                                                                        .add(paneTable15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                                                                    .add(PanelPage3Layout.createSequentialGroup()
                                                                                                        .addContainerGap()
                                                                                                        .add(itemClassContractedNameLabel78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                        .add(0, 0, Short.MAX_VALUE))
                                                                                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, PanelPage3Layout.createSequentialGroup()
                                                                                                        .add(29, 29, 29)
                                                                                                        .add(paneTable14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                                                                    .add(PanelPage3Layout.createSequentialGroup()
                                                                                                        .add(11, 11, 11)
                                                                                                        .add(itemClassContractedNameLabel60)
                                                                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                                                        .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, paneTable18_sum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                                                            .add(paneTable17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                                                            .add(paneTable18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                                                            .add(PanelPage3Layout.createSequentialGroup()
                                                                                                                .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                                    .add(PanelPage3Layout.createSequentialGroup()
                                                                                                                        .add(itemClassContractedNameLabel68)
                                                                                                                        .add(131, 131, 131)
                                                                                                                        .add(itemClassContractedNameLabel69))
                                                                                                                    .add(itemClassContractedNameLabel50)
                                                                                                                    .add(itemClassContractedNameLabel55)
                                                                                                                    .add(paneTable16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 292, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                                                                .add(0, 0, Short.MAX_VALUE)))))
                                                                                                .addContainerGap())
                                                                                        );
                                                                                        PanelPage3Layout.setVerticalGroup(
                                                                                            PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                            .add(PanelPage3Layout.createSequentialGroup()
                                                                                                .add(2, 2, 2)
                                                                                                .add(itemClassContractedNameLabel48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(1, 1, 1)
                                                                                                .add(paneTable13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(itemClassContractedNameLabel67)
                                                                                                .add(2, 2, 2)
                                                                                                .add(itemClassContractedNameLabel64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(1, 1, 1)
                                                                                                .add(paneTable14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(2, 2, 2)
                                                                                                .add(itemClassContractedNameLabel43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(paneTable15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(2, 2, 2)
                                                                                                .add(itemClassContractedNameLabel75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(paneTable16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(itemClassContractedNameLabel50)
                                                                                                .add(2, 2, 2)
                                                                                                .add(itemClassContractedNameLabel78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(1, 1, 1)
                                                                                                .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                                                                    .add(itemClassContractedNameLabel55)
                                                                                                    .add(itemClassContractedNameLabel60))
                                                                                                .add(0, 0, 0)
                                                                                                .add(paneTable17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                                                                    .add(itemClassContractedNameLabel68)
                                                                                                    .add(itemClassContractedNameLabel69))
                                                                                                .add(2, 2, 2)
                                                                                                .add(PanelPage3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                                                                    .add(itemClassContractedNameLabel39)
                                                                                                    .add(itemClassContractedNameLabel56))
                                                                                                .add(0, 0, 0)
                                                                                                .add(paneTable18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(paneTable18_sum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap(140, Short.MAX_VALUE))
                                                                                        );

                                                                                        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
                                                                                        this.setLayout(layout);
                                                                                        layout.setHorizontalGroup(
                                                                                            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                                    .add(PanelTop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
                                                                                                    .add(PanelPage1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
                                                                                                    .add(PanelPage2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
                                                                                                    .add(PanelPage3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE))
                                                                                                .addContainerGap())
                                                                                        );
                                                                                        layout.setVerticalGroup(
                                                                                            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                                            .add(layout.createSequentialGroup()
                                                                                                .add(PanelTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(PanelPage1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(PanelPage2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .add(0, 0, 0)
                                                                                                .add(PanelPage3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap(194, Short.MAX_VALUE))
                                                                                        );
                                                                                    }// </editor-fold>//GEN-END:initComponents

    private void table_1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_1PropertyChange
        changeDataTable(table_1, 1);
    }//GEN-LAST:event_table_1PropertyChange

    private void table_2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_2PropertyChange
        changeDataTable(table_2, 2);
    }//GEN-LAST:event_table_2PropertyChange

    private void txttechnicCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txttechnicCaretUpdate
        Integer targetValue = 0;
        if (txttechnic.getText().replace(",", "").matches("[0-9]+")) {
            targetValue = Integer.parseInt(txttechnic.getText());
            //targetValue = ((Double) Math.floor((targetValue / 12.0))).longValue();
            targetValue = setBigDecimalValue((targetValue / 12.0));
            //get table_1 column îNä‘
            if (flag > 0) {
                Integer value = Integer.parseInt(txttechnic.getText());
                Integer valueYear = Integer.parseInt(table_1.getValueAt(3, 13).toString());
                table_1.setValueAt(valueYear - value, 3, 14);
            }
        }
        if("".equals(txttechnic.getText().toString()))
        {
            Integer valueYear = Integer.parseInt(table_1.getValueAt(3, 13).toString());
            table_1.setValueAt(valueYear, 3, 14);
        }
        txtaverage.setText(targetValue + "");
        // set row ãZîÑñ⁄à¿ when input data textbox
        Double valuetb1 = 0d;
        Double valueText = Double.parseDouble(txtaverage.getText());
        for( int i = 1;i < 14;i ++ )
        {
            valuetb1 = Double.parseDouble(table_1.getValueAt(1, i).toString());
            table_1.setValueAt(setBigDecimalValue(valueText * valuetb1), 2, i);
        }
        //set value cell(0,0) table_16
        if(flagNextPage >= 3)
        {
            table_16.setValueAt(valueText.intValue(), 0, 0);
            Integer valueTarget = 0;
            Integer valueExpected = 0;
            valueTarget     = Integer.parseInt(table_16.getValueAt(0, 0).toString());
            valueExpected   = Integer.parseInt(table_16.getValueAt(0, 1).toString());
            table_16.setValueAt(valueExpected - valueTarget, 0, 2);
        }
    }//GEN-LAST:event_txttechnicCaretUpdate

    private void txtSaleTargetCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtSaleTargetCaretUpdate
        Integer targetValue = 0;
        if (txtSaleTarget.getText().replace(",", "").matches("[0-9]+")) {
            targetValue = Integer.parseInt(txtSaleTarget.getText());
            targetValue = setBigDecimalValue((targetValue / 12.0) );
            //get table_2 column îNä‘
            if (flag > 1) {
                Integer value = Integer.parseInt(txtSaleTarget.getText());
                Integer valueYear = Integer.parseInt(table_2.getValueAt(3, 13).toString());
                table_2.setValueAt(valueYear - value, 3, 14);
            }
        }
        if("".equals(txtSaleTarget.getText().toString()))
        {
            Integer valueYear = Integer.parseInt(table_2.getValueAt(3, 13).toString());
            table_2.setValueAt(valueYear, 3, 14);
        }
        txtaverage1.setText(targetValue + "");
        // set row ãZîÑñ⁄à¿ when input data textbox
        Double valuetb1 = 0d;
        Double valueText = Double.parseDouble(txtaverage1.getText());
        for( int i = 1;i < 14;i ++ )
        {
            valuetb1 = Double.parseDouble(table_2.getValueAt(1, i).toString());
            table_2.setValueAt(setBigDecimalValue(valueText * valuetb1), 2, i);
        }
    }//GEN-LAST:event_txtSaleTargetCaretUpdate

    private void table_3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_3PropertyChange
        changeDataTable(table_3, 3);
    }//GEN-LAST:event_table_3PropertyChange

    private void table_4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_4PropertyChange
        changeDataTable(table_4, 4);
    }//GEN-LAST:event_table_4PropertyChange

    private void table_9PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_9PropertyChange
        changeDataTable(table_9, 5);
    }//GEN-LAST:event_table_9PropertyChange

    private void table_10PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_10PropertyChange
        //changeDataTable(table_10, 6);
    }//GEN-LAST:event_table_10PropertyChange

    private void btNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextActionPerformed
        stopCellTableAll();
        if (!checkInput()) {
            return;
        }
        if (step < 3) {
            step++;
        }
        btNext.setCursor(null);
        try{
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.refresh();
            if (step == 1) {
                PanelPage1.setVisible(false);
                PanelPage2.setVisible(true);
                PanelPage3.setVisible(false);
                btBack.setEnabled(true);
                //PanelRegist.setVisible(false);
                btRegist.setEnabled(false);
            }
            if (step == 2) {
                PanelPage1.setVisible(false);
                PanelPage2.setVisible(false);
                PanelPage3.setVisible(true);
                btNext.setEnabled(false);
                //PanelRegist.setVisible(true);
                btRegist.setEnabled(true);

            }
        }finally{
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btNextActionPerformed

    private void btBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBackActionPerformed
        if (step == 3) {
            step = step - 2;
        } else {
            step--;
        }
        if (step == 0) {
            PanelPage1.setVisible(true);
            PanelPage2.setVisible(false);
            PanelPage3.setVisible(false);
            btBack.setEnabled(false);
            btRegist.setEnabled(false);
            //PanelRegist.setVisible(false);
        }
        if (step == 1) {
            PanelPage1.setVisible(false);
            PanelPage2.setVisible(true);
            PanelPage3.setVisible(false);
            btNext.setEnabled(true);
            btRegist.setEnabled(false);
            //PanelRegist.setVisible(false);

        }
    }//GEN-LAST:event_btBackActionPerformed

    private void btRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistActionPerformed
        stopCellTableAll();
        if(!checkInput())
        {
            return;
        }
        btRegist.setCursor(null);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (this.regist()) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS,
                            ""),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
                            ""),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btRegistActionPerformed

    private void table_11PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_11PropertyChange
        //IVS_LVTu start add 2015/01/22 Task #35026
        if(table_11.getEditingColumn() == 14)
        {
            return;
        }
        changeDataTable(table_11, 6);
        //IVS_LVTu end add 2015/01/22 Task #35026
    }//GEN-LAST:event_table_11PropertyChange

    private void table_13PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_13PropertyChange
        changeDataTable(table_13, 7);
    }//GEN-LAST:event_table_13PropertyChange

    private void Table_14PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_Table_14PropertyChange
        changeDataTable(Table_14, 8);
    }//GEN-LAST:event_Table_14PropertyChange

    private void table_17PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_table_17PropertyChange
        changeDataTable(table_17, 9);
    }//GEN-LAST:event_table_17PropertyChange

    private void table_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_1FocusLost
        if(table_1.getSelectedRow() > -1)
        {
            table_1.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_1FocusLost

    private void table_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_1FocusGained
        table_1.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_1FocusGained

    private void table_2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_2FocusGained
        table_2.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_2FocusGained

    private void table_2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_2FocusLost
        if(table_2.getSelectedRow() > -1)
        {
            table_2.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_2FocusLost

    private void table_3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_3FocusGained
        table_3.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_3FocusGained

    private void table_3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_3FocusLost
        if(table_3.getSelectedRow() > -1)
        {
            table_3.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_3FocusLost

    private void table_4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_4FocusGained
        table_4.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_4FocusGained

    private void table_4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_4FocusLost
        if(table_4.getSelectedRow() > -1)
        {
            table_4.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_4FocusLost

    private void table_5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_5FocusGained
        table_5.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_5FocusGained

    private void table_5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_5FocusLost
        if(table_5.getSelectedRow() > -1)
        {
            table_5.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_5FocusLost

    private void table_6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_6FocusGained
        table_6.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_6FocusGained

    private void table_6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_6FocusLost
        if(table_6.getSelectedRow() > -1)
        {
            table_6.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_6FocusLost

    private void table_8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_8FocusGained
        table_8.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_8FocusGained

    private void table_8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_8FocusLost
        if(table_8.getSelectedRow() > -1)
        {
            table_8.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_8FocusLost

    private void table_9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_9FocusGained
        table_9.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_9FocusGained

    private void table_9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_9FocusLost
        if(table_9.getSelectedRow() > -1)
        {
            table_9.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_9FocusLost

    private void table_10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_10FocusGained
        table_10.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_10FocusGained

    private void table_10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_10FocusLost
        if(table_10.getSelectedRow() > -1)
        {
            table_10.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_10FocusLost

    private void table_11FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_11FocusGained
        table_11.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_11FocusGained

    private void table_11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_11FocusLost
        if(table_11.getSelectedRow() > -1)
        {
            table_11.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_11FocusLost

    private void table_12FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_12FocusGained
        table_12.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_12FocusGained

    private void table_12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_12FocusLost
        if(table_12.getSelectedRow() > -1)
        {
            table_12.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_12FocusLost

    private void table_13FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_13FocusGained
        table_13.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_13FocusGained

    private void table_13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_13FocusLost
        if(table_13.getSelectedRow() > -1)
        {
            table_13.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_13FocusLost

    private void Table_14FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Table_14FocusGained
        Table_14.setCellSelectionEnabled(true);
    }//GEN-LAST:event_Table_14FocusGained

    private void Table_14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Table_14FocusLost
        if(Table_14.getSelectedRow() > -1)
        {
            Table_14.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_Table_14FocusLost

    private void table_15FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_15FocusGained
        table_15.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_15FocusGained

    private void table_15FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_15FocusLost
        if(table_15.getSelectedRow() > -1)
        {
            table_15.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_15FocusLost

    private void table_16FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_16FocusGained
        table_16.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_16FocusGained

    private void table_16FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_16FocusLost
        if(table_16.getSelectedRow() > -1)
        {
            table_16.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_16FocusLost

    private void table_17FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_17FocusGained
        table_17.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_17FocusGained

    private void table_17FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_17FocusLost
        if(table_17.getSelectedRow() > -1)
        {
            table_17.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_17FocusLost

    private void table_18FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_18FocusGained
        table_18.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_18FocusGained

    private void table_18FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_18FocusLost
        if(table_18.getSelectedRow() > -1)
        {
            table_18.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_18FocusLost

    private void table_11_sumFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_11_sumFocusGained
        table_11_sum.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_11_sumFocusGained

    private void table_11_sumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_11_sumFocusLost
        if(table_11_sum.getSelectedRow() > -1)
        {
            table_11_sum.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_11_sumFocusLost

    private void table_18_sumFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_18_sumFocusGained
        table_18_sum.setCellSelectionEnabled(true);
    }//GEN-LAST:event_table_18_sumFocusGained

    private void table_18_sumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_18_sumFocusLost
        if(table_18_sum.getSelectedRow() > -1)
        {
            table_18_sum.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_table_18_sumFocusLost

    private void tbTargetItemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbTargetItemFocusGained
        tbTargetItem.setCellSelectionEnabled(true);
    }//GEN-LAST:event_tbTargetItemFocusGained

    private void tbTargetItemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbTargetItemFocusLost
        if(tbTargetItem.getSelectedRow() > -1)
        {
            tbTargetItem.setCellSelectionEnabled(false);
        }
    }//GEN-LAST:event_tbTargetItemFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelPage1;
    private javax.swing.JPanel PanelPage2;
    private javax.swing.JPanel PanelPage3;
    private javax.swing.JPanel PanelTop;
    private com.geobeck.swing.JTableEx Table_14;
    private javax.swing.JButton btBack;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btRegist;
    private javax.swing.JLabel itemClassContractedNameLabel1;
    private javax.swing.JLabel itemClassContractedNameLabel10;
    private javax.swing.JLabel itemClassContractedNameLabel11;
    private javax.swing.JLabel itemClassContractedNameLabel12;
    private javax.swing.JLabel itemClassContractedNameLabel13;
    private javax.swing.JLabel itemClassContractedNameLabel14;
    private javax.swing.JLabel itemClassContractedNameLabel15;
    private javax.swing.JLabel itemClassContractedNameLabel16;
    private javax.swing.JLabel itemClassContractedNameLabel17;
    private javax.swing.JLabel itemClassContractedNameLabel18;
    private javax.swing.JLabel itemClassContractedNameLabel2;
    private javax.swing.JLabel itemClassContractedNameLabel20;
    private javax.swing.JLabel itemClassContractedNameLabel21;
    private javax.swing.JLabel itemClassContractedNameLabel22;
    private javax.swing.JLabel itemClassContractedNameLabel23;
    private javax.swing.JLabel itemClassContractedNameLabel24;
    private javax.swing.JLabel itemClassContractedNameLabel25;
    private javax.swing.JLabel itemClassContractedNameLabel26;
    private javax.swing.JLabel itemClassContractedNameLabel28;
    private javax.swing.JLabel itemClassContractedNameLabel3;
    private javax.swing.JLabel itemClassContractedNameLabel37;
    private javax.swing.JLabel itemClassContractedNameLabel38;
    private javax.swing.JLabel itemClassContractedNameLabel39;
    private javax.swing.JLabel itemClassContractedNameLabel40;
    private javax.swing.JLabel itemClassContractedNameLabel41;
    private javax.swing.JLabel itemClassContractedNameLabel43;
    private javax.swing.JLabel itemClassContractedNameLabel45;
    private javax.swing.JLabel itemClassContractedNameLabel46;
    private javax.swing.JLabel itemClassContractedNameLabel47;
    private javax.swing.JLabel itemClassContractedNameLabel48;
    private javax.swing.JLabel itemClassContractedNameLabel5;
    private javax.swing.JLabel itemClassContractedNameLabel50;
    private javax.swing.JLabel itemClassContractedNameLabel51;
    private javax.swing.JLabel itemClassContractedNameLabel52;
    private javax.swing.JLabel itemClassContractedNameLabel53;
    private javax.swing.JLabel itemClassContractedNameLabel54;
    private javax.swing.JLabel itemClassContractedNameLabel55;
    private javax.swing.JLabel itemClassContractedNameLabel56;
    private javax.swing.JLabel itemClassContractedNameLabel57;
    private javax.swing.JLabel itemClassContractedNameLabel58;
    private javax.swing.JLabel itemClassContractedNameLabel59;
    private javax.swing.JLabel itemClassContractedNameLabel6;
    private javax.swing.JLabel itemClassContractedNameLabel60;
    private javax.swing.JLabel itemClassContractedNameLabel61;
    private javax.swing.JLabel itemClassContractedNameLabel62;
    private javax.swing.JLabel itemClassContractedNameLabel63;
    private javax.swing.JLabel itemClassContractedNameLabel64;
    private javax.swing.JLabel itemClassContractedNameLabel65;
    private javax.swing.JLabel itemClassContractedNameLabel66;
    private javax.swing.JLabel itemClassContractedNameLabel67;
    private javax.swing.JLabel itemClassContractedNameLabel68;
    private javax.swing.JLabel itemClassContractedNameLabel69;
    private javax.swing.JLabel itemClassContractedNameLabel7;
    private javax.swing.JLabel itemClassContractedNameLabel70;
    private javax.swing.JLabel itemClassContractedNameLabel71;
    private javax.swing.JLabel itemClassContractedNameLabel72;
    private javax.swing.JLabel itemClassContractedNameLabel73;
    private javax.swing.JLabel itemClassContractedNameLabel74;
    private javax.swing.JLabel itemClassContractedNameLabel75;
    private javax.swing.JLabel itemClassContractedNameLabel77;
    private javax.swing.JLabel itemClassContractedNameLabel78;
    private javax.swing.JLabel itemClassContractedNameLabel9;
    private javax.swing.JLabel lbDate;
    private javax.swing.JLabel lbDateValue;
    private javax.swing.JLabel lbShopCategory;
    private javax.swing.JLabel lbShopCategoryvalue;
    private javax.swing.JLabel lbShopValue;
    private javax.swing.JLabel lbshop;
    private javax.swing.JScrollPane paneTable1;
    private javax.swing.JScrollPane paneTable10;
    private javax.swing.JScrollPane paneTable11;
    private javax.swing.JScrollPane paneTable11_sum;
    private javax.swing.JScrollPane paneTable13;
    private javax.swing.JScrollPane paneTable14;
    private javax.swing.JScrollPane paneTable15;
    private javax.swing.JScrollPane paneTable16;
    private javax.swing.JScrollPane paneTable17;
    private javax.swing.JScrollPane paneTable18;
    private javax.swing.JScrollPane paneTable18_sum;
    private javax.swing.JScrollPane paneTable2;
    private javax.swing.JScrollPane paneTable4;
    private javax.swing.JScrollPane paneTable5;
    private javax.swing.JScrollPane paneTable6;
    private javax.swing.JScrollPane paneTable8;
    private javax.swing.JScrollPane paneTable9;
    private javax.swing.JScrollPane paneTable_12;
    private javax.swing.JScrollPane paneTable_7;
    private javax.swing.JScrollPane panetable3;
    private javax.swing.JScrollPane panetable4;
    private com.geobeck.swing.JTableEx table_1;
    private com.geobeck.swing.JTableEx table_10;
    private com.geobeck.swing.JTableEx table_11;
    private com.geobeck.swing.JTableEx table_11_sum;
    private com.geobeck.swing.JTableEx table_12;
    private com.geobeck.swing.JTableEx table_13;
    private com.geobeck.swing.JTableEx table_15;
    private com.geobeck.swing.JTableEx table_16;
    private com.geobeck.swing.JTableEx table_17;
    private com.geobeck.swing.JTableEx table_18;
    private com.geobeck.swing.JTableEx table_18_sum;
    private com.geobeck.swing.JTableEx table_2;
    private com.geobeck.swing.JTableEx table_3;
    private com.geobeck.swing.JTableEx table_4;
    private com.geobeck.swing.JTableEx table_5;
    private com.geobeck.swing.JTableEx table_6;
    private com.geobeck.swing.JTableEx table_7;
    private com.geobeck.swing.JTableEx table_8;
    private com.geobeck.swing.JTableEx table_9;
    private com.geobeck.swing.JTableEx tbKarte;
    private com.geobeck.swing.JTableEx tbTargetItem;
    private com.geobeck.swing.JFormattedTextFieldEx txtSaleTarget;
    private com.geobeck.swing.JFormattedTextFieldEx txtaverage;
    private com.geobeck.swing.JFormattedTextFieldEx txtaverage1;
    private com.geobeck.swing.JFormattedTextFieldEx txttechnic;
    // End of variables declaration//GEN-END:variables

    /**
     * init data.
     */
    private void init() {
        showData();
        initTableColumn();
    }

    /**
     * çƒï\é¶ÇçsÇ§ÅB
     */
    private void refresh() {
        //show data
        this.showData();
    }

    /**
     * ÉfÅ[É^Çï\é¶Ç∑ÇÈÅB
     */
    private void showData() {
        // flagNextPage < 3 then next page.
        if (flagNextPage < 3) {
            // flagNextPage == 0 then show page 1
            if (flagNextPage == 0) {
                showDataPage1();
                flagNextPage++;
                initTableColumn();
                return;
            }
            // flagNextPage == 1 then show page 2
            if (flagNextPage == 1) {
                showDataPage2();
                flagNextPage++;
                initTableColumn();
                return;
            }
            //flagNextPage == 2 then show page 3
            if (flagNextPage == 2) {
                if(step == 1)
                {
                    return;
                }
                showDataPage3();
                flagNextPage++;
                initTableColumn();
                return;
            }
        }                 
    }

    /**
     * show page 1.
     * table_1 to table_5.
     */
    private void showDataPage1() {
        try {
            Vector vec = new Vector();
            // periodMonth = 3 then month begin month 4 show header table
            int tempMonth = 1;
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            if (periodMonth != 12) {
                tempMonth = periodMonth + 1 ;
            }
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change periodMonth
                //ÅÉãZèpîÑè„ñ⁄ïWê›íËÅÑ
            //set table header
            table_1.removeAll();
            TableColumnModel modelColumnTb1 = table_1.getTableHeader().getColumnModel();
            modelColumnTb1.getColumn(0).setHeaderValue("");
            for (int i = 1; i < 13; i++) {
                modelColumnTb1.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }
            ConnectionWrapper con = SystemInfo.getConnection();

            double sumTb1Row1 = 0;
            double sumTb1Row2 = 0;
            double sumTb1Row4 = 0;
            for (int i = 0; i < 4; i++) {
                vec = new Vector();
                switch (i) {
                    //row ëOîNé¿ê—
                    case 0:
                        dts = new DataTargets();
                        dts.loadDataSales(con, shopId, shopCategory, Date, 1);
                        vec.add("ëOîNé¿ê—");
                        for (int j = 0; j < dts.size(); j++) {
                            vec.add(dts.get(j).getTechnic());
                            sumTb1Row1 = sumTb1Row1 + dts.get(j).getTechnic();
                        }
                        //vec.add(Math.round(sumTb1Row1));
                        vec.add(setBigDecimalValue(sumTb1Row1));
                        break;
                    //row åéäiç∑
                    case 1:
                        vec.add("åéäiç∑");
                        for (int j = 0; j < dts.size(); j++) {
                            // if sum ëOîNé¿ê— = 0
                            if (sumTb1Row1 == 0) {
                                vec.add(0);
                            } else {
                                BigDecimal valueDecimal = new BigDecimal((dts.get(j).getTechnic() * 1.0 / (sumTb1Row1 / 12)));
                                valueDecimal = valueDecimal.setScale(1, RoundingMode.HALF_UP);
                                vec.add(valueDecimal);
                                sumTb1Row2 = sumTb1Row2 + valueDecimal.doubleValue();
                            }
                        }
                        //BigDecimal SumTb1Row2Decimal = new BigDecimal(sumTb1Row2);
                        //SumTb1Row2Decimal = SumTb1Row2Decimal.setScale(0, RoundingMode.HALF_UP);
                        vec.add(setBigDecimalValue(sumTb1Row2));
                        break;
                    //row ãZîÑñ⁄à¿
                    case 2:
                        vec.add("ãZîÑñ⁄à¿");
                        break;
                    //row ãZîÑåéñ⁄ïW
                    case 3:
                        dts = new DataTargets();
                        dts.loadDataTarget(con, shopId, shopCategory, Date, 1);
                        vec.add("ãZîÑåéñ⁄ïW");
                        for (int j = 0; j < dts.size(); j++) {
                            vec.add(dts.get(j).getTechnic());
                            sumTb1Row4 = sumTb1Row4 + dts.get(j).getTechnic();
                        }
                        //vec.add(Math.round(sumTb1Row4));
                        vec.add(setBigDecimalValue(sumTb1Row4));
                        vec.add(0);
                        txttechnic.setText(setBigDecimalValue(sumTb1Row4) + "");
                        txtaverage.setText(setBigDecimalValue(sumTb1Row4 / 12.0) + "");
                        flag++;
                        break;
                    default:
                        break;
                }
                ((DefaultTableModel) table_1.getModel()).addRow(vec);

            }
            // set value row ãZîÑñ⁄à¿
            for (int i = 1; i < 14; i++) {
                Double valueColumn = Double.parseDouble(table_1.getValueAt(1, i).toString());
                Double valueText = Double.parseDouble(txtaverage.getText());
                //table_1.setValueAt(Math.round((Double)valueText * valueColumn), 2, i);
                table_1.setValueAt(setBigDecimalValue(valueText * valueColumn), 2, i);
            }

                //ÅÉìXîÃîÑè„ñ⁄ïWê›íËÅÑ table 2
            //set table header
            table_2.removeAll();
            TableColumnModel modelColumnTb2 = table_2.getTableHeader().getColumnModel();
            tempMonth = 1;
            // periodMonth = 3 then month begin month 4 show header table
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            if (periodMonth != 12) {
                tempMonth = periodMonth + 1 ;
            }
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            for (int i = 1; i < 13; i++) {
                modelColumnTb2.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }

            double sumTb2Row1 = 0;
            double sumTb2Row2 = 0;
            double sumTb2Row4 = 0;
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    //row ëOîNé¿ê—
                    case 0:
                        dts = new DataTargets();
                        dts.loadDataSales(con, shopId, shopCategory, Date, 2);
                        vec = new Vector();
                        vec.add("ëOîNé¿ê—");
                        for (int j = 0; j < dts.size(); j++) {
                            vec.add(dts.get(j).getTechnic());
                            sumTb2Row1 = sumTb2Row1 + dts.get(j).getTechnic();
                        }
                        //vec.add(Math.round(sumTb2Row1));
                        vec.add(setBigDecimalValue(sumTb2Row1));
                        break;
                    //row åéäiç∑
                    case 1:
                        vec = new Vector();
                        vec.add("åéäiç∑");
                        for (int j = 0; j < dts.size(); j++) {
                            // if sum ëOîNé¿ê— = 0
                            if (sumTb2Row1 == 0) {
                                vec.add(0);
                            } else {
                                BigDecimal valueDecimal = new BigDecimal((dts.get(j).getTechnic() * 1.0 / (sumTb2Row1 / 12)));
                                valueDecimal = valueDecimal.setScale(1, RoundingMode.HALF_UP);
                                vec.add(valueDecimal);
                                sumTb2Row2 = sumTb2Row2 + valueDecimal.doubleValue();
                            }
                        }
//                        BigDecimal SumTb2Row2Decimal = new BigDecimal(sumTb2Row2);
//                        SumTb2Row2Decimal = SumTb2Row2Decimal.setScale(0, RoundingMode.HALF_UP);
                        vec.add(setBigDecimalValue(sumTb2Row2));
                        break;
                    //row ìXîÃñ⁄à¿
                    case 2:
                        vec = new Vector();
                        vec.add("ìXîÃñ⁄à¿");
                        break;
                    // row ìXîÃåéñ⁄ïW
                    default:
                        dts = new DataTargets();
                        dts.loadDataTarget(con, shopId, shopCategory, Date, 2);
                        vec = new Vector();
                        vec.add("ìXîÃåéñ⁄ïW");
                        for (int j = 0; j < dts.size(); j++) {
                            vec.add(dts.get(j).getTechnic());
                            sumTb2Row4 = sumTb2Row4 + dts.get(j).getTechnic();
                        }
                        //vec.add(Math.round(sumTb2Row4));
                        vec.add(setBigDecimalValue(sumTb2Row4));
                        vec.add(0);
                        //txtSaleTarget.setText(Math.round(sumTb2Row4) + "");
                        //txtaverage1.setText(((Double) Math.floor((sumTb2Row4 / 12.0) + 0.5)).longValue() + "");
                        txtSaleTarget.setText(setBigDecimalValue(sumTb2Row4) + "");
                        txtaverage1.setText(setBigDecimalValue(sumTb2Row4 / 12.0)  + "");
                        flag++;
                        break;
                }
                ((DefaultTableModel) table_2.getModel()).addRow(vec);

            }
            for (int i = 1; i < 14; i++) {
                Double valueColumn = Double.parseDouble(table_2.getValueAt(1, i).toString());
                Double valueText = Double.parseDouble(txtaverage1.getText());
                //table_2.setValueAt(Math.round(valueText * valueColumn), 2, i);
                table_2.setValueAt(setBigDecimalValue(valueText * valueColumn), 2, i);
            }

            // table 3 - ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ
            table_3.removeAll();
            vec = new Vector();
            dts = new DataTargets();
            Integer rate_customer;
            //periodMonth = 3  or = 12
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //if (periodMonth != 12) {
                rate_customer = dts.loadDataCustomer(con, shopId, shopCategory, year, periodMonth);
            //} else {
            //    rate_customer = dts.loadDataCustomer(con, shopId, shopCategory, year, 1,periodMonth);
            //}
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //set value table 3 ëOä˙é¿ê—.ëÂÇ‹Ç©Ç»èóê´ãqêîî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
            vec.add("ëOä˙é¿ê—");
            vec.add(rate_customer);
            ((DefaultTableModel) table_3.getModel()).addRow(vec);
            vec = new Vector();
            Double sumFemaleRate = sumFemaleRate = dts.loadSumFemaleRate(con, shopId, shopCategory, year);
            //set value row ñ⁄ïW
            vec.add("ñ⁄ïW");
            //vec.add(Math.round(sumFemaleRate * 100));
            vec.add(setBigDecimalValue(sumFemaleRate * 100));
            ((DefaultTableModel) table_3.getModel()).addRow(vec);

            //table 4 ëOä˙é¿ê—. ëÂÇ‹Ç©Ç»ãZèpãqíPâøñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
            table_4.removeAll();
            vec = new Vector();
            dts = new DataTargets();

            List<Integer> arrmen = dts.loadDataTrophyCustomer(con, shopId, shopCategory, year, 1, periodMonth);
            List<Integer> arrWomen = dts.loadDataTrophyCustomer(con, shopId, shopCategory, year, 2, periodMonth);

            Integer totalValue = 0;
            Integer totalValueWomen = 0;
            Integer totalNumWomen = arrWomen.get(1).intValue();
            Integer totalValuemen = 0;
            Integer totalNummen = arrmen.get(1).intValue();

            vec.add("ëOä˙é¿ê—");
            if (totalNumWomen != 0 && totalNummen != 0) {
                //totalValue = (arrWomen.get(0) + arrmen.get(0)) / (arrWomen.get(1) + arrmen.get(1));
                totalValue = setBigDecimalValue((arrWomen.get(0) + arrmen.get(0))*1.0 / (arrWomen.get(1) + arrmen.get(1)));
            }
            if (totalNumWomen != 0) {
                //totalValueWomen = arrWomen.get(0) / arrWomen.get(1);
                totalValueWomen = setBigDecimalValue(arrWomen.get(0)*1.0 / arrWomen.get(1));
            }
            if (totalNummen != 0) {
                //totalValuemen = arrmen.get(0).intValue() / arrmen.get(1);
                totalValuemen = setBigDecimalValue(arrmen.get(0).intValue()*1.0 / arrmen.get(1));
            }
            vec.add(totalValue);
            vec.add(totalValueWomen);
            vec.add(totalValuemen);
            ((DefaultTableModel) table_4.getModel()).addRow(vec);

            Integer sumFemaleUnitPrice = 0;
            Integer sumMaleUnitPrice = 0;
            //vec = new Vector();
            //vec = new Vector();
            sumFemaleUnitPrice = dts.loadSumFemaleUnitPrice(con, shopId, shopCategory, year);
            sumMaleUnitPrice = dts.loadSumMaleUnitPrice(con, shopId, shopCategory, year);
            //long sumtable4 = Math.round((sumFemaleUnitPrice * sumFemaleRate) + (sumMaleUnitPrice * (1 - sumFemaleRate)));
            Integer sumtable4 = setBigDecimalValue((sumFemaleUnitPrice * sumFemaleRate) + (sumMaleUnitPrice * (1 - sumFemaleRate)));

            vec = new Vector();
            vec.add("ñ⁄ïW");
            vec.add(sumtable4);
            vec.add(sumFemaleUnitPrice);
            vec.add(sumMaleUnitPrice);
            ((DefaultTableModel) table_4.getModel()).addRow(vec);

            // table 5-ÅÉãZèpãqêîñ⁄ïWÅÑ
            table_5.removeAll();
            TableColumnModel modelColumnTb5 = table_5.getTableHeader().getColumnModel();

            for (int i = 1; i < 13; i++) {
                modelColumnTb5.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }
            vec = new Vector();

            vec.add("ãZèpãqêîñ⁄ïW");
            long sumYear = 0;
            for (int i = 1; i < 13; i++) {
                vec.add(0);
            }
            //set sum
            //vec.add(sumYear);
            vec.add(0);
            ((DefaultTableModel) table_5.getModel()).addRow(vec);
            setDataTable5();
            //IVS_LVTu start add 2015/01/23 Task #35026
            //ÅÉìXîÃçwîÉî‰ó¶ÅEíPâøñ⁄ïWê›íËÅÑ
            tbTargetItem.removeAll();
            vec = new Vector();
            TableColumnModel modeltbTargetItem = tbTargetItem.getTableHeader().getColumnModel();
            tempMonth = 1;
            // periodMonth
            if (periodMonth != 12) {
                tempMonth = periodMonth + 1 ;
            }
            for (int i = 1; i < 13; i++) {
                modeltbTargetItem.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }
            
            MstDataTarget mstTarget = new MstDataTarget();
            ArrayList<MstDataTarget> arr = new ArrayList<MstDataTarget>();
            arr = mstTarget.getSelectTarget(con, shopId, shopCategory, Date);
            //çwîÉî‰ó¶
            vec.add("çwîÉî‰ó¶");
            for(int i = 0; i < arr.size() ; i ++)
            {
                vec.add(setBigDecimalValue(arr.get(i).getItem_sales_rate()*100));
            }
            
            ((DefaultTableModel) tbTargetItem.getModel()).addRow(vec);
            //çwîÉíPâø
            vec = new Vector();
            vec.add("çwîÉíPâø");
            for(int i = 0; i < arr.size() ; i ++)
            {
                vec.add(arr.get(i).getItem_sales_price());
            }
            ((DefaultTableModel) tbTargetItem.getModel()).addRow(vec);
            //IVS_LVTu end add 2015/01/23 Task #35026
        } catch (SQLException ex) {
            Logger.getLogger(SettingDataTargetPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * showDataPage2. show page 2.
     * table_6 to table_12
     */
    private void showDataPage2() {
        try {
            //set header table.
            int tempMonth = 1;
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            if (periodMonth != 12) {
                tempMonth = periodMonth + 1;
            }
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            dts = new DataTargets();
            //table 6. ÅÉäeçƒóàó¶ÅAéñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWê›íËÅÑ. çƒóàó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
            table_6.removeAll();
            ConnectionWrapper con = SystemInfo.getConnection();

            Vector vec;
            //IVS_LVTu start edit 2015/06/02 New request #37146
            int monthRepeat = this.periodMonth;
            int yearRepeat = this.year ;
            if ( periodMonth == 12) {
                yearRepeat = this.year - 1;
            }
            double repeat_net_new = 0;
            double new_net_total  = 0;
            double repeat_new = 0;
            double newTotal  = 0;
            double sub_fixed_total = 0;
            double sub_fixed_appear = 0;
            double fixed_total = 0;
            double fixed_appear = 0;

            Vector vecNet = new Vector();
            Vector vecNew = new Vector();
            Vector vecSubFix = new Vector();
            Vector vecFix = new Vector();

            ResultSetWrapper rsNetrepeat = null;
            ResultSetWrapper rsrepeat = null;
            try {
                vecNet.add("ÉlÉbÉgèWãqêVãKçƒóàó¶");
                vecNew.add("êVãKçƒóàó¶");
                vecSubFix.add("èÄå≈íËçƒóàó¶");
                vecFix.add("å≈íËçƒóàó¶");
                
                for ( int i = 3 ;i < 7;i ++) {
                    if ( i == 5) {
                        i ++;
                    }
                    rsNetrepeat = con.executeQuery(dts.sqlQueryMobileRepeat(this.shopId, yearRepeat, monthRepeat, this.shopCategory, i));
                    rsrepeat = con.executeQuery(dts.getSqlRepeat(this.shopId,yearRepeat, monthRepeat, this.shopCategory, i));
                    if ( rsNetrepeat.next() ) {
                        new_net_total = rsNetrepeat.getDouble("net_new_total");
                        repeat_net_new = rsNetrepeat.getDouble("net_new_reappearance");
                    }
                    if ( rsrepeat.next() ) {
                        newTotal = rsrepeat.getDouble("new_total");
                        repeat_new = rsrepeat.getDouble("new_reappearance");
                        sub_fixed_total = rsrepeat.getDouble("sub_fixed_total");
                        sub_fixed_appear = rsrepeat.getDouble("sub_fixed_reappearance");
                        fixed_total = rsrepeat.getDouble("fixed_total");
                        fixed_appear = rsrepeat.getDouble("fixed_reappearance");
                    }
                        
                    vecNet.add(new_net_total != 0 ? (repeat_net_new*100)/new_net_total : 0);
                    vecNew.add(newTotal != 0 ? (repeat_new*100)/newTotal : 0);
                    vecSubFix.add(sub_fixed_total != 0 ? (sub_fixed_appear*100)/sub_fixed_total : 0);
                    vecFix.add(fixed_total != 0 ? (fixed_appear*100)/fixed_total : 0);
                       
                }
                ((DefaultTableModel) table_6.getModel()).addRow(vecNet);
                ((DefaultTableModel) table_6.getModel()).addRow(vecNew);
                ((DefaultTableModel) table_6.getModel()).addRow(vecSubFix);
                ((DefaultTableModel) table_6.getModel()).addRow(vecFix);
                
            } catch (SQLException ex) {
                Logger.getLogger(SettingDataTargetPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            //IVS_LVTu end edit 2015/06/02 New request #37146
            //table 7
            table_7.removeAll();

            for (int i = 0; i < 4; i++) {
                ArrayList<Integer> arrTargetRate = dts.loadTargetRate(con, shopId, shopCategory, year, i);
                vec = new Vector();
                switch (i) {
                    //ÉlÉbÉgèWãqêVãKçƒóàó¶
                    case 0:
                        vec.add("ÉlÉbÉgèWãqêVãKçƒóàó¶");
                        vec.add(arrTargetRate.get(0));
                        vec.add(arrTargetRate.get(1));
                        vec.add(arrTargetRate.get(2));
                        break;
                    //êVãKçƒóàó¶
                    case 1:
                        vec.add("êVãKçƒóàó¶");
                        vec.add(arrTargetRate.get(0));
                        vec.add(arrTargetRate.get(1));
                        vec.add(arrTargetRate.get(2));
                        break;
                    //èÄå≈íËçƒóàó¶
                    case 2:
                        vec.add("èÄå≈íËçƒóàó¶");
                        vec.add(arrTargetRate.get(0));
                        vec.add(arrTargetRate.get(1));
                        vec.add(arrTargetRate.get(2));
                        break;
                    //å≈íËçƒóàó¶
                    case 3:
                        vec.add("å≈íËçƒóàó¶");
                        vec.add(arrTargetRate.get(0));
                        vec.add(arrTargetRate.get(1));
                        vec.add(arrTargetRate.get(2));
                        break;
                    default:
                        break;
                }
                ((DefaultTableModel) table_7.getModel()).addRow(vec);
            }

            //table 8 - éñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
            table_8.removeAll();
            Integer reservationRate = dts.loadReservationRate(con, shopId, shopCategory, year, Date, 1, periodMonth);
            Integer nextReservationRate = dts.loadReservationRate(con, shopId, shopCategory, year, Date, 2, periodMonth);
            //ñ⁄ïW		
            DataTarget dt = dts.loadTargetReservationRate(con, shopId, shopCategory, year);

            for (int i = 0; i < 2; i++) {
                vec = new Vector();
                switch (i) {
                    //ëOä˙
                    case 0:
                        vec.add("ëOä˙");
                        vec.add(reservationRate);
                        vec.add(nextReservationRate);
                        break;
                    //ñ⁄ïW
                    case 1:
                        vec.add("ñ⁄ïW");
                        vec.add(dt.getBeforeReserve().intValue());
                        vec.add(dt.getNextReserve().intValue());
                        break;
                    default:
                        break;
                }
                ((DefaultTableModel) table_8.getModel()).addRow(vec);
            }

            //table 9 - ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ
            table_9.removeAll();
            Integer targetNewRate = 0;
            Integer previousPeriod = 0;
            for (int i = 0; i < 2; i++) {
                vec = new Vector();
                switch (i) {
                    //êVãKî‰ó¶
                    case 0:
                        previousPeriod = dts.loadPreviousPeriodRate(con, shopId, year, periodMonth, shopCategory);
                        targetNewRate = dts.loadTargetNewRate(con, shopId, shopCategory, year);
                        vec.add("êVãKî‰ó¶");
                        vec.add(previousPeriod);
                        vec.add(targetNewRate);
                        break;
                    //åéïΩãœêlêî
                    case 1:
                        // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 
                        // [Phase4][Product][Code][Edit]ñ⁄ïWè⁄ç◊ê›íË_ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ dang tinh chua dung
                        Integer averageMonthPeriod = 0;
                        Integer averageMonthTarget = 0;
//                        if (previousPeriod != 0) {
////                            averageMonthPeriod = (Integer.parseInt(table_5.getValueAt(0, 13).toString()) / 12) * previousPeriod;
//                        }
                        Integer numberOfPeriod = 0;
                        DataTargets targetItem = new DataTargets();
                        numberOfPeriod = targetItem.getSelectPeriodPeople(shopId, year, periodMonth);
                        //averageMonthPeriod = (long) numberOfPeriod / 12;
                        averageMonthPeriod = setBigDecimalValue(numberOfPeriod*1.0 / 12);
                        if ( targetNewRate != 0) {
                            Double dbTarget = (double) Integer.parseInt(table_5.getValueAt(0, 13).toString()) / 12 ;
                            Double targetPercent = (double) targetNewRate / 100;
                            //averageMonthTarget =Math.round(dbTarget * targetPercent) ;
                            averageMonthTarget =setBigDecimalValue(dbTarget * targetPercent) ;
                        }
                        vec.add("åéïΩãœêlêî");
                        vec.add(averageMonthPeriod);
                        vec.add(averageMonthTarget);
                        break;
                    default:
                        break;
                }
                ((DefaultTableModel) table_9.getModel()).addRow(vec);
            }

            //table 10
            table_10.removeAll();
            TableColumnModel modelColumnTb10 = table_10.getTableHeader().getColumnModel();

            for (int i = 1; i < 13; i++) {
                modelColumnTb10.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }
            vec = new Vector();
            vec.add("êVãKãqêî");
            //long sumYear = 0;
            for (int i = 1; i < 13; i++) {
                vec.add(0);
            }
            //vec.add(sumYear);
            vec.add(0);
            ((DefaultTableModel) table_10.getModel()).addRow(vec);
             setDataTable10();   
            //table 11 - ÅÉî}ëÃï êVãKñ⁄ïWê›íËÅÑ.
            table_11.removeAll();
            TableColumnModel modelColumnTb11 = table_11.getTableHeader().getColumnModel();
            //set header table
            for (int i = 1; i < 13; i++) {
                modelColumnTb11.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }
            arrmfcm = MstFirstComingMotive.getAll(con);
            countRow = arrmfcm.size();
            for (int i = 0; i < countRow; i++) {
                DataTargets datatarget = new DataTargets();
                vec = new Vector();
                Integer sum = 0;
                vec.add(arrmfcm.get(i).getFirstComingMotiveName());
                datatarget.loadDataTargetMotive(con, shopId, shopCategory, Date, arrmfcm.get(i).getFirstComingMotiveClassId());
                //IVS_LVTu start edit 2015/01/22 Task #35026
                boolean isChecked = false;
                for (int j = 0; j < datatarget.getArrDataTargetMotive().size(); j++) {
                    vec.add(datatarget.getArrDataTargetMotive().get(j).getNum());
                    sum = sum + Integer.parseInt(datatarget.getArrDataTargetMotive().get(j).getNum().toString());
                    // if month == 12 then all data row is checked
                    if(datatarget.getArrDataTargetMotive().get(j).getMonth() == 12 && datatarget.getArrDataTargetMotive().get(j).getOwn_flg() != 0)
                    {
                        isChecked = true;
                    }
                }
                vec.add(sum);
                // get checkbox into table.
                vec.add(getItemCheckbox(isChecked));
                //IVS_LVTu end edit 2015/01/22 Task #35026
                ((DefaultTableModel) table_11.getModel()).addRow(vec);
            }

            vec = new Vector();
            Vector vec1 = new Vector();
            vec.add("çáåv");
            vec1.add("ç∑àŸ");
            Integer totalsum1 = 0;
            Integer totalsum2 = 0;
            for (int i = 1; i <= 12; i++) {
                Integer sum = 0;
                for (int j = 0; j < countRow; j++) {
                    sum = sum + Integer.parseInt(table_11.getValueAt(j, i).toString());
                }
                vec.add(sum);
                Integer sum1 = Integer.parseInt(table_10.getValueAt(0, i).toString());
                vec1.add(sum - sum1);
                totalsum1 = totalsum1 + sum;
                totalsum2 = totalsum2 + (sum - sum1);
            }
            vec.add(totalsum1);
            vec1.add(totalsum2);
            //IVS_LVTu start edit 2015/01/20 Mashu_ê›íËâÊñ 
            //((DefaultTableModel) table_11.getModel()).addRow(vec);
            //((DefaultTableModel) table_11.getModel()).addRow(vec1);
            //table_11_sum - ÅÉî}ëÃï êVãKñ⁄ïWê›íËÅÑ.
            //table_11_sum.removeAll();
            SwingUtil.clearTable(this.table_11_sum);
            
            DefaultTableModel dtmtb11_sum = (DefaultTableModel)this.table_11_sum.getModel();
            dtmtb11_sum.setColumnCount(15);
            dtmtb11_sum.addRow(vec);
            dtmtb11_sum.addRow(vec1);
            
            if(table_11.getRowCount() > 7)
            {
                paneTable11_sum.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            }
            //IVS_LVTu end edit 2015/01/20 Mashu_ê›íËâÊñ 
            //table 12 - ÅÉÉfÉVÉãñ⁄ïWê›íËÅÑ
            table_12.removeAll();
            CustomerRankingList listRanking = new CustomerRankingList();
            ArrayList<CustomerRanking> listItemOrderSale = new ArrayList<CustomerRanking>();
            
            DataTarget dt1 = dts.loadDecylTarget(con, shopId, shopCategory, year);
            listRanking.getTargetListByShopCategory(false, shopCategory, year, periodMonth, shopId, listRanking.orderSaleValue);
            listItemOrderSale = listRanking.getCustomerRankingListByCategory();
            
            int i = 1;
            if (listItemOrderSale == null) {
                for (i = 1; i < 4; i++) {
                    vec = new Vector();
                    vec.add(i + "");
                    vec.add("0");
                    vec.add("0");
                    vec.add("0");

                    if (i == 1) {
                        vec.add(dt1.getDecyl_1Rate());
                    }
                    if (i == 2) {
                        vec.add(dt1.getDecyl_2Rate());
                    }
                    if (i == 3) {
                        vec.add(dt1.getDecyl_3Rate());
                    }

                    vec.add("0");
                    if (i == 1) {
                        vec.add(dt1.getDecyl_1Num());
                    }
                    if (i == 2) {
                        vec.add(dt1.getDecyl_2Num());
                    }
                    if (i == 3) {
                        vec.add(dt1.getDecyl_3Num());
                    }
                    ((DefaultTableModel) table_12.getModel()).addRow(vec);
                }
            }else{
                for (CustomerRanking item : listItemOrderSale) {
                    if (i < 4) {
                        vec = new Vector();
                        vec.add(i + "");
                        vec.add(item.getTargetCount());
                        vec.add(item.getSalesValue());
                        vec.add(item.getSalesShareRate());

                        if (i == 1) {
                            vec.add(dt1.getDecyl_1Rate());
                        }
                        if (i == 2) {
                            vec.add(dt1.getDecyl_2Rate());
                        }
                        if (i == 3) {
                            vec.add(dt1.getDecyl_3Rate());
                        }

//                        vec.add(arr[i-1]);
                        vec.add(item.getVisitCount());
                        
                        if (i == 1) {
                            vec.add(dt1.getDecyl_1Num());
                        }
                        if (i == 2) {
                            vec.add(dt1.getDecyl_2Num());
                        }
                        if (i == 3) {
                            vec.add(dt1.getDecyl_3Num());
                        }

                        i++;
                        ((DefaultTableModel) table_12.getModel()).addRow(vec);
                    }
                }            
            }
            // get data target karte
            SwingUtil.clearTable(this.tbKarte);
            DefaultTableModel tbKarteModel = ((DefaultTableModel) tbKarte.getModel());
            //tbKarteModel.setColumnCount(5);
            Vector vecKarteDay = new Vector();
            Vector vecKarteValue = new Vector();
            MstDataTarget dtKarte = dts.loadTargetkarte(con, shopId, shopCategory, year);
            //ì˙êî
            vecKarteDay.add("ì˙êî");
            vecKarteDay.add(dtKarte.getKarte1_day());
            vecKarteDay.add(dtKarte.getKarte2_day());
            vecKarteDay.add(dtKarte.getKarte3_day());
            vecKarteDay.add(dtKarte.getKarte4_day());
            tbKarteModel.addRow(vecKarteDay);
            vecKarteValue.add("ÉJÉãÉeñáêî");
            vecKarteValue.add(dtKarte.getKarte1_value());
            vecKarteValue.add(dtKarte.getKarte2_value());
            vecKarteValue.add(dtKarte.getKarte3_value());
            vecKarteValue.add(dtKarte.getKarte4_value());
            tbKarteModel.addRow(vecKarteValue);
     
        } catch (SQLException ex) {
            Logger.getLogger(SettingDataTargetPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * showDataPage3. show data page 3.
     * table_13 to table_18
     */
    private void showDataPage3() {
        try {
            // flag set header table
            int tempMonth = 1;
            // sum value
            int sumValue = 0;
            //periodMonth = 1 - 12
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            if (periodMonth != 12) {
                tempMonth = periodMonth + 1;
            }
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            ConnectionWrapper con = SystemInfo.getConnection();
            //table 13-ÅÉäeÉÅÉjÉÖÅ[î‰ó¶ñ⁄ïWê›íËÅÑ
            //set table header
            Vector vec = new Vector();
            dts = new DataTargets();
            //int sumValue = 0;
            dts.loadSetTargetRateMenu(con, shopId, shopCategory, year, periodMonth);

            DefaultTableModel table_13model = (DefaultTableModel) table_13.getModel();
            countCol = dts.getSetTargetMenu().size();
            vec.add("ëOä˙é¿ê—");

            table_13model.addColumn("");
            for (int i = 0; i < dts.getSetTargetMenu().size(); i++) {
                table_13model.addColumn(dts.getSetTargetMenu().get(i).technicClassName);

                //sumValue = sumValue + dts.getSetTargetMenu().get(i).customerCount;
            }
            // set color header
            SwingUtil.setJTableHeaderRenderer(table_13, SystemInfo.getTableHeaderRenderer());

            for (int i = 0; i < dts.getSetTargetMenu().size(); i++) {
                Double value = 0d;
                //if (sumValue != 0) {
                if (dts.getSetTargetMenu().get(i).totalNum != 0) {
                    value = (dts.getSetTargetMenu().get(i).customerCount * 1.0 / dts.getSetTargetMenu().get(i).totalNum) * 100;
                }
                //int resultValue = ((Double) Math.floor(value + 0.5)).intValue();
                int resultValue = setBigDecimalValue(value);
                vec.add(resultValue);
            }

            ((DefaultTableModel) table_13.getModel()).addRow(vec);
            vec = new Vector();
            vec.add("ñ⁄ïW");
            for (int i = 0; i < dts.getSetTargetMenu().size(); i++) {
                vec.add(dts.getSetTargetMenu().get(i).technicTargetRate);
            }
            ((DefaultTableModel) table_13.getModel()).addRow(vec);

            //table 14 - ÅÉäeÉÅÉjÉÖÅ[íPâøñ⁄ïWê›íËÅÑ
            //set table header
            DefaultTableModel table_14model = (DefaultTableModel) Table_14.getModel();
            Vector vec1 = new Vector();
            vec1.add("ëOä˙é¿ê—");

            table_14model.addColumn("");
            for (int i = 0; i < dts.getSetTargetMenu().size(); i++) {
                table_14model.addColumn(dts.getSetTargetMenu().get(i).technicClassName);
                if (dts.getSetTargetMenu().get(i).customerCount == 0) {
                    vec1.add(0);
                } else {
                    int value = 0;
                    if (dts.getSetTargetMenu().get(i).customerCount != 0) {
                        value = (dts.getSetTargetMenu().get(i).salesValue / dts.getSetTargetMenu().get(i).customerCount);
                    }
                    vec1.add(value);
                }
            }
            //set color header
            SwingUtil.setJTableHeaderRenderer(Table_14, SystemInfo.getTableHeaderRenderer());

            ((DefaultTableModel) Table_14.getModel()).addRow(vec1);
            vec = new Vector();
            vec.add("ñ⁄ïW");
            for (int i = 0; i < dts.getSetTargetMenu().size(); i++) {
                vec.add(dts.getSetTargetMenu().get(i).technicTargetPrice);
            }
            ((DefaultTableModel) Table_14.getModel()).addRow(vec);

            // table 15 - ÅÉäeÉÅÉjÉÖÅ[îÑè„çÇå©çûÇ›ÅÑ
            DefaultTableModel table_15model = (DefaultTableModel) table_15.getModel();
            vec = new Vector();
            vec.add("ñ⁄ïW");

            sumValue = 0;
            Double valuetb5 = Double.parseDouble(table_5.getValueAt(0, 13).toString());
            table_15model.addColumn("îÑè„çÇç\ê¨");
            for (int i = 0; i < dts.getSetTargetMenu().size(); i++) {
                table_15model.addColumn(dts.getSetTargetMenu().get(i).technicClassName);
                //set value
                Double valueTargetRate = Double.parseDouble(table_13.getValueAt(1, i + 1).toString()) / 100;
                Double valueTargetPrice = Double.parseDouble(Table_14.getValueAt(1, i + 1).toString());
                //Integer value = ((Double) Math.floor((valuetb5 / 12) * (valueTargetRate * valueTargetPrice))).intValue();
                Integer value = setBigDecimalValue((valuetb5 / 12) * (valueTargetRate * valueTargetPrice));
                sumValue = sumValue + value;
                vec.add(value);
            }
            table_15model.addColumn("îÑè„çÇå©çû");
            vec.add(sumValue);
            // set color header
            SwingUtil.setJTableHeaderRenderer(table_15, SystemInfo.getTableHeaderRenderer());
            ((DefaultTableModel) table_15.getModel()).addRow(vec);

            // table 16 - ÅÉãZèpîÑè„ñ⁄ïWÇ∆å©çûÇ›ÇÃç∑àŸÅÑ
            vec = new Vector();
            Integer targetAverageMonth = Integer.parseInt(txtaverage.getText());
            Integer saleExpected = Integer.parseInt(table_15.getValueAt(0, table_15.getColumnCount() - 1).toString());
            Integer valueDifference = saleExpected - targetAverageMonth;
            vec.add(targetAverageMonth);
            vec.add(saleExpected);
            vec.add(valueDifference);
            ((DefaultTableModel) table_16.getModel()).addRow(vec);

            // table 17 - ÅÉè§ïiï î‰ó¶ñ⁄ïWê›íËÅÑ. ï™óﬁï Ç…ñ⁄ïWî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
            dts.loadProductTarget(con, shopId,shopCategory, year, periodMonth);
            countCol1 = dts.getSetProductTarget().size();
            DefaultTableModel table_17model = (DefaultTableModel) table_17.getModel();
            countCol = dts.getSetTargetMenu().size();
            vec = new Vector();
            vec.add("ëOä˙îÑè„çÇ");

            sumValue = 0;
            table_17model.addColumn("");
            for (int i = 0; i < dts.getSetProductTarget().size(); i++) {
                table_17model.addColumn(dts.getSetProductTarget().get(i).item_class_name);
                vec.add(dts.getSetProductTarget().get(i).salesValue);
                sumValue = sumValue + dts.getSetProductTarget().get(i).salesValue;
            }
            // set color header
            SwingUtil.setJTableHeaderRenderer(table_17, SystemInfo.getTableHeaderRenderer());

            ((DefaultTableModel) table_17.getModel()).addRow(vec);
            vec = new Vector();
            vec.add("ç\ê¨î‰ó¶");
            for (int i = 0; i < dts.getSetProductTarget().size(); i++) {
                Double value = 0d;
                if (sumValue != 0) {
                    value = (dts.getSetProductTarget().get(i).salesValue * 1.0 / sumValue) * 100;
                }
                //int resultValue = ((Double) Math.floor(value + 0.5)).intValue();
                int resultValue = setBigDecimalValue(value);
                vec.add(resultValue);
            }
            ((DefaultTableModel) table_17.getModel()).addRow(vec);

            vec = new Vector();
            vec.add("ñ⁄ïWî‰ó¶");
            DataTargets dts1 = new DataTargets();
            dts1.loadSetProductTarget(con, shopId, shopCategory, year);
            for (int i = 0; i < dts1.getSetProductTarget().size(); i++) {
                vec.add(dts1.getSetProductTarget().get(i).itemTargetRate);
            }
            ((DefaultTableModel) table_17.getModel()).addRow(vec);

                // table 18 - ÅÉè§ïiï î‰ó¶ñ⁄ïWê›íËÅÑ.ñ⁄ïWî‰ó¶Ç…ëŒÇ∑ÇÈñàåéÇÃìXîÃîÑè„ñ⁄ïWÇÕâ∫ãLÇ…Ç»ÇËÇ‹Ç∑
            //set header table
            TableColumnModel modelColumnTb18 = table_18.getTableHeader().getColumnModel();
            for (int i = 1; i < 13; i++) {
                modelColumnTb18.getColumn(i).setHeaderValue(tempMonth + "åé");
                tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
            }
            for (int i = 1; i < table_17.getColumnCount(); i++) {
                sumValue = 0;
                vec = new Vector();
                //get column name tabel 17 
                String header = table_17model.getColumnName(i);
                vec.add(header);
                for (int j = 1; j <= 12; j++) {
                    Integer valuePrice = Integer.parseInt(table_2.getValueAt(3, j).toString());
                    Double valueRate = Double.parseDouble(table_17.getValueAt(2, i).toString()) / 100;
                    //Integer value = ((Double) Math.floor((valuePrice * valueRate) + 0.5)).intValue();
                    Integer value = setBigDecimalValue(valuePrice * valueRate);
                    sumValue = sumValue + value;
                    vec.add(value);
                }
                vec.add(sumValue);
                ((DefaultTableModel) table_18.getModel()).addRow(vec);
            }
            Integer totalSum = 0;
            vec = new Vector();
            vec.add("çáåv");
            for (int i = 1; i <= 13; i++) {
                Integer sum = 0;
                for (int j = 0; j < dts.getSetProductTarget().size(); j++) {
                    Integer valuePrice = Integer.parseInt(table_18.getValueAt(j, i).toString());
                    sum = sum + valuePrice;
                }
                totalSum = totalSum + sum;
                vec.add(sum);
            }
            vec.add(totalSum);
            //IVS_LVTu start edit 2015/01/21 Mashu_ê›íËâÊñ 
            //((DefaultTableModel) table_18.getModel()).addRow(vec);
            SwingUtil.clearTable(this.table_18_sum);
            
            DefaultTableModel dtmtb18_sum = (DefaultTableModel)this.table_18_sum.getModel();
            dtmtb18_sum.setColumnCount(14);
            dtmtb18_sum.addRow(vec);
            if(table_18.getRowCount() > 3)
            {
                paneTable18_sum.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            }
            //IVS_LVTu start edit 2015/01/21 Mashu_ê›íËâÊñ 

        } catch (SQLException ex) {
            Logger.getLogger(SettingDataTargetPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Rounding mode to round towards "nearest neighbor" 
     * unless both neighbors are equidistant, in which case round up.
     * @param value
     * @return Integer.
     */
    public Integer setBigDecimalValue(Double value)
    {
        BigDecimal bdValue = new BigDecimal(value);
        return bdValue.setScale(0, RoundingMode.HALF_UP).intValue();
    }
    /**
     * set Data table ÅÉãZèpãqêîñ⁄ïWÅÑ
     */
    public void setDataTable5()
    {
        for (int i = 1; i <= 12; i++) {
            Double TechnicMonthTarget = Double.parseDouble(table_1.getValueAt(3, i).toString());
            Double UnitPrice = Double.parseDouble(table_4.getValueAt(1, 1).toString());
            if (UnitPrice == 0) {
                table_5.setValueAt(0, 0, i);
            } else {
                Double value2 = TechnicMonthTarget / UnitPrice;
                //Integer Customer = ((Double) Math.floor(value2 + 0.5)).intValue();
                Integer Customer = setBigDecimalValue(value2);
                table_5.setValueAt(Customer, 0, i);
            }
        }
        // sum all month
        Integer sumCustomer = 0;
        for (int i = 1; i <= 12; i++) {
            sumCustomer = sumCustomer + Integer.parseInt(table_5.getValueAt(0, i).toString());
        }
        table_5.setValueAt(sumCustomer, 0, 13);
    }
    
    /**
     * set Data table 10 ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ.êVãKãqêî
     */
    public void setDataTable10()
    {
        Integer sumNewCustomer = 0;
        Double rateValue = Double.parseDouble(table_9.getValueAt(0, 2).toString()) / 100;
            for (int i = 1; i <= 12; i++) {
                Double valueCustomer = Double.parseDouble(table_5.getValueAt(0, i).toString());
                //Integer value = ((Double) Math.floor(valueCustomer * rateValue)).intValue();
                Integer value = setBigDecimalValue(valueCustomer * rateValue);
                table_10.setValueAt(value, 0, i);
                sumNewCustomer = sumNewCustomer + value;
            }
            table_10.setValueAt(sumNewCustomer, 0, 13);
    }
    
    /**
     * event value cell table change.
     * @param table - table when edit value.
     * @param type - type of table.
     */
    private void changeDataTable(JTableEx table, int type) {
        int flag = 0;
        int flagtable15 = 0;
        int row = table.getEditingRow();
        int col = table.getEditingColumn();

        if (row < 0 || col < 0) {
            return;
        }
        //event table 1 && table 2
        if (type == 1 || type == 2) {
            Integer sum = 0;
            for (int i = 1; i < 13; i++) {
                sum = sum + Integer.parseInt(table.getValueAt(3, i).toString());
            }
            table.setValueAt(sum, 3, 13);

            // change table column ç∑äz
            Integer value = 0;
            if(type == 1)
            {
                value = Integer.parseInt(txttechnic.getText());
            }
            if(type == 2)
            {
                value = Integer.parseInt(txtSaleTarget.getText());
            }
            
            table.setValueAt(sum - value, 3, 14);

            //set value table 5
            flag = 1;
            //if type == 2 then set value table 18
            if (type == 2 && flagNextPage >= 3) {
                Integer sumRow = 0;
                Integer sumCol = 0;
                Integer totalSumRow = 0;
                Double valueTb2 = Double.parseDouble(table_2.getValueAt(3, col).toString());
                // column table 17 i = row -1 table 18
                //for (int i = 1; i < table_18.getRowCount(); i++) {
                for (int i = 1; i <= table_18.getRowCount(); i++) {
                    Double valueTb17 = Double.parseDouble(table_17.getValueAt(2, i).toString()) / 100;
                    //Integer valuetb18 = ((Double) Math.floor((valueTb2 * valueTb17) + 0.5)).intValue();
                    Integer valuetb18 = setBigDecimalValue((valueTb2 * valueTb17));
                    table_18.setValueAt(valuetb18, i - 1, col);

                    //set sum row table 18
                    sumRow = 0;
                    for (int j = 1; j < 13; j++) {
                        sumRow = sumRow + Integer.parseInt(table_18.getValueAt(i - 1, j).toString());
                    }
                    table_18.setValueAt(sumRow, i - 1, 13);
                }
                //IVS_LVTu start edit 2015/01/21 Mashu_ê›íËâÊñ 
                // sum column table 18 set value table_18_sum
                for (int i = 1; i < 13; i++) {
                    sumCol = 0;
                    //for (int j = 0; j < table_18.getRowCount() - 1; j++) {
                    for (int j = 0; j < table_18.getRowCount() ; j++) {
                        Integer valueTb18 = Integer.parseInt(table_18.getValueAt(j, i).toString());
                        sumCol = sumCol + valueTb18;
                    }
                    //table_18.setValueAt(sumCol, table_18.getRowCount() - 1, i);
                    table_18_sum.setValueAt(sumCol, 0, i);
                    totalSumRow = totalSumRow + sumCol;
                }
                //table_18.setValueAt(totalSumRow, table_18.getRowCount() - 1, 13);
                table_18_sum.setValueAt(totalSumRow, 0, 13);
                //IVS_LVTu end edit 2015/01/21 Mashu_ê›íËâÊñ 
            }
        }

        //event table 3 && table 4
        if (type == 3 || type == 4) {
            //if (type == 3 || (type == 4 && 1 < col)) {
                Double rateFemale = Double.parseDouble(table_3.getValueAt(1, 1).toString()) / 100;
                Double femaleUnitPrice = Double.parseDouble(table_4.getValueAt(1, 2).toString());
                Double maleUnitPrice = Double.parseDouble(table_4.getValueAt(1, 3).toString());
                //Integer value1 = ((Double) Math.floor((femaleUnitPrice * rateFemale) + (maleUnitPrice * (1 - rateFemale)))).intValue();
                Integer value1 = setBigDecimalValue((femaleUnitPrice * rateFemale) + (maleUnitPrice * (1 - rateFemale)));
                table_4.setValueAt(value1, 1, 1);
                //set value table 5
                flag = 1;
        }
        if (flag == 1) {
            setDataTable5();
            flag = 2;
            flagtable15 = 4;
        }
        //event table 9
        //if (type == 5 && col == 2 && row == 0 && flagNextPage >= 2) {
        if (type == 5 || (flag == 2 && flagNextPage >= 2)) {
            Double sumCustomer = Double.parseDouble(table_5.getValueAt(0, 13).toString());
            Double rateValue = Double.parseDouble(table_9.getValueAt(0, 2).toString()) / 100;

            Double value2 = (sumCustomer / 12) * rateValue;
            Integer average = setBigDecimalValue(value2);
            table_9.setValueAt(average, 1, 2);
            flag = 2;
        }

        //set value table 10
        //Integer sumNewCustomer = 0;
        if (flag == 2 && flagNextPage >= 2) {
            setDataTable10();
            flag = 3;
        }
                // set value table 11
        // sum column
        if (type == 6 || flag == 3) {
            if (type == 6) {
                //sum row
                Integer sumValueRow = 0;
                //for (int i = 0; i < table_11.getRowCount() - 2; i++) {
                for (int i = 0; i < table_11.getRowCount(); i++) {
                    sumValueRow = sumValueRow + Integer.parseInt(table_11.getValueAt(i, col).toString());
                }
                //IVS_LVTu start edit 2015/01/20 Mashu_ê›íËâÊñ 
                //table_11.setValueAt(sumValueRow, table_11.getRowCount() - 2, col);
                table_11_sum.setValueAt(sumValueRow, 0, col);
                //get value table 10 Å¶åéïΩãœêlêîÇÕîNä‘ãZèpãqñ⁄ïWÅÄ12Å~êVãKî‰ó¶Ç©ÇÁéZèo
                //Integer value_10 = Integer.parseInt(table_10.getValueAt(0, col).toString());
                //set value table 11-ç∑àŸ
                //table_11.setValueAt((sumValueRow - value_10), table_11.getRowCount() - 1, col);
                //table_11_sum.setValueAt((sumValueRow - value_10), 1, col);

                Integer totalSum = 0;
                Integer sumValueCol = 0;
                //for (int i = 1; i < table_11.getColumnCount() - 1; i++) {
                for (int i = 1; i < table_11.getColumnCount() - 2; i++) {
                    sumValueCol = sumValueCol + Integer.parseInt(table_11.getValueAt(row, i).toString());
                    //totalSum = totalSum + Integer.parseInt(table_11.getValueAt(table_11.getRowCount() - 2, i).toString());
                    // sum col table_11_sum row 0
                    totalSum = totalSum + Integer.parseInt(table_11_sum.getValueAt(0, i).toString());
                }
                //set data cell 13 table_11
                table_11.setValueAt(sumValueCol, row, 13);
                //table_11.setValueAt(totalSum, table_11.getRowCount() - 2, 13);
                //set data cell 13 table_11_sum
                table_11_sum.setValueAt(totalSum, 0, 13);
                flag = 3;
            }
            //set data cell table_11_sum
            if (flag == 3) {
                Integer sum = 0;
                Integer valuetb10 = 0;
                Integer valuetb11 = 0;
                for (int i = 1; i <= 12; i++) {
                    Integer value = 0;
                    valuetb10 = Integer.parseInt(table_10.getValueAt(0, i).toString());
                    //valuetb11 = Integer.parseInt(table_11.getValueAt(table_11.getRowCount() - 2, i).toString());
                    valuetb11 = Integer.parseInt(table_11_sum.getValueAt(0, i).toString());
                    value = valuetb11 - valuetb10;
                    //table_11.setValueAt(value, table_11.getRowCount() - 1, i);
                    table_11_sum.setValueAt(value, 1, i);
                    sum = sum + value;
                }
                //table_11.setValueAt(sum, table_11.getRowCount() - 1, 13);
                table_11_sum.setValueAt(sum, 1, 13);
                //IVS_LVTu end edit 2015/01/20 Mashu_ê›íËâÊñ 
            }
        }
        // event table 13 and event tabel 14 when cell table changed.
        //if (type == 7 || type == 8 && flagNextPage >= 3) {
        if (type == 7 || type == 8 ) {
            Double valuetb13 = 0d;
            valuetb13 = Double.parseDouble(table_13.getValueAt(1, col).toString()) / 100;
            Double valuetb14 = Double.parseDouble(Table_14.getValueAt(1, col).toString());
            Double valuetb5 = Double.parseDouble(table_5.getValueAt(0, 13).toString());
            //Integer value = ((Double) (Math.floor((valuetb5 / 12) * valuetb13 * valuetb14))).intValue();
            Integer value = setBigDecimalValue((valuetb5 / 12) * valuetb13 * valuetb14);

            table_15.setValueAt(value, 0, col);
            //sum table 15
            Integer sumtb15 = 0;
            for (int i = 1; i < table_15.getColumnCount() - 1; i++) {
                sumtb15 = sumtb15 + Integer.parseInt(table_15.getValueAt(0, i).toString());
            }
            table_15.setValueAt(sumtb15, 0, table_15.getColumnCount() - 1);
            //set cell(0,1) table_16
            table_16.setValueAt(sumtb15, 0, 1);
            Integer valueTarget = 0;
            Integer valueExpected = 0;
            valueTarget     = Integer.parseInt(table_16.getValueAt(0, 0).toString());
            valueExpected   = Integer.parseInt(table_16.getValueAt(0, 1).toString());
            table_16.setValueAt(valueExpected - valueTarget, 0, 2);
        }
        //set all cell table_15
        if (flagtable15 == 4 && flagNextPage >= 3) {
            Integer sum = 0;
            for (int i = 1; i < table_15.getColumnCount() - 1; i++) {
                Double valuetb13 = Double.parseDouble(table_13.getValueAt(1, i).toString()) / 100;
                Double valuetb14 = Double.parseDouble(Table_14.getValueAt(1, i).toString());
                Double valuetb5 = Double.parseDouble(table_5.getValueAt(0, 13).toString());
                //Integer value = ((Double) (Math.floor(valuetb5 / 12) * valuetb13 * valuetb14)).intValue();
                Integer value = setBigDecimalValue((valuetb5 / 12) * valuetb13 * valuetb14);
                table_15.setValueAt(value, 0, i);
                sum = sum + value;
            }
            table_15.setValueAt(sum, 0, table_15.getColumnCount() - 1);
            
            //set cell(0,1) table_16
            table_16.setValueAt(sum, 0, 1);
            Integer valueTarget = 0;
            Integer valueExpected = 0;
            valueTarget     = Integer.parseInt(table_16.getValueAt(0, 0).toString());
            valueExpected   = Integer.parseInt(table_16.getValueAt(0, 1).toString());
            table_16.setValueAt(valueExpected - valueTarget, 0, 2);
            
        }
        //event table 17
        //if (type == 9 && flagNextPage >= 3) {
        if (type == 9) {
            //Double valuetb13 = 0d;

            int sumTb17 = 0;
            int sumtemp = 0;
            for (int i = 1; i < table_17.getColumnCount(); i++) {
                sumTb17 = sumTb17 + Integer.parseInt(table_17.getValueAt(2, i).toString());
                if (i != col) {
                    sumtemp = sumtemp + Integer.parseInt(table_17.getValueAt(2, i).toString());
                }
            }
            if (sumTb17 > 100) {
                table_17.setValueAt(100 - sumtemp, 2, col);
            }

            Double valuetb17 = Double.parseDouble(table_17.getValueAt(row, col).toString()) / 100;
            Integer sumRow = 0;
            // column table 17 = row table 18 + 1
            Integer rowtb18 = col - 1;
            for (int i = 1; i < 13; i++) {
                Double valuetb2 = Double.parseDouble(table_2.getValueAt(3, i).toString());
                //Integer value = ((Double) Math.floor((valuetb2 * valuetb17) + 0.5)).intValue();
                Integer value = setBigDecimalValue((valuetb2 * valuetb17));
                sumRow = sumRow + value;

                table_18.setValueAt(value, rowtb18, i);
            }
            table_18.setValueAt(sumRow, rowtb18, 13);
            // column table 18
            //IVS_LVTu start edit 2015/01/21 Mashu_ê›íËâÊñ 
            Integer totalSumColTb18 = 0;
            for (int i = 1; i < 13; i++) {
                Integer sumColtb18 = 0;
                //for (int j = 0; j < table_18.getRowCount() - 1; j++) {
                for (int j = 0; j < table_18.getRowCount() ; j++) {
                    sumColtb18 = sumColtb18 + Integer.parseInt(table_18.getValueAt(j, i).toString());
                }
                //table_18.setValueAt(sumColtb18, table_18.getRowCount() - 1, i);
                table_18_sum.setValueAt(sumColtb18, 0, i);
                totalSumColTb18 = totalSumColTb18 + sumColtb18;
            }
            //table_18.setValueAt(totalSumColTb18, table_18.getRowCount() - 1, 13);
            table_18_sum.setValueAt(totalSumColTb18, 0, 13);
            //IVS_LVTu end edit 2015/01/21 Mashu_ê›íËâÊñ 
        }
    }
    
    /**
     * top cell all table
     */
    public void stopCellTableAll()
    {
        if(table_1.isEditing())
        {
            table_1.getCellEditor().stopCellEditing();
        }
        if(table_2.isEditing())
        {
            table_2.getCellEditor().stopCellEditing();
        }
        if(table_3.isEditing())
        {
            table_3.getCellEditor().stopCellEditing();
        }
        if(table_4.isEditing())
        {
            table_4.getCellEditor().stopCellEditing();
        }
        if(table_7.isEditing())
        {
            table_7.getCellEditor().stopCellEditing();
        }
        if(table_8.isEditing())
        {
            table_8.getCellEditor().stopCellEditing();
        }
        if(table_9.isEditing())
        {
            table_9.getCellEditor().stopCellEditing();
        }
        if(table_11.isEditing())
        {
            table_11.getCellEditor().stopCellEditing();
        }
        if(table_12.isEditing())
        {
            table_12.getCellEditor().stopCellEditing();
        }
        if(table_13.isEditing())
        {
            table_13.getCellEditor().stopCellEditing();
        }
        if(Table_14.isEditing())
        {
            Table_14.getCellEditor().stopCellEditing();
        }
        if(table_17.isEditing())
        {
            table_17.getCellEditor().stopCellEditing();
        }
        if(tbTargetItem.isEditing())
        {
            tbTargetItem.getCellEditor().stopCellEditing();
        }
        if(tbKarte.isEditing())
        {
            tbKarte.getCellEditor().stopCellEditing();
        }
    }

    /**
     * ì¸óÕÉ`ÉFÉbÉNÇçsÇ§ÅB
     *
     * @return ì¸óÕÉGÉâÅ[Ç™Ç»ÇØÇÍÇŒtrueÇï‘Ç∑ÅB
     */
    private boolean checkInput() {
        if (Integer.parseInt(table_1.getValueAt(3, 14).toString()) < 0
                || Integer.parseInt(table_2.getValueAt(3, 14).toString()) < 0) {
            MessageDialog.showMessageDialog(parentFrame,
                    "îNä‘ñ⁄ïWÇ…ëŒÇµÇƒåéñ⁄ïWÇÃçáåvÇ™â∫âÒÇ¡ÇƒÇ¢Ç‹Ç∑ÅB\n" +
                    "ç∑àŸóìÇ™0â~à»è„Ç…Ç»ÇÈÇÊÇ§ñ⁄ïWê›íËÇÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
            
        }
        if(step == 1 && Integer.parseInt(table_11_sum.getValueAt(1, 13).toString()) < 0)
        {
            MessageDialog.showMessageDialog(parentFrame,
                    "î}ëÃï êVãKñ⁄ïWê›íËÇÕç∑àŸÇ™0à»è„Ç…Ç»ÇÈÇÊÇ§ê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // check sum ÅÉè§ïiï î‰ó¶ñ⁄ïWê›íËÅÑ.ñ⁄ïWî‰ó¶
        int sumRate = 0;
        for( int i = 1;i < table_17.getColumnCount();i ++)
        {
            sumRate = sumRate + Integer.parseInt(table_17.getValueAt(2, i).toString());
        }
        if(step == 2 && sumRate < 100)
        {
            MessageDialog.showMessageDialog(parentFrame,
                    "è§ïiï ÇÃñ⁄ïWî‰ó¶ÇÕçáåvÇ≈100%Ç…Ç»ÇÈÇÊÇ§ê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    //IVS_LVTu start add 2015/01/22 Task #35026
    /**
     * get checkbox
     */
    private JCheckBox getItemCheckbox(boolean isCheck) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(isCheck);
        checkBox.setOpaque(false);
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            //    to do.
            }
        });
        return checkBox;
    }
    //IVS_LVTu end add 2015/01/22 Task #35026
    /**
     * ì¸óÕÇ≥ÇÍÇΩÉfÅ[É^Çìoò^Ç∑ÇÈÅB
     *
     * @return true - ê¨å˜
     */
    private boolean regist() {
        boolean result = false;
        try {
            MstDataTarget mdt = new MstDataTarget();
            DataTargetItem dti = new DataTargetItem();
            DataTargetMotive dtm = new DataTargetMotive();
            DataTargetTechnic dtt = new DataTargetTechnic();

            int tempMonth = 1;
            int tempYear = year;
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            if (periodMonth !=12 ) {
                tempMonth = periodMonth + 1;
                tempYear = year;
            }
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            mdt.setShopId(shopId);
            mdt.setShopCategory(shopCategory);
            ConnectionWrapper con = SystemInfo.getConnection();
            try {
                con.begin();
                //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
                boolean flag = false;
                for (int i = 1; i <= 12; i++) {
                    mdt.setMonth(tempMonth);
                    //if (periodMonth == 3 && i == 9) {
                    if (periodMonth != 12 && periodMonth + i > 12 && !flag ) {
                        tempYear++;
                        flag = true;
                    }
                    //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
                    mdt.setYear(tempYear);
                    mdt.setTechnic(Integer.parseInt(table_1.getValueAt(3, i).toString()));
                    mdt.setTechnicItem(Integer.parseInt(table_2.getValueAt(3, i).toString()));
                    mdt.setFemaleRate(Double.parseDouble(table_3.getValueAt(1, 1).toString()) * 1.0 / 100);
                    mdt.setUnitPrice(Integer.parseInt(table_4.getValueAt(1, 1).toString()));
                    mdt.setFemaleUnitPirce(Integer.parseInt(table_4.getValueAt(1, 2).toString()));
                    mdt.setMaleUnitPrice(Integer.parseInt(table_4.getValueAt(1, 3).toString()));
                    mdt.setCustomer(Integer.parseInt(table_5.getValueAt(0, i).toString()));
                    //IVS_LVTu start edit 2015/01/23 Task #35026
                    mdt.setRepert90New(Double.parseDouble(table_7.getValueAt(1, 1).toString()) * 1.0 / 100);
                    mdt.setRepert90Semifix(Double.parseDouble(table_7.getValueAt(2, 1).toString()) * 1.0 / 100);
                    mdt.setRepert90Fix(Double.parseDouble(table_7.getValueAt(3, 1).toString()) * 1.0 / 100);
                    mdt.setRepert120New(Double.parseDouble(table_7.getValueAt(1, 2).toString()) * 1.0 / 100);
                    mdt.setRepert120Semifix(Double.parseDouble(table_7.getValueAt(2, 2).toString()) * 1.0 / 100);
                    mdt.setRepert120Fix(Double.parseDouble(table_7.getValueAt(3, 2).toString()) * 1.0 / 100);
                    mdt.setRepert180New(Double.parseDouble(table_7.getValueAt(1, 3).toString()) * 1.0 / 100);
                    mdt.setRepert180Semifix(Double.parseDouble(table_7.getValueAt(2, 3).toString()) * 1.0 / 100);
                    mdt.setRepert180Fix(Double.parseDouble(table_7.getValueAt(3, 3).toString()) * 1.0 / 100);
                    //IVS_LVTu end edit 2015/01/23 Task #35026
                    mdt.setBeforeReserve(Double.parseDouble(table_8.getValueAt(1, 1).toString()) * 1.0 / 100);
                    mdt.setNextReserve(Double.parseDouble(table_8.getValueAt(1, 2).toString()) * 1.0 / 100);
                    mdt.setNewRate(Double.parseDouble(table_9.getValueAt(0, 2).toString()) * 1.0 / 100);
                    mdt.setNewCustomer(Integer.parseInt(table_10.getValueAt(0, i).toString()));
                    mdt.setDecyl1Rate(Integer.parseInt(table_12.getValueAt(0, 4).toString()));
                    mdt.setDecyl1Num(Integer.parseInt(table_12.getValueAt(0, 6).toString()));
                    mdt.setDecyl2Rate(Integer.parseInt(table_12.getValueAt(1, 4).toString()));
                    mdt.setDecyl2Num(Integer.parseInt(table_12.getValueAt(1, 6).toString()));
                    mdt.setDecyl3Rate(Integer.parseInt(table_12.getValueAt(2, 4).toString()));
                    mdt.setDecyl3Num(Integer.parseInt(table_12.getValueAt(2, 6).toString()));
                    
                    //IVS_LVTu start add 2015/01/23 Task #35026
                    mdt.setItem_sales_rate(Double.parseDouble(tbTargetItem.getValueAt(0, i).toString()) * 1.0/100);
                    mdt.setItem_sales_price(Integer.parseInt(tbTargetItem.getValueAt(1, i).toString()));
                    mdt.setRepeat_90_netnew(Double.parseDouble(table_7.getValueAt(0, 1).toString()) * 1.0 / 100);
                    mdt.setRepeat_120_netnew(Double.parseDouble(table_7.getValueAt(0, 2).toString()) * 1.0 / 100);
                    mdt.setRepeat_180_netnew(Double.parseDouble(table_7.getValueAt(0, 3).toString()) * 1.0 / 100);
                    //IVS_LVTu end add 2015/01/23 Task #35026

                    mdt.setKarte1_day(Integer.parseInt(tbKarte.getValueAt(0, 1).toString()));
                    mdt.setKarte2_day(Integer.parseInt(tbKarte.getValueAt(0, 2).toString()));
                    mdt.setKarte3_day(Integer.parseInt(tbKarte.getValueAt(0, 3).toString()));
                    mdt.setKarte4_day(Integer.parseInt(tbKarte.getValueAt(0, 4).toString()));
                    
                    mdt.setKarte1_value(Integer.parseInt(tbKarte.getValueAt(1, 1).toString()));
                    mdt.setKarte2_value(Integer.parseInt(tbKarte.getValueAt(1, 2).toString()));
                    mdt.setKarte3_value(Integer.parseInt(tbKarte.getValueAt(1, 3).toString()));
                    mdt.setKarte4_value(Integer.parseInt(tbKarte.getValueAt(1, 4).toString()));
                    result = mdt.regist(con);
                    if (!result) {
                        break;
                    }

                    // DataTargetMotive
                    dtm.setShopId(shopId);
                    dtm.setShopCategoryId(shopCategory);
                    dtm.setMonth(tempMonth);
                    dtm.setYear(tempYear);
                    //IVS_LVTu start add 2015/01/22 Task #35026
                    //get value checkbox
                    DefaultTableModel model = (DefaultTableModel) table_11.getModel();
                    // cb.isSelected() == false then valueOwn_flg = 0 else valueOwn_flg = 1
                    Integer valueOwn_flg = 0;
                    
                    for (int j = 0; j < arrmfcm.size(); j++) {
                        dtm.setMotiveId(arrmfcm.get(j).getFirstComingMotiveClassId());
                        dtm.setNum(Integer.parseInt(table_11.getValueAt(j, i).toString()));
                        
                        JCheckBox cb = (JCheckBox) model.getValueAt(j, 14);
                        if (cb.isSelected()) {
                            valueOwn_flg = 1;
                        }
                        else
                        {
                            valueOwn_flg = 0;
                        }
                        
                        dtm.setOwn_flg(valueOwn_flg);
                        result = dtm.regist(con);
                        if (!result) {
                            break;
                        }
                    }
                    //IVS_LVTu end add 2015/01/22 Task #35026
                    if (!result) {
                        break;
                    }
                    //DataTargetTechnic
                    dtt.setShopId(shopId);
                    dtt.setShopCategoryId(shopCategory);
                    dtt.setMonth(tempMonth);
                    dtt.setYear(tempYear);
                    for (int j = 0; j < dts.getSetTargetMenu().size(); j++) {
                        dtt.setTechnicClassID(dts.getSetTargetMenu().get(j).technicClassId);
                        dtt.setTechnicRate(Double.parseDouble(table_13.getValueAt(1, j + 1).toString()) / 100);
                        dtt.setTechnicValue(Integer.parseInt(Table_14.getValueAt(1, j + 1).toString()));
                        result = dtt.regist(con);
                        if (!result) {
                            break;
                        }
                    }
                    if (!result) {
                        break;
                    }
                    //DataTargetItem
                    dti.setShopId(shopId);
                    dti.setShopCategoryId(shopCategory);
                    dti.setMonth(tempMonth);
                    dti.setYear(tempYear);
                    for (int j = 0; j < dts.getSetProductTarget().size(); j++) {
                        dti.setItemcClassId(dts.getSetProductTarget().get(j).itemClassId);
                        dti.setRate(Double.parseDouble(table_17.getValueAt(2, j + 1).toString()) / 100);
                        dti.setValue(Integer.parseInt(table_18.getValueAt(j , i).toString()));
                        result = dti.regist(con);
                        if (!result) {
                            break;
                        }
                    }
                    tempMonth = (tempMonth >= 12) ? 1 : ++tempMonth;
                }
                if (result == false) {
                    con.rollback();
                } else {
                    con.commit();
                }
            } catch (SQLException e) {
                con.rollback();
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    /**
     * init table.
     */
    private void initTableColumn() {
        //set width column table
        table_1.getColumnModel().getColumn(0).setMinWidth(90);
        table_1.getColumnModel().getColumn(0).setMaxWidth(90);
        table_2.getColumnModel().getColumn(0).setMinWidth(90);
        table_2.getColumnModel().getColumn(0).setMaxWidth(90);
        table_3.getColumnModel().getColumn(0).setMinWidth(90);
        table_3.getColumnModel().getColumn(0).setMaxWidth(90);
        table_4.getColumnModel().getColumn(0).setMinWidth(90);
        table_4.getColumnModel().getColumn(0).setMaxWidth(90);
        table_5.getColumnModel().getColumn(0).setMinWidth(90);
        table_5.getColumnModel().getColumn(0).setMaxWidth(90);
        tbTargetItem.getColumnModel().getColumn(0).setMinWidth(90);
        tbTargetItem.getColumnModel().getColumn(0).setMaxWidth(90);
        
    
        if(flagNextPage == 2)
        {
            table_6.getColumnModel().getColumn(0).setMinWidth(120);
            table_6.getColumnModel().getColumn(0).setMaxWidth(120);
            table_7.getColumnModel().getColumn(0).setMinWidth(120);
            table_7.getColumnModel().getColumn(0).setMaxWidth(120);
            table_8.getColumnModel().getColumn(0).setMinWidth(90);
            table_8.getColumnModel().getColumn(0).setMaxWidth(90);
            table_9.getColumnModel().getColumn(0).setMinWidth(90);
            table_9.getColumnModel().getColumn(0).setMaxWidth(90);
            table_10.getColumnModel().getColumn(0).setMinWidth(90);
            table_10.getColumnModel().getColumn(0).setMaxWidth(90);
            table_11.getColumnModel().getColumn(0).setPreferredWidth(100);
            table_12.getColumnModel().getColumn(0).setMaxWidth(90);
            table_12.getColumnModel().getColumn(0).setMinWidth(90);
            //IVS_LVTu start add 2015/01/20 Mashu_ê›íËâÊñ 
            
            table_11.getTableHeader().setResizingAllowed(false);
            table_11_sum.getColumnModel().getColumn(0).setPreferredWidth(100);
            
            TableColumnModel tableModel11_sum = table_11_sum.getColumnModel();
            tableModel11_sum.getColumn(1).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(2).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(3).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(4).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(5).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(6).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(7).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(8).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(9).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(10).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(11).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(12).setCellRenderer(new MinusCellRedRenderer());
            tableModel11_sum.getColumn(13).setCellRenderer(new MinusCellRedRenderer());

            //IVS_LVTu end add 2015/01/20 Mashu_ê›íËâÊñ 
        }
        
        if(flagNextPage == 3)
        {
            table_13.getColumnModel().getColumn(0).setMinWidth(90);
            table_13.getColumnModel().getColumn(0).setMaxWidth(90);
            Table_14.getColumnModel().getColumn(0).setMinWidth(90);
            Table_14.getColumnModel().getColumn(0).setMaxWidth(90);
            table_15.getColumnModel().getColumn(0).setMinWidth(90);
            table_15.getColumnModel().getColumn(0).setMaxWidth(90);
            table_16.getColumnModel().getColumn(0).setMinWidth(90);
            table_16.getColumnModel().getColumn(0).setMaxWidth(90);
            table_17.getColumnModel().getColumn(0).setMinWidth(90);
            table_17.getColumnModel().getColumn(0).setMaxWidth(90);
            table_18.getColumnModel().getColumn(0).setPreferredWidth(100);
            //IVS_LVTu start add 2015/01/20 Mashu_ê›íËâÊñ 
            table_18.getTableHeader().setResizingAllowed(false);
            table_18_sum.getColumnModel().getColumn(0).setPreferredWidth(100);
            //IVS_LVTu end add 2015/01/20 Mashu_ê›íËâÊñ 
        }
                //set color cell table
        //page 1
        new SettingTableCellRenderer(1).setSelectTableCellRenderer(table_1);
        new SettingTableCellRenderer(2).setSelectTableCellRenderer(table_2);
        new SettingTableCellRenderer(3).setSelectTableCellRenderer(table_3);
        new SettingTableCellRenderer(4).setSelectTableCellRenderer(table_4);
        new SettingTableCellRenderer(5).setSelectTableCellRenderer(table_5);
        //page 2
        new SettingTableCellRenderer(6).setSelectTableCellRenderer(table_6);
        new SettingTableCellRenderer(7).setSelectTableCellRenderer(table_7);
        new SettingTableCellRenderer(8).setSelectTableCellRenderer(table_8);
        new SettingTableCellRenderer(9).setSelectTableCellRenderer(table_9);
        new SettingTableCellRenderer(10).setSelectTableCellRenderer(table_10);
        new SettingTableCellRenderer(11).setSelectTableCellRenderer(table_11);
        new SettingTableCellRenderer(12).setSelectTableCellRenderer(table_12);
        //page 3
        new SettingTableCellRenderer(13).setSelectTableCellRenderer(table_13);
        new SettingTableCellRenderer(14).setSelectTableCellRenderer(Table_14);
        new SettingTableCellRenderer(15).setSelectTableCellRenderer(table_15);
        new SettingTableCellRenderer(16).setSelectTableCellRenderer(table_16);
        new SettingTableCellRenderer(17).setSelectTableCellRenderer(table_17);
        new SettingTableCellRenderer(18).setSelectTableCellRenderer(table_18);
        
        //IVS_LVTu start add 2015/01/20 Mashu_ê›íËâÊñ 
        new SettingTableCellRenderer(19).setSelectTableCellRenderer(table_11_sum);
        new SettingTableCellRenderer(20).setSelectTableCellRenderer(table_18_sum);
        new SettingTableCellRenderer(21).setSelectTableCellRenderer(tbTargetItem);
        new SettingTableCellRenderer(22).setSelectTableCellRenderer(tbKarte);
        //IVS_LVTu start add 2015/01/20 Mashu_ê›íËâÊñ 
        // set edit cell table
        TableColumnModel tableModel13 = table_13.getColumnModel();
        for (int i = 1; i <= countCol; i++) {
            table_13.getColumnModel().getColumn(i).setPreferredWidth(100);
            tableModel13.getColumn(i).setCellEditor(new CustomCellEditor(new JTextField(), 3, 2));
        }

        TableColumnModel tableModel14 = Table_14.getColumnModel();
        for (int i = 1; i <= countCol; i++) {
            Table_14.getColumnModel().getColumn(i).setPreferredWidth(100);
            tableModel14.getColumn(i).setCellEditor(new CustomCellEditor(new JTextField(), 9, 1));
        }

        for (int i = 1; i <= countCol; i++) {
            table_15.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        TableColumnModel tableModel17 = table_17.getColumnModel();
        for (int i = 1; i <= countCol1; i++) {
            table_17.getColumnModel().getColumn(i).setPreferredWidth(100);
            tableModel17.getColumn(i).setCellEditor(new CustomCellEditor(new JTextField(), 3, 2));
        }
    }

    /**
     * É{É^ÉìÇ…É}ÉEÉXÉJÅ[É\ÉãÇ™èÊÇ¡ÇΩÇ∆Ç´Ç…ÉJÅ[É\ÉãÇïœçXÇ∑ÇÈÉCÉxÉìÉgÇí«â¡Ç∑ÇÈÅB
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btBack);
        SystemInfo.addMouseCursorChange(btNext);
        SystemInfo.addMouseCursorChange(btRegist);
    }

    /**
     * ÉRÉìÉ|Å[ÉlÉìÉgÇÃäeÉäÉXÉiÅ[ÇÉZÉbÉgÇ∑ÇÈÅB
     */
    private void setListener() {
        txttechnic.addKeyListener(SystemInfo.getMoveNextField());
        txttechnic.addFocusListener(SystemInfo.getSelectText());
        txtSaleTarget.addKeyListener(SystemInfo.getMoveNextField());
        txtSaleTarget.addFocusListener(SystemInfo.getSelectText());
    }
    
    public class SettingTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * ÉZÉãÇÃí[Ç∆ÉeÉLÉXÉgÇÃÉ}Å[ÉWÉì
         */
        private static final int SIDE_MARGIN = 4;
        private Object value = null;
        /**
         * ëIëÇ≥ÇÍÇƒÇ¢ÇÈÇ©Ç«Ç§Ç©
         */
        private boolean isSelected = false;
        /**
         * ëIëéûÇÃêF
         */
        private Color selectedRowColor = null;
        /**
         * âeÇÃêFÇO
         */
        private Color shadow0Color = null;
        /**
         * âeÇÃêFÇP
         */
        private Color shadow1Color = null;
        /**
         * ÉnÉCÉâÉCÉgÇÃêF
         */
        private Color highlightColor = null;

        private int rRow;
        private int rCol;
        private int type;

        private Color cellBackGroundColor = Color.WHITE;

        public Color getCellBackGroundColor() {
            return cellBackGroundColor;
        }

        public void setCellBackGroundColor(Color cellBackGroundColor) {
            this.cellBackGroundColor = cellBackGroundColor;
        }
        private Color Colors[][];

        public Color[][] getColors() {
            return Colors;
        }

        public void setColors(Color[][] Colors) {
            this.Colors = Colors;
        }

        /**
         * ÉRÉìÉXÉgÉâÉNÉ^
         */
        public SettingTableCellRenderer(int type) {
            super();
            this.type = type;
//            if(this.type == 0)
//            {
//                setSelectedRowColor(new Color(204, 204, 204));
//            }
//            else
//            {
//                setSelectedRowColor(new Color(255, 210, 142));
//            }
            setSelectedRowColor(new Color(255, 210, 142));
            setShadow0Color(new Color(113, 113, 113));
            setShadow1Color(new Color(172, 172, 172));
            setHighlightColor(new Color(241, 241, 241));
        }

        public String getText() {
            if (super.getText() == null) {
                return "";
            } else {
                return super.getText();
            }
        }

        /**
         * ëIëéûÇÃêFÇéÊìæÇ∑ÇÈÅB
         *
         * @return ëIëéûÇÃêF
         */
        public Color getSelectedRowColor() {
            return selectedRowColor;
        }

        /**
         * ëIëéûÇÃêFÇê›íËÇ∑ÇÈÅB
         *
         * @param selectedRowColor ëIëéûÇÃêF
         */
        public void setSelectedRowColor(Color selectedRowColor) {
            this.selectedRowColor = selectedRowColor;
        }

        /**
         * âeÇÃêFÇOÇéÊìæÇ∑ÇÈÅB
         *
         * @return âeÇÃêFÇO
         */
        public Color getShadow0Color() {
            return shadow0Color;
        }

        /**
         * âeÇÃêFÇOÇê›íËÇ∑ÇÈÅB
         *
         * @param shadow0Color âeÇÃêFÇO
         */
        public void setShadow0Color(Color shadow0Color) {
            this.shadow0Color = shadow0Color;
        }

        /**
         * âeÇÃêFÇPÇéÊìæÇ∑ÇÈÅB
         *
         * @return âeÇÃêFÇP
         */
        public Color getShadow1Color() {
            return shadow1Color;
        }

        /**
         * âeÇÃêFÇPÇê›íËÇ∑ÇÈÅB
         *
         * @param shadow1Color âeÇÃêFÇP
         */
        public void setShadow1Color(Color shadow1Color) {
            this.shadow1Color = shadow1Color;
        }

        /**
         * ÉnÉCÉâÉCÉgÇÃêFÇéÊìæÇ∑ÇÈÅB
         *
         * @return ÉnÉCÉâÉCÉgÇÃêF
         */
        public Color getHighlightColor() {
            return highlightColor;
        }

        /**
         * ÉnÉCÉâÉCÉgÇÃêFÇê›íËÇ∑ÇÈÅB
         *
         * @param highlightColor ÉnÉCÉâÉCÉgÇÃêF
         */
        public void setHighlightColor(Color highlightColor) {
            this.highlightColor = highlightColor;
        }

        /**
         * ÉeÅ[ÉuÉãÉZÉãÉåÉìÉ_ÉäÉìÉOÇï‘ÇµÇ‹Ç∑ÅB
         *
         * @param table JTable
         * @param value ÉZÉãÇ…äÑÇËìñÇƒÇÈíl
         * @param isSelected ÉZÉãÇ™ëIëÇ≥ÇÍÇƒÇ¢ÇÈèÍçáÇÕ true
         * @param hasFocus ÉtÉHÅ[ÉJÉXÇ™Ç†ÇÈèÍçáÇÕ true
         * @param row çs
         * @param column óÒ
         * @return ÉeÅ[ÉuÉãÉZÉãÉåÉìÉ_ÉäÉìÉO
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            this.value = value;
            this.isSelected = isSelected;
            rRow = row;
            rCol = column;
            super.setForeground((isSelected ? table.getSelectionForeground() : table.getForeground()));
            //super.setBackground((isSelected ? table.getSelectionBackground() : table.getBackground()));

            return this;
        }

        /**
         * êîílÇ©Ç«Ç§Ç©Çê›íËÇ∑ÇÈÅB
         *
         * @param value îªíËÇ∑ÇÈíl
         */
        private boolean isNumeric() {
            if (value instanceof Integer
                    || value instanceof Byte
                    || value instanceof Short
                    || value instanceof Long
                    || value instanceof Float
                    || value instanceof Double) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * è¨êîÇ©Ç«Ç§Ç©Çê›íËÇ∑ÇÈÅB
         *
         * @param value îªíËÇ∑ÇÈíl
         */
        private boolean isDecimal() {
            if (value instanceof Float
                    || value instanceof Double) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * ï`âÊèàóùÇçsÇ§ÅB
         *
         * @param g Graphics
         */
        public void paint(Graphics g) {
            //ëIëÇ≥ÇÍÇƒÇ¢ÇÈèÍçáÅAâöÇÒÇ≈Ç¢ÇÈÇÊÇ§Ç…îwåiÇï`âÊÇ∑ÇÈ
            if (isSelected) {
                g.setColor(selectedRowColor);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.setColor(shadow0Color);
                g.drawLine(0, 0, 0, this.getHeight() - 1);
                g.drawLine(0, 0, this.getWidth() - 1, 0);
                g.setColor(shadow1Color);
                g.drawLine(1, 1, 1, this.getHeight() - 2);
                g.drawLine(1, 1, this.getWidth() - 2, 1);
                g.setColor(highlightColor);
                g.drawLine(this.getWidth() - 1, 1, this.getWidth() - 1, this.getHeight() - 1);
                g.drawLine(1, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1);
            } //ëIëÇ≥ÇÍÇƒÇ¢Ç»Ç¢èÍçá
            else {

                g.setColor(Color.white);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            switch (this.type) {
                case 1:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rRow == 3 && rCol > 0 && rCol <= 12) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } //else khi muon set gia tri ben phai hay trai cua column.
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 2:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rRow == 3 && rCol > 0 && rCol <= 12) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());

                    }
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 3:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rCol == 1 && rRow == 1) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    break;
                case 4:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (1 < rCol && rRow == 1) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    break;
                case 5:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    break;
                case 6:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } 
                    break;
                case 7:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (0 < rCol && rCol < 4) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    break;
                case 8:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (0 < rCol && rCol < 3 && rRow == 1) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    break;
                case 9:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rCol == 2 && rRow == 0) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    break;
                case 10:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    break;
                case 11:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (0 < rCol && rCol < 13 && rRow < countRow) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    break;
                case 12:
                    if(rCol == 0)
                    {
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rCol == 4 || rCol == 6) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    else
                    {
                        if(rCol != 0)
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 13:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rRow == 1 && 0 < rCol) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 14:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rRow == 1 && 0 < rCol) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 15:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    break;
                case 17:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (rRow == 2 && 0 < rCol) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 18:
                    if (rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    break;
                //IVS_LVTu start add 2015/01/20 Mashu_ê›íËâÊñ     
                case 19:
                    if(rCol == 0 || rCol == 14) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 20:
                    if(rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                case 21:
                    if(rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (0 < rCol) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                //IVS_LVTu end add 2015/01/20 Mashu_ê›íËâÊñ 
                case 22:
                    if(rCol == 0) {
                        g.setColor(new Color(204, 204, 204));
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    if (0 < rCol) {
                        g.setColor(selectedRowColor);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    } 
                    if(rCol != 0){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    break;
                default:
                    break;
            }

            g.setColor(Color.black);

            String temp = this.getText();

            if (this.isNumeric()) {
                if (this.isDecimal()) {
                    temp = String.format("%1$,.2f", value);
                } else {
                    temp = String.format("%1$,d", value);
                }
            }

            int baseX = 0;
            Rectangle2D r2d = this.getFont().getStringBounds(temp,
                    new FontRenderContext(new AffineTransform(), true, false));

            switch (this.getHorizontalAlignment()) {
                case SwingConstants.LEADING:
                    if (this.isNumeric()) {
                        baseX = this.getWidth() - ((Double) r2d.getWidth()).intValue() - SIDE_MARGIN;
                    } else if (isDateTime(this.getText()) || isPostalCode(this.getText())) {
                        baseX = (this.getWidth() - ((Double) r2d.getWidth()).intValue()) / 2;
                    } else {
                        baseX = SIDE_MARGIN;
                    }
                    break;
                case SwingConstants.LEFT:
                    baseX = SIDE_MARGIN;
                    break;
                case SwingConstants.CENTER:
                    baseX = (this.getWidth() - ((Double) r2d.getWidth()).intValue()) / 2;
                    break;
                case SwingConstants.RIGHT:
                    baseX = this.getWidth() - ((Double) r2d.getWidth()).intValue() - SIDE_MARGIN;
                    break;
            }

            int baseY = -1;

            switch (this.getVerticalAlignment()) {
                case SwingConstants.TOP:
                    baseY += this.getFont().getSize();
                    break;
                case SwingConstants.CENTER:
                    baseY += (this.getHeight() + this.getFont().getSize()) / 2;
                    break;
                case SwingConstants.BOTTOM:
                    baseY += this.getHeight();
                    break;
            }

            g.setColor(this.getForeground());

            g.drawString(temp, baseX + (isSelected ? 1 : 0), baseY + (isSelected ? 1 : 0));
        }

        private boolean isDateTime(String value) {
            return value.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")
                    || value.matches("[0-9]{4}/[0-9]{2}")
                    || value.matches("[0-9]{2}:[0-9]{2}");
        }

        private boolean isPostalCode(String value) {
            return value.matches("[0-9]{3}-[0-9]{4}");
        }

        /**
         * tableÇ…SelectedTableCellRendererÇê›íËÇµÇ‹Ç∑ÅB
         *
         * @param table JTable
         */
        public void setSelectTableCellRenderer(JTable table) {
            table.setForeground(Color.black);

            table.setDefaultRenderer(Byte.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(Short.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(Integer.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(Long.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(Float.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(Double.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(String.class, new SettingTableCellRenderer(type));
            table.setDefaultRenderer(Object.class, new SettingTableCellRenderer(type));
        }
    }

    public class CustomCellEditor extends DefaultCellEditor {

        JTextField textField = new JTextField();
        Integer type = 0;
        int row = 0;

        public CustomCellEditor(JTextField field, int maxLen, int type) {
            super(field);
            this.textField = field;
            this.type = type;
            textField.setHorizontalAlignment(JTextField.RIGHT);
            ((PlainDocument) textField.getDocument()).setDocumentFilter(
                    new CustomFilter(maxLen, CustomFilter.NUMBER));
            
            textField.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    textField.selectAll();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            // TextFieldÇ…ì‡óeÇ™Ç†ÇÈèÍçáÇÕÅAÇªÇÃÇ‹Ç‹Ç∆Ç∑ÇÈ
            if (value != null) {
                textField.setText(value.toString());
            } else {
                textField.setText("");
            }
            //IVS_LVTu start add 2015/01/26 Task #35026
            // Use only table tbTargetItem
            this.row = row;
            if(type == 3 && row == 0)
            {
                textField.setHorizontalAlignment(JTextField.RIGHT);
                ((PlainDocument) textField.getDocument()).setDocumentFilter(
                    new CustomFilter(3, CustomFilter.NUMBER));
            }
            else if(type == 3 && row == 1)
            {
                textField.setHorizontalAlignment(JTextField.RIGHT);
                ((PlainDocument) textField.getDocument()).setDocumentFilter(
                    new CustomFilter(9, CustomFilter.NUMBER));
            }
            //IVS_LVTu end edit 2015/01/26 Task #35026
            textField.selectAll();
            return textField;
        }

        public Integer getCellEditorValue() {
            if (textField.getText().equals("")) {
                return 0;
            } else {
                //type = 1 max length = 9
                if(type == 1)
                {
                    return Integer.parseInt(textField.getText());
                }
                //type = 2 max length = 3 sum rate table > 100
                //if value > 100 set default is 100
                //IVS_LVTu start edit 2015/01/26 Task #35026
                if ( type == 2 && Integer.parseInt(textField.getText()) > 100) {
                    return 100;
                }else if(type == 3 && Integer.parseInt(textField.getText()) > 100 && this.row == 0){
                    return 100;
                }else {
                    return Integer.parseInt(textField.getText());
                }
                //IVS_LVTu end edit 2015/01/26 Task #35026
            }
        }
    }
    
    
    /**
     * SettingDataTargetFocusTraversalPolicy
     */
    private class SettingDataTargetFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         * aComponent ÇÃÇ†Ç∆Ç≈ÉtÉHÅ[ÉJÉXÇéÛÇØéÊÇÈ Component Çï‘ÇµÇ‹Ç∑ÅB aContainer ÇÕ aComponent
         * ÇÃÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_Ç≈Ç»ÇØÇÍÇŒÇ»ÇËÇ‹ÇπÇÒÅB
         *
         * @param aContainer aComponent ÇÃÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_
         * @param aComponent aContainer ÇÃÇ®ÇªÇÁÇ≠ä‘ê⁄ìIÇ»éqÅAÇ‹ÇΩÇÕ aContainer é©ëÃ
         * @return aComponent ÇÃÇ†Ç∆Ç…ÉtÉHÅ[ÉJÉXÇéÛÇØéÊÇÈ ComponentÅBìKêÿÇ» Component Ç™å©Ç¬Ç©ÇÁÇ»Ç¢èÍçáÇÕ
         * null
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            
            if(step == 0)
            {
                if (aComponent.equals(txttechnic)) {
                    return txtSaleTarget;
                } else {
                    return txttechnic;
                }
            }
            return null;
        }

        /**
         * aComponent ÇÃëOÇ…ÉtÉHÅ[ÉJÉXÇéÛÇØéÊÇÈ Component Çï‘ÇµÇ‹Ç∑ÅB aContainer ÇÕ aComponent
         * ÇÃÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_Ç≈Ç»ÇØÇÍÇŒÇ»ÇËÇ‹ÇπÇÒÅB
         *
         * @param aContainer aComponent ÇÃÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_
         * @param aComponent aContainer ÇÃÇ®ÇªÇÁÇ≠ä‘ê⁄ìIÇ»éqÅAÇ‹ÇΩÇÕ aContainer é©ëÃ
         * @return aComponent ÇÃëOÇ…ÉtÉHÅ[ÉJÉXÇéÛÇØéÊÇÈ ComponentÅBìKêÿÇ» Component Ç™å©Ç¬Ç©ÇÁÇ»Ç¢èÍçáÇÕ
         * null
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            if(step == 1)
            {
                if (aComponent.equals(txttechnic)) {
                    return txtSaleTarget;
                } else {
                    return txttechnic;
                }
            }
            return null;
        }

        /**
         * ÉgÉâÉoÅ[ÉTÉãÉTÉCÉNÉãÇÃç≈èâÇÃ Component Çï‘ÇµÇ‹Ç∑ÅB Ç±ÇÃÉÅÉ\ÉbÉhÇÕÅAèáï˚å¸ÇÃÉgÉâÉoÅ[ÉTÉãÇ™ÉâÉbÉvÇ∑ÇÈÇ∆Ç´Ç…ÅAéüÇ…ÉtÉHÅ[ÉJÉXÇ∑ÇÈ
         * Component ÇîªíËÇ∑ÇÈÇΩÇﬂÇ…égópÇµÇ‹Ç∑ÅB
         *
         * @param aContainer êÊì™ÇÃ Component
         * Çï‘Ç∑ÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_
         * @return aContainer ÇÃÉgÉâÉoÅ[ÉTÉãÉTÉCÉNÉãÇÃêÊì™ÇÃ ComponetÅAÇ‹ÇΩÇÕìKêÿÇ» Component
         * Ç™å©Ç¬Ç©ÇÁÇ»Ç¢èÍçáÇÕ null
         */
        public Component getFirstComponent(Container aContainer) {
            return txttechnic;
        }

        /**
         * ÉgÉâÉoÅ[ÉTÉãÉTÉCÉNÉãÇÃç≈å„ÇÃ Component Çï‘ÇµÇ‹Ç∑ÅB Ç±ÇÃÉÅÉ\ÉbÉhÇÕÅAãtï˚å¸ÇÃÉgÉâÉoÅ[ÉTÉãÇ™ÉâÉbÉvÇ∑ÇÈÇ∆Ç´Ç…ÅAéüÇ…ÉtÉHÅ[ÉJÉXÇ∑ÇÈ
         * Component ÇîªíËÇ∑ÇÈÇΩÇﬂÇ…égópÇµÇ‹Ç∑ÅB
         *
         * @param aContainer aContainer - ç≈å„ÇÃ Component
         * Çï‘Ç∑ÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_
         * @return aContainer ÇÃÉgÉâÉoÅ[ÉTÉãÉTÉCÉNÉãÇÃç≈å„ÇÃ ComponetÅAÇ‹ÇΩÇÕìKêÿÇ» Component
         * Ç™å©Ç¬Ç©ÇÁÇ»Ç¢èÍçáÇÕ null
         */
        public Component getLastComponent(Container aContainer) {
            return txtSaleTarget;
        }

        /**
         * ÉtÉHÅ[ÉJÉXÇê›íËÇ∑ÇÈÉfÉtÉHÉãÉgÉRÉìÉ|Å[ÉlÉìÉgÇï‘ÇµÇ‹Ç∑ÅB aContainer
         * ÇÉãÅ[ÉgÇ∆Ç∑ÇÈÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉTÉCÉNÉãÇ™êVÇµÇ≠äJénÇ≥ÇÍÇΩÇ∆Ç´Ç…ÅAÇ±ÇÃÉRÉìÉ|Å[ÉlÉìÉgÇ…ç≈èâÇ…ÉtÉHÅ[ÉJÉXÇ™ê›íËÇ≥ÇÍÇ‹Ç∑ÅB
         *
         * @param aContainer ÉfÉtÉHÉãÉgÇÃ Component
         * Çï‘Ç∑ÉtÉHÅ[ÉJÉXÉTÉCÉNÉãÇÃÉãÅ[ÉgÇ‹ÇΩÇÕÉtÉHÅ[ÉJÉXÉgÉâÉoÅ[ÉTÉãÉ|ÉäÉVÅ[ÉvÉçÉoÉCÉ_
         * @return aContainer ÇÃÉgÉâÉoÅ[ÉTÉãÉTÉCÉNÉãÇÃÉfÉtÉHÉãÉgÇÃ ComponetÅAÇ‹ÇΩÇÕìKêÿÇ» Component
         * Ç™å©Ç¬Ç©ÇÁÇ»Ç¢èÍçáÇÕ null
         */
        public Component getDefaultComponent(Container aContainer) {
            return txttechnic;
        }

        /**
         * ÉEÉBÉìÉhÉEÇ™ç≈èâÇ…ï\é¶Ç≥ÇÍÇΩÇ∆Ç´Ç…ÉtÉHÅ[ÉJÉXÇ™ê›íËÇ≥ÇÍÇÈÉRÉìÉ|Å[ÉlÉìÉgÇï‘ÇµÇ‹Ç∑ÅB show() Ç‹ÇΩÇÕ setVisible(true)
         * ÇÃåƒÇ—èoÇµÇ≈àÍìxÉEÉBÉìÉhÉEÇ™ï\é¶Ç≥ÇÍÇÈÇ∆ÅA èâä˙âªÉRÉìÉ|Å[ÉlÉìÉgÇÕÇªÇÍà»ç~égópÇ≥ÇÍÇ‹ÇπÇÒÅB
         * àÍìxï ÇÃÉEÉBÉìÉhÉEÇ…à⁄Ç¡ÇΩÉtÉHÅ[ÉJÉXÇ™çƒÇ—ê›íËÇ≥ÇÍÇΩèÍçáÅA Ç‹ÇΩÇÕÅAàÍìxîÒï\é¶èÛë‘Ç…Ç»Ç¡ÇΩÉEÉBÉìÉhÉEÇ™çƒÇ—ï\é¶Ç≥ÇÍÇΩèÍçáÇÕÅA
         * ÇªÇÃÉEÉBÉìÉhÉEÇÃç≈å„Ç…ÉtÉHÅ[ÉJÉXÇ™ê›íËÇ≥ÇÍÇΩÉRÉìÉ|Å[ÉlÉìÉgÇ™ÉtÉHÅ[ÉJÉXèäóLé“Ç…Ç»ÇËÇ‹Ç∑ÅB
         * Ç±ÇÃÉÅÉ\ÉbÉhÇÃÉfÉtÉHÉãÉgé¿ëïÇ≈ÇÕÉfÉtÉHÉãÉgÉRÉìÉ|Å[ÉlÉìÉgÇï‘ÇµÇ‹Ç∑ÅB
         *
         * @param window èâä˙ÉRÉìÉ|Å[ÉlÉìÉgÇ™ï‘Ç≥ÇÍÇÈÉEÉBÉìÉhÉE
         * @return ç≈èâÇ…ÉEÉBÉìÉhÉEÇ™ï\é¶Ç≥ÇÍÇÈÇ∆Ç´Ç…ÉtÉHÅ[ÉJÉXê›íËÇ≥ÇÍÇÈÉRÉìÉ|Å[ÉlÉìÉgÅBìKêÿÇ»ÉRÉìÉ|Å[ÉlÉìÉgÇ™Ç»Ç¢èÍçáÇÕ null
         */
        public Component getInitialComponent(Window window) {
            return txttechnic;
        }
    }

}

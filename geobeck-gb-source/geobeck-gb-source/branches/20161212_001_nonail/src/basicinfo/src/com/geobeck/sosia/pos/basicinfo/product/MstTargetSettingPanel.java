package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.basicinfo.commodity.EditabeTableCellRenderer;
import com.geobeck.sosia.pos.hair.master.product.DataTargetTechnic;
import com.geobeck.sosia.pos.hair.master.product.MstDataTarget;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstShopRelation;
import com.geobeck.sosia.pos.master.company.MstShopRelations;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.border.Border;

/**
 *
 * @author nahoang GB_Mashu 2014/10/06_ñ⁄ïWê›íË
 */
public class MstTargetSettingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private int allOfShop = 2;                                                  //only shop
    private Integer periodMonth = null;
    private Integer categoryId = null;
    private String categoryName = "";
    private Integer shopId = null;
    private String shopName = "";
    private ArrayList<DataTargetTechnic> listDataTechnic = null;

    public MstTargetSettingPanel() {
        super();
        initComponents();
        addMouseCursorChange();
        SystemInfo.initGroupShopComponents(cmbShop, allOfShop);
        initTimeOfYear(calTargetDate);
        initTimeOfYear(cmbSalesYear);
        InitMonth(cmbSalesMonth);
        intShopCategory();
        loadDataTable();
        this.setSize(1020, 660);
        this.setPath("äÓñ{ê›íË >> ñ⁄ïWê›íË");
        this.setTitle("ñ⁄ïWê›íË");
    }

    /*
    Execute method and get list item to bind on table.
    */
    private void loadDataTable() {
        //get period_month propery
        boolean isResult = true;
        MstAccountSetting account = new MstAccountSetting();
        isResult = account.getSQLPeriodMonth();

        if (isResult) {
            periodMonth = account.getPeriodMonth();
            //IVS_LVTu start add 2015/06/02 New request #37146
            if ( periodMonth == null || periodMonth == 0) {
                periodMonth = 12;
            }
            //IVS_LVTu end add 2015/06/02 New request #37146
            
            Integer targetMonth = null;
            //get the start month with condition periodMonth variable.
            if (periodMonth != 12) {
                targetMonth = periodMonth + 1;
            } else {
                targetMonth = 1;
            }

            Integer year = (Integer) calTargetDate.getSelectedItem();
            //check item of shop or group.                 
            if (cmbShop.getSelectedItem() instanceof MstShop) {
                MstShop shopItem = (MstShop) cmbShop.getSelectedItem();
                shopId = shopItem.getShopID();
                if (cmbCategory.getSelectedItem().equals("")) {
                    categoryId = 0;
                } else {
                    MstShopRelation categoryItem = (MstShopRelation) cmbCategory.getSelectedItem();
                    categoryId = categoryItem.getShopCategoryId();
                }
            }

            //get list item from data target table
            MstDataTarget dataTarget = new MstDataTarget();
            ArrayList<MstDataTarget> listItem = new ArrayList<MstDataTarget>();
            listItem = dataTarget.getSQLDataTarget(shopId, categoryId, year, targetMonth);

            //load data target into table.
            showData(listItem, categoryId, shopId, year, targetMonth);
        }
    }

    /*
    Display data into table.
    */
    private void showData(ArrayList<MstDataTarget> listItem, Integer category,
            Integer shopId, Integer targetYear, Integer targetMonth) {

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable.setModel(model);

        //add new colum name
        Vector<Object> colName = new Vector<Object>();
        colName.add("ãZèpîÑè„");
        colName.add("ìXîÃîÑè„");
        colName.add("ëçîÑè„");
        colName.add("ãZèpãqêî");
        colName.add("êVãKî‰ó¶");
        colName.add("êVãKãqêî");
        colName.add("ìXîÃî‰ó¶");
        colName.add("ìXîÃêlêî");

        DataTargetTechnic dataTargetTechnic = new DataTargetTechnic();
        listDataTechnic = new ArrayList<DataTargetTechnic>();
        //get list name of Technic Item to bind into column name.
        listDataTechnic = dataTargetTechnic.getSQLTechnicTarget(targetYear, targetMonth, category, shopId);

        for (DataTargetTechnic className : listDataTechnic) {
            colName.add(className.getTechnicClassName() + "î‰ó¶");              //rate
        }
        for (DataTargetTechnic className : listDataTechnic) {
            colName.add(className.getTechnicClassName() + "íPâø");              //value
        }

        colName.add("ê›íËÇ≈ÇÃãZîÑå©çû");                                        // sum data of rate * value.

        colName.add("90ì˙êVãKçƒóàó¶");
        colName.add("120ì˙êVãKçƒóàó¶");
        colName.add("180ì˙êVãKçƒóàó¶");
        colName.add("90ì˙èÄå≈íËçƒóàó¶");
        colName.add("120ì˙èÄå≈íËçƒóàó¶");
        colName.add("180ì˙èÄå≈íËçƒóàó¶");
        colName.add("90ì˙å≈íËçƒóàó¶");
        colName.add("120ì˙å≈íËçƒóàó¶");
        colName.add("180ì˙å≈íËçƒóàó¶");
        colName.add("éñëOó\ñÒó¶");
        colName.add("éüâÒó\ñÒó¶");

        model.addColumn("ñ⁄ïWçÄñ⁄", colName);                                   //column name

        Integer technicSum = 0;
        Integer itemSum = 0;
        Integer technicItemSum = 0;
        Integer customerSum = 0;
        Integer newCustomerSum = 0;
        double shopNumberSum = 0.0;
        double shopRateSum = 0.0;
        Long rateValueSum = 0L;

        //Get value loop through 12 months for 12 column.
        for (MstDataTarget item : listItem) {
            String month = item.getMonth().toString() + "åé";
            Vector<Object> colData = new Vector<Object>();

            colData.addElement(item.getTechnic());
            colData.addElement(item.getItem());
            colData.addElement(item.getTechnicItem());
            colData.addElement(item.getCustomer());

            Long newRate = 0L;
            newRate = (long) (item.getNewRate() * 100);
            colData.addElement(newRate + "%");

            colData.addElement(item.getNewCustomer());

            double shopNumber = 0.0;
            double shopRate = 0.0;
            int itemAmount = item.getItem();                                    //  ìXîÃîÑè„ 
            int technicAmount = item.getTechnic();                              // ãZèpîÑè„  
            Integer customerAmount = item.getCustomer();                        // ãZèpãqêî
            if (technicAmount != 0) {
                Double a = (double) itemAmount / technicAmount;                 // ìXîÃî‰ó¶ÅFìXîÃîÑè„/ãZèpîÑè„  
                shopRate = Math.round(a * 100.0) / 100.0;
            }

            colData.addElement((Math.round(shopRate * 100)) + " %");
            shopNumber = shopRate * customerAmount;                             // ìXîÃêlêîÅFìXîÃî‰ó¶*ãZèpãqêî
            colData.addElement(Math.round(shopNumber));

            Double rateData = 0.0;
            Integer valueData = 0;
            Double dbrateValueData = 0.0;
            Integer intRateValueData = 0;
            Integer customerValue = 0;

            technicSum += item.getTechnic();
            itemSum += item.getItem();
            technicItemSum += item.getTechnicItem();
            customerSum += item.getCustomer();
            newCustomerSum += item.getNewCustomer();
            shopNumberSum += shopNumber;

            //Get Technic Item in target month.
            listDataTechnic = dataTargetTechnic.getSQLTechnicTarget(item.getYear(), item.getMonth(), category, shopId);

            for (DataTargetTechnic itemClass : listDataTechnic) {
                Long technicRateData = 0L;
                technicRateData = (long) (itemClass.getTechnicRate() * 100);
                colData.add(technicRateData + " %");                  // î‰ó¶
                rateData = itemClass.getTechnicRate();
                valueData = itemClass.getTechnicValue();
                customerValue = item.getCustomer();
                dbrateValueData += customerValue * rateData * valueData;

            }

            BigDecimal bdRateValue = new BigDecimal(dbrateValueData);
            intRateValueData = bdRateValue.setScale(0, RoundingMode.HALF_UP).intValue();

            for (DataTargetTechnic itemClass : listDataTechnic) {
                colData.add(itemClass.getTechnicValue());
            }

            colData.addElement(intRateValueData);

            rateValueSum += intRateValueData;

            Long repert90New = (long) (item.getRepert90New() * 100);
            colData.addElement(repert90New + "%");
            Long repert120New = (long) (item.getRepert120New() * 100);
            colData.addElement(repert120New + "%");
            Long repert180New = (long) (item.getRepert180New() * 100);
            colData.addElement(repert180New + "%");
            Long repert90Semi = (long) (item.getRepert90Semifix() * 100);
            colData.addElement(repert90Semi + "%");
            Long repert120Semi = (long) (item.getRepert120Semifix() * 100);
            colData.addElement(repert120Semi + "%");
            Long repert180Semi = (long) (item.getRepert180Semifix() * 100);
            colData.addElement(repert180Semi + "%");
            Long repert90Fix = (long) (item.getRepert90Fix() * 100);
            colData.addElement(repert90Fix + "%");
            Long repert120Fix = (long) (item.getRepert120Fix() * 100);
            colData.addElement(repert120Fix + "%");
            Long repert180Fix = (long) (item.getRepert180Fix() * 100);
            colData.addElement(repert180Fix + "%");
            Long beforeReserve = (long) (item.getBeforeReserve() * 100);
            colData.addElement(beforeReserve + "%");
            Long nextReserve = (long) (item.getNextReserve() * 100);
            colData.addElement(nextReserve + "%");

            model.addColumn(month, colData);                                    // column 12 month

        }

        //add colum sum of data
        Vector<Object> colSum = new Vector<Object>();
        colSum.add(technicSum);
        colSum.add(itemSum);
        colSum.add(technicItemSum);
        colSum.add(customerSum);
        colSum.add("-");
        colSum.add(newCustomerSum);
        double out = (double) itemSum / technicSum;
        shopRateSum = Math.round(out * 100.0) / 100.0;
        colSum.add(Math.round(shopRateSum * 100) + "%");
        colSum.add(Math.round(shopNumberSum));

        Integer amountLoop = 2 * listDataTechnic.size();                        //loop through rate list and value list
        for(int i = 0; i < amountLoop ; i++){
            colSum.add("-");
        }

        colSum.add(rateValueSum);

        for (int i = 0; i < 11; i++) {
            colSum.add("-");
        }

        model.addColumn("îNä‘", colSum);                                        // column sum data

        initTableColumnWidth();

        SwingUtil.setJTableHeaderRenderer(itemTable, SystemInfo.getTableHeaderRenderer());
    }

    /*
    Set width of table. 
    */
    private void initTableColumnWidth() {
        itemTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        itemTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(7).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(8).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(9).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(10).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(11).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(12).setPreferredWidth(60);
        itemTable.getColumnModel().getColumn(13).setPreferredWidth(100);

    }

    /*
    Initial list of year and bind into combobox
    */
    private void initTimeOfYear(JComboBox combobox) {
        int previousYear, currentYear, nextYear = 0;
        int selectCurrent = 2;

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        previousYear = currentYear - 2;
        nextYear = currentYear + 2;

        for (int i = previousYear; i <= nextYear; i++) {
            combobox.addItem(i);
        }
        combobox.setSelectedIndex(selectCurrent);
    }

    /*
    Initial list of month and bind into combobox
    */
    private void InitMonth(JComboBox combobox) {

        for (int i = 1; i <= 12; i++) {
            combobox.addItem(i);
        }
        combobox.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
    }

    /*
    Initial list of category and bind into combobox
    */
    private void intShopCategory() {
        int flagCategory = 1;
        Integer shopItemId = null;
        
        cmbCategory.removeAllItems();
        cmbCategory.addItem("");
        MstShopRelations listCategory = new MstShopRelations();

        if (cmbShop.getSelectedItem() instanceof MstShop) {
            MstShop itemShop = (MstShop) cmbShop.getSelectedItem();
            flagCategory = itemShop.getUseShopCategory();
            shopItemId = itemShop.getShopID();
        }

        if (flagCategory == 1) {
            ConnectionWrapper con = SystemInfo.getConnection();
            try {

                listCategory.loadByShop(con, shopItemId);
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(MstTargetSettingPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (MstShopRelation item : listCategory) {
                cmbCategory.addItem(item);
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        calTargetDate = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox();
        btnShow = new javax.swing.JButton();
        btnSetting = new javax.swing.JButton();
        buttonExport = new javax.swing.JButton();
        itemScrollPane = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        cmbSalesYear = new javax.swing.JComboBox();
        cmbSalesMonth = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();

        setFocusCycleRoot(true);

        jLabel1.setText("ìXï‹");

        jLabel4.setText("ëŒè€");

        calTargetDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calTargetDateActionPerformed(evt);
            }
        });

        jLabel3.setText("ã∆ë‘");

        cmbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoryActionPerformed(evt);
            }
        });

        btnShow.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        btnShow.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnShow.setMaximumSize(new java.awt.Dimension(25, 9));
        btnShow.setMinimumSize(new java.awt.Dimension(25, 9));
        btnShow.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        btnSetting.setIcon(SystemInfo.getImageIcon("/button/product/target_detail_setting_off.jpg"));
        btnSetting.setPressedIcon(SystemInfo.getImageIcon("/button/product/target_detail_setting_on.jpg"));
        btnSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingActionPerformed(evt);
            }
        });

        buttonExport.setIcon(SystemInfo.getImageIcon("/button/product/result_output_off.jpg"));
        buttonExport.setPressedIcon(SystemInfo.getImageIcon("/button/product/result_output_on.jpg"));
        buttonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportActionPerformed(evt);
            }
        });

        itemScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        itemTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        itemTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
        itemTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.getTableHeader().setReorderingAllowed(true);
        SwingUtil.setJTableHeaderRenderer(itemTable, SystemInfo.getTableHeaderRenderer());
        itemTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);

        SelectTableCellRenderer.setSelectTableCellRenderer(itemTable);
        itemTable.setDefaultRenderer(Object.class, new TableCellAlignRenderer());
        itemTable.setDefaultRenderer(Object.class, new TableCellAlignRenderer(itemTable));
        SwingUtil.setJTableHeaderRenderer(itemTable, SystemInfo.getTableHeaderRenderer());
        itemScrollPane.setViewportView(itemTable);

        cmbSalesYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSalesYearActionPerformed(evt);
            }
        });

        cmbSalesMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSalesMonthActionPerformed(evt);
            }
        });

        jLabel2.setText("îNìx");

        jLabel5.setText("îN");

        jLabel6.setText("åé");

        cmbShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShopActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(itemScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 802, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel1)
                            .add(jLabel4)
                            .add(jLabel3))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(calTargetDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(357, 357, 357)
                                .add(cmbSalesYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(cmbSalesMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(buttonExport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(btnShow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(btnSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jLabel2))))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(cmbShop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cmbSalesYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbSalesMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel6)
                            .add(jLabel5))
                        .add(8, 8, 8))
                    .add(buttonExport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(calTargetDate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .add(jLabel4)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(cmbCategory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                        .add(7, 7, 7))
                    .add(btnShow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(itemScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 554, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void calTargetDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calTargetDateActionPerformed
    }//GEN-LAST:event_calTargetDateActionPerformed

    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
    }//GEN-LAST:event_cmbCategoryActionPerformed

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        btnShow.setCursor(null);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        loadDataTable();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnShowActionPerformed

    //IVS Luc start add 20141020 GB_Mashu_ñ⁄ïWê›íË_é¿ê—
    private void buttonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportActionPerformed
        // TODO add your handling code here:
        String fileName = "";
        MstShop selectedShop = (MstShop)cmbShop.getSelectedItem();
        buttonExport.setCursor(null);
        try {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Object[] options = {"[çîNé¿ê—]", "[ñ⁄ïWç∑àŸ]"};

        int n = JOptionPane.showOptionDialog(this, "çîNé¿ê—Ç∆ñ⁄ïWç∑àŸÇÃÇ«ÇøÇÁÇ≈èoóÕÇµÇ‹Ç∑Ç©ÅH", this.getTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (n < 0) {
            return;
        }
        if (n == 0) {
            fileName = "é¿ê—_çîNé¿ê—";
        } else {
            fileName = "é¿ê—_ñ⁄ïWç∑àŸ";
        }
        int SelectedMonth;
        int SelectedYear;
        SelectedYear = Integer.parseInt(cmbSalesYear.getSelectedItem().toString());
            SelectedMonth = Integer.parseInt(cmbSalesMonth.getSelectedItem().toString());
            int shopCategoryId = 0;
            if (cmbCategory.getSelectedItem().equals("")) {
                shopCategoryId = 0;
            } else {
                MstShopRelation categoryItem = (MstShopRelation) cmbCategory.getSelectedItem();
                shopCategoryId = categoryItem.getShopCategoryId();
            }
         String shopCategoryname= cmbCategory.getSelectedItem().toString();
         MstTargetLogic1 lg = new MstTargetLogic1();
         lg.ExportExcel(fileName,selectedShop,SelectedYear, SelectedMonth, shopCategoryId, shopCategoryname, n);
        }catch (Exception e) {
            
        }
        finally
        {
             setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));    
        }
        
    }//GEN-LAST:event_buttonExportActionPerformed

    //IVS Luc end add 20141020 GB_Mashu_ñ⁄ïWê›íË_é¿ê—
    private void cmbSalesYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSalesYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSalesYearActionPerformed

    private void cmbSalesMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSalesMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSalesMonthActionPerformed

    private void btnSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingActionPerformed
        btnSetting.setCursor(null);
        try{
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        Integer yearItem = (Integer) calTargetDate.getSelectedItem();
        if (cmbShop.getSelectedItem() instanceof MstShop) {
            MstShop shopItem = (MstShop) cmbShop.getSelectedItem();
            shopId = shopItem.getShopID();
            shopName = shopItem.getShopName();
            if (cmbCategory.getSelectedItem().equals("")) {
                categoryId = 0;
                categoryName = "";
            } else {
                MstShopRelation categoryItem = (MstShopRelation) cmbCategory.getSelectedItem();
                categoryId = categoryItem.getShopCategoryId();
                categoryName = categoryItem.getShopClassName();
            }
        }
        SettingDataTargetPanel settingPanel = new SettingDataTargetPanel(periodMonth, yearItem, categoryId, categoryName, shopId, shopName);
        SwingUtil.openAnchorDialog(parentFrame, true, settingPanel, "ñ⁄ïWê›íË", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        parentFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        loadDataTable();    //refresh table
        }finally{
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnSettingActionPerformed

    private void cmbShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShopActionPerformed
        this.intShopCategory();
    }//GEN-LAST:event_cmbShopActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSetting;
    private javax.swing.JButton btnShow;
    private javax.swing.JButton buttonExport;
    private javax.swing.JComboBox calTargetDate;
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JComboBox cmbSalesMonth;
    private javax.swing.JComboBox cmbSalesYear;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbShop;
    private javax.swing.JScrollPane itemScrollPane;
    private javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables

    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnShow);
        SystemInfo.addMouseCursorChange(btnSetting);
        SystemInfo.addMouseCursorChange(buttonExport);
    }
    
    /**
     * óÒÇÃï\é¶à íuÇê›íËÇ∑ÇÈTableCellRenderer
     */
    private class TableCellAlignRenderer extends SelectTableCellRenderer {

        public TableCellAlignRenderer() {
            super();
        }

        public TableCellAlignRenderer(JTable table) {
            setSelectedRowColor(new Color(255, 210, 142));
            setShadow0Color(new Color(113, 113, 113));
            setShadow1Color(new Color(172, 172, 172));
            setHighlightColor(new Color(241, 241, 241));
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            this.isSelected = isSelected;
            rRow = row;
            rCol = column;
            this.value = value;
            super.setForeground((isSelected ? table.getSelectionForeground() : table.getForeground()));
            
            switch (column) {
                case 0:
                    super.setHorizontalAlignment(SwingConstants.CENTER);
                    break;
                case 13:
                    super.setHorizontalAlignment(SwingConstants.CENTER);
                    break;
                default:
                    super.setHorizontalAlignment(SwingConstants.RIGHT);
                    break;
            }

            return this;
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
        private boolean isSelected = false;
        private Color selectedRowColor = null;
        private Color shadow0Color = null;
        /**
         * âeÇÃêFÇP
         */
        private Color shadow1Color = null;
        /**
         * ÉnÉCÉâÉCÉgÇÃêF
         */
        private Color highlightColor = null;
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
        private int rRow;
        private int rCol;
        private int type = 1;
        private Object value = null;
        private static final int SIDE_MARGIN = 4;

        @Override
        public void paint(Graphics g) {
            Integer amount = (listDataTechnic.size() * 2) + 8;
            if (rRow == 2 || rRow == 3 || rRow == 5 || rRow == 6 || rRow == 7 || rRow == amount) {
            } else if (rCol > 0 && rCol < 13) {
                g.setColor(selectedRowColor);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
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

        private boolean isDecimal() {
            if (value instanceof Float
                    || value instanceof Double) {
                return true;
            } else {
                return false;
            }
        }

        private boolean isDateTime(String value) {
            return value.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")
                    || value.matches("[0-9]{4}/[0-9]{2}")
                    || value.matches("[0-9]{2}:[0-9]{2}");
        }

        private boolean isPostalCode(String value) {
            return value.matches("[0-9]{3}-[0-9]{4}");
        }

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
    }
}

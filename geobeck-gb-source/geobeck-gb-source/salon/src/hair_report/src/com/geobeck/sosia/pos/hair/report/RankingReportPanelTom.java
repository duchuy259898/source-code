/*
 * RankingReportPanelTom.java
 *
 * Created on 2008/09/19, 17:29
 */

package com.geobeck.sosia.pos.hair.report;


import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.SwingUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;

import javax.swing.*;
import java.util.*;
import javax.swing.table.*;

import com.geobeck.sosia.pos.hair.report.logic.RankingReportLogic;
import com.geobeck.sosia.pos.hair.report.logic.TechnicSalesReportLogic;
import com.geobeck.sosia.pos.hair.report.beans.SalesDateBean;
import java.awt.Cursor;

/**
 *
 * @author  gloridel
 */
public class RankingReportPanelTom extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    RankingReportLogic.ReportTypes reportType =
            RankingReportLogic.ReportTypes.Designate;;
    private int shopId;
    private MstGroup group = null;
    private ShopStaffs staffs = null;
    
    private final int ColNO    = 0;
    private final int ColStaff = 1;
    private final int ColStore = 2;
    
    private LocalFocusTraversalPolicy   ftp;
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
        return  ftp;
    }
    
    /** コンストラクタ */
    public RankingReportPanelTom() {

        initComponents();
        addMouseCursorChange();
        
        ftp = new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        this.setTitle("カスタム帳票>>ランキング");
        this.setPath("帳票出力");
        shopId = SystemInfo.getCurrentShop().getShopID();
        initCmbDate();
        
        group = SystemInfo.getGroup();
        initWindow(shopId);
    }
    
    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnPdf);
        SystemInfo.addMouseCursorChange(btnExcel);
    }
    
    private void setKeyListener() {
        radioShimei.addKeyListener(SystemInfo.getMoveNextField());
        radioGone.addKeyListener(SystemInfo.getMoveNextField());
        radioStore.addKeyListener(SystemInfo.getMoveNextField());
        radioTechnic.addKeyListener(SystemInfo.getMoveNextField());
        cmbYear.addKeyListener(SystemInfo.getMoveNextField());
        cmbMonth.addKeyListener(SystemInfo.getMoveNextField());
        staffTbl.addKeyListener(SystemInfo.getMoveNextField());
        
        radioShimei.addFocusListener(SystemInfo.getSelectText());
        radioGone.addFocusListener(SystemInfo.getSelectText());
        radioStore.addFocusListener(SystemInfo.getSelectText());
        radioTechnic.addFocusListener(SystemInfo.getSelectText());
        cmbYear.addFocusListener(SystemInfo.getSelectText());
        cmbMonth.addFocusListener(SystemInfo.getSelectText());
        staffTbl.addFocusListener(SystemInfo.getSelectText());
    }
    
    /** ウィンドウの初期化 */
    private void initWindow(int shopId){
        
        this.setSize(500,680);
        
        //if( shopId == 0 ) {
            staffTbl.setVisible(true);
            changeColSize();
            initColComboBox();
            // Table Selection
            staffTbl.setColumnSelectionAllowed(false);
            staffTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
            // 列の並び替え禁止
            staffTbl.getTableHeader().setReorderingAllowed(false);
            // ヘッダのレンダラを設定
            SwingUtil.setJTableHeaderRenderer(staffTbl,
                    SystemInfo.getTableHeaderRenderer());
            initStaffInformation();
        //} else {
        //    pane.setVisible(false);
        //}
        
    }
    
    /** 列の幅をセットする */
    private void changeColSize() {
        TableColumn col = null;
        for(int i=0; i < 3; i++ ) {
            col = staffTbl.getColumnModel().getColumn(i);
            switch(i) {
                case  ColNO:
                    col.setPreferredWidth(20);
                    break;
                case ColStaff:
                    col.setPreferredWidth(100);
                    break;
                case ColStore:
                    col.setPreferredWidth(100);
                    break;
            }
        }
    }
    
    /** コンボボックスの初期化 */
    private void initColComboBox() {
        TableColumn col = staffTbl.getColumnModel().getColumn(ColStore);
        
        Vector<MstShop> values = new Vector<MstShop>();
        values.add(new MstShop());
        
        for(int i=0; i < group.getShops().size() ; i ++ ) {
            values.add(group.getShops().get(i));
            
            System.out.print("ShopId   : " + values.get(i).getShopID() + "\n");
            System.out.print("ShopName :" + values.get(i).getShopName() + "\n");
        }
        
        JComboBox cb = new JComboBox(values);
        cb.setBorder(BorderFactory.createEmptyBorder());
        cb.setSelectedIndex(-1);
        
        TableColumn column = staffTbl.getColumnModel().getColumn(2);
        column.setCellEditor(new DefaultCellEditor(cb));
    }
    
    private void initStaffInformation() {
        // Clear Table
        DefaultTableModel model = (DefaultTableModel)staffTbl.getModel();
        model.setRowCount(0);
        
        // スタッフを設定
        staffs = new ShopStaffs(getSelectedDate());
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            staffs.load(con);
            /*
            if( shopId != 0 ){
                if( staffs.existData(con) ){
                    btnPdf.setEnabled(true);
                    btnExcel.setEnabled(true);
                }else{
                    btnPdf.setEnabled(false);
                    btnExcel.setEnabled(false);
                }
            }
           */
        } catch(java.sql.SQLException e) {
            //SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        for(MstStaff ms : staffs) {
            if( ms != null ){
                addStaffRow(ms);
            }
        }
    }
    
    private boolean update() {
        
        //if( shopId != 0 ) return true;
        
        DefaultTableModel model = (DefaultTableModel)staffTbl.getModel();
        
        for (int rowNum = 0; rowNum < model.getRowCount(); rowNum++ ) {

            Object obj = model.getValueAt(rowNum, ColStore);
            
            if ( obj instanceof MstShop ) {
                MstShop shop = (MstShop)obj;
                
                if ( shop != null ) {
                    staffs.get(rowNum).setShopID(shop.getShopID());
                    staffs.get(rowNum).setShopName(shop.getShopName());
                } else {
                    staffs.get(rowNum).setShopID(null);
                    staffs.get(rowNum).setShopName(null);
                }
            } else {
                staffs.get(rowNum).setShopID(null);
                staffs.get(rowNum).setShopName(null);
            }
        }
        try {
            return staffs.update(SystemInfo.getConnection());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    private void addStaffRow(MstStaff staff) {
        
        if ( staff.getStaffID() != null ) {
            
            DefaultTableModel model = (DefaultTableModel)staffTbl.getModel();
            
            model.addRow(new Vector());
            
            int rowNum = staffTbl.getRowCount() - 1;
            
            System.out.print(staff.getStaffNo() + " : " + staff.getStaffName(0) + "  " + staff.getStaffName(1) + "\n");
            
            //NO
            staffTbl.setValueAt(staff.getStaffNo(), rowNum, ColNO);
            
            //スタッフ名
            staffTbl.setValueAt(staff.getStaffName(0) + "  " + staff.getStaffName(1), rowNum,ColStaff);
            
            // 店舗
            MstShop mstShop = staff.getShop();
            if( mstShop.getShopID() == 0 ) {
                staffTbl.setValueAt("", rowNum,ColStore);
            }else{
                staffTbl.setValueAt(mstShop, rowNum,ColStore);
            }
        }
    }
    
    private void initCmbDate() {
        
        ArrayList<SalesDateBean> beanList = null;
        
        beanList = TechnicSalesReportLogic.getSalesDateBean(shopId);
        
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        String currentYear = new String(calendar.get(Calendar.YEAR)+"");
        int index = -1;
        
        cmbYear.removeAllItems();
        
        for(int i=0; i < beanList.size(); i++) {
            cmbYear.addItem(beanList.get(i).getYear() + "年");
            if( beanList.get(i).getYear().equals(currentYear)){
                index = i;
            }
        }
        
        cmbYear.setSelectedIndex(index);
        
        calendar.setTime(today);
        cmbMonth.setSelectedIndex(calendar.get(Calendar.MONTH));
    }
    
    private Date getSelectedDate() {
        String year = (String)cmbYear.getSelectedItem();
        int month = cmbMonth.getSelectedIndex();
        Calendar calendar = Calendar.getInstance();
        
        year = year.replaceAll("年","");
        calendar.set(Integer.parseInt(year), month, 1, 0, 0, 0);
        
        return calendar.getTime();
    }
    
    /** 自動生成されたコード */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        btnRankType = new javax.swing.ButtonGroup();
        summaryTypeButtonGroup = new javax.swing.ButtonGroup();
        radioShimei = new javax.swing.JRadioButton();
        radioStore = new javax.swing.JRadioButton();
        radioGone = new javax.swing.JRadioButton();
        radioTechnic = new javax.swing.JRadioButton();
        btnPdf = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbYear = new javax.swing.JComboBox();
        cmbMonth = new javax.swing.JComboBox();
        pane = new javax.swing.JScrollPane();
        staffTbl = new javax.swing.JTable();

        btnRankType.add(radioShimei);
        radioShimei.setSelected(true);
        radioShimei.setText("\u6307\u540d\u7387\u96c6\u8a08\u8868");
        radioShimei.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioShimei.setContentAreaFilled(false);
        radioShimei.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioShimei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioShimeiActionPerformed(evt);
            }
        });

        btnRankType.add(radioStore);
        radioStore.setText("\u5e97\u8ca9\u58f2\u4e0a\u96c6\u8a08\u8868");
        radioStore.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioStore.setContentAreaFilled(false);
        radioStore.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioStore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioStoreActionPerformed(evt);
            }
        });

        btnRankType.add(radioGone);
        radioGone.setText("\u5931\u5ba2\u6bd4\u7387\u96c6\u8a08\u8868");
        radioGone.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioGone.setContentAreaFilled(false);
        radioGone.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioGone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioGoneActionPerformed(evt);
            }
        });

        btnRankType.add(radioTechnic);
        radioTechnic.setText("\u6280\u8853\u58f2\u4e0a\u96c6\u8a08\u8868");
        radioTechnic.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioTechnic.setContentAreaFilled(false);
        radioTechnic.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioTechnic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTechnicActionPerformed(evt);
            }
        });

        btnPdf.setIcon(SystemInfo.getImageIcon("/button/print/output_pdf_off.jpg"));
        btnPdf.setBorderPainted(false);
        btnPdf.setContentAreaFilled(false);
        btnPdf.setPressedIcon(SystemInfo.getImageIcon("/button/print/output_pdf_on.jpg"));
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        btnExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcel.setBorderPainted(false);
        btnExcel.setContentAreaFilled(false);
        btnExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        jLabel1.setText("\u96c6\u8a08\u671f\u9593");

        cmbYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbYearActionPerformed(evt);
            }
        });

        cmbMonth.setMaximumRowCount(12);
        cmbMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1\u6708", "2\u6708", "3\u6708", "4\u6708", "5\u6708", "6\u6708", "7\u6708", "8\u6708", "9\u6708", "10\u6708", "11\u6708", "12\u6708" }));
        cmbMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthActionPerformed(evt);
            }
        });

        pane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        staffTbl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        staffTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "NO", "スタッフ名", "店舗名"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pane.setViewportView(staffTbl);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pane, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioShimei)
                            .addComponent(radioStore))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioTechnic)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(radioGone)
                                .addGap(28, 28, 28)
                                .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(31, 31, 31)
                        .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(cmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioShimei)
                    .addComponent(radioGone)
                    .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioStore)
                    .addComponent(radioTechnic))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbYear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMonth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // <editor-fold defaultstate="collapsed" desc="コンポーネントのイベント">
    /** PDFボタン */
    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed

        btnPdf.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if( update() ){
                new RankingReportLogic(reportType, getSelectedDate(), this).
                        viewRankingReport(RankingReportLogic.EXPORT_FILE_PDF);
            }            

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_btnPdfActionPerformed
    
    /** 年変更 */
    private void cmbYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbYearActionPerformed
        initStaffInformation();
    }//GEN-LAST:event_cmbYearActionPerformed
    
    /** 月変更 */
    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed
        initStaffInformation();
    }//GEN-LAST:event_cmbMonthActionPerformed
    
    /** 技術売上ラジオボタン */
    private void radioTechnicActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radioTechnicActionPerformed
    {//GEN-HEADEREND:event_radioTechnicActionPerformed
        reportType = RankingReportLogic.ReportTypes.TechnicSales;
    }//GEN-LAST:event_radioTechnicActionPerformed
    
    /** 失客ラジオボタン */
    private void radioGoneActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radioGoneActionPerformed
    {//GEN-HEADEREND:event_radioGoneActionPerformed
        reportType = RankingReportLogic.ReportTypes.Gone;
    }//GEN-LAST:event_radioGoneActionPerformed
    
    /** 店販売上ラジオボタン */
    private void radioStoreActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radioStoreActionPerformed
    {//GEN-HEADEREND:event_radioStoreActionPerformed
        reportType = RankingReportLogic.ReportTypes.Sales;
    }//GEN-LAST:event_radioStoreActionPerformed
    
    /** 指名ラジオボタン */
    private void radioShimeiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radioShimeiActionPerformed
    {//GEN-HEADEREND:event_radioShimeiActionPerformed
        reportType = RankingReportLogic.ReportTypes.Designate;
    }//GEN-LAST:event_radioShimeiActionPerformed
    
    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExcelActionPerformed
    {//GEN-HEADEREND:event_btnExcelActionPerformed

        btnExcel.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if( update() ){
                new RankingReportLogic(reportType, getSelectedDate(), this).
                        viewRankingReport(RankingReportLogic.EXPORT_FILE_XLS);
            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//GEN-LAST:event_btnExcelActionPerformed
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="自動生成された変数">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnPdf;
    private javax.swing.ButtonGroup btnRankType;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane pane;
    private javax.swing.JRadioButton radioGone;
    private javax.swing.JRadioButton radioShimei;
    private javax.swing.JRadioButton radioStore;
    private javax.swing.JRadioButton radioTechnic;
    private javax.swing.JTable staffTbl;
    private javax.swing.ButtonGroup summaryTypeButtonGroup;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
    /** FocusTraversalPolicy */
    private class LocalFocusTraversalPolicy
            extends FocusTraversalPolicy {
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy() {
            controls.add(radioShimei);
            controls.add(radioGone);
            controls.add(radioStore);
            controls.add(radioTechnic);
            controls.add(cmbYear);
            controls.add(cmbMonth);
            controls.add(staffTbl);
            
            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(radioShimei);
        };
        
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
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

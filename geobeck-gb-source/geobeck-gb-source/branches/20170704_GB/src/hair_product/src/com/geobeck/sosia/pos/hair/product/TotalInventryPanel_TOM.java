/*
 * TotalInventryPanel.java
 *
 * Created on 2008/09/16, 15:41
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.product.logic.TotalInventoryReportLogic_TOM;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import com.geobeck.sosia.pos.hair.data.commodity.DataInventory;

/**
 *
 * @author  s_matsumura
 */
public class TotalInventryPanel_TOM extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    private PrintInventry	ia	=	new PrintInventry();
    private InventoryRecords	ir	=	new InventoryRecords();
    
    
    /** Creates new form TotalInventryPanel */
    public TotalInventryPanel_TOM() {
        super();
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        addMouseCursorChange();
        this.setSize(500, 170);
        this.setPath("帳票");
        this.setTitle("合計棚卸表(TOM)");
        SystemInfo.initGroupShopComponents(shop, 2);
        this.setKeyListener();
        
        loadInventoryHead();
        
        initMonthCombobox(inventoryPeriodMonth);
        
        this.init();
        
        //税抜、税込の初期設定
        if (SystemInfo.getAccountSetting().getReportPriceType() == 0) {
            rdoTaxBlank.setSelected(false);
            rdoTaxUnit.setSelected(true);
        } else {
            rdoTaxBlank.setSelected(true);
            rdoTaxUnit.setSelected(false);
        }
        
    }
    
    /**
     * 棚卸のヘッダ情報を読み込む
     */
    private void loadInventoryHead() {
        ir.setShop((MstShop)shop.getSelectedItem());
        
        try {
            ir.load(SystemInfo.getConnection());
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    /**
     * 棚卸期間の表示（最新）
     */
    public void setInventrySpan() {
        inventoryPeriodYear.removeAllItems();
        initYearCombobox(inventoryPeriodYear);
        
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        
        Calendar cal = Calendar.getInstance();
        DataInventory[] invArray = ir.getInventorys();
        if (invArray.length > 0) {
            Date maxDate = invArray[0].getInventoryDate();
            cal.setTime(maxDate);
        }
        
        inventoryPeriodYear.setSelectedItem(new ComboBoxItem<Integer>("", cal.get(Calendar.YEAR)));
        inventoryPeriodMonth.setSelectedItem(new ComboBoxItem<Integer>("", cal.get(Calendar.MONTH)+1));
    }
    
    private void initMonthCombobox(JComboBox cmbbox) {
        cmbbox.addItem(new ComboBoxItem<Integer>("1月", 1));
        cmbbox.addItem(new ComboBoxItem<Integer>("2月", 2));
        cmbbox.addItem(new ComboBoxItem<Integer>("3月", 3));
        cmbbox.addItem(new ComboBoxItem<Integer>("4月", 4));
        cmbbox.addItem(new ComboBoxItem<Integer>("5月", 5));
        cmbbox.addItem(new ComboBoxItem<Integer>("6月", 6));
        cmbbox.addItem(new ComboBoxItem<Integer>("7月", 7));
        cmbbox.addItem(new ComboBoxItem<Integer>("8月", 8));
        cmbbox.addItem(new ComboBoxItem<Integer>("9月", 9));
        cmbbox.addItem(new ComboBoxItem<Integer>("10月", 10));
        cmbbox.addItem(new ComboBoxItem<Integer>("11月", 11));
        cmbbox.addItem(new ComboBoxItem<Integer>("12月", 12));
    }
    
    private void initYearCombobox(JComboBox cmbbox) {
        Calendar cal = Calendar.getInstance();
        DataInventory[] invArray = ir.getInventorys();
        if (invArray.length > 0) {
            int year = 0;
            for (DataInventory inv : invArray) {
                cal.setTime(inv.getInventoryDate());
                if (year != cal.get(Calendar.YEAR)) {
                    year = cal.get(Calendar.YEAR);
                    cmbbox.addItem(new ComboBoxItem(year + "年", year));
                }
            }
        } else {
            int year = cal.get(Calendar.YEAR);
            cmbbox.addItem(new ComboBoxItem(year + "年", year));
        }
    }
    
    private boolean checkInputs() {
        if( this.inventoryPeriodYear.getSelectedIndex() < 0 ||
                this.inventoryPeriodMonth .getSelectedIndex() < 0 ) {
            MessageDialog.showMessageDialog( this, "集計期間を選んでください。",
                    this.getTitle(),JOptionPane.ERROR_MESSAGE );
            return false;
        }
        
        return true;
    }
    
    protected void callReportGeneratorLogic( int reportFileType ) {
        if( this.checkInputs() == false ) {
            return;
        }
        
        int year = ((ComboBoxItem<Integer>) inventoryPeriodYear.getSelectedItem()).getValue();
        int month = ((ComboBoxItem<Integer>) inventoryPeriodMonth.getSelectedItem()).getValue();
        
        MstShop shopInfo = null;
        boolean reportGenerated = false;
        int taxType = 0;
        
        if(this.rdoTaxBlank.isSelected()) {
            taxType = ReportParameterBean.TAX_TYPE_BLANK;
        } else if( this.rdoTaxUnit.isSelected( )) {   //税込み
            taxType = ReportParameterBean.TAX_TYPE_UNIT;
        }
        
        if(shop.getSelectedItem() instanceof MstShop) {
            shopInfo = (MstShop)shop.getSelectedItem();
        } else {
            shopInfo = SystemInfo.getCurrentShop();
        }
        
        TotalInventoryReportLogic_TOM logic = new TotalInventoryReportLogic_TOM(
                shopInfo, taxType, String.format("%04d%02d", year, month ) );
        
        int result = logic.generateReport( reportFileType );
        
        if(result == logic.RESULT_SUCCESS ){
            // 成功
        }else if(result == logic.RESULT_DATA_NOTHNIG ){
            // データなし
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            
        }else if(result == logic.RESULT_ERROR ){
            // 予期せぬエラー
            MessageDialog.showMessageDialog( this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE );
        }
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        shopLabel = new javax.swing.JLabel();
        lblInventryPeriod = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnOutputPdf = new javax.swing.JButton();
        btnOutputExcel = new javax.swing.JButton();
        inventoryPeriodYear = new javax.swing.JComboBox();
        inventoryPeriodMonth = new javax.swing.JComboBox();

        shopLabel.setText("\u5e97\u8217");

        lblInventryPeriod.setText("\u68da\u5378\u671f\u9593");

        lblTax.setText("\u7a0e\u533a\u5206");

        buttonGroup1.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("\u7a0e\u8fbc");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);

        buttonGroup1.add(rdoTaxBlank);
        rdoTaxBlank.setText("\u7a0e\u629c");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });

        btnOutputPdf.setIcon(SystemInfo.getImageIcon("/button/print/output_pdf_off.jpg"));
        btnOutputPdf.setBorderPainted(false);
        btnOutputPdf.setContentAreaFilled(false);
        btnOutputPdf.setPressedIcon(SystemInfo.getImageIcon("/button/print/output_pdf_on.jpg"));
        btnOutputPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputPdfActionPerformed(evt);
            }
        });

        btnOutputExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutputExcel.setBorderPainted(false);
        btnOutputExcel.setContentAreaFilled(false);
        btnOutputExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutputExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputExcelActionPerformed(evt);
            }
        });

        inventoryPeriodYear.setMaximumRowCount(12);
        inventoryPeriodYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        inventoryPeriodMonth.setMaximumRowCount(12);
        inventoryPeriodMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shopLabel)
                            .addComponent(lblInventryPeriod))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inventoryPeriodYear, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                                .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inventoryPeriodMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTax)
                        .addGap(17, 17, 17)
                        .addComponent(rdoTaxUnit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoTaxBlank, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblInventryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inventoryPeriodYear, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inventoryPeriodMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoTaxUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTaxBlank, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTax))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
	private void btnOutputExcelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputExcelActionPerformed
	{//GEN-HEADEREND:event_btnOutputExcelActionPerformed
            this.callReportGeneratorLogic( TotalInventoryReportLogic_TOM.EXPORT_FILE_XLS );
	}//GEN-LAST:event_btnOutputExcelActionPerformed
        
	private void btnOutputPdfActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputPdfActionPerformed
	{//GEN-HEADEREND:event_btnOutputPdfActionPerformed
            this.callReportGeneratorLogic( TotalInventoryReportLogic_TOM.EXPORT_FILE_PDF );
	}//GEN-LAST:event_btnOutputPdfActionPerformed
        
    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        //既存の棚卸データ（ヘッダ）を読み込む
        ir.setShop((MstShop)shop.getSelectedItem());
        
        try {
            ir.load(SystemInfo.getConnection());
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        setInventrySpan();
    }//GEN-LAST:event_shopActionPerformed
    //初期化
    private void init() {
        this.setInventrySpan();
    }
    
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnOutputPdf);
        SystemInfo.addMouseCursorChange(btnOutputExcel);
    }
    
    private void setKeyListener() {
        shop.addKeyListener(SystemInfo.getMoveNextField());
        shop.addFocusListener(SystemInfo.getSelectText());
        
        
        inventoryPeriodYear.addKeyListener(SystemInfo.getMoveNextField());
        inventoryPeriodYear.addFocusListener(SystemInfo.getSelectText());
        inventoryPeriodMonth.addKeyListener(SystemInfo.getMoveNextField());
        inventoryPeriodMonth.addFocusListener(SystemInfo.getSelectText());
        
        rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
        rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutputExcel;
    private javax.swing.JButton btnOutputPdf;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox inventoryPeriodMonth;
    private javax.swing.JComboBox inventoryPeriodYear;
    private javax.swing.JLabel lblInventryPeriod;
    private javax.swing.JLabel lblTax;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    // End of variables declaration//GEN-END:variables
    
    /**
     *
     */
    private static class ComboBoxItem<T>
    {
        private String	displayString;
        private T		value;
        
        public ComboBoxItem(String displayString, T value) {
            this.displayString = displayString;
            this.value = value;
        }
        
        public T getValue() {
            return this.value;
        }
        
        public String toString() {
            return this.displayString;
        }
        
        public boolean equals(Object o) {
            if (!(o instanceof ComboBoxItem)) {
                return false;
            }
            
            ComboBoxItem<T> v = (ComboBoxItem<T>) o;
            
            return getValue().equals(v.getValue());
        }
    }
    
    
    private LocalFocusTraversalPolicy   ftp;
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
        return  ftp;
    }
    
    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
            extends FocusTraversalPolicy {
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy() {
            controls.add(shop);
            controls.add(inventoryPeriodYear);
            controls.add(inventoryPeriodMonth);
            controls.add(rdoTaxUnit);
            controls.add(rdoTaxBlank);
            
            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(shop);
            controls.add(inventoryPeriodYear);
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

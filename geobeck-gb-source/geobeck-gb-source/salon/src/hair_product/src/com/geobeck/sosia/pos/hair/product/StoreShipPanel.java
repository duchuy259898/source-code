/*
 * StoreShipPanel.java
 *
 * Created on 2008/09/11, 17:48
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.product.logic.StoreShipPanelReportLogic;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author  s_matsumura
 */
public class StoreShipPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    // 表示区分
    public static int SELL_PROPER  = 1;     // 店販
    public static int USE_PROPER   = 2;     // 業務
    // 出力条件
    public static int DATA_EXIST   = 0;     // データあり
    public static int DATA_NOTHING = 1;     // データなし
    public static int DATA_ALL     = 2;     // 全て
    
    private PrintInventry	ia	=	new PrintInventry();
    private InventryPeriod	ib	=	new InventryPeriod();
    
    /**
     * Creates new form StoreShipPanel
     */
    public StoreShipPanel() {
        super();
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        addMouseCursorChange();
        this.setSize(463, 170);
        this.setPath("帳票");
        this.setTitle("入出庫一覧表");
        SystemInfo.initGroupShopComponents(shop, 2);
        this.setKeyListener();
        //this.setListener();
        this.init();
        
        //棚卸区分
        rdoUseProper.setSelected(true);
        rdoSellProper.setSelected(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        shopLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblInventry = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        rdoSellProper = new javax.swing.JRadioButton();
        rdoUseProper = new javax.swing.JRadioButton();
        btnOutputPdf = new javax.swing.JButton();
        suppliersNo = new javax.swing.JTextField();
        suppliers = new javax.swing.JComboBox();
        rdoOnInput = new javax.swing.JRadioButton();
        rdoOffInput = new javax.swing.JRadioButton();
        rdoALL = new javax.swing.JRadioButton();
        inventryPeriod = new javax.swing.JComboBox();
        btnOutputExcel = new javax.swing.JButton();

        shopLabel.setText("\u5e97\u8217");

        jLabel2.setText("\u4ed5\u5165\u5148");

        lblInventry.setText("\u8868\u793a\u533a\u5206");

        jLabel3.setText("\u51fa\u529b\u6761\u4ef6");

        jLabel4.setText("\u96c6\u8a08\u671f\u9593");

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoSellProper);
        rdoSellProper.setText("\u5e97\u8ca9\u7528");
        rdoSellProper.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoSellProper.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoSellProper.setOpaque(false);
        rdoSellProper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSellProperActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoUseProper);
        rdoUseProper.setSelected(true);
        rdoUseProper.setText("\u696d\u52d9\u7528");
        rdoUseProper.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoUseProper.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoUseProper.setOpaque(false);
        rdoUseProper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoUseProperActionPerformed(evt);
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

        suppliersNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        suppliersNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                suppliersNoFocusLost(evt);
            }
        });

        suppliers.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        suppliers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliersActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdoOnInput);
        rdoOnInput.setSelected(true);
        rdoOnInput.setText("\u5165\u529b\u3042\u308a");
        rdoOnInput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoOnInput.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoOnInput.setOpaque(false);

        buttonGroup2.add(rdoOffInput);
        rdoOffInput.setText("\u5165\u529b\u306a\u3057");
        rdoOffInput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoOffInput.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoOffInput.setOpaque(false);

        buttonGroup2.add(rdoALL);
        rdoALL.setText("\u5168\u3066");
        rdoALL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoALL.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoALL.setOpaque(false);

        inventryPeriod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        btnOutputExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutputExcel.setBorderPainted(false);
        btnOutputExcel.setContentAreaFilled(false);
        btnOutputExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutputExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(shopLabel)
                    .addComponent(jLabel2)
                    .addComponent(lblInventry))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                        .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(inventryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(suppliersNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdoOnInput)
                                    .addComponent(rdoSellProper))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rdoOffInput)
                                        .addGap(19, 19, 19)
                                        .addComponent(rdoALL))
                                    .addComponent(rdoUseProper))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(suppliersNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rdoUseProper, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblInventry, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoSellProper, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoOnInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoOffInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoALL, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inventryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void rdoUseProperActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rdoUseProperActionPerformed
	{//GEN-HEADEREND:event_rdoUseProperActionPerformed
		try {
            initPeriod();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
	}//GEN-LAST:event_rdoUseProperActionPerformed

	private void rdoSellProperActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rdoSellProperActionPerformed
	{//GEN-HEADEREND:event_rdoSellProperActionPerformed
		try {
            initPeriod();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
	}//GEN-LAST:event_rdoSellProperActionPerformed
    
	private void btnOutputExcelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputExcelActionPerformed
	{//GEN-HEADEREND:event_btnOutputExcelActionPerformed
		this.callGenerateReportLogic( StoreShipPanelReportLogic.EXPORT_FILE_XLS );
	}//GEN-LAST:event_btnOutputExcelActionPerformed
        
	private void suppliersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_suppliersActionPerformed
	{//GEN-HEADEREND:event_suppliersActionPerformed
		UIUtil.outputSupplier(suppliers, suppliersNo);
	}//GEN-LAST:event_suppliersActionPerformed
        
	private void suppliersNoFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_suppliersNoFocusLost
	{//GEN-HEADEREND:event_suppliersNoFocusLost
		UIUtil.selectSupplier(suppliersNo, suppliers);
	}//GEN-LAST:event_suppliersNoFocusLost
        
        private void setSuppliers(Integer suppliersNo) {
            suppliers.setSelectedIndex(0);
            
            for(int i = 1; i < suppliers.getItemCount(); i ++) {
                MstSupplier	ms	=	(MstSupplier)suppliers.getItemAt(i);
                
                if(ms.getSupplierNo().equals(suppliersNo)) {
                    suppliers.setSelectedIndex(i);
                }
            }
        }
        
        private void init() {
            try {
                this.initSupplier();
                this.initPeriod();
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        /**
         * 仕入先を初期化する。
         */
        private void initSupplier() throws SQLException {
            suppliers.removeAllItems();
            
            for(MstSupplier ms : ia.getSuppliers()) {
                suppliers.addItem(ms);
            }
            
            suppliers.setSelectedIndex(0);
        }
        
        //棚卸期間
        private void initPeriod() throws SQLException {
            ConnectionWrapper cw = SystemInfo.getConnection();
            inventryPeriod.removeAllItems();
            MstShop		ms	=	(MstShop)shop.getSelectedItem();
            ib.setcutoffday(ms.getCutoffDay());
            ib.setShop(ms.getShopID());
			if (rdoSellProper.isSelected())
			{
				ib.setInventoryDivision(1);
			}
			else if (rdoUseProper.isSelected())
			{
				ib.setInventoryDivision(2);
			}
            DateRange[] dateRangeArray = ib.getInventrydate(cw, true);
            if (dateRangeArray.length > 0) {
                for(DateRange obj : dateRangeArray) {
                    inventryPeriod.addItem(obj);
                }
            } else {
                DateRange dr = new DateRange();
                dr.setTo(new Date());
                inventryPeriod.addItem(dr);
            }
            
            inventryPeriod.setSelectedIndex(0);
        }
        
        
        private void addMouseCursorChange() {
            SystemInfo.addMouseCursorChange(btnOutputPdf);
            SystemInfo.addMouseCursorChange(btnOutputExcel);
        }
        
        private boolean checkInputs() {
            if( ( this.suppliers.getSelectedIndex() < 0 ) ||
                    this.suppliersNo.getText().equals( "" ) ) {
                MessageDialog.showMessageDialog( this,
                        MessageUtil.getMessage(12003),
                        this.getTitle(),JOptionPane.ERROR_MESSAGE );
                return false;
            }
            
            if( this.inventryPeriod.getSelectedIndex() < 0 ) {
                //display message
                MessageDialog.showMessageDialog( this,
                        MessageUtil.getMessage(12004),
                        this.getTitle(),JOptionPane.ERROR_MESSAGE );
                return false;
            }
            
            return true;
        }
        
        protected void callGenerateReportLogic( int reportFileType ) {
            if( this.checkInputs() == false ) {
                return;
            }
            
            /**
             * Prepare parameters to pass to StoreShipPanelReportLogic constructor
             */
            int inventoryType = 0;
            int outputType = 0;
            MstShop shopInfo = null;
            MstSupplier supplierInfo = null;
            DateRange dateRange = (DateRange) inventryPeriod.getSelectedItem();
            
            // 表示区分
            if(this.rdoUseProper.isSelected()) {
                inventoryType = this.USE_PROPER;
            } else {
                inventoryType = this.SELL_PROPER;
            }
            
            //出力条件
            if(this.rdoOnInput.isSelected()) {
                outputType = this.DATA_EXIST;
            } else if( this.rdoOffInput.isSelected() ) {
                outputType = this.DATA_NOTHING;
            } else {
                outputType = this.DATA_ALL;
            }
            
            if( shop.getSelectedItem() instanceof MstShop ) {
                shopInfo = (MstShop)shop.getSelectedItem();
            } else {
                shopInfo = SystemInfo.getCurrentShop();
            }
            
            if(suppliers.getSelectedItem() instanceof MstSupplier) {
                supplierInfo = (MstSupplier)suppliers.getSelectedItem();
            }
            
            //create instance of StoreShipPanelReportLogic
            StoreShipPanelReportLogic pickupDeliveryReport = new StoreShipPanelReportLogic(
                    shopInfo, supplierInfo, inventoryType,
                    outputType, dateRange );
            
            int result = pickupDeliveryReport.generateReport( reportFileType );
            
            if(result == StoreShipPanelReportLogic.RESULT_SUCCESS ){
                // 成功
            }else if(result == StoreShipPanelReportLogic.RESULT_DATA_NOTHNIG ){
                // データなし
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(4001),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                
            }else if(result == StoreShipPanelReportLogic.RESULT_ERROR ){
                // 予期せぬエラー
                MessageDialog.showMessageDialog( this,
                        MessageUtil.getMessage(1099),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE );
            }
        }
        
	private void btnOutputPdfActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputPdfActionPerformed
	{//GEN-HEADEREND:event_btnOutputPdfActionPerformed
            this.callGenerateReportLogic( StoreShipPanelReportLogic.EXPORT_FILE_PDF );
	}//GEN-LAST:event_btnOutputPdfActionPerformed
        
    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        try {
            initPeriod();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_shopActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutputExcel;
    private javax.swing.JButton btnOutputPdf;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox inventryPeriod;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblInventry;
    private javax.swing.JRadioButton rdoALL;
    private javax.swing.JRadioButton rdoOffInput;
    private javax.swing.JRadioButton rdoOnInput;
    private javax.swing.JRadioButton rdoSellProper;
    private javax.swing.JRadioButton rdoUseProper;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JComboBox suppliers;
    private javax.swing.JTextField suppliersNo;
    // End of variables declaration//GEN-END:variables
    
    // <editor-fold defaultstate="collapsed" desc="Focus Traversal Code">                          
    private LocalFocusTraversalPolicy   ftp;
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy()
    {
        return  ftp;
    }
    
    private void setKeyListener()
    {
        shop.addKeyListener(SystemInfo.getMoveNextField());
        suppliersNo.addKeyListener(SystemInfo.getMoveNextField());
        rdoSellProper.addKeyListener(SystemInfo.getMoveNextField());
        rdoUseProper.addKeyListener(SystemInfo.getMoveNextField());
        rdoOnInput.addKeyListener(SystemInfo.getMoveNextField());
        rdoOffInput.addKeyListener(SystemInfo.getMoveNextField());
        rdoALL.addKeyListener(SystemInfo.getMoveNextField());
        inventryPeriod.addKeyListener(SystemInfo.getMoveNextField());
        
        shop.addFocusListener(SystemInfo.getSelectText());
        suppliersNo.addFocusListener(SystemInfo.getSelectText());
        rdoSellProper.addFocusListener(SystemInfo.getSelectText());
        rdoUseProper.addFocusListener(SystemInfo.getSelectText());
        rdoOnInput.addFocusListener(SystemInfo.getSelectText());
        rdoOffInput.addFocusListener(SystemInfo.getSelectText());
        rdoALL.addFocusListener(SystemInfo.getSelectText());
        inventryPeriod.addFocusListener(SystemInfo.getSelectText());
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
            
            controls.add(shop);
            controls.add(suppliersNo);
            controls.add(rdoSellProper);
            controls.add(rdoUseProper);
            controls.add(rdoOnInput);
            controls.add(rdoOffInput);
            controls.add(rdoALL);
            controls.add(inventryPeriod);

            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(shop);
            controls.add(suppliersNo);
        };
            
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                                           Component aComponent)
        {
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
                                            Component aComponent)
        {
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
        public Component getFirstComponent(Container aContainer)
        {
            return getDefaultComponent(aContainer);
        }

        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer)
        {
            return getComponentBefore(aContainer, controls.get(0));
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer)
        {
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
        public Component getInitialComponent(Window window)
        {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
    }
    // </editor-fold>
    
}

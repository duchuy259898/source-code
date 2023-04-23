/*
 * StaffSalesListPanel.java
 *
 * Created on 2008/09/16, 16:46
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.product.logic.BackProductLogic;
import com.geobeck.sosia.pos.hair.product.logic.StaffSalesListLogic;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.util.Calendar;
/**
 *
 * @author  s_matsumura
 */
public class StaffSalesListPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    private PrintInventry	ia	=	new PrintInventry();
    private InventryPeriod	ib	=	new InventryPeriod();

    private final int INVALID_TIME  = 1;
    private final int MISSING_STAFF = 2;
    
    private int fileType  = 0;
	/** Creates new form StaffSalesListPanel */
    public StaffSalesListPanel() 
    {
        super();
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        addMouseCursorChange();
        this.setSize(500, 170);
        this.setPath("商品管理 >> 帳票");
        this.setTitle("スタッフ販売明細");
        SystemInfo.initGroupShopComponents(shop, 2);
        this.setKeyListener();
        //this.setListener();
		
        this.initStaff( staff );
        cmbTargetPeriodStart.setDate(new java.util.Date());
        cmbTargetPeriodEnd.setDate(new java.util.Date());

        //税抜、税込の初期設定
        rdoTaxBlank.setSelected(false);
        rdoTaxUnit.setSelected(true);
    }
    
    
     /**
     * 担当者を初期化する。
     */
    protected void initStaff( JComboBox cb )
    {
        cb.addItem(new MstStaff());
        SystemInfo.initStaffComponent(cb);

        cb.setSelectedIndex(0);
    }

	private int checkInput()
    {
        int isErr = 0;
        
        Calendar toDate = Calendar.getInstance();
        toDate.setTime(this.cmbTargetPeriodEnd.getDate());
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTime(this.cmbTargetPeriodStart.getDate());
        
        if( fromDate.after(toDate))
        {
            isErr = INVALID_TIME;
        }
        
        //if( 0 == isErr )
       // {
        //    if( this.staffNo.equals("") )
        //    {
       //         isErr = MISSING_STAFF;
       //     }
       // }
        return isErr;
    }
    
    private void generateStaffInventory()
    {
        int err = this.checkInput();
        int taxType = 0;
        StaffSalesListLogic logic = null;
        MstStaff staff = null;
        
        if( 0 == err )
        {
            MstShop ms     = (MstShop)this.shop.getSelectedItem();
            if( !this.staffNo.getText().equals("") )
            {
                staff = (MstStaff)this.staff.getSelectedItem();
            }
            
            if( this.rdoTaxBlank.isSelected() )
            {
                taxType = BackProductLogic.TAX_EXCLUDED;
            }
            else if( this.rdoTaxUnit.isSelected() )
            {
                taxType = BackProductLogic.TAX_INCLUDED;
            }
            
            logic = new StaffSalesListLogic(ms, staff,  this.cmbTargetPeriodEnd.getDate(), this.cmbTargetPeriodStart.getDate(),
                        taxType,this.fileType);
            
            int result = logic.viewStaffSalesReport();

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
        else
        {
            this.displayMessage(err);
        }
    }

	private void displayMessage(int messageid )
    {
        switch(messageid)
        {
            case INVALID_TIME:
                MessageDialog.showMessageDialog( this.getParentFrame(), 
                        MessageUtil.getMessage(12005),
                        JOptionPane.ERROR_MESSAGE );
                break;
            case MISSING_STAFF:     
                 MessageDialog.showMessageDialog( this.getParentFrame(), 
                        MessageUtil.getMessage(12007),
                        JOptionPane.ERROR_MESSAGE );
                break;
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup1 = new javax.swing.ButtonGroup();
        shopLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnOutputPdf = new javax.swing.JButton();
        staffNo = new javax.swing.JTextField();
        staff = new javax.swing.JComboBox();
        cmbTargetPeriodStart = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel1 = new javax.swing.JLabel();
        cmbTargetPeriodEnd = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        btnOutputExcel = new javax.swing.JButton();

        shopLabel.setText("\u5e97\u8217");

        jLabel2.setText("\u30b9\u30bf\u30c3\u30d5");

        jLabel5.setText("\u96c6\u8a08\u671f\u9593");

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
        shop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                shopActionPerformed(evt);
            }
        });

        btnOutputPdf.setIcon(SystemInfo.getImageIcon("/button/print/output_pdf_off.jpg"));
        btnOutputPdf.setBorderPainted(false);
        btnOutputPdf.setContentAreaFilled(false);
        btnOutputPdf.setPressedIcon(SystemInfo.getImageIcon("/button/print/output_pdf_on.jpg"));
        btnOutputPdf.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOutputPdfActionPerformed(evt);
            }
        });

        staffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffNo.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                staffNoFocusLost(evt);
            }
        });

        staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                staffActionPerformed(evt);
            }
        });

        cmbTargetPeriodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStart.setForeground(java.awt.Color.white);
        cmbTargetPeriodStart.setDefaultDate("2006/05/10");
        cmbTargetPeriodStart.setMaximumSize(new java.awt.Dimension(65, 20));
        cmbTargetPeriodStart.setMinimumSize(new java.awt.Dimension(65, 20));
        cmbTargetPeriodStart.setPreferredSize(new java.awt.Dimension(85, 20));
        cmbTargetPeriodStart.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                cmbTargetPeriodStartFocusGained(evt);
            }
        });

        jLabel1.setText("\uff5e");

        cmbTargetPeriodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                cmbTargetPeriodEndFocusGained(evt);
            }
        });

        btnOutputExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutputExcel.setBorderPainted(false);
        btnOutputExcel.setContentAreaFilled(false);
        btnOutputExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutputExcel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
                    .addComponent(shopLabel)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(lblTax))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                        .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(staffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTaxUnit))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(staff, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(rdoTaxBlank, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                            .addComponent(shopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staff, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTax, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTaxUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTaxBlank, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

      
	private void btnOutputExcelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputExcelActionPerformed
	{//GEN-HEADEREND:event_btnOutputExcelActionPerformed
             this.fileType = StaffSalesListLogic.EXPORT_FILE_XLS;
             this.generateStaffInventory();
	}//GEN-LAST:event_btnOutputExcelActionPerformed

    private void cmbTargetPeriodEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndFocusGained
        cmbTargetPeriodEnd.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndFocusGained

    private void cmbTargetPeriodStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartFocusGained
        cmbTargetPeriodStart.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartFocusGained

    private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed
        UIUtil.outputStaff(staff, staffNo);
    }//GEN-LAST:event_staffActionPerformed

    private void staffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staffNoFocusLost
		UIUtil.selectStaff(staffNo, staff);
    }//GEN-LAST:event_staffNoFocusLost

	private void btnOutputPdfActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputPdfActionPerformed
	{//GEN-HEADEREND:event_btnOutputPdfActionPerformed
        
            this.fileType = StaffSalesListLogic.EXPORT_FILE_PDF;
            this.generateStaffInventory();
	}//GEN-LAST:event_btnOutputPdfActionPerformed

    
    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
       
    }//GEN-LAST:event_shopActionPerformed
    
    private void addMouseCursorChange()
    {
            SystemInfo.addMouseCursorChange(btnOutputPdf);
            SystemInfo.addMouseCursorChange(btnOutputExcel);
    }
    
    private void setKeyListener()
    {
        shop.addKeyListener(SystemInfo.getMoveNextField());
        shop.addFocusListener(SystemInfo.getSelectText());
        staff.addKeyListener(SystemInfo.getMoveNextField());
        staff.addFocusListener(SystemInfo.getSelectText());
        staffNo.addKeyListener(SystemInfo.getMoveNextField());
        staffNo.addFocusListener(SystemInfo.getSelectText());
        rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
        rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodStart.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodStart.addFocusListener(SystemInfo.getSelectText());
        cmbTargetPeriodEnd.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodEnd.addFocusListener(SystemInfo.getSelectText());
    }
    
   
    // <editor-fold defaultstate="collapsed" desc=" Variables declaration - do not modify">    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutputExcel;
    private javax.swing.JButton btnOutputPdf;
    private javax.swing.ButtonGroup buttonGroup1;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEnd;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblTax;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    protected javax.swing.JComboBox staff;
    private javax.swing.JTextField staffNo;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
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
            controls.add(staffNo);
            controls.add(cmbTargetPeriodStart);
            controls.add(cmbTargetPeriodEnd);
            controls.add(rdoTaxUnit);
            controls.add(rdoTaxBlank);
            
            for( Component control : controls ){
                control.addKeyListener(SystemInfo.getMoveNextField());
                control.addFocusListener(SystemInfo.getSelectText());
            }

            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(shop);
            controls.add(staffNo);
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

/*
 * TechnicSalesReportPanelTom.java
 *
 * Created on 2008/09/17, 11:37
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.util.MessageUtil;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import java.util.Calendar;
import javax.swing.JOptionPane;
import com.geobeck.swing.MessageDialog;

import com.geobeck.sosia.pos.basicinfo.company.StaffWorkRegistrationDAO;
import com.geobeck.sosia.pos.basicinfo.company.StaffBasicInfo;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.report.logic.TechnicSalesReportLogic;
import com.geobeck.sosia.pos.hair.report.logic.DailyTechnicSalesReportLogic;
import com.geobeck.sosia.pos.hair.report.logic.MonthlySalesReportLogic;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sosia.pos.hair.report.beans.SalesDateBean;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;

/**
 *
 * @author shiera.delusa
 */
public class TechnicSalesReportPanelTom extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    protected ArrayList<StaffBasicInfo> staffBasicInfoList;
    protected ArrayList<SalesDateBean> salesDateList;
    private boolean staffNameListInitialized = false;
    private boolean staffIdChanged = false;
    private MstShop shopInfo = null;
    
    private static final int MISSING_FIELD_STAFF_INFO = 1;
    
    //Report Generation Parameters:
    protected boolean isDailyReport = true;
        
    /**
     * Creates new form TechnicSalesReportPanelTom
     */
    public TechnicSalesReportPanelTom()
    {
        initClass();
    }
        
    private void initClass()
    {
        initComponents();
        addMouseCursorChange();
        this.setSize(600, 400);
        this.setTitle("月間売上");
        
        SystemInfo.initGroupShopComponents( this.shopNameCombo, 2 );        
        this.shopInfo = (MstShop)this.shopNameCombo.getSelectedItem();
        
        this.initStaffListCombobox();
        this.initDateComboBoxes();
        staffNameListInitialized = true;
    }
    
    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(excelButton);
    }
    
    protected void clearComboBoxes()
    {
        DefaultComboBoxModel model = (DefaultComboBoxModel)this.yearComboBox.getModel();
        model.removeAllElements();
        
        model = (DefaultComboBoxModel)this.monthComboBox.getModel();
        model.removeAllElements();
        
        model = (DefaultComboBoxModel)this.staffNameComboBox.getModel();
        model.removeAllElements();
    }
    
    protected void initDateComboBoxes()
    {
        if( this.initYearComboBox() )
        {
            this.initMonthComboBox();
        }
    }
    
    protected boolean initYearComboBox()
    {
        this.salesDateList = TechnicSalesReportLogic.getSalesDateBean( this.shopInfo.getShopID());
        
        if( this.salesDateList != null )
        {
            if( this.salesDateList.isEmpty() == false )
            {
                int size = this.salesDateList.size();
                String[] items = new String[size];
                String temp = null;
                
                //initialize year combo box:
                for( int index=0; index < size; index++ )
                {
                    items[index] = this.salesDateList.get( index ).getYear();
                    items[index] += "年";
                }
                DefaultComboBoxModel salesYearListModel = new DefaultComboBoxModel(items);
                this.yearComboBox.setModel( salesYearListModel );
                
                this.yearComboBox.setSelectedIndex( 0 );
            }
            else
            {
                this.yearComboBox.setSelectedIndex( -1 );
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected void initMonthComboBox()
    {
        int index = this.yearComboBox.getSelectedIndex();
        if( index < 0 )
            return;
        
        ArrayList<String> months = this.salesDateList.get( index ).getMonths();
        
        if( months == null )
            return;
        
        if( months != null )
        {
            if( months.isEmpty() == false )
            {
                int size = months.size();
                String[] items = new String[size];
                String temp = null;
                
                //initialize month combo box:
                for( index=0; index < size; index++ )
                {
                    items[index] = months.get( index );
                    items[index] += "月";
                }
                DefaultComboBoxModel salesMonthListModel = new DefaultComboBoxModel(items);
                this.monthComboBox.setModel( salesMonthListModel );
                this.monthComboBox.setSelectedIndex( 0 );
            }
        }   
    }
    
    protected void initStaffListCombobox()
    {
        this.staffBasicInfoList = StaffWorkRegistrationDAO.getStaffList( this.shopInfo.getShopID());
        if( this.staffBasicInfoList != null )
        {
            if( this.staffBasicInfoList.isEmpty() == false )
            {
                int size = this.staffBasicInfoList.size();
                String[] items = new String[size];
                String temp = null;
                
                for( int index=0; index < size; index++ )
                {
                    items[index] = staffBasicInfoList.get( index ).getStaffName();
                }
                DefaultComboBoxModel staffListModel = new DefaultComboBoxModel(items);
                this.staffNameComboBox.setModel( staffListModel );
                this.staffNameComboBox.setSelectedIndex( -1 );
            }
        }   
    }
    
    protected String findName( String staffNo )
    {
        String name = null;
        
        for( int index=0; index < this.staffBasicInfoList.size(); index++ )
        {
            if( (this.staffBasicInfoList.get(index)).getStaffNo().equals(staffNo) )
            {
                name = staffBasicInfoList.get(index).getStaffName();
                break;
            }
        }
        return name;
    }
    
    protected void changeSelectedById( String staffNo )
    {
        String selectedName = findName(staffNo);
        if ( selectedName != null ) {
            this.staffNameComboBox.setSelectedItem( selectedName );
        } else {
            this.staffNameComboBox.setSelectedIndex( -1 );
        }
    }
        
    private boolean getInputFieldValues()
    {
        SalesDateBean selectedDate = this.salesDateList.get( this.yearComboBox.getSelectedIndex() );
        if ( selectedDate == null ) {
            return false;
        }

        Calendar reportCal = Calendar.getInstance();
        reportCal.set( Calendar.YEAR, Integer.parseInt( selectedDate.getYear() ) );
        reportCal.set( Calendar.MONTH, Integer.parseInt( selectedDate.getMonth( this.monthComboBox.getSelectedIndex()))-1);
        Calendar today = Calendar.getInstance();
        
        if ( ( today.get( Calendar.MONTH ) == reportCal.get( Calendar.MONTH ) ) &&
                (( today.get( Calendar.YEAR ) == reportCal.get( Calendar.YEAR ) )) )
        {
            reportCal.set( Calendar.DATE, today.get( Calendar.DATE ) );
        }
        else
        {
            reportCal.set( Calendar.DATE, reportCal.getActualMaximum( Calendar.DATE ) );
        }

        this.isDailyReport = this.dailySalesReportBtn.isSelected();
        
        return true;
    }
        
    private void displayMessage( int msgCode )
    {
        switch( msgCode )
        {
            case MISSING_FIELD_STAFF_INFO:
                MessageDialog.showMessageDialog( this.getParentFrame(), 
                    "担当者の情報を入力してください。", 
                    JOptionPane.ERROR_MESSAGE );
                break;
                
            default:
                break;
        }
    }
    
    private void updateStaffInputFields()
    {
        if ( this.staffNameListInitialized && this.staffIdChanged == false ) {
            
            if ( staffBasicInfoList != null ) {
                
                int selectedIdx = this.staffNameComboBox.getSelectedIndex();
                if ( selectedIdx < 0 ) {
                    this.staffIdInput.setText( "" );
                    return;
                } else {
                    if ( this.staffBasicInfoList.get(selectedIdx) != null ) {
                        String staffNo = String.valueOf( this.staffBasicInfoList.get(selectedIdx).getStaffNo() );
                        this.staffIdInput.setText( staffNo );
                    }
                }
            }
            
        } else {
            staffIdChanged = false;
        }
    }

	private void performButtonPressed(int nOutputType)
	{
            if ( this.getInputFieldValues() == false ) return;
            
            SalesDateBean selectedDate = this.salesDateList.get( this.yearComboBox.getSelectedIndex() );
            if ( selectedDate == null ) return;

            int nYear, nMonth;
            int nRet;

            nYear = Integer.parseInt( selectedDate.getYear() );
            nMonth = Integer.parseInt( selectedDate.getMonth( this.monthComboBox.getSelectedIndex()));

            if ( this.isDailyReport ) {
                
                DailyTechnicSalesReportLogic dailySalesReport = new DailyTechnicSalesReportLogic( nYear, nMonth, this.staffBasicInfoList, this.staffNameComboBox.getSelectedIndex(), rdoShopCurrent.isSelected(), this.shopInfo );
                nRet = dailySalesReport.generateReport( nOutputType );
                
            } else {
                
                MonthlySalesReportLogic monthlySalesReport = new MonthlySalesReportLogic( nYear, nMonth, this.shopInfo );
                nRet = monthlySalesReport.generateReport( nOutputType );
            }

            switch (nRet)
            {
                case ReportGeneratorLogic.RESULT_DATA_NOTHNIG:
                    // データなし
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(4001),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);

                                break;

		case ReportGeneratorLogic.RESULT_ERROR:
                    // 予期せぬエラー
                    MessageDialog.showMessageDialog( this,
                            MessageUtil.getMessage(1099),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE );

                                break;
            }

	}
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reportTypeButtonGroup = new javax.swing.ButtonGroup();
        summaryTypeButtonGroup = new javax.swing.ButtonGroup();
        dailySalesReportBtn = new javax.swing.JRadioButton();
        monthlySalesReportBtn = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        yearComboBox = new javax.swing.JComboBox();
        monthComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        staffIdInput = new com.geobeck.swing.JFormattedTextFieldEx();
        staffNameComboBox = new javax.swing.JComboBox();
        excelButton = new javax.swing.JButton();
        shopNameCombo = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel3 = new javax.swing.JLabel();
        summaryLabel = new javax.swing.JLabel();
        rdoShopCurrent = new javax.swing.JRadioButton();
        rdoShopAll = new javax.swing.JRadioButton();

        reportTypeButtonGroup.add(dailySalesReportBtn);
        dailySalesReportBtn.setSelected(true);
        dailySalesReportBtn.setText("個別月間売上");
        dailySalesReportBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dailySalesReportBtn.setBorderPainted(true);
        dailySalesReportBtn.setContentAreaFilled(false);
        dailySalesReportBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dailySalesReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dailySalesReportBtnActionPerformed(evt);
            }
        });

        reportTypeButtonGroup.add(monthlySalesReportBtn);
        monthlySalesReportBtn.setText("店舗月間売上");
        monthlySalesReportBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        monthlySalesReportBtn.setContentAreaFilled(false);
        monthlySalesReportBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        monthlySalesReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthlySalesReportBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("集計期間");

        yearComboBox.setMaximumRowCount(12);
        yearComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearComboBoxActionPerformed(evt);
            }
        });

        monthComboBox.setMaximumRowCount(12);

        jLabel2.setText("担当者");

        staffIdInput.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                staffIdInputCaretUpdate(evt);
            }
        });

        staffNameComboBox.setMaximumRowCount(12);
        staffNameComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                staffNameComboBoxMousePressed(evt);
            }
        });
        staffNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffNameComboBoxActionPerformed(evt);
            }
        });
        staffNameComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                staffNameComboBoxKeyReleased(evt);
            }
        });

        excelButton.setIcon(SystemInfo.getImageIcon( "/button/print/excel_off.jpg" ));
        excelButton.setBorder(null);
        excelButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        excelButton.setPressedIcon(SystemInfo.getImageIcon( "/button/print/excel_on.jpg" ));
        excelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelButtonActionPerformed(evt);
            }
        });

        shopNameCombo.setMaximumRowCount(12);
        shopNameCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopNameComboActionPerformed(evt);
            }
        });

        jLabel3.setText("店舗名");

        summaryLabel.setText("集計対象");

        summaryTypeButtonGroup.add(rdoShopCurrent);
        rdoShopCurrent.setSelected(true);
        rdoShopCurrent.setText("自店");
        rdoShopCurrent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoShopCurrent.setBorderPainted(true);
        rdoShopCurrent.setContentAreaFilled(false);
        rdoShopCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));

        summaryTypeButtonGroup.add(rdoShopAll);
        rdoShopAll.setText("全店");
        rdoShopAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoShopAll.setBorderPainted(true);
        rdoShopAll.setContentAreaFilled(false);
        rdoShopAll.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dailySalesReportBtn)
                        .addGap(30, 30, 30)
                        .addComponent(monthlySalesReportBtn)
                        .addGap(68, 68, 68)
                        .addComponent(excelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(summaryLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(shopNameCombo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(staffIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoShopCurrent))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(rdoShopAll))
                                    .addComponent(staffNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(excelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthlySalesReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dailySalesReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shopNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(monthComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(staffNameComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffIdInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(summaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoShopCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoShopAll, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(94, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

        private void setStaff(String staffNo)
        {
            staffNameComboBox.setSelectedIndex(0);

            for (int i = 1; i < staffNameComboBox.getItemCount(); i++) {
                if (((MstStaff)staffNameComboBox.getItemAt(i)).getStaffNo().equals(staffNo)) {
                    staffNameComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
    private void dailySalesReportBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_dailySalesReportBtnActionPerformed
    {//GEN-HEADEREND:event_dailySalesReportBtnActionPerformed
        this.staffIdInput.setEnabled( true );
        this.staffNameComboBox.setEnabled( true );
        this.rdoShopCurrent.setEnabled( true );
        this.rdoShopAll.setEnabled( true );
    }//GEN-LAST:event_dailySalesReportBtnActionPerformed

    private void monthlySalesReportBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_monthlySalesReportBtnActionPerformed
    {//GEN-HEADEREND:event_monthlySalesReportBtnActionPerformed
        this.staffIdInput.setEnabled( false );
        this.staffNameComboBox.setEnabled( false );
        this.rdoShopCurrent.setEnabled( false );
        this.rdoShopAll.setEnabled( false );
    }//GEN-LAST:event_monthlySalesReportBtnActionPerformed

    private void shopNameComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shopNameComboActionPerformed
    {//GEN-HEADEREND:event_shopNameComboActionPerformed
        this.shopInfo = (MstShop)this.shopNameCombo.getSelectedItem();
        this.clearComboBoxes();
        this.initStaffListCombobox();
        this.initDateComboBoxes();
    }//GEN-LAST:event_shopNameComboActionPerformed

    private void staffNameComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_staffNameComboBoxActionPerformed
    {//GEN-HEADEREND:event_staffNameComboBoxActionPerformed
        this.updateStaffInputFields();
    }//GEN-LAST:event_staffNameComboBoxActionPerformed

    private void staffNameComboBoxKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_staffNameComboBoxKeyReleased
    {//GEN-HEADEREND:event_staffNameComboBoxKeyReleased
        this.updateStaffInputFields();
    }//GEN-LAST:event_staffNameComboBoxKeyReleased

    private void staffNameComboBoxMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_staffNameComboBoxMousePressed
    {//GEN-HEADEREND:event_staffNameComboBoxMousePressed
        this.updateStaffInputFields();
    }//GEN-LAST:event_staffNameComboBoxMousePressed

    private void yearComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_yearComboBoxActionPerformed
    {//GEN-HEADEREND:event_yearComboBoxActionPerformed
        this.initMonthComboBox();
    }//GEN-LAST:event_yearComboBoxActionPerformed

    private void excelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_excelButtonActionPerformed
    {//GEN-HEADEREND:event_excelButtonActionPerformed

        excelButton.setCursor(null);
        
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            performButtonPressed(ReportGeneratorLogic.EXPORT_FILE_XLS);

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//GEN-LAST:event_excelButtonActionPerformed

    private void staffIdInputCaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_staffIdInputCaretUpdate
    {//GEN-HEADEREND:event_staffIdInputCaretUpdate

        String idInput = this.staffIdInput.getText();
        staffIdChanged = true;
        if ( idInput.length() > 0 ) {
            this.changeSelectedById( idInput );
        } else {
            this.staffNameComboBox.setSelectedIndex( -1 );
        }

    }//GEN-LAST:event_staffIdInputCaretUpdate
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton dailySalesReportBtn;
    private javax.swing.JButton excelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JRadioButton monthlySalesReportBtn;
    private javax.swing.JRadioButton rdoShopAll;
    private javax.swing.JRadioButton rdoShopCurrent;
    private javax.swing.ButtonGroup reportTypeButtonGroup;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shopNameCombo;
    private com.geobeck.swing.JFormattedTextFieldEx staffIdInput;
    private javax.swing.JComboBox staffNameComboBox;
    private javax.swing.JLabel summaryLabel;
    private javax.swing.ButtonGroup summaryTypeButtonGroup;
    private javax.swing.JComboBox yearComboBox;
    // End of variables declaration//GEN-END:variables
    
}

/*
 * TargetPerformanceOperatingDayFrame.java
 *
 * Created on 2013/05/03, 13:52
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.MessageDialog;
import com.geobeck.sosia.pos.util.MessageUtil;
import java.util.Calendar;
import javax.swing.JTextField;

/**
 *
 * @author  nakhoa
 */
public class TargetPerformanceOperatingDayFrame  extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    private final String FRAME_TITLE = "目標 ＆ 実績 ＆ 稼働日数";    
    
    //constants:
    private final static int VERIFY_SAVE_DATA = 13000;    
    private ArrayList<Integer> arrYear;
    private int nCurrentEditingYear;
    private int nCurrentEditingMonth;
    private boolean newPage = true;
    private TargetDayPerformanceTablePanel tableDay = new TargetDayPerformanceTablePanel();
	/** Creates new form TargetActualShopInfoFrame */
	public TargetPerformanceOperatingDayFrame()
	{
           // this.setTitle( FRAME_TITLE );
            initComponents();
            SystemInfo.initGroupShopComponents(cblShop, 2);
            cblShop.setSelectedIndex(0);

          //  this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
           // this.setLocationRelativeTo( null );
            this.setSize(830, 680);
            this.setPath("目標＆実績＆稼働日数");
            this.setTitle("目標＆実績＆稼働日数");

            this.initYearComboBox();
            this.initMonthCombobox();
            this.setCurrentEditingYear();
            this.setCurrentEditingMonth();
            tableMonth.loadYearData( getSelectedShopID(), getCurrentEditingYear());
            this.newPage = false;
	}
        
    
    
    /**
     * OVERRIDING processWindowEvent()
     */
    protected void processWindowEvent(WindowEvent e) {
//        if( e.getID() == WindowEvent.WINDOW_CLOSING )
//        {   
//            this.prepareCloseWindow();
//        }
//        super.processWindowEvent(e);
    }

    private void prepareCloseWindow()
    {
//        if( this.targetMonthPerformanceTablePanel1.isCellEdited() )
//        {
//            if( displayMessage( VERIFY_SAVE_DATA ) == JOptionPane.OK_OPTION )
//            {
//                this.targetMonthPerformanceTablePanel1.registerAllDataToDB( getSelectedShopID(), getCurrentEditingYear());
//                this.targetMonthPerformanceTablePanel1.resetCellEditedFlag();
//            }
//        }
    }    
    private void initYearComboBox()
    {
        monthComboBox.removeAllItems();
        int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            monthComboBox.addItem(String.valueOf(y - i));
        }
        monthComboBox.setSelectedItem(Calendar.YEAR);
        monthComboBox.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ((JTextField) monthComboBox.getEditor().getEditorComponent()).selectAll();
            }
        });
    }
    
    private void initMonthCombobox()
    {
        this.dayComboBox.removeAllItems();
        for(int i=1; i<=12; i++)
        {
            this.dayComboBox.addItem(i);
        }
        this.dayComboBox.setSelectedIndex(0);
    }
    
    private int displayMessage( int messageType )
    {
        int userSelection = JOptionPane.CANCEL_OPTION;
        
        switch( messageType )
        {
            case VERIFY_SAVE_DATA:
            {
                userSelection = MessageDialog.showYesNoDialog( this, 
                        MessageUtil.getMessage( VERIFY_SAVE_DATA ), 
                    this.FRAME_TITLE, JOptionPane.OK_CANCEL_OPTION );
                break;
            }
            default:
                break;
        }
        
        return userSelection;
    }

    /**
     * 選択されている店舗を取得する。
     * @return 選択されている店舗
     */
    private MstShop getSelectedShop() {
        if(0 <= cblShop.getSelectedIndex())
            return	(MstShop)cblShop.getSelectedItem();
        else
            return	null;
    }

    /**
     * 選択されている店舗のIDを取得する。
     * @return 選択されている店舗のID
     */
    private Integer getSelectedShopID() {
        MstShop ms = this.getSelectedShop();

        if (ms != null) {
            return ms.getShopID();
        } else {
            return 0;
        }
    }

    private int getCurrentEditingYear() {
        return nCurrentEditingYear;
    }

    private void setCurrentEditingYear() {
        nCurrentEditingYear = Integer.parseInt(this.monthComboBox.getSelectedItem().toString());
    }

    private void setCurrentEditingMonth() {
        nCurrentEditingMonth = this.monthComboBox.getSelectedIndex() + 1;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        shopLabel = new javax.swing.JLabel();
        cblShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel1 = new javax.swing.JLabel();
        monthComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        dayComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        rdMonth = new javax.swing.JRadioButton();
        rdDay = new javax.swing.JRadioButton();
        displayButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPaneMonth = new javax.swing.JScrollPane();
        tableMonth = new com.geobeck.sosia.pos.hair.basicinfo.company.TargetMonthPerformanceTablePanel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jScrollPane2.setViewportView(jLabel2);

        jPanel2.setOpaque(false);

        shopLabel.setText("店舗");

        cblShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cblShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cblShopActionPerformed(evt);
            }
        });

        jLabel1.setText("年度");

        monthComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthComboBoxActionPerformed(evt);
            }
        });
        monthComboBox.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                monthComboBoxVetoableChange(evt);
            }
        });

        jLabel3.setText("月度");

        dayComboBox.setEditable(true);
        dayComboBox.setEnabled(false);
        dayComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayComboBoxActionPerformed(evt);
            }
        });

        jLabel5.setText("対象");

        rdMonth.setSelected(true);
        rdMonth.setText("月目標");
        rdMonth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdMonthStateChanged(evt);
            }
        });
        rdMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMonthActionPerformed(evt);
            }
        });

        rdDay.setText("日目標");
        rdDay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdDayStateChanged(evt);
            }
        });
        rdDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDayActionPerformed(evt);
            }
        });

        displayButton.setIcon(SystemInfo.getImageIcon( "/button/common/show_off.jpg" ));
        displayButton.setBorder(null);
        displayButton.setBorderPainted(false);
        displayButton.setIconTextGap(0);
        displayButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/show_on.jpg" ));
        displayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayButtonActionPerformed(evt);
            }
        });

        registerButton.setIcon(SystemInfo.getImageIcon( "/button/common/regist_off.jpg" ));
        registerButton.setText("\n");
        registerButton.setBorder(null);
        registerButton.setBorderPainted(false);
        registerButton.setIconTextGap(0);
        registerButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        registerButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/regist_on.jpg" ));
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("年");

        jLabel6.setText("月");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(shopLabel)
                                .add(20, 20, 20)
                                .add(cblShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel1)
                                .add(18, 18, 18)
                                .add(monthComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel4)))
                        .add(0, 815, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel3)
                                .add(18, 18, 18)
                                .add(dayComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel6)
                                .add(0, 865, Short.MAX_VALUE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel5)
                                .add(18, 18, 18)
                                .add(rdMonth)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rdDay)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(displayButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 651, Short.MAX_VALUE)
                                .add(registerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(shopLabel)
                    .add(cblShop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(monthComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dayComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(rdMonth)
                        .add(rdDay))
                    .add(displayButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(registerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jScrollPaneMonth.setViewportView(tableMonth);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPaneMonth, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPaneMonth, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("990");
        getAccessibleContext().setAccessibleDescription("750");
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        if(rdMonth.isSelected()){
            if(!this.tableMonth.registerAllDataToDB()){
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "目標＆実績＆稼働日数"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }else{
            if(!this.tableDay.registerAllDataToDB(WIDTH, WIDTH)){
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "目標＆実績＆稼働日数"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_registerButtonActionPerformed

    private void displayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayButtonActionPerformed
        if(newPage){
            return;
        }
        Integer shopId  = null;
        if( cblShop.getSelectedItem() instanceof MstShop ){
            shopId = ((MstShop)cblShop.getSelectedItem()).getShopID();
        }else{
            shopId = 0;
        }
        int yearNew = Integer.parseInt(monthComboBox.getSelectedItem().toString());
        int monthNew = Integer.parseInt(dayComboBox.getSelectedItem().toString());
        if(this.rdMonth.isSelected()){
            tableMonth = new TargetMonthPerformanceTablePanel();
            this.initMonthCombobox();
            this.setCurrentEditingMonth();
            tableMonth.loadYearData( getSelectedShopID(), yearNew);
            jScrollPaneMonth.setViewportView(tableMonth);
        }else{
            this.nCurrentEditingMonth = this.dayComboBox.getSelectedIndex();
            Calendar cal = Calendar.getInstance();
            cal.set(yearNew, monthNew-1, 1);
            tableDay.setTargetDay(cal.getTime());
            tableDay.setShopId(shopId);
            tableDay.loadYearData();
            jScrollPaneMonth.setViewportView(tableDay);
        }
    }//GEN-LAST:event_displayButtonActionPerformed

    private void cblShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cblShopActionPerformed
        this.initYearComboBox();
    }//GEN-LAST:event_cblShopActionPerformed

    private void rdMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMonthActionPerformed
        if(this.rdMonth.isSelected())
        {
            this.rdDay.setSelected(false);
            this.dayComboBox.setEnabled(false);
            jScrollPaneMonth.setViewportView(tableMonth);
        
        }
    }//GEN-LAST:event_rdMonthActionPerformed

    private void rdDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDayActionPerformed
        if(this.rdDay.isSelected())
        {
            tableDay = new TargetDayPerformanceTablePanel();
            this.rdMonth.setSelected(false);
            this.dayComboBox.setEnabled(true);
            this.nCurrentEditingMonth = this.dayComboBox.getSelectedIndex();
            Integer shopId  = null;
            if( cblShop.getSelectedItem() instanceof MstShop ){
                shopId = ((MstShop)cblShop.getSelectedItem()).getShopID();
            }else{
                shopId = 0;
            }
            Calendar cal = Calendar.getInstance();
            cal.set(this.nCurrentEditingYear, this.nCurrentEditingMonth, 1);
            tableDay.setTargetDay(cal.getTime());
            tableDay.setShopId(shopId);
            tableDay.loadYearData();
            jScrollPaneMonth.setViewportView(tableDay);
        }
    }//GEN-LAST:event_rdDayActionPerformed

    private void rdMonthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdMonthStateChanged
         if(this.rdMonth.isSelected())
        {
            this.rdDay.setSelected(false);
            this.dayComboBox.setEnabled(false);
            jScrollPaneMonth.setViewportView(tableMonth);
           
        }
    }//GEN-LAST:event_rdMonthStateChanged

    private void rdDayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdDayStateChanged
        
    }//GEN-LAST:event_rdDayStateChanged

    private void dayComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dayComboBoxActionPerformed
        
    }//GEN-LAST:event_dayComboBoxActionPerformed

    private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboBoxActionPerformed
        
    }//GEN-LAST:event_monthComboBoxActionPerformed

    private void monthComboBoxVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_monthComboBoxVetoableChange
        
    }//GEN-LAST:event_monthComboBoxVetoableChange
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cblShop;
    private javax.swing.JComboBox dayComboBox;
    private javax.swing.JButton displayButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneMonth;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JRadioButton rdDay;
    private javax.swing.JRadioButton rdMonth;
    private javax.swing.JButton registerButton;
    private javax.swing.JLabel shopLabel;
    private com.geobeck.sosia.pos.hair.basicinfo.company.TargetMonthPerformanceTablePanel tableMonth;
    // End of variables declaration//GEN-END:variables

}

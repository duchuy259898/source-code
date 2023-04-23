/*
 * RepeaterReportPanelTom.java
 *
 * Created on 2008/09/26, 13:45
 */

package com.geobeck.sosia.pos.hair.report;

import java.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.master.company.MstShop;

import com.geobeck.sosia.pos.hair.report.logic.TechnicSalesReportLogic;
import com.geobeck.sosia.pos.hair.report.beans.SalesDateBean;
import com.geobeck.sosia.pos.hair.report.logic.RepeaterReportLogic;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.MessageDialog;
import java.awt.Cursor;
import javax.swing.JOptionPane;

/**
 *
 * @author  trino
 */
public class RepeaterReportPanelTom extends  com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
//    private int shopId = 0;
//    private String shopName = null;
    
    /** Creates new form RepeaterReportPanelTom */
    public RepeaterReportPanelTom() {
        
        initComponents();
        addMouseCursorChange();
        
        this.setPath("���[�o��");
        this.setTitle("�J�X�^�����[>>�ė�����");
        this.setSize(400,180);
        SystemInfo.initGroupShopComponents(this.cmbShop, 2);
        
        this.initCmbYear();
    }
    /*
    private void getShopId()
    {
        MstShop ms = new MstShop();
        if( 0 <= this.cmbShop.getSelectedIndex() )
        {
            ms  = (MstShop)this.cmbShop.getSelectedItem();
            this.shopId    = ms.getShopID();
            this.shopName  = ms.getShopName();
        }
    }
     */
    
    /**
     * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
     */
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnExcel);
    }
    
    private MstShop getSelectedShop() {
        return (MstShop)this.cmbShop.getSelectedItem();
    }
    
    private void initCmbYear() {
        this.cmbYear.removeAllItems();
        int index = -1;
        MstShop shopinfo;
        
        shopinfo = (MstShop)this.cmbShop.getSelectedItem();
        ArrayList<SalesDateBean> beanlist = TechnicSalesReportLogic.getSalesDateBean(shopinfo.getShopID());
        Calendar calendar = Calendar.getInstance();
        String currentYear = new String(calendar.get(Calendar.YEAR)+"");
        for(int i= 0; i < beanlist.size() ; i++ ) {
            this.cmbYear.addItem(beanlist.get(i).getYear() + "�N");
            if( beanlist.get(i).getYear().equals(currentYear)){
                index = i;
            }
        }
    }
    
    private Integer getSelectedYear() {
        String year = (String)this.cmbYear.getSelectedItem();
        
        return Integer.valueOf(year.replaceAll("�N",""));
    }
    
    private void generateExportFile() {
        
        RepeaterReportLogic logic  = new RepeaterReportLogic(this.getSelectedYear(), this.getSelectedShop());
        int result = logic.viewRepeaterReport();
        
        if (result == logic.RESULT_DATA_NOTHNIG ) {
            
            // �f�[�^�Ȃ�
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            
        } else if (result == logic.RESULT_ERROR ) {
            
            // �\�����ʃG���[
            MessageDialog.showMessageDialog( this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE );
        }

        System.out.println(result);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        radioRepeater = new javax.swing.JRadioButton();
        btnExcel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbYear = new javax.swing.JComboBox();

        radioRepeater.setSelected(true);
        radioRepeater.setText("\u5897\u6e1b\u5ba2\u63a8\u79fb\u5206\u6790\u8868");
        radioRepeater.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioRepeater.setContentAreaFilled(false);
        radioRepeater.setMargin(new java.awt.Insets(0, 0, 0, 0));

        btnExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnExcel.setBorderPainted(false);
        btnExcel.setContentAreaFilled(false);
        btnExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        jLabel1.setText("\u5e97\u8217\u540d");

        cmbShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("\u96c6\u8a08\u671f\u9593");

        cmbYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radioRepeater)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                        .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbShop, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addContainerGap(191, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioRepeater, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExcelActionPerformed
    {//GEN-HEADEREND:event_btnExcelActionPerformed

        btnExcel.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            generateExportFile();
            
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//GEN-LAST:event_btnExcelActionPerformed
        
    // <editor-fold defaultstate="collapsed" desc=" Variables declaration - do not modify">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbShop;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton radioRepeater;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
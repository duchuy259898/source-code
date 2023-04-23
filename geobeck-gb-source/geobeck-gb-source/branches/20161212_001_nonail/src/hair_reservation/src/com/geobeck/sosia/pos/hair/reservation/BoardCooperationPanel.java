/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.reservation.api.CustomerCommingInfomationResultsWSI;
import com.geobeck.sosia.pos.hair.reservation.api.CustomerCommingInfomationWSI;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.ImagePanel;
import com.geobeck.swing.SwingUtil;
import com.google.gson.Gson;
import connectispotapi.ConnectIspotApi;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import javax.swing.JDialog;

/**
 *
 * @author IVS
 */
public class BoardCooperationPanel extends ImagePanel {

    /**
     * Creates new form BoardCooperationPanel
     */
    public BoardCooperationPanel() {
        super();
        initComponents();
        this.setSize(300, 200);
        //this.setTitle("サロンボード連携");
        this.setImage(SystemInfo.getImageIcon("/hpb/bg.jpg"));
        init();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        jButton1.setIcon(SystemInfo.getImageIcon("/hpb/btn.jpg"));
        jButton1.setPressedIcon(SystemInfo.getImageIcon("/hpb/btn_on.jpg"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(146, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //get result data
        MstStaff staff = new MstStaff();

        connectispotapi.ConnectIspotApi WS = new ConnectIspotApi();
        CustomerCommingInfomationWSI wsi = new CustomerCommingInfomationWSI();
        CustomerCommingInfomationResultsWSI results = new CustomerCommingInfomationResultsWSI();
        SimpleDateFormat dateCommFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calComm = Calendar.getInstance();
        wsi.setPosId(SystemInfo.getPosId().trim());
        wsi.setPassword(SystemInfo.getPosPassWord().trim());
        wsi.setPosSalonId(SystemInfo.getPossSalonId().trim());
        wsi.setComingDate(dateCommFormat.format(calComm.getTime()));
//        wsi.setComingId("");
//        wsi.setPaymentTotalPriceFlg("");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        Calendar cal = Calendar.getInstance();
//        wsi.setDiffDateTime("");
//        wsi.setComingIdFlg("");
//        wsi.setStart("");
//        wsi.setCount("");
        wsi.setFormat("json");
        Gson gson = new Gson();
        String jsonResult = WS.getConnectIspotApiPort().commingInfomation(gson.toJson(wsi));
        if (!jsonResult.equals("")) {
            try {
                results = gson.fromJson(jsonResult, CustomerCommingInfomationResultsWSI.class);
            } catch (Exception e) {

                System.out.println("error at http://api-test.sosia.jp/hpb/ConnectIspotApi.php?wsdl-function:commingInfomation At https://wwwtst.beauty.psa.d.hotpepper.jp/CLA/bt/common/v1/search/comingInfo/doSearch");
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return;

            }
        }
        ((JDialog) this.getParent().getParent().getParent().getParent()).setVisible(false);
        //if(results.getResults().getComing_info().size()>0) {    
        BoardReservationInfoPanel brp = new BoardReservationInfoPanel();
        SwingUtil.openAnchorDialog(null, true, brp, "サロンボード予約情報", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);

        // }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    private void init() {
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RegistPersonalRecessPanel.java
 *
 * Created on 2011/10/04, 19:47:12
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.hair.reservation.RegisterPartime;
import com.geobeck.sosia.pos.hair.reservation.ReservationTimeTablePanel;
import com.geobeck.sosia.pos.master.company.MstShifts;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.swing.AbstractImagePanelEx;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.util.*;
import java.awt.*;
import java.util.Date;
import java.awt.Component;
import javax.swing.*;
import javax.swing.text.*;
import com.geobeck.swing.*;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.DateUtil;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.map.LinkedMap;
/**
 *
 * @author geobeck
 */
//public class RegistPersonalRecessPanel extends javax.swing.JPanel {
public class RegistPersonalRecessPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    private	GregorianCalendar   currentDate = new GregorianCalendar();	/** ì˙ït */
    //private MstShop   shop;
    //private MstShifts shifts;
    private Date      scheduleDate;
    private DataSchedules scheds;
    //âÊñ à íuÇê›íËÇ∑ÇÈéûÇÃí≤êÆíl
    private final int MINUS_X = 4;
    private final int MINUS_Y = 30;
    private JTextField recessStartTimeField = null;
    private JTextField recessEndTimeField = null;
    // ÉVÉtÉgÉ}ÉXÉ^
    private MstShifts shifts = new MstShifts();
    // ÉXÉ^ÉbÉtÉVÉtÉg
    private DataSchedules staffShifts = new DataSchedules();
    private Integer selectedChargeStaffID = null;
    //IVS_LVTu start add 2016/03/17 New request #49127
    private static AbstractImagePanelEx parent;
    //IVS_LVTu end add 2016/03/17 New request #49127
    
    //IVS_LVTu start add 2016/03/24 Bug #49232
    DataRecess dataressCurrent = null;
    //IVS_LVTu end add 2016/03/24 Bug #49232
    
    //IVS_NHTVINH start add 2016/09/07 New request #54487
    private MstAPI mstApi = null;
    private int recessId;
    private MstShop shopSelect;
    //IVS_NHTVINH end add 2016/09/07 New request #54487

//    private AbstractImagePanelEx parent;
    	/**
	 * ì˙ïtÇê›íËÇ∑ÇÈÅB
	 * @param date ì˙ït
	 */
	public void setDate(java.util.Date date){
            this.date.setDate( date );
            currentDate.setTime( date );
        }

	/**
	 * âÊñ à íuÇê›íËÇ∑ÇÈÅB
	 * @param point âÊñ à íuèÓïÒ
	 */
	public void setPoint(Point point){
            int posX = point.x - MINUS_X;
            int posY = point.y - MINUS_Y;

            if(this.isDialog()) {
                    ((JDialog)this.getParent().getParent().getParent().getParent()).setLocation(posX, posY);
            } else {
                    this.setLocation(posX, posY);
            }
        }


    /*
     * É_ÉCÉAÉçÉOÇ∆ÇµÇƒï\é¶Ç≥ÇÍÇΩèÍçáÇÕÅAï¬Ç∂ÇÈÉ{É^ÉìÇóLå¯âª
     */
    private void ShowCloseBtn() {
        btnClose.setVisible(true);
    }

     /*
     * ãxåeéûä‘ìoò^âÊñ ÇÉ_ÉCÉAÉçÉOÇ∆ÇµÇƒÅAï\é¶Ç∑ÇÈ
     */
    public static boolean ShowDialog(Frame owner, MstShop shop, Date targetDate ) {
        SystemInfo.getLogger().log(Level.INFO, "ãxåeéûä‘ìoò^");

        RegistPersonalRecessPanel dlg = new RegistPersonalRecessPanel( shop,targetDate );
        dlg.ShowCloseBtn();
        dlg.scheduleDate = targetDate;
        dlg.loadData();

        SwingUtil.openAnchorDialog(owner, true, dlg, "ãxåeéûä‘ìoò^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);

        dlg.dispose();
        return true;
    }
    
    /*
     * ãxåeéûä‘ìoò^âÊñ ÇÉ_ÉCÉAÉçÉOÇ∆ÇµÇƒÅAï\é¶Ç∑ÇÈ
     */
    //IVS_LVTu start add 2016/03/17 New request #49127
    public static boolean ShowDialog(Frame owner, MstShop shop, Date targetDate , AbstractImagePanelEx p) {
        SystemInfo.getLogger().log(Level.INFO, "ãxåeéûä‘ìoò^");
        parent = p;
        RegistPersonalRecessPanel dlg = new RegistPersonalRecessPanel( shop,targetDate );
        dlg.ShowCloseBtn();
        dlg.scheduleDate = targetDate;
        dlg.loadData();

        SwingUtil.openAnchorDialog(owner, true, dlg, "ãxåeéûä‘ìoò^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);

        dlg.dispose();
        return true;
    }
    //IVS_LVTu start add 2016/03/17 New request #49127

     /*
     * ãxåeéûä‘ìoò^âÊñ ÇÉ_ÉCÉAÉçÉOÇ∆ÇµÇƒÅAï\é¶Ç∑ÇÈ
     */
    public static boolean ShowDialog(Frame owner, MstShop shop, Date targetDate, GregorianCalendar clickTime, Integer staffID) {
        SystemInfo.getLogger().log(Level.INFO, "ãxåeéûä‘ìoò^A");
        RegistPersonalRecessPanel dlg = new RegistPersonalRecessPanel( shop,targetDate );
        dlg.ShowCloseBtn();
        dlg.setSelectedChargeStaffID(staffID);
        dlg.scheduleDate = targetDate;
        //ó\ñÒéûä‘ÇïœçXÇµÇΩéûÇ…ÉÅÉbÉZÅ[ÉWÇï\é¶Ç∑ÇÈÇ©Ç«Ç§Ç©
        if (clickTime != null) {
            String startTime = getFormatTime(targetDate, clickTime);
            dlg.recessStartTimeField.setText(startTime);
            dlg.recessEndTimeField.setText(startTime);
        }
        dlg.loadData();
        SwingUtil.openAnchorDialog(owner, true, dlg, "ãxåeéûä‘ìoò^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);

        return true;
    }

    private static String getFormatTime(java.util.Date dt, Calendar cal) {

            int h = cal.get(Calendar.HOUR_OF_DAY);
            int m = cal.get(Calendar.MINUTE);
            if (Integer.parseInt(DateUtil.format(dt, "dd")) + 1 == cal.get(Calendar.DAY_OF_MONTH)) {
                h += 24;
            }

            return String.format("%1$02d", h) + ":" + String.format("%1$02d", m);
    }
    /*
     * ì˙ïtÅAìXï‹ÇÊÇËÅAÉXÉPÉWÉÖÅ[ÉãÅïãxåeÉfÅ[É^Çì«Ç›çûÇÒÇ≈ï\é¶Ç∑ÇÈ
     */
    private boolean loadData() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_recess");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and staff_id = " + SQLUtil.convertForSQL(this.selectedChargeStaffID));
        sql.append("     and schedule_date = " + SQLUtil.convertForSQLDateOnly(date.getDate()));
        if (recessStartTime.getSelectedItem() == null) {
            sql.append(" and '" + recessStartTimeField.getText().replace(":", "") + "' between start_time and end_time");
        }
        sql.append("     and '" + recessEndTimeField.getText().replace(":", "") + "' < end_time");
        sql.append(" order by");
        sql.append("     recess_id");
        sql.append(" limit 1");

        try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            if (rs.next()) {
                recessStartTimeField.setText(getFormatTime(rs.getString("start_time")));
                recessEndTimeField.setText(getFormatTime(rs.getString("end_time")));
                memoBody1.setText(rs.getString("note"));
                recessIdLabel.setText(rs.getString("recess_id"));
                btnDelete.setEnabled(true);
                
                //IVS_LVTu start add 2016/03/24 Bug #49232
                this.dataressCurrent = new DataRecess();
                this.dataressCurrent.setData(rs);
                //IVS_LVTu end add 2016/03/24 Bug #49232
            }
            rs.close();
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }


        for (int i = 0; i < recessStartTime.getItemCount(); i++) {
            if (recessStartTime.getItemAt(i).toString().equals(recessStartTimeField.getText())) {
                recessStartTime.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < recessEndTime.getItemCount(); i++) {
            if (recessEndTime.getItemAt(i).toString().equals(recessEndTimeField.getText())) {
                recessEndTime.setSelectedIndex(i);
                break;
            }
        }

        return true;
    }

    /*
     * å„énññ
     */
    private void dispose() {
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).dispose();
        }
    }

    /**
     * É{É^ÉìÇ…É}ÉEÉXÉJÅ[É\ÉãÇ™èÊÇ¡ÇΩÇ∆Ç´Ç…ÉJÅ[É\ÉãÇïœçXÇ∑ÇÈÉCÉxÉìÉgÇí«â¡Ç∑ÇÈÅB
     */
    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(btnClose);
        SystemInfo.addMouseCursorChange(btnRegist);
        SystemInfo.addMouseCursorChange(btnDelete);
    }

    /** Creates new form RegistPersonalRecessPanel */
    private RegistPersonalRecessPanel( MstShop shop, Date targetDate) {
        initComponents();
        this.setTitle("ãxåeéûä‘ìoò^");
        //parent = p;
        recessStartTimeField = (JTextField)recessStartTime.getEditor().getEditorComponent();
        recessStartTimeField.setHorizontalAlignment(JTextField.CENTER);
        recessEndTimeField = (JTextField)recessEndTime.getEditor().getEditorComponent();
        recessEndTimeField.setHorizontalAlignment(JTextField.CENTER);

        this.date.setDate( targetDate );
        currentDate.setTime( targetDate );
        this.setSize(300, 250);
//                if (SystemInfo.getDatabase().startsWith("pos_hair_lim")) {
//                    pnlLim.setVisible(true);
//                    this.setSize(595, 580);
//                } else {
//                    pnlLim.setVisible(false);
//                    this.setSize(595, 550);
//                }
                //èâä˙âªèàóù
                this.init(shop);

                recessIdLabel.setVisible(false);
                recessIdLabel.setText("");

                //reservations.getColumn("äJénéûä‘").setCellEditor(new DefaultCellEditor(getStartTimeComboBox()));

//                if (SystemInfo.isReservationOnly()) {
//                    receiptButton.setEnabled(false);
//                }
//
//                if (!SystemInfo.getSetteing().isItoAnalysis()) {
//                    checkNext.setVisible(false);
//                    checkPreorder.setVisible(false);
//                }
//
//                if (SystemInfo.getCurrentShop().getShopID().equals(shop.getShopID())) {
//                    lblOtherShop.setVisible(false);
//                }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateLabel = new javax.swing.JLabel();
        date = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        staffLabel = new javax.swing.JLabel();
        chargeStaffNo = new javax.swing.JTextField();
        chargeStaff = new javax.swing.JComboBox();
        recessStartTimeLabel = new javax.swing.JLabel();
        recessStartTime = new javax.swing.JComboBox();
        Component cs = recessStartTime.getEditor().getEditorComponent();
        PlainDocument docs = (PlainDocument)((JTextComponent)cs).getDocument();
        docs.setDocumentFilter(new CustomFilter(5, "0-9:"));
        recessStartTimeLabel1 = new javax.swing.JLabel();
        recessEndTime = new javax.swing.JComboBox();
        Component ce = recessEndTime.getEditor().getEditorComponent();
        PlainDocument doce = (PlainDocument)((JTextComponent)ce).getDocument();
        doce.setDocumentFilter(new CustomFilter(5, "0-9:"));
        noteScrollPane1 = new javax.swing.JScrollPane();
        memoBody1 = new com.geobeck.swing.JTextAreaEx();
        memoLabel1 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnClose.setVisible(false);
        btnClose.setContentAreaFilled(false);
        btnDelete = new javax.swing.JButton();
        btnRegist = new javax.swing.JButton();
        recessIdLabel = new javax.swing.JLabel();

        dateLabel.setText("ì˙ït");

        date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        date.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        date.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dateItemStateChanged(evt);
            }
        });
        date.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dateFocusGained(evt);
            }
        });

        staffLabel.setText("íSìñé“");

        chargeStaffNo.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        chargeStaffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chargeStaffNoFocusLost(evt);
            }
        });

        chargeStaff.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffActionPerformed(evt);
            }
        });

        recessStartTimeLabel.setText("ãxåeéûä‘");

        recessStartTime.setEditable(true);
        recessStartTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        recessStartTime.setMaximumRowCount(10);
        recessStartTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recessStartTimeItemStateChanged(evt);
            }
        });

        recessStartTimeLabel1.setText("Å`");

        recessEndTime.setEditable(true);
        recessEndTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        recessEndTime.setMaximumRowCount(10);
        recessEndTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recessEndTimeItemStateChanged(evt);
            }
        });

        noteScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        memoBody1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        memoBody1.setColumns(20);
        memoBody1.setLineWrap(true);
        memoBody1.setRows(5);
        memoBody1.setTabSize(4);
        memoBody1.setInputKanji(true);
        noteScrollPane1.setViewportView(memoBody1);

        memoLabel1.setText("ÉRÉÅÉìÉg");

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setBorderPainted(false);
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        btnDelete.setBorderPainted(false);
        btnDelete.setEnabled(false);
        btnDelete.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnRegist.setBackground(new java.awt.Color(255, 255, 255));
        btnRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btnRegist.setBorderPainted(false);
        btnRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btnRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistActionPerformed(evt);
            }
        });

        recessIdLabel.setText("ãxåeID");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memoLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(recessStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recessStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(recessStartTimeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recessEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(staffLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chargeStaffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chargeStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(recessIdLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(noteScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(dateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(staffLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chargeStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chargeStaffNo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recessStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recessStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recessEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recessStartTimeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(memoLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noteScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recessIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dateItemStateChanged

        if (parent != null) {
            if (parent instanceof ReservationTimeTablePanel) {
                ((ReservationTimeTablePanel)parent).setDate(date.getDate());
            }
        }
    }//GEN-LAST:event_dateItemStateChanged

    private void dateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateFocusGained
        date.getInputContext().setCharacterSubsets(null);
}//GEN-LAST:event_dateFocusGained

    private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chargeStaffNoFocusLost

        if (!chargeStaffNo.getText().equals("")) {

            chargeStaffNo.setName("1");
            this.setStaff(chargeStaff, chargeStaffNo.getText());
            chargeStaffNo.setName(null);

        } else {

            chargeStaff.setSelectedIndex(0);
        }
}//GEN-LAST:event_chargeStaffNoFocusLost

    private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed

        if (chargeStaffNo.getName() != null) return;

        MstStaff ms= (MstStaff)chargeStaff.getSelectedItem();

        if (chargeStaff.getSelectedIndex() == 0) {
            chargeStaffNo.setText("");
        }

        if (ms.getStaffID() != null) {
            chargeStaffNo.setText(ms.getStaffNo());
        }

        if(!this.isShowing()) {
            return;
        }
// TODO
//        rr.getReservation().setStaff( (MstStaff)chargeStaff.getSelectedItem() );

//        if( chargeStaff.getSelectedIndex() == 0 ) {
//
//            if (!this.getTitle().equals("ó\ñÒämîF")) {
//                //éwñºÉtÉäÅ[ëŒâû
//                this.shimeiFreeFlag = false;
//                this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/free_off.png"));
//                this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/free_on.png"));
//            }
//
//            setStaffDataToReserve( false );
//
//        } else {
//
//            if (!this.getTitle().equals("ó\ñÒämîF")) {
//                //éwñºÉtÉäÅ[ëŒâû
//                this.shimeiFreeFlag = true;
//                this.jButton1.setIcon(SystemInfo.getImageIcon("/button/account/shimei_off.png"));
//                this.jButton1.setPressedIcon(SystemInfo.getImageIcon("/button/account/shimei_on.png"));
//            }
//
//            setStaffDataToReserve( true );
//        }
    }//GEN-LAST:event_chargeStaffActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
}//GEN-LAST:event_btnCloseActionPerformed
    /*
     * âÊñ ÇÃÉfÅ[É^Çì«Ç›çûÇÒÇ≈ÅAãxåeÉfÅ[É^Çìoò^Ç∑ÇÈ
     */
    private boolean registData()
    {
        //IVS_LVTu start edit 2016/11/30 Bug #58699
//        boolean isDel = false;
//        Integer allianceRecessId = null;
        DataRecess dataress = new DataRecess();
        int staffID = -1;
        if (this.chargeStaff.getSelectedIndex() > 0) {
                staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

        }
        dataress.setStaffId(staffID);

        if(recessStartTime.getSelectedItem() == null){
            dataress.setStartTime(recessStartTimeField.getText().replace(":", ""));
        } else {
            dataress.setStartTime(recessStartTime.getSelectedItem().toString().replace(":", ""));
        }

        if(recessEndTime.getSelectedItem() == null){
            dataress.setEndTime(recessEndTimeField.getText().replace(":", ""));
        } else {
            dataress.setEndTime(recessEndTime.getSelectedItem().toString().replace(":", ""));
        }
        dataress.setScheduleDate(date.getDate());

        dataress.setNote(memoBody1.getText());
        SystemInfo.getLogger().log(Level.INFO, "äJénéûä‘" + dataress.getStartTime());
        SystemInfo.getLogger().log(Level.INFO, "èIóπéûä‘" + dataress.getEndTime() );

        // ãxåeÉfÅ[É^Çìoò^Ç∑ÇÈ
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.begin();
            
            //IVS_LVTu start add 2016/03/24 Bug #49232
            //update data_recess delete_date
//            if(this.dataressCurrent != null && this.dataressCurrent.getStaffId() != staffID) {
//                try {
//                    if(null != dataressCurrent)
//                        allianceRecessId = dataressCurrent.getAllianceRecessId();
//                    this.dataressCurrent.deleteDataRecess(con);
//                    isDel = true;
//                    } catch (SQLException ex) {
//                    Logger.getLogger(RegistPersonalRecessPanel.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
            //IVS_LVTu end add 2016/03/24 Bug #49232
            
            if (recessIdLabel.getText().length() > 0 
                    && this.dataressCurrent != null && this.dataressCurrent.getStaffId() == staffID
                    && this.dataressCurrent.getScheduleDate().compareTo(dataress.getScheduleDate()) == 0) {
                dataress.setRecessId(Integer.valueOf(recessIdLabel.getText()));
            } else {

                //1ÇrecessIdÇ…ÉZÉbÉgÇ∑ÇÈÅB
                int recessId = 1;
                dataress.setRecessId(recessId);

                //ñ≥Ç≠Ç»ÇÈÇ‹Ç≈Ç∏Ç¡Ç∆åüçı
                while (dataress.isExists(con)) {
                    recessId ++;
                    dataress.setRecessId(recessId);
                }
            }
            recessId = dataress.getRecessId();
            dataress.setStaffIdNew(dataress.getStaffId());
            dataress.setRecessIdNew(dataress.getRecessId());
            dataress.setScheduleDateNew(dataress.getScheduleDate());
            if(this.dataressCurrent != null) {
                dataress.setStaffId(this.dataressCurrent.getStaffId());
                dataress.setRecessId(this.dataressCurrent.getRecessId());
                dataress.setScheduleDate(this.dataressCurrent.getScheduleDate());
            }
            dataress.regist(con);
            con.commit();
            
            //IVS_LVTu start add 2016/11/01 New request #55963
            //delete data 
//            SystemInfo.getMstUser().setShopID(shopSelect.getShopID());
//            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0 && isDel && allianceRecessId != null && allianceRecessId != 0){
//                Integer shopId = shopSelect.getShopID();
//                sendRegistRecessAPI(SystemInfo.getLoginID(), this.selectedChargeStaffID, 
//                        date.getDate(), this.dataressCurrent.getRecessId(), allianceRecessId, shopId);
//                
//            }
            //IVS_LVTu end add 2016/11/01 New request #55963
            //IVS_LVTu end edit 2016/11/30 Bug #58699
        } catch(SQLException e) {
            try {
                con.rollback();
            } catch(SQLException ex) {
                SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        } catch (NumberFormatException e) {
            try {
                con.rollback();
            } catch(SQLException ex) {
                SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }
    
     //IVS_NHTVINH start add 2016/09/07 New request #54487
        /**
         * send loginId, staff_id, schedular_date and recess_id  to recessAPI to register
         * then check response , if success return true, else return false
         * @return 
         */
    private Boolean sendRegistRecessAPI(String login_id, int staff_id, Date schedule_date, 
                                        int recess_id, Integer alliance_recess_id, Integer shopId){
        try{
            //String url = "http://10.32.5.21/web/s/send/recess.php";
            //ê⁄ë±èÓïÒÇÕmst_apiÇ©ÇÁéÊìæÇ∑ÇÈÅBÅiapi_id=0Åj
            mstApi = new MstAPI(0);
            String apiUrl = mstApi.getApiUrl();
            String url = apiUrl + "/s/send/recess.php";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            con.setRequestProperty( "charset", "utf-8");

            Format formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = formatter.format(schedule_date);
            //set parameter
            Map mapParam = new LinkedMap();
            mapParam.put("login_id", login_id);
            mapParam.put("staff_id", staff_id);
            mapParam.put("schedule_date", dateString);
            mapParam.put("recess_id", recess_id);
            mapParam.put("alliance_recess_id", alliance_recess_id);
            mapParam.put("shop_id", shopId);
            Gson gson = new Gson(); 
            String jsonParam = gson.toJson(mapParam); 
            String urlParameters = "param=" + jsonParam;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //get response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();
            if(response.toString().contains("\"code\":200")){
                return true;
            }

        }catch(Exception e){
            return false;
        }
        return false;
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        //çÌèúämîF
        if (MessageDialog.showYesNoDialog(
                this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, "ãxåe"),
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
        {
            return;
        }
        Integer alliance_recess_id = null;
        if(null != dataressCurrent)
            alliance_recess_id = dataressCurrent.getAllianceRecessId();
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" delete from data_recess");
        sql.append(" where");
        sql.append("         staff_id = " + SQLUtil.convertForSQL(this.selectedChargeStaffID));
        sql.append("     and schedule_date = " + SQLUtil.convertForSQLDateOnly(date.getDate()));
        sql.append("     and recess_id = " + SQLUtil.convertForSQL(recessIdLabel.getText()));

        try {
            SystemInfo.getConnection().executeUpdate(sql.toString());
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        //IVS_NHTVINH start add 2016/09/07 New request #54380
        if(null != shopSelect) {
            SystemInfo.getMstUser().setShopID(shopSelect.getShopID());
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0){
                Integer shopId = shopSelect.getShopID();
                if(!sendRegistRecessAPI(SystemInfo.getLoginID(), this.selectedChargeStaffID, 
                        date.getDate(), Integer.parseInt(recessIdLabel.getText()), alliance_recess_id, shopId)){
                    MessageDialog.showMessageDialog(this,
                        "î}ëÃÇ∆ÇÃòAìÆÇ™Ç≈Ç´Ç‹ÇπÇÒÇ≈ÇµÇΩÅB",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                }
                recessId = 0;
            }
        }
        //IVS_NHTVINH end ad16d 2016/09/07 New request #54380
        
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }

}//GEN-LAST:event_btnDeleteActionPerformed

     //IVS_NHTVINH start add 2016/10/04 New request #54487 [gb]Ç©ÇÒÇ¥ÇµAPIóp_ã@î\í«â¡Åiãxåeìoò^âÊñ Åj
    /**
     * check if regist time is overlap and it's not update then return true
     * else return false
     * @return 
     */
    private Boolean isRegistTimeOverlap(){
        try{
            DataRecess dataress = new DataRecess();
            int staffID = -1;
            if (this.chargeStaff.getSelectedIndex() > 0) {
                    staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

            }
            dataress.setStaffId(staffID);

            if(recessStartTime.getSelectedItem() == null){
                dataress.setStartTime(recessStartTimeField.getText().replace(":", ""));
            } else {
                dataress.setStartTime(recessStartTime.getSelectedItem().toString().replace(":", ""));
            }

            if(recessEndTime.getSelectedItem() == null){
                dataress.setEndTime(recessEndTimeField.getText().replace(":", ""));
            } else {
                dataress.setEndTime(recessEndTime.getSelectedItem().toString().replace(":", ""));
            }
            dataress.setScheduleDate(date.getDate());
            if (recessIdLabel.getText().length() > 0) {
                dataress.setRecessId(Integer.valueOf(recessIdLabel.getText()));
            } else {
                dataress.setRecessId(-1);
            }
            //IVS_LVTu start add 2016/11/30 Bug #58796
            if(this.dataressCurrent != null 
                    && (!this.dataressCurrent.getStaffId().equals(dataress.getStaffId())
                        ||this.dataressCurrent.getScheduleDate().compareTo(dataress.getScheduleDate()) != 0)) {
                
                dataress.setRecessId(-1);
            }
            //IVS_LVTu end add 2016/11/30 Bug #58796
            
            ConnectionWrapper con = SystemInfo.getConnection();
            con.begin();
            SystemInfo.getMstUser().setShopID(shopSelect.getShopID());
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0){
                if(dataress.checkTimeRegistOverlap(con))
                    return true;
            }
            con.commit();
            
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
    //IVS_NHTVINH end add 2016/10/04 New request #54487
    
    private void btnRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistActionPerformed

        //ì¸óÕÉ`ÉFÉbÉN
        if (!this.checkInput()) {
            return;
        }
        //IVS_NHTVINH end add 2016/10/04 New request #54487 [gb]Ç©ÇÒÇ¥ÇµAPIóp_ã@î\í«â¡Åiãxåeìoò^âÊñ Åj
        if(!isRegistTimeOverlap()){
            if (registData()) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);

                 //IVS_NHTVINH start add 2016/09/07 New request #54380
                if(null != shopSelect) {
                    SystemInfo.getMstUser().setShopID(shopSelect.getShopID());
                    if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0){
                        Integer shopId = shopSelect.getShopID();
                        if(!sendRegistRecessAPI(SystemInfo.getLoginID(), ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID(), 
                                date.getDate(), recessId, null, shopId)){
                            MessageDialog.showMessageDialog(this,
                                "î}ëÃÇ∆ÇÃòAìÆÇ™Ç≈Ç´Ç‹ÇπÇÒÇ≈ÇµÇΩÅB",
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        }
                        recessId = 0;
                    }
                }
                //IVS_NHTVINH end add 2016/09/07 New request #54380
            } else {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ãxåeéûä‘"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
             if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
            } else {
                this.setVisible(false);
            }
        }else{
             MessageDialog.showMessageDialog(this,
                            "ì¸óÕÇ≥ÇÍÇΩéûä‘ë—Ç…ï ÇÃãxåeÇ™ìoò^Ç≥ÇÍÇƒÇ¢Ç‹Ç∑ÅB\n èdï°ÇµÇ»Ç¢éûä‘Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
        }
        //IVS_NHTVINH end add 2016/10/04 New request #54487
    }//GEN-LAST:event_btnRegistActionPerformed

    private void recessStartTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recessStartTimeItemStateChanged
        refreshRecessTime(recessStartTime, recessStartTimeField);
    }//GEN-LAST:event_recessStartTimeItemStateChanged

    private void recessEndTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recessEndTimeItemStateChanged
        refreshRecessTime(recessEndTime, recessEndTimeField);
    }//GEN-LAST:event_recessEndTimeItemStateChanged

    private void refreshRecessTime(JComboBox cb, JTextField fld) {

        if (fld.getText().length() > 0){
            fld.setText(getFormatTime(fld.getText()));

            if(!this.checkTimeFormat(fld.getText())) {
                fld.setText("");
                return;
            }
        }
    }

    private boolean checkTimeFormat(String time) {

        if (!time.matches("[0-9]{1,2}:[0-9]{2}")) return false;

        int	hour	= Integer.parseInt(time.replaceAll(":.+", ""));
        int	minute	= Integer.parseInt(time.replaceAll(".+:", ""));

        if (hour < 0 || 36 < hour) return false;
        if (minute < 0 || 59 < minute) return false;

        return true;
    }

    /**
     * ìoò^èàóùÇçsÇ§ÅB
     */
    private void regist() {

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRegist;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JTextField chargeStaffNo;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo date;
    private javax.swing.JLabel dateLabel;
    private com.geobeck.swing.JTextAreaEx memoBody1;
    private javax.swing.JLabel memoLabel1;
    private javax.swing.JScrollPane noteScrollPane1;
    private javax.swing.JComboBox recessEndTime;
    private javax.swing.JLabel recessIdLabel;
    private javax.swing.JComboBox recessStartTime;
    private javax.swing.JLabel recessStartTimeLabel;
    private javax.swing.JLabel recessStartTimeLabel1;
    private javax.swing.JLabel staffLabel;
    // End of variables declaration//GEN-END:variables

    	/**
	 * èâä˙âªèàóùÇçsÇ§
	 */
	private void init(MstShop shop) {
            //nhtvinh start add 20161017 New request #54487
            shopSelect = shop;
            //nhtvinh end add 20161017 New request #54487
//                // é{èpë‰ÉäÉXÉgÇéÊìæÇ∑ÇÈ
//		this.getMstBeds();
//		// à¬ï™ÉäÉXÉgÇéÊìæÇ∑ÇÈ
//		this.getProportionallys();
//
                // ÉVÉtÉgÉ}ÉXÉ^ÇéÊìæÇ∑ÇÈ
                this.getMstShifts();

                // ÉXÉ^ÉbÉtÉVÉtÉgÇéÊìæÇ∑ÇÈ
                this.getStaffShifts();

              addMouseCursorChange();
//TODO êUÇÈïëÇ¢ÇÕå„Ç≈
//		setListener();
//TODO ó\ñÒìoò^ÉNÉâÉXÇ»ÇÃÇ≈àÍíUï€óØÅiï ìråüì¢
//              rr.setShop(this.getSelectedShop());
//		rr.init();
                this.setStaffs();

//TODOÅ@àÍíUÇ±ÇÃà»â∫ÇÉRÉÅÉìÉg
//		registPanel.setVisible(SystemInfo.getCurrentShop().getShopID() != 0);
//		searchCustomerButton.setVisible(SystemInfo.getCurrentShop().getShopID() != 0);
////		searchTechnicButton.setVisible(SystemInfo.getCurrentShop().getShopID() != 0);
//		customerNo.setEnabled(SystemInfo.getCurrentShop().getShopID() != 0);
//		deleteButton.setEnabled( false );
//		customerInfoButton.setEnabled(false);
//
//
                  recessStartTime.removeAllItems();
                  recessEndTime.removeAllItems();

                  //IVS_LVTu start edit 2015/10/08 New request #43146
//                Integer term        = SystemInfo.getCurrentShop().getTerm();            // éûä‘íPà 
//                Integer openHour    = SystemInfo.getCurrentShop().getOpenHour();       // äJìXéû
//                Integer openMinute  = SystemInfo.getCurrentShop().getOpenMinute();     // äJìXï™
//                Integer closeHour   = SystemInfo.getCurrentShop().getCloseHour();      // ï¬ìXéû
//                Integer closeMinute = SystemInfo.getCurrentShop().getCloseMinute();    // ï¬ìXï™
                Integer term        = null;            // éûä‘íPà 
                Integer openHour    = null;       // äJìXéû
                Integer openMinute  = null;     // äJìXï™
                Integer closeHour   = null;      // ï¬ìXéû
                Integer closeMinute = null;    // ï¬ìXï™
                if ( shop.getShopID() != null) {
                    term        = shop.getTerm();            // éûä‘íPà 
                    openHour    = shop.getOpenHour();       // äJìXéû
                    openMinute  = shop.getOpenMinute();     // äJìXï™
                    closeHour   = shop.getCloseHour();      // ï¬ìXéû
                    closeMinute = shop.getCloseMinute();    // ï¬ìXï™
                }
                //IVS_LVTu start edit 2015/10/08 New request #43146

                if (openHour != null && closeHour != null && term != null) {

                    for (int h = openHour; h <= closeHour; h++) {
                        for (int m = 0; m < 60; m += term) {
                            // äJìXéûä‘ÇÊÇËëOÇÃèÍçá
                            if (h == openHour && m < openMinute) continue;
                            // ï¬ìXéûä‘ÇÊÇËå„ÇÃèÍçá
                            
                            //Luc start edit 20150205 Bug #35169
                            //if (h == closeHour && closeMinute <= m) break;
                            if (h == closeHour && closeMinute < m) break;
                            //Luc end edit 20150205 Bug #35169 

                            // éûä‘ÉZÉbÉg
                            recessStartTime.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                            recessEndTime.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                       }
                    }
                }

                recessStartTime.setSelectedIndex(-1);
                recessStartTime.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        recessStartTimeField.selectAll();
                    }
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        //ãxåeéûä‘ÉäÉtÉåÉbÉVÉÖ
                        refreshRecessTime(recessStartTime, recessStartTimeField);
                    }
                });
                recessStartTime.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        if (evt.getKeyCode() == evt.VK_ENTER) {
                            //ãxåeéûä‘ÉäÉtÉåÉbÉVÉÖ
                            refreshRecessTime(recessStartTime, recessStartTimeField);
                        }
                    }
                });

                recessEndTime.setSelectedIndex(-1);
                recessEndTime.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        recessEndTimeField.selectAll();
                    }
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        //ãxåeéûä‘ÉäÉtÉåÉbÉVÉÖ
                        refreshRecessTime(recessEndTime, recessEndTimeField);
                    }
                });
                recessEndTime.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        if (evt.getKeyCode() == evt.VK_ENTER) {
                            //ãxåeéûä‘ÉäÉtÉåÉbÉVÉÖ
                            refreshRecessTime(recessEndTime, recessEndTimeField);
                        }
                    }
                });
	}

        private String getFormatTime(String time) {
            String s = time;

            if (s.matches("\\d+:\\d+")) {
                int h = Integer.parseInt(s.replaceAll(":.+", ""));
                int m = Integer.parseInt(s.replaceAll(".+:", ""));
                s = String.format("%1$02d", h) + ":" + String.format("%1$02d", m);

            } else {
                s = s.replace(":", "");

                if (s.length() < 5 && CheckUtil.isNumber(s)) {
                    if (s.length() == 1) {
                        s = "0" + s + ":00";
                    } else if (s.length() == 2) {
                        s = s + ":00";
                    } else if (s.length() == 3) {
                        s = "0" + s.substring(0, 1) + ":" + s.substring(1);
                    } else {
                        s = s.substring(0, 2) + ":" + s.substring(2);
                    }

                } else {
                    s = "";
                }
            }

            return s;
        }

	private void setStaffs()
	{
            MstStaffs staffs = new MstStaffs();

            staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());

            try {

                staffs.load(SystemInfo.getConnection(), true);

		for (MstStaff ms : staffs) {
                    chargeStaff.addItem(ms);
//                    regStaff.addItem(ms);
		}

		chargeStaff.setSelectedIndex(0);
//		regStaff.setSelectedIndex(0);

            } catch(SQLException e) {

                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

	}

       /**
         * ÉVÉtÉgÉ}ÉXÉ^ÇéÊìæÇ∑ÇÈ
         */
        private void getMstShifts() {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
//            MstShop ms = (MstShop)shop.getSelectedItem();
//            shifts.setShopId(ms.getShopID());

            try {
                shifts.loadAll(con);
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        private void setChargeStaff(int staffID) {
            for (int i = 1; i < chargeStaff.getItemCount(); i++) {
                if ( ( (MstStaff)chargeStaff.getItemAt( i ) ).getStaffID().intValue() == staffID ) {
                    chargeStaff.setSelectedIndex(i);
                }
            }
        }

        /*
         * ÉXÉ^ÉbÉtÉVÉtÉgÇéÊìæÇ∑ÇÈ
         */
        private void getStaffShifts() {
            ConnectionWrapper con = SystemInfo.getConnection();
//            staffShifts.setShop((MstShop)shop.getSelectedItem());
//            staffShifts.setScheduleDate(currentDate.getTime());
//
//            try {
//                staffShifts.load( con, false );
//            } catch(SQLException e) {
//                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
//            }
        }
        public Integer getSelectedChargeStaffID() {
            return selectedChargeStaffID;
        }

        public void setSelectedChargeStaffID(Integer selectedChargeStaffID) {
            this.selectedChargeStaffID = selectedChargeStaffID;
            SystemInfo.getLogger().log(Level.INFO, "ÉXÉ^ÉbÉtIDÉZÉbÉgÉÅÉ\ÉbÉh");
            if (selectedChargeStaffID != null) {
                SystemInfo.getLogger().log(Level.INFO, "ÉXÉ^ÉbÉtID" + selectedChargeStaffID);
                this.setChargeStaff(selectedChargeStaffID.intValue());
                //rr.getReservation().setStaff( (MstStaff)chargeStaff.getSelectedItem() );
                //this.shimeiFreeFlag=true;
            }
        }

	/**
	 * ì¸óÕÉ`ÉFÉbÉNÇçsÇ§ÅB
	 * @return true - ÇnÇj
	 */
	private boolean checkInput() {

            //íSìñé“
            if (chargeStaff.getSelectedIndex() < 1) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                            "íSìñé“"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    chargeStaffNo.requestFocusInWindow();
                    return false;
            }

            //ãxåeéûä‘
            if(recessStartTimeField.getText().length() == 0){
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                        "ãxåeéûä‘"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                recessStartTime.requestFocusInWindow();
                return	false;
            }

            if(recessEndTimeField.getText().length() == 0){
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                        "ãxåeéûä‘"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                recessEndTime.requestFocusInWindow();
                return	false;
            }
            
            //IVS_LVTu start 2015/10/14 Bug #43497
            if ( checkTime()) {
                MessageDialog.showMessageDialog(
                        this,
                        "ãxåeéûä‘ÇÃê›íËÇ™ê≥ÇµÇ≠Ç†ÇËÇ‹ÇπÇÒÅB", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                recessEndTime.requestFocusInWindow();
                return	false;
            }
            //IVS_LVTu end 2015/10/14 Bug #43497
            
            //nhtvinh start add 20161017 New request #54487
            SystemInfo.getMstUser().setShopID(shopSelect.getShopID());
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0){
                if(isRegisterOutsiteBusinessTime()){
                    return false;
                }
                if(!isTimeHelpOverlap()) {
                    MessageDialog.showMessageDialog(
                        this,
                        "ÉwÉãÉvéûä‘Ç∆ãxåeéûä‘Çèdï°ÇµÇƒ\nìoò^Ç∑ÇÈÇ±Ç∆ÇÕÇ≈Ç´Ç‹ÇπÇÒÅB", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    recessStartTime.requestFocusInWindow();
                    return false;
                }
                //IVS_LVTu start add  2016/11/23 New request #58700
                boolean isRight;
                int staffId = -1;
                DataSchedule scheduleStaff = new DataSchedule();
                ConnectionWrapper con = SystemInfo.getConnection();
                //ÉXÉ^ÉbÉtID
                if (this.chargeStaff.getSelectedIndex() > 0) {
                    staffId = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();
                }
                scheduleStaff.setStaffId(staffId);
                //ì˙ït
                scheduleStaff.setScheduleDate(currentDate.getTime());
                String startTime = (String)recessStartTime.getSelectedItem();
                String endStart = (String)recessEndTime.getSelectedItem();
                //äJénéûä‘
                Integer timeStartRegist = Integer.parseInt(startTime.replaceAll(":", "").trim());
                //èIóπéûä‘
                Integer timeEndRegist = Integer.parseInt(endStart.replaceAll(":", "").trim());

                try {
                    isRight = scheduleStaff.loadByID(con);
                    if(isRight) {
                        // start_time
                        Integer startSchedule   = Integer.parseInt(scheduleStaff.getStartTime().trim().equals("") ? "0" : scheduleStaff.getStartTime());
                        // end_time
                        Integer endSchedule     = Integer.parseInt(scheduleStaff.getEndTime().trim().equals("") ? "0" : scheduleStaff.getEndTime().trim());
                        // check time.
                        if((timeStartRegist < startSchedule)||(timeEndRegist > endSchedule)){
                            isRight = false;
                        }
                    }
                    if(!isRight) {
                        MessageDialog.showMessageDialog(this,
                            "ãxåeéûä‘Ç™ÉVÉtÉgéûä‘äOÇ≈Ç∑",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
                //IVS_LVTu end add  2016/11/23 New request #58700
            }
            //nhtvinh start end 20161017 New request #54487
            return true;
	}
        
        //nhtvinh start add 20161017 New request #54487
        /**
         * check start time register and end time register outside business time 
        */
        private Boolean isRegisterOutsiteBusinessTime(){
            try{
                int staffId = -1;
                if (this.chargeStaff.getSelectedIndex() > 0) {
                        staffId = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

                }
                if(null != shopSelect){
                    Integer shopId = shopSelect.getShopID();
                    Integer startTimeShift = shopSelect.getOpenHour()*100 + shopSelect.getOpenMinute();
                    Integer endTimeShift = shopSelect.getCloseHour()*100 + shopSelect.getCloseMinute();

                    String startTime = (String)recessStartTime.getSelectedItem();
                    String endStart = (String)recessEndTime.getSelectedItem();
                    if(null != startTime){
                        startTime = startTime.replaceAll(":", "").trim();
                        Integer timeStartRegist = Integer.parseInt(startTime);
                        if(timeStartRegist < startTimeShift || timeStartRegist > endTimeShift){
                            MessageDialog.showMessageDialog(this,
                            "ãxåeéûä‘Ç™âcã∆éûä‘äOÇ≈Ç∑",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                            return true;
                        }
                    }
                    if(null != endStart){
                        endStart = endStart.replaceAll(":", "").trim();
                        Integer timeEndRegist = Integer.parseInt(endStart);
                        if(timeEndRegist > endTimeShift){
                            MessageDialog.showMessageDialog(this,
                            "ãxåeéûä‘Ç™âcã∆éûä‘äOÇ≈Ç∑",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                            return true;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
        //nhtvinh end add 20161017 New request #54487
        
        //IVS_LVTu start add 2016/10/25 New request #57846
        /**
         * check time recess overlap time staff help
         * @return boolean
         */
        private boolean isTimeHelpOverlap() {
            GregorianCalendar startRecess = new GregorianCalendar();
            GregorianCalendar endRecess = new GregorianCalendar();
            //Start time
            String start = recessStartTimeField.getText();
            Integer start_hour = Integer.parseInt(start.replaceAll(":.+", ""));
            Integer start_minute = Integer.parseInt(start.replaceAll(".+:", ""));
            startRecess.setTime(currentDate.getTime());
            startRecess.set(Calendar.HOUR_OF_DAY, start_hour);
            startRecess.set(Calendar.MINUTE, start_minute);
            startRecess.set(Calendar.SECOND, 0);
            startRecess.set(Calendar.MILLISECOND, 0);
            // End time
            String end = recessEndTimeField.getText();
            Integer end_hour = Integer.parseInt(end.replaceAll(":.+", ""));
            Integer end_minute = Integer.parseInt(end.replaceAll(".+:", ""));
            endRecess.setTime(currentDate.getTime());
            endRecess.set(Calendar.HOUR_OF_DAY, end_hour);
            endRecess.set(Calendar.MINUTE, (end_minute));
            endRecess.set(Calendar.SECOND, 0);
            endRecess.set(Calendar.MILLISECOND, 0);

            ArrayList<RegisterPartime> listHelp = this.listStaffTimeHelp(((MstStaff)chargeStaff.getSelectedItem()).getStaffID(),currentDate, shopSelect.getShopID());
            for(int j = 0; j < listHelp.size(); j++)
            {
                RegisterPartime regis = new RegisterPartime();
                regis = listHelp.get(j);
                GregorianCalendar startHour = new GregorianCalendar();
                startHour.setTime(regis.getStart_time().getTime());
                startHour.set(Calendar.SECOND, 0);
                startHour.set(Calendar.MILLISECOND, 0);

                GregorianCalendar EndHour = new GregorianCalendar();
                EndHour.setTime(regis.getEnd_time().getTime());
                EndHour.set(Calendar.SECOND, 0);
                EndHour.set(Calendar.MILLISECOND, 0);

                if((startRecess.getTimeInMillis() <= startHour.getTimeInMillis() && endRecess.getTimeInMillis() > startHour.getTimeInMillis())) {
                    return false;
                }else if((startRecess.getTimeInMillis() >= startHour.getTimeInMillis()
                        && endRecess.getTimeInMillis() <= EndHour.getTimeInMillis())) {
                    return false;
                }else if((startRecess.getTimeInMillis() < EndHour.getTimeInMillis() && endRecess.getTimeInMillis() >= EndHour.getTimeInMillis())) {
                    return false;
                }else if((startRecess.getTimeInMillis() < startHour.getTimeInMillis() && endRecess.getTimeInMillis() > EndHour.getTimeInMillis())) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * @param staff_id
         * @param date
         * @return 
         */
        public String getSelectListStaffHelp(Integer staff_id, GregorianCalendar date) {

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" SELECT ");
            sql.append(" mshop.shop_id as main_shop_id, dsd.* ");
            sql.append("  FROM   ");
            sql.append("     data_schedule_detail dsd ");
            sql.append("     INNER JOIN mst_staff mstaff ON dsd.staff_id = mstaff.staff_id  ");
            sql.append("     AND mstaff.delete_date IS NULL  ");
            sql.append("     INNER JOIN mst_shop mshop ON mshop.shop_id = mstaff.shop_id  ");
            sql.append("     WHERE  ");
            sql.append("    dsd.staff_id =").append(SQLUtil.convertForSQL(staff_id));
            sql.append("     AND  ");
            sql.append("     dsd.delete_date IS NULL ");
            sql.append("     AND  ");
            sql.append("    dsd.schedule_date = ").append(SQLUtil.convertForSQL(date)).append("::date");

            return sql.toString();
        }
        /**
         * @param staff_id
         * @param date
         * @return 
         */
        public ArrayList<RegisterPartime> listStaffTimeHelp(Integer staff_id, GregorianCalendar date,Integer currentShopID) {

            ArrayList<RegisterPartime> list = new ArrayList<RegisterPartime>();
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = con.executeQuery(this.getSelectListStaffHelp(staff_id, date));
                RegisterPartime part;
                while (rs.next()) {
                    part = new RegisterPartime();
                    part.setShop_id_main(rs.getInt("main_shop_id"));
                    part.setShop_id_help(rs.getInt("shop_id"));
                    part.setStart_time(rs.getGregorianCalendar("ext_start_time"));
                    part.setEnd_time(rs.getGregorianCalendar("ext_end_time"));

                    list.add(part);
                }
            } catch (Exception e) {
            }
            return list;
        }
        
        //IVS_LVTu end add 2016/10/25 New request #57846

	private void setStaff(JComboBox staffCombo, String staffNo) {

	    for (int i = 0; i < staffCombo.getItemCount(); i++) {

		 MstStaff ms  = (MstStaff)staffCombo.getItemAt(i);

		 //ãÛîíÇÉZÉbÉg
		if (ms.getStaffID() == null) {

		    staffCombo.setSelectedIndex(0);

		} else if ( ms.getStaffNo().equals(staffNo)) {

		    staffCombo.setSelectedIndex(i);
		    return;
		}
	    }
	}
        
        //IVS_LVTu start 2015/10/14 Bug #43497
        private boolean  checkTime() {
            boolean flag = false;
            String strStartTime = "";
            String strEndTime = "";
            Date startTime;
            Date endTime;
            if(recessStartTime.getSelectedItem() == null){
                strStartTime = recessStartTimeField.getText().trim();
            } else {
                strStartTime = recessStartTime.getSelectedItem().toString().trim();
            }

            if(recessEndTime.getSelectedItem() == null){
                strEndTime = recessEndTimeField.getText().trim();
            } else {
                strEndTime = recessEndTime.getSelectedItem().toString().trim();
            }

            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");

        try {
            if ( strStartTime.equals("") || strEndTime.equals("")) {
                return false;
            }
            startTime = parser.parse(strStartTime);
            endTime = parser.parse(strEndTime);
            
            if (startTime.after(endTime) || startTime.equals(endTime)) {
                flag = true;
            }
        } catch (ParseException ex) {
            Logger.getLogger(RegistPersonalRecessPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return flag;
    }
    //IVS_LVTu end 2015/10/14 Bug #43497

}

/*
 * MstAutoMailSettingDetailPanel.java
 *
 * Created on 2011/02/23
 */

package com.geobeck.sosia.pos.mail;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.master.product.MstTechnicClass;
import com.geobeck.sosia.pos.hair.master.product.MstTechnicClasses;
import com.geobeck.sosia.pos.hair.search.product.SearchHairProductDialog;
import com.geobeck.sosia.pos.hair.search.product.SearchHairProductOpener;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.master.mail.MstAutoMailSetting;
import com.geobeck.sosia.pos.master.mail.MstMailTemplate;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.products.Product;
import com.geobeck.sosia.pos.search.mail.SearchMailSignatureDialog;
import com.geobeck.sosia.pos.search.mail.SearchMailTemplateDialog;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.FormatterCreator;
import com.geobeck.swing.MessageDialog;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.logging.*;


/**
 *
 * @author  geobeck
 */
public class MstAutoMailSettingDetailPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx implements  SearchHairProductOpener
{
	public MstAutoMailSettingDetailPanel(MstAutoMailSetting ams)
	{
            this.ams = ams;
            this.init();
	}

        private void init() {
	    this.setMailDatas(0);
	    initComponents();
            this.addMouseCursorChange();
	    this.setSize(800, 650);
            this.setPath("基本設定 >> メール関連");
            this.setTitle("自動メール設定");

            this.shopName.setText(ams.getShop().getShopName());
            this.mailName.setText(ams.getMailName());

            for (int i = 8; i <= 22; i++) {
                sendHour.addItem(i);
            }

            for (int i = 0; i <= 5; i++) {
                sendMinute.addItem(i * 10);
            }

            this.initTechnicClass();
            this.initItemClass();

            sendHour.setSelectedIndex(-1);
            sendHour.setSelectedItem(ams.getSendHour());
            sendMinute.setSelectedItem(ams.getSendMinute());
            
            switch (ams.getDateCondition().intValue()) {
                case 0: dateCondition0.setSelected(true); break;
                case 1: dateCondition1.setSelected(true); break;
                case 2: dateCondition2.setSelected(true); break;
                case 3: dateCondition3.setSelected(true); break;
                case 4: dateCondition4.setSelected(true); break;
                case 5: dateCondition5.setSelected(true); break;
                  //VTAn Start add 20130108
                case 6: dateCondition6.setSelected(true); break;
                case 7: dateCondition7.setSelected(true); break;
                case 8: dateCondition8.setSelected(true); break;    
                    //VTAn End add 20130108
            }

            daysAfterVisit.setText(ams.getDaysAfterVisit() == null ? "" : ams.getDaysAfterVisit().toString());
            lastDaysAfterVisit.setText(ams.getLastDaysAfterVisit() == null ? "" : ams.getLastDaysAfterVisit().toString());
            monthFixedDay.setText(ams.getMonthFixedDay() == null ? "" : ams.getMonthFixedDay().toString());
            daysBeforeBirthday.setText(ams.getDaysBeforeBirthday() == null ? "" : ams.getDaysBeforeBirthday().toString());
            daysBeforeReservation.setText(ams.getDaysBeforeReservation() == null ? "" : ams.getDaysBeforeReservation().toString());
             //VTAn Start add 20130108
            days_cycle_before.setText(ams.getDaysCycleBefore() == null ? "" : ams.getDaysCycleBefore().toString());
            days_cycle_before_follow.setText(ams.getDaysCycleBeforeFollow() == null ? "" : ams.getDaysCycleBeforeFollow().toString());
            days_re_cycle_before.setText(ams.getDaysReCycleBefore() == null ? "" : ams.getDaysReCycleBefore().toString());
            //VTAn End add 20130108

            switch (ams.getVisitType().intValue()) {
                case 0: visitAll.setSelected(true); break;
                case 1: visitNew.setSelected(true); break;
                case 2: visitFixed.setSelected(true); break;
            }
            
            switch (ams.getSex().intValue()) {
                case 0: both.setSelected(true); break;
                case 1: male.setSelected(true); break;
                case 2: female.setSelected(true); break;
            }

            if (ams.getTechnicClassId() != null) {
                for (int i = 0; i < technicClass.getItemCount(); i++) {
                    if (ams.getTechnicClassId().equals(((MstTechnicClass)technicClass.getItemAt(i)).getTechnicClassID())) {
                        technicClass.setSelectedIndex(i);
                        break;
                    }
                }
            }

            if (ams.getItemClassId() != null) {
                for (int i = 0; i < itemClass.getItemCount(); i++) {
                    if (ams.getItemClassId().equals(((MstItemClass)itemClass.getItemAt(i)).getItemClassID())) {
                        itemClass.setSelectedIndex(i);
                        break;
                    }
                }
            }

            mailTitle.setText(ams.getMailTitle());
            mailBody.setText(ams.getMailBody());

            if (!SystemInfo.isUsePointcard()){
                pointKey.setVisible(false);
            }

            this.sendMinute.setVisible(false);
            this.sendMinuteLabel.setVisible(false);
            
            //IVS_LVTu start add 2016/02/23 New request #48621
            courseKey.setVisible(this.courseFlag);
            //IVS_LVTu end add 2016/02/23 New request #48621

        }
        
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateGroup = new javax.swing.ButtonGroup();
        sexGroup = new javax.swing.ButtonGroup();
        visitTypeGroup = new javax.swing.ButtonGroup();
        keyPanel = new javax.swing.JPanel();
        customerNameKey = new javax.swing.JButton();
        staffNameKey = new javax.swing.JButton();
        lastVisitKey = new javax.swing.JButton();
        technicKey = new javax.swing.JButton();
        itemKey = new javax.swing.JButton();
        shopNameKey = new javax.swing.JButton();
        shopAddressKey = new javax.swing.JButton();
        shopTelKey = new javax.swing.JButton();
        shopMailKey = new javax.swing.JButton();
        signatureKey = new javax.swing.JButton();
        nextReservationKey = new javax.swing.JButton();
        reservationStaffKey = new javax.swing.JButton();
        pointKey = new javax.swing.JButton();
        nextReservationMenuKey = new javax.swing.JButton();
        courseKey = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        viewTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        templateButton = new javax.swing.JButton();
        mailTitleLabel = new javax.swing.JLabel();
        mailTitle = new javax.swing.JFormattedTextField();
        mailBodyLabel = new javax.swing.JLabel();
        mailBodyScrollPane = new javax.swing.JScrollPane();
        mailBody = new javax.swing.JTextArea();
        registButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        mailName = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        shopName = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        sendMinuteLabel = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        sendMinute = new javax.swing.JComboBox();
        sendHour = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        both = new javax.swing.JRadioButton();
        dateCondition0 = new javax.swing.JRadioButton();
        dateCondition5 = new javax.swing.JRadioButton();
        dateCondition2 = new javax.swing.JRadioButton();
        dateCondition3 = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        male = new javax.swing.JRadioButton();
        daysBeforeBirthday = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(9, 1, 999));
        jLabel30 = new javax.swing.JLabel();
        daysAfterVisit = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(3, 1, 999));
        monthFixedDay = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(2, 1, 31));
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        technicClass = new javax.swing.JComboBox();
        dateCondition1 = new javax.swing.JRadioButton();
        lastDaysAfterVisit = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(3, 1, 999));
        jLabel33 = new javax.swing.JLabel();
        dateCondition4 = new javax.swing.JRadioButton();
        daysBeforeReservation = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(9, 1, 999));
        jLabel34 = new javax.swing.JLabel();
        visitNew = new javax.swing.JRadioButton();
        visitFixed = new javax.swing.JRadioButton();
        visitAll = new javax.swing.JRadioButton();
        jLabel35 = new javax.swing.JLabel();
        itemClass = new javax.swing.JComboBox();
        dateCondition6 = new javax.swing.JRadioButton();
        dateCondition7 = new javax.swing.JRadioButton();
        dateCondition8 = new javax.swing.JRadioButton();
        days_re_cycle_before = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(9, 1, 999));
        days_cycle_before_follow = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(9, 1, 999));
        days_cycle_before = new com.geobeck.swing.JFormattedTextFieldEx(
            FormatterCreator.createNumberFormatter(9, 1, 999));
        jLabel38 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        shopName1 = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        keyPanel.setOpaque(false);

        customerNameKey.setIcon(SystemInfo.getImageIcon("/button/key/customer_name_off.jpg"));
        customerNameKey.setBorderPainted(false);
        customerNameKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/customer_name_on.jpg"));
        customerNameKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerNameKeyActionPerformed(evt);
            }
        });

        staffNameKey.setIcon(SystemInfo.getImageIcon("/button/key/staff_name_off.jpg"));
        staffNameKey.setBorderPainted(false);
        staffNameKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/staff_name_on.jpg"));
        staffNameKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffNameKeyActionPerformed(evt);
            }
        });

        lastVisitKey.setIcon(SystemInfo.getImageIcon("/button/key/last_visit_off.jpg"));
        lastVisitKey.setBorderPainted(false);
        lastVisitKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/last_visit_on.jpg"));
        lastVisitKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastVisitKeyActionPerformed(evt);
            }
        });

        technicKey.setIcon(SystemInfo.getImageIcon("/button/key/technic_name_off.jpg"));
        technicKey.setBorderPainted(false);
        technicKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/technic_name_on.jpg"));
        technicKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                technicKeyActionPerformed(evt);
            }
        });

        itemKey.setIcon(SystemInfo.getImageIcon("/button/key/item_name_off.jpg"));
        itemKey.setBorderPainted(false);
        itemKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/item_name_on.jpg"));
        itemKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemKeyActionPerformed(evt);
            }
        });

        shopNameKey.setIcon(SystemInfo.getImageIcon("/button/key/shop_name_off.jpg"));
        shopNameKey.setBorderPainted(false);
        shopNameKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/shop_name_on.jpg"));
        shopNameKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopNameKeyActionPerformed(evt);
            }
        });

        shopAddressKey.setIcon(SystemInfo.getImageIcon("/button/key/shop_address_off.jpg"));
        shopAddressKey.setBorderPainted(false);
        shopAddressKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/shop_address_on.jpg"));
        shopAddressKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopAddressKeyActionPerformed(evt);
            }
        });

        shopTelKey.setIcon(SystemInfo.getImageIcon("/button/key/shop_tel_off.jpg"));
        shopTelKey.setBorderPainted(false);
        shopTelKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/shop_tel_on.jpg"));
        shopTelKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopTelKeyActionPerformed(evt);
            }
        });

        shopMailKey.setIcon(SystemInfo.getImageIcon("/button/key/shop_mail_off.jpg"));
        shopMailKey.setBorderPainted(false);
        shopMailKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/shop_mail_on.jpg"));
        shopMailKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopMailKeyActionPerformed(evt);
            }
        });

        signatureKey.setIcon(SystemInfo.getImageIcon("/button/key/signature_off.jpg"));
        signatureKey.setBorderPainted(false);
        signatureKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/signature_on.jpg"));
        signatureKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signatureKeyActionPerformed(evt);
            }
        });

        nextReservationKey.setIcon(SystemInfo.getImageIcon("/button/key/next_reservation_off.jpg"));
        nextReservationKey.setBorderPainted(false);
        nextReservationKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/next_reservation_on.jpg"));
        nextReservationKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextReservationKeyActionPerformed(evt);
            }
        });

        reservationStaffKey.setIcon(SystemInfo.getImageIcon("/button/key/reservation_staff_off.jpg"));
        reservationStaffKey.setBorderPainted(false);
        reservationStaffKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/reservation_staff_on.jpg"));
        reservationStaffKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservationStaffKeyActionPerformed(evt);
            }
        });

        pointKey.setIcon(SystemInfo.getImageIcon("/button/common/point_off.jpg"));
        pointKey.setBorderPainted(false);
        pointKey.setPressedIcon(SystemInfo.getImageIcon("/button/common/point_on.jpg"));
        pointKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointKeyActionPerformed(evt);
            }
        });

        nextReservationMenuKey.setIcon(SystemInfo.getImageIcon("/button/key/next_reservation_menu_off.jpg"));
        nextReservationMenuKey.setBorderPainted(false);
        nextReservationMenuKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/next_reservation_menu_on.jpg"));
        nextReservationMenuKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextReservationMenuKeyActionPerformed(evt);
            }
        });

        courseKey.setIcon(SystemInfo.getImageIcon("/button/key/course_name_off.jpg"));
        courseKey.setBorderPainted(false);
        courseKey.setPressedIcon(SystemInfo.getImageIcon("/button/key/course_name_on.jpg"));
        courseKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseKeyActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout keyPanelLayout = new org.jdesktop.layout.GroupLayout(keyPanel);
        keyPanel.setLayout(keyPanelLayout);
        keyPanelLayout.setHorizontalGroup(
            keyPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(customerNameKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(staffNameKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(lastVisitKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(pointKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(technicKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(itemKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(shopNameKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(shopAddressKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(shopTelKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(shopMailKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(signatureKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(nextReservationKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(reservationStaffKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(nextReservationMenuKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(courseKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        keyPanelLayout.setVerticalGroup(
            keyPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(keyPanelLayout.createSequentialGroup()
                .add(customerNameKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(staffNameKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lastVisitKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pointKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(technicKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(itemKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(courseKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(shopNameKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(shopAddressKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(shopTelKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(shopMailKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(signatureKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nextReservationKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(reservationStaffKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nextReservationMenuKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        viewTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                viewTabStateChanged(evt);
            }
        });

        jPanel1.setOpaque(false);

        templateButton.setIcon(SystemInfo.getImageIcon("/button/mail/template_off.jpg"));
        templateButton.setBorderPainted(false);
        templateButton.setContentAreaFilled(false);
        templateButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/template_on.jpg"));
        templateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateButtonActionPerformed(evt);
            }
        });

        mailTitleLabel.setText("タイトル");

        mailTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        mailBodyLabel.setText("本文");

        mailBodyScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mailBodyScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mailBody.setColumns(20);
        mailBody.setLineWrap(true);
        mailBody.setRows(5);
        mailBodyScrollPane.setViewportView(mailBody);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mailBodyScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mailTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(mailTitleLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 191, Short.MAX_VALUE)
                        .add(templateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mailBodyLabel))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(templateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(mailTitleLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mailTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mailBodyLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mailBodyScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        viewTab.addTab("本文作成", jPanel1);

        registButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.setBorderPainted(false);
        registButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButtonActionPerformed(evt);
            }
        });

        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        mailName.setText("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        jPanel2.add(mailName);
        mailName.setBounds(60, 30, 240, 20);

        jLabel20.setText("技術分類");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(20, 520, 60, 20);

        jLabel21.setText("対象");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(10, 10, 50, 20);

        shopName.setText("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        jPanel2.add(shopName);
        shopName.setBounds(60, 10, 240, 20);

        jLabel22.setText("名称");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(10, 30, 50, 20);

        sendMinuteLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sendMinuteLabel.setText("分");
        jPanel2.add(sendMinuteLabel);
        sendMinuteLabel.setBounds(190, 80, 20, 20);

        jLabel24.setText("【配信日付条件】");
        jPanel2.add(jLabel24);
        jLabel24.setBounds(10, 120, 100, 20);

        jLabel25.setText("【属性】");
        jPanel2.add(jLabel25);
        jLabel25.setBounds(20, 430, 100, 20);

        jLabel26.setText("来店区分");
        jPanel2.add(jLabel26);
        jLabel26.setBounds(20, 460, 60, 20);

        jLabel27.setText("性別");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(20, 490, 60, 20);

        sendMinute.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel2.add(sendMinute);
        sendMinute.setBounds(140, 80, 50, 20);

        sendHour.setMaximumRowCount(20);
        sendHour.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel2.add(sendHour);
        sendHour.setBounds(70, 80, 50, 20);

        jLabel28.setText("日前");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(140, 240, 30, 20);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("時");
        jPanel2.add(jLabel29);
        jLabel29.setBounds(120, 80, 20, 20);

        sexGroup.add(both);
        both.setText("すべて");
        both.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        both.setMargin(new java.awt.Insets(0, 0, 0, 0));
        both.setOpaque(false);
        jPanel2.add(both);
        both.setBounds(210, 490, 50, 20);

        dateGroup.add(dateCondition0);
        dateCondition0.setSelected(true);
        dateCondition0.setText("来店");
        dateCondition0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition0.setOpaque(false);
        jPanel2.add(dateCondition0);
        dateCondition0.setBounds(30, 150, 50, 20);

        dateGroup.add(dateCondition5);
        dateCondition5.setText("毎月");
        dateCondition5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition5.setOpaque(false);
        jPanel2.add(dateCondition5);
        dateCondition5.setBounds(30, 300, 50, 20);

        dateGroup.add(dateCondition2);
        dateCondition2.setText("誕生日当日");
        dateCondition2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition2.setOpaque(false);
        jPanel2.add(dateCondition2);
        dateCondition2.setBounds(30, 210, 110, 20);

        dateGroup.add(dateCondition3);
        dateCondition3.setText("誕生日");
        dateCondition3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition3.setOpaque(false);
        jPanel2.add(dateCondition3);
        dateCondition3.setBounds(30, 240, 60, 20);

        sexGroup.add(female);
        female.setText("女性");
        female.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        female.setMargin(new java.awt.Insets(0, 0, 0, 0));
        female.setOpaque(false);
        jPanel2.add(female);
        female.setBounds(90, 490, 50, 20);

        sexGroup.add(male);
        male.setText("男性");
        male.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        male.setMargin(new java.awt.Insets(0, 0, 0, 0));
        male.setOpaque(false);
        jPanel2.add(male);
        male.setBounds(150, 490, 50, 20);

        daysBeforeBirthday.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        daysBeforeBirthday.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(daysBeforeBirthday);
        daysBeforeBirthday.setBounds(90, 240, 40, 20);

        jLabel30.setText("配信時間");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(10, 80, 50, 20);

        daysAfterVisit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        daysAfterVisit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(daysAfterVisit);
        daysAfterVisit.setBounds(80, 150, 40, 20);

        monthFixedDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        monthFixedDay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(monthFixedDay);
        monthFixedDay.setBounds(80, 300, 40, 20);

        jLabel31.setText("日後");
        jPanel2.add(jLabel31);
        jLabel31.setBounds(130, 150, 50, 20);

        jLabel32.setText("日");
        jPanel2.add(jLabel32);
        jLabel32.setBounds(130, 300, 20, 20);

        technicClass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClass.setEnabled(false);
        jPanel2.add(technicClass);
        technicClass.setBounds(90, 520, 110, 20);

        dateGroup.add(dateCondition1);
        dateCondition1.setText("最終来店日から");
        dateCondition1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition1.setOpaque(false);
        jPanel2.add(dateCondition1);
        dateCondition1.setBounds(30, 180, 100, 20);

        lastDaysAfterVisit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lastDaysAfterVisit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(lastDaysAfterVisit);
        lastDaysAfterVisit.setBounds(130, 180, 40, 20);

        jLabel33.setText("日後");
        jPanel2.add(jLabel33);
        jLabel33.setBounds(200, 180, 50, 20);

        dateGroup.add(dateCondition4);
        dateCondition4.setText("予約日");
        dateCondition4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition4.setOpaque(false);
        jPanel2.add(dateCondition4);
        dateCondition4.setBounds(30, 270, 60, 20);

        daysBeforeReservation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        daysBeforeReservation.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(daysBeforeReservation);
        daysBeforeReservation.setBounds(90, 270, 40, 20);

        jLabel34.setText("日前");
        jPanel2.add(jLabel34);
        jLabel34.setBounds(140, 270, 30, 20);

        visitTypeGroup.add(visitNew);
        visitNew.setText("新規");
        visitNew.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        visitNew.setMargin(new java.awt.Insets(0, 0, 0, 0));
        visitNew.setOpaque(false);
        visitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitNewActionPerformed(evt);
            }
        });
        jPanel2.add(visitNew);
        visitNew.setBounds(90, 460, 50, 20);

        visitTypeGroup.add(visitFixed);
        visitFixed.setText("固定(2回目～)");
        visitFixed.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        visitFixed.setMargin(new java.awt.Insets(0, 0, 0, 0));
        visitFixed.setOpaque(false);
        jPanel2.add(visitFixed);
        visitFixed.setBounds(140, 460, 103, 20);

        visitTypeGroup.add(visitAll);
        visitAll.setSelected(true);
        visitAll.setText("すべて");
        visitAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        visitAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        visitAll.setOpaque(false);
        jPanel2.add(visitAll);
        visitAll.setBounds(250, 460, 50, 20);

        jLabel35.setText("商品分類");
        jPanel2.add(jLabel35);
        jLabel35.setBounds(20, 550, 60, 20);

        itemClass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemClass.setEnabled(false);
        jPanel2.add(itemClass);
        itemClass.setBounds(90, 550, 110, 20);

        dateGroup.add(dateCondition6);
        dateCondition6.setText("(お誘いメール)  来店周期の");
        dateCondition6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition6.setEnabled(false);
        dateCondition6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition6.setOpaque(false);
        dateCondition6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateCondition6ActionPerformed(evt);
            }
        });
        jPanel2.add(dateCondition6);
        dateCondition6.setBounds(30, 330, 200, 20);

        dateGroup.add(dateCondition7);
        dateCondition7.setText("(お誘いフォローメール)  来店周期の");
        dateCondition7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition7.setEnabled(false);
        dateCondition7.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition7.setOpaque(false);
        jPanel2.add(dateCondition7);
        dateCondition7.setBounds(30, 360, 200, 20);

        dateGroup.add(dateCondition8);
        dateCondition8.setText("(再お誘いメール)  来店周期の");
        dateCondition8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateCondition8.setEnabled(false);
        dateCondition8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateCondition8.setOpaque(false);
        jPanel2.add(dateCondition8);
        dateCondition8.setBounds(30, 390, 200, 20);

        days_re_cycle_before.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        days_re_cycle_before.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        days_re_cycle_before.setEnabled(false);
        jPanel2.add(days_re_cycle_before);
        days_re_cycle_before.setBounds(230, 390, 40, 20);

        days_cycle_before_follow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        days_cycle_before_follow.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        days_cycle_before_follow.setEnabled(false);
        jPanel2.add(days_cycle_before_follow);
        days_cycle_before_follow.setBounds(230, 360, 40, 20);

        days_cycle_before.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        days_cycle_before.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        days_cycle_before.setEnabled(false);
        jPanel2.add(days_cycle_before);
        days_cycle_before.setBounds(230, 330, 40, 20);

        jLabel38.setText("日前");
        jPanel2.add(jLabel38);
        jLabel38.setBounds(280, 330, 30, 20);

        jLabel36.setText("日前");
        jPanel2.add(jLabel36);
        jLabel36.setBounds(280, 360, 30, 20);

        jLabel37.setText("日前");
        jPanel2.add(jLabel37);
        jLabel37.setBounds(280, 390, 30, 20);

        shopName1.setForeground(new java.awt.Color(255, 0, 1));
        shopName1.setText("<html>※予約日○日前で設定の場合、「次回予約日時」「予約担当者」「次回予約内容」で表示される内容は<br>&nbsp;&nbsp;&nbsp;○日後の予約情報を表示します。</html>");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(shopName1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 317, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16)
                        .add(keyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(10, 10, 10)
                                .add(viewTab))
                            .add(layout.createSequentialGroup()
                                .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(viewTab))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(keyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 574, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(shopName1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void staffNameKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffNameKeyActionPerformed
	insertKey(MailUtil.KEY_STAFF_NAME);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_staffNameKeyActionPerformed

    private void shopNameKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopNameKeyActionPerformed
//		insertKey(targetName);
	insertKey(MailUtil.KEY_TARGET_NAME);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_shopNameKeyActionPerformed

    private void shopAddressKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopAddressKeyActionPerformed
//		insertKey(targetAddress);
	insertKey(MailUtil.KEY_TARGET_ADDRESS);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_shopAddressKeyActionPerformed

    private void shopTelKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopTelKeyActionPerformed
//		insertKey(targetPhoneNumber);
	insertKey(MailUtil.KEY_TARGET_TEL);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_shopTelKeyActionPerformed

    private void shopMailKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopMailKeyActionPerformed
//	    insertKey(targetMailAddress);
	insertKey(MailUtil.KEY_TARGET_MAIL);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_shopMailKeyActionPerformed

    private void itemKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemKeyActionPerformed
	SystemInfo.getLogger().log(Level.INFO, "商品検索");
        //IVS_LVTu start edit 2016/02/23 New request #48621
	//SearchHairProductDialog	spd = new SearchHairProductDialog(parentFrame, true, this, 2);
        MstShop shop = new MstShop();
        shop.setShopID(shopID);
        SearchHairProductDialog	spd = new SearchHairProductDialog(parentFrame, true, this, shop, 2);
        //IVS_LVTu end edit 2016/02/23 New request #48621
	spd.setVisible(true);
	spd = null;
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_itemKeyActionPerformed

    private void technicKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_technicKeyActionPerformed
	SystemInfo.getLogger().log(Level.INFO, "施術検索");
        //IVS_LVTu start edit 2016/02/23 New request #48621
	//SearchHairProductDialog	spd = new SearchHairProductDialog(parentFrame, true, this, 1);
        MstShop shop = new MstShop();
        shop.setShopID(shopID);
        SearchHairProductDialog	spd = new SearchHairProductDialog(parentFrame, true, this, shop, 1);
        //IVS_LVTu end edit 2016/02/23 New request #48621
	spd.setVisible(true);
	spd = null;
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_technicKeyActionPerformed

    private void customerNameKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerNameKeyActionPerformed
	insertKey(MailUtil.KEY_CUSTOMER_NAME);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_customerNameKeyActionPerformed

    private void lastVisitKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastVisitKeyActionPerformed
	insertKey(MailUtil.KEY_LAST_VISIT);
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_lastVisitKeyActionPerformed

    private void signatureKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signatureKeyActionPerformed
	SystemInfo.getLogger().log(Level.INFO, "署名検索");
	SearchMailSignatureDialog	smsd	=	new SearchMailSignatureDialog(parentFrame, true, groupID, shopID);
	smsd.setVisible(true);
	
	if (smsd.getSelectedSignatureID() != null) {
	    insertKey("[署名" + smsd.getSelectedSignatureID().toString() + "]");
	}
	
	smsd = null;
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_signatureKeyActionPerformed

	private void viewTabStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_viewTabStateChanged
	{//GEN-HEADEREND:event_viewTabStateChanged
	    if(viewTab.getSelectedIndex() == 1) {
	    }
	}//GEN-LAST:event_viewTabStateChanged

	private void templateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_templateButtonActionPerformed
	{//GEN-HEADEREND:event_templateButtonActionPerformed

            SearchMailTemplateDialog smtd = new SearchMailTemplateDialog(parentFrame, true, groupID, shopID);
	    smtd.setVisible(true);

	    MstMailTemplate result = smtd.getSelectedTemplate();

	    if (result != null) {
		mailTitle.setText(result.getMailTemplateTitle());
		mailBody.setText(result.getMailTemplateBody());
                mailTitle.setCaretPosition(0);
                mailBody.setCaretPosition(0);
	    }

	    smtd = null;
	}//GEN-LAST:event_templateButtonActionPerformed

	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
	{//GEN-HEADEREND:event_closeButtonActionPerformed
            this.closeWindow();
	}//GEN-LAST:event_closeButtonActionPerformed

	private void registButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registButtonActionPerformed
	{//GEN-HEADEREND:event_registButtonActionPerformed
            if(this.checkInput()) {
                this.regist();
            }
	}//GEN-LAST:event_registButtonActionPerformed

        private void visitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitNewActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_visitNewActionPerformed

        private void nextReservationKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextReservationKeyActionPerformed
            insertKey(MailUtil.KEY_NEXT_RESERVATION);
            mailBody.requestFocusInWindow();
}//GEN-LAST:event_nextReservationKeyActionPerformed

        private void reservationStaffKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservationStaffKeyActionPerformed
            insertKey(MailUtil.KEY_RESERVATION_STAFF);
            mailBody.requestFocusInWindow();
}//GEN-LAST:event_reservationStaffKeyActionPerformed

        private void pointKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointKeyActionPerformed
            insertKey(MailUtil.KEY_POINT);
            mailBody.requestFocusInWindow();
}//GEN-LAST:event_pointKeyActionPerformed

        private void nextReservationMenuKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextReservationMenuKeyActionPerformed
            insertKey(MailUtil.KEY_NEXT_RESERVATION_MENU);
            mailBody.requestFocusInWindow();
        }//GEN-LAST:event_nextReservationMenuKeyActionPerformed

    private void dateCondition6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateCondition6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateCondition6ActionPerformed

    //IVS_LVTu start add 2016/02/23 New request #48621
    private void courseKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseKeyActionPerformed
        SystemInfo.getLogger().log(Level.INFO, "コース名");
        MstShop shop = new MstShop();
        shop.setShopID(shopID);
        SearchHairProductDialog	spd = new SearchHairProductDialog(parentFrame, true, this, shop, 5);
	spd.setVisible(true);
	spd = null;
	mailBody.requestFocusInWindow();
    }//GEN-LAST:event_courseKeyActionPerformed
    //IVS_LVTu end add 2016/02/23 New request #48621	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton both;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton courseKey;
    private javax.swing.JButton customerNameKey;
    private javax.swing.JRadioButton dateCondition0;
    private javax.swing.JRadioButton dateCondition1;
    private javax.swing.JRadioButton dateCondition2;
    private javax.swing.JRadioButton dateCondition3;
    private javax.swing.JRadioButton dateCondition4;
    private javax.swing.JRadioButton dateCondition5;
    private javax.swing.JRadioButton dateCondition6;
    private javax.swing.JRadioButton dateCondition7;
    private javax.swing.JRadioButton dateCondition8;
    private javax.swing.ButtonGroup dateGroup;
    private com.geobeck.swing.JFormattedTextFieldEx daysAfterVisit;
    private com.geobeck.swing.JFormattedTextFieldEx daysBeforeBirthday;
    private com.geobeck.swing.JFormattedTextFieldEx daysBeforeReservation;
    private com.geobeck.swing.JFormattedTextFieldEx days_cycle_before;
    private com.geobeck.swing.JFormattedTextFieldEx days_cycle_before_follow;
    private com.geobeck.swing.JFormattedTextFieldEx days_re_cycle_before;
    private javax.swing.JRadioButton female;
    private javax.swing.JComboBox itemClass;
    private javax.swing.JButton itemKey;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel keyPanel;
    private com.geobeck.swing.JFormattedTextFieldEx lastDaysAfterVisit;
    private javax.swing.JButton lastVisitKey;
    private javax.swing.JTextArea mailBody;
    private javax.swing.JLabel mailBodyLabel;
    private javax.swing.JScrollPane mailBodyScrollPane;
    private javax.swing.JLabel mailName;
    private javax.swing.JFormattedTextField mailTitle;
    private javax.swing.JLabel mailTitleLabel;
    private javax.swing.JRadioButton male;
    private com.geobeck.swing.JFormattedTextFieldEx monthFixedDay;
    private javax.swing.JButton nextReservationKey;
    private javax.swing.JButton nextReservationMenuKey;
    private javax.swing.JButton pointKey;
    private javax.swing.JButton registButton;
    private javax.swing.JButton reservationStaffKey;
    private javax.swing.JComboBox sendHour;
    private javax.swing.JComboBox sendMinute;
    private javax.swing.JLabel sendMinuteLabel;
    private javax.swing.ButtonGroup sexGroup;
    private javax.swing.JButton shopAddressKey;
    private javax.swing.JButton shopMailKey;
    private javax.swing.JLabel shopName;
    private javax.swing.JLabel shopName1;
    private javax.swing.JButton shopNameKey;
    private javax.swing.JButton shopTelKey;
    private javax.swing.JButton signatureKey;
    private javax.swing.JButton staffNameKey;
    private javax.swing.JComboBox technicClass;
    private javax.swing.JButton technicKey;
    private javax.swing.JButton templateButton;
    private javax.swing.JTabbedPane viewTab;
    private javax.swing.JRadioButton visitAll;
    private javax.swing.JRadioButton visitFixed;
    private javax.swing.JRadioButton visitNew;
    private javax.swing.ButtonGroup visitTypeGroup;
    // End of variables declaration//GEN-END:variables

    private MstAutoMailSetting ams = null;

	private	Integer		groupID			=	null;
	private	Integer		shopID			=	null;
	
	private	Object		target			=	null;
	private	String		targetName		=	"";
	private	String		targetAddress		=	"";
	private	String		targetPhoneNumber	=	"";
	private	String		targetMailAddress	=	"";

        private Map                    mapMail          =       null;
	private ArrayList<MstCustomer> targetCustomers	=	null;
	private ArrayList<MstCustomer> mailCustomers	=	new ArrayList<MstCustomer>();
	private ArrayList<DataMail>    baseDatas        =	new ArrayList<DataMail>();
	private ArrayList<DataMail>    mailDatas	=	new ArrayList<DataMail>();
        private ArrayList<DataMail>    targetList       =       new ArrayList<DataMail>();

	private Integer		selectedIndex		=	-1;
        
        //IVS_LVTu start add 2016/02/23 New request #48621
        private boolean         courseFlag              =       true;
	//IVS_LVTu end add 2016/02/23 New request #48621
	/**
	 * メール送信画面用FocusTraversalPolicy
	 */
	private	HairCommonMailFocusTraversalPolicy	ftp	=
			new HairCommonMailFocusTraversalPolicy();
	
	/**
	 * メール送信画面用FocusTraversalPolicyを取得する。
	 * @return メール送信画面用FocusTraversalPolicy
	 */
	public HairCommonMailFocusTraversalPolicy getFocusTraversalPolicy()
	{
	    return	ftp;
	}

        /**
         * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
         */
        private void addMouseCursorChange() {
            SystemInfo.addMouseCursorChange(closeButton);
            SystemInfo.addMouseCursorChange(registButton);
            SystemInfo.addMouseCursorChange(templateButton);
        }
        
	/**
	 * 対象グループ・店舗のグループＩＤを設定する。
	 */
	public void setGroupID(Integer groupID)
	{
	    this.groupID = groupID;
	}

	/**
	 * 対象グループ・店舗の店舗ＩＤを設定する。
	 */
	public void setShopID(Integer shopID)
	{
	    this.shopID = shopID;
	}

	/**
	 * 対象グループ・店舗の名称を設定する。
	 */
	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

	/**
	 * 対象グループ・店舗の住所を設定する。
	 */
	public void setTargetAddress(String targetAddress)
	{
		this.targetAddress = targetAddress;
	}

	/**
	 * 対象グループ・店舗の電話番号を設定する。
	 */
	public void setTargetPhoneNumber(String targetPhoneNumber)
	{
		this.targetPhoneNumber = targetPhoneNumber;
	}

	/**
	 * 対象グループ・店舗のメールアドレスを設定する。
	 */
	public void setTargetMailAddress(String targetMailAddress)
	{
		this.targetMailAddress = targetMailAddress;
	}

	/**
	 * 対象グループ・店舗のグループＩＤを取得する。
	 */
	public Integer getGroupID()
	{
		return groupID;
	}

	/**
	 * 対象グループ・店舗の店舗ＩＤを取得する。
	 */
	public Integer getShopID()
	{
		return shopID;
	}

	/**
	 * 対象グループ・店舗の名称を取得する。
	 */
	public String getTargetName()
	{
		return targetName;
	}

	/**
	 * 対象グループ・店舗の住所を取得する。
	 */
	public String getTargetAddress()
	{
		return targetAddress;
	}

	/**
	 * 対象グループ・店舗の電話番号を設定する。
	 */
	public String getTargetPhoneNumber()
	{
		return targetPhoneNumber;
	}

	/**
	 * 対象グループ・店舗のメールアドレスを取得する。
	 */
	public String getTargetMailAddress()
	{
		return targetMailAddress;
	}
	
	/**
	 * キー項目を挿入する。
	 */
	private void insertKey(String keyString)
	{
		mailBody.replaceRange(keyString,
				mailBody.getSelectionStart(),
				mailBody.getSelectionEnd());
	}
	
        //IVS_LVTu start edit 2016/02/23 New request #48621
	/**
	 * 対象グループ・店舗情報を設定する。
	 */
	public void setTarget(Object target)
	{
		this.target	=	target;
		
		if(target instanceof MstGroup)
		{
			MstGroup	group	=	(MstGroup)target;
			
			groupID				=	group.getGroupID();
			shopID				=	0;
			targetName			=	group.getGroupName();
			targetAddress		=	group.getFullAddress();
			targetPhoneNumber	=	group.getPhoneNumber();
			targetMailAddress	=	group.getMailAddress();
                        if(!checkShopCourseFlag(group) || shopID == 0) {
                            this.courseFlag = false;
                            courseKey.setVisible(this.courseFlag);
                        }
		}
		else if(target instanceof MstShop)
		{
			MstShop		shop	=	(MstShop)target;
			
			groupID				=	shop.getGroupID();
			shopID				=	shop.getShopID();
			targetName			=	shop.getShopName();
			targetAddress		=	shop.getFullAddress();
			targetPhoneNumber	=	shop.getPhoneNumber();
			targetMailAddress	=	shop.getMailAddress();
                        
                        if(shop.getCourseFlag() != 1) {
                             this.courseFlag = false;
                             courseKey.setVisible(this.courseFlag);
                        }
		}
	}
        

        private boolean checkShopCourseFlag(MstGroup mg) {
                //グループ
            if(mg.getShops().size()> 0) {
                for (MstShop mshop : mg.getShops()) {
                    if ( mshop.getCourseFlag().equals(1)) {
                        return true;
                    }
                }
            } else if ( mg.getGroups().size() > 0) {
                for ( int i = 0;i < mg.getGroups().size() ;i ++) {
                    return checkShopCourseFlag(mg.getGroups().get(i));
                }
            }
            return false;
        }

        //IVS_LVTu end edit 2016/02/23 New request #48621
	/**
	 * メールデータを取得する。
	 */
	public ArrayList<DataMail> getMailDatas()
	{
		return mailDatas;
	}

	/**
	 * メールデータを設定する。
	 */
	public void setMailDatas(int selectedAddress)
	{
	    baseDatas.clear();
            
            if (mapMail != null) {

                for(MstCustomer mc : mailCustomers) {
//                    DataDmHistoryDetail dhd = (DataDmHistoryDetail)mapMail.get(mc.getCustomerID());
//                    baseDatas.add(new DataMail(mc, selectedAddress, dhd.getMailTitle(), dhd.getMailBody()));
                }
                
            } else {

                for(MstCustomer mc : mailCustomers) {
                    baseDatas.add(new DataMail(mc, selectedAddress));
                }
            }
            
            mailDatas.clear();
            mailDatas.addAll(baseDatas);
	}
	

	/**
	 * 選択された施術・商品名を追加する。
	 * @param productDivision 1 - 施術、2 - 商品
	 * @param product 施術・商品
	 */
	public void addSelectedProduct(Integer productDivision, Product product)
	{
//		this.insertKey(product.getProductName());
		if (productDivision == 1) {
		    insertKey("[施術" + product.getProductNo() + "]");
		} else if (productDivision == 2) {
		    insertKey("[商品" + product.getProductNo() + "]");
		}
		
		mailBody.requestFocusInWindow();
	}
       
	//IVS_LVTu start add 2016/02/23 New request #48621
	public void addSelectedCourse(Integer productDivision, Course course){
            insertKey("[コース" + course.getCourseId() + "]");
            mailBody.requestFocusInWindow();
        }
        //IVS_LVTu end add 2016/02/23 New request #48621

	public void addSelectedConsumptionCourse(Integer productDivision, ConsumptionCourse consumptionCourse){}
	
	private void regist()
	{
            if (sendHour.getSelectedIndex() != -1) {
                ams.setSendHour((Integer)sendHour.getSelectedItem());
            }

            ams.setSendMinute((Integer)sendMinute.getSelectedItem());

            if (dateCondition0.isSelected()) {
                ams.setDateCondition(0);
            } else if (dateCondition1.isSelected()) {
                ams.setDateCondition(1);
            } else if (dateCondition2.isSelected()) {
                ams.setDateCondition(2);
            } else if (dateCondition3.isSelected()) {
                ams.setDateCondition(3);
            } else if (dateCondition4.isSelected()) {
                ams.setDateCondition(4);
            } else if (dateCondition5.isSelected()) {
                ams.setDateCondition(5);
            } 
            //VTAn Start add 20130801
             else if (dateCondition6.isSelected()) {
                ams.setDateCondition(6);
            } else if (dateCondition7.isSelected()) {
                ams.setDateCondition(7);
            } else if (dateCondition8.isSelected()) {
                ams.setDateCondition(8);
            }
            //VTAn end add 20130801

            ams.setDaysAfterVisit(daysAfterVisit.getText().length() > 0 ? Integer.valueOf(daysAfterVisit.getText()) : null);
            ams.setLastDaysAfterVisit(lastDaysAfterVisit.getText().length() > 0 ? Integer.valueOf(lastDaysAfterVisit.getText()) : null);
            ams.setMonthFixedDay(monthFixedDay.getText().length() > 0 ? Integer.valueOf(monthFixedDay.getText()) : null);
            ams.setDaysBeforeBirthday(daysBeforeBirthday.getText().length() > 0 ? Integer.valueOf(daysBeforeBirthday.getText()) : null);
            ams.setDaysBeforeReservation(daysBeforeReservation.getText().length() > 0 ? Integer.valueOf(daysBeforeReservation.getText()) : null);
             //VTAn Start add 20130108
            ams.setDaysCycleBefore(days_cycle_before.getText().length() > 0 ? Integer.valueOf(days_cycle_before.getText()) : null);
            ams.setDaysCycleBeforeFollow(days_cycle_before_follow.getText().length() > 0 ? Integer.valueOf(days_cycle_before_follow.getText()) : null);
            ams.setDaysReCycleBefore(days_re_cycle_before.getText().length() > 0 ? Integer.valueOf(days_re_cycle_before.getText()) : null);
            //VTAn End add 20130108

            if (visitAll.isSelected()) {
                ams.setVisitType(0);
            } else if (visitNew.isSelected()) {
                ams.setVisitType(1);
            } else if (visitFixed.isSelected()) {
                ams.setVisitType(2);
            }

            if (both.isSelected()) {
                ams.setSex(0);
            } else if (male.isSelected()) {
                ams.setSex(1);
            } else if (female.isSelected()) {
                ams.setSex(2);
            }

            if (technicClass.getSelectedIndex() > 0) {
                ams.setTechnicClassId(((MstTechnicClass)technicClass.getSelectedItem()).getTechnicClassID());
            } else {
                ams.setTechnicClassId(null);
            }

            if (itemClass.getSelectedIndex() > 0) {
                ams.setItemClassId(((MstItemClass)itemClass.getSelectedItem()).getItemClassID());
            } else {
                ams.setItemClassId(null);
            }

            ams.setMailTitle(mailTitle.getText());
            ams.setMailBody(mailBody.getText());


            try {
                ams.regist(SystemInfo.getConnection());

            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(201),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
            this.showOpener();

            this.closeWindow();
	}
	
	/**
	 * シンプルマスタ登録画面用FocusTraversalPolicy
	 */
	private class HairCommonMailFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent のあとでフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			if (aComponent.equals(sendHour)) {
			    return sendMinute;
			} else if (aComponent.equals(sendMinute)) {
			    return daysAfterVisit;
			} else if (aComponent.equals(daysAfterVisit)) {
			    return lastDaysAfterVisit;
			} else if (aComponent.equals(lastDaysAfterVisit)) {
			    return daysBeforeBirthday;
			} else if (aComponent.equals(daysBeforeBirthday)) {
			    return daysBeforeReservation;
			} else if (aComponent.equals(daysBeforeReservation)) {
			    return monthFixedDay;
			} else if (aComponent.equals(monthFixedDay)) {
			    return days_cycle_before;
                        //VTAn Start add 20130108    
                        } else if (aComponent.equals(days_cycle_before)) {
			    return days_cycle_before_follow;
                        } else if (aComponent.equals(days_cycle_before_follow)) {
			    return days_re_cycle_before;
                        } else if (aComponent.equals(days_re_cycle_before)) {
			    return female; 
                        //VTAn End  add 20130108   
			} else if (aComponent.equals(female)) {
			    return male;
			} else if (aComponent.equals(male)) {
			    return both;
			} else if (aComponent.equals(both)) {
			    return technicClass;
			} else if (aComponent.equals(technicClass)) {
			    return itemClass;
			} else if (aComponent.equals(itemClass)) {
			    return mailTitle;
			} else if (aComponent.equals(mailTitle)) {
			    return mailBody;
			}
			
			return mailTitle;
		}

		/**
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentBefore(Container aContainer, Component aComponent)
		{
		    if (aComponent.equals(mailTitle)) {
			return mailBody;
		    } else if (aComponent.equals(mailBody)) {
			return mailTitle;
		    }

		    return mailTitle;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
		    return mailTitle;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
		    return mailBody;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
		    return mailTitle;
		}
		
		/**
		 * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
		 * show() または setVisible(true) の呼び出しで一度ウィンドウが表示されると、
		 * 初期化コンポーネントはそれ以降使用されません。
		 * 一度別のウィンドウに移ったフォーカスが再び設定された場合、
		 * または、一度非表示状態になったウィンドウが再び表示された場合は、
		 * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
		 * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
		 * @param window 初期コンポーネントが返されるウィンドウ
		 * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
		 */
		public Component getInitialComponent(Window window)
		{
		    return mailTitle;
		}
	}

	/**
	 * ダイアログを閉じる
	 */
	private void closeWindow() {
                if(this.isDialog()) {
                        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
                } else {
                        this.setVisible(false);
                }
	}

	private void initTechnicClass()
	{
            technicClass.addItem(new MstTechnicClass());

            MstTechnicClasses mtcs = new MstTechnicClasses();

            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                mtcs.load(con);
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            for (MstTechnicClass mtc : mtcs) {
                technicClass.addItem(mtc);
            }
	}

	private void initItemClass()
	{
            itemClass.addItem(new MstItemClass());

            MstItemClasses mtcs = new MstItemClasses();

            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                mtcs.load(con);
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            for (MstItemClass mtc : mtcs) {
                itemClass.addItem(mtc);
            }
	}

	/**
	 * 入力チェックを行う。
	 * @return 入力エラーがなければtrueを返す。
	 */
	private boolean checkInput()
	{
            // 来店○日後
            if (dateCondition0.isSelected()) {
                if (daysAfterVisit.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店後の日数"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    daysAfterVisit.requestFocusInWindow();
                    return false;
                }
            }
            // 最終来店日から○日後
            if (dateCondition1.isSelected()) {
                if (lastDaysAfterVisit.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "最終来店後の日数"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    lastDaysAfterVisit.requestFocusInWindow();
                    return false;
                }
            }
            // 毎月○日
            if (dateCondition5.isSelected()) {
                if (monthFixedDay.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "毎月の指定日"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    monthFixedDay.requestFocusInWindow();
                    return false;
                }
            }

            // 誕生日○日前
            if (dateCondition3.isSelected()) {
                if (daysBeforeBirthday.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "誕生日前の日数"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    daysBeforeBirthday.requestFocusInWindow();
                    return false;
                }
            }

            // 予約日○日前
            if (dateCondition4.isSelected()) {
                if (daysBeforeReservation.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "予約日前の日数"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    daysBeforeReservation.requestFocusInWindow();
                    return false;
                }
            }
            
            //VTAn Start add 20140108
             if (dateCondition6.isSelected()) {
                if (days_cycle_before.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "お誘いメール"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    days_cycle_before.requestFocusInWindow();
                    return false;
                }
            }
             
              if (dateCondition7.isSelected()) {
                if (days_cycle_before_follow.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "お誘いフォローメール"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    days_cycle_before_follow.requestFocusInWindow();
                    return false;
                }
            }
              if (dateCondition8.isSelected()) {
                if (days_re_cycle_before.getText().length() == 0) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "再お誘いメール"),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    days_re_cycle_before.requestFocusInWindow();
                    return false;
                }
            }
             //VTAn End Add 20140108

            return true;
	}

}

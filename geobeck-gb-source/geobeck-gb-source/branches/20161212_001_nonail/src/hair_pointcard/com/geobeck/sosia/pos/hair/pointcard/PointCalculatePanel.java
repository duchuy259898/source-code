/*
 * PointCalculatePanel.java
 *
 * Created on 2008/09/08, 11:04
 */
package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.CheckUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author shiera.delusa
 */
public class PointCalculatePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    /**
     * Class variables
     */
    private PointCalculateData dbAccess;
    private LocalFocusTraversalPolicy ftp;

    /**
     * Creates new form PointCalculatePanel
     */
    public PointCalculatePanel() {
        initComponents();

        ftp = new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());

        this.setSize(800, 720);
        this.setPath("��{�ݒ� >> �|�C���g�J�[�h�ݒ� >> �|�C���g�J�[�h�v�Z���ݒ�");
        this.setTitle("�|�C���g�v�Z���o�^");

        SystemInfo.initGroupShopComponents(cblShop, 2);
        if (cblShop.getItemCount() > 1) {
            displayButton.setVisible(true);
        }
        // enable single row selection only
        //this.firstTimeRateComboBox.setSelectionMode( ListSelectionModel.SINGLE_SELECTION  );
        //this.bdayRateComboBox.setSelectionMode( ListSelectionModel.SINGLE_SELECTION  );

        // TODO
        if (SystemInfo.getDatabase().startsWith("pos_hair_ox2") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
            // �L���b�V���o�b�N�p�|�C���g�ݒ� �\��
        } else {
            // �L���b�V���o�b�N�p�|�C���g�ݒ� ��\��
            cashbackLabel1.setVisible(false);
            cashbackLabel2.setVisible(false);
            cashbackLabel3.setVisible(false);
            cashbackLabel4.setVisible(false);
            cashbackLabel5.setVisible(false);
            cashbackPointField.setVisible(false);
            cashbackRateField.setVisible(false);
        }

        dbAccess = new PointCalculateData();

        addMouseCursorChange();
        setKeyListener();

        loadData();

        changeEnabledItems();
    }

    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(registerButton);
        SystemInfo.addMouseCursorChange(displayButton);
    }

    private void setKeyListener() {
        basicRateField.addKeyListener(SystemInfo.getMoveNextField());
        basicPointField.addKeyListener(SystemInfo.getMoveNextField());
        basicRateField2.addKeyListener(SystemInfo.getMoveNextField());
        basicPointField2.addKeyListener(SystemInfo.getMoveNextField());
        basicRateField3.addKeyListener(SystemInfo.getMoveNextField());
        basicPointField3.addKeyListener(SystemInfo.getMoveNextField());
        basicRateField4.addKeyListener(SystemInfo.getMoveNextField());
        basicPointField4.addKeyListener(SystemInfo.getMoveNextField());
        includeTaxBtn.addKeyListener(SystemInfo.getMoveNextField());
        taxExcludeBtn.addKeyListener(SystemInfo.getMoveNextField());
        techItemBtn.addKeyListener(SystemInfo.getMoveNextField());
        techBtn.addKeyListener(SystemInfo.getMoveNextField());
        itemBtn.addKeyListener(SystemInfo.getMoveNextField());
        visitPointField.addKeyListener(SystemInfo.getMoveNextField());
        firstTimeVisitBtn.addKeyListener(SystemInfo.getMoveNextField());
        notFirstTimeVisitBtn.addKeyListener(SystemInfo.getMoveNextField());
        firstVisitPointBtn.addKeyListener(SystemInfo.getMoveNextField());
        firstTimePointField.addKeyListener(SystemInfo.getMoveNextField());
        firstVisitRateBtn.addKeyListener(SystemInfo.getMoveNextField());
        firstTimeRateComboBox.addKeyListener(SystemInfo.getMoveNextField());
        birthdayBtn.addKeyListener(SystemInfo.getMoveNextField());
        notBirthdayBtn.addKeyListener(SystemInfo.getMoveNextField());
        bdayExactBtn.addKeyListener(SystemInfo.getMoveNextField());
        birthdayMonthBtn.addKeyListener(SystemInfo.getMoveNextField());
        beforeAfterBdayBtn.addKeyListener(SystemInfo.getMoveNextField());
        daysRangeField.addKeyListener(SystemInfo.getMoveNextField());
        bdayPointsBtn.addKeyListener(SystemInfo.getMoveNextField());
        bdayPointField.addKeyListener(SystemInfo.getMoveNextField());
        bdayRateBtn.addKeyListener(SystemInfo.getMoveNextField());
        bdayRateComboBox.addKeyListener(SystemInfo.getMoveNextField());
        cashbackPointField.addKeyListener(SystemInfo.getMoveNextField());
        cashbackRateField.addKeyListener(SystemInfo.getMoveNextField());


        basicRateField.addFocusListener(SystemInfo.getSelectText());
        basicPointField.addFocusListener(SystemInfo.getSelectText());
        basicRateField2.addFocusListener(SystemInfo.getSelectText());
        basicPointField2.addFocusListener(SystemInfo.getSelectText());
        basicRateField3.addFocusListener(SystemInfo.getSelectText());
        basicPointField3.addFocusListener(SystemInfo.getSelectText());
        basicRateField4.addFocusListener(SystemInfo.getSelectText());
        basicPointField4.addFocusListener(SystemInfo.getSelectText());
        includeTaxBtn.addFocusListener(SystemInfo.getSelectText());
        taxExcludeBtn.addFocusListener(SystemInfo.getSelectText());
        techItemBtn.addFocusListener(SystemInfo.getSelectText());
        techBtn.addFocusListener(SystemInfo.getSelectText());
        itemBtn.addFocusListener(SystemInfo.getSelectText());
        visitPointField.addFocusListener(SystemInfo.getSelectText());
        firstTimeVisitBtn.addFocusListener(SystemInfo.getSelectText());
        notFirstTimeVisitBtn.addFocusListener(SystemInfo.getSelectText());
        firstVisitPointBtn.addFocusListener(SystemInfo.getSelectText());
        firstTimePointField.addFocusListener(SystemInfo.getSelectText());
        firstVisitRateBtn.addFocusListener(SystemInfo.getSelectText());
        firstTimeRateComboBox.addFocusListener(SystemInfo.getSelectText());
        birthdayBtn.addFocusListener(SystemInfo.getSelectText());
        notBirthdayBtn.addFocusListener(SystemInfo.getSelectText());
        bdayExactBtn.addFocusListener(SystemInfo.getSelectText());
        birthdayMonthBtn.addFocusListener(SystemInfo.getSelectText());
        beforeAfterBdayBtn.addFocusListener(SystemInfo.getSelectText());
        daysRangeField.addFocusListener(SystemInfo.getSelectText());
        bdayPointsBtn.addFocusListener(SystemInfo.getSelectText());
        bdayPointField.addFocusListener(SystemInfo.getSelectText());
        bdayRateBtn.addFocusListener(SystemInfo.getSelectText());
        bdayRateComboBox.addFocusListener(SystemInfo.getSelectText());
        cashbackPointField.addFocusListener(SystemInfo.getSelectText());
        cashbackRateField.addFocusListener(SystemInfo.getSelectText());
    }

    /**
     * FocusTraversalPolicy���擾����B
     *
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    private void clear() {
        // ��{�|�C���g���
        basicRateField.setText(String.valueOf(""));
        basicPointField.setText(String.valueOf(""));
        basicRateField2.setText(String.valueOf(""));
        basicPointField2.setText(String.valueOf(""));
        basicRateField3.setText(String.valueOf(""));
        basicPointField3.setText(String.valueOf(""));
        basicRateField4.setText(String.valueOf(""));
        basicPointField4.setText(String.valueOf(""));
        includeTaxBtn.setSelected(true);
        taxExcludeBtn.setSelected(false);
        techItemBtn.setSelected(true);
        visitPointField.setText(String.valueOf(""));

        // ���񗈓X���T
        firstTimeVisitBtn.setSelected(false);
        notFirstTimeVisitBtn.setSelected(true);
        firstVisitPointBtn.setSelected(true);
        firstTimePointField.setText("");
        firstVisitRateBtn.setSelected(false);
        firstTimeRateComboBox.setSelectedIndex(-1);

        // �a�������T
        birthdayBtn.setSelected(false);
        notBirthdayBtn.setSelected(true);

        bdayExactBtn.setSelected(true);
        birthdayMonthBtn.setSelected(false);
        beforeAfterBdayBtn.setSelected(false);
        daysRangeField.setText("");

        bdayPointsBtn.setSelected(true);
        bdayPointField.setText("");

        bdayRateBtn.setSelected(false);
        bdayRateComboBox.setSelectedIndex(-1);

        // �L���b�V���o�b�N
        cashbackPointField.setText(String.valueOf(""));
        cashbackRateField.setText(String.valueOf(""));
    }

    private boolean loadData() {
        PointCalculateBean data = dbAccess.getPointCalculateData(getSelectedShopID());

        clear();

        if (data == null) {
            return false;
        }

        // ��{�|�C���g���
        if (data.getBasicRate() != null) {
            basicRateField.setText(String.valueOf(data.getBasicRate()));
        }
        if (data.getBasicPoint() != null) {
            basicPointField.setText(String.valueOf(data.getBasicPoint()));
        }

        if (data.getBasicRate2() != null) {
            basicRateField2.setText(String.valueOf(data.getBasicRate2()));
        }
        if (data.getBasicPoint2() != null) {
            basicPointField2.setText(String.valueOf(data.getBasicPoint2()));
        }

        if (data.getBasicRate3() != null) {
            basicRateField3.setText(String.valueOf(data.getBasicRate3()));
        }
        if (data.getBasicPoint3() != null) {
            basicPointField3.setText(String.valueOf(data.getBasicPoint3()));
        }

        if (data.getBasicRate4() != null) {
            basicRateField4.setText(String.valueOf(data.getBasicRate4()));
        }
        if (data.getBasicPoint4() != null) {
            basicPointField4.setText(String.valueOf(data.getBasicPoint4()));
        }

        if (data.getBasicTaxType() != PointCalculateBean.BASIC_TAX_EXCLUDED) {
            includeTaxBtn.setSelected(true);
            taxExcludeBtn.setSelected(false);
        } else {
            includeTaxBtn.setSelected(false);
            taxExcludeBtn.setSelected(true);
        }

        if (data.getBasicTarget().equals(PointCalculateBean.BASIC_TARGET_TECH_ITEM)) {
            techItemBtn.setSelected(true);
        } else if (data.getBasicTarget().equals(PointCalculateBean.BASIC_TARGET_TECH_ONLY)) {
            techBtn.setSelected(true);
        } else {
            itemBtn.setSelected(true);
        }

        if (data.getBasicTarget2().equals(PointCalculateBean.BASIC_TARGET2_ALL)) {
            customerAllBtn.setSelected(true);
        } else {
            customerSosiaBtn.setSelected(true);
        }

        if (data.getVisitPoint() != null) {
            visitPointField.setText(String.valueOf(data.getVisitPoint()));
        }

        // ���񗈓X���T
        if (data.getFirstTimeEnabled() == PointCalculateBean.FIRST_TIME_VISIT_ENABLED) {
            firstTimeVisitBtn.setSelected(true);
            notFirstTimeVisitBtn.setSelected(false);

            if (data.getFirstTimePoint() != 0) {
                firstVisitPointBtn.setSelected(true);
                firstVisitRateBtn.setSelected(false);
                firstTimePointField.setText(String.valueOf(data.getFirstTimePoint()));
            }
            if (data.getFirstTimeRate() != 0.0) {
                firstVisitPointBtn.setSelected(false);
                firstVisitRateBtn.setSelected(true);
                for (int ii = 0; ii < firstTimeRateComboBox.getItemCount(); ii++) {
                    String str = (String) firstTimeRateComboBox.getItemAt(ii);
                    if (Double.valueOf(str).equals(data.getFirstTimeRate())) {
                        firstTimeRateComboBox.setSelectedIndex(ii);
                        break;
                    }
                }
            }
        } else {
            firstTimeVisitBtn.setSelected(false);
            notFirstTimeVisitBtn.setSelected(true);
        }

        // �a�������T
        if (data.getBirthdayEnabled() == PointCalculateBean.BIRTHDAY_ENABLED) {
            birthdayBtn.setSelected(true);
            notBirthdayBtn.setSelected(false);

            switch (data.getBirthdayCond()) {
                case PointCalculateBean.BIRTHDAY_EXACT_DAY:
                    bdayExactBtn.setSelected(true);
                    daysRangeField.setText("");
                    break;
                case PointCalculateBean.BIRTHDAY_MONTH:
                    birthdayMonthBtn.setSelected(true);
                    break;
                case PointCalculateBean.BIRTHDAY_BEFORE_AFTER:
                    beforeAfterBdayBtn.setSelected(true);
                    daysRangeField.setText(String.valueOf(data.getBirthdayRange()));
                    break;
            }
            if (data.getBirthdayPoint() != 0) {
                bdayPointsBtn.setSelected(true);
                bdayPointField.setText(String.valueOf(data.getBirthdayPoint()));
            }
            if (data.getBirthdayRate() != 0.0) {
                bdayRateBtn.setSelected(true);

                for (int ii = 0; ii < bdayRateComboBox.getItemCount(); ii++) {
                    String str = (String) bdayRateComboBox.getItemAt(ii);
                    if (Double.valueOf(str).equals(data.getBirthdayRate())) {
                        bdayRateComboBox.setSelectedIndex(ii);
                        break;
                    }
                }
            }
        } else {
            birthdayBtn.setSelected(false);
            notBirthdayBtn.setSelected(true);
        }

        // �L���b�V���o�b�N
        if (data.getCashbackPoint() != null) {
            cashbackPointField.setText(String.valueOf(data.getCashbackPoint()));
        }
        if (data.getCashbackRate() != null) {
            cashbackRateField.setText(String.valueOf(data.getCashbackRate()));
        }

        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taxTypeBtnGroup = new javax.swing.ButtonGroup();
        firstTimePointBtnGroup = new javax.swing.ButtonGroup();
        firstTimePointRateBtnGroup = new javax.swing.ButtonGroup();
        birthdayEnableBtnGroup = new javax.swing.ButtonGroup();
        birtdayCondBtnGroup = new javax.swing.ButtonGroup();
        birthdayPointRateBtnGroup = new javax.swing.ButtonGroup();
        techItemBtnGroup = new javax.swing.ButtonGroup();
        customerBtnGroup = new javax.swing.ButtonGroup();
        registerButton = new javax.swing.JButton();
        registerButton.setContentAreaFilled(false);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        basicRateField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicRateField.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        basicPointField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicPointField.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        includeTaxBtn = new javax.swing.JRadioButton();
        taxExcludeBtn = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        firstTimeVisitBtn = new javax.swing.JRadioButton();
        notFirstTimeVisitBtn = new javax.swing.JRadioButton();
        firstVisitPointBtn = new javax.swing.JRadioButton();
        firstVisitRateBtn = new javax.swing.JRadioButton();
        firstTimePointField = new javax.swing.JTextField();
        ((PlainDocument)firstTimePointField.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel8 = new javax.swing.JLabel();
        firstTimeRateComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        birthdayBtn = new javax.swing.JRadioButton();
        notBirthdayBtn = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        bdayExactBtn = new javax.swing.JRadioButton();
        birthdayMonthBtn = new javax.swing.JRadioButton();
        beforeAfterBdayBtn = new javax.swing.JRadioButton();
        daysRangeField = new javax.swing.JFormattedTextField();
        ((PlainDocument)daysRangeField.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        bdayPointsBtn = new javax.swing.JRadioButton();
        bdayRateBtn = new javax.swing.JRadioButton();
        bdayPointField = new javax.swing.JFormattedTextField();
        ((PlainDocument)bdayPointField.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        bdayRateComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        displayButton = new javax.swing.JButton();
        displayButton.setVisible(false);
        displayButton.setContentAreaFilled(false);
        shopLabel = new javax.swing.JLabel();
        cblShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel17 = new javax.swing.JLabel();
        techItemBtn = new javax.swing.JRadioButton();
        techBtn = new javax.swing.JRadioButton();
        itemBtn = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        basicRateField2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicRateField2.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        basicPointField2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicPointField2.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        basicRateField3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicRateField3.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel27 = new javax.swing.JLabel();
        basicPointField3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicPointField3.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        basicPointField4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicPointField4.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel28 = new javax.swing.JLabel();
        basicRateField4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicRateField4.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        visitPointField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitPointField.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        customerAllBtn = new javax.swing.JRadioButton();
        customerSosiaBtn = new javax.swing.JRadioButton();
        cashbackLabel1 = new javax.swing.JLabel();
        cashbackLabel2 = new javax.swing.JLabel();
        cashbackRateField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicRateField4.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        cashbackPointField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)basicPointField4.getDocument()).setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
        cashbackLabel3 = new javax.swing.JLabel();
        cashbackLabel4 = new javax.swing.JLabel();
        cashbackLabel5 = new javax.swing.JLabel();

        registerButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registerButton.setBorder(null);
        registerButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        registerButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registerButtonMouseClicked(evt);
            }
        });
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("���|�C���g�v�Z����");

        jLabel2.setText("���|�C���g�Ҍ���");

        basicRateField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        basicPointField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("�~");

        jLabel4.setText("�|�C���g");

        jLabel5.setText("�Ώۋ��z");

        taxTypeBtnGroup.add(includeTaxBtn);
        includeTaxBtn.setSelected(true);
        includeTaxBtn.setText("�ō�");
        includeTaxBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        includeTaxBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        includeTaxBtn.setOpaque(false);

        taxTypeBtnGroup.add(taxExcludeBtn);
        taxExcludeBtn.setText("�Ŕ�");
        taxExcludeBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        taxExcludeBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        taxExcludeBtn.setOpaque(false);

        jLabel6.setText("���D�ғ��T��");

        jLabel7.setText("�����񗈓X���T");

        firstTimePointBtnGroup.add(firstTimeVisitBtn);
        firstTimeVisitBtn.setSelected(true);
        firstTimeVisitBtn.setText("�L��");
        firstTimeVisitBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        firstTimeVisitBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        firstTimeVisitBtn.setOpaque(false);
        firstTimeVisitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstTimeVisitBtnActionPerformed(evt);
            }
        });

        firstTimePointBtnGroup.add(notFirstTimeVisitBtn);
        notFirstTimeVisitBtn.setText("����");
        notFirstTimeVisitBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        notFirstTimeVisitBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        notFirstTimeVisitBtn.setOpaque(false);
        notFirstTimeVisitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notFirstTimeVisitBtnActionPerformed(evt);
            }
        });

        firstTimePointRateBtnGroup.add(firstVisitPointBtn);
        firstVisitPointBtn.setSelected(true);
        firstVisitPointBtn.setText("�|�C���g���Z");
        firstVisitPointBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        firstVisitPointBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        firstVisitPointBtn.setOpaque(false);
        firstVisitPointBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstVisitPointBtnActionPerformed(evt);
            }
        });

        firstTimePointRateBtnGroup.add(firstVisitRateBtn);
        firstVisitRateBtn.setText("�Ҍ����A�b�v");
        firstVisitRateBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        firstVisitRateBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        firstVisitRateBtn.setOpaque(false);
        firstVisitRateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstVisitRateBtnActionPerformed(evt);
            }
        });

        firstTimePointField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setText("�|�C���g���Z");

        firstTimeRateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        firstTimeRateComboBox.setEnabled(false);

        jLabel9.setText("���a�������T");

        birthdayEnableBtnGroup.add(birthdayBtn);
        birthdayBtn.setSelected(true);
        birthdayBtn.setText("�L��");
        birthdayBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        birthdayBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        birthdayBtn.setOpaque(false);
        birthdayBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                birthdayBtnActionPerformed(evt);
            }
        });

        birthdayEnableBtnGroup.add(notBirthdayBtn);
        notBirthdayBtn.setText("����");
        notBirthdayBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        notBirthdayBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        notBirthdayBtn.setOpaque(false);
        notBirthdayBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notBirthdayBtnActionPerformed(evt);
            }
        });

        jLabel10.setText("�K�p����");

        birtdayCondBtnGroup.add(bdayExactBtn);
        bdayExactBtn.setSelected(true);
        bdayExactBtn.setText("����");
        bdayExactBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bdayExactBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bdayExactBtn.setOpaque(false);
        bdayExactBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bdayExactBtnActionPerformed(evt);
            }
        });

        birtdayCondBtnGroup.add(birthdayMonthBtn);
        birthdayMonthBtn.setText("�a����");
        birthdayMonthBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        birthdayMonthBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        birthdayMonthBtn.setOpaque(false);
        birthdayMonthBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                birthdayMonthBtnActionPerformed(evt);
            }
        });

        birtdayCondBtnGroup.add(beforeAfterBdayBtn);
        beforeAfterBdayBtn.setText("�a�����̑O��");
        beforeAfterBdayBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        beforeAfterBdayBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        beforeAfterBdayBtn.setOpaque(false);
        beforeAfterBdayBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beforeAfterBdayBtnActionPerformed(evt);
            }
        });

        daysRangeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        daysRangeField.setEnabled(false);

        jLabel11.setText("��");

        jLabel12.setText("���T");

        birthdayPointRateBtnGroup.add(bdayPointsBtn);
        bdayPointsBtn.setSelected(true);
        bdayPointsBtn.setText("�|�C���g���Z");
        bdayPointsBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bdayPointsBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bdayPointsBtn.setOpaque(false);
        bdayPointsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bdayPointsBtnActionPerformed(evt);
            }
        });

        birthdayPointRateBtnGroup.add(bdayRateBtn);
        bdayRateBtn.setText("�Ҍ����A�b�v");
        bdayRateBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bdayRateBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bdayRateBtn.setOpaque(false);
        bdayRateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bdayRateBtnActionPerformed(evt);
            }
        });

        bdayPointField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        bdayRateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        bdayRateComboBox.setEnabled(false);

        jLabel13.setText("�� ");

        jLabel14.setText("�{");

        jLabel15.setText("�|�C���g���Z");

        jLabel16.setText("�{");

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

        shopLabel.setText("�X��");

        cblShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel17.setText("���Z�Ώ�");

        techItemBtnGroup.add(techItemBtn);
        techItemBtn.setText("�Z�p�{���i");
        techItemBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        techItemBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        techItemBtn.setOpaque(false);

        techItemBtnGroup.add(techBtn);
        techBtn.setText("�Z�p�̂�");
        techBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        techBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        techBtn.setOpaque(false);

        techItemBtnGroup.add(itemBtn);
        itemBtn.setText("���i�̂�");
        itemBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        itemBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        itemBtn.setOpaque(false);

        jLabel18.setText("����");

        jLabel19.setText("�J�[�h");

        jLabel20.setText("�d�q�}�l�[");

        jLabel21.setText("�����E���̑�");

        basicRateField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel22.setText("�~");

        jLabel23.setText("�� ");

        basicPointField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel24.setText("�|�C���g");

        jLabel25.setText("�|�C���g");

        jLabel26.setText("�� ");

        basicRateField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel27.setText("�~");

        basicPointField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        basicPointField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel28.setText("�~");

        basicRateField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel29.setText("�� ");

        jLabel30.setText("�|�C���g");

        jLabel31.setText("���X�|�C���g");

        visitPointField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel32.setText("�|�C���g");

        jLabel33.setText("���Z�Ώ�2");

        customerBtnGroup.add(customerAllBtn);
        customerAllBtn.setSelected(true);
        customerAllBtn.setText("�S�ڋq");
        customerAllBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        customerAllBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        customerAllBtn.setOpaque(false);

        customerBtnGroup.add(customerSosiaBtn);
        customerSosiaBtn.setText("SOSIA���");
        customerSosiaBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        customerSosiaBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        customerSosiaBtn.setOpaque(false);

        cashbackLabel1.setText("���L���b�V���o�b�N��");

        cashbackLabel2.setText("�g�p�|�C���g");

        cashbackRateField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        cashbackPointField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        cashbackLabel3.setText("�|�C���g");

        cashbackLabel4.setText("�� ");

        cashbackLabel5.setText("�~");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashbackLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(firstVisitPointBtn)
                            .addComponent(firstVisitRateBtn)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bdayExactBtn)
                            .addComponent(birthdayMonthBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bdayPointsBtn)
                                    .addComponent(beforeAfterBdayBtn)
                                    .addComponent(bdayRateBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(bdayRateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bdayPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel15)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(daysRangeField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11))
                                            .addComponent(techBtn))
                                        .addGap(16, 16, 16)
                                        .addComponent(itemBtn))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(birthdayBtn)
                                                    .addComponent(firstTimeRateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(3, 3, 3))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(firstTimeVisitBtn)
                                                .addGap(12, 12, 12)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(notFirstTimeVisitBtn)
                                            .addComponent(notBirthdayBtn)
                                            .addComponent(jLabel14)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(firstTimePointField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cashbackPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cashbackLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cashbackLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cashbackRateField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cashbackLabel5))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(shopLabel)
                        .addGap(18, 18, 18)
                        .addComponent(cblShop, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel31)
                            .addComponent(jLabel33))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(basicRateField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(basicPointField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel24))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(basicRateField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(basicPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(basicRateField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(basicPointField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel25))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(basicRateField4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(basicPointField4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel30))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(visitPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel32))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(includeTaxBtn)
                                        .addGap(15, 15, 15)
                                        .addComponent(taxExcludeBtn))
                                    .addComponent(techItemBtn)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(customerAllBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(customerSosiaBtn))))))
                    .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cashbackLabel1))
                .addGap(191, 191, 191))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shopLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cblShop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicRateField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicPointField2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicRateField2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicPointField3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicRateField3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicPointField4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basicRateField4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(includeTaxBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(taxExcludeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(techItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(techBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerAllBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerSosiaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(visitPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(notFirstTimeVisitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(firstTimeVisitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstVisitPointBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstTimePointField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstVisitRateBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstTimeRateComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(birthdayBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(notBirthdayBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bdayExactBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(birthdayMonthBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(beforeAfterBdayBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(daysRangeField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bdayPointsBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bdayPointField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bdayRateBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bdayRateComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(cashbackLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashbackLabel2)
                    .addComponent(cashbackPointField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashbackLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashbackLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashbackRateField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashbackLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void displayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayButtonActionPerformed
	{//GEN-HEADEREND:event_displayButtonActionPerformed
            loadData();
            changeEnabledItems();
	}//GEN-LAST:event_displayButtonActionPerformed

	private void firstVisitPointBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_firstVisitPointBtnActionPerformed
	{//GEN-HEADEREND:event_firstVisitPointBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_firstVisitPointBtnActionPerformed

	private void firstVisitRateBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_firstVisitRateBtnActionPerformed
	{//GEN-HEADEREND:event_firstVisitRateBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_firstVisitRateBtnActionPerformed

	private void bdayExactBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bdayExactBtnActionPerformed
	{//GEN-HEADEREND:event_bdayExactBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_bdayExactBtnActionPerformed

	private void birthdayMonthBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_birthdayMonthBtnActionPerformed
	{//GEN-HEADEREND:event_birthdayMonthBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_birthdayMonthBtnActionPerformed

	private void bdayPointsBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bdayPointsBtnActionPerformed
	{//GEN-HEADEREND:event_bdayPointsBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_bdayPointsBtnActionPerformed

	private void beforeAfterBdayBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_beforeAfterBdayBtnActionPerformed
	{//GEN-HEADEREND:event_beforeAfterBdayBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_beforeAfterBdayBtnActionPerformed

	private void bdayRateBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bdayRateBtnActionPerformed
	{//GEN-HEADEREND:event_bdayRateBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_bdayRateBtnActionPerformed

	private void birthdayBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_birthdayBtnActionPerformed
	{//GEN-HEADEREND:event_birthdayBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_birthdayBtnActionPerformed

	private void notFirstTimeVisitBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_notFirstTimeVisitBtnActionPerformed
	{//GEN-HEADEREND:event_notFirstTimeVisitBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_notFirstTimeVisitBtnActionPerformed

	private void notBirthdayBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_notBirthdayBtnActionPerformed
	{//GEN-HEADEREND:event_notBirthdayBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_notBirthdayBtnActionPerformed

	private void firstTimeVisitBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_firstTimeVisitBtnActionPerformed
	{//GEN-HEADEREND:event_firstTimeVisitBtnActionPerformed
            changeEnabledItems();
	}//GEN-LAST:event_firstTimeVisitBtnActionPerformed

    private void changeEnabledItems() {
        if (firstTimeVisitBtn.isSelected()) {
            this.firstTimePointField.setEnabled(true);
            this.firstTimeRateComboBox.setEnabled(true);
            this.firstVisitPointBtn.setEnabled(true);
            this.firstVisitRateBtn.setEnabled(true);
            // gloridel add
            this.firstTimeRateComboBox.setEnabled(this.firstVisitRateBtn.isSelected());
            this.firstTimePointField.setEnabled(this.firstVisitPointBtn.isSelected());

            this.jLabel8.setEnabled(true);
            this.jLabel14.setEnabled(true);
        } else {
            this.firstTimePointField.setEnabled(false);
            this.firstTimeRateComboBox.setEnabled(false);
            this.firstVisitPointBtn.setEnabled(false);
            this.firstVisitRateBtn.setEnabled(false);

            // gloridel add
            this.firstTimeRateComboBox.setEnabled(false);
            this.firstTimePointField.setEnabled(false);

            this.jLabel8.setEnabled(false);
            this.jLabel14.setEnabled(false);
        }

        if (birthdayBtn.isSelected()) {
            this.jLabel10.setEnabled(true);
            this.jLabel11.setEnabled(true);
            this.jLabel12.setEnabled(true);
            this.jLabel15.setEnabled(true);
            this.jLabel16.setEnabled(true);
            this.bdayExactBtn.setEnabled(true);
            this.birthdayMonthBtn.setEnabled(true);
            this.beforeAfterBdayBtn.setEnabled(true);
            this.daysRangeField.setEnabled(true);
            this.bdayPointsBtn.setEnabled(true);
            this.bdayPointField.setEnabled(true);
            this.bdayRateBtn.setEnabled(true);
            this.bdayRateComboBox.setEnabled(true);

            //gloridel add
            this.daysRangeField.setEnabled(this.beforeAfterBdayBtn.isSelected());
            this.bdayPointField.setEnabled(this.bdayPointsBtn.isSelected());
            this.bdayRateComboBox.setEnabled(this.bdayRateBtn.isSelected());
        } else {
            this.jLabel10.setEnabled(false);
            this.jLabel11.setEnabled(false);
            this.jLabel12.setEnabled(false);
            this.jLabel15.setEnabled(false);
            this.jLabel16.setEnabled(false);
            this.bdayExactBtn.setEnabled(false);
            this.birthdayMonthBtn.setEnabled(false);
            this.beforeAfterBdayBtn.setEnabled(false);
            this.daysRangeField.setEnabled(false);
            this.bdayPointsBtn.setEnabled(false);
            this.bdayPointField.setEnabled(false);
            this.bdayRateBtn.setEnabled(false);
            this.bdayRateComboBox.setEnabled(false);
        }
    }
    // �G���[���
    private final int FAILED_REQUIRED_YEN = 1;
    private final int FAILED_REQUIRED_POINT = 2;
    private final int FAILED_REQUIRED_FIRSTPOINT = 3;
    private final int FAILED_REQUIRED_FIRSTRATE = 4;
    private final int FAILED_REQUIRED_BIRTHDAYS = 5;
    private final int FAILED_REQUIRED_BIRTHPOINT = 6;
    private final int FAILED_REQUIRED_BIRTHRATE = 7;
    private final int FAILED_RANGE_BIRTHDAYS = 8;
    private final int FAILED_REQUIRED_CASHBACK_POINT = 10;
    private final int FAILED_REQUIRED_CASHBACK_YEN = 11;

    /**
     * Displays a message depending on the message type
     *
     * @param msgType its value could be one of the following constants:
     * FAILED_INSERT, FAILED_UPDATE, FAILED_DELETE, MISSING_DISPLAY_SEQUENCE,
     * MISSING_DATE, INVALID_DATE_INPUT
     */
    private void displayMessage(int msgType) {
        switch (msgType) {
            case FAILED_REQUIRED_YEN:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�|�C���g�Ҍ����i�~�j����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case FAILED_REQUIRED_POINT:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�|�C���g�Ҍ����i�|�C���g�j����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case FAILED_REQUIRED_FIRSTPOINT:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "���񗈓X���T�̉��Z�|�C���g����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case FAILED_REQUIRED_FIRSTRATE:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "���񗈓X���T�̊Ҍ�����I�����Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case FAILED_REQUIRED_BIRTHDAYS:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�a�������T���Ԃ���͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case FAILED_REQUIRED_BIRTHPOINT:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�a�������T�̉��Z�|�C���g����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case FAILED_REQUIRED_BIRTHRATE:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�a�������T�̊Ҍ�����I�����Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case FAILED_RANGE_BIRTHDAYS:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�a�������T���Ԃ� 1�`90 �͈̔͂Œl����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case FAILED_REQUIRED_CASHBACK_POINT:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�L���b�V���o�b�N�|�C���g����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case FAILED_REQUIRED_CASHBACK_YEN:
                MessageDialog.showMessageDialog(this.getParentFrame(),
                        "�L���b�V���o�b�N���z����͂��Ă�������",
                        JOptionPane.ERROR_MESSAGE);
                break;

            default:
                break;
        }
    }

    /**
     * �X�V�O�`�F�b�N
     *
     * @return
     */
    private boolean checkBeforeRegister() {
        String strVerify;

        // �K�{�`�F�b�N

        // �Ҍ����i�~�j
        if (basicRateField.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_YEN);
            return false;
        }
        if (basicRateField2.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_YEN);
            return false;
        }
        if (basicRateField3.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_YEN);
            return false;
        }
        if (basicRateField4.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_YEN);
            return false;
        }

        // �Ҍ����i�|�C���g�j
        if (basicPointField.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_POINT);
            return false;

        }
        if (basicPointField2.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_POINT);
            return false;

        }
        if (basicPointField3.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_POINT);
            return false;

        }
        if (basicPointField4.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_POINT);
            return false;
        }

        if (visitPointField.getText().trim().length() == 0) {
            displayMessage(FAILED_REQUIRED_POINT);
            return false;

        }

        if (firstTimeVisitBtn.isSelected() == true) {
            // ���񗈓X�i�|�C���g�j
            if (firstVisitPointBtn.isSelected() == true) {
                if (firstTimePointField.getText().trim().length() == 0) {
                    displayMessage(FAILED_REQUIRED_FIRSTPOINT);
                    return false;
                }
            }

            // ���񗈓X�i�{���j
            if (firstVisitRateBtn.isSelected() == true) {
                if (firstTimeRateComboBox.getSelectedIndex() < 0) {
                    displayMessage(FAILED_REQUIRED_FIRSTRATE);
                    return false;
                }
            }
        }

        if (birthdayBtn.isSelected() == true) {
            // �a�����T�i�����j
            if (beforeAfterBdayBtn.isSelected() == true) {
                strVerify = daysRangeField.getText().trim();
                // �K�{
                if (strVerify.length() == 0) {
                    displayMessage(FAILED_REQUIRED_BIRTHDAYS);
                    return false;
                }

                // �͈�
                if (!CheckUtil.checkRange(Integer.parseInt(strVerify), 1, 90)) {
                    displayMessage(FAILED_RANGE_BIRTHDAYS);
                    return false;
                }

            }

            // �a�����T�i�|�C���g�j
            if (bdayPointsBtn.isSelected() == true) {
                if (bdayPointField.getText().trim().length() == 0) {
                    displayMessage(FAILED_REQUIRED_BIRTHPOINT);
                    return false;
                }
            }

            // �a�����T�i�{���j
            if (bdayRateBtn.isSelected() == true) {
                if (bdayRateComboBox.getSelectedIndex() < 0) {
                    displayMessage(FAILED_REQUIRED_BIRTHRATE);
                    return false;
                }
            }
        }

        // �L���b�V���o�b�N
        if (SystemInfo.getDatabase().startsWith("pos_hair_ox2") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
            if (cashbackPointField.getText().trim().length() == 0) {
                displayMessage(FAILED_REQUIRED_CASHBACK_POINT);
                return false;
            }
            if (cashbackRateField.getText().trim().length() == 0) {
                displayMessage(FAILED_REQUIRED_CASHBACK_YEN);
                return false;
            }
        }

        return true;
    }

    private void registerButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_registerButtonMouseClicked
    {//GEN-HEADEREND:event_registerButtonMouseClicked
        if (!checkBeforeRegister()) {
            return;
        }

        PointCalculateBean bean = new PointCalculateBean();
        bean.setShopid(getSelectedShopID());
        bean.setBasicRate(Integer.parseInt(this.basicRateField.getText()));
        bean.setBasicPoint(Integer.parseInt(this.basicPointField.getText()));

        bean.setBasicRate2(Integer.parseInt(this.basicRateField2.getText()));
        bean.setBasicPoint2(Integer.parseInt(this.basicPointField2.getText()));

        bean.setBasicRate3(Integer.parseInt(this.basicRateField3.getText()));
        bean.setBasicPoint3(Integer.parseInt(this.basicPointField3.getText()));

        bean.setBasicRate4(Integer.parseInt(this.basicRateField4.getText()));
        bean.setBasicPoint4(Integer.parseInt(this.basicPointField4.getText()));

        // �Ώۋ��z
        if (this.includeTaxBtn.isSelected() == true) {
            bean.setBasicTaxType(PointCalculateBean.BASIC_TAX_INCLUDED);
        } else {
            bean.setBasicTaxType(PointCalculateBean.BASIC_TAX_EXCLUDED);
        }

        // ���Z�Ώ�
        if (this.techItemBtn.isSelected()) {
            bean.setBasicTarget(PointCalculateBean.BASIC_TARGET_TECH_ITEM);
        } else if (this.techBtn.isSelected()) {
            bean.setBasicTarget(PointCalculateBean.BASIC_TARGET_TECH_ONLY);
        } else {
            bean.setBasicTarget(PointCalculateBean.BASIC_TARGET_ITEM_ONLY);
        }

        // ���Z�Ώ�2
        if (this.customerAllBtn.isSelected()) {
            bean.setBasicTarget2(PointCalculateBean.BASIC_TARGET2_ALL);
        } else {
            bean.setBasicTarget2(PointCalculateBean.BASIC_TARGET2_SOSIA);
        }

        bean.setVisitPoint(Integer.parseInt(this.visitPointField.getText()));

        // ���񗈓X���T
        if (this.firstTimeVisitBtn.isSelected() == true) {
            bean.setFirstTimeEnabled(PointCalculateBean.FIRST_TIME_VISIT_ENABLED);

            if (this.firstTimePointField.isEnabled()) {
                bean.setFirstTimePoint(Integer.parseInt(this.firstTimePointField.getText()));
            }
            if (this.firstTimeRateComboBox.isEnabled()) {
                bean.setFirstTimeRate(Double.valueOf((String) this.firstTimeRateComboBox.getSelectedItem()));
            }
        } else {
            bean.setFirstTimeEnabled(PointCalculateBean.FIRST_TIME_VISIT_DISABLED);
        }

        // �a�������T
        if (this.birthdayBtn.isSelected() == true) {
            bean.setBirthdayEnabled(PointCalculateBean.BIRTHDAY_ENABLED);
            if (this.bdayExactBtn.isSelected() == true) {
                bean.setBirthdayCond(PointCalculateBean.BIRTHDAY_EXACT_DAY);
            } else if (this.birthdayMonthBtn.isSelected() == true) {
                bean.setBirthdayCond(PointCalculateBean.BIRTHDAY_MONTH);
            } else {
                bean.setBirthdayCond(PointCalculateBean.BIRTHDAY_BEFORE_AFTER);
            }

            if (this.daysRangeField.isEnabled()) {
                bean.setBirthdayRange(Integer.parseInt(this.daysRangeField.getText()));
            }
            if (this.bdayPointField.isEnabled()) {
                bean.setBirthdayPoint(Integer.parseInt(this.bdayPointField.getText()));
            }
            if (this.bdayRateComboBox.isEnabled()) {
                bean.setBirthdayRate(Double.valueOf((String) this.bdayRateComboBox.getSelectedItem()));
            }
        } else {
            bean.setBirthdayEnabled(PointCalculateBean.BIRTHDAY_DISABLED);
        }

        // �L���b�V���o�b�N
        if (SystemInfo.getDatabase().startsWith("pos_hair_ox2") || SystemInfo.getDatabase().startsWith("pos_hair_dev")) {
            bean.setCashbackPoint(Integer.parseInt(this.cashbackPointField.getText()));
            bean.setCashbackRate(Integer.parseInt(this.cashbackRateField.getText()));
        }
        boolean bRet = false;
        if (dbAccess.getPointCalculateData(bean.getShopid()) == null) {
            bRet = dbAccess.insertPointCalculation(bean);
        } else {
            bRet = dbAccess.updatePointCalculation(bean);
        }
        if (bRet == true) {
            // �o�^���܂���
            MessageDialog.showMessageDialog(this.getParentFrame(),
                    MessageUtil.getMessage(201),
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            // �\�����ʃG���[
            MessageDialog.showMessageDialog(this.getParentFrame(),
                    MessageUtil.getMessage(1099),
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_registerButtonMouseClicked

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registerButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.swing.JFormattedTextFieldEx basicPointField;
    private com.geobeck.swing.JFormattedTextFieldEx basicPointField2;
    private com.geobeck.swing.JFormattedTextFieldEx basicPointField3;
    private com.geobeck.swing.JFormattedTextFieldEx basicPointField4;
    private com.geobeck.swing.JFormattedTextFieldEx basicRateField;
    private com.geobeck.swing.JFormattedTextFieldEx basicRateField2;
    private com.geobeck.swing.JFormattedTextFieldEx basicRateField3;
    private com.geobeck.swing.JFormattedTextFieldEx basicRateField4;
    private javax.swing.JRadioButton bdayExactBtn;
    private javax.swing.JFormattedTextField bdayPointField;
    private javax.swing.JRadioButton bdayPointsBtn;
    private javax.swing.JRadioButton bdayRateBtn;
    private javax.swing.JComboBox bdayRateComboBox;
    private javax.swing.JRadioButton beforeAfterBdayBtn;
    private javax.swing.ButtonGroup birtdayCondBtnGroup;
    private javax.swing.JRadioButton birthdayBtn;
    private javax.swing.ButtonGroup birthdayEnableBtnGroup;
    private javax.swing.JRadioButton birthdayMonthBtn;
    private javax.swing.ButtonGroup birthdayPointRateBtnGroup;
    private javax.swing.JLabel cashbackLabel1;
    private javax.swing.JLabel cashbackLabel2;
    private javax.swing.JLabel cashbackLabel3;
    private javax.swing.JLabel cashbackLabel4;
    private javax.swing.JLabel cashbackLabel5;
    private com.geobeck.swing.JFormattedTextFieldEx cashbackPointField;
    private com.geobeck.swing.JFormattedTextFieldEx cashbackRateField;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cblShop;
    private javax.swing.JRadioButton customerAllBtn;
    private javax.swing.ButtonGroup customerBtnGroup;
    private javax.swing.JRadioButton customerSosiaBtn;
    private javax.swing.JFormattedTextField daysRangeField;
    private javax.swing.JButton displayButton;
    private javax.swing.ButtonGroup firstTimePointBtnGroup;
    private javax.swing.JTextField firstTimePointField;
    private javax.swing.ButtonGroup firstTimePointRateBtnGroup;
    private javax.swing.JComboBox firstTimeRateComboBox;
    private javax.swing.JRadioButton firstTimeVisitBtn;
    private javax.swing.JRadioButton firstVisitPointBtn;
    private javax.swing.JRadioButton firstVisitRateBtn;
    private javax.swing.JRadioButton includeTaxBtn;
    private javax.swing.JRadioButton itemBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton notBirthdayBtn;
    private javax.swing.JRadioButton notFirstTimeVisitBtn;
    private javax.swing.JButton registerButton;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JRadioButton taxExcludeBtn;
    private javax.swing.ButtonGroup taxTypeBtnGroup;
    private javax.swing.JRadioButton techBtn;
    private javax.swing.JRadioButton techItemBtn;
    private javax.swing.ButtonGroup techItemBtnGroup;
    private com.geobeck.swing.JFormattedTextFieldEx visitPointField;
    // End of variables declaration//GEN-END:variables

    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
            extends FocusTraversalPolicy {

        ArrayList<Component> controls = new ArrayList<Component>();

        public LocalFocusTraversalPolicy() {
            controls.add(cblShop);
            controls.add(basicRateField);
            controls.add(basicPointField);
            controls.add(basicRateField2);
            controls.add(basicPointField2);
            controls.add(basicRateField3);
            controls.add(basicPointField3);
            controls.add(basicRateField4);
            controls.add(basicPointField4);
            controls.add(includeTaxBtn);
            controls.add(taxExcludeBtn);
            controls.add(techItemBtn);
            controls.add(techBtn);
            controls.add(itemBtn);
            controls.add(customerAllBtn);
            controls.add(customerSosiaBtn);
            controls.add(visitPointField);
            controls.add(firstTimeVisitBtn);
            controls.add(notFirstTimeVisitBtn);
            controls.add(firstVisitPointBtn);
            controls.add(firstTimePointField);
            controls.add(firstVisitRateBtn);
            controls.add(firstTimeRateComboBox);
            controls.add(birthdayBtn);
            controls.add(notBirthdayBtn);
            controls.add(bdayExactBtn);
            controls.add(birthdayMonthBtn);
            controls.add(beforeAfterBdayBtn);
            controls.add(daysRangeField);
            controls.add(bdayPointsBtn);
            controls.add(bdayPointField);
            controls.add(bdayRateBtn);
            controls.add(bdayRateComboBox);
            controls.add(cashbackPointField);
            controls.add(cashbackRateField);
            controls.add(cblShop);     // �Ō�ɐ擪���ēx�o�^
        }

        ;
        
        /**
         * aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
         * !!�擪�ƍŌ�̃R���g���[���������Ɏd�l�s�ɂȂ��ʂł̓o�O��̂Œ���
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for (Component co : controls) {
                if (find) {
                    if (co.isEnabled()) {
                        return co;
                    }
                } else if (aComponent.equals(co)) {
                    find = true;
                }
            }
            return null;
        }

        /**
         * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
         * !!�擪�ƍŌ�̃R���g���[���������Ɏd�l�s�ɂȂ��ʂł̓o�O��̂Œ���
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for (int ii = controls.size(); ii > 0; ii--) {
                Component co = controls.get(ii - 1);
                if (find) {
                    if (co.isEnabled()) {
                        return co;
                    }
                } else if (aComponent.equals(co)) {
                    find = true;
                }
            }
            return null;
        }

        /**
         * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
         */
        public Component getFirstComponent(Container aContainer) {
            return getDefaultComponent(aContainer);
        }

        /**
         * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
         */
        public Component getLastComponent(Container aContainer) {
            return getComponentBefore(aContainer, controls.get(0));
        }

        /**
         * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
         */
        public Component getDefaultComponent(Container aContainer) {
            for (Component co : controls) {
                if (co.isEnabled()) {
                    return co;
                }
            }
            return controls.get(0);
        }

        /**
         * �E�B���h�E���ŏ��ɕ\�����ꂽ�Ƃ��Ƀt�H�[�J�X���ݒ肳���R���|�[�l���g��Ԃ��܂��B
         */
        public Component getInitialComponent(Window window) {
            for (Component co : controls) {
                if (co.isEnabled()) {
                    return co;
                }
            }
            return controls.get(0);
        }
    }

    /**
     * �I������Ă���X�܂��擾����B
     *
     * @return �I������Ă���X��
     */
    private MstShop getSelectedShop() {
        if (0 <= cblShop.getSelectedIndex()) {
            return (MstShop) cblShop.getSelectedItem();
        } else {
            return null;
        }
    }

    /**
     * �I������Ă���X�܂�ID���擾����B
     *
     * @return �I������Ă���X�܂�ID
     */
    private Integer getSelectedShopID() {
        MstShop ms = this.getSelectedShop();

        if (ms != null) {
            return ms.getShopID();
        } else {
            return 0;
        }
    }
}

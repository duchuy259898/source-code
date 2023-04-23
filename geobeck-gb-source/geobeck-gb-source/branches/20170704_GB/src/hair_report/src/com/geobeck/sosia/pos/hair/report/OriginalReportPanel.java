/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.hair.report.beans.OriginalReportBean;
import com.geobeck.sosia.pos.hair.report.logic.OriginalReport01Logic;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.*;
import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.text.*;
import com.geobeck.sosia.pos.hair.report.logic.OriginalReport02Logic;
import com.geobeck.sosia.pos.hair.report.logic.OriginalReport03Logic;
import com.geobeck.sosia.pos.hair.report.logic.OriginalReportCommonLogic;
import com.geobeck.sosia.pos.util.MessageUtil;
import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import jp.co.flatsoft.fscomponent.FSCalenderCombo;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author user
 */
public class OriginalReportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    public static final String[] PERIODS = {
            "1����", "45��", "2����", "3����", "4����", "5����", "6����"};
    
    public static final String[] ORDER_ITEMS = {
            "R2�i�V�K�ė��j", "R�i�����ė��j", "TO���i���Ԓ��ߗ��j"
            , "�q��", "�w����", "���R�~���i�ǂ��j", "���R�~���i�����j"
            , "��������", "��������", "�P��", "����"};

    abstract class StateChangeComponents implements Serializable {
        
        private List<Component> components = null;
        
        public StateChangeComponents() {
        }
        
        /**
         * @return the components
         */
        public List<Component> getComponents() {
            return components;
        }

        /**
         * @param components the components to set
         */
        public void setComponents(Component... components) {
            this.components = this.asList(components);
        }
        
        public boolean addComponent(Component component) {
            return this.components.add(component);
        }
        
        protected List<Component> asList(Component[] args) {
            List<Component> list = new ArrayList<>(args.length);
            list.addAll(Arrays.asList(args));
            return list;
        }
    }
    
    class EnabledComponents extends StateChangeComponents {
        
        public EnabledComponents() {
            this(jPanel1.getComponents());
            super.addComponent(rdoTargetPeriod01);
            super.addComponent(rdoTargetPeriod02);
        }
        
        public EnabledComponents(Component... components) {
            super();
            if (components != null) {
                super.setComponents(components);
            }
        }
    }
    
    class DisabledComponents extends StateChangeComponents {
        
        public DisabledComponents() {
            this(jPanel1.getComponents());
        }
        
        public DisabledComponents(Component... components) {
            super();
            if (components != null) {
                super.setComponents(components);
            }
        }
    }
    
    /**
     * Creates new form OriginalReportPanel
     */
    public OriginalReportPanel() {
        initComponents();
        // addMouseCursorChange();
        this.setSize(720, 500);
        this.setPath("���͒��[�����J�X�^�����[");
        this.setTitle("�I���W�i�����[�o��");
//        SystemInfo.initGroupShopComponents(cmbShop, 3);
        cmbShop.addItem(SystemInfo.getGroup());
        SystemInfo.getGroup().addGroupDataToJComboBox(cmbShop, 3);
        if (!SystemInfo.isGroup()) {
            cmbShop.setSelectedItem(SystemInfo.getCurrentShop());
        }
        cmbStaff.addItem(new MstStaff());
        SystemInfo.initStaffComponent(cmbStaff);
        cmbStaff.setSelectedIndex(0);
        
        // �ΏۊJ�n�N�ݒ�
        Calendar cdr = Calendar.getInstance();
        cdr.setTime(new Date());
        int nowYear = cdr.get(Calendar.YEAR);
        this.initYearCombo(cmbStartYear, nowYear);
        
        // �ΏۏI���N�ݒ�
        this.initYearCombo(cmbEndYear, nowYear);
        
        // �ΏۊJ�n�`�I�����ݒ�
        initTargetCmbSelection();
        
        this.initPeriodCombo(cmbTargetPeriod, 0);
        this.initOrderCombo(cmbOrderNo, 0);

    }
    
    private void initTargetCmbSelection() {
        cmbStartYear.setSelectedIndex(0);
        cmbEndYear.setSelectedIndex(0);
        
        Calendar cdr = Calendar.getInstance();
        cdr.setTime(new Date());
        int nowMonth = cdr.get(Calendar.MONTH);
        cmbStartMonth.setSelectedIndex(nowMonth);
        cmbEndMonth.setSelectedIndex(getEndMonth());
    }
    
    private int getEndMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        int endMonth;
        if (rdoMonthTransition.isSelected()) {
            // �������ڂ̏ꍇ�͊J�n�N��12�����I�����ƂȂ�B
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            endMonth = calendar.get(Calendar.MONTH);
        } else {
            // ���͂��ׂē������I�����ƂȂ�B
            endMonth = calendar.get(Calendar.MONTH);
        }
        return endMonth;
    }
    
    private void refreshComponents(EnabledComponents enableds, DisabledComponents disableds) {
        if (enableds != null && 0 < enableds.getComponents().size()) {
            for (Component child : enableds.getComponents()) {
                if (child instanceof JButton
                        || child instanceof JComboBox
                        || child instanceof JRadioButton) {
                    child.setEnabled(true);
                }
            }
        }
        this.initGrpAgainCalWhet();
        if (disableds != null && 0 < disableds.getComponents().size()) {
            for (Component component : disableds.getComponents()) {
                if (component instanceof JPanel) {
                    this.refreshComponents(null
                            , new DisabledComponents(((JPanel) component).getComponents()));
                } else {
                    component.setEnabled(false);
                }
            }
        }
    }
    
    private void initPeriodCombo(final JComboBox cmb, int index) {
        cmb.removeAllItems();
        for (String period : PERIODS) {
            cmb.addItem(period);
        }
        cmb.setSelectedIndex(index);
    }
    
    private void initOrderCombo(final JComboBox cmb, int index) {
        cmb.removeAllItems();
        for (String period : ORDER_ITEMS) {
            cmb.addItem(period);
        }
        cmb.setSelectedIndex(index);
    }
    
    private void initYearCombo(final JComboBox cmb, int nowYear) {
        cmb.removeAllItems();

        int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            cmb.addItem(String.valueOf(y - i));
        }
        cmb.setSelectedItem(nowYear);
        cmb.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ((JTextField) cmb.getEditor().getEditorComponent()).selectAll();
            }
        });
    }
    
    private void initGrpAgainCalWhet() {
        cmbTargetPeriod.setEnabled(rdoTargetPeriod01.isSelected());
        cmbTargetPeriodStart.setEnabled(rdoTargetPeriod02.isSelected());
        cmbTargetPeriodEnd.setEnabled(rdoTargetPeriod02.isSelected());
    }
    
    private boolean validTargetDate(JComboBox cmbYear, JComboBox cmbMonth) {
        Calendar cal = Calendar.getInstance();
        Date targetDate = null;
	try{
	    cal.set(Calendar.YEAR, Integer.parseInt(cmbYear.getSelectedItem().toString()));
            cal.set(Calendar.MONTH, Integer.parseInt(cmbMonth.getSelectedItem().toString()));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            targetDate = cal.getTime();
	} catch(Exception e){
	    MessageDialog.showMessageDialog(this,
		MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
		this.getTitle(),
		JOptionPane.ERROR_MESSAGE);
	    cmbYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
	    return true;                
	}
        if (targetDate == null) {
            MessageDialog.showMessageDialog(this,
		MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
		this.getTitle(),
		JOptionPane.ERROR_MESSAGE);
	    cmbYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
	    return true;
        }
        return false;
    }
    
    private boolean validTargetPeriodDate(FSCalenderCombo cmbCalender) {
        if (cmbCalender.isEnabled() && cmbCalender.getDate() == null) {
            MessageDialog.showMessageDialog(this,
		MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�ė��Z�o����"),
		this.getTitle(),
		JOptionPane.ERROR_MESSAGE);
	    return true;
        }
        return false;
    }
    
    private void changeYearMonth() {
        try {
            Integer startMonth = Integer.parseInt(this.cmbStartMonth.getSelectedItem().toString());
            Integer startYear = Integer.parseInt(this.cmbStartYear.getSelectedItem().toString());
            Integer endMonth = startMonth-1;
            Integer endYear = startYear+1;
            if (endMonth == 0) {
                endMonth = 12;
                endYear = endYear -1;
            }
            cmbEndYear.setSelectedItem(endYear);
            cmbEndMonth.setSelectedIndex(endMonth-1);
        } catch (Exception e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpExpReportType = new javax.swing.ButtonGroup();
        grpExpReportKind = new javax.swing.ButtonGroup();
        grpTaxKind = new javax.swing.ButtonGroup();
        grpAgainCalWhet = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        ������ExpReportType = new javax.swing.JLabel();
        ������ExpReportKind = new javax.swing.JLabel();
        ������ShopName = new javax.swing.JLabel();
        ������StaffName = new javax.swing.JLabel();
        ������TargetWhet = new javax.swing.JLabel();
        ������AgainCalWhet = new javax.swing.JLabel();
        ������OrderNo = new javax.swing.JLabel();
        rdoRanking = new javax.swing.JRadioButton();
        rdoDesignRanking = new javax.swing.JRadioButton();
        lblTaxKind = new javax.swing.JLabel();
        rdoMonthTransition = new javax.swing.JRadioButton();
        rdoLandingFocus = new javax.swing.JRadioButton();
        rdoShop = new javax.swing.JRadioButton();
        rdoStaff = new javax.swing.JRadioButton();
        cmbShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        cmbStaff = new javax.swing.JComboBox();
        cmbOrderNo = new javax.swing.JComboBox();
        rdoTaxIn = new javax.swing.JRadioButton();
        rdoTaxOut = new javax.swing.JRadioButton();
        cmbStartYear = new javax.swing.JComboBox();
        ((PlainDocument)((JTextComponent)
            cmbStartYear.getEditor().getEditorComponent()).getDocument()).setDocumentFilter(
        new CustomFilter(4, CustomFilter.NUMBER));
    jLabel5 = new javax.swing.JLabel();
    cmbStartMonth = new javax.swing.JComboBox();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    cmbEndYear = new javax.swing.JComboBox();
    ((PlainDocument)((JTextComponent)
        cmbEndYear.getEditor().getEditorComponent()).getDocument()).setDocumentFilter(
    new CustomFilter(4, CustomFilter.NUMBER));
    jLabel10 = new javax.swing.JLabel();
    cmbEndMonth = new javax.swing.JComboBox();
    jLabel11 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    rdoTargetPeriod01 = new javax.swing.JRadioButton();
    cmbTargetPeriod = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
    rdoTargetPeriod02 = new javax.swing.JRadioButton();
    cmbTargetPeriodStart = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
    cmbTargetPeriodEnd = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
    lbl01 = new javax.swing.JLabel();
    btnExcelReport2 = new javax.swing.JButton();
    btnPurposeSetting = new javax.swing.JButton();

    jPanel1.setOpaque(false);

    ������ExpReportType.setText("�o�͒��[");

    ������ExpReportKind.setText("�o�͋敪");

    ������ShopName.setText("�X��");

    ������StaffName.setText("�S����");

    ������TargetWhet.setText("�Ώۊ���");

    ������AgainCalWhet.setText("�ė��Z�o����");

    ������OrderNo.setText("�\����");

    grpExpReportType.add(rdoRanking);
    rdoRanking.setSelected(true);
    rdoRanking.setText("�����L���O");
    rdoRanking.setOpaque(false);
    rdoRanking.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpExpReportTypeChanged(evt);
        }
    });

    grpExpReportType.add(rdoDesignRanking);
    rdoDesignRanking.setText("�f�U�C�������L���O");
    rdoDesignRanking.setOpaque(false);
    rdoDesignRanking.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpExpReportTypeChanged(evt);
        }
    });

    lblTaxKind.setText("�ŋ敪");

    grpExpReportType.add(rdoMonthTransition);
    rdoMonthTransition.setText("��������");
    rdoMonthTransition.setOpaque(false);
    rdoMonthTransition.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpExpReportTypeChanged(evt);
        }
    });

    grpExpReportType.add(rdoLandingFocus);
    rdoLandingFocus.setText("���n�\�z");
    rdoLandingFocus.setOpaque(false);
    rdoLandingFocus.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpExpReportTypeChanged(evt);
        }
    });

    grpExpReportKind.add(rdoShop);
    rdoShop.setSelected(true);
    rdoShop.setText("�X��");
    rdoShop.setOpaque(false);
    rdoShop.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpExpReportKindChanged(evt);
        }
    });

    grpExpReportKind.add(rdoStaff);
    rdoStaff.setText("�S����");
    rdoStaff.setOpaque(false);
    rdoStaff.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpExpReportKindChanged(evt);
        }
    });

    cmbShop.setEnabled(false);

    cmbStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbStaff.setEnabled(false);

    cmbOrderNo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    cmbOrderNo.setSelectedIndex(-1);

    grpTaxKind.add(rdoTaxIn);
    rdoTaxIn.setSelected(true);
    rdoTaxIn.setText("�ō�");
    rdoTaxIn.setOpaque(false);

    grpTaxKind.add(rdoTaxOut);
    rdoTaxOut.setText("�Ŕ�");
    rdoTaxOut.setOpaque(false);

    cmbStartYear.setEditable(true);
    cmbStartYear.setMaximumRowCount(12);
    cmbStartYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbStartYear.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            cmbStartYearPropertyChange(evt);
        }
    });

    jLabel5.setText("�N");

    cmbStartMonth.setMaximumRowCount(12);
    cmbStartMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
    cmbStartMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbStartMonth.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            cmbStartMonthPropertyChange(evt);
        }
    });

    jLabel6.setText("��");

    jLabel7.setText("�`");

    cmbEndYear.setEditable(true);
    cmbEndYear.setMaximumRowCount(12);
    cmbEndYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

    jLabel10.setText("�N");

    cmbEndMonth.setMaximumRowCount(12);
    cmbEndMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
    cmbEndMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbEndMonth.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            cmbEndMonthPropertyChange(evt);
        }
    });

    jLabel11.setText("��");

    jPanel2.setOpaque(false);

    grpAgainCalWhet.add(rdoTargetPeriod01);
    rdoTargetPeriod01.setSelected(true);
    rdoTargetPeriod01.setOpaque(false);
    rdoTargetPeriod01.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpAgainCalWhetChanged(evt);
        }
    });

    //shop.addItem(this.myShop);

    grpAgainCalWhet.add(rdoTargetPeriod02);
    rdoTargetPeriod02.setOpaque(false);
    rdoTargetPeriod02.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            grpAgainCalWhetChanged(evt);
        }
    });

    cmbTargetPeriodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbTargetPeriodStart.setForeground(java.awt.Color.white);
    cmbTargetPeriodStart.setEnabled(false);
    cmbTargetPeriodStart.setMaximumSize(new java.awt.Dimension(65, 20));
    cmbTargetPeriodStart.setMinimumSize(new java.awt.Dimension(65, 20));
    cmbTargetPeriodStart.setPreferredSize(new java.awt.Dimension(85, 20));
    cmbTargetPeriodStart.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            cmbTargetPeriodStartFocusGained(evt);
        }
    });

    cmbTargetPeriodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    cmbTargetPeriodEnd.setEnabled(false);
    cmbTargetPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            cmbTargetPeriodEndFocusGained(evt);
        }
    });

    lbl01.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl01.setText("�`");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(rdoTargetPeriod01)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cmbTargetPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(rdoTargetPeriod02)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(lbl01, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(rdoTargetPeriod01)
                .addComponent(cmbTargetPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(rdoTargetPeriod02)
                .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl01, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(������ExpReportType)
                .addComponent(������ExpReportKind)
                .addComponent(������ShopName)
                .addComponent(������StaffName)
                .addComponent(������TargetWhet)
                .addComponent(������AgainCalWhet)
                .addComponent(������OrderNo)
                .addComponent(lblTaxKind))
            .addGap(25, 25, 25)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cmbStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(rdoShop)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(rdoStaff))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(rdoRanking)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(rdoDesignRanking)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(rdoMonthTransition)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(rdoLandingFocus))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(rdoTaxIn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(rdoTaxOut))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(cmbStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbEndMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel11)))
            .addContainerGap(79, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rdoRanking)
                .addComponent(rdoDesignRanking)
                .addComponent(rdoMonthTransition)
                .addComponent(rdoLandingFocus)
                .addComponent(������ExpReportType))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(������ExpReportKind)
                .addComponent(rdoShop)
                .addComponent(rdoStaff))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(������ShopName)
                .addComponent(cmbShop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(������StaffName)
                .addComponent(cmbStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(������TargetWhet)
                .addComponent(cmbStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel5)
                .addComponent(cmbStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6)
                .addComponent(jLabel7)
                .addComponent(cmbEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10)
                .addComponent(cmbEndMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(������AgainCalWhet)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addComponent(������OrderNo))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cmbOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblTaxKind)
                .addComponent(rdoTaxIn)
                .addComponent(rdoTaxOut))
            .addContainerGap(10, Short.MAX_VALUE))
    );

    btnExcelReport2.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
    btnExcelReport2.setBorderPainted(false);
    btnExcelReport2.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
    btnExcelReport2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnExcelReport2ActionPerformed(evt);
        }
    });

    btnPurposeSetting.setIcon(SystemInfo.getImageIcon("/button/master/target_off.jpg"));
    btnPurposeSetting.setBorderPainted(false);
    btnPurposeSetting.setMaximumSize(new java.awt.Dimension(30, 9));
    btnPurposeSetting.setMinimumSize(new java.awt.Dimension(30, 9));
    btnPurposeSetting.setPreferredSize(new java.awt.Dimension(30, 9));
    btnPurposeSetting.setPressedIcon(SystemInfo.getImageIcon("/button/master/target_on.jpg"));
    btnPurposeSetting.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnPurposeSettingActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnExcelReport2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPurposeSetting, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnPurposeSetting, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btnExcelReport2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    }// </editor-fold>//GEN-END:initComponents

    private Date[] getAgainPeriod(int startYear, int startMonth) {
        
//        int startyear = Integer.parseInt(this.cmbStartYear.getSelectedItem().toString());
//        int startmonth = Integer.parseInt(this.cmbStartMonth.getSelectedItem().toString());
        
        // �ė����Ԃ�Ώۊ��Ԃ��狁�߂�
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, startYear);
        cal.set(Calendar.MONTH, startMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        java.util.Date targetDate = cal.getTime();

        int reappearanceCount = getReappearanceSpan();
        /*********************************/
        // �������O�̌���
        /*********************************/
	Calendar calculationStart = Calendar.getInstance();
	calculationStart.setTime(targetDate);
	if (reappearanceCount == 45) {
            // 45���ė���2�����O��15��
            calculationStart.add(Calendar.MONTH, -2);
            calculationStart.set(Calendar.DAY_OF_MONTH, 15);
	} else {
            // ����ȊO�́������O��1��
            calculationStart.add(Calendar.MONTH, (reappearanceCount * -1));
            calculationStart.set(Calendar.DAY_OF_MONTH, 1);
	}
        
        // �ė��Ώۊ���
	// �J�n���͏W�v�J�n���Ɠ���
	Calendar targetStart = Calendar.getInstance();
	targetStart.setTime(calculationStart.getTime());

	Calendar targetEnd = Calendar.getInstance();
	targetEnd.setTime(targetStart.getTime());
	if (reappearanceCount == 45) {
	    // 45���ė��̏I���́A������14��
	    targetEnd.add(Calendar.MONTH, 1);
	    targetEnd.set(Calendar.DAY_OF_MONTH, 14);
	} else {
	    // �I���͊J�n���̌���
	    targetEnd.add(Calendar.MONTH, 1);
	    targetEnd.add(Calendar.DAY_OF_MONTH, -1);
	}

        // ���Ԑݒ�������ɂ���
        if (reappearanceCount != 45) {
            this.resetSpan(targetStart, targetEnd);
        }

        Date[] fromTo = new Date[2];
        fromTo[0] = targetStart.getTime();
        fromTo[1] = targetEnd.getTime();
        return fromTo;
    }
    
    private void resetSpan(Calendar calStart, Calendar calEnd) {

        int cutoffDay = 0;

        if (cmbShop.getSelectedItem() instanceof MstShop) {
            MstShop ms = (MstShop)cmbShop.getSelectedItem();
            cutoffDay = ms.getCutoffDay();
        } else {
            cutoffDay = SystemInfo.getAccountSetting().getCutoffDay();
        }

        if (cutoffDay == 31) return;

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(calEnd.getTime());

        if (calTo.getActualMaximum(Calendar.DATE) <= cutoffDay) {
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DATE));
        } else {
            calTo.set(Calendar.DAY_OF_MONTH, cutoffDay);
        }

        Calendar calFrom = (Calendar)calTo.clone();
        calFrom.add(Calendar.MONTH, -1);
        calFrom.add(Calendar.DAY_OF_MONTH, 1);

        calStart.setTime(calFrom.getTime());
        calEnd.setTime(calTo.getTime());
    }
    
    /**
     * �ė��Z�o���Ԃ����߂�
     */
    private int getReappearanceSpan() {
        String textSpan = this.cmbTargetPeriod.getSelectedItem().toString();

        if ("1����".equals(textSpan)) {
                return 1;
        } else if ("45��".equals(textSpan)) {
                return 45;
        } else if ("2����".equals(textSpan)) {
                return 2;
        } else if ("3����".equals(textSpan)) {
                return 3;
        } else if ("4����".equals(textSpan)) {
                return 4;
        } else if ("5����".equals(textSpan)) {
                return 5;
        } else {
                return 6;
        }
    }
    
    private void btnExcelReport2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelReport2ActionPerformed
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));              
            boolean isError = this.validTargetDate(cmbStartYear, cmbStartMonth);
            isError = this.validTargetDate(cmbEndYear, cmbEndMonth) || isError;
            if (isError) {
                return;
            }

            if (this.validTargetPeriodDate(cmbTargetPeriodStart)
                    || this.validTargetPeriodDate(cmbTargetPeriodEnd)) {
                return;
            }

            OriginalReportBean bean = null;
            OriginalReportCommonLogic logic = null;

            if (rdoRanking.isSelected()) {
                // �����L���O�̏ꍇ
                bean = new OriginalReportBean(OriginalReportBean.ExpReportType.RANKING);
                logic = new OriginalReport01Logic(bean);
            } else if (rdoDesignRanking.isSelected()) {
                // �f�U�C�������L���O�̏ꍇ
                bean = new OriginalReportBean(OriginalReportBean.ExpReportType.DESIGN_RANKING);
                logic = new OriginalReport03Logic(bean);
            } else if (rdoMonthTransition.isSelected()) {
                // �������ڂ̏ꍇ
                bean = new OriginalReportBean(OriginalReportBean.ExpReportType.MONTH_TRANSITION);
                logic = new OriginalReport01Logic(bean);
            } else if (rdoLandingFocus.isSelected()) {
                // ���n�\�z�̏ꍇ
                bean = new OriginalReportBean(OriginalReportBean.ExpReportType.LANDING_FOCUS);
                logic = new OriginalReport02Logic(bean);
            }

            if (bean != null && logic != null) {
                bean.setShop(cmbShop.getSelectedItem());
                bean.setStaff(cmbStaff.getSelectedItem());
                bean.setTargetDateFrom(
                        Integer.parseInt(this.cmbStartYear.getSelectedItem().toString())
                        , Integer.parseInt(this.cmbStartMonth.getSelectedItem().toString())
                        , null);

                // ���n�\�z�͑Ώۊ��Ԃ��P�����Œ�Ƃ��邽�߁A�I�������J�n���̌������Ƃ���B
                if (rdoLandingFocus.isSelected()) {
                    bean.setTargetDateTo(
                            Integer.parseInt(this.cmbStartYear.getSelectedItem().toString())
                            , Integer.parseInt(this.cmbStartMonth.getSelectedItem().toString())
                            , null);
                } else {
                    bean.setTargetDateTo(
                            Integer.parseInt(this.cmbEndYear.getSelectedItem().toString())
                            , Integer.parseInt(this.cmbEndMonth.getSelectedItem().toString())
                            , null);
                }

                bean.setShopFlag(rdoShop.isSelected());
                bean.setStaffFlag(rdoStaff.isSelected());
                bean.setTargetPeriod01Flag(rdoTargetPeriod01.isSelected());
                bean.setTargetPeriod02Flag(rdoTargetPeriod02.isSelected());
                //�ŋ敪
                bean.setTaxFlag(rdoTaxIn.isSelected());
                //�\��������
                bean.setOrder(cmbOrderNo.getSelectedItem().toString());
                //�\����
                bean.setOrderIndex(cmbOrderNo.getSelectedIndex());

                // �ė��Z�o���Ԃ̐ݒ�
                int startYear = Integer.parseInt(this.cmbStartYear.getSelectedItem().toString());
                int startMonth = Integer.parseInt(this.cmbStartMonth.getSelectedItem().toString());
                int endYear = Integer.parseInt(this.cmbEndYear.getSelectedItem().toString());
                int endMonth = Integer.parseInt(this.cmbEndMonth.getSelectedItem().toString());

                Calendar startYm = Calendar.getInstance();
                startYm.set(Calendar.DATE, 1);
                startYm.set(Calendar.YEAR, startYear);
                startYm.set(Calendar.MONTH, startMonth - 1);

                Calendar endYm = Calendar.getInstance();
                endYm.set(Calendar.DATE, 1);
                endYm.set(Calendar.YEAR, endYear);
                endYm.set(Calendar.MONTH, endMonth - 1);
                List<String> targetYmList = this.getTargetYmList(startYm, endYm);
                Map<String, List<Date>> periodDateMap = new HashMap<>();
                for(String targetYm : targetYmList) {
                    Date[] fromTo;
                    if (this.rdoTargetPeriod01.isSelected()) {
                        if (this.rdoMonthTransition.isSelected()) {
                            // �ė��Z�o���ԂŁ~�����w������Ă���ꍇ�́A�Ώی��ɑ΂��ā~���O����1�����̊Ԃ��ė��Z�o���ԂƂ���B
                            // ���������ڂ̂݁B�����L���O�́~�����w��ł��ΏۊJ�n������Ƃ���1���Ԃ݂̂�ΏۂƂ���B
                            fromTo = getAgainPeriod(Integer.valueOf(targetYm.substring(0, 4)), Integer.valueOf(targetYm.substring(targetYm.length() - 2)));
                        } else {
                            // �����L���O�̍ė��Z�o���Ԃ͑ΏۊJ�n������Ƃ������ԌŒ�Ƃ���B
                            fromTo = getAgainPeriod(Integer.valueOf(cmbStartYear.getSelectedItem().toString()), 
                                                    Integer.valueOf(cmbStartMonth.getSelectedItem().toString()));
                        }
                    } else {
                        // �ė��Z�o���Ԃ�fromto�ݒ�����Ă���ꍇ�́A�Ώی��ɑ΂���ė��Z�o���Ԃ͌Œ�B
                        fromTo = new Date[]{this.cmbTargetPeriodStart.getDate(), this.cmbTargetPeriodEnd.getDate()};
                    }
                    periodDateMap.put(targetYm, Arrays.asList(fromTo));
                }
                bean.setPeriodYmdMap(periodDateMap);
                
                //�ė��Z�o���ԃ��x��
                bean.setCalculationPeriod(this.cmbTargetPeriod.getSelectedItem().toString());
                bean.setPeriodDateFrom(cmbTargetPeriodStart.getDate());
                bean.setPeriodDateTo(cmbTargetPeriodEnd.getDate());
                
                logic.print();
            }
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnExcelReport2ActionPerformed
    private void btnPurposeSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurposeSettingActionPerformed
        TargetSettingPanel tsp = new TargetSettingPanel(this);
        parentFrame.changeContents(tsp);
    }//GEN-LAST:event_btnPurposeSettingActionPerformed

    private List<String> getTargetYmList(Calendar startYm, Calendar endYm) {
        List<String> targetYmList = new ArrayList<>();
        Calendar currentYm = (Calendar)startYm.clone();
        
        // startYm�`endYm�܂ł̔N�����X�g���쐬����B
        while (currentYm.get(Calendar.YEAR) < endYm.get(Calendar.YEAR) ||
               (currentYm.get(Calendar.YEAR) == endYm.get(Calendar.YEAR) &&
                currentYm.get(Calendar.MONTH) <= endYm.get(Calendar.MONTH))) {
            targetYmList.add(String.valueOf(currentYm.get(Calendar.YEAR)) + "/" + StringUtils.leftPad(String.valueOf(currentYm.get(Calendar.MONTH) + 1), 2, '0'));
            currentYm.add(Calendar.MONTH, 1);
        }
        
        return targetYmList;
    }
    
    private void cmbStartMonthPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbStartMonthPropertyChange
        if (this.validTargetDate(cmbStartYear, cmbStartMonth)) {
            return;
        }
    }//GEN-LAST:event_cmbStartMonthPropertyChange

    private void cmbStartYearPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbStartYearPropertyChange
        
    }//GEN-LAST:event_cmbStartYearPropertyChange

    private void cmbTargetPeriodEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndFocusGained
        cmbTargetPeriodEnd.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndFocusGained

    private void cmbTargetPeriodStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartFocusGained
        cmbTargetPeriodStart.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartFocusGained

    private void grpExpReportTypeChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_grpExpReportTypeChanged
        if (rdoRanking.isSelected()) {
            // �����L���O�̏ꍇ
            this.refreshComponents(
                    new EnabledComponents(), new DisabledComponents(cmbStaff));
            cmbStaff.setSelectedIndex(0);
        } else if (rdoDesignRanking.isSelected()) {
            // �f�U�C�������L���O�̏ꍇ
            this.refreshComponents(
                    new EnabledComponents(), new DisabledComponents(cmbOrderNo, jPanel2));
        } else if (rdoMonthTransition.isSelected()) {
            // �������ڂ̏ꍇ
            this.refreshComponents(
                    new EnabledComponents()
                    , new DisabledComponents(cmbOrderNo, rdoShop.isSelected() ? cmbStaff : cmbShop));
            //�X��ID�ɑ�����X�^�b�t�R���{���Đ���
            Integer id = 0;
            if (cmbShop.getSelectedItem() instanceof MstShop) {
                MstShop ms = (MstShop)cmbShop.getSelectedItem();
                id = ms.getShopID();
            }
            setStaffComponent(cmbStaff, id);
            cmbStaff.setSelectedIndex(0);
//            cmbShop.setSelectedIndex(0);
//            cmbShop.setSelectedItem(SystemInfo.getCurrentShop());
        } else if (rdoLandingFocus.isSelected()) {
            // ���n�\�z�̏ꍇ
            rdoShop.setSelected(true);
            this.refreshComponents(
                    new EnabledComponents(), new DisabledComponents(rdoStaff
                            , cmbStaff, cmbEndYear, cmbEndMonth, cmbOrderNo));
            cmbStaff.setSelectedIndex(0);
            cmbShop.setSelectedIndex(0);
            cmbShop.setSelectedItem(SystemInfo.getCurrentShop());
        }
        
        initTargetCmbSelection();
    }//GEN-LAST:event_grpExpReportTypeChanged

    private void grpExpReportKindChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_grpExpReportKindChanged
        if (rdoMonthTransition.isSelected()) {
            // �������ڂ̏ꍇ
            this.refreshComponents(
                    new EnabledComponents(rdoShop.isSelected() ? cmbShop : cmbStaff)
                    , new DisabledComponents(rdoShop.isSelected() ? cmbStaff : cmbShop));
            //�X��ID�ɑ�����X�^�b�t�R���{���Đ���
            Integer id = 0;
            if (cmbShop.getSelectedItem() instanceof MstShop) {
                MstShop ms = (MstShop)cmbShop.getSelectedItem();
                id = ms.getShopID();
            }
            setStaffComponent(cmbStaff, id);
            cmbStaff.setSelectedIndex(0);
//            cmbShop.setSelectedIndex(0);
//            cmbShop.setSelectedItem(SystemInfo.getCurrentShop());
        }
    }//GEN-LAST:event_grpExpReportKindChanged

    private static void setStaffComponent(JComboBox combobox, Integer shopId)
    {
        try
        {
            combobox.removeAllItems();
            combobox.addItem(new MstStaff());
            //�O���[�v
            if(shopId == 0)
            {
                    MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(),
                                    combobox, SystemInfo.getGroup().getShopIDListAll());
            }
            //�X��
            else
            {
                    MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(),
                                    combobox, shopId);
            }
        }
        catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage());
        }
    }
    
    private void grpAgainCalWhetChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_grpAgainCalWhetChanged
        this.initGrpAgainCalWhet();
    }//GEN-LAST:event_grpAgainCalWhetChanged

    private void cmbEndMonthPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbEndMonthPropertyChange
        this.validTargetDate(cmbEndYear, cmbEndMonth);
    }//GEN-LAST:event_cmbEndMonthPropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcelReport2;
    private javax.swing.JButton btnPurposeSetting;
    private javax.swing.JComboBox cmbEndMonth;
    private javax.swing.JComboBox cmbEndYear;
    private javax.swing.JComboBox cmbOrderNo;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbShop;
    private javax.swing.JComboBox cmbStaff;
    private javax.swing.JComboBox cmbStartMonth;
    private javax.swing.JComboBox cmbStartYear;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cmbTargetPeriod;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEnd;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStart;
    private javax.swing.ButtonGroup grpAgainCalWhet;
    private javax.swing.ButtonGroup grpExpReportKind;
    private javax.swing.ButtonGroup grpExpReportType;
    private javax.swing.ButtonGroup grpTaxKind;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbl01;
    private javax.swing.JLabel lblTaxKind;
    private javax.swing.JRadioButton rdoDesignRanking;
    private javax.swing.JRadioButton rdoLandingFocus;
    private javax.swing.JRadioButton rdoMonthTransition;
    private javax.swing.JRadioButton rdoRanking;
    private javax.swing.JRadioButton rdoShop;
    private javax.swing.JRadioButton rdoStaff;
    private javax.swing.JRadioButton rdoTargetPeriod01;
    private javax.swing.JRadioButton rdoTargetPeriod02;
    private javax.swing.JRadioButton rdoTaxIn;
    private javax.swing.JRadioButton rdoTaxOut;
    private javax.swing.JLabel ������AgainCalWhet;
    private javax.swing.JLabel ������ExpReportKind;
    private javax.swing.JLabel ������ExpReportType;
    private javax.swing.JLabel ������OrderNo;
    private javax.swing.JLabel ������ShopName;
    private javax.swing.JLabel ������StaffName;
    private javax.swing.JLabel ������TargetWhet;
    // End of variables declaration//GEN-END:variables
}

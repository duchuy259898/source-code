/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.master.company.MstShopCategory;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

/**
 *
 * @author ttmloan
 */
public class SettingControlPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private ArrayList<MstRankItemGroup> itemGroupList = new ArrayList<MstRankItemGroup>();
    private MstRankAdvancedSetting currentSetting = new MstRankAdvancedSetting();
    private String strItemGroupID = "";

    /**
     * Creates new customizer SettingControlPanel
     */
    public SettingControlPanel() {
        initComponents();
        //this.setSize(790, 436);
        this.setSize(850, 436);
        this.setTitle("条件設定");
        this.addMouseCursorChange();
        this.init();
        btnRegist.setEnabled(true);
        item_group_idTable.getColumnModel().getColumn(0).setMaxWidth(0);
        item_group_idTable.getColumnModel().getColumn(0).setMinWidth(0);
    }

    /**
     * 初期化処理を行う。
     */
    private void init() {
        if (itemSaledGroupTable.getCellEditor() != null) {
            itemSaledGroupTable.getCellEditor().stopCellEditing();
        }
        this.loadShopCategoryData();
        this.loadRankAdvancedSettingData();
        this.loadItemSalesGroup();
    }

    /**
     * Load data from mst_rank_item_group
     */
    private void loadItemSalesGroup() {
        SwingUtil.clearTable(itemSaledGroupTable);
        SwingUtil.clearTable(item_group_idTable);
        DefaultTableModel model = (DefaultTableModel) itemSaledGroupTable.getModel();
        DefaultTableModel modelItemGroupID = (DefaultTableModel) item_group_idTable.getModel();
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getItemSalesGroupSQL());

            while (rs.next()) {
                MstRankItemGroup element = new MstRankItemGroup();
                element.setData(rs);
                itemGroupList.add(element);
                boolean isChecked = false;
                if (currentSetting.getItemSalesGroup() != null && currentSetting.getItemSalesGroup().contains(element.getItemGroupId().toString())) {
                    isChecked = true;
                    if (currentSetting.getItemGroupId() != null && currentSetting.getItemGroupId().contains(element.getItemGroupId().toString())) {
                        Object[] rowData = {element.getItemGroupId(),element.getItemGroupName(), getItemSalesGroupCheckbox(true)};
                        modelItemGroupID.addRow(rowData);
                    }
                    else
                    {
                        Object[] rowData = {element.getItemGroupId(),element.getItemGroupName(), getItemSalesGroupCheckbox(false)};
                        modelItemGroupID.addRow(rowData);
                    }
                }
                Object[] rowData = {element.getItemGroupName(), getItemSalesGroupCheckbox(isChecked)};
                model.addRow(rowData);
            }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * get checkbox
     */
    private JCheckBox getItemSalesGroupCheckbox(boolean isCheck) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(isCheck);
        checkBox.setOpaque(false);
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSalesGroupCheckboxActionPerformed(evt);
            }
        });
        return checkBox;
    }

    /**
     * Process event on checkbox
     * @param evt 
     */
    private void itemSalesGroupCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
        int rowSeleted = item_group_idTable.getEditingRow();
        if(rowSeleted < 0)
        {
            SwingUtil.clearTable(item_group_idTable);
            DefaultTableModel modelItemGroupID = (DefaultTableModel) item_group_idTable.getModel();
            for (int i = 0; i < itemSaledGroupTable.getRowCount(); i++) {
              JCheckBox cb = (JCheckBox)itemSaledGroupTable.getValueAt(i, 1);
                if (cb.isSelected()) {
                    if (currentSetting.getItemGroupId() != null && currentSetting.getItemGroupId().contains(itemGroupList.get(i).getItemGroupId().toString())) {
                        Object[] rowData = {itemGroupList.get(i).getItemGroupId(),itemGroupList.get(i).getItemGroupName(), getItemSalesGroupCheckbox(true)};
                        modelItemGroupID.addRow(rowData);
                    }
                    else
                    {
                        if(strItemGroupID.contains(itemGroupList.get(i).getItemGroupId().toString()))
                        {
                            Object[] rowData = {itemGroupList.get(i).getItemGroupId(),itemGroupList.get(i).getItemGroupName(), getItemSalesGroupCheckbox(true)};
                            modelItemGroupID.addRow(rowData);
                        }
                        else{
                            Object[] rowData = {itemGroupList.get(i).getItemGroupId(),itemGroupList.get(i).getItemGroupName(), getItemSalesGroupCheckbox(false)};
                            modelItemGroupID.addRow(rowData);
                        }
                    }
                }
            }
        }
        else
        {
            if(item_group_idTable.isEditing())
            {
                item_group_idTable.getCellEditor().stopCellEditing();
            }
        }
        strItemGroupID = "";
        for (int i = 0; i < item_group_idTable.getRowCount(); i++) {
          JCheckBox cb = (JCheckBox)item_group_idTable.getValueAt(i, 2);
            if (cb.isSelected()) {
                strItemGroupID += "," + item_group_idTable.getValueAt(i, 0).toString();
            }
        }
        strItemGroupID = strItemGroupID.replaceFirst(",", "");
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getItemSalesGroupSQL() {
        //IVS_LVTu start edit 2015/03/02 new request ( MASHU様独自ランク設計書_20150224)
        return " select DISTINCT mrig.* from mst_rank_item_group mrig "
                + " inner join mst_rank_item_detail mrid on mrid.item_group_id = mrig.item_group_id "
                + " where mrig.delete_date is null "
                + " order by mrig.item_group_id";
        //IVS_LVTu end edit 2015/03/02 new request ( MASHU様独自ランク設計書_20150224)
    }

    /**
     * Set data of mst_rank_advanced_setting
     */
    private void setCurrentDataSetting() {
        unitPrice5.setText(currentSetting.getUnitPrice5() != null ? currentSetting.getUnitPrice5().toString() : "");
        unitPrice4.setText(currentSetting.getUnitPrice4() != null ? currentSetting.getUnitPrice4().toString() : "");
        unitPrice3.setText(currentSetting.getUnitPrice3() != null ? currentSetting.getUnitPrice3().toString() : "");
        unitPrice2.setText(currentSetting.getUnitPrice2() != null ? currentSetting.getUnitPrice2().toString() : "");
        unitPrice1.setText(currentSetting.getUnitPrice1() != null ? currentSetting.getUnitPrice1().toString() : "");
        technicValue5.setText(currentSetting.getTechnicValue5() != null ? currentSetting.getTechnicValue5().toString() : "");
        technicValue4.setText(currentSetting.getTechnicValue4() != null ? currentSetting.getTechnicValue4().toString() : "");
        technicValue3.setText(currentSetting.getTechnicValue3() != null ? currentSetting.getTechnicValue3().toString() : "");
        technicValue2.setText(currentSetting.getTechnicValue2() != null ? currentSetting.getTechnicValue2().toString() : "");
        technicValue1.setText(currentSetting.getTechnicValue1() != null ? currentSetting.getTechnicValue1().toString() : "");
        itemValue5.setText(currentSetting.getItemValue5() != null ? currentSetting.getItemValue5().toString() : "");
        itemValue4.setText(currentSetting.getItemValue4() != null ? currentSetting.getItemValue4().toString() : "");
        itemValue3.setText(currentSetting.getItemValue3() != null ? currentSetting.getItemValue3().toString() : "");
        itemValue2.setText(currentSetting.getItemValue2() != null ? currentSetting.getItemValue2().toString() : "");
        itemValue1.setText(currentSetting.getItemValue1() != null ? currentSetting.getItemValue1().toString() : "");
        visitCycle5.setText(currentSetting.getVisitCycle5() != null ? currentSetting.getVisitCycle5().toString() : "");
        visitCycle4.setText(currentSetting.getVisitCycle4() != null ? currentSetting.getVisitCycle4().toString() : "");
        visitCycle3.setText(currentSetting.getVisitCycle3() != null ? currentSetting.getVisitCycle3().toString() : "");
        visitCycle2.setText(currentSetting.getVisitCycle2() != null ? currentSetting.getVisitCycle2().toString() : "");
        visitCycle1.setText(currentSetting.getVisitCycle1() != null ? currentSetting.getVisitCycle1().toString() : "");
        //IVS_LVTu start add 2015/04/17 New request #36344
        checkSalesFlag.setSelected((currentSetting.getSales_flag() != null && currentSetting.getSales_flag() != 0) ? true : false);
        //IVS_LVTu end add 2015/04/17 New request #36344
        if (technicValue5.getText().length() > 0) {
            technicValue41.setText(String.valueOf(Long.parseLong(technicValue5.getText()) - 1));
        } else {
            technicValue41.setText("");
        }

        if (technicValue4.getText().length() > 0) {
            technicValue31.setText(String.valueOf(Long.parseLong(technicValue4.getText()) - 1));
        } else {
            technicValue31.setText("");
        }

        if (technicValue3.getText().length() > 0) {
            technicValue21.setText(String.valueOf(Long.parseLong(technicValue3.getText()) - 1));
        } else {
            technicValue21.setText("");
        }

        if (technicValue2.getText().length() > 0) {
            technicValue11.setText(String.valueOf(Long.parseLong(technicValue2.getText()) - 1));
        } else {
            technicValue11.setText("");
        }

        if (itemValue5.getText().length() > 0) {
            itemValue41.setText(String.valueOf(Long.parseLong(itemValue5.getText()) - 1));
        } else {
            itemValue41.setText("");
        }

        if (itemValue4.getText().length() > 0) {
            itemValue31.setText(String.valueOf(Long.parseLong(itemValue4.getText()) - 1));
        } else {
            itemValue31.setText("");
        }

        if (itemValue3.getText().length() > 0) {
            itemValue21.setText(String.valueOf(Long.parseLong(itemValue3.getText()) - 1));
        } else {
            itemValue21.setText("");
        }

        if (itemValue2.getText().length() > 0) {
            itemValue11.setText(String.valueOf(Long.parseLong(itemValue2.getText()) - 1));
        } else {
            itemValue11.setText("");
        }

        if (visitCycle5.getText().length() > 0) {
            visitCycle41.setText(String.valueOf(Long.parseLong(visitCycle5.getText()) + 1));
        } else {
            visitCycle41.setText("");
        }
        if (visitCycle4.getText().length() > 0) {
            visitCycle31.setText(String.valueOf(Long.parseLong(visitCycle4.getText()) + 1));
        } else {
            visitCycle31.setText("");
        }
        if (visitCycle3.getText().length() > 0) {
            visitCycle21.setText(String.valueOf(Long.parseLong(visitCycle3.getText()) + 1));
        } else {
            visitCycle21.setText("");
        }
        if (visitCycle2.getText().length() > 0) {
            visitCycle1.setText(String.valueOf(Long.parseLong(visitCycle2.getText()) + 1));
        } else {
            visitCycle1.setText("");
        }
    }

    /**
     * Load data from mst_rank_advanced_setting
     */
    private void loadRankAdvancedSettingData() {
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            Integer shopCategoryId = ((MstShopCategory) categoryCombobox.getSelectedItem()).getShopCategoryId();
            ResultSetWrapper rs = con.executeQuery(this.getRankAdvancedSettingSQL(shopCategoryId));

            if (rs.next()) {
                currentSetting.setData(rs);
                setCurrentDataSetting();
            } else {
                currentSetting = new MstRankAdvancedSetting();
                setCurrentDataSetting();
            }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getRankAdvancedSettingSQL(Integer shopCategoryId) {
        return "select * from mst_rank_advanced_setting"
                + " where shop_category_id = " + shopCategoryId
                + " and delete_date is null";

    }

    /**
     * データをデータベースから読み込む。
     */
    private void loadShopCategoryData() {
        MstShopCategory firstElement = new MstShopCategory();
        firstElement.setShopCategoryId(0);
        categoryCombobox.addItem(firstElement);
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getShopCategorySQL());

            while (rs.next()) {
                MstShopCategory element = new MstShopCategory();
                element.setData(rs);
                categoryCombobox.addItem(element);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getShopCategorySQL() {
        return "select * from mst_shop_category "
                + "where delete_date is null "
                + "order by display_seq";
    }

    /**
     * Check input value
     * @return boolean
     */
    private boolean inputCheck() {
        try {
            Long.parseLong(unitPrice5.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店単価設定のランク５"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            unitPrice5.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(unitPrice4.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店単価設定のランク４"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            unitPrice4.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(unitPrice3.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店単価設定のランク３"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            unitPrice3.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(unitPrice2.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店単価設定のランク２"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            unitPrice2.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(unitPrice1.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店単価設定のランク１"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            unitPrice1.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(technicValue5.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間技術売上設定のランク５"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicValue5.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(technicValue4.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間技術売上設定のランク４"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicValue4.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(technicValue3.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間技術売上設定のランク３"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicValue3.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(technicValue2.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間技術売上設定のランク２"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicValue2.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(itemValue5.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間店販売上設定のランク５"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            itemValue5.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(itemValue4.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間店販売上設定のランク４"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            itemValue4.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(itemValue3.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間店販売上設定のランク３"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            itemValue3.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(itemValue2.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "年間店販売上設定のランク２"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            itemValue2.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(visitCycle5.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店周期設定のランク５"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            visitCycle5.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(visitCycle4.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店周期設定のランク４"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            visitCycle4.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(visitCycle3.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店周期設定のランク３"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            visitCycle3.requestFocusInWindow();
            return false;
        }

        try {
            Long.parseLong(visitCycle2.getText());
        } catch (Exception e) {

            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "来店周期設定のランク２"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            visitCycle2.requestFocusInWindow();
            return false;
        }
        long value15 = Long.parseLong(unitPrice5.getText());
        long value14 = Long.parseLong(unitPrice4.getText());
        long value13 = Long.parseLong(unitPrice3.getText());
        long value12 = Long.parseLong(unitPrice2.getText());
        long value11 = Long.parseLong(unitPrice1.getText());
        

        long value25 = Long.parseLong(technicValue5.getText());
        long value24 = Long.parseLong(technicValue4.getText());
        long value23 = Long.parseLong(technicValue3.getText());
        long value22 = Long.parseLong(technicValue2.getText());
        long value21 = Long.parseLong(technicValue1.getText());
        
        long value35 = Long.parseLong(itemValue5.getText());
        long value34 = Long.parseLong(itemValue4.getText());
        long value33 = Long.parseLong(itemValue3.getText());
        long value32 = Long.parseLong(itemValue2.getText());
        long value31 = Long.parseLong(itemValue1.getText());
        
        long value45 = Long.parseLong(visitCycle5.getText());
        long value44 = Long.parseLong(visitCycle4.getText());
        long value43 = Long.parseLong(visitCycle3.getText());
        long value42 = Long.parseLong(visitCycle2.getText());
        
        if(!(value15 > value14 && value14 > value13 && value13 > value12 && value12 > value11)){
            MessageDialog.showMessageDialog(this,
                    "来店単価は不正です。ランク順の値を考慮、入力してください。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            unitPrice5.requestFocusInWindow();
            return false;
        }
        
        if(!(value25 > value24 && value24 > value23 && value23 > value22 && value22 > value21)){
            MessageDialog.showMessageDialog(this,
                    "年間技術売上は不正です。ランク順の値を考慮、入力してください。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicValue5.requestFocusInWindow();
            return false;
        }
        
        if(!(value35 > value34 && value34 > value33 && value33 > value32 && value32 > value31)){
            MessageDialog.showMessageDialog(this,
                    "年間店販売上は不正です。ランク順の値を考慮、入力してください。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            itemValue5.requestFocusInWindow();
            return false;
        }
        
        if(!(value45 < value44 && value44 < value43 && value43 < value42)){
            MessageDialog.showMessageDialog(this,
                    "来店周期は不正です。ランク順の値を考慮、入力してください。",
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            visitCycle5.requestFocusInWindow();
            return false;
        }
        
        DefaultTableModel model = (DefaultTableModel) itemSaledGroupTable.getModel();
        boolean isExistCheck = false;
        String itemSalesGroupChecked = "";
        for (int i = 0; i < model.getRowCount(); i++) {
            JCheckBox cb = (JCheckBox) model.getValueAt(i, 1);
            if (cb.isSelected()) {
                itemSalesGroupChecked += "," + itemGroupList.get(i).getItemGroupId().toString();
                isExistCheck = true;
            }
        }
        if (!isExistCheck) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "店販売上対象グループ"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            itemSaledGroupTable.requestFocusInWindow();
            return false;
        } else {
            currentSetting.setItemSalesGroup(itemSalesGroupChecked.replaceFirst(",", ""));
        }
        
       //if (itemGroupTarget.getSelectedItem() == null) {
        strItemGroupID = "";
        for (int i = 0; i < item_group_idTable.getRowCount(); i++) {
          JCheckBox cb = (JCheckBox)item_group_idTable.getValueAt(i, 2);
            if (cb.isSelected()) {
                strItemGroupID += "," + item_group_idTable.getValueAt(i, 0).toString();
            }
        }
        strItemGroupID = strItemGroupID.replaceFirst(",", "");
        if ("".equals(strItemGroupID)) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_NOT_SELECTED, "店販購入対象グループ"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            //itemGroupTarget.requestFocusInWindow();
            return false;
        }

        return true;
    }

    /**
     * Update data to database
     */
    private void updateValue() {
        try {
            // 設定値の登録
            StringBuilder sql = new StringBuilder(1000);
            if (currentSetting.getShopCategoryId() == null) {
                sql.append("INSERT INTO mst_rank_advanced_setting( ");
                sql.append(" shop_category_id, unit_price5, unit_price4, unit_price3, unit_price2, ");
                sql.append(" unit_price1, technic_value5, technic_value4, technic_value3, ");
                sql.append(" technic_value2, technic_value1, item_value5, item_value4, item_value3, ");
                sql.append(" item_value2, item_value1, visit_cycle5, visit_cycle4, visit_cycle3, ");
                //IVS_LVTu start edit 2015/04/17 New request #36344
                //sql.append(" visit_cycle2, visit_cycle1, item_group_id, item_sales_group, ");
                sql.append(" visit_cycle2, visit_cycle1, item_group_id, item_sales_group, sales_flag, ");
                //IVS_LVTu end edit 2015/04/17 New request #36344
                sql.append(" insert_date, update_date) VALUES ( ");
                sql.append(SQLUtil.convertForSQL(((MstShopCategory) categoryCombobox.getSelectedItem()).getShopCategoryId()));
                sql.append("," + SQLUtil.convertForSQL(unitPrice5.getText()));
                sql.append("," + SQLUtil.convertForSQL(unitPrice4.getText()));
                sql.append("," + SQLUtil.convertForSQL(unitPrice3.getText()));
                sql.append("," + SQLUtil.convertForSQL(unitPrice2.getText()));
                sql.append("," + SQLUtil.convertForSQL(unitPrice1.getText()));
                sql.append("," + SQLUtil.convertForSQL(technicValue5.getText()));
                sql.append("," + SQLUtil.convertForSQL(technicValue4.getText()));
                sql.append("," + SQLUtil.convertForSQL(technicValue3.getText()));
                sql.append("," + SQLUtil.convertForSQL(technicValue2.getText()));
                sql.append("," + SQLUtil.convertForSQL(technicValue1.getText()));
                sql.append("," + SQLUtil.convertForSQL(itemValue5.getText()));
                sql.append("," + SQLUtil.convertForSQL(itemValue4.getText()));
                sql.append("," + SQLUtil.convertForSQL(itemValue3.getText()));
                sql.append("," + SQLUtil.convertForSQL(itemValue2.getText()));
                sql.append("," + SQLUtil.convertForSQL(itemValue1.getText()));
                sql.append("," + SQLUtil.convertForSQL(visitCycle5.getText()));
                sql.append("," + SQLUtil.convertForSQL(visitCycle4.getText()));
                sql.append("," + SQLUtil.convertForSQL(visitCycle3.getText()));
                sql.append("," + SQLUtil.convertForSQL(visitCycle2.getText()));
                sql.append("," + SQLUtil.convertForSQL(visitCycle1.getText()));
                //sql.append("," + SQLUtil.convertForSQL(((MstRankItemGroup) itemGroupTarget.getSelectedItem()).getItemGroupId()));
                sql.append("," + SQLUtil.convertForSQL(strItemGroupID.toString()));
                sql.append("," + SQLUtil.convertForSQL(currentSetting.getItemSalesGroup()));
                //IVS_LVTu start add 2015/04/17 New request #36344
                if (checkSalesFlag.isSelected() == true) {
                    sql.append("," + SQLUtil.convertForSQL(1));
                }else {
                    sql.append("," + SQLUtil.convertForSQL(0));
                }
                //IVS_LVTu end add 2015/04/17 New request #36344
                sql.append(",current_timestamp,current_timestamp)");

            } else {
                sql.append(" update mst_rank_advanced_setting");
                sql.append(" set");
                sql.append("      unit_price5 = " + SQLUtil.convertForSQL(unitPrice5.getText()));
                sql.append("     ,unit_price4 = " + SQLUtil.convertForSQL(unitPrice4.getText()));
                sql.append("     ,unit_price3 = " + SQLUtil.convertForSQL(unitPrice3.getText()));
                sql.append("     ,unit_price2 = " + SQLUtil.convertForSQL(unitPrice2.getText()));
                sql.append("     ,unit_price1 = " + SQLUtil.convertForSQL(unitPrice1.getText()));
                sql.append("     ,technic_value5 = " + SQLUtil.convertForSQL(technicValue5.getText()));
                sql.append("     ,technic_value4 = " + SQLUtil.convertForSQL(technicValue4.getText()));
                sql.append("     ,technic_value3 = " + SQLUtil.convertForSQL(technicValue3.getText()));
                sql.append("     ,technic_value2 = " + SQLUtil.convertForSQL(technicValue2.getText()));
                sql.append("     ,technic_value1 = " + SQLUtil.convertForSQL(technicValue1.getText()));
                sql.append("     ,item_value5 = " + SQLUtil.convertForSQL(itemValue5.getText()));
                sql.append("     ,item_value4 = " + SQLUtil.convertForSQL(itemValue4.getText()));
                sql.append("     ,item_value3 = " + SQLUtil.convertForSQL(itemValue3.getText()));
                sql.append("     ,item_value2 = " + SQLUtil.convertForSQL(itemValue2.getText()));
                sql.append("     ,item_value1 = " + SQLUtil.convertForSQL(itemValue1.getText()));
                sql.append("     ,visit_cycle5 = " + SQLUtil.convertForSQL(visitCycle5.getText()));
                sql.append("     ,visit_cycle4 = " + SQLUtil.convertForSQL(visitCycle4.getText()));
                sql.append("     ,visit_cycle3 = " + SQLUtil.convertForSQL(visitCycle3.getText()));
                sql.append("     ,visit_cycle2 = " + SQLUtil.convertForSQL(visitCycle2.getText()));
                sql.append("     ,visit_cycle1 = " + SQLUtil.convertForSQL(visitCycle1.getText()));
                sql.append("     ,item_group_id = " + SQLUtil.convertForSQL(strItemGroupID.toString()));
                sql.append("     ,item_sales_group = " + SQLUtil.convertForSQL(currentSetting.getItemSalesGroup()));
                //IVS_LVTu start add 2015/04/17 New request #36344
                if (checkSalesFlag.isSelected() == true) {
                    sql.append(",sales_flag = " + SQLUtil.convertForSQL(1));
                }else {
                    sql.append(",sales_flag = " + SQLUtil.convertForSQL(0));
                }
                //IVS_LVTu end add 2015/04/17 New request #36344
                sql.append("     ,update_date = current_timestamp");
                sql.append(" where");
                sql.append("     shop_category_id = " + SQLUtil.convertForSQL(currentSetting.getShopCategoryId()));
            }
            if (SystemInfo.getConnection().executeUpdate(sql.toString()) == 1) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                loadRankAdvancedSettingData();
            }
        } catch (Exception e) {
        }
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(searchButton);
        SystemInfo.addMouseCursorChange(btnRegist);
        SystemInfo.addMouseCursorChange(btnClose);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        technicValue4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)technicValue4.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        technicValue11 = new com.geobeck.swing.JFormattedTextFieldEx();
        technicValue31 = new com.geobeck.swing.JFormattedTextFieldEx();
        technicValue1 = new com.geobeck.swing.JFormattedTextFieldEx();
        technicValue2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)technicValue2.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        technicValue3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)technicValue3.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        technicValue21 = new com.geobeck.swing.JFormattedTextFieldEx();
        unitPrice1 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)unitPrice1.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        jLabel7 = new javax.swing.JLabel();
        unitPrice2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)unitPrice2.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        technicValue41 = new com.geobeck.swing.JFormattedTextFieldEx();
        unitPrice3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)unitPrice3.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        unitPrice4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)unitPrice4.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue5 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)itemValue5.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)itemValue4.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)itemValue3.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)itemValue2.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue1 = new com.geobeck.swing.JFormattedTextFieldEx();
        visitCycle5 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitCycle5.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue31 = new com.geobeck.swing.JFormattedTextFieldEx();
        itemValue11 = new com.geobeck.swing.JFormattedTextFieldEx();
        itemValue21 = new com.geobeck.swing.JFormattedTextFieldEx();
        visitCycle4 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitCycle4.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        visitCycle3 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitCycle3.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        visitCycle1 = new com.geobeck.swing.JFormattedTextFieldEx();
        visitCycle2 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)visitCycle2.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        visitCycle41 = new com.geobeck.swing.JFormattedTextFieldEx();
        visitCycle31 = new com.geobeck.swing.JFormattedTextFieldEx();
        visitCycle21 = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        unitPrice5 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)unitPrice5.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        technicValue5 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)technicValue5.getDocument()).setDocumentFilter(new CustomFilter(8, CustomFilter.NUMERIC));
        itemValue41 = new com.geobeck.swing.JFormattedTextFieldEx();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        categoryCombobox = new javax.swing.JComboBox();
        btnRegist = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemSaledGroupTable = new com.geobeck.swing.JTableEx();
        jScrollPane2 = new javax.swing.JScrollPane();
        item_group_idTable = new com.geobeck.swing.JTableEx();
        lbMsg1 = new javax.swing.JLabel();
        lbMsg2 = new javax.swing.JLabel();
        lbMsg3 = new javax.swing.JLabel();
        lbMsg4 = new javax.swing.JLabel();
        lbMsg5 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        checkSalesFlag = new javax.swing.JCheckBox();

        setFocusCycleRoot(true);

        jPanel1.setOpaque(false);

        technicValue4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                technicValue4FocusLost(evt);
            }
        });

        technicValue11.setEditable(false);
        technicValue11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue11.setFocusable(false);

        technicValue31.setEditable(false);
        technicValue31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue31.setFocusable(false);

        technicValue1.setEditable(false);
        technicValue1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue1.setFocusable(false);

        technicValue2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                technicValue2FocusLost(evt);
            }
        });

        technicValue3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                technicValue3ActionPerformed(evt);
            }
        });
        technicValue3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                technicValue3FocusLost(evt);
            }
        });

        jLabel2.setText("【来店単価】");

        jLabel6.setText("ランク２");

        jLabel3.setText("ランク５");

        jLabel4.setText("ランク４");

        jLabel5.setText("ランク３");

        technicValue21.setEditable(false);
        technicValue21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue21.setFocusable(false);

        unitPrice1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        unitPrice1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setText("ランク１");

        unitPrice2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        unitPrice2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        technicValue41.setEditable(false);
        technicValue41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue41.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue41.setFocusable(false);

        unitPrice3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        unitPrice3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        unitPrice4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        unitPrice4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        itemValue5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemValue5FocusLost(evt);
            }
        });

        itemValue4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemValue4FocusLost(evt);
            }
        });

        itemValue3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemValue3FocusLost(evt);
            }
        });

        itemValue2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemValue2FocusLost(evt);
            }
        });

        itemValue1.setEditable(false);
        itemValue1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue1.setFocusable(false);

        visitCycle5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                visitCycle5FocusLost(evt);
            }
        });

        itemValue31.setEditable(false);
        itemValue31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue31.setFocusable(false);

        itemValue11.setEditable(false);
        itemValue11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue11.setFocusable(false);

        itemValue21.setEditable(false);
        itemValue21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue21.setFocusable(false);

        visitCycle4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                visitCycle4FocusLost(evt);
            }
        });

        visitCycle3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitCycle3ActionPerformed(evt);
            }
        });
        visitCycle3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                visitCycle3FocusLost(evt);
            }
        });

        visitCycle1.setEditable(false);
        visitCycle1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle1.setFocusable(false);

        visitCycle2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                visitCycle2FocusLost(evt);
            }
        });

        visitCycle41.setEditable(false);
        visitCycle41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle41.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle41.setFocusable(false);

        visitCycle31.setEditable(false);
        visitCycle31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle31.setFocusable(false);

        visitCycle21.setEditable(false);
        visitCycle21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        visitCycle21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        visitCycle21.setFocusable(false);

        jLabel8.setText("円以上");

        jLabel9.setText("円以上");

        jLabel10.setText("円以上");

        jLabel11.setText("日以内");

        jLabel12.setText("円");

        jLabel13.setText("円");

        jLabel14.setText("円");

        jLabel15.setText("円");

        jLabel20.setText("円～");

        jLabel21.setText("円～");

        jLabel22.setText("円～");

        jLabel23.setText("円～");

        jLabel24.setText("円");

        jLabel25.setText("円");

        jLabel26.setText("円");

        jLabel27.setText("円");

        jLabel28.setText("円～");

        jLabel29.setText("円～");

        jLabel30.setText("円～");

        jLabel31.setText("円～");

        jLabel32.setText("円");

        jLabel33.setText("円");

        jLabel34.setText("円");

        jLabel35.setText("円");

        jLabel36.setText("日～");

        jLabel37.setText("日～");

        jLabel38.setText("日～");

        jLabel39.setText("日以上");

        jLabel40.setText("日");

        jLabel41.setText("日");

        jLabel42.setText("日");

        jLabel43.setText("【年間売上】");

        jLabel44.setText("【年間店販売上】");

        jLabel45.setText("【来店周期】");

        unitPrice5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        unitPrice5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        technicValue5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicValue5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        technicValue5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                technicValue5FocusLost(evt);
            }
        });

        itemValue41.setEditable(false);
        itemValue41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        itemValue41.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        itemValue41.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(unitPrice5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(unitPrice2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(unitPrice3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(unitPrice4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(unitPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel15))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel43)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(technicValue5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel9)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(technicValue2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(technicValue3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(technicValue4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(technicValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(technicValue21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(technicValue31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(technicValue41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(technicValue11, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel26))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(itemValue5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemValue1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemValue2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemValue3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel28)
                                            .addComponent(jLabel29)
                                            .addComponent(jLabel31)
                                            .addComponent(jLabel30))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(itemValue11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(itemValue21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(itemValue31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(itemValue41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel35)
                                            .addComponent(jLabel33)
                                            .addComponent(jLabel32)
                                            .addComponent(jLabel34)))
                                    .addComponent(jLabel10))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(visitCycle2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(visitCycle3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(visitCycle4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(visitCycle5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(visitCycle1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel39)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel36)
                                            .addComponent(jLabel37)
                                            .addComponent(jLabel38))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(visitCycle21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(visitCycle31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(visitCycle41, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel40)
                                                .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)))))
                            .addComponent(jLabel45)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel2)))
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(11, 11, 11))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(unitPrice5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(unitPrice4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(unitPrice3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(unitPrice2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(unitPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel22))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel23)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue41, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel24))
                            .addGap(90, 90, 90))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue31, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel25))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel26))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(technicValue11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel44)
                                .addComponent(jLabel43))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel29))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel30))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel31)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel32)
                                .addComponent(itemValue41, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue31, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel33))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel34))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemValue11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel35)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel45)
                                    .addGap(36, 36, 36))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle41, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel40))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle31, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel41))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel42)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel36))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel37))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel38))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(visitCycle1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel39))))))))
        );

        jPanel2.setOpaque(false);

        jLabel1.setText("業態");
        jLabel1.setPreferredSize(new java.awt.Dimension(24, 15));

        categoryCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryComboboxActionPerformed(evt);
            }
        });

        btnRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btnRegist.setBorderPainted(false);
        btnRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btnRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistActionPerformed(evt);
            }
        });

        searchButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        searchButton.setBorderPainted(false);
        searchButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setBorderPainted(false);
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(categoryCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoryCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLabel46.setText("店販購入対象グループ");

        jLabel47.setText("店販売上対象グループ");

        itemSaledGroupTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品グループ", "集計対象"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(itemSaledGroupTable);
        SwingUtil.setJTableHeaderRenderer(itemSaledGroupTable, SystemInfo.getTableHeaderRenderer());
        itemSaledGroupTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(itemSaledGroupTable);
        if (itemSaledGroupTable.getColumnModel().getColumnCount() > 0) {
            itemSaledGroupTable.getColumnModel().getColumn(1).setResizable(false);
        }
        itemSaledGroupTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        itemSaledGroupTable.getColumnModel().getColumn(1).setPreferredWidth(10);

        item_group_idTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "","商品グループ", "集計対象"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false,false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(item_group_idTable);
        SwingUtil.setJTableHeaderRenderer(item_group_idTable, SystemInfo.getTableHeaderRenderer());
        item_group_idTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(item_group_idTable);
        if (item_group_idTable.getColumnModel().getColumnCount() > 0) {
            item_group_idTable.getColumnModel().getColumn(1).setResizable(false);
        }
        item_group_idTable.getColumnModel().getColumn(0).setMaxWidth(0);
        item_group_idTable.getColumnModel().getColumn(0).setMinWidth(0);
        item_group_idTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        item_group_idTable.getColumnModel().getColumn(2).setPreferredWidth(10);

        lbMsg1.setText("※店販購入対象グループを複数選択している場合");

        lbMsg2.setText("買っている・・・設定している商品グループの商品を");

        lbMsg3.setText("全て購入している場合に対象");

        lbMsg4.setText("買っていない・・・設定している商品グループの");

        lbMsg5.setText("いずれかの商品を購入している場合に対象");

        jLabel48.setText("【年間売上設定】");

        checkSalesFlag.setText("店販売上を含む ");
        checkSalesFlag.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkSalesFlag.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkSalesFlag.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbMsg3)
                                        .addGap(91, 91, 91))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(lbMsg5)
                                                .addGap(14, 14, 14))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lbMsg1)
                                                    .addComponent(lbMsg4)
                                                    .addComponent(lbMsg2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel48)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(checkSalesFlag, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE)))))))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(checkSalesFlag))
                        .addGap(25, 25, 25)
                        .addComponent(lbMsg1)
                        .addGap(8, 8, 8)
                        .addComponent(lbMsg2)
                        .addGap(8, 8, 8)
                        .addComponent(lbMsg3)
                        .addGap(8, 8, 8)
                        .addComponent(lbMsg4)
                        .addGap(8, 8, 8)
                        .addComponent(lbMsg5)))
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void technicValue3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_technicValue3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_technicValue3ActionPerformed

    private void visitCycle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitCycle3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visitCycle3ActionPerformed

    private void btnRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistActionPerformed
        btnRegist.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if (itemSaledGroupTable.getCellEditor() != null) {
                itemSaledGroupTable.getCellEditor().stopCellEditing();
            }
            if (inputCheck()) {
                btnRegist.setCursor(null);
                this.updateValue();
            }
        
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnRegistActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
         searchButton.setCursor(null);
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if (itemSaledGroupTable.getCellEditor() != null) {
                itemSaledGroupTable.getCellEditor().stopCellEditing();
            }
            this.loadRankAdvancedSettingData();
            this.loadItemSalesGroup();
            btnRegist.setEnabled(true);
            
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void technicValue5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_technicValue5FocusLost
        if (technicValue5.getText().length() > 0) {
            technicValue41.setText(String.valueOf(Long.parseLong(technicValue5.getText()) - 1));
        }
    }//GEN-LAST:event_technicValue5FocusLost

    private void technicValue4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_technicValue4FocusLost
        if (technicValue4.getText().length() > 0) {
            technicValue31.setText(String.valueOf(Long.parseLong(technicValue4.getText()) - 1));
        }

    }//GEN-LAST:event_technicValue4FocusLost

    private void technicValue3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_technicValue3FocusLost
        if (technicValue3.getText().length() > 0) {
            technicValue21.setText(String.valueOf(Long.parseLong(technicValue3.getText()) - 1));
        }
    }//GEN-LAST:event_technicValue3FocusLost

    private void technicValue2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_technicValue2FocusLost
        if (technicValue2.getText().length() > 0) {
            technicValue11.setText(String.valueOf(Long.parseLong(technicValue2.getText()) - 1));
            technicValue1.setText("0");
        }
    }//GEN-LAST:event_technicValue2FocusLost

    private void itemValue5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemValue5FocusLost
        if (itemValue5.getText().length() > 0) {
            itemValue41.setText(String.valueOf(Long.parseLong(itemValue5.getText()) - 1));
        }
    }//GEN-LAST:event_itemValue5FocusLost

    private void itemValue4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemValue4FocusLost
        if (itemValue4.getText().length() > 0) {
            itemValue31.setText(String.valueOf(Long.parseLong(itemValue4.getText()) - 1));
        }
    }//GEN-LAST:event_itemValue4FocusLost

    private void itemValue3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemValue3FocusLost
        if (itemValue3.getText().length() > 0) {
            itemValue21.setText(String.valueOf(Long.parseLong(itemValue3.getText()) - 1));
        }
    }//GEN-LAST:event_itemValue3FocusLost

    private void itemValue2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemValue2FocusLost
        if (itemValue2.getText().length() > 0) {
            itemValue11.setText(String.valueOf(Long.parseLong(itemValue2.getText()) - 1));
            itemValue1.setText("0");
        }
    }//GEN-LAST:event_itemValue2FocusLost

    private void visitCycle5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_visitCycle5FocusLost
        if (visitCycle5.getText().length() > 0) {
            visitCycle41.setText(String.valueOf(Long.parseLong(visitCycle5.getText()) + 1));
        }
    }//GEN-LAST:event_visitCycle5FocusLost

    private void visitCycle4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_visitCycle4FocusLost
        if (visitCycle4.getText().length() > 0) {
            visitCycle31.setText(String.valueOf(Long.parseLong(visitCycle4.getText()) + 1));
        }
    }//GEN-LAST:event_visitCycle4FocusLost

    private void visitCycle3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_visitCycle3FocusLost
        if (visitCycle3.getText().length() > 0) {
            visitCycle21.setText(String.valueOf(Long.parseLong(visitCycle3.getText()) + 1));
        }
    }//GEN-LAST:event_visitCycle3FocusLost

    private void visitCycle2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_visitCycle2FocusLost
        if (visitCycle2.getText().length() > 0) {
            visitCycle1.setText(String.valueOf(Long.parseLong(visitCycle2.getText()) + 1));
        }
    }//GEN-LAST:event_visitCycle2FocusLost

    private void categoryComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryComboboxActionPerformed
        btnRegist.setEnabled(false);
    }//GEN-LAST:event_categoryComboboxActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRegist;
    private javax.swing.JComboBox categoryCombobox;
    private javax.swing.JCheckBox checkSalesFlag;
    private javax.swing.JTable itemSaledGroupTable;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue1;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue11;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue2;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue21;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue3;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue31;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue4;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue41;
    private com.geobeck.swing.JFormattedTextFieldEx itemValue5;
    private javax.swing.JTable item_group_idTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbMsg1;
    private javax.swing.JLabel lbMsg2;
    private javax.swing.JLabel lbMsg3;
    private javax.swing.JLabel lbMsg4;
    private javax.swing.JLabel lbMsg5;
    private javax.swing.JButton searchButton;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue1;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue11;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue2;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue21;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue3;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue31;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue4;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue41;
    private com.geobeck.swing.JFormattedTextFieldEx technicValue5;
    private com.geobeck.swing.JFormattedTextFieldEx unitPrice1;
    private com.geobeck.swing.JFormattedTextFieldEx unitPrice2;
    private com.geobeck.swing.JFormattedTextFieldEx unitPrice3;
    private com.geobeck.swing.JFormattedTextFieldEx unitPrice4;
    private com.geobeck.swing.JFormattedTextFieldEx unitPrice5;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle1;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle2;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle21;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle3;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle31;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle4;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle41;
    private com.geobeck.swing.JFormattedTextFieldEx visitCycle5;
    // End of variables declaration//GEN-END:variables
    /**
	 * 商品分類マスタ登録画面用FocusTraversalPolicy
	 */
	private	SettingControllFocusTraversalPolicy	ftp	=
			new SettingControllFocusTraversalPolicy();
	
	/**
	 * 商品分類マスタ登録画面用FocusTraversalPolicyを取得する。
	 * @return 商品分類マスタ登録画面用FocusTraversalPolicy
	 */
	public SettingControllFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}   
    /**
	 * SettingControllFocusTraversalPolicy
	 */
	private class SettingControllFocusTraversalPolicy
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
			if (aComponent.equals(unitPrice5))
			{
				return unitPrice4;
			}
			else if (aComponent.equals(unitPrice4))
			{
				return unitPrice3;
			}
			else if (aComponent.equals(unitPrice3))
			{
				return unitPrice2;
			}
                        else if (aComponent.equals(unitPrice2))
			{
				return unitPrice1;
			}
			else if (aComponent.equals(unitPrice1))
			{
				return technicValue5;
			}
                        if (aComponent.equals(technicValue5))
			{
				return technicValue4;
			}
			else if (aComponent.equals(technicValue4))
			{
				return technicValue3;
			}
			else if (aComponent.equals(technicValue3))
			{
				return technicValue2;
			}
                        else if (aComponent.equals(technicValue2))
			{
				return itemValue5;
			}
			else if (aComponent.equals(itemValue5))
			{
				return itemValue4;
			}
                        else if (aComponent.equals(itemValue4))
			{
				return itemValue3;
			}
			else if (aComponent.equals(itemValue3))
			{
				return itemValue2;
			}
                        else if (aComponent.equals(itemValue2))
			{
				return visitCycle5;
			}
			else if (aComponent.equals(visitCycle5))
			{
				return visitCycle4;
			}
                        else if (aComponent.equals(visitCycle4))
			{
				return visitCycle3;
			}
			else if (aComponent.equals(visitCycle3))
			{
				return visitCycle2;
			}
			
			return unitPrice5;
		}

		/**
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{
			if (aComponent.equals(unitPrice5))
			{
				return unitPrice4;
			}
			else if (aComponent.equals(unitPrice4))
			{
				return unitPrice3;
			}
			else if (aComponent.equals(unitPrice3))
			{
				return unitPrice2;
			}
                        else if (aComponent.equals(unitPrice2))
			{
				return unitPrice1;
			}
			else if (aComponent.equals(unitPrice1))
			{
				return technicValue5;
			}
                        if (aComponent.equals(technicValue5))
			{
				return technicValue4;
			}
			else if (aComponent.equals(technicValue4))
			{
				return technicValue3;
			}
			else if (aComponent.equals(technicValue3))
			{
				return technicValue2;
			}
                        else if (aComponent.equals(technicValue2))
			{
				return itemValue5;
			}
			else if (aComponent.equals(itemValue5))
			{
				return itemValue4;
			}
                        else if (aComponent.equals(itemValue4))
			{
				return itemValue3;
			}
			else if (aComponent.equals(itemValue3))
			{
				return itemValue2;
			}
                        else if (aComponent.equals(itemValue2))
			{
				return visitCycle5;
			}
			else if (aComponent.equals(visitCycle5))
			{
				return visitCycle4;
			}
                        else if (aComponent.equals(visitCycle4))
			{
				return visitCycle3;
			}
			else if (aComponent.equals(visitCycle3))
			{
				return visitCycle2;
			}
			
			return unitPrice5;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return unitPrice5;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return visitCycle2;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return unitPrice5;
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
			return unitPrice5;
		}
	}
}

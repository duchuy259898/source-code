/*
 * mobileMemberListPanel.java
 *
 * Created on 2007/03/12, 9:20
 */

package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.sosia.pos.search.customer.SearchCustomerDialog;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.logging.*;
import java.sql.*;

import com.geobeck.swing.*;
import com.geobeck.sosia.pos.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author  katagiri
 */
public class MobileMemberListPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	
    Map mapSosiaShopName = null;
	/** Creates new form mobileMemberListPanel */
	public MobileMemberListPanel()
	{
            initComponents();
            addMouseCursorChange();
            this.setPath("顧客管理");
            this.setTitle("ケータイ会員一覧");
            this.init();
            this.setSize(833, 691);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gearButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        addDateFrom = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel3 = new javax.swing.JLabel();
        addDateTo = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel4 = new javax.swing.JLabel();
        nongear = new javax.swing.JRadioButton();
        gear = new javax.swing.JRadioButton();
        allmember = new javax.swing.JRadioButton();
        searchButton = new javax.swing.JButton();
        SOSIAID = new javax.swing.JTextField();
        cusIspotIDLabel = new javax.swing.JLabel();
        firstName = new com.geobeck.swing.JFormattedTextFieldEx();
        lastName = new com.geobeck.swing.JFormattedTextFieldEx();
        cusIspotIDLabel1 = new javax.swing.JLabel();
        mobileMembersScrollPane = new javax.swing.JScrollPane();
        mobileMembers = new com.geobeck.swing.JTableEx();
        count = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        jPanel1.setOpaque(false);

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setText("店舗");

        jLabel1.setText("登録日");

        jLabel3.setText("〜");

        jLabel4.setText("表示");

        gearButtonGroup.add(nongear);
        nongear.setSelected(true);
        nongear.setText("非連動");
        nongear.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        nongear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        nongear.setOpaque(false);

        gearButtonGroup.add(gear);
        gear.setText("連動済み");
        gear.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gear.setOpaque(false);

        gearButtonGroup.add(allmember);
        allmember.setText("全て");
        allmember.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allmember.setMargin(new java.awt.Insets(0, 0, 0, 0));
        allmember.setOpaque(false);

        searchButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        searchButton.setBorderPainted(false);
        searchButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        cusIspotIDLabel.setText("SOSIA ID");

        firstName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        firstName.setColumns(20);
        firstName.setInputKanji(true);
        firstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameActionPerformed(evt);
            }
        });

        lastName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lastName.setColumns(20);
        lastName.setInputKanji(true);

        cusIspotIDLabel1.setText("姓名");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(38, 38, 38))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(addDateFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(addDateTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 215, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(80, 80, 80))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(cusIspotIDLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cusIspotIDLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(SOSIAID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, firstName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lastName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 105, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(nongear)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(gear)
                        .add(18, 18, 18)
                        .add(allmember)
                        .add(18, 18, 18)
                        .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(7, 7, 7)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(addDateFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(addDateTo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cusIspotIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(SOSIAID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cusIspotIDLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lastName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(firstName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel4)
                        .add(nongear)
                        .add(allmember, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(gear))
                    .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(20, 20, 20))
        );

        mobileMembersScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        mobileMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "顧客情報", "店舗", "ケータイ会員登録日", "SOSIA ID", "姓", "名", "生年月日", "顧客検索", "新規登録"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mobileMembers.setSelectionBackground(new java.awt.Color(255, 210, 142));
        mobileMembers.setSelectionForeground(new java.awt.Color(0, 0, 0));
        mobileMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mobileMembers.getTableHeader().setReorderingAllowed(false);
        this.initTableColumnWidth();
        SwingUtil.setJTableHeaderRenderer(mobileMembers, SystemInfo.getTableHeaderRenderer());
        mobileMembers.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(mobileMembers);
        mobileMembersScrollPane.setViewportView(mobileMembers);

        count.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        count.setText("（対象人数： 0件）");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(207, 207, 207)
                        .add(count)
                        .add(138, 138, 138))
                    .add(layout.createSequentialGroup()
                        .add(mobileMembersScrollPane)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(count)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mobileMembersScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

        private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
            searchButton.setCursor(null);
            this.search();
        }//GEN-LAST:event_searchButtonActionPerformed

    private void firstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField SOSIAID;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo addDateFrom;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo addDateTo;
    private javax.swing.JRadioButton allmember;
    private javax.swing.JLabel count;
    private javax.swing.JLabel cusIspotIDLabel;
    private javax.swing.JLabel cusIspotIDLabel1;
    private com.geobeck.swing.JFormattedTextFieldEx firstName;
    private javax.swing.JRadioButton gear;
    private javax.swing.ButtonGroup gearButtonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private com.geobeck.swing.JFormattedTextFieldEx lastName;
    private com.geobeck.swing.JTableEx mobileMembers;
    private javax.swing.JScrollPane mobileMembersScrollPane;
    private javax.swing.JRadioButton nongear;
    private javax.swing.JButton searchButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    // End of variables declaration//GEN-END:variables
	
    
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
            SystemInfo.addMouseCursorChange(searchButton);
	}
    
	private MobileMemberList mobileMemberList = new MobileMemberList();
        
	/**
	 * ケータイ会員一覧画面用FocusTraversalPolicy
	 */
	private	mobileMemberListFocusTraversalPolicy ftp = new mobileMemberListFocusTraversalPolicy();
	
	/**
	 * ケータイ会員一覧画面用FocusTraversalPolicyを取得する。
	 * @return ケータイ会員一覧画面用FocusTraversalPolicy
	 */
	public mobileMemberListFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	/**
	 * 初期化処理を行う。
	 */
	private void init()
	{
            SystemInfo.initGroupShopComponents(shop, 3);
            
            /*if (SystemInfo.getCurrentShop().getShopID() == 0) {
                ArrayList shopList = new ArrayList();
                for (int i = 0; i < shop.getItemCount(); i++) {
                    if (SystemInfo.getSosiaGearShopList().containsKey(((MstShop)shop.getItemAt(i)).getShopID())) {
                        shopList.add(shop.getItemAt(i));
                    }
                }

                shop.removeAllItems();
                for (Iterator itr = shopList.iterator(); itr.hasNext();) {
                    shop.addItem(itr.next());
                } 
            }
            */
            SwingUtil.clearTable(mobileMembers);
	}
	
	/**
	 * ケータイ会員の一覧を表示する。
	 */
	private void showList()
	{
           
             DefaultTableModel model = (DefaultTableModel)mobileMembers.getModel();
            for (MobileMemberData mmd : mobileMemberList) {
                Object[] rowData = {
                    getUserSearchButton(mmd),
                    mapSosiaShopName.get(mmd.getSosiaCode().toString()),
                    mmd.getAddDateStr(),
                    mmd.getSosiaID().toString(),
                    mmd.getCusName1(),
                    mmd.getCusName2(),
                    mmd.getBirthDateStr(),
                    getSearchButton(mmd),
                    getRegistButton(mmd)
                };
        
                model.addRow(rowData);
            }
            
            count.setText("（対象人数： " + model.getRowCount() + "件）");
	}
	
        /**
	 * ユーザ検索ボタンを取得する
	 */
	private JButton getUserSearchButton(final MobileMemberData mmd)
	{
            JButton button = new JButton();
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
            button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));
            button.setSize(48, 25);
            
            button.setEnabled(mmd.isSosiaGear());
            
            button.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
		{
                    MstCustomerPanel mcp = null;
                            
                    try {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        mcp = new MstCustomerPanel(mmd.getCustomerID(), true, true);
                        SwingUtil.openAnchorDialog( (JFrame)null, true, mcp, "顧客情報", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    
                    mcp = null;
		}
            });
 
            return button;
	}        
        
        /**
	 * 検索ボタンを取得する
	 */
	private JButton getSearchButton(final MobileMemberData mmd)
	{
            JButton button = new JButton();
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/search/search_off.jpg")));
            button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/search/search_on.jpg")));
            button.setSize(48, 25);
            
            button.setEnabled(!mmd.isSosiaGear());
            
            button.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
		{
                    showSearchCustomerDialog(mmd);
		}
            });
            
            return button;
	}

        private void showSearchCustomerDialog(MobileMemberData mmd) {

            SearchCustomerDialog sc = new SearchCustomerDialog( parentFrame, true);
            sc.setSearchCustomerName1(mmd.getCusName1());
            sc.setSearchCustomerName2(mmd.getCusName2());
            sc.setVisible(true);

            if (sc.getSelectedCustomer() != null)
            {
                if (MessageDialog.showYesNoDialog(this,
                            "顧客連動処理を行います。よろしいですか？",
                            this.getTitle(),
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    MstCustomer mc = sc.getSelectedCustomer();
                    // SOSIA ID
                    mc.getSosiaCustomer().setSosiaID(mmd.getSosiaID());
                    // 携帯メールアドレス
                    mc.setCellularMailAddress(mmd.getEmail());
                    // 姓
                    if (mc.getCustomerName(0) == null || mc.getCustomerName(0).length() == 0) {
                        mc.setCustomerName(0, mmd.getCusName1());
                    }
                    // 名
                    if (mc.getCustomerName(1) == null || mc.getCustomerName(1).length() == 0) {
                        mc.setCustomerName(1, mmd.getCusName2());
                    }
                    // 性別
                    if (mc.getSex() == null || mc.getSex().equals(0)) {
                        mc.setSex(mmd.getSex());
                    }
                    // 生年月日
                    if (mc.getBirthday() == null) {
                        mc.setBirthdayDate(mmd.getBirthDate());
                    }

                    try {
                        mc.regist(SystemInfo.getConnection());

                    } catch(SQLException e) {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    }
                }
            }

            sc.dispose();
            sc = null;
            
            this.search();
        }
        
        /**
	 * 登録ボタンを取得する
	 */
	private JButton getRegistButton(final MobileMemberData mmd)
	{
            JButton button = new JButton();
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/new_registration_off.jpg")));
            button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/new_registration_on.jpg")));
            button.setSize(48, 25);
            
            button.setEnabled(!mmd.isSosiaGear());
            
            button.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
		{
                    showMstCustomerPanel(mmd);
		}
            });
            
            return button;
	}
        
        private void showMstCustomerPanel(MobileMemberData mmd) {
            
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                MstCustomerPanel mcp = new MstCustomerPanel(mmd);
                SwingUtil.openAnchorDialog(null, true, mcp, "顧客情報", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            
            this.search();
        }
        
	/**
	 * 検索処理を行う。
	 */
	private void search()
	{
            DefaultTableModel model = (DefaultTableModel)mobileMembers.getModel();
            if( mobileMembers.getCellEditor() != null ) mobileMembers.getCellEditor().stopCellEditing();
            model.setRowCount(0);
            SwingUtil.clearTable(mobileMembers);
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                mobileMemberList.setSosiaCode(null);
                mobileMemberList.setAddDateFrom(null);
                mobileMemberList.setAddDateTo(null);
                mobileMemberList.setGearCondition(null);
                mobileMemberList.setSosiaIdSearch(SOSIAID.getText());
                mobileMemberList.setFirstNameSearch(firstName.getText());
                mobileMemberList.setLastNameSearch(lastName.getText());

                
                //グループ
                String sosiaCode =   "";
                String shopIdList = "";
                if (shop.getSelectedItem() instanceof MstGroup) {
                   MstGroup mg = (MstGroup) shop.getSelectedItem();
                   shopIdList = mg.getShopIDListAll();
                } //店舗
                else if (shop.getSelectedItem() instanceof MstShop) {
                    MstShop ms = (MstShop) shop.getSelectedItem();
                     shopIdList = ms.getShopID().toString();
                }
                if(shopIdList.equals("")) return;
                sosiaCode =  SystemInfo.getSosiaGearByShopIdList(shopIdList);
                mapSosiaShopName =  SystemInfo.getMapSosiaShopName(sosiaCode);
                mobileMemberList.setSosiaCode(sosiaCode);
                if (this.addDateFrom.getDateStr() != null) {
                    mobileMemberList.setAddDateFrom(this.addDateFrom.getDateStrWithFirstTime());
                }
                if (this.addDateTo.getDateStr() != null) {
                    mobileMemberList.setAddDateTo(this.addDateTo.getDateStrWithLastTime());
                }

                if (nongear.isSelected()) {
                    mobileMemberList.setGearCondition(0);
                } else if (gear.isSelected()) {
                    mobileMemberList.setGearCondition(1);
                } else {
                    mobileMemberList.setGearCondition(2);
                }

                mobileMemberList.load();

                this.showList();

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}
	
	/**
	 * ケータイ会員一覧画面用FocusTraversalPolicy
	 */
	private class mobileMemberListFocusTraversalPolicy
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
                    if (aComponent.equals(shop)) {
                        return addDateFrom;
                    } else if (aComponent.equals(addDateFrom)) {
                        return addDateTo;
                    } else if (aComponent.equals(addDateFrom)) {
                        return addDateTo;
                    } else if (aComponent.equals(addDateTo)) {
                        return SOSIAID;
                    } else if (aComponent.equals(SOSIAID)) {
                        return firstName;
                    } else if (aComponent.equals(firstName)) {
                        return lastName;
                    } else if (aComponent.equals(lastName)) {
                        return nongear;
                    } else if (aComponent.equals(nongear)) {
                        return gear;
                    } else if (aComponent.equals(gear)) {
                        return allmember;
                    } else if (aComponent.equals(allmember)) {
                        return searchButton;
                    } else if (aComponent.equals(searchButton)) {
                        return getDefaultComponent();
                    }
                    return getDefaultComponent();
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
                    if (aComponent.equals(searchButton)) {
                        return allmember;
                    } else if (aComponent.equals(allmember)) {
                        return gear;
                    } else if (aComponent.equals(gear)) {
                        return nongear;
                    } else if (aComponent.equals(nongear)) {
                        return lastName;
                    } else if (aComponent.equals(lastName)) {
                        return firstName;
                    } else if (aComponent.equals(firstName)) {
                        return SOSIAID;
                    } else if (aComponent.equals(SOSIAID)) {
                        return addDateTo;
                    } else if (aComponent.equals(addDateTo)) {
                        return addDateFrom;
                    } else if (aComponent.equals(addDateFrom)) {
                        if(1 < shop.getItemCount())
			{
				return	shop;
			}
			else
			{
				return searchButton;
			}
                    }
                    if (aComponent.equals(shop)) {
                        return searchButton;
                    }

                    return getDefaultComponent();
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return getDefaultComponent();
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return mobileMembers;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return getDefaultComponent();
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
			return getDefaultComponent();
		}
		
		public Component getDefaultComponent()
		{
			if(1 < shop.getItemCount())
			{
				return	shop;
			}
			else
			{
				return addDateFrom;
			}
		}
	}
        
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		mobileMembers.getColumnModel().getColumn(0).setPreferredWidth(40);
		mobileMembers.getColumnModel().getColumn(2).setPreferredWidth(90);
		mobileMembers.getColumnModel().getColumn(3).setPreferredWidth(50);
		mobileMembers.getColumnModel().getColumn(4).setPreferredWidth(60);
		mobileMembers.getColumnModel().getColumn(5).setPreferredWidth(60);
		mobileMembers.getColumnModel().getColumn(6).setPreferredWidth(60);
	}

	/**
	 * ケータイ会員一覧テーブル用のTableCellRenderer
	 */
	public class TableCellRenderer extends SelectTableCellRenderer
	{
            /** Creates a new instance of TableCellRenderer */
            public TableCellRenderer()
            {
                super();
            }

            
            /**
             * テーブルセルレンダリングを返します。
             * @param table JTable
             * @param value セルに割り当てる値
             * @param isSelected セルが選択されている場合は true
             * @param hasFocus フォーカスがある場合は true
             * @param row 行
             * @param column 列
             * @return テーブルセルレンダリング
             */
            public Component getTableCellRendererComponent(JTable table, Object value, 
                 boolean isSelected, boolean hasFocus, int row, int column){ 

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                                super.setHorizontalAlignment(SwingConstants.CENTER);
                
                switch(column)
                {
                        case 0:
                        case 1:
                        case 4:
                                super.setHorizontalAlignment(SwingConstants.CENTER);
                                break;
                        default:
                                super.setHorizontalAlignment(SwingConstants.LEFT);
                                break;
                }
                
                return this; 
            } 

	
        }
}

/*
 * StaffWorkRegistrationPanel.java
 *
 * Created on 2008/09/16, 11:49
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.CheckUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import javax.swing.JTable;
import java.util.Date;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.text.PlainDocument;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.util.Vector;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.CustomFilter;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.master.company.MstShop;

/**
 *
 * @author shiera.delusa
 */
public class StaffWorkRegistrationPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    final static protected int NO_WORK_STATUS = 1;
    final static protected int NO_STAFF_INFO = 2;
    
    private MstShop shopInfo = null;
    protected ArrayList<StaffWorkStatusBean> workStatusList;
    protected ArrayList<StaffBasicInfo> staffBasicInfoList;
    protected ArrayList<StaffWorkRegistrationBean> registeredStaffList = new ArrayList<StaffWorkRegistrationBean>();
    boolean staffNameListInitialized = false;
    boolean staffIdChanged = false;
    
    private LocalFocusTraversalPolicy   ftp;
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
        return  ftp;
    }
    
    /** Creates new form StaffWorkRegistrationPanel */
    public StaffWorkRegistrationPanel() {
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        setKeyListener();
        
        this.setSize(500, 480);
        this.setPath("基本設定");
        this.setTitle("出勤登録");
        
        SystemInfo.addMouseCursorChange(registerButton);
        
        SystemInfo.initGroupShopComponents( this.shopNameCombo, 2 );
        this.shopInfo = (MstShop)this.shopNameCombo.getSelectedItem();
        this.initDisplay();
    }
    
    private void setKeyListener() {
        shopNameCombo.addKeyListener(SystemInfo.getMoveNextField());
        workDayCalendar.addKeyListener(SystemInfo.getMoveNextField());
        staffIdInput.addKeyListener(SystemInfo.getMoveNextField());
        workStatusComboBox.addKeyListener(SystemInfo.getMoveNextField());
        registerButton.addKeyListener(SystemInfo.getMoveNextField());
        shopNameCombo.addFocusListener(SystemInfo.getSelectText());
        workDayCalendar.addFocusListener(SystemInfo.getSelectText());
        staffIdInput.addFocusListener(SystemInfo.getSelectText());
        workStatusComboBox.addFocusListener(SystemInfo.getSelectText());
        registerButton.addFocusListener(SystemInfo.getSelectText());
    }
    
    protected void initDisplay() {
        //work status combo box
        this.initWorkStatusComboBox();
        this.initStaffListCombobox();
        this.workDayCalendar.setDate( new Date() );
        
        //disable cell selection of the table
        this.registeredStaffTable.setColumnSelectionAllowed( false );
        // enable single row selection only
        this.registeredStaffTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION  );
        // 列の並び替え禁止
        registeredStaffTable.getTableHeader().setReorderingAllowed(false);
        // ヘッダのレンダラを設定
        SwingUtil.setJTableHeaderRenderer(registeredStaffTable,
                SystemInfo.getTableHeaderRenderer());
        this.updateRegisteredStaffTable();
    }
    
    protected void initStaffListCombobox() {
        
        this.staffBasicInfoList = StaffWorkRegistrationDAO.getStaffList( this.shopInfo.getShopID(), true );
        
        if ( this.staffBasicInfoList != null ) {
            
            if ( this.staffBasicInfoList.isEmpty() == false ) {
                
                int size = this.staffBasicInfoList.size();
                String[] items = new String[size];
                String temp = null;
                
                for ( int index = 0; index < size; index++ ) {
                    items[index] = staffBasicInfoList.get( index ).getStaffName();
                }
                
                DefaultComboBoxModel staffListModel = new DefaultComboBoxModel(items);
                this.staffNameComboBox.setModel( staffListModel );
                this.staffNameComboBox.setSelectedIndex( -1 );
                staffNameListInitialized = true;
            }
        }
    }
    
    protected void initWorkStatusComboBox() {

        this.workStatusList = StaffWorkRegistrationDAO.getAllStaffWorkStatus();

        if ( this.workStatusList != null ) {
            
            if ( this.workStatusList.isEmpty() == false ) {
                
                int size = this.workStatusList.size();
                String[] items = new String[size];
                
                for ( int index=0; index < size; index++ ) {
                    items[index] = (this.workStatusList.get(index)).getDispName();
                }

                DefaultComboBoxModel workListModel = new DefaultComboBoxModel(items);
                this.workStatusComboBox.setModel( workListModel );
                this.workStatusComboBox.setSelectedIndex( -1 );
            }
        }
    }
    
    protected boolean isRegistered( StaffWorkRegistrationBean staffInfo ) {

        StaffWorkRegistrationBean tempBean = null;
        
        for ( int index = 0; index < this.registeredStaffList.size(); index++ ) {
            tempBean = this.registeredStaffList.get( index );
            
            if ( tempBean.getStaffId() == staffInfo.getStaffId() ) {
                return true;
            }
        }
        return false;
    }
    
    protected void registerWorkStatus() {
        
        StaffWorkRegistrationBean toRegister = getRegistrationInfo();
        
        if ( toRegister != null ) {
            
            toRegister.setShopId(this.shopInfo.getShopID());
            
            if ( isRegistered( toRegister ) ) {
                StaffWorkRegistrationDAO.updateWorkStatus( toRegister );
            } else {
                StaffWorkRegistrationDAO.registerWorkStatus( toRegister );
            }
        }
        
        this.updateRegisteredStaffTable();
        this.setSelectedTableItem( toRegister );
    }
    
    protected void updateRegisteredStaffTable() {

        if ( registeredStaffList == null ) {
            return;
        }
        
        if ( registeredStaffList.isEmpty() == false ) {
            this.registeredStaffList.clear();
        }

        this.registeredStaffList = StaffWorkRegistrationDAO.getRegisteredStaffs( this.workDayCalendar.getDate(), this.shopInfo.getShopID() );
        
        //Clear the existing table first
        DefaultTableModel tableModel =(DefaultTableModel) this.registeredStaffTable.getModel();
        while( tableModel.getRowCount() > 0 ) {
            tableModel.removeRow( 0 );
        }
        
        if ( registeredStaffList != null ) {
            if ( registeredStaffList.isEmpty() == false ) {
                for ( int index = 0; index < registeredStaffList.size(); index++ ) {
                    this.addRegisteredStaffTableRow( registeredStaffList.get(index), index );
                }
            }
        }
        //working staffs total
        int workingTotal =
                StaffWorkRegistrationDAO.getWorkingStaffsTotal( this.workDayCalendar.getDate(),
                this.shopInfo.getShopID() );
        this.workingStaffTotalField.setText( String.valueOf( workingTotal ) );
    }
    
    protected void addRegisteredStaffTableRow( StaffWorkRegistrationBean rowData, int rowNum ) {
        
        DefaultTableModel tableModel = (DefaultTableModel) this.registeredStaffTable.getModel();
        
        tableModel.addRow( new Vector() );
        
        for ( int colIndex=0; colIndex < this.registeredStaffTable.getColumnCount(); colIndex++ ) {
            this.registeredStaffTable.setValueAt( rowData.getStaffNo(), rowNum, colIndex++ );
            this.registeredStaffTable.setValueAt( rowData.getStaffName(), rowNum, colIndex++ );
            this.registeredStaffTable.setValueAt( rowData.getWorkStatus().getDispName(), rowNum, colIndex++ );
        }
    }
    
    protected StaffWorkRegistrationBean getRegistrationInfo() {
        
        int index = -1;
        StaffWorkRegistrationBean registrationInfo = new StaffWorkRegistrationBean();
        
        if ( this.staffNameComboBox.getSelectedIndex() < 0 ) {
            this.displayMessage( this.NO_STAFF_INFO );
            return null;
        }
        
        if ( this.workStatusComboBox.getSelectedIndex() < 0 ) {
            this.displayMessage( this.NO_WORK_STATUS );
            return null;
        }
        
        //set the working date
        registrationInfo.setWorkingDate( this.workDayCalendar.getDate() );
        
        //set the staff basic info
        index = this.staffNameComboBox.getSelectedIndex();
        registrationInfo.setBasicInfo( this.staffBasicInfoList.get( index ) );
        
        //set the work status
        index = this.workStatusComboBox.getSelectedIndex();
        registrationInfo.setWorkStatus( this.workStatusList.get( index ) );
        
        return registrationInfo;
    }
    
    protected void changeSelectedById( int staffId ) {
        String selectedName = findName(staffId);
        if( selectedName != null ) {
            this.staffNameComboBox.setSelectedItem( selectedName );
        } else {
            this.staffNameComboBox.setSelectedIndex( -1 );
        }
    }
    
    protected String findName( int staffId ) {
        String name = null;
        
        for( int index=0; index < this.staffBasicInfoList.size(); index++ ) {
            if( (this.staffBasicInfoList.get(index)).getStaffId() == staffId ) {
                name = staffBasicInfoList.get(index).getStaffName();
                break;
            }
        }
        return name;
    }
    
    private void displayMessage( int msgType ) {
        switch( msgType ) {
            case NO_WORK_STATUS:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "状態を選んでください。",
                        JOptionPane.ERROR_MESSAGE );
                break;
            }
            case NO_STAFF_INFO:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "担当者の情報を入れてください。",
                        JOptionPane.ERROR_MESSAGE );
                break;
            }
            
            default:
                break;
        }
    }
    
    private void updateStaffInputFields() {
        
        if( this.staffNameListInitialized && this.staffIdChanged == false ) {
            if( staffBasicInfoList != null ) {
                int selectedIdx = this.staffNameComboBox.getSelectedIndex();
                if( selectedIdx < 0 ) {
                    this.staffIdInput.setText( "" );
                    return;
                } else {
                    if( this.staffBasicInfoList.get(selectedIdx) != null ) {
                        String staffId = this.staffBasicInfoList.get(selectedIdx).getStaffNo();
                        this.staffIdInput.setText( staffId );
                    }
                }
            }
        } else {
            staffIdChanged = false;
        }
    }
    
    protected void clearComboBoxes() {
        DefaultComboBoxModel model = (DefaultComboBoxModel)this.staffNameComboBox.getModel();
        model.removeAllElements();
        
        this.staffIdInput.setText("");
    }
    
    private void updateInputFields() {
        int selectedRow = this.registeredStaffTable.getSelectedRow();
        StaffWorkRegistrationBean staffBean = this.registeredStaffList.get( selectedRow );
        String staffId =  String.valueOf( staffBean.getShopId() );
        String staffName = staffBean.getStaffName();
        this.staffIdInput.setText( staffId );
        this.staffNameComboBox.setSelectedItem( staffName );
        this.workStatusComboBox.setSelectedItem( staffBean.getWorkStatus().getDispName() );
    }
    
    private void updateInputFields( StaffWorkRegistrationBean toSelect ) {
        String staffId =  String.valueOf( toSelect.getShopId() );
        String staffName = toSelect.getStaffName();
        this.staffIdInput.setText( staffId );
        this.staffNameComboBox.setSelectedItem( staffName );
        this.workStatusComboBox.setSelectedItem( toSelect.getWorkStatus().getDispName() );
    }
    
    private void setSelectedTableItem( StaffWorkRegistrationBean toSelect ) {
        
        if (toSelect == null) return;
        
        StaffWorkRegistrationBean tempBean = null;
        int index = 0;
        
        for( index = 0; index < this.registeredStaffList.size(); index++ ) {
            tempBean = this.registeredStaffList.get( index );
            if( tempBean.getStaffId() == toSelect.getStaffId() ) {
                this.registeredStaffTable.setRowSelectionInterval( index, index );
                this.updateInputFields();
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        staffIdInput = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)staffIdInput.getDocument()).setDocumentFilter(new CustomFilter(6,CustomFilter.NUMERIC));
        jLabel3 = new javax.swing.JLabel();
        registerButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        registeredStaffTable = new javax.swing.JTable();
        registeredStaffTable = new javax.swing.JTable(new DefaultTableModel()){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        jLabel4 = new javax.swing.JLabel();
        workingStaffTotalField = new javax.swing.JFormattedTextField();
        workStatusComboBox = new javax.swing.JComboBox();
        staffNameComboBox = new javax.swing.JComboBox();
        workDayCalendar = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel5 = new javax.swing.JLabel();
        shopNameCombo = new com.geobeck.sosia.pos.swing.JComboBoxLabel();

        jLabel1.setText("\u51fa\u52e4\u65e5");

        jLabel2.setText("\u62c5\u5f53\u8005");

        staffIdInput.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                staffIdInputCaretUpdate(evt);
            }
        });
        staffIdInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                staffIdInputFocusLost(evt);
            }
        });

        jLabel3.setText("\u72b6\u614b");

        registerButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg" ));
        registerButton.setAlignmentY(0.0F);
        registerButton.setBorder(null);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setIconTextGap(0);
        registerButton.setMargin(new Insets( 0,0,0,0 ));
        registerButton.setMinimumSize(new java.awt.Dimension(0, 0));
        registerButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg" ));
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        registeredStaffTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "スタッフNo.", "スタッフ名", "状態"
            }
        ));
        registeredStaffTable.setAlignmentX(JTable.CENTER_ALIGNMENT);
        registeredStaffTable.setAlignmentY(JTable.CENTER_ALIGNMENT);
        registeredStaffTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                registeredStaffTableMousePressed(evt);
            }
        });
        registeredStaffTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                registeredStaffTableKeyReleased(evt);
            }
        });

        jScrollPane1.setViewportView(registeredStaffTable);

        jLabel4.setText("\u51fa\u52e4\u4eba\u6570");

        workingStaffTotalField.setEditable(false);
        workingStaffTotalField.setDisabledTextColor(Color.WHITE);

        workStatusComboBox.setMaximumRowCount(11);

        staffNameComboBox.setMaximumRowCount(12);
        staffNameComboBox.setFocusable(false);
        staffNameComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                staffNameComboBoxMouseReleased(evt);
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

        workDayCalendar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                workDayCalendarItemStateChanged(evt);
            }
        });
        workDayCalendar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                workDayCalendarFocusGained(evt);
            }
        });

        jLabel5.setText("\u5e97\u8217");

        shopNameCombo.setMaximumRowCount(12);
        shopNameCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopNameComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(36, 36, 36)
                        .addComponent(shopNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(workingStaffTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(28, 28, 28)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(workDayCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(staffIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(staffNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(workStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(shopNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(workDayCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(staffNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(workStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(workingStaffTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void workDayCalendarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_workDayCalendarItemStateChanged
        this.updateRegisteredStaffTable();
    }//GEN-LAST:event_workDayCalendarItemStateChanged

    private void workDayCalendarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_workDayCalendarFocusGained
        workDayCalendar.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_workDayCalendarFocusGained

	private void staffIdInputFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_staffIdInputFocusLost
	{//GEN-HEADEREND:event_staffIdInputFocusLost
//		MstStaff staff;
		ArrayList<StaffBasicInfo> list;
		int i;

		if( CheckUtil.isNumber(staffIdInput.getText()) )
		{
			list = StaffWorkRegistrationDAO.getStaffList(shopInfo.getShopID(), true);
			for (i = 0; i < list.size(); ++i)
			{
				if (staffIdInput.getText().equals(list.get(i).getStaffNo()))
				{
					staffNameComboBox.setSelectedIndex(i);
					break;
				}
			}
			if (i >= list.size())
			{
				staffIdInput.setText("");
				staffNameComboBox.setSelectedIndex(-1);
			}
		}
		else
		{
			staffIdInput.setText("");
			staffNameComboBox.setSelectedIndex(-1);
		}
		
		this.getFocusTraversalPolicy().getComponentAfter(this,staffIdInput);

	}//GEN-LAST:event_staffIdInputFocusLost
    
    private void registeredStaffTableKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_registeredStaffTableKeyReleased
    {//GEN-HEADEREND:event_registeredStaffTableKeyReleased
        this.updateInputFields();
    }//GEN-LAST:event_registeredStaffTableKeyReleased
    
    private void registeredStaffTableMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_registeredStaffTableMousePressed
    {//GEN-HEADEREND:event_registeredStaffTableMousePressed
        this.updateInputFields();
    }//GEN-LAST:event_registeredStaffTableMousePressed
    
    private void shopNameComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shopNameComboActionPerformed
    {//GEN-HEADEREND:event_shopNameComboActionPerformed
        this.shopInfo = (MstShop)this.shopNameCombo.getSelectedItem();
        this.clearComboBoxes();
        this.initDisplay();
    }//GEN-LAST:event_shopNameComboActionPerformed
    
    private void staffNameComboBoxMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_staffNameComboBoxMouseReleased
    {//GEN-HEADEREND:event_staffNameComboBoxMouseReleased
        this.updateStaffInputFields();
    }//GEN-LAST:event_staffNameComboBoxMouseReleased
    
    private void staffNameComboBoxKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_staffNameComboBoxKeyReleased
    {//GEN-HEADEREND:event_staffNameComboBoxKeyReleased
        this.updateStaffInputFields();
    }//GEN-LAST:event_staffNameComboBoxKeyReleased
        
    private void staffIdInputCaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_staffIdInputCaretUpdate
    {//GEN-HEADEREND:event_staffIdInputCaretUpdate
        /*String idInput = this.staffIdInput.getText();
        staffIdChanged = true;
        if( idInput.length() > 0 ) {
            this.changeSelectedById( Integer.parseInt( idInput ) );
        } else {
            this.staffNameComboBox.setSelectedIndex( -1 );
        }
	*/
    }//GEN-LAST:event_staffIdInputCaretUpdate
    
    private void staffNameComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_staffNameComboBoxActionPerformed
    {//GEN-HEADEREND:event_staffNameComboBoxActionPerformed
        this.updateStaffInputFields();
    }//GEN-LAST:event_staffNameComboBoxActionPerformed
    
    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registerButtonActionPerformed
    {//GEN-HEADEREND:event_registerButtonActionPerformed
        this.registerWorkStatus();
    }//GEN-LAST:event_registerButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton registerButton;
    private javax.swing.JTable registeredStaffTable;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shopNameCombo;
    private com.geobeck.swing.JFormattedTextFieldEx staffIdInput;
    private javax.swing.JComboBox staffNameComboBox;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo workDayCalendar;
    private javax.swing.JComboBox workStatusComboBox;
    private javax.swing.JFormattedTextField workingStaffTotalField;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
            extends FocusTraversalPolicy {
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy() {
            controls.add(shopNameCombo);
            controls.add(workDayCalendar);
            controls.add(staffIdInput);
            controls.add(workStatusComboBox);
            controls.add(registerButton);
            controls.add(shopNameCombo);     // 最後に先頭を再度登録
        };
        
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         * !!先頭と最後のコントロールが同時に仕様不可になる画面ではバグるので注意
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for(Component co : controls){
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         * !!先頭と最後のコントロールが同時に仕様不可になる画面ではバグるので注意
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for( int ii = controls.size(); ii>0; ii-- ){
                Component co = controls.get(ii-1);
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * トラバーサルサイクルの最初の Component を返します。
         */
        public Component getFirstComponent(Container aContainer) {
            return controls.get(0);
        }
        
        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer) {
            return controls.get(controls.size()-2);
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer) {
            return controls.get(0);
        }
        
        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
         */
        public Component getInitialComponent(Window window) {
            return controls.get(0);
        }
    }
    
}

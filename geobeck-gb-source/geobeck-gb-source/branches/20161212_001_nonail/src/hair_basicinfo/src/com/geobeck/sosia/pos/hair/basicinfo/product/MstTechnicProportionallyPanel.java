/*
 * MstTechnicProportionallyPanel.java
 *
 * Created on 2007/08/16, 15:54
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 *
 * @author  kanemoto
 */
public class MstTechnicProportionallyPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private MstTechnicClasses mtcs = new MstTechnicClasses();			    // 技術区分マスタ
    private MstTechnicClass mts = new MstTechnicClass();			    // 技術マスタ
    private ArrayList<DataProportionally> dps =	new ArrayList<DataProportionally>();   // 按分データ
    private MstProportionallys mps = new MstProportionallys();			    // 按分マスタ
    private Integer selIndex = -1;
    //nhanvt start add 20141020 Bug #31557
    boolean isTest = false;
    //nhanvt end add 20141020 Bug #31557
    /** Creates new form MstTechnicProportionallyPanel */
    public MstTechnicProportionallyPanel() {
	
	initComponents();
	this.setSize(833, 691);
	this.setPath("基本設定 >> 技術マスタ");
	this.setTitle("按分登録");
	this.init();
	
	this.backButton.setVisible(false);

	refreshTechnicComboBox();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        proportionallyDataPane = new javax.swing.JScrollPane();
        proportionallyDataTable = new com.geobeck.swing.JTableEx();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        try
        {
            ConnectionWrapper	con	=	SystemInfo.getConnection();

            mtcs.load(con);

            con.close();
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        technicClass = new JComboBox(mtcs.toArray());
        jLabel2 = new javax.swing.JLabel();
        technic = new javax.swing.JComboBox();
        backButton = new javax.swing.JButton();
        mst_proportionallyScrollPane = new javax.swing.JScrollPane();
        mst_proportionally = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        renewButton = new javax.swing.JButton();

        proportionallyDataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "按分名", "ポイント", "割合", "", "削除"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        proportionallyDataTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        proportionallyDataTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
        proportionallyDataTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        proportionallyDataTable.getTableHeader().setReorderingAllowed(false);
        this.initPropotionallyDataTable();
        proportionallyDataTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        TableColumnModel proportionallyDataTableModel = proportionallyDataTable.getColumnModel();
        proportionallyDataTableModel.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        SwingUtil.setJTableHeaderRenderer(proportionallyDataTable, SystemInfo.getTableHeaderRenderer());

        proportionallyDataPane.setViewportView(proportionallyDataTable);

        jPanel1.setOpaque(false);
        jLabel1.setText("\u6280\u8853\u5206\u985e");

        technicClass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                technicClassActionPerformed(evt);
            }
        });

        jLabel2.setText("\u6280\u8853\u540d");

        technic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                technicActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(technicClass, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(technic, 0, 240, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(technicClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(technic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/back_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/back_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        mst_proportionallyScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mst_proportionally.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "按分名", "ポイント", "施術時間"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mst_proportionally.setSelectionBackground(new java.awt.Color(220, 220, 220));
        mst_proportionally.setSelectionForeground(new java.awt.Color(0, 0, 0));
        mst_proportionally.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mst_proportionally.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(mst_proportionally, SystemInfo.getTableHeaderRenderer());
        mst_proportionally.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initMstProportionallyColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(mst_proportionally);
        mst_proportionally.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mst_proportionallyMouseClicked(evt);
            }
        });

        mst_proportionallyScrollPane.setViewportView(mst_proportionally);

        jLabel3.setText("\uff1c\u6309\u5206\u4e00\u89a7\uff1e");

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setContentAreaFilled(false);
        renewButton.setEnabled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mst_proportionallyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(renewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(proportionallyDataPane, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mst_proportionallyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                    .addComponent(proportionallyDataPane, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
	this.showOpener();
    }//GEN-LAST:event_backButtonActionPerformed

    public void setBackButtonVisible(boolean aFlag) {
	this.backButton.setVisible(aFlag);
    }
    
    /**
     * }
     * 選択されている技術分類ＩＤをセットする。
     * @param technicClassID 技術分類ＩＤ
     */
    public void setSelectedTechnicClassID(Integer technicClassID)
    {
	if (technicClassID == null) return;

	for (MstTechnicClass tc : mtcs) {
	    
	    if (technicClassID == tc.getTechnicClassID()) {
		technicClass.setSelectedItem(tc);
		return;
	    }
	}
    }

    /**
     * 選択されている技術ＩＤをセットする。
     * @param technicID 技術ＩＤ
     */
    public void setSelectedTechnicID(Integer technicID)
    {
	if(technicID == null) return;
	if (technicClass.getSelectedIndex() < 0) return;

	for ( MstTechnic mt : mts ) {
	    
	    if ( technicID == mt.getTechnicID() ) {
		technic.setSelectedItem( mt );
		return;
	    }
	}
    }

    /**
     * 技術絞込み変更
     */
    private void technicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_technicActionPerformed
	renewButton.setEnabled( false );
	// 技術絞込みが変更されているかを調べる
        //nhanvt start edit 20141020 Bug #31557
	if( 0 <= technic.getSelectedIndex() && this.isTest  ) {
	    // 按分データを取得する
	    getDataProportionallys();
	}
        //nhanvt start edit 20141020 Bug #31557
    }//GEN-LAST:event_technicActionPerformed

    /**
     * 技術区分絞込み変更
     */
    private void technicClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_technicClassActionPerformed
	// 技術絞込みリストを更新する
	refreshTechnicComboBox();
    }//GEN-LAST:event_technicClassActionPerformed

    /**
     * 按分データ按分変更
     */
    private void proportionallyNameActionPerformed(java.awt.event.ActionEvent evt) {
	int rIndex = proportionallyDataTable.getSelectedRow();
	int cIndex = proportionallyDataTable.getSelectedColumn();
	DataProportionally dp = dps.get( rIndex );
	JComboBox jb =  (JComboBox)proportionallyDataTable.getValueAt( rIndex, cIndex );
	dp.setProportionally( (MstProportionally)jb.getSelectedItem() );
	
	proportionallyDataTable.setValueAt( dp.getProportionally().getProportionallyPoint(), rIndex, cIndex + 1 );
    }
	
    private void renewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewButtonActionPerformed
	if(this.checkInput()) {
	    if( this.regist() ) {
		// 按分データを取得する
		getDataProportionallys();
		// 按分データテーブルを再表示
		showProportionallyDataTable();
	    } else {
		MessageDialog.showMessageDialog(this,
		    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "技術按分データ"),
		    this.getTitle(),
		    JOptionPane.ERROR_MESSAGE);
	    }
	}
    }//GEN-LAST:event_renewButtonActionPerformed

    /**
     * 入力チェックを行う
     * @return 正常値にTrue
     */
    private boolean checkInput()
    {
	// 割合を登録する
	for( int i = 0; i < proportionallyDataTable.getRowCount(); i++ ) {
	    dps.get( i ).setRatio( (Integer)proportionallyDataTable.getValueAt( i, 2 ) );
	}
	return true;
    }
    
    /**
     * データを更新する
     */
    private boolean regist()
    {
	boolean result = true;

	// コネクション取得
	ConnectionWrapper con = SystemInfo.getConnection();

	// コネクションラッパーの取得に失敗
	if (con == null) {
		MessageDialog.showMessageDialog(this,
		    MessageUtil.getMessage(MessageUtil.ERROR_CONNECT_FAILED),
		    this.getTitle(),
		    JOptionPane.ERROR_MESSAGE);
		return false;
	}

	try
	{
	    con.begin();

	    // 選択中の技術に紐づく按分をすべて削除
	    con.executeUpdate(this.getDeleteSQL());

	    for ( DataProportionally dp : dps ) {
		
		if (dp.getDataProportionallyID() == null) {
		    result = dp.add(con);
		} else {
		    result = dp.update(con);
		}
		
		if (!result) break;
	    }
	    
	    if ( result ) {
		// 登録成功
		con.commit();
	    }
	    else
	    {
		// 登録失敗
		con.rollback();
		MessageDialog.showMessageDialog(this,
		MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "按分データ変更"),
		this.getTitle(),
		JOptionPane.ERROR_MESSAGE);
	    }
	}
	catch(SQLException e)
	{
	    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	}
	return result;
    }
    
    /**
     * 按分マスタテーブルのカラムサイズ設定
     */
    private void initMstProportionallyColumnWidth()
    {
	    mst_proportionally.getColumnModel().getColumn(0).setPreferredWidth(200);
	    mst_proportionally.getColumnModel().getColumn(1).setPreferredWidth(50);
    }
    
    /**
     * 按分データテーブルのカラムサイズ設定
     */
    private void initPropotionallyDataTable()
    {
	    proportionallyDataTable.getColumnModel().getColumn(0).setPreferredWidth(200);
	    proportionallyDataTable.getColumnModel().getColumn(1).setPreferredWidth(85);
	    proportionallyDataTable.getColumnModel().getColumn(2).setPreferredWidth(85);
	    proportionallyDataTable.getColumnModel().getColumn(3).setPreferredWidth(45);
	    proportionallyDataTable.getColumnModel().getColumn(4).setPreferredWidth(60);
    }
    
    private void mst_proportionallyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mst_proportionallyMouseClicked

	// ダブルクリック以外は処理を抜ける
	if (evt.getClickCount() != 2) return;
	// 按分一覧で未選択の場合は処理を抜ける
	if (mst_proportionally.getSelectedRow() < 0) return;
	// 技術名コンボが未選択の場合は処理を抜ける
	if (technic.getSelectedIndex() < 0) return;

	DataProportionally dp = new DataProportionally();

	// 技術データ
	dp.setTechnic( mts.get( technic.getSelectedIndex() ) );
	// 按分データ
	dp.setProportionally( this.mps.get( mst_proportionally.getSelectedRow() ) );
	// 割合データ
	dp.setRatio( new Integer( 0 ) );
	this.addDataProportionally( dp );
		
    }//GEN-LAST:event_mst_proportionallyMouseClicked
    
    /**
     * 初期化を行う
     */
    private void init()
    {
	    // 按分マスタを取得する
	    getMstProportionallys();
    }

    /**
     * 按分マスタを取得する
     */
    private void getMstProportionallys() {
	    try
	    {
		ConnectionWrapper con = SystemInfo.getConnection();
		mps.load(con);
	    }
	    catch(Exception e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	    // 按分マスタテーブルを作成する
	    showMstProportionally();
    }
	
    /**
     * 按分マスタテーブルを作成する
     */
    private void showMstProportionally() {
	
	DefaultTableModel model = (DefaultTableModel)mst_proportionally.getModel();

	//全行削除
	model.setRowCount(0);
	mst_proportionally.removeAll();

	for( MstProportionally mp : mps) {
	    Object[]	rowData = {
		mp.getProportionallyName(),
		mp.getProportionallyPoint(),
		mp.getProportionallyTechnicTime(),
	    };
	    model.addRow(rowData);
	}
    }
    
    /**
     * 技術絞込みリストを更新する
     */
    private void refreshTechnicComboBox()
    {
	// 技術マスタ
	MstTechnicClass mtc = new MstTechnicClass();
	
	// 全ての内容を削除
	technic.removeAllItems();
        //nhanvt start edit 20141020 Bug #31557
	isTest = false;
	if (0 <= technicClass.getSelectedIndex()) {
	    // 技術データを取得する
	    this.getTechnicData();
	    for ( MstTechnic mt : mts ) {
		technic.addItem( mt ); 
	    }
	}
	isTest = true;
        //nhanvt end edit 20141020 Bug #31557
	//入力をクリアする
	if (technic.getItemCount() > 0) {
	    technic.setSelectedIndex( 0 );
	} else {
	    technic.setSelectedIndex( -1 );
	}
	// フォーカスを取得
	technic.requestFocusInWindow();
    }

    /**
     * 技術データを更新する
     */
    private void getTechnicData() {
	
	mts.clear();

	if (technicClass.getSelectedIndex() < 0) return;

	mts = (MstTechnicClass)technicClass.getSelectedItem();
	
	//技術マスタをデータベースから読み込む
	try
	{
	    ConnectionWrapper con = SystemInfo.getConnection();
	    mts.loadTechnic(con);
	}
	catch(Exception e)
	{
	    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	}
    }
    
    /**
     * 按分データを取得する
     */
    private void getDataProportionallys() {
	
	    ResultSetWrapper rs;
	    dps.clear();
	    
	    try
	    {
		rs = SystemInfo.getConnection().executeQuery(this.getDataProportionallySQL());
		
		while (rs.next()) {
		    DataProportionally dp = new DataProportionally();
		    dp.setData( rs );
		    // 選択中の技術ID絞込み
                    //nhanvt start edit 20141020 Bug #31557
//		    if( mts.get( technic.getSelectedIndex() ).getTechnicID().compareTo(dp.getTechnic().getTechnicID())==0)
//		    {
			dps.add( dp );
//		    }
                    //nhanvt end edit 20141020 Bug #31557
		}
                System.out.println("dps size : " + dps.size());
		rs.close();
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	    // 按分データをテーブルに登録する
	    showProportionallyDataTable();
    }

    /**
     * 按分データテーブルを表示
     */
    private void showProportionallyDataTable() {
	
	DefaultTableModel model = (DefaultTableModel)proportionallyDataTable.getModel();

	//全行削除
	if ( proportionallyDataTable.getCellEditor() != null ) proportionallyDataTable.getCellEditor().stopCellEditing();
	model.setRowCount(0);
	proportionallyDataTable.removeAll();

	for ( DataProportionally dp : dps) {
	    addProportionallyTableRow( dp );
	}

	renewButton.setEnabled(true);
    }

    /**
     * 按分データテーブルにレコードを追加する
     */
    private void addProportionallyTableRow( DataProportionally dp )
    {
	    DefaultTableModel model = (DefaultTableModel)proportionallyDataTable.getModel();
	    
	    //-----------------------------
	    // 按分削除ボタン作成
	    //-----------------------------
	    javax.swing.JButton button = new javax.swing.JButton();
	    button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
	    		    "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_off.jpg")));
	    button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
	    		    "/images/" + SystemInfo.getSkinPackage() + "/button/common/trash_on.jpg")));
	    button.setBorderPainted(false);
	    button.setContentAreaFilled(false);
	    
	    button.addActionListener(new java.awt.event.ActionListener()
	    {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    // 按分データレコード削除
		    deleteProportionallyData();
		}
	    });

	    //-----------------------------
	    // 行追加
	    //-----------------------------	    
	    Object[] rowData = {
		    dp.getProportionally(),
		    dp.getProportionally().getProportionallyPoint(),
		    dp.getRatio(),
		    null,
		    button
	    };
	    model.addRow(rowData);
    }

    /**
     * 指定按分データを登録する
     */
    private void addDataProportionally( DataProportionally dp )
    {
	if ( 0 <= mst_proportionally.getSelectedRow() ) {
	    
	    dps.add( dp );
	    
	    // 按分データテーブルを再表示
	    showProportionallyDataTable();
	}
    }
    
    /**
     * 指定按分データを削除する
     */
    private void deleteProportionallyData()
    {
	if ( 0 <= proportionallyDataTable.getSelectedRow() ) {

	    dps.remove(proportionallyDataTable.getSelectedRow());

	    // 按分データテーブルを再表示
	    showProportionallyDataTable();
	}
    }
    
    /**
     * 按分データを取得するＳＱＬ文を取得する。
     * @return 按分データを取得するＳＱＬ文
     */
    private String getDataProportionallySQL() {
	return
	    "select\n" +
	    "dp.data_proportionally_id,\n" +
	    "mtc.technic_class_id,\n" +
	    "mtc.technic_class_name,\n" +
	    "mtc.display_seq as \"technic_class_display_seq\",\n" +
	    "mt.technic_id,\n" +
	    "mt.technic_no,\n" +
	    "mt.technic_name,\n" +
	    "mt.price,\n" +
	    "mt.operation_time,\n" +
	    "mt.display_seq as \"technic_display_seq\",\n" +
	    "mp.proportionally_id,\n" +
	    "mp.proportionally_name,\n" +
	    "mp.proportionally_point,\n" +
	    "mp.display_seq as \"proportionally_display_seq\",\n" +
	    "dp.proportionally_ratio\n" +
	    "from\n" +
	    "data_proportionally as dp,\n" +
	    "mst_technic as mt,\n" +
	    "mst_technic_class as mtc,\n" +
	    "mst_proportionally as mp\n" +
	    "where\n" +
	    "dp.delete_date is null\n" +
	    "and\n" +
	    "mt.delete_date is null\n" +
	    "and\n" +
	    "mtc.delete_date is null\n" +
	    "and\n" +
	    "mp.delete_date is null\n" +
	    "and\n" +
	    "dp.technic_id = mt.technic_id\n" +
             //nhanvt start add 20141020 Bug #31557
                " and dp.technic_id =  " + mts.get( technic.getSelectedIndex() ).getTechnicID() + "" +
              //nhanvt end add 20141020 Bug #31557
                
	    "and\n" +
	    "mt.technic_class_id = mtc.technic_class_id\n" +
	    "and\n" +
	    "dp.proportionally_id = mp.proportionally_id\n" +
             
	    "order by\n" +
	    "mtc.display_seq, mt.display_seq, mp.display_seq, dp.data_proportionally_id\n ";
    }
    
    /**
     * 按分データを削除するＳＱＬ文を取得する。
     * @return 按分データを削除するＳＱＬ文
     */
    private String getDeleteSQL()
    {
	    return
		    "update data_proportionally\n" +
		    "set delete_date = current_timestamp\n" +
		    "where delete_date is null \n" +
		    "  and technic_id = " + SQLUtil.convertForSQL( mts.get( technic.getSelectedIndex() ).getTechnicID() ) + ";\n";
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable mst_proportionally;
    private javax.swing.JScrollPane mst_proportionallyScrollPane;
    private javax.swing.JScrollPane proportionallyDataPane;
    private com.geobeck.swing.JTableEx proportionallyDataTable;
    private javax.swing.JButton renewButton;
    private javax.swing.JComboBox technic;
    private javax.swing.JComboBox technicClass;
    // End of variables declaration//GEN-END:variables
    
}

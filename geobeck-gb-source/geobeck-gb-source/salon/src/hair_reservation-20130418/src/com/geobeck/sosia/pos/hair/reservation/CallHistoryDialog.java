/*
 * CallHistoryDialog.java
 *
 * Created on 2006/04/20, 14:13
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.cti.CTICustomerDialog;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.swing.AbstractMainFrame;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;

/**
 * 電話着信履歴
 * 
 * @author geobeck
 */
public class CallHistoryDialog extends javax.swing.JDialog
{
        private MstShop target = null;
        private AbstractMainFrame parentFrame = null;
        
	/**
	 * コンストラクタ
	 * @param parent 
	 * @param modal 
	 */
	public CallHistoryDialog(java.awt.Frame parentFrame, MstShop target)
	{       
            super(parentFrame, true);
            this.parentFrame = (AbstractMainFrame)parentFrame;
            this.target = target;
            init();
	}
    
        // 共通初期化処理
        private void init()
        {
            initComponents();
            addMouseCursorChange();
            this.shop.addItem(this.target);
            this.setListener();
            SwingUtil.moveCenter(this);

            this.deleteOldData();
            this.showData();
        }

        private void deleteOldData() {

            try {

                StringBuilder sql = new StringBuilder(1000);

                sql.append(" delete from data_call_history");
                sql.append(" where");
                sql.append("     shop_id = " + SQLUtil.convertForSQL(this.target.getShopID()));
                sql.append(" and arrive_time < date_trunc('day', current_timestamp)");

                SystemInfo.getConnection().execute(sql.toString());

            } catch (Exception ignore) {
            }
        }

	private void showData()
	{
	    SwingUtil.clearTable(searchResult);

	    DefaultTableModel model = (DefaultTableModel)searchResult.getModel();

	    ConnectionWrapper con = SystemInfo.getConnection();

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      to_char(a.arrive_time, 'hh24:mi') as arrive_time");
            sql.append("     ,a.arrive_number");
            sql.append("     ,b.*");
            sql.append("     ,(");
            sql.append("         select");
            sql.append("             to_char(drd.reservation_datetime, 'yyyy/mm/dd hh24:mi')");
            sql.append("         from");
            sql.append("             data_reservation dr");
            sql.append("                 inner join data_reservation_detail drd");
            sql.append("                     using(shop_id, reservation_no)");
            sql.append("         where");
            sql.append("                 dr.delete_date is null");
            sql.append("             and drd.delete_date is null");
            sql.append("             and dr.customer_id = b.customer_id");
            sql.append("             and drd.reservation_datetime > current_timestamp");
            sql.append("         order by");
            sql.append("             drd.reservation_datetime");
            sql.append("         limit 1");
            sql.append("      ) as next_reserve_date");
            sql.append(" from");
            sql.append("     data_call_history a");
            sql.append("         left join mst_customer b");
            sql.append("                on a.arrive_number in (phone_number, cellular_number)");
            sql.append("               and b.delete_date is null");
            sql.append(" where");
            sql.append("     a.shop_id = " + SQLUtil.convertForSQL(this.target.getShopID()));
            sql.append(" order by");
            sql.append("      arrive_time");
            sql.append("     ,b.customer_id");

	    try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		ResultSetWrapper rs = con.executeQuery(sql.toString());

                int idx = 0;

		while (rs.next()) {

		    MstCustomer mc = new MstCustomer();
		    mc.setData(rs);
                    String ctiNo = rs.getString("arrive_number");

		    Object[] rowData = {
                        ++idx,
                        rs.getString("arrive_time"),
                        ctiNo,
                        getUserSearchButton(mc.getCustomerID(), mc.getCustomerNo()),
                        mc.getCustomerNo(),
                        mc.getFullCustomerName(),
                        rs.getString("next_reserve_date"),
                        getCtiButton(ctiNo, mc.getCustomerID())
                    };
		    model.addRow(rowData);
		}

		rs.close();

                searchResult.requestFocusInWindow();
                searchResult.changeSelection(0, 1, false, false);

	    } catch(SQLException e) {

		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);

            } finally {

                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }

	}

        /**
	 * ユーザ検索ボタンを取得する
	 */
	private JButton getUserSearchButton(final Integer customerID, final String customerNo)
	{
		JButton		button	=	new JButton();
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
		button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));
		button.setSize(48, 25);
		button.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            MstCustomerPanel mcp = null;
                            
                            try {

                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                mcp = new MstCustomerPanel(customerID, true, true);
                                SwingUtil.openAnchorDialog( (JFrame)null, true, mcp, "顧客情報", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                            
                            mcp = null;
                        }
		});

                button.setEnabled(customerNo != null);

		return button;
	}

        /**
	 * CTIボタンを取得する
	 */
	private JButton getCtiButton(final String ctiNo, final Integer customerID)
	{
		JButton button = new JButton();
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/cti_display_off.jpg")));
		button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/cti_display_on.jpg")));
		button.setSize(48, 25);
		button.addActionListener(new java.awt.event.ActionListener()
		{
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        showCTI(ctiNo, customerID);
                    }
		});

		return button;
	}

        private void showCTI(String ctiNo, Integer customerID) {
            this.setVisible(false);
            CTICustomerDialog ccd = new CTICustomerDialog(ctiNo, this.parentFrame, customerID);
        }

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backPanel = new com.geobeck.swing.ImagePanel();
        searchResultScrollPane = new javax.swing.JScrollPane();
        searchResult = new com.geobeck.swing.JTableEx();
        closeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel2 = new javax.swing.JLabel();

        setTitle("電話着信履歴");
        setName("searchCustomerFrame"); // NOI18N

        backPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
        backPanel.setRepeat(true);

        searchResultScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        searchResultScrollPane.setFocusable(false);

        searchResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "着信時間", "着信電話番号", "顧客情報", "顧客No.", "顧客名", "次回予約日時", "画面表示"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchResult.setSelectionBackground(new java.awt.Color(255, 210, 142));
        searchResult.setSelectionForeground(new java.awt.Color(0, 0, 0));
        searchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResult.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(searchResult, SystemInfo.getTableHeaderRenderer());
        searchResult.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        searchResult.setDefaultRenderer(String.class, new TableCellAlignRenderer());
        searchResultScrollPane.setViewportView(searchResult);

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setFocusable(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backPrevious(evt);
            }
        });

        jLabel1.setText("店舗");

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setText("※ 着信履歴は当日分のみ表示されます。");

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, searchResultScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .add(18, 18, 18)
                        .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 360, Short.MAX_VALUE)
                        .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 223, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel1))
                    .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void backPrevious(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backPrevious
	{//GEN-HEADEREND:event_backPrevious
            this.setVisible(false);
	}//GEN-LAST:event_backPrevious
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private com.geobeck.swing.JTableEx searchResult;
    private javax.swing.JScrollPane searchResultScrollPane;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{  
            SystemInfo.addMouseCursorChange(closeButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
	}
	
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
            searchResult.getColumnModel().getColumn(0).setPreferredWidth(40);
            searchResult.getColumnModel().getColumn(1).setPreferredWidth(70);
            searchResult.getColumnModel().getColumn(2).setPreferredWidth(90);
            searchResult.getColumnModel().getColumn(5).setPreferredWidth(100);
            searchResult.getColumnModel().getColumn(6).setPreferredWidth(120);
	}

	/**
	 * 列の表示位置を設定するTableCellRenderer
	 */
	private class TableCellAlignRenderer extends SelectTableCellRenderer
	{
		/** Creates a new instance of ReservationTableCellRenderer */
		public TableCellAlignRenderer()
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
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

			switch(column)
			{
                            default:
                                super.setHorizontalAlignment(SwingConstants.CENTER);
                                break;
			}

			return this;
		}
	}

}

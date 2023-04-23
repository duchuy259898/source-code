/*
 * StaffSalesHistoryPanel.java
 *
 * Created on 2008/09/25, 10:39
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

/**
 *
 * @author  s_matsumura
 */
public class StaffSalesHistoryPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	
	private StaffSalesHistory	ssh	=	new StaffSalesHistory();
	
	/** Creates new form StaffSalesHistoryPanel */
	public StaffSalesHistoryPanel()
	{
		initComponents();
		addMouseCursorChange();
		this.setSize(833, 550);
		this.setPath("商品管理 >> スタッフ販売 >> スタッフ販売履歴");
		this.setTitle("スタッフ販売履歴");
		SystemInfo.initGroupShopComponents(shop, 2);
		this.initStaff( staff );
		this.setKeyListener();
	}
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(selectButton);
		SystemInfo.addMouseCursorChange(showButton);
	}
	private void setKeyListener()
	{
		shop.addKeyListener(SystemInfo.getMoveNextField());
		shop.addFocusListener(SystemInfo.getSelectText());
		slipNo1.addKeyListener(SystemInfo.getMoveNextField());
		slipNo1.addFocusListener(SystemInfo.getSelectText());
		slipNo2.addKeyListener(SystemInfo.getMoveNextField());
		slipNo2.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodStart.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodStart.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodEnd.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodEnd.addFocusListener(SystemInfo.getSelectText());
		staff.addKeyListener(SystemInfo.getMoveNextField());
		staff.addFocusListener(SystemInfo.getSelectText());
		staffNo.addKeyListener(SystemInfo.getMoveNextField());
		staffNo.addFocusListener(SystemInfo.getSelectText());
	}

	/**
	 * 明細の列を初期化する。
	 */
	private void initProductsColumn()
	{
		//列の幅を設定する。
		staffSales.getColumnModel().getColumn(0).setPreferredWidth(80);		// 日付
		staffSales.getColumnModel().getColumn(1).setPreferredWidth(65);     // 伝票No
		staffSales.getColumnModel().getColumn(2).setPreferredWidth(80);		// スタッフ名
		staffSales.getColumnModel().getColumn(3).setPreferredWidth(100);    // 商品名
		staffSales.getColumnModel().getColumn(4).setPreferredWidth(65);		// 小計
		staffSales.getColumnModel().getColumn(5).setPreferredWidth(65);		// 割引
		staffSales.getColumnModel().getColumn(6).setPreferredWidth(65);		// 消費税
		staffSales.getColumnModel().getColumn(7).setPreferredWidth(65);		// 金額

	}

	private void initSumTableColumn()
	{
		sumTable.getColumnModel().getColumn(0).setPreferredWidth(65);		// 伝票件数
		sumTable.getColumnModel().getColumn(1).setPreferredWidth(65);		// 小計
		sumTable.getColumnModel().getColumn(2).setPreferredWidth(65);		// 割引
		sumTable.getColumnModel().getColumn(3).setPreferredWidth(65);		// 消費税
        sumTable.getColumnModel().getColumn(4).setPreferredWidth(65);		// 金額
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        shopLabel = new javax.swing.JLabel();
        shopLabel2 = new javax.swing.JLabel();
        shopLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbTargetPeriodStart = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        cmbTargetPeriodEnd = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel4 = new javax.swing.JLabel();
        staffNo = new javax.swing.JTextField();
        staff = new javax.swing.JComboBox();
        selectButton = new javax.swing.JButton();
        staffsalesScrollPane = new javax.swing.JScrollPane();
        staffSales = new javax.swing.JTable();
        jScrollSumPane = new javax.swing.JScrollPane();
        sumTable = new javax.swing.JTable();
        showButton = new javax.swing.JButton();
        slipNo1 = new javax.swing.JFormattedTextField();
        ((PlainDocument)slipNo1.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.INTEGER));
        slipNo2 = new javax.swing.JFormattedTextField();
        ((PlainDocument)slipNo2.getDocument()).setDocumentFilter(
            new CustomFilter(20, CustomFilter.INTEGER));

        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        shopLabel.setText("\u5e97\u8217");

        shopLabel2.setText("\u4f1d\u7968No.");

        shopLabel3.setText("\u65e5\u4ed8");

        jLabel2.setText("\u30b9\u30bf\u30c3\u30d5");

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel3.setText("\uff5e");

        cmbTargetPeriodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStart.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                cmbTargetPeriodStartFocusGained(evt);
            }
        });

        cmbTargetPeriodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                cmbTargetPeriodEndFocusGained(evt);
            }
        });

        jLabel4.setText("\uff5e");

        staffNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staffNo.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                staffNoFocusLost(evt);
            }
        });

        staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                staffActionPerformed(evt);
            }
        });

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setEnabled(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                selectButtonActionPerformed(evt);
            }
        });

        staffSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "日付", "伝票No.", "スタッフ名", "商品名", "小計", "割引", "消費税", "金額"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        staffSales.setSelectionBackground(new java.awt.Color(255, 210, 142));
        staffSales.setSelectionForeground(new java.awt.Color(0, 0, 0));
        staffSales.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(staffSales, SystemInfo.getTableHeaderRenderer());
        staffSales.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(staffSales);
        staffSales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initProductsColumn();

        SelectTableCellRenderer rightAlignRenderer = new SelectTableCellRenderer();
        rightAlignRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        staffSales.getColumnModel().getColumn(1).setCellRenderer(rightAlignRenderer);
        staffSales.getColumnModel().getColumn(6).setCellRenderer(rightAlignRenderer);

        staffSales.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                staffSalesMouseReleased(evt);
            }
        });
        staffSales.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                staffSalesKeyReleased(evt);
            }
        });

        staffsalesScrollPane.setViewportView(staffSales);

        jScrollSumPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollSumPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sumTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null}
            },
            new String []
            {
                "伝票件数", "小計", "割引", "消費税", "金額"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        sumTable.setCellSelectionEnabled(true);
        sumTable.setSelectionBackground(new java.awt.Color(255, 210, 142));
        sumTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        sumTable.getTableHeader().setReorderingAllowed(false);
        this.initSumTableColumn();
        sumTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);

        SwingUtil.setJTableHeaderRenderer(sumTable, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(sumTable);

        SelectTableCellRenderer sumTaxCellRenderer = new SelectTableCellRenderer();
        sumTaxCellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        sumTable.getColumnModel().getColumn(3).setCellRenderer(sumTaxCellRenderer);

        jScrollSumPane.setViewportView(sumTable);

        showButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        showButton.setBorderPainted(false);
        showButton.setContentAreaFilled(false);
        showButton.setFocusable(false);
        showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        showButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showButtonActionPerformed(evt);
            }
        });

        slipNo1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        slipNo1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        slipNo2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        slipNo2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(staffsalesScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shopLabel2)
                            .addComponent(shopLabel3)
                            .addComponent(jLabel2)
                            .addComponent(shopLabel))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(staffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(staff, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
                                .addComponent(showButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(slipNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(slipNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 545, Short.MAX_VALUE))))
                    .addComponent(jScrollSumPane, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shopLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slipNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slipNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(shopLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staff, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(showButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staffsalesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollSumPane, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void staffSalesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_staffSalesKeyReleased
	{//GEN-HEADEREND:event_staffSalesKeyReleased
		if (staffSales.getSelectedRow() >= 0)
		{
			selectButton.setEnabled(true);
		}
		else
		{
			selectButton.setEnabled(false);
		}
	}//GEN-LAST:event_staffSalesKeyReleased

	private void staffSalesMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_staffSalesMouseReleased
	{//GEN-HEADEREND:event_staffSalesMouseReleased
		if (staffSales.getSelectedRow() >= 0)
		{
			selectButton.setEnabled(true);
		}
		else
		{
			selectButton.setEnabled(false);
		}
	}//GEN-LAST:event_staffSalesMouseReleased
	
	//表示ボタン
    private void showButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showButtonActionPerformed
		searchList();
		showList();
		selectButton.setEnabled(false);
    }//GEN-LAST:event_showButtonActionPerformed
	
	private void searchList()
	{
		this.setSearchCondition();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			try
			{
				ssh.load(con);
			}
			finally
			{
				con.close();
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			if (e instanceof RuntimeException)
			{
				throw (RuntimeException) e;
			}

			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1099),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void showList()
	{
		try
		{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
			SwingUtil.clearTable(staffSales);
			DefaultTableModel	model	=	(DefaultTableModel)staffSales.getModel();
			
			for(StaffSalesHistoryData sshd : ssh)
			{
				String strdate = fmt.format(sshd.getSalesDate());
				Object[]	rowData	=	{	strdate,
				new RecordItem(sshd.getShopID(), sshd.getSlipNo()),
				sshd.getStaffName(),
				sshd.getItemName(),
				sshd.getItemValue(),
				sshd.getDiscountValue(),
				"(" + sshd.getTaxValue() + ")",
				sshd.getAmount()};
				model.addRow(rowData);
			}
			
			showSum();
		}
		catch (Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			if (e instanceof RuntimeException)
			{
				throw (RuntimeException) e;
			}

			MessageDialog.showMessageDialog(this,
				MessageUtil.getMessage(1099),
				this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void showSum()
	{
		SwingUtil.clearTable(sumTable);
		int slipCount = 0;          //伝票件数
		int sumItemValue = 0;       //小計
		int sumDiscountValue = 0;   //割引
		int sumTaxValue = 0;        //消費税
		int sumAmount = 0;          //金額
		
		for(StaffSalesHistoryData sshd : ssh)
		{
			slipCount += 1;
			sumItemValue += sshd.getItemValue();
			sumDiscountValue += sshd.getDiscountValue();
			sumTaxValue += sshd.getTaxValue();
			sumAmount += sshd.getAmount();
		}
		
		Vector v = new Vector();
		v.add(slipCount);
		v.add(sumItemValue);
		v.add(sumDiscountValue);
		v.add("(" + sumTaxValue + ")");
		v.add(sumAmount);
		
		((DefaultTableModel) sumTable.getModel()).addRow(v);
	}
	
	/**
	 * 検索条件をセットする。
	 */
	public void setSearchCondition()
	{
		
		//店舗
		ssh.setShop((MstShop)shop.getSelectedItem());
		
		//伝票No.
		ssh.setSlipNO(0, (slipNo1.getText().equals("") ? null : Integer.parseInt(slipNo1.getText())));
		ssh.setSlipNO(1, (slipNo2.getText().equals("") ? null : Integer.parseInt(slipNo2.getText())));
		
		//日付
		ssh.setSearchDate(0, cmbTargetPeriodStart.getDate());
		ssh.setSearchDate(1, cmbTargetPeriodEnd.getDate());
		
		//スタッフ
		ssh.setStaff((MstStaff)staff.getSelectedItem());
		
	}
	
	//選択ボタン
    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
		int index = staffSales.getSelectedRow();
		if (index < 0)
		{
			return;
		}

		RecordItem rec = (RecordItem) staffSales.getValueAt(index, 1);
		RegisterStaffSalesPanel staffSalesPanel = new RegisterStaffSalesPanel();
		staffSalesPanel.load(rec.getShopId(), rec.getSlipNo());

		JDialog dlg = new JDialog(getParentFrame(), "スタッフ販売", true);
		dlg.getContentPane().add(staffSalesPanel);
		dlg.setResizable(false);
		dlg.pack();
		SwingUtil.moveCenter(dlg);
		dlg.setVisible(true);

		searchList();
		showList();

		dlg.dispose();
    }//GEN-LAST:event_selectButtonActionPerformed
	
	
	/**
	 * スタッフを初期化する。
	 */
	protected void initStaff( JComboBox cb )
	{
		cb.addItem(new MstStaff());
		SystemInfo.initStaffComponent(cb);
		
		cb.setSelectedIndex(0);
	}
	
    private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed
		UIUtil.outputStaff(staff, staffNo);
    }//GEN-LAST:event_staffActionPerformed
	
    private void staffNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staffNoFocusLost
		UIUtil.selectStaff(staffNo, staff);
    }//GEN-LAST:event_staffNoFocusLost

	//日付
    private void cmbTargetPeriodEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndFocusGained
		cmbTargetPeriodEnd.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndFocusGained
	
    private void cmbTargetPeriodStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartFocusGained
		cmbTargetPeriodStart.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartFocusGained
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEnd;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStart;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollSumPane;
    private javax.swing.JButton selectButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private javax.swing.JLabel shopLabel2;
    private javax.swing.JLabel shopLabel3;
    private javax.swing.JButton showButton;
    private javax.swing.JFormattedTextField slipNo1;
    private javax.swing.JFormattedTextField slipNo2;
    protected javax.swing.JComboBox staff;
    private javax.swing.JTextField staffNo;
    private javax.swing.JTable staffSales;
    private javax.swing.JScrollPane staffsalesScrollPane;
    private javax.swing.JTable sumTable;
    // End of variables declaration//GEN-END:variables
	/**
	 * FocusTraversalPolicy
	 */
	private	FocusTraversalPolicy traversalPolicy = 	new FocusTraversalPolicyImpl();

	/**
	 * FocusTraversalPolicyを取得する。
	 * @return FocusTraversalPolicy
	 */
	public FocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	traversalPolicy;
	}

	/**
	 *
	 */
	private static class RecordItem
	{
		private int shopId;
		private int slipNo;

		RecordItem(int shopId, int slipNo)
		{
			this.shopId = shopId;
			this.slipNo = slipNo;
		}

		public int getShopId()
		{
			return this.shopId;
		}

		public int getSlipNo()
		{
			return this.slipNo;
		}

		public String toString()
		{
			return String.valueOf(slipNo);
		}
	}

	private class FocusTraversalPolicyImpl extends FocusTraversalPolicy
	{
		/**
		 * aComponent のあとでフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentAfter(Container aContainer, Component aComponent)
		{
			if (aComponent.equals(shop))
			{
				return slipNo1;
			}
			else if (aComponent.equals(slipNo1))
			{
				return slipNo2;
			}
			else if (aComponent.equals(slipNo2))
			{
				return cmbTargetPeriodStart;
			}
			else if (aComponent.equals(cmbTargetPeriodStart))
			{
				return cmbTargetPeriodEnd;
			}
			else if (aComponent.equals(cmbTargetPeriodEnd))
			{
				return staffNo;
			}
			else if (aComponent.equals(staffNo))
			{
				return staff;
			}
			else if (aComponent.equals(staff))
			{
				return shop;
			}
			
			return shop;
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
			if (aComponent.equals(staff))
			{
				return staffNo;
			}
			else if (aComponent.equals(staffNo))
			{
				return cmbTargetPeriodEnd;
			}
			else if (aComponent.equals(cmbTargetPeriodEnd))
			{
				return cmbTargetPeriodStart;
			}
			else if (aComponent.equals(cmbTargetPeriodStart))
			{
				return slipNo2;
			}
			else if (aComponent.equals(slipNo2))
			{
				return slipNo1;
			}
			else if (aComponent.equals(slipNo1))
			{
				return shop;
			}
			
			return shop;
		}
		
		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return shop;
		}
		
		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return staffNo;
		}
		
		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return shop;
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
			return shop;
		}
	}
}

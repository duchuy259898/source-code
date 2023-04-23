/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.report.custom;

import com.geobeck.sosia.pos.hair.master.product.MstCourseClass;
import com.geobeck.sosia.pos.hair.master.product.MstCourseClasses;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.products.Product;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.products.ProductClass;
import com.geobeck.sosia.pos.products.ProductClasses;
import java.util.Calendar;
import java.util.Date;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import java.awt.Cursor;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yasumoto
 */
public class ModestyReportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

	private MstCourseClasses mccs = new MstCourseClasses();
	private MstItemClasses mics = new MstItemClasses();

	private ProductClasses itemClasses = new ProductClasses();
	Map<Integer, MapProduct> condItemClass = new HashMap<Integer, MapProduct>();
	Map<Integer, MapProduct> condItem = new HashMap<Integer, MapProduct>();

	private boolean loaded = false;
	/**
	 * Creates new form ModestyReportPanel
	 */
	public ModestyReportPanel() {
		try {
			ConnectionWrapper con = SystemInfo.getConnection();

			mccs.load(con);
			mccs.add(0, new MstCourseClass());
			mics.load(con);
			mics.add(0, new MstItemClass());
			con.close();
		} catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		initComponents();
		this.setSize(958, 750);
		this.setPath("カスタム帳票");
		this.setTitle("カスタム帳票");
		this.setKeyListener();
		init();
	}

	private void setKeyListener() {

		cmbTargetDate.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetDate.addFocusListener(SystemInfo.getSelectText());
		repoCommitionButton.addKeyListener(SystemInfo.getMoveNextField());
		repoEkimuButton.addKeyListener(SystemInfo.getMoveNextField());
		repoHanbaiButton.addKeyListener(SystemInfo.getMoveNextField());
		printButton.addKeyListener(SystemInfo.getMoveNextField());
	}

	/**
	 * init form
	 */
	private void init() {
		printButton.setText("");
		condRegistButton.setText("");
		viewListButton.setText("");
		Calendar cal = Calendar.getInstance();

		//期間を初期設定する    
		Calendar cdr = Calendar.getInstance();
		cdr.setTime(new Date());

		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.YEAR, -1);
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		//対象期間の設定
		this.cmbTargetDate.setDate(new Date());
		this.repoHanbaiButton.doClick();
		setMonthLaelState(false);
		SystemInfo.initGroupShopComponents(cblShop, 2);

		for (String creature : ModestyReportAnalysisLogic.ANLY_LABEL) {
			anlyTgtList.addItem(creature);
		}
		initProductClasses();
	}

	private ArrayList<String> getItemList() {
		ArrayList<String> ls = new ArrayList<String>();
		if (lstSelectedItem.getText() != null && !lstSelectedItem.getText().equals("")) {
			for (Map.Entry<Integer, MapProduct> pc : condItemClass.entrySet()) {
				if (pc.getValue().checked) {
					ls.add("0__"+pc.getKey() + "__"+pc.getValue().getName());
				}
				for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
					if (p.getValue().getClassID().equals(pc.getKey()) && p.getValue().checked) {
						ls.add(p.getKey()+ "__"+pc.getKey() + "__"+p.getValue().getName() );
					}
				}
			}
		}
		return ls;
	}

	private void setMonthLabel() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		this.lblMonth.setText("(対象月：" + sdf.format(this.cmbTargetDate.getDate()) + ")");
	}

	private void setMonthLaelState(boolean param) {
		this.lblMonth.setVisible(param);
		this.lblStaff.setVisible(param);
		this.cmbStaff.setVisible(param);
	}

	private void setAnlyPanelState(boolean param, boolean paramb) {
		this.condRegistButton.setVisible(param);
		this.viewListButton.setVisible(paramb);
		this.anlyPanel2.setVisible(param);
		this.lblAnlyTgt.setVisible(paramb);
		this.anlyTgtList.setVisible(paramb);
	}

	/**
	 * 担当者を初期化する。
	 */
	protected void initStaff(JComboBox combobox) {
		if (cblShop.getSelectedItem() != null
				&& cblShop.getSelectedItem() instanceof MstShop) {
			MstShop shop = (MstShop) cblShop.getSelectedItem();
			combobox.removeAllItems();
			combobox.addItem(new MstStaff());
			try {
				MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(),
						combobox, shop.getShopID());
			} catch (SQLException ex) {
				SystemInfo.getLogger().log(Level.SEVERE, null, ex);
			}
		} else {
			combobox.removeAllItems();
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reportGroup = new javax.swing.ButtonGroup();
        analysisBtnGroup = new javax.swing.ButtonGroup();
        cmbTargetDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        repoHanbaiButton = new javax.swing.JRadioButton();
        repoEkimuButton = new javax.swing.JRadioButton();
        repoCommitionButton = new javax.swing.JRadioButton();
        lblDate = new javax.swing.JLabel();
        lblReport = new javax.swing.JLabel();
        cblShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblShop = new javax.swing.JLabel();
        cmbStaff = new javax.swing.JComboBox();
        lblStaff = new javax.swing.JLabel();
        repoSekinin = new javax.swing.JRadioButton();
        repoAnalysisBtn = new javax.swing.JRadioButton();
        anlyPanel2 = new javax.swing.JPanel();
        anlyTgtList = new javax.swing.JComboBox();
        lblAnlyTgt = new javax.swing.JLabel();
        lblAnlyCs = new javax.swing.JLabel();
        lblAnlyItem = new javax.swing.JLabel();
        lblAnlyItemSel = new javax.swing.JLabel();
        lblAnlyItemSeled = new javax.swing.JLabel();
        cmbCourseClass1 = new JComboBox(mccs.toArray());
        cmbCourseClass2 = new JComboBox(mccs.toArray());
        cmbCourseClass3 = new JComboBox(mccs.toArray());
        cmbCourseClass4 = new JComboBox(mccs.toArray());
        cmbCourseClass5 = new JComboBox(mccs.toArray());
        cmbCourseClass6 = new JComboBox(mccs.toArray());
        cmbItemClass1 = new JComboBox(mics.toArray());
        cmbItemClass2 = new JComboBox(mics.toArray());
        cmbItemClass3 = new JComboBox(mics.toArray());
        cmbItemClass4 = new JComboBox(mics.toArray());
        cmbItemClass5 = new JComboBox(mics.toArray());
        cmbItemClass6 = new JComboBox(mics.toArray());
        itemClassScrollPane = new javax.swing.JScrollPane();
        lstItemClass = new javax.swing.JTable();
        itemScrollPane1 = new javax.swing.JScrollPane();
        lstItem = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSelectedItem = new javax.swing.JTextArea();
        cmbItemClass7 = new JComboBox(mics.toArray());
        printButton = new javax.swing.JButton();
        lblMonth = new javax.swing.JLabel();
        condRegistButton = new javax.swing.JButton();
        viewListButton = new javax.swing.JButton();
        repoAnalysisBtn1 = new javax.swing.JRadioButton();
        repoAnalysisBtn2 = new javax.swing.JRadioButton();
        repoAnalysisBtn3 = new javax.swing.JRadioButton();

        setPreferredSize(new java.awt.Dimension(958, 750));

        cmbTargetDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetDate.setFocusCycleRoot(true);
        cmbTargetDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTargetDateItemStateChanged(evt);
            }
        });

        reportGroup.add(repoHanbaiButton);
        repoHanbaiButton.setText("販売実績表");
        repoHanbaiButton.setActionCommand("A");
        repoHanbaiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoHanbaiButtonActionPerformed(evt);
            }
        });

        reportGroup.add(repoEkimuButton);
        repoEkimuButton.setText("役務償却日報");
        repoEkimuButton.setActionCommand("B");
        repoEkimuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoEkimuButtonActionPerformed(evt);
            }
        });

        reportGroup.add(repoCommitionButton);
        repoCommitionButton.setText("コミッション明細書");
        repoCommitionButton.setActionCommand("C");
        repoCommitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoCommitionButtonActionPerformed(evt);
            }
        });

        lblDate.setText("対象日");

        lblReport.setText("出力帳票");

        cblShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(127, 157, 185)));
        cblShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cblShopActionPerformed(evt);
            }
        });

        lblShop.setText("対象");

        cmbStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblStaff.setText("スタッフ名");

        reportGroup.add(repoSekinin);
        repoSekinin.setText("責任者分析表");
        repoSekinin.setActionCommand("D");
        repoSekinin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoSekininActionPerformed(evt);
            }
        });

        reportGroup.add(repoAnalysisBtn);
        repoAnalysisBtn.setText("現状分析報告書");
        repoAnalysisBtn.setActionCommand("E");
        repoAnalysisBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoAnalysisBtnActionPerformed(evt);
            }
        });

        lblAnlyTgt.setText("出力対象");

        lblAnlyCs.setText("<コース分類>");

        lblAnlyItem.setText("<商品分類>");

        lblAnlyItemSel.setText("<商品選択>");

        lblAnlyItemSeled.setText("選択リスト");

        cmbCourseClass1.setMaximumRowCount(12);
        cmbCourseClass1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbCourseClass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseClass1ActionPerformed(evt);
            }
        });

        cmbCourseClass2.setMaximumRowCount(12);
        cmbCourseClass2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbCourseClass2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseClass2ActionPerformed(evt);
            }
        });

        cmbCourseClass3.setMaximumRowCount(12);
        cmbCourseClass3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbCourseClass3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseClass3ActionPerformed(evt);
            }
        });

        cmbCourseClass4.setMaximumRowCount(12);
        cmbCourseClass4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbCourseClass4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseClass4ActionPerformed(evt);
            }
        });

        cmbCourseClass5.setMaximumRowCount(12);
        cmbCourseClass5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbCourseClass5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseClass5ActionPerformed(evt);
            }
        });

        cmbCourseClass6.setMaximumRowCount(12);
        cmbCourseClass6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbCourseClass6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseClass6ActionPerformed(evt);
            }
        });

        cmbItemClass1.setMaximumRowCount(12);
        cmbItemClass1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass1ActionPerformed(evt);
            }
        });

        cmbItemClass2.setMaximumRowCount(12);
        cmbItemClass2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass2ActionPerformed(evt);
            }
        });

        cmbItemClass3.setMaximumRowCount(12);
        cmbItemClass3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass3ActionPerformed(evt);
            }
        });

        cmbItemClass4.setMaximumRowCount(12);
        cmbItemClass4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass4ActionPerformed(evt);
            }
        });

        cmbItemClass5.setMaximumRowCount(12);
        cmbItemClass5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass5ActionPerformed(evt);
            }
        });

        cmbItemClass6.setMaximumRowCount(12);
        cmbItemClass6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass6ActionPerformed(evt);
            }
        });

        itemClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lstItemClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "商品分類"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lstItemClass.setSelectionBackground(new java.awt.Color(255, 210, 142));
        lstItemClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        lstItemClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstItemClass.getTableHeader().setReorderingAllowed(false);
        lstItemClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(lstItemClass, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(lstItemClass);
        lstItemClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstItemClassMouseReleased(evt);
            }
        });
        lstItemClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lstItemClassKeyReleased(evt);
            }
        });
        itemClassScrollPane.setViewportView(lstItemClass);

        itemScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lstItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "商品名", "選択"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lstItem.setSelectionBackground(new java.awt.Color(255, 210, 142));
        lstItem.setSelectionForeground(new java.awt.Color(0, 0, 0));
        lstItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstItem.getTableHeader().setReorderingAllowed(false);
        lstItem.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SwingUtil.setJTableHeaderRenderer(lstItem, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(lstItem);
        lstItem.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                lstItemPropertyChange(evt);
            }
        });
        itemScrollPane1.setViewportView(lstItem);

        lstSelectedItem.setEditable(false);
        lstSelectedItem.setBackground(new java.awt.Color(255, 255, 204));
        lstSelectedItem.setColumns(20);
        lstSelectedItem.setFont(new java.awt.Font("ＭＳ Ｐゴシック", 0, 12)); // NOI18N
        lstSelectedItem.setLineWrap(true);
        lstSelectedItem.setRows(1);
        lstSelectedItem.setTabSize(1);
        jScrollPane1.setViewportView(lstSelectedItem);

        cmbItemClass7.setMaximumRowCount(12);
        cmbItemClass7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbItemClass7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbItemClass7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout anlyPanel2Layout = new javax.swing.GroupLayout(anlyPanel2);
        anlyPanel2.setLayout(anlyPanel2Layout);
        anlyPanel2Layout.setHorizontalGroup(
            anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(anlyPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(anlyPanel2Layout.createSequentialGroup()
                        .addComponent(lblAnlyTgt)
                        .addGap(29, 29, 29)
                        .addComponent(anlyTgtList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 809, Short.MAX_VALUE))
                    .addGroup(anlyPanel2Layout.createSequentialGroup()
                        .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAnlyCs)
                            .addComponent(cmbCourseClass1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCourseClass3, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCourseClass4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCourseClass2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmbCourseClass6, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbCourseClass5, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbItemClass7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblAnlyItem, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbItemClass4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbItemClass1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbItemClass2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbItemClass3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbItemClass6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbItemClass5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38)
                        .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(anlyPanel2Layout.createSequentialGroup()
                                .addComponent(itemClassScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(itemScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblAnlyItemSel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAnlyItemSeled)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        anlyPanel2Layout.setVerticalGroup(
            anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, anlyPanel2Layout.createSequentialGroup()
                .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnlyTgt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anlyTgtList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnlyCs, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnlyItemSel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnlyItemSeled, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(anlyPanel2Layout.createSequentialGroup()
                        .addGroup(anlyPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(itemScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                            .addComponent(itemClassScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(anlyPanel2Layout.createSequentialGroup()
                        .addComponent(cmbCourseClass1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCourseClass2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCourseClass3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCourseClass4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbCourseClass5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCourseClass6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAnlyItem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbItemClass1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbItemClass2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbItemClass3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbItemClass4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbItemClass5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbItemClass6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbItemClass7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 70, Short.MAX_VALUE))))
        );

        printButton.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        printButton.setText("Excel出力　");
        printButton.setToolTipText("Excel出力");
        printButton.setBorderPainted(false);
        printButton.setContentAreaFilled(false);
        printButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printButton.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        lblMonth.setText("月");

        condRegistButton.setIcon(SystemInfo.getImageIcon("/button/mail/cond_regist_off.jpg"));
        condRegistButton.setText("条件登録");
        condRegistButton.setToolTipText("条件登録");
        condRegistButton.setBorderPainted(false);
        condRegistButton.setContentAreaFilled(false);
        condRegistButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        condRegistButton.setPressedIcon(SystemInfo.getImageIcon("/button/mail/cond_regist_on.jpg"));
        condRegistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condRegistButtonActionPerformed(evt);
            }
        });

        viewListButton.setIcon(SystemInfo.getImageIcon("/button/common/view_list_off.jpg"));
        viewListButton.setText("一覧表示");
        viewListButton.setToolTipText("条件登録");
        viewListButton.setBorderPainted(false);
        viewListButton.setContentAreaFilled(false);
        viewListButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewListButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/view_list_on.jpg"));
        viewListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewListButtonActionPerformed(evt);
            }
        });

        reportGroup.add(repoAnalysisBtn1);
        repoAnalysisBtn1.setText("現状分析用リスト(サロン会員)");
        repoAnalysisBtn1.setActionCommand("F");
        repoAnalysisBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoAnalysisBtn1ActionPerformed(evt);
            }
        });

        reportGroup.add(repoAnalysisBtn2);
        repoAnalysisBtn2.setText("現状分析用リスト(フリー会員)");
        repoAnalysisBtn2.setActionCommand("G");
        repoAnalysisBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoAnalysisBtn2ActionPerformed(evt);
            }
        });

        reportGroup.add(repoAnalysisBtn3);
        repoAnalysisBtn3.setText("現状分析用リスト(愛用者)");
        repoAnalysisBtn3.setActionCommand("H");
        repoAnalysisBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repoAnalysisBtn3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(anlyPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDate)
                            .addComponent(lblShop)
                            .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cblShop, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(repoHanbaiButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repoEkimuButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repoCommitionButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repoSekinin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(repoAnalysisBtn))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbTargetDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)
                                .addComponent(condRegistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(repoAnalysisBtn1)
                                .addGap(18, 18, 18)
                                .addComponent(repoAnalysisBtn2)
                                .addGap(18, 18, 18)
                                .addComponent(repoAnalysisBtn3)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShop, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cblShop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReport)
                    .addComponent(repoHanbaiButton)
                    .addComponent(repoEkimuButton)
                    .addComponent(repoCommitionButton)
                    .addComponent(repoSekinin)
                    .addComponent(repoAnalysisBtn))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(repoAnalysisBtn1)
                            .addComponent(repoAnalysisBtn2)
                            .addComponent(repoAnalysisBtn3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTargetDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(printButton)
                            .addComponent(viewListButton)
                            .addComponent(condRegistButton))))
                .addGap(18, 18, 18)
                .addComponent(anlyPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTargetDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTargetDateItemStateChanged
		setMonthLabel();
    }//GEN-LAST:event_cmbTargetDateItemStateChanged

    private void repoCommitionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoCommitionButtonActionPerformed
		setMonthLaelState(true);
		setAnlyPanelState(false, false);
    }//GEN-LAST:event_repoCommitionButtonActionPerformed

    private void repoEkimuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoEkimuButtonActionPerformed
		setMonthLaelState(false);
		setAnlyPanelState(false, false);
    }//GEN-LAST:event_repoEkimuButtonActionPerformed

    private void repoHanbaiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoHanbaiButtonActionPerformed
		setMonthLaelState(false);
		setAnlyPanelState(false, false);
    }//GEN-LAST:event_repoHanbaiButtonActionPerformed

    private void cblShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cblShopActionPerformed
		initStaff(cmbStaff);
		condItemClass = new HashMap<Integer, MapProduct>();
		condItem = new HashMap<Integer, MapProduct>();
		loaded = false;
		loadCondition();
    }//GEN-LAST:event_cblShopActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
		printButton.setCursor(null);
		ModestyReportLogic logic = new ModestyReportLogic();

		try {
			boolean logicResult = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			Date targetDate = this.cmbTargetDate.getDate();
			ButtonModel targetRepo = this.reportGroup.getSelection();
			//ButtonModel targetGRepo = this.reportAnlyGroup.getSelection();

			try {
				if (targetRepo.getActionCommand().equals("C")) {
					// コミッション明細書
					if (cblShop.getSelectedItem() instanceof MstGroup
							|| cmbStaff.getSelectedIndex() == 0) {
						MessageDialog.showMessageDialog(this,
								MessageUtil.getMessage(1102, "スタッフ名"),
								this.getTitle(),
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					MstShop shop = (MstShop) cblShop.getSelectedItem();
					MstStaff staff = (MstStaff) cmbStaff.getSelectedItem();
					logicResult = logic.printMonthlyCommission(shop, staff, targetDate);

				} else if (targetRepo.getActionCommand().equals("D")) {
					// 責任者分析表
					MstShop shop = (MstShop) cblShop.getSelectedItem();
					logicResult = logic.printModestyAnalysisResponsibility(shop, targetDate, this);

				} else if (targetRepo.getActionCommand().equals("B")) {
					// 償却日報
					logicResult = logic.printDailyReport((MstShop) cblShop.getSelectedItem(), targetDate);
				} else if (targetRepo.getActionCommand().equals("E")) {
					// 現状分析報告書
					ArrayList<String> ls = getItemList();
					logicResult = ModestyReportAnalysisLogic.printDailyReport(this, (MstShop) cblShop.getSelectedItem(), targetDate, ls);
				} 
				
				
				 else if (targetRepo.getActionCommand().equals("F")) {
					// 現状分析用リスト(サロン会員)
					ArrayList<String> ls = getItemList();
					logicResult = ModestyReportAnalysisLogic.printListReport(this, (MstShop) cblShop.getSelectedItem(), targetDate, "F");
				}  else if (targetRepo.getActionCommand().equals("G")) {
					// 現状分析用リスト(フリー会員)
					ArrayList<String> ls = getItemList();
					logicResult = ModestyReportAnalysisLogic.printListReport(this, (MstShop) cblShop.getSelectedItem(), targetDate, "G");
				}  else if (targetRepo.getActionCommand().equals("H")) {
					// 現状分析用リスト(愛用者)
					ArrayList<String> ls = getItemList();
					logicResult = ModestyReportAnalysisLogic.printListReport(this, (MstShop) cblShop.getSelectedItem(), targetDate, "H");
				} 
				
				
				
				
				else {
					logicResult = logic.printPaymentReport((MstShop) cblShop.getSelectedItem(), targetDate);
				}

			} catch (Exception e) {
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(1099),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!logicResult) {
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(4001),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
			}
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
    }//GEN-LAST:event_printButtonActionPerformed

    private void repoSekininActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoSekininActionPerformed
		setMonthLaelState(false);
		setAnlyPanelState(false, false);
    }//GEN-LAST:event_repoSekininActionPerformed

    private void repoAnalysisBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoAnalysisBtnActionPerformed
		this.lblMonth.setVisible(true);
		setAnlyPanelState(true, true);
		
		loaded = false;
		loadCondition();
    }//GEN-LAST:event_repoAnalysisBtnActionPerformed

    private void cmbCourseClass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseClass1ActionPerformed
//        refresh();
//        changeTextEditable(true);
    }//GEN-LAST:event_cmbCourseClass1ActionPerformed

    private void cmbCourseClass2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseClass2ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseClass2ActionPerformed

    private void cmbCourseClass3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseClass3ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseClass3ActionPerformed

    private void cmbCourseClass4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseClass4ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseClass4ActionPerformed

    private void cmbCourseClass5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseClass5ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseClass5ActionPerformed

    private void cmbCourseClass6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseClass6ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseClass6ActionPerformed

    private void cmbItemClass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass1ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass1ActionPerformed

    private void cmbItemClass2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass2ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass2ActionPerformed

    private void cmbItemClass3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass3ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass3ActionPerformed

    private void cmbItemClass4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass4ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass4ActionPerformed

    private void cmbItemClass5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass5ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass5ActionPerformed

    private void cmbItemClass6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass6ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass6ActionPerformed

    private void lstItemClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lstItemClassKeyReleased
		this.showProducts(lstItemClass, lstItem);
    }//GEN-LAST:event_lstItemClassKeyReleased

    private void lstItemClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstItemClassMouseReleased
		this.showProducts(lstItemClass, lstItem);
    }//GEN-LAST:event_lstItemClassMouseReleased

    private void lstItemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_lstItemPropertyChange
		this.setItemChecked(lstItem);
    }//GEN-LAST:event_lstItemPropertyChange

    private void cmbItemClass7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemClass7ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemClass7ActionPerformed

	private void loadCondition() {
		if (!loaded) {
		lstSelectedItem.setText("");
		MstShop shop = (MstShop) cblShop.getSelectedItem();
		StringBuilder sql = new StringBuilder();
		try {
			cmbCourseClass1.setSelectedIndex(-1);
			cmbCourseClass2.setSelectedIndex(-1);
			cmbCourseClass3.setSelectedIndex(-1);
			cmbCourseClass4.setSelectedIndex(-1);
			cmbCourseClass5.setSelectedIndex(-1);
			cmbCourseClass6.setSelectedIndex(-1);
			cmbItemClass1.setSelectedIndex(-1);
			cmbItemClass2.setSelectedIndex(-1);
			cmbItemClass3.setSelectedIndex(-1);
			cmbItemClass4.setSelectedIndex(-1);
			cmbItemClass5.setSelectedIndex(-1);
			cmbItemClass6.setSelectedIndex(-1);
			cmbItemClass7.setSelectedIndex(-1);
				
			sql.append("select * from mst_primo_setting where shop_id=" + shop.getShopID());
			ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
			while (rs.next()) {
				cmbCourseClass1.setSelectedIndex(rs.getInt("course1"));
				cmbCourseClass2.setSelectedIndex(rs.getInt("course2"));
				cmbCourseClass3.setSelectedIndex(rs.getInt("course3"));
				cmbCourseClass4.setSelectedIndex(rs.getInt("course4"));
				cmbCourseClass5.setSelectedIndex(rs.getInt("course5"));
				cmbCourseClass6.setSelectedIndex(rs.getInt("course6"));
				cmbItemClass1.setSelectedIndex(rs.getInt("item1"));
				cmbItemClass2.setSelectedIndex(rs.getInt("item2"));
				cmbItemClass3.setSelectedIndex(rs.getInt("item3"));
				cmbItemClass4.setSelectedIndex(rs.getInt("item4"));
				cmbItemClass5.setSelectedIndex(rs.getInt("item5"));
				cmbItemClass6.setSelectedIndex(rs.getInt("item6"));
				cmbItemClass7.setSelectedIndex(rs.getInt("item7"));
				
				for(int i=0; i < lstItemClass.getRowCount(); i++) {
					//lstItemClass.
					this.showProducts(lstItemClass, lstItem);
				}

				String cond = rs.getString("cosmetics");
				for (String c : cond.split(",")) {
					if (c.length() < 3) {
						break;
					}
					String[] strAry = c.split("__");
					if (strAry[0].equals("0")) {
						condItemClass.put(Integer.parseInt(strAry[1]), new MapProduct(strAry[2], true, 0));
					} else {
						condItem.put(Integer.parseInt(strAry[1]), new MapProduct(strAry[2], true, Integer.parseInt(strAry[0])));
					}
					lstSelectedItem.append(strAry[2]);
					lstSelectedItem.append("\n");
                    // 技術・商品の設定を反映する
//					for (Map.Entry<Integer, MapProduct> pc : condItemClass.entrySet()) {
//						if (sp[0].equals("0") && pc.getKey().equals(Integer.parseInt(sp[0]))) {
//							pc.getValue().setChecked(true);
//						}
//						for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
//							if (p.getKey().equals(Integer.parseInt(sp[1])) && pc.getKey().equals(Integer.parseInt(sp[0]))) {
//								p.getValue().setChecked(true);
//							}
//						}
//					}
				}
				this.showProducts(lstItemClass, lstItem);
			}
//			this.showSelectedProduct();
		} catch (Exception e) {
			SystemInfo.getLogger().info(e.getMessage());
		}
		loaded =true;
		} 
	}

    private void condRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condRegistButtonActionPerformed

		condRegistButton.setCursor(null);
		try {
			MstShop shop = (MstShop) cblShop.getSelectedItem();
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ConnectionWrapper con = SystemInfo.getConnection();
			con.begin();
			StringBuilder sql = new StringBuilder(1000);
			Integer condId = 0;
			// Thanh start edit 2014/03/11
			sql.append("select shop_id from mst_primo_setting where shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
			// Thanh end edit 2014/03/11
			ResultSetWrapper rs = con.executeQuery(sql.toString());
			if (rs.next()) {
				condId = rs.getInt("shop_id");
			}

			if (condId == 0) {
				//insert
				sql.setLength(0);
				sql.append(" insert into mst_primo_setting(shop_id, course1, course2, course3, course4, course5, course6, item1, item2, item3, item4, item5, item6, item7, cosmetics, update_shop_id, insert_date, update_date)");
				sql.append(" values(");
				sql.append(SQLUtil.convertForSQL(shop.getShopID()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbCourseClass1.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbCourseClass2.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbCourseClass3.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbCourseClass4.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbCourseClass5.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbCourseClass6.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass1.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass2.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass3.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass4.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass5.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass6.getSelectedIndex()));
				sql.append(",").append(SQLUtil.convertForSQL(cmbItemClass7.getSelectedIndex()));
				String items = "";
				for (String cnd : getItemList()) {
					items += cnd + ",";
				}
				sql.append(",").append(SQLUtil.convertForSQL(items));
				sql.append(",").append(SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
				sql.append(",current_timestamp");
				sql.append(",current_timestamp");
				sql.append(");");
				con.execute(sql.toString());
			} else {
				//insert
				sql.setLength(0);
				sql.append(" update mst_primo_setting SET ");
				sql.append(" course1=").append(SQLUtil.convertForSQL(cmbCourseClass1.getSelectedIndex()));
				sql.append(",course2=").append(SQLUtil.convertForSQL(cmbCourseClass2.getSelectedIndex()));
				sql.append(",course3=").append(SQLUtil.convertForSQL(cmbCourseClass3.getSelectedIndex()));
				sql.append(",course4=").append(SQLUtil.convertForSQL(cmbCourseClass4.getSelectedIndex()));
				sql.append(",course5=").append(SQLUtil.convertForSQL(cmbCourseClass5.getSelectedIndex()));
				sql.append(",course6=").append(SQLUtil.convertForSQL(cmbCourseClass6.getSelectedIndex()));
				sql.append(",item1=").append(SQLUtil.convertForSQL(cmbItemClass1.getSelectedIndex()));
				sql.append(",item2=").append(SQLUtil.convertForSQL(cmbItemClass2.getSelectedIndex()));
				sql.append(",item3=").append(SQLUtil.convertForSQL(cmbItemClass3.getSelectedIndex()));
				sql.append(",item4=").append(SQLUtil.convertForSQL(cmbItemClass4.getSelectedIndex()));
				sql.append(",item5=").append(SQLUtil.convertForSQL(cmbItemClass5.getSelectedIndex()));
				sql.append(",item6=").append(SQLUtil.convertForSQL(cmbItemClass6.getSelectedIndex()));
				sql.append(",item7=").append(SQLUtil.convertForSQL(cmbItemClass7.getSelectedIndex()));
				String items = "";
				for (String cnd : getItemList()) {
					items += cnd + ",";
				}
				sql.append(",cosmetics=").append(SQLUtil.convertForSQL(items));
				sql.append(",update_shop_id=").append(SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
				sql.append(",update_date=current_timestamp");
				sql.append(" where shop_id=").append(SQLUtil.convertForSQL(shop.getShopID()));
				SystemInfo.getLogger().info(sql.toString());
				con.execute(sql.toString());
			}
			con.commit();

			MessageDialog.showMessageDialog(
					this,
					MessageUtil.getMessage(201),
					"条件登録",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			SystemInfo.getLogger().info(e.getMessage());
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
    }//GEN-LAST:event_condRegistButtonActionPerformed

    private void viewListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewListButtonActionPerformed

		MstShop shop = (MstShop) cblShop.getSelectedItem();
		Date targetDate = this.cmbTargetDate.getDate();
		String tgt = ModestyReportAnalysisLogic.ANLY_KEY[anlyTgtList.getSelectedIndex()];
		ModestyReportAnalysisListPanel alp = new ModestyReportAnalysisListPanel(title);
		Set<Integer> tgtCustSet = ModestyReportAnalysisLogic.getTargetCustomerIdSet(this, shop, targetDate, tgt);
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			alp.showData(shop, targetDate, tgtCustSet, anlyTgtList.getSelectedItem().toString());
			alp.setOpener(this);
			this.setVisible(false);
			parentFrame.changeContents(alp);
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
    }//GEN-LAST:event_viewListButtonActionPerformed

    private void repoAnalysisBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoAnalysisBtn1ActionPerformed
		setAnlyPanelState(true, false);
		
		loaded = false;
		loadCondition();
    }//GEN-LAST:event_repoAnalysisBtn1ActionPerformed

    private void repoAnalysisBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoAnalysisBtn2ActionPerformed
		setAnlyPanelState(true, false);
		
		loaded = false;
		loadCondition();
    }//GEN-LAST:event_repoAnalysisBtn2ActionPerformed

    private void repoAnalysisBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repoAnalysisBtn3ActionPerformed
		setAnlyPanelState(true, false);
		
		loaded = false;
		loadCondition();
    }//GEN-LAST:event_repoAnalysisBtn3ActionPerformed

	private void initProductClasses() {
		condItemClass.clear();
		condItem.clear();
		lstSelectedItem.setText("");

		try {

			ConnectionWrapper con = SystemInfo.getConnection();

			itemClasses.setProductDivision(2);
			itemClasses.load(con, SystemInfo.getCurrentShop().getShopID());

		} catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		this.setProductClasses(itemClasses, lstItemClass);
		
		for (int i = lstItemClass.getRowCount(); i >= 0; i--) {
			lstItemClass.setEditingRow(i);
			this.showProducts(lstItemClass, lstItem);
		}

		lstItem.getColumnModel().getColumn(0).setPreferredWidth(300);

	}

	/**
	 * 技術・商品のリストを表示する。
	 */
	private void showProducts(JTable productClassesTable, JTable productsTable) {
		//全行削除
		SwingUtil.clearTable(productsTable);

		//選択されている分類を取得
		ProductClass pc = this.getSelectedProductClass(productClassesTable);

		if (pc == null) {
			return;
		}

		// 選択中のタブ（技術・商品）を取得
		int division = 2;

		try {
			ConnectionWrapper con = SystemInfo.getConnection();
			pc.loadProducts(con, division, SystemInfo.getCurrentShop().getShopID());
		} catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		Map<Integer, MapProduct> condProductClass = null;
		Map<Integer, MapProduct> condProduct = null;

		condProductClass = condItemClass;
		condProduct = condItem;

		Product topProduct = new Product();
		topProduct.setProductClass(pc);
		topProduct.setProductName(pc.toString() + "のいずれか");

		if (!condProductClass.containsKey(pc.getProductClassID())) {
			condProductClass.put(pc.getProductClassID(), new MapProduct(topProduct.getProductName(), false));
		}

		//テーブルに技術・商品を追加
		DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
		model.addRow(new Object[]{topProduct, condProductClass.get(pc.getProductClassID()).checked});

		for (Product mj : pc) {
			if (!condProduct.containsKey(mj.getProductID())) {
				condProduct.put(mj.getProductID(), new MapProduct(mj.getProductName(), false, mj.getProductClass().getProductClassID()));
			}
			model.addRow(new Object[]{mj, condProduct.get(mj.getProductID()).checked});
		}
	}

	private void setProductClasses(ProductClasses productClasses, JTable classTable) {
		SwingUtil.clearTable(classTable);
		DefaultTableModel model = (DefaultTableModel) classTable.getModel();

		for (ProductClass pc : productClasses) {
			model.addRow(new Object[]{pc});
		}

		if (0 < classTable.getRowCount()) {
			classTable.setRowSelectionInterval(0, 0);
		}
	}

	/**
	 * 選択されている分類を取得する。
	 *
	 * @return 選択されている分類
	 */
	public ProductClass getSelectedProductClass(JTable productClassesTable) {
		if (productClassesTable.getSelectedRow() < 0) {
			return null;
		}
		return (ProductClass) productClassesTable.getValueAt(productClassesTable.getSelectedRow(), 0);
	}

	private void setItemChecked(JTable table) {

		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		if (row < 0 || col < 1) {
			return;
		}

		Boolean checked = (Boolean) table.getValueAt(row, col);

		if (row == 0) {
			if (checked) {
				for (int i = 1; i < table.getRowCount(); i++) {
					table.setValueAt(false, i, col);
				}
			}
		} else if (checked) {
			table.setValueAt(false, 0, col);
		}

		table.changeSelection(row, col - 1, false, false);

		Map<Integer, MapProduct> condProductClass = null;
		Map<Integer, MapProduct> condProduct = null;

		condProductClass = condItemClass;
		condProduct = condItem;

		for (int i = 0; i < table.getRowCount(); i++) {
			Product p = (Product) table.getValueAt(i, 0);
			Boolean chk = (Boolean) table.getValueAt(i, 1);
			if (i == 0) {
				condProductClass.get(p.getProductClass().getProductClassID()).checked = chk;
			} else {
				condProduct.get(p.getProductID()).checked = chk;
			}
		}

		this.showSelectedProduct();
	}

	private void showSelectedProduct() {

		lstSelectedItem.setText("");
				
		for (Map.Entry<Integer, MapProduct> pc : condItemClass.entrySet()) {
			if (pc.getValue().checked) {
				lstSelectedItem.append(pc.getValue().getName());
				lstSelectedItem.append("\n");
			}
		}
		for (Map.Entry<Integer, MapProduct> p : condItem.entrySet()) {
			if (p.getValue().checked) {
				lstSelectedItem.append(p.getValue().getName());
				lstSelectedItem.append("\n");
			}
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup analysisBtnGroup;
    private javax.swing.JPanel anlyPanel2;
    private javax.swing.JComboBox anlyTgtList;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cblShop;
    protected javax.swing.JComboBox cmbCourseClass1;
    protected javax.swing.JComboBox cmbCourseClass2;
    protected javax.swing.JComboBox cmbCourseClass3;
    protected javax.swing.JComboBox cmbCourseClass4;
    protected javax.swing.JComboBox cmbCourseClass5;
    protected javax.swing.JComboBox cmbCourseClass6;
    protected javax.swing.JComboBox cmbItemClass1;
    protected javax.swing.JComboBox cmbItemClass2;
    protected javax.swing.JComboBox cmbItemClass3;
    protected javax.swing.JComboBox cmbItemClass4;
    protected javax.swing.JComboBox cmbItemClass5;
    protected javax.swing.JComboBox cmbItemClass6;
    protected javax.swing.JComboBox cmbItemClass7;
    protected javax.swing.JComboBox cmbStaff;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetDate;
    private javax.swing.JButton condRegistButton;
    private javax.swing.JScrollPane itemClassScrollPane;
    private javax.swing.JScrollPane itemScrollPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnlyCs;
    private javax.swing.JLabel lblAnlyItem;
    private javax.swing.JLabel lblAnlyItemSel;
    private javax.swing.JLabel lblAnlyItemSeled;
    private javax.swing.JLabel lblAnlyTgt;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblMonth;
    private javax.swing.JLabel lblReport;
    private javax.swing.JLabel lblShop;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JTable lstItem;
    private javax.swing.JTable lstItemClass;
    private javax.swing.JTextArea lstSelectedItem;
    private javax.swing.JButton printButton;
    private javax.swing.JRadioButton repoAnalysisBtn;
    private javax.swing.JRadioButton repoAnalysisBtn1;
    private javax.swing.JRadioButton repoAnalysisBtn2;
    private javax.swing.JRadioButton repoAnalysisBtn3;
    private javax.swing.JRadioButton repoCommitionButton;
    private javax.swing.JRadioButton repoEkimuButton;
    private javax.swing.JRadioButton repoHanbaiButton;
    private javax.swing.JRadioButton repoSekinin;
    private javax.swing.ButtonGroup reportGroup;
    private javax.swing.JButton viewListButton;
    // End of variables declaration//GEN-END:variables

	class MapProduct {

		private String name = "";
		private Boolean checked = false;
		private Integer classID = 0;

		public MapProduct(String name, Boolean checked) {
			this(name, checked, 0);
		}

		public MapProduct(String name, Boolean checked, Integer classID) {
			this.name = name;
			this.checked = checked;
			this.classID = classID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Boolean getChecked() {
			return checked;
		}

		public void setChecked(Boolean checked) {
			this.checked = checked;
		}

		public Integer getClassID() {
			return classID;
		}

		public void setClassID(Integer classID) {
			this.classID = classID;
		}
	}

}

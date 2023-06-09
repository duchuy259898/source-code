/*
 * MstUseProductPanel.java
 *
 * Created on 2007/01/17, 19:09
 */

package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.products.Product;
import com.geobeck.sosia.pos.products.ProductClass;
import com.geobeck.sosia.pos.swing.JComboBoxLabel;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.swing.IntegerCellEditor;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JTextField;

/**
 *
 * @author  katagiri
 */
public class MstUseProductPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    /** Creates new form MstUseProductPanel */
    public MstUseProductPanel(Integer productDivision) {
        this.setProductDivision(productDivision);
        initComponents();
        addMouseCursorChange();
        SystemInfo.initGroupShopComponents(shop,2);
        this.setSize(830, 500);
        this.setPath("基本設定 >> 商品マスタ");
        this.setTitle("使用商品登録");
        this.init();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        registButton = new javax.swing.JButton();
        referenceProductsScrollPane = new javax.swing.JScrollPane();
        referenceProducts = new javax.swing.JTable();
        productClassScrollPane = new javax.swing.JScrollPane();
        productClass = new javax.swing.JTable();
        referenceLabel = new javax.swing.JLabel();
        selectProductsScrollPane = new javax.swing.JScrollPane();
        selectProducts = new javax.swing.JTable();
        selectLabel = new javax.swing.JLabel();
        shopLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        releaseButton = new javax.swing.JButton();
        releaseAllButton = new javax.swing.JButton();

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                shopItemStateChanged(evt);
            }
        });

        registButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.setBorderPainted(false);
        registButton.setContentAreaFilled(false);
        registButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButtonActionPerformed(evt);
            }
        });

        referenceProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品No.", "商品名", "価格"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
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
        referenceProducts.setSelectionForeground(new java.awt.Color(0, 0, 0));
        referenceProducts.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(referenceProducts, SystemInfo.getTableHeaderRenderer());
        referenceProducts.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(referenceProducts);
        referenceProducts.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                referenceProductsMouseMoved(evt);
            }
        });
        referenceProductsScrollPane.setViewportView(referenceProducts);

        productClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "分類名"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        productClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productClass.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(productClass, SystemInfo.getTableHeaderRenderer());
        productClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(productClass);
        productClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productClassMouseReleased(evt);
            }
        });
        productClass.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                productClassMouseMoved(evt);
            }
        });
        productClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productClassKeyReleased(evt);
            }
        });
        productClassScrollPane.setViewportView(productClass);

        referenceLabel.setText("参照リスト");

        selectProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品No.", "商品名", "価格", "業務適正", "店販適正", "表示順", "予約色", "表示", "division", "ispot_menu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                // return canEdit [columnIndex];
                if(columnIndex ==2){
                    if(selectProducts.getValueAt(rowIndex, 6).toString().equals("1") &&
                        ! selectProducts.getValueAt(rowIndex, 7).toString().equals("0")){
                        return false;
                    }
                    return true;
                }else{
                    return canEdit [columnIndex];
                }
            }
        });
        selectProducts.setSelectionForeground(new java.awt.Color(0, 0, 0));
        selectProducts.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(selectProducts, SystemInfo.getTableHeaderRenderer());
        selectProducts.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(selectProducts);
        TableColumnModel model = selectProducts.getColumnModel();
        model.getColumn(2).setCellEditor(new IntegerCellEditor(new JTextField()));
        model.getColumn(3).setCellEditor(new IntegerCellEditor(new JTextField()));
        model.getColumn(4).setCellEditor(new IntegerCellEditor(new JTextField()));
        model.getColumn(5).setCellEditor(new IntegerCellEditor(new JTextField()));
        selectProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectProductsMouseClicked(evt);
            }
        });
        selectProducts.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                selectProductsPropertyChange(evt);
            }
        });
        selectProductsScrollPane.setViewportView(selectProducts);
        /*
        if (selectProducts.getColumnModel().getColumnCount() > 0) {
            selectProducts.getColumnModel().getColumn(8).setResizable(false);
            selectProducts.getColumnModel().getColumn(9).setResizable(false);
        }
        */
        selectProducts.getColumnModel().getColumn(8).setResizable(false);
        selectProducts.getColumnModel().getColumn(8).setPreferredWidth(0);
        selectProducts.getColumnModel().getColumn(8).setMinWidth(0);
        selectProducts.getColumnModel().getColumn(8).setMaxWidth(0);
        selectProducts.getColumnModel().getColumn(9).setResizable(false);
        selectProducts.getColumnModel().getColumn(9).setPreferredWidth(0);
        selectProducts.getColumnModel().getColumn(9).setMinWidth(0);
        selectProducts.getColumnModel().getColumn(9).setMaxWidth(0);

        selectLabel.setText("選択リスト");

        shopLabel.setText("店舗");

        jPanel1.setOpaque(false);

        selectButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        selectAllButton.setIcon(SystemInfo.getImageIcon("/button/arrow/right2_off.jpg"));
        selectAllButton.setBorderPainted(false);
        selectAllButton.setContentAreaFilled(false);
        selectAllButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/right2_on.jpg"));
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        releaseButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left_off.jpg"));
        releaseButton.setBorderPainted(false);
        releaseButton.setContentAreaFilled(false);
        releaseButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left_on.jpg"));
        releaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseButtonActionPerformed(evt);
            }
        });

        releaseAllButton.setIcon(SystemInfo.getImageIcon("/button/arrow/left2_off.jpg"));
        releaseAllButton.setBorderPainted(false);
        releaseAllButton.setContentAreaFilled(false);
        releaseAllButton.setPressedIcon(SystemInfo.getImageIcon("/button/arrow/left2_on.jpg"));
        releaseAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseAllButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 124, Short.MAX_VALUE)
                .add(releaseAllButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(releaseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(shopLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(14, 14, 14)
                        .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(productClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(referenceLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                .add(183, 183, 183))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(referenceProductsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, selectProductsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(shop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(shopLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(referenceLabel)
                    .add(selectLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(referenceProductsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, selectProductsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, productClassScrollPane, 0, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void referenceProductsMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referenceProductsMouseMoved

        if (selectProducts.getCellEditor() != null) selectProducts.getCellEditor().stopCellEditing();

    }//GEN-LAST:event_referenceProductsMouseMoved

    private void productClassMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productClassMouseMoved

        if (selectProducts.getCellEditor() != null) selectProducts.getCellEditor().stopCellEditing();
        
    }//GEN-LAST:event_productClassMouseMoved

	private void selectProductsPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_selectProductsPropertyChange
	{//GEN-HEADEREND:event_selectProductsPropertyChange

            int row = selectProducts.getEditingRow();
            int col = selectProducts.getEditingColumn();

            if (row < 0 || col < 0) return;

            if (this.getProductDivision() == 1 || this.getProductDivision() == 3) {

                /********************/
                // 使用技術登録
                /********************/

                if (col == 4 || col == 5) {

                    Integer index = productClass.getSelectedRow();
                    Product p = useProducts.getSelectProducts().get(index).get(row);

                    switch (col) {
                        case 4:
                            p.setColor(selectProducts.getValueAt(row, col).toString());
                            break;
                        case 5:
                            p.setReserveFlg(Boolean.valueOf(selectProducts.getValueAt(row, col).toString()));
                            break;
                    }

                } else {

                    Integer index = productClass.getSelectedRow();
                    Integer val = Integer.valueOf(selectProducts.getValueAt(row, col).toString());

                    if (index != null && 0 <= index) {

                        if (useProducts.getSelectProducts().get(index).size() == 0) return;

                        Product p = useProducts.getSelectProducts().get(index).get(row);

                        switch (col) {
                            case 2: p.setPrice(val.longValue()); break;
                            case 3: p.setDisplaySeq(val); break;
                        }
                    }

                }

            } else {

                /********************/
                // 使用商品登録
                /********************/

                Integer index = productClass.getSelectedRow();
                Integer val = Integer.valueOf(selectProducts.getValueAt(row, col).toString());

                if (index != null && 0 <= index) {

                    if (useProducts.getSelectProducts().get(index).size() == 0) return;

                    Product p = useProducts.getSelectProducts().get(index).get(row);

                    switch (col) {
                        case 2: p.setPrice(val.longValue()); break;
                        case 3: p.setUseProperStock(val); break;
                        case 4: p.setSellProperStock(val); break;
                        case 5: p.setDisplaySeq(val); break;
                    }
                }

            }

	}//GEN-LAST:event_selectProductsPropertyChange

    private void shopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_shopItemStateChanged
        JComboBoxLabel cbl =(JComboBoxLabel)evt.getSource();
        useProducts.setShop((MstShop)cbl.getSelectedItem());
        this.load();
        this.showProductClasses();
        if(0 < productClass.getRowCount()) {
            productClass.setRowSelectionInterval(0, 0);
        }
        this.showProducts();
    }//GEN-LAST:event_shopItemStateChanged
        
	private void registButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registButtonActionPerformed
	{//GEN-HEADEREND:event_registButtonActionPerformed

            if (selectProducts.getCellEditor() != null) selectProducts.getCellEditor().stopCellEditing();
            
            registButton.setCursor(null);

            try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                if(this.regist()) {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS,
                            useProducts.getProductDivisionName()),
                            this.getTitle(),
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
                            useProducts.getProductDivisionName()),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }                

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

	}//GEN-LAST:event_registButtonActionPerformed
        
	private void releaseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_releaseButtonActionPerformed
	{//GEN-HEADEREND:event_releaseButtonActionPerformed
            this.moveProduct(false);
	}//GEN-LAST:event_releaseButtonActionPerformed
        
	private void releaseAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_releaseAllButtonActionPerformed
	{//GEN-HEADEREND:event_releaseAllButtonActionPerformed
            this.moveProductAll(false);
	}//GEN-LAST:event_releaseAllButtonActionPerformed
        
	private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectAllButtonActionPerformed
	{//GEN-HEADEREND:event_selectAllButtonActionPerformed
            this.moveProductAll(true);
	}//GEN-LAST:event_selectAllButtonActionPerformed
        
	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
	{//GEN-HEADEREND:event_selectButtonActionPerformed
            this.moveProduct(true);
	}//GEN-LAST:event_selectButtonActionPerformed
        
	private void productClassMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_productClassMouseReleased
	{//GEN-HEADEREND:event_productClassMouseReleased
            this.showProducts();
	}//GEN-LAST:event_productClassMouseReleased
        
	private void productClassKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_productClassKeyReleased
	{//GEN-HEADEREND:event_productClassKeyReleased
            this.showProducts();
	}//GEN-LAST:event_productClassKeyReleased

        private void selectProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectProductsMouseClicked

            if (getProductDivision() == 2) return;

            int row = selectProducts.getSelectedRow();
            int col = selectProducts.getSelectedColumn();

            if (row < 0 || col != 4) return;

            ColorChooserDialog dlg = new ColorChooserDialog(parentFrame, selectProducts.getValueAt(row, col).toString());
            dlg.setVisible(true);

            if (dlg.isClear()) {
                selectProducts.setValueAt("", row, col);
                useProducts.getSelectProducts().get(productClass.getSelectedRow()).get(row).setColor("");
            } else {
                if (dlg.getHexColor().length() > 0) {
                    selectProducts.setValueAt(dlg.getHexColor(), row, col);
                    useProducts.getSelectProducts().get(productClass.getSelectedRow()).get(row).setColor(dlg.getHexColor());
                }
            }

            dlg.dispose();
            dlg = null;

        }//GEN-LAST:event_selectProductsMouseClicked
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable productClass;
    private javax.swing.JScrollPane productClassScrollPane;
    private javax.swing.JLabel referenceLabel;
    private javax.swing.JTable referenceProducts;
    private javax.swing.JScrollPane referenceProductsScrollPane;
    private javax.swing.JButton registButton;
    private javax.swing.JButton releaseAllButton;
    private javax.swing.JButton releaseButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JLabel selectLabel;
    private javax.swing.JTable selectProducts;
    private javax.swing.JScrollPane selectProductsScrollPane;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    // End of variables declaration//GEN-END:variables
    
    private	MstUseProducts		useProducts		=	new MstUseProducts();
    
    /**
     * 店舗を設定する。
     * @param shop 店舗
     */
    public void setShop(MstShop shop)
    { 
      useProducts.setShop(shop);
    }
     
    /**
     * 店舗を取得する。
     * @return 店舗
     */
 
    private MstShop getShop(){      
        return	useProducts.getShop();        
    }    
    /**
     * 処理区分を設定する。
     * @param productDivision 処理区分
     */
    private void setProductDivision(Integer productDivision) {
        useProducts.setProductDivision(productDivision);
    }
    
    /**
     * 処理区分を取得する。
     * @return 処理区分
     */
    private Integer getProductDivision() {
        return	useProducts.getProductDivision();
    }
    
    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(registButton);
        SystemInfo.addMouseCursorChange(selectButton);
        SystemInfo.addMouseCursorChange(selectAllButton);
        SystemInfo.addMouseCursorChange(releaseButton);
        SystemInfo.addMouseCursorChange(releaseAllButton);
    }
    
    /**
     * 初期化処理を行う。
     */
    private void init() {
	
	if (getProductDivision() != 2) {

            TableColumnModel refModel = referenceProducts.getColumnModel();
	    refModel.getColumn(0).setHeaderValue("技術No.");
	    refModel.getColumn(1).setHeaderValue("技術名");
	    
	    TableColumnModel colModel = selectProducts.getColumnModel();
            if (colModel.getColumn(3).getHeaderValue().toString().equals("業務適正")) {
		colModel.removeColumn(colModel.getColumn(3));
		colModel.removeColumn(colModel.getColumn(3));
                
            }
	    colModel.getColumn(0).setHeaderValue("技術No.");
	    colModel.getColumn(1).setHeaderValue("技術名");

	} else {

	    TableColumnModel colModel = selectProducts.getColumnModel();
            colModel.removeColumn(colModel.getColumn(6));
            colModel.removeColumn(colModel.getColumn(6));
        }

        this.load();
        this.showProductClasses();
        if(0 < productClass.getRowCount()) {
            productClass.setRowSelectionInterval(0, 0);
       } 
            
        this.showProducts();
    }
    
    /**
     * データを読み込む。
     */
    private void load() {
        try {
            useProducts.load(SystemInfo.getConnection());
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    /**
     * 分類を表示する。
     */
    private void showProductClasses() {
        SwingUtil.clearTable(productClass);
        
        DefaultTableModel	model	=	(DefaultTableModel)productClass.getModel();
        
        for(ProductClass pc : useProducts.getReferenceProducts()) {
            Vector<Object>	temp	=	new Vector<Object>();
            temp.add(pc);
            model.addRow(temp);
        }
    }
    
    /**
     * データを表示する。
     */
    private void showProducts() {
        
        if (selectProducts.getCellEditor() != null) selectProducts.getCellEditor().stopCellEditing();
        
        Integer	index = productClass.getSelectedRow();
        
        SwingUtil.clearTable(referenceProducts);
        SwingUtil.clearTable(selectProducts);
        
        if(index != null && 0 <= index) {
            this.showProducts(useProducts.getReferenceProducts().get(index), referenceProducts);
            this.showProducts(useProducts.getSelectProducts().get(index), selectProducts);
        }
    }
    
    /**
     * データを表示する。
     * @param pc 分類
     * @param table テーブル
     */
    private void showProducts(ProductClass pc, JTable table) {
        SwingUtil.clearTable(table);
        
        DefaultTableModel model	= (DefaultTableModel)table.getModel();
        
        for(Product p : pc) {
            p.setPrice((table.equals(referenceProducts) ? p.getBasePrice() : p.getPrice()));
            p.setDisplaySeq((table.equals(referenceProducts) ? p.getBaseDisplaySeq() : p.getDisplaySeq()));

            Vector<Object> temp	= new Vector<Object>();
            temp.add(p.getProductNo());
            temp.add(p.getProductName());
            temp.add(p.getPrice());
            temp.add(p.getUseProperStock());
            temp.add(p.getSellProperStock());
            temp.add(p.getDisplaySeq());
            temp.add(p.getColor());
            temp.add(p.isReserveFlg());
            if(table.equals(selectProducts) ){
                if(this.getProductDivision()==1){
                     temp.add(this.getProductDivision());
                     temp.add(p.getIspotMenuId());
                }else{
                    temp.add(this.getProductDivision());
                    temp.add(0);
                }
            }
            model.addRow(temp);
        }
    }
    
    /**
     * 商品・技術を選択（解除）する。
     * @param isSelect true：選択、false：解除
     */
    public void moveProduct(boolean isSelect) {
        Integer		classIndex	=	productClass.getSelectedRow();
        JTable	    fromTable	=	(isSelect ? referenceProducts : selectProducts);
        
        for(Integer i = fromTable.getSelectedRowCount() - 1; 0 <= i; i --) {
            useProducts.moveProduct(isSelect, classIndex, fromTable.getSelectedRows()[i]);
        }
        
        useProducts.sort(isSelect, classIndex);
        
        this.showProducts();
    }
    
    /**
     * 商品・技術を全て選択（解除）する。
     * @param isSelect true：選択、false：解除
     */
    public void moveProductAll(boolean isSelect) {
        Integer		classIndex	=	productClass.getSelectedRow();
        JTable	    fromTable	=	(isSelect ? referenceProducts : selectProducts);
        
        useProducts.moveProductAll(isSelect, classIndex);
        
        this.showProducts();
    }
    
    
    /**
     * 登録処理を行う。
     * @return true - 成功、false - 失敗
     */
    public boolean regist() {
        
        boolean result = false;
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            
            con.begin();
            
            try {
                result = useProducts.regist(con);
                
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            if(result) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return	result;
    }
    
}

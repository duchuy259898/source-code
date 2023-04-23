/*
 * SearchSupplierDialog.java
 *
 * Created on 2008/12/11
 */

package com.geobeck.sosia.pos.hair.search.supplier;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.commodity.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author  katagiri
 */
public class SearchSupplierDialog extends javax.swing.JDialog
{
	/** Creates new form SearchSupplierDialog */
	public SearchSupplierDialog(java.awt.Frame parent, boolean modal) {
	    super(parent, modal);
	    initComponents();
	    this.setTitle("仕入先一覧");
	    this.init();
	    this.load();
            addMouseCursorChange();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        purchaseGroup = new javax.swing.ButtonGroup();
        imagePanel1 = new com.geobeck.swing.ImagePanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplierTable = new javax.swing.JTable();
        selectButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        supplierTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "仕入先No.", "仕入先名", "仕入区分", "郵便番号", "住所", "電話番号"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        supplierTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(supplierTable, SystemInfo.getTableHeaderRenderer());
        supplierTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(supplierTable);
        supplierTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supplierTableMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(supplierTable);

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setFocusable(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonselectCustomer(evt);
            }
        });

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonbackPrevious(evt);
            }
        });

        javax.swing.GroupLayout imagePanel1Layout = new javax.swing.GroupLayout(imagePanel1);
        imagePanel1.setLayout(imagePanel1Layout);
        imagePanel1Layout.setHorizontalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, imagePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                    .addGroup(imagePanel1Layout.createSequentialGroup()
                        .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        imagePanel1Layout.setVerticalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectButtonselectCustomer(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonselectCustomer
	this.setSelectedSupplier();
	this.setVisible(false);
    }//GEN-LAST:event_selectButtonselectCustomer

	private void supplierTableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_supplierTableMouseClicked
	{//GEN-HEADEREND:event_supplierTableMouseClicked
	    if(evt.getClickCount() == 2) {
		this.setSelectedSupplier();
		this.setVisible(false);
	    }
	}//GEN-LAST:event_supplierTableMouseClicked

	private void backButtonbackPrevious(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backButtonbackPrevious
	{//GEN-HEADEREND:event_backButtonbackPrevious
	    this.setVisible(false);
	}//GEN-LAST:event_backButtonbackPrevious
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel imagePanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup purchaseGroup;
    private javax.swing.JButton selectButton;
    private javax.swing.JTable supplierTable;
    // End of variables declaration//GEN-END:variables
	
	private	SearchSupplier ss = new SearchSupplier();
	private MstSupplier selectedSupplier = null;
	
	private void init() {
	    this.initTableColumnWidth();
	}
	
	private void initTableColumnWidth() {
	    
	    //列の幅を設定する。
	    supplierTable.getColumnModel().getColumn(0).setPreferredWidth(60);
	    supplierTable.getColumnModel().getColumn(1).setPreferredWidth(140);
	    supplierTable.getColumnModel().getColumn(2).setPreferredWidth(60);
	    supplierTable.getColumnModel().getColumn(3).setPreferredWidth(60);
	    supplierTable.getColumnModel().getColumn(4).setPreferredWidth(170);
	    supplierTable.getColumnModel().getColumn(5).setPreferredWidth(100);
	}
	
	private void load() {
	    
	    SwingUtil.clearTable(supplierTable);
		
	    ss.load();
		
	    DefaultTableModel model = (DefaultTableModel)supplierTable.getModel();
		
	    for(MstSupplier ms : ss) {

		Vector<Object> temp = new Vector<Object>();
		//IVS_LVTu start edit 2016/03/03 New request #48791	
		temp.add(ms.getSupplierNo().toString());
                //IVS_LVTu end edit 2016/03/03 New request #48791
		temp.add(ms.getSupplierName());
		temp.add(ms.getPurchaseDivisionName());
		temp.add(ms.getPostalCodeWithHyphen());
		temp.add(ms.getFullAddress());
		temp.add(ms.getPhoneNumber());
			
		model.addRow(temp);
	    }
	}
	
	private void setSelectedSupplier() {
	    if(0 <= supplierTable.getSelectedRow()) {
		selectedSupplier = ss.get(supplierTable.getSelectedRow());
	    }
	}

	public MstSupplier getSelectedSupplier(){ 
	    return selectedSupplier;
	}
        private void addMouseCursorChange()
	{
            SystemInfo.addMouseCursorChange(backButton);
            SystemInfo.addMouseCursorChange(selectButton);
        }
}

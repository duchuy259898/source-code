/*
 * SelectProductDialog.java
 *
 * Created on 2015/11/13
 */

package com.geobeck.sosia.pos.hair.customer;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.data.account.CourseClass;
import com.geobeck.sosia.pos.hair.data.account.CourseClasses;
import com.geobeck.sosia.pos.hair.data.account.CunsumptionCourseClasses;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.products.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author lvtu
 */
public class SelectProductDialog extends javax.swing.JDialog
{
        private MstShop shop = SystemInfo.getCurrentShop();
        
	public SelectProductDialog(java.awt.Frame parent, boolean modal,
			Component opener)
	{
		super(parent, modal);
		this.opener = opener;
                if (shop != null) this.shop = shop;
		init();
		initComponents();
		addMouseCursorChange();
		SwingUtil.moveCenter(this);

		selectButton.setVisible( false );
                //技術のコース分類に値をセット
                showProductClass();
                //技術のコース分類に対する技術をセット
                showProducts();
                //コース契約のコース分類に値をセット
                showCourseClass();
                
                showCourse();
		this.initTableColumnWidth();
	}
        
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backPanel = new com.geobeck.swing.ImagePanel();
        selectButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        JtableproductDivision = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        courseClass = new javax.swing.JTable();
        productScrollPane1 = new javax.swing.JScrollPane();
        course = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productClass = new javax.swing.JTable();
        productScrollPane3 = new javax.swing.JScrollPane();
        product = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));

        selectButton.setIcon(SystemInfo.getImageIcon("/button/select/select_off.jpg"));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_on.jpg"));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        JtableproductDivision.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JtableproductDivisionStateChanged(evt);
            }
        });

        courseClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
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
        courseClass.setSelectionBackground(new java.awt.Color(220, 220, 220));
        courseClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        courseClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseClass.getTableHeader().setReorderingAllowed(false);
        courseClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(courseClass);
        SwingUtil.setJTableHeaderRenderer(courseClass, SystemInfo.getTableHeaderRenderer());
        courseClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                courseClassMouseReleased(evt);
            }
        });
        courseClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                courseClassKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(courseClass);

        productScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        course.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "コース名", "回数", "単価"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Long.class
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
        course.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        course.setSelectionBackground(new java.awt.Color(220, 220, 220));
        course.setSelectionForeground(new java.awt.Color(0, 0, 0));
        course.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        course.getTableHeader().setReorderingAllowed(false);
        course.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(course);
        SwingUtil.setJTableHeaderRenderer(course, SystemInfo.getTableHeaderRenderer());
        course.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                courseMouseClicked(evt);
            }
        });
        productScrollPane1.setViewportView(course);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(productScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, productScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addContainerGap())
        );

        JtableproductDivision.addTab("コース", jPanel2);

        productClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
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
        productClass.setSelectionBackground(new java.awt.Color(220, 220, 220));
        productClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        productClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productClass.getTableHeader().setReorderingAllowed(false);
        productClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(productClass);
        SwingUtil.setJTableHeaderRenderer(productClass, SystemInfo.getTableHeaderRenderer());
        productClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productClassMouseReleased(evt);
            }
        });
        productClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productClassKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(productClass);

        productScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        product.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品名", "単価"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        product.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        product.setSelectionBackground(new java.awt.Color(220, 220, 220));
        product.setSelectionForeground(new java.awt.Color(0, 0, 0));
        product.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        product.getTableHeader().setReorderingAllowed(false);
        product.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(product);
        SwingUtil.setJTableHeaderRenderer(product, SystemInfo.getTableHeaderRenderer());
        product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productMouseClicked(evt);
            }
        });
        productScrollPane3.setViewportView(product);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(productScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .add(productScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
                .addContainerGap())
        );

        JtableproductDivision.addTab("商品", jPanel3);

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, JtableproductDivision)
                    .add(backPanelLayout.createSequentialGroup()
                        .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(JtableproductDivision, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
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
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * 分類が変更されたときの処理
	 * @param evt 
	 */
	/**
	 * 戻るボタンが押されたときの処理
	 * @param evt 
	 */
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backButtonActionPerformed
	{//GEN-HEADEREND:event_backButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "戻る");
		this.setVisible(false);
	}//GEN-LAST:event_backButtonActionPerformed

	/**
	 * 選択ボタンが押されたときの処理
	 * @param evt 
	 */
	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
	{//GEN-HEADEREND:event_selectButtonActionPerformed
		this.setSelectedProduct();
	}//GEN-LAST:event_selectButtonActionPerformed

    private void JtableproductDivisionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JtableproductDivisionStateChanged
        if(JtableproductDivision.getSelectedIndex() == 0) {
            //技術タブを選択した場合
            showProducts();
        } else if (JtableproductDivision.getSelectedIndex() == 1) {
            //コース契約タブを選択した場合
            showCourse();
        } 
    }//GEN-LAST:event_JtableproductDivisionStateChanged

    private void courseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseMouseClicked
        if(evt.getClickCount() == 2) {
            if(0 <= course.getSelectedRow()) {
                setSelectedCourse();
            }
        }
    }//GEN-LAST:event_courseMouseClicked

    private void courseClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_courseClassKeyReleased
        showCourse();
    }//GEN-LAST:event_courseClassKeyReleased

    private void courseClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseClassMouseReleased
        showCourse();
    }//GEN-LAST:event_courseClassMouseReleased

    private void productClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productClassMouseReleased
       showProducts();
    }//GEN-LAST:event_productClassMouseReleased

    private void productClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productClassKeyReleased
        showProducts();
    }//GEN-LAST:event_productClassKeyReleased

    private void productMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productMouseClicked
        if(evt.getClickCount() == 2) {
            if(0 <= product.getSelectedRow()) {
                setSelectedProduct();
            }
        }
    }//GEN-LAST:event_productMouseClicked
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane JtableproductDivision;
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JTable course;
    private javax.swing.JTable courseClass;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable product;
    private javax.swing.JTable productClass;
    private javax.swing.JScrollPane productScrollPane1;
    private javax.swing.JScrollPane productScrollPane3;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
	
	private	Component opener				=	null;

        private MstCustomer customer = null;

	private	ProductClasses	productClasses	=	new ProductClasses();

        private CourseClasses courseClasses = new CourseClasses();

        private CunsumptionCourseClasses consumptionCourseClasses = new CunsumptionCourseClasses();

	/**
	 * 選択された商品（技術・商品）
	 */
	private	Product			selectedProduct	=	null;
	/**
         * 選択されたコース
         */
	private Course selectedCourse = null;
        /**
         * 選択された消化コース
         */
        private ConsumptionCourse selectedConsumptionCourse = null;
        
        // vtbphuong start add 20140423 Bug #22496 
        private java.util.Date resevationDate = null;
        // vtbphuong start add 20140423 Bug #22496 

	/**
	 * リラクゼーション用商品（技術・商品）検索画面用FocusTraversalPolicy
	 */
	private	SearchHairProductFocusTraversalPolicy	ftp	=
			new SearchHairProductFocusTraversalPolicy();
	
	/**
	 * リラクゼーション用商品（技術・商品）検索画面用FocusTraversalPolicyを取得する。
	 * @return リラクゼーション用商品（技術・商品）検索画面用FocusTraversalPolicy
	 */
	public SearchHairProductFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(selectButton);
		SystemInfo.addMouseCursorChange(backButton);
	}

	/**
	 * 選択された商品（技術・商品）を取得する。
	 * @return 選択された商品（技術・商品）
	 */
	public Product getSelectedProduct()
	{
		return selectedProduct;
	}

	/**
	 * 選択された商品（技術・商品）をセットする。
	 * @param selectedProduct 選択された商品（技術・商品）
	 */
	public void setSelectedProduct(Product selectedProduct)
	{
		this.selectedProduct = selectedProduct;
	}
        
	/**
	 * 初期化処理を行う。
	 */
	private void init()
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
                        
                        //商品
			productClasses.setProductDivision(2);
			productClasses.load(con, this.shop.getShopID());

                        //コース分類取得
                        courseClasses.setProductDivision(3);
                        courseClasses.load(con, this.shop.getShopID());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
        /**
         * コース契約のコース分類リストを表示する
         */
        private void showCourseClass() {
            DefaultTableModel model = (DefaultTableModel)courseClass.getModel();

            //全行削除
            SwingUtil.clearTable(courseClass);

            for (CourseClass cc : courseClasses) {
                Object[] rowData = {cc};
                model.addRow(rowData);
            }

            if (0 < courseClass.getRowCount()) {
                courseClass.setRowSelectionInterval(0, 0);
            }
        }
	
        /**
         * コース契約の選択されたコース分類に紐付くコースを表示する
         */
        private void showCourse() {
            DefaultTableModel model = (DefaultTableModel)course.getModel();

            //全行削除
            SwingUtil.clearTable(course);

            //選択されている分類を取得
            CourseClass cc = this.getSelectedCourseClass();

            if (cc == null) {
                //選択されていない場合はなにもしない
                return;
            }

            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                //Start edit 20131230 lvut 本部ログイン時の予約登録メニューについて
                if(this.shop != null){
                    cc.loadCourse(con, this.shop.getShopID(), cc.getCourseClassId());
                }else{
                    cc.loadCourse(con, SystemInfo.getCurrentShop().getShopID(), cc.getCourseClassId());
                }
                //End edit 20131230 lvut 本部ログイン時の予約登録メニューについて
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            for (Course c : cc) {
                Object[] rowData = { c, c.getNum(), c.getPrice() };
                model.addRow(rowData);
            }
        }

        /**
	 * 選択されている分類を取得する。
	 * @return 選択されている分類
	 */
	public CourseClass getSelectedCourseClass()
	{
		if(courseClass.getSelectedRow() < 0)	return	null;

		return	(CourseClass)courseClass.getValueAt(courseClass.getSelectedRow(), 0);
	}

        /**
         * コース契約のコース分類リストを表示する
         */
        private void showProductClass() {
            DefaultTableModel model = (DefaultTableModel)productClass.getModel();

            //全行削除
            SwingUtil.clearTable(productClass);

            for (ProductClass pc : productClasses) {
                Object[] rowData = {pc};
                model.addRow(rowData);
            }

            if (0 < productClass.getRowCount()) {
                productClass.setRowSelectionInterval(0, 0);
            }
        }

        /**
	 * 商品のリストを表示する。
	 */
	private void showProducts()
	{
		DefaultTableModel	model	=	(DefaultTableModel)product.getModel();
		
		//全行削除
		model.setRowCount(0);
		product.removeAll();
		
		ProductClass	pc	=	this.getSelectedProductClass();
		
		if(pc == null)	return;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			pc.loadProducts(con, productClasses.getProductDivision(), this.shop.getShopID());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		for(Product mj : pc)
		{
			Object[]	rowData	=	{	mj.getProductName(),
										mj.getPrice()	};
			model.addRow(rowData);
		}
	}
	
	/**
	 * 選択されている分類を取得する。
	 * @return 選択されている分類
	 */
	public ProductClass getSelectedProductClass()
	{
		if(productClass.getSelectedRow() < 0)	return	null;
		
		return	(ProductClass)productClass.getValueAt(productClass.getSelectedRow(), 0);
	}
	
	
	/**
	 * 選択された商品（技術・商品）をセットする。
	 */
	private void setSelectedProduct()
	{
		int	index	=	product.getSelectedRow();
		
		if(index < 0)
		{
			selectedProduct	=	null;
			return;
		}
		
		ProductClass	selClass	=	this.getSelectedProductClass();
		
		selectedProduct	=	selClass.get(index);
		selectedProduct.setProductClass(selClass);
		
		if(opener instanceof SelectProductOpener)
		{
			SelectProductOpener	srp	=	(SelectProductOpener)opener;
			srp.addSelectedProduct(2, selectedProduct);
		}
	}
	
        /**
         * 選択されたコースをセットする
         */
        private void setSelectedCourse()
        {
		int	index	=	course.getSelectedRow();

		if(index < 0)
		{
			selectedCourse	=	null;
			return;
		}

		CourseClass	selClass	=	this.getSelectedCourseClass();

		selectedCourse	=	selClass.get(index);
		selectedCourse.setCourseClass(selClass);

		if(opener instanceof SelectProductOpener)
		{
			SelectProductOpener	srp	=	(SelectProductOpener)opener;
			srp.addSelectedCourse(5, selectedCourse);
		}
        }
        
	/**
	 * リラクゼーション用商品（技術・商品）検索画面用FocusTraversalPolicy
	 */
	private class SearchHairProductFocusTraversalPolicy
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
			if (aComponent.equals(productClass))
			{
				return product;
			}
			else if (aComponent.equals(product))
			{
				return product;
			}
			
			return productClass;
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
			if (aComponent.equals(productClass))
			{
				return productClass;
			}
			else if (aComponent.equals(product))
			{
				return productClass;
			}
			
			return productClass;
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return productClass;
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return product;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return productClass;
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
			return productClass;
		}
	}
	
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
                product.getColumnModel().getColumn(0).setPreferredWidth(350);
            
                course.getColumnModel().getColumn(0).setPreferredWidth(250);
                course.getColumnModel().getColumn(1).setPreferredWidth(45);

                SelectTableCellRenderer r = new SelectTableCellRenderer();
                r.setHorizontalAlignment(SwingConstants.RIGHT);

        }
}

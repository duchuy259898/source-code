/*
 * SearchHairProductDialog.java
 *
 * Created on 2006/05/26, 18:28
 */

package com.geobeck.sosia.pos.hair.search.product;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourseClass;
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
 * �����N�[�[�V�����p�Z�p�E���i�������
 * @author katagiri
 */
public class SearchHairProductDialog extends javax.swing.JDialog
{
        private MstShop shop = SystemInfo.getCurrentShop();

        //IVS_LVTu start add 2016/02/23 New request #48621
        private final int PRODUCT_TECHNIC       = 1;
        private final int PRODUCT_ITEM          = 2;
        private final int PRODUCT_COURSE        = 5;
        //IVS_LVTu end add 2016/02/23 New request #48621


	/**
	 * �R���X�g���N�^
	 * @param parent
	 * @param modal
	 * @param productDivision
	 */
	public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
			Component opener, Integer productDivision)
        {
            this(parent, modal, opener, productDivision, null);
        }

	public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
			Component opener, Integer productDivision, MstShop shop)
	{
                this(parent, modal, opener, productDivision, null, null);
	}

	public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
			Component opener, Integer productDivision, MstShop shop, MstCustomer customer)
	{
		super(parent, modal);
		this.opener = opener;
                this.customer = customer;
                if (shop != null) this.shop = shop;
		this.setProductDivision(productDivision);
		init();
		initComponents();
		addMouseCursorChange();
		this.initNames();
		SwingUtil.moveCenter(this);

		selectButton.setVisible( false );
                //�Z�p�̃R�[�X���ނɒl���Z�b�g
                showProductClass();
                //�Z�p�̃R�[�X���ނɑ΂���Z�p���Z�b�g
                showProducts();
                //�R�[�X�_��̃R�[�X���ނɒl���Z�b�g
                showCourseClass();
		this.initTableColumnWidth();
                // start add 20130802 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
                 //IVS NNTUAN START EDIT 20131028
                /*if (shop.getCourseFlag() == 0) {
                JtableproductDivision.remove(jPanel2);
                JtableproductDivision.remove(jPanel3);
                }*/
                if(this.shop.getShopID() != 0) {
                    if(this.shop.getCourseFlag() == 0){
                        JtableproductDivision.remove(panelCourse);
                        JtableproductDivision.remove(panelConsumption);
                    }
                }
                 //IVS NNTUAN END EDIT 20131028
                // end add 20130802 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
	}

      //IVS NNTUAN START ADD 20131103
       public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
            Component opener, Integer productDivision, MstShop shop, MstCustomer customer, boolean IsContractChange) {
            super(parent, modal);
            this.opener = opener;
            this.customer = customer;
            if (shop != null) {
                this.shop = shop;
            }

            this.setProductDivision(productDivision);
            init();
            initComponents();

            addMouseCursorChange();
            this.initNames();
            SwingUtil.moveCenter(this);

            selectButton.setVisible(false);

            if (IsContractChange) {
                showCourseClass();

                this.JtableproductDivision.setSelectedComponent(panelCourse);
                this.JtableproductDivision.remove(panelTechnic);
                this.JtableproductDivision.remove(panelConsumption);

            } else {
                //�Z�p�̃R�[�X���ނɒl���Z�b�g
                showProductClass();
                //�Z�p�̃R�[�X���ނɑ΂���Z�p���Z�b�g
                showProducts();
                //�R�[�X�_��̃R�[�X���ނɒl���Z�b�g
                showCourseClass();

                this.initTableColumnWidth();
                // start add 20130802 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
                //IVS NNTUAN START EDIT 20131028
                 /*if (shop.getCourseFlag() == 0) {
                 JtableproductDivision.remove(jPanel2);
                 JtableproductDivision.remove(jPanel3);
                 }*/
                if (this.shop.getShopID() != 0) {
                    if (this.shop.getCourseFlag() == 0) {
                        JtableproductDivision.remove(panelCourse);
                        JtableproductDivision.remove(panelConsumption);
                    }
                }
                //IVS NNTUAN END EDIT 20131028
                // end add 20130802 nakhoa �𖱋@�\�g�p�L���̐���ǉ�
            }

        }
	//IVS NNTUAN START ADD 20131103
	/**
	 * �R���X�g���N�^
	 * @param parent
	 * @param modal
	 * @param productDivision
	 */
	public SearchHairProductDialog(java.awt.Dialog parent, boolean modal,
			Component opener, Integer productDivision)
	{
		super(parent, modal);
		this.opener	=	opener;
		this.setProductDivision(productDivision);
		init();
		initComponents();
		addMouseCursorChange();
		this.initNames();
		SwingUtil.moveCenter(this);
	}


        public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
			Component opener, Integer productDivision, MstShop shop, MstCustomer customer, java.util.Date reservationDate)
	{
		super(parent, modal);
		this.opener = opener;
                this.customer = customer;
                if (shop != null) this.shop = shop;
		this.setProductDivision(productDivision);
		init();
		initComponents();
		addMouseCursorChange();
		this.initNames();
		SwingUtil.moveCenter(this);

		selectButton.setVisible( false );
                //�Z�p�̃R�[�X���ނɒl���Z�b�g
                showProductClass();
                //�Z�p�̃R�[�X���ނɑ΂���Z�p���Z�b�g
                showProducts();
                //�R�[�X�_��̃R�[�X���ނɒl���Z�b�g
                showCourseClass();
		this.initTableColumnWidth();
                if(this.shop.getShopID() != 0) {
                    if(this.shop.getCourseFlag() == 0){
                        JtableproductDivision.remove(panelCourse);
                        JtableproductDivision.remove(panelConsumption);
                    }
                }
                if(reservationDate !=null)
                {
                    this.resevationDate = reservationDate;
                }
	}
        //IVS_LVTu start add 2015/12/15 New request #44115
        public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
			Component opener, MstShop shop, MstCustomer customer, Integer productDivision)
	{
		super(parent, modal);
		this.opener = opener;
                this.customer = customer;
                if (shop != null) this.shop = shop;
		this.setProductDivision(productDivision);
		init();
		initComponents();
		addMouseCursorChange();
		//this.initNames();
		SwingUtil.moveCenter(this);

		selectButton.setVisible( false );

                //�Z�p�̃R�[�X���ނɒl���Z�b�g
                showProductClass();
                //�Z�p�̃R�[�X���ނɑ΂���Z�p���Z�b�g
                showProducts();

		this.initTableColumnWidth();

                //JtableproductDivision.remove(jPanel1);
                JtableproductDivision.remove(panelCourse);
                if (this.customer != null) {
                    //�ڋq��񂪂���ꍇ�̂ݎ��s
                    showConsumptionCourseClass();
                    showConsumptionCourse();
                }
	}
        //IVS_LVTu end add 2015/12/15 New request #44115

        //IVS_LVTu start add 2016/02/23 New request #48621
        public SearchHairProductDialog(java.awt.Frame parent, boolean modal,
			Component opener, MstShop shop, Integer productDivision)
	{
		super(parent, modal);
		this.opener = opener;
                if (shop != null) this.shop = shop;
		this.setProductDivision(productDivision);
		init();
		initComponents();
		addMouseCursorChange();
		this.setNames();
		SwingUtil.moveCenter(this);

		selectButton.setVisible( false );
                JtableproductDivision.remove(panelConsumption);
                //�Z�p�̃R�[�X���ނɒl���Z�b�g
                if(productDivision == this.PRODUCT_TECHNIC || productDivision == this.PRODUCT_ITEM) {
                    showProductClass();
                //�Z�p�̃R�[�X���ނɑ΂���Z�p���Z�b�g
                    showProducts();
                    JtableproductDivision.remove(panelCourse);
                }
                //�R�[�X�_��̃R�[�X���ނɒl���Z�b�g
                if(productDivision == this.PRODUCT_COURSE) {
                    showCourseClass();
                    showCourse();
                    JtableproductDivision.remove(panelTechnic);
                }
		this.initTableColumnWidth();
	}
        //IVS_LVTu end add 2016/02/23 New request #48621

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
        panelTechnic = new javax.swing.JPanel();
        productScrollPane = new javax.swing.JScrollPane();
        product = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        productClass = new javax.swing.JTable();
        panelCourse = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        courseClass = new javax.swing.JTable();
        productScrollPane1 = new javax.swing.JScrollPane();
        course = new javax.swing.JTable();
        panelConsumption = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        consumptionCourseClass = new javax.swing.JTable();
        productScrollPane2 = new javax.swing.JScrollPane();
        consumptionCourse = new javax.swing.JTable();

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

        panelTechnic.setPreferredSize(new java.awt.Dimension(401, 313));

        productScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        product.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "���i��", "���z(�ō�)"
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
        product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productMouseClicked(evt);
            }
        });
        productScrollPane.setViewportView(product);

        productClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "�\�񃁃j���["
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
        jScrollPane3.setViewportView(productClass);

        org.jdesktop.layout.GroupLayout panelTechnicLayout = new org.jdesktop.layout.GroupLayout(panelTechnic);
        panelTechnic.setLayout(panelTechnicLayout);
        panelTechnicLayout.setHorizontalGroup(
            panelTechnicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelTechnicLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(productScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTechnicLayout.setVerticalGroup(
            panelTechnicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelTechnicLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelTechnicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .add(productScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addContainerGap())
        );

        JtableproductDivision.addTab("�Z�p", panelTechnic);

        courseClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "�R�[�X����"
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
                "�R�[�X��", "��", "���z�i�ō��j"
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

        org.jdesktop.layout.GroupLayout panelCourseLayout = new org.jdesktop.layout.GroupLayout(panelCourse);
        panelCourse.setLayout(panelCourseLayout);
        panelCourseLayout.setHorizontalGroup(
            panelCourseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelCourseLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(productScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCourseLayout.setVerticalGroup(
            panelCourseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelCourseLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelCourseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, productScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addContainerGap())
        );

        JtableproductDivision.addTab("�R�[�X�_��", panelCourse);

        consumptionCourseClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "�R�[�X����"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        consumptionCourseClass.setSelectionBackground(new java.awt.Color(220, 220, 220));
        consumptionCourseClass.setSelectionForeground(new java.awt.Color(0, 0, 0));
        consumptionCourseClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consumptionCourseClass.getTableHeader().setReorderingAllowed(false);
        consumptionCourseClass.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(consumptionCourseClass);
        SwingUtil.setJTableHeaderRenderer(consumptionCourseClass, SystemInfo.getTableHeaderRenderer());
        consumptionCourseClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                consumptionCourseClassMouseReleased(evt);
            }
        });
        consumptionCourseClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                consumptionCourseClassKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(consumptionCourseClass);

        productScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        consumptionCourse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "�R�[�X��", "�c��", "�_���", "�L������"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        consumptionCourse.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        consumptionCourse.setSelectionBackground(new java.awt.Color(220, 220, 220));
        consumptionCourse.setSelectionForeground(new java.awt.Color(0, 0, 0));
        consumptionCourse.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consumptionCourse.getTableHeader().setReorderingAllowed(false);
        consumptionCourse.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(consumptionCourse);
        SwingUtil.setJTableHeaderRenderer(consumptionCourse, SystemInfo.getTableHeaderRenderer());
        consumptionCourse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                consumptionCourseMouseClicked(evt);
            }
        });
        productScrollPane2.setViewportView(consumptionCourse);

        org.jdesktop.layout.GroupLayout panelConsumptionLayout = new org.jdesktop.layout.GroupLayout(panelConsumption);
        panelConsumption.setLayout(panelConsumptionLayout);
        panelConsumptionLayout.setHorizontalGroup(
            panelConsumptionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelConsumptionLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(productScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelConsumptionLayout.setVerticalGroup(
            panelConsumptionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelConsumptionLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelConsumptionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, productScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addContainerGap())
        );

        JtableproductDivision.addTab("�����R�[�X", panelConsumption);

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
	 * ���ނ��ύX���ꂽ�Ƃ��̏���
	 * @param evt
	 */
	/**
	 * �߂�{�^���������ꂽ�Ƃ��̏���
	 * @param evt
	 */
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backButtonActionPerformed
	{//GEN-HEADEREND:event_backButtonActionPerformed
		SystemInfo.getLogger().log(Level.INFO, "�߂�");
		this.setVisible(false);
	}//GEN-LAST:event_backButtonActionPerformed

	/**
	 * �I���{�^���������ꂽ�Ƃ��̏���
	 * @param evt
	 */
	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
	{//GEN-HEADEREND:event_selectButtonActionPerformed
		this.setSelectedProduct();
	}//GEN-LAST:event_selectButtonActionPerformed

        private void productMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productMouseClicked
            if(evt.getClickCount() == 2) {
                if(0 <= product.getSelectedRow()) {
                    this.setSelectedProduct();
                }
            }
}//GEN-LAST:event_productMouseClicked

        private void courseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseMouseClicked
            if(evt.getClickCount() == 2) {
                if(0 <= course.getSelectedRow()) {
                    setSelectedCourse();
                }
            }
        }//GEN-LAST:event_courseMouseClicked

        private void consumptionCourseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consumptionCourseMouseClicked
            if(evt.getClickCount() == 2) {
                if(0 <= consumptionCourse.getSelectedRow()) {
                    setSelectedConsumptionCourse();
                }
            }
        }//GEN-LAST:event_consumptionCourseMouseClicked

        private void JtableproductDivisionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JtableproductDivisionStateChanged
            if(JtableproductDivision.getSelectedIndex() == 0) {
                //�Z�p�^�u��I�������ꍇ
                showProducts();
            } else if (JtableproductDivision.getSelectedIndex() == 1) {
                //�R�[�X�_��^�u��I�������ꍇ
                showCourse();
            } else if (JtableproductDivision.getSelectedIndex() == 2) {
                //�����R�[�X�^�u��I�������ꍇ
                if (this.customer != null) {
                    //�ڋq��񂪂���ꍇ�̂ݎ��s
                    showConsumptionCourseClass();
                    showConsumptionCourse();
                }
            }
        }//GEN-LAST:event_JtableproductDivisionStateChanged

        private void courseClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_courseClassKeyReleased
            showCourse();
        }//GEN-LAST:event_courseClassKeyReleased

        private void courseClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseClassMouseReleased
            showCourse();
        }//GEN-LAST:event_courseClassMouseReleased

        private void consumptionCourseClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_consumptionCourseClassKeyReleased
            showConsumptionCourse();
        }//GEN-LAST:event_consumptionCourseClassKeyReleased

        private void consumptionCourseClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consumptionCourseClassMouseReleased
            showConsumptionCourse();
        }//GEN-LAST:event_consumptionCourseClassMouseReleased

        private void productClassMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productClassMouseReleased
            showProducts();
        }//GEN-LAST:event_productClassMouseReleased

        private void productClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productClassKeyReleased
            showProducts();
        }//GEN-LAST:event_productClassKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane JtableproductDivision;
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JTable consumptionCourse;
    private javax.swing.JTable consumptionCourseClass;
    private javax.swing.JTable course;
    private javax.swing.JTable courseClass;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelConsumption;
    private javax.swing.JPanel panelCourse;
    private javax.swing.JPanel panelTechnic;
    private javax.swing.JTable product;
    private javax.swing.JTable productClass;
    private javax.swing.JScrollPane productScrollPane;
    private javax.swing.JScrollPane productScrollPane1;
    private javax.swing.JScrollPane productScrollPane2;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables

	private	Component opener				=	null;

        private MstCustomer customer = null;

	private	ProductClasses	productClasses	=	new ProductClasses();

        private CourseClasses courseClasses = new CourseClasses();

        private CunsumptionCourseClasses consumptionCourseClasses = new CunsumptionCourseClasses();

	/**
	 * �I�����ꂽ���i�i�Z�p�E���i�j
	 */
	private	Product			selectedProduct	=	null;
	/**
         * �I�����ꂽ�R�[�X
         */
	private Course selectedCourse = null;
        /**
         * �I�����ꂽ�����R�[�X
         */
        private ConsumptionCourse selectedConsumptionCourse = null;

        // vtbphuong start add 20140423 Bug #22496
        private java.util.Date resevationDate = null;
        // vtbphuong start add 20140423 Bug #22496

	/**
	 * �����N�[�[�V�����p���i�i�Z�p�E���i�j������ʗpFocusTraversalPolicy
	 */
	private	SearchHairProductFocusTraversalPolicy	ftp	=
			new SearchHairProductFocusTraversalPolicy();

	/**
	 * �����N�[�[�V�����p���i�i�Z�p�E���i�j������ʗpFocusTraversalPolicy���擾����B
	 * @return �����N�[�[�V�����p���i�i�Z�p�E���i�j������ʗpFocusTraversalPolicy
	 */
	public SearchHairProductFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}

	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(selectButton);
		SystemInfo.addMouseCursorChange(backButton);
	}

	/**
	 * �I�����ꂽ���i�i�Z�p�E���i�j���擾����B
	 * @return �I�����ꂽ���i�i�Z�p�E���i�j
	 */
	public Product getSelectedProduct()
	{
		return selectedProduct;
	}

	/**
	 * �I�����ꂽ���i�i�Z�p�E���i�j���Z�b�g����B
	 * @param selectedProduct �I�����ꂽ���i�i�Z�p�E���i�j
	 */
	public void setSelectedProduct(Product selectedProduct)
	{
		this.selectedProduct = selectedProduct;
	}

	/**
	 * ���i�敪���擾����B
	 * @return 1�F�Z�p�A2�F���i
	 */
	public Integer getProductDivision()
	{
		return productClasses.getProductDivision();
	}

	/**
	 * ���i�敪���Z�b�g����B
	 * @param productDivision 1�F�Z�p�A2�F���i
	 */
	public void setProductDivision(Integer productDivision)
	{
		productClasses.setProductDivision(productDivision);
	}

	private void initNames()
	{
		DefaultTableModel	model	=	(DefaultTableModel)product.getModel();

		switch(this.getProductDivision())
		{
			case 1:
                                //IVS_TMTrong start edit 2015/09/03 New request #42426
				//this.setTitle("�Z�p����");
                                this.setTitle("�\�񃁃j���[�I��");
                                //IVS_TMTrong end edit 2015/09/03 New request #42426
				model.setColumnIdentifiers(new String[]{"�Z�p��", "���z(�ō�)"});
				break;
			case 2:
				this.setTitle("���i����");
				model.setColumnIdentifiers(new String[]{"���i��", "���z(�ō�)"});
				break;
		}

		SwingUtil.setJTableHeaderRenderer(product, SystemInfo.getTableHeaderRenderer());
	}

        //IVS_LVTu start add 2016/02/23 New request #48621
        private void setNames()
	{
		DefaultTableModel	model	=	(DefaultTableModel)product.getModel();

		switch(this.getProductDivision())
		{
			case 1:
                                this.setTitle("�\�񃁃j���[�I��");
				model.setColumnIdentifiers(new String[]{"�Z�p��", "���z(�ō�)"});
				break;
			case 2:
				this.setTitle("���i����");
				model.setColumnIdentifiers(new String[]{"���i��", "���z(�ō�)"});
                                for (int i = 0;i <JtableproductDivision.getComponentCount();i ++) {
                                    if(JtableproductDivision.getComponent(i).equals(panelTechnic)) {
                                        JtableproductDivision.setTitleAt(i, "���i");
                                    }
                                }
				break;
                        case 5:
				this.setTitle("�R�[�X����");
				for (int i = 0;i <JtableproductDivision.getComponentCount();i ++) {
                                    if(JtableproductDivision.getComponent(i).equals(panelCourse)) {
                                        JtableproductDivision.setTitleAt(i, "�R�[�X");
                                    }
                                }
				break;
		}

		SwingUtil.setJTableHeaderRenderer(product, SystemInfo.getTableHeaderRenderer());
	}
        //IVS_LVTu end add 2016/02/23 New request #48621

	/**
	 * �������������s���B
	 */
	private void init()
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();

			productClasses.load(con, this.shop.getShopID());

                        //�R�[�X���ގ擾
                        courseClasses.setProductDivision(3);
                        courseClasses.load(con, this.shop.getShopID());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

        /**
         * �R�[�X�_��̃R�[�X���ރ��X�g��\������
         */
        private void showCourseClass() {
            DefaultTableModel model = (DefaultTableModel)courseClass.getModel();

            //�S�s�폜
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
         * �R�[�X�_��̑I�����ꂽ�R�[�X���ނɕR�t���R�[�X��\������
         */
        private void showCourse() {
            DefaultTableModel model = (DefaultTableModel)course.getModel();

            //�S�s�폜
            SwingUtil.clearTable(course);

            //�I������Ă��镪�ނ��擾
            CourseClass cc = this.getSelectedCourseClass();

            if (cc == null) {
                //�I������Ă��Ȃ��ꍇ�͂Ȃɂ����Ȃ�
                return;
            }

            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                //Start edit 20131230 lvut �{�����O�C�����̗\��o�^���j���[�ɂ���
                if(this.shop != null){
                    cc.loadCourse(con, this.shop.getShopID(), cc.getCourseClassId());
                }else{
                    cc.loadCourse(con, SystemInfo.getCurrentShop().getShopID(), cc.getCourseClassId());
                }
                //End edit 20131230 lvut �{�����O�C�����̗\��o�^���j���[�ɂ���
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            for (Course c : cc) {
                Object[] rowData = { c, c.getNum(), c.getPrice() };
                model.addRow(rowData);
            }
        }

        /**
	 * �I������Ă��镪�ނ��擾����B
	 * @return �I������Ă��镪��
	 */
	public CourseClass getSelectedCourseClass()
	{
		if(courseClass.getSelectedRow() < 0)	return	null;

		return	(CourseClass)courseClass.getValueAt(courseClass.getSelectedRow(), 0);
	}

        /**
         * �����R�[�X�̃R�[�X���ރ��X�g��\������
         */
        private void showConsumptionCourseClass() {
            DefaultTableModel model = (DefaultTableModel)consumptionCourseClass.getModel();

            //�S�s�폜
            SwingUtil.clearTable(consumptionCourseClass);

            try {
                //�w���R�[�X�擾
                ConnectionWrapper con = SystemInfo.getConnection();
                if(this.resevationDate == null){
                    consumptionCourseClasses.loadConsumptionCourseClass(con, SystemInfo.getCurrentShop().getShopID(), this.customer.getCustomerID());
                }else{
                    consumptionCourseClasses.loadConsumptionCourseClass(con, SystemInfo.getCurrentShop().getShopID(), this.customer.getCustomerID(), this.resevationDate);
                }

            }
            catch (SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            for (ConsumptionCourseClass cc : consumptionCourseClasses) {
                Object[] rowData = {cc};
                model.addRow(rowData);
            }

            if (0 < consumptionCourseClass.getRowCount()) {
                consumptionCourseClass.setRowSelectionInterval(0, 0);
            }
        }

        /**
         * �����R�[�X�̑I�����ꂽ�R�[�X���ނɕR�t���R�[�X��\������
         */
        private void showConsumptionCourse() {
            DefaultTableModel model = (DefaultTableModel)consumptionCourse.getModel();

            //�S�s�폜
            SwingUtil.clearTable(consumptionCourse);

            //�I������Ă��镪�ނ��擾
            ConsumptionCourseClass ccc = this.getSelectedConsumptionCourseClass();

            if (ccc == null) {
                //�I������Ă��Ȃ��ꍇ�͂Ȃɂ����Ȃ�
                return;
            }

            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                if(this.resevationDate == null){
                    ccc.loadConsumptionCourseWithSalesDate(con, SystemInfo.getCurrentShop().getShopID(), this.customer.getCustomerID(), ccc.getCourseClassId());
                }else{
                    ccc.loadConsumptionCourseWithSalesDate(con, SystemInfo.getCurrentShop().getShopID(), this.customer.getCustomerID(), ccc.getCourseClassId(),this.resevationDate);
                }

            }
            catch (SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            NumberFormat nf = NumberFormat.getInstance();
            for (ConsumptionCourse cc : ccc) {
                cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
                //Start edit 20131202 lvut
//                String praiseTimeLimit = "";
//                if(cc.isIsPraiseTime()){
//                    //�L������������ꍇ�L���������Z�b�g����i�w�����t�{�R�[�X�ɐݒ肳��Ă���L�������j
//                    Calendar cal = Calendar.getInstance();
//                    cal.clear();
//                    cal.setTimeInMillis(cc.getSalesDate().getTime());
//                    cal.add(Calendar.MONTH, cc.getPraiseTimeLimit());
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                    praiseTimeLimit = sdf.format(cal.getTime());
//                }
                //IVS_TMTrong start edit 2015/09/03 New request #42426
                //Object[] rowData = { cc, nf.format(cc.getConsumptionRestNum()), cc.getValidDate() };
				// 20190905 �\�񎞂ɗL�������`�F�b�N
				if(this.resevationDate == null ||  (cc.getValidDate()==null || cc.getValidDate()!=null && this.resevationDate.compareTo(cc.getValidDate()) < 1 ) ){
	                Object[] rowData = { cc, nf.format(cc.getConsumptionRestNum()), cc.getSalesDate(), cc.getValidDate() };
	                //IVS_TMTrong end edit 2015/09/03 New request #42426
	                //End edit 20131202 lvut
	                model.addRow(rowData);
				}
            }
        }

                /**
	 * �I������Ă��镪�ނ��擾����B
	 * @return �I������Ă��镪��
	 */
	public ConsumptionCourseClass getSelectedConsumptionCourseClass()
	{
		if(consumptionCourseClass.getSelectedRow() < 0)	return	null;

		return	(ConsumptionCourseClass)consumptionCourseClass.getValueAt(consumptionCourseClass.getSelectedRow(), 0);
	}

        /**
         * �R�[�X�_��̃R�[�X���ރ��X�g��\������
         */
        private void showProductClass() {
            DefaultTableModel model = (DefaultTableModel)productClass.getModel();

            //�S�s�폜
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
	 * ���i�̃��X�g��\������B
	 */
	private void showProducts()
	{
		DefaultTableModel	model	=	(DefaultTableModel)product.getModel();

		//�S�s�폜
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
	 * �I������Ă��镪�ނ��擾����B
	 * @return �I������Ă��镪��
	 */
	public ProductClass getSelectedProductClass()
	{
		if(productClass.getSelectedRow() < 0)	return	null;

		return	(ProductClass)productClass.getValueAt(productClass.getSelectedRow(), 0);
	}


	/**
	 * �I�����ꂽ���i�i�Z�p�E���i�j���Z�b�g����B
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

		if(opener instanceof SearchHairProductOpener)
		{
			SearchHairProductOpener	srp	=	(SearchHairProductOpener)opener;
			srp.addSelectedProduct(this.getProductDivision(), selectedProduct);
		}
	}

        /**
         * �I�����ꂽ�R�[�X���Z�b�g����
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

		if(opener instanceof SearchHairProductOpener)
		{
			SearchHairProductOpener	srp	=	(SearchHairProductOpener)opener;
                        //�R�[�X�_���product division�́u5�v
			srp.addSelectedCourse(5, selectedCourse);
		}
        }

        /**
         * �I�����ꂽ�����R�[�X���Z�b�g����
         */
        private void setSelectedConsumptionCourse()
        {
		int	index	=	consumptionCourse.getSelectedRow();

		if(index < 0)
		{
			selectedConsumptionCourse	=	null;
			return;
		}

		ConsumptionCourseClass	selClass	=	this.getSelectedConsumptionCourseClass();

		selectedConsumptionCourse	=	selClass.get(index);
		selectedConsumptionCourse.setConsumptionCourseClass(selClass);

		if(opener instanceof SearchHairProductOpener)
		{
			SearchHairProductOpener	srp	=	(SearchHairProductOpener)opener;
                        //�����R�[�X��product division�́u6�v
			srp.addSelectedConsumptionCourse(6, selectedConsumptionCourse);
		}
        }

	/**
	 * �����N�[�[�V�����p���i�i�Z�p�E���i�j������ʗpFocusTraversalPolicy
	 */
	private class SearchHairProductFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent �̂��ƂŃt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
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
		 * aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component ��Ԃ��܂��B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̑O�Ƀt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
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
		 * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return productClass;
		}

		/**
		 * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return product;
		}

		/**
		 * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
		 * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return productClass;
		}

		/**
		 * �E�B���h�E���ŏ��ɕ\�����ꂽ�Ƃ��Ƀt�H�[�J�X���ݒ肳���R���|�[�l���g��Ԃ��܂��B
		 * show() �܂��� setVisible(true) �̌Ăяo���ň�x�E�B���h�E���\�������ƁA
		 * �������R���|�[�l���g�͂���ȍ~�g�p����܂���B
		 * ��x�ʂ̃E�B���h�E�Ɉڂ����t�H�[�J�X���Ăѐݒ肳�ꂽ�ꍇ�A
		 * �܂��́A��x��\����ԂɂȂ����E�B���h�E���Ăѕ\�����ꂽ�ꍇ�́A
		 * ���̃E�B���h�E�̍Ō�Ƀt�H�[�J�X���ݒ肳�ꂽ�R���|�[�l���g���t�H�[�J�X���L�҂ɂȂ�܂��B
		 * ���̃��\�b�h�̃f�t�H���g�����ł̓f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * @param window �����R���|�[�l���g���Ԃ����E�B���h�E
		 * @return �ŏ��ɃE�B���h�E���\�������Ƃ��Ƀt�H�[�J�X�ݒ肳���R���|�[�l���g�B�K�؂ȃR���|�[�l���g���Ȃ��ꍇ�� null
		 */
		public Component getInitialComponent(Window window)
		{
			return productClass;
		}
	}

	/**
	 * JTable�̗񕝂�����������B
	 */
	private void initTableColumnWidth()
	{
		//��̕���ݒ肷��B
                //IVS_TMTrong start edit 2015/09/03 New request #42426
		//product.getColumnModel().getColumn(0).setPreferredWidth(240);
                product.getColumnModel().getColumn(0).setPreferredWidth(350);
                //IVS_TMTrong end edit 2015/09/03 New request #42426

                //�R�[�X�_��e�[�u���̃J����������
                //IVS_TMTrong start edit 2015/09/03 New request #42426
                //course.getColumnModel().getColumn(1).setPreferredWidth(20);
                //course.getColumnModel().getColumn(2).setPreferredWidth(50);
                course.getColumnModel().getColumn(0).setPreferredWidth(250);
                course.getColumnModel().getColumn(1).setPreferredWidth(45);
                //IVS_TMTrong start edit 2015/09/03 New request #42426

                //�����R�[�X�e�[�u���̃J����������
                //IVS_TMTrong start edit 2015/09/03 New request #42426
                //consumptionCourse.getColumnModel().getColumn(1).setPreferredWidth(30);
                //consumptionCourse.getColumnModel().getColumn(2).setPreferredWidth(50);
                consumptionCourse.getColumnModel().getColumn(0).setPreferredWidth(250);
                consumptionCourse.getColumnModel().getColumn(1).setPreferredWidth(45);
                //IVS_TMTrong end edit 2015/09/03 New request #42426

                SelectTableCellRenderer r = new SelectTableCellRenderer();
                r.setHorizontalAlignment(SwingConstants.RIGHT);
                consumptionCourse.getColumnModel().getColumn(1).setCellRenderer(r);

        }
}

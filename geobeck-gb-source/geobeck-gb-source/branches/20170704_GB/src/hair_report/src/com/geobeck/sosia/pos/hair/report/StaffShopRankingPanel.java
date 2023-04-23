/*
 * StaffShopRankingPanelNons.java
 *
 * Created on 2013/01/20, 9:00
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.basicinfo.SimpleMaster;
import com.geobeck.sosia.pos.basicinfo.commodity.EditabeTableCellRenderer;
import java.awt.*;
import java.util.Date;
import java.text.*;
import java.sql.*;
import javax.swing.*;
import java.util.logging.*;
import java.util.ArrayList;
import javax.swing.table.*;
import java.math.BigDecimal;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.hair.report.util.*;
import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.merge.cell.MultiSpanCellTableEx;
import com.geobeck.sosia.pos.util.MessageUtil;
import java.util.Vector;
import com.geobeck.sosia.pos.merge.cell.AttributiveCellTableModel;
import com.geobeck.sosia.pos.merge.cell.CellAttribute;
import com.geobeck.sosia.pos.merge.cell.CellSpan;
import com.geobeck.sosia.pos.merge.cell.DefaultCellAttribute;
import com.geobeck.sosia.pos.merge.cell.MultiSpanCellTableUI;
import java.util.Calendar;
/**
 *
 * @author  IVS_tttung
 */
public class StaffShopRankingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
        //nhanvt start add 20141216 New request #33406
        private int useShopCategory = 0;
        private boolean isHideCategory = false;
        private MstShopCategorys		mscg		=	new MstShopCategorys();
        private String lstShopCategoryName = "";
        //LVTu start add 2016/01/19 New request #46728
        private boolean flagCourse = false;
        //LVTu end add 2016/01/19 New request #46728
        
        //nhanvt end add 20141216 New request #33406
	/**
	 * ��S���E�{�p�S�������L���O���X�g
	 */
	private StaffRankingList    stl = null;

	/**
	 * �X�܃����L���O���X�g
	 */
	private ShopRankingList	    srl = null;
       

	/**
	 * �Z�p�E���i�����L���O���X�g
	 */
	private ProductRankingList  trl = null;
        
	/**
	 * �ڋq�����L���O���X�g
	 */
	private CustomerRankingList  crl = null;

        /**
	 * �Z�p�E�l���ド���L���O���X�g
	 */
	private IndividualSalesRankingList  isl = null;
        
	/**
	 * �R���|�[�l���g�������t���O
	 */
	private boolean blnInitComp = false;

        /**
         * �\���{�^�������t���O
         */
        private boolean isShowButtonClicked = false;   
        
        private boolean isNonsSystem = false;

        /** Creates new form StaffShopRankingPanelNons */
	public StaffShopRankingPanel()
	{
                initComponents();
                blnInitComp = true;         //�R���|�[�l���g�������ς�
		addMouseCursorChange();
		this.setSize(830, 680);
		this.setPath("���[�o��");
		this.setTitle("�����L���O");
		this.setKeyListener();
		this.initTableColumnWidth();

                target.addItem(SystemInfo.getGroup());
                SystemInfo.getGroup().addGroupDataToJComboBox(target, 3);
                target.setSelectedIndex(0);
                
                this.initStaff();
                
                //�Ώۊ��Ԃ̐ݒ�
		this.cmbTargetPeriodStartDate.setDate(new Date());
		this.cmbTargetPeriodEndDate.setDate(new Date());
		
		//�Ŕ��A�ō��̏����ݒ�
		if(SystemInfo.getAccountSetting().getReportPriceType() == 0)
		{
			rdoTaxBlank.setSelected(false);
			rdoTaxUnit.setSelected(true);
		}
		else
		{
			rdoTaxBlank.setSelected(true);
			rdoTaxUnit.setSelected(false);
		}
                //�\�����p�R���{�{�b�N�X�̐ݒ�
                initOrderDisplayComponents(orderDisplay);   
                
                //���o�����̊����E�񊈐�����
                initComponentsEnabled();
                
                // �w�b�_��ύX
                JTableHeader mainStaffHeader = mainStaff.getTableHeader();
                JTableHeader techStaffHeader = techStaff.getTableHeader();
                JTableHeader shopHeader = shop.getTableHeader();
                JTableHeader techHeader = tech.getTableHeader();
                JTableHeader itemHeader = item.getTableHeader();
                JTableHeader decileHeader = decile.getTableHeader();
                JTableHeader individualsalesHeader = individualsales.getTableHeader();
                
                mainStaffHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));
                techStaffHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));
                shopHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));
                techHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));
                itemHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));
                decileHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));
                individualsalesHeader.setFont(new java.awt.Font("MS UI Gothic", 1, 6));

                //����������
                this.init();

                if (!SystemInfo.checkAuthority(100)) {
                    ranking.remove(mainStaffScroll);
                }
                if (!SystemInfo.checkAuthority(101)) {
                    ranking.remove(techStaffScroll);
                }
                if (!SystemInfo.checkAuthority(102)) {
                    ranking.remove(shopScroll);
                }
                if (!SystemInfo.checkAuthority(103)) {
                    ranking.remove(techScroll);
                }
                if (!SystemInfo.checkAuthority(104)) {
                    ranking.remove(itemScroll);
                }
                //IVS_LVTu start edit 2016/02/24 New request #48455
                //if (!SystemInfo.checkAuthority(105)) {
                if ((!SystemInfo.checkAuthority(105)) || (SystemInfo.getSystemType().equals(2))) {
                    ranking.remove(decileScroll);
                }
                //IVS_LVTu end edit 2016/02/24 New request #48455
                //nhanvt start add 20141216 New request #33406
                
                if(!flagCourse) {
                    ranking.remove(courseScroll);
                }
                //nhanvt end add 20141216 New request #33406
                if(!(SystemInfo.getDatabase().startsWith("pos_hair_nons") ||SystemInfo.getDatabase().startsWith("pos_hair_nons_bak"))){
                    isNonsSystem = true;
                }
                if(isNonsSystem){
                    initTableShopRankingOld();
                }
	}
        
        //LVTu start edit 2016/01/19 New request #46728
        private void initTableShopRankingOld(){
            shop = new  MultiSpanCellTableEx();
            shop.setUI(new MultiSpanCellTableUI());
            if (flagCourse) {
                shop.setModel(new AttributiveCellTableModel(
                    new Object [][] {
                    },
                    new String [] {
                        "����", "�X�ܖ�", "���q��", "�Z�p�q��", "<html>&nbsp;&nbsp;�Z�p<br>�V�K�q��</html>", "<html>&nbsp;&nbsp;�Z�p<br>�V�K�䗦</html>", "�Z�p����", "�Z�p�q�P��", "�_����z", "�������z", "���i����", "������", "�q�P��", "�ė���", "���X�^�b�t"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false, false, false, false, false, false, false, false,false, false
                    };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            }else {
                shop.setModel(new AttributiveCellTableModel(
                    new Object [][] {
                    },
                    new String [] {
                        "����", "�X�ܖ�", "���q��", "�Z�p�q��", "<html>&nbsp;&nbsp;�Z�p<br>�V�K�q��</html>", "<html>&nbsp;&nbsp;�Z�p<br>�V�K�䗦</html>", "�Z�p����", "�Z�p�q�P��", "���i����", "������", "�q�P��", "�ė���", "���X�^�b�t"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false, false, false, false, false, false,false, false
                    };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            }
                TableColumnModel modelColumn = shop.getColumnModel();
                modelColumn.getColumn(2).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(3).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(4).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(5).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(6).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(7).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(8).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(9).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(10).setCellRenderer(new MinusCellBlueRenderer());
                modelColumn.getColumn(11).setCellRenderer(new MinusCellBlueRenderer());
                //IVS_LVTu start edit 2015/05/11 Bug #36633
                modelColumn.getColumn(12).setCellRenderer(new MinusCellBlueRenderer());
                if (flagCourse) {
                    modelColumn.getColumn(13).setCellRenderer(new MinusCellBlueRenderer());
                    modelColumn.getColumn(14).setCellRenderer(new MinusCellBlueRenderer());
                }
                //IVS_LVTu end edit 2015/05/11 Bug #36633
                
            
                
            shop.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

            shop.setSelectionBackground(new java.awt.Color(220, 220, 220));

            shop.setSelectionForeground(new java.awt.Color(0, 0, 0));
            shop.getTableHeader().setReorderingAllowed(false);
            shop.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            shop.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
            SwingUtil.setJTableHeaderRenderer(shop, SystemInfo.getTableHeaderRenderer());
            shop.setRowHeight(20);
            shopScroll.setViewportView(shop);

   
        }
        
        public void initTableRankingMain(){
            
            
        mainStaff = new MultiSpanCellTableEx();
        mainStaff.setUI(new MultiSpanCellTableUI());
        if (flagCourse) {
            mainStaff.setModel(new AttributiveCellTableModel(
                new Object [][] {

                },
                new String [] {
                    "����", "���O", "�X��", "���q��", "<html>�Z�p<br>�q��</html>", "<html>�Z�p<br>����</html>", "<html>�w��<br>�q��</html>", "<html>�w��<br>����</html>", "<html>�V�K<br>�q��</html>", "<html>�Љ�<br>�q��</html>", "<html>�_��<br>���z</html>", "<html>����<br>���z</html>", "<html>���i<br>����</html>", "������", "<html>���q<br>�P��</html>", "<html>&nbsp;�Z�p<br>�q�P��</html>", "�ė���"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class,java.lang.Long.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
        }else {
            mainStaff.setModel(new AttributiveCellTableModel(
                new Object [][] {

                },
                new String [] {
                    "����", "���O", "�X��", "���q��", "<html>�Z�p<br>�q��</html>", "�Z�p����", "<html>�w��<br>�q��</html>", "�w������", "<html>�V�K<br>�q��</html>", "<html>�Љ�<br>�q��</html>", "���i����", "������", "���q�P��", "<html>&nbsp;�Z�p<br>�q�P��</html>", "�ė���"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class,java.lang.Long.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
        }
        TableColumnModel modelColumnMain1 = mainStaff.getColumnModel();
        modelColumnMain1.getColumn(3).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain1.getColumn(4).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(5).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(6).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(7).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(8).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(9).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(10).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(11).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(12).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(13).setCellRenderer(new MinusCellBlueRenderer());
         modelColumnMain1.getColumn(14).setCellRenderer(new MinusCellBlueRenderer());
        if (flagCourse) {
            modelColumnMain1.getColumn(15).setCellRenderer(new MinusCellBlueRenderer());
            modelColumnMain1.getColumn(16).setCellRenderer(new MinusCellBlueRenderer());
         }
        mainStaff.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        mainStaff.setSelectionBackground(new java.awt.Color(220, 220, 220));
        mainStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
        mainStaff.getTableHeader().setReorderingAllowed(false);
        mainStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtil.setJTableHeaderRenderer(mainStaff, SystemInfo.getTableHeaderRenderer());
        SelectTableCellRenderer.setSelectTableCellRenderer(mainStaff);
        mainStaff.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);    
        mainStaff.setRowHeight(20);
        mainStaffScroll.setViewportView(mainStaff);
        
        }
        
        public void initTableRankingTech(){
            techStaff = new com.geobeck.swing.JTableEx();
            if(flagCourse) {
                techStaff.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                    },
                    new String [] {
                        "����", "���O", "�X��", "<html>&nbsp;&nbsp;&nbsp;��<br>�S����</html>", "<html>�{�p<br>�q��</html>", "<html>�{�p<br>����</html>", "<html>&nbsp;&nbsp;�{�p<br>�w���q��</html>", "<html>&nbsp;&nbsp;�{�p<br>�w������</html>", "<html>&nbsp;AP<br>�q��</html>", "<html>&nbsp;AP<br>����</html>", "<html>�_��<br>���z</html>", "<html>����<br>���z</html>", "<html>���i<br>����</html>", "<html>�S��<br>����</html>", "�q�P��", "<html>&nbsp;&nbsp;�{�p<br>�q�P��</html>"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
            }else {
                techStaff.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                    },
                    new String [] {
                        "����", "���O", "�X��", "<html>&nbsp;&nbsp;&nbsp;��<br>�S����</html>", "<html>�{�p<br>�q��</html>", "<html>�{�p<br>����</html>", "<html>&nbsp;&nbsp;�{�p<br>�w���q��</html>", "<html>&nbsp;&nbsp;�{�p<br>�w������</html>", "<html>&nbsp;AP<br>�q��</html>", "<html>&nbsp;AP<br>����</html>", "<html>���i<br>����</html>", "<html>�S��<br>����</html>", "�q�P��", "<html>&nbsp;&nbsp;�{�p<br>�q�P��</html>"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false, false, false, true, true, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
            }

            techStaff.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

            techStaff.setSelectionBackground(new java.awt.Color(220, 220, 220));

            techStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
            techStaff.getTableHeader().setReorderingAllowed(false);
            techStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            SwingUtil.setJTableHeaderRenderer(techStaff, SystemInfo.getTableHeaderRenderer());
            techStaff.setRowHeight(40);
            SelectTableCellRenderer.setSelectTableCellRenderer(techStaff);
            techStaffScroll.setViewportView(techStaff);
        }
        
	/**
	 * �\�����p�R���{�{�b�N�X������������
	 * @param combobox �R���{�{�b�N�X
	 */
	private void initOrderDisplayComponents(JComboBox combobox)
	{
            //�N���A
            combobox.removeAllItems();
            decileTotalScroll.setVisible(false);
            this.productDivisionMessage.setVisible(false);
            this.message2.setText("");

            //�\�����p�R���{�{�b�N�X�̐ݒ�

            //��S���^�u
            if (ranking.getSelectedComponent().equals(mainStaffScroll)) {
                combobox.addItem("���q��");
                combobox.addItem("�Z�p�q��");
                combobox.addItem("�Z�p����");
                combobox.addItem("�w���q��");
                combobox.addItem("�w������");
                combobox.addItem("�V�K�q��");
                combobox.addItem("�Љ�q��");
                combobox.addItem("���i����");
                combobox.addItem("������");
                combobox.addItem("���q�P��");
                combobox.addItem("�Z�p�q�P��");
                combobox.setSelectedIndex(8);
                //check course flag
                if (this.flagCourse) {
                    this.message.setText("��������ɂ͑S�̊����͊܂܂�Ă���܂���B�@�����q���́A�Z�p�q�{�X�̂̂݋q�{�_��q�{�����q�ƂȂ�܂��B�@��������ɂ͌_����z���܂݂܂��B");
                }else {
                    this.message.setText("��������ɂ͑S�̊����͊܂܂�Ă���܂���B�@�����q���́A�Z�p�q���{�X�̂̂݋q�ƂȂ�܂��B");
                }
                this.message2.setText("<html> ��2�s�ڂ̐��l�́y�\�������z�́u��N�Δ�v�u�ڕW�Δ�v�ɂ��o�͂���܂��B <br>�@     �܂��A�y�\�����e�z���u���ђl�v�ɂ���Ǝ����l�ŕ\�����A�u�䗦�v�ɂ���ƍ�N�䗦���\������܂��B<br> �@     ��N�y�іڕW�ɑ΂��đΏۊ��Ԃ̏W�v�������Ă���ꍇ�͐����ŕ\������܂��B </html>");
            }

            //�{�p�S���^�u
            if (ranking.getSelectedComponent().equals(techStaffScroll)) {
                combobox.addItem("�{�p�q��");
                combobox.addItem("�{�p����");
                combobox.addItem("�{�p�w���q��");
                combobox.addItem("�{�p�w������");
                combobox.addItem("�A�v���[�`�q��");
                combobox.addItem("�A�v���[�`����");
                combobox.addItem("���i����");
                combobox.addItem("�S������");
                combobox.addItem("�q�P��");
                combobox.setSelectedIndex(7);
                if (this.flagCourse) {
                    this.message.setText("���S������ɂ͌_����z���܂݂܂��B");
                }else {
                    this.message.setText("");
                }
            }

            //�X�܃^�u
            if (ranking.getSelectedComponent().equals(shopScroll)) {
                combobox.addItem("���q��");
                combobox.addItem("�Z�p�q��");
                combobox.addItem("�Z�p�V�K�q��");
                combobox.addItem("�Z�p����");
                combobox.addItem("�Z�p�q�P��");
                combobox.addItem("���i����");
                combobox.addItem("������");
                combobox.addItem("�q�P��");
                combobox.setSelectedIndex(6);
                if (flagCourse) {
                    this.message.setText("���e����́A���ׂĊ�����̋��z�ƂȂ�܂��B   ���X�^�b�t�̓X�^�b�t1�l�����蔄��ł��B�@��������ɂ͌_����z���܂݂܂��B");
                }else {
                    this.message.setText("���e����́A���ׂĊ�����̋��z�ƂȂ�܂��B   ���X�^�b�t�̓X�^�b�t1�l�����蔄��ł��B");
                }
                this.message2.setText("<html> ��2�s�ڂ̐��l�́y�\�������z�́u��N�Δ�v�u�ڕW�Δ�v�ɂ��o�͂���܂��B <br>�@     �܂��A�y�\�����e�z���u���ђl�v�ɂ���Ǝ����l�ŕ\�����A�u�䗦�v�ɂ���ƍ�N�䗦���\������܂��B<br> �@     ��N�y�іڕW�ɑ΂��đΏۊ��Ԃ̏W�v�������Ă���ꍇ�͐����ŕ\������܂��B </html>");
            }

            //�Z�p�E���i�^�u
            // if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll) || ranking.getSelectedComponent().equals(courseScroll)) {
            if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll)) {
                combobox.addItem("����");
                combobox.addItem("���㍇�v");
                combobox.setSelectedIndex(0);
                this.message.setText("�����㍇�v�́A���׊�����̋��z�ƂȂ�܂��B");
            }

            //�ڋq�^�u
            if (ranking.getSelectedComponent().equals(decileScroll)) {
                String msg = "<html>";
                msg += "��������z�ɂ́A�������܂܂�Ă��܂��B<br>";
                msg += "�������͊܂݂܂���B";
                msg += "</html>";
                this.message.setText(msg);
                this.productDivisionMessage.setVisible(true);
                decileTotalScroll.setVisible(true);
            }
            
            //�ڋq�^�u
//            if (ranking.getSelectedComponent().equals(individualsalesScroll)) {
//                combobox.addItem("�Z�p�e��");
//                combobox.addItem("�Z�p�q��");
//                combobox.addItem("�w���e��");
//                combobox.addItem("�w����");
//                combobox.addItem("�X�̑e��");
//                combobox.addItem("�X�̐�");
//                combobox.addItem("�Z�p�ڰь��v�z");
//                combobox.addItem("�Z�p�ڰѐ�");
//                combobox.addItem("���i�ԕi���v�z");
//                combobox.addItem("���i�ԕi��");
//                combobox.addItem("�R�[�X�e��");
//                combobox.addItem("�R�[�X��");
//                combobox.addItem("�R�[�X�����e��");
//                combobox.addItem("�R�[�X������");
//                combobox.setSelectedIndex(0);
//                this.message.setText("");
//            }
	}
        
	/**
	 * ���o�����̊����E�񊈐�����
	 */
	private void initComponentsEnabled()
	{
            targetLabel.setEnabled(true);
            target.setEnabled(true);
            staffLabel.setEnabled(true);
            staff.setEnabled(true);
            technicClassLabel.setEnabled(true);
            technicClass.setEnabled(true);
            productDivisionLabel.setEnabled(true);
            productDivision.setEnabled(true);
            rangeDisplayLabel.setEnabled(true);
            rangeDisplay.setEnabled(true);
            orderDisplayLabel.setEnabled(true);
            orderDisplay.setEnabled(true);

            //�\�����p�R���{�{�b�N�X�̐ݒ�

            //��S���^�u�E�{�p�S���^�u
            if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(techStaffScroll)) {
                staffLabel.setEnabled(false);
                staff.setEnabled(false);
                technicClassLabel.setEnabled(false);
                technicClass.setEnabled(false);
                productDivisionLabel.setEnabled(false);
                productDivision.setEnabled(false);
                btnOutput.setEnabled(true);
            }

            //�X�܃^�u
            if (ranking.getSelectedComponent().equals(shopScroll)) {
                targetLabel.setEnabled(false);
                //IVS_LVTu start delete 2015/04/14 Bug #36254
                //target.setEnabled(false);
                //IVS_LVTu end delete 2015/04/14 Bug #36254
                staffLabel.setEnabled(false);
                staff.setEnabled(false);
                technicClassLabel.setEnabled(false);
                technicClass.setEnabled(false);
                productDivisionLabel.setEnabled(false);
                productDivision.setEnabled(false);
                btnOutput.setEnabled(true);
            }

            //�Z�p�E���i�^�u
            // if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll) || ranking.getSelectedComponent().equals(courseScroll)) {
            if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll)) {
                productDivisionLabel.setEnabled(false);
                productDivision.setEnabled(false);
                rangeDisplayLabel.setEnabled(false);
                rangeDisplay.setEnabled(false);
                btnOutput.setEnabled(true);
            }

            //�ڋq�^�u
            if (ranking.getSelectedComponent().equals(decileScroll)) {
                technicClassLabel.setEnabled(false);
                technicClass.setEnabled(false);
                rangeDisplayLabel.setEnabled(false);
                rangeDisplay.setEnabled(false);
                orderDisplayLabel.setEnabled(false);
                orderDisplay.setEnabled(false);
                btnOutput.setEnabled(true);
            }
            
            //�l����
//            if (ranking.getSelectedComponent().equals(individualsalesScroll)) {                
//                targetLabel.setEnabled(false);
//                target.setEnabled(false);
//                staffLabel.setEnabled(false);
//                staff.setEnabled(true);
//                technicClassLabel.setEnabled(false);
//                technicClass.setEnabled(false);
//                productDivisionLabel.setEnabled(false);
//                productDivision.setEnabled(false);
//                btnOutput.setEnabled(true);
//            }

	}
        
	/**
	 * �Z�p���ށE���i���ނ̃Z�b�g
	 */
	private void initTechnicClasses()
	{
    
            technicClass.removeAllItems();
            technicClass.addItem("�S��");

            // �Z�p�^�u
            if (ranking.getSelectedComponent().equals(techScroll)) {
                MstTechnicClasses mtcs = new MstTechnicClasses();

                try {

                    ConnectionWrapper con = SystemInfo.getConnection();
                    mtcs.load(con);

                } catch(Exception e) {

                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }


                for (MstTechnicClass mtc : mtcs) {
                    technicClass.addItem(mtc);
                }
            }

            // ���i�^�u
            if (ranking.getSelectedComponent().equals(itemScroll)) {
                MstItemClasses mgcs = new MstItemClasses();

                try {

                    ConnectionWrapper con = SystemInfo.getConnection();
                    mgcs.load(con);

                } catch(SQLException e) {

                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }

                for (MstItemClass mgc : mgcs) {
                    technicClass.addItem(mgc);
                }
            }
            
            // course�^�u
//            if (ranking.getSelectedComponent().equals(courseScroll)) {
//                MstCourseClasses mgcs = new MstCourseClasses();
//
//                try {
//
//                    ConnectionWrapper con = SystemInfo.getConnection();
//                    mgcs.load(con);
//
//                } catch(SQLException e) {
//
//                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
//                }
//
//                for (MstCourseClass mgc : mgcs) {
//                    technicClass.addItem(mgc);
//                }
//            }
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taxGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblTax1 = new javax.swing.JLabel();
        rdoMutiShop = new javax.swing.JRadioButton();
        rdoAllShop = new javax.swing.JRadioButton();
        lblTax2 = new javax.swing.JLabel();
        cmbReappearanceSpan = new javax.swing.JComboBox();
        lblTax3 = new javax.swing.JLabel();
        rdoLastYear = new javax.swing.JRadioButton();
        rdoTaxBlank2 = new javax.swing.JRadioButton();
        lblTax4 = new javax.swing.JLabel();
        rdoValue = new javax.swing.JRadioButton();
        rdoTaxBlank3 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        lblCategory = new javax.swing.JLabel();
        useShopCategoryScrollPane = new javax.swing.JScrollPane();
        useShopCategoryTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        targetLabel = new javax.swing.JLabel();
        target = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        staffLabel = new javax.swing.JLabel();
        staff = new javax.swing.JComboBox();
        lblTargetPeriod = new javax.swing.JLabel();
        cmbTargetPeriodStartDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        lblTax = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        orderDisplayLabel = new javax.swing.JLabel();
        orderDisplay = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        rangeDisplay = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        rangeDisplayLabel = new javax.swing.JLabel();
        productDivision = new javax.swing.JComboBox();
        productDivisionLabel = new javax.swing.JLabel();
        technicClass = new javax.swing.JComboBox();
        technicClassLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbTargetPeriodEndDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        productDivisionMessage = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnPurposeSetting = new javax.swing.JButton();
        showButton = new javax.swing.JButton();
        btnOutput = new javax.swing.JButton();
        ranking = new javax.swing.JTabbedPane();
        mainStaffScroll = new javax.swing.JScrollPane();
        mainStaff = new MultiSpanCellTableEx();
        techStaffScroll = new javax.swing.JScrollPane();
        techStaff = new com.geobeck.swing.JTableEx();
        shopScroll = new javax.swing.JScrollPane();
        shop = new com.geobeck.swing.JTableEx();
        techScroll = new javax.swing.JScrollPane();
        tech = new com.geobeck.swing.JTableEx();
        itemScroll = new javax.swing.JScrollPane();
        item = new com.geobeck.swing.JTableEx();
        courseScroll = new javax.swing.JScrollPane();
        course = new com.geobeck.swing.JTableEx();
        decileScroll = new javax.swing.JScrollPane();
        decile = new com.geobeck.swing.JTableEx();
        individualsalesScroll = new javax.swing.JScrollPane();
        individualsales = new com.geobeck.swing.JTableEx();
        jPanel1 = new javax.swing.JPanel();
        message = new javax.swing.JLabel();
        decileTotalScroll = new javax.swing.JScrollPane();
        decileTotal = new javax.swing.JTable();
        message2 = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        pnlMain.setFocusCycleRoot(true);
        pnlMain.setOpaque(false);

        jPanel2.setOpaque(false);

        lblTax1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax1.setText("�V�K�敪");
        lblTax1.setFocusCycleRoot(true);

        buttonGroup1.add(rdoMutiShop);
        rdoMutiShop.setSelected(true);
        rdoMutiShop.setText("���X�V�K");
        rdoMutiShop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoMutiShop.setFocusCycleRoot(true);
        rdoMutiShop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoMutiShop.setOpaque(false);

        buttonGroup1.add(rdoAllShop);
        rdoAllShop.setText("�S�X�V�K");
        rdoAllShop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoAllShop.setFocusCycleRoot(true);
        rdoAllShop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoAllShop.setOpaque(false);
        rdoAllShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllShopActionPerformed(evt);
            }
        });

        lblTax2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax2.setText("�ė��Z�o����");
        lblTax2.setFocusCycleRoot(true);

        cmbReappearanceSpan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1����", "45��", "2����", "3����", "4����", "5����", "6����" }));
        cmbReappearanceSpan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbReappearanceSpan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbReappearanceSpanItemStateChanged(evt);
            }
        });
        cmbReappearanceSpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbReappearanceSpanActionPerformed(evt);
            }
        });

        lblTax3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax3.setText("�\������");
        lblTax3.setFocusCycleRoot(true);

        buttonGroup2.add(rdoLastYear);
        rdoLastYear.setSelected(true);
        rdoLastYear.setText("��N�Δ�");
        rdoLastYear.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoLastYear.setFocusCycleRoot(true);
        rdoLastYear.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoLastYear.setOpaque(false);

        buttonGroup2.add(rdoTaxBlank2);
        rdoTaxBlank2.setText("�ڕW�Δ�");
        rdoTaxBlank2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank2.setFocusCycleRoot(true);
        rdoTaxBlank2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank2.setOpaque(false);
        rdoTaxBlank2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTaxBlank2ActionPerformed(evt);
            }
        });

        lblTax4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax4.setText("�\�����e");
        lblTax4.setFocusCycleRoot(true);

        buttonGroup3.add(rdoValue);
        rdoValue.setSelected(true);
        rdoValue.setText("�����l");
        rdoValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoValue.setFocusCycleRoot(true);
        rdoValue.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoValue.setOpaque(false);

        buttonGroup3.add(rdoTaxBlank3);
        rdoTaxBlank3.setText("�䗦");
        rdoTaxBlank3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank3.setFocusCycleRoot(true);
        rdoTaxBlank3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank3.setOpaque(false);
        rdoTaxBlank3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTaxBlank3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(lblTax2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cmbReappearanceSpan, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(lblTax1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rdoMutiShop)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rdoAllShop)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(lblTax3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rdoLastYear))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(lblTax4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rdoValue)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rdoTaxBlank3)
                    .add(rdoTaxBlank2))
                .add(46, 46, 46))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(2, 2, 2)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(rdoMutiShop)
                        .add(rdoAllShop))
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblTax1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblTax3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(rdoLastYear)
                        .add(rdoTaxBlank2)))
                .add(12, 12, 12)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblTax2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(cmbReappearanceSpan, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblTax4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(rdoValue)
                        .add(rdoTaxBlank3)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setOpaque(false);

        lblCategory.setText("�W�v�Ƒ�");

        useShopCategoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "�ƑԖ�", "�I��", "�Ƒ�ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        useShopCategoryTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        useShopCategoryTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(useShopCategoryTable, SystemInfo.getTableHeaderRenderer());
        useShopCategoryTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(useShopCategoryTable);
        useShopCategoryScrollPane.setViewportView(useShopCategoryTable);
        if (useShopCategoryTable.getColumnModel().getColumnCount() > 0) {
            useShopCategoryTable.getColumnModel().getColumn(0).setMinWidth(150);
            useShopCategoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            useShopCategoryTable.getColumnModel().getColumn(0).setMaxWidth(150);
            useShopCategoryTable.getColumnModel().getColumn(2).setMinWidth(0);
            useShopCategoryTable.getColumnModel().getColumn(2).setPreferredWidth(0);
            useShopCategoryTable.getColumnModel().getColumn(2).setMaxWidth(0);
        }

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(lblCategory)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(useShopCategoryScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblCategory)
                    .add(useShopCategoryScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setOpaque(false);

        targetLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel.setText("�ΏۓX��");
        targetLabel.setFocusCycleRoot(true);

        target.setMaximumSize(new java.awt.Dimension(150, 32767));
        target.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetActionPerformed(evt);
            }
        });

        staffLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        staffLabel.setText("��S����");
        staffLabel.setFocusCycleRoot(true);

        staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        staff.setMaximumSize(new java.awt.Dimension(150, 32767));
        staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffActionPerformed(evt);
            }
        });

        lblTargetPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTargetPeriod.setText("�Ώۊ���");
        lblTargetPeriod.setFocusCycleRoot(true);

        cmbTargetPeriodStartDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStartDate.setFocusCycleRoot(true);
        cmbTargetPeriodStartDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartDateFocusLost(evt);
            }
        });

        lblTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax.setText("�ŋ敪");
        lblTax.setFocusCycleRoot(true);

        taxGroup.add(rdoTaxUnit);
        rdoTaxUnit.setText("�ō�");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setFocusCycleRoot(true);
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);

        taxGroup.add(rdoTaxBlank);
        rdoTaxBlank.setText("�Ŕ�");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setFocusCycleRoot(true);
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);
        rdoTaxBlank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTaxBlankActionPerformed(evt);
            }
        });

        orderDisplayLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        orderDisplayLabel.setText("�\����");
        orderDisplayLabel.setFocusCycleRoot(true);

        orderDisplay.setFocusCycleRoot(true);

        rangeDisplay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "�`10��", "�`20��", "�S��" }));
        rangeDisplay.setFocusCycleRoot(true);
        rangeDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rangeDisplayActionPerformed(evt);
            }
        });

        rangeDisplayLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rangeDisplayLabel.setText("�\���͈�");
        rangeDisplayLabel.setFocusCycleRoot(true);

        productDivision.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "�S��", "�Z�p", "���i" }));
        productDivision.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        productDivision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productDivisionActionPerformed(evt);
            }
        });

        productDivisionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        productDivisionLabel.setText("�]���Ώ�");
        productDivisionLabel.setFocusCycleRoot(true);

        technicClass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        technicClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                technicClassActionPerformed(evt);
            }
        });

        technicClassLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        technicClassLabel.setText("����");
        technicClassLabel.setFocusCycleRoot(true);

        jLabel1.setText("�`");
        jLabel1.setFocusCycleRoot(true);

        cmbTargetPeriodEndDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEndDate.setFocusCycleRoot(true);
        cmbTargetPeriodEndDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndDateFocusLost(evt);
            }
        });

        productDivisionMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productDivisionMessage.setText("���Z�p�A���i�̏ꍇ�A�S�̊����͊܂݂܂���B");
        productDivisionMessage.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblTax, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTargetPeriod, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, targetLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, staffLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(10, 10, 10)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(75, 75, 75)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(productDivisionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10)
                                .add(productDivision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(technicClassLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(technicClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(productDivisionMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(cmbTargetPeriodStartDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmbTargetPeriodEndDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(orderDisplayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(rangeDisplayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(rdoTaxUnit)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rdoTaxBlank)
                                .add(217, 217, 217)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(orderDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(rangeDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(73, 73, 73))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(productDivision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(productDivisionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(targetLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(staffLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(staff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(technicClassLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(technicClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(productDivisionMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTargetPeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(cmbTargetPeriodStartDate, 0, 21, Short.MAX_VALUE)
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(cmbTargetPeriodEndDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(rangeDisplayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(orderDisplayLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(rdoTaxUnit)
                            .add(rdoTaxBlank)
                            .add(lblTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(rangeDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(orderDisplay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(2, 2, 2))))
        );

        jPanel4Layout.linkSize(new java.awt.Component[] {cmbTargetPeriodEndDate, cmbTargetPeriodStartDate}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel5.setOpaque(false);

        btnPurposeSetting.setIcon(SystemInfo.getImageIcon("/button/master/target_off.jpg"));
        btnPurposeSetting.setBorderPainted(false);
        btnPurposeSetting.setMaximumSize(new java.awt.Dimension(30, 9));
        btnPurposeSetting.setMinimumSize(new java.awt.Dimension(30, 9));
        btnPurposeSetting.setPreferredSize(new java.awt.Dimension(30, 9));
        btnPurposeSetting.setPressedIcon(SystemInfo.getImageIcon("/button/master/target_on.jpg"));
        btnPurposeSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurposeSettingActionPerformed(evt);
            }
        });

        showButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        showButton.setBorderPainted(false);
        showButton.setContentAreaFilled(false);
        showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        showButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showButtonActionPerformed(evt);
            }
        });

        btnOutput.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput.setBorderPainted(false);
        btnOutput.setContentAreaFilled(false);
        btnOutput.setFocusCycleRoot(true);
        btnOutput.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(showButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 24, Short.MAX_VALUE)
                .add(btnOutput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(jPanel5Layout.createSequentialGroup()
                .add(btnPurposeSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(btnPurposeSetting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, showButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnOutput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout pnlMainLayout = new org.jdesktop.layout.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMainLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 544, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 255, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(52, 52, 52))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
            .add(pnlMainLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(pnlMainLayout.createSequentialGroup()
                .add(26, 26, 26)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ranking.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        ranking.setPreferredSize(new java.awt.Dimension(457, 429));
        ranking.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rankingStateChanged(evt);
            }
        });

        mainStaffScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mainStaffScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        mainStaff.setUI(new MultiSpanCellTableUI());
        mainStaff.setModel(new AttributiveCellTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "����", "���O", "�X��", "���q��", "<html>�Z�p<br>�q��</html>", "�Z�p����", "<html>�w��<br>�q��</html>", "�w������", "<html>�V�K<br>�q��</html>", "<html>�Љ�<br>�q��</html>", "�_����z", "�������z", "���i����", "������", "���q�P��", "<html>&nbsp;�Z�p<br>�q�P��</html>", "�ė���"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class,java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mainStaff.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        mainStaff.setSelectionBackground(new java.awt.Color(220, 220, 220));
        mainStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
        mainStaff.getTableHeader().setReorderingAllowed(false);
        mainStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtil.setJTableHeaderRenderer(mainStaff, SystemInfo.getTableHeaderRenderer());
        mainStaff.setRowHeight(40);
        SelectTableCellRenderer.setSelectTableCellRenderer(mainStaff);
        mainStaffScroll.setViewportView(mainStaff);
        mainStaff.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        TableColumnModel modelColumnMain = mainStaff.getColumnModel();
        modelColumnMain.getColumn(3).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(4).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(5).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(6).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(7).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(8).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(9).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(10).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(11).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(12).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(13).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(14).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(15).setCellRenderer(new MinusCellBlueRenderer());
        modelColumnMain.getColumn(16).setCellRenderer(new MinusCellBlueRenderer());

        ranking.addTab("    ��S��    ", mainStaffScroll);

        techStaffScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        techStaffScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        techStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "����", "���O", "�X��", "<html>&nbsp;&nbsp;&nbsp;��<br>�S����</html>", "<html>�{�p<br>�q��</html>", "<html>�{�p<br>����</html>", "<html>&nbsp;&nbsp;�{�p<br>�w���q��</html>", "<html>&nbsp;&nbsp;�{�p<br>�w������</html>", "<html>&nbsp;AP<br>�q��</html>", "<html>&nbsp;AP<br>����</html>", "<html>�_��<br>���z</html>", "<html>����<br>���z</html>", "<html>���i<br>����</html>", "<html>�S��<br>����</html>", "�q�P��", "<html>&nbsp;&nbsp;�{�p<br>�q�P��</html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        techStaff.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        techStaff.setSelectionBackground(new java.awt.Color(220, 220, 220));
        techStaff.setSelectionForeground(new java.awt.Color(0, 0, 0));
        techStaff.getTableHeader().setReorderingAllowed(false);
        techStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SwingUtil.setJTableHeaderRenderer(techStaff, SystemInfo.getTableHeaderRenderer());
        techStaff.setRowHeight(40);
        SelectTableCellRenderer.setSelectTableCellRenderer(techStaff);
        techStaffScroll.setViewportView(techStaff);

        ranking.addTab("   �{�p�S��   ", techStaffScroll);

        shopScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shopScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        shop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "����", "<html>�X��<br>�R�[�h</html>", "�X�ܖ�", "���q��", "<html>�Z�p<br>�q��</html>", "<html>&nbsp;&nbsp;�Z�p<br>�V�K�q��</html>", "<html>&nbsp;&nbsp;�Z�p<br>�V�K�䗦</html>", "<html>�Z�p<br>����</html>", "<html>&nbsp;&nbsp;�M�t<br>�g���̔�</html>", "<html>&nbsp;&nbsp;�M�t<br>�g�����p</html>", "<html>&nbsp;&nbsp;��<br>���̔�</html>", "<html>&nbsp;&nbsp;��<br>�����p</html>", "<html>&nbsp;&nbsp;�Z�p<br>�q�P��</html>", "���i����", "������", "�X�^�b�t��", "1�l����", "�q�P��"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        shop.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        shop.setSelectionBackground(new java.awt.Color(220, 220, 220));
        shop.setSelectionForeground(new java.awt.Color(0, 0, 0));
        shop.getTableHeader().setReorderingAllowed(false);
        shop.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shop.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(shop, SystemInfo.getTableHeaderRenderer());
        shop.setRowHeight(20);
        shopScroll.setViewportView(shop);
        shop.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        ranking.addTab("     �X��     ", shopScroll);

        techScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tech.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "����", "���ޖ�", "�Z�p��", "�P ��", "�� ��", "���㍇�v", "�������z", "<html>&nbsp;&nbsp;&nbsp;�䗦<br>�i �� �� �j</html>", "<html>&nbsp;&nbsp;&nbsp;�䗦<br>�i �� �� �j</html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tech.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tech.setSelectionBackground(new java.awt.Color(220, 220, 220));
        tech.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tech.getTableHeader().setReorderingAllowed(false);
        tech.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tech.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(tech, SystemInfo.getTableHeaderRenderer());
        tech.setRowHeight(40);
        techScroll.setViewportView(tech);

        ranking.addTab("     �Z�p     ", techScroll);

        itemScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        item.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "����", "���ޖ�", "���i��", "�P ��", "�� ��", "���㍇�v", "�������z", "<html>&nbsp;&nbsp;&nbsp;�䗦<br>�i �� �� �j</html>", "<html>&nbsp;&nbsp;&nbsp;�䗦<br>�i �� �� �j</html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        item.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        item.setSelectionBackground(new java.awt.Color(220, 220, 220));
        item.setSelectionForeground(new java.awt.Color(0, 0, 0));
        item.getTableHeader().setReorderingAllowed(false);
        item.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        item.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(item, SystemInfo.getTableHeaderRenderer());
        item.setRowHeight(40);
        itemScroll.setViewportView(item);
        item.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        ranking.addTab("     ���i     ", itemScroll);

        courseScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        course.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "����", "���ޖ�", "�R�[�X��", "�P ��", "�� ��", "�_����z�v", "�������z", "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�䗦<br>�i �� �� �j</html>", "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�䗦<br>�i �� �� �j</html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        course.getTableHeader().setReorderingAllowed(false);
        course.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        course.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(course, SystemInfo.getTableHeaderRenderer());
        course.setRowHeight(40);
        courseScroll.setViewportView(course);
        course.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        ranking.addTab("�R�[�X�_��", courseScroll);

        decileScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        decile.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "�����N", "�ڋq��", "���X��", "������z", "����V�F�A��", "<html>�݌v����<br>&nbsp;�V�F�A��</html>", "���ϔ�����z", "���ϗ��X��", "�q�P��", "�ꗗ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        decile.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        decile.setSelectionBackground(new java.awt.Color(220, 220, 220));
        decile.setSelectionForeground(new java.awt.Color(0, 0, 0));
        decile.getTableHeader().setReorderingAllowed(false);
        decile.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        decile.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(decile, SystemInfo.getTableHeaderRenderer());
        decile.setRowHeight(40);
        decileScroll.setViewportView(decile);
        decile.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        ranking.addTab("   �ڋq(�f�V��)   ", decileScroll);

        individualsalesScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        individualsalesScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        individualsalesScroll.setAutoscrolls(true);
        individualsalesScroll.setPreferredSize(new java.awt.Dimension(500, 450));

        individualsales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "����", "�X�^�b�t��", "���v���z", "�Z�p�e��", "<html>&nbsp;&nbsp;�Z�p<br>&nbsp;&nbsp;�q��</html>", "�w���e��", "�w����", "�X�̑e��", "�X�̐�", "<html>&nbsp;&nbsp;�Z�p�ڰ�<br>&nbsp;&nbsp;&nbsp;&nbsp;���v�z</html>", "<html>&nbsp;&nbsp;�Z�p<br>&nbsp;�ڰѐ�</html>", "<html>&nbsp;&nbsp;���i�ԕi<br>&nbsp;&nbsp;&nbsp;&nbsp;���v�z</html>", "<html>&nbsp;&nbsp;���i<br>�ԕi��</html>", "�R�[�X�e��", "<html>�R�[�X<br>&nbsp;&nbsp;&nbsp;&nbsp;��</html>", "<html>&nbsp;�R�[�X<br>�����e��</html>", "<html>�R�[�X<br>������</html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        individualsales.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        individualsales.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 450));
        individualsales.setSelectionBackground(new java.awt.Color(220, 220, 220));
        individualsales.setSelectionForeground(new java.awt.Color(0, 0, 0));
        individualsales.getTableHeader().setReorderingAllowed(false);
        individualsales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        individualsales.setDefaultRenderer(Object.class, new RankingTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(individualsales, SystemInfo.getTableHeaderRenderer());
        individualsales.setRowHeight(40);
        individualsalesScroll.setViewportView(individualsales);
        individualsales.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        /*

        ranking.addTab("�l����", individualsalesScroll);
        */

        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        message.setFont(new java.awt.Font("MS UI Gothic", 0, 13)); // NOI18N
        message.setText("<html> XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br> XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX </html> ");
        message.setFocusCycleRoot(true);
        jPanel1.add(message);
        message.setBounds(0, 0, 810, 30);

        decileTotalScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        decileTotalScroll.setFocusable(false);

        decileTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "�ڋq��", "���X��", "������z", "���ϔ�����z", "���ϗ��X��", "�q�P��"
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
        decileTotal.setFocusable(false);
        decileTotal.setRequestFocusEnabled(false);
        decileTotal.setRowSelectionAllowed(false);
        decileTotal.getTableHeader().setReorderingAllowed(false);
        decileTotal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        decileTotal.setDefaultRenderer(Object.class, new CustomerTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(decileTotal, SystemInfo.getTableHeaderRenderer());
        decileTotal.setRowHeight(22);
        decileTotalScroll.setViewportView(decileTotal);

        jPanel1.add(decileTotalScroll);
        decileTotalScroll.setBounds(290, 0, 510, 41);

        message2.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        message2.setForeground(new java.awt.Color(0, 0, 225));
        message2.setText("<html> XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX<br> XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX </html> ");
        jPanel1.add(message2);
        message2.setBounds(0, 20, 810, 60);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 814, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, ranking, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(pnlMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5)
                .add(ranking, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 389, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
        );

        ranking.getAccessibleContext().setAccessibleName("�@�@�X�^�b�t�@�@");
    }// </editor-fold>//GEN-END:initComponents

    private void btnOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutputActionPerformed

        //��S���^�u
        if (ranking.getSelectedComponent().equals(mainStaffScroll)) {
            if (!SystemInfo.checkAuthorityPassword(271)) return;
        }

        //�{�p�S���^�u
        if (ranking.getSelectedComponent().equals(techStaffScroll)) {
            if (!SystemInfo.checkAuthorityPassword(272)) return;
        }

        //�X�܃^�u
        if (ranking.getSelectedComponent().equals(shopScroll)) {
            if (!SystemInfo.checkAuthorityPassword(273)) return;
        }

        //�Z�p�^�u
        if (ranking.getSelectedComponent().equals(techScroll)) {
            if (!SystemInfo.checkAuthorityPassword(274)) return;
        }

        //���i�^�u
        if (ranking.getSelectedComponent().equals(itemScroll)) {
            if (!SystemInfo.checkAuthorityPassword(275)) return;
        }

        //�ڋq�^�u
        if (ranking.getSelectedComponent().equals(decileScroll)) {
            if (!SystemInfo.checkAuthorityPassword(276)) return;
        }
//        //�l����
//        if (ranking.getSelectedComponent().equals(individualsalesScroll)) {
//            if (!SystemInfo.checkAuthorityPassword(277)) return;
//        }
        
        btnOutput.setCursor(null);
        reportOutPut();
    }//GEN-LAST:event_btnOutputActionPerformed
    
    private void targetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetActionPerformed
        this.initStaff();
        //IVS_LVTu start edit 2015/08/26 Bug #42247
        Boolean flg = false;
        if (target.getSelectedItem() instanceof MstGroup) {
            //�O���[�v
            MstGroup mg = (MstGroup) target.getSelectedItem();
            if(mg.getShops().size()> 0) {
                flg = true;
            }
            flg = checkShop((MstGroup) target.getSelectedItem());
            
        }
        if(target.getSelectedItem() instanceof MstShop) {
            flg = true;
        }
        showButton.setEnabled(flg);
        btnOutput.setEnabled(flg);
        
        //check course flag
        flagCourse = checkCourseFlag();
        initOrderDisplayComponents(orderDisplay);
        initTableRankingMain();
        initTableShopRankingOld();
        if (this.stl != null && ranking.getSelectedComponent().equals(techStaffScroll)) {
            showTechStaffRankingData();
        }
        if (!flagCourse) {
            if(ranking.getSelectedComponent().equals(courseScroll)) {
                ranking.setSelectedIndex(0);
            }
            ranking.remove(courseScroll);
        }else {
            //IVS_LVTu start edit 2016/02/24 New request #48455
            //ranking.remove(decileScroll);
            ranking.addTab("�R�[�X�_��",courseScroll);
            if (!SystemInfo.getSystemType().equals(2)) {
                ranking.remove(decileScroll);
                ranking.addTab("   �ڋq(�f�V��)   ", decileScroll);
            }
            //IVS_LVTu end edit 2016/02/24 New request #48455
        }

    }//GEN-LAST:event_targetActionPerformed

    private boolean checkShop(MstGroup mg) {
        if (target.getSelectedItem() instanceof MstGroup) {
            //�O���[�v
            if(mg.getShops().size()> 0) {
                return  true;
            } else if ( mg.getGroups().size() > 0) {
                for ( int i = 0;i < mg.getGroups().size() ;i ++) {
                    return checkShop(mg.getGroups().get(i));
                }
            }
        }
        return false;
    }
    //IVS_LVTu end edit 2015/08/26 Bug #42247
    private void cmbTargetPeriodStartDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDateFocusGained
        cmbTargetPeriodStartDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartDateFocusGained

    private void cmbTargetPeriodEndDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDateFocusGained
        cmbTargetPeriodEndDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndDateFocusGained

    private void technicClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_technicClassActionPerformed
    }//GEN-LAST:event_technicClassActionPerformed

    private void rankingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rankingStateChanged

        //�\�����p�R���{�{�b�N�X�̐ݒ�
        initOrderDisplayComponents(orderDisplay);
        
        //���o�����̊����E�񊈐�����
        initComponentsEnabled();
        
        //�Z�p���ށE���i���ނ̃Z�b�g
        initTechnicClasses();

        //����������
        this.init();
    }//GEN-LAST:event_rankingStateChanged

    private void showButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showButtonActionPerformed
        //����������
        isShowButtonClicked = true;
        showButton.setCursor(null);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(this.cmbTargetPeriodStartDate.getDate());
        end.setTime(this.cmbTargetPeriodEndDate.getDate());
        
        if(start.compareTo(end) != 0) {
            //�Ώۊ��Ԃ��J�n�����I�����Ȃ�G���[
            if(start.after(end)) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_WRONG, "�Ώۊ���"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
//        if(!rdoTaxBlank2.isEnabled()){
//            rdoLastYear.setSelected(true);
//        }
        this.init();
    }//GEN-LAST:event_showButtonActionPerformed

    private void productDivisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productDivisionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productDivisionActionPerformed

    private void rangeDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rangeDisplayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rangeDisplayActionPerformed

    private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffActionPerformed

    private void rdoTaxBlankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTaxBlankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoTaxBlankActionPerformed

    private void rdoAllShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllShopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoAllShopActionPerformed

    private void cmbReappearanceSpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbReappearanceSpanActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbReappearanceSpanActionPerformed

    private void rdoTaxBlank2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTaxBlank2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoTaxBlank2ActionPerformed

    private void rdoTaxBlank3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTaxBlank3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoTaxBlank3ActionPerformed

    private void cmbReappearanceSpanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbReappearanceSpanItemStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbReappearanceSpanItemStateChanged

    private void cmbTargetPeriodStartDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodStartDateFocusLost
        // TODO add your handling code here:
//        if(cmbTargetPeriodStartDate.getDate().getYear() == cmbTargetPeriodEndDate.getDate().getYear() &&
//                cmbTargetPeriodStartDate.getDate().getMonth() == cmbTargetPeriodEndDate.getDate().getMonth()){
//            rdoTaxBlank2.setEnabled(true);
//            
//        }else{
//            rdoTaxBlank2.setEnabled(false);
//        }
    }//GEN-LAST:event_cmbTargetPeriodStartDateFocusLost

    private void cmbTargetPeriodEndDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTargetPeriodEndDateFocusLost
        // TODO add your handling code here:
//         if(cmbTargetPeriodStartDate.getDate().getYear() == cmbTargetPeriodEndDate.getDate().getYear() &&
//                cmbTargetPeriodStartDate.getDate().getMonth() == cmbTargetPeriodEndDate.getDate().getMonth()){
//            rdoTaxBlank2.setEnabled(true);
//            
//        }else{
//            rdoTaxBlank2.setEnabled(false);
//        }
    }//GEN-LAST:event_cmbTargetPeriodEndDateFocusLost

    private void btnPurposeSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurposeSettingActionPerformed

        TargetSettingPanel tsp = new TargetSettingPanel(this);
        parentFrame.changeContents(tsp);
    }//GEN-LAST:event_btnPurposeSettingActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutput;
    private javax.swing.JButton btnPurposeSetting;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox cmbReappearanceSpan;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEndDate;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStartDate;
    private com.geobeck.swing.JTableEx course;
    private javax.swing.JScrollPane courseScroll;
    private com.geobeck.swing.JTableEx decile;
    private javax.swing.JScrollPane decileScroll;
    private javax.swing.JTable decileTotal;
    private javax.swing.JScrollPane decileTotalScroll;
    private com.geobeck.swing.JTableEx individualsales;
    private javax.swing.JScrollPane individualsalesScroll;
    private com.geobeck.swing.JTableEx item;
    private javax.swing.JScrollPane itemScroll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblTargetPeriod;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTax1;
    private javax.swing.JLabel lblTax2;
    private javax.swing.JLabel lblTax3;
    private javax.swing.JLabel lblTax4;
    private com.geobeck.swing.JTableEx mainStaff;
    private javax.swing.JScrollPane mainStaffScroll;
    private javax.swing.JLabel message;
    private javax.swing.JLabel message2;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel orderDisplay;
    private javax.swing.JLabel orderDisplayLabel;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JComboBox productDivision;
    private javax.swing.JLabel productDivisionLabel;
    private javax.swing.JLabel productDivisionMessage;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel rangeDisplay;
    private javax.swing.JLabel rangeDisplayLabel;
    private javax.swing.JTabbedPane ranking;
    private javax.swing.JRadioButton rdoAllShop;
    private javax.swing.JRadioButton rdoLastYear;
    private javax.swing.JRadioButton rdoMutiShop;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxBlank2;
    private javax.swing.JRadioButton rdoTaxBlank3;
    private javax.swing.JRadioButton rdoTaxUnit;
    private javax.swing.JRadioButton rdoValue;
    private com.geobeck.swing.JTableEx shop;
    private javax.swing.JScrollPane shopScroll;
    private javax.swing.JButton showButton;
    private javax.swing.JComboBox staff;
    private javax.swing.JLabel staffLabel;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel target;
    private javax.swing.JLabel targetLabel;
    private javax.swing.ButtonGroup taxGroup;
    private com.geobeck.swing.JTableEx tech;
    private javax.swing.JScrollPane techScroll;
    private com.geobeck.swing.JTableEx techStaff;
    private javax.swing.JScrollPane techStaffScroll;
    private javax.swing.JComboBox technicClass;
    private javax.swing.JLabel technicClassLabel;
    private javax.swing.JScrollPane useShopCategoryScrollPane;
    private javax.swing.JTable useShopCategoryTable;
    // End of variables declaration//GEN-END:variables
	
	private	StaffShopRankingFocusTraversalPolicy	ftp	=
			new StaffShopRankingFocusTraversalPolicy();
	
	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(showButton);
		SystemInfo.addMouseCursorChange(btnOutput);
	}
	
	private void setKeyListener()
	{
		cmbTargetPeriodEndDate.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodEndDate.addFocusListener(SystemInfo.getSelectText());
		cmbTargetPeriodStartDate.addKeyListener(SystemInfo.getMoveNextField());
		cmbTargetPeriodStartDate.addFocusListener(SystemInfo.getSelectText());
		rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
		rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
		orderDisplay.addKeyListener(SystemInfo.getMoveNextField());
		orderDisplay.addFocusListener(SystemInfo.getSelectText());
		productDivision.addKeyListener(SystemInfo.getMoveNextField());
		productDivision.addFocusListener(SystemInfo.getSelectText());
		rangeDisplay.addKeyListener(SystemInfo.getMoveNextField());
		rangeDisplay.addFocusListener(SystemInfo.getSelectText());
	}
	
	/**
	 * �������������s���B
	 */
	public void init()
	{
            try {
                //nhanvt start add 20141201 New request #33406
                String lstShopCategoryId = "";
                lstShopCategoryName = "";
                ArrayList<Integer> arrList = new ArrayList<Integer>();
                ArrayList<String> arrListName = new ArrayList<String>();
                if(mscg.size() >0){
                      for(int i=0; i< useShopCategoryTable.getRowCount(); i++){              
                          Integer shopCategoryId = (Integer)useShopCategoryTable.getModel().getValueAt(i, 2);  
                          String shopCategoryName = (String)useShopCategoryTable.getModel().getValueAt(i, 0);
                          String check = useShopCategoryTable.getModel().getValueAt(i, 1).toString();
                          if(check.equals("true")){
                              arrList.add(shopCategoryId);   
                              arrListName.add(shopCategoryName);
                          }
                      }
                }

                for(int i=0; i< arrList.size(); i++){
                    if(i == (arrList.size()-1)){
                        lstShopCategoryId += arrList.get(i);
                        lstShopCategoryName += arrListName.get(i);
                    }else{
                        lstShopCategoryId += arrList.get(i);
                        lstShopCategoryName += arrListName.get(i);
                        lstShopCategoryId += ",";  
                        lstShopCategoryName += "�A";
                    }
                }
                //nhanvt end add 20141201 New request #33406
    
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                if(blnInitComp){
                    //�R���|�[�l���g�������ς݂̏ꍇ

                    //��S���^�u�E�{�p�S���^�u
                    if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(techStaffScroll)) {
                        //��S���^�u�܂��͎{�p�S���^�u���I������Ă���ꍇ
                        
                        stl = new StaffRankingList();
                        //nhanvt start add 20150309 New request #35223
                        changeTargetDate();
                        //nhanvt end add 20150309 New request #35223
                        //�I�𒆂̃^�u�C���f�b�N�X
                        if (ranking.getSelectedComponent().equals(mainStaffScroll)) {
                            stl.setSelectedTabIndex(0);
                        } else {
                            stl.setSelectedTabIndex(1);
                        }
                        //�X��ID���X�g
                        stl.setShopIDList(getShopIDList());
                        //�Ώۊ���
                        stl.setTermFrom(cmbTargetPeriodStartDate.getDate());
                        stl.setTermTo(cmbTargetPeriodEndDate.getDate());
                        //�ŋ敪
                        stl.setTaxType(getSelectedTaxType());
                        //�\���͈�
                        stl.setRangeDisplay(rangeDisplay.getSelectedIndex());
                        //�\����
                        stl.setOrderDisplay(orderDisplay.getSelectedIndex());
                        
                        //nhanvt start add 20141216 New request #33406
                        if(!lstShopCategoryId.equals("")){
                            stl.setListCategoryId(lstShopCategoryId);
                        }else{
                            stl.setListCategoryId(null);
                        }

                        stl.setHideCategory(isHideCategory);
                        stl.setUseShopCategory(useShopCategory);
                        //nhanvt end add 20141216 New request #33406
                        //IVS_TMTrong start edit 20150715 Bug #40595
                        if(rdoMutiShop.isSelected()){
                            stl.setIsMutiShop(true);
                        }else{
                            stl.setIsMutiShop(false);
                        }
                        //IVS_TMTrong end edit 20150715 Bug #40595
                        if(rdoLastYear.isSelected()){
                            stl.setIsLastYear(true);
                        }else{
                            stl.setMonth(cmbTargetPeriodStartDate.getDate().getMonth());
                            stl.setYear(cmbTargetPeriodStartDate.getDate().getYear());
                        }
                        //Luc start add 20150709 #40006
                        if(ranking.getSelectedComponent().equals(mainStaffScroll)) {
                            stl.setPic(true);
                        }else {
                            stl.setPic(false);
                        }
                         //Luc end add 20150709 #40006 
                        //����������
                        stl.setCourseFlag(flagCourse);
                        stl.load();
                    }

                    //�X�܃^�u
                    if (ranking.getSelectedComponent().equals(shopScroll)) {
                        //�X�܃^�u���I������Ă���ꍇ
                        srl = new ShopRankingList();
                        //nhanvt start add 20150309 New request #35223
                        changeTargetDate();
                        //nhanvt end add 20150309 New request #35223
                        //�Ώۊ���
                        srl.setTermFrom(cmbTargetPeriodStartDate.getDate());
                        srl.setTermTo(cmbTargetPeriodEndDate.getDate());
                        //�ŋ敪
                        srl.setTaxType(getSelectedTaxType());
                        //�\���͈�
                        srl.setRangeDisplay(rangeDisplay.getSelectedIndex());
                        //�\����
                        srl.setOrderDisplay(orderDisplay.getSelectedIndex());

                        //nhanvt start add 20141216 New request #33406
                        if(!lstShopCategoryId.equals("")){
                            srl.setListCategoryId(lstShopCategoryId);
                        }else{
                            srl.setListCategoryId(null);
                        }

                        srl.setHideCategory(isHideCategory);
                        srl.setUseShopCategory(useShopCategory);
                        //IVS_LVTu start edit 2015/04/14 Bug #36254
                        srl.setShopIDList(getShopIDList());
                        //IVS_LVTu end edit 2015/04/14 Bug #36254
                        //nhanvt end add 20141216 New request #33406
                        
                        if(rdoMutiShop.isSelected()){
                            srl.setIsMutiShop(true);
                        }
                        if(rdoLastYear.isSelected()){
                            srl.setIsLastYear(true);
                        }else{
                            srl.setMonth(cmbTargetPeriodStartDate.getDate().getMonth());
                            srl.setYear(cmbTargetPeriodStartDate.getDate().getYear());
                        }
                        //����������
                        srl.setCourseFlag(flagCourse);
                        srl.load();
                        
//                       //nhanvt start add 20150309 New request #35223
//                        //�Ώۊ���
//                        srlLast.setTermFrom(cmbTargetPeriodStartDate.getDate());
//                        srlLast.setTermTo(cmbTargetPeriodEndDate.getDate());
//                        //�ŋ敪
//                        srlLast.setTaxType(getSelectedTaxType());
//                        //�\���͈�
//                        srlLast.setRangeDisplay(rangeDisplay.getSelectedIndex());
//                        //�\����
//                        srlLast.setOrderDisplay(orderDisplay.getSelectedIndex());
//                        
//                        if(!lstShopCategoryId.equals("")){
//                            srlLast.setListCategoryId(lstShopCategoryId);
//                        }else{
//                            srlLast.setListCategoryId(null);
//                        }
//
//                        srlLast.setHideCategory(isHideCategory);
//                        srlLast.setUseShopCategory(useShopCategory);
//                        if(rdoMutiShop.isSelected()){
//                            srlLast.setIsMutiShop(true);
//                        }
//                        if(rdoLastYear.isSelected()){
//                            srlLast.setIsLastYear(true);
//                        }else{
//                            srlLast.setMonth(cmbTargetPeriodStartDate.getDate().getMonth());
//                            srlLast.setYear(cmbTargetPeriodStartDate.getDate().getYear());
//                        }
//                        
//                        srlLast.loadLastYear();
//                        //nhanvt end add 20150309 New request #35223
                    }

                    //�Z�p�E���i�^�u
                     if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll) || ranking.getSelectedComponent().equals(courseScroll)) {
                    //if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll)) {
                        //�Z�p�^�u�܂��͏��i�^�u���I������Ă���ꍇ
                        trl = new ProductRankingList();
                        //�I�𒆂̃^�u�C���f�b�N�X
                        if (ranking.getSelectedComponent().equals(techScroll)) {
                            trl.setSelectedTabIndex(3);
                        } else if(ranking.getSelectedComponent().equals(itemScroll)){
                            trl.setSelectedTabIndex(4);
                        }
                        else
                            {
                                trl.setSelectedTabIndex(5);
                            }
                        //�X��ID���X�g
                        trl.setShopIDList(getShopIDList());
                        //�S����ID
                        if (staff.getSelectedIndex() > 0) {
                            trl.setStaffID(((MstStaff)staff.getSelectedItem()).getStaffID());
                        }
                        //�Ώۊ���
                        trl.setTermFrom(cmbTargetPeriodStartDate.getDate());
                        trl.setTermTo(cmbTargetPeriodEndDate.getDate());
                        //�ŋ敪
                        trl.setTaxType(getSelectedTaxType());
                        //����ID
                        if (technicClass.getSelectedIndex() > 0) {
                            if (ranking.getSelectedComponent().equals(techScroll)) {
                                //�Z�p����
                                trl.setProdClassID(((MstTechnicClass)technicClass.getSelectedItem()).getTechnicClassID());
                            } else if(ranking.getSelectedComponent().equals(itemScroll)) {
                                //���i����
                                trl.setProdClassID(((MstItemClass)technicClass.getSelectedItem()).getItemClassID());
                            }
                            else
                            {
                                trl.setProdClassID(((MstCourseClass)technicClass.getSelectedItem()).getCourseClassID());
                            }
                        }
                        //�\����
                        trl.setOrderDisplay(orderDisplay.getSelectedIndex());

                        //nhanvt start add 20141216 New request #33406
                        if(!lstShopCategoryId.equals("")){
                            trl.setListCategoryId(lstShopCategoryId);
                        }else{
                            trl.setListCategoryId(null);
                        }

                        trl.setHideCategory(isHideCategory);
                        trl.setUseShopCategory(useShopCategory);
                        //nhanvt end add 20141216 New request #33406
                        //����������
                        trl.load();
                    }

                    //�ڋq�^�u
                    if (ranking.getSelectedComponent().equals(decileScroll)) {
                        //�ڋq�^�u���I������Ă���ꍇ
                        crl = new CustomerRankingList();
                        //�X��ID���X�g
                        crl.setShopIDList(getShopIDList());
                        //�S����ID
                        if (staff.getSelectedIndex() > 0) {
                            crl.setStaffID(((MstStaff)staff.getSelectedItem()).getStaffID());
                        }
                        //�Ώۊ���
                        crl.setTermFrom(cmbTargetPeriodStartDate.getDate());
                        crl.setTermTo(cmbTargetPeriodEndDate.getDate());
                        //�ŋ敪
                        crl.setTaxType(getSelectedTaxType());
                        //�]���Ώ�
                        crl.setProductDivision(productDivision.getSelectedIndex());
                        //nhanvt start add 20141216 New request #33406
                        if(!lstShopCategoryId.equals("")){
                            crl.setListCategoryId(lstShopCategoryId);
                        }else{
                            crl.setListCategoryId(null);
                        }

                        crl.setHideCategory(isHideCategory);
                        crl.setUseShopCategory(useShopCategory);
                        //nhanvt end add 20141216 New request #33406
                        //����������
                        crl.load_CustomerRanking();
                    }
                    
                    //�l����
//                    if (ranking.getSelectedComponent().equals(individualsalesScroll)) {
//                        //�ڋq�^�u���I������Ă���ꍇ
//                        isl = new IndividualSalesRankingList();
//                        //�S����ID
//                        if (staff.getSelectedIndex() > 0) {
//                            isl.setStaffID(((MstStaff)staff.getSelectedItem()).getStaffID());
//                        }
//                        //�Ώۊ���
//                        isl.setTermFrom(cmbTargetPeriodStartDate.getDate());
//                        isl.setTermTo(cmbTargetPeriodEndDate.getDate());
//                        //�ŋ敪
//                        isl.setTaxType(getSelectedTaxType());
//                        //�\���͈�
//                        isl.setRangeDisplay(rangeDisplay.getSelectedIndex());
//                        //�\����
//                        isl.setOrderDisplay(orderDisplay.getSelectedIndex());
//
//                        //����������
//                        isl.load();
//                    }
                    
                    //�f�[�^�\��
                    this.showData();
                }
            }catch(Exception e){
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            isShowButtonClicked = false;
	}
        
        //nhanvt start add 20150309 New request #35223
         /**
     * �ė��Z�o���Ԃ����߂�
     */
        private int getReappearanceSpan(){
            String textSpan = this.cmbReappearanceSpan.getSelectedItem().toString();

            if ("1����".equals(textSpan)) {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_30_fix");
                //    srlLast.setCountDate("30 day");
                //}
                if (srl != null) {
                    srl.setCountDate("30 day");
                    srl.setNameColumn("repert_30_fix");
                }
                
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_30_fix");
                //    stlLast.setCountDate("30 day");
                //}
                if (stl != null) {
                    stl.setCountDate("30 day");
                    stl.setNameColumn("repert_30_fix");
                }
                return 1;
            } else if ("45��".equals(textSpan)) {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_45_fix");
                //    srlLast.setCountDate("45 day");
                //}
                if (srl != null) {
                    
                    srl.setCountDate("45 day");
                    srl.setNameColumn("repert_45_fix");
                }
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_45_fix");
                //    stlLast.setCountDate("45 day");
                //}
                if (stl != null) {
                    stl.setCountDate("45 day");
                    stl.setNameColumn("repert_45_fix");
                }
                return 45;
            } else if ("2����".equals(textSpan)) {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_60_fix");
                //    srlLast.setCountDate("60 day");
                //}
                if (srl != null) {
                    srl.setCountDate("60 day");
                    srl.setNameColumn("repert_60_fix");
                }
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_60_fix");
                //    stlLast.setCountDate("60 day");
                //}
                if (stl != null) {
                    stl.setCountDate("60 day");
                    stl.setNameColumn("repert_60_fix");
                }
                return 2;
            } else if ("3����".equals(textSpan)) {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_90_fix");
                //    srlLast.setCountDate("90 day");
                //}
                if (srl != null) {
                    srl.setCountDate("90 day");
                    srl.setNameColumn("repert_90_fix");
                }
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_90_fix");
                //    stlLast.setCountDate("90 day");
                //}
                if (stl != null) {
                    stl.setCountDate("90 day");
                    stl.setNameColumn("repert_90_fix");
                }
                return 3;
            } else if ("4����".equals(textSpan)) {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_120_fix");
                //    srlLast.setCountDate("120 day");
                //}
                if (srl != null) {
                    srl.setCountDate("120 day");
                    srl.setNameColumn("repert_120_fix");
                }
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_120_fix");
                //    stlLast.setCountDate("120 day");
                //}
                if (stl != null) {
                    stl.setCountDate("120 day");
                    stl.setNameColumn("repert_120_fix");
                }
                return 4;
            } else if ("5����".equals(textSpan)) {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_150_fix");
                //    srlLast.setCountDate("150 day");
                //}
                if (srl != null) {
                    srl.setCountDate("150 day");
                    srl.setNameColumn("repert_150_fix");
                }
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_150_fix");
                //    stlLast.setCountDate("150 day");
                //}
                if (stl != null) {
                    stl.setCountDate("150 day");
                    stl.setNameColumn("repert_150_fix");
                }
                return 5;
            } else {
                //if (srlLast != null) {
                //    srlLast.setNameColumn("repert_180_fix");
                //    srlLast.setCountDate("180 day");
                //}
                if (srl != null) {
                    srl.setCountDate("180 day");
                    srl.setNameColumn("repert_180_fix");
                }
                //if (stlLast != null) {
                //    stlLast.setNameColumn("repert_180_fix");
                //    stlLast.setCountDate("180 day");
                //}
                if (stl != null) {
                    stl.setCountDate("180 day");
                    stl.setNameColumn("repert_180_fix");
                }
                return 6;
            }
        }
        
        private void changeTargetDate(){

            if (this.cmbTargetPeriodStartDate.getDate() == null || this.cmbTargetPeriodEndDate.getDate() == null ) return;

            // �ė�����
            int reappearanceCount = getReappearanceSpan();

            boolean isEndOfMonth = false;

            /*********************************/
            // �������O�̊J�n��
            /*********************************/
            Calendar calculationStart = Calendar.getInstance();
            calculationStart.setTime(cmbTargetPeriodStartDate.getDate());
            isEndOfMonth = calculationStart.getActualMaximum(Calendar.DAY_OF_MONTH) == calculationStart.get(Calendar.DAY_OF_MONTH);

            if(reappearanceCount == 45){

                // 45���ė�
                calculationStart.add(Calendar.MONTH, -2);

                // �����̏ꍇ
                if (isEndOfMonth) {
                    calculationStart.set(Calendar.DAY_OF_MONTH, 1);
                    calculationStart.add(Calendar.MONTH, 1);
                    calculationStart.add(Calendar.DAY_OF_MONTH, -1);
                }
                calculationStart.add(Calendar.DAY_OF_MONTH, 14);

            } else {

                // ����ȊO
                calculationStart.add(Calendar.MONTH, (reappearanceCount * -1));

                // �����̏ꍇ
                if (isEndOfMonth) {
                    calculationStart.set(Calendar.DAY_OF_MONTH, 1);
                    calculationStart.add(Calendar.MONTH, 1);
                    calculationStart.add(Calendar.DAY_OF_MONTH, -1);
                }
            }

            /*********************************/
            // �������O�̏I����
            /*********************************/
            Calendar calculationEnd = Calendar.getInstance();
            calculationEnd.setTime(cmbTargetPeriodEndDate.getDate());
            isEndOfMonth = calculationEnd.getActualMaximum(Calendar.DAY_OF_MONTH) == calculationEnd.get(Calendar.DAY_OF_MONTH);

            if(reappearanceCount == 45){

                // 45���ė�
                calculationEnd.add(Calendar.MONTH, -2);

                // �����̏ꍇ
                if (isEndOfMonth) {
                    calculationEnd.set(Calendar.DAY_OF_MONTH, 1);
                    calculationEnd.add(Calendar.MONTH, 1);
                    calculationEnd.add(Calendar.DAY_OF_MONTH, -1);
                }
                calculationEnd.add(Calendar.DAY_OF_MONTH, 14);

            } else {

                // ����ȊO
                calculationEnd.add(Calendar.MONTH, (reappearanceCount * -1));

                // �����̏ꍇ
                if (isEndOfMonth) {
                    calculationEnd.set(Calendar.DAY_OF_MONTH, 1);
                    calculationEnd.add(Calendar.MONTH, 1);
                    calculationEnd.add(Calendar.DAY_OF_MONTH, -1);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            
            if(srl != null){
                srl.setCalculationStartDate(sdf.format(calculationStart.getTime()));
                srl.setCalculationEndDate(sdf.format(calculationEnd.getTime()));
            }
            
            if(stl != null){
                stl.setCalculationStartDate(sdf.format(calculationStart.getTime()));
                stl.setCalculationEndDate(sdf.format(calculationEnd.getTime()));
            }
            
            
        }
        //nhanvt end add 20150309 New request #35223
		
        /**
         * �I�𒆂̐ŋ敪��Ԃ��܂��B(�P�F�Ŕ��F �Q�F�ō�)
         */
        private int getSelectedTaxType()
        {
                if(rdoTaxUnit.isSelected())
                {
                    return 2;
                }

                return	1;
        }
	
	/**
	 * �����L���O�f�[�^��\������B
	 */
	private void showData()
	{
            
            //��S���^�u
            if (ranking.getSelectedComponent().equals(mainStaffScroll)) {
                //��S���^�u���I������Ă���ꍇ
                //��S�������L���O�f�[�^
                this.showStaffRankingData();
            }

            //�{�p�S���^�u
            if (ranking.getSelectedComponent().equals(techStaffScroll)) {
                //�{�p�S���^�u���I������Ă���ꍇ
                //�{�p�S�������L���O�f�[�^
                this.showTechStaffRankingData();
            }

            //�X�܃^�u
            if (ranking.getSelectedComponent().equals(shopScroll)) {
                //�X�܃^�u���I�������  ����ꍇ
                //�X�܃����L���O�f�[�^
                if(isNonsSystem){
                    showShopRankingDataOld();
                }else{
                    this.showShopRankingData();
                }
            }

            //�Z�p�E���i�^�u
            if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll) || ranking.getSelectedComponent().equals(courseScroll)) {
            //if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll)) {
                //�Z�p�E���i�^�u���I������Ă���ꍇ
                //�Z�p�E���i�����L���O�f�[�^
                this.showProductRankingData();
            }

            //�ڋq�^�u
            if (ranking.getSelectedComponent().equals(decileScroll)) {
                //�ڋq�^�u���I������Ă���ꍇ
                //�ڋq�����L���O�f�[�^
                this.showCustomerRankingData();
            }
            
            //�l����
//            if (ranking.getSelectedComponent().equals(individualsalesScroll)) {
//                //�ڋq�^�u���I������Ă���ꍇ
//                //�ڋq�����L���O�f�[�^
//                this.showIndividualSalesRankingData();
//            }
	}
	
	
        //nhanvt start edit 20150309 New request #35223
        private void showStaffRankingData()
	{
            initTableRankingMain();   
            SwingUtil.clearTable(mainStaff);
            if (isShowButtonClicked && !dataExists(stl)) {
                return;
            }
            AttributiveCellTableModel model = (AttributiveCellTableModel) mainStaff.getModel();
            SwingUtil.clearTable(mainStaff);

            //�����L���O�̊J�n�ʒu
            int ranking = 1;
            int count = 0;
            String str = "0";
            for (StaffRanking st : stl) {

                Vector<Object> temp = new Vector<Object>();
                temp.add(ranking <= 3 ? getRankingIcon(ranking) : ranking);
                temp.add(st.getStaff().getFullStaffName());
                temp.add(st.getShopName());
                //LVTu start edit 2016/02/04 New request #46728
                //if (flagCourse) {
                //    temp.add(st.getTotalCount() + st.getCourseCount());
                //}else {
                    temp.add(st.getTotalCount());
                //}
                temp.add(st.getTechCount());
                temp.add(st.getTechSales());
                temp.add(st.getDesignatedCount());
                temp.add(st.getDesignatedSales());
                temp.add(st.getNewCount());
                temp.add(st.getIntroduceCount());
                if (flagCourse) {
                    temp.add(st.getCourseSales());
                    temp.add(st.getCourseDigestionSales());
                }
                temp.add(st.getItemSales());
                //LVTu start edit 2016/02/05 New request #46728
//                if (flagCourse) {
//                    temp.add(st.getTotalSales() + st.getCourseSales());
//                }else {
                    temp.add(st.getTotalSales());
//                }
                temp.add(st.getUnitValue());
                temp.add(st.getTechUnitValue());
                BigDecimal bd = null;
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);
                //IVS_TMTrong start edit 2015-07-20 Bug #40595
                if (st.getRepeat_total()!= 0) {
                    //bd = new BigDecimal((double) st.getRepeatRate() / (double) st.getTotalCount() * 100);
                    bd = new BigDecimal((double) st.getRepeat_count()/ (double) st.getRepeat_total()* 100);
                } else {
                    bd = new BigDecimal(0);
                }
                //IVS_TMTrong end edit 2015-07-20 Bug #40595
                str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                str = str.substring(0, str.length()-3);
                temp.add(str + "%");
                //temp.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                model.getDataVector().add(temp);

                Vector<Object> temp2 = new Vector<Object>();
                //�����l
                if(rdoValue.isSelected()) {
                    //�\�����e�͎����l���I�����Ă���B
                temp2.add(null);
                temp2.add(null);
                temp2.add(null);
                //if (flagCourse) {
                //    temp2.add(st.getTotalCountLast() + st.getCourseCountLast());
                //}else {
                    temp2.add(st.getTotalCountLast());
                //}
                temp2.add(st.getTechCountLast());
                temp2.add(st.getTechSalesLast());
                temp2.add(st.getDesignatedCountLast());
                temp2.add(st.getDesignatedSalesLast());
                temp2.add(st.getNewCountLast());
                temp2.add(st.getIntroduceCountLast());
                if (flagCourse) {
                    temp2.add(st.getCourseSalesLast());
                    temp2.add(st.getCourseDigestionSalesLast());
                }
                temp2.add(st.getItemSalesLast());
//                if (flagCourse) {
//                    temp2.add(st.getTotalSalesLast() + st.getCourseSalesLast());
//                }else {
                    temp2.add(st.getTotalSalesLast());
//                }
                //LVTu end edit 2016/02/05 New request #46728
                temp2.add(st.getUnitValueLast());
                temp2.add(st.getTechUnitValueLast());
                 //IVS_TMTrong start edit 2015-07-20 Bug #40595
                //if (st.getTotalCountLast() != 0) {
                if(st.getRepeat_total_last() != 0){
                    //bd = new BigDecimal((double) st.getRepeatRateLast() / (double) st.getTotalCountLast() * 100);
                    bd = new BigDecimal((double) st.getRepeat_count_last()/ (double) st.getRepeat_total_last()* 100);
                } else {
                    bd = new BigDecimal(0);
                }
                //IVS_TMTrong end edit 2015-07-20 Bug #40595
                str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                str = str.substring(0, str.length()-3);
                temp2.add(str + "%");
                //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                }else {
                    //�\�����e�͔䗦���I�����Ă���B
                    temp2.add(null);
                    temp2.add(null);
                    temp2.add(null);
                    //IVS_TMTrong start edit 2015-07-20 Bug #40595
                    if (st.getTotalCountLast()!= 0) {
                        bd = new BigDecimal((double) st.getTotalCount() / (double) st.getTotalCountLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }
                    //LVTu start edit 2016/03/03 Bug #48910
//                    if(rdoLastYear.isSelected()) {
//                        if (st.getRepeat_total_last()!= 0) {
//                            bd = new BigDecimal((double) st.getRepeat_total()/ (double) st.getRepeat_total_last()* 100);
//
//                        } else {
//                            bd = new BigDecimal(0);
//                        }
//                    }else {
//                        if (st.getTotalCountLast()!= 0) {
//                            bd = new BigDecimal((double) st.getRepeat_total()/ (double) st.getTotalCountLast()* 100);
//                        } else {
//                            bd = new BigDecimal(0);
//                        }
//                    }
                    
                    //IVS_TMTrong end edit 2015-07-20 Bug #40595
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    if (st.getTechCountLast() != 0) {

                        bd = new BigDecimal((double) st.getTechCount() / (double) st.getTechCountLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    if (st.getTechSalesLast() != 0) {
                        bd = new BigDecimal((double) st.getTechSales() / (double) st.getTechSalesLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                    if (st.getDesignatedCountLast() != 0) {
                        bd = new BigDecimal((double) st.getDesignatedCount() / (double) st.getDesignatedCountLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                    if (st.getDesignatedSalesLast() != 0) {
                        bd = new BigDecimal((double) st.getDesignatedSales() / (double) st.getDesignatedSalesLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    if (st.getNewCountLast() != 0) {
                        bd = new BigDecimal((double) st.getNewCount() / (double) st.getNewCountLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    if (st.getIntroduceCountLast() != 0) {
                        bd = new BigDecimal((double) st.getIntroduceCount() / (double) st.getIntroduceCountLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    if (flagCourse) {
                        if (st.getCourseSalesLast() != 0) {
                            bd = new BigDecimal((double) st.getCourseSales() / (double) st.getCourseSalesLast() * 100);
                        } else {
                            bd = new BigDecimal(0);
                        }
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                        str = str.substring(0, str.length()-3);
                        temp2.add(str + "%");

                        if (st.getCourseDigestionSalesLast() != 0) {
                            bd = new BigDecimal((double) st.getCourseDigestionSales() / (double) st.getCourseDigestionSalesLast() * 100);
                        } else {
                            bd = new BigDecimal(0);
                        }
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                        str = str.substring(0, str.length()-3);
                        temp2.add(str + "%");
                    }
                    if (st.getItemSalesLast() != 0) {
                        bd = new BigDecimal((double) st.getItemSales() / st.getItemSalesLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    if (st.getTotalSalesLast() != 0) {
                        bd = new BigDecimal((double) st.getTotalSales() / (double) st.getTotalSalesLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                    if (st.getUnitValueLast() != 0) {
                        bd = new BigDecimal((double) st.getUnitValue() / (double) st.getUnitValueLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                    if (st.getTechUnitValueLast() != 0) {
                        bd = new BigDecimal((double) st.getTechUnitValue() / (double) st.getTechUnitValueLast() * 100);


                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    double repeatRate = 0d;
                    double repeateRateLast = 0d;
                    if(st.getRepeat_total()!= 0) {
                        repeatRate = (Double)(st.getRepeat_count().doubleValue()/st.getRepeat_total().doubleValue()*100);
                    }
                    if(st.getRepeat_total_last()!= 0) {
                        repeateRateLast = (Double)st.getRepeat_count_last().doubleValue()/st.getRepeat_total_last().doubleValue()*100;
                    }
                    bd = new BigDecimal(repeatRate - repeateRateLast);
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    //temp2.add(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                }
                model.getDataVector().add(temp2);
                

                count += 2;
                ranking++;
            }

            CellAttribute cellAtt = new DefaultCellAttribute(count, model.getColumnCount());
            for (int i = 0; i < count; i++) {
                model.setCellAttribute(cellAtt);
                final CellSpan cellSpan = (CellSpan) model.getCellAttribute();
                int[] column1 = {0};
                int[] column2 = {1};
                int[] column3 = {2};
                int[] rowsss = {i, i + 1};
                cellSpan.combine(rowsss, column1);
                cellSpan.combine(rowsss, column2);
                cellSpan.combine(rowsss, column3);
                i++;
            }  
                
	}
        //LVTu end edit 2016/03/03 Bug #48910
        //nhanvt end edit 20150309 New request #35223

	/**
	 * �{�p�S�������L���O�f�[�^��\������B
	 */
	private void showTechStaffRankingData()
	{
                initTableRankingTech();
		SwingUtil.clearTable(techStaff);
        
                if (isShowButtonClicked && !dataExists(stl)) return;
        
		DefaultTableModel model = (DefaultTableModel)techStaff.getModel();
				
                //�����L���O�̊J�n�ʒu
                int ranking = 1;

                for(StaffRanking st : stl)
		{
                    if (flagCourse) {
                        Object[] rowData =
                                {
                                    ranking <= 3 ? getRankingIcon(ranking):ranking,
                                    st.getStaff().getFullStaffName(),
                                    st.getShopName(),
                                    st.getTotalCount(),
                                    st.getTechCount(),
                                    st.getTechSales(),
                                    st.getDesignatedCount(),
                                    st.getDesignatedSales(),
                                    st.getApproachedCount(),
                                    st.getApproachedSales(),
                                    st.getCourseSales(),
                                    st.getCourseDigestionSales(),
                                    st.getItemSales(),
                                    st.getTotalSales(),
                                    st.getUnitValue(),
                                    st.getTechUnitValue()
                                };
                            model.addRow(rowData);
                            ranking++;
                    }else {
                        Object[] rowData =
                            {
                                ranking <= 3 ? getRankingIcon(ranking):ranking,
                                st.getStaff().getFullStaffName(),
                                st.getShopName(),
                                st.getTotalCount(),
                                st.getTechCount(),
                                st.getTechSales(),
                                st.getDesignatedCount(),
                                st.getDesignatedSales(),
                                st.getApproachedCount(),
                                st.getApproachedSales(),
                                st.getItemSales(),
                                st.getTotalSales(),
                                st.getUnitValue(),
                                st.getTechUnitValue()
                            };
                        model.addRow(rowData);
                        ranking++;
                    }
		}
	}
        //LVTu end edit 2016/02/04 New request #46728
        
        
	
	/**
	 * �X�܃����L���O�f�[�^��\������B
	 */
	private void showShopRankingData()
	{
		SwingUtil.clearTable(shop);
        
                if (isShowButtonClicked && !dataExists(srl)) return;
        
		DefaultTableModel model = (DefaultTableModel)shop.getModel();
				
                //�����L���O�̊J�n�ʒu
                int ranking = 1;
                Long taxPaymentValue;
                Long taxProductValue2;
                

                for(ShopRanking sr : srl)
		{
                    if(rdoTaxBlank.isSelected() == false)
                    {
                        Object[] rowData =
                                {
                                    ranking <= 3 ? getRankingIcon(ranking):ranking,
                                    sr.getPrefixString(),
                                    sr.getShopName(),
                                    sr.getTotalCount(),
                                    sr.getTechCount(),
                                    sr.getNewCustomerCount(),
                                    sr.getNewCustomerCountRatio(),
                                    sr.getTechSales(),
                                    sr.getTechSales1(),
                                    sr.getTotalPaymentValue(),
                                    sr.getTotalProductValue1(),
                                    sr.getTotalProductValue2(),
                                    sr.getTechUnitValue(),
                                    sr.getItemSales(),
                                    sr.getTotalSales(),
                                    sr.getTotalStaff(),
                                    sr.getOneSalesPerson(),
                                    sr.getUnitValue()
                                };
                            model.addRow(rowData);
                    }
                    else
                    {
                        taxPaymentValue = SystemInfo.getTax(sr.getTotalPaymentValue(), SystemInfo.getSystemDate());
                        taxProductValue2 = SystemInfo.getTax(sr.getTotalProductValue2(), SystemInfo.getSystemDate());
                        Object[] rowData =
                                {
                                    ranking <= 3 ? getRankingIcon(ranking):ranking,
                                    sr.getPrefixString(),
                                    sr.getShopName(),
                                    sr.getTotalCount(),
                                    sr.getTechCount(),
                                    sr.getNewCustomerCount(),
                                    sr.getNewCustomerCountRatio(),
                                    sr.getTechSales(),
                                    sr.getTechSales1(),
                                    sr.getTotalPaymentValue() - taxPaymentValue,
                                    sr.getTotalProductValue1() - SystemInfo.getTax(sr.getTotalProductValue1(), SystemInfo.getSystemDate()),
                                    sr.getTotalProductValue2() - taxProductValue2,
                                    sr.getTechUnitValue(),
                                    sr.getItemSales(),
                                    sr.getTotalSales() - taxPaymentValue - taxProductValue2,
                                    sr.getTotalStaff(),
                                    sr.getOneSalesPerson(),
                                    sr.getUnitValue()
                                };
                            model.addRow(rowData);
                    }
                    ranking++;
		}
                
	}
        
        /**
	 * �X�܃����L���O�f�[�^��\������B
	 */
        //nhanvt start add 20150309 New request #35223
	private void showShopRankingDataOld()
	{
            initTableShopRankingOld();
	    SwingUtil.clearTable(shop);
            if (isShowButtonClicked && !dataExists(srl)) {
                return;
            }
            AttributiveCellTableModel model = (AttributiveCellTableModel) shop.getModel();
            SwingUtil.clearTable(shop);
            //�����L���O�̊J�n�ʒu
            int ranking = 1;
            int count = 0;
            BigDecimal bd = null;
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(2);
            String str = "0";
            for (ShopRanking sr : srl) {

                Vector<Object> temp = new Vector<Object>();
                temp.add(ranking <= 3 ? getRankingIcon(ranking) : ranking);
                temp.add(sr.getShopName());
                temp.add(sr.getTotalCount());
                temp.add(sr.getTechCount());
                temp.add(sr.getNewCustomerCount());
                temp.add(sr.getNewCustomerCountRatio());
                temp.add(sr.getTechSales());
                temp.add(sr.getTechUnitValue());
                if (flagCourse) {
                    temp.add(sr.getCourseSales());
                    temp.add(sr.getCourseDigestionSales());
                }
                temp.add(sr.getItemSales());
                temp.add(sr.getTotalSales());
                temp.add(sr.getUnitValue());
                //IVS_TMTrong start edit 20150721 Bug #40595
//                if (sr.getTotalCount() != 0) {
//                    bd = new BigDecimal((double) sr.getRepeatRate() / (double) sr.getTotalCount() * 100);
//
//                } else {
//                    bd = new BigDecimal(0);
//                }
                if (sr.getRepeatTotal()!= 0) {
                    bd = new BigDecimal((double) sr.getRepeatCount()/ (double) sr.getRepeatTotal()* 100);

                } else {
                    bd = new BigDecimal(0);
                }
                //IVS_TMTrong end edit 20150721 Bug #40595
                str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                str = str.substring(0, str.length()-3);
                temp.add(str + "%");

                Double averg = 0d;
                try {
                    if (sr.getStaffCount()*srl.getCountMonth() != 0) {
                     averg = (double)sr.getTotalSales() / ((double)sr.getStaffCount()/(double)srl.getCountMonth());
                    }
                }catch (Exception e) {}
                
                temp.add(averg.longValue());
                
                //��N�̃f�[�^
                model.getDataVector().add(temp);
                Vector<Object> temp2 = new Vector<Object>();
                if(rdoValue.isSelected()) {
                    //�\�����e�͎����l���I�����Ă���B
                temp2.add(null);
                temp2.add(sr.getShopName());
                temp2.add(sr.getTotalCountLast());
                temp2.add(sr.getTechCountLast());
                temp2.add(sr.getNewCustomerCountLast());
                temp2.add(sr.getNewCustomerCountRatioLast());
                temp2.add(sr.getTechSalesLast());
                temp2.add(sr.getTechUnitValueLast());
                if (flagCourse) {
                    temp2.add(sr.getCourseSalesLast());
                    temp2.add(sr.getCourseDigestionSalesLast());
                }
                temp2.add(sr.getItemSalesLast());
                temp2.add(sr.getTotalSalesLast());
                temp2.add(sr.getUnitValueLast());
                //IVS_TMTrong start edit 20150721 Bug #40595
//                if (sr.getTotalCountLast() != 0) {
//                    bd = new BigDecimal((double) sr.getRepeatRateLast() / (double) sr.getTotalCountLast() * 100);
//                } else {
//                    bd = new BigDecimal(0);
//                }
                if (sr.getRepeatTotalLast()!= 0) {
                    bd = new BigDecimal((double) sr.getRepeatCountLast()/ (double) sr.getRepeatTotalLast()* 100);
                } else {
                    bd = new BigDecimal(0);
                }
                //IVS_TMTrong end edit 20150721 Bug #40595
                
                            str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                            str = str.substring(0, str.length()-3);
                            temp2.add(str + "%");
                            averg = 0d;
                            try {
                     if (sr.getStaffCountLast()*srl.getCountMonthLast() != 0) {
                        averg = sr.getTotalSalesLast() / ((double)sr.getStaffCountLast()/(double)srl.getCountMonthLast());
                                 }
                            }catch (Exception e) {}
                           
                           temp2.add(averg.longValue());
                            
                }else {
                    //�\�����e�͔䗦���I�����Ă���B    
                    temp2.add(null);
                    temp2.add(sr.getShopName());
                    //IVS_TMTrong start add 20150721 Bug #40595
                    if (sr.getTotalCountLast() != 0) {
                        bd = new BigDecimal((double) sr.getTotalCount() / (double) sr.getTotalCountLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }
                    //IVS_TMTrong end add 20150721 Bug #40595
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");

                    if (sr.getTechCountLast() != 0) {

                        bd = new BigDecimal((double) sr.getTechCount() / (double) sr.getTechCountLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }

                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (sr.getNewCustomerCountLast() != 0) {
                        bd = new BigDecimal((double) sr.getNewCustomerCount() / (double) sr.getNewCustomerCountLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }

                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (sr.getTechCountLast() == 0) {
                        bd = new BigDecimal(0);
                    } else {

                        if (sr.getTechCountLast() * sr.getNewCustomerCountLast() == 0) {
                            bd = new BigDecimal(0);
                        } else {
                            double techR =  sr.getNewCustomerCount().doubleValue()/sr.getTechCount().doubleValue()*100;
                            double techRLast = sr.getNewCustomerCountLast().doubleValue()/sr.getTechCountLast().doubleValue()*100;
                            if (sr.getNewCustomerCount() * sr.getNewCustomerCount() != 0) {
                                bd = new BigDecimal((techR - techRLast));

                            } else {
                                bd = new BigDecimal(0);
                            }
                        }

                    }

                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (sr.getTechSalesLast() != 0) {
                        bd = new BigDecimal((double) sr.getTechSales() / (double) sr.getTechSalesLast() * 100);
                    } else {
                        bd = new BigDecimal(0);
                    }

                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (sr.getTechUnitValueLast() != 0) {
                        bd = new BigDecimal((double) sr.getTechUnitValue() / (double) sr.getTechUnitValueLast() * 100);

                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (flagCourse) {
                        if (sr.getCourseSalesLast()!= 0) {
                            bd = new BigDecimal((double) sr.getCourseSales() / (double) sr.getCourseSalesLast() * 100);
                        } else {
                            bd = new BigDecimal(0);
                        }
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                        str = str.substring(0, str.length()-3);
                        temp2.add(str + "%");
                        if (sr.getCourseDigestionSalesLast()!= 0) {
                            bd = new BigDecimal((double) sr.getCourseDigestionSales()/ (double) sr.getCourseDigestionSalesLast() * 100);
                        } else {
                            bd = new BigDecimal(0);
                        }
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                        str = str.substring(0, str.length()-3);
                        temp2.add(str + "%");
                    }
                    if (sr.getItemSalesLast() != 0) {
                        bd = new BigDecimal((double) sr.getItemSales() / (double) sr.getItemSalesLast() * 100);
                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (sr.getTotalSalesLast() != 0) {
                        bd = new BigDecimal((double) sr.getTotalSales() / sr.getTotalSalesLast() * 100);
                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    if (sr.getUnitValueLast() != 0) {
                        bd = new BigDecimal((double) sr.getUnitValue() / (double) sr.getUnitValueLast() * 100);
                    } else {
                        bd = new BigDecimal(0);
                    }
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    
                    //IVS_TMTrong start add 20150721 Bug #40595
//                    if (sr.getTotalCountLast() != 0) {
//
//                        if (sr.getTotalCount() * sr.getRepeatRate() != 0) {
//
//                            bd = new BigDecimal(((double) sr.getRepeatRate() / (double) sr.getTotalCount() * 100) -((double) sr.getRepeatRateLast() / (double) sr.getTotalCountLast() * 100));
//                        } else {
//                            if (sr.getTotalCount() * sr.getRepeatRate() != 0) {
//                                bd = new BigDecimal(((double) sr.getRepeatRate() / (double) sr.getTotalCount() * 100));
//                            } else {
//                                bd = new BigDecimal(0);
//                            }
//                        }
//
//
//                    } else {
//                        bd = new BigDecimal(0);
//                    }
                    if (sr.getRepeatTotalLast()!= 0) {
                        if (sr.getRepeatTotal()* sr.getRepeatCount()!= 0) {
                            bd = new BigDecimal(((double) sr.getRepeatCount()/ (double) sr.getRepeatTotal()* 100) -((double) sr.getRepeatCountLast()/ (double) sr.getRepeatTotalLast()* 100));
                        } else {
                            if (sr.getRepeatTotal()* sr.getRepeatCount()!= 0) {
                                bd = new BigDecimal(((double) sr.getRepeatCount()/ (double) sr.getRepeatTotal()* 100));
                            } else {
                                bd = new BigDecimal(0);
                            }
                        }
                    } else {
                        bd = new BigDecimal(0);
                    }
                    //IVS_TMTrong end add 20150721 Bug #40595
                    str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");


//                    if (sr.getStaffCount()*srl.getCountMonth() != 0) {
//                        double aa = (double) sr.getTotalSales() / ((double) sr.getStaffCount()/(double)srl.getCountMonth());
//                        double bb = 0.00;
//                        if (sr.getStaffCountLast()*srl.getCountMonth() != 0) {
//                            bb = (double) sr.getTotalSalesLast() / ((double) sr.getStaffCountLast()/(double)srl.getCountMonth());
//                        }
//                        bd = new BigDecimal((aa - bb) * 100);
//
//                    } else {
//                        double bb = 0;
//                        if (sr.getStaffCountLast()*srl.getCountMonth() != 0) {
//                            bb = (double) sr.getTotalSalesLast() / ((double) sr.getStaffCountLast()/(double)srl.getCountMonth());
//                        }
//                        bd = new BigDecimal((0 - bb) * 100);
//                    }
                   
                    averg = 0d;
                    try {
                    if (sr.getStaffCount()*srl.getCountMonth() != 0) {
                            averg = sr.getTotalSales() / ((double)sr.getStaffCount()/(double)srl.getCountMonth());
                         }
                    }catch (Exception e) {}
                    double avergLast = 0;
                     try {
                         if (sr.getStaffCountLast()*srl.getCountMonthLast() != 0) {
                            avergLast = sr.getTotalSalesLast() / ((double)sr.getStaffCountLast()/(double)srl.getCountMonthLast());
                         }
                    }catch (Exception e) {}
                    Double  rate = 0d;
                    if(avergLast!=0) {
                         rate = averg/avergLast *100;
                    }
                    str = nf.format(rate.longValue());
                    str = str.substring(0, str.length()-3);
                    temp2.add(str + "%");
                    
                }
                      

                        model.getDataVector().add(temp2);
                count += 2;
                ranking++;
            }

            CellAttribute cellAtt = new DefaultCellAttribute(count, model.getColumnCount());
            for (int i = 0; i < count; i++) {
                model.setCellAttribute(cellAtt);
                final CellSpan cellSpan = (CellSpan) model.getCellAttribute();
                int[] column1 = {0};
                int[] column2 = {1};
                int[] rowsss = {i, i + 1};
                cellSpan.combine(rowsss, column1);
                cellSpan.combine(rowsss, column2);
                i++;

            }
	}
        //nhanvt end add 20150309 New request #35223
	
	/**
	 * �Z�p�E���i�����L���O�f�[�^��\������B
	 */
	private void showProductRankingData()
	{
		DefaultTableModel model = null;

                // �Z�p�^�u
                if (ranking.getSelectedComponent().equals(techScroll)) {
                    SwingUtil.clearTable(tech);

                    if (isShowButtonClicked && !dataExists(trl)) return;

                    model = (DefaultTableModel)tech.getModel();
                }

                // ���i�^�u
                if (ranking.getSelectedComponent().equals(itemScroll)) {
                    SwingUtil.clearTable(item);

                    if (isShowButtonClicked && !dataExists(trl)) return;

                    model = (DefaultTableModel)item.getModel();
                }
                
                if (ranking.getSelectedComponent().equals(courseScroll)) {
                    SwingUtil.clearTable(course);

                    if (isShowButtonClicked && !dataExists(trl)) return;

                    model = (DefaultTableModel)course.getModel();
                }

                //�����L���O�̊J�n�ʒu
                int ranking = 1;

                NumberFormat nf = NumberFormat.getInstance();

                for(ProductRanking pt : trl)
		{
		    Object[] rowData =
			    {
				ranking <= 3 ? getRankingIcon(ranking):ranking,
                                pt.getClassName(),
				pt.getProdName(),
				nf.format(pt.getUnitPrice()),
				pt.getProdCount(),
				nf.format(pt.getProdSales()),
				nf.format(pt.getProdDiscount()),
				pt.getCountRatio(),
				pt.getSalesRatio(),
			    };
			model.addRow(rowData);
                        ranking++;
		}
	}
        
        /**
	 * showIndividualSalesRankingData
	 */
	private void showIndividualSalesRankingData()
	{
		SwingUtil.clearTable(individualsales);
        
                if (isShowButtonClicked && !dataExists(isl)) return;
        
		DefaultTableModel model = (DefaultTableModel)individualsales.getModel();
				
                //�����L���O�̊J�n�ʒu
                int ranking = 1;
                long sumgetTechSum = 0;
                long sumgetTechCount = 0;
                long sumgetNominaSum = 0;
                long sumgetNominaCount = 0;
                long sumgetItemPrice = 0;
                long sumgetCountItemPrice = 0;
                long sumgetClaimSum = 0;
                long sumgetClaimCount = 0;
                long sumgetTotalSales = 0;         
                long sumgetMiSum = 0;
                long sumgetMiCount = 0;
                long sumgetMcSum = 0;
                long sumgetMcCount = 0;
                long sumgetConSumpSum = 0;
                long sumgetConSumpCount = 0;
                NumberFormat nf = NumberFormat.getInstance();

                for(IndividualSalesRanKing isr : isl)
		{
                    sumgetTotalSales = sumgetTotalSales + isr.getTotalSales();
                    sumgetTechSum = sumgetTechSum + isr.getTechSum();
                    sumgetTechCount = sumgetTechCount + isr.getTechCount();
                    sumgetNominaSum = sumgetNominaSum + isr.getNominaSum();
                    sumgetNominaCount = sumgetNominaCount + isr.getNominaCount();
                    sumgetItemPrice = sumgetItemPrice + isr.getItemPrice();
                    sumgetCountItemPrice = sumgetCountItemPrice + isr.getCount_item_price();
                    sumgetClaimSum = sumgetClaimSum + isr.getClaimSum();
                    sumgetClaimCount = sumgetClaimCount + isr.getClaimCount();
                    sumgetMiSum = sumgetMiSum + isr.getMisum();
                    sumgetMiCount = sumgetMiCount + isr.getMicount();
                    sumgetMcSum = sumgetMcSum + isr.getMcsum();
                    sumgetMcCount = sumgetMcCount + isr.getMccount();
                    sumgetConSumpSum =  sumgetConSumpSum + isr.getConsumpsum();
                    sumgetConSumpCount = sumgetConSumpCount + isr.getConsumpcount();
		   Object[] rowData =
			    {
				ranking <= 3 ? getRankingIcon(ranking):ranking,
				isr.getStaffName(),
                                isr.getTotalSales(),
				isr.getTechSum(),
                                isr.getTechCount(),
				isr.getNominaSum(),
                                isr.getNominaCount(),				
                                isr.getItemPrice(),
                                isr.getCount_item_price(),
				isr.getClaimSum(),
				isr.getClaimCount(),
				isr.getMisum(),
                                isr.getMicount(),
                                isr.getMcsum(),
                                isr.getMccount(),
                                isr.getConsumpsum(),
                                isr.getConsumpcount(),
			    };
			model.addRow(rowData);
                        ranking++;
		}
                Object[] newrowData  = 
                {
                            ranking,
                            "���v",
                             sumgetTotalSales, 
                             sumgetTechSum,
                             sumgetTechCount,
                             sumgetNominaSum,
                             sumgetNominaCount,
                             sumgetItemPrice,
                             sumgetCountItemPrice,
                             sumgetClaimSum,
                             sumgetClaimCount,
                             sumgetMiSum,
                             sumgetMiCount,
                             sumgetMcSum,
                             sumgetMcCount,
                             sumgetConSumpSum,
                             sumgetConSumpCount
                };
                if (ranking > 1)
                {
                    model.addRow(newrowData);
                    ranking++;
                }
	}

	/**
	 * �ڋq�����L���O�f�[�^��\������B
	 */
	private void showCustomerRankingData()
	{
                if (decile.getCellEditor() != null) {
                    decile.getCellEditor().stopCellEditing();
                }
            
		DefaultTableModel model = null;
                
                SwingUtil.clearTable(decile);
                
                model = (DefaultTableModel)decile.getModel();

                //�����L���O�̊J�n�ʒu
                int ranking = 1;

                NumberFormat nf = NumberFormat.getInstance();

                double totalTargetCount = 0d;
                double totalVisitCount = 0d;
                double totalSalesValue = 0d;
                
                for(CustomerRanking cr : crl)
		{
		    Object[] rowData =
			    {
				ranking <= 3 ? getRankingIcon(ranking):ranking,
				nf.format(cr.getTargetCount()),
				nf.format(cr.getVisitCount()),
				nf.format(cr.getSalesValue()),
				cr.getSalesShareRate(),
				cr.getTotalSalesShareRate(),
				nf.format(cr.getAvgUnitPrice()),
				nf.format(cr.getAvgVisitCount()),
				nf.format(cr.getAvgVisitPrice()),
				getDetailButton(),
			    };
			model.addRow(rowData);
                        ranking++;
                        
                        totalTargetCount += cr.getTargetCount();
                        totalVisitCount += cr.getVisitCount();
                        totalSalesValue += cr.getSalesValue();
		}
                
                model = (DefaultTableModel)decileTotal.getModel();
                model.setValueAt(nf.format(totalTargetCount), 0, 0);
                model.setValueAt(nf.format(totalVisitCount), 0, 1);
                model.setValueAt(nf.format(totalSalesValue), 0, 2);
                model.setValueAt(nf.format(0), 0, 3);
                model.setValueAt(nf.format(0), 0, 4);
                model.setValueAt(nf.format(0), 0, 5);

                if (isShowButtonClicked && !dataExists(crl)) return;
                
                BigDecimal bd = null;
                double avgUnitPrice = 0d;
                double avgVisitCount = 0d;
                
                if (totalTargetCount > 0) {
                    // �q�P��
                    avgUnitPrice = totalSalesValue / totalTargetCount;
                    bd = new BigDecimal(avgUnitPrice);
                    model.setValueAt(nf.format(bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue()), 0, 3);

                    // ���ϗ��X��
                    avgVisitCount = totalVisitCount / totalTargetCount;
                    bd = new BigDecimal(avgVisitCount);
                    model.setValueAt(nf.format(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()), 0, 4);
                }

                if (avgVisitCount > 0) {
                    // ���ϗ��X�P��
                    bd = new BigDecimal(avgUnitPrice / avgVisitCount);
                    model.setValueAt(nf.format(bd.setScale(1, BigDecimal.ROUND_HALF_UP).longValue()), 0, 5);
                }
	}

        /**
	 * ���[�U�����{�^�����擾����
	 */
	private JButton getDetailButton()
	{
		JButton button = new JButton();
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/rank_list_off.jpg")));
		button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/rank_list_on.jpg")));
		button.setSize(48, 25);

		button.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            try {

                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                showStaffShopRankingDecilePanel();

                            } finally {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
		});
		return button;
	}        
        
        private void showStaffShopRankingDecilePanel() {                                           
            StaffShopRankingDecilePanel dcl = new StaffShopRankingDecilePanel(this.getTitle());
            dcl.setSelectedRow(decile.getSelectedRow());
            dcl.setTarget(target.getSelectedItem());
            
            if (staff.getSelectedIndex() > 0) {
                dcl.setStaffName(((MstStaff)staff.getSelectedItem()).getFullStaffName());
            } else {
                dcl.setStaffName("�w��Ȃ�");
            }
            dcl.setTargetPeriodStartDate(cmbTargetPeriodStartDate.getDateStr("/"));
            dcl.setTargetPeriodEndDate(cmbTargetPeriodEndDate.getDateStr("/"));
            dcl.setTaxName(rdoTaxUnit.isSelected() ? "�ō�" : "�Ŕ�");
            dcl.setProductDivisionLabelName("�]���Ώ�");
            dcl.setProductDivisionName(productDivision.getSelectedItem().toString());
            dcl.setRankLabel("�����N");
            dcl.setRankNo("�y " + String.valueOf(decile.getSelectedRow() + 1) + " �z");
            dcl.setLimitCount(crl.getLimitCount());
            dcl.showData();
            
            dcl.setOpener(this);
            this.setVisible(false);
            parentFrame.changeContents(dcl);
        } 
        
        
        //nhanvt start add 20141216 New request #33406
     /**
        * init data for table �ƑԖ� left with group shop
        */
       private void initCategoryWithGroupShop() {
           
           SimpleMaster sm = new SimpleMaster(
                   "",
                   "mst_shop_category",
                   "shop_category_id",
                   "shop_class_name", 0);

           sm.loadData();
           mscg = new MstShopCategorys();
           for (MstData md : sm) {
               if(md != null){
                   MstShopCategory category = new MstShopCategory();
                   category.setShopCategoryId(md.getID());
                   category.setShopClassName(md.getName());
                   category.setDisplaySeq(md.getDisplaySeq());
                   mscg.add(category);
               }

           }

       } 

       /**
        * init data for table �ƑԖ� left with muti shop
        */
       private void initCategoryWithMutiShop() {
            MstShop ms = (MstShop)target.getSelectedItem();
            if(ms.getShopID() != null){
               try
               {
                       mscg = new MstShopCategorys();
                       ConnectionWrapper	con	=	SystemInfo.getConnection();
                       mscg.loadByShop(con,ms.getShopID());
               }
               catch(SQLException e)
               {

                       SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
               }
            }

       }  

       /**
        * change combo and show again two table
        * change combo
        * @param evt 
        */
        private void chargeDataCombo() {   

            //mrsRef = new MstShopCategorys();
            //mrsUse = new MstShopCategorys();
            //�O���[�v
            if(target.getSelectedItem() instanceof MstGroup) {
                     displayForDesign(true, 1);
                     useShopCategory = 1;
                     isHideCategory = true;
                     initCategoryWithGroupShop();
            }
            //�X��
            else if(target.getSelectedItem() instanceof MstShop) {
                 MstShop ms = (MstShop)target.getSelectedItem();
                 if(ms.getUseShopCategory().equals(1)){
                        displayForDesign(false, 1);
                        useShopCategory = 1;
                        isHideCategory = true;
                        initCategoryWithMutiShop();
                 }else{
                        displayForDesign(false, 0);
                        useShopCategory = 0;
                        isHideCategory = false;
                 }
            }

            this.showDataUseShopCategory(mscg);
       } 
        
        /**
        * control design screen follow combo �Ώ�
        * @param isGroup
        * @param useShopCategory 
        */
       public void displayForDesign(boolean isGroup, Integer useShopCategory){

           if(isGroup){
               //Luc start edit 20150213  #35207
               if(SystemInfo.getSetteing().isUseShopCategory()) {
                    SwingUtil.clearTable(useShopCategoryTable);
                    useShopCategoryTable.setVisible(true);
                    useShopCategoryScrollPane.setVisible(true);
                    lblCategory.setVisible(true);
               }else {
                    useShopCategoryTable.setVisible(false);
                    useShopCategoryScrollPane.setVisible(false);
                    lblCategory.setVisible(false);
               }
               //Luc end edit 20150213  #35207
               //IVS_TMTrong start edit 2015-07-20 Bug #40595
               rdoMutiShop.setSelected(false);
               rdoAllShop.setSelected(true);
               //Luc start delete 20150724 #40595
               //rdoMutiShop.setEnabled(false);
               //rdoAllShop.setEnabled(true);
               //Luc end delete 20150724 #40595
               //IVS_TMTrong end edit 2015-07-20 Bug #40595
           }else{
               MstShop ms = (MstShop)target.getSelectedItem();
               if(ms.getUseShopCategory().equals(1)){
                    SwingUtil.clearTable(useShopCategoryTable);
                    useShopCategoryTable.setVisible(true);
                    useShopCategoryScrollPane.setVisible(true);
                    lblCategory.setVisible(true);
                    //IVS_TMTrong start edit 2015-07-20 Bug #40595
                    rdoMutiShop.setSelected(true);
                    rdoAllShop.setSelected(false);
                    rdoMutiShop.setEnabled(true);
                    rdoAllShop.setEnabled(true);
                    //IVS_TMTrong end edit 2015-07-20 Bug #40595

               }else{
                    SwingUtil.clearTable(useShopCategoryTable);
                    useShopCategoryTable.setVisible(false);
                    useShopCategoryScrollPane.setVisible(false);
                    lblCategory.setVisible(false);
                    rdoMutiShop.setSelected(true);
                    rdoAllShop.setSelected(false);
                    rdoMutiShop.setEnabled(true);
                    rdoAllShop.setEnabled(true);
               }
           }

       }
        /**
	 * function show data on table 
	 */
	private void showDataUseShopCategory(MstShopCategorys listData)
	{
               
            SwingUtil.clearTable(useShopCategoryTable);
            DefaultTableModel model	= (DefaultTableModel)useShopCategoryTable.getModel();
            //�S�s�폜
            model.setRowCount(0);
            for(MstShopCategory mtc : listData)
            {
                if(mtc != null){
                    Vector<Object> temp	= new Vector<Object>();
                    temp.add(mtc.getShopClassName());
                    temp.add(false);
                    temp.add(mtc.getShopCategoryId());
                    model.addRow(temp);  
                }

            }   
             
	}
        //nhanvt end add 20141216 New request #33406
        
	/**
	 * �����L���O��ʗpFocusTraversalPolicy���擾����B
	 * @return  �����L���O��ʗpFocusTraversalPolicy
	 */
	public StaffShopRankingFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	/**
	 * �����L���O��ʗpFocusTraversalPolicy
	 */
	private class StaffShopRankingFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponentStaffShopRankingFocusTraversalPolicy�B
		 * aContainer �� aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_�łȂ���΂Ȃ�܂���B
		 * @param aContainer aComponent �̃t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @param aComponent aContainer �̂����炭�ԐړI�Ȏq�A�܂��� aContainer ����
		 * @return aComponent �̂��ƂɃt�H�[�J�X���󂯎�� Component�B�K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
                        if (aComponent.equals(target))
			{
                            if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(techStaffScroll) ||
                                   ranking.getSelectedComponent().equals(shopScroll)) {
                                return cmbTargetPeriodStartDate;
                            }
                            return staff;
				
			}
                        else if (aComponent.equals(staff))
			{
				return cmbTargetPeriodStartDate;
			}
			else if (aComponent.equals(cmbTargetPeriodStartDate))
			{
                            return cmbTargetPeriodEndDate;
			}
                        else if(aComponent.equals(cmbTargetPeriodEndDate)){
                            return this.getSelectedTaxType();
                        }
                        else if (aComponent.equals(rdoTaxBlank) ||
					aComponent.equals(rdoTaxUnit))
			{
                           if(!technicClass.isEnabled()){
                               if (!productDivision.isEnabled()){
                                   if(!rangeDisplay.isEnabled()){
                                       return orderDisplay;
                                   }
                                   return rangeDisplay;
                               }
                               return productDivision;
                           }
                           return technicClass;
                            
			}
			else if (aComponent.equals(technicClass))
			{
				if (!productDivision.isEnabled()){
                                   if(!rangeDisplay.isEnabled()){
                                       return orderDisplay;
                                   }
                                   return rangeDisplay;
                               }
                               return productDivision;
			}
			else if (aComponent.equals(productDivision))
			{
				if(rangeDisplay.isEnabled()){
                                       return rangeDisplay;
                                   }
                                else if (orderDisplay.isEnabled()){  
                                    return orderDisplay ;
                                }
                                return this.getStartComponent();
			}
			else if (aComponent.equals(rangeDisplay))
			{
				return orderDisplay;
			}else if (aComponent.equals(orderDisplay))
			{
				if(target.isEnabled()){
                                    return target;
                                }else if(staff.isEnabled()){
                                    return staff;
                                }
                                return cmbTargetPeriodStartDate;
			}
			
			return this.getStartComponent();
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
			return this.getStartComponent();
		}

		/**
		 * �g���o�[�T���T�C�N���̍ŏ��� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�������̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer �擪�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̐擪�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return cmbTargetPeriodStartDate;
		}

		/**
		 * �g���o�[�T���T�C�N���̍Ō�� Component ��Ԃ��܂��B
		 * ���̃��\�b�h�́A�t�����̃g���o�[�T�������b�v����Ƃ��ɁA���Ƀt�H�[�J�X���� Component �𔻒肷�邽�߂Ɏg�p���܂��B
		 * @param aContainer aContainer - �Ō�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̍Ō�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return showButton;
		}

		/**
		 * �t�H�[�J�X��ݒ肷��f�t�H���g�R���|�[�l���g��Ԃ��܂��B
		 * aContainer �����[�g�Ƃ���t�H�[�J�X�g���o�[�T���T�C�N�����V�����J�n���ꂽ�Ƃ��ɁA���̃R���|�[�l���g�ɍŏ��Ƀt�H�[�J�X���ݒ肳��܂��B
		 * @param aContainer �f�t�H���g�� Component ��Ԃ��t�H�[�J�X�T�C�N���̃��[�g�܂��̓t�H�[�J�X�g���o�[�T���|���V�[�v���o�C�_
		 * @return aContainer �̃g���o�[�T���T�C�N���̃f�t�H���g�� Componet�A�܂��͓K�؂� Component ��������Ȃ��ꍇ�� null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return cmbTargetPeriodStartDate;
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
			return cmbTargetPeriodStartDate;
		}
		
                /**
                 * �I�𒆂̐ŋ敪��Ԃ��܂��B
                 */
		private Component getSelectedTaxType()
		{
			if(rdoTaxUnit.isSelected())
			{
				return	rdoTaxUnit;
			}
			
			return	rdoTaxBlank;
		}
                private Component getStartComponent()
		{
			if(target.getItemCount() == 1)
			{
				if(staff.isEnabled())
                                {
                                    return staff;
                                }
                                return cmbTargetPeriodStartDate;
			}
			
			return	target;
		} 
	}
	
	/**
	 * �����L���O�e�[�u���p��TableCellRenderer
	 */
	public class RankingTableCellRenderer extends SelectTableCellRenderer
	{
            /** Creates a new instance of RankingTableCellRenderer */
            public RankingTableCellRenderer()
            {
                super();
            }

            /**
             * �e�[�u���Z�������_�����O��Ԃ��܂��B
             * @param table JTable
             * @param value �Z���Ɋ��蓖�Ă�l
             * @param isSelected �Z�����I������Ă���ꍇ�� true
             * @param hasFocus �t�H�[�J�X������ꍇ�� true
             * @param row �s
             * @param column ��
             * @return �e�[�u���Z�������_�����O
             */
            public Component getTableCellRendererComponent(JTable table,
                            Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                //�����̐����z�u��ύX

                //��S���^�u
                if (ranking.getSelectedComponent().equals(mainStaffScroll)) {
                    switch(column)
                    {
                        case 0:
                            super.setHorizontalAlignment(SwingConstants.CENTER);
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            super.setHorizontalAlignment(SwingConstants.RIGHT);
                            break;
                        default:
                            super.setHorizontalAlignment(SwingConstants.LEFT);
                            break;
                    }
                }

                //�X�܃^�u
                if (ranking.getSelectedComponent().equals(shopScroll)) {
                    switch(column)
                    {
                        case 0:
                            super.setHorizontalAlignment(SwingConstants.CENTER);
                            break;
                        case 1:
                            super.setHorizontalAlignment(SwingConstants.LEFT);
                            break;
                        default:
                            super.setHorizontalAlignment(SwingConstants.RIGHT);
                            break;
                    }
                }

                //�Z�p�E���i�^�u
                // if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll) || ranking.getSelectedComponent().equals(courseScroll)) {
                if (ranking.getSelectedComponent().equals(techScroll) || ranking.getSelectedComponent().equals(itemScroll)) {
                    switch(column)
                    {
                        case 1:
                        case 2:
                            super.setHorizontalAlignment(SwingConstants.LEFT);
                            break;
                        default:
                            super.setHorizontalAlignment(SwingConstants.RIGHT);
                            break;
                    }
                }

                //�ڋq�^�u
                if (ranking.getSelectedComponent().equals(decileScroll)) {
                    switch(column)
                    {
                        default:
                            super.setHorizontalAlignment(SwingConstants.RIGHT);
                            break;
                    }
                }
                
                //�l����
//                if (ranking.getSelectedComponent().equals(individualsalesScroll)) {
//                    switch(column)
//                    {
//                        case 0:
//                            super.setHorizontalAlignment(SwingConstants.CENTER);
//                            break;
//                        case 1:
//                            super.setHorizontalAlignment(SwingConstants.LEFT);
//                            break;
//                        default:
//                            super.setHorizontalAlignment(SwingConstants.RIGHT);
//                            break;
//                    }
//                }

                return this;
            }
	}
	
	/**
	 * �ڋq(�f�V��)���v�e�[�u���p��TableCellRenderer
	 */
	public class CustomerTableCellRenderer extends SelectTableCellRenderer
	{
            /** Creates a new instance of RankingTableCellRenderer */
            public CustomerTableCellRenderer()
            {
                super();
            }

            /**
             * �e�[�u���Z�������_�����O��Ԃ��܂��B
             * @param table JTable
             * @param value �Z���Ɋ��蓖�Ă�l
             * @param isSelected �Z�����I������Ă���ꍇ�� true
             * @param hasFocus �t�H�[�J�X������ꍇ�� true
             * @param row �s
             * @param column ��
             * @return �e�[�u���Z�������_�����O
             */
            public Component getTableCellRendererComponent(JTable table,
                            Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                switch(column)
                {
                    default:
                        super.setHorizontalAlignment(SwingConstants.RIGHT);
                        break;
                }

                return this;
            }
	}
        
	/**
	 * JTable�̗񕝂�����������B
	 */
	private void initTableColumnWidth()
	{
            //��̕���ݒ肷��B

	    //��S���^�u
            mainStaff.getColumnModel().getColumn(0).setPreferredWidth(40);  //����
            mainStaff.getColumnModel().getColumn(1).setPreferredWidth(80);  //���O
	    mainStaff.getColumnModel().getColumn(2).setPreferredWidth(80);  //�X��
	    mainStaff.getColumnModel().getColumn(3).setPreferredWidth(40);  //���q��
	    mainStaff.getColumnModel().getColumn(4).setPreferredWidth(30);  //�Z�p�q��
	    mainStaff.getColumnModel().getColumn(5).setPreferredWidth(55);  //�Z�p����
	    mainStaff.getColumnModel().getColumn(6).setPreferredWidth(30);  //�w���q��
	    mainStaff.getColumnModel().getColumn(7).setPreferredWidth(55);  //�w������
	    mainStaff.getColumnModel().getColumn(8).setPreferredWidth(30);  //�V�K�q��
	    mainStaff.getColumnModel().getColumn(9).setPreferredWidth(30);  //�Љ�q��
	    mainStaff.getColumnModel().getColumn(10).setPreferredWidth(60);  //���i����
	    mainStaff.getColumnModel().getColumn(11).setPreferredWidth(60);  //������
	    mainStaff.getColumnModel().getColumn(12).setPreferredWidth(50);  //���q�P��
	    mainStaff.getColumnModel().getColumn(13).setPreferredWidth(50);  //�Z�p�q�P��
            mainStaff.getColumnModel().getColumn(14).setPreferredWidth(60);
	    //�{�p�S���^�u
            techStaff.getColumnModel().getColumn(0).setPreferredWidth(40);  //����
            techStaff.getColumnModel().getColumn(1).setPreferredWidth(90);  //���O
	    techStaff.getColumnModel().getColumn(2).setPreferredWidth(90);  //�X��
	    
            //�X�܃^�u
            shop.getColumnModel().getColumn(0).setPreferredWidth(40);       //����
            shop.getColumnModel().getColumn(1).setMinWidth(0);              //�X�܃R�[�h
            shop.getColumnModel().getColumn(1).setMaxWidth(0);
            shop.getColumnModel().getColumn(1).setPreferredWidth(0);
            shop.getColumnModel().getColumn(2).setPreferredWidth(170);      //�X��
            
	    //�Z�p�^�u
            tech.getColumnModel().getColumn(0).setPreferredWidth(40);   //����
            tech.getColumnModel().getColumn(1).setPreferredWidth(100);  //���ޖ�
            tech.getColumnModel().getColumn(2).setPreferredWidth(210);  //�Z�p��

	    //���i�^�u
            item.getColumnModel().getColumn(0).setPreferredWidth(40);   //����
            item.getColumnModel().getColumn(1).setPreferredWidth(100);  //���ޖ�
            item.getColumnModel().getColumn(2).setPreferredWidth(210);  //���i��
            
             //�R�[�X�^�u
            course.getColumnModel().getColumn(0).setPreferredWidth(40);   //����
            course.getColumnModel().getColumn(1).setPreferredWidth(100);  //���ޖ�
            course.getColumnModel().getColumn(2).setPreferredWidth(210);  //�R�[�X��
            
	    //�ڋq�^�u
            decile.getColumnModel().getColumn(1).setPreferredWidth(90);
            decile.getColumnModel().getColumn(2).setPreferredWidth(90);
            decile.getColumnModel().getColumn(3).setPreferredWidth(110);
            decile.getColumnModel().getColumn(4).setPreferredWidth(110);
            decile.getColumnModel().getColumn(5).setPreferredWidth(110);
            decile.getColumnModel().getColumn(6).setPreferredWidth(110);
            decile.getColumnModel().getColumn(7).setPreferredWidth(110);
            decile.getColumnModel().getColumn(8).setPreferredWidth(110);
            decile.getColumnModel().getColumn(9).setPreferredWidth(90);
            
            //�l����
            individualsales.getColumnModel().getColumn(0).setPreferredWidth(30);
            individualsales.getColumnModel().getColumn(1).setPreferredWidth(30);
            individualsales.getColumnModel().getColumn(2).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(3).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(4).setPreferredWidth(38);
            individualsales.getColumnModel().getColumn(5).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(6).setPreferredWidth(38);
            individualsales.getColumnModel().getColumn(7).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(8).setPreferredWidth(38);
            individualsales.getColumnModel().getColumn(9).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(10).setPreferredWidth(38);
            individualsales.getColumnModel().getColumn(11).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(12).setPreferredWidth(38);
            individualsales.getColumnModel().getColumn(13).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(14).setPreferredWidth(38);
            individualsales.getColumnModel().getColumn(15).setPreferredWidth(60);
            individualsales.getColumnModel().getColumn(16).setPreferredWidth(38);
	}
        
        /**
	 * �����L���O�A�C�R�����擾����
         * @param ranking �����L���O
         * @return �����L���O�A�C�R��
	 */
	private JButton getRankingIcon(int ranking)
	{
		JButton		rankingIcon	=	new JButton();
		rankingIcon.setBorderPainted(false);
		rankingIcon.setContentAreaFilled(false);
                if (ranking == 1) {
                    //�P�ʂ̏ꍇ
                    rankingIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/print/rank1.png")));
                }
                else if (ranking == 2)
                {
                    //�Q�ʂ̏ꍇ
                    rankingIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/print/rank2.png")));
                }
                else if (ranking == 3)
                {
                    //�R�ʂ̏ꍇ
                    rankingIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/" + SystemInfo.getSkinPackage() + "/button/print/rank3.png")));
                }
		rankingIcon.setSize(48, 25);
		return rankingIcon;
	}

	/**
	 * �I�𒆂̓X��ID�̃��X�g���擾����B
	 */
        private String getShopIDList() {
            
            String result = "";
            
            if(target.getSelectedItem() instanceof MstGroup) {
                //IVS_LVTu start edit 2015/04/14 Bug #36254
                //MstGroup select = (MstGroup)target.getSelectedItem();
                //�O���[�v
                //result = SystemInfo.getGroup().getShopIDListAll();
                //IVS_TMTrong start edit 20150721 Bug #40595
                if(target.getSelectedItem() instanceof MstGroup) {
                    MstGroup select = (MstGroup)target.getSelectedItem();
                    result = select.getShopIDListAll();
                }
                else if(target.getSelectedItem() instanceof MstShop) {
                    MstShop		ms	=	(MstShop)target.getSelectedItem();
                    result = ms.getShopID().toString();
                }
                //IVS_TMTrong start update 20150721 Bug #40595
                //IVS_LVTu end edit 2015/04/14 Bug #36254

            } else {

                //�X��
                result = ((MstShop)target.getSelectedItem()).getShopID().toString();
            }
            //IVS_TMTrong start edit 20150710 Bug #40133
            //IVS_LVTu start add 2015/04/14 Bug #36254
            //if(result.equals("")) {
               // result = "-1";
            //}
            //IVS_LVTu end add 2015/04/14 Bug #36254
            //IVS_TMTrong end edit 20150710 Bug #40133
            return result;
        }

	/**
	 * �I�𒆂̓X�܂̖��̂��擾����B
	 */
        private String getShopName() {
            
            String result = "";
            
            if(target.getSelectedItem() instanceof MstGroup) {

                //�O���[�v
                result = SystemInfo.getGroup().getGroupName();

            } else {

                //�X��
                result = ((MstShop)target.getSelectedItem()).getShopName();
            }
            
            return result;
        }
        
	/**
	 * �S���҂�����������B
	 */
	private void initStaff()
	{
            staff.removeAllItems();
            staff.addItem(new MstStaff());

            try {
                
                MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(), staff, getShopIDList());
                
            } catch(SQLException e) {
                
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage());
            }
            
            staff.setSelectedIndex(0);
            //nhanvt start add 20141216 New request #33406
            this.chargeDataCombo();
            //nhanvt end add 20141216 New request #33406
	}

        private boolean dataExists(ArrayList list) {
            
            boolean exists = list.size() > 0;
            
            if (!exists) {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(1112),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return exists;
        }

        /**
         * ���|�[�g�o�͂��s��
         */
        private void reportOutPut()
        {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                //��S���^�u
                if (ranking.getSelectedComponent().equals(mainStaffScroll)) {
                    this.mainStaffOutput();
                }

                //�{�p�S���^�u
                if (ranking.getSelectedComponent().equals(techStaffScroll)) {
                    this.techStaffOutput();
                }

                //�X�܃^�u
                if (ranking.getSelectedComponent().equals(shopScroll)) {
                    this.shopOutput();
                }

                //�Z�p�^�u
                if (ranking.getSelectedComponent().equals(techScroll)) {
                    this.techOutput();
                }

                //���i�^�u
                if (ranking.getSelectedComponent().equals(itemScroll)) {
                    this.itemOutput();
                }
                
                //�R�[�X�^�u
                if (ranking.getSelectedComponent().equals(courseScroll)) {
                    this.courseOutput();
                }

                //�ڋq�^�u
                if (ranking.getSelectedComponent().equals(decileScroll)) {
                    this.decileOutput();
                }
                
                //�l����
//                if (ranking.getSelectedComponent().equals(individualsalesScroll)) {
//                    this.individualsalesOutput();
//                }

            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

        private void showMessageNoData() {
            MessageDialog.showMessageDialog(
                this,
                MessageUtil.getMessage(4001),
                this.getTitle() + " - " + ranking.getTitleAt(ranking.getSelectedIndex()).trim(),
                JOptionPane.ERROR_MESSAGE);
        }
        
        private void headerOutput(JExcelApi jx, int row, int col1, int col2) {
            int col3 = 12;
	    // �ΏۓX��
            //IVS_TMTrong start edit 20150710 Bug #40133
            //jx.setValue(col1, row, target.isEnabled() ? this.getShopName() : "");
            jx.setValue(col1, row, target.getSelectedItem().toString());
            //IVS_TMTrong end edit 20150710 Bug #40133
	    // ����
            jx.setValue(col2, row, technicClass.isEnabled() ? technicClass.getSelectedItem().toString() : "");
            //nhanvt start
            if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(shopScroll)) {
                if(ranking.getSelectedComponent().equals(mainStaffScroll)){
                     jx.setValue(col3, row, rdoMutiShop.isSelected() ? "���X�V�K" : "�S�X�V�K");
                }else{
                    jx.setValue(10, row, rdoMutiShop.isSelected() ? "���X�V�K" : "�S�X�V�K");
                }
               
            }
            //nhanvt end
            row++;
	    // ��S����
            jx.setValue(col1, row, staff.isEnabled() && staff.getSelectedIndex() > 0 ? ((MstStaff)staff.getSelectedItem()).getFullStaffName() : "");
	    // �]���Ώ�
            jx.setValue(col2, row, productDivision.isEnabled() ? productDivision.getSelectedItem() : "");
            //nhanvt start
            if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(shopScroll)) {
                if(ranking.getSelectedComponent().equals(mainStaffScroll)){
                    jx.setValue(col3, row, (String)cmbReappearanceSpan.getSelectedItem());
                }else{
                    jx.setValue(10, row, (String)cmbReappearanceSpan.getSelectedItem());
                }
            }
            //nhanvt end

            row++;
            // �Ώۊ���
            jx.setValue(col1, row, cmbTargetPeriodStartDate.getDateStr("/") + " �` " + cmbTargetPeriodEndDate.getDateStr("/"));
            // �\���͈�
            jx.setValue(col2, row, rangeDisplay.isEnabled() ? rangeDisplay.getSelectedItem() : "");
            
             //nhanvt start
            if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(shopScroll)) {
                if(ranking.getSelectedComponent().equals(mainStaffScroll)){
                    jx.setValue(col3, row, rdoLastYear.isSelected() ? "��N�Δ�" : "�ڕW�Δ�");
                }else{
                    jx.setValue(10, row, rdoLastYear.isSelected() ? "��N�Δ�" : "�ڕW�Δ�");
                }
            }
            //nhanvt end

            row++;
            // �ŋ敪
            jx.setValue(col1, row, rdoTaxUnit.isSelected() ? "�ō�" : "�Ŕ�");
            // �\����
            jx.setValue(col2, row, orderDisplay.isEnabled() ? orderDisplay.getSelectedItem() : "");
            
             //vtnhan start add 20141201 New request #33406
            
            //nhanvt start
            if (ranking.getSelectedComponent().equals(mainStaffScroll) || ranking.getSelectedComponent().equals(shopScroll)) {
                if(ranking.getSelectedComponent().equals(mainStaffScroll)){
                    jx.setValue(col3, row, rdoValue.isSelected() ? "�����l" : "�䗦");
                }else{
                    jx.setValue(10, row, rdoValue.isSelected() ? "�����l" : "�䗦");
                }
            }
            //nhanvt end
            
            row++;
            if(isHideCategory){
                jx.setValue(col1-1, row, "�Ƒԕ��� �F");
                jx.setValue(col1, row,lstShopCategoryName );
               
            }
            //vtnhan end add 20141201 New request #33406
   
            
        }
        
        private void headerOutputOld(JExcelApi jx, int row, int col1, int col2) {
            
	    // �ΏۓX��
            jx.setValue(col1, row, target.isEnabled() ? this.getShopName() : "");
	    // ����
            jx.setValue(col2, row, technicClass.isEnabled() ? technicClass.getSelectedItem().toString() : "");
            
            row++;
	    // ��S����
            jx.setValue(col1, row, staff.isEnabled() && staff.getSelectedIndex() > 0 ? ((MstStaff)staff.getSelectedItem()).getFullStaffName() : "");
	    // �]���Ώ�
            jx.setValue(col2, row, productDivision.isEnabled() ? productDivision.getSelectedItem() : "");

            row++;
            // �Ώۊ���
            jx.setValue(col1, row, cmbTargetPeriodStartDate.getDateStr("/") + " �` " + cmbTargetPeriodEndDate.getDateStr("/"));
            // �\���͈�
            jx.setValue(col2, row, rangeDisplay.isEnabled() ? rangeDisplay.getSelectedItem() : "");

            row++;
            // �ŋ敪
            jx.setValue(col1, row, rdoTaxUnit.isSelected() ? "�ō�" : "�Ŕ�");
            // �\����
            jx.setValue(col2, row, orderDisplay.isEnabled() ? orderDisplay.getSelectedItem() : "");
        }
        
        private void mainStaffOutput() {

            if (mainStaff.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_��S��");
            if (mainStaff.getColumnCount() > 15) {
                jx.setTemplateFile("/reports/�����L���O_��S��_COURSE.xls");
            }else {
                jx.setTemplateFile("/reports/�����L���O_��S��.xls");
            }

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 8);
            
            //nhanvt start edit 20141201 New request #33406
            // ���b�Z�[�W
            jx.setValue(1, 15, this.message.getText());

	    int outRow = 10;
            //nhanvt end edit 20141201 New request #33406
	    
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, mainStaff.getRowCount() - 1);
	    
	    // �f�[�^�Z�b�g
            //���q��
            double customerCount = 0;
            double customerCountLast = 0;
            double customerCountLast1 = 0;
            
            //�Z�p�q��
            double customerTechCount = 0;
            double customerTechCountLast = 0;
            double customerTechCountLast1 = 0;
            
            //�Z�p����
            double techAmount = 0;
            double techAmountLast = 0;
            double techAmountLast1 = 0;
            
            //�w���q��
            double desinateCount = 0;
            double desinateCountLast = 0;
            double desinateCountLast1 = 0;
            
            //�w������
            double desinateAmount = 0;
            double desinateAmountLast = 0;
            double desinateAmountLast1 = 0;
            
            //�V�K�q��
            double newCount = 0;
            double newCountLast = 0;
             double newCountLast1 = 0;
            
            //�Љ�q��
            double introduceCount = 0;
            double introduceCountLast = 0;
            double introduceCountLast1 = 0;
            
            //�_����z
            double courseAmout = 0;
            double courseAmoutLast = 0;
            double courseAmoutLast1 = 0;
            
            //�������z
            double digestionAmout = 0;
            double digestionAmoutLast = 0;
            double digestionAmoutLast1 = 0;
            
            //���i����
            double itemAmout = 0;
            double itemAmoutLast = 0;
            double itemAmoutLast1 = 0;
            
            //������
            double totalAmout = 0;
            double totalAmoutLast = 0;
            double totalAmoutLast1 = 0;
            
            //���q�P��
            double totalCutomerPrice = 0;
            double totalCutomerPriceLast = 0;
            double totalCutomerPriceLast1 = 0;
            
             //���q�P��
            double totalTechCutomerPrice = 0;
            double totalTechCutomerPriceLast = 0;
            double totalTechCutomerPriceLast1 = 0;
            
             //�ė���

            
            double repeatCount = 0;
            double repeatCountLast = 0;
            double repeatCountTotal = 0;
            double repeatCountTotalLast = 0;
            
            DefaultTableModel model = (DefaultTableModel)mainStaff.getModel();
            for (int row = 0; row < mainStaff.getRowCount(); row++) {
                for (int col = 0; col < mainStaff.getColumnCount(); col++) {
                    
                        jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                        
                        //���q��
                        if(col == 3) {
                            if(row%2 ==0) {
                                customerCount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                customerCountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                customerCountLast1+= stl.get(row/2).getTotalCountLast();
                            }
                        }
                        if(col == 4) {
                            if(row%2 ==0) {
                                customerTechCount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                customerTechCountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                customerTechCountLast1 += stl.get(row/2).getTechCountLast();
                            }
                        }
                        if(col == 5) {
                            if(row%2 ==0) {
                                techAmount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                techAmountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                techAmountLast1 += stl.get(row/2).getTechSalesLast();
                            }
                        }
                        if(col == 6) {
                            if(row%2 ==0) {
                                desinateCount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                desinateCountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                desinateCountLast1+= stl.get(row/2).getDesignatedCountLast();
                            }
                        }
                        if(col == 7) {
                            if(row%2 ==0) {
                                desinateAmount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                desinateAmountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                desinateAmountLast1+= stl.get(row/2).getDesignatedSalesLast();
                            }
                        }
                        
                        if(col == 8) {
                            if(row%2 ==0) {
                                newCount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                newCountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                newCountLast1 += stl.get(row/2).getNewCountLast();
                            }
                        }
                        if(col == 9) {
                            if(row%2 ==0) {
                                introduceCount += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                            }else {
                                introduceCountLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                introduceCountLast1 += stl.get(row/2).getIntroduceCountLast();
                            }
                        }
                        if(flagCourse) {
                            if(col == 10) {
                                if(row%2 ==0) {
                                    courseAmout += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    courseAmoutLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    courseAmoutLast1 += stl.get(row/2).getCourseSalesLast();
                                }
                            }
                            if(col == 11) {
                                if(row%2 ==0) {
                                    digestionAmout += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    digestionAmoutLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    digestionAmoutLast1 += stl.get(row/2).getCourseDigestionSalesLast();
                                }
                            }
                            if(col == 12) {
                                if(row%2 ==0) {
                                    itemAmout += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    itemAmoutLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    itemAmoutLast1 += stl.get(row/2).getItemSalesLast();
                                }
                            }
                            if(col == 13) {
                                if(row%2 ==0) {
                                    totalAmout += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    totalAmoutLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    totalAmoutLast1 += stl.get(row/2).getTotalSalesLast();
                                }
                            }
                            if(col == 14) {
                                if(row%2 ==0) {
                                    totalCutomerPrice += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    totalCutomerPriceLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    try {
                                        totalCutomerPriceLast1 += stl.get(row/2).getTotalSalesLast()/stl.get(row/2).getTotalCountLast();
                                    }catch(Exception e) {
                                        
                                    }
                                }
                            }
                            if(col == 15) {
                                if(row%2 ==0) {
                                    totalTechCutomerPrice +=Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    totalTechCutomerPriceLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                     try {
                                        totalTechCutomerPriceLast1 += stl.get(row/2).getTechSalesLast()/stl.get(row/2).getTechCountLast();
                                    }catch(Exception e) {
                                        
                                    }
                                }
                            }
                            if(col == 16) {
                                if(row%2 ==0) {
                                    repeatCount +=  stl.get(row/2).getRepeat_count();
                                    repeatCountTotal +=  stl.get(row/2).getRepeat_total();
                                }else {
                                    repeatCountLast +=  stl.get(row/2).getRepeat_count_last();
                                    repeatCountTotalLast +=  stl.get(row/2).getRepeat_total_last();
                                }
                            }
                        }else {
                            if(col == 10) {
                                if(row%2 ==0) {
                                    itemAmout += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    itemAmoutLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    itemAmoutLast1 += stl.get(row/2).getItemSalesLast();
                                }
                            }
                            if(col == 11) {
                                if(row%2 ==0) {
                                    totalAmout += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    totalAmoutLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    totalAmoutLast1 += stl.get(row/2).getTotalSalesLast();
                                }
                            }
                            if(col == 12) {
                                if(row%2 ==0) {
                                    totalCutomerPrice += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    totalCutomerPriceLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                     try {
                                        totalCutomerPriceLast1 += stl.get(row/2).getTotalSalesLast()/stl.get(row/2).getTotalCountLast();
                                    }catch(Exception e) {
                                        
                                    }
                                }
                            }
                            if(col == 13) {
                                if(row%2 ==0) {
                                    totalTechCutomerPrice += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                }else {
                                    totalTechCutomerPriceLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%", "").replace(",",""));
                                    try {
                                        totalTechCutomerPriceLast1 += stl.get(row/2).getTechSalesLast()/stl.get(row/2).getTechCountLast();
                                    }catch(Exception e) {
                                        
                                    }
                                }
                            }
                            if(col == 14) {
                                if(row%2 ==0) {
                                    repeatCount +=  stl.get(row/2).getRepeat_count();
                                    repeatCountTotal +=  stl.get(row/2).getRepeat_total();
                                }else {
                                    repeatCountLast +=  stl.get(row/2).getRepeat_count_last();
                                    repeatCountTotalLast +=  stl.get(row/2).getRepeat_total_last();
                                }
                            }
                        }
                    
                }
                outRow++;
            }
            
            //�����l
            if(rdoValue.isSelected()) {
                //���q��
                jx.setValue(4,outRow+1,customerCount);
                jx.setValue(4,outRow+2,customerCountLast);
                
                //�Z�p�q��
                jx.setValue(5,outRow+1,customerTechCount);
                jx.setValue(5,outRow+2,customerTechCountLast);
                
                //�Z�p����
                jx.setValue(6,outRow+1,techAmount);
                jx.setValue(6,outRow+2,techAmountLast);
                
                //�w���q��
                jx.setValue(7,outRow+1,desinateCount);
                jx.setValue(7,outRow+2,desinateCountLast);
                
                //�w������
                jx.setValue(8,outRow+1,desinateAmount);
                jx.setValue(8,outRow+2,desinateAmountLast);
                
                //�V�K�q��
                jx.setValue(9,outRow+1,newCount);
                jx.setValue(9,outRow+2,newCountLast);
                
                //�Љ�q��
                jx.setValue(10,outRow+1,introduceCount);
                jx.setValue(10,outRow+2,introduceCountLast);
                
                if(flagCourse) {
                    //�_����z
                    jx.setValue(11,outRow+1,courseAmout);
                    jx.setValue(11,outRow+2,courseAmoutLast);
                    
                    //�������z
                    jx.setValue(12,outRow+1,digestionAmout);
                    jx.setValue(12,outRow+2,digestionAmoutLast);
                    
                    //���i����
                    jx.setValue(13,outRow+1,itemAmout);
                    jx.setValue(13,outRow+2,itemAmoutLast);
                    
                    //������
                    jx.setValue(14,outRow+1,totalAmout);
                    jx.setValue(14,outRow+2,totalAmoutLast);
                    
                    //���q�P��
                    jx.setValue(15,outRow+1,((Double)(totalAmout / customerCount)).longValue());
                    jx.setValue(15,outRow+2,((Double)(totalAmoutLast / customerCountLast)).longValue());
                    
                    //�Z�p�q�P��
                    jx.setValue(16,outRow+1,((Double)(techAmount/customerTechCount)).longValue());
                    jx.setValue(16,outRow+2,((Double)(techAmountLast/customerTechCountLast)).longValue());
                    
                    //�ė���
                    jx.setValue(17,outRow+1,String.valueOf(((Double)(repeatCount/ repeatCountTotal*100)).longValue()) + "%");
                    jx.setValue(17,outRow+2,String.valueOf(((Double)(repeatCountLast/ repeatCountTotalLast*100)).longValue()) + "%");
                }else {
                    
                    //���i����
                    jx.setValue(11,outRow+1,itemAmout);
                    jx.setValue(11,outRow+2,itemAmoutLast);
                    
                    //������
                    jx.setValue(12,outRow+1,totalAmout);
                    jx.setValue(12,outRow+2,totalAmoutLast);
                    
                     //���q�P��
                    jx.setValue(13,outRow+1,((Double)(totalAmout / customerCount)).longValue());
                    jx.setValue(13,outRow+2,((Double)(totalAmoutLast / customerCountLast)).longValue());
                    
                    //�Z�p�q�P��
                    jx.setValue(14,outRow+1,((Double)(techAmount/customerTechCount)).longValue());
                    jx.setValue(14,outRow+2,((Double)(techAmountLast/customerTechCountLast)).longValue());
                    
                    //�ė���
                    jx.setValue(15,outRow+1,String.valueOf(((Double)(repeatCount/repeatCountTotal*100)).longValue()) + "%");
                    jx.setValue(15,outRow+2,String.valueOf(((Double)(repeatCountLast/ repeatCountTotalLast*100)).longValue()) + "%");
                }
                
            //�䗦       
            }else {
                
                //���q��
                jx.setValue(4,outRow+1,customerCount);
                if(customerCountLast1!=0) {
                    jx.setValue(4,outRow+2,String.valueOf(((Double)(customerCount/customerCountLast1*100)).longValue())+ "%");
                }else {
                    jx.setValue(4,outRow+2, "0%");
                }
                
                //�Z�p�q��
                jx.setValue(5,outRow+1,customerTechCount);
                if(customerTechCountLast1!=0) {
                    jx.setValue(5,outRow+2,String.valueOf(((Double)(customerTechCount/customerTechCountLast1*100)).longValue())+ "%");
                }else {
                    jx.setValue(5,outRow+2,"0%");
                }
                
                //�Z�p����
                jx.setValue(6,outRow+1,techAmount);
                if(techAmountLast1!=0) {
                    jx.setValue(6,outRow+2,String.valueOf(((Double)(techAmount/techAmountLast1*100)).longValue())+ "%");
                }else {
                    jx.setValue(6,outRow+2,"0%");
                }
                
                //�w���q��
                jx.setValue(7,outRow+1,desinateCount);
                if(desinateCountLast1!=0) {
                    jx.setValue(7,outRow+2,String.valueOf(((Double)(desinateCount/desinateCountLast1*100)).longValue())+ "%");
                }else {
                   jx.setValue(7,outRow+2,"0%"); 
                }
                
                //�w������
                jx.setValue(8,outRow+1,desinateAmount);
                if(desinateAmountLast1!=0) {
                    jx.setValue(8,outRow+2,String.valueOf(((Double)(desinateAmount/desinateAmountLast1*100)).longValue())+ "%");
                }else {
                    jx.setValue(8,outRow+2,"0%");
                }
                
                //�V�K�q��
                jx.setValue(9,outRow+1,newCount);
                if(newCountLast1!=0) {
                    jx.setValue(9,outRow+2,String.valueOf(((Double)(newCount/newCountLast1*100)).longValue())+ "%");
                }else {
                    jx.setValue(9,outRow+2,"0%");
                }
                
                //�Љ�q��
                jx.setValue(10,outRow+1,introduceCount);
                if(introduceCountLast1!=0) {
                    jx.setValue(10,outRow+2,String.valueOf(((Double)(introduceCount/introduceCountLast1*100)).longValue())+ "%");
                }else {
                    jx.setValue(10,outRow+2,"0%");
                }
                
                if(flagCourse) {
                    //�_����z
                    jx.setValue(11,outRow+1,courseAmout);
                    if(courseAmoutLast1!=0) {
                        jx.setValue(11,outRow+2,String.valueOf(((Double)(courseAmout/courseAmoutLast1*100)).longValue())+ "%");
                    }else {
                        jx.setValue(11,outRow+2,"0%");
                    }
                    
                    //�������z
                    jx.setValue(12,outRow+1,digestionAmout);
                    if(digestionAmoutLast1!=0) {
                        jx.setValue(12,outRow+2,String.valueOf(((Double)(digestionAmout/digestionAmoutLast1*100)).longValue())+ "%");
                    }else {
                        jx.setValue(12,outRow+2, "0%");
                    }
                    
                    //���i����
                    jx.setValue(13,outRow+1,itemAmout);
                    if(itemAmoutLast1!=0) {
                        jx.setValue(13,outRow+2,String.valueOf(((Double)(itemAmout/itemAmoutLast1*100)).longValue())+ "%");
                    }else {
                        jx.setValue(13,outRow+2,"0%");
                    }
                    
                    //������
                    jx.setValue(14,outRow+1,totalAmout);
                    if(totalAmoutLast1!=0) {
                        jx.setValue(14,outRow+2,String.valueOf(((Double)(totalAmout/totalAmoutLast1*100)).longValue())+ "%");
                    }else {
                        jx.setValue(14,outRow+2, "0%");
                    }
                    
                    //���q�P��
                    jx.setValue(15,outRow+1,((Double)(totalAmout / customerCount)).longValue());
                    if(totalCutomerPriceLast1!=0) {
                        jx.setValue(15,outRow+2,String.valueOf(((Double)(totalCutomerPrice*100/totalCutomerPriceLast1)).longValue())+ "%");
                    }else {
                        jx.setValue(15,outRow+2,"0%");
                    }
                    //�Z�p�q�P��
                    jx.setValue(16,outRow+1,((Double)(techAmount/customerTechCount)).longValue());
                    if(totalTechCutomerPriceLast1!=0){
                        jx.setValue(16,outRow+2,String.valueOf(((Double)(totalTechCutomerPrice*100/totalTechCutomerPriceLast1)).longValue())+ "%");
                    }else {
                        jx.setValue(16,outRow+2, "0%");
                    }
                    //�ė���
                    if(repeatCountTotal!=0) {
                        jx.setValue(17,outRow+1,String.valueOf(((Double)(repeatCount/ repeatCountTotal*100)).longValue()) + "%");
                    }else {
                        jx.setValue(17,outRow+1, "0%");
                    }
                    double repeateR = 0;
                    
                    try {
                        repeateR = repeatCount/repeatCountTotal*100;
                    }catch(Exception e) {
                        
                    }
                    double repeateRLast = 0;
                    try {
                       repeateRLast =  repeatCountLast/ repeatCountTotalLast*100;
                        
                    }catch(Exception e) {}
                    
                    jx.setValue(17,outRow+2,String.valueOf(((Double)( repeateR - repeateRLast)).longValue()) + "%");
                }else {
                    
                    //���i����
                    jx.setValue(11,outRow+1,itemAmout);
                    if(itemAmoutLast1!=0) {
                        jx.setValue(11,outRow+2,String.valueOf(((Double)(itemAmout/itemAmoutLast1*100)).longValue())+ "%");
                    }else {
                        jx.setValue(11,outRow+2, "0%");
                    }
                    
                    //������
                    jx.setValue(12,outRow+1,totalAmout);
                    if(totalAmoutLast1!=0) {
                        jx.setValue(12,outRow+2,String.valueOf(((Double)(totalAmout/totalAmoutLast1*100)).longValue())+ "%");
                    }else {
                        jx.setValue(12,outRow+2,"0%");
                    }
                    
                    //���q�P��
                    jx.setValue(13,outRow+1,((Double)(totalAmout / customerCount)).longValue());
                    if(totalCutomerPriceLast1!=0) {
                        jx.setValue(13,outRow+2,String.valueOf(((Double)(totalCutomerPrice*100/totalCutomerPriceLast1)).longValue())+ "%");
                    }else {
                        jx.setValue(13,outRow+2,"0%");
                    }
                    
                    //�Z�p�q�P��
                    jx.setValue(14,outRow+1,((Double)(techAmount/customerTechCount)).longValue());
                    if(totalTechCutomerPriceLast1!=0) {
                        jx.setValue(14,outRow+2,String.valueOf(((Double)(totalTechCutomerPrice*100/totalTechCutomerPriceLast1)).longValue())+ "%" );
                    }else {
                        jx.setValue(14,outRow+2, "0%" );
                    }
                    
                    //�ė���
                    if(repeatCountTotal!=0) {
                        jx.setValue(15,outRow+1,String.valueOf(((Double)(repeatCount/ repeatCountTotal*100)).longValue()) + "%");
                    }else {
                        jx.setValue(15,outRow+1,"0%");
                    }
                    double repeateR = 0;
                    try {
                        repeateR = repeatCount/repeatCountTotal*100;
                    }catch(Exception e) {
                        
                    }
                    double repeateRLast = 0;
                    try {
                       repeateRLast =  repeatCountLast/ repeatCountTotalLast*100;
                        
                    }catch(Exception e) {}
                    jx.setValue(15,outRow+2,String.valueOf(((Double)(repeateR - repeateRLast)).longValue()) + "%");
                }
            }
             
            int count = 1;
            for(int i =10 ; i< outRow-1; i++ ){
                if(i%2 ==0){
                    jx.setValue(1, i,count);
                    count++;
                }
                
                jx.mergeCells(1, i , 1, i+1);
                jx.mergeCells(2, i , 2, i+1);
                jx.mergeCells(3, i , 3, i+1);
                i++;
            }
            
            
            jx.removeRow(outRow);

	    jx.openWorkbook();
        }
        
        private void techStaffOutput() {

            if (techStaff.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_�{�p�S��");
            if (techStaff.getColumnCount() > 14) {
                jx.setTemplateFile("/reports/�����L���O_�{�p�S��_COURSE.xls");
            }else {
                jx.setTemplateFile("/reports/�����L���O_�{�p�S��.xls");
            }

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 8);
            
            // ���b�Z�[�W
            //nhanvt start edit 20141201 New request #33406
            jx.setValue(1, 14, this.message.getText());

	    int outRow = 10;
	    //nhanvt start edit 20141201 New request #33406
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, techStaff.getRowCount() - 1);
	    
            //���S����
            Integer totalChargCount = 0;
            //�{�p�q��
            Integer bedCount = 0;
            //�{�p����
            Integer bedAmount = 0;
            //�{�p�w���q��
            Integer desinateCount = 0;
            //�{�p�w������
            Integer desinateAmount = 0;
            //AP�q��
            Integer apCount = 0;
            //AP����
            Integer apAmount = 0;
            //�_����z
            Integer courseAmount = 0;
            //�������z
            Integer digestionAmount = 0;
            //���i����
            Integer itemAmout = 0;
            //�S������
            Integer chargAmount = 0;
            //�q�P��
            Integer customerPrice = 0;
            //�{�p�q�P��
            Integer bedCustomerPrice = 0;
	    // �f�[�^�Z�b�g
            DefaultTableModel model = (DefaultTableModel)techStaff.getModel();
            for (int row = 0; row < techStaff.getRowCount(); row++) {
                for (int col = 0; col < techStaff.getColumnCount(); col++) {
                    jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                  
                            
                    if (col == 3) {
                        totalChargCount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                    }

                    if (col == 4) {
                        bedCount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));

                    }
                    if (col == 5) {
                        bedAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));

                    }
                    if (col == 6) {
                        desinateCount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));

                    }
                    if (col == 7) {
                        desinateAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));

                    }
                    if (col == 8) {
                        apCount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                    }
                    if (col == 9) {
                        apAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                    }
                    if(flagCourse) {
                        if (col == 10) {
                            courseAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 11) {
                            digestionAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 12) {
                            itemAmout += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 13) {
                            chargAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 14) {
                            customerPrice += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 15) {
                            bedCustomerPrice += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                     }else {
                       
                        if (col == 10) {
                            itemAmout += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 11) {
                            chargAmount += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 12) {
                            customerPrice += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        }
                        if (col == 13) {
                            bedCustomerPrice += Integer.parseInt(model.getValueAt(row, col).toString().replace(",", "").replace("%", ""));
                        } 
                    }             
                   
                }
                outRow++;
            }
            jx.setValue(4,outRow+1,totalChargCount);
            jx.setValue(5,outRow+1,bedCount);
            jx.setValue(6,outRow+1,bedAmount);
            jx.setValue(7,outRow+1,desinateCount);
            jx.setValue(8,outRow+1,desinateAmount);
            jx.setValue(9,outRow+1,apCount);
            jx.setValue(10,outRow+1,apAmount);
             if(flagCourse) {
                jx.setValue(11,outRow+1,courseAmount);
                jx.setValue(12,outRow+1,digestionAmount);
                jx.setValue(13,outRow+1,itemAmout);
                jx.setValue(14,outRow+1,chargAmount);
                //�q�P��
                jx.setValue(15,outRow+1,chargAmount/totalChargCount);
                //�{�p�q�P��
                jx.setValue(16,outRow+1,bedAmount/bedCount);
             }else {
                
                jx.setValue(11,outRow+1,itemAmout);
                jx.setValue(12,outRow+1,chargAmount);
                //�q�P��
                jx.setValue(13,outRow+1,chargAmount/totalChargCount);
                //�{�p�q�P��
                jx.setValue(14,outRow+1,bedAmount/bedCount);
             }

            jx.removeRow(outRow);

	    jx.openWorkbook();
        }

        private void shopOutput() {

            if (shop.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_�X��");
            if(isNonsSystem){
                if (shop.getColumnCount() > 13) {
                    jx.setTemplateFile("/reports/�����L���O_�X��_COURSE.xls");
                }else {
                    jx.setTemplateFile("/reports/�����L���O_�X��.xls");
                }
                // �w�b�_�o��
                this.headerOutput(jx, 3, 3, 7);
            }else{
                jx.setTemplateFile("/reports/�����L���O_�X��_NONS.xls");
                // �w�b�_�o��
                this.headerOutput(jx, 3, 4, 8);
            }
            
            //nhanvt start edit 20141201 New request #33406
            // ���b�Z�[�W
            jx.setValue(1, 15, this.message.getText());

	    int outRow = 10;
	    //nhanvt end edit 20141201 New request #33406
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, shop.getRowCount() - 1);
            
	    if(!isNonsSystem){
                // �f�[�^�Z�b�g
                try {

                    NumberFormat nf = NumberFormat.getInstance();
                    DefaultTableModel model = (DefaultTableModel)shop.getModel();
                    for (int row = 0; row < shop.getRowCount(); row++) {
                        for (int col = 0; col < shop.getColumnCount(); col++) {

                            switch (col) {
                                case 6:
                                    jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()).doubleValue() / 100);
                                    break;

                                default:
                                    jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                                    break;
                            }
                        }
                        outRow++;
                    }

                } catch (Exception e) {
                }
            }else{
                // �f�[�^�Z�b�g
                try {
                    //���q��
                    double customerCount = 0;
                    double customerCountLast = 0;

                    //�Z�p�q��
                    double customerTechCount = 0;
                    double customerTechCountLast = 0;

                    //�Z�p�V�K�q��
                    double newCustomerTechCount = 0;
                    double newCustomerTechCountLast = 0;

                     //�Z�p����
                    double TechAmount = 0;
                    double TechAmountLast = 0;
                    
                    //�_����z
                    double courseAmout = 0;
                    double courseAmoutLast = 0;

                    //�������z
                    double digestionAmout = 0;
                    double digestionAmoutLast = 0;

                    //���i����
                    double itemAmout = 0;
                    double itemAmoutLast = 0;

                    //������
                    double totalAmout = 0;
                    double totalAmoutLast = 0;

                     //�ė���
                    //double repeatRate = 0;
                    //double repeatRateLast = 0;
                    double repeateCount = 0;
                    double repeateCountTotal = 0;
                    double repeateCountLast = 0;
                    double repeateCountTotalLast = 0;
                    
                     //���X�^�b�t
                    double staffRate = 0;
                    double staffRateLast = 0;
                    
                    NumberFormat nf = NumberFormat.getInstance();
                    DefaultTableModel model = (DefaultTableModel)shop.getModel();
                    for (int row = 0; row < shop.getRowCount(); row++) {
                        for (int col = 0; col < shop.getColumnCount(); col++) {

                            switch (col) {
                                case 5:
                                    jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()).doubleValue() / 100);
                                    break;

                                default:
                                    jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                                    break;
                            }
                            
                            //���q��
                            if(col == 2) {
                                if(row%2 ==0) {
                                    customerCount += srl.get(row/2).getTotalCount();
                                }else {
                                    customerCountLast += srl.get(row/2).getTotalCountLast();
                                }
                            }
                            if(col == 3) {
                                if(row%2 ==0) {
                                    customerTechCount += srl.get(row/2).getTechCount();
                                }else {
                                    customerTechCountLast += srl.get(row/2).getTechCountLast();
                                }
                            }
                            if(col == 4) {
                                if(row%2 ==0) {
                                    newCustomerTechCount +=srl.get(row/2).getNewCustomerCount();
                                }else {
                                    newCustomerTechCountLast += srl.get(row/2).getNewCustomerCountLast();
                                }
                            }
                            
                            if(col == 6) {
                                if(row%2 ==0) {
                                    TechAmount +=  srl.get(row/2).getTechSales();
                                }else {
                                    TechAmountLast += srl.get(row/2).getTechSalesLast();
                                }
                            }
                            
                            if(flagCourse) {
                                if(col == 8) {
                                    if(row%2 ==0) {
                                        courseAmout += srl.get(row/2).getCourseSales();
                                    }else {
                                        courseAmoutLast += srl.get(row/2).getCourseSalesLast();
                                    }
                                }
                                if(col == 9) {
                                    if(row%2 ==0) {
                                        digestionAmout += srl.get(row/2).getCourseDigestionSales();
                                    }else {
                                        digestionAmoutLast += srl.get(row/2).getCourseDigestionSalesLast();
                                    }
                                }
                                if(col == 10) {
                                    if(row%2 ==0) {
                                        itemAmout += srl.get(row/2).getItemSales();
                                    }else {
                                        itemAmoutLast += srl.get(row/2).getItemSalesLast();
                                    }
                                }
                                if(col == 11) {
                                    if(row%2 ==0) {
                                        totalAmout += srl.get(row/2).getTotalSales();
                                    }else {
                                        totalAmoutLast += srl.get(row/2).getTotalSalesLast();
                                    }
                                }
                                
                                if(col == 13) {
                                    if(row%2 ==0) {
                                        repeateCount += srl.get(row/2).getRepeatCount();
                                        repeateCountTotal += srl.get(row/2).getRepeatTotal();
                                    }else {
                                        repeateCountLast += srl.get(row/2).getRepeatCountLast();
                                        repeateCountTotalLast += srl.get(row/2).getRepeatTotalLast();
                                    }
                                }
                                if(col == 14) {
                                    if(row%2 ==0) {
                                        staffRate += srl.get(row/2).getStaffCount();
                                    }else {
                                        staffRateLast += srl.get(row/2).getStaffCountLast();
                                    }
                                }
                            }else {
                                if(col == 8) {
                                    if(row%2 ==0) {
                                        itemAmout += srl.get(row/2).getItemSales();
                                    }else {
                                        itemAmoutLast += srl.get(row/2).getItemSalesLast();
                                    }
                                }
                                if(col == 9) {
                                    if(row%2 ==0) {
                                        totalAmout += srl.get(row/2).getTotalSales();
                                    }else {
                                        totalAmoutLast += srl.get(row/2).getTotalSalesLast();
                                    }
                                }

                                if(col == 11) {
                                    if(row%2 ==0) {
                                        repeateCount += srl.get(row/2).getRepeatCount();
                                        repeateCountTotal += srl.get(row/2).getRepeatTotal();
                                    }else {
                                        repeateCountLast += srl.get(row/2).getRepeatCountLast();
                                        repeateCountTotalLast += srl.get(row/2).getRepeatTotalLast();
                                    }
                                }
                                if(col == 12) {
                                    if(row%2 ==0) {
                                        staffRate +=Double.parseDouble(model.getValueAt(row, col).toString().replace("%","").replace(",", ""));
                                    }else {
                                        staffRateLast += Double.parseDouble(model.getValueAt(row, col).toString().replace("%","").replace(",", ""));
                                    }
                                }
                            }
                        }
                        outRow++;
                    }
                    //�����l
                    if(rdoValue.isSelected()) {
                        //���q��
                        jx.setValue(3,outRow+1,customerCount);
                        jx.setValue(3,outRow+2,customerCountLast);
                        
                        //�Z�p�q��
                        jx.setValue(4,outRow+1,customerTechCount);
                        jx.setValue(4,outRow+2,customerTechCountLast);
                        
                        //�Z�p�V�K�q��
                        jx.setValue(5,outRow+1,newCustomerTechCount);
                        jx.setValue(5,outRow+2,newCustomerTechCountLast);
                        
                        //�Z�p�V�K�䗦
                        jx.setValue(6,outRow+1,String.valueOf(((Double)(newCustomerTechCount/customerTechCount*100)).longValue()) + "%" );
                        jx.setValue(6,outRow+2,String.valueOf(((Double)(newCustomerTechCountLast/customerTechCountLast*100)).longValue()) + "%" );
                        
                        //�Z�p����
                        jx.setValue(7,outRow+1,TechAmount);
                        jx.setValue(7,outRow+2,TechAmountLast);
                        
                        //�Z�p�q�P��
                        jx.setValue(8,outRow+1,TechAmount/ customerTechCount);
                        jx.setValue(8,outRow+2,TechAmountLast/customerTechCountLast);

                        if(flagCourse) {
                            
                            //�_����z
                            jx.setValue(9,outRow+1,courseAmout);
                            jx.setValue(9,outRow+2,courseAmoutLast);
                            
                            //�������z
                            jx.setValue(10,outRow+1,digestionAmout);
                            jx.setValue(10,outRow+2,digestionAmoutLast);
                            
                            //���i����
                            jx.setValue(11,outRow+1,itemAmout);
                            jx.setValue(11,outRow+2,itemAmoutLast);
                            
                            //������
                            jx.setValue(12,outRow+1,totalAmout);
                            jx.setValue(12,outRow+2,totalAmoutLast);
                            
                            //�q�P��
                            jx.setValue(13,outRow+1,totalAmout/customerCount);
                            jx.setValue(13,outRow+2,totalAmoutLast/customerCountLast);
                            
                            //�ė���
                            jx.setValue(14,outRow+1,String.valueOf(((Double)(repeateCount/repeateCountTotal *100)).longValue()) + "%");
                            jx.setValue(14,outRow+2,String.valueOf(((Double)(repeateCountLast/repeateCountTotalLast *100)).longValue()) + "%");
                            
                            //���X�^�b�t
                            jx.setValue(15,outRow+1,staffRate);
                            jx.setValue(15,outRow+2,staffRateLast);
                        }else {
                            
                            //���i����
                            jx.setValue(9,outRow+1,itemAmout);
                            jx.setValue(9,outRow+2,itemAmoutLast);
                            
                            //������
                            jx.setValue(10,outRow+1,totalAmout);
                            jx.setValue(10,outRow+2,totalAmoutLast);
                            
                            //�q�P��
                            jx.setValue(11,outRow+1,totalAmout/customerCount);
                            jx.setValue(11,outRow+2,totalAmoutLast/customerCountLast);
                            
                            //�ė���
                            jx.setValue(12,outRow+1,String.valueOf(((Double)(repeateCount/repeateCountTotal *100)).longValue()) + "%");
                            jx.setValue(12,outRow+2,String.valueOf(((Double)(repeateCountLast/repeateCountTotalLast *100)).longValue()) + "%");
                            
                            //���X�^�b�t
                            jx.setValue(13,outRow+1,staffRate);
                            jx.setValue(13,outRow+2,staffRateLast);
                        }
                        
                    //�䗦    
                    }else {
                        
                        //���q��
                        jx.setValue(3,outRow+1,customerCount);
                        if(customerCountLast!= 0) {
                            jx.setValue(3,outRow+2,String.valueOf(((Double)(customerCount/customerCountLast*100)).longValue())+ "%" );
                        }else {
                            jx.setValue(3,outRow+2,"0%" );
                        }
                        
                        //�Z�p�q��
                        jx.setValue(4,outRow+1,customerTechCount);
                        if(customerTechCountLast!=0) {
                            jx.setValue(4,outRow+2,String.valueOf(((Double)(customerTechCount/customerTechCountLast*100)).longValue())+ "%" );
                        }else {
                            jx.setValue(4,outRow+2, "0%" );
                        }
                        
                        //�Z�p�V�K�q��
                        jx.setValue(5,outRow+1,newCustomerTechCount);
                        if(newCustomerTechCountLast!=0) {
                            jx.setValue(5,outRow+2,String.valueOf(((Double)(newCustomerTechCount/newCustomerTechCountLast*100)).longValue())+ "%" );
                        }else {
                            jx.setValue(5,outRow+2, "0%" );
                        }
                        
                        //�Z�p�V�K�䗦
                        jx.setValue(6,outRow+1,String.valueOf(((Double)(newCustomerTechCount/customerTechCount*100)).longValue()) + "%" );
                        if(newCustomerTechCountLast*customerTechCountLast!=0) {
                            jx.setValue(6,outRow+2,String.valueOf(((Double)(newCustomerTechCount/customerTechCount*100 - newCustomerTechCountLast/customerTechCountLast*100)).longValue()) + "%" );
                        }else {
                            jx.setValue(6,outRow+2, "0%" );
                        }
                        
                        //�Z�p����
                        jx.setValue(7,outRow+1,TechAmount);
                        if(TechAmountLast!=0) {
                            jx.setValue(7,outRow+2,String.valueOf(((Double)(TechAmount / TechAmountLast*100)).longValue())+ "%" );
                        }else {
                            jx.setValue(7,outRow+2,"0%" );
                        }
                        
                       
                        double techPrice = 0d;
                        try {
                                techPrice = TechAmount/ customerTechCount;
                        }catch(Exception e) {}
                        
                        double techPriceLast = 0d;
                        try {
                                techPriceLast = TechAmountLast/ customerTechCountLast;
                        }catch (Exception e) {}
                        
                        //�Z�p�q�P��
                        jx.setValue(8,outRow+1,techPrice);
                        if(techPriceLast!=0) {
                            jx.setValue(8,outRow+2,String.valueOf(((Double)(techPrice/techPriceLast*100)).longValue())+ "%" );
                        }else {
                            jx.setValue(8,outRow+2,"0%" );
                        }

                        if(flagCourse) {
                            
                            //�_����z
                            jx.setValue(9,outRow+1,courseAmout);
                            if(courseAmoutLast!=0) {
                                jx.setValue(9,outRow+2,String.valueOf(((Double)(courseAmout/courseAmoutLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(9,outRow+2,"0%" );
                            }
                            
                            //�������z
                            jx.setValue(10,outRow+1,digestionAmout);
                            if(digestionAmoutLast!=0) {
                                jx.setValue(10,outRow+2,String.valueOf(((Double)(digestionAmout/digestionAmoutLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(10,outRow+2,"0%" );
                            }
                            
                            //���i����
                            jx.setValue(11,outRow+1,itemAmout);
                            if(itemAmoutLast!=0) {
                                jx.setValue(11,outRow+2,String.valueOf(((Double)(itemAmout/itemAmoutLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(11,outRow+2,"0%" );
                            }
                            
                            //������
                            jx.setValue(12,outRow+1,totalAmout);
                            if(totalAmoutLast!=0) {
                                jx.setValue(12,outRow+2,String.valueOf(((Double)(totalAmout/totalAmoutLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(12,outRow+2,"0%" );
                            }
                            
                            //�q�P��
                            if(customerCount!= 0 ) {
                                jx.setValue(13,outRow+1,totalAmout/customerCount);
                            }else {
                                jx.setValue(13,outRow+1,0);
                            }
                            double price =  0;
                            try {
                                price = totalAmout/customerCount;
                            }catch(Exception e) {}
                            double priceLast = 0d;
                            try {
                                    priceLast = totalAmoutLast/customerCountLast;
                            }catch(Exception e) {}
                            
                            if(priceLast!= 0) {
                                jx.setValue(13,outRow+2,String.valueOf(((Double)(price/priceLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(13,outRow+2,"0%" );
                            }
                            
                            //�ė���
                            if(repeateCountTotal!= 0) {
                                jx.setValue(14,outRow+1,String.valueOf(((Double)(repeateCount/repeateCountTotal *100)).longValue()) + "%");
                            }else {
                                jx.setValue(14,outRow+1,"0%");
                            }
                            
                            double repeatR = 0;
                            try {
                            repeatR = repeateCount/repeateCountTotal *100;
                            }catch(Exception e) {}
                            double repeatRLast = 0;
                             try {
                                 repeatRLast = repeateCountLast/repeateCountTotalLast *100;
                             }catch(Exception e) {}
                            jx.setValue(14,outRow+2,String.valueOf(((Double)(repeatR -repeatRLast )).longValue()) + "%");
                            
                            //���X�^�b�t
                            jx.setValue(15,outRow+1,staffRate);
                            if(staffRateLast!=0) {
                                jx.setValue(15,outRow+2,String.valueOf(((Double)(staffRate/staffRateLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(15,outRow+2, "0%" );
                            }
                        }else {
                            
                            //���i����
                            jx.setValue(9,outRow+1,itemAmout);
                            if(itemAmoutLast!=0) {
                                jx.setValue(9,outRow+2,String.valueOf(((Double)(itemAmout/itemAmoutLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(9,outRow+2,"0%" );
                            }
                            
                            //������
                            jx.setValue(10,outRow+1,totalAmout);
                            if(totalAmoutLast!=0) {
                                jx.setValue(10,outRow+2,String.valueOf(((Double)(totalAmout/totalAmoutLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(10,outRow+2,"0%" );
                            }
                            
                            
                            if(customerCount!=0) {
                                jx.setValue(11,outRow+1,totalAmout/customerCount);
                            }else {
                                jx.setValue(11,outRow+1,0);
                            }
                            double price =  0;
                            try {
                                price = totalAmout/customerCount;
                            }catch(Exception e) {}
                            double priceLast = 0d;
                            try {
                                    priceLast = totalAmoutLast/customerCountLast;
                            }catch(Exception e) {}
                            
                            //�q�P��
                            if(priceLast!=0) {
                                jx.setValue(11,outRow+2,String.valueOf(((Double)(price/priceLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(11,outRow+2, "0%" );
                            }
                            
                           if(repeateCountTotal!= 0) {
                                jx.setValue(12,outRow+1,String.valueOf(((Double)(repeateCount/repeateCountTotal *100)).longValue()) + "%");
                            }else {
                                jx.setValue(12,outRow+1,"0%");
                            }
                            
                            double repeatR = 0;
                            try {
                            repeatR = repeateCount/repeateCountTotal *100;
                            }catch(Exception e) {}
                            double repeatRLast = 0;
                             try {
                                 repeatRLast = repeateCountLast/repeateCountTotalLast *100;
                             }catch(Exception e) {}
                            jx.setValue(12,outRow+2,String.valueOf(((Double)(repeatR -repeatRLast )).longValue()) + "%");
                            
                            //���X�^�b�t
                            jx.setValue(13,outRow+1,staffRate);
                            if(staffRateLast!=0) {
                                jx.setValue(13,outRow+2,String.valueOf(((Double)(staffRate/staffRateLast*100)).longValue())+ "%" );
                            }else {
                                jx.setValue(13,outRow+2, "0%" );
                            }
                        }
                        
                    }
                    int count = 1;
                    for(int i =10 ; i< outRow-1; i++ ){
                        if(i%2 ==0){
                            jx.setValue(1, i,count);
                            count++;
                        }

                        jx.mergeCells(1, i , 1, i+1);
                        jx.mergeCells(2, i , 2, i+1);
                        i++;
                    }

                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
            jx.removeRow(outRow);

	    jx.openWorkbook();
        }

        private void techOutput() {

            if (tech.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_�Z�p");
	    jx.setTemplateFile("/reports/�����L���O_�Z�p.xls");

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 6);
            
            //nhanvt start edit 20141201 New request #33406
            // ���b�Z�[�W
            jx.setValue(1, 14, this.message.getText());

	    int outRow = 10;
	    //nhanvt end edit 20141201 New request #33406
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, tech.getRowCount() - 1);

	    // �f�[�^�Z�b�g
            try {

                NumberFormat nf = NumberFormat.getInstance();
                DefaultTableModel model = (DefaultTableModel)tech.getModel();
                double price = 0;
                double quantity = 0;
                double amount = 0;
                double discount = 0;
                double quantityRate = 0;
                double amountRate = 0;
                for (int row = 0; row < tech.getRowCount(); row++) {
                    for (int col = 0; col < tech.getColumnCount(); col++) {

                        switch (col) {
                            case 3:
                            case 5:
                            case 6:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()));
                                break;
                                
                            case 7:
                            case 8:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()).doubleValue() / 100);
                                break;
                                
                            default:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                                break;
                        }
                        switch (col) {
                            
                            case 3:
                                price += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 4:
                                quantity+= Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 5:
                                amount+= Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 6:
                                discount += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 7:
                                quantityRate +=Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 8:
                                amountRate += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%",""));
                                break;
                        }
                    }
                    outRow++;
                }
                jx.setValue(4,outRow+1,price);
                jx.setValue(5,outRow+1,quantity);
                jx.setValue(6,outRow+1,amount);
                jx.setValue(7,outRow+1,discount);
                jx.setValue(8,outRow+1,Math.round(quantityRate/100));
                jx.setValue(9,outRow+1,Math.round(amountRate/100));
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            jx.removeRow(outRow);
            
	    jx.openWorkbook();
        }

        private void itemOutput() {

            if (item.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_���i");
	    jx.setTemplateFile("/reports/�����L���O_���i.xls");

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 6);
            //nhanvt start edit 20141201 New request #33406
            // ���b�Z�[�W
            jx.setValue(1, 14, this.message.getText());

	    int outRow = 10;
	    //nhanvt end edit 20141201 New request #33406
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, item.getRowCount() - 1);

	    // �f�[�^�Z�b�g
            try {

                NumberFormat nf = NumberFormat.getInstance();
                DefaultTableModel model = (DefaultTableModel)item.getModel();
                double price = 0;
                double quantity = 0;
                double amount = 0;
                double discount = 0;
                double quantityRate = 0;
                double amountRate = 0;
                for (int row = 0; row < item.getRowCount(); row++) {
                    for (int col = 0; col < item.getColumnCount(); col++) {

                        switch (col) {
                            case 3:
                            case 5:
                            case 6:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()));
                                break;
                                
                            case 7:
                            case 8:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()).doubleValue() / 100);
                                break;
                                
                            default:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                                break;
                        }
                        switch (col) {
                            
                            case 3:
                                price += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 4:
                                quantity+= Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 5:
                                amount+= Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 6:
                                discount += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 7:
                                quantityRate +=Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 8:
                                amountRate += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%",""));
                                break;
                        }
                    }
                    outRow++;
                }
                jx.setValue(4,outRow+1,price);
                jx.setValue(5,outRow+1,quantity);
                jx.setValue(6,outRow+1,amount);
                jx.setValue(7,outRow+1,discount);
                jx.setValue(8,outRow+1,Math.round(quantityRate/100));
                jx.setValue(9,outRow+1,Math.round(amountRate/100));
                
            } catch (Exception e) {
            }

            jx.removeRow(outRow);

	    jx.openWorkbook();
        }
        
        private void courseOutput() {

            if (course.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_�R�[�X");
	    jx.setTemplateFile("/reports/�����L���O_�R�[�X.xls");

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 6);
            //nhanvt sartt edit 20141201 New request #33406
            // ���b�Z�[�W
            jx.setValue(1, 14, this.message.getText());

	    int outRow = 10;
	    //nhanvt end edit 20141201 New request #33406
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, course.getRowCount() - 1);

	    // �f�[�^�Z�b�g
            try {

                NumberFormat nf = NumberFormat.getInstance();
                DefaultTableModel model = (DefaultTableModel)course.getModel();
                double price = 0;
                double quantity = 0;
                double amount = 0;
                double discount = 0;
                double quantityRate = 0;
                double amountRate = 0;
                for (int row = 0; row < course.getRowCount(); row++) {
                    for (int col = 0; col < course.getColumnCount(); col++) {

                        switch (col) {
                            case 3:
                            case 5:
                            case 6:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()));
                                break;
                                
                            case 7:
                            case 8:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()).doubleValue() / 100);
                                break;
                                
                            default:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                                break;
                        }
                        switch (col) {
                            
                            case 3:
                                price += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 4:
                                quantity+= Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 5:
                                amount+= Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 6:
                                discount += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 7:
                                quantityRate +=Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%", ""));

                                break;
                            case 8:
                                amountRate += Double.parseDouble(model.getValueAt(row, col).toString().replace(",","").replace("%",""));
                                break;
                        }
                    }
                    outRow++;
                }
                jx.setValue(4,outRow+1,price);
                jx.setValue(5,outRow+1,quantity);
                jx.setValue(6,outRow+1,amount);
                jx.setValue(7,outRow+1,discount);
                jx.setValue(8,outRow+1,Math.round(quantityRate/100));
                jx.setValue(9,outRow+1,Math.round(amountRate/100));
                
            } catch (Exception e) {
            }

            jx.removeRow(outRow);

	    jx.openWorkbook();
        }

        private void decileOutput() {

            if (decile.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_�ڋq");
	    jx.setTemplateFile("/reports/�����L���O_�ڋq.xls");

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 7);
            
            // ���b�Z�[�W
            jx.setValue(1, 13, "��������z�ɂ́A�������܂܂�Ă��܂��B");
            jx.setValue(1, 14, "�������͊܂݂܂���B");

	    // �f�[�^�Z�b�g
            try {

                NumberFormat nf = NumberFormat.getInstance();
                
                // ���v��
                DefaultTableModel totalModel = (DefaultTableModel)decileTotal.getModel();
                //nhanvt start edit 20141218 New request #33406
                jx.setValue(4, 14, nf.parse(totalModel.getValueAt(0, 0).toString()));
                jx.setValue(5, 14, nf.parse(totalModel.getValueAt(0, 1).toString()));
                jx.setValue(6, 14, nf.parse(totalModel.getValueAt(0, 2).toString()));
                jx.setValue(7, 14, nf.parse(totalModel.getValueAt(0, 3).toString()));
                jx.setValue(8, 14, nf.parse(totalModel.getValueAt(0, 4).toString()));
                jx.setValue(9, 14, nf.parse(totalModel.getValueAt(0, 5).toString()));
                int outRow = 10;
                //nhanvt end edit 20141218 New request #33406
                // �ǉ��s���Z�b�g
                jx.insertRow(outRow, decile.getRowCount() - 1);
                
                // ���ח�
                DefaultTableModel model = (DefaultTableModel)decile.getModel();
                for (int row = 0; row < decile.getRowCount(); row++) {
                    for (int col = 0; col < decile.getColumnCount() - 1; col++) {

                        switch (col) {
                            case 1:
                            case 2:
                            case 3:
                            case 6:
                            case 7:
                            case 8:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()));
                                break;
                                
                            case 4:
                            case 5:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : nf.parse(model.getValueAt(row, col).toString()).doubleValue() / 100);
                                break;
                                
                            default:
                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
                                break;
                        }
                    }
                    outRow++;
                }

                jx.removeRow(outRow);
                
            } catch (Exception e) {
            }

	    jx.openWorkbook();
        }
        
        private void individualsalesOutput() {
            
            if (individualsales.getRowCount() == 0) {
                this.showMessageNoData();
                return;
            }
            
	    JExcelApi jx = new JExcelApi("�����L���O_�l����");
	    jx.setTemplateFile("/reports/�����L���O_�l����.xls");

            // �w�b�_�o��
            this.headerOutput(jx, 3, 3, 7);
            
            // ���b�Z�[�W
            jx.setValue(1, 9, this.message.getText());

	    int outRow = 9;
	    
	    // �ǉ��s���Z�b�g
	    jx.insertRow(outRow, individualsales.getRowCount() - 1);
	    
	    // �f�[�^�Z�b�g
            try {

                NumberFormat nf = NumberFormat.getInstance();
                DefaultTableModel model = (DefaultTableModel)individualsales.getModel();
                for (int row = 0; row < individualsales.getRowCount(); row++) {
                    for (int col = 0; col < individualsales.getColumnCount(); col++) {
                        jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
//                        switch (col) {
//                            case 5:
//                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
//                                break;
//
//                            default:
//                                jx.setValue(col + 1, outRow, col < 1 ? row + 1 : model.getValueAt(row, col));
//                                break;
//                        }
                    }
                    outRow++;
                }

            } 
            catch (Exception e) 
            {
            }

            jx.removeRow(outRow);

	    jx.openWorkbook();
            
        }
        
    //LVTu start add 2016/01/19 New request #46728
        public boolean checkCourseFlag() {
            if (target.getSelectedItem() != null) {
                if (target.getSelectedItem() instanceof MstShop) {
                    MstShop shop = new MstShop();
                    shop = (MstShop) target.getSelectedItem();
                    if (shop.getCourseFlag().equals(1)) {
                        return true;
                    }
                }            
                if (target.getSelectedItem() instanceof MstGroup) {
                    MstGroup group = new MstGroup();
                    group = (MstGroup) target.getSelectedItem();

                    if (checkGroupShopCourseFlag(group)) {
                        return true;
                    }
                }
            }
            return false;
        }
            
        private boolean checkGroupShopCourseFlag(MstGroup mg) {
            if(mg.getShops().size()> 0) {
                for (MstShop mshop : mg.getShops()) {
                    if ( mshop.getCourseFlag().equals(1)) {
                        return true;
                    }
                }
            } else if ( mg.getGroups().size() > 0) {
                for ( int i = 0;i < mg.getGroups().size() ;i ++) {
                    return checkGroupShopCourseFlag(mg.getGroups().get(i));
                }
            }
            return false;
        }
        //LVTu end add 2016/01/19 New request #46728
        
}

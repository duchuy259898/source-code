/*
 * CSVImportPanel.java
 *
 * Created on 2006/10/21, 15:14
 */

package com.geobeck.sosia.pos.hair.csv;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.logging.*;
import java.sql.*;

import jp.ac.wakhok.tomoharu.csv.*;
import com.geobeck.sosia.pos.csv.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.swing.filechooser.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.ibm.icu.util.JapaneseCalendar;

/**
 *
 * @author  katagiri
 */
public class CSVImportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    // 2016/09/13 GB MOD Start #54560
    /** �ڋq�}�X�^�p�u�ǉ�����s�v�w�b�_ */
    /* private static final String[] MST_CUSTOMER_FIELDS = 
    {"�ڋqID", "�ڋqNo.", "�X��ID", "����1", "����2", "�ӂ肪��1", "�ӂ肪��2"
            , "���O�C��ID", "�p�X���[�h", "�X�֔ԍ�", "�s���{��", "�s�撬��"
            , "����E�Ԓn", "�}���V��������", "�d�b�ԍ�", "�g�ѓd�b", "FAX�ԍ�"
            , "PC���[��", "�g�у��[��", "����", "���N����", "�E��ID", "���l"
            , "�����O���X��", "SOSIA_ID"}; */
    private static final String[] MST_CUSTOMER_FIELDS = 
    {"�ڋqNo.", "����1", "����2", "�ӂ肪��1", "�ӂ肪��2"
            , "�X�֔ԍ�", "�s���{��", "�s�撬��"
            , "����E�Ԓn", "�}���V��������", "�d�b�ԍ�", "�g�ѓd�b", "FAX�ԍ�"
            , "PC���[��", "�g�у��[��", "����", "���N����", "�E��ID", "���l"
            , "�����O���X��", "SOSIA_ID"};
    /** �t�@�C�����I�����b�Z�[�W */
    private static final String DATA_EMPTY_MSG = 
            "�ǉ�����s�����݂��܂���B�t�@�C����I�����Ă��������B";
    /** �p�^�[���G���[���b�Z�[�W */
    private static final String ERROR_PATTERN_MSG = 
            "�ɂ�{0}�ȊO���͂ł��܂���B";
    /** �ڋqNo�p�^�[�����b�Z�[�W */
    private static final String CUSTOMER_NO_PATTERN_MSG = 
            "���p�p�����܂���-";
    /** ���[���A�h���X�p�^�[�����b�Z�[�W */
    private static final String MAIL_ADDRESS_PATTERN_MSG = 
            "���p�p�����܂���-@._";
    /** �ڋqNo�������݃G���[���b�Z�[�W */
    private static final String ERR_CUSTOMER_NO_EXISTS_TWO_OR_MORE_MSG = 
            "�ڋq�}�X�^���ɓ���̌ڋqNo.�����f�[�^��2���ȏ㑶�݂��邽�ߏ����ł��܂���B";
    /** �X�֔ԍ��`���G���[���b�Z�[�W */
    private static final String ERR_POSTAL_CODE_INVALID_MSG = 
            "�X�֔ԍ��͔��p����7���œ��͂��Ă��������B";
    /** ���ʃG���[���b�Z�[�W */
    private static final String ERR_SEX_INVALID_MSG = 
            "���ʂ�1�i�j���j,2�i�����j�ȊO�͓��͂ł��܂���B";
    /** SOSIA ID�@�͈͖����G���[���b�Z�[�W */
    private static final String ERR_SOSIA_ID_ERROR_MSG = 
            "SOSIA ID��0�`999999�œ��͂��Ă��������B";
    /** ���N�����@�t�H�[�}�b�g�G���[���b�Z�[�W */
    // 2016/10/04 GB reMOD Start #54560
    private static final String ERR_BIRTHDAY_FORMAT_MSG = 
            "���N������YYYY-MM-DD�܂���YYYY/MM/DD�œ��͂��Ă��������B";
    /* private static final String ERR_BIRTHDAY_FORMAT_MSG = 
            "���N������YYYY-MM-DD�œ��͂��Ă��������B"; */
    // 2016/10/04 GB reMOD End #54560
    /** ���N�����@�������t�G���[���b�Z�[�W */
    private static final String ERR_BIRTHDAY_LATER_THEN_SYSDATE_MSG = 
            "���N�����ɖ����̓��t�����͂���Ă��܂��B";    
    /** �ڋq�}�X�^�p���ʕ������`�F�b�N���(0:���ʕ������`�F�b�N�ΏۊO) */
    // 2016/10/11 GB reMOD start #54560
    /* private static final int[] MST_CUSTOMER_FIELDS_MAX_LENGTH = 
    {0, 0, 15, 0, 20, 20, 20, 20, 0, 0, 0, 16, 64, 128, 128, 20, 20, 20, 64, 64, 0, 0, 0, 0, 0, 0}; */
    private static final int[] MST_CUSTOMER_FIELDS_MAX_LENGTH = 
    {0, 15, 20, 20, 20, 20, 0, 16, 64, 128, 128, 20, 20, 20, 64, 64, 0, 0, 0, 0, 0, 0};
    // 2016/10/11 GB reMOD End #54560
    // 2016/09/13 GB MOD End #54560
    // 2016/10/12 GB reMOD Start #54560
    private static final String[] MST_CUSTOMER_FIELDS_COLUMN_TYPE = 
    {"varchar", "varchar", "varchar", "varchar", "varchar", "pbchar" 
        ,"varchar", "varchar", "varchar", "varchar", "varchar", "varchar"
        , "varchar", "varchar", "varchar", "int2", "date", "int4", "text", "int4", "int4"};
    private static final String[] MST_CUSTOMER_FIELDS_COLUMN_NAME = 
    {"customer_no", "customer_name1", "customer_name2", "customer_kana1", "customer_kana2", "postal_code" 
        ,"address1", "address2", "address3", "address4", "phone_number", "cellular_number", "fax_number"
        , "pc_mail_address", "cellular_mail_address", "sex", "birthday", "job_id", "note", "before_visit_num", "sosia_id"};
    // 2016/10/12 GB reMOD End #54560
    String[][] tableList={
        // �e�[�u�����iDB�ł̖��́j�A���{��ł̃e�[�u�����̏��Œ�`���Ă��������B
        // CSVImportFrame���\�����ꂽ���ɁA�R���{�{�b�N�X�ɒǉ����郊�X�g�ł��B
        // ��j{"customerMaster", "�ڋq�}�X�^"}
//        {"mst_group",			"0",	"�O���[�v"},
//        {"mst_shop",			"1",	"�X�܏��"},
//        {"mst_staff_class",		"3",	"�X�^�b�t�敪"},
//        {"mst_staff",			"4",	"�X�^�b�t"},
//        {"mst_bed",				"5",	"�{�p��"},
//        {"mst_item_class",		"10",	"���i���ރ}�X�^"},
//		{"mst_item",			"11",	"���i�}�X�^"},
//        {"mst_technic_class",	"12",	"�Z�p���ރ}�X�^"},
//        {"mst_technic",			"13",	"�Z�p�}�X�^"},
//        {"mst_payment_method",	"20",	"�x�����@"},
//        {"mst_discount",		"21",	"�������"},
//        {"mst_tax",				"22",	"�����"},
//        {"mst_customer",		"-1",	"�ڋq�}�X�^"},
//        {"mst_job",				"30",	"�E�ƃ}�X�^"}
	    
//2008/07/16 �ڋq�ȊO�������Ȃ�����
	        {	"mst_customer",				"-1",	"�ڋq�}�X�^"			}
//		// ��Ѓ}�X�^
//		{	"mst_group",				"1",	"�O���[�v"			},
//		{	"mst_shop",					"2",	"�X�܏��"			},
//		{	"mst_authority",			"3",	"�o�^����"			},
//		{	"mst_staff_class",			"4",	"�X�^�b�t�敪"		},
//		{	"mst_staff",				"5",	"�X�^�b�t"			},
//		{	"mst_bed",					"6",	"�{�p��"			},
//		{	"mst_response",				"7",	"���X�|���X�o�^"	},
//		{	"data_response_issue",		"8",	"���X�|���X���s"	},
//		// ���i�}�X�^
//		{	"mst_item_class",			"10",	"���i����"			},
//		{	"mst_item",					"11",	"���i�o�^"			},
//		{	"mst_supplier",				"13",	"�d�����o�^"		},
//		{	"mst_destocking_division",	"14",	"�݌ɒ����敪�o�^"	},
//		// �Z�p�}�X�^
//		{	"mst_technic_class",		"20",	"�Z�p����"			},
//		{	"mst_technic",				"21",	"�Z�p�o�^"			},
//		{	"mst_proportionally",		"22",	"���o�^"			},
//		// �ڋq�}�X�^
//        {	"mst_customer",				"-1",	"�ڋq�}�X�^"			},
//		{	"mst_job",					"30",	"�E�Ɠo�^"			},
//		{	"mst_free_heading_class",	"31",	"�t���[���ڋ敪"	},
//		{	"mst_free_heading",			"32",	"�t���[����"		},
//		// ���Z�}�X�^
//		{	"mst_tax",					"40",	"�����"			},
//		{	"mst_payment_method",		"41",	"�x�������@"		},
//		{	"mst_discount",				"42",	"�������"			},
//		// ���[���}�X�^
//		{	"mst_mail_signature",		"50",	"����"				},
//		{	"mst_mail_template_class",	"51",	"�e���v���[�g����"	},
//		{	"mst_mail_template",		"52",	"�e���v���[�g"		}
    };
	
	Vector<Boolean>		isStringCol	=	new Vector<Boolean>();
	
	Integer rowCount = null;
	
	/** Creates new form CSVImportPanel */
	public CSVImportPanel()
	{
        initComponents();
		addMouseCursorChange();
		initTableList();
                
                // 2016/09/13 GB MOD Start #54560
		//this.setSize(520, 464);
		this.setSize(833, 691);
                // 2016/09/13 GB MOD End #54560
                
		this.setPath("��{�ݒ�");
		this.setTitle("CSV�C���|�[�g");
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        importButton = new javax.swing.JButton();
        deleteScrollPane = new javax.swing.JScrollPane();
        errorTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        insertScrollPane = new javax.swing.JScrollPane();
        insertTable = new javax.swing.JTable();
        selectCSVButton = new javax.swing.JButton();
        tableNameList = new javax.swing.JComboBox();

        jLabel3.setText("�G���[");

        importButton.setIcon(SystemInfo.getImageIcon("/button/master/import_off.jpg"));
        importButton.setBorderPainted(false);
        importButton.setContentAreaFilled(false);
        importButton.setPressedIcon(SystemInfo.getImageIcon("/button/master/import_on.jpg"));
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        deleteScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        deleteScrollPane.getViewport().setBackground(Color.white);

        errorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "�s�ԍ�", "�G���[���e"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
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
        errorTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        errorTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
        errorTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        errorTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(errorTable, SystemInfo.getTableHeaderRenderer());
        errorTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        errorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        errorTable.getColumnModel().getColumn(0).setResizable(false);
        // 2016/09/09 GB MOD Start #54560
        //errorTable.getColumnModel().getColumn(1).setPreferredWidth(450);
        errorTable.getColumnModel().getColumn(1).setPreferredWidth(757);
        // 2016/09/09 GB MOD End #54560
        deleteScrollPane.setViewportView(errorTable);

        jLabel2.setText("�ǉ�����s");

        jLabel1.setText("�e�[�u����");

        insertScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        insertScrollPane.getViewport().setBackground(Color.white);

        insertTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        insertTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        insertTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
        insertTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        insertTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(insertTable, SystemInfo.getTableHeaderRenderer());
        insertTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        insertTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        insertTable.getColumnModel().getColumn(0).setResizable(false);
        insertScrollPane.setViewportView(insertTable);

        selectCSVButton.setIcon(SystemInfo.getImageIcon("/button/select/select_file_off.jpg"));
        selectCSVButton.setBorderPainted(false);
        selectCSVButton.setContentAreaFilled(false);
        selectCSVButton.setPressedIcon(SystemInfo.getImageIcon("/button/select/select_file_on.jpg"));
        selectCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCSVButtonActionPerformed(evt);
            }
        });

        tableNameList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tableNameList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableNameListActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, insertScrollPane)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, deleteScrollPane)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tableNameList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(selectCSVButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(importButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3))
                        .add(441, 441, 441)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(importButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tableNameList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectCSVButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(insertScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 419, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void tableNameListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_tableNameListActionPerformed
	{//GEN-HEADEREND:event_tableNameListActionPerformed
		this.initInsertTable();
	}//GEN-LAST:event_tableNameListActionPerformed

	private void importButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_importButtonActionPerformed
	{//GEN-HEADEREND:event_importButtonActionPerformed
		this.importCSV();
	}//GEN-LAST:event_importButtonActionPerformed

	private void selectCSVButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectCSVButtonActionPerformed
	{//GEN-HEADEREND:event_selectCSVButtonActionPerformed
		this.openCSVFile();
	}//GEN-LAST:event_selectCSVButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane deleteScrollPane;
    private javax.swing.JTable errorTable;
    private javax.swing.JButton importButton;
    private javax.swing.JScrollPane insertScrollPane;
    private javax.swing.JTable insertTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton selectCSVButton;
    private javax.swing.JComboBox tableNameList;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(selectCSVButton);
		SystemInfo.addMouseCursorChange(importButton);
	}
	
	/**
	 * �e�[�u���̃��X�g������������
	 */
	private void initTableList()
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			for(int i = 0; i < tableList.length; i ++)
			{
				int	aIndex	=	Integer.parseInt(tableList[i][1]);
				
				if(
					( aIndex == -1 )||
					( SystemInfo.getAuthority().getAuthoryty(aIndex) )
				)
				{
					TableInfo	ti	=	new TableInfo(tableList[i][0], tableList[i][2]);
					ti.loadTableInfo(con);

					tableNameList.addItem(ti);
				}
			}
			
			if(0 < tableNameList.getItemCount())
				tableNameList.setSelectedIndex(0);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * CSV�t�@�C�����J��
	 */
	private void openCSVFile()
	{
		JFileChooser	jfc	=	new JFileChooser();
		WildcardFileFilter filter = new WildcardFileFilter("*.csv", "CSV�t�@�C��");
		jfc.setFileFilter(filter);
		
		if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			//�e�[�u�����N���A
			SwingUtil.clearTable(insertTable);
			SwingUtil.clearTable(errorTable);
			
			File	f	=	new File(jfc.getSelectedFile().getAbsolutePath());
			
			//�t�@�C�������݂���ꍇ
			if(f.exists())
			{
				this.loadCSV(jfc.getSelectedFile().getAbsolutePath());
			}
			else
			{
				MessageDialog.showMessageDialog(this,
						MessageUtil.getMessage(1200,
						f.getAbsolutePath()),
						this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * CSV�t�@�C����ǂݍ���
	 */
	private void loadCSV(String filePath)
	{
		BufferedReader br	=	null;

		try
		{
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), "Shift_JIS"));
		}
		catch (UnsupportedEncodingException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (FileNotFoundException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		if(br != null)
		{
			try
			{
				boolean		isInit	=	false;
				String		line	=	null;
                                Integer		row		=	1;
                                DefaultTableModel	insModel	=	(DefaultTableModel)insertTable.getModel();
				
				// 2016/10/11 GB reMOD Start #54560
                                // CSV�w�b�_��ǂ�ł���
                                br.readLine();
                                // 2016/10/11 GB reMOD end #54560
                                
                                //�P�s���ǂݍ���
				while((line = br.readLine()) != null)
				{
					// 1�s�ǂݍ��ޓx�ɁAInsertTable�̍s��ǉ����ĉ������B
					CSVTokenizer	csvt	=	new CSVTokenizer(line);
					Vector<Object>	rowData	=	new Vector<Object>();
					
					rowData.add(row);
					
					while (csvt.hasMoreElements())
					{
						Object	data	=	csvt.nextToken();
						// �e�g�[�N�����Ƃ�JTable�̃Z���ɒǉ�
						rowData.add(data);
					}
					
					//�e�[�u���Ƀf�[�^��ǉ�
					insModel.addRow(rowData);
					
					row	++;
				}
			}
			catch (IOException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * �C���|�[�g�f�[�^�̃e�[�u����������
	 */
	private void initInsertTable()
	{
		//�I������Ă���e�[�u���̏����擾
		TableInfo	ti	=	(TableInfo)tableNameList.getSelectedItem();
		
		DefaultTableModel	model	=	(DefaultTableModel)insertTable.getModel();
		
		//�w�b�_�̕�������Z�b�g
		Vector<Object>		colHeader	=	new Vector<Object>();
		colHeader.add("�s�ԍ�");
		
		if(ti.getTableName().equals("mst_customer")){
                    // 2016/10/11 GB MOD Start #54560
                    //rowCount = 25;
                    rowCount = 21;
                    // 2016/10/11 GB MOD End #54560
                }
		
//		for(int i = 0; i < ti.size() - 3; i ++)
		for(int i = 0; i < rowCount; i ++)
		{
                    // 2016/09/13 GB MOD Start #54560
                    //colHeader.add(ti.get(i));
                    colHeader.add(MST_CUSTOMER_FIELDS[i]);
                    // 2016/09/13 GB MOD End #54560
		}
		
        insertTable.setModel(new javax.swing.table.DefaultTableModel(
            new Vector(),
            colHeader
        )
        {
            public Class getColumnClass(int columnIndex)
            {
				//�s�ԍ��ȊO��String�ɐݒ�
				if(columnIndex == 0)
					return	java.lang.Integer.class;
				else
                                        return java.lang.String.class;
            }
			
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
				//�s�ԍ��ȊO�͕ҏW��
                return (columnIndex != 0);
            }
         });
        	
		SwingUtil.setJTableHeaderRenderer(insertTable, SystemInfo.getTableHeaderRenderer());
	}
	
	/**
	 * CSV�t�@�C������f�[�^����荞��
	 */
	private void importCSV()
	{
		try
		{
			SwingUtil.clearTable(errorTable);
			
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			TableInfo	ti	=	(TableInfo)tableNameList.getSelectedItem();
			
			boolean[]	isImported	=	new boolean[insertTable.getRowCount()];
			//2016/09/13 GB MOD Start
                        // �쐬�s���Ȃ��ꍇ�̓��b�Z�[�W���o���A�I��
                        if (insertTable.getRowCount() == 0) {
                            MessageDialog.showMessageDialog(this,
                                            DATA_EMPTY_MSG,
                                            this.getTitle(),
                                            JOptionPane.INFORMATION_MESSAGE);
                                            return;
                        }
                        //2016/09/13 GB MOD End
			for(int i = 0; i < insertTable.getRowCount(); i ++)
			{
			//2016/09/13 GB MOD Start
                            boolean chkFlg = true;
                            // �s���ƂɊe���ڂ̓��e�`�F�b�N
                            //2016/10/11 GB reMOD Start #54560
                            for(int j = 1; j < rowCount + 1; j ++){
                            //for(int j = 2; j < rowCount + 1; j ++){
                            //2016/10/11 GB reMOD End #54560
                                if (!checkRowErr(i, j)) {
                                    chkFlg = false;
                                }
                            }
                            // �`�F�b�N�ŃG���[���������ꍇ�͎��̍s��
                            if (!chkFlg) {
                                isImported[i]	=	false;
                                continue;
                            }
			//2016/09/13 GB MOD End
                            
                            
				String sql = makeInsertSQL(ti,i);
				

				
				con.begin();
				
				//�s���ƂɃe�[�u���ɒǉ�
				try
				{
					isImported[i]	=	(con.executeUpdate(sql) == 1);
				}
				catch(SQLException e)
				{
					isImported[i]	=	false;
					addErrorRow((Integer)insertTable.getValueAt(i, 0), e);
				}
				
				//�C���|�[�g�ł����ꍇ
				if(isImported[i])
				{
					con.commit();
                                        
				}
				//�C���|�[�g�ł��Ȃ������ꍇ
				else
				{
					con.rollback();
				}
			}
			
			DefaultTableModel	model	=	(DefaultTableModel)insertTable.getModel();
			
			for(int row = insertTable.getRowCount() - 1; row >= 0; row --)
			{
				if(isImported[row])
				{
					model.removeRow(row);

				}
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
					tableNameList.getSelectedItem().toString()),
					this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
		}
		
		//�S�s�o�^�ł����ꍇ
		if(errorTable.getRowCount() == 0)
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
		}
		//�o�^�ł��Ȃ��s���������ꍇ
		else
		{
                        MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(7100,
						Integer.toString(insertTable.getRowCount())),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private String makeInsertSQL(TableInfo ti, int i){

		String sql = "";
		
		// 2016/10/11 GB reMOD Start #54560
                //String customerNo = insertTable.getValueAt(i, 2).toString();
                String customerNo = insertTable.getValueAt(i, 1).toString();
                // 2016/10/11 GB reMOD End #54560
		
		int count = getCustomerCount(customerNo);
		
		if (count == 0) {

			/* INSERT�� */
			
			String insertString = "";

			for(int j = 1; j < rowCount+1; j ++){			

				insertString += "," + getColumnValue(ti, i, j, true);
			}

			insertString += ", current_timestamp, current_timestamp, null";

			if(ti.getTableName().equals("mst_customer")){
				insertString += ",null,''";
			}

			sql = "insert into " + ti.getTableName() + " values (";
			sql += insertString.substring(1);
			sql += ")";

		} else if (count == 1) {

			/* UPDATE�� */

			String updateString = "";
			
			// 2016/10/11 GB reMOD Start #54560
                        for(int j = 2; j < rowCount+1; j ++){
                        //for(int j = 3; j < rowCount+1; j ++){
                        
                            // 2016/09/13 GB MOD Start #54560
                            // ���O�C��ID�A�p�X���[�h�͍X�V���Ȃ�
                            /* if (j == 8 || j == 9 ) {
                                continue;
                            } */
                            // 2016/09/13 GB MOD End #54560
                        // 2016/10/11 GB reMOD End #54560
                            String updateColumnValue = getColumnValue(ti, i, j, false);

                            // 2016/10/12 GB reMOD Start #54560
                            updateString += ",";
                            updateString += MST_CUSTOMER_FIELDS_COLUMN_NAME[j-1] + " = " + updateColumnValue;
                            /* if (updateColumnValue.replace("'","").length() > 0) {
                                    updateString += ",";
                                    updateString += MST_CUSTOMER_FIELDS_COLUMN_NAME[j-1] + " = " + updateColumnValue;
                            } */
                            // 2016/10/12 GB reMOD End #54560
			}
                        
                        // 2016/09/13 GB MOD Start #54560
                        // �X�V�����̍X�V����ǉ�
                        updateString += ",";
                        updateString += "update_date = current_timestamp";                        
                        // 2016/09/13 GB MOD End #54560

			sql = "update " + ti.getTableName() + " set ";
			sql += updateString.substring(1);
			sql += " where customer_no = '" + customerNo + "'";
                // 2016/09/13 GB MOD Start #54560
                // ����ڋqNo���������݂���ꍇ�̓`�F�b�N�ŃG���[�ɂ��邽�߂����ɂ͓���Ȃ�
//		} else {
//			
//			// ����ڋqNo���������݂����ꍇ�͂Q�d�o�^�G���[�𔭐�������
//			sql = "insert into " + ti.getTableName();
//			sql += " select * from " + ti.getTableName();
//			sql += " where";
//			sql += "     customer_id = ";
//			sql += "         (";
//			sql += "             select max(customer_id)";
//			sql += "               from " + ti.getTableName();
//			sql += "              where customer_no = '" + customerNo + "'";
//			sql += "         )";
                // 2016/09/13 GB MOD End #54560
		}
		
		return sql;
	}
	
	/**
	 * �G���[�s��ǉ�����B
	 */
	private void addErrorRow(Integer row, Exception e)
	{
		DefaultTableModel	model	=	(DefaultTableModel)errorTable.getModel();
		
		Object[]	errorRow	=	{ row, e.getLocalizedMessage()	};
		
		model.addRow(errorRow);
	}

	/**
	 * ����ڋqNo�̌������擾
	 */
	private int getCustomerCount(String customerNo)
	{
		int result = 0;

		try {
			String sql = "select count(*) as cnt from mst_customer where customer_no = '" + customerNo + "'";

			ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql);

			while(rs.next()) {
				result = rs.getInt("cnt");
			}
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * �X�VSQL���������ɃJ�����^�C�v�ɉ������l���擾
	 */
	// 2016/10/12 GB reMOD Start #54560
        //private String getColumnValue(TableInfo ti, int i, int j) {
        private String getColumnValue(TableInfo ti, int i, int j, boolean isInsert) {
        // 2016/10/12 GB reMOD End #54560

		String result = "";

		if(j == 1 && ti.getTableName().equals("mst_customer")) {
// 2016/09/13 GB MOD Start #54560
//			result = "(select max(customer_id) + 1 as customer_id from " + ti.getTableName() + ")";
			//result = "(select coalesce(max(customer_id), 0) + 1 as customer_id from " + ti.getTableName() + ")";
                        // 2016/10/11 GB reMOD Start #54560
                        if(isInsert){
                            //�ڋqID
                            result += "(select coalesce(max(customer_id), 0) + 1 as customer_id from " + ti.getTableName() + ")";
                            //�ڋqNo
                            result +=  "," + SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());
                            //�V���b�vID(�ڋq���L�̂Ƃ��̃V���b�vID��0)
                            result += ", 0";
                        }
                        // 2016/10/11 GB reMOD End #54560
// 2016/09/13 GB MOD End #54560

                // 2016/10/11 GB reMOD Start #54560		
                //}
                //else if(j == 3 && ti.getTableName().equals("mst_customer") && SystemInfo.getSetteing().isShareCustomer()) {
                    //result = "0";	//�ڋq���L�̂Ƃ��̃V���b�vID��0
                // 2016/10/11 GB reMOD End #54560
// 2016/09/13 GB MOD Start #54560
                
                // 2016/10/11 GB reMOD Start #54560
                //} else if(j == 8 && ti.getTableName().equals("mst_customer")) {

			//result = "(select make_login_id())";	//���O�C��ID����
                //} else if(j == 9 && ti.getTableName().equals("mst_customer")) {

			//result = "(select make_password(8))";	//�p�X���[�h����

                        
                } else if(j == 5 && ti.getTableName().equals("mst_customer")) {

                    if(isInsert){
                        //�ӂ肪�ȂQ
                        result +=  SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());
                        //���O�C��ID
                        result += ", (select make_login_id())";
                        //�p�X���[�h
                        result += ", (select make_password(8))";	//�p�X���[�h����
                    }else{
                        //�X�V
                        //�ӂ肪�ȂQ
                        result +=  SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());
                    }
                        
                } else if(j == 16 && ti.getTableName().equals("mst_customer")) {
                //} else if(j == 20 && ti.getTableName().equals("mst_customer")) {
                // 2016/10/11 GB reMOD End #54560

                        result = (insertTable.getValueAt(i, j).toString().equals("") ? "2" 
                            :SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString()));	//���ʂ͋󔒂̏ꍇ2

// 2016/09/13 GB MOD End #54560
		} else {

                    // 2016/10/11 GB reMOD Start #54560
                    String colType = MST_CUSTOMER_FIELDS_COLUMN_TYPE[j-1];
                    //String colType = ti.get(j - 1).getColumnType();
                    // 2016/10/11 GB reMOD End #54560
                    // 2016/09/13 GB MOD End #54560
//			boolean isTypeText = colType.equals("char") || colType.equals("varchar") || colType.equals("text");
                    boolean isTypeText = colType.equals("char") || colType.equals("varchar") || colType.equals("text") || colType.equals("bpchar");
                    // 2016/09/13 GB MOD End #54560
                    boolean isTypeDate = colType.equals("date") || colType.equals("time") || colType.equals("datetime");

                    if (isTypeText) {

                            result = SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());

                    } else if (isTypeDate) {

                            result = (insertTable.getValueAt(i, j).toString().equals("") ? "null" 
                                      :SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString()));

                    } else {
                            result = (insertTable.getValueAt(i, j).toString().equals("") ? "null" 
                                      :insertTable.getValueAt(i, j).toString());
                    }
		}

		return result;
	}
    // 2016/09/13 GB MOD Start #54560
    /**
     * �G���[�`�F�b�N���{
     * @param row �`�F�b�N�Ώۂ̍s�ԍ�
     * @param col �`�F�b�N�Ώۂ̗�ԍ�
     * @return �`�F�b�N����
     */
    private boolean checkRowErr(Integer row, Integer col)  throws SQLException {
        String val = insertTable.getValueAt(row, col).toString();
        String colName = insertTable.getColumnModel().getColumn(col).getIdentifier().toString();
        Integer errRowNo = Integer.parseInt(insertTable.getValueAt(row, 0).toString());
        Integer maxLength = MST_CUSTOMER_FIELDS_MAX_LENGTH[col];
        
        boolean result = true;

        //���ړƎ��̃`�F�b�N
        switch (col) {
            // 2016/10/11 GB reMOD Start #54560
            case 1:
            //case 2:
            // 2016/10/11 GB reMOD End #54560
            // �ڋqNo�F�K�{�A���p�p�����A�n�C�t���̂݁A�ڋq�}�X�^���ɓ���ڋqNo.��2���ȏ㑶�݂���ꍇNG
            if (val == null || val.equals("")) {
                addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, colName));
                result = false;
            } else if (!checkPatternMatch(val, CustomFilter.ALPHAMERIC)){
                addCheckErrorRow(errRowNo, colName + ERROR_PATTERN_MSG.replaceAll("\\{0\\}", CUSTOMER_NO_PATTERN_MSG));
                result = false;
            } else if (getCustomerCount(val) > 1) {
                addCheckErrorRow(errRowNo, ERR_CUSTOMER_NO_EXISTS_TWO_OR_MORE_MSG);
                result = false;
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 2:
            //case 4:
            // 2016/10/11 GB reMOD End #54560
            // ����1�F�󔒂̏ꍇ�A����2���󔒂Ȃ�NG
            if (val == null || val.equals("")) {
                // 2016/10/11 GB reMOD Start #54560
                String val2 = insertTable.getValueAt(row, 3).toString();
                //String val2 = insertTable.getValueAt(row, 5).toString();
                // 2016/10/11 GB reMOD End #54560
                if (val2 == null || val2.equals("")) {
                    addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "����1,����2"));
                    result = false;
                }
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 6:
            //case 10:
            // 2016/10/11 GB reMOD End #54560
            // �X�֔ԍ�:�󔒂܂��͔��p����7���łȂ����NG
            if (val != null && !val.equals("")) {
                if (!CheckUtil.isNumber(val) || val.length() != 7) {
                    addCheckErrorRow(errRowNo, ERR_POSTAL_CODE_INVALID_MSG);
                    result = false;
                }
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 11:
            case 12:
            case 13:
            //case 15:
            //case 16:
            //case 17:
            // 2016/10/11 GB reMOD End #54560
            // �d�b�ԍ��A�g�ѓd�b�AFAX�ԍ�:���p�����łȂ����NG
            // 2016/10/04 GB reMOD Start #54560
            if(val != null && !val.equals("")){
                if (!CheckUtil.isNumber(val)) {
                    addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_NOT_NUMERIC, colName));
                    result = false;
                }
            }
            break;
            /* if (!CheckUtil.isNumber(val)) {
                addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_NOT_NUMERIC, colName));
                result = false;
            } */
            // 2016/10/04 GB reMOD End #54560
            // 2016/10/11 GB reMOD Start #54560
            case 14:
            case 15:
            //case 18:
            //case 19:
            // 2016/10/11 GB reMOD End #54560
            // PC���[���A�h���X�A�g�у��[���A�h���X:���p�p�����܂���-\@._�łȂ����NG
            if (!checkPatternMatch(val, CustomFilter.MAIL_ADDRESS)) {
                addCheckErrorRow(errRowNo, colName + ERROR_PATTERN_MSG.replaceAll("\\{0\\}", MAIL_ADDRESS_PATTERN_MSG));
                result = false;
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 16:
            //case 20:
            // 2016/10/11 GB reMOD End #54560
            // ����:��,"1","2"�ȊO��NG
            if (val != null && !val.equals("") && !val.equals("1") && !val.equals("2")) {
                addCheckErrorRow(errRowNo, ERR_SEX_INVALID_MSG);
                result = false;
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 17:
            //case 21:
            // 2016/10/11 GB reMOD end #54560
            // ���N����:���t�ȊO�A��������NG
            if (val != null && !val.equals("")) {
                // 2016/10/04 GB reMOD Start #54560
                // yyyy/mm/dd ���o�^�ł���悤��
                if ( (!val.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")  || 
                        !CheckUtil.isDateNonSlash(val.replaceAll("-", "")))
                    && (!val.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")  || 
                        !CheckUtil.isDateNonSlash(val.replaceAll("/", ""))) ) {
                /* if (!val.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")  || 
                !CheckUtil.isDateNonSlash(val.replaceAll("-", ""))) { */
                // 2016/10/04 GB reMOD End #54560
                    addCheckErrorRow(errRowNo, ERR_BIRTHDAY_FORMAT_MSG);
                    result = false;
                } else {
                    JapaneseCalendar temp = new JapaneseCalendar();
                    // 2016/10/04 GB reMOD Start #54560
                    // yyyy/mm/dd ���o�^�ł���悤��
                    String splitType = val.substring(4, 5);
                    String[] birthday = val.split(splitType);
                    // 2016/10/04 GB reMOD Start #54560
                    temp.set(JapaneseCalendar.ERA, JapaneseCalendar.AD);
                    temp.set(JapaneseCalendar.EXTENDED_YEAR, Integer.parseInt(birthday[0]));
                    temp.set(JapaneseCalendar.MONTH, Integer.parseInt(birthday[1]) - 1);
                    temp.set(JapaneseCalendar.DAY_OF_MONTH, Integer.parseInt(birthday[2]));
                    
                    if ((new java.util.GregorianCalendar()).getTimeInMillis()
                            < temp.getTimeInMillis()) {
                        addCheckErrorRow(errRowNo, ERR_BIRTHDAY_LATER_THEN_SYSDATE_MSG);
                        result = false;
                    }
                }
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 18:
            //case 22:
            // 2016/10/11 GB reMOD End #54560
            // �E��ID:���p�����̂݁A�E�ƃ}�X�^�ɑ��݂��Ȃ����NG
            // 2016/10/04 GB reMOD Start #54560
            if(val != null && !val.equals("")){
                if (!CheckUtil.isNumber(val)) {
                    addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_NOT_NUMERIC, colName));
                    result = false;
                } else if (!isExistsMstJob(val)) {
                    addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_INPUT_NOT_EXIST, colName));
                    result = false;
                }          
            }
            /* if (!CheckUtil.isNumber(val)) {
                addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_NOT_NUMERIC, colName));
                result = false;
            } else if (!isExistsMstJob(val)) {
                addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_INPUT_NOT_EXIST, colName));
                result = false;
            } */    
            // 2016/10/04 GB reMOD End #54560
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 20:
            //case 24:
            // 2016/10/11 GB reMOD End #54560
                //�����O���X��:Integer�̂�
                if (val != null && !val.equals("")) { 
                    try {
                        Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_NOT_NUMERIC, colName));
                        result = false;
                    }
                } 
              
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 21:
            //case 25:
            // 2016/10/11 GB reMOD End #54560
                //SOSIA ID:���l�̂�,0�`999999
                if (val != null && !val.equals("")) { 
                    if (!CheckUtil.isNumber(val)) {
                        addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_NOT_NUMERIC, colName));
                    } else {
                        if(val.length() > 6){
                            addCheckErrorRow(errRowNo, ERR_SOSIA_ID_ERROR_MSG);
                            result = false;
                        }else{
                            Integer intVal = Integer.parseInt(val);
                            if (intVal < 0) {
                                addCheckErrorRow(errRowNo, ERR_SOSIA_ID_ERROR_MSG);
                                result = false;
                            } else if (intVal > 999999) {
                                addCheckErrorRow(errRowNo, MessageUtil.getMessage(1105, colName, "999999"));
                                result = false;
                            }
                        }
                    }
                } 
            break;
            // 2016/10/04 GB reMOD Start #54560
            /* default:
                return true; */
            // 2016/10/04 GB reMOD End #54560
        }
        
        
        
        // ����������`�F�b�N
        if (!checkLength(val, maxLength)) {
            addCheckErrorRow(errRowNo, MessageUtil.getMessage(1201, colName, maxLength.toString() + "����"));
            result = false;
        }
        
        return result;
    }
        
    /**
     * ����������`�F�b�N�����{����B
     * @param val �Ώە�����
     * @param maxlength ���������(0�̏ꍇ�̓`�F�b�N���Ȃ�)
     * @return �`�F�b�N����
     */
    private boolean checkLength(String val, Integer maxlength)
    {
        // ����������̃`�F�b�N���s��Ȃ��ꍇ�A�Ώە������󔒂̏ꍇ�̓`�F�b�N���Ȃ�
        if (maxlength == 0 || val == null || val.equals("")) {
            return true;
        } else {
            return val.length() <= maxlength;
        }
    }

    /**
     * �p�^�[���`�F�b�N�����{����B
     * @param val �Ώە�����
     * @param pattern �g�p�\������̃p�^�[��
     * @return �`�F�b�N����
     */
    private boolean checkPatternMatch(String val, String pattern) {
        return val.matches("[" + pattern + "]*");
    }    
    /**
     * �E�ƃ}�X�^�̑��݃`�F�b�N
     * @param jobId �`�F�b�N�Ώۂ̐E��ID(�󔒂̏ꍇ�̓`�F�b�N���Ȃ�)
     * @return �`�F�b�N����(true:�`�F�b�N���Ȃ� or ���݂���)
     * @throws java.sql.SQLException
     */
    public boolean isExistsMstJob(String jobId) throws SQLException {
        ConnectionWrapper con =	SystemInfo.getConnection();
        if (jobId == null || jobId.equals("")) {
            return true;
        }

        if (con == null) {
            return false;
        }

        try {
            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(jobId));
            return rs.next();
        } catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * �E�ƃ}�X�^���݃`�F�b�N�pSelect�����擾����B
     *
     * @return Select��
     */
    private String getSelectSQL(String jobId) {
            return "select *\n"
                    + "from mst_job \n"
                    + "where	job_id = " + SQLUtil.convertForSQL(jobId) + "\n"
                    + "and	delete_date is null\n";

    }    
    /**
     * �`�F�b�N�G���[���ɃG���[�s��ǉ�����B
     * @param row �G���[�̍s�ԍ�
     * @param msg �G���[�̓��e
     */
    private void addCheckErrorRow(Integer row, String msg)
    {
            DefaultTableModel	model	=	(DefaultTableModel)errorTable.getModel();
            
            Object[]	errorRow	=	{row, msg};

            model.addRow(errorRow);
    }
        
    // 2016/09/13 GB MOD End #54560
}

/*
 * CSVExportPanel.java
 *
 * Created on 2007/09/13, 19:32
 */

package com.geobeck.sosia.pos.hair.csv;

import com.geobeck.sosia.pos.csv.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.swing.filechooser.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;
import jp.ac.wakhok.tomoharu.csv.*;

/**
 *
 * @author  kanemoto
 */
public class CSVExportPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	
    String[][] tableList={
        // �e�[�u�����iDB�ł̖��́j�A���{��ł̃e�[�u�����̏��Œ�`���Ă��������B
        // CSVImportFrame���\�����ꂽ���ɁA�R���{�{�b�N�X�ɒǉ����郊�X�g�ł��B
        // ��j{"customerMaster", "�ڋq�}�X�^"}
		// ��Ѓ}�X�^
		{	"mst_group",				"1",	"�O���[�v"			},
		{	"mst_shop",					"2",	"�X�܏��"			},
		{	"mst_authority",			"3",	"�o�^����"			},
		{	"mst_staff_class",			"4",	"�X�^�b�t�敪"		},
		{	"mst_staff",				"5",	"�X�^�b�t"			},
		{	"mst_bed",					"6",	"�{�p��"			},
		{	"mst_response",				"7",	"���X�|���X�o�^"	},
		{	"data_response_issue",		"8",	"���X�|���X���s"	},
		// ���i�}�X�^
		{	"mst_item_class",			"10",	"���i����"			},
		{	"mst_item",					"11",	"���i�o�^"			},
		{	"mst_supplier",				"13",	"�d�����o�^"		},
		{	"mst_destocking_division",	"14",	"�݌ɒ����敪�o�^"	},
		// �Z�p�}�X�^
		{	"mst_technic_class",		"20",	"�Z�p����"			},
		{	"mst_technic",				"21",	"�Z�p�o�^"			},
		{	"mst_proportionally",		"22",	"���o�^"			},
		// �ڋq�}�X�^
        {	"mst_customer",				"-1",	"�ڋq�}�X�^"			},
		{	"mst_job",					"30",	"�E�Ɠo�^"			},
		{	"mst_free_heading_class",	"31",	"�t���[���ڋ敪"	},
		{	"mst_free_heading",			"32",	"�t���[����"		},
		// ���Z�}�X�^
		{	"mst_tax",					"40",	"�����"			},
		{	"mst_payment_method",		"41",	"�x�������@"		},
		{	"mst_discount",				"42",	"�������"			},
		// ���[���}�X�^
		{	"mst_mail_signature",		"50",	"����"				},
		{	"mst_mail_template_class",	"51",	"�e���v���[�g����"	},
		{	"mst_mail_template",		"52",	"�e���v���[�g"		}
    };
	
	Vector<Boolean>		isStringCol	=	new Vector<Boolean>();
	
	/** Creates new form CSVExportPanel */
	public CSVExportPanel() {
		initComponents();
		addMouseCursorChange();
		initTableList();
		this.setSize(800, 464);
		this.setPath("��{�ݒ�");
		this.setTitle("CSV�G�N�X�|�[�g");
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        exportButton = new javax.swing.JButton();
        tableNameList = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        exportScrollPane = new javax.swing.JScrollPane();
        exportTable = new javax.swing.JTable();

        exportButton.setIcon(SystemInfo.getImageIcon("/button/master/export_off.jpg"));
        exportButton.setBorderPainted(false);
        exportButton.setContentAreaFilled(false);
        exportButton.setPressedIcon(SystemInfo.getImageIcon("/button/master/export_on.jpg"));
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        tableNameList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tableNameList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableNameListActionPerformed(evt);
            }
        });

        jLabel1.setText("\u30c6\u30fc\u30d6\u30eb\u540d");

        jLabel2.setText("\u51fa\u529b\u3059\u308b\u884c");

        exportScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        exportScrollPane.getViewport().setBackground(Color.white);
        exportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        exportTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        exportTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
        exportTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        exportTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(exportTable, SystemInfo.getTableHeaderRenderer());
        exportTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        exportTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        exportTable.getColumnModel().getColumn(0).setResizable(false);

        exportScrollPane.setViewportView(exportTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exportScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableNameList, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
                        .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tableNameList, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * �e�[�u�����X�g�I��ύX���̏���
	 */
	private void tableNameListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableNameListActionPerformed
		this.drawTable();
	}//GEN-LAST:event_tableNameListActionPerformed

	private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
		this.exportCSV();
	}//GEN-LAST:event_exportButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exportButton;
    private javax.swing.JScrollPane exportScrollPane;
    private javax.swing.JTable exportTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox tableNameList;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(exportButton);
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
	 * �\���e�[�u���ύX���̏���
	 */
	private void drawTable()
	{
		// �e�[�u���w�b�_�̎擾
		this.initExportTableHeader();
		// �e�[�u���f�[�^�̕\��
		drawExportTableRecord();
	}
	
	/**
	 * �A�E�g�|�[�g�f�[�^�̃e�[�u���w�b�_��������
	 */
	private void initExportTableHeader()
	{
		//�I������Ă���e�[�u���̏����擾
		TableInfo	ti	=	(TableInfo)tableNameList.getSelectedItem();
		
		DefaultTableModel	model	=	(DefaultTableModel)exportTable.getModel();
		
		//�w�b�_�̕�������Z�b�g
		Vector<Object>		colHeader	=	new Vector<Object>();
		colHeader.add("�s�ԍ�");
		for(int i = 0; i < ti.size(); i ++)
		{
			colHeader.add(ti.get(i));
		}
		
        exportTable.setModel(new javax.swing.table.DefaultTableModel(
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
				//�S�s�ҏW�s��
                return false;
            }
        });
		
		SwingUtil.setJTableHeaderRenderer(exportTable, SystemInfo.getTableHeaderRenderer());
	}
	
	/**
	 * �I�𒆂̃e�[�u�����R�[�h���e��\������
	 */
	private void drawExportTableRecord()
	{
		int       lineNo;
		TableInfo ti = (TableInfo)tableNameList.getSelectedItem();
		
		SwingUtil.clearTable(exportTable);
		DefaultTableModel	model	=	(DefaultTableModel)exportTable.getModel();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			ResultSetWrapper	rs	=	con.executeQuery(this.getExportTableSelectSQL());
			
			lineNo = 1;
			while(rs.next())
			{
				Object[]	rowData = new Object[ ti.size() + 1 ];
				rowData[ 0 ] = lineNo++;
				for( int i = 0; i < ti.size(); i++ )
				{
					rowData[ i + 1 ] = rs.getString( i + 1 );
				}
				model.addRow( rowData );
			}
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �I���e�[�u���̃��R�[�h�擾�pSQL���擾����
	 */
	private String getExportTableSelectSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			( (TableInfo)tableNameList.getSelectedItem() ).getTableName() + "\n" +
			";\n";
	}
	
	/**
	 * �\���e�[�u�����e��CSV�o�͂���
	 */
	private void exportCSV()
	{
		String				fileName;
		fileName = getSaveCSVFileName();
		if( fileName != null )
		{
			saveCSVFile( fileName );
		}
	}
	
	/**
	 * CSV�t�@�C�����J��
	 */
	private String getSaveCSVFileName()
	{
		JFileChooser		jfc		=	new JFileChooser();
		WildcardFileFilter filter	=	new WildcardFileFilter("*.csv", "CSV�t�@�C��");
		
		File file = new File( new File( ( (TableInfo)tableNameList.getSelectedItem() ).getTableName() + "_" + String.format( "%1$tY%1$tm%1$td%2$ts", new GregorianCalendar(), new java.util.Date() ) + ".csv" ).getAbsolutePath());
		jfc.setSelectedFile( file );
		jfc.setFileFilter(filter);
		
		if( jfc.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
		{
			return jfc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
	/**
	 * CSV�t�@�C���o�͂��s��
	 */
	private boolean saveCSVFile( String fileName )
	{
		boolean			compliteFlg = true;
		TableInfo		ti = (TableInfo)tableNameList.getSelectedItem();
		BufferedWriter	bw	=	null;
		try {
			bw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( fileName ), "Shift_JIS" ) );
			
			String line;
			for( int y = 0; y < exportTable.getRowCount(); y++ )
			{
				line = "";
				for( int x = 1; x < exportTable.getColumnCount(); x++ )
				{
					line +=
						( exportTable.getValueAt( y, x ) == null ? "" : ( "\"" + (String)exportTable.getValueAt( y, x ) + "\"" ) ) +
						( ( x == exportTable.getColumnCount() - 1 ) ? "" : "," )
						;
				}
				bw.write( line );
				bw.newLine();
			}
			
		}
		catch (FileNotFoundException        e) { SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e); compliteFlg = false; }
		catch (IOException					e) { SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e); compliteFlg = false; }
		finally {
			try {
				if( bw != null ) {
					bw.flush();
					bw.close();
				}
			} catch( Exception e ){}
		}
		return compliteFlg;
	}
	
}
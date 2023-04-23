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
    /** 顧客マスタ用「追加する行」ヘッダ */
    /* private static final String[] MST_CUSTOMER_FIELDS = 
    {"顧客ID", "顧客No.", "店舗ID", "氏名1", "氏名2", "ふりがな1", "ふりがな2"
            , "ログインID", "パスワード", "郵便番号", "都道府県", "市区町村"
            , "町域・番地", "マンション名等", "電話番号", "携帯電話", "FAX番号"
            , "PCメール", "携帯メール", "性別", "生年月日", "職業ID", "備考"
            , "導入前来店回数", "SOSIA_ID"}; */
    private static final String[] MST_CUSTOMER_FIELDS = 
    {"顧客No.", "氏名1", "氏名2", "ふりがな1", "ふりがな2"
            , "郵便番号", "都道府県", "市区町村"
            , "町域・番地", "マンション名等", "電話番号", "携帯電話", "FAX番号"
            , "PCメール", "携帯メール", "性別", "生年月日", "職業ID", "備考"
            , "導入前来店回数", "SOSIA_ID"};
    /** ファイル未選択メッセージ */
    private static final String DATA_EMPTY_MSG = 
            "追加する行が存在しません。ファイルを選択してください。";
    /** パターンエラーメッセージ */
    private static final String ERROR_PATTERN_MSG = 
            "には{0}以外入力できません。";
    /** 顧客Noパターンメッセージ */
    private static final String CUSTOMER_NO_PATTERN_MSG = 
            "半角英数字または-";
    /** メールアドレスパターンメッセージ */
    private static final String MAIL_ADDRESS_PATTERN_MSG = 
            "半角英数字または-@._";
    /** 顧客No複数存在エラーメッセージ */
    private static final String ERR_CUSTOMER_NO_EXISTS_TWO_OR_MORE_MSG = 
            "顧客マスタ内に同一の顧客No.を持つデータが2件以上存在するため処理できません。";
    /** 郵便番号形式エラーメッセージ */
    private static final String ERR_POSTAL_CODE_INVALID_MSG = 
            "郵便番号は半角数字7桁で入力してください。";
    /** 性別エラーメッセージ */
    private static final String ERR_SEX_INVALID_MSG = 
            "性別は1（男性）,2（女性）以外は入力できません。";
    /** SOSIA ID　範囲未満エラーメッセージ */
    private static final String ERR_SOSIA_ID_ERROR_MSG = 
            "SOSIA IDは0～999999で入力してください。";
    /** 生年月日　フォーマットエラーメッセージ */
    // 2016/10/04 GB reMOD Start #54560
    private static final String ERR_BIRTHDAY_FORMAT_MSG = 
            "生年月日はYYYY-MM-DDまたはYYYY/MM/DDで入力してください。";
    /* private static final String ERR_BIRTHDAY_FORMAT_MSG = 
            "生年月日はYYYY-MM-DDで入力してください。"; */
    // 2016/10/04 GB reMOD End #54560
    /** 生年月日　未来日付エラーメッセージ */
    private static final String ERR_BIRTHDAY_LATER_THEN_SYSDATE_MSG = 
            "生年月日に未来の日付が入力されています。";    
    /** 顧客マスタ用共通文字長チェック上限(0:共通文字長チェック対象外) */
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
        // テーブル名（DBでの名称）、日本語でのテーブル名の順で定義してください。
        // CSVImportFrameが表示された時に、コンボボックスに追加するリストです。
        // 例）{"customerMaster", "顧客マスタ"}
//        {"mst_group",			"0",	"グループ"},
//        {"mst_shop",			"1",	"店舗情報"},
//        {"mst_staff_class",		"3",	"スタッフ区分"},
//        {"mst_staff",			"4",	"スタッフ"},
//        {"mst_bed",				"5",	"施術台"},
//        {"mst_item_class",		"10",	"商品分類マスタ"},
//		{"mst_item",			"11",	"商品マスタ"},
//        {"mst_technic_class",	"12",	"技術分類マスタ"},
//        {"mst_technic",			"13",	"技術マスタ"},
//        {"mst_payment_method",	"20",	"支払方法"},
//        {"mst_discount",		"21",	"割引種別"},
//        {"mst_tax",				"22",	"消費税"},
//        {"mst_customer",		"-1",	"顧客マスタ"},
//        {"mst_job",				"30",	"職業マスタ"}
	    
//2008/07/16 顧客以外を見えなくする
	        {	"mst_customer",				"-1",	"顧客マスタ"			}
//		// 会社マスタ
//		{	"mst_group",				"1",	"グループ"			},
//		{	"mst_shop",					"2",	"店舗情報"			},
//		{	"mst_authority",			"3",	"登録権限"			},
//		{	"mst_staff_class",			"4",	"スタッフ区分"		},
//		{	"mst_staff",				"5",	"スタッフ"			},
//		{	"mst_bed",					"6",	"施術台"			},
//		{	"mst_response",				"7",	"レスポンス登録"	},
//		{	"data_response_issue",		"8",	"レスポンス発行"	},
//		// 商品マスタ
//		{	"mst_item_class",			"10",	"商品分類"			},
//		{	"mst_item",					"11",	"商品登録"			},
//		{	"mst_supplier",				"13",	"仕入れ先登録"		},
//		{	"mst_destocking_division",	"14",	"在庫調整区分登録"	},
//		// 技術マスタ
//		{	"mst_technic_class",		"20",	"技術分類"			},
//		{	"mst_technic",				"21",	"技術登録"			},
//		{	"mst_proportionally",		"22",	"按分登録"			},
//		// 顧客マスタ
//        {	"mst_customer",				"-1",	"顧客マスタ"			},
//		{	"mst_job",					"30",	"職業登録"			},
//		{	"mst_free_heading_class",	"31",	"フリー項目区分"	},
//		{	"mst_free_heading",			"32",	"フリー項目"		},
//		// 精算マスタ
//		{	"mst_tax",					"40",	"消費税"			},
//		{	"mst_payment_method",		"41",	"支払い方法"		},
//		{	"mst_discount",				"42",	"割引種別"			},
//		// メールマスタ
//		{	"mst_mail_signature",		"50",	"署名"				},
//		{	"mst_mail_template_class",	"51",	"テンプレート分類"	},
//		{	"mst_mail_template",		"52",	"テンプレート"		}
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
                
		this.setPath("基本設定");
		this.setTitle("CSVインポート");
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

        jLabel3.setText("エラー");

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
                "行番号", "エラー内容"
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

        jLabel2.setText("追加する行");

        jLabel1.setText("テーブル名");

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
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(selectCSVButton);
		SystemInfo.addMouseCursorChange(importButton);
	}
	
	/**
	 * テーブルのリストを初期化する
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
	 * CSVファイルを開く
	 */
	private void openCSVFile()
	{
		JFileChooser	jfc	=	new JFileChooser();
		WildcardFileFilter filter = new WildcardFileFilter("*.csv", "CSVファイル");
		jfc.setFileFilter(filter);
		
		if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			//テーブルをクリア
			SwingUtil.clearTable(insertTable);
			SwingUtil.clearTable(errorTable);
			
			File	f	=	new File(jfc.getSelectedFile().getAbsolutePath());
			
			//ファイルが存在する場合
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
	 * CSVファイルを読み込む
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
                                // CSVヘッダを読んでおく
                                br.readLine();
                                // 2016/10/11 GB reMOD end #54560
                                
                                //１行ずつ読み込む
				while((line = br.readLine()) != null)
				{
					// 1行読み込む度に、InsertTableの行を追加して下さい。
					CSVTokenizer	csvt	=	new CSVTokenizer(line);
					Vector<Object>	rowData	=	new Vector<Object>();
					
					rowData.add(row);
					
					while (csvt.hasMoreElements())
					{
						Object	data	=	csvt.nextToken();
						// 各トークンごとにJTableのセルに追加
						rowData.add(data);
					}
					
					//テーブルにデータを追加
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
	 * インポートデータのテーブルを初期化
	 */
	private void initInsertTable()
	{
		//選択されているテーブルの情報を取得
		TableInfo	ti	=	(TableInfo)tableNameList.getSelectedItem();
		
		DefaultTableModel	model	=	(DefaultTableModel)insertTable.getModel();
		
		//ヘッダの文字列をセット
		Vector<Object>		colHeader	=	new Vector<Object>();
		colHeader.add("行番号");
		
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
				//行番号以外はStringに設定
				if(columnIndex == 0)
					return	java.lang.Integer.class;
				else
                                        return java.lang.String.class;
            }
			
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
				//行番号以外は編集可
                return (columnIndex != 0);
            }
         });
        	
		SwingUtil.setJTableHeaderRenderer(insertTable, SystemInfo.getTableHeaderRenderer());
	}
	
	/**
	 * CSVファイルからデータを取り込む
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
                        // 作成行がない場合はメッセージを出し、終了
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
                            // 行ごとに各項目の内容チェック
                            //2016/10/11 GB reMOD Start #54560
                            for(int j = 1; j < rowCount + 1; j ++){
                            //for(int j = 2; j < rowCount + 1; j ++){
                            //2016/10/11 GB reMOD End #54560
                                if (!checkRowErr(i, j)) {
                                    chkFlg = false;
                                }
                            }
                            // チェックでエラーがあった場合は次の行へ
                            if (!chkFlg) {
                                isImported[i]	=	false;
                                continue;
                            }
			//2016/09/13 GB MOD End
                            
                            
				String sql = makeInsertSQL(ti,i);
				

				
				con.begin();
				
				//行ごとにテーブルに追加
				try
				{
					isImported[i]	=	(con.executeUpdate(sql) == 1);
				}
				catch(SQLException e)
				{
					isImported[i]	=	false;
					addErrorRow((Integer)insertTable.getValueAt(i, 0), e);
				}
				
				//インポートできた場合
				if(isImported[i])
				{
					con.commit();
                                        
				}
				//インポートできなかった場合
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
		
		//全行登録できた場合
		if(errorTable.getRowCount() == 0)
		{
			MessageDialog.showMessageDialog(this,
					MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
		}
		//登録できない行があった場合
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

			/* INSERT文 */
			
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

			/* UPDATE文 */

			String updateString = "";
			
			// 2016/10/11 GB reMOD Start #54560
                        for(int j = 2; j < rowCount+1; j ++){
                        //for(int j = 3; j < rowCount+1; j ++){
                        
                            // 2016/09/13 GB MOD Start #54560
                            // ログインID、パスワードは更新しない
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
                        // 更新日時の更新文を追加
                        updateString += ",";
                        updateString += "update_date = current_timestamp";                        
                        // 2016/09/13 GB MOD End #54560

			sql = "update " + ti.getTableName() + " set ";
			sql += updateString.substring(1);
			sql += " where customer_no = '" + customerNo + "'";
                // 2016/09/13 GB MOD Start #54560
                // 同一顧客Noが複数存在する場合はチェックでエラーにするためここには入らない
//		} else {
//			
//			// 同一顧客Noが複数存在した場合は２重登録エラーを発生させる
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
	 * エラー行を追加する。
	 */
	private void addErrorRow(Integer row, Exception e)
	{
		DefaultTableModel	model	=	(DefaultTableModel)errorTable.getModel();
		
		Object[]	errorRow	=	{ row, e.getLocalizedMessage()	};
		
		model.addRow(errorRow);
	}

	/**
	 * 同一顧客Noの件数を取得
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
	 * 更新SQL文生成時にカラムタイプに応じた値を取得
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
                            //顧客ID
                            result += "(select coalesce(max(customer_id), 0) + 1 as customer_id from " + ti.getTableName() + ")";
                            //顧客No
                            result +=  "," + SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());
                            //ショップID(顧客共有のときのショップIDは0)
                            result += ", 0";
                        }
                        // 2016/10/11 GB reMOD End #54560
// 2016/09/13 GB MOD End #54560

                // 2016/10/11 GB reMOD Start #54560		
                //}
                //else if(j == 3 && ti.getTableName().equals("mst_customer") && SystemInfo.getSetteing().isShareCustomer()) {
                    //result = "0";	//顧客共有のときのショップIDは0
                // 2016/10/11 GB reMOD End #54560
// 2016/09/13 GB MOD Start #54560
                
                // 2016/10/11 GB reMOD Start #54560
                //} else if(j == 8 && ti.getTableName().equals("mst_customer")) {

			//result = "(select make_login_id())";	//ログインID生成
                //} else if(j == 9 && ti.getTableName().equals("mst_customer")) {

			//result = "(select make_password(8))";	//パスワード生成

                        
                } else if(j == 5 && ti.getTableName().equals("mst_customer")) {

                    if(isInsert){
                        //ふりがな２
                        result +=  SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());
                        //ログインID
                        result += ", (select make_login_id())";
                        //パスワード
                        result += ", (select make_password(8))";	//パスワード生成
                    }else{
                        //更新
                        //ふりがな２
                        result +=  SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString());
                    }
                        
                } else if(j == 16 && ti.getTableName().equals("mst_customer")) {
                //} else if(j == 20 && ti.getTableName().equals("mst_customer")) {
                // 2016/10/11 GB reMOD End #54560

                        result = (insertTable.getValueAt(i, j).toString().equals("") ? "2" 
                            :SQLUtil.convertForSQL(insertTable.getValueAt(i, j).toString()));	//性別は空白の場合2

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
     * エラーチェック実施
     * @param row チェック対象の行番号
     * @param col チェック対象の列番号
     * @return チェック結果
     */
    private boolean checkRowErr(Integer row, Integer col)  throws SQLException {
        String val = insertTable.getValueAt(row, col).toString();
        String colName = insertTable.getColumnModel().getColumn(col).getIdentifier().toString();
        Integer errRowNo = Integer.parseInt(insertTable.getValueAt(row, 0).toString());
        Integer maxLength = MST_CUSTOMER_FIELDS_MAX_LENGTH[col];
        
        boolean result = true;

        //項目独自のチェック
        switch (col) {
            // 2016/10/11 GB reMOD Start #54560
            case 1:
            //case 2:
            // 2016/10/11 GB reMOD End #54560
            // 顧客No：必須、半角英数字、ハイフンのみ、顧客マスタ内に同一顧客No.が2件以上存在する場合NG
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
            // 氏名1：空白の場合、氏名2も空白ならNG
            if (val == null || val.equals("")) {
                // 2016/10/11 GB reMOD Start #54560
                String val2 = insertTable.getValueAt(row, 3).toString();
                //String val2 = insertTable.getValueAt(row, 5).toString();
                // 2016/10/11 GB reMOD End #54560
                if (val2 == null || val2.equals("")) {
                    addCheckErrorRow(errRowNo, MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "氏名1,氏名2"));
                    result = false;
                }
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 6:
            //case 10:
            // 2016/10/11 GB reMOD End #54560
            // 郵便番号:空白または半角数字7桁でなければNG
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
            // 電話番号、携帯電話、FAX番号:半角数字でなければNG
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
            // PCメールアドレス、携帯メールアドレス:半角英数字または-\@._でなければNG
            if (!checkPatternMatch(val, CustomFilter.MAIL_ADDRESS)) {
                addCheckErrorRow(errRowNo, colName + ERROR_PATTERN_MSG.replaceAll("\\{0\\}", MAIL_ADDRESS_PATTERN_MSG));
                result = false;
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 16:
            //case 20:
            // 2016/10/11 GB reMOD End #54560
            // 性別:空白,"1","2"以外はNG
            if (val != null && !val.equals("") && !val.equals("1") && !val.equals("2")) {
                addCheckErrorRow(errRowNo, ERR_SEX_INVALID_MSG);
                result = false;
            }
            break;
            // 2016/10/11 GB reMOD Start #54560
            case 17:
            //case 21:
            // 2016/10/11 GB reMOD end #54560
            // 生年月日:日付以外、未来日はNG
            if (val != null && !val.equals("")) {
                // 2016/10/04 GB reMOD Start #54560
                // yyyy/mm/dd も登録できるように
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
                    // yyyy/mm/dd も登録できるように
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
            // 職業ID:半角数字のみ、職業マスタに存在しなければNG
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
                //導入前来店回数:Integerのみ
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
                //SOSIA ID:数値のみ,0～999999
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
        
        
        
        // 文字長上限チェック
        if (!checkLength(val, maxLength)) {
            addCheckErrorRow(errRowNo, MessageUtil.getMessage(1201, colName, maxLength.toString() + "文字"));
            result = false;
        }
        
        return result;
    }
        
    /**
     * 文字長上限チェックを実施する。
     * @param val 対象文字列
     * @param maxlength 文字長上限(0の場合はチェックしない)
     * @return チェック結果
     */
    private boolean checkLength(String val, Integer maxlength)
    {
        // 文字数上限のチェックを行わない場合、対象文字が空白の場合はチェックしない
        if (maxlength == 0 || val == null || val.equals("")) {
            return true;
        } else {
            return val.length() <= maxlength;
        }
    }

    /**
     * パターンチェックを実施する。
     * @param val 対象文字列
     * @param pattern 使用可能文字列のパターン
     * @return チェック結果
     */
    private boolean checkPatternMatch(String val, String pattern) {
        return val.matches("[" + pattern + "]*");
    }    
    /**
     * 職業マスタの存在チェック
     * @param jobId チェック対象の職業ID(空白の場合はチェックしない)
     * @return チェック結果(true:チェックしない or 存在する)
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
     * 職業マスタ存在チェック用Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL(String jobId) {
            return "select *\n"
                    + "from mst_job \n"
                    + "where	job_id = " + SQLUtil.convertForSQL(jobId) + "\n"
                    + "and	delete_date is null\n";

    }    
    /**
     * チェックエラー時にエラー行を追加する。
     * @param row エラーの行番号
     * @param msg エラーの内容
     */
    private void addCheckErrorRow(Integer row, String msg)
    {
            DefaultTableModel	model	=	(DefaultTableModel)errorTable.getModel();
            
            Object[]	errorRow	=	{row, msg};

            model.addRow(errorRow);
    }
        
    // 2016/09/13 GB MOD End #54560
}

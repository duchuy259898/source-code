/*
 * CardLayoutPanel.java
 *
 * Created on 2008/08/18, 21:28
 */

package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.*;
import com.geobeck.swing.CustomFilter;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import javax.swing.border.EtchedBorder;
import javax.swing.text.PlainDocument;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.text.DocumentFilter;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.Color;
import javax.swing.JFormattedTextField;
/**
 *
 * @author  takeda
 */
public class CardLayoutPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    /**
     * CONSTANTS: error codes, etc.
     */
    private static int COMMENT_MAX_LENGTH;
    private static int TITLE_MAX_LENGTH;
    
    private final int FAILED_INSERT = 1;
    private final int FAILED_DELETE = 2;
    private final int FAILED_UPDATE = 3;
    private final int MISSING_DATE = 4;
    private final int INVALID_DATE_INPUT = 5;
    private final int SAME_DATE_INPUT = 6;
    
    private final int DELETED_ROW_INDEX = -1;
    private final int NO_SELECTION = -1;
    
    private	LocalFocusTraversalPolicy	ftp;
    
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy() {
        return	ftp;
    }
    
    /**
     * CLASS VARIABLES
     */
    private ArrayList<PointCardLayoutBean> layoutDataList;
    PointCardLayoutData layoutDBAccess;
    
    boolean comment1Max = false;
    
    /**
     * Creates new form CardLayoutPanel and initizes the table component
     */
    public CardLayoutPanel() {
        
        COMMENT_MAX_LENGTH = SystemInfo.getPointcardConnection().getMaxChars();
        TITLE_MAX_LENGTH = SystemInfo.getPointcardConnection().getMaxChars();
        
        initComponents();
    
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        
        this.setSize(900, 550);
        this.setPath("基本設定 >> ポイントカード設定 >> ポイントカード");
        this.setTitle("カードテンプレート登録");
        
        SystemInfo.initGroupShopComponents(shop, 2);
        
        //initialize class to access the database
        this.layoutDBAccess = new PointCardLayoutData();
        MstShop mstShop = (MstShop)shop.getSelectedItem();
        this.layoutDataList = this.layoutDBAccess.getPointCardLayouts(mstShop.getShopID());
        
        this.initColumn();
        
        //Populate table where: {table_row_index} == {bean_index}
        for( int index = 0; index < layoutDataList.size(); index++  ) {
            PointCardLayoutBean bean = (PointCardLayoutBean)this.layoutDataList.get( index );
            insertNewRow( bean, index );
        }
        
        //disable cell selection of the table
        this.pointCardLayoutTable.setColumnSelectionAllowed( false );
        
        // enable single row selection only
        this.pointCardLayoutTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION  );

        // ボタンの上にマウスカーソルが乗った時にカーソルを変更する
        addMouseCursorChange();
        lblInputNum.setText("※1行につき全角" + (COMMENT_MAX_LENGTH / 2) + "文字まで登録できます。");

        this.setKeyListener();
        
        if (SystemInfo.getPointcardProductId() == 2) {
            commentInputLabel_08.setVisible(false);
            commentInputLabel_09.setVisible(false);
            commentInput_08.setVisible(false);
            commentInput_09.setVisible(false);
            comment8Preview.setVisible(false);
            comment9Preview.setVisible(false);
        }
        
    }

    private void addMouseCursorChange()
    {
        SystemInfo.addMouseCursorChange(insertButton);
        SystemInfo.addMouseCursorChange(editButton);
        SystemInfo.addMouseCursorChange(deleteButton);
    }

    private void setKeyListener() {
        shop.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodStart.addKeyListener(SystemInfo.getMoveNextField());
        cmbTargetPeriodEnd.addKeyListener(SystemInfo.getMoveNextField());
        titleTextField.addKeyListener(SystemInfo.getMoveNextField());
        displayTextField.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_01.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_02.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_03.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_04.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_05.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_06.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_07.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_08.addKeyListener(SystemInfo.getMoveNextField());
        commentInput_09.addKeyListener(SystemInfo.getMoveNextField());
        
        shop.addFocusListener(SystemInfo.getSelectText());
        cmbTargetPeriodStart.addFocusListener(SystemInfo.getSelectText());
        cmbTargetPeriodEnd.addFocusListener(SystemInfo.getSelectText());
        titleTextField.addFocusListener(SystemInfo.getSelectText());
        displayTextField.addFocusListener(SystemInfo.getSelectText());
        commentInput_01.addFocusListener(SystemInfo.getSelectText());
        commentInput_02.addFocusListener(SystemInfo.getSelectText());
        commentInput_03.addFocusListener(SystemInfo.getSelectText());
        commentInput_04.addFocusListener(SystemInfo.getSelectText());
        commentInput_05.addFocusListener(SystemInfo.getSelectText());
        commentInput_06.addFocusListener(SystemInfo.getSelectText());
        commentInput_07.addFocusListener(SystemInfo.getSelectText());
        commentInput_08.addFocusListener(SystemInfo.getSelectText());
        commentInput_09.addFocusListener(SystemInfo.getSelectText());
    }
    
    /**
     * 明細の列を初期化する。
     */
    private void initColumn() {
        pointCardLayoutTable.getColumnModel().getColumn(0).setPreferredWidth(200);       // タイトル
        pointCardLayoutTable.getColumnModel().getColumn(0).setResizable( false );
        pointCardLayoutTable.getColumnModel().getColumn(1).setPreferredWidth(150);      // 利用期間
        pointCardLayoutTable.getColumnModel().getColumn(1).setResizable( false );
        pointCardLayoutTable.getColumnModel().getColumn(2).setPreferredWidth(40);      // 表示順
        pointCardLayoutTable.getColumnModel().getColumn(2).setResizable( false );

        SwingUtil.clearTable(pointCardLayoutTable);
    }

    /**
     * This function must be called after insert, delete, and edit functions.
     * @param selectedDispSeq The sequence number of the entry to be selected in the table.
     */
    private void refreshTable( int selectedDispSeq ) {
        int index = 0;
        
        // リストが作成されていないときは、処理しない
        if( layoutDataList == null ) return;
        
        if( layoutDataList.isEmpty() == false ) {
            layoutDataList.clear();
        }
        
        MstShop mstShop = (MstShop)shop.getSelectedItem();
        this.layoutDataList = this.layoutDBAccess.getPointCardLayouts(mstShop.getShopID());
        
        //Clear the existing table first
        DefaultTableModel tableModel =(DefaultTableModel) this.pointCardLayoutTable.getModel();
        while( tableModel.getRowCount() > 0 ) {
            tableModel.removeRow( 0 );
        }
        
        //Then all data in the new order and display the modifications
        for( index = 0; index < layoutDataList.size(); index++  ) {
            PointCardLayoutBean bean = (PointCardLayoutBean)this.layoutDataList.get( index );
            insertNewRow( bean, index );
        }
        
        //Search for the newly-inserted or the modified item where display_seq=selectedDispSeq
        if( selectedDispSeq != this.DELETED_ROW_INDEX ) {
            PointCardLayoutBean tempBean = null;
            index = 0;
            while( index < this.layoutDataList.size() ) {
                tempBean = this.layoutDataList.get( index );
                if( tempBean.getDisplaySeq() == selectedDispSeq ) {
                    break;
                }
                index++;
            }
            
            // Select the table entry where display_seq=selectedDispSeq
            ListSelectionModel selectionModel = this.pointCardLayoutTable.getSelectionModel();
            selectionModel.setSelectionInterval( index, index );
        }
        //update buttons
        this.enableDisableButtons();
    }
    
    /**
     * Inserts a new item at the end of the table
     * @param rowData   the PointCardLayoutBean that contains the data to be inserted into the table
     * @param rowNum    table row/index where rowData is to be inserted
     */
    private void insertNewRow( PointCardLayoutBean rowData, int rowNum ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd" );
        String date = null;
        
        //add a blank row into the table
        DefaultTableModel tableModel =(DefaultTableModel) this.pointCardLayoutTable.getModel();
        tableModel.addRow( new Vector() );
        
        //fill the new row columns
        for( int colIndex=0; colIndex < this.pointCardLayoutTable.getColumnCount(); colIndex++ ) {
            this.pointCardLayoutTable.setValueAt( rowData.getTemplateTitle(), rowNum, colIndex++ );
            
            if( ( rowData.getFromDate() ) == null && ( rowData.getToDate() == null ) ) {
                date = "指定なし";
                
            } else {
                if( rowData.getFromDate() != null ) {
                    date = dateFormat.format( rowData.getFromDate() );
                } else {
                    date = "指定なし";
                }
                
                date += " ～ ";
                
                if( rowData.getToDate() != null ) {
                    date += dateFormat.format( rowData.getToDate() );
                } else {
                    date += "指定なし";
                }
            }
            this.pointCardLayoutTable.setValueAt( date, rowNum, colIndex++ );
            this.pointCardLayoutTable.setValueAt( rowData.getDisplaySeq(), rowNum, colIndex++ );
        }
    }
    
    /**
     * Displays the values in all the text fields and the date calendar, depending on the selection in the table.
     * @param rowSelected   the selected row (index starts at 0) in the table.
     *                      This is used to identify which PointCardLayoutBean in the layoutDataList to use
     */
    private void populateFields( int rowSelected ) {
        PointCardLayoutBean bean = (PointCardLayoutBean)this.layoutDataList.get( rowSelected );
        
        this.cmbTargetPeriodStart.setDate( bean.getFromDate() );
        this.cmbTargetPeriodEnd.setDate( bean.getToDate() );
        
        this.titleTextField.setText( bean.getTemplateTitle() );
        this.displayTextField.setText( String.valueOf( bean.getDisplaySeq() ) );
        
        //comments:
        this.commentInput_01.setText( bean.getComment1() );
        this.commentInput_02.setText( bean.getComment2() );
        this.commentInput_03.setText( bean.getComment3() );
        this.commentInput_04.setText( bean.getComment4() );
        this.commentInput_05.setText( bean.getComment5() );
        this.commentInput_06.setText( bean.getComment6() );
        this.commentInput_07.setText( bean.getComment7() );
        this.commentInput_08.setText( bean.getComment8() );
        this.commentInput_09.setText( bean.getComment9() );
    }
    
    /**
     * Verifies if there are inputs for sequence number and the dates.
     */
    private boolean checkInputFields() {
        //Check if date fields are blank:
        if( ( this.cmbTargetPeriodStart.getDate() == null ) || ( this.cmbTargetPeriodEnd.getDate() == null )   ) {
            //only a single data with null dates is allowed to exist in the database
            MstShop mstShop = (MstShop)shop.getSelectedItem();
            if( this.layoutDBAccess.isNullTemplatePresent(mstShop.getShopID()) == true ) {
                this.displayMessage( MISSING_DATE );
                return false;
            }
        } else {
            Calendar fromDate = Calendar.getInstance();
            fromDate.setTime( this.cmbTargetPeriodStart.getDate() );
            Calendar toDate = Calendar.getInstance();
            toDate.setTime( this.cmbTargetPeriodEnd.getDate() );
            
            if( fromDate.after( toDate ) ) {
                this.displayMessage( this.INVALID_DATE_INPUT );
                return false;
            }
        }
        return true;
    }
    
    private boolean checkInputFields( int templateId ) {
        //Check if date fields are blank:
        if( ( this.cmbTargetPeriodStart.getDate() == null ) || ( this.cmbTargetPeriodEnd.getDate() == null )   ) {
            //only a single data with null dates is allowed to exist in the database
            MstShop mstShop = (MstShop)shop.getSelectedItem();
            if( this.layoutDBAccess.isNullTemplatePresent(mstShop.getShopID()) == true ) {
                if( this.layoutDBAccess.isTheNullTemplate( mstShop.getShopID(), templateId ) == false ) {
                    this.displayMessage( MISSING_DATE );
                    return false;
                }
            }
        } else {
            Calendar fromDate = Calendar.getInstance();
            fromDate.setTime( this.cmbTargetPeriodStart.getDate() );
            Calendar toDate = Calendar.getInstance();
            toDate.setTime( this.cmbTargetPeriodEnd.getDate() );
            
            if( fromDate.after( toDate ) ) {
                this.displayMessage( this.INVALID_DATE_INPUT );
                return false;
            }
        }
        return true;
    }
    
    /**
     * A function that reads all the input fields.  This is used in insertButtonMousePressed() function.
     * @return a new instance of PointCardLayoutBean that contains the information of the new template
     */
    private PointCardLayoutBean readFieldInputs() {
        PointCardLayoutBean inputBean = new PointCardLayoutBean();
        MstShop mstShop = (MstShop)shop.getSelectedItem();
        inputBean.setShopId(mstShop.getShopID());
        if( this.displayTextField.getText().trim().length() == 0 ){
            inputBean.setDisplaySeq( 99999 );
        }else{
            inputBean.setDisplaySeq( Integer.parseInt( this.displayTextField.getText()) );
        }
        inputBean.setTemplateTitle( this.titleTextField.getText() );
        inputBean.setFromDate( this.cmbTargetPeriodStart.getDate() );
        inputBean.setToDate( this.cmbTargetPeriodEnd.getDate() );
        inputBean.setComment1( this.commentInput_01.getText() );
        inputBean.setComment2( this.commentInput_02.getText() );
        inputBean.setComment3( this.commentInput_03.getText() );
        inputBean.setComment4( this.commentInput_04.getText() );
        inputBean.setComment5( this.commentInput_05.getText() );
        inputBean.setComment6( this.commentInput_06.getText() );
        inputBean.setComment7( this.commentInput_07.getText() );
        inputBean.setComment8( this.commentInput_08.getText() );
        inputBean.setComment9( this.commentInput_09.getText() );
        return inputBean;
    }
    
    /**
     * A function that reads all the input fields.  This is used in editButtonMousePressed() function.
     * @param existingData the PointCardLayoutBean that contains the data of the layout to be edited
     * @return the parameter existingData with the updated information
     */
    private PointCardLayoutBean readFieldInputs( PointCardLayoutBean existingData ) {
        int displaySeq;
        if( this.displayTextField.getText().trim().length() == 0 ){
            displaySeq = 99999;
        }else{
            displaySeq = Integer.parseInt( this.displayTextField.getText() );
        }
        existingData.setDisplaySeq( displaySeq );
        existingData.setTemplateTitle( this.titleTextField.getText() );
        existingData.setFromDate( this.cmbTargetPeriodStart.getDate() );
        existingData.setToDate( this.cmbTargetPeriodEnd.getDate() );
        existingData.setComment1( this.commentInput_01.getText() );
        existingData.setComment2( this.commentInput_02.getText() );
        existingData.setComment3( this.commentInput_03.getText() );
        existingData.setComment4( this.commentInput_04.getText() );
        existingData.setComment5( this.commentInput_05.getText() );
        existingData.setComment6( this.commentInput_06.getText() );
        existingData.setComment7( this.commentInput_07.getText() );
        existingData.setComment8( this.commentInput_08.getText() );
        existingData.setComment9( this.commentInput_09.getText() );
        return existingData;
    }
    
    /**
     * Enables or disables the edit, insert, and delete button depending on the
     * selection in the table.
     */
    private void enableDisableButtons() {
        int selectedRow = this.pointCardLayoutTable.getSelectedRow();
        //if no row is selected, enable the insert button only
        if( selectedRow == this.NO_SELECTION ) {
            this.editButton.setEnabled( false );
            this.deleteButton.setEnabled( false );
            this.insertButton.setEnabled( true );
        } else {
            PointCardLayoutBean bean = this.layoutDataList.get(selectedRow);
            
            this.editButton.setEnabled( true );
            this.insertButton.setEnabled( true );
            
            //@shiera - this condition is not yet final!!!
            if(  (bean.getFromDate()==null) && (bean.getToDate()==null) ) {
                this.deleteButton.setEnabled( false );
            } else {
                this.deleteButton.setEnabled( true );
            }
        }
    }
    
    /**
     * Displays a message depending on the message type
     * @param msgType   its value could be one of the following constants:
     *                  FAILED_INSERT, FAILED_UPDATE, FAILED_DELETE,
     *                   MISSING_DATE, INVALID_DATE_INPUT
     */
    private void displayMessage( int msgType ) {
        switch( msgType ) {
            case FAILED_INSERT:
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "データの登録に失敗しました。",
                        JOptionPane.ERROR_MESSAGE );
                break;
            case FAILED_UPDATE:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "データの更新に失敗しました。",
                        JOptionPane.ERROR_MESSAGE );
                break;
            }
            case FAILED_DELETE:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "ポイントカードテンプレートを削除できませんでした。",
                        JOptionPane.ERROR_MESSAGE );
                break;
            }
            case MISSING_DATE:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "期間を入力してください。", JOptionPane.ERROR_MESSAGE );
                break;
            }
            case SAME_DATE_INPUT:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "他のテンプレートとの間に重複期間が存在します。", JOptionPane.ERROR_MESSAGE );
                break;
            }
            case INVALID_DATE_INPUT:
            {
                MessageDialog.showMessageDialog( this.getParentFrame(),
                        "期間の入力はFROM＜Toでなければいけません。", JOptionPane.ERROR_MESSAGE );
                break;
            }
            
            default:
                break;
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cardPanel = new com.geobeck.swing.ImagePanel();
        comment1Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment2Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment3Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment4Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment5Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment6Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment7Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment8Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        comment9Preview = new com.geobeck.swing.JFormattedTextFieldEx();
        customerIdPreview = new com.geobeck.swing.JFormattedTextFieldEx();
        customerNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        pointsPreview = new com.geobeck.swing.JFormattedTextFieldEx();
        datePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        staffNamePreview = new com.geobeck.swing.JFormattedTextFieldEx();
        pointsPreview1 = new com.geobeck.swing.JFormattedTextFieldEx();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pointCardLayoutTable = new javax.swing.JTable();
        pointCardLayoutTable = new javax.swing.JTable(new DefaultTableModel()){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        insertButton = new javax.swing.JButton();
        insertButton.setContentAreaFilled(false);
        editButton = new javax.swing.JButton();
        editButton.setContentAreaFilled(false);
        deleteButton = new javax.swing.JButton();
        deleteButton.setContentAreaFilled(false);
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbTargetPeriodEnd = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        commentInputLabel_08 = new javax.swing.JLabel();
        commentInputLabel_09 = new javax.swing.JLabel();
        displayTextField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)displayTextField.getDocument()).setDocumentFilter(new CustomFilter(4,CustomFilter.NUMBER));
        commentInput_01 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_01.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_02 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_02.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_03 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_03.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_04 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_04.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_05 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_05.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_06 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_06.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_07 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_07.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_08 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_08.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        commentInput_09 = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)commentInput_09.getDocument()).setDocumentFilter(new LimitByteFilter( COMMENT_MAX_LENGTH ) );
        jLabel16 = new javax.swing.JLabel();
        titleTextField = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)titleTextField.getDocument()).setDocumentFilter(new CustomFilter( TITLE_MAX_LENGTH ) );
        cmbTargetPeriodStart = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        shopLabel = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        lblInputNum = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setOpaque(false);
        jLabel1.setFont(new java.awt.Font("MS UI Gothic", 0, 14));
        jLabel1.setText("\u8868\u793a\u30ec\u30a4\u30a2\u30a6\u30c8");

        cardPanel.setImage(SystemInfo.getImageIcon("/card/background.png"));
        cardPanel.setOpaque(false);
        comment1Preview.setBackground(Color.WHITE);
        comment1Preview.setBorder(null);
        comment1Preview.setEditable(false);
        comment1Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment1Preview.setAutoscrolls(false);
        comment1Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment1Preview.setFocusable(false);
        comment1Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment1Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment1Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment2Preview.setBackground(Color.WHITE);
        comment2Preview.setBorder(null);
        comment2Preview.setEditable(false);
        comment2Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment2Preview.setAutoscrolls(false);
        comment2Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment2Preview.setFocusable(false);
        comment2Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment2Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment2Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment3Preview.setBackground(Color.WHITE);
        comment3Preview.setBorder(null);
        comment3Preview.setEditable(false);
        comment3Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment3Preview.setAutoscrolls(false);
        comment3Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment3Preview.setFocusable(false);
        comment3Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment3Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment3Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment4Preview.setBackground(Color.WHITE);
        comment4Preview.setBorder(null);
        comment4Preview.setEditable(false);
        comment4Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment4Preview.setAutoscrolls(false);
        comment4Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment4Preview.setFocusable(false);
        comment4Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment4Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment4Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment5Preview.setBackground(Color.WHITE);
        comment5Preview.setBorder(null);
        comment5Preview.setEditable(false);
        comment5Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment5Preview.setAutoscrolls(false);
        comment5Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment5Preview.setFocusable(false);
        comment5Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment5Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment5Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment6Preview.setBackground(Color.WHITE);
        comment6Preview.setBorder(null);
        comment6Preview.setEditable(false);
        comment6Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment6Preview.setAutoscrolls(false);
        comment6Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment6Preview.setFocusable(false);
        comment6Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment6Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment6Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment7Preview.setBackground(Color.WHITE);
        comment7Preview.setBorder(null);
        comment7Preview.setEditable(false);
        comment7Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment7Preview.setAutoscrolls(false);
        comment7Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment7Preview.setFocusable(false);
        comment7Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment7Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment7Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment8Preview.setBackground(Color.WHITE);
        comment8Preview.setBorder(null);
        comment8Preview.setEditable(false);
        comment8Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment8Preview.setAutoscrolls(false);
        comment8Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment8Preview.setFocusable(false);
        comment8Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment8Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment8Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        comment9Preview.setBackground(Color.WHITE);
        comment9Preview.setBorder(null);
        comment9Preview.setEditable(false);
        comment9Preview.setForeground(new java.awt.Color(0, 0, 102));
        comment9Preview.setAutoscrolls(false);
        comment9Preview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        comment9Preview.setFocusable(false);
        comment9Preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        comment9Preview.setPreferredSize(new java.awt.Dimension(150, 13));
        comment9Preview.setSelectionColor(new java.awt.Color(255, 255, 255));

        customerIdPreview.setBackground(Color.WHITE);
        customerIdPreview.setBorder(null);
        customerIdPreview.setEditable(false);
        customerIdPreview.setForeground(new java.awt.Color(0, 0, 102));
        customerIdPreview.setText("\uff19\uff19\uff19\uff19\uff19");
        customerIdPreview.setAutoscrolls(false);
        customerIdPreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        customerIdPreview.setFocusable(false);
        customerIdPreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        customerNamePreview.setBackground(Color.WHITE);
        customerNamePreview.setBorder(null);
        customerNamePreview.setEditable(false);
        customerNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        customerNamePreview.setText("\u5c71\u7530\u3000\u592a\u90ce\u3000\u69d8");
        customerNamePreview.setAutoscrolls(false);
        customerNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        customerNamePreview.setFocusable(false);
        customerNamePreview.setFont(new java.awt.Font("MS UI Gothic", 1, 18));
        customerNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        pointsPreview.setBackground(Color.WHITE);
        pointsPreview.setBorder(null);
        pointsPreview.setEditable(false);
        pointsPreview.setForeground(new java.awt.Color(0, 0, 102));
        pointsPreview.setText("\uff19\uff19\uff0c\uff19\uff19\uff19");
        pointsPreview.setAutoscrolls(false);
        pointsPreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        pointsPreview.setFocusable(false);
        pointsPreview.setFont(new java.awt.Font("MS UI Gothic", 1, 18));
        pointsPreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        datePreview.setBackground(Color.WHITE);
        datePreview.setBorder(null);
        datePreview.setEditable(false);
        datePreview.setForeground(new java.awt.Color(0, 0, 102));
        datePreview.setText("2008\u5e7409\u670810\u65e5");
        datePreview.setAutoscrolls(false);
        datePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        datePreview.setFocusable(false);
        datePreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        staffNamePreview.setBackground(Color.WHITE);
        staffNamePreview.setBorder(null);
        staffNamePreview.setEditable(false);
        staffNamePreview.setForeground(new java.awt.Color(0, 0, 102));
        staffNamePreview.setText("\u9234\u6728\u3000\u4e00\u90ce");
        staffNamePreview.setAutoscrolls(false);
        staffNamePreview.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        staffNamePreview.setFocusable(false);
        staffNamePreview.setSelectionColor(new java.awt.Color(255, 255, 255));

        pointsPreview1.setBackground(Color.WHITE);
        pointsPreview1.setBorder(null);
        pointsPreview1.setEditable(false);
        pointsPreview1.setForeground(new java.awt.Color(0, 0, 102));
        pointsPreview1.setText(" POINT");
        pointsPreview1.setAutoscrolls(false);
        pointsPreview1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        pointsPreview1.setFocusable(false);
        pointsPreview1.setFont(new java.awt.Font("MS UI Gothic", 0, 10));
        pointsPreview1.setSelectionColor(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout cardPanelLayout = new javax.swing.GroupLayout(cardPanel);
        cardPanel.setLayout(cardPanelLayout);
        cardPanelLayout.setHorizontalGroup(
            cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPanelLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(staffNamePreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(datePreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(customerNamePreview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(customerIdPreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(comment9Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment8Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment7Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment6Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment5Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment4Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment3Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment2Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addComponent(comment1Preview, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pointsPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pointsPreview1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51))
        );
        cardPanelLayout.setVerticalGroup(
            cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(customerIdPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerNamePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pointsPreview1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointsPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(staffNamePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(comment1Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment2Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment3Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment4Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment5Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment6Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment7Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment8Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(comment9Preview, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jLabel4.setText("\u203b\u4e0a\u8a18\u306e\u8868\u793a\u306f\u30a4\u30e1\u30fc\u30b8\u3068\u306a\u308a\u307e\u3059\u3002");

        jLabel15.setText("\u5b9f\u969b\u306e\u5370\u5b57\u3068\u306f\u7570\u306a\u308a\u307e\u3059\u3002");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel3.setOpaque(false);
        pointCardLayoutTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pointCardLayoutTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "タイトル", "適用期間", "表示順"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        pointCardLayoutTable.setAutoCreateColumnsFromModel(false);
        pointCardLayoutTable.setSelectionBackground(new java.awt.Color(255, 204, 102));
        pointCardLayoutTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        pointCardLayoutTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pointCardLayoutTableMousePressed(evt);
            }
        });
        pointCardLayoutTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pointCardLayoutTableFocusGained(evt);
            }
        });
        pointCardLayoutTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pointCardLayoutTableKeyReleased(evt);
            }
        });

        jScrollPane2.setViewportView(pointCardLayoutTable);

        insertButton.setIcon(SystemInfo.getImageIcon( "/button/common/insert_off.jpg" ));
        insertButton.setBorder(null);
        insertButton.setBorderPainted(false);
        insertButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        insertButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/insert_on.jpg" ));
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        editButton.setIcon(SystemInfo.getImageIcon( "/button/common/update_off.jpg" ));
        editButton.setBorder(null);
        editButton.setBorderPainted(false);
        editButton.setEnabled(false);
        editButton.setFocusCycleRoot(true);
        editButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        editButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/update_on.jpg" ));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setBorder(null);
        deleteButton.setBorderPainted(false);
        deleteButton.setEnabled(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(cardPanel.getBackground());
        jPanel2.setOpaque(false);
        jLabel2.setText("\u671f\u9593");

        jLabel3.setText("\u30bf\u30a4\u30c8\u30eb");

        jLabel5.setText("\uff5e");

        cmbTargetPeriodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodEndFocusGained(evt);
            }
        });

        jLabel6.setText("\u30b3\u30e1\u30f3\u30c8\uff11");

        jLabel7.setText("\u30b3\u30e1\u30f3\u30c8\uff12");

        jLabel8.setText("\u30b3\u30e1\u30f3\u30c8\uff13");

        jLabel9.setText("\u30b3\u30e1\u30f3\u30c8\uff14");

        jLabel10.setText("\u30b3\u30e1\u30f3\u30c8\uff15");

        jLabel11.setText("\u30b3\u30e1\u30f3\u30c8\uff16");

        jLabel12.setText("\u30b3\u30e1\u30f3\u30c8\uff17");

        commentInputLabel_08.setText("\u30b3\u30e1\u30f3\u30c8\uff18");

        commentInputLabel_09.setText("\u30b3\u30e1\u30f3\u30c8\uff19");

        commentInput_01.setInputKanji(true);
        commentInput_01.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_01CaretUpdate(evt);
            }
        });
        commentInput_01.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_01FocusGained(evt);
            }
        });

        commentInput_02.setInputKanji(true);
        commentInput_02.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_02CaretUpdate(evt);
            }
        });
        commentInput_02.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_02FocusGained(evt);
            }
        });

        commentInput_03.setInputKanji(true);
        commentInput_03.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_03CaretUpdate(evt);
            }
        });
        commentInput_03.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_03FocusGained(evt);
            }
        });

        commentInput_04.setInputKanji(true);
        commentInput_04.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_04CaretUpdate(evt);
            }
        });
        commentInput_04.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_04FocusGained(evt);
            }
        });

        commentInput_05.setInputKanji(true);
        commentInput_05.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_05CaretUpdate(evt);
            }
        });
        commentInput_05.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_05FocusGained(evt);
            }
        });

        commentInput_06.setInputKanji(true);
        commentInput_06.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_06CaretUpdate(evt);
            }
        });
        commentInput_06.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_06FocusGained(evt);
            }
        });

        commentInput_07.setInputKanji(true);
        commentInput_07.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_07CaretUpdate(evt);
            }
        });
        commentInput_07.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_07FocusGained(evt);
            }
        });

        commentInput_08.setInputKanji(true);
        commentInput_08.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_08CaretUpdate(evt);
            }
        });
        commentInput_08.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_08FocusGained(evt);
            }
        });

        commentInput_09.setInputKanji(true);
        commentInput_09.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                commentInput_09CaretUpdate(evt);
            }
        });
        commentInput_09.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commentInput_09FocusGained(evt);
            }
        });

        jLabel16.setText("\u8868\u793a\u9806");

        titleTextField.setInputKanji(true);
        titleTextField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                titleTextFieldInputMethodTextChanged(evt);
            }
        });
        titleTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                titleTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                titleTextFieldKeyReleased(evt);
            }
        });

        cmbTargetPeriodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbTargetPeriodStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTargetPeriodStartFocusGained(evt);
            }
        });

        shopLabel.setText("\u5e97\u8217");

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });

        lblInputNum.setText("\u203b");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(commentInputLabel_08, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(commentInputLabel_09, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(commentInput_09, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_08, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_07, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_06, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_03, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_04, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_05, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_02, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(commentInput_01, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(lblInputNum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(displayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(shopLabel)
                                .addGap(14, 14, 14)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)))
                .addContainerGap(228, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(shopLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(cmbTargetPeriodStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(cmbTargetPeriodEnd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(displayTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_02, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_03, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_04, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_05, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_06, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_07, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commentInputLabel_08, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_08, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commentInputLabel_09, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commentInput_09, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInputNum)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        refreshTable(NO_SELECTION);
    }//GEN-LAST:event_shopActionPerformed
    
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if( deleteButton.isEnabled() == true ) {
            int selectedRow = this.pointCardLayoutTable.getSelectedRow();
            if( this.layoutDBAccess.deleteCardLayout( this.layoutDataList.get( selectedRow ) ) == true ) {
                this.refreshTable( DELETED_ROW_INDEX );
            } else {
                this.displayMessage( FAILED_DELETE );
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed
    
    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        if( insertButton.isEnabled() ) {
            int selectedRow = this.pointCardLayoutTable.getSelectedRow();
            
            if( this.checkInputFields() == false )
                return;
            
            MstShop mstShop = (MstShop)shop.getSelectedItem();
            if( this.layoutDBAccess.doToFromDatesExist( mstShop.getShopID(), this.cmbTargetPeriodStart.getDate(),
                    this.cmbTargetPeriodEnd.getDate()  ) == true ) {
                this.displayMessage( SAME_DATE_INPUT );
                return;
            }
            
            PointCardLayoutBean bean = readFieldInputs();
            if( bean.getDisplaySeq() > this.layoutDBAccess.getLastDisplaySequence(mstShop.getShopID()) ) {
//                this.displayTextField.setText( String.valueOf( this.layoutDBAccess.getLastDisplaySequence(mstShop.getShopID()) + 1 ) );
                bean.setDisplaySeq( this.layoutDBAccess.getLastDisplaySequence(mstShop.getShopID()) + 1 );
            }
            
            if(!this.layoutDBAccess.insertNewLayout( bean )){
                this.displayMessage( this.FAILED_INSERT );
            }
            
            this.refreshTable( bean.getDisplaySeq() );
            
        }
    }//GEN-LAST:event_insertButtonActionPerformed
    
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if( editButton.isEnabled() == true ) {
            int selectedRow = this.pointCardLayoutTable.getSelectedRow();
            PointCardLayoutBean lastBeanSelected = this.layoutDataList.get( selectedRow );
            
            if( this.checkInputFields( lastBeanSelected.getTemplateId() ) == false )
                return;
            
            //check if the from_date or to_date overlaps with the dates an existing template
            MstShop mstShop = (MstShop)shop.getSelectedItem();
            if( this.layoutDBAccess.isTheNullTemplate( mstShop.getShopID(), lastBeanSelected.getTemplateId() ) == false ) {
                if( this.layoutDBAccess.doToFromDatesExist( mstShop.getShopID(), this.cmbTargetPeriodStart.getDate(),
                        this.cmbTargetPeriodEnd.getDate(), lastBeanSelected.getTemplateId()  ) == true ) {
                    this.displayMessage( SAME_DATE_INPUT );
                    return;
                }
            }
            
            PointCardLayoutBean bean = readFieldInputs( lastBeanSelected );
            if( bean == null ) {
                this.displayMessage( FAILED_UPDATE );
                this.refreshTable( lastBeanSelected.getDisplaySeq() );
//                this.displayTextField.setText( String.valueOf( lastBeanSelected.getDisplaySeq() ) );
            } else {
                if( bean.getDisplaySeq() > this.layoutDBAccess.getLastDisplaySequence(mstShop.getShopID()) ) {
//                    this.displayTextField.setText( String.valueOf( this.layoutDBAccess.getLastDisplaySequence(mstShop.getShopID()) ) );
                    bean.setDisplaySeq( this.layoutDBAccess.getLastDisplaySequence(mstShop.getShopID()) );
                }
                if( !this.layoutDBAccess.updateCardLayout( bean ) ){
                    this.displayMessage( FAILED_UPDATE );
                }
                this.refreshTable( bean.getDisplaySeq() );
            }
        }
    }//GEN-LAST:event_editButtonActionPerformed
    
    private void titleTextFieldKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_titleTextFieldKeyReleased
    {//GEN-HEADEREND:event_titleTextFieldKeyReleased
        if( this.titleTextField.getText().getBytes().length >= TITLE_MAX_LENGTH ) {
            this.titleTextField.enableInputMethods(false);
        } else {
            this.titleTextField.enableInputMethods(true);
        }
    }//GEN-LAST:event_titleTextFieldKeyReleased
    
    private void titleTextFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_titleTextFieldKeyPressed
    {//GEN-HEADEREND:event_titleTextFieldKeyPressed
        if( this.titleTextField.getText().getBytes().length < TITLE_MAX_LENGTH ) {
            this.titleTextField.enableInputMethods(true);
        } else {
            this.titleTextField.enableInputMethods(false);
        }
    }//GEN-LAST:event_titleTextFieldKeyPressed
    
    private void titleTextFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_titleTextFieldInputMethodTextChanged
    {//GEN-HEADEREND:event_titleTextFieldInputMethodTextChanged
        try {
            this.titleTextField.commitEdit();
        } catch(Exception e){}
        
        if( this.titleTextField.getText().getBytes().length >= TITLE_MAX_LENGTH ) {
            this.titleTextField.enableInputMethods(false);
        } else {
            this.titleTextField.enableInputMethods(true);
        }
    }//GEN-LAST:event_titleTextFieldInputMethodTextChanged
    
    private void commentInput_01CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_01CaretUpdate
    {//GEN-HEADEREND:event_commentInput_01CaretUpdate
        comment1Preview.setText( commentInput_01.getText());
    }//GEN-LAST:event_commentInput_01CaretUpdate
    
    private void cmbTargetPeriodStartFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_cmbTargetPeriodStartFocusGained
    {//GEN-HEADEREND:event_cmbTargetPeriodStartFocusGained
        cmbTargetPeriodStart.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodStartFocusGained
    
    private void cmbTargetPeriodEndFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_cmbTargetPeriodEndFocusGained
    {//GEN-HEADEREND:event_cmbTargetPeriodEndFocusGained
        this.cmbTargetPeriodEnd.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_cmbTargetPeriodEndFocusGained
    
    private void pointCardLayoutTableKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_pointCardLayoutTableKeyReleased
    {//GEN-HEADEREND:event_pointCardLayoutTableKeyReleased
        this.enableDisableButtons();
        populateFields( this.pointCardLayoutTable.getSelectedRow() );
    }//GEN-LAST:event_pointCardLayoutTableKeyReleased
    
    private void pointCardLayoutTableMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_pointCardLayoutTableMousePressed
    {//GEN-HEADEREND:event_pointCardLayoutTableMousePressed
        this.enableDisableButtons();
        populateFields( this.pointCardLayoutTable.getSelectedRow() );
    }//GEN-LAST:event_pointCardLayoutTableMousePressed
    
    private void pointCardLayoutTableFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_pointCardLayoutTableFocusGained
    {//GEN-HEADEREND:event_pointCardLayoutTableFocusGained
        this.enableDisableButtons();
    }//GEN-LAST:event_pointCardLayoutTableFocusGained
    
    private void commentInput_09FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_09FocusGained
    {//GEN-HEADEREND:event_commentInput_09FocusGained
        comment9Preview.setText( commentInput_09.getText());
    }//GEN-LAST:event_commentInput_09FocusGained
    
    private void commentInput_09CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_09CaretUpdate
    {//GEN-HEADEREND:event_commentInput_09CaretUpdate
        comment9Preview.setText( commentInput_09.getText());
    }//GEN-LAST:event_commentInput_09CaretUpdate
    
    private void commentInput_08FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_08FocusGained
    {//GEN-HEADEREND:event_commentInput_08FocusGained
        comment8Preview.setText( commentInput_08.getText());
    }//GEN-LAST:event_commentInput_08FocusGained
    
    private void commentInput_08CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_08CaretUpdate
    {//GEN-HEADEREND:event_commentInput_08CaretUpdate
        comment8Preview.setText( commentInput_08.getText());
    }//GEN-LAST:event_commentInput_08CaretUpdate
    
    private void commentInput_07FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_07FocusGained
    {//GEN-HEADEREND:event_commentInput_07FocusGained
        comment7Preview.setText( commentInput_07.getText());
    }//GEN-LAST:event_commentInput_07FocusGained
    
    private void commentInput_07CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_07CaretUpdate
    {//GEN-HEADEREND:event_commentInput_07CaretUpdate
        comment7Preview.setText( commentInput_07.getText());
    }//GEN-LAST:event_commentInput_07CaretUpdate
    
    private void commentInput_06FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_06FocusGained
    {//GEN-HEADEREND:event_commentInput_06FocusGained
        comment6Preview.setText( commentInput_06.getText());
    }//GEN-LAST:event_commentInput_06FocusGained
    
    private void commentInput_06CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_06CaretUpdate
    {//GEN-HEADEREND:event_commentInput_06CaretUpdate
        comment6Preview.setText( commentInput_06.getText());
    }//GEN-LAST:event_commentInput_06CaretUpdate
    
    private void commentInput_05FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_05FocusGained
    {//GEN-HEADEREND:event_commentInput_05FocusGained
        comment5Preview.setText( commentInput_05.getText());
    }//GEN-LAST:event_commentInput_05FocusGained
    
    private void commentInput_05CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_05CaretUpdate
    {//GEN-HEADEREND:event_commentInput_05CaretUpdate
        comment5Preview.setText( commentInput_05.getText());
    }//GEN-LAST:event_commentInput_05CaretUpdate
    
    private void commentInput_04FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_04FocusGained
    {//GEN-HEADEREND:event_commentInput_04FocusGained
        comment4Preview.setText( commentInput_04.getText());
    }//GEN-LAST:event_commentInput_04FocusGained
    
    private void commentInput_04CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_04CaretUpdate
    {//GEN-HEADEREND:event_commentInput_04CaretUpdate
        comment4Preview.setText( commentInput_04.getText());
    }//GEN-LAST:event_commentInput_04CaretUpdate
    
    private void commentInput_03FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_03FocusGained
    {//GEN-HEADEREND:event_commentInput_03FocusGained
        comment3Preview.setText( commentInput_03.getText());
    }//GEN-LAST:event_commentInput_03FocusGained
    
    private void commentInput_03CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_03CaretUpdate
    {//GEN-HEADEREND:event_commentInput_03CaretUpdate
        comment3Preview.setText( commentInput_03.getText());
    }//GEN-LAST:event_commentInput_03CaretUpdate
    
    private void commentInput_02FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_02FocusGained
    {//GEN-HEADEREND:event_commentInput_02FocusGained
        comment2Preview.setText( commentInput_02.getText());
    }//GEN-LAST:event_commentInput_02FocusGained
    
    private void commentInput_02CaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_commentInput_02CaretUpdate
    {//GEN-HEADEREND:event_commentInput_02CaretUpdate
        comment2Preview.setText( commentInput_02.getText());
    }//GEN-LAST:event_commentInput_02CaretUpdate
    
    private void commentInput_01FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_commentInput_01FocusGained
    {//GEN-HEADEREND:event_commentInput_01FocusGained
        comment1Preview.setText( commentInput_01.getText());
    }//GEN-LAST:event_commentInput_01FocusGained
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private com.geobeck.swing.ImagePanel cardPanel;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodEnd;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo cmbTargetPeriodStart;
    private com.geobeck.swing.JFormattedTextFieldEx comment1Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment2Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment3Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment4Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment5Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment6Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment7Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment8Preview;
    private com.geobeck.swing.JFormattedTextFieldEx comment9Preview;
    private javax.swing.JLabel commentInputLabel_08;
    private javax.swing.JLabel commentInputLabel_09;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_01;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_02;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_03;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_04;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_05;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_06;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_07;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_08;
    private com.geobeck.swing.JFormattedTextFieldEx commentInput_09;
    private com.geobeck.swing.JFormattedTextFieldEx customerIdPreview;
    private com.geobeck.swing.JFormattedTextFieldEx customerNamePreview;
    private com.geobeck.swing.JFormattedTextFieldEx datePreview;
    private javax.swing.JButton deleteButton;
    private com.geobeck.swing.JFormattedTextFieldEx displayTextField;
    private javax.swing.JButton editButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblInputNum;
    private javax.swing.JTable pointCardLayoutTable;
    private com.geobeck.swing.JFormattedTextFieldEx pointsPreview;
    private com.geobeck.swing.JFormattedTextFieldEx pointsPreview1;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    private com.geobeck.swing.JFormattedTextFieldEx staffNamePreview;
    private com.geobeck.swing.JFormattedTextFieldEx titleTextField;
    // End of variables declaration//GEN-END:variables
    
    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
            extends FocusTraversalPolicy {
        
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy() {
            controls.add(shop);
            controls.add(cmbTargetPeriodStart);
            controls.add(cmbTargetPeriodEnd);
            controls.add(titleTextField);
            controls.add(displayTextField);
            controls.add(commentInput_01);
            controls.add(commentInput_02);
            controls.add(commentInput_03);
            controls.add(commentInput_04);
            controls.add(commentInput_05);
            controls.add(commentInput_06);
            controls.add(commentInput_07);
            controls.add(commentInput_08);
            controls.add(commentInput_09);
            
            controls.add(shop);
            controls.add(cmbTargetPeriodStart);
        };
        
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for(Component co : controls){
                if( find ) return co;
                if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            boolean find = false;
            for( int ii = controls.size(); ii>0; ii-- ){
                Component co = controls.get(ii-1);
                if( find ) return co;
                if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }
        
        /**
         * トラバーサルサイクルの最初の Component を返します。
         */
        public Component getFirstComponent(Container aContainer) {
            return controls.get(0);
        }
        
        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer) {
            return controls.get(controls.size()-2);
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer) {
            return controls.get(0);
        }
        
        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
         */
        public Component getInitialComponent(Window window) {
            return controls.get(0);
        }
        
    }
}

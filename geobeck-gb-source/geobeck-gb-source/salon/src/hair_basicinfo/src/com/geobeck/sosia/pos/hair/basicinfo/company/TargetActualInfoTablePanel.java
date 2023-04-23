/*
 * TargetActualInfoTablePanel.java
 *
 * Created on 2008/09/19, 15:41
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.swing.CustomFilter;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.NumberUtil;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import com.geobeck.swing.MessageDialog;
import javax.swing.text.PlainDocument;
/**
 *
 * @author  shiera.delusa
 */
public class TargetActualInfoTablePanel extends javax.swing.JPanel
{
    static final protected int MONTHS_COUNT = 12;
    static final protected int LAST_READONLY_COL = 1;
    static final protected int FIRST_EDITABLE_ROW = 1;
    static final protected int TOTAL_INFO_COLUMN = 14;
    static final protected int LAST_ROW_INDEX = 33;
    
    private static final int MONTHS_LABEL_ROW = 0;
    private static final int ACTUAL_TOTAL_SALES_ROW = 1;
    private static final int ACTUAL_TECHNIQUE_ROW = 2;
    private static final int ACTUAL_COMMODITY_ROW = 3;
    private static final int ACTUAL_CUSTOMERCNT_ROW = 4;
    private static final int ACTUAL_CUSTOMERS_AVE = 5;
    private static final int ESTIMATE_TOTAL_SALES_ROW = 17;
    private static final int ESTIMATE_TECHNIQUE_ROW = 18;
    private static final int ESTIMATE_COMMODITY_ROW = 19;
    private static final int ESTIMATE_CUSTOMERCNT_ROW = 20;
    private static final int ESTIMATE_CUSTOMERS_AVE = 21;
    private static final int LAST_ROW = 33;
    
    private static final int TYPE_LABEL_COLUMN = 0;
    private static final int FIELDS_LABEL_COLUMN = 1;
    private static final int JANUARY_COLUMN = 2;
    private static final int SUM_COLUMN = JANUARY_COLUMN + 12;
    
    
    //class variables
    private ArrayList<DataTargetResultBean> dataTargetResultList = new ArrayList<DataTargetResultBean>(MONTHS_COUNT);
    private DataTargetResultBean dataTargetResultTotal = new DataTargetResultBean();
    private int year;
    private int shopId;
    
    /** Creates new form TargetActualInfoTablePanel */
    public TargetActualInfoTablePanel()
    {
        this.setSize( 1016, 732 );
        initComponents();

        this.tableAllInfo.setDefaultRenderer( Object.class, new TableRendererEx() );
        this.tableAllInfo.setDefaultEditor( Object.class, new TableCellEditorEx() );

        this.initTableHeaderMonths();
        this.initArrays();
        this.initSecondColumn();
    }

    public void loadYearData( int nShopId, int year )
    {
        this.dataTargetResultList.clear();

		this.shopId = nShopId;
        this.year = year;
        this.dataTargetResultList = DataTargetResultDAO.getYearData( shopId, year );
        this.updateTable();
    }
    
    public boolean registerAllDataToDB( int nShopId, int year )
    {
        this.stopCellEditing();
        
        this.readAllFields();
		this.shopId = nShopId;
        this.year = year;
        
        if( DataTargetResultDAO.saveToDatabase( dataTargetResultList ) == false )
        {
            this.loadYearData( nShopId, year );
            return false;
        }
        else
        {
            this.updateTotals();
        }
        
        return true;
    }
    
    public void resetCellEditedFlag()
    {
        // in calling tableAllInfo.getColumnClass() method, you can use column numbers 2-13
        ((TableCellEditorEx)this.tableAllInfo.getDefaultEditor( this.tableAllInfo.getColumnClass(3))).resetCellEditedFlag();
    }
    
    protected void initArrays()
    {
        for( int index=0; index < MONTHS_COUNT; index++ )
        {
            dataTargetResultList.add( new DataTargetResultBean() );
        }
    }
    
    public void updateTable()
    {
        DataTargetResultBean tempBean = null;
        
        for( int index = 0, monthIndex = 1; index < MONTHS_COUNT; index++,monthIndex++ )
        {
            tempBean = this.dataTargetResultList.get( index );
            if( tempBean != null )
            {
                this.updateColumn( LAST_READONLY_COL + monthIndex, tempBean );
            }
            else
            {
                this.clearColumn( LAST_READONLY_COL + monthIndex );
            }
        }
        
        this.updateTotals();
    }
        
    public void updateTotals()
    {
        //‡Œv
        this.calculateTotal();
        this.updateColumn( TOTAL_INFO_COLUMN, this.dataTargetResultTotal );        
    }
    
    private void clearColumn( int columnNum )
    {
        TableModel tableModel = this.tableAllInfo.getModel();
        
        for( int rowIndex = FIRST_EDITABLE_ROW; rowIndex <= LAST_ROW_INDEX; rowIndex++ )
        {
            tableModel.setValueAt( "", rowIndex,columnNum );
        }
    }
    
    private boolean updateColumn( int columnNum, DataTargetResultBean salesInfo )
    {
        int rowIndex = FIRST_EDITABLE_ROW;
        TableModel tableModel = this.tableAllInfo.getModel();
        
        tableModel.setValueAt( salesInfo.getResultTotalStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultTechnicStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultItemStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultInStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultAvgAmountStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultNewStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultKStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultSETStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultSStr(), rowIndex++,columnNum );        
        tableModel.setValueAt( salesInfo.getResultHDStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultPStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultSTPStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultTRStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultETCStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultCRMStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getResultMONStr(), rowIndex++,columnNum );
        
        tableModel.setValueAt( salesInfo.getTargetTotalStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetTechnicStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetItemStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetInStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetAvgAmountStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetNewStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetKStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetSETStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetSStr(), rowIndex++,columnNum );        
        tableModel.setValueAt( salesInfo.getTargetHDStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetPStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetSTPStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetTRStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetETCStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetCRMStr(), rowIndex++,columnNum );
        tableModel.setValueAt( salesInfo.getTargetMONStr(), rowIndex++,columnNum );
        
        tableModel.setValueAt( salesInfo.getOpenDaysStr(), rowIndex++,columnNum );
                
        return true;
    }
    
    protected void readAllFields()
    {
        TableModel tableModel = this.tableAllInfo.getModel();
        DataTargetResultBean tempBean = null;
        int rowIndex = FIRST_EDITABLE_ROW;
        int colIndex = LAST_READONLY_COL + 1;
        
        for( int index = 0; index < MONTHS_COUNT; index++ )
        {
            rowIndex = FIRST_EDITABLE_ROW;
            tempBean = new DataTargetResultBean();
            tempBean.setShopId( this.shopId );
            tempBean.setYear( this.year );
            tempBean.setMonth( index+1 );
            
            rowIndex++;
            
            tempBean.setResultTechnic( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultItem( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultIn( this.getCellValue( rowIndex++, colIndex ) );
            
            rowIndex++;
            
            tempBean.setResultNew( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultK( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultSET( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultS( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultHD( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultP( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultSTP( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultTR( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultETC( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultCRM( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setResultMON( this.getCellValue( rowIndex++, colIndex ) );
                        
            rowIndex++;
            
            tempBean.setTargetTechnic( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetItem( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetIn( this.getCellValue( rowIndex++, colIndex ) );
            
            rowIndex++;
            
            tempBean.setTargetNew( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetK( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetSET( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetS( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetHD( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetP( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetSTP( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetTR( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetETC( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetCRM( this.getCellValue( rowIndex++, colIndex ) );
            tempBean.setTargetMON( this.getCellValue( rowIndex++, colIndex ) );
            
            tempBean.setOpenDays( this.getCellValue( rowIndex++, colIndex ) );
            
            //”„ãŽÀÑ = ‹ZpŽÀÑ@{@¤•iŽÀÑ
            tempBean.setResultTotal( tempBean.getResultTechnic() + tempBean.getResultItem() );            
            //“ü‹qŽÀÑ = ‹ZpŽÀÑ@/ “ü‹qŽÀÑ
            if( ( tempBean.getResultTechnic() > 0 ) && ( tempBean.getResultIn() > 0 ) )
            {
                tempBean.setResultAvgAmount( NumberUtil.round((double)tempBean.getResultTechnic() / (double)tempBean.getResultIn()) );
            }
            //”„ã–Ú•W = ‹Zp–Ú•W@{ ¤•i–Ú•W
            tempBean.setTargetTotal( tempBean.getTargetTechnic() + tempBean.getTargetItem() );        
            //“ü‹q–Ú•W = ‹Zp–Ú•W@/ “ü‹q–Ú•W
            if( ( tempBean.getTargetTechnic() > 0 ) && ( tempBean.getTargetIn() > 0 ) )
            {
                tempBean.setTargetAvgAmount( NumberUtil.round((double)tempBean.getTargetTechnic() / (double)tempBean.getTargetIn()) );
            }
            this.dataTargetResultList.set( index, tempBean );
            
            colIndex++;
        }
    }
    
    protected String getCellValue( int row, int col )
    {
        TableModel tableModel = this.tableAllInfo.getModel();
        Object value = tableModel.getValueAt( row, col );
        if( value == null )
        {
            return null;
        }
        else
        {
            return value.toString();
        }
    }
        
    protected void calculateTotal()
    {
        DataTargetResultBean tempBean = null;
        this.dataTargetResultTotal.resetValues();
        
        for( int index=0; index < this.MONTHS_COUNT; index++ )
        {
            tempBean = this.dataTargetResultList.get( index );
            
            if( tempBean != null )
            {
                this.dataTargetResultTotal.setResultTotal( dataTargetResultTotal.getResultTotal() + tempBean.getResultTotal() );
                this.dataTargetResultTotal.setResultTechnic( dataTargetResultTotal.getResultTechnic() + tempBean.getResultTechnic() );
                this.dataTargetResultTotal.setResultItem( dataTargetResultTotal.getResultItem() + tempBean.getResultItem() );
                this.dataTargetResultTotal.setResultIn( dataTargetResultTotal.getResultIn() + tempBean.getResultIn() );
                this.dataTargetResultTotal.setResultAvgAmount( dataTargetResultTotal.getResultAvgAmount() + tempBean.getResultAvgAmount() );
                this.dataTargetResultTotal.setResultNew( dataTargetResultTotal.getResultNew() + tempBean.getResultNew() );
                this.dataTargetResultTotal.setResultK( dataTargetResultTotal.getResultK() + tempBean.getResultK() );
                this.dataTargetResultTotal.setResultSET( dataTargetResultTotal.getResultSET() + tempBean.getResultSET() );
                this.dataTargetResultTotal.setResultS( dataTargetResultTotal.getResultS() + tempBean.getResultS() );
                this.dataTargetResultTotal.setResultHD( dataTargetResultTotal.getResultHD() + tempBean.getResultHD() );
                this.dataTargetResultTotal.setResultP( dataTargetResultTotal.getResultP() + tempBean.getResultP() );
                this.dataTargetResultTotal.setResultSTP( dataTargetResultTotal.getResultSTP() + tempBean.getResultSTP() );
                this.dataTargetResultTotal.setResultTR( dataTargetResultTotal.getResultTR() + tempBean.getResultTR() );
                this.dataTargetResultTotal.setResultETC( dataTargetResultTotal.getResultETC() + tempBean.getResultETC() );
                this.dataTargetResultTotal.setResultCRM( dataTargetResultTotal.getResultCRM() + tempBean.getResultCRM() );
                this.dataTargetResultTotal.setResultMON( dataTargetResultTotal.getResultMON() + tempBean.getResultMON() );

                this.dataTargetResultTotal.setTargetTotal( dataTargetResultTotal.getTargetTotal() + tempBean.getTargetTotal() );
                this.dataTargetResultTotal.setTargetTechnic( dataTargetResultTotal.getTargetTechnic() + tempBean.getTargetTechnic() );
                this.dataTargetResultTotal.setTargetItem( dataTargetResultTotal.getTargetItem() + tempBean.getTargetItem() );
                this.dataTargetResultTotal.setTargetIn( dataTargetResultTotal.getTargetIn() + tempBean.getTargetIn() );
                this.dataTargetResultTotal.setTargetAvgAmount( dataTargetResultTotal.getTargetAvgAmount() + tempBean.getTargetAvgAmount() );
                this.dataTargetResultTotal.setTargetNew( dataTargetResultTotal.getTargetNew() + tempBean.getTargetNew() );
                this.dataTargetResultTotal.setTargetK( dataTargetResultTotal.getTargetK() + tempBean.getTargetK() );
                this.dataTargetResultTotal.setTargetSET( dataTargetResultTotal.getTargetSET() + tempBean.getTargetSET() );
                this.dataTargetResultTotal.setTargetS( dataTargetResultTotal.getTargetS() + tempBean.getTargetS() );
                this.dataTargetResultTotal.setTargetHD( dataTargetResultTotal.getTargetHD() + tempBean.getTargetHD() );
                this.dataTargetResultTotal.setTargetP( dataTargetResultTotal.getTargetP() + tempBean.getTargetP() );
                this.dataTargetResultTotal.setTargetSTP( dataTargetResultTotal.getTargetSTP() + tempBean.getTargetSTP() );
                this.dataTargetResultTotal.setTargetTR( dataTargetResultTotal.getTargetTR() + tempBean.getTargetTR() );
                this.dataTargetResultTotal.setTargetETC( dataTargetResultTotal.getTargetETC() + tempBean.getTargetETC() );
                this.dataTargetResultTotal.setTargetCRM( dataTargetResultTotal.getTargetCRM() + tempBean.getTargetCRM() );
                this.dataTargetResultTotal.setTargetMON( dataTargetResultTotal.getTargetMON() + tempBean.getTargetMON() );

                this.dataTargetResultTotal.setOpenDays( dataTargetResultTotal.getOpenDays()+ tempBean.getOpenDays() );
            }
        }
        return;
    }
        
    private boolean initTableHeaderMonths()
    {
        this.tableAllInfo.setBorder( LineBorder.createBlackLineBorder() );
        TableModel tableModel = this.tableAllInfo.getModel();
        
        int colIndex = 0;
        int monthNum = 1;
        
        String columnVal = "";
        tableModel.setValueAt( columnVal, 0, colIndex++ );
        tableModel.setValueAt( columnVal, 0, colIndex );
        
        for( colIndex = 2; colIndex < this.tableAllInfo.getColumnCount()-1; colIndex ++ )
        {
            columnVal = monthNum++ + "ŒŽ";
            tableModel.setValueAt( columnVal, 0, colIndex );
        }
        columnVal = "‡Œv";
        tableModel.setValueAt( columnVal, 0, colIndex );
        return true;
    }
    
    private boolean initSecondColumn()
    {   
        int rowIndex = 0;
        int colIndex = 1;
        
        TableModel tableModel = this.tableAllInfo.getModel();        
        
        tableModel.setValueAt( "", rowIndex++, 1 );
        tableModel.setValueAt( "”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "‹Zp", rowIndex++, 1 );
        tableModel.setValueAt( "¤•i", rowIndex++, 1 );
        tableModel.setValueAt( "“ü‹q", rowIndex++, 1 );
        tableModel.setValueAt( "‹q’P‰¿", rowIndex++, 1 );
        tableModel.setValueAt( "V‹K‹q", rowIndex++, 1 );
        tableModel.setValueAt( "K‹q", rowIndex++, 1 );
        tableModel.setValueAt( "SET‹q", rowIndex++, 1 );
        tableModel.setValueAt( "S‹q", rowIndex++, 1 );
        tableModel.setValueAt( "HD‹q", rowIndex++, 1 );
        tableModel.setValueAt( "P‹q", rowIndex++, 1 );
        tableModel.setValueAt( "STP‹q", rowIndex++, 1 );
        tableModel.setValueAt( "TR‹q", rowIndex++, 1 );
        tableModel.setValueAt( "ETC‹q", rowIndex++, 1 );
        tableModel.setValueAt( "CRM‹q", rowIndex++, 1 );
        tableModel.setValueAt( "MON‹q", rowIndex++, 1 );
        tableModel.setValueAt( "”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "‹Zp", rowIndex++, 1 );
        tableModel.setValueAt( "¤•i", rowIndex++, 1 );
        tableModel.setValueAt( "“ü‹q", rowIndex++, 1 );
        tableModel.setValueAt( "‹q’P‰¿", rowIndex++, 1 );
        tableModel.setValueAt( "V‹K‹q", rowIndex++, 1 );
        tableModel.setValueAt( "K‹q", rowIndex++, 1 );
        tableModel.setValueAt( "SET‹q", rowIndex++, 1 );
        tableModel.setValueAt( "S‹q", rowIndex++, 1 );
        tableModel.setValueAt( "HD‹q", rowIndex++, 1 );
        tableModel.setValueAt( "P‹q", rowIndex++, 1 );
        tableModel.setValueAt( "STP‹q", rowIndex++, 1 );
        tableModel.setValueAt( "TR‹q", rowIndex++, 1 );
        tableModel.setValueAt( "ETC‹q", rowIndex++, 1 );
        tableModel.setValueAt( "CRM‹q", rowIndex++, 1 );
        tableModel.setValueAt( "MON‹q", rowIndex++, 1 );
        
        tableModel.setValueAt( "ŽÀÑ", 8, 0 );
        tableModel.setValueAt( "–Ú•W", 25, 0 );
        
        tableModel.setValueAt( "‰Ò“­“ú”", 33, 0 );
        
        return true;
    }

    public boolean isCellEdited()
    {
        this.stopCellEditing();
        // in calling tableAllInfo.getColumnClass() method, you can use column numbers 2-13
        return ((TableCellEditorEx)this.tableAllInfo.getDefaultEditor( this.tableAllInfo.getColumnClass(3))).isCellEdited();
    }
    
    public boolean stopCellEditing()
    {
        boolean bEditingStopped = true;
        
        if( this.tableAllInfo.isEditing() )
        {
            int editCol = this.tableAllInfo.getEditingColumn();
            int editRow = this.tableAllInfo.getEditingRow();            
            if( ((TableCellEditorEx)this.tableAllInfo.getCellEditor( editRow, editCol )).stopCellEditing() == false )
            {
                MessageDialog.showMessageDialog( this, 
                        "ƒe[ƒuƒ‹‚Å—\Šú‚¹‚ÊƒGƒ‰[‚ª”­¶‚µ‚Ü‚µ‚½B", 
                        "", JOptionPane.OK_OPTION );
            }
        }
        return bEditingStopped;
    }
    
    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        tableAllInfo = new javax.swing.JTable();
        tableAllInfo = new javax.swing.JTable(new DefaultTableModel())
        {
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                if( ( rowIndex == 0 ) || ( colIndex == 0 ) || ( colIndex == 1 ) )
                return false;   //Disallow the editing of any cell
                else if( ( rowIndex == 1 ) || (rowIndex == 5) || (rowIndex == 17) || (rowIndex == 21) )
                {
                    return false;
                }
                else if( colIndex == 14 )
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        setNextFocusableComponent(null);
        getAccessibleContext().setAccessibleName("985");
        getAccessibleContext().setAccessibleDescription("558");
        tableAllInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String []
            {
                "ƒ^ƒCƒgƒ‹ 1", "ƒ^ƒCƒgƒ‹ 2", "ƒ^ƒCƒgƒ‹ 3", "ƒ^ƒCƒgƒ‹ 4", "ƒ^ƒCƒgƒ‹ 5", "ƒ^ƒCƒgƒ‹ 6", "ƒ^ƒCƒgƒ‹ 7", "ƒ^ƒCƒgƒ‹ 8", "ƒ^ƒCƒgƒ‹ 9", "ƒ^ƒCƒgƒ‹ 10", "ƒ^ƒCƒgƒ‹ 11", "ƒ^ƒCƒgƒ‹ 12", "ƒ^ƒCƒgƒ‹ 13", "ƒ^ƒCƒgƒ‹ 14", "ƒ^ƒCƒgƒ‹ 15"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        tableAllInfo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tableAllInfo.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        tableAllInfo.setAutoscrolls(false);
        tableAllInfo.setCellSelectionEnabled(true);
        tableAllInfo.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableAllInfo.setShowHorizontalLines(false);
        tableAllInfo.setShowVerticalLines(false);
        tableAllInfo.setTableHeader(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableAllInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 983, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tableAllInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(165, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable tableAllInfo;
    // End of variables declaration//GEN-END:variables

    private class TableRendererEx extends DefaultTableCellRenderer {
        private DecimalFormat format;
        private DecimalFormatSymbols formatSymbols;
        private boolean editableCell = false;
        
        public TableRendererEx() {
            this.format = new DecimalFormat();
            this.formatSymbols = new DecimalFormatSymbols();
            this.formatSymbols.setGroupingSeparator( ',' );
            this.format.setGroupingSize( 3 );
            this.format.setDecimalFormatSymbols( this.formatSymbols );
        }
        
        public Component getTableCellRendererComponent( JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column ) {
            
            if( value != null ) {
                String temp = value.toString();
                boolean printStr = CheckUtil.isNumeric(temp);
                
                if( printStr == true ) {
                    value = format.format( Integer.valueOf( temp ) );
                }
                if( ( row > 0 ) && ( column > 1 ) ) {
                    this.setFont( new Font( null, Font.PLAIN, 9 ) );
                } else {
                    this.setFont( new Font( null, Font.PLAIN, 11 ) );
                }
            }
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            Color background;
            if( isSelected ) {
                background = new Color( 64, 64, 64 );
            }else{
                background = new Color( 224, 224, 224 );
            }
            int horizAlignment = SwingConstants.CENTER;
            Border border = BorderFactory.createMatteBorder( 1, 1, 0, 0, Color.GRAY );
            
            
            if( ( row == MONTHS_LABEL_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                border = BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.GRAY );
            } else if( ( row == LAST_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                border = BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.GRAY );
            } else if( ( column == 0 ) && ( row > 1 && row < 17 ) ) {
                border = BorderFactory.createMatteBorder( 0, 1, 0, 0, Color.GRAY );
            } else if( ( column == TYPE_LABEL_COLUMN ) && ( row > 17 && row < LAST_ROW ) ) {
                border = BorderFactory.createMatteBorder( 0, 1, 0, 0, Color.GRAY );
            } else if( ( row == MONTHS_LABEL_ROW ) || ( column == TYPE_LABEL_COLUMN ) || ( column == FIELDS_LABEL_COLUMN ) ) {
            } else if( ( row == ACTUAL_TOTAL_SALES_ROW )|| ( row == ESTIMATE_TOTAL_SALES_ROW ) ||
                    ( row == ACTUAL_CUSTOMERS_AVE ) || ( row == ESTIMATE_CUSTOMERS_AVE ) ) {
                if( isSelected ) {
                    if( hasFocus ) {
                        background = new Color(128, 0, 0);
                    }else{
                        background = new Color(192, 64, 64);
                    }
                }else{
                    background = Color.ORANGE;
                }
                horizAlignment = SwingConstants.RIGHT;
            } else {
                if( isSelected ) {
                    if( hasFocus ) {
                        background = new Color(0, 0, 128);
                    }else{
                        background = new Color(64, 64, 192);
                    }
                }else{
                    background = Color.WHITE;
                }
                horizAlignment = SwingConstants.RIGHT;
                editableCell = true;
            }
            
            
            
            //set the values:
            this.setBorder( border );
            this.setHorizontalAlignment( horizAlignment );
            this.setBackground( background );
            this.setVisible( true );
            
            return this;
            
        } //end of getTableCellRendererComponent()
        
    } //end of class
    
    private class TableCellEditorEx extends DefaultCellEditor {
        
        protected boolean bCellEdited = false;
        protected JTable tblEditing;
        
        /** Creates a new instance of TableCellEditorEx */
        public TableCellEditorEx() {
            super( new JFormattedTextField() );
        }
        
        //Implement the one method defined by TableCellEditor.
        public Component getTableCellEditorComponent( JTable table,
                Object value, boolean isSelected,int row,int column ) {
            JFormattedTextField textField = (JFormattedTextField)super.getComponent();
            PlainDocument temp = (PlainDocument)textField.getDocument();
            
            if( ( row == MONTHS_LABEL_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( row == LAST_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( column == 0 ) && ( row > 1 && row < 17 ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( column == TYPE_LABEL_COLUMN ) && ( row > 17 && row < LAST_ROW ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( row == MONTHS_LABEL_ROW ) || ( column == TYPE_LABEL_COLUMN ) || ( column == FIELDS_LABEL_COLUMN ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( row == ACTUAL_TOTAL_SALES_ROW )|| ( row == ESTIMATE_TOTAL_SALES_ROW ) ||
                    ( row == ACTUAL_CUSTOMERS_AVE ) || ( row == ESTIMATE_CUSTOMERS_AVE ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else {
                temp.setDocumentFilter(new CustomFilter(8,CustomFilter.NUMBER));
            }
            
            textField.setDocument( temp );
            if( value != null ){
                textField.setText( value.toString() );
            }else{
                textField.setText( "" );
            }
            textField.setFont( new Font( null, Font.PLAIN, 9 ) );
            
            this.tblEditing = table;
            
            return textField;
        }
        
        public boolean isCellEdited() {
            return this.bCellEdited;
        }
        
        public void resetCellEditedFlag() {
            this.bCellEdited = false;
        }
        
        private String getValueAt(int row, int col){
            if( tblEditing.getValueAt(row, col) == null ){
                return "";
            }else{
                return tblEditing.getValueAt(row, col).toString();
            }
        }
        
        public boolean stopCellEditing() {
            int row, col;
            int intCell1;
            int intCell2;
            int intSetVal;
            int sum;
            boolean bDraw = false;
            
            row = tblEditing.getEditingRow();
            col = tblEditing.getEditingColumn();
            
            super.stopCellEditing();
            
            setSum(row);
            
            int editNum;
            if( CheckUtil.isNumeric(getCellEditorValue().toString()) ){
                editNum = Integer.parseInt(getCellEditorValue().toString());
            }
            
            if ( row == ACTUAL_TECHNIQUE_ROW || row == ACTUAL_COMMODITY_ROW ) {
                String tmp1 = getValueAt(ACTUAL_TECHNIQUE_ROW, col);
                String tmp2 = getValueAt(ACTUAL_COMMODITY_ROW, col);
                
                if( CheckUtil.isNumeric(tmp1) ){
                    bDraw = true;
                    intCell1 = Integer.parseInt(tmp1);
                }else{
                    intCell1 = 0;
                }
                if( CheckUtil.isNumeric(tmp2) ){
                    bDraw = true;
                    intCell2 = Integer.parseInt(tmp2);
                }else{
                    intCell2 = 0;
                }
                intSetVal = intCell1 + intCell2;
                
                if( bDraw ){
                    tblEditing.setValueAt(intSetVal, ACTUAL_TOTAL_SALES_ROW, col);
                }else{
                    tblEditing.setValueAt(null, ACTUAL_TOTAL_SALES_ROW, col);
                }
                
                setSum(ACTUAL_TOTAL_SALES_ROW);
                if ( row == ACTUAL_TECHNIQUE_ROW ){
                    tmp2 = getValueAt(ACTUAL_CUSTOMERCNT_ROW, col);
                    intCell2 = 0;
                    if( CheckUtil.isNumeric(tmp2) ){
                        intCell2 = Integer.parseInt(getValueAt(ACTUAL_CUSTOMERCNT_ROW, col));
                    }
                    if( intCell2 != 0 ){
                        intSetVal = NumberUtil.round( (float)intCell1 / (float)intCell2 );
                        tblEditing.setValueAt(intSetVal, ACTUAL_CUSTOMERS_AVE, col);
                    }else{
                        tblEditing.setValueAt(null, ACTUAL_CUSTOMERS_AVE, col);
                    }
                    setSum( ACTUAL_CUSTOMERS_AVE );
                }
            } else if (row == ACTUAL_CUSTOMERCNT_ROW) {
                String tmp1 = getValueAt(ACTUAL_CUSTOMERCNT_ROW, col);
                
                if( CheckUtil.isNumeric(tmp1) ){
                    intCell1 = Integer.parseInt(tmp1);
                    if( intCell1 != 0 ){
                        String tmp2 = getValueAt(ACTUAL_TECHNIQUE_ROW, col);
                        if( CheckUtil.isNumeric(tmp2) ){
                            intCell2 = Integer.parseInt(tmp2);
                        }else{
                            intCell2 = 0; 
                        }
                        bDraw = true;
                        intSetVal = NumberUtil.round( (float)intCell2 / (float)intCell1 );
                        tblEditing.setValueAt(intSetVal, ACTUAL_CUSTOMERS_AVE, col);
                    }
                }
                
                if( !bDraw ){
                    tblEditing.setValueAt(null, ACTUAL_CUSTOMERS_AVE, col);
                }
                setSum(ACTUAL_CUSTOMERS_AVE);
                
            }else if ( row == ESTIMATE_TECHNIQUE_ROW || row == ESTIMATE_COMMODITY_ROW ) {
                String tmp1 = getValueAt(ESTIMATE_TECHNIQUE_ROW, col);
                String tmp2 = getValueAt(ESTIMATE_COMMODITY_ROW, col);
                
                if( CheckUtil.isNumeric(tmp1) ){
                    bDraw = true;
                    intCell1 = Integer.parseInt(tmp1);
                }else{
                    intCell1 = 0;
                }
                if( CheckUtil.isNumeric(tmp2) ){
                    bDraw = true;
                    intCell2 = Integer.parseInt(tmp2);
                }else{
                    intCell2 = 0;
                }
                intSetVal = intCell1 + intCell2;
                
                if( bDraw ){
                    tblEditing.setValueAt(intSetVal, ESTIMATE_TOTAL_SALES_ROW, col);
                }else{
                    tblEditing.setValueAt(null, ESTIMATE_TOTAL_SALES_ROW, col);
                }
                
                setSum(ESTIMATE_TOTAL_SALES_ROW);
                if ( row == ESTIMATE_TECHNIQUE_ROW ){
                    tmp2 = getValueAt(ESTIMATE_CUSTOMERCNT_ROW, col);
                    intCell2 = 0;
                    if( CheckUtil.isNumeric(tmp2) ){
                        intCell2 = Integer.parseInt(getValueAt(ESTIMATE_CUSTOMERCNT_ROW, col));
                    }
                    if( intCell2 != 0 ){
                        intSetVal = NumberUtil.round( (float)intCell1 / (float)intCell2 );
                        tblEditing.setValueAt(intSetVal, ESTIMATE_CUSTOMERS_AVE, col);
                    }else{
                        tblEditing.setValueAt(null, ESTIMATE_CUSTOMERS_AVE, col);
                    }
                    setSum( ESTIMATE_CUSTOMERS_AVE );
                }
            } else if (row == ESTIMATE_CUSTOMERCNT_ROW) {
                String tmp1 = getValueAt(ESTIMATE_CUSTOMERCNT_ROW, col);
                
                if( CheckUtil.isNumeric(tmp1) ){
                    intCell1 = Integer.parseInt(tmp1);
                    if( intCell1 != 0 ){
                        String tmp2 = getValueAt(ESTIMATE_TECHNIQUE_ROW, col);
                        if( CheckUtil.isNumeric(tmp2) ){
                            intCell2 = Integer.parseInt(tmp2);
                        }else{
                            intCell2 = 0; 
                        }
                        bDraw = true;
                        intSetVal = NumberUtil.round( (float)intCell2 / (float)intCell1 );
                        tblEditing.setValueAt(intSetVal, ESTIMATE_CUSTOMERS_AVE, col);
                    }
                }
                
                if( !bDraw ){
                    tblEditing.setValueAt(null, ESTIMATE_CUSTOMERS_AVE, col);
                }
                setSum(ESTIMATE_CUSTOMERS_AVE);                
            }
            
            bCellEdited = true;
            return true;
        }
        
        private void setSum(int row) {
            int sum = 0;
            boolean bDraw = false;
            
            for (int i = JANUARY_COLUMN; i < SUM_COLUMN; ++i) {
                if( CheckUtil.isNumeric(getValueAt(row, i)) ){
                    bDraw = true;
                    sum += Integer.parseInt(getValueAt(row, i));
                }
            }
            
            if( bDraw ){
                tblEditing.setValueAt(sum, row, SUM_COLUMN);
            }else{
                tblEditing.setValueAt(null, row, SUM_COLUMN);
            }
        }
        
    }
    
}

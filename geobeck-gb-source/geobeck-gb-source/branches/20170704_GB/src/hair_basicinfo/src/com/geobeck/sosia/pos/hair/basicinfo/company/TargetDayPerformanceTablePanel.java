/*
 * TargetActualInfoTablePanel.java
 *
 * Created on 2008/09/19, 15:41
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.NumberUtil;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.PlainDocument;
/**
 *
 * @author  shiera.delusa
 */
public class TargetDayPerformanceTablePanel extends javax.swing.JPanel
{
    //static final protected int MONTHS_COUNT = 12;
    static final protected int MONTHS_COUNT = 12;
    static final protected int LAST_READONLY_COL = 1;
    static final protected int FIRST_EDITABLE_ROW = 1;
    static final protected int TOTAL_INFO_COLUMN = 14;
    static final protected int LAST_ROW_INDEX = 58;

    private static final int MONTHS_LABEL_ROW = 0;
    private static final int ACTUAL_TOTAL_SALES_ROW = 1;
    private static final int ACTUAL_TECHNIQUE_ROW = 2;
    private static final int ACTUAL_COMMODITY_ROW = 3;
    private static final int ACTUAL_CUSTOMERCNT_ROW = 4;
    private static final int ACTUAL_CUSTOMERS_AVE = 5;
    private static final int ESTIMATE_TOTAL_SALES_ROW = 21;
    private static final int ESTIMATE_TECHNIQUE_ROW = 22;
    private static final int ESTIMATE_COMMODITY_ROW = 23;
    private static final int ESTIMATE_CUSTOMERCNT_ROW = 24;
    private static final int ESTIMATE_CUSTOMERS_AVE = 25;
    private static final int LAST_ROW = 52;

    private static final int TYPE_LABEL_COLUMN = 0;
    private static final int FIELDS_LABEL_COLUMN = 1;
    private static final int JANUARY_COLUMN = 2;
    
    private static final int TECHNIC_ROW = 27;
    private static final int ITEM_ROW = 28;
    private static final int TOTAL_SALES_ROW = 29;
    private static final int COTA_SALES_ROW = 30;
    private static final int CUT_NUM_ROW = 31;
    private static final int CUT_SALES_ROW = 32;
    private static final int PERM_NUM_ROW = 33;
    private static final int PERM_SALES_ROW = 34;
    private static final int COL_NUM_ROW = 35;
    private static final int COL_SALES_ROW = 36;
    private static final int SPA_NUM_ROW = 37;
    private static final int SPA_SALES_ROW = 38;
    private static final int UN_NUM_ROW = 46;
    private static final int STREAT_NUM_ROW = 39;
    private static final int STREAT_SALES_ROW = 40;
    private static final int OTHER_REVENUE_ROW = 41;
    private static final int ITEM_NUM_ROW = 42;
    private static final int Y_NUM_ROW = 43;
    private static final int M_NUM_ROW = 44;
    private static final int O_NUM_ROW = 45;
    private static final int NEW_NUM_ROW = 46;
    private static final int NEW_REPERT_NUM = 47;
    private static final int USALLY_NUM = 48;
    private static final int TOTAL_NUM_CUS = 49;
    private static final int INTRO_NUM_ROW = 50;
    private static final int NOMINAT_NUM_ROW = 51;
    private static final int OPEN_DAY_ROW = 52;
    private static final int E_STYLIST_ROW = 53;
    private static final int E_ASSISTANT_ROW = 54;
    private static final int AR_STYLIST_ROW = 55;
    private static final int AR_ASSISTANT_ROW = 56;
    private static final int AR_OTHER_ROW = 57;
    private static final int TOTAL_NUM_EMPLOYEE_ROW = 58;
    static final protected int FIRST_EDITABLE_ROW_OX = 26;
    private static final int DAY_COLUMN = 2;
    //class variables
    private int year;
    private int month;
    private int dayOfMonth = 28;
    private java.util.Date targetDay = null;
    private Integer shopId;
    private ListDataTargetOX listmonth = new ListDataTargetOX();
    private ListDataTargetDayOX listDataTargetDayOX = new ListDataTargetDayOX();
    private ArrayList<DataTargetDayOX> listItemTargetDay = new ArrayList<DataTargetDayOX>();
    private long totalTechnic = 0;
    private long totalTechnicOX = 0;
    private long totalItem = 0;
    private long totalItemOX = 0;
    private long totalSales = 0;
    private long totalSalesOX = 0;
    private long totalCota = 0;
    private long totalCotaOX = 0;
    private long totalCutNum = 0;
    private long totalCutNumOX = 0;
    private long totalCutSales = 0;
    private long totalCutSalesOX = 0;
    private long totalPermNum = 0;
    private long totalPermNumOX = 0;
    private long totalPermSale = 0;
    private long totalPermSaleOX = 0;
    private long totalColNum = 0;
    private long totalColNumOX = 0;
    private long totalColSales = 0;
    private long totalColSalesOX = 0;
    private long totalSpaNum = 0;
    private long totalSpaNumOX = 0;
    private long totalSpaSales = 0;
    private long totalSpaSalesOX = 0;
    private long totalTreatNum = 0;
    private long totalTreatNumOX = 0;
    private long totalTreatSales = 0;
    private long totalTreatSalesOX = 0;
    private long totalOtherRevenue = 0;
    private long totalOtherRevenueOX = 0;
    private long totalItemNum = 0;
    private long totalItemNumOX = 0;
    private long totalyNum = 0;
    private long totalyNumOX = 0;
    private long totalmNum = 0;
    private long totalmNumOX = 0;
    private long totaloNum = 0;
    private long totaloNumOX = 0;
    private long totalUnNum = 0;
    private long totalUnNumOX = 0;
    private long totalNewNum = 0;
    private long totalNewNumOX = 0;
    private long totalUsallyNum = 0;
    private long totalUsallyNumOX = 0;
    private long totalNewRepertNum = 0;
    private long totalNewRepertNumOX = 0;
    private long totalNumCustomer = 0;
    private long totalNumCustomerOX = 0;
    private long totalIntroNum = 0;
    private long totalIntroNumOX = 0;
    private long totalNominatNum = 0;
    private long totalNominatNumOX = 0;
    private long totalOpenDayOX = 0;
    private long totaleStylistOX = 0;
    private long totaleAssistantOX = 0;
    private long totalarStylistOX = 0;
    private long totalarAssistantOX = 0;
    private long totalarOtherOX = 0;
    private long columTotalOX = 0;

    // HashMap Data
    // Product
    private HashMap hashTechnic = new HashMap();
    private HashMap hashItem = new HashMap();
    private HashMap hashNominat = new HashMap();
    // Cota
    private HashMap hashCota = new HashMap();
    // Data Sales Detail
    private HashMap hashCutNum = new HashMap();
    private HashMap hashCutSales = new HashMap();
    private HashMap hashPermNum = new HashMap();
    private HashMap hashPermSales = new HashMap();
    private HashMap hashColNum = new HashMap();
    private HashMap hashColSales = new HashMap();
    private HashMap hashSpaNum = new HashMap();
    private HashMap hashSpaSales = new HashMap();
    private HashMap hashTreatNum = new HashMap();
    private HashMap hashTreatSales = new HashMap();
    private HashMap hashItemNum = new HashMap();
    // Customer Sales
    private HashMap hashYNum = new HashMap();
    private HashMap hashMNum = new HashMap();
    private HashMap hashONum = new HashMap();
    private HashMap hashUNNum = new HashMap();
    // New Num
    private HashMap hashNewNum = new HashMap();
    // UsallyNum
    private HashMap hashUsallyNum = new HashMap();
    // NewRepertNum
    private HashMap hashNewRepertNum = new HashMap();
    private HashMap hashIntroNum = new HashMap();
    private HashMap totalDataSales = new HashMap();
    // End HashMap Data
    
    /** Creates new form TargetActualInfoTablePanel */
    public TargetDayPerformanceTablePanel()
    {
        this.setSize( 1000, 970 );
        initComponents();
        this.tableAllInfo.setDefaultRenderer( Object.class, new TableRendererEx() );
        this.tableAllInfo.setDefaultEditor( Object.class, new TableCellEditorEx() );
        
    }

    public void setTargetDay(java.util.Date targetDay){
        this.targetDay = targetDay;
    }

    public java.util.Date getTargetDay(){
        return this.targetDay;
    }

    private void resetTotal(){
        this.totalTechnic           = 0;
        this.totalTechnicOX         = 0;
        this.totalItem              = 0;
        this.totalItemOX            = 0;
        this.totalSales             = 0;
        this.totalSalesOX           = 0;
        this.totalCota              = 0;
        this.totalCotaOX            = 0;
        this.totalCutNum            = 0;
        this.totalCutNumOX          = 0;
        this.totalCutSales          = 0;
        this.totalCutSalesOX        = 0;
        this.totalPermNum           = 0;
        this.totalPermNumOX         = 0;
        this.totalPermSale          = 0;
        this.totalPermSaleOX        = 0;
        this.totalColSales          = 0;
        this.totalColSalesOX        = 0;
        this.totalSpaNum            = 0;
        this.totalSpaNumOX          = 0;
        this.totalSpaSales          = 0;
        this.totalSpaSalesOX        = 0;
        this.totalTreatNum          = 0;
        this.totalTreatNumOX        = 0;
        this.totalTreatSales        = 0;
        this.totalTreatSalesOX      = 0;
        this.totalOtherRevenue      = 0;
        this.totalOtherRevenueOX    = 0;
        this.totalItemNum           = 0;
        this.totalItemNumOX         = 0;
        this.totalyNum              = 0;
        this.totalyNumOX            = 0;
        this.totalmNum              = 0;
        this.totalmNumOX            = 0;
        this.totalUnNum             = 0;
        this.totalUnNumOX           = 0;
        this.totalNewNum            = 0;
        this.totalNewNumOX          = 0;
        this.totalUsallyNum         = 0;
        this.totalUsallyNumOX       = 0;
        this.totalNewRepertNum      = 0;
        this.totalNewRepertNumOX    = 0;
        this.totalNumCustomer       = 0;
        this.totalNumCustomerOX     = 0;
        this.totalIntroNum          = 0;
        this.totalIntroNumOX        = 0;
        this.totalNominatNum        = 0;
        this.totalOpenDayOX         = 0;
        this.totaleStylistOX        = 0;
        this.totaleAssistantOX      = 0;
        this.totalarStylistOX       = 0;
        this.totalarAssistantOX     = 0;
        this.totalarOtherOX         = 0;
        this.columTotalOX           = 0;
    }

    public void resetHash() {
        this.hashTechnic        = new HashMap();
        this.hashItem           = new HashMap();
        this.hashNominat        = new HashMap();
        this.hashCota           = new HashMap();
        this.hashCutNum         = new HashMap();
        this.hashCutSales       = new HashMap();
        this.hashPermNum        = new HashMap();
        this.hashPermSales      = new HashMap();
        this.hashColNum         = new HashMap();
        this.hashColSales       = new HashMap();
        this.hashSpaNum         = new HashMap();
        this.hashSpaSales       = new HashMap();
        this.hashTreatNum       = new HashMap();
        this.hashTreatSales     = new HashMap();
        this.hashItemNum        = new HashMap();
        this.hashYNum           = new HashMap();
        this.hashMNum           = new HashMap();
        this.hashONum           = new HashMap();
        this.hashUNNum          = new HashMap();
        this.hashNewNum         = new HashMap();
        this.hashUsallyNum      = new HashMap();
        this.hashNewRepertNum   = new HashMap();
        this.hashIntroNum       = new HashMap();
        // Total HashMap
        this.totalDataSales     = new HashMap();
    }
    
    private void init(){
        listmonth           = new ListDataTargetOX();
        listItemTargetDay   = new ArrayList<DataTargetDayOX>();
        Calendar cal        = Calendar.getInstance();
        cal.setTime(this.targetDay);
        this.year           = cal.get(Calendar.YEAR);
        this.month          = cal.get(Calendar.MONTH);
        this.dayOfMonth     = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.initTableHeaderMonths();
        this.initSecondColumn();
        resetTotal();
        resetHash();
    }

    private boolean getAllData(){
        boolean result = false;
        try{
            listDataTargetDayOX = new ListDataTargetDayOX();
            listDataTargetDayOX.setShopId(this.shopId);
            listDataTargetDayOX.setTargetDay(this.dayOfMonth);
            listDataTargetDayOX.setTargetMonth(this.month+1);
            listDataTargetDayOX.setTargetYear(this.year);
            listDataTargetDayOX.setDayOfMonth(this.dayOfMonth);
            result = listDataTargetDayOX.LoadAllData();
            listItemTargetDay = listDataTargetDayOX.loadAllTargetDataDayOX(SystemInfo.getConnection());
            if(result){
                setDataToHashMap();
                ShowData();
            }
        }catch(Exception e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return result;
    }

    private void setDataToHashMap(){
        this.hashTechnic        = listDataTargetDayOX.getHashTechnic();
        this.hashItem           = listDataTargetDayOX.getHashItem();
        this.hashNominat        = listDataTargetDayOX.getHashNominat();
        this.hashCota           = listDataTargetDayOX.getHashCota();
        this.hashCutNum         = listDataTargetDayOX.getHashCutNum();
        this.hashCutSales       = listDataTargetDayOX.getHashCutSales();
        this.hashPermNum        = listDataTargetDayOX.getHashPermNum();
        this.hashPermSales      = listDataTargetDayOX.getHashPermSales();
        this.hashColNum         = listDataTargetDayOX.getHashColNum();
        this.hashColSales       = listDataTargetDayOX.getHashColSales();
        this.hashSpaNum         = listDataTargetDayOX.getHashSpaNum();
        this.hashSpaSales       = listDataTargetDayOX.getHashSpaSales();
        this.hashTreatNum       = listDataTargetDayOX.getHashTreatNum();
        this.hashTreatSales     = listDataTargetDayOX.getHashTreatSales();
        this.hashItemNum        = listDataTargetDayOX.getHashItemNum();
        this.hashYNum           = listDataTargetDayOX.getHashYNum();
        this.hashMNum           = listDataTargetDayOX.getHashMNum();
        this.hashONum           = listDataTargetDayOX.getHashONum();
        this.hashUNNum          = listDataTargetDayOX.getHashUnNum();
        this.hashNewNum         = listDataTargetDayOX.getHashNewNum();
        this.hashUsallyNum      = listDataTargetDayOX.getHashUsallyNum();
        this.hashNewRepertNum   = listDataTargetDayOX.getHashNewRepertNum();
        this.hashIntroNum       = listDataTargetDayOX.getHashIntroNum();
    }

    private void ShowData(){
        for(int i = 1;i<=this.dayOfMonth;i++){
            this.updateColumn(i);
        }
        updateColumnTotal();
    }

//    public void refresh()
//    {
//        this.loadDayPerMonth();
//        this.initTableHeaderMonths();
//        this.initArrays();
//        this.initSecondColumn();
//    }

    public void loadYearData()
    {
        try
        {
            init();
            getAllData();
            //ShowData();
            //listmonth.loadPerformance(SystemInfo.getConnection(),shopId , year);
            //updateTableTargetOXMonth();
        }catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
//    public final void loadDayPerMonth()
//    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, 1);
//        DAYS_COUNT = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//    }
    
    public boolean registerAllDataToDB( int nShopId, int year )
    {
        this.stopCellEditing();
        ArrayList<DataTargetDayOX> listItemTargetDayOX = new ArrayList<DataTargetDayOX>();
        listItemTargetDayOX = this.readAllFields();
        if(this.listDataTargetDayOX.saveToDatabase(listItemTargetDayOX) == false){
            return false;
        }else{
            this.loadYearData();
            return true;
        }
    }
    
    public void resetCellEditedFlag()
    {
        // in calling tableAllInfo.getColumnClass() method, you can use column numbers 2-13
        ((TableCellEditorEx)this.tableAllInfo.getDefaultEditor( this.tableAllInfo.getColumnClass(3))).resetCellEditedFlag();
    }
    
//    protected void initArrays()
//    {
//        for( int index=0; index <= DAYS_COUNT; index++ )
//        {
//            dataTargetResultList.add( new DataTargetResultBean() );
//        }
//    }
    
    public void updateTableTargetOXMonth()
    {
        DataTargetOX oxBean = null;

        for( int index = 0, monthIndex = 1; index < MONTHS_COUNT; index++,monthIndex++ )
        {
            oxBean = this.listmonth.get(index);
            if( oxBean != null )
            {
                this.updateColumn( LAST_READONLY_COL + monthIndex);
            }
            else
            {
                this.clearColumn( LAST_READONLY_COL + monthIndex );
            }
        }
        this.updateColumnTotal();
    }
        
//    public void updateTotals()
//    {
//        //���v
//        this.calculateTotal();
//        this.updateColumn( TOTAL_INFO_COLUMN, this.dataTargetResultTotal );
//    }
    
    private void clearColumn( int columnNum )
    {
        TableModel tableModel = this.tableAllInfo.getModel();
        
        for( int rowIndex = 0; rowIndex <= LAST_ROW_INDEX; rowIndex++ )
        {
            tableModel.setValueAt( null, rowIndex,columnNum );
        }
    }
    
    private void updateColumn(int item)
    {
        int columnNum = LAST_READONLY_COL + item;
        int rowIndex = 1;
        DataTargetDayOX goalData = new DataTargetDayOX();
        goalData = this.listItemTargetDay.get(item-1);
        // Table ����
        TableModel tableModel = this.tableAllInfo.getModel();
        Integer totalSale = 0;
        long otherRevenue = 0;
        totalTechnic = totalTechnic + Integer.valueOf(String.valueOf(this.hashTechnic.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashTechnic.get(item)), rowIndex++,columnNum );         // �Z�p����
        totalItem = totalItem + Integer.valueOf(String.valueOf(this.hashItem.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashItem.get(item)), rowIndex++,columnNum );              // ���X��
        // ������
        totalSale = Integer.valueOf(String.valueOf(this.hashTechnic.get(item)));
        totalSale += Integer.valueOf(String.valueOf(this.hashItem.get(item)));
        totalSales = totalSales + totalSale;
        tableModel.setValueAt( String.valueOf(totalSale) , rowIndex++,columnNum);           // ������
        totalCota = totalCota + Integer.valueOf(String.valueOf(this.hashCota.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashCota.get(item)), rowIndex++,columnNum );              // COTA����
        totalCutNum = totalCutNum + Integer.valueOf(String.valueOf(this.hashCutNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashCutNum.get(item)), rowIndex++,columnNum );            // CUT�q��
        totalCutSales = totalCutSales + Integer.valueOf(String.valueOf(this.hashCutSales.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashCutSales.get(item)), rowIndex++,columnNum );           // CUT����
        totalPermNum = totalPermNum + Integer.valueOf(String.valueOf(this.hashPermNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashPermNum.get(item)), rowIndex++,columnNum );           // Perm�q��
        totalPermSale = totalPermSale + Integer.valueOf(String.valueOf(this.hashPermSales.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashPermSales.get(item)), rowIndex++,columnNum );          // Perm����
        totalColNum = totalColNum + Integer.valueOf(String.valueOf(this.hashColNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashColNum.get(item)), rowIndex++,columnNum );            // Col�q��
        totalColSales = totalColSales + Integer.valueOf(String.valueOf(this.hashColSales.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashColSales.get(item)), rowIndex++,columnNum );           // Col����
        totalSpaNum = totalSpaNum + Integer.valueOf(String.valueOf(this.hashSpaNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashSpaNum.get(item)), rowIndex++,columnNum );            // �X�p�q��
        totalSpaSales = totalSpaSales + Integer.valueOf(String.valueOf(this.hashSpaSales.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashSpaSales.get(item)), rowIndex++,columnNum );           // �X�p����
        totalTreatNum = totalTreatNum + Integer.valueOf(String.valueOf(this.hashTreatNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashTreatNum.get(item)), rowIndex++,columnNum );          // �ذ���ċq��
        totalTreatSales = totalTreatSales + Integer.valueOf(String.valueOf(this.hashTreatSales.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashTreatSales.get(item)), rowIndex++,columnNum );         // �ذ���Ĕ���
        // ���̑����� Other revenue
        otherRevenue = totalSale;
        otherRevenue = otherRevenue - Integer.valueOf(String.valueOf(this.hashCota.get(item)));
        otherRevenue = otherRevenue - Integer.valueOf(String.valueOf(this.hashCutSales.get(item)));
        otherRevenue = otherRevenue - Integer.valueOf(String.valueOf(this.hashPermSales.get(item)));
        otherRevenue = otherRevenue - Integer.valueOf(String.valueOf(this.hashColSales.get(item)));
        otherRevenue = otherRevenue - Integer.valueOf(String.valueOf(this.hashSpaSales.get(item)));
        otherRevenue = otherRevenue - Integer.valueOf(String.valueOf(this.hashTreatSales.get(item)));
        totalOtherRevenue = totalOtherRevenue + otherRevenue ;
        tableModel.setValueAt( String.valueOf(otherRevenue), rowIndex++,columnNum );
        totalItemNum = totalItemNum + Integer.valueOf(String.valueOf(this.hashCota.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashItemNum.get(item)), rowIndex++,columnNum );           // �X�̐�
        totalyNum = totalyNum + Integer.valueOf(String.valueOf(this.hashYNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashYNum.get(item)), rowIndex++,columnNum );              // Y�q��
        totalmNum = totalmNum + Integer.valueOf(String.valueOf(this.hashMNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashMNum.get(item)), rowIndex++,columnNum );                 // M�q��
        totaloNum = totaloNum + Integer.valueOf(String.valueOf(this.hashONum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashONum.get(item)), rowIndex++,columnNum );              // O�q��
        totalUnNum = totalUnNum + Integer.valueOf(String.valueOf(this.hashUNNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashUNNum.get(item)), rowIndex++,columnNum );             // ���̑��q��
        totalNewNum = totalNewNum + Integer.valueOf(String.valueOf(this.hashNewNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashNewNum.get(item)), rowIndex++,columnNum );            // �V�K�q��
        totalNewRepertNum = totalNewRepertNum + Integer.valueOf(String.valueOf(this.hashNewRepertNum.get(item)));
        // ���сF�O�񗈓X����3�J���ȓ��ɗ��X�����ڋq��  ( new_repert_num)�@�@�@�@�@�ڕW�F22-(19+20)
        tableModel.setValueAt( String.valueOf(this.hashNewRepertNum.get(item)), rowIndex++,columnNum );      // �ė��q��
        totalUsallyNum = totalUsallyNum + Integer.valueOf(String.valueOf(this.hashUsallyNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashUsallyNum.get(item)), rowIndex++,columnNum );         // �V�ċq��
        // ���q��
        long totalNumCus = 0;
        totalNumCus = Integer.valueOf(String.valueOf(this.hashONum.get(item)));
        totalNumCus += Integer.valueOf(String.valueOf(this.hashYNum.get(item)));
        totalNumCus += Integer.valueOf(String.valueOf(this.hashMNum.get(item)));
        totalNumCustomer = totalNumCustomer + totalNumCus;
        tableModel.setValueAt( String.valueOf(totalNumCus), rowIndex++,columnNum );
        totalIntroNum = totalIntroNum + Integer.valueOf(String.valueOf(this.hashIntroNum.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashIntroNum.get(item)), rowIndex++,columnNum );          // �Љ�V�K
        totalNominatNum = totalNominatNum + Integer.valueOf(String.valueOf(this.hashNominat.get(item)));
        tableModel.setValueAt( String.valueOf(this.hashNominat.get(item)), rowIndex++,columnNum );        // �w���q��
        // Table �ڕW
        totalTechnicOX = totalTechnicOX + goalData.getTechnicID();
        tableModel.setValueAt( goalData.getTechnicIdStr(), rowIndex++,columnNum );         // �Z�p����
        totalItemOX = totalItemOX + goalData.getItem();
        tableModel.setValueAt( goalData.getItemStr(), rowIndex++,columnNum );              // ���X��
        // ������
        totalSale = 0;
        totalSale = goalData.getTechnicID() + goalData.getItem();
        totalSalesOX = totalSalesOX + totalSale;
        tableModel.setValueAt( String.valueOf(totalSale) , rowIndex++,columnNum);           // ������
        totalCotaOX = totalCotaOX + goalData.getCota();
        tableModel.setValueAt( goalData.getCotaStr(), rowIndex++,columnNum );              // COTA����
        totalCutNumOX = totalCutNumOX + goalData.getCutNum();
        tableModel.setValueAt( goalData.getCutNumStr(), rowIndex++,columnNum );            // CUT�q��
        totalCutSalesOX = totalCutSalesOX + goalData.getCutSale();
        tableModel.setValueAt( goalData.getCutSaleStr(), rowIndex++,columnNum );           // CUT����
        totalPermNumOX = totalPermNumOX + goalData.getPermNum();
        tableModel.setValueAt( goalData.getPermNumStr(), rowIndex++,columnNum );           // Perm�q��
        totalPermSaleOX = totalPermSaleOX + goalData.getPermSale();
        tableModel.setValueAt( goalData.getPermSaleStr(), rowIndex++,columnNum );          // Perm����
        totalColNumOX = totalColNumOX + goalData.getColNum();
        tableModel.setValueAt( goalData.getColNumStr(), rowIndex++,columnNum );            // Col�q��
        totalColSalesOX = totalColSalesOX + goalData.getColSale();
        tableModel.setValueAt( goalData.getColSaleStr(), rowIndex++,columnNum );           // Col����
        totalSpaNumOX = totalSpaNumOX + goalData.getSpaNum();
        tableModel.setValueAt( goalData.getSpaNumStr(), rowIndex++,columnNum );            // �X�p�q��
        totalSpaSalesOX = totalSpaSalesOX + goalData.getSpaSale();
        tableModel.setValueAt( goalData.getSpaSaleStr(), rowIndex++,columnNum );           // �X�p����
        totalTreatNumOX = totalTreatNumOX + goalData.getTreatNum();
        tableModel.setValueAt( goalData.getTreatNumStr(), rowIndex++,columnNum );          // �ذ���ċq��
        totalTreatSalesOX = totalTreatSalesOX + goalData.getTreatSale();
        tableModel.setValueAt( goalData.getTreatSaleStr(), rowIndex++,columnNum );         // �ذ���Ĕ���
        // ���̑����� Other revenue
        otherRevenue = 0;
        otherRevenue = totalSale - (goalData.getCota() + goalData.getCutSale() + goalData.getPermSale() + goalData.getColSale());
        totalOtherRevenueOX = totalOtherRevenueOX + otherRevenue ;
        tableModel.setValueAt( String.valueOf(otherRevenue), rowIndex++,columnNum );
        totalItemNumOX = totalItemNumOX + goalData.getItemNum();
        tableModel.setValueAt( goalData.getitemNumStr(), rowIndex++,columnNum );           // �X�̐�
        totalyNumOX = totalyNumOX + goalData.getyNum();
        tableModel.setValueAt( goalData.getyNumStr(), rowIndex++,columnNum );              // Y�q��
        totalmNumOX = totalmNumOX + goalData.getmNum();
        tableModel.setValueAt( goalData.getmNumStr(), rowIndex++,columnNum );                 // M�q��
        totaloNumOX = totaloNumOX + goalData.getoNum();
        tableModel.setValueAt( goalData.getoNumStr(), rowIndex++,columnNum );              // O�q��
        // totalUnNumOX = totalUnNumOX + goalData.getUnNum();
        // tableModel.setValueAt( goalData.getUnNumStr(), rowIndex++,columnNum );             // ���̑��q��
        totalNumCus = 0;
        totalNumCus = goalData.getTotalNum();
        totalNewNumOX = totalNewNumOX + goalData.getNewNum();
        tableModel.setValueAt( goalData.getNewNumStr(), rowIndex++,columnNum );            // �V�K�q��
        totalNewRepertNumOX = totalNewRepertNumOX + goalData.getNewRepertNum();
        // ���сF�O�񗈓X����3�J���ȓ��ɗ��X�����ڋq��  ( new_repert_num)�@�@�@�@�@�ڕW�F22-(19+20)
        tableModel.setValueAt( goalData.getNewRepertNumStr(), rowIndex++,columnNum );      // �ė��q��
        long totalUsally = 0;
        totalUsally = totalNumCus - (goalData.getNewNum() + goalData.getNewRepertNum());
        totalUsallyNumOX = totalUsallyNumOX + totalUsally;
        tableModel.setValueAt( String.valueOf(totalUsally), rowIndex++,columnNum );         // �V�ċq��
        totalNumCustomerOX = totalNumCustomerOX + totalNumCus;
        tableModel.setValueAt( String.valueOf(totalNumCus), rowIndex++,columnNum );
        long columtotal = 0;
        totalIntroNumOX = totalIntroNumOX + goalData.getIntroNum();
        tableModel.setValueAt( goalData.getIntroNumStr(), rowIndex++,columnNum );          // �Љ�V�K
        totalNominatNumOX = totalNominatNumOX + goalData.getNominatNum();
        tableModel.setValueAt( goalData.getnominatNumStr(), rowIndex++,columnNum );        // �w���q��
        //tableModel.setValueAt( null, rowIndex++,columnNum );
        // Colum Total
        totalOpenDayOX = totalOpenDayOX + goalData.getOpenDay();
        // �ғ�����  ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( goalData.getopenDayStr(), rowIndex++,columnNum );
        // �Ј����ؽ� ==> ���͂��ē���鍀�ڂł��B
        totaleStylistOX = totaleStylistOX + goalData.geteStylist();
        columtotal += goalData.geteStylist();
        tableModel.setValueAt( goalData.geteStylistStr(), rowIndex++,columnNum );
        // �Ј������� ==> ���͂��ē���鍀�ڂł��B
        totaleAssistantOX = totaleAssistantOX + goalData.geteAssistant();
        columtotal += goalData.geteAssistant();
        tableModel.setValueAt( goalData.geteAssistantStr(), rowIndex++,columnNum );
        // ���޲Ľ��ؽ� ==> ���͂��ē���鍀�ڂł��B
        totalarStylistOX = totalarStylistOX + goalData.getArStylist();
        columtotal += goalData.getArStylist();
        tableModel.setValueAt( goalData.getArStylistStr(), rowIndex++,columnNum );
        // ���޲ı����� ==> ���͂��ē���鍀�ڂł��B
        totalarAssistantOX = totalarAssistantOX + goalData.getArAssistant();
        columtotal += goalData.getArAssistant();
        tableModel.setValueAt( goalData.getArAssistantStr(), rowIndex++,columnNum );
        // ���޲Ă��̑� ==> ���͂��ē���鍀�ڂł��B
        totalarOtherOX = totalarOtherOX + goalData.getArOther();
        columtotal += goalData.getArOther();
        tableModel.setValueAt( goalData.getArOtherStr(), rowIndex++,columnNum );
        columTotalOX = totaleStylistOX + totaleAssistantOX + totalarStylistOX + totalarAssistantOX + totalarOtherOX;
        tableModel.setValueAt(String.valueOf(columtotal), rowIndex++,columnNum ); // �Ј���
    }

    private boolean updateColumnTotal()
    {
        int columnNum = this.dayOfMonth + 2;
        int rowIndex = 1;
        // Table ����
        TableModel tableModel = this.tableAllInfo.getModel();
        tableModel.setValueAt( String.valueOf(totalTechnic), rowIndex++,columnNum );        // �Z�p����
        tableModel.setValueAt( String.valueOf(totalItem ), rowIndex++,columnNum );          // ���X��
        // ������
        tableModel.setValueAt( String.valueOf(totalSales) , rowIndex++,columnNum);          // ������
        tableModel.setValueAt( String.valueOf(totalCota ), rowIndex++,columnNum );          // COTA����
        tableModel.setValueAt( String.valueOf(totalCutNum ), rowIndex++,columnNum );        // CUT�q��
        tableModel.setValueAt( String.valueOf(totalCutSales ), rowIndex++,columnNum );      // CUT����
        tableModel.setValueAt( String.valueOf(totalPermNum ), rowIndex++,columnNum );       // Perm�q��
        tableModel.setValueAt( String.valueOf(totalPermSale ), rowIndex++,columnNum );      // Perm����
        tableModel.setValueAt( String.valueOf(totalColNum   ), rowIndex++,columnNum );      // Col�q��
        tableModel.setValueAt( String.valueOf(totalColSales ), rowIndex++,columnNum );      // Col����
        tableModel.setValueAt( String.valueOf(totalSpaNum ), rowIndex++,columnNum );        // �X�p�q��
        tableModel.setValueAt( String.valueOf(totalSpaSales ), rowIndex++,columnNum );      // �X�p����
        tableModel.setValueAt( String.valueOf(totalTreatNum ), rowIndex++,columnNum );      // �ذ���ċq��
        tableModel.setValueAt( String.valueOf(totalTreatSales ), rowIndex++,columnNum );    // �ذ���Ĕ���
        tableModel.setValueAt( String.valueOf(totalOtherRevenue ), rowIndex++,columnNum );  // ���̑����� Other revenue
        tableModel.setValueAt( String.valueOf(totalItemNum ), rowIndex++,columnNum );           // �X�̐�
        tableModel.setValueAt( String.valueOf(totalyNum ), rowIndex++,columnNum );              // Y�q��
        tableModel.setValueAt( String.valueOf(totalmNum ), rowIndex++,columnNum );                 // M�q��
        tableModel.setValueAt( String.valueOf(totaloNum ), rowIndex++,columnNum );              // O�q��
        tableModel.setValueAt( String.valueOf(totalUnNum ), rowIndex++,columnNum );             // ���̑��q��
        tableModel.setValueAt( String.valueOf(totalNewNum ), rowIndex++,columnNum );            // �V�K�q��
        // ���сF�O�񗈓X����3�J���ȓ��ɗ��X�����ڋq��  ( new_repert_num)�@�@�@�@�@�ڕW�F22-(19+20)
        tableModel.setValueAt( String.valueOf(totalNewRepertNum  ), rowIndex++,columnNum );
        tableModel.setValueAt( String.valueOf(totalUsallyNum ), rowIndex++,columnNum );         // �V�ċq��
        tableModel.setValueAt( String.valueOf(totalNumCustomer  ), rowIndex++,columnNum );      // �ė��q��
        tableModel.setValueAt( String.valueOf(totalIntroNum ), rowIndex++,columnNum );          // �Љ�V�K
        tableModel.setValueAt( String.valueOf(totalNominatNum ), rowIndex++,columnNum );        // �w���q��
        // Table �ڕW
        tableModel.setValueAt( String.valueOf(totalTechnicOX), rowIndex++,columnNum );        // �Z�p����
        tableModel.setValueAt( String.valueOf(totalItemOX ), rowIndex++,columnNum );          // ���X��
        // ������
        tableModel.setValueAt( String.valueOf(totalSalesOX) , rowIndex++,columnNum);          // ������
        tableModel.setValueAt( String.valueOf(totalCotaOX ), rowIndex++,columnNum );          // COTA����
        tableModel.setValueAt( String.valueOf(totalCutNumOX ), rowIndex++,columnNum );        // CUT�q��
        tableModel.setValueAt( String.valueOf(totalCutSalesOX ), rowIndex++,columnNum );      // CUT����
        tableModel.setValueAt( String.valueOf(totalPermNumOX ), rowIndex++,columnNum );       // Perm�q��
        tableModel.setValueAt( String.valueOf(totalPermSaleOX ), rowIndex++,columnNum );      // Perm����
        tableModel.setValueAt( String.valueOf(totalColNumOX   ), rowIndex++,columnNum );      // Col�q��
        tableModel.setValueAt( String.valueOf(totalColSalesOX ), rowIndex++,columnNum );      // Col����
        tableModel.setValueAt( String.valueOf(totalSpaNumOX ), rowIndex++,columnNum );        // �X�p�q��
        tableModel.setValueAt( String.valueOf(totalSpaSalesOX ), rowIndex++,columnNum );      // �X�p����
        tableModel.setValueAt( String.valueOf(totalTreatNumOX ), rowIndex++,columnNum );      // �ذ���ċq��
        tableModel.setValueAt( String.valueOf(totalTreatSalesOX ), rowIndex++,columnNum );    // �ذ���Ĕ���
        tableModel.setValueAt( String.valueOf(totalOtherRevenueOX ), rowIndex++,columnNum );  // ���̑��q��
        tableModel.setValueAt( String.valueOf(totalItemNumOX ), rowIndex++,columnNum );           // �X�̐�
        tableModel.setValueAt( String.valueOf(totalyNumOX ), rowIndex++,columnNum );              // Y�q��
        tableModel.setValueAt( String.valueOf(totalmNumOX ), rowIndex++,columnNum );                 // M�q��
        tableModel.setValueAt( String.valueOf(totaloNumOX ), rowIndex++,columnNum );              // O�q��
        tableModel.setValueAt( String.valueOf(totalNewNumOX ), rowIndex++,columnNum );            // �V�K�q��
        tableModel.setValueAt( String.valueOf(totalNewRepertNumOX  ), rowIndex++,columnNum );
        tableModel.setValueAt( String.valueOf(totalUsallyNumOX ), rowIndex++,columnNum );         // �V�ċq��
        tableModel.setValueAt( String.valueOf(totalNumCustomerOX  ), rowIndex++,columnNum );      // �ė��q��
        tableModel.setValueAt( String.valueOf(totalIntroNumOX ), rowIndex++,columnNum );          // �Љ�V�K
        tableModel.setValueAt( String.valueOf(totalNominatNumOX ), rowIndex++,columnNum );        // �w���q��
        // Colum Total
        // �ғ�����  ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( String.valueOf(totalOpenDayOX ), rowIndex++,columnNum );
        // �Ј����ؽ� ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( String.valueOf(totaleStylistOX ), rowIndex++,columnNum );
        // �Ј������� ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( String.valueOf(totaleAssistantOX ), rowIndex++,columnNum );
        // ���޲Ľ��ؽ� ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( String.valueOf(totalarStylistOX ), rowIndex++,columnNum );
        // ���޲ı����� ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( String.valueOf(totalarAssistantOX ), rowIndex++,columnNum );
        // ���޲Ă��̑� ==> ���͂��ē���鍀�ڂł��B
        tableModel.setValueAt( String.valueOf(totalarOtherOX ), rowIndex++,columnNum );
        // �Ј���
        tableModel.setValueAt( String.valueOf(columTotalOX ), rowIndex++,columnNum ); // �Ј���
        return true;
    }

    protected ArrayList<DataTargetDayOX> readAllFields()
    {
        DataTargetDayOX  itemTargetDayOX = new DataTargetDayOX();
        ArrayList<DataTargetDayOX> listTargetDayOX = new ArrayList<DataTargetDayOX>();
        int colIndex = LAST_READONLY_COL + 1;
        Calendar cal = Calendar.getInstance();
        for( int index = 0; index < this.dayOfMonth; index++ )
        {
            itemTargetDayOX = new DataTargetDayOX();
            // �X��ID
            itemTargetDayOX.setShopID(this.shopId);
            // �ڕW���t
            cal = Calendar.getInstance();
            cal.set(this.year, this.month, index+1);
            itemTargetDayOX.setDay(cal.getTime());
            // �Z�p����
            if(this.getCellValue(TECHNIC_ROW, colIndex ) != null){
                itemTargetDayOX.setTechnicID(Integer.parseInt(this.getCellValue( TECHNIC_ROW, colIndex )));
            }else{
                itemTargetDayOX.setTechnicID(null);
            }
            // ���X��
            if(this.getCellValue(ITEM_ROW , colIndex ) != null){
                itemTargetDayOX.setItem(Integer.parseInt(this.getCellValue(ITEM_ROW , colIndex )));
            }else{
                itemTargetDayOX.setItem(null);
            }
            // COTA����
            if(this.getCellValue(COTA_SALES_ROW , colIndex ) != null){
                itemTargetDayOX.setCota(Integer.parseInt(this.getCellValue(COTA_SALES_ROW , colIndex )));
            }else{
                itemTargetDayOX.setCota(null);
            }
            // CUT�q��
            if(this.getCellValue(CUT_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setCutNum(Integer.parseInt(this.getCellValue(CUT_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setCutNum(null);
            }
            // CUT����
            if(this.getCellValue(CUT_SALES_ROW, colIndex ) != null){
                itemTargetDayOX.setCutSale(Integer.parseInt(this.getCellValue(CUT_SALES_ROW, colIndex )));
            }else{
                itemTargetDayOX.setCutSale(null);
            }
            // Perm�q��
            if(this.getCellValue(PERM_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setPermNum(Integer.parseInt(this.getCellValue(PERM_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setPermNum(null);
            }
            // Perm����
            if(this.getCellValue(PERM_SALES_ROW, colIndex ) != null){
                itemTargetDayOX.setPermSale(Integer.parseInt(this.getCellValue(PERM_SALES_ROW, colIndex )));
            }else{
                itemTargetDayOX.setPermSale(null);
            }
            // Col�q��
            if(this.getCellValue(COL_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setColNum(Integer.parseInt(this.getCellValue(COL_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setColNum(null);
            }
            // Col����
            if(this.getCellValue(COL_SALES_ROW, colIndex ) != null){
                itemTargetDayOX.setColSale(Integer.parseInt(this.getCellValue(COL_SALES_ROW, colIndex )));
            }else{
                itemTargetDayOX.setColSale(null);
            }
            // �X�p�q��
            if(this.getCellValue(SPA_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setSpaNum(Integer.parseInt(this.getCellValue(SPA_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setSpaNum(null);
            }
            // �X�p����
            if(this.getCellValue(SPA_SALES_ROW, colIndex ) != null){
                itemTargetDayOX.setSpaSale(Integer.parseInt(this.getCellValue(SPA_SALES_ROW, colIndex )));
            }else{
                itemTargetDayOX.setSpaSale(null);
            }
            // �ذ���ċq��
            if(this.getCellValue(STREAT_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setTreatNum(Integer.parseInt(this.getCellValue(STREAT_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setTreatNum(null);
            }
            // �ذ���Ĕ���
            if(this.getCellValue(STREAT_SALES_ROW, colIndex ) != null){
                itemTargetDayOX.setTreatSale(Integer.parseInt(this.getCellValue(STREAT_SALES_ROW, colIndex )));
            }else{
                itemTargetDayOX.setTreatSale(null);
            }
            // �X�̐�
            if(this.getCellValue(ITEM_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setItemNum(Integer.parseInt(this.getCellValue(ITEM_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setItemNum(null);
            }
            // Y�q��
            if(this.getCellValue(Y_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setyNum(Integer.parseInt(this.getCellValue(Y_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setyNum(null);
            }
            // O�q��
            if(this.getCellValue(O_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setoNum(Integer.parseInt(this.getCellValue(O_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setoNum(null);
            }
            // �V�K�q��
            if(this.getCellValue(NEW_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setNewNum(Integer.parseInt(this.getCellValue(NEW_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setNewNum(null);
            }
            // �V�ċq��
            if(this.getCellValue(NEW_REPERT_NUM, colIndex ) != null){
                itemTargetDayOX.setNewRepertNum(Integer.parseInt(this.getCellValue(NEW_REPERT_NUM, colIndex )));
            }else{
                itemTargetDayOX.setNewRepertNum(null);
            }
            // ���q��
            if(this.getCellValue(TOTAL_NUM_CUS, colIndex ) != null){
                itemTargetDayOX.setTotalNum(Integer.parseInt(this.getCellValue(TOTAL_NUM_CUS, colIndex )));
            }else{
                itemTargetDayOX.setTotalNum(null);
            }
            // �Љ�V�K
            if( this.getCellValue(INTRO_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setIntroNum(Integer.parseInt(this.getCellValue(INTRO_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setIntroNum(null);
            }
            // �w���q��
            if(this.getCellValue(NOMINAT_NUM_ROW, colIndex ) != null){
                itemTargetDayOX.setNominatNum(Integer.parseInt(this.getCellValue(NOMINAT_NUM_ROW, colIndex )));
            }else{
                itemTargetDayOX.setNominatNum(null);
            }
            // �ғ�����
            if(this.getCellValue(OPEN_DAY_ROW, colIndex ) != null){
                itemTargetDayOX.setOpenDay(Integer.parseInt(this.getCellValue(OPEN_DAY_ROW, colIndex )));
            }else{
                itemTargetDayOX.setOpenDay(null);
            }
            // �Ј����ؽ�
            if(this.getCellValue(E_STYLIST_ROW, colIndex ) != null){
                itemTargetDayOX.seteStylist(Integer.parseInt(this.getCellValue(E_STYLIST_ROW, colIndex )));
            }else{
                itemTargetDayOX.seteStylist(null);
            }
            // �Ј�������
            if(this.getCellValue(E_ASSISTANT_ROW, colIndex ) != null){
                itemTargetDayOX.seteAssistant(Integer.parseInt(this.getCellValue(E_ASSISTANT_ROW, colIndex )));
            }else{
                itemTargetDayOX.seteAssistant(null);
            }
            // ���޲Ľ��ؽ
            if(this.getCellValue(AR_STYLIST_ROW, colIndex ) != null){
                itemTargetDayOX.setArStylist(Integer.parseInt(this.getCellValue(AR_STYLIST_ROW, colIndex )));
            }else{
                itemTargetDayOX.setArStylist(null);
            }
            // ���޲ı�����
            if(this.getCellValue(AR_ASSISTANT_ROW, colIndex ) != null){
                itemTargetDayOX.setArAssistant(Integer.parseInt(this.getCellValue(AR_ASSISTANT_ROW, colIndex )));
            }else{
                itemTargetDayOX.setArAssistant(null);
            }
            // ���޲Ă��̑�
            if(this.getCellValue(AR_OTHER_ROW, colIndex ) != null){
                itemTargetDayOX.setArOther(Integer.parseInt(this.getCellValue(AR_OTHER_ROW, colIndex )));
            }else{
                itemTargetDayOX.setArOther(null);
            }
            listTargetDayOX.add(itemTargetDayOX);
            colIndex++;
        }
        return listTargetDayOX;
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
            if("".equals(value.toString())){
                return null;
            }
            return value.toString();
        }
    }
        
    private boolean initTableHeaderMonths()
    {
        this.tableAllInfo.setBorder( LineBorder.createBlackLineBorder() );
        TableModel tableModel = this.tableAllInfo.getModel();

        int colIndex = 0;
        int dayNum = 1;

        String columnVal = "";
        tableModel.setValueAt( columnVal, 0, colIndex++ );
        tableModel.setValueAt( columnVal, 0, colIndex );
        for(int colDay = 0; colDay<=this.dayOfMonth; colDay ++)
        {
            colIndex = 2 + colDay;
            columnVal = dayNum++ + "��";
            tableModel.setValueAt( columnVal, 0, colIndex );
        }
        columnVal = "���v";
        tableModel.setValueAt( columnVal, 0, colIndex );
        for(int iRemove = this.dayOfMonth + 3; iRemove <this.tableAllInfo.getColumnCount(); iRemove++ )
        {
            tableAllInfo.getColumnModel().getColumn(iRemove).setMaxWidth(0);
            tableAllInfo.getColumnModel().getColumn(iRemove).setMinWidth(0);
            tableAllInfo.getColumnModel().getColumn(iRemove).setWidth(0);
            tableAllInfo.getColumnModel().getColumn(iRemove).setPreferredWidth(0);
            this.clearColumn(iRemove);
        }
        int colWidth = tableAllInfo.getColumnModel().getColumn(4).getWidth();
        for(int j = 28;j<=this.dayOfMonth;j++){
            tableAllInfo.getColumnModel().getColumn(j+2).setMaxWidth(colWidth);
            tableAllInfo.getColumnModel().getColumn(j+2).setMinWidth(colWidth);
            tableAllInfo.getColumnModel().getColumn(j+2).setWidth(colWidth);
            tableAllInfo.getColumnModel().getColumn(j+2).setPreferredWidth(colWidth);
        }
        tableAllInfo.getColumnModel().getColumn(0).setMaxWidth(75);
        tableAllInfo.getColumnModel().getColumn(0).setMinWidth(75);
        tableAllInfo.getColumnModel().getColumn(0).setWidth(75);
        tableAllInfo.getColumnModel().getColumn(0).setPreferredWidth(75);
        tableAllInfo.getColumnModel().getColumn(1).setMaxWidth(75);
        tableAllInfo.getColumnModel().getColumn(1).setMinWidth(75);
        tableAllInfo.getColumnModel().getColumn(1).setWidth(75);
        tableAllInfo.getColumnModel().getColumn(1).setPreferredWidth(75);
        tableAllInfo.doLayout();
        return true;
    }
    
    private boolean initSecondColumn()
    {   
        int rowIndex = 0;

        TableModel tableModel = this.tableAllInfo.getModel();

       tableModel.setValueAt( "", rowIndex++, 1 );
        tableModel.setValueAt( "�Z�p����", rowIndex++, 1 );
        tableModel.setValueAt( "���X��", rowIndex++, 1 );
        tableModel.setValueAt( "������", rowIndex++, 1 );
        tableModel.setValueAt( "COTA����", rowIndex++, 1 );
        tableModel.setValueAt( "CUT�q��", rowIndex++, 1 );
        tableModel.setValueAt( "CUT����", rowIndex++, 1 );
        tableModel.setValueAt( "Perm�q��", rowIndex++, 1 );
        tableModel.setValueAt( "Perm����", rowIndex++, 1 );
        tableModel.setValueAt( "Col�q��", rowIndex++, 1 );
        tableModel.setValueAt( "Col����", rowIndex++, 1 );
        tableModel.setValueAt( "�X�p�q��", rowIndex++, 1 );
        tableModel.setValueAt( "�X�p����", rowIndex++, 1 );
        tableModel.setValueAt( "�ذ���ċq��", rowIndex++, 1 );
        tableModel.setValueAt( "�ذ���Ĕ���", rowIndex++, 1 );
        tableModel.setValueAt( "���̑�����", rowIndex++, 1 );
        tableModel.setValueAt( "�X�̐�", rowIndex++, 1 );
        tableModel.setValueAt( "Y�q��", rowIndex++, 1 );
        tableModel.setValueAt( "M�q��", rowIndex++, 1 );
        tableModel.setValueAt( "O�q��", rowIndex++, 1 );
        tableModel.setValueAt( "���̑��q��", rowIndex++, 1 );
        tableModel.setValueAt( "�V�K�q��", rowIndex++, 1 );
        tableModel.setValueAt( "�V�ċq��", rowIndex++, 1 );
        tableModel.setValueAt( "�ė��q��", rowIndex++, 1 );
        tableModel.setValueAt( "���q��", rowIndex++, 1 );
        tableModel.setValueAt( "�Љ�V�K", rowIndex++, 1 );
        tableModel.setValueAt( "�w���q��", rowIndex++, 1 );

        tableModel.setValueAt( "�Z�p����", rowIndex++, 1 );
        tableModel.setValueAt( "���X��", rowIndex++, 1 );
        tableModel.setValueAt( "������", rowIndex++, 1 );
        tableModel.setValueAt( "COTA����", rowIndex++, 1 );
        tableModel.setValueAt( "CUT�q��", rowIndex++, 1 );
        tableModel.setValueAt( "CUT����", rowIndex++, 1 );
        tableModel.setValueAt( "Perm�q��", rowIndex++, 1 );
        tableModel.setValueAt( "Perm����", rowIndex++, 1 );
        tableModel.setValueAt( "Col�q��", rowIndex++, 1 );
        tableModel.setValueAt( "Col����", rowIndex++, 1 );
        tableModel.setValueAt( "�X�p�q��", rowIndex++, 1 );
        tableModel.setValueAt( "�X�p����", rowIndex++, 1 );
        tableModel.setValueAt( "�ذ���ċq��", rowIndex++, 1 );
        tableModel.setValueAt( "�ذ���Ĕ���", rowIndex++, 1 );
        tableModel.setValueAt( "���̑�����", rowIndex++, 1 );
        tableModel.setValueAt( "�X�̐�", rowIndex++, 1 );
        tableModel.setValueAt( "Y�q��", rowIndex++, 1 );
        tableModel.setValueAt( "M�q��", rowIndex++, 1 );
        tableModel.setValueAt( "O�q��", rowIndex++, 1 );
        //tableModel.setValueAt( "���̑��q��", rowIndex++, 1 );
        tableModel.setValueAt( "�V�K�q��", rowIndex++, 1 );
        tableModel.setValueAt( "�V�ċq��", rowIndex++, 1 );
        tableModel.setValueAt( "�ė��q��", rowIndex++, 1 );
        tableModel.setValueAt( "���q��", rowIndex++, 1 );
        tableModel.setValueAt( "�Љ�V�K", rowIndex++, 1 );
        tableModel.setValueAt( "�w���q��", rowIndex++, 1 );

        tableModel.setValueAt( "����", 12, 0 );
        tableModel.setValueAt( "�ڕW", 33, 0 );

        tableModel.setValueAt( "�ғ�", 52, 0 );
        tableModel.setValueAt( "����", 52, 1 );
        tableModel.setValueAt( "�Ј��", 53, 0 );
        tableModel.setValueAt( "��ؽ�", 53, 1 );
        tableModel.setValueAt( "�Ј��", 54, 0 );
        tableModel.setValueAt( "�����", 54, 1 );
        tableModel.setValueAt( "���޲�", 55, 0 );
        tableModel.setValueAt( "���ؽ�", 55, 1 );
        tableModel.setValueAt( "���޲�", 56, 0 );
        tableModel.setValueAt( "������", 56, 1 );
        tableModel.setValueAt( "���޲", 57, 0 );
        tableModel.setValueAt( "Ă��̑�", 57, 1 );
        tableModel.setValueAt( "�Ј�", 58, 0 );
        tableModel.setValueAt( "��", 58, 1 );
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
                        "�e�[�u���ŗ\�����ʃG���[���������܂����B", 
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
    
       public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        tableAllInfo = new javax.swing.JTable();
        tableAllInfo = new javax.swing.JTable(new DefaultTableModel()){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if( ( rowIndex == 0 ) || ( colIndex == 0 ) || ( colIndex == 1 ) )
                return false;   //Disallow the editing of any cell
                else if( ( rowIndex <27 ))
                {
                    return false;
                }
                else if( rowIndex == 29 || rowIndex == 41 || rowIndex == 44 || rowIndex == 48 || rowIndex == 58)
                {
                    return false;
                }
                else if( colIndex == 33 )
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

        tableAllInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "�^�C�g�� 1", "�^�C�g�� 2", "�^�C�g�� 3", "�^�C�g�� 4", "�^�C�g�� 5", "�^�C�g�� 6", "�^�C�g�� 7", "�^�C�g�� 8", "�^�C�g�� 9", "�^�C�g�� 10", "�^�C�g�� 11", "�^�C�g�� 12", "�^�C�g�� 13", "�^�C�g�� 14", "�^�C�g�� 15", "Title 16", "Title 17", "Title 18", "Title 19", "Title 20", "Title 21", "Title 22", "Title 23", "Title 24", "Title 25", "Title 26", "Title 27", "Title 28", "Title 29", "Title 30", "Title 31", "Title 32", "Title 33", "Title 34"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableAllInfo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tableAllInfo.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        tableAllInfo.setCellSelectionEnabled(true);
        tableAllInfo.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableAllInfo.setShowHorizontalLines(false);
        tableAllInfo.setShowVerticalLines(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tableAllInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 2000, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tableAllInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableAllInfo.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        getAccessibleContext().setAccessibleName("985");
        getAccessibleContext().setAccessibleDescription("558");
    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollBar jScrollBar1;
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
                    value = format.format( Long.valueOf( temp ) );
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
            if (column == 0 && row >= 52) {
                horizAlignment = SwingConstants.RIGHT;
            } else if (column == 1 && row >= 52) {
                horizAlignment = SwingConstants.LEFT;
            }

            Border border = BorderFactory.createMatteBorder( 1, 1, 0, 0, Color.GRAY );

             if( ( row == MONTHS_LABEL_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                  //background = Color.WHITE;
                border = BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.GRAY );
            } else if( ( row == LAST_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                 //background = Color.WHITE;
                border = BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.GRAY );
            // Phuong add
             } else if ((row == 53) && (column == FIELDS_LABEL_COLUMN)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 54) && (column == FIELDS_LABEL_COLUMN)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 55) && (column == FIELDS_LABEL_COLUMN)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 56) && (column == FIELDS_LABEL_COLUMN)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 57) && (column == FIELDS_LABEL_COLUMN)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 58) && (column ==FIELDS_LABEL_COLUMN)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
             } else if ((row == 58) && (column ==0)) {
                 //background = Color.WHITE;
                 border = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
                 //
            } else if( ( column == 0 ) && ( row > 1 && row < 27 ) ) {
                //background = Color.WHITE;
                border = BorderFactory.createMatteBorder( 0, 1, 0, 0, Color.GRAY );

            } else if( ( column == TYPE_LABEL_COLUMN ) && ( row > 27 && row < LAST_ROW ) ) {
                //background = Color.WHITE;
                border = BorderFactory.createMatteBorder( 0, 1, 0, 0, Color.GRAY );
            } else if( ( row == MONTHS_LABEL_ROW ) || ( column == TYPE_LABEL_COLUMN ) || ( column == FIELDS_LABEL_COLUMN ) ) {
                //background = Color.WHITE;
            } else if( ( row == 29 )|| ( row == 41 ) ||
                    ( row == 44 ) || ( row == 48 )|| ( row == 58 ) ) {
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
            } else if( ( column == 0 ) && ( row > 1 && row < 23 ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( column == TYPE_LABEL_COLUMN ) && ( row > 23 && row < LAST_ROW ) ) {
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

//            int editNum;
//            if (CheckUtil.isNumeric(getCellEditorValue().toString())) {
//                editNum = Integer.parseInt(getCellEditorValue().toString());
//            }

            if (row == TECHNIC_ROW || row == ITEM_ROW) {
                String tmp1 = getValueAt(TECHNIC_ROW, col);
                String tmp2 = getValueAt(ITEM_ROW, col);
                if (CheckUtil.isNumeric(tmp1)) {
                    bDraw = true;
                    intCell1 = Integer.parseInt(tmp1);
                } else {
                    intCell1 = 0;
                }
                if (CheckUtil.isNumeric(tmp2)) {
                    bDraw = true;
                    intCell2 = Integer.parseInt(tmp2);
                } else {
                    intCell2 = 0;
                }
                intSetVal = intCell1 + intCell2;

                if (bDraw) {
                    tblEditing.setValueAt(intSetVal, TOTAL_SALES_ROW, col);
                } else {
                    tblEditing.setValueAt(null, TOTAL_SALES_ROW, col);
                }
                setSum(TOTAL_SALES_ROW);
                setSumTotalOtherRevenue();
                if (row == ACTUAL_TECHNIQUE_ROW) {
                    tmp2 = getValueAt(ACTUAL_CUSTOMERCNT_ROW, col);
                    intCell2 = 0;
                    if (CheckUtil.isNumeric(tmp2)) {
                        intCell2 = Integer.parseInt(getValueAt(ACTUAL_CUSTOMERCNT_ROW, col));
                    }
                    if (intCell2 != 0) {
                        intSetVal = NumberUtil.round((float) intCell1 / (float) intCell2);
                        tblEditing.setValueAt(intSetVal, ACTUAL_CUSTOMERS_AVE, col);
                    } else {
                        tblEditing.setValueAt(null, ACTUAL_CUSTOMERS_AVE, col);
                    }
                    setSum(ACTUAL_CUSTOMERS_AVE);
                }
            }else if(row == COTA_SALES_ROW || row == CUT_SALES_ROW || row == PERM_SALES_ROW  || row == COL_SALES_ROW || row == SPA_SALES_ROW || row == STREAT_SALES_ROW){
                String tmp1 = getValueAt(row, col);
                if (CheckUtil.isNumeric(tmp1)) {
                    setSum(row);
                    setSumTotalOtherRevenue();
                }
            }else if(row == Y_NUM_ROW || row == O_NUM_ROW){
                setSum(row);
                setMNum(col);
            }else if(row == TOTAL_NUM_CUS){
                setSum(row);
                setMNum(col);
                setUsallyNum(col);
            }else if(row == NEW_NUM_ROW || row == NEW_REPERT_NUM){
                setSum(row);
                setUsallyNum(col);
            }else if(row == E_STYLIST_ROW || row == E_ASSISTANT_ROW || row == AR_STYLIST_ROW || row == AR_ASSISTANT_ROW || row == AR_OTHER_ROW){
                setSum(row);
                setSumTotalNumEmployee(col);
            } else if (row == ACTUAL_CUSTOMERCNT_ROW) {
                String tmp1 = getValueAt(ACTUAL_CUSTOMERCNT_ROW, col);

                if (CheckUtil.isNumeric(tmp1)) {
                    intCell1 = Integer.parseInt(tmp1);
                    if (intCell1 != 0) {
                        String tmp2 = getValueAt(ACTUAL_TECHNIQUE_ROW, col);
                        if (CheckUtil.isNumeric(tmp2)) {
                            intCell2 = Integer.parseInt(tmp2);
                        } else {
                            intCell2 = 0;
                        }
                        bDraw = true;
                        intSetVal = NumberUtil.round((float) intCell2 / (float) intCell1);
                        tblEditing.setValueAt(intSetVal, ACTUAL_CUSTOMERS_AVE, col);
                    }
                }

                if (!bDraw) {
                    tblEditing.setValueAt(null, ACTUAL_CUSTOMERS_AVE, col);
                }
                setSum(ACTUAL_CUSTOMERS_AVE);

            } else if (row == ESTIMATE_TECHNIQUE_ROW || row == ESTIMATE_COMMODITY_ROW) {
                String tmp1 = getValueAt(ESTIMATE_TECHNIQUE_ROW, col);
                String tmp2 = getValueAt(ESTIMATE_COMMODITY_ROW, col);

                if (CheckUtil.isNumeric(tmp1)) {
                    bDraw = true;
                    intCell1 = Integer.parseInt(tmp1);
                } else {
                    intCell1 = 0;
                }
                if (CheckUtil.isNumeric(tmp2)) {
                    bDraw = true;
                    intCell2 = Integer.parseInt(tmp2);
                } else {
                    intCell2 = 0;
                }
                intSetVal = intCell1 + intCell2;

                if (bDraw) {
                    tblEditing.setValueAt(intSetVal, ESTIMATE_TOTAL_SALES_ROW, col);
                } else {
                    tblEditing.setValueAt(null, ESTIMATE_TOTAL_SALES_ROW, col);
                }

                setSum(ESTIMATE_TOTAL_SALES_ROW);
                if (row == ESTIMATE_TECHNIQUE_ROW) {
                    tmp2 = getValueAt(ESTIMATE_CUSTOMERCNT_ROW, col);
                    intCell2 = 0;
                    if (CheckUtil.isNumeric(tmp2)) {
                        intCell2 = Integer.parseInt(getValueAt(ESTIMATE_CUSTOMERCNT_ROW, col));
                    }
                    if (intCell2 != 0) {
                        intSetVal = NumberUtil.round((float) intCell1 / (float) intCell2);
                        tblEditing.setValueAt(intSetVal, ESTIMATE_CUSTOMERS_AVE, col);
                    } else {
                        tblEditing.setValueAt(null, ESTIMATE_CUSTOMERS_AVE, col);
                    }
                    setSum(ESTIMATE_CUSTOMERS_AVE);
                }
            } else if (row == ESTIMATE_CUSTOMERCNT_ROW) {
                String tmp1 = getValueAt(ESTIMATE_CUSTOMERCNT_ROW, col);

                if (CheckUtil.isNumeric(tmp1)) {
                    intCell1 = Integer.parseInt(tmp1);
                    if (intCell1 != 0) {
                        String tmp2 = getValueAt(ESTIMATE_TECHNIQUE_ROW, col);
                        if (CheckUtil.isNumeric(tmp2)) {
                            intCell2 = Integer.parseInt(tmp2);
                        } else {
                            intCell2 = 0;
                        }
                        bDraw = true;
                        intSetVal = NumberUtil.round((float) intCell2 / (float) intCell1);
                        tblEditing.setValueAt(intSetVal, ESTIMATE_CUSTOMERS_AVE, col);
                    }
                }

                if (!bDraw) {
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

            for (int i = DAY_COLUMN; i <= dayOfMonth; ++i) {
                if( CheckUtil.isNumeric(getValueAt(row, i)) ){
                    bDraw = true;
                    sum += Integer.parseInt(getValueAt(row, i));
                }
            }

            if( bDraw ){
                tblEditing.setValueAt(sum, row, dayOfMonth+2);
            }else{
                tblEditing.setValueAt(null, row,dayOfMonth+2);
            }
        }

        private void setMNum(int col) {
            Integer resultMNum = 0;
            boolean bDraw = false;
            if( CheckUtil.isNumeric(getValueAt(TOTAL_NUM_CUS  ,col)) ){
                    bDraw = true;
                    resultMNum = Integer.parseInt(getValueAt(TOTAL_NUM_CUS ,col));
            }
            if( CheckUtil.isNumeric(getValueAt(Y_NUM_ROW ,col)) ){
                bDraw = true;
                resultMNum = resultMNum - Integer.parseInt(getValueAt(Y_NUM_ROW,col));
            }
            if( CheckUtil.isNumeric(getValueAt(O_NUM_ROW,col)) ){
                bDraw = true;
                resultMNum = resultMNum - Integer.parseInt(getValueAt(O_NUM_ROW ,col));
            }
            if( bDraw ){
                tblEditing.setValueAt(resultMNum, M_NUM_ROW , col);
            }else{
                tblEditing.setValueAt(null, M_NUM_ROW , col);
            }
            setSum(M_NUM_ROW );
        }

        private void setUsallyNum(int col) {
            Integer resultUsallyNum = 0;
            boolean bDraw = false;
            if( CheckUtil.isNumeric(getValueAt(TOTAL_NUM_CUS  ,col)) ){
                bDraw = true;
                resultUsallyNum = Integer.parseInt(getValueAt(TOTAL_NUM_CUS ,col));
            }
            if( CheckUtil.isNumeric(getValueAt(NEW_NUM_ROW,col)) ){
                bDraw = true;
                resultUsallyNum = resultUsallyNum - Integer.parseInt(getValueAt(NEW_NUM_ROW,col));
            }
            if( CheckUtil.isNumeric(getValueAt(NEW_REPERT_NUM,col)) ){
                bDraw = true;
                resultUsallyNum = resultUsallyNum - Integer.parseInt(getValueAt(NEW_REPERT_NUM ,col));
            }
            if( bDraw ){
                tblEditing.setValueAt(resultUsallyNum, USALLY_NUM , col);
            }else{
                tblEditing.setValueAt(null, USALLY_NUM , col);
            }
            setSum(USALLY_NUM );
        }

        private void setSumTotalNumEmployee(int col) {
            Integer sum = 0;
            boolean bDraw = false;
            if( CheckUtil.isNumeric(getValueAt(E_STYLIST_ROW,col)) ){
                    bDraw = true;
                    sum += Integer.parseInt(getValueAt(E_STYLIST_ROW,col));
            }
            if( CheckUtil.isNumeric(getValueAt(E_ASSISTANT_ROW ,col)) ){
                bDraw = true;
                sum += Integer.parseInt(getValueAt(E_ASSISTANT_ROW ,col));
            }
            if( CheckUtil.isNumeric(getValueAt(AR_STYLIST_ROW ,col)) ){
                bDraw = true;
                sum += Integer.parseInt(getValueAt(AR_STYLIST_ROW ,col));
            }
            if( CheckUtil.isNumeric(getValueAt(AR_ASSISTANT_ROW ,col)) ){
                bDraw = true;
                sum += Integer.parseInt(getValueAt(AR_ASSISTANT_ROW ,col));
            }
            if( CheckUtil.isNumeric(getValueAt(AR_OTHER_ROW ,col)) ){
                bDraw = true;
                sum += Integer.parseInt(getValueAt(AR_OTHER_ROW ,col));
            }
            if( bDraw ){
                tblEditing.setValueAt(sum, TOTAL_NUM_EMPLOYEE_ROW, col);
            }else{
                tblEditing.setValueAt(null, TOTAL_NUM_EMPLOYEE_ROW, col);
            }
            setSum(TOTAL_NUM_EMPLOYEE_ROW);
        }

        private void setSumTotalOtherRevenue() {
            boolean bDraw = false;
            Integer tempOtherRevenue = 0;
            for(int columnIndex = 2;columnIndex < 14;columnIndex++){
                tempOtherRevenue = 0;
                bDraw = false;
                if( CheckUtil.isNumeric(getValueAt(TOTAL_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = Integer.parseInt(getValueAt(TOTAL_SALES_ROW,columnIndex));
                }
                if( CheckUtil.isNumeric(getValueAt(COTA_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = tempOtherRevenue - Integer.parseInt(getValueAt(COTA_SALES_ROW,columnIndex));
                }
                if( CheckUtil.isNumeric(getValueAt(CUT_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = tempOtherRevenue - Integer.parseInt(getValueAt(CUT_SALES_ROW,columnIndex));
                }
                if( CheckUtil.isNumeric(getValueAt(PERM_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = tempOtherRevenue - Integer.parseInt(getValueAt(PERM_SALES_ROW,columnIndex));
                }
                if( CheckUtil.isNumeric(getValueAt(COL_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = tempOtherRevenue - Integer.parseInt(getValueAt(COL_SALES_ROW,columnIndex));
                }
                if( CheckUtil.isNumeric(getValueAt(SPA_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = tempOtherRevenue - Integer.parseInt(getValueAt(SPA_SALES_ROW,columnIndex));
                }
                if( CheckUtil.isNumeric(getValueAt(STREAT_SALES_ROW ,columnIndex)) ){
                    bDraw = true;
                    tempOtherRevenue = tempOtherRevenue - Integer.parseInt(getValueAt(STREAT_SALES_ROW,columnIndex));
                }
                if( bDraw ){
                    tblEditing.setValueAt(tempOtherRevenue, OTHER_REVENUE_ROW , columnIndex);
                }else{
                    tblEditing.setValueAt(null, OTHER_REVENUE_ROW , columnIndex);
                }
            }
            setSum(OTHER_REVENUE_ROW );
        }
    }
    
}

/*
 * TargetMonthPerformanceTablePanel.java
 *
 * Created on 2013/04/29, 15:41
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.NumberUtil;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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
 * @author  nakhoa
 */
public class TargetMonthPerformanceTablePanel extends javax.swing.JPanel
{
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
    private static final int SUM_COLUMN = JANUARY_COLUMN + 12;
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
    //class variables
    private int year;
    private int month;
    private Integer shopId;
    private ListDataTargetOX listmonth = new ListDataTargetOX();
    private ListDataTargetOX listItemTargetOX = new ListDataTargetOX();
    private ListDataTargetOX listDataTargetOx = new ListDataTargetOX();

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

    /** Creates new form TargetMonthPerformanceTablePanel */
    public TargetMonthPerformanceTablePanel()
    {
        this.setSize( 1000, 970 );
        initComponents();
        this.tableAllInfo.setDefaultRenderer( Object.class, new TableRendererEx() );
        this.tableAllInfo.setDefaultEditor( Object.class, new TableCellEditorEx() );
        this.initTableHeaderMonths();
        this.initSecondColumn();
    }

    private void resetTotal(){
        this.totalTechnic = 0;
        this.totalTechnicOX = 0;
        this.totalItem = 0;
        this.totalItemOX = 0;
        this.totalSales = 0;
        this.totalSalesOX = 0;
        this.totalCota = 0;
        this.totalCotaOX = 0;
        this.totalCutNum = 0;
        this.totalCutNumOX = 0;
        this.totalCutSales = 0;
        this.totalCutSalesOX = 0;
        this.totalPermNum = 0;
        this.totalPermNumOX = 0;
        this.totalPermSale = 0;
        this.totalPermSaleOX = 0;
        this.totalColSales = 0;
        this.totalColSalesOX = 0;
        this.totalSpaNum = 0;
        this.totalSpaNumOX = 0;
        this.totalSpaSales = 0;
        this.totalSpaSalesOX = 0;
        this.totalTreatNum = 0;
        this.totalTreatNumOX = 0;
        this.totalTreatSales = 0;
        this.totalTreatSalesOX = 0;
        this.totalOtherRevenue = 0;
        this.totalOtherRevenueOX = 0;
        this.totalItemNum = 0;
        this.totalItemNumOX = 0;
        this.totalyNum = 0;
        this.totalyNumOX = 0;
        this.totalmNum = 0;
        this.totalmNumOX = 0;
        this.totalUnNum = 0;
        this.totalUnNumOX = 0;
        this.totalNewNum = 0;
        this.totalNewNumOX = 0;
        this.totalUsallyNum = 0;
        this.totalUsallyNumOX = 0;
        this.totalNewRepertNum = 0;
        this.totalNewRepertNumOX = 0;
        this.totalNumCustomer = 0;
        this.totalNumCustomerOX = 0;
        this.totalIntroNum = 0;
        this.totalIntroNumOX = 0;
        this.totalNominatNum = 0;
        this.totalOpenDayOX = 0;
        this.totaleStylistOX = 0;
        this.totaleAssistantOX = 0;
        this.totalarStylistOX = 0;
        this.totalarAssistantOX = 0;
        this.totalarOtherOX = 0;
        this.columTotalOX = 0;
    }

    private void init(){
        listmonth = new ListDataTargetOX();
        listItemTargetOX = new ListDataTargetOX();
        listDataTargetOx = new ListDataTargetOX();
        resetTotal();
    }

    public void loadYearData( int nShopId, int year )
    {
	this.shopId = nShopId;
        this.year = year;
        ConnectionWrapper con = SystemInfo.getConnection();
        try
        {
            init();
            listmonth.loadPerformance(con,shopId , year);
            listDataTargetOx.loadAllTargetDataOX(con, shopId, year);
            updateTableTargetOXMonth();
        }catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private void updateColumn( int columnNum, DataTargetOX salesInfo,DataTargetOX goalData )
    {
        int rowIndex = 1;
        // Table ÀÑ
        TableModel tableModel = this.tableAllInfo.getModel();
        Integer totalSale = 0;
        long otherRevenue = 0;
        totalTechnic = totalTechnic + salesInfo.getTechnicID();
        tableModel.setValueAt( salesInfo.getTechnicIdStr(), rowIndex++,columnNum );         // ‹Zp”„ã
        totalItem = totalItem + salesInfo.getItem();
        tableModel.setValueAt( salesInfo.getItemStr(), rowIndex++,columnNum );              // ‘“X”Ì
        // ‘”„ã
        totalSale = salesInfo.getTechnicID() + salesInfo.getItem();
        totalSales = totalSales + totalSale;
        tableModel.setValueAt( String.valueOf(totalSale) , rowIndex++,columnNum);           // ‘”„ã
        totalCota = totalCota + salesInfo.getCota();
        tableModel.setValueAt( salesInfo.getCotaStr(), rowIndex++,columnNum );              // COTA”„ã
        totalCutNum = totalCutNum + salesInfo.getCutNum();
        tableModel.setValueAt( salesInfo.getCutNumStr(), rowIndex++,columnNum );            // CUT‹q”
        totalCutSales = totalCutSales + salesInfo.getCutSale();
        tableModel.setValueAt( salesInfo.getCutSaleStr(), rowIndex++,columnNum );           // CUT”„ã
        totalPermNum = totalPermNum + salesInfo.getPermNum();
        tableModel.setValueAt( salesInfo.getPermNumStr(), rowIndex++,columnNum );           // Perm‹q”
        totalPermSale = totalPermSale + salesInfo.getPermSale();
        tableModel.setValueAt( salesInfo.getPermSaleStr(), rowIndex++,columnNum );          // Perm”„ã
        totalColNum = totalColNum + salesInfo.getColNum();
        tableModel.setValueAt( salesInfo.getColNumStr(), rowIndex++,columnNum );            // Col‹q”
        totalColSales = totalColSales + salesInfo.getColSale();
        tableModel.setValueAt( salesInfo.getColSaleStr(), rowIndex++,columnNum );           // Col”„ã
        totalSpaNum = totalSpaNum + salesInfo.getSpaNum();
        tableModel.setValueAt( salesInfo.getSpaNumStr(), rowIndex++,columnNum );            // ƒXƒp‹q”
        totalSpaSales = totalSpaSales + salesInfo.getSpaSale();
        tableModel.setValueAt( salesInfo.getSpaSaleStr(), rowIndex++,columnNum );           // ƒXƒp”„ã
        totalTreatNum = totalTreatNum + salesInfo.getTreatNum();
        tableModel.setValueAt( salesInfo.getTreatNumStr(), rowIndex++,columnNum );          // ÄØ°ÄÒİÄ‹q”
        totalTreatSales = totalTreatSales + salesInfo.getTreatSale();
        tableModel.setValueAt( salesInfo.getTreatSaleStr(), rowIndex++,columnNum );         // ÄØ°ÄÒİÄ”„ã
        // ‚»‚Ì‘¼”„ã Other revenue
        otherRevenue = totalSale - (salesInfo.getCota() + salesInfo.getCutSale() + salesInfo.getPermSale() + salesInfo.getColSale());
        otherRevenue = otherRevenue - (salesInfo.getSpaSale() + salesInfo.getTreatSale());
        totalOtherRevenue = totalOtherRevenue + otherRevenue ;
        tableModel.setValueAt( String.valueOf(otherRevenue), rowIndex++,columnNum );
        totalItemNum = totalItemNum + salesInfo.getItemNum();
        tableModel.setValueAt( salesInfo.getitemNumStr(), rowIndex++,columnNum );           // “X”Ì”
        totalyNum = totalyNum + salesInfo.getyNum();
        tableModel.setValueAt( salesInfo.getyNumStr(), rowIndex++,columnNum );              // Y‹q”
        totalmNum = totalmNum + salesInfo.getmNum();
        tableModel.setValueAt( salesInfo.getmNumStr(), rowIndex++,columnNum );                 // M‹q”
        totaloNum = totaloNum + salesInfo.getoNum();
        tableModel.setValueAt( salesInfo.getoNumStr(), rowIndex++,columnNum );              // O‹q”
        totalUnNum = totalUnNum + salesInfo.getUnNum();
        tableModel.setValueAt( salesInfo.getUnNumStr(), rowIndex++,columnNum );             // ‚»‚Ì‘¼‹q”
        totalNewNum = totalNewNum + salesInfo.getNewNum();
        tableModel.setValueAt( salesInfo.getNewNumStr(), rowIndex++,columnNum );            // V‹K‹q”
        totalNewRepertNum = totalNewRepertNum + salesInfo.getNewRepertNum();
        // ÀÑF‘O‰ñ—ˆ“X‚©‚ç3ƒJŒˆÈ“à‚É—ˆ“X‚µ‚½ŒÚ‹q”  ( new_repert_num)@@@@@–Ú•WF22-(19+20) 
        tableModel.setValueAt( salesInfo.getNewRepertNumStr(), rowIndex++,columnNum );      // VÄ‹q”
        totalUsallyNum = totalUsallyNum + salesInfo.getUsallyNum();
        tableModel.setValueAt( salesInfo.getUsallyNumStr(), rowIndex++,columnNum );         // Ä—ˆ‹q”
        // ‘‹q”
        long totalNumCus = 0;
        totalNumCus = salesInfo.getoNum() + salesInfo.getyNum() + salesInfo.getmNum();
        totalNumCustomer = totalNumCustomer + totalNumCus;
        tableModel.setValueAt( String.valueOf(totalNumCus), rowIndex++,columnNum );
        totalIntroNum = totalIntroNum + salesInfo.getIntroNum();
        tableModel.setValueAt( salesInfo.getIntroNumStr(), rowIndex++,columnNum );          // Ğ‰îV‹K
        totalNominatNum = totalNominatNum + salesInfo.getNominatNum();
        tableModel.setValueAt( salesInfo.getnominatNumStr(), rowIndex++,columnNum );        // w–¼‹q”
        // Table –Ú•W
        totalTechnicOX = totalTechnicOX + goalData.getTechnicID();
        tableModel.setValueAt( goalData.getTechnicIdStr(), rowIndex++,columnNum );         // ‹Zp”„ã
        totalItemOX = totalItemOX + goalData.getItem();
        tableModel.setValueAt( goalData.getItemStr(), rowIndex++,columnNum );              // ‘“X”Ì
        // ‘”„ã
        totalSale = 0;
        totalSale = goalData.getTechnicID() + goalData.getItem();
        totalSalesOX = totalSalesOX + totalSale;
        tableModel.setValueAt( String.valueOf(totalSale) , rowIndex++,columnNum);           // ‘”„ã
        totalCotaOX = totalCotaOX + goalData.getCota();
        tableModel.setValueAt( goalData.getCotaStr(), rowIndex++,columnNum );              // COTA”„ã
        totalCutNumOX = totalCutNumOX + goalData.getCutNum();
        tableModel.setValueAt( goalData.getCutNumStr(), rowIndex++,columnNum );            // CUT‹q”
        totalCutSalesOX = totalCutSalesOX + goalData.getCutSale();
        tableModel.setValueAt( goalData.getCutSaleStr(), rowIndex++,columnNum );           // CUT”„ã
        totalPermNumOX = totalPermNumOX + goalData.getPermNum();
        tableModel.setValueAt( goalData.getPermNumStr(), rowIndex++,columnNum );           // Perm‹q”
        totalPermSaleOX = totalPermSaleOX + goalData.getPermSale();
        tableModel.setValueAt( goalData.getPermSaleStr(), rowIndex++,columnNum );          // Perm”„ã
        totalColNumOX = totalColNumOX + goalData.getColNum();
        tableModel.setValueAt( goalData.getColNumStr(), rowIndex++,columnNum );            // Col‹q”
        totalColSalesOX = totalColSalesOX + goalData.getColSale();
        tableModel.setValueAt( goalData.getColSaleStr(), rowIndex++,columnNum );           // Col”„ã
        totalSpaNumOX = totalSpaNumOX + goalData.getSpaNum();
        tableModel.setValueAt( goalData.getSpaNumStr(), rowIndex++,columnNum );            // ƒXƒp‹q”
        totalSpaSalesOX = totalSpaSalesOX + goalData.getSpaSale();
        tableModel.setValueAt( goalData.getSpaSaleStr(), rowIndex++,columnNum );           // ƒXƒp”„ã
        totalTreatNumOX = totalTreatNumOX + goalData.getTreatNum();
        tableModel.setValueAt( goalData.getTreatNumStr(), rowIndex++,columnNum );          // ÄØ°ÄÒİÄ‹q”
        totalTreatSalesOX = totalTreatSalesOX + goalData.getTreatSale();
        tableModel.setValueAt( goalData.getTreatSaleStr(), rowIndex++,columnNum );         // ÄØ°ÄÒİÄ”„ã
        // ‚»‚Ì‘¼”„ã Other revenue
        otherRevenue = 0;
        otherRevenue = totalSale - (goalData.getCota() + goalData.getCutSale() + goalData.getPermSale() + goalData.getColSale());
        totalOtherRevenueOX = totalOtherRevenueOX + otherRevenue ;
        tableModel.setValueAt( String.valueOf(otherRevenue), rowIndex++,columnNum );
        totalItemNumOX = totalItemNumOX + goalData.getItemNum();
        tableModel.setValueAt( goalData.getitemNumStr(), rowIndex++,columnNum );           // “X”Ì”
        totalyNumOX = totalyNumOX + goalData.getyNum();
        tableModel.setValueAt( goalData.getyNumStr(), rowIndex++,columnNum );              // Y‹q”
        totalmNumOX = totalmNumOX + goalData.getmNum();
        tableModel.setValueAt( goalData.getmNumStr(), rowIndex++,columnNum );                 // M‹q”
        totaloNumOX = totaloNumOX + goalData.getoNum();
        tableModel.setValueAt( goalData.getoNumStr(), rowIndex++,columnNum );              // O‹q”
        // totalUnNumOX = totalUnNumOX + goalData.getUnNum();
        // tableModel.setValueAt( goalData.getUnNumStr(), rowIndex++,columnNum );             // ‚»‚Ì‘¼‹q”
        totalNumCus = 0;
        totalNumCus = goalData.getTotalNum();
        totalNewNumOX = totalNewNumOX + goalData.getNewNum();
        tableModel.setValueAt( goalData.getNewNumStr(), rowIndex++,columnNum );            // V‹K‹q”
        totalNewRepertNumOX = totalNewRepertNumOX + goalData.getNewRepertNum();
        tableModel.setValueAt( goalData.getNewRepertNumStr(), rowIndex++,columnNum );      // VÄ‹q”
        long totalUsally = 0;
        totalUsallyNumOX = totalUsallyNumOX + totalNumCus;
        totalUsallyNumOX = totalUsallyNumOX - (goalData.getNewNum() + goalData.getNewRepertNum()) ;
        totalUsally = totalNumCus - (goalData.getNewNum() + goalData.getNewRepertNum());
        tableModel.setValueAt( String.valueOf(totalUsally), rowIndex++,columnNum );         // Ä—ˆ‹q”
        totalNumCustomerOX = totalNumCustomerOX + totalNumCus;
        tableModel.setValueAt( String.valueOf(totalNumCus), rowIndex++,columnNum );
        long columtotal = 0;
        totalIntroNumOX = totalIntroNumOX + goalData.getIntroNum();
        tableModel.setValueAt( goalData.getIntroNumStr(), rowIndex++,columnNum );          // Ğ‰îV‹K
        totalNominatNumOX = totalNominatNumOX + goalData.getNominatNum();
        tableModel.setValueAt( goalData.getnominatNumStr(), rowIndex++,columnNum );        // w–¼‹q”
        //tableModel.setValueAt( null, rowIndex++,columnNum );
        // Colum Total
        totalOpenDayOX = totalOpenDayOX + goalData.getOpenDay();
        // ‰Ò“­“ú”  ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( goalData.getopenDayStr(), rowIndex++,columnNum );
        // Ğˆõ½À²Ø½Ä ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        totaleStylistOX = totaleStylistOX + goalData.geteStylist();
        columtotal += goalData.geteStylist();
        tableModel.setValueAt( goalData.geteStylistStr(), rowIndex++,columnNum );
        // Ğˆõ±¼½ÀİÄ ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        totaleAssistantOX = totaleAssistantOX + goalData.geteAssistant();
        columtotal += goalData.geteAssistant();
        tableModel.setValueAt( goalData.geteAssistantStr(), rowIndex++,columnNum );
        // ±ÙÊŞ²Ä½À²Ø½Ä ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        totalarStylistOX = totalarStylistOX + goalData.getArStylist();
        columtotal += goalData.getArStylist();
        tableModel.setValueAt( goalData.getArStylistStr(), rowIndex++,columnNum );
        // ±ÙÊŞ²Ä±¼½ÀİÄ ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        totalarAssistantOX = totalarAssistantOX + goalData.getArAssistant();
        columtotal += goalData.getArAssistant();
        tableModel.setValueAt( goalData.getArAssistantStr(), rowIndex++,columnNum );
        // ±ÙÊŞ²Ä‚»‚Ì‘¼ ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        totalarOtherOX = totalarOtherOX + goalData.getArOther();
        columtotal += goalData.getArOther();
        tableModel.setValueAt( goalData.getArOtherStr(), rowIndex++,columnNum );
        columTotalOX = totaleStylistOX + totaleAssistantOX + totalarStylistOX + totalarAssistantOX + totalarOtherOX;
        tableModel.setValueAt(String.valueOf(columtotal), rowIndex++,columnNum ); // Ğˆõ”
    }

    private boolean updateColumnTotal()
    {
        int columnNum = 14;
        int rowIndex = 1;
        // Table ÀÑ
        TableModel tableModel = this.tableAllInfo.getModel();
        tableModel.setValueAt( String.valueOf(totalTechnic), rowIndex++,columnNum );        // ‹Zp”„ã
        tableModel.setValueAt( String.valueOf(totalItem ), rowIndex++,columnNum );          // ‘“X”Ì
        // ‘”„ã
        tableModel.setValueAt( String.valueOf(totalSales) , rowIndex++,columnNum);          // ‘”„ã
        tableModel.setValueAt( String.valueOf(totalCota ), rowIndex++,columnNum );          // COTA”„ã
        tableModel.setValueAt( String.valueOf(totalCutNum ), rowIndex++,columnNum );        // CUT‹q”
        tableModel.setValueAt( String.valueOf(totalCutSales ), rowIndex++,columnNum );      // CUT”„ã
        tableModel.setValueAt( String.valueOf(totalPermNum ), rowIndex++,columnNum );       // Perm‹q”
        tableModel.setValueAt( String.valueOf(totalPermSale ), rowIndex++,columnNum );      // Perm”„ã
        tableModel.setValueAt( String.valueOf(totalColNum   ), rowIndex++,columnNum );      // Col‹q”
        tableModel.setValueAt( String.valueOf(totalColSales ), rowIndex++,columnNum );      // Col”„ã
        tableModel.setValueAt( String.valueOf(totalSpaNum ), rowIndex++,columnNum );        // ƒXƒp‹q”
        tableModel.setValueAt( String.valueOf(totalSpaSales ), rowIndex++,columnNum );      // ƒXƒp”„ã
        tableModel.setValueAt( String.valueOf(totalTreatNum ), rowIndex++,columnNum );      // ÄØ°ÄÒİÄ‹q”
        tableModel.setValueAt( String.valueOf(totalTreatSales ), rowIndex++,columnNum );    // ÄØ°ÄÒİÄ”„ã
        tableModel.setValueAt( String.valueOf(totalOtherRevenue ), rowIndex++,columnNum );  // ‚»‚Ì‘¼”„ã Other revenue
        tableModel.setValueAt( String.valueOf(totalItemNum ), rowIndex++,columnNum );           // “X”Ì”
        tableModel.setValueAt( String.valueOf(totalyNum ), rowIndex++,columnNum );              // Y‹q”
        tableModel.setValueAt( String.valueOf(totalmNum ), rowIndex++,columnNum );                 // M‹q”
        tableModel.setValueAt( String.valueOf(totaloNum ), rowIndex++,columnNum );              // O‹q”
        tableModel.setValueAt( String.valueOf(totalUnNum ), rowIndex++,columnNum );             // ‚»‚Ì‘¼‹q”
        tableModel.setValueAt( String.valueOf(totalNewNum ), rowIndex++,columnNum );            // V‹K‹q”
        // ÀÑF‘O‰ñ—ˆ“X‚©‚ç3ƒJŒˆÈ“à‚É—ˆ“X‚µ‚½ŒÚ‹q”  ( new_repert_num)@@@@@–Ú•WF22-(19+20)
        tableModel.setValueAt( String.valueOf(totalNewRepertNum  ), rowIndex++,columnNum );
        tableModel.setValueAt( String.valueOf(totalUsallyNum ), rowIndex++,columnNum );         // VÄ‹q”
        tableModel.setValueAt( String.valueOf(totalNumCustomer  ), rowIndex++,columnNum );      // ‘‹q”
        tableModel.setValueAt( String.valueOf(totalIntroNum ), rowIndex++,columnNum );          // Ğ‰îV‹K
        tableModel.setValueAt( String.valueOf(totalNominatNum ), rowIndex++,columnNum );        // w–¼‹q”
        // Table –Ú•W
        tableModel.setValueAt( String.valueOf(totalTechnicOX), rowIndex++,columnNum );        // ‹Zp”„ã
        tableModel.setValueAt( String.valueOf(totalItemOX ), rowIndex++,columnNum );          // ‘“X”Ì
        // ‘”„ã
        tableModel.setValueAt( String.valueOf(totalSalesOX) , rowIndex++,columnNum);          // ‘”„ã
        tableModel.setValueAt( String.valueOf(totalCotaOX ), rowIndex++,columnNum );          // COTA”„ã
        tableModel.setValueAt( String.valueOf(totalCutNumOX ), rowIndex++,columnNum );        // CUT‹q”
        tableModel.setValueAt( String.valueOf(totalCutSalesOX ), rowIndex++,columnNum );      // CUT”„ã
        tableModel.setValueAt( String.valueOf(totalPermNumOX ), rowIndex++,columnNum );       // Perm‹q”
        tableModel.setValueAt( String.valueOf(totalPermSaleOX ), rowIndex++,columnNum );      // Perm”„ã
        tableModel.setValueAt( String.valueOf(totalColNumOX   ), rowIndex++,columnNum );      // Col‹q”
        tableModel.setValueAt( String.valueOf(totalColSalesOX ), rowIndex++,columnNum );      // Col”„ã
        tableModel.setValueAt( String.valueOf(totalSpaNumOX ), rowIndex++,columnNum );        // ƒXƒp‹q”
        tableModel.setValueAt( String.valueOf(totalSpaSalesOX ), rowIndex++,columnNum );      // ƒXƒp”„ã
        tableModel.setValueAt( String.valueOf(totalTreatNumOX ), rowIndex++,columnNum );      // ÄØ°ÄÒİÄ‹q”
        tableModel.setValueAt( String.valueOf(totalTreatSalesOX ), rowIndex++,columnNum );    // ÄØ°ÄÒİÄ”„ã
        tableModel.setValueAt( String.valueOf(totalOtherRevenueOX ), rowIndex++,columnNum );  // ‚»‚Ì‘¼”„ã Other revenue
        tableModel.setValueAt( String.valueOf(totalItemNumOX ), rowIndex++,columnNum );           // “X”Ì”
        tableModel.setValueAt( String.valueOf(totalyNumOX ), rowIndex++,columnNum );              // Y‹q”
        tableModel.setValueAt( String.valueOf(totalmNumOX ), rowIndex++,columnNum );                 // M‹q”
        tableModel.setValueAt( String.valueOf(totaloNumOX ), rowIndex++,columnNum );              // O‹q”
        // tableModel.setValueAt( String.valueOf(totalUnNumOX ), rowIndex++,columnNum );             // ‚»‚Ì‘¼‹q”
        tableModel.setValueAt( String.valueOf(totalNewNumOX ), rowIndex++,columnNum );            // V‹K‹q”
        // ÀÑF‘O‰ñ—ˆ“X‚©‚ç3ƒJŒˆÈ“à‚É—ˆ“X‚µ‚½ŒÚ‹q”  ( new_repert_num)@@@@@–Ú•WF22-(19+20)
        tableModel.setValueAt( String.valueOf(totalNewRepertNumOX  ), rowIndex++,columnNum );
        tableModel.setValueAt( String.valueOf(totalUsallyNumOX ), rowIndex++,columnNum );         // VÄ‹q”
        tableModel.setValueAt( String.valueOf(totalNumCustomerOX  ), rowIndex++,columnNum );      // Ä—ˆ‹q”
        tableModel.setValueAt( String.valueOf(totalIntroNumOX ), rowIndex++,columnNum );          // Ğ‰îV‹K
        tableModel.setValueAt( String.valueOf(totalNominatNumOX ), rowIndex++,columnNum );        // w–¼‹q”
        // Colum Total
        // ‰Ò“­“ú”  ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( String.valueOf(totalOpenDayOX ), rowIndex++,columnNum );
        // Ğˆõ½À²Ø½Ä ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( String.valueOf(totaleStylistOX ), rowIndex++,columnNum );
        // Ğˆõ±¼½ÀİÄ ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( String.valueOf(totaleAssistantOX ), rowIndex++,columnNum );
        // ±ÙÊŞ²Ä½À²Ø½Ä ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( String.valueOf(totalarStylistOX ), rowIndex++,columnNum );
        // ±ÙÊŞ²Ä±¼½ÀİÄ ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( String.valueOf(totalarAssistantOX ), rowIndex++,columnNum );
        // ±ÙÊŞ²Ä‚»‚Ì‘¼ ==> “ü—Í‚µ‚Ä“ü‚ê‚é€–Ú‚Å‚·B
        tableModel.setValueAt( String.valueOf(totalarOtherOX ), rowIndex++,columnNum );
        // Ğˆõ”
        tableModel.setValueAt( String.valueOf(columTotalOX ), rowIndex++,columnNum ); // Ğˆõ”
        return true;
    }

    public void updateTableTargetOXMonth()
    {
        DataTargetOX oxBean = null;
        DataTargetOX oxGoal = null;
        for( int index = 0, monthIndex = 1; index < MONTHS_COUNT; index++,monthIndex++ )
        {
            oxBean = this.listmonth.get(index);
            oxGoal = this.listDataTargetOx.get(index);
            if( oxBean != null )
            {
                this.updateColumn( LAST_READONLY_COL + monthIndex, oxBean,oxGoal);
            }
            else
            {
                this.clearColumn( LAST_READONLY_COL + monthIndex );
            }
        }
        this.updateColumnTotal();
    }

    public boolean registerAllDataToDB()
    {
        this.stopCellEditing();
        this.readAllFields();
        if(this.listItemTargetOX.saveToDatabase() == false){
            return false;
        }else{
            this.loadYearData(this.shopId,this.year );
            return true;
        }
    }
    
    public void resetCellEditedFlag()
    {
        // in calling tableAllInfo.getColumnClass() method, you can use column numbers 2-13
        ((TableCellEditorEx)this.tableAllInfo.getDefaultEditor( this.tableAllInfo.getColumnClass(3))).resetCellEditedFlag();
    }
    
    private void clearColumn( int columnNum )
    {
        TableModel tableModel = this.tableAllInfo.getModel();
        for( int rowIndex = FIRST_EDITABLE_ROW; rowIndex <= LAST_ROW_INDEX; rowIndex++ )
        {
            tableModel.setValueAt( "", rowIndex,columnNum );
        }
    }
    
    protected void readAllFields()
    {
        DataTargetOX  itemTargetOX = new DataTargetOX();
        int colIndex = LAST_READONLY_COL + 1;
        for( int index = 0; index < MONTHS_COUNT; index++ )
        {
            itemTargetOX = new DataTargetOX();
            // “X•ÜID
            itemTargetOX.setShopID(this.shopId);
            // ”N
            itemTargetOX.setYear(this.year);
            // Œ
            itemTargetOX.setMonth(index + 1);
            // ‹Zp”„ã
            if(this.getCellValue(TECHNIC_ROW, colIndex ) != null){
                itemTargetOX.setTechnicID(Integer.parseInt(this.getCellValue( TECHNIC_ROW, colIndex )));
            }else{
                itemTargetOX.setTechnicID(null);
            }
            // ‘“X”Ì
            if(this.getCellValue(ITEM_ROW , colIndex ) != null){
                itemTargetOX.setItem(Integer.parseInt(this.getCellValue(ITEM_ROW , colIndex )));
            }else{
                itemTargetOX.setItem(null);
            }
            // COTA”„ã
            if(this.getCellValue(COTA_SALES_ROW , colIndex ) != null){
                itemTargetOX.setCota(Integer.parseInt(this.getCellValue(COTA_SALES_ROW , colIndex )));
            }else{
                itemTargetOX.setCota(null);
            }
            // CUT‹q”
            if(this.getCellValue(CUT_NUM_ROW, colIndex ) != null){
                itemTargetOX.setCutNum(Integer.parseInt(this.getCellValue(CUT_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setCutNum(null);
            }
            // CUT”„ã
            if(this.getCellValue(CUT_SALES_ROW, colIndex ) != null){
                itemTargetOX.setCutSale(Integer.parseInt(this.getCellValue(CUT_SALES_ROW, colIndex )));
            }else{
                itemTargetOX.setCutSale(null);
            }
            // Perm‹q”
            if(this.getCellValue(PERM_NUM_ROW, colIndex ) != null){
                itemTargetOX.setPermNum(Integer.parseInt(this.getCellValue(PERM_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setPermNum(null);
            }
            // Perm”„ã
            if(this.getCellValue(PERM_SALES_ROW, colIndex ) != null){
                itemTargetOX.setPermSale(Integer.parseInt(this.getCellValue(PERM_SALES_ROW, colIndex )));
            }else{
                itemTargetOX.setPermSale(null);
            }
            // Col‹q”
            if(this.getCellValue(COL_NUM_ROW, colIndex ) != null){
                itemTargetOX.setColNum(Integer.parseInt(this.getCellValue(COL_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setColNum(null);
            }
            // Col”„ã
            if(this.getCellValue(COL_SALES_ROW, colIndex ) != null){
                itemTargetOX.setColSale(Integer.parseInt(this.getCellValue(COL_SALES_ROW, colIndex )));
            }else{
                itemTargetOX.setColSale(null);
            }
            // ƒXƒp‹q”
            if(this.getCellValue(SPA_NUM_ROW, colIndex ) != null){
                itemTargetOX.setSpaNum(Integer.parseInt(this.getCellValue(SPA_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setSpaNum(null);
            }
            // ƒXƒp”„ã
            if(this.getCellValue(SPA_SALES_ROW, colIndex ) != null){
                itemTargetOX.setSpaSale(Integer.parseInt(this.getCellValue(SPA_SALES_ROW, colIndex )));
            }else{
                itemTargetOX.setSpaSale(null);
            }
            // ÄØ°ÄÒİÄ‹q”
            if(this.getCellValue(STREAT_NUM_ROW, colIndex ) != null){
                itemTargetOX.setTreatNum(Integer.parseInt(this.getCellValue(STREAT_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setTreatNum(null);
            }
            // ÄØ°ÄÒİÄ”„ã
            if(this.getCellValue(STREAT_SALES_ROW, colIndex ) != null){
                itemTargetOX.setTreatSale(Integer.parseInt(this.getCellValue(STREAT_SALES_ROW, colIndex )));
            }else{
                itemTargetOX.setTreatSale(null);
            }
            // “X”Ì”
            if(this.getCellValue(ITEM_NUM_ROW, colIndex ) != null){
                itemTargetOX.setItemNum(Integer.parseInt(this.getCellValue(ITEM_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setItemNum(null);
            }
            // Y‹q”
            if(this.getCellValue(Y_NUM_ROW, colIndex ) != null){
                itemTargetOX.setyNum(Integer.parseInt(this.getCellValue(Y_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setyNum(null);
            }
            // O‹q”
            if(this.getCellValue(O_NUM_ROW, colIndex ) != null){
                itemTargetOX.setoNum(Integer.parseInt(this.getCellValue(O_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setoNum(null);
            }
            // V‹K‹q”
            if(this.getCellValue(NEW_NUM_ROW, colIndex ) != null){
                itemTargetOX.setNewNum(Integer.parseInt(this.getCellValue(NEW_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setNewNum(null);
            }
            // VÄ‹q”
            if(this.getCellValue(NEW_REPERT_NUM, colIndex ) != null){
                itemTargetOX.setNewRepertNum(Integer.parseInt(this.getCellValue(NEW_REPERT_NUM, colIndex )));
            }else{
                itemTargetOX.setNewRepertNum(null);
            }
            // ‘‹q”
            if(this.getCellValue(TOTAL_NUM_CUS, colIndex ) != null){
                itemTargetOX.setTotalNum(Integer.parseInt(this.getCellValue(TOTAL_NUM_CUS, colIndex )));
            }else{
                itemTargetOX.setTotalNum(null);
            }
            // Ğ‰îV‹K
            if( this.getCellValue(INTRO_NUM_ROW, colIndex ) != null){
                itemTargetOX.setIntroNum(Integer.parseInt(this.getCellValue(INTRO_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setIntroNum(null);
            }
            // w–¼‹q”
            if(this.getCellValue(NOMINAT_NUM_ROW, colIndex ) != null){
                itemTargetOX.setNominatNum(Integer.parseInt(this.getCellValue(NOMINAT_NUM_ROW, colIndex )));
            }else{
                itemTargetOX.setNominatNum(null);
            }
            // ‰Ò“­“ú”
            if(this.getCellValue(OPEN_DAY_ROW, colIndex ) != null){
                itemTargetOX.setOpenDay(Integer.parseInt(this.getCellValue(OPEN_DAY_ROW, colIndex )));
            }else{
                itemTargetOX.setOpenDay(null);
            }
            // Ğˆõ½À²Ø½Ä
            if(this.getCellValue(E_STYLIST_ROW, colIndex ) != null){
                itemTargetOX.seteStylist(Integer.parseInt(this.getCellValue(E_STYLIST_ROW, colIndex )));
            }else{
                itemTargetOX.seteStylist(null);
            }
            // Ğˆõ±¼½ÀİÄ
            if(this.getCellValue(E_ASSISTANT_ROW, colIndex ) != null){
                itemTargetOX.seteAssistant(Integer.parseInt(this.getCellValue(E_ASSISTANT_ROW, colIndex )));
            }else{
                itemTargetOX.seteAssistant(null);
            }
            // ±ÙÊŞ²Ä½À²Ø½
            if(this.getCellValue(AR_STYLIST_ROW, colIndex ) != null){
                itemTargetOX.setArStylist(Integer.parseInt(this.getCellValue(AR_STYLIST_ROW, colIndex )));
            }else{
                itemTargetOX.setArStylist(null);
            }
            // ±ÙÊŞ²Ä±¼½ÀİÄ
            if(this.getCellValue(AR_ASSISTANT_ROW, colIndex ) != null){
                itemTargetOX.setArAssistant(Integer.parseInt(this.getCellValue(AR_ASSISTANT_ROW, colIndex )));
            }else{
                itemTargetOX.setArAssistant(null);
            }
            // ±ÙÊŞ²Ä‚»‚Ì‘¼
            if(this.getCellValue(AR_OTHER_ROW, colIndex ) != null){
                itemTargetOX.setArOther(Integer.parseInt(this.getCellValue(AR_OTHER_ROW, colIndex )));
            }else{
                itemTargetOX.setArOther(null);
            }
            this.listItemTargetOX.add(itemTargetOX);
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
        int monthNum = 1;
        
        String columnVal = "";
        tableModel.setValueAt( columnVal, 0, colIndex++ );
        tableModel.setValueAt( columnVal, 0, colIndex );
        
        for( colIndex = 2; colIndex < this.tableAllInfo.getColumnCount()-1; colIndex ++ )
        {
            columnVal = monthNum++ + "Œ";
            tableModel.setValueAt( columnVal, 0, colIndex );
        }
        columnVal = "‡Œv";
        tableModel.setValueAt( columnVal, 0, colIndex );
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
        tableModel.setValueAt( "‹Zp”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "‘“X”Ì", rowIndex++, 1 );
        tableModel.setValueAt( "‘”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "COTA”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "CUT‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "CUT”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "Perm‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Perm”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "Col‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Col”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "ƒXƒp‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "ƒXƒp”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "ÄØ°ÄÒİÄ‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "ÄØ°ÄÒİÄ”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "‚»‚Ì‘¼”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "“X”Ì”", rowIndex++, 1 );
        tableModel.setValueAt( "Y‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "M‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "O‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "‚»‚Ì‘¼‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "V‹K‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "VÄ‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Ä—ˆ‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "‘‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Ğ‰îV‹K", rowIndex++, 1 );
        tableModel.setValueAt( "w–¼‹q”", rowIndex++, 1 );
       
        tableModel.setValueAt( "‹Zp”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "‘“X”Ì", rowIndex++, 1 );
        tableModel.setValueAt( "‘”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "COTA”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "CUT‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "CUT”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "Perm‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Perm”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "Col‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Col”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "ƒXƒp‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "ƒXƒp”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "ÄØ°ÄÒİÄ‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "ÄØ°ÄÒİÄ”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "‚»‚Ì‘¼”„ã", rowIndex++, 1 );
        tableModel.setValueAt( "“X”Ì”", rowIndex++, 1 );
        tableModel.setValueAt( "Y‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "M‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "O‹q”", rowIndex++, 1 );
        // tableModel.setValueAt( "‚»‚Ì‘¼‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "V‹K‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "VÄ‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Ä—ˆ‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "‘‹q”", rowIndex++, 1 );
        tableModel.setValueAt( "Ğ‰îV‹K", rowIndex++, 1 );
        tableModel.setValueAt( "w–¼‹q”", rowIndex++, 1 );
       
        tableModel.setValueAt( "ÀÑ", 12, 0 );
        tableModel.setValueAt( "–Ú•W", 37, 0 );
        
        tableModel.setValueAt( "‰Ò“­", 52, 0 );
        tableModel.setValueAt( "“ú”", 52, 1 );
        tableModel.setValueAt( "Ğˆõ½", 53, 0 );
        tableModel.setValueAt( "À²Ø½Ä", 53, 1 );
        tableModel.setValueAt( "Ğˆõ±", 54, 0 );
        tableModel.setValueAt( "¼½ÀİÄ", 54, 1 );
        tableModel.setValueAt( "±ÙÊŞ²Ä", 55, 0 );
        tableModel.setValueAt( "½À²Ø½Ä", 55, 1 );
        tableModel.setValueAt( "±ÙÊŞ²Ä", 56, 0 );
        tableModel.setValueAt( "±¼½ÀİÄ", 56, 1 );
        tableModel.setValueAt( "±ÙÊŞ²", 57, 0 );
        tableModel.setValueAt( "Ä‚»‚Ì‘¼", 57, 1 );
        tableModel.setValueAt( "Ğˆõ", 58, 0 );
        tableModel.setValueAt( "”", 58, 1 );
        
        
       
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

    public void setYear(int year){
        this.year = year;
    }

    public int getMonth(){
        return this.month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(Integer shopId)
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
        setPreferredSize(new java.awt.Dimension(1000, 976));

        tableAllInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
            new String [] {
                "ƒ^ƒCƒgƒ‹ 1", "ƒ^ƒCƒgƒ‹ 2", "ƒ^ƒCƒgƒ‹ 3", "ƒ^ƒCƒgƒ‹ 4", "ƒ^ƒCƒgƒ‹ 5", "ƒ^ƒCƒgƒ‹ 6", "ƒ^ƒCƒgƒ‹ 7", "ƒ^ƒCƒgƒ‹ 8", "ƒ^ƒCƒgƒ‹ 9", "ƒ^ƒCƒgƒ‹ 10", "ƒ^ƒCƒgƒ‹ 11", "ƒ^ƒCƒgƒ‹ 12", "ƒ^ƒCƒgƒ‹ 13", "ƒ^ƒCƒgƒ‹ 14", "ƒ^ƒCƒgƒ‹ 15"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
                .addComponent(tableAllInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 990, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tableAllInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
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
            /*
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
            if (column == 0 && row >= 53) {
                horizAlignment = SwingConstants.RIGHT;
            } else if (column == 1 && row >= 53) {
                horizAlignment = SwingConstants.LEFT;
            }
            Border border = BorderFactory.createMatteBorder( 1, 1, 0, 0, Color.GRAY );
            if( ( row == MONTHS_LABEL_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                border = BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.GRAY );
            } else if( ( row == LAST_ROW ) && ( column == FIELDS_LABEL_COLUMN ) ) {
                border = BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.GRAY );
             } else if ((row == 54) && (column == FIELDS_LABEL_COLUMN)) {
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 55) && (column == FIELDS_LABEL_COLUMN)) {
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 56) && (column == FIELDS_LABEL_COLUMN)) {
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 57) && (column == FIELDS_LABEL_COLUMN)) {
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 58) && (column == FIELDS_LABEL_COLUMN)) {
                 border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);
             } else if ((row == 59) && (column ==FIELDS_LABEL_COLUMN)) {
                 border = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
             } else if ((row == 59) && (column ==0)) {
                 border = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
            } else if( ( column == 0 ) && ( row > 1 && row < 27 ) ) {
                border = BorderFactory.createMatteBorder( 0, 1, 0, 0, Color.GRAY );
            } else if( ( column == TYPE_LABEL_COLUMN ) && ( row > 27 && row < LAST_ROW ) ) {
                border = BorderFactory.createMatteBorder( 0, 1, 0, 0, Color.GRAY );
            } else if( ( row == MONTHS_LABEL_ROW ) || ( column == TYPE_LABEL_COLUMN ) || ( column == FIELDS_LABEL_COLUMN ) ) {
            } else if( ( row == 29 )|| ( row == 41 ) ||
                    ( row == 44 ) || ( row == 49 )|| ( row == 59 ) ) {
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
            
            */
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
            } else if( ( column == 0 ) && ( row > 1 && row < 27 ) ) {
                temp.setDocumentFilter(new CustomFilter(20));
            } else if( ( column == TYPE_LABEL_COLUMN ) && ( row > 27 && row < LAST_ROW ) ) {
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
                setMNum(col);
                setUsallyNum(col);
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
                    setMNum(col);
                    setUsallyNum(col);
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

        private void setMNum(int col) {
            Integer resultMNum = 0;
            boolean bDraw = false;
            if( CheckUtil.isNumeric(getValueAt(TOTAL_NUM_CUS  ,col)) ){
                bDraw = true;
                resultMNum = Integer.parseInt(getValueAt(TOTAL_NUM_CUS ,col));
            }
            if( CheckUtil.isNumeric(getValueAt(Y_NUM_ROW,col)) ){
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

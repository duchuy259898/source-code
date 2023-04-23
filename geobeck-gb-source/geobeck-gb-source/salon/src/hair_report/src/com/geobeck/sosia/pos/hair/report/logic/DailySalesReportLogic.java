/*
 * DailySalesReportLogic.java
 *
 * Created on 2008/09/16, 15:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.StoreReportAttendanceBean;
import com.geobeck.sosia.pos.hair.report.beans.StoreReportExpenditureBean;
import com.geobeck.sosia.pos.hair.report.beans.StoreReportParameterBean;
import com.geobeck.sosia.pos.hair.report.beans.StoreReportRateBean;
import com.geobeck.sosia.pos.hair.report.beans.StoreSubReportBean;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.util.FormatUtil;
import com.geobeck.util.NumberUtil;
import com.geobeck.util.SQLUtil;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.text.SimpleDateFormat;

// use for JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.account.*;


/**
 *
 * @author trino
 */
public class DailySalesReportLogic extends ReportGeneratorLogic {
    // <editor-fold defaultstate="collapsed" desc="Definitions and Variables">
    public static String REPORT_STOREDAILY                              = "StoreDailyReport";
    public static String REPORT_STOREDAILY_JASPER                       = "StoreDailyReport.jasper";
    public static String REPORT_STOREDAILY_TECHNIC			= "StoreDailyReport_Technic.jasper";
    public static String REPORT_STOREDAILY_TECHNICRATE                  = "StoreDailReport_Rate.jasper";
    public static String REPORT_STOREDAILY_EXPENDITURE                  = "StoreDailyReport_Expenditure.jasper";
    public static String REPORT_STOREDAILY_ATTEND                       = "StoreDailyReport_Attendance.jasper";
    
    // Input Parameters
    private int shopId;
    private MstShop shop;
    private Date outputDate = null;
    private String weather;
    private String memo;
    private Date exitTime  = null;
    private Date closeTime = null;
    private int staffId;
    
    private HashMap<String,Object> paramMemMap = null;
    private StoreSubReportBean dataSubReport = null;
    private ConnectionWrapper dbConnection = null;
    
    private long technictaxtotal = 0;
    private long stocktaxtotal   = 0;
    private double taxrate = 0;
    private long technicAmount = 0;
    private long monitorAmount = 0;
    
    private final String newLine = "\n";
    private final String comma = ",";
    private final String apostrophe = "'";
    
    private final int PRODUCT_DISCOUNT = 0;
    private final int PRODUCT_TECHNIC = 1;
    private final int PRODUCT_STOCK   = 2;
    private final int TECHNIC_COUNT   = 9;
    //�q
    private final int CUSTOMER_ALLMEMBER    = 0;
    private final int CUSTOMER_NONMEMBER    = 1;
    private final int CUSTOMER_NEWMEMBER    = 2;
    
    private Register register;
    
    //</editor-fold>
    
    
    /** Creates a new instance of DailySalesReportLogic */
    public DailySalesReportLogic(){
        dbConnection = SystemInfo.getConnection();
        
    }
    
    public void setShopId(int shopId){
        this.shopId = shopId;
    }
    
    public int getShopId(){
        return this.shopId;
    }
    
    public void setShop(MstShop mst_shop){
        this.shop = mst_shop;
    }
    
    public MstShop getShop(){
        return this.shop;
    }
    
    public void setOutputDate(Date outDate){
        this.outputDate = outDate;
    }
    
    public Date getOutputDate(){
        return this.outputDate;
    }
    
    public void setWeather(String tenki){
        this.weather = tenki;
    }
    
    public String getWeather(){
        return this.weather;
    }
    
    public void setMemo(String strMemo){
        this.memo = strMemo;
    }
    
    public String getMemo(){
        return this.memo;
    }
    
    public void setExitTime(Date timeExit){
        this.exitTime = timeExit;
    }
    
    public Date getExitTime(){
        return this.exitTime;
    }
    
    public void setCloseTime(Date timeClose){
        this.closeTime = timeClose;
    }
    
    public Date getCloseTime(){
        return this.closeTime;
    }
    
    public void setStaffId(int id){
        this.staffId = id;
    }
    
    public int getStaffId(){
        return this.staffId;
    }
    
    //�X�ܖ����擾����
    //�Ԃ��F�@�X��
    private String getShopName() {
        String sql= "select mst_shop.shop_name from mst_shop " +
                "where mst_shop.delete_date is NULL " +
                "and	 mst_shop.shop_id = " + this.getShopId();
        String storename = null;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next()) {
                storename = rs.getString("shop_name");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return storename;
    }
    
    // �Z�p����̓���A�䗦�󋵂��擾����
    private void makeTechnicList(double cntCustomer) {
        ArrayList<StoreReportParameterBean> technicList = new ArrayList<StoreReportParameterBean>();
        ArrayList<StoreReportRateBean> rateList = new ArrayList<StoreReportRateBean>();
        String sql = this.technicSql();
        long sales = 0;
        long tax  = 0;
        String strTechName;
        int nTechCnt;
        int nTotalTechCntForRate;
        
        technicAmount = 0;
        monitorAmount = 0;
        
        //System.out.println(sql);
        StoreReportParameterBean bean  = null;
        StoreReportRateBean beanrate = null;
        nTotalTechCntForRate = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            while(rs.next()) {
                bean = new StoreReportParameterBean();
                beanrate = new StoreReportRateBean();
                
                sales  = 0;
                tax = 0;
                
                NumberFormat fmtNum = NumberFormat.getNumberInstance();
                
                //�Z�p�����擾
                strTechName = rs.getString("techName");
                nTechCnt = rs.getInt("techCount");
                
                bean.setTechnicName(strTechName);
                if( strTechName.equals("���j�^") ){
                    bean.setTechnicCount("[" + fmtNum.format(nTechCnt) + "]");
                }else{
                    bean.setTechnicCount(fmtNum.format(nTechCnt));
                }
                 
                if (
                        !(
                        strTechName.equals("�J�b�g")
                        ||  strTechName.equals("�N���[��")
                        ||  strTechName.equals("���j�^")
                        )
                        ) {
                    //�䗦��
                    beanrate.setTechnicName(strTechName);
                    
                    //���X�q��
                    beanrate.setTechnicCount(nTechCnt);
                    
                    if( rateList.size() < TECHNIC_COUNT ) {
                        rateList.add(beanrate);
                    }
                }
                
                //�����X�q��
                nTotalTechCntForRate += rs.getInt("visitCount");
                
                //�Z�p���z�@=�@�擾�����Z�p���z�@-�@�Z�p�̏����
                sales = rs.getLong("techSales");
                //tax = TaxUtil.getTax(sales, this.taxrate,3);
                
                if( strTechName.equals("���j�^") ){
                    bean.setTechnicTotal( "[" + fmtNum.format(sales) + "]" );
                    monitorAmount += sales;
                }else{
                    bean.setTechnicTotal( fmtNum.format(sales)  );
                    technicAmount += sales;
                }
                tax = rs.getLong("taxInTechnic") - sales;
                this.technictaxtotal += tax;
                
                
                //���X�g�ɒǉ�
                if( technicList.size() < TECHNIC_COUNT ) {
                    technicList.add(bean);
                }
            }
            
            //�Z�p���X�g�T�C�Y��9�s�܂łɐݒ肷��
            for(int i = technicList.size(); i < TECHNIC_COUNT ; i++) {
                bean = new StoreReportParameterBean();
                bean.setTechnicName(null);
                technicList.add(bean);
            }
            
            // �e�䗦�����߂�
/*            
            if (nTotalTechCntForRate != 0) {
                for (int i = 0; i < rateList.size(); ++i) {
                    rateList.get(i).setTechnicRate( (double)rateList.get(i).getTechnicCount() / (double)nTotalTechCntForRate );
                }
            }
*/
            if (cntCustomer != 0) {
                for (int i = 0; i < rateList.size(); ++i) {
                    rateList.get(i).setTechnicRate( (double)rateList.get(i).getTechnicCount() / (double)cntCustomer );
                }
            }
            
            //�䗦�󋵃��X�g�T�C�Y��9�s�܂łɐݒ肷��
            for(int i= rateList.size(); i < TECHNIC_COUNT ; i++) {
                beanrate = new StoreReportRateBean();
                beanrate.setTechnicName(null);
                rateList.add(beanrate);
            }
            
            //Container�ɒǉ�����
            this.dataSubReport.setSubStoreTechnicReport(new JRBeanCollectionDataSource(technicList));
            this.dataSubReport.setSubStoreTechnicRateReport(new JRBeanCollectionDataSource(rateList));
        } catch( Exception e ) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private int getStaffAttendance() {
        ArrayList<StoreReportAttendanceBean> staffList = new ArrayList<StoreReportAttendanceBean>();
        ArrayList<String> inList = new ArrayList<String>();
        ArrayList<String> outList = new ArrayList<String>();
        
        StoreReportAttendanceBean staff = null;
        String sql = this.staffStatusSql();
        String status = null;
        String name = null;
        int cnt = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            while(rs.next()) {
                status = null;
                name = null;
                status = "(" + rs.getString("disp_name") + ") ";
                name = rs.getString("staff_name1") + " " + rs.getString("staff_name2");
                if (rs.getInt("working") == 0 ){
                    outList.add(status + name);
                }else{
                    inList.add(status + name);
                }
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        if( inList.size() >= outList.size() ) {
            cnt = inList.size();
        } else {
            cnt = outList.size();
        }
        
        for(int index =0 ; index < cnt ; index++) {
            staff = new StoreReportAttendanceBean();
            if( inList.size() > index ) {
                staff.setInStaffName(inList.get(index));
            }
            
            if( outList.size() > index ) {
                staff.setOutStaffName(outList.get(index));
            }
            
            staffList.add(staff);
        }
        
        for(int i=staffList.size(); i < 12; i++) {
            staff = new StoreReportAttendanceBean();
            staff.setInStaffName(null);
            staffList.add(staff);
        }
        
        this.dataSubReport.setSubStaffAttendance(new JRBeanCollectionDataSource(staffList));
        
        return inList.size();
    }
    
    //�����o��x�o
    private long getExpenditure() {
        ArrayList<StoreReportExpenditureBean> expendList = new ArrayList<StoreReportExpenditureBean>();
        StoreReportExpenditureBean expend = null;
        boolean inOut = false;
        long outVal = 0;
        long inVal = 0;
        long ioVal = 0;
        
        String sql = this.expendSql();
        try {
            ResultSetWrapper rs  = this.dbConnection.executeQuery(sql);
            while( rs.next() ) {
                expend = new StoreReportExpenditureBean();
                inOut = rs.getBoolean("in_out");
                ioVal = rs.getLong("io_value");
                expend.setExpenditureName(rs.getString("use_for"));
                if(inOut == true) {
                    inVal += ioVal;
                    expend.setExpenditureValue(ioVal); 
                } else if( inOut == false) {
                    outVal += ioVal;
                    expend.setExpenditureValue(-1 * ioVal);
                }
                
                if( expendList.size() < 6 ) {
                    expendList.add(expend);
                }
                
            }
        } catch(Exception e ) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        for(int i=expendList.size(); i<6 ; i++) {
            expend = new StoreReportExpenditureBean();
            expend.setExpenditureName(null);
            expendList.add(expend);
        }
        
        this.dataSubReport.setSubStoreExpenditureReport(new JRBeanCollectionDataSource(expendList));
        
        return inVal - outVal;
    }
    
    //�L����
    private String getStaffName() {
        String name = null;
        
        String sql = "select staff_name1 , staff_name2 " + newLine +
                "from mst_staff " + newLine +
                "where delete_date is NULL " +
                "and	staff_id = " + this.getStaffId();
        
        try {
            ResultSetWrapper rs = dbConnection.executeQuery(sql);
            if( rs.next() ){
                name = rs.getString("staff_name1") + " " + rs.getString("staff_name2");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return name;
    }
    
    //���q���ԁA�c�ƏI������
    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getExitTime());
        //���q�X����
        this.paramMemMap.put("CustTimeHr", calendar.get(Calendar.HOUR_OF_DAY));
        this.paramMemMap.put("CustTimeMin", calendar.get(Calendar.MINUTE));
        
        //�c�ƏI������
        calendar.setTime(this.getCloseTime());
        this.paramMemMap.put("SaleEndHr", calendar.get(Calendar.HOUR_OF_DAY));
        this.paramMemMap.put("SaleEndMin", calendar.get(Calendar.MINUTE));
        
    }
    
    //�q�����擾
    private int getAllCustomerCount() {
    
        int cntCustomer = 0;
        int totalCustomer = 0;
        
        //���X�q���F�S�q���F����{�����{�V�K
        totalCustomer = this.getCustomerCount(-1, this.CUSTOMER_ALLMEMBER);
        this.paramMemMap.put("CustomerCount", new Integer(totalCustomer));
        
        //���X�q���F����
        cntCustomer = this.getCustomerCount(this.CUSTOMER_NONMEMBER,this.CUSTOMER_ALLMEMBER);
        this.paramMemMap.put("CustomerNone", new Integer(cntCustomer));
        
        //���X�q���F�V�K
        cntCustomer = this.getCustomerCount(this.CUSTOMER_ALLMEMBER, this.CUSTOMER_NEWMEMBER);
        this.paramMemMap.put("NewCustomerCount", new Integer(cntCustomer));
        
        return totalCustomer;
    }
    
    //�����̔���
    private long getNonMemberSales() {
        String sql = this.nonMemberSalesSql();
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if(rs.next()){
                return rs.getLong("nonSales");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return 0;
    }
    
    //���i����
    //�Ԃ��F���i����
    private long getStockSales() {
        String sql = this.stockSalesSql();
        long stockSales = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if(rs.next()) {
                stockSales = rs.getLong("stockSales");
                this.stocktaxtotal = rs.getLong("taxInStock") - stockSales;
                //sales = 0;
                //tax = 0;
                //�e���i�ɋ��z�������ł𔲂�
                //sales = rs.getLong("product_value");
                //tax = TaxUtil.getTax(sales, this.taxrate, 3);
                //stockSales += (sales - tax);
                //this.stocktaxtotal += tax;
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        //���i�����Ԃ�
        return stockSales;
    }
    
    
    //�Ɩ�����̉�ʓ���DB�ɕۑ�����
    private void registDb() {
        String updateStr = this.updateSql();
        String insertStr  =  this.insertSql();
        
        try {   //�I�񂾓��t�̏�񂪂���΁A�X�V���s�Ȃ��B
            int updateCnt  = dbConnection.executeUpdate(updateStr);
            if( updateCnt ==  0 ) {   //�����ꍇ�ADB�ɒǉ��B
                dbConnection.execute(insertStr);
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    public int viewDailySalesReport(int nExportType) {
        String strPrevYearRate;
        int intIncreaseRate;
        long lngPrevYearSale;
        long lngSalesTgt;
        int intOverall;
        int intCustomerCountEstimate;
        long lngTechEstimate;
        int intCustomerCountTotal;
        long lngTechOvr;
        int intCurrentWorkingDays;
        int intOpenDays;
        long gross = 0;
        long stockSales = 0;
        long shimeiSales = 0;
        long reserveSales  = 0;
        int staffCnt  = 0;
        long   oneWork = 0;
        int    oneService = 0;
        long   unitPrice = 0;
        
        //��ʂ̓���DB�ɕۑ�����
        registDb();
        
        //������
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'�N'  MM'��'�@ dd'��'  EEEEEE");
        this.paramMemMap = new HashMap<String,Object>();
        dataSubReport = new StoreSubReportBean();
        ArrayList<StoreSubReportBean> subReportList = new ArrayList<StoreSubReportBean>();
        this.taxrate = SystemInfo.getTaxRate(this.getOutputDate());
        
        //�q��:���q�i����A�����j�A�V�K�q
        double cntCustomer = this.getAllCustomerCount();
        //�Z�p����
        this.makeTechnicList(cntCustomer);
        //���i����
        stockSales =  this.getStockSales();
        
        //�X�^�b�t�ғ���
        staffCnt  = this.getStaffAttendance();
        //
        long expenditureTotal = this.getExpenditure();
        NumberFormat fmtNum = NumberFormat.getNumberInstance();
        
        this.paramMemMap.put("ExpenditureTotal", fmtNum.format(expenditureTotal));
        
        // Exit/Close Time
        this.getTime();
        
        subReportList.add(this.dataSubReport);
        
        //�Z�p����
        technicAmount = technicAmount + monitorAmount;
        this.paramMemMap.put("TechnicSubTotal", technicAmount);
        //����
        this.paramMemMap.put("TechnicNonMember", this.getNonMemberSales() + monitorAmount);
        
        
        //���i����
        this.paramMemMap.put("StockTotal", stockSales);
        //���v�@=�@�Z�p����@+�@���i����
        this.paramMemMap.put("SubTotal_TS", technicAmount + stockSales);
        //���t
        this.paramMemMap.put("WorkDate", dateFormat.format(this.getOutputDate()));
        //�X�ܖ�
        this.paramMemMap.put("ShopName", this.getShopName());
        //�V�C
        this.paramMemMap.put("WeatherStatus", this.getWeather());
        //����
        this.paramMemMap.put("Memo", this.getMemo());
        //�L����
        this.paramMemMap.put("RecorderName", this.getStaffName());
        
        //�w��
        shimeiSales = 0;	// �w������ 0�Œ�
        this.paramMemMap.put("NameCount", shimeiSales);
        
        //�\��
        reserveSales = 0;	// �w������ 0�Œ�
        this.paramMemMap.put("BookingCount", reserveSales);
        
        //���v�@=�@�w���@+�@�\��
        this.paramMemMap.put("SubTotal", shimeiSales + reserveSales);
        //����� = �Z�p����Ł@+�@���i�����
        this.paramMemMap.put("ConsumptionTax", this.technictaxtotal + this.stocktaxtotal);
        
        //�@������@=�@�Z�p����@+�@���i����@+�w���@+�@�\��@+�@�����
        gross =  technicAmount + stockSales + shimeiSales + reserveSales + this.technictaxtotal + this.stocktaxtotal;
        this.paramMemMap.put("Gross", gross);
        
        if( staffCnt > 0 ) {   //1�l���Z�p����
            oneWork = (long)NumberUtil.round((double)technicAmount / (double)staffCnt);
            this.paramMemMap.put("OneWork", new Long(oneWork));
            //1�l�������q��
            oneService = (int)NumberUtil.round((double)cntCustomer / (double)staffCnt);
            this.paramMemMap.put("OneService", new Integer(oneService));
            //�Z�p�q�P��
            unitPrice = (long)NumberUtil.round((double)technicAmount / (double)cntCustomer);
            this.paramMemMap.put("UnitPrice", new Long(unitPrice));
        }
        
        // �ߕs����
        GregorianCalendar	temp	=	new GregorianCalendar();
        Integer intLogical, intPhysical;
        temp.setTime(this.getOutputDate());
        register = new Register(this.getShop(), temp);
        
        intLogical = register.getLogicalValue();
        intPhysical = register.getPhysicalValue();
        
        long excessAmount = intPhysical - intLogical;
        this.paramMemMap.put("CashExcess", this.getCashExcessStr(excessAmount - expenditureTotal));
        this.paramMemMap.put("ExcessAmount", new Long(excessAmount - expenditureTotal));
        
        // ���W�Ǘ�
        this.paramMemMap.put("DepositAmount", new Long(register.getCashSales() + expenditureTotal));
        this.paramMemMap.put("CardAmount", new Long(register.getCardSales()));
        
        
        //-- �����c�Ə�
        // �����ғ�����
        intOpenDays = this.getOpenDays();
        this.paramMemMap.put("TotalWorkingDays", intOpenDays);
        
        // ���݉ғ�����
        intCurrentWorkingDays = this.getCurrentWorkingDays();
        this.paramMemMap.put("CurrentWorkingDays", intCurrentWorkingDays);
        
        // �c�ғ�����
        this.paramMemMap.put("RemWorkingDays", intOpenDays - intCurrentWorkingDays);
        
        // �Z�p����݌v
        lngTechOvr = this.getTechOverall();
        this.paramMemMap.put("TechOverall", lngTechOvr);
        
        // ���X�q���݌v
        intCustomerCountTotal = this.getCustomerCountTotal();
        this.paramMemMap.put("CustomerCountTotal", intCustomerCountTotal);
        
        if (intCurrentWorkingDays != 0) {
            lngTechEstimate = (long)NumberUtil.round( (double)lngTechOvr / (double)intCurrentWorkingDays * (double)intOpenDays );
            intCustomerCountEstimate = (int)NumberUtil.round( (double)intCustomerCountTotal / (double)intCurrentWorkingDays * (double)intOpenDays );
        } else {
            lngTechEstimate = 0;
            intCustomerCountEstimate = 0;
        }
        
        // �Z�p����\��
        this.paramMemMap.put("TechEstimate", lngTechEstimate);
        
        // ���X�q���\��
        this.paramMemMap.put("CustomerCountEstimate", intCustomerCountEstimate);
        
        if (intCustomerCountTotal != 0) {
            intOverall = (int)Math.floor(lngTechOvr / intCustomerCountTotal);
        } else {
            intOverall = 0;
        }
        
        // �݌v�Z�p�q�P��
        this.paramMemMap.put("Overall", intOverall);
        
        // ����ڕW
        lngSalesTgt = this.getSalesTgt();
        this.paramMemMap.put("Predicted", lngSalesTgt);
        
        // ��
        lngSalesTgt = this.getSalesTgt();
        this.paramMemMap.put("Difference", lngTechEstimate - lngSalesTgt);
        
        // �O�N����
        lngPrevYearSale = this.getPrevYearSale();
        
        // �O�N��
        if (intCustomerCountTotal != 0) {
            intIncreaseRate = (int)NumberUtil.round((double)lngTechEstimate / (double)lngPrevYearSale * 100.);
        } else {
            intIncreaseRate = 0;
        }
        
        strPrevYearRate = FormatUtil.decimalFormat(lngPrevYearSale) + " / " + intIncreaseRate + "%";
        
        this.paramMemMap.put("PrevYearRate", strPrevYearRate);
        
        
        return  this.generateFile(subReportList,nExportType);
        
        //return true;
    }
    
    // PDF or Excel�t�@�C�����쐬
    public int generateFile(Collection collection, int fileType) {
        boolean isCreated = false;
        
        try {
            JasperReport jasperStore             = null;
            JasperReport jasperSubTechnic        = null;
            JasperReport jasperSubRate          = null;
            JasperReport jasperSubStaff          = null;
            JasperReport jasperSubExpend         = null;
            
            jasperStore      =  this.loadReport(REPORT_STOREDAILY_JASPER,REPORT_FILE_TYPE_JASPER);
            jasperSubTechnic =  this.loadReport(REPORT_STOREDAILY_TECHNIC, REPORT_FILE_TYPE_JASPER);
            jasperSubRate    =  this.loadReport(REPORT_STOREDAILY_TECHNICRATE, REPORT_FILE_TYPE_JASPER);
            jasperSubStaff   =  this.loadReport(REPORT_STOREDAILY_ATTEND, REPORT_FILE_TYPE_JASPER);
            jasperSubExpend  =  this.loadReport(REPORT_STOREDAILY_EXPENDITURE, REPORT_FILE_TYPE_JASPER);
            
            paramMemMap.put("TechnicSubReport"   ,jasperSubTechnic );
            paramMemMap.put("TechnicRate"        ,jasperSubRate);
            paramMemMap.put("StaffAttendance"    ,jasperSubStaff);
            paramMemMap.put("Expenditure"        ,jasperSubExpend);
            
            JasperPrint print = JasperFillManager.fillReport(jasperStore, paramMemMap, new JRBeanCollectionDataSource(collection));
            switch(fileType) {
                case EXPORT_FILE_PDF:
                    isCreated = this.generateAndPreviewPDFFile(REPORT_STOREDAILY, print);
                    break;
                case EXPORT_FILE_XLS:
                    isCreated = this.generateAndPreviewXLSFile(REPORT_STOREDAILY, print);
                    break;
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return ReportGeneratorLogic.RESULT_ERROR;
        }
        
        return ReportGeneratorLogic.RESULT_SUCCESS;
    }
    
    
    //�Z�p�q�@:: ���X�q���Ftype = -�P:�w��Ȃ��@�O:�����@1:���
    public int getCustomerCount(int type, int newOld) {

        int cntCustomer = 0;
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     count(distinct ds.slip_no ) as cntCustomer");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid ds");
        sql.append("         join mst_customer mc");
        sql.append("         using (customer_id)");
        sql.append(" where");
        sql.append("         mc.delete_date is null");
        sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and ds.sales_date = " + SQLUtil.convertForSQLDateOnly(this.outputDate));
        sql.append("     and ds.product_division = 1");
        sql.append("     and " + getNotExistsMonitorSQL());

        if (type != -1) {
            if (type == 1) {
                sql.append(" and mc.customer_no = '0'");
            } else {
                sql.append(" and mc.customer_no <> '0'");
            }
        }
        
        if (newOld == 0) {
            sql.append(" and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) >= 1");
        } else {
            sql.append(" and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1");
        }
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql.toString());
            rs.next();
            cntCustomer = rs.getInt("cntCustomer");
        } catch(Exception e ) {
            System.out.println(e.getCause().toString());
        }
        return cntCustomer;
    }
    
    
    private String updateSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date today = new Date();
        
        String sql = " update data_bizreport_log" + newLine +
                "set weather = '" + this.getWeather() + "'," + newLine +
                "memo = '" + this.getMemo() + "'," + newLine +
                "exit_time = '" + timeFormat.format(this.getExitTime()) + "'," + newLine +
                "close_time = '" + timeFormat.format(this.getCloseTime()) + "'," + newLine +
                "staff_id = " + this.getStaffId() + "," +newLine +
                "update_date = '" + todayFormat.format(today) + "'" + newLine +
                "where shop_id = " + this.getShopId() +  " AND" + newLine +
                "output_date = '" + dateFormat.format(this.getOutputDate() ) + "' AND"  + newLine +
                "delete_date is null";
        
        return sql;
    }
    
    private String insertSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date today = new Date();
        
        String sql = "insert into data_bizreport_log" + newLine +
                "(shop_id,output_date,weather,memo,exit_time, close_time,staff_id, insert_date,update_date,delete_date)" + newLine +
                "values" + newLine +
                "(" + this.getShopId() + comma +
                apostrophe + dateFormat.format(this.getOutputDate()) + apostrophe + comma +
                apostrophe + this.getWeather() + apostrophe + comma +
                apostrophe + this.getMemo() + apostrophe + comma +
                apostrophe + timeFormat.format(this.getExitTime()) + apostrophe + comma + newLine +
                apostrophe + timeFormat.format(this.getCloseTime()) + apostrophe + comma +
                this.getStaffId() + comma +
                apostrophe + todayFormat.format(today) + apostrophe + comma +
                apostrophe + todayFormat.format(today) + apostrophe + comma +
                "null )" ;
        
        //System.out.println(sql);
        return sql;
    }
    
    private String technicSql() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      mc.technic_class_name as techName");
        sql.append("     ,sum(sd.discount_detail_value_no_tax) as techSales");
        sql.append("     ,sum(sd.product_num) as techCount");
        sql.append("     ,count(distinct sd.slip_no) as visitCount");
        sql.append("     ,sum(sd.discount_detail_value_in_tax) as taxInTechnic");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid sd");
        sql.append("         inner join mst_customer mcus");
        sql.append("                 on mcus.customer_id = sd.customer_id");
        sql.append("         inner join mst_technic mt");
        sql.append("                 on mt.technic_id = sd.product_id");
        sql.append("                and mt.delete_date is null");
        sql.append("         inner join mst_technic_class mc");
        //Luc start edit 20151116 #44792
        //sql.append("                 on mc.technic_class_id = replace(mt.technic_class_id, 6, 5)");
        sql.append("                on mc.technic_class_id = replace(cast(mt.technic_class_id as text),'6', '5')::int");
        //Luc end edit 20151116 #44792
        sql.append("                and mc.delete_date is null");
        sql.append(" where");
        sql.append("         sd.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and sd.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     and sd.product_division = " + SQLUtil.convertForSQL(this.PRODUCT_TECHNIC));
        sql.append(" group by");
        sql.append("      mc.technic_class_id");
        sql.append("     ,mc.display_seq");
        sql.append("     ,mc.technic_class_name");
        sql.append(" order by");
        sql.append("      mc.display_seq");
        sql.append("     ,mc.technic_class_id");

        return sql.toString();
        // <editor-fold defaultstate="collapsed" desc=" Old Sql">
        /*String sql = "select" + newLine +
                        "count(data_sales_detail.product_value) as techCount," + newLine +
                        "sum (data_sales_detail.product_value)  as techSales, " + newLine +
                        "mst_technic.technic_name" + newLine +
                     "from" + newLine +
                          "data_sales,"        + newLine +
                          "data_sales_detail," + newLine +
                          "mst_use_product,"   + newLine +
                          "mst_technic"        + newLine +
                     "where" + newLine +
                         "data_sales.shop_id = " + this.getShopId()  + " AND" + newLine +
                         "data_sales.sales_date = '" + dateFormat.format(this.getOutputDate())  + "' AND " + newLine +
                         "data_sales.delete_date is null AND" + newLine +
                         "data_sales.shop_id = data_sales_detail.shop_id AND " + newLine +
                         "data_sales.slip_no = data_sales_detail.slip_no AND " + newLine +
                         "data_sales_detail.product_division =  " + this.PRODUCT_TECHNIC  +  " AND " + newLine +
                         "mst_use_product.shop_id = data_sales.shop_id AND " + newLine +
                         "mst_use_product.product_id = data_sales_detail.product_id AND " +  newLine +
                         "mst_use_product.product_division = data_sales_detail.product_division AND " + newLine +
                         "mst_use_product.product_id = mst_technic.technic_id " + newLine +
                    "group by " + newLine +
                    "mst_technic.technic_id,mst_technic.technic_name " ;
         */
        // </editor-fold >
        
    }
    
    //������SQL
    private String nonMemberSalesSql() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     sum(sd.discount_detail_value_no_tax) as nonSales");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid sd");
        sql.append("         inner join mst_customer mc");
        sql.append("                 on mc.customer_id = sd.customer_id");
        sql.append(" where");
        sql.append("         sd.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and sd.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     and sd.product_division = " + SQLUtil.convertForSQL(this.PRODUCT_TECHNIC));
        sql.append("     and mc.customer_no = '0'");
        sql.append("     and mc.delete_date is null");

        return sql.toString();
        // <editor-fold defaultstate="collapsed" desc=" Old Sql">
       /* String sql = "select" + newLine +
                     "sum(data_sales_detail.product_value)as nonSales " + newLine +
                     "from" + newLine +
                     "data_sales," + newLine +
                     "data_sales_detail, " + newLine +
                     "mst_customer " + newLine +
                     "where" + newLine +
                     "data_sales.shop_id = " + this.getShopId() + " AND" + newLine +
                     "data_sales.sales_date = '" + dateFormat.format(this.getOutputDate()) +"' AND" + newLine +
                     "data_sales.delete_date is null AND" + newLine +
                     "data_sales.shop_id = data_sales_detail.shop_id AND" + newLine +
                     "data_sales.slip_no = data_sales_detail.slip_no AND" + newLine +
                     "data_sales_detail.product_division = "+ this.PRODUCT_TECHNIC + " AND" + newLine +
                     "data_sales.customer_id = mst_customer.customer_id AND  " + newLine +
                     "mst_customer.customer_no = '0'  AND " + newLine +
                     "mst_customer.delete_date is null " ;
        */
        // </editor-fold >
        
    }
    
    //���i��SQL
    private String stockSalesSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String stockSql = "select  sum(sd.discount_detail_value_no_tax) as stockSales, " + newLine +
                "sum(sd.discount_detail_value_in_tax) as taxInStock" + newLine +
                "from view_data_sales_valid vs, view_data_sales_detail_valid sd " + newLine +
                "where vs.shop_id = " + this.getShopId() +" AND " + newLine +
                "vs.sales_date ='" + dateFormat.format(this.getOutputDate()) + "' AND" + newLine +
                "vs.shop_id = sd.shop_id AND vs.slip_no = sd.slip_no AND " + newLine +
                "sd.product_division in (2, 4)";
        
        return stockSql;
        // <editor-fold defaultstate="collapsed" desc=" Old Sql">
        /*String stockSql =  "select " + newLine +
                           "data_sales_detail.product_value" + newLine +
                           "from" + newLine +
                           "data_sales,"  + newLine +
                           "data_sales_detail " + newLine +
                           "where" + newLine +
                           "data_sales.shop_id = " + this.getShopId() + " AND " + newLine +
                           "data_sales.sales_date = '" + dateFormat.format(this.getOutputDate()) +"' AND " + newLine +
                           "data_sales.delete_date is null AND" + newLine +
                           "data_sales.shop_id = data_sales_detail.shop_id AND " + newLine +
                           "data_sales.slip_no = data_sales_detail.slip_no AND " + newLine +
                           "data_sales_detail.product_division = " + this.PRODUCT_STOCK ;
         */
        // </editor-fold>
        
    }
    
//     //�w����SQL
//     private String shimeiSql()
//     {
//          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//          String sql = "select  sum(sd.detail_value_no_tax) as shimeiSales " + newLine  +
//                       "from  view_data_sales_valid vs,view_data_sales_detail_valid sd " + newLine +
//                       "where vs.shop_id = " + this.getShopId() + " AND " + newLine +
//                       "vs.sales_date ='" + dateFormat.format(this.getOutputDate()) + "' AND " + newLine +
//                       "vs.shop_id = sd.shop_id AND vs.slip_no = sd.slip_no AND " + newLine +
//                       "sd.designated_flag = 't' " ;
//           return sql;
//
//          // <editor-fold defaultstate="collapsed" desc=" Old Sql">
//          /*String sql = "select " + newLine +
//                      "sum(data_sales_detail.product_value) as shimeiSales" + newLine +
//                      "from" + newLine +
//                      "data_sales," + newLine +
//                      "data_sales_detail" + newLine +
//                      "where" + newLine +
//                      "data_sales.shop_id = " + this.getShopId() + " AND " + newLine +
//                      "data_sales.sales_date = '" +  dateFormat.format(this.getOutputDate())   + "' AND " + newLine +
//                      "data_sales.delete_date is null AND" + newLine +
//                      "data_sales.shop_id = data_sales_detail.shop_id AND " + newLine +
//                      "data_sales.slip_no = data_sales_detail.slip_no AND " + newLine +
//                      "data_sales_detail.product_division != " + this.PRODUCT_DISCOUNT + " AND" + newLine +
//                      "data_sales_detail.designated_flag = 't'";
//           */
//          // </editor-fold>
//
//     }
    
    
    //�X�^�b�t�ғ���SQL
    private String staffStatusSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStart = dateFormat.format(this.getOutputDate()) + " 00:00:00";
        String dayEnd   = dateFormat.format(this.getOutputDate()) + " 23:59:59";
        
        String sql  =  "select" + newLine +
                "ms.staff_name1, " + newLine +
                "ms.staff_name2, " + newLine +
                "mw.working, mw.disp_name"  + newLine +
                "from  "+  newLine +
                "  mst_staff ms" +
                ", data_working_staff ds " + newLine +
                ", mst_work_status mw " + newLine +
                
                "where" + newLine +
                "ms.staff_id = ds.staff_id AND" + newLine +
                "ds.shop_id = " + this.getShopId() + " AND" + newLine +
                "ds.working_date between '" + dayStart+"' and '" + dayEnd +"' AND" + newLine +
                "ds.work_id = mw.work_id and mw.delete_date is null AND" + newLine +
//                "ms.delete_date is null AND "+ newLine +
                "ds.delete_date is null" + newLine +
                
                "order by ms.display_seq, ds.staff_id" ;
        return sql;
        
    }
    
    private String expendSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String sql = "select " + newLine +
                "data_cash_io.in_out," + newLine +
                "data_cash_io.io_value," + newLine +
                "data_cash_io.use_for "  + newLine +
                "from"    + newLine +
                "data_cash_io" + newLine +
                "where" + newLine +
                "data_cash_io.shop_id = "+ this.getShopId() + " AND" + newLine +
                "data_cash_io.io_date = '" + dateFormat.format(this.getOutputDate()) +"' AND" + newLine +
                "data_cash_io.delete_date is null" ;
        
        return sql;
    }
    
    private Integer getOpenDays() {
        String sql = this.getSQLSelectOpenDays();
        int nDays = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ) {
                nDays = rs.getInt("open_days");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return nDays;
    }
    
    private String getSQLSelectOpenDays() {
        
        Calendar calen = Calendar.getInstance();
        calen.setTime(this.getOutputDate());
        
        StringBuilder sql = new StringBuilder(1000);
        
        sql.append(" select");
        sql.append(" 	open_days");
        sql.append(" from data_target_result");
        
        sql.append(" where");
        sql.append(" 	delete_date is NULL");
        sql.append(" and	shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" and	year = " + SQLUtil.convertForSQL( calen.get(Calendar.YEAR) ));
        sql.append(" and	month = " + SQLUtil.convertForSQL( calen.get(Calendar.MONTH) + 1 ));
        
        return sql.toString();
    }
    
    private Integer getCurrentWorkingDays() {
        String sql = this.getSQLSelectCurrentWorkingDays();
        int nDays = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ) {
                nDays = rs.getInt("solddays");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return nDays;
    }
    
    private String getSQLSelectCurrentWorkingDays() {

        Calendar calen = Calendar.getInstance();
        calen.setTime(this.getOutputDate());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append(" 	count(all SUB_SOLDDAY.soldday) as solddays");
        sql.append(" from (");
        sql.append(" 	select");
        sql.append("			count(all sales_date) as soldday");
        sql.append(" 	from data_sales");
        
        sql.append(" 	where");
        sql.append("			shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("		and date_part('year', sales_date) = " + SQLUtil.convertForSQL( calen.get(Calendar.YEAR) ));
        sql.append("		and date_part('month', sales_date) = " + SQLUtil.convertForSQL( calen.get(Calendar.MONTH) + 1 ));
        sql.append("		and date_part('day', sales_date) <= " + SQLUtil.convertForSQL( calen.get(Calendar.DATE) ));
        sql.append("		and delete_date is NULL");
        
        sql.append(" 	group by");
        sql.append("			sales_date");
        sql.append(" ) as SUB_SOLDDAY");
        
        return sql.toString();
    }
    
    private Long getTechOverall() {
        String sql = this.getSQLSelectTechOverall();
        long lTechOvr = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ) {
                lTechOvr = rs.getInt("TechSum");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return lTechOvr;
    }
    
    private String getSQLSelectTechOverall() {

        Calendar calen = Calendar.getInstance();
        calen.setTime(this.getOutputDate());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append(" 	sum(v_dsv_det.discount_detail_value_no_tax) as TechSum");
        sql.append(" from view_data_sales_detail_valid as v_dsv_det");
        sql.append(" where");
        sql.append(" 	date_part('year', v_dsv_det.sales_date) = " + SQLUtil.convertForSQL( calen.get(Calendar.YEAR) ));
        sql.append(" and	date_part('month', v_dsv_det.sales_date) = " + SQLUtil.convertForSQL( calen.get(Calendar.MONTH) + 1 ));
        sql.append(" and	date_part('day', v_dsv_det.sales_date) <= " + SQLUtil.convertForSQL( calen.get(Calendar.DATE) ));
        sql.append(" and	v_dsv_det.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" and	v_dsv_det.product_division = " + SQLUtil.convertForSQL(PRODUCT_TECHNIC));
        
        return sql.toString();
    }
    
    private Integer getCustomerCountTotal() {
        String sql = this.getSQLSelectCustomerCountTotal();
        int nCustCnt = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ) {
                nCustCnt = rs.getInt("CustCnt");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return nCustCnt;
    }
    
    private String getSQLSelectCustomerCountTotal() {

        Calendar calen = Calendar.getInstance();
        calen.setTime(this.getOutputDate());

        Calendar calenFrom = Calendar.getInstance();
        calenFrom.setTime(calen.getTime());
        calenFrom.set(Calendar.DAY_OF_MONTH, 1);
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     count(distinct ds.slip_no) as CustCnt");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid ds");
        sql.append(" where");
        sql.append("         ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(calenFrom.getTime()));
        sql.append("     and ds.sales_date <= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
        sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and ds.product_division = " + SQLUtil.convertForSQL(PRODUCT_TECHNIC));
        sql.append("     and " + getNotExistsMonitorSQL());

        return sql.toString();
    }
    
    private Long getSalesTgt() {
        String sql = this.getSQLSelectSalesTgt();
        long lSalesTgt = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ) {
                lSalesTgt = rs.getInt("target_technic");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return lSalesTgt;
    }
    
    private String getSQLSelectSalesTgt() {

        Calendar calen = Calendar.getInstance();
        calen.setTime(this.getOutputDate());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append(" 	target_technic");
        sql.append(" from data_target_result");
        
        sql.append(" where");
        sql.append(" 	shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" and	year = " + SQLUtil.convertForSQL( calen.get(Calendar.YEAR) ));
        sql.append(" and	month = " + SQLUtil.convertForSQL( calen.get(Calendar.MONTH) + 1 ));
        sql.append("	and delete_date is NULL");
        
        return sql.toString();
    }
    
    private Long getPrevYearSale() {
        String sql = this.getSQLSelectPrevYearSale();
        long lPrevYearSale = 0;
        
        try {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ) {
                lPrevYearSale = rs.getInt("result_technic");
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return lPrevYearSale;
    }
    
    private String getSQLSelectPrevYearSale() {

        Calendar calen = Calendar.getInstance();
        calen.setTime(this.getOutputDate());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append(" 	result_technic");
        sql.append(" from data_target_result");
        
        sql.append(" where");
        sql.append(" 	shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" and	year = " + SQLUtil.convertForSQL( calen.get(Calendar.YEAR) - 1 ));
        sql.append(" and	month = " + SQLUtil.convertForSQL( calen.get(Calendar.MONTH) + 1 ));
        sql.append("	and delete_date is NULL");
        
        return sql.toString();
    }
    
    private String getCashExcessStr(long excessAmount) {
        
        String strExcess;
        
        if (excessAmount > 0) {
            strExcess = "��";
        } else if (excessAmount < 0) {
            strExcess = "��";
        } else {
            strExcess = "�Ȃ�";
        }
        
        return strExcess;
    }
    
    private static String getNotExistsMonitorSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" not exists");
        sql.append(" (");
        sql.append("     select 1");
        sql.append("     from");
        sql.append("         view_data_sales_detail_valid dsd");
        sql.append("             left join mst_technic mt");
        sql.append("                    on dsd.product_id = mt.technic_id");
        sql.append("             left join mst_technic_class mtc");
        sql.append("                    on mt.technic_class_id = mtc.technic_class_id");
        sql.append("     where");
        sql.append("             dsd.shop_id = ds.shop_id");
        sql.append("         and dsd.slip_no = ds.slip_no");
        sql.append("         and dsd.product_division = 1");
        sql.append("         and mtc.technic_class_name = '���j�^'");
        sql.append(" )");
        
        return sql.toString();
    }
    
}

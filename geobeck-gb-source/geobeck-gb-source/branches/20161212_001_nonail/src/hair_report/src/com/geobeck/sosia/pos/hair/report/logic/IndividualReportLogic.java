/*
 * IndividualReportLogic.java
 *
 * Created on 2008/09/17, 11:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.PersonalSubReportBean;
import com.geobeck.sosia.pos.hair.report.beans.SubPersonalNewServiceReportBean;
import com.geobeck.sosia.pos.hair.report.beans.SubPersonalServiceReportBean;
import com.geobeck.sosia.pos.hair.report.beans.SubPersonalStockReportBean;
import com.geobeck.sosia.pos.hair.report.beans.SubPersonalTechnicReportBean;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import java.util.*;
import java.util.logging.Level;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;

// use for JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;

import com.geobeck.excelconcat.SheetConcat;
import com.geobeck.util.SQLUtil;
import java.text.NumberFormat;

/**
 *
 * @author trino
 */
public class IndividualReportLogic extends ReportGeneratorLogic
{
    
    // <editor-fold defaultstate="collapsed" desc="Definitions and Variables">
    // jasper files
    private static String REPORT_RESOURCE_PATH				= "/reports/";
    private static String REPORT_EXPORT_PATH				= "./";
    private static String REPORT_XML_FILE_EXT				= ".jasper";
    private static String REPORT_PERSONALREPORT                          = "PersonalAchievementTotalReport";
    private static String REPORT_PERSONALREPORT_JASPER                   = "PersonalAchievementTotalReport.jasper";
    private static String REPORT_PERSONALREPORT_TECHNIC                  = "PersonalTotalReport_Technic.jasper";
    private static String REPORT_PERSONALREPORT_SERVICE                  = "PersonalTotalReport_Service.jasper";
    private static String REPORT_PERSONALREPORT_STOCK                    = "PersonTotalReport_Stock.jasper";
    private static String REPORT_PERSONALREPORT_NEWSERVICE               = "PersonalTotalReport_NewService.jasper";
    
    private int shopId;
    private MstShop shop;
//    private Integer staffId;
	private MstStaff staffinfo;
	private ArrayList<MstStaff> stafflist = new ArrayList<MstStaff>();
    private Date outputDate = null;
//    private String staffName = null;
    private int totalCustomerCount = 0;
    
    private PersonalSubReportBean pbean = null;
    private final String newLine = "\n";
    private final String shin    = "(新)";
    private final String comma   = "," ;
    private final String apostrophe = "'";
    
    private final int DIVISION_TECHNIC = 1;
    private final int DIVISION_STOCK   = 2;
    
    private final int MAX_TECHNIC_ROW  = 11;
    private final int MAX_CUSTOMER_ROW = 24;
    private final int MAX_STOCK_ROW = 8;
    private final int MAX_PIONEER_ROW = 9;
    // </editor-fold>
    
    private ConnectionWrapper dbConnection = null;

    private long monitorAmount = 0;

    /** Creates a new instance of IndividualReportLogic */
    
    public IndividualReportLogic()
    {
        dbConnection = SystemInfo.getConnection();
    }
    
    public void setShopId(int id){
        this.shopId = id;        
    }
    
    public void setStaffList(JComboBox cmbStaff)
    {
        stafflist.clear();

        for (int i = 1; i < cmbStaff.getItemCount(); ++i)
        {
            MstStaff ms = (MstStaff)cmbStaff.getItemAt(i);
            
            if (ms.getShopID().equals(SystemInfo.getCurrentShop().getShopID())) {
                stafflist.add(ms);
            }
        }

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
    
    /*
    //主担当者名
    public void setStaffName(String name){
        this.staffName = name;
    }
    
    public String getStaffName() {
        return this.staffName;
    }
    
    public void setStaffId(Integer id){
        this.staffId = id;
    }
    
    public Integer getStaffId(){
       return this.staffId;
    }
    */

    public void setStaff(MstStaff staffinfo){
        this.staffinfo = staffinfo;
    }
    
    public MstStaff getStaff(){
       return this.staffinfo;
    }

    public void setOutputDate(Date date){
        this.outputDate = date;
    }
    
    public Date getOutputDate(){
        return this.outputDate;
    }
    
    //店舗名を取得する
    //返す　：　店舗名
    private String getShopName()
    {
         String sql= "select mst_shop.shop_name from mst_shop " +
                      "where mst_shop.shop_id = " + this.getShopId();
         String storename = null;
         
         try
         {
             ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
             if( rs.next())
             {
                 storename = rs.getString("shop_name");
             }
         }
         catch(Exception e)
         {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
         }  
         return storename;
    }
    
  
    // 技術項目、数、金額を取得する
    //　明細は、最大11行件表示、11件超えたものは表示しない。
    //  ただし、合計には含める。
    // 返す: 合計金額
    // ToDo: モニタのチェック
    private long getReportTechnic()
    {
        //DBからの技術情報のデータコンテナ
        SubPersonalTechnicReportBean sptrb = null;
        //コンテナリスト
        ArrayList<SubPersonalTechnicReportBean> technicList = new ArrayList<SubPersonalTechnicReportBean>();
        int index = 1;
        long totalAmount  = 0;
        this.monitorAmount = 0;

        NumberFormat nf = NumberFormat.getInstance();
        
        //クエリーを作成
        String sql = this.technicSQL();
        try
        {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            while( rs.next())
            {
                sptrb = new SubPersonalTechnicReportBean();
                sptrb.setCountIndex(new Integer(index));
                sptrb.setTechnicClassName(rs.getString("technicName"));
                
                if (sptrb.getTechnicClassName().equals("モニタ")) {
                    sptrb.setTechnicCount("[" + nf.format(rs.getInt("technicCount")) + "]");
                    sptrb.setTechnicAmount("[" + nf.format(rs.getLong("technicAmount")) + "]");
                } else {
                    sptrb.setTechnicCount(nf.format(rs.getInt("technicCount")));
                    sptrb.setTechnicAmount(nf.format(rs.getLong("technicAmount")));
                }

                totalAmount += rs.getLong("technicAmount");
                this.monitorAmount += rs.getLong("monitorAmount");
                //最大11行を確認
                if( technicList.size() < MAX_TECHNIC_ROW ){
                    technicList.add(sptrb);
                }
                index ++;
            }
            
        }
        catch(Exception e) 
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }  
               
        //　行　<　最大行、行だけ作成、データなし。Jasper表示するように
        for(int i = technicList.size(); i <  MAX_TECHNIC_ROW ; i++)
        {
            sptrb = new SubPersonalTechnicReportBean();
            sptrb.setTechnicClassName(null);
            sptrb.setCountIndex(new Integer(i+1));
            technicList.add(sptrb);   
        }
        //Jasper用のデータコンテナ
        this.pbean.setSubReportTechnic(new JRBeanCollectionDataSource(technicList));
        //合計金額を返す
        return totalAmount;
    }
      
    //商品を取得、商品合計金額を返す
    // 最大9行件表示9件超えたものは表示しない。
    //返す　：　商品合計金額
    private long getReportStock()
    {
        //商品のデータコンテナ
        SubPersonalStockReportBean spsrb = null;
        //商品のデータのコンテナリスト
        ArrayList<SubPersonalStockReportBean> stockList = new ArrayList<SubPersonalStockReportBean>();
        //クエリーを作成
        String sql = this.stockSQL();
        int index = 1;
        long stockAmount = 0;
        
        try
        {
            ResultSetWrapper rs  = this.dbConnection.executeQuery(sql);
            while(rs.next() )
            {
                spsrb = new SubPersonalStockReportBean();
                spsrb.setCountIndex(new Integer(index));
                spsrb.setStockName(rs.getString("stockName"));
                spsrb.setStockPrice(rs.getLong("stockSale"));
                stockAmount += rs.getLong("stockSale");
                if( stockList.size() < MAX_STOCK_ROW){
                    stockList.add(spsrb);
                }
                index++;
            }
        }
        catch(Exception e) 
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        //リストサイズ　<　商品最大行、行を作成、データなし。Jasper用、行を表示するように
        for(int i = stockList.size(); i < MAX_STOCK_ROW; i++)
        {
            spsrb = new SubPersonalStockReportBean();
            spsrb.setCountIndex(new Integer(i+1));
            spsrb.setStockName(null);
            stockList.add(spsrb);
        }
        //Jasper用のデータコンテナ
        this.pbean.setSubStockReport(new JRBeanCollectionDataSource(stockList));
        //商品合計金額を返す
        return stockAmount;
    }
    
    // 開拓新規者名：氏名、会員番号を取得する
    // 最大9行件
    // 返す：開拓新規数
    private int getReportNewService()
    {    //開拓新規データコンテナリスト
         ArrayList<SubPersonalNewServiceReportBean> sbeanList = new ArrayList<SubPersonalNewServiceReportBean>();
         //開拓新規データのコンテナ
         SubPersonalNewServiceReportBean sbean = null;
         //クエリーを作成
         String sql = this.pioneerSQL();
         int index = 1;
         int pCnt = 0;
         
         try
         {
             ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
             while(rs.next())
             {
                 sbean = new SubPersonalNewServiceReportBean();
                 sbean.setCountIndex(index);
                 sbean.setNewHelpName(rs.getString("name1")+ " " + rs.getString("name2"));
                 //非会員の場合　”-”を表示する
                 if( rs.getInt("no") == 0 ){
                     sbean.setNewHelpId(" - ");
                     sbean.setNewHelpName(sbean.getNewHelpName().trim() + "(非会員)");
                 }
                 else{
                     //会員の場合、会員番号を表示する
                     sbean.setNewHelpId("" + rs.getInt("no"));
                 }
                 //リストに追加前に最大行を確認
                 if(sbeanList.size() < MAX_PIONEER_ROW  ){
                     sbeanList.add(sbean);
                 }
                 index++;
             }
         }
         catch(Exception e)
         {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
         } 
         
         //開拓新規数
         pCnt = sbeanList.size();
         //リストのサイズと開拓最大行を比べる、リストサイズ少ない場合、データなし行を作成
         //Jasper用、行表示するように
         for(int i= sbeanList.size(); i<MAX_PIONEER_ROW ; i++)
         {
             sbean = new SubPersonalNewServiceReportBean();
             sbean.setCountIndex(i+1);
             sbean.setNewHelpName(null);
             sbeanList.add(sbean);
         }
         //Jasperのデータコンテナ
         this.pbean.setSubNewServiceReport(new JRBeanCollectionDataSource(sbeanList));
         //開拓新規数を返す
         return pCnt;
    }
    
    //主担当取扱い者名：氏名、会員番号を取得
    //最大24行、超えたものを表示しない
    //返す：主担当者数
    private String getReportService()
    {
         //主担当取扱いのデータコンテナ
         SubPersonalServiceReportBean bean = null;
         //主担当取扱いのデータコンテナリスト
         ArrayList<SubPersonalServiceReportBean> serviceList = new ArrayList<SubPersonalServiceReportBean>();
         //クエリーを作成
         String sql = this.customerSQL();
         String expr = null;
         
         int index = 1;
         int member  = 0;
         int nonMember = 0;
         
         try
         {
             ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
             while(rs.next())
             {
                 bean = new SubPersonalServiceReportBean();
                 bean.setCountIndex(index);
                 //新規の場合、顧客名の後に"(新)”を入れる。
                 if( rs.getInt("vCount") == 1 ){
                     bean.setHelpName(rs.getString("name1") + "  " + rs.getString("name2") +  shin);
                 }
                 else {
                     bean.setHelpName(rs.getString("name1") + "  " + rs.getString("name2"));
                 }
                 //非会員の場合、会員番号で”-”を表示する
                 if( rs.getInt("num") == 0 ){
                     bean.setHelpName(bean.getHelpName().trim() + "(非会員)");
                     bean.setHelpId(" - ");
                     nonMember ++;
                 }
                 else{
                     bean.setHelpId("" + rs.getInt("num"));
                     member ++;
                 }
                 //最大行？
                 if( serviceList.size() < MAX_CUSTOMER_ROW){
                     serviceList.add(bean);
                 }
                 index++;
             }
         }
         catch(Exception e)
         {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
         } 
        
         //最大行とリストサイズを比べる、リストサイズ少ない場合、行を作成、データなし
         //Jasperで行を表示するように
         for(int i= serviceList.size(); i < MAX_CUSTOMER_ROW; i++)
         {
             bean = new SubPersonalServiceReportBean();
             bean.setCountIndex(i+1);
             bean.setHelpName(null);
             serviceList.add(bean);
         }
         //主担当者数　=　会員　+非会員
         this.totalCustomerCount = nonMember+member;
         //主担当者数部でのデータ
         expr =  new String(  this.totalCustomerCount + " = "  + member + " + " + nonMember );
         //Jasper用のデータコンテナ
         this.pbean.setSubServiceReport(new JRBeanCollectionDataSource(serviceList));
         //主担当者数を返す
         return expr;
    }
    
    //　指名数
    private int getShimeiCount()
    {   //クエリーを作成
        String sql = this.shimeiSQL();
        int count = 0;
        
        try
        {
            ResultSetWrapper rs = this.dbConnection.executeQuery(sql);
            if( rs.next() ){
                count = rs.getInt("designated");
            }
        }
        catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        } 
        
        //指名数を返す
        return count;
    }
  
    //報告を作成、表示する
    public int viewIndividualReport(int nExportType)
    {
		int nRet;
		String strExportFile[] = new String[1];
		ArrayList<String> filelist = new ArrayList<String>();
		ArrayList<String> sheetnamelist = new ArrayList<String>();
                NumberFormat nf = NumberFormat.getInstance();

		if (this.getStaff() != null)
		{
                    if (existsStaffSalesData()) {

			//画面の情報DBに保存する
			this.registerDB();

			pbean = new PersonalSubReportBean();
			ArrayList<PersonalSubReportBean> pbeanList = new ArrayList<PersonalSubReportBean>();
			HashMap<String, Object> paramMap = new HashMap<String,Object>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'年'  MM'月'　 dd'日'  EEEEEE");
			//技術項目
			long technicAmount = this.getReportTechnic();
			paramMap.put("TechnicAmountTotal",technicAmount);
			paramMap.put("MonitorAmountTotal","[" + nf.format(this.monitorAmount) + "]");
			//商品
			paramMap.put("StockPriceTotal",this.getReportStock());
			//主担当取扱い
			paramMap.put("CustomerCount" ,this.getReportService());
			//開拓新規
			paramMap.put("NewHelpCount",this.getReportNewService());

			//技術客単価　=　技術合計金額 /　主担当者数
			if( this.totalCustomerCount > 0 ){ 
			  paramMap.put("TechnicCustEstimate", new Long(Math.round(technicAmount/this.totalCustomerCount)));
			}
			//指名
			paramMap.put("DesignatedCount", this.getShimeiCount());
			//出力指定日
			paramMap.put("SelectedDate", dateFormat.format(this.getOutputDate()));
			//出力店舗名
			paramMap.put("StoreName", this.getShopName());
			//主担当者名　←　選択されたスタッフ名

			paramMap.put("PersonInCharge", this.staffinfo.getFullStaffName());

			pbeanList.add(this.pbean);
			//Excelでの報告を作成
			//this.outputXLS(this.REPORT_PERSONALREPORT, pbeanList, paramMap);

			return this.generateFile(pbeanList, paramMap,nExportType);

                    }
                    
		}
		else
		{
			for (int i = 0; i < this.stafflist.size(); ++i)
			{
				this.staffinfo = stafflist.get(i);

                                if (!existsStaffSalesData()) continue;
                                
				//画面の情報DBに保存する
				this.registerDB();

				pbean = new PersonalSubReportBean();
				ArrayList<PersonalSubReportBean> pbeanList = new ArrayList<PersonalSubReportBean>();
				HashMap<String, Object> paramMap = new HashMap<String,Object>();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'年'  MM'月'　 dd'日'  EEEEEE");
				//技術項目
				long technicAmount = this.getReportTechnic();
				paramMap.put("TechnicAmountTotal",technicAmount);
                                paramMap.put("MonitorAmountTotal","[" + nf.format(this.monitorAmount) + "]");
				//商品
				paramMap.put("StockPriceTotal",this.getReportStock());
				//主担当取扱い
				paramMap.put("CustomerCount" ,this.getReportService());
				//開拓新規
				paramMap.put("NewHelpCount",this.getReportNewService());

				//技術客単価　=　技術合計金額 /　主担当者数
				if( this.totalCustomerCount > 0 ){ 
				  paramMap.put("TechnicCustEstimate", new Long(Math.round(technicAmount/this.totalCustomerCount)));
				}
				//指名
				paramMap.put("DesignatedCount", this.getShimeiCount());
				//出力指定日
				paramMap.put("SelectedDate", dateFormat.format(this.getOutputDate()));
				//出力店舗名
				paramMap.put("StoreName", this.getShopName());
				//主担当者名　←　選択されたスタッフ名

				String str = this.staffinfo.getFullStaffName();
				paramMap.put("PersonInCharge", this.staffinfo.getFullStaffName());

				pbeanList.add(this.pbean);
				//Excelでの報告を作成
				//this.outputXLS(this.REPORT_PERSONALREPORT, pbeanList, paramMap);

				nRet = justgenerateFile(pbeanList, paramMap,nExportType, i, strExportFile);
				filelist.add(strExportFile[0]);
				sheetnamelist.add(this.staffinfo.getStaffID() + "." + this.staffinfo.getFullStaffName());
			}

                        if (filelist.size() > 0) {
                            if (nExportType == EXPORT_FILE_PDF)
                            {
                                    ReportLogic.outputConcatReport(REPORT_PERSONALREPORT, filelist);
                            }
                            else if (nExportType == EXPORT_FILE_XLS)
                            {
                                    SheetConcat.outputXLSFileList(REPORT_PERSONALREPORT, filelist, sheetnamelist);
                            }
                            
                            return ReportGeneratorLogic.RESULT_SUCCESS;
                        }
		}

		return ReportGeneratorLogic.RESULT_DATA_NOTHNIG;
    }

    //PDF と　Excel ファイルを作成
    private int justgenerateFile(Collection collection, HashMap<String, Object> paramMap, int fileType, int filenum, String[] strExportFile)
    {
        boolean isCreated = false;
		
        
        try
        {
            JasperReport jasperPerson           =  this.loadReport(REPORT_PERSONALREPORT_JASPER, REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubTechnic       =  this.loadReport(REPORT_PERSONALREPORT_TECHNIC, REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubService       =  this.loadReport(REPORT_PERSONALREPORT_SERVICE, REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubStock         =  this.loadReport(REPORT_PERSONALREPORT_STOCK,REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubNewService    =  this.loadReport(REPORT_PERSONALREPORT_NEWSERVICE, REPORT_FILE_TYPE_JASPER);
            
            paramMap.put("TechnicClassReport",  jasperSubTechnic);
            paramMap.put("ServiceReport",       jasperSubService);
            paramMap.put("StockReport",         jasperSubStock);
            paramMap.put("NewServiceReport",    jasperSubNewService);

            JasperPrint print = JasperFillManager.fillReport(jasperPerson, paramMap, new JRBeanCollectionDataSource(collection));
            switch(fileType)
            {
                case EXPORT_FILE_PDF:
                    isCreated = this.generatePDFFile(REPORT_PERSONALREPORT + filenum, print, strExportFile);
                    break;
                case EXPORT_FILE_XLS:
                    isCreated = this.generateXLSFile(REPORT_PERSONALREPORT + filenum, print, strExportFile);
                    break;
            }
        }
        catch(Exception e)
        {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			 return ReportGeneratorLogic.RESULT_ERROR;
        }

		return ReportGeneratorLogic.RESULT_SUCCESS;
    }

    //PDF と　Excel ファイルを作成
    private int generateFile(Collection collection, HashMap<String, Object> paramMap, int fileType )
    {
        boolean isCreated = false;
        
        try
        {
            JasperReport jasperPerson           =  this.loadReport(REPORT_PERSONALREPORT_JASPER, REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubTechnic       =  this.loadReport(REPORT_PERSONALREPORT_TECHNIC, REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubService       =  this.loadReport(REPORT_PERSONALREPORT_SERVICE, REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubStock         =  this.loadReport(REPORT_PERSONALREPORT_STOCK,REPORT_FILE_TYPE_JASPER);
            JasperReport jasperSubNewService    =  this.loadReport(REPORT_PERSONALREPORT_NEWSERVICE, REPORT_FILE_TYPE_JASPER);
            
            paramMap.put("TechnicClassReport",  jasperSubTechnic);
            paramMap.put("ServiceReport",       jasperSubService);
            paramMap.put("StockReport",         jasperSubStock);
            paramMap.put("NewServiceReport",    jasperSubNewService);
            
            JasperPrint print = JasperFillManager.fillReport(jasperPerson, paramMap, new JRBeanCollectionDataSource(collection));
            switch(fileType)
            {
                case EXPORT_FILE_PDF:
                    isCreated = this.generateAndPreviewPDFFile(REPORT_PERSONALREPORT, print);
                    break;
                case EXPORT_FILE_XLS:
                    isCreated = this.generateAndPreviewXLSFile(REPORT_PERSONALREPORT, print);
                    break;
            }
        }
        catch(Exception e)
        {
             SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			 return ReportGeneratorLogic.RESULT_ERROR;
        }
		
		return ReportGeneratorLogic.RESULT_SUCCESS;
    }
    /*
    //Excelファイルを作成
    private void outputXLS(String xmlfile, Collection collection, HashMap<String,Object> paramMap)
    {
        try
        {
            JasperReport jasperReport           = null;
            JasperReport jasperSubTechnic       = null;
            JasperReport jasperSubService       = null;
            JasperReport jasperSubStock         = null;
            JasperReport jasperSubNewService    = null;
            
            String subTechnicFileName = this.REPORT_PERSONALREPORT_TECHNIC;
            String subServiceFileName = this.REPORT_PERSONALREPORT_SERVICE;
            String subStockFileName   = this.REPORT_PERSONALREPORT_STOCK;
            String subNewFileName     = this.REPORT_PERSONALREPORT_NEWSERVICE;
            
            InputStream report      =  IndividualReportLogic.class.getResourceAsStream(this.REPORT_RESOURCE_PATH + xmlfile + this.REPORT_XML_FILE_EXT);
       
            
            if(".jrxml".equals(this.REPORT_XML_FILE_EXT))
	    {
		jasperReport = JasperCompileManager.compileReport(report);
	    }
	    else if(".jasper".equals(this.REPORT_XML_FILE_EXT))
	    {
	       jasperReport = (JasperReport)JRLoader.loadObject(report);
	    }
            
            InputStream reportTechnic = IndividualReportLogic.class.getResourceAsStream(this.REPORT_RESOURCE_PATH + subTechnicFileName + this.REPORT_XML_FILE_EXT);	
            InputStream reportService = IndividualReportLogic.class.getResourceAsStream(this.REPORT_RESOURCE_PATH + subServiceFileName + this.REPORT_XML_FILE_EXT);	
            InputStream reportStock = IndividualReportLogic.class.getResourceAsStream(this.REPORT_RESOURCE_PATH + subStockFileName + this.REPORT_XML_FILE_EXT);	
            InputStream reportNew = IndividualReportLogic.class.getResourceAsStream(this.REPORT_RESOURCE_PATH + subNewFileName + this.REPORT_XML_FILE_EXT);	
       
            jasperSubTechnic = (JasperReport)JRLoader.loadObject(reportTechnic);
            jasperSubService = (JasperReport)JRLoader.loadObject(reportService);
            jasperSubStock  = (JasperReport)JRLoader.loadObject(reportStock);
            jasperSubNewService = (JasperReport)JRLoader.loadObject(reportNew);
            
            paramMap.put("TechnicClassReport", jasperSubTechnic);
            paramMap.put("ServiceReport", jasperSubService);
            paramMap.put("StockReport", jasperSubStock);
            paramMap.put("NewServiceReport", jasperSubNewService);
            
            JasperPrint print = JasperFillManager.fillReport(jasperReport, paramMap, new JRBeanCollectionDataSource(collection));
            
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
	    String dateString = format.format(new java.util.Date());

            String exportFile = ReportManager.getExportPath() + xmlfile + dateString + ".xls";
	    JRXlsExporter exporter = new JRXlsExporter(); 
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print); 
	    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, exportFile); 
	    exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE); 
	    exporter.exportReport();
	    Runtime runtime = Runtime.getRuntime();
	    runtime.exec(ReportManager.getPdfViewerPath() + " " + exportFile);
            
        }
        catch(Exception e)
        {
            System.out.println(e.getCause().toString());
        }
    }
    */
    //ユーザを画面入力データDBに保存する
    //データDBである場合、更新、無い場合、追加
    private void registerDB()
    {   //更新または追加クエリーを作成
        String updateStr = this.updateSQL();
        String inserStr  = this.insertSQL();
        
        try
        {
            int updateCnt = dbConnection.executeUpdate(updateStr);
            System.out.println(updateCnt);
            if( updateCnt == 0 ){
                dbConnection.execute(inserStr);
            }         
        }
        catch(Exception e)
        {
            System.out.println(e.getCause().toString());
        }
    }
    
    //追加クエリー
    private String insertSQL()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date today = new Date();
        
        String sql = "insert into data_bizreport_log" + newLine +
                     "(shop_id,output_date,weather,memo,exit_time, close_time,staff_id, insert_date,update_date,delete_date)" + newLine +
                     "values" + newLine +
                     "(" + this.getShopId() + comma +
                     apostrophe + dateFormat.format(this.getOutputDate()) + apostrophe + comma +
                     apostrophe + "" + apostrophe + comma +
                     apostrophe + "" + apostrophe + comma +
                     apostrophe + dateFormat.format(this.getOutputDate()) + apostrophe + comma + newLine +
                     apostrophe + dateFormat.format(this.getOutputDate()) + apostrophe + comma +
                     this.staffinfo.getStaffID() + comma +
                     apostrophe + todayFormat.format(today) + apostrophe + comma +
                     apostrophe + todayFormat.format(today) + apostrophe + comma +
                     "null )" ;
        
        return sql;
    }
    
    //更新クエリー
    private String updateSQL()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date today = new Date();
      
        
        String sql = " update data_bizreport_log" + newLine +
                     "set weather = '',"  +  newLine +
                     "memo = ''," + newLine +
                     "exit_time = '" + dateFormat.format(this.getOutputDate() ) + "'," + newLine +      
                     "close_time = '" + dateFormat.format(this.getOutputDate() ) + "'," + newLine +   
                     "staff_id = " + this.staffinfo.getStaffID() + "," + newLine +
                     "update_date = '" + todayFormat.format(today) + "'" + newLine +
                     "where shop_id = " + this.getShopId() +  " AND" + newLine +
                     "output_date = '" + dateFormat.format(this.getOutputDate() ) + "' AND"  + newLine +
                     "delete_date is null";
        
        return sql;
    }
    
    //技術用クエリー
    private String technicSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        
        sql.append(" select");
        sql.append("      mc.technic_class_name as technicName");
        sql.append("     ,sum( sd.product_num ) as technicCount");
        sql.append("     ,sum( sd.discount_detail_value_no_tax ) as technicAmount");
        sql.append("     ,sum( case when mc.technic_class_name = 'モニタ' then sd.discount_detail_value_no_tax else 0 end) as monitorAmount");
        sql.append(" from");
        sql.append("      view_data_sales_detail_valid sd");
        sql.append("     ,mst_technic mt");
        sql.append("     ,mst_technic_class mc");
        sql.append(" where");
        sql.append("         sd.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and sd.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     and sd.staff_id = " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()));
        sql.append("     and sd.product_division = " + DIVISION_TECHNIC);
        sql.append("     and sd.product_id = mt.technic_id");
        sql.append("     and mt.delete_date is null");
        sql.append("     and mt.technic_class_id = mc.technic_class_id");
        sql.append("     and mc.delete_date is null");
        sql.append(" group by");
        sql.append("      mc.technic_class_id");
        sql.append("     ,mc.technic_class_name");
        sql.append("     ,mc.display_seq");
        sql.append(" order by");
        sql.append("     mc.display_seq");

        return sql.toString();
                 
    }
    
    //商品用クエリー
    private String stockSQL()
    {
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      mt.item_name as stockName");
        sql.append("     ,sum(sd.discount_detail_value_no_tax) as stockSale");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid  sd");
        sql.append("         inner join mst_item mt");
        sql.append("                 on sd.product_id = mt.item_id");
        sql.append("         inner join mst_item_class mc");
        sql.append("                 on mt.item_class_id = mc.item_class_id");
        sql.append(" where");
        sql.append("         sd.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and sd.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     and sd.product_division in (2, 4)");
        sql.append("     and sd.detail_staff_id = " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()));
        sql.append(" group by");
        sql.append("      mt.item_name");
        sql.append("     ,mt.display_seq");
        sql.append(" order by");
        sql.append("      mt.display_seq");

        return sql.toString();

    }
    //主担当取扱いクエリー
    private String customerSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        
        sql.append(" select");
        sql.append("      coalesce(mc.customer_name1, '') as name1");
        sql.append("     ,coalesce(mc.customer_name2, '') as name2");
        sql.append("     ,get_visit_count(mc.customer_id, sv.shop_id, sv.sales_date ) as vCount");
        sql.append("     ,mc.customer_no as num");
        sql.append("     ,mc.customer_id as id");
        sql.append(" from");
        sql.append("     view_data_sales_valid sv");
        sql.append("         inner join mst_customer mc");
        sql.append("                 on sv.customer_id = mc.customer_id");
        sql.append(" where");
        sql.append("         sv.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and sv.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     and sv.staff_id = " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()));
        sql.append("     and mc.delete_date is null");
        sql.append("     and exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 view_data_sales_detail_valid dsd");
        sql.append("             where");
        sql.append("                     shop_id = sv.shop_id");
        sql.append("                 and slip_no = sv.slip_no");
        sql.append("                 and dsd.product_division = 1");
        sql.append("         )");
        sql.append("     and not exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 view_data_sales_detail_valid dsd");
        sql.append("                     left join mst_technic as M_TEQ");
        sql.append("                            on dsd.product_id = M_TEQ.technic_id");
        sql.append("                     left join mst_technic_class as M_TEQC");
        sql.append("                            on M_TEQ.technic_class_id = M_TEQC.technic_class_id");
        sql.append("             where");
        sql.append("                     shop_id = sv.shop_id");
        sql.append("                 and slip_no = sv.slip_no");
        sql.append("                 and dsd.product_division = 1");
        sql.append("                 and M_TEQC.technic_class_name = 'モニタ'");
        sql.append("         )");
        sql.append(" order by");
        sql.append("     sv.slip_no");

        return sql.toString();
    }
    //開拓新規クエリー
    private String pioneerSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        
        sql.append(" select");
        sql.append("      coalesce(mc.customer_name1, '') as name1");
        sql.append("     ,coalesce(mc.customer_name2, '') as name2");
        sql.append("     ,mc.customer_id as id");
        sql.append("     ,mc.customer_no as no");
        sql.append(" from");
        sql.append("     data_pioneer dp");
        sql.append("         inner join view_data_sales_valid sv");
        sql.append("                 on dp.slip_no = sv.slip_no");
        sql.append("                and dp.shop_id = sv.shop_id");
        sql.append("         inner join mst_customer mc");
        sql.append("                 on sv.customer_id = mc.customer_id");
        sql.append("                and mc.delete_date is null");
        sql.append(" where");
        sql.append("         dp.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and dp.staff_id = " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()));
        sql.append("     and dp.delete_date is null");
        sql.append("     and sv.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append(" order by");
        sql.append("     dp.slip_no");
        
         return sql.toString();
    }
    //指名クエリー
    private String shimeiSQL()
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     count(customer_id) as designated");
        sql.append(" from");
        sql.append("     view_data_sales_valid ds");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     and staff_id = " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()));
        sql.append("     and designated_flag = 't'");
        sql.append("     and not exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 view_data_sales_detail_valid dsd");
        sql.append("                     left join mst_technic as M_TEQ");
        sql.append("                            on dsd.product_id = M_TEQ.technic_id");
        sql.append("                     left join mst_technic_class as M_TEQC");
        sql.append("                            on M_TEQ.technic_class_id = M_TEQC.technic_class_id");
        sql.append("             where");
        sql.append("                     shop_id = ds.shop_id");
        sql.append("                 and slip_no = ds.slip_no");
        sql.append("                 and M_TEQC.technic_class_name = 'モニタ'");
        sql.append("         )");
        sql.append("     and exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 view_data_sales_detail_valid dsd");
        sql.append("             where");
        sql.append("                     shop_id = ds.shop_id");
        sql.append("                 and slip_no = ds.slip_no");
        sql.append("                 and product_division = " + DIVISION_TECHNIC);
        sql.append("         )");

        return sql.toString();
    }

    private boolean existsStaffSalesData() {
        
        boolean result = false;
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("     count(distinct slip_no) as cnt");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("             slip_no");
        sql.append("         from");
        sql.append("             view_data_sales_detail_valid");
        sql.append("         where");
        sql.append("                 shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("             and sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("             and " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()) + " in (staff_id, detail_staff_id)");
        
        sql.append("         union");
        
        sql.append("         select");
        sql.append("             ds.slip_no");
        sql.append("         from");
        sql.append("             view_data_sales_valid ds");
        sql.append("                 join data_pioneer dp");
        sql.append("                 using (shop_id, slip_no)");
        sql.append("         where");
        sql.append("                 dp.delete_date is null");
        sql.append("             and dp.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("             and dp.staff_id = " + SQLUtil.convertForSQL(this.staffinfo.getStaffID()));
        sql.append("             and ds.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getOutputDate()));
        sql.append("     ) t");

        try {
            
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            if (rs.next()) {
                result = (rs.getInt("cnt") > 0);
            }
            
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return result;
    }
}

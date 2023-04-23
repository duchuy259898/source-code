/*
 * ShopRankingReportLogic.java
 *
 * Created on 27 August 2008, 15:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import com.geobeck.sosia.pos.hair.report.beans.ShopRankingBean;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;

/**
 *
 * @author shiera.delusa
 */
public class ShopRankingReportLogic extends ReportGeneratorLogic
{
    public static final String FILE_NAME =  "ShopRankingReport";
    public static final String FILE_REPORT_JASPER = "ShopRankingReport.jasper";
    
    private Date reportDateObj;
    
    /**
     * Creates a new instance of ShopRankingReportLogic
     */
    public ShopRankingReportLogic()
    {
    }
    
    public ShopRankingReportLogic( Date targetDateObj )
    {
        this.reportDateObj = targetDateObj;
    }
    
    public void setTargetDateObj( Date targetDate )
    {
        this.reportDateObj = targetDate;
    }
    
    private ArrayList<ShopRankingBean> getAllData()
    {
        ArrayList<ShopRankingBean> list = ShopRankingReportDAO.identifyRanking( this.reportDateObj );
        
        int listSize = list.size();
        int index = 0;

        if ( listSize <= 0 ) return list;

        while ( index < listSize ) {
            ShopRankingBean reportBean = (ShopRankingBean)list.get(index);
            list.set( index, ShopRankingReportDAO.getShopInfo( reportBean, this.reportDateObj ) );
            index++;
        }

        return list;
    }
    
    protected HashMap prepareReportParams()
    {
        HashMap<String,Object> reportParams = new HashMap<String,Object>();
        
        reportParams.put( "reportDate", this.reportDateObj );
        
        return reportParams;
    }
        
    public int generateReport( int exportFileType )
    {
        ArrayList<ShopRankingBean> arrlistBean;

        try {
            
            arrlistBean = getAllData();
            if (arrlistBean.size() <= 0) {
                return ReportGeneratorLogic.RESULT_DATA_NOTHNIG;
            }
/*
            JasperReport jasperReport = this.loadReport( FILE_REPORT_JASPER, ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER );
            JasperPrint reportJasperPrint = JasperFillManager.fillReport(jasperReport, this.prepareReportParams(), new JRBeanCollectionDataSource(arrlistBean) );

            switch( exportFileType )
            {
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile( FILE_NAME, reportJasperPrint );
                    break;

                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile( FILE_NAME, reportJasperPrint );
                    break;
                    
                default:
                    break;
            }
*/

            
            JExcelApi jx = new JExcelApi("業務報告書");
            jx.setTemplateFile("/reports/業務報告書.xls");

            // ヘッダ出力
            jx.setValue(8, 1, this.reportDateObj);

            // 明細開始行
            int row = 4;

            // 追加行数セット
            jx.insertRow(row, arrlistBean.size() - 1);
            
            for (ShopRankingBean d : arrlistBean) {
                jx.setValue(1, row, d.getRanking() + "位");
                jx.setValue(2, row, d.getShopName());
                jx.setValue(3, row, d.getTechnicSales());
                jx.setValue(4, row, d.getItemSales());
                jx.setValue(5, row, d.getTotalSales());
                jx.setValue(6, row, d.getCustomers());
                jx.setValue(7, row, d.getNewCustomers());
                jx.setValue(8, row, d.getTechnicSalesPerStaff());
                jx.setValue(9, row, d.getCustPerStaff());
                jx.setValue(10, row, d.getTechnicPrice());
                jx.setValue(11, row, d.getStaffsOnDuty());
                jx.setValue(12, row, d.getTotalWorkDays());
                jx.setValue(13, row, d.getActualWorkDays());
                jx.setValue(14, row, d.getRemainingWorkDays());
                jx.setValue(15, row, d.getTechnicAccumulated());
                jx.setValue(16, row, d.getTechnicSalesEstimate());
                jx.setValue(17, row, d.getCustomersTotal());
                jx.setValue(18, row, d.getCustomersEstimate());
                jx.setValue(19, row, d.getPreviousYearSales());
                jx.setValue(20, row, d.getLastYearDiff());
                jx.setValue(22, row, d.getTargetSales());
                jx.setValue(23, row, d.getTargetDifference());
                jx.setValue(25, row, d.getOtherExpenses());
                jx.setValue(26, row, d.getTotalExpenses());
                
                row++;
            }

            jx.removeRow(row);

            jx.openWorkbook();
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ReportGeneratorLogic.RESULT_ERROR;
        }
            
        return ReportGeneratorLogic.RESULT_SUCCESS;
    }
    
}

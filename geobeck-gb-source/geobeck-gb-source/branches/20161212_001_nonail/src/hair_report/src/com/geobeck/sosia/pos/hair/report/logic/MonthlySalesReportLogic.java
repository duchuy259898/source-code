/*
 * MonthlySalesReportLogic.java
 *
 * Created on 27 August 2008, 10:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import com.geobeck.sosia.pos.hair.report.beans.MonthlySalesReportBean;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
/**
 *
 * @author shiera.delusa
 */
public class MonthlySalesReportLogic
{
    private Integer intYear;
    private Integer intMonth;
    private MstShop shopinfo;
    private Integer intOpenDays;

    /**
     * Creates a new instance of MonthlySalesReportLogic
     */
    public MonthlySalesReportLogic(Integer intYear, Integer intMonth, MstShop shopinfo)
    {
        this.intYear = intYear;
        this.intMonth = intMonth;
        this.shopinfo = shopinfo;
        this.intOpenDays = MonthlySalesReportDAO.getOpenDays(this.intYear, this.intMonth, this.shopinfo.getShopID());
    }

    public int generateReport( int exportFileType )
    {
        ArrayList<MonthlySalesReportBean> arrlistBean = null;
        
        try {
            
            arrlistBean = fillMonthlySalesInfo();
            if (arrlistBean.size() <= 0) {
                return ReportGeneratorLogic.RESULT_DATA_NOTHNIG;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ReportGeneratorLogic.RESULT_ERROR;
        }

        JExcelApi jx = new JExcelApi("ìXï‹åéä‘îÑè„ï\");
        jx.setTemplateFile("/reports/ìXï‹åéä‘îÑè„ï\.xls");
            
        // ÉwÉbÉ_
        jx.setValue(1, 3, this.intYear);
        jx.setValue(2, 3, this.intMonth);
        jx.setValue(32, 3, this.shopinfo.getShopName());
        jx.setValue(34, 5, this.intOpenDays);

        HashMap<String,Object> reportParams = new HashMap<String,Object>();
        MonthlySalesReportDAO.establishPrevInfo(reportParams, this.intYear, this.intMonth, this.shopinfo.getShopID() );
        jx.setValue(7, 5, reportParams.get("prevTeqSales"));
        jx.setValue(10, 5, reportParams.get("prevCustCnt"));
        jx.setValue(14, 5, reportParams.get("prevDCnt"));
        jx.setValue(17, 5, reportParams.get("prevPCnt"));
        jx.setValue(20, 5, reportParams.get("prevColCnt"));
        jx.setValue(23, 5, reportParams.get("prevTrCnt"));
        jx.setValue(26, 5, reportParams.get("prevNewCnt"));
        jx.setValue(29, 5, reportParams.get("prevItemSales"));

        MonthlySalesReportDAO.establishTgtInfo(reportParams, this.intYear, this.intMonth, this.shopinfo.getShopID() );
        jx.setValue(7, 7, reportParams.get("tgtTeqSales"));
        jx.setValue(10, 7, reportParams.get("tgtCustCnt"));
        jx.setValue(14, 7, reportParams.get("tgtDCnt"));
        jx.setValue(17, 7, reportParams.get("tgtPCnt"));
        jx.setValue(20, 7, reportParams.get("tgtColCnt"));
        jx.setValue(23, 7, reportParams.get("tgtTrCnt"));
        jx.setValue(26, 7, reportParams.get("tgtNewCnt"));
        jx.setValue(29, 7, reportParams.get("tgtItemSales"));
        
        int row = 10;

        for (MonthlySalesReportBean d : arrlistBean) {
        
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.getSalesDay());
            jx.setValue(1, row, cal.get(Calendar.DAY_OF_MONTH));
        
            // ãZèpîÑè„
            jx.setValue(6, row, d.getTechnicSales());
            // ãqêî
            jx.setValue(9, row, d.getCustomers());
            // éwñºêî
            jx.setValue(13, row, d.getNominations());
            // P
            jx.setValue(16, row, d.getPEachDay());
            // Col
            jx.setValue(19, row, d.getColEachDay());
            // Tr
            jx.setValue(22, row, d.getTrEachDay());
            // New
            jx.setValue(25, row, d.getNewEachDay());
            // è§ïiîÑè„
            jx.setValue(28, row, d.getItemSales());
            // Child ãqêî
            jx.setValue(32, row, d.getCustChild());

            row++;
        }
        
        jx.openWorkbook();

        return ReportGeneratorLogic.RESULT_SUCCESS;
    }
    
    private ArrayList fillMonthlySalesInfo()
    {
        return MonthlySalesReportDAO.getMonthlySalesReportBeanData(intYear, intMonth, shopinfo.getShopID(), this.intOpenDays);
    }
    
}

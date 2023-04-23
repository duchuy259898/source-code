/*
 * DailyTechnicSalesReportLogic.java
 *
 * Created on 25 August 2008, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import java.util.ArrayList;
import com.geobeck.sosia.pos.hair.report.beans.DailySalesReportBean;
import com.geobeck.sosia.pos.basicinfo.company.StaffBasicInfo;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.MstShop;
import java.util.Calendar;

/**
 *
 * @author shiera.delusa
 */
public class DailyTechnicSalesReportLogic
{   
    private Integer intYear;
    private Integer intMonth;
    private ArrayList<StaffBasicInfo> staffList;
    private boolean isCurrentShopOnly = true;
    private MstShop shopInfo = null;
    
    /**
     * Creates a new instance of DailyTechnicSalesReportLogic
     */
    public DailyTechnicSalesReportLogic(Integer intYear, Integer intMonth, ArrayList<StaffBasicInfo> list, int index, boolean isCurrentShopOnly, MstShop shopInfo)
    {
        this.intYear = intYear;
        this.intMonth = intMonth;
        this.isCurrentShopOnly = isCurrentShopOnly;
        this.shopInfo = shopInfo;
        
        if (index < 0) {
            this.staffList = list;
        } else {
            this.staffList = new ArrayList<StaffBasicInfo>();
            this.staffList.add(list.get(index));
        }
    }
    
    public int generateReport( int exportFileType )
    {
        ArrayList<OutputData> list = new ArrayList<OutputData>();

        for (StaffBasicInfo staffInfo : this.staffList) {
            Integer intWorkingDays = DailyTechnicSalesReportDAO.getWorkingDays(this.intYear, this.intMonth, staffInfo.getStaffId());
            try {
                ArrayList<DailySalesReportBean> arrlistBean = fillDailySalesInfo(staffInfo.getStaffId(), intWorkingDays);
                if (arrlistBean.size() > 0) {
                    OutputData data = new OutputData();
                    data.list = arrlistBean;
                    data.intWorkingDays = intWorkingDays;
                    data.staffInfo = staffInfo;
                    list.add(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ReportGeneratorLogic.RESULT_ERROR;
            }
        }

        if (list.size() == 0) {
            return ReportGeneratorLogic.RESULT_DATA_NOTHNIG;
        }
        
        JExcelApi jx = new JExcelApi("個別月間売上表");
        jx.setTemplateFile("/reports/個別月間売上表.xls");

        // シートコピー
        for (int i = 0; i < list.size() - 1; i++) {
            jx.copySheet(1,"dummy" + i);
        }
        
        int targetSheetIndex = 1;
        
        for (OutputData data : list) {

            jx.setTargetSheet(targetSheetIndex++);
            
            // シート名
            jx.getTargetSheet().setName(data.staffInfo.getStaffName());

            // ヘッダ
            jx.setValue(1, 3, this.intYear);
            jx.setValue(2, 3, this.intMonth);
            jx.setValue(27, 3, data.staffInfo.getStaffName());
            jx.setValue(1, 5, DailyTechnicSalesReportDAO.getStaffClass(data.staffInfo.getStaffId()));
            jx.setValue(28, 5, data.intWorkingDays);

            int row = 10;
            
            for (DailySalesReportBean d : data.list) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d.getSalesDay());
                jx.setValue(1, row, cal.get(Calendar.DAY_OF_MONTH));

                // 技術売上
                jx.setValue(6, row, d.getTechnicSales());
                // 客数
                jx.setValue(9, row, d.getCustomers());
                // 指名数
                jx.setValue(13, row, d.getNominations());
                // P
                jx.setValue(16, row, d.getPEachDay());
                // Col
                jx.setValue(19, row, d.getColEachDay());
                // Tr
                jx.setValue(22, row, d.getTrEachDay());
                // New
                jx.setValue(25, row, d.getNewEachDay());
                // 日別売上
                jx.setValue(27, row, d.getItemSales());

                row++;
            }
        }

        jx.openWorkbook();
        
        return ReportGeneratorLogic.RESULT_SUCCESS;
    }
    
    private ArrayList fillDailySalesInfo(Integer staffId, Integer intWorkingDays)
    {
        return DailyTechnicSalesReportDAO.getDailySalesReportBeanData(intYear, intMonth, staffId, intWorkingDays, this.isCurrentShopOnly, shopInfo);
    }

    private class OutputData {
        public ArrayList<DailySalesReportBean> list;
        public Integer intWorkingDays;
        public StaffBasicInfo staffInfo;
    }

}
/*
 * RepeaterReportLogic.java
 *
 * Created on 2008/09/26, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.CustomerFluctuationReportBean;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.NumberUtil;
import com.geobeck.util.SQLUtil;
import java.util.logging.Level;
import java.util.ArrayList;

// use for JasperReports

import com.geobeck.sosia.pos.system.SystemInfo;

/**
 *
 * @author trino
 */
public class RepeaterReportLogic extends ReportGeneratorLogic {
    
    private Integer intYear;
    private MstShop shopinfo;
    
    /** Creates a new instance of RepeaterReportLogic */
    public RepeaterReportLogic(Integer intYear, MstShop shopinfo) {
        this.intYear = intYear;
        this.shopinfo = shopinfo;
    }
    
    
    private ArrayList<CustomerFluctuationReportBean> getDetailInfo() throws Exception {
        ArrayList<CustomerFluctuationReportBean> detailList = new ArrayList<CustomerFluctuationReportBean>();
        
        CustomerFluctuationReportBean detailInfo  = null;
        
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
        
        Integer inchargedCustomerCount[]   = new Integer[12];
        Integer intNewCustomerCount[]      = new Integer[12];
        Double dblNewCustomerRate[]        = new Double[12];
        Integer intLostCustomerCount[][]   = new Integer[12][3];
        Double dblLostCustomerRate[][]     = new Double[12][3];
        
        Double[] inchargedCustomerCountAvg = new Double[4];;
        Integer[] newCustomerCountAvg      = new Integer[4];;
        Double[] newCustomerRateAvg        = new Double[4];
        Double[] lostCustomerCountAvg      = new Double[4];
        Double[] lostCustomerRateAvg       = new Double[4];

        String dateCondition = "'" + String.valueOf(intYear) + "/01/01' and '" + String.valueOf(intYear) + "/12/31'";
        
        try {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      BASE.staff_id");
            sql.append("     ,BASE.staff_name");
            sql.append("     ,BASE.each_month");
            sql.append("     ,coalesce(DATA.visitor_count, 0) as visitor_count");
            sql.append("     ,coalesce(DATA.newcustomer_count, 0) as newcustomer_count");
            sql.append("     ,coalesce(DAT4.lostcust, 0) as lostcust4");
            sql.append("     ,coalesce(DAT5.lostcust, 0) as lostcust5");
            sql.append("     ,coalesce(DAT6.lostcust, 0) as lostcust6");
            sql.append(" from ");
            sql.append("     (");
            sql.append("         select");
            sql.append("              m_st.shop_id");
            sql.append("             ,m_st.staff_id");
            sql.append("             ,m_st.staff_no");
            sql.append("             ,m_st.display_seq");
            sql.append("             ,monthes.month as each_month");
            sql.append("             ,(m_st.staff_name1 || '　' || m_st.staff_name2) as staff_name");
            sql.append("         from");
            sql.append("             mst_staff m_st");
            sql.append("             ,(");
            sql.append("                 select '01' as month union");
            sql.append("                 select '02' as month union");
            sql.append("                 select '03' as month union");
            sql.append("                 select '04' as month union");
            sql.append("                 select '05' as month union");
            sql.append("                 select '06' as month union");
            sql.append("                 select '07' as month union");
            sql.append("                 select '08' as month union");
            sql.append("                 select '09' as month union");
            sql.append("                 select '10' as month union");
            sql.append("                 select '11' as month union");
            sql.append("                 select '12' as month");
            sql.append("              ) monthes");
            sql.append("         where");
            sql.append("             m_st.staff_id in");
            sql.append("                 (");
            sql.append("                     select distinct");
            sql.append("                         ds.staff_id");
            sql.append("                     from");
            sql.append("                         view_data_sales_detail_valid ds");
            sql.append("                     where");
            sql.append("                             ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                         and ds.sales_date between " + dateCondition);
            sql.append("                         and ds.product_division = 1");
            sql.append("                         and " + getNotExistsMonitorSQL());
            sql.append("                 )");
            sql.append("     ) BASE");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("                     ,count(distinct ds.slip_no) as visitor_count");
            sql.append("                     ,count(distinct");
            sql.append("                         case when");
            sql.append("                             exists");
            sql.append("                             (");
            sql.append("                                 select 1");
            sql.append("                                 from");
            sql.append("                                     data_pioneer");
            sql.append("                                 where");
            sql.append("                                         delete_date is null");
            sql.append("                                     and shop_id = ds.shop_id");
            sql.append("                                     and slip_no = ds.slip_no");
            sql.append("                                     and staff_id = ds.staff_id");
            sql.append("                             )");
            sql.append("                             then ds.slip_no");
            sql.append("                             else NULL");
            sql.append("                         end");
            sql.append("                      ) as newcustomer_count");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                 group by");
            sql.append("                      ds.staff_id");
            sql.append("                     ,each_month");
            sql.append("             ) as DATA");
            sql.append("              on BASE.staff_id = DATA.staff_id");
            sql.append("             and BASE.each_month = DATA.each_month");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("                     ,count(distinct ds.slip_no) as lostcust");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                     and not exists");
            sql.append("                         (");
            sql.append("                             select 1 from data_sales as BEYOND_SALES");
            sql.append("                             where");
            sql.append("                                     BEYOND_SALES.sales_date  >  ds.sales_date");
            sql.append("                                 and BEYOND_SALES.sales_date  <  date_trunc('month', ds.sales_date + '4 months')");
            sql.append("                                 and BEYOND_SALES.customer_id = ds.customer_id");
            sql.append("                                 and BEYOND_SALES.shop_id = ds.shop_id");
            sql.append("                         )");
            sql.append("                 group by");
            sql.append("                      each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("             ) DAT4");
            sql.append("              on BASE.staff_id = DAT4.staff_id");
            sql.append("             and BASE.each_month = DAT4.each_month");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("                     ,count(distinct ds.slip_no) as lostcust");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                     and not exists");
            sql.append("                         (");
            sql.append("                             select 1 from data_sales as BEYOND_SALES");
            sql.append("                             where");
            sql.append("                                     BEYOND_SALES.sales_date  >  ds.sales_date");
            sql.append("                                 and BEYOND_SALES.sales_date  <  date_trunc('month', ds.sales_date + '5 months')");
            sql.append("                                 and BEYOND_SALES.customer_id = ds.customer_id");
            sql.append("                                 and BEYOND_SALES.shop_id = ds.shop_id");
            sql.append("                         )");
            sql.append("                 group by");
            sql.append("                      each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("             ) DAT5");
            sql.append("              on BASE.staff_id = DAT5.staff_id");
            sql.append("             and BASE.each_month = DAT5.each_month");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("                     ,count(distinct ds.slip_no) as lostcust");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                     and not exists");
            sql.append("                         (");
            sql.append("                             select 1 from data_sales as BEYOND_SALES");
            sql.append("                             where");
            sql.append("                                     BEYOND_SALES.sales_date  >  ds.sales_date");
            sql.append("                                 and BEYOND_SALES.sales_date  <  date_trunc('month', ds.sales_date + '6 months')");
            sql.append("                                 and BEYOND_SALES.customer_id = ds.customer_id");
            sql.append("                                 and BEYOND_SALES.shop_id = ds.shop_id");
            sql.append("                         )");
            sql.append("                 group by");
            sql.append("                      each_month");
            sql.append("                     ,ds.staff_id");
            sql.append("             ) DAT6");
            sql.append("              on BASE.staff_id = DAT6.staff_id");
            sql.append("             and BASE.each_month = DAT6.each_month");
            sql.append(" order by");
            sql.append("      case when BASE.shop_id in (" + SQLUtil.convertForSQL(this.shopinfo.getShopID()) + ") then 0 else 1 end");
            sql.append("     ,BASE.shop_id");
            sql.append("     ,BASE.display_seq");
            sql.append("     ,lpad(BASE.staff_no, 10, '0')");
            sql.append("     ,BASE.staff_id");
            sql.append("     ,BASE.each_month asc");

            result = jdbcConnection.executeQuery( sql.toString() );
            
            Integer staffId = null;
            String staffName = "";
            while (result.next()) {
                int month;
                
                if( staffId != null && staffId != result.getInt("staff_id") ){
                    detailInfo  = new CustomerFluctuationReportBean();
                    detailInfo.setStaffName(staffName);
                    detailInfo.setInchargedCustomerCount(inchargedCustomerCount);
                    detailInfo.setNewCustomerCount(intNewCustomerCount);
                    detailInfo.setLostCustomerCount(intLostCustomerCount);
                    detailInfo.setLostCustomerRate(dblLostCustomerRate);
                    detailInfo.setNewCustomerRate(dblNewCustomerRate);
                    
                    for(int i = 0; i < 4; i++ ){
                        inchargedCustomerCountAvg[i] = (double)((int)inchargedCustomerCount[i*3] + (int)inchargedCustomerCount[i*3+1] + (int)inchargedCustomerCount[i*3+2]) / 3.0;
                        newCustomerCountAvg[i]       = NumberUtil.round((float)((int)intNewCustomerCount[i*3] + (int)intNewCustomerCount[i*3+1] + (int)intNewCustomerCount[i*3+2]) / 3.0F);
                        if( dblNewCustomerRate[i*3] == null || dblNewCustomerRate[i*3+1] == null  || dblNewCustomerRate[i*3+2] == null ){
                            newCustomerRateAvg[i]    = null;
                        }else{
                            newCustomerRateAvg[i]    = ((double)dblNewCustomerRate[i*3] + (double)dblNewCustomerRate[i*3+1] + (double)dblNewCustomerRate[i*3+2]) / 3.0;
                        }
                        
                        if( intLostCustomerCount[i*3][0] == null || intLostCustomerCount[i*3+1][0] == null  || intLostCustomerCount[i*3+2][0] == null ){
                            lostCustomerCountAvg[i]    = null;
                        }else{
                            lostCustomerCountAvg[i]    = ((double)intLostCustomerCount[i*3][0] + (double)intLostCustomerCount[i*3+1][0] + (double)intLostCustomerCount[i*3+2][0]) / 3.0;
                        }
                        
                        if( dblLostCustomerRate[i*3][0] == null || dblLostCustomerRate[i*3+1][0] == null  || dblLostCustomerRate[i*3+2][0] == null ){
                            lostCustomerRateAvg[i]    = null;
                        }else{
                            lostCustomerRateAvg[i]    = ((double)dblLostCustomerRate[i*3][0] + (double)dblLostCustomerRate[i*3+1][0] + (double)dblLostCustomerRate[i*3+2][0]) / 3.0;
                        }
                    }
                    
                    detailInfo.setInchargedCustomerCountAvg(inchargedCustomerCountAvg);
                    detailInfo.setLostCustomerCountAvg(lostCustomerCountAvg);
                    detailInfo.setLostCustomerRateAvg(lostCustomerRateAvg);
                    detailInfo.setNewCustomerCountAvg(newCustomerCountAvg);
                    detailInfo.setNewCustomerRateAvg(newCustomerRateAvg);
                    
                    detailList.add(detailInfo);
                    
                    inchargedCustomerCount    = new Integer[12];
                    intNewCustomerCount       = new Integer[12];
                    dblNewCustomerRate        = new Double[12];
                    intLostCustomerCount      = new Integer[12][3];
                    dblLostCustomerRate       = new Double[12][3];

                    inchargedCustomerCountAvg = new Double[4];;
                    newCustomerCountAvg       = new Integer[4];;
                    newCustomerRateAvg        = new Double[4];
                    lostCustomerCountAvg      = new Double[4];
                    lostCustomerRateAvg       = new Double[4];
                }
                
                staffId = result.getInt("staff_id");
                staffName = result.getString("staff_name");
                month = Integer.valueOf(result.getString("each_month")) - 1;
                intNewCustomerCount[month] = result.getInt("newcustomer_count");
                inchargedCustomerCount[month] = result.getInt("visitor_count");
                intLostCustomerCount[month][0] = result.getInt("lostcust6");
                intLostCustomerCount[month][1] = result.getInt("lostcust5");
                intLostCustomerCount[month][2] = result.getInt("lostcust4");
                
                if (inchargedCustomerCount[month] != 0) {
                    dblNewCustomerRate[month] = (double)intNewCustomerCount[month] / (double)inchargedCustomerCount[month];
                    dblLostCustomerRate[month][0] = (double)intLostCustomerCount[month][0]  / (double)inchargedCustomerCount[month];
                    dblLostCustomerRate[month][1] = (double)intLostCustomerCount[month][1]  / (double)inchargedCustomerCount[month];
                    dblLostCustomerRate[month][2] = (double)intLostCustomerCount[month][2]  / (double)inchargedCustomerCount[month];
                } else {
                    dblNewCustomerRate[month] = null;
                    dblLostCustomerRate[month][0] = null;
                    dblLostCustomerRate[month][1] = null;
                    dblLostCustomerRate[month][2] = null;
                }
            }
            
            if( staffId != null ){
                detailInfo  = new CustomerFluctuationReportBean();
                detailInfo.setStaffName(staffName);
                detailInfo.setInchargedCustomerCount(inchargedCustomerCount);
                detailInfo.setNewCustomerCount(intNewCustomerCount);
                detailInfo.setLostCustomerCount(intLostCustomerCount);
                detailInfo.setLostCustomerRate(dblLostCustomerRate);
                detailInfo.setNewCustomerRate(dblNewCustomerRate);

                for(int i = 0; i < 4; i++ ){
                    inchargedCustomerCountAvg[i] = (double)((int)inchargedCustomerCount[i*3] + (int)inchargedCustomerCount[i*3+1] + (int)inchargedCustomerCount[i*3+2]) / 3.0;
                    newCustomerCountAvg[i]       = NumberUtil.round((float)((int)intNewCustomerCount[i*3] + (int)intNewCustomerCount[i*3+1] + (int)intNewCustomerCount[i*3+2]) / 3.0F);
                    if( dblNewCustomerRate[i*3] == null || dblNewCustomerRate[i*3+1] == null  || dblNewCustomerRate[i*3+2] == null ){
                        newCustomerRateAvg[i]    = null;
                    }else{
                        newCustomerRateAvg[i]    = ((double)dblNewCustomerRate[i*3] + (double)dblNewCustomerRate[i*3+1] + (double)dblNewCustomerRate[i*3+2]) / 3.0;
                    }

                    if( intLostCustomerCount[i*3][0] == null || intLostCustomerCount[i*3+1][0] == null  || intLostCustomerCount[i*3+2][0] == null ){
                        lostCustomerCountAvg[i]    = null;
                    }else{
                        lostCustomerCountAvg[i]    = ((double)intLostCustomerCount[i*3][0] + (double)intLostCustomerCount[i*3+1][0] + (double)intLostCustomerCount[i*3+2][0]) / 3.0;
                    }

                    if( dblLostCustomerRate[i*3][0] == null || dblLostCustomerRate[i*3+1][0] == null  || dblLostCustomerRate[i*3+2][0] == null ){
                        lostCustomerRateAvg[i]    = null;
                    }else{
                        lostCustomerRateAvg[i]    = ((double)dblLostCustomerRate[i*3][0] + (double)dblLostCustomerRate[i*3+1][0] + (double)dblLostCustomerRate[i*3+2][0]) / 3.0;
                    }
                }

                detailInfo.setInchargedCustomerCountAvg(inchargedCustomerCountAvg);
                detailInfo.setLostCustomerCountAvg(lostCustomerCountAvg);
                detailInfo.setLostCustomerRateAvg(lostCustomerRateAvg);
                detailInfo.setNewCustomerCountAvg(newCustomerCountAvg);
                detailInfo.setNewCustomerRateAvg(newCustomerRateAvg);
                
                detailList.add(detailInfo);
            }
            
        } catch( Exception e ) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            throw new Exception("Data error.");
        }
        
        return detailList;
    }
    
    
    public int viewRepeaterReport() {
        
        try {

            JExcelApi jx = new JExcelApi("増減客推移分析表");
            jx.setTemplateFile("/reports/増減客推移分析表.xls");
            
            jx.setValue(1, 1, this.intYear);
            jx.setValue(5, 1, this.shopinfo.getShopName());
            
            ArrayList<CustomerFluctuationReportBean> list = this.getDetailInfo();
            if( list.size() == 0 ) return RESULT_DATA_NOTHNIG;

            // ヘッダ出力
            establishShopSummary(jx);

            // 明細開始行
            int row = 11;
            
            // 余分な行を削除
            for (int i = 0; i < (200 - list.size()); i++ ) {
                for (int j = 0; j < 4; j++) {
                    jx.removeRow(row);
                }
            }

            // 明細出力
            for (CustomerFluctuationReportBean d : list) {

                jx.setValue(1, row, d.getStaffName());

                int col = 3;
                for (int i = 0; i < d.getInchargedCustomerCount().length; i++) {
                    jx.setValue(col, row, d.getInchargedCustomerCount()[i]);
                    col += 3;
                    if ((i + 1) % 3 == 0) col += 2;
                }
                
                col = 3;
                for (int i = 0; i < d.getNewCustomerCount().length; i++) {
                    jx.setValue(col, row + 1, d.getNewCustomerCount()[i]);
                    col += 3;
                    if ((i + 1) % 3 == 0) col += 2;
                }

                col = 3;
                for (int i = 0; i < d.getLostCustomerCount().length; i++) {
                    jx.setValue(col++, row + 2, d.getLostCustomerCount()[i][0]);
                    jx.setValue(col++, row + 2, d.getLostCustomerCount()[i][1]);
                    jx.setValue(col++, row + 2, d.getLostCustomerCount()[i][2]);
                    if ((i + 1) % 3 == 0) col += 2;
                }

                row += 4;
            }
            
            
            jx.openWorkbook();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        
        return RESULT_SUCCESS;
    }
    
    private void establishShopSummary(JExcelApi jx) {
        
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper result = null;
        int i;
        
        Integer intVisitorCount[] = new Integer[12];
        Integer intNewCustomerCount[] = new Integer[12];
        Double dblNewCustomerRate[] = new Double[12];
        Integer intLostCustomerCount4[] = new Integer[12];
        Integer intLostCustomerCount5[] = new Integer[12];
        Integer intLostCustomerCount6[] = new Integer[12];
        Double dblLostCustomerRate4[] = new Double[12];
        Double dblLostCustomerRate5[] = new Double[12];
        Double dblLostCustomerRate6[] = new Double[12];
        
        Integer[] intVisitorCountAvg = new Integer[4];
        Integer[] intNewCustomerCountAvg = new Integer[4];
        Double[] dblNewCustomerRateAvg = new Double[4];
        Integer[] intLostCustomerCount4Avg = new Integer[4];
        Integer[] intLostCustomerCount5Avg = new Integer[4];
        Integer[] intLostCustomerCount6Avg = new Integer[4];
        Double[] dblLostCustomerRate4Avg = new Double[4];
        Double[] dblLostCustomerRate5Avg = new Double[4];
        Double[] dblLostCustomerRate6Avg = new Double[4];

        String dateCondition = "'" + String.valueOf(intYear) + "/01/01' and '" + String.valueOf(intYear) + "/12/31'";
        
        try {

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      BASE.each_month");
            sql.append("     ,coalesce(DATA.visitor_count, 0) as visitor_count");
            sql.append("     ,coalesce(DATA.newcustomer_count, 0) as newcustomer_count");
            sql.append("     ,coalesce(DAT4.lostcust, 0) as lostcust4");
            sql.append("     ,coalesce(DAT5.lostcust, 0) as lostcust5");
            sql.append("     ,coalesce(DAT6.lostcust, 0) as lostcust6");
            sql.append(" from");
            sql.append("     (");
            sql.append("         select");
            sql.append("             monthes.month as each_month");
            sql.append("         from");
            sql.append("         (");
            sql.append("            select '01' as month union");
            sql.append("            select '02' as month union");
            sql.append("            select '03' as month union");
            sql.append("            select '04' as month union");
            sql.append("            select '05' as month union");
            sql.append("            select '06' as month union");
            sql.append("            select '07' as month union");
            sql.append("            select '08' as month union");
            sql.append("            select '09' as month union");
            sql.append("            select '10' as month union");
            sql.append("            select '11' as month union");
            sql.append("            select '12' as month");
            sql.append("         ) monthes");
            sql.append("     ) BASE");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,count(distinct ds.slip_no) as visitor_count");
            
            sql.append("     ,count(distinct");
            sql.append("         case when M_CUST.customer_no <> '0' and get_visit_count(ds.customer_id, ds.shop_id, ds.sales_date) = 1");
            sql.append("             then ds.slip_no");
            sql.append("             else NULL");
            sql.append("         end");
            sql.append("      ) as newcustomer_count");

/*
            sql.append("                     ,count(distinct");
            sql.append("                         case when");
            sql.append("                             M_CUST.before_visit_num +");
            sql.append("                             (");
            sql.append("                                 select count(*)");
            sql.append("                                 from");
            sql.append("                                 (");
            sql.append("                                     select 1");
            sql.append("                                     from");
            sql.append("                                         data_sales");
            sql.append("                                     where");
            sql.append("                                             delete_date is null");
            sql.append("                                         and shop_id = ds.shop_id");
            sql.append("                                         and customer_id = ds.customer_id");
            sql.append("                                         and sales_date <= ds.sales_date");
            sql.append("                                     limit 2");
            sql.append("                                 ) t");
            sql.append("                             ) = 1");
            sql.append("                                 then ds.slip_no");
            sql.append("                                 else NULL");
            sql.append("                         end");
            sql.append("                      ) as newcustomer_count");
*/

            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
//            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                 group by");
            sql.append("                     each_month");
            sql.append("             ) as DATA");
            sql.append("             on BASE.each_month = DATA.each_month");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,count(distinct ds.slip_no) as lostcust");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                     and not exists");
            sql.append("                         (");
            sql.append("                             select 1 from data_sales as BEYOND_SALES");
            sql.append("                             where");
            sql.append("                                     BEYOND_SALES.sales_date  >  ds.sales_date");
            sql.append("                                 and BEYOND_SALES.sales_date  <  date_trunc('month', ds.sales_date + '4 months')");
            sql.append("                                 and BEYOND_SALES.customer_id = ds.customer_id");
            sql.append("                                 and BEYOND_SALES.shop_id = ds.shop_id");
            sql.append("                         )");
            sql.append("                 group by");
            sql.append("                     each_month");
            sql.append("             ) DAT4");
            sql.append("             on BASE.each_month = DAT4.each_month");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,count(distinct ds.slip_no) as lostcust");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                     and not exists");
            sql.append("                         (");
            sql.append("                             select 1 from data_sales as BEYOND_SALES");
            sql.append("                             where");
            sql.append("                                     BEYOND_SALES.sales_date  >  ds.sales_date");
            sql.append("                                 and BEYOND_SALES.sales_date  <  date_trunc('month', ds.sales_date + '5 months')");
            sql.append("                                 and BEYOND_SALES.customer_id = ds.customer_id");
            sql.append("                                 and BEYOND_SALES.shop_id = ds.shop_id");
            sql.append("                         )");
            sql.append("                 group by");
            sql.append("                     each_month");
            sql.append("             ) DAT5");
            sql.append("             on BASE.each_month = DAT5.each_month");
            sql.append("         left join");
            sql.append("             (");
            sql.append("                 select");
            sql.append("                      to_char(ds.sales_date, 'MM') as each_month");
            sql.append("                     ,count(distinct ds.slip_no) as lostcust");
            sql.append("                 from");
            sql.append("                     view_data_sales_detail_valid ds");
            sql.append("                         inner join mst_customer as M_CUST");
            sql.append("                                 on ds.customer_id = M_CUST.customer_id");
            sql.append("                 where");
            sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(this.shopinfo.getShopID()));
            sql.append("                     and ds.sales_date between " + dateCondition);
            sql.append("                     and ds.product_division = 1");
            sql.append("                     and " + getNotExistsMonitorSQL());
            sql.append("                     and M_CUST.customer_no <> '0'");
            sql.append("                     and not exists");
            sql.append("                         (");
            sql.append("                             select 1 from data_sales as BEYOND_SALES");
            sql.append("                             where");
            sql.append("                                     BEYOND_SALES.sales_date  >  ds.sales_date");
            sql.append("                                 and BEYOND_SALES.sales_date  <  date_trunc('month', ds.sales_date + '6 months')");
            sql.append("                                 and BEYOND_SALES.customer_id = ds.customer_id");
            sql.append("                                 and BEYOND_SALES.shop_id = ds.shop_id");
            sql.append("                         )");
            sql.append("                 group by");
            sql.append("                     each_month");
            sql.append("             ) DAT6");
            sql.append("             on BASE.each_month = DAT6.each_month");
            sql.append(" order by");
            sql.append("     BASE.each_month asc");

            result = jdbcConnection.executeQuery(sql.toString());
            
            i = 0;
            while (result.next()) {
                intVisitorCount[i] = result.getInt("visitor_count");
                intNewCustomerCount[i] = result.getInt("newcustomer_count");
                intLostCustomerCount4[i] = result.getInt("lostcust4");
                intLostCustomerCount5[i] = result.getInt("lostcust5");
                intLostCustomerCount6[i] = result.getInt("lostcust6");
                
                if (intVisitorCount[i] != 0) {
                    dblNewCustomerRate[i] = (double)intNewCustomerCount[i] / (double)intVisitorCount[i];
                    dblLostCustomerRate4[i] = (double)intLostCustomerCount4[i] / (double)intVisitorCount[i];
                    dblLostCustomerRate5[i] = (double)intLostCustomerCount5[i] / (double)intVisitorCount[i];
                    dblLostCustomerRate6[i] = (double)intLostCustomerCount6[i] / (double)intVisitorCount[i];
                } else {
                    dblNewCustomerRate[i] = null;
                    dblLostCustomerRate4[i] = null;
                    dblLostCustomerRate5[i] = null;
                    dblLostCustomerRate6[i] = null;
                }
                
                ++i;
            }
            for(i = 0; i < 4; i++ ){
                intVisitorCountAvg[i]       = NumberUtil.round((float)((int)intVisitorCount[i*3] + (int)intVisitorCount[i*3+1] + (int)intVisitorCount[i*3+2]) / 3.0F);
                intNewCustomerCountAvg[i]   = NumberUtil.round((float)((int)intNewCustomerCount[i*3] + (int)intNewCustomerCount[i*3+1] + (int)intNewCustomerCount[i*3+2]) / 3.0F);
                intLostCustomerCount4Avg[i] = NumberUtil.round((float)((int)intLostCustomerCount4[i*3] + (int)intLostCustomerCount4[i*3+1] + (int)intLostCustomerCount4[i*3+2]) / 3.0F);
                intLostCustomerCount5Avg[i] = NumberUtil.round((float)((int)intLostCustomerCount5[i*3] + (int)intLostCustomerCount5[i*3+1] + (int)intLostCustomerCount5[i*3+2]) / 3.0F);
                intLostCustomerCount6Avg[i] = NumberUtil.round((float)((int)intLostCustomerCount6[i*3] + (int)intLostCustomerCount6[i*3+1] + (int)intLostCustomerCount6[i*3+2]) / 3.0F);
                
                if( dblNewCustomerRate[i*3] == null || dblNewCustomerRate[i*3+1] == null  || dblNewCustomerRate[i*3+2] == null ){
                    dblNewCustomerRateAvg[i]    = null;
                }else{
                    dblNewCustomerRateAvg[i]    = ((double)dblNewCustomerRate[i*3] + (double)dblNewCustomerRate[i*3+1] + (double)dblNewCustomerRate[i*3+2]) / 3.0;
                }
                if( dblLostCustomerRate4[i*3] == null || dblLostCustomerRate4[i*3+1] == null  || dblLostCustomerRate4[i*3+2] == null ){
                    dblLostCustomerRate4Avg[i]    = null;
                }else{
                    dblLostCustomerRate4Avg[i]  = ((double)dblLostCustomerRate4[i*3] + (double)dblLostCustomerRate4[i*3+1] + (double)dblLostCustomerRate4[i*3+2]) / 3.0;
                }
                if( dblLostCustomerRate5[i*3] == null || dblLostCustomerRate5[i*3+1] == null  || dblLostCustomerRate5[i*3+2] == null ){
                    dblLostCustomerRate5Avg[i]    = null;
                }else{
                    dblLostCustomerRate5Avg[i]  = ((double)dblLostCustomerRate5[i*3] + (double)dblLostCustomerRate5[i*3+1] + (double)dblLostCustomerRate5[i*3+2]) / 3.0;
                }
                if( dblLostCustomerRate6[i*3] == null || dblLostCustomerRate6[i*3+1] == null  || dblLostCustomerRate6[i*3+2] == null ){
                    dblLostCustomerRate6Avg[i]    = null;
                }else{
                    dblLostCustomerRate6Avg[i]  = ((double)dblLostCustomerRate6[i*3] + (double)dblLostCustomerRate6[i*3+1] + (double)dblLostCustomerRate6[i*3+2]) / 3.0;
                }
            }
/////////
            int col = 3;
            for (i = 0; i < intVisitorCount.length; i++) {
                jx.setValue(col, 3, intVisitorCount[i]);
                col += 3;
                if ((i + 1) % 3 == 0) col += 2;
            }
            
            col = 3;
            for (i = 0; i < intNewCustomerCount.length; i++) {
                jx.setValue(col, 4, intNewCustomerCount[i]);
                col += 3;
                if ((i + 1) % 3 == 0) col += 2;
            }
            
            col = 3;
            for (i = 0; i < intLostCustomerCount4.length; i++) {
                jx.setValue(col, 6, intLostCustomerCount4[i]);
                col += 3;
                if ((i + 1) % 3 == 0) col += 2;
            }

            col = 3;
            for (i = 0; i < intLostCustomerCount6.length; i++) {
                jx.setValue(col, 7, intLostCustomerCount6[i]);
                col += 3;
                if ((i + 1) % 3 == 0) col += 2;
            }

            col = 3;
            for (i = 0; i < intLostCustomerCount5.length; i++) {
                jx.setValue(col, 8, intLostCustomerCount5[i]);
                col += 3;
                if ((i + 1) % 3 == 0) col += 2;
            }

        } catch( Exception e ) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        
        return;
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
        sql.append("         and mtc.technic_class_name = 'モニタ'");
        sql.append(" )");
        
        return sql.toString();
    }
    
}

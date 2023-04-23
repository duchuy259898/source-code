/*
 * MaterialRateLogic_TOM.java
 *
 * Created on 2008/10/17, 15:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.sosia.pos.hair.product.beans.MaterialRateAvgBean_TOM;
import com.geobeck.sosia.pos.hair.product.beans.MaterialRateAvgRankBean_TOM;
import com.geobeck.sosia.pos.hair.product.beans.MaterialRateAvgSubBean_TOM;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author takeda
 */
public class MaterialRateAvgLogic_TOM extends ReportGeneratorLogic{
    
    private String year;

    private static String EXT_JASPER         = ".jasper";
    private static String REPORT             = "MaterialRateAvg_TOM";
    private static String REPORT_SUB         = "MaterialRateAvgSub_TOM";
    private static String REPORT_SUB_SHOP    = "MaterialRateAvgShop_TOM";
   

    /** Creates a new instance of MaterialRateLogic_TOM */
    public MaterialRateAvgLogic_TOM(String year) {
        
        this.year = year;
    }

    public int viewMaterialRateReport(int reportType) {
        
        
        ArrayList<MaterialRateAvgSubBean_TOM> sublist = new ArrayList<MaterialRateAvgSubBean_TOM>();
        MaterialRateAvgSubBean_TOM subBean = new MaterialRateAvgSubBean_TOM();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        
        paramMap.put("reportPeriod", year + "年1月～" + year + "年12月");
        
        try {
            JasperReport jasper = loadReport(REPORT_SUB + EXT_JASPER, REPORT_FILE_TYPE_JASPER);
            paramMap.put("SubName", jasper);

            JasperReport jasperRank = loadReport(REPORT_SUB_SHOP + EXT_JASPER, REPORT_FILE_TYPE_JASPER);
            paramMap.put("SubRankName", jasperRank);
            
            
            ArrayList<MaterialRateAvgBean_TOM> listShop = getShopRate(year);
            subBean.setSubReport(new JRBeanCollectionDataSource(listShop));

            ArrayList<MaterialRateAvgRankBean_TOM> listRank = getShopRanking(year);
            subBean.setSubRankReport(new JRBeanCollectionDataSource(listRank));
            
            sublist.add(subBean);
            
            
            this.generateFile(sublist, paramMap, reportType);
            return RESULT_SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        
    }
    
    //PDF/Excel ファイル作成
    private void generateFile(Collection collection, HashMap<String, Object> paramMap, int fileType) {
        try {
            JasperReport jasperReport = this.loadReport(REPORT + EXT_JASPER, this.REPORT_FILE_TYPE_JASPER);
            JasperPrint  jasperPrint  = JasperFillManager.fillReport(jasperReport, paramMap, new JRBeanCollectionDataSource(collection));
            switch(fileType) {
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile(REPORT, jasperPrint);
                    break;
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile(REPORT, jasperPrint);
                    break;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private ArrayList<MaterialRateAvgBean_TOM> getShopRate(String year) throws Exception
    {
        ArrayList<MaterialRateAvgBean_TOM> list = new ArrayList<MaterialRateAvgBean_TOM>();
        MaterialRateAvgBean_TOM bean = null;
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getShopRateSql(year));
            int ii = 1;
            
            while(rs.next()) {
                bean = new MaterialRateAvgBean_TOM();
                
                bean.setShopName(rs.getString("shop_name"));
                bean.setRate1(rs.getDouble("rate1"));
                bean.setRate2(rs.getDouble("rate2"));
                bean.setRate3(rs.getDouble("rate3"));
                bean.setRate4(rs.getDouble("rate4"));
                bean.setRate5(rs.getDouble("rate5"));
                bean.setRate6(rs.getDouble("rate6"));
                bean.setRate7(rs.getDouble("rate7"));
                bean.setRate8(rs.getDouble("rate8"));
                bean.setRate9(rs.getDouble("rate9"));
                bean.setRate10(rs.getDouble("rate10"));
                bean.setRate11(rs.getDouble("rate11"));
                bean.setRate12(rs.getDouble("rate12"));
                
                list.add(bean);
            }
            
        } catch( SQLException e) {
            e.printStackTrace();
            throw new Exception("Data Error!");
        }
        
        return list;        
    }
    
    private String getShopRateSql(String year)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      m_sp.shop_id");
        sql.append("     ,max(m_sp.shop_name) as shop_name");
        sql.append("     ,max(case when m_sp.month = 1 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate1");
        sql.append("     ,max(case when m_sp.month = 2 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate2");
        sql.append("     ,max(case when m_sp.month = 3 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate3");
        sql.append("     ,max(case when m_sp.month = 4 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate4");
        sql.append("     ,max(case when m_sp.month = 5 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate5");
        sql.append("     ,max(case when m_sp.month = 6 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate6");
        sql.append("     ,max(case when m_sp.month = 7 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate7");
        sql.append("     ,max(case when m_sp.month = 8 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate8");
        sql.append("     ,max(case when m_sp.month = 9 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate9");
        sql.append("     ,max(case when m_sp.month = 10 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate10");
        sql.append("     ,max(case when m_sp.month = 11 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate11");
        sql.append("     ,max(case when m_sp.month = 12 then coalesce(slip.price, 0) / coalesce(sales.price, 1) else 0 end) as rate12");
        sql.append("     ,coalesce(sum(slip.price), 0) / coalesce(sum(sales.price), 1) as ratio");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              ms.shop_id");
        sql.append("             ,ms.shop_name");
        sql.append("             ,m.month");
        sql.append("         from");
        sql.append("             (");
        sql.append("                 select 1 as month union all");
        sql.append("                 select 2 as month union all");
        sql.append("                 select 3 as month union all");
        sql.append("                 select 4 as month union all");
        sql.append("                 select 5 as month union all");
        sql.append("                 select 6 as month union all");
        sql.append("                 select 7 as month union all");
        sql.append("                 select 8 as month union all");
        sql.append("                 select 9 as month union all");
        sql.append("                 select 10 as month union all");
        sql.append("                 select 11 as month union all");
        sql.append("                 select 12 as month");
        sql.append("             ) m");
        sql.append("             cross join mst_shop ms");
        sql.append("         where");
        sql.append("             ms.delete_date is null");
        sql.append("     ) m_sp");
        sql.append("     left join");
        sql.append("         (");
        sql.append("             select");
        sql.append("                  ds.shop_id");
        sql.append("                 ,date_part('month', ds.ship_date) as month");
        sql.append("                 ,sum(dsd.out_num * dsd.cost_price) as price");
        sql.append("             from");
        sql.append("                 data_slip_ship ds");
        sql.append("                     join data_slip_ship_detail dsd");
        sql.append("                     using(shop_id, slip_no)");
        sql.append("             where");
        sql.append("                     dsd.out_class = 1");   // 業務出庫
        sql.append("                 and to_char(ds.ship_date, 'yyyy') = " + SQLUtil.convertForSQL(year));
        sql.append("             group by");
        sql.append("                  ds.shop_id");
        sql.append("                 ,date_part('month', ds.ship_date)");
        sql.append("         ) slip");
        sql.append("         on m_sp.shop_id = slip.shop_id");
        sql.append("        and m_sp.month = slip.month");
        sql.append("     left join");
        sql.append("         (");
        sql.append("             select");
        sql.append("                  shop_id");
        sql.append("                 ,date_part('month', sales_date) as month");
        sql.append("                 ,sum(discount_detail_value_in_tax) as price");
        sql.append("             from");
        sql.append("                 view_data_sales_detail_valid");
        sql.append("             where");
        sql.append("                     product_division = 1");
        sql.append("                 and to_char(sales_date, 'yyyy') = " + SQLUtil.convertForSQL(year));
        sql.append("             group by");
        sql.append("                  shop_id");
        sql.append("                 ,date_part('month', sales_date)");
        sql.append("         ) sales");
        sql.append("         on m_sp.shop_id = sales.shop_id");
        sql.append("        and m_sp.month = sales.month");
        sql.append(" group by");
        sql.append("     m_sp.shop_id");
        sql.append(" order by");
        sql.append("     ratio");

        return sql.toString();
    }
    
    private ArrayList<MaterialRateAvgRankBean_TOM> getShopRanking(String year) throws Exception
    {
        ArrayList<MaterialRateAvgRankBean_TOM> list = new ArrayList<MaterialRateAvgRankBean_TOM>();
        MaterialRateAvgRankBean_TOM bean = null;
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getShopRateSql(year));
            int ii = 1;
            
            while(rs.next()) {
                bean = new MaterialRateAvgRankBean_TOM();
                
                bean.setRank(ii++);
                bean.setShopName(rs.getString("shop_name"));
                bean.setRate(rs.getDouble("ratio"));
                
                list.add(bean);
            }
            
        } catch( SQLException e) {
            e.printStackTrace();
            throw new Exception("Data Error!");
        }
        
        return list;        
    }
    
}

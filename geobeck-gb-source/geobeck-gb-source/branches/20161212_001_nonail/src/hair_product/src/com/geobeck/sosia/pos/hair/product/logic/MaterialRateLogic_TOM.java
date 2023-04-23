/*
 * MaterialRateLogic_TOM.java
 *
 * Created on 2008/10/17, 15:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.sosia.pos.hair.product.beans.MaterialRateBean_TOM;
import com.geobeck.sosia.pos.hair.product.beans.MaterialRateSubBean_TOM;
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
public class MaterialRateLogic_TOM extends ReportGeneratorLogic{
    
    private String year;

    private static String EXT_JASPER         = ".jasper";
    private static String REPORT             = "MaterialRate_TOM";
    private static String REPORT_SUB         = "MaterialRateSub_TOM";
   

    /** Creates a new instance of MaterialRateLogic_TOM */
    public MaterialRateLogic_TOM(String year) {
        
        this.year = year;
    }

    public int viewMaterialRateReport(int reportType) {
        
        ArrayList<MaterialRateSubBean_TOM> sublist = new ArrayList<MaterialRateSubBean_TOM>();
        MaterialRateSubBean_TOM subBean = new MaterialRateSubBean_TOM();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        
        paramMap.put("reportPeriod", year + "年1月～" + year + "年12月");
        
        try {
            // 各月度のランキングデータを設定
            for( int month = 1; month <= 12; month++ ){
                JasperReport jasper = loadReport(REPORT_SUB + EXT_JASPER, REPORT_FILE_TYPE_JASPER);
                paramMap.put("SubName" + String.valueOf(month), jasper);
                
                
                ArrayList<MaterialRateBean_TOM> list = getShopRanking(year + String.format("%02d", month));
                subBean.setSubReport(month-1, new JRBeanCollectionDataSource(list));
            }
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
    
    private ArrayList<MaterialRateBean_TOM> getShopRanking(String yearMonth) throws Exception
    {
        ArrayList<MaterialRateBean_TOM> list = new ArrayList<MaterialRateBean_TOM>();
        MaterialRateBean_TOM bean = null;
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getShopRankingSql(yearMonth));
            int ii = 1;
            
            while(rs.next()) {
                bean = new MaterialRateBean_TOM();
                
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
    
    
    private String getShopRankingSql(String yearMonth)
    {
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      max(m_sp.shop_name) as shop_name");
        sql.append("     ,(coalesce(max(slip.price), 0) / coalesce(max(sales.price), 1)) as ratio");
        sql.append(" from");
        sql.append("     mst_shop m_sp");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      ds.shop_id");
        sql.append("                     ,sum(dsd.out_num * dsd.cost_price) as price");
        sql.append("                 from");
        sql.append("                     data_slip_ship ds");
        sql.append("                         join data_slip_ship_detail dsd");
        sql.append("                         using(shop_id, slip_no)");
        sql.append("                 where");
        sql.append("                         dsd.out_class = 1"); // 業務出庫
        sql.append("                     and to_char(ds.ship_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                 group by");
        sql.append("                     ds.shop_id");
        sql.append("             ) slip");
        sql.append("             on m_sp.shop_id = slip.shop_id");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      shop_id");
        sql.append("                     ,sum(discount_detail_value_in_tax) as price");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                         product_division = 1");
        sql.append("                     and to_char(sales_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                 group by");
        sql.append("                     shop_id");
        sql.append("             ) sales");
        sql.append("             on m_sp.shop_id = sales.shop_id");
        sql.append(" where");
        sql.append("     m_sp.delete_date is null");
        sql.append(" group by");
        sql.append("     m_sp.shop_id");
        sql.append(" order by");
        sql.append("     ratio");

        return sql.toString();
    }

}

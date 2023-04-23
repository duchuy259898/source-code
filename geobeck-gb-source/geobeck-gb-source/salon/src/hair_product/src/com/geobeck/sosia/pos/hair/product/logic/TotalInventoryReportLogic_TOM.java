/*
 * TotalInventoryReportLogic.java
 *
 * Created on 2008/09/25, 15:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.sosia.pos.hair.product.beans.TotalInventoryBean_TOM;
import com.geobeck.sosia.pos.hair.product.beans.TotalInventorySubReportBean_TOM;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Level;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.report.util.ReportManager;

/**
 *
 * @author shiera.delusa
 */
public class TotalInventoryReportLogic_TOM extends ReportGeneratorLogic {
    
    // FILE NAMES:
    private static final String FILE_NAME =  "TotalInventoryReport_TOM";
    private static final String FILE_REPORT_JASPER = "TotalInventoryReport_TOM.jasper";
    private static final String FILE_NAME_SUB_SUPPLIER_TOTAL = "TotalInventoryReport_Supplier_TOM";
    private static final String FILE_REPORT_JASPER_SUPPLIER = "TotalInventoryReport_Supplier_TOM.jasper";
    
    //class variables;
    private MstShop shopDetails = new MstShop();
    private int taxType;
    private String yearMonth = "";
    
    /**
     * Creates a new instance of TotalInventoryReportLogic
     */
    public TotalInventoryReportLogic_TOM( MstShop shopInfo, int taxType, String yearMonth ) {
        this.shopDetails = shopInfo;
        this.taxType = taxType;
        this.yearMonth = yearMonth;
    }
    
    private HashMap prepareReportParams() throws Exception {
        HashMap<String,Object> reportParams = new HashMap<String,Object>();
        
        reportParams.put( "shopName", this.shopDetails.getShopName() );
        reportParams.put( "reportPeriod", yearMonth.substring(0, 4) + "îN" + yearMonth.substring(4, 6) + "åé" );
        
        if( taxType == ReportParameterBean.TAX_TYPE_BLANK ) {
            reportParams.put( "taxType", "ê≈î≤" );
        } else {
            reportParams.put( "taxType", "ê≈çû" );
        }
        
        reportParams.put( "dateReportGeneration", new Date() );
        
        reportParams.put( "supplierJasperReport", getSubReportSupplierJasperReport() );
        
        reportParams.put( "materialRate", getMaterialRate() );
        
        return reportParams;
    }
    
    private JasperReport getSubReportSupplierJasperReport() {
        JasperReport jasperReport = null;
        try {
            jasperReport = this.loadReport( FILE_REPORT_JASPER_SUPPLIER,
                    ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperReport;
    }
    
    public int generateReport( int exportFileType ) {
        
        try {
            ArrayList<TotalInventorySubReportBean_TOM> supplierList = getSuppliersInfo();
            if( supplierList.size() == 0 ){
                return RESULT_DATA_NOTHNIG;
            }
            
            TotalInventoryBean_TOM masterReportBean = new TotalInventoryBean_TOM();
            masterReportBean.setSubReportSupplier( new JRBeanCollectionDataSource( supplierList ) );
            ArrayList<TotalInventoryBean_TOM> masterList = new ArrayList<TotalInventoryBean_TOM>();
            masterList.add( masterReportBean );
            
            JasperReport jasperReport = this.loadReport( FILE_REPORT_JASPER,
                    ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER );
            
            JasperPrint reportJasperPrint = JasperFillManager.fillReport(jasperReport,
                    this.prepareReportParams(), new JRBeanCollectionDataSource( masterList ) );
            
            switch( exportFileType ) {
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile( FILE_NAME, reportJasperPrint );
                    break;
                    
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile( FILE_NAME, reportJasperPrint );
                    break;
                    
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RESULT_ERROR;
        }
        return RESULT_SUCCESS;
    }
    
    /**
     * Get the initial stock information for the édì¸êÊèWåv table in the report
     */
    private ArrayList<TotalInventorySubReportBean_TOM> getSuppliersInfo() throws Exception
    {
        int shopId = this.shopDetails.getShopID();
        ArrayList<TotalInventorySubReportBean_TOM> suppliersList = new ArrayList<TotalInventorySubReportBean_TOM>();
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      base.supplier_id");
        sql.append("     ,base.supplier_no");
        sql.append("     ,base.division");
        sql.append("     ,real_cost_in_tax");
        sql.append("     ,real_cost_no_tax");
        sql.append("     ,in_cost_in_tax");
        sql.append("     ,in_cost_no_tax");
        sql.append("     ,coalesce(out_cost_in_tax, 0) + coalesce(out_cost_in_tax2, 0) + coalesce(out_cost_in_tax3, 0) AS out_cost_in_tax");
        sql.append("     ,coalesce(out_cost_no_tax, 0) + coalesce(out_cost_no_tax2, 0) + coalesce(out_cost_no_tax3, 0) AS out_cost_no_tax");
        sql.append("     ,proper_cost_in_tax");
        sql.append("     ,proper_cost_no_tax");
        sql.append("     ,real_cost_in_tax - proper_cost_in_tax as differ_cost_in_tax");
        sql.append("     ,real_cost_no_tax - proper_cost_no_tax as differ_cost_no_tax");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select distinct");
        sql.append("              m_si.supplier_id");
        sql.append("             ,m_sp.supplier_no");
        sql.append("             ,div.division");
        sql.append("             ,m_sp.supplier_name");
        sql.append("         from");
        sql.append("             mst_supplier_item m_si");
        sql.append("                 left join mst_supplier m_sp");
        sql.append("                        on m_sp.supplier_id = m_si.supplier_id");
        sql.append("                       and m_sp.delete_date is null");
        sql.append("                 left join");
        sql.append("                     (");
        sql.append("                         select 1 as division");
        sql.append("                         union");
        sql.append("                         select 2 as division");
        sql.append("                     ) div");
        sql.append("                     on true");
        sql.append("         where");
        sql.append("             m_si.delete_date is null");
        sql.append("     ) base");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ivd.inventory_division as division");
        sql.append("                     ,max(d_ivd.cost_price) as cost_price");
        sql.append("                     ,sum(case when d_ivd.inventory_division = 1 then m_up.sell_proper_stock else m_up.use_proper_stock end) as proper_stock");
        sql.append("                     ,sum(case when d_ivd.inventory_division = 1 then m_up.sell_proper_stock else m_up.use_proper_stock end * d_ivd.cost_price) as proper_cost_in_tax");
        sql.append("                     ,sum(sign(case when d_ivd.inventory_division = 1 then m_up.sell_proper_stock else m_up.use_proper_stock end * d_ivd.cost_price / (1.0 + get_tax_rate(d_iv.inventory_date))) * ceil(abs(case when d_ivd.inventory_division = 1 then m_up.sell_proper_stock else m_up.use_proper_stock end * d_ivd.cost_price / (1.0 + get_tax_rate(d_iv.inventory_date))))) as proper_cost_no_tax");
        sql.append("                     ,sum(d_ivd.real_stock * d_ivd.cost_price) as real_cost_in_tax");
        sql.append("                     ,sum(sign(d_ivd.real_stock * d_ivd.cost_price / (1.0 + get_tax_rate(d_iv.inventory_date))) * ceil(abs(d_ivd.real_stock * d_ivd.cost_price / (1.0 + get_tax_rate(d_iv.inventory_date))))) as real_cost_no_tax");
        sql.append("                 from");
        sql.append("                     data_inventory d_iv");
        sql.append("                     ,data_inventory_detail d_ivd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ivd.item_id");
        sql.append("                                and m_si.delete_date is null");
        sql.append("                         ,mst_use_product m_up");
        sql.append("                 where");
        sql.append("                         m_up.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and m_up.product_division = 2");
        sql.append("                     and m_up.product_id = m_si.item_id");
        sql.append("                     and d_iv.inventory_id = d_ivd.inventory_id");
        sql.append("                     and d_iv.inventory_division = d_ivd.inventory_division");
        sql.append("                     and d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(d_iv.inventory_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                     and d_ivd.delete_date is null");
        sql.append("                     and d_iv.delete_date is null");
        sql.append("                 group by");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ivd.inventory_division");
        sql.append("             ) s_iv");
        sql.append("             on s_iv.supplier_id = base.supplier_id");
        sql.append("            and s_iv.division = base.division");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ssd.item_use_division as division");
        sql.append("                     ,sum(d_ssd.in_num + d_ssd.attach_num) as in_num");
        sql.append("                     ,sum((d_ssd.in_num) * d_ssd.cost_price) as in_cost_in_tax");
        sql.append("                     ,sum(sign((d_ssd.in_num) * d_ssd.cost_price / (1.0 + get_tax_rate(d_ss.store_date))) * ceil(abs((d_ssd.in_num) * d_ssd.cost_price / (1.0 + get_tax_rate(d_ss.store_date))))) as in_cost_no_tax");
        sql.append("                 from");
        sql.append("                     data_slip_store d_ss");
        sql.append("                     ,data_slip_store_detail d_ssd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ssd.item_id");
        sql.append("                                and m_si.delete_date is null");
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_ss.shop_id = d_ssd.shop_id");
        sql.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sql.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(d_ss.store_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                     and d_ss.delete_date is null");
        sql.append("                     and d_ssd.delete_date is null");
        sql.append("                 group by");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ssd.item_use_division");
        sql.append("             ) s_store");
        sql.append("             on s_store.supplier_id = base.supplier_id");
        sql.append("            and s_store.division = base.division");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ssd.item_use_division as division");
        sql.append("                     ,sum(d_ssd.out_num * d_ssd.cost_price) as out_cost_in_tax");
        sql.append("                     ,sum(sign(d_ssd.out_num * d_ssd.cost_price / (1.0 + get_tax_rate(d_ss.ship_date))) * (abs(d_ssd.out_num * ceil(d_ssd.cost_price / (1.0 + get_tax_rate(d_ss.ship_date)))))) as out_cost_no_tax");
        sql.append("                 from");
        sql.append("                     data_slip_ship d_ss");
        sql.append("                     ,data_slip_ship_detail d_ssd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ssd.item_id");
        sql.append("                                and m_si.delete_date is null");
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_ss.shop_id = d_ssd.shop_id");
        sql.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sql.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(d_ss.ship_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                     and d_ss.delete_date is null");
        sql.append("                     and d_ssd.delete_date is null");
        sql.append("                 group by");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ssd.item_use_division");
        sql.append("             ) s_slip");
        sql.append("             on s_slip.supplier_id = base.supplier_id");
        sql.append("            and s_slip.division = base.division");
        sql.append("          left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,sum(d_sd.product_num * m_si.cost_price) as out_cost_in_tax2");
        sql.append("                     ,sum(sign(d_sd.product_num * m_si.cost_price / (1.0 + get_tax_rate(d_s.sales_date))) * (abs(d_sd.product_num * ceil(m_si.cost_price / (1.0 + get_tax_rate(d_s.sales_date)))))) as out_cost_no_tax2");
        sql.append("                 from");
        sql.append("                     data_sales d_s");
        sql.append("                     ,data_sales_detail d_sd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_sd.product_id");
        sql.append("                                and m_si.delete_date is null");
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_s.shop_id = d_sd.shop_id");
        sql.append("                     and d_s.slip_no = d_sd.slip_no");
        sql.append("                     and d_s.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(d_s.sales_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                     and product_division = 2");
        sql.append("                     and d_s.delete_date is null");
        sql.append("                     and d_sd.delete_date is null");
        sql.append("                 group by");
        sql.append("                     m_si.supplier_id");
        sql.append("             ) sales");
        sql.append("             on sales.supplier_id = base.supplier_id");
        sql.append("            and 1 = base.division");
        sql.append("         left join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ssd.item_use_division as division");
        sql.append("                     ,sum(d_ssd.item_num * m_si.cost_price) as out_cost_in_tax3");
        sql.append("                     ,sum(sign(d_ssd.item_num * m_si.cost_price / (1.0 + get_tax_rate(d_ss.sales_date))) * (abs(d_ssd.item_num * ceil(m_si.cost_price / (1.0 + get_tax_rate(d_ss.sales_date)))))) as out_cost_no_tax3");
        sql.append("                 from");
        sql.append("                     data_staff_sales d_ss");
        sql.append("                     ,data_staff_sales_detail d_ssd");
        sql.append("                         inner join mst_supplier_item m_si");
        sql.append("                                 on m_si.item_id = d_ssd.item_id");
        sql.append("                                and m_si.delete_date is null");
        sql.append("                         left join mst_item m_it");
        sql.append("                                on m_it.item_id = m_si.item_id");
        sql.append("                               and m_it.delete_date is null");
        sql.append("                 where");
        sql.append("                         d_ss.shop_id = d_ssd.shop_id");
        sql.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sql.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(d_ss.sales_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                     and d_ss.delete_date is null");
        sql.append("                     and d_ssd.delete_date is null");
        sql.append("                 group by");
        sql.append("                      m_si.supplier_id");
        sql.append("                     ,d_ssd.item_use_division");
        sql.append("             ) staff");
        sql.append("             on staff.supplier_id = base.supplier_id");
        sql.append("            and staff.division = base.division");
        sql.append(" order by");
        sql.append("      base.supplier_no");
        sql.append("     ,base.supplier_id ");

        try 
        {
            ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
            ResultSetWrapper result = jdbcConnection.executeQuery( sql.toString() );
            TotalInventorySubReportBean_TOM inventoryInfo = null;
            int supplierId = 0; 
            
            while( result.next() )
            {
                if( inventoryInfo == null ){
                    inventoryInfo = new TotalInventorySubReportBean_TOM();
                }else if( supplierId != result.getInt( "supplier_id" ) ){
                    suppliersList.add( inventoryInfo );
                    inventoryInfo = new TotalInventorySubReportBean_TOM();
                }
                supplierId = result.getInt( "supplier_id" );
                inventoryInfo.setSullierName( "íIâµï\ " + result.getString( "supplier_no" ) );
                
                if( taxType == ReportParameterBean.TAX_TYPE_BLANK ){ //ê≈î≤
                    if( result.getInt("division") == 1 ){
                        // é¿ç›å…ã‡äz - ìXîÃ 
                        inventoryInfo.setExistingStockShop( result.getInt( "real_cost_no_tax" ) );
                        // ì¸å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setIncomingStockShop( result.getInt( "in_cost_no_tax" ) );
                        // èoå…ã‡äzÅDìXîÃ 
                        inventoryInfo.setOutgoingStockShop( result.getInt( "out_cost_no_tax" ) );
                        // ìKê≥ç›å…ã‡äzÅDìXîÃ
                        inventoryInfo.setProperStockShop(result.getInt( "proper_cost_no_tax" ) );
                    }else{
                        // é¿ç›å…ã‡äz - ã∆ñ± 
                        inventoryInfo.setExistingStockBusiness( result.getInt( "real_cost_no_tax" ) );
                        // ì¸å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setIncomingStockBusiness( result.getInt( "in_cost_no_tax" ) );
                        // èoå…ã‡äzÅDã∆ñ±
                        inventoryInfo.setOutgoingStockBusiness( result.getInt( "out_cost_no_tax" ) );
                        // ìKê≥ç›å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setProperStockBusiness(result.getInt( "proper_cost_no_tax" ) );
                    }
                }else{
                    if( result.getInt("division") == 1 ){
                        // é¿ç›å…ã‡äz - ìXîÃ 
                        inventoryInfo.setExistingStockShop( result.getInt( "real_cost_in_tax" ) );
                        // ì¸å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setIncomingStockShop( result.getInt( "in_cost_in_tax" ) );
                        // èoå…ã‡äzÅDìXîÃ 
                        inventoryInfo.setOutgoingStockShop( result.getInt( "out_cost_in_tax" ) );
                        // ìKê≥ç›å…ã‡äzÅDìXîÃ
                        inventoryInfo.setProperStockShop(result.getInt( "proper_cost_in_tax" ) );
                    }else{
                        // é¿ç›å…ã‡äz - ã∆ñ± 
                        inventoryInfo.setExistingStockBusiness( result.getInt( "real_cost_in_tax" ) );
                        // ì¸å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setIncomingStockBusiness( result.getInt( "in_cost_in_tax" ) );
                        // èoå…ã‡äzÅDã∆ñ±
                        inventoryInfo.setOutgoingStockBusiness( result.getInt( "out_cost_in_tax" ) );
                        // ìKê≥ç›å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setProperStockBusiness(result.getInt( "proper_cost_in_tax" ) );
                    }
                }
            }
            
            if( inventoryInfo != null ){
                suppliersList.add( inventoryInfo );
            }
            
        } 
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            throw new Exception("Category Data Error.");
        }
        return suppliersList;
    }
    
    
    private Double getMaterialRate() throws Exception
    {
        Double ret = null;
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getMaterialRateSql());
            int ii = 1;
            
            if(rs.next()) {
                ret = rs.getDouble("ratio");
            }
            
        } catch( SQLException e) {
            e.printStackTrace();
            throw new Exception("Data Error!");
        }
        
        return ret;        
    }
    
    private String getMaterialRateSql()
    {
        int shopId = this.shopDetails.getShopID();
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      m_sp.shop_name");
        sql.append("     ,(coalesce(slip.price, 0) / coalesce(sales.price, 1)) as ratio");
        sql.append(" from");
        sql.append("     mst_shop m_sp");
        sql.append("         cross join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                     sum( dsd.out_num * dsd.cost_price ) as price");
        sql.append("                 from");
        sql.append("                     data_slip_ship ds");
        sql.append("                         join data_slip_ship_detail dsd");
        sql.append("                         using(shop_id, slip_no)");
        sql.append("                 where");
        sql.append("                         ds.shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(ds.ship_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                     and dsd.out_class = 1");   // ã∆ñ±èoå…
        sql.append("                     and ds.delete_date is null");
        sql.append("                     and dsd.delete_date is null");
        sql.append("             ) slip");
        sql.append("         cross join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                     sum(discount_detail_value_in_tax) as price");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                         product_division = 1");
        sql.append("                     and shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append("                     and to_char(sales_date, 'yyyymm') = " + SQLUtil.convertForSQL(yearMonth));
        sql.append("             ) sales");
        sql.append(" where");
        sql.append("     m_sp.shop_id = " + SQLUtil.convertForSQL(shopId));

        return sql.toString();
    }

}

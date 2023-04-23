/*
 * TotalInventoryReportLogic.java
 *
 * Created on 2008/09/25, 15:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
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
import com.geobeck.sosia.pos.hair.product.beans.TotalInventorySubReportBean;
import com.geobeck.sosia.pos.hair.product.beans.TotalInventoryBean;
import com.geobeck.sosia.pos.hair.product.DateRange;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.report.util.ReportManager;

/**
 *
 * @author shiera.delusa
 */
public class TotalInventoryReportLogic extends ReportGeneratorLogic {
    
    // FILE NAMES:
    private static final String FILE_NAME =  "TotalInventoryReport";
    private static final String FILE_REPORT_JASPER = "TotalInventoryReport.jasper";
    private static final String FILE_NAME_SUB_CATEGORY_TOTAL = "TotalInventoryReport_CategoryTotal";
    private static final String FILE_REPORT_JASPER_CATEGORY = "TotalInventoryReport_CategoryTotal.jasper";
    private static final String FILE_NAME_SUB_SUPPLIER_TOTAL = "TotalInventoryReport_SupplierTotal";
    private static final String FILE_REPORT_JASPER_SUPPLIER = "TotalInventoryReport_SupplierTotal.jasper";
    
    //class variables;
    private MstShop shopDetails = new MstShop();
    private int taxType;
    private DateRange dateReportRange = new DateRange();
    
    /**
     * Creates a new instance of TotalInventoryReportLogic
     */
    public TotalInventoryReportLogic( MstShop shopInfo, int taxType, DateRange reportRange ) {
        this.shopDetails = shopInfo;
        this.taxType = taxType;
        this.dateReportRange = reportRange;
    }
    
    private HashMap prepareReportParams() {
        HashMap<String,Object> reportParams = new HashMap<String,Object>();
        
        reportParams.put( "shopName", this.shopDetails.getShopName() );
        
        if( this.dateReportRange != null ) {
            reportParams.put( "reportPeriod", this.dateReportRange.toString() );
        }
        
        if( taxType == ReportParameterBean.TAX_TYPE_BLANK ) {
            reportParams.put( "taxType", "ê≈î≤" );
        } else {
            reportParams.put( "taxType", "ê≈çû" );
        }
        
        reportParams.put( "dateReportGeneration", new Date() );
        
        reportParams.put( "categoryJasperReport", getSubReportCategoryJasperReport() );
        reportParams.put( "supplierJasperReport", getSubReportSupplierJasperReport() );
        
        return reportParams;
    }
    
    private JasperReport getSubReportCategoryJasperReport() {
        JasperReport jasperReport = null;
        try {
            jasperReport = this.loadReport( FILE_REPORT_JASPER_CATEGORY,
                    ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperReport;
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
            ArrayList<TotalInventorySubReportBean> categoryList = getCategoriesInfo();
            ArrayList<TotalInventorySubReportBean> supplierList = getSuppliersInfo();
            if( categoryList.size() == 0 && supplierList.size() == 0 ){
                return RESULT_DATA_NOTHNIG;
            }
            
            
            TotalInventoryBean masterReportBean = new TotalInventoryBean();
            masterReportBean.setSubReportCategory( new JRBeanCollectionDataSource( categoryList ) );
            masterReportBean.setSubReportSupplier( new JRBeanCollectionDataSource( supplierList ) );
            ArrayList<TotalInventoryBean> masterList = new ArrayList<TotalInventoryBean>();
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
    private ArrayList<TotalInventorySubReportBean> getSuppliersInfo() throws Exception
    {
        int shopId = this.shopDetails.getShopID();
        ArrayList<TotalInventorySubReportBean> suppliersList = new ArrayList<TotalInventorySubReportBean>();
        
        StringBuffer sqlBuf = new StringBuffer();
        
        String OutNum = "coalesce(out_num, 0) + coalesce(out_num2, 0) + coalesce(out_num3, 0)"; 
        
        sqlBuf.append(" select");
        sqlBuf.append("      base.supplier_id");
        sqlBuf.append("     ,base.supplier_no");
        sqlBuf.append("     ,base.division");
        sqlBuf.append("     ,base.supplier_name");
        sqlBuf.append("     ,init_cost_in_tax");
        sqlBuf.append("     ,init_cost_no_tax");
        sqlBuf.append("     ,real_cost_in_tax");
        sqlBuf.append("     ,real_cost_no_tax");
        sqlBuf.append("     ,in_cost_in_tax");
        sqlBuf.append("     ,in_cost_no_tax");
        sqlBuf.append("     ,coalesce(out_cost_in_tax, 0) + coalesce(out_cost_in_tax2, 0) + coalesce(out_cost_in_tax3, 0) as out_cost_in_tax");
        sqlBuf.append("     ,coalesce(out_cost_no_tax, 0) + coalesce(out_cost_no_tax2, 0) + coalesce(out_cost_no_tax3, 0) as out_cost_no_tax");

/*******************************************************/
/*  í†ïÎç›å…ÇÃåvéZéÆÇÃïsãÔçáèCê≥ [2009-03-04]
 *
	sqlBuf.append( ", (coalesce(initial_stock, 0) + ").append(OutNum).append(" - coalesce(out_num, 0)) * coalesce(cost_price, 0)  as stock_cost_in_tax \n" );
	sqlBuf.append( ", sign((coalesce(initial_stock, 0) + ").append(OutNum).append(" - coalesce(out_num, 0)) * coalesce(cost_price, 0) \n" );
        sqlBuf.append( "      / (1.0 + get_tax_rate(" + SQLUtil.convertForSQL(dateReportRange.getTo()) + ")))  \n" );
        sqlBuf.append( "    * ceil(abs(coalesce(initial_stock, 0) + ").append(OutNum).append(" - coalesce(out_num, 0)) * coalesce(cost_price, 0)  \n" );
        sqlBuf.append( "      / (1.0 + get_tax_rate(" + SQLUtil.convertForSQL(dateReportRange.getTo()) + "))) as stock_cost_no_tax  \n" );
*/

	sqlBuf.append( ",coalesce(init_cost_in_tax, 0) + coalesce(in_cost_in_tax2, 0) - (coalesce(out_cost_in_tax1, 0) + coalesce(out_cost_in_tax2, 0) + coalesce(out_cost_in_tax3, 0)) as stock_cost_in_tax \n" );
	sqlBuf.append( ",coalesce(init_cost_no_tax, 0) + coalesce(in_cost_no_tax2, 0) - (coalesce(out_cost_no_tax1, 0) + coalesce(out_cost_no_tax2, 0) + coalesce(out_cost_no_tax3, 0)) as stock_cost_no_tax \n" );

/*******************************************************/

        sqlBuf.append(" from");
        sqlBuf.append("     (");
        sqlBuf.append("         select distinct");
        sqlBuf.append("              m_si.supplier_id");
        sqlBuf.append("             ,m_sp.supplier_no");
        sqlBuf.append("             ,div.division");
        sqlBuf.append("             ,m_sp.supplier_name");
        sqlBuf.append("         from");
        sqlBuf.append("             mst_supplier_item m_si");
        sqlBuf.append("                 left join mst_supplier m_sp");
        sqlBuf.append("                        on m_sp.supplier_id  = m_si.supplier_id");
        sqlBuf.append("                       and m_sp.delete_date is null");
        sqlBuf.append("                 left join");
        sqlBuf.append("                     (");
        sqlBuf.append("                         select 1 as division");
        sqlBuf.append("                         union");
        sqlBuf.append("                         select 2 as division");
        sqlBuf.append("                     ) div");
        sqlBuf.append("                     on true");
        sqlBuf.append("         where");
        sqlBuf.append("             m_si.delete_date is null");
        sqlBuf.append("     ) base");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ivd.inventory_division as division");
        sqlBuf.append("                     ,max(d_ivd.cost_price) as cost_price");
        sqlBuf.append("                     ,sum(d_ivd.initial_stock)  as initial_stock");
        sqlBuf.append("                     ,sum(d_ivd.initial_stock * d_ivd.cost_price)  as init_cost_in_tax");
        sqlBuf.append("                     ,sum(sign(d_ivd.initial_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date)))");
        sqlBuf.append("                             * ceil(abs(d_ivd.initial_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date))))) as init_cost_no_tax");
        sqlBuf.append("                     ,sum(d_ivd.real_stock * d_ivd.cost_price) as real_cost_in_tax");
        sqlBuf.append("                     ,sum(sign(d_ivd.real_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date)))");
        sqlBuf.append("                             * ceil(abs(d_ivd.real_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date))))) as real_cost_no_tax");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_inventory d_iv");
        sqlBuf.append("                     ,data_inventory_detail d_ivd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ivd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_iv.inventory_id = d_ivd.inventory_id");
        sqlBuf.append("                     and d_iv.inventory_division = d_ivd.inventory_division");
        sqlBuf.append("                     and d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sqlBuf.append("                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ivd.delete_date is null");
        sqlBuf.append("                     and d_iv.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ivd.inventory_division");
        sqlBuf.append("             ) s_iv");
        sqlBuf.append("             on s_iv.supplier_id = base.supplier_id");
        sqlBuf.append("            and s_iv.division = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ssd.item_use_division as division");
        sqlBuf.append("                     ,sum(d_ssd.in_num + d_ssd.attach_num) as in_num");
        sqlBuf.append("                     ,sum((d_ssd.in_num) * d_ssd.cost_price) as in_cost_in_tax");
        sqlBuf.append("                     ,sum(sign((d_ssd.in_num) * d_ssd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))) ");
        sqlBuf.append("                             * ceil(abs((d_ssd.in_num) * d_ssd.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))))) as in_cost_no_tax");
        sqlBuf.append("                     ,sum((d_ssd.in_num + d_ssd.attach_num) * a.cost_price) as in_cost_in_tax2");
        sqlBuf.append("                     ,sum(sign((d_ssd.in_num + d_ssd.attach_num) * a.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))) ");
        sqlBuf.append("                             * ceil(abs((d_ssd.in_num + d_ssd.attach_num) * a.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))))) as in_cost_no_tax2");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_slip_store d_ss");
        sqlBuf.append("                     ,data_slip_store_detail d_ssd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ssd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                         inner join");
        sqlBuf.append("                             (");
        sqlBuf.append("                                 select");
        sqlBuf.append("                                      d_ivd.item_id");
        sqlBuf.append("                                     ,d_ivd.inventory_division");
        sqlBuf.append("                                     ,d_ivd.cost_price");
        sqlBuf.append("                                 from");
        sqlBuf.append("                                     data_inventory d_iv");
        sqlBuf.append("                                         join data_inventory_detail d_ivd");
        sqlBuf.append("                                         using (inventory_id, inventory_division)");
        sqlBuf.append("                                 where");
        sqlBuf.append("                                         d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sqlBuf.append("                                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                             ) a");
        sqlBuf.append("                              on d_ssd.item_id = a.item_id");
        sqlBuf.append("                             and d_ssd.item_use_division = a.inventory_division");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_ss.shop_id = d_ssd.shop_id");
        sqlBuf.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sqlBuf.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_ss.store_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                    and d_ss.store_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ss.delete_date is null");
        sqlBuf.append("                     and d_ssd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ssd.item_use_division");
        sqlBuf.append("             )  s_store");
        sqlBuf.append("             on s_store.supplier_id = base.supplier_id");
        sqlBuf.append("            and s_store.division = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ssd.item_use_division as division");
        sqlBuf.append("                     ,sum(d_ssd.out_num) as out_num");
        sqlBuf.append("                     ,sum(d_ssd.out_num * d_ssd.cost_price) as out_cost_in_tax");
        sqlBuf.append("                     ,sum(sign(d_ssd.out_num * d_ssd.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))) ");
        sqlBuf.append("                             * ceil(abs(d_ssd.out_num * d_ssd.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))))) as out_cost_no_tax");
        sqlBuf.append("                     ,sum(d_ssd.out_num * a.cost_price) as out_cost_in_tax1");
        sqlBuf.append("                     ,sum(sign(d_ssd.out_num * a.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))) ");
        sqlBuf.append("                             * ceil(abs(d_ssd.out_num * a.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))))) as out_cost_no_tax1");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_slip_ship d_ss");
        sqlBuf.append("                     ,data_slip_ship_detail d_ssd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ssd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                         inner join");
        sqlBuf.append("                             (");
        sqlBuf.append("                                 select");
        sqlBuf.append("                                      d_ivd.item_id");
        sqlBuf.append("                                     ,d_ivd.inventory_division");
        sqlBuf.append("                                     ,d_ivd.cost_price");
        sqlBuf.append("                                 from");
        sqlBuf.append("                                     data_inventory d_iv");
        sqlBuf.append("                                         join data_inventory_detail d_ivd");
        sqlBuf.append("                                         using (inventory_id, inventory_division)");
        sqlBuf.append("                                 where");
        sqlBuf.append("                                         d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sqlBuf.append("                                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                             ) a");
        sqlBuf.append("                              on d_ssd.item_id = a.item_id");
        sqlBuf.append("                             and d_ssd.item_use_division = a.inventory_division");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_ss.shop_id = d_ssd.shop_id");
        sqlBuf.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sqlBuf.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_ss.ship_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                     and d_ss.ship_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ss.delete_date is null");
        sqlBuf.append("                     and d_ssd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ssd.item_use_division");
        sqlBuf.append("             ) s_slip");
        sqlBuf.append("             on s_slip.supplier_id = base.supplier_id");
        sqlBuf.append("            and s_slip.division = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,sum(d_sd.product_num) as out_num2");
        sqlBuf.append("                     ,sum(d_sd.product_num * m_si.cost_price) as out_cost_in_tax2");
        sqlBuf.append("                     ,sum(sign(d_sd.product_num * m_si.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_s.sales_date))) ");
        sqlBuf.append("                             * ceil(abs(d_sd.product_num * m_si.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_s.sales_date))))) as out_cost_no_tax2");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_sales d_s");
        sqlBuf.append("                     ,data_sales_detail d_sd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_sd.product_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_s.shop_id = d_sd.shop_id");
        sqlBuf.append("                     and d_s.slip_no = d_sd.slip_no");
        sqlBuf.append("                     and d_s.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_s.sales_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                     and product_division = 2");
        sqlBuf.append("                     and d_s.sales_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_s.delete_date is null");
        sqlBuf.append("                     and d_sd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                     m_si.supplier_id");
        sqlBuf.append("             ) sales");
        sqlBuf.append("             on sales.supplier_id = base.supplier_id");
        sqlBuf.append("            and 1 = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ssd.item_use_division as division");
        sqlBuf.append("                     ,sum(d_ssd.item_num) as out_num3");
        sqlBuf.append("                     ,sum(d_ssd.item_num * m_si.cost_price) as out_cost_in_tax3");
        sqlBuf.append("                     ,sum(sign(d_ssd.item_num * m_si.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.sales_date))) ");
        sqlBuf.append("                             * ceil(abs(d_ssd.item_num * m_si.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.sales_date))))) as out_cost_no_tax3");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_staff_sales d_ss");
        sqlBuf.append("                     ,data_staff_sales_detail d_ssd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ssd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_ss.shop_id = d_ssd.shop_id");
        sqlBuf.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sqlBuf.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_ss.sales_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                     and d_ss.sales_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ss.delete_date is null");
        sqlBuf.append("                     and d_ssd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_si.supplier_id");
        sqlBuf.append("                     ,d_ssd.item_use_division");
        sqlBuf.append("             )  staff");
        sqlBuf.append("             on staff.supplier_id = base.supplier_id");
        sqlBuf.append("            and staff.division = base.division");
        sqlBuf.append(" order by");
        sqlBuf.append("      base.supplier_no");
        sqlBuf.append("     ,base.supplier_id");

        try 
        {
            ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
            ResultSetWrapper result = jdbcConnection.executeQuery( sqlBuf.toString() );
            TotalInventorySubReportBean inventoryInfo = null;
            int supplierId = 0; 
            
            while( result.next() )
            {
                if( inventoryInfo == null ){
                    inventoryInfo = new TotalInventorySubReportBean();
                }else if( supplierId != result.getInt( "supplier_id" ) ){
                    // ä˙éÒç›å…ã‡äz-çáåv and é¿ç›å…ã‡äz-çáåv
                    inventoryInfo.setInitialStockTotal();
                    inventoryInfo.setExistingStockTotal();

                    //ì¸å…ã‡äz
                    inventoryInfo.setIncomingStockTotal();
                    //èoå…ã‡äz
                    inventoryInfo.setOutgoingStockTotal();

                    //í†ïÎç›å…ã‡äz
                    inventoryInfo.setRegisterStockTotal();

                    //âﬂïsë´ã‡äz-ÉfÅ[É^ÉxÅ[ÉXÉAÉNÉZÉXÇ™Ç»Ç¢ÅBåvéZÇæÇØÇ≈Ç∑ÅB
                    inventoryInfo.setExcessStockBusiness();
                    inventoryInfo.setExcessStockShop();
                    inventoryInfo.setExcessStockTotal();

                    suppliersList.add( inventoryInfo );
                    inventoryInfo = new TotalInventorySubReportBean();
                }
                supplierId = result.getInt( "supplier_id" );
                inventoryInfo.setId( result.getInt( "supplier_id" ) );
                inventoryInfo.setCategoryName( result.getString( "supplier_name" ) );
                
                if( taxType == ReportParameterBean.TAX_TYPE_BLANK ){ //ê≈î≤
                    if( result.getInt("division") == 1 ){
                        // ä˙éÒç›å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setInitialStockShop( result.getInt( "init_cost_no_tax" ) );
                        // é¿ç›å…ã‡äz - ìXîÃ 
                        inventoryInfo.setExistingStockShop( result.getInt( "real_cost_no_tax" ) );
                        // ì¸å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setIncomingStockShop( result.getInt( "in_cost_no_tax" ) );
                        // èoå…ã‡äzÅDìXîÃ 
                        inventoryInfo.setOutgoingStockShop( result.getInt( "out_cost_no_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockShop( result.getInt( "stock_cost_no_tax" ) );
                        
                    }else{
                        // ä˙éÒç›å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setInitialStockBusiness( result.getInt( "init_cost_no_tax" ) );
                        // é¿ç›å…ã‡äz - ã∆ñ± 
                        inventoryInfo.setExistingStockBusiness( result.getInt( "real_cost_no_tax" ) );
                        // ì¸å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setIncomingStockBusiness( result.getInt( "in_cost_no_tax" ) );
                        // èoå…ã‡äzÅDã∆ñ±
                        inventoryInfo.setOutgoingStockBusiness( result.getInt( "out_cost_no_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockBusiness( result.getInt( "stock_cost_no_tax" ) );
                    }
                }else{
                    if( result.getInt("division") == 1 ){
                        // ä˙éÒç›å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setInitialStockShop( result.getInt( "init_cost_in_tax" ) );
                        // é¿ç›å…ã‡äz - ìXîÃ 
                        inventoryInfo.setExistingStockShop( result.getInt( "real_cost_in_tax" ) );
                        // ì¸å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setIncomingStockShop( result.getInt( "in_cost_in_tax" ) );
                        // èoå…ã‡äzÅDìXîÃ 
                        inventoryInfo.setOutgoingStockShop( result.getInt( "out_cost_in_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockShop( result.getInt( "stock_cost_in_tax" ) );
                    }else{
                        // ä˙éÒç›å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setInitialStockBusiness( result.getInt( "init_cost_in_tax" ) );
                        // é¿ç›å…ã‡äz - ã∆ñ± 
                        inventoryInfo.setExistingStockBusiness( result.getInt( "real_cost_in_tax" ) );
                        // ì¸å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setIncomingStockBusiness( result.getInt( "in_cost_in_tax" ) );
                        // èoå…ã‡äzÅDã∆ñ±
                        inventoryInfo.setOutgoingStockBusiness( result.getInt( "out_cost_in_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockBusiness( result.getInt( "stock_cost_in_tax" ) );
                    }
                }
            }
            
            if( inventoryInfo != null ){
                // ä˙éÒç›å…ã‡äz-çáåv and é¿ç›å…ã‡äz-çáåv
                inventoryInfo.setInitialStockTotal();
                inventoryInfo.setExistingStockTotal();

                //ì¸å…ã‡äz
                inventoryInfo.setIncomingStockTotal();
                //èoå…ã‡äz
                inventoryInfo.setOutgoingStockTotal();

                //í†ïÎç›å…ã‡äz
                inventoryInfo.setRegisterStockTotal();

                //âﬂïsë´ã‡äz-ÉfÅ[É^ÉxÅ[ÉXÉAÉNÉZÉXÇ™Ç»Ç¢ÅBåvéZÇæÇØÇ≈Ç∑ÅB
                inventoryInfo.setExcessStockBusiness();
                inventoryInfo.setExcessStockShop();
                inventoryInfo.setExcessStockTotal();

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

    /**
     * Get the initial stock information for the ï™óﬁèWåv table in the report
     */
    private ArrayList<TotalInventorySubReportBean> getCategoriesInfo() throws Exception 
    {
        int shopId = this.shopDetails.getShopID();
        ArrayList<TotalInventorySubReportBean> categoriesList = new ArrayList<TotalInventorySubReportBean>();
        StringBuffer sqlBuf = new StringBuffer();

        String OutNum = "coalesce(out_num, 0) + coalesce(out_num2, 0) + coalesce(out_num3, 0)";

        sqlBuf.append(" select");
        sqlBuf.append("      base.item_class_id");
        sqlBuf.append("     ,base.display_seq");
        sqlBuf.append("     ,base.division");
        sqlBuf.append("     ,base.item_class_name");
        sqlBuf.append("     ,init_cost_in_tax");
        sqlBuf.append("     ,init_cost_no_tax");
        sqlBuf.append("     ,real_cost_in_tax");
        sqlBuf.append("     ,real_cost_no_tax");
        sqlBuf.append("     ,in_cost_in_tax");
        sqlBuf.append("     ,in_cost_no_tax");
        sqlBuf.append("     ,coalesce(out_cost_in_tax, 0) + coalesce(out_cost_in_tax2, 0) + coalesce(out_cost_in_tax3, 0) as out_cost_in_tax");
        sqlBuf.append("     ,coalesce(out_cost_no_tax, 0) + coalesce(out_cost_no_tax2, 0) + coalesce(out_cost_no_tax3, 0) as out_cost_no_tax");

/*******************************************************/
/*  í†ïÎç›å…ÇÃåvéZéÆÇÃïsãÔçáèCê≥ [2009-03-04]
 *
	sqlBuf.append( ", (coalesce(initial_stock, 0) + ").append(OutNum).append(" - coalesce(out_num, 0)) * coalesce(cost_price, 0)  as stock_cost_in_tax \n" );
        sqlBuf.append( ", sign((coalesce(initial_stock, 0) + ").append(OutNum).append(" - coalesce(out_num, 0)) * coalesce(cost_price, 0) \n" );
        sqlBuf.append( "      / (1.0 + get_tax_rate(" + SQLUtil.convertForSQL(dateReportRange.getTo()) + ")))  \n" );
        sqlBuf.append( "    * ceil(abs(coalesce(initial_stock, 0) + ").append(OutNum).append(" - coalesce(out_num, 0)) * coalesce(cost_price, 0)  \n" );
        sqlBuf.append( "      / (1.0 + get_tax_rate(" + SQLUtil.convertForSQL(dateReportRange.getTo()) + "))) as stock_cost_no_tax  \n" );
*/

        sqlBuf.append( ",coalesce(init_cost_in_tax, 0) + coalesce(in_cost_in_tax2, 0) - (coalesce(out_cost_in_tax1, 0) + coalesce(out_cost_in_tax2, 0) + coalesce(out_cost_in_tax3, 0)) as stock_cost_in_tax \n" );
	sqlBuf.append( ",coalesce(init_cost_no_tax, 0) + coalesce(in_cost_no_tax2, 0) - (coalesce(out_cost_no_tax1, 0) + coalesce(out_cost_no_tax2, 0) + coalesce(out_cost_no_tax3, 0)) as stock_cost_no_tax \n" );

/*******************************************************/

        sqlBuf.append(" from");
        sqlBuf.append("     (");
        sqlBuf.append("         select distinct");
        sqlBuf.append("              m_itc.item_class_id");
        sqlBuf.append("             ,m_itc.display_seq");
        sqlBuf.append("             ,div.division");
        sqlBuf.append("             ,m_itc.item_class_name ");
        sqlBuf.append("         from");
        sqlBuf.append("             mst_supplier_item  m_si");
        sqlBuf.append("                 left join mst_item m_it");
        sqlBuf.append("                        on m_it.item_id = m_si.item_id");
        sqlBuf.append("                       and m_it.delete_date is null");
        sqlBuf.append("                 left join mst_item_class m_itc");
        sqlBuf.append("                        on m_itc.item_class_id = m_it.item_class_id");
        sqlBuf.append("                       and m_itc.delete_date is null");
        sqlBuf.append("                 left join");
        sqlBuf.append("                     (");
        sqlBuf.append("                         select 1 as division");
        sqlBuf.append("                         union");
        sqlBuf.append("                         select 2 as division");
        sqlBuf.append("                     ) div");
        sqlBuf.append("                     on true");
        sqlBuf.append("         where");
        sqlBuf.append("             m_si.delete_date is null");
        sqlBuf.append("     ) base");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ivd.inventory_division as division");
        sqlBuf.append("                     ,max(d_ivd.cost_price) as cost_price");
        sqlBuf.append("                     ,sum(d_ivd.initial_stock) as initial_stock");
        sqlBuf.append("                     ,sum(d_ivd.initial_stock * d_ivd.cost_price)  as init_cost_in_tax");
        sqlBuf.append("                     ,sum(sign(d_ivd.initial_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date)))");
        sqlBuf.append("                             * ceil(abs(d_ivd.initial_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date))))) as init_cost_no_tax");
        sqlBuf.append("                     ,sum(d_ivd.real_stock * d_ivd.cost_price) as real_cost_in_tax");
        sqlBuf.append("                     ,sum(sign(d_ivd.real_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date)))");
        sqlBuf.append("                             * ceil(abs(d_ivd.real_stock * d_ivd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_iv.inventory_date))))) as real_cost_no_tax");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_inventory d_iv");
        sqlBuf.append("                     ,data_inventory_detail d_ivd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ivd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_iv.inventory_id = d_ivd.inventory_id");
        sqlBuf.append("                     and d_iv.inventory_division = d_ivd.inventory_division");
        sqlBuf.append("                     and d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sqlBuf.append("                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ivd.delete_date is null");
        sqlBuf.append("                     and d_iv.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ivd.inventory_division");
        sqlBuf.append("             ) s_iv");
        sqlBuf.append("             on s_iv.item_class_id = base.item_class_id");
        sqlBuf.append("            and s_iv.division = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ssd.item_use_division as division");
        sqlBuf.append("                     ,sum(d_ssd.in_num + d_ssd.attach_num) as in_num");
        sqlBuf.append("                     ,sum((d_ssd.in_num) * d_ssd.cost_price) as in_cost_in_tax");
        sqlBuf.append("                     ,sum(sign((d_ssd.in_num) * d_ssd.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))) ");
        sqlBuf.append("                             * ceil(abs((d_ssd.in_num) * d_ssd.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))))) as in_cost_no_tax");
        sqlBuf.append("                     ,sum((d_ssd.in_num + d_ssd.attach_num) * a.cost_price) as in_cost_in_tax2");
        sqlBuf.append("                     ,sum(sign((d_ssd.in_num + d_ssd.attach_num) * a.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))) ");
        sqlBuf.append("                             * ceil(abs((d_ssd.in_num + d_ssd.attach_num) * a.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.store_date))))) as in_cost_no_tax2");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_slip_store d_ss");
        sqlBuf.append("                     ,data_slip_store_detail d_ssd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ssd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                         inner join");
        sqlBuf.append("                             (");
        sqlBuf.append("                                 select");
        sqlBuf.append("                                      d_ivd.item_id");
        sqlBuf.append("                                     ,d_ivd.inventory_division");
        sqlBuf.append("                                     ,d_ivd.cost_price");
        sqlBuf.append("                                 from");
        sqlBuf.append("                                     data_inventory d_iv");
        sqlBuf.append("                                         join data_inventory_detail d_ivd");
        sqlBuf.append("                                         using (inventory_id, inventory_division)");
        sqlBuf.append("                                 where");
        sqlBuf.append("                                         d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sqlBuf.append("                                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                             ) a");
        sqlBuf.append("                              on d_ssd.item_id = a.item_id");
        sqlBuf.append("                             and d_ssd.item_use_division = a.inventory_division");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_ss.shop_id = d_ssd.shop_id");
        sqlBuf.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sqlBuf.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_ss.store_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                    and d_ss.store_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ss.delete_date is null");
        sqlBuf.append("                     and d_ssd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ssd.item_use_division");
        sqlBuf.append("             )  s_store");
        sqlBuf.append("             on s_store.item_class_id = base.item_class_id");
        sqlBuf.append("            and s_store.division = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ssd.item_use_division as division");
        sqlBuf.append("                     ,sum(d_ssd.out_num) as out_num");
        sqlBuf.append("                     ,sum(d_ssd.out_num * d_ssd.cost_price) as out_cost_in_tax");
        sqlBuf.append("                     ,sum(sign(d_ssd.out_num * d_ssd.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))) ");
        sqlBuf.append("                             * ceil(abs(d_ssd.out_num * d_ssd.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))))) as out_cost_no_tax");
        sqlBuf.append("                     ,sum(d_ssd.out_num * a.cost_price) as out_cost_in_tax1");
        sqlBuf.append("                     ,sum(sign(d_ssd.out_num * a.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))) ");
        sqlBuf.append("                             * ceil(abs(d_ssd.out_num * a.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.ship_date))))) as out_cost_no_tax1");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_slip_ship d_ss");
        sqlBuf.append("                     ,data_slip_ship_detail d_ssd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ssd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                         inner join");
        sqlBuf.append("                             (");
        sqlBuf.append("                                 select");
        sqlBuf.append("                                      d_ivd.item_id");
        sqlBuf.append("                                     ,d_ivd.inventory_division");
        sqlBuf.append("                                     ,d_ivd.cost_price");
        sqlBuf.append("                                 from");
        sqlBuf.append("                                     data_inventory d_iv");
        sqlBuf.append("                                         join data_inventory_detail d_ivd");
        sqlBuf.append("                                         using (inventory_id, inventory_division)");
        sqlBuf.append("                                 where");
        sqlBuf.append("                                         d_iv.shop_id = " + SQLUtil.convertForSQL(shopId));
        sqlBuf.append("                                     and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                             ) a");
        sqlBuf.append("                              on d_ssd.item_id = a.item_id");
        sqlBuf.append("                             and d_ssd.item_use_division = a.inventory_division");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_ss.shop_id = d_ssd.shop_id");
        sqlBuf.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sqlBuf.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_ss.ship_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                     and d_ss.ship_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ss.delete_date is null");
        sqlBuf.append("                     and d_ssd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ssd.item_use_division");
        sqlBuf.append("             ) s_slip");
        sqlBuf.append("             on s_slip.item_class_id = base.item_class_id");
        sqlBuf.append("            and s_slip.division = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,sum(d_sd.product_num) as out_num2");
        sqlBuf.append("                     ,sum(d_sd.product_num * m_si.cost_price) as out_cost_in_tax2");
        sqlBuf.append("                     ,sum(sign(d_sd.product_num * m_si.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_s.sales_date))) ");
        sqlBuf.append("                             * ceil(abs(d_sd.product_num * m_si.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_s.sales_date))))) as out_cost_no_tax2");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_sales d_s");
        sqlBuf.append("                     ,data_sales_detail d_sd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_sd.product_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_s.shop_id = d_sd.shop_id");
        sqlBuf.append("                     and d_s.slip_no = d_sd.slip_no");
        sqlBuf.append("                     and d_s.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_s.sales_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                     and product_division = 2");
        sqlBuf.append("                     and d_s.sales_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_s.delete_date is null");
        sqlBuf.append("                     and d_sd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                     m_it.item_class_id");
        sqlBuf.append("             ) sales");
        sqlBuf.append("             on sales.item_class_id = base.item_class_id");
        sqlBuf.append("            and 1 = base.division");
        sqlBuf.append("         left join");
        sqlBuf.append("             (");
        sqlBuf.append("                 select");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ssd.item_use_division as division");
        sqlBuf.append("                     ,sum(d_ssd.item_num) as out_num3");
        sqlBuf.append("                     ,sum(d_ssd.item_num * m_si.cost_price) as out_cost_in_tax3");
        sqlBuf.append("                     ,sum(sign(d_ssd.item_num * m_si.cost_price");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.sales_date))) ");
        sqlBuf.append("                             * ceil(abs(d_ssd.item_num * m_si.cost_price ");
        sqlBuf.append("                             / (1.0 + get_tax_rate(d_ss.sales_date))))) as out_cost_no_tax3");
        sqlBuf.append("                 from");
        sqlBuf.append("                     data_staff_sales d_ss");
        sqlBuf.append("                     ,data_staff_sales_detail d_ssd");
        sqlBuf.append("                         inner join mst_supplier_item m_si");
        sqlBuf.append("                                 on m_si.item_id = d_ssd.item_id");
        sqlBuf.append("                                and m_si.delete_date is null");
        sqlBuf.append("                         left join mst_item m_it");
        sqlBuf.append("                                on m_it.item_id = m_si.item_id");
        sqlBuf.append("                               and m_it.delete_date is null");
        sqlBuf.append("                 where");
        sqlBuf.append("                         d_ss.shop_id = d_ssd.shop_id");
        sqlBuf.append("                     and d_ss.slip_no = d_ssd.slip_no");
        sqlBuf.append("                     and d_ss.shop_id = " + SQLUtil.convertForSQL(shopId));
        if( dateReportRange.getFrom() != null ){
            sqlBuf.append("                 and d_ss.sales_date >= " + SQLUtil.convertForSQL(dateReportRange.getFrom()));
        }
        sqlBuf.append("                     and d_ss.sales_date <= " + SQLUtil.convertForSQL(dateReportRange.getTo()));
        sqlBuf.append("                     and d_ss.delete_date is null");
        sqlBuf.append("                     and d_ssd.delete_date is null");
        sqlBuf.append("                 group by");
        sqlBuf.append("                      m_it.item_class_id");
        sqlBuf.append("                     ,d_ssd.item_use_division");
        sqlBuf.append("             )  staff");
        sqlBuf.append("             on staff.item_class_id = base.item_class_id");
        sqlBuf.append("            and staff.division = base.division");
        sqlBuf.append(" order by");
        sqlBuf.append("      base.display_seq");
        sqlBuf.append("     ,base.item_class_id");

        try 
        {
            ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
            ResultSetWrapper result = jdbcConnection.executeQuery( sqlBuf.toString() );
            TotalInventorySubReportBean inventoryInfo = null;
            int itemClassId = 0; 
            
            while( result.next() )
            {
                if( inventoryInfo == null ){
                    inventoryInfo = new TotalInventorySubReportBean();
                }else if( itemClassId != result.getInt( "item_class_id" ) ){
                    // ä˙éÒç›å…ã‡äz-çáåv and é¿ç›å…ã‡äz-çáåv
                    inventoryInfo.setInitialStockTotal();
                    inventoryInfo.setExistingStockTotal();

                    //ì¸å…ã‡äz
                    inventoryInfo.setIncomingStockTotal();
                    //èoå…ã‡äz
                    inventoryInfo.setOutgoingStockTotal();

                    //í†ïÎç›å…ã‡äz
                    inventoryInfo.setRegisterStockTotal();

                    //âﬂïsë´ã‡äz-ÉfÅ[É^ÉxÅ[ÉXÉAÉNÉZÉXÇ™Ç»Ç¢ÅBåvéZÇæÇØÇ≈Ç∑ÅB
                    inventoryInfo.setExcessStockBusiness();
                    inventoryInfo.setExcessStockShop();
                    inventoryInfo.setExcessStockTotal();

                    categoriesList.add( inventoryInfo );
                    inventoryInfo = new TotalInventorySubReportBean();
                }
                itemClassId = result.getInt( "item_class_id" );
                inventoryInfo.setId( result.getInt( "item_class_id" ) );
                inventoryInfo.setCategoryName( result.getString( "item_class_name" ) );
                
                if( taxType == ReportParameterBean.TAX_TYPE_BLANK ){ //ê≈î≤
                    if( result.getInt("division") == 1 ){
                        // ä˙éÒç›å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setInitialStockShop( result.getInt( "init_cost_no_tax" ) );
                        // é¿ç›å…ã‡äz - ìXîÃ 
                        inventoryInfo.setExistingStockShop( result.getInt( "real_cost_no_tax" ) );
                        // ì¸å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setIncomingStockShop( result.getInt( "in_cost_no_tax" ) );
                        // èoå…ã‡äzÅDìXîÃ 
                        inventoryInfo.setOutgoingStockShop( result.getInt( "out_cost_no_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockShop( result.getInt( "stock_cost_no_tax" ) );
                    }else{
                        // ä˙éÒç›å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setInitialStockBusiness( result.getInt( "init_cost_no_tax" ) );
                        // é¿ç›å…ã‡äz - ã∆ñ± 
                        inventoryInfo.setExistingStockBusiness( result.getInt( "real_cost_no_tax" ) );
                        // ì¸å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setIncomingStockBusiness( result.getInt( "in_cost_no_tax" ) );
                        // èoå…ã‡äzÅDã∆ñ±
                        inventoryInfo.setOutgoingStockBusiness( result.getInt( "out_cost_no_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockBusiness( result.getInt( "stock_cost_no_tax" ) );
                    }
                }else{
                    if( result.getInt("division") == 1 ){
                        // ä˙éÒç›å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setInitialStockShop( result.getInt( "init_cost_in_tax" ) );
                        // é¿ç›å…ã‡äz - ìXîÃ 
                        inventoryInfo.setExistingStockShop( result.getInt( "real_cost_in_tax" ) );
                        // ì¸å…ã‡äzÅDìXîÃ 
                        inventoryInfo.setIncomingStockShop( result.getInt( "in_cost_in_tax" ) );
                        // èoå…ã‡äzÅDìXîÃ 
                        inventoryInfo.setOutgoingStockShop( result.getInt( "out_cost_in_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockShop( result.getInt( "stock_cost_in_tax" ) );
                    }else{
                        // ä˙éÒç›å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setInitialStockBusiness( result.getInt( "init_cost_in_tax" ) );
                        // é¿ç›å…ã‡äz - ã∆ñ± 
                        inventoryInfo.setExistingStockBusiness( result.getInt( "real_cost_in_tax" ) );
                        // ì¸å…ã‡äzÅDã∆ñ±
                        inventoryInfo.setIncomingStockBusiness( result.getInt( "in_cost_in_tax" ) );
                        // èoå…ã‡äzÅDã∆ñ±
                        inventoryInfo.setOutgoingStockBusiness( result.getInt( "out_cost_in_tax" ) );
                        //í†ïÎç›å…ã‡äz
                        inventoryInfo.setRegisterStockBusiness( result.getInt( "stock_cost_in_tax" ) );
                    }
                }
            }
            
            if( inventoryInfo != null ){
                // ä˙éÒç›å…ã‡äz-çáåv and é¿ç›å…ã‡äz-çáåv
                inventoryInfo.setInitialStockTotal();
                inventoryInfo.setExistingStockTotal();

                //ì¸å…ã‡äz
                inventoryInfo.setIncomingStockTotal();
                //èoå…ã‡äz
                inventoryInfo.setOutgoingStockTotal();

                //í†ïÎç›å…ã‡äz
                inventoryInfo.setRegisterStockTotal();

                //âﬂïsë´ã‡äz-ÉfÅ[É^ÉxÅ[ÉXÉAÉNÉZÉXÇ™Ç»Ç¢ÅBåvéZÇæÇØÇ≈Ç∑ÅB
                inventoryInfo.setExcessStockBusiness();
                inventoryInfo.setExcessStockShop();
                inventoryInfo.setExcessStockTotal();

                categoriesList.add( inventoryInfo );
            }
            
        } 
        catch( Exception e )
        {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            throw new Exception("Category Data Error.");
        }
        return categoriesList;
    }
    
    
}

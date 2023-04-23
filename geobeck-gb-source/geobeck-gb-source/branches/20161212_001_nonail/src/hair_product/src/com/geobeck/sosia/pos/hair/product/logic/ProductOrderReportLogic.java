/*
 * ProductOrderReportLogic.java
 *
 * Created on 2008/09/25, 9:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.sosia.pos.hair.product.beans.ProductOrderBean;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author takeda
 */
public class ProductOrderReportLogic extends ReportGeneratorLogic {
    
    static private final String REPORT_NAME =  "ProductOrderReport";
    static private final String JASPER_NAME =  REPORT_NAME + ".jasper";
    
    private int shopId;
    private int slipNo;
    private Date salesDate;
    private static String exportFile = null;
    
    /**
     * Creates a new instance of DailyTechnicSalesReportLogic
     */
    private ProductOrderReportLogic( int shopId, int slipNo, Date salesDate )
    {
        this.shopId    = shopId;
        this.slipNo    = slipNo;
        this.salesDate = salesDate;
    }

    public static String getExportFile() {
        return exportFile;
    }

    public static void setExportFile(String aExportFile) {
        exportFile = aExportFile;
    }
    
    static public int OutputPdfReport( int shopId, int slipNo, Date salesDate )
    {
        ProductOrderReportLogic report = new ProductOrderReportLogic(shopId, slipNo, salesDate);
        return report.generateReport(EXPORT_FILE_PDF);
    }
    
    static public int OutputExcelReport( int shopId, int slipNo, Date salesDate )
    {
        ProductOrderReportLogic report = new ProductOrderReportLogic(shopId, slipNo, salesDate);
        return report.generateReport(EXPORT_FILE_XLS);
    }

    static public int OutputPdfReport2( int shopId, int slipNo, Date salesDate )
    {
        ProductOrderReportLogic report = new ProductOrderReportLogic(shopId, slipNo, salesDate);
        return report.generateReport(EXPORT_FILE_PDF2);
    }
    
    private int generateReport( int exportFileType )
    {         
        
        try
        {
            ArrayList list = productOrderList();
            if( list.size() == 0 ){
                return RESULT_DATA_NOTHNIG;
            }
            
            JasperReport jasperReport = this.loadReport( JASPER_NAME, 
                                                ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER );
            
            JasperPrint reportJasperPrint = JasperFillManager.fillReport(jasperReport, 
                    this.prepareReportParams(), new JRBeanCollectionDataSource( list ) );
            
            
            switch( exportFileType )
            {
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile(REPORT_NAME, reportJasperPrint);
                    break;
                    
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile( REPORT_NAME, reportJasperPrint );
                    break;

                case EXPORT_FILE_PDF2:
                    this.generatePDFFile(reportJasperPrint, ProductOrderReportLogic.getExportFile());
                    break;
                    
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return RESULT_ERROR;
        }        
        return RESULT_SUCCESS;
    }
    
    private Map prepareReportParams()
    {
        Map paramMap = new HashMap();
        paramMap.put(JRParameter.REPORT_LOCALE, Locale.JAPANESE );
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(getHeaderSQL());

            if(rs.next())
            {
                // ÉpÉâÉÅÅ[É^ÇÃê∂ê¨
                paramMap.put("SuplierName",  rs.getString("supplier_name"));
                paramMap.put("SuplierFax",   rs.getString("supplier_fax"));
                paramMap.put("OrderNo",      rs.getInt("slip_no"));
                paramMap.put("OrderDate",    rs.getDate("order_date"));
                paramMap.put("ShopName",     rs.getString("shop_name"));
                String str = rs.getString("postal_code");
                paramMap.put("ShopZipCode",  str.substring(0,3) +
                                        "-" + str.substring(3));
                paramMap.put("ShopAddress1", rs.getString("address1") +
                                              rs.getString("address2") +
                                              rs.getString("address3"));
                paramMap.put("ShopAddress2", rs.getString("address4"));
                paramMap.put("StaffName",    rs.getString("staff_name1") +
                                      "Å@" + rs.getString("staff_name2"));
                paramMap.put("ShopFax",      rs.getString("shop_fax"));
                paramMap.put("ShopTel",      rs.getString("phone_number"));
                paramMap.put("SummaryPrice", rs.getInt("summary"));
            }

            rs.close();            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return paramMap;
    }
    
    /**
     * ÉfÅ[É^ÇéÊìæÇ∑ÇÈÇrÇpÇkï∂ÇéÊìæÇ∑ÇÈÅB
     * @return ÉfÅ[É^ÇéÊìæÇ∑ÇÈÇrÇpÇkï∂
     */
    private String getHeaderSQL()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select \n");
        sb.append("msp.supplier_name, \n");
        sb.append("msp.fax_number as supplier_fax, \n");
        sb.append("dso.slip_no, \n");
        sb.append("dso.order_date, \n");
        sb.append("ms.shop_name, \n");
        sb.append("ms.postal_code, \n");
        sb.append("ms.address1, \n");
        sb.append("ms.address2, \n");
        sb.append("ms.address3, \n");
        sb.append("ms.address4, \n");
        sb.append("ms.phone_number, \n");
        sb.append("ms.fax_number as shop_fax, \n");
        sb.append("msf.staff_name1, \n");
        sb.append("msf.staff_name2, \n");
        sb.append("( select sum(cost_price * order_num)");
        sb.append("  from data_slip_order_detail dd");
        sb.append("  where dd.shop_id = dso.shop_id \n");
        sb.append("  and dd.slip_no   = dso.slip_no \n");
        sb.append("  and dd.delete_date is null \n");
        sb.append(") as summary \n");
        sb.append("from data_slip_order dso \n");
        sb.append("left join mst_supplier msp \n");
        sb.append("on  msp.supplier_id = dso.supplier_id \n");
        sb.append("and msp.delete_date is null \n");
        sb.append("left join mst_shop ms \n");
        sb.append("on  ms.shop_id = dso.shop_id \n");
        sb.append("and ms.delete_date is null \n");
        sb.append("left join mst_staff msf \n");
        sb.append("on  msf.staff_id = dso.staff_id \n");
        sb.append("where dso.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n");
        sb.append("and dso.slip_no   = " + SQLUtil.convertForSQL(slipNo) + "\n");
        sb.append("and dso.delete_date is null");
        return sb.toString();
    }
    
    
    private ArrayList productOrderList()
    {
        ArrayList<ProductOrderBean> list = new ArrayList<ProductOrderBean>();
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(getDetailSQL());
            int rowno = 1;
            while(rs.next()){
                list.add( new ProductOrderBean(
                                    rowno,
                                    rs.getString("item_no"),
                                    rs.getString("item_name"),
                                    rs.getInt("cost_price"),
                                    rs.getInt("order_num"),
                                    rs.getInt("price") ));
                ++rowno;
            }
            rs.close();            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return list;
    }

    /**
     * ñæç◊ÉfÅ[É^ÇéÊìæÇ∑ÇÈÇrÇpÇkï∂ÇéÊìæÇ∑ÇÈÅB
     * @return ÉfÅ[É^ÇéÊìæÇ∑ÇÈÇrÇpÇkï∂
     */
    private String getDetailSQL()
    {
    StringBuilder sb = new StringBuilder();
    sb.append("select distinct \n");
    sb.append("dd.item_id, \n");
    sb.append("mit.item_no, \n");
    sb.append("mit.item_name, \n");
    sb.append("sum.order_num, \n");
    sb.append("dd.cost_price, \n");
    sb.append("sum.price \n");
    sb.append("from data_slip_order_detail dd \n");
    sb.append("left join mst_item mit \n");
    sb.append("on mit.item_id = dd.item_id \n");
    sb.append("and dd.delete_date is null \n");
    sb.append("left join( \n");
    sb.append("  select item_id, \n");
    sb.append("   sum(order_num) as order_num, \n");
    sb.append("   sum(cost_price * order_num) as price \n");
    sb.append("  from data_slip_order_detail \n");
    sb.append("  where shop_id = " + SQLUtil.convertForSQL(shopId) + "\n");
    sb.append("  and   slip_no   = " + SQLUtil.convertForSQL(slipNo) +  "\n");
    sb.append("  group by item_id \n");
    sb.append(") sum \n");
    sb.append("on sum.item_id = dd.item_id \n");
    sb.append("where dd.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n");
    sb.append("and dd.slip_no   = " + SQLUtil.convertForSQL(slipNo) +  "\n");
    sb.append("and dd.delete_date is null \n");
    return sb.toString();
    }
        
}

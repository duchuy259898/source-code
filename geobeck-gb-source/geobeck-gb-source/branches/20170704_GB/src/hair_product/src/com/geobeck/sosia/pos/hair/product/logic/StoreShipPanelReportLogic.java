/*
 * StoreShipPanelReportLogic.java
 *
 * Created on 2008/09/26, 11:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.sosia.pos.hair.product.StoreShipPanel;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import com.ibm.icu.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Level;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.product.MstPlace;
import com.geobeck.sosia.pos.hair.product.DateRange;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sosia.pos.hair.product.beans.StoreShipPanelReportBean;

/**
 *
 * @author shiera.delusa
 */
public class StoreShipPanelReportLogic extends ReportGeneratorLogic
{
    // FILE NAMES:
    public static final String FILE_NAME =  "StoreShipPanelReport";
    public static final String FILE_REPORT_JASPER = "StoreShipPanelReport.jasper";
    
    // CLASS VARIABLES:
    private MstShop infoShop = null;
    private MstSupplier infoSupplier = null;
    private DateRange dateRange = null;
    private int typeInventory;
    private int typeOutputRequirement;
    
    /**
     * Creates a new instance of StoreShipPanelReportLogic
     */
    public StoreShipPanelReportLogic( MstShop shopInfo, MstSupplier supplierInfo,
            int inventoryType, int outputReqType, DateRange dateRange )
    {
        this.infoShop = shopInfo;
        this.infoSupplier = supplierInfo;
        this.dateRange = dateRange;
        this.typeInventory = inventoryType;
        this.typeOutputRequirement = outputReqType;
    }
    
    private HashMap prepareReportParams()
    {
        HashMap<String,Object> reportParams = new HashMap<String,Object>();
        
        reportParams.put( "shopName", this.infoShop.getShopName() );
        reportParams.put( "supplierName", this.infoSupplier.getSupplierName() );
        
        if( this.typeInventory == StoreShipPanel.SELL_PROPER )
        {
            reportParams.put( "sectionName", "店販用" );
        }
        else
        {
            reportParams.put( "sectionName", "業務用" );
        }
        
        if( this.typeOutputRequirement == StoreShipPanel.DATA_EXIST)
        {
            reportParams.put( "outputCondition", "入力あり " );
        }
        else if( this.typeOutputRequirement == StoreShipPanel.DATA_NOTHING )
        {
            reportParams.put( "outputCondition", "入力なし " );
        }
        else
        {
            reportParams.put( "outputCondition", "全て" );
        }
        
        reportParams.put( "period", this.dateRange.toString() );
        reportParams.put( "reportGenerationDate", new Date() );
        
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo   = Calendar.getInstance();
        if( dateRange.getFrom() == null ){
            calFrom.setTime(dateRange.getTo());
            calFrom.add(Calendar.DATE, -30);
        }else{
            calFrom.setTime(dateRange.getFrom());
        }
        if( dateRange.getTo() == null ){
            calTo.setTime(dateRange.getFrom());
            calTo.add(Calendar.DATE, 30);
        }else{
            calTo.setTime(dateRange.getTo());
        }
        
        String str = "";
        for(int ii = 0; ii < 31; ii++){
            int day = calFrom.get(Calendar.DATE);
            String fieldName = "day_" + String.format("%02d", ii+1);
            reportParams.put( fieldName, String.format("%02d", day) );
            calFrom.add(Calendar.DATE, 1);
        }
        
        return reportParams;
    }
    
    public int generateReport( int exportFileType )
    {         

        try
        {
            ArrayList<StoreShipPanelReportBean> beanList = getList();
            if( beanList.size() == 0 ){
                return RESULT_DATA_NOTHNIG;
            }
            
            JasperReport jasperReport = this.loadReport( FILE_REPORT_JASPER, 
                    ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER );
            
            JasperPrint reportJasperPrint = JasperFillManager.fillReport(jasperReport, 
                    this.prepareReportParams(),
                    new JRBeanCollectionDataSource( beanList ) );
            
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return RESULT_ERROR;
        }
        
        return RESULT_SUCCESS;
    }
    
    /**
     * This function sets the followin report fields:  分類, 商品名, 期首, and 適正
     * This also saves the item_id and item_class_id into the StoreShipPanelReportBean
     */
    private ArrayList<StoreShipPanelReportBean> getList() throws Exception{
        int shopId     = infoShop.getShopID();
        int supplierID = infoSupplier.getSupplierID();
        
        ArrayList<StoreShipPanelReportBean> storeShipList =
                new ArrayList<StoreShipPanelReportBean>();
        
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append( "select \n" );
        sqlBuffer.append( "  m1.item_id         as item_id \n" );
        sqlBuffer.append( ", m1.item_name       as item_name \n" );
        sqlBuffer.append( ", m1.item_class_id   as item_class_id \n" );
        sqlBuffer.append( ", m2.item_class_name as item_class_name \n" );
        sqlBuffer.append( ", coalesce(initial.initial_stock, initial.initial_stock2, 0) as initial_stock \n" );
        sqlBuffer.append( ", initial.real_stock    as real_stock \n" );
        sqlBuffer.append( ", initial.use_proper_stock   as use_proper_stock \n" );
        sqlBuffer.append( ", initial.sell_proper_stock  as sell_proper_stock \n" );
        if( typeOutputRequirement != StoreShipPanel.DATA_NOTHING ){
            sqlBuffer.append( ", detail.inv_date   as inv_date \n" );
            sqlBuffer.append( ", detail.in_num     as in_num \n" );
            sqlBuffer.append( ", detail.attach_num as attach_num \n" );
            sqlBuffer.append( ", detail.out_num    as out_num \n" );
        }
        sqlBuffer.append( "from ( \n" );
        sqlBuffer.append( "  select \n" );
        sqlBuffer.append( "    m_si.item_id \n" );
        sqlBuffer.append( "    , m_up.use_proper_stock   as use_proper_stock  \n" );
        sqlBuffer.append( "    , m_up.sell_proper_stock  as sell_proper_stock  \n" );
        if( dateRange.getTo() != null ){
            sqlBuffer.append( "  , sd_iv.initial_stock \n" );
            sqlBuffer.append( "  , sd_iv.real_stock \n" );
            
            sqlBuffer.append( "  , ( select real_stock \n");
            sqlBuffer.append( "      from data_inventory_detail dd1 \n");
            sqlBuffer.append( "      ,    data_inventory        dd2 \n");
            sqlBuffer.append( "      where dd1.inventory_id = dd2.inventory_id   \n");
            sqlBuffer.append( "      and   dd1.inventory_division = dd2.inventory_division   \n");
            sqlBuffer.append( "      and   dd2.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n");
            sqlBuffer.append( "      and   dd1.item_id = m_si.item_id \n");
            sqlBuffer.append( "      and   dd2.inventory_date < " + SQLUtil.convertForSQL(dateRange.getFrom()) + " \n");
            sqlBuffer.append( "      and   dd2.inventory_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "      and   dd1.delete_date is null   \n");
            sqlBuffer.append( "      and   dd2.delete_date is null   \n");
            sqlBuffer.append( "      order by inventory_date desc \n");
            sqlBuffer.append( "      limit 1 ) as initial_stock2 \n" );
            
        }else{
            sqlBuffer.append( "  , ( select real_stock \n");
            sqlBuffer.append( "      from data_inventory_detail dd1 \n");
            sqlBuffer.append( "      ,    data_inventory        dd2 \n");
            sqlBuffer.append( "      where dd1.inventory_id = dd2.inventory_id   \n");
            sqlBuffer.append( "      and   dd1.inventory_division = dd2.inventory_division   \n");
            sqlBuffer.append( "      and   dd2.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n");
            sqlBuffer.append( "      and   dd1.item_id = m_si.item_id \n");
            sqlBuffer.append( "      and   dd2.inventory_date < " + SQLUtil.convertForSQL(dateRange.getFrom()) + " \n");
            sqlBuffer.append( "      and   dd2.inventory_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "      and   dd1.delete_date is null   \n");
            sqlBuffer.append( "      and   dd2.delete_date is null   \n");
            sqlBuffer.append( "      order by inventory_date desc \n");
            sqlBuffer.append( "      limit 1 ) as initial_stock \n" );
            sqlBuffer.append( "  , null  as real_stock \n" );
            
            sqlBuffer.append( "  , 0 as initial_stock2 \n" );            
        }
        sqlBuffer.append( "  from    mst_supplier_item         m_si \n" );
        if( dateRange.getTo() != null ){
            sqlBuffer.append( "  left join \n" );
            sqlBuffer.append( "  ( select  \n" );
            sqlBuffer.append( "      d_id.item_id \n" );
            sqlBuffer.append( "    , d_id.initial_stock \n" );
            sqlBuffer.append( "    , d_id.real_stock \n" );
            sqlBuffer.append( "    from data_inventory_detail d_id \n" );
            sqlBuffer.append( "    , data_inventory d_iv \n" );
            sqlBuffer.append( "    where d_iv.inventory_id = d_id.inventory_id \n" );
            sqlBuffer.append( "    and d_iv.inventory_division = d_id.inventory_division \n" );
            sqlBuffer.append( "    and d_iv.inventory_date = " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
            sqlBuffer.append( "    and d_iv.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n" );
            sqlBuffer.append( "    and d_iv.inventory_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "    and d_id.delete_date is null \n");
            sqlBuffer.append( "    and d_iv.delete_date is null \n");
            sqlBuffer.append( "  ) sd_iv \n" );
            sqlBuffer.append( "  on    sd_iv.item_id = m_si.item_id \n" );
        }
        sqlBuffer.append( "  ,     mst_use_product     m_up \n" );
        sqlBuffer.append( "  where m_up.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n" );
        sqlBuffer.append( "  and   m_up.product_division = 2 \n" );
        sqlBuffer.append( "  and   m_up.product_id = m_si.item_id \n" );
        sqlBuffer.append( "  and \n" );
        if( typeOutputRequirement == StoreShipPanel.DATA_NOTHING ){
            sqlBuffer.append( "  not exists ( \n" );
            sqlBuffer.append( "    select item_id \n" );
            sqlBuffer.append( "    from  data_slip_store_detail sx1 \n" );
            sqlBuffer.append( "    ,     data_slip_store sx2 \n" );
            sqlBuffer.append( "    where sx1.shop_id = sx2.shop_id \n" );
            sqlBuffer.append( "    and   sx1.slip_no = sx2.slip_no \n" );
            sqlBuffer.append( "    and   sx1.item_id = m_si.item_id \n" );
            sqlBuffer.append( "    and   sx2.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n");
            if( dateRange.getFrom() != null ){
                sqlBuffer.append( "    and   sx2.store_date >= " + SQLUtil.convertForSQL(dateRange.getFrom()) + "\n" );
            }
            if( dateRange.getTo() != null ){
                sqlBuffer.append( "    and   sx2.store_date <= " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
            }
            sqlBuffer.append( "    and   sx1.item_use_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "    and   sx1.delete_date is null   \n");
            sqlBuffer.append( "    and   sx2.delete_date is null   \n");
            sqlBuffer.append( "    union \n" );
            sqlBuffer.append( "    select item_id \n" );
            sqlBuffer.append( "    from data_slip_ship_detail sx1 \n" );
            sqlBuffer.append( "    ,    data_slip_ship sx2 \n" );
            sqlBuffer.append( "    where sx1.shop_id = sx2.shop_id \n" );
            sqlBuffer.append( "    and   sx1.slip_no = sx2.slip_no \n" );
            sqlBuffer.append( "    and   sx1.item_id = m_si.item_id \n" );
            sqlBuffer.append( "    and   sx2.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n");
            if( dateRange.getFrom() != null ){
                sqlBuffer.append( "    and   sx2.ship_date >= " + SQLUtil.convertForSQL(dateRange.getFrom()) + "\n" );
            }
            if( dateRange.getTo() != null ){
                sqlBuffer.append( "    and   sx2.ship_date <= " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
            }
            sqlBuffer.append( "    and   sx1.item_use_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "    and   sx1.delete_date is null   \n");
            sqlBuffer.append( "    and   sx2.delete_date is null   \n");
            sqlBuffer.append( "  ) \n" );
            sqlBuffer.append( "    and    \n" );
        }
        sqlBuffer.append( "  m_si.supplier_id = " + SQLUtil.convertForSQL(supplierID) + "\n" );
        sqlBuffer.append( "  and m_si.delete_date is null \n" );
        sqlBuffer.append( ") initial \n" );
        sqlBuffer.append( "left join mst_item m1 \n" );
        sqlBuffer.append( "on  m1.item_id = initial.item_id \n" );
        sqlBuffer.append( "left join mst_item_class m2 \n" );
        sqlBuffer.append( "on m2.item_class_id = m1.item_class_id \n" );
        if( typeOutputRequirement != StoreShipPanel.DATA_NOTHING ){
            if( typeOutputRequirement == StoreShipPanel.DATA_ALL ){
                sqlBuffer.append( "left join \n" );
            }else{
                sqlBuffer.append( "inner join \n" );
            }
            sqlBuffer.append( "( \n" );
            sqlBuffer.append( "  select \n" );
            sqlBuffer.append("    coalesce(super1.item_id, super2.item_id) as item_id \n")
                .append("  , coalesce(super1.inv_date, super2.inv_date) as inv_date \n")
                .append("  , super1.in_num  \n")
                .append("  , super1.attach_num \n")
                .append("  , coalesce(super1.out_num, 0)  + coalesce( super2.out_num, 0)  as out_num\n")
                .append("  from\n")
                .append("   ( select   \n")
                .append("    coalesce(sub1.item_id, sub2.item_id) as item_id \n")
                .append("  , coalesce(sub1.inv_date, sub2.inv_date) as inv_date \n")
                .append("  , in_num \n")
                .append("  , attach_num \n")
                .append("  , out_num\n");
            sqlBuffer.append( "  from \n" );
            sqlBuffer.append( "  ( select dd1.item_id    as item_id \n" );
            sqlBuffer.append("    , date_trunc('day',dd2.store_date) as inv_date \n");
            sqlBuffer.append( "    , sum(dd1.in_num)     as in_num \n" );
            sqlBuffer.append( "    , sum(coalesce(dd1.attach_num, 0)) as attach_num \n" );
            sqlBuffer.append( "    from  data_slip_store_detail dd1 \n" );
            sqlBuffer.append( "    ,     data_slip_store dd2 \n" );
            sqlBuffer.append( "    where dd1.shop_id = dd2.shop_id \n" );
            sqlBuffer.append( "    and   dd1.slip_no = dd2.slip_no \n" );
            sqlBuffer.append( "    and   dd1.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n");
            if( dateRange.getFrom() != null ){
                sqlBuffer.append( "    and   dd2.store_date >= " + SQLUtil.convertForSQL(dateRange.getFrom()) + "\n" );
            }
            if( dateRange.getTo() != null ){
                sqlBuffer.append( "    and   dd2.store_date <= " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
            }
            sqlBuffer.append( "    and   dd1.item_use_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "    and   dd1.delete_date is null   \n");
            sqlBuffer.append( "    and   dd2.delete_date is null   \n");
            sqlBuffer.append( "    group by dd1.item_id \n" );
            sqlBuffer.append( "    , dd2.store_date \n" );
            sqlBuffer.append( "  ) sub1 \n" );
            sqlBuffer.append( "  full outer join \n" );
            sqlBuffer.append( "  (select dd1.item_id     as item_id \n" );
            sqlBuffer.append("    , date_trunc('day',dd2.ship_date) as inv_date \n");
            sqlBuffer.append( "    ,sum(dd1.out_num)     as out_num \n" );
            sqlBuffer.append( "    from data_slip_ship_detail dd1 \n" );
            sqlBuffer.append( "    ,    data_slip_ship dd2 \n" );
            sqlBuffer.append( "    where dd1.shop_id = dd2.shop_id \n" );
            sqlBuffer.append( "    and   dd1.slip_no = dd2.slip_no \n" );
            sqlBuffer.append( "    and   dd1.shop_id = " + SQLUtil.convertForSQL(shopId) + " \n");
            if( dateRange.getFrom() != null ){
                sqlBuffer.append( "    and   dd2.ship_date >= " + SQLUtil.convertForSQL(dateRange.getFrom()) + "\n" );
            }
            if( dateRange.getTo() != null ){
                sqlBuffer.append( "    and   dd2.ship_date <= " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
            }
            sqlBuffer.append( "    and   dd1.item_use_division = " + SQLUtil.convertForSQL(typeInventory) + "\n" );
            sqlBuffer.append( "    and   dd1.delete_date is null   \n");
            sqlBuffer.append( "    and   dd2.delete_date is null   \n");
            sqlBuffer.append( "    group by dd1.item_id \n" );
            sqlBuffer.append( "    , dd2.ship_date \n" );
            sqlBuffer.append( "  ) sub2 \n" );
            sqlBuffer.append( "  on  sub1.item_id  =  sub2.item_id \n" );
            sqlBuffer.append( "  and sub1.inv_date =  sub2.inv_date \n" );
            sqlBuffer.append("  ) super1\n")
                .append("--\n")
                .append("full outer join\n")
                .append("( select \n");
            if(typeInventory == StoreShipPanel.SELL_PROPER){
            sqlBuffer.append("    coalesce(staff.item_id, sale_cnt.item_id) as item_id \n")
                .append("  , coalesce(staff.inv_date, sale_cnt.inv_date) as inv_date \n")
                .append("  , coalesce(sale_cnt.out_num, 0) + coalesce(staff.out_num, 0) as out_num\n")
                .append("  from\n")
                .append(" (select\n")
                .append("   saled.product_id as item_id\n")
                .append(" , date_trunc('day',sale.sales_date) as inv_date\n")
                .append(" , sum(saled.product_num) as out_num\n")
                .append("  from\n")
                .append("    data_sales sale\n")
                .append("  , data_sales_detail saled\n")
                .append("  where \n")
                .append("      sale.shop_id = saled.shop_id\n")
                .append("  and sale.slip_no = saled.slip_no\n")
                .append("  and sale.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n")
                .append("  and saled.product_division = 2 -- 商品\n");
                if( dateRange.getFrom() != null ){
                    sqlBuffer.append( "  and sale.sales_date >= " + SQLUtil.convertForSQL(dateRange.getFrom()) + "\n" );
                }
                if( dateRange.getTo() != null ){
                    sqlBuffer.append( "  and sale.sales_date <= " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
                }
                sqlBuffer.append("  and sale.delete_date is null\n")
                .append("  and saled.delete_date is null\n")
                .append("  group by\n")
                .append("    saled.product_id\n")
                .append("  , sale.sales_date\n")
                .append(") sale_cnt\n")
                .append("--\n")
                .append("full outer join\n")
                .append("(select\n");
            }   
            sqlBuffer.append("    stfd.item_id as item_id\n")
                .append("  , date_trunc('day',stf.sales_date) as inv_date\n")
                .append("  , sum(item_num) as out_num\n")
                .append("  from\n")
                .append("    data_staff_sales stf\n")
                .append("  , data_staff_sales_detail stfd\n")
                .append("  where\n")
                .append("      stf.shop_id = stfd.shop_id\n")
                .append("  and stf.slip_no = stfd.slip_no\n")
                .append("  and stf.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n")
                .append("  and stfd.item_use_division = " + SQLUtil.convertForSQL(typeInventory) + "\n");
                if( dateRange.getFrom() != null ){
                    sqlBuffer.append( "  and stf.sales_date >= " + SQLUtil.convertForSQL(dateRange.getFrom()) + "\n" );
                }
                if( dateRange.getTo() != null ){
                    sqlBuffer.append( "  and stf.sales_date <= " + SQLUtil.convertForSQL(dateRange.getTo()) + "\n" );
                }
                sqlBuffer.append("  and stf.delete_date is null\n")
                .append("  and stfd.delete_date is null\n")
                .append("  group by\n")
                .append("    stfd.item_id\n")
                .append("  , stf.sales_date\n");
                         if(typeInventory == StoreShipPanel.SELL_PROPER){
            sqlBuffer.append(") staff\n")
                .append("on  sale_cnt.item_id  = staff.item_id \n")
                .append("and sale_cnt.inv_date = staff.inv_date\n");
                         }            
            sqlBuffer.append(") super2\n")
                .append("on  super1.item_id  = super2.item_id \n")
                .append("and super1.inv_date = super2.inv_date\n")
                .append("--\n");
            sqlBuffer.append( ") detail \n" );
            sqlBuffer.append( "on detail.item_id = m1.item_id \n" );
        }
        sqlBuffer.append( "order by \n" );
        sqlBuffer.append( "  m2.display_seq \n" );
        sqlBuffer.append( ", m2.item_class_id \n" );
        sqlBuffer.append( ", m1.display_seq \n" );
        sqlBuffer.append( ", m1.item_id \n" );
        
        try {
            Integer itemId = null;
            StoreShipPanelReportBean bean = null;

            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper result = con.executeQuery( sqlBuffer.toString() );
            while( result.next() ) {
                if( itemId == null || itemId != result.getInt( "item_id" ) ){
                    itemId = result.getInt( "item_id" );
                    if( bean != null ){
                        storeShipList.add( bean );
                        bean.calcIncomingNoTotal();
                        bean.calcAdditionalNoTotal();
                        bean.calcOutgoingNoTotal();
                        bean.calcRemainingNo();

						bean.setTermEndNo( bean.getRemainingNoTotal() );            //期末在庫
                    }
                    bean = new StoreShipPanelReportBean();
                    bean.setCategoryName( result.getString( "item_class_name" ) );      //分類
                    bean.setBrandName( result.getString( "item_name" ) );               //商品名
                    bean.setBrandId( result.getInt( "item_id" ) );
                    bean.setCategoryId( result.getInt( "item_class_id" ) );
                    bean.setTermStartNo( result.getInt( "initial_stock" ) );            //期首
//                    bean.setTermEndNo( result.getInt( "real_stock" ) );            //期末在庫

                    if ( typeInventory == StoreShipPanel.SELL_PROPER ){
                        bean.setSuitableNo( result.getInt( "sell_proper_stock" ) );     //適正
                    }else{
                        bean.setSuitableNo( result.getInt( "use_proper_stock" ) );      //適正
                    }
                }
                

                if( typeOutputRequirement != StoreShipPanel.DATA_NOTHING ){
                    Date targetDate = result.getDate("inv_date");
                    if( targetDate != null ){
                        int days;
                        if( dateRange.getFrom() == null ){
                            days = 30 + getDays(dateRange.getTo(), targetDate);
                        }else{
                            days = getDays(dateRange.getFrom(), targetDate);
                        }
                         
                        bean.setIncomingNo(days, result.getInt("in_num"));
                        bean.setAdditionalNo(days, result.getInt("attach_num"));
                        bean.setAllOutgoingNo(days, result.getInt("out_num"));
                    }
                }                
            }
            if( bean != null ){
                storeShipList.add( bean );
                bean.calcIncomingNoTotal();
                bean.calcAdditionalNoTotal();
                bean.calcOutgoingNoTotal();
                bean.calcRemainingNo();

				bean.setTermEndNo( bean.getRemainingNoTotal() );            //期末在庫
            }
            
        } catch( Exception e ) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            throw(new Exception("Data Error."));
        }
        
        return storeShipList;
    }
    
    // 日付間の日数を求める
    private int getDays(Date start, Date end) {
        long lStart = start.getTime();
        long lEnd = end.getTime();
        long result = (lEnd - lStart) / (1000*60*60*24);
        return new Long(result).intValue();
    }
}

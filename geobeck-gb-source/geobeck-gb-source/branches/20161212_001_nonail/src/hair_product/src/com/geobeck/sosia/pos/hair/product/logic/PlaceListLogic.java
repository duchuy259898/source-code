/*
 * PlaceListLogic.java
 *
 * Created on 2008/09/25, 21:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.logic;

import com.geobeck.util.SQLUtil;
import java.util.*;
import java.util.logging.Level;
import java.io.*;
import java.text.*;
import java.sql.SQLException;

// use for JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.product.MstPlace;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.hair.product.beans.PlaceListBean;
import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;

/**
 *
 * @author trino
 */
public class PlaceListLogic extends ReportGeneratorLogic {
    // <editor-fold defaultstate="collapsed" desc="Definitions and Variables">
    private static String REPORT_PLACELIST_JASPER                  = "PlaceList.jasper";
    private static String REPORT_PLACELIST                         = "PlaceList";
    
    private final static String INVENTORY_ALL_STR                     = "全て";
    private final static String INVENTORY_STORE_STR                   = "店販用";
    private final static String INVENTORY_WORK_STR                    = "業務用";
    private final static String OUTPUT_COND_UNIT_STR                  = "在庫あり";
    private final static String OUTPUT_COND_NONE_STR                  = "在庫なし";
    private final static String OUTPUT_COND_ALL_STR                   = "全て";
    
    public final static int INVENTORY_ALL  = 3;
    public final static int INVENTORY_WORK = 2;
    public final static int INVENTORY_STORE = 1;
    
    public final static int OUTPUT_COND_UNIT = 1;
    public final static int OUTPUT_COND_NONE = 2;
    public final static int OUTPUT_COND_ALL  = 3;
    
    private MstShop shop = null;
    private MstSupplier supplier = null;
    private MstPlace place = null;
    private Date toDate  = null;
    private Date fromDate = null;
    private int filetype = 0;
    private int inventoryType = 0;
    private int outputCond  = 0;
    
    // </editor-fold>
    /** Creates a new instance of PlaceListLogic */
    public PlaceListLogic(MstShop mstshop , MstSupplier mstsupplier, MstPlace mstplace,
            Date todate, Date fromdate, int ftype) {
        this.shop = mstshop;
        this.supplier = mstsupplier;
        this.toDate = todate;
        this.fromDate = fromdate;
        this.filetype = ftype;
        this.place = mstplace;
    }
    
    public void setInventory(int type) {
        this.inventoryType = type;
    }
    
    public void setOutputCond(int cond) {
        this.outputCond = cond;
    }
    
    private ArrayList getplacelist() throws Exception {
        
        ArrayList<PlaceListBean> beanlist = new ArrayList<PlaceListBean>();
        PlaceListBean bean = null;
        /** 期首 */
        int initialStock;
        /** 適正在庫数 */
        int properStock;
        /** 入庫数 */
        int inNum;
        /** 出庫数 */
        int outNum;
        /** 在庫数 */
        int finalStock;
        
        try {
            ConnectionWrapper dbConnection = SystemInfo.getConnection();
            ResultSetWrapper rs = dbConnection.executeQuery(this.placeSQL());
            while(rs.next()) {
                bean = new PlaceListBean();
                bean.setLocation(rs.getString("place_name"));
                bean.setCategory(rs.getString("item_class_name"));
                bean.setProductName(rs.getString("item_name"));
                
                bean.setTerm(initialStock = rs.getInt("initial_stock")); // 期首
                bean.setSuited(properStock = rs.getInt("proper_stock")); // 適正
                
                bean.setInCnt(inNum = rs.getInt("in_num")); // 入庫数
                bean.setOutCnt(outNum = rs.getInt("out_num")); // 出庫数
                
                bean.setCurrentCnt(finalStock = initialStock + inNum - outNum); // 在庫数
                bean.setDiffer(finalStock - properStock); // 適正差異
                
                beanlist.add(bean);
            }
            
        } catch( SQLException e) {
            e.printStackTrace();
            throw new Exception("Data Error!");
        }
        
        return beanlist;
    }
    
    public int viewPlaceListReport() {
        
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        
        
        Date date = new Date();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy/MM/dd");
        
        paramMap.put("StoreName", this.shop.getShopName());
        paramMap.put("Location", this.place.getPlaceName());
        
        if( this.fromDate != null ) {
            paramMap.put("ReqDateStart", dateFormat.format(this.fromDate));
        }
        
        if( this.toDate != null ) {
            paramMap.put("ReqDateEnd" , dateFormat.format(this.toDate));
        }
        
        if( this.place.getPlaceID() != null ) {
            paramMap.put("Location", this.place.getPlaceName());
        }else{
            paramMap.put("Location", "指定なし");
        }
        
        if( this.supplier.getSupplierID() != null ) {
            paramMap.put("Suppliers", this.supplier.getSupplierName());
        } else {
            paramMap.put("Suppliers", "指定なし");
        }
        
        switch(this.inventoryType) {
            case INVENTORY_ALL:
                paramMap.put("Display", INVENTORY_ALL_STR);
                break;
            case INVENTORY_STORE:
                paramMap.put("Display", INVENTORY_STORE_STR);
                break;
            case INVENTORY_WORK:
                paramMap.put("Display", INVENTORY_WORK_STR);
                break;
        }
        
        switch(this.outputCond) {
            case OUTPUT_COND_ALL:
                paramMap.put("DisplayCond", OUTPUT_COND_ALL_STR);
                break;
            case OUTPUT_COND_UNIT:
                paramMap.put("DisplayCond",OUTPUT_COND_UNIT_STR);
                break;
            case OUTPUT_COND_NONE:
                paramMap.put("DisplayCond", OUTPUT_COND_NONE_STR);
                break;
        }
        
        ArrayList<PlaceListBean> beanList;
        try {
            beanList = this.getplacelist();
            if( beanList.size() == 0 ){
                return RESULT_DATA_NOTHNIG;
            }
            this.generateFile(beanList, paramMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            return RESULT_ERROR;
        }
        
        return RESULT_SUCCESS;
    }
    
    
    private void generateFile(Collection collection, HashMap<String,Object> paramMap) {
        try {
            
            JasperReport jasperReport = this.loadReport(REPORT_PLACELIST_JASPER, this.REPORT_FILE_TYPE_JASPER);
            JasperPrint  jasperPrint  = JasperFillManager.fillReport(jasperReport, paramMap, new JRBeanCollectionDataSource(collection));
            switch(this.filetype) {
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile(REPORT_PLACELIST_JASPER, jasperPrint);
                    break;
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile(REPORT_PLACELIST, jasperPrint);
                    break;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    // 入庫数、出庫数を取得する
    private String placeSQL() {
        StringBuffer sqlBuf = new StringBuffer();
        
        sqlBuf.append( "select \n" );
        sqlBuf.append( "  base.place_id \n" );
        sqlBuf.append( ", base.pl_seq \n" );
        sqlBuf.append( ", base.place_name \n" );
        sqlBuf.append( ", base.item_class_id \n" );
        sqlBuf.append( ", base.itc_seq \n" );
        sqlBuf.append( ", base.item_class_name \n" );
        sqlBuf.append( ", base.it_seq \n" );
        sqlBuf.append( ", base.item_id \n" );
        sqlBuf.append( ", base.item_name \n" );
        sqlBuf.append( ", coalesce(initial_stock, 0) as initial_stock \n" ); // 期首
        sqlBuf.append( ", coalesce(proper_stock, 0) as proper_stock \n" ); // 適正
        sqlBuf.append( ", coalesce(in_num, 0) as in_num \n" ); // 入庫数
        // 出庫伝票による出庫
        sqlBuf.append( ", coalesce(out_num, 0)\n" );
        // 店販による出庫
        if( inventoryType != INVENTORY_WORK ){
            sqlBuf.append("+(select\n")
            .append("    coalesce(sum(saled.product_num), 0)\n")
            .append("  from data_sales sale\n")
            .append("  , data_sales_detail saled\n")
            .append("  where \n")
            .append("      sale.shop_id = saled.shop_id\n")
            .append("  and sale.slip_no = saled.slip_no\n")
            .append("  and sale.shop_id =  " + SQLUtil.convertForSQL(shop.getShopID()) + "\n")
            .append("  and saled.product_id = base.item_id\n")
            .append("  and saled.product_division = 2 -- 商品\n");
            if( fromDate != null ){
                sqlBuf.append( "  and sale.sales_date >= " + SQLUtil.convertForSQL(fromDate) + " \n" );
            }
            if( toDate != null ){
                sqlBuf.append( "  and sale.sales_date <= " + SQLUtil.convertForSQL(toDate) + " \n" );
            }
            sqlBuf.append("  and sale.delete_date is null\n")
            .append("  and saled.delete_date is null\n")
            .append(")\n");
        }
        //スタッフ販売による出庫
        sqlBuf.append("+(select\n")
        .append("    coalesce(sum(item_num), 0)\n")
        .append("  from\n")
        .append("    data_staff_sales stf\n")
        .append("  , data_staff_sales_detail stfd\n")
        .append("  where\n")
        .append("      stf.shop_id = stfd.shop_id\n")
        .append("  and stf.slip_no = stfd.slip_no\n")
        .append("  and stf.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + "\n")
        .append("  and stfd.item_id = base.item_id\n");
        if( inventoryType != INVENTORY_ALL ){
            sqlBuf.append( "  and stfd.item_use_division = " + SQLUtil.convertForSQL(inventoryType) + " \n" );
        }
        if( fromDate != null ){
            sqlBuf.append( "  and stf.sales_date >= " + SQLUtil.convertForSQL(fromDate) + " \n" );
        }
        if( toDate != null ){
            sqlBuf.append( "  and stf.sales_date <= " + SQLUtil.convertForSQL(toDate) + " \n" );
        }
        sqlBuf.append("  and stf.delete_date is null\n")
        .append("  and stfd.delete_date is null\n")
        .append(")\n");
        sqlBuf.append( "as out_num \n" ); // 出庫数
        sqlBuf.append( "from  \n" );
        sqlBuf.append( "( \n" );
        sqlBuf.append( "  select distinct \n" );
        sqlBuf.append( "    m_pl.place_id \n" );
        sqlBuf.append( "  , m_pl.place_name \n" );
        sqlBuf.append( "  , m_pl.display_seq   as pl_seq  \n" );
        sqlBuf.append( "  , m_itc.item_class_id \n" );
        sqlBuf.append( "  , m_itc.display_seq  as itc_seq \n" );
        sqlBuf.append( "  , m_itc.item_class_name  \n" );
        sqlBuf.append( "  , m_it.item_id \n" );
        sqlBuf.append( "  , m_it.display_seq   as it_seq \n" );
        sqlBuf.append( "  , m_it.item_name \n" );
        if( inventoryType == INVENTORY_STORE ){
            sqlBuf.append( "  , m_up.sell_proper_stock as proper_stock \n" );
        }else if( inventoryType == INVENTORY_WORK ){
            sqlBuf.append( "  , m_up.use_proper_stock as proper_stock \n" );
        }else{
            sqlBuf.append( "  , m_up.use_proper_stock + m_up.sell_proper_stock as proper_stock \n" );
        }
        sqlBuf.append( "  from  mst_supplier_item  m_si \n" );
        sqlBuf.append( "  left join mst_item m_it \n" );
        sqlBuf.append( "  on m_it.item_id = m_si.item_id \n" );
        sqlBuf.append( "  and m_it.delete_date is null \n" );
        sqlBuf.append( "  left join mst_item_class m_itc \n" );
        sqlBuf.append( "  on m_itc.item_class_id = m_it.item_class_id \n" );
        sqlBuf.append( "  and m_itc.delete_date is null \n" );
        sqlBuf.append( "  left join mst_place m_pl \n" );
        sqlBuf.append( "  on m_pl.place_id = m_it.place_id \n" );
        sqlBuf.append( "  and m_pl.delete_date is null \n" );
        sqlBuf.append( "  ,     mst_use_product     m_up \n" );
        sqlBuf.append( "  where m_up.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + " \n" );
        sqlBuf.append( "  and   m_up.product_division = 2 \n" );
        sqlBuf.append( "  and   m_up.product_id = m_si.item_id \n" );
        if( supplier.getSupplierID() != null ){
            sqlBuf.append( "  and   m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + " \n" );
            sqlBuf.append( "  and   m_si.delete_date is null \n" );
        }else{
            sqlBuf.append( "  and   m_si.delete_date is null \n" );
        }
        sqlBuf.append( ") base \n" );
        if( toDate != null ){
            sqlBuf.append( "left join  \n" );
            sqlBuf.append( "(  select \n" );
            sqlBuf.append( "    m_it.item_class_id \n" );
            sqlBuf.append( "  , m_it.item_id \n" );
            sqlBuf.append( "  , sum(d_ivd.initial_stock) as initial_stock \n" );
            sqlBuf.append( "  from data_inventory d_iv \n" );
            sqlBuf.append( "  ,    data_inventory_detail d_ivd \n" );
            if( supplier.getSupplierID() != null ){
                sqlBuf.append( "  inner join mst_supplier_item m_si \n" );
                sqlBuf.append( "  on  m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + " \n" );
                sqlBuf.append( "  and m_si.item_id = d_ivd.item_id \n" );
                sqlBuf.append( "  and m_si.delete_date is null \n" );
                sqlBuf.append( "  left join mst_item m_it \n" );
                sqlBuf.append( "  on  m_it.item_id = m_si.item_id \n" );
                sqlBuf.append( "  and m_it.delete_date is null \n" );
            }else{
                sqlBuf.append( "  left join mst_item m_it \n" );
                sqlBuf.append( "  on  m_it.item_id = d_ivd.item_id \n" );
                sqlBuf.append( "  and m_it.delete_date is null \n" );
            }
            sqlBuf.append( "  where d_iv.inventory_id = d_ivd.inventory_id \n" );
            sqlBuf.append( "  and   d_iv.inventory_division = d_ivd.inventory_division \n" );
            sqlBuf.append( "  and   d_iv.inventory_date = " + SQLUtil.convertForSQL(toDate) + " \n" );
            sqlBuf.append( "  and d_ivd.delete_date is null \n" );
            sqlBuf.append( "  and d_iv.delete_date is null \n" );
            if( inventoryType != INVENTORY_ALL ){
                sqlBuf.append( "  and d_ivd.inventory_division = " + SQLUtil.convertForSQL(inventoryType) + " \n" );
            }
            sqlBuf.append( "  and d_iv.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + " \n" ); // 店舗ID
            sqlBuf.append( "  group by m_it.item_class_id, m_it.item_id \n" );
            sqlBuf.append( ")  s_iv \n" );
            sqlBuf.append( "on  s_iv.item_class_id = base.item_class_id \n" );
            sqlBuf.append( "and s_iv.item_id = base.item_id \n" );
        }else{
            sqlBuf.append( "left join  \n" );
            sqlBuf.append( "( select \n" );
            sqlBuf.append( "    m_it.item_class_id \n" );
            sqlBuf.append( "  , m_it.item_id \n" );
            sqlBuf.append( "  , sum(d_ivd.initial_stock) as initial_stock \n" );
            sqlBuf.append( "  from data_inventory d_iv \n" );
            sqlBuf.append( "  ,    data_inventory_detail d_ivd \n" );
            if( supplier.getSupplierID() != null ){
                sqlBuf.append( "  inner join mst_supplier_item m_si \n" );
                sqlBuf.append( "  on  m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + " \n" );
                sqlBuf.append( "  and m_si.item_id = d_ivd.item_id \n" );
                sqlBuf.append( "  and m_si.delete_date is null \n" );
                sqlBuf.append( "  left join mst_item m_it \n" );
                sqlBuf.append( "  on  m_it.item_id = m_si.item_id \n" );
                sqlBuf.append( "  and m_it.delete_date is null \n" );
            }else{
                sqlBuf.append( "  left join mst_item m_it \n" );
                sqlBuf.append( "  on  m_it.item_id = d_ivd.item_id \n" );
                sqlBuf.append( "  and m_it.delete_date is null \n" );
            }
            sqlBuf.append( "  where d_iv.inventory_id = d_ivd.inventory_id \n" );
            sqlBuf.append( "  and   d_iv.inventory_division = d_ivd.inventory_division \n" );
            sqlBuf.append( "  and   d_iv.inventory_id = " );
            sqlBuf.append( "  ( " );
            sqlBuf.append( "    select inventory_id" );
            sqlBuf.append( "    from   data_inventory" );
            sqlBuf.append( "    where  inventory_date < " + SQLUtil.convertForSQL(toDate) + " \n" );
            sqlBuf.append( "    order by inventory_date desc " );
            sqlBuf.append( "    limit 1 " );
            sqlBuf.append( "  ) " );
            sqlBuf.append( "  and d_iv.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + " \n" );
            sqlBuf.append( "  and d_ivd.delete_date is null \n" );
            sqlBuf.append( "  and d_iv.delete_date is null \n" );
            if( inventoryType != INVENTORY_ALL ){
                sqlBuf.append( "  and d_ivd.inventory_division = " + SQLUtil.convertForSQL(inventoryType) + " \n" );
            }
            sqlBuf.append( "  group by m_it.item_class_id, m_it.item_id \n" );
            sqlBuf.append( ")  s_iv \n" );
            sqlBuf.append( "on  s_iv.item_class_id = base.item_class_id \n" );
            sqlBuf.append( "and s_iv.item_id = base.item_id \n" );
            
        }
        sqlBuf.append( "left join \n" );
        sqlBuf.append( "( select \n" );
        sqlBuf.append( "    m_it.item_class_id \n" );
        sqlBuf.append( "  , m_it.item_id \n" );
        sqlBuf.append( "  , sum(d_ssd.in_num + coalesce(d_ssd.attach_num, 0)) as in_num \n" );
        sqlBuf.append( "  from data_slip_store d_ss \n" );
        sqlBuf.append( "  ,    data_slip_store_detail d_ssd \n" );
        if( supplier.getSupplierID() != null ){
            sqlBuf.append( "  inner join mst_supplier_item m_si \n" );
            sqlBuf.append( "  on  m_si.item_id = d_ssd.item_id \n" );
            sqlBuf.append( "  and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + " \n" );
            sqlBuf.append( "  and m_si.delete_date is null \n" );
            sqlBuf.append( "  left join mst_item m_it \n" );
            sqlBuf.append( "  on  m_it.item_id = m_si.item_id \n" );
        }else{
            sqlBuf.append( "  left join mst_item m_it \n" );
            sqlBuf.append( "  on  m_it.item_id = d_ssd.item_id \n" );
        }
        sqlBuf.append( "  and m_it.delete_date is null \n" );
        sqlBuf.append( "  where d_ss.shop_id = d_ssd.shop_id \n" );
        sqlBuf.append( "  and d_ss.slip_no = d_ssd.slip_no \n" );
        sqlBuf.append( "  and d_ss.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + " \n" );
        if( fromDate != null ){
            sqlBuf.append( "  and   d_ss.store_date >= " + SQLUtil.convertForSQL(fromDate) + " \n" );
        }
        if( toDate != null ){
            sqlBuf.append( "  and   d_ss.store_date <= " + SQLUtil.convertForSQL(toDate) + " \n" );
        }
        sqlBuf.append( "  and d_ss.delete_date is null \n" );
        sqlBuf.append( "  and d_ssd.delete_date is null \n" );
        if( inventoryType != INVENTORY_ALL ){
            sqlBuf.append( "  and d_ssd.item_use_division = " + SQLUtil.convertForSQL(inventoryType) + " \n" );
        }
        sqlBuf.append( "  group by m_it.item_class_id, m_it.item_id \n" );
        sqlBuf.append( ")  s_store \n" );
        sqlBuf.append( "on  s_store.item_class_id = base.item_class_id \n" );
        sqlBuf.append( "and s_store.item_id = base.item_id  \n" );
        sqlBuf.append( "left join \n" );
        sqlBuf.append( "( select \n" );
        sqlBuf.append( "    m_it.item_class_id \n" );
        sqlBuf.append( "  , m_it.item_id \n" );
        sqlBuf.append( "  , sum(d_ssd.out_num) as out_num \n" );
        sqlBuf.append( "  from data_slip_ship d_ss \n" );
        sqlBuf.append( "  ,    data_slip_ship_detail d_ssd \n" );
        if( supplier.getSupplierID() != null ){
            sqlBuf.append( "  inner join mst_supplier_item m_si \n" );
            sqlBuf.append( "  on  m_si.item_id = d_ssd.item_id \n" );
            sqlBuf.append( "  and m_si.supplier_id = " + SQLUtil.convertForSQL(supplier.getSupplierID()) + " \n" );
            sqlBuf.append( "  and m_si.delete_date is null \n" );
            sqlBuf.append( "  left join mst_item m_it \n" );
            sqlBuf.append( "  on  m_it.item_id = m_si.item_id \n" );
        }else{
            sqlBuf.append( "  left join mst_item m_it \n" );
            sqlBuf.append( "  on  m_it.item_id = d_ssd.item_id \n" );
        }
        sqlBuf.append( "  and m_it.delete_date is null \n" );
        sqlBuf.append( "  where d_ss.shop_id = d_ssd.shop_id \n" );
        sqlBuf.append( "  and d_ss.slip_no = d_ssd.slip_no \n" );
        sqlBuf.append( "  and d_ss.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + " \n" );
        if( fromDate != null ){
            sqlBuf.append( "  and   d_ss.ship_date >= " + SQLUtil.convertForSQL(fromDate) + " \n" );
        }
        if( toDate != null ){
            sqlBuf.append( "  and   d_ss.ship_date <= " + SQLUtil.convertForSQL(toDate) + " \n" );
        }
        if( inventoryType != INVENTORY_ALL ){
            sqlBuf.append( "  and d_ssd.item_use_division = " + SQLUtil.convertForSQL(inventoryType) + " \n" );
        }
        sqlBuf.append( "  and d_ss.delete_date is null \n" );
        sqlBuf.append( "  and d_ssd.delete_date is null \n" );
        sqlBuf.append( "  group by m_it.item_class_id, m_it.item_id \n" );
        sqlBuf.append( "  order by m_it.item_class_id, m_it.item_id \n" );
        sqlBuf.append( ")  s_slip \n" );
        sqlBuf.append( "on  s_slip.item_class_id = base.item_class_id \n" );
        sqlBuf.append( "and s_slip.item_id = base.item_id  \n" );
        if( outputCond == OUTPUT_COND_UNIT ){
            sqlBuf.append( "where coalesce(initial_stock, 0) + coalesce(in_num, 0) - coalesce(out_num, 0) != 0 \n" );
        }else if( outputCond == OUTPUT_COND_NONE ){
            sqlBuf.append( "where coalesce(initial_stock, 0) + coalesce(in_num, 0) - coalesce(out_num, 0) = 0 \n" );
        }else{
            sqlBuf.append( "where 1=1 \n" );
        }
        if( place.getPlaceID() != null ){
            sqlBuf.append( "  and base.place_id = " + SQLUtil.convertForSQL(place.getPlaceID()) + " \n" );
        }
        sqlBuf.append( "order by \n" );
        sqlBuf.append( "  base.pl_seq, base.place_id \n" );
        sqlBuf.append( ", base.itc_seq, base.item_class_id \n" );
        sqlBuf.append( ", base.it_seq, base.item_id \n" );
        
        return sqlBuf.toString();
    }
}

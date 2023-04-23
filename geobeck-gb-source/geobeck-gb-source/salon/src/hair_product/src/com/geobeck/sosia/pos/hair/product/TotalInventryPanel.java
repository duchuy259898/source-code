/*
 * TotalInventryPanel.java
 *
 * Created on 2008/09/16, 15:41
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.product.logic.TotalInventoryReportLogic;
import com.geobeck.sosia.pos.hair.report.util.JPOIApiSaleTransittion;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.SQLUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.rmi.CORBA.Util;
import javax.swing.JOptionPane;
import jxl.CellReferenceHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 *
 * @author  s_matsumura
 */
public class TotalInventryPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private PrintInventry	ia	=	new PrintInventry();
    private InventryPeriod	ib	=	new InventryPeriod();
    
    /** Creates new form TotalInventryPanel */
    public TotalInventryPanel() {
        super();
        initComponents();
        
        ftp	= new LocalFocusTraversalPolicy();
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        
        addMouseCursorChange();
        this.setSize(500, 170);
        this.setPath("帳票");
        this.setTitle("合計棚卸表");
        SystemInfo.initGroupShopComponents(shop, 2);
        this.setKeyListener();
        
        this.init();
        
        //税抜、税込の初期設定
        if (SystemInfo.getAccountSetting().getReportPriceType() == 0) {
            rdoTaxBlank.setSelected(false);
            rdoTaxUnit.setSelected(true);
        } else {
            rdoTaxBlank.setSelected(true);
            rdoTaxUnit.setSelected(false);
        }
        // Thanh start add 2014/09/04
        if (SystemInfo.getDatabase().startsWith("pos_hair_mashu")) {
            btnOutputPdf.setVisible(false);
            // IVS_LVTu start edit 2014/09/29 MASHU_棚卸表
            lblTax.setVisible(false);
            rdoTaxBlank.setVisible(false);
            rdoTaxUnit.setVisible(false);
        }
        else
        {
            btnOutputPdf.setVisible(true);
            lblTax.setVisible(true);
            rdoTaxBlank.setVisible(true);
            rdoTaxUnit.setVisible(true);
            // IVS_LVTu end edit 2014/09/29 MASHU_棚卸表
        }
        // Thanh end add 2014/09/04
    }
    
    private boolean checkInputs() {
        if( this.inventoryPeriod.getSelectedIndex() < 0 ) {
            MessageDialog.showMessageDialog( this, 
                    MessageUtil.getMessage(12005),
                    this.getTitle(),JOptionPane.ERROR_MESSAGE );
            return false;
        }
        
        return true;
    }
    
    protected void callReportGeneratorLogic( int reportFileType ) {
        if( this.checkInputs() == false ) {
            return;
        }
        
        MstShop shopInfo = null;
        boolean reportGenerated = false;
        int taxType = 0;
        
        if(this.rdoTaxBlank.isSelected()) {
            // 税抜き
            taxType = ReportParameterBean.TAX_TYPE_BLANK;
        } else if( this.rdoTaxUnit.isSelected( )) {
            //税込み
            taxType = ReportParameterBean.TAX_TYPE_UNIT;
        }
        
        if(shop.getSelectedItem() instanceof MstShop) {
            shopInfo = (MstShop)shop.getSelectedItem();
        } else {
            shopInfo = SystemInfo.getCurrentShop();
        }
        // Thanh start add 2014/09/04
        // IVS_LVTu start edit 2014/09/29 MASHU_棚卸表
        //if (SystemInfo.getDatabase().equals("pos_hair_mashu") || SystemInfo.getDatabase().equals("pos_hair_mashu_dev") || SystemInfo.getDatabase().equals("pos_hair_mashu_it") || SystemInfo.getDatabase().equals("pos_hair_mashu_it1") || SystemInfo.getDatabase().equals("pos_hair_mashu_it2") || SystemInfo.getDatabase().equals("pos_hair_mashu_it3")) {            
        if (SystemInfo.getDatabase().startsWith("pos_hair_mashu")) {
        // IVS_LVTu start edit 2014/09/29 MASHU_棚卸表
            String sql1 = "SELECT item_class_id,item_class_name from mst_item_class"
                    + " where delete_date is null order by display_seq  ";
            String sql2 = "SELECT supplier_id,supplier_name from mst_supplier"
                    + " where delete_date is null order by supplier_id  ";
            try{
                 ConnectionWrapper cw = SystemInfo.getConnection();
                 
                 
                 ResultSetWrapper rs1 = cw.executeQuery(sql1);
                 
                 ResultSetWrapper rs2 = cw.executeQuery(sql2);
                 
                 // IVS_LVTu start add 2014/09/09 MASHU_棚卸表
                 
                 ResultSetWrapper rs4 = cw.executeQuery(getItemClassByShopIdByDate(SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo()) ,SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom()),shopInfo.getShopID()));
                 
                 ResultSetWrapper rs5 = cw.executeQuery(getDataSaleUseSQL(SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo()) ,SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom()),shopInfo.getShopID(), 1));
                 
                 ResultSetWrapper rs6 = cw.executeQuery(getDataSaleUseSQL(SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo()) ,SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom()),shopInfo.getShopID(), 2));
                 
                 ResultSetWrapper rs7 = cw.executeQuery(getProductConsumSQL(SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo()) ,SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom()),shopInfo.getShopID(), 1));
                 
                 ResultSetWrapper rs8 = cw.executeQuery(getProductConsumSQL(SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo()) ,SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom()),shopInfo.getShopID(), 2));
                 //Luc start edit 20150629  #38264                
                 //JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion("棚卸表_MASHU");
                 //jx.setTemplateFile("/reports/棚卸表_MASHU.xls");
                 JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion("合計棚卸表_MASHU");
                 jx.setTemplateFile("/reports/合計棚卸表_MASHU.xls");
                 //Luc end edit 20150629 #38264
                 jx.setCellValue(3, 3, shopInfo.getShopName());
                 jx.setCellValue(3, 4, ((DateRange)inventoryPeriod.getSelectedItem()).toString());
                 
                 rs4.last();
                 rs1.last();
                 rs2.last();
                 
                 int totalRowRs4 = rs4.getRow();
                 int totalRowRs1 = rs1.getRow();
                 int totalColumn2 = rs2.getRow() + 2;
                 
                 rs4.beforeFirst();
                 rs1.beforeFirst();
                 rs2.beforeFirst();
                 int column = 3;
                 int row = 8;
                 
                 while(rs4.next()) {
                     if (row > 8 && row < totalRowRs4 + 7) {
                             jx.copyRow(7, row - 1);
                         }
                    jx.setCellValue(2, row, rs4.getString("item_class_name"));
                    jx.setCellValue(3, row, rs4.getInt("total_over_stock"));
                    jx.setCellValue(4, row, rs4.getInt("total_in_num"));
                    // IVS_LVTu start edit 2015/09/24 New request #42137
                    //jx.setCellValue(5, row, rs4.getInt("total_in_cost_no_tax"));
                    jx.setCellValue(5, row, rs4.getDouble("total_in_cost_no_tax"));
                    jx.setCellValue(6, row, rs4.getInt("total_attach_num"));
                    jx.setCellValue(7, row, rs4.getInt("sales_num"));
                    jx.setCellValue(8, row, rs4.getInt("sales_money"));
                    jx.setCellValue(9, row, rs4.getInt("total_move_num"));
                    jx.setCellValue(10, row, rs4.getInt("total_move_cost_no_tax"));
                    jx.setCellValue(11, row, rs4.getInt("total_out_num_12"));
                    jx.setCellValue(12, row, rs4.getInt("total_out_cost_no_tax_12"));
                    jx.setCellValue(13, row, rs4.getInt("total_real_stock_num"));
                    jx.setCellValue(14, row, rs4.getInt("total_real_stock_cost_no_tax"));
                    jx.setCellValue(15, row, rs4.getInt("accumulative_num"));
                    // edit 20160614 #50881-start
                    //jx.setCellValue(16, row, rs4.getDouble("accumulative_amount"));
                    jx.setCellValue(16, row, rs4.getDouble("accumulative_cost_no_tax"));
                    // edit 20160614 #50881-end
                    
                    row ++;
                 }
                 
                 if( rs5.next() ) {
                    jx.setCellValue(3, row + 1, rs5.getInt("total_real_stock"));
                    jx.setCellValue(4, row + 1, rs5.getInt("total_in_num"));
                    //jx.setCellValue(5, row + 1, rs5.getInt("total_in_cost_no_tax"));
                    jx.setCellValue(5, row + 1, rs5.getDouble("total_in_cost_no_tax"));
                    jx.setCellValue(6, row + 1, rs5.getInt("total_attach_num"));
                    jx.setCellValue(7, row + 1, rs5.getInt("sales_num"));
                    jx.setCellValue(8, row + 1, rs5.getInt("sales_money"));
                    jx.setCellValue(9, row + 1, rs5.getInt("total_move_num"));
                    jx.setCellValue(10, row + 1, rs5.getInt("total_move_cost_no_tax"));
                    jx.setCellValue(11, row + 1, rs5.getInt("total_out_num_12"));
                    jx.setCellValue(12, row + 1, rs5.getInt("total_out_cost_no_tax_12"));
                    jx.setCellValue(13, row + 1, rs5.getInt("total_real_stock_num"));
                    jx.setCellValue(14, row + 1, rs5.getInt("total_real_stock_cost_no_tax"));
                    jx.setCellValue(15, row + 1, rs5.getInt("accumulative_num"));
                    jx.setCellValue(16, row + 1, rs5.getInt("accumulative_amount"));
                 }
                 if( rs6.next() ) {
                    jx.setCellValue(3, row + 2, rs6.getInt("total_real_stock"));
                    jx.setCellValue(4, row + 2, rs6.getInt("total_in_num"));
                    //jx.setCellValue(5, row + 2, rs6.getInt("total_in_cost_no_tax"));
                    jx.setCellValue(5, row + 2, rs6.getDouble("total_in_cost_no_tax"));
                    jx.setCellValue(6, row + 2, rs6.getInt("total_attach_num"));
                    jx.setCellValue(7, row + 2, rs6.getInt("sales_num"));
                    jx.setCellValue(8, row + 2, rs6.getInt("sales_money"));
                    jx.setCellValue(9, row + 2, rs6.getInt("total_move_num"));
                    jx.setCellValue(10, row + 2, rs6.getInt("total_move_cost_no_tax"));
                    jx.setCellValue(11, row + 2, rs6.getInt("total_out_num_12"));
                    jx.setCellValue(12, row + 2, rs6.getInt("total_out_cost_no_tax_12"));
                    jx.setCellValue(13, row + 2, rs6.getInt("total_real_stock_num"));
                    jx.setCellValue(14, row + 2, rs6.getInt("total_real_stock_cost_no_tax"));
                    jx.setCellValue(15, row + 2, rs6.getInt("accumulative_num"));
                    jx.setCellValue(16, row + 2, rs6.getInt("accumulative_amount"));
                 }
                 if( rs7.next() ) {
                    jx.setCellValue(3, row + 3, rs7.getInt("total_real_stock"));
                    jx.setCellValue(4, row + 3, rs7.getInt("total_in_num"));
                    //jx.setCellValue(5, row + 3, rs7.getInt("total_in_cost_no_tax"));
                    jx.setCellValue(5, row + 3, rs7.getDouble("total_in_cost_no_tax"));
                    jx.setCellValue(6, row + 3, rs7.getInt("total_attach_num"));
                    jx.setCellValue(7, row + 3, rs7.getInt("sales_num"));
                    jx.setCellValue(8, row + 3, rs7.getInt("sales_money"));
                    jx.setCellValue(9, row + 3, rs7.getInt("total_move_num"));
                    jx.setCellValue(10, row +3, rs7.getInt("total_move_cost_no_tax"));
                    jx.setCellValue(11, row + 3, rs7.getInt("total_out_num_12"));
                    jx.setCellValue(12, row + 3, rs7.getInt("total_out_cost_no_tax_12"));
                    jx.setCellValue(13, row + 3, rs7.getInt("total_real_stock_num"));
                    jx.setCellValue(14, row + 3, rs7.getInt("total_real_stock_cost_no_tax"));
                    jx.setCellValue(15, row + 3, rs7.getInt("accumulative_num"));
                    jx.setCellValue(16, row + 3, rs7.getInt("accumulative_amount"));
                 }
                 if( rs8.next() ) {
                    jx.setCellValue(3, row + 4, rs8.getInt("total_real_stock"));
                    jx.setCellValue(4, row + 4, rs8.getInt("total_in_num"));
                    //jx.setCellValue(5, row + 4, rs8.getInt("total_in_cost_no_tax"));
                    jx.setCellValue(5, row + 4, rs8.getDouble("total_in_cost_no_tax"));
                    // IVS_LVTu end edit 2015/09/24 New request #42137
                    jx.setCellValue(6, row + 4, rs8.getInt("total_attach_num"));
                    jx.setCellValue(7, row + 4, rs8.getInt("sales_num"));
                    jx.setCellValue(8, row + 4, rs8.getInt("sales_money"));
                    jx.setCellValue(9, row + 4, rs8.getInt("total_move_num"));
                    jx.setCellValue(10, row +4, rs8.getInt("total_move_cost_no_tax"));
                    jx.setCellValue(11, row + 4, rs8.getInt("total_out_num_12"));
                    jx.setCellValue(12, row + 4, rs8.getInt("total_out_cost_no_tax_12"));
                    jx.setCellValue(13, row + 4, rs8.getInt("total_real_stock_num"));
                    jx.setCellValue(14, row + 4, rs8.getInt("total_real_stock_cost_no_tax"));
                    jx.setCellValue(15, row + 4, rs8.getInt("accumulative_num"));
                    jx.setCellValue(16, row + 4, rs8.getInt("accumulative_amount"));
                 }
                // IVS_LVTu end add 2014/09/09 MASHU_棚卸表
                 if (rs2.next()) {
                      // IVS_LVTu start edit 2014/09/10 MASHU_棚卸表
                      //JPOIApiSaleTransittion jx = new JPOIApiSaleTransittion("test");
                      //jx.setTemplateFile("/reports/test.xls");
                      rs2.beforeFirst();
                      //int column = 3;
                      //int row = 3;
                      row = row + 8; 
                      int templateRow = row;

                      while (rs2.next()) {
                         //jx.setCellValue(column, 3, rs2.getString("supplier_name"));
                         jx.setCellValue(column, templateRow, rs2.getString("supplier_name"));
                         column += 1;                         
                         //jx.copyCell(jx.getCell(3, 3), jx.getCell(column, 3));
                         //jx.copyCell(jx.getCell(3, 4), jx.getCell(column, 4));
                         //jx.copyCell(jx.getCell(3, 5), jx.getCell(column, 5));
                         if ( column <= totalColumn2 ) {
                            jx.copyCell(jx.getCell(3, templateRow ), jx.getCell(column, templateRow));
                            jx.copyCell(jx.getCell(3, templateRow + 1), jx.getCell(column, templateRow + 1));
                            jx.copyCell(jx.getCell(3, templateRow + 2), jx.getCell(column, templateRow + 2));
                         }
                     }
                     rs2.beforeFirst();
                     //row = 4;
                     row = row + 1;
                     while (rs1.next()) {
                         //if (row > 4) {
                         if (row > (templateRow + 1) && row < totalRowRs1 + templateRow) {
                             //jx.copyRow(3, row - 1);
                             jx.copyRow(templateRow, row - 1);
                     // IVS_LVTu end edit 2014/09/10 MASHU_棚卸表
                         }
                         jx.setCellValue(2, row, rs1.getString("item_class_name"));
                         column = 3;
                         rs2.beforeFirst();
                         while (rs2.next()) {
                             
                             String sql = "SELECT m_si.supplier_id ,supplier_name,item_class_name,  "
                            //IVS_LVTu start edit 2015/09/10 Bug #42528
                            + " sum(sign(coalesce(dssd.in_num,0) * coalesce(dssd.cost_price,0) / (1.0 + get_tax_rate(dss.store_date))) * (abs(coalesce(dssd.in_num,0) * ceil(coalesce(dssd.cost_price,0) / (1.0 + get_tax_rate(dss.store_date)))))) AS real_cost_no_tax "
                                     //IVS_LVTu end edit 2015/09/10 Bug #42528
                            + " FROM data_slip_store dss "
                            + " INNER JOIN data_slip_store_detail dssd using(shop_id,slip_no) "
                            + " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dssd.item_id"
                            + " AND m_si.delete_date IS NULL"
                            + " INNER JOIN mst_supplier msi ON dss.supplier_id = msi.supplier_id and m_si.supplier_id = msi.supplier_id  "
                            + " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id"
                            + " AND m_it.delete_date IS NULL"
                            + " inner join mst_item_class mic on m_it.item_class_id = mic.item_class_id"
                            + " WHERE "
                            + "  dss.shop_id = " + shopInfo.getShopID();
                           
                           if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
                                sql  += "  AND dss.store_date >= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom());
                            }
                            sql  += "  AND dss.store_date <= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo())       
                            + "  AND dssd.delete_date IS NULL"
                            + "  AND dss.delete_date IS NULL"
                            + "  AND m_si.supplier_id = "   + rs2.getInt("supplier_id")    
                            + "  AND  mic.item_class_id = "   + rs1.getInt("item_class_id")     
                            //IVS_LVTu start add New request #42532
                            + "  AND dss.ship_slip_no is null "
                            //IVS_LVTu end add New request #42532
                            + " GROUP BY m_si.supplier_id ,supplier_name,"
                            + "         item_class_name"
                            + " order by m_si.supplier_id ,item_class_name";
                            ResultSetWrapper rs = cw.executeQuery(sql);
                             if (rs.next()) {
                                 jx.setCellValue(column, row, rs.getDouble("real_cost_no_tax"));
                             }
                             column +=1;
                         }
                         row += 1;
                     }
                      
                    rs2.beforeFirst();  
                    column = 3;
                    row = row - 1;
                    while (rs2.next())
                    {
                        if ( column < totalColumn2 ) {
                            jx.copyCell(jx.getCell(3, row + 2), jx.getCell(column + 1, row + 2));
                            jx.copyCell(jx.getCell(3, row + 3), jx.getCell(column + 1, row + 3));
                            jx.copyCell(jx.getCell(3, row + 4), jx.getCell(column + 1, row + 4));
                            jx.copyCell(jx.getCell(3, row + 5), jx.getCell(column + 1, row + 5));
                            jx.copyCell(jx.getCell(3, row + 7), jx.getCell(column + 1, row + 7));
                            // IVS_LVTu start add 2014/09/09 MASHU_棚卸表
                            String str ="SUM(" + CellReferenceHelper.getCellReference(column, row + 1);
                            str = str + ":" + CellReferenceHelper.getCellReference(column,  row + 4)+")";
                            HSSFCell cellNew = jx.getCell(column + 1, row + 7);
                            cellNew.setCellFormula(str);
                            // IVS_LVTu end add 2014/09/09 MASHU_棚卸表
                        }
                        
                            String sql = "SELECT m_si.supplier_id ,supplier_name,dssd.item_use_division,  "
                            + " sum(coalesce(dssd.in_num,0) * coalesce(dssd.cost_price,0) ) AS real_cost_no_tax"
                            + " from data_slip_store dss "
                            + " inner join data_slip_store_detail dssd using(shop_id,slip_no) "
                            + " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dssd.item_id"
                            + " AND m_si.delete_date IS NULL"
                            + " INNER JOIN mst_supplier msi ON dss.supplier_id = msi.supplier_id and m_si.supplier_id = msi.supplier_id   "
                            + " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id"
                            + " AND m_it.delete_date IS NULL"
                            + " inner join mst_item_class mic on m_it.item_class_id = mic.item_class_id"
                            + " WHERE "
                            + "  dss.shop_id = " + shopInfo.getShopID();
                            if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
                              sql  += "  AND dss.store_date >= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom());
                            }
                            sql  += "  AND dss.store_date <= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo())       
                            + "  AND dssd.delete_date IS NULL"
                            + "  AND dss.delete_date IS NULL"   
                            + "  and dssd.item_use_division = 1"          
                            + "  AND m_si.supplier_id = "   + rs2.getInt("supplier_id")           
                            + "  AND mic.item_class_id not in (9,61)"      
                            //IVS_LVTu start add New request #42532
                            + "  AND dss.ship_slip_no is null "
                            //IVS_LVTu end add New request #42532
                            + " GROUP BY m_si.supplier_id ,supplier_name,"
                            + "         dssd.item_use_division"
                            + " Union All"         
                            + " SELECT m_si.supplier_id ,supplier_name,dssd.item_use_division, "
                           
                            + " sum(coalesce(dssd.in_num,0) * coalesce(dssd.cost_price,0) ) AS real_cost_no_tax"
                            + " from data_slip_store dss "
                            + " inner join data_slip_store_detail dssd using(shop_id,slip_no) "
                            + " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dssd.item_id"
                            + " AND m_si.delete_date IS NULL"
                            + " INNER JOIN mst_supplier msi ON dss.supplier_id = msi.supplier_id and m_si.supplier_id = msi.supplier_id   "
                            + " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id"
                            + " AND m_it.delete_date IS NULL"
                            + " inner join mst_item_class mic on m_it.item_class_id = mic.item_class_id"
                            + " WHERE "
                            + "  dss.shop_id = " + shopInfo.getShopID();
                            if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
                                sql  += "  AND dss.store_date >= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom());
                            }
                            sql  += "  AND dss.store_date <= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo())       
                            + "  AND dssd.delete_date IS NULL"
                            + "  AND dss.delete_date IS NULL"   
                            + "  and dssd.item_use_division = 2"          
                            + "  AND m_si.supplier_id = "   + rs2.getInt("supplier_id")           
                            + "  AND mic.item_class_id not in (9,61)" 
                            //IVS_LVTu start add New request #42532
                            + "  AND dss.ship_slip_no is null "
                            //IVS_LVTu end add New request #42532
                            + " GROUP BY m_si.supplier_id ,supplier_name,"
                            + "         dssd.item_use_division" 
                            + " Union All"         
                            + " SELECT m_si.supplier_id ,supplier_name,3 as item_use_division, "
                            
                            + " sum(coalesce(dssd.in_num,0) * coalesce(dssd.cost_price,0) ) AS real_cost_no_tax"
                            + " from data_slip_store dss "
                            + " inner join data_slip_store_detail dssd using(shop_id,slip_no) "
                            + " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dssd.item_id"
                            + " AND m_si.delete_date IS NULL"
                            + " INNER JOIN mst_supplier msi ON dss.supplier_id = msi.supplier_id and m_si.supplier_id = msi.supplier_id   "
                            + " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id"
                            + " AND m_it.delete_date IS NULL"
                            + " inner join mst_item_class mic on m_it.item_class_id = mic.item_class_id"
                            + " WHERE "
                            + "  dss.shop_id = " + shopInfo.getShopID();
                            if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
                                sql  += "  AND dss.store_date >= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom());
                            }
                            sql  += "  AND dss.store_date <= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo())       
                            + "  AND dssd.delete_date IS NULL"
                            + "  AND dss.delete_date IS NULL" 
                            + "  AND m_si.supplier_id = "   + rs2.getInt("supplier_id")           
                            + "  AND mic.item_class_id in (9)"    
                            //IVS_LVTu start add New request #42532
                            + "  AND dss.ship_slip_no is null "
                            //IVS_LVTu end add New request #42532
                            + " GROUP BY m_si.supplier_id ,supplier_name"
                            + " Union All"         
                            + " SELECT m_si.supplier_id ,supplier_name,4 as item_use_division, "
                            
                            + "sum(coalesce(dssd.in_num,0) * coalesce(dssd.cost_price,0) ) AS real_cost_no_tax"
                            + " from data_slip_store dss "
                            + " inner join data_slip_store_detail dssd using(shop_id,slip_no) "
                            + " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dssd.item_id"
                            + " AND m_si.delete_date IS NULL"
                            + " INNER JOIN mst_supplier msi ON dss.supplier_id = msi.supplier_id and m_si.supplier_id = msi.supplier_id   "
                            + " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id"
                            + " AND m_it.delete_date IS NULL"
                            + " inner join mst_item_class mic on m_it.item_class_id = mic.item_class_id"
                            + " WHERE "
                            + "  dss.shop_id = " + shopInfo.getShopID();
                            if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
                                sql  += "  AND dss.store_date >= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getFrom());
                            }
                            sql  += "  AND dss.store_date <= " + SQLUtil.convertForSQL(((DateRange)inventoryPeriod.getSelectedItem()).getTo())       
                            + "  AND dssd.delete_date IS NULL"
                            + "  AND dss.delete_date IS NULL" 
                            + "  AND m_si.supplier_id = "   + rs2.getInt("supplier_id")           
                            + "  AND mic.item_class_id in (61)"  
                            //IVS_LVTu start add New request #42532
                            + "  AND dss.ship_slip_no is null "
                            //IVS_LVTu end add New request #42532
                            + " GROUP BY m_si.supplier_id ,supplier_name";
                          
                            ResultSetWrapper rs3 = cw.executeQuery(sql);
                            jx.setCellValue(column, row + 2, 0);
                            jx.setCellValue(column, row + 3, 0);
                            jx.setCellValue(column, row + 4, 0);
                            jx.setCellValue(column, row + 5, 0);
                            int total = 0;
                            while (rs3.next()) {
                                 total += rs3.getInt("real_cost_no_tax");
                                if (rs3.getInt("item_use_division") == 1) {
                                    jx.setCellValue(column, row + 2, rs3.getInt("real_cost_no_tax"));                                   
                                }else if (rs3.getInt("item_use_division") == 2)
                                {
                                    jx.setCellValue(column, row + 3, rs3.getInt("real_cost_no_tax")); 
                                }
                                else if (rs3.getInt("item_use_division") == 3)
                                {
                                    jx.setCellValue(column, row + 4, rs3.getInt("real_cost_no_tax")); 
                                }
                                 else if (rs3.getInt("item_use_division") == 4)
                                {
                                    jx.setCellValue(column, row + 5, rs3.getInt("real_cost_no_tax")); 
                                }
                                
                            }  
                            // IVS_LVTu end edit 2015/09/20 New request #42137
                            // IVS_LVTu start edit 2014/09/09 MASHU_棚卸表
                            // jx.setCellValue(column, row + 7, total);     
                            // IVS_LVTu end edit 2014/09/09 MASHU_棚卸表
                            column +=1;
                    }
                }
                jx.setFormularActive();
               jx.openWorkbook();
           
               return;
                 
            }
            catch(Exception ex){
                ex.printStackTrace();
                return;
            }
                
           
        }
        // Thanh end add 2014/09/04
        TotalInventoryReportLogic totalInvetory = new TotalInventoryReportLogic(
                shopInfo, taxType, (DateRange)inventoryPeriod.getSelectedItem() );
        
        int result = totalInvetory.generateReport( reportFileType );
        
        if(result == TotalInventoryReportLogic.RESULT_SUCCESS ){
            // 成功
        }else if(result == TotalInventoryReportLogic.RESULT_DATA_NOTHNIG ){
            // データなし
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(4001),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            
        }else if(result == TotalInventoryReportLogic.RESULT_ERROR ){
            // 予期せぬエラー
            MessageDialog.showMessageDialog( this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE );
        }
        
    }
    // IVS_LVTu start add 2014/09/09 MASHU_棚卸表
    /**
     * method getItemClassByShopIdByDate.
     * @param toDate
     * @param fromDate
     * @param shopId
     * @return 
     */
      public String getItemClassByShopIdByDate(String toDate, String fromDate, int shopId)
    {
        StringBuilder strSQL = new StringBuilder();
        strSQL.append( " select t.item_class_id, \n");
        strSQL.append( " t.item_class_name, \n");
        strSQL.append( " sum(coalesce(total_over_stock,0)) AS total_over_stock, \n");
        strSQL.append( " sum(coalesce(total_in_num,0)) as total_in_num, \n");
        strSQL.append( " sum(coalesce(total_in_cost_no_tax,0)) as total_in_cost_no_tax, \n");
        strSQL.append( " sum(coalesce(total_attach_num,0)) as total_attach_num, \n");
        strSQL.append( " sum(coalesce(total_out_num,0)) + sum(coalesce(total_out_num2,0)) as sales_num, \n");
        strSQL.append( " sum(coalesce(total_out_cost_no_tax,0)) + sum(coalesce(total_out_cost_no_tax2,0)) as sales_money,  \n");
        strSQL.append( " sum((-1 * coalesce(total_out_num_minus,0))) + sum(coalesce(total_in_num_plus,0)) as total_move_num, \n");
        strSQL.append( " sum((-1 * coalesce(total_out_minus_cost_no_tax,0))) + sum(coalesce(total_in_plus_cost_no_tax,0)) as total_move_cost_no_tax, \n");
        strSQL.append( " sum(coalesce(total_out_num_12,0)) as total_out_num_12, \n");
        strSQL.append( " sum(coalesce(total_out_cost_no_tax_12,0)) as total_out_cost_no_tax_12, \n");
        strSQL.append( " sum(coalesce(total_real_stock_num,0)) as total_real_stock_num, \n");
        strSQL.append( " sum(coalesce(total_real_stock_cost_no_tax,0)) as total_real_stock_cost_no_tax, \n");
        strSQL.append( " sum(coalesce(total_real_stock_num,0) - (coalesce(total_over_stock,0)+coalesce(total_in_num,0)+coalesce(total_attach_num,0)\n");
        strSQL.append( " -(coalesce(total_out_num,0)+coalesce(total_out_num2,0)) + (-1 * coalesce(total_out_num_minus,0)) +coalesce(total_in_num_plus,0)\n"); 
        strSQL.append( " -coalesce(total_out_num_12,0)\n");
        strSQL.append( " )) as accumulative_num,\n");
        strSQL.append( " sum(coalesce(total_real_stock_num_amount,0) - (coalesce(total_over_stock_amount,0)+coalesce(total_in_num_amount,0)+coalesce(total_attach_num_amount,0)\n");
        strSQL.append( " -(coalesce(total_out_num_amount,0)+coalesce(total_out_num2_amount,0)) + (-1 * coalesce(total_out_num_minus_amount,0)) +coalesce(total_in_num_plus_amount,0)\n");
        strSQL.append( " -coalesce(total_out_num_12_amount,0)\n");
        strSQL.append( " )) as accumulative_amount\n");
        // add 20160614 #50881-start
        //過不足金額（税抜）
        strSQL.append( " ,sum(coalesce(total_real_stock_cost_no_tax,0) - (coalesce(total_over_stock_cost_no_tax,0)+coalesce(total_in_cost_no_tax,0)+coalesce(total_attach_cost_no_tax,0)\n");
        strSQL.append( " -(coalesce(total_out_cost_no_tax,0)+coalesce(total_out_cost_no_tax2,0)) + (-1 * coalesce(total_out_minus_cost_no_tax,0)) +coalesce(total_in_plus_cost_no_tax,0)\n");
        strSQL.append( " -coalesce(total_out_cost_no_tax_12,0)\n");
        strSQL.append( " )) as accumulative_cost_no_tax\n");
        // add 20160614 #50881-end
        strSQL.append( " FROM \n"); 
        strSQL.append( " ( \n");
        strSQL.append( " select msti.item_class_id,msti.item_class_name,it.item_id,msti.display_seq, \n");
        
        //--[total_over_stock]繰越し個数
        strSQL.append( " (SELECT \n");
        strSQL.append( "         sum(coalesce(d_ivd.real_stock,0)) AS total_over_stock \n");
        strSQL.append( "    FROM data_inventory d_iv, \n");
        strSQL.append( "         data_inventory_detail d_ivd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( "    AND m_it.delete_date IS NULL \n");
        strSQL.append( "    WHERE d_iv.inventory_id = d_ivd.inventory_id \n");
        strSQL.append( "      AND d_iv.inventory_division = d_ivd.inventory_division \n");
        strSQL.append( "      AND d_iv.shop_id ="+ shopId +"\n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ fromDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");
        }else {
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ toDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");
        }
        strSQL.append( "      AND d_ivd.delete_date IS NULL \n");
        strSQL.append( "      AND d_iv.delete_date IS NULL  \n");
        strSQL.append( "      AND d_iv.inventory_division IN (1, 2) \n");
        strSQL.append( "      AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id	\n");
        strSQL.append( "      AND m_it.item_id = m_up.product_id and d_iv.shop_id = m_up.shop_id \n");
        strSQL.append( " ) as total_over_stock,\n");
        
        //--[total_over_stock_amount]繰越し金額（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "         sum(coalesce(d_ivd.real_stock * d_ivd.cost_price,0)) AS total_over_stock_amount \n");
        strSQL.append( "    FROM data_inventory d_iv, \n");
        strSQL.append( "         data_inventory_detail d_ivd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( "    AND m_it.delete_date IS NULL \n");
        strSQL.append( "    WHERE d_iv.inventory_id = d_ivd.inventory_id \n");
        strSQL.append( "      AND d_iv.inventory_division = d_ivd.inventory_division \n");
        strSQL.append( "      AND d_iv.shop_id ="+ shopId +"\n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ fromDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");
        }else {
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ toDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");
        }
        strSQL.append( "      AND d_ivd.delete_date IS NULL \n");
        strSQL.append( "      AND d_iv.delete_date IS NULL \n");
        strSQL.append( "      AND d_iv.inventory_division IN (1, 2) \n");
        strSQL.append( "      AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id \n");
        strSQL.append( "      AND m_it.item_id = m_up.product_id and d_iv.shop_id = m_up.shop_id \n");
        strSQL.append( " ) as total_over_stock_amount,\n");

        // add 20160614 #50881-start
        //--[total_over_stock_cost_no_tax]繰越し金額（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "         sum(sign(coalesce(d_ivd.real_stock, 0) * coalesce(d_ivd.cost_price, 0) / (1.0 + get_tax_rate(d_iv.inventory_date))) * (abs(coalesce(d_ivd.real_stock, 0) * ceil(coalesce(d_ivd.cost_price, 0) / (1.0 + get_tax_rate(d_iv.inventory_date)))))) AS total_over_stock_cost_no_tax \n");
        strSQL.append( "    FROM data_inventory d_iv, \n");
        strSQL.append( "         data_inventory_detail d_ivd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( "    AND m_it.delete_date IS NULL \n");
        strSQL.append( "    WHERE d_iv.inventory_id = d_ivd.inventory_id \n");
        strSQL.append( "      AND d_iv.inventory_division = d_ivd.inventory_division \n");
        strSQL.append( "      AND d_iv.shop_id ="+ shopId +"\n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ fromDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");
        }else {
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ toDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");
        }
        strSQL.append( "      AND d_ivd.delete_date IS NULL \n");
        strSQL.append( "      AND d_iv.delete_date IS NULL \n");
        strSQL.append( "      AND d_iv.inventory_division IN (1, 2) \n");
        strSQL.append( "      AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id \n");
        strSQL.append( "      AND m_it.item_id = m_up.product_id and d_iv.shop_id = m_up.shop_id \n");
        strSQL.append( " ) as total_over_stock_cost_no_tax,\n");
        // add 20160614 #50881-end
        
        //--[total_in_num]仕入個数&仕入金額&サービス個数
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(coalesce(d_ssd.in_num,0)) AS total_in_num \n");
        strSQL.append( " FROM data_slip_store d_ss , \n");
        strSQL.append( "    data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ss.shop_id ="+ shopId +" \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no NOT IN \n");
        strSQL.append( " ( SELECT dss.slip_no \n");
        strSQL.append( " FROM data_move_stock dms \n");
        strSQL.append( " INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id \n");
        strSQL.append( " AND dms.dest_slip_no = dss.slip_no \n");
        strSQL.append( " WHERE dms.dest_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND dss.store_date BETWEEN " + fromDate + " AND " + toDate + " ) \n");
        }else {
            strSQL.append( " AND dss.store_date <= " + toDate + " ) \n");
        }
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_in_num, \n");
        
        //--[total_in_num_amount]（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(coalesce(d_ssd.in_num * d_ssd.cost_price,0)) AS total_in_num_amount \n");
        strSQL.append( " FROM data_slip_store d_ss , \n");
        strSQL.append( "    data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ss.shop_id ="+ shopId +" \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no NOT IN \n");
        strSQL.append( " ( SELECT dss.slip_no \n");
        strSQL.append( " FROM data_move_stock dms \n");
        strSQL.append( " INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id \n");
        strSQL.append( " AND dms.dest_slip_no = dss.slip_no \n");
        strSQL.append( " WHERE dms.dest_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND dss.store_date BETWEEN  " + fromDate + " AND " + toDate + " ) \n");
        }else {
            strSQL.append( " AND dss.store_date <= " + toDate + " ) \n");
        }
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_in_num_amount, \n");
        
        //--[total_in_cost_no_tax]（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(sign(coalesce(d_ssd.in_num,0) * coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * (abs(coalesce(d_ssd.in_num,0) * ceil(coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date)))))) AS total_in_cost_no_tax \n");
        strSQL.append( " FROM data_slip_store d_ss , \n");
        strSQL.append( "    data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL	\n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ss.shop_id ="+ shopId +" \n");
//        strSQL.append( " AND d_ss.ship_slip_no is null  \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no NOT IN \n");
        strSQL.append( " ( SELECT dss.slip_no \n");
        strSQL.append( " FROM data_move_stock dms \n");
        strSQL.append( " INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id \n");
        strSQL.append( " AND dms.dest_slip_no = dss.slip_no \n");
        strSQL.append( " WHERE dms.dest_shop_id =" + shopId + " \n");	
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND dss.store_date BETWEEN " + fromDate + " AND " + toDate + " ) \n");
        }else {
            strSQL.append( " AND dss.store_date <= " + toDate + " ) \n");
        }
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_in_cost_no_tax, \n");
        
        //--[total_attach_num]サービス個数
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(coalesce(d_ssd.attach_num,0)) AS total_attach_num \n");
        strSQL.append( " FROM data_slip_store d_ss , \n");
        strSQL.append( "    data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL	\n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ss.shop_id ="+ shopId +" \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no NOT IN \n");
        strSQL.append( " ( SELECT dss.slip_no \n");
        strSQL.append( " FROM data_move_stock dms \n");
        strSQL.append( " INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id \n");
        strSQL.append( " AND dms.dest_slip_no = dss.slip_no \n");
        strSQL.append( " WHERE dms.dest_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND dss.store_date BETWEEN " + fromDate + " AND " + toDate + " ) \n");
        }else {
            strSQL.append( " AND dss.store_date <= " + toDate + " ) \n");
        }
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_attach_num, \n");

        //--[total_attach_num_amount]サービス金額（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(coalesce(d_ssd.attach_num * d_ssd.cost_price,0)) AS total_attach_num_amount \n");
        strSQL.append( " FROM data_slip_store d_ss, \n");
        strSQL.append( "    data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL	\n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ss.shop_id ="+ shopId +" \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no NOT IN \n");
        strSQL.append( " ( SELECT dss.slip_no \n");
        strSQL.append( " FROM data_move_stock dms \n");
        strSQL.append( " INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id \n");
        strSQL.append( " AND dms.dest_slip_no = dss.slip_no \n");
        strSQL.append( " WHERE dms.dest_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND dss.store_date BETWEEN " + fromDate + " AND " + toDate + " ) \n");
        }else {
            strSQL.append( " AND dss.store_date <= " + toDate + " ) \n");
        }
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_attach_num_amount, \n");
        
        //--[total_attach_cost_no_tax]サービス金額（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(sign(coalesce(d_ssd.attach_num, 0) * coalesce(d_ssd.cost_price, 0) / (1.0 + get_tax_rate(d_ss.store_date))) *  abs(coalesce(d_ssd.attach_num, 0) * ceil((coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date)))))) AS total_attach_cost_no_tax \n");
        strSQL.append( " FROM data_slip_store d_ss , \n");
        strSQL.append( "    data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL	\n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ss.shop_id ="+ shopId +" \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");	
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no NOT IN \n");
        strSQL.append( " ( SELECT dss.slip_no \n");
        strSQL.append( " FROM data_move_stock dms \n");
        strSQL.append( " INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id \n");
        strSQL.append( " AND dms.dest_slip_no = dss.slip_no \n");
        strSQL.append( " WHERE dms.dest_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND dss.store_date BETWEEN " + fromDate + " AND " + toDate + " ) \n");
        }else {
            strSQL.append( " AND dss.store_date <= " + toDate + " ) \n");
        }
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_attach_cost_no_tax, \n");
        
        //--[total_out_num]販売個数
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(coalesce(d_sshd.out_num,0)) AS total_out_num \n");
        strSQL.append( " FROM data_slip_ship d_ssh , \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( " AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( " AND d_ssh.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ssh.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " \n");	
        }else {
            strSQL.append( " AND d_ssh.ship_date <=  " + toDate + "  \n");	
        }
        strSQL.append( " AND d_sshd.item_use_division in (1, 3) \n");	// 店販
        strSQL.append( " AND d_sshd.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.slip_no NOT IN \n");
        strSQL.append( "  ( SELECT dssh.slip_no	\n");
        strSQL.append( "   FROM data_move_stock dms \n");
        strSQL.append( "   INNER JOIN data_slip_ship dssh ON dms.src_shop_id = dssh.shop_id \n");
        strSQL.append( "   AND dms.src_slip_no = dssh.slip_no \n");
        strSQL.append( "   WHERE dms.src_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND dssh.ship_date BETWEEN  " + fromDate + " AND " + toDate +" ) \n");
        }else {
            strSQL.append( "   AND dssh.ship_date <=  " + toDate + " ) \n");
        }
        strSQL.append( "   AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( "   AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num, \n" );
        
        //--[total_out_num_amount]販売金額（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(coalesce(d_sshd.out_num * d_sshd.cost_price,0)) AS total_out_num_amount \n");
        strSQL.append( " FROM data_slip_ship d_ssh , \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( " AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( " AND d_ssh.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ssh.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_sshd.item_use_division in (1, 3) \n");	// 店販
        strSQL.append( " AND d_sshd.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.slip_no NOT IN \n");
        strSQL.append( "  ( SELECT dssh.slip_no	\n");
        strSQL.append( "   FROM data_move_stock dms \n");
        strSQL.append( "   INNER JOIN data_slip_ship dssh ON dms.src_shop_id = dssh.shop_id \n");
        strSQL.append( "   AND dms.src_slip_no = dssh.slip_no \n");
        strSQL.append( "   WHERE dms.src_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND dssh.ship_date BETWEEN " + fromDate + " AND " + toDate +" ) \n");
        }else {
            strSQL.append( "   AND dssh.ship_date <= " + toDate + " ) \n");
        }
        strSQL.append( "   AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( "   AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num_amount, \n" );
        
        //--[total_out_cost_no_tax]販売金額（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(sign(coalesce(d_sshd.out_num,0) * coalesce(d_sshd.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(d_sshd.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_cost_no_tax \n");
        strSQL.append( " FROM data_slip_ship d_ssh , \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( " AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( " AND d_ssh.supplier_id = msi.supplier_id \n");
        strSQL.append( " AND d_ssh.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " \n");	
        }else {
            strSQL.append( " AND d_ssh.ship_date <=  " + toDate + "  \n");
        }
        strSQL.append( " AND d_sshd.item_use_division in (1, 3) \n"); // 店販
        strSQL.append( " AND d_sshd.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.slip_no NOT IN \n");
        strSQL.append( "  ( SELECT dssh.slip_no	\n");
        strSQL.append( "   FROM data_move_stock dms \n");
        strSQL.append( "   INNER JOIN data_slip_ship dssh ON dms.src_shop_id = dssh.shop_id \n");
        strSQL.append( "   AND dms.src_slip_no = dssh.slip_no \n");
        strSQL.append( "   WHERE dms.src_shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND dssh.ship_date BETWEEN " + fromDate + " AND " + toDate +" ) \n");
        }else {
            strSQL.append( "   AND dssh.ship_date <= " + toDate + " ) \n");
        }
        strSQL.append( "  AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_cost_no_tax, \n" );

        //--[total_out_num2]販売個数2
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(coalesce(dsd.product_num,0)) AS total_out_num2 \n");
        strSQL.append( " FROM data_sales ds , \n");
        strSQL.append( "    data_sales_detail dsd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dsd.product_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " WHERE ds.shop_id = dsd.shop_id	\n");
        strSQL.append( " AND ds.slip_no = dsd.slip_no \n");
        strSQL.append( " AND ds.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND ds.sales_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND ds.sales_date <= " + toDate +" \n");
        }
        strSQL.append( " AND ds.delete_date IS NULL \n");
        strSQL.append( " AND dsd.delete_date IS NULL \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( " AND dsd.product_division IN (2) \n");	//商品だけ
        strSQL.append( " AND m_it.item_id = m_up.product_id and ds.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num2,\n");
        
        //--[total_out_num2_amount]販売金額2（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(coalesce(dsd.product_num * m_si.cost_price,0)) AS total_out_num2_amount \n");	// オリジナル
        //strSQL.append( "        sum(coalesce(dsd.product_num * dsd.product_value,0) - dsd.discount_value) AS total_out_num2_amount \n");	// 割引額マイナスするか？★★★★
        //strSQL.append( "        sum(coalesce(dsd.product_num * dsd.product_value,0)) AS total_out_num2_amount \n");							// 割引額マイナスしない
        strSQL.append( " FROM data_sales ds , \n");
        strSQL.append( "    data_sales_detail dsd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dsd.product_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " WHERE ds.shop_id = dsd.shop_id \n");
        strSQL.append( " AND ds.slip_no = dsd.slip_no \n");
        strSQL.append( " AND ds.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND ds.sales_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND ds.sales_date <= " + toDate +" \n");
        }
        strSQL.append( " AND ds.delete_date IS NULL \n");
        strSQL.append( " AND dsd.delete_date IS NULL \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( " AND dsd.product_division IN (2) \n");	//商品だけ
        strSQL.append( " AND m_it.item_id = m_up.product_id and ds.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num2_amount,\n");
        
        //--[total_out_cost_no_tax2]販売金額2（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "        sum(sign(coalesce(dsd.product_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(ds.sales_date))) * (abs(coalesce(dsd.product_num,0) * ceil(coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(ds.sales_date)))))) AS total_out_cost_no_tax2 \n");	// オリジナル
        //strSQL.append( "        sum(sign(coalesce(dsd.product_num,0) * coalesce(dsd.product_value,0) / (1.0 + get_tax_rate(ds.sales_date))) * (abs(coalesce(dsd.product_num,0) * ceil(coalesce(dsd.product_value,0) / (1.0 + get_tax_rate(ds.sales_date))))) - dsd.discount_value) AS total_out_cost_no_tax2 \n");	// 割引額マイナスするか？★★★★
        //strSQL.append( "        sum(sign(coalesce(dsd.product_num,0) * coalesce(dsd.product_value,0) / (1.0 + get_tax_rate(ds.sales_date))) * (abs(coalesce(dsd.product_num,0) * ceil(coalesce(dsd.product_value,0) / (1.0 + get_tax_rate(ds.sales_date)))))) AS total_out_cost_no_tax2 \n");							// 割引額マイナスしない
        strSQL.append( " FROM data_sales ds, \n");
        strSQL.append( "    data_sales_detail dsd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = dsd.product_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " WHERE ds.shop_id = dsd.shop_id \n");
        strSQL.append( " AND ds.slip_no = dsd.slip_no \n");
        strSQL.append( " AND ds.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND ds.sales_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND ds.sales_date <= " + toDate +" \n");
        }
        strSQL.append( " AND ds.delete_date IS NULL \n");
        strSQL.append( " AND dsd.delete_date IS NULL \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( " AND dsd.product_division IN (2) \n");	//商品だけ
        strSQL.append( " AND m_it.item_id = m_up.product_id and ds.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_cost_no_tax2,\n");
       
        //--[total_in_num_plus]入庫個数（移動個数）
        strSQL.append( " (SELECT \n");
        strSQL.append( "      sum(coalesce(d_ssd.in_num,0)) AS total_in_num_plus \n");
        strSQL.append( " FROM data_slip_store d_ss, \n");
        strSQL.append( "     data_slip_store_detail d_ssd \n");	
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id  AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id and msi.delete_date IS NULL \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");	
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");	
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no in ( select dest_slip_no from data_move_stock where dest_shop_id = " + shopId + " ) \n");
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_in_num_plus,\n");
        
        //--[total_in_num_plus_amount]入庫金額（移動金額）（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "      sum(coalesce(d_ssd.in_num * d_ssd.cost_price,0)) AS total_in_num_plus_amount \n");
        strSQL.append( " FROM data_slip_store d_ss, \n");
        strSQL.append( "     data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id  AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id and msi.delete_date IS NULL \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");	
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no in ( select dest_slip_no from data_move_stock where dest_shop_id = " + shopId + " ) \n");
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_in_num_plus_amount,\n");
        
        //--[total_in_plus_cost_no_tax]入庫金額（移動金額）（税抜）
        strSQL.append( " (SELECT  \n");
        strSQL.append( "      sum(sign(coalesce(d_ssd.in_num,0) * coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * ceil(abs(coalesce(d_ssd.in_num,0) * coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))))) AS total_in_plus_cost_no_tax \n");																																	
        strSQL.append( " FROM data_slip_store d_ss, \n");
        strSQL.append( "     data_slip_store_detail d_ssd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id  AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id and msi.delete_date IS NULL \n"	);
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ss.shop_id = d_ssd.shop_id \n");
        strSQL.append( " AND d_ss.slip_no = d_ssd.slip_no \n");
        strSQL.append( " AND d_ss.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( " AND d_ss.store_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( " AND d_ss.store_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_ssd.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.delete_date IS NULL \n");
        strSQL.append( " AND d_ss.slip_no in ( select dest_slip_no from data_move_stock where dest_shop_id = " + shopId + " ) \n");
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ss.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_in_plus_cost_no_tax,\n");
        
        //--[total_out_num_minus]出庫個数
        strSQL.append( " (SELECT \n");
        strSQL.append( "     sum(coalesce(d_sshd.out_num,0)) AS total_out_num_minus \n");
        strSQL.append( " FROM data_slip_ship d_ssh, \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( " AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( " AND d_ssh.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
             strSQL.append( " AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
             strSQL.append( " AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_sshd.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.slip_no in ( \n");
        strSQL.append( "           SELECT dms.src_slip_no FROM data_move_stock dms \n");	// 店舗間移動で、入庫完了のみ
        strSQL.append( "           INNER JOIN data_slip_store dss ON dss.shop_id = dms.dest_shop_id AND dss.slip_no = dms.dest_slip_no \n");
        strSQL.append( "           WHERE dms.src_shop_id = " + shopId + " \n");
        strSQL.append( "           AND dss.delete_date IS NULL \n");
        strSQL.append( "           AND dss.store_date >= date "+fromDate+" AND date_trunc('day', dss.store_date) <= date "+toDate+") \n");
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num_minus, \n");
        
        //--[total_out_num_minus_amount]出庫金額（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "     sum(coalesce(d_sshd.out_num * d_sshd.cost_price,0)) AS total_out_num_minus_amount \n");
        strSQL.append( " FROM data_slip_ship d_ssh, \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( " AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( " AND d_ssh.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
             strSQL.append( " AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
             strSQL.append( " AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_sshd.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.slip_no in ( \n");
        strSQL.append( "           SELECT dms.src_slip_no FROM data_move_stock dms \n");	// 店舗間移動で、入庫完了のみ
        strSQL.append( "           INNER JOIN data_slip_store dss ON dss.shop_id = dms.dest_shop_id AND dss.slip_no = dms.dest_slip_no \n");
        strSQL.append( "           WHERE dms.src_shop_id = " + shopId + " \n");
        strSQL.append( "           AND dss.delete_date IS NULL \n");
        strSQL.append( "           AND dss.store_date >= DATE "+fromDate+" AND date_trunc('day', dss.store_date) <= DATE "+toDate+" ) \n");
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num_minus_amount, \n");
        
        //--[total_out_num_minus_amount]出庫金額（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "     sum(sign(coalesce(d_sshd.out_num,0) * coalesce(d_sshd.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(d_sshd.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_minus_cost_no_tax \n");																																	
        strSQL.append( " FROM data_slip_ship d_ssh, \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( " AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( " AND d_ssh.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
             strSQL.append( " AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
             strSQL.append( " AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( " AND d_sshd.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.delete_date IS NULL \n");
        strSQL.append( " AND d_ssh.slip_no in ( \n");
        strSQL.append( "           SELECT dms.src_slip_no FROM data_move_stock dms \n");	// 店舗間移動で、入庫完了のみ
        strSQL.append( "           INNER JOIN data_slip_store dss ON dss.shop_id = dms.dest_shop_id AND dss.slip_no = dms.dest_slip_no \n");
        strSQL.append( "           WHERE dms.src_shop_id = " + shopId + " \n");
        strSQL.append( "           AND dss.delete_date IS NULL \n");
        strSQL.append( "           AND dss.store_date >= DATE "+fromDate+" AND date_trunc('day', dss.store_date) <= DATE "+toDate+" ) \n");
        strSQL.append( " AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( " AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_minus_cost_no_tax, \n");
        
        //--[total_out_num_12]業務使用個数
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(coalesce(d_sshd.out_num,0)) AS total_out_num_12 \n");
        strSQL.append( " FROM data_slip_ship d_ssh , \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( "  AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( "  AND d_ssh.supplier_id = msi.supplier_id \n");
        strSQL.append( "  AND d_ssh.shop_id =" + shopId+ "\n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "  AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( "  AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( "  AND d_sshd.delete_date IS NULL \n");
        strSQL.append( "  AND d_ssh.delete_date IS NULL	\n");
        strSQL.append( "  AND d_sshd.out_class = '1' \n");	// 出庫区分=業務出庫
        strSQL.append( "      and m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num_12, \n");
        
        //--[total_out_num_12_amount]業務使用金額(税込)
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(coalesce(d_sshd.out_num * d_sshd.cost_price,0)) AS total_out_num_12_amount \n");
        strSQL.append( " FROM data_slip_ship d_ssh , \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( "  AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( "  AND d_ssh.supplier_id = msi.supplier_id \n");
        strSQL.append( "  AND d_ssh.shop_id =" + shopId+ "\n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "  AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( "  AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( "  AND d_sshd.delete_date IS NULL \n");
        strSQL.append( "  AND d_ssh.delete_date IS NULL	\n");
        strSQL.append( "  AND d_sshd.out_class = '1' \n");	// 出庫区分=業務出庫
        strSQL.append( "  AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_num_12_amount, \n");
        
        //--[total_out_cost_no_tax_12]業務使用金額(税抜)
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(sign(coalesce(d_sshd.out_num,0) * coalesce(d_sshd.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(d_sshd.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_cost_no_tax_12 \n");
        strSQL.append( " FROM data_slip_ship d_ssh , \n");
        strSQL.append( "     data_slip_ship_detail d_sshd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id	\n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id \n");
        strSQL.append( " WHERE d_ssh.shop_id = d_sshd.shop_id \n");
        strSQL.append( "  AND d_ssh.slip_no = d_sshd.slip_no \n");
        strSQL.append( "  AND d_ssh.supplier_id = msi.supplier_id \n");
        strSQL.append( "  AND d_ssh.shop_id =" + shopId+ "\n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "  AND d_ssh.ship_date BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( "  AND d_ssh.ship_date <= " + toDate + " \n");
        }
        strSQL.append( "  AND d_sshd.delete_date IS NULL \n");
        strSQL.append( "  AND d_ssh.delete_date IS NULL	\n");
        strSQL.append( "  AND d_sshd.out_class = '1' \n");	// 出庫区分=業務出庫
        strSQL.append( "  AND m_it.item_id = m_up.product_id and d_ssh.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_out_cost_no_tax_12, \n");
        
        //--[total_real_stock_num]実在庫数
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(coalesce(d_ivd.real_stock,0)) as total_real_stock_num \n");
        strSQL.append( " FROM data_inventory d_iv , \n");
        strSQL.append( "     data_inventory_detail d_ivd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " WHERE d_iv.inventory_id = d_ivd.inventory_id \n");
        strSQL.append( "  AND d_iv.inventory_division = d_ivd.inventory_division \n");
        strSQL.append( "  AND d_iv.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "  AND d_iv.inventory_date  BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( "  AND d_iv.inventory_date <= " + toDate + " \n");
        }
        strSQL.append( "  AND d_ivd.delete_date IS NULL \n");
        strSQL.append( "  AND d_iv.delete_date IS NULL \n");
        strSQL.append( "  AND d_iv.inventory_division IN (1, 2)	\n");
        strSQL.append( "  AND m_it.item_id = m_up.product_id and d_iv.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_real_stock_num, \n");

        //--[total_real_stock_num_amount]実在庫金額（税込）
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(coalesce(d_ivd.real_stock * d_ivd.cost_price,0)) as total_real_stock_num_amount \n");
        strSQL.append( " FROM data_inventory d_iv , \n");
        strSQL.append( "     data_inventory_detail d_ivd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " WHERE d_iv.inventory_id = d_ivd.inventory_id \n");
        strSQL.append( "  AND d_iv.inventory_division = d_ivd.inventory_division \n");
        strSQL.append( "  AND d_iv.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "  AND d_iv.inventory_date  BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( "  AND d_iv.inventory_date <=  " + toDate + " \n");
        }
        strSQL.append( "  AND d_ivd.delete_date IS NULL \n");
        strSQL.append( "  AND d_iv.delete_date IS NULL \n");
        strSQL.append( "  AND d_iv.inventory_division IN (1, 2)	\n");
        strSQL.append( "  AND m_it.item_id = m_up.product_id and d_iv.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_real_stock_num_amount, \n");

        //--[total_real_stock_cost_no_tax]実在庫金額（税抜）
        strSQL.append( " (SELECT \n");
        strSQL.append( "       sum(sign(coalesce(d_ivd.real_stock,0) * coalesce(d_ivd.cost_price,0) / (1.0 + get_tax_rate(d_iv.inventory_date))) * abs(coalesce(d_ivd.real_stock,0) * ceil((coalesce(d_ivd.cost_price,0) / (1.0 + get_tax_rate(d_iv.inventory_date)))))) AS total_real_stock_cost_no_tax \n");																																
        strSQL.append( " FROM data_inventory d_iv , \n");
        strSQL.append( "     data_inventory_detail d_ivd \n");
        strSQL.append( " INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        strSQL.append( " LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id \n");
        strSQL.append( " AND m_it.delete_date IS NULL \n");
        strSQL.append( " WHERE d_iv.inventory_id = d_ivd.inventory_id \n");
        strSQL.append( "  AND d_iv.inventory_division = d_ivd.inventory_division \n");
        strSQL.append( "  AND d_iv.shop_id =" + shopId + " \n");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "  AND d_iv.inventory_date  BETWEEN " + fromDate + " AND " + toDate + " \n");
        }else {
            strSQL.append( "  AND d_iv.inventory_date <= " + toDate + " \n");
        }
        strSQL.append( "  AND d_ivd.delete_date IS NULL \n");
        strSQL.append( "  AND d_iv.delete_date IS NULL \n");
        strSQL.append( "  AND d_iv.inventory_division IN (1, 2) \n");
        strSQL.append( "  AND m_it.item_id = m_up.product_id and d_iv.shop_id = m_up.shop_id \n");
        strSQL.append( "  AND m_it.item_class_id = msti.item_class_id and m_it.item_id = it.item_id) as total_real_stock_cost_no_tax \n");
        
        strSQL.append( " FROM  mst_supplier_item m_si \n");
        strSQL.append( " LEFT JOIN mst_item it on it.item_id = m_si.item_id  \n");
        strSQL.append( " left join mst_item_class msti on msti.item_class_id = it.item_class_id  AND msti.delete_date IS NULL \n");
        strSQL.append( " ,mst_use_product m_up \n");
        strSQL.append( " WHERE msti.delete_date is null \n");
        strSQL.append( " AND m_up.product_division = 2 \n");
        strSQL.append( " AND m_up.product_id = m_si.item_id \n");
        strSQL.append( " AND m_si.delete_date IS NULL \n");
        
        strSQL.append( " group by msti.item_class_id, it.item_id, msti.item_class_name, msti.display_seq, m_up.product_id, m_up.shop_id \n");
        strSQL.append( " ORDER BY msti.display_seq \n");
        strSQL.append( "  )t \n");
        strSQL.append( " group by item_class_id, t.item_class_name, t.display_seq \n");
        strSQL.append( " ORDER BY display_seq \n");
        
        return strSQL.toString();
    } 
    
    
    /**
     * method getDataSaleUseSQL.
     * @param toDate
     * @param fromDate
     * @param shopId
     * @param type
     * @return String.
     */
    public String getDataSaleUseSQL(String toDate, String fromDate, int shopId, int type)
    {
        StringBuilder strSQL = new StringBuilder(1000);
        strSQL.append( "SELECT msh.shop_id,	 ");
        strSQL.append( "       coalesce(temp1. total_real_stock,0) as  total_real_stock, ");
        strSQL.append( "       coalesce(temp2.total_in_num,0) as total_in_num, ");
        strSQL.append( "       coalesce(temp2.total_in_cost_no_tax,0) as total_in_cost_no_tax, ");
        strSQL.append( "       coalesce(temp2.total_attach_num,0) as total_attach_num, ");
        if( type == 1) {
            strSQL.append( "       coalesce(temp3.total_out_num,0) + coalesce(temp4.total_out_num2,0) as sales_num, ");
            strSQL.append( "       coalesce(temp3.total_out_cost_no_tax,0) + coalesce( temp4.total_out_cost_no_tax2,0) as sales_money , ");
        }
        else {
            strSQL.append( "       coalesce(temp3.total_out_num,0) as sales_num , ");
            strSQL.append( "       coalesce(temp3.total_out_cost_no_tax,0) as sales_money, ");
        }   
        strSQL.append( "       (-1 * coalesce(temp33.total_out_num_minus,0)) + coalesce(temp22.total_in_num_plus,0) as total_move_num, ");
        strSQL.append( "       (-1 * coalesce(temp33.total_out_minus_cost_no_tax,0)) + coalesce(temp22.total_in_plus_cost_no_tax,0) as total_move_cost_no_tax, ");
        strSQL.append( "       coalesce(temp5.total_out_num_12,0) as total_out_num_12 , ");
        strSQL.append( "       coalesce(temp5.total_out_cost_no_tax_12,0) as total_out_cost_no_tax_12, ");
        strSQL.append( "       coalesce(temp6.total_real_stock_num,0) as total_real_stock_num, ");
        strSQL.append( "       coalesce(temp6.total_real_stock_cost_no_tax,0) as total_real_stock_cost_no_tax, ");
        strSQL.append( "        coalesce(total_real_stock_num,0) - (coalesce(total_real_stock,0)+coalesce(total_in_num,0)+coalesce(total_attach_num,0)\n");
        strSQL.append( "        -(coalesce(total_out_num,0) +coalesce(total_out_num2,0)) + (-1 * coalesce(total_out_num_minus,0)) +coalesce(total_in_num_plus,0)\n"); 
        strSQL.append( "        -coalesce(total_out_num_12,0)\n");
        strSQL.append( "        ) as accumulative_num,\n");
        strSQL.append( "         coalesce(total_real_stock_num_amount,0) - (coalesce(total_real_stock_amount,0)+coalesce(total_in_num_amount,0)+coalesce(total_attach_num_amount,0)\n");
        strSQL.append( "        -(coalesce(total_out_num_amount,0) +coalesce(total_out_num2_amount,0)) + (-1 * coalesce(total_out_num_minus_amount,0)) +coalesce(total_in_num_plus_amount,0)\n"); 
        strSQL.append( "        -coalesce(total_out_num_12_amount,0)\n");
        strSQL.append( "         ) as accumulative_amount\n");
        strSQL.append( "FROM   ");
        strSQL.append( "  mst_shop msh ");
        //繰越し個数
        strSQL.append( "LEFT JOIN  ");
        strSQL.append( "   (SELECT d_iv.shop_id, ");
        strSQL.append( "          sum(coalesce(d_ivd.real_stock,0)) AS total_real_stock,sum(coalesce(d_ivd.real_stock,0)*m_si.cost_price) AS total_real_stock_amount ");
        strSQL.append( "   FROM data_inventory d_iv , ");
        strSQL.append( "        data_inventory_detail d_ivd ");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id ");
        strSQL.append( "   AND m_si.delete_date IS NULL ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL ");
        //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_iv.inventory_id = d_ivd.inventory_id ");
        strSQL.append( "     AND d_iv.inventory_division = d_ivd.inventory_division ");
        strSQL.append( "     AND d_iv.shop_id ="+ shopId +" ");
       if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ fromDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' ");																																	
       }
       else {
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ toDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");																																	
        }
        strSQL.append( "     AND d_ivd.delete_date IS NULL ");
        strSQL.append( "     AND d_iv.delete_date IS NULL  ");
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        //店販用
        if( type == 1) {
            strSQL.append( "     AND d_iv.inventory_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_iv.inventory_division IN (2) ");
        } 
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_iv.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "   GROUP BY d_iv.shop_id ");
        strSQL.append( "   ) temp1 ON temp1.shop_id = msh.shop_id ");
        //仕入個数&仕入金額&サービス個数
        strSQL.append( "LEFT JOIN  ");
        strSQL.append( "  (SELECT d_ss.shop_id, ");
        strSQL.append( "          sum(coalesce(d_ssd.in_num,0)) AS total_in_num,sum(coalesce(d_ssd.in_num,0)*m_si.cost_price) AS total_in_num_amount, ");
        // IVS_LVTu start edit 2015/09/24 New request #42137
        //strSQL.append( "          sum(sign(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * ceil(abs(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))))) AS total_in_cost_no_tax, ");
        //IVS_LVTu start edit 2015/09/10 Bug #42528
        strSQL.append( "          sum(sign(coalesce(d_ssd.in_num,0) * coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * (abs(coalesce(d_ssd.in_num,0) * ceil(coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date)))))) AS total_in_cost_no_tax, ");
        //IVS_LVTu end edit 2015/09/10 Bug #42528
        // IVS_LVTu end edit 2015/09/24 New request #42137
        strSQL.append( "          sum(coalesce(d_ssd.attach_num,0)) AS total_attach_num,  sum(coalesce(d_ssd.attach_num,0)*m_si.cost_price) AS total_attach_num_amount");
        strSQL.append( "   FROM data_slip_store d_ss , ");
        strSQL.append( "        data_slip_store_detail d_ssd ");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL ");
         //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_ss.shop_id = d_ssd.shop_id ");
        strSQL.append( "     AND d_ss.slip_no = d_ssd.slip_no ");
        strSQL.append( "     AND d_ss.supplier_id = msi.supplier_id ");
        strSQL.append( "     AND d_ss.shop_id ="+ shopId +" ");
        //IVS_LVTu start add New request #42532
        strSQL.append( "     AND d_ss.ship_slip_no is null  \n");
        //IVS_LVTu end add New request #42532
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_ss.store_date BETWEEN  " + fromDate + " AND " + toDate + "  ");
        }
        else {
            strSQL.append( "     AND d_ss.store_date <=  " + toDate + "  ");
        }
        strSQL.append( "     AND d_ssd.delete_date IS NULL ");
        strSQL.append( "     AND d_ss.delete_date IS NULL ");
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_ss.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        strSQL.append( "     AND d_ss.slip_no NOT IN ");
        strSQL.append( "       ( SELECT dss.slip_no ");
        strSQL.append( "        FROM data_move_stock dms ");
        strSQL.append( "        INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id ");
        strSQL.append( "        AND dms.dest_slip_no = dss.slip_no ");
        strSQL.append( "        WHERE dms.dest_shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "          AND dss.store_date BETWEEN  " + fromDate + " AND " + toDate + " ) ");
        }
        else {
            strSQL.append( "          AND dss.store_date <=  " + toDate + " ) ");
        }
        //店販用
        if( type == 1) {
            strSQL.append( "     AND d_ssd.item_use_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_ssd.item_use_division IN (2) ");
        }   
        strSQL.append( "   GROUP BY d_ss.shop_id ");
        strSQL.append( "   ) temp2 ON temp2.shop_id = msh.shop_id ");
        //販売個数&販売金額(原価x売数)
        strSQL.append( "LEFT JOIN  ");
        strSQL.append( "  (SELECT d_ssh.shop_id, ");
        if(type ==1) {
            strSQL.append( "          sum(coalesce(d_sshd.out_num,0)) AS total_out_num, sum(coalesce(d_sshd.out_num,0)*m_si.cost_price) AS total_out_num_amount,");
            strSQL.append( "          sum(sign(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_cost_no_tax ");
        
        }else {
            strSQL.append( "          0 AS total_out_num, 0 AS total_out_num_amount,");
            strSQL.append( "          0 AS total_out_cost_no_tax ");
        }
        strSQL.append( "   FROM data_slip_ship d_ssh , ");
        strSQL.append( "        data_slip_ship_detail d_sshd ");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL ");
         //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_ssh.shop_id = d_sshd.shop_id ");
        strSQL.append( "     AND d_ssh.slip_no = d_sshd.slip_no ");
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_ssh.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "     AND d_ssh.supplier_id = msi.supplier_id ");
        strSQL.append( "     AND d_ssh.shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + "  ");
        }
        else {
            strSQL.append( "     AND d_ssh.ship_date <=  " + toDate +  "  ");
        }
        strSQL.append( "     AND d_sshd.delete_date IS NULL ");
        strSQL.append( "     AND d_ssh.delete_date IS NULL ");
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        strSQL.append( "     AND d_ssh.slip_no NOT IN ");
        strSQL.append( "       ( SELECT dssh.slip_no ");
        strSQL.append( "        FROM data_move_stock dms ");
        strSQL.append( "        INNER JOIN data_slip_ship dssh ON dms.src_shop_id = dssh.shop_id ");
        strSQL.append( "        AND dms.src_slip_no = dssh.slip_no ");
        strSQL.append( "        WHERE dms.src_shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "          AND dssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " ) ");
        }
        else {
            strSQL.append( "          AND dssh.ship_date <=  " + toDate + " ) ");
        }
        //店販用
        if( type == 1) {
            strSQL.append( "     AND d_sshd.item_use_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_sshd.item_use_division IN (2) ");
        }   
        strSQL.append( "   GROUP BY d_ssh.shop_id ");
        strSQL.append( "   ) temp3 ON temp3.shop_id = msh.shop_id ");
        //販売個数&販売金額(原価x売数)
        if( type == 1) {
            strSQL.append( " LEFT JOIN " ) ;
            strSQL.append( "   (SELECT ds.shop_id , " );
            strSQL.append( "   sum(coalesce(dsd.product_num,0)) AS total_out_num2,sum(coalesce(dsd.product_num,0)*m_si.cost_price) AS total_out_num2_amount, " );
            strSQL.append( "   sum(sign(coalesce(dsd.product_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(ds.sales_date))) * ceil(abs(coalesce(dsd.product_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(ds.sales_date))))) AS total_out_cost_no_tax2 " );
            strSQL.append( "    FROM data_sales ds , " );
            strSQL.append( " data_sales_detail dsd " );
            strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = dsd.product_id AND m_si.delete_date IS NULL " );
            strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
            
            //Luc start add 20150713 #38264
            strSQL.append( "   ,mst_use_product m_up ");
            //Luc end add 20150713 #38264
            strSQL.append( "    WHERE ds.shop_id = dsd.shop_id " );
            strSQL.append( "  AND ds.slip_no = dsd.slip_no " );
            //Luc start add 20150713 #38264
            strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and ds.shop_id=m_up.shop_id ");
            //Luc end add 20150713 #38264
            strSQL.append( "  AND ds.shop_id ="+ shopId +" " );
            if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
                strSQL.append( "  AND ds.sales_date BETWEEN  " + fromDate + " AND " + toDate + "  " );
            }
            else {
                strSQL.append( "  AND ds.sales_date <=  " + toDate + "  " );
            }
            strSQL.append( "  AND ds.delete_date IS NULL " );
            strSQL.append( "  AND dsd.delete_date IS NULL " );
            strSQL.append( "  AND m_si.delete_date IS NULL  " );
            //商品だけ
            strSQL.append( "  AND dsd.product_division IN (2) " );
            strSQL.append( "  AND m_it.item_class_id not in (9,61) " );
            strSQL.append( "    GROUP BY ds.shop_id " );
            strSQL.append( "    ) temp4 ON temp4.shop_id = msh.shop_id " );
        }else {
            strSQL.append( " LEFT JOIN " ) ;
            strSQL.append( "   (SELECT " );
            strSQL.append( "  0 as total_out_num2,0 AS total_out_num2_amount, " );
            strSQL.append( "  0 AS total_out_cost_no_tax2 " );
             strSQL.append( "   ) temp4 ON 1= 1 " );
        } 
        //移動個数&移動金額
        strSQL.append( "LEFT JOIN  ");
        strSQL.append( "  (SELECT d_ss.shop_id, ");
        strSQL.append( "          sum(coalesce(d_ssd.in_num,0)) AS total_in_num_plus, sum(coalesce(d_ssd.in_num,0)*m_si.cost_price) AS total_in_num_plus_amount,");
        strSQL.append( "          sum(sign(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * ceil(abs(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))))) AS total_in_plus_cost_no_tax ");
        strSQL.append( "   FROM data_slip_store d_ss, ");
        strSQL.append( "        data_slip_store_detail d_ssd ");
        // IVS_LVTu start edit 2014/09/30 MASHU_棚卸表
        //strSQL.append( "   INNER JOIN data_move_stock dms ON dms.dest_shop_id = d_ssd.shop_id and dms.dest_slip_no = d_ssd.slip_no and dms.item_id  = d_ssd.item_id");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id  AND m_si.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id and msi.delete_date IS NULL ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL ");
         //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_ss.shop_id = d_ssd.shop_id ");
        strSQL.append( "     AND d_ss.slip_no = d_ssd.slip_no ");
        //strSQL.append( "     AND d_ss.supplier_id = msi.supplier_id ");
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_ss.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "     AND d_ss.shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_ss.store_date BETWEEN  " + fromDate + " AND " + toDate + "  ");
        }
        else {
            strSQL.append( "     AND d_ss.store_date <=  " + toDate + "  ");
        }
        strSQL.append( "     AND d_ssd.delete_date IS NULL ");
        strSQL.append( "     AND d_ss.delete_date IS NULL ");
        //strSQL.append( "     AND dms.delete_date IS NULL ");
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        strSQL.append( "     AND d_ss.slip_no in ( select dest_slip_no from data_move_stock where dest_shop_id = "+ shopId + " ) ");
        //店販用
        if( type == 1) {
            strSQL.append( "     AND d_ssd.item_use_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_ssd.item_use_division IN (2) ");
        }   
        strSQL.append( "   GROUP BY d_ss.shop_id ");
        strSQL.append( "   ) temp22 ON temp22.shop_id = msh.shop_id ");
        strSQL.append( "    ");
        strSQL.append( " LEFT JOIN   ");
        strSQL.append( "  (SELECT d_ssh.shop_id, ");
        strSQL.append( "          sum(coalesce(d_sshd.out_num,0)) AS total_out_num_minus,sum(coalesce(d_sshd.out_num,0)*m_si.cost_price) AS total_out_num_minus_amount, ");
        strSQL.append( "          sum(sign(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_minus_cost_no_tax ");
        strSQL.append( "   FROM data_slip_ship d_ssh, ");
        strSQL.append( "        data_slip_ship_detail d_sshd ");
        //strSQL.append( "   INNER JOIN data_move_stock dms ON dms.src_shop_id = d_sshd.shop_id and dms.src_slip_no = d_sshd.slip_no and dms.item_id  = d_sshd.item_id ");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id ");
        strSQL.append( "   AND m_it.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id ");
         //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_ssh.shop_id = d_sshd.shop_id ");
        strSQL.append( "     AND d_ssh.slip_no = d_sshd.slip_no ");
        //strSQL.append( "     AND d_ssh.supplier_id = msi.supplier_id ");
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_ssh.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "     AND d_ssh.shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + "  ");
        }
        else {
            strSQL.append( "     AND d_ssh.ship_date <= " + toDate + "  ");
        }
        strSQL.append( "     AND d_sshd.delete_date IS NULL ");
        strSQL.append( "     AND d_ssh.delete_date IS NULL ");
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        strSQL.append( "     AND d_ssh.slip_no in ( select src_slip_no from data_move_stock where src_shop_id = "+ shopId + " ) ");
        // IVS_LVTu end edit 2014/09/30 MASHU_棚卸表
        //店販用
        if( type == 1) {
            strSQL.append( "     AND d_sshd.item_use_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_sshd.item_use_division IN (2) ");
        }   
        strSQL.append( "   GROUP BY d_ssh.shop_id ");
        strSQL.append( "   ) temp33 ON temp33.shop_id = msh.shop_id ");
        //業務使用個数&業務使用金額
        strSQL.append( "LEFT JOIN    ");
        strSQL.append( "   (SELECT d_ssh.shop_id, ");
        if(type==1) {
            strSQL.append( "          0 AS total_out_num_12,0 AS total_out_num_12_amount, ");
            strSQL.append( "          0 AS total_out_cost_no_tax_12 ");
        
        }else {
            strSQL.append( "          sum(coalesce(d_sshd.out_num,0)) AS total_out_num_12,sum(coalesce(d_sshd.out_num,0)*m_si.cost_price) AS total_out_num_12_amount, ");
            strSQL.append( "          sum(sign(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_cost_no_tax_12 ");
        }
        strSQL.append( "   FROM data_slip_ship d_ssh , ");
        strSQL.append( "        data_slip_ship_detail d_sshd ");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL ");
         //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_ssh.shop_id = d_sshd.shop_id ");
        strSQL.append( "     AND d_ssh.slip_no = d_sshd.slip_no ");
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_ssh.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "     AND d_ssh.supplier_id = msi.supplier_id ");
        strSQL.append( "     AND d_ssh.shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + "  ");
        }
        else {
            strSQL.append( "     AND d_ssh.ship_date <=  " + toDate + "  ");
        }
        strSQL.append( "     AND d_sshd.delete_date IS NULL ");
        strSQL.append( "     AND d_ssh.delete_date IS NULL ");
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        //店販用
        if( type == 1) {
            strSQL.append( "     AND d_sshd.item_use_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_sshd.item_use_division IN (2) ");
        } 
        strSQL.append( "   GROUP BY d_ssh.shop_id ");
        strSQL.append( "   ) temp5 ON temp5.shop_id = msh.shop_id ");
        //実在庫数&棚卸し残高
        strSQL.append( "LEFT JOIN  ");
        strSQL.append( "  (SELECT d_iv.shop_id, ");
        strSQL.append( "          sum(coalesce(d_ivd.real_stock,0)) as total_real_stock_num,  sum(coalesce(d_ivd.real_stock,0)*m_si.cost_price) as total_real_stock_num_amount,");
        strSQL.append( "          sum(sign(coalesce(d_ivd.real_stock,0) * coalesce(d_ivd.cost_price,0) / (1.0 + get_tax_rate(d_iv.inventory_date))) * ceil(abs(coalesce(d_ivd.real_stock,0) * coalesce(d_ivd.cost_price,0) / (1.0 + get_tax_rate(d_iv.inventory_date))))) AS total_real_stock_cost_no_tax ");
        strSQL.append( "   FROM data_inventory d_iv , ");
        strSQL.append( "        data_inventory_detail d_ivd ");
        strSQL.append( "   INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id ");
        strSQL.append( "   AND m_si.delete_date IS NULL ");
        strSQL.append( "   LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id ");
        strSQL.append( "   AND m_it.delete_date IS NULL ");
        strSQL.append( "   INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id ");
         //Luc start add 20150713 #38264
        strSQL.append( "   ,mst_use_product m_up ");
        //Luc end add 20150713 #38264
        strSQL.append( "   WHERE d_iv.inventory_id = d_ivd.inventory_id ");
        strSQL.append( "     AND d_iv.inventory_division = d_ivd.inventory_division ");
        //Luc start add 20150713 #38264
        strSQL.append( "    AND m_up.product_id = m_it.item_id and m_up.product_division=2 and d_iv.shop_id=m_up.shop_id ");
        //Luc end add 20150713 #38264
        strSQL.append( "     AND d_iv.shop_id ="+ shopId +" ");
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND d_iv.inventory_date  BETWEEN  " + fromDate + " AND " + toDate + "  ");
        }
        else {
            strSQL.append( "     AND d_iv.inventory_date  <=  " + toDate + "  ");
        }
        strSQL.append( "     AND d_ivd.delete_date IS NULL ");
        strSQL.append( "     AND d_iv.delete_date IS NULL  ");
        strSQL.append( "     AND m_it.item_class_id not in (9,61) ");
        //店販用
        if( type == 1) {
           strSQL.append( "     AND d_iv.inventory_division IN (1) ");
        }
        else {
            strSQL.append( "     AND d_iv.inventory_division IN (2) ");
        } 
        strSQL.append( "   GROUP BY d_iv.shop_id ");
        strSQL.append( "   ) temp6 ON temp6.shop_id = msh.shop_id ");
        strSQL.append( "where msh.shop_id ="+ shopId +" " );
     
        return strSQL.toString();
    }
    
    /**
     * method getProductConsumSQL.
     * @param toDate
     * @param fromDate
     * @param shopId
     * @param type
     * @return 
     */
    public String getProductConsumSQL(String toDate, String fromDate, int shopId, int type)
    {
         StringBuilder strSQL = new StringBuilder(1000);
         strSQL.append( "	SELECT msh.shop_id, " );
        strSQL.append( "     coalesce(temp1. total_real_stock,0) as  total_real_stock, " );
        strSQL.append( "     coalesce(temp2.total_in_num,0) as total_in_num, " );
        strSQL.append( "     coalesce(temp2.total_in_cost_no_tax,0) as total_in_cost_no_tax, " );
        strSQL.append( "     coalesce(temp2.total_attach_num,0) as total_attach_num, " );
        strSQL.append( "     coalesce(temp3.total_out_num,0) + coalesce( temp4.total_out_num2,0) as sales_num, " );
        strSQL.append( "     coalesce(temp3.total_out_cost_no_tax,0) + coalesce(temp4.total_out_cost_no_tax2,0) as sales_money , " );
        strSQL.append( "     (-1 * coalesce(temp33.total_out_num_minus,0)) + coalesce(temp22.total_in_num_plus,0) as total_move_num, " );
        strSQL.append( "     (-1 * coalesce(temp33.total_out_minus_cost_no_tax,0)) + coalesce(temp22.total_in_plus_cost_no_tax,0) as total_move_cost_no_tax, " );
        strSQL.append( "     coalesce(temp5.total_out_num_12,0) as total_out_num_12, " );
        strSQL.append( "     coalesce(temp5.total_out_cost_no_tax_12,0) as total_out_cost_no_tax_12, " );
        strSQL.append( "     coalesce(temp6.total_real_stock_num,0) as total_real_stock_num, " );
        strSQL.append( "     coalesce(temp6.total_real_stock_cost_no_tax,0) as total_real_stock_cost_no_tax, " );
        strSQL.append( "     coalesce(total_real_stock_num,0) - (coalesce(total_real_stock,0)+coalesce(total_in_num,0)+coalesce(total_attach_num,0)\n");
        strSQL.append( "     -(coalesce(total_out_num,0) +coalesce(total_out_num2,0)) + (-1 * coalesce(total_out_num_minus,0)) +coalesce(total_in_num_plus,0)\n"); 
        strSQL.append( "     -coalesce(total_out_num_12,0)\n");
        strSQL.append( "     ) as accumulative_num,\n");
        strSQL.append( "     coalesce(total_real_stock_num_amount,0) - (coalesce(total_real_stock_amount,0)+coalesce(total_in_num_amount,0)+coalesce(total_attach_num_amount,0)\n");
        strSQL.append( "     -(coalesce(total_out_num_amount,0) +coalesce(total_out_num2_amount,0)) + (-1 * coalesce(total_out_num_minus_amount,0)) +coalesce(total_in_num_plus_amount,0)\n"); 
        strSQL.append( "     -coalesce(total_out_num_12_amount,0)\n");
        strSQL.append( "     ) as accumulative_amount\n");
        strSQL.append( " FROM " );
        strSQL.append( "   mst_shop msh " );
        //繰越し個数
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "    (SELECT d_iv.shop_id, " );
        // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_ivd.real_stock,0)) AS total_real_stock,sum(coalesce(d_ivd.real_stock,0)*m_si.cost_price) AS total_real_stock_amount " );
        // Luc end edit 20150717 #38264
        strSQL.append( "    FROM data_inventory d_iv , " );
        strSQL.append( "   data_inventory_detail d_ivd " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id " );
        strSQL.append( "    AND m_si.delete_date IS NULL " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
        strSQL.append( "    WHERE d_iv.inventory_id = d_ivd.inventory_id " );
        strSQL.append( "   AND d_iv.inventory_division = d_ivd.inventory_division " );
        strSQL.append( "   AND d_iv.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ fromDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' " );																																
        }
        else {
            strSQL.append( "      AND d_iv.inventory_date = to_date(to_char(TIMESTAMP "+ toDate +",'yyyy/mm/01'),'yyyy/mm/dd') + interval '-1 day' \n");																																	
        }
        strSQL.append( "   AND d_ivd.delete_date IS NULL " );
        strSQL.append( "   AND d_iv.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        //業務用及び店販用
        strSQL.append( "   AND d_iv.inventory_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_iv.shop_id " );
        strSQL.append( "    ) temp1 ON temp1.shop_id = msh.shop_id " );
        //仕入個数&仕入金額&サービス個数
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "   (SELECT d_ss.shop_id, " );
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_ssd.in_num,0)) AS total_in_num, sum(coalesce(d_ssd.in_num,0)*m_si.cost_price) AS total_in_num_amount," );
         // Luc end edit 20150717 #38264
        // IVS_LVTu start edit 2015/09/24 New request #42137
        //strSQL.append( "     sum(sign(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * ceil(abs(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))))) AS total_in_cost_no_tax, " );
        //IVS_LVTu start edit 2015/09/10 Bug #42528
        strSQL.append( "     sum(sign(coalesce(d_ssd.in_num,0) * coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * (abs(coalesce(d_ssd.in_num,0) * ceil(coalesce(d_ssd.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date)))))) AS total_in_cost_no_tax, " );
        //IVS_LVTu end edit 2015/09/10 Bug #42528
        // IVS_LVTu end edit 2015/09/24 New request #42137
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_ssd.attach_num,0)) AS total_attach_num,sum(coalesce(d_ssd.attach_num,0)*m_si.cost_price) AS total_attach_num_amount " );
         // Luc end edit 20150717 #38264
        strSQL.append( "    FROM data_slip_store d_ss , " );
        strSQL.append( "   data_slip_store_detail d_ssd " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id AND m_si.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
        strSQL.append( "    WHERE d_ss.shop_id = d_ssd.shop_id " );
        strSQL.append( "   AND d_ss.slip_no = d_ssd.slip_no " );
        strSQL.append( "   AND d_ss.supplier_id = msi.supplier_id " );
        strSQL.append( "   AND d_ss.shop_id ="+ shopId +" " );
        //IVS_LVTu start add New request #42532
        strSQL.append( " AND d_ss.ship_slip_no is null  \n");
        //IVS_LVTu end add New request #42532
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_ss.store_date BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND d_ss.store_date <=  " + toDate +  " " );
        }
        strSQL.append( "   AND d_ssd.delete_date IS NULL " );
        strSQL.append( "   AND d_ss.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        strSQL.append( "   AND d_ss.slip_no NOT IN " );
        strSQL.append( "     ( SELECT dss.slip_no " );
        strSQL.append( "   FROM data_move_stock dms " );
        strSQL.append( "   INNER JOIN data_slip_store dss ON dms.dest_shop_id = dss.shop_id " );
        strSQL.append( "   AND dms.dest_slip_no = dss.slip_no " );
        strSQL.append( "   WHERE dms.dest_shop_id ="+ shopId +" " );

        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND dss.store_date BETWEEN  " + fromDate + " AND " + toDate + " ) " );
        }
        else {
            strSQL.append( "     AND dss.store_date <=  " + toDate + " ) " );
        }
        //業務用及び店販用
        strSQL.append( "   AND d_ssd.item_use_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_ss.shop_id " );
        strSQL.append( "    ) temp2 ON temp2.shop_id = msh.shop_id " );
        //販売個数&販売金額(原価x売数)
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "   (SELECT d_ssh.shop_id, " );
        // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_sshd.out_num,0)) AS total_out_num, sum(coalesce(d_sshd.out_num,0)*m_si.cost_price) AS total_out_num_amount," );
        // Luc end edit 20150717 #38264
        strSQL.append( "     sum(sign(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_cost_no_tax " );
        strSQL.append( "    FROM data_slip_ship d_ssh , " );
        strSQL.append( "   data_slip_ship_detail d_sshd " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
        strSQL.append( "    WHERE d_ssh.shop_id = d_sshd.shop_id " );
        strSQL.append( "   AND d_ssh.slip_no = d_sshd.slip_no " );
        strSQL.append( "   AND d_ssh.supplier_id = msi.supplier_id " );
        strSQL.append( "   AND d_ssh.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND d_ssh.ship_date <= " + toDate +  " " );
        }
        strSQL.append( "   AND d_sshd.delete_date IS NULL " );
        strSQL.append( "   AND d_ssh.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        strSQL.append( "   AND d_ssh.slip_no NOT IN " );
        strSQL.append( "     ( SELECT dssh.slip_no " );
        strSQL.append( "   FROM data_move_stock dms " );
        strSQL.append( "   INNER JOIN data_slip_ship dssh ON dms.src_shop_id = dssh.shop_id " );
        strSQL.append( "   AND dms.src_slip_no = dssh.slip_no " );
        strSQL.append( "   WHERE dms.src_shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "     AND dssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " ) " );
        }
        else {
            strSQL.append( "     AND dssh.ship_date <=  " + toDate +  " ) " );
        }
        //業務用及び店販用
        strSQL.append( "   AND d_sshd.item_use_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_ssh.shop_id " );
        strSQL.append( "    ) temp3 ON temp3.shop_id = msh.shop_id " );
        //販売個数&販売金額(原価x売数)
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "   (SELECT ds.shop_id , " );
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(dsd.product_num,0)) AS total_out_num2, sum(coalesce(dsd.product_num,0)*m_si.cost_price) AS total_out_num2_amount," );
         // Luc end edit 20150717 #38264
        strSQL.append( "     sum(sign(coalesce(dsd.product_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(ds.sales_date))) * ceil(abs(coalesce(dsd.product_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(ds.sales_date))))) AS total_out_cost_no_tax2 " );
        strSQL.append( "    FROM data_sales ds , " );
        strSQL.append( "   data_sales_detail dsd " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = dsd.product_id AND m_si.delete_date IS NULL " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
        strSQL.append( "    WHERE ds.shop_id = dsd.shop_id " );
        strSQL.append( "   AND ds.slip_no = dsd.slip_no " );
        strSQL.append( "   AND ds.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND ds.sales_date BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND ds.sales_date <=  " + toDate +  " " );
        }
        strSQL.append( "   AND ds.delete_date IS NULL " );
        strSQL.append( "   AND dsd.delete_date IS NULL " );
        strSQL.append( "   AND m_si.delete_date IS NULL " );
        strSQL.append( "   AND dsd.product_division IN (2) " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        strSQL.append( "    GROUP BY ds.shop_id " );
        strSQL.append( "    ) temp4 ON temp4.shop_id = msh.shop_id " );
        //移動個数&移動金額
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "   (SELECT d_ss.shop_id, " );
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_ssd.in_num,0)) AS total_in_num_plus, sum(coalesce(d_ssd.in_num,0)*m_si.cost_price) AS total_in_num_plus_amount, " );
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(sign(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))) * ceil(abs(coalesce(d_ssd.in_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ss.store_date))))) AS total_in_plus_cost_no_tax " );
        strSQL.append( "    FROM data_slip_store d_ss, " );
        strSQL.append( "   data_slip_store_detail d_ssd " );
        // IVS_LVTu start edit 2014/09/30 MASHU_棚卸表
        //strSQL.append( "    INNER JOIN data_move_stock dms ON dms.dest_shop_id = d_ssd.shop_id and dms.dest_slip_no = d_ssd.slip_no and dms.item_id  = d_ssd.item_id " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ssd.item_id  AND m_si.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id and msi.delete_date IS NULL " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
        strSQL.append( "    WHERE d_ss.shop_id = d_ssd.shop_id " );
        strSQL.append( "   AND d_ss.slip_no = d_ssd.slip_no " );
        //strSQL.append( "   AND d_ss.supplier_id = msi.supplier_id " );
        strSQL.append( "   AND d_ss.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_ss.store_date BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND d_ss.store_date <=  " + toDate + " " );
        }
        strSQL.append( "   AND d_ssd.delete_date IS NULL " );
        strSQL.append( "   AND d_ss.delete_date IS NULL " );
        //strSQL.append( "   AND dms.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        strSQL.append( "   AND d_ss.slip_no in ( select dest_slip_no from data_move_stock where dest_shop_id = "+ shopId + " ) " );
        //業務用及び店販用
        strSQL.append( "   AND d_ssd.item_use_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_ss.shop_id " );
        strSQL.append( "    ) temp22 ON temp22.shop_id = msh.shop_id " );
        strSQL.append( "  LEFT JOIN " );
        strSQL.append( "   (SELECT d_ssh.shop_id, " );
        // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_sshd.out_num,0)) AS total_out_num_minus,sum(coalesce(d_sshd.out_num,0)*m_si.cost_price) AS total_out_num_minus_amount, " );
        // Luc start edit 20150717 #38264
        strSQL.append( "     sum(sign(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_minus_cost_no_tax " );
        strSQL.append( "    FROM data_slip_ship d_ssh, " );
        strSQL.append( "   data_slip_ship_detail d_sshd " );
        //strSQL.append( "    INNER JOIN data_move_stock dms ON dms.src_shop_id = d_sshd.shop_id and dms.src_slip_no = d_sshd.slip_no and dms.item_id  = d_sshd.item_id " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id " );
        strSQL.append( "    AND m_it.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id " );
        strSQL.append( "    WHERE d_ssh.shop_id = d_sshd.shop_id " );
        strSQL.append( "   AND d_ssh.slip_no = d_sshd.slip_no " );
        //strSQL.append( "   AND d_ssh.supplier_id = msi.supplier_id " );
        strSQL.append( "   AND d_ssh.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND d_ssh.ship_date <=  " + toDate +" " );
        }
        strSQL.append( "   AND d_sshd.delete_date IS NULL " );
        strSQL.append( "   AND d_ssh.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        strSQL.append( "   AND d_ssh.slip_no in ( select src_slip_no from data_move_stock where src_shop_id = "+ shopId + " ) " );
        // IVS_LVTu end edit 2014/09/30 MASHU_棚卸表
        //業務用及び店販用
        strSQL.append( "   AND d_sshd.item_use_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_ssh.shop_id " );
        strSQL.append( "    ) temp33 ON temp33.shop_id = msh.shop_id " );
        //業務使用個数&業務使用金額
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "    (SELECT d_ssh.shop_id, " );
        // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_sshd.out_num,0)) AS total_out_num_12,sum(coalesce(d_sshd.out_num,0)*m_si.cost_price) AS total_out_num_12_amount, " );
        // Luc start edit 20150717 #38264
        strSQL.append( "     sum(sign(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))) * ceil(abs(coalesce(d_sshd.out_num,0) * coalesce(m_si.cost_price,0) / (1.0 + get_tax_rate(d_ssh.ship_date))))) AS total_out_cost_no_tax_12 " );
        strSQL.append( "    FROM data_slip_ship d_ssh , " );
        strSQL.append( "   data_slip_ship_detail d_sshd " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_sshd.item_id AND m_si.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_supplier msi ON m_si.supplier_id = msi.supplier_id " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id AND m_it.delete_date IS NULL " );
        strSQL.append( "    WHERE d_ssh.shop_id = d_sshd.shop_id " );
        strSQL.append( "   AND d_ssh.slip_no = d_sshd.slip_no " );
        strSQL.append( "   AND d_ssh.supplier_id = msi.supplier_id " );
        strSQL.append( "   AND d_ssh.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_ssh.ship_date BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND d_ssh.ship_date <=  " + toDate + " " );
        }
        strSQL.append( "   AND d_sshd.delete_date IS NULL " );
        strSQL.append( "   AND d_ssh.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        //業務用及び店販用
        strSQL.append( "   AND d_sshd.item_use_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_ssh.shop_id " );
        strSQL.append( "    ) temp5 ON temp5.shop_id = msh.shop_id " );
        //実在庫数&棚卸し残高
        strSQL.append( " LEFT JOIN " );
        strSQL.append( "   (SELECT d_iv.shop_id, " );
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(coalesce(d_ivd.real_stock,0)) as total_real_stock_num, sum(coalesce(d_ivd.real_stock,0)*m_si.cost_price) as total_real_stock_num_amount," );
         // Luc start edit 20150717 #38264
        strSQL.append( "     sum(sign(coalesce(d_ivd.real_stock,0) * coalesce(d_ivd.cost_price,0) / (1.0 + get_tax_rate(d_iv.inventory_date))) * ceil(abs(coalesce(d_ivd.real_stock,0) * coalesce(d_ivd.cost_price,0) / (1.0 + get_tax_rate(d_iv.inventory_date))))) AS total_real_stock_cost_no_tax " );
        strSQL.append( "    FROM data_inventory d_iv , " );
        strSQL.append( "   data_inventory_detail d_ivd " );
        strSQL.append( "    INNER JOIN mst_supplier_item m_si ON m_si.item_id = d_ivd.item_id " );
        strSQL.append( "    AND m_si.delete_date IS NULL " );
        strSQL.append( "    LEFT JOIN mst_item m_it ON m_it.item_id = m_si.item_id " );
        strSQL.append( "    AND m_it.delete_date IS NULL " );
        strSQL.append( "    INNER JOIN mst_item_class mic ON m_it.item_class_id = mic.item_class_id " );
        strSQL.append( "    WHERE d_iv.inventory_id = d_ivd.inventory_id " );
        strSQL.append( "   AND d_iv.inventory_division = d_ivd.inventory_division " );
        strSQL.append( "   AND d_iv.shop_id ="+ shopId +" " );
        if( ((DateRange)inventoryPeriod.getSelectedItem()).getFrom() != null ){
            strSQL.append( "   AND d_iv.inventory_date  BETWEEN  " + fromDate + " AND " + toDate + " " );
        }
        else {
            strSQL.append( "   AND d_iv.inventory_date  <=  " + toDate + " " );
        }
        strSQL.append( "   AND d_ivd.delete_date IS NULL " );
        strSQL.append( "   AND d_iv.delete_date IS NULL " );
        if( type == 1) {
           strSQL.append( "   AND m_it.item_class_id = 9 " );
        }
        else {
            strSQL.append( "   AND m_it.item_class_id = 61 " );
        } 
        //業務用及び店販用
        strSQL.append( "   AND d_iv.inventory_division IN (1,2) " );
        strSQL.append( "    GROUP BY d_iv.shop_id " );
        strSQL.append( "    ) temp6 ON temp6.shop_id = msh.shop_id " );
        strSQL.append( " where msh.shop_id ="+ shopId +" " );
        
         return strSQL.toString();
    }
    
    // IVS_LVTu end add 2014/09/11 MASHU_棚卸表
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        shopLabel = new javax.swing.JLabel();
        lblInventryPeriod = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        inventoryPeriod = new javax.swing.JComboBox();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        btnOutputPdf = new javax.swing.JButton();
        btnOutputExcel = new javax.swing.JButton();

        shopLabel.setText("店舗");

        lblInventryPeriod.setText("棚卸期間");

        lblTax.setText("税区分");

        buttonGroup1.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("税込");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);

        buttonGroup1.add(rdoTaxBlank);
        rdoTaxBlank.setText("税抜");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);

        inventoryPeriod.setMaximumRowCount(20);
        inventoryPeriod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        shop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });

        btnOutputPdf.setIcon(SystemInfo.getImageIcon("/button/print/output_pdf_off.jpg"));
        btnOutputPdf.setBorderPainted(false);
        btnOutputPdf.setContentAreaFilled(false);
        btnOutputPdf.setPressedIcon(SystemInfo.getImageIcon("/button/print/output_pdf_on.jpg"));
        btnOutputPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputPdfActionPerformed(evt);
            }
        });

        btnOutputExcel.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutputExcel.setBorderPainted(false);
        btnOutputExcel.setContentAreaFilled(false);
        btnOutputExcel.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutputExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInventryPeriod)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shopLabel)
                            .addComponent(lblTax))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdoTaxUnit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoTaxBlank, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(inventoryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                                .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblInventryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inventoryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTax, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTaxUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTaxBlank, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnOutputPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOutputExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
	private void btnOutputExcelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputExcelActionPerformed
	{//GEN-HEADEREND:event_btnOutputExcelActionPerformed
            //IVS_LVTu start add 2014/12/02 Mashu_New request #33431
            btnOutputExcel.setCursor(null);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //IVS_LVTu end add 2014/12/02 Mashu_New request #33431
            this.callReportGeneratorLogic( TotalInventoryReportLogic.EXPORT_FILE_XLS );
            //IVS_LVTu start add 2014/12/02 Mashu_New request #33431
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            //IVS_LVTu end add 2014/12/02 Mashu_New request #33431
	}//GEN-LAST:event_btnOutputExcelActionPerformed
        
	private void btnOutputPdfActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOutputPdfActionPerformed
	{//GEN-HEADEREND:event_btnOutputPdfActionPerformed
            //IVS_LVTu start add 2014/12/02 Mashu_New request #33431
            btnOutputPdf.setCursor(null);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //IVS_LVTu end add 2014/12/02 Mashu_New request #33431
            this.callReportGeneratorLogic( TotalInventoryReportLogic.EXPORT_FILE_PDF );
            //IVS_LVTu start add 2014/12/02 Mashu_New request #33431
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            //IVS_LVTu end add 2014/12/02 Mashu_New request #33431
	}//GEN-LAST:event_btnOutputPdfActionPerformed
        
    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed
        initPeriod();
    }//GEN-LAST:event_shopActionPerformed
        //初期化
    private void init() {
        this.initPeriod();
    }
    
    //棚卸期間
    private void initPeriod() {
        try {
            ConnectionWrapper cw = SystemInfo.getConnection();
            inventoryPeriod.removeAllItems();
            MstShop		ms	=	(MstShop)shop.getSelectedItem();
            ib.setcutoffday(ms.getCutoffDay());
            ib.setShop(ms.getShopID());
            for(DateRange obj : ib.getInventrydate(cw, false)) {
                inventoryPeriod.addItem(obj);
            }
            
            if(inventoryPeriod.getItemCount() > 0) {
                inventoryPeriod.setSelectedIndex(0);
            }
            
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(1099),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnOutputPdf);
        SystemInfo.addMouseCursorChange(btnOutputExcel);
    }
    
    private void setKeyListener() {
        shop.addKeyListener(SystemInfo.getMoveNextField());
        shop.addFocusListener(SystemInfo.getSelectText());
        inventoryPeriod.addKeyListener(SystemInfo.getMoveNextField());
        inventoryPeriod.addFocusListener(SystemInfo.getSelectText());
        rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
        rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutputExcel;
    private javax.swing.JButton btnOutputPdf;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox inventoryPeriod;
    private javax.swing.JLabel lblInventryPeriod;
    private javax.swing.JLabel lblTax;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JLabel shopLabel;
    // End of variables declaration//GEN-END:variables
    
    // <editor-fold defaultstate="collapsed" desc=" LocalFocusTraversalPolicy Code">
    private LocalFocusTraversalPolicy   ftp;
    /**
     * FocusTraversalPolicyを取得する。
     * @return FocusTraversalPolicy
     */
    public LocalFocusTraversalPolicy getFocusTraversalPolicy()
    {
        return  ftp;
    }
    
    /**
     * FocusTraversalPolicy
     */
    private class LocalFocusTraversalPolicy
                    extends FocusTraversalPolicy
    {
        ArrayList<Component> controls = new ArrayList<Component>();
        public LocalFocusTraversalPolicy()
        {
            controls.add(shop);
            controls.add(inventoryPeriod);
            controls.add(rdoTaxUnit);
            controls.add(rdoTaxBlank);

            // 最後に先頭を再度登録(同時にEnabledがFalseにならないところまで重複登録)
            controls.add(shop);
            controls.add(inventoryPeriod);
        };
            
        /**
         * aComponent のあとでフォーカスを受け取る Component を返します。
         */
        public Component getComponentAfter(Container aContainer,
                                           Component aComponent)
        {
            boolean find = false;
            for(Component co : controls){
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }

        /**
         * aComponent の前にフォーカスを受け取る Component を返します。
         */
        public Component getComponentBefore(Container aContainer,
                                            Component aComponent)
        {
            boolean find = false;
            for( int ii = controls.size(); ii>0; ii-- ){
                Component co = controls.get(ii-1);
                if( find ){
                    if( co.isEnabled() ){
                        return co;
                    }
                } else if (aComponent.equals(co)){
                    find = true;
                }
            }
            return null;
        }

        /**
         * トラバーサルサイクルの最初の Component を返します。
         */
        public Component getFirstComponent(Container aContainer)
        {
            return getDefaultComponent(aContainer);
        }

        /**
         * トラバーサルサイクルの最後の Component を返します。
         */
        public Component getLastComponent(Container aContainer)
        {
            return getComponentBefore(aContainer, controls.get(0));
        }
        
        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。
         */
        public Component getDefaultComponent(Container aContainer)
        {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
        
        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
         */
        public Component getInitialComponent(Window window)
        {
            for(Component co : controls){
                if( co.isEnabled() ){
                    return co;
                }
            }
            return controls.get(0);
        }
    }
    // </editor-fold>
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.beans;

import com.geobeck.sosia.pos.customer.InventoryManagementCustomerData;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author tmtrong
 */
public class ItemDeliveryHistoryListBean extends ArrayList<ItemDeliveryHistoryBean> {

    private String addDateFrom = null;
    private String addDateTo = null;
    private Integer shopID = null;
    private String shopIDList = null;

      /**
     * @return the addDateFrom
     */
    public String getAddDateFrom() {
        return addDateFrom;
    }

    /**
     * @param addDateFrom the addDateFrom to set
     */
    public void setAddDateFrom(String addDateFrom) {
        this.addDateFrom = addDateFrom;
    }

    /**
     * @return the addDateTo
     */
    public String getAddDateTo() {
        return addDateTo;
    }

    /**
     * @param addDateTo the addDateTo to set
     */
    public void setAddDateTo(String addDateTo) {
        this.addDateTo = addDateTo;
    }

    /**
     * @return the shopID
     */
    public Integer getShopID() {
        return shopID;
    }

    /**
     * @param shopID the shopID to set
     */
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    /**
     * @return the shopIDList
     */
    public String getShopIDList() {
        return shopIDList;
    }

    /**
     * @param shopIDList the shopIDList to set
     */
    public void setShopIDList(String shopIDList) {
        this.shopIDList = shopIDList;
    }

    public boolean ItemDeliveryHistoryExport() {
      this.clear();
      try {
          ConnectionWrapper con = SystemInfo.getConnection();
          ResultSetWrapper rs = con.executeQuery(this.getItemDeliveryHistoryExportSQL());
          while (rs.next()) {
              ItemDeliveryHistoryBean mmd = new ItemDeliveryHistoryBean();
              mmd.setData(rs);
              this.add(mmd);
          }
          rs.close();
      } catch (SQLException e) {
          SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
          return false;
      }
      return true;
    }

    private String getItemDeliveryHistoryExportSQL() {
       StringBuilder sql = new StringBuilder(1000);
          sql.append(" SELECT "); //20190302 （７）商品受渡で出庫した数が正しく反映されていない問題 コメントアウト⇒ DISTINCT ");
              sql.append(" ds.shop_id ");
              sql.append(" ,to_char(pp2.operation_date,'YYYY/MM/dd') AS operation_date");
              sql.append(" ,c.customer_no ");
              sql.append(" ,COALESCE(c.customer_name1, '') || COALESCE(c.customer_name2, '') AS customer_name ");
              sql.append(" ,p.product_name ");
              sql.append(" ,pp2.product_num ");
              sql.append(" ,pp2.delivery_num ");
              sql.append(" ,pp2.staffname ");
              sql.append(" ,ds.sales_date ");
          sql.append(" FROM ( ");
              sql.append(" SELECT ds.sales_date ");
                  sql.append(" ,dsd.slip_no ");
                  sql.append(" ,dsd.slip_detail_no ");
                  sql.append(" ,ds.shop_id ");
                  sql.append(" ,dsd.product_id ");
                  sql.append(" ,sum(dsd.product_num)::INT AS product_num ");
                  sql.append(" ,ds.customer_id ");
                  sql.append(" ,dsd.product_division ");
              sql.append(" FROM data_sales ds ");
              sql.append(" INNER JOIN data_sales_detail dsd ON ds.slip_no = dsd.slip_no ");
                  sql.append(" AND ds.shop_id = dsd.shop_id ");
                  sql.append(" WHERE dsd.product_division = 2 ");
                  sql.append(" AND ds.delete_date IS NULL ");
                  //sql.append(" AND ds.sales_date >= "+SQLUtil.convertForSQL(getAddDateFrom()));
                  sql.append(" AND ds.sales_date <= "+SQLUtil.convertForSQL(getAddDateTo()));
                  sql.append(" AND dsd.delete_date IS NULL ");
                  sql.append(" GROUP BY dsd.slip_no ");
                  sql.append(" ,dsd.slip_detail_no ");
                  sql.append(" ,ds.sales_date ");
                  sql.append(" ,ds.shop_id ");
                  sql.append(" ,dsd.product_id ");
                  sql.append(" ,ds.customer_id ");
                  sql.append(" ,dsd.product_division ");
              sql.append(" ) ds ");
              sql.append(" INNER JOIN mst_customer c ON ds.customer_id = c.customer_id ");
              sql.append(" INNER JOIN view_mst_product p ON ds.product_id = p.product_id ");
              sql.append(" AND p.product_division = ds.product_division ");
              sql.append(" LEFT JOIN ( ");
                  sql.append(" SELECT pp2.shop_id ");
                  sql.append(" ,slip_no ");
                  sql.append(" ,slip_detail_no ");
                  sql.append(" ,SUM(pp2.product_num)::INT AS product_num ");
                  sql.append(" ,COALESCE(staff.staff_name1,' ') || COALESCE(staff.staff_name2,' ') AS staffname ");
                  sql.append(" ,operation_date ");
                  sql.append(" ,delivery_num ");
              sql.append(" FROM data_pickup_product pp2 inner join mst_staff staff ");
                  sql.append(" on pp2.staff_id = staff.staff_id ");
              sql.append(" WHERE STATUS <> 1 ");
                  sql.append(" AND pp2.operation_date between "+SQLUtil.convertForSQL(getAddDateFrom()) +" and "+SQLUtil.convertForSQL(getAddDateTo()));
                  sql.append(" AND pp2.delete_date IS NULL ");
                  sql.append(" AND staff.delete_date IS NULL ");
                  sql.append(" AND COALESCE(pp2.product_num, 0) > 0 ");
                  sql.append(" GROUP BY pp2.shop_id ");
                  sql.append(" ,slip_no ");
                  sql.append(" ,slip_detail_no, ");
                  sql.append(" COALESCE(staff.staff_name1,' ') || COALESCE(staff.staff_name2,' ') ");
                  sql.append(" ,operation_date ");
                  sql.append(" ,delivery_num ");
                  sql.append(" ,product_num ");
              sql.append(" ) AS pp2 ON ds.shop_id = pp2.shop_id ");
              sql.append(" AND ds.slip_no = pp2.slip_no ");
              sql.append(" AND ds.slip_detail_no = pp2.slip_detail_no ");
             sql.append(" WHERE COALESCE(ds.product_num, 0) > 0 AND COALESCE(pp2.product_num, 0) >0 ");
              if(this.getShopID()!=0)
              {
                  sql.append(" and ds.shop_id = " +  SQLUtil.convertForSQL(this.getShopID()));
              }
              else{
                   sql.append(" and ds.shop_id in  (" + this.getShopIDList() + ")\n" );
              }
          sql.append(" ORDER BY operation_date DESC");

       return sql.toString();
    }
}

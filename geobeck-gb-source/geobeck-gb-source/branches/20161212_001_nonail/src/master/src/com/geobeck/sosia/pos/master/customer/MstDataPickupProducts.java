/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.customer;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;

/**
 *
 * @author ivs
 */
public class MstDataPickupProducts extends ArrayList<MstDataPickupProduct> {

    private Integer customerId;
    private Integer slipNo = null;
    private Integer shopId = null;

    public Integer getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    
    public void load(ConnectionWrapper con) throws Exception {
        ResultSetWrapper rs = con.executeQuery(getSelectSQL());
        this.clear();
        while (rs.next()) {
            MstDataPickupProduct dpp = new MstDataPickupProduct();
            //  dpp.setDataForReturnGoods(rs);
            dpp.setDataDelivery(rs);
            this.add(dpp);
        }
    }

    public boolean regist(ConnectionWrapper con) {
        boolean flg = true;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getPickupProductNum() >= 0) {
                continue;
            }
            if (!this.get(i).registHin(con)) {
                flg = false;
            }

        }
        return flg;
    }

    public String getSelectSQL() {
        String sql = "";
        sql += "select ds.shop_id , ds.slip_no , dsd.slip_detail_no, ds.customer_id, \n"
                + " ds.sales_date, dsd.product_id , wp.product_name \n"
                + ", wp.price \n "
                + ",sum(dsd.product_num) as sales_num \n"
                + ", dpd.pick_num \n"
                + ",ms.staff_id, ms.staff_name1,ms.staff_name2 \n "
                + "from data_sales ds \n"
                + "inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no \n "
                + "inner join view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n"
                + "  left join ( " + "\n"
                + "             select slip_detail_no, sum(product_num):: INT as pick_num" + "\n"
                + "              from data_pickup_product" + "\n"
                + "             where slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";

               sql += "             and shop_id  =" + SQLUtil.convertForSQL(this.shopId) + "\n";
               sql += "             and delete_date is null" + "\n"
                + "             group by slip_detail_no" + "\n"
                + ") dpd on dpd.slip_detail_no= dsd.slip_detail_no" + "\n"
                + "inner join mst_staff ms on ms.staff_id = ds.staff_id  " + "\n"
                + "where ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomerId()) + "\n";
                sql += "and ds.shop_id =" + SQLUtil.convertForSQL(this.shopId) + "\n";
        
                sql += "and dsd.product_division = 2" + "\n"
                + "and ds.delete_date is null" + "\n"
                + "group by ds.sales_date, dsd.product_id, wp.product_name, wp.price ,ms.staff_id, ms.staff_name1,ms.staff_name2"
                + "   ,dpd.pick_num ,ds.shop_id , ds.slip_no , dsd.slip_detail_no, ds.customer_id \n"
                + "   ORDER BY ds.sales_date DESC   , dsd.slip_detail_no \n";
        return sql;

    }
}

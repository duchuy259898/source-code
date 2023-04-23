/*
 * InventoryManagementCustomerList.java
 *
 * Created on 2013/03/06, 09:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author ivs_tttung
 */
public class InventoryManagementCustomerList extends ArrayList<InventoryManagementCustomerData> {

    private String sosiaCode = null;
    private String addDateFrom = null;
    private String addDateTo = null;
    private Integer gearCondition = null;
    private Integer shopID = null;
    private String customerNo = null;
    private String shopIDList = null;
    private String credit_name = null;

    public String getSosiaCode() {
        return sosiaCode;
    }

    public void setSosiaCode(String sosiaCode) {
        this.sosiaCode = sosiaCode;
    }

    public String getAddDateFrom() {
        return addDateFrom;
    }

    public void setAddDateFrom(String addDateFrom) {
        this.addDateFrom = addDateFrom;
    }

    public String getAddDateTo() {
        return addDateTo;
    }

    public void setAddDateTo(String addDateTo) {
        this.addDateTo = addDateTo;
    }

    public Integer getGearCondition() {
        return gearCondition;
    }

    public void setGearCondition(Integer gearCondition) {
        this.gearCondition = gearCondition;
    }

    // Khoa add Start 20121105 ispot‰ïˆõˆê——
    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getShopIDList() {
        return shopIDList;
    }

    public void setShopIDList(String shopIDList) {
        this.shopIDList = shopIDList;
    }

    /**
     * @return the credit_name
     */
    public String getCredit_name() {
        return credit_name;
    }

    /**
     * @param credit_name the credit_name to set
     */
    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    public InventoryManagementCustomerList() {
    }

    public boolean load(Integer flag, Integer customer_id) {
        this.clear();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getCustomerCreditRegistMemberList(flag, customer_id));
            while (rs.next()) {
                InventoryManagementCustomerData mmd = new InventoryManagementCustomerData();
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

    /**
     * ‰æ–Ê‚É‚ÄŒŸõðŒ‚ði‚èž‚Ý‚µIspot‰ïˆõŒŸõAˆê——•\Ž¦
     *
     * @param flag
     * @return
     */
    private String getCustomerCreditRegistMemberList(Integer flag, Integer customer_id) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select distinct ds.slip_no,ds.slip_detail_no, ds.shop_id, ds.sales_date,  c.customer_id,  c.customer_no,\n");
        //IVS_LVTu start edit 2015/06/19 Bug #37674
        //sql.append(" c.customer_name1 || c.customer_name2 as customer_name,  p.product_name,  ds.product_num as sp_product_num,  \n");
        sql.append(" COALESCE(c.customer_name1, '') || COALESCE(c.customer_name2, '') as customer_name,  p.product_name,  ds.product_num as sp_product_num,  \n");
        //IVS_LVTu end edit 2015/06/19 Bug #37674
        sql.append(" ds.product_value,  pp2.product_num as pp_product_num,  ds.product_num_used,p.price  \n");
        sql.append(" from \n");
        sql.append(" (\n");
        sql.append("     select  ds.sales_date,dsd.slip_no,dsd.slip_detail_no,ds.shop_id,dsd.product_id ,sum(dsd.product_num)::INT as product_num,\n");
        sql.append("             sum(dsd.product_num) as product_num_used,sum(dsd.product_value) as product_value,ds.customer_id, dsd.product_division  \n");
        sql.append("     from   data_sales ds\n");
        sql.append("     INNER JOIN data_sales_detail dsd on ds.slip_no=dsd.slip_no and ds.shop_id=dsd.shop_id   \n");
        sql.append("     where   dsd.product_division =2 and ds.delete_date is null and ds.sales_date >= "+SQLUtil.convertForSQL(addDateFrom) +"     and ds.sales_date <="+SQLUtil.convertForSQL(addDateTo) +"\n");
        //IVS_LVTu start add 2015/06/18 Bug #37642
        sql.append("     AND dsd.delete_date IS NULL \n");
        //IVS_LVTu end add 2015/06/18 Bug #37642
        sql.append("     group by dsd.slip_no,dsd.slip_detail_no,ds.sales_date,ds.shop_id,dsd.product_id ,ds.customer_id, dsd.product_division \n");
        sql.append(" )ds \n");
        sql.append(" INNER JOIN mst_customer c on ds.customer_id =c.customer_id\n");
        sql.append(" INNER JOIN   view_mst_product p on ds.product_id=p.product_id and p.product_division = ds.product_division \n");
        sql.append(" LEFT JOIN (\n");
        sql.append("     SELECT shop_id,slip_no,slip_detail_no,SUM(pp2.product_num)::INT AS product_num \n");
        sql.append("     FROM data_pickup_product pp2 \n");
        sql.append("     WHERE status <>1  and delete_date is null \n");
        sql.append("     GROUP BY shop_id,slip_no,slip_detail_no\n");
        sql.append(" ) AS pp2 ON  ds.shop_id=pp2.shop_id AND ds.slip_no=pp2.slip_no AND ds.slip_detail_no=pp2.slip_detail_no  \n");
        if ((this.customerNo!=null && !this.customerNo.equals("")) || flag == 0 || flag == 1) {
       //     sql.append(" where \n");
            if (flag == 0) {
                sql.append("where  COALESCE( ds.product_num,0)= COALESCE(pp2.product_num,0)\n");
                if ((!this.customerNo.equals("")&&this.customerNo!=null)) {
                    sql.append(" AND c.customer_no=" + SQLUtil.convertForSQL(this.getCustomerNo()) + "\n");
                }
            } else if (flag == 1) {
                sql.append("where  COALESCE( ds.product_num,0)> COALESCE( pp2.product_num,0)\n");
                if ((!this.customerNo.equals("")&&this.customerNo!=null) ) {
                    sql.append(" AND c.customer_no=" + SQLUtil.convertForSQL(this.getCustomerNo()) + "\n");

                }
                
            }else {
                if ((!this.customerNo.equals("")&&this.customerNo!=null)) {
                    sql.append(" where   c.customer_no=" + SQLUtil.convertForSQL(this.getCustomerNo()) + "\n");
                }
            }
        } else {
			 sql.append(" where 1=1 ");
		}
        if(this.getShopID() !=0)
        {
            sql.append(" and ds.shop_id = " +  SQLUtil.convertForSQL(this.getShopID()));
        }
         // vtbphuong start add 20140502 Request #22768
        else{
             sql.append(" and ds.shop_id in  (" + this.getShopIDList() + ")\n" );
        }
        // vtbphuong end add 20140502 Request #22768
        sql.append(" ORDER BY ds.sales_date DESC");

		SystemInfo.getLogger().info("[IMCL]" + sql.toString());
		return sql.toString();
    }
}
 
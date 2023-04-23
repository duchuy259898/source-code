/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.customer;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author nakhoa
 */
public class MstDataPickupProduct {

    /**
     * 店舗
     */
    protected MstShop shop = new MstShop();
    /**
     * 伝票No.
     */
    protected Integer slipNo = null;
    /**
     * 伝票明細No.
     */
    protected Integer slipDetailNo = null;
    /*
     * 受渡商品詳細No.
     */
    protected Integer pickupProductNo = null;
    /**
     * 数量
     */
    protected Integer productNum = null;
    protected Integer pickupProductNum = null;
    protected Integer productNumUse = null;
    protected String productName = "";
    protected Long productValue = null;
    /**
     * 処理日
     */
    protected java.util.Date operationDate = null;
    protected java.util.Date salesDate = null;
    /**
     * 処理内容
     */
    protected Integer status = null;
    /**
     * スタッフ
     */
    protected MstStaff staff = new MstStaff();
    /**
     * 顧客
     */
    protected MstCustomer customer = new MstCustomer();

    /**
     * 店舗を取得する。
     *
     * @return 店舗
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * 店舗をセットする。
     *
     * @param shop 店舗
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * 伝票No.
     *
     * @return 伝票No.
     */
    public Integer getSlipNo() {
        return slipNo;
    }

    /**
     * 伝票No.
     *
     * @param slipNo 伝票No.
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * 伝票明細No.
     *
     * @return 伝票明細No.
     */
    public Integer getSlipDetailNo() {
        return slipDetailNo;
    }

    /**
     * 伝票明細No.
     *
     * @param slipDetailNo 伝票明細No.
     */
    public void setSlipDetailNo(Integer slipDetailNo) {
        this.slipDetailNo = slipDetailNo;
    }

    /**
     * 受渡商品詳細No.
     *
     * @return 受渡商品詳細No.
     */
    public Integer getPickupProductNo() {
        return pickupProductNo;
    }

    /**
     * 受渡商品詳細No.
     *
     * @param pickupProductNo 受渡商品詳細No.
     */
    public void setPickupProductNo(Integer pickupProductNo) {
        this.pickupProductNo = pickupProductNo;
    }

    /**
     * 数量
     *
     * @return 数量
     */
    public Integer getProductNum() {
        return productNum;
    }

    /**
     * 数量
     *
     * @param productNum 数量
     */
    public Integer getPickupProductNum() {
        return pickupProductNum;
    }

    public void setPickupProductNum(Integer pickupProductNum) {
        this.pickupProductNum = pickupProductNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public Integer getProductNumUse() {
        return productNumUse;
    }

    public void setProductNumUse(Integer productNumUse) {
        this.productNumUse = productNumUse;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductValue() {
        return productValue;
    }

    public void setProductValue(Long productValue) {
        this.productValue = productValue;
    }

    /**
     * 処理内容
     *
     * @return 処理内容
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 処理内容
     *
     * @param status 処理内容
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * スタッフ
     *
     * @return スタッフ
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * スタッフ
     *
     * @param staff スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * 処理日を取得する。
     *
     * @return 処理日
     */
    public java.util.Date getOperationDate() {
        return operationDate;
    }

    /**
     * 処理日をセットする。
     *
     * @param operationDate 処理日
     */
    public void setOperationDate(java.util.Date operationDate) {
        this.operationDate = operationDate;
    }

    /**
     * 処理日を取得する。
     *
     * @return 処理日
     */
    public java.util.Date getSalesDate() {
        return salesDate;
    }

    /**
     * 顧客を取得する。
     *
     * @return 顧客
     */
    public MstCustomer getCustomer() {
        return customer;
    }

    /**
     * 顧客をセットする。
     *
     * @param customer 顧客
     */
    public void setCustomer(MstCustomer customer) {
        this.customer = customer;
    }

    /**
     * 処理日をセットする。
     *
     * @param operationDate 処理日
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate = salesDate;
    }

    public ArrayList<MstDataPickupProduct> load(ConnectionWrapper con, Integer customerId) throws Exception {
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(customerId));
        ArrayList<MstDataPickupProduct> list = new ArrayList<MstDataPickupProduct>();
        MstDataPickupProduct dpp = new MstDataPickupProduct();
        while (rs.next()) {
            dpp = new MstDataPickupProduct();
            dpp.setDataDelivery(rs);
            list.add(dpp);
        }
        return list;
    }

    public ArrayList<MstDataPickupProduct> loadDeliveryProducts(ConnectionWrapper con) throws Exception {
        ResultSetWrapper rs = con.executeQuery(this.getSelectDeliveryProductsSQL());
        ArrayList<MstDataPickupProduct> list = new ArrayList<MstDataPickupProduct>();
        MstDataPickupProduct dpp = new MstDataPickupProduct();
        while (rs.next()) {
            dpp = new MstDataPickupProduct();
            dpp.setDataDeliveryProducts(rs, con);
            list.add(dpp);
        }
        return list;
    }

    /**
     * ResultSetWrapperからデータを取得する。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));
        this.setPickupProductNo(rs.getInt("pickup_product_no"));
        this.setProductNum(rs.getInt("product_num"));
        Double temp = rs.getDouble("product_num_used");
        this.setProductNumUse(temp.intValue());
        this.setStatus(rs.getInt("status"));
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        this.setStaff(ms);
        this.setOperationDate(rs.getTime("operation_date"));
        this.setSalesDate(rs.getDate("sales_date"));
        this.setProductName(rs.getString("product_name"));
        this.setProductValue(rs.getLong("product_value"));
        this.setPickupProductNum(rs.getInt("pickup_product_num"));
        MstCustomer cus = new MstCustomer();
        cus.setCustomerID(rs.getInt("customer_id"));
        this.setCustomer(cus);
    }
    
    public void setDataDelivery(ResultSetWrapper rs) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));
        this.setProductNum(rs.getInt("sales_num"));
        
        this.setProductNumUse(rs.getInt("sales_num") - rs.getInt("pick_num") );
        
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        this.setStaff(ms);
        this.setSalesDate(rs.getDate("sales_date"));
        this.setProductName(rs.getString("product_name"));
        this.setProductValue(rs.getLong("price"));
        this.setPickupProductNum(rs.getInt("pick_num"));
        MstCustomer cus = new MstCustomer();
        cus.setCustomerID(rs.getInt("customer_id"));
        this.setCustomer(cus);
    }
    
    

    /**
     * ResultSetWrapperからデータを取得する。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setDataForReturnGoods(ResultSetWrapper rs) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));
        this.setPickupProductNo(rs.getInt("pickup_product_no"));
        this.setProductNum(rs.getInt("product_num"));
        Double temp = rs.getDouble("product_num_used");
        this.setProductNumUse(temp.intValue());
        this.setStatus(rs.getInt("status"));
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        this.setStaff(ms);
        this.setOperationDate(rs.getTime("operation_date"));
        this.setSalesDate(rs.getDate("sales_date"));
        this.setProductName(rs.getString("product_name"));
        this.setProductValue(rs.getLong("product_value"));
        this.setPickupProductNum(rs.getInt("pickup_product_num"));
    }

    public String getSelectSQL(Integer customerId) {
        String sql="";
        sql = "select ds.shop_id , ds.slip_no , dsd.slip_detail_no, ds.customer_id, \n"
                + " ds.sales_date, dsd.product_id , wp.product_name \n"
                + ", wp.price \n "
                + ",sum(dsd.product_num) as sales_num \n"
                + ",dpd.pick_num \n"
                + ",ms.staff_id, ms.staff_name1,ms.staff_name2 \n "
                + "from data_sales ds \n"
                + "inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no \n "
                + "inner join view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n"
                + "  left join ( "+ "\n"
                + "             select slip_detail_no, sum(product_num):: INT as pick_num"+ "\n"
                + "              from data_pickup_product"+ "\n"
                + "             where slip_no = " + SQLUtil.convertForSQL(this.slipNo) + "\n";
                if(this.getShop().getShopID() !=0)
                {      sql += "             and shop_id  =" + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";}
        
                sql+= "             and delete_date is null"+ "\n"
                + "             group by slip_detail_no"+ "\n"
                + ") dpd on dpd.slip_detail_no= dsd.slip_detail_no"+ "\n"
                //TMTrong start edit 20150630 Bug #38363
                + "left join mst_staff ms on ms.staff_id = ds.staff_id  " + "\n"
                //TMTrong end edit 20150630 Bug #38363
                + "where ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and ds.customer_id = " + SQLUtil.convertForSQL(customerId) + "\n";
                if(this.getShop().getShopID() !=0)
                {sql+= "and ds.shop_id =" + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";}
                sql+= "and dsd.product_division = 2" + "\n"
                + "and ds.delete_date is null" + "\n"
                + "group by ds.sales_date, dsd.product_id, wp.product_name, wp.price ,ms.staff_id, ms.staff_name1,ms.staff_name2"
                + "   ,dpd.pick_num ,ds.shop_id , ds.slip_no , dsd.slip_detail_no ,ds.customer_id  \n"
                + "   ORDER BY ds.sales_date DESC  , dsd.slip_detail_no \n";
         return sql;
    }

    public void setDataDeliveryProducts(ResultSetWrapper rs, ConnectionWrapper con) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));
        this.setPickupProductNo(rs.getInt("pickup_product_no"));
        this.setProductNum(rs.getInt("product_num"));
        this.setStatus(rs.getInt("status"));
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        ms.load(con);
        this.setStaff(ms);
        this.setOperationDate(rs.getTime("operation_date"));
    }

    public String getSelectDeliveryProductsSQL() {
        return "select  pp.shop_id,\n"
                + "pp.slip_no,\n"
                + "pp.slip_detail_no,\n"
                + "pp.pickup_product_no,\n"
                + "pp.operation_date,\n"
                + "pp.product_num ::INT ,\n"
                + "st.staff_id,\n"
                + "pp.status\n"
                + "from mst_staff st,\n"
                + "data_pickup_product pp\n"
                + "where pp.delete_date is null\n"
                + "and st.staff_id = pp.staff_id\n"
                + "and pp.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and pp.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and pp.slip_detail_no =" + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "ORDER BY pp.operation_date ASC ";
    }

    public String getInsertSQL() {
        return "INSERT INTO\n"
                + " data_pickup_product\n"
                + " (\n"
                + " shop_id,\n"
                + " slip_no,\n"
                + " slip_detail_no,\n"
                + " pickup_product_no,\n"
                + " product_num,\n"
                + " operation_date,\n"
                + " status,\n"
                + " staff_id,\n"
                + " insert_date,\n"
                + " update_date\n"
                + " )\n"
                + " VALUES\n"
                + " (\n"
                + this.getShop().getShopID() + ",\n"
                + this.getSlipNo() + " ,\n"
                + this.getSlipDetailNo() + ",\n"
                + "     (select coalesce(max(pickup_product_no), 0) + 1 "
                + "     from data_pickup_product "
                + "     where shop_id=" + this.getShop().getShopID() + "),\n"
                + SQLUtil.convertForSQL(this.getPickupProductNum()) + ",\n"
                + SQLUtil.convertForSQL(this.getOperationDate()) + ",\n"
                + SQLUtil.convertForSQL(this.getStatus()) + ",\n"
                + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n"
                + "current_timestamp, \n"
                + "current_timestamp \n"
                + " )\n";
    }

    public String getInserSalestSQL() {
        return "INSERT INTO\n"
                + " data_pickup_product\n"
                + " (\n"
                + " shop_id,\n"
                + " slip_no,\n"
                + " slip_detail_no,\n"
                + " pickup_product_no,\n"
                + " product_num,\n"
                + " operation_date,\n"
                + " status,\n"
                + " staff_id,\n"
                + " insert_date,\n"
                + " update_date\n"
                + " )\n"
                + " VALUES\n"
                + " (\n"
                + this.getShop().getShopID() + ",\n"
                + this.getSlipNo() + " ,\n"
                + this.getSlipDetailNo() + ",\n"
                + "     (select coalesce(max(pickup_product_no), 0) + 1 "
                + "     from data_pickup_product "
                + "     where shop_id=" + this.getShop().getShopID() + "),\n"
                + SQLUtil.convertForSQL(this.getProductNum()) + ",\n"
                + SQLUtil.convertForSQL(this.getOperationDate()) + ",\n"
                + SQLUtil.convertForSQL(this.getStatus()) + ",\n"
                + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n"
                + "current_timestamp,\n"
                + "current_timestamp \n"
                + " )\n";
    }

    public boolean isExists(ConnectionWrapper con) {
        boolean flg = false;
        try {

            ResultSetWrapper rs = con.executeQuery(this.getExistsSQL());

            if (rs.next()) {
                flg = true;
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getMessage());
        }
        return flg;
    }

    public boolean delete(ConnectionWrapper con) {
        boolean flg = true;
        try {
            con.execute(this.getDeleteSQL());
        } catch (SQLException e) {
            flg = false;
            SystemInfo.getLogger().log(Level.WARNING, e.getMessage());
        }
        return flg;
    }

    private String getExistsSQL() {
        return "SELECT * "
                + "FROM data_pickup_product "
                + "WHERE   shop_id = " + this.getShop().getShopID()
                + " AND slip_no=" + this.slipNo
                + " AND slip_detail_no=" + this.getSlipDetailNo()
                + " AND shop_id=" + this.getShop().getShopID();

    }

    public boolean regist(ConnectionWrapper con) {
        boolean flg = true;
        try {
            if (this.isExists(con)) {
                if (!this.delete(con)) {
                    flg = false;
                }
            }
            if (con.executeUpdate(this.getInsertSQL()) < 1) {
                flg = false;
            }
        } catch (Exception e) {
            flg = false;
        }
        return flg;

    }
    
    
    public boolean registHin(ConnectionWrapper con) {
        boolean flg = true;
        try {
            if (con.executeUpdate(this.getInsertSQL()) < 1) {
                flg = false;
            }
        } catch (Exception e) {
            flg = false;
        }
        return flg;

    }
    

    public boolean registSales(ConnectionWrapper con) {
        boolean flg = true;
        try {
            if (this.isExists(con)) {
                if (!this.delete(con)) {
                    flg = false;
                }
            }
            if (con.executeUpdate(this.getInserSalestSQL()) < 1) {
                flg = false;
            }
        } catch (Exception e) {
            flg = false;
        }
        return flg;

    }

    private String getDeleteSQL() {
        return "UPDATE  "
                + "data_pickup_product "
                + "SET delete_date = now()"
                + "WHERE   shop_id = " + this.getShop().getShopID()
                + " AND slip_no=" + this.slipNo
                + " AND slip_detail_no=" + this.getSlipDetailNo()
                + " AND shop_id=" + this.getShop().getShopID();

    }
}

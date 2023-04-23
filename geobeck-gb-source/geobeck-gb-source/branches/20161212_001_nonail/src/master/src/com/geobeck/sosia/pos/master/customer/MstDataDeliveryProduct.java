/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.customer;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nakhoa
 */
public class MstDataDeliveryProduct {

    /**
     * ŒÚ‹qNo.
     */
    /**
     * ó“n“ú
     */
    protected java.util.Date saleDate = null;
    /*
     /**
     * productName
     */
    protected String productName = null;
    /**
     * purchasesNo
     */
    /**
     * ƒXƒ^ƒbƒt
     */
    protected MstStaff staff = new MstStaff();
    /**
     * “X•Ü
     */
    protected MstShop shop = new MstShop();
    /**
     * “`•[No.
     */
    protected Integer slipNo = null;
    /**
     * “`•[–¾×No.
     */
    protected Integer slipDetailNo = null;
    /*
     * ó“n¤•iÚ×No.
     */
    protected Integer pickupProductNo = null;

    protected Date operationDate = null;

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }
    
     // 2014/06/06 Thien An added start
    /**
     * ”z‘—“ú
     */
    protected String deliveryDate = null;
    
    /**
     * ”z‘—“ú‚ğƒZƒbƒg‚·‚éB
     *
     * @param deliveryDate ”z‘—“ú
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    /**
     * ”z‘—“ú‚ğæ“¾‚·‚éB
     *
     * @return ”z‘—“ú
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }
    
    /**
     * —X•Ö”Ô†
     */
    protected String postalCode = null;

    /**
     * —X•Ö”Ô†‚ğƒZƒbƒg‚·‚éB
     * 
     * @param postalCode —X•Ö”Ô†
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    /**
     * —X•Ö”Ô†‚ğæ“¾‚·‚éB
     * 
     * @return —X•Ö”Ô†
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * “s“¹•{Œ§
     */
    protected String address1 = null;

    /**
     * “s“¹•{Œ§‚ğƒZƒbƒg‚·‚éB
     * 
     * @param address1 “s“¹•{Œ§
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    
    /**
     * “s“¹•{Œ§‚ğæ“¾‚·‚éB
     * 
     * @return “s“¹•{Œ§
     */
    public String getAddress1() {
        return address1;
    }
    
    /**
     * s‹æ’¬‘º
     */
    protected String address2 = null;

    /**
     * s‹æ’¬‘º‚ğƒZƒbƒg‚·‚éB
     * 
     * @param address2 s‹æ’¬‘º
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    /**
     * s‹æ’¬‘º‚ğæ“¾‚·‚éB
     * 
     * @return s‹æ’¬‘º
     */
    public String getAddress2() {
        return address2;
    }
    
    /**
     * s‹æ’¬‘º
     */
    protected String address3 = null;

    /**
     * s‹æ’¬‘º‚ğƒZƒbƒg‚·‚éB
     * 
     * @param address3 s‹æ’¬‘º
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }
    
    /**
     * s‹æ’¬‘º‚ğæ“¾‚·‚éB
     * 
     * @return s‹æ’¬‘º
     */
    public String getAddress3() {
        return address3;
    }
    
    /**
     * ƒ}ƒ“ƒVƒ‡ƒ“–¼“™
     */
    protected String address4 = null;

    /**
     * ƒ}ƒ“ƒVƒ‡ƒ“–¼“™‚ğƒZƒbƒg‚·‚éB
     * 
     * @param address4 ƒ}ƒ“ƒVƒ‡ƒ“–¼“™
     */
    public void setAddress4(String address4) {
        this.address4 = address4;
    }
    
    /**
     * ƒ}ƒ“ƒVƒ‡ƒ“–¼“™‚ğæ“¾‚·‚éB
     * 
     * @return ƒ}ƒ“ƒVƒ‡ƒ“–¼“™
     */
    public String getAddress4() {
        return address4;
    }
    
    /**
     * ‘î”z”
     */
    protected Integer deliveryNum = null;

    /**
     * ‘î”z”‚ğƒZƒbƒg‚·‚éB
     * 
     * @param deliveryNum ‘î”z”
     */
    public void setDeliveryNum(Integer deliveryNum) {
        this.deliveryNum = deliveryNum;
    }
    
    /**
     * ‘î”z”‚ğæ“¾‚·‚éB
     * 
     * @return ‘î”z”
     */
    public Integer getDeliveryNum() {
        return deliveryNum;
    }
    //2014/06/06 Thien An added end


    /**
     * ”—Ê
     */
    protected Integer productNum = null;
    protected Integer staffID = null;
    protected Integer shopID = null;
    protected MstStaffs staffs = null;
    protected Integer status = null;

    public Double getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Double salesNum) {
        this.salesNum = salesNum;
    }

    public Double getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Double usedNum) {
        this.usedNum = usedNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPickNum() {
        return pickNum;
    }

    public void setPickNum(Double pick_num) {
        this.pickNum = pick_num;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    protected Double salesNum = null;
    protected Double usedNum = null;
    protected Double price = null;
    protected Double amount = null;
    protected Double pickNum = null;

    /**
     * /**
     * ŒÚ‹qNo.
     *
     * @param customerNo ŒÚ‹qNo.
     */
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public java.util.Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(java.util.Date saleDate) {
        this.saleDate = saleDate;
    }

    public MstStaff getStaff() {
        return staff;
    }

    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * “X•Ü‚ğæ“¾‚·‚éB
     *
     * @return “X•Ü
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * “X•Ü‚ğƒZƒbƒg‚·‚éB
     *
     * @param shop “X•Ü
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * “`•[No.
     *
     * @return “`•[No.
     */
    public Integer getSlipNo() {
        return slipNo;
    }

    /**
     * “`•[No.
     *
     * @param slipNo “`•[No.
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * “`•[–¾×No.
     *
     * @return “`•[–¾×No.
     */
    public Integer getSlipDetailNo() {
        return slipDetailNo;
    }

    /**
     * “`•[–¾×No.
     *
     * @param slipDetailNo “`•[–¾×No.
     */
    public void setSlipDetailNo(Integer slipDetailNo) {
        this.slipDetailNo = slipDetailNo;
    }

    /**
     * ó“n¤•iÚ×No.
     *
     * @return ó“n¤•iÚ×No.
     */
    public Integer getPickupProductNo() {
        return pickupProductNo;
    }

    /**
     * ó“n¤•iÚ×No.
     *
     * @param pickupProductNo ó“n¤•iÚ×No.
     */
    public void setPickupProductNo(Integer pickupProductNo) {
        this.pickupProductNo = pickupProductNo;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public MstStaffs getStaffs() {
        if (staffs == null) {
            return getStaffs(SystemInfo.getCurrentShop());
        }
        return staffs;
    }

    public MstStaffs getStaffs(MstShop ms) {
        staffs = new MstStaffs();
        if (ms != null) {
            staffs.setShopIDList(ms.getShopID().toString());
        } else {
            staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
        }

        try {

            staffs.load(SystemInfo.getConnection(), true);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return staffs;
    }
    
     public boolean registMission(ArrayList<MstDataDeliveryProduct> list, ConnectionWrapper con) throws SQLException {
        try {
            con.begin();
            MstDataDeliveryProduct mdp = new MstDataDeliveryProduct();
            Integer shopId = -1;
            Integer slipNo = -1;
            Integer slipDetailNo = -1;
            for (int i = 0; i < list.size(); i++) {
                mdp = new MstDataDeliveryProduct();
                mdp = list.get(i);
                int DeliveryNum = list.get(i).getDeliveryNum();
                if (list.get(i).getProductNum() > 0) {
                    mdp.setDeliveryNum(0);
                    mdp.setStatus(0);
                    if (con.executeUpdate(getInsertSQL(mdp)) != 1) {
                    con.rollback();
                    return false;
                    }
                }
                if (DeliveryNum > 0) {
                    mdp.setDeliveryNum(DeliveryNum);
                    mdp.setStatus(1);
                    mdp.setProductNum(0);
                    if (con.executeUpdate(getInsertSQL(mdp)) != 1) {
                    con.rollback();
                    return false;
                    }
                }
                
                shopId = mdp.getShop().getShopID();
                slipNo = mdp.getSlipNo();
                slipDetailNo = mdp.getSlipDetailNo();
            }
            con.commit();
            con.close();
            return true;
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            con.rollback();
            con.close();
            return false;
        }

    }
    
    //2014/06/10 Thien An added start
    public MstStaffs getStaffsNotBlank() {
        if (staffs == null) {
            return getStaffsNotBlank(SystemInfo.getCurrentShop());
        }
        return staffs;
    }

    public MstStaffs getStaffsNotBlank(MstShop ms) {
        staffs = new MstStaffs();
        if (ms != null) {
            staffs.setShopIDList(ms.getShopID().toString());
        } else {
            staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
        }

        try {

            staffs.load(SystemInfo.getConnection(), false);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return staffs;
    }
    //2014/06/10 Thien An added end 

    public ArrayList<MstDataDeliveryProduct> load(ConnectionWrapper con, Integer customerId, Integer shopId, Integer slipNo) {
        ArrayList<MstDataDeliveryProduct> list = new ArrayList<MstDataDeliveryProduct>();
        try {
            ResultSetWrapper rs = new ResultSetWrapper();
            rs = con.executeQuery(this.getSelectSQL(customerId, shopId, slipNo));
            MstDataDeliveryProduct dpp = new MstDataDeliveryProduct();
            while (rs.next()) {
                dpp = new MstDataDeliveryProduct();
                dpp.setData(rs);
                list.add(dpp);

            }
        } catch (SQLException ex) {
            Logger.getLogger(MstDataDeliveryProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
     public ArrayList<MstDataDeliveryProduct> loadOnlySlipNo(ConnectionWrapper con, Integer customerId, Integer shopId, Integer slipNo) {
        ArrayList<MstDataDeliveryProduct> list = new ArrayList<MstDataDeliveryProduct>();
        try {
            ResultSetWrapper rs = new ResultSetWrapper();
            rs = con.executeQuery(this.getSelectOnlySlipNoSQL(customerId, shopId, slipNo));
            MstDataDeliveryProduct dpp = new MstDataDeliveryProduct();
            while (rs.next()) {
                dpp = new MstDataDeliveryProduct();
                dpp.setData(rs);
                list.add(dpp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MstDataDeliveryProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    

    public boolean regist(ArrayList<MstDataDeliveryProduct> list, ConnectionWrapper con) throws SQLException {
        try {
            con.begin();
            MstDataDeliveryProduct mdp = new MstDataDeliveryProduct();
            Integer shopId = -1;
            Integer slipNo = -1;
            Integer slipDetailNo = -1;
            for (int i = 0; i < list.size(); i++) {
                mdp = new MstDataDeliveryProduct();
                mdp = list.get(i);
                if (con.executeUpdate(getInsertSQL(mdp)) != 1) {
                    con.rollback();
                    return false;
                }
                shopId = mdp.getShop().getShopID();
                slipNo = mdp.getSlipNo();
                slipDetailNo = mdp.getSlipDetailNo();
            }
            con.commit();
            con.close();
            return true;
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            con.rollback();
            con.close();
            return false;
        }

    }

    public void setData(ResultSetWrapper rs) throws SQLException {
         MstShop shoptemp = new MstShop();
        shoptemp.setShopID(rs.getInt("shop_id"));
        this.setShop(shoptemp);
        this.setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));
        this.setSaleDate(rs.getDate("sales_date"));
        this.setProductName(rs.getString("product_name"));
        MstStaff ms = new MstStaff();
        //2014/06/10 Thien An deleted start
//        ms.setStaffID(rs.getInt("staff_id"));
//        ms.setStaffName(0, rs.getString("staff_name1"));
//        ms.setStaffName(1, rs.getString("staff_name2"));
        //2014/06/10 Thien An edited end
        ms.setShop(shoptemp);
        this.setStaff(ms);
        //this.setStaffID(rs.getInt("staff_id")); 2014/06/10 Thien An deleted
        this.setSalesNum(rs.getDouble("sales_num"));
        //2014/06/10 Thien An edited
        //this.setUsedNum(rs.getDouble("sales_num") - rs.getDouble("pick_num") );     
        this.setUsedNum(rs.getDouble("sales_num") - rs.getDouble("pick_num") - rs.getInt("dlvr_num"));
        //2014/06/10 Thien An edited
        this.setPrice(rs.getDouble("price"));
        this.setAmount(this.getPrice() * this.getUsedNum());
        this.setPickNum(rs.getDouble("pick_num"));
        
        //2014/06/06 Thien An added start
        this.setDeliveryNum(rs.getInt("dlvr_num"));
        //2014/06/06 Thien An added end
    }

    private String getDeleteSQL(MstDataDeliveryProduct dpp) {
        return "update data_pickup_product\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where shop_id = " + SQLUtil.convertForSQL(dpp.getShop().getShopID()) + "\n"
                + " and slip_no = " + SQLUtil.convertForSQL(dpp.getSlipNo()) + "\n"
                + " and slip_detail_no = " + SQLUtil.convertForSQL(dpp.getSlipDetailNo()) + "\n";
    }

    private String getInsertSQL(MstDataDeliveryProduct dpp) {
        return "insert into data_pickup_product\n"
                + "(shop_id , slip_no,\n"
                + "slip_detail_no, pickup_product_no, product_num, operation_date, \n"
                + "delivery_date, \n" //2014/06/06 Thien An added start
                + "post_code, \n"
                + "address1, \n"
                + "address2, \n"
                + "address3, \n"
                + "address4, \n"
                + "delivery_num, \n" //2014/06/06 Thien An added end
                + "status, staff_id, insert_date, update_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(dpp.getShop().getShopID()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getSlipNo()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getSlipDetailNo()) + ",\n"
                + "(    select coalesce(max(pickup_product_no), 0) + 1 from data_pickup_product \n"
                + "     where shop_id = " + SQLUtil.convertForSQL(dpp.getShop().getShopID())+ " ),\n"
                + SQLUtil.convertForSQL(dpp.getProductNum()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getOperationDate()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getDeliveryDate()) + ",\n" //2014/06/06 Thien An added start
                + SQLUtil.convertForSQL(dpp.getPostalCode()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getAddress1()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getAddress2()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getAddress3()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getAddress4()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getDeliveryNum(),"0") + ",\n" //2014/06/06 Thien An added end
                + SQLUtil.convertForSQL(dpp.getStatus()) + ",\n"
                + SQLUtil.convertForSQL(dpp.getStaff().getStaffID()) + ",\n"
                + "current_timestamp, current_timestamp)\n";
    }

    public String getSelectSQL(Integer customerId, Integer shopId, Integer slipNo) {
        StringBuilder sql = new StringBuilder(1000);
        //LUC START EDIT 20131227
        //        sql.append("SELECT distinct* from ( \n ");
        //        sql.append("    SELECT ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        //        sql.append("    dsd.product_id , wp.product_name \n ");
        //        sql.append("    , wp.price ,dsd.product_num as sales_num \n ");
        //        sql.append("   ,ms.staff_id, ms.staff_name1,ms.staff_name2 \n  ");
        //        sql.append("     , CASE WHEN ds.sales_date is null THEN drd.reservation_datetime  ELSE ds.sales_date END as sales_date  \n");
        //        sql.append("  , sum(COALESCE(dpp.product_num,0)) :: INT as pick_num \n  ");
        //        sql.append("    FROM data_sales ds \n ");
        //        sql.append("    INNER JOIN  data_sales_detail  dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no \n ");
        //        sql.append("    INNER JOIN view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n ");
        //        sql.append("    INNER JOIN data_pickup_product dpp on dsd.shop_id = dpp.shop_id \n ");
        //        sql.append("    AND dsd.slip_no = dpp.slip_no and dsd.slip_detail_no = dpp.slip_detail_no \n ");
        //        sql.append("    LEFT JOIN  mst_staff ms on ms.staff_id = ds.staff_id \n ");
        //        sql.append("    INNER JOIN data_reservation dr on dr.slip_no = ds.slip_no and dr.shop_id = ds.shop_id \n ");
        //        sql.append("    AND dr.customer_id = ds.customer_id \n ");
        //        sql.append("    INNER JOIN data_reservation_detail drd on dr.reservation_no = drd.reservation_no \n");
        //        sql.append("    AND dr.shop_id = drd.shop_id \n ");
        //        sql.append("    WHERE \n ");
        //        sql.append("     ds.shop_id   =" + SQLUtil.convertForSQL(shopId) + "\n");
        //        sql.append("    AND ds.customer_id  = " + SQLUtil.convertForSQL(customerId) + "\n");
        //        sql.append("    AND dsd.product_division = 2 \n ");
        //        sql.append("    AND ds.delete_date is null \n ");
        //        sql.append("     AND dr.delete_date is null AND drd.delete_date is null \n ");
        //        sql.append("    AND dpp.delete_date is null \n ");
        //        sql.append("    GROUP BY  ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        //        sql.append("    ds.sales_date, dsd.product_id , wp.product_name \n ");
        //        sql.append("     ,ms.staff_id, ms.staff_name1,ms.staff_name2  \n ");
        //        sql.append("    , wp.price,dsd.product_num,  drd.reservation_datetime \n ");
        //        sql.append("    ORDER BY ds.slip_no desc \n ");
        //        sql.append("    ) as pick \n ");
        //        sql.append("WHERE  pick_num < sales_num \n ");
        //        sql.append(" and sales_date is not null \n ");
        
       sql.append("SELECT distinct* from ( \n ");
        sql.append("    SELECT ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        sql.append("    dsd.product_id , wp.product_name \n ");
        sql.append("    , dsd.product_value as price ,dsd.product_num as sales_num \n ");
        sql.append("   --,ms.staff_id, ms.staff_name1,ms.staff_name2 \n  "); //2014/06/10 Thien An edited
        sql.append("     , CASE WHEN ds.sales_date is null THEN \n");
        sql.append("    (select min(drd.reservation_datetime)	from data_reservation dr \n");       
        sql.append("    INNER JOIN data_reservation_detail drd on dr.reservation_no = drd.reservation_no      AND dr.shop_id = drd.shop_id\n");       
        sql.append("    where  dr.slip_no = ds.slip_no and dr.shop_id = ds.shop_id       AND dr.customer_id = ds.customer_id\n"); 
        sql.append("    AND dr.delete_date is null AND drd.delete_date is null \n");  
        sql.append("    )\n");
        sql.append(" ELSE ds.sales_date END as sales_date  \n");
        sql.append("  , sum(COALESCE(dpp.product_num,0)) :: INT as pick_num \n  ");
        sql.append("  , sum(COALESCE(dpp.delivery_num,0)) :: INT as dlvr_num \n  "); //2014/06/09 Thien An added
        sql.append("    --, dpp.delivery_date \n "); //2014/06/10 Thien An edited
        sql.append("    FROM data_sales ds \n ");
        sql.append("    INNER JOIN  data_sales_detail  dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no \n ");
        sql.append("    INNER JOIN view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n ");
        sql.append("    INNER JOIN mst_item_class itc on itc.item_class_id = wp.class_id \n "); //2014/06/24 Thien An added
        sql.append("    LEFT JOIN data_pickup_product dpp on dsd.shop_id = dpp.shop_id \n ");
        sql.append("    AND dsd.slip_no = dpp.slip_no and dsd.slip_detail_no = dpp.slip_detail_no \n ");
        sql.append("    LEFT JOIN  mst_staff ms on ms.staff_id = ds.staff_id \n ");
        sql.append("    WHERE \n ");
        sql.append("     ds.shop_id   =" + SQLUtil.convertForSQL(shopId) + "\n");
        sql.append("    AND ds.customer_id  = " + SQLUtil.convertForSQL(customerId) + "\n");
        sql.append("    AND dsd.product_division = 2 \n ");
        sql.append("    AND ds.delete_date is null \n ");
        sql.append("    AND dpp.delete_date is null \n ");
        sql.append("    AND (itc.prepa_class_id is null or itc.prepa_class_id <> 1) \n "); //2014/06/24 Thien An added
        sql.append("    GROUP BY  ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        sql.append("    ds.sales_date, dsd.product_id , wp.product_name \n ");
        sql.append("     --,ms.staff_id, ms.staff_name1,ms.staff_name2  \n "); //2014/06/10 Thien An edited
        sql.append("     --, dpp.delivery_date \n "); //2014/06/10 Thien An added
        sql.append("    , dsd.product_value,dsd.product_num,ds.customer_id \n ");
        sql.append("    ORDER BY ds.slip_no desc \n ");
        sql.append("    ) as pick \n ");
        sql.append("WHERE  pick_num + dlvr_num < sales_num \n "); //2014/06/09 Thien An added
        //Luc start delete 20150629 #38253
        //sql.append(" and sales_date is not null \n ");
        //Luc end delete 20150629 #38253
        //LUC END EDIT 20121227
        return sql.toString();

    }   
     public String getSelectOnlySlipNoSQL(Integer customerId, Integer shopId, Integer slipNo) {
        StringBuilder sql = new StringBuilder(1000);
        //LUC START EDIT 20131227
        //        sql.append("    SELECT distinct ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        //        sql.append("    dsd.product_id , wp.product_name \n ");
        //        sql.append("    , wp.price ,dsd.product_num as sales_num \n ");
        //        sql.append("   ,ms.staff_id, ms.staff_name1,ms.staff_name2 \n  ");
        //        sql.append("     , CASE WHEN ds.sales_date is null THEN drd.reservation_datetime  ELSE ds.sales_date END as sales_date  \n");
        //        sql.append("  , sum(COALESCE(dpp.product_num,0)) :: INT as pick_num \n  ");
        //        sql.append("    FROM data_sales ds \n ");
        //        sql.append("    INNER JOIN  data_sales_detail  dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no \n ");
        //        sql.append("    INNER JOIN view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n ");
        //        sql.append("    INNER JOIN data_pickup_product dpp on dsd.shop_id = dpp.shop_id \n ");
        //        sql.append("    AND dsd.slip_no = dpp.slip_no and dsd.slip_detail_no = dpp.slip_detail_no \n ");
        //        sql.append("    LEFT JOIN  mst_staff ms on ms.staff_id = ds.staff_id \n ");
        //        sql.append("    INNER JOIN data_reservation dr on dr.slip_no = ds.slip_no and dr.shop_id = ds.shop_id \n ");
        //        sql.append("    AND dr.customer_id = ds.customer_id \n ");
        //        sql.append("    INNER JOIN data_reservation_detail drd on dr.reservation_no = drd.reservation_no \n");
        //        sql.append("    AND dr.shop_id = drd.shop_id \n ");
        //        sql.append("    WHERE \n ");
        //        sql.append("     ds.shop_id   =" + SQLUtil.convertForSQL(shopId) + "\n");
        //        sql.append("    AND ds.customer_id  = " + SQLUtil.convertForSQL(customerId) + "\n");
        //        sql.append("    AND ds.slip_no  = " + SQLUtil.convertForSQL(slipNo) + "\n");
        //        sql.append("    AND dsd.product_division = 2 \n ");
        //        sql.append("    AND ds.delete_date is null \n ");
        //        sql.append("    AND dpp.delete_date is null \n ");
        //        sql.append("    GROUP BY  ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        //        sql.append("    ds.sales_date, dsd.product_id , wp.product_name \n ");
        //        sql.append("     ,ms.staff_id, ms.staff_name1,ms.staff_name2  \n ");
        //        sql.append("    , wp.price,dsd.product_num,  drd.reservation_datetime \n ");
        //        sql.append("    ORDER BY ds.slip_no desc \n ");
        
       sql.append("    SELECT distinct ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        sql.append("    dsd.product_id , wp.product_name \n ");
        sql.append("    , wp.price ,dsd.product_num as sales_num \n ");
        sql.append("   --,ms.staff_id, ms.staff_name1,ms.staff_name2 \n  "); //2014/06/10 Thien An edited
        sql.append("     , CASE WHEN ds.sales_date is null THEN \n");
        sql.append("    (select min(drd.reservation_datetime)	from data_reservation dr \n");       
        sql.append("    INNER JOIN data_reservation_detail drd on dr.reservation_no = drd.reservation_no      AND dr.shop_id = drd.shop_id\n");       
        sql.append("    where  dr.slip_no = ds.slip_no and dr.shop_id = ds.shop_id       AND dr.customer_id = ds.customer_id\n"); 
        sql.append("    AND dr.delete_date is null AND drd.delete_date is null \n");  
        sql.append("    )\n");
        sql.append(" ELSE ds.sales_date END as sales_date  \n");
        sql.append("  , sum(COALESCE(dpp.product_num,0)) :: INT as pick_num \n  ");
        sql.append("  , sum(COALESCE(dpp.delivery_num,0)) :: INT as dlvr_num \n  "); //2014/06/09 Thien An added
        sql.append("    --, dpp.delivery_date \n "); //2014/06/09 Thien An added
        sql.append("    FROM data_sales ds \n ");
        sql.append("    INNER JOIN  data_sales_detail  dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no \n ");
        sql.append("    INNER JOIN view_mst_product wp on wp.product_division = dsd.product_division and wp.product_id = dsd.product_id \n ");
        sql.append("    INNER JOIN mst_item_class itc on itc.item_class_id = wp.class_id \n "); //2014/06/24 Thien An added
        sql.append("    LEFT JOIN data_pickup_product dpp on dsd.shop_id = dpp.shop_id \n ");
        sql.append("    AND dsd.slip_no = dpp.slip_no and dsd.slip_detail_no = dpp.slip_detail_no \n ");
        sql.append("    LEFT JOIN  mst_staff ms on ms.staff_id = ds.staff_id \n ");
        sql.append("    WHERE \n ");
        sql.append("     ds.shop_id   =" + SQLUtil.convertForSQL(shopId) + "\n");
        sql.append("    AND ds.customer_id  = " + SQLUtil.convertForSQL(customerId) + "\n");
        sql.append("    AND ds.slip_no  = " + SQLUtil.convertForSQL(slipNo) + "\n");
        sql.append("    AND dsd.product_division = 2 \n ");
        sql.append("    AND ds.delete_date is null \n ");
        sql.append("    AND dpp.delete_date is null \n ");
        sql.append("    AND (itc.prepa_class_id is null or itc.prepa_class_id <> 1) \n "); //2014/06/24 Thien An added
        sql.append("    GROUP BY  ds.shop_id , ds.slip_no , dsd.slip_detail_no, \n ");
        sql.append("    ds.sales_date, dsd.product_id , wp.product_name \n ");
        sql.append("     --,ms.staff_id, ms.staff_name1,ms.staff_name2  \n "); //2014/06/10 Thien An edited
        sql.append("     --, dpp.delivery_date \n "); //2014/06/10 Thien An edited
        sql.append("    , wp.price,dsd.product_num,ds.customer_id \n ");
        sql.append("    ORDER BY ds.slip_no desc \n ");
        //LUC END EDIT 20131227
        return sql.toString();

    }
    
    
    
}

/*
 * PrepaidData.java
 *
 * Created on 2008/09/01, 17:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

import java.util.logging.Level;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import java.sql.SQLException;

/**
 *
 * @author takeda
 */
public class PrepaidData {
    
    private Integer prepaidId = null;
    private Integer shopId = null;
    private Integer customerId = null;
    private Integer slipNo = null;
    private Long salesValue = null;
    private Long useValue = null;

    public PrepaidData()
    {
        
    }
    
    public PrepaidData( Integer shopId, Integer customerId,
                        Integer slipNo, Long useValue, Long salesValue) {
        
        this.setShopId(shopId);
        this.setCustomerId(customerId);
        this.setSlipNo(slipNo);
        this.setUseValue(useValue);
        this.setSalesValue(salesValue);
    }

    /**
     * 過去に発生したプリペイドであれば、その情報を取得する
     *
     */
    public static PrepaidData getPrepaidData( Integer shopId, Integer slipNo )
    {
        PrepaidData prepaid = new PrepaidData();
        prepaid.setShopId( shopId );
        prepaid.setSlipNo( slipNo );;
        if( prepaid.getAccountPrepaid() ){
            return prepaid;
        }
        return null;
    }
    
    
    /**
     * 対象顧客の現在プリペイドを取得します
     * @param customer_id  CusomerID
     * @return 現在残プリペイド
     */
    public boolean getAccountPrepaid() {
        boolean bRet = false;
        String sqlQuery = "SELECT " +
                " shop_id," +
                " prepaid_id," +
                " customer_id," +
                " slip_no," +
                " use_value," +
                " sales_value" +
                " FROM data_prepaid" +
                " WHERE shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
                " AND   slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
                " AND delete_date IS NULL";
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper result = con.executeQuery( sqlQuery );
            if( result.next() ) {
                this.setShopId(result.getInt("shop_id"));
                this.setPrepaidId(result.getInt("prepaid_id"));
                this.setCustomerId(result.getInt("customer_id"));
                this.setSlipNo(result.getInt("slip_no"));
                this.setUseValue(result.getLong("use_value"));
                this.setSalesValue(result.getLong("sales_value"));
                bRet = true;
            }
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        return bRet;
    }
    
    public boolean update()
    {
        String sqlQuery = "UPDATE data_prepaid" +
            " SET " +
            "    use_value      = " + SQLUtil.convertForSQL(this.getUseValue()) +
            " ,  sales_value = " + SQLUtil.convertForSQL(this.getSalesValue()) +
            " ,  customer_id = " + SQLUtil.convertForSQL(this.getCustomerId()) + 
            " ,  update_date    = current_timestamp " +
            " WHERE shop_id  = " + SQLUtil.convertForSQL(this.getShopId()) +
            " AND   prepaid_id = " + SQLUtil.convertForSQL(this.getPrepaidId());
                
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            con.execute( sqlQuery );
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    public boolean insertPrepaidCalculation() {
        String sqlQuery = "INSERT INTO data_prepaid(" +
            " shop_id," +
            " prepaid_id," +
            " customer_id," +
            " slip_no," +
            " sales_value," +
            " use_value," +
            " insert_date," +
            " update_date" +
            ")SELECT " +
            SQLUtil.convertForSQL(this.getShopId()) + "," +
            " coalesce(max(prepaid_id), 0) + 1," +
            SQLUtil.convertForSQL(this.getCustomerId()) + "," +
            SQLUtil.convertForSQL(this.getSlipNo()) + "," +
            SQLUtil.convertForSQL(this.getSalesValue()) + "," +
            SQLUtil.convertForSQL(this.getUseValue()) + "," +
            " current_timestamp, current_timestamp" +
            " FROM data_prepaid" +
            " WHERE shop_id = " + SQLUtil.convertForSQL(this.getShopId());
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            con.execute( sqlQuery );
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 対象顧客の現在プリペイドを取得します
     * @param customer_id  CusomerID
     * @return 現在残プリペイド
     */
    public static Long getNowValue( Integer customer_id ) {
        Long nowvalue = 0L;
        
        if( customer_id == null ) return null;
        
        String sqlQuery = "SELECT sum(sales_value) - sum(use_value)" +
            " FROM data_prepaid" +
            " WHERE customer_id = " + SQLUtil.convertForSQL(customer_id) +
            " AND delete_date IS NULL";
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper result = con.executeQuery( sqlQuery );
            if( result.next() ) {
                nowvalue = result.getLong(1);
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        return nowvalue;
    }

    public Integer getShopId() {
        return shopId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getSlipNo() {
        return slipNo;
    }

    public Long getUseValue() {
        return useValue;
    }

    public Long getSalesValue() {
        return salesValue;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    public void setUseValue(Long useValue) {
        this.useValue = useValue;
    }

    public void setSalesValue(Long salesValue) {
        this.salesValue = salesValue;
    }

    public Integer getPrepaidId() {
        return prepaidId;
    }

    public void setPrepaidId(Integer prepaidId) {
        this.prepaidId = prepaidId;
    }
        
    //nahoang Add start 20140609
    public boolean isExists(ConnectionWrapper con) {

        return this.getPrepaidPanel(con);

    }
    
    public boolean checkDataPrepaid(ConnectionWrapper con, Integer slipNo, Integer shopId ){
        String sqlQuery = "SELECT "
                + " shop_id,"
                + " prepaid_id,"
                + " customer_id,"
                + " slip_no,"
                + " use_value,"
                + " sales_value"
                + " FROM data_prepaid"
                + " WHERE shop_id = " + shopId
                + " AND   slip_no = " + slipNo;
        
        try{
            ResultSetWrapper rs = con.executeQuery(sqlQuery);
            if (rs.next()) {
                useValue = rs.getLong("use_value");
                salesValue = rs.getLong("sales_value");
                this.setSalesValue(salesValue);
                this.setUseValue(useValue);
                return true;                
            } else {
                return false;
            }
        }catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        }
              
    }
    
    public boolean getPrepaidPanel(ConnectionWrapper con) {
        String sqlQuery = "SELECT "
                + " shop_id,"
                + " prepaid_id,"
                + " customer_id,"
                + " slip_no,"
                + " use_value,"
                + " sales_value"
                + " FROM data_prepaid"
                + " WHERE shop_id = " + SQLUtil.convertForSQL(this.getShopId())
                + " AND   slip_no = " + SQLUtil.convertForSQL(this.getSlipNo())
                + " AND delete_date IS NULL";

        try {
            ResultSetWrapper rs = con.executeQuery(sqlQuery);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        }   
    }

    public boolean updatePrepaidPanel(ConnectionWrapper con) {
        String sqlQuery = "UPDATE data_prepaid"
                + " SET "
                + "    use_value      = " + SQLUtil.convertForSQL(this.getUseValue())
                + " ,  sales_value = " + SQLUtil.convertForSQL(this.getSalesValue())
                + " ,  customer_id = " + SQLUtil.convertForSQL(this.getCustomerId())
                + ", delete_date = NULL"
                + //            " ,  update_date    = current_timestamp " +
                " WHERE shop_id  = " + SQLUtil.convertForSQL(this.getShopId())
                + " AND   slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());

        try {
            con.execute(sqlQuery);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }
    
    public boolean insertPrepaidPanel(ConnectionWrapper con){
        String sqlQuery = "INSERT INTO data_prepaid(" +
            " shop_id," +
            " prepaid_id," +
            " customer_id," +
            " slip_no," +
            " sales_value," +
            " use_value," +
            " insert_date," +
            " update_date" +
            ")SELECT " +
            SQLUtil.convertForSQL(this.getShopId()) + "," +
            " coalesce(max(prepaid_id), 0) + 1," +
            SQLUtil.convertForSQL(this.getCustomerId()) + "," +
            SQLUtil.convertForSQL(this.getSlipNo()) + "," +
            SQLUtil.convertForSQL(this.getSalesValue()) + "," +
            SQLUtil.convertForSQL(this.getUseValue()) + "," +
            " current_timestamp, current_timestamp" +
            " FROM data_prepaid" +
            " WHERE shop_id = " + SQLUtil.convertForSQL(this.getShopId());
        try {            
            con.execute( sqlQuery );
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    public boolean regist(ConnectionWrapper con) {
        if (isExists(con)) {
            //--update
            if (this.updatePrepaidPanel(con)) {
                return true;
            }
        } else {
            //--insert
            if (this.insertPrepaidPanel(con)) {
                return true;
            }
        }
        return false;
    }
    //nahoang Add end
}

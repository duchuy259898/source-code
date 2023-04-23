/*
 * PointData.java
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

/**
 *
 * @author takeda
 */
public class PointData {
    
    private Integer pointId = null;
    private Integer shopId = null;
    private Integer customerId = null;
    private Integer slipNo = null;
    private Integer paymentNo = null;
    private Long usePoint = null;
    private Long suppliedPoint = null;

    public PointData()
    {
        
    }
    
    public PointData( Integer shopId, Integer customerId,
                        Integer slipNo, Integer paymentNo,
                        Long usePoint, Long suppliedPoint) {
        
        this.setShopId(shopId);
        this.setCustomerId(customerId);
        this.setSlipNo(slipNo);
        this.setPaymentNo(paymentNo);
        this.setUsePoint(usePoint);
        this.setSuppliedPoint(suppliedPoint);
    }

    /**
     * 過去に発生したポイントであれば、その情報を取得する
     *
     */
    public static PointData getPointData( Integer shopId, Integer slipNo )
    {
        PointData point = new PointData();
        point.setShopId( shopId );
        point.setSlipNo( slipNo );;
        if( point.getAccountPoint() ){
            return point;
        }
        return null;
    }
    
    
    /**
     * 対象顧客の現在ポイントを取得します
     * @param customer_id  CusomerID
     * @return 現在残ポイント
     */
    public boolean getAccountPoint() {
        Integer nowpoint = 0;
        boolean bRet = false;
        
        String sqlQuery = "SELECT " +
                " shop_id," +
                " point_id," +
                " customer_id," +
                " slip_no," +
                " payment_no," +
                " use_point," +
                " supplied_point" +
                " FROM data_point" +
                " WHERE shop_id = " + SQLUtil.convertForSQL(this.getShopId()) +
                " AND   slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) +
                " AND delete_date IS NULL";
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper result = con.executeQuery( sqlQuery );
            if( result.next() ) {
                this.setShopId(result.getInt("shop_id"));
                this.setPointId(result.getInt("point_id"));
                this.setCustomerId(result.getInt("customer_id"));
                this.setSlipNo(result.getInt("slip_no"));
                this.setPaymentNo(result.getInt("payment_no"));
                this.setUsePoint(result.getLong("use_point"));
                this.setSuppliedPoint(result.getLong("supplied_point"));
                bRet = true;
            }
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        return bRet;
    }
    
    public boolean update()
    {
        String sqlQuery = "UPDATE data_point" +
            " SET " +
            "    use_point      = " + SQLUtil.convertForSQL(this.getUsePoint()) + 
            " ,  supplied_point = " + SQLUtil.convertForSQL(this.getSuppliedPoint()) + 
            " ,  customer_id = " + SQLUtil.convertForSQL(this.getCustomerId()) + 
            " ,  update_date    = current_timestamp " +
            " WHERE shop_id  = " + SQLUtil.convertForSQL(this.getShopId()) +
            " AND   point_id = " + SQLUtil.convertForSQL(this.getPointId());
                
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
     * ポイント計算レートを作成する
     * @param bean
     * @return
     */
    public boolean insertPointCalculation() {
        String sqlQuery = "INSERT INTO data_point(" +
            " shop_id," +
            " point_id," +
            " customer_id," +
            " slip_no," +
            " payment_no," +
            " use_point," +
            " supplied_point," +
            " insert_date," +
            " update_date" +
            ")SELECT " +
            SQLUtil.convertForSQL(this.getShopId()) + "," +
            " coalesce(max(point_id), 0) + 1," +
            SQLUtil.convertForSQL(this.getCustomerId()) + "," +
            SQLUtil.convertForSQL(this.getSlipNo()) + "," +
            SQLUtil.convertForSQL(this.getPaymentNo()) + "," +
            SQLUtil.convertForSQL(this.getUsePoint()) + "," +
            SQLUtil.convertForSQL(this.getSuppliedPoint()) + "," +
            " current_timestamp, current_timestamp" +
            " FROM data_point" +
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
     * 対象顧客の現在ポイントを取得します
     * @param customer_id  CusomerID
     * @return 現在残ポイント
     */
    public static Long getNowPoint( Integer customer_id ) {
        Long nowpoint = 0L;
        
        if( customer_id == null ) return null;
        
        String sqlQuery = "SELECT sum(supplied_point) - sum(use_point)" +
            " FROM data_point" +
            " WHERE customer_id = " + SQLUtil.convertForSQL(customer_id) +
            " AND delete_date IS NULL";
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper result = con.executeQuery( sqlQuery );
            if( result.next() ) {
                nowpoint = result.getLong(1);
            }
            result.close();
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
        return nowpoint;
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

    public Integer getPaymentNo() {
        return paymentNo;
    }

    public Long getUsePoint() {
        return usePoint;
    }

    public Long getSuppliedPoint() {
        return suppliedPoint;
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

    public void setPaymentNo(Integer paymentNo) {
        this.paymentNo = paymentNo;
    }

    public void setUsePoint(Long usePoint) {
        this.usePoint = usePoint;
    }

    public void setSuppliedPoint(Long suppliedPoint) {
        this.suppliedPoint = suppliedPoint;
    }

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }
}

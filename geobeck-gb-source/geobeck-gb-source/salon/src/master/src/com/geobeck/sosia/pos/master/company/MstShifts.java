/*
 * MstShifts.java
 *
 * Created on 2009/04/30, 11:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author takeda
 */
public class MstShifts extends ArrayList<MstShift>{
    
    private Integer shopId;
    
    /** Creates a new instance of MstShifts */
    public MstShifts() {
    }
    
    /**
     * シフトを読み込む
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException SQLException
     */
    public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException {

        this.clear();
        
        if (isAddBlank) {
            this.add(new MstShift());
        }
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        while(rs.next()) {
            MstShift ms = new MstShift();
            ms.setData(rs);
            
            this.add(ms);
        }
        
        return true;
    }
    
    private String getSelectSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_shift ms");
        sql.append(" where");
        sql.append("         ms.delete_date is null");
        sql.append("     and ms.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" order by");
        sql.append("      shift_name");
        sql.append("     ,shift_id");

        return sql.toString();
    }

    public boolean loadAll(ConnectionWrapper con) throws SQLException {

        this.clear();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectAllSQL());
        
        while(rs.next()) {
            MstShift ms = new MstShift();
            ms.setData(rs);
            
            this.add(ms);
        }
        
        return true;
    }

    private String getSelectAllSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_shift ms");
        sql.append(" where");
        sql.append("         ms.delete_date is null");
        sql.append(" order by");
        sql.append("      shift_name");
        sql.append("     ,shift_id");

        return sql.toString();
    }
    
    public MstShift getShift(Integer shiftId){
        return getShift(shiftId, this.getShopId());
    }

    public MstShift getShift(Integer shiftId, Integer staffShopId){
        
        for( MstShift shift : this ){
            boolean isSame = true;
            isSame = isSame && shift.getShiftId().equals(shiftId);
            isSame = isSame && shift.getShopId().equals(staffShopId);
            
            if (isSame) {
                return shift;
            }
        }
        return null;
    }
    
    /*
     * 基本シフト を一括登録する
     */
    public void regist(ConnectionWrapper con) throws SQLException {
        for( MstShift shift : this ){
            if (shift.getShopId().equals(this.getShopId())) {
                shift.regist(con);
            }
        }
    }
    
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}

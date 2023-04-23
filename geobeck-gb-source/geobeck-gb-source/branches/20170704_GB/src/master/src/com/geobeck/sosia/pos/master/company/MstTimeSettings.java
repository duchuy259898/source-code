/*
 * MstTimeSettings.java
 *
 * Created on 2010/07/15, 11:45
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
 * @author geobeck
 */
public class MstTimeSettings extends ArrayList<MstTimeSetting>{
    
    private Integer shopId;
    
    /** Creates a new instance of MstTimeSettings */
    public MstTimeSettings() {
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
            this.add(new MstTimeSetting());
        }
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        while(rs.next()) {
            MstTimeSetting ms = new MstTimeSetting();
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
        sql.append("     mst_time_setting ms");
        sql.append(" where");
        sql.append("         ms.delete_date is null");
        sql.append("     and ms.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" order by");
        sql.append("      time_name");
        sql.append("     ,time_id");

        return sql.toString();
    }

    public boolean loadAll(ConnectionWrapper con) throws SQLException {

        this.clear();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectAllSQL());
        
        while(rs.next()) {
            MstTimeSetting ms = new MstTimeSetting();
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
        sql.append("     mst_time_setting ms");
        sql.append(" where");
        sql.append("         ms.delete_date is null");
        sql.append(" order by");
        sql.append("      time_name");
        sql.append("     ,time_id");

        return sql.toString();
    }
    
    public MstTimeSetting getTime(Integer timeId){
        return getTime(timeId, this.getShopId());
    }

    public MstTimeSetting getTime(Integer timeId, Integer staffShopId){
        
        for( MstTimeSetting time : this ){
            boolean isSame = true;
            isSame = isSame && time.getTimeId().equals(timeId);
            isSame = isSame && time.getShopId().equals(staffShopId);
            
            if (isSame) {
                return time;
            }
        }
        return null;
    }
    
    /*
     * 基本シフト を一括登録する
     */
    public void regist(ConnectionWrapper con) throws SQLException {
        for( MstTimeSetting time : this ){
            if (time.getShopId().equals(this.getShopId())) {
                time.regist(con);
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

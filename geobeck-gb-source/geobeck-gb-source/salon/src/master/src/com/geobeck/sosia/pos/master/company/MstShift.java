/*
 * MstShift.java
 *
 * Created on 2009/04/30, 11:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author takeda
 */
public class MstShift {
    
    private Integer shopId;     /* 店舗ID       key */
    private Integer shiftId;    /* シフトID     key */
    private String shiftName;   /* シフト名         */
    private String startTime;   /* 開始時間         */
    private String endTime;     /* 終了時間         */
    
    public MstShift() {
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(shiftName);
        
        return sb.toString();
    }
    
    public boolean equals(Object o) {
        if(o != null && o instanceof MstShift) {
            MstShift shift = (MstShift)o;
            if( shift.getShopId() == this.getShopId() &&
                    shift.getShiftId() == this.getShiftId() ) {
                
                return	true;
            } else {
                return	false;
            }
        } else {
            return	false;
        }
    }
    
    // 指定した時間が、勤務時間内かを調査
    public boolean inRange(String time)
    {
        // 時間未設定は、省く
        if( startTime == null || endTime == null || 
            (startTime.equals("0000") && endTime.equals("0000")) ||
            CheckUtil.isNumber(startTime) == false ||
            CheckUtil.isNumber(endTime)   == false ){
            return false;
        }
        
        // 指定時間が、範囲内
        if( (Integer.parseInt(time) >= Integer.parseInt(startTime)) &&
             (Integer.parseInt(time) < Integer.parseInt(endTime)) ){
            return true;
        }
        return false;
    }

    // 指定した時間が、勤務時間内かを調査
    public boolean inRangeEndTime(String time)
    {
        // 時間未設定は、省く
        if( startTime == null || endTime == null ||
            (startTime.equals("0000") && endTime.equals("0000")) ||
            CheckUtil.isNumber(startTime) == false ||
            CheckUtil.isNumber(endTime)   == false ){
            return false;
        }

        // 指定時間が、範囲内
        if( (Integer.parseInt(time) >= Integer.parseInt(startTime)) &&
             (Integer.parseInt(time) <= Integer.parseInt(endTime)) ){
            return true;
        }
        return false;
    }
    
    public Integer getShopId() {
        return shopId;
    }
    
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
    
    public Integer getShiftId() {
        return shiftId;
    }
    
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }
    
    public String getShiftName() {
        return shiftName;
    }
    
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    /**
     * データをクリアする。
     */
    public void clear() {
        this.setShopId(null);
        this.setShiftId(null);
        this.setShiftName("");
        this.setStartTime("");
        this.setEndTime("");
    }
    
    /**
     * シフトマスタから、設定されている店舗ID、シフトIDのデータを読み込む。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)	return	true;
        if(this.getShiftId() == null || this.getShiftId() < 0)	return	true;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
        } else {
            return false;
        }
        
        return	true;
    }
    
    /**
     * シフトマスタから、設定されている店舗ID、シフト名のデータを読み込む。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean loadByShiftName(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)	return	true;
        if(this.getShiftName() == null || this.getShiftName().length() <= 0)	return	true;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectByShiftNameSQL());
        
        if(rs.next()) {
            this.setData(rs);
        } else {
            return false;
        }
        
        return	true;
    }
    
    /**
     * シフトマスタから、設定されている店舗ID、シフト名のデータを読み込み 対応しているシフトID を返す。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return Integer シフトID
     */
    public static Integer figureShiftIDByName(ConnectionWrapper con, Integer intShopID, String strShiftName) throws SQLException {
        if (intShopID == null  ||  intShopID < 0)	return	null;
        if (strShiftName == null  ||  strShiftName.length() <= 0)	return	null;
        
        if(con == null)	return	null;
        
        MstShift shift = new MstShift();
        shift.setShopId(intShopID);
        shift.setShiftName(strShiftName);
        ResultSetWrapper rs = con.executeQuery(shift.getSelectByShiftNameSQL());
        
        if(rs.next()) {
            return (Integer)rs.getObject("shift_id");
        } else {
            return null;
        }
    }
    
    /**
     * ResultSetWrapperのデータを読み込む。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setShopId(rs.getInt("shop_id"));
        this.setShiftId(rs.getInt("shift_id"));
        this.setShiftName(rs.getString("shift_name"));
        this.setStartTime(rs.getString("start_time"));
        this.setEndTime(rs.getString("end_time"));
    }
    
    
    /**
     * シフトマスタにデータを登録する。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if(isExists(con)) {
            return (con.executeUpdate(this.getUpdateSQL()) == 1);
        } else {
            if(con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }
            return true;
        }
    }
    
    
    /**
     * シフトマスタからデータを削除する。（論理削除）
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        if(isExists(con)) {
            if(con.executeUpdate(this.getDeleteSQL()) == 1) {
                return	true;
            }
        }
        
        return	false;
    }
    
    /**
     * シフトマスタにデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)	return	false;
        if(this.getShiftId() == null || this.getShiftId() < 0)	return	false;
        
        if(con == null)	return	false;
        
        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
        
        if(rs.next())	return	true;
        else	return	false;
    }
    
    /**
     * 「休」のインスタンスを作成して返す
     * @param Integer intShopID
     * @return Recess Instance
     */

    public static MstShift createRecessShiftInstance(Integer intShopID)
    {
        MstShift shiftBreak = new MstShift();

        shiftBreak.setShopId(intShopID);
        shiftBreak.setShiftId(0);
        shiftBreak.setShiftName("●");
        shiftBreak.setStartTime("");
        shiftBreak.setEndTime("");
        
        return shiftBreak;
    }

    /**
     * Select文を取得する。
     * @return Select文
     */
    private String getSelectSQL() {
        return	"select *\n" +
                "from mst_shift\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    shift_id = " + SQLUtil.convertForSQL(this.getShiftId()) + "\n" +
                "order by	shift_id\n";
    }
    
    /**
     * シフト名からの Select文を取得する。
     * @return Select文
     */
    private String getSelectByShiftNameSQL() {
        return	"select *\n" +
                "from mst_shift\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    shift_name = " + SQLUtil.convertForSQL(this.getShiftName()) + "\n" +
                "order by	shift_id\n";
    }
    
    /**
     * Insert文を取得する。
     * @return Insert文
     */
    private String	getInsertSQL() {
        return	"insert into mst_shift\n" +
                "(shop_id, shift_id, shift_name,\n" +
                "start_time, end_time,\n" +
                "insert_date, update_date, delete_date)\n" +
                "select\n" +
                SQLUtil.convertForSQL(this.getShopId()) + ",\n" +
                "coalesce(max(shift_id), 0) + 1,\n" +
                SQLUtil.convertForSQL(this.getShiftName()) + ",\n" +
                SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "current_timestamp, current_timestamp, null\n" +
                "from mst_shift\n" +
                "where	shop_id = " + SQLUtil.convertForSQL(this.getShopId());
    }
    
    /**
     * Update文を取得する。
     * @return Update文
     */
    private String	getUpdateSQL() {
        return	"update mst_shift\n" +
                "set\n" +
                "shift_name = " + SQLUtil.convertForSQL(this.getShiftName()) + ",\n" +
                "start_time = " + SQLUtil.convertForSQL(this.getStartTime()) + ",\n" +
                "end_time   = " + SQLUtil.convertForSQL(this.getEndTime()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    shift_id = " + SQLUtil.convertForSQL(this.getShiftId()) + "\n";
    }
    
    /**
     * 削除用Update文を取得する。
     * @return 削除用Update文
     */
    private String	getDeleteSQL() {
        return	"update mst_shift\n" +
                "set\n" +
                "update_date = current_timestamp,\n" +
                "delete_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                "and    shift_id = " + SQLUtil.convertForSQL(this.getShiftId()) + "\n";
    }
    
 
}

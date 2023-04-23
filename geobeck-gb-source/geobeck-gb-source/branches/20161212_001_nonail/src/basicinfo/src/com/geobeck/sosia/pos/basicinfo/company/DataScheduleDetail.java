/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author lvtu
 */
public class DataScheduleDetail implements Cloneable{

    Integer             staffID             = null;
    String[]		staffName           ={"", ""};
    java.util.Date      scheduleDate        = null;
    Integer             scheduleDetailNo    = null;
    Integer             shopID              = null;
    java.util.Date      startTime           = null;
    java.util.Date      endTime             = null;
    Integer             travelTime1         = null;
    Integer             travelTime2         = null;
    java.util.Date      extStartTime        = null;
    java.util.Date      extEndTime          = null;

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Integer getScheduleDetailNo() {
        return scheduleDetailNo;
    }

    public void setScheduleDetailNo(Integer scheduleDetailNo) {
        this.scheduleDetailNo = scheduleDetailNo;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopId) {
        this.shopID = shopId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTravelTime1() {
        return travelTime1;
    }

    public void setTravelTime1(Integer travelTime1) {
        this.travelTime1 = travelTime1;
    }

    public Integer getTravelTime2() {
        return travelTime2;
    }

    public void setTravelTime2(Integer travelTime2) {
        this.travelTime2 = travelTime2;
    }

    public Date getExtStartTime() {
        return extStartTime;
    }

    public void setExtStartTime(Date extStartTime) {
        this.extStartTime = extStartTime;
    }

    public Date getExtEndTime() {
        return extEndTime;
    }

    public void setExtEndTime(Date extEndTime) {
        this.extEndTime = extEndTime;
    }
    

    public void setStaffName(int index, String staffName)
    {
            this.staffName[index] = (staffName == null ? "" : staffName);
    }


    public String getFullStaffName()
    {
            return	(staffName[0] == null ? "" : staffName[0]) +
                            (staffName[0] != null && !staffName[0].equals("") &&
                            staffName[1] != null && !staffName[1].equals("") ? "　" : "")
                            +	(staffName[1] == null  ? "" : staffName[1]);
    }
    
    @Override
    public String toString() {
        return this.getFullStaffName();
    }
    
    public DataScheduleDetail clone() throws CloneNotSupportedException {
        return (DataScheduleDetail)super.clone();
    }

    /**
     * ResultSetWrapperからデータをセットする
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean setData(ResultSetWrapper rs) throws SQLException {
        if(rs.getString("schedule_detail_no") == null ) return false;
        
        this.setStaffID(rs.getInt("staff_id_detail"));
        this.setStaffName(0,rs.getString("staff_name1"));
        this.setStaffName(1,rs.getString("staff_name2"));
        this.setScheduleDate(rs.getDate("schedule_date_detail"));
        this.setScheduleDetailNo(rs.getInt("schedule_detail_no"));
        this.setShopID(rs.getInt("shop_id_detail"));
        this.setStartTime(rs.getTime("start_time_detail"));
        this.setEndTime(rs.getTime("end_time_detail"));
        this.setTravelTime1(rs.getInt("travel_time1_detail"));
        this.setTravelTime2(rs.getInt("travel_time2_detail"));
        this.setExtStartTime(rs.getTime("ext_start_time_detail"));
        this.setExtEndTime(rs.getTime("ext_end_time_detail"));
        return true;
    }

    /**
     * regist data table DataScheduleDetail
     * @param con
     * @return
     * @throws SQLException 
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (isExists(con)) {
            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }
        } else if (con.executeUpdate(this.getInsertSQL()) != 1) {
            return false;
        }

        return true;
    }

    /**
     * delete row.
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getDeleteSQL();
        } else {
            return false;
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check exists
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getStaffID() == null || this.getScheduleDate() == null || this.getScheduleDetailNo() == null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * load data
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 取得成功する
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.setData(rs);
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }
    
    public void setMaxScheduleDetailNo(ConnectionWrapper con) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery(getMaxScheduleDetailNoSQL());
        if(rs.next())
        {
            this.setScheduleDetailNo(rs.getInt("max_id"));
        }
        if(this.scheduleDetailNo == null) {
            this.scheduleDetailNo = 1;
        }

        rs.close();
    }
    
    //IVS_NHTVINH start add 2016/10/26 New request #57853 
    //[gb]かんざしAPI用_機能追加（スタッフシフト休憩登録画面）_ヘルプと休憩の重複不可対応
    /**
     * check time overlap, 
     * if time regist is overlap then return true
     * else return false
     */
    public boolean isTimeRegistOverlap(ConnectionWrapper con) throws SQLException {
        if( null != this.getStaffID()
            && this.getStaffID() >= 0 
            && null != this.getScheduleDate() 
            && null != this.getExtStartTime()
            && null != this.getExtEndTime()
            && null != con) {
            
            ResultSetWrapper rs = con.executeQuery(this.getSelectWithTimeSql());
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }
    //IVS_NHTVINH end add 2016/10/26 New request #57853 
    
    private String getMaxScheduleDetailNoSQL()
    {
        return "select max(schedule_detail_no) +1  as max_id\n" +
                "from data_schedule_detail\n"
                + "where staff_id = "+ SQLUtil.convertForSQL(this.staffID) + "\n"
                + "and schedule_date = "+ SQLUtil.convertForSQL(this.scheduleDate) +"\n"
                + "group by staff_id, schedule_date";
    }

    //IVS_NHTVINH start add 2016/10/26 New request #57853 
    //[gb]かんざしAPI用_機能追加（スタッフシフト休憩登録画面）_ヘルプと休憩の重複不可対応
    private String getSelectWithTimeSql(){
        return "select dsd.*\n"
                + " from data_schedule_detail dsd\n"
                + " where staff_id = "+ SQLUtil.convertForSQL(this.staffID) + "\n"
                + " and delete_date is null \n"
                + " and schedule_date = "+ SQLUtil.convertForSQL(this.scheduleDate) + "\n"
                + " and ((ext_start_time >= " + SQLUtil.convertForSQL(this.getExtStartTime()) + "\n" 
                + " and ext_start_time < " + SQLUtil.convertForSQL(this.getExtEndTime()) + ")\n" 
                + " or (ext_start_time <= " + SQLUtil.convertForSQL(this.getExtStartTime()) + "\n" 
                + " and ext_end_time > " + SQLUtil.convertForSQL(this.getExtStartTime()) + "))\n";
    }
    //IVS_NHTVINH end add 2016/10/26 New request #57853 
    
    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        return "select dsd.*\n"
                + "from data_schedule_detail dsd\n"
                + "where staff_id = "+ SQLUtil.convertForSQL(this.staffID) + "\n"
                + "and schedule_date = "+ SQLUtil.convertForSQL(this.scheduleDate) + "\n"
                + "and schedule_detail_no = " + SQLUtil.convertForSQL(this.scheduleDetailNo) + "\n";
    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        return "insert into data_schedule_detail\n"
                + "(staff_id, schedule_date, schedule_detail_no, shop_id, start_time,\n"
                + "end_time, travel_time1, travel_time2, ext_start_time, ext_end_time,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.staffID) + ",\n"
                + SQLUtil.convertForSQL(this.scheduleDate) + ",\n"
                + SQLUtil.convertForSQL(this.scheduleDetailNo) + ",\n"
                + SQLUtil.convertForSQL(this.shopID) + ",\n"
                + SQLUtil.convertForSQL(this.startTime) + ",\n"
                + SQLUtil.convertForSQL(this.endTime) + ",\n"
                + SQLUtil.convertForSQL(this.travelTime1) + ",\n"
                + SQLUtil.convertForSQL(this.travelTime2) + ",\n"
                + SQLUtil.convertForSQL(this.extStartTime) + ",\n"
                + SQLUtil.convertForSQL(this.extEndTime) + ",\n"
                + "current_timestamp, current_timestamp, null";  
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        return "update data_schedule_detail\n"
                + "set\n"
                +"shop_id =" + SQLUtil.convertForSQL(this.shopID) + ",\n"
                +"start_time =" + SQLUtil.convertForSQL(this.startTime) + ",\n"
                +"end_time =" + SQLUtil.convertForSQL(this.endTime) + ",\n"
                +"travel_time1 =" + SQLUtil.convertForSQL(this.travelTime1) + ",\n"
                +"travel_time2 =" + SQLUtil.convertForSQL(this.travelTime2) + ",\n"
                +"ext_start_time =" + SQLUtil.convertForSQL(this.extStartTime) + ",\n"
                +"ext_end_time =" + SQLUtil.convertForSQL(this.extEndTime) + ",\n"
                +"update_date = current_timestamp\n" 
                + "where delete_date is null\n"
                + "and staff_id = "+ SQLUtil.convertForSQL(this.staffID) + "\n"
                + "and schedule_date = "+ SQLUtil.convertForSQL(this.scheduleDate) + "\n"
                + "and schedule_detail_no = " + SQLUtil.convertForSQL(this.scheduleDetailNo) + "\n";
    }

    /**
     * 削除用Update文を取得する。
     *
     * @return 削除用Update文
     */
    private String getDeleteSQL() {
        return "update data_schedule_detail\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where staff_id = "+ SQLUtil.convertForSQL(this.staffID) + "\n"
                + "and schedule_date = "+ SQLUtil.convertForSQL(this.scheduleDate) +"\n"
                + "and schedule_detail_no = " + SQLUtil.convertForSQL(this.scheduleDetailNo) ;
    }

}

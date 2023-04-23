/*
 * DataSchedule.java
 *
 * Created on 2009/04/30, 12:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstShift;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author takeda
 */
public class DataSchedule implements Cloneable{
    
    private Integer   shopId;           /* ショップID         Key */
    private Integer   staffId;          /* スタッフID         Key */
    private Date      scheduleDate;     /* 日付               Key */
//    private Integer   shiftId;          /* スケジュールID         */
    private MstShift  basicshift;       /* 基本シフト         */
//    private String    shiftName;        /* シフト名                */
    private DataRecesses Recesses;        /* 休憩データ             */
    
    private DataScheduleDetail scheduleDetails = new DataScheduleDetail();

    public DataScheduleDetail getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(DataScheduleDetail scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public DataSchedule clone() throws CloneNotSupportedException {
        return (DataSchedule)super.clone();
    }
    
   private String startTime;

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
   private String endTime;
   
    
    /** Creates a new instance of DataSchedule */
    public DataSchedule() {
        basicshift = new MstShift();
        Recesses = new DataRecesses();
    }
    public boolean equals(Object o) {
        if(o != null && o instanceof DataSchedule) {
            DataSchedule sched = (DataSchedule)o;
            if ( sched.getShopId().equals(this.getShopId()) &&
                sched.getStaffId().equals(this.getStaffId()) &&
                sched.getScheduleDate().equals(this.getScheduleDate()) )
            {
                return	true;
            }
            else
            {
                return	false;
            }
        } else {
            return	false;
        }
    }
    

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public MstShift getBasicshift()
    {
        return basicshift;
    }

    public void setBasicshift(MstShift basicshift)
    {
        this.basicshift = basicshift;
    }

    public Integer getShiftId()
    {
        if (this.basicshift == null)
        {
            return null;
        }
        
        return this.basicshift.getShiftId();
    }

    public void setShiftId(Integer shiftId)
    {
        if (this.basicshift == null)
        {
            this.basicshift = new MstShift();
        }
        
        this.basicshift.setShiftId(shiftId);
       
    }
    
    public String getShiftName()
    {
        if (this.basicshift == null)
        {
            return null;
        }
        
        return this.basicshift.getShiftName();
    }
    
    
    
    public void setShiftName(String shiftName)
    {
        if (this.basicshift == null)
        {
            this.basicshift = new MstShift();
        }
        
        this.basicshift.setShiftName(shiftName);
        
    }
    
    public DataRecesses getRecesses() {
        return Recesses;
    }

    public void setRecesses(DataRecesses Recesses) {
        this.Recesses = Recesses;
    }


    
    /**
     * データをクリアする。
     */
    public void clear() {
        this.setShopId(null);
        this.setStaffId(null);
        this.setScheduleDate(null);
        this.setBasicshift(null);
        this.setRecesses(null);
    }
    
    /**
     * スケジュールデータから、設定されているスタッフID、スケジュール日付のデータを読み込む。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(this.getShopId() == null || this.getShopId() < 0)  return	true;
        if(this.getStaffId() == null || this.getStaffId() < 0)  return	true;
        if(this.getScheduleDate() == null)                       return  true;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
            
            while(true){
                DataRecess recess = new DataRecess();
                if( recess.setData(rs) ){
                    this.Recesses.add(recess);
                }
                
                if(!rs.next()) break;
            }
            
        } else {
            return false;
        }
        
        return	true;
    }
    
    //IVS_LVTu start add  2016/11/23 New request #58700
    /**
     * スケジュールデータから、設定されているスタッフID、スケジュール日付のデータを読み込む。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean loadByID(ConnectionWrapper con) throws SQLException {
        if(this.getStaffId() == null || this.getStaffId() < 0)  return	false;
        if(this.getScheduleDate() == null)                       return  false;
        
        if(con == null)	return	false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectByIdSQL());
        
        if(rs.next()) {
            this.setData(rs);
        } else {
            return false;
        }
        
        return	true;
    }
    //IVS_LVTu end add  2016/11/23 New request #58700
    
    /**
     * ResultSetWrapperのデータを読み込む。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setShopId(rs.getInt("shop_id"));
        this.setStaffId(rs.getInt("staff_id"));
        this.setScheduleDate(rs.getDate("schedule_date"));
        this.setShiftId(rs.getInt("shift_id"));
        this.setShiftName(rs.getString("shift_name"));
        this.setStartTime(rs.getString("schedule_start"));
        this.setEndTime(rs.getString("schedule_end"));
        if(rs.getString("shift_name")==null&&rs.getString("schedule_start")!=null&&rs.getString("schedule_end")!=null)
        {
            
            this.getBasicshift().setShiftName("FromToScheduleSetting");
            this.getBasicshift().setStartTime(rs.getString("schedule_start"));
            this.getBasicshift().setEndTime(rs.getString("schedule_end"));
        }
    }
    
    
    /**
     * スケジュールデータにデータを登録する。
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
     * スケジュールデータからデータを削除する。（論理削除）
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
     * スケジュールデータにデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {

        if (this.getShopId() == null || this.getShopId() < 0) return true;
        if (this.getStaffId() == null || this.getStaffId() < 0) return true;
        if (this.getScheduleDate() == null) return true;
        
        if (con == null) return false;
        
        String a = this.getSelectSQL();
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        return rs.next();
    }
    
    /**
     * Select文を取得する。
     * @return Select文
     */
    private String getSelectSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        //sql.append("      ds.*");
        sql.append("      ds.shop_id,ds.staff_id,ds.schedule_date,ds.shift_id,ds.insert_date,ds.update_date,ds.delete_date");
        sql.append("     ,ms.shift_name");
        sql.append("     ,dr.recess_id");
        sql.append("     ,dr.start_time ");
        sql.append("     ,dr.end_time");
        sql.append("     ,ds.start_time as schedule_start");
        sql.append("     ,ds.end_time as schedule_end");
        sql.append("     ,dr.note ");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              coalesce((select shop_id from mst_staff where staff_id = d.staff_id), 0) as shop_id");
        sql.append("             ,d.*");
        sql.append("         from");
        sql.append("             data_schedule d");
        sql.append("     ) ds");
        sql.append("         left join mst_shift ms");
        sql.append("                on ms.shop_id = ds.shop_id");
        sql.append("               and ms.shift_id = ds.shift_id");
        sql.append("         left join data_recess dr");
        sql.append("                on ds.staff_id = dr.staff_id");
        sql.append("               and ds.schedule_date = dr.schedule_date");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ms.delete_date is null");
        sql.append("     and dr.delete_date is null");
        sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("     and ds.staff_id = " + SQLUtil.convertForSQL(this.getStaffId()));
        sql.append("     and ds.schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()));
        sql.append(" order by");
        sql.append("      ds.staff_id");
        sql.append("     ,ds.schedule_date");
        sql.append("     ,ms.shift_name");
        sql.append("     ,dr.recess_id");

        return sql.toString();

    }
    
    /**
     * Insert文を取得する。
     * @return Insert文
     */
    private String	getInsertSQL() {
        return	"insert into data_schedule \n" +
                "(staff_id, schedule_date, shift_id, \n" +
                "insert_date, update_date, delete_date) \n" +
                "values \n" +
                "( \n" +
                SQLUtil.convertForSQL(this.getStaffId()) + ", \n" +
                SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + ", \n" +
                SQLUtil.convertForSQL(this.getShiftId()) + ", \n" +
                "current_timestamp, current_timestamp, null \n" +
                ")\n";
    }
    
    /**
     * Update文を取得する。
     * @return Update文
     */
    private String	getUpdateSQL() {
        return	"update data_schedule\n" +
                "set \n" +
                "shift_id   = " + SQLUtil.convertForSQL(this.getShiftId()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n";
    }
    
    /**
     * 削除用Update文を取得する。
     * @return 削除用Update文
     */
    private String	getDeleteSQL() {
        return	"update data_schedule\n" +
                "set\n" +
                "update_date = current_timestamp,\n" +
                "delete_date = current_timestamp\n" +
                "where  delete_date is null\n" +
                "and    staff_id      = " + SQLUtil.convertForSQL(this.getStaffId()) + "\n" +
                "and    schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()) + "\n";
    }
    
    //IVS_LVTu start add  2016/11/23 New request #58700
    /**
     * Select文を取得する。
     * @return Select文
     */
    private String getSelectByIdSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     ds.*");
        sql.append("     ,staff.shop_id");
        sql.append("     ,ms.shift_name");
        sql.append("     ,ms.start_time as schedule_start");
        sql.append("     ,ms.end_time as schedule_end");
        sql.append(" from");
        sql.append(" data_schedule ds");
        sql.append(" inner join mst_staff staff on ds.staff_id = staff.staff_id");
        sql.append(" inner join mst_shift ms");
        sql.append(" on ms.shop_id = staff.shop_id");
        sql.append(" and ms.shift_id = ds.shift_id");
        sql.append(" where");
        sql.append("     ds.delete_date is null");
        sql.append("     and ms.delete_date is null");
        sql.append("     and ds.staff_id = ").append(SQLUtil.convertForSQL(this.getStaffId()));
        sql.append("     and ds.schedule_date = ").append(SQLUtil.convertForSQLDateOnly(this.getScheduleDate()));

        return sql.toString();
    }
    
    //IVS_LVTu end add  2016/11/23 New request #58700

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }


    
}
